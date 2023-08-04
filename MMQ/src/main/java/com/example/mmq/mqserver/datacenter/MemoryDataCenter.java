package com.example.mmq.mqserver.datacenter;

import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.Exchange;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.core.Message;

import java.util.LinkedList;
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

}
