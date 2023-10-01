package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/30 22:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    // 订单编号
    private String orderId;
    // 用户ID
    private int userId;
    // 订单总价格
    private int orderTotalPrice;
    // 用户地址信息
    private String userAddr;
    // 用户姓名
    private String userName;
    // 用户电话
    private String userTel;
    // 订单创建时间
    private Date createTime;
    // 订单备注
    private String orderDesc;
    // 回收类型: 1 快递， 2 上门验收
    private int retrieveType;
    // 订单状态：1 新订单，2 待指派，3 已指派，4 已完成，5 已关闭，6 用户已寄出，7 平台验收中，8 验收通过，9 待退回，10 已退回，11 用户已取消
    private int orderStatus;

    private String payName;
    private String payAccount;

    //寄送物流名称
    private String sendLogisticsName;
    //寄送物流单号
    private String sendLogisticsId;
    // 订单指派处理人
    private String orderProcessor;
    // 退回原因
    private String backText;
    // 退回物流名称
    private String backLogisticsName;
    // 退回物流单号
    private String backLogisticsId;

}
