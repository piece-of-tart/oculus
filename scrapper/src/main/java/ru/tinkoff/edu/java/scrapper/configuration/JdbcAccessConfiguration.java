package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.dao.jdbc.JdbcChatDao;
import ru.tinkoff.edu.java.scrapper.dao.jdbc.JdbcLinkDao;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;


@Configuration
@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {

    private final JdbcChatDao jdbcChatDao;
    private final JdbcLinkDao jdbcLinkDao;
    @Bean
    public TgChatService chatService() {
        return new JdbcChatService(jdbcChatDao);
    }

    @Bean
    public LinkService linkService(){
        return new JdbcLinkService(jdbcLinkDao);
    }
}