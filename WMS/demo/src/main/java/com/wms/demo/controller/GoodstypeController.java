package com.wms.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.common.QueryPageParam;
import com.wms.demo.common.Result;
import com.wms.demo.entity.Goods;
import com.wms.demo.entity.Goodstype;
import com.wms.demo.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wt
 * @since 2023-09-11
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {

    @Autowired
    private GoodstypeService goodstypeService;

    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype) {
        return goodstypeService.save(goodstype) ? Result.suc() : Result.fail();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype) {
        return goodstypeService.updateById(goodstype) ? Result.suc() : Result.fail();
    }

    @GetMapping("/del")
    public Result del(Integer id) {
        return goodstypeService.removeById(id) ? Result.suc() : Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");


        Page<Goodstype> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goodstype> wrapper = new LambdaQueryWrapper();
        if (StringUtils.hasLength(name)) {
            wrapper.like(Goodstype::getName, name);
        }

        IPage result = goodstypeService.pageCC(page, wrapper);
        return Result.suc(result.getRecords(), result.getTotal());

    }

    @GetMapping("/list")
    public Result list() {
        List list = goodstypeService.list();
        return Result.suc(list);
    }
}
