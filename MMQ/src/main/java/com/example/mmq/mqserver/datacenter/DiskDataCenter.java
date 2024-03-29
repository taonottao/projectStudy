package com.example.mmq.mqserver.datacenter;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.Exchange;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.core.Message;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用这个类来管理所有硬盘上的数据
 * 1. 数据库：交换机、绑定、队列
 * 2. 数据文件：消息
 * 上层应用如果需要操作硬盘，统一需要通过这个类来使用（上层代码不关心当前数据是否是存储在数据库中还是文件中）
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/3 18:51
 */
public class DiskDataCenter {
    private DatabaseManager databaseManager = new DatabaseManager();
    private MessageFileManager messageFileManager = new MessageFileManager();

    public void init() {
        // 针对上述两个实例进行初始化
        databaseManager.init();
        messageFileManager.init();
    }

    // 封装交换机操作
    public void insertExchange(Exchange exchange) {
        databaseManager.insertExchange(exchange);
    }

    public void deleteExchange(String exchangerName) {
        databaseManager.deleteExchange(exchangerName);
    }

    public List<Exchange> selectAllExchange() {
        return databaseManager.selectAllExchanges();
    }

    // 封装队列操作
    public void insertQueue(MSGQueue queue) throws IOException {
        databaseManager.insertQueue(queue);
        // 创建队列的同时，不仅仅是把队列对象写到数据库中，还需要创建出对应的目录和文件
        messageFileManager.createQueueFiles(queue.getName());
    }

    public void deleteQueue(String queueName) throws IOException {
        databaseManager.deleteQueue(queueName);
        // 删除队列的同时，不仅仅是把队列从数据库中删除，还需要删除对应的目录和文件
        messageFileManager.destoryQueueFiles(queueName);
    }

    public List<MSGQueue> selectAllQueue() {
        return databaseManager.selectAllQueue();
    }

    // 封装绑定操作
    public void insertBinding(Binding binding) {
        databaseManager.insertBinding(binding);
    }

    public void deleteBinding(Binding binding) {
        databaseManager.deleteBinding(binding);
    }

    public List<Binding> selectAllBinding() {
        return databaseManager.selectAllBinding();
    }

    // 封装消息操作
    public void sendMessage(MSGQueue queue, Message message) throws MQException, IOException {
        messageFileManager.sendMessage(queue, message);
    }

    public void deleteMessage(MSGQueue queue, Message message) throws IOException, ClassNotFoundException, MQException {
        messageFileManager.deleteMessage(queue, message);
        if (messageFileManager.checkGC(queue.getName())) {
            messageFileManager.gc(queue);
        }
    }

    public LinkedList<Message> loadAllMessageFromQueue(String queueName) throws MQException, IOException, ClassNotFoundException {
        return messageFileManager.loadAllMessageFromQueue(queueName);
    }

}
