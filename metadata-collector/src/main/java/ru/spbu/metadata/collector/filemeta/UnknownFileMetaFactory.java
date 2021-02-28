package ru.spbu.metadata.collector.filemeta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.common.domain.FileType;

@Component
public class UnknownFileMetaFactory extends FileMetaFactory {
    public UnknownFileMetaFactory(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public FileMeta createFileMeta(FileSystem fs, LocatedFileStatus hadoopFileStatus) {
        return new FileMeta(
                getFilePath(hadoopFileStatus.getPath()),
                objectMapper.createObjectNode(),
                false,
                FileType.UNKNOWN
        );
    }
}
