package com.ding.spring_001.mapper;

import com.ding.spring_001.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> selectAll();
}
