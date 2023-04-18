package ru.tinkoff.edu.java.scrapper.client.bot;

import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

public interface BotClient {
    void sendNotification(LinkUpdate linkUpdate);
}
