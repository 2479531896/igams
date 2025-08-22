package com.matridx.springboot.util.file.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 连接，上传，下载到FTP工具
 * {@code @Description:}
 *                连接，上传，下载到FTP工具
 */
public class FtpUtil {
	private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

	/**
	 *
	 * @param path 上传到ftp服务器哪个路径下
	 * @param addr 地址
	 * @param port 端口号
	 * @param username 用户名
	 * @param password 密码
	 */
	public static FTPClient connect(String path,String addr,int port,String username,String password) throws Exception {
		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(addr,port);
		ftp.login(username,password);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return ftp;
		}
		ftp.changeWorkingDirectory(path);
		return ftp;
	}

	/**
	 *
	 * @param file 上传的文件或文件夹
	 */
	public static void upload(File file,FTPClient ftp) throws Exception{
		Logger log = LoggerFactory.getLogger(FtpUtil.class);
		FileInputStream input = null;
		try{
			if(file.isDirectory()){
				log.error("FtpUtil---路径是文件夹");
				ftp.makeDirectory(file.getName());
				ftp.changeWorkingDirectory(file.getName());
				String[] files = file.list();
				if (files != null) {
					for (String s : files) {
						File file1 = new File(file.getPath() + "\\" + s);
						if (file1.isDirectory()) {
							upload(file1, ftp);
							ftp.changeToParentDirectory();
						} else {
							File file2 = new File(file.getPath() + "\\" + s);
							input = new FileInputStream(file2);
							ftp.storeFile(file2.getName(), input);
							input.close();
						}
					}
				}
			}else{
				log.error("FtpUtil---路径是文件");
                //File file2 = new File(file.getPath());
				input = new FileInputStream(file);
				ftp.enterLocalPassiveMode();
				ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
				boolean isok = ftp.storeFile(file.getName(), input);
				log.error("FtpUtil---"+file.getName()+"上传是否成功isok："+(isok?"yes":"no"));
				input.close();
			}
		}catch(Exception e){
			log.error("FtpUtil---upload："+e.getMessage());
			throw e;
		}finally{
			if(input != null){
				input.close();
			}
		}
	}

	/**
	 *
	 * @param path 上传到ftp服务器哪个路径下, 如路径path不存在则生成目录路径并选择到该路径下，如路径存在则选择到该路径下
	 * @param addr 地址
	 * @param port 端口号
	 * @param username 用户名
	 * @param password 密码
	 */
	public static FTPClient connectDir(String path,String addr,int port,String username,String password) throws Exception {
		Logger log = LoggerFactory.getLogger(FtpUtil.class);
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("UTF-8"); // 中文支持
		int reply;
		ftp.connect(addr,port);
		boolean islogin = ftp.login(username,password);
		log.error("FtpUtil---登录是否成功islogin："+(islogin?"yes":"no"));
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			return ftp;
		}
		mkdirPath(path, ftp);
		return ftp;
	}

	public static boolean mkdirPath(String path, FTPClient ftp){
		Logger log = LoggerFactory.getLogger(FtpUtil.class);
		try{
			String directory = path.endsWith("/")?path:path+"/";
			if ( !directory.equalsIgnoreCase("/") && !ftp.changeWorkingDirectory(directory)){
				//如果远程目录不存在，则递归创建远程服务器目录(changeWorkingDirectory类似linux服务的cd切换路径)
				int start = 0;
				int end;
				if (directory.startsWith("/")){
					start = 1;
				}
				end = directory.indexOf("/",start);
				do{
					String subDirectory = path.substring(0, end);
					if (!ftp.changeWorkingDirectory(subDirectory)){
						if (ftp.makeDirectory(subDirectory)){
							log.error("FtpUtil---ftp.makeDirectory(subDirectory)：");
							if (!ftp.changeWorkingDirectory(subDirectory)){
								log.error("FtpUtil---!ftp.changeWorkingDirectory(subDirectory)");
								return false;
							}
						} else {
							return false;
						}
					}
					start = end +1;
					end = directory.indexOf("/",start);
				}while (end>start);
			}
			return true;
		}catch (Exception e){
			log.error("FtpUtil---mkdirPath报错："+e.getMessage());
			return false;
		}
	}

	/**
	 *
	 * @param fileName 获取文件的名称
	 * @param localPath 保存本地
	 */
	public static boolean download(FTPClient ftp,String fileName,String localPath) throws Exception{
		OutputStream is = null;
		try{
			/*FTPFile[] fs = ftp.listFiles();
			for(FTPFile ff:fs){
				System.out.println(ff.getName());
				if(ff.getName().equals(fileName)){
					File localFile = new File(localPath+"/"+ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					System.out.println("ccc" +ff.getName()+fileName);
					is.close();
				}
			}*/
			is = new FileOutputStream(localPath);
			ftp.setBufferSize(1024);
			//设置文件类型（二进制）
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			/*
			 * ftp.retrieveFile(new
			 * String(fileName.getBytes("GB2312"),"ISO-8859-1"), is);
			 */
			ftp.retrieveFile(fileName, is);
			is.flush();
			ftp.logout();
		}catch(Exception e){
			log.error(e.getMessage());
		}finally {
			try {
				if(is != null){
					is.close();
				}
				if(ftp.isConnected()){
					ftp.disconnect();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return true;
	}


	public static void main(String[] args) throws Exception{ //ftp://60.191.45.245:7821      ftp://172.17.60.184/
//		FTPClient ftp = FtpUtil.connect("/xxx/yyy", "172.17.60.184", 21, "anonymous", "123");
//		File file = new File("D:\\matridx\\download\\2022\\202208\\20220801\\");
//		FtpUtil.upload(file,ftp);
////		FtpUtil.download(ftp, "/163949.tlp27", "D:\\matridx\\download\\2022\\202208\\20220801\\6666.tlp27");

		FTPClient ftp = FtpUtil.connectDir("/xxx/yyy/zzz", "172.17.60.184", 21, "anonymous", "123");
		File file1 = new File("\\matridx\\download\\2022\\202208\\20220802\\112931.tlp27");//112931.tlp27
		FtpUtil.upload(file1,ftp);
//		File file2 = new File("D:\\matridx\\download\\2022\\202208\\20220802\\112931.tlp27");
//		FtpUtil.upload(file2,ftp);
//		FtpUtil.download(ftp, "/163949.tlp27", "D:\\matridx\\download\\2022\\202208\\20220801\\6666.tlp27");
	}

}
