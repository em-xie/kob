package com.kob.backend.service.user.account;

import com.kob.backend.domain.User;

/**
 * @作者：xie
 * @时间：2023/1/6 16:08
 */
public interface ISysUserService {
    User selectUserByUserName(String userName);
}
