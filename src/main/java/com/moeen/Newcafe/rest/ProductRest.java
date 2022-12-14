package com.moeen.Newcafe.rest;


import com.moeen.Newcafe.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductRest {
    @PostMapping(path = "/addProduct")
    ResponseEntity<String> addProduct(@RequestBody(required = true) Map<String ,String>requestMap);
    @GetMapping(path = "/getAllProduct")
    ResponseEntity<List<ProductWrapper>> getALlProduct();
    @PostMapping(path = "/updateProduct")
    ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String,String> requestMap);
    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable("id")Integer id);
    @PostMapping(path="updateStatus")
    ResponseEntity<String> updateProductStatus(@RequestBody(required = true)Map<String,String> requestMap);
    @GetMapping(path="/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable("id") Integer id);
    @GetMapping(path="/getProductById/{id}")
    ResponseEntity<List<ProductWrapper>> getProductById(@PathVariable("id") Integer id);

}
