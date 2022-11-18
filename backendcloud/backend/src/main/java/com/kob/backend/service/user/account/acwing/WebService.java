package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者：xie
 * @时间：2022/11/18 13:51
 */
public interface WebService {

    JSONObject applyCode();

    JSONObject receiveCode(String code, String state);
}