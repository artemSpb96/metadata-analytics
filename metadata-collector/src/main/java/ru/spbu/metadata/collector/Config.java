package ru.spbu.metadata.collector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public RestTemplate metadataApiRestTemplate() {
        return new RestTemplate();
    }
}
