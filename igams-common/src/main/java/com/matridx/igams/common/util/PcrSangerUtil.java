package com.matridx.igams.common.util;

import com.matridx.igams.common.dao.entities.PictureFloat;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PcrSangerUtil {

//    public static void main(String[] args) throws IOException, URISyntaxException, CompoundNotFoundException {
//        //获取文件夹下所有文件夹List
//        PcrSangerUtil pcrSangerUtil = new PcrSangerUtil();
//        pcrSangerUtil.ExportWord("/matridx/pcrsanger/pcrsanger.zip","/matridx/pcrsanger/pcrsanger","/matridx/pcrsanger/1710154040486ZvmzvL.docx",
//                "PCR_SANGER_REPORT_DOC","/matridx/fileupload/temp/","/matridx/fileupload/release/");
//        String filePath = "/matridx/pcrsanger/YB0001W/YB0001W_C001_TSS20221201-0571-00067_G02.ab1";
//        String filePath = "/matridx/pcrsanger/YB0001W/3730.ab1";
//        File file = new File(filePath);
//        ABITrace tracer = new ABITrace(file);
//        BufferedImage image = tracer.getImage(200, 100);
//        int newWidth = 500;
//        int newHeight = image.getHeight();
//        // 创建一个新的BufferedImage对象，用于保存压缩后的图片
//        BufferedImage compressedImage = new BufferedImage(newWidth, newHeight, image.getType());
//
//        // 使用Graphics2D进行绘制，实现压缩效果
//        Graphics2D g2d = compressedImage.createGraphics();
//        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
//        g2d.dispose();
//
//        // 保存压缩后的图片
//        try {
//            ImageIO.write(compressedImage, "jpg", new File("/matridx/pcrsanger/YB0001W/output_compressed.jpg"));
//            System.out.println("Image compressed and saved successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error saving compressed image: " + e.getMessage());
//        }
//    }

    private Logger log = LoggerFactory.getLogger(PcrSangerUtil.class);
    public String ExportWord(String zipPath,String unZipPath,String templateFilePath,String ywlx,String tempFilePath,String releaseFilePath){
        String returnPath = "";
        String folderPath = ZipUtil.unZipFile(unZipPath, zipPath);
        String zipfilepath = System.currentTimeMillis() + RandomUtil.getRandomString();
        //获取文件夹下所有文件名称List
        File templateFile;
        List<Map<String,Object>> allFiles = getAllFiles(new File(folderPath));
        if (!CollectionUtils.isEmpty(allFiles)){
            FileInputStream fileInputStream = null;
            XWPFDocument document = null;
            OutputStream output = null;
            for (Map<String,Object> allFile : allFiles) {
                try {
                    Map<String,Object> map = new HashMap<>();
                    map.put("ybbh",allFile.get("ybbh").toString());//样本编号
                    File folder = new File(folderPath + "/" + allFile.get("ybbh").toString());
                    //读取模板文件
                    templateFile = new File(templateFilePath);
                    if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
                        log.error("PcrSangerUtil.ExportWord--------------模板不存在！请重新确认模板！");
                        return null;
                    }
                    //把模板文件放入流
                    fileInputStream = new FileInputStream(templateFile);
                    //读取模板文件word
                    document = new XWPFDocument(fileInputStream);
                    templateDeal(document,allFile);
                    fileInputStream.close();
                    String storePath = mkDirs(ywlx,tempFilePath,zipfilepath);
                    //输出流，设置替换后的文件名为：样本编码+".docx"
                    output = new FileOutputStream(storePath + "/" +  allFile.get("ybbh").toString() + ".docx");
                    //写入文件
                    document.write(output);
                    //关闭流
                    document.close();
                    output.close();
                    ZipUtil.toZip(storePath,storePath+".zip",true);
                    returnPath = storePath+".zip";
                } catch (FileNotFoundException e) {
                    log.error("PcrSangerUtil.ExportWord--------------模板不存在！请重新确认模板！");
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                        }
                        if (document != null) {
                            document.close();
                        }
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    } catch (Exception e) {
                        log.error(e.toString());
                    }
                }
            }
        }
        return returnPath;
    }

    /**
     * 模板替换
     * @param document
     * @return
     */
    public boolean templateDeal(XWPFDocument document,Map<String,Object> map) throws IOException{
        //读取引物编号顺序
        List<String> primerNubmerList = readPrimerNumberingSequence(document);
        //处理各个List顺序chu
        dealIndexOfList(primerNubmerList,map);
        // 1、替换段落中的循环内容（表格外，文字图片）
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        if (paragraphs != null && paragraphs.size() > 0) {
            getIterateIndexAndReplace(paragraphs, map ,false);
        }
        // 2、替换段落中普通内容（表格外，文字图片）
        List<XWPFParagraph> iterators = document.getParagraphs();
        for (int i = iterators.size()-1; i>-1; i--) {
            XWPFParagraph paragraph = iterators.get(i);
            replaceParagraph(paragraph, map);
        }
        // 3、替换表格中的循环内容
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext()) {
            XWPFTable table = (XWPFTable) itTable.next();
            // 替换表中的数据
            replaceTable(table, map);
        }
        return true;
    }

    /**
     * 读取引物编号顺序
     * @param document
     * @return
     */
    public List<String> readPrimerNumberingSequence(XWPFDocument document){
        List<String> primerNubmerList = new ArrayList<>();
        Iterator<XWPFTable> tableList = document.getTablesIterator();
        while (tableList.hasNext()) {
            XWPFTable table = (XWPFTable) tableList.next();
            List<XWPFTableRow> rows = table.getRows();
            //判断是否为引物编号顺序表
            if (rows.size() > 0 && rows.get(0).getCell(0).getText().contains("«PrimerNumber»")){
                //去除标记行
                table.removeRow(0);
                //循环遍历偶数行，获取顺序
                for (int i = 0; i < rows.size(); i++) {
                    if (i%2== 0){
                        continue;
                    }else {
                        List<XWPFTableCell> tableCells = rows.get(i).getTableCells();
                        for (XWPFTableCell tableCell : tableCells) {
                            if (StringUtil.isNotBlank(tableCell.getText())){
                                primerNubmerList.add(tableCell.getText());
                            }
                        }
                    }
                }
                break;
            }
        }
        return primerNubmerList;
    }

    /**
     * 处理各个List顺序
     * @param primerNubmerList
     * @param map
     * @return
     */
    public boolean dealIndexOfList(List<String> primerNubmerList,Map<String, Object> map){
        if (!CollectionUtils.isEmpty(primerNubmerList)){
            List<Map<String,Object>> newAb1List = new ArrayList<>();
            List<Map<String,Object>> newAb1PicList = new ArrayList<>();
            List<Map<String,Object>> newSeqList = new ArrayList<>();
            List<Map<String,Object>> newFileNameList = new ArrayList<>();
            List<String> oldAb1List = (List<String>) map.get("ab1List");
            List<String> oldAb1PicList = (List<String>) map.get("ab1PicList");
            List<String> oldSeqList = (List<String>) map.get("seqList");
            List<String> oldFileNameList = (List<String>) map.get("fileNameList");
            for (String name : primerNubmerList) {
                for (int i = oldAb1List.size() - 1; i >= 0; i--) {
                    String ab1 = oldAb1List.get(i);
                    if (ab1.contains(name)){
                        Map<String,Object> ab1Map = new HashMap<>();
                        ab1Map.put("value",ab1);
                        newAb1List.add(ab1Map);
                        oldAb1List.remove(ab1);
                        break;
                    }
                }
                for (int i = oldAb1PicList.size() - 1; i >= 0; i--) {
                    String ab1Pic = oldAb1PicList.get(i);
                    if (ab1Pic.contains(name)){
                        Map<String,Object> ab1PicMap = new HashMap<>();
                        String[] split = ab1Pic.substring(ab1Pic.lastIndexOf(File.separator) + 1).split("_");
                        String keyName = split[0]+"_"+split[1];
                        List<Map<String, Object>> collect = newAb1PicList.stream().filter(item -> item.get("name").equals(keyName)).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(collect)){
                            ab1PicMap = collect.get(0);
                            newAb1PicList.remove(collect.get(0));
                        }
                        ab1PicMap.put("name",keyName);
                        List<String> picList= new ArrayList<>();
                        if (ab1PicMap.get("value")!=null) {
                            picList = (List<String>) ab1PicMap.get("value");
                        }
                        picList.add(ab1Pic);
                        ab1PicMap.put("value",picList);
                        newAb1PicList.add(ab1PicMap);
                        oldAb1PicList.remove(ab1Pic);
                    }
                }
                if (!CollectionUtils.isEmpty(newAb1PicList)){
                    for (Map<String, Object> newAb1Pic : newAb1PicList) {
                        List<String> picList = (List<String>) newAb1Pic.get("value");
                        newAb1Pic.put("value",picList.stream().sorted(Comparator.comparing(String::toString)).collect(Collectors.toList()));
                    }
                }
                for (int i = oldSeqList.size() - 1; i >= 0; i--) {
                    String seq = oldSeqList.get(i);
                    if (seq.contains(name)){
                        Map<String,Object> seqMap = new HashMap<>();
                        seqMap.put("value",seq);
                        String[] split = seq.substring(seq.lastIndexOf(File.separator) + 1).split("_");
                        if (split[1].indexOf(".")>-1){
                            seqMap.put("name",split[0]+"_"+split[1].substring(0,split[1].lastIndexOf(".")));
                        }else {
                            seqMap.put("name",split[0]+"_"+split[1]);
                        }
                        newSeqList.add(seqMap);
                        oldSeqList.add(seq);
                        break;
                    }
                }
                for (int i = oldFileNameList.size() - 1; i >= 0; i--) {
                    String fileName = oldFileNameList.get(i);
                    if (fileName.contains(name)){
                        Map<String,Object> fileNameMap = new HashMap<>();
                        fileNameMap.put("value",fileName);
                        newFileNameList.add(fileNameMap);
                        oldFileNameList.add(fileName);
                        break;
                    }
                }
            }
            map.put("ab1List",newAb1List);
            map.put("ab1PicList",newAb1PicList);
            map.put("seqList",newSeqList);
            map.put("fileNameList",newFileNameList);
        }
        return true;
    }

    /**
     * 获取循环体的段落索引和run索引
     * @param map 变量map
     * @param isCell 是否是表格单元格
     */
    private void getIterateIndexAndReplace(Object object,Map<String, Object> map,boolean isCell) {
        List<XWPFParagraph> paragraphs = null;
        XWPFTableCell cell = null;
        if (isCell){
            cell = (XWPFTableCell) object;
            paragraphs = cell.getParagraphs();
        }else{
            paragraphs = (List<XWPFParagraph>) object;
        }
        for (int startParIndex = 0; startParIndex < paragraphs.size(); startParIndex++) {
            int iterateStartParIndex = -1;//循环开始段落索引
            int iterateStartRunIndex = -1;//循环开始run索引
            int iterateEndParIndex = -1;//循环结束段落索引
            int iterateEndRunIndex = -1;//循环结束run索引
            if (paragraphs.get(startParIndex) != null) {
                String pText = paragraphs.get(startParIndex).getText();
                if (pText.contains("«Iterate.")){
                    //若当前段落包含循环开始标签，则获取循环开始段落索引
                    iterateStartParIndex = startParIndex;
                    //在当前段落中获取循环开始run索引
                    iterateStartRunIndex = getRunIndexInPar(paragraphs.get(startParIndex), "«Iterate.");
                    //判断当前段落中是否包含循环结束标签
                    if (pText.contains("«/Iterate.")) {
                        //若当前段落包含循环结束标签，则说明循环体在同一个段落中，获取循环结束段落索引
                        iterateEndParIndex = startParIndex;
                    }else {
                        //若当前段落不包含循环结束标签，则说明循环体在多个段落中，从下一个段落开始去获取循环结束段落索引
                        for (int endParIndex = startParIndex+1; endParIndex < paragraphs.size(); endParIndex++) {
                            if (paragraphs.get(endParIndex) != null) {
                                String pTextNext = paragraphs.get(endParIndex).getText();
                                if (pTextNext.contains("«/Iterate.")) {
                                    //若当前段落包含循环结束标签，则说明循环体在同一个段落中，获取循环结束段落索引
                                    iterateEndParIndex = endParIndex;
                                    break;
                                }
                            }
                        }
                    }
                    //在当前段落中获取循环结束run索引
                    iterateEndRunIndex = getRunIndexInPar(paragraphs.get(iterateEndParIndex), "«/Iterate.");
                }
            }

            if (iterateStartParIndex != -1 && iterateEndParIndex != -1 && iterateStartRunIndex != -1 && iterateEndRunIndex != -1){
                XWPFParagraph startPar = paragraphs.get(iterateStartParIndex);
                String iterateName = startPar.getText();
                String iterateListName = iterateName.substring(iterateName.indexOf("«Iterate.")+9, iterateName.indexOf("»"));//循环变量名
                String iterateStartText = "«Iterate." + iterateListName + "»";//循环体开始标记
                String iterateEndText = "«/Iterate." + iterateListName + "»";//循环体结束标记
                String iterateJudge = "";//循环体标题中的判断条件
                if (iterateListName.indexOf("?")>-1){
                    iterateJudge = iterateListName.substring(iterateListName.indexOf("?")+1);
                    iterateListName = iterateListName.substring(0, iterateListName.indexOf("?"));
                }
                List<Object> iterateList = (List<Object>) map.get(iterateListName);//获取循环变量值
                if (StringUtil.isNotBlank(iterateJudge) && iterateList != null && iterateList.size()>0){
                    iterateList = getIterateList(iterateList,iterateJudge);
                }
                if (iterateList != null && iterateList.size()>0){
                    int listSize = iterateList.size();
                    if (iterateEndParIndex > iterateStartParIndex){
                        //循环体在多个段落中,多个段落一起循环
//						List<XWPFRun> prefixHalfParRunList = new ArrayList<>();//循环体开始段落中的run集合
                        List<XWPFParagraph> paragraphList = new ArrayList<>();//循环体段落集合
//						List<XWPFRun> suffixHalfParRunList = new ArrayList<>();//循环体结束段落中的run集合
                        //获取循环体段落
                        XWPFParagraph endPar = paragraphs.get(iterateEndParIndex);//循环体结束段落
//						XWPFRun startRun = startPar.getRuns().get(iterateStartRunIndex);//循环体开始run
//						int startRunSize = startPar.getRuns().size() - 1 - iterateStartRunIndex;//循环体开始段落中的run数量
//						XWPFRun endRun = endPar.getRuns().get(iterateEndRunIndex);//循环体结束run

                        //获取循环体段落
                        if (iterateEndParIndex-iterateStartParIndex>1){
                            for (int i = iterateStartParIndex+1; i < iterateEndParIndex; i++) {
                                paragraphList.add(paragraphs.get(i));
                            }
                        }
                        if (listSize>0) {
                            for (int i = 0; i < listSize; i++) {
                                Object o = iterateList.get(i);
                                int insertParIndex = iterateEndParIndex;
//								int insertRunIndex = iterateEndRunIndex;
                                if (paragraphList.size()>0){
                                    for (XWPFParagraph fromPar : paragraphList) {
                                        insertParIndex = insertParIndex + 1;
                                        XmlCursor xmlCursor = endPar.getCTP().newCursor();//在模板行最后一段处新加一个段落
                                        //在tmpPar之后插入新的段落
                                        XWPFParagraph toPar = null;
                                        if (isCell){
                                            toPar = cell.insertNewParagraph(xmlCursor);
                                        }else {
                                            XWPFDocument document = startPar.getDocument();
                                            toPar = document.insertNewParagraph(xmlCursor);
                                        }
                                        toPar = copyPar(fromPar,toPar);
                                        List<XWPFRun> fromRuns = fromPar.getRuns();
                                        if (fromRuns.size()>0){
                                            for (XWPFRun fromRun : fromRuns) {
                                                XWPFRun toRun = toPar.createRun();
                                                toRun = replaceIterateRun(toRun, fromRun, o, iterateStartText, iterateEndText, i);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //移除循环体模板
                        for (int i = iterateEndParIndex-1; i >= iterateStartParIndex; i--) {
                            if (isCell){
                                cell.removeParagraph(paragraphs.indexOf(paragraphs.get(i)));
                            }else {
                                XWPFDocument document = startPar.getDocument();
                                document.removeBodyElement(document.getPosOfParagraph(paragraphs.get(i)));
                            }
                        }
                        //移除循环体模板中«/Iterate.»部分
                        if (isCell){
                            cell.removeParagraph(paragraphs.indexOf(endPar));
                        }else {
                            XWPFDocument document = endPar.getDocument();
                            document.removeBodyElement(document.getPosOfParagraph(endPar));
                        }
                    }else {
                        //循环体在同一个段落中
                        //获取循环体段落中的run集合
                        List<XWPFRun> runList = new ArrayList<>();
                        XWPFRun beforeRun = startPar.getRuns().get(iterateStartRunIndex);
                        String beforeRunText = beforeRun.text();
                        String iterateInBefore = beforeRunText.substring(Math.min((beforeRunText.indexOf(iterateStartText) + iterateStartText.length() + 1), beforeRunText.length()));//循环前，循环体中的内容
                        String iterateOutBefore = beforeRunText.substring(0, beforeRunText.indexOf(iterateStartText));//循环前，循环体外的内容
                        beforeRun.setText(iterateOutBefore, 0);
                        if (StringUtil.isNotBlank(iterateInBefore)){
                            XWPFRun tmpBeforeRun = startPar.insertNewRun(iterateStartRunIndex+1);
                            tmpBeforeRun.getCTR().setRPr(beforeRun.getCTR().getRPr());
                            runList.add(tmpBeforeRun);
                        }
                        for (int i = iterateStartRunIndex+1; i < iterateEndRunIndex; i++) {
                            runList.add(startPar.getRuns().get(i));
                        }
                        XWPFRun afterRun = startPar.getRuns().get(iterateEndRunIndex);
                        String afterRunText = afterRun.text();
                        if(afterRunText.indexOf(iterateEndText) >0) {
                            String iterateInAfter = afterRunText.substring(0, afterRunText.indexOf(iterateEndText));//循环后，循环体中的内容
                            if (StringUtil.isNotBlank(iterateInAfter)){
                                XWPFRun tmpBeforeRun = startPar.insertNewRun(iterateEndRunIndex);
                                tmpBeforeRun.getCTR().setRPr(beforeRun.getCTR().getRPr());
                                runList.add(tmpBeforeRun);
                            }
                        }
                        String iterateOutAfter = afterRunText.substring(Math.min((afterRunText.indexOf(iterateEndText) + iterateEndText.length() + 1), afterRunText.length()));//循环后，循环体外的内容
                        afterRun.setText(iterateOutAfter, 0);

                        for (int i = listSize-1; i >=  0; i--) {
                            Object o = iterateList.get(i);
                            int insertRunIndex = iterateStartRunIndex+runList.size();
                            for (XWPFRun fromRun : runList) {
                                insertRunIndex = insertRunIndex + 1;
                                XWPFRun toRun = startPar.insertNewRun(insertRunIndex);
                                toRun = replaceIterateRun(toRun, fromRun, o, iterateStartText, iterateEndText, i);
                            }
                        }
                        //删除模版run
                        for (int i = iterateEndRunIndex-1; i > iterateStartRunIndex; i--) {
                            startPar.removeRun(i);
                        }
                    }
                }else{
                    //移除循环体模板
                    for (int i = iterateEndParIndex-1; i >= iterateStartParIndex; i--) {
                        if (isCell){
                            cell.removeParagraph(paragraphs.indexOf(paragraphs.get(i)));
                        }else {
                            XWPFDocument document = startPar.getDocument();
                            document.removeBodyElement(document.getPosOfParagraph(paragraphs.get(i)));
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据条件筛选list
     * @param list
     * @param judgeText
     * @return
     */
    private List<Object> getIterateList(List<Object> list,String judgeText){
        List<Object> iterateList = new ArrayList<>();
        if (judgeText.indexOf("==") > -1){
            String[] split = judgeText.split("==");
            if (split.length == 2){
                String key = split[0];
                String okValue= split[1];
                for (Object o : list) {
                    try {
                        Method method = o.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
                        String value = (String) method.invoke(o);
                        if (okValue.equals(value)){
                            iterateList.add(o);
                        }
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        log.error("get" + key.substring(0, 1).toUpperCase() + key.substring(1)+"方法不存在或为获取到内容");
                    }
                }
            }
        } else if (judgeText.indexOf("!=") >-1) {
            String[] split = judgeText.split("!=");
            if (split.length == 2){
                String key = split[0];
                String notokValue= split[1];
                for (Object o : list) {
                    try {
                        Method method = o.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
                        String value = (String) method.invoke(o);
                        if (!notokValue.equals(value)){
                            iterateList.add(o);
                        }
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        log.error("get" + key.substring(0, 1).toUpperCase() + key.substring(1)+"方法不存在或为获取到内容");
                    }
                }
            }
        } else {
            for (Object o : list) {
                try {
                    Method method = o.getClass().getMethod("get" + judgeText.substring(0, 1).toUpperCase() + judgeText.substring(1));
                    String value = (String) method.invoke(o);
                    if (StringUtil.isNotBlank(value)){
                        iterateList.add(o);
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    log.error("get" + judgeText.substring(0, 1).toUpperCase() + judgeText.substring(1)+"方法不存在或为获取到内容");
                }
            }
        }
        return iterateList;
    }

    /**
     * 获取段落中指定字符串的Run索引
     * @param paragraph 段落
     * @param text 指定字符串
     * @return run索引
     */
    private int getRunIndexInPar(XWPFParagraph paragraph, String text) {
        int runIndex = -1;
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs != null && runs.size() > 0) {
            for (int i = 0; i < runs.size(); i++) {
                if (runs.get(i) != null && runs.get(i).text().contains(text)) {
                    runIndex = i;
                    return runIndex;
                }
            }
        }
        return runIndex;
    }

    /**
     * 复制一行
     * @param table
     * @param sourceRow
     * @param rowIndex
     * @return
     */
    public XWPFTableRow copy(XWPFTable table,XWPFTableRow sourceRow,int rowIndex){
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
                for (int i = 0; i < sourceCell.getParagraphs().size(); i++) {
                    if (targetCell.getParagraphs().size()<sourceCell.getParagraphs().size()){
                        targetCell.addParagraph();
                    }
                    targetCell.getParagraphs().get(i).getCTP().setPPr(sourceCell.getParagraphs().get(i).getCTP().getPPr());
                    if(sourceCell.getParagraphs().get(i).getRuns()!=null&&sourceCell.getParagraphs().get(i).getRuns().size()>0){
                        for (int j = 0; j < sourceCell.getParagraphs().get(i).getRuns().size(); j++) {
                            XWPFRun cellR = targetCell.getParagraphs().get(i).createRun();
                            cellR.setText(sourceCell.getParagraphs().get(i).getRuns().get(j).text());
                            cellR.setBold(sourceCell.getParagraphs().get(i).getRuns().get(j).isBold());
                            cellR.getCTR().setRPr(sourceCell.getParagraphs().get(i).getRuns().get(j).getCTR().getRPr());
                            if ( null != sourceCell.getParagraphs().get(i).getRuns() && sourceCell.getParagraphs().get(i).getRuns().size()>0){
                                if (StringUtil.isNotBlank(sourceCell.getParagraphs().get(i).getRuns().get(j).getFontFamily())){
                                    cellR.setFontFamily(sourceCell.getParagraphs().get(i).getRuns().get(j).getFontFamily());
                                }
                            }
                        }
                    }else{
                        targetCell.setText(sourceCell.getText());
                    }
                }
            }else{
                targetCell.setText(sourceCell.getText());
            }
        }
        return targetRow;
    }

    /**
     * 复制run
     * @param fromRun 从哪个run复制
     * @param toRun 复制到哪个run
     * @return
     */
    private XWPFRun copyRun(XWPFRun fromRun, XWPFRun toRun) {
        toRun.getCTR().setRPr(fromRun.getCTR().getRPr());
        return toRun;
    }

    private XWPFParagraph copyPar(XWPFParagraph fromPar, XWPFParagraph toPar) {
        toPar.getCTP().setPPr(fromPar.getCTP().getPPr());
        return toPar;
    }

    private XWPFRun replaceIterateRun(XWPFRun toRun,XWPFRun fromRun,Object o,String iterateStartText,String iterateEndText,int i) {
        toRun = copyRun(fromRun, toRun);
        String fromText = fromRun.text();
        while (fromText.contains("«list.")){
            if (fromText.contains("«list.pic")){
                String objName = fromText.substring(fromText.indexOf("«list.pic")+9, fromText.indexOf("»"));
                String replaceText = fromText.substring(fromText.indexOf("«list.pic"), fromText.indexOf("»")+1);
                int h = 0;
                int w = 550;
                if (objName.indexOf(".height")>-1){
                    objName = objName.substring(objName.indexOf(".height")+7);
                    h = Integer.parseInt(objName.substring(0, objName.indexOf(".")));
                }else if (objName.indexOf(".width")>-1){
                    objName = objName.substring(objName.indexOf(".width")+6);
                    w = Integer.parseInt(objName.substring(0, objName.indexOf(".")));
                }
                objName = objName.substring(objName.indexOf(".")+1);
                //反射方法 把对象中的value插入到数据中
                Object value = null;
                if ("java.util.HashMap".equals(o.getClass().getName())){
                    try {
                        value = ((Map<String, Object>) o).get(objName);

                    } catch (Exception e) {
                        log.error("对象中没有get"+objName+"方法");
                    }
                } else {
                    String totmName = objName.substring(0,1).toUpperCase()+objName.substring(1);
                    try {
                        Method method = o.getClass().getMethod("get" + totmName);
                        value = (String) method.invoke(o);
                    } catch (Exception e) {
                        log.error("对象中没有get"+totmName+"方法");
                    }
                }
                if (value != null){
                    if (value instanceof String){
                        String valueStr = (String) ((Map<String,Object>) o).get(objName);
                        if (StringUtil.isNotBlank(valueStr)){
                            try {
                                List<PictureFloat> list = new ArrayList<>();
                                File picture = new File(valueStr);
                                BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                                //获取图片长宽，以及长宽的比列
                                int height = sourceImg.getHeight();
                                int width = sourceImg.getWidth();
                                BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
                                if (h>0){
                                    height = h;
                                    width = divide.multiply(new BigDecimal(height)).intValue();
                                }
                                if (w>0){
                                    width = w;
                                    height = new BigDecimal(width).divide(divide, 2, RoundingMode.HALF_EVEN).intValue();
                                }
                                PictureFloat imageFloat=new PictureFloat(valueStr, width, height, 0, 0);
                                list.add(imageFloat);
                                overlayPictures(toRun,list);
                            } catch (IOException e) {
                                log.error("图片替换失败"+e.getMessage());
                            }
                        }
                        fromText = fromText.replace(replaceText, "");
                    } else if (value instanceof List) {
                        List<String> valueList = (List<String>) ((Map<String,Object>) o).get(objName);
                        for (String valueStr : valueList) {
                            if (StringUtil.isNotBlank(valueStr)){
                                try {
                                    List<PictureFloat> list = new ArrayList<>();
                                    File picture = new File(valueStr);
                                    BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                                    //获取图片长宽，以及长宽的比列
                                    int height = sourceImg.getHeight();
                                    int width = sourceImg.getWidth();
                                    BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
                                    if (h>0){
                                        height = h;
                                        width = divide.multiply(new BigDecimal(height)).intValue();
                                    }
                                    if (w>0){
                                        width = w;
                                        height = new BigDecimal(width).divide(divide, 2, RoundingMode.HALF_EVEN).intValue();
                                    }
                                    PictureFloat imageFloat=new PictureFloat(valueStr, width, height, 0, 0);
                                    list.add(imageFloat);
                                    overlayPictures(toRun,list);
                                } catch (IOException e) {
                                    log.error("图片替换失败"+e.getMessage());
                                }
                            }
                        }
                        fromText = fromText.replace(replaceText, "");
                    }
                }


            }else if (fromText.contains("«list.file")){
                String objName = fromText.substring(fromText.indexOf("«list.file")+10, fromText.indexOf("»"));
                String replaceText = fromText.substring(fromText.indexOf("«list.file"), fromText.indexOf("»")+1);
                objName = objName.substring(objName.indexOf(".")+1);
                //反射方法 把对象中的value插入到数据中
                String value = "";
                if ("java.util.HashMap".equals(o.getClass().getName())){
                    try {
                        value = (String) ((Map<String,Object>) o).get(objName);
                    } catch (Exception e) {
                        log.error("对象中没有get"+objName+"方法");
                    }
                } else {
                    String totmName = objName.substring(0,1).toUpperCase()+objName.substring(1);
                    try {
                        Method method = o.getClass().getMethod("get" + totmName);
                        value = (String) method.invoke(o);
                    } catch (Exception e) {
                        log.error("对象中没有get"+totmName+"方法");
                    }
                }
            }else if (fromText.contains("«list.")){
                String objName = fromText.substring(fromText.indexOf("«list.")+6, fromText.indexOf("»"));
                //反射方法 把对象中的value插入到数据中
                String value = "";
                if (objName.indexOf("?")>-1 && objName.indexOf(":")>-1){
                    String judgeText = objName.substring(0, objName.indexOf("?"));
                    String trueValue = objName.substring(objName.indexOf("?")+1, objName.indexOf(":"));
                    String falseValue = objName.indexOf(":")<objName.length()?objName.substring(objName.indexOf(":")+1):"";
//					String endText = fromText.substring(fromText.indexOf("»")+1);
                    if (judgeText.indexOf("==") > -1) {
                        String[] split = judgeText.split("==");
                        if (split.length == 2) {
                            try {
                                Method method = o.getClass().getMethod("get" + split[0].substring(0, 1).toUpperCase() + split[0].substring(1));
                                value = (String) method.invoke(o);
                            } catch (Exception e) {
                                log.error("对象中没有get" + split[0].substring(0, 1).toUpperCase() + split[0].substring(1) + "方法");
                            }
                            if (value.equals(split[1])) {
                                value = trueValue;
                            } else {
                                value = falseValue;
                            }
                        }
                    } else if (judgeText.indexOf("!=") > -1) {
                        String[] split = judgeText.split("!=");
                        if (split.length == 2) {
                            try {
                                Method method = o.getClass().getMethod("get" + split[0].substring(0, 1).toUpperCase() + split[0].substring(1));
                                value = (String) method.invoke(o);
                            } catch (Exception e) {
                                log.error("对象中没有get" + split[0].substring(0, 1).toUpperCase() + split[0].substring(1) + "方法");
                            }
                            if (!value.equals(split[1])) {
                                value = trueValue;
                            } else {
                                value = falseValue;
                            }
                        }
                    } else {
                        try {
                            Method method = o.getClass().getMethod("get" + judgeText.substring(0, 1).toUpperCase() + judgeText.substring(1));
                            value = (String) method.invoke(o);
                        } catch (Exception e) {
                            log.error("对象中没有get" + judgeText.substring(0, 1).toUpperCase() + judgeText.substring(1) + "方法");
                        }
                        if (StringUtil.isNotBlank(value)) {
                            value = trueValue;
                        } else {
                            value = falseValue;
                        }
                    }
                    fromText = fromText.replace("«list."+objName+"»","«"+ value+"»");
                    continue;
                }
                String totmName = objName.substring(0,1).toUpperCase()+objName.substring(1);

                if (("indexNum".equals(objName) || "indexNumCircle".equals(objName) || "indexNumRoman".equals(objName) || "indexNumChinese".equals(objName)) && StringUtil.isBlank(value)){
                    value = String.valueOf(i+1);
                    value = replaceIndexNum(value,objName);
                }else {
                    try {
                        String addNum = null;
                        String subNum = null;
                        if (totmName.indexOf("+")>-1){
                            addNum = totmName.substring(totmName.indexOf("+")+1);
                            totmName = totmName.substring(0,totmName.indexOf("+"));
                        }
                        if (totmName.indexOf("-")>-1){
                            subNum = totmName.substring(totmName.indexOf("+")+1);
                            totmName = totmName.substring(0,totmName.indexOf("+"));
                        }
                        Method method = o.getClass().getMethod("get" + totmName);
                        value = (String) method.invoke(o);
                        if (StringUtil.isNotBlank(value) && StringUtil.isNotBlank(addNum)){
                            BigDecimal bigDecimalvalue = new BigDecimal(value);
                            BigDecimal bigDecimaladd = new BigDecimal(addNum);
                            BigDecimal endvalue = bigDecimalvalue.add(bigDecimaladd);
                            value = endvalue.toString();
                        }
                        if (StringUtil.isNotBlank(value) && StringUtil.isNotBlank(subNum)){
                            BigDecimal bigDecimalvalue = new BigDecimal(value);
                            BigDecimal bigDecimalsub = new BigDecimal(subNum);
                            BigDecimal endvalue = bigDecimalvalue.subtract(bigDecimalsub);
                            value = endvalue.toString();
                        }
                    } catch (Exception e) {
                        log.error("对象中没有get"+totmName+"方法");
                    }
                }
                fromText = fromText.replace("«list."+objName+"»", value);
            } else if (fromText.contains(iterateStartText)) {
                fromText = fromText.replace(iterateStartText, "");
            } else if (fromText.contains(iterateEndText)) {
                fromText = fromText.replace(iterateEndText, "");
            } else {
                toRun.setText(fromText);
            }
        }
        toRun.setText(fromText);
        return toRun;
    }

    /**
     * 转换数字格式
     * @param num
     * @param type
     * indexNumCircle 1-20转换成①-⑳
     * indexNumRoman 1-3999转换成罗马数字
     * indexNumChinese 1-10000转换成中文数字
     * @return
     */
    public String replaceIndexNum(String num, String type){
        if ("indexNumCircle".equals(type)){
            //将序号转换成特殊符号数字，将1-9转换成①-⑨
            if ("1".equals(num)){
                num = num.replace("1","①");
            } else if ("2".equals(num)) {
                num = num.replace("2","②");
            } else if ("3".equals(num)) {
                num = num.replace("3","③");
            } else if ("4".equals(num)) {
                num = num.replace("4","④");
            } else if ("5".equals(num)) {
                num = num.replace("5","⑤");
            } else if ("6".equals(num)) {
                num = num.replace("6","⑥");
            } else if ("7".equals(num)) {
                num = num.replace("7","⑦");
            } else if ("8".equals(num)) {
                num = num.replace("8","⑧");
            } else if ("9".equals(num)) {
                num = num.replace("9","⑨");
            } else if ("10".equals(num)) {
                num = num.replace("10","⑩");
            } else if ("11".equals(num)) {
                num = num.replace("11","⑪");
            } else if ("12".equals(num)) {
                num = num.replace("12","⑫");
            } else if ("13".equals(num)) {
                num = num.replace("13","⑬");
            } else if ("14".equals(num)) {
                num = num.replace("14","⑭");
            } else if ("15".equals(num)) {
                num = num.replace("15","⑮");
            } else if ("16".equals(num)) {
                num = num.replace("16","⑯");
            } else if ("17".equals(num)) {
                num = num.replace("17","⑰");
            } else if ("18".equals(num)) {
                num = num.replace("18","⑱");
            } else if ("19".equals(num)) {
                num = num.replace("19","⑲");
            } else if ("20".equals(num)) {
                num = num.replace("20","⑳");
            }
        } else if ("indexNumRoman".equals(type)) {
            //将序号转换成罗马数字
            int romanNum = Integer.parseInt(num);
            if (romanNum >= 1 && romanNum <= 3999) {
                int[] DIGITS = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
                String[] ROMAN_NAMES = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
                StringBuilder result = new StringBuilder();
                int lastIndex = 0;
                for (int i = 0; i < DIGITS.length; i++) {
                    int currentIndex = Math.max(lastIndex, i);
                    while (romanNum >= DIGITS[currentIndex]) {
                        result.append(ROMAN_NAMES[currentIndex]);
                        romanNum -= DIGITS[currentIndex];
                        lastIndex = currentIndex;
                    }
                }
                num = result.reverse().toString();
            }
        } else if ("indexNumChinese".equals(type)) {
            String[] NUMBER_LIST = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
            String[] SYMBOLS = {"十", "百", "千", "万"};
            int number = Integer.parseInt(num);
            if (number >= 0 && number <= 10000) {
                StringBuilder result = new StringBuilder();
                // 判断数字的位数
                if (number == 0) {
                    result.append("零");
                } else if (number < 0) {
                    result.append("负");
                    number = -number;
                } else if (number >= 10000) {
                    int i = 0;
                    while (number >= 1000) {
                        result.append(SYMBOLS[i++]);
                        number -= 1000;
                    }
                    result.append(NUMBER_LIST[i]);
                } else if (number >= 100) {
                    int i = 0;
                    while (number >= 100) {
                        result.append(SYMBOLS[i++]);
                        number -= 100;
                    }
                    result.append(NUMBER_LIST[i]);
                } else if (number >= 10) {
                    int i = 0;
                    while (number >= 10) {
                        result.append(SYMBOLS[i++]);
                        number -= 10;
                    }
                    result.append(NUMBER_LIST[i]);
                } else {
                    result.append(NUMBER_LIST[number]);
                }
                num = result.reverse().toString();
            }
        }
        return num;
    }

    /**
     * 替换图片、文字（包括图片,«»中包含Pic即可）
     * 若其中包含leftBreak或者allBreak,则在该图片前换行
     * 若其中包含rightBreak或者allBreak,则在该图片后换行
     * 若其中包含qrCode，则生成防伪码
     */
    private void replaceParagraph(XWPFParagraph paragraph,Map<String,Object> map) throws IOException {
        List<XWPFRun> runs = paragraph.getRuns();
        for (int q = 0; runs != null && q < runs.size(); q++) {
            String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
            if (StringUtil.isBlank(oneparaString)) {
                continue;
            }
            int startTagPos = oneparaString.indexOf("«");
            int endTagPos = oneparaString.indexOf("»");
            if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
                if (StringUtil.isNotBlank(varString)) {
                    if (varString.contains("ybbh_Pic")){
                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                        List<PictureFloat> list=new ArrayList<>();
                        Object imaget = map.get(varString);
                        int picHeight = 250;
                        int picWidth = 350;
                        if (imaget!=null) {
                            String imagetFile =imaget.toString();
                            File picture = new File(imagetFile);
                            BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                            //获取图片长宽，以及长宽的比列
                            int height = sourceImg.getHeight();
                            int width = sourceImg.getWidth();
                            BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
                            int imgWidth = new BigDecimal(picHeight).multiply(divide).intValue();
                            int imgHeight = picHeight;
                            if (imgWidth > picWidth){
                                imgWidth = picWidth;
                                imgHeight = new BigDecimal(imgWidth).divide(divide, 2, RoundingMode.HALF_UP).intValue();
                            }
                            PictureFloat imageFloat=new PictureFloat(imagetFile, imgWidth, imgHeight, 0, 0);
                            list.add(imageFloat);
                            overlayPictures(runs.get(q),list);
                        }
                        runs.get(q).setText(oneparaString, 0);
                    } else if (varString.contains("Pic") && !varString.contains("ybbh_Pic")){
                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                        List<PictureFloat> list=new ArrayList<>();
                        Object imaget = map.get(varString);
                        int picHeight = 250;
                        if (imaget!=null) {
                            String imagetFile =imaget.toString();
                            File picture = new File(imagetFile);
                            BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                            //获取图片长宽，以及长宽的比列
                            int height = sourceImg.getHeight();
                            int width = sourceImg.getWidth();
                            BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
                            int imgWidth = new BigDecimal(picHeight).multiply(divide).intValue();
                            PictureFloat imageFloat=new PictureFloat(imagetFile, imgWidth, picHeight, 0, 0);
                            list.add(imageFloat);
                            overlayPictures(runs.get(q),list);
                        }
                        runs.get(q).setText(oneparaString, 0);
                    } else{
                        Object object_value = map.get(varString);
                        if (object_value instanceof String) {
                            String rep_value = (String) object_value;
                            // 如果找不到相应的替换问题，则替换成空字符串
                            if (StringUtil.isBlank(rep_value)) {
                                oneparaString = oneparaString.replace("«" + varString + "»", "");
                            } else {
                                oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
                            }
                            runs.get(q).setText(oneparaString, 0);
                        } else {
                            oneparaString = oneparaString.replace("«" + varString + "»", "");
                            runs.get(q).setText(oneparaString, 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 替换表格中的信息
     * @param table   //当前迭代的表格
     * @param map //需要替换的数据
     */
    private void replaceTable(XWPFTable table, Map<String, Object> map) throws IOException {
        List<XWPFTableRow> rows = table.getRows();
        for (int i = rows.size() - 1; i >= 0; i--) {
            List<XWPFTableCell> tableCells = rows.get(i).getTableCells();
            for (int j = 0; j < tableCells.size(); j++) {
                if (tableCells.get(j)!=null && StringUtil.isNotBlank(tableCells.get(j).getText())){
                    String cellText = tableCells.get(j).getText();
                    if (cellText != null && cellText.indexOf("«")>-1){
                        if (cellText.contains("«fileNameList.")){
                            //循环替换
                            //获取当前行
                            List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("fileNameList");
                            int copySize = list.size();
                            for (int a = 0; a < copySize; a++) {
                                //复制模板行
                                XWPFTableRow row = copy(table, rows.get(i), table.getRows().size());
                                //替换内容
                                replaceTableRow(row,list.get(a));
                            }
                            //合并第一列
                            if (copySize>1){
                                rows.get(i+1).getCell(0).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
                                for (int a = 1; a < copySize; a++) {
                                    rows.get(i+1+a).getCell(0).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
                                }
                            }
                            //删除模板行
                            table.removeRow(i);
                            break;
                        } else if (cellText.contains("«seqList.")) {
                            //循环替换
                            //获取当前行
                            List<Map<String,Object>> list = (List<Map<String, Object>>) map.get("seqList");
                            int copySize = list.size();
                            for (int a = 0; a < copySize; a++) {
                                //复制模板行
                                XWPFTableRow row = copy(table, rows.get(i), table.getRows().size());
                                //替换内容
                                replaceTableRow(row,list.get(a));
                            }
                            //删除模板行
                            table.removeRow(i);
                            break;
                        } else {
                            //普通替换
                            List<XWPFParagraph> paragraphs = tableCells.get(j).getParagraphs();
                            for (XWPFParagraph paragraph : paragraphs) {
                                replaceParagraph(paragraph,map);
;                            }
                        }
                    }
                }
            }
        }
    }

    public void replaceTableRow(XWPFTableRow row,Map<String,Object> map) throws IOException {
        List<XWPFTableCell> cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {
            String cellText = cell.getText();
            if (StringUtil.isNotBlank(cellText) && cellText.contains("«")) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (int j = 0; runs != null && j < runs.size(); j++) {
                        String oneparaString = runs.get(j).getText(runs.get(j).getTextPosition());
                        if (StringUtil.isBlank(oneparaString)) {
                            continue;
                        }
                        int startTagPos = oneparaString.indexOf("«");
                        int endTagPos = oneparaString.indexOf("»");
                        if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
                            String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
                            String[] split = varString.split("\\.");
                            if (StringUtil.isNotBlank(varString)) {
                                if (varString.contains(".file.")){
                                    Object object_value = map.get(split[split.length-1]);
                                    String rep_value = (String) object_value;
                                    File rep_file = new File(rep_value);
                                    StringBuilder contentBuilder = new StringBuilder();
                                    //读取rep_file文件内容
                                    if (rep_file.exists()) {
                                        Files.lines(Paths.get(rep_value)).forEach(contentBuilder::append);
                                    }
                                    rep_value = contentBuilder.toString();
                                    // 如果找不到相应的替换问题，则替换成空字符串
                                    if (StringUtil.isBlank(rep_value)) {
                                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                                    } else {
                                        oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
                                    }
                                    runs.get(j).setText(oneparaString, 0);
                                } else{
                                    Object object_value = map.get(split[split.length-1]);
                                    if (object_value instanceof String) {
                                        String rep_value = (String) object_value;
                                        // 如果找不到相应的替换问题，则替换成空字符串
                                        if (StringUtil.isBlank(rep_value)) {
                                            oneparaString = oneparaString.replace("«" + varString + "»", "");
                                        } else {
                                            oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
                                        }
                                        runs.get(j).setText(oneparaString, 0);
                                    } else {
                                        oneparaString = oneparaString.replace("«" + varString + "»", "");
                                        runs.get(j).setText(oneparaString, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 图片叠加方法(png格式，第一张为背景图可以传透明图片，从第二张开始浮动)
     * @param run
     * @param picList
     * @return
     */
    public boolean overlayPictures(XWPFRun run, List<PictureFloat> picList) {
        // 图片路径
        FileInputStream image = null;
        try {
            if(picList != null && picList.size() > 0) {
                run.setText("",0);
                for (int i = 0; i < picList.size(); i++) {
                    PictureFloat pictureFloat = picList.get(i);
                    image = new FileInputStream(pictureFloat.getWjlj());
                    run.addPicture(image, XWPFDocument.PICTURE_TYPE_PNG, pictureFloat.getWjlj(), Units.toEMU(pictureFloat.getWidth()), Units.toEMU(pictureFloat.getHeight()));
                    if(i > 0) {
                        imageFloatDeal(run, i, pictureFloat.getWidth(),
                                pictureFloat.getHeight(), pictureFloat.getLeft(), pictureFloat.getTop());
                    }
                    image.close();
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            try {
                if(image != null) {
                    image.close();
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
        return false;
    }

    /**图片浮动处理
     * @param run
     * @param i
     * @param width
     * @param height
     * @param left
     * @param top
     * @throws XmlException
     */
    private void imageFloatDeal(XWPFRun run, int i, int width, int height, int left, int top) throws XmlException {
        // 获取到浮动图片数据
        CTDrawing mid_drawing = run.getCTR().getDrawingArray(i);
        CTGraphicalObject mid_graphicalobject = mid_drawing.getInlineArray(0).getGraphic();
        // 拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor mid_anchor = getAnchorWithGraphic(mid_graphicalobject, "mid",
                Units.toEMU(width), Units.toEMU(height),//图片大小
                Units.toEMU(left), Units.toEMU(top), false);//相对当前段落位置 需要计算段落已有内容的左偏移
        mid_drawing.setAnchorArray(new CTAnchor[]{mid_anchor});//添加浮动属性
        mid_drawing.removeInline(0);//删除行内属性
    }

    /**word添加图片
     * @param ctGraphicalObject 图片数据
     * @param deskFileName      图片描述
     * @param width             宽
     * @param height            高
     * @param leftOffset        水平偏移 left
     * @param topOffset         垂直偏移 top
     * @param behind            文字上方，文字下方
     * @return
     * @throws XmlException
     * @throws Exception
     */
    private CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject,
                                          String deskFileName, int width, int height,
                                          int leftOffset, int topOffset, boolean behind) throws XmlException {
        String anchorXML = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                + "<wp:simplePos x=\"0\" y=\"0\"/>"
                + "<wp:positionH relativeFrom=\"column\">"
                + "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
                + "</wp:positionH>"
                + "<wp:positionV relativeFrom=\"paragraph\">"
                + "<wp:posOffset>" + topOffset + "</wp:posOffset>"
                + "</wp:positionV>"
                + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                + "<wp:wrapNone/>"
                + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + deskFileName + "\"/><wp:cNvGraphicFramePr/>"
                + "</wp:anchor>";
        CTDrawing drawing = null;
        drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(ctGraphicalObject);
        return anchor;
    }

    /**
     * 获取文件夹下所有文件夹名称（样本编号）
     * @param folder
     * @return
     */
    public List<Map<String,Object>> getAllFiles(File folder) {
        List<Map<String,Object>> list = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) {     // 如果文件夹为空
            return null;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                if ("IMG".equals(fileName)){
                    //IMG文件夹下
                    dealZipFile(file,list,"ab1PicList");
                } else if ("SEQ".equals(fileName)) {
                    //SEQ文件夹下
                    dealZipFile(file,list,"seqList");
                } else if ("AB1".equals(fileName)) {
                    //AB1文件夹下
                    dealZipFile(file,list,"ab1List");
                } else if (fileName.endsWith(".png")){
                    //.png
                    dealZipFile(file,list,"ab1PicList");
                } else if (fileName.endsWith(".seq")) {
                    //.seq
                    dealZipFile(file,list,"seqList");
                } else if (fileName.endsWith(".ab1")) {
                    //.ab1
                    dealZipFile(file,list,"ab1List");
                } else {
                    Map<String,Object> map = new HashMap<>();
                    List<Map<String, Object>> collect = list.stream().filter(item -> item.get("ybbh").equals(fileName)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)){
                        map = collect.get(0);
                        list.remove(collect.get(0));
                    }
                    map.put("ybbh",fileName);
                    File[] childFiles = file.listFiles();
                    List<String> ab1List = new ArrayList<>();
                    if (map.get("ab1List")!=null) {
                        ab1List = (List<String>) map.get("ab1List");
                    }
                    List<String> ab1PicList = new ArrayList<>();
                    if (map.get("ab1PicList")!=null) {
                        ab1PicList = (List<String>) map.get("ab1PicList");
                    }
                    List<String> seqList = new ArrayList<>();
                    if (map.get("seqList")!=null) {
                        seqList = (List<String>) map.get("seqList");
                    }
                    if (childFiles!=null && childFiles.length>0){
                        for (File childFile : childFiles) {
                            String childFileName = childFile.getName();
                            if (childFileName.endsWith(".jpg")){
                                map.put("pic_name",childFileName.substring(0,childFileName.lastIndexOf(".")));
                                map.put("ybbh_Pic",childFile.getAbsolutePath());
                            } else if (childFileName.endsWith(".ab1")) {
                                ab1List.add(childFile.getAbsolutePath());
                            } else if (childFileName.endsWith(".png")) {
                                ab1PicList.add(childFile.getAbsolutePath());
                            } else if (childFileName.endsWith(".seq")) {
                                seqList.add(childFile.getAbsolutePath());
                            }
                        }
                    }
                    map.put("ab1List",ab1List);
                    map.put("seqList",seqList);
                    map.put("ab1PicList",ab1PicList);
                    // 如果是子文件夹，则递归调用
                    list.add(map);
                }
            } else {
                Map<String,Object> map = new HashMap<>();
                if (StringUtil.isNotBlank(fileName)){
                    String ybbh = fileName.split("_")[0];
                    List<Map<String, Object>> collect = list.stream().filter(item -> item.get("ybbh").equals(ybbh)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)){
                        map = collect.get(0);
                        list.remove(collect.get(0));
                    }
                    map.put("ybbh",ybbh);
                    if (fileName.endsWith(".ab1")){
                        List<String> ab1List = new ArrayList<>();
                        if (map.get("ab1List")!=null) {
                            ab1List = (List<String>) map.get("ab1List");
                        }
                        ab1List.add(file.getAbsolutePath());
                        map.put("ab1List",ab1List);
                    }else if (fileName.endsWith(".png")) {
                        List<String> ab1PicList = new ArrayList<>();
                        if (map.get("ab1List")!=null) {
                            ab1PicList = (List<String>) map.get("ab1PicList");
                        }
                        ab1PicList.add(file.getAbsolutePath());
                        map.put("ab1PicList",ab1PicList);
                    } else if (fileName.endsWith(".seq")) {
                        List<String> seqList = new ArrayList<>();
                        if (map.get("seqList")!=null) {
                            seqList = (List<String>) map.get("seqList");
                        }
                        seqList.add(file.getAbsolutePath());
                        map.put("seqList",seqList);
                    }
                    list.add(map);
                }
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            List<String> seqList = (List<String>) list.get(i).get("seqList");
            List<String> fileNameList = new ArrayList<>();
            for (String s : seqList) {
                fileNameList.add(s.substring(s.lastIndexOf(File.separator)+1,s.lastIndexOf(".")));
            }
            list.get(i).put("fileNameList",fileNameList);
        }
        return list;
    }

    public void dealZipFile(File file,List<Map<String,Object>> list,String key){
        if (file.isDirectory()){
            File[] childFiles = file.listFiles();
            for (File childFile : childFiles) {
                String ybbh = childFile.getName().split("_")[0];
                Map<String,Object> map = new HashMap<>();
                List<Map<String, Object>> collect = list.stream().filter(item -> item.get("ybbh").equals(ybbh)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)){
                    map = collect.get(0);
                    list.remove(collect.get(0));
                }
                List<String> valueList = new ArrayList<>();
                if (map.get(key)!=null) {
                    valueList = (List<String>) map.get(key);
                }
                valueList.add(childFile.getAbsolutePath());
                map.put("ybbh",ybbh);
                map.put(key,valueList);
                list.add(map);
            }
        } else {
            String ybbh = file.getName().split("_")[0];
            Map<String,Object> map = new HashMap<>();
            List<Map<String, Object>> collect = list.stream().filter(item -> item.get("ybbh").equals(ybbh)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)){
                map = collect.get(0);
                list.remove(collect.get(0));
            }
            List<String> valueList = new ArrayList<>();
            if (map.get(key)!=null) {
                valueList = (List<String>) map.get(key);
            }
            valueList.add(file.getAbsolutePath());
            map.put("ybbh",ybbh);
            map.put(key,valueList);
            list.add(map);
        }
    }
    /**
     * 创建文件夹
     * @param ywlx
     * @param realFilePath
     * @return
     */
    protected String mkDirs(String ywlx,String realFilePath,String otherName)
    {
        String storePath;
        if(ywlx!=null) {
            //根据日期创建文件夹
            storePath =realFilePath + ywlx +"/"+ "UP"+
                    DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
                    DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
                    DateUtils.getCustomFomratCurrentDate("yyyyMMdd") + (StringUtil.isNotBlank(otherName)?("/"+otherName):"");

        }else {
            storePath=realFilePath;
        }

        File file = new File(storePath);
        if (file.isDirectory()) {
            return storePath;
        }
        if(file.mkdirs())
        {
            return storePath;
        }
        return null;
    }
}
