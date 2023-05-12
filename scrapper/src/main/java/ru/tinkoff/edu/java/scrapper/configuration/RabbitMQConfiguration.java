package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.Arrays;

@Configuration(value = "rabbitMQConfiguration")
@ConditionalOnProperty(prefix = "app", name = "useQueue", havingValue = "true")
@ConfigurationProperties
public class RabbitMQConfiguration {

    @Bean(name = "queue")
    public Queue myQueue(@Value("${rabbitmq.queue}") String queueName, @Value("${rabbitmq.exchange.dlx}") String exchangeDlx) {
        return QueueBuilder.durable(queueName).deadLetterExchange(exchangeDlx).build();
    }

    @Bean(name = "exchange")
    public DirectExchange exchange(@Value("${rabbitmq.exchange}") String exchange) {
        return new DirectExchange(exchange, true, false);
    }

    @Bean(name = "binding")
    public Binding binding(@Qualifier(value = "queue") Queue queue, @Qualifier(value = "exchange") DirectExchange exchange,
                           @Value("${rabbitmq.routingKey}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean(name = "deadLetterExchange")
    public FanoutExchange deadLetterExchange(@Value("${rabbitmq.exchange.dlx}") String exchangeDlx) {
        return new FanoutExchange(exchangeDlx);
    }

    @Bean(name = "deadLetterQueue")
    public Queue deadLetterQueue(@Value("${rabbitmq.queue.dlx}") String queueDlxName) {
        return QueueBuilder.durable(queueDlxName).build();
    }

    @Bean
    public Binding deadLetterBinding(@Qualifier("deadLetterQueue") Queue deadLetterQueue,
                                     @Qualifier("deadLetterExchange") FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
