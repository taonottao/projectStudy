package com.example.mmq.mqserver.core;

import lombok.Data;

/**
 * 表示队列与交换机之间的关系
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/30 17:31
 */
@Data
public class Binding {
    private String exchangeName;
    private String queueName;
    // 这个就相当于文章中介绍的QQ画图红包的出题
    private String bindingKey;
}
