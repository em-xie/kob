package com.kob.backend.utils;

import com.kob.backend.pojo.User;
import com.kob.backend.service.Imp.utils.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @作者：xie
 * @时间：2022/11/10 14:24
 */
public class UserUtil {
    public static User getUser() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        return loginUser.getUser();
    }


}
