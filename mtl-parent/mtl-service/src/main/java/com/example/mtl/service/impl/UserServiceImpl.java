package com.example.mtl.service.impl;

import com.example.mtl.beans.User;
import com.example.mtl.service.UserService;
import com.example.mtl.util.MD5Utils;
import com.wt.mtl.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/28 22:37
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User check(String username, String password) {
        User user = userDAO.selectUserByUsername(username);
        if (user != null) {
            String s = MD5Utils.md5(password + user.getUserSalt());
            if (user.getUserPassword().equals(s)) {
                return user;
            }
        }
        return null;
    }
}
