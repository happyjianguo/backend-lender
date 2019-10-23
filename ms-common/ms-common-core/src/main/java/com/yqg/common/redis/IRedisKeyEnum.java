package com.yqg.common.redis;

/**
 * redis Key 接口
 * Created by gao on 2018/06/19.
 */
public interface IRedisKeyEnum {
    /**
     * 获取key值
     * @return
     */
    String getKey();

    /**
     * 过期时间
     * @return
     */
    Long getExpireSeconds();

    /**
     * 追加key(在默认key后追加)
     * @param appendKey
     * @return
     */
    IRedisKeyEnum appendToDefaultKey(String appendKey);
    /**
     * 追加key(在当前key后追加)
     * @param appendKey
     * @return
     */
    IRedisKeyEnum appendToCurrentKey(String appendKey);
    /**
     * 替换当前key中的某部分
     * @param oldKeyPart
     * @param newKeyPar
     * @return
     */
    IRedisKeyEnum replaceCurrentKey(String oldKeyPart,String newKeyPar);


}
