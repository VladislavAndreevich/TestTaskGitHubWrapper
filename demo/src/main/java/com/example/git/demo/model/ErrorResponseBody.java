package com.example.git.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseBody {
    private Integer status;

    @JsonProperty("Message")
    private String message;
}
