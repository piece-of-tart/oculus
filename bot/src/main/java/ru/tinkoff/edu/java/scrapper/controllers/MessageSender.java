package ru.tinkoff.edu.java.scrapper.controllers;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface MessageSender {
    void sendMessage(SendMessage message);
}
