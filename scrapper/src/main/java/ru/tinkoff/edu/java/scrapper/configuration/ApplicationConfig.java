package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClientImpl;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClientRabbitMQImpl;
import ru.tinkoff.edu.java.scrapper.scheduling.Scheduler;

import java.time.Duration;

@Validated
@ConfigurationProperties
@PropertySource(value = "classpath:application.properties")
@ComponentScan("ru/tinkoff/edu/java/scrapper/scheduling")
public class ApplicationConfig {
    @Bean
    public Scheduler getScheduler(@Value("${scheduler.interval}") Integer milliseconds) {
        return new Scheduler(Duration.ofMillis(milliseconds));
    }

    @Bean(name = "botClient")
    @ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "false")
    public BotClient botClientWithoutQueue(@Value("${app.telegram.bot.base_url}") String baseUrl) {
        if (baseUrl != null) {
            return new BotClientImpl(baseUrl);
        }
        return new BotClientImpl();
    }

    @Bean(name = "botClient")
    @DependsOn({"rabbitMQConfiguration"})
    @ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "true")
    public BotClient botClientWithQueue(RabbitTemplate rabbitTemplate, Binding binding) {
        return new BotClientRabbitMQImpl(rabbitTemplate, binding);
    }
}
