package ru.spbu.metadata.collector.filemeta;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.SeekableInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileMetaFactoryChooser {
    private static final Logger log = LoggerFactory.getLogger(FileMetaFactoryChooser.class);

    private static final String PARQUET_FILE_MAGIC_NUMBER = "PAR1";

    private final DirectoryFileMetaFactory directoryFileMetaFactory;
    private final ParquetFileMetaFactory parquetFileMetaFactory;
    private final UnknownFileMetaFactory unknownFileMetaFactory;

    public FileMetaFactoryChooser(
            DirectoryFileMetaFactory directoryFileMetaFactory,
            ParquetFileMetaFactory parquetFileMetaFactory,
            UnknownFileMetaFactory unknownFileMetaFactory
    ) {
        this.directoryFileMetaFactory = directoryFileMetaFactory;
        this.parquetFileMetaFactory = parquetFileMetaFactory;
        this.unknownFileMetaFactory = unknownFileMetaFactory;
    }

    public FileMetaFactory choose(FileStats fileStats, Supplier<SeekableInputStream> inputStreamSupplier) {
        if (fileStats.isDirectory()) {
            return directoryFileMetaFactory;
        }

        SeekableInputStream inputStream = inputStreamSupplier.get();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        int bytesRead;
        try {
            bytesRead = inputStream.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException("Can not create stream for hadoop file", e);
        }

        buffer.rewind();
        String fileHeader = StandardCharsets.US_ASCII.decode(buffer).toString();
        log.debug("Input file: {}, header: {}", fileStats.getPath(), fileHeader);

        if (bytesRead == 4 && fileHeader.equals(PARQUET_FILE_MAGIC_NUMBER)) {
            return parquetFileMetaFactory;
        }

        return unknownFileMetaFactory;
    }
}
