package com.example.git.demo.service;

import com.example.git.demo.client.GitHubClient;
import com.example.git.demo.configuration.PropertyConfiguration;
import com.example.git.demo.model.BranchInfo;
import com.example.git.demo.model.RepositoryInfo;
import com.example.git.demo.model.client.BranchResponse;
import com.example.git.demo.model.client.Commit;
import com.example.git.demo.model.client.Repository;
import com.example.git.demo.model.client.UserRepositoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class UserRepositoryService {

    private static final String FIRST_CALL_PAGE = "1";
    private static final String HEADER_KEY_LINK = "Link";
    private static final Pattern PATTERN = Pattern.compile("(?<=page=)(\\d+)(?=>;\\srel=\"last\")");

    private final GitHubClient gitHubClient;
    private final PropertyConfiguration configuration;

    public List<RepositoryInfo> getAllNotForksReposByCustomerName(String username, String token) {
        ResponseEntity<UserRepositoryResponse> response = getRepositoriesByCustomerName(username, FIRST_CALL_PAGE, token);

        List<Repository> repositories = Objects.requireNonNull(response.getBody()).getItems();
        String lastPage = findLastPageFromHeadersOrNull(response.getHeaders());
        if (lastPage != null) {
            List<Integer> pages = IntStream.rangeClosed(2, Integer.parseInt(lastPage)).boxed().collect(Collectors.toList());
            repositories.addAll(pages.parallelStream().flatMap(page ->
                            Objects.requireNonNull(getRepositoriesByCustomerName(username, String.valueOf(page), token).getBody()).getItems().stream())
                    .collect(Collectors.toList()));
        }
        return repositories.stream()
                .map(repository -> getRepositoryInfo(username, repository, token))
                .collect(Collectors.toList());
    }

    private RepositoryInfo getRepositoryInfo(String username, Repository repo, String token) {
        String firstCallPage = "1";
        ResponseEntity<List<BranchResponse>> responseEntity = getBranchesFromRepo(username, repo, firstCallPage, token);
        List<BranchResponse> branches = Objects.requireNonNull(responseEntity.getBody());

        String lastPage = findLastPageFromHeadersOrNull(responseEntity.getHeaders());
        if (lastPage != null) {
            List<Integer> pages = IntStream.rangeClosed(2, Integer.parseInt(lastPage)).boxed().collect(Collectors.toList());
            branches.addAll(pages.parallelStream().flatMap(page ->
                            Objects.requireNonNull(getBranchesFromRepo(username, repo, String.valueOf(page), token).getBody()).stream())
                    .collect(Collectors.toList()));
        }

        List<BranchInfo> branchInfoList = branches
                .stream()
                .map(branchDTO -> new BranchInfo(branchDTO.getName(), getShaFromCommit(branchDTO.getCommit())))
                .collect(Collectors.toList());

        return new RepositoryInfo(repo.getName(), repo.getOwner().getLogin(), branchInfoList);
    }

    private String getShaFromCommit(Commit commit) {
        return Objects.nonNull(commit) ? commit.getSha() : null;
    }

    private String findLastPageFromHeadersOrNull(HttpHeaders headers) {
        String lastPage = null;
        if (headers.containsKey(HEADER_KEY_LINK)) {
            String link = headers.getFirst(HEADER_KEY_LINK);
            Matcher matcher = PATTERN.matcher(link);
            if (matcher.find()) {
                lastPage = matcher.group();
            }
        }
        return lastPage;
    }

    private ResponseEntity<List<BranchResponse>> getBranchesFromRepo(
            String username, Repository repo, String firstCallPage, String token) {
        return gitHubClient
                .getBranchesFromRepo(username, repo.getName(), configuration.getPerPage(), firstCallPage, token);
    }

    private ResponseEntity<UserRepositoryResponse> getRepositoriesByCustomerName(String username, String page, String token) {
        return gitHubClient
                .getAllNotForksRepositoriesByCustomerName(username, configuration.getPerPage(), page, token);
    }
}
