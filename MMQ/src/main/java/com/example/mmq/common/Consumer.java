package com.example.mmq.common;

import com.example.mmq.mqserver.core.BasicProperties;

import java.io.IOException;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/7 13:56
 */
@FunctionalInterface
public interface Consumer {
    // Delivery 的意思是“投递”，这个方法预期是在每次服务器收到消息之后，来调用
    // 通过这个方法把消息推送给对应的消费者
    void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) throws MQException, IOException;
}
