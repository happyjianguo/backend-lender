package com.yqg.api.user.useruser.bo;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 图片验证码
 * Created by gao on 2018/6/29.
 */
@Data
public class ImageCaptchBo {
    /**
     * 验证码会话id
     */
    String imgSessionId;
    /**
     * 验证码会话id
     */
    @JSONField(serialize=false)
    String imgBase64;
}
