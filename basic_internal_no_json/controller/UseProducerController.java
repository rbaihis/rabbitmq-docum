package tn.rabbit_mq_node2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.rabbit_mq_node2.publisher.RabbitMqProducer;

@RestController
@RequestMapping("/api/v1")
public class UseProducerController {

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    // http://localhost:9020/api/v1/publish?message=hello%20world!
    @GetMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestParam("message") String message){

        rabbitMqProducer.sendMessage(message);

        String responseMessage= "producer api called successfully message now sent to rabbitMq and will be managed by MessageQueueBroker ...";
        return ResponseEntity.status(200).body(responseMessage);
    }
}
