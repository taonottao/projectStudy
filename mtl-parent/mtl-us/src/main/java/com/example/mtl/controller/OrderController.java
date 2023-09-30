package com.example.mtl.controller;

import com.example.mtl.beans.BasicInfo;
import com.example.mtl.beans.Goods;
import com.example.mtl.service.BasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/create")
    public String createOrder(Goods goods, Integer price, String ids, Model model) {
        // 当前商品选中的评估项即选项详情
        List<BasicInfo> basicInfoList = basicInfoService.listInfoDetailByIds(ids);
        model.addAttribute("goods", goods);
        model.addAttribute("price", price);
        model.addAttribute("basicInfoList", basicInfoList);
        return "trade";
    }

}
