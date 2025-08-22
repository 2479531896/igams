package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.post.IFzjcxxDao;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.gzip.GZIPUtil;
import com.matridx.springboot.util.xml.XmlUtil;
import com.neusoft.si.entrance.webservice.CarryXmlToDbService;
import com.neusoft.si.entrance.webservice.collectdeclareservice.CollectDeclareService;
import com.neusoft.si.entrance.webservice.collectdeclareservice.Exception_Exception;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadReportToWjw {

    private final Logger log = LoggerFactory.getLogger(UploadReportToWjw.class);


    private List<FzjcxxDto> fzjcxxlist;
    private IFzjcxxDao fzjcxxDao;
    User operator;
    private String pc_address;
    private String dc_address;
    private String data_submit;

    public void init(List<FzjcxxDto> fzjcxxlist, IFzjcxxDao fzjcxxDao, User operator, String pc_address, String dc_address,String data_submit){
        this.fzjcxxDao=fzjcxxDao;
        this.fzjcxxlist=fzjcxxlist;
        this.operator=operator;
        this.pc_address=pc_address;
        this.dc_address=dc_address;
        this.data_submit=data_submit;
    }

    public void dockHealthInterface() {
        log.error("--上传卫健委--新冠报告上传卫健委线程开始-----");

        try{
            //一次数据上传到上级平台需要注意
            // 假设上传一批数据，这批数据需要对应三个单子：检验，常规，申请单；先将一批数据生成符合规则的xml多个数据节点，将这些数据进行一次gzip压缩才能放到上传接口的内容中
            // 这一批数据需要对应三个批次号，三个单次号，三个上传号（检验对检验，申请对申请，比如申请的批次号只能给申请的单次号，申请的单次号只能给申请的上传号）
            Map<String,Object> map = new HashMap<>();
            map.put("fzjcxxlist",fzjcxxlist);
            GZIPUtil gzipUtil = new GZIPUtil();
            String notessq = applicationReport(fzjcxxlist,operator);
            log.error("notessq:============================"+notessq);
            String notesyz = examineReport(fzjcxxlist,operator);
            log.error("notesyz:============================"+notesyz);
            String notescg = normalReport(fzjcxxlist);
            log.error("notescg:============================"+notescg);

            String compressStrCG = gzipUtil.compressEncode(notescg);
            String compressStrJY = gzipUtil.compressEncode(notesyz);
            String compressStrSQ = gzipUtil.compressEncode(notessq);

            //生成批次xml请求
            String standardcode = "REQ.C0101.0303.02";
            String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
            String totalXmljy = generateTotalXml(text, standardcode);
            String standardcodeCG = "REQ.C0101.0303.0201";
            String totalXmlcg = generateTotalXml(text, standardcodeCG);
            String standardcodeSQ = "REQ.C0101.0303.01";
            String totalXmlsq = generateTotalXml(text, standardcodeSQ);
            log.error("批次xml请求，检验==================================="+totalXmljy);
            log.error("批次xml请求，常规==================================="+totalXmlcg);
            log.error("批次xml请求，申请==================================="+totalXmlsq);
            //获取批次任务号服务
            String totalResponseJY = totalDeclare(totalXmljy);
            String totalResponseCG = totalDeclare(totalXmlcg);
            String totalResponseSQ = totalDeclare(totalXmlsq);
            log.error("批次xml响应，检验==================================="+totalResponseJY);
            log.error("批次xml响应，常规==================================="+totalResponseCG);
            log.error("批次xml响应，申请==================================="+totalResponseSQ);

            //判断获取批次服务号是否成功，成功继续执行，失败就抛出异常
            String pcTotalRetJY = getRetValue(totalResponseJY);
            if("-1".equals(pcTotalRetJY)){
                throw new BusinessException("msg","检验标准获取批次服务号失败！");
            }
            String pcTotalRetCG = getRetValue(totalResponseCG);
            if("-1".equals(pcTotalRetCG)){
                throw new BusinessException("msg","常规检验标准获取批次服务号失败！");
            }
            String pcTotalRetSQ = getRetValue(totalResponseSQ);
            if("-1".equals(pcTotalRetSQ)){
                throw new BusinessException("msg","申请标准获取批次服务号失败！");
            }

            //解析xml获得批次任务号
            String totaltaskidJY = getTaskid(totalResponseJY);
            String totaltaskidCG = getTaskid(totalResponseCG);
            String totaltaskidSQ = getTaskid(totalResponseSQ);
            log.error("批次任务号解析，检验==================================="+totaltaskidJY);
            log.error("批次任务号解析，常规==================================="+totaltaskidCG);
            log.error("批次任务号解析，申请==================================="+totaltaskidSQ);
            String pctaskid = totaltaskidSQ+"sq" +","+ totaltaskidJY+"jy" +","+ totaltaskidCG+"cg" ;
            map.put("pctaskid",pctaskid);


            int k=1;
            String singletaskidSQ="";
            String singletaskidJY="";
            String singletaskidCG="";

            while( k <= 3){
                if (k==3){
                    //============================单次请求(针对申请)========
                    //生成单次xml请求
                    String singleXml = generateSingleXml(text,totaltaskidSQ, Integer.toString(k), standardcodeSQ);
                    log.error("申请--单次请求xml============================="+singleXml);
                    //获取单次任务号服务
                    String singleResponse = singleDeclare(singleXml);
                    log.error("申请--单次响应xml============================="+singleResponse);
                    //判断获取单次服务号是否成功，成功继续执行，失败抛出异常
                    String dcSingleRetSQ = getRetValue(singleResponse);
                    if("-1".equals(dcSingleRetSQ)){
                        throw new BusinessException("msg","申请标准获取单次服务号失败！");
                    }
                    //解析xml获取单次任务号
                    String singletaskid = getTaskid(singleResponse);
                    singletaskidSQ = singletaskid+"sq";
                    log.error("申请--解析出单次任务号============================="+singletaskid);
                    //===============================发送一次申请单=====
                    //生成数据上传xml请求f
                    String uploadXmlSQ = generateUploadXml(text, singletaskid, compressStrSQ, standardcodeSQ);
                    log.error("申请--上传生成请求xml============================="+uploadXmlSQ);
                    //数据归集上传服务
                    String uploadResponseSQ = uploadData(uploadXmlSQ);
                    log.error("申请--上传响应xml============================="+uploadResponseSQ);
                    //解析获取响应xml返回的信息值
                    String retcodeSQ = getRetValue(uploadResponseSQ);
                    if ("-1".equals(retcodeSQ)){
                        throw new BusinessException("msg","申请标准上传核酸检测数据失败！");
                    }
                }else if (k==2){
                    //============================单次请求(针对常规)========
                    //生成单次xml请求
                    String singleXml = generateSingleXml(text,totaltaskidCG, Integer.toString(k), standardcodeCG);
                    log.error("常规--单次请求xml============================="+singleXml);
                    //获取单次任务号服务
                    String singleResponse = singleDeclare(singleXml);
                    log.error("常规--单次响应xml============================="+singleResponse);
                    //判断获取单次服务号是否成功，成功继续执行，失败抛出异常
                    String dcSingleRetCG = getRetValue(singleResponse);
                    if("-1".equals(dcSingleRetCG)){
                        throw new BusinessException("msg","常规标准获取单次服务号失败！");
                    }
                    //解析xml获取单次任务号
                    String singletaskid = getTaskid(singleResponse);
                    singletaskidCG = singletaskid+"cg";
                    log.error("常规--解析出单次任务号============================="+singletaskid);
                    //=============================发送一次常规单======
                    //生成数据上传xml请求
                    String uploadXmlJY = generateUploadXml(text, singletaskid, compressStrCG, standardcodeCG);
                    log.error("常规--上传生成请求xml============================="+uploadXmlJY);
                    //数据归集上传服务
                    String uploadResponseJY = uploadData(uploadXmlJY);
                    log.error("常规--上传响应xml============================="+uploadResponseJY);
                    //解析获取响应xml返回的信息值
                    String retcodeCG = getRetValue(uploadResponseJY);
                    if ("-1".equals(retcodeCG)){
                        throw new BusinessException("msg","常规标准上传核酸检测数据失败！");
                    }
                    System.out.println(retcodeCG);
                }else if (k==1){
                    //============================单次请求(针对检验)========
                    //生成单次xml请求
                    String singleXml = generateSingleXml(text,totaltaskidJY, Integer.toString(k), standardcode);
                    log.error("检验--单次请求xml============================="+singleXml);
                    //获取单次任务号服务
                    String singleResponse = singleDeclare(singleXml);
                    log.error("检验--单次响应xml============================="+singleResponse);
                    //判断获取单次服务号是否成功，成功继续执行，失败抛出异常
                    String dcSingleRetJY = getRetValue(singleResponse);
                    if("-1".equals(dcSingleRetJY)){
                        throw new BusinessException("msg","检验标准获取单次服务号失败！");
                    }
                    //解析xml获取单次任务号
                    String singletaskid = getTaskid(singleResponse);
                    singletaskidJY = singletaskid+"jy";
                    log.error("检验--解析出单次任务号============================="+singletaskid);
                    //============================发送一次检验单======
                    //生成数据上传xml请求
                    String uploadXmlJY = generateUploadXml(text, singletaskid, compressStrJY, standardcode);
                    log.error("检验--上传生成请求xml============================="+uploadXmlJY);
                    //数据归集上传服务
                    String uploadResponseJY = uploadData(uploadXmlJY);
                    log.error("检验--上传响应xml============================="+uploadResponseJY);
                    //解析获取响应xml返回的信息值
                    String retcodeJY = getRetValue(uploadResponseJY);
                    if ("-1".equals(retcodeJY)){
                        throw new BusinessException("msg","检验标准上传核酸检测数据失败！");
                    }
                    System.out.println(retcodeJY);
                }
                k++;
            }
            String dctaskid = singletaskidSQ +","+ singletaskidJY +","+ singletaskidCG;
            map.put("dctaskid",dctaskid);
            fzjcxxDao.updateTaskid(map);//更新单次任务号和批次任务号
            //更新卫建上报成功时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createdate = sdf.format(date);
            map.put("sbsj",createdate);
            fzjcxxDao.updateSbsj(map);
        } catch (Exception e) {
            log.error("dockHealthInterface报错========================================",e);
            e.printStackTrace();
        }
    }

    private String applicationReport(List<FzjcxxDto> list, User operator) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<dmp></dmp>";
            StringBuilder xmlstr = new StringBuilder();
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();
            Element datasets = rootElement.addElement("datasets");

            Element setcode = datasets.addElement("setcode");
            String standardcodebmSQ = "C0101.0303.01";
            setcode.setText(standardcodebmSQ);
            datasets.addElement("settype");

            for (FzjcxxDto fzjcxxDto : list) {
                Element setdetails = datasets.addElement("setdetails");

                setdetails.addElement("BUSINESS_ID");
                setdetails.addElement("ORGANIZATION_CODE");
                setdetails.addElement("UPDATE_DATE");
                setdetails.addElement("DATAGENERATE_DATE");
                setdetails.addElement("ORGANIZATION_NAME");
                setdetails.addElement("BASIC_ACTIVE_ID");
                setdetails.addElement("RECORD_IDEN");
                setdetails.addElement("DOMAIN_CODE");
                setdetails.addElement("SERIALNUM_ID");
                setdetails.addElement("ARCHIVE_DATE");
                setdetails.addElement("CREATE_DATE");
                setdetails.addElement("BATCH_NUM");
                setdetails.addElement("LOCAL_ID");
                setdetails.addElement("TASK_ID");
                /*本人姓名  +++  */
                Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
                WS02_01_039_001.setText(fzjcxxDto.getXm());
                /*性别代码*/
                Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
                WS02_01_040_01.setText(fzjcxxDto.getXb());
                /*性别名称*/
                Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
                CT02_01_040_01.setText(fzjcxxDto.getXbmc());
                setdetails.addElement("WS02_01_026_01");
                setdetails.addElement("WS02_01_032_02");
                setdetails.addElement("WS99_99_026_01");
                setdetails.addElement("WS99_99_026_02");
                setdetails.addElement("WS99_99_903_40");
                setdetails.addElement("WS99_99_902_07");
                setdetails.addElement("CT99_99_902_07");
                setdetails.addElement("WS02_01_060_01");
                setdetails.addElement("CT02_01_060_01");
                //出生日期
                Element WS02_01_005_01_01 = setdetails.addElement("WS02_01_005_01_01");
                String d_zjh = fzjcxxDto.getZjh();
                if (StringUtil.isNotBlank(d_zjh)) {
                    String zjh = crypt.dCode(d_zjh);
                    if (StringUtil.isNotBlank(d_zjh) && zjh.length() >= 14)
                        WS02_01_005_01_01.setText(zjh.substring(6, 14));
                }
                /*身份证件类别代码  +++  */
                Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
//                WS02_01_031_01.setText("01");
                /*身份证件类别名称  +++  */
                Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
//                CT02_01_031_01.setText("居民身份证");
                if (fzjcxxDto.getZjlxmc().contains("身份证")) {
                    WS02_01_031_01.setText("01");
                    CT02_01_031_01.setText("居民身份证");
                } else if (fzjcxxDto.getZjlxmc().contains("护照")) {
                    WS02_01_031_01.setText("03");
                    CT02_01_031_01.setText("护照");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居住证")) {
                    WS02_01_031_01.setText("08");
                    CT02_01_031_01.setText("港澳台居民居住证");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居住证")) {
                    WS02_01_031_01.setText("08");
                    CT02_01_031_01.setText("港澳台居民居住证");
                } else if (fzjcxxDto.getZjlxmc().contains("外国人永久居住证")) {
                    WS02_01_031_01.setText("99");
                    CT02_01_031_01.setText("其他");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居民来往内地通行证")) {
                    WS02_01_031_01.setText("06");
                    CT02_01_031_01.setText("港澳居民来往内地通行证");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居民来往内地通行证")) {
                    WS02_01_031_01.setText("07");
                    CT02_01_031_01.setText("台湾居民来往内地通行证");
                }
                /*身份证号码  +++  */
                Element WS02_01_030_01 = setdetails.addElement("WS02_01_030_01");
                WS02_01_030_01.setText(crypt.dCode(fzjcxxDto.getZjh()));
                setdetails.addElement("WS02_01_010_01");
                /*机构唯一流水号 门急诊号  +++ */
                Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
                WS01_00_010_01.setText("MZ" + fzjcxxDto.getBbzbh());
                /*机构唯一流水号 电子申请单编号  +++  */
                Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
                WS01_00_008_03.setText("SQ" + fzjcxxDto.getBbzbh());
                setdetails.addElement("WS01_00_906_01");
                /*机构唯一流水号 报告单编号 +++ */
                Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
                WS01_00_018_01.setText("BG" + fzjcxxDto.getBbzbh());
                setdetails.addElement("WS05_01_024_01");
                setdetails.addElement("CT05_01_024_01");
                /*院内疾病诊断代码（若院内没有，则写‘无’） +++ */
                Element WS99_99_902_09 = setdetails.addElement("WS99_99_902_09");
                WS99_99_902_09.setText("无");
                /*院内疾病诊断名称（若院内没有，则写‘无’）  +++  */
                Element CT99_99_902_09 = setdetails.addElement("CT99_99_902_09");
                CT99_99_902_09.setText("无");
                /*作废标识  0未作废  1作废  +++  */
                Element WS06_00_901_01 = setdetails.addElement("WS06_00_901_01");
                WS06_00_901_01.setText("0");
                /*项目序号（用于描述本条记录的排列顺序号码）  +++  */
                Element WS01_00_907_01 = setdetails.addElement("WS01_00_907_01");
                String[] syh = fzjcxxDto.getSyh().split("-");
                WS01_00_907_01.setText(syh[0] + syh[1]);
                setdetails.addElement("WS04_30_019_01");
                setdetails.addElement("WS04_30_020_01");
                setdetails.addElement("WS01_00_903_01");
                /*1/2（1:新型冠状病毒RNA检测，2:新型冠状病毒抗体检测）  +++  */
                Element WS02_10_916_01 = setdetails.addElement("WS02_10_916_01");
                WS02_10_916_01.setText("1");
                /*新型冠状病毒RNA检测/新型冠状病毒抗体检测  +++  */
                Element WS02_10_916_02 = setdetails.addElement("WS02_10_916_02");
                WS02_10_916_02.setText("新型冠状病毒RNA检测");
                /*申请人姓名*/
                Element WS02_01_039_097 = setdetails.addElement("WS02_01_039_097");
                WS02_01_039_097.setText(operator.getZsxm());
                setdetails.addElement("WS99_99_030_03");
                /*申请日期时间  +++  */
                Element WS06_00_917_01 = setdetails.addElement("WS06_00_917_01");
                WS06_00_917_01.setText(fzjcxxDto.getSqdsj());
                /*D99*/
                Element WS08_10_025_02 = setdetails.addElement("WS08_10_025_02");
                WS08_10_025_02.setText("D99");
                /*其他科室*/
                Element CT08_10_025_02 = setdetails.addElement("CT08_10_025_02");
                CT08_10_025_02.setText("其他科室");
                setdetails.addElement("WS99_99_902_95");
                setdetails.addElement("CT99_99_902_95");
                /*申请机构代码  +++ */
                Element WS08_10_052_02 = setdetails.addElement("WS08_10_052_02");
                WS08_10_052_02.setText("330110PDY10181417P1202");
                /*申请机构名称  +++ */
                Element CT08_10_052_02 = setdetails.addElement("CT08_10_052_02");
                CT08_10_052_02.setText("杭州杰毅医学检验实验室有限公司");
                setdetails.addElement("WS08_10_052_22");
                setdetails.addElement("CT08_10_052_22");

                xmlstr.append(XmlUtil.getInstance().doc2String(document));
                xmlstr = new StringBuilder(xmlstr.toString().split("\\n")[1]);
            }
            return xmlstr.toString();
        } catch (DocumentException e) {
            log.error("applicationReport报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    private String examineReport(List<FzjcxxDto> list, User operator) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<dmp></dmp>";
            StringBuilder xmlstr = new StringBuilder();
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();
            Element datasets = rootElement.addElement("datasets");
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            Element setcode = datasets.addElement("setcode");
            String standardcodebm = "C0101.0303.02";
            setcode.setText(standardcodebm);
            datasets.addElement("settype");
            for (FzjcxxDto fzjcxxDto : list) {
                Element setdetails = datasets.addElement("setdetails");
                setdetails.addElement("BUSINESS_ID");
                setdetails.addElement("ORGANIZATION_CODE");
                setdetails.addElement("UPDATE_DATE");
                setdetails.addElement("DATAGENERATE_DATE");
                setdetails.addElement("ORGANIZATION_NAME");
                setdetails.addElement("BASIC_ACTIVE_ID");
                setdetails.addElement("RECORD_IDEN");
                setdetails.addElement("DOMAIN_CODE");
                setdetails.addElement("SERIALNUM_ID");
                setdetails.addElement("ARCHIVE_DATE");
                setdetails.addElement("CREATE_DATE");
                setdetails.addElement("BATCH_NUM");
                setdetails.addElement("LOCAL_ID");
                setdetails.addElement("TASK_ID");
                /*本人姓名 +++ */
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
                if (StringUtil.isNotBlank(fzjcxxDto.getNl()))
                    WS02_01_026_01.setText(fzjcxxDto.getNl());
                setdetails.addElement("WS02_01_032_02");
                setdetails.addElement("WS99_99_026_01");
                setdetails.addElement("WS99_99_026_02");
                /*证件号码*/
                Element WS99_99_903_40 = setdetails.addElement("WS99_99_903_40");
                WS99_99_903_40.setText(crypt.dCode(fzjcxxDto.getZjh()));
                Element WS99_99_902_07 = setdetails.addElement("WS99_99_902_07");
                WS99_99_902_07.setText("99");
                setdetails.addElement("CT99_99_902_07");
                Element WS02_01_060_01 = setdetails.addElement("WS02_01_060_01");
                WS02_01_060_01.setText("4");
                /*门诊*/
                Element CT02_01_060_01 = setdetails.addElement("CT02_01_060_01");
                CT02_01_060_01.setText("其他");
                /*出生日期*/
                Element WS02_01_005_01_01 = setdetails.addElement("WS02_01_005_01_01");

                String d_zjh = fzjcxxDto.getZjh();
                if (StringUtil.isNotBlank(d_zjh)) {
                    String zjh = crypt.dCode(d_zjh);
                    if (StringUtil.isNotBlank(d_zjh) && zjh.length() >= 14)
                        WS02_01_005_01_01.setText(zjh.substring(6, 14));
                }

                //WS02_01_005_01_01.setText(  crypt.dCode(list.get(i).getZjh()).substring(6,14)  );
                /*身份证件类别代码 +++ */
                Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
//                WS02_01_031_01.setText("01");
                /*身份证件类别名称 +++ */
                Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
                if (fzjcxxDto.getZjlxmc().contains("身份证")) {
                    WS02_01_031_01.setText("01");
                    CT02_01_031_01.setText("居民身份证");
                } else if (fzjcxxDto.getZjlxmc().contains("护照")) {
                    WS02_01_031_01.setText("03");
                    CT02_01_031_01.setText("护照");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居住证")) {
                    WS02_01_031_01.setText("08");
                    CT02_01_031_01.setText("港澳台居民居住证");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居住证")) {
                    WS02_01_031_01.setText("08");
                    CT02_01_031_01.setText("港澳台居民居住证");
                } else if (fzjcxxDto.getZjlxmc().contains("外国人永久居住证")) {
                    WS02_01_031_01.setText("99");
                    CT02_01_031_01.setText("其他");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居民来往内地通行证")) {
                    WS02_01_031_01.setText("06");
                    CT02_01_031_01.setText("港澳居民来往内地通行证");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居民来往内地通行证")) {
                    WS02_01_031_01.setText("07");
                    CT02_01_031_01.setText("台湾居民来往内地通行证");
                }
                /*身份证件号码 +++ */
                Element WS02_01_030_01 = setdetails.addElement("WS02_01_030_01");
                WS02_01_030_01.setText(crypt.dCode(fzjcxxDto.getZjh()));
                /*机构唯一流水号  门（急）诊号 +++ */
                Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
                WS01_00_010_01.setText("MZ" + fzjcxxDto.getBbzbh());
                /*机构唯一流水号 电子申请单编号 +++ */
                Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
                WS01_00_008_03.setText("SQ" + fzjcxxDto.getBbzbh());
                /*机构唯一流水号 报告单编号 +++ */
                Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
                WS01_00_018_01.setText("BG" + fzjcxxDto.getBbzbh());
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
                WS99_99_902_128.setText("32");//上呼吸道标本
                /*标本名称*/
                Element CT99_99_902_128 = setdetails.addElement("CT99_99_902_128");
                CT99_99_902_128.setText("上呼吸道标本");
                /*机构样本唯一流水号（按照某一特定编码规则赋予检查、检验标本的顺序号）*/
                Element WS01_00_003_01 = setdetails.addElement("WS01_00_003_01");
                String[] syh = fzjcxxDto.getSyh().split("-");
                WS01_00_003_01.setText(syh[0] + syh[1]);
                setdetails.addElement("WS04_50_135_01");
                /*标本部位（病理标本所取患者部位）*/
                Element WS04_30_904_01 = setdetails.addElement("WS04_30_904_01");
                WS04_30_904_01.setText("上呼吸道");
                /*标本采样日期时间*/
                Element WS04_50_137_01 = setdetails.addElement("WS04_50_137_01");
                WS04_50_137_01.setText(fzjcxxDto.getCjsj());
                setdetails.addElement("WS02_01_039_061");
                /*送检人员*/
                setdetails.addElement("WS02_01_039_055");
                /*科室代码*/
                Element WS08_10_025_05 = setdetails.addElement("WS08_10_025_05");
                WS08_10_025_05.setText("D99");
                /*科室名称*/
                Element CT08_10_025_05 = setdetails.addElement("CT08_10_025_05");
                CT08_10_025_05.setText("其他科室");
                setdetails.addElement("WS99_99_902_14");
                setdetails.addElement("CT99_99_902_14");
                setdetails.addElement("WS08_10_052_05");
                setdetails.addElement("CT08_10_052_05");
                /*接收标本日期时间*/
                Element WS04_50_141_01 = setdetails.addElement("WS04_50_141_01");
//                Date jssj = simpleDateFormat.parse(list.get(i).getJssj());   simpleDateFormat.format(jssj)
                WS04_50_141_01.setText(fzjcxxDto.getJssj());
                setdetails.addElement("WS02_01_039_056");
                setdetails.addElement("WS04_50_901_01");
                setdetails.addElement("WS04_50_908_01");
                /*备注*/
                setdetails.addElement("WS06_00_179_01");
                setdetails.addElement("WS02_01_039_065");
                /*检验人员*/
                Element WS02_01_039_173 = setdetails.addElement("WS02_01_039_173");
                WS02_01_039_173.setText(fzjcxxDto.getSyrymc());
                /*检验日期时间 +++ */
                Element WS06_00_925_01 = setdetails.addElement("WS06_00_925_01");
//                Date sysj = simpleDateFormat.parse(  list.get(i).getSysj() );
                WS06_00_925_01.setText(fzjcxxDto.getSysj());
                setdetails.addElement("WS02_01_039_049");
                /*报告医生身份证号*/
                setdetails.addElement("WS99_99_906_01");
                setdetails.addElement("WS99_99_030_04");
                /*审核人员*/
                Element WS02_01_039_057 = setdetails.addElement("WS02_01_039_057");
                WS02_01_039_057.setText(operator.getZsxm());
                /*审核日期 与上传日期时间都为当天，且比上传时间晚*/
                Element WS06_00_927_01 = setdetails.addElement("WS06_00_927_01");
                WS06_00_927_01.setText(fzjcxxDto.getFssj());
                /*报告日期*/
                Element WS06_00_926_01 = setdetails.addElement("WS06_00_926_01");
//                Date bgrq = simpleDateFormat.parse(  list.get(i).getBgrq() );
                WS06_00_926_01.setText(fzjcxxDto.getBgrq());
                setdetails.addElement("WS08_10_025_03");
                setdetails.addElement("CT08_10_025_03");
                setdetails.addElement("WS99_99_902_98");
                setdetails.addElement("CT99_99_902_98");
                /*报告机构代码*/
                Element WS08_10_052_03 = setdetails.addElement("WS08_10_052_03");
                WS08_10_052_03.setText("330110PDY10181417P1202");
                /*报告机构名称*/
                Element CT08_10_052_03 = setdetails.addElement("CT08_10_052_03");
                CT08_10_052_03.setText("杭州杰毅医学检验实验室有限公司");
                setdetails.addElement("WS99_99_030_10");
                setdetails.addElement("WS99_99_010_48");
                xmlstr.append(XmlUtil.getInstance().doc2String(document));
                xmlstr = new StringBuilder(xmlstr.toString().split("\\n")[1]);
            }
            return xmlstr.toString();
        } catch (DocumentException e) {
            log.error("examineReport报错=====================================================",e);
//            e.printStackTrace();
        }
        return null;
    }
    private String normalReport(List<FzjcxxDto> list) {
        try {
            DBEncrypt crypt = new DBEncrypt();
            String text = "<dmp></dmp>";
            StringBuilder xmlstr = new StringBuilder();
            Document document = DocumentHelper.parseText(text);
            Element rootElement = document.getRootElement();
            Element datasets = rootElement.addElement("datasets");
            Element setcode = datasets.addElement("setcode");
            String standardcodebmCG = "C0101.0303.0201";
            setcode.setText(standardcodebmCG);
            datasets.addElement("settype");
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            for (FzjcxxDto fzjcxxDto : list) {
                Element setdetails = datasets.addElement("setdetails");

                setdetails.addElement("BUSINESS_ID");
                setdetails.addElement("ORGANIZATION_CODE");
                setdetails.addElement("UPDATE_DATE");
                setdetails.addElement("DATAGENERATE_DATE");
                setdetails.addElement("ORGANIZATION_NAME");
                setdetails.addElement("BASIC_ACTIVE_ID");
                setdetails.addElement("RECORD_IDEN");
                setdetails.addElement("DOMAIN_CODE");
                setdetails.addElement("SERIALNUM_ID");
                setdetails.addElement("ARCHIVE_DATE");
                setdetails.addElement("CREATE_DATE");
                setdetails.addElement("BATCH_NUM");
                setdetails.addElement("LOCAL_ID");
                setdetails.addElement("TASK_ID");
                /*本人姓名 +++ */
                Element WS02_01_039_001 = setdetails.addElement("WS02_01_039_001");
                WS02_01_039_001.setText(fzjcxxDto.getXm());
                /*性别代码 0未知的性别 1男性 2女性 9未说明的性别*/
                Element WS02_01_040_01 = setdetails.addElement("WS02_01_040_01");
                WS02_01_040_01.setText(fzjcxxDto.getXb());
                /*性别名称*/
                Element CT02_01_040_01 = setdetails.addElement("CT02_01_040_01");
                CT02_01_040_01.setText(fzjcxxDto.getXbmc());
                setdetails.addElement("WS02_01_026_01");
                setdetails.addElement("WS02_01_032_02");
                /*机构唯一代码 门急诊号 +++  */
                Element WS01_00_010_01 = setdetails.addElement("WS01_00_010_01");
                WS01_00_010_01.setText("MZ" + fzjcxxDto.getBbzbh());
                /*身份证件类别代码  +++ */
                // 01居民身份证 02居民户口薄 03护照
                Element WS02_01_031_01 = setdetails.addElement("WS02_01_031_01");
//                WS02_01_031_01.setText("01");
                /*身份证件类别名称 +++ */
                Element CT02_01_031_01 = setdetails.addElement("CT02_01_031_01");
//                CT02_01_031_01.setText("居民身份证");
                if (fzjcxxDto.getZjlxmc().contains("身份证")) {
                    WS02_01_031_01.setText("01");
                    CT02_01_031_01.setText("居民身份证");
                } else if (fzjcxxDto.getZjlxmc().contains("护照")) {
                    WS02_01_031_01.setText("03");
                    CT02_01_031_01.setText("护照");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居住证")) {
                    WS02_01_031_01.setText("08");
                    CT02_01_031_01.setText("港澳台居民居住证");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居住证")) {
                    WS02_01_031_01.setText("08");
                    CT02_01_031_01.setText("港澳台居民居住证");
                } else if (fzjcxxDto.getZjlxmc().contains("外国人永久居住证")) {
                    WS02_01_031_01.setText("99");
                    CT02_01_031_01.setText("其他");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居民来往内地通行证")) {
                    WS02_01_031_01.setText("06");
                    CT02_01_031_01.setText("港澳居民来往内地通行证");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居民来往内地通行证")) {
                    WS02_01_031_01.setText("07");
                    CT02_01_031_01.setText("台湾居民来往内地通行证");
                }
                /*身份证号码 +++ */
                Element WS02_01_906_01 = setdetails.addElement("WS02_01_906_01");
                WS02_01_906_01.setText(crypt.dCode(fzjcxxDto.getZjh()));
                /*机构唯一流水号 电子申请单编号 +++ */
                Element WS01_00_008_03 = setdetails.addElement("WS01_00_008_03");
                WS01_00_008_03.setText("SQ" + fzjcxxDto.getBbzbh());
                /*机构唯一流水号 报告单编号 +++ */
                Element WS01_00_018_01 = setdetails.addElement("WS01_00_018_01");
                WS01_00_018_01.setText("BG" + fzjcxxDto.getBbzbh());
                /*项目序号（用于描述本条记录的排列顺序号码）? 这个如何理解 +++ */
                Element WS01_00_907_01 = setdetails.addElement("WS01_00_907_01");
                String[] syh = fzjcxxDto.getSyh().split("-");
                WS01_00_907_01.setText(syh[0] + syh[1]);
                /*1/2/3/4（1:新型冠状病毒核酸检测，2:新型冠状病毒抗体IgG，3:新型冠状病毒抗体IgM，4:新型冠状病毒总抗体）+++ */
                Element WS04_30_019_01 = setdetails.addElement("WS04_30_019_01");
                WS04_30_019_01.setText("1");//=============================
                /*新型冠状病毒核酸检测/新型冠状病毒抗体IgG/新型冠状病毒抗体IgM/新型冠状病毒总抗体 +++ */
                Element WS04_30_020_01 = setdetails.addElement("WS04_30_020_01");
                WS04_30_020_01.setText("新型冠状病毒核酸检测");
                setdetails.addElement("WS02_10_916_01");
                setdetails.addElement("WS02_10_916_02");
                /*阴性/阳性/混管可疑阳性----说明：其中单管和混管阴性传“阴性”；单管阳性传“阳性”；混管阳性传“混管可疑阳性”*/
                Element WS04_30_009_03 = setdetails.addElement("WS04_30_009_03");
                String jcjgmc;
                if ("阳性".equals(fzjcxxDto.getJcjgmc())) {
                    jcjgmc = "阳性";
                } else if ("阴性".equals(fzjcxxDto.getJcjgmc())) {
                    jcjgmc = "阴性";
                } else {
                    jcjgmc = "混管可疑阳性";
                }
                WS04_30_009_03.setText(jcjgmc);
                setdetails.addElement("WS05_01_904_02");
                setdetails.addElement("WS05_10_926_01");
                setdetails.addElement("WS04_30_009_06");
                setdetails.addElement("WS04_30_023_04");
                setdetails.addElement("WS04_30_023_05");
                setdetails.addElement("WS04_30_016_01");
                setdetails.addElement("WS04_50_913_01");
                setdetails.addElement("WS04_50_902_01");
                setdetails.addElement("WS04_10_903_01");
                setdetails.addElement("WS02_10_027_01");
                /*报告机构代码 +++ */
                Element WS08_10_052_03 = setdetails.addElement("WS08_10_052_03");
                WS08_10_052_03.setText("330110PDY10181417P1202");
                /*报告机构名称  +++ */
                Element CT08_10_052_03 = setdetails.addElement("CT08_10_052_03");
                CT08_10_052_03.setText("杭州杰毅医学检验实验室有限公司");
                /*报告日期时间  +++  */
                Element WS06_00_926_01 = setdetails.addElement("WS06_00_926_01");
//                Date bgrq = simpleDateFormat.parse(list.get(i).getBgrq());
                WS06_00_926_01.setText(fzjcxxDto.getBgrq());
                setdetails.addElement("WS99_99_010_48");

                xmlstr.append(XmlUtil.getInstance().doc2String(document));
                xmlstr = new StringBuilder(xmlstr.toString().split("\\n")[1]);
            }
            return xmlstr.toString();
        } catch (DocumentException e) {
            log.error("normalReport报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    //发送生成批次服务号xml请求，获取响应批次任务号
    private String generateTotalXml(String text, String standardcode) {
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(text);
            Element rootElement = document.getRootElement();
            Element switchset = rootElement.element("switchset");
            Element serviceinf = switchset.element("serviceinf");
            Element servicecode = serviceinf.element("servicecode");
            String pccode = "XBSJCJCJQ:PCRWHHQQ";
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
            log.error("generateTotalXml报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    //发送获取批次任务号服务请求xml，得到响应
    private String totalDeclare(String xmlrequest) throws Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CollectDeclareService.class);
        factory.setAddress(pc_address);
//        factory.setAddress("http://192.26.64.134:8889/SJCJHQRWHJKQ/PCRWHHQQ");
        // 需要服务接口文件
        CollectDeclareService client = (CollectDeclareService) factory.create();
        return client.totalDeclare(xmlrequest);
    }

    //解析获取响应xml返回的信息值
    private String getRetValue(String uploadResponse) {
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
            log.error("getRetValue报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    //解析xml获取任务号
    private String getTaskid(String totalResponse) {
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
            log.error("getTaskid报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    //发送生成单次服务号xml请求，获取单次服务号
    private String generateSingleXml(String text, String taskid, String xh, String standardcode) {
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(text);
            Element rootElement = document.getRootElement();
            Element switchset = rootElement.element("switchset");
            Element serviceinf = switchset.element("serviceinf");
            Element servicecode = serviceinf.element("servicecode");
            String dccode = "XBSJCJCJQ:HQDCRWHQ";
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
            log.error("generateSingleXml报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    //发送上传数据集，获取接收成功信息
    private String generateUploadXml(String text, String taskid, String data ,String standardcode) {
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(text);
            Element rootElement = document.getRootElement();
            Element switchset = rootElement.element("switchset");
            Element serviceinf = switchset.element("serviceinf");
            Element servicecode = serviceinf.element("servicecode");
            String sccode = "XBSJCJCJQ:SJSCQ";
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
            log.error("generateUploadXml报错=====================================================",e);
            //e.printStackTrace();
        }
        return null;
    }

    //发送获取单次任务号服务请求xml，得到响应
    private String singleDeclare(String xmlrequest) throws Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CollectDeclareService.class);
        factory.setAddress(dc_address);
//        factory.setAddress("http://192.26.64.134:8889/SJCJHQRWHJKQ/HQDCRWHQ");     //192.26.64.134:8889/SJCJHQRWHJKQ/HQDCRWHQ?wsdl
        // 需要服务接口文件                                                             //<daqtaskid>20180601020100354</daqtaskid>
        CollectDeclareService client = (CollectDeclareService) factory.create();
        return client.singleDeclare(xmlrequest);
    }

    //发送数据归集上传请求xml，得到响应
    private String uploadData(String xmlrequest) throws com.neusoft.si.entrance.webservice.Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CarryXmlToDbService.class);
        factory.setAddress(data_submit);
//        factory.setAddress("http://192.26.64.134:8889/SJCJSJSCJKQ/SJSCQ");     //192.26.64.134:8889/SJCJSJSCJKQ/SJSCQ?wsdl
        // 需要服务接口文件                                                             //<daqtaskid>20180601020100354</daqtaskid>
        CarryXmlToDbService client = (CarryXmlToDbService) factory.create();
        return client.handle(xmlrequest);
    }
}
