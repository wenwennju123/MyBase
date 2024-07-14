package com.leowork.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Leo
 * @version 1.0
 * @className WorkBenchIndexController
 * @since 1.0
 **/
@Controller
public class WorkBenchIndexController {

    /**
     * 负责跳转workbench的主页面的controller
     * url和处理的资源路径保持一致，方法名见名知意
     * @return
     */
    @RequestMapping("/workbench/index.action")
    public String index(){
        return "workbench/index";
    }



}
