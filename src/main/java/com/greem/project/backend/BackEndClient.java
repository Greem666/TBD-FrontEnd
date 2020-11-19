package com.greem.project.backend;

import com.greem.project.backend.exceptions.AddChildException;
import com.greem.project.domain.ChildDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackEndClient {

    private final RestTemplate restTemplate;
    private final BackEndConfig backEndConfig;

    public List<ChildDto> getAllChildren() {
        Optional<ChildDto[]> children = Optional.ofNullable(restTemplate.getForObject(baseChildUri(), ChildDto[].class));
        return children.map(Arrays::asList).orElseGet(() -> Collections.singletonList(new ChildDto()));
    }

    public ChildDto addChild(ChildDto newChild) {
        HttpEntity<ChildDto> request = new HttpEntity<>(newChild);
        Optional<ChildDto> child = Optional.ofNullable(restTemplate.postForObject(baseChildUri(), request, ChildDto.class));
        return child.orElseThrow(
                () -> new AddChildException(
                        String.format("Could not add child entry for %s %s.", newChild.getFirstName(), newChild.getLastName())
                )
        );
    }

    private URI baseChildUri() {
        return UriComponentsBuilder.fromUriString(backEndConfig.getUrl())
                .port(backEndConfig.getPort())
                .path("/v1/child")
                .build()
                .encode().toUri();
    }
}
