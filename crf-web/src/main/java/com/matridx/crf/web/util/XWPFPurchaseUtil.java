package com.matridx.crf.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.dom4j.DocumentException;
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

public class XWPFPurchaseUtil{
	
	private Logger log = LoggerFactory.getLogger(XWPFPurchaseUtil.class);
	public Map<String,Object> replacePurchase(Map<String,Object> map,IFjcfbService fjcfbService,String FTP_URL,String DOC_OK,AmqpTemplate amqpTempl,String index) {
		Map<String,Object> resultMap=new HashMap<String, Object>();
		log.error("开启线程：生成报告--------------开始");
				FileInputStream fileInputStream = null;
				XWPFDocument document = null;
				OutputStream output = null;
				String releasePath=(String) map.get("releaseFilePath"); //正式文件路径
				String tempPath=(String) map.get("tempPath"); //临时文件路径
				try {
					// 如果模板文件不存在则直接返回
					String templateFilePath=map.get("wjlj")!=null?map.get("wjlj").toString():"";
					DBEncrypt dbEncrypt=new DBEncrypt();
					File templateFile = new File(dbEncrypt.dCode(templateFilePath));
					if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
						log.error("模板不存在！请重新确认模板！");
						resultMap.put("message", "模板不存在！请重新确认模板！");
						resultMap.put("status","error");
						return resultMap;
					}
					
					// 把文件放入流
					fileInputStream = new FileInputStream(templateFile);
					// 读取word文件
					document = new XWPFDocument(fileInputStream);
					new_template_special(document,map);
					// 关闭流
					fileInputStream.close();
					// 写到临时文件夹
					
					// 临时文件路径
					String tempFilePath = tempPath + BusTypeEnum.IMP_HZXX_TEMPLATE.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
							+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
					//正式文件名
					String releaseFilePath = releasePath + BusTypeEnum.IMP_HZXX_TEMPLATE.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
							+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
					//文件名
					String fileName = System.currentTimeMillis() + RandomUtil.getRandomString()+"("+index+")"+".docx";
					mkDirs(tempFilePath);
					output = new FileOutputStream(tempFilePath+"/"+fileName);
					document.write(output);
					
					mkDirs(releaseFilePath);
					CutFile(tempFilePath+"/"+fileName,releaseFilePath+"/"+fileName,true);
					
					DBEncrypt bpe = new DBEncrypt();
					FjcfbDto fjcfbDto = new FjcfbDto();
					
					String fjid=StringUtil.generateUUID();
					fjcfbDto.setFjid(fjid);
					fjcfbDto.setYwid(map.get("hzid").toString());
					fjcfbDto.setZywid("");
					fjcfbDto.setXh("1");
					fjcfbDto.setYwlx(BusTypeEnum.IMP_HZXX_TEMPLATE.getCode());
					String wjm=(String)map.get("hzxm")+(String)map.get("zyh")+".docx";
					fjcfbDto.setWjm(wjm);
					fjcfbDto.setWjlj(bpe.eCode(releaseFilePath+"/"+fileName));
					fjcfbDto.setFwjlj(bpe.eCode(releaseFilePath));
					fjcfbDto.setFwjm(bpe.eCode(fileName));
					fjcfbDto.setZhbj("0");
					boolean isTrue=fjcfbService.insert(fjcfbDto);
					resultMap.put("fjcfbDto", fjcfbDto);
					
					if(isTrue) {
						//删除旧的文件  
						String wjljjm=bpe.dCode(fjcfbDto.getWjlj());
						//连接服务器
						boolean sendFlg=sendWordFile(wjljjm,FTP_URL);
						if(sendFlg) {
							Map<String,String> pdfMap=new HashMap<>();
							pdfMap.put("wordName", fileName);
							pdfMap.put("fwjlj",bpe.eCode(releaseFilePath));
							pdfMap.put("fjid",fjcfbDto.getFjid());
							pdfMap.put("ywlx",BusTypeEnum.IMP_HZXX_TEMPLATE.getCode());
							pdfMap.put("MQDocOkType",DOC_OK);
							//发送Rabbit消息转换pdf
							amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
						}
					}
					
				} catch (Exception e) {
					log.error(e.fillInStackTrace().toString());
				} finally {
					try {
						if (output != null)
							output.close();
						if (document != null)
							document.close();
						if (fileInputStream != null) {
							fileInputStream.close();
						}
					} catch (Exception e) {
						log.error(e.toString());
					}
				}
				resultMap.put("message", "病例报告导出完成");
				resultMap.put("status","success");
				resultMap.put("word_ywlx",BusTypeEnum.IMP_HZXX_TEMPLATE.getCode());
				resultMap.put("pdf_ywlx",BusTypeEnum.IMP_HZXX_TEMPLATE.getCode());
				return resultMap;	}
	
	/**
	 * 替换表格
	 * @param table
	 * @param map
	 */
	private static void replaceTable(XWPFTable table,Map<String,Object> map) {
			for (int i = 0; i < table.getRows().size(); i++) {
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
									String rep_value= String.valueOf(map.get(varString));
									if(StringUtil.isNotBlank(rep_value) && map.get(varString)!=null) {
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
	 * 复制一行
	 * @param table      要插入的table
	 * @param sourceRow  复制的模板
	 * @param rowIndex   插入的位置
	 * @return
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
		XWPFTableCell targetCell = null;
		for (XWPFTableCell sourceCell : cellList) {
		    targetCell = targetRow.addNewTableCell();
		    //列属性
		    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
		    //段落属性
		    if(sourceCell.getParagraphs()!=null&&sourceCell.getParagraphs().size()>0){                     
		    	targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
	            if(sourceCell.getParagraphs().get(0).getRuns()!=null&&sourceCell.getParagraphs().get(0).getRuns().size()>0){
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
					String content = oneparaString.substring(star+1, end).toString();
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
			if(endTagPos >=0)
				t_xml_str = t_xml_str.substring(endTagPos + 1);
			else
				t_xml_str = "";
		}
	}
private boolean new_template_special(XWPFDocument document,Map<String, Object> map) throws DocumentException, XmlException, InvalidFormatException, IOException {
		/**
		 * 3.替换封面信息：封面内容为XML格式
		 */
		for (int i = 0; i < document.getParagraphs().size(); i++) {
			replaceHead(document.getParagraphs().get(i), map);
		}
		
		
		/**
		 * 4.替换表格中的内容
		 */
		Iterator<XWPFTable> itTable = document.getTablesIterator();
		while (itTable.hasNext()) {
			XWPFTable table = (XWPFTable) itTable.next();
			// 替换表中的数据
			replaceTable(table, map);
		}

		
		/**
		 * 7.处理表格中需要加粗、斜体的内容
		 */
		Iterator<XWPFTable> bold_Table = document.getTablesIterator(); 
		while (bold_Table.hasNext()) {
			XWPFTable table = (XWPFTable) bold_Table.next();
			addBlodForTable(table); 
		}
		
		/**
		 * 8.替换掉文本中的信息
		 */
		List<XWPFParagraph> iterators = document.getParagraphs();
		for (int i = iterators.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators.get(i);
			replaceText(paragraph, map,i,document);
		}
		
		/**
		 * 9.设置标题格式 ：先整理标题格式
		 */
		List<XWPFParagraph> iterators_title = document.getParagraphs();
		for (int i = iterators_title.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_title.get(i);
			replacTitle(paragraph,document,map);
		}
		
		/**
		 * 10.替换掉空的段落
		 */
		List<XWPFParagraph> iterators_s = document.getParagraphs();
		for (int i = iterators_s.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_s.get(i);
			replaceParagraph(paragraph);
			addBlodForParagraph(paragraph);
		}
		return true;
	}
	
	/**
	 * 根据路径创建文件
	 * 
	 * @param storePath
	 * @return
	 */
	private static boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()){
			return true;
		}
		return file.mkdirs();
	}
	
	/**
	 * 替换文本中的信息，不在table内的信息
	 * @param paragraph 当前段落
	 * @param map       替换数据
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private void replaceText(XWPFParagraph paragraph, Map<String, Object> map,int index,XWPFDocument document)
			throws InvalidFormatException, IOException {
		//判断一下，如果类型为其他（脑脊液等），删除掉去人源解释
			List<XWPFRun> runs = paragraph.getRuns();
			for (int i = 0; i < runs.size() && runs != null; i++) {
				// 获取文本信息
				String text = runs.get(i).getText(runs.get(i).getTextPosition());
				if (StringUtil.isNotBlank(text)) {
					// 为对应一个Run里多个变量，需要进行循环
					if (text.contains("images")) {
						int startTagPos=text.indexOf("«");
						int endTagPos=text.indexOf("»");
						if(startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
							String  varString = text.substring(startTagPos + 1, endTagPos);
							if(varString.contains("images")) {
								@SuppressWarnings("unchecked")
								List<String> wjljList=  (List<String>) map.get("images");
								if(wjljList.size()>0) {
									for (int j = 0; j < wjljList.size(); j++) {
										String imgFile=wjljList.get(j);
										FileInputStream is =new FileInputStream(imgFile);
										runs.get(i).setText("",0);
										runs.get(i).addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(350), Units.toEMU(200));
									}
								}else {
									runs.get(i).setText("",0);
								}
								
							}
						}
					}else {
						while (true) {
							int startTagPos = text.indexOf("«");
							int endTagPos = text.indexOf("»");
							// 寻找到需要替换的变量，如果没有找到则到下一个run
							if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
								String varString = text.substring(startTagPos + 1, endTagPos);
								Object object_value = map.get(varString);
								if (object_value instanceof String) {
									String rep_value = (String) object_value;
									// 如果找不到相应的替换问题，则替换成空字符串
									if (StringUtil.isBlank(rep_value)) {
										if("host".equals(varString)) {
											text = text.replace("«" + varString + "»", "");
										}else {
											text = text.replace("«" + varString + "»", "一");
										}
									} else {
										text = text.replace("«" + varString + "»", rep_value);
									}
									runs.get(i).setText(text, 0);
								} else {
									if("host".equals(varString)) {
										text = text.replace("«" + varString + "»", "");
									}else if(varString.contains("REM")){
										if(map.get("sfqry")!=null&&"1".equals(map.get("sfqry").toString())) {
											text=text.replace("«REM", "").replace("»", "");
										}else {
											text="";
										}
									}else {
                                       break;
									}
									runs.get(i).setText(text, 0);
								}
							} else {
								break;
							}
						}
					}
				}
			}
		}
	
	/**
	 * 替换头部的信息
	 * 
	 * @param paragraph 当前段落
	 * @param map       替换数据
	 */
	private void replaceHead(XWPFParagraph paragraph, Map<String, Object> map) throws DocumentException, XmlException {
		for (int i = 0; i < paragraph.getCTP().sizeOfRArray(); i++) {
			XmlObject xml_obj = paragraph.getCTP().getRArray(i);
			String xml_str = xml_obj.toString();
			String t_xml_str = xml_str;
			while (StringUtils.isNotBlank(t_xml_str)) {
				int startTagPos = t_xml_str.indexOf("«");
				int endTagPos = t_xml_str.indexOf("»");
				if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
					String varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
					Object object_value = map.get(varString);
					if (object_value instanceof String) {
						String rep_value = (String) object_value;
						xml_str = xml_str.replaceAll("«" + varString + "»", rep_value);
						XmlObject xobj = XmlObject.Factory.parse(xml_str);
						xml_obj.set(xobj);
					}
				}else {
					break;
				}
				if(endTagPos >=0)
					t_xml_str = t_xml_str.substring(endTagPos + 1);
				else
					t_xml_str = "";
			}
		}
	}
	/**
	 * 设置标题
	 * @param paragraph
	 */
	private void replacTitle(XWPFParagraph paragraph,XWPFDocument document,Map<String,Object> map) {
		if(paragraph.getText().contains("«Bacteria:")||paragraph.getText().contains("«Mycobacteria:")||paragraph.getText().contains("«MCR:")||paragraph.getText().contains("«Fungi:")||paragraph.getText().contains("«Viruses_D:")||paragraph.getText().contains("«Viruses_R:")
				||paragraph.getText().contains("«Parasite:")||paragraph.getText().contains("«Background:")||paragraph.getText().contains("«Gene:")||paragraph.getText().contains("«Results:")||paragraph.getText().contains("«Commensal:")) {
			String title=paragraph.getText().split("\\:")[1].replace("»", "");
			XWPFRun tmprun=null;
			for (int i = 0; i < paragraph.getRuns().size(); i++) {
				if(StringUtil.isNotBlank(paragraph.getRuns().get(i).getText(paragraph.getRuns().get(i).getTextPosition()))) {
					tmprun=paragraph.getRuns().get(i);
					break;
				}
			}
			String fontFamily="思源黑体";
			int fontSize=tmprun.getFontSize();
			String fontColor=tmprun.getColor();
			for (int i = paragraph.getRuns().size()-1; i >-1 ; i--) {
				paragraph.removeRun(i);
			}
			XWPFRun run=paragraph.createRun();
			run.setFontFamily(fontFamily);
			run.setFontSize(fontSize);
			run.setBold(true);
			run.setColor(fontColor);
			run.setText(title);
		}
	}
	
	/**
	 * 表格里边设置加粗，斜体
	 * @param table
	 */
	private  void  addBlodForTable(XWPFTable table) {
		List<XWPFTableRow> rows=table.getRows();
		for (XWPFTableRow row:rows) {
			List<XWPFTableCell> cells=row.getTableCells();
			for (XWPFTableCell cell:cells) {
				if(cell.getText()!=null) {
					if(cell.getText().contains("{br}")) {
						String[] texts=cell.getText().split("\\{br}");
						if(texts.length>0) {
							for (int i =cell.getParagraphs().size()-1; i >0 ; i--) {
								cell.removeParagraph(i);
							}
							XWPFParagraph paragraph=cell.getParagraphs().get(0);
//							if(paragraph.getText().contains("<Indent>")) {
//								paragraph.setIndentationHanging(700);
//							}
							XWPFRun tmprun=null;
							for (int i = 0; i < paragraph.getRuns().size(); i++) {
								if(StringUtil.isNotBlank(paragraph.getRuns().get(i).getText(paragraph.getRuns().get(i).getTextPosition()))) {
									tmprun=paragraph.getRuns().get(i);
									break;
								}
							}
							String fontFamily="思源黑体";
							if(StringUtil.isNotBlank(tmprun.getFontFamily())) {
								fontFamily=tmprun.getFontFamily();
							};
							int fontSize=0;
							if(tmprun.getFontSize()>-1) {
								fontSize=tmprun.getFontSize();
							};
							for (int j = paragraph.getRuns().size()-1; j >-1; j--) {
								paragraph.removeRun(j);
							}
							for (int i = 0; i < texts.length; i++) {
								XWPFRun run=paragraph.createRun();
								run.setFontFamily(fontFamily);
								if(fontSize>0) {
									run.setFontSize(fontSize);
								}
								String text=texts[i].replace("null", "");
 								if(text.contains("{\\n}")||text.contains("{n}")){
									run.addBreak();
									text="  "+text.replace("{\\n}", "").replace("{n}", "");
								}
								if(text.contains("{b+i}")) {
									run.setBold(true);
									run.setItalic(true);
									text=text.replace("{b+i}", "");
								}
								if(text.contains("{b}")) {
									run.setBold(true);
									text=text.replace("{b}", "");
								}
								if(text.contains("{i}")) {
									run.setItalic(true);
									text=text.replace("{i}", "");
								}
								if(text.contains("{color:")){
									int begin=texts[i].indexOf("{");
									int end=texts[i].indexOf("}");
									String flg=texts[i].substring(begin, end+1);
									String color=flg.substring(0+1,flg.length()-1).split(":")[1];
									run.setColor(color);
									text=text.replace(flg, "");
								}
								run.setText(text);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 替换空的段落内容为一
	 * @param paragraph
	 */
	private void replaceParagraph(XWPFParagraph paragraph) {
		List<XWPFRun> runs = paragraph.getRuns();
		int xh=0;
		for (int k = 0; k < runs.size() && runs != null; k++) {
			String text = runs.get(k).getText(runs.get(k).getTextPosition());
			if (text != null && StringUtil.isNotBlank(text)) {
				int startTagPos=text.indexOf("«");
				int endTagPos=text.indexOf("»");
				if(startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						if(xh==0) {
							text = text.replace(text, "一");
							xh++;
						}else if(xh>0){
							text = text.replace(text, "");
						}
					}
				}
			runs.get(k).setText(text, 0);
		}
	}
	/**
	 * 段落里边设置加粗，斜体
	 * @param paragraph
	 */
	private  void  addBlodForParagraph(XWPFParagraph paragraph) {
		if(paragraph.getText().contains("{br}")){
			String[] texts=paragraph.getText().split("\\{br}");
			XWPFRun tmprun=null;
			for (int i = 0; i < paragraph.getRuns().size(); i++) {
				tmprun=paragraph.getRuns().get(i);
				if(StringUtil.isNotBlank(paragraph.getRuns().get(i).getText(paragraph.getRuns().get(i).getTextPosition()))) {
					tmprun=paragraph.getRuns().get(i);
					break;
				}
			}
			String fontFamily="思源黑体";
			if(StringUtil.isNotBlank(tmprun.getFontFamily())) {
				fontFamily=tmprun.getFontFamily();
			}
			int fontSize=0;
			if(tmprun.getFontSize()>0) {
				fontSize=tmprun.getFontSize();
			}
			if(texts.length>0) {
				for (int j = paragraph.getRuns().size()-1; j >-1; j--) {
					paragraph.removeRun(j);
				}
				for (int i = 0; i < texts.length; i++) {
					XWPFRun run=paragraph.createRun();
					run.setFontFamily(fontFamily);
					if(fontSize>0) {
						run.setFontSize(fontSize);
					}
					String text=texts[i].replace("null", "");
					if(text.contains("{\\n}")||text.contains("{n}")){
						run.addBreak();
						text="  "+text.replace("{\\n}", "").replace("{n}", "");
					}
					if(text.contains("{b+i}")) {
						run.setBold(true);
						run.setItalic(true);
						text=texts[i].replace("{b+i}", "");
					}
					if(text.contains("{b}")) {
						run.setBold(true);
						text=texts[i].replace("{b}", "");
					}
					if(text.contains("{i}")) {
						run.setItalic(true);
						text=texts[i].replace("{i}", "");
					}
					if(text.contains("{color:")){
						int begin=texts[i].indexOf("{");
						int end=texts[i].indexOf("}");
						String flg=texts[i].substring(begin, end+1);
						String color=flg.substring(0+1,flg.length()-1).split(":")[1];
						run.setColor(color);
						text=texts[i].replace(flg, "");
					}
					run.setText(text);
				}
			}
		}
	}

	/**
	 * 从原路径剪切到目标路径
	 * @param srcPath
	 * @param distPath
	 * @return
	 */
	private static boolean CutFile(String s_srcFile,String s_distFile,boolean coverFlag) {
		boolean flag = false;
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile))
			return flag;
		
		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists())
			return flag;
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
	
	/**
	 * 上传Word文件
	 * @return
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
			
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("file", contentsAsResource);
			RestTemplate t_restTemplate = new RestTemplate();
			//发送文件到服务器
			String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);
			return "OK".equals(reString)?true:false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
