package ru.spbu.metadata.collector;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.google.common.collect.Streams;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import ru.spbu.metadata.collector.filemeta.FileMeta;
import ru.spbu.metadata.collector.filemeta.FileMetaFactoryChooser;

public class HdfsTraverser {
    private final FileSystem fs;
    private final FileMetaFactoryChooser fileMetaFactoryChooser;

    public HdfsTraverser(FileSystem fs, FileMetaFactoryChooser fileMetaFactoryChooser) {
        this.fs = fs;
        this.fileMetaFactoryChooser = fileMetaFactoryChooser;
    }

    public Stream<FileMeta> traverse(Path root) throws IOException {
        FsIterator fsIterator = new FsIterator(fs.listFiles(root, true));

        return Streams.stream(fsIterator)
                .map(hadoopFileStatus ->
                        fileMetaFactoryChooser.choose(fs, hadoopFileStatus).createFileMeta(fs, hadoopFileStatus));
    }

    private static class FsIterator implements Iterator<LocatedFileStatus> {
        private final RemoteIterator<LocatedFileStatus> remoteIterator;

        public FsIterator(RemoteIterator<LocatedFileStatus> remoteIterator) {
            this.remoteIterator = remoteIterator;
        }

        @Override
        public boolean hasNext() {
            try {
                return remoteIterator.hasNext();
            } catch (IOException e) {
                throw new RuntimeException("Remote iterator exception", e);
            }
        }

        @Override
        public LocatedFileStatus next() {
            try {
                return remoteIterator.next();
            } catch (IOException e) {
                throw new RuntimeException("Remote iterator exception", e);
            }
        }
    }
}
