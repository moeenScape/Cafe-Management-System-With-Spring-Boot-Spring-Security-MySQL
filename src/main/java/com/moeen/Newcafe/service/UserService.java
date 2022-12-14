package com.moeen.Newcafe.service;

import com.moeen.Newcafe.Entity.User;
import com.moeen.Newcafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);
    List<User> getAllUser();
    ResponseEntity<List<UserWrapper>> getAllUserByRole();

    ResponseEntity<String> update(Map<String, String> requestMap);

    ResponseEntity<String> deleteUser(Map<String, String> requestMap);

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> forgetPassword(Map<String, String> requestMap);
}
