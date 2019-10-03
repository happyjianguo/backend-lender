package com.yqg.api.upload.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 图片上传信息
 * Created by gao on 2018/7/2.
 */
@Data
public class UploadImgRo extends UploadFileRo {

    @ReqStringNotEmpty
    @ApiModelProperty(value = "是否创建缩略图", required = true)
    boolean createThumbnail;
}
