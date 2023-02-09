package com.kob.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.common.core.mapper.BaseMapperPlus;
import com.kob.job.domain.CallJob;
import com.kob.job.domain.vo.CallJobVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @作者：xie
 * @时间：2023/2/2 22:35
 */
public interface CallJobMapper extends BaseMapper<CallJob> {

//    @Select("select update_time from call_job ")
//    CallJob updateTime();

    public List<CallJob>  selectByStatus();

    public int UpdateStatus(CallJob callJob);

    public int UpdateUpdateTime(CallJob callJob);
}
