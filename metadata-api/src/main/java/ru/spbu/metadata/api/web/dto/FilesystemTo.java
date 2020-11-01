package ru.spbu.metadata.api.web.dto;

import java.time.LocalDateTime;

public class FilesystemTo {
    private Integer id;
    private Integer activeVersion;
    private String name;
    private String url;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Integer getId() {
        return id;
    }

    public FilesystemTo setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getActiveVersion() {
        return activeVersion;
    }

    public FilesystemTo setActiveVersion(Integer activeVersion) {
        this.activeVersion = activeVersion;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilesystemTo setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FilesystemTo setUrl(String url) {
        this.url = url;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public FilesystemTo setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public FilesystemTo setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
