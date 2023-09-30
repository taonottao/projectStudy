package com.wt.mtl.dao;

import com.example.mtl.beans.User;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/28 22:25
 */
public interface UserDAO {
    User selectUserByUsername(String username);
}
