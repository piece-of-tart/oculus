package ru.tinkoff.edu.java.scrapper.dao.jdbc;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.ChatEntity;

import java.util.ArrayList;
import java.util.List;

public class JdbcChatDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcChatDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(ChatEntity chatEntity) {
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatEntity.id());
    }

    public ChatEntity remove(long chatId) {
        String deleteChatLinksQuery = "DELETE FROM chat_link WHERE chat_id = ?";
        jdbcTemplate.update(deleteChatLinksQuery, chatId);
        String deleteChatQuery = "DELETE FROM chat WHERE id = ?";
        jdbcTemplate.update(deleteChatQuery, chatId);
        return new ChatEntity(chatId);
    }

    public List<ChatEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", rs -> {
            List<ChatEntity> entityList = new ArrayList<>();
            while (rs.next()) {
                entityList.add(new ChatEntity(rs.getLong("id")));
            }
            return entityList;
        });
    }


    public ChatEntity findById(long chatId) {
        return jdbcTemplate.query("SELECT id FROM chat WHERE id = ?",
                ps -> { ps.setLong(1, chatId); }, rse -> {
            ChatEntity chatEntity = null;
            while (rse.next()) {
                chatEntity = new ChatEntity(rse.getLong("id"));
            }
            return chatEntity;
        });
    }
}
