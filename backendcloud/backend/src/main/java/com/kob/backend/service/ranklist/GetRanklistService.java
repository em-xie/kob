package com.kob.backend.service.ranklist;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者：xie
 * @时间：2022/11/15 13:25
 */
public interface GetRanklistService {
    JSONObject getList(Integer page);
}

