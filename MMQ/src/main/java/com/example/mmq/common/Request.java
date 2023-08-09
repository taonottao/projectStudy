package com.example.mmq.common;

import lombok.Data;

/**
 * 表示一个网络通信的请求对象，按照自定义协议的格式来展开的
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/8 19:29
 */
@Data
public class Request {
    private int type;
    private int length;
    private byte[] payload;
}
