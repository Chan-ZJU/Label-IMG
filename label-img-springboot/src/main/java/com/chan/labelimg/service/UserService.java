package com.chan.labelimg.service;

import com.chan.labelimg.mapper.UserMapper;
import com.chan.labelimg.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 3:52 PM
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean isExist(String username){
        User user = userMapper.getByUsername(username);
        return null!=user;
    }

    public User getByName(String username){
        return userMapper.getByUsername(username);
    }

    public User getByNameAndPassword(String username, String password){
        return userMapper.getByUsernameAndPassword(username,password);
    }
}
