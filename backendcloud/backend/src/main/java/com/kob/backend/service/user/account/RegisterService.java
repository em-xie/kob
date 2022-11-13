package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:16
 */
public interface RegisterService {
    public Map<String,String> register(String username, String password ,String confirmedPassword);
}
