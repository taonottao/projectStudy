package com.example.mmq.mqserver.datacenter;

import com.example.mmq.mqserver.mapper.MetaMapper;

import java.io.File;
import java.io.FileWriter;

/**
 * 通过这个类来整合上述的数据库操作
 *
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/31 19:58
 */

public class DatabaseManager {
    private MetaMapper mapper;

    // 针对数据库进行初始化 = 建库建表 + 插入一些默认数据
    public void init() {
        if (!checkDBExists()) {

            createTable();

            createDefaultData();

            System.out.println("[DatabaseManager] 数据库初始化完成！");
        } else {
            System.out.println("[DatabaseManager] 数据库已经存在！");
        }
    }

    private void createTable() {

    }

    private boolean checkDBExists() {
        File file = new File("./data/meta.db");
        if (file.exists()) {
            return true
        } else {
            return false;
        }
    }
}
