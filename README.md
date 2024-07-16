# <span>RabbitMQ Spring implementation 
****UseCaseExample : monolithic-app--internal-usage </span>****

## dependency rabbitmq pom.xml
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.amqp</groupId>
	<artifactId>spring-rabbit-test</artifactId>
	<scope>test</scope>
</dependency>
```


## packages-structure
```yaml
- resources
  - application.properties (define env vars for your rabbitMQ to use in beans or services)
- java
  - config (where to define your beans exchange, queue, binding, channel)
    - RabbitMQConfig.java (bean defined)
  - publisher (publish service logic)
  - consumer (consumer service logic)
  - dto  (data class for the message to be exchanged )
  - controller 
```

## application.properties (avoid naming dependency)
- **values:** could be define statically in app , use this for better decoupling
```ini
#rabbitmq-env-vars-setup
spring.rabbitmq.host=192.168.43.10
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

#env-custom-vars-for-message-queues-setup
rabbitmq.queue.name=queue_test1
rabbitmq.exchange.name=exchange_test1
rabbitmq.routing.keyname=routing_queue_test1
```

### Config-beans (exchange, queue, binding)
- **config/RabbitMQConfig.java**
```java

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


```

## Publisher (very basic example)
- **publisher/RabbitMQProducer.java**
```java


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


```

## Consumer
- **consumer/RabbitMQConsumer.java**
```java

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

```


## DTO_used in this example
- **dto/noclassInThisBasicExampleDefined**
```md
we didn't use dto in this sample example we only use a basic String message
```


## Controller
- to test rabbit mq by publishing a message and see the app is log if it get consumed and the listener is triggred
- **controller/UseProducerController.java**
```java
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

```



## Testing and output 
- **Get-Request:** http://localhost:9020/api/v1/publish?message=hello%20world!
- **output**:
```log
2024-07-16T17:55:20.690+02:00  INFO 10648 --- [rabbit_mq_node2] [nio-9020-exec-1] t.r.publisher.RabbitMqProducer           : Message to publish sent -> hello world!
2024-07-16T17:55:20.697+02:00  INFO 10648 --- [rabbit_mq_node2] [ntContainer#0-1] t.r.consumer.RabbitMqConsumer            : Received message -> hello world!
```


## Review on ExchangeTypes
- **Direct Exchange:** Routes messages with a specific routing key to the queues that are bound to it with the same key.
- **Fanout Exchange:** Routes messages to all queues bound to it regardless of routing key.
- **Topic Exchange:** Routes messages to queues based on pattern matching between the routing key and the pattern specified in the binding.
- **Headers Exchange:** Routes messages based on header values instead of routing key.

## Review Persistence and Acknowledgment
- **Persistent Messages:** Ensure messages are stored on disk and survive broker restarts. Achieved by setting the `delivery_mode` to 2 when publishing messages.
- **Acknowledgments:** Used to confirm message delivery. Consumers send an acknowledgment to RabbitMQ once they have successfully processed a message.

## Review on Channel
A channel is a lightweight connection within a TCP connection to the RabbitMQ broker, allowing for multiple streams of communication without the overhead of multiple TCP connections.

### Review on When Channels Are Needed
- To separate different logical streams of communication within the same connection.
- To handle multiple consumers or producers efficiently within a single application.
- To achieve concurrency in message processing.

### Review on When Channels Are Critical
- When high throughput and low latency are required.
- To avoid TCP connection overhead and limit resource consumption.
- In scenarios where multiple threads or processes need to interact with RabbitMQ simultaneously, each using its own channel.

## Review on How It Works (Brief Overview)
1. **Producer** publishes messages to an **Exchange**.
2. **Exchange** routes messages to **Queues** based on bindings.
3. **Consumers** retrieve messages from **Queues**.

## Review on Key Features and When to Use
- **Queues:** Use when needing to buffer messages between producers and consumers.
- **Exchanges and Bindings:** Use for routing messages based on routing keys and headers.
- **Acknowledgments:** Ensure reliable delivery and processing of messages.
- **Clustering:** Use for high availability and scalability.
- **Plugins (e.g., Shovel, Federation):** Use for integrating with other messaging systems or enhancing functionality.

This expanded outline provides additional details on exchange types, persistence, acknowledgments, and the role of channels, making the documentation more comprehensive and useful for understanding and utilizing RabbitMQ effectively.

## Review on Persistent Messages
- **Definition:** A message marked as persistent is stored on disk by RabbitMQ, ensuring that it survives broker restarts.
- **Behavior:** When a persistent message is sent to a queue, RabbitMQ writes it to disk. This helps in ensuring that messages are not lost in case of a broker crash.

## Review on Acknowledgments
- **Definition:** Acknowledgments are used to confirm that a message has been received and processed by a consumer.
- **Behavior:** When a consumer receives a message, it processes the message and then sends an acknowledgment back to RabbitMQ. If the consumer dies without sending an acknowledgment, RabbitMQ will requeue the message and deliver it to another consumer.

## Review on Persistence and Acknowledgment Together
- **Interaction:** When a message is persistent and a consumer acknowledges it:
  - The message is stored on disk when it is placed in the queue.
  - Once the consumer processes the message and sends an acknowledgment, RabbitMQ can safely delete the message from the queue and from disk storage, knowing that it has been successfully processed.

### Summary
- **Persistent Messages:** Are not deleted immediately upon queuing; they are stored on disk to survive broker restarts.
- **Acknowledged Messages:** Are deleted from the queue (and disk if persistent) once the consumer processes them and sends an acknowledgment to RabbitMQ. This ensures message durability until successful processing.
