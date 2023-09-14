package com.wms.demo.common;

import lombok.Data;

import java.util.HashMap;

/**
 * 用来封装分页参数
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/9 19:37
 */
@Data
public class QueryPageParam {
    // 默认
    private static int PAGE_SIZE=20;
    private static int PAGE_NUM=1;

    private int pageSize=PAGE_SIZE;
    private int pageNum=PAGE_NUM;

    private HashMap param = new HashMap();
}
