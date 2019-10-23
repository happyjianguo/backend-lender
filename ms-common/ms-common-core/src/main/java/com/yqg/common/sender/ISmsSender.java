package com.yqg.common.sender;

/**
 * 短信发送工具接口
 * Created by gao on 2018/6/23.
 */
public interface ISmsSender {
    /**
     * 发送
     *
     * @param mobileNumber 短信验证码
     * @param template     内容模板
     * @param params       内容参数
     */
    void send(String mobileNumber, IContentTemplate template, String... params);

}
