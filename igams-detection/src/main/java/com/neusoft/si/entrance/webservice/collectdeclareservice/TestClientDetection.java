package com.neusoft.si.entrance.webservice.collectdeclareservice;

import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.gzip.GZIPUtil;
import com.matridx.springboot.util.xml.XmlUtil;
import com.neusoft.si.entrance.webservice.CarryXmlToDbService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.dom4j.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestClientDetection {
    public static String pccode = "XBSJCJCJQ:PCRWHHQQ";
    public static String dccode = "XBSJCJCJQ:HQDCRWHQ";
    public static String sccode = "XBSJCJCJQ:SJSCQ";
    public static String standardcode = "REQ.C0101.0303.02";
    public static String standardcodebm = "C0101.0303.02";

    public static String standardcodeSQ = "REQ.C0101.0303.01";
    public static String standardcodebmSQ = "C0101.0303.01";

    public static String standardcodeCG = "REQ.C0101.0303.0201";
    public static String standardcodebmCG = "C0101.0303.0201";


    public static String text ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<messages xmlns=\"http://www.neusoft.com/hit/rhin\">\n" +
            "  <heartbeat></heartbeat>\n" +
            "  <switchset>\n" +
            "    <authority>\n" +
            "      <authoritytype></authoritytype>\n" +
            "      <username></username>\n" +
            "      <userpwd></userpwd>\n" +
            "      <license></license>\n" +
            "    </authority>\n" +
            "    <visitor>\n" +
            "      <sourceorgan>330110PDY10181417P1202</sourceorgan>\n" +
            "      <sourcedomain>3301000143</sourcedomain>\n" +
            "    </visitor>\n" +
            "    <serviceinf>\n" +
            "      <servicecode>采集单次声明服务servicecode</servicecode>\n" +
            "    </serviceinf>\n" +
            "    <provider>\n" +
            "      <targetorgan></targetorgan>\n" +
            "      <targetdomain></targetdomain>\n" +
            "    </provider>\n" +
            "    <route></route>\n" +
            "    <process></process>\n" +
            "  </switchset>\n" +
            "  <business>\n" +
            "    <standardcode></standardcode>\n" +
            "    <requestset>\n" +
            "      <reqcondition>\n" +
            "        <condition></condition>\n" +
            "      </reqcondition>\n" +
            "      <reqpaging></reqpaging>\n" +
            "      <reqpageindex></reqpageindex>\n" +
            "      <reqpageset></reqpageset>\n" +
            "    </requestset>\n" +
            "    <datacompress></datacompress>\n" +
            "    <daqtaskid>20180601020100354</daqtaskid>\n" +
            "    <businessdata></businessdata>\n" +
            "  </business>\n" +
            "  <extendset></extendset>\n" +
            "</messages>";


    /*

    public static void main(String[] args) throws Exception_Exception, MalformedURLException, com.neusoft.si.entrance.webservice.Exception_Exception {
        try{
            DBEncrypt crypt = new DBEncrypt();
            FzjcxxDto fzjcxx1 = new FzjcxxDto();
            fzjcxx1.setXm("申双美");
            fzjcxx1.setXb("2");
            fzjcxx1.setXbmc("女性");
            fzjcxx1.setZjh("r5lTcXeIx8J7/ouNh+nAlN74/RT1LqgIBrUzsMXy5mI=");
            fzjcxx1.setFzjcid("fzjcid1111");
            fzjcxx1.setNl("24");
            fzjcxx1.setSyh("2021102600001");
            fzjcxx1.setJcxmid("3340E7DFD66D417DBE8387E1633ACAC4");
            fzjcxx1.setCjsj("2021-10-26 10:33:56");
            fzjcxx1.setSjrymc("陆辛萍");
            fzjcxx1.setJssj("2021-10-26 16:33:56");
            fzjcxx1.setJyrymc("姚嘉伟");
            if (fzjcxx1 != null){
                fzjcxx1.setZjh(crypt.dCode(fzjcxx1.getZjh()));
            }

            FzjcxxDto fzjcxx2 = new FzjcxxDto();
            fzjcxx2.setXm("洪敏政");
            fzjcxx2.setXb("1");
            fzjcxx2.setXbmc("男性");
            fzjcxx2.setZjh("y0R3MQaTiZcZphTTfROWD5sohatZ15wXfnHX7jNT1mQ=");
            fzjcxx2.setFzjcid("fzjcid222");
            fzjcxx2.setNl("27");
            fzjcxx2.setSyh("2021102600002");
            fzjcxx2.setJcxmid("3340E7DFD66D417DBE8387E1633ACAC4");
            fzjcxx2.setCjsj("2021-10-26 10:35:56");
            fzjcxx2.setSjrymc("陆辛萍");
            fzjcxx2.setJssj("2021-10-26 16:33:56");
            fzjcxx2.setJyrymc("姚嘉伟");
            if (fzjcxx2 != null){
                fzjcxx2.setZjh(crypt.dCode(fzjcxx2.getZjh()));
            }
            List<FzjcxxDto> list = new ArrayList<>();
            list.add(fzjcxx1);
            list.add(fzjcxx2);
            GZIPUtil gzipUtil = new GZIPUtil();
            String notesyz = examineReport(list);
            String notescg = normalReport(list);
            String notessq = applicationReport(list);
            System.out.println(notesyz);
            System.out.println(notescg);
            System.out.println(notessq);

            String compressStrCG = gzipUtil.compressEncode(notescg);
            String compressStrJY = gzipUtil.compressEncode(notesyz);
            String compressStrSQ = gzipUtil.compressEncode(notessq);
            System.out.println();

            //生成批次xml请求
            String totalXmljy = generateTotalXml(text,standardcode);
            String totalXmlcg = generateTotalXml(text,standardcodeCG);
            String totalXmlsq = generateTotalXml(text,standardcodeSQ);
            System.out.println("批次xml请求，检验==================================="+totalXmljy);
            System.out.println("批次xml请求，常规==================================="+totalXmlcg);
            System.out.println("批次xml请求，申请==================================="+totalXmlsq);
            //获取批次任务号服务
            String totalResponseJY = totalDeclare(totalXmljy);
            String totalResponseCG = totalDeclare(totalXmlcg);
            String totalResponseSQ = totalDeclare(totalXmlsq);
            System.out.println("批次xml响应，检验==================================="+totalResponseJY);
            System.out.println("批次xml响应，常规==================================="+totalResponseCG);
            System.out.println("批次xml响应，申请==================================="+totalResponseSQ);
            //解析xml获得批次任务号
            String totaltaskidJY = getTaskid(totalResponseJY);
            String totaltaskidCG = getTaskid(totalResponseCG);
            String totaltaskidSQ = getTaskid(totalResponseSQ);
            System.out.println(totaltaskidJY);
            System.out.println(totaltaskidCG);
            System.out.println(totaltaskidSQ);

//            for (int i=0; i<list.size(); i++){
                int k=0;

                while( k <= 3){
                    if (k==3){
//                        //============================单次请求(针对申请)========
//                        //生成单次xml请求
//                        String singleXml = generateSingleXml(text,totaltaskidSQ, Integer.toString(k), standardcodeSQ);
//                        System.out.println(singleXml);
//                        //获取单次任务号服务
//                        String singleResponse = singleDeclare(singleXml);
//                        System.out.println(singleResponse);
//                        //解析xml获取单次任务号
//                        String singletaskid = getTaskid(singleResponse);
//                        System.out.println(singletaskid);
//                        //===============================发送一次申请单=====
//                        //生成数据上传xml请求f
//                        String uploadXmlSQ = generateUploadXml(text, singletaskid, compressStrSQ, standardcodeSQ);
//                        System.out.println(uploadXmlSQ);
//                        //数据归集上传服务
//                        String uploadResponseSQ = uploadData(uploadXmlSQ);
//                        System.out.println(uploadResponseSQ);
//                        //解析获取响应xml返回的信息值
//                        String retcodeSQ = getRetValue(uploadResponseSQ);
//                        System.out.println(retcodeSQ);
                    }else if (k==2){
//                        //============================单次请求(针对常规)========
//                        //生成单次xml请求
//                        String singleXml = generateSingleXml(text,totaltaskidCG, Integer.toString(k), standardcodeCG);
//                        System.out.println(singleXml);
//                        //获取单次任务号服务
//                        String singleResponse = singleDeclare(singleXml);
//                        System.out.println(singleResponse);
//                        //解析xml获取单次任务号
//                        String singletaskid = getTaskid(singleResponse);
//                        System.out.println(singletaskid);
//                        //=============================发送一次常规单======
//                        //生成数据上传xml请求f
//                        String uploadXmlJY = generateUploadXml(text, singletaskid, compressStrJY, standardcodeCG);
//                        System.out.println(uploadXmlJY);
//
//                        //数据归集上传服务
//                        String uploadResponseJY = uploadData(uploadXmlJY);
//                        System.out.println(uploadResponseJY);
//                        //解析获取响应xml返回的信息值
//                        String retcodeJY = getRetValue(uploadResponseJY);
//                        System.out.println(retcodeJY);
                    }else if (k==1){
                        //============================单次请求(针对检验)========
                        //生成单次xml请求
                        String singleXml = generateSingleXml(text,totaltaskidJY, Integer.toString(k), standardcode);
                        System.out.println(singleXml);
                        //获取单次任务号服务
                        String singleResponse = singleDeclare(singleXml);
                        System.out.println(singleResponse);
                        //解析xml获取单次任务号
                        String singletaskid = getTaskid(singleResponse);
                        System.out.println(singletaskid);
                        //============================发送一次检验单======
                        //生成数据上传xml请求f
                        String uploadXmlJY = generateUploadXml(text, singletaskid, compressStrJY,standardcode);
                        System.out.println(uploadXmlJY);
                        //数据归集上传服务
                        String uploadResponseJY = uploadData(uploadXmlJY);
                        System.out.println(uploadResponseJY);
                        //解析获取响应xml返回的信息值
                        String retcodeJY = getRetValue(uploadResponseJY);
                        System.out.println(retcodeJY);
                    }
                    k++;
                }
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    private static String examineReport(List<FzjcxxDto> list) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<dmp></dmp>";
            String xmlstr = "";
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();
            Element datasets = rootElement.addElement("datasets");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            Element setcode = datasets.addElement("setcode");
            setcode.setText(standardcodebm);
            Element settype = datasets.addElement("settype");
            for (int i=0; i<list.size(); i++){
                Element setdetails = datasets.addElement("setdetails");

                Element BUSINESS_ID = setdetails.addElement("BUSINESS_ID");
                Element ORGANIZATION_CODE = setdetails.addElement("ORGANIZATION_CODE");
                Element UPDATE_DATE = setdetails.addElement("UPDATE_DATE");
                Element DATAGENERATE_DATE = setdetails.addElement("DATAGENERATE_DATE");
                Element ORGANIZATION_NAME = setdetails.addElement("ORGANIZATION_NAME");
                Element BASIC_ACTIVE_ID = setdetails.addElement("BASIC_ACTIVE_ID");
                Element RECORD_IDEN = setdetails.addElement("RECORD_IDEN");
                Element DOMAIN_CODE = setdetails.addElement("DOMAIN_CODE");
                Element SERIALNUM_ID = setdetails.addElement("SERIALNUM_ID");
                Element ARCHIVE_DATE = setdetails.addElement("ARCHIVE_DATE");
                Element CREATE_DATE = setdetails.addElement("CREATE_DATE");
                Element BATCH_NUM = setdetails.addElement("BATCH_NUM");
                Element LOCAL_ID = setdetails.addElement("LOCAL_ID");
                Element TASK_ID = setdetails.addElement("TASK_ID");
                /*本人姓名 +++ */
                Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
                WS02_01_039_001.setText(list.get(i).getXm());
                /*性别代码*/
                Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
                WS02_01_040_01.setText(list.get(i).getXb());
                /*性别名称*/
                Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
                CT02_01_040_01.setText(list.get(i).getXbmc());
                /*年龄(岁)*/
                Element WS02_01_026_01 = setdetails.addElement("WS02_01_026_01");
                WS02_01_026_01.setText(list.get(i).getNl());
                Element WS02_01_032_02 = setdetails.addElement("WS02_01_032_02");
                Element WS99_99_026_01 = setdetails.addElement("WS99_99_026_01");
                Element WS99_99_026_02 = setdetails.addElement("WS99_99_026_02");
                /*证件号码*/
                Element WS99_99_903_40 = setdetails.addElement("WS99_99_903_40");
                WS99_99_903_40.setText(list.get(i).getZjh());
                Element WS99_99_902_07 = setdetails.addElement("WS99_99_902_07");
                WS99_99_902_07.setText("99");
                Element CT99_99_902_07 = setdetails.addElement("CT99_99_902_07");
                Element WS02_01_060_01 = setdetails.addElement("WS02_01_060_01");
                WS02_01_060_01.setText("1");
                /*门诊*/
                Element CT02_01_060_01 = setdetails.addElement("CT02_01_060_01");
                CT02_01_060_01.setText("门诊");
                /*出生日期*/
                Element WS02_01_005_01_01 = setdetails.addElement("WS02_01_005_01_01");
                /*身份证件类别代码 +++ */
                Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
                WS02_01_031_01.setText("01");
                /*身份证件类别名称 +++ */
                Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
                CT02_01_031_01.setText("居民身份证");
                /*身份证件号码 +++ */
                Element WS02_01_030_01 = setdetails.addElement("WS02_01_030_01");
                WS02_01_030_01.setText(list.get(i).getZjh());
                /*机构唯一流水号  门（急）诊号 +++ */
                Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
                WS01_00_010_01.setText(StringUtil.generateUUID());
                /*机构唯一流水号 电子申请单编号 +++ */
                Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
                WS01_00_008_03.setText(StringUtil.generateUUID());
                /*机构唯一流水号 报告单编号 +++ */
                Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
                WS01_00_018_01.setText(StringUtil.generateUUID());
                /*新型冠状病毒RNA检测/新型冠状病毒抗体检测 报告单名称 +++ */
                Element WS01_00_900_02 = setdetails.addElement("WS01_00_900_02");
                WS01_00_900_02.setText("新型冠状病毒RNA检测");
                /*报告单类别代码 1临时报告  2最终报告 +++ */
                Element WS01_00_900_01 = setdetails.addElement("WS01_00_900_01");
                WS01_00_900_01.setText("2");
                Element CT01_00_900_01 = setdetails.addElement("CT01_00_900_01");
                CT01_00_900_01.setText("最终报告");
                /*  1/2（1:新型冠状病毒RNA检测，2:新型冠状病毒抗体检测） 申请项目代码 +++ */
                Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
                WS02_10_916_01.setText("1");
                /* 新型冠状病毒RNA检测/新型冠状病毒抗体检测 申请项目名称 +++ */
                Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
                WS02_10_916_02.setText("新型冠状病毒RNA检测");
                /*标本类别代码*/
                Element WS04_50_134_01 = setdetails.addElement("WS04_50_134_01");
                WS04_50_134_01.setText("32");//上呼吸道标本
                /*标本类别名称*/
                Element CT04_50_134_01 = setdetails.addElement("CT04_50_134_01");
                CT04_50_134_01.setText("上呼吸道标本");
                /*院内标本类别代码*/
                Element WS99_99_902_128 = setdetails.addElement("WS99_99_902_128");
                /*标本名称*/
                Element CT99_99_902_128 = setdetails.addElement("CT99_99_902_128");
                /*机构样本唯一流水号（按照某一特定编码规则赋予检查、检验标本的顺序号）*/
                Element WS01_00_003_01 = setdetails.addElement("WS01_00_003_01");
                WS01_00_003_01.setText(list.get(i).getSyh());//TODO
                Element WS04_50_135_01 = setdetails.addElement("WS04_50_135_01");
                /*标本部位（病理标本所取患者部位）*/
                Element WS04_30_904_01 = setdetails.addElement("WS04_30_904_01");
                WS04_30_904_01.setText("咽喉");//TODO 可能需要填口咽拭子中文名称，就是基础数据的名称
                /*标本采样日期时间*/
                Element WS04_50_137_01 = setdetails.addElement("WS04_50_137_01");
                Date cjsj = simpleDateFormat.parse(list.get(i).getCjsj());
                WS04_50_137_01.setText(    simpleDateFormat.format(cjsj)    );
                Element WS02_01_039_061 = setdetails.addElement("WS02_01_039_061");
                /*送检人员*/
                Element WS02_01_039_055 = setdetails.addElement("WS02_01_039_055");
                WS02_01_039_055.setText(list.get(i).getSjrymc());//TODO需要更换人员吗
                /*科室代码*/
                Element WS08_10_025_05 = setdetails.addElement("WS08_10_025_05");
                WS08_10_025_05.setText("D99");
                /*科室名称*/
                Element CT08_10_025_05 = setdetails.addElement("CT08_10_025_05");
                CT08_10_025_05.setText("其他科室");
                Element WS99_99_902_14 = setdetails.addElement("WS99_99_902_14");
                Element CT99_99_902_14 = setdetails.addElement("CT99_99_902_14");
                Element WS08_10_052_05 = setdetails.addElement("WS08_10_052_05");
                Element CT08_10_052_05 = setdetails.addElement("CT08_10_052_05");
                /*接收标本日期时间*/
                Element WS04_50_141_01 = setdetails.addElement("WS04_50_141_01");
                Date jssj = simpleDateFormat.parse(list.get(i).getJssj());
                WS04_50_141_01.setText(  simpleDateFormat.format(jssj)  );
                Element WS02_01_039_056 = setdetails.addElement("WS02_01_039_056");
                Element WS04_50_901_01 = setdetails.addElement("WS04_50_901_01");
                Element WS04_50_908_01 = setdetails.addElement("WS04_50_908_01");
                /*备注*/
                Element WS06_00_179_01 = setdetails.addElement("WS06_00_179_01");
                WS06_00_179_01.setText("无");
                Element WS02_01_039_065 = setdetails.addElement("WS02_01_039_065");
                /*检验人员*/
                Element WS02_01_039_173 = setdetails.addElement("WS02_01_039_173");
                WS02_01_039_173.setText(list.get(i).getJyrymc());
                /*检验日期时间 +++ */
                Element WS06_00_925_01 = setdetails.addElement("WS06_00_925_01");
                Date cjsjx = simpleDateFormat.parse(list.get(i).getCjsj());
                WS06_00_925_01.setText(  simpleDateFormat.format(cjsjx)   );
                Element WS02_01_039_049 = setdetails.addElement("WS02_01_039_049");
                /*报告日期时间*/
                Element WS99_99_906_01 = setdetails.addElement("WS99_99_906_01");
                Element WS99_99_030_04 = setdetails.addElement("WS99_99_030_04");
                /*审核人员*/
                Element WS02_01_039_057 = setdetails.addElement("WS02_01_039_057");
                WS02_01_039_057.setText("陆辛萍");//TODO
                /*与上传日期时间都为当天，且比上传时间晚*/
                Element WS06_00_927_01 = setdetails.addElement("WS06_00_927_01");
                Element WS06_00_926_01 = setdetails.addElement("WS06_00_926_01");
                Element WS08_10_025_03 = setdetails.addElement("WS08_10_025_03");
                Element CT08_10_025_03 = setdetails.addElement("CT08_10_025_03");
                Element WS99_99_902_98 = setdetails.addElement("WS99_99_902_98");
                Element CT99_99_902_98 = setdetails.addElement("CT99_99_902_98");
                /*报告机构代码*/
                Element WS08_10_052_03 = setdetails.addElement("WS08_10_052_03");
                WS08_10_052_03.setText("330110PDY10181417P1202");
                /*报告机构名称*/
                Element CT08_10_052_03 = setdetails.addElement("CT08_10_052_03");
                CT08_10_052_03.setText("杭州杰毅医学检验实验室有限公司");
                Element WS99_99_030_10 = setdetails.addElement("WS99_99_030_10");
                Element WS99_99_010_48 = setdetails.addElement("WS99_99_010_48");
                xmlstr = xmlstr + XmlUtil.getInstance().doc2String(document);
                xmlstr = xmlstr.split("\\n")[1];
                System.out.println();
            }
            return xmlstr;
        } catch (DocumentException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String normalReport(List<FzjcxxDto> list) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<dmp></dmp>";
            String xmlstr = "";
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();
            for (int i=0; i<list.size(); i++){
                Element datasets = rootElement.addElement("datasets");

                Element setcode = datasets.addElement("setcode");
                setcode.setText(standardcodebmCG);
                Element settype = datasets.addElement("settype");
                Element setdetails = datasets.addElement("setdetails");

                Element BUSINESS_ID = setdetails.addElement("BUSINESS_ID");
                Element ORGANIZATION_CODE = setdetails.addElement("ORGANIZATION_CODE");
                Element UPDATE_DATE = setdetails.addElement("UPDATE_DATE");
                Element DATAGENERATE_DATE = setdetails.addElement("DATAGENERATE_DATE");
                Element ORGANIZATION_NAME = setdetails.addElement("ORGANIZATION_NAME");
                Element BASIC_ACTIVE_ID = setdetails.addElement("BASIC_ACTIVE_ID");
                Element RECORD_IDEN = setdetails.addElement("RECORD_IDEN");
                Element DOMAIN_CODE = setdetails.addElement("DOMAIN_CODE");
                Element SERIALNUM_ID = setdetails.addElement("SERIALNUM_ID");
                Element ARCHIVE_DATE = setdetails.addElement("ARCHIVE_DATE");
                Element CREATE_DATE = setdetails.addElement("CREATE_DATE");
                Element BATCH_NUM = setdetails.addElement("BATCH_NUM");
                Element LOCAL_ID = setdetails.addElement("LOCAL_ID");
                Element TASK_ID = setdetails.addElement("TASK_ID");
                /*本人姓名 +++ */
                Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
                WS02_01_039_001.setText(list.get(i).getXm());
                /*性别代码 0未知的性别 1男性 2女性 9未说明的性别*/
                Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
                WS02_01_040_01.setText(list.get(i).getXb());
                /*性别名称*/
                Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
                CT02_01_040_01.setText(list.get(i).getXbmc());
                Element WS02_01_026_01 = setdetails.addElement("WS02_01_026_01");
                Element WS02_01_032_02 = setdetails.addElement("WS02_01_032_02");
                /*机构唯一代码*/
                Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
                WS01_00_010_01.setText(StringUtil.generateUUID());
                /*身份证件类别代码*/
                // 01居民身份证 02居民户口薄 03护照
                Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
                WS02_01_031_01.setText("01");//由于我们的证件类型都是基础数据的csid，但上传xml需要的是文档规定的数字
                /*身份证件类别名称*/
                Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
                CT02_01_031_01.setText("居民身份证");
                /*身份证号码*/
                Element WS02_01_906_01 = setdetails.addElement("WS02_01_906_01");
                WS02_01_906_01.setText(list.get(i).getZjh());
                /*机构唯一流水号*/
                Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
                WS01_00_008_03.setText(StringUtil.generateUUID());
                /*机构唯一流水号*/
                Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
                WS01_00_018_01.setText(StringUtil.generateUUID());
                /*项目序号（用于描述本条记录的排列顺序号码）? 这个如何理解 */
                Element WS01_00_907_01 = setdetails.addElement("WS01_00_907_01");
                WS01_00_907_01.setText(list.get(i).getFzjcid());//感觉不太对，这个是指的顺序===================
                /*1/2/3/4（1:新型冠状病毒核酸检测，2:新型冠状病毒抗体IgG，3:新型冠状病毒抗体IgM，4:新型冠状病毒总抗体）*/
                Element WS04_30_019_01 = setdetails.addElement("WS04_30_019_01");
                WS04_30_019_01.setText("1");//=============================
                /*新型冠状病毒核酸检测/新型冠状病毒抗体IgG/新型冠状病毒抗体IgM/新型冠状病毒总抗体*/
                Element WS04_30_020_01 = setdetails.addElement("WS04_30_020_01");
                WS04_30_020_01.setText("新型冠状病毒核酸检测");
                Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
                Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
                /*阴性/阳性*/
                Element WS04_30_009_03 = setdetails.addElement("WS04_30_009_03");
                WS04_30_009_03.setText("阴性");//=========================这个需要确定什么字段存的结果，结果代码写为中文
                Element WS05_01_904_02 = setdetails.addElement("WS05_01_904_02");
                Element WS05_10_926_01 = setdetails.addElement("WS05_10_926_01");
                Element WS04_30_009_06 = setdetails.addElement("WS04_30_009_06");
                Element WS04_30_023_04 = setdetails.addElement("WS04_30_023_04");
                Element WS04_30_023_05 = setdetails.addElement("WS04_30_023_05");
                Element WS04_30_016_01 = setdetails.addElement("WS04_30_016_01");
                Element WS04_50_913_01 = setdetails.addElement("WS04_50_913_01");
                Element WS04_50_902_01 = setdetails.addElement("WS04_50_902_01");
                Element WS04_10_903_01 = setdetails.addElement("WS04_10_903_01");
                Element WS02_10_027_01 = setdetails.addElement("WS02_10_027_01");
                /*报告机构代码*/
                Element WS08_10_052_03 = setdetails.addElement("WS08_10_052_03");
                WS08_10_052_03.setText("330110PDY10181417P1202");
                /*报告机构名称*/
                Element CT08_10_052_03 = setdetails.addElement("CT08_10_052_03");
                CT08_10_052_03.setText("杭州杰毅医学检验实验室有限公司");
                /*报告日期时间*/
                Element WS06_00_926_01 = setdetails.addElement("WS06_00_926_01");
                WS06_00_926_01.setText(   new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())    );
                Element WS99_99_010_48 = setdetails.addElement("WS99_99_010_48");

                xmlstr = xmlstr + XmlUtil.getInstance().doc2String(document);
                xmlstr = xmlstr.split("\\n")[1];
                System.out.println();
            }
            return xmlstr;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String applicationReport(List<FzjcxxDto> list) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<dmp></dmp>";
            String xmlstr = "";
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();
            for (int i=0; i<list.size(); i++){
                Element datasets = rootElement.addElement("datasets");

                Element setcode = datasets.addElement("setcode");
                setcode.setText(standardcodebmSQ);
                Element settype = datasets.addElement("settype");
                Element setdetails = datasets.addElement("setdetails");

                Element BUSINESS_ID = setdetails.addElement("BUSINESS_ID");
                Element ORGANIZATION_CODE = setdetails.addElement("ORGANIZATION_CODE");
                Element UPDATE_DATE = setdetails.addElement("UPDATE_DATE");
                Element DATAGENERATE_DATE = setdetails.addElement("DATAGENERATE_DATE");
                Element ORGANIZATION_NAME = setdetails.addElement("ORGANIZATION_NAME");
                Element BASIC_ACTIVE_ID = setdetails.addElement("BASIC_ACTIVE_ID");
                Element RECORD_IDEN = setdetails.addElement("RECORD_IDEN");
                Element DOMAIN_CODE = setdetails.addElement("DOMAIN_CODE");
                Element SERIALNUM_ID = setdetails.addElement("SERIALNUM_ID");
                Element ARCHIVE_DATE = setdetails.addElement("ARCHIVE_DATE");
                Element CREATE_DATE = setdetails.addElement("CREATE_DATE");
                Element BATCH_NUM = setdetails.addElement("BATCH_NUM");
                Element LOCAL_ID = setdetails.addElement("LOCAL_ID");
                Element TASK_ID = setdetails.addElement("TASK_ID");
                /*本人姓名*/
                Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
                WS02_01_039_001.setText(list.get(i).getXm());
                /*性别代码*/
                Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
                WS02_01_040_01.setText(list.get(i).getXb());
                /*性别名称*/
                Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
                CT02_01_040_01.setText(list.get(i).getXbmc());
                Element WS02_01_026_01 = setdetails.addElement("WS02_01_026_01");
                Element WS02_01_032_02 = setdetails.addElement("WS02_01_032_02");
                Element WS99_99_026_01 = setdetails.addElement("WS99_99_026_01");
                Element WS99_99_026_02 = setdetails.addElement("WS99_99_026_02");
                Element WS99_99_903_40 = setdetails.addElement("WS99_99_903_40");
                Element WS99_99_902_07 = setdetails.addElement("WS99_99_902_07");
                Element CT99_99_902_07 = setdetails.addElement("CT99_99_902_07");
                Element WS02_01_060_01 = setdetails.addElement("WS02_01_060_01");
                Element CT02_01_060_01 = setdetails.addElement("CT02_01_060_01");
                Element WS02_01_005_01_01 = setdetails.addElement("WS02_01_005_01_01");
                /*身份证件类别代码*/
                Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
                WS02_01_031_01.setText("01");
                /*身份证件类别名称*/
                Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
                CT02_01_031_01.setText("居民身份证");
                /*身份证号码*/
                Element WS02_01_030_01 = setdetails.addElement("WS02_01_030_01");
                WS02_01_030_01.setText(list.get(i).getZjh());
                Element WS02_01_010_01 = setdetails.addElement("WS02_01_010_01");
                /*机构唯一流水号*/
                Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
                WS01_00_010_01.setText(StringUtil.generateUUID());
                /*机构唯一流水号*/
                Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
                WS01_00_008_03.setText(StringUtil.generateUUID());
                Element WS01_00_906_01 = setdetails.addElement("WS01_00_906_01");
                /*机构唯一流水号*/
                Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
                WS01_00_018_01.setText(StringUtil.generateUUID());
                WS01_00_018_01.setText(StringUtil.generateUUID());
                Element WS05_01_024_01 = setdetails.addElement("WS05_01_024_01");
                Element CT05_01_024_01 = setdetails.addElement("CT05_01_024_01");
                /*院内疾病诊断代码（若院内没有，则写‘无’）*/
                Element WS99_99_902_09 = setdetails.addElement("WS99_99_902_09");
                WS99_99_902_09.setText("无");
                /*院内疾病诊断名称（若院内没有，则写‘无’）*/
                Element CT99_99_902_09 = setdetails.addElement("CT99_99_902_09");
                CT99_99_902_09.setText("无");
                /*作废标识  0未作废  1作废 */
                Element WS06_00_901_01 = setdetails.addElement("WS06_00_901_01");
                WS06_00_901_01.setText("0");
                /*项目序号（用于描述本条记录的排列顺序号码）*/
                Element WS01_00_907_01 = setdetails.addElement("WS01_00_907_01");
                WS01_00_907_01.setText(StringUtil.generateUUID());//===========================
                Element WS04_30_019_01 = setdetails.addElement("WS04_30_019_01");
                Element WS04_30_020_01 = setdetails.addElement("WS04_30_020_01");
                Element WS01_00_903_01 = setdetails.addElement("WS01_00_903_01");
                /*1/2（1:新型冠状病毒RNA检测，2:新型冠状病毒抗体检测）*/
                Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
                WS02_10_916_01.setText("1");
                /*新型冠状病毒RNA检测/新型冠状病毒抗体检测*/
                Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
                WS02_10_916_02.setText("新型冠状病毒RNA检测");
                /*申请人姓名*/
                Element WS02_01_039_097 = setdetails.addElement("WS02_01_039_097");
                WS02_01_039_097.setText(list.get(i).getXm());//todo
                Element WS99_99_030_03 = setdetails.addElement("WS99_99_030_03");
                /*申请日期时间*/
                Element WS06_00_917_01 = setdetails.addElement("WS06_00_917_01");
                //WS06_00_917_01.setText(   new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())    );
                /*D99*/
                Element WS08_10_025_02 = setdetails.addElement("WS08_10_025_02");
                WS08_10_025_02.setText("D99");
                /*其他科室*/
                Element CT08_10_025_02 = setdetails.addElement("CT08_10_025_02");
                CT08_10_025_02.setText("其他科室");
                Element WS99_99_902_95 = setdetails.addElement("WS99_99_902_95");
                Element CT99_99_902_95 = setdetails.addElement("CT99_99_902_95");
                /*申请机构代码*/
                Element WS08_10_052_02 = setdetails.addElement("WS08_10_052_02");
                WS08_10_052_02.setText("330110PDY10181417P1202");
                /*申请机构名称*/
                Element CT08_10_052_02 = setdetails.addElement("CT08_10_052_02");
                CT08_10_052_02.setText("杭州杰毅医学检验实验室有限公司");
                Element WS08_10_052_22 = setdetails.addElement("WS08_10_052_22");
                Element CT08_10_052_22 = setdetails.addElement("CT08_10_052_22");

                xmlstr = xmlstr + XmlUtil.getInstance().doc2String(document);
                xmlstr = xmlstr.split("\\n")[1];
                System.out.println();
            }
            return xmlstr;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    //常规报告
    private static String normalReport(FzjcxxDto fzjcxxDto) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<datasets></datasets>";
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();

            Element setcode = rootElement.addElement("setcode");
            setcode.setText(standardcodebmCG);
            Element settype = rootElement.addElement("settype");
            Element setdetails = rootElement.addElement("setdetails");

            Element BUSINESS_ID = setdetails.addElement("BUSINESS_ID");
            Element ORGANIZATION_CODE = setdetails.addElement("ORGANIZATION_CODE");
            Element UPDATE_DATE = setdetails.addElement("UPDATE_DATE");
            Element DATAGENERATE_DATE = setdetails.addElement("DATAGENERATE_DATE");
            Element ORGANIZATION_NAME = setdetails.addElement("ORGANIZATION_NAME");
            Element BASIC_ACTIVE_ID = setdetails.addElement("BASIC_ACTIVE_ID");
            Element RECORD_IDEN = setdetails.addElement("RECORD_IDEN");
            Element DOMAIN_CODE = setdetails.addElement("DOMAIN_CODE");
            Element SERIALNUM_ID = setdetails.addElement("SERIALNUM_ID");
            Element ARCHIVE_DATE = setdetails.addElement("ARCHIVE_DATE");
            Element CREATE_DATE = setdetails.addElement("CREATE_DATE");
            Element BATCH_NUM = setdetails.addElement("BATCH_NUM");
            Element LOCAL_ID = setdetails.addElement("LOCAL_ID");
            Element TASK_ID = setdetails.addElement("TASK_ID");
            /*本人姓名*/
            Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
            WS02_01_039_001.setText(fzjcxxDto.getXm());
            /*性别代码 0未知的性别 1男性 2女性 9未说明的性别*/
            Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
            WS02_01_040_01.setText(fzjcxxDto.getXb());
            /*性别名称*/
            Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
            CT02_01_040_01.setText(fzjcxxDto.getXbmc());
            Element WS02_01_026_01 = setdetails.addElement("WS02_01_026_01");
            Element WS02_01_032_02 = setdetails.addElement("WS02_01_032_02");
            /*机构唯一代码*/
            Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
            WS01_00_010_01.setText(StringUtil.generateUUID());
            /*身份证件类别代码*/
            // 01居民身份证 02居民户口薄 03护照
            Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
            WS02_01_031_01.setText("01");//由于我们的证件类型都是基础数据的csid，但上传xml需要的是文档规定的数字
            /*身份证件类别名称*/
            Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
            CT02_01_031_01.setText("居民身份证");
            /*身份证号码*/
            Element WS02_01_906_01 = setdetails.addElement("WS02_01_906_01");
            WS02_01_906_01.setText(fzjcxxDto.getZjh());
            /*机构唯一流水号*/
            Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
            WS01_00_008_03.setText(StringUtil.generateUUID());
            /*机构唯一流水号*/
            Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
            WS01_00_018_01.setText(StringUtil.generateUUID());
            /*项目序号（用于描述本条记录的排列顺序号码）? 这个如何理解 */
            Element WS01_00_907_01 = setdetails.addElement("WS01_00_907_01");
            WS01_00_907_01.setText(fzjcxxDto.getFzjcid());//感觉不太对，这个是指的顺序===================
            /*1/2/3/4（1:新型冠状病毒核酸检测，2:新型冠状病毒抗体IgG，3:新型冠状病毒抗体IgM，4:新型冠状病毒总抗体）*/
            Element WS04_30_019_01 = setdetails.addElement("WS04_30_019_01");
            WS04_30_019_01.setText("1");//=============================
            /*新型冠状病毒核酸检测/新型冠状病毒抗体IgG/新型冠状病毒抗体IgM/新型冠状病毒总抗体*/
            Element WS04_30_020_01 = setdetails.addElement("WS04_30_020_01");
            WS04_30_020_01.setText("新型冠状病毒核酸检测");
            Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
            Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
            /*阴性/阳性*/
            Element WS04_30_009_03 = setdetails.addElement("WS04_30_009_03");
            WS04_30_009_03.setText("阴性");//=========================这个需要确定什么字段存的结果，结果代码写为中文
            Element WS05_01_904_02 = setdetails.addElement("WS05_01_904_02");
            Element WS05_10_926_01 = setdetails.addElement("WS05_10_926_01");
            Element WS04_30_009_06 = setdetails.addElement("WS04_30_009_06");
            Element WS04_30_023_04 = setdetails.addElement("WS04_30_023_04");
            Element WS04_30_023_05 = setdetails.addElement("WS04_30_023_05");
            Element WS04_30_016_01 = setdetails.addElement("WS04_30_016_01");
            Element WS04_50_913_01 = setdetails.addElement("WS04_50_913_01");
            Element WS04_50_902_01 = setdetails.addElement("WS04_50_902_01");
            Element WS04_10_903_01 = setdetails.addElement("WS04_10_903_01");
            Element WS02_10_027_01 = setdetails.addElement("WS02_10_027_01");
            /*报告机构代码*/
            Element WS08_10_052_03 = setdetails.addElement("WS08_10_052_03");
            WS08_10_052_03.setText("330110PDY10181417P1202");
            /*报告机构名称*/
            Element CT08_10_052_03 = setdetails.addElement("CT08_10_052_03");
            CT08_10_052_03.setText("杭州杰毅医学检验实验室有限公司");
            /*报告日期时间*/
            Element WS06_00_926_01 = setdetails.addElement("WS06_00_926_01");
            WS06_00_926_01.setText(   new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())    );
            Element WS99_99_010_48 = setdetails.addElement("WS99_99_010_48");

            String xmlstr = XmlUtil.getInstance().doc2String(document);
            return xmlstr.split("\\n")[1];

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    //生成门急诊检验报告
    private static String examineReport(FzjcxxDto fzjcxxDto) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<datasets></datasets>";
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();

            Element setcode = rootElement.addElement("setcode");
            setcode.setText(standardcodebm);
            Element settype = rootElement.addElement("settype");
            Element setdetails = rootElement.addElement("setdetails");

            Element BUSINESS_ID = setdetails.addElement("BUSINESS_ID");
            Element ORGANIZATION_CODE = setdetails.addElement("ORGANIZATION_CODE");
            Element UPDATE_DATE = setdetails.addElement("UPDATE_DATE");
            Element DATAGENERATE_DATE = setdetails.addElement("DATAGENERATE_DATE");
            Element ORGANIZATION_NAME = setdetails.addElement("ORGANIZATION_NAME");
            Element BASIC_ACTIVE_ID = setdetails.addElement("BASIC_ACTIVE_ID");
            Element RECORD_IDEN = setdetails.addElement("RECORD_IDEN");
            Element DOMAIN_CODE = setdetails.addElement("DOMAIN_CODE");
            Element SERIALNUM_ID = setdetails.addElement("SERIALNUM_ID");
            Element ARCHIVE_DATE = setdetails.addElement("ARCHIVE_DATE");
            Element CREATE_DATE = setdetails.addElement("CREATE_DATE");
            Element BATCH_NUM = setdetails.addElement("BATCH_NUM");
            Element LOCAL_ID = setdetails.addElement("LOCAL_ID");
            Element TASK_ID = setdetails.addElement("TASK_ID");
            /*本人姓名*/
            Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
            WS02_01_039_001.setText(fzjcxxDto.getXm());
            /*性别代码*/
            Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
            WS02_01_040_01.setText(fzjcxxDto.getXb());
            /*性别名称*/
            Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
            CT02_01_040_01.setText(fzjcxxDto.getXbmc());
            /*年龄(岁)*/
            Element WS02_01_026_01 = setdetails.addElement("WS02_01_026_01");
            WS02_01_026_01.setText(fzjcxxDto.getNl());
            Element WS02_01_032_02 = setdetails.addElement("WS02_01_032_02");
            Element WS99_99_026_01 = setdetails.addElement("WS99_99_026_01");
            Element WS99_99_026_02 = setdetails.addElement("WS99_99_026_02");
            /*证件号码*/
            Element WS99_99_903_40 = setdetails.addElement("WS99_99_903_40");
            WS99_99_903_40.setText(fzjcxxDto.getZjh());
            Element WS99_99_902_07 = setdetails.addElement("WS99_99_902_07");
            Element CT99_99_902_07 = setdetails.addElement("CT99_99_902_07");
            Element WS02_01_060_01 = setdetails.addElement("WS02_01_060_01");
            WS02_01_060_01.setText("1");
            /*门诊*/
            Element CT02_01_060_01 = setdetails.addElement("CT02_01_060_01");
            CT02_01_060_01.setText("门诊");
            /*出生日期*/
            Element WS02_01_005_01_01 = setdetails.addElement("WS02_01_005_01_01");
            /*身份证件类别代码*/
            Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
            WS02_01_031_01.setText("01");
            /*身份证件类别名称*/
            Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
            CT02_01_031_01.setText("居民身份证");
            /*身份证件号码*/
            Element WS02_01_030_01 = setdetails.addElement("WS02_01_030_01");
            WS02_01_030_01.setText(fzjcxxDto.getZjh());
            /*机构唯一流水号  门（急）诊号*/
            Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
            WS01_00_010_01.setText(StringUtil.generateUUID());
            /*机构唯一流水号 电子申请单编号 */
            Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
            WS01_00_008_03.setText(StringUtil.generateUUID());
            /*机构唯一流水号 报告单编号 */
            Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
            WS01_00_018_01.setText(StringUtil.generateUUID());
            /*新型冠状病毒RNA检测/新型冠状病毒抗体检测*/
            Element WS01_00_900_02 = setdetails.addElement("WS01_00_900_02");
            WS01_00_900_02.setText("新型冠状病毒RNA检测");
            /*报告单类别代码 1临时报告  2最终报告*/
            Element WS01_00_900_01 = setdetails.addElement("WS01_00_900_01");
            WS01_00_900_01.setText("1");
            Element CT01_00_900_01 = setdetails.addElement("CT01_00_900_01");
            CT01_00_900_01.setText("临时报告");
            /*  1/2（1:新型冠状病毒RNA检测，2:新型冠状病毒抗体检测）  */
            Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
            WS02_10_916_01.setText("1");
            /* 新型冠状病毒RNA检测/新型冠状病毒抗体检测 */
            Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
            WS02_10_916_02.setText("新型冠状病毒RNA检测");
            /*标本类别代码*/
            Element WS04_50_134_01 = setdetails.addElement("WS04_50_134_01");
            /*标本类别名称*/
            Element CT04_50_134_01 = setdetails.addElement("CT04_50_134_01");
            /*院内标本类别代码*/
            Element WS99_99_902_128 = setdetails.addElement("WS99_99_902_128");
            /*标本名称*/
            Element CT99_99_902_128 = setdetails.addElement("CT99_99_902_128");
            /*机构样本唯一流水号（按照某一特定编码规则赋予检查、检验标本的顺序号）*/
            Element WS01_00_003_01 = setdetails.addElement("WS01_00_003_01");
            Element WS04_50_135_01 = setdetails.addElement("WS04_50_135_01");
            /*标本部位（病理标本所取患者部位）*/
            Element WS04_30_904_01 = setdetails.addElement("WS04_30_904_01");
            /*标本采样日期时间*/
            Element WS04_50_137_01 = setdetails.addElement("WS04_50_137_01");
            Element WS02_01_039_061 = setdetails.addElement("WS02_01_039_061");
            /*送检人员*/
            Element WS02_01_039_055 = setdetails.addElement("WS02_01_039_055");
            /*科室代码*/
            Element WS08_10_025_05 = setdetails.addElement("WS08_10_025_05");
            /*科室名称*/
            Element CT08_10_025_05 = setdetails.addElement("CT08_10_025_05");
            Element WS99_99_902_14 = setdetails.addElement("WS99_99_902_14");
            Element CT99_99_902_14 = setdetails.addElement("CT99_99_902_14");
            Element WS08_10_052_05 = setdetails.addElement("WS08_10_052_05");
            Element CT08_10_052_05 = setdetails.addElement("CT08_10_052_05");
            /*接收标本日期时间*/
            Element WS04_50_141_01 = setdetails.addElement("WS04_50_141_01");
            Element WS02_01_039_056 = setdetails.addElement("WS02_01_039_056");
            Element WS04_50_901_01 = setdetails.addElement("WS04_50_901_01");
            Element WS04_50_908_01 = setdetails.addElement("WS04_50_908_01");
            /*备注*/
            Element WS06_00_179_01 = setdetails.addElement("WS06_00_179_01");
            Element WS02_01_039_065 = setdetails.addElement("WS02_01_039_065");
            /*检验人员*/
            Element WS02_01_039_173 = setdetails.addElement("WS02_01_039_173");
            /*检验日期时间*/
            Element WS06_00_925_01 = setdetails.addElement("WS06_00_925_01");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date cjsj = simpleDateFormat.parse(fzjcxxDto.getCjsj());
            WS06_00_925_01.setText(  simpleDateFormat.format(cjsj)   );
            Element WS02_01_039_049 = setdetails.addElement("WS02_01_039_049");
            /*报告日期时间*/
            Element WS99_99_906_01 = setdetails.addElement("WS99_99_906_01");
            Element WS99_99_030_04 = setdetails.addElement("WS99_99_030_04");
            /*审核人员*/
            Element WS02_01_039_057 = setdetails.addElement("WS02_01_039_057");
            /*与上传日期时间都为当天，且比上传时间晚*/
            Element WS06_00_927_01 = setdetails.addElement("WS06_00_927_01");
            Element WS06_00_926_01 = setdetails.addElement("WS06_00_926_01");
            Element WS08_10_025_03 = setdetails.addElement("WS08_10_025_03");
            Element CT08_10_025_03 = setdetails.addElement("CT08_10_025_03");
            Element WS99_99_902_98 = setdetails.addElement("WS99_99_902_98");
            Element CT99_99_902_98 = setdetails.addElement("CT99_99_902_98");
            /*报告机构代码*/
            Element WS08_10_052_03 = setdetails.addElement("WS08_10_052_03");
            WS08_10_052_03.setText("330110PDY10181417P1202");
            /*报告机构名称*/
            Element CT08_10_052_03 = setdetails.addElement("CT08_10_052_03");
            CT08_10_052_03.setText("杭州杰毅医学检验实验室有限公司");
            Element WS99_99_030_10 = setdetails.addElement("WS99_99_030_10");
            Element WS99_99_010_48 = setdetails.addElement("WS99_99_010_48");

            String xmlstr = XmlUtil.getInstance().doc2String(document);
            return xmlstr.split("\\n")[1];
        } catch (DocumentException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    //申请单
    private static String applicationReport(FzjcxxDto fzjcxxDto) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<datasets></datasets>";
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();

            Element setcode = rootElement.addElement("setcode");
            setcode.setText(standardcodebmSQ);
            Element settype = rootElement.addElement("settype");
            Element setdetails = rootElement.addElement("setdetails");

            Element BUSINESS_ID = setdetails.addElement("BUSINESS_ID");
            Element ORGANIZATION_CODE = setdetails.addElement("ORGANIZATION_CODE");
            Element UPDATE_DATE = setdetails.addElement("UPDATE_DATE");
            Element DATAGENERATE_DATE = setdetails.addElement("DATAGENERATE_DATE");
            Element ORGANIZATION_NAME = setdetails.addElement("ORGANIZATION_NAME");
            Element BASIC_ACTIVE_ID = setdetails.addElement("BASIC_ACTIVE_ID");
            Element RECORD_IDEN = setdetails.addElement("RECORD_IDEN");
            Element DOMAIN_CODE = setdetails.addElement("DOMAIN_CODE");
            Element SERIALNUM_ID = setdetails.addElement("SERIALNUM_ID");
            Element ARCHIVE_DATE = setdetails.addElement("ARCHIVE_DATE");
            Element CREATE_DATE = setdetails.addElement("CREATE_DATE");
            Element BATCH_NUM = setdetails.addElement("BATCH_NUM");
            Element LOCAL_ID = setdetails.addElement("LOCAL_ID");
            Element TASK_ID = setdetails.addElement("TASK_ID");
            /*本人姓名*/
            Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
            WS02_01_039_001.setText(fzjcxxDto.getXm());
            /*性别代码*/
            Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
            /*性别名称*/
            Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
            Element WS02_01_026_01 = setdetails.addElement("WS02_01_026_01");
            Element WS02_01_032_02 = setdetails.addElement("WS02_01_032_02");
            Element WS99_99_026_01 = setdetails.addElement("WS99_99_026_01");
            Element WS99_99_026_02 = setdetails.addElement("WS99_99_026_02");
            Element WS99_99_903_40 = setdetails.addElement("WS99_99_903_40");
            Element WS99_99_902_07 = setdetails.addElement("WS99_99_902_07");
            Element CT99_99_902_07 = setdetails.addElement("CT99_99_902_07");
            Element WS02_01_060_01 = setdetails.addElement("WS02_01_060_01");
            Element CT02_01_060_01 = setdetails.addElement("CT02_01_060_01");
            Element WS02_01_005_01_01 = setdetails.addElement("WS02_01_005_01_01");
            /*身份证件类别代码*/
            Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
            WS02_01_031_01.setText("01");
            /*身份证件类别名称*/
            Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
            CT02_01_031_01.setText("居民身份证");
            /*身份证号码*/
            Element WS02_01_030_01 = setdetails.addElement("WS02_01_030_01");
            WS02_01_030_01.setText(fzjcxxDto.getZjh());
            Element WS02_01_010_01 = setdetails.addElement("WS02_01_010_01");
            /*机构唯一流水号*/
            Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
            WS01_00_010_01.setText(StringUtil.generateUUID());
            /*机构唯一流水号*/
            Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
            WS01_00_008_03.setText(StringUtil.generateUUID());
            Element WS01_00_906_01 = setdetails.addElement("WS01_00_906_01");
            /*机构唯一流水号*/
            Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
            WS01_00_018_01.setText(StringUtil.generateUUID());
            WS01_00_018_01.setText(StringUtil.generateUUID());
            Element WS05_01_024_01 = setdetails.addElement("WS05_01_024_01");
            Element CT05_01_024_01 = setdetails.addElement("CT05_01_024_01");
            /*院内疾病诊断代码（若院内没有，则写‘无’）*/
            Element WS99_99_902_09 = setdetails.addElement("WS99_99_902_09");
            WS99_99_902_09.setText("无");
            /*院内疾病诊断名称（若院内没有，则写‘无’）*/
            Element CT99_99_902_09 = setdetails.addElement("CT99_99_902_09");
            CT99_99_902_09.setText("无");
            /*作废标识  0未作废  1作废 */
            Element WS06_00_901_01 = setdetails.addElement("WS06_00_901_01");
            WS06_00_901_01.setText("0");
            /*项目序号（用于描述本条记录的排列顺序号码）*/
            Element WS01_00_907_01 = setdetails.addElement("WS01_00_907_01");
            WS01_00_907_01.setText(StringUtil.generateUUID());//===========================
            Element WS04_30_019_01 = setdetails.addElement("WS04_30_019_01");
            Element WS04_30_020_01 = setdetails.addElement("WS04_30_020_01");
            Element WS01_00_903_01 = setdetails.addElement("WS01_00_903_01");
            /*1/2（1:新型冠状病毒RNA检测，2:新型冠状病毒抗体检测）*/
            Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
            WS02_10_916_01.setText("1");
            /*新型冠状病毒RNA检测/新型冠状病毒抗体检测*/
            Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
            WS02_10_916_02.setText("新型冠状病毒RNA检测");
            /*申请人姓名*/
            Element WS02_01_039_097 = setdetails.addElement("WS02_01_039_097");
            Element WS99_99_030_03 = setdetails.addElement("WS99_99_030_03");
            /*申请日期时间*/
            Element WS06_00_917_01 = setdetails.addElement("WS06_00_917_01");
            WS06_00_917_01.setText(   new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())    );
            /*D99*/
            Element WS08_10_025_02 = setdetails.addElement("WS08_10_025_02");
            /*其他科室*/
            Element CT08_10_025_02 = setdetails.addElement("CT08_10_025_02");
            Element WS99_99_902_95 = setdetails.addElement("WS99_99_902_95");
            Element CT99_99_902_95 = setdetails.addElement("CT99_99_902_95");
            /*申请机构代码*/
            Element WS08_10_052_02 = setdetails.addElement("WS08_10_052_02");
            WS08_10_052_02.setText("330110PDY10181417P1202");
            /*申请机构名称*/
            Element CT08_10_052_02 = setdetails.addElement("CT08_10_052_02");
            CT08_10_052_02.setText("杭州杰毅医学检验实验室有限公司");
            Element WS08_10_052_22 = setdetails.addElement("WS08_10_052_22");
            Element CT08_10_052_22 = setdetails.addElement("CT08_10_052_22");

            String xmlstr = XmlUtil.getInstance().doc2String(document);
            return xmlstr.split("\\n")[1];
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    //解析获取响应xml返回的信息值
    private static String getRetValue(String uploadResponse) {
        String retcode = "";
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(uploadResponse);
            Element rootElement = document.getRootElement();
            Element business = rootElement.element("business");
            List<Element> businessElements = business.elements();
            for (Element element : businessElements){
                if ("returnmessage".equals(element.getName())){
                    Element retcodeEle = element.element("retcode");
                    retcode = retcodeEle.getText();
                }
            }
            return retcode;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    //解析xml获取任务号
    private static String getTaskid(String totalResponse) {
        String taskid = "";
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(totalResponse);
            Element rootElement = document.getRootElement();
            Element business = rootElement.element("business");
            List<Element> businessElements = business.elements();
            for (Element element : businessElements){
                if ("businessdata".equals(element.getName())){
                    Element taskidEle = element.element("taskid");
                    taskid = taskidEle.getText();
                }
            }
            return taskid;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    //发送生成批次服务号xml请求，获取响应批次任务号
    private static String generateTotalXml(String text, String standardcode) {
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(text);
            Element rootElement = document.getRootElement();
            Element switchset = rootElement.element("switchset");
            Element serviceinf = switchset.element("serviceinf");
            Element servicecode = serviceinf.element("servicecode");
            servicecode.setText(pccode);
            Element business = rootElement.element("business");
            List<Element> businessElements = business.elements();
            for (Element element : businessElements){
                if ("businessdata".equals(element.getName())){
                    Element declaretype = element.addElement("declaretype");
                    declaretype.setText("0");

                    Element collecttype = element.addElement("collecttype");
                    collecttype.setText("0");

                    Element totaldeclare = element.addElement("totaldeclare");
                    Element colrescode = totaldeclare.addElement("colrescode");
                    colrescode.setText(standardcode);
                    Element tasknum = totaldeclare.addElement("tasknum");
                    tasknum.setText("5");
                    Element begindatetime = totaldeclare.addElement("begindatetime");
                    begindatetime.setText(  new SimpleDateFormat("yyyyMMdd").format(new Date())+"000000" );
                    Element enddatetime = totaldeclare.addElement("enddatetime");
                    enddatetime.setText(  new SimpleDateFormat("yyyyMMdd").format(new Date())+"235959" );
                    Element tdeclare = totaldeclare.addElement("tdeclare");

                    Element setcode = tdeclare.addElement("setcode");
                    setcode.setText(standardcode.substring(4));
                    Element datanum = tdeclare.addElement("datanum");
                    datanum.setText("5000");
                }
            }
            return XmlUtil.getInstance().doc2String(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    //发送生成单次服务号xml请求，获取单次服务号
    private static String generateSingleXml(String text, String taskid, String xh, String standardcode) {
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(text);
            Element rootElement = document.getRootElement();
            Element switchset = rootElement.element("switchset");
            Element serviceinf = switchset.element("serviceinf");
            Element servicecode = serviceinf.element("servicecode");
            servicecode.setText(dccode);
            Element business = rootElement.element("business");
            List<Element> businessElements = business.elements();
            for (Element element : businessElements){
                if ("businessdata".equals(element.getName())){
                    Element declaretype = element.addElement("declaretype");
                    declaretype.setText("1");

                    Element singledeclare = element.addElement("singledeclare");
                    Element totaltaskid = singledeclare.addElement("totaltaskid");
                    totaltaskid.setText(taskid);  //====================这个是批次任务号
                    Element colrescode = singledeclare.addElement("colrescode");
                    colrescode.setText(standardcode);
                    Element sn = singledeclare.addElement("sn");
                    sn.setText(xh);//================某批次的第几个单次任务号

                    Element declare = singledeclare.addElement("declare");
                    Element setcode = declare.addElement("setcode");
                    setcode.setText(standardcode.substring(4));
                    Element datanum = declare.addElement("datanum");
                    datanum.setText("1000");
                }
            }
            return XmlUtil.getInstance().doc2String(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    //发送上传数据集，获取接收成功信息
    private static String generateUploadXml(String text, String taskid, String data ,String standardcode) {
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(text);
            Element rootElement = document.getRootElement();
            Element switchset = rootElement.element("switchset");
            Element serviceinf = switchset.element("serviceinf");
            Element servicecode = serviceinf.element("servicecode");
            servicecode.setText(sccode);
            Element business = rootElement.element("business");
            List<Element> businessElements = business.elements();
            for (Element element : businessElements){
                if ("businessdata".equals(element.getName())){
                    element.setText(data);
                }else if ("datacompress".equals(element.getName())){
                    element.setText("1");
                }else if ("daqtaskid".equals(element.getName())){
                    element.setText(taskid);//==========================单次任务号
                }else if ("standardcode".equals(element.getName())){
                    element.setText(standardcode);
                }else if ("requestset".equals(element.getName())){
                    Element reqcondition = element.element("reqcondition");
                    Element condition = reqcondition.element("condition");
                    Element collecttype = condition.addElement("collecttype");
                    collecttype.setText("0");
                }
            }
            return XmlUtil.getInstance().doc2String(document);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    //发送数据归集上传请求xml，得到响应
    private static String uploadData(String xmlrequest) throws com.neusoft.si.entrance.webservice.Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CarryXmlToDbService.class);
        factory.setAddress("http://192.26.64.134:8889/SJCJSJSCJKQ/SJSCQ");     //192.26.64.134:8889/SJCJSJSCJKQ/SJSCQ?wsdl
        // 需要服务接口文件                                                             //<daqtaskid>20180601020100354</daqtaskid>
        CarryXmlToDbService client = (CarryXmlToDbService) factory.create();
        String result = client.handle(xmlrequest);
        return result;
    }
    //发送获取单次任务号服务请求xml，得到响应
    private static String singleDeclare(String xmlrequest) throws Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CollectDeclareService.class);
        factory.setAddress("http://192.26.64.134:8889/SJCJHQRWHJKQ/HQDCRWHQ");     //192.26.64.134:8889/SJCJHQRWHJKQ/HQDCRWHQ?wsdl
        // 需要服务接口文件                                                             //<daqtaskid>20180601020100354</daqtaskid>
        CollectDeclareService client = (CollectDeclareService) factory.create();
        String result = client.singleDeclare(xmlrequest);
        return result;
    }
    //发送获取批次任务号服务请求xml，得到响应
    private static String totalDeclare(String xmlrequest) throws Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CollectDeclareService.class);
        factory.setAddress("http://192.26.64.134:8889/SJCJHQRWHJKQ/PCRWHHQQ");
        // 需要服务接口文件
        CollectDeclareService client = (CollectDeclareService) factory.create();
        String result = client.totalDeclare(xmlrequest);
        return result;
    }


    //=============================生成三个检验单的压缩编码xml========
//                String str = normalReport(fzjcxx1);
//                String compressStrCG = gzipUtil.compressEncode(str);
//                System.out.println("常规报告"+str);
//
//                String str1 = examineReport(fzjcxx1);
//                String compressStrJY = gzipUtil.compressEncode(str1);
//                System.out.println("检验报告"+str1);
//
//                String str2 = applicationReport(fzjcxx1);
//                String compressStrSQ = gzipUtil.compressEncode(str2);
//                System.out.println("申请告"+str2);
//============================单次请求===========================================
////        生成单次xml请求
//            String singleXml = generateSingleXml(text,totaltaskid);
//            System.out.println(singleXml);
////        获取单次任务号服务
//            String singleResponse = singleDeclare(singleXml);
//            System.out.println(singleResponse);
////        解析xml获取单次任务号
//            String singletaskid = getTaskid(singleResponse);
//            System.out.println(singletaskid);


////=============================生成xml================================================
//            String str = normalReport(fzjcxx1);
//            String compressStrCG = gzipUtil.compressEncode(str);
//            System.out.println("常规报告"+str);
//
//            String str1 = examineReport(fzjcxx1);
//            String compressStrJY = gzipUtil.compressEncode(str1);
//            System.out.println("检验报告"+str1);
//
//            String str2 = applicationReport(fzjcxx1);
//            String compressStrSQ = gzipUtil.compressEncode(str2);
//            System.out.println("申请告"+str2);
////============================发送一次检验单==================================
////        String data = "i have a dream";
////        生成数据上传xml请求f
//            String uploadXmlCG = generateUploadXml(text, singletaskid, compressStrCG);
//            System.out.println(uploadXmlCG);
////        数据归集上传服务
//            String uploadResponseCG = uploadData(uploadXmlCG);
//            System.out.println(uploadResponseCG);
////        解析获取响应xml返回的信息值
//            String retcodeCG = getRetValue(uploadResponseCG);
//            System.out.println(retcodeCG);
////=============================发送一次常规单=================================
////        String data = "i have a dream";
////        生成数据上传xml请求f
//            String uploadXmlJY = generateUploadXml(text, singletaskid, compressStrJY);
//            System.out.println(uploadXmlJY);
//
////        数据归集上传服务
//            String uploadResponseJY = uploadData(uploadXmlJY);
//            System.out.println(uploadResponseJY);
////        解析获取响应xml返回的信息值
//            String retcodeJY = getRetValue(uploadResponseJY);
//            System.out.println(retcodeJY);
////===============================发送一次申请单===============================
////        String data = "i have a dream";
////        生成数据上传xml请求f
//            String uploadXmlSQ = generateUploadXml(text, singletaskid, compressStrSQ);
//            System.out.println(uploadXmlSQ);
////        数据归集上传服务
//            String uploadResponseSQ = uploadData(uploadXmlSQ);
//            System.out.println(uploadResponseSQ);
////        解析获取响应xml返回的信息值
//            String retcodeSQ = getRetValue(uploadResponseSQ);
//            System.out.println(retcodeSQ);


//        String data = "i have a dream";
////        生成数据上传xml请求f
//        String uploadXml = generateUploadXml(text, singletaskid, data);
//        System.out.println(uploadXml);
////        数据归集上传服务
//        String uploadResponse = uploadData(uploadXml);
//        System.out.println(uploadResponse);
////        解析获取响应xml返回的信息值
//        String retcode = getRetValue(uploadResponse);
//        System.out.println(retcode);

}
