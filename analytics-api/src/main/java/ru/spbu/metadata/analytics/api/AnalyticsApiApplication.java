package ru.spbu.metadata.analytics.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.spbu.metadata.common", "ru.spbu.metadata.analytics.api"})
public class AnalyticsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsApiApplication.class, args);
    }

}
