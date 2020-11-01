package ru.spbu.metadata.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("filesystem")
public class Filesystem {
    @Id
    @Column("fs_id")
    private final Integer id;

    @Column("active_ver")
    private final int activeVersion;

    @Column("name")
    private final String name;

    @Column("url")
    private final String url;

    @Column("create_time")
    private final LocalDateTime createTime;

    @Column("update_time")
    private final LocalDateTime updateTime;

    public Filesystem(
            Integer id,
            int activeVersion,
            String name,
            String url,
            LocalDateTime createTime,
            LocalDateTime updateTime
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
