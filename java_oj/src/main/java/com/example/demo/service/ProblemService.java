package com.example.demo.service;

import com.example.demo.entity.Problem;
import com.example.demo.mapper.ProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 19:16
 */
@Service
public class ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    public int insert(Problem problem) {
        return problemMapper.insert(problem);
    }

    public int delete(Integer id) {
        return problemMapper.delete(id);
    }

    public List<Problem> selectAll(){
        return problemMapper.selectAll();
    }

    public Problem selectOne(Integer id) {
        return problemMapper.selectOne(id);
    }

    public int praise(Integer id) {
        return problemMapper.praise(id);
    }
}
