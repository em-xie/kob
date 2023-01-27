package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import com.kob.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 15:01
 */
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info/")
    public R<Map<String, Object>> getInfo() {

        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user",infoService.getInfo());
        return R.ok(ajax);
    }
}
