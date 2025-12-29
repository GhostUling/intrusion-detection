package com.ding.spring_001.exception;


import com.ding.spring_001.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理器
 * 作用：捕获程序运行时的错误
 * @author hxy
 * @date 2023/9/20
 */
@ControllerAdvice(basePackages = "cn.hxy.controller")//指定了需要拦截的 Controller 包路径，将异常处理器限定在特定的包路径下
public class GlobalExceptionHandler {
    //打印日志
    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //标识一个方法是用来处理特定类型异常的方法
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, Exception e) {
        log.error("异常信息",e);
        return Result.error("系统异常");
    }

    //捕获自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customError(HttpServletRequest request, CustomException e) {
        return Result.error(e.getMsg());
    }
}