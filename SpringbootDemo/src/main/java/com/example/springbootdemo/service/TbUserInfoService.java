package com.example.springbootdemo.service;

import com.example.springbootdemo.entity.TbUserInfo;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/3/26 16:28
 */
public interface TbUserInfoService {

    /**
     * 查询用户
     * @param id
     * @return
     */
    String selectUser(Long id);

    String deleteUser(Long id);

    String updateUser(Long id);

    String insertUser(Long id, Integer age, String name);

    String insertUserForEntity(TbUserInfo tbUserInfo);
}
