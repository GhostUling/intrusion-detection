package com.ding.spring_001.service;

import org.apache.catalina.User;

public interface UserService {

    List<User> selectAll();
}