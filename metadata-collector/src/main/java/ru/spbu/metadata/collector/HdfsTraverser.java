package ru.spbu.metadata.collector;

import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.Streams;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.SeekableInputStream;
import ru.spbu.metadata.collector.filemeta.FileMeta;
import ru.spbu.metadata.collector.filemeta.FileMetaFactory;
import ru.spbu.metadata.collector.filemeta.FileMetaFactoryChooser;
import ru.spbu.metadata.collector.filemeta.FileStats;

public class HdfsTraverser implements Traverser {
    private final FileSystem fs;
    private final FileMetaFactoryChooser fileMetaFactoryChooser;

    public HdfsTraverser(FileSystem fs, FileMetaFactoryChooser fileMetaFactoryChooser) {
        this.fs = fs;
        this.fileMetaFactoryChooser = fileMetaFactoryChooser;
    }

    public Stream<FileMeta> traverse(String root) {
        FsIterator fsIterator = new FsIterator(fs, new Path(root));

        return Streams.stream(fsIterator)
                .map(this::mapper);
    }

    private FileMeta mapper(FileStatus hadoopFileStatus) {
        HadoopInputFile hadoopInputFile;
        try {
            hadoopInputFile = HadoopInputFile.fromStatus(hadoopFileStatus, fs.getConf());
        } catch (IOException e) {
            throw new RuntimeException("Could not create hadoopInputFile", e);
        }

        Supplier<SeekableInputStream> inputStreamSupplier = () -> {
            try {
                return hadoopInputFile.newStream();
            } catch (IOException e) {
                throw new RuntimeException("Could not create inputStream", e);
            }
        };

        String filePath = hadoopFileStatus.getPath().toUri().getPath();
        FileStats fileStats = new FileStats(filePath, hadoopFileStatus.getLen(), hadoopFileStatus.isDirectory());
        FileMetaFactory fileMetaFactory = fileMetaFactoryChooser.choose(fileStats, inputStreamSupplier);
        return fileMetaFactory.createFileMeta(fileStats, inputStreamSupplier);
    }

    private static class FsIterator implements Iterator<FileStatus> {
        private final Stack<RemoteIterator<FileStatus>> iterators = new Stack<>();
        private final FileSystem fs;
        private RemoteIterator<FileStatus> curIterator;
        private FileStatus curFile;

        public FsIterator(FileSystem fs, Path root) {
            this.fs = fs;
            this.curIterator = listStatusIterator(root);
        }

        @Override
        public boolean hasNext() {
            while (curFile == null) {
                try {
                    if (curIterator.hasNext()) {
                        handleFileStat(curIterator.next());
                    } else if (!iterators.empty()) {
                        curIterator = iterators.pop();
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Can not get next file", e);
                }
            }
            return true;
        }

        private void handleFileStat(FileStatus stat) {
            curFile = stat;
            if (stat.isDirectory()) {
                iterators.push(listStatusIterator(stat.getPath()));
            }
        }

        @Override
        public FileStatus next() {
            if (curFile == null) {
                throw new IllegalStateException("Call and check hasNext() before next()");
            }

            FileStatus result = curFile;
            curFile = null;
            return result;
        }

        private RemoteIterator<FileStatus> listStatusIterator(Path path) {
            try {
                return this.fs.listStatusIterator(path);
            } catch (IOException e) {
                throw new RuntimeException("Can not get files status iterator", e);
            }
        }
    }
}
