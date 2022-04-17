package com.se397.controller;

import com.se397.model.Order;
import com.se397.model.OrderDetail;
import com.se397.model.User;
import com.se397.repository.OrderDetailRepository;
import com.se397.repository.OrderRepository;
import com.se397.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ApprovalController {
    @Autowired
    private OrderService orderService = new OrderService();

    @Autowired
    private OrderDetailService orderDetailService = new OrderDetailService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private UserService userService = new UserService();

    @Autowired
    private OrderRepository orderRepository;

    /* Approval Orders*/
    @GetMapping("/approval-order")
    public String getApprovalPage(Model model) {
        return approvalPaging(model , 1);
    }

    @GetMapping("/approval-order/page/{pageNumber}")
    public String approvalPaging(Model model , @PathVariable("pageNumber") int currentPage) {
        Page<Order> page = orderService.getOrdersByPaging(currentPage);
        List<Order> orders = page.getContent();

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage" , currentPage);
        model.addAttribute("orders" , orders);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("orderDetail" , new OrderDetail());
        model.addAttribute("orderFalse" , orderRepository.getOrderWithStatus(false));
        return "admin/approvalOrderPage";
    }

    /* Detail Approval Orders */
    @GetMapping("/approval-order/{id}/{orderId}/detail")
    public String getApprovalDetail(Model model, @PathVariable Long id ,@PathVariable int orderId) {
        model.addAttribute("customer" , userService.getUserById(id));
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("order" , orderService.getOrderById(orderId));
        model.addAttribute("orderDetails" , orderDetailService.getOrderDetailByOrderId(orderId));
        model.addAttribute("orderFalse" , orderRepository.getOrderWithStatus(false));
        OrderDetail orderDetailTemp = new OrderDetail();
        model.addAttribute("totalPrice" , orderDetailTemp.totalPrice(orderId , orderDetailService.getOrderDetailByOrderId(orderId)));
        return "admin/approvalDetail";
    }

    @PostMapping("/approval-order/{id}/{orderId}/detail")
    public String handleConfirmOrder(@PathVariable Long id, @PathVariable int orderId) {
        for(Order order : orderService.getOrderByCustomerId(id)) {
            if(order.getId() == orderId) {
                order.setStatus(true);
                orderService.saveOrder(order);
            }
        }
        return "redirect:/admin/approval-order";
    }

    /* Handle Search */
    @PostMapping("/approval-order/search")
    public String handleSearch(@RequestParam("nameSearch") String nameSearch , @RequestParam String orderStatus  , Model model) {
        model.addAttribute("orderDetail" , new OrderDetail());
        model.addAttribute("categories" , categoryService.getList());
        model.addAttribute("orderFalse" , orderRepository.getOrderWithStatus(false));

        if(nameSearch.equals("") && orderStatus.equals("0")) {
            model.addAttribute("orders" , orderRepository.getOrdersByUserNameAndStatus("" , false));
        } else if (nameSearch.equals("") && orderStatus.equals("1")) {
            model.addAttribute("orders" , orderRepository.getOrdersByUserNameAndStatus("" , true));
        } else if (!nameSearch.equals("") && orderStatus.equals("0")) {
            model.addAttribute("orders" , orderRepository.getOrdersByUserNameAndStatus(nameSearch , false));
        } else if (!nameSearch.equals("") && orderStatus.equals("1")) {
            model.addAttribute("orders" , orderRepository.getOrdersByUserNameAndStatus(nameSearch , true));
        }

        return "admin/approvalOrderPage";
    }
}
