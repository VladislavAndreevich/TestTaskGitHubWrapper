package com.example.git.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class GitIntegrationApplication {

    public static void main(String[] args) {
		SpringApplication.run(GitIntegrationApplication.class, args);
    }
}
