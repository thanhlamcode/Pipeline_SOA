package queue;

import com.google.gson.Gson;
import core.Message;
import core.OrderRequest;
import core.entities.IMessage;
import pipes.Pipeline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessageProcessor {
    private final MessageQueue messageQueue;
    private final Pipeline pipeline;
    private final ExecutorService executorService;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final int threadCount;
    private final Path jsonFilePath = Paths.get("order_data.json");
    private final Gson gson = new Gson();
    private long lastModified = 0;

    public MessageProcessor(MessageQueue messageQueue, Pipeline pipeline, int threadCount) {
        this.messageQueue = messageQueue;
        this.pipeline = pipeline;
        this.threadCount = threadCount;
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void start() {
        System.out.println("Starting Message Processor with " + threadCount + " threads");

        // Subscribe to queue
        messageQueue.subscribe("orders", this::processMessage);

        // Start JSON file watcher
        startJsonFileWatcher();

        // Start worker threads
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                System.out.println("Worker thread " + threadId + " started");
                while (running.get()) {
                    try {
                        // Worker just sleeps, actual processing is done by queue callback
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("Worker thread " + threadId + " stopped");
            });
        }
    }

    private void startJsonFileWatcher() {
        executorService.submit(() -> {
            System.out.println("JSON file watcher started, monitoring: " + jsonFilePath.toAbsolutePath());
            while (running.get()) {
                try {
                    if (Files.exists(jsonFilePath)) {
                        long currentModified = Files.getLastModifiedTime(jsonFilePath).toMillis();
                        if (currentModified > lastModified) {
                            lastModified = currentModified;
                            processJsonFile();
                        }
                    }
                    Thread.sleep(500); // Check every 500ms
                } catch (IOException | InterruptedException e) {
                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    System.err.println("Error watching JSON file: " + e.getMessage());
                }
            }
            System.out.println("JSON file watcher stopped");
        });
    }

    private void processJsonFile() {
        try {
            String jsonContent = new String(Files.readAllBytes(jsonFilePath));
            OrderRequest orderRequest = gson.fromJson(jsonContent, OrderRequest.class);
            if (orderRequest != null) {
                System.out.println("Processing order from JSON file with ID: "
                        + orderRequest.getOrder_info().getOrders().get(0).getOrderId());
                IMessage message = new Message(orderRequest);
                processMessage(message);
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    private void processMessage(IMessage message) {
        try {
            System.out.println("Processing message: " + message.getPayload().getClass().getSimpleName());
            IMessage result = pipeline.process(message);
            if (result.isSuccess()) {
                System.out.println("Message processed successfully");
            } else {
                System.err.println("Message processing failed: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        running.set(false);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Message Processor stopped");
    }
}
