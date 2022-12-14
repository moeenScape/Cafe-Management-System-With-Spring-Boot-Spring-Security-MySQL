package com.moeen.Newcafe.serviceImpl;

import com.google.common.base.Strings;
import com.moeen.Newcafe.Entity.Category;
import com.moeen.Newcafe.JWT.JwtFilter;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.dao.CategoryDao;
import com.moeen.Newcafe.dao.UserDao;
import com.moeen.Newcafe.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private UserDao userDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin())
            {
                if(validateCategory(requestMap,false))
                {
                    categoryDao.save(getCategoryFromMap(requestMap,false));
                    return CafeUtils.getResponse("Add Category Successfully",HttpStatus.OK);
                }

            }else{
                return CafeUtils.getResponse("UnAuthorized Entry", HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true"))
            {
                return new ResponseEntity<List<Category>>(categoryDao.allCategory(),HttpStatus.OK);
            }
            return new ResponseEntity<List<Category>>(categoryDao.findAll(),HttpStatus.OK);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin())
            {
                if( validateCategory(requestMap,true))
                {
                    Optional<Category> optional=categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty())
                    {
                        categoryDao.save(getCategoryFromMap(requestMap,true));
                        return CafeUtils.getResponse("Category Update Successfully",HttpStatus.OK);
                    }else {
                        return CafeUtils.getResponse("Can not Find Category ID",HttpStatus.BAD_REQUEST);
                    }
                }
                return CafeUtils.getResponse(CafeConstent.INVALID_DATA,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategory(Map<String, String> requestMap, boolean b) {
        if(requestMap.containsKey("name"))
        {
            if(requestMap.containsKey("id") && b)
            {
                return true;
            } else if (!b) {
                return true;
            }
        }
        return false;
    }
    private Category getCategoryFromMap(Map<String,String> requestMap,Boolean isAdd)
    {Category category=new Category();
        if(isAdd)
        {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

}
