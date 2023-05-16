package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource(value = "classpath:application.properties")
public class TelegramConfiguration {
    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String botToken;
}
