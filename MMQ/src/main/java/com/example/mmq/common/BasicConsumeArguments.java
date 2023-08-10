package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/9 9:29
 */
@Data
public class BasicConsumeArguments extends BasicArguments implements Serializable {
    private String consumerTag;
}

