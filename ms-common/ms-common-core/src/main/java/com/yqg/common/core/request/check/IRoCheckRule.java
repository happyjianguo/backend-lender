package com.yqg.common.core.request.check;

/**
 * 请求检查规则
 * Created by gao on 2018/6/18.
 */
public interface IRoCheckRule {


    /**
     *检查
     */
    void doCheck(Object request) throws Exception;
}
