package com.matridx.igams.wechat.util;

import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class BaseWord{
	
//	@Value("${matridx.fileupload.prefix}")
//	private String prefix;
//
//	@Value("${matridx.fileupload.releasePath}")
//	private String releaseFilePath;
	/**
	 * 根据路径创建文件
	 * @param ywlx
	 * @param realFilePath
	 * @return
	 */
	protected String mkDirs(String ywlx,String realFilePath)
	{
		String storePath;
		if(ywlx!=null) {
			//根据日期创建文件夹
			storePath =realFilePath + ywlx +"/"+ "UP"+ 
							DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+ 
							DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
							DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			
		}else {
			storePath=realFilePath;
		}
		
		File file = new File(storePath);
		if (file.isDirectory()) {
			return storePath;
		}
		if(file.mkdirs())
		{
			return storePath;
		}
		return null;
	}
	
	/**
	 * 从原路径剪切到目标路径
	 * @param s_srcFile
	 * @param s_distFile
	 * @param coverFlag
	 * @return
	 */
	protected boolean CutFile(String s_srcFile,String s_distFile,boolean coverFlag) {
		boolean flag = false;
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile)) {
            return flag;
        }
		
		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists()) {
            return flag;
        }
		//目标文件已经存在
		if(distFile.exists()) {
			if(coverFlag) {
				srcFile.renameTo(distFile);
				flag = true;
			}
		}else {
			srcFile.renameTo(distFile);
			flag = true;
		}
		return flag;
	}
}
