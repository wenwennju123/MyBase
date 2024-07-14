package com.leowork.crm.commons.utils;

import java.util.UUID;

/**
 * 专门用来生成UUID的工具类
 * @author Leo
 * @version 1.0
 * @className UUIDUtils
 * @since 1.0
 **/
public class UUIDUtils {
    public static String getUUID(){
        /*使用JDK的uuid工具类生成 默认生成32位 并去掉其中的"-"*/
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
