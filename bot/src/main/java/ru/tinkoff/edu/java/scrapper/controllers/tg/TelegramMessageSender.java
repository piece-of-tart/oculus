package ru.tinkoff.edu.java.scrapper.controllers.tg;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public interface TelegramMessageSender {
    void sendMessage(SendMessage message);
}
