package com.se397.service;

import com.se397.model.Province;
import com.se397.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    public List<Province> getList() {
        return provinceRepository.findAll();
    }

    public Province getProvinceById(int id) {
        return provinceRepository.findById(id).orElse(null);
    }
}
