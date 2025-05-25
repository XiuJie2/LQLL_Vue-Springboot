package com.example.springboot.common;


public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setMsg("请求成功");
        result.setCode("200");
        result.setData(data);
        return result;
    }


    public static Result error(String code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
