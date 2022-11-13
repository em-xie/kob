package com.kob.mathcingsystem;

import com.kob.mathcingsystem.service.MatchingService;
import com.kob.mathcingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @作者：xie
 * @时间：2022/11/13 10:56
 */
@SpringBootApplication
public class MatchingSystemApplication {

    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start();
        SpringApplication.run(MatchingSystemApplication.class, args);
    }

}