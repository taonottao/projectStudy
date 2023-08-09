package com.example.mmq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 使用这个类来表示方法的公共参数/辅助的字段
 * 后续每个方法又会有一些不同的参数，不同的参数再分别使用不同的子类来表示
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/8 19:44
 */
@Data
public class BasicArguments implements Serializable {
    // 表示一次 请求/响应 的身份标识，可以把请求和响应对上
    protected String rid;
    // 这次通信使用的 channel 的身份标识
    protected String channelId;
}
