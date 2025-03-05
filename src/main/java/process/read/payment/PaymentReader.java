package process.read.payment;

import core.Message;
import core.OrderRequest;
import core.Payment;
import core.entities.IMessage;

import java.util.HashMap;
import java.util.Map;

public class PaymentReader {
    // Simulated bank verification system
    private static final Map<String, Boolean> cardVerification = new HashMap<>();

    static {
        // Initialize with some test data
        cardVerification.put("7485-2222-3456-2435", true);
        cardVerification.put("1234-5678-9012-3456", true);
        // Add more as needed
    }

    public IMessage read(IMessage message) {
        if (!(message.getPayload() instanceof OrderRequest)) {
            message.setSuccess(false);
            message.setMessage("Invalid payload type. Expected OrderRequest.");
            return message;
        }

        OrderRequest orderRequest = (OrderRequest) message.getPayload();
        Payment payment = orderRequest.getOrder_info().getPayments();

        // Verify the card
        Boolean isVerified = cardVerification.get(payment.getCardNumber());
        if (isVerified == null || !isVerified) {
            message.setSuccess(false);
            message.setMessage("Payment card verification failed.");
            return message;
        }

        return new Message(payment);
    }
}
