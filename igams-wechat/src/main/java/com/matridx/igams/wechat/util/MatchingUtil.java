package com.matridx.igams.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.InterfaceExceptionUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.SslUtils;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import com.matridx.springboot.util.encrypt.Encrypt_ECB;
import com.matridx.springboot.util.xml.XmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.NodeList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 外部对接接口工具类
 *
 * @author hmz
 **/
public class MatchingUtil {
    //日志
    private Logger log = LoggerFactory.getLogger(MatchingUtil.class);
    //调用信息
    private Map<String, Object> map;
    //接口调用明细a
    private JkdymxDto jkdymxDto;
    //redis工具类
    private RedisUtil redisUtil;
    //restTemplate
    private RestTemplate restTemplate;
    //rabbitmq工具类
    private AmqpTemplate amqpTempl;
    //送检信息service
    private ISjxxService sjxxService;
    //送检信息service
    private IFjsqService fjsqService;
    //送检检测项目service
    private ISjjcxmService sjjcxmService;
    //送检实验管理service
    private ISjsyglService sjsyglService;
    //项目实验管理service
    private IXmsyglService xmsyglService;
    /**
     * 医院信息service
     */
    private IYyxxService yyxxService;
    //钉钉消息工具类
    private DingTalkUtil dingTalkUtil;
    //钉钉消息管理service
    private IDdxxglService ddxxglService;
    //送检伙伴信息service
    private ISjhbxxService sjhbxxService;
    //信息对应service
    private IXxdyService xxdyService;
    //送检扩展信息service
    private ISjkzxxService sjkzxxService;
    //接口调用明细service
    private IJkdymxService jkdymxService;
    //外部送检信息service
    private IWbsjxxService wbsjxxService;

    private ISjgzbyService sjgzbyService;

    private IHbsfbzService hbsfbzService;
    private ISjnyxService sjnyxService;
    private ISjdlxxService sjdlxxService;
	private ISjxxjgService sjxxjgService;
    private INyypxxService nyypxxService;

    private InterfaceExceptionUtil interfaceExceptionUtil;
	/**
     * @Description: 艾迪康获取token
     * @Author: 郭祥杰
     * @Date: 2025/6/23 16:16
     */
    final String ADK_TOKEN_URL = "/api-sample/entrust/open/gettoken";
    /**
     * @Description: 返回杰毅的订单详情
     * @Author: 郭祥杰
     * @Date: 2025/6/23 16:16
     */
    final String ADK_GETAPPLICATIONDETAIL_URL = "/api-sample/external/getApplicationDetail";
    /**
     * 获取授权Token
     */
    final String QIANMAI_TOKEN_URL = "/api/v1/authentication/requesttoken";
    /**
     * 千麦外送第三方获取千麦外送数据
     * 从此接口可以获取，千麦外送给第三方的委托数据，
     * 当客户拿到千麦委托数据后，可以回写标记，下次就不会再次给这条数据了
     * 有千麦条码时，结束时间不起作用，时间差不超过7天
     */
    final String QIANMAI_INFO_GET_URL = "/api/v1/entrust/getdelegatedata";
    /**
     * 千麦外送第三方获取千麦外送数据后回调
     * 当客户拿到千麦委托数据后，回写接口，下次就不会再次给这条数据了
     */
    final String QIANMAI_BACK_URL = "/api/v1/entrust/uploadentruststatus";
    /**
     * 千麦外送第三方，出结果后回写千麦接口
     * 当客户出具结果后，调用接口，把结果返回给千麦。注意这里回调千麦接口的时候，必须要跟千麦的每个小项都有对照了，才能回写回来，否则会返回错误，告知哪些还未对照。对照关系维护在千麦方，数据中心=>实验室=>外部项目对照
     */
    final String QIANMAI_REPORT_RETURN_URL = "/api/v1/entrust/uploadresult";
    /**
     * 获取授权Token
     */
    final String HENUO_TOKEN_URL = "/limsDxp/loginDxp";
    /**
     * 禾诺外送第三方获取禾诺外送数据
     * 从此接口可以获取，禾诺外送给第三方的委托数据，
     * 当客户拿到禾诺委托数据后，可以回写标记，下次就不会再次给这条数据了
     * 有禾诺条码时，结束时间不起作用，时间差不超过7天
     */
    final String HENUO_INFO_GET_URL = "/limsDxp/entSampleBiz/sampleList";
    final String HENUO_ITEMLIST_URL = "/limsDxp/baseData/itemList";
    final String HENUO_UPLOAD_URL = "/limsDxp/entReportBiz/upload";
    /**
     * 获取授权Token
     */
    final String AIDIKANG_TOKEN_URL = "/api/oauth/gettoken";
    /**
     * 艾迪康 写入报告信息(PDF文件)
     * 备注：第三方本地化系统将报告单文件写入艾迪康系统
     */
    final String AIDIKANG_REPORT_RETURN_URL = "/api/report/reportfile/create";
    /**
     * 艾迪康2 获取授权Token、获取样本、回传报告
     */
    final String AIDIKANG_NEW_TOKEN_URL = "/api-sample/entrust/open/gettoken";
    final String AIDIKANG_NEW_INFO_GET_URL = "/api-sample/entrust/open/sampleinf";
    final String AIDIKANG_NEW_REPORT_RETURN_URL = "/api-sample/entrust/open/result";
    /**
     * 艾迪康3 获取授权Token、获取样本、回传报告
     */
    final String AIDIKANG_NEWNEW_INFO_GET_URL = "/api-sample/external/getApplicationDetail";
    final String AIDIKANG_NEWNEW_REPORT_RETURN_URL = "/api-sample/external/createReport";

    /**
     * 先声外送第三方获取先声外送数据
     * 从此接口可以获取，先声外送给第三方的委托数据，
     */
    final String XIANSHENG_INFO_GET_URL = "/std/order/detail";
    final String XIANSHENG_INFO_CONFIRM_URL = "/lims/api/open/receiveOrderSample";
    final String XIANSHENG_REPORT_RETURN_URL = "/lims/bioData/synBioData/saveBioResult";
    //先声创建任务单
    final String XIASHENG_CREATETASK_URL="/lims/api/open/createTask";
    //先声保存实验结果
    final String XIASHENG_SAVERESULT_URL="/lims/api/open/saveExperimentResult";

    //-------------------------------------------------------------------------------------------------//
    //-------------------------------------------以下为公用方法-------------------------------------------//
    //-------------------------------------------------------------------------------------------------//

    /**
     * 接口 根据路径创建文件
     *
     * @param ywlx         业务类型
     * @param realFilePath 真实路径
     * @return String 文件路径
     */
    public String mkDirs(String ywlx, String realFilePath) {
        String storePath;
        if (ywlx != null) {
            //根据日期创建文件夹
            storePath = realFilePath + ywlx + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
                    DateUtils.getCustomFomratCurrentDate("yyyyMMdd");

        } else {
            storePath = realFilePath;
        }

        File file = new File(storePath);
        if (file.isDirectory()) {
            return storePath;
        }
        if (file.mkdirs()) {
            return storePath;
        }
        return null;
    }

    /**
     * 接口 处理pdf文件 转为base64
     *
     * @param pdfPath pdf文件路径
     * @return String base64
     */
    public String getPdfMsg(String pdfPath) {
        byte[] pdfData = null;
        Base64 base64 = new Base64();//sun.misc.BASE64Decoder() jdk8可用，11不可用，改为当前的方法替换
        DBEncrypt dbEncrypt = new DBEncrypt();
        try {
            if (StringUtil.isNotBlank(pdfPath)) {
                FileInputStream fileInputStream = new FileInputStream(dbEncrypt.dCode(pdfPath));
                pdfData = new byte[fileInputStream.available()];
                fileInputStream.read(pdfData);
                fileInputStream.close();
            } else {
                return "";
            }

        } catch (IOException e) {
            log.error("调用接口--数据回传------PDF文件转base64报错" + e.getMessage());
        }
        return base64.encodeAsString(pdfData);//sun.misc.BASE64Decoder() jdk8可用，11不可用，改为当前的方法替换
    }

