package ru.tinkoff.edu.java.scrapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.configuration.JdbcAccessConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.JpaAccessConfiguration;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkUpdate;

import java.net.URI;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(value = {ApplicationConfig.class, JdbcAccessConfiguration.class, JpaAccessConfiguration.class})
@Log4j2
public class ScrapperApplication {
    public static void main(String[] args) {
        final var context = SpringApplication.run(ScrapperApplication.class, args);
        final var config = context.getBean(ApplicationConfig.class);
    }
}
