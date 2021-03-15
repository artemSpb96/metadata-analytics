package ru.spbu.metadata.collector;

import java.io.IOException;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.util.HadoopOutputFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.avro.Schema.*;

public class TestDataGenerator {
    private Configuration config;
    private FileSystem filesystem;

    @BeforeEach
    void setUp() throws IOException {
        config = new Configuration();
        config.set("fs.defaultFS", "hdfs://localhost:9000");
        System.setProperty("hadoop.home.dir", "/usr/local/hadoop");
        filesystem = FileSystem.get(config);
    }

    @Test
    public void generateDirectoryWithConsistentMeta() throws IOException {
        Path directory = new Path("/tmp-1");
        filesystem.mkdirs(directory);

        Schema schema = createRecord(
                "test1",
                "doc",
                "namespace",
                false,
                List.of(new Field("testField", create(Type.INT)))
        );

        for (int i = 0; i < 3; i++) {
            HadoopOutputFile hadoopOutputFile = HadoopOutputFile.fromPath(new Path(directory, "file-" + i), config);
            ParquetWriter<GenericRecord> parquetWriter = ParquetFactory.createParquetWriter(hadoopOutputFile, schema);

            GenericData.Record record = new GenericData.Record(schema);
            record.put("testField", i);

            parquetWriter.write(record);
            parquetWriter.close();
        }
    }

    @Test
    public void generateDirectoryWithInconsistentMeta() throws IOException {
        Path directory = new Path("/tmp-2");
        filesystem.mkdirs(directory);

        for (int i = 0; i < 3; i++) {
            Schema schema = createRecord(
                    "test1",
                    "doc",
                    "namespace",
                    false,
                    List.of(new Field("testField_" + i, create(Type.INT)))
            );

            HadoopOutputFile hadoopOutputFile = HadoopOutputFile.fromPath(new Path(directory, "file-" + i), config);
            ParquetWriter<GenericRecord> parquetWriter = ParquetFactory.createParquetWriter(hadoopOutputFile, schema);

            GenericData.Record record = new GenericData.Record(schema);
            record.put("testField_" + i, i);

            parquetWriter.write(record);
            parquetWriter.close();
        }
    }

    @Test
    public void changeFileMeta() throws IOException {
        Path directory = new Path("/tmp-1");

        Schema schema = createRecord(
                "test1",
                "doc",
                "namespace",
                false,
                List.of(new Field("newTestField", create(Type.INT)))
        );

        HadoopOutputFile hadoopOutputFile = HadoopOutputFile.fromPath(new Path(directory, "file-1"), config);
        ParquetWriter<GenericRecord> parquetWriter = ParquetFactory.createParquetWriter(hadoopOutputFile, schema);

        GenericData.Record record = new GenericData.Record(schema);
        record.put("newTestField", 1);

        parquetWriter.write(record);
        parquetWriter.close();
    }

    @Test
    public void deleteFile() throws IOException {
        Path directory = new Path("/tmp-1");

        filesystem.delete(new Path(directory, "file-2"), false);
    }

    @Test
    public void addFile() throws IOException {
        Path directory = new Path("/tmp-1");

        Schema schema = createRecord(
                "test1",
                "doc",
                "namespace",
                false,
                List.of(new Field("testField", create(Type.INT)))
        );

        HadoopOutputFile hadoopOutputFile = HadoopOutputFile.fromPath(new Path(directory, "file-3"), config);
        ParquetWriter<GenericRecord> parquetWriter = ParquetFactory.createParquetWriter(hadoopOutputFile, schema);

        GenericData.Record record = new GenericData.Record(schema);
        record.put("testField", 3);

        parquetWriter.write(record);
        parquetWriter.close();
    }
}
