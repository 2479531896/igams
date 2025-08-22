package com.matridx.igams.common.util;


import org.apache.poi.openxml4j.util.ZipSecureFile;

public class CommonWordUtil{
	/**
	 * 重新整理模板文件，把变量放到一个段落里，方便后续处理
	 * @param filePath
	 * @return
	 */
	public boolean reformWord(String filePath) {
		//解决docx等压缩类型文件，读取时出现zip爆炸问题，即压缩率大的文件读取报错
		ZipSecureFile.setMinInflateRatio(-1.0d);
		if(filePath.endsWith(".doc")) {
			
		}else if(filePath.endsWith(".docx")){
			CommonXWPFWordUtil wordUtil = new CommonXWPFWordUtil();
			return wordUtil.reformWord(filePath);
		}
		return true;
	}
}
