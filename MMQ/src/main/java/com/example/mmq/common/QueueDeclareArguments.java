package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/9 9:16
 */
@Data
public class QueueDeclareArguments extends BasicArguments implements Serializable {
    private String queueName;
    private boolean durable;
    private boolean exclusive;
    private boolean autoDelete;
    private Map<String, Object> arguments;
}
