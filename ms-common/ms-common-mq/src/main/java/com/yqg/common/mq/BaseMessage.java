package com.yqg.common.mq;

import lombok.Data;

import java.util.Date;

/**
 * 消息对象
 * Created by gao on 2017/9/10.
 */
@Data
public class BaseMessage<T> {
    /**
     * 编号
     */
    private Long id;
    /**
     * 消息
     */
    private String msg;
    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 消息内容
     */
    private T data;
}
