package com.kob.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.job.domain.MyJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @作者：xie
 * @时间：2023/1/31 19:51
 */
@Mapper
public interface MyJobMapper extends BaseMapper<MyJob> {

    @Select("select id from my_job where job_desc=#{jobDesc}")
    MyJob selectIdByJobDesc(String jobDesc);
}
