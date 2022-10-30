package com.example.git.demo.controller;

import com.example.git.demo.model.RepositoryInfo;
import com.example.git.demo.service.UserRepositoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserRepositoryController {

    private final UserRepositoryService userRepositoryService;

    @GetMapping(value = "/users/{username}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryInfo> getAllRepositoriesByCustomerName(@PathVariable String username,
                                                                 @RequestParam(required = false) String token) {
        return userRepositoryService.getAllNotForksReposByCustomerName(username, token);
    }
}
