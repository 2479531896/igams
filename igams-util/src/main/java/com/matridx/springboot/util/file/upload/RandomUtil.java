package com.matridx.springboot.util.file.upload;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 随机工具类
 * 
 * @author hejunling , hechuan
 * {@code @dateTime} Oct 22, 2013 2:27:58 PM
 */
public class RandomUtil {

	private static final String	ALL_CHARACTOR			= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final String	ALL_CHARACTOR_NUMBER	= "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 随机6位数字
	 */
	public static int getRandomNumber() {
		Random random = new Random();
        return random.nextInt(1000000);
	}

	/**
	 * 随机6位字母
	 */
	public static String getRandomString() {
		StringBuilder builder = new StringBuilder(6);
		Random random = new Random();
		int len = ALL_CHARACTOR.length();
		for (int i = 0; i < 6; i++) {
			int nextInt = random.nextInt(len);
			builder.append(ALL_CHARACTOR.charAt(nextInt));
		}
		return builder.toString();
	}

	/**
	 * 随机6位字母，数字的组合
	 */
	public static String getRandomStringNumber() {
		StringBuilder builder = new StringBuilder(6);
		Random random = new Random();
		int len = ALL_CHARACTOR_NUMBER.length();
		for (int i = 0; i < 6; i++) {
			int nextInt = random.nextInt(len);
			builder.append(ALL_CHARACTOR_NUMBER.charAt(nextInt));
		}
		return builder.toString();
	}

	
	/** 
     * 生成随机文件名：当前年月日时分秒+五位随机数 
     */
    public static String getRandomFileName() {  
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");  
        Date date = new Date();  
        String str = simpleDateFormat.format(date);  
        Random random = new Random();  
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数  
  
        return rannum + str;// 当前时间  
    }  
}
