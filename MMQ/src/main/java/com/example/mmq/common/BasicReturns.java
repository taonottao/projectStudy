package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 这个类表示各个远程调用的方法的返回值的公共信息
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/8 19:50
 */
@Data
public class BasicReturns implements Serializable {
    // 用来表示唯一的请求和响应
    protected String rid;
    // 用来表示一个 channel
    protected String channelId;
    // 表示当前这个远程调用方法的返回值
    protected boolean ok;
}
