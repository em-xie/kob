package com.kob.mathcingsystem.service;

/**
 * @作者：xie
 * @时间：2022/11/13 10:56
 */
public interface MatchingService {
    String addPlayer(Integer userId, Integer rating);
    String removePlayer(Integer userId);
}
