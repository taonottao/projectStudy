package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dao.ClassInfoMapper;
import com.example.springbootdemo.entity.ClassInfo;
import com.example.springbootdemo.service.ClassInfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/3/26 20:34
 */
@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    @Resource
    private ClassInfoMapper classInfoMapper;


    /**
     * 查询
     *
     * @param id
     */
    @Override
    public String selectInfo(Long id) {

        ClassInfo classInfo = classInfoMapper.selectById(id);
        return classInfo.toString();

    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public String deleteInfo(Long id) {
        try {
            int i = classInfoMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败111";
        }
        return "删除成功";
    }

    /*
    修改
     */
    @Override
    public String updateInfo(Long id) {
        try {
            //1:先判断是否存在，存在修改，不存在提示数据不存在\
            ClassInfo classInfo = classInfoMapper.selectById(id);
            classInfo.setClassName("大三");
//            classInfo.setId(1001L);
            classInfoMapper.updateById(classInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败111";
        }
        return "修改成功";
    }

    /*
    添加
     */
    @Override
    public String insertInfoForEntity(ClassInfo classInfo) {
        try {
            classInfoMapper.insert(classInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }
}
