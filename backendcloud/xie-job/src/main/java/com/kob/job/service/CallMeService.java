package com.kob.job.service;

import cn.dev33.satoken.stp.StpUtil;
import com.kob.common.utils.email.MailUtils;
import com.kob.job.consumer.CallWebSocketServer;
import com.kob.job.domain.CallJob;
import com.kob.job.mapper.CallJobMapper;
import com.kob.job.util.VeDate;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @作者：xie
 * @时间：2023/1/30 13:21
 */
@Slf4j
@Service
public class CallMeService{

    @Autowired
    private CallJobMapper callJobMapper;

    public static RestTemplate restTemplate;
    private final static String CallMeUrl = "http://127.0.0.1:3000/kob/call/now/";
//    @Autowired
//    private UtilsCallMeService utilsCallMeService;

    @XxlJob("CallMeHandler")
    public void CallMeHandler() throws Exception {
//        String param = XxlJobHelper.getJobParam();    // 获取参数
//        long jobId = XxlJobHelper.getJobId();
//        log.info("demoJobHandlerParam-定时任务执行了,任务id:" + jobId + ",...,参数:" + param);
        List<CallJob> callJobsList = callJobMapper.selectByStatus();
        if(callJobsList != null && callJobsList.size() > 0) {
            for (CallJob callJob : callJobsList) {
                String finalTime = callJob.getFinalTime();
                Date updateTime = callJob.getUpdateTime();
                Date endDate = VeDate.StrtoDate(finalTime);
                long longBegin = updateTime.getTime();
                long longEnd = endDate.getTime();
                if(longEnd>longBegin){
                    updateTime = VeDate.StrtoDate(VeDate.getPreTime1(updateTime,"1"));
                    CallJob callJob1 = new CallJob();
                    callJob1.setId(callJob.getId());
                    callJob1.setUpdateTime(updateTime);
                    callJobMapper.UpdateUpdateTime(callJob1);
                }else{
                    CallJob callJob1 = new CallJob();
                    callJob1.setId(callJob.getId());
                    callJob1.setStatus("1");
                    callJobMapper.UpdateStatus(callJob1);
                    MailUtils.sendText(callJob.getToWho(), "demo", "预约提醒");
                    //restTemplate.postForObject(CallMeUrl,"success",String.class);
                    if(callJob.getUserId().equals(CallWebSocketServer.users.get(callJob.getUserId()))) {
                        CallWebSocketServer.users.get(callJob.getUserId()).sendMessage("success");

                    }
                }

            }
        }
        //Date updateTime = callJob.getUpdateTime();
        
//        MyJob myJob = myJobMapper.selectById(jobId);
//        MailUtils.sendText(myJob.getToWho(), "demo","预约提醒" );
//        XxlJobUtil.login("http://127.0.0.1:3010/xxl-job-admin","admin","123456");
//        XxlJobUtil.stopJob("http://127.0.0.1:3010/xxl-job-admin", (int) jobId);

    }


}
