package com.yqg.common.redis.lock;

/**
 * 获取锁后需要处理的逻辑
 * Created by gao on 2018/7/6.
 */
public interface AquiredLockWorker<T> {
    T invokeAfterLockAquire() throws Exception;
}
