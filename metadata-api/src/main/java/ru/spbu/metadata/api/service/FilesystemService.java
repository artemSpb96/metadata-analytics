package ru.spbu.metadata.api.service;

import org.springframework.stereotype.Service;
import ru.spbu.metadata.api.domain.Filesystem;
import ru.spbu.metadata.api.repository.FilesystemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilesystemService {
    private final FilesystemRepository filesystemRepository;

    public FilesystemService(FilesystemRepository filesystemRepository) {
        this.filesystemRepository = filesystemRepository;
    }

    public List<Filesystem> findAll() {
        List<Filesystem> filesystems = new ArrayList<>();
        filesystemRepository.findAll().forEach(filesystems::add);

        return filesystems;
    }

    public Optional<Filesystem> findById(int id) {
        return filesystemRepository.findById(id);
    }

    public Filesystem create(String name, String url) {
        LocalDateTime now = LocalDateTime.now();
        return filesystemRepository.save(new Filesystem(
                null,
                1,
                name,
                url,
                now,
                now
        ));
    }

    public void updateActiveVersion(int id, int newActiveVersion) {
        filesystemRepository.updateActiveVersion(id, newActiveVersion, LocalDateTime.now());
    }
}
