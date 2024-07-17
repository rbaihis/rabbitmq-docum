package tn.rabbit_mq_node2.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.rabbit_mq_node2.dto.User;

@Service
public class RabbitMqConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumer.class);

//    @Value("${rabbitmq.queue.name}")
//    private  String queueName; can't use variable expects constant


    @RabbitListener(queues = { "${rabbitmq.queue.json.name}" })
    public void consume(User user){

        // message arg will have data affected by autowiring thx to rabbitListener
        LOGGER.info(String.format("Received message -> %s",user.toString()));
    }
}
