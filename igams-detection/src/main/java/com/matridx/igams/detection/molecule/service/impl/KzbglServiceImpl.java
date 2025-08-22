package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbglDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbglModel;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxDto;
import com.matridx.igams.detection.molecule.dao.post.IKzbglDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IKzbglService;
import com.matridx.igams.detection.molecule.service.svcinterface.IKzbmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class KzbglServiceImpl extends BaseBasicServiceImpl<KzbglDto, KzbglModel, IKzbglDao> implements IKzbglService{

    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IFzjcxxService fzjcxxService;
    @Autowired
    IKzbmxService kzbmxService;
    private static final String XLSX = ".xlsx";
    public static final String ROW_MERGE = "row_merge";
    public static final String COLUMN_MERGE = "column_merge";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final int CELL_OTHER = 0;
    private static final int CELL_ROW_MERGE = 1;
    private static final int CELL_COLUMN_MERGE = 2;

    private final Logger log = LoggerFactory.getLogger(KzbglServiceImpl.class);
    /**
     * 通过角色ID获取角色检测单位
     */
    @Override
    public List<Map<String, String>> getJsjcdwByjsid(String dqjs) {
        return dao.getJsjcdwByjsid(dqjs);
    }

    @Override
    public String generateKzbh(KzbglDto kzbglDto) {
//        板号：实验室编号（检测单位）-日期-三位序号
//        默认选角色单位限制的第一位检测单位的参数代码，这里需要考虑一下没有检测单位的情况
        String jcdwbh = "";
        List<Map<String,String>> jcdwList = dao.getJsjcdwByjsid(kzbglDto.getDqjs());
        if (jcdwList != null && jcdwList.size()>0){
            if (StringUtil.isNotBlank(jcdwList.get(0).get("jcdw"))){
                jcdwbh = jcdwList.get(0).get("jcdw");
            }
        }
        JcsjDto jcsjDto = jcsjService.getDtoById(jcdwbh);
        if (jcsjDto != null ){
            jcdwbh = jcsjDto.getCsdm();
        }
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String dqrq = sf.format(date);
        String prefix = jcdwbh + "-" + dqrq + "-";
        String serial = dao.getKzbhSerial(prefix);
        return prefix + serial;
    }

    /**
     * 新增扩增板明细数据
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertKzbmx(KzbglDto kzbglDto) {
        try{
        	List<String> syhs = kzbglDto.getSyhs();
            //首先组装出所有明细数据，一次插入表中
//            Map<String,Object> map = new HashMap<>();
//            map.put("ybbhs",kzbglDto.getYbbhs());
            List<Map<String,String>> mapList;
            Map<String,String> ybbhAndFzjcidmap = new HashMap<>();
            List<KzbmxDto> kzbmxlist = new ArrayList<>();
            if (kzbglDto.getYbbhs() != null && kzbglDto.getYbbhs().length>0 ){
                mapList = fzjcxxService.getYbbhAndFzjcid(kzbglDto.getYbbhs());
                for (Map<String, String> stringStringMap : mapList) {
                    ybbhAndFzjcidmap.put(stringStringMap.get("ybbh"), stringStringMap.get("fzjcid"));
                }
            }
            kzbglDto.setKzbid(StringUtil.generateUUID());
            dao.insertDto(kzbglDto);
            if (  (kzbglDto.getYbbhs().length == kzbglDto.getXhs().length) && (kzbglDto.getYbbhs().length == kzbglDto.getHls().length )  ){
                for (int i=0 ; i<kzbglDto.getYbbhs().length ; i++){
                    KzbmxDto kzbmxDto = new KzbmxDto();
                    for(String s:syhs){
                        String[] split = s.split(",");
                        if(split[0].equals(kzbglDto.getYbbhs()[i])){
                            kzbmxDto.setSyh(split[1]);
                        }
                    }
                    kzbmxDto.setKzbmxid(StringUtil.generateUUID());
                    kzbmxDto.setKzbid(kzbglDto.getKzbid());
                    kzbmxDto.setXh( kzbglDto.getXhs()[i] );
                    kzbmxDto.setHs(kzbglDto.getHls()[i].substring(0,1));
                    kzbmxDto.setLs(kzbglDto.getHls()[i].substring( kzbglDto.getHls()[i].indexOf("-")+1));
                    kzbmxDto.setYbbh(kzbglDto.getYbbhs()[i]);
                    kzbmxDto.setFzjcid(ybbhAndFzjcidmap.get(kzbglDto.getYbbhs()[i]));
                    kzbmxDto.setLrry(kzbglDto.getLrry());
                    kzbmxlist.add(kzbmxDto);
                }
                kzbmxService.batchInsertDtoList(kzbmxlist);
            }
            return true;
        }catch (Exception e){
            log.error(e.toString());
            log.error("KzbglServiceImpl中insertKzbmx出错"+e.getMessage());
            throw e;
        }
    }

    /**
     * 修改扩增板相关信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateData(KzbglDto kzbglDto) {
        try {
            List<String> syhs = kzbglDto.getSyhs();
            //首先组装出所有明细数据，一次插入表中
//            Map<String,Object> map = new HashMap<>();
//            map.put("ybbhs",kzbglDto.getYbbhs());
            List<Map<String,String>> mapList;
            Map<String,String> ybbhAndFzjcidmap = new HashMap<>();
            List<KzbmxDto> kzbmxlist = new ArrayList<>();
            if (kzbglDto.getYbbhs() != null && kzbglDto.getYbbhs().length>0 ){
                mapList = fzjcxxService.getYbbhAndFzjcid(kzbglDto.getYbbhs());
                for (Map<String, String> stringStringMap : mapList) {
                    ybbhAndFzjcidmap.put(stringStringMap.get("ybbh"), stringStringMap.get("fzjcid"));
                }
            }
            dao.updateDto(kzbglDto);
            Map<String,Object> delemap = new HashMap<>();
            String[] ids = new String[1];
            ids[0] = kzbglDto.getKzbid();
            delemap.put("kzbidlist", ids);
            delemap.put("scry", kzbglDto.getScry());
            kzbmxService.deleteKzbmxByKzbid(delemap);
            if (  (kzbglDto.getYbbhs().length == kzbglDto.getXhs().length) && (kzbglDto.getYbbhs().length == kzbglDto.getHls().length )  ){
                for (int i=0 ; i<kzbglDto.getYbbhs().length ; i++){
                    KzbmxDto kzbmxDto = new KzbmxDto();
                    for(String s:syhs){
                        String[] split = s.split(",");
                        if(split[0].equals(kzbglDto.getYbbhs()[i])){
                            kzbmxDto.setSyh(split[1]);
                        }
                    }
                    kzbmxDto.setKzbmxid(StringUtil.generateUUID());
                    kzbmxDto.setKzbid(kzbglDto.getKzbid());
                    kzbmxDto.setXh( kzbglDto.getXhs()[i] );
                    kzbmxDto.setHs(kzbglDto.getHls()[i].substring(0,1));
                    kzbmxDto.setLs(kzbglDto.getHls()[i].substring( kzbglDto.getHls()[i].indexOf("-")+1));
                    kzbmxDto.setYbbh(kzbglDto.getYbbhs()[i]);
                    kzbmxDto.setFzjcid(ybbhAndFzjcidmap.get(kzbglDto.getYbbhs()[i]));
                    kzbmxDto.setLrry(kzbglDto.getLrry());
                    kzbmxlist.add(kzbmxDto);
                }
                kzbmxService.batchInsertDtoList(kzbmxlist);
            }
            return true;
        }catch (Exception e){
            log.error("KzbglServiceImpl中updateData出错"+e.getMessage());
            return false;
        }
    }

    /**
     * 删除扩增板相关信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteData(KzbglDto kzbglDto) {
        try{
            Map<String,Object> map = new HashMap<>();
            map.put("kzbidlist", kzbglDto.getIds());
            map.put("scry", kzbglDto.getScry());
            dao.delete(kzbglDto);
            kzbmxService.deleteKzbmxByKzbid(map);
            return true;
        }catch (Exception e){
            log.error("KzbglServiceImpl中deleteData出错"+e.getMessage());
            return false;
        }
    }

    /**
     * 找出不存在的ybbh数据
     */
    @Override
    public List<String> exitYbbh(KzbglDto kzbglDto) {
        List<String> exitslist = fzjcxxService.getYbbhList(kzbglDto.getYbbhs());
        String[] ybbhs = kzbglDto.getYbbhs();
        List<String> notExits = new ArrayList<>();
        List<String> exits = new ArrayList<>();
        for (String exitStr :ybbhs) {
            if(!"-K1".equals(exitStr)&&!"-K2".equals(exitStr)&&!"-K3".equals(exitStr)&&!"-NC".equals(exitStr)&&!"-PC".equals(exitStr)&&!"-QC".equals(exitStr)){
                if(!exitslist.contains(exitStr)){
                    notExits.add(exitStr);
                }else{
                    exits.add(exitStr);
                }
            }
        }
        
        if (notExits.size() < 1) {
            List<String> notExistSyh = new ArrayList<>();
            String[] array=exits.toArray(new String[]{});
            List<String> ybbhsBySyhExist = fzjcxxService.getYbbhsBySyhExist(array);
            List<String> existSyh = new ArrayList<>();
            for(String s:ybbhsBySyhExist){
                String[] split = s.split(",");
                existSyh.add(split[0]);
            }
            for (String exitStr :ybbhs) {
                if(!"-K1".equals(exitStr)&&!"-K2".equals(exitStr)&&!"-K3".equals(exitStr)&&!"-NC".equals(exitStr)&&!"-PC".equals(exitStr)&&!"-QC".equals(exitStr)){
                    if(!existSyh.contains(exitStr)){
                        notExistSyh.add(exitStr);
                    }
                }
            }
            if(notExistSyh.size()>0){
            	return notExistSyh;
            }
            kzbglDto.setSyhs(ybbhsBySyhExist);
        }
        return notExits;
    }

    /**
     * 新增扩增板
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertKzb(KzbglDto kzbglDto, FzjcxxDto fzjcxxDto, User user){
        kzbglDto.setKzbid(StringUtil.generateUUID());
        if(StringUtil.isNotBlank(fzjcxxDto.getSyry())){
            Date date_t = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String createdate = sdf.format(date_t);
            kzbglDto.setSyrq(createdate);
        }
        kzbglDto.setLrry(user.getYhid());
        kzbglDto.setJcdw(fzjcxxDto.getJcdw());
        boolean isSuccess=insertDto(kzbglDto);
        if(!isSuccess){
            return false;
        }
        KzbmxDto kzbmxDto=new KzbmxDto();
        kzbmxDto.setKzbmxid(StringUtil.generateUUID());
        kzbmxDto.setSyh(fzjcxxDto.getSyh());
        kzbmxDto.setKzbid(kzbglDto.getKzbid());
        kzbmxDto.setYbbh(fzjcxxDto.getYbbh());
        kzbmxDto.setXh(fzjcxxDto.getKzbxh());
        kzbmxDto.setLrry(user.getYhid());
        kzbmxDto.setHs(fzjcxxDto.getHs());
        kzbmxDto.setLs(fzjcxxDto.getLs());
        isSuccess=kzbmxService.insert(kzbmxDto);
        return isSuccess;
    }

    public void exportKzb(HttpServletResponse response, File file, String fileName, String sheetName,
                               List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
        // 整个 Excel 表格 book 对象
        SXSSFWorkbook book = new SXSSFWorkbook();
        // 每个 Sheet 页
        Sheet sheet = book.createSheet(sheetName);
        // 设置表头背景色
        CellStyle headStyle = book.createCellStyle();
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        CellStyle headStyle2 = book.createCellStyle();
        headStyle2.setAlignment(HorizontalAlignment.CENTER);
        headStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置表格列宽度（默认为15个字节）
        sheet.setDefaultColumnWidth(15);
        // 创建合并算法数组
        int rowLength = sheetDataList.size();
        int columnLength = sheetDataList.get(0).size();
        int[][] mergeArray = new int[rowLength][columnLength];
        for (int i = 0; i < sheetDataList.size(); i++) {
            // 每个 Sheet 页中的行数据
            Row row = sheet.createRow(i);
            List<Object> rowList = sheetDataList.get(i);
            for (int j = 0; j < rowList.size(); j++) {
                // 每个行数据中的单元格数据
                Object o = rowList.get(j);
                int v;
                Cell cell = row.createCell(j);
                if (i == 0) {
                    // 第一行为表头行，默认白底色
                    if(j<13){
                        v = setCellValue(cell, o, headStyle);
                    }else{
                        v = setCellValue(cell, o,headStyle2);
                    }
                } else {
                    // 其他行为数据行，默认白底色
                    CellStyle rowStyle = book.createCellStyle();
                    if(i<11){
                        if(j>12){
                            rowStyle.setAlignment(HorizontalAlignment.CENTER);
                            rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        }else{
                            rowStyle.setAlignment(HorizontalAlignment.CENTER);
                            rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            rowStyle.setBorderBottom(BorderStyle.THIN);
                            rowStyle.setBorderTop(BorderStyle.THIN);
                            rowStyle.setBorderLeft(BorderStyle.THIN);
                            rowStyle.setBorderRight(BorderStyle.THIN);
                        }
                    }else{
                        rowStyle.setAlignment(HorizontalAlignment.CENTER);
                        rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    }
                    v = setCellValue(cell, o, rowStyle);
                }
                mergeArray[i][j] = v;
            }
        }
        // 合并单元格
        mergeCells(sheet, mergeArray);
        // 写数据
        if (response != null) {
            // 前端导出
            try {
                write(response, book, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 本地导出
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                ByteArrayOutputStream ops = new ByteArrayOutputStream();
                book.write(ops);
                fos.write(ops.toByteArray());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置当前Sheet页的单元格样式以及数据处理
     *
     */
    private static int setCellValue(Cell cell, Object o, CellStyle style) {
        // 设置样式
        cell.setCellStyle(style);
        // 数据为空时
        if (o == null) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue("");
            return CELL_OTHER;
        }
        // 是否为字符串
        if (o instanceof String) {
            String s = o.toString();
            cell.setCellType(CellType.STRING);
            cell.setCellValue(s);
            if (s.equals(ROW_MERGE)) {
                return CELL_ROW_MERGE;
            } else if (s.equals(COLUMN_MERGE)) {
                return CELL_COLUMN_MERGE;
            } else {
                return CELL_OTHER;
            }
        }
        // 是否为字符串
        if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(o.toString()));
            return CELL_OTHER;
        }
        // 是否为Boolean
        if (o instanceof Boolean) {
            cell.setCellType(CellType.BOOLEAN);
            cell.setCellValue((Boolean) o);
            return CELL_OTHER;
        }
        // 如果是BigDecimal，则默认3位小数
        if (o instanceof BigDecimal) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(((BigDecimal) o).setScale(3, RoundingMode.HALF_UP).doubleValue());
            return CELL_OTHER;
        }
        // 如果是Date数据，则显示格式化数据
        if (o instanceof Date) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(formatDate((Date) o));
            return CELL_OTHER;
        }
        // 如果是其他，则默认字符串类型
        cell.setCellType(CellType.STRING);
        cell.setCellValue(o.toString());
        return CELL_OTHER;
    }

    /**
     * 如果是Date数据，则显示格式化数据
     *
     */
    private static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    /**
     * 合并当前Sheet页的单元格
     *
     * @param sheet      当前 sheet 页
     * @param mergeArray 合并单元格算法
     */
    private static void mergeCells(Sheet sheet, int[][] mergeArray) {
        // 横向合并
        for (int x = 0; x < mergeArray.length; x++) {
            int[] arr = mergeArray[x];
            boolean merge = false;
            int y1 = 0;
            int y2 = 0;
            for (int y = 0; y < arr.length; y++) {
                int value = arr[y];
                if (value == CELL_COLUMN_MERGE) {
                    if (!merge) {
                        y1 = y;
                    }
                    y2 = y;
                    merge = true;
                } else {
                    merge = false;
                    if (y1 > 0) {
                        sheet.addMergedRegion(new CellRangeAddress(x, x, (y1 - 1), y2));
                    }
                    y1 = 0;
                    y2 = 0;
                }
            }
            if (y1 > 0) {
                sheet.addMergedRegion(new CellRangeAddress(x, x, (y1 - 1), y2));
            }
        }
        // 纵向合并
        int xLen = mergeArray.length;
        int yLen = mergeArray[0].length;
        for (int y = 0; y < yLen; y++) {
            boolean merge = false;
            int x1 = 0;
            int x2 = 0;
            for (int x = 0; x < xLen; x++) {
                int value = mergeArray[x][y];
                if (value == CELL_ROW_MERGE) {
                    if (!merge) {
                        x1 = x;
                    }
                    x2 = x;
                    merge = true;
                } else {
                    merge = false;
                    if (x1 > 0) {
                        sheet.addMergedRegion(new CellRangeAddress((x1 - 1), x2, y, y));
                    }
                    x1 = 0;
                    x2 = 0;
                }
            }
            if (x1 > 0) {
                sheet.addMergedRegion(new CellRangeAddress((x1 - 1), x2, y, y));
            }
        }
    }

    /**
     * 跳转Excel下载
     *
     */
    private static void write(HttpServletResponse response, SXSSFWorkbook book, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String name = new String(fileName.getBytes("GBK"), "ISO8859_1") + XLSX;
        response.addHeader("Content-Disposition", "attachment;filename=" + name);
        ServletOutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
    }
}
