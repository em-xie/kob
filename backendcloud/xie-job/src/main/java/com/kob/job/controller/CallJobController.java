package com.kob.job.controller;

import com.alibaba.fastjson.JSONObject;
import com.kob.common.core.domain.R;
import com.kob.job.domain.MyJob;
import com.kob.job.service.CallJobService;
import com.kob.job.util.XxlJobUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2023/2/2 22:45
 */
@RestController
@Slf4j
@RequestMapping("/call/job")
public class CallJobController {

    @Autowired
    private CallJobService callJobService;

    @PostMapping("/add")
    public R<Map<String, Object>> add(@Validated @RequestBody Map<String, String> data) {
        Map<String, Object> ajax = new HashMap<>();
        Map<String, String> res = callJobService.add(data);
        ajax.put("add",res);
        return  R.ok(ajax);
    }
}
