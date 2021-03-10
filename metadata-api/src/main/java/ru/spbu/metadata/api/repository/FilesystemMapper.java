package ru.spbu.metadata.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.common.domain.Filesystem;

@Component
public class FilesystemMapper implements RowMapper<Filesystem> {

    @Override
    public Filesystem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Filesystem(
                rs.getInt("fs_id"),
                rs.getInt("active_ver"),
                rs.getString("name"),
                rs.getString("url"),
                rs.getTimestamp("create_time").toLocalDateTime(),
                rs.getTimestamp("update_time").toLocalDateTime()
        );
    }
}
