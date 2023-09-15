package com.example.ecommerce_platform.controller;


import com.example.ecommerce_platform.entity.Member;
import com.example.ecommerce_platform.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wt
 * @since 2023-09-14
 */
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 会员登录
     * @param member
     * @return
     */
    @RequestMapping("/login")
    public String login(Member member) {
        System.out.println("账号:" + member.getUname());
        Member loginMember = memberService.login(member);
        if (loginMember != null) {
            // 转发首页
            return "index.jsp";
        }
        return "login.jsp";
    }
}
