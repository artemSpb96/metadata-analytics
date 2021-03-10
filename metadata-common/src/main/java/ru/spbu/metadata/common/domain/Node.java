package ru.spbu.metadata.common.domain;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;

public class Node {
    private final Integer filesystemId;
    private final String path;
    private final int startVersion;
    private final int version;
    private final JsonNode meta;
    private final LocalDateTime createTime;
    private final boolean isDir;
    private final FileType fileType;

    @JsonCreator
    public Node(
            @JsonProperty("filesystemId") Integer filesystemId,
            @JsonProperty("path") String path,
            @JsonProperty("startVersion") int startVersion,
            @JsonProperty("version") int version,
            @JsonProperty("meta") JsonNode meta,
            @JsonProperty("createTime") LocalDateTime createTime,
            @JsonProperty("isDir") boolean isDir,
            @JsonProperty("fileType") FileType fileType
    ) {
        this.filesystemId = filesystemId;
        this.path = path;
        this.startVersion = startVersion;
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

    public int getStartVersion() {
        return startVersion;
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

    @JsonProperty("isDir")
    public boolean isDir() {
        return isDir;
    }

    public FileType getFileType() {
        return fileType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("filesystemId", filesystemId)
                .add("path", path)
                .add("startVersion", startVersion)
                .add("version", version)
                .add("meta", meta)
                .add("createTime", createTime)
                .add("isDir", isDir)
                .add("fileType", fileType)
                .toString();
    }
}
