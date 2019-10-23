package com.yqg.common.mq;

import com.alibaba.fastjson.JSON;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.exceptions.BaseExceptionEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Kafka 消息发送服务端（多线程）
 * Created by gao on 2017/9/10.
 */

@Component
public class KafkaMessageService {
    private static AtomicInteger messageNo = new AtomicInteger(0);
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ExecutorService taskExecutor = new ThreadPoolExecutor(
            5,
            5,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(1024));

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 发送消息到管理端，发送失败自动重复
     *
     * @param baseMessage
     */
    public void pushToManager(String topic,final BaseMessage baseMessage) throws BusinessException {
        if(StringUtils.isEmpty(topic)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR.setCustomMessage("topic 不能为空"));
        }
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    kafkaTemplate.send(topic,JSON.toJSONString(baseMessage)); //发送消息
                    logger.info("发送消息:"+topic+"-"+JSON.toJSONString(baseMessage));
                } catch (Exception e) {
                    logger.warn("Kafka error, baseMessage: {}", e);
                }
            }
        });
    }
}

