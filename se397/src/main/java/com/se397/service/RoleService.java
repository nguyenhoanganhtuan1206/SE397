package com.se397.service;

import com.se397.model.Role;
import com.se397.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getList() {
        return roleRepository.findAll();
    }
}
