package com.kob.backend.service.user.account;

        import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:15
 */
public interface LoginService {

    public  Map<String,String> getToken(String username, String password);
}
