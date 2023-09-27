package com.example.mtl.service.impl;

import com.example.mtl.beans.Goods;
import com.example.mtl.service.PriceCountService;
import com.wt.mtl.dao.GoodsDAO;
import com.wt.mtl.dao.InfoDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/26 22:07
 */
@Service
public class PriceCountServiceImpl implements PriceCountService {

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private InfoDetailDAO infoDetailDAO;

    @Override
    public int countPrice(int goodsId, String ids) {


        Goods goods = goodsDAO.selectGoodsById(goodsId);
        int price1 = infoDetailDAO.countPriceInfoDetails(goodsId, ids);
        int price2 = goods.getGoodCost()-price1;
        int price = Math.max(price2, goods.getGoodMinPrice());

        return price;
    }
}
