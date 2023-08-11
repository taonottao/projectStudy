package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/10 12:24
 */

@Data
public class BasicAckArguments extends BasicArguments implements Serializable {
    private String queueName;
    private String messageId;
}
