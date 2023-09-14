package com.wms.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.common.QueryPageParam;
import com.wms.demo.common.Result;
import com.wms.demo.entity.Goods;
import com.wms.demo.entity.Record;
import com.wms.demo.service.GoodsService;
import com.wms.demo.service.RecordService;
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
 * @since 2023-09-12
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        Page<Record> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.apply("a.goods=b.id and c.id = b.storage and b.goodsType = d.id");

        if ("2".equals(roleId)) {
            wrapper.apply("a.userId="+userId);
        }
        if (StringUtils.hasLength(name)) {
            wrapper.like("b.name", name);
        }
        if (StringUtils.hasLength(goodstype)) {
            wrapper.eq("d.id", goodstype);
        }
        if (StringUtils.hasLength(storage)) {
            wrapper.eq("c.id", storage);
        }

        IPage result = recordService.pageCC(page, wrapper);
        return Result.suc(result.getRecords(), result.getTotal());

    }

    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getCount();

        String action = record.getAction();
        if ("2".equals(action)) {//入库
            n = -n;
            record.setCount(n);
        }

        int num = goods.getCount() + n;
        goods.setCount(num);
        goodsService.updateById(goods);
        return recordService.save(record) ? Result.suc() : Result.fail();
    }
}
