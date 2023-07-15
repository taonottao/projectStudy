package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/14 23:29
 */
@Mapper
public interface UserMapper {

    int reg(Userinfo userinfo);

    Userinfo getUserByName(String username);

}
