package com.kob.backend.mapper.oss;


import com.kob.backend.domain.oss.SysOssConfig;
import com.kob.backend.domain.oss.vo.SysOssConfigVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对象存储配置Mapper接口
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
@Mapper
public interface SysOssConfigMapper extends BaseMapperPlus<SysOssConfigMapper, SysOssConfig, SysOssConfigVo> {

}
