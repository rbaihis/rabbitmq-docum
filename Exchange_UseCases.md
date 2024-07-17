# RabbitMQ Exchange Examples

## Direct Exchange

### Customer Support System

- **Problem**: A company receives support requests for different issues (e.g., technical problems, billing questions, general inquiries) and needs to route these requests to the appropriate department.
- **Solution**: Use a direct exchange to route support requests based on the issue type as the routing key (e.g., `technical`, `billing`, `general`).
- **Why Direct Exchange?**
  - Each request is routed to the correct department using a specific routing key.
  - Example:
    - A message with the routing key `technical` is sent to the technical support queue.
    - A message with the routing key `billing` is sent to the billing support queue.
    - A message with the routing key `general` is sent to the general support queue.
  - Benefit: Avoids complexity, making it straightforward for new developers to understand and implement.

## Fanout Exchange

### System Update Notifications

- **Problem**: An IT department needs to notify all employees about a system update.
- **Solution**: Use a fanout exchange to broadcast the update notification to all employee queues.
- **Why Fanout Exchange?**
  - Broadcasts the same message to all queues bound to the exchange.
  - Example:
    - An update notification message is sent to the fanout exchange.
    - All queues bound to the fanout exchange receive the update notification.
  - Benefit: Simplifies broadcasting messages to multiple recipients without specifying routing keys.

## Topic Exchange

### News Article Categorization

- **Problem**: A news platform needs to route articles to different sections (e.g., sports, politics, technology) based on their category.
- **Solution**: Use a topic exchange with routing keys like `news.sports`, `news.politics`, `news.technology`, and bind queues with patterns like `news.*`.
- **Why Topic Exchange?**
  - Flexible routing based on patterns allows handling of different categories with a single exchange.
  - Example:
    - A message with the routing key `news.sports` is routed to the sports news queue.
    - A message with the routing key `news.politics` is routed to the politics news queue.
    - A message with the routing key `news.technology` is routed to the technology news queue.
  - Benefit: Suitable for content filtering and targeted message distribution based on categories.

## Headers Exchange

### Order Processing with Multiple Attributes

- **Problem**: An online store needs to route orders based on multiple attributes like customer priority (e.g., VIP, regular) and order type (e.g., electronics, clothing).
- **Solution**: Use a headers exchange to route messages based on headers like `priority` and `order_type`.
- **Why Headers Exchange?**
  - Allows routing based on multiple attributes without complex routing key structures.
  - Example:
    - A message with headers `priority=VIP` and `order_type=electronics` is routed to the VIP electronics queue.
    - A message with headers `priority=regular` and `order_type=clothing` is routed to the regular clothing queue.
  - Benefit: Simplifies routing for messages with multiple attributes, ideal for managing complex routing scenarios.


---

- *Created and maintained by [Seif-Allah Rbaihi]( https://github.com/rbaihis )*  *[Linkdin]( https://www.linkedin.com/in/seif-allah-rbaihi-2b6091126 )*
