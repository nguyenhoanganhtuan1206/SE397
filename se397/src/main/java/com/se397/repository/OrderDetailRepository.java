package com.se397.repository;

import com.se397.model.Order;
import com.se397.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("select od from OrderDetail od where od.order.id = ?1")
    List<OrderDetail> getOrderDetailByOrderId(int id);

    @Query(value="select * from orderdetail \n" +
            "inner join orders \n" +
            "on orders.id = orderdetail.order_id \n" +
            "inner join `users`\n" +
            "on `users`.id = orders.user_id\n" +
            "where `users`.id=?1 and orders.id = ?2" , nativeQuery=true)
    List<OrderDetail> getOrderDetailByUserId(Long id , int orderId);

    @Query(value= "select * from orderdetail \n" +
            "inner join orders\n" +
            "on orders.id = orderdetail.order_id\n" +
            "where month(orders.start_date) = month(CURDATE())" , nativeQuery=true)
    List<OrderDetail> getOrderDetailByCurrentMonth();

    @Query(value ="select * from orderdetail \n" +
            "inner join orders\n" +
            "on orders.id = orderdetail.order_id\n" +
            "where year(orders.start_date) = year(CURDATE())" , nativeQuery = true)
    List<OrderDetail> getOrderDetailByCurrentYear();

    /* Get quantity product sold */
    @Query(value = "select count(product_id) from orderdetail where product_id = ?1" , nativeQuery = true)
    int getQuantitySold(int id);
}
