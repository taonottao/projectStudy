package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.common.AppVariable;
import com.example.demo.common.PasswordUtil;
import com.example.demo.entity.Userinfo;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/14 23:56
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/reg")
    public AjaxResult reg(Userinfo userinfo){
        // 1. 非空校验
        if(userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
            || !StringUtils.hasLength(userinfo.getPassword())){
            return AjaxResult.fail(-1, "注册失败，请重试！");
        }

        // 密码加盐
        userinfo.setPassword(PasswordUtil.encrypt(userinfo.getPassword()));
        return AjaxResult.success(userService.reg(userinfo));
    }

    @RequestMapping("/login")
    public AjaxResult getUserByName(HttpServletRequest request, String username, String password) {
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)){
            return AjaxResult.fail(-1, "登录失败，请重试！");
        }

        Userinfo userinfo = userService.getUserByName(username);
        if (userinfo == null || userinfo.getId() <= 0) {
            return AjaxResult.fail(-1, "查询异常!!");
        }

        // 比对密码
        if(!PasswordUtil.checkPassword(password, userinfo.getPassword())){
            return AjaxResult.fail(-1, "账户或密码错误！");
        }

        // 账户密码正确, 登录成功
        // 将用户存储到 session
        HttpSession session = request.getSession();
        session.setAttribute(AppVariable.USER_SESSION_KEY, userinfo);
        userinfo.setPassword("");// 返回前端之前隐藏敏感信息(密码)

        return AjaxResult.success(userinfo);

    }
}
