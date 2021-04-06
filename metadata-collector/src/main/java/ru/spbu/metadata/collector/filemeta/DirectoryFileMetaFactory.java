package ru.spbu.metadata.collector.filemeta;

import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.parquet.io.SeekableInputStream;
import org.springframework.stereotype.Component;

@Component
public class DirectoryFileMetaFactory extends FileMetaFactory {
    public DirectoryFileMetaFactory(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public FileMeta createFileMeta(FileStats fileStats, Supplier<SeekableInputStream> inputStreamSupplier) {
        return new FileMeta(
                fileStats.getPath(),
                objectMapper.createObjectNode(),
                true,
                null
        );
    }
}
