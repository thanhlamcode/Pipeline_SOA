package web.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import filters.*;
import pipes.Pipeline;
import queue.MessageProcessor;
import queue.MessageQueue;

@Configuration
public class ApplicationConfig {

    @Bean
    public Pipeline pipeline() {
        Pipeline pipeline = new Pipeline();
        pipeline.addFilter(new InventoryCheckFilter());
        pipeline.addFilter(new DeliveryServiceFilter());
        pipeline.addFilter(new PaymentVerificationFilter());
        pipeline.addFilter(new OrderCreationFilter());
        pipeline.addFilter(new EmailNotificationFilter());
        return pipeline;
    }

    @Bean
    public MessageQueue messageQueue() {
        return new MessageQueue();
    }

    @Bean
    public MessageProcessor messageProcessor(MessageQueue messageQueue, Pipeline pipeline) {
        MessageProcessor processor = new MessageProcessor(messageQueue, pipeline, 4);
        processor.start();
        return processor;
    }
}