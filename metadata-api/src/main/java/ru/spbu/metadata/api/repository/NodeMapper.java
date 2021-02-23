package ru.spbu.metadata.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.common.domain.FileType;
import ru.spbu.metadata.common.domain.Node;

@Component
public class NodeMapper implements RowMapper<Node> {
    private final ObjectMapper objectMapper;

    public NodeMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
        String metaString = rs.getString("meta");

        JsonNode meta;
        try {
            meta = objectMapper.readTree(metaString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Could not transform string %s to json", metaString), e);
        }

        return new Node(
                rs.getInt("fs_id"),
                rs.getString("path"),
                rs.getInt("ver"),
                meta,
                rs.getTimestamp("create_time").toLocalDateTime(),
                rs.getBoolean("is_dir"),
                FileType.fromString(rs.getString("file_type"))
        );
    }
}
