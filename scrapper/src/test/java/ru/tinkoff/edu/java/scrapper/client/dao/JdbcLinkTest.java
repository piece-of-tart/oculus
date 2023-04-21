package ru.tinkoff.edu.java.scrapper.client.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import ru.tinkoff.edu.java.scrapper.client.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.dao.JdbcChatDao;
import ru.tinkoff.edu.java.scrapper.dao.JdbcLinkDao;
import ru.tinkoff.edu.java.scrapper.dto.LinkEntity;

import javax.sql.DataSource;
import java.net.URI;
import java.util.List;

@SpringBootTest
public class JdbcLinkTest extends IntegrationEnvironment {
    @Container
    public PostgreSQLContainer<?> postgresqlContainer = POSTGRES_SQL_CONTAINER;
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(testDataSource());
    private final JdbcLinkDao linkRepository = new JdbcLinkDao(jdbcTemplate);

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
        List<LinkEntity> linkEntitiesSent = getSampleListOfEntities();
        for (var link : linkEntitiesSent) {
            linkRepository.add(link);
        }
        for (var link : linkEntitiesSent) {
            Assert.assertTrue(jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = ? AND description = ?",
                            link.chatId(), link.description()) != 0);
        }
    }

    @Transactional
    @Rollback
    @Test
    public void removeTest() {
        List<LinkEntity> linkEntitiesSent = getSampleListOfEntities();
        for (var link : linkEntitiesSent) {
            linkRepository.add(link);
        }
        for (var link : linkEntitiesSent) {
            Assert.assertEquals(link, linkRepository.remove(link.chatId(), link.uri()));
        }
        for (var link : linkEntitiesSent) {
            Assert.assertEquals(0, jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = ? AND description = ?",
                    link.chatId(), link.description()));
        }
    }

    @Transactional
    @Rollback
    @Test
    public void findAllTest() {
        List<LinkEntity> linkEntitiesSent = getSampleListOfEntities();
        for (var link : linkEntitiesSent) {
            linkRepository.add(link);
        }
        List<LinkEntity> linkEntitiesReceived = linkRepository.findAll();
        for (var link : linkEntitiesSent) {
            Assert.assertTrue(linkEntitiesReceived.contains(link));
        }
    }

    private List<LinkEntity> getSampleListOfEntities() {
        return List.of(
                new LinkEntity(URI.create("matlab.com"), -100000, "Destiny"),
                new LinkEntity(URI.create("wolfram.info"), -100000, "Wolframchik helps us to solve integrals"),
                new LinkEntity(URI.create("tinkoff.edu"), -100000, "Tinkoff's educational platform")
        );
    }
}