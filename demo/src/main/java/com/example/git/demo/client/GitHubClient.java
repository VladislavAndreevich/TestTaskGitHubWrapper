package com.example.git.demo.client;

import com.example.git.demo.configuration.PropertyConfiguration;
import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.BranchResponse;
import com.example.git.demo.model.UserRepositoryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class GitHubClient {

    private static final String GET_USER_REPOS_WHEN_FORKS_IS_FALSE_URL = "/search/repositories?q=user:{username} forks:0";
    private static final String GET_BRANCHES_FROM_USER_REPO = "/repos/{username}/{repoName}/branches";

    private final RestTemplate template;
    private final PropertyConfiguration propertyConfiguration;

    public UserRepositoryResponse getAllNotForksRepositoriesByCustomerName(String username) {
        try {
            String url = propertyConfiguration.getGithubBaseUrl() + GET_USER_REPOS_WHEN_FORKS_IS_FALSE_URL;
            ResponseEntity<UserRepositoryResponse> response = template.getForEntity(
                    url, UserRepositoryResponse.class, username);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                log.error("User not found with following username: {}", username);
                throw new UserNotFoundException(e.getMessage());
            }
            throw e;
        }
    }

    public List<BranchResponse> getBranchesFromRepo(String username, String repoName) {
        String url = propertyConfiguration.getGithubBaseUrl() + GET_BRANCHES_FROM_USER_REPO;
        ResponseEntity<List<BranchResponse>> branches = template.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}, username, repoName);
        return branches.getBody();
    }
}
