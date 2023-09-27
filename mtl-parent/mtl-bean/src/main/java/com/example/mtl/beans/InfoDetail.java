package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 评估项选项
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/21 23:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InfoDetail {
    private int infoDetailId;
    private String infoDetailName;
    private String infoDetailDesc;
}
