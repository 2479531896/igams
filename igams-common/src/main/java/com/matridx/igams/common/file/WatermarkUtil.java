package com.matridx.igams.common.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Component
public class WatermarkUtil {
	
	private static final Logger log = LoggerFactory.getLogger(WatermarkUtil.class);
	
	private static final Integer WATER_MAX_LENGTH = 36;
	
	public static String watermarkName = "";
	public static String imagePath = "";
	public static String watermarkFontsize = "";
	public static int watermarkStart = 1;
	public static String watermarkNumX = "";
	public static String watermarkNumY = "";
	public static String watermarkFillOpacity = "";
	public static String watermarkRotation = "";
    public static String watermarkPageNumber = "";
	
	/**
	  * 用图片方式添加水印到pdf里
	 * @param file
	 * @return
	 */
	public static boolean addWatermarkOfImage(final File file){
		PdfReader reader = null;
		File outFile = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(file.getPath());
			String outputFile = file.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();
			
			addWatermarkOfImage(stamper, waterMarkCount, imagePath);
			
			log.error("为文件("+file.getAbsolutePath()+")添加水印("+imagePath+")成功!");
			return true;
		} catch (Exception e) {
			log.error("为文件("+file.getAbsolutePath()+")添加水印失败!"+e.getMessage(), e);
			return false;
		} finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            if(outFile.exists() && outFile.length() > 0) {
				deleteFile(file);
				outFile.renameTo(file);
			}
		}
	}
	
	/**
	 * 用文字方式添加水印
	 * @param file
	 * @return
	 */
	public static boolean addWatermark(final File file,SyxxDto syxxDto){
		getWatermark(syxxDto);
		
		PdfReader reader = null;
		File outFile = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(file.getPath());
			String outputFile = file.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if (!outFile.exists()) {
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();
			// 水印最多12个汉字
			if (watermarkName.getBytes().length > WATER_MAX_LENGTH) {
				watermarkName = new String(watermarkName.getBytes(), 0, WATER_MAX_LENGTH / 3);
			}
			
			addWatermark(stamper, waterMarkCount, watermarkName);
			
			log.error("为文件("+file.getAbsolutePath()+")添加水印("+watermarkName+")成功!");
			return true;
		} catch (Exception e) {
			log.error("为文件("+file.getAbsolutePath()+")添加水印失败!"+e.getMessage(), e);
			return false;
		} finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            if(outFile.exists() && outFile.length() > 0) {
				deleteFile(file);
				outFile.renameTo(file);
			}
		}
	}
	
	/**
	  * 强势删除文件
     * 
     * @param file : 文件
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
        	log.warn("删除文件"+file.getAbsolutePath()+"失败!" + e.getMessage(), e);
        }
    }
    
    private static void addWatermarkOfImage(PdfStamper stamper, int waterMarkCount, String imagePath){
    	try{
    		//创建背景图片对象
    		Image image = null;
    		if(imagePath!=null&&!imagePath.isEmpty()) {
    			image = Image.getInstance(imagePath);
    			image.setAbsolutePosition(450, 750);
    		}

    		PdfContentByte under;
    		for (int i = 1; i <= waterMarkCount; i++) {
    			//拿到pdf指定的一页
    			under = stamper.getUnderContent(i);
    			// 添加图片
    			under.addImage(image);
    		}
    		stamper.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
	
	/**
	 * 加水印
	 * 
	 * @param stamper
	 *            ：PDF顶层对象
	 * @param pageRectangle
	 *            ：页面大小
	 * @param waterMarkCount
	 *            ：页面页数
	 * @param waterMarkName
	 *            ：水印名字
	 */
	private static void addWatermark(PdfStamper stamper, int waterMarkCount, String waterMarkName) {
		PdfContentByte content;
		BaseFont baseEnglish = null;
		BaseFont baseChinese = null;
		try {
			// 设置字体
			baseEnglish = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			baseChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
			//baseChinese = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException | IOException e) {
			log.warn("设置字体时出错:"+e.getMessage(), e);
		}

        for (int i = watermarkStart; i < waterMarkCount + 1; i++) {
			content = stamper.getOverContent(i);// 获得PDF最顶层
			content.reset();
			// 这里采用了备忘录模式
			content.saveState();
			// set Transparency
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(Float.parseFloat(watermarkFillOpacity));// 设置透明度为0.3
			content.setGState(gs);

			content.beginText();
			content.setColorFill(BaseColor.GRAY);
			if (waterMarkName.length() == waterMarkName.getBytes().length) {
				content.setFontAndSize(baseEnglish, Float.parseFloat(watermarkFontsize));
			} else {
				content.setFontAndSize(baseChinese, Float.parseFloat(watermarkFontsize));
			}
			float x;
			float y;
			int intX = Integer.parseInt(watermarkNumX);
			int intY = Integer.parseInt(watermarkNumY);
			for(int a=0; a<=intX; a++){
				x = PageSize.A4.getWidth() / intX * (a);
				for(int b=0; b<=intY; b++){
					y = PageSize.A4.getHeight() /intY * (b);
					content.showTextAligned(Element.ALIGN_LEFT, waterMarkName, x, y, Float.parseFloat(watermarkRotation));// 水印文字成35度角倾斜
				}
			}
			//是否显示页数
			if("1".equals(watermarkPageNumber)) {
				content.showTextAligned(Element.ALIGN_BOTTOM, String.valueOf(i), 295, 0, 0);
				content.showTextAligned(Element.ALIGN_BOTTOM, String.valueOf(i), 295, 0, 0);
				content.showTextAligned(Element.ALIGN_BOTTOM, String.valueOf(i), 295, 0, 0);
			}
			
			content.endText();

			content.restoreState();// 注意这里必须调用一次restoreState 否则设置无效
		}
	}
	
	private static void getWatermark(SyxxDto syxxDto){
		
		//水印名称
		watermarkName =syxxDto.getSyz();
		//字体大小
		watermarkFontsize =syxxDto.getSyztdx();
		//水印图片
		if(syxxDto.getSytplj()!=null) {
			String tmp_imagePath=syxxDto.getSytplj();
			DBEncrypt dbEncrypt=new DBEncrypt();
			imagePath=dbEncrypt.dCode(tmp_imagePath);
		}
		//开始页数 为空时默认为1
		watermarkStart=syxxDto.getKsys()!=null? Integer.parseInt(syxxDto.getKsys()):1;
		//横向间隔数
		watermarkNumX =syxxDto.getSyhxjg();
		//纵向间隔数
		watermarkNumY =syxxDto.getSyzxjg();
		//透明度
		watermarkFillOpacity =syxxDto.getSytmd();
		//倾斜角度
		watermarkRotation = syxxDto.getSyqxjd();
		//页数显示
		watermarkPageNumber=syxxDto.getYsxs();
	}
	
	public static void main(String[] args) {
		//iPdf.doc2Pdf(orgQueue.getFullFilePathAndName(), destFile);
		File file = new File("D:/zfsoft/fileManager/upfile/release/20151106/1446804527187VaEZZF.pdf");
		if(file.exists()){
//			addWatermark(file);
		}
	}
}
