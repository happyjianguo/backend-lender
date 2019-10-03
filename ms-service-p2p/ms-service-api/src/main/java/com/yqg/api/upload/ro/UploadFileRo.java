package com.yqg.api.upload.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图片上传信息
 * Created by gao on 2018/7/2.
 */
@Data
public class UploadFileRo extends BaseSessionIdRo {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "文件类型", required = true)
    String fileType;

}
