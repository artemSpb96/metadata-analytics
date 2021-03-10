package ru.spbu.metadata.analytics.api.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.JsonDiff;
import org.springframework.stereotype.Service;
import ru.spbu.metadata.analytics.api.domain.NodeDiff;
import ru.spbu.metadata.common.MetadataApiClient;
import ru.spbu.metadata.common.domain.FileType;
import ru.spbu.metadata.common.domain.Filesystem;
import ru.spbu.metadata.common.domain.Node;

@Service
public class AnalyticsService {
    private final MetadataApiClient metadataApiClient;
    private final ObjectMapper objectMapper;

    public AnalyticsService(MetadataApiClient metadataApiClient, ObjectMapper objectMapper) {
        this.metadataApiClient = metadataApiClient;
        this.objectMapper = objectMapper;
    }

    public List<Node> findDirectoriesWithConsistentMetaFiles(int filesystemId, FileType fileType) {
        Filesystem filesystem = metadataApiClient.getFilesystem(filesystemId).orElseThrow();

        List<Node> fittedNodes = new LinkedList<>();
        Node rootNode = new Node(
                filesystemId,
                "/",
                0,
                0,
                null,
                null,
                true,
                null
        );
        dfs(rootNode, fileType, filesystem.getActiveVersion(), fittedNodes);

        return fittedNodes;
    }

    public NodeDiff findDiff(int filesystemId, String path, int fromVersion, int toVersion) {
        Optional<Node> fromNodeOpt = metadataApiClient.getNode(filesystemId, path, fromVersion);
        Optional<Node> toNodeOpt = metadataApiClient.getNode(filesystemId, path, toVersion);

        ObjectNode fromNodeFileAttrs = objectMapper.createObjectNode();
        fromNodeFileAttrs.put("isDir", fromNodeOpt.map(Node::isDir).orElse(null));
        fromNodeFileAttrs.put("fileType", fromNodeOpt.map(o -> o.getFileType().name()).orElse(null));

        ObjectNode toNodeFileAttrs = objectMapper.createObjectNode();
        toNodeFileAttrs.put("isDir", toNodeOpt.map(Node::isDir).orElse(null));
        toNodeFileAttrs.put("fileType", toNodeOpt.map(o -> o.getFileType().name()).orElse(null));


        JsonNode fileAttrsDiff = JsonDiff.asJson(fromNodeFileAttrs, toNodeFileAttrs);
        JsonNode metaDiff = JsonDiff.asJson(
                fromNodeOpt.map(Node::getMeta).orElseGet(objectMapper::createObjectNode),
                toNodeOpt.map(Node::getMeta).orElseGet(objectMapper::createObjectNode)
        );

        return new NodeDiff(fileAttrsDiff, metaDiff);
    }

    private void dfs(Node node, FileType fileType, int version, List<Node> fittedNodes) {
        List<Node> childrenNodes = metadataApiClient.getChildrenNodes(
                node.getFilesystemId(),
                version,
                node.getPath()
        );

        if (childrenNodes.isEmpty()) {
            return;
        }

        JsonNode fileMeta = childrenNodes.get(0).getMeta();
        boolean isConsistentMeta = true;
        boolean hasChildrenDirs = false;
        boolean isAllFilesOfGivenType = true;
        for (Node childrenNode : childrenNodes) {
            if (childrenNode.isDir()) {
                hasChildrenDirs = true;
                dfs(childrenNode, fileType, version, fittedNodes);
            } else if (childrenNode.getFileType() != fileType) {
                isAllFilesOfGivenType = false;
            } else if (!fileMeta.equals(childrenNode.getMeta())) {
                isConsistentMeta = false;
            }
        }

        if (!hasChildrenDirs && isConsistentMeta && isAllFilesOfGivenType) {
            fittedNodes.add(node);
        }
    }
}
