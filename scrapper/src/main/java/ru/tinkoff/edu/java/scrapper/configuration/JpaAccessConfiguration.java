package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaChatDao;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaChatLinkDao;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaLinkDao;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaLinkTypeDao;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

@Configuration
@ConfigurationProperties
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@AllArgsConstructor
public class JpaAccessConfiguration {
    private final JpaChatDao jpaChatDao;
    private final JpaChatLinkDao jpaChatLinkDao;
    private final JpaLinkDao jpaLinkDao;
    private final JpaLinkTypeDao jpaLinkTypeDao;

    @Bean
    public TgChatService chatService() {
        return new JpaTgChatService(jpaChatDao);
    }

    @Bean
    public LinkService linkService(){
        return new JpaLinkService(jpaLinkDao, jpaChatLinkDao, jpaLinkTypeDao);
    }
}
