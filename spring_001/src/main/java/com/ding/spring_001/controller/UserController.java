package com.ding.spring_001.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//告诉SpringBoot当前类是一个控制器，可以接收前端请求。交给Spring容器管理
@RestController//默认返回时会经过视图解析器
@RequestMapping("/user")
public class UserController {
    //我现在是不是需要去业务层处理逻辑 可能要计算等问题的逻辑
    @Autowired
    UserService userService;

    @RequestMapping("/selectAll")
    public  List<User> selectAll() {
        List<User> users = userService.selectAll();
        return users;
    }
}