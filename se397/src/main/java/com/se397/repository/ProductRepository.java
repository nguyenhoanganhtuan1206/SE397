package com.se397.repository;

import com.se397.model.Order;
import com.se397.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product , Integer> {
    @Query(value = "select * from product where product.`name` like concat('%', ?1 ,'%')\n" +
            "order by `status` desc" , nativeQuery=true)
    List<Product> getProductByName(String name);

    @Query(value = "select `users`.`name` , orders.start_date ,  sum(orderdetail.quantity * product.price) , `users`.address , `users`.phoneNumber , orders.`status` , province.`name` from product  inner join orderdetail on orderdetail.product_id = product.id inner join orders\n" +
            "on orderdetail.order_id = orders.id\n" +
            "inner join `users`\n" +
            "on `users`.id = orders.user_id\n" +
            "inner join province\n" +
            "on province.id = `users`.province_id\n" +
            "group by user_id" ,nativeQuery = true)
    List<Product> getApprovalOrder();

    @Query(value = "select * from product where product.`name` like concat('%', ?1 ,'%')\n" +
            "order by `status` desc" , nativeQuery=true)
    Page<Product> getProductByNameAndPaging(String name , Pageable pageable);

    /* Recommend Product */
    @Query("select p from Product p where p.category.id = ?1")
    List<Product> getProductByCategory(int id);

    /* Get products by status */
    @Query("select  p from Product p where p.status = ?1")
    List<Product> getProductByStatus(boolean status);

    /* Get products by status and name */
    @Query(value ="select * from product where product.`name` like concat('%', ?1 ,'%') and `status` = ?2" ,nativeQuery = true)
    List<Product> getProductByStatusAndName(String name , boolean status);
}
