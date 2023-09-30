package com.example.mtl.controller;

import com.example.mtl.beans.Category;
import com.example.mtl.beans.User;
import com.example.mtl.service.CategoryService;
import com.example.mtl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/28 22:45
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/login")
    public String login(String username, String password, HttpServletRequest request) {
        User user = userService.check(username, password);
        String pagePath = "login";
        if (user == null) {
            request.setAttribute("tips", "账号或密码错误！");
        } else {
            pagePath = "index";
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // 因为跳转到 index.html 需要加载一级分类，所以在跳转之前需要传递一级分类集合
            List<Category> categoryList = categoryService.selectCategories();
            request.setAttribute("categoryList", categoryList);
        }
        return pagePath;
    }
}
