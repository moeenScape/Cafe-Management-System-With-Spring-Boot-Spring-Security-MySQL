package com.moeen.Newcafe.restImpl;

import com.moeen.Newcafe.Entity.Category;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.dao.CategoryDao;
import com.moeen.Newcafe.rest.CategoryRest;
import com.moeen.Newcafe.service.CategoryService;
import com.moeen.Newcafe.serviceImpl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
public class CategoryRestImpl implements CategoryRest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            return categoryService.addNewCategory(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            return categoryService.getAllCategory(filterValue);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
       return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
