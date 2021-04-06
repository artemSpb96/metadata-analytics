package ru.spbu.metadata.collector.filemeta;

import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.parquet.io.SeekableInputStream;

public abstract class FileMetaFactory {
    protected final ObjectMapper objectMapper;

    public FileMetaFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract FileMeta createFileMeta(FileStats fileStats, Supplier<SeekableInputStream> inputStreamSupplier);
}
