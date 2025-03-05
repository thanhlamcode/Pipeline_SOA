package web.controllers;

import core.OrderRequest;
import core.entities.IMessage;
import core.Message;
import filters.DeliveryServiceFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryServiceFilter deliveryServiceFilter;

    public DeliveryController() {
        this.deliveryServiceFilter = new DeliveryServiceFilter();
    }

    @PostMapping("/check")
    public IMessage checkDelivery(@RequestBody OrderRequest orderRequest) {
        IMessage message = new Message(orderRequest);
        return deliveryServiceFilter.execute(message);
    }
}
