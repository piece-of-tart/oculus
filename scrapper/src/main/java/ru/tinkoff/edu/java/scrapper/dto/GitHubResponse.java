package ru.tinkoff.edu.java.scrapper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GitHubResponse {
    private String name;
    private String description;
    private String eTag;
}
