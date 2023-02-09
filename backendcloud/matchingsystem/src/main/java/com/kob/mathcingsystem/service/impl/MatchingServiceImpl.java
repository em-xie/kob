package com.kob.mathcingsystem.service.impl;

import com.kob.mathcingsystem.service.MatchingService;
import com.kob.mathcingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/13 10:58
 */
@Service
public class MatchingServiceImpl implements MatchingService {

    //全局只有一个线程池 设置为静态
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating,Integer botId) {
       // System.out.println("add player:" +userId+ " " +rating);
        matchingPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        //System.out.println("remove player:" + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
