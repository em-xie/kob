package com.xxl.job.admin.controller;
import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.cron.CronExpression;
import com.xxl.job.admin.core.model.CallMe.XxlJobQuery;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.thread.JobScheduleHelper;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.service.LoginService;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
/**
 * @作者：xie
 * @时间：2023/1/30 19:27
 */
@RestController
@RequestMapping("/api/jobinfo")
public class CallMeController {
    private static Logger logger = LoggerFactory.getLogger(CallMeController.class);
    @Autowired
    private XxlJobService xxlJobService;
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public Map<String, Object> pageList(@RequestBody XxlJobQuery xxlJobQuery) {
        return xxlJobService.pageList(
                xxlJobQuery.getStart(),
                xxlJobQuery.getLength(),
                xxlJobQuery.getJobGroup(),
                xxlJobQuery.getTriggerStatus(),
                xxlJobQuery.getJobDesc(),
                xxlJobQuery.getExecutorHandler(),
                xxlJobQuery.getAuthor());
    }
    @PostMapping("/save")
    public ReturnT<String> add(@RequestBody(required = true)XxlJobInfo jobInfo) {

        // next trigger time (5s后生效，避开预读周期)
        long nextTriggerTime = 0;

//        boolean scheduleDataNotChanged = jobInfo.getScheduleType().equals(exists_jobInfo.getScheduleType()) && jobInfo.getScheduleConf().equals(exists_jobInfo.getScheduleConf());
        try {
            Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
            if (nextValidTime == null) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_never_fire"));
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid")+" | "+ e.getMessage());
        }

        jobInfo.setTriggerStatus(1);
        jobInfo.setTriggerLastTime(0);
        jobInfo.setTriggerNextTime(nextTriggerTime);

        jobInfo.setUpdateTime(new Date());

        if(jobInfo.getId()==0){
            return xxlJobService.add(jobInfo);
        }else{
            return xxlJobService.update(jobInfo);
        }
    }
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ReturnT<String> delete(int id) {
        return xxlJobService.remove(id);
    }
    @RequestMapping(value = "/start",method = RequestMethod.GET)
    public ReturnT<String> start(int id) {
        return xxlJobService.start(id);
    }
    @RequestMapping(value = "/stop",method = RequestMethod.GET)
    public ReturnT<String> stop(int id) {
        return xxlJobService.stop(id);
    }
    @RequestMapping(value="login", method=RequestMethod.GET)
    @PermissionLimit(limit=false)
    public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){
        boolean ifRem = (ifRemember!=null && ifRemember.trim().length()>0 && "on".equals(ifRemember))?true:false;
        ReturnT<String> result= loginService.login(request, response, userName, password, ifRem);
        return result;
    }
}