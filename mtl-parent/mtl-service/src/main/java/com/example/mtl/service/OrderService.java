package com.example.mtl.service;

import com.example.mtl.beans.Order;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/1 14:41
 */
public interface OrderService {

    String addOrder(Order order, int goodId, String ids);

}
