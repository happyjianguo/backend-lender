package com.yqg.common.utils;

/**
 * Created by gao on 2017/8/24.
 */
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES加密
 */
@Component
public class DESUtils {
//    //测试
//    public static void main(String args[]) {
//        long start=System.currentTimeMillis();
//        String result = encrypt("122");
//        System.out.println("加密结果:"+result+"加密耗时:"+(System.currentTimeMillis()-start));
//
//        start=System.currentTimeMillis();
//        result=decrypt(result);
//        System.out.println("解密结果:"+result+"解密耗时:"+(System.currentTimeMillis()-start));
//    }


   private static String desKey = "D5880DD82010913B5707433253118984263D7857298773549468758875018579537B57772163084478873699447306034466200AAA41196057412B434059469SSS23589270273686087290124712345D";

    private static String filePath = "";
    /**
     * 默认运行环境
     */
    private static String runEnv="test";

    @Value("${spring.profiles.active}")
    public void setRunEnv(String active) {
        runEnv = active;
    }

    @Value("${des.file.path}")
    public void setFilePaht(String path) {
        filePath = path;
    }

    private static String getDesKey(){
        if(!runEnv.equals("test")){
            desKey=TextToFieldUtils.readFileByLines(filePath);
        }
        return desKey;
    }

    /**
     * 加密字符串
     * @param data
     * @return
     */
    public static String encrypt(String data) {
        byte[] encrypt = encrypt(data.getBytes(), getDesKey());
        String result = Base64.encodeBase64String(encrypt);
        return result;
    }

    /**
     * 解密字符串
     * @param data
     * @return
     */
    public static String decrypt(String data) {
        byte[] encrypt = new byte[0];
        try {
            encrypt = decrypt(Base64.decodeBase64(data), getDesKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = new String(encrypt);
        return result;
    }

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    private static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }
}
