package com.example.git.demo.service;

import com.example.git.demo.client.GitHubClient;
import com.example.git.demo.model.BranchInfo;
import com.example.git.demo.model.Commit;
import com.example.git.demo.model.Repository;
import com.example.git.demo.model.RepositoryInfo;
import com.example.git.demo.model.UserRepositoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRepositoryService {

    private final GitHubClient gitHubClient;

    public List<RepositoryInfo> getAllNotForksReposByCustomerName(String username) {
        UserRepositoryResponse response = gitHubClient.getAllNotForksRepositoriesByCustomerName(username);
        List<Repository> repositories = response.getItems();

        return repositories.stream()
                .map(repository -> getRepositoryInfo(username, repository))
                .collect(Collectors.toList());
    }

    private RepositoryInfo getRepositoryInfo(String username, Repository repo) {
        List<BranchInfo> branches = gitHubClient.getBranchesFromRepo(username, repo.getName())
                .stream()
                .map(branchDTO -> new BranchInfo(branchDTO.getName(), getShaFromCommit(branchDTO.getCommit())))
                .collect(Collectors.toList());

        return new RepositoryInfo(repo.getName(), repo.getOwner().getLogin(), branches);
    }

    private String getShaFromCommit(Commit commit) {
        return Objects.nonNull(commit) ? commit.getSha() : null;
    }
}
