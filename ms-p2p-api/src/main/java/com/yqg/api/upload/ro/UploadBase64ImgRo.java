package com.yqg.api.upload.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Base64编码图片上传请求对象
 */
@Data
public class UploadBase64ImgRo extends UploadBase64FileRo {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "是否创建缩略图", required = true)
    boolean createThumbnail;

}
