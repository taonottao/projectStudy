package com.example.mmq;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.*;
import com.example.mmq.mqserver.datacenter.DiskDataCenter;
import com.example.mmq.mqserver.datacenter.MemoryDataCenter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
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

    @Test
    public void testRecovery() throws MQException, IOException, ClassNotFoundException {
        // 由于后续需要进行数据库操作，依赖 MyBatis，就需要先启动 SpringApplication。这样才能进行后续的数据库操作
        MmqApplication.context = SpringApplication.run(MmqApplication.class);

        // 1. 在硬盘上构造好数据
        DiskDataCenter diskDataCenter = new DiskDataCenter();
        diskDataCenter.init();

        Exchange expExchange = createTestExchange("testExchange");
        diskDataCenter.insertExchange(expExchange);

        MSGQueue expQueue = createTestQueue("testQueue");
        diskDataCenter.insertQueue(expQueue);

        Binding expBinding = new Binding();
        expBinding.setBindingKey("testBindingKey");
        expBinding.setQueueName("testQueue");
        expBinding.setExchangeName("testExchange");
        diskDataCenter.insertBinding(expBinding);

        Message expMessage = createTestMessage("testMessage");
        diskDataCenter.sendMessage(expQueue, expMessage);

        // 2. 进行恢复操作
        memoryDataCenter.recovery(diskDataCenter);

        // 3. 对比结果
        Exchange actExchange = memoryDataCenter.getExchange("testExchange");
        Assertions.assertEquals(expExchange.getName(), actExchange.getName());
        Assertions.assertEquals(expExchange.isAutoDelete(), actExchange.isAutoDelete());
        Assertions.assertEquals(expExchange.getType(), actExchange.getType());
        Assertions.assertEquals(expExchange.isDurable(), actExchange.isDurable());

        MSGQueue actQueue = memoryDataCenter.getQueue("testQueue");
        Assertions.assertEquals(expQueue.getName(), actQueue.getName());
        Assertions.assertEquals(expQueue.isAutoDelete(), actQueue.isAutoDelete());
        Assertions.assertEquals(expQueue.isDurable(), actQueue.isDurable());
        Assertions.assertEquals(expQueue.isExclusive(), actQueue.isExclusive());

        Binding actBinding = memoryDataCenter.getBinding("testExchange", "testQueue");
        Assertions.assertEquals(expBinding.getQueueName(), actBinding.getQueueName());
        Assertions.assertEquals(expBinding.getBindingKey(), actBinding.getBindingKey());
        Assertions.assertEquals(expBinding.getExchangeName(), actBinding.getExchangeName());

        Message actMessage = memoryDataCenter.pollMessage("testQueue");
        Assertions.assertEquals(expMessage.getMessageId(), actMessage.getMessageId());
        Assertions.assertEquals(expMessage.getRoutingKey(), actMessage.getRoutingKey());
        Assertions.assertEquals(expMessage.getDeliverMode(), actMessage.getDeliverMode());
        Assertions.assertArrayEquals(expMessage.getBody(), actMessage.getBody());

        // 4. 清理硬盘数据，把整个 data 目录里的内容都删掉
        MmqApplication.context.close();
        File dataDir = new File("./data");
        FileUtils.deleteDirectory(dataDir);
    }

}
