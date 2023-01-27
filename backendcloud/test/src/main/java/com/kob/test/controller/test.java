package com.kob.test.controller;

import com.kob.common.core.domain.PageQuery;
import com.kob.common.core.domain.R;
import com.kob.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者：xie
 * @时间：2023/1/25 13:43
 */

@RestController
public class test {
    @GetMapping("/list")
    public R<Void> genList() {
        return R.ok();
    }
}
