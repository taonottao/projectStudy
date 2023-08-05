package com.example.mmq.mqserver;

import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.*;
import com.example.mmq.mqserver.datacenter.DiskDataCenter;
import com.example.mmq.mqserver.datacenter.MemoryDataCenter;

import java.io.IOException;
import java.util.Map;

/**
 * 通过这个类来表示虚拟主机
 * 每个虚拟主机下面都管理着自己的交换机。队列、绑定、消息数据
 * 同时提供 api 共上层使用
 * 针对VirtualHost 这个类，作为业务逻辑的整合者，就需要对代码中抛出的异常进行处理了。
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/5 10:02
 */
public class VirtualHost {

    private String virtualHostName;
    private MemoryDataCenter memoryDataCenter = new MemoryDataCenter();
    private DiskDataCenter diskDataCenter = new DiskDataCenter();
    private Router router = new Router();

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public MemoryDataCenter getMemoryDataCenter() {
        return memoryDataCenter;
    }

    public DiskDataCenter getDiskDataCenter() {
        return diskDataCenter;
    }

    public VirtualHost(String virtualHostName) {
        this.virtualHostName = virtualHostName;

        // 对于 MemoryDataCenter 来说，不需要额外的初始化操作。只要对象 new 出来就行
        // 但是对于 DiskDataCenter 来说，则需要进行初始化操作，建库建表和初始数据的设定
        // 另外还需要针对硬盘的数据，进行恢复到内存中
        diskDataCenter.init();

        try {
            memoryDataCenter.recovery(diskDataCenter);
        } catch (MQException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println("[VirtualHost] 恢复内存数据失败！");
        }

    }

