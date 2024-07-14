package com.leowork.crm.settings.mapper;

import com.leowork.crm.settings.entity.User;

import java.util.List;
import java.util.Map;

/**
 * myBatis逆向工程生成
 * @author Leowork
 */
public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Mon Aug 21 18:05:20 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Mon Aug 21 18:05:20 CST 2023
     */
    int insert(User row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Mon Aug 21 18:05:20 CST 2023
     */
    int insertSelective(User row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Mon Aug 21 18:05:20 CST 2023
     */
    User selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Mon Aug 21 18:05:20 CST 2023
     */
    int updateByPrimaryKeySelective(User row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Mon Aug 21 18:05:20 CST 2023
     */
    int updateByPrimaryKey(User row);




    /**
     * ******************************************************************
     * 根据账号以及密码查询用户
     * @param map 账号以及密码
     * @return 查询结果用户信息包装对象
     */
    User selectUserByLoginActAndPwd(Map<String, Object> map);

    /**
     * 查询所有的用户，用于创建市场活动页面下的创建人选择
     * @return 查询结果集
     */
    List<User> selectAllUsers();




}