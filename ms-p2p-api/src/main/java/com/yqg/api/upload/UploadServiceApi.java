package com.yqg.api.upload;

/**
 * 上传服务接口
 * Created by gao on 2018/7/6.
 */
public class UploadServiceApi {

    /**
     * 文件上传服务
     */
    public static final String serviceName = "service-upload";
    
    public static final String path_healthcheck = "/api-upload/healthcheck";
    /**
     * 普通文件上传
     */
    public static final String path_file = "/public/api-upload/upload/file";
    public static final String path_file_control = "/api-upload/upload/file";
    /**
     * 图片上传
     */
    public static final String path_image = "/public/api-upload/upload/image";

    public static final String path_image_control = "/api-upload/upload/image";
    /**
     * base64编码文件上传
     */
    public static final String path_base64File = "/api-upload/upload/base64File";
    /**
     * base64编码图片上传
     */
    public static final String path_base64Image = "/api-upload/upload/base64Image";
    /**
     * show image
     */
    public static final String path_showImage = "/public/api-upload/upload/showImage";
    public static final String path_showImageControl = "/api-upload/upload/showImage";

    /**
     * download attachment
     */
    public static final String path_downloadContract = "/public/api-upload/downloadContract/{creditorNo}";
    public static final String path_downloadAttachment = "/public/api-upload/upload/downloadAttachment";
    public static final String path_downloadAttachmentControl = "/api-upload/upload/downloadAttachment";

}
