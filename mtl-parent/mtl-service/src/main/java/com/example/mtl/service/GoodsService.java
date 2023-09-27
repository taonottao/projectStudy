package com.example.mtl.service;


import com.example.mtl.beans.Goods;
import com.example.mtl.util.PageInfo;


/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/20 18:13
 */
public interface GoodsService {
    PageInfo<Goods> listGoodsByBrandId(int brandId, int pageNum);
    Goods getGoodsById(int goodsId);
}
