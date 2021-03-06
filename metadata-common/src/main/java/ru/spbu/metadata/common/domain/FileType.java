package ru.spbu.metadata.common.domain;

public enum FileType {
    PARQUET,
    UNKNOWN;

    public static FileType fromString(String str) {
        if (str == null) {
            return null;
        }

        for (FileType fileType : FileType.values()) {
            if (fileType.name().equals(str)) {
                return fileType;
            }
        }

        throw new RuntimeException("Not found fileType with " + str + " name");
    }
}
