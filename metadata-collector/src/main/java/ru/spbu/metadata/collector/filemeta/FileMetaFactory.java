package ru.spbu.metadata.collector.filemeta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;

public abstract class FileMetaFactory {
    protected final ObjectMapper objectMapper;

    public FileMetaFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract FileMeta createFileMeta(FileSystem fs, LocatedFileStatus hadoopFileStatus);

    protected String getFilePath(Path hadoopPath) {
        return hadoopPath.toUri().getPath();
    }
}
