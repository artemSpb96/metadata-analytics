package ru.spbu.metadata.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class NodeCreationParams {
    private final JsonNode meta;
    private final String path;
    private final boolean isDir;
    private final FileType fileType;

    @JsonCreator
    public NodeCreationParams(
            @JsonProperty("meta") JsonNode meta,
            @JsonProperty("path") String path,
            @JsonProperty("isDir") boolean isDir,
            @JsonProperty("fileType") FileType fileType
    ) {
        this.meta = meta;
        this.path = path;
        this.isDir = isDir;
        this.fileType = fileType;
    }

    public JsonNode getMeta() {
        return meta;
    }

    public String getPath() {
        return path;
    }

    @JsonProperty("isDir")
    public boolean isDir() {
        return isDir;
    }

    public FileType getFileType() {
        return fileType;
    }
}
