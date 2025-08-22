package com.matridx.igams.experiment.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.experiment.dao.entities.WksjmxDto;
import com.matridx.igams.experiment.service.svcinterface.IWksjmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PoolingUtil {
	//日志
	private Logger log = LoggerFactory.getLogger(PoolingUtil.class);
	
	private BigDecimal MAX_DATASIZE = new BigDecimal("5.5");
	private BigDecimal MAX_DBLSIZE = new BigDecimal("8");

	/**
	 * 处理
	 *
	 * @param exportPath
	 * @param redisUtil
	 * @param map
	 * @param wksjmxList
	 */
	public void dealPooling(String exportPath, String wjm, RedisUtil redisUtil, Map<String, Object> map, List<WksjmxDto> wksjmxList, Map<String, Object> setting, IWksjmxService wksjmxService, IFjcfbService fjcfbService) {
		log.error("开始 dealPooling 导出-----" + exportPath);
		if (map.get("cskz1") == null) {
			Object detectionExtend = redisUtil.hget("matridx_xtsz", "pooling.detection.extend");
			if (detectionExtend != null) {
				com.alibaba.fastjson.JSONObject job = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detectionExtend));
				map.put("cskz1", job.getString("szz"));
			}
		}
		String wkid = (String) map.get("wkid");
		Map<String, Object> poolingInfo = JSONObject.parseObject(map.get("cskz1").toString(), new TypeReference<>() {});
		poolingInfo.put("sjxz", poolingInfo.get("sjxz"));
		poolingInfo.put("qcnt", map.get("qcnt"));
		poolingInfo.put("tcnt", map.get("tcnt"));
		if (map.get("sjzl") != null && StringUtil.isNotBlank(map.get("sjzl").toString())) {
			poolingInfo.put("sjzl", map.get("sjzl").toString());
		}
		log.error("开始获取模板文件...");
		setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "isContinue", "正在获取模板文件...");
		if (!CollectionUtils.isEmpty(wksjmxList)) {
			FileInputStream fileInputStream = null;
			FileOutputStream out = null;
			Workbook workbook = null;
			try {
				//2、开始替换excle表
				//2.1、读取excle文件
				fileInputStream = new FileInputStream(exportPath);
				workbook = WorkbookFactory.create(fileInputStream);
				//2.2、获取需要替换的sheet表
				List<Map<String, Object>> sheetList = new ArrayList<>();
				int numberOfSheets = workbook.getNumberOfSheets();
				if (numberOfSheets == 2) {
					Map<String, Object> sheetTpool = new HashMap<>();
					sheetTpool.put("sheetName", "tpool");
					sheetTpool.put("wkStartRowIndex", 3);//文库开始行数
					sheetTpool.put("wkmcIn", "-tNGS-,-tNGSA-,-HS-,MDL004-NC-,MDL004-PC-,MDL005-tNC-,MDL005-tPC-,MDL004-mNC-,MDL004-mPC-");
					sheetList.add(sheetTpool);
					Map<String, Object> sheetAll = new HashMap<>();
					sheetAll.put("sheetName", "全");
					sheetAll.put("wkStartRowIndex", 4);//文库开始行数
					sheetAll.put("wkmcOut", "-tNGS-,-tNGSA-,-HS-,MDL004-NC-,MDL004-PC-,MDL005-tNC-,MDL005-tPC-,MDL004-mNC-,MDL004-mPC-");
					sheetList.add(sheetAll);
					dealMultipleSheets(redisUtil, wkid+"_"+map.get("timezone").toString(), poolingInfo, workbook, wksjmxList, sheetList, setting);
				} else if (numberOfSheets == 1) {
					Map<String, Object> sheetAll = new HashMap<>();
					sheetAll.put("sheetName", "全");
					sheetAll.put("wkStartRowIndex", 3);//文库开始行数
					sheetList.add(sheetAll);
					dealSingleSheet(redisUtil, wkid+"_"+map.get("timezone").toString(), poolingInfo, workbook, wksjmxList, sheetList, setting);
				}
				setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "isContinue", "正在保存文件...");
				// 导出后处理
				afterPoolingDeal(workbook,wksjmxList,wksjmxService);
			} catch (Exception e) {
				log.error("pooling导出-----异常:" + e.getMessage());
				try {
					if (fileInputStream != null)
						fileInputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					log.error("pooling导出-----文件关闭出错：" + e1.getMessage());
				}
				setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "isFinish", "导出失败！" + e.getMessage());
			} finally {
				try {
					//2.4、保存替换后的excle文件
					out = new FileOutputStream(exportPath);
					if (workbook != null) {
						workbook.write(out);
					}
					if (out != null) {
						out.close();
					}
					setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "isFinish", "导出完成！");
					setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "fileName", "导出完成！");
					setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "filePath", "导出完成！");
					savePoolingFile(exportPath,wjm,wkid,fjcfbService);
					log.error("pooling导出-----结束");
				} catch (IOException e) {
					log.error("pooling导出2-----异常:" + e.getMessage());
					setPoolingRedisStatus(redisUtil, wkid+"_"+map.get("timezone").toString(), "isFinish", "导出失败！" + e.getMessage());
				}
			}
		}
	}

	public void savePoolingFile(String filepath,String wjm,String wkid,IFjcfbService fjcfbService){
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(StringUtil.generateUUID());
		fjcfbDto.setYwid(wkid);
		fjcfbDto.setZywid("");
		fjcfbDto.setXh("1");
		fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_POOLING_EXPORT.getCode());
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(filepath));
		fjcfbDto.setFwjlj(bpe.eCode(filepath.replace("/"+wjm,"")));
		fjcfbDto.setFwjm(bpe.eCode(filepath.substring(filepath.lastIndexOf("/")) + 1));
		fjcfbDto.setZhbj("0");
		fjcfbService.insert(fjcfbDto);
	}

	/**
	 * pooing文件下载处理
	 */
	public void poolingDownloadExport(RedisUtil redisUtil, String wkid,String timezone, HttpServletResponse response) {
		InputStream in = null;
		OutputStream out = null;
		try {
			String filePath = (String) redisUtil.hget("POOLING_EXPORT:" + wkid+"_"+timezone, "filePath");
			String fileName = (String) redisUtil.hget("POOLING_EXPORT:" + wkid+"_"+timezone, "fileName");

			//设置Content-Disposition
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
			response.setContentType("application/octet-stream; charset=utf-8");
			//response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			//读取目标文件，通过response将目标文件写到客户端
			//获取目标文件的绝对路径
			//读取文件
			in = new FileInputStream(filePath);
			out = response.getOutputStream();

			//创建缓冲区
			byte[] buffer = new byte[1024];
			int len;
			//循环将输入流中的内容读取到缓冲区当中
			while ((len = in.read(buffer)) > 0) {
				//输出缓冲区的内容到浏览器，实现文件下载
				out.write(buffer, 0, len);
			}
			//写文件
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			redisUtil.del("POOLING_EXPORT:" + wkid+"_"+timezone);
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
	}

	/**
	 * 获取POOLING导出的信息
	 *
	 * @param redisUtil
	 * @param fileid
	 * @return
	 */
	public Map<String, String> getPoolingRedisStatus(RedisUtil redisUtil, String fileid) {
		if (StringUtil.isNotBlank(fileid)) {
			Map<String, String> statusMap = new HashMap<>();
			if (isStatusTrue(redisUtil, "isCancel", fileid)) {
				statusMap.put("status", "cancel");
			} else if (isStatusTrue(redisUtil, "isFinish", fileid)) {
				statusMap.put("status", "finish");
				statusMap.put("fileName", (String) redisUtil.hget("POOLING_EXPORT:" + fileid, "fileName"));
				statusMap.put("filePath", (String) redisUtil.hget("POOLING_EXPORT:" + fileid, "filePath"));
			} else if (isStatusTrue(redisUtil, "isContinue", fileid)) {
				statusMap.put("status", "continue");
			}
			statusMap.put("message", (String) redisUtil.hget("POOLING_EXPORT:" + fileid, "message"));
			return statusMap;
		}
		return null;
	}

	/**
	 * 判断状态是否为true
	 *
	 * @param redisUtil redis工具类
	 * @param statusStr 导出状态：[isCancel,isFinish,isContinue]
	 * @param fileid	导出文件id
	 * @return
	 */
	public boolean isStatusTrue(RedisUtil redisUtil, String statusStr, String fileid) {
		boolean isStatusTrue = false;
		if (redisUtil.hget("POOLING_EXPORT:" + fileid, statusStr) != null) {
			isStatusTrue = (Boolean) redisUtil.hget("POOLING_EXPORT:" + fileid, statusStr);
		}
		return isStatusTrue;
	}

	/**
	 * 设置POOLING导出的状态
	 *
	 * @param redisUtil redis工具类
	 * @param fileid	导出文件id
	 * @param status	导出状态：[isCancel,isFinish,isContinue]
	 * @param message   导出状态信息
	 */
	public void setPoolingRedisStatus(RedisUtil redisUtil, String fileid, String status, String message) {
		setPoolingRedisStatus(redisUtil, fileid, status, message, null, null);
	}

	/**
	 * 设置POOLING导出的状态
	 *
	 * @param redisUtil redis工具类
	 * @param fileid	导出文件id
	 * @param status	导出状态：[isCancel,isFinish,isContinue]
	 * @param message   导出状态信息
	 * @param fileName  导出文件名称
	 * @param filePath  导出文件路径
	 */
	public void setPoolingRedisStatus(RedisUtil redisUtil, String fileid, String status, String message, String fileName, String filePath) {
		if (StringUtil.isNotBlank(status) && "start".equals(status)) {
			redisUtil.hset("POOLING_EXPORT:" + fileid, "isCancel", false, 3600);
			redisUtil.hset("POOLING_EXPORT:" + fileid, "isFinish", false, 3600);
			redisUtil.hset("POOLING_EXPORT:" + fileid, "isContinue", true, 3600);
			redisUtil.hset("POOLING_EXPORT:" + fileid, "message", StringUtil.isNotBlank(message) ? message : "导出中......", 3600);
			if (!StringUtil.isAnyBlank(fileName, filePath)) {
				redisUtil.hset("POOLING_EXPORT:" + fileid, "fileName", fileName, 3600);
				redisUtil.hset("POOLING_EXPORT:" + fileid, "filePath", filePath, 3600);
			}
			return;
		}
		if (isStatusTrue(redisUtil, "isCancel", fileid)) {
			throw new RuntimeException("导出任务已取消！");
		}
		if (StringUtil.isNotBlank(status)) {
			switch (status) {
				case "isCancel":
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isCancel", true, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isFinish", false, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isContinue", false, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "message", StringUtil.isNotBlank(message) ? message : "取消导出！", 3600);
					break;
				case "isFinish":
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isCancel", false, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isFinish", true, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isContinue", false, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "message", StringUtil.isNotBlank(message) ? message : "导出完成！", 3600);
					break;
				case "isContinue":
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isCancel", false, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isFinish", false, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "isContinue", true, 3600);
					redisUtil.hset("POOLING_EXPORT:" + fileid, "message", StringUtil.isNotBlank(message) ? message : "导出中......", 3600);
					break;
			}
		}
		if (!StringUtil.isAnyBlank(fileName, filePath)) {
			redisUtil.hset("POOLING_EXPORT:" + fileid, "fileName", fileName, 3600);
			redisUtil.hset("POOLING_EXPORT:" + fileid, "filePath", filePath, 3600);
		}
	}

	/**
	 * 部分单元格设置默认值
	 * V:5~11,V:13~14,V:17,V:26~27,AA:5~6,Z:7
	 * U:5~11,U:13~14,U:17,U:26~27
	 */
	private void setDefaultCell(Sheet sheet, Map<String, Object> map, Workbook workbook) {
		//1、设置“文库参考体积”,U:5,V:5
		setDefaultValue(sheet, "V:5", "U:5", "wkcktj", map, workbook);
		//2、设置“稀释文库参考体积”,U:6,V:6
		setDefaultValue(sheet, "V:6", "U:6", "xswkcktj", map, workbook);
		//3、设置“稀释倍数”,U:7,V:7
		setDefaultValue(sheet, "V:7", "U:7", "xsbs", map, workbook);
		//4、设置“定量使用“原液”还是“稀释液””,U:8,V:8
		setDefaultValue(sheet, "V:8", "U:8", "dly", map, workbook);
		//5、设置“文库定量标曲片段长度(bp)”,U:9,V:9
		setDefaultValue(sheet, "V:9", "U:9", "bp", map, workbook);
		//6、设置“预计每run下机数据总量(M read)”,U:10,V:10
		//单表多表存在出入，故此处注释
		//setDefaultValue(sheet,"V:10","U:10","sjzl",map,workbook);
		//7、设置“混样系数（数值越大，混样浓度越高）”,U:11,V:11
		setDefaultValue(sheet, "V:11", "U:11", "hyxs", map, workbook);
		//8、设置“血液样本的平均片段大小(bp)”,U:13,V:13
		setDefaultValue(sheet, "V:13", "U:13", "xybp", map, workbook);
		//9、设置“其他样本的平均片段大小(bp)”,U:14,V:14
		setDefaultValue(sheet, "V:14", "U:14", "qtbp", map, workbook);
		//10、设置“数据量/pooling 系数（M reads）”,U:17,V:17
		setDefaultValue(sheet, "V:17", "U:17", "cnsd", map, workbook);
		//11、设置“混合文库稀释体积(ul)”,U:26,V:26
		setDefaultValue(sheet, "V:26", "U:26", "hhwkxstj", map, workbook);
		//12、设置“混合文库稀释浓度设定(pM)”,U:27,V:27
		setDefaultValue(sheet, "V:27", "U:27", "hhwkxsnd", map, workbook);
		//13、设置“预pooling终体积(uL)”,AA:5,Z:5
		setDefaultValue(sheet, "AA:5", "Z:5", "ypztj", map, workbook);
		//14、设置“上机混合文库终浓度(pM)”,AA:6,Z:6
		setDefaultValue(sheet, "AA:6", "Z:6", "sjhhwkznd", map, workbook);
		//15、根据9、10，计算“需要的混合文库摩尔数（e-15 mol)”,Z:7
		calculateCell("Z", sheet.getRow(6), workbook);
		//设置“试剂选择”,Y:27
		setDefaultValue(sheet, "Y:27", null, "sjxz", map, workbook);
	}

	/**
	 * 设置默认值，设定值
	 *
	 * @param sheet	sheet
	 * @param sdNumber 设定值位置，A:1,AA:2
	 * @param mrNumber 默认值位置，A:1,AB:3
	 * @param key	  key
	 * @param map	  map
	 * @param workbook workbook
	 */
	private void setDefaultValue(Sheet sheet, String sdNumber, String mrNumber, String key, Map<String, Object> map, Workbook workbook) {
		if (StringUtil.isNotBlank(key) && map != null && map.get(key) != null && StringUtil.isNotBlank(map.get(key).toString())) {
			if (StringUtil.isNotBlank(sdNumber)) {
				//设定值
				int cellSdNum = calculateXPosition(sdNumber.split(":")[0]);
				int rowSdNum = Integer.parseInt(sdNumber.split(":")[1]) - 1;
				Cell cellSD = sheet.getRow((short) rowSdNum).getCell((short) cellSdNum);
				if (cellSD == null) {
					cellSD = sheet.getRow((short) rowSdNum).createCell(cellSdNum);
				}
				String value = map.get(key).toString();
				try {
					cellSD.setCellValue(new BigDecimal(value).doubleValue());
				} catch (Exception e) {
					cellSD.setCellValue(value);
				}
			}
			if (StringUtil.isNotBlank(mrNumber)) {
				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				//默认值 计算
				int cellMrNum = calculateXPosition(mrNumber.split(":")[0]);
				int rowMrNum = Integer.parseInt(mrNumber.split(":")[1]) - 1;
				Cell cellMR = sheet.getRow((short) rowMrNum).getCell((short) cellMrNum);
				CellValue cellMRvalue = evaluator.evaluate(cellMR);
				cellMR.setCellValue(cellMRvalue.formatAsString().replaceAll("\"", ""));
			}
		}
	}

	/**
	 * 计算excle横坐标的下标值
	 *
	 * @param xPosition
	 * @return
	 */
	private int calculateXPosition(String xPosition) {
		int num = -1;
		for (int i = 0; i < xPosition.length(); i++) {
			int a = xPosition.substring(i, i + 1).getBytes(StandardCharsets.UTF_8)[0] - 64;
			int pow = (int) Math.pow(26, xPosition.length() - i - 1);
			num = num + a * pow;
		}
		return num;
	}

	/**
	 * 计算行中的单个元值，并返回
	 *
	 * @param cellNumber 单元格序号，A,B,C...
	 * @param row		行
	 * @param workbook   workbook
	 * @return
	 */
	private String calculateCell(String cellNumber, Row row, Workbook workbook) {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		//获取需要替换的单元格序号（0开始）
		int cellIndex = calculateXPosition(cellNumber);
		Cell cell = row.getCell(cellIndex);
		//替换FINDB函数为FIND函数（引入自定义函数不生效情况下使用）
		cell.setCellFormula(cell.getCellFormula().replaceAll("FINDB", "FIND"));
		//根据公式进行计算
		CellValue value = evaluator.evaluate(cell);
		String text = value.formatAsString().replaceAll("\"", "");
		//将计算结果赋值给单元格
		try {
			cell.setCellValue(new BigDecimal(text).doubleValue());
		} catch (Exception e) {
			cell.setCellValue(text);
		}

		return text;
	}

	/**
	 * 设置A、B列的值
	 *
	 * @param startRowIndex
	 * @param wksjmxList
	 * @param sheet
	 * @throws BusinessException
	 */
	private void setAandBColumnValue(int startRowIndex, List<WksjmxDto> wksjmxList, Sheet sheet,Workbook workbook) throws BusinessException {
		StringBuffer errorString = new StringBuffer();
		int v_cellIndex = calculateXPosition("V");
		int p_cellIndex = calculateXPosition("P");
		int q_cellIndex = calculateXPosition("Q");
		Short[] bgcolors = new Short[] {
			IndexedColors.AQUA.getIndex(),
			IndexedColors.LIGHT_YELLOW.getIndex(),
			IndexedColors.LIGHT_ORANGE.getIndex(),
			IndexedColors.LAVENDER.getIndex(),
			IndexedColors.PALE_BLUE.getIndex(),
			IndexedColors.LIGHT_GREEN.getIndex(),
			IndexedColors.LIGHT_TURQUOISE.getIndex(),
			IndexedColors.TURQUOISE.getIndex(),
			IndexedColors.LEMON_CHIFFON.getIndex(),
			IndexedColors.TAN.getIndex(),
			IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex(),
			IndexedColors.CORAL.getIndex()
		};
		for (int i = 0; i < wksjmxList.size(); i++) {
			Row row = sheet.getRow((short) i + startRowIndex);
			if (row != null) {
				//3.1、替换“文库名称”，第一列
				Cell cell_wkmc = row.getCell((short) 0);
				if (cell_wkmc == null) {
					cell_wkmc = row.createCell(0);
				}
				cell_wkmc.setCellValue(wksjmxList.get(i).getWkbm());
				//3.2、替换“浓度Quantity”，第二列
				Cell cell_nd = row.getCell((short) 1);
				if (cell_nd == null) {
					cell_nd = row.createCell(1);
				}
				//原液加样体积
				Cell cell_p = row.getCell(p_cellIndex);
				if (cell_p == null) {
					cell_p = row.createCell(15);
				}
				//稀释液加样体积
				Cell cell_q = row.getCell(q_cellIndex);
				if (cell_q == null) {
					cell_q = row.createCell(16);
				}
				
				if (StringUtil.isBlank(wksjmxList.get(i).getDlnd()) && StringUtil.isBlank(wksjmxList.get(i).getWknd())) {
					errorString.append("请完善" + (StringUtil.isNotBlank(wksjmxList.get(i).getWkbm()) ? wksjmxList.get(i).getWkbm() : "") + "的定量浓度。\r\n");
				}
				try {
					if(StringUtil.isNotBlank(wksjmxList.get(i).getDlnd()))
						cell_nd.setCellValue(new BigDecimal(wksjmxList.get(i).getDlnd()).doubleValue());
					else {
						BigDecimal xsbs = new BigDecimal("20");
						try {
							xsbs = new BigDecimal(sheet.getRow(6).getCell(v_cellIndex).getNumericCellValue());
						}catch (Exception e) {
							log.error("V12无法获取数据。");
						}
						cell_nd.setCellValue(new BigDecimal(wksjmxList.get(i).getWknd()).divide(xsbs,3,RoundingMode.HALF_UP).doubleValue());
					}
					//存在文库明细序号的情况下
					if(StringUtil.isNotBlank(wksjmxList.get(i).getWkmxxh())) {
						BigDecimal wkxh = new BigDecimal(wksjmxList.get(i).getWkmxxh()).divide(new BigDecimal("8"),0,RoundingMode.UP);
						
						CreationHelper createHelper = workbook.getCreationHelper();
						CellStyle t_cellstyle = workbook.createCellStyle();
						t_cellstyle.setFillForegroundColor(bgcolors[(wkxh.intValue()-1)%11]);
						t_cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						t_cellstyle.setBorderTop(BorderStyle.THIN);
						t_cellstyle.setBorderBottom(BorderStyle.THIN);
						t_cellstyle.setBorderLeft(BorderStyle.MEDIUM);
						t_cellstyle.setBorderRight(BorderStyle.MEDIUM);
						t_cellstyle.setDataFormat(createHelper.createDataFormat().getFormat("0.00"));
						cell_p.setCellStyle(t_cellstyle);
						CellStyle t2_cellstyle = workbook.createCellStyle();
						t2_cellstyle.setFillForegroundColor(bgcolors[(wkxh.intValue()-1)%11]);
						t2_cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						t2_cellstyle.setBorderTop(BorderStyle.THIN);
						t2_cellstyle.setBorderBottom(BorderStyle.THIN);
						t2_cellstyle.setBorderLeft(BorderStyle.MEDIUM);
						t2_cellstyle.setBorderRight(BorderStyle.MEDIUM);
						t2_cellstyle.setDataFormat(createHelper.createDataFormat().getFormat("0.00"));
						cell_q.setCellStyle(t2_cellstyle);
					}
				} catch (Exception e) {
					cell_nd.setCellValue(wksjmxList.get(i).getDlnd());
				}
			}
		}
		if(errorString.length() > 0)
			throw new BusinessException(errorString.toString());
	}

	/**
	 * 处理单个工作表
	 *
	 * @param redisUtil
	 * @param rediskey
	 * @param map
	 * @param workbook
	 * @param wksjmxList
	 * @param sheetList
	 * @throws BusinessException
	 */
	public void dealSingleSheet(RedisUtil redisUtil, String rediskey, Map<String, Object> map, Workbook workbook, List<WksjmxDto> wksjmxList, List<Map<String, Object>> sheetList, Map<String, Object> setting) throws BusinessException {
		//初始化部分值
		int u_cellIndex = calculateXPosition("U");
		int v_cellIndex = calculateXPosition("V");
		int z_cellIndex = calculateXPosition("Z");
		for (Map<String, Object> sheetMap : sheetList) {
			String sheetName = (String) sheetMap.get("sheetName");
			int wkStartRowIndex = (Integer) sheetMap.get("wkStartRowIndex");
			//获取需要替换的sheet表
			Sheet sheet = workbook.getSheet(sheetName);
			//log.error("V01 初始minMol=" + sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue());
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）");
			//1、设置强制重新计算公式
			sheet.setForceFormulaRecalculation(true);
			//2、设置固定参数
			//V:5~11,V:13~14,V:17,V:26~27,AA:5~6,Z:7
			//U:5~11,U:13~14,U:17,U :26~27
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在设置默认值");
			dealMapAndSetting(map, setting, true);
			setPoolingFactorSheet(sheet,map);
			//log.error("V02 初始minMol=" + sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue() + " " + JSON.toJSONString(map));
			setDefaultValue(sheet, "V:10", "U:10", "sjzl", map, workbook);
			//log.error("V03 初始u_cellIndex=" + sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue());
			setDefaultCell(sheet, map, workbook);
			//log.error("V04 初始u_cellIndex=" + sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue());
			//3、获取需要替换的sheet表的“文库名称”和“浓度Quantity”列，默认从第4行开始替换
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在设置文库名称、浓度Quantity");
			setAandBColumnValue(wkStartRowIndex, wksjmxList, sheet,workbook);
			//log.error("V05 初始v_cellIndex=" + sheet.getRow(10).getCell(v_cellIndex).getNumericCellValue());
			/** 4、根据“文库名称”和“浓度Quantity”列，计算并赋值各列的值
			 *  片段大小：				D列，受U:13,U:14影响
			 *  原液体积：				F列，受U:5影响					(U:5已在setDefaultCell方法中赋值)
			 *  文库原液浓度(pM)：			G列，受D列,U:7,U:8,U:9影响		(U:7~9已在setDefaultCell方法中赋值)
			 *  稀释液体积：				I列，受U:6影响					(U:6已在setDefaultCell方法中赋值)
			 *  文库稀释液浓度(pM)：		J列，受D列,U:7,U:8,U:9影响		(U:7~9已在setDefaultCell方法中赋值)
			 *  稀释液总摩尔量：			L列，受I列,J列影响
			 *  摩尔量排序：				K列，受L列影响
			 *  pooling系数：			M列，受U~V:39~48影响(pooling系数批量调整)(列数不固定)
			 *  文库原液加样体积（μL）：	P列，受F列,G列,I列,J列,M列,U:12影响	(U:12为可调参数)
			 *  文库稀释液加样体积（μL）：	Q列，受J列,M列,U:12影响			(U:12为可调参数)
			 */
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在根据“文库名称”和“浓度Quantity”列，计算并赋值各列的值");
			//sheet.getRow(11).getCell(v_cellIndex).setCellValue(50);
			//为防止默认混样系数采用计算出U12，故需要设置V12的初始值为0
			sheet.getRow(11).getCell(v_cellIndex).setCellValue(1);
			
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			//log.error("V06 初始v_cellIndex=" + sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue());
			//log.error("V06 初始u_cellIndex=" + sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue());
			//MRead在生成excle之前就已设定，故不需要计算
//			calculatorPcAndNcInPRow(sheet,workbook);
			//5、产能设定相关计算
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在计算 产能设定相关数据 中");
			//5.1、计算“剩余产能（M reads）”，U:18
			//calculatorMRead(redisUtil,rediskey,workbook,sheet,u_cellIndex,mReads,mReadMixs);
			//5.1 根据是否混合文库确定是否调整Q和Q-NC，Q-PC的稀释
			calculatorQmNGS(workbook,sheet,map);
			//5.2、根据“混样系数（数值越大，混样浓度越高）”，“单文库最低加入摩尔量”相关计算
			calculatorMolAndVolume(redisUtil, rediskey, workbook, sheet, u_cellIndex, z_cellIndex,map);
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在保存工作表（" + sheet.getSheetName() + "）");
		}
	}

	/**
	 * 处理多工作表
	 *
	 * @param redisUtil
	 * @param rediskey
	 * @param map
	 * @param workbook
	 * @param wksjmxList
	 * @param sheetList
	 * @throws BusinessException
	 */
	public void dealMultipleSheets(RedisUtil redisUtil, String rediskey, Map<String, Object> map, Workbook workbook, List<WksjmxDto> wksjmxList, List<Map<String, Object>> sheetList, Map<String, Object> setting) throws BusinessException {
		//初始化部分值
		dealSheets(redisUtil, rediskey, map, workbook, wksjmxList, sheetList.get(0), setting, false);
		dealSheets(redisUtil, rediskey, map, workbook, wksjmxList, sheetList.get(1), setting, true);
		calculatorFirstOfSheets(redisUtil, rediskey, workbook, sheetList.get(0),setting);
		calculatorSecondOfSheets(redisUtil, rediskey, workbook, sheetList.get(1),setting);
		//最后再次判断tpool的U:22是否大于U:21
		Sheet tpoolSheet = workbook.getSheet((String) sheetList.get(0).get("sheetName"));
		Cell v22 = tpoolSheet.getRow(21).getCell(calculateXPosition("V"));
		Cell u22 = tpoolSheet.getRow(21).getCell(calculateXPosition("U"));
		try {
			if (v22.getCellFormula().contains("INT(MAX(INT(SUM(M:M)*10/SUM(全!M:M)/10*4000*6),全!R4/SUM(全!R:R)*4000*6))")) {
				String minValue = v22.getStringCellValue().substring(1);
				BigDecimal tpoolV22Value = new BigDecimal(minValue);
				BigDecimal tpoolU22Value = new BigDecimal(u22.getNumericCellValue());
				if (tpoolV22Value.compareTo(tpoolU22Value) > 0) {
					needDiluteDeal(tpoolSheet, workbook, tpoolV22Value,new BigDecimal(String.valueOf(setting.get("TheMinimum"))),false);
				}
			}
		} catch (Exception e) {
			log.error("tpoolSheet---V:22 转换失败！" + e.getMessage());
		}
	}

	/**
	 * 处理多表的第一张表
	 *
	 * @param redisUtil
	 * @param wkid
	 * @param map
	 * @param workbook
	 * @param wksjmxList
	 * @param sheetMap
	 * @param setting
	 * @param isAll
	 * @throws BusinessException
	 */
	private void dealSheets(RedisUtil redisUtil, String wkid, Map<String, Object> map, Workbook workbook, List<WksjmxDto> wksjmxList, Map<String, Object> sheetMap, Map<String, Object> setting, boolean isAll) throws BusinessException {
		//获取工作表
		String sheetName = (String) sheetMap.get("sheetName");
		int wkStartRowIndex = (Integer) sheetMap.get("wkStartRowIndex");
		List<String> wkmcInList = new ArrayList<>();
		List<String> wkmcOutList = new ArrayList<>();
		String wkmcIn = sheetMap.get("wkmcIn") != null ? sheetMap.get("wkmcIn").toString() : "";
		if (StringUtil.isNotBlank(wkmcIn)) {
			wkmcInList = Arrays.asList(wkmcIn.split(","));
		}
		String wkmcOut = sheetMap.get("wkmcOut") != null ? sheetMap.get("wkmcOut").toString() : "";
		if (StringUtil.isNotBlank(wkmcOut)) {
			wkmcOutList = Arrays.asList(wkmcOut.split(","));
		}
		List<WksjmxDto> wksjmxDtos = dealWksjmxList(wksjmxList, wkmcInList, wkmcOutList);
		if (CollectionUtils.isEmpty(wksjmxDtos)) {
			throw new BusinessException("工作表（" + sheetName + "）中没有符合条件的文库，请检查！");
		}
		//获取需要替换的sheet表
		Sheet sheet = workbook.getSheet(sheetName);
		setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）");
		//1、设置强制重新计算公式
		sheet.setForceFormulaRecalculation(true);
		//2、设置固定参数
		//V:5~11,V:13~14,V:17,V:26~27,AA:5~6,Z:7
		//U:5~11,U:13~14,U:17,U :26~27
		setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）---正在设置默认值");
		dealMapAndSetting(map, setting, isAll);
		setPoolingFactorSheet(sheet,map);
		if (isAll) {
			setDefaultValue(sheet, "V:10", "U:10", "sjzl", map, workbook);
		}
		setDefaultCell(sheet, map, workbook);
		//3、获取需要替换的sheet表的“文库名称”和“浓度Quantity”列，默认从第4行开始替换
		setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）---正在设置文库名称、浓度Quantity");
		setAandBColumnValue(wkStartRowIndex, wksjmxDtos, sheet,workbook);
	}
	
	/**
	 * 根据是否同时包含Q和T，来决定是否调整pooling系数，主要是为了省人民这里容量配平
	 * @param sheet
	 * @param map
	 */
	private void calculatorQmNGS(Workbook workbook,Sheet sheet,Map<String, Object> map) {
		//判断是否Q\T是否存在
		if (map.get("qcnt") !=null && Integer.parseInt(String.valueOf(map.get("qcnt"))) >0 && map.get("tcnt") !=null && Integer.parseInt(String.valueOf(map.get("tcnt"))) >0){
			//若存在：
			//  把Q\nc\pc样本存在Alist，重新设置pooling
			//  ( (总数据量-Tpooling系数*U17) / Qpooling系数(Q+NC+PC) )/ u17 得到比例m
			//  每行原pooling系数/q的总pooling系数 * 比例 = 新pooling系数，并设置
			List<Row> qList = new ArrayList<>();
			List<Row> tNcPcList = new ArrayList<>();
			List<Row> qNcPcList = new ArrayList<>();
			List<Row> tList = new ArrayList<>();
			BigDecimal qPoolingNum = new BigDecimal(0);
			BigDecimal tNcPcPoolingNum = new BigDecimal(0);
			BigDecimal qNcPcPoolingNum = new BigDecimal(0);
			BigDecimal tPoolingNum = new BigDecimal(0);
			int a_cellIndex = calculateXPosition("A");
			int b_cellIndex = calculateXPosition("B");
			int m_cellIndex = calculateXPosition("M");
			int n_cellIndex = calculateXPosition("N");
			//int r_cellIndex = calculateXPosition("R");
			int u_cellIndex = calculateXPosition("U");
			int z_cellIndex = calculateXPosition("Z");
			BigDecimal theMinimum = new BigDecimal(String.valueOf(map.get("TheMinimum")));
			//总数据量
			BigDecimal allsum = BigDecimal.valueOf(sheet.getRow(9).getCell(u_cellIndex).getNumericCellValue());
			//数据量/pooling 系数（M reads）
			BigDecimal u17 = BigDecimal.valueOf(sheet.getRow(16).getCell(u_cellIndex).getNumericCellValue());
			//文库参考体积
			BigDecimal u5 = BigDecimal.valueOf(sheet.getRow(4).getCell(u_cellIndex).getNumericCellValue());
			//稀释倍数
			BigDecimal u7 = BigDecimal.valueOf(sheet.getRow(6).getCell(u_cellIndex).getNumericCellValue());
			//预计每run下机数据总量(M read)
			BigDecimal u10 = BigDecimal.valueOf(sheet.getRow(9).getCell(u_cellIndex).getNumericCellValue());
			//需要的混合文库摩尔数（e-15 mol)
			BigDecimal z7 = BigDecimal.valueOf(sheet.getRow(6).getCell(z_cellIndex).getNumericCellValue());
			int startRowIndex = 3;
			while (true) {
				Row row = sheet.getRow(startRowIndex);
				if (row == null) {
					break;
				}
				Cell cell = row.getCell(a_cellIndex);
				if (cell == null) {
					break;
				}
				String cellValue = row.getCell(a_cellIndex).getStringCellValue();
				if (StringUtil.isBlank(cellValue)) {
					break;
				}
				BigDecimal m = new BigDecimal(row.getCell(m_cellIndex).getNumericCellValue()).setScale(3,RoundingMode.DOWN);
				if (cellValue.contains("-tNGS")  || cellValue.contains("-HS") || cellValue.contains("-TBtNGS")
						|| cellValue.contains("MDL004-NC-") || cellValue.contains("MDL004-PC-")
						|| cellValue.contains("MDL005-tNC-") || cellValue.contains("MDL005-tPC-")
						|| cellValue.contains("MDL004-mNC-") || cellValue.contains("MDL004-mPC-")) {
					tPoolingNum = tPoolingNum.add(m);
					tList.add(row);
					if(cellValue.contains("-NC-") || cellValue.contains("-PC-")
						|| cellValue.contains("-tNC-") || cellValue.contains("-tPC-")
						|| cellValue.contains("-mNC-") || cellValue.contains("-mPC-")) {
						tNcPcPoolingNum = tNcPcPoolingNum.add(m);
						tNcPcList.add(row);
					}
				}else {
					if(cellValue.contains("-PC-") ||cellValue.contains("-NC-")||cellValue.contains("-DC-")) {
						qNcPcPoolingNum = qNcPcPoolingNum.add(m);
						qNcPcList.add(row);
					}else {
						qPoolingNum = qPoolingNum.add(m);
						qList.add(row);
					}
				}
				startRowIndex++;
			}
			//计算QList 因为浓度低而造成的体积达到最大值的行，把相应的放到tList里，为了减少重复计算量，故放在此处
			boolean isNeedReset=false;
			//确认是否存在没有达到最大体积的标本，如果有，则把相应的系数尽量加到标本上，在标本这边无法增加的情况下才增加外参
			List<Row> re_qList = new ArrayList<>();
			if (!CollectionUtils.isEmpty(qList)){
				//总下机量 - （固定的t的pool + ncPc的pool）* 每pooling数据量 = 剩余数据量。假定标本不会超出体积最大值
				BigDecimal t_allsum = allsum.subtract(tPoolingNum.add(qNcPcPoolingNum).multiply(u17));
				// 剩余数据量 / (标本的pool * 每pooling数据量) = 标本的pooling调整系数
				BigDecimal divide = t_allsum.divide(qPoolingNum.multiply(u17), 6, RoundingMode.DOWN);
				
				qPoolingNum = new BigDecimal("0");
				for (Row cells : qList) {
					//获取经过系数调整后的pooling系数
					BigDecimal r_pooling = divide.multiply(BigDecimal.valueOf(cells.getCell(m_cellIndex).getNumericCellValue()))
							.setScale(3,RoundingMode.DOWN);
					//计算体积最大值情况下的pooling系数（**前提条件：pooling系数设置成与下机下机数据量*U17 之间一样
					BigDecimal theory_pooling = u5.multiply(BigDecimal.valueOf(cells.getCell(b_cellIndex).getNumericCellValue()))
							.multiply(u7).multiply(u10).divide(u17.multiply(theMinimum).multiply(z7),3,RoundingMode.DOWN);
					// 当理论最大系数小于调整过的pooling系数，则设置为理论系数
					if(r_pooling.compareTo(theory_pooling) > 0) {
						isNeedReset = true;

						log.error(" calculatorQmNGS 系数为理论系数。行：" + cells.getCell(a_cellIndex).getStringCellValue() + 
								" theory_pooling:" + theory_pooling + " r_pooling=" + r_pooling);
						//直接设置超过最大值的行的pooling系数为 最大值
						cells.getCell(n_cellIndex).setCellValue(theory_pooling.doubleValue());
						tPoolingNum = tPoolingNum.add(theory_pooling);
						tList.add(cells);
					}
					//如果单个标本数据量大于45M，则设置为45M，防止一个样本过于庞大 暂时废弃
					//else if
					else {
						log.error(" calculatorQmNGS 系数为实际系数。行：" + cells.getCell(a_cellIndex).getStringCellValue() + 
								" theory_pooling:" + theory_pooling + " r_pooling:" + r_pooling);
						//没有超过最大值，则直接乘以系数
						cells.getCell(n_cellIndex).setCellValue(r_pooling.doubleValue());
						qPoolingNum = qPoolingNum.add(r_pooling);
						re_qList.add(cells);
					}
				}
				// 因为存在体积的限制，故重新计算系数，尽量达到pooling系数一致。所有需要调整的pooling 都设置过n列系数
				if(isNeedReset && re_qList.size() > 0)
				{
					t_allsum = allsum.subtract(tPoolingNum.add(qNcPcPoolingNum).multiply(u17));
					divide = t_allsum.divide(qPoolingNum.multiply(u17), 6, RoundingMode.DOWN);
					for (Row cells : re_qList) {
						//获取经过系数调整后的pooling系数
						BigDecimal r_pooling = divide.multiply(BigDecimal.valueOf(cells.getCell(n_cellIndex).getNumericCellValue()))
								.setScale(3,RoundingMode.DOWN);
						log.error(" calculatorQmNGS 重新计算系数。行：" + cells.getCell(a_cellIndex).getStringCellValue() + 
								 " r_pooling:" + r_pooling);
						cells.getCell(n_cellIndex).setCellValue(r_pooling.doubleValue());
					}
				}else if(isNeedReset && qNcPcList.size() > 0) {
					//如果所有样本已经到最大体积，则只能增加NC和PC，这个时候n列是没有值的
					t_allsum = allsum.subtract(tPoolingNum.multiply(u17));
					divide = t_allsum.divide(qNcPcPoolingNum.multiply(u17), 6, RoundingMode.DOWN);
					for (Row cells : qNcPcList) {
						//获取经过系数调整后的pooling系数
						BigDecimal r_pooling = divide.multiply(BigDecimal.valueOf(cells.getCell(m_cellIndex).getNumericCellValue()))
								.setScale(3,RoundingMode.DOWN);
						//计算体积最大值情况下的pooling系数（**前提条件：pooling系数设置成与下机下机数据量*U17 之间一样
						BigDecimal theory_pooling = u5.multiply(BigDecimal.valueOf(cells.getCell(b_cellIndex).getNumericCellValue()))
								.multiply(u7).multiply(u10).divide(u17.multiply(theMinimum).multiply(z7),3,RoundingMode.DOWN);
						
						// 当理论最大系数小于调整过的pooling系数，则设置为理论系数
						if(r_pooling.compareTo(theory_pooling) > 0) {
							log.error(" calculatorQmNGS 则只能增加NC和PC 系数为理论系数。行：" + cells.getCell(a_cellIndex).getStringCellValue() + 
									" theory_pooling:" + theory_pooling + " r_pooling=" + r_pooling);
							//直接设置超过最大值的行的pooling系数为 最大值
							cells.getCell(n_cellIndex).setCellValue(theory_pooling.doubleValue());
						}
						else {
							log.error(" calculatorQmNGS 则只能增加NC和PC 系数为实际系数。行：" + cells.getCell(a_cellIndex).getStringCellValue() + 
									" theory_pooling:" + theory_pooling + " r_pooling=" + r_pooling);
							cells.getCell(n_cellIndex).setCellValue(r_pooling.doubleValue());
						}
					}
				}
				//最后如果没有NC和PC，则不进行调整
			}else if(!CollectionUtils.isEmpty(qNcPcList)) {
				//如果只有NC和PC，则只能增加NC和PC
				BigDecimal t_allsum = allsum.subtract(tPoolingNum.multiply(u17));
				BigDecimal divide = t_allsum.divide(qNcPcPoolingNum.multiply(u17), 6, RoundingMode.DOWN);
				for (Row cells : qNcPcList) {
					//获取经过系数调整后的pooling系数
					BigDecimal r_pooling = divide.multiply(BigDecimal.valueOf(cells.getCell(m_cellIndex).getNumericCellValue()))
							.setScale(3,RoundingMode.DOWN);
					//防止某个标本超过最大值，这边先考虑不超过5.5
					if(r_pooling.compareTo(MAX_DATASIZE) > 0) {
						r_pooling = new BigDecimal("5.5");
					}
					
					log.error(" calculatorQmNGS 则 qNcPcList 实际系数。行：" + cells.getCell(a_cellIndex).getStringCellValue() + 
							 " r_pooling=" + r_pooling);
					cells.getCell(n_cellIndex).setCellValue(r_pooling.doubleValue());
				}
			}
			
			if (!CollectionUtils.isEmpty(qList)||!CollectionUtils.isEmpty(qNcPcList)){
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			}
		}
	}

	/**
	 * 计算多表的第一张表的相关计算
	 *
	 * @param redisUtil
	 * @param rediskey
	 * @param workbook
	 * @param sheetMap
	 * @throws BusinessException
	 */
	private void calculatorFirstOfSheets(RedisUtil redisUtil, String rediskey, Workbook workbook, Map<String, Object> sheetMap, Map<String, Object> setting) {
		//1、初始化部分值
		int u_cellIndex = calculateXPosition("U");
		int z_cellIndex = calculateXPosition("Z");
		//2、获取工作表、获取需要替换的sheet表
		String sheetName = (String) sheetMap.get("sheetName");
		Sheet sheet = workbook.getSheet(sheetName);
		setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）");
		//3、设置强制重新计算公式
		sheet.setForceFormulaRecalculation(true);
		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		//4、产能设定相关计算
		setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）---计算 产能设定相关数据 中");
		//根据“混样系数（数值越大，混样浓度越高）”，“单文库最低加入摩尔量”相关计算
		calculatorMolAndVolume(redisUtil, rediskey, workbook, sheet, u_cellIndex, z_cellIndex,setting);
		setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在保存工作表（" + sheet.getSheetName() + "）");
	}

	/**
	 * 计算多表的第二张表的相关计算
	 *
	 * @param redisUtil
	 * @param wkid
	 * @param workbook
	 * @param sheetMap
	 * @throws BusinessException
	 */
	private void calculatorSecondOfSheets(RedisUtil redisUtil, String wkid, Workbook workbook, Map<String, Object> sheetMap, Map<String, Object> setting) {
		//1、初始化部分值
		int u_cellIndex = calculateXPosition("U");
		int v_cellIndex = calculateXPosition("V");
		int z_cellIndex = calculateXPosition("Z");
		List<String> mReads = Arrays.asList(new String[]{"150", "400", "450", "500"});
		List<String> mReadMixs = Arrays.asList(new String[]{"125", "475"});
		//2、获取工作表、获取需要替换的sheet表
		String sheetName = (String) sheetMap.get("sheetName");
		Sheet sheet = workbook.getSheet(sheetName);
		setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）");
		//为防止默认混样系数采用计算出U12，故需要设置V12的初始值为1
		sheet.getRow(11).getCell(v_cellIndex).setCellValue(1);
		//3、设置强制重新计算公式
		sheet.setForceFormulaRecalculation(true);
		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		//4、计算PC\NC的pooling系数
		calculatorPcAndNcInPRow(sheet, workbook);
		//5、产能设定相关计算
		//5.1、计算“剩余产能（M reads）”，U:18
		setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在处理工作表（" + sheet.getSheetName() + "）---正在计算 产能设定相关数据 中");
		//MRead在生成excle之前就已设定，故不需要计算
		calculatorMRead(redisUtil, wkid, workbook, sheet, u_cellIndex, mReads, mReadMixs);
		//5.2、根据“混样系数（数值越大，混样浓度越高）”，“单文库最低加入摩尔量”相关计算
		calculatorMolAndVolume(redisUtil, wkid, workbook, sheet, u_cellIndex, z_cellIndex,setting);
		setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在保存工作表（" + sheet.getSheetName() + "）");
	}

	/**
	 * 处理wksjmxList
	 *
	 * @param wksjmxList
	 * @param wkmcInList
	 * @param wkmcOutList
	 * @return
	 */
	private List<WksjmxDto> dealWksjmxList(List<WksjmxDto> wksjmxList, List<String> wkmcInList, List<String> wkmcOutList) {
		List<WksjmxDto> wksjmxs = new ArrayList<>();
		if (CollectionUtils.isEmpty(wkmcInList) && CollectionUtils.isEmpty(wkmcOutList)) {
			wksjmxs = wksjmxList;
		} else if (CollectionUtils.isEmpty(wkmcInList) && !CollectionUtils.isEmpty(wkmcOutList)) {
			for (WksjmxDto wksjmxDto : wksjmxList) {
				boolean isAdd = true;
				if (StringUtil.isNotBlank(wksjmxDto.getWkbm())) {
					for (String key : wkmcOutList) {
						if (wksjmxDto.getWkbm().contains(key)) {
							isAdd = false;
							break;
						}
					}
				}
				if (isAdd) {
					wksjmxs.add(wksjmxDto);
				}
			}
		} else {
			for (WksjmxDto wksjmxDto : wksjmxList) {
				boolean isAdd = true;
				if (StringUtil.isNotBlank(wksjmxDto.getWkbm())) {
					if (!CollectionUtils.isEmpty(wkmcInList)) {
						for (String key : wkmcInList) {
							if (!wksjmxDto.getWkbm().contains(key)) {
								isAdd = false;
							} else {
								isAdd = true;
								break;
							}
						}
					}
					if (isAdd && !CollectionUtils.isEmpty(wkmcOutList)) {
						for (String key : wkmcOutList) {
							if (wksjmxDto.getWkbm().contains(key)) {
								isAdd = false;
								break;
							}
						}
					}
				}
				if (isAdd) {
					wksjmxs.add(wksjmxDto);
				}
			}
		}
		return wksjmxs;
	}

	/**
	 * “单文库最低加入摩尔量”相关计算
	 *
	 * @param redisUtil
	 * @param rediskey
	 * @param workbook
	 * @param sheet
	 * @param u_cellIndex
	 * @param z_cellIndex
	 */
	private void calculatorMolAndVolume(RedisUtil redisUtil, String rediskey, Workbook workbook, Sheet sheet, int u_cellIndex, int z_cellIndex,Map<String,Object> map) {
		//5.2、根据“混样系数（数值越大，混样浓度越高）”，“单文库最低加入摩尔量”相关计算
		setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在计算 单文库最低加入摩尔量 中");
		BigDecimal tpoolMinValue = null;
		try {
			if (sheet.getRow(21).getCell(calculateXPosition("V")).getCellFormula().contains("INT(MAX(INT(SUM(M:M)*10/SUM(全!M:M)/10*4000*6),全!R4/SUM(全!R:R)*4000*6))")) {
				String minValue = sheet.getRow(21).getCell(calculateXPosition("V")).getStringCellValue().substring(1);
				tpoolMinValue = new BigDecimal(minValue);
			}
		} catch (Exception e) {
			log.error(sheet.getSheetName() + "---V:22 转换失败！" + e.getMessage());
		}
		boolean needDilute = true;
		//5.2.1、计算“需要的混合文库摩尔数（e-15 mol)”,Z:7
		double needMol = sheet.getRow(6).getCell(z_cellIndex).getNumericCellValue();
		//log.error("计算获取需要的混合文库摩尔数 needMol= " + needMol + " TheMinimum=" + map.get("TheMinimum"));
		BigDecimal bigDecimal = new BigDecimal(needMol).multiply(new BigDecimal(String.valueOf(map.get("TheMinimum"))));
		if (tpoolMinValue != null && tpoolMinValue.compareTo(bigDecimal) > 0) {
			bigDecimal = tpoolMinValue;
		}
		//需要稀释后处理
		needDiluteDeal(sheet, workbook, bigDecimal,new BigDecimal(String.valueOf(map.get("TheMinimum"))),true);
		//5.2.3、“混合后总摩尔量(e-15 mol)”U:22
		double afterMixMol = sheet.getRow(21).getCell(u_cellIndex).getNumericCellValue();
		setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "正在 判断是否需要稀释中");
		//“混合后总摩尔量(e-15 mol)”U:22>2.5倍的“需要的混合文库摩尔数（e-15 mol)”Z:7 可以不稀释
		//5.2.4、“混合后总摩尔浓度(pM)”U:23
		double afterMixMolConcentration = sheet.getRow(22).getCell(u_cellIndex).getNumericCellValue();
		//“混合后总摩尔浓度(pM)”U:23 > 6 够变性稀释
		//“混合后总摩尔浓度(pM)”U:23 > 20 需要稀释,20以内同时超过2.5倍需要的混合文库摩尔数可以不稀释
		if(map.get("needDilute")!=null)
		{
			needDilute = Boolean.valueOf((String)map.get("needDilute"));
		}else{
			if (afterMixMolConcentration <= 10 || (afterMixMolConcentration <= 20 && afterMixMol > 2.5 * needMol)) {
				needDilute = false;
			}
		}
		//需要稀释
		if (needDilute) {
			setPoolingRedisStatus(redisUtil, rediskey, "isContinue", "需要稀释，正在计算“混合后总体积(ul)”");
			double u26 = sheet.getRow(25).getCell(u_cellIndex).getNumericCellValue();//混合文库稀释体积U:27
			double u27 = sheet.getRow(26).getCell(u_cellIndex).getNumericCellValue();//混合文库稀释浓度U:28
			BigDecimal multiplyValue = new BigDecimal(u26).multiply(new BigDecimal(u27));//混合文库稀释摩尔量
			if (tpoolMinValue != null && tpoolMinValue.compareTo(multiplyValue) > 0) {
				multiplyValue = tpoolMinValue;
			}
			needDiluteDeal(sheet, workbook, multiplyValue,new BigDecimal(String.valueOf(map.get("TheMinimum"))),false);
		} else {
			//设置V:27为空值
			sheet.getRow(26).getCell(calculateXPosition("V")).setCellValue("");
		}
	}

	/**
	 * 计算“剩余产能（M reads）”，U:18
	 *
	 * @param redisUtil
	 * @param wkid
	 * @param workbook
	 * @param sheet
	 * @param u_cellIndex
	 * @param mReads
	 * @param mReadMixs
	 */
	private void calculatorMRead(RedisUtil redisUtil, String wkid, Workbook workbook, Sheet sheet, int u_cellIndex, List<String> mReads, List<String> mReadMixs) {
		//5.1、计算“剩余产能（M reads）”，U:18
		double doubleSpareCapacity = sheet.getRow(17).getCell(u_cellIndex).getNumericCellValue();
		//获取“数据量/pooling 系数（M reads）”U:17的值
		String mRead = new BigDecimal(sheet.getRow(9).getCell(u_cellIndex).getNumericCellValue()).stripTrailingZeros().toPlainString();
		while (doubleSpareCapacity <= 0) {
			setPoolingRedisStatus(redisUtil, wkid, "isContinue", "正在计算 数据量/pooling 系数（M reads） ...");
			//获取“预计每run下机数据总量(M read)”，U:10
			if (mReads.contains(mRead) && mReads.indexOf(mRead) < mReads.size() - 1) {
				mRead = mReads.get(mReads.indexOf(mRead) + 1);
			} else if (mReadMixs.contains(mRead) && mReadMixs.indexOf(mRead) < mReadMixs.size() - 1) {
				mRead = mReads.get(mReadMixs.indexOf(mRead) + 1);
			} else {
				log.error("pooling表导出-----产能设定错误，预计每run下机数据总量(M read) 处理存在问题");
				break;
			}
			try {
				sheet.getRow(9).getCell(u_cellIndex).setCellValue(new BigDecimal(mRead).doubleValue());
			} catch (Exception e) {
				sheet.getRow(9).getCell(u_cellIndex).setCellValue(mRead);
			}
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			doubleSpareCapacity = sheet.getRow(17).getCell(u_cellIndex).getNumericCellValue();//剩余产能
		}
	}

	/**
	 * 计算NC、PC的POOLING系数
	 *
	 * @param sheet
	 * @param workbook
	 */
	private void calculatorPcAndNcInPRow(Sheet sheet, Workbook workbook) {
		int a_cellIndex = calculateXPosition("A");
		int m_cellIndex = calculateXPosition("M");
		int n_cellIndex = calculateXPosition("N");
		int u_cellIndex = calculateXPosition("U");
		List<Row> NPList = new ArrayList<>();//NC、PCList
		List<Row> elseList = new ArrayList<>();//非NC、PCList
		BigDecimal elsePoolingValue = new BigDecimal(0);//除NC\PC\tpool
		int startRowIndex = 3;
		//计算NC、PC中预计下机reads之和
		while (true) {
			Row row = sheet.getRow(startRowIndex);
			if (row == null) {
				break;
			}
			Cell cell = row.getCell(a_cellIndex);
			if (cell == null) {
				break;
			}
			String cellValue = row.getCell(a_cellIndex).getStringCellValue();
			if (StringUtil.isNotBlank(cellValue)) {
				if ((cellValue.contains("-NC-") || cellValue.contains("-PC-")|| cellValue.contains("-mNC-")|| cellValue.contains("-mPC-")
						|| cellValue.contains("-tNC-")|| cellValue.contains("-PC-")) && cellValue.indexOf("-TBtNGS-") == -1) {
					NPList.add(row);
				} else if (!"tpool".equals(row.getCell(0).getStringCellValue())) {
					elseList.add(row);
					elsePoolingValue = elsePoolingValue.add(new BigDecimal(row.getCell(m_cellIndex).getNumericCellValue()));
				}
			} else {
				break;
			}
			startRowIndex++;
		}
		//获取 预计每run下机数据总量(M read) U:10
		BigDecimal u10 = new BigDecimal(sheet.getRow(9).getCell(u_cellIndex).getNumericCellValue());
		//获取 数据量/pooling 系数（M reads） U:17
		BigDecimal u17 = new BigDecimal(sheet.getRow(16).getCell(u_cellIndex).getNumericCellValue());
		//获取 tpool的pooling系数 M:4
		BigDecimal m4 = new BigDecimal(sheet.getRow(3).getCell(m_cellIndex).getNumericCellValue());
		BigDecimal tpoolDataValue = m4.multiply(u17);
		if (elseList.size() == 1 && "tpool".equals(elseList.get(0).getCell(0).getStringCellValue())) {
			//value = 剩余数据量/NC\PC个数/U17
			BigDecimal value = u10.subtract(tpoolDataValue).divide(new BigDecimal(NPList.size())).divide(u17, 2, RoundingMode.DOWN);
			for (Row row : NPList) {
				row.getCell(n_cellIndex).setCellValue(value.doubleValue());
			}
		} else {
			//根据公式1调整样本的数据量和系数，nc,pc系数再用公式2调整
			//获取 NC\PC总数据量（NC\PC个数*5M）
			BigDecimal npDataValue = new BigDecimal(NPList.size() * 5);
			BigDecimal factor = u10.subtract(tpoolDataValue).subtract(npDataValue).divide(elsePoolingValue.multiply(u17), 2, RoundingMode.DOWN);
			for (Row row : elseList) {
				//公式1
				row.getCell(n_cellIndex).setCellValue(factor.multiply(new BigDecimal(row.getCell(m_cellIndex).getNumericCellValue())).doubleValue());
			}
			//公式2
			for (Row row : NPList) {
				row.getCell(n_cellIndex).setCellValue(new BigDecimal(5).divide(u17, 2, RoundingMode.DOWN).doubleValue());
			}
		}
		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	}

	/**
	 * 需要稀释后处理
	 * 该方法是为了调整“单文库最低加入摩尔量”V:12 使得 “混合后总体积(ul)”U:21 > “混合文库稀释用体积(ul)”U:28 * TheMinimum（最小上机次数）
	 * 化简后可得条件为：U:26 * U:27 * TheMinimum < U:22
	 * 其中U:26、U:27均为设定值，不可变，故需观测U:22即可
	 * 而U:22为=SUMPRODUCT(Q:Q,J:J)+SUMPRODUCT(P:P,G:G)
	 * 	U22= U12* sum(M) + specialCList 最大体积
	 * @param sheet
	 * @param workbook
	 * @param totalMol 混合文库稀释摩尔量
	 * @param isEvaluate 是否计算execl全局
	 */
	private void needDiluteDeal(Sheet sheet, Workbook workbook, BigDecimal totalMol,BigDecimal b_TheMinimum,boolean isEvaluate) {
		int a_cellIndex = calculateXPosition("A");//文库名称
		int b_cellIndex = calculateXPosition("B");//文库名称
		int f_cellIndex = calculateXPosition("F");//原液体积
		int g_cellIndex = calculateXPosition("G");//原液浓度
		int i_cellIndex = calculateXPosition("I");//稀释液体积
		int j_cellIndex = calculateXPosition("J");//稀释液浓度
		int m_cellIndex = calculateXPosition("M");//pooling系数
		int n_cellIndex = calculateXPosition("N");//pooling系数
		int u_cellIndex = calculateXPosition("U");
		int v_cellIndex = calculateXPosition("V");
		double minMol = sheet.getRow(11).getCell(u_cellIndex).getNumericCellValue();//单文库最低加入摩尔量U:12
		log.error("“单文库最低加入摩尔量”V:12 totalMol= " + totalMol + " 初始minMol=" + minMol);
		//double u5 = sheet.getRow(4).getCell(u_cellIndex).getNumericCellValue();//文库参考体积U:5
		double u7 = sheet.getRow(6).getCell(u_cellIndex).getNumericCellValue();//稀释倍数U:7

		BigDecimal b_u7 = BigDecimal.valueOf(sheet.getRow(6).getCell(u_cellIndex).getNumericCellValue());
		BigDecimal b_v10 = BigDecimal.valueOf(sheet.getRow(9).getCell(v_cellIndex).getNumericCellValue());
		BigDecimal b_u17 = BigDecimal.valueOf(sheet.getRow(16).getCell(u_cellIndex).getNumericCellValue());
		BigDecimal b_u26 = BigDecimal.valueOf(sheet.getRow(25).getCell(u_cellIndex).getNumericCellValue());
		BigDecimal b_u27 = BigDecimal.valueOf(sheet.getRow(26).getCell(u_cellIndex).getNumericCellValue());
		
		BigDecimal Sum_pooling = new BigDecimal(0);//pooling系数之和
		//获取所有可能用到的值，存入list（避免用到的时候多次从excle中获取）
		int startRowIndex = 3;
		//=====为特殊情况做准备，初始化
		//采用稀释液的Alist
		List<Map<String, Object>> specialAList = new ArrayList<>();
		//采用原液的Blist
		List<Map<String, Object>> specialBList = new ArrayList<>();
		// specialCList 超过最大值List  主要计算 混合后总摩尔浓度(pM) 不能小于9
		List<Map<String, Object>> specialCList = new ArrayList<>();
		//为防止重复循环，专门设置一个整体
		List<Map<String, Object>> AllList = new ArrayList<>();
		// U23=U22/U21 => U23= (U12*M/G *G + U12*M/J *J)/ SUM(P+Q)  => U23= (SUM（取AList + BList 的 Pooling系数） + CList)/(A+B +C)
		//SUM（取AList + BList 的 Pooling系数） + CList 的 F*U7*SUM( B) / 10000（10000为U12的近似值）  --》 X 相当于U22。
		BigDecimal specialX = new BigDecimal(0);
		//AList的行采用 pooling系数/B列(浓度) + BList 的行 pooling系数/(B列(浓度) * U7) + CList 个数*F（采用近似值） 的 , 进行加和  --> Y 相当于U21
		BigDecimal specialY = new BigDecimal(0);
		//确认是否存在-DBL-文库，如果存在，则不降低浓度
		boolean isExistDBL = false;
		
		while (true) {
			Map<String, Object> rowMap = new HashMap<>();
			Row row = sheet.getRow(startRowIndex);
			if (row == null) {
				break;
			}
			Cell cell = row.getCell(a_cellIndex);
			if (cell == null) {
				break;
			}
			String cellValue = row.getCell(a_cellIndex).getStringCellValue();
			if (StringUtil.isNotBlank(cellValue)) {
				if (cellValue.contains("-DBL-"))
					isExistDBL = true;
				double bNum = row.getCell(b_cellIndex).getNumericCellValue();
				rowMap.put("rowIndex", startRowIndex);
				rowMap.put("a", cellValue);
				rowMap.put("b", bNum);
				double fNum = row.getCell(f_cellIndex).getNumericCellValue();
				rowMap.put("f", fNum);
				rowMap.put("g", row.getCell(g_cellIndex).getNumericCellValue());
				rowMap.put("i", row.getCell(i_cellIndex).getNumericCellValue());
				double jNum = row.getCell(j_cellIndex).getNumericCellValue();
				rowMap.put("j", jNum);
				//SUM（取AList + BList 的 Pooling系数） + CList 的 F*U7*SUM( B) / 10000（10000为U12的近似值）  --》 X。
				double poolingNum = row.getCell(m_cellIndex).getNumericCellValue();
				BigDecimal b_poolingNum = new BigDecimal(poolingNum);
				rowMap.put("m", poolingNum);
				Sum_pooling = Sum_pooling.add(b_poolingNum).setScale(6,RoundingMode.HALF_UP);
				//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
				BigDecimal t_mDivideJ = b_poolingNum.divide(new BigDecimal(jNum),6,RoundingMode.HALF_UP);
				rowMap.put("mDivideJ",t_mDivideJ);
				
				BigDecimal t_i = new BigDecimal((Double)rowMap.get("i"));
				BigDecimal t_mDivideG = t_mDivideJ.divide(new BigDecimal(u7),6,RoundingMode.HALF_UP);
				if(t_mDivideG.multiply(new BigDecimal(minMol)).compareTo(new BigDecimal(fNum)) >= 0) {
					specialCList.add(rowMap);
					//AList的行采用 pooling系数/B列(浓度) + BList 的行 pooling系数/(B列(浓度) * U7) + CList 个数*F（采用近似值） 的 , 进行加和  --> Y
					specialY = specialY.add(new BigDecimal(fNum).divide(new BigDecimal(10000), 6, RoundingMode.HALF_UP));
					specialX = specialX.add(new BigDecimal(fNum).multiply(b_u7).multiply(new BigDecimal(jNum)).divide(new BigDecimal("10000"),6, RoundingMode.HALF_UP));//CList 的 F*U7*SUM( B) / 10000（10000为U12的近似值）
				}else if(t_mDivideJ.multiply(new BigDecimal(minMol)).compareTo(t_i) >= 0) {
					specialBList.add(rowMap);
					//AList的行采用 pooling系数/B列(浓度) + BList 的行 pooling系数/(B列(浓度) * U7) + CList 个数*F（采用近似值） 的 , 进行加和  --> Y
					specialY = specialY.add(t_mDivideJ.divide(new BigDecimal(u7),6,RoundingMode.HALF_UP));
					specialX = specialX.add(b_poolingNum);//SUMPooling系数(AList +BList)
				}else {
					specialAList.add(rowMap);
					//=====为特殊情况做准备，计算初始Y值，因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					//AList的行采用 pooling系数/B列(浓度) + BList 的行 pooling系数/(B列(浓度) * U7) + CList 个数*F（采用近似值） 的 , 进行加和  --> Y
					specialY = specialY.add(t_mDivideJ);
					specialX = specialX.add(b_poolingNum);//SUMPooling系数(AList +BList)
				}
				AllList.add(rowMap);
			} else {
				break;
			}
			startRowIndex++;
		}
		//为特殊情况做准备 
		//对specialAList进行排序，先把所有的标本计算每一行的  M / J  , 进行排序 从小到大, 放在Alist 下
		specialAList = specialAList.stream().sorted(Comparator.comparingDouble(m -> ((BigDecimal) m.get("mDivideJ")).doubleValue())).collect(Collectors.toList());
		//对specialBList进行排序，先把所有的标本计算每一行的  M / J  , 进行排序 从大到小, 放在Blist 下
		specialBList = specialBList.stream().sorted(Comparator.comparingDouble((Map<String, Object> m)->((BigDecimal) m.get("mDivideJ")).doubleValue()).reversed()).collect(Collectors.toList());
		// U22 >= totalMol(也就是Z7 * 最小上机次数 或者 U26*U27) => QJ+PG >= totalMol  => U12*SUM(M) >= totalMol
		//不考虑超出体积的情况下，根据化简后公式获取基础值：单文库最低加入摩尔量 U12 = 混合文库稀释摩尔量totalMol(Z7*2.1倍 或者 U26* U27)/pooling系数之和
		double newMinMol = totalMol.divide(Sum_pooling, 6, RoundingMode.HALF_UP).doubleValue();
		//如果计算出来的基础值大，则采用基础值，然后再确认在这个情况下体积有没有达到最大值，达到最大值时证明估计偏高，需要重新计算
		if (newMinMol > minMol) {
			BigDecimal t_minMol = new BigDecimal(newMinMol);	
			t_minMol = t_minMol.divide(new BigDecimal(20), 0, RoundingMode.UP).multiply(new BigDecimal(20));
			minMol = t_minMol.doubleValue();
			log.error("pooling系数之和=" + Sum_pooling + " minMol=" + minMol);
		}else {
			log.error("计算获取的最低摩尔量比较小 pooling系数之和=" + Sum_pooling + " 原始minMol=" + minMol + " newMinMol=" + newMinMol);
		}
		// specialAList 稀释List
		// specialBList 原液List
		// 根据计算出来的U12(minMol),重新确认specialAList哪些因为IF(U$12*M28/J28>=I28，则放入原液 specialBList
		for (int index = specialAList.size() - 1; index >= 0; index--) {
			Map<String, Object> rowMap = specialAList.get(index);
			BigDecimal t_mDivideJ = (BigDecimal)rowMap.get("mDivideJ");
			BigDecimal t_i = new BigDecimal((Double)rowMap.get("i"));
			if(t_mDivideJ.multiply(new BigDecimal(minMol)).compareTo(t_i) >= 0) {
				specialAList.remove(index);
				specialBList.add(rowMap);
				//AList的行采用 pooling系数/B列(浓度) + BList 的行 pooling系数/(B列(浓度) * U7) + CList 个数*F（采用近似值） 的 , 进行加和  --> Y
				specialY = specialY.subtract(t_mDivideJ).add(t_mDivideJ.divide(new BigDecimal(u7),6,RoundingMode.HALF_UP));
			}
		}
		BigDecimal t_bigtotal = b_u27.multiply(b_u26).multiply(b_TheMinimum);
		while (true){
			log.error(" 确认循环次数：" + minMol + " Sum_pooling:" + Sum_pooling);
			//备份总pooling系数，减去Clist得到A+B的pooling系数
			BigDecimal t_ABsumpooling = Sum_pooling.setScale(6,RoundingMode.HALF_UP);
			//CList的F*G
			BigDecimal t_cListSUM = BigDecimal.ZERO;
			BigDecimal t_diff_cListSUM = BigDecimal.ZERO;
			// 先根据最新的U12调整当前 specialCList 的pooling系数
			if(specialCList !=null && specialCList.size() > 0) {
				for (int index = specialCList.size() - 1; index >= 0; index--) {
					Map<String, Object> rowMap = specialCList.get(index);
					BigDecimal fNum = new BigDecimal(String.valueOf(rowMap.get("f")));//原液最大体积
					BigDecimal gNum = new BigDecimal(String.valueOf(rowMap.get("g")));//原液浓度
					BigDecimal mNum = new BigDecimal(String.valueOf(rowMap.get("m")));//pooling系数
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					//理论pooling最大值
					BigDecimal t_pooling = fNum.multiply(gNum).divide(new BigDecimal(minMol),3, RoundingMode.HALF_DOWN);
					t_ABsumpooling = t_ABsumpooling.subtract(mNum);
					rowMap.put("m", t_pooling.doubleValue());
					//根据理论pooling最大值调整总pooling系数
					Sum_pooling = Sum_pooling.subtract(mNum).add(t_pooling).setScale(6,RoundingMode.HALF_UP);
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pooling.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);
					t_cListSUM = t_cListSUM.add(fNum.multiply(gNum));
					
					log.error(" 根据U12 调整行：" + rowMap.get("a") + " t_pooling=" + t_pooling);
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pooling.doubleValue());
					//获取CList 这边产生的pooling系数差额
					t_diff_cListSUM.add(mNum.subtract(t_pooling));
				}
			}
			//相应pooling系数优先加算到MDL001的PC和NC上，然后再加算到MDL004的PC和NC上。
			Map<String,Object> result_NcPcMap = AdjustNcPcPooling(sheet,b_TheMinimum,specialAList,specialBList,t_diff_cListSUM,Sum_pooling);
			BigDecimal t_sumpoolBigDecimal = (BigDecimal)result_NcPcMap.get("sum_pooling");
			if(t_sumpoolBigDecimal != null && t_sumpoolBigDecimal.compareTo(BigDecimal.ZERO) > 0)
				Sum_pooling = t_sumpoolBigDecimal;
			//根据化简后公式获取基础值：U12 (单文库最低加入摩尔量) = 混合文库稀释摩尔量/pooling系数之和
			double t_newMinMol = totalMol.divide(Sum_pooling, 6, RoundingMode.HALF_UP).doubleValue();
			//如果计算出来的基础值大，则采用基础值，然后再确认在这个情况下体积有没有达到最大值，达到最大值时证明估计偏高，需要重新计算
			if (t_newMinMol > minMol) {
				BigDecimal t_minMol = new BigDecimal(t_newMinMol);	
				t_minMol = t_minMol.divide(new BigDecimal(20), 0, RoundingMode.UP).multiply(new BigDecimal(20));
				minMol = t_minMol.doubleValue();
				log.error(" 调整后pooling系数之和=" + Sum_pooling + " minMol=" + minMol);
			}
			// U21 >= U28* b_TheMinimum 最小上机次数 => U21 >= U27*U26/U23 *b_TheMinimum =>  U21 >= U21*U27*U26/U22 * b_TheMinimum
			// U22 >= U27*U26* b_TheMinimum => U12*SUM(AList的M + Blist的M) + CList的F*G >= U27*U26* b_TheMinimum 
			// => U12>= (U27*U26* b_TheMinimum - CList的F*G)/SUM(AList的M + Blist的M)   混合后总体积 >= 混合文库稀释用体积 * b_TheMinimum
			t_newMinMol = t_bigtotal.subtract(t_cListSUM).divide(t_ABsumpooling,6,RoundingMode.HALF_UP).doubleValue();
			//如果所需体积计算出来的U12值大，则采用这个体积计算出来的值
			if (t_newMinMol > minMol) {
				BigDecimal t_minMol = new BigDecimal(t_newMinMol);	
				t_minMol = t_minMol.divide(new BigDecimal(20), 0, RoundingMode.UP).multiply(new BigDecimal(20));
				minMol = t_minMol.doubleValue();
				log.error(" 根据所需混合体积计算结果 t_newMinMol：" +t_newMinMol + " minMol：" +minMol);
			}
			
			//SUM（取AList + BList 的 Pooling系数） + CList 的 F*U7*SUM( B) / 10000（10000为U12的近似值）  --》 X  类似U22
			//AList的行采用   pooling系数/B列(浓度) + BList 的行 pooling系数/(B列(浓度) * U7) + CList 个数*F（采用近似值） 的 , 进行加和  --> Y 类似U21。
			//计算  X / Y (也即是U23的值)，如果大于9。则退出 flg=false
			//若X/Y小于9
			if (specialX.divide(specialY,3,RoundingMode.UP).compareTo(new BigDecimal(9)) <0 ){
				if (CollectionUtils.isEmpty(specialAList)){
					break;
				}
				//从最大把相应的标本放到  BList（Alist 进行Remove）
				Map<String, Object> special = specialAList.get(specialAList.size() - 1);
				specialBList.add(special);
				specialAList.remove(special);
				BigDecimal mDivideJ = (BigDecimal) special.get("mDivideJ");
				//Y = Y - 移除的A mDivideJ + 增加的B (mDivideJ / U7)
				specialY = specialY.subtract(mDivideJ).add(mDivideJ.divide(new BigDecimal(u7),6,RoundingMode.HALF_UP));
			} else {
				//达到9后，U12 = 最后一次的BList的行，计算  U$12 =I * J /M，取50的整数
				if (!CollectionUtils.isEmpty(specialBList)){
					Map<String, Object> lastSpecial = specialBList.get(specialBList.size() - 1);
					BigDecimal specialI = new BigDecimal(lastSpecial.get("i").toString());
					BigDecimal specialJ = new BigDecimal(lastSpecial.get("j").toString());
					BigDecimal specialM = new BigDecimal(lastSpecial.get("m").toString());
					BigDecimal end = specialI.multiply(specialJ).divide(specialM, 0, RoundingMode.UP);
					log.error("“单文库最低加入摩尔量”mDivideJ = " + end + " specialI=" + specialI + " specialJ=" + specialJ + " specialM=" + specialM);
					if (end.compareTo(new BigDecimal(minMol))>0){
						end = end.divide(new BigDecimal(20), 0, RoundingMode.UP).multiply(new BigDecimal(20));
						minMol = end.doubleValue();
					}
				}else {
					break;
				}
				boolean isAddToCList = false;
				for (int index = specialBList.size() - 1; index >= 0; index--) {
					Map<String, Object> rowMap = specialBList.get(index);
					BigDecimal f = new BigDecimal(String.valueOf(rowMap.get("f")));
					BigDecimal i = new BigDecimal(String.valueOf(rowMap.get("i")));//稀释液体积
					BigDecimal j = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					BigDecimal m = new BigDecimal(String.valueOf(rowMap.get("m")));//pooling系数
					//判断是否加原液:U$12*M/J>=I => U$12 >=I*J/M
					BigDecimal judgeNum = i.multiply(j).divide(m, 2, RoundingMode.HALF_UP);//稀释液摩尔量/pooling系数
					boolean isOriginalSolution = new BigDecimal(String.valueOf(minMol)).compareTo(judgeNum) >= 0;
					if (isOriginalSolution) {
						BigDecimal b = new BigDecimal(String.valueOf(rowMap.get("b")));// 浓度
						BigDecimal g = new BigDecimal(String.valueOf(rowMap.get("g")));//原液浓度
						BigDecimal mDivideJ = new BigDecimal(String.valueOf(rowMap.get("mDivideJ")));//原液浓度
						//判断是否超过原液上限：U$12*M/G>F
						BigDecimal originalSolution = new BigDecimal(String.valueOf(minMol)).multiply(m).divide(g, 6, RoundingMode.HALF_UP);
						boolean isOver = originalSolution.compareTo(f) > 0;
						if (isOver) {
							isAddToCList = true;
							specialBList.remove(rowMap);
							specialCList.add(rowMap);
							//X = X - 移除的B的pooling系数 + 增加的C的F*U7*B/U12**2  10000为U12的近似值
							specialX = specialX.subtract(m).add(f.multiply(new BigDecimal(u7)).multiply(b).divide(new BigDecimal(100000000), 6, RoundingMode.HALF_UP));
							//Y = Y - 移除的B的(mDivideJ / U7) + 增加的C的F
							specialY = specialY.subtract(mDivideJ.divide(new BigDecimal(u7),6,RoundingMode.HALF_UP)).add(f.divide(new BigDecimal(10000), 6, RoundingMode.HALF_UP));
						}
					}
				}
				//如果没有加到CList，证明当前数值已够
				if (!isAddToCList){
					break;
				}
			}
		}
		
		//设置出来的U12（单文库最低加入摩尔量）
		sheet.getRow(11).getCell(v_cellIndex).setCellValue(minMol);
		//最后再对全体进行循环，防止设置pooling系数为5.5，但整体系数未达标，造成标本数据量太高，减少浓度降低一半，数据量也一半
		BigDecimal total_po = b_v10.divide(b_u17, 3, RoundingMode.HALF_UP);
		BigDecimal diff_po = Sum_pooling.divide(total_po, 3, RoundingMode.HALF_UP);
		//当整体的pooling只有一半时，减少浓度降低一半，数据量也一半
		if(diff_po.compareTo(new BigDecimal("0.4")) < 0 && !isExistDBL) {
			log.error(" needDiluteDeal 的比例低于0.4 ： " + diff_po + " 总:"+ total_po);
			Cell aa6_cell = sheet.getRow(5).getCell(calculateXPosition("AA"));
			Cell v10_cell = sheet.getRow(9).getCell(calculateXPosition("V"));
			BigDecimal b_aa6 = BigDecimal.ZERO;
			BigDecimal t_b_v10 = BigDecimal.ZERO;
			switch(aa6_cell.getCellType()){
			case STRING:
				String valueString = aa6_cell.getStringCellValue();
				b_aa6 = new BigDecimal(valueString);
				break;
			case NUMERIC:
				double valueNumber = aa6_cell.getNumericCellValue();
				b_aa6 = new BigDecimal(valueNumber);
				break;
			default:
				break;
			}
			switch(v10_cell.getCellType()){
			case STRING:
				String valueString = v10_cell.getStringCellValue();
				t_b_v10 = new BigDecimal(valueString);
				break;
			case NUMERIC:
				double valueNumber = v10_cell.getNumericCellValue();
				t_b_v10 = new BigDecimal(valueNumber);
				break;
			default:
				break;
			}
			for (int index = 0; index < AllList.size(); index++) {
				Map<String, Object> rowMap = AllList.get(index);
				BigDecimal m = new BigDecimal(String.valueOf(rowMap.get("m")));
				String libName = (String)rowMap.get("a");
				// (row - x)/(sum -x) * Total <= 4.5 => x >= (row*Total - 4.5*sum)/(Total -4.5) => row-x = row - (row*Total - 4.5*sum)/(Total -4.5)
				// 如果存在多个，那么第二个开始还是会有问题，但只是考虑近似值，就不用那么精确了
				BigDecimal maxSize = MAX_DATASIZE;
				if (libName.contains("-DBL-"))
					maxSize = MAX_DBLSIZE;
				BigDecimal max_m = maxSize.multiply(diff_po);
				if(m.compareTo(max_m) > 0) {
					BigDecimal t_m = m.subtract(m.multiply(total_po).subtract(maxSize.multiply(Sum_pooling)).divide(total_po.subtract(maxSize), 3, RoundingMode.HALF_UP));
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_m.doubleValue());
					rowMap.put("m", t_m);
					log.error(" needDiluteDeal 的0.5 超过最大" + maxSize+ "M重新设置。" + rowMap.get("a") + " 设置pooling系数:"+ t_m);
				}
			}
			if(b_aa6.compareTo(new BigDecimal("2")) >= 0) {
				aa6_cell.setCellValue(b_aa6.divide(new BigDecimal("2"),2,RoundingMode.HALF_UP).doubleValue());
				v10_cell.setCellValue(t_b_v10.divide(new BigDecimal("2"),2,RoundingMode.HALF_UP).doubleValue());
				total_po = total_po.divide(new BigDecimal("2"),2,RoundingMode.HALF_UP);
			}
		}else if(diff_po.compareTo(new BigDecimal("0.8"))<=0) {
			log.error(" needDiluteDeal 的比例低于0.8 ： " + diff_po + " 总:"+ total_po);
			for (int index = 0; index < AllList.size(); index++) {
				Map<String, Object> rowMap = AllList.get(index);
				BigDecimal m = new BigDecimal(String.valueOf(rowMap.get("m")));
				String libName = (String)rowMap.get("a");
				// (row - x)/(sum -x) * Total <= 4.5 => x >= (row*Total - 4.5*sum)/(Total -4.5) => row-x = row - (row*Total - 4.5*sum)/(Total -4.5)
				// 如果存在多个，那么第二个开始还是会有问题，但只是考虑近似值，就不用那么精确了
				BigDecimal maxSize = MAX_DATASIZE;
				if (libName.contains("-DBL-"))
					maxSize = MAX_DBLSIZE;
				BigDecimal max_m = maxSize.multiply(diff_po);
				if(m.compareTo(max_m) > 0) {
					BigDecimal t_m = m.subtract(m.multiply(total_po).subtract(maxSize.multiply(Sum_pooling)).divide(total_po.subtract(maxSize), 3, RoundingMode.HALF_UP));
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_m.doubleValue());
					rowMap.put("m", t_m);
					log.error(" needDiluteDeal 的 0.8 超过最大" + maxSize+ "M重新设置。" + rowMap.get("a") + " 设置pooling系数:"+ t_m);
				}
			}
		}

		if(isEvaluate)
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		
		log.error(" needDiluteDeal 的evaluateAll 的执行完成时间。" + minMol);
	}
	
	/**
	 * 相应pooling系数优先加算到MDL001的PC和NC上，然后再加算到MDL004的PC和NC上。
	 * @param sheet
	 * @param theMinimum 文库最小倍数
	 * @param specialAList
	 * @param specialBList
	 * @param diff_cListSUM
	 * @param Sum_pooling 当前总pooling系数
	 * @return
	 */
	private Map<String, Object> AdjustNcPcPooling(Sheet sheet,BigDecimal theMinimum,List<Map<String, Object>> specialAList,List<Map<String, Object>> specialBList,
			BigDecimal diff_cListSUM,BigDecimal Sum_pooling) {
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("sum_pooling", Sum_pooling);
		
		log.error("准备调整NC和PC的pooling系数 当前总pooling系数 = " + Sum_pooling);
		//MDL001的NC和PC列表
		List<Map<String, Object>> MDL1NcPcList = new ArrayList<Map<String, Object>>();
		//MDL004的NC和PC列表
		List<Map<String, Object>> MDL4NcPcList = new ArrayList<Map<String, Object>>();
		//除去MDL001 的NC和PC的样本，需要重新调整pooling系数
		List<Map<String, Object>> MDL1OtherList = new ArrayList<Map<String, Object>>();
		int u_cellIndex = calculateXPosition("U");
		int z_cellIndex = calculateXPosition("Z");
		int n_cellIndex = calculateXPosition("N");
		//数据量/pooling 系数（M reads）
		BigDecimal u17 = BigDecimal.valueOf(sheet.getRow(16).getCell(u_cellIndex).getNumericCellValue());
		//文库参考体积
		BigDecimal u5 = BigDecimal.valueOf(sheet.getRow(4).getCell(u_cellIndex).getNumericCellValue());
		//稀释倍数
		BigDecimal u7 = BigDecimal.valueOf(sheet.getRow(6).getCell(u_cellIndex).getNumericCellValue());
		//预计每run下机数据总量(M read)
		BigDecimal u10 = BigDecimal.valueOf(sheet.getRow(9).getCell(u_cellIndex).getNumericCellValue());
		//需要的混合文库摩尔数（e-15 mol)
		BigDecimal z7 = BigDecimal.valueOf(sheet.getRow(6).getCell(z_cellIndex).getNumericCellValue());
		
		//根据当前总pooling 与 预计总pooling 的相差
		BigDecimal diff_pooling = u10.divide(u17,3,RoundingMode.DOWN).subtract(Sum_pooling);
		if(diff_pooling.compareTo(new BigDecimal("2")) < 0) {
			log.error("未达到pooling系数的调整阈值，退出。 ");
			return resultMap;
		}
		
		//先筛选出MDL001 和 MDL004 的PC和NC的标本
		for (int index = specialAList.size() - 1; index >= 0; index--) {
			Map<String, Object> rowMap = specialAList.get(index);
			checkLib(MDL1NcPcList,MDL4NcPcList,MDL1OtherList,rowMap);
		}
		for (int index = specialBList.size() - 1; index >= 0; index--) {
			Map<String, Object> rowMap = specialBList.get(index);
			checkLib(MDL1NcPcList,MDL4NcPcList,MDL1OtherList,rowMap);
		}
		//第一步先把相应的pooling系数加到MDL001的标本上
		if(MDL1OtherList!=null && MDL1OtherList.size() > 0) {
			//计算每一个NC和PC的调整系数.
			BigDecimal b_coeff = diff_pooling.divide(BigDecimal.valueOf(MDL1OtherList.size()),3,RoundingMode.DOWN);
			
			for (int index = MDL1OtherList.size() - 1; index >= 0; index--) {
				Map<String, Object> rowMap = MDL1OtherList.get(index);
				//获取经过系数调整后的pooling系数
				BigDecimal m_pooling = BigDecimal.valueOf((Double)rowMap.get("m"));
				String libName = (String)rowMap.get("a");
				BigDecimal maxSize = MAX_DATASIZE;
				if (libName.contains("-DBL-"))
					maxSize = MAX_DBLSIZE;
				
				BigDecimal r_pooling = b_coeff.add(m_pooling).setScale(3,RoundingMode.DOWN);
				//计算体积最大值情况下的pooling系数（**前提条件：pooling系数设置成与下机下机数据量*U17 之间一样
				BigDecimal theory_pooling = u5.multiply(BigDecimal.valueOf((Double)rowMap.get("b")))
						.multiply(u7).multiply(u10).divide(u17.multiply(theMinimum).multiply(z7),3,RoundingMode.DOWN);
				BigDecimal t_pool	 = BigDecimal.ZERO;
				// 当理论最大系数小于调整过的pooling系数，则设置为理论系数
				if(r_pooling.compareTo(theory_pooling) > 0) {
					t_pool = theory_pooling;
					//为防止单个标本数据量过大，对最大值进行限制，当前限制4.5pooling系数，相当于45M
					if(theory_pooling.compareTo(maxSize) > 0) {
						t_pool = maxSize.add(BigDecimal.ZERO);
					}
					//直接设置超过最大值的行的pooling系数为 最大值
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pool.doubleValue());
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					rowMap.put("m", t_pool.doubleValue());
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pool.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);
					log.error(" AdjustNcPcPooling 设置Q2.0的pooling系数为理论系数。行数：" + rowMap.get("a") + 
							" pooling：" + m_pooling + " =>" + t_pool + " theory_pooling:" + theory_pooling + " 差额:" + diff_pooling);
				}
				else {
					t_pool = r_pooling;
					//为防止单个标本数据量过大，对最大值进行限制，当前限制4.5pooling系数，相当于45M
					if(r_pooling.compareTo(maxSize) > 0) {
						t_pool = maxSize.add(BigDecimal.ZERO);
					}
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pool.doubleValue());
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					rowMap.put("m", t_pool.doubleValue());
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pool.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);
					log.error(" AdjustNcPcPooling调整Q2.0的pooling系数为 直接系数，行数：" + rowMap.get("a") + 
							" pooling：" + m_pooling + " =>" + r_pooling + " r_pooling:" + r_pooling + " 差额:" + diff_pooling);
				}
				diff_pooling = diff_pooling.subtract(t_pool).add(m_pooling);
				Sum_pooling = Sum_pooling.subtract(m_pooling).add(t_pool);
			}
		}
		//第二步加到MDL001 的外参上
		if(MDL1NcPcList!=null && MDL1NcPcList.size() > 0) {
			//计算每一个NC和PC的调整系数.
			BigDecimal b_coeff = diff_pooling.divide(BigDecimal.valueOf(MDL1NcPcList.size()),3,RoundingMode.DOWN);
			for (int index = MDL1NcPcList.size() - 1; index >= 0; index--) {
				Map<String, Object> rowMap = MDL1NcPcList.get(index);
				//获取经过系数调整后的pooling系数
				BigDecimal m_pooling = BigDecimal.valueOf((Double)rowMap.get("m"));
				BigDecimal r_pooling = b_coeff.add(m_pooling).setScale(3,RoundingMode.DOWN);
				//计算体积最大值情况下的pooling系数（**前提条件：pooling系数设置成与下机下机数据量*U17 之间一样
				BigDecimal theory_pooling = u5.multiply(BigDecimal.valueOf((Double)rowMap.get("b")))
						.multiply(u7).multiply(u10).divide(u17.multiply(theMinimum).multiply(z7),3,RoundingMode.DOWN);
				BigDecimal t_pool	 = BigDecimal.ZERO;
				// 当理论最大系数小于调整过的pooling系数，则设置为理论系数
				if(r_pooling.compareTo(theory_pooling) > 0) {
					t_pool = theory_pooling;
					//为防止单个标本数据量过大，对最大值进行限制，当前限制4.5pooling系数，相当于45M
					if(theory_pooling.compareTo(MAX_DATASIZE) > 0) {
						t_pool = new BigDecimal("5.5");
					}
					//直接设置超过最大值的行的pooling系数为 最大值
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pool.doubleValue());
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					rowMap.put("m", t_pool.doubleValue());
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pool.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);
					log.error(" AdjustNcPcPooling 设置MDL001的NcPc的pooling系数为理论系数。行数：" + rowMap.get("a") + 
							" pooling：" + m_pooling + " =>" + t_pool + " theory_pooling:" + theory_pooling + " 差额:" + diff_pooling);
				}
				else {
					t_pool = r_pooling;
					//为防止单个标本数据量过大，对最大值进行限制，当前限制4.5pooling系数，相当于45M
					if(r_pooling.compareTo(MAX_DATASIZE) > 0) {
						t_pool = new BigDecimal("5.5");
					}
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pool.doubleValue());
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					rowMap.put("m", t_pool.doubleValue());
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pool.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);
					log.error(" AdjustNcPcPooling调整MDL001的NcPc的pooling系数为 直接系数，行数：" + rowMap.get("a") + 
							" pooling：" + m_pooling + " =>" + t_pool + " r_pooling:" + r_pooling + " 差额:" + diff_pooling);
				}
				diff_pooling = diff_pooling.subtract(t_pool).add(m_pooling);
				Sum_pooling = Sum_pooling.subtract(m_pooling).add(t_pool);
			}
		}
		//如果还有剩余则加到MDL004 的外参上(废弃)
		//如果有剩余的，则同步加到 MDL004和其他tNGS上
		/*if(MDL4NcPcList!=null && MDL4NcPcList.size() > 0 && diff_pooling.compareTo(new BigDecimal("0")) > 0) {
			//计算每一个NC和PC的调整系数。
			//防止MDL004 NC和PC这边加的太多，打算除以一半
			BigDecimal b_coeff = diff_pooling.divide(BigDecimal.valueOf(MDL4NcPcList.size()).multiply(new BigDecimal("2")),3,RoundingMode.DOWN);
			for (int index = MDL4NcPcList.size() - 1; index >= 0; index--) {
				Map<String, Object> rowMap = MDL4NcPcList.get(index);
				//获取经过系数调整后的pooling系数
				BigDecimal m_pooling = BigDecimal.valueOf((Double)rowMap.get("m"));
				BigDecimal r_pooling = b_coeff.add(m_pooling).setScale(3,RoundingMode.DOWN);
				//计算体积最大值情况下的pooling系数（**前提条件：pooling系数设置成与下机下机数据量*U17 之间一样
				BigDecimal theory_pooling = u5.multiply(BigDecimal.valueOf((Double)rowMap.get("b")))
						.multiply(u7).multiply(u10).divide(u17.multiply(theMinimum).multiply(z7),3,RoundingMode.DOWN);
				BigDecimal t_pool = BigDecimal.ZERO;
				// 当理论最大系数小于调整过的pooling系数，则设置为理论系数
				if(r_pooling.compareTo(theory_pooling) > 0) {
					t_pool = theory_pooling;
					//为防止单个标本数据量过大，对最大值进行限制，当前限制4.5pooling系数，相当于3M
					if(r_pooling.compareTo(new BigDecimal("0.3")) > 0) {
						t_pool = new BigDecimal("0.3");
					}
					//直接设置超过最大值的行的pooling系数为 最大值
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pool.doubleValue());
					
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					rowMap.put("m", t_pool.doubleValue());
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pool.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);

					log.error(" AdjustNcPcPooling 设置MDL004的NcPc的pooling系数为理论系数。行数：" + rowMap.get("a") + 
							" pooling：" + m_pooling + " =>" + t_pool + " theory_pooling:" + theory_pooling + " 差额:" + diff_pooling);
				}
				else {
					t_pool = r_pooling;
					//为防止单个标本数据量过大，对最大值进行限制，当前限制4.5pooling系数，相当于45M
					if(r_pooling.compareTo(new BigDecimal("0.3")) > 0) {
						t_pool = new BigDecimal("0.3");
					}
					sheet.getRow((int)rowMap.get("rowIndex")).getCell(n_cellIndex).setCellValue(t_pool.doubleValue());
					BigDecimal jNum = new BigDecimal(String.valueOf(rowMap.get("j")));//稀释液浓度
					rowMap.put("m", t_pool.doubleValue());
					//因为J跟G一样 pooling系数/B列(浓度) == t_mDivideJ
					BigDecimal t_mDivideJ = t_pool.divide(jNum,6,RoundingMode.HALF_UP);
					rowMap.put("mDivideJ",t_mDivideJ);
					log.error(" AdjustNcPcPooling调整MDL004的NcPc的pooling系数为 直接系数，行数：" + rowMap.get("a") + 
							" pooling：" + m_pooling + " =>" + t_pool + " r_pooling:" + r_pooling  + " 差额:" + diff_pooling);
				}
				diff_pooling = diff_pooling.subtract(t_pool).add(m_pooling);
				Sum_pooling = Sum_pooling.subtract(m_pooling).add(t_pool);
			}
		}*/
		
		//对specialAList进行排序，先把所有的标本计算每一行的  M / J  , 进行排序 从小到大, 放在Alist 下
		specialAList = specialAList.stream().sorted(Comparator.comparingDouble(m -> ((BigDecimal) m.get("mDivideJ")).doubleValue())).collect(Collectors.toList());
		//对specialBList进行排序，先把所有的标本计算每一行的  M / J  , 进行排序 从大到小, 放在Blist 下
		specialBList = specialBList.stream().sorted(Comparator.comparingDouble((Map<String, Object> m)->((BigDecimal) m.get("mDivideJ")).doubleValue()).reversed()).collect(Collectors.toList());
		
		log.error("AdjustNcPcPooling最终完成，退出。 最终pooling系数之和=" + Sum_pooling);
		resultMap.put("sum_pooling", Sum_pooling);
		
		return resultMap;
	}
	
	/**
	 * 根据文库名称，分别抽取数据到相应NcPc队列里
	 * @param MDL1NcPcList MDL001的NC和QC标本
	 * @param MDL4NcPcList MDL004的NC和QC标本
	 * @param MDL1OtherList MDL001的Q标本
	 * @param rowMap
	 * @return
	 */
	private boolean checkLib(List<Map<String, Object>> MDL1NcPcList,List<Map<String, Object>> MDL4NcPcList
			,List<Map<String, Object>> MDL1OtherList,Map<String, Object> rowMap) {
		String libName = (String)rowMap.get("a");
		if (libName.contains("-tNGS")  || libName.contains("-HS") || libName.contains("-TBtNGS")
				||libName.contains("MDL004-NC-") || libName.contains("MDL004-PC-")
				|| libName.contains("MDL005-tNC-") || libName.contains("MDL005-tPC-")
				|| libName.contains("MDL004-mNC-") || libName.contains("MDL004-mPC-")) {
			if(libName.contains("MDL004-NC-") || libName.contains("MDL004-PC-")
					|| libName.contains("MDL005-tNC-") || libName.contains("MDL005-tPC-")
					|| libName.contains("MDL004-mNC-") || libName.contains("MDL004-mPC-")) {
				MDL4NcPcList.add(rowMap);
			}
		}else {
			if(libName.contains("-PC-") ||libName.contains("-NC-")
					||libName.contains("-mPC-")||libName.contains("-mNC-")) {
				MDL1NcPcList.add(rowMap);
			}else {
				MDL1OtherList.add(rowMap);
			}
		}
		return true;
	}

	/**
	 * 处理setting,并存放进map中
	 *
	 * @param map
	 * @param setting
	 * @param isAll
	 */
	public void dealMapAndSetting(Map<String, Object> map, Map<String, Object> setting, boolean isAll) {
		if (setting != null) {
			//0设置“Coefficient”,T:39~V:49
			if (setting.get("Coefficient") != null) {
				map.put("Coefficient", setting.get("Coefficient"));
			}
			//1设置“试剂选择”,Y:27
			if (setting.get("ReagentName") != null) {
				map.put("sjxz", setting.get("ReagentName"));
			}
			//2设置“预pooling终体积(uL)”,AA:5,Z:5
			if (setting.get("PoolVolume") != null) {
				map.put("ypztj", setting.get("PoolVolume"));
			}
			//3设置“上机混合文库终浓度(pM)”,AA:6,Z:6
			if (setting.get("Concentration") != null) {
				map.put("sjhhwkznd", setting.get("Concentration"));
			}
			//4设置“文库参考体积”,U:5,V:5
			if (setting.get("LibraryReferenceVolume") != null) {
				map.put("wkcktj", setting.get("LibraryReferenceVolume"));
			}
			//5设置“稀释文库参考体积”,U:6,V:6
			if (setting.get("DilutedLibraryReferenceVolume") != null) {
				map.put("xswkcktj", setting.get("DilutedLibraryReferenceVolume"));
			}
			//6设置“稀释倍数”,U:7,V:7
			if (setting.get("DilutionFactor") != null) {
				map.put("xsbs", setting.get("DilutionFactor"));
			}
			//7设置“定量使用“原液”还是“稀释液””,U:8,V:8
			if (setting.get("SolutionType") != null) {
				map.put("dly", ((Map<String, Object>) setting.get("SolutionType")).get("value"));
			} else {
				map.put("dly", "稀释液");
			}
			//8设置“数据量/pooling 系数（M reads）”,U:17,V:17
			if (setting.get("MReadsMultiple") != null) {
				map.put("cnsd", setting.get("MReadsMultiple"));
			}
			//9设置“混合文库稀释体积(ul)”,U:26,V:26
			if (setting.get("MixDilutedLibraryReferenceVolume") != null) {
				map.put("hhwkxstj", setting.get("MixDilutedLibraryReferenceVolume"));
			}
			//10设置“混合文库稀释浓度设定(pM)”,U:27,V:27
			if (setting.get("MixDilutedLibraryReferenceConcentration") != null) {
				map.put("hhwkxsnd", setting.get("MixDilutedLibraryReferenceConcentration"));
			}
			//11设置“最小上机次数”
			if (setting.get("TheMinimum") != null) {
				map.put("TheMinimum", setting.get("TheMinimum")!=null?setting.get("TheMinimum"):1.5);
			}
			//12设置“是否进行稀释”
			if (setting.get("needDilute") != null) {
				map.put("needDilute", setting.get("needDilute"));
			}
			if (!isAll) {
				//1设置“试剂选择”,Y:27
				if (setting.get("tpool_ReagentName") != null) {
					map.put("sjxz", setting.get("tpool_ReagentName"));
				}
				//2设置“预pooling终体积(uL)”,AA:5,Z:5
				if (setting.get("tpool_PoolVolume") != null) {
					map.put("ypztj", setting.get("tpool_PoolVolume"));
				}
				//3设置“上机混合文库终浓度(pM)”,AA:6,Z:6
				if (setting.get("tpool_Concentration") != null) {
					map.put("sjhhwkznd", setting.get("tpool_Concentration"));
				}
				//4设置“文库参考体积”,U:5,V:5
				if (setting.get("tpool_LibraryReferenceVolume") != null) {
					map.put("wkcktj", setting.get("tpool_LibraryReferenceVolume"));
				}
				//5设置“稀释文库参考体积”,U:6,V:6
				if (setting.get("tpool_DilutedLibraryReferenceVolume") != null) {
					map.put("xswkcktj", setting.get("tpool_DilutedLibraryReferenceVolume"));
				}
				//6设置“稀释倍数”,U:7,V:7
				if (setting.get("tpool_DilutionFactor") != null) {
					map.put("xsbs", setting.get("tpool_DilutionFactor"));
				}
				//7设置“定量使用“原液”还是“稀释液””,U:8,V:8
				if (setting.get("tpool_SolutionType") != null) {
					map.put("dly", ((Map<String, Object>) setting.get("tpool_SolutionType")).get("value"));
				} else {
					map.put("dly", "稀释液");
				}
				//8设置“数据量/pooling 系数（M reads）”,U:17,V:17
				if (setting.get("tpool_MReadsMultiple") != null) {
					map.put("cnsd", setting.get("tpool_MReadsMultiple"));
				}
				//9设置“混合文库稀释体积(ul)”,U:26,V:26
				if (setting.get("tpool_MixDilutedLibraryReferenceVolume") != null) {
					map.put("hhwkxstj", setting.get("tpool_MixDilutedLibraryReferenceVolume"));
				}
				//10设置“混合文库稀释浓度设定(pM)”,U:27,V:27
				if (setting.get("tpool_MixDilutedLibraryReferenceConcentration") != null) {
					map.put("hhwkxsnd", setting.get("tpool_MixDilutedLibraryReferenceConcentration"));
				}
			}
		}
	}

	/**
	 * 设置pooling系数批量调整 表
	 * @param sheet
	 * @param map
	 */
	private void setPoolingFactorSheet(Sheet sheet,Map<String,Object> map){
		if (map != null && map.get("Coefficient") != null){
			int repalceRowIndex = 38;
			int nameCellIndex = calculateXPosition("T");
			int keyCellIndex = calculateXPosition("U");
			int valueCellIndex = calculateXPosition("V");
			Object coefficient = map.get("Coefficient");
			List<Map<String,Object>> poolingCoefficientList = (List<Map<String, Object>>) coefficient;
			Map<String,Cell> cellMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(poolingCoefficientList)){
				while (true) {
					Row row = sheet.getRow(repalceRowIndex);
					if (row == null) {
						break;
					}
					Cell t = row.getCell(nameCellIndex);
					if (t == null) {
						break;
					}
					Cell u = row.getCell(keyCellIndex);
					Cell v = row.getCell(valueCellIndex);
					if (v == null) {
						break;
					}
					if (StringUtil.isNotBlank(u.getStringCellValue())){
						cellMap.put(u.getStringCellValue(),v);
					}else {
						cellMap.put("blank",v);
						break;
					}
					repalceRowIndex ++;
				}
				for (Map<String, Object> poolingCoefficient : poolingCoefficientList) {
					Object key = poolingCoefficient.get("key");
					//Object name = poolingCoefficient.get("name");
					Object value = poolingCoefficient.get("value");
					Class<?> cls = value.getClass();
					if (key != null){
						Cell cell = cellMap.get(key);
						if(cls.getName().contains("Integer"))
							cell.setCellValue(((Integer) value).doubleValue());
						else
							cell.setCellValue(((BigDecimal) value).doubleValue());
					} else {
						Cell cell = cellMap.get("blank");
						if(cls.getName().contains("Integer"))
							cell.setCellValue(((Integer) value).doubleValue());
						else
							cell.setCellValue(((BigDecimal) value).doubleValue());
					}
				}
			}
		}
	}

	/**
	 * pooling导出后处理
	 * @param workbook
	 */
	private void afterPoolingDeal(Workbook workbook, List<WksjmxDto> wksjmxList, IWksjmxService wksjmxService){
		List<WksjmxDto> list = new ArrayList<>();
		int numberOfSheets = workbook.getNumberOfSheets();
		if (numberOfSheets == 2) {
			Sheet tpoolSheet = workbook.getSheet("tpool");
			Sheet quanSheet = workbook.getSheet("全");
			getAAndRValue(tpoolSheet, 3, list);
			getAAndRValue(quanSheet, 4, list);
		} else if (numberOfSheets == 1) {
			Sheet quanSheet = workbook.getSheet("全");
			getAAndRValue(quanSheet, 3, list);
		}
		for (WksjmxDto wk : list) {
			Optional<WksjmxDto> first = wksjmxList.stream().filter(item -> item.getWkbm().equals(wk.getWkbm())).findFirst();
			if (first.isPresent()){
				WksjmxDto wksjmxDto = first.get();
				wk.setWksjmxid(wksjmxDto.getWksjmxid());
			}
		}
		boolean isSuccess = wksjmxService.updateWksjmxAfterPooling(list);
		log.error("文库、数据量:{},{}", isSuccess, JSON.toJSONString(list));
	}

	/**
	 * 获取A和R的值
	 * @param sheet
	 * @param startRowIndex
	 * @param list
	 */
	private void getAAndRValue(Sheet sheet, int startRowIndex, List<WksjmxDto> list) {
		int a_cellIndex = calculateXPosition("A");//文库名称
		int r_cellIndex = calculateXPosition("R");//预计下机量
		while (true) {
			WksjmxDto wksjmxDto = new WksjmxDto();
			Row row = sheet.getRow(startRowIndex);
			if (row == null) {
				break;
			}
			Cell cell = row.getCell(a_cellIndex);
			if (cell == null) {
				break;
			}
			String a_cellValue = row.getCell(a_cellIndex).getStringCellValue();
			if (StringUtil.isNotBlank(a_cellValue)) {
				String r_cellValue = String.valueOf(row.getCell(r_cellIndex).getNumericCellValue());
				wksjmxDto.setWkbm(a_cellValue);
				wksjmxDto.setYjxjsjl(r_cellValue);
				list.add(wksjmxDto);
			} else {
				break;
			}
			startRowIndex++;
		}
	}
}
