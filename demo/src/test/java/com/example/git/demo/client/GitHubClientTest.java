package com.example.git.demo.client;

import com.example.git.demo.configuration.PropertyConfiguration;
import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.BranchResponse;
import com.example.git.demo.model.Commit;
import com.example.git.demo.model.UserRepositoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

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
        when(restTemplateMock.getForEntity(anyString(), eq(UserRepositoryResponse.class), eq(TEST_USERNAME)))
                .thenReturn(responseEntity);

        UserRepositoryResponse actual = client.getAllNotForksRepositoriesByCustomerName(TEST_USERNAME);

        assertThat(actual).isEqualTo(response);
    }

    @Test
    void getAllNotForksRepositoriesByCustomerName_throwsException_whenUserNotFound() {
        when(restTemplateMock.getForEntity(any(), eq(UserRepositoryResponse.class), eq(TEST_USERNAME)))
                .thenThrow(new UserNotFoundException("testMessage"));

        assertThrows(UserNotFoundException.class, () -> client.getAllNotForksRepositoriesByCustomerName(TEST_USERNAME));
    }

    @Test
    void getBranchesFromRepo_returnBranches() {
        BranchResponse branchResponse = new BranchResponse("testName", new Commit("testSha"));
        ResponseEntity<List<BranchResponse>> responseEntity = new ResponseEntity<>(
                Collections.singletonList(branchResponse), HttpStatus.OK);
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class),
                eq(TEST_USERNAME), eq(TEST_REPO_NAME))).thenReturn(responseEntity);

        List<BranchResponse> actualBranches = client.getBranchesFromRepo(TEST_USERNAME, TEST_REPO_NAME);

        assertThat(actualBranches).isEqualTo(Collections.singletonList(branchResponse));
    }
}