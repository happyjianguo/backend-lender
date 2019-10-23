package com.yqg.common.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 获取RedissonClient连接类
 * Created by gao on 2018/7/6.
 */
//@Component
public class RedissonConnector {

    @Autowired
    RedissonClient redisson;

    @PostConstruct
    public void init(){
        redisson = Redisson.create();
    }

    public RedissonClient getClient(){
        return redisson;
    }

}
