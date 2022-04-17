package com.se397.model;

import com.se397.repository.OrderDetailRepository;
import com.se397.repository.OrderRepository;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id" , nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    public double totalPrice(List<OrderDetail> orderDetails) {
        double price =0;
        for(OrderDetail orderDetail : orderDetails) {
            price += orderDetail.quantity * orderDetail.product.getPrice();
        }
        return Math.round(price);
    }

    public double totalPrice(int id , List<OrderDetail> orderDetails) {
        double price = 0;
        for(OrderDetail orderDetail : orderDetails) {
            if(orderDetail.getOrder().getId() == id) {
                price += orderDetail.getQuantity() * orderDetail.getProduct().getPrice();
            }
        }
        return Math.round(price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
