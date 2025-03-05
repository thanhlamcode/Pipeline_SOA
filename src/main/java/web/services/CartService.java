package web.services;

import org.springframework.stereotype.Service;
import web.models.CartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    // Maps session IDs to shopping carts
    private final Map<String, List<CartItem>> carts = new ConcurrentHashMap<>();

    public List<CartItem> getCart(String sessionId) {
        return carts.computeIfAbsent(sessionId, k -> new ArrayList<>());
    }

    public void addToCart(String sessionId, CartItem item) {
        List<CartItem> cart = getCart(sessionId);

        // Check if the item already exists in the cart
        for (CartItem cartItem : cart) {
            if (cartItem.getProductId() == item.getProductId()) {
                // Update quantity instead of adding a new item
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }

        // If item is not in cart, add it
        cart.add(item);
    }

    public void updateCartItem(String sessionId, int productId, int quantity) {
        List<CartItem> cart = getCart(sessionId);

        for (CartItem cartItem : cart) {
            if (cartItem.getProductId() == productId) {
                if (quantity <= 0) {
                    cart.remove(cartItem);
                } else {
                    cartItem.setQuantity(quantity);
                }
                return;
            }
        }
    }

    public void clearCart(String sessionId) {
        carts.remove(sessionId);
    }

    public double getCartTotal(String sessionId) {
        List<CartItem> cart = getCart(sessionId);
        double total = 0;

        for (CartItem item : cart) {
            total += item.getTotal();
        }

        return total;
    }
}

