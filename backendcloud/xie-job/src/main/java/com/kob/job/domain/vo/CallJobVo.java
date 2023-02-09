package com.kob.job.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @作者：xie
 * @时间：2023/2/2 22:29
 */
@Data
public class CallJobVo {
    private static final long serialVersionUID = 1L;

    /**
     * 对象存储主键
     */
    private Integer id;
    private String toWho;
    private String finalTime;
    private Date createTime;
    private Date updateTime;
    private int status;
}
