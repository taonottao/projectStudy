package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 订单快照实体类
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/30 22:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {

    // 快照ID
    private int itemId;
    //订单编号
    private String orderId;
    // 商品ID
    private int goodId;
    // 商品名称
    private String goodName;
    // 商品图片
    private String goodImgPath;
    // 商品评估项目
    private String goodInfo;
    // 商品评估价格
    private int goodPrice;
    // 评价状态：0 未评价  1 已评价
    private int isComment;

}
