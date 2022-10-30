package com.example.git.demo.model.client;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repository {
    private String name;
    private RepositoryOwner owner;
}
