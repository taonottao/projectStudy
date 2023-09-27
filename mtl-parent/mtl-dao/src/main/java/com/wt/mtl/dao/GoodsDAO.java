package com.wt.mtl.dao;

import com.example.mtl.beans.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/20 16:49
 */
public interface GoodsDAO {
    List<Goods> selectGoodsByBrandIdWithPage(@Param("brandId") int brandId,
                                             @Param("start") int start,
                                             @Param("limit") int limit);

    int selectCountByBrandId(int brandId);

    Goods selectGoodsById(int goodsId);
}
