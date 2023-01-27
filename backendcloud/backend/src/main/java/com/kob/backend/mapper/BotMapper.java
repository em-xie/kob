package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.domain.Bot;
import com.kob.backend.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @作者：xie
 * @时间：2022/11/10 14:14
 */
@Mapper
public interface BotMapper extends BaseMapper<Bot> {


}
