package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者：xie
 * @时间：2022/11/18 13:49
 */
public interface AcAppService {

    JSONObject applyCode();

    JSONObject receiveCode(String code, String state);
}