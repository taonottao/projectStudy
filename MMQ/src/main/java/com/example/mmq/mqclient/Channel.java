package com.example.mmq.mqclient;

import com.example.mmq.common.*;
import com.example.mmq.mqserver.core.BasicProperties;
import com.example.mmq.mqserver.core.ExchangeType;
import lombok.Data;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/11 14:14
 */
@Data
public class Channel {
    private String channelId;
    // 当前这个 channel 属于哪个连接
    private Connection connection;
    // 用来存储后续客户端收到的服务器的响应, key 是 rid
    private ConcurrentHashMap<String, BasicReturns> basicReturnsMap = new ConcurrentHashMap<>();
    // 如果当前的 channel 订阅了某个队列，就需要在此处记录下对应的回调是啥。当该队列的消息返回回来的时候，调用回调。
    // 此处约定一个 Channel 中只能有一个回调
    private Consumer consumer;

    public Channel(String channelId, Connection connection) {
        this.channelId = channelId;
        this.connection = connection;
    }

    // 在这个方法中，和服务器进行交互，告知服务器，此处客户端创建了新的 channel 了
    public boolean createChannel() throws IOException {
        // 对于创建 Channel 操作来说，payload 就是一个 basicArguments 对象
        BasicArguments basicArguments = new BasicAckArguments();
        basicArguments.setChannelId(channelId);
        basicArguments.setRid(generateRid());
        byte[] payload = BinaryTool.toBytes(basicArguments);

        Request request = buildRequest(0x1, payload);
        // 构造出完整请求后，就可以发送这个请求了
        connection.writeRequest(request);
        // 等待服务器的响应
        BasicReturns basicReturns = waitResult(basicArguments.getRid());
        return basicReturns.isOk();
    }

    // 期望使用这个方法来阻塞等待服务器的响应
    private BasicReturns waitResult(String rid) {
        BasicReturns basicReturns = null;
        while ((basicReturns = basicReturnsMap.get(rid)) == null) {
            // 说明响应还没到，需要阻塞等待
            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 读取成功之后，还需要把这个消息从哈希表中删除掉。
        basicReturnsMap.remove(rid);
        return basicReturns;
    }

    public void putReturns(BasicReturns basicReturns) {
        basicReturnsMap.put(basicReturns.getRid(), basicReturns);
        synchronized (this) {
            notifyAll();
        }
    }

    private String generateRid() {
        return "R-" + UUID.randomUUID().toString();
    }

    // 关闭 channel，给服务器发送 type=0x2 的请求
    public boolean close() throws IOException {
        BasicArguments basicArguments = new BasicAckArguments();
        basicArguments.setRid(generateRid());
        basicArguments.setChannelId(channelId);
        byte[] payload = BinaryTool.toBytes(basicArguments);

        Request request = buildRequest(0x2, payload);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(basicArguments.getRid());
        return basicReturns.isOk();
    }

    // 创建交换机
    public boolean exchangeDeclare(String exchangeName, ExchangeType exchangeType, boolean durable, boolean autoDelete,
                                   Map<String, Object> arguments) throws IOException {
        ExchangeDeclareArguments exchangeDeclareArguments = new ExchangeDeclareArguments();
        exchangeDeclareArguments.setRid(generateRid());
        exchangeDeclareArguments.setChannelId(channelId);
        exchangeDeclareArguments.setExchangeName(exchangeName);
        exchangeDeclareArguments.setExchangeType(exchangeType);
        exchangeDeclareArguments.setDurable(durable);
        exchangeDeclareArguments.setAutoDelete(autoDelete);
        exchangeDeclareArguments.setArguments(arguments);
        byte[] payload = BinaryTool.toBytes(exchangeDeclareArguments);

        Request request = buildRequest(0x3, payload);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(exchangeDeclareArguments.getRid());
        return basicReturns.isOk();
    }

    // 删除交换机
    public boolean exchangeDelete(String exchangeName) throws IOException {
        ExchangeDeleteArguments exchangeDeleteArguments = new ExchangeDeleteArguments();
        exchangeDeleteArguments.setRid(generateRid());
        exchangeDeleteArguments.setChannelId(channelId);
        exchangeDeleteArguments.setExchangeName(exchangeName);
        byte[] payload = BinaryTool.toBytes(exchangeDeleteArguments);

        Request request = buildRequest(0x4, payload);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(exchangeDeleteArguments.getRid());
        return basicReturns.isOk();
    }

    // 创建队列
    public boolean queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete,
                                Map<String, Object> arguments) throws IOException {
        QueueDeclareArguments queueDeclareArguments = new QueueDeclareArguments();
        queueDeclareArguments.setRid(generateRid());
        queueDeclareArguments.setChannelId(channelId);
        queueDeclareArguments.setQueueName(queueName);
        queueDeclareArguments.setDurable(durable);
        queueDeclareArguments.setExclusive(exclusive);
        queueDeclareArguments.setAutoDelete(autoDelete);
        queueDeclareArguments.setArguments(arguments);
        byte[] payload = BinaryTool.toBytes(queueDeclareArguments);

        Request request = buildRequest(0x5, payload);

        connection.writeRequest(request);

        BasicReturns basicReturns = waitResult(queueDeclareArguments.getRid());
        return basicReturns.isOk();
    }

