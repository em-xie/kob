package com.kob.backend.mapper.oss;

import com.kob.backend.domain.oss.SysOss;
import com.kob.backend.domain.oss.vo.SysOssVo;
import com.kob.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文件上传 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysOssMapper extends BaseMapperPlus<SysOssMapper, SysOss, SysOssVo> {
}
