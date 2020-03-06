package com.example.smartlock.RSATest;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    /**
     * 生成256为的AES密钥
     * @return 包含密钥的map对象，含键值key，对应密钥，密钥已用URL安全的base64算法加密
     */
    public static Map<String,String> generateKey(){
        Map<String,String> res = new HashMap<>();
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            res.put("key",Base64Utils.encodeURL(secretKey.getEncoded()));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * 加密
     * @param key 密钥，密钥必须采用URL安全的base64算法加密
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public static byte[] encode(String key,byte[] data){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Utils.decodeURL(key),"AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] res=cipher.doFinal(data);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String encodeURL(String key,String data){
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Utils.decodeURL(key),"AES");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//            byte[] res=cipher.doFinal(data.getBytes());
//            return Base64Utils.encodeURL(res);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 解密
     * @param key 密钥，密钥必须采用URL安全的base64算法加密
     * @param data 待解密的数据
     * @return 解密后的数据
     */
    public static byte[] decode(String key,byte[] data){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Utils.decodeURL(key),"AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] res=cipher.doFinal(data);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static byte[] decodeURL(String key,String data){
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Utils.decodeURL(key),"AES");
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//            byte[] res=cipher.doFinal(Base64Utils.decodeURL(data));
//            return res;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
