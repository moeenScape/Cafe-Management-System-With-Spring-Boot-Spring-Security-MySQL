package com.moeen.Newcafe.serviceImpl;

import com.google.common.base.Strings;
import com.moeen.Newcafe.Entity.User;
import com.moeen.Newcafe.JWT.CustomerUserDetailsService;
import com.moeen.Newcafe.JWT.JWTUtil;
import com.moeen.Newcafe.JWT.JwtFilter;
import com.moeen.Newcafe.JWT.SecurityConfig;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.Utils.EmailUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.dao.UserDao;
import com.moeen.Newcafe.service.UserService;
import com.moeen.Newcafe.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    SecurityConfig securityConfig;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("inside SignUp{}",requestMap);
       try{
           if (validateSignUpMap(requestMap)) {
               User user = userDao.findByEmailId(requestMap.get("email"));
               if (Objects.isNull(user)) {
                   userDao.save(getUserFromMap(requestMap));
                   return CafeUtils.getResponse("Register User", HttpStatus.CREATED);

               } else {
                   return CafeUtils.getResponse("Email Already Exist!", HttpStatus.BAD_REQUEST);
               }

           } else {
               return CafeUtils.getResponse(CafeConstent.INVALID_DATA, HttpStatus.BAD_REQUEST);
           }
       }
       catch (Exception ex)
       {
           ex.printStackTrace();
       }
       return CafeUtils.getResponse("its wrong",HttpStatus.BAD_REQUEST);
    }


    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contractNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContractNumber(requestMap.get("contractNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword((requestMap.get("password")));
        user.setStatus(requestMap.get("status"));
        user.setRole(requestMap.get("role"));
        return user;

    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside Login{}",requestMap);
        try{
            Authentication auth=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );

            if(auth.isAuthenticated())
            {
                if(customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String >("{\"token\":\""+jwtUtil.generateToken(customerUserDetailsService.getUserDetails().getEmail(),
                            customerUserDetailsService.getUserDetails().getRole())+"\"}",HttpStatus.OK);
                }
                else{
                    return  new ResponseEntity<String>("{\"message\":\""+"Wait for admin approve"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }

        }catch (Exception ex)
        {
//            ex.printStackTrace();
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"bad bad bad"+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUserByRole() {
        //return userDao.getAllUserByRole(role);
        try{
            //return new ResponseEntity<List<UserWrapper>>(userDao.getAllUserByRole(),HttpStatus.OK);
            if(jwtFilter.isAdmin())
            {
                return new ResponseEntity<List<UserWrapper>>(userDao.getAllUserByRole(),HttpStatus.OK);
                //return new ResponseEntity<String>("nothing",HttpStatus.OK);

            }else{
                return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }


        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin())
            {
                Optional<User> optional=userDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty())
                {
                    userDao.updateStatus(requestMap.get("Status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("Status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return CafeUtils.getResponse("Updated User",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponse("User ID don't find",HttpStatus.BAD_REQUEST);
                }
            }
            else {return CafeUtils.getResponse(CafeConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin())
            {
                Optional<User> optional=userDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty())
                {
                    userDao.deleteById(Integer.parseInt(requestMap.get("id")));
                    return CafeUtils.getResponse("Updated Delete",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponse("User ID don't find",HttpStatus.BAD_REQUEST);
                }
            }
            else {return CafeUtils.getResponse(CafeConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            User userObj =userDao.findByEmailId(jwtFilter.getCurrentUser());
//            log.info("inside change password{}",userObj);
            if(!userObj.equals(null))
            {
                String oldPass=userObj.getPassword();
                String newPas=requestMap.get("oldPassword");
                String newNEwPass=requestMap.get("newPassword");
                log.info("Inside New Password{} {} {}",oldPass,newPas,newNEwPass);
                if(oldPass.equals(newNEwPass))
                {
                    return CafeUtils.getResponse("Old and new password same",HttpStatus.OK);
                }
                else if(oldPass!=newPas)
                {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CafeUtils.getResponse("Password Change",HttpStatus.OK);

                }
                else
                {
                    return CafeUtils.getResponse("Incorrect old password",HttpStatus.BAD_REQUEST);
                }
            }return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgetPassword(Map<String, String> requestMap) {
        try{
            User userObj =userDao.findByEmailId(requestMap.get("email"));
            if(!Objects.isNull(userObj) && !Strings.isNullOrEmpty(userObj.getEmail()))
            {
                emailUtils.forgetMail(userObj.getEmail(),"Credintian by Moeen", userObj.getPassword());
                return CafeUtils.getResponse("Check Your Mail",HttpStatus.OK);

            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

//    private boolean checkPassWord(String newNEwPass) {
//        int count1=0,count=0;
////        int len=newNEwPass.length();
//        char[] arrays=newNEwPass.toCharArray();
//        for(int i=0;i<newNEwPass.length();i++)
//        {
//
//            if((arrays[i]>='A' || arrays[i]<='Z')||(arrays[i]>='a' || arrays[i]<='z'))
//            {
//                count++;
//            }
//            else if(arrays[i]>='0'||arrays[i]<='9')
//            {
//                count1++;
//            }
//        }
//        if(count>=3 &&count1>=2)
//        {
//            return true;
//        }
//        return false;
//
//    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true"))
        {
            emailUtils.sendSimpleMail(jwtFilter.getCurrentUser(),"Account Approve","User:- "+user+"\n is approved By\nAdmin:- "+jwtFilter.getCurrentUser(),allAdmin);
        }else {
            emailUtils.sendSimpleMail(jwtFilter.getCurrentUser(),"Account Disable","User:- "+user+"\n is approved By\nAdmin:- "+jwtFilter.getCurrentUser(),allAdmin);
        }
    }

}
