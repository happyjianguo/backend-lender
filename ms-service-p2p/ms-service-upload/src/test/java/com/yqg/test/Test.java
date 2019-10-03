package com.yqg.test;

import com.yqg.common.utils.Base64Utils;
import com.yqg.common.utils.FileUtils;
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by gao on 2017/11/24.
 */
public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        test.upload("/Users/gao/Desktop/1.png");
    }

    public void upload(String localFile) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();

            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost("http://localhost:9030/upload/base64Image");

            httpPost = new HttpPost("http://localhost:9030/upload/image");
            httpPost = new HttpPost("http://localhost:8080/api-upload/upload/image");

            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFile));

            StringBody base64Data = new StringBody(Base64Utils.encode(FileUtils.file2Byte(localFile)), ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody fileName = new StringBody("1.png", ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody fileType = new StringBody("IDCARD", ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody sessionId = new StringBody("a474279a61e847e5a814f696c923e5f9", ContentType.create(
                    "text/plain", Consts.UTF_8));
            StringBody createThumbnail = new StringBody("true", ContentType.create(
                    "text/plain", Consts.UTF_8));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    // 相当于<input type="file" name="file"/>
                    .addPart("file", bin)
                    // 相当于<input type="text" name="userName" value=userName>
                    .addPart("base64Data", base64Data)
                    .addPart("fileName", fileName)
                    .addPart("fileType", fileType)
                    .addPart("sessionId", sessionId)
                    .addPart("createThumbnail", createThumbnail)
                    .build();

            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            System.out.println("The response value of token:" + response.getFirstHeader("token"));

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }

            // 销毁
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
