package ru.tinkoff.edu.java.scrapper.controllers.tg.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;

public class UnknownCommand extends AbstractCommand {
    public UnknownCommand(ScrapperSender scrapperSender) {
        super(scrapperSender);
    }

    @Override
    public SendMessage handle(Message message) {
        isDone = true;
        return new SendMessage(String.valueOf(message.getChatId()), "This command isn't supported.");
    }
}
