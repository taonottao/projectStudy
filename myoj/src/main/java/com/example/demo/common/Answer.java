package com.example.demo.common;

import lombok.Data;

/**
 * 编译运行的返回结果
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/28 11:51
 */
@Data
public class Answer {
    // error 为0，没问题， 1表示编译错误，2表示运行错误
    private int error;
    private String reason;
    private String stdout;
    private String stderr;
}
