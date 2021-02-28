package ru.spbu.metadata.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@Configuration
public class Config {
    @Bean
    public RestTemplate metadataApiRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    ObjectMapper primaryObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        var visibilityChecker = objectMapper.getVisibilityChecker()
                .withFieldVisibility(ANY)
                .withGetterVisibility(NONE)
                .withSetterVisibility(NONE)
                .withIsGetterVisibility(NONE)
                .withCreatorVisibility(NONE);

        objectMapper.setVisibility(visibilityChecker);

        var serializationConfig = objectMapper.getSerializationConfig()
                .without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .without(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        objectMapper.setConfig(serializationConfig);

        var deserializationConfig = objectMapper.getDeserializationConfig()
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .without(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        objectMapper.setConfig(deserializationConfig);

        objectMapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModules(new JavaTimeModule());

        return objectMapper;
    }
}
