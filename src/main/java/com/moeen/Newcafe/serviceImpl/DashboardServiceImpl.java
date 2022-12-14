package com.moeen.Newcafe.serviceImpl;

import com.moeen.Newcafe.dao.BillDao;
import com.moeen.Newcafe.dao.CategoryDao;
import com.moeen.Newcafe.dao.ProductDao;
import com.moeen.Newcafe.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BillDao billDao;
    @Autowired
    ProductDao productDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map=new HashMap<>();
        map.put("category",categoryDao.count());
        map.put("product",productDao.count());
        map.put("bill",billDao.count());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
