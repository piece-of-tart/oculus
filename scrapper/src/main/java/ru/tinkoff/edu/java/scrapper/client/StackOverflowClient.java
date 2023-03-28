package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.StackOverflowResponse;


@Component
public class StackOverflowClient {
    private final WebClient webClient;

    public StackOverflowClient() {
        this("https://api.stackexchange.com");
    }

    public StackOverflowClient(String baseUrl) {
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<StackOverflowResponse> getQuestion(String id) {
        String uri = "/2.3/questions/" + id + "?site=stackoverflow";
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(StackOverflowResponse.class);
    }
}
