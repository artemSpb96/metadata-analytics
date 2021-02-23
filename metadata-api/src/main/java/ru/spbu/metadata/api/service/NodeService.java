package ru.spbu.metadata.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import ru.spbu.metadata.api.repository.NodeRepository;
import ru.spbu.metadata.common.domain.Node;
import ru.spbu.metadata.common.domain.NodeCreationParams;

@Service
public class NodeService {
    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public Optional<Node> findNode(int filesystemId, String path, int version) {
        return nodeRepository.findNode(filesystemId, path, version);
    }

    public List<Node> findChildrenNodes(int filesystemId, String basePath, int version) {
        return nodeRepository.findChildrenNodes(filesystemId, basePath, version);
    }

    public void create(int filesystemId, int version, NodeCreationParams nodeCreationParams) {
        nodeRepository.save(new Node(
                filesystemId,
                nodeCreationParams.getPath(),
                version,
                nodeCreationParams.getMeta(),
                LocalDateTime.now(),
                nodeCreationParams.isDir(),
                nodeCreationParams.getFileType()
        ));
    }
}
