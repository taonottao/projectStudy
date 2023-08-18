package com.example.demo.entity;

import lombok.Data;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/16 15:48
 */
@Data
public class Comment {
    private int id;
    private String username;
    private int pid;
    private String content;
    private int likeCount;
    private String publishTime;
}
