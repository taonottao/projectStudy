package com.example.mmq.common;

import lombok.Data;

/**
 * 这个对象表示一个响应，根据自定义应用层协议来的
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/8 19:32
 */
@Data
public class Response {
    private int type;
    private int length;
    private byte[] payload;
}
