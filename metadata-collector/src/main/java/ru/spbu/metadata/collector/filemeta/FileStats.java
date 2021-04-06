package ru.spbu.metadata.collector.filemeta;

public class FileStats {
    private final String path;
    private final long length;
    private final boolean isDirectory;

    public FileStats(String path, long length, boolean isDirectory) {
        this.path = path;
        this.length = length;
        this.isDirectory = isDirectory;
    }

    public String getPath() {
        return path;
    }

    public long getLength() {
        return length;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
