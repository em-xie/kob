package com.kob.backend.controller.user.bot;

import com.kob.backend.domain.Bot;
import com.kob.backend.domain.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.UserMapper;
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
 * @时间：2023/1/27 15:47
 */
@RestController
public class BotContextExampleController {


    @Autowired
    private BotMapper botMapper;

    @PostMapping("/bot/context/example/")
    public R<Map<String, Object>> add() {
        Map<String, Object> ajax = new HashMap<>();

        Bot bot=  botMapper.selectById(12);
        String res = bot.getContent();
        ajax.put("example",res);
        return  R.ok(ajax);
    }
}
