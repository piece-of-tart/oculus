package ru.tinkoff.edu.java.scrapper.controllers.tg.commands;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
public class HelpCommand extends AbstractCommand {

    private static final String response = Map.of(
                    "/start", "Let you register new user. You can have several users in one tg account.",
                    "/help", "Show all commands that bot supporting and their descriptions.",
                    "/track", "Let you subscribe on link. It's  List of tracking links unique for every user.",
                    "/untrack", "Let you unsubscribe from link. List of tracking links unique for every user.",
                    "/list", "Show all tracking by you links.")
            .entrySet()
            .stream()
            .map(e -> e.getKey() + " - " + e.getValue() + "\n")
            .collect(Collectors.joining());

    public HelpCommand(ScrapperSender scrapperSender) {
        super(scrapperSender);
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(response);
        isDone = true;
        log.info("Ready to send message in helpCommand");
        return sendMessage;
    }
}
