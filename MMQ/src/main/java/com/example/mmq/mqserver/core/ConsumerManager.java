package com.example.mmq.mqserver.core;

import com.example.mmq.common.Consumer;
import com.example.mmq.common.ConsumerEnv;
import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.VirtualHost;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 通过这个类来实现消费消息的核心逻辑
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/7 16:17
 */
public class ConsumerManager {
    // 持有一个上层的 VirtualHost 对象的引用，用来操作数据。
    private VirtualHost parent;
    // 指定一个线程池，负责去执行具体的回调任务
    private ExecutorService workerPool = Executors.newFixedThreadPool(4);
    // 存放“令牌”的队列
    private BlockingQueue<String> tokenQueue = new LinkedBlockingQueue<>();
    // 扫描线程
    private Thread scannerThread = null;

    public ConsumerManager(VirtualHost parent) {
        this.parent = parent;
        scannerThread = new Thread(() -> {
            while (true) {
                try {
                    // 1. 拿到令牌
                    String queueName = tokenQueue.take();
                    // 2. 根据令牌，找到队列
                    MSGQueue queue = parent.getMemoryDataCenter().getQueue(queueName);
                    if (queue == null) {
                        throw new MQException("[ConsumerManager] 取令牌后发现，队列不存在！queueName=" + queueName);
                    }
                    // 3. 从这个队列中消费一个消息
                    synchronized (queue) {
                        consumeMessage(queue);
                    }
                } catch (InterruptedException | MQException e) {
                    e.printStackTrace();
                }
            }
        });
        // 把线程设为后台线程
        scannerThread.setDaemon(true);
        scannerThread.start();
    }

    // 这个方法的调用时机就是发送消息的时候
    public void notifyConsume(String queueName) throws InterruptedException {
        tokenQueue.put(queueName);
    }

    public void addConsumer(String consumerTag, String queueName, boolean autoAck, Consumer consumer) throws MQException {
        // 找到对应的队列
        MSGQueue queue = parent.getMemoryDataCenter().getQueue(queueName);
        if (queue == null) {
            throw new MQException("[ConsumerManager] 队列不存在！queueName=" + queueName);
        }
        ConsumerEnv consumerEnv = new ConsumerEnv(consumerTag, queueName, autoAck, consumer);
        synchronized (queue) {
            queue.addConsumerEnv(consumerEnv);
            // 如果当前队列中已经有了一些消息了，需要立即就消费掉
            int n = parent.getMemoryDataCenter().getMessageCount(queueName);
            for (int i = 0; i < n; i++) {
                // 这个方法调用一次就消费一条消息
                consumeMessage(queue);
            }
        }
    }

    private void consumeMessage(MSGQueue queue) {
        // 1. 按照轮询的方式，找个消费者出来
        ConsumerEnv luckyDog = queue.chooseConsumer();
        if (luckyDog == null) {
            // 当前队列没有消费者，暂时不消费，等后面有消费者出现再说
            return;
        }
        // 2. 从队列中取出一个消息
        Message message = parent.getMemoryDataCenter().pollMessage(queue.getName());
        if (message == null) {
            // 当前队列中还没有消息，也不需要消费
            return;
        }
        // 3. 把消息带入到消费者的回调方法中，丢给线程池执行
        workerPool.submit(() ->{
            try {
                // 1. 把消息放到待确认的集合里, 这个操作势必在执行回调之前
                parent.getMemoryDataCenter().addMessageWaitAck(queue.getName(), message);
                // 2. 真正执行回调
                luckyDog.getConsumer().handleDelivery(luckyDog.getConsumerTag(), message.getBasicProperties(), message.getBody());
                // 3. 如果当前是“自动应答”，就可以直接删除消息了
                //    如果当前是“手动应答”，则先不处理，交给回叙消费者调用 basicAck 方法来处理
                if (luckyDog.isAutoAck()) {
                    if (message.getDeliverMode() == 2) {
                        parent.getDiskDataCenter().deleteMessage(queue, message);
                    }
                    parent.getMemoryDataCenter().removeMessageWaitAck(queue.getName(), message.getMessageId());
                    // 删除内存里消息中心里的消息
                    parent.getMemoryDataCenter().removeMessage(message.getMessageId());
                    System.out.println("[ConsumerManager] 消息被成功消费！queueName=" + queue.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
