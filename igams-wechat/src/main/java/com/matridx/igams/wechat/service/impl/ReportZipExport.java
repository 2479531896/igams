package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ReportZipExport{
	
	
	private Logger log = LoggerFactory.getLogger(ReportZipExport.class);
	
	private RedisUtil redisUtil;
	private String key;
	private String storePath;
	private String folderName;
	private List<SjxxDto> sjxxDtos;
	
	public void init(String key, String storePath, String folderName, List<SjxxDto> sjxxDtos, RedisUtil redisUtil){
		this.key = key;
		this.storePath = storePath;
		this.folderName = folderName;
		this.sjxxDtos = sjxxDtos;
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
		for (int i = 0; i < sjxxDtos.size(); i++) {
			if(isCanceled(key)){//取消了则结束方法
				log.info("-------isCanceled------取消下载-------");
				return;
			}
			String wjlj = crypt.dCode(sjxxDtos.get(i).getWjlj());
			String wjm = sjxxDtos.get(i).getWjm();
			if (StringUtil.isNotBlank(sjxxDtos.get(i).getBglxbj()) && "ALLPDF".equalsIgnoreCase(sjxxDtos.get(i).getBglxbj())){
				int len = sjxxDtos.get(i).getWjm().lastIndexOf(".");
				wjm = sjxxDtos.get(i).getWjm().substring(0,len)+"_"+sjxxDtos.get(i).getYwlx()+sjxxDtos.get(i).getWjm().substring(len);
			}
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
	 * @param key
	 * @return
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
	 * @param src
	 * @param dest
	 * @return
	 */
    public boolean copyFile(String src, String dest){
    	boolean flag = false;
    	FileInputStream in = null;
    	FileOutputStream out = null;
		try {
			in = new FileInputStream(src);
			File file = new File(new String(dest.getBytes("utf-8"),"utf-8"));
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
	        	in.close();
		        out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return flag;
    }
}
