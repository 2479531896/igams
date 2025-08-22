package com.matridx.springboot.util.base;

public class UrlUtil {

	/**
	 * 分布式端口拼接方法
	 */
	public static String getWebPrefix(String url){
		return "http://" + url.replace("/", "");
	}
}
