package ru.spbu.metadata.api.web.transformer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.spbu.metadata.api.domain.Node;
import ru.spbu.metadata.api.web.dto.NodeCreationParamsTo;
import ru.spbu.metadata.api.web.dto.NodeTo;

import java.io.IOException;

@Component
public class NodeTransformer {
    private final ObjectMapper objectMapper;

    public NodeTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public NodeTo toDto(Node node) {
        return new NodeTo()
                .setFilesystemId(node.getFilesystemId())
                .setPath(node.getPath())
                .setVersion(node.getVersion())
                .setMeta(toJson(node.getMeta()))
                .setCreateTime(node.getCreateTime());
    }

    public String toMetadata(NodeCreationParamsTo nodeCreationParamsTo) {
        return toString(nodeCreationParamsTo.getMeta());
    }

    private JsonNode toJson(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }

        try {
            return objectMapper.readTree(str);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not transform string %s to json", str));
        }
    }

    private String toString(JsonNode json) {
        if (json == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not transform json to string");
        }
    }
}
