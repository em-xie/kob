package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/10/31 14:13
 */
@RestController
@RequestMapping("/pk/")
public class BotInfoController {
    @RequestMapping("getbotinfo/")
    public Map<String,String> getBotInfo(){
        HashMap<String, String> bot1 = new HashMap<>();
        bot1.put("name","1");
        bot1.put("rating","2");
        return bot1;
    }
}
