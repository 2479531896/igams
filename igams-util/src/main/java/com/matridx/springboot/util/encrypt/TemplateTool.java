package com.matridx.springboot.util.encrypt;


import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.matridx.springboot.util.base.StringUtil;

public class TemplateTool {

//	private HSSFWorkbook hssfWorkbook;
//
//	private HSSFSheet hssfSheet;
//
//	private String fileName;


	/**
      * 读.xlsx文件 扩展 因内存溢出
     * @author goofus
     */
    public static  List<Map<String,String>> readXlsx(String excelFile,int i_startNum,int i_endNum) throws Exception {
    	if(StringUtils.isBlank(excelFile)) return null;
		InputStream is = new FileInputStream(excelFile);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<Map<String,String>> rows = new ArrayList<>();
    	//只读取第一个SHEET
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        if (xssfSheet == null) return rows;
        
        //迭代读每一行
        int rowTotal = xssfSheet.getLastRowNum();
		if(rowTotal < i_endNum)
        	i_endNum = rowTotal;
        int cellTotal = 0;
        XSSFRow headRow = xssfSheet.getRow(0);
        if(null != headRow) cellTotal = headRow.getPhysicalNumberOfCells();//是获取不为空的列个数
        for (int rowNum = i_startNum; rowNum <= i_endNum; rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow == null) continue;
            
            Map<String,String> row = new HashMap<>();
            //迭代每个单元格
            String cellDate = null,dateSpecial=null, cellTime=null,timeSpecial=null, cellNum=null, cellRandom=null;
			for(int c=0; c<cellTotal; c++){
            	if(c==0) {
            		XSSFCell cell = xssfRow.getCell(c);
            		dateSpecial = getValue(cell);//取单元格的值
            		if(StringUtil.isBlank(dateSpecial))
            			return rows;
            		cellDate = getValue(cell).replaceAll("\\.", "");
                	row.put(""+c ,getValue(cell));
            	}else if(c==1) {
            		XSSFCell cell = xssfRow.getCell(c);
            		timeSpecial = getValue(cell);
            		cellTime = getValue(cell).replaceAll("\\:", "");
                	row.put(""+c ,getValue(cell));
            	}else if(c==2) {
            		XSSFCell cell = xssfRow.getCell(c);
            		cellNum = getValue(cell);
                	row.put(""+c ,getValue(cell));
            	}else if(c==3) {
            		XSSFCell cell = xssfRow.getCell(c);
            		cellRandom = getValue(cell);
                	row.put(""+c ,getValue(cell));
            	}                   
            }
            

            String fileName = "cnad-" + cellDate + cellTime +".CSV";//组装文件名字生产文件
        	
        	//根据cellNum判断共需要生产多少个文件
        	int flag =1;//标记这个标本总共可以出现的次数
        	int j=1, k=1;
			if (cellNum != null) {
				while(j <= Integer.parseInt(cellNum)) {
					flag = createFile(fileName, cellNum, cellRandom, dateSpecial, timeSpecial, flag);

	//        		//分成多个文件的时候需要间隔3-5min的文件名，主要处理time，得到一个新的specialTime
					String neoTimeSpecial = null;
					if (timeSpecial != null) {
						neoTimeSpecial = dealBatchTime(timeSpecial);
					}
					String neoCellTime = null;
					if (neoTimeSpecial != null) {
						neoCellTime = neoTimeSpecial.replaceAll("\\:", "");
					}

                    fileName = "cnad-" + cellDate + neoCellTime +".CSV";
					timeSpecial = neoTimeSpecial;

					j= k*8;
					k++;
				}
			}
			rows.add(row);
        }
        return rows;
    }
    
    
    //这个方法主要每8个一组时候用来组装名称的timeSpecial部分
	private static String dealBatchTime(String timeSpecial) {
		String result;
		
		//3-5min，生成一个180-300秒的随机数，并分割随机数为分和秒
		Random randomSecond = new Random();
		int randomSec = randomSecond.nextInt(300)%(300-180+1)+180;
		int randMin = randomSec/60;
		int randSec = randomSec%60;
		
		//分割speciTime
		String[] time = timeSpecial.split(":");
		int timeHour = Integer.parseInt(time[0]);
		int timeMinutes = Integer.parseInt(time[1]);
		int timeSeconds = Integer.parseInt(time[2]);	
		
		//随机生成的时间和原本的时间相加
        int min = timeMinutes + randMin;
		int second = timeSeconds + randSec;
		int addMin = 0;
		int addHour = 0;
		String hourStr,minStr,secondStr;
		
		if(second >= 60) {
			secondStr = String.valueOf(second-60);
			result = leftAddFormat(secondStr, 2, "0");
			addMin = 1;
		}else {
			secondStr = String.valueOf(second);
			result = leftAddFormat(secondStr, 2, "0");
		}
		if(min + addMin >=60) {
			minStr = String.valueOf(min + addMin-60);
			result = leftAddFormat(minStr, 2, "0")+":"+result;
			addHour = 1;
		}else {
			minStr = String.valueOf(min+addMin);
			result = leftAddFormat(minStr, 2, "0")+":"+result;
		}
		//得到新的specialTime
		hourStr = String.valueOf(timeHour + addHour);
		result = leftAddFormat(hourStr, 2, "0")+":"+result;

		return result;
	}
    
	 /**
	   * 根据位数左边补充相应的字符
	   * @param src 原始字符串
	   * @param length 规定位数
	   * @param addchar 添加的字符
	   */
	  public static String leftAddFormat(String src,int length,String addchar){
	    StringBuilder result = new StringBuilder();

	    if(src.length() >= length)
	      return src;
	    else{
            result.append(String.valueOf(addchar).repeat(length - src.length()));
	      result.append(src);
	    }
	    return result.toString();
	  }

	
	
	public static int createFile(String fileName, String cellNum, String cellRandom, String dateSpecial, String timeSpecial, int flag) throws IOException{
	    String sourceFilePath = "E:\\sheetfile\\cnad-202003031437221.CSV";
		String targetFilePath = "E:\\createFile\\createNewFile6\\" + fileName;
		
		FileWriter out = new FileWriter(targetFilePath);		
		BufferedReader read = new BufferedReader(new FileReader(sourceFilePath));
		
		//Experiment Finish后面用来替换的字段
		dateSpecial = dateSpecial.replaceAll("\\.", "/");
//		timeSpecial = timeSpecial.substring(0, 5);	
		String repStrFinish = dateSpecial+" " + timeSpecial;
		
		int cellValue = Integer.parseInt(cellNum);
		String str;
		while((str = read.readLine()) != null){
			if(str.contains("Experiment Finished")) {//将Experiment Finished改行进行替换处理
				String repStr = "Experiment Finished:," + repStrFinish;
				out.write(repStr);
				out.write("\r\n");
			}else if(str.contains("Sample Name")) {//替换标本随机数
				//先把sample这一行的标题读完
				out.write(str);
				out.write("\r\n");
				//循环输出标本个数
				for(int n=1; n<=8 && flag<=cellValue ; n++) {
					int targetNum = createSampleNum(cellRandom);
					String sampleNum = String.valueOf(targetNum);
					sampleNum = leftAddFormat(sampleNum, 6, "0");
					String repSampleStr = n + "," + "标本" + n + "," + sampleNum;//替换行
					System.out.println(repSampleStr);
					out.write(repSampleStr);
					out.write("\r\n");
					flag++;
				}
				break;
		    }else{
				out.write(str);
				out.write("\r\n");
			}
		}		
		out.flush();
		out.close();		
		read.close();		
		return flag;
	}

	private static int createSampleNum(String cellRandom) {
		//取一个规定范围内的随机值
		int numMin =  Integer.parseInt(cellRandom.substring(0,cellRandom.indexOf("-")));
		int numMax =  Integer.parseInt(cellRandom.substring(cellRandom.indexOf("-")+1));
		Random random = new Random();
        return random.nextInt(numMax)%(numMax-numMin+1)+numMin;
	}
    
    
    private static String getValue(XSSFCell xssfCell) {
    	if(null == xssfCell) return "";
        if (xssfCell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == CellType.NUMERIC) {
			if(HSSFDateUtil.isCellDateFormatted(xssfCell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue()));
			}else{
				DecimalFormat df = new DecimalFormat("#.####");
				return df.format(xssfCell.getNumericCellValue());
			}
			//return String.valueOf(xssfCell.getNumericCellValue());
        } else {
            //xssfCell.setCellType(xssfCell.CELL_TYPE_STRING);
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }
       

	public static void main(String[] args) throws Exception {
		readXlsx("E:\\sheetfile\\MD027.xlsx", 1, 100);
	}
	
	
	
