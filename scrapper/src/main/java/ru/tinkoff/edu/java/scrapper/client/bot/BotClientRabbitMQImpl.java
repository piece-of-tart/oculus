package ru.tinkoff.edu.java.scrapper.client.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkUpdate;

@RequiredArgsConstructor
@Log4j2
public class BotClientRabbitMQImpl implements BotClient {
    private RabbitTemplate rabbitTemplate;
    private Binding binding;

    public BotClientRabbitMQImpl(RabbitTemplate rabbitTemplate, Binding binding) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
    }

    public void sendNotification(LinkUpdate linkUpdate) {
        log.warn("rabbitTemplate.getMessageConverter(): " + rabbitTemplate.getMessageConverter());
        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), linkUpdate);
        System.out.println("[x] Sent '" + linkUpdate + "' :to: " + binding.getExchange());
    }
}
