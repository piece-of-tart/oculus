package ru.tinkoff.edu.java.scrapper.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.dto.jpa.Chat;

public interface JpaChatDao extends JpaRepository<Chat, Long> {
}
