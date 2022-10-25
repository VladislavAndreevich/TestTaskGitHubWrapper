package com.example.git.demo.service;

import com.example.git.demo.client.GitHubClient;
import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.BranchResponse;
import com.example.git.demo.model.Commit;
import com.example.git.demo.model.Repository;
import com.example.git.demo.model.RepositoryInfo;
import com.example.git.demo.model.RepositoryOwner;
import com.example.git.demo.model.UserRepositoryResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRepositoryServiceTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_REPO_NAME = "testRepoName";

    private GitHubClient clientMock = mock(GitHubClient.class);
    private UserRepositoryService service = new UserRepositoryService(clientMock);

    @Test
    public void getAllRepositoriesByCustomerName_returnListRepositoriesWhenUserIsFound() {
        List<Repository> items = Collections.singletonList(new Repository(TEST_REPO_NAME, new RepositoryOwner("login")));
        UserRepositoryResponse clientResponse = new UserRepositoryResponse(items);

        List<BranchResponse> branchResponse = Collections.singletonList(new BranchResponse("master", new Commit("testSha")));
        when(clientMock.getAllNotForksRepositoriesByCustomerName(TEST_USERNAME)).thenReturn(clientResponse);
        when(clientMock.getBranchesFromRepo(TEST_USERNAME, TEST_REPO_NAME)).thenReturn(branchResponse);

        List<RepositoryInfo> actualRepoInfo = service.getAllNotForksReposByCustomerName(TEST_USERNAME);

        assertThat(actualRepoInfo).isEqualTo(actualRepoInfo);
    }

    @Test
    public void getAllRepositoriesByCustomerName_throwException_whenUserNotFound() {
        when(clientMock.getAllNotForksRepositoriesByCustomerName(TEST_USERNAME)).thenThrow(new UserNotFoundException("test message"));
        assertThrows(UserNotFoundException.class, () -> service.getAllNotForksReposByCustomerName(TEST_USERNAME));
    }
}
