package com.yqg.common.log.formatter;

import java.util.Map;

/**
 * Created by gao on 2017/9/10.
 */
public class LogMessgeInfo {

    private String level;
    private String clazz;
    private String timestamp;
    private String message;
    private Map extraPropertiesMap;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getExtraPropertiesMap() {
        return extraPropertiesMap;
    }

    public void setExtraPropertiesMap(Map extraPropertiesMap) {
        this.extraPropertiesMap = extraPropertiesMap;
    }
}
