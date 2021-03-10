package ru.spbu.metadata.api.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbu.metadata.api.service.NodeService;
import ru.spbu.metadata.api.web.exception.NotFoundException;
import ru.spbu.metadata.common.domain.Node;
import ru.spbu.metadata.common.domain.NodeCreationParams;

@RestController
public class NodeController {
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/v{ver}/filesystems/{filesystemId}/{version}")
    public Node getNode(
            @PathVariable int filesystemId,
            @PathVariable int version,
            @RequestParam String path
    ) {
        return nodeService.findNode(filesystemId, path, version)
                .orElseThrow(() -> new NotFoundException("Not found node"));
    }

    @GetMapping("/v{ver}/filesystems/{filesystemId}/{version}/children")
    public ResponseEntity<List<Node>> getChildrenNodes(
            @PathVariable int filesystemId,
            @PathVariable int version,
            @RequestParam String path
    ) {
        List<Node> nodes = nodeService.findChildrenNodes(filesystemId, path, version);

        return ResponseEntity.ok(nodes);
    }

    @PutMapping("/v{ver}/filesystems/{filesystemId}/{version}")
    public void saveNode(
            @PathVariable int filesystemId,
            @PathVariable int version,
            @RequestBody NodeCreationParams nodeCreationParams
    ) {
        nodeService.saveNode(
                filesystemId,
                version,
                nodeCreationParams
        );
    }
}
