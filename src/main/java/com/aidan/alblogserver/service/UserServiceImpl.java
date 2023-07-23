package com.aidan.alblogserver.service;

import com.aidan.alblogserver.mapper.UserMapper;
import com.aidan.alblogserver.pojo.User;
import com.aidan.alblogserver.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        User user = userMapper.findByNameAndPassword(username, MD5Util.code(password));
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }


}
