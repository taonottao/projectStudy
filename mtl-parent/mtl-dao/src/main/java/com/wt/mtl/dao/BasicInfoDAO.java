package com.wt.mtl.dao;

import com.example.mtl.beans.BasicInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/21 23:45
 */
public interface BasicInfoDAO {

    List<BasicInfo> selectBasicInfoByGoodsId(@Param("goodsId") int goodsId,
                                             @Param("step") int step);

    List<BasicInfo> selectInfoDetailByIds(String ids);

}
