package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class FileZipExport {

private final Logger log = LoggerFactory.getLogger(FileZipExport.class);
	
	private RedisUtil redisUtil;
	private String key;
	private String storePath;
	private String folderName;
	private List<QgmxDto> qgmxDtos;
	private List<GysxxDto> gysxxDtos;

	public void init(String key, String storePath, String folderName,List<QgmxDto> qgmxDtos,RedisUtil redisUtil){
		this.key = key;
		this.storePath = storePath;
		this.folderName = folderName;
		this.qgmxDtos = qgmxDtos;
		this.redisUtil = redisUtil;
	}
	public void initGys(String key, String storePath,List<GysxxDto> gysxxDtos, RedisUtil redisUtil, String folderName){
		this.key = key;
		this.gysxxDtos = gysxxDtos;
		this.redisUtil = redisUtil;
		this.storePath = storePath;
		this.folderName = folderName;
	}
	/**
	 * 处理报告压缩下载
	 */
	public void fileZipExportDeal() {
		// TODO Auto-generated method stub
		log.info("-------fileZipExportDeal-------下载请购明细压缩文件线程开启------");
		DBEncrypt crypt = new DBEncrypt();
		redisUtil.hset("EXP_:_"+ key,"Fin", false,3600);
		for (int i = 0; i < qgmxDtos.size(); i++) {
			if(isCanceled(key)){//取消了则结束方法
				log.info("-------isCanceled------取消下载-------");
				return;
			}
			String wjlj = crypt.dCode(qgmxDtos.get(i).getWjlj());
			String wjm = qgmxDtos.get(i).getWjm();
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
	 * 处理报告压缩下载
	 */
	public void fileZipExportDealGys() {
		// TODO Auto-generated method stub
		log.info("-------fileZipExportDeal-------下载请购明细压缩文件线程开启------");
		DBEncrypt crypt = new DBEncrypt();
		redisUtil.hset("EXP_:_"+ key,"Fin", false,3600);
		for (int i = 0; i < gysxxDtos.size(); i++) {
			if(isCanceled(key)){//取消了则结束方法
				log.info("-------isCanceled------取消下载-------");
				return;
			}
			String wjlj = crypt.dCode(gysxxDtos.get(i).getWjlj());
			String wjm = gysxxDtos.get(i).getWjm();
			String newWjlj = gysxxDtos.get(i).getStorePath() + "/" + wjm;
			File file = new File(wjlj);
			if (file.exists()) {
				log.error("-----复制文件 wjlj:" + wjlj+"---------newWjlj:"+newWjlj);
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
    public void copyFile(String src, String dest){
		//文件对象
		File file1 = new File(src);
		File file2 = new File(dest);
		InputStream in = null;
		OutputStream out = null;
		try {
			//输入输出对象创建
			in = new FileInputStream(file1);
			out = new FileOutputStream(file2);
			//设置最大读取10个字节
			byte[] bytes = new byte[10];
			int n;
			while ((n = in.read(bytes)) != -1) {
				//读取a.txt文件内容，然后写入到b.txt文件中
				out.write(bytes, 0, n);
			}
		}catch (IOException e){
			// TODO Auto-generated catch block
				throw new RuntimeException(e);
		} finally {
			try {
				if (in!=null){
					in.close();
				}
				if (out!=null){
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
//		FileInputStream in = null;
//		FileOutputStream out = null;
//		try {
//			in = new FileInputStream(src);
//			File file = new File(dest);
//			if(!file.exists())
//				file.createNewFile();
//			out = new FileOutputStream(file);
//			int c;
//			byte buffer[] = new byte[1024];
//			while((c = in.read(buffer)) != -1){
//				for(int i = 0; i < c; i++)
//					out.write(buffer[i]);
//			}
//			flag = true;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.error(e.getMessage());
//		} finally {
//			try {
//				in.close();
//				out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				log.error(e.getMessage());
//			}
//		}
	}
}
