package com.yqg.common.redis;


import com.yqg.common.config.RedisConfig;

/**
 * 基础的redis key
 * Created by gao on 2018/6/20.
 */
public enum BaseRedisKeyEnums implements IRedisKeyEnum {
    /**
     * 用户会话
     */
    USER_SESSION_KEY(RedisConfig.defaultExpireSeconds * 6*24),
    /**
     * 表级缓存
     */
    QUERY_TABLE_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 数据缓存
     */
    QUERY_CACHE_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 图片验证码
     */
    CAPTCHA_IMAGE_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 短信验证码-注册
     */
    CAPTCHA_REGISTER_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 短信验证码-登录
     */
    CAPTCHA_LOGIN_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 短信验证码-重置密码
     */
    CAPTCHA_PASSWORD_REST_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 系统维护信息
     */
    SYSTEM_MAINTAIN_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 分布式锁
     */
    LOCK_KEY(RedisConfig.defaultExpireSeconds),
    /**
     * 不做权限检查
     */
    FILTER_NOT_CHECK_AUTH_KEY(-1L),
    /**
     *  请求不需要解密
     */
    FILTER_NOT_DECRYPT_KEY(-1L),
    ;


    private String defaultKey;
    private String key;
    private Long expireSeconds;

    BaseRedisKeyEnums(Long expireSeconds) {
        this.defaultKey = this.name().replaceAll("_KEY", "").replaceAll("_", ":").toLowerCase();
        this.expireSeconds = expireSeconds;
    }

    /**
     * 查询默认key
     *
     * @return
     */
    public String getDefaultKey() {
        return this.defaultKey;
    }


    /**
     * 获取键值
     *
     * @return
     */
    public String getKey() {
        if (key == null) {
            key = defaultKey;
        }
        return key;
    }

    /**
     * 获取过期时间
     *
     * @return
     */
    public Long getExpireSeconds() {
        return expireSeconds;
    }


    @Override
    public BaseRedisKeyEnums appendToDefaultKey(String appendKey) {
        if (!appendKey.startsWith(":")) {
            appendKey = ":".concat(appendKey);
        }
        this.key = defaultKey.concat(appendKey);
        return this;
    }

    @Override
    public BaseRedisKeyEnums appendToCurrentKey(String appendKey) {
        if (!appendKey.startsWith(":")) {
            appendKey = ":".concat(appendKey);
        }
        this.key = key.concat(appendKey);
        return this;
    }

    @Override
    public BaseRedisKeyEnums replaceCurrentKey(String oldKeyPart, String newKeyPar) {
        this.key = key.replace(oldKeyPart, newKeyPar);
        return this;
    }
}
