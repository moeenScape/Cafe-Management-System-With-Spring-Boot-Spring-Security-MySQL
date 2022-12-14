package com.moeen.Newcafe.rest;

import com.moeen.Newcafe.Entity.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface BillRest {
    @PostMapping(path = "/generatebill")
    ResponseEntity<String> generateBill(@RequestBody(required = true)Map<String,Object> requestMap);
    @GetMapping(path = "/getbill")
    ResponseEntity<List<Bill>> getBills();
    @PostMapping(path = "/getPdf")
    ResponseEntity<byte[]>getPDF(@RequestBody Map<String ,Object> requestMap);
    @PostMapping(path = "/deleteByID/{id}")
    ResponseEntity<String> deleteBillById(@PathVariable("id") Integer id);


}
