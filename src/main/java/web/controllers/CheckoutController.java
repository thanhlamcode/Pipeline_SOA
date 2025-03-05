package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import web.models.OrderViewModel;
import web.services.CartService;
import web.services.OrderService;

import javax.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        if (cartService.getCart(session.getId()).isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cartService.getCart(session.getId()));
        model.addAttribute("cartTotal", cartService.getCartTotal(session.getId()));
        model.addAttribute("orderViewModel", new OrderViewModel());

        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @ModelAttribute OrderViewModel orderViewModel,
            HttpSession session,
            Model model) {

        // Default customer ID if not provided
        if (orderViewModel.getCustomerId() <= 0) {
            orderViewModel.setCustomerId(1);
        }

        // Set the cart items to the order view model
        orderViewModel.setItems(cartService.getCart(session.getId()));

        boolean success = orderService.submitOrder(session.getId(), orderViewModel);

        if (success) {
            return "order-confirmation";
        } else {
            model.addAttribute("error", "There was an error processing your order. Please try again.");
            return "checkout";
        }
    }
}
