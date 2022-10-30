package com.example.git.demo.service;

import com.example.git.demo.client.GitHubClient;
import com.example.git.demo.configuration.PropertyConfiguration;
import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.RepositoryInfo;
import com.example.git.demo.model.client.BranchResponse;
import com.example.git.demo.model.client.Commit;
import com.example.git.demo.model.client.Repository;
import com.example.git.demo.model.client.RepositoryOwner;
import com.example.git.demo.model.client.UserRepositoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRepositoryServiceTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_REPO_NAME = "testRepoName";
    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_PAGE = "1";
    private static final String TEST_PER_PAGE = "1";

    private GitHubClient clientMock = mock(GitHubClient.class);
    private PropertyConfiguration configurationMock = mock(PropertyConfiguration.class);
    private UserRepositoryService service = new UserRepositoryService(clientMock, configurationMock);

    @Test
    public void getAllRepositoriesByCustomerName_returnListRepositoriesWhenUserIsFound() {
        List<Repository> items = Collections.singletonList(new Repository(TEST_REPO_NAME, new RepositoryOwner("login")));
        UserRepositoryResponse clientResponse = new UserRepositoryResponse(items);
        ResponseEntity<UserRepositoryResponse> response = new ResponseEntity<>(clientResponse, HttpStatus.OK);
        List<BranchResponse> branchResponse = Collections.singletonList(new BranchResponse("master", new Commit("testSha")));
        ResponseEntity<List<BranchResponse>> branchResponseResponseEntity = new ResponseEntity<>(branchResponse, HttpStatus.OK);
        when(clientMock.getAllNotForksRepositoriesByCustomerName(eq(TEST_USERNAME), any(), any(), eq(TEST_TOKEN))).thenReturn(response);
        when(clientMock.getBranchesFromRepo(eq(TEST_USERNAME), eq(TEST_REPO_NAME), any(), any(), eq(TEST_TOKEN))).thenReturn(branchResponseResponseEntity);

        List<RepositoryInfo> actualRepoInfo = service.getAllNotForksReposByCustomerName(TEST_USERNAME, TEST_TOKEN);

        assertThat(actualRepoInfo).isEqualTo(actualRepoInfo);
    }

    @Test
    public void getAllRepositoriesByCustomerName_throwException_whenUserNotFound() {
        when(clientMock.getAllNotForksRepositoriesByCustomerName(eq(TEST_USERNAME), any(), any(), eq(TEST_TOKEN))).thenThrow(new UserNotFoundException("test message"));
        assertThrows(UserNotFoundException.class, () -> service.getAllNotForksReposByCustomerName(TEST_USERNAME, TEST_TOKEN));
    }
}
