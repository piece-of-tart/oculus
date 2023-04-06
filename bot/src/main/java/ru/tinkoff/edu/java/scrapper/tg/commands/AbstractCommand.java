package ru.tinkoff.edu.java.scrapper.tg.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;

public abstract class AbstractCommand {
    protected final ScrapperSender scrapperSender;
    protected boolean isDone;

    public AbstractCommand(ScrapperSender scrapperSender) {
        this.scrapperSender = scrapperSender;
        this.isDone = false;
    }

    public abstract SendMessage handle(Message message);

    public boolean isDone() {
        return isDone;
    }
}
