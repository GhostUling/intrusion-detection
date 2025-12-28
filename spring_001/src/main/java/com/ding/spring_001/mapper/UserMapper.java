package com.ding.spring_001.mapper;

import com.ding.spring_001.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> selectAll();


    List<User> selectByUserNameAndPhone(String name, String phone);


    int add(User user);
}
