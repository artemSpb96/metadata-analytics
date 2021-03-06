package ru.spbu.metadata.collector.filemeta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.stereotype.Component;

@Component
public class DirectoryFileMetaFactory extends FileMetaFactory {
    public DirectoryFileMetaFactory(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public FileMeta createFileMeta(FileSystem fs, FileStatus hadoopFileStatus) {
        return new FileMeta(
                getFilePath(hadoopFileStatus.getPath()),
                objectMapper.createObjectNode(),
                true,
                null
        );
    }
}
