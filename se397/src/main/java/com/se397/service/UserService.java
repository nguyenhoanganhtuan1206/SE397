package com.se397.service;

import com.se397.model.Role;
import com.se397.model.User;
import com.se397.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    /* Get customer by id */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /* Get user by email */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /* Get user */
    public List<User> getListUser() {
        return userRepository.findAll();
    }

    public List<User> getListUserWithRoleId(int id) {
        return userRepository.getUserWithRoleId(id);
    }

    /* Paging */
    public Page<User> getListUserWithRoleIdByPaging(int id , int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1 , 10);
        return userRepository.getUserWithRoleIdByPaging(id , pageable);
    }

    public Page<User> getListUserPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), grantedAuthorities
        );
    }
}
