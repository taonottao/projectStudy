package com.example.mmq;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.*;
import com.example.mmq.mqserver.datacenter.MemoryDataCenter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/4 12:50
 */
@SpringBootTest
public class MemoryDataCenterTests {

    private MemoryDataCenter memoryDataCenter = null;

    @BeforeEach
    public void setUp(){
        memoryDataCenter = new MemoryDataCenter();
    }

    @AfterEach
    public void tearDown(){
        memoryDataCenter = null;
    }

    private Exchange createTestExchange(String exchangeName) {
        Exchange exchange = new Exchange();
        exchange.setName(exchangeName);
        exchange.setType(ExchangeType.DIRECT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        return exchange;
    }

    private MSGQueue createTestQueue(String queueName) {
        MSGQueue queue = new MSGQueue();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setExclusive(false);
        queue.setAutoDelete(false);
        return queue;
    }

    @Test
    public void testExchange() {
        Exchange excExchange = createTestExchange("testExchange");
        memoryDataCenter.insertExchange(excExchange);
        Exchange actExchange = memoryDataCenter.getExchange("testExchange");
        Assertions.assertEquals(excExchange, actExchange);

        memoryDataCenter.deleteExchange("testExchange");
        actExchange = memoryDataCenter.getExchange("testExchange");
        Assertions.assertNull(actExchange);
    }

    @Test
    public void testQueue(){
        MSGQueue excQueue = createTestQueue("testQueue");
        memoryDataCenter.insertQueue(excQueue);
        MSGQueue actQueue = memoryDataCenter.getQueue("testQueue");
        Assertions.assertEquals(excQueue, actQueue);

        memoryDataCenter.deleteQueue("testQueue");
        actQueue = memoryDataCenter.getQueue("testQueue");
        Assertions.assertNull(actQueue);
    }

    @Test
    public void testBinding() throws MQException {
        Binding excBinding = new Binding();
        excBinding.setExchangeName("testExchange");
        excBinding.setQueueName("testQueue");
        excBinding.setBindingKey("testKey");
        memoryDataCenter.insertBinding(excBinding);
        Binding actBinding = memoryDataCenter.getBinding("testExchange", "testQueue");
        Assertions.assertEquals(excBinding, actBinding);

        ConcurrentHashMap<String, Binding> bindingMap = memoryDataCenter.getBindings("testExchange");
        Assertions.assertEquals(1, bindingMap.size());
        Assertions.assertEquals(excBinding, bindingMap.get("testQueue"));

        memoryDataCenter.deleteBinding(excBinding);
        actBinding = memoryDataCenter.getBinding("testExchange", "testQueue");
        Assertions.assertNull(actBinding);
    }

    private Message createTestMessage(String content) {
        Message message = Message.createMessageWithId("testRoutingKey", null, content.getBytes());
        return message;
    }

    @Test
    public void testMessaeg() {
        Message excMessage = createTestMessage("testMessage");
        memoryDataCenter.addMessage(excMessage);
        Message actMessage = memoryDataCenter.getMessage(excMessage.getMessageId());
        Assertions.assertEquals(excMessage, actMessage);
        memoryDataCenter.removeMessage(excMessage.getMessageId());
        actMessage = memoryDataCenter.getMessage(excMessage.getMessageId());
        Assertions.assertNull(actMessage);

    }

    @Test
    public void testSendMessage() {
        MSGQueue queue = createTestQueue("testQueue");
        List<Message> excMessages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = createTestMessage("testMessage" + i);
            memoryDataCenter.senMessage(queue, message);
            excMessages.add(message);
        }

        List<Message> actMessages = new ArrayList<>();
        while (true) {
            Message message = memoryDataCenter.pollMessage("testQueue");
            if (message == null) {
                break;
            }
            actMessages.add(message);
        }

        Assertions.assertEquals(excMessages.size(), actMessages.size());
        for (int i = 0; i < excMessages.size(); i++) {
            Assertions.assertEquals(excMessages.get(i), actMessages.get(i));
        }
    }

    @Test
    public void testMessageWaitAck(){
        Message expMessage = createTestMessage("expMessage");
        memoryDataCenter.addMessageWaitAck("testQueue", expMessage);

        Message actMessage = memoryDataCenter.getMessageWaitAck("testQueue", expMessage.getMessageId());
        Assertions.assertEquals(expMessage, actMessage);

        memoryDataCenter.removeMessageWaitAck("testQueue", expMessage.getMessageId());
        actMessage = memoryDataCenter.getMessageWaitAck("testQueue", expMessage.getMessageId());

        Assertions.assertNull(actMessage);
    }

}
