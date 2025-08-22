package com.matridx.springboot.util.file.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读EXCEL文件
 * @author goofus
 *
 */
public class ExcelHelper {
    private static final Logger log = LoggerFactory.getLogger(ExcelHelper.class);
	private XSSFWorkbook xssfWorkbook;
	
	private XSSFSheet xssfSheet;
	
	private HSSFWorkbook hssfWorkbook;

	private HSSFSheet hssfSheet;
	
	private String fileName;
	
	private InputStream is;
	
	private int maxRowlength;
	
	public final static String EXCEL_POSTFIX_XLS = ".xls";
	public final static String EXCEL_POSTFIX_XLSX = ".xlsx";
//	public final static String EXCEL_POSTFIX_TXT = ".txt";
	public final static String DOT_UNICODE = ".";
	
	/**
     * 读Excel文件，自动识别.xlsx或.xls
     * @author goofus
     */
    public List<Map<String,String>> readExcel(String excelFile) throws Exception {
        if (StringUtils.isBlank(excelFile)) {
            return null;
        } else {
        	fileName = excelFile;
            String postfix = excelFile.substring(excelFile.lastIndexOf(DOT_UNICODE)).toLowerCase();
            if(StringUtils.isBlank(postfix)) return null;
            if (EXCEL_POSTFIX_XLS.equals(postfix)) {
                return readXls(excelFile);
            } else if (EXCEL_POSTFIX_XLSX.equals(postfix)) {
                return readXlsx(excelFile);
            }
        }
        return null;
    }
    
    /**
     * 读Excel文件，自动识别.xlsx或.xls.此方法不会根据头部限定读取的列数，但因为每次都要判断列数，效率会降低。此方法主要是为了区分readExcel 用
     * @author goofus
     */
    public List<Map<String,String>> readExcelALL(String excelFile) throws Exception {
        if (StringUtils.isBlank(excelFile)) {
            return null;
        } else {
        	fileName = excelFile;
            String postfix = excelFile.substring(excelFile.lastIndexOf(DOT_UNICODE)).toLowerCase();
            if(StringUtils.isBlank(postfix)) return null;
            if (EXCEL_POSTFIX_XLS.equals(postfix)) {
                return readXlsALL(excelFile);
            } else if (EXCEL_POSTFIX_XLSX.equals(postfix)) {
                return readXlsxALL(excelFile);
            }
        }
        return null;
    }
    
	/**
     * 读Excel文件，自动识别.xlsx或.xls
     * @author goofus
     */
    public List<Map<String,String>> readExcel(String excelFile,int i_stratNum,int i_endNum) throws Exception {
        if (StringUtils.isBlank(excelFile)) {
            return null;
        } else {
        	fileName = excelFile;
            String postfix = excelFile.substring(excelFile.lastIndexOf(DOT_UNICODE)).toLowerCase();
            if(StringUtils.isBlank(postfix)) return null;
            if (EXCEL_POSTFIX_XLS.equals(postfix)) {
                return readXls(excelFile,i_stratNum,i_endNum);
            } else if (EXCEL_POSTFIX_XLSX.equals(postfix)) {
                return readXlsx(excelFile,i_stratNum,i_endNum);
            }
        }
        return null;
    }