//	/**
//	   * 两个字符串时间进行相加，只计算时间，不计算日期
//	   * @param firstTime
//	   * @param secondTIime
//	   * @return
//	   */
//	  public static String addExcuteTime(String firstTime,String secondTIime){
//	    if(StringUtil.isBlank(firstTime) || StringUtil.isBlank(secondTIime)){
//	      return null;
//	    }
//	    String[] firsts = firstTime.split(":");
//	    String[] seconds = secondTIime.split(":");
//	    if(firsts.length !=3 || seconds.length!= 3)
//	    {
//	      return null;
//	    }
//	    String result;
//	    try {
//	      int hour = Integer.parseInt(firsts[0]) + Integer.parseInt(seconds[0]);
//	      int min = Integer.parseInt(firsts[1]) + Integer.parseInt(seconds[1]);
//	      int second = Integer.parseInt(firsts[2]) + Integer.parseInt(seconds[2]);
//	      int addMin = 0;
//	      int addHour = 0;
//	      //加算秒
//	      if(second >= 60){
//	    	  String.valu
//	        result = StringUtil.leftAddFormat(String.valueOf(second - 60),2,"0");
//	        addMin = 1;
//	      }else{
//	        result = StringUtil.leftAddFormat(String.valueOf(second),2,"0");
//	      }
//	      if(min + addMin >= 60){
//	        result = StringUtil.leftAddFormat(String.valueOf(min+ addMin - 60),2,"0") + ":" + result;
//	        addHour =1;
//	      }else{
//	        result = StringUtil.leftAddFormat(String.valueOf(min+ addMin),2,"0") + ":" + result;
//	      }
//	      result = StringUtil.leftAddFormat(String.valueOf(hour + addHour),2,"0") + ":" + result;
//	    }catch (Exception e){
//	      Log.e("DateTimeUtil",e.getMessage());
//	      return null;
//	    }
//	    return result;
//	  }
	

}
