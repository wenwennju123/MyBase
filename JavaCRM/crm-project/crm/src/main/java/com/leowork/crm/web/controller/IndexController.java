package com.leowork.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Leo
 * @version 1.0
 * @className IndexController
 * @since 1.0
 **/
@Controller
public class IndexController {
    /**
     * 理论上Controller分配的url是
     * http://127.0.0.1:8080/crm/ 为所有路径共有，协议://ip:端口/web应用名，从 / 开始，必须省略
     * “/” 就可以代表根路径
     * @return index页面
     */
    @RequestMapping("/")
    public String index(){
        System.out.println("访问controller，路径为 / 请求转发跳转到index首页");
        /*
        根据视图解析器，会自动添加前缀后缀
         */
        return "index";
    }
}
