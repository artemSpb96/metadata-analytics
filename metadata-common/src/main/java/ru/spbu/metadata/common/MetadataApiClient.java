package ru.spbu.metadata.common;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.spbu.metadata.common.domain.Node;
import ru.spbu.metadata.common.domain.NodeCreationParams;

@Component
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

    public void createNode(int filesystemId, int version, NodeCreationParams nodeCreationParams) {
        URI createNodeUrl = UriComponentsBuilder.fromUriString(url)
                .pathSegment("filesystems", String.valueOf(filesystemId), String.valueOf(version))
                .build()
                .toUri();

        metadataApiRestTemplate.put(createNodeUrl, nodeCreationParams);
    }

    public List<Node> getChildrenNodes(int filesystemId, int version, String path) {
        URI getChildrenNodesUrl = UriComponentsBuilder.fromUriString(url)
                .pathSegment("filesystems", String.valueOf(filesystemId), String.valueOf(version), "children")
                .queryParam("path", path)
                .build()
                .toUri();

        return metadataApiRestTemplate.exchange(
                getChildrenNodesUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Node>>() {
                }
        ).getBody();
    }
}
