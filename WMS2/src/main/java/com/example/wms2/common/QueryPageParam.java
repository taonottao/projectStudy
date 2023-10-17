package com.example.wms2.common;

import lombok.Data;

import java.util.HashMap;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/6 22:41
 */
@Data
public class QueryPageParam {

    public final int PAGE_SIZE = 20;
    public final int PAGE_NUM = 1;

    private int pageSize = PAGE_SIZE;
    private int pageNum = PAGE_NUM;

    private HashMap param = new HashMap();
}
