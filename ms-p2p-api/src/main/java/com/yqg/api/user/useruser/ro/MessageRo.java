package com.yqg.api.user.useruser.ro;

import com.yqg.api.user.enums.MessageTypeEnum;
import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Lixiangjun on 2019/5/20.
 */
@Data
public class MessageRo {

    @ApiModelProperty(value = "标题", required = true)
    private String title;

    @ApiModelProperty(value = "是否已读", required = true)
    private Integer status;

    @ApiModelProperty(value = "消息id", required = true)
    private String messageIds;

    //消息类型
    MessageTypeEnum messageTypeEnum;

    @ApiModelProperty(value = "消息内容", required = true)
    private String content;

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;


}
