package com.se397.service;

import com.se397.model.Product;
import com.se397.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    /* Get list */
    public List<Product> getList() {
        return productRepository.findAll();
    }

    /* Get product by id */
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    /* Get product by name */
    public List<Product> getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    public Page<Product> getProductWithPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsWithPaging(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,8);
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByNameAndPaging(String name ,int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,8);
        return productRepository.getProductByNameAndPaging(name , pageable);
    }
}
