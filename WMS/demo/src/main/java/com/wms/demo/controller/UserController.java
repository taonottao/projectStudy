package com.wms.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.common.QueryPageParam;
import com.wms.demo.common.Result;
import com.wms.demo.entity.Menu;
import com.wms.demo.entity.User;
import com.wms.demo.service.MenuService;
import com.wms.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wt
 * @since 2023-09-09
 */
//@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    @GetMapping("/findByNo")
    public Result findByNo(String no) {
        List<User> list = userService.lambdaQuery().eq(User::getNo, no).list();
        return list.size()>0 ?Result.suc(list):Result.fail();
    }

    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user)?Result.suc() : Result.fail();
    }

    // 更新
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateById(user)?Result.suc():Result.fail();
    }

    // 修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);
    }

    // 新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    // 删除
    @GetMapping("/del")
    public Result del(Integer id) {

        return userService.removeById(id)?Result.suc(id):Result.fail();
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {

        List<User> list = userService.lambdaQuery().eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword())
                .lt(User::getErrCount, 5).list();
        if (list.size() > 0) {
            User user1 = list.get(0);
            user1.setErrCount(0);
            userService.updateById(user1);
            List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRoleId()).list();
            HashMap map = new HashMap();
            map.put("user", user1);
            map.put("menu", menuList);
            return Result.suc(map);
        } else {
            User tmp = userService.getByNo(user.getNo());
            System.out.println(tmp.getId());
            if (tmp != null) {
                int count = tmp.getErrCount();
                count++;
                tmp.setErrCount(count);
                if (count >= 5) {
                    tmp.setNoStatus(1);
                    userService.updateById(tmp);
                    return Result.isFrozen();
                }
                userService.updateById(tmp);
            }
        }

        return Result.fail();
    }


    // 删除
    @GetMapping("/delete")
    public boolean delete(Integer id) {
        return userService.removeById(id);
    }

    // 查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasLength(user.getName())){
            lambdaQueryWrapper.like(User::getName, user.getName());
        }
        return Result.suc(userService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
//    public List<User> listPage(@RequestBody HashMap map) {
    public List<User> listPage(@RequestBody QueryPageParam query) {
//        System.out.println(query);
//        System.out.println("num=" + query.getPageNum());
//        System.out.println("size=" + query.getPageSize());
        HashMap param = query.getParam();
//        System.out.println("name="+param.get("name"));
//        System.out.println("no="+param.get("no"));

        String name = (String) param.get("name");

//        Page<User> page = new Page<>(1, 2 );
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName,name);

        // 对姓名进行模糊查询，对得到的结果进行分页处理
        IPage result = userService.page(page, lambdaQueryWrapper);
        System.out.println(result.getTotal());// 总记录数

        return result.getRecords();
    }
    @PostMapping("/listPageC")
    public List<User> listPageC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();

        String name = (String) param.get("name");

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName,name);

        // 对姓名进行模糊查询，对得到的结果进行分页处理
        IPage result = userService.pageC(page);
//        IPage result = userService.pageCC(page, lambdaQueryWrapper);
        System.out.println(result.getTotal());// 总记录数

        return result.getRecords();
    }
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();

        String name = (String) param.get("name");
        String sex = (String)param.get("sex");
        String roleId = (String)param.get("roleId");

        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.hasLength(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }
        if(StringUtils.hasLength(sex)){
            lambdaQueryWrapper.eq(User::getSex,sex);
        }
        if(StringUtils.hasLength(roleId)){
            lambdaQueryWrapper.eq(User::getRoleId,roleId);
        }

        // 对姓名进行模糊查询，对得到的结果进行分页处理
//        IPage result = userService.pageC(page);
        IPage result = userService.pageCC(page, lambdaQueryWrapper);
        System.out.println(result.getTotal());// 总记录数

        return Result.suc(result.getRecords(), result.getTotal());
    }
}
