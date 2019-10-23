package com.yqg.common.redis;

import com.alibaba.fastjson.JSON;
import com.yqg.common.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * Created by gao on 2018/6/19.
 */
@Component
public class RedisUtil {

    @Autowired
    protected RedisTemplate<String, String> redisTemplateForJSON;

    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    /**
     * 分页数据拆分存储key
     */
    private static final String PAGE_CONTENT = "page:ontent";
    private static final String PAGE_TOTAL = "page:total";

    private final static long LOCK_EXPIRE = 30 * 1000L;//单个业务持有锁的时间30s，防止死锁
    private final static long LOCK_TRY_INTERVAL = 30L;//默认30ms尝试一次
    private final static long LOCK_TRY_TIMEOUT = 20 * 1000L;//默认尝试20s

    /**
     * 添加对象
     *
     * @param keyEnum
     * @param object
     */
    public void set(IRedisKeyEnum keyEnum, Object object) {
        if (object instanceof String) {
            if (keyEnum.getExpireSeconds()!=-1) {
                redisTemplateForJSON.opsForValue().set(keyEnum.getKey(),
                        (String) object, keyEnum.getExpireSeconds(), TimeUnit.SECONDS);
            }else {
                redisTemplateForJSON.opsForValue().set(keyEnum.getKey(),
                        (String) object);
            }

        } else if (object instanceof Page) {
            Page page = (Page) object;
            set(keyEnum.appendToCurrentKey(PAGE_CONTENT), page.getContent());
            set(keyEnum.replaceCurrentKey(PAGE_CONTENT, PAGE_TOTAL), page.getTotalElements());
        } else {
            if (keyEnum.getExpireSeconds()!=-1) {
                redisTemplateForJSON.opsForValue().set(keyEnum.getKey(),
                        JSON.toJSONString(object), keyEnum.getExpireSeconds(), TimeUnit.SECONDS);
            }else {
                redisTemplateForJSON.opsForValue().set(keyEnum.getKey(),
                        JSON.toJSONString(object));
            }
        }

    }

    /**
     * 根据key获取value
     *
     * @param keyEnum 键
     * @return 值
     */
    public String get(IRedisKeyEnum keyEnum) {
        return redisTemplateForJSON.opsForValue().get(keyEnum.getKey());
    }

    /**
     * 根据key获取对象
     *
     * @param keyEnum
     * @return
     */
    public <T> T get(IRedisKeyEnum keyEnum, Class<T> clazz) {
        T t = null;
        String jsonString = redisTemplateForJSON.opsForValue().get(keyEnum.getKey());
        if (!StringUtils.isEmpty(jsonString))
            t = JSON.parseObject(jsonString, clazz);
        return t;
    }

    /**
     * 根据key获取List
     *
     * @param keyEnum
     * @return
     */
    public <T> List<T> getList(IRedisKeyEnum keyEnum, Class<T> clazz) {
        List<T> ts = null;
        String listJson = redisTemplateForJSON.opsForValue().get(keyEnum.getKey());
        if (!StringUtils.isEmpty(listJson)) {
            ts = JSON.parseArray(listJson, clazz);
        }
        return ts;
    }

    /**
     * 获取分页结果
     *
     * @param keyEnum
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Page<T> getPage(IRedisKeyEnum keyEnum, Class<T> clazz, Pageable pageable) {
        Page<T> page = null;
        List<T> content = getList(keyEnum.appendToCurrentKey(PAGE_CONTENT), clazz);
        Long totalElement = get(keyEnum.replaceCurrentKey(PAGE_CONTENT, PAGE_TOTAL), Long.class);
        if (!CollectionUtils.isEmpty(content)) {
            page = new PageImpl<T>(content, pageable, totalElement);
        }
        return page;
    }

    /**
     * 根据key删除
     *
     * @param keyEnum
     */
    public void delete(IRedisKeyEnum keyEnum) {
        redisTemplateForJSON.opsForValue().getOperations().delete(keyEnum.getKey());
    }



    /**
     * 追加到list
     *
     * @param keyEnum
     * @param value
     * @param repeatAble  重复值是否可以添加
     */
    public void listAppend(IRedisKeyEnum keyEnum, String value, boolean repeatAble) {
        if (repeatAble) {
            redisTemplateForJSON.opsForList().leftPush(keyEnum.getKey(), value);
        } else if (!listContain(keyEnum, value)) {
            redisTemplateForJSON.opsForList().leftPush(keyEnum.getKey(), value);
        }
    }

