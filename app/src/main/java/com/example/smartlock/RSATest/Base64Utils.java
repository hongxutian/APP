package com.example.smartlock.RSATest;


import org.apache.commons.m.codec.binary.Base64;

public class Base64Utils {

    /**
     * base64加密
     * @param data 待加密数据
     * @return 加密后的base64字符串
     */
    public static String encode(String data){
        return Base64.encodeBase64String(data.getBytes());
    }

    public static String encode(byte[] data){
        return Base64.encodeBase64String(data);
    }

    /**
     * base64解密
     * @param data 待解密数据
     * @return 解密后的字节数据，如果传入的数据不是base64字符串，返回为空
     */
    public static byte[] decode(String data){
        if(!Base64.isBase64(data)){
            return null;
        }
        return Base64.decodeBase64(data);
    }

    /**
     * base64加密,URL安全
     * @param data 待加密数据
     * @return 加密后的base64字符串
     */
    public static String encodeURL(String data){
        return Base64.encodeBase64URLSafeString(data.getBytes());
    }

    public static String encodeURL(byte[] data){
        return Base64.encodeBase64URLSafeString(data);
    }

    /**
     * base64解密，URL安全
     * @param data 待解密数据
     * @return 解密后的字节数据，如果传入的数据不是base64字符串，返回为空
     */
    public static byte[] decodeURL(String data){
        if(!Base64.isBase64(data)){
            return null;
        }
        return Base64.decodeBase64(data);
    }
}
