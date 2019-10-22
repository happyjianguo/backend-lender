package com.yqg.api.user.enums;

import com.yqg.common.config.RedisConfig;
import com.yqg.common.redis.IRedisKeyEnum;

/**
 * Created by Lixiangjun on 2019/5/17.
 */
public enum UserRedisKeyEnums implements IRedisKeyEnum {

    USER_SESSION_SMS_KEY_COUNT(RedisConfig.defaultExpireSeconds*6*24),

    USER_REFUND_PASSWORD_KEY(Long.valueOf(-1L));

    private String defaultKey = this.name().replaceAll("_KEY", "").replaceAll("_", ":").toLowerCase();
    private String key;
    private Long expireSeconds;

    private UserRedisKeyEnums(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getDefaultKey() {
        return this.defaultKey;
    }

    public String getKey() {
        if(this.key == null) {
            this.key = this.defaultKey;
        }

        return this.key;
    }

    public Long getExpireSeconds() {
        return this.expireSeconds;
    }

    public UserRedisKeyEnums appendToDefaultKey(String appendKey) {
        if(!appendKey.startsWith(":")) {
            appendKey = ":".concat(appendKey);
        }

        this.key = this.defaultKey.concat(appendKey);
        return this;
    }

    public UserRedisKeyEnums appendToCurrentKey(String appendKey) {
        if(!appendKey.startsWith(":")) {
            appendKey = ":".concat(appendKey);
        }

        this.key = this.key.concat(appendKey);
        return this;
    }

    public UserRedisKeyEnums replaceCurrentKey(String oldKeyPart, String newKeyPar) {
        this.key = this.key.replace(oldKeyPart, newKeyPar);
        return this;
    }
}
