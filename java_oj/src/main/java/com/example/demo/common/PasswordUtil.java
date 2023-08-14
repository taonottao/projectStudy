package com.example.demo.common;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 密码加盐、登录验证
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/17 15:53
 */
public class PasswordUtil {

    public static void main(String[] args) {
        System.out.println(encrypt("admin"));
        System.out.println(checkPassword("admin", encrypt("admin")));
    }


    /**
     * 密码加盐
     * @param password
     * @return
     */
    public static String encrypt(String password) {
        // 生成盐值
        String salt = UUID.randomUUID().toString().replace("-", "");
        // 生成加盐之后的密码
        String saltPassword = DigestUtils.md5DigestAsHex((salt+password).getBytes());
        // 生成最终密码，约定最终密码格式为：32为盐值 + $ + 32位盐值密码
        String finalPassword = salt + "$" + saltPassword;
        return finalPassword;
    }

    /**
     * encrypt重载方法，用于用户输入密码时，将密码加盐，再比对
     * @param password
     * @param salt
     * @return
     */
    public static String encrypt(String password, String salt) {
        String saltPassword = DigestUtils.md5DigestAsHex((salt+password).getBytes());
        String finalPassword = salt + "$" + saltPassword;
        return finalPassword;
    }

    /**
     * 验证密码
     * @param inputPassword
     * @param finalPassword
     * @return
     */
    public static boolean checkPassword(String inputPassword, String finalPassword) {
        if (StringUtils.hasLength(inputPassword) && StringUtils.hasLength(finalPassword)
                && finalPassword.length() == 65) {
            String salt = finalPassword.split("\\$")[0];
            String finalPassword2 = encrypt(inputPassword, salt);

            if (finalPassword.equals(finalPassword2)) {
                return true;
            }
        }
        return false;
    }

}
