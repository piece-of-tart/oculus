package ru.tinkoff.edu.java.scrapper.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.edu.java.scrapper.dto.jpa.ChatLink;
import ru.tinkoff.edu.java.scrapper.dto.jpa.ChatLinkId;

import java.util.List;

public interface JpaChatLinkDao extends JpaRepository<ChatLink, ChatLinkId> {
    List<ChatLink> findAllByLinkId(Long linkId);

    @Query("SELECT cl FROM ChatLink cl WHERE cl.chatId = :chatId")
    List<ChatLink> findAllByChatId(Long chatId);
}
