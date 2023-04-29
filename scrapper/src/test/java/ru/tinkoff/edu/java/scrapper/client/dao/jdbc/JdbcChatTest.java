package ru.tinkoff.edu.java.scrapper.client.dao.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.client.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dao.jdbc.JdbcChatDao;
import ru.tinkoff.edu.java.scrapper.dto.jdbc.ChatEntity;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@SpringBootTest
public class JdbcChatTest extends IntegrationEnvironment {
    @Container
    public PostgreSQLContainer<?> postgresqlContainer = POSTGRES_SQL_CONTAINER;
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(testDataSource());
    private final JdbcChatDao chatRepository = new JdbcChatDao(jdbcTemplate);

    public DataSource testDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresqlContainer.getDriverClassName());
        dataSource.setUrl(postgresqlContainer.getJdbcUrl());
        dataSource.setUsername(postgresqlContainer.getUsername());
        dataSource.setPassword(postgresqlContainer.getPassword());
        return dataSource;
    }

    @Transactional
    @Rollback
    @Test
    public void addTest() {
        List<ChatEntity> chatEntitiesSent = getSampleListOfEntities();
        for (var chat : chatEntitiesSent) {
            chatRepository.add(chat);
        }
        for (var chat : chatEntitiesSent) {
            Assert.assertTrue(jdbcTemplate.update("DELETE FROM chat WHERE id = ?", chat.id()) != 0);
        }
    }

    @Transactional
    @Rollback
    @Test
    public void removeTest() {
        List<ChatEntity> chatEntitiesSent = getSampleListOfEntities();
        for (var chat : chatEntitiesSent) {
            chatRepository.add(chat);
        }
        for (var chat : chatEntitiesSent) {
            Assert.assertEquals(chat, chatRepository.remove(chat.id()));
        }
        for (var chat : chatEntitiesSent) {
            Assert.assertEquals(0, jdbcTemplate.update("DELETE FROM chat WHERE id = ?;", chat.id()));
        }
    }

    @Transactional
    @Rollback
    @Test
    public void findAllTest() {
        List<ChatEntity> chatEntitiesSent = getSampleListOfEntities();
        for (var chat : chatEntitiesSent) {
            chatRepository.add(chat);
        }
        List<ChatEntity> chatEntitiesReceived = chatRepository.findAll();
        for (var chat : chatEntitiesReceived) {
            Assert.assertTrue(chatEntitiesReceived.contains(chat));
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
