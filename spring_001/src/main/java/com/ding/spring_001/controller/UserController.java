package com.ding.spring_001.controller;

import com.ding.spring_001.common.Result;
import com.ding.spring_001.entity.User;
import com.ding.spring_001.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    /* 用户登录 */
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User dataSourceUser = userService.selectByUserNameAndPass(user);
        if(dataSourceUser == null) {
            return Result.error("请输入正确的用户名和密码");
        }
        return Result.success(dataSourceUser);
    }

    /* 查询所有用户信息 */
    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        PageInfo<User> users = userService.selectAll(user);
        return Result.success(users);
    }

    /* 根据用户名和电话查询用户信息 */
    @GetMapping("/selectByUserNameAndPhone")
    public Result selectByUserNameAndPhone(User user) {
        PageInfo<User> list = userService.selectByUserNameAndPhone(user);
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

    /*单独删除 */
    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id) {
        Integer i = userService.del(id);
        if (i != 1)
            return Result.error("删除失败");
        return Result.success();
    }

    /*批量删除 */
    @DeleteMapping("/dels/{ids}")
    public Result deleteBatch(@PathVariable int[] ids) {
        int i = userService.deleteBatch(ids);
        if (i > 0)
            return Result.success();
        return Result.error("批量删除失败");
    }

    /*修改用户信息 */
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        System.out.println(user);
        int i = userService.update(user);
        if (i >= 0)
            return Result.success();
        return Result.error("修改失败");
    }
}