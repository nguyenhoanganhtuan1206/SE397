package com.se397.controller;

import com.se397.model.*;
import com.se397.repository.EvaluationRepository;
import com.se397.repository.OrderDetailRepository;
import com.se397.repository.ProductRepository;
import com.se397.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;

@Controller
public class EvaluationController {
    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @GetMapping("/{id}/user/evaluation-product")
    public String getEvaluatePage(Model model , @PathVariable int id
            , HttpSession session , @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        Product product = productRepository.findById(id).orElse(null);

        model.addAttribute("product" , product);
        model.addAttribute("evaluation" , new Evaluation());
        model.addAttribute("addToCart", carts);
        model.addAttribute("cart", new Cart());
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("quantitySold", orderDetailRepository.getQuantitySold(product.getId()));
        return "evaluatePage";
    }

    @PostMapping("/{id}/user/evaluation-product")
    public String handleEvaluate(@PathVariable int id , HttpSession session ,
                                 @RequestParam("rating") String ratingValue , @RequestParam("feedbackMsg")String feedbackMsg) {
        Evaluation evaluation = new Evaluation();
        Product product = productRepository.findById(id).orElse(null);
        System.out.println(id);
        User user = (User)session.getAttribute("user");

        evaluation.setLocalDate(LocalDate.now());
        evaluation.setProduct(product);
        evaluation.setUser(user);
        evaluation.setRate(Float.parseFloat(ratingValue));
        evaluation.setContent(feedbackMsg);
        evaluationRepository.save(evaluation);
        return "redirect:/evaluation-product";
    }

    /* After feedback */
    @GetMapping("/evaluation-product")
    public String handleAfterFeedback(Model model , HttpSession session,@SessionAttribute("carts") HashMap<Integer, Cart> carts) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        User customer = (User) session.getAttribute("user");
        model.addAttribute("customer" ,customer);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("order" , session.getAttribute("orderId"));
        model.addAttribute("orderDetails" , orderDetailRepository);
        model.addAttribute("addToCart", carts);
        model.addAttribute("totalPrice" , new OrderDetail());
        return "pageAfterFeedback";
    }
}
