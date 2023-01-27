package com.kob.backend.service.Imp.user.bot;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.domain.Bot;
import com.kob.backend.service.user.bot.RemoveService;
//import com.kob.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/10 14:20
 */
@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public Map<String, String> remove(Map<String, String> data) {

//        User user = UserUtil.getUser();
        Integer loginId = StpUtil.getLoginIdAsInt();

        int bot_id =Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);
        Map<String, String> map = new HashMap<>();
        if (bot == null) {
            map.put("error_message", "Bot不存在或已被删除");
            return map;
        }

        if (!bot.getUserId().equals(loginId)) {
            map.put("error_message", "没有权限删除该Bot");
            return map;
        }

        botMapper.deleteById(bot_id);

        map.put("error_message", "success");
        return map;


    }
}
