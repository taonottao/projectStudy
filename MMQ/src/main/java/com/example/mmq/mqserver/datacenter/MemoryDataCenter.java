package com.example.mmq.mqserver.datacenter;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.Exchange;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.core.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用这个类来统一管理内存中的所有数据
 * 该类提供的一些方法，可能会在多线程的环境下使用，因此要注意线程安全问题
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/3 23:12
 */
public class MemoryDataCenter {
    private ConcurrentHashMap<String, Exchange> exchangeMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, MSGQueue> queueMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Binding>> bindingsMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Message> messageMap = new ConcurrentHashMap<>();

    // 队列与消息的关系
    private ConcurrentHashMap<String, LinkedList<Message>> queueMessageMap = new ConcurrentHashMap<>();

    // 待确认的消息
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Message>> queueMessageWaitAckMap = new ConcurrentHashMap<>();

    public void insertExchange(Exchange exchange) {
        exchangeMap.put(exchange.getName(), exchange);
        System.out.println("[MemoryDataCenter] 新交换机添加成功！exchangeName=" + exchange.getName());
    }

    public Exchange getExchange(String exchangeName) {
        return exchangeMap.get(exchangeName);
    }

    public void deleteExchange(String exchangeName) {
        exchangeMap.remove(exchangeName);
        System.out.println("[MemoryDataCenter] 交换机被移除！exchangeName=" + exchangeName);
    }

    public void insertQueue(MSGQueue queue) {
        queueMap.put(queue.getName(), queue);
        System.out.println("[MemoryDataCenter] 新队列添加成功！exchangeName=" + queue.getName());
    }

    public MSGQueue getQueue(String queueName) {
        return queueMap.get(queueName);
    }

    public void deleteQueue(String queueName) {
        queueMap.remove(queueName);
        System.out.println("[MemoryDataCenter] 队列已移除！queueName=" + queueName);
    }

    public void insertBinding(Binding binding) throws MQException {
        // 先使用 exchangeName 查一下，对应的 哈希表 是否存在，不存在就创建一个
//        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.get(binding.getExchangeName());
//        if (bindingMap == null) {
//            bindingMap = new ConcurrentHashMap<>();
//            bindingsMap.put(binding.getExchangeName(), bindingMap);
//        }
        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.computeIfAbsent(binding.getExchangeName(),
                k -> new ConcurrentHashMap<>());

        // 再根据 queueName 查一下，如果已经存在，就抛出异常，不存在才能插入。
        synchronized (bindingMap) {
            if (bindingMap.get(binding.getQueueName()) != null) {
                throw new MQException("[MemoryDataCenter] 绑定已经存在！exchangeName=" + binding.getExchangeName() +
                        ", queueName=" + binding.getQueueName());
            }
            bindingMap.put(binding.getQueueName(), binding);
        }
        System.out.println("[MemoryDataCenter] 新绑定添加成功！exchangeName=" + binding.getExchangeName() +
                ", queueName=" + binding.getQueueName());
    }

    /**
     * 获取绑定，写两个版本
     * 1. 根据 exchangeName 和 queueName 确定唯一一个绑定
     * 2. 根据 exchangeName 获取到所有的 绑定
     * @param exchangeName
     * @param queueName
     * @return
     */
    public Binding getBinding(String exchangeName, String queueName) {
        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.get(exchangeName);
        if (bindingMap == null) {
            return null;
        }
        return bindingMap.get(queueName);
    }

    public ConcurrentHashMap<String, Binding> getBindings(String exchangeName) {
        return bindingsMap.get(exchangeName);
    }

    public void deleteBinding(Binding binding) throws MQException {
        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.get(binding.getExchangeName());
        if (bindingMap == null) {
            // 该交换机没有绑定任何队列
            throw new MQException("[MemoryDatacenter] 绑定不存在！exchangeName=" + binding.getExchangeName() +
                    ", queueName=" + binding.getQueueName());
        }

        bindingMap.remove(binding.getQueueName());
        System.out.println("[MemoryDataCenter] 绑定已移除！exchangeName=" + binding.getExchangeName() +
                ", queueName=" + binding.getQueueName());
    }

    public void addMessage(Message message) {
        messageMap.put(message.getMessageId(), message);
        System.out.println("[MemoryDataCenter] 新消息添加成功！messageId=" + message.getMessageId());
    }

    public Message getMessage(String messageId) {
        return messageMap.get(messageId);
    }

