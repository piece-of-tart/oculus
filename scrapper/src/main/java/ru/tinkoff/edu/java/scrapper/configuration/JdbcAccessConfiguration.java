package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.dao.jdbc.JdbcChatDao;
import ru.tinkoff.edu.java.scrapper.dao.jdbc.JdbcLinkDao;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;

import javax.sql.DataSource;


@Configuration
@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JdbcChatDao jdbcChatDao(JdbcTemplate jdbcTemplate) {
        return new JdbcChatDao(jdbcTemplate);
    }

    @Bean
    public JdbcLinkDao jdbcLinkDao(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkDao(jdbcTemplate);
    }

    @Bean
    public TgChatService chatService(JdbcChatDao jdbcChatDao) {
        return new JdbcChatService(jdbcChatDao);
    }

    @Bean
    public LinkService linkService(JdbcLinkDao jdbcLinkDao){
        return new JdbcLinkService(jdbcLinkDao);
    }
}