package com.kob.job.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.common.utils.StringUtils;
import com.kob.common.utils.redis.RedisUtils;
import com.kob.job.domain.CallJob;
import com.kob.job.mapper.CallJobMapper;
import com.kob.job.service.CallJobService;
import com.kob.job.service.CallMeService;
import com.kob.job.util.VeDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2023/2/2 22:37
 */
@RequiredArgsConstructor
@Service
public class CallJobServiceImpl implements CallJobService {
    @Autowired
    private CallJobMapper callJobMapper;
    @Override
    public Map<String, String> add(Map<String, String> data) {
        String to = data.get("to");
        //2022-11-10 15:10:39
        String finalTime = data.get("finalTime");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//        simpleDateFormat.
        HashMap<String, String> map = new HashMap<>();
        if (to == null || to.length() == 0) {
            map.put("error_message", "信息不能为空");
            return map;
        }




        Date now = new Date();
        String time = StringUtils.substring(finalTime,0,2);
        String finalTime1 = VeDate.getPreTime1(now,time);
        Date now1 = new Date();
        Integer loginId = StpUtil.getLoginIdAsInt();
        CallJob job = new CallJob(null,to,finalTime1,now1,now1,"0",loginId);
        callJobMapper.selectByStatus();
        callJobMapper.insert(job);

        map.put("error_message", "success");
        return map;
    }
}
