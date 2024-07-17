package tn.rabbit_mq_node2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.rabbit_mq_node2.dto.User;
import tn.rabbit_mq_node2.publisher.RabbitMqProducer;

@RestController
@RequestMapping("/api/v1")
public class UseProducerController {

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    // http://localhost:9020/api/v1/publish + json_user_raw_data
    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody User user){

        rabbitMqProducer.sendJsonMessage(user);

        String responseMessage= "producer api called successfully message now sent to rabbitMq and will be managed by MessageQueueBroker ...";
        return ResponseEntity.status(200).body(responseMessage);
    }

}
