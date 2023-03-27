package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.ClassInfo;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/3/26 20:23
 */
public interface ClassInfoService {

    /**
     * 操作表
     */

    String selectInfo(Long id);

    String deleteInfo(Long id);

    String updateInfo(Long id);

    String insertInfoForEntity(ClassInfo classInfo);
}
