package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.GitHubResponse;

@Component
public class GitHubClient {
    private final WebClient webClient;
    private final static String baseUrl = "https://api.github.com";

    public GitHubClient() {
        this(baseUrl);
    }

    public GitHubClient(String baseUrl) {
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<GitHubResponse> getRepository(String owner, String repo) {
        String uri = "/repos/" + owner + "/" + repo;
        return webClient.get()
                .uri(uri)
                .exchangeToMono(response -> {
                    HttpHeaders headers = response.headers().asHttpHeaders();
                    String etag = headers.getFirst("ETag");

                    return response.bodyToMono(GitHubResponse.class)
                            .map(repository -> {
                               repository.setETag(etag);
                               return repository;
                            });
                });
    }
}
