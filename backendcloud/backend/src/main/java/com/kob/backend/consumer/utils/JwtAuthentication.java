//package com.kob.backend.consumer.utils;
//
//import com.kob.backend.utils.JwtUtil;
//import io.jsonwebtoken.Claims;
//
///**
// * @作者：xie
// * @时间：2022/11/11 15:02
// */
//public class JwtAuthentication {
////    静态函数，在外面能用
//    public static Integer getUserId(String token) {
//        int userId = -1; //-1表示不存在
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userId = Integer.parseInt(claims.getSubject());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return userId;
//    }
//}
//
