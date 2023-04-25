package ru.tinkoff.edu.java.scrapper.client.bot;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdate;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

@Component
@Log4j2
public class BotClientImpl implements BotClient {
    private static final String DEFAULT_URL = "http://localhost:8080";
    private final WebClient webClient;

    public BotClientImpl() {
        this(DEFAULT_URL);
    }

    public BotClientImpl(String baseUrl) {
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
