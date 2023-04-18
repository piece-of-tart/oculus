package ru.tinkoff.edu.java.scrapper.client;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IntegrationEnvironment {
    protected static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER;
    static final String configFileName = "master.xml";
    static final Path pathToConfigFile = Path.of("./").toAbsolutePath().getParent().getParent().resolve("migrations/");

    static {
        POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:14");
        POSTGRES_SQL_CONTAINER.start();

        try {
            Connection connection = DriverManager.getConnection(
                    POSTGRES_SQL_CONTAINER.getJdbcUrl(),
                    POSTGRES_SQL_CONTAINER.getUsername(),
                    POSTGRES_SQL_CONTAINER.getPassword()
            );
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    configFileName,
                    new DirectoryResourceAccessor(pathToConfigFile),
                    database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
