package com.example.mmq;

import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.Exchange;
import com.example.mmq.mqserver.core.ExchangeType;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.datacenter.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

// 加上这个注解，该类就会被识别为单元测试类
@SpringBootTest
public class DatabaseManagerTests {

    private DatabaseManager databaseManager = new DatabaseManager();

    // 接下来下面这里需要编写多个方法，每个方法都是一组/一个单元测试用例
    // 还需要做一个准备工作，还需要写两个方法，分别用于进行“准备工作”和“收尾工作”

    // 使用这个方法来执行准备工作，每个用例执行前，都要调用这个方法
    @BeforeEach
    public void setUp(){
        // 由于在 init 中，需要通过 context 对象拿到 metaMapper 实例的
        // 所以就需要把 context 对象给搞出来
        MmqApplication.context = SpringApplication.run(MmqApplication.class);
        databaseManager.init();
    }

    // 使用这个方法来执行收尾工作，每个用例执行后，都要调用这个方法
    @AfterEach
    public void tearDown(){
        // 这里要进行的操作，就是把数据库清空（把数据库文件 meta.db 直接删了就行）
        // 注意，此处不能直接删除，需要先关闭上述的 context 对象！
        // 此处的 context 对象，持有了 MetaMapper 的实例，MetaMapper 实例又打开了 meta.da 文件
        // 如果 meta.da 文件被打开了，此时删除文件操作就不会成功（Windows 系统的限制，linux 则没有这个问题）
        // 另一方面，获取 context 操作会占用 8080 端口，此处的 close 也是释放 8080 端口
        MmqApplication.context.close();
        databaseManager.deleteDB();
    }

    @Test
    public void testInitTable(){
        List<Exchange> exchangeList = databaseManager.selectAllExchanges();
        List<MSGQueue> queueList = databaseManager.selectAllQueue();
        List<Binding> bindingList = databaseManager.selectAllBinding();

        Assertions.assertEquals(1, exchangeList.size());
        Assertions.assertEquals("", exchangeList.get(0).getName());
        Assertions.assertEquals(ExchangeType.DIRECT, exchangeList.get(0).getType());
        Assertions.assertEquals(0, queueList.size());
        Assertions.assertEquals(0, bindingList.size());
    }

    private Exchange createExchange(String exchangeName) {
        Exchange exchange = new Exchange();
        exchange.setName(exchangeName);
        exchange.setType(ExchangeType.FANOUT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        exchange.setArguments("aaa", 1);
        exchange.setArguments("bbb", 2);

        return exchange;
    }

    @Test
    public void testInsertExchange(){
        Exchange exchange = createExchange("testExchange");
        databaseManager.insertExchange(exchange);
        List<Exchange> exchangeList = databaseManager.selectAllExchanges();
        Assertions.assertEquals(2, exchangeList.size());
        Exchange newExchange = exchangeList.get(1);
        Assertions.assertEquals("testExchange", newExchange.getName());
        Assertions.assertEquals(ExchangeType.FANOUT, newExchange.getType());
        Assertions.assertEquals(false, newExchange.isAutoDelete());
        Assertions.assertEquals(true, newExchange.isDurable());
        Assertions.assertEquals(1, newExchange.getArguments("aaa"));
        Assertions.assertEquals(2, newExchange.getArguments("bbb"));
    }

    @Test
    public void testDeleteExchange(){
        Exchange exchange = createExchange("testExchange");
        databaseManager.insertExchange(exchange);
        List<Exchange> exchangeList = databaseManager.selectAllExchanges();
        Assertions.assertEquals(2, exchangeList.size());
        Exchange newExchange = exchangeList.get(1);
        Assertions.assertEquals("testExchange", newExchange.getName());

        databaseManager.deleteExchange("testExchange");
        List<Exchange> exchangeList1 = databaseManager.selectAllExchanges();
        Assertions.assertEquals(1, exchangeList1.size());
        Assertions.assertEquals("", exchangeList1.get(0).getName());
    }

    private MSGQueue createQueue(String queueName) {
        MSGQueue queue = new MSGQueue();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        queue.setArguments("aaa",1);
        queue.setArguments("bbb",2);
        return queue;
    }

    @Test
    public void testInsertQueue(){
        MSGQueue queue = createQueue("testQueue");
        databaseManager.insertQueue(queue);

        List<MSGQueue> queueList = databaseManager.selectAllQueue();

        Assertions.assertEquals(1, queueList.size());
        MSGQueue newQueue = queueList.get(0);
        Assertions.assertEquals("testQueue", newQueue.getName());
        Assertions.assertEquals(true, newQueue.isDurable());
        Assertions.assertEquals(false, newQueue.isAutoDelete());
        Assertions.assertEquals(false, newQueue.isExclusive());
        Assertions.assertEquals(1, newQueue.getArguments("aaa"));
        Assertions.assertEquals(2, newQueue.getArguments("bbb"));
    }

    @Test
    public void testDeleteQueue(){
        MSGQueue queue = createQueue("testQueue");
        databaseManager.insertQueue(queue);

        List<MSGQueue> queueList = databaseManager.selectAllQueue();

        Assertions.assertEquals(1, queueList.size());

        databaseManager.deleteQueue("testQueue");
        queueList = databaseManager.selectAllQueue();
        Assertions.assertEquals(0, queueList.size());

    }

    private Binding createBinding(String exchangeName, String queueName) {
        Binding binding = new Binding();
        binding.setExchangeName(exchangeName);
        binding.setQueueName(queueName);
        binding.setBindingKey("testBindingKey");
        return binding;
    }

    @Test
    public void testInsertBinding(){
        Binding binding = createBinding("testExchange", "testQueue");
        databaseManager.insertBinding(binding);

        List<Binding> bindingList = databaseManager.selectAllBinding();
        Assertions.assertEquals(1, bindingList.size());
        Assertions.assertEquals("testExchange", bindingList.get(0).getExchangeName());
        Assertions.assertEquals("testQueue", bindingList.get(0).getQueueName());
        Assertions.assertEquals("testBindingKey", bindingList.get(0).getBindingKey());
    }

    @Test
    public void testDeleteBinding(){
        Binding binding = createBinding("testExchange", "testQueue");
        databaseManager.insertBinding(binding);

        List<Binding> bindingList = databaseManager.selectAllBinding();
        Assertions.assertEquals(1, bindingList.size());
        databaseManager.deleteBinding(binding);
        bindingList = databaseManager.selectAllBinding();
        Assertions.assertEquals(0, bindingList.size());
    }

}
