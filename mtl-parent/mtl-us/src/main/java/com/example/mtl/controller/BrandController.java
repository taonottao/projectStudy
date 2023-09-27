package com.example.mtl.controller;

import com.example.mtl.beans.Brand;
import com.example.mtl.service.BrandService;
import com.example.mtl.util.CodeParam;
import com.example.mtl.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 23:23
 */
@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @ResponseBody
    @RequestMapping("/list")
    public ResultVO list(@RequestParam(defaultValue = "0") Integer categoryId) {
        List<Brand> brandList = brandService.selectBrandByCategoryId(categoryId);

        return ResultVO.success(CodeParam.SUCCESS_CODE, "success", brandList);
    }

}
