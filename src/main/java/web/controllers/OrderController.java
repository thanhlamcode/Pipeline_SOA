package web.controllers;

import core.OrderRequest;
import core.entities.IMessage;
import core.Message;
import filters.OrderCreationFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderCreationFilter orderCreationFilter;

    public OrderController() {
        this.orderCreationFilter = new OrderCreationFilter();
    }

    @PostMapping("/create")
    public IMessage createOrder(@RequestBody OrderRequest orderRequest) {
        IMessage message = new Message(orderRequest);
        return orderCreationFilter.execute(message);
    }
}
