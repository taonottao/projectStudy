package com.example.mtl.service;

import com.example.mtl.beans.BasicInfo;

import java.util.List;

/**
 * 商品评估项
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/23 20:05
 */
public interface BasicInfoService {

    List<BasicInfo> listInfoByGoodsId(int goodsId, int step);

    // 根据已经选中的选项 ID 查询选项详情已经选项对应的评估项信息
    List<BasicInfo> listInfoDetailByIds(String ids);

}
