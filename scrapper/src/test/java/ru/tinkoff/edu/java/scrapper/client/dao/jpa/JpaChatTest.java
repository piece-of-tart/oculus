package ru.tinkoff.edu.java.scrapper.client.dao.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.client.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.ChatEntity;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@SpringBootTest(classes = {ScrapperApplication.class, IntegrationEnvironment.IntegrationEnvironmentConfiguration.class},
        properties = { "app.database-access-type=jpa" })
public class JpaChatTest extends IntegrationEnvironment {
    @Container
    public PostgreSQLContainer<?> postgreSQLContainer = POSTGRES_SQL_CONTAINER;
    @Autowired
    private JpaTgChatService jpaTgChatService;

    @Transactional
    @Rollback
    @Test
    public void addTest() {
        List<ChatEntity> chatEntitiesSent = getSampleListOfEntities();
        for (var chat : chatEntitiesSent) {
            jpaTgChatService.register(chat.id());
        }
        for (var chat : chatEntitiesSent) {
            Assertions.assertTrue(jpaTgChatService.exists(chat.id()));
        }
    }

    @Transactional
    @Rollback
    @Test
    public void removeTest() {
        List<ChatEntity> chatEntitiesSent = getSampleListOfEntities();
        for (var chat : chatEntitiesSent) {
            jpaTgChatService.register(chat.id());
        }
        for (var chat : chatEntitiesSent) {
            jpaTgChatService.unregister(chat.id());
            Assertions.assertFalse(jpaTgChatService.exists(chat.id()));
        }
    }

    private List<ChatEntity> getSampleListOfEntities() {
        final long[] chatIds = RandomGenerator.getDefault().longs().limit(10).toArray();
        List<ChatEntity> chatEntityList = new ArrayList<>();
        for (long chatId : chatIds) {
            chatEntityList.add(new ChatEntity(chatId));
        }
        return chatEntityList;
    }
}
