package com.example.demo.common;

import lombok.Data;

/**
 * 表示一个 task 的执行结果
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 13:50
 */
@Data
public class Answer {
    // 错误码，约定 error 为0表示编译运行正常，为1表示编译出错，为2便是运行出错（抛异常）
    private int error;
    // 出错的提示信息，error为1，编译出错，reason就存放编译出错的信息；error为2，运行出错，reason就存放运行出错的信息
    private String reason;
    // 运行程序得到的标准输出结果
    private String stdout;
    // 运行程序得到的标准错误结果
    private String stderr;
}