    // 疾控MD5相关方法
    private static final String ALGORITHM = "MD5";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private String MD5encode(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes());
                return getFormattedText(messageDigest.digest());
            } catch (Exception var2) {
                return null;
            }
        }
    }

    private String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);

        for(int j = 0; j < len; ++j) {
            buf.append(HEX_DIGITS[bytes[j] >> 4 & 15]);
            buf.append(HEX_DIGITS[bytes[j] & 15]);
        }
        return buf.toString();
    }
    // 疾控AES加密
    private String encrypt4Aes(String data) {
        return AESCoderEncrypt(data, "967E0A3263980523");
    }

    private String AESCoderEncrypt(String data, String key) {
        if (key == null) {
            System.out.println("Key is null");
            return null;
        } else if (key.length() != 16) {
            System.out.println(key + "length <> 16");
            return null;
        } else {
            try {
                byte[] keyBytes = key.getBytes("utf-8");
                byte[] ivBytes = "A14B3AA27DEEB4CB".getBytes("utf-8");
                SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec ivParam = new IvParameterSpec(ivBytes);
                cipher.init(1, skeySpec, ivParam);
                byte[] encrypted = cipher.doFinal(data.getBytes("utf-8"));
                return (new Base64()).encodeToString(encrypted);
            } catch (Exception var8) {
                Exception e = var8;
                e.printStackTrace();
                return null;
            }
        }
    }
    //初始化
    public void init(Map<String, Object> map, RedisUtil redisUtil, RestTemplate restTemplate, AmqpTemplate amqpTempl,
                     ISjxxService sjxxService, ISjjcxmService sjjcxmService, ISjsyglService sjsyglService, IYyxxService yyxxService,
                     DingTalkUtil dingTalkUtil, IDdxxglService ddxxglService, ISjhbxxService sjhbxxService, IXxdyService xxdyService,
                     ISjkzxxService sjkzxxService, IXmsyglService xmsyglService, IFjsqService fjsqService, IJkdymxService jkdymxService,
                     IWbsjxxService wbsjxxService ,ISjgzbyService sjgzbyService,IHbsfbzService hbsfbzService,ISjnyxService sjnyxService,ISjdlxxService sjdlxxService,
                     ISjxxjgService sjxxjgService,InterfaceExceptionUtil interfaceExceptionUtil,INyypxxService nyypxxService) {
        this.map = map;
        this.redisUtil = redisUtil;
        this.restTemplate = restTemplate;
        this.amqpTempl = amqpTempl;
        this.sjxxService = sjxxService;
        this.sjjcxmService = sjjcxmService;
        this.sjsyglService = sjsyglService;
        this.yyxxService = yyxxService;
        this.dingTalkUtil = dingTalkUtil;
        this.ddxxglService = ddxxglService;
        this.sjhbxxService = sjhbxxService;
        this.xxdyService = xxdyService;
        this.sjkzxxService = sjkzxxService;
        this.xmsyglService = xmsyglService;
        this.fjsqService = fjsqService;
        this.jkdymxService = jkdymxService;
        this.wbsjxxService = wbsjxxService;
        this.jkdymxDto = initJkdymxDto();
        this.sjgzbyService=sjgzbyService;
        this.hbsfbzService=hbsfbzService;
        this.sjnyxService=sjnyxService;
        this.sjdlxxService=sjdlxxService;
        this.sjxxjgService=sjxxjgService;
        this.interfaceExceptionUtil=interfaceExceptionUtil;
        this.nyypxxService=nyypxxService;
    }

    /**
     * 初始化接口调用明细DTO
     * @return
     */
    public JkdymxDto initJkdymxDto(){
        JkdymxDto jkdymxDto = new JkdymxDto();
        jkdymxDto.setLxqf("send"); // 类型区分 发送 send;接收recv
        jkdymxDto.setDyfl(InvokingTypeEnum.RECEIVE_INSPECTINFO.getCode());
        jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null));
        return jkdymxDto;
    }

    /**
     * 匹配样本类型
     * @param sjxxDto
     * @param yblxList
     * @param yblxmc
     * @param redisUtil
     */
    public void compareYblx(SjxxDto sjxxDto,List<JcsjDto> yblxList,String yblxmc,RedisUtil redisUtil){
        JcsjDto yblxDto = new JcsjDto();
        for (JcsjDto yblx : yblxList) {
            if (yblx.getCsmc().equals(yblxmc)) {
                sjxxDto.setYblx(yblx.getCsid());
                sjxxDto.setYblxdm(yblx.getCsdm());
                yblxDto = yblx;
                break;
            }
            // 20240904 "其它"样本类型的csdm由XXX改为G
            if ("XXX".equals(yblx.getCsdm()) || "G".equals(yblx.getCsdm())) {
                yblxDto = yblx;
            }
        }
        // 20240904 "其它"样本类型的csdm由XXX改为G
        if (("XXX".equals(yblxDto.getCsdm()) || ("G".equals(yblxDto.getCsdm()))) && StringUtil.isNotBlank(yblxmc)) {
            Object obj = redisUtil.hget("XXDY:YBLX", yblxmc);
            if (obj != null){
                XxdyDto xxdy = JSON.parseObject(obj.toString(),XxdyDto.class);
                JcsjDto ybjcsjDto = redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(),xxdy.getDyxx());
                if (ybjcsjDto != null){
                    yblxDto = ybjcsjDto;
                }
            }
        }
        sjxxDto.setYblx(yblxDto.getCsid());
        sjxxDto.setYblxdm(yblxDto.getCsdm());
        sjxxDto.setYblxmc(yblxmc);
        //判断如果样本类型选择其他时，并且其他标本类型内容为"全血"或者"血浆"时，标本类型传"B"
        // 20240904 "其它"样本类型的csdm由XXX改为G
        if ("XXX".equals(sjxxDto.getYblxdm()) || "G".equals(sjxxDto.getYblxdm())) {
            if ("全血".equals(sjxxDto.getYblxmc()) || "血浆".equals(sjxxDto.getYblxmc())|| sjxxDto.getYblxmc().contains("外周血")) {
                sjxxDto.setYblxdm("B");
            }
        }
    }

    /**
     * 匹配检测项目
     * @param sjxxDto
     * @param jcxmList
     * @param jcxmmc
     * @param jcxmdm
     * @param splitStr
     */
    public void compareJcxm(SjxxDto sjxxDto,List<JcsjDto> jcxmList,String jcxmmc,String jcxmdm,String splitStr,IXxdyService tmpXxdyService){
        List<String> xms = new ArrayList<>();
        if (StringUtil.isNotBlank(jcxmdm)){
            if (StringUtil.isNotBlank(splitStr)){
                jcxmdm = jcxmdm.replaceAll("，", ",");
                xms = List.of(jcxmdm.split(splitStr));
            } else {
                xms = Collections.singletonList(jcxmdm);
            }
        } else if (StringUtil.isNotBlank(jcxmmc)){
            if (StringUtil.isNotBlank(splitStr)){
                jcxmmc = jcxmmc.replaceAll("，", ",");
                xms = List.of(jcxmmc.split(splitStr));
            } else {
                xms = Collections.singletonList(jcxmmc);
            }
        }
        List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < xms.size(); i++) {
            String xm = xms.get(i);
            list.add(Map.of(
                    "yxx", sjxxDto.getDb() + "-" + xm,
                    "dylx", "JCXMPP"
            ));
            list.add(Map.of(
                    "yxx", xm,
                    "dylx", "JCXMPP"
            ));
        }
        for (int i = 0; i < xms.size(); i++) {
            String xm = xms.get(i);
            list.add(Map.of(
                    "yxx", sjxxDto.getDb() + "-" + xm,
                    "dylx", "JCZXMPP"
            ));
            list.add(Map.of(
                    "yxx", xm,
                    "dylx", "JCZXMPP"
            ));
        }
        if (!CollectionUtils.isEmpty(list)){
            List<JcsjDto> pps = tmpXxdyService.getCompareListSortByList(list);
            if (!CollectionUtils.isEmpty(pps)){
                Optional<JcsjDto> jcxmOptional = pps.stream().filter(item -> "DETECT_TYPE".equals(item.getJclb())).findFirst();
                if (jcxmOptional.isPresent()){
                    SjjcxmDto sjjcxmDto = new SjjcxmDto();
                    sjjcxmDto.setJcxmid(jcxmOptional.get().getCsid());
                    sjjcxmDtos.add(sjjcxmDto);
                    List<String> jcxmids = !CollectionUtils.isEmpty(sjxxDto.getJcxmids())? sjxxDto.getJcxmids():new ArrayList<String>();
                    jcxmids.add(jcxmOptional.get().getCsid());
                    sjxxDto.setJcxmids(jcxmids);
                    sjxxDto.setSjjcxms(sjjcxmDtos);
                    sjxxDto.setJcxmmc((StringUtil.isNotBlank(sjxxDto.getJcxmmc()) ? (sjxxDto.getJcxmmc() + ",") : "") + jcxmOptional.get().getCsmc());
                } else {
                    Optional<JcsjDto> jczxmOptional = pps.stream().filter(item -> "DETECT_SUBTYPE".equals(item.getJclb())).findFirst();
                    if (jczxmOptional.isPresent()){
                        SjjcxmDto sjjcxmDto = new SjjcxmDto();
                        sjjcxmDto.setJcxmid(jczxmOptional.get().getFcsid());
                        sjjcxmDto.setJczxmid(jczxmOptional.get().getCsid());
                        sjjcxmDtos.add(sjjcxmDto);
                        sjxxDto.setSjjcxms(sjjcxmDtos);
                        sjxxDto.setJcxmmc((StringUtil.isNotBlank(sjxxDto.getJcxmmc()) ? (sjxxDto.getJcxmmc() + ",") : "") + jczxmOptional.get().getCsmc());
                    }
                }
            }
        }
    }

    /**
     * 重新发送rabbit
     * @param map
     * @param message
     */
    private void reSendRabbit(Map<String, Object> map,String message){
        int repetitions = 0;
        List<String> exceptionMessage = new ArrayList<>();
        // 重新发送rabbit
        if (map.containsKey("Repetitions")){
            // 判断重复次数
            repetitions = Integer.parseInt(map.get("Repetitions").toString());
        }
        if (map.containsKey("ExceptionMessage")){
            // 判断重复次数
            exceptionMessage = JSONArray.parseObject((String) map.get("ExceptionMessage"), new TypeReference<List<String>>() {}) ;
        }
        repetitions ++;
        map.put("Repetitions", repetitions);
        exceptionMessage.add(repetitions+"."+message);
        map.put("ExceptionMessage", JSON.toJSONString(exceptionMessage));
        amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(map));
    }
	
    /**
     * 计算年龄
     *
     * @param birthday      生日
     * @param dateFormatter 日期格式化程序
     * @return {@link String }
     */
    private String calculateAge(String birthday,String dateFormatter){
        if ("0".equals(birthday)||StringUtil.isBlank(birthday)){
            return "0";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatter);
        LocalDate birthDate = LocalDate.parse(birthday, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return String.valueOf(period.getYears());
    }

    /**
     * 处理保存数据
     *
     * @param sjxxDto 送检信息
     * @param sjqfdm  送检区分代码
     * @param kdlxid  快递类型id
     * @param sjqfid  送检区分id
     * @param person  人员
     * @param info    信息
     * @return Map<String, Object> 保存状态信息
     */
    public Map<String, Object> dealSaveSjxxDto(SjxxDto sjxxDto, String sjqfdm, String kdlxid, String sjqfid, String person, String info) {
        if (StringUtil.isNotBlank(sjxxDto.getDb())) {
            // 若收费且金额为空时，获取默认金额
            if (!"0".equals(sjxxDto.getSfsf()) && StringUtil.isBlank(sjxxDto.getFkje())) {
                SjhbxxDto sjhbxxDto = new SjhbxxDto();
                List<SjjcxmDto> sjjcxms = sjxxDto.getSjjcxms();
                sjhbxxDto.setHbmc(sjxxDto.getDb());
                SjhbxxDto reSjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
                List<HbsfbzDto> sfbzlist=hbsfbzService.viewByid(reSjhbxxDto.getHbid());
                BigDecimal fkje=new BigDecimal("0");
                if(!CollectionUtils.isEmpty(sjjcxms)){
                    for(SjjcxmDto sjjcxmDto:sjjcxms){

                        Optional<HbsfbzDto> opt=sfbzlist.stream().filter(e->StringUtil.isNotBlank(e.getJcxmid())&&StringUtil.isNotBlank(sjjcxmDto.getJcxmid())&&e.getJcxmid().equals(sjjcxmDto.getJcxmid())).filter(e->StringUtil.isNotBlank(e.getJczxmid())&&StringUtil.isNotBlank(sjjcxmDto.getJczxmid())&&e.getJczxmid().equals(sjjcxmDto.getJczxmid())).findFirst();
                        if(opt.isPresent()){
                            sjjcxmDto.setYfje(opt.get().getSfbz());
                            String yfje=StringUtil.isNotBlank(sjjcxmDto.getYfje())?sjjcxmDto.getYfje():"0";
                            fkje=fkje.add(new BigDecimal(yfje));
                        }else{
                            Optional<HbsfbzDto> optt=sfbzlist.stream().filter(e->StringUtil.isNotBlank(e.getJcxmid())&&StringUtil.isNotBlank(sjjcxmDto.getJcxmid())&&e.getJcxmid().equals(sjjcxmDto.getJcxmid())).findFirst();
                            if(optt.isPresent()){
                                sjjcxmDto.setYfje(optt.get().getSfbz());
                                String yfje=StringUtil.isNotBlank(sjjcxmDto.getYfje())?sjjcxmDto.getYfje():"0";
                                fkje=fkje.add(new BigDecimal(yfje));
                            }
                        }
                    }
                }
                sjxxDto.setFkje(fkje.toString());
                // 判断若为免D或免R时减半
//                if (reSjhbxxDto != null && StringUtil.isNotBlank(reSjhbxxDto.getSfbz())) {
//                    String fkje = reSjhbxxDto.getSfbz();
//                    if ("2".equals(sjxxDto.getSfsf()) || "3".equals(sjxxDto.getSfsf())) {
//                        fkje = String.valueOf(Double.parseDouble(reSjhbxxDto.getSfbz()) / 2);
//                    }
//
//                }
            }
        }
        //查询是否有重复
        SjxxDto sjxxDto_t = sjxxService.getSjxxInfoByYbbh(sjxxDto);
        boolean isDealSuccess = false;
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("样本编号", sjxxDto.getYbbh());
        infoMap.put("外部编码", sjxxDto.getWbbm());
        if (sjxxDto_t != null) {
            if (StringUtil.isNotBlank(sjxxDto_t.getJsrq())) {
                sjxxDto.setSjid(sjxxDto_t.getSjid());
                sjxxDto.setJcdw(sjxxDto_t.getJcdw());
                sjxxDto.setJcdwmc(sjxxDto_t.getJcdwmc());

                SjxxDto updateSjxx = new SjxxDto();
                updateSjxx.setSjid(sjxxDto_t.getSjid());
                updateSjxx.setXgry(sjxxDto.getWbbm());
                updateSjxx.setWbbm(sjxxDto.getWbbm());
                updateSjxx.setHzxm(sjxxDto.getHzxm());
                updateSjxx.setXb(sjxxDto.getXb());
                updateSjxx.setNl(sjxxDto.getNl());
                updateSjxx.setNldw(sjxxDto.getNldw());
                updateSjxx.setKs(sjxxDto.getKs());
                updateSjxx.setQtks(sjxxDto.getQtks());
                updateSjxx.setSjdw(sjxxDto.getSjdw());
                updateSjxx.setSjdwmc(sjxxDto.getSjdwmc());
                updateSjxx.setLczz(sjxxDto.getLczz());
                sjxxService.updateByid(updateSjxx);
                //执行更新方法
                log.error("处理申请单信息------样本已接收，只更新wbbm，xb,nl,ks,sjdw！样本编号：" + sjxxDto.getYbbh() + "，外部编码：" + sjxxDto.getWbbm());
                infoMap.put("STATUS", "exist");
            } else {
                try {
                    isDealSuccess = updateMatchingSjxxDto(sjxxDto, sjxxDto_t, sjqfdm, person);
                } catch (Exception e) {
                    log.error("处理申请单信息------更新失败！" + e.getMessage() + JSON.toJSONString(info));
                    infoMap.put("STATUS", "fail");
                }
                if (isDealSuccess) {
                    log.error("处理申请单信息------更新成功！样本编号：" + sjxxDto.getYbbh() + "，外部编码：" + sjxxDto.getWbbm());
                    infoMap.put("STATUS", "update");
                } else {
                    log.error("处理申请单信息------更新失败！" + JSON.toJSONString(info));
                    infoMap.put("STATUS", "fail");
                }
            }
        } else {
            //执行新增方法
            try {
                sjxxDto.setKdlx(kdlxid);//快递类型
                sjxxDto.setKdh("无");//快递单号
                sjxxDto.setSjqf(sjqfid);//送检区分
                sjxxDto.setSjid(StringUtil.generateUUID());//送检id
                sjxxDto.setLrry(person);//录入人员
                sjxxDto.setScbj("0");//删除标记
                sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());//状态
                sjxxDto.setSfs("1");//是否收费
                isDealSuccess = insertMatchingSjxxDto(sjxxDto, sjqfdm);
            } catch (Exception e) {
                log.error("处理申请单信息------新增失败！" + e.getMessage() + JSON.toJSONString(info));
                infoMap.put("STATUS", "fail");
            }
            if (isDealSuccess) {
                log.error("处理申请单信息------新增成功！样本编号：" + sjxxDto.getYbbh() + "，外部编码：" + sjxxDto.getWbbm());
                infoMap.put("STATUS", "insert");
            } else {
                log.error("处理申请单信息------新增失败！" + JSON.toJSONString(info));
                infoMap.put("STATUS", "fail");
            }
        }
        return infoMap;
    }

    /**
     * 新增送检信息
     *
     * @param sjxxDto sjxx
     * @param sjqfdm  送检区分代码
     * @return boolean 是否成功
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertMatchingSjxxDto(SjxxDto sjxxDto, String sjqfdm) throws Exception {
        boolean isSuccess;
        isSuccess = sjxxService.insertDto(sjxxDto);
        if (!isSuccess) {
            throw new BusinessException("msg", "新增送检信息表失败！");
        }
        isSuccess = insertOrUpdateSjjcxm(sjxxDto);
        if (!isSuccess) {
            throw new BusinessException("msg", "新增检测项目表失败！");
        }
        RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADD.getCode() + JSONObject.toJSONString(sjxxDto));
        sjxxDto.setSjqf(sjqfdm);
        //筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
		//清除之前的数据，重新设置sjid查询所有复检信息
		FjsqDto fjsqDto_t = new FjsqDto();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=sdf.format(new Date());
		fjsqDto_t.setDsyrq(nowDate);
		fjsqDto_t.setSjid(sjxxDto.getSjid());
		List<FjsqDto> resultFjDtos = fjsqService.getSyxxByFj(fjsqDto_t);
		if(resultFjDtos!=null && resultFjDtos.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for(FjsqDto res_FjsqDto:resultFjDtos) {
				ids.add(res_FjsqDto.getFjid());
			}
			sjxxDto.setIds(ids);
		}
        List<SjsyglDto> insertInfo = sjsyglService.getDetectionInfo(sjxxDto, null, DetectionTypeEnum.DETECT_SJ.getCode());
        
		if (!CollectionUtils.isEmpty(insertInfo)){
			//更新项目实验数据和送检实验数据
			isSuccess = sjxxService.addOrUpdateSyData(insertInfo,sjxxDto,sjxxDto.getLrry());
			
			if (isSuccess) {
				SjsyglDto sjsyglDto_t=new SjsyglDto();
				sjsyglDto_t.setSjid(sjxxDto.getSjid());
				//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
				List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_ADD.getCode() + JSONObject.toJSONString(list));

		        XmsyglDto xmsyglDto_t = new XmsyglDto();
				xmsyglDto_t.setYwid(sjxxDto.getSjid());
				List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
			}
        }
        return isSuccess;
    }

    /**
     * 更新送检信息
     *
     * @param sjxxDto   sjxx
     * @param sjxxDto_t 数据库中的sjxx
     * @param sjqfdm    送检区分代码
     * @param person    修改人员
     * @return boolean 是否成功
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateMatchingSjxxDto(SjxxDto sjxxDto, SjxxDto sjxxDto_t, String sjqfdm, String person) throws BusinessException {
        boolean isSuccess;
        //执行更新方法
        sjxxDto.setXgry(person);//修改人员
        sjxxDto.setKdh(StringUtil.isNotBlank(sjxxDto_t.getKdh()) ? sjxxDto_t.getKdh() : "无");//快递单号，若为空则设置为无
        sjxxDto.setSjid(sjxxDto_t.getSjid());//送检id

        sjxxDto.setJcdw(sjxxDto_t.getJcdw());
        sjxxDto.setJcdwmc(sjxxDto_t.getJcdwmc());

        sjxxDto.setBz(StringUtil.isNotBlank(sjxxDto_t.getBz()) ? sjxxDto_t.getBz() : sjxxDto.getBz());//备注，若原来有备注则不覆盖
        sjxxDto.setJcdw(null);
        isSuccess = sjxxService.update(sjxxDto);
        if (!isSuccess) {
            throw new BusinessException("msg", "更新送检信息表失败！");
        }
        if (StringUtil.isNotBlank(sjxxDto.getJcxmmc())) {
            isSuccess = insertOrUpdateSjjcxm(sjxxDto);
        }
        if (!isSuccess) {
            throw new BusinessException("msg", "更新检测项目表失败！");
        }
        RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_XHU.getCode() + JSONObject.toJSONString(sjxxDto));
        sjxxDto.setSjqf(sjqfdm);
      //筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
		//清除之前的数据，重新设置sjid查询所有复检信息
		FjsqDto fjsqDto_t = new FjsqDto();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=sdf.format(new Date());
		fjsqDto_t.setDsyrq(nowDate);
		fjsqDto_t.setSjid(sjxxDto.getSjid());
		List<FjsqDto> resultFjDtos = fjsqService.getSyxxByFj(fjsqDto_t);
		if(resultFjDtos!=null && resultFjDtos.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for(FjsqDto res_FjsqDto:resultFjDtos) {
				ids.add(res_FjsqDto.getFjid());
			}
			sjxxDto.setIds(ids);
		}
        List<SjsyglDto> insertInfo = sjsyglService.getDetectionInfo(sjxxDto, null, DetectionTypeEnum.DETECT_SJ.getCode());

		if (!CollectionUtils.isEmpty(insertInfo)){
			//更新项目实验数据和送检实验数据
			isSuccess = sjxxService.addOrUpdateSyData(insertInfo,sjxxDto,person);
			
			if (isSuccess) {
				SjsyglDto sjsyglDto_t=new SjsyglDto();
				sjsyglDto_t.setSjid(sjxxDto.getSjid());
				//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
				List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
				XmsyglDto xmsyglDto_t = new XmsyglDto();
				xmsyglDto_t.setYwid(sjxxDto.getSjid());
				List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
			}
        }
		return true;
    }

    /**
     * 新增或修改送检检测项目
     *
     * @param sjxxDto sjxx
     * @return boolean 是否成功
     */
    private boolean insertOrUpdateSjjcxm(SjxxDto sjxxDto) {
        //修改送检检测项目
        List<SjjcxmDto> sjjcxms = sjxxDto.getSjjcxms();
        if (!CollectionUtils.isEmpty(sjjcxms)) {
            for (int i = 0; i < sjjcxms.size(); i++) {
                sjjcxms.get(i).setSjid(sjxxDto.getSjid());
                sjjcxms.get(i).setXh(String.valueOf(i + 1));
                String jczxmid = sjxxDto.getJczxm();
                if (StringUtil.isNotBlank(jczxmid)) {
                    sjjcxms.get(i).setJczxmid(jczxmid);
                }
            }
            sjxxDto.setSjjcxms(sjjcxms);
        }
        sjxxDto.setXgry(StringUtil.isNotBlank(sjxxDto.getXgry())?sjxxDto.getXgry():sjxxDto.getLrry());
        return sjjcxmService.updateBySjxx(sjxxDto,null);
    }

    /**
     * 发送未匹配消息
     *
     * @param unCompareInfo         未匹配科室信息
     * @param unCompareDetectionInfo    未匹配检测项目信息
     * @param msgTitle           消息标题
     */
    private void sendMessage(List<String> unCompareInfo, List<String> unCompareDetectionInfo, Object uploadFailedSampleInfo, String msgTitle) {
        if (!CollectionUtils.isEmpty(unCompareInfo)) {
            StringBuilder msgContent = new StringBuilder(msgTitle + "，当前有科室信息未匹配！");
            for (String msg : unCompareInfo) {
                msgContent.append("\n\n").append(msg);
            }
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UNCOMPARE_KS_INFO.getCode());
            if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
                for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                    if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                        dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), msgTitle + "，当前有科室信息未匹配！", msgContent.toString());
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(unCompareDetectionInfo)) {
            StringBuilder msgContent = new StringBuilder(msgTitle + "，当前有检测项目信息未匹配！");
            for (String msg : unCompareDetectionInfo) {
                msgContent.append("\n\n").append(msg);
            }
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UNCOMPARE_DETECT_INFO.getCode());
            if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
                for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                    if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                        dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), msgTitle + "，当前有检测项目信息未匹配！", msgContent.toString());
                    }
                }
            }
        }
        if (uploadFailedSampleInfo != null) {
            StringBuilder msgContent = new StringBuilder(msgTitle);
            msgContent.append("\n\n").append(JSON.toJSONString(uploadFailedSampleInfo));
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UPLOAD_FAILED_SAMPLE_INFO.getCode());
            if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
                for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                    if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                        dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), msgTitle + "，当前有报告回传失败！", msgContent.toString());
                    }
                }
            }
        }
    }

    /**
     * 杏和接口调用方法
     * hospitalId 医院id（必传）
     * method 调用方法
     * selectByDate		根据日期查询数据
     * startDate 开始日期 格式：yyyy-MM-dd 不传默认当天
     * startTime 开始时间 格式：HH:mm 不传默认00:00
     * endDate 结束日期 格式：yyyy-MM-dd 不传默认当天
     * endTime 结束时间 格式：HH:mm 不传默认23:59
     * selectById			根据样本编号查询数据
     * requisition_id		外部编码（必传）
     * updateById			申请单状态修改（未使用）
     * uploadById			上传报告数据
     * wjlj			pdf报告文件路径（必传）
     * SampleInfo_INSPECTION_SID		检验单号 此报告在实验室的唯一号 20191108G0039952
     * SampleInfo_GROUP_ID 			分组号 检测小组代号 G063
     * SampleInfo_INSPECTION_CLASS		报告类型 一般检验类、微生物检验类、骨髓检验类、一般检验描述类
     * SampleInfo_REQUISITION_ID		条码号 送检机构标签号码 1027755492
     * SampleInfo_PDF_NAME 			报告单名称 一个送检标本检测机构分多次出报告的报告单名称必须有区别，可直接传检测机构的报告单名称 20191108G0039952
     * ResultInfo_TEST_ITEM_ID			分析项目代码 服务机构分析项目代码 TEST3666
     * ResultInfo_GROUP_ID				分组号 检测小组代号 G063
     * ResultInfo_SAMPLE_NUMBER		检测顺序号 样本号 2437
     * ResultInfo_TEST_ITEM_REFERENCE	参考范围 如：2.00-3.98
     * ResultInfo_TEST_METHOD			检测方法 如：荧光PCR法
     * INSPECTION_INSTRUMENT			检测设备 如：ACCESS2，AU5800无具体设备传无
     * ResultInfo_QUANTITATIVE_RESULT	检测定量结果1.35
     * ResultInfo_TEST_ITEM_UNIT		检测单位ng/ml
     *
     */
    public void xingheInfo() {
        Object hospitalIdsObj = map.get("hospitalId");
        Object methodObj = map.get("method");
        if (null != hospitalIdsObj && StringUtil.isNotBlank(hospitalIdsObj.toString())) {
            String hospitalId = hospitalIdsObj.toString();
            String method = "";
            if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
                method = methodObj.toString();
            } else {
                log.error("调用杏和接口------未传入调用方法名");
            }
            try {
                jkdymxDto.setDydz("xingheInfo."+method+"."+hospitalId);
                if ("selectByDate".equals(method)) {
                    Map<String, Object> xingheInfoByDate = generateXingheAreaMsg("SelectAreaReqInfo", "RQ", hospitalId, map);
                    dealReturnInfo(xingheInfoByDate, hospitalId,"old");
                } else if ("selectById".equals(method)) {
                    Map<String, Object> xingheInfoById = generateXingheAreaMsg("SelectAreaReqInfo", "ID", hospitalId, map);
                    dealReturnInfo(xingheInfoById, hospitalId,"old");
                } else if ("updateById".equals(method)) {
                    log.error("调用杏和接口------该调用方法暂未启用method：" + method);
                    // Map<String, Object> xingheInfoByUpdate = generateXingheAreaMsg("UpdateAreaReqState",null,hospitalId,map);
                } else if ("uploadById".equals(method)) {
                    Map<String, Object> xingheInfoByUpload = generateXingheAreaMsg("RecvAreaResultInfo", null, hospitalId, map);
                    String resultCode = (String) xingheInfoByUpload.get("ResultCode");
                    if ("-1".equals(resultCode)){
                        sendMessage(null,null,xingheInfoByUpload,"杏和系统-样本-"+map.get("ybbh")+"-医院：" + hospitalId+"，报告回传失败！");
                        reSendRabbit(map,"杏和系统-样本-"+map.get("ybbh")+"-医院：" + hospitalId+"，报告回传失败！");
                    }
                    jkdymxDto.setFhnr(JSON.toJSONString(xingheInfoByUpload).length()>4000?JSON.toJSONString(xingheInfoByUpload).substring(0,4000):JSON.toJSONString(xingheInfoByUpload)); // 返回内容
                    jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                    jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
                    log.error("调用杏和接口------上传报告数据--样本编号：" + map.get("ybbh") + "--上传结果：" + JSON.toJSONString(xingheInfoByUpload));
                } else {
                    log.error("调用杏和接口------调用方法名出错method：" + method);
                }
                jkdymxService.insertJkdymxDto(jkdymxDto);
            } catch (Exception e) {
                String message = e.getMessage();
                log.error("调用杏和接口---失败---" + message);
                if ("uploadById".equals(method)){
                    reSendRabbit(map,"杏和系统-样本-"+map.get("ybbh")+"-医院：" + hospitalId+"，报告回传失败！"+e.getMessage());
                }
                if(message != null && message.length() > 4000){
                    message = message.substring(0,4000);
                }
                jkdymxDto.setFhnr(message); // 返回内容
                jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
                jkdymxService.insertJkdymxDto(jkdymxDto);
            }
        } else {
            log.error("调用杏和接口------未传入hospitalId");
        }
    }

    /**
     * 杏和接口调用方法  6.0
     * hospitalId 医院id（必传）
     * method 调用方法
     * selectByDate		根据日期查询数据
     * startDate 开始日期 格式：yyyy-MM-dd 不传默认当天
     * startTime 开始时间 格式：HH:mm 不传默认00:00
     * endDate 结束日期 格式：yyyy-MM-dd 不传默认当天
     * endTime 结束时间 格式：HH:mm 不传默认23:59
     * selectById			根据样本编号查询数据
     * requisition_id		外部编码（必传）
     * updateById			申请单状态修改（未使用）
     * uploadById			上传报告数据
     * wjlj			pdf报告文件路径（必传）
     * SampleInfo_INSPECTION_SID		检验单号 此报告在实验室的唯一号 20191108G0039952
     * SampleInfo_GROUP_ID 			分组号 检测小组代号 G063
     * SampleInfo_INSPECTION_CLASS		报告类型 一般检验类、微生物检验类、骨髓检验类、一般检验描述类
     * SampleInfo_REQUISITION_ID		条码号 送检机构标签号码 1027755492
     * SampleInfo_PDF_NAME 			报告单名称 一个送检标本检测机构分多次出报告的报告单名称必须有区别，可直接传检测机构的报告单名称 20191108G0039952
     * ResultInfo_TEST_ITEM_ID			分析项目代码 服务机构分析项目代码 TEST3666
     * ResultInfo_GROUP_ID				分组号 检测小组代号 G063
     * ResultInfo_SAMPLE_NUMBER		检测顺序号 样本号 2437
     * ResultInfo_TEST_ITEM_REFERENCE	参考范围 如：2.00-3.98
     * ResultInfo_TEST_METHOD			检测方法 如：荧光PCR法
     * INSPECTION_INSTRUMENT			检测设备 如：ACCESS2，AU5800无具体设备传无
     * ResultInfo_QUANTITATIVE_RESULT	检测定量结果1.35
     * ResultInfo_TEST_ITEM_UNIT		检测单位ng/ml
     *
     */
    public void xingheInfoNew() {
        Object hospitalIdsObj = map.get("hospitalId");
        Object methodObj = map.get("method");
        if (null != hospitalIdsObj && StringUtil.isNotBlank(hospitalIdsObj.toString())) {
            String hospitalId = hospitalIdsObj.toString();
            String method = "";
            if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
                method = methodObj.toString();
            } else {
                log.error("调用杏和接口6.0------未传入调用方法名");
            }
            try {
                jkdymxDto.setDydz("xingheInfo6.0."+method+"."+hospitalId);
                if ("selectByDate".equals(method)) {
                    Map<String, Object> xingheInfoByDate = generateXingheAreaMsgNew("SelectAreaReqInfo", "RQ", hospitalId, map);
                    dealReturnInfo(xingheInfoByDate, hospitalId,"new");
                } else if ("selectById".equals(method)) {
                    Map<String, Object> xingheInfoById = generateXingheAreaMsgNew("SelectAreaReqInfo", "ID", hospitalId, map);
                    dealReturnInfo(xingheInfoById, hospitalId,"new");
                } else if ("updateById".equals(method)) {
                    log.error("调用杏和接口6.0------该调用方法暂未启用method：" + method);
                    // Map<String, Object> xingheInfoByUpdate = generateXingheAreaMsgNew("UpdateAreaReqState",null,hospitalId,map);
                } else if ("uploadById".equals(method)) {
                    Map<String, Object> xingheInfoByUpload = generateXingheAreaMsgNew("RecvAreaResultInfo", null, hospitalId, map);
                    String resultCode = (String) xingheInfoByUpload.get("ResultCode");
                    if ("-1".equals(resultCode)){
                        sendMessage(null,null,xingheInfoByUpload,"杏和系统6.0-样本-"+map.get("ybbh")+"-医院：" + hospitalId+"，报告回传失败！");
                        reSendRabbit(map,"杏和系统6.0-样本-"+map.get("ybbh")+"-医院：" + hospitalId+"，报告回传失败！");
                    }
                    jkdymxDto.setFhnr(JSON.toJSONString(xingheInfoByUpload).length()>4000?JSON.toJSONString(xingheInfoByUpload).substring(0,4000):JSON.toJSONString(xingheInfoByUpload)); // 返回内容
                    jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                    jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
                    log.error("调用杏和接口6.0------上传报告数据--样本编号：" + map.get("ybbh") + "--上传结果：" + JSON.toJSONString(xingheInfoByUpload));
                } else {
                    log.error("调用杏和接口6.0------调用方法名出错method：" + method);
                }
                jkdymxService.insertJkdymxDto(jkdymxDto);
            } catch (Exception e) {
                String message = e.getMessage();
                log.error("调用杏和接口6.0---失败---" + message);
                if ("uploadById".equals(method)){
                    reSendRabbit(map,"杏和系统6.0-样本-"+map.get("ybbh")+"-医院：" + hospitalId+"，报告回传失败！"+e.getMessage());
                }
                if(message != null && message.length() > 4000){
                    message = message.substring(0,4000);
                }
                jkdymxDto.setFhnr(message); // 返回内容
                jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
                jkdymxService.insertJkdymxDto(jkdymxDto);
            }
        } else {
            log.error("调用杏和接口6.0------未传入hospitalId");
        }
    }

    /**
     * 千麦接口调用方法
     * method 调用方法
     */
    public void qianmaiInfo() {
        Object methodObj = map.get("method");
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            String method = methodObj.toString();
            jkdymxDto.setDydz("qianmaiInfo."+method);
            if ("selectByDate".equals(method) || "selectById".equals(method) || "select".equals(method)) {
                timedCallQianmai(map);
            } else if ("uploadById".equals(method)) {
                returnReport(map);
            }
        } else {
            log.error("千麦：qianmaiInfo-----未传入调用方法名");
        }
    }

    /**
     * 禾诺接口调用方法
     * method 调用方法
     */
    public void henuoInfo() {
        Object methodObj = map.get("method");
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            String method = methodObj.toString();
            jkdymxDto.setDydz("henuoInfo."+method);
            if ("selectByDate".equals(method) || "selectById".equals(method) || "select".equals(method)) {
                timedCallHenuoNew(map);
            } else if ("itemList".equals(method)) {
                itemList("LIMSDXP");
            } else if ("uploadById".equals(method)){
                returnHenuoReport(map);
            }else {
                log.error("禾诺：获取样本信息成功-----未传入调用方法名");
            }
            jkdymxService.insertJkdymxDto(jkdymxDto);
        } else {
            log.error("禾诺：获取样本信息成功-----未传入调用方法名");
        }
    }
    //-------------------------------------------------------------------------------------------------//
    //-----------------------------------------以下为杏和接口方法-----------------------------------------//
    //-------------------------------------------------------------------------------------------------//

    /**
     * 生成并发送杏和xml
     *
     * @param methodCode 方法名称
     * @param queryMode  查询方式 非查询NONE,根据id查询 ID,根据日期查询 RQ
     * @param hospitalId 医院ID
     * @param infoMap    数据存放map
     * @return resultXmlMsg 杏和返回数据
     * @throws Exception xml请求异常信息
     */
    public Map<String, Object> generateXingheAreaMsg(String methodCode, String queryMode, String hospitalId, Map<String, Object> infoMap) throws Exception {
        String nameSpace = "http://xhlis.org/";
        String serviceName = "XingHeAreaService";
        String portName = "XingHeAreaServiceSoap12";
        String elementName = "CallAreaInterface";
        String responseName = "CallAreaInterfaceResponse";

        //杏和配置信息：wsdlurl
        String xinghe_wsdlurl = map.get("xinghe_wsdlurl").toString();
        //杏和配置信息：ticket
        String xinghe_ticket = map.get("xinghe_ticket").toString();
        //杏和配置信息：hospitalservice
        String xinghe_hospitalservice = map.get("xinghe_hospitalservice").toString();
        // 1.create service
        URL url = new URL(xinghe_wsdlurl);
        QName sname = new QName(nameSpace, serviceName, "xhl");
        javax.xml.ws.Service service = javax.xml.ws.Service.create(url, sname);
        service.addPort(new QName(nameSpace, "XingHeAreaServiceSoap12"), "http://www.w3.org/2003/05/soap/bindings/HTTP/", url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+url.getPath());
        service.addPort(new QName(nameSpace, "XingHeAreaServiceSoap"), "http://schemas.xmlsoap.org/wsdl/soap/http", url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+url.getPath());
        // 2.create Dispatch object
        Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, javax.xml.ws.Service.Mode.MESSAGE);

        // 3.create SOAPMessage
        SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
        msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

        // 3.1 create SOAPEnvelope
        SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
        envelope.setPrefix("soap");
        envelope.addNamespaceDeclaration("xhl", nameSpace);
        envelope.removeAttribute("xmlns:env");

        // 3.2 create SOAPHeader
        SOAPHeader header = envelope.getHeader();
        if (null == header) {
            header = envelope.addHeader();
        }
        header.setPrefix("soap");

        // 3.3 create SOAPBody
        SOAPBody body = envelope.getBody();
        body.setPrefix("soap");
        SOAPBodyElement callAreaInterfaceBody = body.addBodyElement(new QName(nameSpace, elementName, "xhl"));
        SOAPElement headElement = callAreaInterfaceBody.addChildElement(new QName(nameSpace, "headXml", "xhl"));
        SOAPElement bodyElement = callAreaInterfaceBody.addChildElement(new QName(nameSpace, "bodyXml", "xhl"));
        SOAPElement pdfFileDataElement = callAreaInterfaceBody.addChildElement(new QName(nameSpace, "pdfFileData", "xhl"));
        //生成headXml，并拼接
        generateHeadXml(headElement, methodCode, xinghe_ticket);
        //获取申请单信息(仅套餐项目)、获取申请单信息(含分析项目)
        if ("SelectAreaReqInfo".equals(methodCode) || "SelectAreaSampleInfo".equals(methodCode)) {
            log.error("调用杏和接口--获取申请单信息------医院ID:" + hospitalId + "--调用方法:" + methodCode);
            if ("RQ".equals(queryMode)) {
                String headXml = generateSelectDateAreaInfoXml(infoMap, hospitalId, xinghe_hospitalservice,0);
                bodyElement.setValue(headXml);
            } else if ("ID".equals(queryMode)) {
                String headXml = generateSelectSingleAreaInfoXml(infoMap, hospitalId, xinghe_hospitalservice,0);
                bodyElement.setValue(headXml);
            } else {
                throw new RuntimeException("获取申请单信息" + hospitalId + "：非查询,queryMode:" + queryMode);
            }
            //接口调用明细 -- 设置请求内容
            jkdymxDto.setNr(JSON.toJSONString(JSON.toJSONString(bodyElement)));
        } else if ("UpdateAreaReqState".equals(methodCode)) {
            //申请单状态修改
            log.error("调用杏和接口--申请单状态修改------医院ID:" + hospitalId + "--调用方法:UpdateAreaReqState");

        } else if ("RecvAreaResultInfo".equals(methodCode)) {
            log.error("调用杏和接口--数据回传杏和------医院ID:" + hospitalId + "--样本编号:" + infoMap.get("ybbh"));
            //结果回传数据中心
            Map<String, Object> bodyMap = dealBodyMap(infoMap);
            String bodyxml = generateBodyXml(bodyMap, hospitalId, xinghe_hospitalservice,0);
            bodyElement.setValue(bodyxml);
            String pdfMsg = getPdfMsg(infoMap.get("wjlj").toString());
            pdfFileDataElement.setValue(pdfMsg);
            //将msg存储到文件中
            String filePath = mkDirs(BusTypeEnum.IMP_REPORT_XINGHE_XML.getCode(), "/matridx/fileupload/temp/");
            String fileName = "xinghe_" + hospitalId + "_" + infoMap.get("ybbh") + "_" + System.currentTimeMillis() + ".xml";
            //将msg存储到文件中
            msg.writeTo(new FileOutputStream(filePath + "/" + fileName));
        }
        // set timeout
        SOAPMessage response = dispatch.invoke(msg);

        // 4. get response and transfer to dom object
        org.w3c.dom.Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
        String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
        return getResultXmlMsg(result);
    }

    /**
     * 杏和区域接口 生成调用方法头HeadXml
     *
     * @param methodCode 方法名称
     * @param ticket     调用凭证
     */
    public void generateHeadXml(SOAPElement headElement, String methodCode, String ticket) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Root");
        Element MethodCode = root.addElement("MethodCode");
        Element Ticket = root.addElement("Ticket");
        MethodCode.setText(methodCode);
        Ticket.setText(ticket);
        String headXml = "<![CDATA[" + root.asXML() + "]]>";
        headElement.setValue(headXml);
    }

    /**
     * 杏和区域接口 生成调用方法 查询单个
     *
     * @param infoMap          数据存放map
     * @param HOSPITAL_ID      医院ID
     * @param HOSPITAL_SERVICE 检测机构ID
     */
    public String generateSelectSingleAreaInfoXml(Map<String, Object> infoMap, String HOSPITAL_ID, String HOSPITAL_SERVICE,int transformFlg) {
        Document document = DocumentHelper.createDocument();
        Element data = document.addElement("DATA");
        Element item = data.addElement("ITEM");
        item.addAttribute("HOSPITAL_ID", HOSPITAL_ID);
        item.addAttribute("HOSPITAL_SERVICE", HOSPITAL_SERVICE);
        if (null != infoMap.get("requisition_id")) {
            log.error("调用杏和接口--获取申请单信息------医院ID:" + HOSPITAL_ID + "--根据ID查询:" + infoMap.get("requisition_id").toString());
            item.addAttribute("REQUISITION_ID", infoMap.get("requisition_id").toString());
        } else {
            throw new RuntimeException("调用杏和接口--获取申请单信息------医院ID:" + HOSPITAL_ID + "未传入requisition_id");
        }
        if(transformFlg==1){
            return data.asXML();
        }else{
            return "<![CDATA[" + data.asXML() + "]]>";
        }
    }

    /**
     * 杏和区域接口 生成调用方法 查询日期范围
     *
     * @param infoMap          数据存放map
     * @param HOSPITAL_ID      医院ID
     * @param HOSPITAL_SERVICE 检测机构ID
     */
    public String generateSelectDateAreaInfoXml(Map<String, Object> infoMap, String HOSPITAL_ID, String HOSPITAL_SERVICE,int transformFlg) {
        Document document = DocumentHelper.createDocument();
        Element data = document.addElement("DATA");
        Element item = data.addElement("ITEM");
        item.addAttribute("HOSPITAL_ID", HOSPITAL_ID);
        item.addAttribute("HOSPITAL_SERVICE", HOSPITAL_SERVICE);
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //默认开始日期为当天日期
        String startDate = nowDate;
        //默认结束日期为当天日期
        String endDate = nowDate;
        //默认开始时间为00:00
        String startTime = "00:00";
        //默认结束时间为23:59
        String endTime = "23:59";
        if (null != infoMap.get("startDate")) {
            startDate = infoMap.get("startDate").toString();
        }
        if (null != infoMap.get("endDate")) {
            endDate = infoMap.get("endDate").toString();
        }
        if (null != infoMap.get("startTime")) {
            startTime = infoMap.get("startTime").toString();
        }
        if (null != infoMap.get("endTime")) {
            endTime = infoMap.get("endTime").toString();
        }
        log.error("调用杏和接口--获取申请单信息------医院ID:" + HOSPITAL_ID + "--根据日期查询:" + startDate + " " + startTime + " ~ " + endDate + " " + endTime);
        item.addAttribute("DATA_FROM", startDate);
        item.addAttribute("DATA_TO", endDate);
        item.addAttribute("START_TIME", startTime);
        item.addAttribute("STOP_TIME", endTime);
        if(transformFlg==1){
            return data.asXML();
        }else{
            return "<![CDATA[" + data.asXML() + "]]>";
        }
    }

    /**
     * 杏和区域接口 生成调用方法 查询日期范围
     *
     * @param infoMap          数据存放map
     * @param HOSPITAL_ID      医院ID
     * @param HOSPITAL_SERVICE 检测机构ID
     */
    public String generateSelectDateAreaInfoXmlNew(Map<String, Object> infoMap, String HOSPITAL_ID, String HOSPITAL_SERVICE) {
        Document document = DocumentHelper.createDocument();
        Element data = document.addElement("DATA");
        Element item = data.addElement("ITEM");
        item.addAttribute("HOSPITAL_ID", HOSPITAL_ID);
        item.addAttribute("HOSPITAL_SERVICE", HOSPITAL_SERVICE);
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //默认开始日期为当天日期
        String startDate = nowDate;
        //默认结束日期为当天日期
        String endDate = nowDate;
        //默认开始时间为00:00
        String startTime = "00:00";
        //默认结束时间为23:59
        String endTime = "23:59";
        if (null != infoMap.get("startDate")) {
            startDate = infoMap.get("startDate").toString();
        }
        if (null != infoMap.get("endDate")) {
            endDate = infoMap.get("endDate").toString();
        }
        if (null != infoMap.get("startTime")) {
            startTime = infoMap.get("startTime").toString();
        }
        if (null != infoMap.get("endTime")) {
            endTime = infoMap.get("endTime").toString();
        }
        log.error("调用杏和接口--获取申请单信息------医院ID:" + HOSPITAL_ID + "--根据日期查询:" + startDate + " " + startTime + " ~ " + endDate + " " + endTime);
        item.addAttribute("DATA_FROM", startDate);
        item.addAttribute("DATA_TO", endDate);
        item.addAttribute("START_TIME", startTime);
        item.addAttribute("STOP_TIME", endTime);
        String headXml = data.asXML();
        return headXml;
    }

    /**
     * 杏和区域接口 生成调用方法体BodyXml
     *
     * @param bodyMap          数据存放map
     * @param HOSPITAL_ID      医院ID
     * @param HOSPITAL_SERVICE 检测机构ID
     * @throws BusinessException xml处理异常信息
     */
    public String generateBodyXml(Map<String, Object> bodyMap, String HOSPITAL_ID, String HOSPITAL_SERVICE,int transformFlg) throws BusinessException {
        Document document = DocumentHelper.createDocument();
        Element request = document.addElement("Request");
        Element sampleInfo = request.addElement("SampleInfo");
        Element resultInfo = request.addElement("ResultInfo");
        Element explainInfo = request.addElement("explainInfo");
        //组装标本主体信息SampleInfo
        generateSampleInfo(sampleInfo, bodyMap, HOSPITAL_ID, HOSPITAL_SERVICE);
        //组装标本结果信息ResultInfo
        generateResultInfo(resultInfo, bodyMap, HOSPITAL_ID);
        //组装诊断建议信息explainInfo
        generateExplainInfo(explainInfo, bodyMap);

        if(transformFlg==1){
            return request.asXML();
        }else{
            return "<![CDATA[" + request.asXML() + "]]>";
        }
    }

    /**
     * 杏和区域接口 处理送检信息
     *
     * @param infoMap 数据存放map
     * @return Map<String, Object>
     */
    public Map<String, Object> dealBodyMap(Map<String, Object> infoMap) {
        Map<String, Object> bodyMap = new HashMap<>();
        // 处理必填项
        // 标本主体信息SampleInfo
        if (infoMap.get("ybbh") != null && infoMap.get("ybbh").toString().contains("G")){
            bodyMap.put("SampleInfo_INSPECTION_SID", infoMap.get("ybbh"));                //检验单号 此报告在实验室的唯一号 20220728G0320001				    原为ybbh
            bodyMap.put("SampleInfo_REQUISITION_ID", infoMap.get("wbbm"));                //条码号 送检机构标签号码 0913841700								原为wbbm
        } else {
            bodyMap.put("SampleInfo_INSPECTION_SID", infoMap.get("wbbm"));                //检验单号 此报告在实验室的唯一号 20220728G0320001					定为wbbm
            bodyMap.put("SampleInfo_REQUISITION_ID", infoMap.get("ybbh"));                //条码号 送检机构标签号码 0913841700								定为ybbh
        }
//        bodyMap.put("SampleInfo_INSPECTION_SID", infoMap.get("wbbm"));                //检验单号 此报告在实验室的唯一号 20220728G0320001					定为wbbm
        String cskz6 = (String) infoMap.get("cskz6");
        String groupId = "";
        String sampleNumber = "";
        if (StringUtil.isNotBlank(cskz6)) {
            String[] cskz6s = cskz6.split(",");
            groupId = cskz6s[0];
            sampleNumber = cskz6s[1];
        }
        bodyMap.put("SampleInfo_GROUP_ID", groupId);                                    //分组号 检测小组代号
        bodyMap.put("SampleInfo_INSPECTION_CLASS", "一般检验类");                        //报告类型 一般检验类、微生物检验类、骨髓检验类、一般检验描述类			固定值 一般检验类
//        bodyMap.put("SampleInfo_REQUISITION_ID", infoMap.get("ybbh"));                //条码号 送检机构标签号码 0913841700								定为ybbh
        bodyMap.put("SampleInfo_PDF_NAME", infoMap.get("fwjm"));                        //报告单名称，一个送检标本检测机构分多次出报告的报告单名称必须有区别		暂定fwjm
        // 标本结果信息ResultInfo
        bodyMap.put("ResultInfo_TEST_ITEM_ID", infoMap.get("testTeamId"));                        //分析项目代码 服务机构分析项目代码
        bodyMap.put("ResultInfo_GROUP_ID", groupId);                                    //分组号 检测小组代号
        bodyMap.put("ResultInfo_SAMPLE_NUMBER", sampleNumber);                        //检测顺序号 样本号
        bodyMap.put("ResultInfo_TEST_ITEM_REFERENCE", "无");                            //参考范围 如：2.00-3.98
        bodyMap.put("ResultInfo_TEST_METHOD", "荧光PCR法");                            //检测方法 如：荧光PCR法
        bodyMap.put("ResultInfo_INSTRUMENT", "无");                                    //检测设备 如：ACCESS2，AU5800无具体设备传无
        bodyMap.put("ResultInfo_QUANTITATIVE_RESULT", "见报告单");                    //检测定量结果
        bodyMap.put("ResultInfo_TEST_ITEM_UNIT", "见报告单");                            //检测单位
        // SampleInfo
        // bodyMap.put("SampleInfo_INSPECTION_DATE","20191012");//检测日期 YYYYMMDD
        // bodyMap.put("SampleInfo_INSPECTION_TIME","121856");//检测时间 HH24MISS
        bodyMap.put("SampleInfo_SAMPLE_NUMBER", sampleNumber);                        //检测顺序号 样本号
        // bodyMap.put("SampleInfo_PATIENT_TYPE","1");//病人类别 1住院2门诊3住院急诊4门诊急诊5体检7其他
        // bodyMap.put("SampleInfo_PATIENT_ID","1842");//病人主索引号
        // bodyMap.put("SampleInfo_OUTPATIENT_ID","7151");//病人ID号
        // bodyMap.put("SampleInfo_INPATIENT_ID","1842");//病人就诊流水号
        // bodyMap.put("SampleInfo_CHARGE_TYPE","");//收费类别 医保、自费。。。
        bodyMap.put("SampleInfo_PATIENT_NAME", infoMap.get("hzxm"));        //姓名
        bodyMap.put("SampleInfo_PATIENT_SEX", infoMap.get("xb"));            //性别 1男2女3不详
        bodyMap.put("SampleInfo_AGE_INPUT", infoMap.get("nl"));                //年龄
        // bodyMap.put("SampleInfo_PATIENT_BIRTHDAY","");//出生日期
        // bodyMap.put("SampleInfo_ID_CARD","");//身份证号
        // bodyMap.put("SampleInfo_PATIENT_NATION","");//民族代码
        // bodyMap.put("SampleInfo_PATIENT_NATION_NAME","");//民族名称
        // bodyMap.put("SampleInfo_BLOODTYPE_ABO","");//ABO血型
        // bodyMap.put("SampleInfo_BLOODTYPE_RH","");//RH血型
        // bodyMap.put("SampleInfo_PATIENT_DEPT","LIS1101");//科别代码
        // bodyMap.put("SampleInfo_PATIENT_DEPT_NAME",">内科（住院）");//科别名称
        // bodyMap.put("SampleInfo_PATIENT_WARD","LIS1002");//病区代码
        // bodyMap.put("SampleInfo_PATIENT_WARD_NAME","住院病区");//病区名称
        // bodyMap.put("SampleInfo_PATIENT_BED","105");//床位号
        // bodyMap.put("SampleInfo_ESPECIAL_CONDITION","");//特殊条件
        // bodyMap.put("SampleInfo_CLINICAL_DIAGNOSES","泌尿道感染");//诊断代码
        // bodyMap.put("SampleInfo_CLINICAL_DIAGNOSES_NAME","泌尿道感染");//诊断名称
        // bodyMap.put("SampleInfo_SAMPLE_CLASS","");//标本种类代码
        bodyMap.put("SampleInfo_SAMPLE_CLASS_NAME", infoMap.get("yblxmc"));    //标本种类名称 如：血清、全血
        // bodyMap.put("SampleInfo_INFECT_STATUS","");//传染病标记
        // bodyMap.put("SampleInfo_SAMPLE_STATUS","");//标本性状代码
        // bodyMap.put("SampleInfo_SAMPLE_STATUS_NAME","");//标本性状名称 如：溶血，脂血
        // bodyMap.put("SampleInfo_SAMPLING_POSITION","");//采集部位代码
        // bodyMap.put("SampleInfo_SAMPLING_POSITION_NAME","");//采集部位名称 如：左手臂
        // bodyMap.put("SampleInfo_TEST_ORDER","LIS9127+LIS9130");//检测项目代码
        // bodyMap.put("SampleInfo_TEST_ORDER_NAME","FPSA+TPSA");//检测项目名称 大项，如肝功、肾功
        // bodyMap.put("SampleInfo_SAMPLE_CHARGE","130");//收费金额
        // bodyMap.put("SampleInfo_SAMPLING_TIME","");//采集时间
        // bodyMap.put("SampleInfo_SAMPLING_PERSON","沙家浜管理员");//采集人员
        // bodyMap.put("SampleInfo_INCEPT_TIME","");//接收时间
        // bodyMap.put("SampleInfo_INCEPT_PERSON","自动分拣2");//接收人员
        // bodyMap.put("SampleInfo_INPUT_TIME","");//录入时间
        // bodyMap.put("SampleInfo_INPUT_PERSON","陈雨婷");//录入人员
        // bodyMap.put("SampleInfo_REQUISITION_TIME","");//申请时间
        // bodyMap.put("SampleInfo_REQUISITION_PERSON","LIS1095_马耀生");//申请人员
        // bodyMap.put("SampleInfo_DELIVER_HOSPITAL","沙家浜卫生院");//送检医院的标本来源
        bodyMap.put("SampleInfo_INSPECTION_PERSON", infoMap.get("jyry"));//检测人员
        // bodyMap.put("SampleInfo_INSTRUMENT","DM2");//检测仪器 多个仪器用,分隔如：AU5800,ACCESS2
        bodyMap.put("SampleInfo_CHECK_PERSON", infoMap.get("shry"));//报告审核人员
        bodyMap.put("SampleInfo_CHECK_TIME", infoMap.get("shrq"));//报告审核时间
        // bodyMap.put("SampleInfo_CHARGE_STATE","charged");//收费状态
        // bodyMap.put("SampleInfo_INSPECTION_STATE","audited");//检验单状态 默认audited
//		bodyMap.put("SampleInfo_REMARK_NAME",(String) infoMap.get("bz"));//标本备注
        //ResultInfo
        // bodyMap.put("ResultInfo_QUALITATIVE_RESULT","z");//结果判断 正常z偏高h偏低l报警偏高ah报警偏低al危急值偏高ch危急值偏低cl，其他直接传
        // bodyMap.put("ResultInfo_INSPECTION_DATE","20191012");//检测日期 YYYYMMDD
        // bodyMap.put("ResultInfo_INSPECTION_TIME","121856");//检测时间 HH24MISS
        // bodyMap.put("ResultInfo_INSPECTION_INSTRUMENT","DM2");//检测设备 如：ACCESS2，AU5800无具体设备传无
        // bodyMap.put("ResultInfo_ORIGINAL_RESULT","1.35");//仪器原始结果
        // bodyMap.put("ResultInfo_TEST_ITEM_SORT","MY013");//排序号
        // bodyMap.put("ResultInfo_ENGLISH_NAME","FPSA");//英文名称
        // bodyMap.put("ResultInfo_CHINESE_NAME","游离PSA");//中文名称
        // bodyMap.put("ResultInfo_PRINT_ID","1");//打印顺序
        // bodyMap.put("ResultInfo_col_position","1");//报告单的列的位置 多列的情况，默认1
        // bodyMap.put("ResultInfo_report_id","1");//报告单ID号 多张报告单的情况，默认1
        //explainInfo
         bodyMap.put("ExplainInfo_DIAGNOSIS_ADVICE",infoMap.get("json")!=null?infoMap.get("json").toString():"");//诊断建议
        // bodyMap.put("ExplainInfo_RESULT_ANALYZE","本检测用于新生儿先天性肾上皮质增生症（CAH）筛查和诊断检测");//结果分析
        return bodyMap;
    }

    /**
     * 杏和区域接口 组装标本主体信息SampleInfo
     *
     * @param sampleInfo       调用方法body
     * @param bodyMap          数据存放map
     * @param HOSPITAL_ID      医院ID
     * @param HOSPITAL_SERVICE 检测机构ID
     * @throws BusinessException xml处理异常信息
     */
    public void generateSampleInfo(Element sampleInfo, Map<String, Object> bodyMap, String HOSPITAL_ID, String HOSPITAL_SERVICE) throws BusinessException {
        // 拼接xml
        Element sampleInfo_INSPECTION_SID = sampleInfo.addElement("INSPECTION_SID");//1.检验单号 此报告在实验室的唯一号
        Element sampleInfo_GROUP_ID = sampleInfo.addElement("GROUP_ID");//2.分组号 检测小组代号
        Element sampleInfo_HOSPITAL_ID = sampleInfo.addElement("HOSPITAL_ID");//3.送检机构代码 送检医院的机构代码
        Element sampleInfo_HOSPITAL_SERVICE = sampleInfo.addElement("HOSPITAL_SERVICE");//4.服务机构代码 检测实验室的机构代码
        Element sampleInfo_INSPECTION_CLASS = sampleInfo.addElement("INSPECTION_CLASS");//5.报告类型 一般检验类、微生物检验类、骨髓检验类、一般检验描述类
        Element sampleInfo_INSPECTION_DATE = sampleInfo.addElement("INSPECTION_DATE");//6.检测日期 YYYYMMDD
        Element sampleInfo_INSPECTION_TIME = sampleInfo.addElement("INSPECTION_TIME");//7.检测时间 HH24MISS
        Element sampleInfo_SAMPLE_NUMBER = sampleInfo.addElement("SAMPLE_NUMBER");//8.检测顺序号 样本号
        Element sampleInfo_REQUISITION_ID = sampleInfo.addElement("REQUISITION_ID");//9.条码号 送检机构标签号码
        Element sampleInfo_PATIENT_TYPE = sampleInfo.addElement("PATIENT_TYPE");//10.病人类别 1住院2门诊3住院急诊4门诊急诊5体检7其他
        Element sampleInfo_PATIENT_ID = sampleInfo.addElement("PATIENT_ID");//11.病人主索引号
        Element sampleInfo_OUTPATIENT_ID = sampleInfo.addElement("OUTPATIENT_ID");//12.病人ID号
        Element sampleInfo_INPATIENT_ID = sampleInfo.addElement("INPATIENT_ID");//13.病人就诊流水号
        Element sampleInfo_CHARGE_TYPE = sampleInfo.addElement("CHARGE_TYPE");//14.收费类别 医保、自费。。。
        Element sampleInfo_PATIENT_NAME = sampleInfo.addElement("PATIENT_NAME");//15.姓名
        Element sampleInfo_PATIENT_SEX = sampleInfo.addElement("PATIENT_SEX");//16.性别 1男2女3不详
        Element sampleInfo_AGE_INPUT = sampleInfo.addElement("AGE_INPUT");//17.年龄
        Element sampleInfo_PATIENT_BIRTHDAY = sampleInfo.addElement("PATIENT_BIRTHDAY");//18.出生日期
        Element sampleInfo_ID_CARD = sampleInfo.addElement("ID_CARD");//19.身份证号
        Element sampleInfo_PATIENT_NATION = sampleInfo.addElement("PATIENT_NATION");//20.民族代码
        Element sampleInfo_PATIENT_NATION_NAME = sampleInfo.addElement("PATIENT_NATION_NAME");//21.民族名称
        Element sampleInfo_BLOODTYPE_ABO = sampleInfo.addElement("BLOODTYPE_ABO");//22.ABO血型
        Element sampleInfo_BLOODTYPE_RH = sampleInfo.addElement("BLOODTYPE_RH");//23.RH血型
        Element sampleInfo_PATIENT_DEPT = sampleInfo.addElement("PATIENT_DEPT");//24.科别代码
        Element sampleInfo_PATIENT_DEPT_NAME = sampleInfo.addElement("PATIENT_DEPT_NAME");//25.科别名称
        Element sampleInfo_PATIENT_WARD = sampleInfo.addElement("PATIENT_WARD");//26.病区代码
        Element sampleInfo_PATIENT_WARD_NAME = sampleInfo.addElement("PATIENT_WARD_NAME");//27.病区名称
        Element sampleInfo_PATIENT_BED = sampleInfo.addElement("PATIENT_BED");//28.床位号
        Element sampleInfo_ESPECIAL_CONDITION = sampleInfo.addElement("ESPECIAL_CONDITION");//29.特殊条件
        Element sampleInfo_CLINICAL_DIAGNOSES = sampleInfo.addElement("CLINICAL_DIAGNOSES");//30.诊断代码
        Element sampleInfo_CLINICAL_DIAGNOSES_NAME = sampleInfo.addElement("CLINICAL_DIAGNOSES_NAME");//31.诊断名称
        Element sampleInfo_SAMPLE_CLASS = sampleInfo.addElement("SAMPLE_CLASS");//32.标本种类代码
        Element sampleInfo_SAMPLE_CLASS_NAME = sampleInfo.addElement("SAMPLE_CLASS_NAME");//33.标本种类名称 如：血清、全血
        Element sampleInfo_INFECT_STATUS = sampleInfo.addElement("INFECT_STATUS");//34.传染病标记
        Element sampleInfo_SAMPLE_STATUS = sampleInfo.addElement("SAMPLE_STATUS");//35.标本性状代码
        Element sampleInfo_SAMPLE_STATUS_NAME = sampleInfo.addElement("SAMPLE_STATUS_NAME");//36.标本性状名称 如：溶血，脂血
        Element sampleInfo_SAMPLING_POSITION = sampleInfo.addElement("SAMPLING_POSITION");//37.采集部位代码
        Element sampleInfo_SAMPLING_POSITION_NAME = sampleInfo.addElement("SAMPLING_POSITION_NAME");//38.采集部位名称 如：左手臂
        Element sampleInfo_TEST_ORDER = sampleInfo.addElement("TEST_ORDER");//39.检测项目代码
        Element sampleInfo_TEST_ORDER_NAME = sampleInfo.addElement("TEST_ORDER_NAME");//40.检测项目名称 大项，如肝功、肾功
        Element sampleInfo_SAMPLE_CHARGE = sampleInfo.addElement("SAMPLE_CHARGE");//41.收费金额
        Element sampleInfo_SAMPLING_TIME = sampleInfo.addElement("SAMPLING_TIME");//42.采集时间
        Element sampleInfo_SAMPLING_PERSON = sampleInfo.addElement("SAMPLING_PERSON");//43.采集人员
        Element sampleInfo_INCEPT_TIME = sampleInfo.addElement("INCEPT_TIME");//44.接收时间
        Element sampleInfo_INCEPT_PERSON = sampleInfo.addElement("INCEPT_PERSON");//45.接收人员
        Element sampleInfo_INPUT_TIME = sampleInfo.addElement("INPUT_TIME");//46.录入时间
        Element sampleInfo_INPUT_PERSON = sampleInfo.addElement("INPUT_PERSON");//47.录入人员
        Element sampleInfo_REQUISITION_TIME = sampleInfo.addElement("REQUISITION_TIME");//48.申请时间
        Element sampleInfo_REQUISITION_PERSON = sampleInfo.addElement("REQUISITION_PERSON");//49.申请人员
        Element sampleInfo_DELIVER_HOSPITAL = sampleInfo.addElement("DELIVER_HOSPITAL");//50.送检医院的标本来源
        Element sampleInfo_INSPECTION_PERSON = sampleInfo.addElement("INSPECTION_PERSON");//51.检测人员
        Element sampleInfo_INSTRUMENT = sampleInfo.addElement("INSTRUMENT");//52.检测仪器 多个仪器用,分隔如：AU5800,ACCESS2
        Element sampleInfo_CHECK_PERSON = sampleInfo.addElement("CHECK_PERSON");//53.报告审核人员
        Element sampleInfo_CHECK_TIME = sampleInfo.addElement("CHECK_TIME");//54.报告审核时间
        Element sampleInfo_CHARGE_STATE = sampleInfo.addElement("CHARGE_STATE");//55.收费状态
        Element sampleInfo_INSPECTION_STATE = sampleInfo.addElement("INSPECTION_STATE");//56.检验单状态 默认audited
        Element sampleInfo_PDF_NAME = sampleInfo.addElement("PDF_NAME");//57.报告单名称 报告单名称，例如：20191108G0039952  一个送检标本检测机构分多次出报告的报告单名称必须有区别，可直接传检测机构的报告单名称
        Element sampleInfo_REMARK_NAME = sampleInfo.addElement("REMARK_NAME");//58.标本备注
        // 标本主体信息SampleInfo

        // 必填项处理
        // HOSPITAL_ID 送检机构代码 送检医院的机构代码
        sampleInfo_HOSPITAL_ID.setText(HOSPITAL_ID);
        // HOSPITAL_SERVICE 服务机构代码 检测实验室的机构代码
        sampleInfo_HOSPITAL_SERVICE.setText(HOSPITAL_SERVICE);
        // INSPECTION_SID 检验单号 此报告在实验室的唯一号 例：20191108G0039952
        Object sampleInfo_inspection_sid = bodyMap.get("SampleInfo_INSPECTION_SID");
        if (sampleInfo_inspection_sid != null && StringUtil.isNotBlank(sampleInfo_inspection_sid.toString())) {
            sampleInfo_INSPECTION_SID.setText(sampleInfo_inspection_sid.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本主体信息SampleInfo中的必填项：检验单号 INSPECTION_SID 为空！");
        }
        // GROUP_ID 分组号 检测小组代号 例：G063
        Object sampleInfo_group_id = bodyMap.get("SampleInfo_GROUP_ID");
        if (sampleInfo_group_id != null && StringUtil.isNotBlank(sampleInfo_group_id.toString())) {
            sampleInfo_GROUP_ID.setText(sampleInfo_group_id.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本主体信息SampleInfo中的必填项：分组号 GROUP_ID 为空！");
        }
        // INSPECTION_CLASS 告类型 一般检验类、微生物检验类、骨髓检验类、一般检验描述类
        Object sampleInfo_inspection_class = bodyMap.get("SampleInfo_INSPECTION_CLASS");
        if (sampleInfo_inspection_class != null && StringUtil.isNotBlank(sampleInfo_inspection_class.toString())) {
            sampleInfo_INSPECTION_CLASS.setText(sampleInfo_inspection_class.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本主体信息SampleInfo中的必填项：报告类型 INSPECTION_CLASS 为空！");
        }
        // REQUISITION_ID 条码号 送检机构标签号码
        Object sampleInfo_requisition_id = bodyMap.get("SampleInfo_REQUISITION_ID");
        if (sampleInfo_requisition_id != null && StringUtil.isNotBlank(sampleInfo_requisition_id.toString())) {
            sampleInfo_REQUISITION_ID.setText(sampleInfo_requisition_id.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本主体信息SampleInfo中的必填项：条码号 REQUISITION_ID 为空！");
        }
        // PDF_NAME 报告单名称 报告单名称，例如：20191108G0039952  一个送检标本检测机构分多次出报告的报告单名称必须有区别，可直接传检测机构的报告单名称
        Object sampleInfo_pdf_name = bodyMap.get("SampleInfo_PDF_NAME");
        if (sampleInfo_pdf_name != null && StringUtil.isNotBlank(sampleInfo_pdf_name.toString())) {
            sampleInfo_PDF_NAME.setText(sampleInfo_pdf_name.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本主体信息SampleInfo中的必填项：报告单名称 PDF_NAME 为空！");
        }
        //检测人员
        Object sampleInfo_inspection_person = bodyMap.get("SampleInfo_INSPECTION_PERSON");
        if (sampleInfo_inspection_person != null && StringUtil.isNotBlank(sampleInfo_inspection_person.toString())) {
            sampleInfo_INSPECTION_PERSON.setText(sampleInfo_inspection_person.toString());
        }
        // 报告审核人员
        Object sampleInfo_check_person = bodyMap.get("SampleInfo_CHECK_PERSON");
        if (sampleInfo_check_person != null && StringUtil.isNotBlank(sampleInfo_check_person.toString())) {
            sampleInfo_CHECK_PERSON.setText(sampleInfo_check_person.toString());
        }
        // 报告审核时间
        Object sampleInfo_check_time = bodyMap.get("SampleInfo_CHECK_TIME");
        if (sampleInfo_check_time != null && StringUtil.isNotBlank(sampleInfo_check_time.toString())) {
            sampleInfo_CHECK_TIME.setText(sampleInfo_check_time.toString());
        }
    }

    /**
     * 杏和区域接口 组装标本结果信息ResultInfo
     *
     * @param resultInfo  调用方法body
     * @param bodyMap     数据存放map
     * @param HOSPITAL_ID 医院ID
     * @throws BusinessException xml处理异常信息
     */
    public void generateResultInfo(Element resultInfo, Map<String, Object> bodyMap, String HOSPITAL_ID) throws BusinessException {
        // 标本结果信息ResultInfo
        Element resultInfo_TEST_ITEM_ID = resultInfo.addElement("TEST_ITEM_ID");//1.分析项目代码 服务机构分析项目代码
        Element resultInfo_GROUP_ID = resultInfo.addElement("GROUP_ID");//2.分组号 检测小组代号
        Element resultInfo_QUALITATIVE_RESULT = resultInfo.addElement("QUALITATIVE_RESULT");//3.结果判断 正常z偏高h偏低l报警偏高ah报警偏低al危急值偏高ch危急值偏低cl，其他直接传
        Element resultInfo_INSPECTION_DATE = resultInfo.addElement("INSPECTION_DATE");//4.检测日期 YYYYMMDD
        Element resultInfo_INSPECTION_TIME = resultInfo.addElement("INSPECTION_TIME");//5.检测时间 HH24MISS
        Element resultInfo_SAMPLE_NUMBER = resultInfo.addElement("SAMPLE_NUMBER");//6.检测顺序号 样本号
        Element resultInfo_TEST_ITEM_REFERENCE = resultInfo.addElement("TEST_ITEM_REFERENCE");//7.参考范围 如：2.00-3.98
        Element resultInfo_TEST_METHOD = resultInfo.addElement("TEST_METHOD");//8.检测方法 如：荧光PCR法
        Element resultInfo_INSPECTION_INSTRUMENT = resultInfo.addElement("INSPECTION_INSTRUMENT");//9.检测设备 如：ACCESS2，AU5800无具体设备传无
        Element resultInfo_ORIGINAL_RESULT = resultInfo.addElement("ORIGINAL_RESULT");//10.仪器原始结果
        Element resultInfo_TEST_ITEM_SORT = resultInfo.addElement("TEST_ITEM_SORT");//11.排序号
        Element resultInfo_ENGLISH_NAME = resultInfo.addElement("ENGLISH_NAME");//12.英文名称
        Element resultInfo_CHINESE_NAME = resultInfo.addElement("CHINESE_NAME");//13.中文名称
        Element resultInfo_QUANTITATIVE_RESULT = resultInfo.addElement("QUANTITATIVE_RESULT");//14.检测定量结果
        Element resultInfo_TEST_ITEM_UNIT = resultInfo.addElement("TEST_ITEM_UNIT");//15.检测单位
        Element resultInfo_PRINT_ID = resultInfo.addElement("PRINT_ID");//16.打印顺序
        Element resultInfo_COL_POSITION = resultInfo.addElement("COL_POSITION");//17.报告单的列的位置 多列的情况，默认1
        Element resultInfo_REPORT_ID = resultInfo.addElement("REPORT_ID");//18.报告单ID号 多张报告单的情况，默认1

        // 必填项
        // 分析项目代码 服务机构分析项目代码
        Object resultInfo_test_item_id = bodyMap.get("ResultInfo_TEST_ITEM_ID");
        if (resultInfo_test_item_id != null && StringUtil.isNotBlank(resultInfo_test_item_id.toString())) {
            resultInfo_TEST_ITEM_ID.setText(resultInfo_test_item_id.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：分析项目代码 TEST_ITEM_ID 为空！");
        }
        // 分组号 检测小组代号
        Object resultInfo_group_id = bodyMap.get("ResultInfo_GROUP_ID");
        if (resultInfo_group_id != null && StringUtil.isNotBlank(resultInfo_group_id.toString())) {
            resultInfo_GROUP_ID.setText(resultInfo_group_id.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：分组号 GROUP_ID 为空！");
        }
        // 检测顺序号 样本号
        Object resultInfo_sample_number = bodyMap.get("ResultInfo_SAMPLE_NUMBER");
        if (resultInfo_sample_number != null && StringUtil.isNotBlank(resultInfo_sample_number.toString())) {
            resultInfo_SAMPLE_NUMBER.setText(resultInfo_sample_number.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：检测顺序号 SAMPLE_NUMBER 为空！");
        }
        // 参考范围 如：2.00-3.98
        Object resultInfo_test_item_reference = bodyMap.get("ResultInfo_TEST_ITEM_REFERENCE");
        if (resultInfo_test_item_reference != null && StringUtil.isNotBlank(resultInfo_test_item_reference.toString())) {
            resultInfo_TEST_ITEM_REFERENCE.setText(resultInfo_test_item_reference.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：检测顺序号 TEST_ITEM_REFERENCE 为空！");
        }
        // 检测方法 如：荧光PCR法
        Object resultInfo_test_method = bodyMap.get("ResultInfo_TEST_METHOD");
        if (resultInfo_test_method != null && StringUtil.isNotBlank(resultInfo_test_method.toString())) {
            resultInfo_TEST_METHOD.setText(resultInfo_test_method.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：检测方法 TEST_METHOD 为空！");
        }
        // 检测定量结果
        Object resultInfo_quantitative_result = bodyMap.get("ResultInfo_QUANTITATIVE_RESULT");
        if (resultInfo_quantitative_result != null && StringUtil.isNotBlank(resultInfo_quantitative_result.toString())) {
            resultInfo_QUANTITATIVE_RESULT.setText(resultInfo_quantitative_result.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：检测定量结果 QUANTITATIVE_RESULT 为空！");
        }
        // 检测单位
        Object resultInfo_test_item_unit = bodyMap.get("ResultInfo_TEST_ITEM_UNIT");
        if (resultInfo_test_item_unit != null && StringUtil.isNotBlank(resultInfo_test_item_unit.toString())) {
            resultInfo_TEST_ITEM_UNIT.setText(resultInfo_test_item_unit.toString());
        } else {
            throw new BusinessException("调用杏和接口--数据回传杏和------医院ID:" + HOSPITAL_ID + "--标本结果信息ResultInfo：检测单位 TEST_ITEM_UNIT 为空！");
        }

    }

    /**
     * 杏和区域接口 组装诊断建议信息explainInfo
     *
     * @param explainInfo 调用方法body
     * @param bodyMap     数据存放map
     */
    public void generateExplainInfo(Element explainInfo, Map<String, Object> bodyMap) {
        // 诊断建议信息explainInfo
        Element explainInfo_DIAGNOSIS_ADVICE = explainInfo.addElement("DIAGNOSIS_ADVICE");
        Element explainInfo_RESULT_ANALYZE = explainInfo.addElement("RESULT_ANALYZE");
        // 建议
        Object explainInfo_diagnosis_advice = bodyMap.get("ExplainInfo_DIAGNOSIS_ADVICE");
        if (explainInfo_diagnosis_advice != null && StringUtil.isNotBlank(explainInfo_diagnosis_advice.toString())) {
            String text ="<![CDATA[" + explainInfo_diagnosis_advice.toString() + "]]>";
            explainInfo_DIAGNOSIS_ADVICE.setText(text);
        }
        // 结果分析
         /*Object explainInfo_result_analyze = bodyMap.get("explainInfo_RESULT_ANALYZE");
         if (explainInfo_result_analyze != null && StringUtil.isNotBlank(explainInfo_result_analyze.toString())) {
         explainInfo_RESULT_ANALYZE.setText(explainInfo_result_analyze.toString());
         }*/
    }

    /**
     * 杏和区域接口 解析返回xml
     *
     * @param xmlMsg 杏和返回的xml
     * @return map 解析后的xml数据
     */
    public Map<String, Object> getResultXmlMsg(String xmlMsg) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            XmlUtil xmlUtil = XmlUtil.getInstance();
            Document document = xmlUtil.parse(xmlMsg);
            Element rootElement = document.getRootElement();
            for (Element element : rootElement.elements()) {
                readXmlToMap(resultMap, element);
            }
        } catch (DocumentException e) {
            log.error("调用接口--处理申请单信息------getResultXmlMsg 报错" + e.getMessage());
        }
        return resultMap;
    }

    /**
     * 处理xml转成map
     *
     * @param xmlMap  杏和返回的xml
     * @param element 调用方法body
     */
    public void readXmlToMap(Map<String, Object> xmlMap, Element element) {
        if (!CollectionUtils.isEmpty(element.elements())) {
            List<Map<String, Object>> elementList = new ArrayList<>();
            List<Element> elements = element.elements();
            for (Element e : elements) {
                Map<String, Object> emap = new HashMap<>();
                readXmlToMap(emap, e);
                elementList.add(emap);
            }
            xmlMap.put(element.getName(), elementList);
        } else {
            xmlMap.put(element.getName(), element.getText());
        }
    }

    /**
     * 处理杏和接口返回数据
     *
     * @param infoMap 杏和返回的数据
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void dealReturnInfo(Map<String, Object> infoMap, String hospitalId,String dealflg) {
        String resultCode = (String) infoMap.get("ResultCode");
        if ("1".equals(resultCode)) {
            Object itemObj = infoMap.get("Items");
            List<Map<String, Object>> items = (List<Map<String, Object>>) itemObj;
            List<SjdwxxDto> sjdwxxlist = sjxxService.getSjdw();//科室信息List
            List<JcsjDto> yblxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//样本类型List
            List<JcsjDto> jcxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
            List<JcsjDto> jcdwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
            String sjqfdm = "";
            //设置快递类型为无
            List<JcsjDto> kdlxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
            String kdlxid = "";
            for (JcsjDto kdlx : kdlxList) {
                if ("QYY".equals(kdlx.getCsdm())) {
                    kdlxid = kdlx.getCsid();
                    break;
                }
            }
            //设置送检区分为入院
            String sjqfid = "";
            List<JcsjDto> sjqfList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
            for (JcsjDto sjqf : sjqfList) {
                if ("入院".equals(sjqf.getCsmc())) {
                    sjqfid = sjqf.getCsid();
                    sjqfdm = sjqf.getCsdm();
                    break;
                }
            }
            int updateCount = 0;
            int insertCount = 0;
            int totalCount = 0;
            int alreadyCount = 0;
            List<String> unCompareInfo = new ArrayList<>();
            List<String> unCompareDetectionInfo = new ArrayList<>();
            //遍历新增送检信息
            for (Map<String, Object> item : items) {
                Map<String, String> info = new HashMap<>();
                try {
                    totalCount++;
                    List<Map<String, String>> itemList = (List<Map<String, String>>) item.get("Item");
                    for (Map<String, String> key : itemList) {
                        Object[] keys = key.keySet().toArray();
                        for (Object k : keys) {
                            info.put(k.toString(), key.get(k.toString()));
                        }
                    }
                    SjxxDto sjxxDto = xingheInfoToSjxx(info, hospitalId, sjdwxxlist, yblxList, jcxmList, jcdwList, unCompareInfo, unCompareDetectionInfo,redisUtil);
                    String yhm = "hospitalXinghe";
                    if("new".equals(dealflg)){
                        yhm = "hospitalXingheNew";
                    }
                    Map<String, Object> resultMap = dealSaveSjxxDto(sjxxDto, sjqfdm, kdlxid, sjqfid, yhm, JSON.toJSONString(info));
                    if(resultMap.get("STATUS") != null && ("insert".equals(resultMap.get("STATUS")) || "update".equals(resultMap.get("STATUS")) || "exist".equals(resultMap.get("STATUS")))){
                        SjkzxxDto sjkzxxDto = new SjkzxxDto();
                        sjkzxxDto.setSjid(sjxxDto.getSjid());
                        sjkzxxDto.setXgry(yhm);
                        boolean result = sjkzxxService.update(sjkzxxDto);
                        if (!result){
                            sjkzxxDto.setLrry(yhm);
                            sjkzxxService.insertDto(sjkzxxDto);
                        }
                        WbsjxxDto wbsjxxDto = new WbsjxxDto();
                        wbsjxxDto.setSjid(sjxxDto.getSjid());
                        wbsjxxDto.setInfojson(JSON.toJSONString(info));
                        List<WbsjxxDto> listBySjid = wbsjxxService.getListBySjid(sjxxDto.getSjid());
                        if (CollectionUtils.isEmpty(listBySjid) || listBySjid.size() == 0){
                            wbsjxxDto.setId(StringUtil.generateUUID());
                            wbsjxxService.insert(wbsjxxDto);
                        }else {
                            wbsjxxService.updateInfojsonBySjid(wbsjxxDto);
                        }
                    }
                    String status = (String) resultMap.get("STATUS");
                    switch (status) {
                        case "insert":
                            insertCount++;
                            break;
                        case "update":
                            updateCount++;
                            break;
                        case "exist":
                            alreadyCount++;
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--异常！异常信息：" + e.getMessage());
                    sendMessage(null, null, info, "调用杏和接口--处理申请单信息失败------医院ID:" + hospitalId);
                }
            }
            sendMessage(unCompareInfo, unCompareDetectionInfo, null, "医院：" + hospitalId);
            log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
            jkdymxDto.setQtxx("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
            jkdymxDto.setFhnr(JSON.toJSONString(infoMap).length()>4000?JSON.toJSONString(infoMap).substring(0,4000):JSON.toJSONString(infoMap)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
        } else {
            log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--" + infoMap.get("ResultMsg"));
            jkdymxDto.setFhnr(JSON.toJSONString(infoMap)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
        }
    }

    /**
     * 转换杏和信息为送检信息
     *
     * @param infoMap 杏和返回的数据
     * @return sjxxDto 送检信息
     */
    public SjxxDto xingheInfoToSjxx(Map<String, String> infoMap, String hospitalId, List<SjdwxxDto> sjdwxxlist, List<JcsjDto> yblxList, List<JcsjDto> jcxmList, List<JcsjDto> jcdwList, List<String> unCompareInfo, List<String> unCompareDetectionInfo,RedisUtil redisUtil) {
        SjxxDto sjxxDto = new SjxxDto();
        Map<String, String> jsonMap = null;
        if (map.get("json") != null){
            String json = map.get("json").toString();
            jsonMap = (Map<String, String>) JSON.parse(json);
        }
        //sjxxDto.set(infoMap.get("PATIENT_ID"));//病人ID =>
        //sjxxDto.set(infoMap.get("PATIENT_TYPE"));//病人类别 => 1住院2门诊3住院急诊4门诊急诊5体检6外院7其他8科研9金卡10质控11临药基地12分院13住院VIP14门诊VIP15婚检16复查17验证18急诊19特需病房20特需门诊
        //sjxxDto.set(infoMap.get("INPATIENT_ID"));//病人唯一号 =>
        //sjxxDto.set(infoMap.get("VISIT_NUM"));// 次数 =>
        //sjxxDto.set(infoMap.get("CHARGE_TYPE"));//收费类别 => 医保、自费。。。
        //sjxxDto.set(infoMap.get("PATIENT_BIRTHDAY"));//出生年月
        //sjxxDto.set(infoMap.get("PATIENT_NATION"));//名族 =>
        //sjxxDto.set(infoMap.get("DIET"));//是否饮食 =>
        //sjxxDto.set(infoMap.get("BLOODTYPE_RH"));//RH血型 =>
        //sjxxDto.set(infoMap.get("BLOODTYPE_ABO"));//ABO血型 =>
        //sjxxDto.set(infoMap.get("RH_PHENOTYPE"));//RH表型 =>
        //sjxxDto.set(infoMap.get("PATIENT_DEPT"));//科别 =>
        //sjxxDto.set(infoMap.get("PATIENT_WARD"));//病区 =>
        //sjxxDto.set(infoMap.get("PATIENT_WARD_NAME"));//病区名称 =>
        //sjxxDto.set(infoMap.get("ID_CARD"));//身份证号 =>
        //sjxxDto.set(infoMap.get("REQUISITION_TIME"));//申请时间 =>
        //sjxxDto.set(infoMap.get("REQUISITION_PERSON"));//申请人员 =>
        //sjxxDto.set(infoMap.get("ORDER_ITEM_ID"));//申请项目ID => 	诊疗项目代码、套餐代码

        //根据LIS对应的医院id设置
        YyxxDto yyxxDto = new YyxxDto();
        YyxxDto real_yyxxDto = new YyxxDto();
        yyxxDto.setCskz3(hospitalId);
        List<YyxxDto> yyxxDtos = yyxxService.selectYyxxByLisId(yyxxDto);
        //yy.cskz3: HOSPITAL_ID(医院id),db(合作伙伴),ResultInfo_TEST_ITEM_ID(分析项目代码),jcdwdm(检测单位代码),院区区分(REQUISITION_ID的前两位)
        if (!CollectionUtils.isEmpty(yyxxDtos)) {
            if (yyxxDtos.size() > 1) {
                //院区匹配,目前浙江医院有区分(REQUISITION_ID 三墩医院 前两位 02；灵隐院区 前两位 01)
                for (YyxxDto yy : yyxxDtos) {
                    String[] split = yy.getCskz3().split(",");
                    if (split.length < 5) {
                        continue;
                    }
                    String yq = split[4];
                    String requisition_id = infoMap.get("REQUISITION_ID");
                    String yqid = requisition_id.substring(0, 2);
                    if (yq.equals(yqid)) {
                        sjxxDto.setSjdw(yy.getDwid());
                        sjxxDto.setSjdwmc(yy.getDwmc());
                        sjxxDto.setDb(yy.getCskz3().split(",")[1]);
                        real_yyxxDto = yy;
                        break;
                    }
                }
            } else {
                sjxxDto.setSjdw(yyxxDtos.get(0).getDwid());
                sjxxDto.setSjdwmc(yyxxDtos.get(0).getDwmc());
                sjxxDto.setDb(yyxxDtos.get(0).getCskz3().split(",")[1]);
                real_yyxxDto = yyxxDtos.get(0);
            }
        } else {
            log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--医院信息未匹配到！");
        }
        if (!CollectionUtils.isEmpty(jcxmList)) {
            String mrjcdwid = "";
            String mrjcdwmc = "";
            String jcdwdm = "";
            if (jsonMap != null && jsonMap.get("jcdw")!=null){
                jcdwdm = jsonMap.get("jcdw").toString();
            } else {
                if (real_yyxxDto != null) {
                    String[] split = real_yyxxDto.getCskz3().split(",");
                    if (split.length >= 4) {
                        jcdwdm = split[3];
                    }
                }
            }
            for (JcsjDto jcdw : jcdwList) {
                if (jcdw.getCsdm().equals(jcdwdm)) {
                    sjxxDto.setJcdw(jcdw.getCsid());
                    sjxxDto.setJcdwmc(jcdw.getCsmc());
                    break;
                }
                if ("1".equals(jcdw.getSfmr())) {
                    mrjcdwid = jcdw.getCsid();
                    mrjcdwmc = jcdw.getCsmc();
                }
            }
            if (StringUtil.isBlank(sjxxDto.getJcdw())) {
                sjxxDto.setJcdw(mrjcdwid);
                sjxxDto.setJcdwmc(mrjcdwmc);
            }
        } else {
            log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--检测单位信息未匹配到！");
        }
        //设置科室
        String ksmc = infoMap.get("PATIENT_DEPT_NAME");//科别名称 =>
        String qtsjdwid = "";
        for (SjdwxxDto sjdwxx : sjdwxxlist) {
            if (sjdwxx.getDwmc().equals(ksmc)) {
                sjxxDto.setKs(sjdwxx.getDwid());
                if ("1".equals(sjdwxx.getKzcs())) {
                    //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                    sjxxDto.setQtks(ksmc);
                    sjxxDto.setKsmc(ksmc);
                }
                break;
            } else if (StringUtil.isNotBlank(sjdwxx.getKzcs3())) {
                String[] ppkss = sjdwxx.getKzcs3().split(",");
                boolean isCompare = false;
                for (String ppks : ppkss) {
                    if (ppks.equals(ksmc)) {
                        sjxxDto.setKs(sjdwxx.getDwid());
                        if ("1".equals(sjdwxx.getKzcs())) {
                            //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                            sjxxDto.setQtks(ksmc);
                            sjxxDto.setKsmc(ksmc);
                        }
                        isCompare = true;
                        break;
                    }
                }
                if (isCompare) {
                    break;
                }
            }
            if ("00999".equals(sjdwxx.getDwdm())) {
                qtsjdwid = sjdwxx.getDwid();
            }
        }
        if (StringUtil.isBlank(sjxxDto.getKs())) {
            sjxxDto.setKs(qtsjdwid);
            sjxxDto.setQtks(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            sjxxDto.setKsmc(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--科室信息未匹配到！科室名称:" + ksmc);
            unCompareInfo.add((unCompareInfo.size() + 1) + "、医院：" + sjxxDto.getSjdwmc() + ",\n\n　　未匹配科室：" + ksmc + ",\n\n　　标本:" + infoMap.get("INSPECTION_ID"));
        }

        //样本类型，若匹配到，则取匹配到的yblx,若匹配不到，则去其他的yblx
        String yblxmc = infoMap.get("SAMPLE_CLASS_NAME");//标本种类 => yblxmc
        compareYblx(sjxxDto,yblxList,yblxmc,redisUtil);
        //匹配检测项目
        String jcxmmc = infoMap.get("ORDER_ITEM_NAME");//申请项目名称 => 	诊疗项目代码、套餐名称
        String jcxmpp = real_yyxxDto!=null?real_yyxxDto.getJcxmpp():"";//获取匹配到的医院的检测项目匹配字段
        String jcsjCskz3 = "IMP_REPORT_ONCO_QINDEX_TEMEPLATE";//默认Q_mNGS
        String jcsjCskz1 = "";
        if (StringUtil.isNotBlank(jcxmpp)) {
            String jcxmppdm = "";
            //若jcxmpp不为空
            String[] pps = jcxmpp.split(";");
            for (String pp : pps) {
                String[] ppnr = pp.split(":");
                if (jcxmmc.equals(ppnr[0])) {
                    jcxmppdm = ppnr[1];
                    break;
                }
            }
            if (StringUtil.isNotBlank(jcxmppdm)) {
                String[] cskzs = jcxmppdm.split(",");
                jcsjCskz3 = cskzs[0];
                jcsjCskz1 = cskzs[1];
            }
        } else {
            jcxmmc = jcxmmc.toUpperCase();
            if (jcxmmc.contains("DNA") && jcxmmc.contains("RNA")) {
                jcsjCskz1 = "C";
            } else if (jcxmmc.contains("DNA") && !jcxmmc.contains("RNA")) {
                jcsjCskz1 = "D";
            } else if (!jcxmmc.contains("DNA") && jcxmmc.contains("RNA")) {
                jcsjCskz1 = "R";
            } else if (jcxmmc.contains("ONCO")) {
                jcsjCskz1 = "O";
            }
        }
        for (JcsjDto jcxm : jcxmList) {
            if (jcsjCskz3.equals(jcxm.getCskz3())) {
                List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
                SjjcxmDto sjjcxmDto = new SjjcxmDto();
                if (jcsjCskz3.equals(jcxm.getCskz3()) && jcsjCskz1.equals(jcxm.getCskz1())) {
                    sjjcxmDto.setJcxmid(jcxm.getCsid());
                    sjjcxmDtos.add(sjjcxmDto);
                    sjxxDto.setSjjcxms(sjjcxmDtos);
                    sjxxDto.setJcxmmc(jcxm.getCsmc());
                    break;
                }
            }
        }
        //若文字未匹配上，设置为默认检测项目
        if (StringUtil.isBlank(sjxxDto.getJcxmmc())) {
            log.error("调用杏和接口--处理申请单信息------医院ID:" + hospitalId + "--检测项目为匹配，请手动匹配！--" + jcxmmc);
            unCompareDetectionInfo.add((unCompareDetectionInfo.size() + 1) + "、医院：" + sjxxDto.getSjdwmc() + ",\n\n　　未匹配检测项目：" + jcxmmc + ",\n\n　　标本:" + infoMap.get("REQUISITION_ID"));
        }


        sjxxDto.setWbbm(infoMap.get("INSPECTION_ID"));//检验单号 => sjxx.wbbm	上传报告需要
        sjxxDto.setYbbh(infoMap.get("REQUISITION_ID"));//申请单ID => sjxx.ybbh	上传报告需要
        sjxxDto.setCskz6(infoMap.get("SAMPLE_NUMBER") + "," + infoMap.get("GROUP_ID"));//LIS样本号,分组号 => 上传报告需要
        sjxxDto.setZyh(infoMap.get("OUTPATIENT_ID"));//住院号 => sjxx.zyh
        sjxxDto.setHzxm(infoMap.get("PATIENT_NAME"));//姓名 => sjxx.hzxm
        sjxxDto.setXb("1".equals(infoMap.get("PATIENT_SEX")) ? "1" : ("2".equals(infoMap.get("PATIENT_SEX")) ? "2" : "3"));//性别 => sjxx.xb	1男2女3未知
        sjxxDto.setNl(infoMap.get("PATIENT_AGE"));//年龄 => sjxx.nl + sjxx.nldw
        sjxxDto.setJqyy(infoMap.get("TAKE_MEDICINE"));//用药信息 => sjxx.jqyy
        sjxxDto.setCwh(infoMap.get("PATIENT_BED"));//床号 => sjxx.cwh
        sjxxDto.setQqzd(StringUtil.isNotBlank(infoMap.get("CLINICAL_DIAGNOSES")) ? infoMap.get("CLINICAL_DIAGNOSES") : "-");//临床诊断 => sjxx.qqzd
        sjxxDto.setLczz("-");//临床症状
        sjxxDto.setSjys(infoMap.get("REQUISITION_PERSON"));//送检医生
        sjxxDto.setYbtj("-");//标本体积
        sjxxDto.setDh(infoMap.get("MOBILE_NO"));//患者联系电话 => 电话
        sjxxDto.setCyrq(infoMap.get("SAMPLING_TIME"));//采样时间 => sjxx.cyrq
//        sjxxDto.setSfje(infoMap.get("CHARGE"));//价格 => sjxx.sfje
//        sjxxDto.setSfsf(infoMap.get("CHARGE_STATE"));//收费状态 => sjxx.sfsf	0未收费1已收费
        sjxxDto.setBz(infoMap.get("REMARK"));//备注 => sjxx.bz
        sjxxDto.setLrsj(infoMap.get("INPUT_TIME"));//录入时间 => sjxx.lrsj
        return sjxxDto;
    }

    /**
     * 生成并发送杏和xml 6.0版
     *
     * @param methodCode 方法名称
     * @param queryMode  查询方式 非查询NONE,根据id查询 ID,根据日期查询 RQ
     * @param hospitalId 医院ID
     * @param infoMap    数据存放map
     * @return resultXmlMsg 杏和返回数据
     * @throws Exception xml请求异常信息
     */
    public Map<String, Object> generateXingheAreaMsgNew(String methodCode, String queryMode, String hospitalId, Map<String, Object> infoMap) throws Exception {

        String responseName = "CallAreaInterfaceResponse";

        //杏和配置信息：wsdlurl
        String xinghe_newurl = map.get("xinghe_newurl").toString();
        //杏和配置信息：ak
        String xinghe_ak = map.get("xinghe_ak").toString();
        //杏和配置信息：sk
        String xinghe_sk = map.get("xinghe_sk").toString();
        //杏和配置信息：ticket
        //String xinghe_ticket = map.get("xinghe_ticket").toString();
        //杏和配置信息：hospitalservice
        String xinghe_hospitalservice = map.get("xinghe_hospitalservice").toString();

        // 创建HttpHeaders并设置Content-Type为application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestParams = new JSONObject();
        requestParams.put("MethodCode",methodCode);
        requestParams.put("Ticket",xinghe_ak);

        //获取申请单信息(仅套餐项目)、获取申请单信息(含分析项目)
        if ("SelectAreaReqInfo".equals(methodCode) || "SelectAreaSampleInfo".equals(methodCode)) {
            log.error("调用杏和接口6.0--获取申请单信息------医院ID:" + hospitalId + "--调用方法:" + methodCode);
            if ("RQ".equals(queryMode)) {
                String headXml = generateSelectDateAreaInfoXml(infoMap, hospitalId, xinghe_hospitalservice,1);
                String str_encry = Encrypt_ECB.encrypt(headXml, xinghe_sk);
                requestParams.put("SampleData",str_encry);
            } else if ("ID".equals(queryMode)) {
                String headXml = generateSelectSingleAreaInfoXml(infoMap, hospitalId, xinghe_hospitalservice,1);
                String str_encry = Encrypt_ECB.encrypt(headXml, xinghe_sk);
                requestParams.put("SampleData",str_encry);
            } else {
                throw new RuntimeException("获取申请单信息6.0" + hospitalId + "：非查询,queryMode:" + queryMode);
            }
            //接口调用明细 -- 设置请求内容
            jkdymxDto.setNr(JSON.toJSONString(requestParams));
        } else if ("UpdateAreaReqState".equals(methodCode)) {
            //申请单状态修改
            log.error("调用杏和接口6.0--申请单状态修改------医院ID:" + hospitalId + "--调用方法:UpdateAreaReqState");

        } else if ("RecvAreaResultInfo".equals(methodCode)) {
            log.error("调用杏和接口6.0--数据回传杏和------医院ID:" + hospitalId + "--样本编号:" + infoMap.get("ybbh"));
            //结果回传数据中心
            Map<String, Object> bodyMap = dealBodyMap(infoMap);
            String bodyxml = generateBodyXml( bodyMap, hospitalId, xinghe_hospitalservice,1);
            String str_encry = Encrypt_ECB.encrypt(bodyxml, xinghe_sk);
            requestParams.put("SampleData",str_encry);
            String pdfMsg = getPdfMsg(infoMap.get("wjlj").toString());
            requestParams.put("PdfData",pdfMsg);
        }
        // set timeout

        // 创建HttpEntity，包装需要发送的请求信息
        HttpEntity<String> t_entity = new HttpEntity<>(requestParams.toString(), headers);
        // 访问指定网址，并获取结果
        Map<String, String> result = getResultFromUrl(xinghe_newurl, t_entity);
        if(result == null){
            throw new RuntimeException("调用杏和接口6.0" + hospitalId + "：无法访问指定网址,url:" + xinghe_newurl);
        }

        String ResultCode = result.get("ResultCode");
        String encry_data = result.get("Data");
        if(StringUtil.isBlank(encry_data) && "1".equals(ResultCode)){
            String jsonresult = JSONObject.toJSONString(result);
            Map<String, Object> ob_result = JSONObject.parseObject(jsonresult,Map.class);
            return ob_result;
        }
        String data = Encrypt_ECB.decrypt(encry_data, xinghe_sk);

        Object o_xinheflg = redisUtil.get("XINGHE_LOG_FLG");
        if (o_xinheflg != null && "1".equals(o_xinheflg.toString())) {
            log.error("调用杏和接口6.0 " + xinghe_newurl + "：返回结果:" + data);
        }

        if("1".equals(ResultCode)){
            return getResultXmlMsg(data);
        }else{
            throw new RuntimeException("调用杏和接口6.0" + hospitalId + "：返回结果异常,ResultCode:" + ResultCode + " ResultMsg:" + result.get("ResultMsg"));
        }
    }

    /**
     * 访问指定utl，并忽视https的证书要求
     * @param xinghe_newurl
     * @param t_entity
     * @return
     */
    private Map<String, String> getResultFromUrl(String xinghe_newurl, HttpEntity<String> t_entity) {
        RestTemplate restTemplate = null;
        try {
            TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            HttpClientBuilder clientBuilder = HttpClients.custom();

            CloseableHttpClient httpClient = clientBuilder.setSSLSocketFactory(sslsf).build();

            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(30000);
            httpRequestFactory.setConnectTimeout(30000);
            httpRequestFactory.setReadTimeout(30000);

            httpRequestFactory.setHttpClient(httpClient);

            restTemplate = new RestTemplate(httpRequestFactory);
            //解决乱码
            List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
            httpMessageConverters.stream().forEach(httpMessageConverter ->{
                if(httpMessageConverter instanceof StringHttpMessageConverter){
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter)httpMessageConverter;
                    messageConverter.setDefaultCharset(Charset.forName("UTF-8"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        String str_result= restTemplate.exchange(xinghe_newurl, HttpMethod.POST, t_entity, String.class).getBody();
        Map<String, String> result = JSONObject.parseObject(str_result,Map.class);

        return result;
    }


    //-------------------------------------------------------------------------------------------------//
    //-----------------------------------------以下为千麦接口方法-----------------------------------------//
    //-------------------------------------------------------------------------------------------------//

    /**
     * 千麦获取授权Token
     *
     * @return token
     */
    private String getQianmaiToken() {
        return getQianmaiToken(false);
    }

    /**
     * 千麦获取授权Token
     *
     * @return token
     */
    private String getQianmaiToken(boolean isRefresh) {
        if (!isRefresh) {
            Object qianmaiToken = redisUtil.get("QIANMAI_TOKEN");
            if (qianmaiToken != null) {
                return (String) qianmaiToken;
            }
        }
        String QIANMAI_URL = map.get("QIANMAI_URL").toString();
        String USERCODE = map.get("QIANMAI_USERCODE").toString();
        String PASSWORD = map.get("QIANMAI_PASSWORD").toString();
        String apiUserCode = "apiUserCode=" + USERCODE;
        String apiPassword = "apiPassword=" + PASSWORD;
        String url = QIANMAI_URL + QIANMAI_TOKEN_URL + "?" + apiUserCode + "&" + apiPassword;
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> result = null;
        try {
            result = restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            log.error("千麦：获取token失败-----请求:" + url);
            log.error("千麦：获取token失败-----结果：" + e.getMessage());
        }
        if (result != null && (int) result.get("Code") == 0 && "success".equals((String) result.get("Msg"))) {
            log.error("千麦：获取token成功-----结果：" + JSON.toJSONString(result));
            redisUtil.set("QIANMAI_TOKEN", (String) result.get("Data"), 3500);
            return (String) result.get("Data");
        }
        log.error("千麦：获取token失败-----请求:" + url);
        log.error("千麦：获取token失败-----结果：" + JSON.toJSONString(result));
        return null;
    }

    /**
     * 定时调用千麦接口
     *
     * @param codeMap 参数
     */
    public void timedCallQianmai(Map<String, Object> codeMap) {
//		code = "barcodes=1111;2222,dateStartNum=-5,dateEndNum=0,startDate=2023-01-01,endDate=2023-01-02";
        Object barcodesObj = codeMap.get("barcodes");
        String barcodes;
        if (barcodesObj != null) {
            barcodes = barcodesObj.toString();
            String[] barcodeList = barcodes.split(";");
            for (String barcode : barcodeList) {
                dealQainmaiInfo(barcode, null, null);
                //接口调用明细 -- 保存信息
                jkdymxService.insertJkdymxDto(jkdymxDto);
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sdf.format(new Date());
            String startDate = nowDate;
            String endDate = nowDate;
            Object dateStartNum = codeMap.get("dateStartNum");
            if (dateStartNum != null) {
                String dateNum = dateStartNum.toString();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, Integer.parseInt(dateNum));
                startDate = sdf.format(cal.getTime());
            }
            Object dateEndNum = codeMap.get("dateEndNum");
            if (dateEndNum != null) {
                String dateNum = dateEndNum.toString();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, Integer.parseInt(dateNum));
                endDate = sdf.format(cal.getTime());
            }
            Object startDateObj = codeMap.get("startDate");
            if (startDateObj != null) {
                startDate = startDateObj.toString();
            }
            Object endDateObj = codeMap.get("endDate");
            if (endDateObj != null) {
                endDate = endDateObj.toString();
            }
            dealQainmaiInfo(null, startDate, endDate);
            //接口调用明细 -- 保存信息
            jkdymxService.insertJkdymxDto(jkdymxDto);
        }
    }

    /**
     * 处理千麦样本信息
     *
     * @param barcode   样本编号
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public void dealQainmaiInfo(String barcode, String startDate, String endDate) {
        List<Map<String, Object>> list = receiveInfoList(barcode, startDate, endDate, 1);
        if (!CollectionUtils.isEmpty(list)) {
//            List<Integer> idList = new ArrayList<>();
            List<SjdwxxDto> sjdwxxlist = sjxxService.getSjdw();//科室信息List
            List<JcsjDto> yblxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//样本类型List
            List<JcsjDto> jcxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
            List<JcsjDto> jcdwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
            String sjqfdm = "";
            //设置快递类型为无
            List<JcsjDto> kdlxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
            String kdlxid = "";
            for (JcsjDto kdlx : kdlxList) {
                if ("QYY".equals(kdlx.getCsdm())) {
                    kdlxid = kdlx.getCsid();
                    break;
                }
            }
            //设置送检区分为入院
            String sjqfid = "";
            List<JcsjDto> sjqfList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
            for (JcsjDto sjqf : sjqfList) {
                if ("特检".equals(sjqf.getCsmc())) {
                    sjqfid = sjqf.getCsid();
                    sjqfdm = sjqf.getCsdm();
                    break;
                }
            }
            int updateCount = 0;
            int insertCount = 0;
            int totalCount = 0;
            int alreadyCount = 0;
            List<String> unCompareInfo = new ArrayList<>();
            for (Map<String, Object> map : list) {
                totalCount++;
                SjxxDto sjxxDto = qianmaiToSjxx(map, sjdwxxlist, yblxList, jcxmList, jcdwList, unCompareInfo);
                Map<String, Object> resultMap = dealSaveSjxxDto(sjxxDto, sjqfdm, kdlxid, sjqfid, "hospitalQianmai", JSON.toJSONString(map));
                if(resultMap.get("STATUS") != null && ("insert".equals(resultMap.get("STATUS")) || "update".equals(resultMap.get("STATUS")) || "exist".equals(resultMap.get("STATUS")))){
                    SjkzxxDto sjkzxxDto = new SjkzxxDto();
                    sjkzxxDto.setSjid(sjxxDto.getSjid());
                    Map<String,Object> qtxxMap = new HashMap<>();
                    qtxxMap.put("CollectionTime",map.get("CollectionTime") != null ? map.get("CollectionTime").toString() : "");
                    qtxxMap.put("ReceiveTime",map.get("ReceiveTime") != null ? map.get("ReceiveTime").toString() : "");
                    sjkzxxDto.setQtxx(JSON.toJSONString(qtxxMap));
                    sjkzxxDto.setXgry("hospitalQianmai");
                    boolean result = sjkzxxService.update(sjkzxxDto);
                    if (!result){
                        sjkzxxDto.setLrry("hospitalQianmai");
                        sjkzxxService.insertDto(sjkzxxDto);
                    }
                }
                    
                String status = (String) resultMap.get("STATUS");
                switch (status) {
                    case "insert":
                        insertCount++;
//                        if (map.get("Id") != null) {
//                            idList.add(Integer.parseInt(map.get("Id").toString()));
//                        }
                        break;
                    case "update":
                        updateCount++;
                        break;
                    case "exist":
                        alreadyCount++;
                        break;
                    default:
                        break;
                }
            }
            jkdymxDto.setQtxx("千麦：获取样本信息成功-----结果：共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
            jkdymxDto.setFhnr(JSON.toJSONString(list).length()>4000?JSON.toJSONString(list).substring(0,4000):JSON.toJSONString(list)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
            //接口调用明细 -- 设置返回内容，返回结果
            log.error("千麦：获取样本信息成功-----结果：共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
            //回写数据，再次调用不获取数据
//            backAlreadyData(idList);
            sendMessage(unCompareInfo, null, null, "千麦对接系统，报告回传失败！");
        } else {
            //接口调用明细 -- 设置返回内容，返回结果
            jkdymxDto.setFhnr(JSON.toJSONString(list)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            log.error("千麦：获取样本信息失败-----未获取到样本信息-----结果：" + JSON.toJSONString(list));
        }
    }

    /**
     * 千麦回写数据
     * @param idList 回传ids
     */
    public void backAlreadyData(List<Integer> idList) {
        String QIANMAI_URL = map.get("QIANMAI_URL").toString();
        if (!CollectionUtils.isEmpty(idList)) {
            String token = getQianmaiToken();
            String url = QIANMAI_URL + QIANMAI_BACK_URL;
            //设置restTemplate请求头
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.set("Authorization", "Bearer " + token);
            HttpEntity requestEntity = new HttpEntity(idList, requestHeaders);
            try {
                Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);
                if (result != null && (int) result.get("Code") == 0 && "success".equals((String) result.get("Msg"))) {
                    log.error("千麦：回写数据成功-----结果：" + JSON.toJSONString(result));
                }
            } catch (RestClientException e) {
                log.error("千麦：回写数据失败-----请求:" + url);
                log.error("千麦：回写数据失败-----结果：" + e.toString());
            }
        }
    }

    /**
     * 回传报告给千麦
     * @param map 传入参数
     * @return boolean 是否成功
     */
    private void returnReport(Map<String, Object> map) {
        String QIANMAI_URL = map.get("QIANMAI_URL").toString();
        String EntrustHosCode = map.get("QIANMAI_EntrustHosCode").toString();
        Map<String, Object> requestEntrustModel = new HashMap<>();
        requestEntrustModel.put("LabCode", map.get("LabCode").toString());
        requestEntrustModel.put("BarCode", map.get("BarCode").toString());
        requestEntrustModel.put("PurCodes", map.get("PurCodes").toString());
        requestEntrustModel.put("PurNames", map.get("PurNames").toString());
        requestEntrustModel.put("EntrustHosCode", EntrustHosCode);
        requestEntrustModel.put("ExamResult", new ArrayList<String>());
        String pdfMsg = getPdfMsg(map.get("wjlj").toString());
        List<Map<String, Object>> reportInfoList = new ArrayList<>();
        Map<String, Object> reportInfo = new HashMap<>();
        reportInfo.put("ReportFileName", map.get("wjm").toString());
        reportInfo.put("Ref1", pdfMsg);
        reportInfoList.add(reportInfo);
        requestEntrustModel.put("ReportInfo", reportInfoList);
        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(requestEntrustModel);
        String token = getQianmaiToken();
        String url = QIANMAI_URL + QIANMAI_REPORT_RETURN_URL;
        //设置restTemplate请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", "Bearer " + token);
        HttpEntity requestEntity = new HttpEntity(resultList, requestHeaders);
        //接口调用明细 -- 设置请求内容
        jkdymxDto.setNr(JSON.toJSONString(requestEntrustModel));
        try {
            Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);

            //将msg存储到文件中
            String filePath = mkDirs(BusTypeEnum.IMP_REPORT_QIANMAI_XML.getCode(), "/matridx/fileupload/temp/");
            String fileName = "qianmai" + "_" + map.get("BarCode").toString() + "_" + System.currentTimeMillis() + ".xml";
            String msg = JSON.toJSONString(requestEntity);
            FileUtils.writeStringToFile(new File(filePath + fileName), msg);
            if (result != null && (int) result.get("Code") == 0 && "success".equals((String) result.get("Msg"))) {
                log.error("千麦：回传报告成功-----结果：" + JSON.toJSONString(result));
                //接口调用明细 -- 设置返回内容
                jkdymxDto.setFhnr(JSON.toJSONString(result).length()>4000?JSON.toJSONString(result).substring(0,4000):JSON.toJSONString(result)); // 返回内容
                jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
                jkdymxService.insertJkdymxDto(jkdymxDto);
            }
        } catch (RestClientException e) {
            log.error("千麦：回传报告失败-----请求:" + url);
            log.error("千麦：回传报告失败-----结果：" + e.getMessage());
            //接口调用明细 -- 设置返回内容，返回结果
            jkdymxDto.setFhnr("千麦：回传报告失败-----结果：" + e.getMessage()); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            jkdymxService.insertJkdymxDto(jkdymxDto);
            reSendRabbit(map,e.getMessage());
            log.error("千麦：回传报告失败-----重新发送rabbit!");
        } catch (IOException e) {
            log.error("千麦：回传报告数据备份失败-----结果：" + e.getMessage());
            jkdymxDto.setFhnr("千麦：回传报告数据备份失败-----结果：" + e.getMessage()); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            jkdymxService.insertJkdymxDto(jkdymxDto);
        }
    }

    /**
     * 千麦获取样本信息
     *
     * @param barcode           样本条码 有千麦条码时，时间不起作用
     * @param startDate         开始时间 有千麦条码时，开始时间不起作用
     * @param endDate           结束时间 有千麦条码时，结束时间不起作用，时间差不超过7天
     * @param requestDataMethod 请求数据方式 0：获取所有数据 1：获取最新数据
     * @return List<Map<String, Object>> 样本信息
     */
    private List<Map<String, Object>> receiveInfoList(String barcode, String startDate, String endDate, int requestDataMethod) {
        String QIANMAI_URL = map.get("QIANMAI_URL").toString();
        String EntrustHosCode = map.get("QIANMAI_EntrustHosCode").toString();
        Map<String, Object> requestEntrustModel = new HashMap<>();
        requestEntrustModel.put("EntrustHosCode", EntrustHosCode);
        if (StringUtil.isNotBlank(barcode)) {
            requestEntrustModel.put("Barcode", barcode);
        }
        if (StringUtil.isNotBlank(startDate)) {
            requestEntrustModel.put("StartDate", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            requestEntrustModel.put("EndDate", endDate);
        }
        requestEntrustModel.put("RequestDataMethod", requestDataMethod);
        String token = getQianmaiToken();
        String url = QIANMAI_URL + QIANMAI_INFO_GET_URL;
        //设置restTemplate请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", "Bearer " + token);
        HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(requestEntrustModel), requestHeaders);
        //接口调用明细 -- 设置请求内容
        jkdymxDto.setNr(JSON.toJSONString(requestEntrustModel));
        log.error("千麦：获取样本信息-----请求参数：" + JSON.toJSONString(requestEntrustModel));
        Map<String, Object> result = null;
        try {
            result = restTemplate.postForObject(url, requestEntity, Map.class);
        } catch (RestClientException e) {
            //接口调用明细 -- 设置返回内容，返回结果
            jkdymxDto.setFhnr(JSON.toJSONString(e.toString()).length()>4000?JSON.toJSONString(e.toString()).substring(0,4000):JSON.toJSONString(e.toString())); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            log.error("千麦：获取样本信息失败-----请求:" + url);
            log.error("千麦：获取样本信息失败-----结果：" + e.toString());
        }
        if (result != null && (int) result.get("Code") == 0 && "success".equals((String) result.get("Msg"))) {
            log.error("千麦：获取样本信息成功-----结果：" + JSON.toJSONString(result));
            //接口调用明细 -- 设置返回内容
            jkdymxDto.setFhnr(JSON.toJSONString(result).length()>4000?JSON.toJSONString(result).substring(0,4000):JSON.toJSONString(result)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            return (List<Map<String, Object>>) result.get("Data");
        }
        //接口调用明细 -- 设置返回内容，返回结果
        jkdymxDto.setFhnr(JSON.toJSONString(result).length()>4000?JSON.toJSONString(result).substring(0,4000):JSON.toJSONString(result)); // 返回内容
        jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
        jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
        log.error("千麦：获取样本信息失败-----结果：" + JSON.toJSONString(result));
        return null;
    }

    /**
     * 千麦信息转成sjxxDto
     *
     * @param map        千麦信息
     * @param sjdwxxlist 科室信息List
     * @param yblxList   样本类型List
     * @param jcxmList  检测项目List
     * @param jcdwList  检测单位List
     * @param unCompareInfo 未匹配信息
     * @return SjxxDto 送检信息
     */
    private SjxxDto qianmaiToSjxx(Map<String, Object> map, List<SjdwxxDto> sjdwxxlist, List<JcsjDto> yblxList, List<JcsjDto> jcxmList, List<JcsjDto> jcdwList, List<String> unCompareInfo) {
        SjxxDto sjxxDto = new SjxxDto();
        sjxxDto.setWbbm(map.get("Id").toString());
        //1、样本编号(必填)
        sjxxDto.setYbbh((String) map.get("BarCode"));
        //2、患者姓名(必填)
        sjxxDto.setHzxm((String) map.get("PatientName"));
        //3、性别(必填)(sjxx.xb	1男2女3未知)
        String GenderName = map.get("GenderName") != null ? (String) map.get("GenderName") : "";
        switch (GenderName) {
            case "男":
                sjxxDto.setXb("1");
                break;
            case "女":
                sjxxDto.setXb("2");
                break;
            default:
                sjxxDto.setXb("3");
                break;
        }
        //4、年龄(必填)(sjxx.nl + sjxx.nldw)
        String nl = (map.get("Age1") != null ? map.get("Age1").toString() : (map.get("Age2") != null ? map.get("Age2").toString() : "0"));
        String nldw = map.get("AgeUnitName1") != null ? map.get("AgeUnitName1").toString() : (map.get("AgeUnitName2") != null ? map.get("AgeUnitName2").toString() : "");
        sjxxDto.setNl(nl + nldw);//年龄 => sjxx.nl + sjxx.nldw
        //5、送检医生(必填)
        sjxxDto.setSjys(map.get("Doctor") != null ? map.get("Doctor").toString() : "-");
        //6、科室(必填)
        String ksmc = map.get("DeptName") != null ? map.get("DeptName").toString() : "";
        String qtsjdwid = "";
        for (SjdwxxDto sjdwxx : sjdwxxlist) {
            if (sjdwxx.getDwmc().equals(ksmc)) {
                sjxxDto.setKs(sjdwxx.getDwid());
                if ("1".equals(sjdwxx.getKzcs())) {
                    //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                    sjxxDto.setQtks(ksmc);
                    sjxxDto.setKsmc(ksmc);
                }
                break;
            } else if (StringUtil.isNotBlank(sjdwxx.getKzcs3())) {
                String[] ppkss = sjdwxx.getKzcs3().split(",");
                boolean isCompare = false;
                for (String ppks : ppkss) {
                    if (ppks.equals(ksmc)) {
                        sjxxDto.setKs(sjdwxx.getDwid());
                        if ("1".equals(sjdwxx.getKzcs())) {
                            //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                            sjxxDto.setQtks(ksmc);
                            sjxxDto.setKsmc(ksmc);
                        }
                        isCompare = true;
                        break;
                    }
                }
                if (isCompare) {
                    break;
                }
            }
            if ("00999".equals(sjdwxx.getDwdm())) {
                qtsjdwid = sjdwxx.getDwid();
            }
        }
        if (StringUtil.isBlank(sjxxDto.getKs())) {
            sjxxDto.setKs(qtsjdwid);
            sjxxDto.setQtks(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            sjxxDto.setKsmc(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            log.error("千麦：处理送检信息-----科室信息未匹配到！------标本:" + sjxxDto.getYbbh() + "-----科室名称:" + (StringUtil.isNotBlank(ksmc) ? ksmc : "-"));
            if (StringUtil.isNotBlank(ksmc)) {
                //若科室名称为空白，则不加入钉钉消息中
                unCompareInfo.add((unCompareInfo.size() + 1) + "、千麦,\n\n　　未匹配科室：" + (StringUtil.isNotBlank(ksmc) ? ksmc : "-") + ",\n\n　　标本:" + sjxxDto.getYbbh());
            }
        }
        //7、住院号
        sjxxDto.setZyh(map.get("PatientId") != null ? map.get("PatientId").toString() : "");
        //8、床位号
        sjxxDto.setCwh(map.get("BedNo") != null ? map.get("BedNo").toString() : "");
        //9、电话
        sjxxDto.setDh(map.get("Phone") != null ? map.get("Phone").toString() : "");
        //10、送检单位(必填)
        String LabName = map.get("LabName") != null ? (String) map.get("LabName") : "";
        if ("杭州千麦医学检验实验室".equals(LabName) && (sjxxDto.getYbbh().startsWith("N") || sjxxDto.getYbbh().startsWith("817386"))){
            LabName = "广西千麦医学检验实验室";
            map.put("LabName","杭州千麦医学检验实验室");
            map.put("EntrustHosName","广州杰毅实验室");
        }
//        String LabCode = map.get("LabCode") != null ? (String) map.get("LabCode") : "";
        YyxxDto yyxxDto = new YyxxDto();
        YyxxDto real_yyxxDto = new YyxxDto();
        yyxxDto.setDwmc(LabName);
        List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
        if (!CollectionUtils.isEmpty(yyxxDtos)) {
            real_yyxxDto = yyxxDtos.get(0);
            sjxxDto.setSjdw(real_yyxxDto.getDwid());
            sjxxDto.setSjdwmc(real_yyxxDto.getDwmc());
        } else {
            sjxxDto.setBz("【医院信息未匹配到:"+LabName+",匹配后请删除该内容!】");
            log.error("千麦：处理送检信息-----医院信息未匹配到！------标本:" + sjxxDto.getYbbh());
        }
        //12、合作伙伴(必填)
        String cskz3 = real_yyxxDto.getCskz3();
        if (StringUtil.isNotBlank(cskz3)) {
            String[] cskz3s = cskz3.split(",");
            sjxxDto.setDb(cskz3s[1]);
            if (StringUtil.isNotBlank(cskz3s[1]) && cskz3s[1].contains("?")){
                //ParentCustomerCode==61B049?黄冈精准中心:武汉千麦
                boolean isTrue = true;
                String judge = cskz3s[1].substring(0,cskz3s[1].indexOf("?"));
                String trueValue = cskz3s[1].substring(cskz3s[1].indexOf("?")+1,cskz3s[1].indexOf(":"));
                String falseValue = cskz3s[1].substring(cskz3s[1].indexOf(":")+1);
                if (judge.contains("ParentCustomerCode") && map.get("ParentCustomerCode") != null){
                    judge = judge.replace("ParentCustomerCode", (String) map.get("ParentCustomerCode"));
                    if (judge.contains("==")){
                        isTrue = judge.substring(0,judge.indexOf("==")).equals(judge.substring(judge.indexOf("==")+2));
                    }
                    sjxxDto.setDb(isTrue?trueValue:falseValue);
                }
            }
        } else {
            log.error("千麦：处理送检信息-----合作伙伴信息未匹配到！------标本:" + sjxxDto.getYbbh());
        }
        //13、检测项目(必填)
        String jcxmdm = map.get("PurCodes") != null ? map.get("PurCodes").toString() : "";
        String jcxmmc = map.get("PurNames") != null ? map.get("PurNames").toString() : "";
        String cskz6 = "";
        if (StringUtil.isNotBlank(jcxmmc)) {
            jcxmmc = jcxmmc.replaceAll("，", ",");
            jcxmdm = jcxmdm.replaceAll("，", ",");
            String[] jcxmmcs = jcxmmc.split(",");
            String[] jcxmdms = jcxmdm.split(",");
            List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
            //匹配检测项目
            String jcxmpp = real_yyxxDto.getJcxmpp();//获取匹配到的医院的检测项目匹配字段
            for (int i = 0; i < jcxmmcs.length; i++) {
                String jcsjCskz3 = "IMP_REPORT_ONCO_QINDEX_TEMEPLATE";//默认Q_mNGS
                String jcsjCskz1 = "";
                if (StringUtil.isNotBlank(jcxmpp)) {
                    String jcxmppdm = "";
                    //若jcxmpp不为空
                    String[] pps = jcxmpp.split(";");
                    for (String pp : pps) {
                        String[] ppnr = pp.split(":");
                        if ((jcxmmcs[i] + "@" + jcxmdms[i]).equals(ppnr[0])) {
                            jcxmppdm = ppnr[1];
                            break;
                        }
                    }
                    if (StringUtil.isNotBlank(jcxmppdm)) {
                        String[] cskzs = jcxmppdm.split(",");
                        jcsjCskz3 = cskzs[0];
                        jcsjCskz1 = cskzs[1];
                    }
                    for (JcsjDto jc_jcxm : jcxmList) {
                        if (jcsjCskz3.equals(jc_jcxm.getCskz3())) {
                            SjjcxmDto sjjcxmDto = new SjjcxmDto();
                            if (jcsjCskz3.equals(jc_jcxm.getCskz3()) && jcsjCskz1.equals(jc_jcxm.getCskz1())) {
                                sjjcxmDto.setJcxmid(jc_jcxm.getCsid());
                                sjjcxmDtos.add(sjjcxmDto);
                                sjxxDto.setSjjcxms(sjjcxmDtos);
                                sjxxDto.setJcxmmc((StringUtil.isNotBlank(sjxxDto.getJcxmmc()) ? (sjxxDto.getJcxmmc() + ",") : "") + jc_jcxm.getCsmc());
                                cskz6 = (StringUtil.isNotBlank(cskz6) ? (cskz6 + ",") : "") + jcxmmcs[i] + "@" + jcxmdms[i] + ":" + jcsjCskz3 + "_" + jcsjCskz1;
                                break;
                            }
                        }
                    }
                }
            }
            sjxxDto.setCskz6(cskz6);
        }
        //若文字未匹配上，设置为默认检测项目
        if (StringUtil.isBlank(sjxxDto.getJcxmmc())) {
            sjxxDto.setBz((StringUtil.isNotBlank(sjxxDto.getBz())?sjxxDto.getBz():"") + "【检测项目未匹配:"+ jcxmdm + "," + jcxmmc+",匹配后请删除该内容!】");
            log.error("千麦：处理送检信息-----检测项目未匹配，请手动匹配！------标本:" + sjxxDto.getYbbh() + ",检测项目:" + jcxmdm + "," + jcxmmc);
        }
        //14、送检区分(必填)
        //15、样本类型(必填)
        String yblxmc = (String) map.get("SampleTypeName");
//        String yblxdm = (String) map.get("SampleTypeCode");
        compareYblx(sjxxDto,yblxList,yblxmc,redisUtil);
        //16、前期诊断(必填)
        sjxxDto.setQqzd(map.get("Diagnosis") != null ? map.get("Diagnosis").toString() : "-");
        //17、临床症状(必填)
        sjxxDto.setLczz("-");
        //18、采样时间(必填)
        sjxxDto.setCyrq(map.get("CollectionTime") != null ? map.get("CollectionTime").toString() : "");
        //19、检测单位(必填)
//        String dfsysdm = map.get("EntrustHosCode") != null ? map.get("EntrustHosCode").toString() : "";
        String dfsysmc = map.get("EntrustHosName") != null ? map.get("EntrustHosName").toString() : "";
        XxdyDto xxdyDto = new XxdyDto();
        xxdyDto.setYxx(dfsysmc);
        xxdyDto.setDydm("QIANMAI");
        xxdyDto.setDylxcsdm("JC01");
        XxdyDto xx_jcdw = xxdyService.getDto(xxdyDto);
        if (xx_jcdw != null) {
            sjxxDto.setJcdw(xx_jcdw.getDyxx());
            sjxxDto.setJcdwmc(xx_jcdw.getDyxxmc());
        } else {
            for (JcsjDto jc_jcdw : jcdwList) {
                if ("1".equals(jc_jcdw.getSfmr())) {
                    sjxxDto.setJcdw(jc_jcdw.getCsid());
                    sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                    break;
                }
            }
        }
        //20、备注
        String bz = map.get("Remark") != null ? map.get("Remark").toString() : "";
        sjxxDto.setBz((StringUtil.isNotBlank(sjxxDto.getBz())?sjxxDto.getBz():"") + bz);
        return sjxxDto;
    }


    //-------------------------------------------------------------------------------------------------//
    //-----------------------------------------以下为禾诺接口方法-----------------------------------------//
    //-------------------------------------------------------------------------------------------------//

    /**
     * 禾诺获取授权Token
     *
     * @return token
     */
    private String getHenuoToken() {
        return getHenuoToken(false);
    }

    /**
     * 禾诺获取授权Token
     *
     * @return token
     */
    private String getHenuoToken(boolean isRefresh) {
        if (!isRefresh) {
            Object henuoToken = redisUtil.get("HENUO_TOKEN"+map.get("HENUO_ACCOUNT").toString());
            if (henuoToken != null) {
                return (String) henuoToken;
            }
        }
        String HENUO_URL = map.get("HENUO_URL").toString();
        String ACCOUNT = map.get("HENUO_ACCOUNT").toString();
        String PASSWORD = map.get("HENUO_PASSWORD").toString();
        String USERTYPE = map.get("HENUO_USERTYPE").toString();
        Map<String, String> requestEntrustModel = new HashMap<>();
        requestEntrustModel.put("account", ACCOUNT);
        requestEntrustModel.put("password", PASSWORD);
        requestEntrustModel.put("userType", USERTYPE);
        String url = HENUO_URL + HENUO_TOKEN_URL;
        //设置restTemplate请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(requestEntrustModel), requestHeaders);
        try {
            Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);
            if (result != null && ((int) result.get("code") == 200 || (Boolean) result.get("success"))) {
                log.error("禾诺：获取token成功-----结果：" + JSON.toJSONString(result));
                redisUtil.set("HENUO_TOKEN"+map.get("HENUO_ACCOUNT").toString(), (String) result.get("data"), 39600);//缓存11小时
                return (String) result.get("data");
            }
        } catch (RestClientException e) {
            log.error("禾诺：获取token失败-----请求:" + url);
            log.error("禾诺：获取token失败-----结果：" + e.toString());
        }
        return null;
    }

    /**
     * 定时调用禾诺接口
     *
     * @param codeMap 传入参数
     */
    public void timedCallHenuo(Map<String, Object> codeMap) {
//		code = "barcode=1111,entBarcode=222,dateStartNum=-5,dateEndNum=0,startDate=2023-01-01,endDate=2023-01-02,startTime=08:00;endTime=18:00;timeStartNum=0;timeEndNum=0";
        Object barcodeObj = codeMap.get("barcode");
        Object entBarcodeObj = codeMap.get("entBarcode");
        String barcode;
        String entBarcode;
        if (barcodeObj != null) {
            barcode = barcodeObj.toString();
            dealHenuoInfo(barcode, null, null, null);
        } else if (entBarcodeObj != null) {
            entBarcode = entBarcodeObj.toString();
            dealHenuoInfo(null, entBarcode, null, null);
        } else {
            String startDate_str = "";
            String startTime_str = "";
            String endDate_str = "";
            String endTime_str = "";
            SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timesdf = new SimpleDateFormat("HH:mm");
            Object startDate = codeMap.get("startDate");
            if (startDate != null) {
                startDate_str = startDate.toString();
            }
            Object endDate = codeMap.get("endDate");
            if (endDate != null) {
                endDate_str = endDate.toString();
            }
            Object startTime = codeMap.get("startTime");
            if (startTime != null) {
                startTime_str = startTime.toString() + ":00.000";
            }
            Object endTime = codeMap.get("endTime");
            if (endTime != null) {
                endTime_str = endTime.toString() + ":59.999";
            }
            if (StringUtil.isBlank(startDate_str)) {
                Object dateStartNum = codeMap.get("dateStartNum");
                String dateNum = dateStartNum != null ? dateStartNum.toString() : "0";
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, Integer.parseInt(dateNum));
                startDate_str = datesdf.format(cal.getTime());
            }
            if (StringUtil.isBlank(endDate_str)) {
                Object dateEndNum = codeMap.get("dateEndNum");
                String dateNum = dateEndNum != null ? dateEndNum.toString() : "0";
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, Integer.parseInt(dateNum));
                endDate_str = datesdf.format(cal.getTime());
            }
            if (StringUtil.isBlank(startTime_str)) {
                Object timeStartNum = codeMap.get("timeStartNum");
                if (timeStartNum != null) {
                    String timeNum = timeStartNum.toString();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR, Integer.parseInt(timeNum));
                    startTime_str = timesdf.format(cal.getTime()) + ":00.000";
                } else {
                    startTime_str = "00:00:00.000";
                }
            }
            if (StringUtil.isBlank(endTime_str)) {
                Object timeEndNum = codeMap.get("timeEndNum");
                if (timeEndNum != null) {
                    String timeNum = timeEndNum.toString();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR, Integer.parseInt(timeNum));
                    endTime_str = timesdf.format(cal.getTime()) + ":59.999";
                } else {
                    endTime_str = "23:59:59.999";
                }
            }
            dealHenuoInfo(null, null, startDate_str + " " + startTime_str, endDate_str + " " + endTime_str);
        }
    }

    /**
     * 处理禾诺样本信息
     *
     * @param barcode   样本编号
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public void dealHenuoInfo(String barcode, String entBarcode, String startDate, String endDate) {
        List<Map<String, Object>> list = receiveInfoList(barcode, entBarcode, startDate, endDate);
        if (!CollectionUtils.isEmpty(list)) {
            List<SjdwxxDto> sjdwxxlist = sjxxService.getSjdw();//科室信息List
            List<JcsjDto> yblxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//样本类型List
            List<JcsjDto> jcxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
            List<JcsjDto> jcdwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
            String sjqfdm = "";
            //设置快递类型为无
            List<JcsjDto> kdlxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
            String kdlxid = "";
            for (JcsjDto kdlx : kdlxList) {
                if ("QYY".equals(kdlx.getCsdm())) {
                    kdlxid = kdlx.getCsid();
                    break;
                }
            }
            //设置送检区分为特检
            String sjqfid = "";
            List<JcsjDto> sjqfList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
            for (JcsjDto sjqf : sjqfList) {
                if ("特检".equals(sjqf.getCsmc())) {
                    sjqfid = sjqf.getCsid();
                    sjqfdm = sjqf.getCsdm();
                    break;
                }
            }
            //10、送检单位(必填)
            YyxxDto yyxxDto = new YyxxDto();
            YyxxDto real_yyxxDto = new YyxxDto();
            yyxxDto.setDwmc("领生");
            List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
            if (!CollectionUtils.isEmpty(yyxxDtos)) {
                real_yyxxDto = yyxxDtos.get(0);
            }

            int updateCount = 0;
            int insertCount = 0;
            int totalCount = 0;
            int alreadyCount = 0;
            List<String> unCompareInfo = new ArrayList<>();
            for (Map<String, Object> mapT : list) {
                totalCount++;
                SjxxDto sjxxDto = henuoToSjxx(mapT, sjdwxxlist, yblxList, jcxmList, jcdwList, real_yyxxDto, unCompareInfo);
                String person = "hospitalHenuo";
                if(map.get("HENUO_LRRY")!=null && StringUtil.isNotBlank(map.get("HENUO_LRRY").toString())) {
                    person = map.get("HENUO_LRRY").toString();
                }
                Map<String, Object> resultMap = dealSaveSjxxDto(sjxxDto, sjqfdm, kdlxid, sjqfid, person, JSON.toJSONString(mapT));
                if(resultMap.get("STATUS") != null && ("insert".equals(resultMap.get("STATUS")) || "update".equals(resultMap.get("STATUS")) || "exist".equals(resultMap.get("STATUS")))){
                    SjkzxxDto sjkzxxDto = new SjkzxxDto();
                    sjkzxxDto.setSjid(sjxxDto.getSjid());
                    sjkzxxDto.setQtxx(JSON.toJSONString(mapT));
                    sjkzxxDto.setXgry(person);
                    boolean result = sjkzxxService.update(sjkzxxDto);
                    if (!result){
                        sjkzxxDto.setLrry(person);
                        sjkzxxService.insertDto(sjkzxxDto);
                    }
                }
                String status = (String) resultMap.get("STATUS");
                switch (status) {
                    case "insert":
                        insertCount++;
                        break;
                    case "update":
                        updateCount++;
                        break;
                    case "exist":
                        alreadyCount++;
                        break;
                    default:
                        break;
                }
            }
            jkdymxDto.setQtxx("禾诺：获取样本信息成功-----结果：共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
            jkdymxDto.setFhnr(JSON.toJSONString(list).length()>4000?JSON.toJSONString(list).substring(0,4000):JSON.toJSONString(list)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
            log.error("禾诺：获取样本信息成功-----结果：共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
            sendMessage(unCompareInfo, null, null, "千麦对接系统，报告回传失败！");
        } else {
            //接口调用明细 -- 设置返回内容，返回结果
            jkdymxDto.setFhnr(JSON.toJSONString(list)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            log.error("禾诺：获取样本信息失败-----未获取到样本信息-----结果：" + JSON.toJSONString(list));
        }
    }

    /**
     * 禾诺获取样本信息
     *
     * @param barcode    中心条码 有禾诺条码时，时间不起作用
     * @param entBarcode 外送单位条码 有禾诺条码时，时间不起作用
     * @param startDate  开始时间 有禾诺条码时，开始时间不起作用
     * @param endDate    结束时间 有禾诺条码时，结束时间不起作用，时间差不超过3天
     * @return  List<Map<String, Object>> 禾诺信息
     */
    private List<Map<String, Object>> receiveInfoList(String barcode, String entBarcode, String startDate, String endDate) {
        Map<String, Object> requestEntrustModel = new HashMap<>();
        if (StringUtil.isNotBlank(barcode)) {
            requestEntrustModel.put("barcode", barcode);
        }
        if (StringUtil.isNotBlank(entBarcode)) {
            requestEntrustModel.put("entBarcode", entBarcode);
        }
        if (StringUtil.isNotBlank(startDate)) {
            requestEntrustModel.put("beginTime", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            requestEntrustModel.put("endTime", endDate);
        }
        String EntrustHosCode = map.get("HENUO_EntrustHosCode").toString();
        String HENUO_URL = map.get("HENUO_URL").toString();
        requestEntrustModel.put("entrustHosCode", EntrustHosCode);
        String token = getHenuoToken();
        String url = HENUO_URL + HENUO_INFO_GET_URL;
        //设置restTemplate请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", "Bearer " + token);
        HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(requestEntrustModel), requestHeaders);
        try {
            log.error("禾诺：获取样本信息-----请求参数：" + JSON.toJSONString(requestEntity));
            //接口调用明细 -- 设置请求内容
            jkdymxDto.setNr(JSON.toJSONString(requestEntrustModel));
            Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);
            if (result != null && (int) result.get("code") == 200 && (Boolean) result.get("success")) {
                log.error("禾诺：获取样本信息成功-----结果：" + JSON.toJSONString(result));
                jkdymxDto.setFhnr(JSON.toJSONString(result.get("data")).length()>4000?JSON.toJSONString(result.get("data")).substring(0,4000):JSON.toJSONString(result.get("data"))); // 返回内容
                jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                return (List<Map<String, Object>>) result.get("data");
            }
            jkdymxDto.setFhnr(JSON.toJSONString(result).length()>4000?JSON.toJSONString(result).substring(0,4000):JSON.toJSONString(result)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            log.error("禾诺：获取样本信息失败-----结果：" + JSON.toJSONString(result));
        } catch (RestClientException e) {
            jkdymxDto.setFhnr(JSON.toJSONString(e.toString())); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            log.error("禾诺：获取样本信息失败-----请求:" + url);
            log.error("禾诺：获取样本信息失败-----结果：" + e.toString());
        }
        return null;
    }

    /**
     * 禾诺信息转成sjxxDto
     *
     * @param mapT        禾诺信息
     * @param sjdwxxlist 医院信息
     * @param yblxList  样本类型
     * @param jcxmList  检测项目
     * @param jcdwList  检测单位
     * @param unCompareInfo 未匹配信息
     * @return sjxxDto
     */
    private SjxxDto henuoToSjxx(Map<String, Object> mapT, List<SjdwxxDto> sjdwxxlist, List<JcsjDto> yblxList, List<JcsjDto> jcxmList, List<JcsjDto> jcdwList, YyxxDto real_yyxxDto, List<String> unCompareInfo) {
        SjxxDto sjxxDto = new SjxxDto();
        sjxxDto.setWbbm(mapT.get("barcode").toString());
        //1、样本编号(必填)
        sjxxDto.setYbbh((String) mapT.get("barcode"));
        //2、患者姓名(必填)
        sjxxDto.setHzxm((String) mapT.get("patientName"));
        //3、性别(必填)(sjxx.xb	1男2女3未知)
        String GenderName = mapT.get("sex") != null ? (String) mapT.get("sex") : "";
        switch (GenderName) {
            case "男":
                sjxxDto.setXb("1");
                break;
            case "女":
                sjxxDto.setXb("2");
                break;
            case "1":
                sjxxDto.setXb("1");
                break;
            case "2":
                sjxxDto.setXb("2");
                break;
            default:
                sjxxDto.setXb("3");
                break;
        }
        //4、年龄(必填)(sjxx.nl + sjxx.nldw)
        String nl = mapT.get("age") != null ? mapT.get("age").toString() : "0";
        String ageType = mapT.get("ageType ") != null ? mapT.get("ageType ").toString() : "";
        String nldw;
        switch (ageType) {
            case "1":
                nldw = "岁";
                break;
            case "2":
                nldw = "月";
                break;
            case "3":
                nldw = "天";
                break;
            case "4":
                nldw = "小时";
                break;
            case "5":
                nldw = "分钟";
                break;
            case "6":
                nldw = "周";
                break;
            default:
                nldw = "";
                break;
        }
        sjxxDto.setNl(nl + nldw);//年龄 => sjxx.nl + sjxx.nldw
        //5、送检医生(必填)
        sjxxDto.setSjys(mapT.get("doctorName") != null ? mapT.get("doctorName").toString() : "-");
        //6、科室(必填)
        String ksmc = mapT.get("hosDept") != null ? mapT.get("hosDept").toString() : "";
        String qtsjdwid = "";
        for (SjdwxxDto sjdwxx : sjdwxxlist) {
            if (sjdwxx.getDwmc().equals(ksmc)) {
                sjxxDto.setKs(sjdwxx.getDwid());
                if ("1".equals(sjdwxx.getKzcs())) {
                    //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                    sjxxDto.setQtks(ksmc);
                    sjxxDto.setKsmc(ksmc);
                }
                break;
            } else if (StringUtil.isNotBlank(sjdwxx.getKzcs3())) {
                String[] ppkss = sjdwxx.getKzcs3().split(",");
                boolean isCompare = false;
                for (String ppks : ppkss) {
                    if (ppks.equals(ksmc)) {
                        sjxxDto.setKs(sjdwxx.getDwid());
                        if ("1".equals(sjdwxx.getKzcs())) {
                            //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                            sjxxDto.setQtks(ksmc);
                            sjxxDto.setKsmc(ksmc);
                        }
                        isCompare = true;
                        break;
                    }
                }
                if (isCompare) {
                    break;
                }
            }
            if ("00999".equals(sjdwxx.getDwdm())) {
                qtsjdwid = sjdwxx.getDwid();
            }
        }
        if (StringUtil.isBlank(sjxxDto.getKs())) {
            sjxxDto.setKs(qtsjdwid);
            sjxxDto.setQtks(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            sjxxDto.setKsmc(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            log.error("禾诺：处理送检信息-----科室信息未匹配到！------标本:" + sjxxDto.getYbbh() + "-----科室名称:" + (StringUtil.isNotBlank(ksmc) ? ksmc : "-"));
            if (StringUtil.isNotBlank(ksmc)) {
                //若科室名称为空白，则不加入钉钉消息中
                unCompareInfo.add((unCompareInfo.size() + 1) + "、禾诺,\n\n　　未匹配科室：" + (StringUtil.isNotBlank(ksmc) ? ksmc : "-") + ",\n\n　　标本:" + sjxxDto.getYbbh());
            }
        }
        //7、住院号
        sjxxDto.setZyh(mapT.get("patientNo") != null ? mapT.get("patientNo").toString() : "");
        //8、床位号
        sjxxDto.setCwh(mapT.get("bedNo") != null ? mapT.get("bedNo").toString() : "");
        //9、电话
        sjxxDto.setDh(mapT.get("patientPhone") != null ? mapT.get("patientPhone").toString() : "");
        //10、送检单位(必填)

        if(mapT.get("hospitalName")!=null && StringUtil.isNotBlank(mapT.get("hospitalName").toString())){
            YyxxDto dto = new YyxxDto();
            dto.setDwmc(mapT.get("hospitalName").toString());
            List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(dto);
            if (!CollectionUtils.isEmpty(yyxxDtos)) {
                sjxxDto.setSjdw(yyxxDtos.get(0).getDwid());
            }else {
                dto.setDwmc("第三方");
                yyxxDtos = yyxxService.queryByDwmc(dto);
                if (!CollectionUtils.isEmpty(yyxxDtos)) {
                    sjxxDto.setSjdw(yyxxDtos.get(0).getDwid());
                }
                sjxxDto.setSjdwmc(mapT.get("hospitalName").toString());
            }
        }else {
            sjxxDto.setSjdw(real_yyxxDto.getDwid());
            sjxxDto.setSjdwmc("-");
        }
        //12、合作伙伴(必填)
        if(map.get("HENUO_DB")!=null && StringUtil.isNotBlank(map.get("HENUO_DB").toString())){
            sjxxDto.setDb(map.get("HENUO_DB").toString());
        }else{
            String cskz3 = real_yyxxDto.getCskz3();
            if (StringUtil.isNotBlank(cskz3)) {
                String[] cskz3s = cskz3.split(",");
                sjxxDto.setDb(cskz3s[1]);
            } else {
                log.error("禾诺：处理送检信息-----合作伙伴信息未匹配到！------标本:" + sjxxDto.getYbbh());
            }
        }
        //13、检测项目(必填)
        String jcxmmc = "";
        String jcxmdm = "";
        if(map.get("HENUO_FLAG")!=null && "1".equals(map.get("HENUO_FLAG").toString())){
            if(mapT.get("sampleItems")!=null && StringUtil.isNotBlank(mapT.get("sampleItems").toString())){
                String listJson = JSONObject.toJSONString(mapT.get("sampleItems"));
                List<Map<String,Object>> list = JSON.parseObject(listJson,new TypeReference<List<Map<String,Object>>>(){});
                if(!CollectionUtils.isEmpty(list)) {
                    for (Map<String, Object> map1 : list) {
                        if(map1.get("applyItemName")!=null && StringUtil.isNotBlank(map1.get("applyItemName").toString())){
                            jcxmmc = jcxmmc + ","+ map1.get("applyItemName").toString();
                        }
                    }
                    if(StringUtil.isNotBlank(jcxmmc)) {
                        jcxmmc = jcxmmc.substring(1);
                        compareJcxm(sjxxDto,jcxmList,jcxmmc,jcxmmc,",",xxdyService);
                    }
                }
            }
        }else{
            jcxmdm = mapT.get("applyItemCodes") != null ? mapT.get("applyItemCodes").toString() : "";
            jcxmmc = mapT.get("applyItemNames") != null ? mapT.get("applyItemNames").toString() : "";
            if (StringUtil.isNotBlank(jcxmmc)) {
                jcxmmc = jcxmmc.replaceAll("，", ",");
                jcxmdm = jcxmdm.replaceAll("，", ",");
                String[] jcxmdms = jcxmdm.split(",");
                List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
                //匹配检测项目
                String jcxmpp = real_yyxxDto.getJcxmpp();//获取匹配到的医院的检测项目匹配字段
                for (String s : jcxmdms) {
                    String jcsjCskz3 = "IMP_REPORT_ONCO_QINDEX_TEMEPLATE";//默认Q_mNGS
                    String jcsjCskz1 = "";
                    if (StringUtil.isNotBlank(jcxmpp)) {
                        String jcxmppdm = "";
                        //若jcxmpp不为空
                        String[] pps = jcxmpp.split(";");
                        for (String pp : pps) {
                            String[] ppnr = pp.split(":");
                            if (s.equals(ppnr[0])) {
                                jcxmppdm = ppnr[1];
                                break;
                            }
                        }
                        if (StringUtil.isNotBlank(jcxmppdm)) {
                            String[] cskzs = jcxmppdm.split(",");
                            jcsjCskz3 = cskzs[0];
                            jcsjCskz1 = cskzs[1];
                        }
                        for (JcsjDto jc_jcxm : jcxmList) {
                            if (jcsjCskz3.equals(jc_jcxm.getCskz3())) {
                                SjjcxmDto sjjcxmDto = new SjjcxmDto();
                                if (jcsjCskz3.equals(jc_jcxm.getCskz3()) && jcsjCskz1.equals(jc_jcxm.getCskz1())) {
                                    sjjcxmDto.setJcxmid(jc_jcxm.getCsid());
                                    sjjcxmDtos.add(sjjcxmDto);
                                    sjxxDto.setSjjcxms(sjjcxmDtos);
                                    sjxxDto.setJcxmmc((StringUtil.isNotBlank(sjxxDto.getJcxmmc()) ? (sjxxDto.getJcxmmc() + ",") : "") + jc_jcxm.getCsmc());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        //若文字未匹配上，设置为默认检测项目
        if (StringUtil.isBlank(sjxxDto.getJcxmmc())) {
            log.error("禾诺：处理送检信息-----检测项目未匹配，请手动匹配！------标本:" + sjxxDto.getYbbh() + ",检测项目:" + jcxmdm + "," + jcxmmc);
        }
        //14、送检区分(必填)
        //15、样本类型(必填)
        String yblxmc = (String) mapT.get("sampleTypeName");
        compareYblx(sjxxDto,yblxList,yblxmc,redisUtil);
        //16、前期诊断(必填)
        sjxxDto.setQqzd(mapT.get("clinicalDiagnosis") != null ? mapT.get("clinicalDiagnosis").toString() : "-");
        //17、临床症状(必填)
        sjxxDto.setLczz("-");
        //18、采样时间(必填)
        sjxxDto.setCyrq(mapT.get("sampleCollectionTime") != null ? mapT.get("sampleCollectionTime").toString() : "");
        //19、检测单位(必填)(郑州实验室)
        String jcdw = "25";
        if(map.get("HENUO_DWDM")!=null && StringUtil.isNotBlank(map.get("HENUO_DWDM").toString())){
            jcdw = map.get("HENUO_DWDM").toString();
        }
        for (JcsjDto jc_jcdw : jcdwList) {
            if (jcdw.equals(jc_jcdw.getCsdm())) {
                sjxxDto.setJcdw(jc_jcdw.getCsid());
                sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                break;
            }
        }
        //20、备注
        String bz = mapT.get("sampleRemark") != null ? mapT.get("sampleRemark").toString() : "";
        sjxxDto.setBz(bz);
        return sjxxDto;
    }

    private void itemList(String localInterfaceCode) {
        Map<String, Object> requestEntrustModel = new HashMap<>();
        String HENUO_URL = map.get("HENUO_URL").toString();
        requestEntrustModel.put("localInterfaceCode", localInterfaceCode);
        String token = getHenuoToken();
        String url = HENUO_URL + HENUO_ITEMLIST_URL;
        //设置restTemplate请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", "Bearer " + token);
        HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(requestEntrustModel), requestHeaders);
        try {
            Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);
            if (result != null && (int) result.get("code") == 200 && (Boolean) result.get("success")) {
                log.error("禾诺：获取对照项目列表成功-----结果：" + JSON.toJSONString(result));
            }
            log.error("禾诺：获取对照项目列表失败-----结果：" + JSON.toJSONString(result));
        } catch (RestClientException e) {
            log.error("禾诺：获取对照项目列表失败-----请求:" + url);
            log.error("禾诺：获取对照项目列表失败-----结果：" + e.toString());
        }
    }

    //-------------------------------------------------------------------------------------------------//
    //-----------------------------------------以下为艾迪康接口方法-----------------------------------------//
    //-------------------------------------------------------------------------------------------------//


    /**
     * 艾迪康接口调用方法
     * method 调用方法
     */
    public void aidikangInfo() {
        Object methodObj = map.get("method");
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            String method = methodObj.toString();
            if ("uploadById".equals(method)) {
                // 1.0版:/api/report/reportfile/create
                returnAidikangReport(AIDIKANG_TOKEN_URL,AIDIKANG_REPORT_RETURN_URL);
            }
            if ("test".equals(method)){
                log.error("艾迪康：获取样本信息成功-----测试方法");
                getAidikangToken(AIDIKANG_TOKEN_URL);
            }
        } else {
            log.error("艾迪康：获取样本信息成功-----未传入调用方法名");
        }
    }
    /**
     * 艾迪康接口调用方法
     * method 调用方法
     */
    public void aidikangNewInfo() {
        Object methodObj = map.get("method");
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            String method = methodObj.toString();
            if ("uploadById".equals(method)) {
                // 2.0版:/api-sample/entrust/open/result
                returnAidikangReport(AIDIKANG_NEW_TOKEN_URL, AIDIKANG_NEW_REPORT_RETURN_URL);
            }
        } else {
            log.error("艾迪康：获取样本信息成功-----未传入调用方法名");
        }
    }
    /**
     * 艾迪康接口调用方法
     * method 调用方法
     */
    public void aidikangNewNewInfo() {
        Object methodObj = map.get("method");
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            String method = methodObj.toString();
            if ("uploadById".equals(method)) {
                // 3.0版:/api-sample/external/createReport
                returnAidikangReport(AIDIKANG_NEW_TOKEN_URL, AIDIKANG_NEWNEW_REPORT_RETURN_URL);
            }
        } else {
            log.error("艾迪康：获取样本信息成功-----未传入调用方法名");
        }
    }

    /**
     * 艾迪康获取授权Token
     *
     * @return token
     */
    private String getAidikangToken(String TOKEN_URL) {
        return getAidikangToken(TOKEN_URL,false);
    }

    /**
     * 艾迪康获取授权Token
     *
     * @return token
     */
    private String getAidikangToken(String TOKEN_URL,boolean isRefresh) {
        if (!isRefresh) {
            if (AIDIKANG_TOKEN_URL.equals(TOKEN_URL)){
                Object aidikangToken = redisUtil.get("AIDIKANG_TOKEN");
                if (aidikangToken != null) {
                    return (String) aidikangToken;
                }
            } else {
                Object aidikangToken = redisUtil.get("AIDIKANG_TOKEN_NEW");
                if (aidikangToken != null) {
                    return (String) aidikangToken;
                }
            }
        }
        String AIDIKANG_URL = map.get("AIDIKANG_URL").toString();
        String USERNAME = map.get("AIDIKANG_USERNAME").toString();
        String PASSWORD = map.get("AIDIKANG_PASSWORD").toString();
        String url = AIDIKANG_URL + TOKEN_URL;
        SslUtils factory = new SslUtils();
        factory.setReadTimeout(60000);
        factory.setConnectTimeout(30000);
        RestTemplate restTemplate = new RestTemplate(factory);
        Map<String, Object> result = null;
        try {
            if (AIDIKANG_TOKEN_URL.equals(TOKEN_URL)){
                String apiUserCode = "userName=" + USERNAME;
                String apiPassword = "userPwd=" + PASSWORD;
                url = url + "?" + apiUserCode + "&" + apiPassword;
                result = restTemplate.postForObject(url, null, Map.class);
            } else {
                Map<String, String> requestEntrustModel = new HashMap<>();
                requestEntrustModel.put("userName",USERNAME);
                requestEntrustModel.put("userPwd",PASSWORD);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity requestEntity = new HttpEntity(requestEntrustModel, requestHeaders);
                result = restTemplate.postForObject(url, requestEntity, Map.class);
            }
        } catch (RestClientException e) {
            log.error("艾迪康：获取token失败-----请求:" + url);
            log.error("艾迪康：获取token失败-----结果：" + e.getMessage());
        }
        if (result != null && ((int) result.get("code") == 200 || "操作成功".equals((String) result.get("msg")))) {
            log.error("艾迪康：获取token成功-----结果：" + JSON.toJSONString(result));
            LinkedHashMap<String,String> data = (LinkedHashMap<String,String>) result.get("data");
            String accessToken = data.get("access_token");

            if (AIDIKANG_TOKEN_URL.equals(TOKEN_URL)){
                redisUtil.set("AIDIKANG_TOKEN", accessToken, 1500);
            } else {
                redisUtil.set("AIDIKANG_TOKEN_NEW", accessToken, 1500);
            }
            return accessToken;
        }
        log.error("艾迪康：获取token失败-----请求 return:" + url);
        log.error("艾迪康：获取token失败-----结果 return：" + JSON.toJSONString(result));
        return null;
    }

    /**
     * 回传报告给艾迪康
     * @return boolean 是否成功
     */
    private void returnAidikangReport(String TOKEN_URL, String REPORT_RETURN_URL) {
        String AIDIKANG_URL = map.get("AIDIKANG_URL").toString();
        String token = getAidikangToken(TOKEN_URL);
        int cnt =0;
        while(StringUtil.isBlank(token)) {
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				log.error("returnAidikangReport token 获取时sleep出错。");
			}
        	cnt ++;
        	if(cnt > 10) {
                log.error("艾迪康：多次循环获取token失败-----请求:" + REPORT_RETURN_URL);
                return;
        	}
        	token = getAidikangToken(TOKEN_URL);
        	
        }
        String url = AIDIKANG_URL + REPORT_RETURN_URL;
        String logSample = "样本信息："+map.get("barcode") + map.get("patientname");
        try {
            Map<String,Object> requestEntrustMap = new HashMap<>();
            MultiValueMap<String, Object> requestEntrustModel = new LinkedMultiValueMap<>();
            requestEntrustModel.add("businessclass",map.get("AIDIKANG_BUSINESSCLASS"));//根据具体业务分配，通用代码为ADC_BC0000,联系技术索要编号(必填)
            requestEntrustMap.put("businessclass",map.get("AIDIKANG_BUSINESSCLASS"));//根据具体业务分配，通用代码为ADC_BC0000,联系技术索要编号(必填)
            requestEntrustModel.add("orgcode",map.get("AIDIKANG_ORGCODE"));//委托机构代号,联系技术索要编号(必填)
            requestEntrustMap.put("orgcode",map.get("AIDIKANG_ORGCODE"));//委托机构代号,联系技术索要编号(必填)
            requestEntrustModel.add("barcode",map.get("barcode"));//委托方条码(必填)
            requestEntrustMap.put("barcode",map.get("barcode"));//委托方条码(必填)
            requestEntrustModel.add("patientname",map.get("patientname"));//报告患者姓名(非必填)
            requestEntrustMap.put("patientname",map.get("patientname"));//报告患者姓名(非必填)
            requestEntrustModel.add("extbarcode",map.get("extbarcode"));//检测机构项目编号(必填)
            requestEntrustMap.put("extbarcode",map.get("extbarcode"));//检测机构项目编号(必填)
            requestEntrustModel.add("reporttime",map.get("reporttime"));//报告发布时间(必填)
            requestEntrustMap.put("reporttime",map.get("reporttime"));//报告发布时间(必填)
            requestEntrustModel.add("testman",map.get("testman"));//检测者姓名(必填)
            requestEntrustMap.put("testman",map.get("testman"));//检测者姓名(必填)
            requestEntrustModel.add("checkman",map.get("checkman"));//审核者姓名(必填)
            requestEntrustMap.put("checkman",map.get("checkman"));//审核者姓名(必填)
            if(map.get("reporttime")!=null && !map.get("reporttime").equals(map.get("scbgrq"))) {
                requestEntrustModel.add("checkflag", "1");//默认为0；0-初次发布；1-重新发布(非必填)
                requestEntrustMap.put("checkflag","1");//默认为0；0-初次发布；1-重新发布(非必填)
            }
            else {
                requestEntrustModel.add("checkflag", "0");//默认为0；0-初次发布；1-重新发布(非必填)
                requestEntrustMap.put("checkflag","0");//默认为0；0-初次发布；1-重新发布(非必填)
            }
            requestEntrustModel.add("filename",map.get("filename"));//仅文件名不带后缀.pdf，如重新发布文件名须和之前发布过的相同(必填)
            requestEntrustMap.put("filename",map.get("filename"));//仅文件名不带后缀.pdf，如重新发布文件名须和之前发布过的相同(必填)
            //设置restTemplate请求头
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity requestEntity = null;
            requestHeaders.set("Authorization", "Bearer " + token);
            if (AIDIKANG_NEW_REPORT_RETURN_URL.equals(REPORT_RETURN_URL)){
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                // 将文件转成bas64存入pdf字段
                requestEntrustMap.put("pdf", getPdfMsg(map.get("wjlj").toString()));
                List<Map<String,Object>> list = new ArrayList<>();
                Map<String,Object> listmap = new HashMap<>();
                listmap.put("barcode",map.get("barcode"));
                listmap.put("itemCode",map.get("extbarcode"));
                listmap.put("testResult",map.get("testResult"));
                listmap.put("ResultHint",map.get("ResultHint"));
                listmap.put("resultReference","");
                listmap.put("resultReference_Critical","");
                listmap.put("unitName","");
                listmap.put("testMethod","");
                //新接口,需返回sampleItem
                requestEntrustMap.put("sampleItem",JSON.toJSONString(list));
                requestEntity = new HttpEntity(requestEntrustMap, requestHeaders);
            } else {
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                DBEncrypt dbEncrypt = new DBEncrypt();
                String wjlj = dbEncrypt.dCode(map.get("wjlj").toString());
                File file = new File(wjlj);
                MultiValueMap<String,String> fileMap = new LinkedMultiValueMap<>();
                ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name("pdf").filename(file.getName()).build();
                fileMap.add(HttpHeaders.CONTENT_DISPOSITION,contentDisposition.toString());
                byte[] fileBytes = Files.readAllBytes(Paths.get(wjlj));
                HttpEntity<byte[]> fileEntity = new HttpEntity<>(fileBytes,fileMap);
                requestEntrustModel.add(contentDisposition.toString(),fileEntity);//报告文件 File(必填)
                requestEntity = new HttpEntity(requestEntrustModel, requestHeaders);
            }
            SslUtils factory = new SslUtils();
            factory.setReadTimeout(60000);
            factory.setConnectTimeout(30000);
            RestTemplate restTemplate = new RestTemplate(factory);

            Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);

            log.error("艾迪康：回传报告-----结果：" + url + "  token:" + token + "  " + logSample + "    " + JSON.toJSONString(result));
            if (JSON.toJSONString(result).contains("token错误") || JSON.toJSONString(result).contains("token失效")){
                getAidikangToken(TOKEN_URL,true);
                returnAidikangReport(TOKEN_URL, REPORT_RETURN_URL);
                return;
            }
            //将msg存储到文件中
            String filePath = mkDirs(BusTypeEnum.IMP_REPORT_AIDIKANG_XML.getCode(), "/matridx/fileupload/temp/");
            String s_barcode = "";
            if(map.get("barcode")!=null)
                s_barcode = map.get("barcode").toString();
            String fileName = "aidikang" + "_" + s_barcode + "_" + System.currentTimeMillis() + ".xml";
            String msg = JSON.toJSONString(requestEntity);
            FileUtils.writeStringToFile(new File(filePath + fileName), msg);
        } catch (RestClientException e) {
            log.error("艾迪康：回传报告失败-----结果：" + url + "  token:" + token + "    " + logSample + "    " + e.getMessage());
        }
        catch (IOException e) {
            log.error("艾迪康：回传报告数据备份失败-----结果：" + url + "  token:" + token + "    " + logSample + "    " + e.getMessage());
        }
    }

    //-------------------------------------------------------------------------------------------------//
    //--------------------------以下为广州创惠信息科技有限公司医院检验项目外送接口方法---------------------------//
    //------------------------------------------烟台毓璜顶医院-------------------------------------------//
    //-------------------------------------------------------------------------------------------------//
    public Map<String, Object> yuhuangdingInfo(){
        Map<String, Object> returnMap = new HashMap<>();
        Object methodObj = map.get("method");
        String method = "";
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            method = methodObj.toString();
            log.error("调用毓璜顶医院接口------yuhuangdingInfo：" + method);
            try {
                if ("selectById".equals(method)) {
                    returnMap = generateYuhuangdingAreaMsg("GetLisRequest", "GetLisRequestResponse", "hospSampleID", map);
                    returnMap.put("status","success");
                } else if ("uploadById".equals(method)) {
                    jkdymxDto.setDydz("yuhuangding."+method);
                    returnMap = generateYuhuangdingAreaMsg("UploadLisRepData", "UploadLisRepDataResponse", "reportResult", map);
                    jkdymxDto.setFhnr(JSON.toJSONString(returnMap).length()>4000?JSON.toJSONString(returnMap).substring(0,4000):JSON.toJSONString(returnMap)); // 返回内容
                    jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                    jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
                    log.error("调用毓璜顶医院接口------上传报告数据--样本编号：" + map.get("ybbh") + "--上传结果：" + JSON.toJSONString(returnMap));
                    jkdymxService.insertJkdymxDto(jkdymxDto);
                } else {
                    log.error("调用毓璜顶医院接口------调用方法名出错method：" + method);
                    returnMap.put("status", "fail");
                    returnMap.put("message", "失败!调用方法名出错method：" + method);
                }
                return returnMap;
            } catch (Exception e) {
                log.error("调用毓璜顶医院接口------" + e.getMessage());
                if (method.equals("uploadById")) {
                    reSendRabbit(map,e.getMessage());
                    log.error("调用毓璜顶医院接口：回传报告失败-----重新发送rabbit!");
                }
                returnMap.put("status", "fail");
                returnMap.put("message", "失败!"+e.getMessage());
                return returnMap;

            }
        } else {
            log.error("调用毓璜顶医院接口------未传入调用方法名");
            returnMap.put("status", "fail");
            returnMap.put("message", "失败!未传入调用方法名!");
            return returnMap;
        }
    }
    public Map<String, Object> yuhuangdingInfo(Map<String, Object> yuhuangdingMap){
        this.map = yuhuangdingMap;
        log.error("调用毓璜顶医院接口------调用参数:---param:" + JSON.toJSONString(yuhuangdingMap));
        return yuhuangdingInfo();
    }

    /**
     * 生成请求体,并发送请求
     * @param methodName
     * @param responseName
     * @param bodyElementName
     * @param sampleMap
     * @return
     * @throws Exception
     */
    private Map<String, Object> generateYuhuangdingAreaMsg(String methodName, String responseName, String bodyElementName, Map<String, Object> sampleMap) throws Exception {
        // 1.create service
        // 毓璜顶配置信息
        String nameSpace = "http://tempuri.org/";
        String serviceName = "OutSideService";
        String portName = "OutSideServiceSoap12";

        String yuhuangding_wsdlurl = "http://123.130.112.90:8032/OutSideService.asmx?WSDL";
        if (map!=null&&map.get("yuhuangding_wsdlurl") != null && StringUtil.isNotBlank((String) map.get("yuhuangding_wsdlurl"))){
            yuhuangding_wsdlurl = map.get("yuhuangding_wsdlurl").toString();
        }
        log.error("调用毓璜顶医院接口------url:" + yuhuangding_wsdlurl + " methodName:" + methodName + " json:" + JSON.toJSONString(sampleMap));
        // 1.create service
        URL url = new URL(yuhuangding_wsdlurl);
        QName sname = new QName(nameSpace, serviceName);
        javax.xml.ws.Service service = javax.xml.ws.Service.create(url, sname);
        // 2.create Dispatch object
        Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, javax.xml.ws.Service.Mode.MESSAGE);

        // 3.create SOAPMessage
        SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
        msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

        // 3.1 create SOAPEnvelopedc
        SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
        envelope.setPrefix("soap12");
        envelope.addNamespaceDeclaration("tem", nameSpace);
        envelope.removeAttribute("xmlns:env");

        // 3.3 create SOAPBody
        SOAPBody body = envelope.getBody();
        body.setPrefix("soap12");
        SOAPBodyElement callAreaInterfaceBody = body.addBodyElement(new QName(nameSpace, methodName, "tem"));
        SOAPElement bodyElement = callAreaInterfaceBody.addChildElement(new QName(nameSpace, bodyElementName, "tem"));
        if ("GetLisRequest".equals(methodName)) {
            String hospSampleID = (String) sampleMap.get("hospSampleID");
            hospSampleID = "<![CDATA[" + hospSampleID + "]]>";
            bodyElement.setValue(hospSampleID);
        } else if ("UploadLisRepData".equals(methodName)) {
            generateYuhuangdingBody(bodyElement, sampleMap);
            //将msg存储到文件中
            String filePath = mkDirs(BusTypeEnum.IMP_REPORT_YUHUANGDING_XML.getCode(), "/matridx/fileupload/temp/");
            String fileName = "yuhuangding_" + sampleMap.get("ybbh") + "_" + System.currentTimeMillis() + ".xml";
            //将msg存储到文件中
            msg.writeTo(new FileOutputStream(filePath + "/" + fileName));
            
            log.error("调用毓璜顶医院接口------结果回传:" + sampleMap.get("ybbh") + " fileName:" + fileName);
        }
        // set timeout
        SOAPMessage response = dispatch.invoke(msg);
        // 4. get response and transfer to dom object
        org.w3c.dom.Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();

        NodeList nodes = doc.getElementsByTagName(responseName);
        if (nodes.getLength() > 0) {
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            log.error("调用毓璜顶医院接口------返回结果:{}", result);
            if ("UploadLisRepData".equals(methodName)) {
                if (StringUtil.isNotBlank(result) && !result.contains("success")){
                    throw new BusinessException("回传报告结果未提示success!"+result);
                }else {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("result",result);
                    return resultMap;
                }
            } else {
                if (StringUtil.isBlank(result)){
                    return null;
                }
                return getResultXmlMsg(result);
            }
        }
        log.error("调用毓璜顶医院接口------未获取到响应结果");
        return null;
    }

    /**
     * 组装请求体
     * @param bodyElement
     * @param sampleMap
     * @throws SOAPException
     */
    private void generateYuhuangdingBody(SOAPElement bodyElement,Map<String,Object> sampleMap) throws SOAPException {
        Document document = DocumentHelper.createDocument();
        SjxxDto sjxxDto = new SjxxDto();
        sjxxDto.setSjid((String) sampleMap.get("sjid"));
        Map<String, String> dtoMap = sjxxService.getDtoMap(sjxxDto);
        Element Report_Result = document.addElement("Report_Result");
        Element Report_Info = Report_Result.addElement("Report_Info");
        Element ext_lab_code = Report_Info.addElement("ext_lab_code");
        ext_lab_code.setText((String) sampleMap.get("wbbm"));// 第三方检验中心编码
        Element lis_Barcode = Report_Info.addElement("lis_Barcode");
        lis_Barcode.setText((String) sampleMap.get("ybbh"));// 医院条码
        Element ext_Barcode = Report_Info.addElement("ext_Barcode");
        ext_Barcode.setText((String) sampleMap.get("wbbm"));// 第三方检验中心条码
        Element ext_checkItem = Report_Info.addElement("ext_checkItem");
        ext_checkItem.setText(dtoMap.get("jcxmmc"));// 第三方检验中心检验项目
        Element pat_name = Report_Info.addElement("pat_name");
        pat_name.setText(dtoMap.get("hzxm"));// 姓名
        Element pat_age = Report_Info.addElement("pat_age");
        pat_age.setText(dtoMap.get("nl"));// 年龄
        Element pat_id = Report_Info.addElement("pat_id");
        if (StringUtil.isNotBlank(dtoMap.get("zhy"))){
            pat_id.setText(dtoMap.get("zhy"));// 住院号/门诊号/体检号
        }
        Element pat_bedNo = Report_Info.addElement("pat_bedNo");
        if (StringUtil.isNotBlank(dtoMap.get("cwh"))){
            pat_bedNo.setText(dtoMap.get("cwh"));// 床号/房号
        }
        Element pat_tel = Report_Info.addElement("pat_tel");
        if (StringUtil.isNotBlank(dtoMap.get("dh"))){
            pat_tel.setText(dtoMap.get("dh"));// 联系电话
        }
        Element pat_sex = Report_Info.addElement("pat_sex");
        if (StringUtil.isNotBlank(dtoMap.get("xb"))){
            pat_sex.setText(dtoMap.get("xb"));// 性别
        }
        Element sam_name = Report_Info.addElement("sam_name");
        if (StringUtil.isNotBlank(dtoMap.get("yblxmc"))){
            sam_name.setText(dtoMap.get("yblxmc"));// 标本名称
        }
        Element doctor_name = Report_Info.addElement("doctor_name");
        if (StringUtil.isNotBlank(dtoMap.get("sjys"))){
            doctor_name.setText(dtoMap.get("sjys"));// 申请医生
        }
        Element dept_name = Report_Info.addElement("dept_name");
        if (StringUtil.isNotBlank(dtoMap.get("qtks"))){
            dept_name.setText(dtoMap.get("qtks"));// 申请科室
        }
        Element clinical_diag = Report_Info.addElement("clinical_diag");
        if (StringUtil.isNotBlank(dtoMap.get("qqzd"))){
            clinical_diag.setText(dtoMap.get("qqzd"));// 临床诊断
        }
        Element ext_receive_tim = Report_Info.addElement("ext_receive_tim");
        if (StringUtil.isNotBlank(dtoMap.get("mjsrq"))){
            ext_receive_tim.setText(dtoMap.get("mjsrq"));// 第三方检验中心样本收取时间(2013-07-29 14:11:11)
        }
        Element ext_check_time = Report_Info.addElement("ext_check_time");
        if (StringUtil.isNotBlank(dtoMap.get("msyrq"))){
            ext_check_time.setText(dtoMap.get("msyrq"));// 第三方检验中心检验时间(2013-07-29 14:11:11)
        }
        Element ext_upload_time = Report_Info.addElement("ext_upload_time");
        if (StringUtil.isNotBlank(dtoMap.get("mbgrq"))){
            ext_upload_time.setText(dtoMap.get("mbgrq"));// 第三方检验中心报告数据上传时间(2013-07-29 14:11:11)
        }
        Report_Info.addElement("ext_first_audit_time");// 第三方检验中心一审时间(2013-07-29 14:11:11)
        Report_Info.addElement("ext_second_audit_time");// 第三方检验中心二审时间(2013-07-29 14:11:11)
        Report_Info.addElement("pat_height");// 身高
        Report_Info.addElement("pat_wight");// 体重
        Report_Info.addElement("pat_pre_week");// 孕周
        Report_Info.addElement("pat_birthday");// 出生时间(2013-07-29 14:11:11)
        Report_Info.addElement("pat_ori_name");// 病人来源(住院/门诊/体检)
        Report_Info.addElement("sam_state");// 标本情况
        Report_Info.addElement("ext_check_ID");// 第三方检验中心检查号
        Report_Info.addElement("ext_report_suggestion");// 第三方检验中心报告建议
        Report_Info.addElement("ext_report_remark");// 第三方检验中心报告备注
        Element ext_checker = Report_Info.addElement("ext_checker");// 第三方检验中心检验人
        if (StringUtil.isNotBlank((String) sampleMap.get("jyryzsxm"))) {
            ext_checker.setText((String) sampleMap.get("jyryzsxm"));// 第三方检验中心检验人
        }
        Report_Info.addElement("ext_first_audit");// 第三方检验中心一审人
        if (StringUtil.isNotBlank((String) sampleMap.get("shryzsxm"))) {
            ext_checker.setText((String) sampleMap.get("shryzsxm"));// 第三方检验中心一审人
        }
        Report_Info.addElement("ext_second_audit");// 第三方检验中心二审人
        Report_Info.addElement("ext_intstrmt_name");// 第三方检验中心检验仪器名称
        Element ext_lab_name = Report_Info.addElement("ext_lab_name");// 第三方检验中心主检实验室名称
        ext_lab_name.setText(dtoMap.get("jcdwmc"));
        Report_Info.addElement("ext_report_type");// 第三方检验中心报告类型
        Report_Info.addElement("ext_report_code");// 第三方检验中心报告单编码
        Element result_info = Report_Info.addElement("result_info");
        result_info.addElement("ext_report_code");// 排序号
        result_info.addElement("ext_combine_name");// 第三方检验中心组合名称
        result_info.addElement("ext_combine_code");// 第三方检验中心组合编码
        result_info.addElement("ext_item_name");// 第三方检验中心项目名称
        result_info.addElement("ext_item_code");// 第三方检验中心项目编码
        result_info.addElement("result");// 结果
        result_info.addElement("result_unit");// 结果单位
        result_info.addElement("result_flag");// 结果提示
        result_info.addElement("result_reference");// 参考值
        result_info.addElement("result_date");// 结果时间(2013-07-29 14:11:11)
        result_info.addElement("result_test_method");// 实验方法
        result_info.addElement("result_suggestion");// 建议与解释
        result_info.addElement("result_remark");// 结果备注
        result_info.addElement("lis_combine_name");// lis组合名称
        result_info.addElement("lis_combine_code");// lis组合编码
        result_info.addElement("lis_item_name");// lis项目名称
        result_info.addElement("lis_item_code");// lis项目编码
        Element result_detail_info = result_info.addElement("result_detail_info");// 从属项目结果表
        result_detail_info.addElement("ext_detail_item");// 第三方检验中心从属项目
        result_detail_info.addElement("ext_detail_code");// 第三方检验中心从属项目编码
        result_detail_info.addElement("detail_result_seq");// 排序号
        result_detail_info.addElement("detail_result");// 结果
        result_detail_info.addElement("detail_result_remark");// 备注
        Element result_pic = result_info.addElement("result_pic");// 结果图片
        result_pic.addElement("pic_content");// 结果图片内容(base64)
        result_pic.addElement("pic_seq");// 排序号
        result_pic.addElement("pic_name");// 结果图片名称

        Element report_pic = Report_Info.addElement("report_pic");
        Element pic_content = report_pic.addElement("pic_content");// 结果图片内容(base64)
        pic_content.setText(getPdfMsg((String) sampleMap.get("wjlj")));
        Element pic_name = report_pic.addElement("pic_name");// 报告图片名称
        pic_name.setText((String) sampleMap.get("wjm"));
        Element pic_seq = report_pic.addElement("pic_seq");// 排序号
        pic_seq.setText("1");

        String bodyxml = "<![CDATA[" + Report_Result.asXML() + "]]>";
        log.error("调用毓璜顶医院接口------结果回传:" + sampleMap.get("ybbh") + " -----XML信息：" + bodyxml);
        bodyElement.setValue(bodyxml);
    }

    /**
     * 将键值放在 map 中
     *
     * @param itemList
     * @param infoMap
     * @param fatherStr 父级力量
     */
    private void putKeyValueInMap(List<Map<String, Object>> itemList, Map<String, Object> infoMap,String fatherStr) {
    	if(itemList==null)
    		return;
        for (Map<String, Object> key : itemList) {
            Object[] keys = key.keySet().toArray();
            for (Object k : keys) {
                String mapKey = (StringUtil.isNotBlank(fatherStr)?(fatherStr+"."):"") + k.toString();
                if (key.get(k.toString()) != null) {
                    if (key.get(k.toString()) instanceof String){
                        infoMap.put(mapKey, key.get(k.toString()));
                    } else if (key.get(k.toString()) instanceof List){
                        List<Map<String, Object>> list = (List<Map<String, Object>>) key.get(k.toString());
                        putKeyValueInMap(list, infoMap, mapKey);
                    }
                }
            }
        }
    }

    /**
     * 处理毓璜顶 数据
     *
     * @param result           结果
     * @param tmpRedisUtil     tmp redis util
     * @param tmpSjxxService   TMP SJXX 服务
     * @param tmpSjkzxxService TMP SJKZXX 服务
     * @param tmpYyxxService   TMP YYXX 服务
     * @param tmpSjdwxxService TMP SJDWXX 服务
     * @param tmpXxdyService   TMP XXDY 服务
     * @return {@link SjxxDto }
     */
    public SjxxDto dealYuhuangdingInfo(Map<String, Object> result, RedisUtil tmpRedisUtil, ISjxxService tmpSjxxService, ISjkzxxService tmpSjkzxxService, IYyxxService tmpYyxxService, ISjdwxxService tmpSjdwxxService, IXxdyService tmpXxdyService) {
        Map<String, Object> infoMap = new HashMap<>();
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) result.get("Data_Row");
        putKeyValueInMap(itemList, infoMap,null);
        SjxxDto sjxxDto = new SjxxDto();
        List<SjdwxxDto> sjdwxxlist = tmpSjxxService.getSjdw();//科室信息List
        List<JcsjDto> yblxList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//样本类型List
        List<JcsjDto> jcxmList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
        List<JcsjDto> jcdwList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
        List<JcsjDto> kdlxList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
        List<JcsjDto> sjqfList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
        // 设置快递类型为无
        for (JcsjDto kdlx : kdlxList) {
            if ("QYY".equals(kdlx.getCsdm())) {
                sjxxDto.setKdlx(kdlx.getCsid());
                sjxxDto.setKdh("无");
                break;
            }
        }
        // 设置送检区分为入院
        for (JcsjDto sjqf : sjqfList) {
            if ("特检".equals(sjqf.getCsmc())) {
                sjxxDto.setSjqf(sjqf.getCsid());
                break;
            }
        }
        // 设置检测单位(必填)(北京实验室2)
        for (JcsjDto jc_jcdw : jcdwList) {
            if ("11".equals(jc_jcdw.getCsdm())) {
                sjxxDto.setJcdw(jc_jcdw.getCsid());
                sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                break;
            }
        }
        // 设置送检单位(必填)
        YyxxDto yyxxDto = new YyxxDto();
        YyxxDto real_yyxxDto = new YyxxDto();
        yyxxDto.setDwmc("烟台毓璜顶医院");
        List<YyxxDto> yyxxDtos = tmpYyxxService.queryByDwmc(yyxxDto);
        if (!CollectionUtils.isEmpty(yyxxDtos)) {
            real_yyxxDto = yyxxDtos.get(0);
        }
        sjxxDto.setSjdwbj(real_yyxxDto.getCskz1());
        sjxxDto.setSjdw(real_yyxxDto.getDwid());
        sjxxDto.setSjdwmc(real_yyxxDto.getDwmc());
        sjxxDto.setHospitalname(real_yyxxDto.getDwmc());
        // 设置合作伙伴(必填)(济南艾迪康毓璜顶定制)
        String cskz3 = real_yyxxDto.getCskz3();
        if (StringUtil.isNotBlank(cskz3)) {
            String[] cskz3s = cskz3.split(",");
            sjxxDto.setDb(cskz3s[1]);
        } else {
            sjxxDto.setDb("济南艾迪康毓璜顶定制");
        }
        // 设置样本体积
        sjxxDto.setYbtj("-");
        // 设置临床症状
        sjxxDto.setLczz("-");
        // 1、医院条码 => wbbm、ybbh
        sjxxDto.setYbbh(infoMap.get("lis_Barcode").toString());
        // 2、病人id(住院号/门诊号/体检号) => zyh
        sjxxDto.setZyh(infoMap.get("pat_id").toString());
        // 3、病人姓名 => hzxm
        sjxxDto.setHzxm(infoMap.get("pat_name").toString());
        // 4、病床号 => cwh
        String bedNo = StringUtil.isNotBlank((String)infoMap.get("pat_bedNo"))?(String)infoMap.get("pat_bedNo"):(StringUtil.isNotBlank((String)infoMap.get("pat_bedno"))?(String)infoMap.get("pat_bedno"):"");
        sjxxDto.setCwh(bedNo);
        // 5、采样时间 => cyrq
        String cyrq = infoMap.get("blood_time").toString();
        if (StringUtil.isBlank(cyrq)){
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cyrq = currentDate.format(formatter);
        }
        sjxxDto.setCyrq(cyrq);
        // 6、性别(必填)(sjxx.xb	1男2女3未知)
        String pat_sex = infoMap.get("pat_sex") != null ? (String) infoMap.get("pat_sex") : "";
        switch (pat_sex) {
            case "1":
                sjxxDto.setXb("1");
                break;
            case "2":
                sjxxDto.setXb("2");
                break;
            default:
                sjxxDto.setXb("3");
                break;
        }
        // 7、出生日期
        // infoMap.get("pat_birthday").toString();
        // 8、年龄 => nl
        sjxxDto.setNl(infoMap.get("pat_age").toString());
        // 9、年龄单位 => nldw
        String pat_ageUnit = infoMap.get("pat_ageunit") != null ? (String) infoMap.get("pat_ageunit") : "0";
        switch (pat_ageUnit){
            case "0":
                sjxxDto.setNldw("岁");
                break;
            case "1":
                sjxxDto.setNldw("个月");
                break;
            case "2":
                sjxxDto.setNldw("天");
                break;
            case "3":
                sjxxDto.setNldw("小时");
                break;
            case "4":
                sjxxDto.setNl((StringUtil.isNotBlank(sjxxDto.getNl())?sjxxDto.getNl():"")+"成人");
                break;
            case "5":
                sjxxDto.setNl((StringUtil.isNotBlank(sjxxDto.getNl())?sjxxDto.getNl():"")+"儿童");
                break;
            case "6":
                sjxxDto.setNl((StringUtil.isNotBlank(sjxxDto.getNl())?sjxxDto.getNl():"")+"新生儿");
                break;
            case "7":
                sjxxDto.setNldw("分钟");
                break;
            default:
                sjxxDto.setNldw("岁");
                break;
        }
        // 10、病人电话 => dh
        sjxxDto.setDh(infoMap.get("pat_tel").toString());
        // 11、送检科室 => ks、qtks
        String dept_name = infoMap.get("dept_name") != null ? infoMap.get("dept_name").toString() : "";
        String qtsjdwid = "";
        for (SjdwxxDto sjdwxx : sjdwxxlist) {
            if (sjdwxx.getDwmc().equals(dept_name)) {
                sjxxDto.setKs(sjdwxx.getDwid());
                if ("1".equals(sjdwxx.getKzcs())) {
                    //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                    sjxxDto.setQtks(dept_name);
                    sjxxDto.setKsmc(dept_name);
                }
                break;
            } else if (StringUtil.isNotBlank(sjdwxx.getKzcs3())) {
                String[] ppkss = sjdwxx.getKzcs3().split(",");
                boolean isCompare = false;
                for (String ppks : ppkss) {
                    if (ppks.equals(dept_name)) {
                        sjxxDto.setKs(sjdwxx.getDwid());
                        if ("1".equals(sjdwxx.getKzcs())) {
                            //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                            sjxxDto.setQtks(dept_name);
                            sjxxDto.setKsmc(dept_name);
                        }
                        isCompare = true;
                        break;
                    }
                }
                if (isCompare) {
                    break;
                }
            }
            if ("00999".equals(sjdwxx.getDwdm())) {
                qtsjdwid = sjdwxx.getDwid();
            }
        }
        if (StringUtil.isBlank(sjxxDto.getKs())) {
            sjxxDto.setKs(qtsjdwid);
            sjxxDto.setQtks(StringUtil.isNotBlank(dept_name) ? dept_name : "-");
            sjxxDto.setKsmc(StringUtil.isNotBlank(dept_name) ? dept_name : "-");
            log.error("创惠：处理送检信息-----科室信息未匹配到！------标本:" + sjxxDto.getYbbh() + "-----科室名称:" + (StringUtil.isNotBlank(dept_name) ? dept_name : "-"));
        }
        // 12、送检医生 => sjys
        sjxxDto.setSjys(infoMap.get("doctor_name").toString());
        // 13、送检医生电话 => ysdh
        sjxxDto.setYsdh(infoMap.get("doctor_tel").toString());
        // 14、临床诊断 => qqzd
        sjxxDto.setQqzd(infoMap.get("clinical_diag").toString());
        // 15、样本名称 => yblx
        String samp_name = infoMap.get("samp_name") != null ? infoMap.get("samp_name").toString() : "";
        compareYblx(sjxxDto,yblxList,samp_name,tmpRedisUtil);
        // 16、证件号码
        // infoMap.get("PidIdentityCard").toString();
        // 17、检测项目
        compareJcxm(sjxxDto,jcxmList,infoMap.get("LisItems.lis_item_name").toString(),infoMap.get("LisItems.lis_item_code").toString(),",",tmpXxdyService);
        return sjxxDto;
    }

    /**
     * 疾控中心数据平台
     */
    public void CDCdataExchangePlatformInfo(){
        Object methodObj = map.get("method");
        String method = "";
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            method = methodObj.toString();
        } else {
            log.error("调用疾控数据交换平台接口------未传入调用方法名");
        }
        try {
            jkdymxDto.setDydz("CDCdataExchangePlatformInfo."+method);
            if ("saveCDCdate".equals(method)) {
                String filePath = mkDirs(BusTypeEnum.IMP_REPORT_UNUPLOAD_CDC_TXT.getCode(), "/matridx/fileupload/temp/");
                String fileName = "new_"+ map.get("ybbh") + "_" + System.currentTimeMillis() + ".txt";
                CDCdataSave(filePath + "/" + fileName);
                jkdymxDto.setFhnr(filePath + "/" + fileName); // 返回内容
                jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
                log.error("调用疾控数据交换平台接口------保存报告数据--样本编号：" + map.get("ybbh") + "--文件路径：" + filePath + "/" + fileName);
            } else {
                log.error("调用疾控数据交换平台接口------调用方法名出错method：" + method);
            }
            jkdymxService.insertJkdymxDto(jkdymxDto);
        } catch (Exception e) {
            log.error("调用疾控数据交换平台接口------" + e.getMessage());
        }
    }

    //-------------------------------------------------------------------------------------------------//
    //-----------------------------------------以下为先声接口方法-----------------------------------------//
    //-------------------------------------------------------------------------------------------------//

    /**
     * 调用先声接口
     */
    public void xianshengInfo(){
        Object methodObj = map.get("method");
        String method = "";
        if (null != methodObj && StringUtil.isNotBlank(methodObj.toString())) {
            method = methodObj.toString();
            try {
                jkdymxDto.setDydz("xiansheng."+method);
                if ("selectById".equals(method)) {
                    //获取标本信息
                    dealXianshengInfo();
                } else if ("uploadById".equals(method)) {
                    //上传标本实验信息和生信信息。
                    boolean isSuccess = uploadXianshengInfo();
                    //因为生信结果还未保存，或者因为网络连接有问题,尝试3次后进入整体重试机制
                    int count = 0;
                    while (count < 3 &&!isSuccess) {
                        Thread.sleep(15000);
                        //获取生信信息
                        isSuccess = uploadXianshengInfo();
                        count++;
                    }
                    //如果还是失败，则进入整体重试机制
                    if (!isSuccess) {
                        log.error("调用先声接口------重试3次，报告还是未成功发送。");
                        //因网络问题，重新发送报告
                        Map<String,String> params=new HashMap<>();
                        params.put("ybbh",(String)map.get("ybbh"));
                        params.put("sjid",(String)map.get("sjid"));
                        params.put("yhm","IVD");
                        params.put("subname","生信回传");
                        params.put("resultinfo","生信回传" + (String)map.get("sjid"));
                        resetReportSend(params);
                    }
                } else if ("confirmById".equals(method)) {
                    //接收标本信息
                    confirmXianshengInfo();
                } else {
                    log.error("调用先声接口------调用方法名出错method：" + method);
                }
                jkdymxService.insertJkdymxDto(jkdymxDto);
            } catch (Exception e) {
                log.error("调用先声接口------方法为：" + method + " 发生异常：" + e.getMessage());
                if ("uploadById".equals(method)){
                    //因各种异常情况，重新发送报告
                    Map<String,String> params=new HashMap<>();
                    params.put("ybbh",(String)map.get("ybbh"));
                    params.put("sjid",(String)map.get("sjid"));
                    params.put("yhm","IVD");
                    params.put("subname","生信回传");
                    params.put("resultinfo","生信回传 发生Exception:" + (String)map.get("sjid"));
                    resetReportSend(params);
                }
            }
        } else {
            log.error("调用先声接口------未传入调用方法名");
        }
    }
    /**
     * 回传报告信息
     */
    private boolean uploadXianshengInfo() {
        List<Map<String,Object>> dataList=new ArrayList<>();
        List<String> newsampleId=new ArrayList<>();
        Map<String,String> xpMap=new HashMap<>();
        SjxxDto sjxxDto1 =(SjxxDto)map.get("sjxxDto");
        WeChatInspectionReportModel inspinfo =(WeChatInspectionReportModel)map.get("inspinfo");
        String biosequrl=map.get("biosequrl").toString();
        //获取耐药信息
        String wkbh=inspinfo.getSample_id();
        List<String> sampleIds=new ArrayList<>();
        if(wkbh.indexOf("+")!=-1){
            sampleIds= Arrays.asList(wkbh.split("\\+"));
        }else{
            sampleIds.add(wkbh);
        }
        for(String sampleId :sampleIds){
            newsampleId.add(sampleId);
            //内部编码最后一位
            String nbbm_last=sjxxDto1.getNbbm().substring(sjxxDto1.getNbbm().length() - 1);
            //wkbh数组
            String[] sampleIdArr=sampleId.split("-");
            //jth
            String jth="";
            for(int i=sampleIdArr.length-1;i>=0;i--){
                if(sampleIdArr[i].length()==8){
                    jth=sampleIdArr[i-1];
                    break;
                }
            }
            //根据nbbm分割
            String[] notnbbmArr=sampleId.split(sjxxDto1.getNbbm());
            //nbbm分割后取值
            String jtArrStr=notnbbmArr[1];
            //根据jth分割
            String[] jtArr=jtArrStr.split(jth);
            //获取后缀
            String hzStr=jtArr[0];
            //特殊处理--
            String[] __Str=hzStr.split("--");
            String newHz="";
            if(StringUtil.isNotBlank(__Str[0])){
                newHz=__Str[0].substring(1,__Str[0].length()).replace("-","");
            }else{
                newHz=__Str[1].split("-")[1].replace("-","");
            }

            XxdyDto xxdyDto=new XxdyDto();
            xxdyDto.setDyxxid(sjxxDto1.getJclx());
            xxdyDto.setKzcs3(newHz);//后缀
            xxdyDto.setXshz(nbbm_last);//样本类型代码
            xxdyDto.setDylxcsdm("ZKXMNC");//参考质控对应
            List<XxdyDto> xxdyDtos_nc=xxdyService.getDtoList(xxdyDto);
            xxdyDto.setDylxcsdm("ZKXMPC");//参考质控对应
            List<XxdyDto> xxdyDtos_pc=xxdyService.getDtoList(xxdyDto);
            List<XxdyDto> xxdyDtos=new ArrayList<>();
            xxdyDtos.addAll(xxdyDtos_nc);
            xxdyDtos.addAll(xxdyDtos_pc);
            if(!org.apache.commons.collections.CollectionUtils.isEmpty(xxdyDtos)){
                //筛选符合的list
                if(StringUtil.isNotBlank(xxdyDtos.get(0).getKzcs4())){//若有样本类型限制
                    for(int i=xxdyDtos.size()-1;i>=0;i--){
                        if(!xxdyDtos.get(0).getKzcs4().equals(xxdyDtos.get(i).getKzcs4())){
                            xxdyDtos.remove(i);
                        }
                    }
                }
                //参考质控文库名
                for(XxdyDto xxdyDto1:xxdyDtos) {
                    String ckzkwkmc = xxdyDto1.getKzcs2();
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("wkbh", sampleId);
                    paramMap.put("wkbh1", ckzkwkmc);
                    List<Map<String, String>> wkbhList = sjxxService.getNcPcByWkbh(paramMap);
                    if (!org.apache.commons.collections.CollectionUtils.isEmpty(wkbhList)) {
                        for (Map<String, String> map1 : wkbhList) {
                            newsampleId.add(map1.get("wkbh"));
                            xpMap.put(map1.get("wkbh"),map1.get("xpm"));
                        }
                    }
                }
            }
            //qc部分处理
        }
        for(String sampleId:newsampleId){
            RestTemplate restTemplate=new RestTemplate();
            String reString="";
            Map<String,Object> dataMap=new HashMap<>();
            String nbbm_last=sjxxDto1.getNbbm().substring(sjxxDto1.getNbbm().length() - 1);

            //sjxxjg处理, 因为sjxxjg处理花费时间最多，并且可以判断个数，所以可以防止因为审核结果还未保存完成，这边就开始执行造成结果为空的情况
            SjxxjgDto sjxxjgDto = new SjxxjgDto();
            if("B".equals(nbbm_last)&&wkbh.contains("+")){
                if(sampleId.contains("DNA")){
                    sjxxjgDto.setJclx("D");
                }else if(sampleId.contains("HS")){
                    sjxxjgDto.setJclx("R");
                }

            }else{
                sjxxjgDto.setJclx(inspinfo.getDetection_cskz1());
            }

            sjxxjgDto.setXpmc(xpMap.get(sampleId));
            sjxxjgDto.setWkbh(sampleId);
            List<Map<String, Object>> infoList = sjxxjgService.getInfoList(sjxxjgDto);
            if(infoList==null || infoList.size()==0){
                return false;
            }
            map.put(sampleId,infoList);

            if(wkbh.indexOf(sampleId)!=-1){
                dataMap.put("sampleId",sampleId);

                boolean flag=true;
                if("B".equals(nbbm_last)&&wkbh.contains("+")){
                    if(sampleId.contains("DNA")&&wkbh.contains("-tNGS")){
                        flag=false;
                    }else if(sampleId.contains("HS")){
                        flag=false;
                    }
                }
                if(flag){
                    try{
                        //http://dx.matridx.com:8002/BCL/api/qcinfo/
                        if(biosequrl.endsWith("/"))
                            biosequrl = biosequrl.substring(0, biosequrl.length() - 1);
                        reString = restTemplate.getForObject(biosequrl + "/BCL/api/qcinfo/"+sampleId+"/?token=eba3f34c24943e1c", String.class);
                        JSONObject jsonObject = JSONObject.parseObject(reString);
                        JSONObject object = JSONObject.parseObject(jsonObject.get("data").toString());
                        Map<String,String> qCMap=new HashMap<>();
                        qCMap.put("total_reads",object.get("clean_reads")+"");
                        qCMap.put("Q30_rawdata",object.get("raw_q30")+"");
                        qCMap.put("Q30_cleandata",object.get("clean_q30")+"");
                        qCMap.put("adapter_percent",object.get("adapter")+"");
                        qCMap.put("duplicate_ratio",inspinfo.getDuplicate_ratio());
                        qCMap.put("lowQ_percent",inspinfo.getLowQ_percent());
                        qCMap.put("library_concentration",object.get("con.lib")+"");
                        Map<String,String>wkcxParam=new HashMap<>();
                        wkcxParam.put("wkbh",sampleId);
                        List<Map<String,String>> wkcxList=sjxxService.getWkcxByWkbh(wkcxParam);
                        if(!org.springframework.util.CollectionUtils.isEmpty(wkcxList)){
                            Map<String,String> wkcxbbMap=wkcxList.get(0);
                            JSONObject spikeObj = JSONObject.parseObject(wkcxbbMap.get("spikejson")+"");
                            Set<String>spikeSet= spikeObj.keySet();
                            int IC_reads=0;
                            for(String spikekey:spikeSet){
                                IC_reads+=Integer.valueOf(spikeObj.get(spikekey).toString());
                            }
                            qCMap.put("IC_reads",IC_reads+"");
                            BigDecimal hundred = new BigDecimal(100);
                            BigDecimal totalreads = new BigDecimal(wkcxbbMap.get("totalreads")+"");
                            BigDecimal host_reads = new BigDecimal(wkcxbbMap.get("homo").toString()).multiply(totalreads).divide(hundred,5, RoundingMode.HALF_UP);
                            qCMap.put("host_reads",String.valueOf(host_reads.longValue())+"");
                            BigDecimal homo = new BigDecimal(wkcxbbMap.get("homo")+"");
                            BigDecimal bacteria = new BigDecimal(wkcxbbMap.get("bacteria")+"");
                            BigDecimal fungi = new BigDecimal(wkcxbbMap.get("fungi")+"");
                            BigDecimal viruses = new BigDecimal(wkcxbbMap.get("viruses")+"");
                            BigDecimal parasite = new BigDecimal(wkcxbbMap.get("parasite")+"");
                            BigDecimal spikein = new BigDecimal(wkcxbbMap.get("spikein")+"");
                            BigDecimal Microbe_reads=homo.add(bacteria).add(fungi).add(viruses).add(parasite).add(spikein).multiply(totalreads).divide(hundred,5, RoundingMode.HALF_UP);
                            qCMap.put("Microbe_reads",String.valueOf(Microbe_reads.longValue()));
                            qCMap.put("host_percent",wkcxbbMap.get("homo")+"");
                            qCMap.put("IC_percent",wkcxbbMap.get("spikein")+"");
                            qCMap.put("microbe_percent",homo.add(bacteria).add(fungi).add(viruses).add(parasite).add(spikein).multiply(totalreads).toString());
                            qCMap.put("Species_number","");
                            qCMap.put("nonhost_nonIC_reads",hundred.subtract(homo).subtract(spikein).multiply(totalreads).divide(hundred,5, RoundingMode.HALF_UP).toString());
                            qCMap.put("nonHostRatio",hundred.subtract(homo).toString());
                        }
                        dataMap.put("qcData",qCMap);
                    }catch (Exception e){
                        log.error("Receive matchingReportSend: 从生信获取qc数据错误"+e.getMessage());
                    }

                    //ARM_Family  //point_mutation
                    List<Map<String,String>>pointList=new ArrayList<>();
                    List<Map<String,String>>armList=new ArrayList<>();
                    SjnyxDto sjnyxParamDto=new SjnyxDto();
                    sjnyxParamDto.setWkbh(wkbh);
                    List<SjnyxDto> nyList=sjnyxService.getNyxByWkbh(sjnyxParamDto);
                    if(!org.springframework.util.CollectionUtils.isEmpty(nyList)){
                        for(SjnyxDto sjnyxDto:nyList){
                            Map<String,String> pointMap=new HashMap<>();
                            Map<String,String> armMap=new HashMap<>();
                            if(StringUtil.isNotBlank(sjnyxDto.getJson())){
                                pointMap.put("Report_or_No","YES");
                                pointMap.put("depth",sjnyxDto.getTbsd());
                                pointMap.put("point_mutation_arg_table_genus_name",sjnyxDto.getTbjy());
                                pointMap.put("point_mutation_arg_table_mutation_detection_result",sjnyxDto.getTbjg());
                                pointMap.put("drugclass_zh",sjnyxDto.getNyx());//耐药类型
                                pointMap.put("species_CH",sjnyxDto.getQyz());
                                pointMap.put("product_id",sampleId);
                                pointList.add(pointMap);
                            }else{
                                armMap.put("Report_or_No","YES");
                                String nyyp = sjnyxDto.getYp();
                                armMap.put("drugclass_zh",sjnyxDto.getYp());
                                armMap.put("species_CH",sjnyxDto.getQyz());
                                armMap.put("product_id",sampleId);
                                //针对先声的耐药药品信息，当前从生信获取到的不是很稳定，所以这边做了一个数据库进行维护 2025-08-01
                                if(StringUtil.isNotBlank(nyyp)){
                                    List<String> nyypArr = Arrays.stream(nyyp.split(";")).toList();
                                    NyypxxDto nyypxxParam=new NyypxxDto();
                                    nyypxxParam.setNyzwms(nyypArr);
                                    List<NyypxxDto> nyypxxDtoList = nyypxxService.getDtoList(nyypxxParam);
                                    if(!org.springframework.util.CollectionUtils.isEmpty(nyypxxDtoList)){
                                        String  anti="";
                                        for(NyypxxDto nyypxxDto:nyypxxDtoList){
                                            if(StringUtil.isBlank(nyypxxDto.getXgyp()))
                                                continue;
                                            anti +=nyypxxDto.getXgyp() + ";";
                                        }
                                        armMap.put("anti",anti);
                                    }else{
                                        armMap.put("anti",StringUtil.isBlank(sjnyxDto.getYsyp())?"-":sjnyxDto.getYsyp());//疑似耐药新增
                                    }
                                }else
                                    armMap.put("anti",StringUtil.isBlank(sjnyxDto.getYsyp())?"-":sjnyxDto.getYsyp());//疑似耐药新增
                                armMap.put("AMR_Gene_Family",sjnyxDto.getJy());
                                String xls=sjnyxDto.getXls();
                                if(StringUtil.isNotBlank(xls)){
                                    try {
                                        if (xls.indexOf(";") != -1) {
                                            String[] xlsArr = xls.split(";");
                                            int xls_int = 0;
                                            for (String xlsStr : xlsArr) {
                                                xls_int += Integer.valueOf(xlsStr);
                                            }
                                            armMap.put("AROreads", String.valueOf(xls_int));
                                        } else {
                                            armMap.put("AROreads", xls);
                                        }
                                    }catch(Exception e){
                                        log.error("耐药序列数转换失败！" + xls);
                                        armMap.put("AROreads","0");
                                    }
                                }else{
                                    armMap.put("AROreads","0");
                                }
                                armList.add(armMap);
                            }
                        }
                    }
                    dataMap.put("nyList",nyList);
                    dataMap.put("armList",armList);
                    dataMap.put("pointList",pointList);

                    //VirFactor
                    List<Map<String,String>>vifList=new ArrayList<>();
                    SjdlxxDto sjdlxxDto=new SjdlxxDto();
                    sjdlxxDto.setWkbh(wkbh);
                    List<SjdlxxDto> sjdlxxDtoList = sjdlxxService.getDtoListByWkbh(sjdlxxDto);
                    if(!org.springframework.util.CollectionUtils.isEmpty(sjdlxxDtoList)){
                        for(SjdlxxDto dataDto:sjdlxxDtoList){
                            Map<String,String> vifMap=new HashMap<>();
                            vifMap.put("Report_or_No","YES");
                            vifMap.put("Gene",dataDto.getJy());
                            vifMap.put("Gene_anno",dataDto.getMs());
                            vifMap.put("VFID",dataDto.getDlyzid());
                            vifMap.put("VF_Name",dataDto.getDlyz());
                            vifMap.put("Bacteria_CH",dataDto.getZmc());
                            vifMap.put("taxid",dataDto.getGlwzid());
                            vifMap.put("VFcategory",dataDto.getDlyzlx());
                            vifMap.put("reads",dataDto.getXls());
                            vifMap.put("product_id",sampleId);
                            vifList.add(vifMap);
                        }
                    }
                    dataMap.put("vifList",vifList);
                }
            }

            dataList.add(dataMap);
        }

        String nbbm_last=sjxxDto1.getNbbm().substring(sjxxDto1.getNbbm().length() - 1);

        map.put("dataList",dataList);
        map.put("wkbhs",newsampleId);//为了轮询生成物种sheet
        if("B".equals(nbbm_last)&&wkbh.contains("+")){
            List<Map<String,Object>> dataList1=new ArrayList<>();
            String[] sampleIdArr=wkbh.split("\\+");
            String sampleId_HS="";
            String sampleId_DNA="";
            for(String str:sampleIdArr){
                if(str.contains("DNA")&&wkbh.contains("-tNGS")){
                    sampleId_HS=str;
                }else if(str.contains("HS")){
                    sampleId_HS=str;
                }else{
                    sampleId_DNA=str;
                }
            }
            newsampleId.remove(sampleId_HS);
            map.put("wkbhs",newsampleId);

            List<Map<String, Object>> infoList_DNA=(List<Map<String,Object>>)map.get(sampleId_DNA);

            List<Map<String, Object>> infoList_HS=(List<Map<String,Object>>)map.get(sampleId_HS);

            List<Map<String, Object>>info_List = Stream.concat(infoList_DNA.stream(), infoList_HS.stream())
                    .collect(Collectors.toMap(map_hb -> map_hb.get("jdid"), map_hb -> map_hb, (map1, map2) -> map1)) // 使用(map1, map2) -> map1来保留第一个遇到的元素（可以根据需要选择保留哪一个）
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            map.remove(sampleId_HS);
            map.put(sampleId_DNA,info_List);
            for(Map<String,Object> map1:dataList){
                if(sampleId_DNA.equals(map1.get("sampleId"))){
                    dataList1.add(map1);
                }
            }
            map.put("dataList",dataList1);
        }
        List<WbsjxxDto> listBySjid = wbsjxxService.getListBySjid(map.get("sjid").toString());
        WbsjxxDto wbsjxxDto = listBySjid.get(0);
        if (StringUtil.isNotBlank(wbsjxxDto.getInfojson())) {
            Map<String,Object> jsonMap = (Map<String, Object>) JSON.parse(wbsjxxDto.getInfojson());
            if (StringUtil.isBlank((String) jsonMap.get("bioinfoResultFlg")) || "0".equals((String) jsonMap.get("bioinfoResultFlg"))){
                String result = null;
                try {
                    String wjlj = geneateXianshengReport(jsonMap);
                    if (StringUtil.isBlank(wjlj)){
                        log.error("调用先声接口------回传报告信息失败，未生成报告文件！");
                        return false;
                    }
                    String XIANSHENG_URL = map.get("XIANSHENG_URL").toString();
                    if(XIANSHENG_URL.endsWith("/"))
                        XIANSHENG_URL = XIANSHENG_URL.substring(0,XIANSHENG_URL.length()-1);
                    String url = XIANSHENG_URL + XIANSHENG_REPORT_RETURN_URL;
                    url = url + "?serviceBillId=" + jsonMap.get("serviceBillId").toString() + "&goodsCode=" + jsonMap.get("goodsCode").toString();
                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
                    requestHeaders.set("source","22");
                    String ts = String.valueOf(System.currentTimeMillis());
                    requestHeaders.set("ts", ts);
                    requestHeaders.set("sign", new Encrypt().stringToMD5("e8fae5d61ef7c2afda1b62c178ebcb71"+ts));
//                    requestEntrustModel.add("serviceBillId",jsonMap.get("serviceBillId"));
//                    requestEntrustModel.add("goodsCode",jsonMap.get("goodsCode"));
                    File file = new File(wjlj);
                    MultiValueMap<String,Object> fileMap = new LinkedMultiValueMap<>();
                    fileMap.add("analyseFile",new FileSystemResource(wjlj));
                    HttpEntity requestEntity = new HttpEntity(fileMap, requestHeaders);
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                    result = response.getBody();

                    try {
                        String xxnr = "先声对接--样本编号：" + map.get("ybbh") + "生信回传正常。" + result;
                        IDdxxglService t_ddxxglService = (IDdxxglService)ServiceFactory.getService("ddxxglServiceImpl");
                        DingTalkUtil t_dingTalkUtil = (DingTalkUtil)ServiceFactory.getService("dingTalkUtil");
                        List<DdxxglDto> ddxxList = t_ddxxglService.selectByDdxxlx("INTERFACE_EXCEPTION");
                        if (ddxxList != null && !ddxxList.isEmpty()) {
                            for (DdxxglDto ddxxglDto : ddxxList) {
                                if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                                    t_dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", xxnr);
                                }
                            }
                        }
                    }catch(Exception ex){
                        log.error("调用先声接口------生信回传--发送钉钉通知失败：{}", ex.getMessage());
                    }

                    log.error("调用先声接口------生信回传--样本编号：" + map.get("ybbh") + "--返回结果：" + result);
                } catch (Exception e) {
                    log.error("调用先声接口------生信回传 发生异常--样本编号：" + map.get("ybbh") + "--返回结果：" + e.getMessage());
                    try {
                        String xxnr = "先声的生信回传接口出现异常，标本编号为：" + map.get("ybbh") + "异常信息为：" + e.getMessage();
                        IDdxxglService t_ddxxglService = (IDdxxglService)ServiceFactory.getService("ddxxglServiceImpl");
                        DingTalkUtil t_dingTalkUtil = (DingTalkUtil)ServiceFactory.getService("dingTalkUtil");
                        List<DdxxglDto> ddxxList = t_ddxxglService.selectByDdxxlx("INTERFACE_EXCEPTION");
                        if (ddxxList != null && !ddxxList.isEmpty()) {
                            for (DdxxglDto ddxxglDto : ddxxList) {
                                if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                                    t_dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", xxnr);
                                }
                            }
                        }
                    }catch (Exception ex) {
                        log.error("发送钉钉通知失败：{}", ex.getMessage());
                    }
                    return false;
                }
            } else {
                log.error("调用先声接口------生信回传--样本编号：" + map.get("ybbh") + "--返回结果：失败!该样本已回传!");
            }
        } else {
            log.error("调用先声接口------生信回传--样本编号：" + map.get("ybbh") + "--返回结果：失败!未获取到外部送检信息!");
        }
        return true;
    }

    /**
     * 先声接口保存实验结果
     * @param map
     */
    public boolean saveResultInfo(Map<String,Object> map){
        try {

            String XIANSHENG_URL = map.get("XIANSHENG_URL").toString();
            if(XIANSHENG_URL.endsWith("/"))
                XIANSHENG_URL = XIANSHENG_URL.substring(0,XIANSHENG_URL.length()-1);
            String url = XIANSHENG_URL + XIASHENG_SAVERESULT_URL;
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8);
            requestHeaders.set("source","22");
            String ts = String.valueOf(System.currentTimeMillis());
            requestHeaders.set("ts", ts);
            requestHeaders.set("sign", new Encrypt().stringToMD5("e8fae5d61ef7c2afda1b62c178ebcb71"+ts));
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("serviceBillId",map.get("serviceBillId"));
            requestBody.put("goodsCode",map.get("goodsCode"));
            requestBody.put("taskCode",map.get("taskCode"));
            requestBody.put("nodeCode",map.get("nodeCode"));
            requestBody.put("experimentResult",JSON.toJSONString(map.get("experimentResult")));
            requestBody.put("needBase64Convert",false);
            HttpEntity requestEntity = new HttpEntity(requestBody, requestHeaders);
            RestTemplate restTemplate1=new RestTemplate();
            ResponseEntity<String> response = restTemplate1.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String result = response.getBody();
            try {
                String xxnr = "先声对接--样本编号：" + map.get("samplerCode") + "实验结果正常。" + result;
                IDdxxglService t_ddxxglService = (IDdxxglService)ServiceFactory.getService("ddxxglServiceImpl");
                DingTalkUtil t_dingTalkUtil = (DingTalkUtil)ServiceFactory.getService("dingTalkUtil");
                List<DdxxglDto> ddxxList = t_ddxxglService.selectByDdxxlx("INTERFACE_EXCEPTION");
                if (ddxxList != null && !ddxxList.isEmpty()) {
                    for (DdxxglDto ddxxglDto : ddxxList) {
                        if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                            t_dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", xxnr);
                        }
                    }
                }
            }catch(Exception ex){
                log.error("调用先声接口------实验结果--发送钉钉通知失败：{}", ex.getMessage());
            }
            log.error("调用先声接口------保存实验结果：" + map.get("samplerCode") + "--返回结果：" + result);
        } catch (Exception e) {
            log.error("调用先声接口------保存实验结果 异常：" + map.get("samplerCode") + "--返回结果：" + e.getMessage());
            try {
                String xxnr = "先声的实验结果接口出现异常，标本编号为：" + map.get("samplerCode") + " 异常信息为：" + e.getMessage();

                IDdxxglService t_ddxxglService = (IDdxxglService)ServiceFactory.getService("ddxxglServiceImpl");
                DingTalkUtil t_dingTalkUtil = (DingTalkUtil)ServiceFactory.getService("dingTalkUtil");
                List<DdxxglDto> ddxxList = t_ddxxglService.selectByDdxxlx("INTERFACE_EXCEPTION");
                if (ddxxList != null && !ddxxList.isEmpty()) {
                    for (DdxxglDto ddxxglDto : ddxxList) {
                        if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                            t_dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "通知", xxnr);
                        }
                    }
                }
            }catch (Exception ex) {
                log.error("发送钉钉通知失败：{}", ex.getMessage());
            }
            return false;
        }
        return true;
    }
    private String geneateXianshengReport(Map<String,Object> jsonMap) {
        String filePath = mkDirs(BusTypeEnum.IMP_REPORT_XIANSHENG.getCode(), "/matridx/fileupload/temp/");
        String hzxm = (String) map.get("hzxm");
        String orderNumber = (String) jsonMap.get("orderId");
        String product_id = (String) jsonMap.get("goodsCode");
        String ybbh = (String) map.get("ybbh");
        String sampleTypeName = (String) map.get("yblxmc");
        String fileName = "xiansheng_" + ybbh + "_" + System.currentTimeMillis() + ".xls";
        //1.初始化excel   工作表QC
        Workbook workbook = new XSSFWorkbook(); // 创建Excel工作簿
        Sheet sheet_QC = workbook.createSheet("QC"); // 创建工作表QC
        Row row0_QC = sheet_QC.createRow(0); // 创建标题行
        String[] qc_title = {"Report_or_No","Name","order_number","sample","total_reads","Q30_rawdata","Q30_cleandata","lowQ_percent","short_percent","adapter_percent","clean_reads","duplicate_ratio","IC_reads","host_reads","Microbe_reads","host_percent","IC_percent","microbe_percent","Species_number","nonhost_nonIC_reads","nonHostRatio","sample_type","product_id","total_nucleic_acid","libraryinput","library_concentration"};
        for (int i = 0; i < qc_title.length; i++) {
            row0_QC.createCell(i).setCellValue(qc_title[i]);
        }
        //2.初始化excel   工作表point_mutation
        String[] point_mutation_title = {"Report_or_No","Sample","patient_name","ARO_Accession_ID","point_mutation_arg_table_genus_name","point_mutation_arg_table_mutation_detection_result","AMR_Gene_Family_Full_Name","ARO_famliy_Annotation","ARO_famliy","species","species_CH","point_mutation_arg_table_mutation_rate","drugclass","drugclass_zh","anti","coverage","depth","confidence","order_number","product_id","Confidence"};
        Sheet sheet_point_mutation=workbook.createSheet("point_mutation"); // 创建工作表point_mutat;
        Row row0_point_mutation=sheet_point_mutation.createRow(0); // 创建标题行;
        for (int i = 0; i < point_mutation_title.length; i++) {
            row0_point_mutation.createCell(i).setCellValue(point_mutation_title[i]);
        }
        //3.初始化excel   工作表ARM_Family
        Sheet sheet_ARM_Family = workbook.createSheet("ARM_Family"); // 创建工作表ARM_Family
        Row row0_ARM_Family = sheet_ARM_Family.createRow(0); // 创建标题行
        String[] ARM_Family_title = {"Report_or_No","Sample","patient_name","ARO_Accession_ID","Family_ARO_Accession","AMR_Gene_Family","AMR_Gene_Family_Full_Name","ARO_famliy_Annotation","ARO_famliy","species","species_CH","AROreads","drugclass","drugclass_zh","anti","coverage","depth","confidence","order_number","product_id","Confidence"};
        for (int i = 0; i < ARM_Family_title.length; i++) {
            row0_ARM_Family.createCell(i).setCellValue(ARM_Family_title[i]);
        }
        //4.初始化excel   工作表ARM_Family
        Sheet sheet_VirFactor = workbook.createSheet("VirFactor"); // 创建工作表VirFactor
        Row row0_VirFactor = sheet_VirFactor.createRow(0); // 创建标题行
        String[] VirFactor_title = {"Report_or_No","Sample","patient_name","Gene","Gene_anno","VFID","VF_Name","Bacteria","Bacteria_CH","taxid","VFCID","VFcategory","Characteristics","Structure","Function","Mechanism","reads","uniqreads","coverage","depth","confidence","order_number","product_id","Confidence"};
        for (int i = 0; i < VirFactor_title.length; i++) {
            row0_VirFactor.createCell(i).setCellValue(VirFactor_title[i]);
        }

        if(map.get("dataList")!=null){
            List<Map<String,Object>> dataList=(List<Map<String,Object>>)map.get("dataList");
            for(int x = 0; x <dataList.size();x++){
                Map<String,Object> dataMap=dataList.get(x);
                if(dataMap.get("qcData")!=null){
                    Map<String,String>qCMap = (Map<String,String>)dataMap.get("qcData");
                    Row row = sheet_QC.createRow(sheet_QC.getLastRowNum() + 1);
                    row.createCell(0).setCellValue("YES");// Report_or_No
                    row.createCell(1).setCellValue(hzxm);// Name
                    row.createCell(2).setCellValue(orderNumber);// order_number
                    row.createCell(3).setCellValue(ybbh);// Sample
                    row.createCell(4).setCellValue(qCMap.get("total_reads")+"");// total_reads
                    row.createCell(5).setCellValue(qCMap.get("Q30_rawdata")+"");// Q30_rawdata
                    row.createCell(6).setCellValue(qCMap.get("Q30_cleandata")+"");// Q30_cleandata
                    row.createCell(7).setCellValue(qCMap.get("lowQ_percent")+"");// lowQ_percent
                    row.createCell(8).setCellValue("-");// short_percent
                    row.createCell(9).setCellValue(qCMap.get("adapter_percent")+"");// adapter_percent
                    row.createCell(10).setCellValue(qCMap.get("clean_reads")+"");// clean_reads
                    row.createCell(11).setCellValue(qCMap.get("duplicate_ratio")+"");// duplicate_ratio
                    row.createCell(12).setCellValue(qCMap.get("IC_reads")+"");// IC_reads
                    row.createCell(13).setCellValue(qCMap.get("host_reads")+"");// host_reads
                    row.createCell(14).setCellValue(qCMap.get("microbe_reads")+"");// microbe_reads
                    row.createCell(15).setCellValue(qCMap.get("host_percent")+"");// host_percent
                    row.createCell(16).setCellValue(qCMap.get("IC_percent")+"");// IC_percent
                    row.createCell(17).setCellValue(qCMap.get("microbe_percent")+"");// microbe_percent
                    row.createCell(18).setCellValue("-");// Species_number
                    row.createCell(19).setCellValue(qCMap.get("nonhost_nonIC_reads")+"");// nonhost_nonIC_reads
                    row.createCell(20).setCellValue(qCMap.get("duplicate_ratio")+"");// nonHostRatio
                    row.createCell(21).setCellValue(sampleTypeName);// sample_type
                    row.createCell(22).setCellValue(product_id);// product_id
                    row.createCell(23).setCellValue("-");// total_nucleic_acid
                    row.createCell(24).setCellValue("-");// libraryinput
                    row.createCell(25).setCellValue(qCMap.get("library_concentration")+"");// library_concentration
                }
                // 耐药
                if(dataMap.get("pointList")!=null){
                    List<Map<String,String>> pointList = (List<Map<String,String>>)dataMap.get("pointList");
                    if(!CollectionUtils.isEmpty(pointList)){
                        for(int i=0;i<pointList.size();i++){
                            Map<String,String> pointMap=pointList.get(i);
                            Row row = sheet_point_mutation.createRow(sheet_point_mutation.getLastRowNum() + 1);
                            row.createCell(0).setCellValue("YES");// Report_or_No
                            row.createCell(1).setCellValue(ybbh);// Sample
                            row.createCell(2).setCellValue(hzxm);// patient_name
                            row.createCell(3).setCellValue("-");// ARO_Accession_ID
                            row.createCell(4).setCellValue(pointMap.get("point_mutation_arg_table_genus_name"));// point_mutation_arg_table_genus_name
                            row.createCell(5).setCellValue(pointMap.get("point_mutation_arg_table_mutation_detection_result"));// point_mutation_arg_table_mutation_detection_result
                            row.createCell(6).setCellValue("-");// AMR_Gene_Family_Full_Name
                            row.createCell(7).setCellValue("-");// ARO_famliy_Annotation
                            row.createCell(8).setCellValue("-");// ARO_famliy
                            row.createCell(9).setCellValue("-");// species
                            row.createCell(10).setCellValue(pointMap.get("species_CH"));// species_CH
                            row.createCell(11).setCellValue("0");// AROreads
                            row.createCell(12).setCellValue("-");// drugclass
                            row.createCell(13).setCellValue(pointMap.get("drugclass_zh"));// drugclass_zh
                            row.createCell(14).setCellValue("-");// anti
                            row.createCell(15).setCellValue("-");// coverage
                            row.createCell(16).setCellValue(pointMap.get("depth"));// depth
                            row.createCell(17).setCellValue("-");// confidence
                            row.createCell(18).setCellValue(orderNumber);// order_number
                            row.createCell(19).setCellValue(product_id);// product_id
                            row.createCell(20).setCellValue("-");// Confidence
                        }
                    }
                }
                // 耐药
                if(dataMap.get("armList")!=null){
                    List<Map<String,String>>armList = (List<Map<String,String>>)dataMap.get("armList");
                    if(!CollectionUtils.isEmpty(armList)){
                        for(int i=0;i<armList.size();i++){
                            Map<String,String> armMap=armList.get(i);
                            Row row = sheet_ARM_Family.createRow(sheet_ARM_Family.getLastRowNum() + 1);
                            row.createCell(0).setCellValue("YES");// Report_or_No
                            row.createCell(1).setCellValue(ybbh);// Sample
                            row.createCell(2).setCellValue(hzxm);// patient_name
                            row.createCell(3).setCellValue("-");// ARO_Accession_ID
                            row.createCell(4).setCellValue("-");// Family_ARO_Accession
                            row.createCell(5).setCellValue(armMap.get("AMR_Gene_Family"));// AMR_Gene_Family
                            row.createCell(6).setCellValue("-");// AMR_Gene_Family_Full_Name
                            row.createCell(7).setCellValue("-");// ARO_famliy_Annotation
                            row.createCell(8).setCellValue("-");// ARO_famliy
                            row.createCell(9).setCellValue("-");// species
                            row.createCell(10).setCellValue(armMap.get("species_CH"));// species_CH
                            row.createCell(11).setCellValue(armMap.get("AROreads"));// AROreads
                            row.createCell(12).setCellValue("-");// drugclass
                            row.createCell(13).setCellValue(armMap.get("drugclass_zh"));// drugclass_zh
                            row.createCell(14).setCellValue(armMap.get("anti"));// anti
                            row.createCell(15).setCellValue("-");// coverage
                            row.createCell(16).setCellValue("-");// depth
                            row.createCell(17).setCellValue("高");// confidence
                            row.createCell(18).setCellValue(orderNumber);// order_number
                            row.createCell(19).setCellValue(product_id);// product_id
                            row.createCell(20).setCellValue("-");// Confidence
                        }
                    }
                }
                // 毒力
                if(dataMap.get("vifList")!=null){
                    List<Map<String,String>> vifList = (List<Map<String,String>>)dataMap.get("vifList");
                    if(!CollectionUtils.isEmpty(vifList)){
                        for(int i=0;i<vifList.size();i++){
                            Map<String,String> armMap=vifList.get(i);
                            Row row = sheet_VirFactor.createRow(sheet_VirFactor.getLastRowNum() + 1);
                            row.createCell(0).setCellValue("YES");// Report_or_No
                            row.createCell(1).setCellValue(ybbh);// Sample
                            row.createCell(2).setCellValue(hzxm);// patient_name
                            row.createCell(3).setCellValue(armMap.get("Gene"));// Gene
                            row.createCell(4).setCellValue(armMap.get("Gene_anno"));// Gene_anno
                            row.createCell(5).setCellValue(armMap.get("VFID"));// VFID
                            row.createCell(6).setCellValue(armMap.get("VF_Name"));// VF_Name
                            row.createCell(7).setCellValue("-");// Bacteria
                            row.createCell(8).setCellValue(armMap.get("Bacteria_CH"));// Bacteria_CH
                            row.createCell(9).setCellValue(armMap.get("taxid"));// taxid
                            row.createCell(10).setCellValue("-");// VFCID
                            row.createCell(11).setCellValue(armMap.get("VFcategory"));// VFcategory
                            row.createCell(12).setCellValue("-");// Characteristics
                            row.createCell(13).setCellValue("-");// Structure
                            row.createCell(14).setCellValue("-");// Function
                            row.createCell(15).setCellValue("-");// Mechanism
                            row.createCell(16).setCellValue(armMap.get("reads"));// reads
                            row.createCell(17).setCellValue("-");// uniqreads
                            row.createCell(18).setCellValue("-");// coverage
                            row.createCell(19).setCellValue("-");// depth
                            row.createCell(20).setCellValue("-");// confidence
                            row.createCell(21).setCellValue(orderNumber);// order_number
                            row.createCell(22).setCellValue(product_id);// product_id
                            row.createCell(23).setCellValue("-");// Confidence
                        }
                    }
                }
            }
        }


        // 物种检出数据
        String oldname="";
        if(map.get("wkbhs") != null){
            List<String> wkhbs=(List<String>) map.get("wkbhs");
            List<Map<String,String>> tswzlx=sjkzxxService.getTsWzlx();
            for (int i = 0; i < wkhbs.size(); i++) {
                List<Map<String,Object>> sjxxjgDtoList = (List<Map<String,Object>>) map.get(wkhbs.get(i));
                //6.初始化excel   工作表VirFactor
                if(!CollectionUtils.isEmpty(sjxxjgDtoList)){
                    String title="";
                    if(ybbh.equals(sjxxjgDtoList.get(0).get("ybbh"))){
                        title=sjxxjgDtoList.get(0).get("ybbh")+"-"+sjxxjgDtoList.get(0).get("hzxm");
                    }else{
                        title=String.valueOf(sjxxjgDtoList.get(0).get("ybbh"));
                    }
                    //防止信息对应中多个相同导致sheet重名得错误
                    if(StringUtil.isNotBlank(oldname)){
                       if(oldname.equals(title)){
                           break;
                       }else{
                           oldname=title;
                       }
                    }else{
                        oldname=title;
                    }
                    Sheet sheet_sample = workbook.createSheet(title); // 创建工作表创建工作表VirFactor
                    Row row0_sample = sheet_sample.createRow(0); // 创建标题行
                    String[] sample_title = {"Medical_Classification","superkingdom","species","species_CH","reads","Medical_Annotation","Relative_abundance","RPM","Coverage","Coverage_png","MeanDepth","taxid","genus","genus_CH","genus_reads","genusAbundance","sample","order_number","product_id","Confidence","Gram_type"};
                    for (int j = 0; j < sample_title.length; j++) {
                        row0_sample.createCell(j).setCellValue(sample_title[j]);
                    }
                    for(int k=0;k<sjxxjgDtoList.size();k++){
                        Row row = sheet_sample.createRow(sheet_sample.getLastRowNum() + 1);
                        Map<String,Object> sample =sjxxjgDtoList.get(k);
                        String Medical_Classification = "";
                        if (sample.get("jglx") != null){
                            if ("possible".equals(sample.get("jglx").toString())){
                                Medical_Classification = "病原体";
                            } else if ("pathogen".equals(sample.get("jglx").toString())){
                                Medical_Classification = "病原体";
                            } else if ("background".equals(sample.get("jglx").toString())){
                                Medical_Classification = "疑似病原体";
                            } else if ("commensal".equals(sample.get("jglx").toString())){
                                Medical_Classification = "定值";
                            }
                        }
                        row.createCell(0).setCellValue(Medical_Classification);// Medical_Classification
                        String superkingdom="virus_rna";
                        if("Virus".equals(sample.get("wzfl") != null?(String)sample.get("wzfl"):"")){
                            //病毒-DNA->virus_dna
                            //病毒-RNA->virus_rna
                            if("DNA".equals(sample.get("wzfllx") != null?(String)sample.get("wzfllx"):"")){
                                superkingdom="virus_dna";
                            }
                        }else if("Parasite".equals(sample.get("wzfl") != null?(String)sample.get("wzfl"):"")){
                            //寄生虫->eukaryota
                            superkingdom="eukaryota";
                        }else{
                            superkingdom=sample.get("wzfl") != null?(String)sample.get("wzfl"):"";
                        }
                        if(!CollectionUtils.isEmpty(tswzlx)){
                            //支原体 衣原体
                            Optional<Map<String,String>> opt_mcr=tswzlx.stream().filter(e->"0".equals(e.get("lx"))).filter(e->e.get("taxid").equals(sample.get("jdid"))).findFirst();
                            if(opt_mcr.isPresent()){
                                superkingdom="others_mycoplasma_chlamydia";
                            }
                            //非结核分枝杆菌
                            Optional<Map<String,String>> opt_ntm=tswzlx.stream().filter(e->"1".equals(e.get("lx"))).filter(e->e.get("taxid").equals(sample.get("jdid"))).findFirst();
                            if(opt_ntm.isPresent()){
                                superkingdom="others_non_tuberculosis";
                            }
                        }
                        if("结核分枝杆菌复合群".equals(sample.get("wzzwm") != null?(String)sample.get("wzzwm"):"")){
                            superkingdom="others_tuberculosis";
                        }
                        row.createCell(1).setCellValue(superkingdom);// superkingdom
                        row.createCell(2).setCellValue(sample.get("wzywm") != null?(String)sample.get("wzywm"):"");// species
                        row.createCell(3).setCellValue(sample.get("wzzwm") != null?(String)sample.get("wzzwm"):"");// species_CH
                        row.createCell(4).setCellValue(sample.get("zdqs") != null?(String)sample.get("zdqs"):"");// reads
                        row.createCell(5).setCellValue(sample.get("wzzs") != null?(String)sample.get("wzzs"):"");// Medical_Annotation
                        row.createCell(6).setCellValue(sample.get("xdfd") != null?(String)sample.get("xdfd"):"");// Relative_abundance
                        row.createCell(7).setCellValue(sample.get("rpm") != null?(String)sample.get("rpm"):"");// RPM
                        row.createCell(8).setCellValue(sample.get("jyzfgd") != null?(String)sample.get("jyzfgd"):"");// Coverage
                        row.createCell(9).setCellValue("");// Coverage_png
                        row.createCell(10).setCellValue("");// MeanDepth
                        row.createCell(11).setCellValue(sample.get("jdid") != null?(String)sample.get("jdid"):"");// taxid
                        row.createCell(12).setCellValue(sample.get("sm") != null?(String)sample.get("sm"):"");// genus
                        row.createCell(13).setCellValue(sample.get("szwm") != null?(String)sample.get("szwm"):"");// genus_CH
                        row.createCell(14).setCellValue(sample.get("sdds") != null?(String)sample.get("sdds"):"");// genus_reads
                        String genusAbundance=sample.get("sfd") != null?(String)sample.get("sfd"):"";
                        String newgenusAbundance="";
                        if(StringUtil.isNotBlank(genusAbundance)){
                            newgenusAbundance=genusAbundance+"%";
                        }
                        row.createCell(15).setCellValue(newgenusAbundance);// genusAbundance
                        row.createCell(16).setCellValue(sample.get("ybbh") != null?(String)sample.get("ybbh"):"");// sample
                        row.createCell(17).setCellValue(orderNumber);// order_number
                        row.createCell(18).setCellValue(product_id);// product_id
                        row.createCell(19).setCellValue("");// Confidence
                        row.createCell(20).setCellValue(sample.get("wzlx") != null?(String)sample.get("wzlx"):"");// Gram_type
                    }
                }
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath +File.separator+ fileName)) {
            workbook.write(outputStream); // 将工作簿写入文件
            workbook.close(); // 关闭工作簿释放资源
        } catch (Exception e){
            log.error("调用先声接口------生信回传--样本编号：" + map.get("ybbh") + "--返回结果：" + e.getMessage());
            return null;
        }
        return filePath +File.separator+ fileName;
    }

    /**
     * 标本接收通知
     */
    private void confirmXianshengInfo() {
        List<WbsjxxDto> listBySjid = wbsjxxService.getListBySjid(map.get("sjid").toString());
        WbsjxxDto wbsjxxDto = listBySjid.get(0);
        if (StringUtil.isNotBlank(wbsjxxDto.getInfojson())) {
            Map<String,Object> jsonMap = (Map<String, Object>) JSON.parse(wbsjxxDto.getInfojson());
            if (StringUtil.isBlank((String) jsonMap.get("receiveFlg")) || "0".equals((String) jsonMap.get("receiveFlg"))){
                String XIANSHENG_URL = map.get("XIANSHENG_URL").toString();
                if(XIANSHENG_URL.endsWith("/"))
                    XIANSHENG_URL = XIANSHENG_URL.substring(0, XIANSHENG_URL.length() - 1);
                String url = XIANSHENG_URL + XIANSHENG_INFO_CONFIRM_URL;

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                requestHeaders.set("source","22");
                //ts:时间戳,毫秒
                String ts = String.valueOf(System.currentTimeMillis());
                requestHeaders.set("ts", ts);
                requestHeaders.set("sign", new Encrypt().stringToMD5("e8fae5d61ef7c2afda1b62c178ebcb71"+ts));
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("serviceBillId",jsonMap.get("serviceBillId").toString());
                requestBody.put("goodsCode",jsonMap.get("goodsCode").toString());
                List<String> sampleCodeList = new ArrayList<>();
                sampleCodeList.add((String) map.get("ybbh"));
                requestBody.put("sampleCodeList",sampleCodeList);
//                HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(requestBody),requestHeaders);
                HttpEntity httpEntity = new HttpEntity(requestBody,requestHeaders);
                log.error("调用先声接口------接受确认--样本编号：" + map.get("ybbh") + "--发送结果：" + JSON.toJSONString(httpEntity));
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                String result = response.getBody();
                log.error("调用先声接口------接受确认--样本编号：" + map.get("ybbh") + "--返回结果：" + result);
            } else {
                log.error("调用先声接口------接受确认--样本编号：" + map.get("ybbh") + "--返回结果：失败!该样本已接收确认!");
            }
        } else {
            log.error("调用先声接口------接受确认--样本编号：" + map.get("ybbh") + "--返回结果：失败!未获取到外部送检信息!");
        }
    }

    /**
     * 调用先声获取样本接口,并返回map
     * @return
     * @throws Exception
     */
    private Map<String, Object> receiveInfoMap() {
        try {
            String XIANSHENG_ORDERURL = map.get("XIANSHENG_ORDERURL").toString();
            String appId = map.get("XIANSHENG_APPID").toString();
            String appSecret = map.get("XIANSHENG_APPSECRET").toString();
            String business = map.get("XIANSHENG_BUSINESS").toString();
            if(XIANSHENG_ORDERURL.endsWith("/"))
                XIANSHENG_ORDERURL = XIANSHENG_ORDERURL.substring(0, XIANSHENG_ORDERURL.length() - 1);
            String url = XIANSHENG_ORDERURL + XIANSHENG_INFO_GET_URL;
            String orderId = String.valueOf(map.get("orderId"));
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.set("appId", appId);
            requestHeaders.set("appSecret", appSecret);
            requestHeaders.set("business", business);
            long timestamp = System.currentTimeMillis();
            requestHeaders.set("timestamp", String.valueOf(timestamp));
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("orderId",orderId);
            requestHeaders.set("sign", xianshengGenerateSign(requestBody,timestamp,appSecret));
            HttpEntity httpEntity = new HttpEntity(requestHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url+"?orderId="+orderId, HttpMethod.GET, httpEntity, String.class);
            if (200 == response.getStatusCodeValue()){
                String body = response.getBody();
                log.error("调用先声接口------请求成功:{}",body);
                Map<String,Object> bodyMap = (Map<String, Object>) JSON.parse(body);
                if (200 == (Integer)bodyMap.get("errorCode")){
                    Map<String,Object> data = (Map<String, Object>) bodyMap.get("data");
                    redisUtil.hdel("HOSPITAL_XIANSHENG",map.get("orderId"));
                    return data;
                } else {
                    log.error("调用先声接口------请求失败 errorcode不是200:{}",JSON.toJSONString(response));
                }
            } else {
                log.error("调用先声接口------请求失败 返回状态不是200:{}",JSON.toJSONString(response));
            }
        } catch (Exception e) {
            log.error("调用先声接口------请求失败 发送异常:{}",e.getMessage());
        }
        return null;
    }
	
    private void CDCdataSave(String filePath) throws Exception {
        List<WbsjxxDto> listBySjid = wbsjxxService.getListBySjid((String) map.get("sjid"));
        map.put("wbsjxxDtos",listBySjid);
        // 将Map转成json字符串,并将存储在路径为filePath的文件中
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(JSON.toJSONString(map));
        }
    }

    /**
     * 处理先声样本信息
     */
    public void dealXianshengInfo() {
        Map<String, Object> infoMap = receiveInfoMap();
        //处理返回信息，形成标本List
        List<Map<String,Object>> list = checkAndAdjustInfo(infoMap);

        if (list == null) {
            jkdymxDto.setFhnr(JSON.toJSONString(infoMap)); // 返回内容
            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
            jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
            log.error("处理先声样本信息失败: 未整理出样本信息清单!");
            return;
        }

        String sjqfdm = "";
        //设置快递类型为无
        List<JcsjDto> kdlxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
        String kdlxid = "";
        for (JcsjDto kdlx : kdlxList) {
            if ("QYY".equals(kdlx.getCsdm())) {
                kdlxid = kdlx.getCsid();
                break;
            }
        }
        //10、送检单位(必填)
        YyxxDto yyxxDto = new YyxxDto();
        YyxxDto real_yyxxDto = new YyxxDto();
        yyxxDto.setDwmc("第三方");
        List<YyxxDto> yyxxDtos = yyxxService.queryByDwmc(yyxxDto);
        if (!CollectionUtils.isEmpty(yyxxDtos)) {
            real_yyxxDto = yyxxDtos.get(0);
        }

        //设置送检区分为特检
        String sjqfid = "";
        List<JcsjDto> sjqfList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));

        SjhbxxDto paramDto=new SjhbxxDto();
        paramDto.setHbmc("南京先声");
        SjhbxxDto sjhbxxDto=sjhbxxService.selectSjhb(paramDto);
        if(sjhbxxDto!=null){
            if(StringUtil.isNotBlank(sjhbxxDto.getSjqf())){
                for (JcsjDto sjqf : sjqfList) {
                    if (sjhbxxDto.getSjqf().equals(sjqf.getCsid())) {
                        sjqfid = sjqf.getCsid();
                        sjqfdm = sjqf.getCsdm();
                        break;
                    }
                }
            }
        }
        if (StringUtil.isBlank(sjqfid)) {
            for (JcsjDto sjqf : sjqfList) {
                if ("1".equals(sjqf.getSfmr())) {
                    sjqfid = sjqf.getCsid();
                    sjqfdm = sjqf.getCsdm();
                    break;
                }
            }
        }

        int updateCount = 0;
        int insertCount = 0;
        int totalCount = 0;
        int alreadyCount = 0;
        List<String> unCompareInfo = new ArrayList<>();
        for (Map<String, Object> sampleMap : list) {
            totalCount++;
            SjxxDto sjxxDto = xianshengToSjxx(sampleMap,  real_yyxxDto, unCompareInfo);
            Map<String, Object> resultMap = dealSaveSjxxDto(sjxxDto, sjqfdm, kdlxid, sjqfid, "hospitalXiansheng", JSON.toJSONString(sampleMap));
            if(resultMap.get("STATUS") != null && ("insert".equals(resultMap.get("STATUS")) || "update".equals(resultMap.get("STATUS")) || "exist".equals(resultMap.get("STATUS")))){
                SjkzxxDto sjkzxxDto = new SjkzxxDto();
                sjkzxxDto.setSjid(sjxxDto.getSjid());
                map.put("limsTestCode",sampleMap.get("limsTestCode"));
                map.put("samplerCode",sampleMap.get("samplerCode"));
                map.put("patientId",sampleMap.get("patientId"));
                map.put("labName",sampleMap.get("labName"));
                map.put("labCode",sampleMap.get("labCode"));
                map.put("detectionType",sampleMap.get("detectionType"));
                map.put("jcxmmc",sampleMap.get("jcxmmc"));
                sjkzxxDto.setQtxx(JSON.toJSONString(map));
                sjkzxxDto.setXgry("hospitalXiansheng");
                boolean result = sjkzxxService.update(sjkzxxDto);
                if (!result){
                    sjkzxxDto.setLrry("hospitalXiansheng");
                    sjkzxxService.insertDto(sjkzxxDto);
                }
                WbsjxxDto wbsjxxDto = new WbsjxxDto();
                wbsjxxDto.setSjid(sjxxDto.getSjid());
                wbsjxxDto.setSjbm((String) map.get("samplerCode"));
                Map<String, Object> infojson = new HashMap<>();
//                        infojson.put("getSampleFlg","0");
                infojson.put("receiveFlg","0");
                infojson.put("bioinfoResultFlg","0");
                infojson.put("customerUrl","");
                infojson.put("limsTestCode",sampleMap.get("limsTestCode"));
                infojson.put("goodsCode",sampleMap.get("limsTestCode"));
                infojson.put("orderId",map.get("orderId"));
                infojson.put("serviceBillId",sampleMap.get("serviceBillId"));
                wbsjxxDto.setInfojson(JSON.toJSONString(infojson));
                WbsjxxDto dtoBySjid = wbsjxxService.getDtoBySjid(wbsjxxDto.getSjid());
                if (dtoBySjid != null){
                    wbsjxxDto.setId(dtoBySjid.getId());
                    wbsjxxService.update(wbsjxxDto);
                } else {
                    wbsjxxDto.setId(StringUtil.generateUUID());
                    wbsjxxService.insert(wbsjxxDto);
                }
                if(!CollectionUtils.isEmpty(sjxxDto.getSjgzbys())){
                    sjgzbyService.insertBySjxx(sjxxDto);
                }
            }
            String status = (String) resultMap.get("STATUS");
            switch (status) {
                case "insert":
                    insertCount++;
                    break;
                case "update":
                    updateCount++;
                    break;
                case "exist":
                    alreadyCount++;
                    break;
                default:
                    break;
            }
        }
        redisUtil.hdel("HOSPITAL_XIANSHENG",map.get("orderId"));
        jkdymxDto.setQtxx("先声：获取样本信息成功-----结果：共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
        jkdymxDto.setFhnr(JSON.toJSONString(list).length()>4000?JSON.toJSONString(list).substring(0,4000):JSON.toJSONString(list)); // 返回内容
        jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
        jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
        log.error("先声：获取样本信息成功-----结果：共获取到" + totalCount + "条数据,新增" + insertCount + "条数据,更新" + updateCount + "条数据,已接收不执行操作数据" + alreadyCount + "条！");
        sendMessage(unCompareInfo, null, null, "先声对接系统，报告回传失败！");
    }

    /**
     * 根据返回结果，检查并调整信息 ，形成标本List
     * @param infoMap
     */
    private List<Map<String,Object>> checkAndAdjustInfo(Map<String, Object> infoMap){
        List<Map<String,Object>> list = new ArrayList<>();

        // 处理Map信息，打平结构
        if (infoMap == null || infoMap.isEmpty()) {
            //接口调用明细 -- 设置返回内容，返回结果
            log.error("先声：获取样本信息失败-----未获取到返回信息-----。");
            return null;
        }

        if (infoMap.get("limsTestCode") == null || StringUtil.isBlank((String)infoMap.get("limsTestCode"))) {
            log.error("先声：获取样本信息失败-----未获取到项目信息-----。");
            return null;
        }

        List<Map<String,Object>> serviceItemList = (List<Map<String, Object>>) infoMap.get("serviceItemList");
        if (CollectionUtils.isEmpty(serviceItemList)) {
            //接口调用明细 -- 设置返回内容，返回结果

            log.error("先声：获取样本信息失败-----未获取到样本信息-----结果：" + JSON.toJSONString(infoMap.get("serviceItemList")));
            return null;
        }

        // 处理样本信息,暂时先去除不需要的字段
        Map<String, Object> expFiledsInfo = (Map<String, Object>)infoMap.get("expFileds");
        Object o_clinicalInfoData = infoMap.get("clinicalInfoData");
        infoMap.remove("expFileds");
        infoMap.remove("clinicalInfo");
        infoMap.remove("clinicalInfoData");
        infoMap.remove("serviceItemList");
        Map<String, Object> map_clinicalInfoData = null;
        if(o_clinicalInfoData!=null && StringUtil.isNotBlank((String)o_clinicalInfoData)){
            map_clinicalInfoData = JSON.parseObject((String)o_clinicalInfoData, Map.class);
            infoMap.putAll(map_clinicalInfoData);
        }

        if(expFiledsInfo!=null && !expFiledsInfo.isEmpty()){
            Map<String, Object> map_expFiledsInfo = new HashMap<>();
            Object[] expFiledsKeys = expFiledsInfo.keySet().toArray();
            for (Object ck : expFiledsKeys) {
                String childMapKey =ck.toString();
                if (expFiledsInfo.get(childMapKey) != null) {
                    map_expFiledsInfo.put("expFileds."+childMapKey,expFiledsInfo.get(childMapKey));
                }
            }
            infoMap.putAll(map_expFiledsInfo);
        }

        for (Map<String, Object> serviceItem : serviceItemList) {
            if (serviceItem.get("samplerInfoList")== null || serviceItem.get("limsTestCode") == null || StringUtil.isBlank((String)serviceItem.get("limsTestCode"))) {
                continue;
            }
            List<Map<String,Object>> samplerInfoList = (List<Map<String, Object>>)serviceItem.get("samplerInfoList");
            for (Map<String, Object> samplerInfo : samplerInfoList) {
                samplerInfo.putAll(infoMap);
                list.add(samplerInfo);
            }
        }
        return list;
    }
        
    /**
     * 先声数据转换成sjxxDto
     * @param map
     * @param real_yyxxDto
     * @param unCompareInfo
     * @return
     */
    private SjxxDto xianshengToSjxx(Map<String, Object> map,  YyxxDto real_yyxxDto, List<String> unCompareInfo) {

        List<SjdwxxDto> sjdwxxlist = sjxxService.getSjdw();//科室信息List
        List<JcsjDto> yblxList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//样本类型List
        List<JcsjDto> jcxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
        List<JcsjDto> jcdwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
        List<JcsjDto> jczxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode()));//检测子项目List
        SjxxDto sjxxDto = new SjxxDto();
        sjxxDto.setWbbm(map.get("samplerCode").toString());
        //1、样本编号(必填)
        sjxxDto.setYbbh((String) map.get("samplerCode"));
        //1、外部编码
        sjxxDto.setWbbm((String) map.get("samplerCode"));
        //2、患者姓名(必填)
        sjxxDto.setHzxm((String) map.get("patientName"));
        //3、性别(必填)(sjxx.xb	1男2女3未知)
        String GenderName = map.get("sex") != null ? String.valueOf(map.get("sex")) : "";
        switch (GenderName) {
            case "男":
                sjxxDto.setXb("1");
                break;
            case "女":
                sjxxDto.setXb("2");
                break;
            case "1":
                sjxxDto.setXb("1");
                break;
            case "2":
                sjxxDto.setXb("2");
                break;
            default:
                sjxxDto.setXb("3");
                break;
        }
        //4、年龄(必填)(sjxx.nl + sjxx.nldw)
        String nl=map.get("timeBirthday")!=null?map.get("timeBirthday").toString().split(" ")[0]:"0";
        String nldw = "岁";
        sjxxDto.setNl(calculateAge(nl,"yyyy-MM-dd") );//年龄 => sjxx.nl + sjxx.nldw
        sjxxDto.setNldw(nldw);
        //5、送检医生(必填)
        sjxxDto.setSjys(map.get("expFileds.doctorName") != null ? map.get("expFileds.doctorName").toString() : "-");
        //6、科室(必填)
        String ksmc = map.get("expFileds.departmentName") != null ? map.get("expFileds.departmentName").toString() : "-";
        String qtsjdwid = "";
        for (SjdwxxDto sjdwxx : sjdwxxlist) {
            if (sjdwxx.getDwmc().equals(ksmc)) {
                sjxxDto.setKs(sjdwxx.getDwid());
                sjxxDto.setQtks(ksmc);
                sjxxDto.setKsmc(ksmc);
                break;
            } else if (StringUtil.isNotBlank(sjdwxx.getKzcs3())) {
                String[] ppkss = sjdwxx.getKzcs3().split(",");
                boolean isCompare = false;
                for (String ppks : ppkss) {
                    if (ppks.equals(ksmc)) {
                        sjxxDto.setKs(sjdwxx.getDwid());
                        sjxxDto.setQtks(ksmc);
                        sjxxDto.setKsmc(ksmc);
                        isCompare = true;
                        break;
                    }
                }
                if (isCompare) {
                    break;
                }
            }
            if ("00999".equals(sjdwxx.getDwdm())) {
                qtsjdwid = sjdwxx.getDwid();
            }
        }
        if (StringUtil.isBlank(sjxxDto.getKs())) {
            sjxxDto.setKs(qtsjdwid);
            sjxxDto.setQtks(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            sjxxDto.setKsmc(StringUtil.isNotBlank(ksmc) ? ksmc : "-");
            log.error("先声：处理送检信息-----科室信息未匹配到！------标本:" + sjxxDto.getYbbh() + "-----科室名称:" + (StringUtil.isNotBlank(ksmc) ? ksmc : "-"));
        }
        //7、住院号
        sjxxDto.setZyh(map.get("住院号") != null ? map.get("住院号").toString() : "");
        //8、床位号
        sjxxDto.setCwh(map.get("床号") != null ? map.get("床号").toString() : "");
        //9、电话
        //sjxxDto.setDh(map.get("patientPhone") != null ? map.get("patientPhone").toString() : "");
        //10、送检单位(必填)
        sjxxDto.setSjdw(real_yyxxDto.getDwid());
        sjxxDto.setSjdwmc(real_yyxxDto.getDwmc());
        map.put("EntrustHosName",map.get("labName") != null ? map.get("labName").toString() : "");

        if (map.get("送检实验室")!=null) {
            Optional<JcsjDto>opt=jcdwList.stream().filter(e->e.getCsmc().equals(map.get("送检实验室"))).filter(e->"0".equals(e.getScbj())).findFirst();
            if(opt.isPresent()){
                sjxxDto.setJcdw(opt.get().getCsid());
                sjxxDto.setJcdwmc(opt.get().getCsmc());
            }else{
                XxdyDto paramDto1=new XxdyDto();
                paramDto1.setYxx("南京先声-"+map.get("送检实验室"));
                List<XxdyDto>xxdyDtoList1=xxdyService.getDtoMsgByYxx(paramDto1);
                if(!CollectionUtils.isEmpty(xxdyDtoList1)){
                    String csid=xxdyDtoList1.get(0).getDyxx();
                    List<JcsjDto> jcsjDtoList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
                    Optional<JcsjDto>opt1=jcsjDtoList.stream().filter(e->e.getCsid().equals(csid)).findFirst();
                    if(opt1.isPresent()){
                        sjxxDto.setJcdw(csid);
                        sjxxDto.setJcdwmc(opt1.get().getCsmc());
                    }else{
                        for (JcsjDto jc_jcdw : jcdwList) {
                            if ("1".equals(jc_jcdw.getSfmr())) {
                                sjxxDto.setJcdw(jc_jcdw.getCsid());
                                sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                                break;
                            }
                        }
                    }
                }else{
                    for (JcsjDto jc_jcdw : jcdwList) {
                        if ("1".equals(jc_jcdw.getSfmr())) {
                            sjxxDto.setJcdw(jc_jcdw.getCsid());
                            sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                            break;
                        }
                    }
                }

            }
        } else {
            for (JcsjDto jc_jcdw : jcdwList) {
                if ("1".equals(jc_jcdw.getSfmr())) {
                    sjxxDto.setJcdw(jc_jcdw.getCsid());
                    sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                    break;
                }
            }
        }
        //12、合作伙伴(必填)
//        String cskz3 = real_yyxxDto.getCskz3();
//        if (StringUtil.isNotBlank(cskz3)) {
//            String[] cskz3s = cskz3.split(",");
//            sjxxDto.setDb(cskz3s[0]);
//        } else {
//            log.error("先声：处理送检信息-----合作伙伴信息未匹配到！------标本:" + sjxxDto.getYbbh());
//        }
        sjxxDto.setDb("南京先声");
        //13、检测项目(必填)
        XxdyDto paramDto=new XxdyDto();
        paramDto.setYxx("南京先声-"+map.get("limsTestCode").toString());
        List<XxdyDto>xxdyDtoList=xxdyService.getDtoMsgByYxx(paramDto);

        List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(xxdyDtoList)){
            Optional<JcsjDto>opt=jcxmList.stream().filter(e->e.getCsid().equals(xxdyDtoList.get(0).getDyxx())).findFirst();
            Optional<JcsjDto>opt_zxm=jczxmList.stream().filter(e->e.getCsmc().equals(xxdyDtoList.get(0).getKzcs4())).findFirst();

            sjxxDto.setJcxmmc(opt.get().getCsmc());
            SjjcxmDto sjjcxmDto = new SjjcxmDto();
            sjjcxmDto.setJcxmid(xxdyDtoList.get(0).getDyxx());
            sjjcxmDto.setSfsf("1");
            if(opt_zxm.isPresent()){
                sjjcxmDto.setJczxmid(opt_zxm.get().getCsid());
                sjxxDto.setJcxmmc(opt.get().getCsmc()+"-"+opt_zxm.get().getCsmc());
            }
            sjjcxmDtos.add(sjjcxmDto);
            sjxxDto.setSjjcxms(sjjcxmDtos);
            map.put("detectionType",xxdyDtoList.get(0).getKzcs2());
            map.put("jcxmmc",xxdyDtoList.get(0).getKzcs2());
        }

