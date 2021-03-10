package ru.spbu.metadata.analytics.api.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.spbu.metadata.analytics.api.domain.NodeDiff;
import ru.spbu.metadata.analytics.api.service.AnalyticsService;
import ru.spbu.metadata.common.domain.FileType;
import ru.spbu.metadata.common.domain.Node;

@RestController
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/v{ver}/directories")
    public ResponseEntity<List<Node>> findDirectories(
            @RequestParam int filesystemId,
            @RequestParam FileType fileType
    ) {
        return ResponseEntity.ok(analyticsService.findDirectoriesWithConsistentMetaFiles(filesystemId, fileType));
    }

    @GetMapping("/v{ver}/diff")
    public NodeDiff findDiff(
            @RequestParam int filesystemId,
            @RequestParam String path,
            @RequestParam int fromVersion,
            @RequestParam int toVersion
    ) {
        return analyticsService.findDiff(filesystemId, path, fromVersion, toVersion);
    }
}
