package com.matridx.las.netty.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.las.netty.dao.entities.WkmxDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;

public class XWPFLibraryUtil {
	private Logger log = LoggerFactory.getLogger(XWPFLibraryUtil.class);

	public Map<String, Object> replaceDetection(Map<String, Object> map, IFjcfbService fjcfbService, String FTP_URL) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String releasePath = (String) map.get("releaseFilePath"); // 正式文件路径
		String tempPath = (String) map.get("tempPath"); // 临时文件路径
		FileInputStream fileInputStream = null;
		XSSFWorkbook  sheets  = null;
		OutputStream output = null;
		try {
			String templateFilePath = map.get("wjlj") != null ? map.get("wjlj").toString() : ""; // 模板路径
			DBEncrypt dbEncrypt = new DBEncrypt();
			File templateFile = new File(dbEncrypt.dCode(templateFilePath));
			if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
				log.error("模板不存在！请重新确认模板！");
				resultMap.put("message", "模板不存在！请重新确认模板！");
				resultMap.put("status", "error");
				return resultMap;
			}
			fileInputStream = new FileInputStream(templateFile);
			sheets = new XSSFWorkbook(fileInputStream);
			 // 获取第一个表单Sheet
			XSSFSheet sheetAt = sheets.getSheetAt(0);
			//得到Excel工作表的行数
			int rowcount = sheetAt.getLastRowNum();
			//获取要替换的单元格位置
			int libraryNamesum = 0;
			int quantitysum = 0;
			int rowsum = 0;
			boolean flg = false;
			for (int i = 1; i < rowcount; i++) {
				if(flg)
					break;
				 //获得当前行所有单元格
				int cellcount = sheetAt.getRow(i).getLastCellNum();
				for(int j = 0; j < cellcount; j++) {
					if(flg)
						break;
					//遍历单元格
					XSSFCell cell = sheetAt.getRow(i).getCell((short) j);
					//判断单元格的值是否等于文库名称
					if("文库名称".equals(cell.getStringCellValue())) {
						libraryNamesum = j;
						rowsum = i+1;
					}
					if("Quantity".equals(cell.getStringCellValue())) {
						quantitysum = j;
						flg = true;
					}
				}				
			 }

			//遍历文库集合
			@SuppressWarnings("unchecked")
			List<WkmxDto> wksyypList = (List<WkmxDto>) map.get("wksyypList");
			for (int w = 0; w < wksyypList.size(); w++) {
				 //获得指定单元格
				XSSFCell libraryName = sheetAt.getRow(rowsum).getCell((short) libraryNamesum);
				XSSFCell quantity = sheetAt.getRow(rowsum).getCell((short) quantitysum);
				libraryName.setCellValue(wksyypList.get(w).getLibraryName()); 
				quantity.setCellValue(wksyypList.get(w).getQuantity());
				rowsum = rowsum + 1;
			}
			

			fileInputStream.close();
			// 临时文件路径
			String tempFilePath = tempPath + BusTypeEnum.IMP_LIBRARY_EXCEL.getCode() + "/" + "UP"
					+ DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP"
					+ DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP"
					+ DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			// 正式文件名
			String releaseFilePath = releasePath + BusTypeEnum.IMP_LIBRARY_EXCEL.getCode() + "/" + "UP"
					+ DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP"
					+ DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP"
					+ DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			// 文件名
			String fileName = System.currentTimeMillis() + RandomUtil.getRandomString() + ".xls";
			mkDirs(tempFilePath);
			output = new FileOutputStream(tempFilePath + "/" + fileName);
			
			sheets.write(output);

			mkDirs(releaseFilePath);
			CutFile(tempFilePath + "/" + fileName, releaseFilePath + "/" + fileName, true);

			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto fjcfbDto = new FjcfbDto();

			String fjid = StringUtil.generateUUID();
			fjcfbDto.setFjid(fjid);
			fjcfbDto.setYwid(map.get("wkid").toString());
			fjcfbDto.setZywid("");
			fjcfbDto.setXh("1");
			fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_EXCEL.getCode());
			String wjm =  "pooling.xls";
			fjcfbDto.setWjm(wjm);
			fjcfbDto.setWjlj(bpe.eCode(releaseFilePath + "/" + fileName));
			fjcfbDto.setFwjlj(bpe.eCode(releaseFilePath));
			fjcfbDto.setFwjm(bpe.eCode(fileName));
			fjcfbDto.setZhbj("0");
			boolean isTrue = fjcfbService.insert(fjcfbDto);
			resultMap.put("fjcfbDto", fjcfbDto);
			if (isTrue) {
				// 删除旧的文件
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.toString());
		} finally {
			try {
				if (output != null)
					output.close();
				if (sheets != null)
					sheets.close();
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		resultMap.put("message", "文件生成完成");
		resultMap.put("status", "success");
		resultMap.put("word_ywlx", BusTypeEnum.IMP_LIBRARY_EXCEL.getCode());
		return resultMap;
	}



	/**
	 * 根据路径创建文件
	 * 
	 * @param storePath
	 * @return
	 */
	private static boolean mkDirs(String storePath) {
		File file = new File(storePath);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdirs();
	}

	/**
	 * 从原路径剪切到目标路径
	 * @param s_srcFile
	 * @param s_distFile
	 * @param coverFlag
	 * @return
	 */
	private static boolean CutFile(String s_srcFile, String s_distFile, boolean coverFlag) {
		boolean flag = false;
		// 路径如果为空则直接返回错误
		if (StringUtil.isBlank(s_srcFile) || StringUtil.isBlank(s_distFile))
			return flag;

		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		// 文件不存在则直接返回错误
		if (!srcFile.exists())
			return flag;
		// 目标文件已经存在
		if (distFile.exists()) {
			if (coverFlag) {
				srcFile.renameTo(distFile);
				flag = true;
			}
		} else {
			srcFile.renameTo(distFile);
			flag = true;
		}
		return flag;
	}
}
