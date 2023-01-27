package com.kob.oss.factory;

import com.kob.common.utils.JsonUtils;
import com.kob.common.utils.StringUtils;
import com.kob.common.utils.redis.RedisUtils;
import com.kob.oss.constant.OssConstant;
import com.kob.oss.core.OssClient;
import com.kob.oss.exception.OssException;
import com.kob.oss.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传Factory
 *
 * @author Lion Li
 */
@Slf4j
public class OssFactory {

    private static final    Map<String, OssClient> CLIENT_CACHE = new ConcurrentHashMap<>();

    /**
     * 初始化工厂
     */
    public static void init() {
        log.info("初始化OSS工厂");
        RedisUtils.subscribe(OssConstant.CACHE_CONFIG_KEY, String.class, configKey -> {
            OssClient client = getClient(configKey);
            // 未初始化不处理
            if (client != null) {
                refresh(configKey);
                log.info("订阅刷新OSS配置 => " + configKey);
            }
        });
    }

    /**
     * 获取默认实例
     */
    public static OssClient instance() {
        // 获取redis 默认类型
        String configKey = RedisUtils.getCacheObject(OssConstant.CACHE_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        return instance(configKey);
    }

    /**
     * 根据类型获取实例
     */
    public static OssClient instance(String configKey) {
        OssClient client = getClient(configKey);
        if (client == null) {
            refresh(configKey);
            return getClient(configKey);
        }
        return client;
    }

    private static void refresh(String configKey) {
        Object json = RedisUtils.getCacheObject(OssConstant.SYS_OSS_KEY + configKey);
        OssProperties properties = JsonUtils.parseObject(json.toString(), OssProperties.class);
        if (properties == null) {
            throw new OssException("系统异常, '" + configKey + "'配置信息不存在!");
        }
        CLIENT_CACHE.put(configKey, new OssClient(configKey, properties));
    }

    private static OssClient getClient(String configKey) {
        return CLIENT_CACHE.get(configKey);
    }

}
