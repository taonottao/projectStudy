package com.example.mmq.demo;

import com.example.mmq.common.Consumer;
import com.example.mmq.common.MQException;
import com.example.mmq.mqclient.Channel;
import com.example.mmq.mqclient.Connection;
import com.example.mmq.mqclient.ConnectionFactory;
import com.example.mmq.mqserver.core.BasicProperties;
import com.example.mmq.mqserver.core.ExchangeType;

import java.io.IOException;

/**
 * 这个类表示一个消费者
 * 通常这个类也应该是在一个独立的服务器中被执行
 */
public class DemoConsumer {
    public static void main(String[] args) throws IOException, MQException, InterruptedException {
        System.out.println("启动消费者");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.113.225.72");
        factory.setPort(9090);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("testExchange", ExchangeType.DIRECT, true, false, null);
        channel.queueDeclare("testQueue", true, false, false, null);

        channel.basicConsume("testQueue", true, new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) throws MQException, IOException {
                System.out.println("[消费数据]开始！");
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("basicProperties=" + basicProperties);
                String bodyString = new String(body, 0, body.length);
                System.out.println("body=" + bodyString);
                System.out.println("[消费数据]结束！");
            }
        });

        // 由于消费者也不知道生产者要生产多少，就在这里通过这个循环模拟一直等待消息
        while (true) {
            Thread.sleep(500);
        }
    }
}
