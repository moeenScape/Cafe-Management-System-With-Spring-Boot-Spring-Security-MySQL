package com.moeen.Newcafe.service;

import com.moeen.Newcafe.Entity.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateBill(Map<String, Object> requestMap);
    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPDF(Map<String, Object> requestMap);

    ResponseEntity<String> deleteBillById(Integer id);
}
