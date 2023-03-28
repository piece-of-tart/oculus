package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.scheduling.Scheduler;
import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@ComponentScan("ru/tinkoff/edu/java/scrapper/scheduling")
public class ApplicationConfig {
    @Autowired
    private Scheduler scheduler;

    public Scheduler getScheduler() {
        return scheduler;
    }
}