package ru.tinkoff.edu.java.scrapper.controllers.tg;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.AbstractCommand;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.HelpCommand;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.ListCommand;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.StartCommand;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.TrackCommand;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.UnknownCommand;
import ru.tinkoff.edu.java.scrapper.controllers.tg.commands.UntrackCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Log4j2
public class CommandHandler {
    private final Map<Long, AbstractCommand> lastCommandByChatId = new HashMap<>();
    private final Set<Long> registeredChatIds = new HashSet<>();
    private final ScrapperSender scrapperSender;
    @Autowired
    public CommandHandler(ScrapperSender scrapperSender) {
        this.scrapperSender = scrapperSender;
    }

    public List<SendMessage> commandHandle(Message message) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        final String messageText = message.getText();
        final AbstractCommand lastCommand = lastCommandByChatId.get(message.getChatId());

        log.warn("CHAT_ID:" + message.getChatId());

        if (!registeredChatIds.contains(message.getChatId())) {
            if (!"/start".equals(messageText)) {
                AbstractCommand registerCommand = new StartCommand(scrapperSender);
                sendMessageList.add(registerCommand.handle(message));
            }
            registeredChatIds.add(message.getChatId());
        }

        final AbstractCommand commandNow;
        if (lastCommand == null) {
            commandNow = switch (messageText) {
                case "/help" -> new HelpCommand(scrapperSender);
                case "/list" -> new ListCommand(scrapperSender);
                case "/start" -> new StartCommand(scrapperSender);
                case "/track" -> new TrackCommand(scrapperSender);
                case "/untrack" -> new UntrackCommand(scrapperSender);
                default -> new UnknownCommand(scrapperSender);
            };
            lastCommandByChatId.put(message.getChatId(), commandNow);
        } else {
            commandNow = lastCommand;
        }

        sendMessageList.add(commandNow.handle(message));
        if (commandNow.isDone()) {
            lastCommandByChatId.remove(message.getChatId());
        }

        return sendMessageList;
    }
}
