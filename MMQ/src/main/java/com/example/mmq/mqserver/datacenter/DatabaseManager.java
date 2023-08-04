package com.example.mmq.mqserver.datacenter;

import com.example.mmq.MmqApplication;
import com.example.mmq.mqserver.core.Binding;
import com.example.mmq.mqserver.core.Exchange;
import com.example.mmq.mqserver.core.ExchangeType;
import com.example.mmq.mqserver.core.MSGQueue;
import com.example.mmq.mqserver.mapper.MetaMapper;

import java.io.File;
import java.util.List;

/**
 * 通过这个类来整合上述的数据库操作
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/31 19:58
 */

public class DatabaseManager {
    private MetaMapper metaMapper;

    // 针对数据库进行初始化 = 建库建表 + 插入一些默认数据
    public void init() {

        metaMapper = MmqApplication.context.getBean(MetaMapper.class);

        if (!checkDBExists()) {
            // 先创建 data 目录
            File dataDir = new File("./data");
            dataDir.mkdirs();

            createTable();

            createDefaultData();

            System.out.println("[DatabaseManager] 数据库初始化完成！");
        } else {
            System.out.println("[DatabaseManager] 数据库已经存在！");
        }
    }

    public void deleteDB() {
        File file = new File("./data/meta.db");
        boolean ret = file.delete();
        if (ret) {
            System.out.println("[DatabaseManager] 删除数据库文件成功！");
        } else {
            System.out.println("[DatabaseManager] 删除数据库文件失败！");
        }

        File dataDir = new File("./data");
        // delete() 删除目录必须保证目录是空的
        ret = dataDir.delete();
        if (ret) {
            System.out.println("[DatabaseManager] 删除数据库目录成功！");
        } else {
            System.out.println("[DatabaseManager] 删除数据库目录失败！");
        }
    }

    private boolean checkDBExists() {
        File file = new File("./data/meta.db");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void createTable() {
        metaMapper.createExchangeTable();
        metaMapper.createQueueTable();
        metaMapper.createBindingTable();
        System.out.println("[DatabaseManager] 创建表完成！");
    }

    /**
     * 此处要添加的默认数据主要是添加一个默认的交换机
     * RabbitMQ 有一个这样的设定：带有一个匿名的交换机，类型是 DIRECT
     */
    private void createDefaultData() {
        Exchange exchange = new Exchange();
        exchange.setName("");
        exchange.setType(ExchangeType.DIRECT);
        exchange.setDurable(true);
        exchange.setAutoDelete(false);
        metaMapper.insertExchange(exchange);
        System.out.println("[DatabaseManager] 创建初始数据完成！");
    }

    public void insertExchange(Exchange exchange) {
        metaMapper.insertExchange(exchange);
    }

    public List<Exchange> selectAllExchanges() {
        return metaMapper.selectAllExchange();
    }

    public void deleteExchange(String exchangeName) {
        metaMapper.deleteExchange(exchangeName);
    }

    public void insertQueue(MSGQueue queue) {
        metaMapper.insertQueue(queue);
    }

    public List<MSGQueue> selectAllQueue() {
        return metaMapper.selectAllQueue();
    }

    public void deleteQueue(String queueName) {
        metaMapper.deleteQueue(queueName);
    }

    public void insertBinding(Binding binding) {
        metaMapper.insertBinding(binding);
    }

    public List<Binding> selectAllBinding() {
        return metaMapper.selectAllBinding();
    }

    public void deleteBinding(Binding binding) {
        metaMapper.deleteBinding(binding);
    }

}
