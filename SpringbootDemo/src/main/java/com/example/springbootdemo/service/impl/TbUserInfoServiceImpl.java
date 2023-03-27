package com.example.springbootdemo.service.impl;

import com.example.springbootdemo.dao.TbUserInfoMapper;
import com.example.springbootdemo.entity.TbUserInfo;
import com.example.springbootdemo.service.TbUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/3/26 16:29
 */
@Service
public class TbUserInfoServiceImpl implements TbUserInfoService {

    @Resource
    private TbUserInfoMapper tbUserInfoMapper;

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    @Override
    public String selectUser(Long id) {
        TbUserInfo tbUserInfo = tbUserInfoMapper.selectById(id);
        return tbUserInfo.toString();
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Override
    public String deleteUser(Long id) {
        try {
            int i = tbUserInfoMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败111";
        }
        return "删除成功";
    }


    @Override
    public String updateUser(Long id) {
        try {
            //1:根据id查询这个用户存不存在
            TbUserInfo tbUserInfo1 = tbUserInfoMapper.selectById(id);
            tbUserInfo1.setName("李四-0237");
            tbUserInfo1.setAge(100);
            tbUserInfoMapper.updateById(tbUserInfo1);
        }catch (Exception e){
            e.printStackTrace();
            return "修改失败111";
        }
        return "修改成功";
    }

    @Override
    public String insertUser(Long id, Integer age, String name) {
        try {
            TbUserInfo tbUserInfo = new TbUserInfo();
            tbUserInfo.setId(id);
            tbUserInfo.setAge(age);
            tbUserInfo.setName(name);
            tbUserInfoMapper.insert(tbUserInfo);
        }catch (Exception e){
            e.printStackTrace();
            return "新增失败111";
        }
        return "新增成功";
    }

    @Override
    public String insertUserForEntity(TbUserInfo tbUserInfo) {
        try {
            System.out.println(11);
            tbUserInfoMapper.insert(tbUserInfo);
        }catch (Exception e){
            e.printStackTrace();
            return "新增失败insertUserForEntity";
        }
        return "新增成功insertUserForEntity";
    }


}
