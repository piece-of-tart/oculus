package ru.tinkoff.edu.java.scrapper.client;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

@Log4j2
public class SimpleIntegrationTest extends IntegrationEnvironment {
    @Test
    public void easyTest() {
        Assertions.assertTrue(POSTGRES_SQL_CONTAINER.isCreated());
        log.error(POSTGRES_SQL_CONTAINER.getJdbcUrl());
        log.error(POSTGRES_SQL_CONTAINER.getUsername());
        log.error(POSTGRES_SQL_CONTAINER.getPassword());
        log.error(POSTGRES_SQL_CONTAINER.getDatabaseName());
        log.error(POSTGRES_SQL_CONTAINER.getContainerId());
    }
}
