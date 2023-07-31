package com.example.mmq.mqserver.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 表示一个交换机
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/30 17:30
 */
@Data
public class Exchange {
    // 此处使用 name 来作为交换机的身份标识。（唯一的）
    private String name;
    // 交换机类型，DIRECT  FANOUT  TOPIC
    private ExchangeType type = ExchangeType.DIRECT;
    // 该交换机是否要持久化存储，true 表示需要， false 表示不需要
    private boolean durable = false;
    // 如果当前交换机没人使用了，就会自动删除
    // 这个属性暂时放在这里，在后续的代码中并没有真的实现这个自动删除功能，属于锦上添花
    private boolean autoDelete = false;
    // arguments 表示的是创建交换机时指定一些额外的参数选项，后续代码也是没有真正实现。
    // 为了把这个 arguments 存到数据库中，需要把 Map 转成 json 格式的字符串
    private Map<String, Object> arguments = new HashMap<>();

    public String getArguments(){
        // 是把当前的 arguments 参数，从 Map 转成 String(json)
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 如果真的发生异常，就返回空的 json 字符串
        return "{}";
    }

    // 这个方法，是从数据库读数据之后，构造 Exchange 对象，会自动调用到
    public void setArguments(String argumentsJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        // 把参数中的 argumentsJson 按照 json 格式解析
        // 转成上述的 Map 对象
        try {
            this.arguments = objectMapper.readValue(argumentsJson, new TypeReference<HashMap<String,Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
