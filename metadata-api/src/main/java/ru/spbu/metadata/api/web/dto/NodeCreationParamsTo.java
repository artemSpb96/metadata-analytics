package ru.spbu.metadata.api.web.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class NodeCreationParamsTo {
    private JsonNode meta;
    private String path;

    public JsonNode getMeta() {
        return meta;
    }

    public NodeCreationParamsTo setMeta(JsonNode meta) {
        this.meta = meta;
        return this;
    }

    public String getPath() {
        return path;
    }

    public NodeCreationParamsTo setPath(String path) {
        this.path = path;
        return this;
    }
}
