package com.example.mtl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/28 22:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    private int userId;
    private String userName;
    private String userPassword;
    private String userSalt;

}
