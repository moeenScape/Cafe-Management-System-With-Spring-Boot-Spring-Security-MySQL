package com.moeen.Newcafe.dao;

import com.moeen.Newcafe.Entity.Product;
import com.moeen.Newcafe.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer> {
    List<ProductWrapper> getAllProduct();
    @Modifying
    @Transactional
    void updateStatus(@Param("id") Integer id, @Param("status") String status);

    List<ProductWrapper> getBCategory(@Param("id") Integer id);

    List<ProductWrapper> getProductById(@Param("id") Integer id);
}
