package ru.tinkoff.edu.java.scrapper.tg.commands;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;

import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
public class UntrackCommand extends AbstractCommand {
    public UntrackCommand(ScrapperSender scrapperSender) {
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
            log.info("We caught message without text. ChatId=" + message.getChatId() + "; userName=" + message.getFrom());
            return new SendMessage(stringChatId, "Server got message without text.");
        }
        if ("/untrack".equals(messageText)) {
            textResponse = "Enter link, please.";
        } else {
            try {
                scrapperSender.deleteTrackedLink(message.getChatId(), new RemoveLinkRequest(new URI(messageText)));
                textResponse = "Now, link " + messageText + " is untracked.";
                isDone = true;
            } catch (URISyntaxException e) {
                textResponse = "This is not a correct link.";
            }
        }
        return new SendMessage(stringChatId, textResponse);
    }
}
