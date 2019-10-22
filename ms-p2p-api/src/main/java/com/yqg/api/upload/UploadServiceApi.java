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
    /**
     * 普通文件上传
     */
    public static final String path_file = "/upload/file";
    /**
     * 图片上传
     */
    public static final String path_image = "/upload/image";
    /**
     * base64编码文件上传
     */
    public static final String path_base64File = "/upload/base64File";
    /**
     * base64编码图片上传
     */
    public static final String path_base64Image = "/upload/base64Image";


}
