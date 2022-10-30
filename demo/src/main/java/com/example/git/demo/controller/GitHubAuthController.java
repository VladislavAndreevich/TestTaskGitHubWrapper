package com.example.git.demo.controller;

import com.example.git.demo.client.GitHubClient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class GitHubAuthController {

    private final GitHubClient gitHubClient;

    // callback from https://github.com/login/oauth/authorize?client_id=70e9f8939c05807bd013
    @GetMapping(value = "/login/oauth2/code/github")
    public String getAccessTokenByCode(@RequestParam String code) {
        return gitHubClient.getAccessTokenByCode(code);
    }

}
