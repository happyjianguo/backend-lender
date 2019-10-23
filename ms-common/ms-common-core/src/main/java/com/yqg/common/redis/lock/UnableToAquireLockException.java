package com.yqg.common.redis.lock;

/**
 * 获取锁异常类
 * Created by gao on 2018/7/6.
 */
public class UnableToAquireLockException extends RuntimeException{
    public UnableToAquireLockException() {
    }

    public UnableToAquireLockException(String message) {
        super(message);
    }

    public UnableToAquireLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