    // 删除队列
    public boolean queueDelete(String queueName) throws IOException {
        QueueDeleteArguments queueDeleteArguments = new QueueDeleteArguments();
        queueDeleteArguments.setRid(generateRid());
        queueDeleteArguments.setChannelId(channelId);
        queueDeleteArguments.setQueueName(queueName);
        byte[] payload = BinaryTool.toBytes(queueDeleteArguments);

        Request request = buildRequest(0x6, payload);

        connection.writeRequest(request);

        BasicReturns basicReturns = waitResult(queueDeleteArguments.getRid());
        return basicReturns.isOk();
    }

    // 创建绑定
    public boolean queueBind(String queueName, String exchangeName, String bindingKey) throws IOException {
        QueueBindArguments queueBindArguments = new QueueBindArguments();
        queueBindArguments.setRid(generateRid());
        queueBindArguments.setChannelId(channelId);
        queueBindArguments.setQueueName(queueName);
        queueBindArguments.setExchangeName(exchangeName);
        queueBindArguments.setBindingKey(bindingKey);
        byte[] payload = BinaryTool.toBytes(queueBindArguments);

        Request request = buildRequest(0x7, payload);

        connection.writeRequest(request);

        BasicReturns basicReturns = waitResult(queueBindArguments.getRid());
        return basicReturns.isOk();
    }

    // 解除绑定
    public boolean queueUnbind(String queueName, String exchangeName) throws IOException {
        QueueUnbindArguments arguments = new QueueUnbindArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        arguments.setExchangeName(exchangeName);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = buildRequest(0x8, payload);

        connection.writeRequest(request);

        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    // 发送消息
    public boolean basicPublish(String exchangeName, String routingKey, BasicProperties basicProperties, byte[] body) throws IOException {
        BasicPublishArguments arguments = new BasicPublishArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setExchangeName(exchangeName);
        arguments.setRoutingKey(routingKey);
        arguments.setBasicProperties(basicProperties);
        arguments.setBody(body);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = buildRequest(0x9, payload);
        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    // 订阅消息
    public boolean basicConsume(String queueName, boolean autoAck, Consumer consumer) throws MQException, IOException {
        // 先设置回调
        if (this.consumer != null) {
            throw new MQException("该 channel 已经设置过消费消息的回调了，不能重复设置！");
        }
        this.consumer = consumer;

        BasicConsumeArguments arguments = new BasicConsumeArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        // 此处的 consumerTag 也是用 channelId 来表示了
        arguments.setConsumerTag(channelId);
        arguments.setAutoAck(autoAck);
        arguments.setQueueName(queueName);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = buildRequest(0xa, payload);

        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }

    // 确认消息
    public boolean basicAck(String queueName, String messageId) throws IOException {
        BasicAckArguments arguments = new BasicAckArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        arguments.setMessageId(messageId);
        byte[] payload = BinaryTool.toBytes(arguments);

        Request request = buildRequest(0xb, payload);
        connection.writeRequest(request);
        BasicReturns basicReturns = waitResult(arguments.getRid());
        return basicReturns.isOk();
    }


    // 封装设置 Request 方法
    private Request buildRequest(int type, byte[] payload) {
        Request request = new Request();
        request.setType(type);
        request.setLength(payload.length);
        request.setPayload(payload);
        return request;
    }

}
