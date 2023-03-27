package com.example.springbootdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootdemo.entity.ClassInfo;
import com.example.springbootdemo.service.ClassInfoService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/3/26 20:18
 */

@RestController
@RequestMapping(value = "helloCn")
public class MyHelloWorld {

    @Autowired
    private ClassInfoService classInfoService;

    @GetMapping("myHelloWorld")
    public String myHelloWorld(){
        return "success1234";
    }

    /**
     * 根据id查询
     */
    @PostMapping(value = "selectId")
    public String selectForPost(@RequestParam(value = "id") Long id){

        String s = classInfoService.selectInfo(id);
        return s;

    }

    /**
     * 修改
     */
    @PostMapping(value = "updateId")
    public String updateForPost(@RequestParam(value = "id") Long id){
        try {
            String msg = classInfoService.updateInfo(id);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败";
        }
    }

    /**
     * 删除
     */
    @PostMapping(value = "deleteId")
    public String deleteForPost(@RequestParam(value = "id") Long id){
        try {
            classInfoService.deleteInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }
        return "删除成功";
    }

    /**
     * 增加
     */
    @PostMapping(value = "insertForEntity")
    public String insertForEntity(@RequestBody ClassInfo classInfo){
        try {
            classInfoService.insertInfoForEntity(classInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "新增失败";
        }
        return "新增成功";
    }


}
