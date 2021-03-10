package ru.spbu.metadata.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilesystemCreationParams {
    private final String name;
    private final String url;

    @JsonCreator
    public FilesystemCreationParams(
            @JsonProperty("name") String name,
            @JsonProperty("url") String url
    ) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
