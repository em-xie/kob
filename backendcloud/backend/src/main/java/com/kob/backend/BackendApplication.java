package com.kob.backend;

import com.kob.backend.service.Imp.oss.SysOssConfigServiceImpl;
import com.kob.backend.service.oss.ISysOssConfigService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.kob.backend","com.kob.generator","com.kob.job","com.kob.sms"})
public class BackendApplication {



    public static void main(String[] args) {


        SpringApplication.run(BackendApplication.class, args);


    }

}
