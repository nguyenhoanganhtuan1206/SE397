package com.se397.controller;

import com.se397.model.Cart;
import com.se397.service.CategoryService;
import com.se397.service.ProductService;
import com.se397.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("carts")
public class LoginController {

    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @ModelAttribute("carts")
    public Map<Integer, Cart> setUpCart() {
        return new HashMap<>();
    }

    /* Get Login Page */
    @GetMapping("/login")
    public String getLoginPage(Model model, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        model.addAttribute("addToCart", carts);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("products", productService.getList());
        model.addAttribute("cart", new Cart());
        return "authentication/loginPage";
    }
}
