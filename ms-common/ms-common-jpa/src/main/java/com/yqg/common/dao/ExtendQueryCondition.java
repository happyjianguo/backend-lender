package com.yqg.common.dao;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展查询条件
 * Created by gao on 2018/6/21.
 */
@Data
public class ExtendQueryCondition {
    /**
     * 比较类型GT大于,GTE大于等于,LT小于,LTE小于等于,NE不等于
     */
    public enum CompareType {
        GT, GTE, LT, LTE, NE,GT_TIME,GTE_TIME,LT_TIME,LTE_TIME
    }
    //TODO not in 查询 ,日期范围查询
    /**
     * 需要精确查询的字段
     */
    private List<String> likeQueyFields = new ArrayList<>();

    /**
     * in查询的key字段,value值list
     */
    private Map<String, List<Object>> inQueryMap = new HashMap<>();
    /**
     * not in查询的key字段,value值list
     */
    private Map<String, List<Object>> notInQueryMap = new HashMap<>();

    /**
     * 比较查询,大于,小于,不等于
     */
    private Map<String, Map<CompareType, Object>> compareQueryMap = new HashMap<>();

    /**
     * 增加一个指定模糊查询字段
     *
     * @param fieldName
     */
    public void addLikeQueyField(String fieldName) {
        likeQueyFields.add(fieldName);
    }

    /**
     * 增加一个in查询字段和值list
     *
     * @param fieldName
     * @param inValueList
     */
    public void addInQueryMap(String fieldName, List<Object> inValueList) {
        inQueryMap.put(fieldName, inValueList);
    }

    /**
     * 增加一个not in查询字段和值list
     *
     * @param fieldName
     * @param notInValueList
     */
    public void addNotInQueryMap(String fieldName, List<Object> notInValueList) {
        notInQueryMap.put(fieldName, notInValueList);
    }

    /**
     * 增加一个比较查询信息
     *
     * @param fieldName
     * @param compareInfo
     */
    public void addCompareQueryMap(String fieldName, Map<CompareType, Object> compareInfo) {
        compareQueryMap.put(fieldName, compareInfo);
    }

}
