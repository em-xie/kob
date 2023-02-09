package com.kob.backend;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.common.utils.StringUtils;
import com.kob.common.utils.redis.RedisUtils;
import com.kob.job.consumer.CallWebSocketServer;
import com.kob.job.domain.CallJob;
import com.kob.job.mapper.CallJobMapper;
import com.kob.job.util.VeDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class BackendApplicationTests {

    @Autowired
    private CallJobMapper callJobMapper;

    @Test
    void contextLoads() throws ParseException {

        List<CallJob> callJobsList = callJobMapper.selectByStatus();

        if(callJobsList != null && callJobsList.size() > 0) {
            for (CallJob callJob : callJobsList) {
                String time = StringUtils.substring(callJob.getFinalTime(),0,2);

                //log.info(time);
               // log.info(String.valueOf(VeDate.strToDate(callJob.getFinalTime())));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String mydate1 = "";
                Date beginDate = callJob.getCreateTime();
                Date update = callJob.getUpdateTime();
                long Time = (beginDate.getTime() / 1000) + Integer.parseInt(time) * 60;
                beginDate.setTime(Time * 1000);
                mydate1 = format.format(beginDate);
                //log.info(mydate1);
                Date date = VeDate.strToDate(mydate1);
                //log.info(String.valueOf(date));

                Date endDate = format.parse(mydate1);
                long longBegin = update.getTime();
                long longEnd = endDate.getTime();
                log.info(String.valueOf(longBegin));
                log.info(String.valueOf(longEnd));
                if(longEnd>longBegin+10){

                    update = VeDate.StrtoDate(VeDate.getPreTime1(update,"1"));
                    log.info(format.format(update));

                    CallJob callJob1 = new CallJob();
                    callJob1.setId(callJob.getId());
                    callJob1.setUpdateTime(update);
                    callJobMapper.UpdateUpdateTime(callJob1);
                }


            }
        }


    }

    @Test
    void test1(){
        Date now1 = new Date();
        CallJob job = new CallJob(null,"2","1",now1,now1,"0",1);
        RedisUtils.setCacheObject("xxl-job",job);

    }

    @Test
    void test2(){
        List<CallJob> callJobsList = callJobMapper.selectByStatus();
        for (CallJob callJob : callJobsList) {
            CallWebSocketServer.users.get(callJob.getUserId()).sendMessage("success");
        }

    }

}
