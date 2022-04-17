package com.se397.service;

import com.se397.model.Order;
import com.se397.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public List<Order> getList() {
        return orderRepository.findAll();
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public int getCountOrderByCustomerId(Long id) {
        return orderRepository.getQuantityOrderByCustomerId(id);
    }

    public List<Order> getOrderByCustomerId(Long id) {
        return orderRepository.getOrderByCustomerId(id);
    }

    /* Paging */
    public Page<Order> getOrdersByPaging(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1 , 10);
        return orderRepository.getOrdersNewByPaging(pageable);
    }
}
