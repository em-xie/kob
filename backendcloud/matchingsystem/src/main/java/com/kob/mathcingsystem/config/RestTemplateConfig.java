package com.kob.mathcingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @作者：xie
 * @时间：2022/11/13 11:31
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}