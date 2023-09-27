package com.example.mtl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Stack;

/**
 * 响应异步请求的 vo 类
 * @version 1.0
 * @Author T-WANG
 * @Date 2023/9/19 23:43
 */
@Data
@ToString
public class ResultVO<T> {

    private int code;
    private String msg;
    private Object data;

    public static ResultVO success(Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO success(int code, String msg, Object data) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setData(data);
        return resultVO;
    }

    public static ResultVO fail(int code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
