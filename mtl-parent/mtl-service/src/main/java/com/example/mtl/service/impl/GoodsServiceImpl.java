package com.example.mtl.service.impl;

import com.example.mtl.beans.Goods;
import com.example.mtl.service.GoodsService;
import com.example.mtl.util.PageInfo;
import com.wt.mtl.dao.GoodsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/20 18:30
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDAO goodsDAO;
    @Override
    public PageInfo<Goods> listGoodsByBrandId(int brandId, int pageNum) {
        int start = (pageNum-1)*PageInfo.PAGE_SIZE;
        int limit = PageInfo.PAGE_SIZE;

        List<Goods> goodsList = goodsDAO.selectGoodsByBrandIdWithPage(brandId, start, limit);
        int count = goodsDAO.selectCountByBrandId(brandId);
        int pageCount = count%limit==0? count/limit : count/limit+1;
        int prePage = pageNum > 1 ? pageNum-1:1;
        int nextPage = pageNum < pageCount?pageNum+1:pageCount;
        return new PageInfo<Goods>(pageNum, pageCount,count, prePage, nextPage, goodsList);
    }

    @Override
    public Goods getGoodsById(int goodsId) {
        Goods goods = goodsDAO.selectGoodsById(goodsId);
        return goods;
    }
}
