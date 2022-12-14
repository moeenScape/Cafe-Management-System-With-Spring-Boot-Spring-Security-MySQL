package com.moeen.Newcafe.serviceImpl;

import com.moeen.Newcafe.Entity.Category;
import com.moeen.Newcafe.Entity.Product;
import com.moeen.Newcafe.JWT.JwtFilter;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.dao.ProductDao;
import com.moeen.Newcafe.dao.UserDao;
import com.moeen.Newcafe.service.ProductService;
import com.moeen.Newcafe.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDao productDao;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    private UserDao userDao;

    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin())
            {
                if(validateProductMap(requestMap,false))
                {
                    productDao.save(getProductFromMap(requestMap,false));
                    return CafeUtils.getResponse("ProductAdded",HttpStatus.CREATED);
                }
                return CafeUtils.getResponse(CafeConstent.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponse(CafeConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
            return new ResponseEntity<>(productDao.getAllProduct(),HttpStatus.OK);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin())
            {
                if(validateProductMap(requestMap,true))
                {
                    Optional<Product> optional=productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty())
                    {
                        Product product=getProductFromMap(requestMap,true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return CafeUtils.getResponse("Product Updated",HttpStatus.OK);
                    }else {
                        return CafeUtils.getResponse("Product Id doesn't exist",HttpStatus.BAD_REQUEST);
                    }
                }
                return CafeUtils.getResponse(CafeConstent.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if(jwtFilter.isAdmin())
            {
                Optional<Product> optional=productDao.findById(id);
                if(!optional.isEmpty())
                {
                    productDao.deleteById(id);
                    return CafeUtils.getResponse("Product Delete Successfully",HttpStatus.OK);
                }else{
                    return CafeUtils.getResponse("ID does not exist",HttpStatus.BAD_REQUEST);
                }

            }
            return CafeUtils.getResponse(CafeConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return CafeUtils.getResponse(CafeConstent.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        if(jwtFilter.isAdmin())
        {
            Optional<Product> optional=productDao.findById(Integer.parseInt(requestMap.get("id")));
            if(!optional.isEmpty())
            {
                productDao.updateStatus(Integer.parseInt(requestMap.get("id")),requestMap.get("status"));
                return CafeUtils.getResponse("Update Status Successfully",HttpStatus.OK);
            }else {
                return CafeUtils.getResponse("ID Does not exist",HttpStatus.OK);
            }
        }
        return CafeUtils.getResponse(CafeConstent.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try{
            Optional<Product> optional=productDao.findById(id);
            if(optional.isEmpty())
            {
                return new ResponseEntity<List<ProductWrapper>>(productDao.getBCategory(id),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductById(Integer id) {
        try{
            Optional<Product> optional=productDao.findById(id);
            if(!optional.isEmpty())
            {
                return new ResponseEntity<List<ProductWrapper>>(productDao.getProductById(id),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category=new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product=new Product();
        if(isAdd)
        {
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else {
            product.setStatus("true");
        }
        product.setName(requestMap.get("name"));
        product.setCategory(category);
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name"))
        {
            if(requestMap.containsKey("id"))
            {
                return true;
            } else if (!validateId) {return true;}
        }
        return false;
    }
}
