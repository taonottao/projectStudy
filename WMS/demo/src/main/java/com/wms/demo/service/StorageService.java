package com.wms.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.demo.entity.Storage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.demo.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wt
 * @since 2023-09-11
 */
public interface StorageService extends IService<Storage> {
    IPage pageCC(IPage<Storage> page, Wrapper wrapper);
}
