package ru.spbu.metadata.api.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.spbu.metadata.api.domain.Filesystem;
import ru.spbu.metadata.api.service.FilesystemService;
import ru.spbu.metadata.api.web.dto.FilesystemTo;
import ru.spbu.metadata.api.web.dto.FilesystemsTo;
import ru.spbu.metadata.api.web.transformer.FilesystemTransformer;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FilesystemController {
    private final FilesystemService filesystemService;
    private final FilesystemTransformer filesystemTransformer;

    public FilesystemController(FilesystemService filesystemService, FilesystemTransformer filesystemTransformer) {
        this.filesystemService = filesystemService;
        this.filesystemTransformer = filesystemTransformer;
    }

    @GetMapping("/v{ver}/filesystems")
    public FilesystemsTo getFilesystems() {
        List<FilesystemTo> filesystemToList = filesystemService.findAll().stream()
                .map(filesystemTransformer::toDto)
                .collect(Collectors.toList());

        return new FilesystemsTo(filesystemToList);
    }

    @GetMapping("/v{ver}/filesystems/{filesystemId}")
    public FilesystemTo getFilesystem(@PathVariable int filesystemId) {
        Filesystem filesystem = filesystemService.findById(filesystemId).orElseThrow();

        return filesystemTransformer.toDto(filesystem);
    }

    @PostMapping("/v{ver}/filesystems")
    public FilesystemTo createFilesystem(@RequestBody FilesystemTo filesystem) {
        Filesystem createdFilesystem = filesystemService.create(filesystem.getName(), filesystem.getUrl());

        return filesystemTransformer.toDto(createdFilesystem);
    }

    @PutMapping("/v{ver}/filesystems/{filesystemId}")
    public void updateFilesystem(@PathVariable int filesystemId, @RequestBody FilesystemTo filesystem) {
        filesystemService.updateActiveVersion(filesystemId, filesystem.getActiveVersion());
    }
}
