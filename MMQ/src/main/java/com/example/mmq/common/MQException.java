package com.example.mmq.common;

/**
 * 自定义异常类，如果是我们的 mq 的业务逻辑中出现了异常，
 * 就抛出这个异常，同时在构造方法中指定出现异常的原因。
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/2 12:30
 */
public class MQException extends Exception {
    public MQException(String reason) {
        super(reason);
    }
}
