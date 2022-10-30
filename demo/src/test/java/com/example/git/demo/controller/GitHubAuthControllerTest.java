package com.example.git.demo.controller;

import com.example.git.demo.client.GitHubClient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GitHubAuthControllerTest {

    private GitHubClient clientMock = mock(GitHubClient.class);
    private GitHubAuthController controller = new GitHubAuthController(clientMock);

    @Test
    public void getAllRepositoriesByCustomerName_returnListRepositoriesWhenUserIsFound() {
        String testCode = "testCode";
        String testToken = "testToken";
        when(clientMock.getAccessTokenByCode(testCode)).thenReturn(testToken);

        String actualToken = controller.getAccessTokenByCode(testCode);

        assertThat(actualToken).isEqualTo(testToken);
    }
}
