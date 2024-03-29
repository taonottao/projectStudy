package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/9 9:14
 */
@Data
public class ExchangeDeleteArguments extends BasicArguments implements Serializable {
    private String exchangeName;
}
