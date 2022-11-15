package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者：xie
 * @时间：2022/11/15 10:23
 */
public interface GetRecordListService {
    JSONObject getList(Integer page);
}
