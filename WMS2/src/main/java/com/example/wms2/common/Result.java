package com.example.wms2.common;

import lombok.Data;

/**
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/10/6 21:36
 */
@Data
public class Result {

    private int code;// 1 正常 2 错误
    private String msg;
    private Object data;

    public static Result succ() {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("成功！");
        return result;
    }
    public static Result succ(String msg) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg(msg);
        return result;
    }
    public static Result succ(Object data) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("成功！");
        result.setData(data);
        return result;
    }
    public static Result succ(String msg, Object data) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode(2);
        result.setMsg("失败！");
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setCode(2);
        result.setMsg(msg);
        return result;
    }

}
