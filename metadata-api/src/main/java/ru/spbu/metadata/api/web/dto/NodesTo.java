package ru.spbu.metadata.api.web.dto;

import java.util.List;

public class NodesTo {
    private final List<NodeTo> nodes;

    public NodesTo(List<NodeTo> nodes) {
        this.nodes = nodes;
    }

    public List<NodeTo> getNodes() {
        return nodes;
    }
}
