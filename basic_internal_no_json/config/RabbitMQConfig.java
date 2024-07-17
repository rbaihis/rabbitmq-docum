package tn.rabbit_mq_node2.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.keyName}")
    private String keyName;
    @Value("${spring.rabbitmq.username}")
    private String userRMQ;
    @Value("${spring.rabbitmq.password}")
    private String pwRMQ;
    @Value("${spring.rabbitmq.host}")
    private String hostRMQ;
    @Value("${spring.rabbitmq.port}")
    private int portRMQ;

    // creating a cashing connectionFactory ( Recommended) but can be ommited if u want
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername(userRMQ);
        connectionFactory.setPassword(pwRMQ);
        connectionFactory.setHost(hostRMQ);
        connectionFactory.setPort(portRMQ);
        return connectionFactory;
    }

    @Bean
    public Queue myTestQueue(){
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange myTestExchange(){
        return new TopicExchange(exchangeName);
    }

    // binding between queue and exchange using routing keyName
    @Bean
    public Binding bindingMyTestQueueToTestExchange(){
        return BindingBuilder
                .bind(myTestQueue()).
                to(myTestExchange())
                .with(keyName);
    }

    /*
     - more beans are required for spring boot app
     - SpringBoot-AutoConfiguration will set those beans automatically
     - can be defined explicitly if use-case is needed for it
     - the required-main-beans:
        ConnectionFactory (Creates connections to the RabbitMQ broker. It's essential for establishing communication.)
        RabbitTemplate (Provides methods to send and receive messages with RabbitMQ. Simplifies the interaction.)
        RabbitAdmin (Automates the creation of exchanges, queues, and bindings by declaring them at startup.)
        Binding (Defines the relationship between an exchange and a queue, using a routing key.)
        queue (Stores messages until they are consumed.)
        exchange (Routes messages to queues based on routing rules. Types include direct, topic, fanout, and headers.)
     - other beans :
        MessageConverter (Converts messages to and from different formats (e.g., JSON).)
        SimpleMessageListenerContainer (Manages the lifecycle of message listeners and threads.)
        CachingConnectionFactory (Enhances performance by caching connections and sessions.)
        AmqpTemplate (A higher-level abstraction over RabbitTemplate for AMQP operations.)
        DirectExchange (A type of exchange that routes messages with a specific routing key to queues.)
        FanoutExchange (A type of exchange that routes messages to all bound queues, ignoring routing keys.)
        TopicExchange ( Routes messages to queues based on matching between a message routing key and the pattern that was used to bind a queue to an exchange.)
    */
}
