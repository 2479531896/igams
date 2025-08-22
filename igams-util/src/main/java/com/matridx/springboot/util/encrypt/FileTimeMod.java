package com.matridx.springboot.util.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTimeMod {

	private static final Logger log = LoggerFactory.getLogger(FileTimeMod.class);
	
    public static void updateFilesTime(String pathname) {
    	File file = new File(pathname);
		if (file.exists()) {
			File[] fileList = file.listFiles();
			if (fileList != null) {
				for (File files : fileList) {
					if (files.isDirectory()) {
						updateFilesTime(files.getParent() + "\\" + files.getName());
					}
					if (files.isFile()) {
						String fileName = files.getName();
						String nametime = fileName.substring(fileName.indexOf("-")+1, fileName.indexOf("."));

						String year = nametime.substring(0,4);
						String month = nametime.substring(4,6);
						String date = nametime.substring(6,8);
						String hour = nametime.substring(8,10);
						String mintes = nametime.substring(10,12);
						String second = nametime.substring(12,14);
						String time = year+"-"+month+"-"+date+" "+hour+":"+mintes+":"+second;
	//					String time = "2021-05-07 13:40:27";

						//更改文件的修改时间
						fileTimeMod(time,files);
					}
				}
			}

		} else {
			System.out.println("文件不存在");
		}
    }
    
    
    public static void fileTimeMod(String time, File fileToChange) {	
        //将字符串的时间转为date类型，在转为long类型
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime;
    	try {
    		dateTime = dateFormat.parse(time);
    		long longTime = dateTime.getTime();//得到新时间
    		fileToChange.setLastModified(longTime);//将文件的创建时间设置为新时间
    	} catch (ParseException e) {
			log.error(e.getMessage());
    	}   
    }
    

	 public static void main(String[] args) throws Exception {
		  updateFilesTime("E:\\TimeUpdate");
//		 updateFilesTime("F:\\质检数据\\20210507(0303-14M)");
	 }
}    