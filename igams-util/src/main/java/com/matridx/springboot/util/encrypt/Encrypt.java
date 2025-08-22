package com.matridx.springboot.util.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 加密用到的工具类,MD5加密,目前无法进行解密
 * @author lt
 * {@code @time} 2011-3-14
 *
 */
public class Encrypt {
	private static final Logger log = LoggerFactory.getLogger(Encrypt.class);
	public Encrypt() {
	}

	public static String encrypt(String PlainStr) {
		return "{MD5}" + testHA2(PlainStr, "md5");
	}
	
	/**
	 * 兼顾前台的MD5加密方式，这是标准的
	 */
	public static String stringToMD5(String plainText) {
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        int codeLength = md5code.length();
        for (int i = 0; i < 32 - codeLength; i++) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }
	
	public static String testHA2(String data, String ha) {
		byte[] buffer = new byte[0];
		MessageDigest messageDigest = null;
		String s;
		try {
			buffer = data.getBytes(StandardCharsets.UTF_8);
			messageDigest = MessageDigest.getInstance(ha);
			messageDigest.update(buffer);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if (messageDigest != null) {
			buffer = messageDigest.digest();
		}
		s = Base64.encodeBase64String(buffer);
		return s;
	}
	
	public static String encrypt(String data, String ha) {
        char[] hexDigits ={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = data.getBytes(StandardCharsets.UTF_8);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(ha);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
           log.error(e.getMessage());
            return null;
        }
    }
	
	public String extractTokenKey(String value) {
		if (value == null) {
			return null;
		}
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
		}

        byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, bytes));
    }

	public static void main(String[] args) {
		System.out.println(Encrypt.stringToMD5("104933644122807952"));
		System.out.println(Encrypt.encrypt("oOzUd5IWNQTqPoOr70ntvxti-OR8"));
	}
	
}