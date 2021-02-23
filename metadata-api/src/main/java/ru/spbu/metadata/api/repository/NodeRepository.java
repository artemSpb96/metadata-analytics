package ru.spbu.metadata.api.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.spbu.metadata.common.domain.Node;

@Repository
public class NodeRepository {
    private static final String SELECT_NODE_QUERY = "" +
            "SELECT fs_id, path, ver, meta, create_time, is_dir, file_type " +
            "FROM node " +
            "WHERE fs_id = :filesystemId AND path = :path AND ver <= :version " +
            "ORDER BY ver DESC " +
            "LIMIT 1;";

    private static final String SELECT_CHILDREN_NODES_QUERY = "" +
            "WITH selected_nodes AS (SELECT fs_id, path, MAX(ver) AS ver " +
            "FROM node " +
            "WHERE fs_id = :filesystemId AND path LIKE :basePath AND ver <= :version " +
            "GROUP BY fs_id, path) " +
            "SELECT n.fs_id, n.path, n.ver, n.meta, n.create_time, n.is_dir, n.file_type " +
            "FROM node AS n " +
            "JOIN selected_nodes AS sn ON sn.fs_id = n.fs_id AND sn.path = n.path AND sn.ver = n.ver;";

    private static final String INSERT_NODE_QUERY = "" +
            "INSERT INTO node(fs_id, path, ver, meta, create_time, is_dir, file_type) " +
            "VALUES (:filesystemId, :path, :version, to_json(:meta::json), :createTime, :isDir, :fileType);";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final NodeMapper nodeMapper;
    private final ObjectMapper objectMapper;

    public NodeRepository(NamedParameterJdbcTemplate jdbcTemplate, NodeMapper nodeMapper, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.nodeMapper = nodeMapper;
        this.objectMapper = objectMapper;
    }

    public Optional<Node> findNode(int filesystemId, String path, int version) {
        List<Node> nodes = jdbcTemplate.query(
                SELECT_NODE_QUERY,
                Map.of("filesystemId", filesystemId, "path", path, "version", version),
                nodeMapper
        );

        return nodes.isEmpty() ? Optional.empty() : Optional.of(nodes.get(0));
    }

    public List<Node> findChildrenNodes(int filesystemId, String basePath, int version) {
        //TODO: escape basePath
        //TODO: find children after {basePath}/
        return jdbcTemplate.query(
                SELECT_CHILDREN_NODES_QUERY,
                Map.of("filesystemId", filesystemId, "basePath", basePath + "%", "version", version),
                nodeMapper
        );
    }

    public void save(Node node) {
        String meta;
        try {
            meta = objectMapper.writeValueAsString(node.getMeta());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Could not transform json to string: %s", node.getMeta()), e);
        }

        jdbcTemplate.update(
                INSERT_NODE_QUERY,
                Map.of(
                        "filesystemId", node.getFilesystemId(),
                        "path", node.getPath(),
                        "version", node.getVersion(),
                        "meta", meta,
                        "createTime", node.getCreateTime(),
                        "isDir", node.isDir(),
                        "fileType", node.getFileType().name()
                )
        );
    }
}
