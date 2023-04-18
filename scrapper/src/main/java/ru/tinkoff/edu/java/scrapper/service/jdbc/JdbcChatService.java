package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.jvnet.hk2.annotations.Service;
import ru.tinkoff.edu.java.scrapper.dao.JdbcChatDao;
import ru.tinkoff.edu.java.scrapper.dto.ChatEntity;
import ru.tinkoff.edu.java.scrapper.service.TgChatService;

@Service
public class JdbcChatService implements TgChatService {
    private final JdbcChatDao jdbcChatDao;

    public JdbcChatService(JdbcChatDao jdbcChatDao) {
        this.jdbcChatDao = jdbcChatDao;
    }

    @Override
    public void register(long tgChatId) {
        jdbcChatDao.add(new ChatEntity(tgChatId));
    }

    @Override
    public void unregister(long tgChatId) {
        jdbcChatDao.remove(tgChatId);
    }

    @Override
    public boolean exists(long id) {
        return jdbcChatDao.findById(id) != null;
    }
}
