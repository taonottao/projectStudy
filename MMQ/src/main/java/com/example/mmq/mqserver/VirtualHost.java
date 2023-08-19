package com.example.mmq.mqserver;

import com.example.mmq.common.Consumer;
import com.example.mmq.common.MQException;
import com.example.mmq.mqserver.core.*;
import com.example.mmq.mqserver.datacenter.DiskDataCenter;
import com.example.mmq.mqserver.datacenter.MemoryDataCenter;
import org.apache.coyote.OutputBuffer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private ConsumerManager consumerManager = new ConsumerManager(this);

    // 操作交换机的锁对象
    private final Object exchangeLocker = new Object();
    private final Object queueLocker = new Object();

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
            synchronized (exchangeLocker) {
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
            }
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
            synchronized (exchangeLocker) {
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
            }
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
            synchronized (queueLocker) {
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
            }
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
            synchronized (queueLocker) {
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
            }
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
            synchronized (exchangeLocker) {
                synchronized (queueLocker) {
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
                }
            }
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
            synchronized (exchangeLocker) {
                synchronized (queueLocker) {
                    // 1. 获取绑定，看是否存在
                    Binding binding = memoryDataCenter.getBinding(exchangeName, queueName);
                    if (binding == null) {
                        throw new MQException("[VirtualHost] 删除绑定失败！绑定不存在！exchangeName=" + exchangeName + ", queueName=" + queueName);
                    }
                    diskDataCenter.deleteBinding(binding);
                    memoryDataCenter.deleteBinding(binding);
                    System.out.println("[VirtualHost] 删除绑定成功！");
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 删除绑定失败！");
            e.printStackTrace();
            return false;
        }
    }

    public boolean basicPublish(String exchangeName, String routingKey, BasicProperties basicProperties, byte[] body) {
        try {
            // 1. 转换交换机的名字
            exchangeName = virtualHostName + exchangeName;
            // 2. 检查 routingKey 是否合法
            if (!router.checkRoutingKey(routingKey)) {
                throw new MQException("[VirtualHost] routingKey 非法！routingKey=" + routingKey);
            }
            // 3. 查找交换机对象
            Exchange exchange = memoryDataCenter.getExchange(exchangeName);
            if (exchange == null) {
                throw new MQException("[VirtualHost] 交换机不存在！exchangeName=" + exchangeName);
            }
            // 4. 判定交换机的类型
            if (exchange.getType() == ExchangeType.DIRECT) {
                // 按照直接交换机的方式来转发消息
                // 以 routingKey 作为队列的名字，直接把消息写入指定队列中
                // 此时，可以无视绑定关系
                String queueName = virtualHostName + routingKey;
                // 5. 构造消息对象
                Message message = Message.createMessageWithId(routingKey, basicProperties, body);
                // 6. 查找给队列名对应的对象
                MSGQueue queue = memoryDataCenter.getQueue(queueName);
                if (queue == null) {
                    throw new MQException("[VirtualHost] 队列不存在！queueName=" + queueName);
                }
                // 7. 队列存在，直接给队列中写入消息
                sendMessage(queue, message);
            } else {
                // 按照 fanout 和 topic 的方式来转发消息
                // 5. 找到该交换机关联的所有绑定，并遍历这些绑定对象
                ConcurrentHashMap<String, Binding> bindingsMap = memoryDataCenter.getBindings(exchangeName);
                for (Map.Entry<String, Binding> entry : bindingsMap.entrySet()) {
                    // 1) 获取到绑定对象，判断对应的队列是否存在
                    Binding binding = entry.getValue();
                    MSGQueue queue = memoryDataCenter.getQueue(binding.getQueueName());
                    if (queue == null) {
                        // 此处就不抛出异常了，可能有很多个这样的队列
                        // 我们不希望因为一个队列的失败，影响到其他队列的消息的传输
                        System.out.println("[VirtualHost] basicPublish 发送消息时，发现队列不存在！queueName=" + binding.getQueueName());
                        continue;
                    }
                    // 2) 构造消息对象
                    Message message = Message.createMessageWithId(routingKey, basicProperties, body);
                    // 3) 判断这个消息是否能转发给该队列
                    //    如果是 fanout，所有绑定的队列都要转发的
                    //    如果是 topic，还需要判定一下 bindingKey 和 routingKey 是否匹配
                    if (!router.route(exchange.getType(), binding, message)) {
                        continue;
                    }
                    // 4) 真正转发消息给队列
                    sendMessage(queue, message);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] 消息发送失败！");
            e.printStackTrace();
            return false;
        }
    }

    private void sendMessage(MSGQueue queue, Message message) throws MQException, IOException, InterruptedException {
        // 此处发送消息，就是把消息写入到硬盘 和 内存上
        int deliverMode = message.getDeliverMode();
        // deliverMode 为 1 表示不持久化，为 2 表示持久化
        if (deliverMode == 2) {
            diskDataCenter.sendMessage(queue, message);
        }
        // 写入内存
        memoryDataCenter.sendMessage(queue, message);

        // 此处还需要补充一个逻辑，通知消费者可以消费消息了。
        consumerManager.notifyConsume(queue.getName());
    }

    /**
     *
     * @param consumerTag 消费者的身份标识
     * @param queueName
     * @param autoAck 消息被消费完后，应答的方式，为 true 自动应答，为 false 手动应答
     * @param consumer 是一个回调函数。此处类型设定为函数式接口，这样后续调用 basicConsume 并且传实参的时候，就可以使用 lambda
     * @return
     */
    public boolean basicConsume(String consumerTag, String queueName, boolean autoAck, Consumer consumer) {
        // 构造一个 ConsumerEnv 对象，把这个对应的队列找到，再把这个 Consumer 对象添加到该队列中
        queueName = virtualHostName + queueName;
        try {
            consumerManager.addConsumer(consumerTag, queueName, autoAck, consumer);
            System.out.println("[VirtualHost] basicConsume 成功！queueName=" + queueName);
            return true;
        } catch (Exception e) {
            System.out.println("[VirtualHost] basicConsume 失败！queueName=" + queueName);
            e.printStackTrace();
            return false;
        }
    }

    public boolean basicAck(String queueName, String messageId) {
        queueName = virtualHostName + queueName;
        try {
            Message message = memoryDataCenter.getMessage(messageId);
            if (message == null) {
                throw new MQException("[VirtualHost] 要确认的消息不存在！messageId=" + messageId);
            }
            MSGQueue queue = memoryDataCenter.getQueue(queueName);
            if (queue == null) {
                throw new MQException("[VirtualHost] 要确认的队列不存在！queueName=" + queueName);
            }

            if (message.getDeliverMode() == 2) {
                diskDataCenter.deleteMessage(queue,message);
            }
            memoryDataCenter.removeMessage(messageId);
            memoryDataCenter.removeMessageWaitAck(queueName,messageId);
            System.out.println("[VirtualHost] basicAck 成功！queueName=" + queueName + ", messageId=" + messageId);
            return true;

        } catch (Exception e) {
            System.out.println("[VirtualHost] basicAck 失败！queueName=" + queueName + ", messageId=" + messageId);
            e.printStackTrace();
            return false;
        }
    }

}
