package com.example.mtl.controller;

import com.example.mtl.beans.Goods;
import com.example.mtl.service.GoodsService;
import com.example.mtl.util.CodeParam;
import com.example.mtl.util.PageInfo;
import com.example.mtl.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/20 19:30
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ResponseBody
    @RequestMapping("/listByBrand")
    public ResultVO list(Integer brandId, @RequestParam(defaultValue = "1") Integer pageNum) {
        PageInfo<Goods> goodsPageInfo = goodsService.listGoodsByBrandId(brandId, pageNum);
        return ResultVO.success(CodeParam.SUCCESS_CODE, "success", goodsPageInfo);
    }

}
