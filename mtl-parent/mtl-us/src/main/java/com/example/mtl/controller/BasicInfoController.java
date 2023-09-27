package com.example.mtl.controller;

import com.example.mtl.beans.BasicInfo;
import com.example.mtl.beans.Goods;
import com.example.mtl.service.BasicInfoService;
import com.example.mtl.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/23 20:51
 */
@Controller
@RequestMapping("/basicInfo")
public class BasicInfoController {
    @Autowired
    private BasicInfoService basicInfoService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/list")
    public String list(Integer goodsId, Model model) {
        Goods goods = goodsService.getGoodsById(goodsId);
        List<BasicInfo> step1List = basicInfoService.listInfoByGoodsId(goodsId, 1);
        List<BasicInfo> step2List = basicInfoService.listInfoByGoodsId(goodsId, 2);
        List<BasicInfo> step3List = basicInfoService.listInfoByGoodsId(goodsId, 3);

        model.addAttribute("goods", goods);
        model.addAttribute("step1List",step1List);
        model.addAttribute("step2List",step2List);
        model.addAttribute("step3List",step3List);

        return "info";
    }
}
