package filters;

import core.Order;
import core.OrderRequest;
import core.Payment;
import core.entities.IFilter;
import core.entities.IMessage;
import services.BankingService;

public class PaymentVerificationFilter implements IFilter {
    @Override
    public boolean isMatch(IMessage message) {
        return message.getPayload() instanceof OrderRequest;
    }

    @Override
    public IMessage execute(IMessage message) {
        OrderRequest orderRequest = (OrderRequest) message.getPayload();
        Payment payment = orderRequest.getOrder_info().getPayments();

        // Simple validation of card number format
        if (payment.getCardNumber() == null ||
                !payment.getCardNumber().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            message.setSuccess(false);
            message.setMessage("Invalid card number format. Expected: ####-####-####-####");
            return message;
        }

        // Simple validation of CVV
        if (payment.getCvv() == null || !payment.getCvv().matches("\\d{3}")) {
            message.setSuccess(false);
            message.setMessage("Invalid CVV format. Expected: 3 digits");
            return message;
        }

        // Calculate the total amount for the order
        double totalAmount = 0;
        for (Order order : orderRequest.getOrder_info().getOrders()) {
            // Get price from a simulated product database
            double price = 50.0 + (order.getProductId() * 5); // Simple price calculation
            totalAmount += price * order.getQuantity();
        }

        // Add delivery fee if available
        if (orderRequest.getOrder_info().getDelivery().getDeliveryFee() > 0) {
            totalAmount += orderRequest.getOrder_info().getDelivery().getDeliveryFee();
        }

        // Verify payment with the banking service
        BankingService.PaymentVerificationResult result =
                BankingService.verifyPayment(payment, totalAmount);

        if (!result.isVerified()) {
            message.setSuccess(false);
            message.setMessage("Payment verification failed: " + result.getMessage());
            return message;
        }

        // Process the payment
        BankingService.PaymentProcessResult processResult =
                BankingService.processPayment(payment, totalAmount);

        if (!processResult.isSuccess()) {
            message.setSuccess(false);
            message.setMessage("Payment processing failed: " + processResult.getMessage());
            return message;
        }

        // Store the transaction ID
        payment.setTransactionId(processResult.getTransactionId());

        System.out.println("Payment verified and processed. Amount: $" + totalAmount +
                ", Transaction ID: " + processResult.getTransactionId());

        return message;
    }
}
