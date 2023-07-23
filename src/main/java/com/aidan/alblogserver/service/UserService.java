package com.aidan.alblogserver.service;

import com.aidan.alblogserver.pojo.User;

public interface UserService {
    //检查用户信息
    User checkUser(String username,String password);
    // 得到用户
    User getUser(Long id);
}
