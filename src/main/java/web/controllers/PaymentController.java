package web.controllers;

import core.OrderRequest;
import core.entities.IMessage;
import core.Message;
import filters.PaymentVerificationFilter;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // Chấp nhận request từ tất cả các nguồn
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentVerificationFilter paymentVerificationFilter;

    public PaymentController() {
        this.paymentVerificationFilter = new PaymentVerificationFilter();
    }

    @PostMapping("/verify")
    public IMessage verifyPayment(@RequestBody OrderRequest orderRequest) {
        IMessage message = new Message(orderRequest);
        return paymentVerificationFilter.execute(message);
    }
}
