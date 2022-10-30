package com.example.git.demo.client;

import com.example.git.demo.configuration.PropertyConfiguration;
import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.client.BranchResponse;
import com.example.git.demo.model.client.Commit;
import com.example.git.demo.model.client.UserRepositoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GitHubClientTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_REPO_NAME = "testRepoName";
    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_PAGE = "1";
    private static final String TEST_PER_PAGE = "1";

    private RestTemplate restTemplateMock = mock(RestTemplate.class);
    private PropertyConfiguration propertyMock = mock(PropertyConfiguration.class);
    private GitHubClient client = new GitHubClient(restTemplateMock, propertyMock);

    @BeforeEach
    void setUpPropertyConfiguration() {
        when(propertyMock.getGithubBaseUrl()).thenReturn("testUrl");
    }

    @Test
    void getAllNotForksRepositoriesByCustomerName_returnNotForksRepository() {
        UserRepositoryResponse response = new UserRepositoryResponse();
        ResponseEntity<UserRepositoryResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(UserRepositoryResponse.class),
                eq(TEST_USERNAME),
                eq(TEST_PER_PAGE),
                eq(TEST_PAGE))).thenReturn(responseEntity);

        ResponseEntity<UserRepositoryResponse> actual = client.getAllNotForksRepositoriesByCustomerName(TEST_USERNAME, TEST_PER_PAGE, TEST_PAGE, TEST_TOKEN);

        assertThat(actual.getBody()).isEqualTo(response);
    }

    @Test
    void getAllNotForksRepositoriesByCustomerName_throwsException_whenUserNotFound() {
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(UserRepositoryResponse.class),
                eq(TEST_USERNAME),
                eq(TEST_PER_PAGE),
                eq(TEST_PAGE))).thenThrow(new UserNotFoundException("testMessage"));

        assertThrows(UserNotFoundException.class, () -> client.getAllNotForksRepositoriesByCustomerName(TEST_USERNAME, TEST_PER_PAGE, TEST_PAGE, TEST_TOKEN));
    }

    @Test
    void getBranchesFromRepo_returnBranches() {
        BranchResponse branchResponse = new BranchResponse("testName", new Commit("testSha"));
        ResponseEntity<List<BranchResponse>> responseEntity = new ResponseEntity<>(
                Collections.singletonList(branchResponse), HttpStatus.OK);
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class),
                eq(TEST_USERNAME),
                eq(TEST_REPO_NAME),
                eq(TEST_PER_PAGE),
                eq(TEST_PAGE))).thenReturn(responseEntity);

        ResponseEntity<List<BranchResponse>> actualBranches = client.getBranchesFromRepo(TEST_USERNAME, TEST_REPO_NAME, TEST_PER_PAGE, TEST_PAGE, TEST_TOKEN);

        assertThat(actualBranches.getBody()).isEqualTo(Collections.singletonList(branchResponse));
    }

    @Test
    void getGitHubToken_returnGitHubToken() {
        Map<String, String> mapResponse = Collections.singletonMap("access_token", TEST_TOKEN);
        ResponseEntity<Map<String, String>> responseEntity = new ResponseEntity<>(mapResponse, HttpStatus.OK);
        String testClientId = "testClientId";
        String testClientSecret = "testClientSecret";
        String testCode = "testCode";

        when(propertyMock.getClientId()).thenReturn(testClientId);
        when(propertyMock.getClientSecret()).thenReturn(testClientSecret);
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.POST),
                eq(null),
                any(ParameterizedTypeReference.class),
                eq(testClientId),
                eq(testClientSecret),
                eq(testCode))).thenReturn(responseEntity);

        String actualToken = client.getAccessTokenByCode(testCode);

        assertThat(actualToken).isEqualTo(TEST_TOKEN);
    }
}