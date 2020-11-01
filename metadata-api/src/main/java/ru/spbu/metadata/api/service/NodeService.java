package ru.spbu.metadata.api.service;

import org.springframework.stereotype.Service;
import ru.spbu.metadata.api.domain.Node;
import ru.spbu.metadata.api.repository.NodeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public void create(int filesystemId, String path, int version, String meta) {
        nodeRepository.save(new Node(
                filesystemId,
                path,
                version,
                meta,
                LocalDateTime.now()
        ));
    }
}
