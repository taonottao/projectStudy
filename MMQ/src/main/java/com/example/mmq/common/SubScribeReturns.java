package com.example.mmq.common;

import com.example.mmq.mqserver.core.BasicProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/10 12:28
 */
@Data
public class SubScribeReturns extends BasicReturns implements Serializable {
    private String consumerTag;
    private BasicProperties basicProperties;
    private byte[] body;
}
