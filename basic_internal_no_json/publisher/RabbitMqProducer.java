package tn.rabbit_mq_node2.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.keyName}")
    private String keyName;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqProducer.class);
    private  final RabbitTemplate rabbitTemplate;

    @Autowired //autowiring  the template
    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage( String message){
        LOGGER.info(String.format("Message to publish sent -> %s",message));
        // template used to send and convert message
        rabbitTemplate.convertAndSend(exchangeName,keyName,message);

    }
}