    public void listRemove(IRedisKeyEnum keyEnum, String value){
        redisTemplateForJSON.opsForList().remove(keyEnum.getKey(),1,value);
    }

    /**
     * 添加lsit
     *
     * @param keyEnum
     * @param list
     */
    public void listAddAll(IRedisKeyEnum keyEnum, List<String> list) {
        redisTemplateForJSON.opsForList().leftPushAll(keyEnum.getKey(), list);
    }

    /**
     * 获取list所有数据
     *
     * @param keyEnum
     * @return
     */
    public List<String> listGetAll(IRedisKeyEnum keyEnum) {
        Long size = redisTemplateForJSON.opsForList().size(keyEnum.getKey());
        return redisTemplateForJSON.opsForList().range(keyEnum.getKey(), 0, size);
    }

    /**
     * list是否包含value
     *
     * @param keyEnum
     * @param value
     * @return
     */
    public boolean listContain(IRedisKeyEnum keyEnum, String value) {
        List<String> list = listGetAll(keyEnum);
        for (String s : list) {
            if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void setAdd(IRedisKeyEnum keyEnum, String value){
        redisTemplateForJSON.opsForSet().add(keyEnum.getKey(),value);
    }

    public void setRemove(IRedisKeyEnum keyEnum, String value){
        redisTemplateForJSON.opsForSet().remove(keyEnum.getKey(),value);
    }

    public boolean setContain(IRedisKeyEnum keyEnum, String value){
      return   redisTemplateForJSON.opsForSet().isMember(keyEnum.getKey(),value);
    }

    public Set<String> setGetAll(IRedisKeyEnum keyEnum){
       return redisTemplateForJSON.opsForSet().members(keyEnum.getKey());
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Object lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock    锁的名称
     * @param timeout 获取超时时间 单位ms
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Object lock, long timeout) {
        return getLock(lock, timeout, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock        锁的名称
     * @param timeout     获取锁的超时时间
     * @param tryInterval 多少毫秒尝试获取一次
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Object lock, long timeout, long tryInterval) {
        return getLock(lock, timeout, tryInterval, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock           锁的名称
     * @param timeout        获取锁的超时时间
     * @param tryInterval    多少毫秒尝试获取一次
     * @param lockExpireTime 锁的过期
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Object lock, long timeout, long tryInterval, long lockExpireTime) {
        return getLock(lock, timeout, tryInterval, lockExpireTime);
    }


    /**
     * 操作redis获取全局锁
     *
     * @param lock           锁的名称
     * @param timeout        获取的超时时间
     * @param tryInterval    多少ms尝试一次
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean getLock(Object lock, long timeout, long tryInterval, long lockExpireTime) {
        try {
            if (lock == null) {
                return false;
            }
            long startTime = System.currentTimeMillis();
            String key = MD5Util.md5LowerCase(lock);
            key = BaseRedisKeyEnums.LOCK_KEY.appendToDefaultKey(key).getKey();
            do {
                if (!redisTemplateForJSON.hasKey(key)) {
                    redisTemplateForJSON.opsForValue().set(key, JSON.toJSONString(lock), lockExpireTime, TimeUnit.MILLISECONDS);
                    return true;
                } else {//存在锁
                    logger.debug("锁已存在,");
                }
                if (System.currentTimeMillis() - startTime > timeout) {//尝试超过了设定值之后直接跳出循环
                    return false;
                }
                Thread.sleep(tryInterval);
            }
            while (redisTemplateForJSON.hasKey(key));
        } catch (InterruptedException e) {
            logger.error("分布式锁获取异常{}", e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 释放锁
     */
    public void releaseLock(Object lock) {
        if (null != lock) {
            String key = MD5Util.md5LowerCase(lock);
            key = BaseRedisKeyEnums.LOCK_KEY.appendToDefaultKey(key).getKey();
            redisTemplateForJSON.opsForValue().getOperations().delete(key);
        } else {
            logger.error("分布式锁获释放异常,对象为null");
        }

    }
}
