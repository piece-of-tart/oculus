package ru.tinkoff.edu.java.scrapper.controllers.updates;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

@Component
@Log4j2
public class QueueUpdateListener {
    @Autowired
    private UpdateListener updateListener;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receive(LinkUpdate linkUpdate) {
        log.info(getClass().toString() + " - got link with updates: " + linkUpdate);
        updateListener.sendUpdatesToUser(linkUpdate);
    }

    @RabbitListener(queues = "${rabbitmq.queue}.dlq")
    public void processFailedMessage(Message message) {
        log.warn("Process failed message: {}", message);
    }
}
