package web.controllers;

import core.OrderRequest;
import core.entities.IMessage;
import core.Message;
import filters.InventoryCheckFilter;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // Chấp nhận request từ tất cả các nguồn
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryCheckFilter inventoryCheckFilter;

    public InventoryController() {
        this.inventoryCheckFilter = new InventoryCheckFilter();
    }

    @PostMapping("/check")
    public IMessage checkInventory(@RequestBody OrderRequest orderRequest) {
        IMessage message = new Message(orderRequest);
        return inventoryCheckFilter.execute(message);
    }

    @GetMapping("/status/{productId}")
    public String getInventoryStatus(@PathVariable int productId) {
        int stock = InventoryCheckFilter.getInventoryLevel(productId);
        return "{\"productId\": " + productId + ", \"stock\": " + stock + "}";
    }
}
