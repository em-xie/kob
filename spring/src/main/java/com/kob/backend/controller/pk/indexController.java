package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @作者：xie
 * @时间：2022/10/31 14:08
 */

@Controller
@RequestMapping("/pk/")
public class indexController {
    @RequestMapping("index/")
    public String index(){
        return "pk/index.html";
    }
}
