package com.example.mtl.controller;

import com.example.mtl.beans.BasicInfo;
import com.example.mtl.beans.Goods;
import com.example.mtl.beans.Order;
import com.example.mtl.beans.User;
import com.example.mtl.service.BasicInfoService;
import com.example.mtl.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/29 17:22
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private BasicInfoService basicInfoService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    public String createOrder(Goods goods, Integer price, String ids, Model model) {
        // 当前商品选中的评估项即选项详情
        List<BasicInfo> basicInfoList = basicInfoService.listInfoDetailByIds(ids);
        model.addAttribute("goods", goods);
        model.addAttribute("price", price);
        model.addAttribute("ids", ids);
        model.addAttribute("basicInfoList", basicInfoList);
        return "trade";
    }

    @RequestMapping("/save")
    public String saveOrder(Order order, Integer goodId, String ids, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        order.setUserId(user.getUserId());
        String orderId = orderService.addOrder(order, goodId, ids);
        if (orderId != null) {
            request.setAttribute("orderId", orderId);
            request.setAttribute("tips", "订单提交成功！");
        } else {
            request.setAttribute("tips", "订单提交失败！");
        }
        return "order-tips";
    }

}
