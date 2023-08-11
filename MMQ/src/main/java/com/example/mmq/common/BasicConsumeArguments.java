package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/9 9:29
 */
@Data
public class BasicConsumeArguments extends BasicArguments implements Serializable {
    private String consumerTag;
    private String queueName;
    private boolean autoAck;
    // 这个类对应的 basicConsume 方法中，还有一个参数，是回调函数（如何来处理消息）
    // 这个回调函数是不能通过网络传输的
    // 站在 broker server 这边，针对消息的处理回调，其实是统一的（把消息返回给客户端）
    // 客户端这边收到消息之后，再在客户端这边执行一个用户自定义的回调就行了
    // 此时，客户端不需要把自身的回调告诉服务器
    // 这个类也就不需要 consumer 成员了
}

