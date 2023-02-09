package com.kob.job.service.utils.Impl;

import com.kob.job.domain.MyJob;
import com.kob.job.mapper.MyJobMapper;
import com.kob.job.service.utils.UtilsCallMeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2023/1/31 20:30
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UtilsCallMeServiceImpl implements UtilsCallMeService {
    private final MyJobMapper baseMapper;
    @Override
    public MyJob getId(String jobDesc) {
        return baseMapper.selectIdByJobDesc(jobDesc);
    }


}
