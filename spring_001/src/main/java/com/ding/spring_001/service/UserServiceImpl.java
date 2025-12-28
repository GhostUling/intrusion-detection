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
    private UserMapper userMapper;

    // 查询所有用户信息
    @Override
    public List<User> selectAll() {
        List<User> list = userMapper.selectAll();
        return list;
    }

    // 根据用户名和电话查询用户信息
    @Override
    public List<User> selectByUserNameAndPhone(String name, String phone) {
        List<User> list = userMapper.selectByUserNameAndPhone(name, phone);
        return list;
    }


    // 添加用户信息
    @Override
    public int add(User user) {
        int i = userMapper.add(user);
        return i;
    }
}