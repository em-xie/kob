package com.kob.mathcingsystem.controller;

import com.kob.mathcingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @作者：xie
 * @时间：2022/11/13 11:02
 */
@RestController
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

   // 参数不能使用普通map，MultiValueMap和普通map的区别时，这个是一个键对应多个值
    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(userId,rating);
    }


    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}
