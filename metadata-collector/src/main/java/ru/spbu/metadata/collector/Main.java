package ru.spbu.metadata.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.spbu.metadata.common.MetadataApiClient;
import ru.spbu.metadata.common.domain.NodeCreationParams;

@SpringBootApplication(scanBasePackages = {"ru.spbu.metadata.common"})
public class Main implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private final MetadataApiClient metadataApiClient;

    public Main(MetadataApiClient metadataApiClient) {
        this.metadataApiClient = metadataApiClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Configuration config = new Configuration();
        config.set("fs.defaultFS", "hdfs://localhost:9000");

        FileSystem fileSystem = FileSystem.get(config);

        if (!args.containsOption("filesystemId") || !args.containsOption("version")) {
            throw new RuntimeException("Required options: ['filesystemId', 'version']");
        }

        int filesystemId = Integer.parseInt(args.getOptionValues("filesystemId").get(0));
        int version = Integer.parseInt(args.getOptionValues("version").get(0));

        new HdfsTraverser(fileSystem, new ObjectMapper())
                .traverse(new Path("/"))
                .forEach(fileMeta -> {
                    log.info("Find fileMeta: {}", fileMeta);
                    metadataApiClient.createNode(
                            filesystemId,
                            version,
                            new NodeCreationParams(
                                    fileMeta.getMeta(),
                                    fileMeta.getPath(),
                                    fileMeta.isDir(),
                                    fileMeta.getFileType()
                            )
                    );
                });
    }
}
