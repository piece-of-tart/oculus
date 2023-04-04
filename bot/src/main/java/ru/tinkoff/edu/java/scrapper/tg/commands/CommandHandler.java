package ru.tinkoff.edu.java.scrapper.tg.commands;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.controllers.ScrapperSender;
import ru.tinkoff.edu.java.scrapper.dto.LastCommandAndStage;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommandHandler {
    final private Map<Long, LastCommandAndStage> lastCommandAndStageByChatId = new HashMap<>();
    final private Set<String> supportedCommands = Set.of("/start", "/help", "/track", "/untrack", "/list");
    private final ScrapperSender scrapperSender;
    @Autowired
    public CommandHandler(ScrapperSender scrapperSender) {
        this.scrapperSender = scrapperSender;
    }

    public java.util.List<SendMessage> commandHandle(Message message) {
        java.util.List<SendMessage> sendMessageList = new ArrayList<>();
        final String command = message.getText();
        LastCommandAndStage lastCommandAndStage = lastCommandAndStageByChatId.get(message.getChatId());
        if (lastCommandAndStage == null) {
            sendMessageList.add(startCommand(message.getChatId(), message.getFrom().getUserName()));
            lastCommandAndStageByChatId.put(message.getChatId(),new LastCommandAndStage("/hello", 0));
            lastCommandAndStage = lastCommandAndStageByChatId.get(message.getChatId());
        }
        sendMessageList.add(
                switch (command) {
            case "/help" -> helpCommand(message.getChatId());
            case "/list" -> listCommand(message.getChatId());
            case "/start" -> startCommand(message.getChatId(), message.getFrom().getUserName());
            case "/track", "/untrack" -> new SendMessage(String.valueOf(message.getChatId()), "Enter link");
            default ->  {
                if (lastCommandAndStage.getLastCommand() == null) {
                    yield unknownCommand(message.getChatId());
                }
                yield switch (lastCommandAndStage.getLastCommand()) {
                    case "/track" -> trackCommand(message.getChatId(), message.getText());
                    case "/untrack" -> untrackCommand(message.getChatId(), message.getText());
                    default -> unknownCommand(message.getChatId());
                };
            }
        });
        if (message.getText().equals(lastCommandAndStage.getLastCommand())) {
            lastCommandAndStage.setStage(lastCommandAndStage.getStage() + 1);
        } else {
            lastCommandAndStage.setLastCommand(supportedCommands.contains(message.getText()) ? message.getText() : null);
            lastCommandAndStage.setStage(0);
        }
        return sendMessageList;
    }

    private SendMessage untrackCommand(Long chatId, String url) {
        clearLastCommandAndCommandStage(chatId);
        String text = scrapperSender.deleteTrackedLink(chatId, url);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    private SendMessage trackCommand(long chatId, String url) {
        clearLastCommandAndCommandStage(chatId);
        String text = scrapperSender.addNewTrackedLink(chatId, url);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    private void clearLastCommandAndCommandStage(long chatId) {
        lastCommandAndStageByChatId.put(chatId, null);
    }

    private SendMessage startCommand(long chatId, @NonNull String username) {
        boolean wasRegisteredBefore = scrapperSender.registerNewUser(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("User with name " + username +
                (wasRegisteredBefore ?  " already exists." : " successfully registered."));
        return sendMessage;
    }

    private SendMessage helpCommand(long chatId) {
        return Help.getSendMessage(chatId);
    }

    private SendMessage listCommand(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        java.util.List<String> links = scrapperSender
                .getLinksByChatId(chatId)
                .links()
                .stream()
                .map(LinkResponse::url)
                .toList();

        if (links.size() == 0) {
            sendMessage.setText("You have no tracking links.");
        } else {
            sendMessage.setText(String.join("\n", links));
        }
        return sendMessage;
    }

    private SendMessage unknownCommand(long chatId) {
        return  new SendMessage(String.valueOf(chatId), "Unknown command");
    }

    private static class Help {
        final static private String response = Map.of(
                        "/start", "Let you register new user. You can have several users in one tg account.",
                        "/help", "Show all commands that bot supporting and their descriptions.",
                        "/track", "Let you subscribe on link. It's  List of tracking links unique for every user.",
                        "/untrack", "Let you unsubscribe from link. List of tracking links unique for every user.",
                        "/list", "Show all tracking by you links.")
                .entrySet()
                .stream()
                .map(e -> e.getKey() + " - " + e.getValue() + "\n")
                .collect(Collectors.joining());

        private static SendMessage getSendMessage(long chatId) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(response);
            return sendMessage;
        }
    }
}

