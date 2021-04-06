package ru.spbu.metadata.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.spbu.metadata.common.domain.Filesystem;

@Repository
public class FilesystemRepository {
    private static final String INSERT_FILESYSTEM_QUERY = "" +
            "INSERT INTO filesystem(active_ver, name, url, create_time, update_time) " +
            "VALUES (:activeVer, :name, :url, :createTime, :updateTime) " +
            "RETURNING fs_id;";

    private static final String UPDATE_ACTIVE_VERSION_QUERY = "" +
            "UPDATE filesystem " +
            "SET active_ver = :activeVer, " +
            "    update_time = :updateTime " +
            "WHERE fs_id = :id;";

    private static final String SELECT_FILESYSTEM_BY_ID = "" +
            "SELECT fs_id, active_ver, name, url, create_time, update_time " +
            "FROM filesystem " +
            "WHERE fs_id = :filesystemId;";

    private static final String SELECT_ALL_FILESYSTEMS = "" +
            "SELECT fs_id, active_ver, name, url, create_time, update_time " +
            "FROM filesystem;";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final FilesystemMapper filesystemMapper;

    public FilesystemRepository(NamedParameterJdbcTemplate jdbcTemplate, FilesystemMapper filesystemMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filesystemMapper = filesystemMapper;
    }

    public Optional<Filesystem> findFilesystem(int filesystemId) {
        List<Filesystem> filesystems = jdbcTemplate.query(
                SELECT_FILESYSTEM_BY_ID,
                new MapSqlParameterSource("filesystemId", filesystemId),
                filesystemMapper
        );

        return filesystems.isEmpty() ? Optional.empty() : Optional.of(filesystems.get(0));
    }

    public List<Filesystem> findAll() {
        return jdbcTemplate.query(SELECT_ALL_FILESYSTEMS, filesystemMapper);
    }

    public Integer createFilesystem(Filesystem filesystem) {
        return jdbcTemplate.queryForObject(
                INSERT_FILESYSTEM_QUERY,
                new MapSqlParameterSource()
                        .addValue("activeVer", filesystem.getActiveVersion())
                        .addValue("name", filesystem.getName())
                        .addValue("url", filesystem.getUrl())
                        .addValue("createTime", filesystem.getCreateTime())
                        .addValue("updateTime", filesystem.getUpdateTime()),
                Integer.class
        );
    }

    public void updateActiveVersion(int id, int activeVersion, LocalDateTime updateTime) {
        jdbcTemplate.update(
                UPDATE_ACTIVE_VERSION_QUERY,
                new MapSqlParameterSource()
                        .addValue("activeVer", activeVersion)
                        .addValue("updateTime", updateTime)
                        .addValue("id", id)
        );
    }
}
