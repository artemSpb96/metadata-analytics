package ru.spbu.metadata.collector;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MetadataApiClient {
    private final RestTemplate metadataApiRestTemplate;
    private final String url;

    public MetadataApiClient(
            RestTemplate metadataApiRestTemplate,
            @Value("${metadata.api.url}") String url
    ) {
        this.metadataApiRestTemplate = metadataApiRestTemplate;
        this.url = url;
    }

    public void createNode(int filesystemId, int version, Node node) {
        URI createNodeUrl = UriComponentsBuilder.fromUriString(url)
                .pathSegment("filesystems", String.valueOf(filesystemId), String.valueOf(version))
                .build()
                .toUri();

        metadataApiRestTemplate.put(createNodeUrl, node);
    }
}
