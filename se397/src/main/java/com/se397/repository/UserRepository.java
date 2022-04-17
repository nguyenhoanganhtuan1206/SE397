package com.se397.repository;

import com.se397.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = ?1")
    User findByEmail(String email);

    @Query(value="select * from `users` inner join user_role on user_role.user_id = `users`.id inner join `role` on `role`.id = user_role.role_id where `role`.id = ?1" ,nativeQuery = true)
    Page<User> getUserWithRoleIdByPaging(int id , Pageable pageable);

    @Query(value="select * from `users` inner join user_role on user_role.user_id = `users`.id inner join `role` on `role`.id = user_role.role_id where `role`.id = ?1" ,nativeQuery = true)
    List<User> getUserWithRoleId(int id);

    @Query(value = "select * from `users` where `users`.id = ?1" , nativeQuery=true)
    User getUserById(Long id);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id = ?1")
    void deleteUserWithId(Long id);
}
