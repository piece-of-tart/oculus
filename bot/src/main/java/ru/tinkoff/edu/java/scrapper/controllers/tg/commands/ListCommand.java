package ru.tinkoff.edu.java.scrapper.controllers.tg.commands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;

import java.util.List;

public class ListCommand extends AbstractCommand {
    public ListCommand(ScrapperSender scrapperSender) {
        super(scrapperSender);
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());

        List<String> links = scrapperSender
                .getLinksByChatId(message.getChatId())
                .stream()
                .map(linkResponse -> linkResponse.uri().toString())
                .toList();

        if (links.size() == 0) {
            sendMessage.setText("You have no tracking links.");
        } else {
            sendMessage.setText(String.join("\n", links));
        }
        isDone = true;
        return sendMessage;
    }
}
