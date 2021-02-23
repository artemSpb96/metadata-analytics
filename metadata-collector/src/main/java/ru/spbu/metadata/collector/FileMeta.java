package ru.spbu.metadata.collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;
import ru.spbu.metadata.common.domain.FileType;

public class FileMeta {
    private final String path;
    private final JsonNode meta;
    private final boolean isDir;
    private final FileType fileType;

    public FileMeta(String path, JsonNode meta, boolean isDir, FileType fileType) {
        this.path = path;
        this.meta = meta;
        this.isDir = isDir;
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public JsonNode getMeta() {
        return meta;
    }

    public boolean isDir() {
        return isDir;
    }

    public FileType getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path)
                .add("meta", meta)
                .add("isDir", isDir)
                .add("fileType", fileType)
                .toString();
    }
}
