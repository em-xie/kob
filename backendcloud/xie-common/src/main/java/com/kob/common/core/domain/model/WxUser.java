package com.kob.common.core.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @作者：xie
 * @时间：2022/8/27 14:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class WxUser extends LoginUser {
    /**
     * id
     */

    private Long id;
    /**
     * 父id
     */
    private Long pid;
    /**
     * unionid
     */
    private String unionid;
    /**
     * openid
     */
    private String openid;
    /**
     * 用户类型
     */
    private String utype;
    /**
     * 手机
     */
    private String tel;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private String sex;
    /**
     * 城市
     */
    private String city;
    /**
     * 地址
     */
    private String address;
    /**
     * 积分
     */
    private Long credits;
    /**
     * 券数
     */
    private Long tickets;
    /**
     * 余额
     */
    private Long balance;
    /**
     * 身份
     */
    private String identity;
    /**
     * 最近登录
     */
    private Date lastLoginTime;
    /**
     * 备注
     */
    private String remark;
}
