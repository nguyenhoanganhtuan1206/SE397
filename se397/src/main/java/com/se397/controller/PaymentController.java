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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@SessionAttributes("carts")
public class PaymentController {
    @ModelAttribute("carts")
    public HashMap<Integer , Cart> setUpCart() {
        return new HashMap<>();
    }

    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private ProvinceService provinceService = new ProvinceService();

    @Autowired
    private UserService userService = new UserService();

    @Autowired
    private OrderService orderService = new OrderService();

    @Autowired
    private OrderDetailService orderDetailService = new OrderDetailService();

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/payment")
    public String getPaymentPage(@ModelAttribute("carts") HashMap<Integer, Cart> carts
                                 , Model model , HttpSession session) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {
            User customer = (User) session.getAttribute("user");
            model.addAttribute("customer" ,customer);
            model.addAttribute("addToCart", carts);
            model.addAttribute("categories", categoryService.getList());
            model.addAttribute("products", productService.getList());
            model.addAttribute("cart", new Cart());
            model.addAttribute("provinces" , provinceService.getList());
            return "paymentPage";
        }
    }

    /* Delete product from cart */
    @GetMapping("/{id}/remove-product-cart")
    public String deleteProduct(@PathVariable int id ,@SessionAttribute("carts") HashMap<Integer, Cart> carts) {
        new Cart().deleteProduct(carts, id);
        return "redirect:/payment";
    }

    /* Increase Product */
    @GetMapping("/{id}/increase-product")
    public String increaseProduct(@PathVariable int id, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        new Cart().increaseProduct(id , carts);
        return "redirect:/payment";
    }

    /* Decrease Product */
    @GetMapping("/{id}/decrease-product")
    public String decreaseProduct(@PathVariable int id, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        new Cart().decreaseProduct(id , carts);
        return "redirect:/payment";
    }

    /* Payment */
    @PostMapping("/{id}/payment")
    public String paymentProduct(HttpSession session ,@PathVariable Long id, @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Order order = new Order();
        OrderDetail orderDetail = null;

        Set<OrderDetail> orderDetails = new HashSet<>();
        Integer[] keys = new Integer[carts.size()];
        int index = 0;

        for(Map.Entry<Integer , Cart> entry : carts.entrySet()) {
            orderDetail = new OrderDetail();
            orderDetail.setProduct(productService.getProductById(entry.getValue().getId()));
            orderDetail.setQuantity(entry.getValue().getQuantity());
            orderDetails.add(orderDetail);

            for(int i = 0 ; i < orderDetails.size() ; i++) {
                order.setOrderDetails(orderDetails);
                order.setUser(userService.getUserById(id));
                order.setLocalDate(LocalDate.now());
                order.setStatus(false);
                orderService.saveOrder(order);
            }

            orderDetail.setOrder(order);
            orderDetailService.saveOrderDetail(orderDetail);

            keys[index] = entry.getKey();
            index++;
        }

        for (int i = 0 ; i < keys.length ; i++) {
            carts.remove(keys[i]);
        }

        session.setAttribute("orderId" , order.getId());
        session.removeAttribute("carts");
        return "redirect:/confirm-order/user";
    }

    /* After payment */
    @GetMapping("/confirm-order/user")
    public String confirmOrderPage(HttpSession session , Model model ,@SessionAttribute("carts") HashMap<Integer, Cart> carts) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {
            User customer = (User) session.getAttribute("user");
            model.addAttribute("customer" ,customer);
            model.addAttribute("categories", categoryService.getList());
            model.addAttribute("products", productService.getList());
            model.addAttribute("provinces" , provinceService.getList());
            model.addAttribute("quantityOrder", orderService.getCountOrderByCustomerId(customer.getId()));
            model.addAttribute("order" , session.getAttribute("orderId"));
            model.addAttribute("orderDetails" , orderDetailRepository);
            model.addAttribute("addToCart", carts);
            model.addAttribute("totalPrice" , new OrderDetail());
            return "userOrderConfirm";
        }
    }
}
