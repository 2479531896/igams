package com.matridx.igams.common.util;

import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 将List<Map<String,Object>> 类型数据导出到Excel文件
 */
public class DataToExcel {
    //data数据
    private List<Map<String, Object>> mapList;
    //文件名称
    private String fileName;
    //替换及判断参数
    private Map<String, Object> csMap;
    //报表导出类型
    private String dclx;
    private RedisUtil redisUtil;

    public String getDclx() {
        return dclx;
    }

    public void setDclx(String dclx) {
        this.dclx = dclx;
    }

    public void init(List<Map<String, Object>> mapList, String fileName, Map<String, Object> csMap, RedisUtil redisUtil, String dclx) {
        this.mapList = mapList;
        this.fileName = fileName;
        this.csMap = csMap;
        this.redisUtil = redisUtil;
        this.dclx = dclx;
    }

    private static final Logger logger = LoggerFactory.getLogger(DataToExcel.class);
    /**
     * @description 生成excel
     * @param
     * @return
     */
    public  void createExcel() {
        logger.error("数据转成Excel...");
        redisUtil.hset("EXP_:_"+ csMap.get("wjid"),"Fin", false,3600);
        redisUtil.hset("EXP_:_"+ csMap.get("wjid"),"Cnt", "0");
        String tempPath = (String) csMap.get("tempPath"); //临时文件路径

        XSSFWorkbook wb =null;
        FileOutputStream outputStream = null;
        try {

        String templateFilePath = csMap.get("wjlj") != null ? csMap.get("wjlj").toString() : "";
        DBEncrypt dbEncrypt = new DBEncrypt();
        File templateFile = new File(dbEncrypt.dCode(templateFilePath));
        //获取数据源的 key, 用于获取列数及设置标题
        Map<String, Object> map = mapList.get(0);
        Set<String> stringSet = map.keySet();
        List<String> headList = new ArrayList<>(stringSet);
        // 通过字符串的ascii码排序
        headList = headList.stream().sorted(Comparator.comparing(String::toString)).collect(Collectors.toList());
        // 定义一个新的工作簿
         wb = new XSSFWorkbook(new FileInputStream(templateFile));
        // 创建一个Sheet页
         wb.setSheetName(0,fileName.replace(".xlsx","").replace(".xls",""));
        XSSFSheet sheet = wb.getSheetAt(0);
        replaceSheet(csMap,sheet);
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeight(10);
        // 标题字体样式
        XSSFFont titleFont = wb.createFont();
        // 加粗
        titleFont.setBold(true);
        // 字体名称
        titleFont.setFontName("宋体");
        // 字体大小
        titleFont.setFontHeight(10);
        // 标题样式
        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(titleFont);
        // 竖向居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 横向居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        // 边框
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        // 数字样式
        XSSFCellStyle numStyle = wb.createCellStyle();
        // 设置字体css
        numStyle.setFont(font);
        // 竖向居中
        numStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 横向居中
        numStyle.setAlignment(HorizontalAlignment.CENTER);
        // 边框
        numStyle.setBorderBottom(BorderStyle.THIN);
        numStyle.setBorderLeft(BorderStyle.THIN);
        numStyle.setBorderRight(BorderStyle.THIN);
        numStyle.setBorderTop(BorderStyle.THIN);
        DataFormat df = wb.createDataFormat();
        numStyle.setDataFormat(df.getFormat("0.00_ "));
        //获得表格第一行
        Row row;
        //chen 优化标题获取 根据数据源信息给第一行每一列设置标题
        if (StringUtil.isNotBlank(String.valueOf(csMap.get("bbcsdm")))&&String.valueOf(csMap.get("bbcsdm")).contains("BMFY")){
            row = sheet.getRow(3);
            for (int i = 2; i < headList.size(); i++) {
                Cell cell = row.createCell(i);
                String value = headList.get(i).split("@")[1];
                if (isNum(value)){
                    cell.setCellValue(Double.parseDouble(value));
                }else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(titleStyle);
            }
            XSSFRow rows;
            XSSFCell cells;
            //循环拿到的数据给所有行每一列设置对应的值
            for (int i = 0; i < mapList.size(); i++) {

                // 在这个sheet页里创建一行
                rows = sheet.createRow(i + 10);
                //chen 优化值获取 给该行数据赋值
                for (int j = 0; j < headList.size(); j++) {
                    String value;
                    if (mapList.get(i).get(headList.get(j)) != null) {
                        value = String.valueOf(mapList.get(i).get(headList.get(j)));
                    } else {
                        value = "";
                    }
                    cells = rows.createCell(j);
                    if (isNum(value)){
                        cells.setCellValue(Double.parseDouble(value));
                    }else {
                        cells.setCellValue(value);
                    }
                    cells.setCellStyle(numStyle);
                }
            }
            mergeSameCellContentColumn(sheet,11,0);
        }else if (StringUtil.isNotBlank(String.valueOf(csMap.get("bbcsdm")))&&String.valueOf(csMap.get("bbcsdm")).contains("CBFY")){
            row = sheet.getRow(2);
            for (int i = 0; i < headList.size(); i++) {
                Cell cell = row.createCell(i);
                String value = headList.get(i).split("@")[1];
                if (isNum(value)){
                    cell.setCellValue(Double.parseDouble(value));
                }else {
                    cell.setCellValue(value);
                }
                cell.setCellStyle(titleStyle);
            }
            XSSFRow rows;
            XSSFCell cells;
            //循环拿到的数据给所有行每一列设置对应的值
            for (int i = 0; i < mapList.size(); i++) {

                // 在这个sheet页里创建一行
                rows = sheet.createRow(i + 3);
                //chen 优化值获取 给该行数据赋值
                for (int j = 0; j < headList.size(); j++) {
                    String value;
                    if (mapList.get(i).get(headList.get(j)) != null) {
                        value = String.valueOf(mapList.get(i).get(headList.get(j)));
                    } else {
                        value = "";
                    }
                    cells = rows.createCell(j);
                    if (isNum(value)){
                        cells.setCellValue(Double.parseDouble(value));
                    }else {
                        cells.setCellValue(value);
                    }
                    cells.setCellStyle(numStyle);
                }
            }
            mergeSameCellContentColumn(sheet,3,0);
        }
            int RowCells=sheet.getRow(sheet.getLastRowNum()-1).getPhysicalNumberOfCells();
            for (int i = 0; i < RowCells; i++) {
                //设置列宽
                sheet.setColumnWidth(i, 4000);
            }
            File file = new File(tempPath + BusTypeEnum.FINANCE_ACCOUNT.getCode());
            file.mkdirs();
            String pathname= tempPath + BusTypeEnum.FINANCE_ACCOUNT.getCode() + "/" + fileName.replace(".xlsx", "").replace(".xls", "") + "-" + new Date().getTime() + ".xls";
            // 输出Excel文件
            outputStream = new FileOutputStream(pathname);
            wb.write(outputStream);
            redisUtil.hset("EXP_:_"+csMap.get("wjid"), "Fin",true);
            redisUtil.hset("EXP_:_"+csMap.get("wjid"),"fileName", fileName.replace(".xlsx", "").replace(".xls", "") + "-" + new Date().getTime() + ".xls");
            redisUtil.hset("EXP_:_"+csMap.get("wjid"),"filePath", pathname);
        } catch (Exception e) {
            logger.error("DataToExcel-createExcel"+e.getMessage());
            redisUtil.hset("EXP_:_"+csMap.get("wjid"), "Fin",true);
            String errorMsg = e.toString();
            if(StringUtil.isBlank(errorMsg))
                errorMsg = "无错误信息";
            redisUtil.hset("EXP_:_"+csMap.get("wjid"),"Msg",errorMsg);
        }finally {
            try {
                if (wb!=null){
                    wb.close();
                }
               if (outputStream!=null){
                   outputStream.close();
               }
            } catch (Exception e) {
                redisUtil.hset("EXP_:_"+csMap.get("wjid"),"Msg",e.getMessage());
            }
        }
    }
    /**
     * 替换Excel模板文件内容
     *
     * @param replaceMap 文档数据
     */
    public void replaceSheet(Map<String, Object> replaceMap, Sheet sheet) {
        try {

            for (int j=0;j<sheet.getPhysicalNumberOfRows();j++){
                Row row = sheet.getRow(j);
                int num = row.getLastCellNum()+1;
                for (int i = 0; i < num; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null ||cell.getCellType()!= CellType.STRING || cell.getStringCellValue() == null) {
                        continue;
                    }
                    String value = cell.getStringCellValue();
                    int startTagPos = value.indexOf("«");
                    int endTagPos = value.indexOf("»");
                    if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                        cell.setCellValue(replaceValue(value, replaceMap,startTagPos,endTagPos));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("DataToExcel-replaceSheet"+e.getMessage());
        }
    }
    /**
     * 替换内容
     *
     * @param value
     * @param replaceMap
     * @return
     */
    public  String replaceValue(String value, Map<String, Object> replaceMap,int startTagPos,int endTagPos) {
        String str = value.substring(startTagPos+1, endTagPos);
        if (StringUtil.isBlank(str)) {
            return value;
        }
        value = value.replace("«" + str + "»", replaceMap.get(str)!=null?(replaceMap.get(str).toString()):"");
        return value;
    }
    /**
     * 合并指定Excel sheet页、指定列中连续相同内容的单元格
     *
     * @param sheet    Excel sheet
     * @param startRow 从第几行开始， startRow的值从1开始
     * @param column   指定列
     */
    public void mergeSameCellContentColumn(Sheet sheet, int startRow, int column) {
        int totalRows = sheet.getLastRowNum();
        int firstRow = 0;
        int lastRow = 0;
        // 上一次比较是否相同
        boolean isPrevCompareSame = false;
        String prevMergeAddress = null;
        String currentMergeAddress;
        // 从第几开始判断是否相同
        if (totalRows >= startRow) {
            for (int i = startRow; i <= totalRows; i++) {
                String lastRowCellContent = sheet.getRow(i - 1).getCell(column).getStringCellValue();
                String curRowCellContent = sheet.getRow(i).getCell(column).getStringCellValue();
                if (curRowCellContent.equals(lastRowCellContent)) {
                    if (!isPrevCompareSame) {
                        firstRow = i - 1;
                    }
                    lastRow = i;
                    isPrevCompareSame = true;
                } else {
                    isPrevCompareSame = false;
                    currentMergeAddress = firstRow + lastRow + column + column + "";
                    if (lastRow > firstRow && !Objects.equals(currentMergeAddress, prevMergeAddress)) {
                        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, column, column));
                        prevMergeAddress = currentMergeAddress;
                    }
                }
                // 最后一行时判断是否有需要合并的行
                if ((i == totalRows) && (lastRow > firstRow)) {
                    currentMergeAddress = firstRow + lastRow + column + column + "";
                    if (!Objects.equals(currentMergeAddress, prevMergeAddress)) {
                        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, column, column));
                    }
                }
            }
        }
    }
    public  boolean isNum(String str) {
        if (StringUtil.isBlank(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str.replace("-",""));
        return isNum.matches();
    }

    public void createRevenueCostExcel() {
        logger.error("createRevenueCostExcel 数据转成Excel...");
        redisUtil.hset("EXP_:_"+ csMap.get("wjid"),"Fin", false,3600);
        redisUtil.hset("EXP_:_"+ csMap.get("wjid"),"Cnt", "0");
        String tempPath = (String) csMap.get("tempPath"); //临时文件路径

        XSSFWorkbook wb =null;
        FileOutputStream outputStream = null;
        try {

            String templateFilePath = csMap.get("wjlj") != null ? csMap.get("wjlj").toString() : "";
            DBEncrypt dbEncrypt = new DBEncrypt();
            File templateFile = new File(dbEncrypt.dCode(templateFilePath));
            Map<String, List<LinkedHashMap<String, Object>>> resultMap = dealData(mapList);
            // 定义一个新的工作簿
            wb = new XSSFWorkbook(new FileInputStream(templateFile));
            // 创建一个Sheet页
            wb.setSheetName(0,fileName.replace(".xlsx","").replace(".xls",""));
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFFont font = wb.createFont();
            font.setFontName("宋体");
            font.setFontHeight(10);
            XSSFCellStyle bodyStyle = wb.createCellStyle();
            // 设置字体css
            bodyStyle.setFont(font);
            // 竖向居中
            bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 横向居中
            bodyStyle.setAlignment(HorizontalAlignment.CENTER);
            // 边框
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);
            bodyStyle.setBorderTop(BorderStyle.THIN);
            XSSFRow rows;
            XSSFCell cells;
            int hs = 2;
            //循环拿到的数据给所有行每一列设置对应的值
            for (String cCusName : resultMap.keySet()) {
                List<LinkedHashMap<String, Object>> list = resultMap.get(cCusName);
                for (LinkedHashMap<String, Object> linkedHashMap : list) {
                    Set<String> keys = linkedHashMap.keySet();
                    rows = sheet.createRow(hs);
                    hs++;
                    int zds = 0;
                    cells = rows.createCell(zds);
                    zds++;
                    cells.setCellValue(cCusName);
                    cells.setCellStyle(bodyStyle);
                    for (String key : keys) {
                        String value;
                        if (linkedHashMap.get(key) != null) {
                            value = String.valueOf(linkedHashMap.get(key));
                        } else {
                            value = "";
                        }
                        cells = rows.createCell(zds);
                        zds++;
                        cells.setCellValue(value);
                        cells.setCellStyle(bodyStyle);
                    }
                }
            }
            mergeSameCellContentColumn(sheet,3,0);
            int RowCells=sheet.getRow(sheet.getLastRowNum()-1).getPhysicalNumberOfCells();
            for (int i = 0; i < RowCells; i++) {
                //设置列宽
                sheet.setColumnWidth(i, 4000);
            }
            File file = new File(tempPath + BusTypeEnum.FINANCE_ACCOUNT.getCode());
            file.mkdirs();
            String pathname= tempPath + BusTypeEnum.FINANCE_ACCOUNT.getCode() + "/" + fileName.replace(".xlsx", "").replace(".xls", "") + "-" + new Date().getTime() + ".xls";
            // 输出Excel文件
            outputStream = new FileOutputStream(pathname);
            wb.write(outputStream);
            redisUtil.hset("EXP_:_"+csMap.get("wjid"), "Fin",true);
            redisUtil.hset("EXP_:_"+csMap.get("wjid"),"fileName", fileName.replace(".xlsx", "").replace(".xls", "") + "-" + new Date().getTime() + ".xls");
            redisUtil.hset("EXP_:_"+csMap.get("wjid"),"filePath", pathname);
        } catch (Exception e) {
            logger.error("DataToExcel-createExcel"+e.getMessage());
            redisUtil.hset("EXP_:_"+csMap.get("wjid"), "Fin",true);
            String errorMsg = e.toString();
            if(StringUtil.isBlank(errorMsg))
                errorMsg = "无错误信息";
            redisUtil.hset("EXP_:_"+csMap.get("wjid"),"Msg",errorMsg);
        }finally {
            try {
                if (wb!=null){
                    wb.close();
                }
                if (outputStream!=null){
                    outputStream.close();
                }
            } catch (Exception e) {
                redisUtil.hset("EXP_:_"+csMap.get("wjid"),"Msg",e.getMessage());
            }
        }
    }

    private Map<String,List<LinkedHashMap<String, Object>>> dealData(List<Map<String, Object>> mapList) {
        Map<String,List<LinkedHashMap<String, Object>>> result = new HashMap<>();
        List<String> cCusNames = mapList.stream().map(e->String.valueOf(e.get("cCusName"))).distinct().collect(Collectors.toList());
        for (String cCusName : cCusNames) {
            //客户 最大行数
            int maxhs = mapList.stream().filter(e -> e.get("cCusName").equals(cCusName)).collect(Collectors.groupingBy(
                    e -> e.get("yf"),
                    Collectors.counting()
            )).values().stream().max(Long::compare).get().intValue();
            List<LinkedHashMap<String, Object>> list = new ArrayList<>();
            for (int hs = 1; hs <= maxhs; hs++) {
                //初始化值
                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
                for (int i = 1; i <= 12; i++) {
                    linkedHashMap.put("客户总成本"+i,"");
                    linkedHashMap.put("客户总收入"+i,"");
                    linkedHashMap.put("产品名称"+i,"");
                    linkedHashMap.put("销售单号"+i,"");
                    linkedHashMap.put("成本"+i,"");
                    linkedHashMap.put("收入"+i,"");
                }
                list.add(linkedHashMap);
                Iterator<Map<String, Object>> iterator = mapList.iterator();
                while (iterator.hasNext()){
                    Map<String, Object> map = iterator.next();
                    if (hs==1){
                        if (map.get("cCusName").equals(cCusName) && !"".equals(map.get("zcb"))){
                            linkedHashMap.put("客户总成本"+map.get("yf"),map.get("zcb"));
                            linkedHashMap.put("客户总收入"+map.get("yf"),map.get("zsr"));
                            linkedHashMap.put("产品名称"+map.get("yf"),map.get("cInvName"));
                            linkedHashMap.put("销售单号"+map.get("yf"),map.get("cDLCode"));
                            linkedHashMap.put("成本"+map.get("yf"),map.get("iSum"));
                            linkedHashMap.put("收入"+map.get("yf"),map.get("iPrice"));
                            iterator.remove();
                        }
                    }else {
                        if (map.get("cCusName").equals(cCusName) && "".equals(map.get("zcb")) && "".equals(linkedHashMap.get("产品名称"+map.get("yf")))){
                            linkedHashMap.put("客户总成本"+map.get("yf"),map.get("zcb"));
                            linkedHashMap.put("客户总收入"+map.get("yf"),map.get("zsr"));
                            linkedHashMap.put("产品名称"+map.get("yf"),map.get("cInvName"));
                            linkedHashMap.put("销售单号"+map.get("yf"),map.get("cDLCode"));
                            linkedHashMap.put("成本"+map.get("yf"),map.get("iSum"));
                            linkedHashMap.put("收入"+map.get("yf"),map.get("iPrice"));
                            iterator.remove();
                        }
                    }
                }
            }
            result.put(cCusName,list);
        }
        return result;
    }
}