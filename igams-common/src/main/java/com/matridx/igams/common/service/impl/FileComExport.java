package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.enums.ExportTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.util.RedisXmlConfig;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileComExport{
	
	private final Logger log = LoggerFactory.getLogger(FileComExport.class);
	
	private RedisUtil redisUtil;
	private RedisXmlConfig redisXmlConfig;
	private DcszDto dcszDto;
	private Object dtoInstance;
	private List<DcszDto> choseList;
	private String wjid;
	
	private String exportLimit;
	
	public void init(RedisXmlConfig redisXmlConfig,DcszDto dcszDto,Object dtoInstance, List<DcszDto> choseList,String exportLimit){
		this.redisUtil = redisXmlConfig.getRedisUtil();
		this.redisXmlConfig = redisXmlConfig;
		this.dcszDto = dcszDto;
		this.dtoInstance = dtoInstance;
		this.choseList = choseList;
		this.wjid = dcszDto.getWjid();
		this.exportLimit = exportLimit;
	}
	
	/**
	 * 下载文件生成处理
	 */
	public void commExportDeal(){
		if ("java.util.HashMap".equals(dtoInstance.getClass().getName())){
			Map<String, Object> entryData = (Map<String, Object>) dtoInstance;

			String filePath = (String)redisUtil.hget("EXP_:_"+wjid, "filePath");

			redisUtil.hset("EXP_:_"+ wjid,"Fin", false,3600);
			redisUtil.hset("EXP_:_"+ wjid,"Cnt", "0");

			List<List<String>> exportData;
			int startIRow =1;
			FileOutputStream fos = null;
			HSSFWorkbook wb = null;

			try{
				int totalCount = dcszDto.getTotalNumber();

				int showCount = Integer.parseInt(exportLimit);//写入限制条数 避免内存溢出

				int totalPage = totalCount%showCount==0?totalCount/showCount:totalCount/showCount+1;

				//创建工作表
				fos = new FileOutputStream(filePath);
				wb = new HSSFWorkbook();// keep 10000 rows in memory, exceeding rows will be flushed to disk
				Sheet sheet =  wb.createSheet();//创建sheet
				wb.setSheetName(0, ExportTypeEnum.getValueByCode(dcszDto.getYwid()));
				for(int curPage=1;curPage<=totalPage;curPage++){
					if(curPage==1){
						entryData.put("pageSize",showCount);
						//设置标题行
						Row headRow = sheet.createRow(0);
						for(int l =0;l<choseList.size();l++){
							Cell headCell = headRow.createCell(l);
							HSSFRichTextString hts=new HSSFRichTextString(choseList.get(l).getXsmc()/*+"["+choseList.get(l).getDczd()+"]"*/);
							headCell.setCellValue(hts);
						}
						//冻结第一行全部列
						sheet.createFreezePane(0, 1);
					}
					entryData.put("pageNumber",curPage);
					entryData.put("pageStart",(curPage-1)*showCount);

					//获取配置导出配置
					Map<String, String> configMap = redisXmlConfig.getExpConfig(dcszDto.getYwid());
					String classDao = configMap.get("class-service");
					String daoMethod = configMap.get("method");
					Object daoInstance = ServiceFactory.getService(classDao);//获取方法所在class

					if(isCanceled(wjid)){//取消了则跳出循环
						break;
					}

					Map<String, String> otherParMap = new HashMap<>();
					otherParMap.put("jsid", dcszDto.getJsid());
					otherParMap.put("yhid", dcszDto.getYhid());

					//获取数据
					exportData = configDataMap(choseList,entryData,otherParMap, daoInstance,daoMethod);

					for(int i=startIRow;i<(exportData.size()+startIRow);i++){

						if(isCanceled(wjid)){//取消了则跳出循环
							break;
						}

						Row row = sheet.createRow(i);
						for(int l =0;l<exportData.get(i-startIRow).size();l++){
							Cell cell = row.createCell(l);
							if("double".equals(choseList.get(l).getXssx())){
								try {
									String ivalue = exportData.get(i-startIRow).get(l);
									if(StringUtil.isNotBlank(ivalue)){
										double htd = Double.parseDouble(ivalue);
										cell.setCellValue(htd);
									}else{
										cell.setCellValue("");
									}
								} catch (NumberFormatException e) {
									HSSFRichTextString hts=new HSSFRichTextString(exportData.get(i-startIRow).get(l));
									cell.setCellValue(hts);
									log.error(e.getMessage());
								}
							}else{
								HSSFRichTextString hts=new HSSFRichTextString(exportData.get(i-startIRow).get(l));
								cell.setCellValue(hts);
							}
						}
						redisUtil.hset("EXP_:_"+wjid, "Cnt",String.valueOf(i));
					}
					startIRow += exportData.size();

					//输出更新缓存到文件
					fos.flush();
				}
				wb.write(fos);
				fos.flush();
				redisUtil.hset("EXP_:_"+wjid, "Fin",true);
			}catch(Exception e){
				redisUtil.hset("EXP_:_"+wjid, "Fin",true);
				String errorMsg = e.toString();
				log.error(e.toString());
				if(StringUtil.isBlank(errorMsg))
					errorMsg = "无错误信息";
				redisUtil.hset("EXP_:_"+wjid,"Msg",errorMsg);
			}finally{
				try{
					if(fos!=null){
						fos.flush();
						fos.close();
					}
					if(wb!=null)
						wb.close();
				}catch(Exception ex){
					redisUtil.hset("EXP_:_"+wjid,"Msg",ex.getMessage());
				}
			}
		}else {
			BaseModel entryData = (BaseModel) dtoInstance;

			String filePath = (String)redisUtil.hget("EXP_:_"+wjid, "filePath");

			redisUtil.hset("EXP_:_"+ wjid,"Fin", false,3600);
			redisUtil.hset("EXP_:_"+ wjid,"Cnt", "0");

			List<List<String>> exportData;
			int startIRow =1;
			FileOutputStream fos = null;
			HSSFWorkbook wb = null;

			try{
				int totalCount = dcszDto.getTotalNumber();

				int showCount = Integer.parseInt(exportLimit);//写入限制条数 避免内存溢出

				int totalPage = totalCount%showCount==0?totalCount/showCount:totalCount/showCount+1;

				//创建工作表
				fos = new FileOutputStream(filePath);
				wb = new HSSFWorkbook();// keep 10000 rows in memory, exceeding rows will be flushed to disk
				Sheet sheet =  wb.createSheet();//创建sheet
				wb.setSheetName(0, ExportTypeEnum.getValueByCode(dcszDto.getYwid()));
				for(int curPage=1;curPage<=totalPage;curPage++){
					if(curPage==1){
						entryData.setPageSize(showCount);

						//设置标题行
						Row headRow = sheet.createRow(0);
						for(int l =0;l<choseList.size();l++){
							Cell headCell = headRow.createCell(l);
							HSSFRichTextString hts=new HSSFRichTextString(choseList.get(l).getXsmc()/*+"["+choseList.get(l).getDczd()+"]"*/);
							headCell.setCellValue(hts);
						}
						//冻结第一行全部列
						sheet.createFreezePane(0, 1);
					}
					entryData.setPageNumber(curPage);
					entryData.setPageStart((curPage - 1)*showCount);

					//获取配置导出配置
					Map<String, String> configMap = redisXmlConfig.getExpConfig(dcszDto.getYwid());
					String classEntity = configMap.get("class-entity");
					String classDao = configMap.get("class-service");
					String daoMethod = configMap.get("method");
					Object daoInstance = ServiceFactory.getService(classDao);//获取方法所在class

					if(isCanceled(wjid)){//取消了则跳出循环
						break;
					}

					Map<String, String> otherParMap = new HashMap<>();
					otherParMap.put("jsid", dcszDto.getJsid());
					otherParMap.put("yhid", dcszDto.getYhid());

					//获取数据
					exportData = configData(choseList,entryData,otherParMap,classEntity,daoInstance,daoMethod);

					for(int i=startIRow;i<(exportData.size()+startIRow);i++){

						if(isCanceled(wjid)){//取消了则跳出循环
							break;
						}

						Row row = sheet.createRow(i);
						for(int l =0;l<exportData.get(i-startIRow).size();l++){
							Cell cell = row.createCell(l);
							if("double".equals(choseList.get(l).getXssx())){
								try {
									String ivalue = exportData.get(i-startIRow).get(l);
									if(StringUtil.isNotBlank(ivalue)){
										double htd = Double.parseDouble(ivalue);
										cell.setCellValue(htd);
									}else{
										cell.setCellValue("");
									}
								} catch (NumberFormatException e) {
									HSSFRichTextString hts=new HSSFRichTextString(exportData.get(i-startIRow).get(l));
									cell.setCellValue(hts);
									log.error(e.getMessage());
								}
							}else{
								HSSFRichTextString hts=new HSSFRichTextString(exportData.get(i-startIRow).get(l));
								cell.setCellValue(hts);
							}
						}
						redisUtil.hset("EXP_:_"+wjid, "Cnt",String.valueOf(i));
					}
					startIRow += exportData.size();

					/*
				if(curPage * showCount > totalCount)
					this.getSession().setAttribute(dcszDto.getFilename()+"Cnt", String.valueOf(totalCount));
				else
					this.getSession().setAttribute(dcszDto.getFilename()+"Cnt", String.valueOf(curPage * showCount));*/
					//输出更新缓存到文件
					fos.flush();
				}
				wb.write(fos);
				fos.flush();
				redisUtil.hset("EXP_:_"+wjid, "Fin",true);
			}catch(Exception e){
				redisUtil.hset("EXP_:_"+wjid, "Fin",true);
				String errorMsg = e.toString();
				log.error(e.toString());
				if(StringUtil.isBlank(errorMsg))
					errorMsg = "无错误信息";
				redisUtil.hset("EXP_:_"+wjid,"Msg",errorMsg);
			}finally{
				try{
					if(fos!=null){
						fos.flush();
						fos.close();
					}
					if(wb!=null)
						wb.close();
				}catch(Exception ex){
					redisUtil.hset("EXP_:_"+wjid,"Msg",ex.getMessage());
				}
			}
		}

	}
	
	/**
	 * 判断是否被取消
	 */
	private boolean isCanceled(String wjid){
		boolean isCanceled = false;
		if(redisUtil.hget("EXP_:_"+wjid, "isCancel") != null){
			isCanceled = (Boolean) redisUtil.hget("EXP_:_" + wjid, "isCancel");
		}
		return isCanceled;
	}
	
	/**
	 * 根据选择的字段导出数据
	 */
	@SuppressWarnings("unchecked")
	private List<List<String>> configData(List<DcszDto> choseList,BaseModel entryData,Map<String, String> otherParMap,String classEntity,Object daoInstance,String daoMethod) throws Exception{
		
		Class<?> dtoClass = Class.forName(classEntity);//获取实体类class

		Method getListMethod = daoInstance.getClass().getMethod(daoMethod, Map.class);//获取方法
		
		List<Object> list;
		Map<String,Object> params = new HashMap<>();
		params.put("choseList", choseList);
		params.put("entryData", entryData);
		params.put("otherPars", otherParMap);
		list = (List<Object>)getListMethod.invoke(daoInstance, params);//获取数据列表

		List<List<String>> allData = new ArrayList<>();
		for(Object entry:list){
			
			if(isCanceled(wjid)){//取消了则跳出循环
				break;
			}
			
			if(entry==null)
				continue;
			
			List<String> rowData = new ArrayList<>();
			for(DcszDto dcsz:choseList){
				try{
					String getFieldMethodName = "get" + StringUtil.firstToUpper(dcsz.getDczd().toLowerCase());
					Method getMethod = dtoClass.getMethod(getFieldMethodName, null);
					String val = (String)getMethod.invoke(entry, null);//实体类get方法获取值
					rowData.add(val);
				}catch(NoSuchMethodException e){
					throw new BusinessException("字段'"+dcsz.getDczdmc()+"("+dcsz.getDczd()+")'无法匹配！");
				}
			}
			allData.add(rowData);
		}
		
		System.gc();
		
		return allData;
	}
	/**
	 * 根据选择的字段导出数据
	 */
	@SuppressWarnings("unchecked")
	private List<List<String>> configDataMap(List<DcszDto> choseList, Map<String, Object> entryData, Map<String, String> otherParMap, Object daoInstance, String daoMethod) throws Exception{
		Method getListMethod = daoInstance.getClass().getMethod(daoMethod, Map.class);//获取方法
		List<Object> list;
		Map<String,Object> params = new HashMap<>();
		params.put("choseList", choseList);
		params.put("entryData", entryData);
		params.put("otherPars", otherParMap);
		list = (List<Object>)getListMethod.invoke(daoInstance, params);//获取数据列表
		List<List<String>> allData = new ArrayList<>();
		for(Object entry:list){
			if(isCanceled(wjid)){//取消了则跳出循环
				break;
			}
			if(entry==null)
				continue;

			List<String> rowData = new ArrayList<>();
			for(DcszDto dcsz:choseList){
				String getFieldMethodName = dcsz.getDczd().toLowerCase();
				String val = ((HashMap<String, String>)entry).get(getFieldMethodName);
				rowData.add(val);
			}
			allData.add(rowData);
		}
		System.gc();
		return allData;
	}
}
