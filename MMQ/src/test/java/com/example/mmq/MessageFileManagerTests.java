package com.example.mmq;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.core.Message;
import com.example.mmq.mqserver.datacenter.MessageFileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/3 14:28
 */
@SpringBootTest
public class MessageFileManagerTests {

    private MessageFileManager messageFileManager = new MessageFileManager();

    private static final String queueName1 = "testQueue1";
    private static final String queueName2 = "testQueue2";

    @BeforeEach
    public void setUp() throws IOException {
        // 准备阶段，创建出两个队列，以备后用
        messageFileManager.createQueueFiles(queueName1);
        messageFileManager.createQueueFiles(queueName2);
    }

    @AfterEach
    public void tearDown() throws IOException {
        // 收尾阶段，就把刚才的队列干掉
        messageFileManager.destoryQueueFiles(queueName1);
        messageFileManager.destoryQueueFiles(queueName2);
    }

    @Test
    public void testCreateFiles(){
        File queueDataFile1 = new File("./data/" + queueName1 + "/queue_data.txt");
        Assertions.assertEquals(true, queueDataFile1.isFile());
        File queueStatFile1 = new File("./data/" + queueName1 + "/queue_stat.txt");
        Assertions.assertEquals(true, queueStatFile1.isFile());

        File queueDataFile2 = new File("./data/" + queueName2 + "/queue_data.txt");
        Assertions.assertEquals(true, queueDataFile2.isFile());
        File queueStatFile2 = new File("./data/" + queueName2 + "/queue_stat.txt");
        Assertions.assertEquals(true, queueStatFile2.isFile());
    }

    @Test
    public void testReadWriteStat(){
        MessageFileManager.Stat stat = new MessageFileManager.Stat();
        stat.totalCount = 100;
        stat.validCount = 50;

        // 此处需要用反射的方式，来调用 writeStat 和 readStat 了
        // java 原生的 api 很难用
        // 此处使用 Spring 封装好的 反射工具类
        ReflectionTestUtils.invokeMethod(messageFileManager, "writeStat", queueName1, stat);

        MessageFileManager.Stat newStat = ReflectionTestUtils.invokeMethod(messageFileManager, "readStat", queueName1);
        Assertions.assertEquals(100, newStat.totalCount);
        Assertions.assertEquals(50, newStat.validCount);
    }

    private MSGQueue createTestQueue(String queueName) {
        MSGQueue queue = new MSGQueue();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        return queue;
    }

    private Message createTestMessage(String content) {
        Message message = Message.createMessageWithId("testRoutingKey",null, content.getBytes());
        return message;
    }

    @Test
    public void testSendMessage() throws MQException, IOException, ClassNotFoundException {
        Message message = createTestMessage("testMessage");
        MSGQueue queue = createTestQueue(queueName1);
        messageFileManager.sendMessage(queue, message);

        MessageFileManager.Stat stat = ReflectionTestUtils.invokeMethod(messageFileManager, "readStat", queueName1);
        Assertions.assertEquals(1, stat.totalCount);
        Assertions.assertEquals(1, stat.validCount);

        LinkedList<Message> messages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(1, messages.size());
        Message curMessage = messages.get(0);
        Assertions.assertEquals(message.getMessageId(), curMessage.getMessageId());
        Assertions.assertEquals(message.getRoutingKey(), curMessage.getRoutingKey());
        Assertions.assertEquals(message.getDeliverMode(), curMessage.getDeliverMode());
        Assertions.assertArrayEquals(message.getBody(), curMessage.getBody());

        System.out.println("message:" + curMessage);
    }

    @Test
    public void testLoadAllMessageFromQueue() throws MQException, IOException, ClassNotFoundException {
        MSGQueue queue = createTestQueue(queueName1);
        List<Message> exceptedMessages = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Message message = createTestMessage("testMessage" + i);
            messageFileManager.sendMessage(queue, message);
            exceptedMessages.add(message);
        }

        LinkedList<Message> messages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(exceptedMessages.size(), messages.size());
        for (int i = 0; i < exceptedMessages.size(); i++) {
            Message expMessage = exceptedMessages.get(i);
            Message actMessage = messages.get(i);
            System.out.println("[" + i + "] actMessage=" + actMessage);

            Assertions.assertEquals(expMessage.getMessageId(), actMessage.getMessageId());
            Assertions.assertEquals(expMessage.getRoutingKey(), actMessage.getRoutingKey());
            Assertions.assertEquals(expMessage.getDeliverMode(), actMessage.getDeliverMode());
            Assertions.assertArrayEquals(expMessage.getBody(), actMessage.getBody());
            Assertions.assertEquals(0x1, actMessage.getIsValue());
        }
    }

    @Test
    public void testDeleteMessage() throws IOException, ClassNotFoundException, MQException {
        MSGQueue queue = createTestQueue(queueName1);
        List<Message> exceptedMessages = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Message message = createTestMessage("testMessage" + i);
            messageFileManager.sendMessage(queue, message);
            exceptedMessages.add(message);
        }

        messageFileManager.deleteMessage(queue, exceptedMessages.get(7));
        messageFileManager.deleteMessage(queue, exceptedMessages.get(8));
        messageFileManager.deleteMessage(queue, exceptedMessages.get(9));

        LinkedList<Message> messages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(7, messages.size());
        for (int i = 0; i < messages.size(); i++) {
            Message expMessage = exceptedMessages.get(i);
            Message actMessage = messages.get(i);
            System.out.println("[" + i + "] actMessage=" + actMessage);

            Assertions.assertEquals(expMessage.getMessageId(), actMessage.getMessageId());
            Assertions.assertEquals(expMessage.getRoutingKey(), actMessage.getRoutingKey());
            Assertions.assertEquals(expMessage.getDeliverMode(), actMessage.getDeliverMode());
            Assertions.assertArrayEquals(expMessage.getBody(), actMessage.getBody());
            Assertions.assertEquals(0x1, actMessage.getIsValue());
        }
    }

    @Test
    public void testGC() throws IOException, ClassNotFoundException, MQException {
        MSGQueue queue = createTestQueue(queueName1);
        List<Message> exceptedMessages = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Message message = createTestMessage("testMessage" + i);
            messageFileManager.sendMessage(queue, message);
            exceptedMessages.add(message);
        }
        File beforeGCFile = new File("./data/" + queueName1 + "/queue_data.txt");
        long beforeGCLen = beforeGCFile.length();

        for (int i = 0; i < 100; i += 2) {
            messageFileManager.deleteMessage(queue, exceptedMessages.get(i));
        }

        messageFileManager.gc(queue);

        LinkedList<Message> messages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(50, messages.size());
        for (int i = 0; i < messages.size(); i++) {
            Message expMessage = exceptedMessages.get(2*i+1);
            Message actMessage = messages.get(i);
            System.out.println("[" + i + "] actMessage=" + actMessage);

            Assertions.assertEquals(expMessage.getMessageId(), actMessage.getMessageId());
            Assertions.assertEquals(expMessage.getRoutingKey(), actMessage.getRoutingKey());
            Assertions.assertEquals(expMessage.getDeliverMode(), actMessage.getDeliverMode());
            Assertions.assertArrayEquals(expMessage.getBody(), actMessage.getBody());
            Assertions.assertEquals(0x1, actMessage.getIsValue());
        }

        File afterGCFile = new File("./data/" + queueName1 + "/queue_data.txt");
        long afterGCLen = afterGCFile.length();
        System.out.println("before:" + beforeGCLen);
        System.out.println("after:" + afterGCLen);
        Assertions.assertTrue(beforeGCLen >afterGCLen);
    }

}
