package com.matridx.las.netty.util.snapshot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 系统全局快照
 */
@Component
public class SnapShotGloabalUtil {

    private static final Logger logger = LoggerFactory.getLogger(SnapShotGloabalUtil.class);

    static String path = "";
    //读出所有的值
    public static String inputFile() throws Exception {
        Properties props = new Properties();//使用Properties类来加载属性文件
        FileInputStream iFile = new FileInputStream(path);
        props.load(iFile);
        /**begin*******直接遍历文件key值获取*******begin*/
        Iterator<String> iterator = props.stringPropertyNames().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
        }
        /**end*******直接遍历文件key值获取*******end*/

        /**begin*******在知道Key值的情况下，直接getProperty即可获取*******begin*/
        String json = props.getProperty("json");
        logger.info(json);
        /**end*******在知道Key值的情况下，直接getProperty即可获取*******end*/
        iFile.close();
        return json;

    }

    public static void outputFile(String key, String value) throws IOException {
                 ///保存属性到b.properties文件
                 Properties props=new Properties();

                 FileOutputStream oFile = new FileOutputStream(path, true);//true表示追加打开
                 props.setProperty(key, value);
                 //store(OutputStream,comments):store(输出流，注释)  注释可以通过“\n”来换行
                 props.store(oFile,"保存数据");
                 oFile.close();
            }



    public static void updateProperties(String keyname, String keyvalue) {
        try {
            Properties props=new Properties();

            props.load(new FileInputStream(path));
            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(path);
            props.setProperty(keyname, keyvalue);
            logger.info(keyname,keyvalue);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("属性文件更新错误："+e.getMessage());
        }
    }
}
