package ru.spbu.metadata.common.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Filesystem {
    private final Integer id;
    private final int activeVersion;
    private final String name;
    private final String url;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;

    @JsonCreator
    public Filesystem(
            @JsonProperty("id") Integer id,
            @JsonProperty("activeVersion") int activeVersion,
            @JsonProperty("name") String name,
            @JsonProperty("url") String url,
            @JsonProperty("createTime") LocalDateTime createTime,
            @JsonProperty("updateTime") LocalDateTime updateTime
    ) {
        this.id = id;
        this.activeVersion = activeVersion;
        this.name = name;
        this.url = url;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public int getActiveVersion() {
        return activeVersion;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}
