package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CovidReportZipExport {
	
	
	private final Logger log = LoggerFactory.getLogger(CovidReportZipExport.class);
	
	private RedisUtil redisUtil;
	private String key;
	private String storePath;
	private String folderName;
	private List<FzjcxxDto> fzjcxxDtos;
	
	public void init(String key, String storePath, String folderName, List<FzjcxxDto> fzjcxxDtos, RedisUtil redisUtil){
		this.key = key;
		this.storePath = storePath;
		this.folderName = folderName;
		this.fzjcxxDtos = fzjcxxDtos;
		this.redisUtil = redisUtil;
	}
	
	/**
	 * 处理报告压缩下载
	 */
	public void reportZipExportDeal() {
		// TODO Auto-generated method stub
		log.info("-------reportZipExportDeal-------下载压缩报告线程开启------");
		DBEncrypt crypt = new DBEncrypt();
		redisUtil.hset("EXP_:_"+ key,"Fin", false,3600);
		for (int i = 0; i < fzjcxxDtos.size(); i++) {
			if(isCanceled(key)){
				//取消了则结束方法
				log.info("-------isCanceled------取消下载-------");
				return;
			}
			String wjlj = crypt.dCode(fzjcxxDtos.get(i).getWjlj());
			String wjm = fzjcxxDtos.get(i).getWjm();
			String newWjlj = storePath + "/" + wjm;
			File file = new File(wjlj);
			if (file.exists()) {
				copyFile(wjlj, newWjlj);
			}else{
				log.info("-----文件未找到:" + wjlj);
			}
			redisUtil.hset("EXP_:_"+key, "Cnt", String.valueOf(i+1));
		}
		//调用公共方法压缩文件
		String srcZip = storePath+".zip";
		ZipUtil.toZip(storePath, srcZip, true);
		String fileName = folderName+".zip";
		redisUtil.hset("EXP_:_"+key, "filePath", srcZip);
		redisUtil.hset("EXP_:_"+key, "fileName", fileName);
		redisUtil.hset("EXP_:_"+key, "Fin",true);
	}
	
	/**
	 * 判断是否被取消
	 */
	private boolean isCanceled(String key){
		boolean isCanceled = false;
		if(redisUtil.hget("EXP_:_"+key, "isCancel") != null){
			isCanceled = (Boolean) redisUtil.hget("EXP_:_" + key, "isCancel");
		}
		return isCanceled;
	}
	
	/**
	 * 复制文件
	 */
    public boolean copyFile(String src, String dest){
    	boolean flag = false;
    	FileInputStream in = null;
    	FileOutputStream out = null;
		try {
			in = new FileInputStream(src);
			File file = new File(dest);
	        if(!file.exists())
	            file.createNewFile();
	        out = new FileOutputStream(file);
	        int c;
	        byte[] buffer = new byte[1024];
	        while((c = in.read(buffer)) != -1){
	            for(int i = 0; i < c; i++)
	                out.write(buffer[i]);        
	        }
	        flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return flag;
    }
}
