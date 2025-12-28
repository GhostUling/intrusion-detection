package com.ding.spring_001.service;


import com.ding.spring_001.entity.User;


import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserService {

    List<User> selectAll();


    List<User> selectByUserNameAndPhone(@Param("name") String name, @Param("phone") String phone);
    // 多个参数需要使用Param注解进行标注


    int add(User user);
}
