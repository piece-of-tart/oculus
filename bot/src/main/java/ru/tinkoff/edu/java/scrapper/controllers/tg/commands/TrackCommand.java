package ru.tinkoff.edu.java.scrapper.controllers.tg.commands;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;

import java.net.URI;

@Log4j2
public class TrackCommand extends AbstractCommand {
    private URI uri = null;
    private String description = null;

    public TrackCommand(ScrapperSender scrapperSender) {
        super(scrapperSender);
    }

    @Override
    public SendMessage handle(Message message) {
        final String messageText;
        final String stringChatId = String.valueOf(message.getChatId());
        String textResponse;
        if (message.hasText()) {
            messageText = message.getText();
        } else {
            log.info("We caught message without text. ChatId=" + message.getChatId()
                    + "; userName=" + message.getFrom());
            return new SendMessage(stringChatId, "Server got message without text.");
        }
        if ("/track".equals(messageText)) {
            textResponse = "Enter link, please.";
        } else if (uri == null) {
            try {
                uri = URI.create(messageText);
                textResponse = "Enter description, please.";
            } catch (RuntimeException e) {
                textResponse = "This is not a correct/supported link. Enter another one.";
            }
        } else if (description == null) {
            description = messageText;
            scrapperSender.addNewTrackedLink(message.getChatId(), new AddLinkRequest(uri, description));
            textResponse = "Now, link " + uri.toString() + " is tracked.";
            isDone = true;
        } else {
            log.warn("Illegal state: we execute method when link has had tracked yet.");
            textResponse = "Server couldn't handle with this message, try again, please.";
        }
        return new SendMessage(stringChatId, textResponse);
    }
}
