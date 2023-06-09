package ru.tinkoff.edu.java.scrapper.controllers.tg;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tinkoff.edu.java.scrapper.configuration.TelegramConfiguration;

import java.util.List;

@Log4j2
@Component
@Controller
public class TelegramController extends TelegramLongPollingBot implements TelegramMessageSender {
    final private TelegramConfiguration telegramConfiguration;
    final private CommandHandler commandHandler;

    @Autowired
    public TelegramController(TelegramConfiguration telegramConfiguration, CommandHandler commandHandler) {
        super(new DefaultBotOptions(), telegramConfiguration.getBotToken());
        this.telegramConfiguration = telegramConfiguration;
        this.commandHandler = commandHandler;
        setCommandMenu();
    }

    private void setCommandMenu() {
        List<BotCommand> commands = List.of(
                new BotCommand("/start", "register new user"),
                new BotCommand("/help", "show window with commands"),
                new BotCommand("/track", "start to track new link"),
                new BotCommand("/untrack", "stop to track link"),
                new BotCommand("/list", "show list of tracking links")
        );
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            logError("Couldn't set command menu in telegram.", e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            final Message message = update.getMessage();
            if (message.hasText()) {
                commandHandler.commandHandle(message).forEach(this::sendMessage);
            }
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logError("Error when trying to send a message.", e.getMessage());
        }
    }

    private void logError(final String event, final String message) {
        log.error("[Class]: " + getClass() + ", [event]: " + event + ", [message]: " + message);
    }

    @Override
    public String getBotUsername() {
        return telegramConfiguration.getBotName();
    }
}
