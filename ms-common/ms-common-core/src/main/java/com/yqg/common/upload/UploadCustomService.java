package com.yqg.common.upload;

import com.alibaba.fastjson.JSON;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.upload.config.UploadCustomConfig;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 上传服务
 * Created by gao on 2017/11/29.
 */
@Component
public class UploadCustomService {
   private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UploadCustomConfig uploadCustomConfig;

    /**
     * 上传二进制流文件
     *
     * @param sessionId
     * @param filePath
     * @param fileType
     * @return
     */
    public BaseResponse uploadFile(String sessionId, String filePath, String fileType) throws BusinessException {
        // 把文件转换成流对象FileBody
        FileBody fileBody = new FileBody(new File(filePath));
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("file", fileBody)
                .addPart("sessionId", this.getStringFileBody(sessionId))
                .addPart("fileType", this.getStringFileBody(fileType))
                .build();
        return this.upload(uploadCustomConfig.getUploadFilePath(), reqEntity);
    }

    /**
     * 上传base64图片
     *
     * @param sessionId
     * @param base64Str
     * @param fileName
     * @param fileType
     * @return
     */
    public BaseResponse uploadBase64Img(String sessionId, String base64Str, String fileName, String fileType) throws BusinessException {

        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("fileName", this.getStringFileBody(fileName))
                .addPart("base64Data", this.getStringFileBody(base64Str))
                .addPart("fileType", this.getStringFileBody(fileType))
                .addPart("sessionId", this.getStringFileBody(sessionId))
                .addPart("createThumbnail", this.getStringFileBody("true"))
                .build();
        return this.upload(uploadCustomConfig.getUploadBase64Path(), reqEntity);
    }

    private StringBody getStringFileBody(String content) {
        return new StringBody(content, ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    private BaseResponse upload(String urlPath, HttpEntity reqEntity) throws BusinessException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        BaseResponse baseResponse = null;
        try {
            httpClient = HttpClients.createDefault();

            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost(uploadCustomConfig.getUploadHost() + urlPath);
            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                String resultString = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
//                byte[] data = Base64Utils.decode(resultString.replaceAll("\"",""));
//                byte[] decodedData = RSAUtils.decryptByPrivateKey(data,
//                        RSAUtils.privateKeyStr);
//                resultString = new String(decodedData);
                // 打印响应内容
                logger.info("文件上传返回结果:" + resultString);
                baseResponse = JSON.parseObject(resultString, BaseResponse.class);
            }
            // 销毁
            EntityUtils.consume(resEntity);

        } catch (Exception e) {
            logger.error("文件上传异常:{}", e);
            throw new  BusinessException(BaseExceptionEnums.UPLOAD_SAVE_ERROR);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("文件上传异常{}",e);
                throw new  BusinessException(BaseExceptionEnums.UPLOAD_SAVE_ERROR);
            }
        }
        return baseResponse;
    }
}
