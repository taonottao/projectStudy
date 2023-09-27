package com.example.mtl.service.impl;

import com.example.mtl.beans.Category;
import com.example.mtl.service.CategoryService;
import com.wt.mtl.dao.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 16:24
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public List<Category> selectCategories() {
        List<Category> list = categoryDAO.selectCategories();
        return list;
    }
}
