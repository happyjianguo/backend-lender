package com.yqg.user.service.usermessage;

import com.yqg.api.user.useruser.ro.MessageListRo;
import com.yqg.api.user.useruser.ro.MessageRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserMessage;

import java.util.List;

/**
 * Created by Lixiangjun on 2019/5/20.
 */
public interface UserMessageService extends BaseService<UserMessage> {

    /**
     * 获取用户通知消息列表
     * @param ro
     * @return
     * @throws Exception
     */
    BasePageResponse<UserMessage> getMessageList(MessageListRo ro)throws BusinessException;


    /**
     * 发送通知消息
     * @param ro
     * @throws BusinessException
     */
    void addUserMessage(MessageRo ro)throws BusinessException;

    /**
     * 通知消息已读
     * @param ro
     * @throws BusinessException
     */
    void updateUserMessage(MessageListRo ro)throws BusinessException;
}
