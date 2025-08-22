package com.matridx.springboot.util.gzip;

import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * ssm 2021/10/21
 * 通过java.util.zip.GZIPOutputStream对字符串基于base64（sun.misc.BASE64Encoder().encode）加密压缩compressEncode处理，
 * 再经过base64解密（sun.misc.BASE64Decoder().decodeBuffer）后java.util.zip.GZIPInputStream还原解压原始为字符串，
 */
public class GZIPUtil {
    /**
     * 压缩指定的字符串
     */
    public  String compressEncode(String str) throws IOException{
        if (str == null || str.isEmpty()) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return Base64.encodeBase64String(out.toByteArray());//sun.misc.BASE64Decoder().encode() jdk8可用，11不可用，改为当前的方法替换
    }

    /**
     * 对字节类型进行解压缩
     */
    public  String uncompressDecode(String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return null;
        }
        byte[] bytes = new Base64().decode(str);//sun.misc.BASE64Decoder().decodeBuffer() jdk8可用，11不可用，改为当前的方法替换
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        if(!isGZIP(in)){
            return str;
        }
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        ungzip.close();
        return out.toString();
    }

    /**
     * 判断是否为gzip文件
     */
    private  boolean isGZIP(InputStream in) throws IOException{
        boolean mFlag=true;
        // 取前两个字节
        byte[] header = new byte[2];
        in.read(header);
        in.reset();
        int b3=((header[1] & 0xff)<<8) |(header[0] & 0xff);
        if(b3 != GZIPInputStream.GZIP_MAGIC){
            mFlag=false;
        }
        return mFlag;
    }

//    public static void main(String[] args) throws IOException {
//        //用法测试
//        String s = "wahaha hangzhou jieyi shengwu";
//        GZIPUtil gzipUtil = new GZIPUtil();
//        String m = gzipUtil.compressEncode(s);
//        String mold = gzipUtil.compressEncodeOld(s);
//        System.out.println("1.字符串原大小："+s.length());
//        System.out.println("2.压缩后大小新的："+gzipUtil.compressEncode(s).length());
//        System.out.println("2.压缩后大小旧的："+gzipUtil.compressEncodeOld(s).length());
//        System.out.println("3.解压缩后大小："+gzipUtil.uncompressDecode(s).length());
//
//        System.out.println("a.压缩后字段新的"+m);
//        System.out.println("a.压缩后字段旧的"+mold);
//        System.out.println("b.解压缩后字段"+gzipUtil.uncompressDecode(s));
//
//    }

}
