package com.greem.project.backend;

import com.greem.project.domain.ChildDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackEndClient {

    private final RestTemplate restTemplate;
    private final BackEndConfig backEndConfig;

    public List<ChildDto> getAllChildren() {
        Optional<ChildDto[]> children = Optional.ofNullable(restTemplate.getForObject(baseChildUri(), ChildDto[].class));
        return children.map(Arrays::asList).orElseGet(() -> Arrays.asList(new ChildDto()));
    }

    public ChildDto addChild(ChildDto newChild) {
        Optional<ChildDto> child = Optional.ofNullable(restTemplate.postForObject(baseChildUri(), newChild, ChildDto.class));
        return child.orElseGet(ChildDto::new);
    }

    private URI baseChildUri() {
        return UriComponentsBuilder.fromUriString(backEndConfig.getUrl())
                .port(backEndConfig.getPort())
                .path("/v1/child")
                .build()
                .encode().toUri();
    }
}
