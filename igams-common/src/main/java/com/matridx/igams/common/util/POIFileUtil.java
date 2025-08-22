package com.matridx.igams.common.util;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class POIFileUtil {

    /**
     * POI解析PDF文档内容
     * @param file
     * @return
     * @throws IOException
     */
    public static String pdf2String(File file) throws IOException {
        PDDocument document=PDDocument.load(file);
        PDFTextStripper stripper=new PDFTextStripper();
        stripper.setSortByPosition(false);
        String result=stripper.getText(document);
        document.close();
        return result;
    }

    /**
     * POI解析XLSX文档内容
     * @param file
     * @return
     * @throws IOException
     */
    public static String xlsx2String(File file) throws IOException {
        String result="";
        try {
            FileInputStream inputStream=new FileInputStream(file);
            StringBuilder stringBuilder=new StringBuilder();
            XSSFWorkbook xssfWorkbook=new XSSFWorkbook();
            XSSFSheet sheet=xssfWorkbook.getSheetAt(0);
            for(int i=sheet.getFirstRowNum()+1;i<sheet.getPhysicalNumberOfRows();i++){
                XSSFRow row=sheet.getRow(i);
                for(int j=row.getFirstCellNum();j<row.getPhysicalNumberOfCells();j++){
                    stringBuilder.append(row.getCell(j).toString());
                }
            }
            inputStream.close();
            result+=stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
