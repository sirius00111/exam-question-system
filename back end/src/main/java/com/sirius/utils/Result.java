package com.sirius.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    private Integer code;       // 返回的状态码
    private String message;     // 返回的状态信息
    private Object data;        // 返回的状态数据
    public Result(Integer code,String message,Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
    // 返回成功信息

    public static Result success(Object data){
        return new Result(200,"success", data);
    }
    // 返回错误信息(包含错误的状态码)
    public static Result error(Integer code,Object data){
        return new Result(code, "error", data);
    }

}