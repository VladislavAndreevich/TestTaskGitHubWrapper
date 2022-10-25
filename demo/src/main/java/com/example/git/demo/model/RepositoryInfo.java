package com.example.git.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryInfo {
    private String name;
    private String owner;
    private List<BranchInfo> branches;
}
