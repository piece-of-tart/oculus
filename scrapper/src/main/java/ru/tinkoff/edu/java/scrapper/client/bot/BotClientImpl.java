package ru.tinkoff.edu.java.scrapper.client.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkUpdate;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

@Log4j2
public class BotClientImpl implements BotClient {
    private final WebClient webClient;

    public BotClientImpl(final String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void sendNotification(LinkUpdate linkUpdate) {
        this.webClient.post()
                .uri("updates")
                .header("Content-Type", "application/json")
                .body(Mono.just(linkUpdate), LinkUpdate.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        res -> {
                            res.toEntity(String.class).subscribe(
                                    entity -> log.error("Error occurred while sending notification", entity));
                            return Mono.error(new HttpClientErrorException(res.statusCode()));
                        }
                );
    }
}
