package com.leowork.crm.commons.contants;

/**
 * 常量类
 * @author Leo
 * @version 1.0
 * @className Contants
 * @since 1.0
 **/
public class Contants {
    /**
     * ReturnObject类中的Code值，返回编码对应状态
     * 0 -- 登陆失败
     */
    public static final String RETURN_OBJECT_CODE_FILED = "0";
    /**
     * ReturnObject类中的Code值，返回编码对应状态
     * 1 -- 登陆成功
     */
    public static final String RETURN_OBJECT_CODE_SUCCESS = "1";
    /**
     * 用户账户信息锁定状态
     * 0 -- 已锁定
     */
    public static final String ACT_STATE_LOCKED = "0";
    /**
     * date类型进行格式化 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * date类型进行格式化 yyyy-MM-dd
     */
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    /**
     * 保存当前用户到session中的key
     */
    public static final String SESSION_USER= "sessionUser";
    /**
     * TRUE
     */
    public static final String TRUE = "true";
    /**
     * FALSE
     */
    public static final String FALSE = "false";
}
