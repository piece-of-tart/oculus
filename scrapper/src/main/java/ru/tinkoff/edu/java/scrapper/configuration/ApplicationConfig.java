package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.scheduling.Scheduler;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@ComponentScan("ru/tinkoff/edu/java/scrapper/scheduling")
public class ApplicationConfig {

    @Bean
    public Scheduler getScheduler(@Value("${scheduler.interval}") Integer milliseconds) {
        return new Scheduler(Duration.ofMillis(milliseconds));
    }
}