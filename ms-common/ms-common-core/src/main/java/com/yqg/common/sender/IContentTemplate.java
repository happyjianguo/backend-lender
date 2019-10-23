package com.yqg.common.sender;

/**
 * Created by gao on 2018/6/23.
 */
public interface IContentTemplate {

    /**
     * 获取模板编号
     * @return
     */
    String getTemplateId();

    /**
     * 模板名称
     * @return
     */
    String getTemplateName();
    /**
     * 获取内容
     * @return
     */
    String getContent(String...params);
}
