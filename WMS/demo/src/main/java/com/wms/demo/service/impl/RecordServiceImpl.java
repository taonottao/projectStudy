package com.wms.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.entity.Record;
import com.wms.demo.mapper.RecordMapper;
import com.wms.demo.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wt
 * @since 2023-09-12
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {


    @Autowired
    private RecordMapper recordMapper;

    @Override
    public IPage pageCC(Page<Record> page, Wrapper<Record> wrapper) {
        return recordMapper.pageCC(page, wrapper);
    }
}
