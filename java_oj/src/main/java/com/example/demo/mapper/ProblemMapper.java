package com.example.demo.mapper;

import com.example.demo.entity.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通过这个接口来声明针对 Problem 的增删改查
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 18:39
 */
@Mapper
public interface ProblemMapper {

    int insert(Problem problem);

    int delete(@Param("id") Integer id);

    List<Problem> selectAll();

    Problem selectOne(@Param("id")Integer id);

    int praise(@Param("id") Integer id);

}
