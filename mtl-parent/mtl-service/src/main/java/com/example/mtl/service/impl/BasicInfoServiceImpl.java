package com.example.mtl.service.impl;

import com.example.mtl.beans.BasicInfo;
import com.example.mtl.service.BasicInfoService;
import com.wt.mtl.dao.BasicInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/23 20:34
 */
@Service
public class BasicInfoServiceImpl implements BasicInfoService {
    @Autowired
    private BasicInfoDAO basicInfoDAO;
    @Override
    public List<BasicInfo> listInfoByGoodsId(int goodsId, int step) {
        List<BasicInfo> basicInfoList = basicInfoDAO.selectBasicInfoByGoodsId(goodsId, step);
        return basicInfoList;
    }
}