    // 创建交换机
    // 如果交换机不存在，就创建，存在就返回
    // 创建成功返回 true，否则返回 false
    public boolean exchangeDeclare(String exchangeName, ExchangeType exchangeType, boolean durable, boolean autoDelete,
                                   Map<String, Object> arguments){
        // 把交换机的名字，加上虚拟主机作为前缀
        exchangeName = virtualHostName + exchangeName;
        try {
            // 1. 判断该交换机是否已经存在，直接通过内存查询
            Exchange existsExchange = memoryDataCenter.getExchange(exchangeName);
            if (existsExchange != null) {
                // 该交换机已经存在
                System.out.println("[VirtualHost] 交换机已经存在！exchangeName=" + exchangeName);
                return true;
            }
            // 2. 真正创建交换机
            Exchange exchange = new Exchange();
            exchange.setName(exchangeName);
            exchange.setType(exchangeType);
            exchange.setDurable(durable);
            exchange.setArguments(arguments);
            // 3. 把交换机对象写入硬盘
            if (durable) {
                diskDataCenter.insertExchange(exchange);
            }
            // 5. 把交换机对象写入内存
            memoryDataCenter.insertExchange(exchange);
            System.out.println("[VirtualHost] 交换机创建完成！exchangeName=" + exchangeName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 交换机创建失败！exchangeName=" + exchangeName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean exchangeDelete(String exchangeName) {
        exchangeName = virtualHostName + exchangeName;
        try {
            // 1. 先找到对应的交换机
            Exchange toDelete = memoryDataCenter.getExchange(exchangeName);
            if (toDelete == null) {
                throw new MQException("[virtualHost] 交换机不存在，无法删除！");
            }
            // 2. 删除硬盘上的数据
            if (toDelete.isDurable()) {
                diskDataCenter.deleteExchange(exchangeName);
            }
            // 3. 删除内存中的交换机数据
            memoryDataCenter.deleteExchange(exchangeName);
            System.out.println("[VirtualHost] 交换机删除成功！exchangeName=" + exchangeName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 交换机删除失败！exchangeName=" + exchangeName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete,
                                Map<String, Object> arguments) {
        queueName = virtualHostName + queueName;
        try {
            // 1. 判断队列是否存在
            MSGQueue existsQueue = memoryDataCenter.getQueue(queueName);
            if (existsQueue != null) {
                System.out.println("[VirtualHost] 队列已经存在！queueName=" + queueName);
                return true;
            }
            // 2. 创建队列对象
            MSGQueue queue = new MSGQueue();
            queue.setName(queueName);
            queue.setDurable(durable);
            queue.setExclusive(exclusive);
            queue.setAutoDelete(autoDelete);
            queue.setArguments(arguments);
            // 3. 写进硬盘
            if (durable) {
                diskDataCenter.insertQueue(queue);
            }
            // 4. 写进内存
            memoryDataCenter.insertQueue(queue);
            System.out.println("[VirtualHost] 队列创建成功！queueName=" + queueName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 队列创建失败！queueName=" + queueName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean queueDelete(String queueName) {
        queueName = virtualHostName + queueName;
        try {
            // 1. 根据队列名字，查询队列对象
            MSGQueue queue = memoryDataCenter.getQueue(queueName);
            if (queue == null) {
                throw new MQException("[VirtualHost] 队列不存在！queueName=" + queueName);
            }
            // 2. 删除硬盘数据
            if (queue.isDurable()) {
                diskDataCenter.deleteQueue(queueName);
            }
            // 3. 删除内存数据
            memoryDataCenter.deleteQueue(queueName);
            System.out.println("[VirtualHost] 队列删除成功！queueName=" + queueName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 队列删除失败！queueName=" + queueName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean queueBind(String queueName, String exchangeName, String bindingKey) {
        queueName = virtualHostName + queueName;
        exchangeName = virtualHostName + exchangeName;
        try {
            // 1. 判断当前绑定是否已经存在
            Binding existsBinding = memoryDataCenter.getBinding(exchangeName, queueName);
            if (existsBinding != null) {
                throw new MQException("[VirtualHost] binding 已经存在！queueName=" + queueName + ", exchangeName=" + exchangeName);
            }
            // 2. 验证 bindingKey 是否合法
            if(!router.checkBindingKey(bindingKey)){
                throw new MQException("[VirtualHost] 非法！bindingKey=" + bindingKey);
            }
            // 3. 创建 Binding 对象
            Binding binding = new Binding();
            binding.setExchangeName(exchangeName);
            binding.setQueueName(queueName);
            binding.setBindingKey(bindingKey);
            // 4. 获取一下对应的交换机和队列。如果交换机或者队列不存在，这样的绑定也是无法创建的。
            MSGQueue queue = memoryDataCenter.getQueue(queueName);
            if (queue == null) {
                throw new MQException("[VirtualHost] 队列不存在！queueName=" + queueName);
            }
            Exchange exchange = memoryDataCenter.getExchange(exchangeName);
            if (exchange == null) {
                throw new MQException("[VirtualHost] 交换机不存在！exchangeName=" + exchangeName);
            }
            if (exchange.isDurable() && queue.isDurable()) {
                diskDataCenter.insertBinding(binding);
            }
            memoryDataCenter.insertBinding(binding);
            System.out.println("[VirtualHost] 绑定创建成功！exchangeName=" + exchangeName + ", queueName=" + queueName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 绑定创建失败！exchangeName=" + exchangeName + ", queueName=" + queueName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean queueUnbind(String queueName, String exchangeName) {
        queueName = virtualHostName + queueName;
        exchangeName = virtualHostName + exchangeName;
        try {
            // 1. 获取绑定，看是否存在
            Binding binding = memoryDataCenter.getBinding(exchangeName, queueName);
            if (binding == null) {
                throw new MQException("[VirtualHost] 删除绑定失败！绑定不存在！exchangeName=" + exchangeName + ", queueName=" + queueName);
            }
            diskDataCenter.deleteBinding(binding);
            memoryDataCenter.deleteBinding(binding);
            System.out.println("[VirtualHost] 删除绑定成功！");
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 删除绑定失败！");
            e.printStackTrace();
            return false;
        }
    }

}
