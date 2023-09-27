package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 品牌实体类
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 22:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Brand implements Serializable {

    private Integer brandId;
    private String brandName;
    private String brandLogo;
    private String brandDesc;
    private LocalDateTime createTime;
    private Integer brandStatus;


}
