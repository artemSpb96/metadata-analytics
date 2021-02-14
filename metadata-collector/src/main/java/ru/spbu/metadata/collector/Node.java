package ru.spbu.metadata.collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;

public class Node {
    private final String path;
    private final JsonNode meta;

    public Node(String path, JsonNode meta) {
        this.path = path;
        this.meta = meta;
    }

    public String getPath() {
        return path;
    }

    public JsonNode getMeta() {
        return meta;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path)
                .add("meta", meta)
                .toString();
    }
}
