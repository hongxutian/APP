package com.example.smartlock.RSATest;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtils {

    private static String RSAAlgorithm="RSA/None/PKCS1Padding";

    /**
     * 截取byte数组中的一部分
     * @param resource 被截取的数组
     * @param start 起始位置
     * @param end 结束位置，当结束位置超过数组的长度时，结束位置为数组的长度减去1
     * @return 截取的数据
     */
    private static byte[] subBytes(byte[] resource,int start,int end){
        int len = resource.length;
        if(start > end){
            return null;
        }
        if(start < 0 || end < 0){
            return null;
        }
        if(len <= start){
            return null;
        }
        int i = start;
        int j = 0;
        byte[] res = new byte[end - start + 1];
        while (i <= end && i <len){
            res[j] = resource[i];
            i++;
            j++;
        }
        return res;
    }

    /**
     * 将两个byte数组拼接在一起
     * @param resource 待拼接的数组
     * @param data 待拼接的数组
     * @return 拼接后的数组
     */
    private static byte[] catBytes(byte[] resource,byte[] data){
        int resourceLen = 0;
        int dataLen = 0;
        if(resource == null){
            resourceLen = 0;
        }else {
            resourceLen = resource.length;
        }
        if(data == null){
            dataLen = 0;
        }else {
            dataLen = data.length;
        }
        int resLen = resourceLen + dataLen;
        if(resLen == 0){
            return null;
        }
        byte[] res = new byte[resLen];
        int i = 0;
        for(int j=0;j<resourceLen;j++){
            res[i] = resource[j];
            i++;
        }
        for(int j=0;j<dataLen;j++){
            res[i] = data[j];
            i++;
        }
        return res;
    }

    /**
     * 生成1024的RSA密钥对
     * @return 包含私钥和公钥的map对象，键值为privateKey和publicKey，分别对应私钥和公钥
     * 生成的私钥和公钥用URL安全的base64算法加密过了
     */
    public static Map<String,String> generateKeyPair(){
        Map<String,String> res = new HashMap<>();
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            res.put("privateKey",Base64Utils.encodeURL(keyPair.getPrivate().getEncoded()));
            res.put("publicKey",Base64Utils.encodeURL(keyPair.getPublic().getEncoded()));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * 用私钥解密数据
     * @param key 私钥，必须是采用URL安全的base64算法加密了的密钥
     * @param data 待解密的数据
     * @return 解密后的数据
     */
    public static byte[] decodeByPrivateKey(String key,byte[] data){
        try{
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeURL(key));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey =(RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(RSAAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE,privateKey);

            int dataLen = data.length;
            int offset = 0;
            byte[] res = new byte[0];
            while (offset < dataLen){
                byte[] temp = null;
                if((dataLen-offset)>128){
                    temp = subBytes(data,offset,offset+127);
                    offset = offset + 128;
                }else {
                    temp = subBytes(data,offset,dataLen-1);
                    offset = dataLen;
                }
                res = catBytes(res,cipher.doFinal(temp));
            }
            return res;
        }catch (NoSuchAlgorithmException|InvalidKeySpecException| NoSuchPaddingException|InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException| BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeByPublicKey(String key,byte[] data){
        try{
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decodeURL(key));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey =(RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSAAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE,publicKey );

            int dataLen = data.length;
            int offset = 0;
            byte[] res = new byte[0];
            while (offset < dataLen){
                byte[] temp = null;
                if((dataLen-offset)>128){
                    temp = subBytes(data,offset,offset+127);
                    offset = offset + 128;
                }else {
                    temp = subBytes(data,offset,dataLen-1);
                    offset = dataLen;
                }
                res = catBytes(res,cipher.doFinal(temp));
            }
            return res;
        }catch (NoSuchAlgorithmException|InvalidKeySpecException| NoSuchPaddingException|InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException| BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用公钥加密数据
     * @param key 公钥，必须是采用URL安全的base64算法加密了的密钥
     * @param data 待加密的数据
     * @return 加密后的数据
     */
    public static byte[] encodeByPublicKey(String key,byte[] data){
        try{
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Utils.decodeURL(key));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey =(RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSAAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);

            int dataLen = data.length;
            int offset = 0;
            byte[] res = new byte[0];
            while (offset < dataLen){
                byte[] temp = null;
                if((dataLen-offset)>117){
                    temp = subBytes(data,offset,offset+116);
                    offset = offset + 117;
                }else {
                    temp = subBytes(data,offset,dataLen-1);
                    offset = dataLen;
                }
                res = catBytes(res,cipher.doFinal(temp));
            }
            return res;
        }catch (NoSuchAlgorithmException|InvalidKeySpecException| NoSuchPaddingException|InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException| BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encodeByPrivateKey(String key,byte[] data){
        try{
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeURL(key));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey =(RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(RSAAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);

            int dataLen = data.length;
            int offset = 0;
            byte[] res = new byte[0];
            while (offset < dataLen){
                byte[] temp = null;
                if((dataLen-offset)>117){
                    temp = subBytes(data,offset,offset+116);
                    offset = offset + 117;
                }else {
                    temp = subBytes(data,offset,dataLen-1);
                    offset = dataLen;
                }
                res = catBytes(res,cipher.doFinal(temp));
            }
            return res;
        }catch (NoSuchAlgorithmException|InvalidKeySpecException| NoSuchPaddingException|InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException| BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

}
