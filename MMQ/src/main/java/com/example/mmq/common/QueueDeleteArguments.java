package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/9 9:18
 */
@Data
public class QueueDeleteArguments extends BasicArguments implements Serializable {
    private String queueName;
}
