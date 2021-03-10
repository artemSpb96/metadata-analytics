package ru.spbu.metadata.analytics.api.service;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import ru.spbu.metadata.common.MetadataApiClient;
import ru.spbu.metadata.common.domain.FileType;
import ru.spbu.metadata.common.domain.Filesystem;
import ru.spbu.metadata.common.domain.Node;

@Service
public class AnalyticsService {
    private final MetadataApiClient metadataApiClient;

    public AnalyticsService(MetadataApiClient metadataApiClient) {
        this.metadataApiClient = metadataApiClient;
    }

    public List<Node> findDirectoriesWithConsistentMetaFiles(int filesystemId, FileType fileType) {
        Filesystem filesystem = metadataApiClient.getFilesystem(filesystemId);

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

    public void dfs(Node node, FileType fileType, int version, List<Node> fittedNodes) {
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
