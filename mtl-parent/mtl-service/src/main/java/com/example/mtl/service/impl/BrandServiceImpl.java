package com.example.mtl.service.impl;

import com.example.mtl.beans.Brand;
import com.example.mtl.service.BrandService;
import com.wt.mtl.dao.BrandDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 23:19
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDAO brandDAO;

    @Override
    public List<Brand> selectBrandByCategoryId(Integer categoryId) {
        return brandDAO.selectBrandByCategoryId(categoryId);
    }
}
