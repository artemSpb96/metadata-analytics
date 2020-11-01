package ru.spbu.metadata.api.web.transformer;

import org.springframework.stereotype.Component;
import ru.spbu.metadata.api.domain.Filesystem;
import ru.spbu.metadata.api.web.dto.FilesystemTo;

@Component
public class FilesystemTransformer {
    public FilesystemTo toDto(Filesystem filesystem) {
        return new FilesystemTo()
                .setId(filesystem.getId())
                .setActiveVersion(filesystem.getActiveVersion())
                .setName(filesystem.getName())
                .setUrl(filesystem.getUrl())
                .setCreateTime(filesystem.getCreateTime())
                .setUpdateTime(filesystem.getUpdateTime());
    }
}
