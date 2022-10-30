package com.example.git.demo.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PropertyConfiguration {

    @Value("${github.base-url}")
    private String githubBaseUrl;

    @Value("${github.request.per-page}")
    private String perPage;

    @Value("${github.auth.client-id}")
    private String clientId;

    @Value("${github.auth.client-secret}")
    private String clientSecret;
}
