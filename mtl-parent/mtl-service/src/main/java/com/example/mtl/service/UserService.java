package com.example.mtl.service;

import com.example.mtl.beans.User;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/28 22:35
 */
public interface UserService {

    User check(String username, String password);

}
