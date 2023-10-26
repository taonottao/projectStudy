package com.example.mtl.dao;

import com.example.mtl.bean.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/26 22:50
 */
@Mapper
public interface CategoryDAO {

    public List<Category> selectCategories();

}
