package ru.spbu.metadata.collector;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.parquet.hadoop.util.HadoopInputFile;

public class HdfsTraverser {
    private final FileSystem fs;
    private final ObjectMapper objectMapper;

    public HdfsTraverser(FileSystem fs, ObjectMapper objectMapper) {
        this.fs = fs;
        this.objectMapper = objectMapper;
    }

    public Stream<Node> traverse(Path root) throws IOException {
        FsIterator fsIterator = new FsIterator(fs.listFiles(root, true));

        return Streams.stream(fsIterator)
                .map(this::toNode);
    }

    private Node toNode(LocatedFileStatus fileStatus) {
        HadoopInputFile inputFile = null;
        try {
            inputFile = HadoopInputFile.fromStatus(fileStatus, fs.getConf());

            try (var reader = ParquetFactory.createParquetReader(inputFile)) {
                GenericRecord record = reader.read();

                return new Node(
                        fileStatus.getPath().toUri().getPath(),
                        objectMapper.readTree(record.getSchema().toString())
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Read input file exception", e);
        }
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
