package com.ding.spring_001.service;

import com.ding.spring_001.entity.User;
import com.ding.spring_001.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    //调用持久层的接口,执行sql
    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        List<User> list = userMapper.selectAll();
        return list;
    }
}