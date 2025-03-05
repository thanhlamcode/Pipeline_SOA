package filters;

import core.*;
import core.entities.IFilter;
import core.entities.IMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderCreationFilter implements IFilter {
    private static final AtomicInteger invoiceIdGenerator = new AtomicInteger(1);

    @Override
    public boolean isMatch(IMessage message) {
        return message.getPayload() instanceof OrderRequest;
    }

    @Override
    public IMessage execute(IMessage message) {
        OrderRequest orderRequest = (OrderRequest) message.getPayload();
        OrderInfo orderInfo = orderRequest.getOrder_info();

        // Calculate total amount
        double totalAmount = 0;
        for (Order order : orderInfo.getOrders()) {
            // In a real system, we would look up the price from a product database
            double price = 100.0 + (order.getProductId() * 10); // Simple price calculation
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

        // Replace the payload with the invoice info
        message.setPayload(invoiceInfo);

        return message;
    }
}
