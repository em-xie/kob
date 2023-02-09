package com.kob.job.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kob.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @作者：xie
 * @时间：2023/2/2 22:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("call_job")
public class CallJob  {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String toWho;
    private String finalTime;
    private Date createTime;
    private Date updateTime;
    private String status;
    private Integer userId;
}
