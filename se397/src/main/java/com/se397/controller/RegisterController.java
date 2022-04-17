package com.se397.controller;

import com.se397.dto.UserDTO;
import com.se397.model.Cart;
import com.se397.model.Role;
import com.se397.model.User;
import com.se397.repository.RoleRepository;
import com.se397.service.*;
import com.se397.ultil.EncrypPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
@SessionAttributes("carts")
public class RegisterController {
    @ModelAttribute("carts")
    public HashMap<Integer , Cart> setUpCart() {
        return new HashMap<>();
    }

    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private UserService userService = new UserService();

    @Autowired
    private ProvinceService provinceService = new ProvinceService();

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/user-register")
    public String getRegisterPage(@ModelAttribute("carts") HashMap<Integer, Cart> carts, Model model) {
        model.addAttribute("addToCart", carts);
        model.addAttribute("provinces" , provinceService.getList());
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("products", productService.getList());
        model.addAttribute("cart", new Cart());
        model.addAttribute("customer" , new UserDTO());
        return "authentication/registerPage";
    }

    @PostMapping("/register-account")
    public String handleRegister(@Valid @ModelAttribute("customer") UserDTO customer , BindingResult bindingResult , Model model
    , @ModelAttribute("carts") HashMap<Integer, Cart> carts) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("addToCart", carts);
            model.addAttribute("provinces" , provinceService.getList());
            model.addAttribute("categories", categoryService.getList());
            model.addAttribute("products", productService.getList());
            model.addAttribute("cart", new Cart());
            return "authentication/registerPage";
        } else {
            if (userService.getUserByEmail(customer.getEmail()) != null) {
                model.addAttribute("addToCart", carts);
                model.addAttribute("categories", categoryService.getList());
                model.addAttribute("products", productService.getList());
                model.addAttribute("provinces" , provinceService.getList());
                model.addAttribute("cart", new Cart());
                model.addAttribute("msg" , "User is existing!");
                return "authentication/registerPage";
            } else {
                if (!new UserDTO().checkConfirmPassword(customer.getPassword(), customer.getConfirmPassword())) {
                    model.addAttribute("addToCart", carts);
                    model.addAttribute("categories", categoryService.getList());
                    model.addAttribute("products", productService.getList());
                    model.addAttribute("provinces" , provinceService.getList());
                    model.addAttribute("cart", new Cart());
                    model.addAttribute("msg" , "Password is not matches!");
                    return "authentication/registerPage";
                }
                User user = new User();
                user.setName(customer.getFullName());
                user.setPassword(EncrypPasswordUtils.EncrypPasswordUtils(customer.getPassword()));
                user.setEmail(customer.getEmail());
                user.setCreateAt(LocalDate.now());
                user.setPhoneNumber(customer.getPhoneNumber());
                user.setProvince(provinceService.getProvinceById(customer.getProvince()));
                /* Authorization */
                Set<Role>roles = new HashSet<>();
                Role role = roleRepository.findByName("ROLE_MEMBER");
                roles.add(role);
                user.setRoles(roles);

                userService.save(user);
                return "redirect:/login";
            }
        }
    }
}
