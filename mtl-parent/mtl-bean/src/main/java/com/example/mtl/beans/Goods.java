package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.PriorityQueue;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/20 16:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Goods {
    private Integer goodId;
    private String goodName;
    private String goodImg;
    private Integer goodCost;
    private Integer goodMinPrice;
//    private Integer good_first_price;
//    private Integer good_second_price;
//    private Integer good_third_price;
//    private Integer good_forth_price;
//    private Integer good_fifth_price;

}
