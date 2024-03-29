package com.example.mmq.common;

import com.example.mmq.mqserver.core.ExchangeType;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/8 20:09
 */
@Data
public class ExchangeDeclareArguments extends BasicArguments implements Serializable {
    private String exchangeName;
    private ExchangeType exchangeType;
    private boolean durable;
    private boolean autoDelete;
    private Map<String, Object> arguments;
}
