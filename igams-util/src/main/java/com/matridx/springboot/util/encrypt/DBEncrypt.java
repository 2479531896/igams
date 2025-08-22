package com.matridx.springboot.util.encrypt;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class DBEncrypt{

	private static final Logger log = LoggerFactory.getLogger(DBEncrypt.class);

	private Properties properties;

	public Object getObject() {
		return getProperties();
	}

	public boolean isSingleton() {
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties inProperties) {
		this.properties = inProperties;
		String originalUsername = properties.getProperty("user");
		String originalPassword = properties.getProperty("password");
		//String originalJdbcUrl = properties.getProperty("jdbcUrl");
		if (originalUsername != null) {
			String newUsername = deEncryptUsername(originalUsername);
			properties.put("user", newUsername);
		}
		if (originalPassword != null) {
			String newPassword = deEncryptPassword(originalPassword);
			properties.put("password", newPassword);
		}
//		if (originalJdbcUrl != null) {
//			String newJdbcUrl = deEncryptJdbcUrl(originalJdbcUrl);
//			properties.put("jdbcUrl", newJdbcUrl);
//		}
	}

	private String deEncryptUsername(String originalUsername) {
		return dCode(originalUsername, null);
	}
	
	private String deEncryptPassword(String originalPassword) {
		return dCode(originalPassword, null);
	}
	
	public String eCode(String str) {
		return eCode(str, null);
	}
	
	public String eCode(String needEncrypt, String key){
		byte[] result;
		try {
			//创建密码器
			Cipher enCipher = Cipher.getInstance("AES");
			// 初始化为加密模式的密码器
			enCipher.init(Cipher.ENCRYPT_MODE, Key.loadKey(key));
			//数据加密
			result = enCipher.doFinal(needEncrypt.getBytes());
			return Base64.encodeBase64String(result);
		} catch (Exception e) {
			throw new IllegalStateException("System doesn't support DES algorithm.");
		}
	}
	
	public String dCode(String str) {
		return dCode(str, null);
	}
	
	public String dCode(String str, String key) {
		return dCode(str.getBytes(), key);
	}
	
	public String dCode(byte[] result, String key){
		String s;
		try {
			Cipher deCipher = Cipher.getInstance("AES");
			deCipher.init(Cipher.DECRYPT_MODE, Key.loadKey(key));
			result = Base64.decodeBase64(new String(result));
			byte[] strByte = deCipher.doFinal(result);
			s = new String(strByte);
		} catch (Exception e) {
			throw new IllegalStateException("System doesn't support DES algorithm.");
		}
		return s;
	}
	
	/**
	* DES 加密
	* @param sKey 64位
	*/
	public String eCodeDes(String needEncrypt,String sKey){
		try {
			SecureRandom random = new SecureRandom();
		 DESKeySpec desKey = new DESKeySpec(sKey.getBytes());
		 //创建一个密匙工厂，然后用它把DESKeySpec转换成
		 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		 SecretKey securekey = keyFactory.generateSecret(desKey);
		 //Cipher对象实际完成加密操作
		 Cipher cipher = Cipher.getInstance("DES");
		 //用密匙初始化Cipher对象,ENCRYPT_MODE用于将 Cipher 初始化为加密模式的常量
		 cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		 //现在，获取数据并加密
		 byte[] result =cipher.doFinal(needEncrypt.getBytes());
		 
		 return Base64.encodeBase64String(result);
		 //正式执行加密操作
		 //return Base64.encodeBase64String(result); //按单部分操作加密或解密数据，或者结束一个多部分操作
		} catch (Exception e) {
			throw new IllegalStateException("System doesn't support DES algorithm.");
		}
	}
	
	/**
	* DES 解密
	* @param sKey 64位
	*/
	public String dCodeDes(String needEncrypt,String sKey){
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(sKey.getBytes());
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");//返回实现指定转换的 Cipher 对象
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			
			byte[] result =cipher.doFinal(Base64.decodeBase64(needEncrypt));
			
			return new String(result);
			// 真正开始解密操作
			//return Base64.encodeBase64String(result);
		} catch (Exception e) {
			throw new IllegalStateException("System doesn't support DES algorithm.");
		}
	}
	
	public static boolean checkSign(String sign) {
		DBEncrypt dbEncrypt = new DBEncrypt();
		String s_dateString = dbEncrypt.dCode(sign);
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date param_date = df.parse(s_dateString);
			
			Date noe_dateDate = new Date();
			
			// 获得两个时间的毫秒时间差异
		 long diff = noe_dateDate.getTime() - param_date.getTime();

            return diff >= 0 && diff <= 300000;
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}
	
	private static final byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };
	
	/** 
	 * JS加密，JAVA解密 
	 */
	public static String jsDecode(String str) {
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;
	
		while (i < len) {
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}
	
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((b1 << 2) | ((b2 & 0x30) >>> 4));
	
			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toString();
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2));
	
			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toString();
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write(((b3 & 0x03) << 6) | b4);
		}
		return buf.toString();
	}

	public static void main(String[] args) throws IOException, JSONException {
		String ss = "BDYY-7510461423-3009";
		String wbbh = StringUtil.isNotBlank(ss)?ss.length()>=10?ss.substring(0,10):ss:"";
		System.out.println(wbbh);

	}

}