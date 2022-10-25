package com.example.git.demo.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PropertyConfiguration {

    @Value("${github.base-url}")
    private String githubBaseUrl;
}
