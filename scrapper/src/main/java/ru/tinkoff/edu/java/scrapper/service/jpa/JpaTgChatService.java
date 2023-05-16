package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dao.jpa.JpaChatDao;
import ru.tinkoff.edu.java.scrapper.dto.jpa.Chat;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {

    private final JpaChatDao jpaChatDao;

    @Override
    @Transactional
    public void register(long tgChatId) {
        jpaChatDao.save(new Chat(tgChatId));
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        jpaChatDao.delete(new Chat(tgChatId));
    }

    @Override
    @Transactional
    public boolean exists(long tgChatId) {
        return jpaChatDao.existsById(tgChatId);
    }
}
