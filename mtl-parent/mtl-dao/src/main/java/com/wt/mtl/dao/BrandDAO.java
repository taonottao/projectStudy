package com.wt.mtl.dao;

import com.example.mtl.beans.Brand;

import java.util.List;

/**
 * 品牌信息的数据库操作
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 22:52
 */

public interface BrandDAO {

    List<Brand> selectBrandByCategoryId(Integer id);

}
