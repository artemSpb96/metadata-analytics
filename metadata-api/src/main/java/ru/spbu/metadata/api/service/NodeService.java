package ru.spbu.metadata.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spbu.metadata.api.repository.NodeRepository;
import ru.spbu.metadata.common.domain.Node;
import ru.spbu.metadata.common.domain.NodeCreationParams;

@Service
public class NodeService {
    private static final Logger log = LoggerFactory.getLogger(NodeService.class);

    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    private static boolean isNodeNotChanged(Node oldNode, Node newNode) {
        return oldNode.getFileType() == newNode.getFileType() &&
                oldNode.getMeta().equals(newNode.getMeta()) &&
                oldNode.isDir() == newNode.isDir();
    }

    public Optional<Node> findNode(int filesystemId, String path, int version) {
        return nodeRepository.findNode(filesystemId, path, version);
    }

    public List<Node> findChildrenNodes(int filesystemId, String basePath, int version) {
        return nodeRepository.findChildrenNodes(filesystemId, basePath, version);
    }

    @Transactional
    public void saveNode(int filesystemId, int version, NodeCreationParams nodeCreationParams) {
        Optional<Node> oldNodeOpt = nodeRepository.findNode(filesystemId, nodeCreationParams.getPath(), version);

        Node newNode = new Node(
                filesystemId,
                nodeCreationParams.getPath(),
                version,
                nodeCreationParams.getMeta(),
                LocalDateTime.now(),
                nodeCreationParams.isDir(),
                nodeCreationParams.getFileType()
        );

        if (oldNodeOpt.isEmpty()) {
            nodeRepository.createNode(newNode);
            log.info("Create new node: {}", newNode);
            return;
        }

        if (isNodeNotChanged(oldNodeOpt.get(), newNode)) {
            log.info("Do not save node  duplicated node of {}", oldNodeOpt.get());
            return;
        }

        if (oldNodeOpt.get().getVersion() != newNode.getVersion()) {
            nodeRepository.createNode(newNode);
            log.info("Create node {} cause it differs from previous version {}", newNode, oldNodeOpt.get());
        } else {
            nodeRepository.updateNode(newNode);
            log.info("Update node {} cause it differs from node with same version {}", newNode, oldNodeOpt.get());
        }
    }
}
