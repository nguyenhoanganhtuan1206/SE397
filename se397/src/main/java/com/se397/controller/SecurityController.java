package com.se397.controller;

import com.se397.model.Cart;
import com.se397.service.CategoryService;
import com.se397.service.ProductService;
import com.se397.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("carts")
public class SecurityController {
    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private UserService userService = new UserService();

    @ModelAttribute("carts")
    public Map<Integer, Cart> setUpHashCart() {
        return new HashMap<>();
    }

    @GetMapping("/user")
    public String getUserPage(Model model , HttpSession session ,
                              @ModelAttribute("carts") HashMap<Integer, Cart> carts , Principal principal) {
        model.addAttribute("addToCart", carts);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("products", productService.getList());
        model.addAttribute("cart", new Cart());
        session.setAttribute("user", userService.getUserByEmail(principal.getName()));
        return "redirect:/";
    }

    /* 403 */
    @GetMapping("/403")
    public String pageError(Model model , HttpSession session ,
                            @ModelAttribute("carts") HashMap<Integer, Cart> carts , Principal principal) {
        model.addAttribute("addToCart", carts);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("products", productService.getList());
        model.addAttribute("cart", new Cart());
        session.setAttribute("user" , userService.getUserByEmail(principal.getName()));
        return "authentication/403";
    }
}
