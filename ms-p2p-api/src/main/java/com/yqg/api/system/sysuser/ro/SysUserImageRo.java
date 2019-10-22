package com.yqg.api.system.sysuser.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import lombok.Data;

/**
 * Created by Lixiangjun on 2019/6/14.
 */
@Data
public class SysUserImageRo extends BaseSessionIdRo {

   //用户图片路径
   private String userImageUrl;

}
