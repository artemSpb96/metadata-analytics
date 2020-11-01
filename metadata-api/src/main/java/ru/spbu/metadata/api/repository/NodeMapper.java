package ru.spbu.metadata.api.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.api.domain.Node;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NodeMapper implements RowMapper<Node> {

    @Override
    public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Node(
                rs.getInt("fs_id"),
                rs.getString("path"),
                rs.getInt("ver"),
                rs.getString("meta"),
                rs.getTimestamp("create_time").toLocalDateTime()
        );
    }
}
