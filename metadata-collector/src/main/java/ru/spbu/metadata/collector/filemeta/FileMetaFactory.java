package ru.spbu.metadata.collector.filemeta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public abstract class FileMetaFactory {
    protected final ObjectMapper objectMapper;

    public FileMetaFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract FileMeta createFileMeta(FileSystem fs, FileStatus hadoopFileStatus);

    protected String getFilePath(Path hadoopPath) {
        return hadoopPath.toUri().getPath();
    }
}
