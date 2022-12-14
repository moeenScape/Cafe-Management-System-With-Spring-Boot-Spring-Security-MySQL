package com.moeen.Newcafe.rest;

import com.moeen.Newcafe.Entity.User;
import com.moeen.Newcafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserRest {
    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMap);
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String ,String > requestMap);
    @GetMapping(path = "/allUser")
    public ResponseEntity<List<User>> getAllUser();
    @GetMapping(path = "/allUserByRole")
    public ResponseEntity<List<UserWrapper>> getUserByRole();
    @PostMapping (path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);
    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteUser(@RequestBody(required = true) Map<String,String> requestMap);
    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String,String > requestMap);
    @PostMapping(path = "/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody(required = true)Map<String,String> requestMap);
}
