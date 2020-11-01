package ru.spbu.metadata.api.web.dto;

import java.util.List;

public class FilesystemsTo {
    private final List<FilesystemTo> filesystems;

    public FilesystemsTo(List<FilesystemTo> filesystems) {
        this.filesystems = filesystems;
    }

    public List<FilesystemTo> getFilesystems() {
        return filesystems;
    }
}
