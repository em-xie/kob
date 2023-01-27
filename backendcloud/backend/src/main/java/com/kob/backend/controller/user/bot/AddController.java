package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import com.kob.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/10 14:34
 */

@RestController
public class AddController {

    @Autowired
    private AddService addService;


    @PostMapping("/user/bot/add/")
    public R<Map<String, Object>> add(@Validated @RequestBody Map<String, String> data) {
        Map<String, Object> ajax = new HashMap<>();
        Map<String, String> res = addService.add(data);
        ajax.put("add",res);
        return  R.ok(ajax);
    }
}
