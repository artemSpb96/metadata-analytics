package ru.spbu.metadata.collector;

import java.util.stream.Stream;

import ru.spbu.metadata.collector.filemeta.FileMeta;

public interface Traverser {
    Stream<FileMeta> traverse(String root);
}
