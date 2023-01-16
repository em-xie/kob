package com.kob.backend;

import com.kob.backend.service.Imp.oss.SysOssConfigServiceImpl;
import com.kob.backend.service.oss.ISysOssConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BackendApplication {



    public static void main(String[] args) {


        SpringApplication.run(BackendApplication.class, args);


    }

}
