package process.read.invoice;

import core.*;
import core.entities.IMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceReader {
    // Simulated invoice generator
    private static final AtomicInteger invoiceIdGenerator = new AtomicInteger(1);
    private static final Map<Integer, Double> productPrices = new HashMap<>();

    static {
        // Initialize with sample product prices
        for (int i = 1; i <= 50; i++) {
            productPrices.put(i, 100.0 + (i * 10)); // Simple price generation
        }
    }

    public IMessage read(IMessage message) {
        if (!(message.getPayload() instanceof OrderRequest)) {
            message.setSuccess(false);
            message.setMessage("Invalid payload type. Expected OrderRequest.");
            return message;
        }

        OrderRequest orderRequest = (OrderRequest) message.getPayload();
        OrderInfo orderInfo = orderRequest.getOrder_info();

        // Calculate total amount
        double totalAmount = 0;
        for (Order order : orderInfo.getOrders()) {
            Double price = productPrices.get(order.getProductId());
            if (price == null) {
                message.setSuccess(false);
                message.setMessage("Product ID " + order.getProductId() + " not found.");
                return message;
            }
            totalAmount += price * order.getQuantity();
        }

        // Create invoice
        Invoice invoice = new Invoice(
                invoiceIdGenerator.getAndIncrement(),
                orderInfo.getOrders().get(0).getCustId(),
                totalAmount,
                false,
                orderInfo.getOrders()
        );

        // Create invoice info with all necessary details
        InvoiceInfo invoiceInfo = new InvoiceInfo(invoice, orderInfo.getDelivery(), orderInfo.getPayments());

        return new Message(invoiceInfo);
    }
}
