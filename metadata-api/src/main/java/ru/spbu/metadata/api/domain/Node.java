package ru.spbu.metadata.api.domain;

import java.time.LocalDateTime;

public class Node {
    private final Integer filesystemId;
    private final String path;
    private final int version;
    private final String meta;
    private final LocalDateTime createTime;

    public Node(Integer filesystemId, String path, int version, String meta, LocalDateTime createTime) {
        this.filesystemId = filesystemId;
        this.path = path;
        this.version = version;
        this.meta = meta;
        this.createTime = createTime;
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

    public String getMeta() {
        return meta;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
