package ru.tinkoff.edu.java.scrapper.tg.commands;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.dto.LinkDTO;
import ru.tinkoff.edu.java.scrapper.dto.UserTgData;
import ru.tinkoff.edu.java.scrapper.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandHandler {
    final private static Map<Long, UserTgData> userByChatId = new HashMap<>();
    final private static Set<String> supportedCommands = Set.of("/start", "/help", "/track", "/untrack", "/list");
    public static java.util.List<SendMessage> commandHandle(Message message) {
        java.util.List<SendMessage> sendMessageList = new ArrayList<>();
        final String command = message.getText();
        UserTgData userTgData = userByChatId.get(message.getChatId());
        String greeting = null;
        if (userTgData == null) {
            sendMessageList.add(startCommand(message.getChatId(), message.getFrom().getUserName()));
            userTgData = userByChatId.get(message.getChatId());
        }
        sendMessageList.add(
                switch (command) {
            case "/help" -> helpCommand(message.getChatId());
            case "/list" -> listCommand(message.getChatId());
            case "/start" -> startCommand(message.getChatId(), message.getFrom().getUserName());
            case "/track", "/untrack" -> new SendMessage(String.valueOf(message.getChatId()), "Enter link");
            default ->  {
                if (userTgData.getLastSentCommand() == null) {
                    yield unknownCommand(message.getChatId());
                }
                yield switch (userTgData.getLastSentCommand()) {
                    case "/track" -> trackCommand(message.getChatId(), message.getText());
                    case "/untrack" -> untrackCommand(message.getChatId(), message.getText());
                    default -> unknownCommand(message.getChatId());
                };
            }
        });
        if (message.getText().equals(userTgData.getLastSentCommand())) {
            userTgData.setCommandStage(userTgData.getCommandStage() + 1);
        } else {
            userTgData.setLastSentCommand(supportedCommands.contains(message.getText()) ? message.getText() : null);
            userTgData.setCommandStage(0);
        }
        return sendMessageList;
    }

    private static SendMessage untrackCommand(Long chatId, String url) {
        clearLastCommandAndCommandStage(chatId);
        UserDTO userDTO = userByChatId.get(chatId).getUserDTO();
        boolean wasDeleted = userDTO.trackedLinks().remove(new LinkDTO(url));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(wasDeleted ? url + " was successfully deleted." : url + " isn't being tracked.");
        return sendMessage;
    }

    private static SendMessage trackCommand(long chatId, String url) {
        clearLastCommandAndCommandStage(chatId);
        UserDTO userDTO = userByChatId.get(chatId).getUserDTO();
        userDTO.trackedLinks().add(new LinkDTO(url));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(url + " is tracked now.");
        return sendMessage;
    }

    private static void clearLastCommandAndCommandStage(long chatId) {
        UserTgData userTgData = userByChatId.get(chatId);
        userTgData.setLastSentCommand(null);
        userTgData.setCommandStage(0);
    }

    private static SendMessage startCommand(long chatId, @NonNull String username) {
        UserTgData user = userByChatId.putIfAbsent(chatId, new UserTgData(new UserDTO(username, new ArrayList<>())));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("User with name " + username +
                (user == null ? " successfully registered." : " already exists."));
        return sendMessage;
    }

    private static SendMessage helpCommand(long chatId) {
        return Help.getSendMessage(chatId);
    }

    private static SendMessage listCommand(long chatId) {
        return List.getSendMessage(chatId);
    }

    private static SendMessage unknownCommand(long chatId) {
        return  new SendMessage(String.valueOf(chatId), "Unknown command");
    }


    private static class Help {
        final private static String response = Map.of(
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

    private static class List {
        private static SendMessage getSendMessage(long chatId) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            UserTgData userTgData = userByChatId.get(chatId);
            if (userTgData == null) {
                throw new IllegalStateException();
            }
            UserDTO userDTO = userTgData.getUserDTO();
            if (userDTO.trackedLinks().size() == 0) {
                sendMessage.setText("You have no tracking links.");
            } else {
                sendMessage.setText(userDTO
                        .trackedLinks()
                        .stream()
                        .map(LinkDTO::url)
                        .collect(Collectors.joining("\n")));
            }
            return sendMessage;
        }
    }
}
