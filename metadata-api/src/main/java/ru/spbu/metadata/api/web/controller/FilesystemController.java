package ru.spbu.metadata.api.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.spbu.metadata.api.service.FilesystemService;
import ru.spbu.metadata.api.web.exception.NotFoundException;
import ru.spbu.metadata.common.domain.Filesystem;
import ru.spbu.metadata.common.domain.FilesystemCreationParams;
import ru.spbu.metadata.common.domain.FilesystemUpdateParams;

@RestController
public class FilesystemController {
    private final FilesystemService filesystemService;

    public FilesystemController(FilesystemService filesystemService) {
        this.filesystemService = filesystemService;
    }

    @GetMapping("/v{ver}/filesystems")
    public ResponseEntity<List<Filesystem>> getFilesystems() {
        List<Filesystem> filesystems = filesystemService.findAll();

        return ResponseEntity.ok(filesystems);
    }

    @GetMapping("/v{ver}/filesystems/{filesystemId}")
    public Filesystem getFilesystem(@PathVariable int filesystemId) {
        return filesystemService.findById(filesystemId)
                .orElseThrow(() -> new NotFoundException("Not found filesystem"));
    }

    @PostMapping("/v{ver}/filesystems")
    public CreateFilesystemResponse createFilesystem(@RequestBody FilesystemCreationParams filesystemCreationParams) {
        int filesystemId = filesystemService.create(filesystemCreationParams);

        return new CreateFilesystemResponse(filesystemId);
    }

    @PutMapping("/v{ver}/filesystems/{filesystemId}")
    public void updateFilesystem(
            @PathVariable int filesystemId,
            @RequestBody FilesystemUpdateParams filesystemUpdateParams
    ) {
        filesystemService.updateActiveVersion(filesystemId, filesystemUpdateParams);
    }

    public static class CreateFilesystemResponse {
        private final int filesystemId;

        public CreateFilesystemResponse(int filesystemId) {
            this.filesystemId = filesystemId;
        }

        public int getFilesystemId() {
            return filesystemId;
        }
    }
}
