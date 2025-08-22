package com.matridx.springboot.util.file.upload;

import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


@Component
public class ZipUtil {

	private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);
	
	private static final int BUFFER_SIZE = 2 * 1024;
	
	/**
	 * 压缩文件解压
	 * @param path 解压路径
	 * @param filename 文件名称，要包含路径
	 */
	public static String unZipFile(String path,String filename) {
		String realPath="";
		try {
			String t_path = path;
			if(StringUtil.isNotBlank(path)&&!path.endsWith("/")) {
				t_path = path + "/";
			}
			
			ZipFile zipFile = new ZipFile(filename,Charset.forName("GBK"));
			Enumeration<? extends ZipEntry> zList =  zipFile.entries();


			File zipF = new File(filename);
			realPath = t_path + zipF.getName().replace(".zip", "");
			
			File firstFile=new File(realPath);
			firstFile.mkdir();
			
			t_path = realPath + "/";
			
			ZipEntry ze;
	        byte[] buf=new byte[1024];
	        while(zList.hasMoreElements()){
	            ze= zList.nextElement();
	            if(ze.isDirectory()){
	                File f=new File(t_path + ze.getName());
	                f.mkdir();
	                continue;
	            }
				OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(t_path, ze.getName())));
	            InputStream is=new BufferedInputStream(zipFile.getInputStream(ze));
	            int readLen;
	            while ((readLen=is.read(buf, 0, 1024))!=-1) {
	                os.write(buf, 0, readLen);
	            }
	            is.close();
	            os.close();
	        }
	        zipFile.close();
	        
		}catch(Exception e) {
			log.error(e.toString());
		}
        return realPath;
	}
	
	/** 
	* 给定根目录，返回一个相对路径所对应的实际文件名. 
    * @param baseDir 指定根目录 
    * @param absFileName 相对路径名，来自于ZipEntry中的name 
    * @return java.io.File 实际的文件 
    */  
    public static File getRealFileName(String baseDir, String absFileName){
        String[] dirs=absFileName.split("/");
        if(dirs.length>1){
        	File ret = null;
			String path = "";
            for (int i = 0; i < dirs.length-1;i++) {
                ret=new File(baseDir, path + dirs[i]);
				path += dirs[i] +"/";
            }
            if(!ret.exists())
                ret.mkdirs();
        }
        return new File(baseDir,absFileName);
    }
	
	/**
	 * 压缩成ZIP 方法
	 * @param srcDir 压缩文件夹路径
	 * @param KeepDirStructure 是否保留原来的目录结构, true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static String toZip(String srcDir,String srcZip, boolean KeepDirStructure){


//		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		FileOutputStream out;
		try {
			out = new FileOutputStream(srcZip);
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
//			long end = System.currentTimeMillis();
//			log.info("-------toZip---------压缩完成，耗时：" + (end - start) + " ms");
			return "success";
		} catch (Exception e) {
			log.error("zip error from ZipUtils" + e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
		return null;
	}
	
	/**
	 * 递归压缩方法
	 * @param sourceFile       源文件
	 * @param zos              zip输出流
	 * @param name             压缩后的名称
	 * @param KeepDirStructure 是否保留原来的目录结构, true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
			throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (KeepDirStructure) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}
				}
			}
		}
	}
	
	public static void main(String[] params) {
//		ZipUtil.unZipFile("F:\\压缩测试\\","F:\\压缩测试\\jy.zip");
		// 测试压缩方法
		//ZipUtil.toZip("D:\\Git", "D:\\Git.zip", true);
		String encoding = System.getProperty("file.encoding");
		System.out.print(encoding);

	}
}
