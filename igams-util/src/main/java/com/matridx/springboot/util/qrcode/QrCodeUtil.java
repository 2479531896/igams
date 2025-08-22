package com.matridx.springboot.util.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
import javax.imageio.ImageIO;
 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

/**
 * QR链接生成器
 * @author Kaijian Lee
 * 
 */
public class QrCodeUtil {
 
    public static void main(String[] args) {
        String url = "http://demo.d.com";
        String path = "E:\\Qr\\" + File.separator + "testQrcode";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        createQrCode(url, path, fileName);
    }
 
    /**
     * 根据提供的网址信息，生成相应的二维码，然后把图片保存到相应的地址中
     * @param url 要放到二维码中的信息
     * @param path 保存文件路径
     * @param fileName 保存文件名
     * @return
     */
    public static String createQrCode(String url, String path, String fileName) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            File file = new File(path, fileName);
            if (file.exists() || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
            	writeToFile(bitMatrix, "jpg", file);
            	return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 把缓存图片写入相应的文件中
     * @param matrix
     * @param format
     * @param file
     * @throws IOException
     */
    static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }
 
    /**
     * 把缓存图片输出到相应的流里
     * @param matrix
     * @param format
     * @param stream
     * @throws IOException
     */
    static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }
 
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
 
    /**
     * 生成缓存图片
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
 
}