package com.yqg.common.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.yqg.common.context.ApplicationContextProvider;
import com.yqg.common.log.formatter.JsonFormatter;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by gao on 2017/9/10.
 */
public class KafkaAppender  extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private KafkaTemplate<String,String> kafkaTemplate;
    private JsonFormatter jsonFormatter;

    @Override
    protected void append(ILoggingEvent eventObject) {
        this.init();
        this.kafkaTemplate.send("logTopic2",this.jsonFormatter.format(eventObject));
    }
    private void init(){
        if(kafkaTemplate==null) {
            kafkaTemplate = ApplicationContextProvider.getBean(KafkaTemplate.class);
            jsonFormatter=ApplicationContextProvider.getBean(JsonFormatter.class);
        }
    }


}