//        String jcxmdm = map.get("applyItemCodes") != null ? map.get("applyItemCodes").toString() : "";
//        String jcxmmc = map.get("applyItemNames") != null ? map.get("applyItemNames").toString() : "";
//        if (StringUtil.isNotBlank(jcxmmc)) {
//            jcxmmc = jcxmmc.replaceAll("，", ",");
//            jcxmdm = jcxmdm.replaceAll("，", ",");
//            String[] jcxmdms = jcxmdm.split(",");
//            List<SjjcxmDto> sjjcxmDtos = new ArrayList<>();
//            //匹配检测项目
//            String jcxmpp = real_yyxxDto.getJcxmpp();//获取匹配到的医院的检测项目匹配字段
//            for (String s : jcxmdms) {
//                String jcsjCskz3 = "IMP_REPORT_ONCO_QINDEX_TEMEPLATE";//默认Q_mNGS
//                String jcsjCskz1 = "";
//                if (StringUtil.isNotBlank(jcxmpp)) {
//                    String jcxmppdm = "";
//                    //若jcxmpp不为空
//                    String[] pps = jcxmpp.split(";");
//                    for (String pp : pps) {
//                        String[] ppnr = pp.split(":");
//                        if (s.equals(ppnr[0])) {
//                            jcxmppdm = ppnr[1];
//                            break;
//                        }
//                    }
//                    if (StringUtil.isNotBlank(jcxmppdm)) {
//                        String[] cskzs = jcxmppdm.split(",");
//                        jcsjCskz3 = cskzs[0];
//                        jcsjCskz1 = cskzs[1];
//                    }
//                    for (JcsjDto jc_jcxm : jcxmList) {
//                        if (jcsjCskz3.equals(jc_jcxm.getCskz3())) {
//                            SjjcxmDto sjjcxmDto = new SjjcxmDto();
//                            if (jcsjCskz3.equals(jc_jcxm.getCskz3()) && jcsjCskz1.equals(jc_jcxm.getCskz1())) {
//                                sjjcxmDto.setJcxmid(jc_jcxm.getCsid());
//                                sjjcxmDtos.add(sjjcxmDto);
//                                sjxxDto.setSjjcxms(sjjcxmDtos);
//                                sjxxDto.setJcxmmc((StringUtil.isNotBlank(sjxxDto.getJcxmmc()) ? (sjxxDto.getJcxmmc() + ",") : "") + jc_jcxm.getCsmc());
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
        //若文字未匹配上，设置为默认检测项目
        if (StringUtil.isBlank(sjxxDto.getJcxmmc())) {
            log.error("先声：处理送检信息-----检测项目未匹配，请手动匹配！------标本:" + sjxxDto.getYbbh() + ",检测项目:" + sjxxDto.getJcxmmc());
        }
        //14、样本类型(必填)
        String yblxmc = (String) map.get("sampleTypeName");
        compareYblx(sjxxDto,yblxList,yblxmc,redisUtil);
        //15、前期诊断(必填)
        sjxxDto.setQqzd(map.get("临床诊断（包括影像学）") != null ? map.get("临床诊断（包括影像学）").toString() : "-");
        //17、临床症状(必填)
        sjxxDto.setLczz(map.get("临床描述") != null ? map.get("临床描述").toString() : "-");
        //18、采样时间(必填)
        sjxxDto.setCyrq(map.get("timeSampling") != null ? map.get("timeSampling").toString() : "");

        //20、备注
        //String bz = map.get("sampleRemark") != null ? map.get("sampleRemark").toString() : "";
        //sjxxDto.setBz(bz);
        //21 关注病原
            if(map.get("重点关注病原")!=null){
            List<SjgzbyDto>bys=new ArrayList<>();
            String gzbyStr=map.get("重点关注病原").toString();
            List<JcsjDto> byList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PATHOGENY_TYPE.getCode());

            String[] gzbyArr=gzbyStr.split("、");
            for(String gzby:gzbyArr){
                if(StringUtil.isNotBlank(gzby)){
                    Optional<JcsjDto> byDto=byList.stream().filter(e->e.getCsmc().indexOf(gzby)!=-1).findFirst();
                    if(byDto.isPresent()){
                        SjgzbyDto sjgzbyDto=new SjgzbyDto();
                        sjgzbyDto.setBy(byDto.get().getCsid());
                        bys.add(sjgzbyDto);
                    }
                }
            }
            sjxxDto.setSjgzbys(bys);
        }
        return sjxxDto;
    }

    /**
     * 先声生成sign
     *
     * @param map       地图
     * @param timeStamp 时间戳
     * @param secret    秘密
     * @return {@link String }
     */
    private String xianshengGenerateSign(Map<String, Object> map, long timeStamp, String secret){
        try {
            Set<Map.Entry<String, Object>> set = map.entrySet();
            Iterator<Map.Entry<String, Object>> it = set.iterator();
            Map<String,String> pras = new HashMap<String, String>();
            List<String> keys = new ArrayList<String>();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey().trim();
                String value = String.valueOf(entry.getValue());
                if(!"sign".equals(key) && StringUtils.hasText(value)){
                    keys.add(key);
                    pras.put(key, value.trim());
                }
            }
            Collections.sort(keys);
            StringBuffer bf = new StringBuffer();
            for(int i=0;i<keys.size();i++){
                bf.append(keys.get(i)).append("=");
                bf.append(pras.get(keys.get(i))+"&");
            }
            bf.append(timeStamp).append("&").append(secret);
            Encrypt encrypt = new Encrypt();
            String md5 = encrypt.stringToMD5(bf.toString());
            log.error("generateSign key :: {}, md5 sign :: {}", bf.toString(), md5);
            return md5;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return "";
    }

    /**
     * @Description: 艾迪康获取样本接口
     * @param sampleCode
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/6/23 15:28
     */
    public Map<String, Object> queryADKSample(String sampleCode,String logid,String password,String requesturl){
        Map<String, Object> resultMap = new HashMap<>();
        String token = queryADKToken(logid,password,requesturl);
        if(StringUtil.isBlank(token)) {
            return null;
        }
        String jsonStr = getADKSample(token, sampleCode, requesturl);
        if(StringUtil.isBlank(jsonStr)) {
            return null;
        }
        log.error("艾迪康系统获取到的jsonStr:"+jsonStr);
        Map<String, Object> jsonMap = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>(){});
        if(jsonMap == null)
            return null;
        if(jsonMap.get("data")!=null && StringUtil.isNotBlank(jsonMap.get("data").toString())){
            resultMap = JSON.parseObject(jsonMap.get("data").toString(), new TypeReference<Map<String, Object>>(){});
        }else if("error".equals(jsonMap.get("code"))){
            resultMap.put("code", "error");
            resultMap.put("message", jsonMap.get("message"));
        }
        return resultMap;
    }

    /**
     * @Description: 调用艾迪康接口获取样本详情
     * @param token
     * @param sampleCode
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2025/6/25 10:58
     */
    public String getADKSample(String token,String sampleCode,String requesturl){
        if(StringUtil.isBlank(token) || StringUtil.isBlank(sampleCode) || StringUtil.isBlank(requesturl)) {
            return null;
        }
        try{
            String jsonBody = "{\"orgcode\": \"1019\", \"barcode\":\""+sampleCode+"\"}";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requesturl+ADK_GETAPPLICATIONDETAIL_URL))
                    .header("Authorization", token)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))  // 设置JSON请求体
                    .build();
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            return response.body();
        }catch (Exception e){
            log.error("艾迪康接口获取样本信息失败!"+e.getMessage());
            return null;
        }
    }

    /**
     * @Description: 获取艾迪康Token
     * @param
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2025/6/23 16:45
     */
    public String queryADKToken(String logid,String password,String requesturl){
        if(StringUtil.isBlank(logid) || StringUtil.isBlank(password) || StringUtil.isBlank(requesturl)){
            return null;
        }
        try {
            DBEncrypt p = new DBEncrypt();
            String jsonBody = "{\"userName\":\""+p.dCode(logid)+"\",\"userPwd\":\""+p.dCode(password)+"\"}";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requesturl+ADK_TOKEN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            if (response.statusCode() != 200) {
                log.error("请求失败: HTTP " + response.statusCode());
                return null;
            }
            Map<String,Object> objectMap = JSON.parseObject(response.body(),new TypeReference<Map<String,Object>>(){});
            String token = "";
            if(objectMap.get("data")!=null && StringUtil.isNotBlank(objectMap.get("data").toString())) {
                Map<String,Object> data = JSON.parseObject(objectMap.get("data").toString(),new TypeReference<Map<String,Object>>(){});
                if(data.get("access_token")!=null){
                    token = "Bearer "+data.get("access_token").toString();
                }
            }
            return token;
        } catch (Exception e) {
            log.error("获取Token失败", e);
            return null;
		}
    }

    public SjxxDto adkAssembleData(Map<String,Object> akdMap,RedisUtil tmpRedisUtil,ISjxxService tmpSjxxService,IYyxxService tmpYyxxService,IXxdyService tmpXxdyService){
        SjxxDto sjxxDto = new SjxxDto();
        if(akdMap==null){
            return sjxxDto;
        }
        if(akdMap.get("patientname")!=null && StringUtil.isNotBlank(akdMap.get("patientname").toString())) {
            sjxxDto.setHzxm(akdMap.get("patientname").toString());
        }
        if(akdMap.get("age")!=null && StringUtil.isNotBlank(akdMap.get("age").toString())) {
            String ageStr = akdMap.get("age").toString();
            int index = ageStr.indexOf('^');
            if (index != -1) {
                String dw = ageStr.substring(index + 1);
                if("月".equals(dw)){
                    sjxxDto.setNl(ageStr.substring(0, index));
                    sjxxDto.setNldw("个月");
                }else if("日".equals(dw) || "天".equals(dw)){
                    sjxxDto.setNl(ageStr.substring(0, index));
                    sjxxDto.setNldw("天");
                }else if("岁".equals(dw)){
                    sjxxDto.setNl(ageStr.substring(0, index));
                    sjxxDto.setNldw("岁");
                }else{
                    sjxxDto.setNl(ageStr.substring(0, index)+dw);
                    sjxxDto.setNldw("无");
                }
            }else {
                sjxxDto.setNl(ageStr);
                sjxxDto.setNldw("无");
            }
        }
        if(akdMap.get("sex")!=null && StringUtil.isNotBlank(akdMap.get("sex").toString())) {
            if("男".equals(akdMap.get("sex").toString())){
                sjxxDto.setXb("1");
            }else if("女".equals(akdMap.get("sex").toString())){
                sjxxDto.setXb("2");
            }else {
                sjxxDto.setXb("3");
            }
        }
        if(akdMap.get("patienttel")!=null && StringUtil.isNotBlank(akdMap.get("patienttel").toString())) {
            sjxxDto.setDh(akdMap.get("patienttel").toString());
        }
        if(akdMap.get("Requestdoctor")!=null && StringUtil.isNotBlank(akdMap.get("Requestdoctor").toString())) {
            sjxxDto.setSjys(akdMap.get("Requestdoctor").toString());
        }
        if(akdMap.get("requestdoctortel")!=null && StringUtil.isNotBlank(akdMap.get("requestdoctortel").toString())) {
            sjxxDto.setYsdh(akdMap.get("requestdoctortel").toString());
        }
        if(akdMap.get("patientno")!=null && StringUtil.isNotBlank(akdMap.get("patientno").toString())) {
            sjxxDto.setZyh(akdMap.get("patientno").toString());
        }
        if(akdMap.get("patientbed")!=null && StringUtil.isNotBlank(akdMap.get("patientbed").toString())) {
            sjxxDto.setCwh(akdMap.get("patientbed").toString());
        }
        if(akdMap.get("sampletime")!=null && StringUtil.isNotBlank(akdMap.get("sampletime").toString())) {
            sjxxDto.setCyrq(akdMap.get("sampletime").toString());
        }
        List<SjdwxxDto> sjdwxxlist = tmpSjxxService.getSjdw();//科室信息List
        List<JcsjDto> yblxList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//样本类型List
        List<JcsjDto> jcxmList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目List
        List<JcsjDto> jcdwList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位List
        List<JcsjDto> kdlxList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
        List<JcsjDto> sjqfList = tmpRedisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
        if(!CollectionUtils.isEmpty(sjdwxxlist) && akdMap.get("requesthispitaldepart")!=null && StringUtil.isNotBlank(akdMap.get("requesthispitaldepart").toString())){
            String dept_name = akdMap.get("requesthispitaldepart").toString();
            String qtsjdwid = "";
            for (SjdwxxDto sjdwxx : sjdwxxlist) {
                if (sjdwxx.getDwmc().equals(dept_name)) {
                    sjxxDto.setKs(sjdwxx.getDwid());
                    if ("1".equals(sjdwxx.getKzcs())) {
                        //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                        sjxxDto.setQtks(dept_name);
                        sjxxDto.setKsmc(dept_name);
                    }
                    break;
                } else if (StringUtil.isNotBlank(sjdwxx.getKzcs3())) {
                    String[] ppkss = sjdwxx.getKzcs3().split(",");
                    boolean isCompare = false;
                    for (String ppks : ppkss) {
                        if (ppks.equals(dept_name)) {
                            sjxxDto.setKs(sjdwxx.getDwid());
                            if ("1".equals(sjdwxx.getKzcs())) {
                                //若匹配上科室，且科室的kzcs=1,则将科室名称存入'qtks'中
                                sjxxDto.setQtks(dept_name);
                                sjxxDto.setKsmc(dept_name);
                            }
                            isCompare = true;
                            break;
                        }
                    }
                    if (isCompare) {
                        break;
                    }
                }
                if ("00999".equals(sjdwxx.getDwdm())) {
                    qtsjdwid = sjdwxx.getDwid();
                }
            }
            if (StringUtil.isBlank(sjxxDto.getKs())) {
                sjxxDto.setKs(qtsjdwid);
                sjxxDto.setQtks(StringUtil.isNotBlank(dept_name) ? dept_name : "-");
                sjxxDto.setKsmc(StringUtil.isNotBlank(dept_name) ? dept_name : "-");
                log.error("创惠：处理送检信息-----科室信息未匹配到！------标本:" + sjxxDto.getYbbh() + "-----科室名称:" + (StringUtil.isNotBlank(dept_name) ? dept_name : "-"));
            }
        }
        if(akdMap.get("requesthispitalname")!=null && StringUtil.isNotBlank(akdMap.get("requesthispitalname").toString())){
            YyxxDto yyxxDto = new YyxxDto();
            String s_dwmc = akdMap.get("requesthispitalname").toString();
            String[] dwmcs = s_dwmc.replace("）","").split("（");
            List<YyxxDto> yyxxDtos = null;
            for(String dwmc : dwmcs){
                yyxxDto.setDwmc(dwmc);
                yyxxDtos = tmpYyxxService.queryAllByMc(yyxxDto);
                if(yyxxDtos!=null && yyxxDtos.size() > 0 ){
                    break;
                }
            }
            if(!CollectionUtils.isEmpty(yyxxDtos)){
                sjxxDto.setSjdw(yyxxDtos.get(0).getDwid());
                sjxxDto.setHospitalname(akdMap.get("requesthispitalname").toString());
                sjxxDto.setSjdwbj("1");
            }else{
                yyxxDto.setDwmc("第三方");
                yyxxDtos = tmpYyxxService.queryByDwmc(yyxxDto);
                sjxxDto.setSjdwbj("1");
                sjxxDto.setSjdw(yyxxDtos.get(0).getDwid());
                sjxxDto.setHospitalname(yyxxDtos.get(0).getDwmc());
            }
            sjxxDto.setSjdwmc(akdMap.get("requesthispitalname").toString());
        }
        for (JcsjDto kdlx : kdlxList) {
            if ("QYY".equals(kdlx.getCsdm())) {
                sjxxDto.setKdlx(kdlx.getCsid());
                sjxxDto.setKdh("无");
                break;
            }
        }
        for (JcsjDto sjqf : sjqfList) {
            if ("特检".equals(sjqf.getCsmc())) {
                sjxxDto.setSjqf(sjqf.getCsid());
                break;
            }
        }
        for (JcsjDto jc_jcdw : jcdwList) {
            if ("32".equals(jc_jcdw.getCsdm())) {
                sjxxDto.setJcdw(jc_jcdw.getCsid());
                sjxxDto.setJcdwmc(jc_jcdw.getCsmc());
                break;
            }
        }
        sjxxDto.setYbtj("-");
        sjxxDto.setLczz("-");
        sjxxDto.setQqzd("-");
        sjxxDto.setDb("西安艾迪康");
        compareYblx(sjxxDto,yblxList,akdMap.get("sampletype").toString(),tmpRedisUtil);
        compareJcxm(sjxxDto,jcxmList,akdMap.get("testitemname").toString(),akdMap.get("testitemname").toString(),",",tmpXxdyService);
        return sjxxDto;
    }

    /**
     * 重新执行报告生成。先声
     * @param map
     * @return
     */
    private void resetReportSend(Map<String,String> map) {
        //boolean result = true;
        log.error("开始进入南京先声重发报告结果-生信结果流程。");
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("service", "sjxxWsServiceImpl");
            params.put("method", "resendNewReportByException");
            //params.put("resettime", "1,2,3,4,5");
            String resultinfo = map.get("resultinfo");
            if (StringUtil.isBlank(resultinfo)) {
                resultinfo = "";
            } else if (resultinfo.length() > 128) {
                resultinfo = resultinfo.substring(0, 128);
            }
            params.put("key", "先声：sendreport:" + map.get("ybbh") + ":" + resultinfo);
            //第三次的时候进行提醒
            params.put("resetRemindcnt", "3");
            params.put("ifuser", "南京先声");
            params.put("ifname", "报告发送流程" + map.get("subname"));
            params.put("ifmsg", "标本编码" + map.get("ybbh") + "报告发送失败");
            //重试机制的个性化参数
            params.put("sjid", map.get("sjid"));
            params.put("yhm", map.get("yhm"));

            interfaceExceptionUtil.comResetMechanism(params);
        } catch (Exception e) {
            log.error("开始进入南京先声重建报告信息发送流程 发生异常：" + e.getMessage());
        }
    }

    /**
     * @Description: 禾诺获取信息接口
     * @param codeMap
     * @return void
     * @Author: 郭祥杰
     * @Date: 2025/7/14 14:47
     */
    public void timedCallHenuoNew(Map<String, Object> codeMap) {
        String portMesStr = codeMap.get("HENUO_PORTCONFIG")!= null? codeMap.get("HENUO_PORTCONFIG").toString() : "";
        if(StringUtil.isNotBlank(portMesStr)) {
            List<Map<String, Object>> portMesList = JSON.parseObject(portMesStr, new TypeReference<List<Map<String, Object>>>(){});
            if(!CollectionUtils.isEmpty(portMesList)) {
                for (Map<String,Object> portMesMap : portMesList) {
                    if(portMesMap.get("account")!=null && StringUtil.isNotBlank(portMesMap.get("account").toString())
                        && portMesMap.get("password")!=null && StringUtil.isNotBlank(portMesMap.get("password").toString())
                        && portMesMap.get("userType")!=null && StringUtil.isNotBlank(portMesMap.get("userType").toString())
                        && portMesMap.get("entrustHosCode")!=null && StringUtil.isNotBlank(portMesMap.get("entrustHosCode").toString())
                        && portMesMap.get("url")!=null && StringUtil.isNotBlank(portMesMap.get("url").toString())
                        && portMesMap.get("lrry")!=null && StringUtil.isNotBlank(portMesMap.get("lrry").toString())
                        && portMesMap.get("db")!=null && StringUtil.isNotBlank(portMesMap.get("db").toString())){
                        map.put("HENUO_DB",portMesMap.get("db").toString());
                        if(portMesMap.get("dwdm")!=null && StringUtil.isNotBlank(portMesMap.get("dwdm").toString())){
                            map.put("HENUO_DWDM",portMesMap.get("dwdm").toString());
                        }
                        if(portMesMap.get("flag")!=null && StringUtil.isNotBlank(portMesMap.get("flag").toString())){
                            map.put("HENUO_FLAG",portMesMap.get("flag").toString());
                        }
                        map.put("HENUO_URL",portMesMap.get("url").toString());
                        map.put("HENUO_LRRY",portMesMap.get("lrry").toString());
                        if(portMesMap.get("hospitalFlag")!=null && StringUtil.isNotBlank(portMesMap.get("hospitalFlag").toString())){
                            map.put("HENUO_HOSPITALFLAG",portMesMap.get("hospitalFlag").toString());
                        }
                        if(portMesMap.get("hospitalUrl")!=null && StringUtil.isNotBlank(portMesMap.get("hospitalUrl").toString())){
                            map.put("HENUO_HOSPITALURL",portMesMap.get("hospitalUrl").toString());
                        }
                        map.put("HENUO_ACCOUNT",portMesMap.get("account").toString());
                        map.put("HENUO_PASSWORD",portMesMap.get("password").toString());
                        map.put("HENUO_USERTYPE",portMesMap.get("userType").toString());
                        map.put("HENUO_EntrustHosCode",portMesMap.get("entrustHosCode").toString());
                        timedCallHenuo(map);
                    }
                }
            }
        }else{
            timedCallHenuo(codeMap);
        }
    }

    /**
     * 回传报告给禾诺
     * @param mapT 传入参数
     * @return boolean 是否成功
     */
    private void returnHenuoReport(Map<String, Object> mapT) {
        if(map.get("HENUO_PORTCONFIG")!=null && StringUtil.isNotBlank(map.get("HENUO_PORTCONFIG").toString())){
            List<Map<String,Object>> list = JSON.parseObject(map.get("HENUO_PORTCONFIG").toString(),new TypeReference<List<Map<String,Object>>>(){});
            boolean flag = true;
            for (Map<String,Object> maps:list){
                if(maps.get("lrry")!=null && "hospitalHenuo2".equals(maps.get("lrry").toString())
                        && maps.get("account")!=null && StringUtil.isNotBlank(maps.get("account").toString())
                        && maps.get("password")!=null && StringUtil.isNotBlank(maps.get("password").toString())
                        && maps.get("userType")!=null && StringUtil.isNotBlank(maps.get("userType").toString())
                        && maps.get("entrustHosCode")!=null && StringUtil.isNotBlank(maps.get("entrustHosCode").toString())
                        && maps.get("url")!=null && StringUtil.isNotBlank(maps.get("url").toString())){
                    map.put("HENUO_URL",maps.get("url").toString());
                    map.put("HENUO_ACCOUNT",maps.get("account").toString());
                    map.put("HENUO_PASSWORD",maps.get("password").toString());
                    map.put("HENUO_USERTYPE",maps.get("userType").toString());
                    map.put("HENUO_EntrustHosCode",maps.get("entrustHosCode").toString());
                    flag = false;
                    break;
                }
            }
            if(!flag && map.get("wjlj")!=null && StringUtil.isNotBlank(map.get("wjlj").toString())
                && map.get("BarCode")!=null && StringUtil.isNotBlank(map.get("BarCode").toString())
                && map.get("sjid")!=null && StringUtil.isNotBlank(map.get("sjid").toString())){
                SjkzxxDto sjkzxxDto = sjkzxxService.getDtoById(map.get("sjid").toString());
                if(sjkzxxDto!=null && StringUtil.isNotBlank(sjkzxxDto.getQtxx())){
                    String pdfMsg = getPdfMsg(map.get("wjlj").toString());
                    String token = getHenuoToken();
                    Map<String, Object> requestEntrustModel = new HashMap<>();
                    requestEntrustModel.put("entrustHosCode",map.get("HENUO_EntrustHosCode").toString());
                    requestEntrustModel.put("reportNo",map.get("sjid").toString());
                    requestEntrustModel.put("entBarcode",map.get("sjid")!=null?map.get("sjid").toString():"");
                    requestEntrustModel.put("invokeSource","杭州杰毅");
                    requestEntrustModel.put("reportFile","data:application/pdf;base64,"+pdfMsg);
                    Map<String,Object> qtxxMap = JSON.parseObject(sjkzxxDto.getQtxx(),new TypeReference<Map<String,Object>>(){});
                    if(qtxxMap!=null){
                        requestEntrustModel.put("hospitalName",qtxxMap.get("hospitalName")!=null?qtxxMap.get("hospitalName").toString():"");
                        requestEntrustModel.put("barcode",qtxxMap.get("barcode")!=null?qtxxMap.get("barcode").toString():"");
                        requestEntrustModel.put("patientName",qtxxMap.get("patientName")!=null?qtxxMap.get("patientName").toString():"");
                        requestEntrustModel.put("sex",qtxxMap.get("sex")!=null?qtxxMap.get("sex").toString():"");
                        requestEntrustModel.put("age",qtxxMap.get("age")!=null?qtxxMap.get("age").toString():"");
                        requestEntrustModel.put("ageType",qtxxMap.get("ageType")!=null?qtxxMap.get("ageType").toString():"");
                        requestEntrustModel.put("patientType",qtxxMap.get("patientType")!=null?qtxxMap.get("patientType").toString():"");
                        requestEntrustModel.put("sampleCollectionTime",qtxxMap.get("sampleCollectionTime")!=null?qtxxMap.get("sampleCollectionTime").toString():"");
                        requestEntrustModel.put("logisticsTime",qtxxMap.get("logisticsTime")!=null?qtxxMap.get("logisticsTime").toString():"");
                        requestEntrustModel.put("sampleReceiveTime",qtxxMap.get("sampleReceiveTime")!=null?qtxxMap.get("sampleReceiveTime").toString():"");
                        requestEntrustModel.put("sampleTypeName",qtxxMap.get("sampleTypeName")!=null?qtxxMap.get("sampleTypeName").toString():"");
                        List<Map<String,Object>> reportResults = new ArrayList<>();
                        if(qtxxMap.get("sampleItems")!=null && StringUtil.isNotBlank(qtxxMap.get("sampleItems").toString())){
                            List<Map<String,Object>> sampleItemsList = JSON.parseObject(qtxxMap.get("sampleItems").toString(),new TypeReference<List<Map<String,Object>>>(){});
                            for(Map<String,Object> sampleItemsMap:sampleItemsList){
                                Map<String,Object> reportResult = new HashMap<>();
                                reportResult.put("testItemCode_Ext",sampleItemsMap.get("applyItemCode").toString());
                                reportResult.put("testItemName_Ext",sampleItemsMap.get("applyItemName").toString());
                                reportResult.put("result","详见PDF");
                                reportResult.put("testItemName",sampleItemsMap.get("testItemName").toString());
                                reportResult.put("testItemCode",sampleItemsMap.get("testItemCode").toString());
                                reportResults.add(reportResult);
                            }
                        }
                        requestEntrustModel.put("reportResults",reportResults);
                    }
                    if(requestEntrustModel.get("barcode")==null || StringUtil.isBlank(requestEntrustModel.get("barcode").toString())){
                        requestEntrustModel.put("barcode",map.get("Barcode").toString());
                    }
                    requestEntrustModel.put("testedTime","");
                    requestEntrustModel.put("testedUserName","");
                    requestEntrustModel.put("sampleNo","");
                    requestEntrustModel.put("preVerifiedTime","");
                    requestEntrustModel.put("preVerifiedUserName","");
                    requestEntrustModel.put("verifiedTime","");
                    requestEntrustModel.put("verifiedUserName","");
                    requestEntrustModel.put("paperType","");
                    requestEntrustModel.put("rptUseType","");
                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                    requestHeaders.set("Authorization", "Bearer " + token);
                    HttpEntity requestEntity = new HttpEntity(JSON.toJSONString(requestEntrustModel), requestHeaders);
                    jkdymxDto.setNr(JSON.toJSONString(requestEntrustModel));
                    String url = map.get("HENUO_URL").toString()+HENUO_UPLOAD_URL;
                    try {
                        Map<String, Object> result = restTemplate.postForObject(url, requestEntity, Map.class);
                        if (result != null && result.get("success")!=null) {
                            log.error("禾诺：回传报告成功-----结果：" + JSON.toJSONString(result));
                            //接口调用明细 -- 设置返回内容
                            jkdymxDto.setFhnr(JSON.toJSONString(result).length()>4000?JSON.toJSONString(result).substring(0,4000):JSON.toJSONString(result)); // 返回内容
                            jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                            jkdymxDto.setSfcg("true".equals(result.get("success").toString())?"1":"0"); // 是否成功  0:失败 1:成功 2:未知
                            jkdymxService.insertJkdymxDto(jkdymxDto);
                        }
                    } catch (RestClientException e) {
                        log.error("禾诺：回传报告失败-----请求:" + url);
                        log.error("禾诺：回传报告失败-----结果：" + e.getMessage());
                        //接口调用明细 -- 设置返回内容，返回结果
                        jkdymxDto.setFhnr("禾诺：回传报告失败-----结果：" + e.getMessage()); // 返回内容
                        jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
                        jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
                        jkdymxService.insertJkdymxDto(jkdymxDto);
                        reSendRabbit(map,e.getMessage());
                        log.error("禾诺：回传报告失败-----重新发送rabbit!");
                    }
                }
            }
        }
    }
}
