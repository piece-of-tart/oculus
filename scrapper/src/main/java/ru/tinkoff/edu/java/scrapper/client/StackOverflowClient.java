package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;


@Component
public class StackOverflowClient {
    private final WebClient webClient;
    private final static String BASE_URL = "https://api.stackexchange.com";

    public StackOverflowClient() {
        this(BASE_URL);
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
