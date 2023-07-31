package com.example.mmq.mqserver.mapper;

import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.Exchange;
import com.example.mmq.mqserver.core.MSGQueue;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/31 13:28
 */
@Mapper
public interface MetaMapper {
    // 提供三个核心的建表方法
    void createExchangeTable();
    void createQueueTable();
    void createBindingTable();


    void insertExchange(Exchange exchange);
    void deleteExchange(String exchangeName);
    void insertQueue(MSGQueue queue);
    void deleteQueue(String queueName);
    void insertBinding(Binding binding);
    void deleteBinding(Binding binding);
}
