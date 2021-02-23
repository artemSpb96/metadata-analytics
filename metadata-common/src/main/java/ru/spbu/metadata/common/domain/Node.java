package ru.spbu.metadata.common.domain;


import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;

public class Node {
    private final Integer filesystemId;
    private final String path;
    private final int version;
    private final JsonNode meta;
    private final LocalDateTime createTime;
    private final boolean isDir;
    private final FileType fileType;

    public Node(
            Integer filesystemId,
            String path,
            int version,
            JsonNode meta,
            LocalDateTime createTime,
            boolean isDir,
            FileType fileType
    ) {
        this.filesystemId = filesystemId;
        this.path = path;
        this.version = version;
        this.meta = meta;
        this.createTime = createTime;
        this.isDir = isDir;
        this.fileType = fileType;
    }

    public Integer getFilesystemId() {
        return filesystemId;
    }

    public String getPath() {
        return path;
    }

    public int getVersion() {
        return version;
    }

    public JsonNode getMeta() {
        return meta;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public boolean isDir() {
        return isDir;
    }

    public FileType getFileType() {
        return fileType;
    }
}
