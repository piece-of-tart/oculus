package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.tinkoff.edu.java.scrapper.controllers.tg.TelegramController;

@Log4j2
@Component
public class TelegramBotInitializer {
    private final TelegramController telegramController;

    @Autowired
    public TelegramBotInitializer(TelegramController bot) {
        this.telegramController = bot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramController);
        } catch (TelegramApiException e) {
            log.error("Couldn't create connection with telegram. "
                    + "Class: " + e.getClass() + ", message: " + e.getMessage());
        }
    }
}
