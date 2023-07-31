package com.example.mmq.mqserver.core;

import lombok.Data;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/30 17:35
 */
public enum ExchangeType {
    DIRECT(0),
    FANOUT(1),
    TOPIC(2);

    private final int type;

    private ExchangeType(int type) {
        this.type = type;
    }

    public int getType(){
        return type;
    }

}
