package com.moeen.Newcafe.restImpl;

import com.moeen.Newcafe.Entity.User;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.rest.UserRest;
import com.moeen.Newcafe.service.UserService;
import com.moeen.Newcafe.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{

        return userService.signUp(requestMap);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<List<User>>(userService.getAllUser(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getUserByRole() {
        try{
            return userService.getAllUserByRole();

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
        //return new ResponseEntity<List<UserWrapper>>(userService.getAllUserByRole(role),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            return userService.update(requestMap);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(Map<String, String> requestMap) {
        try{
            return userService.deleteUser(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            return userService.changePassword(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
        try{
            return userService.forgetPassword(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
