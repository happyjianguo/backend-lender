package com.yqg.api.upload.ro;

import com.alibaba.fastjson.annotation.JSONField;
import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Base64编码文件上传请求对象
 */
@Data
public class UploadBase64FileRo extends UploadFileRo {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "base64编码数据", required = true)
    @JSONField(serialize=false)
    private String base64Data;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "文件名", required = true)
    private String fileName;

}
