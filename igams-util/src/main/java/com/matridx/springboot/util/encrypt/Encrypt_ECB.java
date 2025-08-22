package com.matridx.springboot.util.encrypt;

import com.matridx.springboot.util.base.StringUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

/**
 * 国密SM4/ECB/PKCS7Padding对称加密解密
 */
public class Encrypt_ECB {
    /// <summary>
    /// SM4加密实现
    /// </summary>
    /// <param name="sk">平台分配的SK</param>
    /// <param name="content">加密原文</param>
    /// <returns>返回加密结果</returns>
    public static String encrypt(String data, String SK) {
        String ENCODING = "UTF-8";
        String ALGORITHM_NAME = "SM4";
        String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
        Security.addProvider(new BouncyCastleProvider());
        try {
            // 16进制字符串--&gt;byte[]
            byte[] keyData = SK.getBytes();
            // String--&gt;byte[]
            byte[] srcData = data.getBytes(ENCODING);
            // 加密后的数组
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(keyData, ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
            byte[] cipherArray = cipher.doFinal(srcData);
            // 转成16进制返回
            StringBuilder hexstr = new StringBuilder();
            String shaHex;
            for (byte b : cipherArray) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
            //return ByteUtils.toHexString(cipherArray);
        } catch (Exception e) {
            //return str + e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    /// <summary>
    /// SM4加密实现
    /// </summary>
    /// <param name="sk">平台分配的SK</param>
    /// <param name="content">加密原文</param>
    /// <returns>返回加密结果</returns>
    public static String decrypt(String data, String SK) {
        if(StringUtil.isBlank(data))
            return "";
        String ENCODING = "UTF-8";
        String ALGORITHM_NAME = "SM4";
        String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
        Security.addProvider(new BouncyCastleProvider());
        try {
            // 16进制字符串--&gt;byte[]
            byte[] keyData = SK.getBytes();
            // String--&gt;byte[]
            byte[] srcData = Hex.decode(data);
            // 加密后的数组
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
            SecretKeySpec sm4Key = new SecretKeySpec(keyData, ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, sm4Key);
            byte[] cipherArray = cipher.doFinal(srcData);
            // 转成16进制返回
            String resutls = new String(cipherArray,ENCODING);
            return resutls;
            //return ByteUtils.toHexString(cipherArray);
        } catch (Exception e) {
            //return str + e.getMessage();
            e.printStackTrace();
        }
        return null;
    }
}
