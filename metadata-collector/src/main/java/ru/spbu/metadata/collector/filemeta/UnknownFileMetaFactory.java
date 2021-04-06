package ru.spbu.metadata.collector.filemeta;

import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.parquet.io.SeekableInputStream;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.common.domain.FileType;

@Component
public class UnknownFileMetaFactory extends FileMetaFactory {
    public UnknownFileMetaFactory(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public FileMeta createFileMeta(FileStats fileStats, Supplier<SeekableInputStream> inputStreamSupplier) {
        return new FileMeta(
                fileStats.getPath(),
                objectMapper.createObjectNode(),
                false,
                FileType.UNKNOWN
        );
    }
}
