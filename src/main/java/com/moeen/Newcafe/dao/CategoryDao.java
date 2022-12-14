package com.moeen.Newcafe.dao;

import com.moeen.Newcafe.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryDao extends JpaRepository<Category,Integer> {
    //@Query(name = "select c from Category c")
    List<Category> allCategory();
}
