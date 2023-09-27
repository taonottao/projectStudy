package com.example.mtl.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 分页信息
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/20 18:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageInfo<T> {
    private int pageNum;
    private int pageCount;
    private int count;
    private int prePage;
    private int nextPage;
    private List<T> list;

    public static final int PAGE_SIZE=8;
}
