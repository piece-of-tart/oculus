package ru.tinkoff.edu.java.scrapper.controllers.updates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.tinkoff.edu.java.scrapper.controllers.tg.TelegramMessageSender;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

@Component
public class UpdateListener {
    @Autowired
    private TelegramMessageSender telegramMessageSender;

    public void sendUpdatesToUser(LinkUpdate linkUpdate) {
        for (long tgChatId : linkUpdate.tgChatIds()) {
            telegramMessageSender.sendMessage(
                    new SendMessage(String.valueOf(tgChatId),
                            "Updates in this source: " + linkUpdate.uri().toString())
            );
        }
    }
}
