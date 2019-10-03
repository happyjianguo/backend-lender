package com.yqg.api.upload.bo;

import lombok.Data;

/**
 * 图片上传业务对象
 * Created by gao on 2018/7/2.
 */
@Data
public class UploadImgBo extends UploadFileBo {

    /**
     * 缩略图存储路径
     */
    String thumbnailPath;

}
