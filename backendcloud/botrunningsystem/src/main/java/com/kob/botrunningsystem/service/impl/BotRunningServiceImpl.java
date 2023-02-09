package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/14 9:19
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {
    public static final BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String  botCode, String input) {
       // System.out.println("add bot: " + userId + " " + botCode + " " + input);
       // System.out.println("add bot: " + userId + " " + input);
        botPool.addBot(userId, botCode, input);
        return "add bot success";
    }
}