package ru.spbu.metadata.analytics.api.domain;

import com.fasterxml.jackson.databind.JsonNode;

public class NodeDiff {
    private final JsonNode fileAttrsDiff;
    private final JsonNode metaDiff;

    public NodeDiff(JsonNode fileAttrsDiff, JsonNode metaDiff) {
        this.fileAttrsDiff = fileAttrsDiff;
        this.metaDiff = metaDiff;
    }

    public JsonNode getFileAttrsDiff() {
        return fileAttrsDiff;
    }

    public JsonNode getMetaDiff() {
        return metaDiff;
    }
}
