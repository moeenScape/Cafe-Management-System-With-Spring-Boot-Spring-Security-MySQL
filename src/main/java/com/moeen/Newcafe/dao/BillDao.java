package com.moeen.Newcafe.dao;

import com.moeen.Newcafe.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BillDao extends JpaRepository<Bill,Integer> {

    List<Bill> getAllBills();

    List<Bill> getBillsByName(@Param("currentUser") String currentUser);
}
