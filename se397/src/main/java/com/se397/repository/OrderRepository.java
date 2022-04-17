package com.se397.repository;

import com.se397.dto.ApprovalOrderDTO;
import com.se397.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select count(o.id) from Order o where o.user.id = ?1")
    int getQuantityOrderByCustomerId(Long id);

    @Query(value="select * from orders where user_id =?1 order by orders.status asc , orders.id desc" , nativeQuery=true)
    List<Order> getOrderByCustomerId(Long id);

    @Query("select o from Order o where o.status = ?1")
    List<Order> getOrderWithStatus(Boolean status);

    @Query(value="select * from orders order by  orders.`status` , orders.start_date desc" ,nativeQuery = true)
    List<Order> getOrdersNew();

    @Query(value="select * from orders order by  orders.`status` , orders.start_date desc" ,nativeQuery = true)
    Page<Order> getOrdersNewByPaging(Pageable pageable);

    @Query(value="select * from orders \n" +
            "inner join `users`\n" +
            "on orders.user_id = `users`.id\n" +
            "where `users`.`name` like concat('%' , ?1 , '%') and  orders.`status` = ?2" ,nativeQuery=true)
    List<Order> getOrdersByUserNameAndStatus(String username , Boolean status);
}
