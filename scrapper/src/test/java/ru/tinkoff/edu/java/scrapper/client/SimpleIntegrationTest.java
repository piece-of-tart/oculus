package ru.tinkoff.edu.java.scrapper.client;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SimpleIntegrationTest extends IntegrationEnvironment {
    @Test
    public void easyTest() {
        Assertions.assertTrue(POSTGRES_SQL_CONTAINER.isCreated());
    }
}
