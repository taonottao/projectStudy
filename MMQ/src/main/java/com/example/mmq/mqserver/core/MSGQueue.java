package com.example.mmq.mqserver.core;

import com.example.mmq.common.ConsumerEnv;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这个类表示一个存储消息的队列
 * MSG => message
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/30 17:30
 */
@Data
public class MSGQueue {
    // 表示队列的身份标识
    private String name;
    // 标识队列是否持久化
    private boolean durable = false;
    // 这个属性如果为 true，表示这个队列只能被一个消费者使用
    // 这个 独占 功能，也是先列出来，但是后续我们并不实现
    private boolean exclusive = false;
    // 如果当前交换机没人使用了，就会自动删除
    private boolean autoDelete = false;
    // arguments 表示扩展参数，后续代码也是没有真正实现。
    private Map<String, Object> arguments = new HashMap<>();
    // 当前队列都有哪些消费者订阅
    private List<ConsumerEnv> consumerEnvList = new ArrayList<>();
    // 记录当前取到了第几个消费者，方便实现轮询策略
    private AtomicInteger consumerSeq = new AtomicInteger(0);

    // 添加一个新的订阅者
    public void addConsumerEnv(ConsumerEnv consumerEnv) {
            consumerEnvList.add(consumerEnv);
    }

    // 订阅者的删除暂时先不考虑
    // 挑选一个订阅者用来处理当前的消息（按照轮询的方式）
    public ConsumerEnv chooseConsumer() {
        if (consumerEnvList.size() == 0) {
            // 该队列没有人订阅
            return null;
        }
        // 计算一下当前要取的元素的下标
        int index = consumerSeq.get() % consumerEnvList.size();
        consumerSeq.getAndIncrement();
        return consumerEnvList.get(index);
    }

    public String getArguments(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public void setArguments(String argumentsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.arguments = objectMapper.readValue(argumentsJson, new TypeReference<HashMap<String,Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Object getArguments(String key){
        return arguments.get(key);
    }

    public void setArguments(String key, Object value) {
        arguments.put(key, value);
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
