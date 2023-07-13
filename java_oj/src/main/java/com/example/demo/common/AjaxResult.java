package com.example.demo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一数据格式的返回
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/7/12 19:10
 */
@Data
public class AjaxResult implements Serializable {
    // 状态码
    private Integer code;
    // 状态码描述信息
    private String msg;
    // 返回的数据
    private Object data;

    /**
     *操作成功返回的结果
     * @param data
     * @return
     */
    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(200);
        ajaxResult.setMsg("");
        ajaxResult.setData(data);
        return ajaxResult;
    }
    public static AjaxResult success(int code, Object data){
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg("");
        ajaxResult.setData(data);
        return ajaxResult;
    }
    public static AjaxResult success(int code, String msg, Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(msg);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    /**
     * 操作失败返回的结果
     * @param code
     * @param msg
     * @return
     */
    public static AjaxResult fail(int code, String msg) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(msg);
        ajaxResult.setData(null);
        return ajaxResult;
    }
    public static AjaxResult fail(int code, String msg, Object data){
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(code);
        ajaxResult.setMsg(msg);
        ajaxResult.setData(data);
        return ajaxResult;
    }
}
