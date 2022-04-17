package com.se397.service;

import com.se397.model.Category;
import com.se397.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getList() {
        return categoryRepository.findAll();
    }
}
