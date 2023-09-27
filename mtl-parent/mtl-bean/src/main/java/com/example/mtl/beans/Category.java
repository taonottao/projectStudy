package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 商品一级分类实体类
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 15:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category {

    private Integer categoryId;

    private String categoryName;

    private String categoryIcon;

    private String categoryStatus;

}
