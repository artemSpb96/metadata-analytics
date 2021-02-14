package ru.spbu.metadata.api.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.spbu.metadata.api.domain.Node;
import ru.spbu.metadata.api.service.NodeService;
import ru.spbu.metadata.api.web.dto.NodeCreationParamsTo;
import ru.spbu.metadata.api.web.dto.NodeTo;
import ru.spbu.metadata.api.web.dto.NodesTo;
import ru.spbu.metadata.api.web.transformer.NodeTransformer;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NodeController {
    private final NodeService nodeService;
    private final NodeTransformer nodeTransformer;

    public NodeController(NodeService nodeService, NodeTransformer nodeTransformer) {
        this.nodeService = nodeService;
        this.nodeTransformer = nodeTransformer;
    }

    @GetMapping("/v{ver}/filesystems/{filesystemId}/{version}")
    public NodeTo getNode(
            @PathVariable int filesystemId,
            @PathVariable int version,
            @RequestParam String path
    ) {
        Node node = nodeService.findNode(filesystemId, path, version).orElseThrow();

        return nodeTransformer.toDto(node);
    }

    @GetMapping("/v{ver}/filesystems/{filesystemId}/{version}/children")
    public NodesTo getChildrenNodes(
            @PathVariable int filesystemId,
            @PathVariable int version,
            @RequestParam String path
    ) {
        List<NodeTo> nodeToList = nodeService.findChildrenNodes(filesystemId, path, version).stream()
                .map(nodeTransformer::toDto)
                .collect(Collectors.toList());

        return new NodesTo(nodeToList);
    }

    @PutMapping("/v{ver}/filesystems/{filesystemId}/{version}")
    public void createNode(
            @PathVariable int filesystemId,
            @PathVariable int version,
            @RequestBody NodeCreationParamsTo nodeCreationParamsTo
    ) {
        nodeService.create(
                filesystemId,
                nodeCreationParamsTo.getPath(),
                version,
                nodeTransformer.toMetadata(nodeCreationParamsTo)
        );
    }
}
