package com.se397.controller;
import com.se397.repository.OrderRepository;
import com.se397.repository.UserRepository;
import com.se397.service.ProvinceService;
import com.se397.service.UserService;
import com.se397.ultil.EncrypPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class UserDetailController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProvinceService provinceService = new ProvinceService();

    @Autowired
    private UserService userService = new UserService();
    /* Detail Information User*/
    @GetMapping("/detail-user/{id}")
    public String getDetailUserPage(@PathVariable Long id , Model model) {
        model.addAttribute("customer" , userRepository.findById(id).orElse(null));
        model.addAttribute("password" , EncrypPasswordUtils.EncrypPasswordUtils(userService.getUserById(id).getPassword()));
        model.addAttribute("orderFalse" , orderRepository.getOrderWithStatus(false));
        model.addAttribute("provinces" , provinceService.getList());
        return "admin/detailAccountUser";
    }

    @PostMapping("/detail-user/{id}/remove")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteUserWithId(id);
        return "redirect:/admin/management-account";
    }

}
