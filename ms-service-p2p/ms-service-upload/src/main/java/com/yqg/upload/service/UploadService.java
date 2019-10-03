package com.yqg.upload.service;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.api.upload.bo.UploadImgBo;
import com.yqg.api.upload.bo.UploadFileBo;
import com.yqg.api.upload.ro.UploadFileRo;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

/**
 * 文件上传服务器
 * Created by gao on 2018/6/30.
 */
public interface UploadService {
    /**
     * 文件存储
     *
     * @param file
     */
    UploadFileBo fileSave(MultipartFile file, UploadFileRo ro) throws BusinessException;

    /**
     * 生成缩略图
     * @param image
     * @param uploadImgBo
     * @return
     */
    void imgThumbnailSave(Image image,UploadImgBo uploadImgBo) throws BusinessException ;
}