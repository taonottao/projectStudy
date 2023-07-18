package com.example.demo.config;

import com.example.demo.common.AppVariable;
import com.example.demo.entity.Userinfo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * 登录拦截器
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/17 18:59
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            // 登录成功
            System.out.println("当前登录用户为：" +
                    ((Userinfo) session.getAttribute(AppVariable.USER_SESSION_KEY)).getUsername());
            return true;
        }
        response.sendRedirect("/login.html");
        return false;
    }
}
