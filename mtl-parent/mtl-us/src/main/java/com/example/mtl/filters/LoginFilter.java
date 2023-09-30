package com.example.mtl.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/29 21:40
 */
@Component
@WebFilter("/*")
public class LoginFilter implements Filter {

    // 放不受限的资源
    private String[] excludePath = {"/", "/index.html", "/brand/list", "/goods/listByBrand", "/basicInfo/list",
            "/price/count", "/login.html", "/user/login"};
    private String[] excludeExts = {".jpg",".css",".js",".png"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取请求路径
        String uri = request.getRequestURI();
        // 判断是否放行
        boolean b = judge(uri);
        if (b) {
            filterChain.doFilter(servletRequest, servletResponse);// 放行
        } else {
            HttpSession session = request.getSession();
            Object user = session.getAttribute("user");
            if (user == null) {
                request.setAttribute("tips", "请先登录!");
                request.getRequestDispatcher("/login.html").forward(request, response);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);// 放行
            }
        }

    }

    private boolean judge(String path) {
        boolean ret = false;
        for (String s : excludePath) {
            if (s.equals(path)) {
                ret = true;
                break;
            }
        }
        for (String s : excludeExts) {
            if (path.endsWith(s)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
