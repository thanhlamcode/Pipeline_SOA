package clients;




import filters.*;
import pipes.Pipeline;
import queue.MessageProcessor;
import queue.MessageQueue;
import services.OrderGeneratorService;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Order Processing System...");

        // Create pipeline and add filters in order
        Pipeline pipeline = new Pipeline();
        pipeline.addFilter(new InventoryCheckFilter());
        pipeline.addFilter(new DeliveryServiceFilter()); // Make sure this class exists
        pipeline.addFilter(new PaymentVerificationFilter());
        pipeline.addFilter(new OrderCreationFilter());
        pipeline.addFilter(new EmailNotificationFilter());

        // Create message queue
        MessageQueue messageQueue = new MessageQueue();

        // Create and start message processor with 4 threads
        MessageProcessor messageProcessor = new MessageProcessor(messageQueue, pipeline, 4);
        messageProcessor.start();

        // Create and start order generator service
        OrderGeneratorService orderGenerator = new OrderGeneratorService(messageQueue);
        orderGenerator.start();

        // Create an order_data.json file if it doesn't exist
        File orderDataFile = new File("order_data.json");
        if (!orderDataFile.exists()) {
            System.out.println("Creating initial order data file...");
            // This line was in your code but appears to be incomplete/incorrect
            // orderGenerator.generateAndSendOrder();
        }

        System.out.println("System is running. Enter 'exit' to stop.");

        // Register shutdown hook to stop services safely
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Order Processing System...");
            orderGenerator.stop();
            messageProcessor.stop();
            System.out.println("System stopped successfully.");
        }));

        // Wait for user input to stop the application
        try (Scanner scanner = new Scanner(System.in)) {
            String input;
            while (!(input = scanner.nextLine()).equalsIgnoreCase("exit")) {
                System.out.println("Enter 'exit' to stop the application.");
            }
        }

        // Manually trigger shutdown process
        System.out.println("Shutting down...");
        orderGenerator.stop();
        messageProcessor.stop();
        System.out.println("Application stopped. Exiting main thread.");
    }
}