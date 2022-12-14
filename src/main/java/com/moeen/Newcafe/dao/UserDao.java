package com.moeen.Newcafe.dao;

import com.moeen.Newcafe.Entity.User;
import com.moeen.Newcafe.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByEmailId(@Param("email") String email);
    List<UserWrapper> getAllUserByRole();
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String Status,@Param("id") Integer id);
    List<String> getAllAdmin();

    //User findByEmail(@Param("email") String email);
}