package com.ding.spring_001.controller;

import com.ding.spring_001.common.Result;
import com.ding.spring_001.entity.User;
import com.ding.spring_001.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

//告诉SpringBoot当前类是一个控制器，可以接收前端请求。交给Spring容器管理
@CrossOrigin//该注解解决跨域问题
@RestController//默认返回时会经过视图解析器
@RequestMapping("/user")
public class UserController {
    //我现在是不是需要去业务层处理逻辑 可能要计算等问题的逻辑
    @Autowired
    UserService userService;


    /* 查询所有用户信息 */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> users = userService.selectAll();
        return Result.success(users);
    }

    /* 根据用户名和电话查询用户信息 */
    @GetMapping("/selectByUserNameAndPhone")
    public Result selectByUserNameAndPhone(String name, String phone) {
        List<User> list = userService.selectByUserNameAndPhone(name, phone);
        return Result.success(list);
    }

    /*添加用户信息 */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        int i = userService.add(user);
        if (i == 1)
            return Result.success();
        return Result.error("添加失败");
    }
}