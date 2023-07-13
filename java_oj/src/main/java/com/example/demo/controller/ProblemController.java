package com.example.demo.controller;

import com.example.demo.common.AjaxResult;
import com.example.demo.entity.Problem;
import com.example.demo.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 19:18
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @RequestMapping("/add")
    public AjaxResult add(Problem problem) {
        // 1. 非空校验
        if (problem == null || !StringUtils.hasLength(problem.getTitle())
                || !StringUtils.hasLength(problem.getDescription())
                || !StringUtils.hasLength(problem.getTestCode())
                || !StringUtils.hasLength(problem.getTemplateCode())) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 2. 数据库的添加操作
        return AjaxResult.success(problem);
    }

    @RequestMapping("/delete")
    public AjaxResult delete(Integer id) {
        if (id == null || id < 0) {
            // 参数有误
            return AjaxResult.fail(-1, "参数异常");
        }

        return AjaxResult.success(problemService.delete(id));
    }

    @RequestMapping("/selectAll")
    public AjaxResult selectAll() {
        return AjaxResult.success(problemService.selectAll());
    }

    @RequestMapping("/selectOne")
    public AjaxResult selectOne(Integer id) {
        if (id == null || id < 0) {
            // 参数有误
            return AjaxResult.fail(-1, "参数异常");
        }
        return AjaxResult.success(problemService.selectOne(id));
    }
}
