//package com.kob.backend.utils;
//
//import cn.dev33.satoken.stp.StpUtil;
//import com.kob.backend.mapper.UserMapper;
//import com.kob.backend.pojo.User;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
///**
// * @作者：xie
// * @时间：2022/11/10 14:24
// */
//public class UserUtil {
//    @Autowired
//    private UserMapper userMapper;
//
//    public static User getUser() {
//
//        Integer loginId = StpUtil.getLoginIdAsInt();
//        userMapper.selectById(loginId);
//        return loginUser.getUser();
//
//    }
//
//
//}
