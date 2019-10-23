package com.yqg.common.redis;

import com.alibaba.fastjson.JSON;
import com.yqg.common.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基础json格式Redis数据存取类,继承此类可以直接使用公共的方法,也可以使用redisTemplateForJSON自定义其他方法
 * @author gaohaiming
 * @date 2017-12-19
 * @param <T>
 */
@Repository
public abstract class BaseObjectRedisRepository<T> {


    private Class<T> entityClass;

    public BaseObjectRedisRepository() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Autowired
    protected RedisTemplate<String, String> redisTemplateForJSON;

    /**
     * 添加对象
     *
     * @param key
     * @param object
     * @param expireSeconds 过期时间（秒）
     */
    public void add(String key, T object, Long expireSeconds) {
        redisTemplateForJSON.opsForValue().set(key, JSON.toJSONString(object), expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 添加对象List
     *
     * @param key
     * @param object
     * @param expireSeconds
     */
    public void add(String key, List<T> object, Long expireSeconds) {
        redisTemplateForJSON.opsForValue().set(key, JSON.toJSONString(object), expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 添加对象默认过期时间（10分钟）
     *
     * @param key
     * @param object
     */
    public void add(String key, T object) {
        add(key, object, RedisConfig.defaultExpireSeconds);
    }

    /**
     * 添加对象List默认过期时间（10分钟）
     *
     * @param key
     * @param objectList
     */
    public void add(String key, List<T> objectList) {
        add(key, objectList, RedisConfig.defaultExpireSeconds);
    }

    /**
     * 根据key获取对象
     *
     * @param key
     * @return
     */
    public T get(String key) {
        T t = null;
        String userJson = redisTemplateForJSON.opsForValue().get(key);
        if (!StringUtils.isEmpty(userJson))
            t = JSON.parseObject(userJson, entityClass);
        return t;
    }

    /**
     * 根据key获取List
     *
     * @param key
     * @return
     */
    public List<T> getList(String key) {
        List<T> ts = null;
        String listJson = redisTemplateForJSON.opsForValue().get(key);
        if (!StringUtils.isEmpty(listJson)) {
            ts = JSON.parseArray(listJson, entityClass);
        }
        return ts;
    }

    /**
     * 根据key删除
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplateForJSON.opsForValue().getOperations().delete(key);
    }
}
