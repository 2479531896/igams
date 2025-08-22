package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSON;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author hmz
 * @date2022/08/04 version V1.0
 **/
public class ImgQrCodeUtil {
    private final Logger log = LoggerFactory.getLogger(ImgQrCodeUtil.class);
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int margin = 0;
    private static final int LogoPart = 4;

    /**
     * 生成二维码并合成
     * @param infoMap
     * @param isPerpendicular 是否为竖版
     * @return
     * @throws Exception
     */
    public String generateQrcodeImg(Map<String,Object> infoMap,boolean isPerpendicular,String backgroundPath) throws Exception {
        String logoPath = infoMap.get("logoPath")!=null?infoMap.get("logoPath").toString():null;
        String qrCodePath = infoMap.get("qrCodePath").toString();
        String qrCodeContent = infoMap.get("qrCodeContent").toString();
        int frameWidth = Integer.parseInt(infoMap.get("frameWidth").toString());
        String imgPath = infoMap.get("imgPath").toString();
        ImgQrCodeUtil qrCodeUtil = new ImgQrCodeUtil();
        int backWidth = 0;
        int backHeight = 0;
        int qrCodeSize;
        if (StringUtil.isNotBlank(backgroundPath)){
            File file = new File(backgroundPath);
            BufferedImage backgroundImg = ImageIO.read(new FileInputStream(file));
            backWidth = backgroundImg.getWidth();
            backHeight = backgroundImg.getHeight();
            qrCodeSize = isPerpendicular?backWidth:backHeight;
        }else {
            qrCodeSize = (int) infoMap.get("qrCodeSize");
        }
        String qrCodeFlie = qrCodeUtil.creatQrcodeImg(qrCodeContent, logoPath, null, qrCodePath, qrCodeSize, qrCodeSize,null,null, Color.BLUE);
		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----qrCodeFlie:"+qrCodeFlie);
        String imgFile = qrCodeUtil.creatImg(qrCodeFlie,backgroundPath, imgPath, backWidth, backHeight, frameWidth,infoMap,isPerpendicular);
		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----imgFile:"+imgFile);
        return imgFile;
    }
    /**
     * 生成二维码并合成
     * @param infoMap
     * @param isPerpendicular 是否为竖版
     * @return
     * @throws Exception
     */
    public String generateQrcodeImg(Map<String,Object> infoMap,boolean isPerpendicular) throws Exception {
        String backgroundPath = "/matridx/fileupload/release/IMP_BASIC_IMG/perpendicularBack.png";
        if (!isPerpendicular){
            backgroundPath = "/matridx/fileupload/release/IMP_BASIC_IMG/horizontalBack.png";
        }
        return generateQrcodeImg(infoMap,isPerpendicular,backgroundPath);
    }
    /**
     * 图片合成方法
     * @param oldImgPath
     * @param newImgPath
     * @param width
     * @param height
     * @param frameWidth
     * @param infoList
     * @return
     * @throws IOException
     */
    public String creatImg(String oldImgPath, String backgroundPath, String newImgPath, int width, int height, int frameWidth, Map<String,Object> infoMap,boolean isPerpendicular) throws IOException {
        //设置新文件名
        String newImgFile = newImgPath+"/" + System.currentTimeMillis() + RandomUtil.getRandomString()+".png";
        //设置生成的底图的存放路径
        File file = new File(newImgFile);
        //生成图片画布
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //生成用于该画布的画笔对象
        Graphics2D g2d = bi.createGraphics();
        // ----------  背景透明代码开始  -----------------
        bi = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        //填充指定矩形区域的颜色
        g2d = bi.createGraphics();
        g2d.fillRect(0,0,width, height);
        g2d.dispose();
        // ----------  背景透明代码结束  -----------------
        // ----------  背景拼接开始  -----------------
        if (StringUtil.isNotBlank(backgroundPath)){
            File backgroundFile = new File(backgroundPath);
            BufferedImage backgroundImg = ImageIO.read(new FileInputStream(backgroundFile));
            g2d = bi.createGraphics();
            g2d.drawImage(backgroundImg, 0, 0, null);
            g2d.dispose();
        }

        // ----------  背景拼接结束  -----------------
        // ----------  二维码拼接开始  -----------------
        //获取二维码图片
        File oldImgFile = new File(oldImgPath);
        //载入生成的二维码图片
        BufferedImage qrCodeImg = ImageIO.read(oldImgFile);
        //将二维码图片绘制到画布上
        g2d = bi.createGraphics();
        g2d.drawImage(qrCodeImg,frameWidth,frameWidth,(isPerpendicular?width:height)-2*frameWidth,(isPerpendicular?width:height)-2*frameWidth,null);
        g2d.dispose();

        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        log.error("可用字体库：" + JSON.toJSONString(fontName));
        // ----------  二维码拼接结束  -----------------
        // ----------  样本编号开始  -----------------
        if (infoMap.get("ybbh")!=null){
//            try {
                g2d = bi.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color( 108,202,234));
                Font fontAlgerian = new Font(null, Font.PLAIN, 45);
                g2d.setFont(fontAlgerian);
                StringBuilder code = new StringBuilder(infoMap.get("ybbh").toString());
                int verifySize = code.length();
                String code1 = code.substring(0,verifySize/2);
                String code2 = code.substring(verifySize/2);
                code = new StringBuilder(code2);
            code.append(" ".repeat(Math.max(0, (isPerpendicular ? verifySize : (verifySize / 4)))));
                code.append(code1);
                verifySize = code.length();
                char[] chars = code.toString().toCharArray();
                double xiexian = isPerpendicular?(height-width-2*frameWidth - 4*65):(width-height-frameWidth - 4*45);
                AffineTransform affine = new AffineTransform();
                for (int i = 0; i < verifySize; i++) {
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:verifySize---i"+verifySize+"-----"+i);
                    //旋转角度
                    double angle = BigDecimal.valueOf(2 * Math.PI).divide(BigDecimal.valueOf(verifySize), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(i)).doubleValue();
                    double anchorX = isPerpendicular?(width/2 - Math.sin(angle)*xiexian):((width+height-frameWidth)/2 + Math.sin(angle)*xiexian);
                    double anchorY = isPerpendicular?(height-2*frameWidth + Math.cos(angle)*xiexian):((height+2*frameWidth)/2-Math.cos(angle)*xiexian);
    //            double xiexian = (height-width+frameWidth)/2 - 4*fm.getHeight();
    //            double anchorY = (height+width-frameWidth)/2 + Math.cos(angle)*xiexian;
                    affine.setToRotation(angle, anchorX, anchorY);
                    g2d.setTransform(affine);
//                    g2d.drawChars(chars, i, 1, (int) anchorX, (int) anchorY);
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:affine.drawString开始");
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:affine.drawString开始"+String.valueOf(chars[i]));
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:affine.drawString开始"+anchorX+"-----"+anchorY);
                    g2d.drawString(String.valueOf(chars[i]),  (int) anchorX, (int) anchorY);
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:affine.drawString结束");
                }
                g2d.dispose();
//            } catch (Exception e){
//                log.error(e.getMessage());
//            }
            // ----------  样本编号结束  -----------------
        }
        if (infoMap.get("hzxm")!=null){
    		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:9");
            // ----------  姓名开始  -----------------
//            try {
                g2d = bi.createGraphics();
                g2d.setColor( new Color(29,32,136));
                //提前设置文字参数，防止生成的文字带有锯齿
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:10");
                String hzxm = infoMap.get("hzxm").toString();//姓名
                int onlyWidth = isPerpendicular?(width-2*frameWidth):(width-height);//姓名的最大可显示长度
                int fontsize = 60;
                Font font = new Font("simhei",Font.PLAIN,60);//设置文字大小
                g2d.setFont(font);//设置到画笔
                // 计算文字长度，计算居中的x点坐标
                FontMetrics fmt = g2d.getFontMetrics(font);//获取该大小字体的字体度量单位
        		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:11");
                for (;;){
                    int strWidth = fmt.stringWidth(hzxm);
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:12");
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:strWidth"+strWidth);
            		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:onlyWidth"+onlyWidth);
                    if (strWidth > onlyWidth){
                        fontsize--;
                        font = new Font("simhei",Font.PLAIN,fontsize);
                        g2d.setFont(font);
                        fmt = g2d.getFontMetrics(font);
                    }else{
                        break;
                    }
                }
                int textWidth = fmt.stringWidth(hzxm);
                int leftWidth = isPerpendicular?((width-textWidth)/2):((width+height-frameWidth-textWidth)/2);
                //文字距离上放的距离
                double anorexia = isPerpendicular?(height-width-2*frameWidth - 4*65):(width-height-frameWidth - 4*45);
                int topHeight = isPerpendicular?(height-5*frameWidth):((int) ((height+anorexia-frameWidth)/2));
        		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:13");
                substrTextAndSet(fmt,g2d,hzxm,textWidth,leftWidth,topHeight,null);
                g2d.dispose();
        		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:14");
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
            // ----------  姓名结束  -----------------
        }
        if (infoMap.get("jcxmmc")!=null){
            // ----------  检测项目名称开始  -----------------
//            try {
                g2d = bi.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(10,171,98,128));
                Font fontAlgerian = new Font("simhei", Font.PLAIN, 30);
                g2d.setFont(fontAlgerian);
                String jcxmmc = infoMap.get("jcxmmc").toString();
                if (isPerpendicular){
                    int onlyWidth = width-2*frameWidth;//姓名的最大可显示长度
                    int fontsize = 60;
                    Font font = new Font("simhei",Font.PLAIN,30);//设置文字大小
                    g2d.setFont(font);//设置到画笔
                    // 计算文字长度，计算居中的x点坐标
                    FontMetrics fmt = g2d.getFontMetrics(font);//获取该大小字体的字体度量单位
                    for (;;){
                        int strWidth = fmt.stringWidth(jcxmmc);
                        if (strWidth > onlyWidth){
                            fontsize--;
                            font = new Font("simhei",Font.PLAIN,fontsize);
                            g2d.setFont(font);
                            fmt = g2d.getFontMetrics(font);
                        }else{
                            break;
                        }
                    }
                    int textWidth = fmt.stringWidth(jcxmmc);
                    int leftWidth = (width-textWidth)/2;
                    //文字距离上放的距离
                    substrTextAndSet(fmt,g2d,jcxmmc,textWidth,leftWidth,height-15,null);
                }else {
                    char[] oldchars = jcxmmc.toCharArray();
                    StringBuilder newjcxmmc = new StringBuilder();
                    for (int i = oldchars.length - 1; i >= 0; i--) {
                        newjcxmmc.append(oldchars[i]);
                    }
                    newjcxmmc = new StringBuilder("　　" + newjcxmmc + "　");
                    int verifySize = 28;
                    if (newjcxmmc .length()<verifySize){
                        var i = (verifySize - newjcxmmc.length())/2;
                        var j = (verifySize - newjcxmmc.length())%2;
                        for (int k = 0; k < i; k++) {
                            newjcxmmc = new StringBuilder("　" + newjcxmmc + "　");
                        }
                        if (j==1){
                            newjcxmmc.insert(0, "　");
                        }
                    }else {
                        verifySize = newjcxmmc.length();
                    }
                    char[] chars = newjcxmmc.toString().toCharArray();
                    double xiexian = 55 + width - height - frameWidth - 4 * 45;
                    for (int i = 0; i < verifySize; i++) {
                        AffineTransform affine = new AffineTransform();
                        //旋转角度
                        double angle = BigDecimal.valueOf(Math.PI).divide(BigDecimal.valueOf(verifySize), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(i)).subtract(BigDecimal.valueOf(Math.PI * 1.5)).doubleValue();
                        double anchorX = (width + height - frameWidth) / 2 + Math.sin(angle) * xiexian;
                        double anchorY = (height + 2 * frameWidth) / 2 - Math.cos(angle) * xiexian;
                        affine.setToRotation(angle + Math.PI, anchorX, anchorY);
                        g2d.setTransform(affine);
                        g2d.drawString(String.valueOf(chars[i]), (int) anchorX, (int) anchorY);
                    }
                }
                g2d.dispose();
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
            // ----------  检测项目名称结束  -----------------
        }
        // ----------  文字结束  -----------------
        //生成底图
        ImageIO.write(bi, "png", file);
        return newImgFile;
    }

    public int substrTextAndSet(FontMetrics fm,Graphics2D g2d,String text,int textWidth,int left, int top, String oldText){
        if (textWidth < fm.stringWidth(text)){
            String newText = text;
            int substrLength = text.length();
            while (textWidth<fm.stringWidth(newText)) {
                substrLength--;
                newText = text.substring(0,substrLength);
            }
            top = substrTextAndSet(fm,g2d,text.substring(0,substrLength),textWidth,left,top,null);
            top = substrTextAndSet(fm,g2d,text.substring(substrLength),textWidth,left,top,text);
        }else {
    		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:substrTextAndSet.drawString开始"+text+"-----"+left+"-----"+top);
            g2d.drawString(text,left,top);
    		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatImg:substrTextAndSet.drawString结束"+text+"-----"+left+"-----"+top);
            top += fm.getHeight();//获取该字体的高度
        }
        return top;
    }


    /**
     * 二维码生成方法
     * @param qrCodeContent 二维码内容
     * @param qrCodeLogoPath Logo图片路径
     * @param qrCodeText 水印文字
     * @param outPutPath 输出路径
     * @param width 二维码宽度
     * @param height 二维码高度
     * @param textSize 文字大小
     * @param textStyle 文字样式
     * @param textColor 文字颜色
     *
     */
    public String creatQrcodeImg(String qrCodeContent, String qrCodeLogoPath, String qrCodeText,String outPutPath, int width, int height, String textStyle, String textSize,Color textColor){
        String format = "png";
		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatQrcodeImg:1");
        BitMatrix bitMatrix = setBitMatrix(qrCodeContent, width, height);
		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatQrcodeImg:2");
        // 可通过输出流输出到页面,也可直接保存到文件
        OutputStream outStream;
        try {
    		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatQrcodeImg:3");
            String outPutFile = outPutPath+"/" + System.currentTimeMillis() + RandomUtil.getRandomString()+".png";
            outStream = new FileOutputStream(outPutFile);
    		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatQrcodeImg:4");
            writeToFile(bitMatrix, format, outStream, qrCodeLogoPath, qrCodeText,textStyle,textSize,textColor);
    		//log.error("报告替换：new_template_special：picParagraph-----generateQrcodeImg-----creatQrcodeImg:5");
            outStream.close();
            return outPutFile;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    /**
     * 生成二维码矩阵信息
     * @param content 二维码图片内容
     * @param width   二维码图片宽度
     * @param height  二维码图片高度
     */
    public static BitMatrix setBitMatrix(String content, int width, int height) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 指定编码方式,防止中文乱码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 指定纠错等级
        hints.put(EncodeHintType.MARGIN, margin); // 指定二维码四周白色区域大小
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitMatrix;
    }

    /**
     * 将二维码图片输出
     * @param matrix    二维码矩阵信息
     * @param format    图片格式
     * @param outStream 输出流
     * @param logoPath  logo图片路径
     * @param qrCodeText 二维码文字
     */
    public static void writeToFile(BitMatrix matrix, String format, OutputStream outStream, String logoPath, String qrCodeText, String textStyle, String textSize,Color textColor) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        // 加入LOGO水印效果
        if (StringUtil.isNotBlank(logoPath)) {
            image = addLogo(image, logoPath);
        }
        // 加入文字水印效果
        if (StringUtil.isNotBlank(qrCodeText)){
            //默认文字在下方
            int imageW = image.getWidth();
            int imageH = image.getHeight();
            int startX = (imageW - (Integer.parseInt(textSize) * qrCodeText.length())) / 2;
            // y开始的位置：图片高度-（图片高度-图片宽度）/2
            int startY = imageH - (imageH - imageW) / 2 + Integer.parseInt(textSize);
            Graphics2D g = image.createGraphics();
            //g.drawImage(src, 0, 0, imageW, imageH, null);
            g.setColor(textColor);
            g.setFont(new Font(null, Integer.parseInt(textStyle), Integer.parseInt(textSize)));
            g.drawString(qrCodeText, startX, startY);
            g.dispose();
        }
        ImageIO.write(image, format, outStream);
    }

    /**
     * 生成二维码图片
     * @param matrix 二维码矩阵信息
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * 在二维码图片中添加logo图片
     * @param image    二维码图片
     * @param logoPath logo图片路径
     */
    public static BufferedImage addLogo(BufferedImage image, String logoPath) throws IOException {
        Graphics2D g = image.createGraphics();
        BufferedImage logoImage = ImageIO.read(new File(logoPath));
        // 计算logo图片大小,可适应长方形图片,根据较短边生成正方形
        int width = image.getWidth() < image.getHeight() ? image.getWidth() / LogoPart : image.getHeight() / LogoPart;
        int height = width;
        // 计算logo图片放置位置
        int x = (image.getWidth() - width) / 2;
        int y = (image.getHeight() - height) / 2;
        // 在二维码图片上绘制logo图片
        g.drawImage(logoImage, x, y, width, height, null);
        // 绘制logo边框,可选
        //        g.drawRoundRect(x, y, logoImage.getWidth(), logoImage.getHeight(), 10, 10);
        g.setStroke(new BasicStroke(2)); // 画笔粗细
        g.setColor(Color.WHITE); // 边框颜色
        g.drawRect(x, y, width, height); // 矩形边框
        logoImage.flush();
        g.dispose();
        return image;
    }

    /**
     * 为图片添加文字
     * @param pressText   文字
     * @param newImage    带文字的图片
     * @param targetImage 需要添加文字的图片
     * @param fontStyle   字体风格
     * @param color       字体颜色
     * @param fontSize    字体大小
     * @param width       图片宽度
     * @param height      图片高度
     */
    public static void pressText(String pressText, String newImage, String targetImage, int fontStyle, Color color, int fontSize, int width, int height) {
        // 计算文字开始的位置
        // x开始的位置：（图片宽度-字体大小*字的个数）/2
        int startX = (width - (fontSize * pressText.length())) / 2;
        // y开始的位置：图片高度-（图片高度-图片宽度）/2
        int startY = height - (height - width) / 2 + fontSize;
        try {
            File file = new File(targetImage);
            BufferedImage src = ImageIO.read(file);
            int imageW = src.getWidth(null);
            int imageH = src.getHeight(null);
            BufferedImage image = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, imageW, imageH, null);
            g.setColor(color);
            g.setFont(new Font(null, fontStyle, fontSize));
            g.drawString(pressText, startX, startY);
            g.dispose();
            FileOutputStream out = new FileOutputStream(newImage);
            ImageIO.write(image, "png", out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析二维码图片
     * @param filePath 图片路径
     */
    public static String decodeQR(String filePath) {
        if ("".equalsIgnoreCase(filePath)) {
            return "二维码图片不存在!";
        }
        String content = "";
        EnumMap<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8"); // 指定编码方式,防止中文乱码
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);
            content = result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


}
