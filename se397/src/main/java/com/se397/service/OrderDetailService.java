package com.se397.service;

import com.se397.model.OrderDetail;
import com.se397.repository.OrderDetailRepository;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    public List<OrderDetail> getOrderDetailByOrderId(int id) {
        return orderDetailRepository.getOrderDetailByOrderId(id);
    }

    public List<OrderDetail> getList() {
        return orderDetailRepository.findAll();
    }
}
