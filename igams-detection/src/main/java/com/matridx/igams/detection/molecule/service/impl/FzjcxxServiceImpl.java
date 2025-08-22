package com.matridx.igams.detection.molecule.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.post.IJcsjDao;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.file.AttachHelper;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.detection.molecule.dao.entities.*;
import com.matridx.igams.detection.molecule.dao.post.IFzjcxmDao;
import com.matridx.igams.detection.molecule.dao.post.IFzjcxxDao;
import com.matridx.igams.detection.molecule.dao.post.IHzxxDao;
import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.igams.detection.molecule.enums.NUONUOEnum;
import com.matridx.igams.detection.molecule.service.svcinterface.*;
import com.matridx.igams.detection.molecule.util.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.gzip.GZIPUtil;
import com.matridx.springboot.util.verify.CheckIdCard;
import com.matridx.springboot.util.xml.XmlUtil;
import com.neusoft.si.entrance.webservice.CarryXmlToDbService;
import com.neusoft.si.entrance.webservice.collectdeclareservice.CollectDeclareService;
import com.neusoft.si.entrance.webservice.collectdeclareservice.Exception_Exception;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAlihealthMedicalOrderRealtimeGetRequest;
import com.taobao.api.request.AlibabaAlihealthMedicalReportCustomerReportSyncRequest;
import com.taobao.api.request.AlibabaAlihealthMedicalReservationKeepAppointmentFinishRequest;
import com.taobao.api.response.AlibabaAlihealthMedicalOrderRealtimeGetResponse;
import com.taobao.api.response.AlibabaAlihealthMedicalReportCustomerReportSyncResponse;
import com.taobao.api.response.AlibabaAlihealthMedicalReservationKeepAppointmentFinishResponse;
import nuonuo.open.sdk.NNOpenSDK;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Holder;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FzjcxxServiceImpl extends BaseBasicServiceImpl<FzjcxxDto, FzjcxxModel, IFzjcxxDao> implements IFzjcxxService, IAuditService, IFileImport {

    @Autowired
    IShgcService shgcService;
    @Autowired
    IJcsjDao jcsjDao;
    @Autowired
    IHzxxDao hzxxDao;
    @Autowired
    IHzxxService hzxxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IFjcfbService fjcfbService;
	@Autowired
    IXxglService xxglService;
	@Autowired
    IFzjcsjService fzjcsjService;
	@Autowired
    IJcsjService jcsjService;
	@Autowired
	IFzjcxmDao fzjcxmDao;
    @Autowired
    ICommonService commonservice;
    @Autowired
    RedisUtil redisUtil;
	@Autowired
    IShxxService shxxService;
    @Autowired
    IFzjcxmService fzjcxmService;
    @Autowired
    IFzjcjgService fzjcjgService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IDxglService dxglService;
    @Autowired
    IYyxxService yyxxService;
    @Autowired
    IXtszService xtszService;
    @Autowired
    IJkdymxService jkdymxService;
    @Autowired
    AttachHelper attachHelper;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempPath;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.ftp.url:}")
	private String FTP_URL = null;
    @Value("${matridx.aliyunSms.signNameMatridx:}")
    private String signNameMatridx;
    @Value("${matridx.aliyunSms.templateCodeXgbg:}")
    private String templateCodeXgbg;
    @Value("${matridx.reportSubmit.pc_address:}")
    private String pc_address;
    @Value("${matridx.reportSubmit.dc_address:}")
    private String dc_address;
    @Value("${matridx.reportSubmit.data_submit:}")
    private String data_submit;
    @Value("${matridx.reportSubmit.cjsj_address:}")
    private String cjsj_address;
    @Value("${matridx.reportSubmit.health_organ:}")
    private String health_organ;
    @Value("${matridx.reportSubmit.organization_name:}")
    private String organization_name;
    @Value("${matridx.reportSubmit.health_domain:}")
    private String health_domain;
    @Value("${matridx.reportSubmit.cjsj_code:}")
    private String cjsj_code;
    @Value("${matridx.fileupload.prefix:}")
    private String prefix;
    @Value("${matridx.fileupload.tempPath:}")
    private String tempFilePath;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;

	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;

    @Value("${matridx.aliyunSms.templateCodeXgyy:}")
    private String templateCodeXgyy;
    @Value("${matridx.glzhealth.appointmentfinishurl:}")
    private String glzhealthAppointmentfinishurl;
    @Value("${matridx.alihealth.appSecret:}")
    private String alihealthAppSecret;
    @Value("${matridx.alihealth.appKey:}")
    private String alihealthAppKey;
    @Value("${matridx.alihealth.serverUrl:}")
    private String alihealthServerUrl;
    @Value("${matridx.alihealth.merchantAesKey:}")
    private String alihealthMerchantAesKey;


    private final String text ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
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
            "      <servicecode></servicecode>\n" +
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


    //发送核酸检测采集信息上传服务xml请求
    public String generateCjsjXml(List<FzjcxxDto> fzjcxxList, String text) {
        //证件类型名称需存在
        try {
            DBEncrypt encrypt = new DBEncrypt();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document documentContent = xmlUtil.parse(text);
            Element rootElementContent = documentContent.getRootElement();
            rootElementContent.addNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance").addNamespace("schemaLocation","http://www.neusoft.com/hit/rhin file:///e:/request_message.xsd");
            Element sourceorgan = rootElementContent.element("switchset").element("visitor").element("sourceorgan");
            sourceorgan.setText(health_organ);//health_organ
            Element sourcedomain = rootElementContent.element("switchset").element("visitor").element("sourcedomain");
            sourcedomain.setText(health_domain);//health_domain
            Element servicecode = rootElementContent.element("switchset").element("serviceinf").element("servicecode");
            servicecode.setText(cjsj_code);
            Element reqpaging = rootElementContent.element("business").element("requestset").element("reqpaging");
            reqpaging.setText("0");//内容必填但自定义无要求
            Element reqpageindex = rootElementContent.element("business").element("requestset").element("reqpageindex");
            reqpageindex.setText("-1");//内容必填但自定义无要求
            Element reqpageset = rootElementContent.element("business").element("requestset").element("reqpageset");
            reqpageset.setText("0");//内容必填但自定义无要求
            Element datacompress = rootElementContent.element("business").element("datacompress");
            datacompress.setText("0");
            Element businessdata = rootElementContent.element("business").element("businessdata");
            Element datasets = businessdata.addElement("datasets");
            rootElementContent.element("business").element("daqtaskid").setText("");
            datasets.addElement("setcode");
            datasets.addElement("settype");
            for (FzjcxxDto fzjcxxDto : fzjcxxList) {
                Element setdetails = datasets.addElement("setdetails");
                setdetails.addElement("YLJGDM").setText(health_organ);//医疗机构代码
                setdetails.addElement("YLJGMC").setText(organization_name);//医疗机构名称
                setdetails.addElement("KH");//就诊卡号
                setdetails.addElement("KLX").setText("99");//卡类型（病人性质）（详见值域列表中“卡类型字典”）
                Element idtype = setdetails.addElement("IDTYPE");//身份证件类型（详见值域列表中“身份证件类别代码”）
                if (fzjcxxDto.getZjlxmc().contains("身份证")) {
                    idtype.setText("01");//居民身份证
                } else if (fzjcxxDto.getZjlxmc().contains("护照")) {
                    idtype.setText("03");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居住证")) {
                    idtype.setText("08");//港澳台居民居住证
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居住证")) {
                    idtype.setText("08");//港澳台居民居住证
                } else if (fzjcxxDto.getZjlxmc().contains("外国人永久居住证")) {
                    idtype.setText("10");
                } else if (fzjcxxDto.getZjlxmc().contains("港澳居民来往内地通行证")) {
                    idtype.setText("06");
                } else if (fzjcxxDto.getZjlxmc().contains("台湾居民来往内地通行证")) {
                    idtype.setText("07");
                } else {
                    idtype.setText("99");//其他
                }
                setdetails.addElement("SFZH").setText(encrypt.dCode(fzjcxxDto.getZjh()));//证件号码
                Element dhhm = setdetails.addElement("DHHM");//联系电话
                if (StringUtil.isNotBlank(fzjcxxDto.getSj())) {
                    dhhm.setText(encrypt.dCode(fzjcxxDto.getSj()));//证件号码;
                }
                setdetails.addElement("JYBGDH").setText("BG" + fzjcxxDto.getBbzbh());//检验报告单号,检验报告的唯一ID，，要与报告上传结果里的单号一致
                setdetails.addElement("YBH").setText(fzjcxxDto.getBbzbh());//样本号
                setdetails.addElement("TMH").setText(fzjcxxDto.getBbzbh());//条码号
                setdetails.addElement("JZLSH").setText(fzjcxxDto.getFzjcid());//就诊流水号
                setdetails.addElement("BRBSLB").setText("4");//就诊类别   1门诊 2急诊 3住院 4其他 5 体检
                setdetails.addElement("BRID").setText(encrypt.dCode(fzjcxxDto.getZjh()));//病人ID
                setdetails.addElement("XM").setText(fzjcxxDto.getXm());//病人姓名
                Element xbdm = setdetails.addElement("XBDM");//性别 详见值域列表中“性别代码 1male  2female  3unknow
                if (StringUtil.isNotBlank(fzjcxxDto.getXb())) {
                    if ("男".equals(fzjcxxDto.getXb()))
                        xbdm.setText("1");
                    else if ("女".equals(fzjcxxDto.getXb()))
                        xbdm.setText("2");
                    else
                        xbdm.setText("9");
                } else {
                    xbdm.setText("9");
                }
                Element csrq = setdetails.addElement("CSRQ");//出生年月YYYYMMDD
                if (StringUtil.isNotBlank(fzjcxxDto.getZjh())) {
                    String zjh = encrypt.dCode(fzjcxxDto.getZjh());
                    if (zjh.length() >= 14)
                        csrq.setText(zjh.substring(6, 14));
                }
                setdetails.addElement("BQBH");//病人病区编号
                setdetails.addElement("BQ");//病人病区名称
                setdetails.addElement("CH");//床号
                Element sqrq = setdetails.addElement("SQRQ");//检查申请日期 YYYYMMDDHHMMSS
                sqrq.setText(df.format(dff.parse(fzjcxxDto.getCjsj())));
                setdetails.addElement("SQYSDM");//申请医生代码
                setdetails.addElement("SQYSXM");//申请医生姓名
                setdetails.addElement("SQKSBM").setText("0");//申请科室代码
                setdetails.addElement("JYBGDJG").setText("无");//申请科室名称
                Element cjsj = setdetails.addElement("CJSJ");//标本采集日期 YYYYMMDDHHMMSS
                cjsj.setText(df.format(dff.parse(fzjcxxDto.getCjsj())));
                setdetails.addElement("CJRDM");//标本采集医生代码
                setdetails.addElement("CJRXM");//标本采集医生姓名
                setdetails.addElement("JCMD").setText("新型冠状病毒RNA检测");//检查目的
                setdetails.addElement("BBLXDM").setText("32");//标本类型代码  (32:上呼吸道)
                setdetails.addElement("BBLXMC").setText("上呼吸道标本");//标本类型名称
                Element cjsjsczt = setdetails.addElement("CJXXSCZT");//采集信息上传状态  1 ：新增   ，   2 ：更新
                if (StringUtil.isNotBlank(fzjcxxDto.getCjxxsczt())) {
                    if ("0".equals(fzjcxxDto.getCjxxsczt())) {
                        cjsjsczt.setText("1");
                    } else {
                        cjsjsczt.setText("2");
                        Date date = dff.parse(fzjcxxDto.getCjsj());
                        date.setTime(date.getTime() + 1000);
                        cjsj.setText(df.format(date));
                    }
                } else {
                    cjsjsczt.setText("1");
                }
            }
            return XmlUtil.getInstance().doc2String(documentContent);
        } catch (ParseException e) {
            //这个报错可能出现与df.parse代码处
            log.error("generateCjsjXml报错=====================================================",e.getMessage());
        } catch (Exception e) {
            log.error("generateCjsjXml报错=====================================================",e.getMessage());
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

    //发送采集时间上传服务请求xml，得到响应
    private String cjsjDeclare(String xmlrequest) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(cjsj_address+"?wsdl");
        try {
            Holder<String> xmlData = new Holder<>(xmlrequest);
            Object[] object  = client.invoke("resourceMethod",xmlData);
            return (object[0].toString());
        } catch (java.lang.Exception e) {
            log.error("cjsjDeclare方法报错："+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    //发送数据归集上传请求xml，得到响应
    private String uploadData(String xmlrequest) throws com.neusoft.si.entrance.webservice.Exception_Exception {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CarryXmlToDbService.class);
        factory.setAddress(data_submit);
//        factory.setAddress("http://192.26.64.134:8889/SJCJSJSCJKQ/SJSCQ");     //192.26.64.134:8889/SJCJSJSCJKQ/SJSCQ?wsdl
        // 需要服务接口文件
        CarryXmlToDbService client = (CarryXmlToDbService) factory.create();
        return client.handle(xmlrequest);
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

//    =====================================结束==========================================
	
	private final Logger log = LoggerFactory.getLogger(FzjcxxServiceImpl.class);

    /**
     * 删除按钮
     */
    public boolean delCovidDetails(FzjcxxDto fzjcxxDto){
        return dao.delCovidDetails(fzjcxxDto);
    }

    /**
     * 废弃按钮
     */
    public boolean discardCovidDetails(FzjcxxDto fzjcxxDto){
        return dao.discardCovidDetails(fzjcxxDto);
    }

    /**
     * 新冠列表数据
     */
    @Override
    public List<FzjcxxDto> getPagedDtoList(FzjcxxDto fzjcxxDto){
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        List<FzjcxxDto> list = dao.getPagedDtoList(fzjcxxDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_COVID.getCode(), "zt", "fzjcid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 新冠列表查看
     */
    public List<FzjcxxDto> viewCovidDetails(FzjcxxDto fzjcxxDto){
        return dao.viewCovidDetails(fzjcxxDto);
    }

    @Override
    public FzjcxxDto getSampleAcceptInfo(FzjcxxDto fzjcxxDto) throws BusinessException {
        List<FzjcxxDto> fzjcxxDtos = dao.getFzjcxxByybbh(fzjcxxDto);
        if (null == fzjcxxDtos || fzjcxxDtos.size() == 0){
        	log.error("没有该标本编码！");
//            throw new BusinessException("msg","没有该标本编码！");
            return null;
        }
        if (StringUtil.isBlank(fzjcxxDtos.get(0).getBbzt())){
            Map<String, List<JcsjDto>> bbztlist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.XG_SAMPLESTATE});
            for (JcsjDto jcsj:bbztlist.get(BasicDataTypeEnum.XG_SAMPLESTATE.getCode())) {
                if ("1".equals(jcsj.getSfmr())){
                    fzjcxxDtos.get(0).setBbzt(jcsj.getCsid());
                    break;
                }
            }
        }
        DBEncrypt dbEncrypt=new DBEncrypt();
        if (StringUtil.isNotBlank(fzjcxxDtos.get(0).getSj())){
            fzjcxxDtos.get(0).setSj(dbEncrypt.dCode(fzjcxxDtos.get(0).getSj()));
        }
//        fzjcxxDtos.get(0).setSj(dbEncrypt.dCode(fzjcxxDtos.get(0).getSj()));
        if (StringUtil.isNotBlank(fzjcxxDtos.get(0).getSyh())){
            String[] strs = fzjcxxDtos.get(0).getSyh().split("-");
            fzjcxxDtos.get(0).setSyh(strs[1]);
        }else{
            // 入库单号：RK-年月日-流水号
            String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
            String prefix = date + "-";
            // 查询流水号
            Map<String,String> map=new HashMap<>();
            map.put("prefix",prefix);
            map.put("jclx",fzjcxxDtos.get(0).getJclx());
            String serial = dao.getSyhSerial(map);
            fzjcxxDtos.get(0).setSyh(serial);
        }
        if ( StringUtil.isBlank(fzjcxxDtos.get(0).getNbbh())){
            if (fzjcxxDtos.get(0).getYbbh().contains("WX")){
                fzjcxxDtos.get(0).setNbbh(generateYbbh(fzjcxxDtos.get(0)).replace("JX","WX"));
            }else{
                fzjcxxDtos.get(0).setNbbh(generateYbbh(fzjcxxDtos.get(0)));
            }
        }
        if (StringUtil.isBlank(fzjcxxDtos.get(0).getCjsj())){
        	log.error("该用户还未录入！");
            throw new BusinessException("msg","该用户还未录入！");
        }
        return fzjcxxDtos.get(0);
    }

    @Override
    public List<FzjcxxDto> getSampleAcceptInfoList(FzjcxxDto fzjcxxDto) throws BusinessException {
        List<FzjcxxDto> fzjcxxDtos = dao.getFzjcxxByybbh(fzjcxxDto);
        if (null == fzjcxxDtos || fzjcxxDtos.size() == 0){
            log.error("没有该标本编码！");
            throw new BusinessException("msg","没有该标本编码！");
        }
        DBEncrypt dbEncrypt=new DBEncrypt();
        for (FzjcxxDto fzjcxxDto1: fzjcxxDtos) {
            if (StringUtil.isNotBlank(fzjcxxDto1.getSj())){
                fzjcxxDto1.setSj(dbEncrypt.dCode(fzjcxxDto1.getSj()));
            }
        }
        return fzjcxxDtos;
    }

    @Override
    public FzjcxxDto getInfoByNbbh(FzjcxxDto fzjcxxDto) throws BusinessException {
        List<FzjcxxDto> fzjcxxDtos = dao.getInfoByNbbh(fzjcxxDto);
        if (fzjcxxDtos == null || fzjcxxDtos.size() <= 0){
        	log.error("没有该条记录！");
            throw new BusinessException("msg","没有该条记录！");
        }
        return fzjcxxDtos.get(0);
    }

    @Override
    public Boolean updateInfoByNbbh(FzjcxxDto fzjcxxDto){
        return  dao.updateInfoByNbbh(fzjcxxDto) != 0;
    }

    @Override
    public String getFzjcxxByybbh() {
        String yearLast = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
        calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
        String weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
        if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == Calendar.DECEMBER){
            calendar.add(Calendar.DATE, -7);
            weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
            weekOfYear = (Integer.parseInt(weekOfYear) + 1) + "";
        }
        String yearandweek = null;
        if (weekOfYear.length() == 1){
            yearandweek = yearLast + "0" + weekOfYear;
        } else if (weekOfYear.length() == 2){
            yearandweek = yearLast + weekOfYear;
        }
        String prefix = "JX" + yearandweek;
        // 查询流水号
        String serial = dao.getNbbhSerial(prefix);
        return prefix + serial;
    }

    @Override
    public String generateYbbh(FzjcxxDto fzjcxxDto) {
        Map<String, String> infoMap = new HashMap<>();
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
            List<Map<String, String>> infoMaps = dao.getconfirmDmInfo(fzjcxxDto);
            if(CollectionUtils.isEmpty(infoMaps))
                return null;
            infoMap = infoMaps.get(0);
        } else{
            if (StringUtil.isNotBlank(fzjcxxDto.getJcdw())){
                infoMap.put("jcdwdm", redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode(), fzjcxxDto.getJcdw()).getCsdm());
            }
            if (StringUtil.isNotBlank(fzjcxxDto.getJclx())){
                infoMap.put("jclxcskz2", redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION.getCode(), fzjcxxDto.getJclx()).getCsdm());
            }
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getJclx())){
            List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            for (JcsjDto jcsjDto : jcsjDtos) {
                if (jcsjDto.getCsdm().equals(fzjcxxDto.getJclx())){
                    infoMap.put("jclxcskz1", jcsjDto.getCskz1());
                    infoMap.put("jclxcskz2", jcsjDto.getCskz2());
                    break;
                }
            }
        }
        if (infoMap == null){
            return null;
        }
        /*----------获取流水号规则-------开始----------*/
        //分子检测类型cskz1
        //样本编号替换规则{key:length:isConditionOfSerial}=>{替换关键词:长度:是否为计算流水号的条件}
        //默认规则如下: 分子检测类型cskz2（2位） + 年份（2位） + 周数（2位） + 流水号（5位）
        String ybbhgz = "{jclxcskz2:2:true}{nf:2:true}{zs:2:true}{lsh:5:false}";
        if (StringUtil.isNotBlank(infoMap.get("jclxcskz1"))){
            ybbhgz = infoMap.get("jclxcskz1");
        }
        log.error("样本编号(新规则)规则："+ ybbhgz);
        /*----------获取流水号规则-------结束----------*/
        /*----------样本编号规则处理-----开始----------*/
        //1、根据规则获取样本编号长度：nbbmLength
        int ybbhLength = 0;//内部编码长度（累加）
        //2、流水号生成的查询条件（里面包含conditionList、startindex、seriallength、deafultserial）：serialMap
        Map<String, Object> serialMap = new HashMap<>();//流水号生成查询条件
        //3、样本编号规则中isConditionOfSerial为true的条件：conditionList
        List<Map<String,Object>> conditionList = new ArrayList<>();
        //4、样本编号字符串List
        List<Map<String,String>> ybbhStrList = new ArrayList<>();
        //5、流水号List
        List<Map<String,String>> serialStrList = new ArrayList<>();
        //6、循环替换ybbhgz中个关键词(除有关流水号的内容不替换)
        while (ybbhgz.contains("{") && ybbhgz.contains("}")) {
            Map<String,String> ybbhMap = new HashMap<>();
            int length = 0;//默认关键词长度为0
            boolean isConditionOfSerial = false;//默认 是否加入流水哈生成的判断条件 为false
            int startIndex = ybbhgz.indexOf("{");//获取开始下标
            int endIndex = ybbhgz.indexOf("}");//获取结束下标
            if (startIndex != 0){
                // 情况1：替换符前方有字符串；若“{”在不在首位
                // xxxx-{jcdwdm:2:true}
                // 截取“xxxx-”存入nbbmStrList中
                String valueStr = ybbhgz.substring(0,startIndex);
                endIndex = startIndex-1;
                ybbhMap.put("key","string");//设置关键词为string
                ybbhMap.put("value",valueStr);//设置对应值
                ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
                length = valueStr.length();
                ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
            }else {
                // 情况1：替换符前方无字符串；若“{”在首位
                // {jcdwdm:2:true}
                //截取{}内的内容
                String key = ybbhgz.substring(startIndex + 1, endIndex);//jcdwdm:2:true
                //若截取内容内有“:”则分割；
                //若截取内容内无“:”，则默认key为截取内容，长度为其map中长度，默认是否加入流水哈生成的判断条件为false
                if (key.contains(":")) {
                    String[] split = key.split(":");//例：[jcdwdm,4,true]
                    key = split[0];//第一位为关键词key，例：jcdwdm
                    if (StringUtil.isNotBlank(split[1])){
                        try {
                            length = Integer.parseInt(split[1]);//第二位为对应关键词的长度，例：2
                        } catch (NumberFormatException e) {
                            log.error("样本编号(新规则)警告：key:"+key+"的length:"+split[1]+"不是数字");
                        }
                    }
                    //若有第三位，则第三位为是否加入流水哈生成的判断条件，例：true
                    //若无第三位，默认为false
                    if (split.length >= 3){
                        isConditionOfSerial = "true".equalsIgnoreCase(split[2]);
                    }
                }
                ybbhMap.put("key",key);//设置关键词
                //若为流水号，不做处理，
                if (("lsh").equals(key)){
                    ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                    ybbhMap.put("value","");//设置值为空（后面处理）
                    ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
                    serialStrList.add(ybbhMap);//存入流水号List中
                } else {
                    //内部编码特殊处理
                    dealBhKeyMap(key, fzjcxxDto, infoMap);
                    String valueStr = infoMap.get(key);//从infoMap中获取对应值，例：jcdwdm对应为01
                    if (valueStr.length() > length && length != 0){
                        valueStr = valueStr.substring(valueStr.length()-length);//将值截取至对应长度。例：截取01的后2位（01为对应值，2为对应值长度）（默认从末尾开始截取长度，若需要从前面开始截取，则需要增加特殊key,并在dealNbbmKeyMap方法中处理）
                    }
                    length = valueStr.length();//获取对应值最终长度（针对没设长度的时候）
                    ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
                    ybbhMap.put("value",valueStr);//设置值
                    ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
                }
                if (isConditionOfSerial){
                    Map<String,Object> map = new HashMap<>();
                    map.put("conditionIndex",ybbhLength+1);
                    map.put("conditionLength",length);
                    map.put("conditionKey",ybbhMap.get("value"));
                    conditionList.add(map);
                }
            }
            ybbhStrList.add(ybbhMap);//将截取替换内容添加到list
            ybbhgz = ybbhgz.substring(endIndex+1);//截取剩余分规则
            ybbhLength += length;//内部编码长度累加
        }
        //7、若内部编码规则最后还有字符串，则进行处理
        if(ybbhgz!=null&&ybbhgz.length()>0){
            Map<String,String> ybbhMap = new HashMap<>();
            ybbhMap.put("key","string");//设置关键词为string
            ybbhMap.put("value",ybbhgz);//设置对应值
            ybbhMap.put("index", String.valueOf(ybbhLength));//设置位置（前面所有字符的长度）
            int length = ybbhgz.length();
            ybbhMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
            ybbhStrList.add(ybbhMap);//将截取替换内容添加到list
            ybbhLength += length;//内部编码长度累加
        }
        //8、遍历流水号List,循环生成对应的流水号
        for (Map<String, String> ybbhInfo : serialStrList) {
            serialMap.put("startindex", Integer.parseInt(ybbhInfo.get("index"))+1);//流水号开始位置
            serialMap.put("seriallength", Integer.parseInt(ybbhInfo.get("length")));//流水号长度
            serialMap.put("ybbhLength", ybbhLength);//内部编码长度
            serialMap.put("conditionList",conditionList);//条件List
            serialMap.put("jcdm", infoMap.get("jcdm"));//检测项目cskz1（区分F项目和其他项目）
            //生成获取流水号
            String customSerial = dao.getCustomSerial(serialMap);
            ybbhInfo.put("value",customSerial);
        }
        //拼接list获取完整内部编码
        String ybbh = "";
        for (Map<String, String> ybbhStr : ybbhStrList) {
            ybbh += ybbhStr.get("value");
        }
        return ybbh;
    }


    /**
     * 内部编码相关处理
     * @param key
     * @param fzjcxxDto
     * @param infoMap
     * @return
     */
    public Map<String, String> dealBhKeyMap(String key, FzjcxxDto fzjcxxDto, Map<String, String> infoMap){
        if ("nf".equals(key)){
            // 年份 处理
            dealNf(infoMap);
        }else if ("yf".equals(key)) {
            // 周数 处理
            dealYf(infoMap);
        }else if ("ts".equals(key)) {
            // 周数 处理
            dealTs(infoMap);
        }else if ("zs".equals(key)){
            // 周数 处理
            dealZs(infoMap);
        }
        return infoMap;
    }

    /**
     * 年份处理
     * @param infoMap
     */
    void dealNf(Map<String, String> infoMap){
        String year = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date().getTime());
        infoMap.put("nf",year);
    }
    /**
     * 月份处理
     * @param infoMap
     */
    void dealYf(Map<String, String> infoMap){
        Calendar currentDate = Calendar.getInstance();
        // 获取当前月份
        int yf = currentDate.get(Calendar.MONTH) + 1;
        infoMap.put("yf", String.valueOf(yf));

    }
    /**
     * 天数处理
     * @param infoMap
     */
    void dealTs(Map<String, String> infoMap){
        Calendar currentDate = Calendar.getInstance();
        // 获取当前天数
        int ts = currentDate.get(Calendar.DAY_OF_MONTH);
        infoMap.put("ts", String.valueOf(ts));

    }

    /**
     * 年份处理 长度两位
     * @param infoMap
     */
    void dealZs(Map<String, String> infoMap){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
        calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
        String weekOfYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));// 获得当前日期属于今年的第几周
        if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == 11){
            calendar.add(Calendar.DATE, -7);
            weekOfYear = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)+1);// 获得当前日期属于今年的第几周
        }
        weekOfYear = weekOfYear.length()>1?weekOfYear:"0"+weekOfYear;
        infoMap.put("zs",weekOfYear);
    }


    @Override
    public List<FzjcxxDto> getFzjcxxByNbbh(FzjcxxDto fzjcxxDto) {
        return dao.getFzjcxxByNbbh(fzjcxxDto);
    }

    @Override
    public List<FzjcxxDto> getFzjcxxBySyh(FzjcxxDto FzjcxxDto) {
        return dao.getFzjcxxBySyh(FzjcxxDto);
    }

    @Override
    public boolean saveFzjcxxInfo(FzjcxxDto fzjcxxDto) throws BusinessException {
        boolean success=  dao.saveFzjcxxInfo(fzjcxxDto) !=0;
        if (!success){
        	log.error("保存失败！");
            throw new BusinessException("msg","保存失败！");
        }
        if(!"W".equals(fzjcxxDto.getJcdxcsdm()))
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZJC_EDI.getCode() + JSONObject.toJSONString(fzjcxxDto));
        return true;
    }

    @Override
    public String getSyhSerial(String jclx) throws BusinessException {
        // 入库单号：RK-年月日-流水号
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = date + "-";
        // 查询流水号
        Map<String,String> map=new HashMap<>();
        map.put("prefix",prefix);
        map.put("jclx",jclx);
        return dao.getSyhSerial(map);
    }

    /**
     * 获取新冠报告附件
     */
    public List<FjcfbDto> getFjcfb(FzjcxxDto fzjcxxDto){
        return dao.getFjcfb(fzjcxxDto);
    }

    /**
     * 从数据库分页获取导出数据
     */
    public List<FzjcxxDto> getListForSelectExp(Map<String, Object> params){
        FzjcxxDto fzjcxxDto = (FzjcxxDto) params.get("entryData");
        queryJoinFlagExport(params,fzjcxxDto);
        List<FzjcxxDto> list=dao.getListForSelectExp(fzjcxxDto);
        DBEncrypt dbEncrypt=new DBEncrypt();
        for(FzjcxxDto fzjcxxDto_t:list){
            if(StringUtil.isNotBlank(fzjcxxDto_t.getZjh())){
                fzjcxxDto_t.setZjh(dbEncrypt.dCode(fzjcxxDto_t.getZjh()));
            }
            if(StringUtil.isNotBlank(fzjcxxDto_t.getSj())){
                fzjcxxDto_t.setSj(dbEncrypt.dCode(fzjcxxDto_t.getSj()));
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, FzjcxxDto fzjcxxDto)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList)
        {
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        fzjcxxDto.setSqlParam(sqlParam.toString());
    }

    /**
     * 根据搜索条件获取导出条数
     */
    public int getCountForSearchExp(FzjcxxDto fzjcxxDto,Map<String,Object> params){
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getCountForSearchExp(fzjcxxDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<FzjcxxDto> getListForSearchExp(Map<String, Object> params) {
        FzjcxxDto fzjcxxDto = (FzjcxxDto) params.get("entryData");
        queryJoinFlagExport(params, fzjcxxDto);
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        List<FzjcxxDto> list = dao.getListForSearchExp(fzjcxxDto);
        DBEncrypt dbEncrypt=new DBEncrypt();
        for(FzjcxxDto fzjcxxDto_t:list){
            if(StringUtil.isNotBlank(fzjcxxDto_t.getZjh())){
                fzjcxxDto_t.setZjh(dbEncrypt.dCode(fzjcxxDto_t.getZjh()));
            }
            if(StringUtil.isNotBlank(fzjcxxDto_t.getSj())){
                fzjcxxDto_t.setSj(dbEncrypt.dCode(fzjcxxDto_t.getSj()));
            }
        }
        return list;
    }

    /**
	* 获取预约的数据
     */
    @Override
	public Map<String, Object> getAppointList(HzxxDto hzxxDto) {
//    	2.1 后台查到今天之后有预约数据，显示数据，患者信息显示查看（证件号码和姓名，其余信息都是可编辑），其他数据显示并可编辑
//    	2.2 后台查不到今天之后的预约数据，但能查到昨天以前的预约数据，界面div显示预约清单，人员选择预约数据，界面中的人员数据为选中的数据，且患者信息为查看
//    	2.3 后台查不到任何预约数据，但有过检测历史，界面的患者信息为查看，其他如检测项目需要补充（查看界面都要存患者id）
//    	2.4 后台查不到预约数据，无检测历史， 界面患者信息处于编辑状态，新增到数据库
//    	使用身份证查找患者信息关联分子检测信息，找出
		Map<String,Object> map = new HashMap<>();
        DBEncrypt crypt = new DBEncrypt();
	    HzxxDto hzxx;
	    FzjcxxDto afterdto = new FzjcxxDto();
	    hzxx = hzxxDao.getHzxxByZjh(hzxxDto);
        if (hzxx != null ){//解密主要用于前端页面回显信息使用
            hzxx.setSj(crypt.dCode(hzxx.getSj()));
            hzxx.setZjh(crypt.dCode(hzxx.getZjh()));
        }
	    if (StringUtil.isNotBlank(hzxxDto.getZjh())) {
	    	afterdto = dao.afterDayList(hzxxDto);//今天之后（包含今天）的预约数据 1
		}
		map.put("fzjcxxDto",afterdto);
		map.put("hzxxDto",hzxx);
		return map;
	}
	
	/**
	 * 取过期的预约数据
	 */
	@Override
	public List<FzjcxxDto> overdueAppoint(HzxxDto hzxxDto) {
		//取今天之后的预约数据（不含今天）
//		List<FzjcxxDto> beforelist = dao.beforeDayList(hzxxDto.getZjh());//今天之前（不包含）的预约数据 3
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    hzxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.beforeDayList(hzxxDto);
	}
	
     /**
	 * 提交时，要更新患者表的确认字段，要生成编号  更新分子检测信息表的采集人员和采集时间
      */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, String> checkAppointData(FzjcxxDto fzjcxxDto, HzxxDto hzxxDto) throws Exception {
        Map<String, String> map= new HashMap<>();
        //新样本编号规则修改： 检测单位代码 + 检测项目代码参数代码 + 22年份 + 7位流水号
        //eg：01JX220000001
        //旧编号规则：检测项目代码+检测单位参数代码+六位流水号
        String ybbh = generateYbbh(fzjcxxDto);

        JcsjDto jclxDto;//分子检测类型基础数据 单混检
        jclxDto = jcsjService.getDtoById(fzjcxxDto.getFzjczxmid());
        if (StringUtil.isBlank( fzjcxxDto.getYbbh_ym() )){
            //页面未传标本编号过来，无论单混都是新批次，此时使用新ybbh
            fzjcxxDto.setYbbh(ybbh);
            if ("D".equals(jclxDto.getCskz1())){//新增的时候，不管是单混检，di
                fzjcxxDto.setBbzbh(fzjcxxDto.getYbbh());
                fzjcxxDto.setWybh(fzjcxxDto.getYbbh() + "01");//唯一编码，方式混检和单检样本编码重复
                map.put("ybbh_ym", "");
                map.put("xh", "");
            }else {//混检
                fzjcxxDto.setBbzbh(fzjcxxDto.getYbbh() + fzjcxxDto.getXh());
                fzjcxxDto.setWybh(fzjcxxDto.getYbbh() + fzjcxxDto.getXh());
                map.put("ybbh_ym", fzjcxxDto.getYbbh());
                int xh = Integer.parseInt(fzjcxxDto.getXh())+1 ;
                if (xh<=Integer.parseInt(jclxDto.getCskz2())){
                    if (xh<10)
                        map.put("xh", "0"+ xh);
                    else
                        map.put("xh", String.valueOf(  Integer.parseInt( fzjcxxDto.getXh() )+1  ) );
                }else {
                    map.put("ybbh_ym", "");
                    map.put("xh", "");
                }
            }
        }else {
            //页面传输了ybbh_ym则表示已经在混检的第某条数据
            fzjcxxDto.setYbbh(fzjcxxDto.getYbbh_ym());
            if ("H".equals(jclxDto.getCskz1())){//混检
                //int x =Integer.parseInt(jclxDto.getCskz2());
                if (Integer.parseInt(fzjcxxDto.getXh())<= Integer.parseInt(jclxDto.getCskz2())){
                    fzjcxxDto.setBbzbh(fzjcxxDto.getYbbh() + fzjcxxDto.getXh());
                    fzjcxxDto.setWybh(fzjcxxDto.getYbbh() + fzjcxxDto.getXh());
                    map.put("ybbh_ym", fzjcxxDto.getYbbh());
                    int xh = Integer.parseInt(fzjcxxDto.getXh())+1 ;
                    if (xh<=Integer.parseInt(jclxDto.getCskz2())){
                        if (xh<10)
                            map.put("xh", "0"+ xh);
                        else
                            map.put("xh", String.valueOf(  Integer.parseInt( fzjcxxDto.getXh())+1  ) );
                    }else {
                        map.put("ybbh_ym", "");
                        map.put("xh", "");
                    }
                }else {
                    map.put("ybbh_ym", "");
                    map.put("xh", "");
                }
            }
        }

		StringBuilder jcxmmc = new StringBuilder();//存储一个检测信息对应的多个检测项目名称，格式xx,xx,xx
		StringBuilder jcxmid = new StringBuilder();
		JcsjDto jcsjDto;
		String[] fzxmlist = fzjcxxDto.getFzxmids();
        Map<String,Object> rabbitmap = new HashMap<>();

        rabbitmap.put("fzxmlist",fzxmlist);
        List<FzjcxmDto> fzjcxmDtoList = new ArrayList<>();

        for (String s : fzxmlist) {//该循环处理分子检测项目的jcxmmc和jcxmid字段
            jcsjDto = jcsjDao.getDtoById(s);
            jcxmmc.append(",").append(jcsjDto.getCsmc());
            jcxmid.append(",").append(jcsjDto.getCsid());
        }
		if (StringUtil.isNotBlank(jcxmmc.toString())) {
			fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
		}
		if (StringUtil.isNotBlank(jcxmid.toString())) {
			fzjcxxDto.setJcxmid(jcxmid.substring(1));
		}
		
		//更新患者信息，进行确认
		hzxxService.updateSfqr(hzxxDto);
        rabbitmap.put("hzxxDto",hzxxDto);
//		amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.HZXX_MOD.getCode() + JSONObject.toJSONString(hzxxDto));

		//查找分子检测信息表，无则新增，有则更新确认
		if (StringUtil.isBlank(fzjcxxDto.getFzjcid())) {
			//无则新增分子检测信息，分子检测项目
			fzjcxxDto.setFzjcid(StringUtil.generateUUID());
            fzjcxxDto.setCjsj(   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   );
//            fzjcxxDto.setCyd("浙江省杭州市余杭区良渚街道金昌路2069号生命科技小镇9-3号楼北侧");
            fzjcxxDto.setFkje("40.00");
            JcsjDto jcsj = new JcsjDto();
            jcsj.setJclb(BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
            jcsj.setCsdm("R");
            JcsjDto jcsj_jcdx = jcsjService.getDtoByCsdmAndJclb(jcsj);
            if (jcsj_jcdx!=null){
                fzjcxxDto.setJcdxlx(jcsj_jcdx.getCsid());
            }
            //区分新冠数据
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if(jclxList.size()>0){
                for(JcsjDto tjcsjDto : jclxList){
                    if("TYPE_COVID".equals(tjcsjDto.getCsdm()))
                        fzjcxxDto.setJclx(tjcsjDto.getCsid());
                }
            }
			dao.insertDto(fzjcxxDto);
            map.put("sfxz", "1");
            map.put("fzjcid", fzjcxxDto.getFzjcid());
            rabbitmap.put("fzjcxxDto_add",fzjcxxDto);
//			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_ADD.getCode() + JSONObject.toJSONString(fzjcxxDto));
		}else {
			//能查找到则更新，更新前要先判断标本编号是否存在，防止重复点击生成两次样本编号,样本编号存在表示已经保存则不更新，不存在则更新
            fzjcxxDto.setCjsj(   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   );
            FzjcxxDto isDuplicate = getDto(fzjcxxDto);
            if (StringUtil.isNotBlank(isDuplicate.getYbbh())){//数据库的样本编号已经存在，不可再生成新编号使用，有旧编号的数据使用旧编号
                fzjcxxDto.setYbbh(isDuplicate.getYbbh());
            }
            if (StringUtil.isNotBlank(isDuplicate.getFkbj()) && "0".equals(isDuplicate.getFkbj())){
                map.put("sfxz", "1");
                map.put("fzjcid", isDuplicate.getFzjcid());
            }
            JcsjDto jcsj = new JcsjDto();
            jcsj.setJclb(BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
            jcsj.setCsdm("R");
            JcsjDto jcsj_jcdx = jcsjService.getDtoByCsdmAndJclb(jcsj);
            if (jcsj_jcdx!=null){
                fzjcxxDto.setJcdxlx(jcsj_jcdx.getCsid());
            }
            dao.updateConfirm(fzjcxxDto);
            rabbitmap.put("fzjcxxDto_mod",fzjcxxDto);
//			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_MOD.getCode() + JSONObject.toJSONString(fzjcxxDto));
		}
		//先删除向目标，再新增项目信息
		fzjcxmDao.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
//		amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXM_DEL.getCode() + JSONObject.toJSONString(fzjcxxDto));
        for (String s : fzxmlist) {
            //遍历检测项目一个一个新增到数据库
            FzjcxmDto fzjcxmDto = new FzjcxmDto();
            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
            fzjcxmDto.setFzjcxmid(s);
            fzjcxmDto.setLrry(fzjcxxDto.getLrry());
            fzjcxmDto.setFzjczxmid(fzjcxxDto.getFzjczxmid());//存分子检测子项目ID
            fzjcxmDao.insertDto(fzjcxmDto);
            fzjcxmDtoList.add(fzjcxmDto);
//			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXM_ADD.getCode() + JSONObject.toJSONString(fzjcxmDto));
        }
        rabbitmap.put("fzjcxmDtoList",fzjcxmDtoList);
        FzjcxxDto dto = getDto(fzjcxxDto);
        //若是从平台接口预约的，则调用对应履约确认接口
        if(dto!=null){
            if("阿里健康".equals(dto.getPt())){
                confirmPerformance(dto);
            }else if ("橄榄枝健康".equals(dto.getPt())){
                confirmPerformanceToGanlanzhi(dto);
            }
        }

        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.SFLR_SUB.getCode() + JSONObject.toJSONString(rabbitmap));
        return map;
    }
	
	@Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        FzjcxxDto fzjcxxDto = (FzjcxxDto)baseModel;
        fzjcxxDto.setXgry(operator.getYhid());
        fzjcxxDto.setFzjcid(auditParam.getShgcDto().getYwid());
        dao.updateZt(fzjcxxDto);
        fzjcxxDto=getDto(fzjcxxDto);
        if(!"W".equals(fzjcxxDto.getJcdxcsdm())) {
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.SHZT_MOD.getCode() + JSONObject.toJSONString(fzjcxxDto));
        }
        return true;
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        try{
            //List<String> ids=new ArrayList<>();
            for (int j=0; j < shgcList.size(); j++){
                //            for(ShgcDto shgcDto : shgcList){
                FzjcxxDto fzjcxxDto = new FzjcxxDto();
                fzjcxxDto.setFzjcid(shgcList.get(j).getYwid());
                //获取标本申请信息，主要获取标本ID和申请数，用于后面计算
                List<FzjcxxDto> fzjcxxDtos = viewCovidDetails(fzjcxxDto);
                FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
                fzjcxxDto.setXgry(operator.getYhid());
                fzjcxxDto.setXm(fzjcxxDtos.get(0).getXm());

                //审核退回
                if(AuditStateEnum.AUDITBACK.equals(shgcList.get(j).getAuditState())){

                    fzjcxxDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                //审核通过
                }else if(AuditStateEnum.AUDITED.equals(shgcList.get(j).getAuditState())){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String bgrq=sdf.format(new Date());
                    fzjcxxDto_t.setBgrq(bgrq);
                    dao.updateBgrq(fzjcxxDto_t);
                    fzjcxxDto.setBgrq(fzjcxxDto_t.getBgrq());
                    fzjcxxDto.setJcdxcsdm(fzjcxxDto_t.getJcdxcsdm());
                    fzjcxxDto.setZt(StatusEnum.CHECK_PASS.getCode());

                    //发送rabbit消息更新8085端的检测信息
                    fzjcxxDto_t.setXgry(operator.getYhid());
                    fzjcxxDto_t.setZt(StatusEnum.CHECK_PASS.getCode());
                    //log.error("正常发送消息到8085");
                    if(!"W".equals(fzjcxxDto_t.getJcdxcsdm())) {
                        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_CON.getCode() + JSONObject.toJSONString(fzjcxxDto_t));
                    }

                    //========================接口对接代码开始===============================
                    //只有人检的数据才上传卫健委
                    if (j == shgcList.size()-1){
                        //dao.updateFssjByYwids(shgcList);
                        List<FzjcxxDto> fzjcxxlistx = getFzjcxxListByYwids(shgcList);//查找数据为单次任务号为空的，已有任务号的说明已经上传过则不再上传
                        if (fzjcxxlistx!=null && fzjcxxlistx.size()>0){
                            List<FzjcxxDto> personlist=new ArrayList<>();
                            for(FzjcxxDto fzjcxxDto_r : fzjcxxlistx){
                                fzjcxxDto_r.setShry(operator.getYhid());//将审核人员传进去
                                if(!"W".equals(fzjcxxDto_r.getJcdxcsdm())
                                        && StringUtils.isNotBlank(fzjcxxDto_r.getXm())
                                        && StringUtils.isNotBlank(fzjcxxDto_r.getNl())
                                        && StringUtils.isNotBlank(fzjcxxDto_r.getZjh())
                                        && StringUtils.isNotBlank(fzjcxxDto_r.getZjlx())
                                        && StringUtils.isNotBlank(fzjcxxDto_r.getXb())){
                                    personlist.add(fzjcxxDto_r);
                                }
                            }
                            if(!"POSITIVE".equals(fzjcxxDto_t.getJcjgdm())){
                                if(personlist.size()>0){
                                    dao.updateFssjByIds(fzjcxxlistx);//更新发送时间和发送标记，无发送时间的更新为当前时间，有发送时间的不更改发送时间
                                    //启用线程上传数据至卫健委
                                    UploadReportToWjw uploadReportToWjw=new UploadReportToWjw();
                                    uploadReportToWjw.init(fzjcxxlistx,dao,operator,pc_address,dc_address,data_submit);
                                    UploadReportToWjwThread uploadReportToWjwThread=new UploadReportToWjwThread(uploadReportToWjw);
                                    uploadReportToWjwThread.start();
                                }
                            }
                            //生成报告，启用新线程，采用发送rabbit消息
                            Map<String,String> backMap = auditParam.getBackMap();
                            backMap.put("APPOINTMENT_DETECTION_" + fzjcxxlistx.get(0).getFzjcid(),RabbitEnum.FZXX_REP.getCode() + JSONObject.toJSONString(fzjcxxlistx));
                            //amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.FZXX_REP.getCode() + JSONObject.toJSONString(fzjcxxlistx));
                        }
                    }
                }else{ //审核中
                    fzjcxxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                    //发送钉钉消息--取消审核人员
                    if(shgcList.get(j).getNo_spgwcyDtos() != null && shgcList.get(j).getNo_spgwcyDtos().size() > 0){
                        try{
                            int size = shgcList.get(j).getNo_spgwcyDtos().size();
                            for(int i=0;i<size;i++){
                                if(StringUtil.isNotBlank(shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm())){
                                    talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),shgcList.get(j).getNo_spgwcyDtos().get(i).getYhid(),shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm(),
                                            shgcList.get(j).getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcList.get(j).getShlbmc() ,fzjcxxDto_t.getYbbh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        }catch(Exception e){
                            log.error(e.getMessage());
                        }
                    }
                }
                //更新状态
                dao.updateZt(fzjcxxDto);
                if(!"W".equals(fzjcxxDto_t.getJcdxcsdm())) {
                    amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.SHZT_MOD.getCode() + JSONObject.toJSONString(fzjcxxDto));
                }
//            }
            }
            return true;

        } catch (Exception e){
        	log.error(e.getMessage());
            return false;
        }
    }
    
    /**
	 * 审核完成后处理,为了获取到更新后的报告日期，所以重写基类的方法
	 * @param dto	审核信息，主要是业务ID列表，ywids
	 * @param operator  操作人
	 * @param param 审核传递的额外参数信息
	 */
	public boolean updateAuditEnd(ShxxDto dto, User operator, Map<String, String> param) {
		//按枚举有序进行校验
		Set<String> keySet = param.keySet();
        for (String item : keySet) {
            if (item.startsWith("APPOINTMENT_DETECTION_")) {
                //生成报告，启用新线程，采用发送rabbit消息
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), param.get(item));
            }
        }
		return true;
	}

    private boolean dockHealthInterface(List<FzjcxxDto> fzjcxxlist, User operator) throws BusinessException {
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
            dao.updateTaskid(map);
            //更新卫建上报成功时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createdate = sdf.format(date);
            map.put("sbsj",createdate);
            dao.updateSbsj(map);
            return true;
        } catch (Exception e) {
		    log.error("dockHealthInterface报错========================================",e.getMessage());
            throw new BusinessException("msg","数据上传卫建委失败！");
        }
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String fzjcid = shgcDto.getYwid();
                FzjcxxDto fzjcxxDto = new 	FzjcxxDto();
                fzjcxxDto.setFzjcid(fzjcid);
                fzjcxxDto=getDto(fzjcxxDto);
                fzjcxxDto.setXgry(operator.getYhid());
                fzjcxxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.updateZt(fzjcxxDto);
                if(!"W".equals(fzjcxxDto.getJcdxcsdm()))
                    amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.SHZT_MOD.getCode()+ JSONObject.toJSONString(fzjcxxDto));
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String fzjcid = shgcDto.getYwid();
                FzjcxxDto fzjcxxDto = new 	FzjcxxDto();
                fzjcxxDto.setFzjcid(fzjcid);
                fzjcxxDto=getDto(fzjcxxDto);
                fzjcxxDto.setXgry(operator.getYhid());
                fzjcxxDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.updateZt(fzjcxxDto);
                if(!"W".equals(fzjcxxDto.getJcdxcsdm()))
                    amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.SHZT_MOD.getCode()+ JSONObject.toJSONString(fzjcxxDto));
            }
        }
        return true;
    }

    /**
	 * 生产报告结果
	 */
	@Override
	public Map<String, Object> generateReport(FzjcxxDto fzjcxxDto) throws BusinessException{
	    //由于沿用了上传卫健的SQL，需要转换时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            if(StringUtils.isNotBlank(fzjcxxDto.getJssj()))
                fzjcxxDto.setJssj(sdf.format(sdfs.parse(fzjcxxDto.getJssj())));
            if(StringUtils.isNotBlank(fzjcxxDto.getCjsj()))
                fzjcxxDto.setCjsj(sdf.format(sdfs.parse(fzjcxxDto.getCjsj())));
            if(StringUtils.isNotBlank(fzjcxxDto.getSqdsj()))
                fzjcxxDto.setSqdsj(sdf.format(sdfs.parse(fzjcxxDto.getSqdsj())));
            if(StringUtils.isNotBlank(fzjcxxDto.getSysj()))
                fzjcxxDto.setSysj(sdf.format(sdfs.parse(fzjcxxDto.getSysj())));
            if(StringUtils.isNotBlank(fzjcxxDto.getBgrq()))
                fzjcxxDto.setBgrq(sdf.format(sdfs.parse(fzjcxxDto.getBgrq())));
            if(StringUtils.isNotBlank(fzjcxxDto.getQrsj()))
                fzjcxxDto.setQrsj(sdf.format(sdfs.parse(fzjcxxDto.getQrsj())));
            if(StringUtils.isNotBlank(fzjcxxDto.getFssj()))
                fzjcxxDto.setFssj(sdf.format(sdfs.parse(fzjcxxDto.getFssj())));
        } catch (ParseException e) {
            throw new BusinessException("日期转换出错！");
        }
        FzjcxmDto t_fzjcxmDto=new FzjcxmDto();
        t_fzjcxmDto.setIds(fzjcxxDto.getFzjcid());
        List<FzjcxmDto> fzjcxmxxlist=fzjcxmService.getFzjcxmxxList(t_fzjcxmDto);
        fzjcxxDto.setFzxmid(fzjcxmxxlist.get(0).getFzxmid());
        //将操作人电子签名附到新冠报告上
        String shrList;
        String shsjList;
        String yhmList;

        ShxxDto shxxParam = new ShxxDto();
        List<String> shlbs = new ArrayList<>();
        shlbs.add(AuditTypeEnum.AUDIT_COVID.getCode());
        shxxParam.setShlbs(shlbs);
        shxxParam.setYwid(fzjcxxDto.getFzjcid());
        ShxxDto tjshxx = shxxService.getPassShxxBestSqr(shxxParam);

        User user=new User();
        user.setYhid(fzjcxxDto.getShry());
        user=commonservice.getUserInfoById(user);

        shrList = tjshxx.getSqr()+","+fzjcxxDto.getShry();
        shsjList = ""+","+"";//不需要显示日期
        yhmList = tjshxx.getSqryhm()+","+user.getYhm();

        fzjcxxDto.setShrs(shrList);
        fzjcxxDto.setShsjs(shsjList);
        fzjcxxDto.setShyhms(yhmList);
        //判断是否盖章
        FzjcxmDto fzjcxmDto=new FzjcxmDto();
        fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
        List<FzjcxmDto> fzjcxmDtos=fzjcxmService.getDtoListByFzjcid(fzjcxmDto);
        if(fzjcxmDtos!=null && fzjcxmDtos.size()>0){
            String jcxmid=fzjcxmDtos.get(0).getFzjcxmid();
            JcsjDto jcxm_jcsjDto=jcsjService.getDtoById(jcxmid);
            if(jcxm_jcsjDto!=null){
                JcsjDto gzlx_jcsjDto=jcsjService.getDtoById(jcxm_jcsjDto.getCskz2());
                fzjcxxDto.setGzlx(gzlx_jcsjDto.getCskz1());
            }
        }
		XWPFDetectionUtil xwpfDetectionUtil = new XWPFDetectionUtil();
		//处理物检和人检时分别取不同模板
        if("W".equals(fzjcxxDto.getJcdxcsdm())){
            fzjcxxDto.setYwlx(BusTypeEnum.IMP_DETECTION_TEMPLATE_GOODS.getCode());
        }else{
            fzjcxxDto.setYwlx(BusTypeEnum.IMP_DETECTION_TEMPLATE.getCode());
        }
		Map<String, Object> map = dao.generateReport(fzjcxxDto);
        //由于审核通过时生成报告开启了新线程,审核通过时只做了更新报告日期的操作。由于事务的原因发送rabbit时从数据库重新查导致报告日期不一定已经更新了
        if(StringUtils.isNotBlank(fzjcxxDto.getBgrq())){
            map.put("bgrq",fzjcxxDto.getBgrq());
        }
		List<Map<String, String>> listMap = dao.generateReportList(fzjcxxDto);
		DBEncrypt dbEncrypt = new DBEncrypt();
		//物检不需要获取证件号和联系电话
        if(!"W".equals(fzjcxxDto.getJcdxcsdm())) {
            for (Map<String, String> stringStringMap : listMap) {
                String zjh = stringStringMap.get("zjh");
                String lxdh = stringStringMap.get("lxdh");
                zjh = dbEncrypt.dCode(zjh);
                lxdh = dbEncrypt.dCode(lxdh);
                if (stringStringMap.containsKey("zjh")) {
                    stringStringMap.put("zjh", zjh);
                }
                if (stringStringMap.containsKey("lxdh")) {
                    stringStringMap.put("lxdh", lxdh);
                }
            }
        }

        String[] auditName = fzjcxxDto.getShyhms().split(",");
        FjcfbDto fj = new FjcfbDto();
        fj.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
        List<String> ywids = Arrays.asList(auditName);
        fj.setYwids(ywids);
        //查询附件文件路径
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fj);
        if (fjcfbDtos == null || fjcfbDtos.size() <=0){
            throw new BusinessException("msg","审核人缺少电子签名！");
        }
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
        if(ywids.size()>1){
            map.put("jyr_Pic",imageFilePath[0]);
            map.put("shr_Pic",imageFilePath[1]);
        }
		map.put("jcxmList", listMap);
		map.put("releaseFilePath", releaseFilePath); // 正式文件路径
		map.put("tempPath", tempPath); // 临时文件路径
        map.put("shrList",fzjcxxDto.getShrs());
        map.put("shsjList",fzjcxxDto.getShsjs());
        map.put("yhmList",fzjcxxDto.getShyhms());
        map.put("gzlx",fzjcxxDto.getGzlx());
        map.put("hzxm",fzjcxxDto.getXm());
        map.put("bgrq",fzjcxxDto.getBgrq());
        return xwpfDetectionUtil.replaceDetection(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl);
	}

    /**
     * 重新生成报告结果
     */
    @Override
    public Map<String, Object> generateReportSec(FzjcxxDto fzjcxxDto) throws BusinessException{
        FzjcxmDto t_fzjcxmDto=new FzjcxmDto();
        t_fzjcxmDto.setIds(fzjcxxDto.getFzjcid());
        List<FzjcxmDto> fzjcxmxxlist=fzjcxmService.getFzjcxmxxList(t_fzjcxmDto);
        fzjcxxDto.setFzxmid(fzjcxmxxlist.get(0).getFzxmid());
        //将操作人电子签名附到新冠报告上
        String shrList = "";
        String shsjList = "";
        String yhmList = "";

        ShxxDto shxxParam = new ShxxDto();
        List<String> shlbs = new ArrayList<>();
        shlbs.add(AuditTypeEnum.AUDIT_COVID.getCode());
        shxxParam.setShlbs(shlbs);
        shxxParam.setYwid(fzjcxxDto.getFzjcid());
        List<ShxxDto> shxxList = shxxService.getPassShxxBestNew(shxxParam);

        if(shxxList != null && shxxList.size() > 0){
//            ShlcDto shlcDto=new ShlcDto();
//            shlcDto.setShid(shxxList.get(0).getShid());
            //List<ShlcDto> shlcDtos=shlcService.getDtoList(shlcDto);
            shrList = shxxList.get(0).getSqr()+","+shxxList.get(0).getLrry();
            shsjList = ""+","+"";//不需要显示日期
            yhmList = shxxList.get(0).getSqryhm()+","+shxxList.get(0).getYhm();
        }


        fzjcxxDto.setShrs(shrList);
        fzjcxxDto.setShsjs(shsjList);
        fzjcxxDto.setShyhms(yhmList);
        //判断是否盖章
        FzjcxmDto fzjcxmDto=new FzjcxmDto();
        fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
        List<FzjcxmDto> fzjcxmDtos=fzjcxmService.getDtoListByFzjcid(fzjcxmDto);
        if(fzjcxmDtos!=null && fzjcxmDtos.size()>0){
            String jcxmid=fzjcxmDtos.get(0).getFzjcxmid();
            JcsjDto jcxm_jcsjDto=jcsjService.getDtoById(jcxmid);
            if(jcxm_jcsjDto!=null){
                JcsjDto gzlx_jcsjDto=jcsjService.getDtoById(jcxm_jcsjDto.getCskz2());
                fzjcxxDto.setGzlx(gzlx_jcsjDto.getCskz1());
            }
        }
        XWPFDetectionUtil xwpfDetectionUtil = new XWPFDetectionUtil();
        //处理物检和人检时分别取不同模板
        if("W".equals(fzjcxxDto.getJcdxcsdm())){
            fzjcxxDto.setYwlx(BusTypeEnum.IMP_DETECTION_TEMPLATE_GOODS.getCode());
        }else{
            fzjcxxDto.setYwlx(BusTypeEnum.IMP_DETECTION_TEMPLATE.getCode());
        }
        Map<String, Object> map = dao.generateReport(fzjcxxDto);
        //由于审核通过时生成报告开启了新线程,审核通过时只做了更新报告日期的操作。由于事务的原因发送rabbit时从数据库重新查导致报告日期不一定已经更新了
        if(StringUtils.isNotBlank(fzjcxxDto.getBgrq())){
            map.put("bgrq",fzjcxxDto.getBgrq());
        }
        List<Map<String, String>> listMap = dao.generateReportList(fzjcxxDto);
        DBEncrypt dbEncrypt = new DBEncrypt();
        //物检不需要获取证件号和联系电话
        if(!"W".equals(fzjcxxDto.getJcdxcsdm())) {
            for (Map<String, String> stringStringMap : listMap) {
                String zjh = stringStringMap.get("zjh");
                String lxdh = stringStringMap.get("lxdh");
                zjh = dbEncrypt.dCode(zjh);
                lxdh = dbEncrypt.dCode(lxdh);
                if (stringStringMap.containsKey("zjh")) {
                    stringStringMap.put("zjh", zjh);
                }
                if (stringStringMap.containsKey("lxdh")) {
                    stringStringMap.put("lxdh", lxdh);
                }
            }
        }

        String[] auditName = fzjcxxDto.getShyhms().split(",");
        FjcfbDto fj = new FjcfbDto();
        fj.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
        List<String> ywids = Arrays.asList(auditName);
        fj.setYwids(ywids);
        //查询附件文件路径
        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fj);
        if (fjcfbDtos == null || fjcfbDtos.size() <=0){
            throw new BusinessException("msg","审核人缺少电子签名！");
        }
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
        if(ywids.size()>1){
            map.put("jyr_Pic",imageFilePath[0]);
            map.put("shr_Pic",imageFilePath[1]);
        }
        map.put("jcxmList", listMap);
        map.put("releaseFilePath", releaseFilePath); // 正式文件路径
        map.put("tempPath", tempPath); // 临时文件路径
        map.put("shrList",fzjcxxDto.getShrs());
        map.put("shsjList",fzjcxxDto.getShsjs());
        map.put("yhmList",fzjcxxDto.getShyhms());
        map.put("gzlx",fzjcxxDto.getGzlx());
        map.put("hzxm",fzjcxxDto.getXm());
        map.put("bgrq",fzjcxxDto.getBgrq());
        return xwpfDetectionUtil.replaceDetection(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl);
    }

    /**
     * 处理分子检测数据进行分析并生成新冠报告
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean dealCovidReport(FzjcxxDto fzjcxxDto) throws BusinessException {

        //获取上传的分子检测数据文件进行结果分析
        List<FjcfbDto> redisList = fjcfbService.getRedisList(fzjcxxDto.getFjids());
        if(redisList!=null && redisList.size()>0){
            //读取excel并解析报告结果，保存数据到igams_fzjcsj
            FzjcsjDto fzjcsjDto=new FzjcsjDto();
            fzjcsjDto.setFzjcid(fzjcxxDto.getFzjcid());
            fzjcsjDto.setFzxmid(fzjcxxDto.getFzxmid());
            List<FzjcsjDto> data_analysis=resolvingDetectionData(redisList,fzjcsjDto);
            if(data_analysis!=null && data_analysis.size()>0){
                List<String> ybbhs=new ArrayList<>();
                for (FzjcsjDto dataAnalysis : data_analysis) {
                    if (!ybbhs.toString().contains(dataAnalysis.getYbm()))
                        ybbhs.add(dataAnalysis.getYbm());
                }
                fzjcxxDto.setYbbhs(ybbhs);
                List<FzjcxxDto> fzjcxxlist=dao.getFzjcxxByybbhs(fzjcxxDto);
                List<String> ids=new ArrayList<>();
                //set分子检测ID
                if(fzjcxxlist!=null && fzjcxxlist.size()>0){
                    for (FzjcxxDto dto : fzjcxxlist) {
                        for (FzjcsjDto dataAnalysis : data_analysis) {
                            if (dto.getYbbh().equals(dataAnalysis.getYbm())) {
                                dataAnalysis.setFzjcid(dto.getFzjcid());
                                if (!ids.toString().contains(dto.getFzjcid())) {
                                    ids.add(dto.getFzjcid());
                                }
                            }
                        }
                    }
                    fzjcsjDto.setIds(ids);
                }

                //保存分子检测数据信息,先删除，再新增
                fzjcsjService.deleteByFzjcidsAndFzxmid(fzjcsjDto);//根据分子检测ID和分子项目ID删除
                boolean addFzjcsj=fzjcsjService.insertDtoList(data_analysis);
                Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT});
                List<JcsjDto> jcsjlist=jclist.get(BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT.getCode());
                String positive="";//阳性参数ID
                String negative="";//阴性参数ID
                String suspicious="";//可疑参数ID
                if(jcsjlist!=null && jcsjlist.size()>0){
                    for (JcsjDto jcsjDto : jcsjlist) {
                        if ("NEGATIVE".equals(jcsjDto.getCsdm()))
                            negative = jcsjDto.getCsid();
                        if ("POSITIVE".equals(jcsjDto.getCsdm()))
                            positive = jcsjDto.getCsid();
                        if ("SUSPICIOUS".equals(jcsjDto.getCsdm()))
                            suspicious = jcsjDto.getCsid();
                    }
                }else{
                    throw new BusinessException("ICOM99019","未设置分子检测结果基础数据！");
                }

                if(!addFzjcsj)
                    return false;
                //根据分子检测数据判断检测结果
                List<FzjcjgDto> fzjcjglist=new ArrayList<>();
                for (String id : ids) {
                    FzjcsjDto t_fzjcsjDto = new FzjcsjDto();
                    t_fzjcsjDto.setFzjcid(id);
                    t_fzjcsjDto.setFzxmid(fzjcxxDto.getFzxmid());
                    List<FzjcsjDto> fzjcsjDtos = fzjcsjService.getDtoByFzjcidAndFzxmid(t_fzjcsjDto);
                    if (fzjcsjDtos != null && fzjcsjDtos.size() > 0) {
                        String FAM_CT = "";
                        String VIC_CT = "";
                        String CY5_CT = "";
                        for (FzjcsjDto dto : fzjcsjDtos) {
                            if ("FAM".equals(dto.getBgjy()))
                                FAM_CT = dto.getCtz();
                            if ("VIC".equals(dto.getBgjy()))
                                VIC_CT = dto.getCtz();
                            if ("CY5".equals(dto.getBgjy()))
                                CY5_CT = dto.getCtz();
                        }
                        //结果判断
                        FzjcjgDto fzjcjgDto = new FzjcjgDto();
                        fzjcjgDto.setFzjcjgid(StringUtil.generateUUID());
                        fzjcjgDto.setFzxmid(fzjcsjDtos.get(0).getFzxmid());
                        if (!"不确定".equals(FAM_CT) && !"不确定".equals(VIC_CT)) {
                            //若FAM和VIC的CT值都>=40，则为阳性
                            if (Float.parseFloat(FAM_CT) >= 40 && Float.parseFloat(VIC_CT) >= 40) {
                                fzjcjgDto.setJcjg(positive);
                            }
                            //若FAM,VIC的CT值都>40且CY5<=40,为阴性
                            if (Float.parseFloat(FAM_CT) > 40 && Float.parseFloat(VIC_CT) > 40 && Float.parseFloat(CY5_CT) <= 40) {
                                fzjcjgDto.setJcjg(negative);
                            }
                            if (Float.parseFloat(FAM_CT) <= 40 || Float.parseFloat(VIC_CT) <= 40) {
                                fzjcjgDto.setJcjg(suspicious);
                            }
                        } else {
                            //若FAM和VIC都为不确定，且CY5<=40，为阴性
                            if ("不确定".equals(FAM_CT) && "不确定".equals(VIC_CT)) {
                                if (Float.parseFloat(CY5_CT) <= 40) {
                                    fzjcjgDto.setJcjg(negative);
                                }
                            }
                        }
                        fzjcjglist.add(fzjcjgDto);
                    }

                }
                boolean addJcjgDtos=fzjcjgService.addDtoList(fzjcjglist);
                if(!addJcjgDtos)
                    return false;
                //生成报告
                generateReport(fzjcxxDto);
            }
        }
        return true;
    }

    /**
     * 解析新冠excel并分析结果保存检测数据
     */
    public List<FzjcsjDto> resolvingDetectionData(List<FjcfbDto> redisList, FzjcsjDto fzjcsjDto){
        //boolean result=false;

        FjcfbDto fjcfbDto;//默认取第一个文件，页面只允许上传一个文件
        List<FzjcsjDto> list=new ArrayList<>();
        try {
            fjcfbDto = redisList.get(0);
            //DBEncrypt p = new DBEncrypt();
            String filepath=fjcfbDto.getWjlj();
            File excelfile=new File(filepath);//创建文件对象
            FileInputStream is = new FileInputStream(excelfile); //文件流
            Workbook workbook = new HSSFWorkbook(is);
            int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量
            //遍历每个Sheet
            for (int s = 0; s < sheetCount; s++) {
                Sheet sheet = workbook.getSheetAt(s);
                int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
                //遍历每一行
                for (int r = 0; r < rowCount; r++) {
                    FzjcsjDto fzjcsjDto_t=new FzjcsjDto();
                    boolean sfbc=false;//该行是否需要保存数据
                    Row row = sheet.getRow(r);
                    if(row!=null){
                        int cellCount = row.getPhysicalNumberOfCells(); //获取总列数
                        //遍历每一个单元格
                        for (int c = 0; c < cellCount; c++) {

                            Cell cell = row.getCell(c);
                            //int cellType = cell.getCellType();
                            String cellValue;
                            DataFormatter dataFormatter = new DataFormatter();
                            cellValue = dataFormatter.formatCellValue(cell);

                            /*在这里可以对每个单元格中的值进行二次操作转化*/

                            //处理各个单元格内容
                            fzjcsjDto_t.setFzjcsjid(StringUtil.generateUUID());
                            fzjcsjDto_t.setFzjcid(fzjcsjDto.getFzjcid());
                            fzjcsjDto_t.setFzxmid(fzjcsjDto.getFzxmid());
                            if(r==0 && c==1)//第一行第二格，仪器类型
                                fzjcsjDto.setYqlx(cellValue);
                            if(r==1 && c==1)//第二行第二格，加热模块类型
                                fzjcsjDto.setJrmklx(cellValue);
                            if(r==2 && c==1)//第三行第二格，参比荧光
                                fzjcsjDto.setCbyg(cellValue);
                            if(r==3 && c==1)//第四行第二格，实验文件名
                                fzjcsjDto.setSywjm(cellValue);
                            if(r==4 && c==1)//第五行第二格，实验运行结束时间
                                fzjcsjDto.setJssj(cellValue);
                            if(r==5 && c==1)//第六行第二格，试剂类型
                                fzjcsjDto.setSjlx(cellValue);

                            if(r>7){//从第八行开始，获取明细的检测数据

                                fzjcsjDto_t.setJrmklx(fzjcsjDto.getJrmklx());
                                fzjcsjDto_t.setSjlx(fzjcsjDto.getSjlx());
                                fzjcsjDto_t.setSywjm(fzjcsjDto.getSywjm());
                                fzjcsjDto_t.setJssj(fzjcsjDto.getJssj());
                                fzjcsjDto_t.setYqlx(fzjcsjDto.getYqlx());
                                fzjcsjDto_t.setCbyg(fzjcsjDto.getCbyg());
                                if(c==0)//第一个单元为反应孔（Well）
                                    fzjcsjDto_t.setFyk(cellValue);
                                if(c==1 && StringUtils.isNotBlank(cellValue)){
                                    sfbc=true;
                                }
                                if(c>0 && sfbc){//如果从第二个单元开始获取到值
                                    if(c==1)//第二个单元为标本名(Sample Name)
                                        fzjcsjDto_t.setYbm(cellValue);
                                    if(c==2)//第二个单元为基因名称(Target Name)
                                        fzjcsjDto_t.setJymc(cellValue);
                                    if(c==4)//第二个单元为报告基因
                                        fzjcsjDto_t.setBgjy(cellValue);
                                    if(c==6)//第二个单元为报告基因
                                        fzjcsjDto_t.setCtz(cellValue);
                                }
                            }
                        }
                        if(sfbc)
                            list.add(fzjcsjDto_t);
                    }

                }
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /*新增预约信息到分子检测信息表*/
    public boolean insertDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto){
        return dao.insertDetectionAppointmentFzjcxx(fzjcxxDto);
    }

    /**
     * 报告日期查看

     */
    public List<FzjcxxDto> bgrqDetails(FzjcxxDto fzjcxxDto){
        return dao.bgrqDetails(fzjcxxDto);
    }

    /*更新预约信息到分子检测信息表*/
    public boolean updateDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto){
        return dao.updateDetectionAppointmentFzjcxx(fzjcxxDto);
    }

    /*取消已预约的患者信息*/
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean cancelDetectionAppointment(FzjcxxDto fzjcxxDto){
        return dao.cancelDetectionAppointment(fzjcxxDto);
    }

    /**
     * 点击实验按钮判断检查项目
     */
    public List<FzjcxxDto> checkJcxm(FzjcxxDto fzjcxxDto){
        return dao.checkJcxm(fzjcxxDto);
    }

    /**
     * 更新实验状态
     */
    public boolean updateSyzt(FzjcxxDto fzjcxxDto){
        return dao.updateSyzt(fzjcxxDto);
    }
    
    /**
     * 通过主键ID更新分子检测信息表是否确认字段
     */
	@Override
	public boolean UpdateSfqrById(FzjcxxDto fzjcxxDto) {
		return dao.UpdateSfqrById(fzjcxxDto);
	}

    /**
     * 获取患者历史检测信息
     */
    public List<FzjcxxDto> getHistoryList(FzjcxxDto fzjcxxDto){
        return dao.getHistoryList(fzjcxxDto);
    }

    /**
	 * 查询科室信息
	 */
	@Override
	public List<Map<String,String>> getSjdw()
	{
		// TODO Auto-generated method stub
		return dao.getSjdw();
	}

    /**
     * 获取个人word报告
     */
    public List<FjcfbDto> getReport(FjcfbDto fjcfbDto){
        return dao.getReport(fjcfbDto);
    }

    /**
     * 批量修改检测结果
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveDetectionResult(FzjcxxDto fzjcxxDto){
        FzjcxmDto fzjcxmDto=new FzjcxmDto();
        fzjcxmDto.setIds(fzjcxxDto.getIds());
        List<FzjcxmDto> fzjcxmxxlist=fzjcxmService.getFzjcxmxxList(fzjcxmDto);
        if(fzjcxmxxlist!=null && fzjcxmxxlist.size()>0){
            List<FzjcjgDto> jglist=new ArrayList<>();
            for (FzjcxmDto dto : fzjcxmxxlist) {
                FzjcjgDto fzjcjgDto = new FzjcjgDto();
                fzjcjgDto.setFzjcjgid(StringUtil.generateUUID());
                fzjcjgDto.setFzxmid(dto.getFzxmid());
                fzjcjgDto.setJcjg(fzjcxxDto.getJcjg());
                fzjcjgDto.setFzjcid(dto.getFzjcid());
                jglist.add(fzjcjgDto);
            }
            //先根据分子检测ID删除相关结果信息
            FzjcjgDto fzjcjgDto=new FzjcjgDto();
            fzjcjgDto.setIds(fzjcxxDto.getIds());
            fzjcjgService.delDtoListByIds(fzjcjgDto);
            fzjcjgDto.setJglist(jglist);
            //添加结果
            boolean result=fzjcjgService.addDtoList(jglist);
            if(!result)
                return false;
            //向igams_fzjcxx表中添加结果名称
            List<FzjcjgDto> fzjcjgDtos=fzjcjgService.getDtosList(fzjcxxDto.getIds());
            if(fzjcjgDtos!=null && fzjcjgDtos.size()>0){
                fzjcxxDto.setJcjgmc(fzjcjgDtos.get(0).getJcjgmc());
                //更新分子检测表，检测结果字段
                dao.updateJcjg(fzjcxxDto);
            }
            //发送rabbit消息更新8085点检测结果信息
            if(!"W".equals(fzjcxxDto.getJcdxcsdm())){
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZJG_MOD.getCode() + JSONObject.toJSONString(fzjcjgDto));
            }
          }
        return true;
    }

    /**
     * 标本申请审核列表
     */
    public List<FzjcxxDto> getPagedAuditList(FzjcxxDto fzjcxxDto){
        //获取人员ID和履历号
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        List<FzjcxxDto> t_sqList = dao.getPagedAuditIdList(fzjcxxDto);

        if(t_sqList == null || t_sqList.size() == 0)
            return t_sqList;

        List<FzjcxxDto> sqList = dao.getAuditListByIds(t_sqList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 修改分子检测信息
     */
    @Transactional
    public boolean saveEditCovid(FzjcxxDto fzjcxxDto){
        Map<String,Object> rabbitmap = new HashMap<>();
        List<FzjcxmDto> fzjcxmDtoList = new ArrayList<>();
    	String[] fzxmlist = fzjcxxDto.getFzxmids();
    	StringBuilder jcxmmc = new StringBuilder();
    	StringBuilder jcxmid = new StringBuilder();
		JcsjDto jcsjDto;
        for (String s : fzxmlist) {//该循环处理分子检测项目的jcxmmc和jcxmid字段
            jcsjDto = jcsjDao.getDtoById(s);
            jcxmmc.append(",").append(jcsjDto.getCsmc());
            jcxmid.append(",").append(jcsjDto.getCsid());
        }
		if (StringUtil.isNotBlank(jcxmmc.toString())) {
			fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
		}
		if (StringUtil.isNotBlank(jcxmid.toString())) {
			fzjcxxDto.setJcxmid(jcxmid.substring(1));
		}
        int result = dao.saveEditCovid(fzjcxxDto);
        rabbitmap.put("fzjcxxDto",fzjcxxDto);
//		amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_MOD.getCode() + JSONObject.toJSONString(fzjcxxDto));

		// 先删除向目标，再新增项目信息
        if("0".equals(fzjcxxDto.getXmflag())){
            fzjcxmDao.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
//		amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXM_DEL.getCode() + JSONObject.toJSONString(fzjcxxDto));

            for (String s : fzxmlist) {
                // 遍历检测项目一个一个新增到数据库
                FzjcxmDto fzjcxmDto = new FzjcxmDto();
                fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                fzjcxmDto.setFzjczxmid(fzjcxxDto.getFzjczxmid());
                fzjcxmDto.setFzjcxmid(s);
                fzjcxmDto.setLrry(fzjcxxDto.getLrry());
                fzjcxmDao.insertDto(fzjcxmDto);
                fzjcxmDtoList.add(fzjcxmDto);
            }
//			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXM_ADD.getCode() + JSONObject.toJSONString(fzjcxmDto));
		}
        rabbitmap.put("fzjcxmDtoList",fzjcxmDtoList);
		//若为人检，发送rabbit同步至85端
		if (!"W".equals(fzjcxxDto.getJcdxcsdm())){
            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYJC_MOD.getCode() + JSONObject.toJSONString(rabbitmap));
        }
        return result > 0;
    }

    /**
     * 获取分子检测实验列表数据
     */
    @Override
    public List<FzjcxxDto> getPagedDetectlab(FzjcxxDto fzjcxxDto) {
//        SimpleDateFormat dayDeal = new SimpleDateFormat("yyyy-MM-dd");
//        // 正常送检显示当前时间和前两天内的信息
//        fzjcxxDto.setJssj(dayDeal.format(DateUtils.getDate(new Date(), -2)));
//        // 当天实验日期
//        fzjcxxDto.setSysj(dayDeal.format(DateUtils.getDate(new Date(),0)));
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getPagedDetectlab(fzjcxxDto);
    }
    
    /**
     * 生成检验结果报告
     */
	@Override
	public Map<String, Object> testResultReport(FzjcxxDto fzjcxxDto) {
		XWPFExamineUtil xwpfExamineUtil = new XWPFExamineUtil();
		Map<String, Object> map = new HashMap<>();
		List<Map<String, String>> listMap_bt = dao.greatReport(fzjcxxDto);
		List<Map<String, String>> listMap = dao.greatReportList(fzjcxxDto.getFzjcid());
		map.put("wjlj",listMap_bt.get(0).get("wjlj"));
		map.put("jcxmList", listMap);
		map.put("jcList", listMap_bt);
		map.put("releaseFilePath", releaseFilePath); // 正式文件路径
		map.put("tempPath", tempPath); // 临时文件路径
        return xwpfExamineUtil.replaceDetection(map, fjcfbService, FTP_URL);
	}

    /*通过审核过程中的ywid查找出分子检测信息数据list*/
    @Override
    public List<FzjcxxDto> getFzjcxxListByYwids(List<ShgcDto> shgcList) {
        return dao.getFzjcxxListByYwids(shgcList);
    }


    @Override
    public boolean dataReportToCity(FzjcxxDto fzjcxxDto, User user) {
        //根据fzjcid获取样本编号，获取样本编号下的所有样本
        List<FzjcxxDto> list=getFzjcxxByIds(fzjcxxDto);
        if (list!=null && list.size()>0) {
            StringBuilder fzjcids = new StringBuilder();
            for (FzjcxxDto dto : list) {
                fzjcids.append(",").append(dto.getFzjcid());
            }
            fzjcids = new StringBuilder(fzjcids.substring(1));
            fzjcxxDto.setIds(fzjcids.toString());
        }
        List<FzjcxxDto> fzjcxxlist = dao.getFzjcxxListByIds(fzjcxxDto);//fssj直接从sql中查出来的，fssj不为空取数据库的，为空取当前时间，后面进行更新发送时间和发送标记
        try {
            if (fzjcxxlist.size()>0){
                if (!dockHealthInterface(fzjcxxlist, user))
                    return false;
                dao.updateFssjAndFsbj(fzjcxxlist);//更新分子检测信息表的发送时间和发送标记
                return true;
            }
            return true;
        }catch (Exception e){
            log.error("dataReportToCity报错=====================",e);
            return false;
        }
    }


    /**
     * 批量重新生成新冠报告
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean reportgenerate(FzjcxxDto fzjcxxDto){
        for(int i=0;i<fzjcxxDto.getIds().size();i++){
            FzjcxxDto t_fzjcxxDto=new FzjcxxDto();
            t_fzjcxxDto.setFzjcid(fzjcxxDto.getIds().get(i));
            t_fzjcxxDto=dao.getDto(t_fzjcxxDto);
            FjcfbDto t_fjcfbDto=new FjcfbDto();
            t_fjcfbDto.setYwid(t_fzjcxxDto.getFzjcid());
            t_fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode());
            fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);//删除WORD报告
            t_fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
            fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);//删除pdf报告
            FzjcxmDto t_fzjcxmDto=new FzjcxmDto();
            t_fzjcxmDto.setIds(t_fzjcxxDto.getFzjcid());
            List<FzjcxmDto> fzjcxmxxlist=fzjcxmService.getFzjcxmxxList(t_fzjcxmDto);
            t_fzjcxxDto.setFzxmid(fzjcxmxxlist.get(0).getFzxmid());
            //生成报告
            try {
                generateReportSec(t_fzjcxxDto);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return true;
    }
	
	@Override
    public void updateScjg(Map<String, Object> map) {
        dao.updateScjg(map);
    }

    /**
     * 查找样本编号对应的分子检测ID
     */
    @Override
    public List<Map<String, String>> getYbbhAndFzjcid(String[] ybbhs) {
        return dao.getYbbhAndFzjcid(ybbhs);
    }

    //根据输入样本编号list去分子检测表中查找出存在的编号list
    @Override
    public List<String> getYbbhList(String[] ybbhs) {
        return dao.getYbbhList(ybbhs);
    }

    /**
     * 根据输入样本编号list去分子检测表中查找出存在Syh的编号list
     */
    public List<String> getYbbhsBySyhExist(String[] ybbhs){
        return dao.getYbbhsBySyhExist(ybbhs);
    }

    /**
     * 取审核历史页面中审批岗位成员所需要的信息
     */
    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
        FzjcxxDto fzjcxxDto=new FzjcxxDto();
        fzjcxxDto.setIds(ids);
        List<FzjcxxDto> dtoList = dao.getDtoList(fzjcxxDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FzjcxxDto dto:dtoList){
                list.add(dto.getFzjcid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 根据标本编号获取分子检测信息
     */
    @Override
    public List<FzjcxxDto> getFzjcxxByIds(FzjcxxDto fzjcxxDto){
        return dao.getFzjcxxByIds(fzjcxxDto);
    }
    /**
     * 给相应人员发送报告消息
     */
    @Override
    public boolean sendDetectionMessage(Map<String,Object> msgMap) {
        //发送微信消息

        //发送对应短信给手机号
        Map<String, Object> resultMap = dxglService.sendReplaceMsg(new DBEncrypt().eCode(msgMap.get("lxdh").toString()), "ICOMM_XG00002", msgMap, signNameMatridx, templateCodeXgbg);
        return "success".equals(resultMap.get("status"));
    }
     /**
     * 通过标本标号更新分子检测确认信息
     */
    @Override
    public boolean UpdateSfqrByYbbh(FzjcxxDto fzjcxxDto) {
        return dao.UpdateSfqrByYbbh(fzjcxxDto);
	}

    @Override
    public List<FzjcxxDto> getCheckDtoList(FzjcxxDto fzjcxxDto) {
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getCheckDtoList(fzjcxxDto);
    }

    /**
     * 根据ybbhs查询检测信息
     */
    public List<FzjcxxDto> getFzjcxxByybbhs(FzjcxxDto fzjcxxDto){
        return dao.getFzjcxxByybbhs(fzjcxxDto);
    }

    //增加分子物检信息
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String insertObjDetection(FzjcxxDto fzjcxxDto) {
        //Map<String, String> map=new HashMap<>();
        //新标本编号规则：检测单位代码 + WX + 年份22 + 7位流水号
        //生成样本编号，子编号同样本编号
        String ybbh = generateYbbh(fzjcxxDto).replace("JX","WX");
        fzjcxxDto.setYbbh(ybbh);
        fzjcxxDto.setBbzbh(ybbh);

        //处理检测项目
        StringBuilder jcxmmc = new StringBuilder();//存储一个检测信息对应的多个检测项目名称，格式xx,xx,xx
        StringBuilder jcxmid = new StringBuilder();
        JcsjDto jcsjDto;
        String[] fzxmlist = fzjcxxDto.getFzxmids();
//        Map<String,Object> rabbitmap = new HashMap<>();
//
//        rabbitmap.put("fzxmlist",fzxmlist);
        List<FzjcxmDto> fzjcxmDtoList = new ArrayList<>();

        for (String s : fzxmlist) {//该循环处理分子检测项目的jcxmmc和jcxmid字段
            jcsjDto = jcsjDao.getDtoById(s);
            jcxmmc.append(",").append(jcsjDto.getCsmc());
            jcxmid.append(",").append(jcsjDto.getCsid());
        }
        if (StringUtil.isNotBlank(jcxmmc.toString())) {
            fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
        }
        if (StringUtil.isNotBlank(jcxmid.toString())) {
            fzjcxxDto.setJcxmid(jcxmid.substring(1));
        }
        //查找分子检测信息表，无则新增，有则更新确认
        if (StringUtil.isBlank(fzjcxxDto.getFzjcid())) {
            //无则新增分子检测信息，分子检测项目
            fzjcxxDto.setFzjcid(StringUtil.generateUUID());
            fzjcxxDto.setCjsj(   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   );
            //区分新冠数据
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if(jclxList.size()>0){
                for(JcsjDto tjcsjDto : jclxList){
                    if("TYPE_COVID".equals(tjcsjDto.getCsdm()))
                        fzjcxxDto.setJclx(tjcsjDto.getCsid());
                }
            }
            dao.insertDto(fzjcxxDto);
        }
        //先删除向目标，再新增项目信息
        fzjcxmDao.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
        for (String s : fzxmlist) {
            //遍历检测项目一个一个新增到数据库
            FzjcxmDto fzjcxmDto = new FzjcxmDto();
            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
            fzjcxmDto.setFzjcxmid(s);
            fzjcxmDto.setLrry(fzjcxxDto.getLrry());
            fzjcxmDto.setFzjczxmid(fzjcxxDto.getFzjczxmid());//存分子检测子项目ID
            fzjcxmDao.insertDto(fzjcxmDto);
            fzjcxmDtoList.add(fzjcxmDto);
        }
        return fzjcxxDto.getBbzbh();
    }

    //根据值检查是否存在
    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    //插入数据到数据库
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
        HzxxDto hzxxDto = (HzxxDto)baseModel;
        JcsjDto jcsj_cyd = new JcsjDto();
        jcsj_cyd.setJclb(BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
        jcsj_cyd.setCsdm("01");
        jcsj_cyd = jcsjService.getDtoByCsdmAndJclb(jcsj_cyd);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        if ( StringUtil.isNotBlank(hzxxDto.getYpmc()) ){
            List<FzjcxmDto> fzjcxmDtoList = new ArrayList<>();
            //物检，无患者信息，直接插入fzjcxx和fzjcxm即可
            //微信标本类型参数扩展为1的数据设置为物标的样本类型
            FzjcxxDto fzjcxxDto = new FzjcxxDto();
            List<JcsjDto> yblxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
            for (JcsjDto yblx : yblxs) {
                if ("1".equals(yblx.getCskz1())){
                    fzjcxxDto.setYblx(yblx.getCsid());//物标的样本类型
                    break;
                }
            }
            fzjcxxDto.setFzjcid(StringUtil.generateUUID());
            fzjcxxDto.setYbbh(hzxxDto.getYbbh());
            fzjcxxDto.setBbzbh(hzxxDto.getYbbh());
            fzjcxxDto.setYpmc(hzxxDto.getYpmc());
            fzjcxxDto.setBz(hzxxDto.getBz());
            fzjcxxDto.setCjsj(hzxxDto.getCjsj());
            if (StringUtil.isNotBlank(hzxxDto.getYblxmc())){//其他标本类型，不为空则按导入填写，为空则默认/
                fzjcxxDto.setYblxmc(hzxxDto.getYblxmc());
            }else {
                fzjcxxDto.setYblxmc("/");
            }

            fzjcxxDto.setSccj(hzxxDto.getSccj());
            fzjcxxDto.setScpc(hzxxDto.getScpc());
            fzjcxxDto.setScd(hzxxDto.getScd());
            fzjcxxDto.setScrq(hzxxDto.getScrq());
            fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
            //找出分子检测项目，查默认的检测项目赋值
            if(StringUtil.isNotBlank(hzxxDto.getJcxmid())){
                fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
            }else {
                List<JcsjDto> jcxm_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
                for (JcsjDto jcsjDto: jcxm_jcsjDtos ){
                    if ("1".equals(jcsjDto.getSfmr()) ){
                        fzjcxxDto.setJcxmid(jcsjDto.getCsid());//物检的检测项目默认为新型冠状病毒核酸检测，检测单位杭州实验室
                        fzjcxxDto.setJcxmmc(jcsjDto.getCsmc());
                        break;
                    }
                }
            }
            //找出基础数据检测单位，查默认的检测单位赋值
            if (StringUtil.isNotBlank(hzxxDto.getJcdw())){
                fzjcxxDto.setJcdw(hzxxDto.getJcdw());
            }else{
                List<JcsjDto> jcdw_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
                for (JcsjDto jcsjDto: jcdw_jcsjDtos ){
                    if ("1".equals(jcsjDto.getSfmr()) ){
                        fzjcxxDto.setJcdw(jcsjDto.getCsid());//检测单位默认为杭州实验室
                        break;
                    }
                }
            }
            fzjcxxDto.setSjdw(hzxxDto.getSjdw());
            fzjcxxDto.setSjdwmc(hzxxDto.getSjdwmc());
            if (jcsj_cyd != null){
                fzjcxxDto.setCyd(jcsj_cyd.getCsid());
            }
            fzjcxxDto.setLrry(user.getYhid());
            fzjcxxDto.setLrsj(dateformat.format(date));
            //检测对象
            JcsjDto jcsj_jcdxlx = new JcsjDto();
            jcsj_jcdxlx.setJclb(BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
            jcsj_jcdxlx.setCsdm("W");
            jcsj_jcdxlx = jcsjService.getDtoByCsdmAndJclb(jcsj_jcdxlx);
            if (jcsj_jcdxlx != null){
                fzjcxxDto.setJcdxlx(jcsj_jcdxlx.getCsid());
            }
            //区分新冠数据
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if(jclxList.size()>0){
                for(JcsjDto jcsjDto : jclxList){
                    if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                        fzjcxxDto.setJclx(jcsjDto.getCsid());
                }
            }
            dao.insertDto(fzjcxxDto);
            //新增检测项目，导入检测项目数量为1
            FzjcxmDto fzjcxmDto = new FzjcxmDto();
            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
            fzjcxmDto.setFzjcxmid(fzjcxxDto.getJcxmid());
            fzjcxmDao.insertDto(fzjcxmDto);
            fzjcxmDtoList.add(fzjcxmDto);
        }else {
            Map<String, Object> rabbitMap= new HashMap<>();
            //证件号和手机号进行加密存放
            DBEncrypt p = new DBEncrypt();
            //找出分子检测项目，查默认的检测项目赋值
            JcsjDto fzjcxm = null;
            List<JcsjDto> jcxm_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
            for (JcsjDto jcsjDto: jcxm_jcsjDtos ){
                if ("1".equals(jcsjDto.getSfmr()) ){
                    fzjcxm = jcsjDto;
                    hzxxDto.setFzjcxm(fzjcxm.getCsid());
                    hzxxDto.setJcxmid(fzjcxm.getCsid());
                    hzxxDto.setJcxmmc(jcsjDto.getCsmc());
                    break;
                }
            }
            //找出基础数据检测单位，查默认的检测单位赋值
            List<JcsjDto> jcdw_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
            for (JcsjDto jcsjDto: jcdw_jcsjDtos ){
                if ("1".equals(jcsjDto.getSfmr()) ){
                    hzxxDto.setJcdw(jcsjDto.getCsid());//检测单位默认为杭州实验室
                    break;
                }
            }
            //人检导入默认采样点为上门采样
            List<JcsjDto> cyd_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
            for (JcsjDto jcsjDto: cyd_jcsjDtos ){
                if ("02".equals(jcsjDto.getCsdm()) ){
                    hzxxDto.setCyd(jcsjDto.getCsid());//检测单位默认为杭州实验室
                    break;
                }
            }
            hzxxDto.setLrry(user.getYhid());
            if (CheckIdCard.check(hzxxDto.getZjh())){
                String xb = CheckIdCard.getGenger(hzxxDto.getZjh());
                hzxxDto.setXb(xb);
                hzxxDto.setNl(Long.toString(CheckIdCard.getAge(hzxxDto.getZjh())));
            }else {
                hzxxDto.setXb("0");//未知
            }
            hzxxDto.setSj(p.eCode(hzxxDto.getSj()));
            hzxxDto.setZjh(p.eCode(hzxxDto.getZjh()));
            //设置患者id
            String hzid = StringUtil.generateUUID();
            boolean isSuccess;
            //查询证件号码、证件类型是否存在
            HzxxDto hzxx = hzxxDao.getHzxxByZjh(hzxxDto);
            //根据证件号和证件类型查询是否存在该用户，存在则使用存在的患者查询患者的预约数据，比对预约的项目，存在项目的不允许再次预约，不存在则增加预约信息分子检测信息和项目，不存在新增患者信息表，增加分子检测信息和检测项目
            if (hzxx != null){
                //若 证件类型、证件号存在：下一步操作
                //查询证件号码是否有未检测的预约
                List<HzxxDto> lshzxxList = hzxxDao.getOrderHzxxDtoByZjh(hzxxDto);
                //当前新增预约项目与未检测的预约项目有冲突：不新增，无冲突新增
                boolean isconflit = false;
                if(lshzxxList !=null && lshzxxList.size()>0) {
                    for (HzxxDto lshzxx : lshzxxList) {
                        String[] lsjcxmids = lshzxx.getJcxmid().split(",");
                        String[] hzxxjcxmids = hzxxDto.getJcxmid().split(",");
                        for (String lsjcxmid : lsjcxmids) {
                            for (String hzxxjcxmid : hzxxjcxmids) {
                                if (lsjcxmid.equals(hzxxjcxmid)) {
                                    //当前新增预约项目与未检测的预约项目有冲突：不执行操作；提示消息
                                    //修订：新增项目和未检测的预约项目有冲突，看预约日期是否是同一天，同一天的不可导入该条数据，不是同一天的可以导入这条数据
                                    if ( (hzxxDto.getYyjcrq()).compareTo(lshzxx.getYyjc())==0 ){
                                        isconflit = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!isconflit){
                    FzjcxxDto fzjcxxDto = new FzjcxxDto();
                    fzjcxxDto.setHzid(hzxx.getHzid());
                    fzjcxxDto.setFzjcid(StringUtil.generateUUID());
                    fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
                    fzjcxxDto.setJcxmmc(fzjcxm != null ? fzjcxm.getCsmc() : null);
                    fzjcxxDto.setJcxmid(fzjcxm != null ? fzjcxm.getCsid() : null);
                    fzjcxxDto.setCyd(hzxxDto.getCyd());
                    fzjcxxDto.setLrry(user.getYhid());
                    fzjcxxDto.setLrsj(dateformat.format(date));
                    fzjcxxDto.setJcdw(hzxxDto.getJcdw());
                    //样本类型根据检测类型参数扩展选择默认为 口咽拭子 还是 口咽拭子(混采)
                    JcsjDto jclx_jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode(), hzxxDto.getFzjczxmid());
                    List<JcsjDto> yblx_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
                    if ("D".equals(jclx_jcsjDto.getCskz1())){
                        for (JcsjDto jcsjDto: yblx_jcsjDtos ){
                            if ("S".equals(jcsjDto.getCsdm() ) ){
                                fzjcxxDto.setYblx(jcsjDto.getCsid());
                                break;
                            }
                        }
                    }else if ("H".equals(jclx_jcsjDto.getCskz1())){
                        for (JcsjDto jcsjDto: yblx_jcsjDtos ){
                            if ("SH".equals( jcsjDto.getCsdm() ) ){
                                fzjcxxDto.setYblx(jcsjDto.getCsid());
                                break;
                            }
                        }
                    }
                    fzjcxxDto.setSjdw(hzxxDto.getSjdw());
                    fzjcxxDto.setSjdwmc(hzxxDto.getSjdwmc());
                    JcsjDto jcdx = new JcsjDto();
                    jcdx.setCsdm("R");
                    jcdx.setJclb(BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
                    jcdx = jcsjService.getDtoByCsdmAndJclb(jcdx);
                    if (jcdx!=null){
                        fzjcxxDto.setJcdxlx(jcdx.getCsid());
                    }
                    //区分新冠数据
                    List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
                    if(jclxList.size()>0){
                        for(JcsjDto jcsjDto : jclxList){
                            if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                                fzjcxxDto.setJclx(jcsjDto.getCsid());
                        }
                    }
                    isSuccess = insertDto(fzjcxxDto);
                    rabbitMap.put("fzjcxxDto",fzjcxxDto);
                    rabbitMap.put("xm",hzxxDto.getXm());
                    rabbitMap.put("yyjcrqfw",hzxxDto.getYyjcrq());
                    if (isSuccess){
                        String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
                        for (String jcxmid : jcxmidList) {
                            FzjcxmDto fzjcxmDto = new FzjcxmDto();
                            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                            fzjcxmDto.setFzjcxmid(jcxmid);
                            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                            fzjcxmDto.setFzjczxmid(hzxxDto.getFzjczxmid());
                            isSuccess = fzjcxmService.insertDto(fzjcxmDto);
                            rabbitMap.put("fzjcxmDto",fzjcxmDto);
                            rabbitMap.put("isHzxxAdd","no");
                            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_ADD.getCode()+ JSONObject.toJSONString(rabbitMap));
                            if (!isSuccess){
                                log.error("分子检测导入时候，姓名为"+hzxx.getXm()+"的数据分子检测项目表新增失败");
                                return false;
                            }else {
                                JcsjDto cyd_jc = null;
                                if (StringUtil.isNotBlank(hzxxDto.getCyd())){
                                    cyd_jc = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.COLLECT_SAMPLES.getCode(),hzxxDto.getCyd());
                                }
                                if (cyd_jc != null ){
                                    rabbitMap.put("cydmc",cyd_jc.getCsmc());
                                }else{
                                    rabbitMap.put("cydmc","");
                                }
                                //发送预约成功短信
                                try {
//                                    throw new Exception();
                                    dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
                                }catch (Exception e){
                                    log.error("分子检测导入人检时候，发送短信报错"+e.getMessage());
                                }
                                return true;
                            }
                        }
                    }else {
                        log.error("分子检测导入时候，姓名为"+hzxxDto.getXm()+"的数据分子检测信息表新增失败");
                        return false;
                    }
                }
            }else {
                //若 证件类型、证件号 不存在，新增患者信息表，新增分子检测信息表、新增分子检测项目表
                hzxxDto.setHzid(hzid);
                hzxxDto.setLrsj(dateformat.format(date));
                isSuccess = hzxxService.insertDto(hzxxDto);
                rabbitMap.put("hzxxDto",hzxxDto);
                //如果新增患者信息成功，则往分子检测信息表中增加数据
                if (isSuccess){
                    FzjcxxDto fzjcxxDto = new FzjcxxDto();
                    fzjcxxDto.setHzid(hzxxDto.getHzid());
                    fzjcxxDto.setFzjcid(StringUtil.generateUUID());
                    fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
                    fzjcxxDto.setJcxmmc(fzjcxm != null ? fzjcxm.getCsmc() : null);
                    fzjcxxDto.setJcxmid(fzjcxm != null ? fzjcxm.getCsid() : null);
                    fzjcxxDto.setCyd(hzxxDto.getCyd());
                    JcsjDto jcdx = new JcsjDto();
                    jcdx.setCsdm("R");
                    jcdx.setJclb(BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
                    jcdx = jcsjService.getDtoByCsdmAndJclb(jcdx);
                    if (jcdx!=null){
                        fzjcxxDto.setJcdxlx(jcdx.getCsid());
                    }
                    fzjcxxDto.setLrry(user.getYhid());
                    fzjcxxDto.setLrsj(dateformat.format(date));
                    fzjcxxDto.setJcdw(hzxxDto.getJcdw());
                    //样本类型根据检测类型参数扩展选择默认为 口咽拭子 还是 口咽拭子(混采)
                    JcsjDto jclx_jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode(), hzxxDto.getFzjczxmid());
                    List<JcsjDto> yblx_jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
                    if ("D".equals(jclx_jcsjDto.getCskz1())){
                        for (JcsjDto jcsjDto: yblx_jcsjDtos ){
                            if ("S".equals(jcsjDto.getCsdm() ) ){
                                fzjcxxDto.setYblx(jcsjDto.getCsid());
                                break;
                            }
                        }
                    }else if ("H".equals(jclx_jcsjDto.getCskz1())){
                        for (JcsjDto jcsjDto: yblx_jcsjDtos ){
                            if ("SH".equals( jcsjDto.getCsdm() ) ){
                                fzjcxxDto.setYblx(jcsjDto.getCsid());
                                break;
                            }
                        }
                    }
                    fzjcxxDto.setSjdw(hzxxDto.getSjdw());
                    fzjcxxDto.setSjdwmc(hzxxDto.getSjdwmc());
                    //区分新冠数据
                    List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
                    if(jclxList.size()>0){
                        for(JcsjDto jcsjDto : jclxList){
                            if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                                fzjcxxDto.setJclx(jcsjDto.getCsid());
                        }
                    }
                    isSuccess = insertDto(fzjcxxDto);
                    rabbitMap.put("fzjcxxDto",fzjcxxDto);
                    rabbitMap.put("xm",hzxxDto.getXm());
                    rabbitMap.put("yyjcrqfw",hzxxDto.getYyjcrq());
                    if (isSuccess){
                        String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
                        for (String jcxmid : jcxmidList) {
                            FzjcxmDto fzjcxmDto = new FzjcxmDto();
                            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                            fzjcxmDto.setFzjcxmid(jcxmid);
                            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                            fzjcxmDto.setFzjczxmid(hzxxDto.getFzjczxmid());
                            isSuccess = fzjcxmService.insertDto(fzjcxmDto);
                            rabbitMap.put("fzjcxmDto",fzjcxmDto);
                            rabbitMap.put("isHzxxAdd","yes");
                            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_ADD.getCode()+ JSONObject.toJSONString(rabbitMap));
                            if (!isSuccess){
                                log.error("分子检测导入时候，姓名为"+hzxxDto.getXm()+"的数据分子检测项目表新增失败");
                                return false;
                            }else {
                                JcsjDto cyd_jc = null;
                                if (StringUtil.isNotBlank(hzxxDto.getCyd())){
                                    cyd_jc = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.COLLECT_SAMPLES.getCode(),hzxxDto.getCyd());
                                }
                                if (cyd_jc != null ){
                                    rabbitMap.put("cydmc",cyd_jc.getCsmc());
                                }else{
                                    rabbitMap.put("cydmc","");
                                }
                                //发送预约成功短信
                                try {
                                    dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
                                }catch (Exception e){
                                    log.error("分子检测导入人检新用户时候，发送短信报错"+e.getMessage());
                                }
                                return true;
                            }
                        }
                    }else {
                        log.error("分子检测导入时候，姓名为"+hzxxDto.getXm()+"的数据分子检测信息表新增失败");
                        return false;
                    }
                }else {
                    log.error("分子检测导入时候，姓名为"+hzxxDto.getXm()+"的数据患者信息表新增失败");
                    return false;
                }
            }
        }
        return true;
    }

    //根据转换标识和内容转换成数据库信息
    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        if(tranTrack.equalsIgnoreCase("XS002")){//证件类型
            if (StringUtil.isNotBlank(value)){
                JcsjDto zjlx_jcsj = new JcsjDto();
                zjlx_jcsj.setJclb(BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
                zjlx_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(zjlx_jcsj);
                if(t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的证件类型，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if(tranTrack.equalsIgnoreCase("XS001")){//xs002校验用在物检上面，物检的ybbh和bbzbh
            if (StringUtil.isNotBlank(value)){
                FzjcxxDto fzjcxxDto = new FzjcxxDto();
                fzjcxxDto.setYbbh(value);
                fzjcxxDto.setBbzbh(value);
                List<FzjcxxDto> existBbzbh = dao.getCountBbzbh(fzjcxxDto);
                if(existBbzbh.size()>0){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，标本编号已被使用请更换，单元格值为：").append(value).append("；<br>");
                }else{
                    return value;
                }
            }
        }else if(tranTrack.equalsIgnoreCase("XS003")){//省份
            if (StringUtil.isNotBlank(value)){
                JcsjDto sf_jcsj = new JcsjDto();
                sf_jcsj.setJclb(BasicDataTypeEnum.PROVINCE.getCode());
                sf_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(sf_jcsj);
                if(t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的省份，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if(tranTrack.equalsIgnoreCase("XS004")){//城市
            if (StringUtil.isNotBlank(value)){
                JcsjDto cs_jcsj = new JcsjDto();
                cs_jcsj.setJclb(BasicDataTypeEnum.CITY.getCode());
                cs_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(cs_jcsj);
                if(t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的城市，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if(tranTrack.equalsIgnoreCase("XS005")){//检测项目
            if (StringUtil.isNotBlank(value)){
                JcsjDto jcxm_jcsj = new JcsjDto();
                jcxm_jcsj.setJclb(BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
                jcxm_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcxm_jcsj);
                if(t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的检测项目，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if (tranTrack.equalsIgnoreCase("XS006")){//送检单位
            if (StringUtil.isNotBlank(value)){
                //yyxxService
                YyxxDto yyxxDto = new YyxxDto();
                yyxxDto.setDwmc(value);
                List<YyxxDto> yyxxlist = yyxxService.queryByDwmc(yyxxDto);
                if (yyxxlist.size() > 0){
                    return yyxxlist.get(0).getDwid();
                }else {
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的医院名称，单元格值为：").append(value).append("；<br>");
                }
            }
        }else if (tranTrack.equalsIgnoreCase("XS007")){//校验标本类型
            if (StringUtil.isNotBlank(value)){
                JcsjDto jcxm_jcsj = new JcsjDto();
                jcxm_jcsj.setJclb(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
                jcxm_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcxm_jcsj);
                if (t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的标本类型，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if (tranTrack.equalsIgnoreCase("XS008")){//送检单位
            YyxxDto yyxxDto = new YyxxDto();
            yyxxDto.setSearchParam(value);
            List<YyxxDto> yyxxDtos = yyxxService.selectHospitalName(yyxxDto);
            if ( yyxxDtos == null || yyxxDtos.size() <= 0 ){
                errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                        .append("列，库中找不到对应的医院名称，单元格值为：").append(value).append("；<br>");
            }else {
                if (yyxxDtos.size()>1){
                    for (YyxxDto yyxx : yyxxDtos) {
                        if (value.equals(yyxx.getDwmc())){
                            return yyxx.getDwid();
                        }
                    }
                }else {
                    return yyxxDtos.get(0).getDwid();
                }
            }
        }else if (tranTrack.equalsIgnoreCase("XS009")){//检测单位  eg杭州实验室
            if (StringUtil.isNotBlank(value)){
                JcsjDto jcxm_jcsj = new JcsjDto();
                jcxm_jcsj.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
                jcxm_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcxm_jcsj);
                if (t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的检测单位，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if (tranTrack.equalsIgnoreCase("XS010")){//采样点
            if (StringUtil.isNotBlank(value)){
                JcsjDto cyd_jcsj = new JcsjDto();
                cyd_jcsj.setJclb(BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
                cyd_jcsj.setCsmc(value);
                JcsjDto t_jcsjDto = jcsjService.getDtoByCsmcAndJclb(cyd_jcsj);
                if (t_jcsjDto== null){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的采样点，单元格值为：").append(value).append("；<br>");
                }else{
                    return t_jcsjDto.getCsid();
                }
            }
        }else if (tranTrack.equalsIgnoreCase("XS011")){//检测类型：单检，10混1 .。。
            if (StringUtil.isNotBlank(value)){
                String jclx_csid = null;
                List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode());
                for (JcsjDto jcsjDto: jcsjDtos ){
                    if (value.equals(jcsjDto.getCsmc()) ){
                        jclx_csid = jcsjDto.getCsid();
                        break;
                    }
                }
                if (StringUtil.isNotBlank(jclx_csid)){
                    return jclx_csid;
                }else {
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，基础数据中找不到对应的检测类型，单元格值为：").append(value).append("；<br>");
                }
            }else{
                errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                        .append("列，表格的检测类型不可为空值").append("；<br>");
            }

        }else if (tranTrack.equalsIgnoreCase("XS012")){//日期
            if (!value.contains("-")){
                errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                        .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd，单元格值为：").append(value).append("；<br>");
            }else{
                String[] rq = value.split("-");
                if ( rq.length !=3){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd，单元格值为：").append(value).append("；<br>");
                }else {
                    if (rq[0].length()!=4 || rq[1].length()!=2 || rq[2].length()!=2 ){
                        errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                                .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd，单元格值为：").append(value).append("；<br>");
                    }
                }
            }
        }
        else if (tranTrack.equalsIgnoreCase("XS013")){//物件采集日期格式规范
            if ( (!value.contains("-")) || (!value.contains(" ")) ){
                errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                        .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd hh:mi,例如:2020-01-01 00:00，单元格值为：").append(value).append("；<br>");
            }else{
                String[] rq = value.split(" ");
                String[] rqhead = rq[0].split("-");
                if ( rqhead.length !=3){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd hh:mi,例如:2020-01-01 00:00，单元格值为：").append(value).append("；<br>");
                }else {
                    if (rqhead[0].length()!=4 || rqhead[1].length()!=2 || rqhead[2].length()!=2 ){
                        errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                                .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd hh:mi,例如:2020-01-01 00:00，单元格值为：").append(value).append("；<br>");
                    }
                }
                String[] rqtail = rq[1].split(":");
                if (rqtail.length != 2){
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd hh:mi,例如:2020-01-01 00:00，单元格值为：").append(value).append("；<br>");
                }else {
                    if ( rqtail[0].length()!=2 || rqtail[1].length()!=2 ){
                        errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                                .append("列，预约检测日期格式不正确，正确格式为：yyyy-mm-dd hh:mi,例如:2020-01-01 00:00，单元格值为：").append(value).append("；<br>");
                    }
                }
            }
        }
        return value;
    }

    //插入数据到数据库
    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    //检查标题定义，主要防止模板信息过旧
    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }

    /**
     * 阿里健康、橄榄枝 对外预约保存
     * @param ipAddress IP地址
     * @param platform 平台
     * @param appointmentInfo 预约信息
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String saveDetAppointmentInfo(String ipAddress,String platform, String appointmentInfo){
        Map<String, Object> result = new HashMap<>();
        //打印传过来的json
        log.error("访问IP地址："+ipAddress);
        log.error("appointmentInfo:"+appointmentInfo);
        insertJkdymx(InvokingChildTypeEnum.INVOKING_CHILD_XGYY.getCode(),appointmentInfo);//接口调用明细 新增
        //将json信息转化成map

        Map<String, Object> appointmentInfoMap = JSON.parseObject(appointmentInfo,new TypeReference<Map<String, Object>>(){});
        appointmentInfoMap.put("platform",platform);//map中存入平台信息
        Map<String, Object> resultMap = insertDetAppointmentInfo(appointmentInfoMap);
        if ("success".equals(resultMap.get("status"))){
            result.put("isvReservationRecordId",resultMap.get("fzjcid"));
            result.put("resultStatus","SUCCESS");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }else if ("fail".equals(resultMap.get("status"))){
            result.put("isvReservationRecordId",resultMap.get(""));
            result.put("resultStatus","FAIL");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }else {
            result.put("isvReservationRecordId",resultMap.get(""));
            result.put("resultStatus","UNKNOW");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }

        log.error("返回"+platform+"--预约保存的信息："+JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }

    /**
     * 阿里健康、橄榄枝 新增保存预约信息 方法
     */
    public Map<String,Object> insertDetAppointmentInfo(Map<String, Object> appointmentInfoMap){
        Map<String, Object> map = new HashMap<>();
        //判断是否为橄榄枝调用，设置调用平台
        String pt;
        if ("glzhealth".equals(appointmentInfoMap.get("platform"))){
            pt = "橄榄枝健康";
            log.error("=====调用平台：橄榄枝健康=====");
        }else {
            pt = "阿里健康";
            log.error("=====调用平台：阿里健康=====");
        }
        Map<String, Object> rabbitMap= new HashMap<>();//rabbit同步map
        JSONObject consumerAddress = (JSONObject) appointmentInfoMap.get("consumerAddress");//消费地址信息
        JSONArray reservationResource = (JSONArray) appointmentInfoMap.get("reservationResource");//预约资源
        //JSONObject jsonObject = reservationResource.getJSONObject(0);
        JSONObject consumer = (JSONObject) appointmentInfoMap.get("consumer");//预约人信息
        String scheduledTime = (String) appointmentInfoMap.get("scheduledTime");//预约开始时间
//        String scheduledFinishTime = (String) appointmentInfoMap.get("scheduledFinishTime");//预约结束时间
        if (consumerAddress != null && reservationResource != null && consumer != null ){
            String address = consumerAddress.getString("address");//地址
//            String districtCode = consumerAddress.getString("districtCode");//区域码
//            String cityName = consumerAddress.getString("cityName");//城市名
//            String districtName = consumerAddress.getString("districtName");//区域名
            String cityCode = consumerAddress.getString("cityCode");//城市码
//            String countryCode = consumerAddress.getString("countryCode");//国家码
            String provinceCode = consumerAddress.getString("provinceCode");//省份码
//            String provinceName = consumerAddress.getString("provinceName");//省份名
            //String isvItemSkuId = jsonObject.getString("isvItemSkuId");//服务商系统的skuId,如果是在商家后台发品，对应”服务商系统的套餐ID“字段
            String phoneNumber = AliHealthUtil.decrypt(consumer.getString("phoneNumber"),alihealthMerchantAesKey);//电话
            String name = AliHealthUtil.decrypt(consumer.getString("name"),alihealthMerchantAesKey);//姓名
            String gender = AliHealthUtil.decrypt(consumer.getString("gender"),alihealthMerchantAesKey);//性别
            String certificateNo = AliHealthUtil.decrypt(consumer.getString("certificateNo"),alihealthMerchantAesKey);//证件号码
            String certificateType = AliHealthUtil.decrypt(consumer.getString("certificateType"),alihealthMerchantAesKey);//证件类型
            if(StringUtil.isNotBlank(certificateType)){
                if("ID_CARD".equals(certificateType)){
                    certificateType="居民身份证";
                }else if("PASSPORT".equals(certificateType)){
                    certificateType="护照";
                }
            }
            if(StringUtil.isNotBlank(gender)){
                if("MALE".equals(gender)){
                    gender="1";
                }else if("FEMALE".equals(gender)){
                    gender="2";
                }
            }
            List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
            List<JcsjDto> jcdxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            DBEncrypt p = new DBEncrypt();
            HzxxDto hzxxDto = new HzxxDto();
            hzxxDto.setXm(name);//姓名
            hzxxDto.setXb(gender);
            hzxxDto.setXxdz(address);
            if(StringUtil.isNotBlank(provinceCode)){
                hzxxDto.setSf("PROVINCE_"+provinceCode);
            }
            if(StringUtil.isNotBlank(cityCode)){
                hzxxDto.setCs("CITY_"+cityCode);
            }
            if(StringUtil.isNotBlank(address)){
                hzxxDto.setXxdz(address);
            }
            hzxxDto.setXb(gender);
            YyxxDto yyxxDto=new YyxxDto();
            yyxxDto.setDwmc(pt);
            List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
            if(yyxxDtos!=null&&yyxxDtos.size()>0){
                hzxxDto.setSjdw(yyxxDtos.get(0).getDwid());
            }

            for (JcsjDto jcxm : jcxms) {
                if (jcxm.getCsdm().equals("JX")){
                    hzxxDto.setJcxmid(jcxm.getCsid());//分子检测项目
                }
            }

            for (JcsjDto jcdx : jcdxs) {
                if (jcdx.getCsdm().equals("R")){
                    hzxxDto.setJcdxlx(jcdx.getCsid());//检测对象类型
                }
            }
            hzxxDto.setLrry(pt);//根据接口调用方获取录入人员
            hzxxDto.setSjdwmc(pt);//根据接口调用放获取送检单位名称
            hzxxDto.setLrsj(dateformat.format(date));//录入时间
            List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
            String cyd = "";
            for (JcsjDto cydJcsj : cydList) {
                if ("01".equals(cydJcsj.getCsdm())){
                    cyd = cydJcsj.getCsid();
                }
            }
            hzxxDto.setCyd(cyd);//设置采样点
            String zjlx = compareIDType(certificateType);
            hzxxDto.setZjlx(zjlx);//设置证件类型
            if (CheckIdCard.check(certificateNo)) {
                hzxxDto.setNl(Long.toString(CheckIdCard.getAge(certificateNo)));//调用方法 根据证件号获取年龄
            }
            hzxxDto.setSj(p.eCode(phoneNumber));//加密手机号 存入数据库
            hzxxDto.setZjh(p.eCode(certificateNo));//加密证件号 存入数据库
            hzxxDto.setYyjcrq(scheduledTime);//预约检测日期
            rabbitMap.put("yyjcrqfw",scheduledTime);
            //设置患者id
            String hzid = StringUtil.generateUUID();
            boolean isSuccess;
            //查询证件号码、证件类型是否存在
            HzxxDto hzxx = hzxxDao.getHzxxByZjh(hzxxDto);
            /*
            存在：下一步操作；
            不存在：新增 患者信息、分子检测信息、分子检测项目；
		    */
            if (hzxx != null) {
                hzxxDto.setHzid(hzxx.getHzid());
                //若 证件类型、证件号存在：下一步操作
                //查询证件号码是否有未检测的同一天的预约
                List<HzxxDto> lshzxxList = hzxxService.getHzxxDtoByZjhAndDate(hzxxDto);
                //当前新增预约项目与未检测的同一天的预约项目有冲突：不新增，无冲突新增
                /*
                当前新增预约项目与未检测的同一天的预约项目有冲突：不新增，提示消息
                当前新增预约项目与未检测的同一天的预约项目无冲突：下一步操作；
                 */
                if (lshzxxList != null && lshzxxList.size() > 0) {
                    //当前新增预约项目与未检测的同一天的预约项目有冲突：不执行操作；提示消息
                    map.put("message", "您当前已有未过期的预约，请勿重复预约！");
                    map.put("status", "fail");
                    return map;
                }
                //当前新增预约项目与未检测的预约项目无冲突：下一步操作：
				/*
				姓名不一致:不执行操作；提示消息
				姓名一致:修改患者信息表（手机、地址），新增分子检测信息，新增分子检测项目
				 */
                if (!hzxx.getXm().equals(hzxxDto.getXm())) {
                    //姓名不一致:下一步操作
					/*
					是否确认为1：不执行操作；提示信息
					是否确认为0：修改患者信息表，新增分子检测信息，新增分子检测项目；
					 */
                    if ("1".equals(hzxx.getSfqr())) {
                        //若 是否确认为1：不执行操作；提示信息
                        map.put("message", "姓名与证件号码不匹配，请确认后提交！");
                        map.put("status", "fail");
                        return map;
                    } else {
                        //是否确认为0：修改患者信息表，新增分子检测信息，新增分子检测项目；
                        hzxxDto.setXgsj(hzxxDto.getLrsj());//设置修改时间
                        hzxxDto.setXgry(hzxxDto.getLrry());//设置修改人员
                        isSuccess = hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
                        rabbitMap.put("hzxxDto", hzxxDto);
                        if (isSuccess) {
                            FzjcxxDto fzjcxxDto = new FzjcxxDto();
                            fzjcxxDto.setHzid(hzxx.getHzid());//设置患者id
                            fzjcxxDto.setFzjcid(StringUtil.generateUUID());//设置分子检测id
                            fzjcxxDto.setPt(pt);//设置平台
                            fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());//设置预约检测日期
                            fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());//设置检测项目名称
                            fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());//设置检测项目id
                            fzjcxxDto.setCyd(hzxxDto.getCyd());//设置采样点
                            fzjcxxDto.setLrsj(hzxx.getLrsj());//设置录入时间
                            fzjcxxDto.setLrry(hzxxDto.getLrry());//设置录入人员
                            fzjcxxDto.setSjdwmc(hzxxDto.getSjdwmc());//设置送检单位
                            fzjcxxDto.setSjdw(hzxxDto.getSjdw());//设置送检单位
                            fzjcxxDto.setYyrq(dateformat.format(date));//设置预约日期
                            fzjcxxDto.setJcdxlx(hzxxDto.getJcdxlx());//设置检测对象类型
                            XtszDto xtszDto = xtszService.selectById("xg_zfje");
                            if (null!=xtszDto){
                                fzjcxxDto.setFkje(xtszDto.getSzz());
                            }
                            isSuccess = insertDetectionAppointmentFzjcxx(fzjcxxDto);
                            rabbitMap.put("fzjcxxDto", fzjcxxDto);
                            if (isSuccess) {
                                String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
                                for (String jcxmid : jcxmidList) {
                                    FzjcxmDto fzjcxmDto = new FzjcxmDto();
                                    fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                                    fzjcxmDto.setFzjcxmid(jcxmid);
                                    fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                                    isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
                                    rabbitMap.put("fzjcxmDto", fzjcxmDto);
                                    amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_MOD.getCode() + JSONObject.toJSONString(rabbitMap));
                                    if (!isSuccess) {
                                        map.put("message", "分子检测项目表新增失败！");
                                        map.put("status", "fail");
                                        return map;
                                    } else {
                                        map.put("message", "提交成功！");
                                        map.put("status", "success");
                                        //设置采样点名称（若没有匹配到，则设置为默认采样点名称）
                                        String cydmc = "";
                                        String mrcydmc = "";
                                        List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
                                        for (JcsjDto cydDto : cyds) {
                                            if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
                                                cydmc = cydDto.getCsmc();
                                            }
                                            if ("1".equals(cydDto.getSfmr())){
                                                mrcydmc = cydDto.getCsmc();
                                            }
                                        }
                                        if (StringUtil.isBlank(cydmc)){
                                            cydmc = mrcydmc;
                                        }
                                        rabbitMap.put("cydmc",cydmc);
                                        map.put("fzjcid",fzjcxxDto.getFzjcid());
                                        //阿里健康会发送一条短信，避免重复发送，取消我们的发送短信
//                                        dxglService.sendReplaceMsg(hzxxDto.getSj(), "ICOMM_XG00001", rabbitMap, signNameMatridx, templateCodeXgyy);
                                        //发送预约成功短信
                                        dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
                                        return map;
                                    }
                                }
                            } else {
                                map.put("message", "分子检测信息表新增失败！");
                                map.put("status", "fail");
                                return map;
                            }
                        } else {
                            map.put("message", "患者信息表修改失败！");
                            map.put("status", "fail");
                            return map;
                        }
                    }
                } else {
                    //若 姓名一致 修改患者信息表（手机、地址），新增分子检测信息，新增分子检测项目
                    hzxxDto.setXgsj(hzxxDto.getLrsj());//设置修改时间
                    hzxxDto.setXgry(hzxxDto.getLrry());//设置修改人员
                    isSuccess = hzxxService.updateDetectionAppointmentHzxx(hzxxDto);
                    rabbitMap.put("hzxxDto", hzxxDto);
                    if (isSuccess) {
                        FzjcxxDto fzjcxxDto = new FzjcxxDto();
                        fzjcxxDto.setHzid(hzxx.getHzid());//设置患者id
                        fzjcxxDto.setFzjcid(StringUtil.generateUUID());//设置分子检测id
                        fzjcxxDto.setPt(pt);//设置平台
                        fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());//设置预约检测日期
                        fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());//设置检测项目名称
                        fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());//设置检测项目id
                        fzjcxxDto.setCyd(hzxxDto.getCyd());//设置采样点
                        fzjcxxDto.setLrsj(hzxxDto.getLrsj());//设置录入时间
                        fzjcxxDto.setLrry(hzxxDto.getLrry());//设置录入人员
                        fzjcxxDto.setSjdwmc(hzxxDto.getSjdwmc());//设置送检单位
                        fzjcxxDto.setYyrq(dateformat.format(date));//设置预约日期
                        fzjcxxDto.setSjdw(hzxxDto.getSjdw());//设置送检单位
                        fzjcxxDto.setJcdxlx(hzxxDto.getJcdxlx());//设置检测对象类型
                        XtszDto xtszDto = xtszService.selectById("xg_zfje");
                        if (null!=xtszDto){
                            fzjcxxDto.setFkje(xtszDto.getSzz());
                        }
                        isSuccess = insertDetectionAppointmentFzjcxx(fzjcxxDto);
                        rabbitMap.put("fzjcxxDto", fzjcxxDto);
                        if (isSuccess) {
                            String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
                            for (String jcxmid : jcxmidList) {
                                FzjcxmDto fzjcxmDto = new FzjcxmDto();
                                fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                                fzjcxmDto.setFzjcxmid(jcxmid);
                                fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                                isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
                                rabbitMap.put("fzjcxmDto", fzjcxmDto);
                                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_MOD.getCode() + JSONObject.toJSONString(rabbitMap));
                                if (!isSuccess) {
                                    map.put("message", "分子检测项目表新增失败！");
                                    map.put("status", "fail");
                                    return map;
                                } else {
                                    map.put("message", "提交成功！");
                                    map.put("status", "success");
                                    //设置采样点名称（若没有匹配到，则设置为默认采样点名称）
                                    String cydmc = "";
                                    String mrcydmc = "";
                                    List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
                                    for (JcsjDto cydDto : cyds) {
                                        if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
                                            cydmc = cydDto.getCsmc();
                                        }
                                        if ("1".equals(cydDto.getSfmr())){
                                            mrcydmc = cydDto.getCsmc();
                                        }
                                    }
                                    if (StringUtil.isBlank(cydmc)){
                                        cydmc = mrcydmc;
                                    }
                                    rabbitMap.put("cydmc",cydmc);
                                    map.put("fzjcid",fzjcxxDto.getFzjcid());
                                    //阿里健康会发送一条短信，避免重复发送，取消我们的发送短信
//                                    dxglService.sendReplaceMsg(hzxxDto.getSj(), "ICOMM_XG00001", rabbitMap, signNameMatridx, templateCodeXgyy);
                                    //发送预约成功短信
                                    dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
                                    return map;
                                }
                            }
                        } else {
                            map.put("message", "分子检测信息表新增失败！");
                            map.put("status", "fail");
                            return map;
                        }
                    } else {
                        map.put("message", "患者信息表修改失败！");
                        map.put("status", "fail");
                        return map;
                    }
                }
            }else {
                //若 证件类型、证件号 不存在，新增患者信息表，新增分子检测信息表、新增分子检测项目表
                hzxxDto.setHzid(hzid);//设置患者id
                isSuccess = hzxxService.insertDetectionAppointmentHzxx(hzxxDto);
                rabbitMap.put("hzxxDto",hzxxDto);
                //如果新增患者信息成功，则往分子检测信息表中增加数据
                if (isSuccess){
                    FzjcxxDto fzjcxxDto = new FzjcxxDto();
                    fzjcxxDto.setHzid(hzxxDto.getHzid());//设置患者id
                    fzjcxxDto.setFzjcid(StringUtil.generateUUID());//设置分子检测id
                    fzjcxxDto.setPt(pt);//设置平台
                    fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());//设置预约检测日期
                    fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());//设置检测项目名称
                    fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());//设置检测项目id
                    fzjcxxDto.setCyd(hzxxDto.getCyd());//设置采样点
                    fzjcxxDto.setLrsj(hzxxDto.getLrsj());//设置录入时间
                    fzjcxxDto.setLrry(hzxxDto.getLrry());//设置录入人员
                    fzjcxxDto.setSjdwmc(hzxxDto.getSjdwmc());//设置送检单位
                    fzjcxxDto.setYyrq(dateformat.format(date));//设置预约日期
                    fzjcxxDto.setSjdw(hzxxDto.getSjdw());//设置送检单位
                    fzjcxxDto.setJcdxlx(hzxxDto.getJcdxlx());//设置检测对象类型
                    XtszDto xtszDto = xtszService.selectById("xg_zfje");
                    if (null!=xtszDto){
                        fzjcxxDto.setFkje(xtszDto.getSzz());
                    }
                    isSuccess = insertDetectionAppointmentFzjcxx(fzjcxxDto);
                    rabbitMap.put("fzjcxxDto",fzjcxxDto);
                    if (isSuccess){
                        String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
                        for (String jcxmid : jcxmidList) {
                            FzjcxmDto fzjcxmDto = new FzjcxmDto();
                            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                            fzjcxmDto.setFzjcxmid(jcxmid);
                            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                            isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
                            rabbitMap.put("fzjcxmDto",fzjcxmDto);
                            rabbitMap.put("isHzxxAdd","yes");
                            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_ADD.getCode()+ JSONObject.toJSONString(rabbitMap));
                            if (!isSuccess){
                                map.put("message","分子检测项目表新增失败！");
                                map.put("status", "fail");
                                return map;
                            }else {
                                map.put("message","提交成功！");
                                map.put("status", "success");
                                //设置采样点名称（若没有匹配到，则设置为默认采样点名称）
                                String cydmc = "";
                                String mrcydmc = "";
                                List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
                                for (JcsjDto cydDto : cyds) {
                                    if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
                                        cydmc = cydDto.getCsmc();
                                    }
                                    if ("1".equals(cydDto.getSfmr())){
                                        mrcydmc = cydDto.getCsmc();
                                    }
                                }
                                if (StringUtil.isBlank(cydmc)){
                                    cydmc = mrcydmc;
                                }
                                rabbitMap.put("cydmc",cydmc);
								map.put("fzjcid",fzjcxxDto.getFzjcid());
                                //阿里健康会发送一条短信，避免重复发送，取消我们的发送短信
//                                dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00001", rabbitMap, signNameMatridx, templateCodeXgyy);
                                //发送预约成功短信
                                dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
                                return map;
                            }
                        }
                    }else {
                        map.put("message","分子检测信息表新增失败！");
                        map.put("status", "fail");
                        return map;
                    }
                }else {
                    map.put("message","患者信息表新增失败！");
                    map.put("status", "fail");
                    return map;
                }
            }
        }else {
            map.put("status","fail");
            map.put("message","信息不完整，预约失败");
            return map;
        }
        return map;
    }

    /**
     * 比对证件类型 方法
     */
    public String compareIDType(String ID_TYPE){
        List<JcsjDto> zjlxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
        String otherZjlx = "";
        for (JcsjDto zjlx : zjlxs) {
            if (zjlx.getCsmc().contains(ID_TYPE) || zjlx.getCskz1().contains(ID_TYPE)){
                return zjlx.getCsid();
            }
            if (zjlx.getCsdm().equals("99")){
                otherZjlx = zjlx.getCsid();
            }
        }
        //若未匹配到证件类型，则返回“其它”证件类型的参数id
        return otherZjlx;
    }

    /**
     * 阿里健康 服务商确认履约完成
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean confirmPerformance(FzjcxxDto fzjcxxDto) throws Exception {
        log.error("==============================阿里健康--确认履约==============================");
        TaobaoClient client = new DefaultTaobaoClient(alihealthServerUrl, alihealthAppKey, alihealthAppSecret);
        AlibabaAlihealthMedicalReservationKeepAppointmentFinishRequest req =new AlibabaAlihealthMedicalReservationKeepAppointmentFinishRequest() ;
        AlibabaAlihealthMedicalReservationKeepAppointmentFinishRequest.FinishKeepAppointmentTopRequest obj1 = new AlibabaAlihealthMedicalReservationKeepAppointmentFinishRequest.FinishKeepAppointmentTopRequest();
        obj1.setIsvReservationRecordId(fzjcxxDto.getFzjcid());
        req.setFinishKeepAppointmentTopRequest(obj1);
        AlibabaAlihealthMedicalReservationKeepAppointmentFinishResponse rsp;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            throw new Exception("阿里健康履约出错！");
        }
        log.error(rsp.getBody());
        String resultStatus = rsp.getResultStatus();
        if("SUCCESS".equals(resultStatus)){
            log.error(JSONObject.toJSONString(rsp));
            log.error("=============================阿里健康--确认履约成功=============================");
            return true;
        }else{
            throw new Exception("阿里健康--确认履约失败");
        }
    }

    /**
     * 橄榄枝健康 服务商确认履约完成
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean confirmPerformanceToGanlanzhi(FzjcxxDto fzjcxxDto) throws Exception {
        log.error("==============================橄榄枝健康--确认履约==============================");
        Map<String, Object> rabbitMap = new HashMap<>();
        rabbitMap.put("isvReservationRecordId",fzjcxxDto.getFzjcid());
        String results = sendPost(glzhealthAppointmentfinishurl, "finish_keep_appointment_top_request="+JSONObject.toJSONString(rabbitMap));
        JSONObject rsp = JSON.parseObject(results);
        if("SUCCESS".equals(rsp.get("result_status"))){
            log.error(results);
            log.error("=============================橄榄枝健康--确认履约成功=============================");
            return true;
        }else{
            log.error(results);
            log.error("=============================橄榄枝健康--确认履约失败=============================");
            throw new Exception("橄榄枝健康--确认履约失败");
        }
    }

    /*通过审核过程中的ywids查找出分子检测信息数据list*/
    public List<FzjcxxDto> getListByIds(FzjcxxDto fzjcxxDto){
        return dao.getListByIds(fzjcxxDto);
    }

    /**
     * 获取平台未付款记录
     */
    public List<FzjcxxDto> getUnpaidByPt(FzjcxxDto fzjcxxDto){
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getUnpaidByPt(fzjcxxDto);
    }

    /**
     * 批量更新
     */
    public int updateList(List<FzjcxxDto> list){
        return dao.updateList(list);
    }

    /**
     * 阿里健康、橄榄枝 对外取消预约
     * @param ipAddress IP地址
     * @param platform 平台
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public String cancelDetAppointmentInfo(String ipAddress,String platform,String appointmentInfo){
        //打印传过来的json
        log.error("=====访问IP地址："+ipAddress+"=====");
        log.error("appointmentInfo:"+appointmentInfo);
        insertJkdymx(InvokingChildTypeEnum.INVOKING_CHILD_XGQX.getCode(),appointmentInfo);//接口调用明细 新增
        //将json信息转化成map
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> appointmentInfoMap = JSON.parseObject(appointmentInfo,new TypeReference<Map<String, Object>>(){});
        appointmentInfoMap.put("platform",platform);
        Map<String, Object> resultMap = cancelSaveDetAppointmentInfo(appointmentInfoMap,appointmentInfo);
        if ("success".equals(resultMap.get("status"))){
            result.put("resultStatus","SUCCESS");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }else if ("fail".equals(resultMap.get("status"))){
            result.put("resultStatus","FAIL");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }else if ("accept".equals(resultMap.get("status"))){
            result.put("resultStatus","ACCEPT");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }else {
            result.put("resultStatus","UNKNOW");
            result.put("resultCode",resultMap.get("message"));
            result.put("resultMsg",resultMap.get("message"));
        }
        log.error("返回"+platform+"--取消预约的信息："+JSON.toJSONString(result));
        return JSON.toJSONString(result);
    }

    /**
     * 阿里健康、橄榄枝 预约取消保存 方法
     */
    public Map<String,Object> cancelSaveDetAppointmentInfo(Map<String, Object> appointmentInfoMap,String appointmentInfo){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> rabbitMap= new HashMap<>();//rabbit同步map
        //判断是否为橄榄枝调用，设置调用平台
        String pt;
        if ("glzhealth".equals(appointmentInfoMap.get("platform"))){
            pt = "橄榄枝健康";
            log.error("=====调用平台：橄榄枝健康=====");
        }else {
            pt = "阿里健康";
            log.error("=====调用平台：阿里健康=====");
        }
        List<String> ids=new ArrayList<>();
        String isvReservationRecordId = (String) appointmentInfoMap.get("isvReservationRecordId");//分子检测id

        FzjcxxDto fzjcxxDto=new FzjcxxDto();
        ids.add(isvReservationRecordId);
        fzjcxxDto.setIds(ids);
        fzjcxxDto.setFzjcid(isvReservationRecordId);
        FzjcxxDto dto = getDto(fzjcxxDto);
        boolean isSuccess =false;
        if(dto!=null){
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            if (StringUtil.isNotBlank(dto.getCjsj())){
                //若采集时间不为空，返回修改预约失败
                map.put("message","您的检测已接收，取消预约失败！");
                map.put("status", "fail");
                return map;
            }
            boolean causedByModification = false;
            if(appointmentInfo.contains("causedByModification")){
                causedByModification = (Boolean) appointmentInfoMap.get("causedByModification");//是否是修改预约触发的取消
            }
            if(causedByModification){
                //是修改预约触发的取消
                if("阿里健康".equals(pt)){
                    //阿里健康的修改（先删除、后发起新增调用）
                    fzjcxxDto.setScry(pt);
                    fzjcxxDto.setScry(dateformat.format(date));
                    isSuccess=delCovidDetails(fzjcxxDto);
                    rabbitMap.put("fzjcxxDto",fzjcxxDto);
                    if (isSuccess){
                        /*去掉 阿里健康 修改预约 删除分子检测项目表的代码 2022-02-08*/
                        /*FzjcxmDto fzjcxmDto = new FzjcxmDto();
                        fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                        isSuccess = fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);
                        rabbitMap.put("fzjcxmDto",fzjcxmDto);
                        if (isSuccess){
                            //发送修改取消预约rabbit
                            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.AHYY_UPD.getCode()+ JSONObject.toJSONString(rabbitMap));
                        }*/
                        //发送修改取消预约rabbit
                        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.AHYY_UPD.getCode()+ JSONObject.toJSONString(rabbitMap));
                    }
                }else {
                    //橄榄枝健康的修改
                    fzjcxxDto.setXgry(pt);
                    fzjcxxDto.setXgsj(dateformat.format(date));
                    String scheduledTime = (String) appointmentInfoMap.get("scheduledTime");
                    fzjcxxDto.setYyjcrq(scheduledTime);
                    isSuccess=updateAppDate(fzjcxxDto);
                    if (isSuccess){
                        //发送修改预约rabbit
                        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.GHYY_UPD.getCode()+ JSONObject.toJSONString(rabbitMap));
                    }
                }
            }else{
                //不是修改预约出发的取消
                fzjcxxDto.setScry(pt);
                fzjcxxDto.setScsj(dateformat.format(date));
                fzjcxxDto.setFkbj("2");
                isSuccess=cancelDetectionAppointment(fzjcxxDto);
                rabbitMap.put("fzjcxxDto",fzjcxxDto);
                if (isSuccess){
                    /*去掉 阿里、橄榄枝平台 取消预约 删除分子检测项目表的代码 2022-02-08*/
                    /*FzjcxmDto fzjcxmDto = new FzjcxmDto();
                    fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                    isSuccess = fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);
                    rabbitMap.put("fzjcxmDto",fzjcxmDto);
                    if (isSuccess){
                        //发送取消预约rabbit
                        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_DEL.getCode()+ JSONObject.toJSONString(rabbitMap));
                    }*/
                    amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_DEL.getCode()+ JSONObject.toJSONString(rabbitMap));
                }
            }

        }
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? "取消预约成功" : "取消预约失败");
        return map;
    }

    /**
     * 获取ip地址
     */
    public String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 新增接口调用明细信息
     */
    public void insertJkdymx(String dyzfl,String appointmentInfo){
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JkdymxDto jkdymxDto = new JkdymxDto();
        jkdymxDto.setLxqf("recv");
        jkdymxDto.setDysj(dateformat.format(date));
        jkdymxDto.setNr(appointmentInfo);
//        jkdymxDto.setDydz(request.getRequestURI());
        jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_XGLR.getCode());
        jkdymxDto.setDyzfl(dyzfl);
        jkdymxDto.setSfcg("2");
        jkdymxService.insertJkdymxDto(jkdymxDto);
    }
    /**
     * 获取阿里订单情况
     */
    public void getOrderDetails(){
        FzjcxxDto fzjcxxDto=new FzjcxxDto();
        fzjcxxDto.setPt("阿里健康");
        List<FzjcxxDto> list = getUnpaidByPt(fzjcxxDto);
        List<FzjcxxDto> xglist = new ArrayList<>();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list!=null&&list.size()>0){
            for(FzjcxxDto dto:list){
                TaobaoClient client = new DefaultTaobaoClient(alihealthServerUrl, alihealthAppKey, alihealthAppSecret);
                AlibabaAlihealthMedicalOrderRealtimeGetRequest req = new AlibabaAlihealthMedicalOrderRealtimeGetRequest();
                AlibabaAlihealthMedicalOrderRealtimeGetRequest.GetOrderRequest obj1 = new AlibabaAlihealthMedicalOrderRealtimeGetRequest.GetOrderRequest();
                obj1.setIsvReservationRecordId(dto.getFzjcid());
                req.setParam0(obj1);
                AlibabaAlihealthMedicalOrderRealtimeGetResponse rsp = null;
                try {
                    rsp = client.execute(req);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                if (rsp != null) {
                    log.error("--阿里健康--获取支付详情返回数据："+rsp.getBody());
                    if(!"FAIL".equals(rsp.getResultStatus())){
                        AlibabaAlihealthMedicalOrderRealtimeGetResponse.OrderInfo data = rsp.getData();
                        dto.setFkje(data.getTotalSalesPrice());
                        dto.setSfje(data.getActualPaymentAmount());
                        dto.setFkbj("1");
                        dto.setFkrq(data.getPaymentSuccessTime());
                        dto.setPtorderno(data.getOrderId());
                        Date date = new Date();
                        dto.setXgsj(dateformat.format(date));
                        xglist.add(dto);
                    }
                }
            }
            if(xglist.size()>0){
                updateList(xglist);
                Map<String, Object> rabbitMap= new HashMap<>();//rabbit同步map
                rabbitMap.put("fzjcxxList",xglist);
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.XGFK_MOD.getCode()+ JSONObject.toJSONString(rabbitMap));
            }
        }
    }

    /**
     * 根据条件下载报告压缩包
     */
    public Map<String,Object> reportZipDownload(FzjcxxDto fzjcxxDto, HttpServletRequest request){
        // 查询下载报告
        List<String> ids = fzjcxxDto.getIds();
        List<FzjcxxDto> fzjcxxDtos;
        //判断是否选中
        if(ids != null && ids.size() > 0){
            // 根据选择查询报告信息(pdf结果)
            fzjcxxDtos = dao.selectDownloadReportByIds(fzjcxxDto);
        }else{
            // 根据条件查询报告信息(pdf结果)
            fzjcxxDtos = dao.selectDownloadReport(fzjcxxDto);
        }
        return zipDownload(fzjcxxDtos);
    }

    @Override
    public List<FzjcxxDto> getExistBbzbh(FzjcxxDto fzjcxxDto) {
        return dao.getExistBbzbh(fzjcxxDto);
    }

    /**
     * 下载压缩报告
     */
    private Map<String, Object> zipDownload(List<FzjcxxDto> fzjcxxDtos){
        Map<String, Object> map = new HashMap<>();
        try {
            // TODO Auto-generated method stub
            // 判断是否大于1000条
            if (fzjcxxDtos != null && fzjcxxDtos.size() > 1000) {
                map.put("error", "超过1000个报告！");
                map.put("status", "fail");
                return map;
            } else if (fzjcxxDtos == null || fzjcxxDtos.size() == 0) {
                map.put("error", "未找到报告信息！");
                map.put("status", "fail");
                return map;
            }
            String key = String.valueOf(System.currentTimeMillis());
            redisUtil.hset("EXP_:_" + key, "Cnt", "0");
            map.put("count", fzjcxxDtos.size());
            map.put("redisKey", key);
            String folderName = "UP" + System.currentTimeMillis();
            String storePath = prefix + tempFilePath + BusTypeEnum.IMP_REPORTZIP.getCode() + "/" + folderName;
            mkDirs(storePath);
            map.put("srcDir", storePath);
            // 开启线程拷贝临时文件
            CovidReportZipExport covidReportZipExport = new CovidReportZipExport();
            covidReportZipExport.init(key, storePath, folderName, fzjcxxDtos, redisUtil);
            CovidReportZipThread covidReportZipThread = new CovidReportZipThread(covidReportZipExport);
            covidReportZipThread.start();
            map.put("status", "success");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据路径创建文件
     */
    private boolean mkDirs(String storePath)
    {
        File file = new File(storePath);
        if (file.isDirectory())
        {
            return true;
        }
        return file.mkdirs();
    }

    /**
     * 退款订单定时查询
     */
    public void refundInquiryQuery(){
        //获取新冠检测类型
        FzjcxxDto t_fzjcxxDto=new FzjcxxDto();
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    t_fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        List<FzjcxxDto> fzjcxxDtos = dao.getRefundOrder(t_fzjcxxDto);
        if (null != fzjcxxDtos && fzjcxxDtos.size() > 0){
            NNOpenSDK sdk = NNOpenSDK.getIntance();
            String taxnum = NUONUOEnum.TAXNUM.getCode(); // 授权企业税号
            String appKey = NUONUOEnum.APPKEY.getCode();
            String appSecret = NUONUOEnum.APPSECRET.getCode();
            String method1 = NUONUOEnum.REFUND_INQUIRY_QUERY.getCode(); // API方法名
            String method;
            method = new String( method1.getBytes(StandardCharsets.UTF_8) , StandardCharsets.UTF_8);
            XtszDto xtszDto = xtszService.selectById("access_token");
            String accessToken ="";
            if (null!=xtszDto){
                accessToken  =  xtszDto.getSzz();//获取token 访问令牌
            }
            String url = NUONUOEnum.URL.getCode();  // SDK请求地址
            String senid = UUID.randomUUID().toString().replace("-", "");
            String content;
            for (FzjcxxDto fzjcxxDto:fzjcxxDtos) {
                 content = "{" +
                        "\"refundNo\": \""+fzjcxxDto.getRefundno()+"\","+
                        "\"taxNo\": \""+taxnum+"\"" +  //税号
                        "}";
                String refundInfo = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
                log.error("退款信息："+refundInfo);
                JSONObject jsonObject =  JSONObject.parseObject(refundInfo);
                if (StringUtil.isNotBlank(jsonObject.get("code").toString()) && "JH200".equals(jsonObject.get("code").toString())){
                    JSONObject object = JSONObject.parseObject(jsonObject.get("result").toString());
                    if (StringUtil.isNotBlank(object.get("refundStatus").toString()) && "2".equals(object.get("refundStatus").toString())){
                        FzjcxxModel fzjcxxModel = new FzjcxxModel();
                        fzjcxxModel.setFzjcid(fzjcxxDto.getFzjcid());
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date=new Date();
                        boolean last = true;
                        if (StringUtil.isNotBlank(fzjcxxDto.getCzbs())){
                            fzjcxxModel.setCzbs("0");
                            fzjcxxModel.setFkbj("0");
                            fzjcxxModel.setSfje("1");
                            fzjcxxModel.setPtorderno("1");
                            fzjcxxModel.setZffs("1");
                            fzjcxxModel.setOrderno("1");
                            last= false;
                        }else{
                            fzjcxxModel.setFkbj("2");
                        }
                        fzjcxxModel.setTkcgrq(dateformat.format(date));
                        boolean success = dao.update(fzjcxxModel)!=0;
                        if (success){
                            amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FKBJ_MOD.getCode() + JSONObject.toJSONString(fzjcxxModel));
                            if (last){
                                Map<String, Object> rabbitMap= new HashMap<>();
                                boolean isSuccess;
                                FzjcxxDto fzjcxxDto1 = new FzjcxxDto();
                                fzjcxxDto1.setWxid(fzjcxxDto.getWxid());
                                fzjcxxDto1.setHzid(fzjcxxDto.getHzid());
                                fzjcxxDto1.setFzjcid(fzjcxxDto.getFzjcid());
                                fzjcxxDto1.setScsj(dateformat.format(date));
                                isSuccess=cancelDetectionAppointment(fzjcxxDto1);
                                rabbitMap.put("fzjcxxDto",fzjcxxDto1);
                                if (isSuccess){
                                    FzjcxmDto fzjcxmDto = new FzjcxmDto();
                                    fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                                    isSuccess=fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);
                                    if (isSuccess){
                                        rabbitMap.put("fzjcxmDto",fzjcxmDto);
                                        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.YYXX_DEL.getCode()+ JSONObject.toJSONString(rabbitMap));
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * 阿里回传报告
     */
    public boolean dataReportToAli(FzjcxxDto fzjcxxDto)
    {
        boolean flag=false;
        fzjcxxDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
        List<FzjcxxDto> fzjcxxlist = getListByIds(fzjcxxDto);
        if (fzjcxxlist.size()>0){
            for(FzjcxxDto dto:fzjcxxlist){
                String zjh="";
                String sj="";
                if("1".equals(dto.getZjlxdm())){
                    DBEncrypt dbEncrypt=new DBEncrypt();
                    if(StringUtil.isNotBlank(dto.getZjh())){
                        zjh=dbEncrypt.dCode(dto.getZjh());
                    }
                    if(StringUtil.isNotBlank(dto.getSj())){
                        sj=dbEncrypt.dCode(dto.getSj());
                    }
                }

                TaobaoClient client = new DefaultTaobaoClient(alihealthServerUrl, alihealthAppKey, alihealthAppSecret);
                AlibabaAlihealthMedicalReportCustomerReportSyncRequest req = new AlibabaAlihealthMedicalReportCustomerReportSyncRequest();
                AlibabaAlihealthMedicalReportCustomerReportSyncRequest.ConsumerReportSyncRequest obj1 = new AlibabaAlihealthMedicalReportCustomerReportSyncRequest.ConsumerReportSyncRequest();
                obj1.setIsvReportId(dto.getFjid());
                obj1.setReportName(dto.getWjm());
                obj1.setIsvReportUrl(applicationurl+"/ws/file/downloadFile?fjid="+dto.getFjid());
                log.info(obj1.getIsvReportUrl());
                obj1.setSourceIsvReservationRecordId(dto.getFzjcid());
                obj1.setConsumerIdCardNo(zjh);
//		obj1.setConsumerPassportNo("E0450000");
                obj1.setConsumerPhoneNumber(sj);
                req.setParam0(obj1);
                AlibabaAlihealthMedicalReportCustomerReportSyncResponse rsp = null;
                try {
                    rsp = client.execute(req);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                if (rsp != null) {
                    log.info(rsp.getBody());
                    String resultStatus = rsp.getResultStatus();
                    if("SUCCESS".equals(resultStatus)){
                        log.info("-------阿里健康---回传报告成功------");
                        flag=true;
                    }else{
                        log.info("-------阿里健康---回传报告失败------");
                        flag=false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 修改预约日期
     */
    public boolean updateAppDate(FzjcxxDto fzjcxxDto){
        return dao.updateAppDate(fzjcxxDto);
    }

    /**
     * 更新阿里订单信息
     */
    public boolean updateAliOrder(FzjcxxDto fzjcxxDto){
        return dao.updateAliOrder(fzjcxxDto);
    }
	
    /**
     * 小程序扫预约码更新分子检测信息
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveCovid(FzjcxxDto fzjcxxDto) throws Exception {
        HzxxDto hzxxDto = new HzxxDto();
        hzxxDto.setHzid(fzjcxxDto.getHzid());
        hzxxDto.setXgry(fzjcxxDto.getCjry());
        Map<String,Object> rabbitmap = new HashMap<>();
        boolean isSuccess = hzxxService.updateSfqr(hzxxDto);
        rabbitmap.put("hzxxDto",hzxxDto);
        if (isSuccess){
            List<JcsjDto> yblxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
            if (yblxList.size()>0) {
                for (JcsjDto yblx : yblxList) {
                    if ("H".equals(fzjcxxDto.getFzjczxmcskz1())){
                        fzjcxxDto.setWybh(fzjcxxDto.getBbzbh());//用于单检和混检编号不重复
                        if ("SH".equals(yblx.getCsdm())){
                          fzjcxxDto.setYblx(yblx.getCsid());
                          break;
                        }
                    }else if ("D".equals(fzjcxxDto.getFzjczxmcskz1())){
                        fzjcxxDto.setWybh(fzjcxxDto.getYbbh()+"01");
                        if ("S".equals(yblx.getCsdm())){
                            fzjcxxDto.setYblx(yblx.getCsid());
                            break;
                        }
                    }
                }
            }
            //更新分子检测信息
            fzjcxxDto.setCjsj(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            String[] fzxmlist = new String[1];
            List<JcsjDto> fzjcxmJcsjDtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
            for (JcsjDto jcsjDto : fzjcxmJcsjDtoList) {
                if ("JX".equals(jcsjDto.getCsdm())){
                    fzxmlist = new String[]{jcsjDto.getCsid()};
                    break;
                }
            }
            rabbitmap.put("fzxmlist",fzxmlist);
            StringBuilder jcxmmc = new StringBuilder();//存储一个检测信息对应的多个检测项目名称，格式xx,xx,xx
            StringBuilder jcxmid = new StringBuilder();
            for (String s : fzxmlist) {//该循环处理分子检测项目的jcxmmc和jcxmid字段
                for (JcsjDto jcsjDto : fzjcxmJcsjDtoList) {
                    if (jcsjDto.getCsid().equals(s)) {
                        jcxmmc.append(",").append(jcsjDto.getCsmc());
                        jcxmid.append(",").append(jcsjDto.getCsid());
                    }
                }
            }
            if (StringUtil.isNotBlank(jcxmmc.toString())) {
                fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
            }
            if (StringUtil.isNotBlank(jcxmid.toString())) {
                fzjcxxDto.setJcxmid(jcxmid.substring(1));
            }
            if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
                fzjcxxDto.setXgry(fzjcxxDto.getCjry());
                isSuccess = dao.updateConfirm(fzjcxxDto);
                rabbitmap.put("fzjcxxDto_mod",fzjcxxDto);
            }else {
                List<JcsjDto> jcdxlxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
                for (JcsjDto jcsjDto : jcdxlxList) {
                    if ("R".equals(jcsjDto.getCsdm())){
                        fzjcxxDto.setJcdxlx(jcsjDto.getCsid());
                        break;
                    }
                }
                fzjcxxDto.setYyjcrq(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                fzjcxxDto.setYyrq(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                fzjcxxDto.setFzjcid(StringUtil.generateUUID());
                fzjcxxDto.setLrry(fzjcxxDto.getCjry());
                //区分新冠数据
                List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
                if(jclxList.size()>0){
                    for(JcsjDto jcsjDto : jclxList){
                        if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                            fzjcxxDto.setJclx(jcsjDto.getCsid());
                    }
                }
                int i = dao.insertDto(fzjcxxDto);
                if (i<1){
                    isSuccess = false;
                }
                rabbitmap.put("fzjcxxDto_add",fzjcxxDto);

            }
            fzjcxmService.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
            List<FzjcxmDto> fzjcxmDtoList = new ArrayList<>();
            for (String s : fzxmlist) {
                //遍历检测项目一个一个新增到数据库
                FzjcxmDto fzjcxmDto = new FzjcxmDto();
                fzjcxmDto.setFzxmid(StringUtil.generateUUID());
                fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
                fzjcxmDto.setFzjcxmid(s);
                fzjcxmDto.setLrry(fzjcxxDto.getXgry());
                fzjcxmDto.setFzjczxmid(fzjcxxDto.getFzjczxmid());//存分子检测子项目ID
                fzjcxmDao.insertDto(fzjcxmDto);
                fzjcxmDtoList.add(fzjcxmDto);
            }
            rabbitmap.put("fzjcxmDtoList",fzjcxmDtoList);
            if("阿里健康".equals(fzjcxxDto.getPt())){
                confirmPerformance(fzjcxxDto);
            }else if ("橄榄枝健康".equals(fzjcxxDto.getPt())){
                confirmPerformanceToGanlanzhi(fzjcxxDto);
            }
            if (isSuccess){
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.SFLR_SUB.getCode() + JSONObject.toJSONString(rabbitmap));
            }
        }
        return isSuccess;
    }

    /**
     * 小程序扫码更新物检分子检测信息
     */
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean materialDetConfirm(FzjcxxDto fzjcxxDto) {
        boolean isSuccess = true;
        List<JcsjDto> yblxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
        if (yblxList.size()>0) {
            for (JcsjDto yblx : yblxList) {
                if ("H".equals(fzjcxxDto.getFzjczxmcskz1())){
                    fzjcxxDto.setWybh(fzjcxxDto.getBbzbh());//用于单检和混检编号不重复
                    if ("SH".equals(yblx.getCsdm())){
                      fzjcxxDto.setYblx(yblx.getCsid());
                      break;
                    }
                }else if ("D".equals(fzjcxxDto.getFzjczxmcskz1())){
                    fzjcxxDto.setWybh(fzjcxxDto.getYbbh()+"01");
                    if ("S".equals(yblx.getCsdm())){
                        fzjcxxDto.setYblx(yblx.getCsid());
                        break;
                    }
                }
            }
        }
        //更新分子检测信息
        fzjcxxDto.setCjsj(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        String[] fzxmlist = new String[1];
        List<JcsjDto> fzjcxmJcsjDtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        for (JcsjDto jcsjDto : fzjcxmJcsjDtoList) {
            if ("JX".equals(jcsjDto.getCsdm())){
                fzxmlist = new String[]{jcsjDto.getCsid()};
                break;
            }
        }
        StringBuilder jcxmmc = new StringBuilder();//存储一个检测信息对应的多个检测项目名称，格式xx,xx,xx
        StringBuilder jcxmid = new StringBuilder();
        for (String s : fzxmlist) {//该循环处理分子检测项目的jcxmmc和jcxmid字段
            for (JcsjDto jcsjDto : fzjcxmJcsjDtoList) {
                if (jcsjDto.getCsid().equals(s)) {
                    jcxmmc.append(",").append(jcsjDto.getCsmc());
                    jcxmid.append(",").append(jcsjDto.getCsid());
                }
            }
        }
        if (StringUtil.isNotBlank(jcxmmc.toString())) {
            fzjcxxDto.setJcxmmc(jcxmmc.substring(1));
        }
        if (StringUtil.isNotBlank(jcxmid.toString())) {
            fzjcxxDto.setJcxmid(jcxmid.substring(1));
        }
        //有分子检测id,非新增
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
            fzjcxxDto.setXgry(fzjcxxDto.getCjry());
            isSuccess = dao.updateConfirm(fzjcxxDto);
        }else {
            //无分子检测id，新增分子检测信息
            List<JcsjDto> jcdxlxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
            for (JcsjDto jcsjDto : jcdxlxList) {
                if ("W".equals(jcsjDto.getCsdm())){
                    fzjcxxDto.setJcdxlx(jcsjDto.getCsid());
                    break;
                }
            }
            fzjcxxDto.setYyjcrq(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            fzjcxxDto.setFzjcid(StringUtil.generateUUID());
            fzjcxxDto.setLrry(fzjcxxDto.getCjry());
            //区分新冠数据
            List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if(jclxList.size()>0){
                for(JcsjDto jcsjDto : jclxList){
                    if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                        fzjcxxDto.setJclx(jcsjDto.getCsid());
                }
            }
            int i = dao.insertDto(fzjcxxDto);
            if (i<1){
                isSuccess = false;
            }
        }
        fzjcxmService.deleteFzjcxmByFzjcid(fzjcxxDto.getFzjcid());
        List<FzjcxmDto> fzjcxmDtoList = new ArrayList<>();
        for (String s : fzxmlist) {
            //遍历检测项目一个一个新增到数据库
            FzjcxmDto fzjcxmDto = new FzjcxmDto();
            fzjcxmDto.setFzxmid(StringUtil.generateUUID());
            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
            fzjcxmDto.setFzjcxmid(s);
            fzjcxmDto.setLrry(fzjcxxDto.getXgry());
            fzjcxmDto.setFzjczxmid(fzjcxxDto.getFzjczxmid());//存分子检测子项目ID
            fzjcxmDao.insertDto(fzjcxmDto);
            fzjcxmDtoList.add(fzjcxmDto);
        }
        return isSuccess;
    }
    //通过分子检测id获取患者ID
    @Override
    public String getHzidByFzjcid(String fzjcid) {
        return dao.getHzidByFzjcid(fzjcid);
    }

    /**
     * 通过分子检测ID查找某条预约数据
     */
    @Override
    public FzjcxxDto getOderDtoByFzjcid(FzjcxxDto fzjcxxDto) {
        return dao.getOderDtoByFzjcid(fzjcxxDto);
    }

    /**
     * 获取当前用户当前采样点的统计数据（管数、总数）
     */
    public Map<String,Object> getCountData(FzjcxxDto fzjcxxDto){
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getCountData(fzjcxxDto);
    }
    /**
     * 获取当前用户当前采样点的统计数据(人数)
     */
    public List<FzjcxxDto> getPeopleCountData(FzjcxxDto fzjcxxDto){
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getPeopleCountData(fzjcxxDto);
    }
    @Override
    public List<FzjcxxDto> getExistBySyhYbbh(FzjcxxDto fzjcxxDto) {
        return dao.getExistBySyhYbbh(fzjcxxDto);
    }

   /**
     * 钉钉小程序混检采集页面删除操作
     */
   @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackCovid(FzjcxxDto fzjcxxDto){
        //回滚分子检测信息
        boolean callbackFzjcxx=dao.callbackFzjcxx(fzjcxxDto);
        if(!callbackFzjcxx)
            return false;
        //回滚客户是否确认信息
        HzxxDto hzxxDto=new HzxxDto();
        hzxxDto.setHzid(fzjcxxDto.getHzid());
        hzxxDto.setSfqr("0");
        hzxxService.updateDto(hzxxDto);
        //发送rabbit
        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_BAC.getCode()+ JSONObject.toJSONString(fzjcxxDto));
        return true;
    }
	
	@Override
    public FzjcxxDto getFzjcxxDto(FzjcxxDto fzjcxxDto) {
        return dao.getFzjcxxDto(fzjcxxDto);
    }
	
     /**
     * 更新报告完成时间
     */
    public boolean updateBgwcsj(FzjcxxDto fzjcxxDto){
        return dao.updateBgwcsj(fzjcxxDto);
    }
    /**
     * 锁定新冠标本
     */
	 
    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean positiveLock(FzjcxxDto fzjcxxDto){
        //修改删除标记为3
        boolean result=dao.lockSample(fzjcxxDto);
        if(!result)
            return false;
        //添加一条审核过程信息，防止提交审核
        if(fzjcxxDto.getIds()!=null && fzjcxxDto.getIds().size()>0){
            List<ShgcDto> shgcDtos=new ArrayList<>();
            for(int i=0;i<fzjcxxDto.getIds().size();i++){
                ShgcDto shgcDto=new ShgcDto();
                shgcDto.setYwid(fzjcxxDto.getIds().get(i));
                shgcDto.setGcid(StringUtil.generateUUID());
                shgcDto.setShid("0001");//随便给一个值
                shgcDto.setSqr("admin");//随便给一个值
                shgcDto.setXlcxh("1");
                shgcDtos.add(shgcDto);
            }
            if(shgcDtos.size()>0){
                shgcService.insertDtoList(shgcDtos);
            }
        }
        return true;
    }

    //修改分子检测信息是否交付状态
    @Override
    public boolean updateSfjfFzjcxx(FzjcxxDto fzjcxxDto) {
        return dao.updateSfjfFzjcxx(fzjcxxDto);
    }

    /**
     * 钉钉获取数据
     */
    public List<FzjcxxDto> getFzjcxxList(FzjcxxDto fzjcxxDto){
        //区分新冠数据
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        if(jclxList.size()>0){
            for(JcsjDto jcsjDto : jclxList){
                if("TYPE_COVID".equals(jcsjDto.getCsdm()))
                    fzjcxxDto.setJclx(jcsjDto.getCsid());
            }
        }
        return dao.getFzjcxxList(fzjcxxDto);
    }

    /**
     * 保存履历信息
     */
    @Override
    public Boolean insertInfoResume(FzjcxxDto fzjcxxDto) {
        return dao.insertInfoResume(fzjcxxDto)!=0;
    }

    /**
     * 上传采集时间
     */
    public boolean cjsjReport(FzjcxxDto fzjcxxDto) {
        try {
            List<FzjcxxDto> list=getFzjcxxByIds(fzjcxxDto);
            if (list!=null && list.size()>0) {
                StringBuilder fzjcids = new StringBuilder();
                for (FzjcxxDto dto : list) {
                    fzjcids.append(",").append(dto.getFzjcid());
                }
                fzjcids = new StringBuilder(fzjcids.substring(1));
                fzjcxxDto.setIds(fzjcids.toString());
                String requestXml = generateCjsjXml(list,text);//生产上传采集信息的xml
                log.info("入参===========================\r\n\t"+requestXml);
                String responseXml = cjsjDeclare(requestXml);//上传信息到卫健，并得到卫健返回的xml
                log.info("出参===========================\r\n\t"+responseXml);
                cjsjResponseDeal(responseXml);//处理卫健返回的xml，更新fzjcxx表的采集信息上传状态和采集信息上传时间字段
            }
            return true;
        }catch (Exception e){
            log.error("cjsjReport报错"+e.getMessage());
            return false;
        }
    }

    private boolean cjsjResponseDeal(String responseXml) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            List<FzjcxxDto> list = new ArrayList<>();
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document documentContent = xmlUtil.parse(responseXml);
            Element retcode = documentContent.getRootElement().element("business").element("returnmessage").element("retcode");
            if ("1".equals(retcode.getText())){
                List<Element> elements = documentContent.getRootElement().element("business").element("businessdata").element("datasets").elements();
                for (Element element:elements){
                    if ("setdetails".equals(element.getName())){
                        Element cljgdm = element.element("CLJGDM");
                        String bbzbh = element.element("JYBGDH").getText().substring(2);//返回的检验报告单号为：BG+bbzbh
                        FzjcxxDto fzjcxxDto = new FzjcxxDto();
                        if ("1".equals(cljgdm.getText())){
                            fzjcxxDto.setCjxxsczt("1");
                            fzjcxxDto.setCjxxscsj(dff.format(df.parse(element.element("CLRQSJ").getText())));
                            fzjcxxDto.setBbzbh(bbzbh);
                            list.add(fzjcxxDto);
                        }else {
                            fzjcxxDto.setCjxxsczt("2");
                            fzjcxxDto.setCjxxscsj(dff.format(df.parse(element.element("CLRQSJ").getText())));
                            fzjcxxDto.setBbzbh(bbzbh);
                            list.add(fzjcxxDto);
                        }
                    }
                }
            }
            dao.updateCjxxsc(list);
            return true;
        } catch (java.lang.Exception e) {
            log.error("cjsjResponseDeal报错"+e.getMessage());
            return false;
        }
    }



    @Override
    public List<FzjcxxDto> getResumeList(FzjcxxDto fzjcxxDto) {
        return dao.getResumeList(fzjcxxDto);
    }
}
