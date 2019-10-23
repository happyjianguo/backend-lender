package com.yqg.common.dynamicdata;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * Created by gao on 2018/6/28.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {

        //可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();
        System.out.println("------------lookupKey---------"+lookupKey);

        return lookupKey;
    }
}
