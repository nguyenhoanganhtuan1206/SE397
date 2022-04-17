package com.se397.controller;

import com.se397.dto.UserDTO;
import com.se397.model.Cart;
import com.se397.model.Province;
import com.se397.model.Role;
import com.se397.model.User;
import com.se397.repository.RoleRepository;
import com.se397.service.*;
import com.se397.ultil.EncrypPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
@SessionAttributes("carts")
public class PersonalController {
    @ModelAttribute("carts")
    public HashMap<Integer , Cart> setUpCart() {
        return new HashMap<>();
    }

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private CategoryService categoryService = new CategoryService();


    @Autowired
    private ProvinceService provinceService = new ProvinceService();

    @Autowired
    private UserService userService = new UserService();


    @GetMapping("/{id}/personal-info")
    public String getPersonalPage(@PathVariable Long id , Model model ,
                                  HttpSession session , @ModelAttribute("carts")HashMap<Integer , Cart> carts) {
        if(session.getAttribute("user") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("customer" , userService.getUserById(id));
            model.addAttribute("addToCart", carts);
            model.addAttribute("categories", categoryService.getList());
            model.addAttribute("provinces" , provinceService.getList());
            model.addAttribute("cart", new Cart());
            return "personalPage";
        }
    }

    @PostMapping("/{id}/personal-info")
    public String handlePersonalInfo(@PathVariable Long id , @ModelAttribute("carts")HashMap<Integer , Cart> carts ,
                                     @Valid User user , BindingResult bindingResult , Model model
            , @RequestParam String confirmPassword , @RequestParam String password) {
        if(bindingResult.hasFieldErrors()) {
            model.addAttribute("customer" , userService.getUserById(id));
            model.addAttribute("addToCart", carts);
            model.addAttribute("categories", categoryService.getList());
            model.addAttribute("provinces" , provinceService.getList());
            model.addAttribute("cart", new Cart());
            return "personalPage";
        } else {
            if(!new UserDTO().checkConfirmPassword(password, confirmPassword)) {
                model.addAttribute("customer" , userService.getUserById(id));
                model.addAttribute("addToCart", carts);
                model.addAttribute("categories", categoryService.getList());
                model.addAttribute("provinces" , provinceService.getList());
                model.addAttribute("cart", new Cart());
                userService.save(user);
                model.addAttribute("msgError" , "Password is not matches!");
                return "personalPage";
            }
            /* Change info user */
            User userTemp = new User();
            userTemp.setId(user.getId());
            userTemp.setPassword(EncrypPasswordUtils.EncrypPasswordUtils(password));
            userTemp.setAddress(user.getAddress());
            userTemp.setEmail(user.getEmail());
            userTemp.setName(user.getName());
            userTemp.setProvince(user.getProvince());
            userTemp.setPhoneNumber(user.getPhoneNumber());
            userTemp.setUpdateAt(LocalDate.now());
            userTemp.setCreateAt(user.getCreateAt());
            /* Authorization */
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName("ROLE_MEMBER");
            roles.add(role);
            userTemp.setRoles(roles);

            model.addAttribute("customer" , userService.getUserById(id));
            model.addAttribute("addToCart", carts);
            model.addAttribute("categories", categoryService.getList());
            model.addAttribute("provinces" , provinceService.getList());
            model.addAttribute("cart", new Cart());
            userService.save(userTemp);
            model.addAttribute("msg" , "Change information successfully!");
            return "personalPage";
        }
    }
}
