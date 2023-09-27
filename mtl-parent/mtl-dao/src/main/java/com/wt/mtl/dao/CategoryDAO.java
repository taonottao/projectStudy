package com.wt.mtl.dao;

import com.example.mtl.beans.Category;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 16:06
 */
public interface CategoryDAO {
    List<Category> selectCategories();
}
