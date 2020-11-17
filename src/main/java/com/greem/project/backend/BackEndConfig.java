package com.greem.project.backend;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BackEndConfig {
    @Value("${backend.api.endpoint.url}")
    private String url;

    @Value("${backend.api.endpoint.port}")
    private int port;
}
