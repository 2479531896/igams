package com.matridx.springboot.util.encrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.matridx.springboot.util.base.StringUtil;

public class Key {
	private static final String encryptKey = "2018matridx!";

	public Key() {
	}

	public static SecretKeySpec loadKey(String key) {
		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg;
		try {
			kg = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// AES 要求密钥长度为 128
			if (StringUtil.isNotBlank(key)) {
				random.setSeed(key.getBytes());
				kg.init(128, random);
			} else {
				random.setSeed(encryptKey.getBytes());
				kg.init(128,random);
			}
			// 生成一个密钥
			SecretKey secretKey = kg.generateKey();
			return new SecretKeySpec(secretKey.getEncoded(), "AES");// 转换为AES专用密钥
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Key.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}
}