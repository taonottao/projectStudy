package com.example.mmq.mqserver.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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
}
