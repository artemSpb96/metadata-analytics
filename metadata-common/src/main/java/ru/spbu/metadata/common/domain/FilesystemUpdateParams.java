package ru.spbu.metadata.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FilesystemUpdateParams {
    private final int activeVersion;

    @JsonCreator
    public FilesystemUpdateParams(
            @JsonProperty("activeVersion") int activeVersion
    ) {
        this.activeVersion = activeVersion;
    }

    public int getActiveVersion() {
        return activeVersion;
    }
}
