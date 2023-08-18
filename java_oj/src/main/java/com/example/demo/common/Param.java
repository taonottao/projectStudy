package com.example.demo.common;

import org.springframework.util.StringUtils;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/8/16 16:46
 */
public class Param {

    public static boolean checkParamNull(Object object,String... params) {
        if (object == null) {
            return true;
        }

        for (String param : params) {
            if (!StringUtils.hasLength(param)) {
                return true;
            }
        }
        return false;
    }

}
