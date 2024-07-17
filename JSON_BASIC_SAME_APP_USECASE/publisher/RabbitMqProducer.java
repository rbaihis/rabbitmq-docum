package tn.rabbit_mq_node2.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.rabbit_mq_node2.dto.User;

@Service
public class RabbitMqProducer {

    @Value("${rabbitmq.exchange.json.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.json.keyName}")
    private String keyName;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqProducer.class);
    private  final RabbitTemplate rabbitTemplate;

    @Autowired //autowiring  the template
    public RabbitMqProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendJsonMessage( User user){
        LOGGER.info(String.format("Message to publish sent -> %s",user.toString()));
        // template used to send and convert message
        rabbitTemplate.convertAndSend(exchangeName,keyName,user);

    }

}
