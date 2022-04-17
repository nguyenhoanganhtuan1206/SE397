package com.se397.controller;

import com.se397.model.*;
import com.se397.repository.*;
import com.se397.service.*;
import com.se397.ultil.EncrypPasswordUtils;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService = new UserService();

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("orderFalse" , orderRepository.getOrderWithStatus(false));
        model.addAttribute("totalOrders" , orderRepository.findAll());
        model.addAttribute("ordersStatus" , orderRepository.getOrderWithStatus(false));
        model.addAttribute("userQuantity" , userService.getListUserWithRoleId(2));
        model.addAttribute("adminQuantity" , userService.getListUserWithRoleId(1));
        model.addAttribute("products" , productService.getList());
        model.addAttribute("orderDetailRepo" , orderDetailRepository);
        model.addAttribute("evaluationRepo" , evaluationRepository);
        model.addAttribute("totalPriceMonth" , new OrderDetail().totalPrice(orderDetailRepository.getOrderDetailByCurrentMonth()));
        model.addAttribute("totalPriceCurYear" , new OrderDetail().totalPrice(orderDetailRepository.getOrderDetailByCurrentYear()));

        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication().getName());
        return "admin/adminPage";
    }

    @GetMapping("/management-account")
    public String getManagementAccountPage(Model model , HttpSession session) {
        return managementAccountByPaging(1 , session , model);
    }

    @GetMapping("/management-account/page/{pageNumber}")
    public String managementAccountByPaging(@PathVariable("pageNumber") int currentPage , HttpSession session , Model model) {
        Page<User> page = userService.getListUserWithRoleIdByPaging(2, currentPage);
        List<User> users = page.getContent();

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        User admin = (User) session.getAttribute("admin");
        model.addAttribute("admin" , admin);
        model.addAttribute("users" , users);
        model.addAttribute("orderFalse", orderRepository.getOrderWithStatus(false));
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage" , currentPage);
        model.addAttribute("userRep" , userRepository);
        model.addAttribute("categories" , categoryRepository.findAll());
        return "admin/accountManagementPage";
    }

    /* Management Products */
    @GetMapping("/management-products")
    public String managementProduct(Model model) {
        return managementProductByPaging(1 , model);
    }

    @GetMapping("/management-products/page/{pageNumber}")
    public String managementProductByPaging(@PathVariable("pageNumber") int currentPage , Model model) {
        Page<Product> page = productService.getProductsWithPaging(currentPage);
        List<Product> products = page.getContent();

        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        model.addAttribute("products", products);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage" , currentPage);
        model.addAttribute("orderFalse" , orderRepository.getOrderWithStatus(false));
        model.addAttribute("categories" , categoryRepository.findAll());
        return "admin/managementProducts";
    }

    @PostMapping("/management-products/search")
    public String handleSearchProducts(Model model, @RequestParam String nameSearch
            , @RequestParam("orderStatus") String orderStatus) {
        System.out.println(nameSearch);
        if (nameSearch.equals("") && orderStatus.equals("Hết hàng")) {
            model.addAttribute("products", productRepository.getProductByStatus(false));
        } else if (nameSearch.equals("") && orderStatus.equals("Còn hàng")) {
            model.addAttribute("products", productRepository.getProductByStatus(true));
        } else if(!nameSearch.equals("") && orderStatus.equals("Hết hàng")) {
            model.addAttribute("products", productRepository.getProductByStatusAndName(nameSearch , true));
        } else if (!nameSearch.equals("") && orderStatus.equals("Còn hàng")) {
            model.addAttribute("products", productRepository.getProductByStatusAndName(nameSearch , true));
        }
        model.addAttribute("orderFalse", orderRepository.getOrderWithStatus(false));
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/managementProducts";
    }
}