    /**
     * 读.xlsx文件
     * @author goofus
     */
    public List<Map<String,String>> readXlsx(String excelFile) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
        try {
            is = new FileInputStream(excelFile);
            xssfWorkbook = new XSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        List<Map<String,String>> rows = new ArrayList<>();
    	//只读取第一个SHEET
        xssfSheet = xssfWorkbook.getSheetAt(0);
        if (xssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = xssfSheet.getLastRowNum();
        maxRowlength = rowTotal;
        int cellTotal;
		/*
		 * XSSFRow headRow = xssfSheet.getRow(0); if(null != headRow) cellTotal
		 * = headRow.getPhysicalNumberOfCells();
		 */
		int number=0;
            for (int rowNum = 0; rowNum <= rowTotal; rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (rowNum==0){
                    number=xssfRow.getPhysicalNumberOfCells()-1;
                }else {
                    for (int i=0;i<xssfRow.getPhysicalNumberOfCells();i++){
                        if (i>number){
                            number=number+1;
                            throw new Exception("第"+ number +"列有问题,请检查(可能存在空格)!");
                        }
                    }
                }
                cellTotal = xssfRow.getLastCellNum();

                Map<String,String> row = new HashMap<>();
                //迭代每个单元格
                for(int c=0; c<cellTotal; c++){
                    XSSFCell cell = xssfRow.getCell(c);
                    row.put(""+c ,getValue(cell));//取单元格的值
                }
                rows.add(row);
            }
        return rows;
    }
    
    /**
     * 读.xlsx文件
     * @author goofus
     */
    public List<Map<String,String>> readXlsxALL(String excelFile) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
        is = new FileInputStream(excelFile);
        xssfWorkbook = new XSSFWorkbook(is);
        List<Map<String,String>> rows = new ArrayList<>();
    	//只读取第一个SHEET
        xssfSheet = xssfWorkbook.getSheetAt(0);
        if (xssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = xssfSheet.getLastRowNum();
        maxRowlength = rowTotal;
        
        for (int rowNum = 0; rowNum <= rowTotal; rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) continue;
            //每次迭代重新获取列数
            int cellTotal = xssfRow.getPhysicalNumberOfCells();
            Map<String,String> row = new HashMap<>();
            //迭代每个单元格
            for(int c=0; c<cellTotal; c++){
            	XSSFCell cell = xssfRow.getCell(c);
            	row.put(""+c ,getValue(cell));//取单元格的值
            }
            rows.add(row);
        }
        return rows;
    }
    
    /**
     * 返回最大行数
     */
    public int getMaxRowlength(){
    	return maxRowlength;
    }
    
    /**
     * 读.xlsx文件 扩展 因内存溢出
     * @author goofus
     */
    public List<Map<String,String>> readXlsx(String excelFile,int i_startNum,int i_endNum) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
        is = new FileInputStream(excelFile);
        xssfWorkbook = new XSSFWorkbook(is);
        List<Map<String,String>> rows = new ArrayList<>();
    	//只读取第一个SHEET
        xssfSheet = xssfWorkbook.getSheetAt(0);
        if (xssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = xssfSheet.getLastRowNum();
        maxRowlength = rowTotal;
        if(rowTotal < i_endNum)
        	i_endNum = rowTotal;
        int cellTotal = 0;
        XSSFRow headRow = xssfSheet.getRow(0);
        if(null != headRow) cellTotal = headRow.getPhysicalNumberOfCells();
        for (int rowNum = i_startNum; rowNum <= i_endNum; rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) continue;
            
            Map<String,String> row = new HashMap<>();
            //迭代每个单元格
            for(int c=0; c<cellTotal; c++){
            	XSSFCell cell = xssfRow.getCell(c);
            	row.put(""+c ,getValue(cell));//取单元格的值
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 读.xls文件
     * @author goofus
     */
    public List<Map<String,String>> readXls(String excelFile) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
        try {
            is = new FileInputStream(excelFile);
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }
        List<Map<String,String>> rows = new ArrayList<>();
        //只读取第一个SHEET
        hssfSheet = hssfWorkbook.getSheetAt(0);
        if (hssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = hssfSheet.getLastRowNum();
        maxRowlength = rowTotal;
        int cellTotal;
		/*
		 * HSSFRow headRow = hssfSheet.getRow(0); if(null != headRow) cellTotal
		 * = headRow.getPhysicalNumberOfCells();
		 */
        for (int rowNum = 0; rowNum <= rowTotal; rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) continue;
            cellTotal = hssfRow.getPhysicalNumberOfCells();

            Map<String,String> row = new HashMap<>();
            //迭代每个单元格
            for(int c=0; c<cellTotal; c++){
            	HSSFCell cell = hssfRow.getCell(c);
            	row.put(""+c, getValue(cell));//取单元格的值
            }
            rows.add(row);
        }
        return rows;
    }
    
    /**
     * 读.xls文件
     * @author goofus
     */
    public List<Map<String,String>> readXlsALL(String excelFile) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
        is = new FileInputStream(excelFile);
        hssfWorkbook = new HSSFWorkbook(is);
        List<Map<String,String>> rows = new ArrayList<>();
        //只读取第一个SHEET
        hssfSheet = hssfWorkbook.getSheetAt(0);
        if (hssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = hssfSheet.getLastRowNum();
        maxRowlength = rowTotal;
        
        for (int rowNum = 0; rowNum <= rowTotal; rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) continue;

            int cellTotal = hssfRow.getPhysicalNumberOfCells();
            
            Map<String,String> row = new HashMap<>();
            //迭代每个单元格
            for(int c=0; c<cellTotal; c++){
            	HSSFCell cell = hssfRow.getCell(c);
            	row.put(""+c, getValue(cell));//取单元格的值
            }
            rows.add(row);
        }
        return rows;
    }
    
