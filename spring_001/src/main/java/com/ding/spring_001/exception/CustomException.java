package com.ding.spring_001.exception;

/**
 * 自定义异常
 * 当我们的程序需要抛出错误提示前端的时候可以使用
 * @author hxy
 * @date 2023/10/17
 */
public class CustomException extends RuntimeException{

    static final long serialVersionUID = 11232L;

    private String msg;


    public CustomException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}