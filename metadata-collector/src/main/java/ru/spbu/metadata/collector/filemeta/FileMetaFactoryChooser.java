package ru.spbu.metadata.collector.filemeta;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.parquet.hadoop.util.HadoopInputFile;
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

    public FileMetaFactory choose(FileSystem fs, LocatedFileStatus hadoopFileStatus) {
        if (hadoopFileStatus.isDirectory()) {
            return directoryFileMetaFactory;
        }

        HadoopInputFile hadoopInputFile;
        try {
            hadoopInputFile = HadoopInputFile.fromStatus(hadoopFileStatus, fs.getConf());
        } catch (IOException e) {
            throw new RuntimeException("Can not read hadoop file", e);
        }

        ByteBuffer buffer = ByteBuffer.allocate(4);
        int bytesRead;
        try {
            bytesRead = hadoopInputFile.newStream().read(buffer);
        } catch (IOException e) {
            throw new RuntimeException("Can not create stream for hadoop file", e);
        }

        buffer.rewind();
        String fileHeader = StandardCharsets.US_ASCII.decode(buffer).toString();
        log.debug("Input file: {}, header: {}", hadoopInputFile, fileHeader);

        if (bytesRead == 4 && fileHeader.equals(PARQUET_FILE_MAGIC_NUMBER)) {
            return parquetFileMetaFactory;
        }

        return unknownFileMetaFactory;
    }
}
