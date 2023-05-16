package ru.tinkoff.edu.java.scrapper.scheduling;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.ParserLinker;
import ru.tinkoff.edu.java.parser.values.GithubValue;
import ru.tinkoff.edu.java.parser.values.StackOverflowValue;
import ru.tinkoff.edu.java.parser.values.Value;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkEntity;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.LinkUpdateData;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Random;

@Component
@Log4j2
public class LinkUpdaterScheduler {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;
    private final LinkService linkService;

    @Autowired
    public LinkUpdaterScheduler(GitHubClient gitHubClient, StackOverflowClient stackOverflowClient,
                                BotClient botClient, @Qualifier("jpaLinkService") LinkService linkService) {
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
        this.botClient = botClient;
        this.linkService = linkService;
    }

    @Scheduled(fixedDelayString = "#{@getScheduler.interval.toMillis()}")
    public void update() {
        log.debug("Update " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
                .format(ZonedDateTime.now(ZoneId.of("Europe/Moscow"))));
        checkGitHubLinks();
        checkStackOverflowLinks();
    }

    private void checkStackOverflowLinks() {
        List<LinkUpdateData> linkList = linkService.getLinksByType("stackoverflow.com");
        for (LinkUpdateData link : linkList) {
            Value questionId = new ParserLinker().parse(link.uri().toString());
            if (questionId instanceof StackOverflowValue stackOverflowValue) {
                StackOverflowResponse stackOverflowResponse =
                        stackOverflowClient.getQuestion(stackOverflowValue.id()).block();
                assert stackOverflowResponse != null;
                OffsetDateTime lastActivityDate = stackOverflowResponse.getItems().get(0).getLastActivityDate();
                OffsetDateTime lastActivityInDatabase =
                        OffsetDateTime.ofInstant(link.lastChecked().toInstant(), ZoneOffset.UTC);
                if (lastActivityInDatabase.isBefore(lastActivityDate)) {
                    sendNotifications(link);
                }
                checkLink(link, new Random().nextLong());
            }
        }
    }

    private void checkGitHubLinks() {
        List<LinkUpdateData> linkList = linkService.getLinksByType("github.com");
        for (LinkUpdateData link : linkList) {
            Value questionId = new ParserLinker().parse(link.uri().toString());
            if (questionId instanceof GithubValue githubValue) {
                var lastRepositoryEvent = gitHubClient.getRepository(githubValue.user(), githubValue.rep()).block();
                assert lastRepositoryEvent != null;
                if (lastRepositoryEvent.getETag() == null) {
                    log.warn("Catch ban from github.com");
                    log.warn("checkGithubLinks: " + lastRepositoryEvent);
                    return;
                }
                int lastHashETagCode = lastRepositoryEvent.getETag().hashCode();
                if (lastHashETagCode != link.lastUpdatedId()) {
                    sendNotifications(link);
                }
                checkLink(link, lastHashETagCode);
            }
        }
    }

    private void sendNotifications(LinkUpdateData link) {
        List<Long> chatIds = linkService.getLinksByUri(link.uri()).stream().map(LinkEntity::chatId).toList();
        botClient.sendNotification(new LinkUpdate(0L, link.uri(), "notification about update", chatIds));
    }

    private void checkLink(LinkUpdateData link, long lastUpdatedId) {
        linkService.update(new LinkUpdateData(
                link.uri(),
                lastUpdatedId,
                new Date(new java.util.Date(System.currentTimeMillis()).getTime()))
        );
    }
}
