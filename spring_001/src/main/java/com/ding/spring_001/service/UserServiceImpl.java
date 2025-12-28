package com.ding.spring_001.service;

import com.ding.spring_001.entity.User;
import com.ding.spring_001.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<User> selectAll(User user) {
        //开启分页
        Integer pageNum=user.getPageNum();
        Integer pageSize = user.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<User> users = userMapper.selectAll();
        PageInfo<User> userPageInfo=new PageInfo<>(users);
        return userPageInfo;
    }

    // 根据用户名和电话查询用户信息
    @Override
    public PageInfo<User> selectByUserNameAndPhone(User user) {
        PageHelper.startPage(user.getPageNum(),user.getPageSize());
        List<User> list = userMapper.selectByUserNameAndPhone(user.getName(),user.getPhone());
        PageInfo<User> userPageInfo=new PageInfo<>(list);
        return userPageInfo;
    }


    // 添加用户信息
    @Override
    public int add(User user) {
        int i = userMapper.add(user);
        return i;
    }


    // 删除用户信息
    @Override
    public int del(Integer id) {
        int i = userMapper.del(id);
        return i;
    }

    
    // 批量删除用户信息
    @Override
    public int deleteBatch(int[] ids) {
        int i = userMapper.deleteBatch(ids);
        return i;
    }
}