package com.example.mmq.mqserver.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/30 18:43
 */
@Data
public class BasicProperties implements Serializable {
    // 消息的唯一身份标识，此处为了保证 id 的唯一性，使用 UUID
    private String messageId;
    // 是一个消息上带有的内容，和 bindingKey 做匹配 (交换机类型为 TOPIC)
    // 如果当前的交换机类型是 DIRECT，此时 routingKey 就表示要转发的队列名
    // 如果当前的交换机类型是 FANOUT，此时 routingKey 无意义（不使用）
    private String routingKey;
    // 这个属性表示消息是否要持久化。1 表示不持久化，2 表示持久化
    private int deliverMode = 1;

    // 其实针对 RabbitMQ 来说，BasicProperties 里面还有很多别的属性，但是我们这里就先不考虑了。
}
