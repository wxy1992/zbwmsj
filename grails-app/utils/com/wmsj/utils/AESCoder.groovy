package com.wmsj.utils

import org.apache.commons.codec.binary.Base64

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest

public class AESCoder {

	private static final String ALGORITHM = "AES";

  	 /**
     * 解密算法
     * @param sSrc 待解密的字符串
     * @param sKey 密钥KEY(16位字符)
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public static String decryptAES(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

  	/**
     * 加密算法
     * @param sSrc 待加密字符串
     * @param sKey 密钥KEY(16位字符)
     * @return AES加密后进行BASE64处理
     * @throws Exception 异常
     */
    public static String encryptAES(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encodeToString(encrypted);
    }

    /**
     * 生成MD5摘要
     * @param value
     * @return
     */
    public static String getMD5(String value) {
        if (value == null || "".equals(value)){
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes("UTF-8"));
            byte[] b = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                int v = (int) b[i];
                v = v < 0 ? 0x100 + v : v;
                String hexString = Integer.toHexString(v);
                if (hexString.length() == 1){
                    sb.append('0');
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
