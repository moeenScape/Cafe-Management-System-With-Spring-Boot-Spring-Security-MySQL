package com.moeen.Newcafe.JWT;

import com.moeen.Newcafe.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;
    public com.moeen.Newcafe.Entity.User userDetails;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside LoadUserByUsername{}",username);
        userDetails=userDao.findByEmailId(username);
        if(!Objects.isNull(userDetails))
        {
            return new User(userDetails.getEmail(),userDetails.getPassword(),new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("User not found");
        }

    }
    public com.moeen.Newcafe.Entity.User getUserDetails()
    {
        return userDetails;
    }
}
