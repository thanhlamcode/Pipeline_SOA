package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.models.CartItem;
import web.models.ProductViewModel;
import web.services.CartService;
import web.services.ProductService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<ProductViewModel> products = productService.getAllProducts();
        model.addAttribute("products", products);

        List<CartItem> cart = cartService.getCart(session.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.getCartTotal(session.getId()));

        return "home";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") int productId, Model model) {
        ProductViewModel product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "product-detail";
    }

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        ProductViewModel product = productService.getProductById(productId);

        if (product != null && quantity > 0 && quantity <= product.getAvailableQuantity()) {
            CartItem item = new CartItem(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    quantity
            );

            cartService.addToCart(session.getId(), item);
        }

        return "redirect:/";
    }

    @PostMapping("/cart/update")
    public String updateCart(
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        cartService.updateCartItem(session.getId(), productId, quantity);

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cart = cartService.getCart(session.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("cartTotal", cartService.getCartTotal(session.getId()));

        return "cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session.getId());
        return "redirect:/cart";
    }
}