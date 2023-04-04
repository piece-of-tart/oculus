package ru.tinkoff.edu.java.scrapper.controllers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public interface TelegramMessageSender {
    void sendMessage(SendMessage message);
}
