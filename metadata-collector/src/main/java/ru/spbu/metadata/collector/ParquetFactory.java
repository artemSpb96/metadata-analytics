package ru.spbu.metadata.collector;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.OutputFile;

public class ParquetFactory {
    public static ParquetWriter<GenericRecord> createParquetWriter(OutputFile outputFile, Schema schema) throws IOException {
        return AvroParquetWriter.<GenericRecord>builder(outputFile)
                .withSchema(schema)
                .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                .build();
    }

    public static ParquetReader<GenericRecord> createParquetReader(InputFile inputFile) throws IOException {
        return AvroParquetReader.<GenericRecord>builder(inputFile)
                .build();
    }
}
