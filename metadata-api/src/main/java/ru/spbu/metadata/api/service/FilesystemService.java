package ru.spbu.metadata.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import ru.spbu.metadata.api.repository.FilesystemRepository;
import ru.spbu.metadata.common.domain.Filesystem;
import ru.spbu.metadata.common.domain.FilesystemCreationParams;
import ru.spbu.metadata.common.domain.FilesystemUpdateParams;

@Service
public class FilesystemService {
    private final FilesystemRepository filesystemRepository;

    public FilesystemService(FilesystemRepository filesystemRepository) {
        this.filesystemRepository = filesystemRepository;
    }

    public List<Filesystem> findAll() {
        return filesystemRepository.findAll();
    }

    public Optional<Filesystem> findById(int id) {
        return filesystemRepository.findFilesystem(id);
    }

    public int create(FilesystemCreationParams filesystemCreationParams) {
        LocalDateTime now = LocalDateTime.now();
        Filesystem newFilesystem = new Filesystem(
                null,
                0,
                filesystemCreationParams.getName(),
                filesystemCreationParams.getUrl(),
                now,
                now
        );

        return filesystemRepository.createFilesystem(newFilesystem);
    }

    public void updateActiveVersion(int id, FilesystemUpdateParams filesystemUpdateParams) {
        filesystemRepository.updateActiveVersion(id, filesystemUpdateParams.getActiveVersion(), LocalDateTime.now());
    }
}
