package com.yqg.user.service.usermessage.impl;

import com.yqg.api.user.enums.MessageTypeEnum;
import com.yqg.api.user.useruser.ro.MessageListRo;
import com.yqg.api.user.useruser.ro.MessageRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.UserMessageDao;
import com.yqg.user.entity.UserMessage;
import com.yqg.user.entity.UserUser;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.usermessage.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lixiangjun on 2019/5/20.
 */
@Service("userMessageService")
public class UserMessageServiceImpl extends UserCommonServiceImpl implements UserMessageService {

    @Autowired
    UserMessageDao userMessageDao;


    @Override
    public BasePageResponse<UserMessage> getMessageList(MessageListRo ro) throws BusinessException {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserUuid(ro.getUserId());
        userMessage.setDisabled(0);
        userMessage.setStatus(0);
        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);

        Page<UserMessage> userMessagePage = userMessageDao.findForPage(userMessage, ro.convertPageRequest());

        BasePageResponse<UserMessage> response = new BasePageResponse<>(userMessagePage);
        response.setContent(userMessagePage.getContent());

        return response;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserMessage(MessageRo ro) throws BusinessException {
        MessageTypeEnum messageTypeEnum = ro.getMessageTypeEnum();
        if(messageTypeEnum!=null){
            UserMessage userMessage = new UserMessage();
            String content = "";
            if(StringUtils.isEmpty(ro.getContent())){
                content = messageTypeEnum.getContent();
            }else {
                content = ro.getContent();
            }
            String templateName = ro.getMessageTypeEnum().getTemplateId();
            userMessage.setUserUuid(ro.getUserId());
            userMessage.setStatus(0);
            userMessage.setContent(content);
            userMessage.setTitle(templateName);
            userMessageDao.addOne(userMessage);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserMessage(MessageListRo ro) throws BusinessException {
        String messageIds = ro.getMessageIds();
        if(!StringUtils.isEmpty(messageIds)){
            String[] strings = messageIds.split(",");
            for(int i=0;i<strings.length;i++){
                UserMessage userMessage = userMessageDao.findOne(strings[i]);
                userMessage.setStatus(1);
                userMessageDao.updateOne(userMessage);
            }
        }

    }

    @Override
    public String addOne(UserMessage userMessage) throws BusinessException {
        return userMessageDao.addOne(userMessage);
    }

    @Override
    public UserMessage findById(String s) throws BusinessException {
        return this.userMessageDao.findOneById(s, new UserMessage());

    }

    @Override
    public <E> E findById(String s, Class<E> aClass) throws BusinessException {
        UserMessage userMessage = findById(s);
        E bo = BeanCoypUtil.copyToNewObject(userMessage, aClass);
        return bo;

    }

    @Override
    public UserMessage findOne(UserMessage userMessage) throws BusinessException {
        return this.userMessageDao.findOne(userMessage);

    }

    @Override
    public <E> E findOne(UserMessage userMessage, Class<E> aClass) throws BusinessException {
        UserMessage entity =  findOne(userMessage);
        E bo = BeanCoypUtil.copyToNewObject(entity, aClass);
        return bo;
    }

    @Override
    public List<UserMessage> findList(UserMessage userMessage) throws BusinessException {
        return this.userMessageDao.findForList(userMessage);
    }

    @Override
    public <E> List<E> findList(UserMessage entity, Class<E> aClass) throws BusinessException {
        List<UserMessage> userMessageList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UserMessage useruser : userMessageList) {
            E bo = BeanCoypUtil.copyToNewObject(useruser, aClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    public void updateOne(UserMessage userMessage) throws BusinessException {
        userMessageDao.updateOne(userMessage);
    }

    @Override
    public void deleteById(String s) throws BusinessException {
        userMessageDao.delete(s);
    }

    @Override
    public void deleteOne(UserMessage userMessage) throws BusinessException {
        userMessageDao.deleteOne(userMessage);
    }
}
