package com.example.git.demo.client;

import com.example.git.demo.configuration.PropertyConfiguration;
import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.client.BranchResponse;
import com.example.git.demo.model.client.UserRepositoryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class GitHubClient {

    private static final String GET_USER_REPOS_WHEN_FORKS_IS_FALSE_URL = "/search/repositories?q=user:{username}&per_page={perPage}&page={page}";
    private static final String GET_BRANCHES_FROM_USER_REPO = "/repos/{username}/{repoName}/branches?per_page={perPage}&page={page}";
    private static final String GET_GIT_HUB_TOKEN = "https://github.com/login/oauth/access_token?client_id={clientId}&client_secret={clientSecret}&code={code}";
    private static final String ACCESS_TOKEN = "access_token";

    private final RestTemplate template;
    private final PropertyConfiguration propertyConfiguration;

    public ResponseEntity<UserRepositoryResponse> getAllNotForksRepositoriesByCustomerName(
            String username, String perPage, String page, String token) {
        try {
            String url = propertyConfiguration.getGithubBaseUrl() + GET_USER_REPOS_WHEN_FORKS_IS_FALSE_URL;
            HttpEntity<Object> entity = getHttpEntity(token);
            return template.exchange(
                    url, HttpMethod.GET, entity, UserRepositoryResponse.class, username, perPage, page);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                log.error("User not found with following username: {}", username);
                throw new UserNotFoundException(e.getMessage());
            }
            throw e;
        }
    }

    public ResponseEntity<List<BranchResponse>> getBranchesFromRepo(
            String username, String repoName, String perPage, String page, String token) {
        String url = propertyConfiguration.getGithubBaseUrl() + GET_BRANCHES_FROM_USER_REPO;
        HttpEntity<Object> entity = getHttpEntity(token);
        return template.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}, username, repoName, perPage, page);
    }

    private HttpEntity<Object> getHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        if (Objects.nonNull(token)) {
            headers.setBearerAuth(token);
        }
        return new HttpEntity<>(null, headers);
    }

    public String getAccessTokenByCode(String code) {
        ResponseEntity<Map<String, String>> exchange = template.exchange(GET_GIT_HUB_TOKEN,
                HttpMethod.POST, null, new ParameterizedTypeReference<>() {},
                propertyConfiguration.getClientId(), propertyConfiguration.getClientSecret(), code);
        Map<String, String> body = exchange.getBody();
        return body.get(ACCESS_TOKEN);
    }
}
