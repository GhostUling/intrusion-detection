package com.ding.spring_001.service;


import com.ding.spring_001.entity.User;


import java.util.List;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

public interface UserService {

    PageInfo<User> selectAll(User user);


    PageInfo<User> selectByUserNameAndPhone(User user);
    // 多个参数需要使用Param注解进行标注


    int add(User user);


    int del(Integer id);


    int deleteBatch(int[] ids);

    
    int update(User user);
}
