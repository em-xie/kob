//package com.kob.backend.service.Imp;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.kob.backend.mapper.UserMapper;
//import com.kob.backend.domain.User;
//import com.kob.backend.service.Imp.utils.UserDetailsImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * @作者：xie
// * @时间：2022/11/7 13:10
// */
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserMapper userMapper;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        User user = userMapper.selectOne(queryWrapper);
//        if (user == null) {
//            throw new RuntimeException("用户不存在");
//        }
//
//        return new UserDetailsImpl(user);
//    }
//}
