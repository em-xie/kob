package com.kob.backend.service.Imp.record;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * @作者：xie
 * @时间：2022/11/15 10:25
 */
@Service
public class GetRecordListServiceImpl implements GetRecordListService {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {

//        JSONObject resp1 = new JSONObject();
//        if(RedisUtils.hasKey("records_count"))
//        {
//
//            String item1 = JsonUtils.toJsonString((RedisUtils.getCacheObject("records")));
//            resp1.put("records",item1);
//            String count = JsonUtils.toJsonString(RedisUtils.getCacheObject("records_count"));
//            resp1.put("records_count",count);
//            return resp1;
//        }else {
            IPage<Record> recordIPage = new Page<>(page, 10);
            QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
            JSONObject resp = new JSONObject();

            List<JSONObject> items = new LinkedList<>();
            for (Record record : records) {
                User userA = userMapper.selectById(record.getAId());
                User userB = userMapper.selectById(record.getBId());
                JSONObject item = new JSONObject();
                item.put("a_photo", userA.getPhoto());
                item.put("a_username", userA.getUsername());
                item.put("b_photo", userB.getPhoto());
                item.put("b_username", userB.getUsername());
                item.put("record", record);
                String result = "平局";
                if ("A".equals(record.getLoser())) result = "B胜";
                else if ("B".equals(record.getLoser())) result = "A胜";
                item.put("result", result);
                items.add(item);

            }
            resp.put("records", items);
            //RedisUtils.setCacheObject("records" ,items, Duration.ofMinutes(Constants.RECORD_EXPIRATION));
            Long count = recordMapper.selectCount(null);
            //RedisUtils.setCacheObject("records_count", count,Duration.ofMinutes(Constants.RECORD_EXPIRATION));
            resp.put("records_count", count);

            return resp;
        }

}
