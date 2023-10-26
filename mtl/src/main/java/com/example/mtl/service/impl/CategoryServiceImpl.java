package com.example.mtl.service.impl;

import com.example.mtl.bean.Category;
import com.example.mtl.dao.CategoryDAO;
import com.example.mtl.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/26 23:13
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public List<Category> listCategory() {
        return categoryDAO.selectCategories();
    }

}
