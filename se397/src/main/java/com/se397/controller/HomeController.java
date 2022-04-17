package com.se397.controller;

import com.se397.model.Cart;
import com.se397.model.Product;
import com.se397.model.User;
import com.se397.service.CategoryService;
import com.se397.service.ProductService;
import com.se397.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("carts")
public class HomeController {
    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @ModelAttribute("carts")
    public Map<Integer, Cart> setUpHashCart() {
        return new HashMap<>();
    }

    /* Home page*/
    @GetMapping("/")
    public String getHomePage(Model model, HttpSession session, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
        }
        return listByPage(model, carts, 1);
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model , @ModelAttribute("carts") HashMap<Integer, Cart> carts , @PathVariable("pageNumber") int currentPage) {
        Page<Product> page = productService.getProductsWithPaging(currentPage);
        List<Product> products = page.getContent();

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        model.addAttribute("addToCart", carts);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("products", products);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage" , currentPage);
        model.addAttribute("cart", new Cart());
        return "homePage";
    }

    /* Search Page */
    @GetMapping("/search-product")
    public String getSearchPage(Model model, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        model.addAttribute("addToCart", carts);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("cart", new Cart());
        return "searchPage";
    }

    @PostMapping("/search-product")
    public String handleSearch(@RequestParam String keyword, RedirectAttributes redirectAttributes, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        List<Product> products = productService.getProductByName(keyword);
        redirectAttributes.addFlashAttribute("products", products);
        return "redirect:/search-product";
    }

    /* Log out */
    @GetMapping("/log-out")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            Cookie cookie = new Cookie("remember-me", "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:/";
    }

}
