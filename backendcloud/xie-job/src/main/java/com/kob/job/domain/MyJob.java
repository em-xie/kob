package com.kob.job.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @作者：xie
 * @时间：2023/1/30 19:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("my_job")
public class MyJob  {
    @TableId(type = IdType.AUTO)
    private Integer id;				// 主键ID
    private String toWho;
    private int jobGroup;		// 执行器主键ID
    private String jobDesc;     // 备注
    private String jobCron;
    private Date addTime;
    private Date updateTime;
    private String author;		// 负责人
    private String alarmEmail;	// 报警邮件
    private String scheduleType;			// 调度类型
    private String scheduleConf;			// 调度配置，值含义取决于调度类型
    private String misfireStrategy;			// 调度过期策略
    private String executorRouteStrategy;	// 执行器路由策略
    private String executorHandler;		    // 执行器，任务Handler名称
    private String executorParam;		    // 执行器，任务参数
    private String executorBlockStrategy;	// 阻塞处理策略
    private int executorTimeout;     		// 任务执行超时时间，单位秒
    private int executorFailRetryCount;		// 失败重试次数
    private String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
    private String glueSource;		// GLUE源代码
    private String glueRemark;		// GLUE备注
    private Date glueUpdatetime;	// GLUE更新时间
    private String childJobid;		// 子任务ID，多个逗号分隔
    private int triggerStatus;		// 调度状态：0-停止，1-运行
    private long triggerLastTime;	// 上次调度时间
    private long triggerNextTime;	// 下次调度时间




}
