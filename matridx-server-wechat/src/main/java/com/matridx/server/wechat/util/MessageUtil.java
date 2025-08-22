package com.matridx.server.wechat.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class MessageUtil {
	public static final String MSGTYPE_EVENT = "event";//消息类型--事件
	public static final String MESSAGE_SUBSCIBE = "subscribe";//消息事件类型--订阅事件
	public static final String MESSAGE_UNSUBSCIBE = "unsubscribe";//消息事件类型--取消订阅事件
	public static final String MESSAGE_CLICK = "CLICK";//消息事件类型--点击菜单拉取消息时的事件推送
	public static final String MESSAGE_VIEW = "VIEW";//消息事件类型--点击菜单跳转链接时的事件推送
	public static final String MESSAGE_TEXT = "text";//消息类型--文本消息
	public static final String MESSAGE_SCAN = "SCAN";//消息类型--扫描带参数二维码
	
	public static void main(String[] args) {
		try{
			/**二维码图片的保存路径**/
			String imagePath = "E:\\条形码8.png" ;
			//File file = new File(imagePath) ;
			
			/**条形码解析**/
			//QRCodeReader reader = new QRCodeReader();
			
			BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result result = new MultiFormatReader().decode(bitmap, null);
                System.out.println("图片中格式：   " + result.getText());
            }
			
			BufferedImage image2 = ImageIO.read(new File(imagePath));  
            LuminanceSource source2 = new BufferedImageLuminanceSource(image2);  
            Binarizer binarizer2 = new HybridBinarizer(source2);  
            BinaryBitmap binaryBitmap2 = new BinaryBitmap(binarizer2);  
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
            Result result2 = new MultiFormatReader().decode(binaryBitmap2, hints);// 对图像进行解码  
            System.out.println("图片中内容：  "+ result2.getText());  
            System.out.println("图片中格式：   " + result2.getBarcodeFormat()); 
            
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}
