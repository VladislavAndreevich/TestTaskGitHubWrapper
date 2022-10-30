package com.example.git.demo.integration;

import com.example.git.demo.model.client.BranchResponse;
import com.example.git.demo.model.client.Commit;
import com.example.git.demo.model.client.Repository;
import com.example.git.demo.model.client.RepositoryOwner;
import com.example.git.demo.model.client.UserRepositoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoryIntegrationTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String INVALID_USERNAME = "invalidUsername";
    private static final String URL_USERS_REPOS = "/users/{username}/repos";
    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_PAGE = "1";
    private static final String TEST_PER_PAGE = "1";
    @MockBean
    private RestTemplate restTemplateMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllRepositoriesByCustomerName_responseStatusOk() throws Exception {
        Repository repository = new Repository("testRepoName", new RepositoryOwner("testLogin"));
        UserRepositoryResponse response = new UserRepositoryResponse(Collections.singletonList(repository));
        ResponseEntity<UserRepositoryResponse> responseEntityForUserRepos = new ResponseEntity<>(response, HttpStatus.OK);
        BranchResponse branchResponse = new BranchResponse("testName", new Commit("testSha"));
        ResponseEntity<List<BranchResponse>> responseEntityForBranches = new ResponseEntity<>(
                Collections.singletonList(branchResponse), HttpStatus.OK);

        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(UserRepositoryResponse.class),
                any(),
                any(),
                any())).thenReturn(responseEntityForUserRepos);

        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class),
                eq(TEST_USERNAME),
                eq("testRepoName"),
                any(),
                any())).thenReturn(responseEntityForBranches);

        mockMvc.perform(get(URL_USERS_REPOS, TEST_USERNAME)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRepositoriesByCustomerName_responseStatusNotFound() throws Exception {
        HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(UserRepositoryResponse.class),
                any(),
                any(),
                any())).thenThrow(clientErrorException);

        mockMvc.perform(get(URL_USERS_REPOS, INVALID_USERNAME)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllRepositoriesByCustomerName_responseNotAcceptable() throws Exception {
        mockMvc.perform(get(URL_USERS_REPOS, INVALID_USERNAME)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable());
    }
}
