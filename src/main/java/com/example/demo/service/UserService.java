package com.example.demo.service;

import com.example.demo.constant.Constants;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public void login(String username, String password) {
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException("用户/密码必须填写");
        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new ServiceException("用户名或密码错误");
        }
        if (user.getStatus().equals(Constants.FAIL_STATUS)) {
            throw new ServiceException("用户状态异常");

        }
        if (user.getLoginStatus().equals(Constants.LOGIN_STATUS_I)) {
            throw new ServiceException("用户已登录，请勿重复登录");
        }
        user.setLoginStatus(Constants.LOGIN_STATUS_I);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.update(user);
    }

    public void register(User user) {
        if (StringUtils.isAnyBlank(user.getEmail(), user.getUsername(), user.getPassword()))
        {
            throw new ServiceException("用户/密码/邮箱必须填写");
        }
        User checkUser = userMapper.selectByUsername(user.getUsername());
        if (checkUser != null) {
            throw new ServiceException("用户名已被使用");
        }
        user.setStatus("正常");
        user.setLastLoginTime(LocalDateTime.now());
        user.setCreatedTime(LocalDateTime.now());
        user.setLoginStatus(Constants.LOGIN_STATUS_O);
        userMapper.insert(user);
    }
}
