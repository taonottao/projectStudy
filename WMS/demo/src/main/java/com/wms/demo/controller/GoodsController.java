package com.wms.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.common.QueryPageParam;
import com.wms.demo.common.Result;
import com.wms.demo.entity.Goods;
import com.wms.demo.service.GoodsService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wt
 * @since 2023-09-11
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        return goodsService.save(goods) ? Result.suc() : Result.fail();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        return goodsService.updateById(goods) ? Result.suc() : Result.fail();
    }

    @GetMapping("/del")
    public Result del(Integer id) {
        return goodsService.removeById(id) ? Result.suc() : Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");

        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(name)) {
            wrapper.like(Goods::getName, name);
        }
        if (StringUtils.hasLength(goodstype)) {
            wrapper.eq(Goods::getGoodstype, goodstype);
        }
        if (StringUtils.hasLength(storage)) {
            wrapper.eq(Goods::getStorage, storage);
        }

        Page<Goods> page = new Page<>();
        page.setSize(query.getPageSize());
        page.setCurrent(query.getPageNum());

        IPage result = goodsService.pageCC(page, wrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }
}
