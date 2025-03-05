package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.*;
import queue.MessageQueue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderGeneratorService {
    private final MessageQueue messageQueue;
    private final Random random = new Random();
    private final AtomicInteger orderIdCounter = new AtomicInteger(1);
    private final AtomicInteger customerIdCounter = new AtomicInteger(1);
    private final AtomicInteger paymentIdCounter = new AtomicInteger(1);
    private final ScheduledExecutorService scheduler;
    private static final int MAX_PRODUCTS = 50;
    private static final int MAX_DELIVERY_NODES = 100;
    private static final String ORDER_DATA_FILE = "order_data.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public OrderGeneratorService(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    // Start service
    public void start() {
        scheduler.scheduleAtFixedRate(this::generateAndSendOrder, 0, 1, TimeUnit.SECONDS);
        System.out.println("Order generator service started.");
    }

    // Stop service
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Order generator service stopped.");
    }

    // Generate and send order
    public void generateAndSendOrder() {
        try {
            // Generate random time for next order
            long delay = 2000 + random.nextInt(8000); // 2-10 seconds

            // Generate random order
            OrderRequest orderRequest = generateRandomOrder();

            // Save order to JSON file
            saveOrderToJson(orderRequest);

            // Send message to queue
            messageQueue.sendMessage(new Message(orderRequest), "orders");

            System.out.println("Generated order with ID: " +
                    orderRequest.getOrder_info().getOrders().get(0).getOrderId() +
                    ", saved to " + ORDER_DATA_FILE +
                    ", next order in " + (delay / 1000) + " seconds.");

            // Schedule next order with random time
            scheduler.schedule(this::generateAndSendOrder, delay, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.err.println("Error generating order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generate random order
    private OrderRequest generateRandomOrder() {
        int custId = customerIdCounter.getAndIncrement();
        int numItems = 1 + random.nextInt(3); // 1-3 items per order

        // Create list of items
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            Order order = new Order(
                    orderIdCounter.getAndIncrement(),
                    custId,
                    1 + random.nextInt(MAX_PRODUCTS), // productId from 1-50
                    1 + random.nextInt(20), // quantity from 1-20
                    "Random order " + i + " for customer " + custId
            );
            orders.add(order);
        }

        // Create delivery information
        Delivery delivery = new Delivery(
                1 + random.nextInt(MAX_DELIVERY_NODES), // nodeId from 1-100
                "Delivery note for customer " + custId,
                generateRandomAddress(),
                true
        );

        // Create payment information
        Payment payment = new Payment(
                paymentIdCounter.getAndIncrement(),
                custId,
                generateRandomCardNumber(),
                generateRandomCVV()
        );

        // Package into OrderInfo
        OrderInfo orderInfo = new OrderInfo(orders, delivery, payment);

        // Create OrderRequest
        return new OrderRequest(orderInfo);
    }

    // Generate random address
    private String generateRandomAddress() {
        String[] streets = {"Nguyen Van Linh", "Le Loi", "Tran Hung Dao", "Nguyen Hue", "Hai Ba Trung"};
        String[] districts = {"District 1", "District 2", "District 3", "Thu Duc", "Binh Thanh"};
        String[] cities = {"Ho Chi Minh", "Ha Noi", "Da Nang", "Can Tho", "Hai Phong"};

        return random.nextInt(100) + " " +
                streets[random.nextInt(streets.length)] + ", " +
                districts[random.nextInt(districts.length)] + ", " +
                cities[random.nextInt(cities.length)] + ", " +
                "Viet Nam";
    }

    // Generate random credit card number
    private String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) cardNumber.append("-");
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10));
            }
        }
        String card = cardNumber.toString();
        // Đăng ký số thẻ mới vào hệ thống ngân hàng nếu chưa tồn tại
        BankingService.registerCard(card, 10000.0, true);
        return card;
    }

    // Generate random CVV
    private String generateRandomCVV() {
        return String.format("%03d", random.nextInt(1000));
    }

    // Save order as JSON
    private void saveOrderToJson(OrderRequest orderRequest) {
        try (FileWriter writer = new FileWriter(ORDER_DATA_FILE)) {
            gson.toJson(orderRequest, writer);
        } catch (IOException e) {
            System.err.println("Error saving order to JSON: " + e.getMessage());
        }
    }

    // Read latest order from JSON file
    public OrderRequest readOrderFromJson() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(ORDER_DATA_FILE)));
            return gson.fromJson(jsonContent, OrderRequest.class);
        } catch (IOException e) {
            System.err.println("Error reading order from JSON: " + e.getMessage());
            return null;
        }
    }
}