    /**
     * 读.xls文件 扩展 因内存溢出
     * @author goofus
     */
    public List<Map<String,String>> readXls(String excelFile,int i_startNum,int i_endNum) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
        is = new FileInputStream(excelFile);
        hssfWorkbook = new HSSFWorkbook(is);
        List<Map<String,String>> rows = new ArrayList<>();
        //只读取第一个SHEET
        hssfSheet = hssfWorkbook.getSheetAt(0);
        if (hssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = hssfSheet.getLastRowNum();
        maxRowlength = rowTotal;
        if(rowTotal < i_endNum)
        	i_endNum = rowTotal;
        int cellTotal = 0;
        HSSFRow headRow = hssfSheet.getRow(0);
        if(null != headRow) cellTotal = headRow.getPhysicalNumberOfCells();
        for (int rowNum = i_startNum; rowNum <= i_endNum; rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow == null) continue;
            
            Map<String,String> row = new HashMap<>();
            //迭代每个单元格
            for(int c=0; c<cellTotal; c++){
            	HSSFCell cell = hssfRow.getCell(c);
            	row.put(""+c, getValue(cell));//取单元格的值
            }
            rows.add(row);
        }
        return rows;
    }
    
    @SuppressWarnings({ "static-access", "deprecation" })
    private String getValue(XSSFCell xssfCell) {
    	if(null == xssfCell) return "";
        if (xssfCell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == CellType.NUMERIC) {
			if(HSSFDateUtil.isCellDateFormatted(xssfCell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue()));
			}else{
				DecimalFormat df = new DecimalFormat("#.#####");
				return df.format(xssfCell.getNumericCellValue());
			}
			//return String.valueOf(xssfCell.getNumericCellValue());
        } else {
        	xssfCell.setCellType(CellType.STRING);
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    @SuppressWarnings({ "static-access", "deprecation" })
    private String getValue(HSSFCell hssfCell) {
    	if(null == hssfCell) return "";
        if (hssfCell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == CellType.NUMERIC) {
        	if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()));
			}else{
				DecimalFormat df = new DecimalFormat("#.####");
				return df.format(hssfCell.getNumericCellValue());
			}
            //return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    public void setValue(int row,int column,String value){
    	if(xssfSheet != null){
    		if(xssfSheet.getRow(row)==null)
    			return;
    		XSSFCell cell= xssfSheet.getRow(row).getCell(column);
    		if(cell == null)
    			cell = xssfSheet.getRow(row).createCell(column);
    		cell.setCellValue(value);
    	}else{
    		if(hssfSheet.getRow(row)==null)
    			return;
    		HSSFCell cell= hssfSheet.getRow(row).getCell(column);
    		if(cell == null)
    			cell = hssfSheet.getRow(row).createCell(column);
    		cell.setCellValue(value);
    	}
    }
    
    public boolean saveFile() throws Exception{
    	FileOutputStream out = null;
    	try{
    		is.close();
    		is = null;
	    	out = new FileOutputStream(fileName);
	    	if(xssfWorkbook != null){
	    		xssfWorkbook.write(out);
	    	}else{
	    		hssfWorkbook.write(out);
	    	}
	    	out.close();
        }catch(Exception e){
    		if(out!=null){
    			out.close();
            }
    		if(is!=null){
    			is.close();
    			is = null;
    		}
    	}
    	
    	return true;
    }
    
}
