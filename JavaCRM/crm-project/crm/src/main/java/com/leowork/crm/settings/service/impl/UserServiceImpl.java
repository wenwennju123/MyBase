package com.leowork.crm.settings.service.impl;

import com.leowork.crm.settings.entity.User;
import com.leowork.crm.settings.mapper.UserMapper;
import com.leowork.crm.settings.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author Leo
 * @version 1.0
 * @className UserServiceImpl
 * @since 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        System.out.println("调用UserService的queryUserByLoginActAndPwd方法");
        User user = userMapper.selectUserByLoginActAndPwd(map);

        return user;
    }

    @Override
    public List<User> queryAllUsers() {
        System.out.println("调用UserService的queryAllUsers方法");
        return userMapper.selectAllUsers();
    }
}
