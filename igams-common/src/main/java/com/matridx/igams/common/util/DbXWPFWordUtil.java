package com.matridx.igams.common.util;

import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class DbXWPFWordUtil {
	private RedisUtil redisUtil;
	Map<String, Object> map;
	String wjid;
	public void init(RedisXmlConfig redisXmlConfig,Map<String, Object> map,String wjid){
		this.redisUtil = redisXmlConfig.getRedisUtil();
		this.map = map;
		this.wjid = wjid;
	}

	/**
	 * 重新整理模板文件，把变量放到一个段落里，方便后续处理
	 *
	 * @param filePath
	 * @return
	 */
	public boolean reformWord(String filePath) {
		//文件不存在，则返回false
		if (StringUtil.isBlank(filePath))
			return false;


		File file = new File(filePath);
		//文件不存在，则返回false
		if (!file.exists())
			return false;

		file.renameTo(new File(filePath + "_back"));

		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		XWPFDocument document = null;
		try {
			// 把文件放入流
			fileInputStream = new FileInputStream(filePath + "_back");
			//读取word文件
			document = new XWPFDocument(fileInputStream);
			//读取首页中的文字，特殊处理
			reformHeadParagraph(document);

			//先整理未在表格中的段落
			reformOutsideParagraph(document, null);

			//再整理表格中的段落
			reformTableParagraph(document);

			//最后整理页眉页脚
			reformHeadFootParagraph(document);

			fileOutputStream = new FileOutputStream(filePath);

			document.write(fileOutputStream);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// TODO: handle exception
				if (fileOutputStream != null)
					fileOutputStream.close();
				// TODO: handle exception
				if (document != null)
					document.close();
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * 处理首页中的参数信息
	 *
	 * @param document
	 * @return
	 */
	private boolean reformHeadParagraph(XWPFDocument document) {
		int maxsize = Math.min(document.getParagraphs().size(), 3);
		for (int x = 0; x < maxsize; x++) {
			XWPFParagraph paragraph = document.getParagraphs().get(x);
			for (int i = 0; i < paragraph.getCTP().sizeOfRArray(); i++) {
				XmlObject xml_obj = paragraph.getCTP().getRArray(i);
				String xml_str = xml_obj.toString();
				String t_xml_str = xml_str;
				while (StringUtils.isNotBlank(t_xml_str)) {
					int startTagPos = t_xml_str.indexOf("«");
					int endTagPos = t_xml_str.indexOf("»");
					if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						String varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
						String t_varString = varString;
						//如果中间有很多分割，则删除多余部分，整合到一个CTP里面
						String paramText = "";
						while (t_varString.contains("<w:t>")) {
							String pString = t_varString.substring(t_varString.indexOf("<w:t>") + 5);
							int endPos = pString.indexOf("</w:t>");
							if (endPos > -1) {
								String t_pString = pString.substring(0, pString.indexOf("</w:t>")).trim();
								paramText += t_pString;
								t_varString = pString.substring(pString.indexOf("</w:t>") + 6);
							} else {
								break;
							}
						}
						if (!paramText.isEmpty()) {
							xml_str = xml_str.replace(varString, paramText);
							try {
								XmlObject xobj = XmlObject.Factory.parse(xml_str);
								xml_obj.set(xobj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//如果没有分割则不作任何处理
					} else {
						break;
					}
                    t_xml_str = t_xml_str.substring(endTagPos + 1);
				}
			}
		}
		return true;
	}

	/**
	 * 整理文档中表格外的段落
	 *
	 * @param document 文档
	 * @param cell     表格
	 * @return
	 */
	private boolean reformOutsideParagraph(XWPFDocument document, XWPFTableCell cell) {

		//先整理未在表格中的段落
		List<XWPFParagraph> graphs;

		if (document != null)
			graphs = document.getParagraphs();
		else {
			graphs = cell.getParagraphs();
		}

		List<XWPFParagraph> varParagraphs = new ArrayList<>();
		StringBuilder varString = new StringBuilder();
		//循环所有段落，查找其中的替换信息，把相应替换信息放到一个段落里
		for (XWPFParagraph xwpfParagraph : graphs) {
			String text = xwpfParagraph.getText();
			//查找里面的所有变量
			while (true) {
				int startIndex = text.indexOf("«");
				//段落里存在替换信息
				if (startIndex != -1) {
					varParagraphs.add(xwpfParagraph);

					//变量在同一段落里
					int endIndex = text.indexOf("»");
					if (endIndex != -1) {
//						String text_flg=text.substring(startIndex+1,endIndex);
//						if(text_flg.indexOf(":")==-1) {
						varString.append(text, startIndex + 1, endIndex);
						//重新设置变量的段落
						reformParagraph(document, cell, varParagraphs, varString.toString());

						//重新查找后续的变量
						text = text.substring(endIndex + 1);
						varString = new StringBuilder();
						varParagraphs = new ArrayList<>();
						//						}else {
//							isFind = false;
//							break;
//						}
					} else {
						break;
					}

				} else {
					//没有找到开始字符，则跳出到下一个段落
					break;
				}
			}
		}
		return true;
	}

	/**
	 * 整理文档中表格中的段落
	 *
	 * @param document
	 * @return
	 */
	private boolean reformTableParagraph(XWPFDocument document) {

		List<XWPFTable> tables = document.getTables();

		for (XWPFTable xwpfTable : tables) {
			int rcount = xwpfTable.getNumberOfRows();
			for (int i = 0; i < rcount; i++) {
				//拿到当前行的内容
				XWPFTableRow row = xwpfTable.getRow(i);
				//获取当前行的列数
				List<XWPFTableCell> cells = row.getTableCells();
				//循环列（cells 多）
				for (XWPFTableCell cell : cells) {
					reformOutsideParagraph(null, cell);
				}
			}
		}

		return true;
	}

	/**
	 * 重新设置变量的段落信息
	 *
	 * @param paragraphs 变量持续的段落信息
	 * @param varString  变量信息
	 * @return
	 */
	private boolean reformParagraph(XWPFDocument document, XWPFTableCell cell, List<XWPFParagraph> paragraphs, String varString) {
		if (paragraphs == null || paragraphs.isEmpty() || StringUtil.isBlank(varString))
			return false;

		StringBuffer t_varString = null;
		boolean isFind = false;
		//结束标记所在的Run位置
		int endPos = -1;
		List<Integer> waitList = new ArrayList<>();

		//因为要删除相应的字段，为了保证正确，只能从后往前循环
		for (int i = paragraphs.size() - 1; i >= 0; i--) {
			//如果为持续段落里的中间段落，则删除
			if (i > 0 && i != (paragraphs.size() - 1)) {
				//文本放到变量中，用于判断是否是需要操作的变量，因为是倒循环，所以用要用插入
				t_varString.insert(0, paragraphs.get(i).getText());
				if (document != null) {
					//删除中间段落
					document.removeBodyElement(document.getPosOfParagraph(paragraphs.get(i)));
				} else {
					cell.removeParagraph(i);
				}
				continue;
			}

			//如果切换段落，则代表等待队列里的信息都可以删除
			if (!waitList.isEmpty()) {
				delWaitList(paragraphs.get(i + 1), waitList, varString);
			}
			List<XWPFRun> xwpfRuns = paragraphs.get(i).getRuns();
			//如果持续段落不只是一个，则对第一个段落里的« 后的run都进行删除
			for (int j = xwpfRuns.size() - 1; j >= 0; j--) {

				String textString = xwpfRuns.get(j).getText(xwpfRuns.get(j).getTextPosition());
				//已到结束标记，Run里不存在数据，则删除
				if (endPos > 0 && StringUtil.isBlank(textString)) {
					//paragraphs.get(i).removeRun(j);
					waitList.add(j);
					continue;
				} else if (textString == null) {
					continue;
				}

				while (true) {
					//找到结束标记，则开始记录
					int endTagPos = 0;
					if (endPos < 0) {
						endTagPos = textString.indexOf("»");
						if (endTagPos != -1) {
							t_varString = new StringBuffer();
							t_varString = t_varString.insert(0, textString.substring(0, endTagPos));
							waitList.clear();
							endPos = j;
						}
					}

					//如果找到结束标记
					if (endPos > 0) {
						//不管有没有找到开始标记，则把当前的Run放到等待删除队列中
						waitList.add(j);
						//查找开始标记
						int startTagPos = textString.lastIndexOf("«");
						//找到开始标记，并且两个在同一个run里，则只截取相应信息放到变量名称里，并设置标记为true
						if (startTagPos > -1 && endPos == j && i == (paragraphs.size() - 1) && startTagPos < endTagPos) {
							isFind = true;
							t_varString = new StringBuffer();
							t_varString = t_varString.insert(0, textString.substring(startTagPos + 1, endTagPos));
						}
						//找到开始标记，两个不在同一个run里，则截取结束为止的文字，并置标记为true
						else if (startTagPos > -1 && endPos != j) {
							isFind = true;
							t_varString = t_varString.insert(0, textString.substring(startTagPos + 1));
						} else if (endPos != j) {
							t_varString = t_varString.insert(0, textString);
							break;
						} else {
							break;
						}
						//已找到变量，则跳出进行判断是否为需要查找的变量
                        //如果找到的变量是正确的，则根据删除队列进行删除信息
                        if (varString.equals(t_varString.toString())) {
                            //如果在同一个run，则无需删除信息
                            if (waitList.size() > 1) {
                                delWaitList(paragraphs.get(i), waitList, varString);
                            }
                            break;
                        } else {
                            //找到的变量不正确,重新初始化
                            t_varString = null;
                            isFind = false;
                            endPos = -1;
                            waitList.clear();
                            //当前run重新计算
                            textString = textString.substring(0, startTagPos);
                            //未找到则继续下一个run
                        }
                    } else {
						break;
					}
				}
				if (isFind) {
					break;
				}
			}

			if (isFind) {
				break;
			}
		}

		return true;
	}

	/**
	 * 整理页眉页脚的段落
	 *
	 * @param document 文档
	 * @param cell     表格
	 * @return
	 */
	private boolean reformHeadFootParagraph(XWPFDocument document) {

		//先整理页眉的段落
		List<XWPFHeader> headerList = document.getHeaderList();
		for (XWPFHeader header : headerList) {
			List<XWPFParagraph> paragraphs = header.getParagraphs();
			replaceHeadFootParagraphText(paragraphs, document);
		}
		//先整理页脚的段落
		List<XWPFFooter> footerList = document.getFooterList();
		for (XWPFFooter foot : footerList) {
			List<XWPFParagraph> paragraphs = foot.getParagraphs();
			replaceHeadFootParagraphText(paragraphs, document);
		}
		return true;
	}

	/**
	 * 找到文档中的变量并进行替换
	 *
	 * @param paragraphs
	 * @param document
	 * @return
	 */
	private boolean replaceHeadFootParagraphText(List<XWPFParagraph> paragraphs, XWPFDocument document) {
		List<XWPFParagraph> varParagraphs = new ArrayList<>();
		StringBuilder varString = new StringBuilder();
		//循环所有段落，查找其中的替换信息，把相应替换信息放到一个段落里
		for (XWPFParagraph xwpfParagraph : paragraphs) {
			String text = xwpfParagraph.getText();
			//查找里面的所有变量
			while (true) {
				int startIndex = text.indexOf("«");
				//段落里存在替换信息
				if (startIndex != -1) {
					varParagraphs.add(xwpfParagraph);

					//变量在同一段落里
					int endIndex = text.indexOf("»");
					if (endIndex != -1) {
						varString.append(text, startIndex + 1, endIndex);
						//重新设置变量的段落
						reformHeadFootParagraph(paragraphs, varParagraphs, varString.toString());

						//重新查找后续的变量
						text = text.substring(endIndex + 1);
						varString = new StringBuilder();
						varParagraphs = new ArrayList<>();
					} else {
						break;
					}

				} else {
					//没有找到开始字符，则跳出到下一个段落
					break;
				}
			}
		}
		for (XWPFParagraph paragraph : paragraphs) {
			for (int i = 0; i < paragraph.getCTP().sizeOfRArray(); i++) {
				XmlObject xml_obj = paragraph.getCTP().getRArray(i);
				String xml_str = xml_obj.toString();
				String t_xml_str = xml_str;
				while (StringUtils.isNotBlank(t_xml_str)) {
					int startTagPos = t_xml_str.indexOf("«");
					int endTagPos = t_xml_str.indexOf("»");
					if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						String s_varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
						String t_varString = s_varString;
						//如果中间有很多分割，则删除多余部分，整合到一个CTP里面
						String paramText = "";
						while (t_varString.contains("<w:t>")) {
							String pString = t_varString.substring(t_varString.indexOf("<w:t>") + 5);
							int endPos = pString.indexOf("</w:t>");
							if (endPos > -1) {
								String t_pString = pString.substring(0, pString.indexOf("</w:t>")).trim();
								paramText += t_pString;
								t_varString = pString.substring(pString.indexOf("</w:t>") + 6);
							} else {
								break;
							}
						}
						if (!paramText.isEmpty()) {
							xml_str = xml_str.replace(s_varString, paramText);
							try {
								XmlObject xobj = XmlObject.Factory.parse(xml_str);
								xml_obj.set(xobj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						//如果没有分割则不作任何处理
					} else {
						break;
					}
                    t_xml_str = t_xml_str.substring(endTagPos + 1);
				}
			}
		}
		return true;
	}

	/**
	 * 重新设置变量的段落信息
	 *
	 * @param paragraphs 变量持续的段落信息
	 * @param varString  变量信息
	 * @return
	 */
	private boolean reformHeadFootParagraph(List<XWPFParagraph> y_paragraphs, List<XWPFParagraph> varParagraphs, String varString) {
		if (varParagraphs == null || varParagraphs.isEmpty() || StringUtil.isBlank(varString))
			return false;

		StringBuffer t_varString = null;
		boolean isFind = false;
		//结束标记所在的Run位置
		int endPos = -1;
		List<Integer> waitList = new ArrayList<>();

		//因为要删除相应的字段，为了保证正确，只能从后往前循环
		for (int i = varParagraphs.size() - 1; i >= 0; i--) {
			//如果为持续段落里的中间段落，则删除
			if (i > 0 && i != (varParagraphs.size() - 1)) {
				//文本放到变量中，用于判断是否是需要操作的变量，因为是倒循环，所以用要用插入
				t_varString.insert(0, varParagraphs.get(i).getText());
				y_paragraphs.remove(varParagraphs.get(i));
				continue;
			}

			//如果切换段落，则代表等待队列里的信息都可以删除
			if (!waitList.isEmpty()) {
				delWaitList(varParagraphs.get(i + 1), waitList, varString);
			}
			List<XWPFRun> xwpfRuns = varParagraphs.get(i).getRuns();
			//如果持续段落不只是一个，则对第一个段落里的« 后的run都进行删除
			for (int j = xwpfRuns.size() - 1; j >= 0; j--) {

				String textString = xwpfRuns.get(j).getText(xwpfRuns.get(j).getTextPosition());
				//已到结束标记，Run里不存在数据，则删除
				if (endPos > 0 && StringUtil.isBlank(textString)) {
					//paragraphs.get(i).removeRun(j);
					waitList.add(j);
					continue;
				} else if (textString == null) {
					continue;
				}

				while (true) {
					//找到结束标记，则开始记录
					int endTagPos = 0;
					if (endPos < 0) {
						endTagPos = textString.lastIndexOf("»");
						if (endTagPos != -1) {
							t_varString = new StringBuffer();
							t_varString = t_varString.insert(0, textString.substring(0, endTagPos));
							waitList.clear();
							endPos = j;
						}
					}

					//如果找到结束标记
					if (endPos > 0) {
						//不管有没有找到开始标记，则把当前的Run放到等待删除队列中
						waitList.add(j);
						//查找开始标记
						int startTagPos = textString.lastIndexOf("«");
						//找到开始标记，并且两个在同一个run里，则只截取相应信息放到变量名称里，并设置标记为true
						if (startTagPos > -1 && endPos == j && i == (varParagraphs.size() - 1) && startTagPos < endTagPos) {
							isFind = true;
							t_varString = new StringBuffer();
							t_varString = t_varString.insert(0, textString.substring(startTagPos + 1, endTagPos));
						}
						//找到开始标记，两个不在同一个run里，则截取结束为止的文字，并置标记为true
						else if (startTagPos > -1 && endPos != j) {
							isFind = true;
							t_varString = t_varString.insert(0, textString.substring(startTagPos + 1));
						} else if (endPos != j) {
							t_varString = t_varString.insert(0, textString);
							break;
						} else {
							break;
						}
						//已找到变量，则跳出进行判断是否为需要查找的变量
                        //如果找到的变量是正确的，则根据删除队列进行删除信息
                        if (varString.equals(t_varString.toString())) {
                            //如果在同一个run，则无需删除信息
                            if (waitList.size() > 1) {
                                delWaitList(varParagraphs.get(i), waitList, varString);
                            }
                            break;
                        } else {
                            //找到的变量不正确,重新初始化
                            t_varString = null;
                            isFind = false;
                            endPos = -1;
                            waitList.clear();
                            //当前run重新计算
                            textString = textString.substring(0, startTagPos);
                            //未找到则继续下一个run
                        }
                    } else {
						break;
					}
				}
				if (isFind) {
					break;
				}
			}

			if (isFind) {
				break;
			}
		}

		return true;
	}

	/**
	 * 根据删除队列删除相应的信息
	 *
	 * @param paragraph 当前段落
	 * @param waitList  等待删除队列
	 * @param varString 变量名称
	 * @return
	 */
	private boolean delWaitList(XWPFParagraph paragraph, List<Integer> waitList, String varString) {

		if (waitList==null||waitList.isEmpty())
			return false;

		for (int i = 0; i < waitList.size(); i++) {
			if (i == 0) {
				//起始run要截取到结束标记
				XWPFRun run = paragraph.getRuns().get(waitList.get(i));
				String textString = run.getText(run.getTextPosition());
				int endPos = textString.indexOf("»");
				if (endPos > -1) {
					run.setText(textString.substring(endPos + 1), 0);
				} else {
					//删除中间的run
					paragraph.removeRun(waitList.get(i));
				}
			} else if (i == (waitList.size() - 1)) {
				//起始run要截取到开始标记
				XWPFRun run = paragraph.getRuns().get(waitList.get(i));
				String textString = run.getText(run.getTextPosition());
				int startPos = textString.lastIndexOf("«");
				if (startPos > -1) {
					run.setText(textString.substring(0, startPos + 1) + varString + "»", 0);
				} else {
					//删除中间的run
					paragraph.removeRun(waitList.get(i));
				}
			} else {
				//删除中间的run
				paragraph.removeRun(waitList.get(i));
			}
		}

		return true;
	}
		public void replaceFinanceTemplate() {
		Map<String, Object> resultMap = new HashMap<>();
			Map<String, Object> map_t = new HashMap<>();
		String releasePath = (String) map.get("releaseFilePath"); //正式文件路径
			FileOutputStream output =null;
			XSSFWorkbook workbook =null;
		String tempPath = (String) map.get("tempPath"); //临时文件路径
			redisUtil.hset("EXP_:_"+ wjid,"Fin", false,3600);
			redisUtil.hset("EXP_:_"+ wjid,"Cnt", "0");
		try {
			String templateFilePath = map.get("wjlj") != null ? map.get("wjlj").toString() : "";
			DBEncrypt dbEncrypt = new DBEncrypt();
			File templateFile = new File(dbEncrypt.dCode(templateFilePath));
			if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
				resultMap.put("message", "模板不存在！请重新确认模板！");
				resultMap.put("status", "error");
			}
			 workbook = new XSSFWorkbook(new FileInputStream(templateFile));
			workbook.setSheetName(0, map.get("year")+"-"+ map.get("mbmc"));
			Sheet xssfSheet = workbook.getSheetAt(0);
			File file = new File(tempPath + BusTypeEnum.FINANCE_ACCOUNT.getCode());
			if (file.mkdirs()){
				file.mkdir();
			}
			replaceSheet(map,xssfSheet);
			replaceSheetFromTwo((List<Map<String, Object>>) map.get("maplist"),xssfSheet);
			String pathname= tempPath + BusTypeEnum.FINANCE_ACCOUNT.getCode() + "/" + map.get("year") + "-" + map.get("mbmc") + "-" + new Date().getTime() + ".xls";
			// 输出Excel文件
			 output = new FileOutputStream(pathname);
			// 设置文件头
//			httpResponse.setHeader("Content-Disposition",
//					"attchement;filename=" + new String((pathname + ".xls").getBytes("gb2312"), "ISO8859-1"));
//			httpResponse.setContentType("application/msexcel");
			workbook.write(output);
			output.flush();
			redisUtil.hset("EXP_:_"+wjid, "Fin",true);
			redisUtil.hset("EXP_:_"+wjid,"fileName", map.get("year") + "医检所收入对比表"  + ".xls");
			redisUtil.hset("EXP_:_"+wjid,"filePath", pathname);
		} catch (Exception e) {
			e.printStackTrace();
			redisUtil.hset("EXP_:_"+wjid, "Fin",true);
		} finally{
			try{
				if(output!=null){
					output.flush();
					output.close();
				}
				if(workbook!=null)
					workbook.close();
			}catch(Exception ex){
				redisUtil.hset("EXP_:_"+wjid,"Msg",ex.getMessage());
			}
		}
		}
	/**
	 * 替换Excel模板文件内容
	 *
	 * @param replaceMap 文档数据
	 */
	public static void replaceSheet(Map<String, Object> replaceMap, Sheet sheet) {
		try {
			Row row = sheet.getRow(0);
			int num = row.getLastCellNum()+1;
			for (int i = 0; i < num; i++) {
				Cell cell = row.getCell(i);
				if (cell == null ||cell.getCellType()!=CellType.STRING || cell.getStringCellValue() == null) {
						continue;
					}
					String value = cell.getStringCellValue();
					int startTagPos = value.indexOf("«");
					int endTagPos = value.indexOf("»");
					if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						cell.setCellValue(replaceValue(value, replaceMap,startTagPos,endTagPos));
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 替换Excel模板文件内容
	 *
	 * @param replaceMap 文档数据
	 */
	public static void replaceSheetFromTwo(List<Map<String, Object>> listmap, Sheet sheet) {
		try {
			if(listmap!=null&&!listmap.isEmpty()) {
				Row row = sheet.getRow(0);
				int num = row.getLastCellNum()+1;

				for (int i = 0; i < listmap.size(); i++) {
					Row row_t = sheet.createRow(i+1);
					for (int j = 0; j < num; j++) {
						Cell cell = row_t.createCell(j);
						Cell keyCell=row.getCell(j);
						if(keyCell!=null){
							String key=keyCell.getStringCellValue();
							cell.setCellValue(listmap.get(i).get(key)+"");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 替换内容
	 *
	 * @param value
	 * @param replaceMap
	 * @return
	 */
	public static String replaceValue(String value, Map<String, Object> replaceMap,int startTagPos,int endTagPos) {
		String str = value.substring(startTagPos+1, endTagPos);
		if (StringUtil.isBlank(str)) {
			return value;
		}
		value = value.replace("«" + str + "»", replaceMap.get(str)!=null?(replaceMap.get(str).toString()):"");
		return value;
	}
	/**
	 * 新增一行数据
	 */
	public static void copy(int num,Row row,Row row1){
		for (int i = 0; i < num; i++) {
			Cell cell = row.getCell(i);
			if (cell == null || cell.getCellType() != CellType.STRING || cell.getStringCellValue() == null) {
				continue;
			}
			Cell cell1=row1.createCell(i);
			if (StringUtil.isNotBlank(cell.getStringCellValue())){
				cell1.setCellValue(cell.getStringCellValue());
			}
		}
	}
}
