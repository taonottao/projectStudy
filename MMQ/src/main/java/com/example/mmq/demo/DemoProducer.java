package com.example.mmq.demo;

import com.example.mmq.mqclient.Channel;
import com.example.mmq.mqclient.Connection;
import com.example.mmq.mqclient.ConnectionFactory;
import com.example.mmq.mqserver.core.BasicProperties;
import com.example.mmq.mqserver.core.ExchangeType;

import java.io.IOException;

/**
 * 这个类用来表示一个生产者
 * 通常这是一个单独的服务器程序
 */
public class DemoProducer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("启动生产者！");
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("47.113.225.72");
        factory.setHost("127.0.0.1");
        factory.setPort(9090);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建交换机和队列
        channel.exchangeDeclare("testExchange1", ExchangeType.DIRECT, true, false, null);
        channel.queueDeclare("testQueue1", true, false, false, null);

        // 创建一个消息并发送
        byte[] body = "hello222 consumer".getBytes();
        BasicProperties basicProperties = new BasicProperties();
        basicProperties.setDeliverMode(2);
        boolean ok = channel.basicPublish("testExchange1", "testQueue1", basicProperties, body);
        System.out.println("消息投递完成！ok=" + ok);

        Thread.sleep(500);
        channel.close();
        connection.close();
    }
}
