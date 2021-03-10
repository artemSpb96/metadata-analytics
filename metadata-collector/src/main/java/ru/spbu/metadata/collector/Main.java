package ru.spbu.metadata.collector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import ru.spbu.metadata.collector.filemeta.FileMetaFactoryChooser;
import ru.spbu.metadata.common.MetadataApiClient;
import ru.spbu.metadata.common.domain.Filesystem;
import ru.spbu.metadata.common.domain.FilesystemUpdateParams;
import ru.spbu.metadata.common.domain.NodeCreationParams;

@SpringBootApplication(scanBasePackages = {"ru.spbu.metadata.common", "ru.spbu.metadata.collector.filemeta"})
public class Main implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private final MetadataApiClient metadataApiClient;
    private final FileMetaFactoryChooser fileMetaFactoryChooser;

    public Main(MetadataApiClient metadataApiClient, FileMetaFactoryChooser fileMetaFactoryChooser) {
        this.metadataApiClient = metadataApiClient;
        this.fileMetaFactoryChooser = fileMetaFactoryChooser;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!args.containsOption("filesystemId")) {
            throw new RuntimeException("Required options: ['filesystemId']");
        }

        int filesystemId = Integer.parseInt(args.getOptionValues("filesystemId").get(0));

        StopWatch sw = new StopWatch("Update filesystem metadata");

        sw.start("get filesystem info");
        Filesystem filesystem = metadataApiClient.getFilesystem(filesystemId)
                .orElseThrow(() -> new RuntimeException("Not found filesystem by " + filesystemId + " id"));
        sw.stop();

        Configuration config = new Configuration();
        config.set("fs.defaultFS", filesystem.getUrl());
        FileSystem fileSystem = FileSystem.get(config);
        int newFilesystemVersion = filesystem.getActiveVersion() + 1;

        sw.start("traverse filesystem and update meta");
        new HdfsTraverser(fileSystem, fileMetaFactoryChooser)
                .traverse(new Path("/"))
                .forEach(fileMeta -> {
                    log.info("Find fileMeta: {}", fileMeta);
                    metadataApiClient.createNode(
                            filesystemId,
                            newFilesystemVersion,
                            new NodeCreationParams(
                                    fileMeta.getMeta(),
                                    fileMeta.getPath(),
                                    fileMeta.isDir(),
                                    fileMeta.getFileType()
                            )
                    );
                });
        sw.stop();

        sw.start("commit new filesystem version");
        metadataApiClient.updateFilesystem(filesystemId, new FilesystemUpdateParams(newFilesystemVersion));
        sw.stop();

        log.info(sw.prettyPrint());
    }
}
