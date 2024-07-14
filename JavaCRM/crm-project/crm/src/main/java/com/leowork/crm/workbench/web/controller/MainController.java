package com.leowork.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Leo
 * @version 1.0
 * @className MainController
 * @since 1.0
 **/
@Controller
public class MainController {

    @RequestMapping("/workbench/main/index.action")
    public String index(){
        /*跳转workbench下的main的主页*/
        return "workbench/main/index";
    }

}
