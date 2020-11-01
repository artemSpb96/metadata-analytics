package ru.spbu.metadata.api.web.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class NodeTo {
    private Integer filesystemId;
    private String path;
    private int version;
    private JsonNode meta;
    private LocalDateTime createTime;

    public Integer getFilesystemId() {
        return filesystemId;
    }

    public NodeTo setFilesystemId(Integer filesystemId) {
        this.filesystemId = filesystemId;
        return this;
    }

    public String getPath() {
        return path;
    }

    public NodeTo setPath(String path) {
        this.path = path;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public NodeTo setVersion(int version) {
        this.version = version;
        return this;
    }

    public JsonNode getMeta() {
        return meta;
    }

    public NodeTo setMeta(JsonNode meta) {
        this.meta = meta;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public NodeTo setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }
}
