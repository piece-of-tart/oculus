package ru.tinkoff.edu.java.scrapper.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.parser.ParserLinker;
import ru.tinkoff.edu.java.parser.parsers.Parser;
import ru.tinkoff.edu.java.parser.values.GithubValue;
import ru.tinkoff.edu.java.parser.values.StackOverflowValue;
import ru.tinkoff.edu.java.parser.values.Value;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdateData;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;
    private final LinkService linkService;

    @Scheduled(fixedDelayString = "#{@getScheduler.interval.toMillis()}")
    public void update() {
        log.info("Update " + Calendar.getInstance().toString());
        checkGitHubLinks();
        checkStackOverflowLinks();
    }

    private void checkStackOverflowLinks() {
        List<LinkUpdateData> linkList = linkService.getListByType("stackoverflow");
        for (LinkUpdateData link : linkList){
            Value questionId = new ParserLinker().parse(link.uri().toString());
            if (questionId instanceof StackOverflowValue stackOverflowValue) {
                StackOverflowResponse stackOverflowResponse = stackOverflowClient.getQuestion(stackOverflowValue.id()).block();
                assert stackOverflowResponse != null;
                OffsetDateTime lastActivityDate = stackOverflowResponse.getItems().get(0).getLastActivityDate();
                OffsetDateTime lastActivityInDatabase = OffsetDateTime.ofInstant(link.lastChecked().toInstant(), ZoneOffset.UTC);
                if (lastActivityInDatabase.isBefore(lastActivityDate)) {
                    sendNotifications(link);
                }
                checkLink(link, new Random().nextLong());
            }
        }
    }

    private void checkGitHubLinks(){
        List<LinkUpdateData> linkList = linkService.getListByType("github");
        for (LinkUpdateData link : linkList){
            Value questionId = new ParserLinker().parse(link.uri().toString());
            if (questionId instanceof GithubValue githubValue) {
                var lastRepositoryEvent = gitHubClient.getRepository(githubValue.user(), githubValue.rep()).block();
                assert lastRepositoryEvent != null;
                int lastHashETagCode = lastRepositoryEvent.getETag().hashCode();
                if (lastHashETagCode != link.lastUpdatedId()) {
                    sendNotifications(link);
                }
                checkLink(link, lastHashETagCode);
            }
        }
    }

    private void sendNotifications(LinkUpdateData link) {
        List<Long> chatIds = linkService.get(link.uri()).stream().map(LinkEntity::chatId).toList();
        botClient.sendNotification(new LinkUpdate(0L, link.uri(), "notification about update", chatIds));
    }

    private void checkLink(LinkUpdateData link, long lastUpdatedId){
        linkService.update(new LinkUpdateData(
                link.uri(),
                lastUpdatedId,
                new Date(new java.util.Date(
                        System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)
                ).getTime()))
        );
    }
}
