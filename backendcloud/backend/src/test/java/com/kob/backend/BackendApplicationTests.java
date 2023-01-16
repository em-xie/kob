package com.kob.backend;

import cn.dev33.satoken.stp.StpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {

        System.out.println(StpUtil.getTokenValue());
    }

}
