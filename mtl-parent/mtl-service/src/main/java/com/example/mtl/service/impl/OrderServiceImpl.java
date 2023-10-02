package com.example.mtl.service.impl;

import com.example.mtl.beans.*;
import com.example.mtl.service.OrderService;
import com.wt.mtl.dao.BasicInfoDAO;
import com.wt.mtl.dao.GoodsDAO;
import com.wt.mtl.dao.OrderDAO;
import com.wt.mtl.dao.OrderItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/1 14:43
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private BasicInfoDAO basicInfoDAO;

    @Override
    @Transactional
    public String addOrder(Order order, int goodId, String ids) {
        // 1. 生成订单编号(唯一)
        String orderId  = UUID.randomUUID().toString().replace("-", "");
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        // 设置默认订单状态
        // 快递交易: 1待寄送，2已寄出，3已送达(待检测)，（4待付款，5已完成）/（6待退回，7代退回签收，8已退回）
        // 上门交易: 9待上门交易，5已完成，10已取消
        int orderStatus = order.getRetrieveType()==1?1:9;
        order.setOrderStatus(orderStatus);

        // 2. 保存订单
        int i = orderDAO.insertOrder(order);

        // 3. 保存快照
        if (i > 0) {
            Goods goods = goodsDAO.selectGoodsById(goodId);
            List<BasicInfo> basicInfoList = basicInfoDAO.selectInfoDetailByIds(ids);
            String goodsInfo = "";
            for (int m = 0; m < basicInfoList.size(); m++) {
                BasicInfo basicInfo = basicInfoList.get(m);
                goodsInfo += basicInfo.getBasicInfoName()+": ";
                List<InfoDetail> infoDetailList =  basicInfo.getInfoDetailList();
                for (int n = 0; n < infoDetailList.size(); n++) {
                    goodsInfo += infoDetailList.get(n).getInfoDetailName();
                    if (n < infoDetailList.size() - 1) {
                        goodsInfo += "、";
                    } else {
                        goodsInfo += " | ";
                    }

                }
            }
            OrderItem orderItem = new OrderItem(0, orderId, goods.getGoodId(), goods.getGoodName(),
                    goods.getGoodImg(), goodsInfo, order.getOrderTotalPrice(),0);
            int j = orderItemDAO.insertOrderItem(orderItem);
            if (j > 0) {
                return orderId;
            }
        }
        return null;
    }
}
