package com.example.git.demo.controller;

import com.example.git.demo.exception.UserNotFoundException;
import com.example.git.demo.model.RepositoryInfo;
import com.example.git.demo.service.UserRepositoryService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRepositoryControllerTest {

    private static final String TEST_USERNAME = "testUsername";

    private UserRepositoryService serviceMock = mock(UserRepositoryService.class);
    private UserRepositoryController controller = new UserRepositoryController(serviceMock);

    @Test
    public void getAllRepositoriesByCustomerName_returnListRepositoriesWhenUserIsFound() {
        List<RepositoryInfo> repositoryInfos = Collections.singletonList(mock(RepositoryInfo.class));
        when(serviceMock.getAllNotForksReposByCustomerName(TEST_USERNAME)).thenReturn(repositoryInfos);

        List<RepositoryInfo> actualRepoInfo = controller.getAllRepositoriesByCustomerName(TEST_USERNAME);

        assertThat(actualRepoInfo).isEqualTo(repositoryInfos);
    }

    @Test
    public void getAllRepositoriesByCustomerName_throwException_whenUserNotFound() {
        when(serviceMock.getAllNotForksReposByCustomerName(TEST_USERNAME)).thenThrow(new UserNotFoundException("test message"));

        assertThrows(UserNotFoundException.class, () -> controller.getAllRepositoriesByCustomerName(TEST_USERNAME));
    }
}
