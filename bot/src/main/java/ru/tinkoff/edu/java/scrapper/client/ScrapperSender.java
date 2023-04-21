package ru.tinkoff.edu.java.scrapper.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;

import java.util.List;

@Component
@Log4j2
public class ScrapperSender {
    private final WebClient webClient;

    public ScrapperSender(@Value("${scrapper.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public List<LinkResponse> getLinksByChatId(long chatId) {
        return webClient.get()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LinkResponse>>() {})
                .block();
    }

    public String registerNewUser(long chatId) {
        String response = webClient.post()
                .uri("/tg-chat/" + chatId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("<Error while registering new user>"))
                .block();
        log.info("RESPONSE:" + response);
        return response;
    }

    public String addNewTrackedLink(long chatId, AddLinkRequest addLinkRequest) {
        try {
            return webClient.post()
                    .uri("/links")
                    .header("Tg-Chat-Id", String.valueOf(chatId))
                    .body(BodyInserters.fromValue(addLinkRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (RuntimeException e) {
            return "Couldn't connect to scrapper";
        }
    }

    public String deleteTrackedLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        try {
            return webClient.method(HttpMethod.DELETE)
                    .uri("/links")
                    .header("Tg-Chat-Id", String.valueOf(chatId))
                    .body(BodyInserters.fromValue(removeLinkRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            return "Link " + removeLinkRequest.uri() + " doesn't exist.";
        }
    }
}
