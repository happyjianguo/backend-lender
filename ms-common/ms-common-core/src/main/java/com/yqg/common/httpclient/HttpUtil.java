package com.yqg.common.httpclient;

import com.alibaba.fastjson.JSON;
import com.yqg.common.utils.Base64Utils;
import com.yqg.common.utils.BeanCoypUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http工具,get、post
 * Created by gao on 2018/7/3.
 */
public class HttpUtil {

    static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static String getApiUrl(String serviceName, String apiPath) {
        return new StringBuilder("http://").append(serviceName).append(apiPath).toString();
    }

    /**
     * 发送POST方法的请求,表单参数
     *
     * @param serviceName  发送请求的 服务名
     * @param apiPath  发送请求的 接口路径
     * @param object     请求参数，请求参数。
     * @return 所代表远程资源的响应结果
     */
    public static String sendFormPost(String serviceName, String apiPath, Object object) throws Exception {

        Map<String, Object> params = BeanCoypUtil.convertBean(object);
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramString.append(entry.getKey());
            paramString.append("=");
            paramString.append(entry.getValue());
            paramString.append("&");
        }
        paramString.deleteCharAt(paramString.length() - 1);

        return sendPost(serviceName, apiPath, paramString.toString(), false, false);
    }

    /**
     * http 请求post,Json对象
     *
     * @param serviceName  发送请求的 服务名
     * @param apiPath  发送请求的 接口路径
     * @param object
     * @return
     */
    public static String sendJsonPost(String serviceName, String apiPath, Object object) {

        return sendPost(serviceName, apiPath, JSON.toJSONString(object), false, true);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param serviceName  发送请求的 服务名
     * @param apiPath  发送请求的 接口路径
     * @param paramString 请求参数。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String serviceName, String apiPath, String paramString, boolean getBase64, boolean isJsonParam) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String apiUrl=getApiUrl(serviceName, apiPath);
        try {
            try {
                System.setProperty("sun.net.http.retryPost", "false");
            } catch (Exception e) {
                logger.error("set propertiest err", e);
            }

            URL realUrl = new URL(apiUrl);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (isJsonParam) {
                conn.setRequestProperty("Content-Type",
                        "application/json;charset=utf-8");
            } else {
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=utf-8");
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(
                    conn.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(paramString);

            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            byte[] data = null;
            if (getBase64) {
                // 读取图片字节数组
                try {
                    InputStream in2 = conn.getInputStream();
                    data = new byte[in2.available()];
                    in2.read(data);
                    in2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 对字节数组Base64编码
                return Base64Utils.encode(data);// 返回Base64编码过的字节数组字符串
            }

            String line;
            while ((line = in.readLine()) != null) {

                result += line;

            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！" + result + ",服务地址" + apiUrl
                    + ",参数" + paramString + "异常:{}", e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error("发送post请求关闭流出错:{}", ex);
            }
        }
        logger.info("sendPost请求URL:" + apiUrl + "\r\n  请求参数:" + paramString + "\r\n" + "返回结果：" + result);
        return result;

    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String method, String param) {
        // LogUtils.audit("sendGet param:" + param);
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + method + "?" + param;
            logger.debug("sendGet 请求：" + urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl
                    .openConnection();
            connection.setConnectTimeout(60000);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！", e);

        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error("发送GET请求出现异常！", e2);
            }
        }
        logger.info("sendGet 返回：" + result);
        return result;
    }
}