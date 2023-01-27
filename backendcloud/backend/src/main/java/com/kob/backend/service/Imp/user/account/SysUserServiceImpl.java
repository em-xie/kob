package com.kob.backend.service.Imp.user.account;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.domain.User;
import com.kob.backend.service.user.account.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2023/1/6 16:10
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl implements ISysUserService {

    private final UserMapper baseMapper;

    @Override
    public User selectUserByUserName(String username) {

        return baseMapper.selectUserByUserName(username);
    }
}
