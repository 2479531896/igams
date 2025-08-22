package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFileInspectionImport;
import com.matridx.igams.common.util.RedisXmlConfig;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileComInspectionImport extends FileComImport{
	
	//错误信息
	protected final StringBuffer errorMessage = new StringBuffer();
//	//确认信息
//	protected StringBuffer confirmMessage = new StringBuffer();
//	//警告信息
//	protected StringBuffer warnMessage = new StringBuffer();
//	//提示信息
//	protected StringBuffer hintMessage = new StringBuffer();
	//记录导入文件不可以重复的全部的列值
	protected StringBuffer exists = new StringBuffer();
//	//记录导入文件不可以重复的全部的列值（记得多字段单行内容）
//	protected Map<String,String> multiKeys = new HashMap<String, String>();
//	//记录导入文件不可以重复的全部的列值（多字段判断）
//	protected List<Map<String,String>> multiExists = new ArrayList<Map<String,String>>();
//	//警告记录信息
//	protected List<ImportRecordModel> warnRecList = new ArrayList<ImportRecordModel>();
//	//确认记录信息
//	protected List<ImportRecordModel> confirmRecList = new ArrayList<ImportRecordModel>();
//	//是否为隔断记录
//	protected boolean isPluralRec = false;
	
	private FjcfbDto fjcfbDto;
	private RedisUtil redisUtil;
	private IFileInspectionImport fileInspectionImport;
	private RedisXmlConfig redisXmlConfig;
	private String fjid;
	private User user;
	
	public void init(FjcfbDto fjcfbDto,RedisXmlConfig redisXmlConfig,IFileInspectionImport fileInspectionImport,User user){
		
		this.fjcfbDto = fjcfbDto;
		this.redisUtil = redisXmlConfig.getRedisUtil();
		this.fileInspectionImport = fileInspectionImport;
		this.redisXmlConfig = redisXmlConfig;
		this.fjid = fjcfbDto.getFjid();
		this.user = user;
	}
	
	public void before() {
		redisUtil.hset("IMP_:_"+fjid,"msg", "导入处理开始",3600);
	}

	public void after() {
		redisUtil.hset("IMP_:_"+fjid,"endflg", "1");
		redisUtil.hset("IMP_:_"+fjid,"msg", "导入处理结束。");
	}
	
	/**
	 * 上传文件查询送检信息，同时反馈给客户端
	 */
	public void FileInspectionImportCheck() throws BusinessException{
		// TODO Auto-generated method stub
		try{
			redisUtil.hset("IMP_:_"+fjid,"endflg", "0");
			before();
			
			String wjm = fjcfbDto.getWjm();
			int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
			String suffix = wjm.substring(index);
			//判断送检文件
			if(".zip".equals(suffix) || ".rar".equals(suffix)){
				int indexs = fjcfbDto.getLsbcdz().lastIndexOf("/"); 
	            if ((indexs >-1) && (indexs < (fjcfbDto.getLsbcdz().length()))) {
	                String folder = fjcfbDto.getLsbcdz().substring(0, indexs);
	                //解压,返回文件夹路径
	                String unZipFile = ZipUtil.unZipFile(folder, fjcfbDto.getLsbcdz());
	                //获取标本标号，查询信息
	    			File dirFile = new File(unZipFile);
	    			if(dirFile.exists()){
	    				File[] t_files = dirFile.listFiles();
	    				if (t_files != null) {
	    					List<String> ybbhs = new ArrayList<>();
	    		            for (File fileChildDir : t_files) {
	    		                if (fileChildDir.isFile()) {
	    		                	String fileName = fileChildDir.getName().substring(fileChildDir.getName().lastIndexOf("_")+1,fileChildDir.getName().lastIndexOf("."));
	    		                	ybbhs.add(fileName);
	    		                }else if(fileChildDir.isDirectory()){
	    		                	File[] tmp_files = fileChildDir.listFiles();
	    		                	for (File tp_fileChildDir : tmp_files) {
	    		                		if (tp_fileChildDir.isFile()) {
	    		                			String fileName = tp_fileChildDir.getName().substring(tp_fileChildDir.getName().lastIndexOf("_")+1,tp_fileChildDir.getName().lastIndexOf("."));
	    		                			ybbhs.add(fileName);
	    	    		                }
	    		                	}
	    		                }
	    		            }
	    		            if(!ybbhs.isEmpty()) {
		    		            List<Map<String, String>> sjxxList = getInspection(ybbhs);
		    		            redisUtil.hset("IMP_:_"+fjid,"sjxxList", sjxxList);
	    		            }else {
		    		            redisUtil.hset("IMP_:_"+fjid,"sjxxList", null);
	    		            }
	    				}
	    			}
	            }
			}else{
				//获取标本编号,查询信息
				String fileName = wjm.substring(wjm.lastIndexOf("_")+1, wjm.lastIndexOf("."));
				List<String> ybbhs = new ArrayList<>();
				ybbhs.add(fileName);
				List<Map<String, String>> sjxxList = getInspection(ybbhs);
				redisUtil.hset("IMP_:_"+fjid,"sjxxList", sjxxList);
			}
			
			after();
			
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", e.getMessage());
			throw new BusinessException("",e.getMessage());
		}
	}
	
	/**
	 * 根据标本编号获取送检信息，用于页面显示
	 */
	public List<Map<String,String>> getInspection(List<String> ybbhs){
		return fileInspectionImport.getListByYbbhs(ybbhs,errorMessage);
	}
	
	
	/**
	 * 更新送检报告
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean persistentFileInspectionAfterCheck() {
		// TODO Auto-generated method stub
		try {
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "0");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "文件开始导入。");
			
			Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
			String dtoName = impConfig.get("class-entity");
			Class<?> dtoClass = Class.forName(dtoName);
			Object dtoInstance = dtoClass.newInstance();
			//设置录入人员
			String setFjidMethodName = "set" + StringUtil.firstToUpper("fjid");
			Method setFjid = dtoClass.getMethod(setFjidMethodName, String.class);
			setFjid.invoke(dtoInstance, fjcfbDto.getFjid());
			//设置录入人员
			String setLrryMethodName = "set" + StringUtil.firstToUpper("lrry");
			Method setLrry = dtoClass.getMethod(setLrryMethodName, String.class);
			setLrry.invoke(dtoInstance, fjcfbDto.getLrry());
			//设置临时保存地址
			String setLsbcdzMethodName = "set" + StringUtil.firstToUpper("lsbcdz");
			Method setLsbcdz = dtoClass.getMethod(setLsbcdzMethodName, String.class);
			setLsbcdz.invoke(dtoInstance, fjcfbDto.getLsbcdz());
			//设置是否发送报告
			String setSffsMethodName = "set" + StringUtil.firstToUpper("sffs");
			Method setSffs = dtoClass.getMethod(setSffsMethodName, String.class);
			setSffs.invoke(dtoInstance, fjcfbDto.getSffs());
			
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入中，请稍候！");
			boolean result = fileInspectionImport.saveImportReport((BaseModel)dtoInstance, user, errorMessage);
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			if(result) {
				if(StringUtil.isNotBlank(errorMessage)) {
					redisUtil.hset("IMP_:_"+fjid, "insertWarn", errorMessage);
				}else {
					redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入完成!");
				}
			}else {
				redisUtil.hset("IMP_:_"+fjid, "insertError", "数据导入失败!" + errorMessage.toString());
			}
		}catch(BusinessException e){
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			redisUtil.hset("IMP_:_"+fjid, "insertError", e.getMsg());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			redisUtil.hset("IMP_:_"+fjid, "insertError", "数据导入失败!" +e.getMessage());
		}
		return true;
	}
}
