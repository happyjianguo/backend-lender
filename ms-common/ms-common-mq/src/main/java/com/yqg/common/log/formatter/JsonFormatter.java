package com.yqg.common.log.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JsonFormatter implements Formatter {

  private boolean expectJsonMessage = false;
  private boolean includeMethodAndLineNumber = false;
  private Map extraPropertiesMap = null;
  SimpleDateFormat dateTimeFormat= new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public String format(ILoggingEvent event) {
    LogMessgeInfo logMessgeInfo=new LogMessgeInfo();
    logMessgeInfo.setTimestamp(dateTimeFormat.format(new Date()));
    logMessgeInfo.setClazz(event.getLoggerName());
    logMessgeInfo.setLevel(event.getLevel().levelStr);
    logMessgeInfo.setExtraPropertiesMap(extraPropertiesMap);
    logMessgeInfo.setMessage(event.getFormattedMessage());

    return JSON.toJSONString(logMessgeInfo);
  }

  public boolean getExpectJsonMessage() {
    return expectJsonMessage;
  }

  public void setExpectJsonMessage(boolean expectJsonMessage) {
    this.expectJsonMessage = expectJsonMessage;
  }

  public boolean getIncludeMethodAndLineNumber() {
    return includeMethodAndLineNumber;
  }

  public void setIncludeMethodAndLineNumber(boolean includeMethodAndLineNumber) {
    this.includeMethodAndLineNumber = includeMethodAndLineNumber;
  }

  public void setExtraProperties(String thatExtraProperties) {
    final Properties properties = new Properties();
    try {
      properties.load(new StringReader(thatExtraProperties));
      Enumeration enumeration = properties.propertyNames();
      extraPropertiesMap = new HashMap();
      while(enumeration.hasMoreElements()){
        String name = (String)enumeration.nextElement();
        String value = properties.getProperty(name);
        extraPropertiesMap.put(name,value);
      }
    } catch (IOException e) {
      System.out.println("There was a problem reading the extra properties configuration: "+e.getMessage());
      e.printStackTrace();
    }
  }
}