package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 商品评估项（评估类目）
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/21 23:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicInfo {
    private int basicInfoId;
    private String basicInfoName;
    private int basicInfoStatus;
    private int basicInfoStep;
    private List<InfoDetail> infoDetailList;
}
