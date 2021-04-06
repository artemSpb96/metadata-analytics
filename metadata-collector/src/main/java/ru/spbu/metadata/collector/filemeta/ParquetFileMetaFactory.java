package ru.spbu.metadata.collector.filemeta;

import java.io.IOException;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.io.DelegatingSeekableInputStream;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.SeekableInputStream;
import org.springframework.stereotype.Component;
import ru.spbu.metadata.collector.ParquetFactory;
import ru.spbu.metadata.common.domain.FileType;

@Component
public class ParquetFileMetaFactory extends FileMetaFactory {
    public ParquetFileMetaFactory(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public FileMeta createFileMeta(FileStats fileStats, Supplier<SeekableInputStream> inputStreamSupplier) {
        SeekableInputStream inputStream = inputStreamSupplier.get();
        try (var reader = ParquetFactory.createParquetReader(new DelegatingInputFile(fileStats, inputStream))) {
            GenericRecord record = reader.read();

            return new FileMeta(
                    fileStats.getPath(),
                    objectMapper.readTree(record.getSchema().toString()),
                    false,
                    FileType.PARQUET
            );
        } catch (IOException e) {
            throw new RuntimeException("Read parquer input file exception", e);
        }
    }

    private static class DelegatingInputFile implements InputFile {
        private final FileStats fileStats;
        private final SeekableInputStream inputStream;

        public DelegatingInputFile(FileStats fileStats, SeekableInputStream inputStream) {
            this.fileStats = fileStats;
            this.inputStream = inputStream;
        }

        @Override
        public long getLength() {
            return fileStats.getLength();
        }

        @Override
        public SeekableInputStream newStream() {
            return new DelegatingSeekableInputStream(inputStream) {
                @Override
                public long getPos() throws IOException {
                    return inputStream.getPos();
                }

                @Override
                public void seek(long newPos) throws IOException {
                    inputStream.seek(newPos);
                }
            };
        }
    }
}
