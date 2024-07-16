package tn.rabbit_mq_node2.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConsumer.class);

//    @Value("${rabbitmq.queue.name}")
//    private  String queueName; can't use variable expects constant


    @RabbitListener(queues = { "${rabbitmq.queue.name}" })
    public void consume(String message){

        // message arg will have data affected by autowiring thx to rabbitListener
        LOGGER.info(String.format("Received message -> %s",message));
    }
}