    public void removeMessage(String messageId) {
        messageMap.remove(messageId);
        System.out.println("[MemoryDataCenter] 消息被移除！messageId=" + messageId);
    }

    // 发送消息到指定队列
    public void senMessage(MSGQueue queue, Message message) {

        LinkedList<Message> messages = queueMessageMap.computeIfAbsent(queue.getName(),
                k -> new LinkedList<>());

        synchronized (messages) {
            messages.add(message);
        }
        addMessage(message);
        System.out.println("[MemoryDataCenter] 消息被投递到队列中！messageid=" + message.getMessageId());
    }

    // 从队列中取消息
    public Message pollMessage(String queueName) {
        LinkedList<Message> messages = queueMessageMap.get(queueName);
        if (messages == null) {
            return null;
        }
        synchronized (messages) {
            if (messages.size() == 0) {
                return null;
            }

            // 链表中有元素，就进行头删
            Message curMessage = messages.remove(0);
            System.out.println("[MemoryDataCenter] 消息从队列中取出！messageId=" + curMessage.getMessageId());
            return curMessage;
        }
    }

    // 获取指定队列中的消息个数
    public int getMessageCount(String queueName) {
        LinkedList<Message> messages = queueMessageMap.get(queueName);
        if (messages == null) {
            return 0;
        }
        synchronized (messages) {
            return messages.size();
        }
    }

    // 添加未确认的消息
    public void addMessageWaitAck(String queueName, Message message) {
        ConcurrentHashMap<String, Message> messageHashMap = queueMessageWaitAckMap.computeIfAbsent(queueName,
                k -> new ConcurrentHashMap<>());
        messageHashMap.put(message.getMessageId(), message);
        System.out.println("[MemoryDataCenter] 消息进入待确认队列！messageId=" + message.getMessageId());
    }

    // 删除未确认的消息
    public void removeMessageWaitAck(String queueName, String messageId) {
        ConcurrentHashMap<String, Message> messageHashMap = queueMessageWaitAckMap.get(queueName);
        if (messageHashMap == null) {
            return;
        }
        messageHashMap.remove(messageId);
        System.out.println("[MemoryDataCenter] 消息从待确认队列删除！messageId=" + messageId);
    }

    // 获取指定的未确认的消息
    public Message getMessageWaitAck(String queueName, String messageId) {
        ConcurrentHashMap<String, Message> messageHashMap = queueMessageWaitAckMap.get(queueName);
        if (messageHashMap == null) {
            return null;
        }
        return messageHashMap.get(messageId);
    }

    // 这个方法就是从硬盘上读取数据，把硬盘中之前的持久化存储的各个维度的数据都恢复到内存中
    public void recovery(DiskDataCenter diskDataCenter) throws MQException, IOException, ClassNotFoundException {
        exchangeMap.clear();
        queueMap.clear();
        bindingsMap.clear();
        messageMap.clear();
        queueMessageMap.clear();
        // 1. 恢复所有的交换机数据
        List<Exchange> exchanges = diskDataCenter.selectAllExchange();
        for (Exchange exchange : exchanges) {
            exchangeMap.put(exchange.getName(), exchange);
        }
        // 2. 恢复所有的队列数据
        List<MSGQueue> queues = diskDataCenter.selectAllQueue();
        for (MSGQueue queue : queues) {
            queueMap.put(queue.getName(), queue);
        }
        // 3. 恢复所有的绑定数据
        List<Binding> bindings = diskDataCenter.selectAllBinding();
        for (Binding binding : bindings) {
            ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.computeIfAbsent(binding.getExchangeName(),
                    k -> new ConcurrentHashMap<>());
            bindingMap.put(binding.getQueueName(), binding);
        }
        // 4. 恢复所有的消息
        // 遍历所有的队列，根据每个队列的名字，获取到所有的消息
        for (MSGQueue queue : queues) {
            LinkedList<Message> messages = diskDataCenter.loadAllMessageFromQueue(queue.getName());
            queueMessageMap.put(queue.getName(), messages);
            for (Message message : messages) {
                messageMap.put(message.getMessageId(), message);
            }
        }

        // 注意！针对“未确认消息” 这部分内存中存在的数据，不需要从何硬盘中恢复。之前考虑硬盘存储的时候，也没有设定这一块
        // 一旦在等待 ack 的过程中，服务器重启了，此时这些未被确认的消息就恢复成未被取走的消息
        // 这个消息在硬盘上存储的时候，就被当做是“未被取走”

    }

}
