package com.matridx.igams.bioinformation.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.MngsmxjgDto;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class log2Util {

    private static final Logger log = LoggerFactory.getLogger(log2Util.class);
    /***
     * 对焦函数
     */
    public static double log2(double N) {
        return 1000 * (Math.log(N) / Math.log(2));//Math.log的底为e
    }

    /***
     * 根据数据生成pkl_data 用于计算结果集的二进制文件
     * @param filePath  生产的数据文件路径
     * @param fileName  文件名称
     * @param pklPath 生成的二级制文件路径
     * @param pyPath  执行的json文件路径
     * @param jsonString  数据
     */
    public static void buildPklData(String filePath,String fileName,String pklPath,String pyPath,String jsonString){
        log.error("filePath:"+filePath);
        log.error("fileName:"+fileName);
        log.error("pklPath:"+pklPath);
        log.error("pyPath:"+pyPath);
        FileWriter fw = null;
        try
        {
            File file = new File(filePath+fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(filePath+fileName);
            fw.write(jsonString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fw != null) {
                    fw.close();
                }
            }
            catch (Exception e)
            {
                log.error(e.getMessage());
                e.printStackTrace();
            }

        }
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("python3 "+pyPath+" "+filePath+fileName+" "+pklPath);
            //proc = Runtime.getRuntime().exec(args1);// 执行py文件

            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                log.error(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 读取Qz排位
     */
    public static double GaussianKernelSmoothing (String path,String type,String taxid,double qz,String pklpath){
        double qzindex=0.00;
        log.error("filePath:"+path);
        log.error("fileName:"+type);
        log.error("pklPath:"+taxid);
        log.error("pyPath:"+pklpath);

        Process proc;
        //String[] args1 = new String[]{"python3 ", path, qz+" ",type+" ",taxid+" ",pklpath};
        String line;
        StringBuilder qzpw= new StringBuilder();
        try {
            proc = Runtime.getRuntime().exec("python3 "+path+" "+qz+" "+type+" "+taxid+" "+pklpath);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            while ((line = in.readLine()) != null) {
                System.out.println(line);
                qzpw.append(line);
                log.error(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        if(StringUtil.isNotBlank(qzpw.toString())){
            qzindex=Double.parseDouble(qzpw.toString());
        }

        return qzindex;
    }

    public static String GaussianKernelSmoothingList (String taxidpath, String path, List<MngsmxjgDto> list, String pklpath){
        log.error("filePath:"+path);
        log.error("pyPath:"+pklpath);
        FileWriter fw = null;
        try
        {
            File file = new File(taxidpath+"qzlist.txt");
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(taxidpath+"qzlist.txt");
            fw.write(JSONObject.toJSONString(list));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fw != null) {
                    fw.close();
                }
            }
            catch (Exception e)
            {
                log.error(e.getMessage());
                e.printStackTrace();
            }

        }
        Process proc;
        String line;
        StringBuilder qzpw= new StringBuilder();
        try {
            proc = Runtime.getRuntime().exec("python3 "+path+" "+taxidpath+" "+pklpath);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            while ((line = in.readLine()) != null) {
                System.out.println(line);
                qzpw.append(line);
                log.error(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return qzpw+"";
    }



}
