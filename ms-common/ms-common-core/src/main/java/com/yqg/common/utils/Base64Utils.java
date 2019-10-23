package com.yqg.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * BASE64编码解码工具包
 *
 * @author niebiaofei
 */
public class Base64Utils {

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * <p>
     * BASE64字符串解码为二进制数据
     * </p>
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) {
        return Base64.decodeBase64(base64.getBytes());
    }

    /**
     * 解码
     * @param base64
     * @return
     */
    public static String decodeToString(String base64) {
        return new String(Base64.decodeBase64(base64.getBytes()), Charset.forName("UTF-8"));
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字符串
     * </p>
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * 编码
     * @param toBase64
     * @return
     */
    public static String encodeString(String toBase64) {
        return new String(Base64.encodeBase64(toBase64.getBytes()));
    }

    /**
     * <p>
     * 将文件编码为BASE64字符串
     * </p>
     * <p>
     * 大文件慎用，可能会导致内存溢出
     * </p>
     *
     * @param filePath 文件绝对路径
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String encodeFile(String filePath) throws IOException {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * <p>
     * BASE64字符串转回文件
     * </p>
     *
     * @param filePath 文件绝对路径
     * @param base64   编码字符串
     * @throws IOException
     * @throws Exception
     */
    public static void decodeToFile(String filePath, String base64) throws IOException {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /**
     * <p>
     * 文件转换为二进制数组
     * </p>
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws IOException {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }

    /**
     * <p>
     * 二进制数据写文件
     * </p>
     *
     * @param bytes    二进制数据
     * @param filePath 文件生成目录
     * @throws IOException
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws IOException {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }

    private static String baseUrl = "http://localhost:8082/";

    /**
     * @param destUrl
     * @return
     * @throws IOException
     */
    public static String generateBase64Content(String destUrl) throws IOException {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[1024];
        StringBuilder sBuilder = new StringBuilder();
        try {
            url = new URL(baseUrl + destUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            while ((bis.read(buf)) != -1) {
                sBuilder.append(new String(buf));
                buf = new byte[1024];
            }
            return Base64Utils.encode(sBuilder.toString().getBytes());
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
    }

//    public static void main(String[] args) throws IOException {
//        System.err.println(generateBase64Content("http://i3.sinaimg.cn/blog/2014/1029/S129809T1414550868715.jpg"));
//    }
}
