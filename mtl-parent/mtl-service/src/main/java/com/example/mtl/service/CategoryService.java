package com.example.mtl.service;

import com.example.mtl.beans.Category;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 16:15
 */

public interface CategoryService {
    public List<Category> selectCategories();
}
