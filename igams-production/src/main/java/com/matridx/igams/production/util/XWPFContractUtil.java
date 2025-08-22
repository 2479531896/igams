package com.matridx.igams.production.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.service.impl.MatridxByteArrayResource;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;

public class XWPFContractUtil{
	
	private final Logger log = LoggerFactory.getLogger(XWPFContractUtil.class);
	public Map<String,Object> replaceContract(Map<String,Object> map,IFjcfbService fjcfbService,String FTP_URL,String DOC_OK,AmqpTemplate amqpTempl) {
		Map<String,Object> resultMap=new HashMap<>();
		String releasePath=(String) map.get("releaseFilePath"); //正式文件路径
		String tempPath=(String) map.get("tempPath"); //临时文件路径
		FileInputStream fileInputStream = null;
		XWPFDocument document = null;
		OutputStream output = null;
		try {
			String templateFilePath=map.get("wjlj")!=null?map.get("wjlj").toString():"";
			DBEncrypt dbEncrypt=new DBEncrypt();
			File templateFile = new File(dbEncrypt.dCode(templateFilePath));
			if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
				log.error("模板不存在！请重新确认模板！");
				resultMap.put("message", "模板不存在！请重新确认模板！");
				resultMap.put("status","error");
				return resultMap;
			}
			fileInputStream=new FileInputStream(templateFile);
			document=new XWPFDocument(fileInputStream);
			//处理表格
			Iterator<XWPFTable> itTable = document.getTablesIterator();
			while (itTable.hasNext()) {
				XWPFTable table = itTable.next();
				// 替换表中的数据
				replaceTable(table,map);
			}
			
			//处理段落 处理段落需要进行倒叙处理
			List<XWPFParagraph> iterators = document.getParagraphs();
			for (int i = iterators.size()-1; i>-1; i--) {
				XWPFParagraph paragraph = iterators.get(i);
				replaceParagraph(paragraph,map);
			}
			
			List<XWPFParagraph> XMLList = document.getParagraphs();
			for (int i = XMLList.size()-1; i>-1; i--) {
				XWPFParagraph paragraph = XMLList.get(i);
				for (int j = 0; j < paragraph.getCTP().sizeOfRArray(); j++) {
					XmlObject xml_obj = paragraph.getCTP().getRArray(j);
					repalceXMLObject(xml_obj,map);
				}
			}
			fileInputStream.close();
			// 临时文件路径
			String tempFilePath = tempPath + BusTypeEnum.IMP_CONTRACT_WORD.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
					+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			//正式文件名
			String releaseFilePath = releasePath + BusTypeEnum.IMP_CONTRACT_WORD.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
					+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			//文件名
			String fileName = System.currentTimeMillis() + RandomUtil.getRandomString()+".docx";
			mkDirs(tempFilePath);
			output = new FileOutputStream(tempFilePath+"/"+fileName);
			document.write(output);
			
			mkDirs(releaseFilePath);
			CutFile(tempFilePath+"/"+fileName,releaseFilePath+"/"+fileName);
			
			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto fjcfbDto = new FjcfbDto();
			
			String fjid=StringUtil.generateUUID();
			fjcfbDto.setFjid(fjid);
			fjcfbDto.setYwid(map.get("htid").toString());
			fjcfbDto.setZywid("");
			fjcfbDto.setXh("1");
			fjcfbDto.setYwlx(BusTypeEnum.IMP_CONTRACT_WORD.getCode());
			String wjm=(String)map.get("number")+(String) map.get("seller")+".docx";
			fjcfbDto.setWjm(wjm);
			fjcfbDto.setWjlj(bpe.eCode(releaseFilePath+"/"+fileName));
			fjcfbDto.setFwjlj(bpe.eCode(releaseFilePath));
			fjcfbDto.setFwjm(bpe.eCode(fileName));
			fjcfbDto.setZhbj("0");
			boolean isTrue=fjcfbService.insert(fjcfbDto);
			resultMap.put("fjcfbDto", fjcfbDto);
			if(isTrue) {
				//删除旧的文件  
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
				String wjljjm=bpe.dCode(fjcfbDto.getWjlj());
				//连接服务器
				boolean sendFlg=sendWordFile(wjljjm,FTP_URL);
				if(sendFlg) {
					Map<String,String> pdfMap=new HashMap<>();
					pdfMap.put("wordName", fileName);
					pdfMap.put("fwjlj",bpe.eCode(releaseFilePath));
					pdfMap.put("fjid",fjcfbDto.getFjid());
					pdfMap.put("ywlx",BusTypeEnum.IMP_CONTRACT.getCode());
					pdfMap.put("MQDocOkType",DOC_OK);
					//发送Rabbit消息转换pdf
					amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
					//删除PDF
					FjcfbDto fjcfbDto_t=new FjcfbDto();
					fjcfbDto_t.setYwlx(BusTypeEnum.IMP_CONTRACT.getCode());
					fjcfbDto_t.setYwid(map.get("htid").toString());
					fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			log.error(e.toString());
		}finally {
			try {
				if (output != null)
					output.close();
				if (document != null)
					document.close();
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			} 
		}
		resultMap.put("message", "合同生成完成");
		resultMap.put("status","success");
		resultMap.put("word_ywlx",BusTypeEnum.IMP_CONTRACT_WORD.getCode());
		resultMap.put("pdf_ywlx",BusTypeEnum.IMP_CONTRACT.getCode());
		return resultMap;
	}

	public Map<String,Object> replaceQualityContract(Map<String,Object> map,IFjcfbService fjcfbService,String FTP_URL,String DOC_OK,AmqpTemplate amqpTempl) {
		Map<String,Object> resultMap=new HashMap<>();
		String releasePath=(String) map.get("releaseFilePath"); //正式文件路径
		String tempPath=(String) map.get("tempPath"); //临时文件路径
		FileInputStream fileInputStream = null;
		XWPFDocument document = null;
		OutputStream output = null;
		try {
			String templateFilePath=map.get("wjlj")!=null?map.get("wjlj").toString():"";
			DBEncrypt dbEncrypt=new DBEncrypt();
			File templateFile = new File(dbEncrypt.dCode(templateFilePath));
			if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
				log.error("模板不存在！请重新确认模板！");
				resultMap.put("message", "模板不存在！请重新确认模板！");
				resultMap.put("status","error");
				return resultMap;
			}
			fileInputStream=new FileInputStream(templateFile);
			document=new XWPFDocument(fileInputStream);
			//处理表格
			Iterator<XWPFTable> itTable = document.getTablesIterator();
			XWPFTable table=null;
			while (itTable.hasNext()) {
				 table = itTable.next();
				// 替换表中的数据
				if (table.getText().contains("seller")){
					replaceTableOnly(table,map);
				}
			}
			replaceTable(table,map);
			//处理段落 处理段落需要进行倒叙处理
			List<XWPFParagraph> iterators = document.getParagraphs();
			for (int i = iterators.size()-1; i>-1; i--) {
				XWPFParagraph paragraph = iterators.get(i);
				replaceParagraph(paragraph,map);
			}

			List<XWPFParagraph> XMLList = document.getParagraphs();
			for (int i = XMLList.size()-1; i>-1; i--) {
				XWPFParagraph paragraph = XMLList.get(i);
				for (int j = 0; j < paragraph.getCTP().sizeOfRArray(); j++) {
					XmlObject xml_obj = paragraph.getCTP().getRArray(j);
					repalceXMLObject(xml_obj,map);
				}
			}
			fileInputStream.close();
			// 临时文件路径
			String tempFilePath = tempPath + BusTypeEnum.IMP_QUALITY_WORD.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
					+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			//正式文件名
			String releaseFilePath = releasePath + BusTypeEnum.IMP_QUALITY_WORD.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
					+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			//文件名
			String fileName = System.currentTimeMillis() + RandomUtil.getRandomString()+".docx";
			mkDirs(tempFilePath);
			output = new FileOutputStream(tempFilePath+"/"+fileName);
			document.write(output);

			mkDirs(releaseFilePath);
			CutFile(tempFilePath+"/"+fileName,releaseFilePath+"/"+fileName);

			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto fjcfbDto = new FjcfbDto();

			String fjid=StringUtil.generateUUID();
			fjcfbDto.setFjid(fjid);
			fjcfbDto.setYwid(map.get("zlxyid").toString());
			fjcfbDto.setZywid("");
			fjcfbDto.setXh("1");
			fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_WORD.getCode());
			String wjm=(String)map.get("number")+(String) map.get("seller")+".docx";
			fjcfbDto.setWjm(wjm);
			fjcfbDto.setWjlj(bpe.eCode(releaseFilePath+"/"+fileName));
			fjcfbDto.setFwjlj(bpe.eCode(releaseFilePath));
			fjcfbDto.setFwjm(bpe.eCode(fileName));
			fjcfbDto.setZhbj("0");
			boolean isTrue=fjcfbService.insert(fjcfbDto);
			resultMap.put("fjcfbDto", fjcfbDto);
			if(isTrue) {
				//删除旧的文件
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
				String wjljjm=bpe.dCode(fjcfbDto.getWjlj());
				//连接服务器
				boolean sendFlg=sendWordFile(wjljjm,FTP_URL);
				if(sendFlg) {
					Map<String,String> pdfMap=new HashMap<>();
					pdfMap.put("wordName", fileName);
					pdfMap.put("fwjlj",bpe.eCode(releaseFilePath));
					pdfMap.put("fjid",fjcfbDto.getFjid());
					pdfMap.put("ywlx",BusTypeEnum.IMP_QUALITYT.getCode());
					pdfMap.put("MQDocOkType",DOC_OK);
					//发送Rabbit消息转换pdf
					amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
					//删除PDF
					FjcfbDto fjcfbDto_t=new FjcfbDto();
					fjcfbDto_t.setYwlx(BusTypeEnum.IMP_QUALITYT.getCode());
					fjcfbDto_t.setYwid(map.get("zlxyid").toString());
					fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			log.error(e.toString());
		}finally {
			try {
				if (output != null)
					output.close();
				if (document != null)
					document.close();
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		resultMap.put("message", "合同生成完成");
		resultMap.put("status","success");
		resultMap.put("word_ywlx",BusTypeEnum.IMP_QUALITY_WORD.getCode());
		resultMap.put("pdf_ywlx",BusTypeEnum.IMP_QUALITYT.getCode());
		return resultMap;
	}
	
	/**
	 * 替换表格
	 * @param table
	 * @param map
	 */
	private static void replaceTable(XWPFTable table,Map<String,Object> map) {
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> listMap=(List<Map<String, Object>>) map.get("htmx");
		if(!CollectionUtils.isEmpty(listMap)) {
			for (int i = 0; i < listMap.size(); i++) {
				XWPFTableRow row=copy(table, table.getRows().get(1),2+i);
				List<XWPFTableCell> cells=row.getTableCells();
				for (XWPFTableCell cell : cells) {
					List<XWPFParagraph> cellParList = cell.getParagraphs();
					for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
						List<XWPFRun> runs = cellParList.get(p).getRuns();
						for (int q = 0; runs != null && q < runs.size(); q++) {
							String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
							if (StringUtil.isBlank(oneparaString))
								continue;
							int startTagPos = oneparaString.indexOf("«");
							int endTagPos = oneparaString.indexOf("»");
							if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
								String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
								if(StringUtil.isNotBlank(varString)) {
									String rep_value=(String) listMap.get(i).get(varString);
									if(StringUtil.isNotBlank(rep_value)) {
										oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
									}else {
										oneparaString = oneparaString.replace("«" + varString + "»", "/");
									}
									runs.get(q).setText(oneparaString, 0);
								}
							}
						}
					}
				}
			}
		}
		table.removeRow(1);
		
		for (int i = table.getRows().size()-2; i < table.getRows().size(); i++) {
			List<XWPFTableCell> ListCell=table.getRows().get(i).getTableCells();
			for (XWPFTableCell cell:ListCell) {
				List<XWPFParagraph> cellParList = cell.getParagraphs();
				for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
					List<XWPFRun> runs = cellParList.get(p).getRuns();
					for (int q = 0; runs != null && q < runs.size(); q++) {
						String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
						if (StringUtil.isBlank(oneparaString))
							continue;
						int startTagPos = oneparaString.indexOf("«");
						int endTagPos = oneparaString.indexOf("»");
						if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
							String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
							if(StringUtil.isNotBlank(varString)) {
								String rep_value=(String) map.get(varString);
								if(StringUtil.isNotBlank(rep_value)) {
									oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
							}else {
									oneparaString = oneparaString.replace("«" + varString + "»", "/");
								}
								runs.get(q).setText(oneparaString, 0);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 替换表格但是不重复
	 * @param table
	 * @param map
	 */
	private static void replaceTableOnly(XWPFTable table,Map<String,Object> map) {
		for(int i=0;i<table.getRows().size();i++){
			List<XWPFTableCell> cells=table.getRows().get(i).getTableCells();
			for (XWPFTableCell cell : cells) {
				List<XWPFParagraph> cellParList = cell.getParagraphs();
				for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
					List<XWPFRun> runs = cellParList.get(p).getRuns();
					for (int q = 0; runs != null && q < runs.size(); q++) {
						String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
						if (StringUtil.isBlank(oneparaString))
							continue;
						int startTagPos = oneparaString.indexOf("«");
						int endTagPos = oneparaString.indexOf("»");
						if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
							String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
							if(StringUtil.isNotBlank(varString)) {
								String rep_value=(String) map.get(varString);
								if(StringUtil.isNotBlank(rep_value)) {
									oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
								}else {
									oneparaString = oneparaString.replace("«" + varString + "»", "/");
								}
								runs.get(q).setText(oneparaString, 0);
							}
						}
					}
				}
			}
		}
		table.removeRow(1);
//		for (int i = table.getRows().size()-2; i < table.getRows().size(); i++) {
//			List<XWPFTableCell> ListCell=table.getRows().get(i).getTableCells();
//			for (XWPFTableCell cell:ListCell) {
//				List<XWPFParagraph> cellParList = cell.getParagraphs();
//				for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
//					List<XWPFRun> runs = cellParList.get(p).getRuns();
//					for (int q = 0; runs != null && q < runs.size(); q++) {
//						String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
//						if (StringUtil.isBlank(oneparaString))
//							continue;
//						int startTagPos = oneparaString.indexOf("«");
//						int endTagPos = oneparaString.indexOf("»");
//						if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
//							String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
//							if(StringUtil.isNotBlank(varString)) {
//								String rep_value=(String) map.get(varString);
//								if(StringUtil.isNotBlank(rep_value)) {
//									oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
//								}else {
//									oneparaString = oneparaString.replace("«" + varString + "»", "/");
//								}
//								runs.get(q).setText(oneparaString, 0);
//							}
//						}
//					}
//				}
//			}
//		}
	}
	/**
	 * 复制一行
	 * @param table      要插入的table
	 * @param sourceRow  复制的模板
	 * @param rowIndex   插入的位置
	 
	 */
	private static XWPFTableRow copy(XWPFTable table,XWPFTableRow sourceRow,int rowIndex){
	    //在表格指定位置新增一行
		XWPFTableRow targetRow = table.insertNewTableRow(rowIndex);
		//复制行属性
		targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
		List<XWPFTableCell> cellList = sourceRow.getTableCells();
		if (null == cellList) {
		    return targetRow;
		}
		//复制列及其属性和内容
		XWPFTableCell targetCell;
		for (XWPFTableCell sourceCell : cellList) {
		    targetCell = targetRow.addNewTableCell();
		    //列属性
		    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
		    //段落属性
		    if(!CollectionUtils.isEmpty(sourceCell.getParagraphs())) {                     
		    	targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
	            if(!CollectionUtils.isEmpty(sourceCell.getParagraphs().get(0).getRuns())) {
	            	XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
	    	        cellR.setText(sourceCell.getText());
	    	        cellR.setBold(sourceCell.getParagraphs().get(0).getRuns().get(0).isBold());
	    	        cellR.getCTR().setRPr(sourceCell.getParagraphs().get(0).getRuns().get(0).getCTR().getRPr());
	            }else{
	            	targetCell.setText(sourceCell.getText());
	            }
	        }else{
	        	targetCell.setText(sourceCell.getText());
	        }
	    }
		return targetRow;
	}
	
	/**
	 * 替换段落
	 * @param paragraph
	 * @param map
	 */
	private static void replaceParagraph(XWPFParagraph paragraph,Map<String,Object> map) {
		List<XWPFRun> runs = paragraph.getRuns();
		for (int q = 0; runs != null && q < runs.size(); q++) {
			String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
			if(StringUtil.isNotBlank(oneparaString)) {
				int star = oneparaString.indexOf("«");
				int end = oneparaString.indexOf("»");
				if (star > -1 && end > -1) {
					String content = oneparaString.substring(star+1, end);
					if(StringUtil.isNotBlank(content)) {
						String tmpText=(String) map.get(content.toLowerCase());
						if(StringUtil.isNotBlank(tmpText)) {
							runs.get(q).setText(tmpText, 0);
						}else {
							runs.get(q).setText("/", 0);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 替换底部XML
	 * @param xml_obj
	 * @param map
	 * @throws XmlException 
	 */
	private static void repalceXMLObject(XmlObject xml_obj,Map<String,Object> map) throws XmlException {
		String xml_str=xml_obj.toString();
		String t_xml_str = xml_str;
		while (StringUtils.isNotBlank(t_xml_str)) {
			int startTagPos = t_xml_str.indexOf("«");
			int endTagPos = t_xml_str.indexOf("»");
			if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
				String varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
				if(StringUtil.isNotBlank(varString)) {
						Object object_value = map.get(varString.toLowerCase());
					if(object_value!=null) {
						if (object_value instanceof String) {
							String rep_value = (String) object_value;
							xml_str = xml_str.replaceAll("«" + varString + "»", rep_value);
							XmlObject xobj = XmlObject.Factory.parse(xml_str);
							xml_obj.set(xobj);
						}
					}else {
						xml_str = xml_str.replaceAll("«" + varString + "»", "/");
						XmlObject xobj = XmlObject.Factory.parse(xml_str);
						xml_obj.set(xobj);
					}
				}
			}else {
				break;
			}
            t_xml_str = t_xml_str.substring(endTagPos + 1);
		}
	}
	
	/**
	 * 根据路径创建文件
	 *
	 * @param storePath
	 */
	private static void mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()){
			return;
		}
		file.mkdirs();
	}
	
	/**
	 * 从原路径剪切到目标路径
	 */
	private static void CutFile(String s_srcFile, String s_distFile) {
		boolean flag = false;
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile))
			return;
		
		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists())
			return;
		//目标文件已经存在
		if(distFile.exists()) {
			if(true) {
				srcFile.renameTo(distFile);
				flag = true;
			}
		}else {
			srcFile.renameTo(distFile);
			flag = true;
		}
	}
	
	/**
	 * 上传Word文件
	 
	 */
	private boolean sendWordFile(String fileName,String FTP_URL) {
		//连接服务器
		//FTPClient ftp=FtpUtil.connect(FTPWORD_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
		//上传到服务器word下边的文件夹
		//FtpUtil.upload(new File(wjljjm), ftp);
		try {
			File t_file = new File(fileName);
			//文件不存在不做任何操作
			if(!t_file.exists())
				return true;
			
			byte[] bytesArray = new byte[(int) t_file.length()];
			
			FileInputStream t_fis = new FileInputStream(t_file);
			t_fis.read(bytesArray); //read file into bytes[]
			t_fis.close();
			//需要给文件的名称
			MatridxByteArrayResource contentsAsResource = new MatridxByteArrayResource(bytesArray);
			contentsAsResource.setFilename(t_file.getName());
			
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("file", contentsAsResource);
			RestTemplate t_restTemplate = new RestTemplate();
			//发送文件到服务器
			String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);
			return "OK".equals(reString);
		}catch(Exception e) {
			return false;
		}
	}
}
