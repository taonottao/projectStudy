package com.example.demo.common;

import lombok.Data;

/**
 * 统一返回格式
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/28 19:19
 */
@Data
public class ResultAjax {

    private Integer code;
    private String msg;
    private Object data;

    public static ResultAjax success(Object data) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setCode(200);
        resultAjax.setData(data);
        resultAjax.setMsg("");
        return resultAjax;
    }

    public static ResultAjax fail(int code, String msg) {
        ResultAjax resultAjax = new ResultAjax();
        resultAjax.setCode(code);
        resultAjax.setMsg(msg);
        return resultAjax;
    }

}
