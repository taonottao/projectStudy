package com.wt.mtl.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/26 21:35
 */
public interface InfoDetailDAO {

    int countPriceInfoDetails(@Param("goodsId") int goodsId,
                              @Param("ids") String ids);

}
