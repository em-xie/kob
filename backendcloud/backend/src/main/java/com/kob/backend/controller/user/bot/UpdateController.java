package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/10 15:06
 */
@RestController
public class UpdateController {

    @Autowired
    private UpdateService updateService;


    @PostMapping("/api/user/bot/update/")
    public Map<String,String> update(@RequestParam Map<String,String> data){
        return  updateService.update(data);
    }
}
