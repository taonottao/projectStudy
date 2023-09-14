package com.wms.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.entity.Goodstype;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wt
 * @since 2023-09-11
 */
public interface GoodstypeService extends IService<Goodstype> {

    IPage pageCC(Page<Goodstype> page, Wrapper<Goodstype> wrapper);

}
