package com.se397.controller;

import com.se397.model.Cart;
import com.se397.model.Order;
import com.se397.model.OrderDetail;
import com.se397.model.User;
import com.se397.repository.OrderDetailRepository;
import com.se397.repository.OrderRepository;
import com.se397.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("carts")
public class HistoryController {
    @ModelAttribute("carts")
    public Map<Integer, Cart> setUpCart() {
        return new HashMap<>();
    }

    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private OrderService orderService = new OrderService();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    /* Get history page */
    @GetMapping("/{id}/history")
    public String getHistoryPage(HttpSession session , Model model, @ModelAttribute("carts") HashMap<Integer , Cart> carts
    , @PathVariable Long id) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User customer = (User) session.getAttribute("user");
        model.addAttribute("customer", customer);
        model.addAttribute("addToCart", carts);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("products", productService.getList());
        model.addAttribute("quantityOrder", orderService.getCountOrderByCustomerId(id));
        model.addAttribute("orders" , orderService.getOrderByCustomerId(id));
        model.addAttribute("orderDetails" , orderDetailRepository);
        model.addAttribute("totalPrice" , new OrderDetail());
        model.addAttribute("cart", new Cart());
        return "historyPage";
    }

    /* Cancel order */
    @PostMapping("/{orderId}/history/cancel-order")
    public String handleCancelOrder(@PathVariable int orderId, HttpSession session) {
        System.out.println(orderId);
        User customer = (User) session.getAttribute("user");
//        orderRepository.deleteById(id);
        return "redirect:/" + customer.getId() + "/history";
    }
}
