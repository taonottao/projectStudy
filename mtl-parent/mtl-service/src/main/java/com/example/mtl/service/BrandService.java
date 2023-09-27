package com.example.mtl.service;

import com.example.mtl.beans.Brand;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 23:19
 */
public interface BrandService {

    List<Brand> selectBrandByCategoryId(Integer categoryId);

}
