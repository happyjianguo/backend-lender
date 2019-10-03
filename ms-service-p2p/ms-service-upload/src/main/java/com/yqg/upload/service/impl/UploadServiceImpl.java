package com.yqg.upload.service.impl;

import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.UuidUtil;
import com.yqg.upload.config.UploadConfig;
import com.yqg.api.upload.bo.UploadImgBo;
import com.yqg.api.upload.bo.UploadFileBo;
import com.yqg.api.upload.ro.UploadFileRo;
import com.yqg.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件上传服务
 * Created by gao on 2018/6/30.
 */
@Service
public class UploadServiceImpl implements UploadService {

    public Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UploadConfig uploadConfig = new UploadConfig();

    @Override
    public UploadFileBo fileSave(MultipartFile file, UploadFileRo ro) throws BusinessException {
        UploadFileBo uploadFileBo = new UploadFileBo();
        //取得当前上传文件的文件名称
        String originalFilename = file.getOriginalFilename();
        String rootPath = uploadConfig.getRootPath();

        StringBuilder fullSavePath = new StringBuilder(rootPath);
        if (!rootPath.endsWith(File.separator)) {
            fullSavePath.append(File.separator);
        }
        createFullSavePath(ro.getFileType(), originalFilename, fullSavePath);
        fileSaveToLocal(file, originalFilename, fullSavePath);
        uploadFileBo.setFilePath(fullSavePath.toString());
        return uploadFileBo;
    }


    /**
     * 生成图片缩略图
     *
     * @param image
     * @param uploadImgBo
     * @return
     * @throws BusinessException
     */
    @Override
    public void imgThumbnailSave(Image image, UploadImgBo uploadImgBo) throws BusinessException {
        logger.info("生成缩略图============");
        try {
            logger.debug("ByteArrayInputStream转换image 流========");
            int height = image.getHeight(null) * uploadConfig.getThumbnailHeight() / image.getWidth(null);
            BufferedImage bufferedImage = new BufferedImage(uploadConfig.getThumbnailWidth()
                    , height, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = bufferedImage.createGraphics();

            logger.debug("image 写入bufferImg========");
            boolean drawImage = graphics.drawImage(image, 0, 0, uploadConfig.getThumbnailWidth()
                    , height, null);
            logger.debug("drawImage===={}", drawImage);

            String imgThumbnailSavePath = uploadImgBo.getFilePath().replace(".", "_thumbnail.");

            logger.info("缩略图url:[{}]======原高:[{}],现高:[{}],原宽:[{}],现宽:[{}]", imgThumbnailSavePath
                    , image.getHeight(null), bufferedImage.getHeight(), image.getWidth(null), bufferedImage.getWidth());
            String formatName = imgThumbnailSavePath.substring(imgThumbnailSavePath.lastIndexOf(".") + 1);
            ImageIO.write(bufferedImage, /*"GIF"*/ formatName /* format desired */, new File(imgThumbnailSavePath) /* target */);

            bufferedImage.flush();

            logger.info("缩略图完成url:[{}]======", imgThumbnailSavePath);
            uploadImgBo.setThumbnailPath(imgThumbnailSavePath);
        } catch (Exception e) {
            logger.error("缩略图生成异常:{}",e.getMessage());
            throw new BusinessException(BaseExceptionEnums.UPLOAD_SAVE_ERROR.setCustomMessage("缩略图生成失败"));
        }
    }

    /**
     * 生成随机目录
     *
     * @param sb
     * @param level
     * @return
     */
    private StringBuilder getRandomPath(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append(File.separator);
            sb.append((char) (Math.abs(UuidUtil.create().hashCode()) % 26 + 97));
        }
        return sb.append(File.separator);
    }

    /**
     * 生成存储路径
     *
     * @param fileType
     * @param originalFilename
     * @param fullSavePath
     * @throws BusinessException
     */
    private void createFullSavePath(String fileType, String originalFilename, StringBuilder fullSavePath) throws BusinessException {
        if (!StringUtils.isEmpty(originalFilename) && originalFilename.contains(".")) {
            //扩展名
            String extensionName = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
            //文件名转换
            String uniqueName = UuidUtil.create() + extensionName;
            fullSavePath.append(fileType.toLowerCase());
            getRandomPath(fullSavePath, 3);
            fullSavePath.append(uniqueName);
            logger.info("文件路径存储:{}", fullSavePath);

        } else {
            throw new BusinessException(BaseExceptionEnums.UPLOAD_FILE_NAME_ERROR);
        }
    }

    /**
     * 文件存储
     *
     * @param file
     * @param fileName
     * @param fullPath
     * @throws BusinessException
     */
    private void fileSaveToLocal(MultipartFile file, String fileName, StringBuilder fullPath) throws BusinessException {
        File fileToSave = new File(fullPath.toString());
        //如果目录不存在,创建目录
        if (!fileToSave.getParentFile().exists()) {
            if (fileToSave.getParentFile().mkdirs()) {
                logger.info("创建目录:{}", fileToSave.getParentFile().getPath());
            }
        }

        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(
                    new FileOutputStream(fileToSave));
            out.write(file.getBytes());
            out.flush();
        } catch (IOException e) {
            logger.error("文件:" + fileName + "存储失败!" + e);
            throw new BusinessException(BaseExceptionEnums.UPLOAD_SAVE_ERROR);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

