package ru.spbu.metadata.collector.filemeta;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.collector.ParquetFactory;
import ru.spbu.metadata.common.domain.FileType;

@Component
public class ParquetFileMetaFactory extends FileMetaFactory {
    public ParquetFileMetaFactory(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public FileMeta createFileMeta(FileSystem fs, LocatedFileStatus hadoopFileStatus) {
        HadoopInputFile inputFile = null;
        try {
            inputFile = HadoopInputFile.fromStatus(hadoopFileStatus, fs.getConf());

            try (var reader = ParquetFactory.createParquetReader(inputFile)) {
                GenericRecord record = reader.read();

                return new FileMeta(
                        getFilePath(hadoopFileStatus.getPath()),
                        objectMapper.readTree(record.getSchema().toString()),
                        false,
                        FileType.PARQUET
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Read parquer input file exception", e);
        }
    }
}
