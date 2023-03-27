package com.example.springbootdemo.controller;


import com.example.springbootdemo.entity.TbUserInfo;
import com.example.springbootdemo.service.TbUserInfoService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/3/25 23:58
 */
@RestController
@RequestMapping(value = "helloCon")
public class HelloWorld {

    @Autowired
    private TbUserInfoService tbUserInfoService;

    @GetMapping("helloWorld")
    public String helloWorld(){
        return "success1122";
    }


    /**
     * get 方式查询用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "selectUser")
    public String selectUser(@RequestParam(value = "age") Long id){
        String s = tbUserInfoService.selectUser(id);
        return s;
    }


    /**
     * post方式查询用户信息
     */
    @PostMapping(value = "selectId")
    public String selectUserForPost(@RequestParam(value = "id") Long id){

        String s = tbUserInfoService.selectUser(id);
        return s;
    }

    /**
     *根据用户id 修改用户信息
     */
    @PostMapping(value = "updateUser")
    public String updateUser(@RequestParam(value = "id") Long id){
        try{
            String msg = tbUserInfoService.updateUser(id);
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            return "修改失败";
        }
    }


    /**
     * 根据用户id 删除用户信息
     * @param id
     * @return
     */
    @PostMapping(value = "deleteId")
    public String deleteUser(@RequestParam(value = "id") Long id){
       try{
           tbUserInfoService.deleteUser(id);
       }catch (Exception e){
           e.printStackTrace();
           return "删除失败";
       }
        return "删除成功";
    }


    /**
     * 新增用户信息
     * @return
     */
    @PostMapping(value = "insertUser")
    public String insertUser(@RequestParam(value = "id") Long id,@RequestParam(value = "age") Integer age,@RequestParam(value = "name") String name){
        try{
            tbUserInfoService.insertUser(id,age,name);
        }catch (Exception e){
            e.printStackTrace();
            return "新增失败";
        }
        return "新增成功";
    }



    @PostMapping(value = "insertUserForEntity")
    public String insertUserForEntity(@RequestBody TbUserInfo tbUserInfo){
        try{
            tbUserInfoService.insertUserForEntity(tbUserInfo);
        }catch (Exception e){
            e.printStackTrace();
            return "新增失败";
        }
        return "新增成功";
    }


    /**
     * 批量新增用户
     * @param tbUserInfo
     * @return
     */
    public String insertUserBatch(@RequestBody TbUserInfo tbUserInfo){

        return "error";
    }

}
