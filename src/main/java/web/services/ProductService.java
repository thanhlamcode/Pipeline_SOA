package web.services;

import org.springframework.stereotype.Service;
import filters.InventoryCheckFilter;
import web.models.ProductViewModel;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductService {

    private final Map<Integer, ProductViewModel> products = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Initialize product catalog with sample data
        // In a real application, this would come from a database
        for (int i = 1; i <= 50; i++) {
            String productName = "Product " + i;
            double price = 100.0 + (i * 10); // Same as in the original code
            String description = "Description for product " + i;
            int availableQuantity = InventoryCheckFilter.getInventoryLevel(i);

            products.put(i, new ProductViewModel(i, productName, price, description, availableQuantity));
        }
    }

    public List<ProductViewModel> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public ProductViewModel getProductById(int productId) {
        return products.get(productId);
    }

    public void updateInventory(int productId, int quantityChange) {
        ProductViewModel product = products.get(productId);
        if (product != null) {
            product.setAvailableQuantity(product.getAvailableQuantity() + quantityChange);
        }
    }
}

