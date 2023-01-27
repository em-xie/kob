package com.kob.backend.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @作者：xie
 * @时间：2022/11/10 14:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bot")
public class Bot {
    @TableId(type = IdType.AUTO)
    private Integer id; //在pojo里最好用Integer，否则会报警告
    private Integer userId; //pojo里要用驼峰命名法和数据库的下划线对应
    private String title;
    private String description;
    private String content;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date modifytime;


}
