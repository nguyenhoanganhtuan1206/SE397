package com.se397.controller;

import com.se397.model.Cart;
import com.se397.model.Product;
import com.se397.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("carts")
public class CartController {
    @Autowired
    ProductService productService = new ProductService();

    @ModelAttribute("carts")
    public Map<Integer, Cart> setUpCart() {
        return new HashMap<>();
    }

    /* Delete product from cart */
    @GetMapping("/{id}/remove-product")
    public String deleteProductFromCart(@PathVariable int id ,@SessionAttribute("carts") HashMap<Integer, Cart> carts) {
        new Cart().deleteProduct(carts, id);
        return "redirect:/";
    }

    /* Add product to cart */
    @PostMapping("/{id}/add-to-cart")
    public String handleAddToCart(@PathVariable int id, @SessionAttribute("carts") HashMap<Integer, Cart> carts
    , RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(id);

        if (!carts.containsKey(id)) {
            Cart cart = new Cart();
            cart.setId(product.getId());
            cart.setImage(product.getImage());
            cart.setName(product.getName());
            cart.setCategory(product.getCategory().getName());
            cart.setQuantity(1);
            carts.put(id, cart);
            if(product.getPromotion() == null) {
                cart.setPrice(product.getPrice());
            } else {
                cart.setPrice(new Cart().pricePromotion(product.getPrice() , product.getPromotion().getPercent()));
            }
        } else {
            Cart cart = carts.get(id);
            cart.setQuantity(cart.getQuantity() + 1);
            carts.replace(id, carts.get(id), cart);
        }

        redirectAttributes.addFlashAttribute("addToCart" , carts);
        return "redirect:/";
    }
}
