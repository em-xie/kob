package com.kob.backend.service.oss;

import com.kob.backend.domain.oss.SysOss;
import com.kob.backend.domain.oss.bo.SysOssBo;
import com.kob.backend.domain.oss.vo.SysOssVo;
import com.kob.common.core.domain.PageQuery;

import com.kob.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author Lion Li
 */
public interface ISysOssService {

    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    List<SysOssVo> listByIds(Collection<Long> ossIds);

    SysOss getById(Long ossId);

    SysOss upload(MultipartFile file);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
