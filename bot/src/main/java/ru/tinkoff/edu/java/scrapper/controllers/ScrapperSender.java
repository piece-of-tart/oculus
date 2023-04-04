package ru.tinkoff.edu.java.scrapper.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;

import java.util.Objects;


@Component
public class ScrapperSender {
    private final WebClient webClient;

    public ScrapperSender(@Value("${scrapper.baseUrl}") String scrapperBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(scrapperBaseUrl).build();
    }

    public ListLinksResponse getLinksByChatId(long chatId) {
        return webClient.get()
                .uri("/links")
                .header("Tg-Chat-Id", String.valueOf(chatId))
                .retrieve()
                .bodyToMono(ListLinksResponse.class)
                .block();
    }

    public boolean registerNewUser(long chatId) {
        return Objects.equals(webClient.post()
                .uri("/tg-chat/" + chatId)
                .retrieve()
                .bodyToMono(String.class)
                .block(), "User with id=" + chatId + " was registered before.");
    }

    public String addNewTrackedLink(long chatId, String url) {
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
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

    public String deleteTrackedLink(long chatId, String url) {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
        try {
            return webClient.method(HttpMethod.DELETE)
                    .uri("/links")
                    .header("Tg-Chat-Id", String.valueOf(chatId))
                    .body(BodyInserters.fromValue(removeLinkRequest))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            return "Link " + url + " doesn't exist.";
        }
    }
}
