package com.example.demo.entity;

import lombok.Data;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 18:30
 */
@Data
public class Problem {
    private int id;
    private String title;
    private String level;
    private String description;
    private String templateCode;
    private String testCode;
}
