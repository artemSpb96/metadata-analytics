package ru.spbu.metadata.collector;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.collector.filemeta.FileMetaFactoryChooser;

@Component
public class FilesystemTraverserChooser {
    private final FileMetaFactoryChooser fileMetaFactoryChooser;

    public FilesystemTraverserChooser(FileMetaFactoryChooser fileMetaFactoryChooser) {
        this.fileMetaFactoryChooser = fileMetaFactoryChooser;
    }

    public FilesystemTraverser choose(String filesystemUrl) {
        if (filesystemUrl.startsWith("hdfs")) {
            Configuration config = new Configuration();
            config.set("fs.defaultFS", filesystemUrl);

            FileSystem fileSystem;
            try {
                fileSystem = FileSystem.get(config);
            } catch (IOException e) {
                throw new RuntimeException("Can not connect to hdfs by url: " + filesystemUrl);
            }

            return new HdfsTraverser(fileSystem, fileMetaFactoryChooser);
        }

        throw new RuntimeException("Unsupported filesystem with url: " + filesystemUrl);
    }
}
