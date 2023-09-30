package com.example.mtl.controller;

import com.example.mtl.beans.Goods;
import com.example.mtl.service.GoodsService;
import com.example.mtl.service.PriceCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 计算价格
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/26 22:38
 */
@Controller
@RequestMapping("/price")
public class PriceCountController {

    @Autowired
    private PriceCountService priceCountService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/count")
    public String count(Integer goodsId, String property, String descId, Model model) {

        Goods goods = goodsService.getGoodsById(goodsId);
        String ids = property + "," + descId;

        int price = priceCountService.countPrice(goodsId, ids);
        // 商品信息
        model.addAttribute("goods", goods);
        model.addAttribute("price", price);
        model.addAttribute("ids", ids);
        return "price";

    }
}
