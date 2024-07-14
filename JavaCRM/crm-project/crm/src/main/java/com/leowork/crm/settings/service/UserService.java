package com.leowork.crm.settings.service;

import com.leowork.crm.settings.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author Leo
 * @version 1.0
 * @className UserServicee
 * @since 1.0
 **/
public interface UserService {
    /**
     * 查询user
     * @param map 用户信息封装map
     * @return 查询结果
     */
    User queryUserByLoginActAndPwd(Map<String, Object> map);

    /**
     * 查询所有用户
     * @return 查询结果集
     */
    List<User> queryAllUsers();
}
