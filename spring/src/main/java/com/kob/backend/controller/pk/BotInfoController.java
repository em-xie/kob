package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @作者：xie
 * @时间：2022/10/31 14:13
 */
@RestController
@RequestMapping("/pk/")
public class BotInfoController {
    @RequestMapping("getbotinfo/")
    public List<String> getBotInfo(){
        LinkedList<String> list = new LinkedList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        return list;
    }
}
