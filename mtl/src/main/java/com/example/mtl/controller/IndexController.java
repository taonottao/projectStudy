package com.example.mtl.controller;

import com.example.mtl.bean.Category;
import com.example.mtl.service.CategoryService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/26 23:21
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/index.html")
    public String loadCategory(Model model) {
        List<Category> categoryList = categoryService.listCategory();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }

    @RequestMapping("/")
    public String loadCategory2(Model model) {
        List<Category> categoryList = categoryService.listCategory();
        model.addAttribute("categoryList", categoryList);
        return "index";
    }

}
