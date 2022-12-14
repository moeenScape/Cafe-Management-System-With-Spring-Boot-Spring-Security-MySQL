package com.moeen.Newcafe.restImpl;

import com.moeen.Newcafe.Entity.Bill;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.dao.BillDao;
import com.moeen.Newcafe.rest.BillRest;
import com.moeen.Newcafe.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {
    @Autowired
    BillService billService;
    @Autowired
    private BillDao billDao;

    @Override
    public ResponseEntity<String> generateBill(Map<String, Object> requestMap) {
        try{
            return billService.generateBill(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            return billService.getBills();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPDF(Map<String, Object> requestMap) {
        try{
            return billService.getPDF(requestMap);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBillById(Integer id) {
        try{
            return billService.deleteBillById(id);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
