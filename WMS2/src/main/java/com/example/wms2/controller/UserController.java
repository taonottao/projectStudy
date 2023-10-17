package com.example.wms2.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wms2.common.QueryPageParam;
import com.example.wms2.common.Result;
import com.example.wms2.entity.Menu;
import com.example.wms2.entity.User;
import com.example.wms2.service.MenuService;
import com.example.wms2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wt
 * @since 2023-10-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    @RequestMapping("/list")
    public Result list() {
        return Result.succ("成功！", userService.list());
    }

    @RequestMapping("login")
    public Result login(@RequestBody User user) {
        List<User> userList = userService.lambdaQuery().eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword())
                .lt(User::getErrCount, 5).list();
        if (!CollectionUtils.isEmpty(userList)) {
            User user1 = userList.get(0);
            user1.setErrCount(0);
            userService.updateById(user1);
            List<Menu> menuList = menuService.lambdaQuery().like(Menu::getMenuright, user1.getRoleId()).list();
            HashMap map = new HashMap();
            map.put("user", user1);
            map.put("menu", menuList);
            return Result.succ(map);
        } else {
            User user1 = userService.getById(user.getId());
            if (user1 == null) {
                return Result.fail("账号或密码错误！");
            }
            Integer errCount = user1.getErrCount();
            if (errCount >= 5) {
                user1.setErrTime(LocalDateTime.now());
                userService.updateById(user1);
                return Result.fail("账号已冻结，30分钟后解冻");
            } else {
                user1.setErrCount(errCount + 1);
                userService.updateById(user1);
                int chances = 5 - user1.getErrCount();
                return Result.fail("账号或密码错误！还有" + chances + "次机会！");
            }
        }
    }

    @RequestMapping("listPage")
    public Result listPage(@RequestBody QueryPageParam queryPageParam) {
        HashMap param = queryPageParam.getParam();

        String name = (String) param.get("name");
        String roleId = (String) param.get("roleId");
        String sex = (String) param.get("sex");

        Page<User> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());

        LambdaQueryWrapper<User> wrapper =new LambdaQueryWrapper<>();

        if (StringUtils.hasLength(name)) {
            wrapper.like(User::getName, name);
        }
        if (StringUtils.hasLength(roleId)) {
            wrapper.eq(User::getRoleId, roleId);
        }
        if (StringUtils.hasLength(sex)) {
            wrapper.eq(User::getSex, sex);
        }

        IPage result = userService.page(page, wrapper);

        return Result.succ(result.getRecords());
    }
}
