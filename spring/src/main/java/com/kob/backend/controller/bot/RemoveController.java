package com.kob.backend.controller.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/10 14:51
 */
@RestController
public class RemoveController {
    @Autowired
    private RemoveService removeService;

    @PostMapping("/user/bot/remove/")
    public Map<String,String> remove(@RequestParam Map<String,String> data){
        return removeService.remove(data);
    }
}