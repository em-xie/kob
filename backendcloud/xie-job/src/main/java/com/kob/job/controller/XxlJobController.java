package com.kob.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.kob.common.core.domain.R;
import com.kob.job.domain.MyJob;
import com.kob.job.mapper.MyJobMapper;
import com.kob.job.service.CallMeService;
import com.kob.job.util.XxlJobUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2023/1/30 19:46
 */

@RestController
@Slf4j
@RequestMapping("/job/api")
public class XxlJobController {

    @Autowired
    private MyJobMapper myJobMapper;
    @Autowired
    private CallMeService callMeService;


    @RequestMapping(value = "/pageList",method = RequestMethod.GET)
    public Object pageList() throws IOException {
        //int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author
        JSONObject test=new JSONObject();
        test.put("length",10);
        XxlJobUtil.login("http://127.0.0.1:3010/xxl-job-admin","admin","123456");
        JSONObject response = XxlJobUtil.pageList("http://127.0.0.1:3010/xxl-job-admin", test);
        return  response.get("data");
    }

    @GetMapping("/add")
    public R<Void> add(@RequestParam Map<String, String> data) throws Exception {
        String to = data.get("to");
        String jobDesc = data.get("jobDesc");
        MyJob myJob =new MyJob();
        myJob.setJobCron("0/5 * * * * ?");
        myJob.setJobGroup(1);
        myJob.setJobDesc(jobDesc);
        myJob.setAddTime(new Date());
        myJob.setUpdateTime(new Date());
        myJob.setAuthor("xie");
        myJob.setAlarmEmail("");
        myJob.setScheduleType("CRON");
        myJob.setScheduleConf("0/20 * * * * ?");
        myJob.setMisfireStrategy("DO_NOTHING");
        myJob.setExecutorRouteStrategy("FIRST");
        myJob.setExecutorHandler("CallMeHandler");
        myJob.setExecutorParam("att");
        myJob.setExecutorBlockStrategy("SERIAL_EXECUTION");
        myJob.setExecutorTimeout(0);
        myJob.setExecutorFailRetryCount(1);
        myJob.setGlueType("BEAN");
        myJob.setGlueSource("");
        myJob.setGlueRemark("GLUE代码初始化");
        myJob.setGlueUpdatetime(new Date());
        JSONObject test = (JSONObject) JSONObject.toJSON(myJob);
        XxlJobUtil.login("http://127.0.0.1:3010/xxl-job-admin","admin","123456");
        JSONObject response = XxlJobUtil.addJob("http://127.0.0.1:3010/xxl-job-admin", test);

        myJob.setId((Integer) response.get("content"));
        myJob.setToWho(to);
        myJob.setTriggerLastTime(0);
        myJob.setTriggerNextTime(0);
        myJob.setChildJobid("");


        if (response.containsKey("code") && 200 == (Integer) response.get("code")) {
            log.info("新增成功");

            myJob.setId(response.getInteger("content"));
            myJobMapper.insert(myJob);

            return  R.ok();
        } else {
            log.info("新增失败");
            return R.fail();
        }


    }

    @RequestMapping(value = "/stop/{jobId}",method = RequestMethod.GET)
    public void stop(@PathVariable("jobId") Integer jobId) throws IOException {

        XxlJobUtil.login("http://127.0.0.1:3010/xxl-job-admin","admin","123456");
        JSONObject response = XxlJobUtil.stopJob("http://127.0.0.1:3010/xxl-job-admin", jobId);
        if (response.containsKey("code") && 200 == (Integer) response.get("code")) {
            System.out.println("任务停止成功");
        } else {
            System.out.println("任务停止失败");
        }
    }

    @RequestMapping(value = "/delete/{jobId}",method = RequestMethod.GET)
    public void delete(@PathVariable("jobId") Integer jobId) throws IOException {

        XxlJobUtil.login("http://127.0.0.1:3010/xxl-job-admin","admin","123456");
        JSONObject response = XxlJobUtil.deleteJob("http://127.0.0.1:3010/xxl-job-admin", jobId);
        if (response.containsKey("code") && 200 == (Integer) response.get("code")) {
            System.out.println("任务移除成功");
        } else {
            System.out.println("任务移除失败");
        }
    }


    @RequestMapping(value = "/start/{jobId}",method = RequestMethod.GET)
    public void start(@PathVariable("jobId") Integer jobId) throws IOException {

        XxlJobUtil.login("http://127.0.0.1:3010/xxl-job-admin","admin","123456");
        JSONObject response = XxlJobUtil.startJob("http://127.0.0.1:3010/xxl-job-admin", jobId);
        if (response.containsKey("code") && 200 == (Integer) response.get("code")) {
            System.out.println("任务启动成功");
        } else {
            System.out.println("任务启动失败");
        }
    }

}
