package web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import core.*;
import queue.MessageQueue;
import web.models.CartItem;
import web.models.OrderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private final MessageQueue messageQueue;
    private final CartService cartService;
    private final ProductService productService;

    private final AtomicInteger orderIdCounter = new AtomicInteger(1000);
    private final AtomicInteger paymentIdCounter = new AtomicInteger(1000);

    @Autowired
    public OrderService(MessageQueue messageQueue, CartService cartService, ProductService productService) {
        this.messageQueue = messageQueue;
        this.cartService = cartService;
        this.productService = productService;
    }

    public boolean submitOrder(String sessionId, OrderViewModel orderViewModel) {
        try {
            List<CartItem> cartItems = cartService.getCart(sessionId);

            if (cartItems.isEmpty()) {
                return false;
            }

            // Create order entities
            List<Order> orders = new ArrayList<>();
            for (CartItem item : cartItems) {
                Order order = new Order(
                        orderIdCounter.getAndIncrement(),
                        orderViewModel.getCustomerId(),
                        item.getProductId(),
                        item.getQuantity(),
                        "Web order for product: " + item.getProductName()
                );
                orders.add(order);
            }

            // Create delivery information
            Delivery delivery = new Delivery(
                    1, // Default node ID
                    "Web order delivery",
                    orderViewModel.getAddress(),
                    true
            );

            // Create payment information
            Payment payment = new Payment(
                    paymentIdCounter.getAndIncrement(),
                    orderViewModel.getCustomerId(),
                    orderViewModel.getCardNumber(),
                    orderViewModel.getCvv()
            );

            // Combine into order request
            OrderInfo orderInfo = new OrderInfo(orders, delivery, payment);
            OrderRequest orderRequest = new OrderRequest(orderInfo);

            // Submit to message queue
            messageQueue.sendMessage(new Message(orderRequest), "orders");

            // Clear the cart
            cartService.clearCart(sessionId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

