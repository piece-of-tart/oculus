package ru.tinkoff.edu.java.scrapper.tg.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.edu.java.scrapper.client.ScrapperSender;

public class StartCommand extends AbstractCommand {
    public StartCommand(ScrapperSender scrapperSender) {
        super(scrapperSender);
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        String wasRegisteredBefore = scrapperSender.registerNewUser(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("User with name " + message.getFrom().getUserName() + " " +
                wasRegisteredBefore);
        isDone = true;
        return sendMessage;
    }
}
