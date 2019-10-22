package com.yqg.common.enums;


import com.yqg.common.config.RedisConfig;
import com.yqg.common.redis.IRedisKeyEnum;

/**
 * 基础的redis key
 * Created by huanhuan on 2019/5/10.
 */
public enum RedisKeyEnums implements IRedisKeyEnum {
    /**
     * 用户购物车
     */
    USER_CART_KEY(-1L),

    /**
     * 一键渐入购物车，查询缓存
     */
    SCATTERSTANDARD_QUERY_CACHE_KEY(-1L),

    PAY_EXPRIRESECONDS(RedisConfig.defaultExpireSeconds*3),//30分钟

    /**
     * 机构投资人清分收入总计
     * 过期时间 25h
     */
    BRANCH_DISBURSE_SUM_KEY(RedisConfig.defaultExpireSeconds*6*48),
    ;

    private String defaultKey = this.name().replaceAll("_KEY", "").replaceAll("_", ":").toLowerCase();
    private String key;
    private Long expireSeconds;

    private RedisKeyEnums(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
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
    public RedisKeyEnums appendToDefaultKey(String appendKey) {
        if (!appendKey.startsWith(":")) {
            appendKey = ":".concat(appendKey);
        }
        this.key = defaultKey.concat(appendKey);
        return this;
    }

    @Override
    public RedisKeyEnums appendToCurrentKey(String appendKey) {
        if (!appendKey.startsWith(":")) {
            appendKey = ":".concat(appendKey);
        }
        this.key = key.concat(appendKey);
        return this;
    }

    @Override
    public RedisKeyEnums replaceCurrentKey(String oldKeyPart, String newKeyPar) {
        this.key = key.replace(oldKeyPart, newKeyPar);
        return this;
    }
}
