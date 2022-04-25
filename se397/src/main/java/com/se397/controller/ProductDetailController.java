package com.se397.controller;

import com.se397.model.Cart;
import com.se397.model.Category;
import com.se397.model.Evaluation;
import com.se397.model.Product;
import com.se397.repository.*;
import com.se397.service.CategoryService;
import com.se397.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

@Controller
@SessionAttributes("carts")
public class ProductDetailController {
    @Autowired
    private ProductService productService = new ProductService();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CategoryService categoryService = new CategoryService();

    @Autowired
    private EvaluationRepository evaluationRepository;

    /* ADMIN */
    @GetMapping("/admin/product-detail/{id}/detail")
    public String getProductDetail(@PathVariable int id, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("orderFalse", orderRepository.getOrderWithStatus(false));
        return "admin/productDetail";
    }

    @PostMapping("/admin/product-detail/{id}")
    public String handleEditProduct(@ModelAttribute("product") Product product, @PathVariable int id,
                                    @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String fileContain = "/assets/img/product/products-photo/" + product.getId() + "/";
            product.setImage(fileContain.concat(fileName));

            Product savedProduct = productRepository.save(product);

            String uploadDir = "./se397/src/main/resources/static/assets/img/product/products-photo/" + savedProduct.getId();
            Path uploadPath = Paths.get(uploadDir);
            System.out.println(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                System.out.println(filePath);
                System.out.println(filePath.toFile().getAbsolutePath());
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.getMessage();
            }
            return "redirect:/admin/management-products";
        } else {
            Product productTemp = productRepository.findById(id).orElse(null);
            productTemp.setImage(productTemp.getImage());
            productRepository.save(productTemp);
            return "redirect:/admin/management-products";
        }
    }

    /* Product Remove */
    @PostMapping("/admin/detail-product/remove/{id}")
    public String handleRemove(@PathVariable int id) {
        productRepository.deleteById(id);
        return "redirect:/admin/management-products";
    }

    /* Product Detail User */
    @GetMapping("/{id}/product-detail")
    public String productDetailPage(@PathVariable int id, @ModelAttribute("carts") HashMap<Integer, Cart> carts
            , Model model) {
        Product product = productRepository.findById(id).orElse(null);

        model.addAttribute("addToCart", carts);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getList());
        model.addAttribute("productsRecommend" , productRepository.getProductByCategory(product.getCategory().getId()));
        model.addAttribute("quantitySold", orderDetailRepository.getQuantitySold(product.getId()));
        model.addAttribute("cart", new Cart());
        Evaluation evaluation = new Evaluation();
        model.addAttribute("evaluations" , evaluationRepository.getEvaluationsByProductId(id));
        model.addAttribute("rating" , evaluation.calculateEvaluation(id , evaluationRepository.findAll()));
        model.addAttribute("lastName" , evaluation);
        return "productDetail";
    }

    /* Add product to cart */
    @PostMapping("/{id}/detail/add-to-cart")
    public String addProductToCart(@ModelAttribute("carts") HashMap<Integer, Cart> carts , @PathVariable int id , Model model) {
        Product product = productService.getProductById(id);

        if (!carts.containsKey(id)) {
            Cart cart = new Cart();
            cart.setId(product.getId());
            cart.setImage(product.getImage());
            cart.setName(product.getName());
            cart.setCategory(product.getCategory().getName());
            cart.setQuantity(1);
            carts.put(id, cart);
            if(product.getPromotion() == null) {
                cart.setPrice(product.getPrice());
            } else {
                cart.setPrice(new Cart().pricePromotion(product.getPrice() , product.getPromotion().getPercent()));
            }
        } else {
            Cart cart = carts.get(id);
            cart.setQuantity(cart.getQuantity() + 1);
            carts.replace(id, carts.get(id), cart);
        }
        return "redirect:/" + id + "/product-detail";
    }
}
