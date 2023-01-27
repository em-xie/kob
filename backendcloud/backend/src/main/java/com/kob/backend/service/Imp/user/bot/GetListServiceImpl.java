package com.kob.backend.service.Imp.user.bot;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.domain.Bot;
import com.kob.backend.service.user.bot.GetListService;
//import com.kob.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @作者：xie
 * @时间：2022/11/10 14:22
 */
@Service
public class GetListServiceImpl implements GetListService {
    @Autowired
    private BotMapper botMapper;
    @Override
    public List<Bot> getList() {
        Integer loginId = StpUtil.getLoginIdAsInt();
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",loginId);

        return botMapper.selectList(queryWrapper);
    }
}
