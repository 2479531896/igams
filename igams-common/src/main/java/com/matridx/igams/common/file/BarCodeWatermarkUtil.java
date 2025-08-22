package com.matridx.igams.common.file;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.post.IFjcfbDao;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.springboot.util.encrypt.DBEncrypt;

/**
 *@描述		：  添加条形码水印
 */
public class BarCodeWatermarkUtil {

	private static final Logger log = LoggerFactory.getLogger(BarCodeWatermarkUtil.class);
	
	/**
	  * 生成条形码
     * @param code 生成条码码的编号
	 */
	private static byte[] genBarCodeJpg(String barCode) {
        Code39Bean bean = new Code39Bean();
        final int dpi = 250;
        //样式
        bean.setModuleWidth(0.21);
        bean.setBarHeight(7);
        bean.doQuietZone(false);
        bean.setQuietZone(2);

        //两边空白区        
        bean.setFontName("Helvetica");
        bean.setFontSize(3);
        bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);

        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,
                    "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY
                    , true, 0);

            bean.generateBarcode(canvas, barCode);
            canvas.finish();
            return out.toByteArray();

        } catch (Exception e) {
        	log.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	/**
	  * 将条形码写入PDF文件
     * @param pdfFile PDF类型的文件(word等需要转换为PDF)
     * @param barcode 生成条形码的code
     * @return
     * @throws Exception
     */
	public static boolean pdfAddControlCode(File pdfFile, String barCode) {
        if(StringUtils.isBlank(barCode)){
            return false;
        }
        PdfReader reader = null;
        File outFile = null;
        PdfStamper stamper = null;
        try {
			reader = new PdfReader(pdfFile.getPath());
			String outputFile = pdfFile.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();
			
			//在第一页加上条形码
	        Image img = Image.getInstance(genBarCodeJpg(barCode));
			
	        PdfContentByte under;
	        for(int i=1;i<=waterMarkCount;i++){
	        	Rectangle rectangle = reader.getPageSize(i);
	        	float width = rectangle.getWidth();
	        	float height = rectangle.getHeight();
	        	
	        	img.setAlignment(1);//居中显示
	        	//显示位置，根据需要调整
	        	img.setAbsolutePosition(width-img.getScaledWidth()-40, height - 60);
	        	//显示为原条形码图片大小的比例，百分比
	        	img.scalePercent(40);
	        	
	        	// 拿到pdf指定的一页
	        	under = stamper.getOverContent(i);
	        	// 添加条形码
	        	under.addImage(img);
	        }
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码成功!");
			return true;
		} catch(Exception e){
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码失败!"+e.getMessage(), e);
			return false;
		}finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            deleteFile(pdfFile);
			outFile.renameTo(pdfFile);
		}
        
    }
	
	/**
     * 将审核信息写入PDF文件
     * @param pdfFile PDF类型的文件(word等需要转换为PDF)
     * @param barcode 生成条形码的code
     * @return
     * @throws Exception
     */
	public static boolean pdfAddAuditInfo(File pdfFile, String auditInfo,String auditDateInfo,String auditNameStr, String sxrq) {
		if(StringUtils.isBlank(auditInfo) || StringUtils.isBlank(auditDateInfo)){
            return false;
        }
		
		String[] auditStrings = auditInfo.split(",");
		String[] auditDate = auditDateInfo.split(",");
		String[] auditName = auditNameStr.split(",");
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		FjcfbDto fj = new FjcfbDto();
		fj.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
		List<String> ywids = Arrays.asList(auditName);
		fj.setYwids(ywids);
		//查询附件文件路径
		List<FjcfbDto> fjcfbDtos = fileUploadDao.selectFjcfbDtoByYwidsAndYwlx(fj);
		String[] imageFilePath = new String[ywids.size()];
		if(fjcfbDtos != null){
			DBEncrypt p = new DBEncrypt();
			for (int i = 0; i < ywids.size(); i++) {
                for (FjcfbDto fjcfbDto : fjcfbDtos) {
                    if (fjcfbDto.getYwid().equals(ywids.get(i))) {
                        imageFilePath[i] = p.dCode(fjcfbDto.getWjlj());
                        break;
                    }
                }
			}
		}
		if(auditStrings.length != auditDate.length)
			return false;
		
        PdfReader reader = null;
        File outFile = null;
        PdfStamper stamper = null;
        try {
			reader = new PdfReader(pdfFile.getPath());
			String outputFile = pdfFile.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();
	        PdfContentByte under;
	        
        	//Rectangle rectangle = reader.getPageSize(waterMarkCount);
        	//float width = rectangle.getWidth();
        	BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        	// 拿到pdf指定的一页
        	under = stamper.getOverContent(waterMarkCount);
			for(int i =0;i< auditStrings.length;i++) {
				File file = new File(imageFilePath[i]);
				if(file.exists()) {
					//在第一页加上审核
			        Image img = Image.getInstance(imageFilePath[i]);
			        
		        	img.setAlignment(1);//居中显示
		        	//显示位置，根据需要调整
		        	img.setAbsolutePosition(i * 110 + 50, 142);
		        	//显示为原条形码图片大小的比例，百分比
		        	//img.scalePercent(80);
		        	//设置图片大小
		        	img.scaleAbsolute(80, 48);
		        	// 添加条形码
		        	under.addImage(img);
				}
	        	//加载文本
	        	under.beginText();
	        	under.setTextRenderingMode(0);
	        	under.setFontAndSize(bf, 14);
	        	under.setTextMatrix(100,100);
	        	under.showTextAligned(Element.ALIGN_CENTER, auditDate[i], i * 110 + 90, 100, 0);
    	        //结束加载文本
	        	under.endText();
			}
			
			//加载文本
        	under.beginText();
        	under.setTextRenderingMode(0);
        	under.setFontAndSize(bf, 14);
        	under.setTextMatrix(100,100);
        	under.showTextAligned(Element.ALIGN_CENTER, sxrq, 450, 50, 0);
	        //结束加载文本
        	under.endText();
			
			return true;
		} catch(Exception e){
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码失败!"+e.getMessage(), e);
			return false;
		}finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            deleteFile(pdfFile);
			outFile.renameTo(pdfFile);
		}
        
    }

	/**
	 * 将电子签名写入PDF文件
	 * @param pdfFile PDF类型的文件(word等需要转换为PDF)
	 * @return
	 * @throws Exception
	 */
	public static boolean pdfAddAuditReportInfo(File pdfFile, String auditInfo,String auditDateInfo,String auditNameStr, String sxrq) {
		if(StringUtils.isBlank(auditInfo) || StringUtils.isBlank(auditDateInfo)){
			return false;
		}

		String[] auditStrings = auditInfo.split(",");
		String[] auditDate = auditDateInfo.split(",");
		String[] auditName = auditNameStr.split(",");
		log.error(" 审核人---------> "+ Arrays.toString(auditStrings));
		log.error(" 审核时间---------> "+ Arrays.toString(auditDate));
		log.error(" 用户名---------> "+ Arrays.toString(auditName));
		IFjcfbDao fileUploadDao = ServiceFactory.getService(IFjcfbDao.class);
		FjcfbDto fj = new FjcfbDto();
		fj.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
		List<String> ywids = Arrays.asList(auditName);
		fj.setYwids(ywids);
		//查询附件文件路径
		List<FjcfbDto> fjcfbDtos = fileUploadDao.selectFjcfbDtoByYwidsAndYwlx(fj);
		log.error(" 文件名---------> "+fjcfbDtos.get(0).getWjm());
		String[] imageFilePath = new String[ywids.size()];
        DBEncrypt p = new DBEncrypt();
        for (int i = 0; i < ywids.size(); i++) {
for (FjcfbDto fjcfbDto : fjcfbDtos) {
if (fjcfbDto.getYwid().equals(ywids.get(i))) {
imageFilePath[i] = p.dCode(fjcfbDto.getWjlj());
break;
}
}
        }

        if(auditStrings.length == 0){
			return false;
		}else{
			if(auditDate.length>0){
				if(auditStrings.length != auditDate.length)
					return false;
			}
		}

		PdfReader reader = null;
		File outFile = null;
		PdfStamper stamper = null;
		try {
			FileInputStream inputStream=new FileInputStream(pdfFile.getPath());
			reader = new PdfReader(inputStream);
			String outputFile = pdfFile.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();
			PdfContentByte under;

			//Rectangle rectangle = reader.getPageSize(waterMarkCount);
			//float width = rectangle.getWidth();
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
			// 拿到pdf指定的一页
			under = stamper.getOverContent(waterMarkCount);
			for(int i =0;i< auditStrings.length;i++) {
				String auditdate_t="";
				if(auditDate.length>0)
					auditdate_t=auditDate[i];
				File file = new File(imageFilePath[i]);
				if(file.exists()) {
					//在第一页加上审核
					Image img = Image.getInstance(imageFilePath[i]);

					img.setAlignment(1);//居中显示
					//显示位置，根据需要调整
					img.setAbsolutePosition(i * 185 + 120, 160);
					//显示为原条形码图片大小的比例，百分比
					//img.scalePercent(80);
					//设置图片大小
					img.scaleAbsolute(50, 28);
					// 添加条形码
					under.addImage(img);
				}
				//加载文本
				under.beginText();
				under.setTextRenderingMode(0);
				under.setFontAndSize(bf, 14);
				under.setTextMatrix(100,100);
				under.showTextAligned(Element.ALIGN_CENTER, auditdate_t, i * 110 + 90, 100, 0);
				//结束加载文本
				under.endText();
			}

			//加载文本
			under.beginText();
			under.setTextRenderingMode(0);
			under.setFontAndSize(bf, 14);
			under.setTextMatrix(100,100);
			under.showTextAligned(Element.ALIGN_CENTER, sxrq, 450, 50, 0);
			//结束加载文本
			under.endText();

			return true;
		} catch(Exception e){
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码失败!"+e.getMessage(), e);
			return false;
		}finally {
			try {
				if(stamper!=null){
					stamper.close();
					stamper.flush();
				}
			} catch (Exception e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
			try {
				if(reader!=null){
					reader.close();
				}
			} catch (Exception e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
			deleteFile(pdfFile);
			outFile.renameTo(pdfFile);
		}

	}

	/**
	 * 将印章图片加入送检结果pdf文件
	 * @param pdfFile
	 * @param chapter
	 * @return
	 */
	public static boolean pdfAddChapter(File pdfFile, String chapter) {
		// TODO Auto-generated method stub
        PdfReader reader = null;
        File outFile = null;
        PdfStamper stamper = null;
        try {
			FileInputStream inputStream=new FileInputStream(pdfFile.getPath());
			reader = new PdfReader(inputStream);
			String outputFile = pdfFile.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();
			
			//在第一页加上条形码
	        Image img = Image.getInstance(Thread.currentThread().getContextClassLoader().getResource("static/image/"+chapter));
	        
	        PdfContentByte under;
	        for(int i=1;i<=waterMarkCount;i++){
	        	if(i == waterMarkCount-1){
	        		Rectangle rectangle = reader.getPageSize(i);
		        	float width = rectangle.getWidth();
		        	float height = rectangle.getHeight();
		        	
		        	img.setAlignment(1);//居中显示
		        	//显示位置，根据需要调整
		        	img.setAbsolutePosition(width-240, height - 705);
		        	//显示为原条形码图片大小的比例，百分比
		        	//设置图片大小
		        	img.scaleAbsolute(180, 180);
		        	
		        	// 拿到pdf指定的一页
		        	under = stamper.getOverContent(i);
		        	// 添加条形码
		        	under.addImage(img);
	        	}
	        }
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码成功!");
			return true;
		} catch(Exception e){
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码失败!"+e.getMessage(), e);
			return false;
		}finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            deleteFile(pdfFile);
			outFile.renameTo(pdfFile);
		}
	}

	/**
	 * 将印章图片加入送检结果pdf文件
	 * @param pdfFile
	 * @param chapter
	 * @return
	 */
	public static boolean pdfAddChapterInfo(File pdfFile, String chapter) {
		// TODO Auto-generated method stub
		PdfReader reader = null;
		File outFile = null;
		PdfStamper stamper = null;
		try {
			FileInputStream inputStream=new FileInputStream(pdfFile.getPath());
			reader = new PdfReader(inputStream);
			String outputFile = pdfFile.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();

			//盖章放到最后一页
			Image img = Image.getInstance(Thread.currentThread().getContextClassLoader().getResource("static/image/"+chapter));

			PdfContentByte under;
			for(int i=1;i<=waterMarkCount;i++){
				if(i == waterMarkCount){
					Rectangle rectangle = reader.getPageSize(i);
					float width = rectangle.getWidth();
					float height = rectangle.getHeight();

					img.setAlignment(1);//居中显示
					//显示位置，根据需要调整
					img.setAbsolutePosition(width-220, height - 705);
					//显示为原条形码图片大小的比例，百分比
					//设置图片大小
					img.scaleAbsolute(120, 120);

					// 拿到pdf指定的一页
					under = stamper.getOverContent(i);
					// 添加条形码
					under.addImage(img);
				}
			}
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码成功!");
			return true;
		} catch(Exception e){
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码失败!"+e.getMessage(), e);
			return false;
		}finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            deleteFile(pdfFile);
			outFile.renameTo(pdfFile);
		}
	}


	/**
	 * 将印章图片加入送检结果pdf文件
	 * @param pdfFile
	 * @param chapter
	 * @return
	 */
	public static boolean pdfAddChapterInfoWithPlace(File pdfFile, String chapter,String floatWidth, String floatHeight) {
		// TODO Auto-generated method stub
		PdfReader reader = null;
		File outFile = null;
		PdfStamper stamper = null;
		try {
			FileInputStream inputStream=new FileInputStream(pdfFile.getPath());
			reader = new PdfReader(inputStream);
			String outputFile = pdfFile.getParent() + File.separator + UUID.randomUUID()+".pdf";
			outFile = new File(outputFile);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			stamper = new PdfStamper(reader, new FileOutputStream(outputFile));
			int waterMarkCount = reader.getNumberOfPages();

			//盖章放到最后一页
			Image img = Image.getInstance(Thread.currentThread().getContextClassLoader().getResource("static/image/"+chapter));

			PdfContentByte under;
			for(int i=1;i<=waterMarkCount;i++){
				if(i == waterMarkCount){
					Rectangle rectangle = reader.getPageSize(i);
					float width = rectangle.getWidth();
					float height = rectangle.getHeight();

					img.setAlignment(1);//居中显示
					//显示位置，根据需要调整
					if (StringUtil.isNotBlank(floatWidth) && StringUtil.isNotBlank(floatHeight)) {
						img.setAbsolutePosition(width-Float.parseFloat(floatWidth), height - Float.parseFloat(floatHeight));
					} else {
						img.setAbsolutePosition(width-220, height - 505);
					}
					//显示为原条形码图片大小的比例，百分比
					//设置图片大小
					img.scaleAbsolute(150, 150);

					// 拿到pdf指定的一页
					under = stamper.getOverContent(i);
					// 添加条形码
					under.addImage(img);
				}
			}
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码成功!");
			return true;
		} catch(Exception e){
			log.error("为文件("+pdfFile.getAbsolutePath()+")添加条形码失败!"+e.getMessage(), e);
			return false;
		}finally {
			try {
				stamper.close();
				reader.close();
			} catch (DocumentException | IOException e) {
				log.warn("关闭PdfStamper失败!"+e.getMessage(), e);
			}
            deleteFile(pdfFile);
			outFile.renameTo(pdfFile);
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
        	e.printStackTrace();
        	log.warn("删除文件"+file.getAbsolutePath()+"失败!" + e.getMessage(), e);
        }
    }
    
    public static void main(String[] args) {
		try {
			File file = new File("e:/demo.pdf");
			BarCodeWatermarkUtil.pdfAddControlCode(file, "666666666666666");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
