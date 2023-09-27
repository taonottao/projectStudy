package com.example.mtl.controller;

import com.example.mtl.beans.Category;
import com.example.mtl.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 16:47
 */
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;


    @RequestMapping("/index.html")
    public String loadCategory(Model model) {
        List<Category> categoryList = categoryService.selectCategories();
        System.out.println(categoryList);
        System.out.println(model.toString());
        model.addAttribute("categoryList",categoryList);
        return "index";
    }
    @RequestMapping("/")
    public String loadCategory2(Model model) {
        List<Category> categoryList = categoryService.selectCategories();
        model.addAttribute("categoryList",categoryList);
        return "index";
    }
}
