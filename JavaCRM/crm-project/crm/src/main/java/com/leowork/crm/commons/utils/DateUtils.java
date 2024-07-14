package com.leowork.crm.commons.utils;

import com.leowork.crm.commons.contants.Contants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户格式化日期时间Date的工具类
 * @author Leo
 * @version 1.0
 * @className DateUtils
 * @since 1.0
 *
 * 格式可以采用常量定义，也可写入配置文件
 **/
public class DateUtils {
    /**
     * 将date类型进行格式化
     * yyyy-MM-dd HH:mm:ss
     * @param date 输入日期
     * @return 转换完成的日期字符串
     */
    public static String formatDateTime(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat(Contants.FORMAT_DATE_TIME);

        return sdf.format(date);
    }

    /**
     * 将date类型进行格式化
     * yyyy-MM-dd
     * @param date 输入日期
     * @return 转换完成的日期字符串
     */
    public static String formatDate(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat(Contants.FORMAT_DATE);

        return sdf.format(date);
    }
}
