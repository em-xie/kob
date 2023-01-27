package com.kob.backend.service.Imp.user.account;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.domain.User;

import com.kob.backend.service.user.account.InfoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:56
 */
@Slf4j
@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserMapper userMapper;



    @Override
    public Map<String, String> getInfo() {

        Integer loginId = StpUtil.getLoginIdAsInt();
        //log.info(String.valueOf(StpUtil.getLoginIdAsInt()));
        User user = userMapper.selectById(loginId);
        HashMap<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }
}
