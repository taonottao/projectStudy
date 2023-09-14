package com.wms.demo.entity;

import lombok.Data;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/12 16:22
 */
@Data
public class RecordVO extends Record {
    private String username;
    private String adminname;
    private String goodsname;
    private String storagename;
    private String goodstypename;
}
