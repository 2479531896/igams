package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.domain.express.TraceDynamicQueryService.queryDynamicTraceInfo.TraceInfo;
import com.lop.open.api.sdk.domain.express.TraceDynamicQueryService.queryDynamicTraceInfo.TraceQueryResponse;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.plugin.factory.OAuth2PluginFactory;
import com.lop.open.api.sdk.request.express.QueryDynamictraceinfoLopRequest;
import com.lop.open.api.sdk.response.express.QueryDynamictraceinfoLopResponse;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.CheckControl;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.AuditResult;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.MQQueueModel;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.dao.post.IFjcfbDao;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.ErrorTypeEnum;
import com.matridx.igams.common.enums.ExpressTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.impl.MatridxByteArrayResource;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFileInspectionImport;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IStatisService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.InterfaceExceptionUtil;
import com.matridx.igams.common.util.POIFileUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.CwglDto;
import com.matridx.igams.wechat.dao.entities.DdyhDto;
import com.matridx.igams.wechat.dao.entities.ExternalMessageModel;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.HbsfbzDto;
import com.matridx.igams.wechat.dao.entities.KdxxDto;
import com.matridx.igams.wechat.dao.entities.MxxxDto;
import com.matridx.igams.wechat.dao.entities.NewHttpServletRequest;
import com.matridx.igams.wechat.dao.entities.ResFirstDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjgzbyDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjkdxxDto;
import com.matridx.igams.wechat.dao.entities.SjkjxxDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjlczzDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjqqjcDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjsyglModel;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjysxxDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqDto;
import com.matridx.igams.wechat.dao.entities.SysxxDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.dao.entities.WbsjxxDto;
import com.matridx.igams.wechat.dao.entities.WeChatDetailedReportModel;
import com.matridx.igams.wechat.dao.entities.WeChatDetailedResultModel;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionReportModel;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionReportsModel;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionResistanceModel;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionSpeciesModel;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.XmsyglDto;
import com.matridx.igams.wechat.dao.entities.XmsyglModel;
import com.matridx.igams.wechat.dao.entities.YhsjxxjgDto;
import com.matridx.igams.wechat.dao.post.IFjsqDao;
import com.matridx.igams.wechat.dao.post.ISjtssqDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.dao.post.ISjxxTwoDao;
import com.matridx.igams.wechat.dao.post.ISjxxWsDao;
import com.matridx.igams.wechat.dao.post.ISjzzsqDao;
import com.matridx.igams.wechat.dao.post.IWxyhDao;
import com.matridx.igams.wechat.service.svcinterface.ICwglService;
import com.matridx.igams.wechat.service.svcinterface.IDdyhService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IHbdwqxService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.igams.wechat.service.svcinterface.IKdxxService;
import com.matridx.igams.wechat.service.svcinterface.IMxxxService;
import com.matridx.igams.wechat.service.svcinterface.INyypxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjdlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjgzbyService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjkdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjkjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjlczzService;
import com.matridx.igams.wechat.service.svcinterface.ISjnbbmService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjpdglService;
import com.matridx.igams.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxCommonService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxResStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.igams.wechat.service.svcinterface.ISjysxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.igams.wechat.service.svcinterface.ISysxxService;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.igams.wechat.service.svcinterface.IWbsjxxService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.igams.wechat.service.svcinterface.IXmsyglService;
import com.matridx.igams.wechat.service.svcinterface.IYhsjxxjgService;
import com.matridx.igams.wechat.util.MatchingUtil;
import com.matridx.igams.wechat.util.ReportZipThread;
import com.matridx.igams.wechat.util.SendConfirmMessageThread;
import com.matridx.igams.wechat.util.SendConfirmNormalMessageThread;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.email.EmailUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.SFDTO;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SjxxServiceImpl extends BaseBasicServiceImpl<SjxxDto, SjxxModel, ISjxxDao> implements ISjxxService, IStatisService, IFileInspectionImport
{
	private Logger logger = LoggerFactory.getLogger(CheckControl.class);

	/**
	 * FTP服务器下word的路径
	 */
//	@Value("${matridx.ftp.wordpath}")
//	String FTPWORD_PATH = null;


	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	/**
	 * FTP服务器地址 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.url}")
	private String FTP_URL = null;

	/**
	 * FTP服务器的用户名 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
//	@Value("${matridx.ftp.user}")
//	String FTP_USER = null;

	@Value("${matridx.wechat.applicationurl:}")
	private String address;

//	@Value("${matridx.ftp.pd}")
//	String FTP_PD = null;
//	@Value("${matridx.ftp.port}")
//	Integer FTP_PORT = null;
	// 微信通知的模板ID
	@Value("${matridx.wechat.kpi_templateid:}")
	private String kpi_templateid = null;
	// 微信通知标本状态的模板ID
	@Value("${matridx.wechat.ybzt_templateid:}")
	private String ybzt_templateid = null;

	@Value("${matridx.wechat.serverapplicationurl:}")
	private String serverapplicationurl;
	@Autowired
	ISjkjxxService sjkjxxService;
	@Autowired
	ISjxxTwoDao sjxxTwoDao;
	@Autowired
	ISjjcjgService sjjcjgService;
	@Autowired
	ISjysxxService sjysxxService;
	@Autowired
	ISjjcxmService sjjcxmService;
	@Autowired
	ISjlczzService sjlczzService;
	@Autowired
	ISjkjxxService sjkjxxService1;
	@Autowired
	ISjgzbyService sjgzbyService;
	@Autowired
	ISjqqjcService sjqqjcService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IMxxxService mxxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IFjcfbDao fjcfbDao;
	@Autowired
	ISjsyglService sjsyglService;
	@Autowired
	IXmsyglService xmsyglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ISjwzxxService sjwzxxService;
	@Autowired
	ISjxxjgService sjxxjgService;
	@Autowired
	IYhsjxxjgService yhsjxxjgService;
	@Autowired
	ICwglService cwglService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	IWbhbyzService wbhbyzService;
	@Autowired
	ISjbgsmService sjbgsmService;
	@Autowired
	ISjnyxService sjnyxService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	IDdyhService ddyhService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	IFjsqDao fjsqDao;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IWxyhDao wxyhDao;
	@Autowired
	ISjxxCommonService sjxxCommonService;
	@Autowired
	IYyxxService yyxxService;
	@Autowired
	ISjdwxxService sjdwxxService;
	@Autowired
	IHbsfbzService hbsfbzService;
	@Autowired
	IKdxxService kdxxService;
	@Autowired
	ISjkdxxService sjkdxxService;
	@Autowired
	ISysxxService sysxxService;
//	@Autowired
//	ISjyzService sjyzService;
	@Autowired
	ISjzzsqDao sjzzsqDao;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	IHbdwqxService hbdwqxService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	IXxdyService xxdyService;
	@Autowired
	@Lazy
	ISjpdglService sjpdglService;
	@Autowired
	ISjxxResStatisticService sjxxResStatisticService;
	@Autowired
	ISjxxWsDao sjxxWsDao;
	@Autowired
	ISjkzxxService sjkzxxService;
	@Autowired
	IWbsjxxService wbsjxxService;
	@Autowired
	ISjnbbmService sjnbbmService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	InterfaceExceptionUtil interfaceExceptionUtil;
	@Autowired
	HttpServletRequest httpServletRequest;
	@Autowired
	ISjbgsmService sjbgsmservice;
	@Autowired
	IHbqxService hbqxService;
	@Autowired
	INyypxxService nyypxxService;
	@Autowired
	ISjdlxxService sjdlxxService;



	/**
	 * 设置HttpServletRequest
	 * @return
	 */
	public void saveHttpServletRequest(HttpServletRequest request){
		this.httpServletRequest = request;
	}

	@Autowired
	ISjtssqDao sjtssqDao;

	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.inspectionurl:}")
	private String inspectionurl;
	@Value("${matridx.fileupload.prefix:}")
	private String prefix;
	@Value("${matridx.fileupload.releasePath:}")
	private String releaseFilePath;
	@Value("${matridx.file.pathPrefix:}")
	private String pathPrefix;
	@Value("${matridx.file.exportFilePath:}")
	private String exportFilePath;
	@Value("${matridx.wechat.externalurl:}")
	private String externalurl;
	@Value("${matridx.sf.client_code:}")
	private String client_code;
	@Value("${matridx.sf.check_word:}")
	private String check_word;
	@Value("${matridx.sf.call_url_prod:}")
	private String call_url_prod;
	@Value("${matridx.xinghe.ticket:}")
	private String xinghe_ticket;
	@Value("${matridx.xinghe.wsdlurl:}")
	private String xinghe_wsdlurl;
	@Value("${matridx.xinghe.hospitalservice:}")
	private String xinghe_hospitalservice;
	@Value("${matridx.xinghe.newurl:}")
	private String xinghe_newurl;
	@Value("${matridx.xinghe.newak:}")
	private String xinghe_ak;
	@Value("${matridx.xinghe.newsk:}")
	private String xinghe_sk;

	@Value("${matridx.jingdong.appSecret:}")
	private String appSecret;
	@Value("${matridx.jingdong.appKey:}")
	private String appKey;
	@Value("${matridx.jingdong.serverUrl:}")
	private String serverUrl;
	@Value("${matridx.jingdong.customerCode:}")
	private String customerCode;
	@Value("${matridx.jingdong.refreshToken:}")
	private String refreshToken;

	@Value("${matridx.qianmai.url:}")
	private String QIANMAI_URL;
	@Value("${matridx.qianmai.usercode:}")
	private String QIANMAI_USERCODE;
	@Value("${matridx.qianmai.password:}")
	private String QIANMAI_PASSWORD;
	@Value("${matridx.qianmai.entrustHosCode:}")
	private String QIANMAI_EntrustHosCode;
	@Value("${matridx.henuo.url:}")
	private String HENUO_URL;
	@Value("${matridx.henuo.account:}")
	private String HENUO_ACCOUNT;
	@Value("${matridx.henuo.password:}")
	private String HENUO_PASSWORD;
	@Value("${matridx.henuo.userType:}")
	private String HENUO_USERTYPE;
	@Value("${matridx.henuo.entrustHosCode:}")
	private String HENUO_EntrustHosCode;
	@Value("${matridx.henuo.new.url:}")
	private String HENUO_NEW_URL;
	@Value("${matridx.henuo.new.account:}")
	private String HENUO_NEW_ACCOUNT;
	@Value("${matridx.henuo.new.password:}")
	private String HENUO_NEW_PASSWORD;
	@Value("${matridx.henuo.new.userType:}")
	private String HENUO_NEW_USERTYPE;
	@Value("${matridx.henuo.new.entrustHosCode:}")
	private String HENUO_NEW_EntrustHosCode;

	/**
	 * 时效性统计
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getSxx(SjxxDto sjxxDto){
		return dao.getSxx(sjxxDto);
	}
	/**
	 * 复测率统计
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getFcl(SjxxDto sjxxDto){
		return dao.getFcl(sjxxDto);
	}

	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;

	@Autowired
	private ISjybztService sjybztService;

	@Autowired
	private ISjzmjgService sjzmjgService;
	private Logger log = LoggerFactory.getLogger(SjxxServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean expressRemind() {
		//=============================================京东快递==================================================
		JcsjDto jcsjDtoJD = new JcsjDto();
		jcsjDtoJD.setCsdm("JD");
		jcsjDtoJD.setJclb(BasicDataTypeEnum.SD_TYPE.getCode());
		jcsjDtoJD = jcsjService.getDtoByCsdmAndJclb(jcsjDtoJD);//快递类型基础数据

		SjkdxxDto sjkdxxDto = new SjkdxxDto();
		sjkdxxDto.setKdlx(jcsjDtoJD.getCsid());

		List<SjkdxxDto> sjkdDtoList = sjkdxxService.getSjkdDtosNotAccept(sjkdxxDto);
		for (SjkdxxDto sjkdxxDto1: sjkdDtoList ) {
			List<KdxxDto> jdInfo = new ArrayList<>();
			try {
				//设置接口域名(有的开放业务同时支持生产和沙箱环境，有的仅支持生产，具体以开放业务中的【API文档-请求地址】为准)，生产域名：https://api.jdl.com 沙箱环境域名：https://uat-api.jdl.com
				DefaultDomainApiClient client = new DefaultDomainApiClient(serverUrl);
				QueryDynamictraceinfoLopRequest request = new QueryDynamictraceinfoLopRequest();
				request.setCustomerCode(customerCode);
				request.setWaybillCode(sjkdxxDto1.getMailno());
				LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin(
						client.getServerUrl(), //合作伙伴应用无需传入
						appKey,//appkey
						appSecret,//appsecret
						refreshToken //refrshtoken  合作伙伴应用无需传入
				);
				request.addLopPlugin(lopPlugin);
				//发送HTTP请求（请记得更换为自己要使用的接口入参对象、出参对象）
				QueryDynamictraceinfoLopResponse response = client.execute(request);
				TraceQueryResponse<List<TraceInfo>> result = response.getResult();
				if ("成功".equals(result.getMsg())){
					List<TraceInfo> data = result.getData();
					for (int i=0; i<data.size(); i++){
						KdxxDto kdxxDto = new KdxxDto();
						kdxxDto.setKdid(StringUtil.generateUUID());
						kdxxDto.setMailno(data.get(i).getWaybillCode());
						kdxxDto.setAcceptaddress(data.get(i).getOpeTitle());
						kdxxDto.setAccepttime(data.get(i).getOpeTime());
						kdxxDto.setRemark(data.get(i).getOpeRemark());
						kdxxDto.setOpcode(data.get(i).getState());
						kdxxDto.setPx(Integer.toString(i));
						kdxxDto.setCourier(data.get(i).getCourier());
						kdxxDto.setCouriertel(data.get(i).getCourierTel());

						if (StringUtil.isBlank(sjkdxxDto1.getStarttime())){
							sjkdxxDto1.setStarttime(data.get(0).getOpeTime());
							sjkdxxService.updateStarttimeByMailno(sjkdxxDto1);
						}
						if ("150".equals(data.get(data.size()-1).getState())){
							//说明快递已经签收
							sjkdxxDto1.setEndtime(data.get(i).getOpeTime());
							sjkdxxDto1.setKdzt("1");
							sjkdxxService.updateEndtimeByMailno(sjkdxxDto1);
							if (StringUtil.isNotBlank(sjkdxxDto1.getStarttime())&&StringUtil.isNotBlank(data.get(i).getOpeTime())){
								SjpdglDto sjpdglDto = new SjpdglDto();
								DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
								SimpleDateFormat df_sj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
								//日期格式要对应 不然会报错
								LocalDateTime kssj = LocalDateTime.parse(df_sj.format(DateUtils.parseDate(sjkdxxDto1.getStarttime())), df);
								LocalDateTime jssj = LocalDateTime.parse(df_sj.format(DateUtils.parseDate(data.get(i).getOpeTime())), df);
								long hmrqc = ChronoUnit.MILLIS.between(kssj, jssj);
								sjpdglDto.setYssc(String.valueOf(hmrqc));
								sjpdglDto.setGldh(sjkdxxDto1.getMailno());
								sjpdglService.updateByGldh(sjpdglDto);
							}
						}
						jdInfo.add(kdxxDto);
					}
					if (!jdInfo.isEmpty()){
						int count = kdxxService.getCountByMailNo(jdInfo.get(0));
						if (count == 0){
							//快递结点一个都未存入
							kdxxService.insertList(jdInfo);
						}else if (count < jdInfo.size()){
							//快递结点有部分已经存入，增加小部分结点
							List<KdxxDto> gapList = new ArrayList<>();
							for (int j=count; j<jdInfo.size(); j++){
								gapList.add(jdInfo.get(j));
							}
							kdxxService.insertList(gapList);
						}
					}
				}
			} catch (Exception e) {
				log.error("expressRemind报错======="+e.getMessage()+"错误码参考地址：https://cloud.jdl.com/#/devSupport/53215");
				e.printStackTrace();
			}
		}

		//=============================================顺丰快递==================================================
		com.matridx.igams.common.sf.EspServiceCode espServiceCode = com.matridx.igams.common.sf.EspServiceCode.EXP_RECE_SEARCH_ROUTES;//查路由

		JcsjDto jcsjDtoSF = new JcsjDto();
		jcsjDtoSF.setCsdm("SF");
		jcsjDtoSF.setJclb(BasicDataTypeEnum.SD_TYPE.getCode());
		jcsjDtoSF = jcsjService.getDtoByCsdmAndJclb(jcsjDtoSF);//快递类型基础数据

		SjkdxxDto sjkdxxDto_sf = new SjkdxxDto();
		sjkdxxDto_sf.setKdlx(jcsjDtoSF.getCsid());
		String[] phoneTail = {"1679"};//13396591679
		SFDTO sfDto = new SFDTO();
		sfDto.setLanguage("0");
		sfDto.setTrackingType("1");
		sfDto.setMethodType("1");

		Map<String, String> params = new HashMap<>();
		//从sjkdxx获取快递单
		List<String> mailNoList_sf = sjkdxxService.getKdhListByKdlx(sjkdxxDto_sf);
		for(int index = 0; index < mailNoList_sf.size(); index=index+10) {
			//每次都最多十个单号查询
			List<String> kdhList = new ArrayList<>();
			if(index-10 < 0) {
				if( mailNoList_sf.size() <= 10 ) {
					kdhList.addAll(mailNoList_sf.subList(0, mailNoList_sf.size()));
				}else {
					kdhList.addAll(mailNoList_sf.subList(0, 10));
				}
			}else {
				if( index+10 <= mailNoList_sf.size() ) {
					kdhList.addAll(mailNoList_sf.subList(index, index+10));
				}else if( index+10 > mailNoList_sf.size() ) {
					kdhList.addAll(mailNoList_sf.subList(index,mailNoList_sf.size()));
				}
			}
			sfDto.setTrackingNumber(kdhList);//只能一次查询最多10条数据
			for(int j=0; j<phoneTail.length; j++) {
				StringBuilder sb = new StringBuilder();
				for(int k=0; k<kdhList.size(); k++) {
					sb = sb.append(",").append(phoneTail[j]);//多少个快递单号匹配多少个相同的电话号码
				}
				if(StringUtil.isNotBlank(sb)) {
					sfDto.setCheckPhoneNo(sb.substring(1).toString());
				}
				String msgData = JSONObject.toJSONString(sfDto);//得到请求的json
				String timeStamp = String.valueOf(System.currentTimeMillis());
				try {
					params.put("partnerID", client_code);  // 顾客编码 ，对应丰桥上获取的clientCode
					params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
					params.put("serviceCode",espServiceCode.getCode());// 接口服务码
					params.put("timestamp", timeStamp);// 时间
					params.put("msgData", msgData);// 请求json字符串
					params.put("msgDigest", com.matridx.igams.common.sf.CallExpressServiceTools.getMsgDigest(msgData,timeStamp,check_word));//校验码。
					log.error("msgData"+ msgData);
				} catch (UnsupportedEncodingException e) {
					log.error("kdhRemind报错1====="+e.getMessage());
				}

				String result = null;
				JSONObject routeResps = new JSONObject();
				try {
					result = com.matridx.igams.common.sf.HttpClientUtil.post(call_url_prod, params);
					log.error("call_url_prod" + call_url_prod);
					log.error("result" + result);
					JSONObject jsonResult =JSONObject.parseObject(result);
					JSONObject apiResultData_t = JSONObject.parseObject( jsonResult.getString("apiResultData") );
					routeResps = apiResultData_t.getJSONObject("msgData");
					//routeResps = jsonResult.getJSONObject("apiResultData").getJSONObject("msgData");
				}catch (UnsupportedEncodingException e) {
					log.error("kdhRemind报错2====="+e.getMessage());
				}
				log.error("====调用请求：" + params.get("msgData"));
//				log.error("====调用丰桥的接口服务代码：" + String.valueOf(testService.getCode()) );
				log.error("===返回结果：" +result);

				if( (routeResps != null) && (!routeResps.isEmpty()) ) {
					JSONArray routeArray = routeResps.getJSONArray("routeResps"); //获取响应json中的每一个订单信息
					for(int i=0; i<routeArray.size(); i++) {
						JSONObject Mailnode = routeArray.getJSONObject(i);//订单号
						//List<KdxxDto> routeList = Mailnode.getJSONArray("routes").toJavaList(KdxxDto.class);//订单的路由节点
						List<KdxxDto> routeList = JSONArray.parseArray(Mailnode.getJSONArray("routes").toJSONString(), KdxxDto.class);
						int xh=0;

						SjkdxxDto sjkdxx = new SjkdxxDto();//
						DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
						SimpleDateFormat df_sj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						LocalDateTime kssj = null;
						for(KdxxDto route : routeList) {//遍历订单下的各个路由节点，将订单号和节点顺序存储进去，且查看路由状态码，已签收的则更新送检的顺丰标记
							route.setKdid(StringUtil.generateUUID());
							route.setMailno(Mailnode.getString("mailNo").toString());
							route.setPx(String.valueOf(xh++));
							if("0".equals(route.getPx())) {
								sjkdxx.setMailno(Mailnode.getString("mailNo").toString());
								sjkdxx.setStarttime(route.getAccepttime());
								sjkdxxService.updateStarttimeByMailno(sjkdxx);
								if (StringUtil.isNotBlank(route.getAccepttime())){
									kssj = LocalDateTime.parse(df_sj.format(DateUtils.parseDate(route.getAccepttime())), df);
								}
							}
							if("80".equals(route.getOpcode())) {
								sjkdxx.setMailno(Mailnode.getString("mailNo").toString());
								sjkdxx.setEndtime(route.getAccepttime());
								sjkdxx.setKdzt("1");
								sjkdxxService.updateEndtimeByMailno(sjkdxx);
								if(kssj!=null&&StringUtil.isNotBlank(sjkdxx.getEndtime())){
									SjpdglDto sjpdglDto = new SjpdglDto();
									//日期格式要对应 不然会报错
									LocalDateTime jssj = LocalDateTime.parse(df_sj.format(DateUtils.parseDate(sjkdxx.getEndtime())), df);
									long hmrqc = ChronoUnit.MILLIS.between(kssj, jssj);
									sjpdglDto.setYssc(String.valueOf(hmrqc));
									sjpdglDto.setGldh(sjkdxx.getMailno());
									sjpdglService.updateByGldh(sjpdglDto);
								}
							}
						}
						if(!routeList.isEmpty()) {
							//插入之前需要判断重复操作，防止出现未签收快递重复插入节点信息的情况
							//插入订单对应的节点前先从数据库中查找出订单节点个数
							KdxxDto kdDto = new KdxxDto();
							kdDto.setMailno(Mailnode.getString("mailNo").toString());
							int count = kdxxService.getCountByMailNo(kdDto);
							if(count == 0) {
								kdxxService.insertList(routeList);
							}else if(count < routeList.size() ){//只补充还未插入的节点信息
								List<KdxxDto> gapList = new ArrayList<>();
								for(int n=count; n<routeList.size(); n++) {
									gapList.add(routeList.get(n));
								}
								kdxxService.insertList(gapList);
							}
//							kdhSet.remove(Mailnode.getString("mailNo").toString());
						}
					}
				}
			}

		}
		return true;
	}

	/**
	 * 定时任务查询未签收的快递号
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean kdhRemind(){
		com.matridx.igams.common.sf.EspServiceCode testService = com.matridx.igams.common.sf.EspServiceCode.EXP_RECE_SEARCH_ROUTES;//查路由
		JcsjDto jcsjDtoKD = new JcsjDto();
		jcsjDtoKD.setCsdm("SF");
		jcsjDtoKD.setJclb(BasicDataTypeEnum.SD_TYPE.getCode());
		jcsjDtoKD = jcsjService.getDtoByCsdmAndJclb(jcsjDtoKD);//快递类型基础数据

		Map<String, String> params = new HashMap<>();
		List<String> kdhEndList = new ArrayList<>();//后续存储已签收的快递单号
		List<SjkdxxDto> sjkdxxList = new ArrayList<>();

		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setLrsj(date.format(DateUtils.getDate(new Date(), -3)));

//		String[] phone = {"13073630791","15669078776","18667166857","18905813203","13023679130","17606528967","18067936361","15355427571","19157786836","15695711938","15695715597","19157818713","15658071937","17357100190 ","13173623797"};
		String[] phoneTail = {"0791","8776","6857","3203","9130","8967","6361","7571","6836","1938","5597","8713","1937","0190 ","3797"};

		SFDTO sfDto = new SFDTO();

		Set<String> kdhSet = dao.getKdhIsNotNullList(sjxxDto);
		List<String> kdhAllList = new ArrayList<>();
		kdhAllList.addAll(kdhSet);
		sjxxDto.setKdhs(   (String[]) kdhAllList.toArray(new String[kdhAllList.size()]) );
//		List<SjxxDto> sjidAndkdhList = dao.getSjidAndKdhList(sjxxDto);

		sfDto.setLanguage("0");
		sfDto.setTrackingType("1");
		sfDto.setMethodType("1");

		for(int index = 0; index < kdhAllList.size(); index=index+10) {
			//每次都最多十个单号查询
			List<String> kdhList = new ArrayList<>();
			if(index-10 < 0) {
				if( kdhAllList.size() <= 10 ) {
					kdhList.addAll(kdhAllList.subList(0, kdhAllList.size()));
				}else {
					kdhList.addAll(kdhAllList.subList(0, 10));
				}
			}else {
				if( index+10 <= kdhAllList.size() ) {
					kdhList.addAll(kdhAllList.subList(index, index+10));
				}else if( index+10 > kdhAllList.size() ) {
					kdhList.addAll(kdhAllList.subList(index,kdhAllList.size()));
				}
			}
//			System.out.println(kdhList);
			List<SjxxDto> sjidAndkdhList = dao.getSjidAndKdhList(sjxxDto);
			sfDto.setTrackingNumber(kdhList);//只能一次查询最多10条数据
			for(int j=0; j<phoneTail.length; j++) {
				StringBuilder sb = new StringBuilder();
				for(int k=0; k<kdhList.size(); k++) {
					sb = sb.append(",").append(phoneTail[j]);//多少个快递单号匹配多少个相同的电话号码
				}
				if(StringUtil.isNotBlank(sb)) {
					sfDto.setCheckPhoneNo(sb.substring(1).toString());
				}
				//JSONObject json = new JSONObject();
				String msgData = JSONObject.toJSONString(sfDto);//得到请求的json
				String timeStamp = String.valueOf(System.currentTimeMillis());
				try {
					params.put("partnerID", client_code);  // 顾客编码 ，对应丰桥上获取的clientCode
					params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
					params.put("serviceCode",testService.getCode());// 接口服务码
					params.put("timestamp", timeStamp);// 时间
					params.put("msgData", msgData);// 请求json字符串
					params.put("msgDigest", com.matridx.igams.common.sf.CallExpressServiceTools.getMsgDigest(msgData,timeStamp,check_word));//校验码。
				} catch (UnsupportedEncodingException e) {
					log.error("kdhRemind报错1====="+e.getMessage());
				}

				String result = null;
				JSONObject routeResps = new JSONObject();
				try {
					result = com.matridx.igams.common.sf.HttpClientUtil.post(call_url_prod, params);
					JSONObject jsonResult =JSONObject.parseObject(result);
					JSONObject apiResultData_t = JSONObject.parseObject( jsonResult.getString("apiResultData") );
					routeResps = apiResultData_t.getJSONObject("msgData");
					//routeResps = jsonResult.getJSONObject("apiResultData").getJSONObject("msgData");
				}catch (UnsupportedEncodingException e) {
					log.error("kdhRemind报错2====="+e.getMessage());
				}
				log.error("====调用请求：" + params.get("msgData"));
//				log.error("====调用丰桥的接口服务代码：" + String.valueOf(testService.getCode()) );
				log.error("===返回结果：" +result);

				if( (routeResps != null) && (!routeResps.isEmpty()) ) {
					JSONArray routeArray = routeResps.getJSONArray("routeResps"); //获取响应json中的每一个订单信息
					for(int i=0; i<routeArray.size(); i++) {
						JSONObject Mailnode = routeArray.getJSONObject(i);//订单号
						//List<KdxxDto> routeList = Mailnode.getJSONArray("routes").toJavaList(KdxxDto.class);//订单的路由节点
						List<KdxxDto> routeList = JSONArray.parseArray(Mailnode.getJSONArray("routes").toJSONString(), KdxxDto.class);
						int xh=0;

						SjkdxxDto sjkdxx = new SjkdxxDto();//
						sjkdxx.setMailno(Mailnode.getString("mailNo").toString());//
						sjkdxx.setKdzt("0");//
						for(KdxxDto route : routeList) {//遍历订单下的各个路由节点，将订单号和节点顺序存储进去，且查看路由状态码，已签收的则更新送检的顺丰标记
							route.setKdid(StringUtil.generateUUID());
							route.setMailno(Mailnode.getString("mailNo").toString());
							route.setPx(String.valueOf(xh++));
							if("0".equals(route.getPx())) {//
								sjkdxx.setStarttime(route.getAccepttime());//
							}//
							if("80".equals(route.getOpcode())) {
								kdhEndList.add(Mailnode.getString("mailNo").toString());//kdhEndList主要存储签收的快递单号，目的为了一次更新送检表的顺丰标记
								sjkdxx.setEndtime(route.getAccepttime());//
								sjkdxx.setKdzt("1");//
							}
						}
						if( !StringUtils.isBlank(sjkdxx.getStarttime()) ) {
							sjkdxxList.add(sjkdxx);
						}
						if(!routeList.isEmpty()) {
							//插入之前需要判断重复操作，防止出现未签收快递重复插入节点信息的情况
							//插入订单对应的节点前先从数据库中查找出订单节点个数
							KdxxDto kdDto = new KdxxDto();
							kdDto.setMailno(Mailnode.getString("mailNo").toString());
							int count = kdxxService.getCountByMailNo(kdDto);
							if(count == 0) {
								kdxxService.insertList(routeList);
							}else if(count < routeList.size() ){//只补充还未插入的节点信息
								List<KdxxDto> gapList = new ArrayList<>();
								for(int n=count; n<routeList.size(); n++) {
									gapList.add(routeList.get(n));
								}
								kdxxService.insertList(gapList);
							}
//							kdhSet.remove(Mailnode.getString("mailNo").toString());
						}
					}
				}
			}
			if(!kdhEndList.isEmpty()) {
				//更新送检表的sfbj为1，表示已签收的
				dao.updateSfbj(kdhEndList);
			}
	//		if(!kdhSet.isEmpty()) {
	//			//通过遍历仍然查找不到的，更新送检表的顺丰标记为2，表示快递号不存在
	//			List<String> list = new ArrayList<String>();
	//			list.addAll(kdhSet);
	//			boolean isUnfindSfbj = dao.updateUnfindSfbj(list);
	//		}

			//遍历组装出送检表中未签收和签收的sjid和kdh的数据
			List<SjkdxxDto> resultList = new ArrayList<>();
			for(int i=0; i<sjidAndkdhList.size(); i++) {//sjidAndkdhList查找的送检表kdbj为null和1的情况
				for(int j=0; j<sjkdxxList.size(); j ++) {//sjkdxxList查找出来的是顺丰组装起来的快递号，开始结束时间，签收状态等信息
					if(  ( sjkdxxList.get(j).getMailno() ).equals( sjidAndkdhList.get(i).getKdh() ) ) {
						SjkdxxDto sjkd = new SjkdxxDto();
						sjkd.setEndtime(sjkdxxList.get(j).getEndtime());
						sjkd.setStarttime(sjkdxxList.get(j).getStarttime());
						sjkd.setSjkdid(  StringUtil.generateUUID()  );
						sjkd.setYwid(  sjidAndkdhList.get(i).getSjid()  );
						sjkd.setMailno(  sjidAndkdhList.get(i).getKdh()  );
						sjkd.setKdzt(  sjkdxxList.get(j).getKdzt()  );
						sjkd.setYwlx(ExpressTypeEnum.SJXX.getCode());
						sjkd.setKdlx(jcsjDtoKD.getCsid());
						resultList.add(  sjkd  );
					}
				}
			}
			log.error("组装sjid和kdh得到resultList======"+JSONArray.parseArray(JSON.toJSONString(resultList)));
			if(!resultList.isEmpty()) {
				//先查找对应的sjid和mailno的数据是否存在，存在更新kdzt，未存在则插入数据
				//遍历resultList分成两份,一份是表插入的,一份是表更新快递签收状态(目的：防止一条一条插入更新日志太多)
				//先从表中选出所有未签收的所有数据，一个一个比较sjid和kdh信息
				boolean result = sjkdxxService.modSjkdxxByList(resultList);
				if(!result){
					log.error("更新送检快递信息数据失败");
				}
			}
		}
		return true;
	}

	/**
	 * 查找未收款记录
	 * @return
	 */
	public List<SjxxDto> getDtoBySf(String csid){
		return dao.getDtoBySf(csid);
	}

	@Override
	public List<SjxxDto> getPagedDtoBySf(SjxxDto sjxxDto) {
		return dao.getPagedDtoBySf(sjxxDto);
	}

	@Override
	public Boolean updateList(List<SjxxDto> list) {
		return sjxxTwoDao.updateList(list);
	}

//	@Override
//	public List<SjxxDto> getInfoByYbbh(SjxxDto sjxxDto) {
//		return dao.getInfoByYbbh(sjxxDto);
//	}

	/**
	 * 查找未收款记录
	 * @return
	 */
	public List<SjxxDto> getCountByYxdb(SjxxDto sjxxDto){
		return dao.getCountByYxdb(sjxxDto);
	}
	/**
	 * 查找未收款记录发送给医学代表
	 * @return
	 */
	public List<SjxxDto> getListByYxdb(SjxxDto sjxxDto){
		return dao.getListByYxdb(sjxxDto);
	}
	/**
	 * 查找未收款记录发送给大区经理
	 * @return
	 */
	public List<SjxxDto> getListByDqjl(SjxxDto sjxxDto){
		return dao.getListByDqjl(sjxxDto);
	}
	/**
	 * 查找未收款记录数发送给大区经理
	 * @return
	 */
	public List<SjxxDto> getCountByDqjl(SjxxDto sjxxDto){
		return dao.getCountByDqjl(sjxxDto);
	}
	/**
	 * 分页查询送检信息
	 *
	 * @param sjxxDto
	 */
	public List<SjxxDto> getPagedDtoList(SjxxDto sjxxDto)
	{
		List<SjxxDto> sjxxlist = dao.getPagedDtoList(sjxxDto);
		if(sjxxlist != null && sjxxlist.size() > 0) {
			jcsjService.handleCodeToValue(sjxxlist, new BasicDataTypeEnum[]
			{ BasicDataTypeEnum.FIRST_SJXXKZ, BasicDataTypeEnum.SECOND_SJXXKZ, BasicDataTypeEnum.THIRD_SJXXKZ, BasicDataTypeEnum.FOURTH_SJXXKZ,
					BasicDataTypeEnum.SD_TYPE, BasicDataTypeEnum.DETECTION_UNIT, BasicDataTypeEnum.INSPECTION_DIVISION, BasicDataTypeEnum.INVOICE_APPLICATION, BasicDataTypeEnum.CLASSIFY }, new String[]
			{ "cskz1:cskz1", "cskz2:cskz2", "cskz3:cskz3", "cskz4:cskz4" ,"kdlx:kdlxmc", "jcdw:jcdwmc", "sjqf:sjqfmc", "kpsq:kpsqmc","hb_fl:flmc"});
			try {
				List<String> sjids = new ArrayList<>();
				// 用户集合
				List<String> yghs = new ArrayList<>();
				// 科室集合
				List<String> dws = new ArrayList<>();
				for (SjxxDto t_sjxxDto : sjxxlist) {
					sjids.add(t_sjxxDto.getSjid());
					// 添加用户
					String hb_yhid = t_sjxxDto.getHb_yhid();
					String jsry = t_sjxxDto.getJsry();
					if (hb_yhid != null && !yghs.contains(hb_yhid)) {
						yghs.add(hb_yhid);
					}
					if(jsry != null && !yghs.contains(jsry)) {
						yghs.add(jsry);
					}
					// 添加科室
					String ks = t_sjxxDto.getKs();
					if(ks != null && !dws.contains(ks)) {
						dws.add(ks);
					}
				}
				// 用户查询
				List<User> xtyhs = null;
				if(yghs != null && yghs.size() > 0) {
					xtyhs = commonService.getZsxmByYhid(yghs);
				}
				// 科室查询
				List<SjdwxxDto> sjdwxxDtos = null;
				if(dws != null && dws.size() > 0) {
					sjdwxxDtos = sjdwxxService.getListByDwid(dws);
				}
				// 检测项目查询
				List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getListBySjid(sjids);
				// 查询收费标准
				List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getListBySjxxDtos(sjxxlist);
				for (SjxxDto s_sjxxDto : sjxxlist) {
					if(xtyhs != null && xtyhs.size() >0) {
						String hb_yhid = s_sjxxDto.getHb_yhid();
						String jsry = s_sjxxDto.getJsry();
						if (StringUtil.isNotBlank(hb_yhid) || StringUtil.isNotBlank(jsry)) {
							for (User xtyh : xtyhs) {
								if (xtyh.getYhid().equals(hb_yhid)) {
									s_sjxxDto.setZsxm(xtyh.getZsxm());
								}
								if (xtyh.getYhid().equals(jsry)) {
									s_sjxxDto.setJsrymc(xtyh.getZsxm());
								}
							}
						}
					}
					if(sjdwxxDtos!= null && sjdwxxDtos.size() > 0) {
						if (StringUtil.isNotBlank(s_sjxxDto.getKs())) {
							for (SjdwxxDto sjdwxxDto : sjdwxxDtos) {
								if (s_sjxxDto.getKs().equals(sjdwxxDto.getDwid())) {
									if(StringUtil.isBlank(s_sjxxDto.getQtks())) {
										s_sjxxDto.setQtks(sjdwxxDto.getDwmc());
										break;
									}
								}
							}
						}
					}
					if(sjjcxmDtos != null && sjjcxmDtos.size() > 0) {
						for (SjjcxmDto sjjcxmDto : sjjcxmDtos) {
							if(sjjcxmDto.getSjid().equals(s_sjxxDto.getSjid())) {
								s_sjxxDto.setJcxmmc(sjjcxmDto.getJcxmmc());
								s_sjxxDto.setJcxmkzcs(sjjcxmDto.getCskz1());
								s_sjxxDto.setJcxmid(sjjcxmDto.getJcxmid());
								break;
							}
						}
					}
					if(hbsfbzDtos != null && hbsfbzDtos.size() > 0) {
						for (HbsfbzDto hbsfbzDto : hbsfbzDtos) {
							if(hbsfbzDto.getXm().equals(s_sjxxDto.getJcxmid()) && hbsfbzDto.getHbid().equals(s_sjxxDto.getHbid())) {
								s_sjxxDto.setSfbz(hbsfbzDto.getSfbz());
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
		return sjxxlist;
	}

	/**
	 * 分页查询送检信息(优化)
	 * @param params
	 */
	@Override
	public List<SjxxDto> getDtoListOptimize(Map<String,Object> params) {
		int count = dao.getCountOptimize(params);
		params.put("totalNumber", count);
		List<SjxxDto> sjxxlist = dao.getDtoListOptimize(params);
		jcsjService.handleCodeToValue(sjxxlist, new BasicDataTypeEnum[]
		{ BasicDataTypeEnum.FIRST_SJXXKZ, BasicDataTypeEnum.SECOND_SJXXKZ, BasicDataTypeEnum.THIRD_SJXXKZ, BasicDataTypeEnum.FOURTH_SJXXKZ }, new String[]
		{ "cskz1:cskz1", "cskz2:cskz2", "cskz3:cskz3", "cskz4:cskz4" });
		return sjxxlist;
	}

	/**
	 * 通过id查询送检信息
	 *
	 * @param sjxxDto
	 */
	@Override
	public SjxxDto getDtoById(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getDtoById(sjxxDto.getSjid());
	}

	/**
	 * 修改送检信息
	 *
	 * @param sjxxDto
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> UpdateSjxx(SjxxDto sjxxDto,User user,SjkzxxDto sjkzxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		hashMap.put("status", "fail");
		hashMap.put("message", xxglService.getModelById("ICOM00002").getXxnr());

		if("0".equals(sjxxDto.getSfsf())||"0".equals(sjxxDto.getFkbj())) {
			sjxxDto.setSfje("0");
			sjxxDto.setFkrq("1");
		}

		SjybztDto sjybztDto = new SjybztDto();
		sjybztDto.setSjid(sjxxDto.getSjid());
		List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
		List<String> strzts=sjxxDto.getZts();
		if(strzts==null||strzts.size()!=sjybztDtos.size()){
			insertAll(sjxxDto,user.getYhid());

			if(StringUtil.isNotBlank(sjxxDto.getJsrq())&&StringUtil.isBlank(sjxxDto.getBgrq())){
				sendMessageByConfirmStatus(sjxxDto,user);
			}
		}else{
			boolean flag=true;
			for(SjybztDto dto:sjybztDtos){
				if(!strzts.contains(dto.getZt())){
					flag=false;
					break;
				}
			}
			insertAll(sjxxDto,user.getYhid());
			if(!flag){
				if(StringUtil.isNotBlank(sjxxDto.getJsrq())&&StringUtil.isBlank(sjxxDto.getBgrq())){
					sendMessageByConfirmStatus(sjxxDto,user);
				}
			}
		}
		List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
		if (jcxmlist!=null&&jcxmlist.size()>0){
			BigDecimal yfje=new BigDecimal("0");
			BigDecimal sfje=new BigDecimal("0");
			String sfsf_str="";
			for(SjjcxmDto dto:jcxmlist) {
				if (StringUtil.isNotBlank(dto.getSfsf())) {
					sfsf_str=dto.getSfsf();
					break;
				}
			}
			if(StringUtil.isNotBlank(sfsf_str)){
				BigDecimal sfsf=new BigDecimal(sfsf_str);
				for(SjjcxmDto dto:jcxmlist) {
					if (StringUtil.isNotBlank(dto.getSfsf())) {
						BigDecimal bigDecimal=new BigDecimal(dto.getSfsf());
						if(sfsf.compareTo(bigDecimal)!=1){
							sfsf=bigDecimal;
						}
					}
				}
				sjxxDto.setSfsf(sfsf.toString());
			}else{
				sjxxDto.setSfsf("1");
			}
			for(SjjcxmDto dto:jcxmlist){
				if(StringUtil.isNotBlank(dto.getYfje())){
					yfje=yfje.add(new BigDecimal(dto.getYfje()));
				}
				if(StringUtil.isNotBlank(dto.getSfje())){
					sfje=sfje.add(new BigDecimal(dto.getSfje()));
				}
				if(StringUtil.isNotBlank(dto.getFkrq())){
					sjxxDto.setFkrq(dto.getFkrq());
				}
			}
			sjxxDto.setFkje(yfje.toString());
			sjxxDto.setSfje(sfje.toString());
		}
		if (StringUtil.isBlank(sjxxDto.getYblxmc()) && StringUtil.isNotBlank(sjxxDto.getYblx())){
			JcsjDto yblx_jc = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(), sjxxDto.getYblx());
			sjxxDto.setYblxmc(yblx_jc.getCsmc());
		}
		boolean result = dao.updateForbiont(sjxxDto);
		if (!result)
		{
			return hashMap;
		}
		result = sjkzxxService.update(sjkzxxDto);
		if (!result)
		{
			sjkzxxDto.setLrry(sjxxDto.getXgry());
			sjkzxxService.insertDto(sjkzxxDto);
		}
		// 修改同步标记设置为1
		sjxxDto.setTbbj(1);
		//如果是否接收修改为否
		if ("0".equals(sjxxDto.getSfjs())){
			//修改实验管理的接收信息
			SjsyglDto sjsyglDto = new SjsyglDto();
			sjsyglDto.setSjid(sjxxDto.getSjid());
			sjsyglDto.setJsrq(sjxxDto.getJsrq());
			sjsyglDto.setSfjs(sjxxDto.getSfjs());
			sjsyglDto.setJsry(sjxxDto.getJsry());
			sjsyglDto.setXgry(sjxxDto.getXgry());
			sjsyglService.updateJsInfo(sjsyglDto);
		}
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
		if (sjxxDto.getIds() != null && sjxxDto.getIds().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getIds().size(); i++)
			{
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getIds().get(i), sjxxDto.getSjid());
				if (!saveFile)
					return hashMap;
			}
		}
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getFjids().size(); i++)
			{
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i), sjxxDto.getSjid());
				if (!saveFile)
					return hashMap;
			}
			// 附件排序
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(sjxxDto.getYwlx());
			boolean isSuccess = fjcfbService.fileSort(fjcfbDto);
			if (!isSuccess)
				return hashMap;
		}
		// 拷贝文件到微信服务器
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			List<FjcfbModel> fjcfbModels = new ArrayList<>();
			for (int i = 0; i < sjxxDto.getFjids().size(); i++)
			{
				FjcfbModel t_fjcfbModel = new FjcfbModel();
				t_fjcfbModel.setFjid(sjxxDto.getFjids().get(i));
				FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);
				fjcfbModels.add(fjcfbModel);

			}
			boolean isSuccess = sendFilesToAli(fjcfbModels);
			if (!isSuccess)
				return hashMap;
		}
		List<JcsjDto> yblxList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> sjqfList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		String yblxdm="";
		String jcdwmc="";
		String sjqfdm="";
		if(yblxList!=null&&yblxList.size()>0){
			for(JcsjDto jcsjDto:yblxList){
				if(sjxxDto.getYblx().equals(jcsjDto.getCsid())){
					yblxdm=jcsjDto.getCsdm();
					break;
				}
			}
			//判断如果样本类型选择其他时，并且其他标本类型内容为"全血"或者"血浆"时，标本类型传"B"
			// 20240904 "其它"样本类型的csdm由XXX改为G
			if("XXX".equals(yblxdm) || "G".equals(yblxdm)){
				if(StringUtil.isNotBlank(sjxxDto.getYblxmc())){
					if("全血".equals(sjxxDto.getYblxmc())||"血浆".equals(sjxxDto.getYblxmc())){
						yblxdm="B";
					}
				}
			}
		}
		if (StringUtil.isNotBlank(sjxxDto.getNbbm()) && "B".equals(sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length()-1)))
			yblxdm="B";
		if(jcdwList!=null&&yblxList.size()>0){
			for(JcsjDto jcsjDto:jcdwList){
				if(sjxxDto.getJcdw().equals(jcsjDto.getCsid())){
					jcdwmc=jcsjDto.getCsmc();
					break;
				}
			}
		}
		if(sjqfList!=null&&sjqfList.size()>0){
			for(JcsjDto jcsjDto:sjqfList){
				if(sjxxDto.getSjqf().equals(jcsjDto.getCsid())){
					sjqfdm=jcsjDto.getCsdm();
					break;
				}
			}
		}
		if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
			yblxdm=sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length()-1);
		}
		sjxxDto.setYblxdm(yblxdm);
		sjxxDto.setJcdwmc(jcdwmc);
		sjxxDto.setSjqf(sjqfdm);
		//根据样本编号查找数据库中的实验日期情况。之所以用getDtosByYbbh 是因为关联的表比较少
		List<SjxxDto> tt_sjxxDto = dao.getDtosByYbbh(sjxxDto.getYbbh());
		//因为涉及到文库合并，所以出过报告的 或者已经做过实验的送检 不应该更新sjsygl 和xmsygl 表的数据。项目也不应该可以修改
		if(StringUtil.isBlank(sjxxDto.getBgrq()) && (tt_sjxxDto == null ||tt_sjxxDto.size() == 0 || (StringUtil.isBlank(tt_sjxxDto.get(0).getSyrq()) 
				&& StringUtil.isBlank(tt_sjxxDto.get(0).getDsyrq()) && StringUtil.isBlank(tt_sjxxDto.get(0).getQtsyrq())))) {
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
			List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto,null,DetectionTypeEnum.DETECT_SJ.getCode());
			
			if (!CollectionUtils.isEmpty(insertInfo)){
				//更新项目实验数据和送检实验数据
				boolean flag = addOrUpdateSyData(insertInfo,sjxxDto,user.getYhid());
				//因为修改送检信息的话，可能因为修改了标本类型或者其他造成对所有的复测也有影响，则需要重建所有的sjsygl的数据
				/*resultFjDtos = fjsqService.getNotSyFjxxBySjxx(fjsqDto_t);
				for(FjsqDto res_FjsqDto:resultFjDtos) {
					res_FjsqDto.setXgry(sjxxDto.getXgry());
					fjsqService.addOrUpdateSyData(res_FjsqDto, sjxxDto);
				}*/
				SjsyglDto sjsyglDto_t=new SjsyglDto();
				sjsyglDto_t.setSjid(sjxxDto.getSjid());
				//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
				List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
				XmsyglDto xmsyglDto_t = new XmsyglDto();
				xmsyglDto_t.setYwid(sjxxDto.getSjid());
				List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
	
				if (flag && StringUtil.isNotBlank(sjxxDto.getNbbm())){
					for (SjsyglModel sjsyglModel : list) {
						if (StringUtil.isBlank(sjsyglModel.getJcbj()) || "0".equals(sjsyglModel.getJcbj())){
							hashMap.put("message","存在未接收的送检实验数据！");
							hashMap.put("status","alert");
							break;
						}
					}
				}
			}
		}
		hashMap.put("status","success");
		hashMap.put("message", xxglService.getModelById("ICOM00001").getXxnr());
		return hashMap;
	}
	
	/**
	 * 根据页面传递的送检实验数据，结合原有的数据，原本就存在的检测类型数据，则进行更新，没有的数据则进行新增
	 * 涉及 项目实验管理表，送检实验管理表
	 * @param insertInfo
	 * @param sjxxDto
	 * @param yhid
	 * @return
	 */
	public boolean addOrUpdateSyData(List<SjsyglDto> insertInfo,SjxxDto sjxxDto,String yhid) {
		//为防止未设置id，删除数据太多
		if( sjxxDto==null || StringUtil.isBlank(sjxxDto.getSjid()))
			return false;
		
		XmsyglDto xmsyglDto_t = new XmsyglDto();
		xmsyglDto_t.setYwid(sjxxDto.getSjid());
		xmsyglDto_t.setScry(StringUtil.isNotBlank(sjxxDto.getXgry())?sjxxDto.getXgry():sjxxDto.getLrry());

		//为防止已有的 sjsygl 数据未处理（因为更改类型或者项目造成数据没有关联出来的情况)需要取一下当前ywid的实验数据
		SjsyglDto sjsyglDto_t=new SjsyglDto();
		sjsyglDto_t.setYwid(sjxxDto.getSjid());
		List<SjsyglModel> beforSy_list = sjsyglService.getModelList(sjsyglDto_t);

		xmsyglService.deleteInfo(xmsyglDto_t);
		xmsyglService.delInfo(xmsyglDto_t);
		
		//获取到删除之前正常的送检实验数据  和 公用方法返回的list
		List<SjsyglDto> updateList=new ArrayList<>();
		List<SjsyglDto> insertList =  new ArrayList<>();
		
		List<XmsyglDto> updateXmsyDtos = new ArrayList<>();
		List<XmsyglDto> insertXmsyDtos = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		//如果检测单位和主表修改后的一致 并且 是新增的项目 为了后面根据检测单位，检测类型分类设置 所以给他改为修改前主表的检测单位
		for (SjsyglDto sjsyglDto : insertInfo) {
			if(!sjsyglDto.getJcdw().equals(sjxxDto.getJcdw()) && StringUtil.isBlank(sjsyglDto.getSyglid())&& StringUtil.isNotBlank(sjxxDto.getJcdw())){
				sjsyglDto.setJcdw(sjxxDto.getJcdw());
			}else if(StringUtil.isBlank(sjsyglDto.getJcdw())){
				sjsyglDto.setJcdw(sjxxDto.getJcdw());
			}
			if(StringUtil.isNotBlank(sjsyglDto.getSyglid()))
				ids.add(sjsyglDto.getSyglid());
		}
		sjsyglDto_t=new SjsyglDto();
		sjsyglDto_t.setSjid(sjxxDto.getSjid());
		sjsyglDto_t.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
		//为防止对已有的数据的影响，所以先只删除标记为1的复检的数据
		if(StringUtil.isNotBlank(sjxxDto.getSjid()))
			sjsyglService.deleteInfo(sjsyglDto_t);
		if(beforSy_list != null && beforSy_list.size() > 0) {
			for(SjsyglModel sjsy:beforSy_list) {
				boolean isFindsy = false;
				for(String id:ids) {
					if(id.equals(sjsy.getSyglid())) {
						isFindsy = true;
						break;
					}
				}
				if(!isFindsy) {
					ids.add(sjsy.getSyglid());
				}
			}
		}
		sjsyglDto_t=new SjsyglDto();
		sjsyglDto_t.setIds(ids);
		//先保存原有数据
		List<SjsyglModel> befor_list = new ArrayList<>();
		if(ids.size() > 0) {
			befor_list = sjsyglService.getModelList(sjsyglDto_t);
			sjsyglDto_t.setScry(yhid);
			//对有关联的所有数据先打上删除标记
			sjsyglService.delInfo(sjsyglDto_t);
		}
		//list 通过检测单位分组
		Map<String, List<SjsyglDto>> map = insertInfo.stream().collect(Collectors.groupingBy(SjsyglDto::getJcdw));
		if (!CollectionUtils.isEmpty(map)){
			Iterator<Map.Entry<String, List<SjsyglDto>>> entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String,  List<SjsyglDto>> entry = entries.next();
				List<SjsyglDto> resultModelList = entry.getValue();
				String jcdw = entry.getKey();
				if (StringUtil.isNotBlank(jcdw) && !CollectionUtils.isEmpty(resultModelList)){
					//在通过检测类型分组
					Map<String, List<SjsyglDto>> listMap = resultModelList.stream().collect(Collectors.groupingBy(SjsyglDto::getJclxid));
					if (!CollectionUtils.isEmpty(listMap)){
						Iterator<Map.Entry<String, List<SjsyglDto>>> entryIterator = listMap.entrySet().iterator();
						while (entryIterator.hasNext()) {
							Map.Entry<String,  List<SjsyglDto>> stringListEntry = entryIterator.next();
							String jclx = stringListEntry.getKey();
							List<SjsyglDto> sjsyglDtoList = stringListEntry.getValue();
							if (StringUtil.isNotBlank(jclx) && !CollectionUtils.isEmpty(sjsyglDtoList)){
								sjsyglDtoList=sjsyglDtoList.stream().sorted(Comparator.comparing(SjsyglDto::getPx)).collect(Collectors.toList());
								boolean flag = true;
								//用修改之前的实验数据和修改后的对比如果检测单位和检测类型都相等则不用新增，是修改
								for (SjsyglModel sjsyglModel : befor_list) {
									if (jcdw.equals(sjsyglModel.getJcdw()) && jclx.equals(sjsyglModel.getJclxid())){
										SjsyglDto dto = new SjsyglDto();
										dto.setSyglid(sjsyglModel.getSyglid());
										dto.setScbj("0");
										dto.setXgry(yhid);
										if(!sjsyglModel.getJcdw().equals(sjxxDto.getJcdw())){//若不相等，则是修改了检测单位，将和原检测单位相同的进行更新
											dto.setJcdw(sjxxDto.getJcdw());
										}
										String jcxmmc = "";
										String wksxbm = "";
										//循环当前项目下的所有送检实验清单，把相同检测类型的送检实验管理数据的项目名称合并，ywid 是实际的ID，所以不相等就不用考虑
										for (SjsyglDto sjsyglDto : sjsyglDtoList) {
											//判断项目实验明细是否存在  存在修改 不存在新增
											if(jcxmmc.indexOf(sjsyglDto.getJcxmmc())==-1) {
												jcxmmc += ","+sjsyglDto.getJcxmmc();
												wksxbm += ","+sjsyglDto.getWksxbm();
											}
											//同一个项目但不同的对应ID（检测单位和标本类型不一样所造成），如果检测类型xmsygl表的数据应该重新创建
											//ywid 就是实际的id，如果ID不同，就证明在这个检测类型上该送检ID没有相应的实验数据
											if(sjsyglDto.getYwid().equals(sjxxDto.getSjid())) {
												if (StringUtil.isNotBlank(sjsyglDto.getXmsyglid())){
													XmsyglDto xmsyglDto = new XmsyglDto();
													xmsyglDto.setXmsyglid(sjsyglDto.getXmsyglid());
													xmsyglDto.setXgry(yhid);
													xmsyglDto.setYwid(sjsyglDto.getYwid());
													xmsyglDto.setSyglid(sjsyglModel.getSyglid());
													xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
													xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
													updateXmsyDtos.add(xmsyglDto);
												}else{
													XmsyglDto xmsyglDto = new XmsyglDto();
													xmsyglDto.setXmsyglid(StringUtil.generateUUID());
													xmsyglDto.setSyglid(sjsyglModel.getSyglid());
													xmsyglDto.setLrry(yhid);
													xmsyglDto.setWkdm(sjsyglDto.getNbzbm());
													xmsyglDto.setDyid(sjsyglDto.getDyid());
													xmsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
													xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
													xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
													xmsyglDto.setYwid(sjsyglDto.getSjid());
													insertXmsyDtos.add(xmsyglDto);
												}
											}
										}
										//在先录入DNA，然后加测RNA，后又修改送检信息未D+R，则数据不需要跟加测分开
										dto.setDyid(sjsyglDtoList.get(0).getDyid());
										dto.setJcxmid(sjsyglDtoList.get(0).getJcxmid());
										dto.setJczxmid(sjsyglDtoList.get(0).getJczxmid());
										dto.setNbzbm(sjsyglDtoList.get(0).getNbzbm());
										dto.setWksxbm(wksxbm.length() > 0?wksxbm.substring(1):"");
										dto.setYwid(sjsyglDtoList.get(0).getYwid());
										dto.setXmmc(jcxmmc.length() > 0?jcxmmc.substring(1):"");
										dto.setXgry(yhid);
										updateList.add(dto);
										flag = false;
										break;
									}
								}
								if (flag){
									String syglid = StringUtil.generateUUID();
									sjsyglDtoList.get(0).setSyglid(syglid);
									sjsyglDtoList.get(0).setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
									String jcxmmc = "";
									String wksxbm = "";
									for (SjsyglDto sjsyglDto : sjsyglDtoList) {
										if(jcxmmc.indexOf(sjsyglDto.getJcxmmc())==-1) {
											jcxmmc += ","+sjsyglDto.getJcxmmc();
											wksxbm += ","+sjsyglDto.getWksxbm();
										}
										if(sjsyglDto.getYwid().equals(sjxxDto.getSjid())) {
											XmsyglDto xmsyglDto = new XmsyglDto();
											xmsyglDto.setXmsyglid(StringUtil.generateUUID());
											xmsyglDto.setSyglid(syglid);
											xmsyglDto.setWkdm(sjsyglDto.getNbzbm());
											xmsyglDto.setDyid(sjsyglDto.getDyid());
											xmsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
											xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
											xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
											xmsyglDto.setYwid(sjsyglDto.getSjid());
											xmsyglDto.setLrry(yhid);
											insertXmsyDtos.add(xmsyglDto);
										}
									}
									if(jcdw.equals(sjxxDto.getJcdw())){
										sjsyglDtoList.get(0).setJcdw(sjxxDto.getJcdw());
										if("1".equals(sjxxDto.getSfjs())){
											sjsyglDtoList.get(0).setSfjs(sjxxDto.getSfjs());
											sjsyglDtoList.get(0).setJsrq(sjxxDto.getJsrq());
											sjsyglDtoList.get(0).setJsry(sjxxDto.getJsry());
										}
									}
									sjsyglDtoList.get(0).setYwid(sjxxDto.getSjid());
									sjsyglDtoList.get(0).setWksxbm(wksxbm.length() > 0?wksxbm.substring(1):"");
									sjsyglDtoList.get(0).setXmmc(jcxmmc.length() > 0?jcxmmc.substring(1):"");
									insertList.add(sjsyglDtoList.get(0));
								}
							}
						}
					}
				}
			}
		}
		boolean flag = false;
		if(!CollectionUtils.isEmpty(insertList)){
			sjsyglService.insertList(insertList);
			flag = true;
		}
		if(!CollectionUtils.isEmpty(updateList)) {
			sjsyglService.modAllList(updateList);
		}
		if (!CollectionUtils.isEmpty(updateXmsyDtos)){
			xmsyglService.modToNormal(updateXmsyDtos);
		}
		if (!CollectionUtils.isEmpty(insertXmsyDtos))
			xmsyglService.insertList(insertXmsyDtos);
		
		return flag;
	}

	/**
	 * 查询科室信息
	 */
	@Override
	public List<SjdwxxDto> getSjdw()
	{
		// TODO Auto-generated method stub
		return dao.getSjdw();
	}

	/**
	 * 插入送检信息到数据库
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(SjxxDto sjxxDto)
	{
		if (StringUtil.isBlank(sjxxDto.getSjid()))
		{
			sjxxDto.setSjid(StringUtil.generateUUID());
		}
		//去除空格
		if (StringUtil.isNotBlank(sjxxDto.getYbbh())) {
			sjxxDto.setYbbh(sjxxDto.getYbbh().trim());
		}
		int result = dao.insert(sjxxDto);
		return result != 0;
	}


	/**
	 * 保存反馈消息
	 *
	 * @param sjxxDto
	 * @throws BusinessException
	 */
	public void getFeedBack(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		// 把送检反馈添加到对应的送检信息中
		sjxxDto.setYbbh(getDtoById(sjxxDto.getSjid()).getYbbh());
		try
		{
			dao.updateFeedBack(sjxxDto);
			if ("1".equals(sjxxDto.getSfzq()))
			{
				sjxxDto.setSfzq("是");
			} else
			{
				sjxxDto.setSfzq("否");
			}
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm("INSPECTION_FEEDBACK");
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
			if (ddxxglDtos != null && ddxxglDtos.size() > 0)
			{
				for (int i = 0; i < ddxxglDtos.size(); i++)
				{
					if (StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid()))
					{
						String title = xxglService.getMsg("ICOMM_SH00029");
						String sjid=sjxxDto.getSjid();
						String message=xxglService.getMsg("ICOMM_SH00019", sjxxDto.getHzxm(),sjxxDto.getYbbh(),sjxxDto.getYblxmc(),sjxxDto.getSfzq(),sjxxDto.getLcfk(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
						String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/inspection/getDdFeedbackView?sjid=" + sjid;
						// 外网访问
						String external = externalurl + "/common/view/displayView?view_url=/ws/inspection/getDdFeedbackView?sjid=" + sjid;
						List<BtnJsonList> btnJsonLists = new ArrayList<>();
						BtnJsonList btnJsonList = new BtnJsonList();
						btnJsonList.setTitle("内网访问");
						btnJsonList.setActionUrl(internalbtn);
						btnJsonLists.add(btnJsonList);
						btnJsonList = new BtnJsonList();
						btnJsonList.setTitle("外网访问");
						btnJsonList.setActionUrl(external);
						btnJsonLists.add(btnJsonList);
						talkUtil.sendCardMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), title, message, btnJsonLists, "1");
					}
				}
			}
			// 获取服务器文件信息
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			String fjids = StringUtils.join(sjxxDto.getFjids(), ",");
			paramMap.add("fjids", fjids);

			if(StringUtil.isNotBlank(menuurl)) {

				RestTemplate t_restTemplate = new RestTemplate();
				String param = t_restTemplate.postForObject(menuurl + "/wechat/getFjcfbModel", paramMap, String.class);
				// 删除反馈附件文件
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
				fjcfbDto.setYwid(sjxxDto.getSjid());
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
				if (param != null)
				{
					JSONArray parseArray = JSONObject.parseArray(param);
					for (int i = 0; i < parseArray.size(); i++)
					{
						FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
						boolean result = fjcfbService.insert(fjcfbModel);
						if (!result)
							throw new BusinessException("", "附件更新未成功！");
						// 下载服务器文件到指定文件夹
						downloadFile(fjcfbModel);
					}
				}
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			log.error(e.getMessage());
		}

	}

	/**
	 * 微信端新增送检信息时的处理
	 *
	 * @param sjxxDto
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void checkApply(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		String url = menuurl + "/common/view/displayView?view_url=/common/view/inspectionView?ybbh=" + sjxxDto.getYbbh() + "&hzxm=" + sjxxDto.getHzxm();
		try
		{
			RestTemplate t_restTemplate = new RestTemplate();
			if(StringUtil.isNotBlank(sjxxDto.getDb())){

				// 若收费且金额为空时，获取默认金额
				if (!"0".equals(sjxxDto.getSfsf()) && StringUtil.isBlank(sjxxDto.getFkje()))
				{
					SjhbxxDto sjhbxxDto = new SjhbxxDto();
					List<SjjcxmDto> sjjcxms = sjxxDto.getSjjcxms();
					sjhbxxDto.setSjxms(sjjcxms);
					sjhbxxDto.setHbmc(sjxxDto.getDb());
					SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
					// 判断若为免D或免R时减半
					if (re_sjhbxxDto != null && StringUtil.isNotBlank(re_sjhbxxDto.getSfbz())){
						sjxxDto.setFkje(re_sjhbxxDto.getSfbz());
					}
				}
				// 通过合作伙伴判断是否盖章
				SjhbxxDto t_sjhbxxDto = new SjhbxxDto();
				t_sjhbxxDto.setHbmc(sjxxDto.getDb());
				t_sjhbxxDto = sjhbxxService.getDto(t_sjhbxxDto);
				if (t_sjhbxxDto != null){
					if (StringUtil.isNotBlank(t_sjhbxxDto.getGzcskz1())){
						JcsjDto jcsjDto = new JcsjDto();
						jcsjDto.setJclb(BasicDataTypeEnum.THIRD_SJXXKZ.getCode());
						List<JcsjDto> jcsjList = jcsjService.getCskz1NotNull(jcsjDto);
						if (jcsjList != null && jcsjList.size() > 0){
							sjxxDto.setCskz3(jcsjList.get(0).getCsid());
						}
					}
				}
			}
			// 新增送检信息
			insertDto(sjxxDto);
			// 新增送检检测项目
			sjjcxmService.insertBySjxx(sjxxDto);
			if (StringUtil.isNotBlank(sjxxDto.getWxid()) && StatusEnum.CHECK_PASS.getCode().equals(sjxxDto.getZt()))
			{
				SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
				if (sjkjxxDto != null)
				{
					boolean result = sjkjxxService.deleteById(sjxxDto.getWxid());
					if (!result)
						throw new BusinessException("", "送检快捷信息更新未成功！");
				}
				// 新增送检快捷信息
				boolean result = sjkjxxService1.inserBySjxxDto(sjxxDto);
				if (!result)
					throw new BusinessException("", "送检快捷信息更新未成功！");
			}
			if (StringUtil.isNotBlank(sjxxDto.getSjdw()))
			{
				// 查询医生表，判断是否新增医生信息
				List<SjysxxDto> SjysxxDtos = sjysxxService.selectSjysxxDtoBySjys(sjxxDto);
				if (SjysxxDtos == null || SjysxxDtos.size() == 0)
				{
					sjysxxService.insertBySjxxDto(sjxxDto);
				}
			}
			// 新增送检临床症状
			sjlczzService.insertBySjxx(sjxxDto);
			// 新增关注病原
			sjgzbyService.insertBySjxx(sjxxDto);
			// 新增前期检测
			sjqqjcService.insertBySjxx(sjxxDto);
			//没有设置url，则不发送钉钉消息，也不发送其他消息
			if(StringUtil.isNotBlank(menuurl) && StatusEnum.CHECK_PASS.getCode().equals(sjxxDto.getZt())) {
				// 发送钉钉消息
				JcsjDto jcsjDto_t = new JcsjDto();
				jcsjDto_t.setJclb("DINGMESSAGETYPE");
				jcsjDto_t.setCsdm("INSPECTION_TYPE");
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto_t).getCsdm());
				if (ddxxglDtos != null && ddxxglDtos.size() > 0)
				{
					JcsjDto jcsjDto = new JcsjDto();
					if (StringUtil.isBlank(sjxxDto.getYblxmc()))
					{
						jcsjDto = jcsjService.getDtoById(sjxxDto.getYblx());
					}
					String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
					String ICOMM_SJ00001 = xxglService.getMsg("ICOMM_SJ00001");
					for (int i = 0; i < ddxxglDtos.size(); i++)
					{
						if (StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid()))
						{
							talkUtil.sendWorkMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), ICOMM_SJ00002, StringUtil.replaceMsg(ICOMM_SJ00001, sjxxDto.getYbbh(), sjxxDto.getDb(), sjxxDto.getSjdwmc(),
									StringUtil.isBlank(sjxxDto.getYblxmc()) ? jcsjDto.getCsmc() : sjxxDto.getYblxmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), url);
						}
					}
				}
				// 检验输入的合作伙伴是否存在，不存在则发送钉钉消息给合作伙伴为无的人员
				SjhbxxDto sjhbxxDto = new SjhbxxDto();
				sjhbxxDto.setHbmc(sjxxDto.getDb());
				List<SjhbxxDto> sjhbxx = sjhbxxService.getXtyhByHbmc();
				SjhbxxDto getsjhb = sjhbxxService.selectSjhb(sjhbxxDto);
				if (getsjhb == null)
				{
					for (int i = 0; i < sjhbxx.size(); i++)
					{
						List<BtnJsonList> btnJsonLists = new ArrayList<>();
						BtnJsonList btnJsonList = new BtnJsonList();
						btnJsonList.setTitle("查看送检信息!");
						btnJsonList.setActionUrl(url);
						btnJsonLists.add(btnJsonList);
						if (StringUtil.isNotBlank(sjhbxx.get(i).getDdid()))
						{
							talkUtil.sendCardMessage(sjhbxx.get(i).getYhm(), sjhbxx.get(i).getDdid(), xxglService.getMsg("ICOMM_SJ000010"),
									xxglService.getMsg("ICOMM_SJ000011", sjxxDto.getYbbh(), sjxxDto.getSjdw(), sjxxDto.getDb(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
						}
					}
				}
				// 获取服务器文件信息
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				String fjids = StringUtils.join(sjxxDto.getFjids(), ",");
				paramMap.add("fjids", fjids);

				String param = t_restTemplate.postForObject(menuurl + "/wechat/getFjcfbModel", paramMap, String.class);
				if (param != null)
				{
					JSONArray parseArray = JSONObject.parseArray(param);
					for (int i = 0; i < parseArray.size(); i++)
					{
						FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
						boolean result = fjcfbService.insert(fjcfbModel);
						if (!result)
							throw new BusinessException("", "附件更新未成功！");
						// 下载服务器文件到指定文件夹
						downloadFile(fjcfbModel);
					}
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}

	/**
	 * 手机端修改送检信息的时候
	 *
	 * @param sjxxDto
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void checkModApply(SjxxDto sjxxDto,SjxxDto oldDto) throws BusinessException {
		if (StringUtil.isNotBlank(sjxxDto.getSjpdid())){
			SjpdglDto sjpdglDto=new SjpdglDto();
			sjpdglDto.setSjpdid(sjxxDto.getSjpdid());
			sjpdglDto.setSjid(sjxxDto.getSjid());
			boolean isSuccess= sjpdglService.update(sjpdglDto);
			if (!isSuccess)
				throw new BusinessException("", "送检id更新未成功！");
		}
		//SjxxDto oldDto = dao.getDtoById(sjxxDto.getSjid());//查数据库原信息
		// 通过合作伙伴判断是否盖章
		SjhbxxDto t_sjhbxxDto = new SjhbxxDto();
		t_sjhbxxDto.setHbmc(sjxxDto.getDb());
		t_sjhbxxDto = sjhbxxService.getDto(t_sjhbxxDto);
		if (t_sjhbxxDto != null){
			if(StringUtil.isNotBlank(t_sjhbxxDto.getSfbz())){
				sjxxDto.setFkje(t_sjhbxxDto.getSfbz());
			}
			if (StringUtil.isNotBlank(t_sjhbxxDto.getGzcskz1())){
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb(BasicDataTypeEnum.THIRD_SJXXKZ.getCode());
				List<JcsjDto> jcsjList = jcsjService.getCskz1NotNull(jcsjDto);
				if (jcsjList != null && jcsjList.size() > 0){
					sjxxDto.setCskz3(jcsjList.get(0).getCsid());
				}
			}
		}
		// TODO Auto-generated method stub
		int result = dao.weChatUpdate(sjxxDto);
		if (result == 0)
			throw new BusinessException("", "送检信息更新未成功！");
		if (StringUtil.isNotBlank(sjxxDto.getWxid())) {
			SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
			if (sjkjxxDto != null) {
				boolean isSuccess = sjkjxxService.deleteById(sjxxDto.getWxid());
				if (!isSuccess)
					throw new BusinessException("", "送检快捷信息更新未成功！");
			}
			// 新增送检快捷信息
			boolean isSuccess = sjkjxxService1.inserBySjxxDto(sjxxDto);
			if (!isSuccess)
				throw new BusinessException("", "送检快捷信息更新未成功！");
		}
		if (StringUtil.isNotBlank(sjxxDto.getSjdw())) {
			// 查询医生表，判断是否新增医生信息
			List<SjysxxDto> SjysxxDtos = sjysxxService.selectSjysxxDtoBySjys(sjxxDto);
			if (SjysxxDtos == null || SjysxxDtos.size() == 0) {
				sjysxxService.insertBySjxxDto(sjxxDto);
			}
		}
		if (!StringUtil.isNotBlank(sjxxDto.getJsrq())){
			//若样本未被接收，更新送检检测项目
			//若样本已被接收，不更新送检检测项目
			//修改送检检测项目
			sjjcxmService.updateBySjxx(sjxxDto,oldDto);
		}
		// 新增送检临床症状
		sjlczzService.insertBySjxx(sjxxDto);
		// 新增关注病原
		sjgzbyService.insertBySjxx(sjxxDto);
		// 新增前期检测
		sjqqjcService.insertBySjxx(sjxxDto);

		// 删除送检附件文件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		fjcfbService.deleteByYwidAndYwlx(fjcfbDto);

		// 没有设置URL则不去获取文件信息
		if (StringUtil.isNotBlank(menuurl) && (StatusEnum.CHECK_PASS.getCode().equals(sjxxDto.getZt()) || "0".equals(sjxxDto.getScbj()))) {
			// 获取服务器文件信息
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			String fjids = StringUtils.join(sjxxDto.getFjids(), ",");
			paramMap.add("fjids", fjids);

			RestTemplate t_restTemplate = new RestTemplate();
			String param = t_restTemplate.postForObject(menuurl + "/wechat/getFjcfbModel", paramMap, String.class);
			if (param != null) {
				JSONArray parseArray = JSONObject.parseArray(param);
				for (int i = 0; i < parseArray.size(); i++) {
					FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
					boolean isSuccess = fjcfbService.insert(fjcfbModel);
					if (!isSuccess)
						throw new BusinessException("", "附件更新未成功！");
					// 下载服务器文件到指定文件夹
					downloadFile(fjcfbModel);
				}
			}
		}
		SjtssqDto sjtssqDto=new SjtssqDto();
		sjtssqDto.setSjid(sjxxDto.getSjid());
		sjtssqDto.setZt("80");
		List<SjtssqDto> sjtssqDtos = sjtssqDao.getDtoList(sjtssqDto);
		SjjcxmDto sjjcxmDto=new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getDtoList(sjjcxmDto);
		sjjcxmService.readjustPayment(sjxxDto,sjjcxmDtos,sjtssqDtos);//公共方法，调整标本信息的付款金额以及检测项目应付金额
		sjjcxmService.updateSjjcxmDtos(sjjcxmDtos);//更新检测项目应付金额
		dao.updateTssq(sjxxDto);//更新标本信息的付款金额
	}

	/**
	 * 文件下载
	 *
	 * @param fjcfbModel
	 * @return
	 */
	public boolean downloadFile(FjcfbModel fjcfbModel)
	{
		String wjlj = fjcfbModel.getWjlj();
		String fwjlj = fjcfbModel.getFwjlj();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("wjlj", wjlj);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
			RestTemplate t_restTemplate = new RestTemplate();
			ResponseEntity<byte[]> response = t_restTemplate.exchange(menuurl + "/wechat/getImportFile", HttpMethod.POST, httpEntity, byte[].class);
			// 校验文件夹目录是否存在，不存在就创建一个目录
			mkDirs(crypt.dCode(fwjlj));
			byte[] result = response.getBody();
			inputStream = new ByteArrayInputStream(result);

			outputStream = new FileOutputStream(new File(filePath));

			int len;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1)
			{
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 根据路径创建文件
	 *
	 * @param storePath
	 * @return
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
	 * 获取检验申请清单
	 *
	 * @param sjxxDto
	 * @return
	 */
	public Map<String, Object> getApplyList(SjxxDto sjxxDto)
	{

		Map<String, Object> map = new HashMap<>();

		int cnt = dao.getInfListCount(sjxxDto);
		// 总记录条数
		map.put("totalsize", cnt);
		// 如果指定了一页条数，则按照此设定进行分页，如果没有则默认50条一页
		int pageSize = sjxxDto.getPageSize();
		if (pageSize <= 0)
		{
			pageSize = 50;
			sjxxDto.setPageSize(pageSize);
		}
		String currentPage = sjxxDto.getStartpage();
		if (StringUtil.isBlank(currentPage) || !StringUtil.isNumeric(currentPage))
		{
			currentPage = "0";
			sjxxDto.setStartpage(currentPage);
		}
		// 获取标本列表
		List<SjxxDto> sjxxList = dao.getInfList(sjxxDto);

		for (int i = 0; i < sjxxList.size(); i++)
		{
			SjxxDto sjxxDto2 = sjxxList.get(i);
			// 检测项目
			SjjcxmDto sjjcxmDto = new SjjcxmDto();
			sjjcxmDto.setSjid(sjxxDto2.getSjid());
			sjxxDto2.setSjjcxms(sjjcxmService.getDtoList(sjjcxmDto));
			// 临床症状
			SjlczzDto sjlczzDto = new SjlczzDto();
			sjlczzDto.setSjid(sjxxDto2.getSjid());
			sjxxDto2.setSjlczzs(sjlczzService.getDtoList(sjlczzDto));
			// 前期检测
			SjqqjcDto sjqqjcDto = new SjqqjcDto();
			sjqqjcDto.setSjid(sjxxDto2.getSjid());
			sjxxDto2.setSjqqjcs(sjqqjcService.getDtoList(sjqqjcDto));
			// 关注病原
			SjgzbyDto sjgzbyDto = new SjgzbyDto();
			sjqqjcDto.setSjid(sjxxDto2.getSjid());
			sjxxDto2.setSjgzbys(sjgzbyService.getDtoList(sjgzbyDto));
		}

		map.put("sjxxList", sjxxList);

		int totalpage = cnt / pageSize + 1;
		// 总页数
		map.put("totalpage", totalpage);
		// 当前页数
		map.put("currentpage", currentPage + 1);

		return map;
	}

	/**
	 * 检测结果文件处理
	 *
	 * @param sjxxDto
	 * @param fjcfbDto
	 * @return
	 */
	public boolean fileHandling(SjxxDto sjxxDto, FjcfbDto fjcfbDto)
	{
		// 删除原有文件
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (fjcfbDtos != null && fjcfbDtos.size() > 0)
		{
			for (int i = 0; i < fjcfbDtos.size(); i++)
			{
				fjcfbService.delFile(fjcfbDtos.get(i));
			}
		}
		// 文件复制到正式文件夹，插入信息至正式表
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getFjids().size(); i++)
			{
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i), sjxxDto.getSjid());
				if (!saveFile)
					return false;
			}
			// 附件排序
			boolean isSuccess = fjcfbService.fileSort(fjcfbDto);
			if (!isSuccess)
				return false;
		}
		// 拷贝文件到微信服务器
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getFjids().size(); i++)
			{
				FjcfbModel t_fjcfbModel = new FjcfbModel();
				t_fjcfbModel.setFjid(sjxxDto.getFjids().get(i));
				FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);
				boolean isSuccess = sendFileToAli(fjcfbModel);
				if (!isSuccess)
					return false;

				String ywlx = fjcfbDto.getYwlx();
				if (ywlx.indexOf("WORD") != -1){
					//转换PDF
					DBEncrypt bpe = new DBEncrypt();
					String wjljjm=bpe.dCode(fjcfbModel.getWjlj());
					//连接服务器
					boolean sendFlg=sendWordFile(wjljjm,FTP_URL);
					if(sendFlg) {
						Map<String,String> pdfMap= new HashMap<>();
						pdfMap.put("wordName", bpe.dCode(fjcfbModel.getFwjm()));
						pdfMap.put("fwjlj",fjcfbModel.getFwjlj());
						pdfMap.put("fjid",fjcfbModel.getFjid());
						pdfMap.put("ywlx",ywlx.replace("_WORD", ""));
						pdfMap.put("MQDocOkType",DOC_OK);
						//删除PDF
						FjcfbDto fjcfbDto_t=new FjcfbDto();
						fjcfbDto_t.setYwlx(pdfMap.get("ywlx").toString());
						fjcfbDto_t.setYwid(sjxxDto.getSjid());
						fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
					}else {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Z项目文件保存
	 *
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public String uploadSaveFile(SjxxDto sjxxDto) throws BusinessException {
//		//文件复制到正式文件夹，插入信息至正式表
		String fjids = "";
		for (int i = 0; i < sjxxDto.getFjids().size(); i++) {
			String ywid = StringUtil.generateUUID();
			fjids+=","+sjxxDto.getFjids().get(i);
			boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i),ywid);
			if(!saveFile)
				throw new BusinessException("msg","文件保存失败!");
		}
		if(!StringUtil.isNotBlank(fjids))
			throw new BusinessException("msg","文件保存失败!");
		Map<String,Object> map = new HashMap<>();
		map.put("fjids",fjids.substring(1));
		map.put("yhid",sjxxDto.getXgry());
		map.put("nr",sjxxDto.getNr());
		amqpTempl.convertAndSend("wechat.exchange",MQTypeEnum.Z_FILE_DISPOSE.getCode(),JSONObject.toJSONString(map));
		return fjids.substring(1);
	}

	@Override
	public boolean disposeFile(Map<String,Object> map,User userDto) throws Exception {
		FjcfbDto fjcfbDto = new FjcfbDto();
		String[] fjids = map.get("fjids").toString().split(",");
		List<String> list_fjid = new ArrayList<>();
		Collections.addAll(list_fjid, fjids);
		if (null == list_fjid || list_fjid.size()<= 0){
			//todo  发送消息处理
			log.error("未获取到相应业务ID：" + map.get("fjids").toString());
		}
		fjcfbDto.setFjids(list_fjid);

		//fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByFjids(fjcfbDto);

		if (null != fjcfbDtoList && fjcfbDtoList.size()>0){

			// 正常送检显示当前时间和前两天内的信息
			SjxxDto sjxxDto = new SjxxDto();

			sjxxDto.setNr(String.valueOf(map.get("nr")));

			//getAccptList中查的fjid和sjid都以sjid为名称返回
			List<SjxxDto> list = dao.getAccptList(sjxxDto);
			for (FjcfbDto dto : fjcfbDtoList) {
				String wjm=dto.getWjm(); //文件名
				String wjmhz=wjm.substring(wjm.lastIndexOf("."), wjm.length());
				String wjlj=dto.getWjlj();//加密后的文件路径
				DBEncrypt encrypt=new DBEncrypt();
				String wjlj_d=encrypt.dCode(wjlj);//解密后的文件路径
				if(".xlsx".equals(wjmhz)) {
					creatXSSFWorkbook(wjlj_d,userDto,list,dto);
				}else if(".xls".equals(wjmhz)) {
					creatHSSFWorkbook(wjlj_d,userDto,list,dto);
				}
			}

			if(list!=null&&!list.isEmpty()){
				List<String> ywids=new ArrayList<>();
				for(SjxxDto dto:list){
					if("1".equals(dto.getFlg_qf())){
						ywids.add(dto.getSjid());
					}
				}
				if(!ywids.isEmpty()){
					List<ShgcDto> spgwcyByYwids = shgcService.getSpgwcyByYwids(ywids);
					List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RFS_RESULTS_TYPE.getCode());
					if(spgwcyByYwids!=null&&!spgwcyByYwids.isEmpty()){
						for(ShgcDto dto:spgwcyByYwids){
							if (StringUtil.isNotBlank(dto.getDdid())) {
								String ybbh="";
								for(SjxxDto sjxxDto_t:list){
									if(sjxxDto_t.getSjid().equals(dto.getYwid())){
										ybbh=sjxxDto_t.getYbbh();
									}
								}
								SjjcjgDto sjjcjgDto = new SjjcjgDto();
								sjjcjgDto.setYwid(dto.getYwid());
								List<SjjcjgDto> dtoList1 = sjjcjgService.getDtoList(sjjcjgDto);
								if (dtoList1.size() < jcsjDtos.size()) {
									continue;
								}
								talkUtil.sendWorkMessage(dto.getYhm(), dto.getDdid(), "ResFirst附件上传通知", "您当前处负责的ResFirst审核中标本编号为: "+ybbh+" 且类型为复测申请有附件上传,请及时查看!");
							}
						}
					}
				}
			}
		}
		return true;
	}


	/**
	 * .xlsx文件解析
	 * @param wjlj
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void creatXSSFWorkbook(String wjlj,User userDto,List<SjxxDto> list,FjcfbDto dto) throws Exception {
		fjcfbService.deleteByFjid(dto);
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(wjlj));
		int numberOfSheets = workbook.getNumberOfSheets();
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RFS_RESULTS_TYPE.getCode());
		List<JcsjDto> bgjgList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_RESULT.getCode());
		//确定最小列
		JcsjDto resFirstInfo = jcsjService.getResFirstInfo();
		List<FjcfbDto> fjcfbDtoList = new ArrayList<>();
		//NC值处理
		Map<String,String> nc_map = new HashMap<>();
		List<String> stringList = new ArrayList<>();
		List<SjjcjgDto> dtos = new ArrayList<>();
		stringList.add("123");
		String empty = "NUll";
		Map<String,String> Nc_map = new HashMap<>();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet =workbook.getSheetAt(i);
			String nbbm = null;
			int max;
			int min;
			String valueString = null;//天隆还是上传
			if (null != sheet.getRow(0) && null != sheet.getRow(0).getCell(4)){
				valueString = sheet.getRow(0).getCell(4).toString();
			}
			if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
				max = 3000;
				min = 800;
			}else{
				max = 11000;
				min = 2500;
			}
			SjjcjgDto sjjcjgDto = new SjjcjgDto();
			for (int j = 4; j < 6; j++) {
				if (StringUtil.isNotBlank(nbbm)){
					break;
				}
				for (int k = 3; k < 5; k++) {
					if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k)){
						nbbm = sheet.getRow(j).getCell(k).toString().toUpperCase().split(" ")[0];
						sjjcjgDto.setNbbm(nbbm);
						break;
					}
				}
			}
			if(StringUtil.isBlank(nbbm)) {
				sjjcjgDto.setInfo("无法处理!");
				sjjcjgDto.setZt("3");
				dtos.add(sjjcjgDto);
				continue;
			}
			String bbh = null;
			String value = String.valueOf(sheet.getRow(Integer.parseInt(resFirstInfo.getMin_cel()==null?"30":resFirstInfo.getMin_cel())-1).getCell(0));
			int Identity = getIdentityValue(value);
			if (StringUtil.isNotBlank(nbbm) && null != sheet.getRow(4) && null != sheet.getRow(4).getCell(5) && StringUtil.isNotBlank(String.valueOf(sheet.getRow(4).getCell(5)))){
				bbh = String.valueOf(sheet.getRow(4).getCell(5));
				bbh=bbh.toUpperCase().split("-")[0];
				sjjcjgDto.setBbh(bbh);
				if(!"A".equals(bbh) && !"B".equals(bbh) ){
					sjjcjgDto.setInfo("版本号错误"+bbh+"！内部编码："+nbbm);
					sjjcjgDto.setZt("2");
					dtos.add(sjjcjgDto);
					sendMessage(userDto,sjjcjgDto.getInfo());
					continue;
				}
//					String pdz = String.valueOf(sheet.getRow(Integer.parseInt(resFirstInfo.getMax_cel()==null?"37":resFirstInfo.getMax_cel())-Identity).getCell(Integer.parseInt(resFirstInfo.getMax_row()==null?"13":resFirstInfo.getMax_row())-1));
//					if("B".equals(bbh) && Double.parseDouble(pdz)>500){
//						sjjcjgDto.setInfo("请确认版本信息是否正确"+bbh+"！内部编码："+nbbm);
//						sjjcjgDto.setZt("2");
//						dtos.add(sjjcjgDto);
//						sendMessage(userDto,sjjcjgDto.getInfo());
//						continue;
//					}
				if (stringList.contains(nbbm+'-'+bbh)){
					sjjcjgDto.setInfo("出现重复的内部编码"+nbbm+'-'+bbh+"！请检查文件重新上传！");
					sjjcjgDto.setZt("2");
					sjjcjgDto.setBbh("");
					dtos.add(sjjcjgDto);
					sendMessage(userDto,sjjcjgDto.getInfo());
					continue;
				}
				stringList.add(nbbm+'-'+bbh);
			}
			if (StringUtil.isNotBlank(nbbm) && (nbbm.startsWith("NC-") ||nbbm.startsWith("DC-")) ){
				if (null == bbh){
					bbh = empty;
				}
				String string = "";
				if (nbbm.startsWith("DC-")){
					string = Nc_map.get("DC-" + bbh);
				}else{
					string = Nc_map.get("NC-" + bbh);
				}
				int meter = 0;
				if (StringUtil.isNotBlank(string)){
					meter = Integer.parseInt(string);
				}
				meter++;
				if (nbbm.startsWith("DC-")){
					Nc_map.put("DC-" + bbh,String.valueOf(meter));
				}else{
					Nc_map.put("NC-" + bbh,String.valueOf(meter));
				}

				BigDecimal count = BigDecimal.ZERO;
				BigDecimal pos = BigDecimal.ZERO;
				BigDecimal one = new BigDecimal(1d);
				List<Double> doubles = new ArrayList<>();
				for (int j = 30-Identity; j <= 37-Identity; j++) {
					for (int k = 1; k <=12 ; k++) {
						if ("B".equals(bbh) && ( j == 37-Identity || (k == 12 && j == 37-Identity-1)))
							continue;
						if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k) && !"OVER".equals(sheet.getRow(j).getCell(k).toString()) && StringUtil.isNotBlank(sheet.getRow(j).getCell(k).toString())){
							double sz = Double.parseDouble(sheet.getRow(j).getCell(k).toString());
							doubles.add(sz);
							if (sz < max && sz > min){
								pos = pos.add(one);
								count = count.add(new BigDecimal(sz));
							}
						}
					}
				}
				BigDecimal compare;
				BigDecimal standard;
				BigDecimal defaultValue;
				if ("B".equals(bbh)){
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(8000);
					}
				}else{
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(9000);
					}
				}
				if(pos.compareTo(new BigDecimal(10d)) == -1) {
					//根据大小排序
					if (CollectionUtils.isEmpty(doubles) || doubles.size() < 20){
						if(CollectionUtils.isEmpty(doubles)){
							sjjcjgDto.setInfo("请确认数据是否正确"+bbh+"！内部编码："+nbbm);
						}else{
							sjjcjgDto.setInfo("符合标准数据量少于20"+bbh+"！内部编码："+nbbm);
						}
						sjjcjgDto.setZt("2");
						dtos.add(sjjcjgDto);
						sendMessage(userDto,sjjcjgDto.getInfo());
						continue;
					}
					sortDoubleList(doubles);
					//取前20个
					count = BigDecimal.ZERO;
					pos =  new BigDecimal(20);
					for (int j = 0; j < doubles.size(); j++) {
						if (j == 20)
							break;
						count = count.add(new BigDecimal(doubles.get(j)));
					}
				}
				compare = count.divide(pos,0,RoundingMode.HALF_UP);
				if(compare.compareTo(BigDecimal.ZERO) == 0) {
					standard = defaultValue;
				}else {
					standard = defaultValue.divide(compare,2,RoundingMode.HALF_UP);//标化系数
				}
				for (JcsjDto jcsjDto : jcsjDtos) {
					if (!bbh.equals(jcsjDto.getCskz4()) && !empty.equals(bbh))
						continue;
					if (empty.equals(bbh) && StringUtil.isNotBlank(jcsjDto.getCskz4()))
						continue;
					String[] strings = jcsjDto.getCskz1().split(",");
					String nc_ysz = String.valueOf(sheet.getRow(Integer.parseInt(strings[0])-Identity).getCell(Integer.parseInt(strings[1])-1));
					if (StringUtil.isBlank(nc_ysz) || "OVER".equals(nc_ysz)  || "null".equals(nc_ysz)){
						nc_ysz = "1";
					}
					// todo 待定
					String multiply = String.valueOf(new BigDecimal(Double.parseDouble(nc_ysz)).multiply(standard).setScale(0,RoundingMode.HALF_UP));
					nc_map.put(jcsjDto.getCsid()+"_"+meter+"_"+bbh,nc_ysz+"_"+multiply);
				}
			}
			sjjcjgDto.setInfo("处理中!");
			sjjcjgDto.setZt("0");
			dtos.add(sjjcjgDto);
		}
		List<String> arrayList =new ArrayList<>();
//			List<String> stringArrayList =new ArrayList<>();
		arrayList.add("1");
//			stringArrayList.add("1");
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet =workbook.getSheetAt(i);
			String nbbm = null;
			if ("2".equals(dtos.get(i).getZt()))
				break;
			else if("3".equals(dtos.get(i).getZt()))
				continue;
			for (int j = 4; j < 6; j++) {
				for (int k = 3; k < 5; k++) {
					if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k)){
						nbbm = sheet.getRow(j).getCell(k).toString().toUpperCase().split(" ")[0];
						break;
					}
				}
				if(StringUtil.isNotBlank(nbbm)){
					break;
				}
			}
			if (StringUtil.isNotBlank(nbbm)){
				int max;
				int min;
				String valueString = null;//天隆还是上传
				if (null != sheet.getRow(0) && null != sheet.getRow(0).getCell(4)){
					valueString = sheet.getRow(0).getCell(4).toString();
				}
				if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
					max = 3000;
					min = 800;
				}else{
					max = 11000;
					min = 2500;
				}
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String jcsj = dateformat.format(new Date());
				String bbh;
				if (null != sheet.getRow(4) && null != sheet.getRow(4).getCell(5) && StringUtil.isNotBlank(String.valueOf(sheet.getRow(4).getCell(5)))) {
					bbh = String.valueOf(sheet.getRow(4).getCell(5)).split("-")[0];
				}else{
					bbh = empty;
				}
				String value = String.valueOf(sheet.getRow(Integer.parseInt(resFirstInfo.getMin_cel()==null?"30":resFirstInfo.getMin_cel())-1).getCell(0));
				int Identity = getIdentityValue(value);
				BigDecimal count = BigDecimal.ZERO;
				BigDecimal pos = BigDecimal.ZERO;
				BigDecimal one = new BigDecimal(1d);
				List<Double> doubles = new ArrayList<>();
				for (int j = 30-Identity; j <= 37-Identity; j++) {
					for (int k = 1; k <=12 ; k++) {
						if ("B".equals(bbh) && ( j == 37-Identity || (k == 12 && j == 37-Identity-1)))
							continue;
						if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k) && !"OVER".equals(sheet.getRow(j).getCell(k).toString()) && StringUtil.isNotBlank(sheet.getRow(j).getCell(k).toString())){
							double sz = Double.parseDouble(sheet.getRow(j).getCell(k).toString());
							doubles.add(sz);
							if (sz < max && sz > min){
								pos = pos.add(one);
								count = count.add(new BigDecimal(sz));
							}
						}
					}
				}
				BigDecimal compare;
				BigDecimal standard;
				BigDecimal defaultValue;
				if ("B".equals(bbh)){
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(8000);
					}
				}else{
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(9000);
					}
				}
				if(pos.compareTo(new BigDecimal(10d)) == -1) {
					//根据大小排序
					if (CollectionUtils.isEmpty(doubles) || doubles.size() < 20){
						SjjcjgDto sjjcjgDto = new SjjcjgDto();
						sjjcjgDto.setNbbm(nbbm);
						sjjcjgDto.setBbh(bbh);
						if(CollectionUtils.isEmpty(doubles)){
							sjjcjgDto.setInfo("请确认数据是否正确"+bbh+"！内部编码："+nbbm);
						}else{
							sjjcjgDto.setInfo("符合标准数据量少于20"+bbh+"！内部编码："+nbbm);
						}
						sjjcjgDto.setZt("2");
						dtos.add(sjjcjgDto);
						sendMessage(userDto,sjjcjgDto.getInfo());
						continue;
					}
					sortDoubleList(doubles);
					//取前20个
					count = BigDecimal.ZERO;
					pos =  new BigDecimal(20);
					for (int j = 0; j < doubles.size(); j++) {
						if (j == 20)
							break;
						count = count.add(new BigDecimal(doubles.get(j)));
					}
				}
				compare = count.divide(pos,0,RoundingMode.HALF_UP);
				if(compare.compareTo(BigDecimal.ZERO) == 0) {
					standard = defaultValue;
				}else {
					standard = defaultValue.divide(compare,2,RoundingMode.HALF_UP);//标化系数
				}
				for (SjxxDto sjxxDto : list) {
					if(nbbm.equals(sjxxDto.getNbbm().toUpperCase())){
						List<SjjcjgDto> sjjcjgDtos = new ArrayList<>();
						if (!arrayList.contains(sjxxDto.getSjid())){
							FjcfbDto fjcfbDto = new FjcfbDto();
							fjcfbDto.setYwid(sjxxDto.getSjid());
							fjcfbDto.setFjid(StringUtil.generateUUID());
							fjcfbDto.setYwlx(dto.getYwlx());
							fjcfbDto.setWjm(dto.getWjm());
							fjcfbDto.setWjlj(dto.getWjlj());
							fjcfbDto.setZhbj(dto.getZhbj());
							fjcfbDto.setFwjlj(dto.getFwjlj());
							fjcfbDto.setFwjm(dto.getFwjm());
							fjcfbDto.setZywid(dto.getZywid());
							List<FjcfbDto> t_sjfj = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
							if(t_sjfj==null || t_sjfj.size()==0)
								fjcfbDto.setXh("1");
							else
								fjcfbDto.setXh(String.valueOf(t_sjfj.size() + 1));
							fjcfbDtoList.add(fjcfbDto);
							arrayList.add(sjxxDto.getSjid());
						}
						int index = 0;
						for (JcsjDto jcsjDto : jcsjDtos) {
							if (!bbh.equals(jcsjDto.getCskz4()) && !empty.equals(bbh))
								continue;
							if (empty.equals(bbh) && StringUtil.isNotBlank(jcsjDto.getCskz4()))
								continue;
							SjjcjgDto sjjcjgDto = new SjjcjgDto();
							sjjcjgDto.setJcjgid(StringUtil.generateUUID());
							sjjcjgDto.setYwid(sjxxDto.getSjid());
							sjjcjgDto.setLx(sjxxDto.getFlg_qf());
							sjjcjgDto.setXh(Integer.toString(index));
							sjjcjgDto.setJclx(sjxxDto.getJcxmid());
							// NC值
							String[] strList_1 = (nc_map.get(jcsjDto.getCsid()+"_1"+"_"+bbh) == null ? "0_0" : nc_map.get(jcsjDto.getCsid()+"_1"+"_"+bbh)).split("_");
							String[] strList_2 = (nc_map.get(jcsjDto.getCsid()+"_2"+"_"+bbh) == null ? "0_0" : nc_map.get(jcsjDto.getCsid()+"_2"+"_"+bbh)).split("_");
							sjjcjgDto.setNcysz(strList_1[0]);
							sjjcjgDto.setNcjsz(strList_1[1]);
							sjjcjgDto.setNcysz2(strList_2[0]);
							sjjcjgDto.setNcjsz2(strList_2[1]);
							sjjcjgDto.setJcjg(jcsjDto.getCsid());
							sjjcjgDto.setJcjgdm(jcsjDto.getCsdm());
							sjjcjgDto.setLrry(userDto.getYhid());
							String[] strings = jcsjDto.getCskz1().split(",");
							String sz = String.valueOf(sheet.getRow(Integer.parseInt(strings[0])-Identity).getCell(Integer.parseInt(strings[1])-1));
							if (StringUtil.isBlank(sz) || "OVER".equals(sz) || "null".equals(sz)){
								sz = "0";
							}
							BigDecimal beforeMultiply = new BigDecimal(Double.parseDouble(sz));
							BigDecimal multiply = beforeMultiply.multiply(standard).setScale(0,RoundingMode.HALF_UP);//标化后荧光值
							sjjcjgDto.setJgsz(String.valueOf(multiply));
							String pdz = new BigDecimal(Double.parseDouble(sz)).subtract(compare).toString();
							sjjcjgDto.setPdz(pdz);
							//参考比率2=标化后的荧光值/NC计算值   ，NC计算值等于标化后的NC值
							BigDecimal ckbl2 = new BigDecimal(0);
							if (!"0".equals(sjjcjgDto.getNcjsz())){
								ckbl2 = beforeMultiply.divide(new BigDecimal(sjjcjgDto.getNcysz()),2, RoundingMode.HALF_UP);
							}
							BigDecimal ckbl1 = beforeMultiply.divide(compare,2, RoundingMode.HALF_UP);
							sjjcjgDto.setJl(judgmentConclusion(Double.parseDouble(sjjcjgDto.getJgsz()),jcsjDto.getCskz3(),bgjgList,ckbl2,jcsjDto.getCsmc(),ckbl1));
							sjjcjgDto.setCkbl1(String.valueOf(ckbl1));
							sjjcjgDto.setCkbl2(String.valueOf(ckbl2));
							sjjcjgDto.setPjz(String.valueOf(compare));
							sjjcjgDtos.add(sjjcjgDto);
							index++;
						}
						if (CollectionUtils.isEmpty(sjjcjgDtos))
							continue;
						SjjcjgDto sjjcjgDto = new SjjcjgDto();
						sjjcjgDto.setYwid(sjxxDto.getSjid());
						sjjcjgDto.setBbh(bbh);
						sjjcjgService.delete(sjjcjgDto);
						sjjcjgService.insertList(sjjcjgDtos);
						List<SjjcjgDto> dtoList1 = sjjcjgService.getDtoList(sjjcjgDto);
						if (dtoList1.size() < jcsjDtos.size()){
							continue;
						}
						//若数据库中的检测结果与基础数据中大小一致则对物种进行特殊处理，并更新
						List<SjjcjgDto> needUpdateJlList = specialTreatment(dtoList1, bgjgList);
						//批量更新
						sjjcjgService.updatesjjcjgJlList(needUpdateJlList);
//							if (stringArrayList.contains(nbbm))
//								continue;
//							stringArrayList.add(nbbm);
						ShgcDto shgcDto = new ShgcDto();
						shgcDto.setExtend_1("[\""+sjxxDto.getSjid()+"\"]");
						if ("0".equals(sjxxDto.getFlg_qf())){
							SjxxDto sjxxDto1 = new SjxxDto();
							sjxxDto1.setSjid(sjxxDto.getSjid());
							sjxxDto1.setXgry(userDto.getYhid());
							sjxxDto1.setJcsj(jcsj);
							dao.updateDto(sjxxDto1);
							shgcDto.setShlb(AuditTypeEnum.AUDIT_RFS_SJ.getCode());
							try {
								shgcService.checkAndCommit(shgcDto,userDto);
							} catch (Exception e) {
								List<String> ywids = new ArrayList<>();
								ywids.add(sjxxDto.getSjid());
								shgcService.deleteByYwids(ywids);
								logger.error("送检删除原有审核流程！提交新的审核流程：样本编号（"+sjxxDto.getYbbh()+"）");
								shgcService.checkAndCommit(shgcDto,userDto);
							}
						}else {
							FjsqDto fjsqDto = new FjsqDto();
							fjsqDto.setFjid(sjxxDto.getSjid());
							fjsqDto.setXgry(userDto.getYhid());
							fjsqDto.setJcsj(jcsj);
							fjsqService.update(fjsqDto);
								//取消上传提示 更新为用当前人员审批
//								String RFS_SC000001 = xxglService.getMsg("RFS_SC000001");
//								String RFS_SC000002 = xxglService.getMsg("RFS_SC000002");
//								if(StringUtil.isNotBlank(userDto.getDdid())){
//									talkUtil.sendWorkMessage(userDto.getYhm(), userDto.getDdid(), RFS_SC000001,StringUtil.replaceMsg(RFS_SC000002,sjxxDto.getNbbm(),sjxxDto.getQf(),sjxxDto.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
//								}
								ShgcDto dtoByYwid = shgcService.getDtoByYwid(sjxxDto.getSjid());
								if (null != dtoByYwid && "1".equals(dtoByYwid.getXlcxh())){
									ShxxDto shxxDto = new ShxxDto();
									shxxDto.setShlb(AuditTypeEnum.AUDIT_RFS_FC.getCode());
									shxxDto.setYwid(sjxxDto.getSjid());
									shxxDto.setLcxh("1");
									shxxDto.setSftg("1");

									DataPermission.addWtParam(shxxDto);
									Map<String, String[]> paramMap = new HashMap<>();
									paramMap.put("ywid", new String[]{sjxxDto.getSjid()});
									paramMap.put("ywzd", new String[]{"sjid"});
									HttpServletRequest  newRequest = new NewHttpServletRequest(httpServletRequest,paramMap);
									AuditResult auditResult = shgcService.doAudit(shxxDto, userDto,newRequest);
									shgcService.doBatchAuditEnd(auditResult, userDto,shxxDto, true);
							}
	
						}
						SjsyglDto sjsyglDto = new SjsyglDto();
						sjsyglDto.setXgry(userDto.getYhid());
						sjsyglDto.setJcxmid(sjxxDto.getJcxmid());
						sjsyglDto.setJczxmid(sjxxDto.getJczxm());
						if ("1".equals(sjxxDto.getFlg_qf())){
							sjsyglDto.setYwid(sjxxDto.getSjid());//这里业务ID只是为了方便当作参数传递，实际上数据库已删除ywid字段
							sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
						}else {
							sjsyglDto.setYwid(sjxxDto.getSjid());//这里业务ID只是为了方便当作参数传递，实际上数据库已删除ywid字段
							sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
						}
						sjsyglService.updateJcsj(sjsyglDto);
						break;
					}
				}

			}
			dtos.get(i).setZt("1");
			dtos.get(i).setInfo("完成!");

		}
		if(null!=fjcfbDtoList && fjcfbDtoList.size() >0){
			fjcfbService.batchInsertFjcfb(fjcfbDtoList);
		}
		redisUtil.set("RFS_UP:"+userDto.getYhid()+":"+dto.getFjid(), JSONObject.toJSONString(dtos),3600L);
	}

	/**
	 * .xls文件解析
	 * @param wjlj
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void creatHSSFWorkbook(String wjlj,User userDto,List<SjxxDto> list,FjcfbDto dto) throws Exception {
		fjcfbService.deleteByFjid(dto);
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(wjlj));
		int numberOfSheets = workbook.getNumberOfSheets();
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RFS_RESULTS_TYPE.getCode());
		List<JcsjDto> bgjgList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_RESULT.getCode());
		List<FjcfbDto> fjcfbDtoList = new ArrayList<>();
		//确定最小列
		JcsjDto resFirstInfo = jcsjService.getResFirstInfo();
		//NC值处理
		Map<String,String> nc_map = new HashMap<>();
		List<String> stringList = new ArrayList<>();
		List<SjjcjgDto> dtos = new ArrayList<>();
		stringList.add("123");
		String empty = "NUll";
		Map<String,String> Nc_map = new HashMap<>();
		for (int i = 0; i < numberOfSheets; i++) {
			HSSFSheet sheet =workbook.getSheetAt(i);
			String nbbm = null;
			int max;
			int min;
			String valueString = null;//天隆还是上传
			if (null != sheet.getRow(0) && null != sheet.getRow(0).getCell(4)){
				valueString = sheet.getRow(0).getCell(4).toString();
			}
			if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
				max = 3000;
				min = 800;
			}else{
				max = 11000;
				min = 2500;
			}
			SjjcjgDto sjjcjgDto = new SjjcjgDto();
			for (int j = 4; j < 6; j++) {
				if (StringUtil.isNotBlank(nbbm)){
					break;
				}
				for (int k = 3; k < 5; k++) {
					if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k)){
						nbbm = sheet.getRow(j).getCell(k).toString().toUpperCase().split(" ")[0];
						sjjcjgDto.setNbbm(nbbm);
						break;
					}
				}
			}
			if(StringUtil.isBlank(nbbm)) {
				sjjcjgDto.setInfo("无法处理!");
				sjjcjgDto.setZt("3");
				dtos.add(sjjcjgDto);
				continue;
			}
			String bbh = null;
			String value = String.valueOf(sheet.getRow(Integer.parseInt(resFirstInfo.getMin_cel()==null?"30":resFirstInfo.getMin_cel())-1).getCell(0));
			int Identity = getIdentityValue(value);
			if (StringUtil.isNotBlank(nbbm) && null != sheet.getRow(4) && null != sheet.getRow(4).getCell(5) && StringUtil.isNotBlank(String.valueOf(sheet.getRow(4).getCell(5)))){
				bbh = String.valueOf(sheet.getRow(4).getCell(5));
				bbh=bbh.toUpperCase().split("-")[0];
				sjjcjgDto.setBbh(bbh);
				if(!"A".equals(bbh) && !"B".equals(bbh) ){
					sjjcjgDto.setInfo("版本号错误"+bbh+"！内部编码："+nbbm);
					sjjcjgDto.setZt("2");
					dtos.add(sjjcjgDto);
					sendMessage(userDto,sjjcjgDto.getInfo());
					continue;
				}
				/*String pdz = String.valueOf(sheet.getRow(Integer.parseInt(resFirstInfo.getMax_cel()==null?"37":resFirstInfo.getMax_cel())-Identity).getCell(Integer.parseInt(resFirstInfo.getMax_row()==null?"13":resFirstInfo.getMax_row())-1));
				if("B".equals(bbh) && Double.parseDouble(pdz)>500){
					sjjcjgDto.setInfo("请确认版本信息是否正确"+bbh+"！内部编码："+nbbm);
					sjjcjgDto.setZt("2");
					dtos.add(sjjcjgDto);
					sendMessage(userDto,sjjcjgDto.getInfo());
					continue;
				}*/
				if (stringList.contains(nbbm+'-'+bbh)){
					sjjcjgDto.setInfo("出现重复的内部编码"+nbbm+'-'+bbh+"！请检查文件重新上传！");
					sjjcjgDto.setZt("2");
					sjjcjgDto.setBbh("");
					dtos.add(sjjcjgDto);
					sendMessage(userDto,sjjcjgDto.getInfo());
					continue;
				}
				stringList.add(nbbm+'-'+bbh);
			}
			if (StringUtil.isNotBlank(nbbm) && (nbbm.startsWith("NC-") || nbbm.startsWith("DC-"))){
				if (null == bbh){
					bbh = empty;
				}
				//String string = Nc_map.get("NC-" + bbh);
				String string = "";
				if (nbbm.startsWith("DC-")){
					string = Nc_map.get("DC-" + bbh);
				}else{
					string = Nc_map.get("NC-" + bbh);
				}
				int meter = 0;
				if (StringUtil.isNotBlank(string)){
					meter = Integer.parseInt(string);
				}
				meter++;
				//Nc_map.put("NC-" + bbh,String.valueOf(meter));
                if (nbbm.startsWith("DC-")){
                    Nc_map.put("DC-" + bbh,String.valueOf(meter));
                }else{
                    Nc_map.put("NC-" + bbh,String.valueOf(meter));
                }

				BigDecimal count = BigDecimal.ZERO;
				BigDecimal pos = BigDecimal.ZERO;
				BigDecimal one = new BigDecimal(1d);
				List<Double> doubles = new ArrayList<>();
				for (int j = 30-Identity; j <= 37-Identity; j++) {
					for (int k = 1; k <=12 ; k++) {
						if ("B".equals(bbh) && ( j == 37-Identity || (k == 12 && j == 37-Identity-1)))
							continue;
						if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k) && !"OVER".equals(sheet.getRow(j).getCell(k).toString()) && StringUtil.isNotBlank(sheet.getRow(j).getCell(k).toString())){
							double sz = Double.parseDouble(sheet.getRow(j).getCell(k).toString());
							doubles.add(sz);
							if (sz < max && sz > min){
								pos = pos.add(one);
								count = count.add(new BigDecimal(sz));
							}
						}
					}
				}
				BigDecimal compare;
				BigDecimal standard;
				BigDecimal defaultValue;
				if ("B".equals(bbh)){
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(8000);
					}
				}else{
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(9000);
					}
				}
				if(pos.compareTo(new BigDecimal(10d)) == -1) {
					//根据大小排序
					if (CollectionUtils.isEmpty(doubles) || doubles.size() < 20){
						if(CollectionUtils.isEmpty(doubles)){
							sjjcjgDto.setInfo("请确认数据是否正确"+bbh+"！内部编码："+nbbm);
						}else{
							sjjcjgDto.setInfo("符合标准数据量少于20"+bbh+"！内部编码："+nbbm);
						}
						sjjcjgDto.setZt("2");
						dtos.add(sjjcjgDto);
						sendMessage(userDto,sjjcjgDto.getInfo());
						continue;
					}
					sortDoubleList(doubles);
					//取前20个
					count = BigDecimal.ZERO;
					pos =  new BigDecimal(20);
					for (int j = 0; j < doubles.size(); j++) {
						if (j == 20)
							break;
						count = count.add(new BigDecimal(doubles.get(j)));
					}
				}
				compare = count.divide(pos,0,RoundingMode.HALF_UP);
				if(compare.compareTo(BigDecimal.ZERO) == 0) {
					standard = defaultValue;
				}else {
					standard = defaultValue.divide(compare,2,RoundingMode.HALF_UP);//标化系数
				}
				for (JcsjDto jcsjDto : jcsjDtos) {
					if (!bbh.equals(jcsjDto.getCskz4()) && !empty.equals(bbh))
						continue;
					if (empty.equals(bbh) && StringUtil.isNotBlank(jcsjDto.getCskz4()))
						continue;
					String[] strings = jcsjDto.getCskz1().split(",");
					String nc_ysz = String.valueOf(sheet.getRow(Integer.parseInt(strings[0])-Identity).getCell(Integer.parseInt(strings[1])-1));
					if (StringUtil.isBlank(nc_ysz) || "OVER".equals(nc_ysz) || "null".equals(nc_ysz)){
						nc_ysz = "1";
					}
					// todo 待定
					String multiply = String.valueOf(new BigDecimal(Double.parseDouble(nc_ysz)).multiply(standard).setScale(0,RoundingMode.HALF_UP));
					nc_map.put(jcsjDto.getCsid()+"_"+meter+"_"+bbh,nc_ysz+"_"+multiply);
				}
			}
			sjjcjgDto.setInfo("处理中!");
			sjjcjgDto.setZt("0");
			dtos.add(sjjcjgDto);
		}
		List<String> arrayList =new ArrayList<>();
//			List<String> stringArrayList =new ArrayList<>();
		arrayList.add("1");
//			stringArrayList.add("1");
		for (int i = 0; i < numberOfSheets; i++) {
			HSSFSheet sheet =workbook.getSheetAt(i);
			if ("2".equals(dtos.get(i).getZt()))
				break;
			else if("3".equals(dtos.get(i).getZt()))
				continue;
			String nbbm = null;
			for (int j = 4; j < 6; j++) {
				for (int k = 3; k < 5; k++) {
					if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k)){
						nbbm = sheet.getRow(j).getCell(k).toString().toUpperCase().split(" ")[0];
						break;
					}
				}
				if(StringUtil.isNotBlank(nbbm)){
					break;
				}
			}
			if (StringUtil.isNotBlank(nbbm)){
				int max;
				int min;
				String valueString = null;//天隆还是上传
				if (null != sheet.getRow(0) && null != sheet.getRow(0).getCell(4)){
					valueString = sheet.getRow(0).getCell(4).toString();
				}
				if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
					max = 3000;
					min = 800;
				}else{
					max = 11000;
					min = 2500;
				}
				SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String jcsj = dateformat.format(new Date());
				String bbh;
				if (null != sheet.getRow(4) && null != sheet.getRow(4).getCell(5) && StringUtil.isNotBlank(String.valueOf(sheet.getRow(4).getCell(5)))) {
					bbh = String.valueOf(sheet.getRow(4).getCell(5)).split("-")[0];
				}else{
					bbh = empty;
				}
				String value = String.valueOf(sheet.getRow(Integer.parseInt(resFirstInfo.getMin_cel()==null?"30":resFirstInfo.getMin_cel())-1).getCell(0));
				int Identity = getIdentityValue(value);
				BigDecimal count = BigDecimal.ZERO;
				BigDecimal pos = BigDecimal.ZERO;
				BigDecimal one = new BigDecimal(1d);
				List<Double> doubles = new ArrayList<>();
				for (int j = 30-Identity; j <= 37-Identity; j++) {
					for (int k = 1; k <=12 ; k++) {
						if ("B".equals(bbh) && ( j == 37-Identity || (k == 12 && j == 37-Identity-1)))
							continue;
						if (null != sheet.getRow(j) && null != sheet.getRow(j).getCell(k) && !"OVER".equals(sheet.getRow(j).getCell(k).toString()) && StringUtil.isNotBlank(sheet.getRow(j).getCell(k).toString())){
							double sz = Double.parseDouble(sheet.getRow(j).getCell(k).toString());
							doubles.add(sz);
							if (sz < max && sz > min){
								pos = pos.add(one);
								count = count.add(new BigDecimal(sz));
							}
						}
					}
				}
				BigDecimal compare;
				BigDecimal standard;
				BigDecimal defaultValue;
				if ("B".equals(bbh)){
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(8000);
					}
				}else{
					if (StringUtils.isNotBlank(valueString) && "pcrExperiment".equals(valueString)){
						defaultValue = new BigDecimal(7000);
					}else{
						defaultValue = new BigDecimal(9000);
					}
				}
				if(pos.compareTo(new BigDecimal(10d)) == -1) {
					//根据大小排序
					if (CollectionUtils.isEmpty(doubles) || doubles.size() < 20){
						SjjcjgDto sjjcjgDto = new SjjcjgDto();
						sjjcjgDto.setNbbm(nbbm);
						sjjcjgDto.setBbh(bbh);
						if(CollectionUtils.isEmpty(doubles)){
							sjjcjgDto.setInfo("请确认数据是否正确"+bbh+"！内部编码："+nbbm);
						}else{
							sjjcjgDto.setInfo("符合标准数据量少于20"+bbh+"！内部编码："+nbbm);
						}
						sjjcjgDto.setZt("2");
						dtos.add(sjjcjgDto);
						sendMessage(userDto,sjjcjgDto.getInfo());
						continue;
					}
					sortDoubleList(doubles);
					//取前20个
					count = BigDecimal.ZERO;
					pos =  new BigDecimal(20);
					for (int j = 0; j < doubles.size(); j++) {
						if (j == 20)
							break;
						count = count.add(new BigDecimal(doubles.get(j)));
					}
				}
				compare = count.divide(pos,0,RoundingMode.HALF_UP);
				if(compare.compareTo(BigDecimal.ZERO) == 0) {
					standard = defaultValue;
				}else {
					standard = defaultValue.divide(compare,2,RoundingMode.HALF_UP);//标化系数
				}
				for (SjxxDto sjxxDto : list) {
					if(nbbm.equals(sjxxDto.getNbbm().toUpperCase())){
						List<SjjcjgDto> sjjcjgDtos = new ArrayList<>();
						if (!arrayList.contains(sjxxDto.getSjid())){
							FjcfbDto fjcfbDto = new FjcfbDto();
							fjcfbDto.setYwid(sjxxDto.getSjid());
							fjcfbDto.setFjid(StringUtil.generateUUID());
							fjcfbDto.setYwlx(dto.getYwlx());
							fjcfbDto.setWjm(dto.getWjm());
							fjcfbDto.setWjlj(dto.getWjlj());
							fjcfbDto.setZhbj(dto.getZhbj());
							fjcfbDto.setFwjlj(dto.getFwjlj());
							fjcfbDto.setFwjm(dto.getFwjm());
							fjcfbDto.setZywid(dto.getZywid());
							List<FjcfbDto> t_sjfj = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
							if(t_sjfj==null || t_sjfj.size()==0)
								fjcfbDto.setXh("1");
							else
								fjcfbDto.setXh(String.valueOf(t_sjfj.size() + 1));
							fjcfbDtoList.add(fjcfbDto);
							arrayList.add(sjxxDto.getSjid());
						}
						int index = 0;
						for (JcsjDto jcsjDto : jcsjDtos) {
							if (!bbh.equals(jcsjDto.getCskz4()) && !empty.equals(bbh))
								continue;
							if (empty.equals(bbh) && StringUtil.isNotBlank(jcsjDto.getCskz4()))
								continue;
							SjjcjgDto sjjcjgDto = new SjjcjgDto();
							sjjcjgDto.setJcjgid(StringUtil.generateUUID());
							sjjcjgDto.setYwid(sjxxDto.getSjid());
							sjjcjgDto.setLx(sjxxDto.getFlg_qf());
							sjjcjgDto.setXh(Integer.toString(index));
							sjjcjgDto.setJclx(sjxxDto.getJcxmid());
							// NC值
							String[] strList_1 = (nc_map.get(jcsjDto.getCsid()+"_1"+"_"+bbh) == null ? "0_0" : nc_map.get(jcsjDto.getCsid()+"_1"+"_"+bbh)).split("_");
							String[] strList_2 = (nc_map.get(jcsjDto.getCsid()+"_2"+"_"+bbh) == null ? "0_0" : nc_map.get(jcsjDto.getCsid()+"_2"+"_"+bbh)).split("_");
							sjjcjgDto.setNcysz(strList_1[0]);
							sjjcjgDto.setNcjsz(strList_1[1]);
							sjjcjgDto.setNcysz2(strList_2[0]);
							sjjcjgDto.setNcjsz2(strList_2[1]);
							sjjcjgDto.setJcjg(jcsjDto.getCsid());
							sjjcjgDto.setJcjgdm(jcsjDto.getCsdm());
							sjjcjgDto.setLrry(userDto.getYhid());
							String[] strings = jcsjDto.getCskz1().split(",");
							String sz = String.valueOf(sheet.getRow(Integer.parseInt(strings[0])-Identity).getCell(Integer.parseInt(strings[1])-1));
							if (StringUtil.isBlank(sz) || "OVER".equals(sz) || "null".equals(sz)){
								sz = "0";
							}
							BigDecimal beforeMultiply = new BigDecimal(Double.parseDouble(sz));
							BigDecimal multiply = beforeMultiply.multiply(standard).setScale(0,RoundingMode.HALF_UP);//标化后荧光值
							sjjcjgDto.setJgsz(String.valueOf(multiply));
							String pdz = new BigDecimal(Double.parseDouble(sz)).subtract(compare).toString();
							sjjcjgDto.setPdz(pdz);
							BigDecimal ckbl2 = new BigDecimal(0);
							if (!"0".equals(sjjcjgDto.getNcjsz())){
								ckbl2 = beforeMultiply.divide(new BigDecimal(sjjcjgDto.getNcysz()),2, RoundingMode.HALF_UP);
							}
							BigDecimal ckbl1 = beforeMultiply.divide(compare,2, RoundingMode.HALF_UP);
							sjjcjgDto.setJl(judgmentConclusion(Double.parseDouble(sjjcjgDto.getJgsz()),jcsjDto.getCskz3(),bgjgList,ckbl2,jcsjDto.getCsmc(),ckbl1));
							sjjcjgDto.setCkbl1(String.valueOf(ckbl1));
							sjjcjgDto.setCkbl2(String.valueOf(ckbl2));
							sjjcjgDto.setPjz(String.valueOf(compare));
							sjjcjgDtos.add(sjjcjgDto);
							index++;
						}
						if (CollectionUtils.isEmpty(sjjcjgDtos))
							continue;
						SjjcjgDto sjjcjgDto = new SjjcjgDto();
						sjjcjgDto.setYwid(sjxxDto.getSjid());
						sjjcjgDto.setBbh(bbh);
						sjjcjgService.delete(sjjcjgDto);
						sjjcjgService.insertList(sjjcjgDtos);
						List<SjjcjgDto> dtoList1 = sjjcjgService.getDtoList(sjjcjgDto);
						if (dtoList1.size() < jcsjDtos.size()) {
							continue;
						}
						//若数据库中的检测结果与基础数据中大小一致则对物种进行特殊处理，并更新
						List<SjjcjgDto> needUpdateJlList = specialTreatment(dtoList1, bgjgList);
						//批量更新
						sjjcjgService.updatesjjcjgJlList(needUpdateJlList);
//							if (stringArrayList.contains(nbbm))
//								continue;
//							stringArrayList.add(nbbm);
						ShgcDto shgcDto = new ShgcDto();
						shgcDto.setExtend_1("[\""+sjxxDto.getSjid()+"\"]");
						if ("0".equals(sjxxDto.getFlg_qf())){
							SjxxDto sjxxDto1 = new SjxxDto();
							sjxxDto1.setSjid(sjxxDto.getSjid());
							sjxxDto1.setXgry(userDto.getYhid());
							sjxxDto1.setJcsj(jcsj);
							dao.updateDto(sjxxDto1);
							shgcDto.setShlb(AuditTypeEnum.AUDIT_RFS_SJ.getCode());
							try {
								shgcService.checkAndCommit(shgcDto,userDto);
							} catch (Exception e) {
								List<String> ywids = new ArrayList<>();
								ywids.add(sjxxDto.getSjid());
								shgcService.deleteByYwids(ywids);
								logger.error("送检删除原有审核流程！提交新的审核流程：样本编号（"+sjxxDto.getYbbh()+"）");
								shgcService.checkAndCommit(shgcDto,userDto);
							}
						}else {
	
							FjsqDto fjsqDto = new FjsqDto();
							fjsqDto.setFjid(sjxxDto.getSjid());
							fjsqDto.setXgry(userDto.getYhid());
							fjsqDto.setJcsj(jcsj);
							fjsqService.update(fjsqDto);
								//取消上传提示 更新为用当前人员审批
//								String RFS_SC000001 = xxglService.getMsg("RFS_SC000001");
//								String RFS_SC000002 = xxglService.getMsg("RFS_SC000002");
//								if(StringUtil.isNotBlank(userDto.getDdid())){
//									talkUtil.sendWorkMessage(userDto.getYhm(), userDto.getDdid(), RFS_SC000001,StringUtil.replaceMsg(RFS_SC000002,sjxxDto.getNbbm(),sjxxDto.getQf(),sjxxDto.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
//								}
								ShgcDto dtoByYwid = shgcService.getDtoByYwid(sjxxDto.getSjid());
								if (null != dtoByYwid && "1".equals(dtoByYwid.getXlcxh())){
									ShxxDto shxxDto = new ShxxDto();
									shxxDto.setShlb(AuditTypeEnum.AUDIT_RFS_FC.getCode());
									shxxDto.setYwid(sjxxDto.getSjid());
									shxxDto.setLcxh("1");
									shxxDto.setSftg("1");

									DataPermission.addWtParam(shxxDto);
									Map<String, String[]> paramMap = new HashMap<>();
									paramMap.put("ywid", new String[]{sjxxDto.getSjid()});
									paramMap.put("ywzd", new String[]{"sjid"});
									HttpServletRequest  newRequest = new NewHttpServletRequest(httpServletRequest,paramMap);
									AuditResult auditResult = shgcService.doAudit(shxxDto, userDto,newRequest);
									shgcService.doBatchAuditEnd(auditResult, userDto,shxxDto, true);
							}
						}
						SjsyglDto sjsyglDto = new SjsyglDto();
						sjsyglDto.setXgry(userDto.getYhid());
						sjsyglDto.setJcxmid(sjxxDto.getJcxmid());
						sjsyglDto.setYwid(sjxxDto.getSjid());//这里的getSjid包含sjid或者fjid
						if ("1".equals(sjxxDto.getFlg_qf())){
							sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
						}else {
							sjsyglDto.setYwlx(DetectionTypeEnum.DETECT_SJ.getCode());
						}
						sjsyglService.updateJcsj(sjsyglDto);
						break;
					}
				}

			}
			dtos.get(i).setZt("1");
			dtos.get(i).setInfo("完成!");

		}
		if(null!=fjcfbDtoList && fjcfbDtoList.size() >0){
			fjcfbService.batchInsertFjcfb(fjcfbDtoList);
		}
		redisUtil.set("RFS_UP:"+userDto.getYhid()+":"+dto.getFjid(), JSONObject.toJSONString(dtos),3600L);
	}
	public void sortDoubleList (List<Double> doubles){
		if (doubles.size() > 0){
			// 使用 Stream 排序
			doubles.sort(new Comparator<>() {
							 @Override
							 public int compare(Double d1, Double d2) {
								 return d1.compareTo(d2);
							 }
						 }
			);
		}
	}

	/**
	 * 判断改物种结果知否存在
	 * @param sjjcjgMap 物种Map
	 * @param wzcsdm 匹配的物种代码
	 * @param jcjg 匹配的结果，ying,yang,huiqu
	 * @return
	 */
	public boolean checkSjjcjg(Map<String,SjjcjgDto> sjjcjgMap,String wzcsdm, JcsjDto jcjg){
		return sjjcjgMap.get(wzcsdm) != null && jcjg != null && jcjg.getCsid().equals(sjjcjgMap.get(wzcsdm).getJl());
	}
	/**
	 * 特殊判断
	 * @param sjjcjgDtos
	 * @param jlList
	 */
	public List<SjjcjgDto> specialTreatment(List<SjjcjgDto> sjjcjgDtos,List<JcsjDto> jlList){
		List<SjjcjgDto> needUpdateList = new ArrayList<>();
		JcsjDto yang = new JcsjDto();
		JcsjDto ying = new JcsjDto();
//		JcsjDto huiqu = new JcsjDto();
		for (JcsjDto jcsjDto : jlList) {
			if ("001".equals(jcsjDto.getCsdm())){
				yang = jcsjDto;
				continue;
			}
			if ("002".equals(jcsjDto.getCsdm())){
				ying = jcsjDto;
				continue;
			}
//			if ("003".equals(jcsjDto.getCsdm())){
//				huiqu = jcsjDto;
//				break;
//			}
		}
//		String jcjgdm = sjjcjgDto.getJcjgdm();//获取ResFirst检测结果代码（即菌的参数代码）
//		String jldm = sjjcjgDto.getJldm();//获取结论代码：001 阳性，002 阴性，003 检测灰区
		Map<String,SjjcjgDto> sjjcjgMap = new HashMap<>();
		//将sjjcjgDtos存入sjjcjgMap中，sjjcjgDto.getJcjgdm()作为key,sjjcjgDto作为value
		for (SjjcjgDto sjjcjgDto : sjjcjgDtos) {
			sjjcjgMap.put(sjjcjgDto.getJcjgdm(),sjjcjgDto);
		}
		//处理非耐药基因病原体报告逻辑：
		if ( checkSjjcjg(sjjcjgMap,"573",yang) ){
			//1）产酸克雷伯菌(参数代码 571)阳性或者灰区时，当肺炎克雷伯菌(参数代码 573)阳性，将产酸克雷伯菌(参数代码 571)该条数据放入阴性列表，其他情况不变化。
			if (sjjcjgMap.get("571") != null){
				sjjcjgMap.get("571").setJldm(ying.getCsdm());
				sjjcjgMap.get("571").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("571"));
			}
			//5）米氏克雷伯氏菌(参数代码 1134687)阳性或者灰区时，当肺炎克雷伯菌(参数代码 573)阳性时，将米氏克雷伯氏菌(参数代码 1134687)该条数据放入阴性列表。其他情况不变化。
			if (sjjcjgMap.get("1134687") != null){
				sjjcjgMap.get("1134687").setJldm(ying.getCsdm());
				sjjcjgMap.get("1134687").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("1134687"));
			}
			//6）拟肺炎克雷伯菌(参数代码 1463165)阳性或者灰区时，当肺炎克雷伯菌(参数代码 573)阳性时，将拟肺炎克雷伯菌(参数代码 1463165)该条数据放入阴性列表。其他情况不变化。
			if ( sjjcjgMap.get("1463165") != null){
				sjjcjgMap.get("1463165").setJldm(ying.getCsdm());
				sjjcjgMap.get("1463165").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("1463165"));
			}
		}
		//2）副血链球菌(参数代码 1318)阳性或者灰区时，当肺炎链球菌(参数代码 1313)阳性时，将副血链球菌(参数代码 1318)该条数据放入阴性列表，其他情况不变化。
		if ( sjjcjgMap.get("1318") != null && checkSjjcjg(sjjcjgMap,"1313",yang) ){
			sjjcjgMap.get("1318").setJldm(ying.getCsdm());
			sjjcjgMap.get("1318").setJl(ying.getCsid());
			needUpdateList.add(sjjcjgMap.get("1318"));
		}
		//3) 无乳链球菌(参数代码 1311)阳性或者灰区时，当肺炎链球菌(参数代码 1313)或者副血链球菌(参数代码 1318)阳性时，将无乳链球菌(参数代码 1311)该条数据放入阴性列表，其他情况不变化。
		if ( sjjcjgMap.get("1311") != null && (checkSjjcjg(sjjcjgMap,"1313",yang) || checkSjjcjg(sjjcjgMap,"1318",yang)) ){
			sjjcjgMap.get("1311").setJldm(ying.getCsdm());
			sjjcjgMap.get("1311").setJl(ying.getCsid());
			needUpdateList.add(sjjcjgMap.get("1311"));
		}
		//4）戈登链球菌(参数代码 1302)阳性或者灰区时，当肺炎链球菌(参数代码 1313)或无乳链球菌(参数代码 1311)或副血链球菌(参数代码 1318)阳性时，将戈登链球菌(参数代码 1302)该条数据放入阴性列表。其他情况不变化。
		if ( sjjcjgMap.get("1302") != null && (checkSjjcjg(sjjcjgMap,"1313",yang) || checkSjjcjg(sjjcjgMap,"1311",yang) || checkSjjcjg(sjjcjgMap,"1318",yang)) ){
			sjjcjgMap.get("1302").setJldm(ying.getCsdm());
			sjjcjgMap.get("1302").setJl(ying.getCsid());
			needUpdateList.add(sjjcjgMap.get("1302"));
		}
		//处理耐药基因病原体报告逻辑：
		//[1280,1283] -- 金黄色葡萄球菌(参数代码 1280)，溶血葡萄球菌(参数代码 1283)
		if ( checkSjjcjg(sjjcjgMap,"1280",ying) && checkSjjcjg(sjjcjgMap,"1283",ying) ){
			//耐药基因-mecA(参数代码 NY-mecA) 阳性或者灰区时，当金黄色葡萄球菌(参数代码 1280)，溶血葡萄球菌(参数代码 1283)均为阴性时，将耐药基因-mecA 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-mecA") != null){
				sjjcjgMap.get("NY-mecA").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-mecA").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-mecA"));
			}
			//耐药基因-mecC(参数代码 NY-mecC) 阳性或者灰区时，当金黄色葡萄球菌(参数代码 1280)，溶血葡萄球菌(参数代码 1283)均为阴性时，将耐药基因-mecC 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-mecC") != null){
				sjjcjgMap.get("NY-mecC").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-mecC").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-mecC"));
			}
		}
		//[1352,1351] -- 屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)
		if ( checkSjjcjg(sjjcjgMap,"1352",ying) && checkSjjcjg(sjjcjgMap,"1351",ying) ){
			//耐药基因-vanA(参数代码 NY-vanA) 阳性或者灰区时，当屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)均为阴性时，将耐药基因-vanA 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-vanA") != null){
				sjjcjgMap.get("NY-vanA").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-vanA").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-vanA"));
			}
			//耐药基因-vanB(参数代码 NY-vanB) 阳性或者灰区时，当屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)均为阴性时，将耐药基因-vanB 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-vanB") != null){
				sjjcjgMap.get("NY-vanB").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-vanB").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-vanB"));
			}
			//耐药基因-vanC(参数代码 NY-vanC) 阳性或者灰区时，当屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)均为阴性时，将耐药基因-vanC 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-vanC") != null){
				sjjcjgMap.get("NY-vanC").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-vanC").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-vanC"));
			}
			//耐药基因-vanE(参数代码 NY-vanE) 阳性或者灰区时，当屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)均为阴性时，将耐药基因-vanE 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-vanE") != null){
				sjjcjgMap.get("NY-vanE").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-vanE").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-vanE"));
			}
		}
		//[470,106654,584,562] -- 鲍曼不动杆菌(参数代码 470)，医院不动杆菌(参数代码 106654)，奇异变形杆菌(参数代码 584)，大肠埃希菌(参数代码 562)
		//耐药基因-OXA-23(参数代码 NY-OXA-23) 阳性或者灰区时，当鲍曼不动杆菌(参数代码 470)，医院不动杆菌(参数代码 106654)，奇异变形杆菌(参数代码 584)，大肠埃希菌(参数代码 562)均为阴性时，将耐药基因OXA-23 放入阴性列表。其他情况，不做变化。
		if ( sjjcjgMap.get("NY-OXA-23") != null && checkSjjcjg(sjjcjgMap,"470",ying) && checkSjjcjg(sjjcjgMap,"106654",ying) && checkSjjcjg(sjjcjgMap,"584",ying) && checkSjjcjg(sjjcjgMap,"562",ying) ){
			sjjcjgMap.get("NY-OXA-23").setJldm(ying.getCsdm());
			sjjcjgMap.get("NY-OXA-23").setJl(ying.getCsid());
			needUpdateList.add(sjjcjgMap.get("NY-OXA-23"));
		}
		//[573,571,548,1463165,1134687,354276] -- 雷伯菌属（Klebsiella）包括肺炎克雷伯菌(参数代码 573)，产酸克雷伯菌(参数代码 571)，产气克雷伯菌(参数代码 548)，拟肺炎克雷伯菌(参数代码 1463165)，米氏克雷伯氏菌(参数代码 1134687)，阴沟肠杆菌复合群(参数代码 354276)
		if ( checkSjjcjg(sjjcjgMap,"573",ying) && checkSjjcjg(sjjcjgMap,"571",ying) && checkSjjcjg(sjjcjgMap,"548",ying) && checkSjjcjg(sjjcjgMap,"1463165",ying) && checkSjjcjg(sjjcjgMap,"1134687",ying) && checkSjjcjg(sjjcjgMap,"354276",ying) ){
			//[562] + [573,571,548,1463165,1134687,354276] -- 大肠埃希菌(参数代码 562)
			if (checkSjjcjg(sjjcjgMap,"562",ying)){
				//耐药基因-OXA-48(参数代码 NY-OXA-48) 阳性或者灰区时，当大肠埃希菌(参数代码 562)，克雷伯菌属（Klebsiella）包括肺炎克雷伯菌(参数代码 573)，产酸克雷伯菌(参数代码 571)，产气克雷伯菌(参数代码 548)，拟肺炎克雷伯菌(参数代码 1463165)，米氏克雷伯氏菌(参数代码 1134687)，阴沟肠杆菌复合群(参数代码 354276)均为阴性时，将耐药基因OXA-48放入阴性列表。其他情况，不做变化。
				if (sjjcjgMap.get("NY-OXA-48") != null){
					sjjcjgMap.get("NY-OXA-48").setJldm(ying.getCsdm());
					sjjcjgMap.get("NY-OXA-48").setJl(ying.getCsid());
					needUpdateList.add(sjjcjgMap.get("NY-OXA-48"));
				}
				//耐药基因-CTX-M(参数代码 NY-CTX-M) 阳性或者灰区时，当大肠埃希菌(参数代码 562)，克雷伯菌属（Klebsiella）包括肺炎克雷伯菌(参数代码 573)，产酸克雷伯菌(参数代码 571)，产气克雷伯菌(参数代码 548)，拟肺炎克雷伯菌(参数代码 1463165)，米氏克雷伯氏菌(参数代码 1134687)，阴沟肠杆菌复合群(参数代码 354276)均为阴性时，将耐药基因CTX-M 放入阴性列表。其他情况，不做变化。
				if (sjjcjgMap.get("NY-CTX-M") != null){
					sjjcjgMap.get("NY-CTX-M").setJldm(ying.getCsdm());
					sjjcjgMap.get("NY-CTX-M").setJl(ying.getCsid());
					needUpdateList.add(sjjcjgMap.get("NY-CTX-M"));
				}
				//耐药基因-NDM(参数代码 NY-NDM) 阳性或灰区时，当大肠埃希菌(参数代码 562)，克雷伯菌属（Klebsiella）包括肺炎克雷伯菌(参数代码 573)，产酸克雷伯菌(参数代码 571)，产气克雷伯菌(参数代码 548)，拟肺炎克雷伯菌(参数代码 1463165)，米氏克雷伯氏菌(参数代码 1134687)，阴沟肠杆菌复合群(参数代码 354276)，铜绿假单胞菌(参数代码 287)均为阴性时，将耐药基因-NDM放入阴性列表。其他情况不做变化。
				if ( sjjcjgMap.get("NY-NDM") != null && checkSjjcjg(sjjcjgMap,"287",ying) ){
					sjjcjgMap.get("NY-NDM").setJldm(ying.getCsdm());
					sjjcjgMap.get("NY-NDM").setJl(ying.getCsid());
					needUpdateList.add(sjjcjgMap.get("NY-NDM"));
				}
			}
			//[562,1352,1351] + [573,571,548,1463165,1134687,354276] -- 大肠埃希菌(参数代码 562),屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)
			//耐药基因-vanG(参数代码 NY-vanG) 阳性或者灰区时，当大肠埃希菌(参数代码 562)，克雷伯菌属（Klebsiella）包括肺炎克雷伯菌(参数代码 573)，产酸克雷伯菌(参数代码 571)，产气克雷伯菌(参数代码 548)，拟肺炎克雷伯菌(参数代码 1463165)，米氏克雷伯氏菌(参数代码 1134687)，阴沟肠杆菌复合群(参数代码 354276)，屎肠球菌(参数代码 1352)，粪肠球菌(参数代码 1351)均为阴性时，将耐药基因-vanG 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-vanG") != null && checkSjjcjg(sjjcjgMap,"562",ying) && checkSjjcjg(sjjcjgMap,"1352",ying) && checkSjjcjg(sjjcjgMap,"1351",ying) ){
				sjjcjgMap.get("NY-vanG").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-vanG").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-vanG"));
			}
			//[573,571,548,1463165,1134687,354276]
			//耐药基因-KPC(参数代码 NY-KPC) 阳性或者灰区时,当克雷伯菌属（Klebsiella）包括肺炎克雷伯菌(参数代码 573)，产酸克雷伯菌(参数代码 571)，产气克雷伯菌(参数代码 548)，拟肺炎克雷伯菌(参数代码 1463165)，米氏克雷伯氏菌(参数代码 1134687)，阴沟肠杆菌复合群(参数代码 354276)均为阴性时，将耐药基因-KPC 放入阴性列表。其他情况，不做变化。
			if (sjjcjgMap.get("NY-KPC") != null){
				sjjcjgMap.get("NY-KPC").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-KPC").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-KPC"));
			}
		}
		//[287] -- 铜绿假单胞菌(参数代码 287)
		if (checkSjjcjg(sjjcjgMap,"287",ying)){
			//耐药基因-VIM(参数代码 NY-VIM) 阳性或者灰区时，当铜绿假单胞菌(参数代码 287)为阴性时，将耐药基因-VIM 放入阴性列表。其他情况，不做变化。
			if (sjjcjgMap.get("NY-VIM") != null){
				sjjcjgMap.get("NY-VIM").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-VIM").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-VIM"));
			}
			//[470,106654] + [287] -- 鲍曼不动杆菌(参数代码 470)，医院不动杆菌(参数代码 106654)
			//耐药基因-IMP(参数代码 NY-IMP) 阳性或者灰区时，当铜绿假单胞菌(参数代码 287)，鲍曼不动杆菌(参数代码 470)，医院不动杆菌(参数代码 106654)均为阴性时，将耐药基因-IMP 放入阴性列表。其他情况，不做变化。
			if ( sjjcjgMap.get("NY-IMP") != null && checkSjjcjg(sjjcjgMap,"470",ying) && checkSjjcjg(sjjcjgMap,"106654",ying) ){
				sjjcjgMap.get("NY-IMP").setJldm(ying.getCsdm());
				sjjcjgMap.get("NY-IMP").setJl(ying.getCsid());
				needUpdateList.add(sjjcjgMap.get("NY-IMP"));
			}
		}
		//[562,573,354276] -- 大肠埃希菌(参数代码 562)，肺炎克雷伯菌(参数代码 573)，阴沟肠杆菌复合群(参数代码 354276)
		//耐药基因-MCR(参数代码 NY-MCR) 阳性或者灰区时，当大肠埃希菌(参数代码 562)，肺炎克雷伯菌(参数代码 573)，阴沟肠杆菌复合群(参数代码 354276)均为阴性时，将耐药基因MCR 放入阴性列表。其他情况，不做变化。
		if ( sjjcjgMap.get("NY-MCR") != null && checkSjjcjg(sjjcjgMap,"562",ying) && checkSjjcjg(sjjcjgMap,"573",ying) && checkSjjcjg(sjjcjgMap,"354276",ying) ){
			sjjcjgMap.get("NY-MCR").setJldm(ying.getCsdm());
			sjjcjgMap.get("NY-MCR").setJl(ying.getCsid());
			needUpdateList.add(sjjcjgMap.get("NY-MCR"));
		}
		return needUpdateList;
	}

	/**
	 * 发送消息
	 * @return
	 */
	public void sendMessage (User user,String message){
		talkUtil.sendWorkMessage(user.getYhid(), user.getDdid(), "ResFirst文件上传错误消息",StringUtil.replaceMsg( "# ResFirst文件上传错误消息\n"+
						"请检测上传文件或联系管理员解决！\n" +
						"* 错误消息：#{0}\n" +
						"* 时间：#{1} ",StringUtil.isBlank(message)?"上传失败！":message,
				DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
	}

	/**
	 * rfs解析文件标识值
	 * @return
	 */
	public int getIdentityValue(String value){
		if ("<>".equals(value)){
			return 0;
		}
		return 1;
	}


	public String judgmentConclusion(Double sz , String cskz3,List<JcsjDto> bgjgList,BigDecimal ckbl2,String csmc,BigDecimal ckbl1) throws BusinessException {
		String[] split = cskz3.split(",");
		String jlmc = null;

		for (int i = 0; i < split.length; i++) {
			String[] strings = split[i].split(":");
			String jl;
			if (i==0){
				jl = "阴性";
			}else if (i==1){
				jl = "检测灰区";
			}else {
				jl = "阳性";
			}

			if ("-".equals(strings[0])){
				if (sz <= Double.parseDouble(strings[1])){
					jlmc = jl;
					break;
				}
			}else if ("-".equals(strings[1])){
				if (sz > Double.parseDouble(strings[0])){
					jlmc = jl;
					break;
				}
			}else{
				if (sz > Double.parseDouble(strings[0]) && sz <= Double.parseDouble(strings[1])){
					jlmc = jl;
					break;
				}
			}
		}
		if (!csmc.startsWith("NC") && !csmc.startsWith("PC")){
//			if (ckbl1.doubleValue() >= 2.0d){
//				jlmc = "阳性";
//			}else if (ckbl1.doubleValue() < 2.0d && ckbl1.doubleValue() >= 1.4d){
//				jlmc = "检测灰区";
//			}else if (ckbl1.doubleValue() < 1.4d){
//				jlmc = "阴性";
//			}
			if (ckbl2.doubleValue() != 0){
				if ("阳性".equals(jlmc)){
					if (ckbl2.doubleValue() < 1.6d){
						jlmc = "阴性";
					}
				}else if ("检测灰区".equals(jlmc)){
					if (ckbl2.doubleValue() < 1.5d){
						jlmc = "阴性";
					}
				}
			}
		}
		if (StringUtil.isNotBlank(jlmc)){
			if(null != bgjgList && bgjgList.size()>0){
				for (JcsjDto jcsjDto : bgjgList) {
					if (jlmc.equals(jcsjDto.getCsmc())){
						return  jcsjDto.getCsid();
					}
				}
			}else {
				throw new BusinessException("msg","未获取到基础数据！");
			}

		}else {
			throw new BusinessException("msg","基础数据取值范围设置错误！");
		}
		return null;
	}

	/**
	 * 检验结果导入保存
	 *
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public boolean uploadSaveReport(SjxxDto sjxxDto) throws BusinessException
	{
		// TODO Auto-generated method stub
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setScry(sjxxDto.getXgry());
		SjxxDto t_sjxxDto = new SjxxDto();
		t_sjxxDto.setSjid(sjxxDto.getSjid());
		SjbgsmDto sjbgsmDto = new SjbgsmDto();
		sjbgsmDto.setSjid(sjxxDto.getSjid());
		sjbgsmDto.setJclx(sjxxDto.getJcxmid());
		sjbgsmDto.setJczlx(sjxxDto.getJczxmid());
		List<SjbgsmDto> sjbgsmDtos=sjbgsmService.selectSjbgBySjid(sjbgsmDto);
		//若没有报告说明信息则插入一条
		if(CollectionUtils.isEmpty(sjbgsmDtos)){
			sjbgsmDto.setBgrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
			sjbgsmService.insert(sjbgsmDto);
		}
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			fjcfbDto.setYwlx(sjxxDto.getYwlx());
			boolean result = fileHandling(sjxxDto, fjcfbDto);
			if (!result)
				return false;
		}
		if (sjxxDto.getW_fjids() != null && sjxxDto.getW_fjids().size() > 0)
		{
			t_sjxxDto.setFjids(sjxxDto.getW_fjids());
			fjcfbDto.setYwlx(sjxxDto.getW_ywlx());
			boolean result = fileHandling(t_sjxxDto, fjcfbDto);
			if (!result)
				return false;
		}
		if (sjxxDto.getFjids_q() != null && sjxxDto.getFjids_q().size() > 0)
		{
			fjcfbDto.setYwlx(sjxxDto.getYwlx_q());
			boolean result = fileHandling(sjxxDto, fjcfbDto);
			if (!result)
				return false;
		}
		if (sjxxDto.getW_fjids_q() != null && sjxxDto.getW_fjids_q().size() > 0)
		{
			t_sjxxDto.setFjids(sjxxDto.getW_fjids_q());
			fjcfbDto.setYwlx(sjxxDto.getW_ywlx_q());
			boolean result = fileHandling(t_sjxxDto, fjcfbDto);
			if (!result)
				return false;
		}
		if(sjxxDto.getW_fjids_z() !=null && sjxxDto.getW_fjids_z().size()>0) {
			for (int i = 0; i < sjxxDto.getW_fjids_z().size(); i++) {
				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+sjxxDto.getW_fjids_z().get(i));
				String pathString = (String)mFile.get("wjlj");
				extractSelfFree(pathString, sjxxDto);
			}
		}
//		if(sjxxDto.getX_fjids_pdf() !=null && sjxxDto.getX_fjids_pdf().size()>0) {
//			for (int i = 0; i < sjxxDto.getX_fjids_pdf().size(); i++) {
//				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+sjxxDto.getX_fjids_pdf().get(i));
//				String pathString = (String)mFile.get("wjlj");
//				sjxxDto.setLx("XPERT");
//				extractSelfFree(pathString, sjxxDto);
//			}
//		}
//		if(sjxxDto.getY_fjids_pdf() !=null && sjxxDto.getY_fjids_pdf().size()>0) {
//			for (int i = 0; i < sjxxDto.getY_fjids_pdf().size(); i++) {
//				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+sjxxDto.getY_fjids_pdf().get(i));
//				String pathString = (String)mFile.get("wjlj");
//				sjxxDto.setLx("GXM");
//				extractSelfFree(pathString, sjxxDto);
//			}
//		}
//		if(sjxxDto.getG_fjids_pdf() !=null && sjxxDto.getG_fjids_pdf().size()>0) {
//			for (int i = 0; i < sjxxDto.getG_fjids_pdf().size(); i++) {
//				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+sjxxDto.getG_fjids_pdf().get(i));
//				String pathString = (String)mFile.get("wjlj");
//				sjxxDto.setLx("GM");
//				extractSelfFree(pathString, sjxxDto);
//			}
//		}
		// 更新本地送检信息
		sjxxDto.setSfsc("1");
		int count = dao.updateReport(sjxxDto);
		if (count == 0)
			return false;
		List<SjxxDto> sjxxDtos = dao.selectBgmbBySjid(sjxxDto.getSjid());
		if (null != sjxxDtos && sjxxDtos.size()>0 && null != sjxxDtos.get(0)){
			if (StringUtil.isNotBlank(sjxxDtos.get(0).getMxid())){
				FjcfbDto fjcfbDto1 = new FjcfbDto();
				fjcfbDto1.setYwid(sjxxDto.getSjid());
				fjcfbDto1.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto1);
				if (null != fjcfbDtos && fjcfbDtos.size()>0){
					String url = address+"/common/view/displayView?view_url=/ws/file/pdfPreview?fjid="+fjcfbDtos.get(0).getFjid();
					MxxxDto mxxxDto = new MxxxDto();
					mxxxDto.setMxid(sjxxDtos.get(0).getMxid());
					mxxxDto.setStatus("2");
					mxxxDto.setHasReport("false");
					mxxxDto.setReportUrl(url);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					mxxxDto.setStatusTime(simpleDateFormat.format(new Date()));
					boolean success = mxxxService.update(mxxxDto);
					if(success){
						mxxxService.sendMxMessage(mxxxDto.getMxid());
					}
				}
			}
		}
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
		/**
		 * 修改 只有在上传pdf的时候才会在此处发送消息，上传word的时候，改为在接收转换后的pdf之后发送消息
		 */
//		if ((sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0) || (sjxxDto.getW_fjids() != null && sjxxDto.getW_fjids().size() > 0))
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			sjxxDto.setPdflx(sjxxDto.getYwlx());
			sjxxDto.setWordlx(sjxxDto.getW_ywlx());
			return sendReportMessage(sjxxDto);
//		}else if ((sjxxDto.getFjids_q() != null && sjxxDto.getFjids_q().size() > 0) || (sjxxDto.getW_fjids_q() != null && sjxxDto.getW_fjids_q().size() > 0))
		}else if (sjxxDto.getFjids_q() != null && sjxxDto.getFjids_q().size() > 0)
		{
			sjxxDto.setPdflx(sjxxDto.getYwlx_q());
			sjxxDto.setWordlx(sjxxDto.getW_ywlx_q());
			return sendReportMessage(sjxxDto);
		}
		return true;
	}

	/**
	 * 给相应人员发送文件消息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean sendReportMessage(SjxxDto sjxxDto) throws BusinessException{
		// 如果没有设置URL，则直接返回，不发送消息。此为内容修改使用
		if (StringUtil.isBlank(menuurl))
			return true;

		// 创建请求工厂
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		// 设置连接超时时间，单位毫秒
		requestFactory.setConnectTimeout(5000); // 5秒
		// 设置读取超时时间，单位毫秒
		requestFactory.setReadTimeout(10000); // 10秒
		
		RestTemplate t_restTemplate = new RestTemplate(requestFactory);

		boolean sendFlg = false;
		DBEncrypt dbEncrypt = new DBEncrypt();
		// 发送钉钉消息
		// 查询接收人员列表
		List<SjxxDto> sjxxDtos = dao.getReceiveUserByDb(sjxxDto);
		if (sjxxDtos != null && sjxxDtos.size() > 0) {
			try {
				for (int i = 0; i < sjxxDtos.size(); i++) {
					SjxxDto t_SjxxDto = sjxxDtos.get(i);
					//处理项目名称信息和报告类型不一致的问题
					String cskz1=sjxxDto.getPdflx().substring(sjxxDto.getPdflx().length()-1,sjxxDto.getPdflx().length());
					String cskz3=sjxxDto.getPdflx().substring(0,sjxxDto.getPdflx().lastIndexOf("_"));
					
					List<JcsjDto> detectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
					for(JcsjDto jcsjDto:detectList){
						if(jcsjDto.getCskz1().equals(cskz1) && jcsjDto.getCskz3().equals(cskz3)){
							t_SjxxDto.setJcxmmc(jcsjDto.getCsmc());
							break;
						}
					}
					// 如果邮箱不为空，则发送邮件
					//消息替换map
					Map<String, Object> msgMap = new HashMap<>();
					msgMap.put("bgscsj",DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					if (t_SjxxDto.getJyjg()!=null && StringUtil.isNotBlank(t_SjxxDto.getJyjg())){
						if ("0".equals(t_SjxxDto.getJyjg())){
							t_SjxxDto.setJyjg("阴性");
						}
						if ("1".equals(t_SjxxDto.getJyjg())){
							t_SjxxDto.setJyjg("阳性");
						}
						if ("2".equals(t_SjxxDto.getJyjg())){
							t_SjxxDto.setJyjg("疑似");
						}
					}
					msgMap.put("sjxxDto",t_SjxxDto);
					if(StringUtil.isNotBlank(t_SjxxDto.getYx())){
						log.error("lwj - sendReportMessage -- 开始发送邮件。 ");
						sendFlg = true;
						List<String> yxlist = new ArrayList<>();
						if(sjxxDto.getSendFlag()!=null && "1".equals(sjxxDto.getSendFlag())){
							if(StringUtil.isNotBlank(sjxxDto.getSendmail())){
								yxlist = StringUtil.splitMsg(yxlist, sjxxDto.getSendmail(), "，|,");
							}
						}else{
							yxlist = StringUtil.splitMsg(yxlist, t_SjxxDto.getYx(), "，|,");
						}
						Map<String, Object> emailMap;
						if ("4".equals(t_SjxxDto.getHblx())) {
							emailMap = emailUtil.sendEmailReturnMap(yxlist, xxglService.getReplaceMsg("ICOMM_SJ00011",msgMap),
									xxglService.getMsg("ICOMM_SJ00012", t_SjxxDto.getHzxm(), t_SjxxDto.getYbbh()
											, t_SjxxDto.getYblxmc(), t_SjxxDto.getJcxmmc(),t_SjxxDto.getHospitalname(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
									dbEncrypt.dCode(t_SjxxDto.getWordwjlj()), t_SjxxDto.getWordwjm(),null);
						} else {
							emailMap = emailUtil.sendEmailReturnMap(yxlist, xxglService.getReplaceMsg("ICOMM_SJ00011",msgMap),
									xxglService.getMsg("ICOMM_SJ00012", t_SjxxDto.getHzxm(), t_SjxxDto.getYbbh()
											, t_SjxxDto.getYblxmc(), t_SjxxDto.getJcxmmc(),t_SjxxDto.getHospitalname(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
									dbEncrypt.dCode(t_SjxxDto.getWjlj()), t_SjxxDto.getWjm(),null);
						}

						if (emailMap != null && emailMap.size() > 0) {
							if (emailMap.get("status") != null && "fail".equals(emailMap.get("status").toString())) {
								Object failEmails = emailMap.get("failEmails");
								if (failEmails!=null){
									List<String> list = (List<String>) failEmails;
									if (list.size()>0){
										String failEmail = t_SjxxDto.getHzxm() + "_" + t_SjxxDto.getYbbh() + "，以下邮箱发送失败！\n\n";
										for (String msg : list) {
											failEmail += ("\n\n" + msg);
										}
										List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.EMAIL_SEND_FAIL.getCode());
										if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
											for (DdxxglDto ddxxglDto : ddxxglDtolist) {
												if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
													talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), t_SjxxDto.getHzxm() + "_" + t_SjxxDto.getYbbh() + "，以下邮箱发送失败！", failEmail);
												}
											}
										}
									}
								}
							}
						}
						log.error("lwj - sendReportMessage -- 结束发送邮件。 ");
					}
					// 如果微信不为空，则微信通知
					if (StringUtil.isNotBlank(t_SjxxDto.getHbcskz1())) {
						log.error("lwj - sendReportMessage -- 开始发送微信。 ");
						String downloadurl = menuurl + "/wechat/file/downloadFile?fjid=";
						// 如果为传递word版报告
						if ("4".equals(t_SjxxDto.getHblx())) {
							String sign = URLEncoder.encode(commonService.getSign(sjxxDtos.get(i).getWordfjid()), "UTF-8");
							downloadurl = downloadurl + sjxxDtos.get(i).getWordfjid() + "&sign=" + sign;
							if (StringUtil.isBlank(sjxxDtos.get(i).getWordfjid()))
								continue;
						} else {
							String sign = URLEncoder.encode(commonService.getSign(sjxxDtos.get(i).getFjid()), "UTF-8");
							downloadurl = downloadurl + sjxxDtos.get(i).getFjid() + "&sign=" + sign;
							if (StringUtil.isBlank(sjxxDtos.get(i).getFjid()))
								continue;
						}
						sendFlg = true;
						// 组装微信消息
						MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
						paramMap.add("wxid", null);
						paramMap.add("wbcxdm", null);
						paramMap.add("ybbh", t_SjxxDto.getYbbh());
						paramMap.add("downloadurl", downloadurl);
						paramMap.add("bz", "患者姓名：" + t_SjxxDto.getHzxm());
						// 让服务器发送信息到相应的微信里
						String[] arrWxid = t_SjxxDto.getHbcskz1().split("，|,");
						for (int j = 0; j < arrWxid.length; j++) {
							paramMap.set("wxid", arrWxid[j]);
							WxyhDto wxyhDto = new WxyhDto();
							wxyhDto.setWxid(arrWxid[j]);
							WxyhDto wxyhByWxid = wxyhDao.getWxyhByWxid(wxyhDto);
							if (wxyhByWxid != null && StringUtil.isNotBlank(wxyhByWxid.getWbcxdm())){
								paramMap.set("wbcxdm", wxyhByWxid.getWbcxdm());
							}
							try {
								String status = t_restTemplate.postForObject(menuurl + "/wechat/sendReportTouser", paramMap, String.class);
								log.error("lwj - sendReportMessage -- 发送微信消息，发送对象："+arrWxid[j]+",发送状态："+status);
							} catch (RestClientException e) {
								log.error("lwj - sendReportMessage -- 发送微信消息失败，发送对象："+arrWxid[j] + e.getMessage());
							}
						}
						log.error("lwj - sendReportMessage -- 结束发送微信。 ");
					}
					// 如果合作伙伴类型为1（直销）的情况，则为公司员工，则通过钉钉进行通知
					if ("1".equals(t_SjxxDto.getHblx()) && StringUtil.isNotBlank(t_SjxxDto.getDdid())) {
						if (StringUtil.isBlank(t_SjxxDto.getFjid()))
							continue;
						log.error("lwj - sendReportMessage -- 开始发送钉钉。 ");
						String sign = URLEncoder.encode(commonService.getSign(sjxxDtos.get(i).getFjid()),
								"UTF-8");
						String download = menuurl + "/wechat/file/downloadFile?fjid="
								+ sjxxDtos.get(i).getFjid() + "&sign=" + sign;
						String viewurl = menuurl + "/wechat/file/pdfPreview?fjid=" + t_SjxxDto.getFjid()
								+ "&sign=" + sign;
						List<BtnJsonList> btnJsonLists = new ArrayList<>();
						BtnJsonList btnJsonList = new BtnJsonList();
						btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00004"));
						btnJsonList.setActionUrl(viewurl);
						btnJsonLists.add(btnJsonList);
						BtnJsonList t_btnJsonList = new BtnJsonList();
						t_btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00005"));
						t_btnJsonList.setActionUrl(download);
						btnJsonLists.add(t_btnJsonList);
						sendFlg = true;
						try {
							talkUtil.sendCardMessage(t_SjxxDto.getYhm(), t_SjxxDto.getDdid(),
									xxglService.getReplaceMsg("ICOMM_SJ00003",msgMap),
									xxglService.getMsg("ICOMM_SJ00006", t_SjxxDto.getYbbh(), t_SjxxDto.getHzxm(),
											t_SjxxDto.getYblxmc(), t_SjxxDto.getNbbm(), t_SjxxDto.getJcxmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
									btnJsonLists, "1");
							String mediaId = talkUtil.uploadFileToDingTalk(t_SjxxDto.getYhm(), dbEncrypt.dCode(t_SjxxDto.getWjlj()), t_SjxxDto.getWjm());
							talkUtil.sendFileMessage(t_SjxxDto.getYhm(),t_SjxxDto.getDdid(),mediaId);
						} catch (RestClientException e) {
							log.error("lwj - sendReportMessage -- 发送钉钉消息失败，发送对象："+t_SjxxDto.getDdid() + e.getMessage());
						}
						log.error("lwj - sendReportMessage -- 结束发送钉钉。 ");
					}
				}
				if (!sendFlg) {
					log.error("lwj - sendReportMessage -- 开始查找 伙伴为无的钉钉ID。 ");
					// 查询伙伴为'无'的钉钉ID
					List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getXtyhByHbmc();
					if (sjhbxxDtos != null) {
						for (int i = 0; i < sjhbxxDtos.size(); i++) {
							Map<String, Object> msgMap = new HashMap<>();
							msgMap.put("bgscsj",DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
							if (sjxxDtos.get(0).getJyjg()!=null && StringUtil.isNotBlank(sjxxDtos.get(0).getJyjg())){
								if ("0".equals(sjxxDtos.get(0).getJyjg())){
									msgMap.put("jyjg","阴性");
								}
								if ("1".equals(sjxxDtos.get(0).getJyjg())){
									msgMap.put("jyjg","阳性");
								}
								if ("2".equals(sjxxDtos.get(0).getJyjg())){
									msgMap.put("jyjg","疑似");
								}
							}
							msgMap.put("sjxxDto",sjxxDtos.get(0));
							String sign = URLEncoder.encode(commonService.getSign(sjxxDtos.get(0).getFjid()), "UTF-8");
							String viewurl = menuurl + "/wechat/file/pdfPreview?fjid=" + sjxxDtos.get(0).getFjid()
									+ "&sign=" + sign;
							List<BtnJsonList> btnJsonLists = new ArrayList<>();
							BtnJsonList btnJsonList = new BtnJsonList();
							btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00004"));
							btnJsonList.setActionUrl(viewurl);
							btnJsonLists.add(btnJsonList);
							try {
								talkUtil.sendCardMessage(sjhbxxDtos.get(i).getYhm(), sjhbxxDtos.get(i).getDdid(),
										xxglService.getReplaceMsg("ICOMM_SJ00003",msgMap),
										xxglService.getMsg("ICOMM_SJ00006", sjxxDtos.get(0).getYbbh(),
												sjxxDtos.get(0).getHzxm(), sjxxDtos.get(0).getYblxmc(),
												sjxxDtos.get(0).getNbbm(), sjxxDtos.get(0).getJcxmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
										btnJsonLists, "1");

								String mediaId = talkUtil.uploadFileToDingTalk(sjhbxxDtos.get(i).getYhm(), dbEncrypt.dCode(sjxxDtos.get(0).getWjlj()), sjxxDtos.get(0).getWjm());
								talkUtil.sendFileMessage(sjhbxxDtos.get(i).getYhm(),sjhbxxDtos.get(i).getDdid(),mediaId);
							} catch (RestClientException e) {
								log.error("lwj - sendReportMessage -- 发送钉钉消息失败，发送对象："+sjhbxxDtos.get(i).getDdid() + e.getMessage());
							}
						}
					}
					log.error("lwj - sendReportMessage -- 结束查找 伙伴为无的钉钉ID。 ");
				}
				//如果报告是ONCO的，同步发送消息至韩序钉钉055666321229766
//				if (sjxxDto.getWordlx().contains("ONCO") || sjxxDto.getPdflx().contains("ONCO")){
//					log.error("lwj - sendReportMessage -- 开始发送钉钉。 055666321229766");
//					//组装msgMap
//					Map<String, Object> msgMap = new HashMap<>();
//					msgMap.put("bgscsj",DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
//					if (sjxxDtos.get(0).getJyjg()!=null && StringUtil.isNotBlank(sjxxDtos.get(0).getJyjg())){
//						if ("0".equals(sjxxDtos.get(0).getJyjg())){
//							msgMap.put("jyjg","阴性");
//						}
//						if ("1".equals(sjxxDtos.get(0).getJyjg())){
//							msgMap.put("jyjg","阳性");
//						}
//						if ("2".equals(sjxxDtos.get(0).getJyjg())){
//							msgMap.put("jyjg","疑似");
//						}
//					}
//					msgMap.put("sjxxDto",sjxxDtos.get(0));
//					String sign = URLEncoder.encode(commonService.getSign(sjxxDtos.get(0).getFjid()), "UTF-8");
//					String download = menuurl + "/wechat/file/downloadFile?fjid=" + sjxxDtos.get(0).getFjid() + "&sign=" + sign;
//					String viewurl = menuurl + "/wechat/file/pdfPreview?fjid=" + sjxxDtos.get(0).getFjid() + "&sign=" + sign;
//					List<BtnJsonList> btnJsonLists = new ArrayList<BtnJsonList>();
//					BtnJsonList btnJsonList = new BtnJsonList();
//					btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00004"));
//					btnJsonList.setActionUrl(viewurl);
//					btnJsonLists.add(btnJsonList);
//					BtnJsonList t_btnJsonList = new BtnJsonList();
//					t_btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00005"));
//					t_btnJsonList.setActionUrl(download);
//					btnJsonLists.add(t_btnJsonList);
//					talkUtil.sendCardMessage("null", "055666321229766",
//							xxglService.getReplaceMsg("ICOMM_SJ00003",msgMap),
//							xxglService.getMsg("ICOMM_SJ00006", sjxxDtos.get(0).getYbbh(),
//									sjxxDtos.get(0).getHzxm(), sjxxDtos.get(0).getYblxmc(),
//									sjxxDtos.get(0).getNbbm(), sjxxDtos.get(0).getJcxmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
//							btnJsonLists, "1");
//					String downloadurl = menuurl + "/wechat/file/sendDingTalkFile?fjid=" + sjxxDtos.get(0).getFjid() + "&ddid=055666321229766"; t_restTemplate.getForObject(downloadurl, String.class, "");
//					log.error("lwj - sendReportMessage -- 结束发送钉钉。 055666321229766");
//				}
			} catch (UnsupportedEncodingException e) {
				log.error("lwj - sendReportMessage -- UnsupportedEncodingException --- " + e.toString());
//				throw new BusinessException("msg", e.toString());
			}catch (Exception e) {
				log.error("lwj - sendReportMessage -- Exception --- " + e.toString());
//				throw new BusinessException("msg", e.toString());
			}
			return true;
		}
		return false;
	}


	/**
	 * 给相应人员发送邮箱消息
	 *
	 * @param
	 * @return
	 */
	@Override
	public boolean sendReportOnlyToYx(SjxxDto sjxxDto) throws BusinessException{
		// 如果没有设置URL，则直接返回，不发送消息。此为内容修改使用
		if (StringUtil.isBlank(menuurl))
			return true;
		DBEncrypt dbEncrypt = new DBEncrypt();
		// 发送钉钉消息
		// 查询接收人员列表
		List<SjxxDto> sjxxDtos = dao.getReceiveUserByDb(sjxxDto);
		if (sjxxDtos != null && sjxxDtos.size() > 0) {
			try {
				for (int i = 0; i < sjxxDtos.size(); i++) {
					SjxxDto t_SjxxDto = sjxxDtos.get(i);
					//处理项目名称信息和报告类型不一致的问题
					String cskz1=sjxxDto.getPdflx().substring(sjxxDto.getPdflx().length()-1,sjxxDto.getPdflx().length());
					String cskz3=sjxxDto.getPdflx().substring(0,sjxxDto.getPdflx().lastIndexOf("_"));
					
					List<JcsjDto> detectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
					for(JcsjDto jcsjDto:detectList){
						if(jcsjDto.getCskz1().equals(cskz1) && jcsjDto.getCskz3().equals(cskz3)){
							t_SjxxDto.setJcxmmc(jcsjDto.getCsmc());
							break;
						}
					}
					// 如果邮箱不为空，则发送邮件
					//消息替换map
					Map<String, Object> msgMap = new HashMap<>();
					msgMap.put("bgscsj",DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					if (t_SjxxDto.getJyjg()!=null && StringUtil.isNotBlank(t_SjxxDto.getJyjg())){
						if ("0".equals(t_SjxxDto.getJyjg())){
							t_SjxxDto.setJyjg("阴性");
						}
						if ("1".equals(t_SjxxDto.getJyjg())){
							t_SjxxDto.setJyjg("阳性");
						}
						if ("2".equals(t_SjxxDto.getJyjg())){
							t_SjxxDto.setJyjg("疑似");
						}
					}
					msgMap.put("sjxxDto",t_SjxxDto);
					if(StringUtil.isNotBlank(t_SjxxDto.getYx2())){
						log.error("lwj - sendReportMessage -- 开始发送邮件。 ");
						
						List<String> yxlist = new ArrayList<>();
						if(sjxxDto.getSendFlag()!=null && "1".equals(sjxxDto.getSendFlag())){
							if(StringUtil.isNotBlank(sjxxDto.getSendmail())){
								yxlist = StringUtil.splitMsg(yxlist, sjxxDto.getSendmail(), "，|,");
							}
						}else{
							yxlist = StringUtil.splitMsg(yxlist, t_SjxxDto.getYx2(), "，|,");
						}
						Map<String, Object> emailMap;
						if ("4".equals(t_SjxxDto.getHblx())) {
							emailMap = emailUtil.sendEmailReturnMap(yxlist, xxglService.getReplaceMsg("ICOMM_SJ00011", msgMap),
									xxglService.getMsg("ICOMM_SJ00012", t_SjxxDto.getHzxm(), t_SjxxDto.getYbbh()
											, t_SjxxDto.getYblxmc(), t_SjxxDto.getJcxmmc(), t_SjxxDto.getHospitalname(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
									dbEncrypt.dCode(t_SjxxDto.getWordwjlj()), t_SjxxDto.getWordwjm(), null);
						} else {
							emailMap = emailUtil.sendEmailReturnMap(yxlist, xxglService.getReplaceMsg("ICOMM_SJ00011",msgMap),
									xxglService.getMsg("ICOMM_SJ00012", t_SjxxDto.getHzxm(), t_SjxxDto.getYbbh()
											, t_SjxxDto.getYblxmc(), t_SjxxDto.getJcxmmc(),t_SjxxDto.getHospitalname(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
									dbEncrypt.dCode(t_SjxxDto.getWjlj()), t_SjxxDto.getWjm(),null);
						}
						if (emailMap != null && emailMap.size() > 0) {
							if (emailMap.get("status") != null && "fail".equals(emailMap.get("status").toString())) {
								Object failEmails = emailMap.get("failEmails");
								if (failEmails!=null){
									List<String> list = (List<String>) failEmails;
									if (list.size()>0){
										String failEmail = t_SjxxDto.getHzxm() + "_" + t_SjxxDto.getYbbh() + "，以下邮箱发送失败！\n\n";
										for (String msg : list) {
											failEmail += ("\n\n" + msg);
										}
										List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.EMAIL_SEND_FAIL.getCode());
										if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
											for (DdxxglDto ddxxglDto : ddxxglDtolist) {
												if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
													talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), t_SjxxDto.getHzxm() + "_" + t_SjxxDto.getYbbh() + "，以下邮箱发送失败！", failEmail);
												}
											}
										}
									}
								}
							}
						}
						log.error("lwj - sendReportMessage -- 结束发送邮件。 ");
					}
				}
			} catch (UnsupportedEncodingException e) {
				log.error("lwj - sendReportMessage -- UnsupportedEncodingException --- " + e.toString());
//				throw new BusinessException("msg", e.toString());
			}catch (Exception e) {
				log.error("lwj - sendReportMessage -- Exception --- " + e.toString());
//				throw new BusinessException("msg", e.toString());
			}
			return true;
		}
		return false;
	}

	/**
	 * 查看附件
	 */
	@Override
	public List<FjcfbDto> selectFjByWjid(String sjid)
	{
		// TODO Auto-generated method stub
		List<FjcfbDto> fjcfbDtos = dao.selectFjByWjid(sjid);
		for (int i = 0; i < fjcfbDtos.size(); i++)
		{
			String wjmhz = fjcfbDtos.get(i).getWjm().substring(fjcfbDtos.get(i).getWjm().lastIndexOf(".") + 1);
			fjcfbDtos.get(i).setWjmhz(wjmhz);
		}
		return fjcfbDtos;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean AddSjxx(SjxxDto sjxxDto,SjkzxxDto sjkzxxDto)
	{
		RestTemplate t_restTemplate = new RestTemplate();
		if(StringUtil.isNotBlank(sjxxDto.getKyxm())){
			List<JcsjDto> kyxmList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RESEARCH_PROJECT.getCode());
			for (JcsjDto kyxm : kyxmList) {
				if (sjxxDto.getKyxm().equals(kyxm.getCsid())){
					sjxxDto.setSfsf(kyxm.getCskz2());
					List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
					if (jcxmlist!=null&&!CollectionUtils.isEmpty(jcxmlist)){
						for(SjjcxmDto dto:jcxmlist){
							dto.setSfsf(kyxm.getCskz2());
						}
					}
					sjxxDto.setJcxm(JSON.toJSONString(jcxmlist));
					break;
				}
			}
		}
		if(StringUtil.isNotBlank(sjxxDto.getSjqf())){
			List<JcsjDto> sjqfList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			for (JcsjDto jcsjDto : sjqfList) {
				if (sjxxDto.getSjqf().equals(jcsjDto.getCsid())&&StringUtil.isNotBlank(jcsjDto.getCskz2())){
					sjxxDto.setSfsf(jcsjDto.getCskz2());
					List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
					if (jcxmlist!=null&&!CollectionUtils.isEmpty(jcxmlist)){
						for(SjjcxmDto dto:jcxmlist){
							dto.setSfsf(jcsjDto.getCskz2());
						}
					}
					sjxxDto.setJcxm(JSON.toJSONString(jcxmlist));
					break;
				}
			}
		}
		// 获取默认金额
		if (sjxxDto.getFkbj()==null||"0".equals(sjxxDto.getFkbj()))
		{
			SjhbxxDto sjhbxxDto = new SjhbxxDto();
			List<SjjcxmDto> sjxms = new ArrayList<>();
			List<String> jcxmids = sjxxDto.getJcxmids();
			for (int i = 0; i < jcxmids.size(); i++)
			{
				SjjcxmDto jcxmDto = new SjjcxmDto();
				jcxmDto.setJcxmid(jcxmids.get(i));
				sjxms.add(jcxmDto);
			}
			sjhbxxDto.setSjxms(sjxms);
			sjhbxxDto.setHbmc(sjxxDto.getDb());
			SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
			if (re_sjhbxxDto != null)
			{
				if("0".equals(sjxxDto.getSfsf())) {
					sjxxDto.setFkje("0");
				}else if("1".equals(sjxxDto.getSfsf())) {
					sjxxDto.setFkje(re_sjhbxxDto.getSfbz());
				}else if("2".equals(sjxxDto.getSfsf())||"3".equals(sjxxDto.getSfsf())) {
					String fkje=re_sjhbxxDto.getSfbz();
					if(fkje!=null) {
						fkje=(Double.parseDouble(fkje) / 2)+"";
						sjxxDto.setFkje(fkje);
					}
				}else {
					sjxxDto.setFkje(re_sjhbxxDto.getSfbz());
				}
			}
		}
		// 通过合作伙伴判断是否盖章
		SjhbxxDto sjhbxxDto = new SjhbxxDto();
		sjhbxxDto.setHbmc(sjxxDto.getDb());
		sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
		if (sjhbxxDto != null)
		{
			if (StringUtil.isNotBlank(sjhbxxDto.getGzcskz1()))
			{
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb(BasicDataTypeEnum.THIRD_SJXXKZ.getCode());
				List<JcsjDto> jcsjList = jcsjService.getCskz1NotNull(jcsjDto);
				if (jcsjList != null && jcsjList.size() > 0)
				{
					sjxxDto.setCskz3(jcsjList.get(0).getCsid());
				}
			}
		}
		boolean result = insertDto(sjxxDto);
		if (!result)
		{
			return false;
		}
		result = insertAll(sjxxDto,sjxxDto.getLrry());
		if (!result)
		{
			return false;
		}
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADD.getCode() + JSONObject.toJSONString(sjxxDto));
		sjkzxxDto.setSjid(sjxxDto.getSjid());
		result = sjkzxxService.insertDto(sjkzxxDto);
		if (!result)
		{
			return false;
		}
		List<JcsjDto> yblxList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> sjqfList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		String yblxdm="";
		String jcdwmc="";
		String sjqfdm="";
		if(yblxList!=null&&yblxList.size()>0){
			for(JcsjDto jcsjDto:yblxList){
				if(sjxxDto.getYblx().equals(jcsjDto.getCsid())){
					yblxdm=jcsjDto.getCsdm();
					break;
				}
			}
			//判断如果样本类型选择其他时，并且其他标本类型内容为"全血"或者"血浆"时，标本类型传"B"
			// 20240904 "其它"样本类型的csdm由XXX改为G
			if("XXX".equals(yblxdm) || "G".equals(yblxdm)){
				if(StringUtil.isNotBlank(sjxxDto.getYblxmc())){
					if("全血".equals(sjxxDto.getYblxmc())||"血浆".equals(sjxxDto.getYblxmc())){
						yblxdm="B";
					}
				}
			}
		}
		if(jcdwList!=null&&jcdwList.size()>0){
			for(JcsjDto jcsjDto:jcdwList){
				if(sjxxDto.getJcdw().equals(jcsjDto.getCsid())){
					jcdwmc=jcsjDto.getCsmc();
					break;
				}
			}
		}
		if(sjqfList!=null&&sjqfList.size()>0){
			for(JcsjDto jcsjDto:sjqfList){
				if(sjxxDto.getSjqf().equals(jcsjDto.getCsid())){
					sjqfdm=jcsjDto.getCsdm();
					break;
				}
			}
		}
		sjxxDto.setYblxdm(yblxdm);
		sjxxDto.setJcdwmc(jcdwmc);
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
			//ids用于排除不需要的复检ID
			sjxxDto.setIds(ids);
		}
		List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto,null,DetectionTypeEnum.DETECT_SJ.getCode());
		
		if (!CollectionUtils.isEmpty(insertInfo)){
			//更新项目实验数据和送检实验数据
			addOrUpdateSyData(insertInfo,sjxxDto,sjxxDto.getLrry());
			//因为修改送检信息的话，可能因为修改了标本类型或者其他造成对所有的复测也有影响，则需要重建所有的sjsygl的数据
			/*resultFjDtos = fjsqService.getNotSyFjxxBySjxx(fjsqDto_t);
			for(FjsqDto res_FjsqDto:resultFjDtos) {
				res_FjsqDto.setXgry(sjxxDto.getLrry());
				fjsqService.addOrUpdateSyData(res_FjsqDto, sjxxDto);
			}*/
			SjsyglDto sjsyglDto_t=new SjsyglDto();
			sjsyglDto_t.setSjid(sjxxDto.getSjid());
			//更新所有送检实验的数据
			//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
			List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
			XmsyglDto xmsyglDto_t = new XmsyglDto();
			xmsyglDto_t.setYwid(sjxxDto.getSjid());
			List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
		}
		if (sjxxDto.getIds() != null && sjxxDto.getIds().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getIds().size(); i++)
			{
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getIds().get(i), sjxxDto.getSjid());
				if (!saveFile)
					return false;
			}
		}
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getFjids().size(); i++)
			{
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i), sjxxDto.getSjid());
				if (!saveFile)
					return false;
			}
			// 附件排序
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(sjxxDto.getYwlx());
			result = fjcfbService.fileSort(fjcfbDto);
			if (!result)
				return false;
		}
		//如果设置了URL，则拷贝文件到微信服务器，发送钉钉消息
		if(StringUtil.isNotBlank(menuurl)) {
			// 拷贝文件到微信服务器
			try
			{
				DBEncrypt dbEncrypt = new DBEncrypt();

				if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
				{
					for (int i = 0; i < sjxxDto.getFjids().size(); i++)
					{
						FjcfbModel t_fjcfbModel = new FjcfbModel();
						t_fjcfbModel.setFjid(sjxxDto.getFjids().get(i));
						FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);

						if (fjcfbModel != null)
						{
							String wjlj = fjcfbModel.getWjlj();
							String pathString = dbEncrypt.dCode(wjlj);
							File file = new File(pathString);
							// 文件不存在不做任何操作
							if (!file.exists())
								return true;

							byte[] bytesArray = new byte[(int) file.length()];

							FileInputStream fis = new FileInputStream(file);
							fis.read(bytesArray); // read file into bytes[]
							fis.close();
							// 需要给文件的名称
							ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray)
							{
								@Override
								public String getFilename()
								{
									return file.getName();
								}
							};
							MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
							paramMap.add("file", contentsAsResource);
							paramMap.add("fjcfbModel", fjcfbModel);
							// 发送文件到服务器
							String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReport", paramMap, String.class);

							if ("OK".equals(reString))
							{
								// 更新文件的转换标记为true
								result = fjcfbService.updateZhbj(fjcfbModel);
								if (!result)
									return false;
							}
						}
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			// 发送钉钉消息
			String url = menuurl + "/common/view/displayView?view_url=/common/view/inspectionView?ybbh=" + sjxxDto.getYbbh() + "&hzxm=" + sjxxDto.getHzxm();
			sjxxDto = dao.getDto(sjxxDto);
			if("1".equals(sjxxDto.getYyxxCskz1())) {
				sjxxDto.setHospitalname(sjxxDto.getHospitalname()+"-"+sjxxDto.getSjdwmc());
			}
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm("INSPECTION_TYPE");
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
			if (ddxxglDtos != null && ddxxglDtos.size() > 0){
				String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
				String ICOMM_SJ00001 = xxglService.getMsg("ICOMM_SJ00001");
				for (int i = 0; i < ddxxglDtos.size(); i++){
					if (StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid())){
						talkUtil.sendWorkMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), ICOMM_SJ00002,
								StringUtil.replaceMsg(ICOMM_SJ00001, sjxxDto.getYbbh(),sjxxDto.getDb(), sjxxDto.getHospitalname(), sjxxDto.getYblxmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), url);
					}
				}
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertAll(SjxxDto sjxxDto,String yhid)
	{
		// TODO Auto-generated method stub
		sjjcxmService.insertSjjcxm(sjxxDto,yhid);
		sjgzbyService.insertSjgzby(sjxxDto);
		sjqqjcService.insertBySjxx(sjxxDto);
		sjlczzService.insertSjlczz(sjxxDto);
		sjybztService.insertYbzt(sjxxDto);
		return true;
	}

	/**
	 * 根据送检标本编号查询送检信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getDto(SjxxDto sjxxDto)
	{
		if (StringUtil.isBlank(sjxxDto.getSjid()) && StringUtil.isBlank(sjxxDto.getYbbh())&&StringUtil.isBlank(sjxxDto.getNbbm()))
			return null;
		// 获取送检信息
		SjxxDto resSjxxDto = dao.getDto(sjxxDto);
//		SjxxDto resSjxxDto = dao.getSjxxDto(sjxxDto);
		if(resSjxxDto == null)
			return null;

		//设置分子核型结果
		if (StringUtil.isNotBlank(resSjxxDto.getCskz1())){
			resSjxxDto.setFzhxjg(resSjxxDto.getCskz1());
		}
		if (resSjxxDto.getNldw() == null || resSjxxDto.getNldw() == "")
		{
			resSjxxDto.setNldw("岁");
		}
		if (resSjxxDto.getQtks() != null && resSjxxDto.getQtks() != "")
		{
			resSjxxDto.setKsmc(resSjxxDto.getKsmc() + "-" + resSjxxDto.getQtks());
		}
		if (resSjxxDto.getYyxxCskz1() != null && resSjxxDto.getYyxxCskz1() != "" && "1".equals(resSjxxDto.getYyxxCskz1()))
		{
			resSjxxDto.setYymc(resSjxxDto.getHospitalname() + "-" + resSjxxDto.getYymc());
		}
		Map<String, String> sjxxMap = dao.getAllSjxxOther(resSjxxDto.getSjid());
		if (sjxxMap.get("jcxmmc") != null && StringUtil.isBlank(resSjxxDto.getJcxmmc()))
		{
			resSjxxDto.setJcxmmc(sjxxMap.get("jcxmmc"));
		}
		if (sjxxMap.get("cskz1") != null)
		{
			resSjxxDto.setCskz1(sjxxMap.get("cskz1"));
		}
		if (sjxxMap.get("cskz3") != null)
		{
			resSjxxDto.setCskz3(sjxxMap.get("cskz3"));
		}
		if (sjxxMap.get("cskz3s") != null)
		{
			resSjxxDto.setCskz3s(sjxxMap.get("cskz3s"));
		}
		if (sjxxMap.get("jcxm") != null)
		{
			resSjxxDto.setJcxm(sjxxMap.get("jcxm"));
		}
		if (sjxxMap.get("lczz") != null)
		{
			resSjxxDto.setLczzmc(sjxxMap.get("lczz"));
		}
		if (sjxxMap.get("gzbymc") != null)
		{
			resSjxxDto.setGzbymc(sjxxMap.get("gzbymc"));
		}
		if (sjxxMap.get("gzbymc") != null)
		{
			resSjxxDto.setGzbymc(sjxxMap.get("gzbymc"));
		}
		if (sjxxMap.get("qqjcmc") != null)
		{
			resSjxxDto.setQqjcmc(sjxxMap.get("qqjcmc"));
		}
		if (sjxxMap.get("ybztmc") != null)
		{
			resSjxxDto.setYbztmc(sjxxMap.get("ybztmc"));
		}
		return resSjxxDto;
	}


	@Override
	public List<SjxxDto> getDtoAllList(SjxxDto sjxxDto){
		List<SjxxDto> dtoList = dao.getDtoList(sjxxDto);
		if(CollectionUtils.isEmpty(dtoList)) {
			return null;
		}
		for (SjxxDto dto : dtoList) {
			if (StringUtil.isNotBlank(dto.getCskz1())){
				dto.setFzhxjg(dto.getCskz1());
			}
			if (dto.getNldw() == null || dto.getNldw() == "") {
				dto.setNldw("岁");
			}
			if (dto.getQtks() != null && dto.getQtks() != "") {
				dto.setKsmc(dto.getKsmc() + "-" + dto.getQtks());
			}
			if (dto.getYyxxCskz1() != null && dto.getYyxxCskz1() != "" && "1".equals(dto.getYyxxCskz1())) {
				dto.setYymc(dto.getHospitalname() + "-" + dto.getYymc());
			}
			Map<String, String> sjxxMap = dao.getAllSjxxOther(dto.getSjid());
			if (sjxxMap.get("jcxmmc") != null && StringUtil.isBlank(dto.getJcxmmc())) {
				dto.setJcxmmc(sjxxMap.get("jcxmmc"));
			}
			if (sjxxMap.get("cskz1") != null) {
				dto.setCskz1(sjxxMap.get("cskz1"));
			}
			if (sjxxMap.get("cskz3") != null) {
				dto.setCskz3(sjxxMap.get("cskz3"));
			}
			if (sjxxMap.get("jcxm") != null) {
				dto.setJcxm(sjxxMap.get("jcxm"));
			}
			if (sjxxMap.get("lczz") != null) {
				dto.setLczzmc(sjxxMap.get("lczz"));
			}
			if (sjxxMap.get("gzbymc") != null) {
				dto.setGzbymc(sjxxMap.get("gzbymc"));
			}
			if (sjxxMap.get("gzbymc") != null) {
				dto.setGzbymc(sjxxMap.get("gzbymc"));
			}
			if (sjxxMap.get("qqjcmc") != null) {
				dto.setQqjcmc(sjxxMap.get("qqjcmc"));
			}
			if (sjxxMap.get("ybztmc") != null) {
				dto.setYbztmc(sjxxMap.get("ybztmc"));
			}
			if(StringUtil.isNotBlank(dto.getQtks())){
				dto.setKsmc(dto.getQtks());
			}
		}
		return dtoList;
	}

	/**
	 * 根据搜索条件获取导出条数
	 *
	 * @param sjxxDto
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(SjxxDto sjxxDto,Map<String, Object> params) {
		String jsid = (String) params.get("jsid");
		String yhid = (String) params.get("yhid");
		List<Map<String,String>> jcdwList=dao.getJsjcdwByjsid(jsid);
		int count=0;
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				//判断伙伴权限
				List<String> hbqxList = hbqxService.getHbidByYhid(yhid);
				if(hbqxList!=null && hbqxList.size()>0) {
					List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
					if(hbmcList!=null  && hbmcList.size()>0) {
						sjxxDto.setSjhbs(hbmcList);
					}
				}
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					sjxxDto.setJcdwxz(strList);
					count=dao.getCountForSearchExp(sjxxDto);
				}
			}else {
				count=dao.getCountForSearchExp(sjxxDto);
			}
		}else {
			count=dao.getCountForSearchExp(sjxxDto);
		}

		return count;
	}

	/**
	 * 根据搜索条件分页获取导出信息
	 *
	 * @param params
	 * @return
	 */
	public List<SjxxDto> getListForSearchExp(Map<String, Object> params)
	{
		SjxxDto sjxxDto = (SjxxDto) params.get("entryData");
		queryJoinFlagExport(params, sjxxDto);
		//查询角色检测单位限定
		List<SjxxDto> list;
		@SuppressWarnings("unchecked")
		Map<String, String> otherPars = (Map<String, String>) params.get("otherPars");
		String jsid = otherPars.get("jsid");
		String yhid = otherPars.get("yhid");
		List<Map<String,String>> jcdwList=dao.getJsjcdwByjsid(jsid);
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				//判断伙伴权限
				List<String> hbqxList = hbqxService.getHbidByYhid(yhid);
				if(hbqxList!=null && hbqxList.size()>0) {
					List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
					if(hbmcList!=null  && hbmcList.size()>0) {
						sjxxDto.setSjhbs(hbmcList);
					}
				}
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					sjxxDto.setJcdwxz(strList);
					list=dao.getListForSearchExp(sjxxDto);
				}else {
					list= new ArrayList<>();
				}
			}else {
				list = dao.getListForSearchExp(sjxxDto);
			}
		}else {
			list = dao.getListForSearchExp(sjxxDto);
		}

		return list;
	}
	/**
	 * 选中导出
	 *
	 * @param params
	 * @return
	 */
	public List<SjxxDto> getListForSelectExp(Map<String, Object> params)
	{
		SjxxDto sjxxDto = (SjxxDto) params.get("entryData");
		queryJoinFlagExport(params, sjxxDto);
		return dao.getListForSelectExp(sjxxDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, SjxxDto sjxxDto)
	{
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for (DcszDto dcszDto : choseList)
		{
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;
			if(dcszDto.getDczd().equalsIgnoreCase("HOSPITALNAME")) {
				sjxxDto.setHospitalName_flg("Y");
			}else if (dcszDto.getDczd().equalsIgnoreCase("YBLXDM"))
			{
				sjxxDto.setYblx_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("YBLXMC"))
			{
				sjxxDto.setYblx_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("DWMC"))
			{
				sjxxDto.setSjdwxx_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("DWDM"))
			{
				sjxxDto.setSjdwxx_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("QTKS"))
			{
				sjxxDto.setSjdwxx_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("SJDWMC") || dcszDto.getDczd().equalsIgnoreCase("YYTJMC"))
			{
				sjxxDto.setHospitalName_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("JCXM"))
			{
				sjxxDto.setJcxmmc_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("JCZXM"))
			{
				sjxxDto.setJczxmmc_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("CSKZ1"))
			{
				sjxxDto.setCskz1_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("CSKZ2"))
			{
				sjxxDto.setCskz2_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("CSKZ3"))
			{
				sjxxDto.setCskz3_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("CSKZ4"))
			{
				sjxxDto.setCskz4_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("JCXMMC"))
			{
				sjxxDto.setJcxmmc_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("JCZXMMC"))
			{
				sjxxDto.setJczxmmc_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("ZMXM"))
			{
				sjxxDto.setZmxm_flg("Y");
			} else if (dcszDto.getDczd().equalsIgnoreCase("ZMMDD"))
			{
				sjxxDto.setZmmdd_flg("Y");
			}else if (dcszDto.getDczd().equalsIgnoreCase("JCDWMC"))
			{
				sjxxDto.setJcdwmc_flg("Y");
			}else if (dcszDto.getDczd().equalsIgnoreCase("FL"))
			{
				sjxxDto.setFl(dcszDto.getDczd());
			}else if (dcszDto.getDczd().equalsIgnoreCase("ZFL"))
			{
				sjxxDto.setZfl(dcszDto.getDczd());
			}
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		sjxxDto.setSqlParam(sqlParam.toString());
	}
	/**
	 * 根据搜索条件分页实验列表导出
	 *
	 * @param params
	 * @return
	 */
	public List<SjxxDto> getListForSearchExpSy(Map<String, Object> params)
	{

		SjxxDto sjxxDto = (SjxxDto) params.get("entryData");
		queryJoinFlagExport(params, sjxxDto);
		return sjxxTwoDao.getListForSearchExpSy(sjxxDto);
	}
	/**
	 * 根据搜索条件获取导出实验列表导出条数
	 *
	 * @param sjxxDto
	 * @return
	 */
	public int getCountForSearchExpSy(SjxxDto sjxxDto,Map<String, Object> params) {
		return sjxxTwoDao.getCountForSearchExpSy(sjxxDto);
	}

	/**
	 * 选中导出实验列表
	 *
	 * @param params
	 * @return
	 */
	public List<SjxxDto> getListForSelectExpSy(Map<String, Object> params)
	{
		SjxxDto sjxxDto = (SjxxDto) params.get("entryData");
		queryJoinFlagExport(params, sjxxDto);
		return sjxxTwoDao.getListForSelectExpSy(sjxxDto);
	}

	/**
	 * 修改合作伙伴
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateDB(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		boolean result = dao.updateDB(sjxxDto);
		if (result) {
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJHB_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
		}
		return result;
	}

	/**
	 * 确认标本信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean sampleConfig(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		int cnt = dao.updateConfirmInfo(sjxxDto);
		return cnt != 0;
	}

	/**
	 * 送检收样确认保存
	 *
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean inspectionSaveConfirm(SjxxDto sjxxDto,List<String> stringList,SjkzxxDto sjkzxxDto) throws BusinessException {

		// TODO Auto-generated method stub
		SjxxDto sjxxDto_t=dao.getDtoById(sjxxDto.getSjid());
		sjxxDto.setYbbh(sjxxDto_t.getYbbh());
		if ("0".equals(sjxxDto.getSfjs())) {
			sjxxDto.setNbbm("");
		}
		int cnt = dao.updateConfirmInfo(sjxxDto);
		boolean zt = sjybztService.insertYbzt(sjxxDto);
		if (cnt == 0 || !zt)
			return false;
		if(StringUtil.isNotBlank(sjkzxxDto.getBbsdwd())){
			sjkzxxDto.setXgry(sjxxDto.getXgry());
			boolean update = sjkzxxService.update(sjkzxxDto);
			if(!update){
				sjkzxxDto.setLrry(sjxxDto.getXgry());
				sjkzxxService.insertDto(sjkzxxDto);
			}
		}
		if (null != stringList && stringList.size()>0) {
			List<SjsyglDto> list = new ArrayList<>();
			for (String id : stringList) {
				SjsyglDto sjsyglDto_t = new SjsyglDto();
				sjsyglDto_t.setJclxid(id);
				sjsyglDto_t.setSjid(sjxxDto.getSjid());
				sjsyglDto_t.setSfjs(sjxxDto.getSfjs());
				sjsyglDto_t.setJsry(sjxxDto.getXgry());
				sjsyglDto_t.setXgry(sjxxDto.getXgry());
				list.add(sjsyglDto_t);
			}
			if (!CollectionUtils.isEmpty(list)) {
				sjsyglService.updateConfirmList(list);
			}
		}
		if(sjxxDto.getFjids()!=null && sjxxDto.getFjids().size()>0) {
			if("local".equals(sjxxDto.getFjbcbj())) {
				for (int i = 0; i < sjxxDto.getFjids().size(); i++)
				{
					boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i), sjxxDto.getSjid());
					if (!saveFile)
						return false;
				}
			}else {
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				String fjids = StringUtils.join(sjxxDto.getFjids(), ",");
				paramMap.add("fjids", fjids);
				paramMap.add("ywid", sjxxDto.getSjid());
				RestTemplate restTemplate = new RestTemplate();
				String param=restTemplate.postForObject(menuurl +"/wechat/getFileAddress", paramMap, String.class);
				if(param!=null) {
					JSONArray parseArray = JSONObject.parseArray(param);
					for (int i = 0; i < parseArray.size(); i++) {
						FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
						fjcfbModel.setYwid(sjxxDto.getSjid());
						// 下载服务器文件到指定文件夹
						commonService.downloadFile(fjcfbModel);
						boolean isSuccess = fjcfbService.insert(fjcfbModel);
						if (!isSuccess)
							return false;
					}
				}
			}
		}
		SjxxDto t_sjxxDto = getDto(sjxxDto);
		//85端采用syglid 进行更新，故需要重新检索获取已经设置接收日期的实验信息 2024-02-29
		if(StringUtil.isNotBlank(t_sjxxDto.getSjid())) {
			SjsyglModel t_sjsyModel = new SjsyglModel();
			t_sjsyModel.setSjid(t_sjxxDto.getSjid());
			List<SjsyglModel> t_resultList = sjsyglService.getModelList(t_sjsyModel);
			if(t_resultList!=null && t_resultList.size() > 0) {
				List<String> t_syglids = new ArrayList<String>();
				for(int i=0;i<t_resultList.size();i++) {
					if(StringUtil.isNotBlank(t_resultList.get(i).getJsrq()))
						t_syglids.add(t_resultList.get(i).getSyglid());
				}
				if(t_syglids.size() > 0) {
					t_sjxxDto.setZts(t_syglids);
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_CFM.getCode() + JSONObject.toJSONString(t_sjxxDto));
				}
			}
		}
		// 发送消息
		if(StringUtil.isBlank(sjxxDto_t.getJsrq())) {
			//发送验证审核信息
			//Map<String, Object> resultmap = sendPCRAudit(sjxxDto);
			//获取内部编码最后一位
			if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
				String suffix = sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length() - 1);
				if(!"B".equals(suffix)){//当内部编码的最后一位为B，就是血液的时候，不再自动发起PCR
					sendPCRAudit(sjxxDto);
				}
			}else{
				sendPCRAudit(sjxxDto);
			}
			SjybztDto sjybztDto = new SjybztDto();
			sjybztDto.setSjid(sjxxDto.getSjid());
			List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
			boolean result = false;
			for (SjybztDto sjybztDto_t : sjybztDtos) {
				boolean bol = commonService.queryAuthMessage(t_sjxxDto.getHbid(),sjybztDto_t.getYbztCskz2());
				// if ( (StringUtil.isNotBlank(sjybztDto_t.getYbztCskz1()) && StringUtil.isNotBlank(sjybztDto_t.getYbztCskz2())) && ("B".equals(sjybztDto_t.getYbztCskz1()) || "S".equals(sjybztDto_t.getYbztCskz1()) || "L".equals(sjybztDto_t.getYbztCskz1()) || "N".equals(sjybztDto_t.getYbztCskz1())) ) {
				if(StringUtil.isNotBlank(sjybztDto_t.getYbztCskz1()) && StringUtil.isNotBlank(sjybztDto_t.getYbztCskz2())) {
					result = true;
					if(bol){
						SjxxDto messageSjxx = new SjxxDto();
						messageSjxx.setSjid(t_sjxxDto.getSjid());
						messageSjxx.setHzxm(t_sjxxDto.getHzxm());
						messageSjxx.setYblxmc(t_sjxxDto.getYblxmc());
						messageSjxx.setYbzt_flg(sjybztDto_t.getYbztCskz1());
						messageSjxx.setYbztmc(sjybztDto_t.getCsmc());
						messageSjxx.setYbzt_cskz2(sjybztDto_t.getYbztCskz2());//这里取到标本状态的扩展参数2，比如ICOMM_1001
						sendConfirmMessage(messageSjxx);
					}
				}
			}
			if(!result) {
				boolean bol = commonService.queryAuthMessage(t_sjxxDto.getHbid(),"INFORM_HB00006");
				if(bol){
					sendConfirmNormalMessage(t_sjxxDto);
				}
			}
		}
		return true;
	}


	/**
	 * 保存附件
	 * @param
	 * @return
	 */
	public boolean saveScanFile(SjxxDto sjxxDto) throws BusinessException {
		// 获取服务器文件信息
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		String fjids = StringUtils.join(sjxxDto.getFjids(), ",");
		paramMap.add("fjids", fjids);
		RestTemplate t_restTemplate = new RestTemplate();
		String param = t_restTemplate.postForObject(menuurl + "/wechat/getFjcfbModel", paramMap, String.class);
		if (param != null) {
			JSONArray parseArray = JSONObject.parseArray(param);
			for (int i = 0; i < parseArray.size(); i++) {
				FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
				boolean result = fjcfbService.insert(fjcfbModel);
				if (!result)
					throw new BusinessException("msg", "附件更新未成功！");
				// 下载服务器文件到指定文件夹
				downloadFile(fjcfbModel);
			}
		}
		return true;
	}


	@Override
	public boolean saveLocalScanFile(SjxxDto sjxxDto) {
		for (int i = 0; i < sjxxDto.getFjids().size(); i++) {
			boolean success = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i),sjxxDto.getSjid());
			if (!success)
				return false;
		}
		return true;
	}

	/**
	 * 根据确认状态发送消息
	 *
	 * @param sjxxDto
	 * @return
	 */
	public boolean sendConfirmMessage(SjxxDto sjxxDto) throws BusinessException{
		boolean sendFlg = false;
		// 发送钉钉消息
		// 查询接收人员列表
		List<SjxxDto> sjxxDtos = dao.getReceiveUserByDb(sjxxDto);
		if (sjxxDtos != null && sjxxDtos.size() > 0) {
			try {
				SendConfirmMessage sendConfirmMessage = new SendConfirmMessage();
				sendConfirmMessage.init(sjxxDtos,xxglService,sendFlg,sjxxDto,talkUtil,emailUtil,sjhbxxService,commonService,ybzt_templateid,menuurl);
				SendConfirmMessageThread sendConfirmMessageThread = new SendConfirmMessageThread(sendConfirmMessage);
				sendConfirmMessageThread.start();
			} catch (Exception e) {
				log.error("SendConfirmMessage -- 标本状态提醒异常:" + e.toString());
				throw new BusinessException("msg", e.toString());
			}
			return true;
		}
		return false;
	}

	/*****
	 * 根据确认状态发送消息
	 *
	 * @param sjxxDto
	 * @return
	 */
	public boolean sendConfirmNormalMessage(SjxxDto sjxxDto) throws BusinessException{
		boolean sendFlg = false;
		// 发送钉钉消息
		// 查询接收人员列表
		List<SjxxDto> sjxxDtos = dao.getReceiveUserByDb(sjxxDto);
		if (sjxxDtos != null && sjxxDtos.size() > 0) {
			try {
				SendConfirmMessage sendConfirmMessage = new SendConfirmMessage();
				sendConfirmMessage.init(sjxxDtos,xxglService,sendFlg,sjxxDto,talkUtil,emailUtil,sjhbxxService,commonService,ybzt_templateid,menuurl);
				SendConfirmNormalMessageThread sendConfirmNormalMessageThread = new SendConfirmNormalMessageThread(sendConfirmMessage);
				sendConfirmNormalMessageThread.start();
			} catch (Exception e) {
				log.error("sendConfirmNormalMessage -- 标本状态提醒异常:" + e.toString());
				throw new BusinessException("msg", e.toString());
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取送检结果接口
	 *
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean getInspectionResult(SjxxDto sjxxDto)
	{
		RestTemplate t_restTemplate = new RestTemplate();
		// TODO Auto-generated method stub
		String getForObject = t_restTemplate.getForObject(inspectionurl + "/core/api/sampleresult/?ybbh=" + sjxxDto.getYbbh(), String.class, "");
		if (StringUtil.isNotBlank(getForObject))
		{
			WeChatInspectionReportModel inspinfo = JSONObject.parseObject(getForObject, WeChatInspectionReportModel.class);
			if (inspinfo != null)
			{
				if (StringUtil.isNotBlank(inspinfo.getStatus()) && !"success".equals(inspinfo.getStatus()))
					return false;

				// 检验结果 0：无 1：阳性 2：阴性
				String s_jyjg = "0";
				// 保存信息至物种管理表
				List<WeChatInspectionSpeciesModel> possible = inspinfo.getPossible();
				List<SjwzxxDto> sjwzxxDtos = new ArrayList<>();
				if (possible != null && possible.size() > 0)
				{
					// 检验结果 0：无 1：阳性 2：阴性
					s_jyjg = "2";
					for (int i = 0; i < possible.size(); i++)
					{
						SjwzxxDto sjwzxxDto = new SjwzxxDto();
						sjwzxxDto.setSjwzid(StringUtil.generateUUID());
						sjwzxxDto.setLrry(sjxxDto.getLrry());
						sjwzxxDto.setWzid(possible.get(i).getTaxid());
						sjwzxxDto.setWzywm(possible.get(i).getName());
						sjwzxxDto.setWzzwm(possible.get(i).getCn_name());
						sjwzxxDto.setWzfl(possible.get(i).getSp_type());
						sjwzxxDto.setDqs(possible.get(i).getReads_count());
						sjwzxxDto.setJyzfgd(possible.get(i).getCoverage());
						sjwzxxDto.setXdfd(possible.get(i).getAbundance());
						sjwzxxDto.setSjid(sjxxDto.getSjid());
						sjwzxxDto.setXpxx(inspinfo.getSample_id());
						sjwzxxDto.setJglx("possible");
						sjwzxxDtos.add(sjwzxxDto);
					}
				}
				List<WeChatInspectionSpeciesModel> pathogen = inspinfo.getPathogen();
				String pathogenmc = "";
				if (pathogen != null && pathogen.size() > 0)
				{
					// 检验结果 0：无 1：阳性 2：阴性
					s_jyjg = "1";
					for (int i = 0; i < pathogen.size(); i++)
					{
						pathogenmc = pathogenmc + "," + (pathogen.get(i).getCn_name());
						SjwzxxDto sjwzxxDto = new SjwzxxDto();
						sjwzxxDto.setSjwzid(StringUtil.generateUUID());
						sjwzxxDto.setLrry(sjxxDto.getLrry());
						sjwzxxDto.setWzid(pathogen.get(i).getTaxid());
						sjwzxxDto.setWzywm(pathogen.get(i).getName());
						sjwzxxDto.setWzzwm(pathogen.get(i).getCn_name());
						sjwzxxDto.setWzfl(pathogen.get(i).getSp_type());
						sjwzxxDto.setDqs(pathogen.get(i).getReads_count());
						sjwzxxDto.setJyzfgd(pathogen.get(i).getCoverage());
						sjwzxxDto.setXdfd(pathogen.get(i).getAbundance());
						sjwzxxDto.setSjid(sjxxDto.getSjid());
						sjwzxxDto.setXpxx(inspinfo.getSample_id());
						sjwzxxDto.setJglx("pathogen");
						sjwzxxDtos.add(sjwzxxDto);
					}
				}
				if (sjwzxxDtos != null && sjwzxxDtos.size() > 0)
				{
					sjwzxxService.deleteBysjwzxxDto(sjwzxxDtos.get(0));
					boolean result = sjwzxxService.insertBysjwzxxDtos(sjwzxxDtos);
					if (!result)
						return false;
					Map<String,Object> map= new HashMap<>();
					String jsonObject = JSON.toJSON(sjwzxxDtos).toString();
					map.put("sjwzxxDtos", jsonObject);
					map.put("sjid",sjxxDto.getSjid());
					map.put("jclx", inspinfo.getDetection_type());
					// 同步信息至微信服务器
					amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.RESULT_INSPECTION.getCode(), JSONObject.toJSONString(map));
					return true;
				}
				List<WeChatInspectionSpeciesModel> background = inspinfo.getBackground();
				String backgroundmc = "";
				if (background != null && background.size() > 0) {
					for (int i = 0; i < background.size(); i++) {
						backgroundmc = backgroundmc + "," + ( background.get(i).getCn_name() );
					}
				}
				// 保存送检报告说明
				SjbgsmDto sjbgsmDto = new SjbgsmDto();
				sjbgsmDto.setSjid(sjxxDto.getSjid());
				sjbgsmDto.setGgzdsm(inspinfo.getPathogen_comment());
				if(StringUtil.isNotBlank(pathogenmc) && pathogenmc.length() > 0)
					sjbgsmDto.setGgzdzb(pathogenmc.substring(1));
				else
					sjbgsmDto.setGgzdzb(pathogenmc);
				sjbgsmDto.setYssm(inspinfo.getPossible_comment());

				if(StringUtil.isNotBlank(backgroundmc) && backgroundmc.length() > 0)
					sjbgsmDto.setYszb(backgroundmc.substring(1));
				else
					sjbgsmDto.setYszb(backgroundmc);
				sjbgsmDto.setCkwx(inspinfo.getRefs());
				//添加送检报告说明，先删除再新增
				List<SjbgsmDto> list = sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
				if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list) && StringUtil.isNotBlank(list.get(0).getScbgrq())){
					sjbgsmDto.setScbgrq(list.get(0).getScbgrq());
				}else{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					sjbgsmDto.setScbgrq(simpleDateFormat.format(date));
				}
				sjbgsmDto.setJclx(inspinfo.getDetection_type());
				sjbgsmDto.setJczlx(inspinfo.getDetection_subtype());
				sjbgsmService.deleteById(sjbgsmDto.getSjid());
				boolean result = sjbgsmService.insert(sjbgsmDto);
				if (!result)
					return false;
				// 同步信息至微信服务器
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.COMMENT_INSPECTION.getCode(), JSONObject.toJSONString(sjbgsmDto));

				// 更新报告日期
				Date date = new Date();
				sjxxDto.setBgrq(date.toString());
				sjxxDto.setJyjg(s_jyjg);
				int count = dao.updateReport(sjxxDto);
				if (count == 0)
					return false;
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
			}
		}
		return false;
	}

	/**
	 * 接收送检结果接口
	 *
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean receiveInspectionResult(String str)
	{
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(str))
		{
			WeChatInspectionReportModel inspinfo = JSONObject.parseObject(str, WeChatInspectionReportModel.class);
			return dealInspectionInfo(inspinfo);
		}
		return false;
	}


	/**
	 * 获取修改送检信息
	 */
	@Override
	public boolean receiveModSjxx(String str)
	{
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(str))
		{
			SjxxDto sjxxDto = JSONObject.parseObject(str, SjxxDto.class);
			if (sjxxDto != null)
			{
				if (StringUtil.isNotBlank(sjxxDto.getStatus()) && !"success".equals(sjxxDto.getStatus()))
					return false;
				SjxxDto t_sjxxDto = dao.getDto(sjxxDto);
				if (t_sjxxDto != null)
				{
					updateforbioinformation(sjxxDto);
				}
				return false;
			}
			return false;
		}
		return false;
	}

	/**
	 * 根据送检id获取物种信息
	 */
	@Override
	public List<SjwzxxDto> selectWzxxBySjid(SjxxDto sjxxDto)
	{
		List<SjwzxxDto> sjwzxxDtos = dao.selectWzxxBySjid(sjxxDto);
		for (SjwzxxDto sjwzxxDto : sjwzxxDtos) {
			if (StringUtil.isNotBlank(sjwzxxDto.getRefANI())){
				try {
					BigDecimal refANI = new BigDecimal(Double.parseDouble(sjwzxxDto.getRefANI()));
					sjwzxxDto.setRefANI(refANI.setScale(2, RoundingMode.HALF_UP).toString());
				}catch (Exception e){
					log.error("--matridx-- refANI转换异常，{}",sjwzxxDto.getRefANI());
				}
			}
		}
		return sjwzxxDtos;
	}

	/**
	 * 图片预览
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<FjcfbDto> preview(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.preview(sjxxDto);
	}

	/**
	 * 手机支付结果
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean weChatPayResult(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		int result = dao.updateModel(sjxxDto);
		return result != 0;
	}

	/**
	 * 同步修改信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateforbioinformation(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.updateforbioinformation(sjxxDto);
	}

	/**
	 * 保存word报告,转换成pdf
	 *
	 * @param file
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName =
	{ "BusinessException", "Exception" })
	public boolean receiveWordReport(MultipartFile file, SjxxDto sjxxDto) throws BusinessException
	{
		// TODO Auto-generated method stub
		if (sjxxDto != null && StringUtil.isNotBlank(sjxxDto.getYbbh()))
		{
			SjxxDto t_sjxxDto = dao.getDto(sjxxDto);
			if (t_sjxxDto != null)
			{
				String wordType = t_sjxxDto.getCskz3() + "_" + sjxxDto.getJclx() + "_WORD";
				String pdfType = t_sjxxDto.getCskz3() + "_" + sjxxDto.getJclx();
				String wjm = file.getOriginalFilename();
				// 根据日期创建文件夹
				String fwjlj = prefix + releaseFilePath + wordType + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
						+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
				int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
				String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
				String fwjm = t_name + wjm.substring(index);
				String wjlj = fwjlj + "/" + fwjm;
				mkDirs(fwjlj);
				DBEncrypt bpe = new DBEncrypt();
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setFjid(StringUtil.generateUUID());
				fjcfbDto.setYwid(t_sjxxDto.getSjid());
				fjcfbDto.setZywid("");
				fjcfbDto.setXh("1");
				fjcfbDto.setYwlx(wordType);
				fjcfbDto.setWjm(wjm);
				fjcfbDto.setWjlj(bpe.eCode(wjlj));
				fjcfbDto.setFwjlj(bpe.eCode(fwjlj));
				fjcfbDto.setFwjm(bpe.eCode(fwjm));
				fjcfbDto.setZhbj("0");
				FjcfbDto t_fjcfbDto = new FjcfbDto();
				t_fjcfbDto.setYwid(t_sjxxDto.getSjid());
				t_fjcfbDto.setYwlx(wordType);
				// 删除Word文件
				fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);
				// 删除Pdf文件
				t_fjcfbDto.setYwlx(pdfType);
				fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);

				boolean result = fjcfbService.insert(fjcfbDto);
				if (!result)
					return false;
				byte[] buffer = new byte[4096];
				FileOutputStream fos = null;
				BufferedOutputStream output = null;

				InputStream fis = null;
				BufferedInputStream input = null;
				try
				{
					fis = file.getInputStream();
					input = new BufferedInputStream(fis);
					mkDirs(fwjlj);
					fos = new FileOutputStream(wjlj);
					output = new BufferedOutputStream(fos);
					int n;
					while ((n = input.read(buffer, 0, 4096)) > -1)
					{
						output.write(buffer, 0, n);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
					return false;
				} finally
				{
					closeStream(new Closeable[]
					{ fis, input, output, fos });
				}
				// 发送文件至微信服务器
				fjcfbDto.setJclx(sjxxDto.getJclx());
				result = sendFileToAli(fjcfbDto);
				if (!result)
					return false;
				// 上传word文件
				FjcfbDto fjcfbDtos = fjcfbService.getDto(fjcfbDto);
				int begin = fjcfbDtos.getWjm().lastIndexOf(".");
				String wjmkzm = fjcfbDtos.getWjm().substring(begin);
				if ((wjmkzm.equalsIgnoreCase(".doc")) || (wjmkzm.equalsIgnoreCase(".docx")) || (wjmkzm.equalsIgnoreCase(".xls")) || (wjmkzm.equalsIgnoreCase(".xlsx")))
				{
					DBEncrypt p = new DBEncrypt();
					String wjljjm = p.dCode(fjcfbDtos.getWjlj());
					// 上传Word文件
					boolean sendFlg = sendWordFile(wjljjm);
					if (sendFlg)
					{
						Map<String, String> map = new HashMap<>();
						String fwjmc = p.dCode(fjcfbDtos.getFwjm());
						map.put("wordName", fwjmc);
						map.put("fwjlj", fjcfbDtos.getFwjlj());
						map.put("fjid", fjcfbDtos.getFjid());
						map.put("ywlx", fjcfbDtos.getYwlx());
						map.put("gzlxmc", t_sjxxDto.getGzlxmc());
						map.put("MQDocOkType", DOC_OK);
						// 发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(map));
					} else
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 上传Word文件
	 *
	 * @return
	 * @throws BusinessException
	 */
	private boolean sendWordFile(String fileName) throws BusinessException
	{
		// 连接服务器
		// FTPClient ftp=FtpUtil.connect(FTPWORD_PATH, FTP_URL, FTP_PORT,
		// FTP_USER, FTP_PD );
		// 上传到服务器word下边的文件夹
		// FtpUtil.upload(new File(wjljjm), ftp);
		try
		{
			File t_file = new File(fileName);
			// 文件不存在不做任何操作
			if (!t_file.exists())
				return true;

			byte[] bytesArray = new byte[(int) t_file.length()];

			FileInputStream t_fis = new FileInputStream(t_file);
			t_fis.read(bytesArray); // read file into bytes[]
			t_fis.close();
			// 需要给文件的名称
			ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray)
			{
				@Override
				public String getFilename()
				{
					return t_file.getName();
				}
			};
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("file", contentsAsResource);
			RestTemplate t_restTemplate = new RestTemplate();
			// 发送文件到服务器
			String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);

			return "OK".equals(reString);
		} catch (Exception e)
		{
			throw new BusinessException("msg", "word文件上传失败！");
		}
	}

	/**
	 * 关闭流
	 *
	 * @param streams
	 */
	private static void closeStream(Closeable[] streams)
	{
		if (streams == null || streams.length < 1)
			return;
		for (int i = 0; i < streams.length; i++)
		{
			try
			{
				Closeable stream = streams[i];
				if (null != stream)
				{
					stream.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存送检报告信息
	 *
	 * @param info
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveInspectionResult(WeChatInspectionReportsModel info)
	{
		// TODO Auto-generated method stub
		WeChatInspectionReportModel inspinfo = new WeChatInspectionReportModel();
		if (info.getPossible() != null && info.getPossible().size() > 0)
		{
			List<WeChatInspectionSpeciesModel> possibleList = JSONArray.parseArray(info.getPossible().toString(), WeChatInspectionSpeciesModel.class);
			inspinfo.setPossible(possibleList);
		}
		if (info.getPathogen() != null && info.getPathogen().size() > 0)
		{
			List<WeChatInspectionSpeciesModel> pathogenList = JSONArray.parseArray(info.getPathogen().toString(), WeChatInspectionSpeciesModel.class);
			inspinfo.setPathogen(pathogenList);
		}
		if (info.getDrug_resistance() != null && info.getDrug_resistance().size() > 0)
		{
			List<WeChatInspectionResistanceModel> resistanceList = JSONArray.parseArray(info.getDrug_resistance().toString(), WeChatInspectionResistanceModel.class);
			inspinfo.setDrug_resistance_stat(resistanceList);
		}
		inspinfo.setPathogen_comment(info.getPathogen_comment());
		inspinfo.setPathogen_names(info.getPathogen_names());
		inspinfo.setPossible_comment(info.getPossible_comment());
		inspinfo.setPossible_names(info.getPossible_names());
		inspinfo.setRefs(info.getRefs());
		inspinfo.setSample_id(info.getSample_id());
		inspinfo.setStatus(info.getStatus());
		if (StringUtil.isBlank(info.getYbbh()))
			return false;
		inspinfo.setYbbh(info.getYbbh());
		inspinfo.setDetection_type(info.getDetection_type());
		return dealInspectionInfo(inspinfo);
	}

	public boolean dealInspectionInfo(WeChatInspectionReportModel inspinfo)
	{
		if (inspinfo != null)
		{
			if (StringUtil.isNotBlank(inspinfo.getStatus()) && !"success".equals(inspinfo.getStatus()))
				return false;
			// 查询是否有此标本信息
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setYbbh(inspinfo.getYbbh());
			SjxxDto t_sjxxDto = dao.getDto(sjxxDto);
			// 默认检测类型为DNA
			if (StringUtil.isBlank(inspinfo.getDetection_type()))
			{
				inspinfo.setDetection_type("D");
			}
			if (t_sjxxDto != null)
			{
				// 保存信息至物种管理表
				List<SjwzxxDto> sjwzxxDtos = new ArrayList<>();
				// 检验结果 0：无 1：阳性 2：阴性
				String s_jyjg = "0";

				List<WeChatInspectionSpeciesModel> possible = inspinfo.getPossible();
				if (possible != null && possible.size() > 0)
				{
					// 检验结果 0：无 1：阳性 2：阴性
					s_jyjg = "2";
					for (int i = 0; i < possible.size(); i++)
					{
						SjwzxxDto sjwzxxDto = new SjwzxxDto();
						sjwzxxDto.setSjwzid(StringUtil.generateUUID());
						sjwzxxDto.setWzid(possible.get(i).getTaxid());
						sjwzxxDto.setWzywm(possible.get(i).getName());
						sjwzxxDto.setWzzwm(possible.get(i).getCn_name());
						sjwzxxDto.setWzfl(possible.get(i).getSp_type());
						sjwzxxDto.setDqs(possible.get(i).getReads_count());
						sjwzxxDto.setJyzfgd(possible.get(i).getCoverage());
						sjwzxxDto.setXdfd(possible.get(i).getAbundance());
						sjwzxxDto.setSjid(t_sjxxDto.getSjid());
						sjwzxxDto.setXpxx(inspinfo.getSample_id());
						sjwzxxDto.setXh(String.valueOf(i + 1));
						sjwzxxDto.setSid(possible.get(i).getGenus_taxid());
						sjwzxxDto.setSm(possible.get(i).getGenus_name());
						sjwzxxDto.setSzwm(possible.get(i).getGenus_cn_name());
						sjwzxxDto.setSdds(possible.get(i).getGenus_reads_accum());
						sjwzxxDto.setSfd(possible.get(i).getGenus_abundance());
						sjwzxxDto.setWzfllx(possible.get(i).getVirus_type());
						sjwzxxDto.setJglx("possible");
						sjwzxxDto.setJclx(inspinfo.getDetection_type());
						sjwzxxDtos.add(sjwzxxDto);
					}
				}
				List<WeChatInspectionSpeciesModel> pathogen = inspinfo.getPathogen();
				String pathogenmc = "";
				if (pathogen != null && pathogen.size() > 0)
				{
					// 检验结果 0：无 1：阳性 2：阴性
					s_jyjg = "1";
					for (int i = 0; i < pathogen.size(); i++)
					{
						pathogenmc = pathogenmc + "," + (pathogen.get(i).getCn_name());
						SjwzxxDto sjwzxxDto = new SjwzxxDto();
						sjwzxxDto.setSjwzid(StringUtil.generateUUID());
						sjwzxxDto.setWzid(pathogen.get(i).getTaxid());
						sjwzxxDto.setWzywm(pathogen.get(i).getName());
						sjwzxxDto.setWzzwm(pathogen.get(i).getCn_name());
						sjwzxxDto.setWzfl(pathogen.get(i).getSp_type());
						sjwzxxDto.setDqs(pathogen.get(i).getReads_count());
						sjwzxxDto.setJyzfgd(pathogen.get(i).getCoverage());
						sjwzxxDto.setXdfd(pathogen.get(i).getAbundance());
						sjwzxxDto.setSjid(t_sjxxDto.getSjid());
						sjwzxxDto.setXpxx(inspinfo.getSample_id());
						sjwzxxDto.setXh(String.valueOf(i + 1));
						sjwzxxDto.setSid(pathogen.get(i).getGenus_taxid());
						sjwzxxDto.setSm(pathogen.get(i).getGenus_name());
						sjwzxxDto.setSzwm(pathogen.get(i).getGenus_cn_name());
						sjwzxxDto.setSdds(pathogen.get(i).getGenus_reads_accum());
						sjwzxxDto.setSfd(pathogen.get(i).getGenus_abundance());
						sjwzxxDto.setWzfllx(pathogen.get(i).getVirus_type());
						sjwzxxDto.setJglx("pathogen");
						sjwzxxDto.setJclx(inspinfo.getDetection_type());
						sjwzxxDtos.add(sjwzxxDto);
					}
				}
				if (sjwzxxDtos != null && sjwzxxDtos.size() > 0){
					// 因为是采用送检ID的，所以只需要取一条数据即可
					sjwzxxService.deleteBysjwzxxDto(sjwzxxDtos.get(0));
					boolean result = sjwzxxService.insertBysjwzxxDtos(sjwzxxDtos);
					if (!result)
						return false;
					Map<String,Object> map= new HashMap<>();
					String jsonObject = JSON.toJSON(sjwzxxDtos).toString();
					map.put("sjwzxxDtos", jsonObject);
					map.put("sjid",t_sjxxDto.getSjid());
					map.put("jclx", inspinfo.getDetection_type());
					if(StringUtil.isNotBlank(menuurl)) {
						// 同步信息至微信服务器
						amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.RESULT_INSPECTION.getCode(), JSONObject.toJSONString(map));
					}
				}else{
					//没有信息时删除相关送检物种信息
					SjwzxxDto sjwzxxDto = new SjwzxxDto();
					sjwzxxDto.setSjid(t_sjxxDto.getSjid());
					sjwzxxService.deleteBysjwzxxDto(sjwzxxDto);
				}
				List<WeChatInspectionSpeciesModel> background = inspinfo.getBackground();
				String backgroundmc = "";
				if (background != null && background.size() > 0) {
					for (int i = 0; i < background.size(); i++) {
						backgroundmc = backgroundmc + "," + ( background.get(i).getCn_name() );
					}
				}
				// 保存送检报告说明
				SjbgsmDto sjbgsmDto = new SjbgsmDto();
				sjbgsmDto.setSjid(t_sjxxDto.getSjid());
				sjbgsmDto.setGgzdsm(inspinfo.getPathogen_comment());
				sjbgsmDto.setGgzdzb(pathogenmc.substring(1));
				sjbgsmDto.setYssm(inspinfo.getPossible_comment());
				sjbgsmDto.setYszb(backgroundmc.substring(1));
				sjbgsmDto.setCkwx(inspinfo.getRefs());
				sjbgsmDto.setJclx(inspinfo.getDetection_type());
				sjbgsmDto.setJczlx(inspinfo.getDetection_subtype());
				sjbgsmDto.setBgrq(DateUtils.getCustomFomratCurrentDate(null));
				//添加送检报告说明，先删除再新增
				List<SjbgsmDto> list = sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
				if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list) && StringUtil.isNotBlank(list.get(0).getScbgrq())){
					sjbgsmDto.setScbgrq(list.get(0).getScbgrq());
				}else{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date();
					sjbgsmDto.setScbgrq(simpleDateFormat.format(date));
				}
				sjbgsmService.deleteBySjbgsmDto(sjbgsmDto);
				boolean result = sjbgsmService.insert(sjbgsmDto);
				if (!result)
					return false;
				if(StringUtil.isNotBlank(menuurl)) {
					// 同步信息至微信服务器
					amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.COMMENT_INSPECTION.getCode(), JSONObject.toJSONString(sjbgsmDto));
				}
				// 保存送检耐药性
				List<WeChatInspectionResistanceModel> drug_resistance = inspinfo.getDrug_resistance_stat();
				if (drug_resistance != null && drug_resistance.size() > 0)
				{
					List<SjnyxDto> sjnyxDtos = new ArrayList<>();
					for (int i = 0; i < drug_resistance.size(); i++)
					{
						SjnyxDto sjnyxDto = new SjnyxDto();
						sjnyxDto.setSjnyxid(StringUtil.generateUUID());
						sjnyxDto.setSjid(t_sjxxDto.getSjid());
						sjnyxDto.setYp(drug_resistance.get(i).getDrug_class());
						sjnyxDto.setDs(drug_resistance.get(i).getGene_count());
						sjnyxDto.setJy(drug_resistance.get(i).getRelated_gene());
						sjnyxDto.setJl(drug_resistance.get(i).getMain_mechanism());
						sjnyxDto.setXgwz(drug_resistance.get(i).getRef_species());
						sjnyxDto.setXh(String.valueOf(i + 1));
						sjnyxDto.setJclx(inspinfo.getDetection_type());
						sjnyxDto.setWkbh(inspinfo.getSample_id());
						sjnyxDto.setYsyp(drug_resistance.get(i).getDrug_name());
						sjnyxDto.setJyfx(drug_resistance.get(i).getGenes());
						sjnyxDto.setXls(drug_resistance.get(i).getReads());
						sjnyxDto.setQyz(drug_resistance.get(i).getOrigin_species());
						sjnyxDtos.add(sjnyxDto);
					}
					sjnyxService.deleteBySjnyxDto(sjnyxDtos.get(0));
					result = sjnyxService.insertBysjnyxDtos(sjnyxDtos);
					if (!result)
						return false;
					Map<String,Object> map= new HashMap<>();
					String jsonObject = JSON.toJSON(sjnyxDtos).toString();
					map.put("sjnyxDtos", jsonObject);
					map.put("sjid",t_sjxxDto.getSjid());
					map.put("jclx", inspinfo.getDetection_type());
					if(StringUtil.isNotBlank(menuurl)) {
						// 同步信息至微信服务器
						amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.RESISTANCE_INSPECTION.getCode(), JSONObject.toJSONString(map));
					}
				}
				// 更新报告日期
				Date date = new Date();
				t_sjxxDto.setBgrq(date.toString());
				t_sjxxDto.setJyjg(s_jyjg);
				int count = dao.updateReport(t_sjxxDto);
				if (count == 0)
					return false;
				if(StringUtil.isNotBlank(menuurl)) {
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(t_sjxxDto));
				}
			} else
			{
				// 不存在此标本，将数据加入错误信息表
				CwglDto cwglDto = new CwglDto();
				cwglDto.setCwlx(ErrorTypeEnum.INSPECTION_RESULT_TYPE.getCode());
				String str = JSONObject.toJSONString(inspinfo);
				if (str.length() > 4000)
				{
					str = str.substring(0, 4000);
				}
				cwglDto.setYsnr(str);
				cwglService.insertDto(cwglDto);
				return false;
			}
			return true;
		}
		return false;
	}


	/**
	 * 发送文件至微信服务器
	 *
	 * @param fjcfbModel
	 * @return
	 */
	@Override
	public boolean sendFileToAli(FjcfbModel fjcfbModel)
	{
		//如果为空，直接返回
		if(StringUtil.isBlank(menuurl)) {
			return true;
		}
		// 拷贝文件到微信服务器
		try
		{
			DBEncrypt dbEncrypt = new DBEncrypt();
			if (fjcfbModel != null)
			{
				String wjlj = fjcfbModel.getWjlj();
				String pathString = dbEncrypt.dCode(wjlj);
				File file = new File(pathString);
				// 文件不存在不做任何操作
				if (!file.exists())
					return true;

				byte[] bytesArray = new byte[(int) file.length()];

				FileInputStream fis = new FileInputStream(file);
				fis.read(bytesArray); // read file into bytes[]
				fis.close();
				// 需要给文件的名称
				ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray)
				{
					@Override
					public String getFilename()
					{
						return file.getName();
					}
				};
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				paramMap.add("file", contentsAsResource);
				paramMap.add("fjcfbModel", fjcfbModel);

				// 设置超时时间
				HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
				httpRequestFactory.setConnectionRequestTimeout(6000);
				httpRequestFactory.setConnectTimeout(6000);
				httpRequestFactory.setReadTimeout(240000);
				RestTemplate t_restTemplate = new RestTemplate(httpRequestFactory);
				// 发送文件到服务器
				String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReport", paramMap, String.class);

				if ("OK".equals(reString))
				{
					// 更新文件的转换标记为true
					boolean isSuccess = fjcfbService.updateZhbj(fjcfbModel);
					if (!isSuccess)
						return false;
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
			return false;
		}
		return true;
	}
	/**
	 * 发送多文件至微信服务器
	 * @param fjcfbModels
	 * @return
	 */
	public boolean sendFilesToAli(List<FjcfbModel> fjcfbModels)
	{
		//如果为空，直接返回
		if(StringUtil.isBlank(menuurl)) {
			return true;
		}
		// 拷贝文件到微信服务器
		try
		{
			DBEncrypt dbEncrypt = new DBEncrypt();
			List<ByteArrayResource> contentsAsResources = new ArrayList<>();
			List<FjcfbModel> sendFjcfbModels = new ArrayList<>();
			if (fjcfbModels != null && fjcfbModels.size() > 0) {
				for (FjcfbModel fjcfbModel : fjcfbModels) {
					String wjlj = fjcfbModel.getWjlj();
					String pathString = dbEncrypt.dCode(wjlj);
					File file = new File(pathString);
					// 文件不存在不做任何操作
					if (!file.exists())
						break;

					byte[] bytesArray = new byte[(int) file.length()];

					FileInputStream fis = new FileInputStream(file);
					fis.read(bytesArray); // read file into bytes[]
					fis.close();
					// 需要给文件的名称
					ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray)
					{
						@Override
						public String getFilename()
						{
							return file.getName();
						}
					};
					contentsAsResources.add(contentsAsResource);
					sendFjcfbModels.add(fjcfbModel);
				}

				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				for (ByteArrayResource contentsAsResource : contentsAsResources) {
					paramMap.add("files", contentsAsResource);
				}
				paramMap.add("fjcfbModels", sendFjcfbModels);

				// 设置超时时间
				HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
				httpRequestFactory.setConnectionRequestTimeout(6000);
				httpRequestFactory.setConnectTimeout(6000);
				httpRequestFactory.setReadTimeout(240000);
				RestTemplate t_restTemplate = new RestTemplate(httpRequestFactory);
				// 发送文件到服务器
				String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReportWithFiles", paramMap, String.class);

				if ("OK".equals(reString)) {
					List<String> ids = new ArrayList<>();
					for (FjcfbModel fjcfbModel : sendFjcfbModels) {
						ids.add(fjcfbModel.getFjid());
					}
					FjcfbModel updatezhbjFjModel = new FjcfbModel();
					updatezhbjFjModel.setIds(ids);
					// 更新文件的转换标记为true
					boolean isSuccess = fjcfbService.updateZhbj(updatezhbjFjModel);
					if (!isSuccess)
						return false;
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
			return false;
		}
		return true;
	}


	/**
	 * 通过送检id查询检测标记
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto selectjcbjByid(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.selectjcbjByid(sjxxDto);
	}

	/**
	 * 修改检测标记（送检表）
	 *
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public boolean updateJcbjByid(SjxxDto sjxxDto) throws BusinessException
	{
		// TODO Auto-generated method stub
		if (StringUtil.isBlank(sjxxDto.getJcbj()))
		{
			sjxxDto.setJcbj("0");
			sjxxDto.setSyrq(null);
		}
		if (StringUtil.isBlank(sjxxDto.getDjcbj()))
		{
			sjxxDto.setDjcbj("0");
			sjxxDto.setDsyrq(null);
		}
		if (StringUtil.isBlank(sjxxDto.getQtjcbj()))
		{
			sjxxDto.setQtjcbj("0");
			sjxxDto.setQtsyrq(null);
		}
		boolean result = dao.updateJcbjByid(sjxxDto);
		//发送实验通知
		if(result && !"F".equals(sjxxDto.getCskz1())) {
			if("1".equals(sjxxDto.getSfsytz())) {
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
				Date date=new Date();
				String dqrq=formatter.format(date);//当前日期
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(Calendar.DATE,1);
				date=calendar.getTime();
				String mtrq=formatter.format(date);//明天
				String ICOMM_SJ00048=xxglService.getMsg("ICOMM_SJ00048");
				for(int i=0;i<sjxxDto.getIds().size();i++) {
					SjxxDto t_sjxxDto=dao.getDtoById(sjxxDto.getIds().get(i));
					if(t_sjxxDto!=null) {
						boolean messageBoolean = commonService.queryAuthMessage(t_sjxxDto.getHbid(),"INFORM_HB00008");
						if(messageBoolean){
							String ICOMM_SJ00047=xxglService.getMsg("ICOMM_SJ00047", t_sjxxDto.getHzxm(),t_sjxxDto.getCsmc(),dqrq,mtrq);
							String keyword1 = DateUtils.getCustomFomratCurrentDate("HH:mm:ss");
							String keyword2 = t_sjxxDto.getYbbh();
							Map<String,Object> map= new HashMap<>();
							map.put("yxbt", ICOMM_SJ00048);
							map.put("yxnr", ICOMM_SJ00047);
							map.put("ddbt", ICOMM_SJ00048);
							map.put("ddnr", ICOMM_SJ00047);
							map.put("wxbt", ICOMM_SJ00048);
							map.put("remark", ICOMM_SJ00047);
							map.put("keyword1", keyword1);
							map.put("keyword2", keyword2);
							map.put("templateid", ybzt_templateid);
							map.put("xxmb","TEMPLATE_EXCEPTION");
							String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+ICOMM_SJ00047;

							map.put("reporturl",reporturl);
							sjxxCommonService.sendMessageThread(t_sjxxDto, map);
						}
					}
				}
			}
		}
//		if (result == true)
//		{
//			if(sjxxDto.getSyrq()!=null && sjxxDto.getDsyrq()!=null) {
//				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//				try {
//					Date syrq = format.parse(sjxxDto.getSyrq());
//					Date dsyrq = format.parse(sjxxDto.getDsyrq());
//					int compareTo = syrq.compareTo(dsyrq);
//					if(compareTo==1) {
//						checkAnalyseSize(sjxxDto.getSyrq(), sjxxDto);
//					}else {
//						checkAnalyseSize(sjxxDto.getDsyrq(), sjxxDto);
//					}
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}else if(sjxxDto.getSyrq()!=null && sjxxDto.getDsyrq()==null) {
//				checkAnalyseSize(sjxxDto.getSyrq(), sjxxDto);
//			}else if(sjxxDto.getSyrq()==null && sjxxDto.getDsyrq()!=null) {
//				checkAnalyseSize(sjxxDto.getDsyrq(), sjxxDto);
//			}
//			
//		}
		return result;
	}

	/**
	 * 实现Statis接口
	 */
	@Override
	public ModelAndView getStatisPage()
	{
		// TODO Auto-generated method stub
		// 地图显示最近一周的时间
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6); // 得到前六天
		Date weekdate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String weektoday = df.format(weekdate);
		String week = weektoday + "~" + date;
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_list");
		mav.addObject("weektime", week);
		return mav;
	}

	/**
	 * 按照送检单位统计个数。点击后按照送检医生排名
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisBysjhb(SjxxDto sjxxDto)
	{
		return dao.getStatisBysjhb(sjxxDto);
	}
	/**
	 * 按照送检单位统计个数。点击后按照送检医生排名(接收日期)
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisBysjhbAndJsrq(SjxxDto sjxxDto)
	{
		return dao.getStatisBysjhbAndJsrq(sjxxDto);
	}

	@Override
	public List<Map<String, String>> getStatisWzSf(SjxxDto sjxxDto) {
		return dao.getStatisWzSf(sjxxDto);
	}

	/**
	 * 按照周送检单位统计个数。点击后按照送检医生排名
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisWeekBysjhb(SjxxDto sjxxDto)
	{
		return dao.getStatisWeekBysjhb(sjxxDto);
	}
	/**
	 * 按照周送检单位统计个数。点击后按照送检医生排名(接收日期)
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisWeekBysjhbAndJsrq(SjxxDto sjxxDto)
	{
		return dao.getStatisWeekBysjhbAndJsrq(sjxxDto);
	}

	/**
	 * 根据日期查询每天标本数量
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByDate(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub

		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getStatisYblxByJsrq(sjxxDto);
	}
	/**
	 * 根据日期查询每天标本数量(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByDateAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getStatisYblxByJsrqAndJsrq(sjxxDto);
	}

	/**
	 * 根据标本类型统计标本信息(按照日期)
	 *
	 * @return
	 */
	public List<Map<String, String>> getStatisByYblx(SjxxDto sjxxDto)
	{
		return dao.getStatisByYblx(sjxxDto);
	}
	/**
	 * 根据标本类型统计标本信息(按照日期)(接收日期)
	 *
	 * @return
	 */
	public List<Map<String, String>> getStatisByYblxAndJsrq(SjxxDto sjxxDto)
	{
		return dao.getStatisByYblxAndJsrq(sjxxDto);
	}

	/**
	 * 按照合作伙伴，查找所属的送检单位的送检信息
	 *
	 * @return
	 */
	public List<Map<String, String>> getSjdwBydb(SjxxDto sjxxDto)
	{
		return dao.getSjdwBydb(sjxxDto);
	}

	/**
	 * 高关注度
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWzxxByGgzd(SjxxDto sjxxDto)
	{
		return dao.getWzxxByGgzd(sjxxDto);
	}

	/**
	 * 统计结果类型为高关，疑似以及无的标本数量
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getYbnumByJglx(SjxxDto sjxxDto)
	{
		return dao.getYbnumByJglx(sjxxDto);
	}

	/**
	 * 发送统计连接给送检人员
	 *
	 * @return
	 */
	@Override
	public boolean sendStatis()
	{
		// TODO Auto-generated method stub
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("STATIS_TYPE");
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		String jsrqstart;
		String jsrqend;
		int dayOfWeek = DateUtils.getWeek(new Date());
		if (dayOfWeek <= 2){
			jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd");
			jsrqend = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd");
		} else{
			jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd");
			jsrqend = DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd");
		}
		String sign = null;
		try
		{
			sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(jsrqstart + jsrqend), "UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 内网访问
		String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/statistics/weekLeadStatisPageByJsrq%3Fjsrqstart%3D" + jsrqstart + "%26jsrqend%3D" + jsrqend + "%26sign%3D" + sign;
		// 外网访问
		//String external = externalurl + "/common/view/displayView?view_url=/ws/statistics/weekLeadStatisPage%3Fjsrqstart%3D" + jsrqstart + "%26jsrqend%3D" + jsrqend + "%26sign%3D" + sign;
		if (ddxxglDtolist != null && ddxxglDtolist.size() > 0)
		{
			String msgTitle = xxglService.getMsg("ICOMM_ZH00003");
			String msgInfo = xxglService.getMsg("ICOMM_ZH00004",jsrqstart + "~~" + jsrqend, DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
			for (int i = 0; i < ddxxglDtolist.size(); i++)
			{
				List<BtnJsonList> btnJsonLists = new ArrayList<>();
				BtnJsonList btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("详细");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				/*btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("外网访问");
				btnJsonList.setActionUrl(external);
				btnJsonLists.add(btnJsonList);*/
				if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid()))
				{
					talkUtil.sendCardMessage(ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(), msgTitle,
							msgInfo, btnJsonLists, "1");
				}
			}
		}
		return true;
	}

	/**
	 * 发送日报链接给送检人员
	 *
	 * @return
	 */
	@Override
	public boolean sendStatisByDay()
	{
		// TODO Auto-generated method stub
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("DALIY_TYPE");
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setTime(date);
		// 更改发送当日的信息
		// calendar.add(Calendar.DATE,-1);
		date = calendar.getTime();
		String jsrq = sdf.format(date);
		String sign = commonService.getSign(jsrq);
		try
		{
			sign = URLEncoder.encode(sign, "UTF-8");
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (Exception e)
		{

		}
		// 内网访问
		String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/statistics/getDaily%3Fjsrq%3D" + jsrq + "%26sign%3D" + sign;
		// 外网访问
		//String external = externalurl + "/common/view/displayView?view_url=/ws/statistics/getDaily%3Fjsrq%3D" + jsrq + "%26sign%3D" + sign;
		if (ddxxglDtolist != null && ddxxglDtolist.size() > 0)
		{
			for (int i = 0; i < ddxxglDtolist.size(); i++)
			{
				List<BtnJsonList> btnJsonLists = new ArrayList<>();
				BtnJsonList btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("详细");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				/*btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("外网访问");
				btnJsonList.setActionUrl(external);
				btnJsonLists.add(btnJsonList);*/
				if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid()))
				{
					talkUtil.sendCardMessage(ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(), xxglService.getMsg("ICOMM_ZH00008"), xxglService.getMsg("ICOMM_ZH00009", jsrq), btnJsonLists, "1");
				}
			}
		}
		return true;
	}

	public boolean sendStatisByWxid()
	{
//		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtil.isBlank(kpi_templateid))
		{
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int dayOfWeek = DateUtils.getWeek(new Date());
		cal.set(Calendar.DAY_OF_WEEK, 1);
		if (dayOfWeek <= 2)
		{
			cal.add(Calendar.DAY_OF_YEAR, -7);
		}
		String startdate = DateUtils.format(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, 7);
		String enddate = DateUtils.format(cal.getTime());

		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setJsrqstart(startdate);
		sjxxDto.setJsrqend(enddate);
		List<WxyhDto> wxyhDtos = wxyhService.selectDtoByJsrq(sjxxDto);
		if (wxyhDtos != null && wxyhDtos.size() > 0)
		{
			for (int i = 0; i < wxyhDtos.size(); i++)
			{
				String reporturl = menuurl + "/wechat/packging_weekly?jsrqstart=" + startdate + "&jsrqend=" + enddate;
				try
				{
					String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(startdate + enddate + wxyhDtos.get(i).getWxid()), "UTF-8"), "UTF-8");
					reporturl = reporturl + "&lrry=" + URLEncoder.encode(URLEncoder.encode(wxyhDtos.get(i).getWxid(), "UTF-8"), "UTF-8") + "&sign=" + sign;
				} catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("sendWeekUserReport  转译异常！" + e.toString());
				}
				// 临时测试 start
				if (i > 3){
					return true;
				}
				// 临时测试 end  wxyhDtos.get(i).getWxid()
//					Map<String, String> messageMap = new HashMap<>();
//					messageMap.put("title", "您好！");
//					messageMap.put("keyword1", startdate + " ~ " + enddate);
//					messageMap.put("keyword2", "请点击查看");
//					messageMap.put("keyword3", null);
//					messageMap.put("keyword4", null);
//					messageMap.put("reporturl", reporturl);
//					messageMap.put("remark", "送检统计已生成，快点击查看吧！");
//					commonService.sendWeChatMessageMap("templatedm","oj0QJ58d0HnbQ_xraXs0yrOR5clA",null,messageMap);
				commonService.sendWeChatMessage(kpi_templateid, "oj0QJ58d0HnbQ_xraXs0yrOR5clA", "您好！", startdate + " ~ " + enddate, "请点击查看", null, null, "送检统计已生成，快点击查看吧！", reporturl);
//				map.put("status", "success");
			}
		} else
		{
			return false;
		}
		return true;
	}

	/**
	 * 查询当天录入数量,标本数量,报告数量,高关,低关数量
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String, Object> getYbslByday(SjxxDto sjxxDto) {
		Map<String, Object> map = new HashMap<>();
		String date;
		if (StringUtil.isNotBlank(sjxxDto.getJsrq())){
			date = sjxxDto.getJsrq();
		}else {
			date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		//获取ResFirst™检测项目参数ID
		String jcxm ="EEDE5E591AAF4958ABD41820EA16C393";
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		for (JcsjDto jcsjDto : jcsjDtos) {
			if ("IMP_REPORT_RFS_TEMEPLATE".equals(jcsjDto.getCskz3())){
				jcxm = jcsjDto.getCsid();
			}
		}
		//统计检测项目为不等于ResFirst™的
		sjxxDto.setJcxm(jcxm);
		// 标本数量
		sjxxDto.setJsrq(date);
		Map<String, Object> dtybsl = dao.getYbslByday(sjxxDto);
		map.put("dtybsl", dtybsl);
		// 报告数量
		SjxxDto sjxxDtobg = new SjxxDto();
		sjxxDtobg.setBgrq(date);
		//统计检测项目为不等于ResFirst™的
		sjxxDtobg.setJcxm(jcxm);
		Map<String, Object> dtbgsl = dao.getYbslByday(sjxxDtobg);
		map.put("dtbgsl", dtbgsl);
		// 高关数和低关数
		List<Map<String, String>> dtggdgsl = dao.getYbnumByJglx(sjxxDtobg);
		map.put("dtggdgsl", dtggdgsl);
		// 今日录入数
		sjxxDto.setLrsj(date);
		Map<String, Object> dtlrsl = dao.getLrYbslByday(sjxxDto);
		map.put("dtlrybsl", dtlrsl);
		// 今日实验数
		Map<String, String> symap = new HashMap<>();
		symap.put("num", dao.getJcbjBySyrq(sjxxDto) + "");
		map.put("symap", symap);
		//未发报告数
		//统计检测项目为不等于ResFirst™的
		SjxxDto wfbgSjxxDto = new SjxxDto();
		wfbgSjxxDto.setJcxm(jcxm);
		wfbgSjxxDto.setJsrq(date);
		Map<String, String> wfbgmap = new HashMap<>();
		wfbgmap.put("num", this.querysjxxnumByRq(wfbgSjxxDto) + "");
		map.put("wfbgmap", wfbgmap);
		//统计检测项目为等于ResFirst™的
		//ResFirst™当日情况
		Map<String, String> rmap = sjxxResStatisticService.getRYbStatisByDay(sjxxDto);
		map.put("rmap",rmap);
		return map;
	}
	/**
	 * 获取未发报告总数
	 * @param sjxxDto
	 * @return
	 */
	public Integer querysjxxnumByRq(SjxxDto sjxxDto) {
		Calendar calendar = Calendar.getInstance();
		//接收日期不等于空并且当前日期不等于 日期参数
		if (StringUtil.isNotBlank(sjxxDto.getJsrq())&&!DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd").equals(sjxxDto.getJsrq())){
			String jsrq = sjxxDto.getJsrq();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				//获取当天晚上的数据
				Date parse = format.parse(jsrq+" 23:59:59");
				calendar.setTime(parse);
			} catch (ParseException e) {
				e.printStackTrace();
				log.error("querysjxxnumByRq  日期解析异常！" + e.toString());
			}
		}else {
			calendar.setTime(new Date());
		}
		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if (StringUtil.isNotBlank(sjxxDto.getJsrq())){
			sjxxDto.setBgrq(sjxxDto.getJsrq());
		}else {
			String bgrq = format.format(new Date());
			sjxxDto.setBgrq(bgrq);
		}

		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());

		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);

		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		String cxkssj = formatHH.format(calendar.getTime());
		calendar.add(Calendar.DATE, 1);
		String lrsjEnd = formatHH.format(calendar.getTime());

		sjxxDto.setSyrq(syrq);
		sjxxDto.setDsyrq(syrq);

		sjxxDto.setLrsj(cxkssj);
		sjxxDto.setLrsjStart(cxkssj);
		sjxxDto.setLrsjEnd(lrsjEnd);

		sjxxDto.setSyrqstart(syrqStart);
		sjxxDto.setSyrqend(syrqEnd);

		return dao.querysjxxnum(sjxxDto);
	}
	/**
	 * 统计每天标本的阳性率
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisYxlBybgrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getStatisYxlBybgrq(sjxxDto);
	}

	/**
	 * 统计每周的标本数变化情况
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisWeekYbsByJsrq(SjxxDto sjxxDto)
	{
		List<Map<String, String>> listMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			return dao.getStatisWeekYbsByJsrq(sjxxDto);
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}
	/**
	 * 统计每周的标本数变化情况(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisWeekYbsByJsrqAndJsrq(SjxxDto sjxxDto)
	{
		List<Map<String, String>> listMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			return dao.getStatisWeekYbsByJsrqAndJsrq(sjxxDto);
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}

	/**
	 * 查询送检报告完成度
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String, Object> getBgWcd(SjxxDto sjxxDto)
	{
		Map<String, Object> map = new HashMap<>();
//		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		// 查询前一天进行了实验的标本数量
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DATE, -1); // 得到前一天
//		Date yestdate = calendar.getTime();
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		String yestoday = df.format(yestdate);
//		SjxxDto sjxxwcd = new SjxxDto();
//		sjxxwcd.setSyrq(yestoday);
//		sjxxwcd.setDsyrq(yestoday);
//		sjxxwcd.setQtsyrq(yestoday);
//		Map<String, Object> sjxxwad = dao.getybwcd(sjxxwcd);
//		map.put("sjxxqytybs", sjxxwad);
//		SjxxDto t_sjxxDto = new SjxxDto();
//		// 前一天进行实验的标本数量
//		t_sjxxDto.setSyrq(yestoday);
//		t_sjxxDto.setBgrqbj("1");
//		Map<String, Object> ztsdybsl = dao.getYbslByday(t_sjxxDto);
//		map.put("sjxxwad", ztsdybsl);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		Date nowDate = calendar.getTime();

		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());

		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());

		String bgrq = format.format(nowDate);
		SjxxDto sjxx = new SjxxDto();
		sjxx.setSyrq(syrq);
		sjxx.setDsyrq(syrq);

		sjxx.setSyrqstart(syrqStart);
		sjxx.setSyrqend(syrqEnd);
		Map<String, List<JcsjDto>> jcsjList =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK});
		List<JcsjDto> flsqList=jcsjList.get(BasicDataTypeEnum.RECHECK.getCode());
		if(flsqList!=null && flsqList.size()>0) {
			List<String> strList= new ArrayList<>();
			for (JcsjDto jcsjDto:flsqList) {
				//复测的才进行
				if("RECHECK".equals(jcsjDto.getCsdm())) {
					strList.add(jcsjDto.getCsid());
				}
			}
			sjxx.setFjlxs(strList);
		}
		sjxx.setBgrq(bgrq);
		Map<String,String> ywcbgsmap = dao.getYwcbgs(sjxx);
		int wfbgs=ywcbgsmap.get("wfbgs")==null?0:Integer.valueOf(String.valueOf(ywcbgsmap.get("wfbgs")));
		int fcs=ywcbgsmap.get("fcs")==null?0:Integer.valueOf(String.valueOf(ywcbgsmap.get("fcs")));
		int bgs=ywcbgsmap.get("bgs")==null?0:Integer.valueOf(String.valueOf(ywcbgsmap.get("bgs")));
		Map<String, Object> sjxxqytybs = new HashMap<>();
		sjxxqytybs.put("num",fcs+bgs+wfbgs);
		Map<String, Object> sjxxwad =new HashMap<>();
		sjxxwad.put("num",fcs+bgs);
		map.put("sjxxqytybs", sjxxqytybs);
		map.put("sjxxwad", sjxxwad);
		return map;
	}

	/**
	 * 获取最近一周标本送检路线
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, Object>> getSjxlxx(SjxxDto sjxxDto)
	{
		return dao.getSjxlxx(sjxxDto);
	}

	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxBySy(SjxxDto sjxxDto)
	{
		List<String> rqs = new ArrayList<>();
		if ("week".equals(sjxxDto.getTj())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		}else {
			rqs = getRqsByStartAndEnd(sjxxDto);
		}
		sjxxDto.setRqs(rqs);
		List<Map<String, String>> resultMaps = dao.getSjxxBySy(sjxxDto);

		String s_preRq = "";
		long sum = 0, presum = 0;
		for (int i = 0; i < resultMaps.size(); i++)
		{
			// 日期有变化
			if (!s_preRq.equals(resultMaps.get(i).get("rq")))
			{
				if (presum == 0 && i != 0)
				{
					resultMaps.get(i - 1).put("bl", "0");
				} else if (presum != 0)
				{
					BigDecimal bg_sum = new BigDecimal(presum);
					resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
				}

				presum = sum;

				s_preRq = resultMaps.get(i).get("rq");

				if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum = Integer.parseInt(resultMaps.get(i).get("num"));
				} else
				{
					sum = 0;
				}
			} else if ("1".equals(resultMaps.get(i).get("sfjs")))
			{
				sum += Long.parseLong(resultMaps.get(i).get("num"));
			}

		}
		if (presum == 0 && resultMaps.size() > 0)
		{
			resultMaps.get(resultMaps.size() - 1).put("bl", "0");
		} else if (presum != 0)
		{
			BigDecimal bg_sum = new BigDecimal(presum);
			resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
		}

		return resultMaps;
	}
	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxBySyAndJsrq(SjxxDto sjxxDto)
	{
		List<String> rqs = new ArrayList<>();
		if ("week".equals(sjxxDto.getTj())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		}else {
			rqs = getRqsByStartAndEnd(sjxxDto);
		}

		sjxxDto.setRqs(rqs);
		List<Map<String, String>> resultMaps = dao.getSjxxBySyAndJsrq(sjxxDto);

		String s_preRq = "";
		long sum = 0, presum = 0;
		for (int i = 0; i < resultMaps.size(); i++)
		{
			// 日期有变化
			if (!s_preRq.equals(resultMaps.get(i).get("rq")))
			{
				if (presum == 0 && i != 0)
				{
					resultMaps.get(i - 1).put("bl", "0");
				} else if (presum != 0)
				{
					BigDecimal bg_sum = new BigDecimal(presum);
					resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
				}

				presum = sum;

				s_preRq = resultMaps.get(i).get("rq");

				if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum = Integer.parseInt(resultMaps.get(i).get("num"));
				} else
				{
					sum = 0;
				}
			} else if ("1".equals(resultMaps.get(i).get("sfjs")))
			{
				sum += Long.parseLong(resultMaps.get(i).get("num"));
			}

		}
		if (presum == 0 && resultMaps.size() > 0)
		{
			resultMaps.get(resultMaps.size() - 1).put("bl", "0");
		} else if (presum != 0)
		{
			BigDecimal bg_sum = new BigDecimal(presum);
			resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
		}

		return resultMaps;
	}

	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxWeekBySy(SjxxDto sjxxDto)
	{
		List<Map<String, String>> listMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getSjxxWeekBySy(sjxxDto);

			String s_preRq = "";
			int sum = 0, presum = 0;
			for (int i = 0; i < resultMaps.size(); i++)
			{
				// 日期有变化
				if (!s_preRq.equals(resultMaps.get(i).get("rq")))
				{
					if (presum == 0 && i != 0)
					{
						resultMaps.get(i - 1).put("bl", "0");
					} else if (presum != 0)
					{
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
					}

					presum = sum;
					s_preRq = resultMaps.get(i).get("rq");

					if ("1".equals(resultMaps.get(i).get("sfjs")))
					{
						sum = Integer.parseInt(resultMaps.get(i).get("num"));
					} else
					{
						sum = 0;
					}
				} else if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum += Integer.parseInt(resultMaps.get(i).get("num"));
				}

			}
			if (presum == 0 && resultMaps.size() > 0)
			{
				resultMaps.get(resultMaps.size() - 1).put("bl", "0");
			} else if (presum != 0)
			{
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
			}

			return resultMaps;
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}
	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxWeekBySyAndJsrq(SjxxDto sjxxDto)
	{
		List<Map<String, String>> listMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getSjxxWeekBySyAndJsrq(sjxxDto);

			String s_preRq = "";
			int sum = 0, presum = 0;
			for (int i = 0; i < resultMaps.size(); i++)
			{
				// 日期有变化
				if (!s_preRq.equals(resultMaps.get(i).get("rq")))
				{
					if (presum == 0 && i != 0)
					{
						resultMaps.get(i - 1).put("bl", "0");
					} else if (presum != 0)
					{
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
					}

					presum = sum;
					s_preRq = resultMaps.get(i).get("rq");

					if ("1".equals(resultMaps.get(i).get("sfjs")))
					{
						sum = Integer.parseInt(resultMaps.get(i).get("num"));
					} else
					{
						sum = 0;
					}
				} else if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum += Integer.parseInt(resultMaps.get(i).get("num"));
				}

			}
			if (presum == 0 && resultMaps.size() > 0)
			{
				resultMaps.get(resultMaps.size() - 1).put("bl", "0");
			} else if (presum != 0)
			{
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
			}

			return resultMaps;
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}

	/**
	 * 外部发送消息
	 *
	 * @param sjxxDto
	 * @param message
	 * @return
	 */
	@Override
	public boolean sendOutMessage(SjxxDto sjxxDto, String message, String title) throws BusinessException {
		// TODO Auto-generated method stub
		try {
			sjxxDto = dao.getDto(sjxxDto);
			boolean sendFlg = false;
			// 发送钉钉消息
			// 查询接收人员列表
			sjxxDto.setPdflx(sjxxDto.getCskz3() + "_" + sjxxDto.getCskz1());
			sjxxDto.setWordlx(sjxxDto.getCskz3() + "_" + sjxxDto.getCskz1() + "_WORD");

			RestTemplate t_restTemplate = new RestTemplate();

			List<SjxxDto> sjxxDtos = dao.getReceiveUserByDb(sjxxDto);
			if (sjxxDtos != null && sjxxDtos.size() > 0) {
				for (int i = 0; i < sjxxDtos.size(); i++) {
					SjxxDto t_SjxxDto = sjxxDtos.get(i);
					// 如果邮箱不为空发送邮件
					if(StringUtil.isNotBlank(t_SjxxDto.getYx())){
						sendFlg = true;
						List<String> yxlist = new ArrayList<>();
						yxlist = StringUtil.splitMsg(yxlist, t_SjxxDto.getYx(), "，|,");
						emailUtil.sendEmail(yxlist, title, message);
					}
					// 如果微信不为空，则微信通知
					if (StringUtil.isNotBlank(t_SjxxDto.getHbcskz1())) {
						sendFlg = true;
						// templateid
						MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
						paramMap.add("wxid", null);
						paramMap.add("ybbh", t_SjxxDto.getYbbh());
						paramMap.add("title", title);
						paramMap.add("message", message);
						paramMap.add("bgrq", t_SjxxDto.getBgrq());
						// 让服务器发送信息到相应的微信里
						String[] arrWxid = t_SjxxDto.getHbcskz1().split("，|,");
						for (int j = 0; j < arrWxid.length; j++) {
							paramMap.set("wxid", arrWxid[j]);
							t_restTemplate.postForObject(menuurl + "/wechat/sendOutMessage", paramMap, String.class);
						}
					}
					// 如果合作伙伴类型为1（直销）的情况，则为公司员工，则通过钉钉进行通知
					if ("1".equals(t_SjxxDto.getHblx()) && StringUtil.isNotBlank(t_SjxxDto.getDdid())) {
						talkUtil.sendWorkMessage(t_SjxxDto.getYhm(), t_SjxxDto.getDdid(), title, message);
						sendFlg = true;
					}
				}
				if (!sendFlg) {
					// 查询伙伴为'无'的钉钉ID
					List<SjhbxxDto> sjhbxxDto = sjhbxxService.getXtyhByHbmc();
					for (int i = 0; i < sjxxDtos.size(); i++) {
						talkUtil.sendWorkMessage(sjhbxxDto.get(i).getYhm(), sjhbxxDto.get(i).getDdid(), title, message);
					}
				}
				return true;
			}
		} catch (Exception e) {
			log.error("sendOutMessage: " + e.toString());
			throw new BusinessException("msg", e.toString());
		}
		return false;
	}

	/**
	 * 本地发送多条消息
	 *
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	public boolean sendMessage(SjxxDto sjxxDto) throws BusinessException
	{
		String title = sjxxDto.getBt();/* xxglService.getModelById("ICOMM_ZH00005").getXxnr() */
		String message = sjxxDto.getNr();/* xxglService.getModelById("ICOMM_ZH00006").getXxnr() */
		if (sjxxDto.getIds() != null && sjxxDto.getIds().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getIds().size(); i++)
			{
				SjxxDto sjxxDtos = new SjxxDto();
				sjxxDtos.setYbbh(sjxxDto.getIds().get(i));

				String new_message=replaceMessage(message,sjxxDto.getIds().get(i));
				boolean result = sendOutMessage(sjxxDtos, new_message, title);
				if (!result)
					return false;
			}
		}
		return true;
	}

	/**
	 * 替换发送内容中的关键字
	 * @param message
	 * @param ybbh
	 * @return
	 */
	private String replaceMessage(String message,String  ybbh) {
		SjxxDto sjxxDto=dao.getSJxxForSend(ybbh);
		message=message.replace("{","{").replace("}","}");
		String new_message=message;
		int index=getIndex(message);
		for (int i = 0; i < index; i++) {
			int begin=message.indexOf("{");
			int end=message.indexOf("}");
			if (begin > -1 && end > -1) {
				String tem = message.substring(begin+1, end).toString();
				String TotmpVar =tem.substring(0, 1).toUpperCase() + tem.substring(1);
				// 反射方法 把map的value 插入到数据中
				message=message.substring(end+1);
				try {
					Method t_method = sjxxDto.getClass().getMethod("get" + TotmpVar);
					String s_value =(String) t_method.invoke(sjxxDto);
					new_message=new_message.replace(tem,s_value);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		new_message=new_message.replace("{", "").replace("}", "");
		return new_message;
	}

	/**
	 * 查看关键字“«” 出现的次数
	 *
	 * @param text
	 * @return
	 */
	public int getIndex(String text) {
		int count = 0;
		char param = '{';
		for (int i = 0; i < text.length(); i++) {
			char cm = text.charAt(i);
			if (cm == param) {
				count++;
			}
		}
		return count;
	}
	/**
	 * 保存详细审核结果
	 *
	 * @param str
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean receiveDetailedReport(String str)
	{
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(str))
		{
			WeChatDetailedReportModel details = JSONObject.parseObject(str, WeChatDetailedReportModel.class);
			if(StringUtil.isBlank(details.getYbbh())){
				return false;
			}
			// 查询是否有此标本信息
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setYbbh(details.getYbbh());
			SjxxDto t_sjxxDto = dao.getDto(sjxxDto);
			// 默认检测类型为DNA
			if (StringUtil.isBlank(details.getDetection_type()))
			{
				details.setDetection_type("D");
			}
			if (t_sjxxDto != null)
			{
				Map<String, Map<String, WeChatDetailedResultModel>> map = details.getResult();
				List<SjxxjgDto> sjxxjgDtos = new ArrayList<>();
				int i = 0;
				if(map!=null && map.size() > 0 ) {
					for (Entry<String, Map<String, WeChatDetailedResultModel>> entrys : map.entrySet())
					{
						if(entrys.getValue().size() > 0) {
							for (Entry<String, WeChatDetailedResultModel> entry : entrys.getValue().entrySet())
							{
								SjxxjgDto sjxxjgDto = new SjxxjgDto();
								sjxxjgDto.setXxjgid(StringUtil.generateUUID());
								sjxxjgDto.setXh(String.valueOf(i));
								sjxxjgDto.setSjid(t_sjxxDto.getSjid());
								sjxxjgDto.setJdid(entry.getKey());
								sjxxjgDto.setFjdid(entry.getValue().getParent());
								sjxxjgDto.setWzywm(entry.getValue().getName());
								sjxxjgDto.setWzzwm(entry.getValue().getCn_name());
								sjxxjgDto.setWzfl(entrys.getKey());
								sjxxjgDto.setFldj(entry.getValue().getRank());
								sjxxjgDto.setDqs(entry.getValue().getReads_count());
								sjxxjgDto.setZdqs(entry.getValue().getReads_accum());
								sjxxjgDto.setDwds(entry.getValue().getRpm());
								sjxxjgDto.setDsbfb(entry.getValue().getRatio());
								sjxxjgDto.setJyzfgd(entry.getValue().getCoverage());
								sjxxjgDto.setSm(entry.getValue().getGenus_name());
								sjxxjgDto.setSdds(entry.getValue().getGenus_reads_count());
								sjxxjgDto.setXpjcl(entry.getValue().getFrequency());
								sjxxjgDto.setGzd(entry.getValue().getInterest());
								sjxxjgDto.setXdfd(entry.getValue().getAbundance());
								sjxxjgDto.setJclx(details.getDetection_type());
								sjxxjgDto.setWzfllx(entry.getValue().getVirus_type());
								sjxxjgDto.setXmdm(details.getXmdm());
								sjxxjgDto.setWkbh(details.getSample_id());
								sjxxjgDto.setXpmc(details.getChip_uid());
								sjxxjgDtos.add(sjxxjgDto);
								i++;
							}
						}else {
							log.error("receiveDetailedReport 接口：entrys.getValue  为0。");
						}
					}
				}else {
					log.error("receiveDetailedReport 接口：map 为0。");
				}
				if (sjxxjgDtos != null && sjxxjgDtos.size() > 0)
				{
					// 因为是采用送检ID的，所以只需要取一条数据即可
					if((sjxxjgDtos.get(0).getWkbh().indexOf("NC")!=-1)||(sjxxjgDtos.get(0).getWkbh().indexOf("PC")!=-1)){
						sjxxjgDtos.get(0).setScflag("1");
					}
					sjxxjgService.deleteBySjxxjgDto(sjxxjgDtos.get(0));
					boolean result = sjxxjgService.insertBySjxxjgDtos(sjxxjgDtos);
					if (!result)
						return false;
					// 同步信息至微信服务器
					if(StringUtil.isNotBlank(menuurl)) {
						amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.DETAILED_INSPECTION.getCode(), JSONObject.toJSONString(sjxxjgDtos));
					}
				} else
				{
					log.error(" receiveDetailedReport 没有数据：" + str);
					SjxxjgDto sjxxjgDto=new SjxxjgDto();
					sjxxjgDto.setSjid(t_sjxxDto.getSjid());
					sjxxjgDto.setJclx(details.getDetection_type());
					sjxxjgDto.setXmdm(details.getXmdm());
					sjxxjgDtos.add(sjxxjgDto);
					sjxxjgService.deleteBySjxxjgDto(sjxxjgDtos.get(0));
					// 同步信息至微信服务器
					if(StringUtil.isNotBlank(menuurl)) {
						amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.DETAILED_INSPECTION.getCode(), JSONObject.toJSONString(sjxxjgDtos));
					}
				}
			} else
			{
				// 不存在此标本，将数据加入错误信息表
				CwglDto cwglDto = new CwglDto();
				cwglDto.setCwlx(ErrorTypeEnum.INSPECTION_DETAILED_TYPE.getCode());
				if (str.length() > 4000)
				{
					str = str.substring(0, 4000);
				}
				cwglDto.setYsnr(str);
				cwglService.insertDto(cwglDto);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 日报统计
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByDay(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxByDay(sjxxDto);
	}
	/**
	 * 日报统计(接收日期)
	 *
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByDayAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxByDayAndJsrq(sjxxDto);
	}

	/**
	 * 按照合作伙伴统计
	 */
	@Override
	public List<Map<String, String>> getYbxxByDb(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> dbList = dao.getDb(sjxxDto.getJsrq());
		List<Map<String, String>> sjhbList = null;
		if (dbList != null && dbList.size() > 0)
		{
			sjxxDto.setSjhbs(dbList);
			sjhbList = dao.getYbxxByDb(sjxxDto);
		}
		return sjhbList;
	}

	/**
	 * 按照统计名称分类统计
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByTjmc(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> dbList = dao.getTjmc(sjxxDto);
		List<Map<String, String>> sjhbList = null;
		if (dbList != null && dbList.size() > 0)
		{
			sjxxDto.setSjhbs(dbList);
			sjhbList = dao.getYbxxByTjmc(sjxxDto);
		}
		return sjhbList;
	}
	/**
	 * 按照统计名称分类统计(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByTjmcAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> dbList = dao.getTjmcByJsrq(sjxxDto);
		List<Map<String, String>> sjhbList = null;
		if (dbList != null && dbList.size() > 0)
		{
			sjxxDto.setSjhbs(dbList);
			sjhbList = dao.getYbxxByTjmcAndJsrq(sjxxDto);
		}
		return sjhbList;
	}

	/**
	 * 修改参数扩展
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateCskz(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		boolean success = dao.updateCskz(sjxxDto);
		if (success){
			SjxxDto rabbitSjxxDto = new SjxxDto();
			rabbitSjxxDto.setSfsf(sjxxDto.getSfsf());
			rabbitSjxxDto.setXgry(sjxxDto.getXgry());
			rabbitSjxxDto.setIds(sjxxDto.getIds());
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.CSKZ_UPD.getCode() + JSONObject.toJSONString(rabbitSjxxDto));
		}
		return success;
	}

	/**
	 * 查询扩展参数
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getSjxxCskz(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSjxxCskz(sjxxDto);
	}

	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedTlxxList(SjxxDto sjxxDto)
	{
		return dao.getPagedTlxxList(sjxxDto);
	}

	/**
	 * 根据送检ids查询送检物种信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjwzxxDto> selectWzxxBySjids(SjxxDto sjxxDto)
	{
		return dao.selectWzxxBySjids(sjxxDto);
	}

	/**
	 * 根据ids查询送检信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> selectSjxxBySjids(SjxxDto sjxxDto)
	{
		return dao.selectSjxxBySjids(sjxxDto);
	}

	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息(不分页)
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getTlxxList(SjxxDto sjxxDto)
	{
		return dao.getTlxxList(sjxxDto);
	}

	/**
	 * 查询内部编码是不是已经存在
	 *
	 * @param sjxxDto
	 * @return
	 */
	public Integer getCountBynbbm(SjxxDto sjxxDto)
	{
		return dao.getCountBynbbm(sjxxDto);
	}

	/**
	 * 根据伙伴分类，伙伴子分类，代表名查询送检信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String, List<Map<String, String>>> getStatisByFl(SjxxDto sjxxDto)
	{

		// TODO Auto-generated method stub
		// 设置合作伙伴
		setDbsByStartAndEnd(sjxxDto);
		if(sjxxDto.getDbs() == null || sjxxDto.getDbs().size() ==0)
		{
			return null;
		}
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByFl(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return setReceiveId(fllist, "fl");
	}

	/**
	 * 查询分类下伙伴信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByHbfl(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		// 设置合作伙伴
		setDbsByStartAndEnd(sjxxDto);
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByFl(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}

	/**
	 * 查询分类下统计名称伙伴信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByTjHbfl(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		// 设置合作伙伴
		setTjsByStartAndEnd(sjxxDto);
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByTjFl(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	/**
	 * 查询分类下统计名称伙伴信息(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByTjHbflAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		// 设置合作伙伴
		setTjsByStartAndEnd(sjxxDto);
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByTjFlAndJsrq(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}

	/**
	 * 设置返回对应ID
	 *
	 * @param fllist
	 * @param name
	 * @return
	 */
	@Override
	public Map<String, List<Map<String, String>>> setReceiveId(List<Map<String, String>> fllist, String name)
	{
		if (fllist == null)
			return null;
		Map<String, List<Map<String, String>>> flmap = new HashMap<>();
		for (int i = 0; i < fllist.size(); i++)
		{
			String csdm = fllist.get(i).get(name);
			if(StringUtil.isBlank(csdm)) {
				if(flmap.get("default") == null) {
					List<Map<String, String>> t_list = new ArrayList<>();
					flmap.put("default", t_list);
				}
				flmap.get("default").add(fllist.get(i));
			}else {
				if (flmap.get(csdm) == null)
				{
					List<Map<String, String>> t_list = new ArrayList<>();
					flmap.put(csdm, t_list);
				}
				flmap.get(csdm).add(fllist.get(i));
			}
		}
		return flmap;
	}

	/**
	 * 根据周的伙伴分类，伙伴子分类，代表名查询送检信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	public Map<String, List<Map<String, String>>> getStatisByWeekFl(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setDbsByStartAndEnd(sjxxDto);
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByWeekFl(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return setReceiveId(fllist, "fl");
	}

	/**
	 * 查询分类下伙伴信息(周)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByWeekHbfl(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setDbsByStartAndEnd(sjxxDto);
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByWeekFl(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}

	/**
	 * 查询分类下统计名称的伙伴信息(周)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByWeekTjHbfl(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setTjsByStartAndEnd(sjxxDto);
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByWeekTjFl(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}

	/**
	 * 查询分类下统计名称的伙伴信息(周)(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByWeekTjHbflAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setTjsByStartAndEnd(sjxxDto);
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByWeekTjFlAndJsrq(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}

	/**
	 * 查询标本编号是不是已经存在
	 *
	 * @param sjxxDto
	 * @return
	 */
	public Integer getCountByybbh(SjxxDto sjxxDto)
	{
		return dao.getCountByybbh(sjxxDto);
	}
	/**
	 * 验证标本编号以及外部编码是不是已经存在
	 * @return
	 */
	public Integer getCountByWbbmAndYbbh(SjxxDto sjxxDto)
	{
		return dao.getCountByWbbmAndYbbh(sjxxDto);
	}

	/**
	 * 根据接收日期的开始日期和结束日期，自动计算每一天的日期，形成一个list
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<String> getRqsByStartAndEnd(SjxxDto sjxxDto)
	{
		List<String> rqs = new ArrayList<>();
		try
		{
			if ("day".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
				} else
				{
					endcalendar.setTime(new Date());
				}
				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			} else if ("mon".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqMstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqMend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqMend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}

			} else if ("year".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqYstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqYend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqYend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return rqs;
	}

	/**
	 * 查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getBbsList(SjxxDto sjxxDto){
		return dao.getBbsList(sjxxDto);
	}
	/**
	 * 查询送检信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getBbsListByJsrq(SjxxDto sjxxDto){
		return dao.getBbsListByJsrq(sjxxDto);
	}

	/**
	 * 按照报告日期统计送检报告个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisSjbgByBgrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> listMap = new ArrayList<>();
		try
		{
			List<String> rqs = new ArrayList<>();
			if ("day".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getBgrqstart()));
				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getBgrqend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getBgrqend()));
				} else
				{
					endcalendar.setTime(new Date());
				}
				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			} else if ("mon".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getBgrqMstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getBgrqMend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getBgrqMend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}
			} else if ("year".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getBgrqYstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getBgrqYend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getBgrqYend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}

			sjxxDto.setRqs(rqs);
			listMap = dao.getStatisSjbgByBgrq(sjxxDto);
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}

	/**
	 * 根据分类和子分类信息，自动获取合作伙伴信息
	 *
	 * @param sjxxDto
	 */
	private void setDbsByStartAndEnd(SjxxDto sjxxDto)
	{

		SjhbxxDto sjhbxxDto = new SjhbxxDto();
		List<String> dbs = new ArrayList<>();
		if (StringUtil.isNotBlank(sjxxDto.getDb()))
		{
			dbs.add(sjxxDto.getDb());
			sjxxDto.setDbs(dbs);
		} else if (StringUtil.isNotBlank(sjxxDto.getHbzfl()))
		{
			sjhbxxDto.setZfl(sjxxDto.getHbzfl());
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getDtoList(sjhbxxDto);
			if (sjhbxxDtos != null && sjhbxxDtos.size() > 0)
			{
				for (int i = 0; i < sjhbxxDtos.size(); i++)
				{
					dbs.add(sjhbxxDtos.get(i).getHbmc());
				}
				sjxxDto.setDbs(dbs);
			}
		} else if (StringUtil.isNotBlank(sjxxDto.getHbfl()))
		{
			sjhbxxDto.setFl(sjxxDto.getHbfl());
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getDtoList(sjhbxxDto);
			if (sjhbxxDtos != null && sjhbxxDtos.size() > 0)
			{
				for (int i = 0; i < sjhbxxDtos.size(); i++)
				{
					dbs.add(sjhbxxDtos.get(i).getHbmc());
				}
				sjxxDto.setDbs(dbs);
			}
		} else
		{
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getDtoList(sjhbxxDto);
			if (sjhbxxDtos != null && sjhbxxDtos.size() > 0)
			{
				for (int i = 0; i < sjhbxxDtos.size(); i++)
				{
					dbs.add(sjhbxxDtos.get(i).getHbmc());
				}
				sjxxDto.setDbs(dbs);
			}
		}
	}

	/**
	 * 根据日期检查送检标本，并自动计算标本量，暂时废弃，标本量计算暂时不执行
	 *
	 * @param date
	 * @return
	 */
	/*private boolean checkAnalyseSize(String date, SjxxDto sjxxdto)
	{
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setSyrq(date);
		sjxxDto.setJcdw(sjxxdto.getJcdw());
		List<SjxxDto> lists = dao.selectInspSize(sjxxDto);
		return checkAnalyseSizeByList(lists, sjxxdto);
	}*/

	/**
	 * 计算标本量，同时根据标本量计算标本的大小，小于8M 和小于5M 要进行提醒
	 *
	 * @param list
	 * @return
	 */
	/*private boolean checkAnalyseSizeByList(List<SjxxDto> list, SjxxDto sjxxdto)
	{

		Map<String, Integer> caclResult = caclSampSize(list);

		int ana_size = caclResult.get("AD") + caclResult.get("AR") + caclResult.get("BD") * 2 + caclResult.get("BR") + caclResult.get("BC") * 3 + caclResult.get("AC") * 2;

		if (ana_size != 0)
		{

			BigDecimal b_totle_size = new BigDecimal(ana_size);
			BigDecimal b_size = new BigDecimal(400);
			BigDecimal b_avg_size = b_size.divide(b_totle_size, 2, BigDecimal.ROUND_HALF_UP);

			double avg_size = b_avg_size.doubleValue();
			double result_AD, result_AR, result_BD, result_BR, result_BC, result_AC;

			result_AD = new BigDecimal(caclResult.get("AD")).multiply(b_avg_size).doubleValue();

			result_AR = new BigDecimal(caclResult.get("AR")).multiply(b_avg_size).doubleValue();

			result_BD = new BigDecimal(caclResult.get("BD")).multiply(new BigDecimal(2)).multiply(b_avg_size).doubleValue();

			result_BR = new BigDecimal(caclResult.get("BR")).multiply(b_avg_size).doubleValue();

			result_AC = new BigDecimal(caclResult.get("AC")).multiply(new BigDecimal(2)).multiply(b_avg_size).doubleValue();

			result_BC = new BigDecimal(caclResult.get("BC")).multiply(new BigDecimal(3)).multiply(b_avg_size).doubleValue();
			if (avg_size < 5)
			{
				// 发送低于5M的警告消息
				// 首先判断是否已经发送
				// 不存在此标本，将数据加入错误信息表
				CwglDto cwglDto = new CwglDto();
				cwglDto.setCwlx(ErrorTypeEnum.INSPECTION_MIN_ERROR.getCode());
				cwglDto.setCwsj(list.get(0).getSyrq());
				cwglDto.setYsnr("警告信息：数据量低于5M，实际为" + avg_size + "M。其中AD:" + caclResult.get("AD") + " 其中AR:" + caclResult.get("AR") + " 其中BD:" + caclResult.get("BD") + " 其中BR:" + caclResult.get("BR")
						+ " 其中AC:" + caclResult.get("AC") + " 其中BC:" + caclResult.get("BC"));

				List<CwglDto> cwList = cwglService.getDtoList(cwglDto);
				if (cwList == null || cwList.size() == 0)
				{
					// 发送钉钉消息
					String token = talkUtil.getToken();
					List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.INSPECTION_MIN_ERROR.getCode());

					String title = xxglService.getMsg("ICOMM_INSP00003");
					String message = xxglService.getMsg("ICOMM_INSP00001",
							String.valueOf(avg_size) + "M (" + (caclResult.get("AD") + caclResult.get("AR") + caclResult.get("BD") + caclResult.get("BR") + caclResult.get("AC") + caclResult.get("BC"))
									+ ")",
							String.valueOf(caclResult.get("BD")) + " (" + result_BD + "M)", String.valueOf(caclResult.get("BR")) + " (" + result_BR + "M)",
							String.valueOf(caclResult.get("BC")) + " (" + result_BC + "M)", String.valueOf(caclResult.get("AD")) + " (" + result_AD + "M)",
							String.valueOf(caclResult.get("AR")) + " (" + result_AR + "M)", String.valueOf(caclResult.get("AC")) + " (" + result_AC + "M)",
							DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					if (ddxxglDtolist != null && ddxxglDtolist.size() > 0)
					{
						for (int i = 0; i < ddxxglDtolist.size(); i++)
						{
							if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid()))
							{
								String syrq = sjxxdto.getSyrq();
								// 内网访问
								String internalbtn = applicationurl + "/ws/inspection/sampleCapacityListPage?syrq=" + syrq;
								// 外网访问
								String external = externalurl + "/ws/inspection/sampleCapacityListPage?syrq=" + syrq;
								List<BtnJsonList> btnJsonLists = new ArrayList<BtnJsonList>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("内网访问");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendWorkMessage("null", ddxxglDtolist.get(i).getDdid(), title, message, btnJsonLists, "1");
							}
						}
					}
					cwglService.insertDto(cwglDto);
					sjxxdto.setCwid(cwglDto.getCwid());//只有标本量小于5M时才提交审核
				}
				return false;
			} else if (avg_size < 8)
			{
				// 发送低于8M的警告消息
				// 首先判断是否已经发送
				// 不存在此标本，将数据加入错误信息表
				CwglDto cwglDto = new CwglDto();
				cwglDto.setCwlx(ErrorTypeEnum.INSPECTION_STAND_ERROR.getCode());
				cwglDto.setCwsj(list.get(0).getSyrq());
				cwglDto.setYsnr("提示信息：数据量低于8M，实际为" + avg_size + "M。其中AD:" + caclResult.get("AD") + " 其中AR:" + caclResult.get("AR") + " 其中BD:" + caclResult.get("BD") + " 其中BR:" + caclResult.get("BR")
						+ " 其中AC:" + caclResult.get("AC") + " 其中BC:" + caclResult.get("BC"));

				List<CwglDto> cwList = cwglService.getDtoList(cwglDto);
				if (cwList == null || cwList.size() == 0)
				{
					// 发送钉钉消息
					String token = talkUtil.getToken();
					List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.INSPECTION_STAND_ERROR.getCode());

					String title = xxglService.getMsg("ICOMM_INSP00003");
					String message = xxglService.getMsg("ICOMM_INSP00002",
							String.valueOf(avg_size) + "M (" + (caclResult.get("AD") + caclResult.get("AR") + caclResult.get("BD") + caclResult.get("BR") + caclResult.get("AC") + caclResult.get("BC"))
									+ ")",
							String.valueOf(caclResult.get("BD")) + " (" + result_BD + "M)", String.valueOf(caclResult.get("BR")) + " (" + result_BR + "M)",
							String.valueOf(caclResult.get("BC")) + " (" + result_BC + "M)", String.valueOf(caclResult.get("AD")) + " (" + result_AD + "M)",
							String.valueOf(caclResult.get("AR")) + " (" + result_AR + "M)", String.valueOf(caclResult.get("AC")) + " (" + result_AC + "M)",
							DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					if (ddxxglDtolist != null && ddxxglDtolist.size() > 0)
					{
						for (int i = 0; i < ddxxglDtolist.size(); i++)
						{
							if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid()))
							{
								String syrq = sjxxdto.getSyrq();
								// 内网访问
								String internalbtn = applicationurl + "/ws/inspection/sampleCapacityListPage?syrq=" + syrq;
								// 外网访问
								String external = externalurl + "/ws/inspection/sampleCapacityListPage?syrq=" + syrq;
								List<BtnJsonList> btnJsonLists = new ArrayList<BtnJsonList>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("内网访问");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendWorkMessage("null", ddxxglDtolist.get(i).getDdid(), title, message, btnJsonLists, "1");
							}
						}
					}
					cwglService.insertDto(cwglDto);
				}
				return true;
			}
		}

		return true;
	}*/

	/**
	 * 计算标本量，DNA+RNA 计算为两个标本量 血的DNA计算为 2个标本量，也就是说血的DNA+RNA计算为 3个标本量
	 *
	 * @param list
	 * @return
	 */
	/*private Map<String, Integer> caclSampSize(List<SjxxDto> list)
	{

		Map<String, Integer> resultMap = new HashMap<>();
		resultMap.put("AD", 0);
		resultMap.put("AR", 0);
		resultMap.put("BD", 0);
		resultMap.put("BR", 0);
		resultMap.put("BC", 0);
		resultMap.put("AC", 0);
		if (list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				SjxxDto sjxxDto = list.get(i);
				// 内部编码最后一位为B则判断为血，血按照16M(最低10M)来计算，其他的按照8M(最低5M)来计算。
				// 如果要做RNA的则再增加8M(最低5M)
				String nbbm = sjxxDto.getNbbm();
				String jcxmkzcs = sjxxDto.getJcxmcskz();
				if (StringUtil.isNotBlank(nbbm))
				{
					String yblx = nbbm.substring(nbbm.length() - 1);
					// 最后一位为B则判断为血，血按照16M(最低10M)来计算
					if ("B".equals(yblx))
					{
						// 如果要做RNA的则再增加8M(最低5M)
						if ("R".equals(jcxmkzcs))
						{
							resultMap.put("BR", resultMap.get("BR") + 1);
						} else if ("C".equals(jcxmkzcs))
						{
							resultMap.put("BC", resultMap.get("BC") + 1);
						} else
						{
							resultMap.put("BD", resultMap.get("BD") + 1);
						}
					} else
					{
						// 如果要做RNA的则再增加8M(最低5M)
						if ("R".equals(jcxmkzcs))
						{
							resultMap.put("AR", resultMap.get("AR") + 1);
						} else if ("C".equals(jcxmkzcs))
						{
							resultMap.put("AC", resultMap.get("AC") + 1);
						} else
						{
							resultMap.put("AD", resultMap.get("AD") + 1);
						}
					}
				}
			}
		}
		return resultMap;
	}*/

	/**
	 * 生信部访问查询方法
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getDtoForbiont(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getDtoForbiont(sjxxDto);
	}

	/**
	 * 根据合作伙伴查询送检数量
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByDb(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setDbsByStartAndEnd(sjxxDto);
		List<Map<String, String>> hblist = dao.getStatisByDb(sjxxDto);

		if (hblist == null || hblist.size() == 0)
			return null;
		return hblist;
	}

	/**
	 * 根据统计名称查询送检数量
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByTj(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setTjsByStartAndEnd(sjxxDto);
		List<Map<String, String>> hblist = dao.getStatisByTj(sjxxDto);

		if (hblist == null || hblist.size() == 0)
			return null;
		return hblist;
	}
	/**
	 * 根据统计名称查询送检数量(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByTjAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		setTjsByStartAndEnd(sjxxDto);
		List<Map<String, String>> hblist = dao.getStatisByTjAndJsrq(sjxxDto);

		if (hblist == null || hblist.size() == 0)
			return null;
		return hblist;
	}

	/**
	 * 根据分类和子分类信息，自动获取合作伙伴信息
	 *
	 * @param sjxxDto
	 */
	private void setTjsByStartAndEnd(SjxxDto sjxxDto)
	{
		SjhbxxDto sjhbxxDto = new SjhbxxDto();
		List<String> dbs = new ArrayList<>();
		//新加将前端的伙伴传入如果有不需要查询
		if (StringUtil.isNotBlank(sjxxDto.getDbhbs())) {
			String[] dbhbs = sjxxDto.getDbhbs().split(",");
			for (int i = 0; i < dbhbs.length; i++) {
				if (dbhbs[i] != null)
					dbs.add(dbhbs[i]);
			}
			sjxxDto.setDbs(dbs);
			return;
		}
		if (StringUtil.isNotBlank(sjxxDto.getDb()))
		{
			dbs.add(sjxxDto.getDb());
			sjxxDto.setDbs(dbs);
		} else if (StringUtil.isNotBlank(sjxxDto.getHbzfl()))
		{
			sjhbxxDto.setZfl(sjxxDto.getHbzfl());
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getTjDtoList(sjhbxxDto);
			if (sjhbxxDtos != null && sjhbxxDtos.size() > 0)
			{
				for (int i = 0; i < sjhbxxDtos.size(); i++)
				{
					dbs.add(sjhbxxDtos.get(i).getHbmc());
				}
				sjxxDto.setDbs(dbs);
			}
		} else if (StringUtil.isNotBlank(sjxxDto.getHbfl()))
		{
			sjhbxxDto.setFl(sjxxDto.getHbfl());
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getTjDtoList(sjhbxxDto);
			if (sjhbxxDtos != null && sjhbxxDtos.size() > 0)
			{
				for (int i = 0; i < sjhbxxDtos.size(); i++)
				{
					dbs.add(sjhbxxDtos.get(i).getHbmc());
				}
				sjxxDto.setDbs(dbs);
			}
		} else
		{
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getTjDtoList(sjhbxxDto);
			if (sjhbxxDtos != null && sjhbxxDtos.size() > 0)
			{
				for (int i = 0; i < sjhbxxDtos.size(); i++)
				{
					dbs.add(sjhbxxDtos.get(i).getHbmc());
				}
				sjxxDto.setDbs(dbs);
			}
		}
	}

	/**
	 * 统计复检申请
	 *
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getFjsqByDay(FjsqDto fjsqDto)
	{
		// TODO Auto-generated method stub
		return dao.getFjsqByDay(fjsqDto);
	}
	/**
	 * 统计废弃标本
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getFqybByYbzt(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getFqybByYbzt(sjxxDto);
	}

	@Override
	public List<SjxxDto> getPageFqybList(SjybztDto sjybztDto)
	{
		List<SjxxDto> sjxxDtos = dao.getPageFqybList(sjybztDto);
		SjybztDto sjybztDto_t = new SjybztDto();
		for (SjxxDto sjxxDto : sjxxDtos) {
			sjybztDto_t.setSjid(sjxxDto.getSjid());
			List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto_t);
			String ybztmc="";
			for (SjybztDto sjybztDto_h : sjybztDtos) {
				ybztmc = ybztmc+","+sjybztDto_h.getCsmc();
			}
			if(ybztmc.length()>0){
				ybztmc = ybztmc.substring(1);
			}
			sjxxDto.setYbztmc(ybztmc);
		}
		return sjxxDtos;
	}

	/**
	 * 统计所有的溶血标本（nbbm为B开头的）
	 *
	 * @param sjybztDto
	 * @return
	 */
	@Override
	public Integer getCountAllSjxx(SjybztDto sjybztDto)
	{
		// TODO Auto-generated method stub
		return dao.getCountAllSjxx(sjybztDto);
	}
	/**
	 * 统计所有的溶血标本（nbbm为B开头的）(接收日期)
	 *
	 * @param sjybztDto
	 * @return
	 */
	@Override
	public Integer getCountAllSjxxByJsrq(SjybztDto sjybztDto)
	{
		// TODO Auto-generated method stub
		return dao.getCountAllSjxxByJsrq(sjybztDto);
	}

	/**
	 * 统计所有状态为溶血的标本数量
	 *
	 * @param sjybztDto
	 * @return
	 */
	@Override
	public Integer getCountByzt(SjybztDto sjybztDto)
	{
		// TODO Auto-generated method stub
		return dao.getCountByzt(sjybztDto);
	}

	/**
	 * 统计所有的废弃标本
	 *
	 * @param sjybztDto
	 * @return
	 */
	@Override
	public Integer getAllFqyb(SjybztDto sjybztDto)
	{
		// TODO Auto-generated method stub
		return dao.getAllFqyb(sjybztDto);
	}

	/**
	 * 外部提交复检审核
	 *
	 * @param str
	 * @return
	 */
	@Override
	public boolean submitRecheckAudit(String str, HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(str))
		{
			SjxxDto sjxxDto = JSONObject.parseObject(str, SjxxDto.class);
			if (sjxxDto == null)
			{
				log.error("submitRecheckAudit:  未获取到送检信息！");
				return false;
			}
			if (StringUtil.isBlank(sjxxDto.getYbbh()))
			{
				log.error("submitRecheckAudit:  未获取到标本编号！ ybbh: " + sjxxDto.getYbbh());
				return false;
			}
			if (StringUtil.isBlank(sjxxDto.getLx()))
			{
				log.error("submitRecheckAudit:  未获取到复检类型！ lx: " + sjxxDto.getLx());
				return false;
			}
			if ("RECHECK_TYPE".equals(sjxxDto.getLx()))
			{
				JcsjDto jcsjDto=new JcsjDto();
				jcsjDto.setCsdm("RECHECK");
				jcsjDto.setJclb(BasicDataTypeEnum.RECHECK.getCode());
				JcsjDto jcsjDto_t =jcsjService.getDto(jcsjDto);
				if(jcsjDto_t!=null) {
					sjxxDto.setLx(jcsjDto_t.getCsid());
					sjxxDto.setLxdm(jcsjDto_t.getCsdm());
				}
				if (StringUtil.isBlank(sjxxDto.getYy()))
				{
					log.error("submitRecheckAudit:  未获取到复检原因！ yy: " + sjxxDto.getYy());
					return false;
				}
			} else if ("ADDJCXM_TYPE".equals(sjxxDto.getLx()))
			{
				JcsjDto jcsjDto=new JcsjDto();
				jcsjDto.setCsdm("ADDDETECT");
				jcsjDto.setJclb(BasicDataTypeEnum.RECHECK.getCode());
				JcsjDto jcsjDto_t =jcsjService.getDto(jcsjDto);
				if(jcsjDto_t!=null) {
					sjxxDto.setLx(jcsjDto_t.getCsid());
					sjxxDto.setLxdm(jcsjDto_t.getCsdm());
				}

				if (StringUtil.isBlank(sjxxDto.getBz()))
				{
					log.error("submitRecheckAudit:  未获取到备注！ bz: " + sjxxDto.getBz());
					return false;
				}
			} else
			{
				log.error("submitRecheckAudit:  复检类型有误！ lx: " + sjxxDto.getLx());
				return false;
			}
			if (StringUtil.isBlank(sjxxDto.getJcxm()))
			{
				log.error("submitRecheckAudit:  未获取到检测项目标记！ jcxm: " + sjxxDto.getJcxm());
				return false;
			}
			// 根据标本编号获取送检信息
			SjxxDto t_sjxxDto = dao.getDto(sjxxDto);
			if (t_sjxxDto == null)
			{
				log.error("submitRecheckAudit:  未查询到标本编号对应信息！");
				return false;
			}
			// 获取检测项目信息
			SjxxDto x_sjxxDto = new SjxxDto();
			x_sjxxDto.setSjid(t_sjxxDto.getSjid());
			x_sjxxDto.setJcxm(sjxxDto.getJcxm());
			List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getSjjcxmDtos(x_sjxxDto);
			if (CollectionUtils.isEmpty(sjjcxmDtos))
			{
				log.error("submitRecheckAudit:  获取对应检测项目基础数据失败！ ");
				return false;
			}

			// 根据tokenid获取用户信息
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("access_token", request.getParameter("access_token"));
			RestTemplate t_restTemplate = new RestTemplate();
			User user = t_restTemplate.postForObject(applicationurl + "/systemrole/common/getUserByToken", paramMap, User.class);
			if (user == null)
			{
				log.error("submitRecheckAudit:  未获取到token对应用户信息！");
				return false;
			}

			FjsqDto fjsqDto = new FjsqDto();
			if (StringUtil.isBlank(sjxxDto.getBgbj()))
			{
				fjsqDto.setBgbj("1");
			} else
			{
				fjsqDto.setBgbj(sjxxDto.getBgbj());
			}
			fjsqDto.setLx(sjxxDto.getLx());
			fjsqDto.setFjlxdm(sjxxDto.getLxdm());
			fjsqDto.setYy(sjxxDto.getYy());
			fjsqDto.setBz(sjxxDto.getBz());
			fjsqDto.setJcxm(sjjcxmDtos.get(0).getJcxmid());
			fjsqDto.setSjid(t_sjxxDto.getSjid());
			fjsqDto.setJcdw(t_sjxxDto.getJcdw());
			fjsqDto.setShlx(AuditTypeEnum.AUDIT_OUT_RECHECK.getCode());
			if(StringUtil.isBlank(fjsqDto.getYfje())&&"ADDDETECT".equals(fjsqDto.getFjlxdm())){
				if(t_sjxxDto!=null&&StringUtil.isNotBlank(fjsqDto.getSfff())&&"1".equals(fjsqDto.getSfff())&&StringUtil.isNotBlank(t_sjxxDto.getDb())){
					HbsfbzDto hbsfbzDto=new HbsfbzDto();
					hbsfbzDto.setHbmc(t_sjxxDto.getDb());
					List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
					if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0){
						for(HbsfbzDto dto:hbsfbzDtos){
							if(fjsqDto.getJcxm().equals(dto.getXm())){
								if(StringUtil.isNotBlank(fjsqDto.getJczxm())){
									if(fjsqDto.getJczxm().equals(dto.getZxm())){
										fjsqDto.setYfje(dto.getSfbz());
										break;
									}
								}else{
									fjsqDto.setYfje(dto.getSfbz());
									break;
								}
							}
						}
					}
				}
			}
			SjxxDto sjxxDto_T=new SjxxDto();
			sjxxDto_T.setSjid(fjsqDto.getSjid());
			sjxxDto_T = dao.getDto(sjxxDto_T);
			List<JcsjDto> yblxList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
			List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
			List<JcsjDto> sjqfList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			String yblxdm="";
			String jcdwmc="";
			String sjqfdm="";
			//判断是否已有内部编码，如果有取内部编码最后一位
			if(StringUtil.isNotBlank(sjxxDto_T.getNbbm())){
				String nbbm=sjxxDto_T.getNbbm();
				yblxdm=nbbm.substring(nbbm.length()-1);
			}else{
				if(yblxList!=null&&yblxList.size()>0){
					for(JcsjDto jcsjDto:yblxList){
						if(sjxxDto_T.getYblx().equals(jcsjDto.getCsid())){
							yblxdm=jcsjDto.getCsdm();
							break;
						}
					}
					//判断如果样本类型选择其他时，并且其他标本类型内容为"全血"或者"血浆"时，标本类型传"B"
					// 20240904 "其它"样本类型的csdm由XXX改为G
					if("XXX".equals(yblxdm) || "G".equals(yblxdm)){
						if(StringUtil.isNotBlank(sjxxDto_T.getYblxmc())){
							if("全血".equals(sjxxDto_T.getYblxmc())||"血浆".equals(sjxxDto_T.getYblxmc())){
								yblxdm="B";
							}
						}
					}
				}
			}
			if(jcdwList!=null&&yblxList.size()>0){
				for(JcsjDto jcsjDto:jcdwList){
					if(fjsqDto.getJcdw().equals(jcsjDto.getCsid())){
						jcdwmc=jcsjDto.getCsmc();
						break;
					}
				}
			}
			if(sjqfList!=null&&sjqfList.size()>0){
				for(JcsjDto jcsjDto:sjqfList){
					if(sjxxDto_T.getSjqf().equals(jcsjDto.getCsid())){
						sjqfdm=jcsjDto.getCsdm();
						break;
					}
				}
			}
			fjsqDto.setYblxdm(yblxdm);
			fjsqDto.setJcdwmc(jcdwmc);
			fjsqDto.setSjqfdm(sjqfdm);
			List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
			String csdm="";
			for(JcsjDto dto:list){
				if(dto.getCsid().equals(fjsqDto.getLx())){
					csdm=dto.getCsdm();
					break;
				}
			}
			List<String> lxs=new ArrayList<>();
			if(StringUtil.isNotBlank(csdm)){
				String[] strings = csdm.split("_");
				for(JcsjDto dto:list){
					if(dto.getCsdm().indexOf(strings[strings.length-1])!=-1){
						lxs.add(dto.getCsid());
					}
				}
			}
			FjsqDto fjsqDto_t=new FjsqDto();
			fjsqDto_t.setSjid(fjsqDto.getSjid());
			fjsqDto_t.setFjlxs(lxs);
			// 查询当前送检信息是否已经存在
			int num = fjsqService.getCount(fjsqDto_t);
			if (num > 0)
			{
				List<FjsqDto> fjsqDtos = fjsqService.getDtoList(fjsqDto_t);
				if (fjsqDtos != null && fjsqDtos.size() > 0)
				{
					for (int i = 0; i < fjsqDtos.size(); i++)
					{
						//如果为未提交，则新增复检申请 modify 姚嘉伟  2021/8/3
//						if (StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt()))
//						{
//							log.error("submitRecheckAudit:  已有信息未提交！");
//							return true;
//						}
						if (StatusEnum.CHECK_SUBMIT.getCode().equals(fjsqDtos.get(i).getZt()))
						{
							//若已有复测信息且在审核中按成功处理，不要返回错误信息 modify 姚嘉伟  2021/7/29 start
							log.error("submitRecheckAudit:  已有信息在审核中！");
							return true;
							// modify 姚嘉伟  2021/7/29 end
						}
						if (!StatusEnum.CHECK_PASS.getCode().equals(fjsqDtos.get(i).getZt()) && !StatusEnum.CHECK_UNPASS.getCode().equals(fjsqDtos.get(i).getZt())
						&& !StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt()))
						{
							log.error("submitRecheckAudit:  未查询到对应状态！");
							return false;
						}
					}
					// 如果状态是审核完成，或者审核不通过，则进行修改操作
					fjsqDto.setLrry(user.getYhid());
					fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
					if (StringUtil.isBlank(fjsqDto.getFjid()))
					{
						fjsqDto.setFjid(StringUtil.generateUUID());
					}
					boolean result = fjsqService.insertFjsq(fjsqDto);
					if (!result)
					{
						log.error("submitRecheckAudit:  插入复检信息失败！");
						return false;
					}
					//更新 送检实验管理 和 项目实验管理
					fjsqService.addOrUpdateSyData(fjsqDto, sjxxDto_T);
					
					// 提交审核
					ShgcDto shgcDto = new ShgcDto();
					shgcDto.setExtend_1("[" + fjsqDto.getFjid() + "]");
					shgcDto.setShlb(AuditTypeEnum.AUDIT_OUT_RECHECK.getCode());
					try
					{
						Map<String, Object> map = shgcService.checkAndCommit(shgcDto, user);
						if ("fail".equals(map.get("status")))
						{
							log.error("submitRecheckAudit:  提交审核失败！" + map.get("message"));
							return false;
						}
						return true;
					} catch (BusinessException e)
					{
						log.error("submitRecheckAudit:  提交审核失败！" + e.getMsg() + e.toString());
						return false;
					} catch (Exception e)
					{
						log.error("submitRecheckAudit:  提交审核失败！" + e.toString());
						return false;
					}
				}
			} else if (num == 0)
			{
				fjsqDto.setLrry(user.getYhid());
				fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
				if (StringUtil.isBlank(fjsqDto.getFjid()))
				{
					fjsqDto.setFjid(StringUtil.generateUUID());
				}
				boolean result = fjsqService.insertFjsq(fjsqDto);
				if (!result)
				{
					log.error("submitRecheckAudit:  插入复检信息失败！");
					return false;
				}
				//更新 送检实验管理 和 项目实验管理
				fjsqService.addOrUpdateSyData(fjsqDto, sjxxDto_T);
				// 提交审核
				ShgcDto shgcDto = new ShgcDto();
				shgcDto.setExtend_1("['" + fjsqDto.getFjid() + "']");
				shgcDto.setShlb(AuditTypeEnum.AUDIT_OUT_RECHECK.getCode());
				try
				{
					Map<String, Object> map = shgcService.checkAndCommit(shgcDto, user);
					if ("fail".equals(map.get("status")))
					{
						log.error("submitRecheckAudit:  提交审核失败！" + map.get("message"));
						return false;
					}
					return true;
				} catch (BusinessException e)
				{
					log.error("submitRecheckAudit:  提交审核失败！" + e.getMsg() + e.toString());
					return false;
				} catch (Exception e)
				{
					log.error("submitRecheckAudit:  提交审核失败！" + e.toString());
					return false;
				}
			}
		} else
		{
			log.error("submitRecheckAudit:  未接收到信息！--- str: " + str);
		}
		return false;
	}

	/**
	 * 根据送检ids判断所选数据检测项目是否相同
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> checkJcxm(SjxxDto sjxxDto)
	{
		return dao.checkJcxm(sjxxDto);
	}

	/**
	 * 判断当天实验的标本实验报告是否全部完成 当天标本(申请中都算) 复检 不发送报告 当天报告数里计算该记录 发送报告 当天报告数里不算 新增检测项目
	 * 不发送报告 当天报告数里计算该记录 发送报告 当天报告数里计算该记录 昨日标本(审核完成的才算) 复检 不发送报告 当天报告数里不算 发送报告
	 * 当天报告数里计算该记录 新增检测项目 不发送报告 当天报告数里不算 发送报告 当天报告数里计算该记录
	 *
	 * @return
	 */
	public void checkSybgSfwc()
	{
		//如果没有设置URL，则认为无需跟服务器关联，则不发送任何信息
		if(StringUtil.isBlank(menuurl))
			return;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		Date nowDate = calendar.getTime();

		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());

		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());

		String bgrq = format.format(nowDate);
		SjxxDto sjxx = new SjxxDto();
		sjxx.setSyrq(syrq);
		sjxx.setDsyrq(syrq);

		sjxx.setSyrqstart(syrqStart);
		sjxx.setSyrqend(syrqEnd);
		//现在复检类型需要从基础数据查询 张晗
		//<!-- select>
		Map<String, List<JcsjDto>> jcsjList =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK});
		List<JcsjDto> flsqList=jcsjList.get(BasicDataTypeEnum.RECHECK.getCode());
		if(flsqList!=null && flsqList.size()>0) {
			List<String> strList= new ArrayList<>();
			for (JcsjDto jcsjDto:flsqList) {
				//复测的才进行
				if("RECHECK".equals(jcsjDto.getCsdm())) {
					strList.add(jcsjDto.getCsid());
				}
			}
			sjxx.setFjlxs(strList);
		}
		//临时定死 
		List<String> dbs = new ArrayList<String>();
		dbs.add("上海六院");
		dbs.add("安徽省立");
		dbs.add("艾迪康实验室");

		sjxx.setDbs(dbs);
		List<SjxxDto> checklist = dao.checkSybgSfwc(sjxx);
		sjxx.setDbs(null);
		// 特检里没有未发送报告的数据
		if (checklist.size() == 0)
		{
			// 检测前日新增的检测里报告日期是否都为今日
			FjsqDto fjsqDto = new FjsqDto();
			fjsqDto.setLrsj(syrq);
			fjsqDto.setBgrq(bgrq);
			if(flsqList!=null && flsqList.size()>0) {
				List<String> strList= new ArrayList<>();
				for (JcsjDto jcsjDto:flsqList) {
					//加测的才进行
					if("ADDDETECT".equals(jcsjDto.getCsdm())||"RECHECK".equals(jcsjDto.getCsdm())) {
						strList.add(jcsjDto.getCsid());
					}
				}
				fjsqDto.setFjlxs(strList);
			}
			List<FjsqDto> fjwcs = fjsqService.checkFjbgSfwc(fjsqDto);

			if (fjwcs == null || fjwcs.size() == 0)
			{
				sjxx.setBgrq(bgrq);
				Map<String,String> map = dao.getYwcbgs(sjxx);

				//获取系统设置，根据设置值中的日期判断若日期不同则发送消息，否则不发送
				XtszDto xtszDto = new XtszDto();
				xtszDto.setSzlb(GlobalString.MATRIDX_SENDREPORT_FINISHMESSAGE);
				xtszDto=xtszService.getDto(xtszDto);
				if(xtszDto!=null){
					if(!bgrq.equals(xtszDto.getSzz())){
						// 发送钉钉消息
						JcsjDto jcsjDto = new JcsjDto();
						jcsjDto.setJclb("DINGMESSAGETYPE");
						jcsjDto.setCsdm("INSPECTION_REPORT_TYPE");
						List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());

						String title = xxglService.getMsg("ICOMM_SJ000012");
						String info = xxglService.getMsg("ICOMM_INSP00004");
						for (int i = 0; i < ddxxglDtolist.size(); i++)
						{
							if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid()))
							{
								talkUtil.sendWorkMessage(ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(), title,
										StringUtil.replaceMsg(info, String.valueOf(map.get("num")), String.valueOf(map.get("bgs"))+"+"+String.valueOf(map.get("fcs")), String.valueOf(map.get("bgs")), String.valueOf(map.get("fcs")), String.valueOf(map.get("jrfcs"))));
							}
						}
						//更新系统设置信息
						xtszDto.setSzz(bgrq);
						xtszDto.setOldszlb(GlobalString.MATRIDX_SENDREPORT_FINISHMESSAGE);
						xtszService.update(xtszDto);
					}
				}
			}
		}
	}

	/**
	 * 新增定时任务报告发送情况清单
	 */
	public void sendReportsNumberByTime(){
		if(StringUtil.isBlank(menuurl))
			return;
		List<Map<String,String>> mapList = this.getFsbgs(null);
		// 发送钉钉消息
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.INSPECTION_REPORT_TYPE.getCode());
		String title = xxglService.getMsg("ICOMM_SJ00068");
		String info = xxglService.getMsg("ICOMM_INSP00006");
		int num =0;
		int tenlast=0;
		String tenlastStr = "";
		int fivelast=0;
		String fivelastStr = "";
		int thirtylast=0;
		String thirtylastStr = "";
		if(mapList.size()>0){
			for(Map<String,String> map:mapList){
				if (StringUtil.isNotBlank(map.get("scbgrq"))){
					if(StringUtil.isNotBlank(map.get("num"))){
						num+=Integer.valueOf(map.get("num"));
					}
					if(StringUtil.isNotBlank(map.get("tenlast"))){
						tenlast+=Integer.valueOf(map.get("tenlast"));
					}
					if(StringUtil.isNotBlank(map.get("fivelast"))){
						fivelast+=Integer.valueOf(map.get("fivelast"));
					}
					if(StringUtil.isNotBlank(map.get("thirtylast"))){
						thirtylast+=Integer.valueOf(map.get("thirtylast"));
					}
				}
			}
		}
		if(num>0){
			tenlastStr = String.valueOf(tenlast) + "（"+ new BigDecimal(tenlast).divide(new BigDecimal(num), 4,RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)+"%）";
			fivelastStr = String.valueOf(fivelast) + "（"+ new BigDecimal(fivelast).divide(new BigDecimal(num), 4,RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)+"%）";
			thirtylastStr = String.valueOf(thirtylast) + "（"+ new BigDecimal(thirtylast).divide(new BigDecimal(num), 4,RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)+"%）";
		}else{
			tenlastStr = String.valueOf(tenlast);
			fivelastStr = String.valueOf(fivelast);
			thirtylastStr = String.valueOf(thirtylast);
		}
		for (DdxxglDto ddxxglDto : ddxxglDtolist) {
			if (StringUtil.isNotBlank(ddxxglDto.getDdid())){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				//SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
				DBEncrypt p = new DBEncrypt();
				String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/inspection/bgxxDdUncollectedView%3Fddid%3D"+ddxxglDto.getDdid()+"%26xjsj%3D"+p.eCode(calendar.getTime().getTime()+"").replace("+","%2B");
				// 外网访问
				//String external = externalurl + "/common/view/displayView?view_url=/ws/inspection/bgxxDdUncollectedView?ddid="+list.get(i).getDdid();
				List<BtnJsonList> btnJsonLists = new ArrayList<>();
				BtnJsonList btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("详细情况");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				talkUtil.sendCardMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), title,
						StringUtil.replaceMsg(info, String.valueOf(num), tenlastStr, fivelastStr,thirtylastStr), btnJsonLists,
						"1");
			}
		}
	}
	/**
	 * 定时发送今日实验报告数据
	 * @return
	 */
	public void sendReportsNumber(Map<String,String> csMap){
		//如果没有设置URL，则认为无需跟服务器关联，则不发送任何信息
		if(StringUtil.isBlank(menuurl))
			return;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Date nowDate = calendar.getTime();
		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());
		String bgrq = format.format(nowDate);
		SjxxDto sjxx = new SjxxDto();
		sjxx.setSyrq(syrq);
		sjxx.setDsyrq(syrq);
		sjxx.setSyrqstart(syrqStart);
		sjxx.setSyrqend(syrqEnd);
		sjxx.setBgrq(bgrq);

		//现在复检类型需要从基础数据查询 张晗
		//<!-- select>
		List<JcsjDto> flsqList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RECHECK.getCode());
		List<String> strList= new ArrayList<>();
		for (JcsjDto jcsjDto:flsqList) {
			//复测的才进行
			if("RECHECK".equals(jcsjDto.getCsdm())) {
				strList.add(jcsjDto.getCsid());
			}
		}
		sjxx.setFjlxs(strList);
		String bh=csMap.get("bh");
		String wbh=csMap.get("wbh");
		String wbhjcdw=csMap.get("wbhjcdw");
		//包含的送检伙伴
		if(StringUtil.isNotBlank(bh)){
			List<String> bhsjdw =new ArrayList<>();
			String[] bhArr=bh.split(";");
			for(String bhStr:bhArr){
				bhsjdw.add(bhStr);
			}
			sjxx.setBhsjdw(bhsjdw);
		}
		if(StringUtil.isNotBlank(wbh)){
			List<String> wbhsjhb =new ArrayList<>();
			String[] wbhArr=wbh.split(";");
			for(String wbhStr:wbhArr){
				wbhsjhb.add(wbhStr);
			}
			sjxx.setWbhsjhb(wbhsjhb);
		}
		if(StringUtil.isNotBlank(wbhjcdw)){
			List<String> jcdwList =new ArrayList<>();
			String[] wbhArr=wbhjcdw.split(";");
			for(String wbhStr:wbhArr){
				jcdwList.add(wbhStr);
			}
			sjxx.setWbhsjdw(jcdwList);
		}
		Map<String,String> map = dao.getYwcbgs(sjxx);
		// 发送钉钉消息
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.INSPECTION_REPORT_TYPE.getCode());
		String title = xxglService.getMsg("ICOMM_SJ00068");
		String info = xxglService.getMsg("ICOMM_INSP00005");

		for (DdxxglDto ddxxglDto : ddxxglDtolist) {
			if (StringUtil.isNotBlank(ddxxglDto.getDdid())){
				talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), title,
						StringUtil.replaceMsg(info, String.valueOf(map.get("wfbgs")) + "(" + String.valueOf(map.get("bfb")) + "%)", 
								String.valueOf(map.get("bgs"))+"+"+String.valueOf(map.get("fcs")), String.valueOf(map.get("bgs")), String.valueOf(map.get("fcs")), 
								String.valueOf(map.get("jrfcs")), DateUtils.getCustomFomratCurrentDate("MM-dd HH:mm:ss"), String.valueOf(map.get("num"))));
			}
		}
	}
	/**
	 * 数据滚动刷新
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListSjxx(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getListSjxx(sjxxDto);
	}

	/**
	 * 查询刷新之间新增的送检信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListSjxxBylrsj(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getListSjxxBylrsj(sjxxDto);
	}

	/**
	 * 报告阳性率列表
	 *
	 * @param sjwzxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListPositive(SjwzxxDto sjwzxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getListPositive(sjwzxxDto);
	}

	/**
	 * 合作伙伴查看送检列表
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getPageDtoBySjhb(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getPageDtoBySjhb(sjxxDto);
	}

	/**
	 * 统计送检物种信息
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWzxxBySjid(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getWzxxBySjid(sjxxDto);
	}

	/**
	 * 自定义统计图
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> customStatis(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		if (sjxxDto.getTjtj().toLowerCase().contains("insert"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("insert", ""));
		}
		if (sjxxDto.getTjtj().toLowerCase().contains("update"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("update", ""));
		}
		if (sjxxDto.getTjtj().toLowerCase().contains("delete"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("delete", ""));
		}
		if (sjxxDto.getTjtj().toLowerCase().contains("alert"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("alert", ""));
		}
		if (sjxxDto.getTjtj().toLowerCase().contains("drop"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("drop", ""));
		}

		// 去除统计周期
		sjxxDto.setJsrqstart(sjxxDto.getParam().get("jsrqstart"));
		sjxxDto.setJsrqend(sjxxDto.getParam().get("jsrqend"));
		sjxxDto.setBgrqstart(sjxxDto.getParam().get("bgrqstart"));
		sjxxDto.setBgrqend(sjxxDto.getParam().get("bgrqend"));
		sjxxDto.setTjzq(sjxxDto.getParam().get("tjzq"));
		if (sjxxDto.getBgrqstart() != null && sjxxDto.getBgrqstart() != "")
		{
			sjxxDto.setJsrqstart("");
		}
		if (sjxxDto.getBgrqend() != null && sjxxDto.getBgrqend() != "")
		{
			sjxxDto.setJsrqend("");
		}
		// 组装统计条件
		if (sjxxDto.getTjtj().length() > 0)
		{
			String[] tjtjs = sjxxDto.getTjtj().replace("[", "").replace("]", "").trim().split(",");
			for (int i = 0; i < tjtjs.length; i++)
			{
				String[] tjtj = tjtjs[i].substring(1, tjtjs[i].length() - 1).split("\\:");
				if (tjtj.length > 1)
				{
					if ("sjdw".equals(tjtj[0]))
					{
						sjxxDto.setDwmc(tjtj[1]);
					} else if ("ks".equals(tjtj[0]))
					{
						sjxxDto.setKsmc(tjtj[1]);
					} else if ("yblx".equals(tjtj[0]))
					{
						sjxxDto.setYblxmc(tjtj[1]);
					} else if ("fl".equals(tjtj[0]))
					{
						sjxxDto.setFl(tjtj[1]);
					} else if ("zfl".equals(tjtj[0]))
					{
						sjxxDto.setZfl(tjtj[1]);
					} else if ("bgmb".equals(tjtj[0]))
					{
						sjxxDto.setBgmb(tjtj[1]);
					} else if ("db".equals(tjtj[0]))
					{
						sjxxDto.setDb(tjtj[1]);
					} else if ("jcxmid".equals(tjtj[0]))
					{
						sjxxDto.setJcxmmc(tjtj[1]);
					}
				}
			}
		}
		List<Map<String, String>> list = null;
		// 组装统计字段
		if (sjxxDto.getTjzds() != null && sjxxDto.getTjzds().length == 2)
		{
			List<SjxxDto> tmp_list = dao.getCustomStatisHead(sjxxDto);
			if (tmp_list != null && tmp_list.size() > 0)
			{
				Map<String, Object> paraMap = new HashMap<>();
				paraMap.put("tjzdList", sjxxDto.getTjzds());
				paraMap.put("mainSelectList", sjxxDto.getMainSelects());
				paraMap.put("mainGroupList", sjxxDto.getMainGroups());
				paraMap.put("selzdList", sjxxDto.getSelectzds());
				paraMap.put("groupzdList", sjxxDto.getGroupzds());
				paraMap.put("heads", tmp_list);
				paraMap.put("sjxxDto", sjxxDto);
				list = dao.getCustomStatisByHead(paraMap);
			}
		} else
		{
			list = dao.getCustomStatisList(sjxxDto);
		}
		return list;
	}

	/**
	 * 自定义统计（列表显示）
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCustomStatisList(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub

		if (sjxxDto.getTjtj().toLowerCase().contains("insert"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("insert", ""));
		} else if (sjxxDto.getTjtj().toLowerCase().contains("update"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("update", ""));
		} else if (sjxxDto.getTjtj().toLowerCase().contains("delete"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("delete", ""));
		} else if (sjxxDto.getTjtj().toLowerCase().contains("alert"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("alert", ""));
		} else if (sjxxDto.getTjtj().toLowerCase().contains("drop"))
		{
			sjxxDto.setTjtj(sjxxDto.getTjtj().toLowerCase().replace("drop", ""));
		}
		sjxxDto.setJsrqstart(sjxxDto.getParam().get("jsrqstart"));
		sjxxDto.setJsrqend(sjxxDto.getParam().get("jsrqend"));
		sjxxDto.setBgrqstart(sjxxDto.getParam().get("bgrqstart"));
		sjxxDto.setBgrqend(sjxxDto.getParam().get("bgrqend"));
		sjxxDto.setTjzq(sjxxDto.getParam().get("tjzq"));
		if (sjxxDto.getBgrqstart() != null && sjxxDto.getBgrqstart() != "")
		{
			sjxxDto.setJsrqstart("");
		}
		if (sjxxDto.getBgrqend() != null && sjxxDto.getBgrqend() != "")
		{
			sjxxDto.setJsrqend("");
		}
		// 组装统计条件
		if (sjxxDto.getTjtj() != null && sjxxDto.getTjtj().length() > 0)
		{
			String[] tjtjs = sjxxDto.getTjtj().trim().split(",");
			for (int i = 0; i < tjtjs.length; i++)
			{
				String[] tjtj = tjtjs[i].substring(1, tjtjs[i].length() - 1).split("\\:");
				if (tjtj.length > 1)
				{
					if ("sjdw".equals(tjtj[0]))
					{
						sjxxDto.setDwmc(tjtj[1]);
					} else if ("ks".equals(tjtj[0]))
					{
						sjxxDto.setKsmc(tjtj[1]);
					} else if ("yblx".equals(tjtj[0]))
					{
						sjxxDto.setYblxmc(tjtj[1]);
					} else if ("fl".equals(tjtj[0]))
					{
						sjxxDto.setFl(tjtj[1]);
					} else if ("zfl".equals(tjtj[0]))
					{
						sjxxDto.setZfl(tjtj[1]);
					} else if ("bgmb".equals(tjtj[0]))
					{
						sjxxDto.setBgmb(tjtj[1]);
					} else if ("db".equals(tjtj[0]))
					{
						sjxxDto.setDb(tjtj[1]);
					} else if ("jcxmid".equals(tjtj[0]))
					{
						sjxxDto.setJcxmmc(tjtj[1]);
					}
				}
			}
		}
		return dao.getCustomStatisList(sjxxDto);
	}

	/**
	 * 修改临床反馈
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateFeedBack(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		boolean result = dao.updateFeedBack(sjxxDto);
		if (sjxxDto.getFjids() != null && sjxxDto.getFjids().size() > 0)
		{
			for (int i = 0; i < sjxxDto.getFjids().size(); i++)
			{
				boolean saveFile = fjcfbService.save2RealFile(sjxxDto.getFjids().get(i), sjxxDto.getSjid());
				if (!saveFile)
					return false;
			}
		}
		// 获取送检附件IDs
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (fjcfbDtos != null && fjcfbDtos.size() > 0)
		{
			List<String> fjids = new ArrayList<>();
			for (int i = 0; i < fjcfbDtos.size(); i++)
			{
				fjids.add(fjcfbDtos.get(i).getFjid());
			}
			sjxxDto.setFjids(fjids);
		}
		if (result)
		{
			// 查询送检信息，发送钉钉消息
			SjxxDto sjxx = dao.getDto(sjxxDto);
			sjxxDto.setHzxm(sjxx.getHzxm());
			sjxxDto.setYbbh(sjxx.getYbbh());
			sjxxDto.setYblxmc(sjxx.getYblxmc());
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm("INSPECTION_FEEDBACK");
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
			if (ddxxglDtos != null && ddxxglDtos.size() > 0)
			{
				for (int i = 0; i < ddxxglDtos.size(); i++)
				{
					if (StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid()))
					{
						String title = xxglService.getMsg("ICOMM_SH00029");
						String sjid=sjxxDto.getSjid();
						String message=xxglService.getMsg("ICOMM_SH00019", sjxxDto.getHzxm(),sjxxDto.getYbbh(),sjxxDto.getYblxmc(),sjxx.getSfzqmc(),sjxxDto.getLcfk(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
						String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/inspection/getDdFeedbackView?sjid=" + sjid;
			 			// 外网访问
						String external = externalurl + "/common/view/displayView?view_url=/ws/inspection/getDdFeedbackView?sjid=" + sjid;
						List<BtnJsonList> btnJsonLists = new ArrayList<>();
						BtnJsonList btnJsonList = new BtnJsonList();
						btnJsonList.setTitle("内网访问");
						btnJsonList.setActionUrl(internalbtn);
						btnJsonLists.add(btnJsonList);
						btnJsonList = new BtnJsonList();
						btnJsonList.setTitle("外网访问");
						btnJsonList.setActionUrl(external);
						btnJsonLists.add(btnJsonList);
						talkUtil.sendCardMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), title, message, btnJsonLists, "1");
					}
				}
			}
		}
		return result;
	}

	/**
	 * 根据送检ID查询伙伴信息
	 *
	 * @param sjid
	 * @return
	 */
	@Override
	public List<SjxxDto> selectBgmbBySjid(String sjid)
	{
		// TODO Auto-generated method stub
		return dao.selectBgmbBySjid(sjid);
	}

	/**
	 * 日报增加送检清单
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListBydaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub

		return dao.getListBydaily(sjxxDto);
	}
	/**
	 * 日报增加送检清单(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListBydailyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub

		return dao.getListBydailyAndJsrq(sjxxDto);
	}

	/**
	 * 获取内部编码（组装）
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public String getNbbm(SjxxDto sjxxDto){
		SjxxDto sjxxDto_dm = dao.getconfirmDm(sjxxDto.getYbbh());
		if (sjxxDto_dm == null){
			return null;
		}
		String yearLast = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date().getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置星期一为一周开始的第一天
		calendar.setTimeInMillis(System.currentTimeMillis());// 获得当前的时间戳
		String weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + "";// 获得当前日期属于今年的第几周
		if ("1".equals(weekOfYear) && calendar.get(Calendar.MONTH) == 11){
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
		String value = StringUtil.isNotBlank(sjxxDto_dm.getCskz4())?sjxxDto_dm.getCskz4():"0";
		yearandweek += value;
		sjxxDto_dm.setSerial(yearandweek);
		// 杭州实验室开始特殊处理
//		if("01".equals(sjxxDto_dm.getJcdwdm())) {
//			if(("IMP_REPORT_TNGS_TEMEPLATE".equals(sjxxDto_dm.getJcxmcskz3()))) {
//				sjxxDto_dm.setJcdwdm("T1");
//			}else if(!"科研".equals(sjxxDto_dm.getSjqfmc()) && ("IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(sjxxDto_dm.getJcxmcskz3()) || (StringUtil.isNotBlank(sjxxDto_dm.getJcxmcskz3()) && sjxxDto_dm.getJcxmcskz3().contains("IMP_REPORT_SEQ_")) )
//					&& !"B".equals(sjxxDto_dm.getLxdm())) {
//				SjhbxxDto sjhbxxDto = sjxxCommonService.getHbDtoFromId(sjxxDto);
//				if(sjhbxxDto!=null && StringUtil.isNotBlank(sjhbxxDto.getHbid())) {
//					if("D".equals(sjxxDto_dm.getJcdm()) || "C".equals(sjxxDto_dm.getJcdm()))
//						sjxxDto_dm.setJcdwdm("X1");
//					else if("R".equals(sjxxDto_dm.getJcdm()))
//						sjxxDto_dm.setJcdwdm("X1");
//				}
//			}
//		}
		//X项目限制修改
		if(("01".equals(sjxxDto_dm.getJcdwdm()) && "IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(sjxxDto_dm.getJcxmcskz3()) && !"科研".equals(sjxxDto.getSjqfmc())) ) {
			SjhbxxDto sjhbxxDto = sjxxCommonService.getHbDtoFromId(sjxxDto);
			if (null == sjhbxxDto || StringUtil.isBlank(sjhbxxDto.getHbid())) {
				sjxxDto_dm.setJcxm("X");
			}
		}
		String serial = dao.getSerial(sjxxDto_dm);
		if(sjxxDto_dm.getJcdwdm()==null) {
			sjxxDto_dm.setJcdwdm("");
		}
//		新增了一个tNGS的项目，该项目内部编码规则需要更改。如果检测项目的cskz1为T，并且检测单位的csdm为01，则对内部编码前两位的规则进行修改，前两位原本是直接取检测单位的csdm，如果符合以上条件，将0替换为T，更改为T1
		//若样本类型为其他且yblxmc填写的全血/血浆则内部编码最后一位为B
		JcsjDto jc_yblx = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(), sjxxDto_dm.getYblx());
		String nbbm =sjxxDto_dm.getJcxm() + sjxxDto_dm.getJcdwdm()+sjxxDto_dm.getJcdm() + yearandweek + serial + sjxxDto_dm.getLxdm();
		if ( "其他".equals(jc_yblx.getCsmc()) && ("全血".equals(sjxxDto_dm.getYblxmc())||"血浆".equals(sjxxDto_dm.getYblxmc()))){
			nbbm =sjxxDto_dm.getJcxm() + sjxxDto_dm.getJcdwdm()+sjxxDto_dm.getJcdm() + yearandweek  + serial + 'B';
		}
		return nbbm;
	}

	/**
	 * 根据规则生成内部编码
	 * @param sjxxDto
	 * @return string
	 *
	 */
	@Override
	public String generateNbbm(SjxxDto sjxxDto) throws BusinessException {
		List<Map<String, String>> infoMaps = dao.getconfirmDmInfo(sjxxDto.getYbbh());
		if(CollectionUtils.isEmpty(infoMaps))
			return null;
		Map<String, String> infoMap = infoMaps.get(0);
		if (infoMap == null){
			return null;
		}
		/*----------获取流水号规则-------开始----------*/
		//送检伙伴的cskz4配置的内部编码规则（最优先）
		//检测单位的cskz4配置的内部编码规则（次优先）
		//送检区分的cskz1配置的内部编码规则（次次优先）
		//内部编码替换规则{key:length:isConditionOfSerial}=>{替换关键词:长度:是否为计算流水号的条件}
		//特检编码规则,科研编码规则,入院编码规则,环境监控编码规则,验证编码规则,特殊SP编码规则
		//默认规则如下:
		//特检 INSPECT			编码规则(共11位):	实验室(2位) + 年份(2位) + 周数(2位) + 周标本流水号(4位) + 样本代码
		//科研 RESEARCH			编码规则(共11位):	实验室(2位) + 科研编码(4位) + 该科研的标本流水号(4位) + 样本代码
		//入院 HOSPITALIZED		编码规则(共11位):	实验室(2位) + 年份(2位) + 周数(2位) + 周标本流水号(4位) + 样本代码
		//环境监控 ENV_MONITOR	编码规则:
		//验证 VERIFICATION		编码规则:
		//特殊 SP				编码规则(共11位):	实验室(2位) + 年份(2位) + 周数(2位) + 特殊的代码(2位) + 周标本流水号(2位) + 样本代码
		String nbbmgz = "{jcxm:1:false}{jcdwdm:2:true}{nf:2:true}{zs:2:true}{lsh:4:false}{lxdm::false}";
		List<String> gzList = new ArrayList<>();
		gzList.add(infoMap.get("hbbmgz"));//伙伴-编码规则
		if(StringUtil.isNotBlank(infoMap.get("jcdwbmgz"))){
			Map<String,Object> jsonmap = (Map<String, Object>) JSONObject.parse(infoMap.get("jcdwbmgz"));
			if (jsonmap.containsKey("INNER_CODE")){
				gzList.add(jsonmap.get("INNER_CODE").toString());
			}
		}
		gzList.add(infoMap.get("sjqfbmgz"));//送检区分-编码规则
		for (String bmgz : gzList) {
			if (StringUtil.isNotBlank(bmgz)){
				String[] nbbmgzList = bmgz.split(",");
				if ((nbbmgzList.length == 1 || StringUtil.isBlank(infoMap.get("sjqfxh"))) && StringUtil.isNotBlank(nbbmgzList[0])){
					nbbmgz = nbbmgzList[0];
					break;
				}else if(nbbmgzList.length >= Integer.parseInt(infoMap.get("sjqfxh")) && StringUtil.isNotBlank(nbbmgzList[Integer.parseInt(infoMap.get("sjqfxh"))-1])){
					nbbmgz = nbbmgzList[Integer.parseInt(infoMap.get("sjqfxh"))-1];
					break;
				}
			}
		}
		log.error("内部编号(新规则)规则："+ nbbmgz);
		/*----------获取流水号规则-------结束----------*/
		/*----------内部编码规则处理-----开始----------*/
		//1、根据规则获取内部编码长度：nbbmLength
		int nbbmLength = 0;//内部编码长度（累加）
		//2、流水号生成的查询条件（里面包含conditionList、startindex、seriallength、deafultserial）：serialMap
		Map<String, Object> serialMap = new HashMap<>();//流水号生成查询条件
		//3、内部编码规则中isConditionOfSerial为true的条件：conditionList
		List<Map<String,Object>> conditionList = new ArrayList<>();
		//4、内部编码字符串List
		List<Map<String,String>> nbbmStrList = new ArrayList<>();
		//5、流水号List
		List<Map<String,String>> serialStrList = new ArrayList<>();
		//6、循环替换nbbmgz中个关键词(除有关流水号的内容不替换)
		while (nbbmgz.contains("{") && nbbmgz.contains("}")) {
			Map<String,String> nbbmMap = new HashMap<>();
			int length = 0;//默认关键词长度为0
			boolean isConditionOfSerial = false;//默认 是否加入流水哈生成的判断条件 为false
			int startIndex = nbbmgz.indexOf("{");//获取开始下标
			int endIndex = nbbmgz.indexOf("}");//获取结束下标
			if (startIndex != 0){
				// 情况1：替换符前方有字符串；若“{”在不在首位
				// xxxx-{jcdwdm:2:true}
				// 截取“xxxx-”存入nbbmStrList中
				String valueStr = nbbmgz.substring(0,startIndex);
				endIndex = startIndex-1;
				nbbmMap.put("key","string");//设置关键词为string
				nbbmMap.put("value",valueStr);//设置对应值
				nbbmMap.put("index", String.valueOf(nbbmLength));//设置位置（前面所有字符的长度）
				length = valueStr.length();
				nbbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
			}else {
				// 情况1：替换符前方无字符串；若“{”在首位
				// {jcdwdm:2:true}
				//截取{}内的内容
				String key = nbbmgz.substring(startIndex + 1, endIndex);//jcdwdm:2:true
				//若截取内容内有“:”则分割；
				//若截取内容内无“:”，则默认key为截取内容，长度为其map中长度，默认是否加入流水哈生成的判断条件为false
				if (key.contains(":")) {
					String[] split = key.split(":");//例：[jcdwdm,4,true]
					key = split[0];//第一位为关键词key，例：jcdwdm
					if (StringUtil.isNotBlank(split[1])){
						try {
							length = Integer.parseInt(split[1]);//第二位为对应关键词的长度，例：2
						} catch (NumberFormatException e) {
							log.error("内部编号(新规则)警告：key:"+key+"的length:"+split[1]+"不是数字");
						}
					}
					//若有第三位，则第三位为是否加入流水哈生成的判断条件，例：true
					//若无第三位，默认为false
					if (split.length >= 3){
						isConditionOfSerial = "true".equalsIgnoreCase(split[2]);
					}
				}
				nbbmMap.put("key",key);//设置关键词
				//若为流水号，不做处理，
				if (("lsh").equals(key)){
					nbbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
					nbbmMap.put("value","");//设置值为空（后面处理）
					nbbmMap.put("index", String.valueOf(nbbmLength));//设置位置（前面所有字符的长度）
					serialStrList.add(nbbmMap);//存入流水号List中
				} else {
					//内部编码特殊处理
					sjnbbmService.dealNbbmKeyMap(key, sjxxDto, infoMap);
					String valueStr = infoMap.get(key);//从infoMap中获取对应值，例：jcdwdm对应为01
					if (valueStr != null && valueStr.length() > length && length != 0){
						valueStr = valueStr.substring(valueStr.length()-length);
						//将值截取至对应长度。例：截取01的后2位（01为对应值，2为对应值长度）（默认从末尾开始截取长度，若需要从前面开始截取，则需要增加特殊key,并在dealNbbmKeyMap方法中处理）
						length = valueStr.length();//获取对应值最终长度（针对没设长度的时候）
					}

					nbbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
					nbbmMap.put("value",valueStr);//设置值
					nbbmMap.put("index", String.valueOf(nbbmLength));//设置位置（前面所有字符的长度）
				}
				if (isConditionOfSerial){
					Map<String,Object> map = new HashMap<>();
					map.put("conditionIndex",nbbmLength+1);
					map.put("conditionLength",length);
					map.put("conditionKey",nbbmMap.get("value"));
					conditionList.add(map);
				}
			}
			nbbmStrList.add(nbbmMap);//将截取替换内容添加到list
			nbbmgz = nbbmgz.substring(endIndex+1);//截取剩余分规则
			nbbmLength += length;//内部编码长度累加
		}
		//7、若内部编码规则最后还有字符串，则进行处理
		if(nbbmgz!=null&&nbbmgz.length()>0){
			Map<String,String> nbbmMap = new HashMap<>();
			nbbmMap.put("key","string");//设置关键词为string
			nbbmMap.put("value",nbbmgz);//设置对应值
			nbbmMap.put("index", String.valueOf(nbbmLength));//设置位置（前面所有字符的长度）
			int length = nbbmgz.length();
			nbbmMap.put("length", String.valueOf(length));//设置长度（当前字符串的长度）
			nbbmStrList.add(nbbmMap);//将截取替换内容添加到list
			nbbmLength += length;//内部编码长度累加
		}
		//8、遍历流水号List,循环生成对应的流水号
		for (Map<String, String> nbbmInfo : serialStrList) {
			serialMap.put("startindex", Integer.parseInt(nbbmInfo.get("index"))+1);//流水号开始位置
			serialMap.put("seriallength", Integer.parseInt(nbbmInfo.get("length")));//流水号长度
			serialMap.put("nbbmLength", nbbmLength);//内部编码长度
			serialMap.put("conditionList",conditionList);//条件List
			serialMap.put("jcdm", infoMap.get("jcdm"));//检测项目cskz1（区分F项目和其他项目）
			//生成获取流水号
			String customSerial = dao.getCustomSerial(serialMap);
			nbbmInfo.put("value",customSerial);
		}
		//拼接list获取完整内部编码
		String nbbm = "";
		for (Map<String, String> nbbmStr : nbbmStrList) {
			nbbm += nbbmStr.get("value");
		}
		return nbbm;
	}
	/**
	 * 检测项目个数
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDRByDay(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxDRByDay(sjxxDto);
	}
	/**
	 * 检测项目个数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDRByDayAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxDRByDayAndJsrq(sjxxDto);
	}

	/**
	 * 检测项目个数
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDRByDayAndJcdw(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxDRByDayAndJcdw(sjxxDto);
	}
	/**
	 * 检测项目个数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDRByDayAndJcdwAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxDRByDayAndJcdwAndJsrq(sjxxDto);
	}

	/**
	 * 收费标本里边检测数量
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxTypeByDay(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxTypeByDay(sjxxDto);
	}
	/**
	 * 收费标本里边检测数量(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxTypeByDayAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxTypeByDayAndJsrq(sjxxDto);
	}

	/**
	 * 按照日期统计收费标本
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjxxDRBySy(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getSjxxDRBySy(sjxxDto);
	}
	/**
	 * 按照日期统计收费标本(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjxxDRBySyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getSjxxDRBySyAndJsrq(sjxxDto);
	}

	/**
	 * 按照周统计检测总条数
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjxxDRByWeek(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getSjxxWeekDRBySy(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 按照周统计检测总条数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjxxDRByWeekAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(StringUtil.isNotBlank(sjxxDto.getJsrqend())?sjxxDto.getJsrqend():sdf.format(new Date())));
			int zq = Integer.parseInt(StringUtil.isNotBlank(sjxxDto.getZq()) && !"00".equals(sjxxDto.getZq())?sjxxDto.getZq():"7");
			if (zq == 0)
				zq = 7;
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getSjxxWeekDRBySyAndJsrq(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 周报统计收费标本下边检测项目的总条数
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxTypeBYWeek(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getYbxxTypeBYWeek(sjxxDto);
	}

	/**
	 * 周报统计收费标本下边检测项目的总条数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxTypeBYWeekAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getYbxxTypeBYWeekAndJsrq(sjxxDto);
	}

	/**
	 * 周报统计收费标本下边的检测项目总条数(通过周)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeekYbxxType(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getWeekYbxxType(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 周报统计收费标本下边的检测项目总条数(通过周)(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeekYbxxTypeByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(StringUtil.isNotBlank(sjxxDto.getJsrqend())?sjxxDto.getJsrqend():sdf.format(new Date())));
			int zq = Integer.parseInt(StringUtil.isNotBlank(sjxxDto.getZq()) && !"00".equals(sjxxDto.getZq())?sjxxDto.getZq():"7");
			if (zq == 0)
				zq = 7;
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getWeekYbxxTypeByJsrq(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 根据日期返回两周前的日期
	 *
	 * @param data
	 * @return
	 */
	public static String getBeforeWeek(String data, int day)
	{
		String dataFormat = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(data));
			calendar.add(Calendar.DATE, day);
			dataFormat = sdf.format(calendar.getTime());
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataFormat;
	}

	/**
	 * 统计上上周的临床反馈结果
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getLcfkByBefore(SjxxDto sjxxDto)
	{
		SjxxDto t_sjxxDto = new SjxxDto();
		// TODO Auto-generated method stub
		String jsrqstart = getBeforeWeek(sjxxDto.getJsrqstart(), -7);
		String jsrqend = getBeforeWeek(sjxxDto.getJsrqend(), -7);
		t_sjxxDto.setJsrqstart(jsrqstart);
		t_sjxxDto.setJsrqend(jsrqend);
		return dao.getLcfkByBefore(t_sjxxDto);
	}

	/**
	 * 根据样类型统计前三的阳性率
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJyjgByYblx(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJyjgByYblx(sjxxDto);
	}

	/**
	 * 增加科室的圆饼统计图
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getKsByweek(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getKsByweek(sjxxDto);
	}
	/**
	 * 增加科室的圆饼统计图(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getKsByweekAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getKsByweekAndJsrq(sjxxDto);
	}

	/**
	 * 增加合作医院数，科室数，医生数的统计表
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjdwSjysKs(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> strList = new ArrayList<>();
		strList.add("医院");
		strList.add("医师");
		strList.add("科室");
		List<Map<String, String>> ToWeek = dao.getSjdwSjysKs(sjxxDto);
		String jsrqend = getBeforeWeek(sjxxDto.getJsrqend(), -7);
		sjxxDto.setJsrqend(jsrqend);
		List<Map<String, String>> beforeWeek = dao.getSjdwSjysKs(sjxxDto);
		List<Map<String, String>> ListMap = new ArrayList<>();
		for (int i = 0; i < ToWeek.size(); i++)
		{
			Map<String, String> map1 = new HashMap<>();
			String before = String.valueOf(beforeWeek.get(i).get("num"));
			String now = String.valueOf(ToWeek.get(i).get("num"));
			int more = Integer.parseInt(now) - Integer.parseInt(before);
			map1.put("hzdw", strList.get(i));
			map1.put("before", before);
			map1.put("now", now);
			map1.put("more", more + "");
			ListMap.add(map1);
		}
		sjxxDto.setJsrqend(getBeforeWeek(sjxxDto.getJsrqend(), +7));
		return ListMap;
	}

	/**
	 * 增加合作医院数，科室数，医生数的统计表(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjdwSjysKsByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> strList = new ArrayList<>();
		strList.add("医院");
		strList.add("医师");
		strList.add("科室");
		List<Map<String, String>> ToWeek = dao.getSjdwSjysKsByJsrq(sjxxDto);
		String jsrqend = getBeforeWeek(sjxxDto.getJsrqend(), -7);
		sjxxDto.setJsrqend(jsrqend);
		List<Map<String, String>> beforeWeek = dao.getSjdwSjysKsByJsrq(sjxxDto);
		List<Map<String, String>> ListMap = new ArrayList<>();
		for (int i = 0; i < ToWeek.size(); i++)
		{
			Map<String, String> map1 = new HashMap<>();
			String before = String.valueOf(beforeWeek.get(i).get("num"));
			String now = String.valueOf(ToWeek.get(i).get("num"));
			int more = Integer.parseInt(now) - Integer.parseInt(before);
			map1.put("hzdw", strList.get(i));
			map1.put("before", before);
			map1.put("now", now);
			map1.put("more", more + "");
			ListMap.add(map1);
		}
		sjxxDto.setJsrqend(getBeforeWeek(sjxxDto.getJsrqend(), +7));
		return ListMap;
	}

	/**
	 * 根据样本编号下载报告
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	public Map<String, Object> reportZipDownloadByNbbms(SjxxDto sjxxDto, HttpServletRequest request){
		List<String> nbbms = sjxxDto.getNbbms();
		List<String> ids = new ArrayList<>();
		if (!CollectionUtils.isEmpty(nbbms)){
			//根据 样本编号查询送检ids
			List<SjxxDto> listByYbbhs = dao.getDtoList(sjxxDto);
			if (!CollectionUtils.isEmpty(listByYbbhs)){
				for (SjxxDto listByYbbh : listByYbbhs) {
					ids.add(listByYbbh.getSjid());
				}
			}
		}
		sjxxDto.setIds(ids);
		return reportZipDownload(sjxxDto,request);
	}

	/**
	 * 根据条件下载报告压缩包
	 *
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@Override
	public Map<String, Object> reportZipDownload(SjxxDto sjxxDto, HttpServletRequest request) {
		List<String> ywids = sjxxDto.getIds();
		List<String> sjids = new ArrayList<>();
		if(ywids!=null&&ywids.size()>0){
			SjzzsqDto sjzzsqDto =new SjzzsqDto();
			sjzzsqDto.setIds(ywids);
			String sjidsByIds = sjzzsqDao.getSjidsByIds(sjzzsqDto);
			if(StringUtil.isNotBlank(sjidsByIds)){
				String[] split = sjidsByIds.split(",");
				Collections.addAll(sjids, split);
				sjxxDto.setIds(sjids);
			}
		}

		// 查询下载报告
		List<String> ids = sjxxDto.getIds();
		List<SjxxDto> sjxxDtos;
		//判断是否选中
		Map<String, Object> paraMap = pareMapFromDto(sjxxDto);
		paraMap.put("pdfywlxs",wechatCommonUtils.getInspectionPdfYwlxs());
		paraMap.put("wordywlxs",wechatCommonUtils.getInspectionWordYwlxs());
		if(ids != null && ids.size() > 0){
			// 根据选择查询报告信息(pdf结果)
			sjxxDtos = dao.selectDownloadReportByIds(paraMap);
		}else{
			// 根据条件查询报告信息(pdf结果)
			sjxxDtos = dao.selectDownloadReport(paraMap);
		}
		List<SjxxDto> sjxxDtos_t = new ArrayList<>();
		//遍历查询结果，取其中检测项目与之匹配的下载
		for (SjxxDto sjxxDto_t : sjxxDtos) {
			//根据送检id查询检测项目
			Map<String, String> sjxxMap = dao.getAllSjxxOther(sjxxDto_t.getSjid());
			sjxxDto_t.setBglxbj(sjxxDto.getBglxbj());
			if ("ALLPDF".equals(sjxxDto.getBglxbj().toUpperCase())){
				sjxxDtos_t.add(sjxxDto_t);
			}else {
				//拼接业务类型
				String ywlx = sjxxMap.get("cskz3") + "_" + sjxxMap.get("cskz1");
				if("WORD".equals(sjxxDto.getBglxbj().toUpperCase()))
					ywlx=ywlx+"_" + sjxxDto.getBglxbj().toUpperCase();
				if (ywlx.equals(sjxxDto_t.getYwlx())){
					sjxxDtos_t.add(sjxxDto_t);
				}
			}
		}
		return zipDownload(sjxxDtos_t);
	}

	/**
	 * 根据条件下载报告压缩包（伙伴权限限制）
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@Override
	public Map<String, Object> reportZipDownloadSjhb(SjxxDto sjxxDto, HttpServletRequest request) {
		// TODO Auto-generated method stub
		// 查询下载报告
		List<String> ids = sjxxDto.getIds();
		List<SjxxDto> sjxxDtos;
		//判断是否选中
		Map<String, Object> paraMap = pareMapFromDto(sjxxDto);
		paraMap.put("pdfywlxs",wechatCommonUtils.getInspectionPdfYwlxs());
		paraMap.put("wordywlxs",wechatCommonUtils.getInspectionWordYwlxs());
		if(ids != null && ids.size() > 0){
			// 根据选择查询报告信息(pdf结果)
			sjxxDtos = dao.selectDownloadReportByIds(paraMap);
		}else{
			// 根据条件查询报告信息(pdf结果)
			sjxxDtos = dao.selectDownloadReportSjhb(paraMap);
		}
		List<SjxxDto> sjxxDtos_t = new ArrayList<>();
		//遍历查询结果，取其中检测项目与之匹配的下载
		for (SjxxDto sjxxDto_t : sjxxDtos) {
			//根据送检id查询检测项目
			Map<String, String> sjxxMap = dao.getAllSjxxOther(sjxxDto_t.getSjid());
			sjxxDto_t.setBglxbj(sjxxDto.getBglxbj());
			if ("ALLPDF".equals(sjxxDto.getBglxbj().toUpperCase())){
				sjxxDtos_t.add(sjxxDto_t);
			}else {
				//拼接业务类型
				String ywlx = sjxxMap.get("cskz3") + "_" + sjxxMap.get("cskz1");
				if("WORD".equals(sjxxDto.getBglxbj().toUpperCase()))
					ywlx=ywlx+"_" + sjxxDto.getBglxbj().toUpperCase();
				if (ywlx.equals(sjxxDto_t.getYwlx())){
					sjxxDtos_t.add(sjxxDto_t);
				}
			}
		}
		return zipDownload(sjxxDtos_t);
	}

	/**
	 * 下载压缩报告
	 * @param sjxxDtos
	 * @return
	 */
	private Map<String, Object> zipDownload(List<SjxxDto> sjxxDtos){
		Map<String, Object> map = new HashMap<>();
		try {
			// TODO Auto-generated method stub
			// 判断是否大于1000条
			if (sjxxDtos != null && sjxxDtos.size() > 1000) {
				map.put("error", "超过1000个报告！");
				map.put("status", "fail");
				return map;
			} else if (sjxxDtos == null || sjxxDtos.size() == 0) {
				map.put("error", "未找到报告信息！");
				map.put("status", "fail");
				return map;
			}
			String key = String.valueOf(System.currentTimeMillis());
			redisUtil.hset("EXP_:_" + key, "Cnt", "0");
			map.put("count", sjxxDtos.size());
			map.put("redisKey", key);
			String folderName = "UP" + String.valueOf(System.currentTimeMillis());
			String storePath = pathPrefix + exportFilePath + BusTypeEnum.IMP_REPORTZIP.getCode() + "/" + folderName;
			mkDirs(storePath);
			map.put("srcDir", storePath);
			// 开启线程拷贝临时文件
			ReportZipExport reportZipExport = new ReportZipExport();
			reportZipExport.init(key, storePath, folderName, sjxxDtos, redisUtil);
			ReportZipThread reportZipThread = new ReportZipThread(reportZipExport);
			reportZipThread.start();
			map.put("status", "success");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 复制文件
	 *
	 * @param src
	 * @param dest
	 * @return
	 */
	public boolean copyFile(String src, String dest)
	{
		boolean flag = false;
		FileInputStream in = null;
		FileOutputStream out = null;
		try
		{
			in = new FileInputStream(src);
			File file = new File(dest);
			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);
			int c;
			byte[] buffer = new byte[1024];
			while ((c = in.read(buffer)) != -1)
			{
				for (int i = 0; i < c; i++)
					out.write(buffer[i]);
			}
			flag = true;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			try
			{
				in.close();
				out.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 发送日报统计连接给微信用户
	 */
	public void sendStatisDaily()
	{
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("STATISTIC_PARTNER");
		List<DdxxglDto> xtyhList = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		if (xtyhList != null && xtyhList.size() > 0)
		{
			for (int i = 0; i < xtyhList.size(); i++)
			{
				if (xtyhList.get(i).getYhid() != null)
				{
					try
					{
						WxyhDto wxyhDto = new WxyhDto();
						wxyhDto.setYhid(xtyhList.get(i).getYhid());
						wxyhDto = wxyhDao.getWxyhByYhid(xtyhList.get(i).getYhid());
						if (wxyhDto != null && wxyhDto.getWechatid() != null)
						{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String jsrq = sdf.format(new Date());
							String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(xtyhList.get(i).getYhid() + jsrq), "utf-8"), "utf-8");
							String reporturl = externalurl + "/ws/statictis/statictisByYhid?yhid=" + xtyhList.get(i).getYhid() + "&jsrq=" + jsrq + "&sign=" + sign + "&flg=dayily";
							log.info("微信日报：" + xtyhList.get(i).getZsxm() +"--" +reporturl);
//							Map<String, String> messageMap = new HashMap<>();
//							messageMap.put("title", "您好！");
//							messageMap.put("keyword1", jsrq);
//							messageMap.put("keyword2", "请点击查看");
//							messageMap.put("keyword3", null);
//							messageMap.put("keyword4", null);
//							messageMap.put("reporturl", reporturl);
//							messageMap.put("remark", "送检统计已生成，快点击查看吧！");
//							commonService.sendWeChatMessageMap("templatedm",wxyhDto.getWechatid(),null,messageMap);
							commonService.sendWeChatMessage(kpi_templateid, wxyhDto.getWechatid(), "您好！", jsrq, "请点击查看", null, null, "送检统计已生成，快点击查看吧！", reporturl);
						}
					} catch (UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 发送日报至小程序（根据伙伴权限进行限制）
	 * @return
	 */
	public Map<String, Object> sendStatisDailyToMiniProgram(String yhid,HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		if(StringUtil.isNotBlank(yhid)) {
			String jsrq=request.getParameter("jsrq");
			if(jsrq=="" || jsrq==null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				jsrq = sdf.format(new Date());
			}
			try {
				String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(yhid + jsrq), "utf-8"), "utf-8");
				String external = externalurl + "/ws/statictis/statictisByYhid?yhid=" + yhid +"&jsrq=" + jsrq + "&sign=" + sign + "&flg=dayily";
				map.put("url", external);
				String internalbtn = applicationurl + "/ws/statictis/statictisByYhid?yhid=" + yhid +"&jsrq=" + jsrq + "&sign=" + sign + "&flg=dayily&netflg=inter";
				map.put("interurl", internalbtn);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 发送周报统计连接给微信用户
	 *//*
	public void sendStatisWeekly()
	{
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("STATISTIC_PARTNER");
		List<DdxxglDto> xtyhList = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		if (xtyhList != null && xtyhList.size() > 0)
		{
			for (int i = 0; i < xtyhList.size(); i++)
			{
				if (xtyhList.get(i).getYhid() != null)
				{
					WxyhDto wxyhDto = new WxyhDto();
					wxyhDto.setYhid(xtyhList.get(i).getYhid());
					wxyhDto = wxyhDao.getWxyhByYhid(xtyhList.get(i).getYhid());
					if (wxyhDto != null && wxyhDto.getWechatid() != null)
					{
						try
						{
							int dayOfWeek = DateUtils.getWeek(new Date());
							String jsrqstart;
							String jsrqend;
							if (dayOfWeek <= 2)
							{
								jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd");
								jsrqend = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 1), "yyyy-MM-dd");
							} else
							{
								jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd");
								jsrqend = DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd");
							}
							String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(xtyhList.get(i).getYhid() + jsrqstart + jsrqend), "utf-8"), "utf-8");
							String reporturl = externalurl + "/ws/statictis/statictisByYhid?yhid=" + xtyhList.get(i).getYhid() + "&jsrqstart=" + jsrqstart + "&jsrqend=" + jsrqend + "&sign=" + sign
									+ "&flg=weekly";
							log.info("微信周报：" + xtyhList.get(i).getZsxm() +"--"+reporturl);
							commonService.sendWeChatMessage(kpi_templateid, wxyhDto.getWechatid(), "您好！", jsrqstart + "~" + jsrqend, "请点击查看", null, null, "送检统计已生成，快点击查看吧！", reporturl);
						} catch (UnsupportedEncodingException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}*/
	/**
	 * 发送统计连接给送检人员
	 *
	 * @return
	 */
	public boolean sendStatisWeekly()
	{
		// TODO Auto-generated method stub
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("STATISTIC_PARTNER");
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		String jsrqstart;
		String jsrqend;
		int dayOfWeek = DateUtils.getWeek(new Date());
		if (dayOfWeek <= 2){
			jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd");
			jsrqend = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd");
		} else{
			jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd");
			jsrqend = DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd");
		}
		if (ddxxglDtolist != null && ddxxglDtolist.size() > 0)
		{
			for (int i = 0; i < ddxxglDtolist.size(); i++)
			{
				String external = null;
				try
				{
					String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(ddxxglDtolist.get(i).getYhid() + jsrqstart + jsrqend), "utf-8"), "utf-8");
					// 外网访问
					external = externalurl + "/ws/statictis/statictisByYhid?yhid=" + ddxxglDtolist.get(i).getYhid()+ "&jsrqstart=" + jsrqstart + "&jsrqend=" + jsrqend + "&sign=" + sign + "&flg=weekly";
				} catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<BtnJsonList> btnJsonLists = new ArrayList<>();
				BtnJsonList btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("详细");
				btnJsonList.setActionUrl(external);
				btnJsonLists.add(btnJsonList);
				if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid()))
				{
					talkUtil.sendCardMessage(ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(), xxglService.getMsg("ICOMM_ZH00003"),
							xxglService.getMsg("ICOMM_ZH00004", jsrqstart + "~~" + jsrqend, DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
				}
			}
		}
		return true;
	}

	/**
	 * 小程序查看周报
	 * @param yhid
	 * @return
	 */
	public Map<String, Object> sendStatisWeeklyToMiniProgram(String yhid,HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		if(StringUtil.isNotBlank(yhid)){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			int dayOfWeek;
			String jsrq=request.getParameter("jsrq");
			String jsrqstart="";
			String jsrqend="";
			try {
				if(StringUtils.isNotBlank(jsrq)) {
					dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
				}else {
					jsrq=sf.format(new Date());
					dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
				}
				if (dayOfWeek <= 2) {
					jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 7), "yyyy-MM-dd");
					jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 1), "yyyy-MM-dd");
				} else {
					jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek), "yyyy-MM-dd");
					jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), 6 - dayOfWeek), "yyyy-MM-dd");
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(yhid + jsrqstart + jsrqend), "utf-8"), "utf-8");
				String external = externalurl + "/ws/statictis/statictisByYhid?yhid=" + yhid + "&jsrqstart=" + jsrqstart + "&jsrqend=" + jsrqend + "&sign=" + sign + "&flg=weekly";
				map.put("url", external);
				String internalbtn = applicationurl + "/ws/statictis/statictisByYhid?yhid=" + yhid + "&jsrqstart=" + jsrqstart + "&jsrqend=" + jsrqend + "&sign=" + sign + "&flg=weekly&netflg=inter";
				map.put("interurl", internalbtn);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * 接收外部送检信息
	 * @param request
	 * @param jsonStr
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean receiveInspectInfo(HttpServletRequest request, String jsonStr) throws BusinessException {
		// TODO Auto-generated method stub
		List<SjxxDto> sjxxDtos = JSONObject.parseArray(jsonStr, SjxxDto.class);
		List<String> unCompareInfo = new ArrayList<>();
		RestTemplate t_restTemplate = new RestTemplate();
		if(sjxxDtos != null && sjxxDtos.size() > 0){
			for (int i = 0; i < sjxxDtos.size(); i++) {
				if (StringUtil.isNotBlank(sjxxDtos.get(i).getDb())){
				sjxxDtos.get(i).setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(
						new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.INSPECTION_DIVISION,
								BasicDataTypeEnum.PATHOGENY_TYPE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT});

				//匹配前期检测(代码)
				List<SjqqjcDto> sjqqjcDtos = new ArrayList<>();
				JSONArray qqjclist = JSONObject.parseArray(sjxxDtos.get(i).getQqjc());
				List<JcsjDto> qqjcJcsj = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_TYPE.getCode());
				//获取到前期检测的json数组。根据名称匹配参数名称，如参数名称没匹配上则去尝试匹配参数扩展的别名
				//注意，别名格式为a,b,c   json格式为"qqjc":[{"qqjcmc":"PCT","qqjcz":"11"},{"qqjcmc":"白细胞","qqjcz":"12"}]
				if (qqjclist != null && qqjclist.size()>0){
					for (int k=0; k<qqjclist.size(); k++){
						String qqjcmc = ((JSONObject) qqjclist.get(k)).getString("qqjcmc");
						String qqjcz = ((JSONObject) qqjclist.get(k)).getString("qqjcz");
						for (JcsjDto jcsjDto: qqjcJcsj){
							if (   qqjcmc.equals(jcsjDto.getCsmc()) && StringUtil.isNotBlank(qqjcz)  ){//对比基础数据的参数名称
								//传入的前期检测和基础数据对比
								SjqqjcDto sjqqjcDto = new SjqqjcDto();
								sjqqjcDto.setYjxm(jcsjDto.getCsid());
								sjqqjcDto.setJcz(qqjcz);
								sjqqjcDtos.add(sjqqjcDto);
								break;
							}else {
								//对比参数名称没匹配上，则对比参数扩展3的别名
								if (StringUtil.isNotBlank(jcsjDto.getCskz3())){
									String[] cskzlist = jcsjDto.getCskz3().split(",");
									for (String mc: cskzlist){
										if (qqjcmc.equals(mc) && StringUtil.isNotBlank(qqjcz)){
											SjqqjcDto sjqqjcDto = new SjqqjcDto();
											sjqqjcDto.setYjxm(jcsjDto.getCsid());
											sjqqjcDto.setJcz(qqjcz);
											sjqqjcDtos.add(sjqqjcDto);
											break;
										}
									}
								}
							}
						}
					}
				}
				sjxxDtos.get(i).setSjqqjcs(sjqqjcDtos);
				//log.error("----------receiveInspectInfo---------- 匹配前期检测 :"+ JSONObject.toJSONString(sjqqjcDtos));

				// 匹配送检单位
				YyxxDto yyxxDto = new YyxxDto();
				String sjdwmc = sjxxDtos.get(i).getSjdw();
				if (StringUtil.isNotBlank(sjxxDtos.get(i).getSjdwmc())){
					sjdwmc = sjxxDtos.get(i).getSjdwmc();
				}
				if(StringUtil.isBlank(sjdwmc)) {
					// 查询其它医院id
					List<YyxxDto> yyxxDtos = yyxxService.selectOther();
					if(yyxxDtos != null && yyxxDtos.size() > 0) {
						sjxxDtos.get(i).setSjdw(yyxxDtos.get(0).getDwid());
					}else {
						sjxxDtos.get(i).setSjdw("未设置其它医院(dwmc=第三方)！");
					}
					sjxxDtos.get(i).setSjdwmc("-");
				}else {
					yyxxDto.setDwmc(sjdwmc);
					List<YyxxDto> yyxxList = yyxxService.queryByDwmc(yyxxDto);
					if(yyxxList != null && yyxxList.size() > 0) {
						// 取第一个保存
						sjxxDtos.get(i).setSjdw(yyxxList.get(0).getDwid());
						sjxxDtos.get(i).setSjdwmc(yyxxList.get(0).getDwmc());
					}else {
						// 查询其它医院id
						List<YyxxDto> yyxxDtos = yyxxService.selectOther();
						if(yyxxDtos != null && yyxxDtos.size() > 0) {
							sjxxDtos.get(i).setSjdw(yyxxDtos.get(0).getDwid());
						}else {
							sjxxDtos.get(i).setSjdw("未设置其它医院(dwmc=第三方)！");
						}
						sjxxDtos.get(i).setSjdwmc(sjdwmc);
					}
				}
				//log.error("----------receiveInspectInfo---------- 匹配送检单位 sjdw:"+ sjxxDtos.get(i).getSjdw()+" sjdwmc:" + sjxxDtos.get(i).getSjdwmc());

				// 匹配送检区分
				// 先用json的DB代表字段查送检伙伴，查找到送检伙伴取伙伴表sjqf字段，查找不到送检伙伴或者json未传送检区分字段的取基础数据送检区分的默认设置
				String sjqf = sjxxDtos.get(i).getSjqf();
				SjhbxxDto sjhb = null;
				if (StringUtil.isNotBlank(sjxxDtos.get(i).getDb())){
					sjhb = new SjhbxxDto();
					sjhb.setHbmc(sjxxDtos.get(i).getDb());
					sjhb = sjhbxxService.getDto(sjhb);
				}
				if(sjhb != null && StringUtil.isNotBlank(sjhb.getSjqf())){
					//查找到送检伙伴信息的，送检区分从伙伴信息中取
					sjqf = sjhb.getSjqf();
				} else {
					//查找不到送检伙伴的，取基础数据默认设置
//					String sjqf = null;
					List<JcsjDto> distinguish = jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
					if(distinguish!=null) {
						for (int j = 0; j < distinguish.size(); j++) {
							if(distinguish.get(j).getCsdm().equals(sjxxDtos.get(i).getSjqf())){
								sjqf = distinguish.get(j).getCsid();
								break;
							}
							if("1".equals(distinguish.get(j).getSfmr())) {
								sjqf = distinguish.get(j).getCsid();
							}
						}
					}
				}

				sjxxDtos.get(i).setSjqf(sjqf);
				//log.error("----------receiveInspectInfo---------- 匹配送检区分 sjqf:"+ sjqf);

				//匹配科室(名称)
				List<SjdwxxDto> sjdwxxList= getSjdw();
				String ks = null;
				String qtks= null;
				String unCompareKsid = "";
				for (int j = 0; j < sjdwxxList.size(); j++) {
					if(sjdwxxList.get(j).getDwmc().equals(sjxxDtos.get(i).getKs())){
						ks = sjdwxxList.get(j).getDwid();
						qtks = null;
						if ("1".equals(sjdwxxList.get(j).getKzcs())){
							qtks = sjxxDtos.get(i).getKs();
						}
						break;
					}else if (StringUtil.isNotBlank(sjdwxxList.get(j).getKzcs3())){
						String[] ppkss = sjdwxxList.get(j).getKzcs3().split(",");
						boolean isCompare = false;
						for (String ppks : ppkss) {
							if (ppks.equals(sjxxDtos.get(i).getKs())){
								ks = sjdwxxList.get(j).getDwid();
								qtks = null;
								if ("1".equals(sjdwxxList.get(j).getKzcs())){
									qtks = sjxxDtos.get(i).getKs();
								}
								isCompare = true;
								break;
							}
						}
						if(isCompare){
							break;
						}
					}
					if("00999".equals(sjdwxxList.get(j).getDwdm())){
						ks = sjdwxxList.get(j).getDwid();
						qtks = sjxxDtos.get(i).getKs();
						unCompareKsid = ks;
					}
				}
				sjxxDtos.get(i).setKs(ks);
				sjxxDtos.get(i).setQtks(qtks);
				if(StringUtil.isNotBlank(unCompareKsid) && ks.equals(unCompareKsid)){
					if(!("-".equals(qtks) || "--".equals(qtks))){
						//将未匹配的科室
						unCompareInfo.add((unCompareInfo.size()+1)+"、伙伴："+sjxxDtos.get(i).getDb()+",\n\n未匹配科室："+qtks+",\n\n标本:"+sjxxDtos.get(i).getWbbm());
					}
				}
				//log.error("----------receiveInspectInfo---------- 匹配科室 ks:"+ ks+" qtks:" + qtks);

				//匹配关注病原(名称)
				List<JcsjDto> pathogenylist = jclist.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode());
				List<String> bys = sjxxDtos.get(i).getBys();
				if(bys!=null &&bys.size()>0) {
					for (int j = 0; j < pathogenylist.size(); j++) {
						for (int k = 0; k < bys.size(); k++) {
							if(bys.get(k).equals(pathogenylist.get(j).getCsmc())){
								bys.set(k, pathogenylist.get(j).getCsid());
								break;
							}
						}
					}
				}
				sjxxDtos.get(i).setBys(bys);
				//log.error("----------receiveInspectInfo---------- 匹配关注病原 :"+ JSONObject.toJSONString(bys));

				//匹配标本类型(名称)
				List<JcsjDto> samplelist = jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
				String yblx = null;
				String yblxmc = null;
				String yblxdm = null;
				//判断标本类型时，进行特殊处理 如果为全血或者 CFDNA血液 则设置为外周血
				if ("全血".equals(sjxxDtos.get(i).getYblx()) || "血液(cfDNA)".equals(sjxxDtos.get(i).getYblx())
						|| "血液".equals(sjxxDtos.get(i).getYblx())){
					sjxxDtos.get(i).setYblx("外周血");
				}
				for (int j = 0; j < samplelist.size(); j++) {
					if(samplelist.get(j).getCsmc().equals(sjxxDtos.get(i).getYblx())){
						yblx = samplelist.get(j).getCsid();
						yblxdm = samplelist.get(j).getCsdm();
						yblxmc = null;
						if("1".equals(samplelist.get(j).getCskz1())){
							yblxmc = sjxxDtos.get(i).getYblx();
						}
						break;
					}
					if("1".equals(samplelist.get(j).getCskz1())){
						yblx = samplelist.get(j).getCsid();
						yblxmc = sjxxDtos.get(i).getYblx();
						yblxdm = samplelist.get(j).getCsdm();
					}
				}
				sjxxDtos.get(i).setYblx(yblx);
				sjxxDtos.get(i).setYblxmc(yblxmc);
				sjxxDtos.get(i).setYblxdm(yblxdm);
				//log.error("----------receiveInspectInfo---------- 匹配标本类型 yblx:"+ yblx+" yblxmc:" + yblxmc);

				//匹配检测单位(代码)
				String jcdw = null;
				//若接收到送检信息中参数扩展1不为空，匹配迪安实验室数据，查找对应的检测单位基础数据
				if (sjxxDtos.get(i).getCskz1()!=null && !"".equals(sjxxDtos.get(i).getCskz1().replaceAll(" ",""))){
					SysxxDto sysxxDto = sysxxService.getSysxxDtoByDfsysmc(sjxxDtos.get(i).getCskz1());
					if (sysxxDto != null){
						jcdw = sysxxDto.getWfsys();
						sjxxDtos.get(i).setJcdw(sysxxDto.getWfsys());
					}
				}
				//若cskz1未匹配上实验室
				if (StringUtil.isBlank(jcdw)) {
					List<JcsjDto> decetionlist = jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
					for (int j = 0; j < decetionlist.size(); j++) {
						if(decetionlist.get(j).getCsdm().equals(sjxxDtos.get(i).getJcdw())){
							jcdw = decetionlist.get(j).getCsid();
							sjxxDtos.get(i).setJcdwmc(decetionlist.get(j).getCsmc());
							break;
						}
						if ( "1".equals(decetionlist.get(j).getSfmr()) ){
							jcdw = decetionlist.get(j).getCsid();
							sjxxDtos.get(i).setJcdwmc(decetionlist.get(j).getCsmc());
						}
					}
					sjxxDtos.get(i).setJcdw(jcdw);
				}
				//log.error("----------receiveInspectInfo---------- 匹配检测单位 :"+ sjxxDtos.get(i).getJcdw());
				//匹配检测项目(代码)
				boolean isThirdProject = true;
				//判断送检伙伴是否被限制3.0项目
				List<SjhbxxDto> hbxzDtos = sjxxCommonService.getHbxzDtoByHbmc(sjxxDtos.get(i).getDb());
				if (hbxzDtos!=null && hbxzDtos.size()>0){
//					for (SjhbxxDto sjhbxxDto : hbxzDtos) {
//						//若送检伙伴是否在伙伴限制表中，且对应实验室匹配，则只送检2.0项目
//						if (sjxxDtos.get(i).getJcdw().equals(sjhbxxDto.getSysid())){
//							isThirdProject = false;
//							break;
//						}
//					}
					isThirdProject = false;
				}
				if (isThirdProject){
					//若系统设置中有thirdProject.jcdw配置，且有值
					Object xtsz_o = redisUtil.hget("matridx_xtsz", "thirdProject.jcdw");
					if (xtsz_o!=null){
						String xtsz_s = xtsz_o.toString();
						if (StringUtil.isNotBlank(xtsz_s)){
							XtszDto xtszDto = JSON.parseObject(xtsz_s, XtszDto.class);
							String thirdJcdw = xtszDto.getSzz();//若检测单位代码在thirdJcdw中，则匹配3.0项目，默认杭州实验室
							//后期若系统设置删除，则表示所有实验室均已更新到3.0系统
							JcsjDto jcdwDto = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode(),sjxxDtos.get(i).getJcdw());
							if (StringUtil.isNotBlank(thirdJcdw) && !thirdJcdw.contains(jcdwDto.getCsdm())){
								//若系统设置中thirdProject.jcdw配置不为空，且不包含当前检测单位代码，则匹配2.0项目
								isThirdProject = false;
							}
						}
					}
				}
				List<JcsjDto> datectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
				List<String> jcxmids = new ArrayList<>();
				for (int j = 0; j < datectlist.size(); j++) {
					if (isThirdProject){
						//匹配3.0不带Onco项目
						if (datectlist.get(j).getCskz1().equals(sjxxDtos.get(i).getJcxm()) && "IMP_REPORT_SEQ_INDEX_TEMEPLATE".equals(datectlist.get(j).getCskz3())){
							jcxmids.add(datectlist.get(j).getCsid());
							sjxxDtos.get(i).setJcxmids(jcxmids);
							break;
						}
					}else {
						if(datectlist.get(j).getCskz1().equals(sjxxDtos.get(i).getJcxm()) && "IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(datectlist.get(j).getCskz3())){
							jcxmids.add(datectlist.get(j).getCsid());
							sjxxDtos.get(i).setJcxmids(jcxmids);
							break;
						}
					}
					String t_cskz3 = datectlist.get(j).getCskz3()==null?"":datectlist.get(j).getCskz3();
					if ("T".equals(sjxxDtos.get(i).getJcxm()) && "C".equals(datectlist.get(j).getCskz1()) && "IMP_REPORT_SEQ_TNGS".equals(t_cskz3)){
						jcxmids.add(datectlist.get(j).getCsid());
						sjxxDtos.get(i).setJcxmids(jcxmids);
						break;
					}else if ("K".equals(sjxxDtos.get(i).getJcxm()) && "C".equals(datectlist.get(j).getCskz1()) 
							&& t_cskz3.contains("IMP_REPORT_SEQ_TNGS")){
						jcxmids.add(datectlist.get(j).getCsid());
						sjxxDtos.get(i).setJcxmids(jcxmids);
						break;
					}
					if (datectlist.get(j).getCskz1().equals(sjxxDtos.get(i).getJcxm()) && "IMP_REPORT_RFS_TEMEPLATE".equals(datectlist.get(j).getCskz3())){//RFS项目
						jcxmids.add(datectlist.get(j).getCsid());
						sjxxDtos.get(i).setJcxmids(jcxmids);
						break;
					}
				}
				//log.error("----------receiveInspectInfo---------- 匹配检测项目 :"+ JSONObject.toJSONString(sjxxDtos.get(i).getJcxmids()));

				//匹配快递类型(代码)
				List<JcsjDto> expressage = jclist.get(BasicDataTypeEnum.SD_TYPE.getCode());
				for (int j = 0; j < expressage.size(); j++) {
					if(expressage.get(j).getCsdm().equals(sjxxDtos.get(i).getKdlx())){
						sjxxDtos.get(i).setKdlx(expressage.get(j).getCsid());
						break;
					}
				}
				//log.error("----------receiveInspectInfo---------- 匹配快递类型 :"+ sjxxDtos.get(i).getKdlx());


				// 匹配是否收费，没匹配到默认为是
				if("否".equals(sjxxDtos.get(i).getSfsf())) {
					sjxxDtos.get(i).setSfsf("0");
				}else if("免D".equals(sjxxDtos.get(i).getSfsf())) {
					sjxxDtos.get(i).setSfsf("2");
				}else if("免R".equals(sjxxDtos.get(i).getSfsf())) {
					sjxxDtos.get(i).setSfsf("3");
				}else {
					sjxxDtos.get(i).setSfsf("1");
				}
				//log.error("----------receiveInspectInfo---------- 匹配是否收费 :"+ sjxxDtos.get(i).getSfsf());


				// 获取默认金额
				if (sjxxDtos.get(i).getFkbj()==null||"0".equals(sjxxDtos.get(i).getFkbj()))
				{
					SjhbxxDto sjhbxxDto = new SjhbxxDto();
					if (sjxxDtos.get(i).getJcxmids() != null && sjxxDtos.get(i).getJcxmids().size() > 0){
						List<SjjcxmDto> sjjcxms = new ArrayList<>();
						for (String jcxmid : sjxxDtos.get(i).getJcxmids()) {
							SjjcxmDto sjjcxmDto = new SjjcxmDto();
							sjjcxmDto.setJcxmid(jcxmid);
							sjjcxms.add(sjjcxmDto);
						}
						sjxxDtos.get(i).setJcxm(JSON.toJSONString(sjjcxms));
						sjhbxxDto.setSjxms(sjjcxms);
					}else {
						List<SjjcxmDto> sjjcxms = sjxxDtos.get(i).getSjjcxms();
						sjhbxxDto.setSjxms(sjjcxms);
					}
					sjhbxxDto.setHbmc(sjxxDtos.get(i).getDb());
					SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
					if (re_sjhbxxDto != null)
					{
						if("0".equals(sjxxDtos.get(i).getSfsf())) {
							sjxxDtos.get(i).setFkje("0");
						}else if("1".equals(sjxxDtos.get(i).getSfsf())) {
							sjxxDtos.get(i).setFkje(re_sjhbxxDto.getSfbz());
						}else if("2".equals(sjxxDtos.get(i).getSfsf())||"3".equals(sjxxDtos.get(i).getSfsf())) {
							String fkje=re_sjhbxxDto.getSfbz();
							if(fkje!=null) {
								fkje=(Double.parseDouble(fkje) / 2)+"";
								sjxxDtos.get(i).setFkje(fkje);
							}
						}else {
							sjxxDtos.get(i).setFkje(re_sjhbxxDto.getSfbz());
						}
					}
				}

				//保存数据，默认为80状态
				sjxxDtos.get(i).setYbbh(sjxxDtos.get(i).getWbbm());
				/*
				 *	1.获取数据后要优先判断 标本编号是否存在，如果不存在则返回错误
				 * 	2.如果存在才去后台确认标本编号是否已使用。
				 * 	3.如果已经使用则返回错误信息
				 */
				if(StringUtil.isNotBlank(sjxxDtos.get(i).getYbbh())){
//					int countybbh = getCountByybbh(sjxxDtos.get(i));
//					if (countybbh > 0) {
//						throw new BusinessException("msg","标本编号已经存在,不能重复使用！ybbh:" + sjxxDtos.get(i).getYbbh());
//					}
					//更改上述逻辑
					//样本编号不为空时，根据样本编号查数据库，查到则更新，未查到则插入
					//根据样本编号查数据，查出多条SQL只取第一条
					//当改数据存在时看是否接收，未接受则更新，接收则报编号不能重复使用错误
					List<SjxxDto> sjxxs = dao.getDtosByYbbh(sjxxDtos.get(i).getYbbh());
					if (sjxxs != null && sjxxs.size() > 0){
						if (StringUtil.isBlank(sjxxs.get(0).getJsrq())){
							sjxxDtos.get(i).setSjid(sjxxs.get(0).getSjid());
							boolean isupdate = update(sjxxDtos.get(i));
							if (!isupdate){ throw new BusinessException("msg","本地更新失败"); }
							boolean result_gl = insertAll(sjxxDtos.get(i),sjxxDtos.get(i).getXgry());//关联表信息更新（先删除后插入，故新增和更新都可公用）
							if (!result_gl)
								throw new BusinessException("msg","关联表保存失败！");
							RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(sjxxDtos.get(i)));

							//送检实验管理表操作
							List<JcsjDto> distinguish = jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
							String sjqfdm = "";
							if(distinguish!=null) {
								for (int j = 0; j < distinguish.size(); j++) {
									if(distinguish.get(j).getCsdm().equals(sjxxDtos.get(i).getSjqf())){
										sjqfdm = distinguish.get(j).getCsdm();
										break;
									}
								}
							}
							sjxxDtos.get(i).setSjqf(sjqfdm);
							//筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
							//清除之前的数据，重新设置sjid查询所有复检信息
							FjsqDto fjsqDto_t = new FjsqDto();
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							String nowDate=sdf.format(new Date());
							fjsqDto_t.setDsyrq(nowDate);
							fjsqDto_t.setSjid(sjxxDtos.get(i).getSjid());
							List<FjsqDto> resultFjDtos = fjsqService.getSyxxByFj(fjsqDto_t);
							if(resultFjDtos!=null && resultFjDtos.size() > 0) {
								List<String> ids = new ArrayList<String>();
								for(FjsqDto res_FjsqDto:resultFjDtos) {
									ids.add(res_FjsqDto.getFjid());
								}
								sjxxDtos.get(i).setIds(ids);
							}
							List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDtos.get(i),null, DetectionTypeEnum.DETECT_SJ.getCode());
							
							if (!CollectionUtils.isEmpty(insertInfo)){
								//更新项目实验数据和送检实验数据
								boolean isSuccess = addOrUpdateSyData(insertInfo,sjxxDtos.get(i),sjxxDtos.get(i).getLrry());
								if (isSuccess){
									SjsyglDto sjsyglDto_t=new SjsyglDto();
									sjsyglDto_t.setSjid(sjxxDtos.get(i).getSjid());
									List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
									RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
									XmsyglDto xmsyglDto_t = new XmsyglDto();
									xmsyglDto_t.setYwid(sjxxDtos.get(i).getSjid());
									List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
									RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
								}
							}
						}else{
							throw new BusinessException("msg","标本编号已经存在,不能重复使用！ybbh:" + sjxxDtos.get(i).getYbbh());
						}
					}else{
						sjxxDtos.get(i).setLrry(sjxxDtos.get(i).getWbbm());
						sjxxDtos.get(i).setZt(StatusEnum.CHECK_PASS.getCode());
						boolean result = insertDto(sjxxDtos.get(i));
						if (!result)
							throw new BusinessException("msg","本地保存失败！");
						boolean result_gl = insertAll(sjxxDtos.get(i),sjxxDtos.get(i).getLrry());//关联表信息更新（先删除后插入，故新增和更新都可公用）
						if (!result_gl)
							throw new BusinessException("msg","关联表保存失败！");
						RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADD.getCode() + JSONObject.toJSONString(sjxxDtos.get(i)));

						//送检实验管理表操作
						List<JcsjDto> distinguish = jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
						String sjqfdm = "";
						if(distinguish!=null) {
							for (int j = 0; j < distinguish.size(); j++) {
								if(distinguish.get(j).getCsid().equals(sjxxDtos.get(i).getSjqf())){
									sjqfdm = distinguish.get(j).getCsdm();
									break;
								}
							}
						}
						sjxxDtos.get(i).setSjqf(sjqfdm);
						
						//筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
						//清除之前的数据，重新设置sjid查询所有复检信息
						FjsqDto fjsqDto_t = new FjsqDto();
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
						String nowDate=sdf.format(new Date());
						fjsqDto_t.setDsyrq(nowDate);
						fjsqDto_t.setSjid(sjxxDtos.get(i).getSjid());
						List<FjsqDto> resultFjDtos = fjsqService.getSyxxByFj(fjsqDto_t);
						if(resultFjDtos!=null && resultFjDtos.size() > 0) {
							List<String> ids = new ArrayList<String>();
							for(FjsqDto res_FjsqDto:resultFjDtos) {
								ids.add(res_FjsqDto.getFjid());
							}
							sjxxDtos.get(i).setIds(ids);
						}
						List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDtos.get(i),null, DetectionTypeEnum.DETECT_SJ.getCode());
						
						if (!CollectionUtils.isEmpty(insertInfo)){
							//更新项目实验数据和送检实验数据
							boolean isSuccess = addOrUpdateSyData(insertInfo,sjxxDtos.get(i),sjxxDtos.get(i).getLrry());
							if (isSuccess){
								SjsyglDto sjsyglDto_t=new SjsyglDto();
								sjsyglDto_t.setSjid(sjxxDtos.get(i).getSjid());
								//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
								List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto_t);
								RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_ADD.getCode() + JSONObject.toJSONString(list));
								XmsyglDto xmsyglDto_t = new XmsyglDto();
								xmsyglDto_t.setYwid(sjxxDtos.get(i).getSjid());
								List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
								RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
							}
						}
					}
				}else {
					throw new BusinessException("msg","标本编号为空");
				}

				List<FjcfbDto> fjcfbDtos = new ArrayList<>();
				DBEncrypt dbEncrypt = new DBEncrypt();
				//判断是否有文件
				if(request instanceof MultipartHttpServletRequest) {
					MultipartFile t_file = ((MultipartHttpServletRequest) request).getFile("file");
					if(t_file != null){
						//送检附件
						List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
						MultipartFile[] imp_file = new MultipartFile[files.size()];
						files.toArray(imp_file);
						List<String> wjms = sjxxDtos.get(i).getWjms();
						if(wjms != null && wjms.size() > 0){
							for (int j = 0; j < wjms.size(); j++) {
								if(imp_file!=null&& imp_file.length>0){
									for (int k = 0; k < imp_file.length; k++) {
										if(!imp_file[k].isEmpty()){
											MultipartFile file = imp_file[k];
											String wjm = file.getOriginalFilename();
											if(StringUtil.isNotBlank(wjm) && wjm.equals(wjms.get(j))){
												// 根据日期创建文件夹
												String fwjlj = prefix + releaseFilePath + sjxxDtos.get(i).getYwlx() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
														+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
												int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
												String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
												String fwjm = t_name + wjm.substring(index);
												String wjlj = fwjlj + "/" + fwjm;
												mkDirs(fwjlj);
												FjcfbDto fjcfbDto = new FjcfbDto();
												fjcfbDto.setFjid(StringUtil.generateUUID());
												fjcfbDto.setYwid(sjxxDtos.get(i).getSjid());
												fjcfbDto.setZywid("");
												fjcfbDto.setXh("1");
												fjcfbDto.setYwlx(sjxxDtos.get(i).getYwlx());
												fjcfbDto.setWjm(wjm);
												fjcfbDto.setWjlj(dbEncrypt.eCode(wjlj));
												fjcfbDto.setFwjlj(dbEncrypt.eCode(fwjlj));
												fjcfbDto.setFwjm(dbEncrypt.eCode(fwjm));
												fjcfbDto.setZhbj("0");
												boolean result = fjcfbService.insert(fjcfbDto);
												if (!result)
													throw new BusinessException("msg","附件保存失败！");
												byte[] buffer = new byte[4096];
												FileOutputStream fos = null;
												BufferedOutputStream output = null;
												InputStream fis = null;
												BufferedInputStream input = null;
												try {
													fis = file.getInputStream();
													input = new BufferedInputStream(fis);
													mkDirs(fwjlj);
													fos = new FileOutputStream(wjlj);
													output = new BufferedOutputStream(fos);
													int n;
													while ((n = input.read(buffer, 0, 4096)) > -1) {
														output.write(buffer, 0, n);
													}
												} catch (Exception e) {
													e.printStackTrace();
													return false;
												} finally {
													closeStream(new Closeable[] { fis, input, output, fos });
												}
												fjcfbDtos.add(fjcfbDto);
											}
										}
									}
								}
							}
						}
						// 附件排序
						FjcfbDto fjcfbDto = new FjcfbDto();
						fjcfbDto.setYwid(sjxxDtos.get(i).getSjid());
						fjcfbDto.setYwlx(sjxxDtos.get(i).getYwlx());
						boolean result_f = fjcfbService.fileSort(fjcfbDto);
						if (!result_f)
							log.error("附件排序失败！");
						if(fjcfbDtos.size() > 0){
							// 拷贝文件到微信服务器
							for (int j = 0; j < fjcfbDtos.size(); j++) {
								FjcfbModel fjcfbModel = fjcfbDtos.get(j);
								if (fjcfbModel != null) {
									String wjlj = fjcfbModel.getWjlj();
									String pathString = dbEncrypt.dCode(wjlj);
									File file = new File(pathString);
									// 文件不存在不做任何操作
									if (file.exists()){
										byte[] bytesArray = new byte[(int) file.length()];
										FileInputStream fis;
										try {
											fis = new FileInputStream(file);
											fis.read(bytesArray); // read file into bytes[]
											fis.close();
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											log.equals("未找到文件："+e.toString());
											throw new BusinessException("msg","未找到文件!");
										} catch (IOException e) {
											// TODO Auto-generated catch block
											log.equals("读写文件异常："+e.toString());
											throw new BusinessException("msg","读写文件异常！");
										}
										// 需要给文件的名称
										ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray) {
											@Override
											public String getFilename() {
												return file.getName();
											}
										};
										MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
										paramMap.add("file", contentsAsResource);
										paramMap.add("fjcfbModel", fjcfbModel);
										// 发送文件到服务器
										String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReport", paramMap, String.class);
										if ("OK".equals(reString)) {
											// 更新文件的转换标记为true
											boolean result_zhbj = fjcfbService.updateZhbj(fjcfbModel);
											if (!result_zhbj)
												throw new BusinessException("msg","转换标记更新失败！");
										}
									}
								}
							}
						}
					}
				}
				// 发送钉钉消息
				String url = menuurl + "/common/view/displayView?view_url=/common/view/inspectionView?ybbh=" + sjxxDtos.get(i).getYbbh() + "&hzxm=" + sjxxDtos.get(i).getHzxm();
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("INSPECTION_TYPE");
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				if (ddxxglDtos != null && ddxxglDtos.size() > 0) {
					String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
					String ICOMM_SJ00001 = xxglService.getMsg("ICOMM_SJ00001");
					for (int j = 0; j < ddxxglDtos.size(); j++) {
						if (StringUtil.isNotBlank(ddxxglDtos.get(j).getDdid())) {
							talkUtil.sendWorkMessage(ddxxglDtos.get(j).getYhm(), ddxxglDtos.get(j).getDdid(), ICOMM_SJ00002, StringUtil.replaceMsg(ICOMM_SJ00001,
									sjxxDtos.get(i).getYbbh(),sjxxDtos.get(i).getDb(), sjxxDtos.get(i).getSjdwmc(), sjxxDtos.get(i).getYblxmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), url);
						}
					}
				}
			}else {
				throw new BusinessException("msg","未明确送检伙伴，请重新传递！");
			}
			}
		}
		if (unCompareInfo.size()>0){
			String msgContent = "当前有科室信息未匹配！";
			for (String msg : unCompareInfo) {
				msgContent += ("\n\n"+msg);
			}
			List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UNCOMPARE_KS_INFO.getCode());
			if (!CollectionUtils.isEmpty(ddxxglDtolist)){
				for (DdxxglDto ddxxglDto : ddxxglDtolist) {
					if (StringUtil.isNotBlank(ddxxglDto.getDdid())){
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),"当前有科室信息未匹配！", msgContent);
					}
				}
			}
		}
		return true;
	}


	/**
	 * 接收外部送检信息New
	 * @param request
	 * @param jsonStr
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> receiveInspectInfoMap(HttpServletRequest request, String jsonStr) throws BusinessException {

		List<SjxxDto> sjxxDtos = JSONObject.parseArray(jsonStr, SjxxDto.class);

		Map<String,Object> map = receiveInspectInfoMap(request,sjxxDtos);

		return map;
	}

	/**
	 * 接收外部送检信息New--提供不同参数的接口，主要是为了防止 JSONObject.parseArray执行两次，提高效率
	 * @param request
	 * @param sjxxDtos
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> receiveInspectInfoMap(HttpServletRequest request, List<SjxxDto> sjxxDtos) throws BusinessException {
		Map<String,Object> map = new HashMap<>();
		String status = "success";
		String errorCode = "0";
		String errorInfo = "保存成功！";
		List<Map<String,String>> errorList = new ArrayList<>();
		if(sjxxDtos != null && sjxxDtos.size() > 0) {
			List<String> unCompareInfo = new ArrayList<>();//未匹配信息List
			Map<String, List<JcsjDto>> jclist = new HashMap<>();
			jclist.put(BasicDataTypeEnum.SD_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.DETECT_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.DETECTION_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.PATHOGENY_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.DETECTION_UNIT.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
			jclist.put(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.INSPECTION_DIVISION.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
			for (SjxxDto sjxxDto : sjxxDtos) {
				Pattern pattern = Pattern.compile("^[0-9]+$");

				//广西人民会出现连点得情况，去做一个redis锁，由于数据量和出错情况不多先注释掉
				//if (!redisUtil.setIfAbsent("redis-lock"+sjxxDto.getWbbm(), "redis-lock"+sjxxDto.getWbbm(),5L, TimeUnit.MINUTES)) {
				//	continue;
				//}
				if(StringUtil.isNotBlank(sjxxDto.getSjdwmc())&&"昆明医科大学第一附属医院".equals(sjxxDto.getSjdwmc())&&StringUtil.isNotBlank(sjxxDto.getWbbm())&&sjxxDto.getWbbm().startsWith("900")&&pattern.matcher(sjxxDto.getHzxm()).matches()){
					continue;
				}
				try {
					dealReceiveInspectInfo(sjxxDto, unCompareInfo, jclist, request);
				} catch (Exception e) {
					status = "fail";
					errorCode = "105";
					Map<String,String> errorMap = new HashMap<>();
					errorMap.put("wbbm",sjxxDto.getWbbm());
					errorMap.put("errorInfo",e.getMessage());
					log.error("接收外部送检信息保存失败！外部编码："+sjxxDto.getWbbm()+" errorInfo:"+e.getMessage());
					errorList.add(errorMap);
				}
			}
			//若有未匹配信息，则发送钉钉消息
			if (unCompareInfo.size()>0){
				String msgContent = "当前有科室信息未匹配！";
				for (String msg : unCompareInfo) {
					msgContent += ("\n\n"+msg);
				}
				List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UNCOMPARE_KS_INFO.getCode());
				if (!CollectionUtils.isEmpty(ddxxglDtolist)){
					for (DdxxglDto ddxxglDto : ddxxglDtolist) {
						if (StringUtil.isNotBlank(ddxxglDto.getDdid())){
							talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),"当前有科室信息未匹配！", msgContent);
						}
					}
				}
			}
		}
		if (errorList != null && errorList.size() > 0) {
			errorInfo = JSON.toJSONString(errorList);
		}
		map.put("status", status);
		map.put("errorCode", errorCode);
		map.put("errorInfo", errorInfo);
		return map;
	}


	/**
	 * 接收外部送检信息NewPlus
	 * @param request
	 * @param jsonStr
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Map<String,Object> receiveInspectInfoMapPlus(HttpServletRequest request, String jsonStr) throws BusinessException{
		Map<String,Object> map = new HashMap<>();
		String status = "success";
		String errorCode = "0";
		String errorInfo = "保存成功！";
		List<Map<String,String>> errorList = new ArrayList<>();
		List<SjxxDto> sjxxDtos = JSONObject.parseArray(jsonStr, SjxxDto.class);
		if(sjxxDtos != null && sjxxDtos.size() > 0) {
			List<String> unCompareInfo = new ArrayList<>();//未匹配信息List
			Map<String, List<JcsjDto>> jclist = new HashMap<>();
			jclist.put(BasicDataTypeEnum.SD_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.DETECT_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.DETECTION_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.PATHOGENY_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.DETECTION_UNIT.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
			jclist.put(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
			jclist.put(BasicDataTypeEnum.INSPECTION_DIVISION.getCode(), redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
			for (SjxxDto sjxxDto : sjxxDtos) {
				try {
					if (StringUtil.isNotBlank(sjxxDto.getDb())){
						//如果DB长度超长，则直接跳出 2025-05-13
						if(sjxxDto.getDb().length()>32){
							throw new BusinessException("msg","DB长度超长，直接跳出");
						}
						String mergeJcxm = "D,R";//需要合并的检测项目参数扩展1
						String mergeJcxmcskz3 = "IMP_REPORT_SEQ_INDEX_TEMEPLATE,IMP_REPORT_ONCO_QINDEX_TEMEPLATE";//需要合并的检测项目参数扩展3
						boolean isSame = false;//wbsjxx表中是否有与sjxxDto同样数据
						//1、查询wbsjxx表中是否存在对应数据
						WbsjxxDto wbsjxxDto = new WbsjxxDto();
						wbsjxxDto.setSjbm(sjxxDto.getWbbm());//设置实际编码
						String wbbh = StringUtil.isNotBlank(sjxxDto.getWbbm())?sjxxDto.getWbbm().length()>=10?sjxxDto.getWbbm().substring(0,10):sjxxDto.getWbbm():"";
						wbsjxxDto.setWbbm(wbbh);//设置外部编号（截取前10位）
						wbsjxxDto.setHzxm(sjxxDto.getHzxm());//设置患者姓名
						wbsjxxDto.setCyrq(sjxxDto.getCyrq());//设置采样日期
						wbsjxxDto.setZyh(sjxxDto.getZyh());//设置住院号
						wbsjxxDto.setYblx(sjxxDto.getYblx());//设置样本类型（未匹配过的）
						String jcdw = null;
						//若接收到送检信息中参数扩展1不为空，匹配迪安实验室数据，查找对应的检测单位基础数据
						if (sjxxDto.getCskz1()!=null && !"".equals(sjxxDto.getCskz1().replaceAll(" ",""))){
							SysxxDto sysxxDto = sysxxService.getSysxxDtoByDfsysmc(sjxxDto.getCskz1());
							if (sysxxDto != null){
								jcdw = sysxxDto.getWfsys();
							}
						}
						//若参数扩展1未匹配上实验室，则根据jcdw匹配基础数据的jcdwdm
						if (StringUtil.isBlank(jcdw)) {
							List<JcsjDto> jc_jcdwList = jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
							for (int j = 0; j < jc_jcdwList.size(); j++) {
								if(jc_jcdwList.get(j).getCsdm().equals(sjxxDto.getJcdw())){
									jcdw = jc_jcdwList.get(j).getCsid();
									sjxxDto.setJcdwmc(jc_jcdwList.get(j).getCsmc());
									break;
								}
								if ( "1".equals(jc_jcdwList.get(j).getSfmr()) ){
									jcdw = jc_jcdwList.get(j).getCsid();
									sjxxDto.setJcdwmc(jc_jcdwList.get(j).getCsmc());
								}
							}
						}
						JcsjDto jcxm_jcsj = compareJcxm(sjxxDto, null, jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
						wbsjxxDto.setJcxmid(jcxm_jcsj.getCsid());
						WbsjxxDto dto = wbsjxxService.getDtoById(wbsjxxDto.getSjbm());
						//若当前检测项目在需合并的检测项目中
						if ((mergeJcxm.contains(sjxxDto.getJcxm()) || mergeJcxm.contains(jcxm_jcsj.getCskz1()))){
							List<WbsjxxDto> wbsjxxDtos = wbsjxxService.getDtoList(wbsjxxDto);//时间倒序,条件为，患者姓名，住院号，样本类型，采样日期
							if (!CollectionUtils.isEmpty(wbsjxxDtos)){
								//如果存在同样标本编号的数据，优先该数据，否则采用另外的数据.2024-02-17 比如如果DNA已经接收，那么新增的RNA应该是新建一条数据
								WbsjxxDto tmp_wbsjxx = null;
								for(WbsjxxDto t_wbsjxx:wbsjxxDtos) {
									if(t_wbsjxx.getSjbm().equals(sjxxDto.getWbbm())) {
										sjxxDto.setWbbm(t_wbsjxx.getYbbh());
										//t_wbsjjcxm = t_wbsjxx.getJcxmid();
										tmp_wbsjxx = t_wbsjxx;
										break;
									}
								}
								// 根据患者姓名，住院号，样本类型，采样日期找到相同记录后，再确认项目是否一致，如果编码不一致但项目一致，则判断为需要新增一条
								// add 2024-06-06 lwj
								if(tmp_wbsjxx == null) {
									// 样本信息第一次进来
									// 若已有的最新的数据已被接受,则直接新增,若未被接收,则进行一下判断
									if(StringUtil.isBlank(wbsjxxDtos.get(0).getJsrq())) {
										boolean isMeraged = false;//是否已被合并
										//获取最新的一条数据,判断该数据是否有被合并过
										WbsjxxDto wbsjxxDto_first = wbsjxxDtos.get(0);
										//判断项目是否一致
										if(!wbsjxxDto_first.getJcxmid().equals(wbsjxxDto.getJcxmid())) {
											//若不一致,需要判断需不需要合并
											wbsjxxDtos.remove(wbsjxxDto_first);
											if(!CollectionUtils.isEmpty(wbsjxxDtos)){
												List<WbsjxxDto> collect = wbsjxxDtos.stream().filter(item -> item.getSjid().equals(wbsjxxDto_first.getSjid())).collect(Collectors.toList());
												if (!CollectionUtils.isEmpty(collect)){
													isMeraged = true;
												}
											}
											if (!isMeraged){
												wbsjxxDtos.add(wbsjxxDto_first);
												//未被合并过
												sjxxDto.setWbbm(wbsjxxDto_first.getYbbh());
												tmp_wbsjxx = wbsjxxDto_first;
											}
										}
									}
								}
								boolean hasD = false;
								boolean hasR = false;
								//若存在数据
								List<String> jcxmList = new ArrayList<>();
								//将现有的检测项目存入jcxmList中
								jcxmList.add(wbsjxxDto.getJcxmid());
								// 前面已经判断是否编码一致，以及是否项目一致，如果t_isFind为true，则判断这条数据是否之前已经传入，如果已经传入则不再新增
								// add 2024-06-06 lwj
								if(tmp_wbsjxx != null) {
									//1.1、判断List中的数据是否与sjxxDto中的项目匹配
									for (int i = wbsjxxDtos.size() - 1; i >= 0; i--) {
										if(tmp_wbsjxx.getSjid().equals(wbsjxxDtos.get(i).getSjid())) {
											if (!wbsjxxDtos.get(i).getJcxmid().equals(wbsjxxDto.getJcxmid())){
												jcxmList.add(wbsjxxDtos.get(i).getJcxmid());
											} else {
												isSame = true;
											}
										}
									}
								}
								//1.2、循环jcxmList，获取项目对应boolean
								for (String jcxm : jcxmList) {
									JcsjDto jcsjDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), jcxm);
									if ("D".equals(jcsjDto.getCskz1())){
										hasD = true;
									} else if ("R".equals(jcsjDto.getCskz1())) {
										hasR = true;
									}
								}
								//1.3、合并项目
								if (hasD && hasR){
									sjxxDto.setJcxm("C");
									//log.error("----------receiveInspectInfo---------- 检测项目合并 :C");
								}
							} else {
								sjxxDto.setWbbm(wbsjxxDto.getSjbm());//设置sjxxDto外部编号
							}
						} else {
							sjxxDto.setWbbm(wbsjxxDto.getSjbm());//设置sjxxDto外部编号
						}
						//为了区分合并项目和正常的接收项目，需要增加一个标记，但为充分利用现有标记，把信息放在 zmxm_flg(自免)里
						sjxxDto.setZmxm_flg("1");
						//2、处理送检数据
						String erroMsg = "";
						try {
							dealReceiveInspectInfo(sjxxDto, unCompareInfo, jclist, request);
						} catch (BusinessException e) {
							erroMsg = e.getMessage();
						}
						//3、wbsjxx表添加数据
						if (dto!=null){
							wbsjxxDto.setId(dto.getId());
							wbsjxxDto.setWbqtxx(sjxxDto.getQtxx());
							wbsjxxService.update(wbsjxxDto);
						}else if (!isSame && dto == null){
							wbsjxxDto.setId(StringUtil.generateUUID());
							wbsjxxDto.setSjid(sjxxDto.getSjid());
							wbsjxxDto.setWbqtxx(sjxxDto.getQtxx());
							if(StringUtil.isBlank(wbsjxxDto.getJcxmid()) && !CollectionUtils.isEmpty(sjxxDto.getJcxmids())){
								wbsjxxDto.setJcxmid(sjxxDto.getJcxmids().get(0));
							}
							wbsjxxService.insert(wbsjxxDto);
						}
						if (StringUtil.isNotBlank(erroMsg)){
							throw new BusinessException("msg",erroMsg);
						}
					} else {
						throw new BusinessException("msg","送检伙伴信息为空！");
					}
				} catch (Exception e) {
					status = "fail";
					errorCode = "105";
					Map<String,String> errorMap = new HashMap<>();
					errorMap.put("wbbm",sjxxDto.getWbbm());
					errorMap.put("errorInfo",e.getMessage());
					log.error("接收外部送检信息保存失败！外部编码："+sjxxDto.getWbbm()+" errorInfo"+e.getMessage());
					errorList.add(errorMap);
				}
			}
			//若有未匹配信息，则发送钉钉消息
			if (unCompareInfo.size()>0){
				String msgContent = "当前有科室信息未匹配！";
				for (String msg : unCompareInfo) {
					msgContent += ("\n\n"+msg);
				}
				List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UNCOMPARE_KS_INFO.getCode());
				if (!CollectionUtils.isEmpty(ddxxglDtolist)){
					for (DdxxglDto ddxxglDto : ddxxglDtolist) {
						if (StringUtil.isNotBlank(ddxxglDto.getDdid())){
							talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(),"当前有科室信息未匹配！", msgContent);
						}
					}
				}
			}
		}
		if (errorList != null && errorList.size() > 0) {
			errorInfo = JSON.toJSONString(errorList);
		}
		map.put("status", status);
		map.put("errorCode", errorCode);
		map.put("errorInfo", errorInfo);
		return map;
	}
	/**
	 * 处理外部送检信息
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	public boolean dealReceiveInspectInfo(SjxxDto sjxxDto,List<String> unCompareInfo,Map<String, List<JcsjDto>> jcList, HttpServletRequest request) throws BusinessException {
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(sjxxDto.getDb())) {
			//如果DB长度超长，则直接跳出 2025-05-13
			if(sjxxDto.getDb().length()>32){
				throw new BusinessException("msg","DB长度超长，直接跳出");
			}

			sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			//1、匹配前期检测(代码)
			JSONArray qqjclist = JSONObject.parseArray(sjxxDto.getQqjc());//前期检测，json格式为"qqjc":[{"qqjcmc":"PCT","qqjcz":"11"},{"qqjcmc":"白细胞","qqjcz":"12"}]
			if (qqjclist != null && qqjclist.size() > 0) {
				//获取到前期检测的json数组。匹配前期检测基础数据的参数名称，若未匹配上，则匹配基础数据的cskz3（别名）
				//json格式为"qqjc":[{"qqjcmc":"PCT","qqjcz":"11"},{"qqjcmc":"白细胞","qqjcz":"12"}]
				List<SjqqjcDto> sjqqjcDtos = new ArrayList<>();
				List<JcsjDto> jc_qqjcList = jcList.get(BasicDataTypeEnum.DETECTION_TYPE.getCode());
				for (int k = 0; k < qqjclist.size(); k++) {
					String qqjcmc = ((JSONObject) qqjclist.get(k)).getString("qqjcmc");//前期检测名称
					String qqjcz = ((JSONObject) qqjclist.get(k)).getString("qqjcz");//前期检测值
					if (jc_qqjcList != null && jc_qqjcList.size() > 0) {
						for (JcsjDto jcsjDto : jc_qqjcList) {
							//匹配前期检测基础数据的参数名称，若未匹配上，则匹配基础数据的cskz3（别名）
							if (StringUtil.isNotBlank(qqjcmc) && qqjcmc.equals(jcsjDto.getCsmc()) && StringUtil.isNotBlank(qqjcz)) {
								//匹配前期检测基础数据的参数名称
								SjqqjcDto sjqqjcDto = new SjqqjcDto();
								sjqqjcDto.setYjxm(jcsjDto.getCsid());
								sjqqjcDto.setJcz(qqjcz);
								sjqqjcDtos.add(sjqqjcDto);
								break;
							} else {
								//匹配基础数据的cskz3（别名），别名格式为a,b,c
								if (StringUtil.isNotBlank(jcsjDto.getCskz3())) {
									String[] cskzlist = jcsjDto.getCskz3().split(",");
									if (cskzlist.length > 0) {
										for (String mc : cskzlist) {
											if (qqjcmc.equals(mc) && StringUtil.isNotBlank(qqjcz)) {
												SjqqjcDto sjqqjcDto = new SjqqjcDto();
												sjqqjcDto.setYjxm(jcsjDto.getCsid());
												sjqqjcDto.setJcz(qqjcz);
												sjqqjcDtos.add(sjqqjcDto);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				sjxxDto.setSjqqjcs(sjqqjcDtos);
			}
			//log.error("----------receiveInspectInfo---------- 匹配前期检测 :" + JSONObject.toJSONString(sjxxDto.getSjqqjcs()));

			//2、匹配送检单位（1、sjdwmc送检单位名称；2、sjdw送检单位；3、第三方）
			boolean compareSuccess = false;
			YyxxDto yyxxDto = new YyxxDto();
			String sjdwmc = StringUtil.isNotBlank(sjxxDto.getSjdwmc()) ? sjxxDto.getSjdwmc() : sjxxDto.getSjdw();//送检单位名称，若sjdwmc为空，则取sjdw；若sjdwmc不为空，则取sjdwmc
			if (StringUtil.isNotBlank(sjdwmc)) {
				// 若送检单位名称不为空
				// 根据送检单位名称查询未删除的医院信息
				yyxxDto.setDwmc(sjdwmc);
				List<YyxxDto> yyxxList = yyxxService.queryAllByMc(yyxxDto);
				//若查到,则设置单位id为获取到的List中的第一个id
				if (yyxxList != null && yyxxList.size() > 0) {
					// 取第一个保存
					sjxxDto.setSjdw(yyxxList.get(0).getDwid());//设置单位id为获取到的List中的第一个id
					sjxxDto.setSjdwmc(yyxxList.get(0).getDwmc());//设置送检单位名称为获取到的List中的第一个名称
					compareSuccess = true;
				}
			}
			if (!compareSuccess) {
				// 若送检单位未匹配成功（未查到+未传），
				// 获取查询单位名称为“第三方”的医院，并设置单位id为其id，单位名称为”-“；
				// 若未查到，则设置单位id为“未设置其它医院(dwmc=第三方)！”，单位名称为”-“
				List<YyxxDto> yyxxDtos = yyxxService.selectOther();
				if (yyxxDtos != null && yyxxDtos.size() > 0) {
					sjxxDto.setSjdw(yyxxDtos.get(0).getDwid());
				} else {
					sjxxDto.setSjdw("未设置其它医院(dwmc=第三方)！");
				}
				sjxxDto.setSjdwmc(StringUtil.isNotBlank(sjdwmc)?sjdwmc:"-");
			}
			//log.error("----------receiveInspectInfo---------- 匹配送检单位 :【sjdw:" + sjxxDto.getSjdw() + "】——【sjdwmc:" + sjxxDto.getSjdwmc()+"】");

			//3、匹配送检区分（1、伙伴.送检区分；2、代码；3、默认送检区分）
			//先用json的DB代表字段查送检伙伴，查找到送检伙伴取伙伴表sjqf字段，查找不到送检伙伴或者json未传送检区分字段的取基础数据送检区分的默认设置
			String sjqf = sjxxDto.getSjqf();
			String sjqfdm = "";
			SjhbxxDto sjhb = new SjhbxxDto();
			sjhb.setHbmc(sjxxDto.getDb());
			sjhb = sjhbxxService.getDto(sjhb);
			if (sjhb != null && StringUtil.isNotBlank(sjhb.getSjqf())) {
				//查找到送检伙伴信息的，送检区分从伙伴信息中取
				sjqf = sjhb.getSjqf();
			} else {
				//查找不到送检伙伴的sjqf，则根据传入的sjqf匹配基础数据的参数代码，若匹配不上，取基础数据默认设置
				List<JcsjDto> jc_sjqfList = jcList.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				if (jc_sjqfList != null && jc_sjqfList.size() > 0) {
					for (int j = 0; j < jc_sjqfList.size(); j++) {
						if (jc_sjqfList.get(j).getCsdm().equals(sjxxDto.getSjqf())) {
							sjqf = jc_sjqfList.get(j).getCsid();
							sjqfdm = jc_sjqfList.get(j).getCsdm();
							break;
						}
						if ("1".equals(jc_sjqfList.get(j).getSfmr())) {
							sjqf = jc_sjqfList.get(j).getCsid();
							sjqfdm = jc_sjqfList.get(j).getCsdm();
						}
					}
				}
			}
			sjxxDto.setSjqf(sjqf);
			//log.error("----------receiveInspectInfo---------- 匹配送检区分 :" + sjqf);

			//4、匹配科室(名称)
			List<SjdwxxDto> sjdwxxList = getSjdw();
			SjdwxxDto sjdwxx = new SjdwxxDto();
			SjdwxxDto qtSjdwxx = new SjdwxxDto();
			String receiveKsmc = sjxxDto.getKs();
			boolean isCompare = false;
			for (int j = 0; j < sjdwxxList.size(); j++) {
				if (sjdwxxList.get(j).getDwmc().equals(receiveKsmc)) {
					//根据科室名称匹配
					sjdwxx = sjdwxxList.get(j);
					isCompare = true;
				} else if (StringUtil.isNotBlank(sjdwxxList.get(j).getKzcs3())) {
					//若科室名称未匹配上，则根据科室cskz3进行匹配
					String[] ppkss = sjdwxxList.get(j).getKzcs3().split(",");
					if (ppkss.length > 0) {
						for (String ppks : ppkss) {
							if (ppks.equals(receiveKsmc)) {
								sjdwxx = sjdwxxList.get(j);
								isCompare = true;
								break;
							}
						}
					}
				}
				if (isCompare) {
					//若已匹配上，结束循环
					break;
				}
				if ("00999".equals(sjdwxxList.get(j).getDwdm())) {
					//设置其它科室为单位代码为00999的科室
					qtSjdwxx = sjdwxxList.get(j);
				}
			}
			if (!isCompare) {
				//若科室未匹配上，则设置ks为其它科室
				sjdwxx = qtSjdwxx;
				if (!("-".equals(receiveKsmc) || "--".equals(receiveKsmc))) {
					//将未匹配的科室
					unCompareInfo.add((unCompareInfo.size() + 1) + "、伙伴：" + sjxxDto.getDb() + ",\n\n未匹配科室：" + receiveKsmc + ",\n\n标本:" + sjxxDto.getWbbm());
				}
			}
			//若匹配上，则设置ks为匹配上的科室id
			sjxxDto.setKs(sjdwxx.getDwid());
			if ("1".equals(sjdwxx.getKzcs())) {
				//若匹配上的科室的kzcs为1，则设置qtks为传入的科室名称
				sjxxDto.setQtks(receiveKsmc);
			} else {
				sjxxDto.setQtks(null);
			}
			//log.error("----------receiveInspectInfo---------- 匹配科室 :【ksid:" + sjxxDto.getKs() + "】——【ksmc:" + receiveKsmc + "】");

			//5、匹配关注病原(名称)
			List<JcsjDto> jc_gzbyList = jcList.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode());
			List<String> bys = sjxxDto.getBys();
			if (bys != null && bys.size() > 0) {
				for (int j = 0; j < jc_gzbyList.size(); j++) {
					for (int k = 0; k < bys.size(); k++) {
						if (bys.get(k).equals(jc_gzbyList.get(j).getCsmc())) {
							bys.set(k, jc_gzbyList.get(j).getCsid());
							break;
						}
					}
				}
			}
			sjxxDto.setBys(bys);
			//log.error("----------receiveInspectInfo---------- 匹配关注病原 :" + JSONObject.toJSONString(bys));

			//6、匹配标本类型(名称)
			List<JcsjDto> jc_yblxList = jcList.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
			JcsjDto yblxDto = new JcsjDto();
			//判断标本类型时，进行特殊处理 如果为全血或者 CFDNA血液 则设置为外周血
			if ("全血".equals(sjxxDto.getYblx()) || "血液(cfDNA)".equals(sjxxDto.getYblx()) || "血液".equals(sjxxDto.getYblx())) {
				sjxxDto.setYblx("外周血");
			}
			if (jc_yblxList != null && jc_yblxList.size() > 0) {
				for (int j = 0; j < jc_yblxList.size(); j++) {
					if (jc_yblxList.get(j).getCsmc().equals(sjxxDto.getYblx())) {
						yblxDto = jc_yblxList.get(j);
						break;
					}
					// 20240904 "其它"样本类型的csdm由XXX改为G
					if ("XXX".equals(jc_yblxList.get(j).getCsdm()) || "G".equals(jc_yblxList.get(j).getCsdm())) {
						yblxDto = jc_yblxList.get(j);
					}
				}
			}
			// 20240904 "其它"样本类型的csdm由XXX改为G
			if ("XXX".equals(yblxDto.getCsdm()) || "G".equals(yblxDto.getCsdm())) {
				Object obj = redisUtil.hget("XXDY:YBLX", sjxxDto.getYblx());
				if (obj != null){
					XxdyDto xxdy = JSON.parseObject(obj.toString(),XxdyDto.class);
					JcsjDto ybjcsjDto = redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode(),xxdy.getDyxx());
					if (ybjcsjDto != null){
						yblxDto = ybjcsjDto;
					}
				}
			}
			sjxxDto.setYblxmc(sjxxDto.getYblx());
			sjxxDto.setYblx(yblxDto.getCsid());
			sjxxDto.setYblxdm(yblxDto.getCsdm());
			//log.error("----------receiveInspectInfo---------- 匹配标本类型 :【yblx:"+ yblxDto.getCsid()+"】——【yblxmc:" + sjxxDto.getYblxmc() + "】");

			//7、匹配检测单位(1、cskz1匹配实验室信息；2、jcdw匹配基础数据代码；3、默认实验室)
			String jcdw = null;
			//若接收到送检信息中参数扩展1不为空，匹配迪安实验室数据，查找对应的检测单位基础数据
			if (sjxxDto.getCskz1()!=null && !"".equals(sjxxDto.getCskz1().replaceAll(" ",""))){
				SysxxDto sysxxDto = sysxxService.getSysxxDtoByDfsysmc(sjxxDto.getCskz1());
				if (sysxxDto != null){
					jcdw = sysxxDto.getWfsys();
					sjxxDto.setJcdw(sysxxDto.getWfsys());
					sjxxDto.setJcdwmc(sysxxDto.getWfsysmc());
				}
			}
			//若参数扩展1未匹配上实验室，则根据jcdw匹配基础数据的jcdwdm
			if (StringUtil.isBlank(jcdw)) {
				List<JcsjDto> jc_jcdwList = jcList.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
				for (int j = 0; j < jc_jcdwList.size(); j++) {
					if(jc_jcdwList.get(j).getCsdm().equals(sjxxDto.getJcdw())){
						jcdw = jc_jcdwList.get(j).getCsid();
						sjxxDto.setJcdwmc(jc_jcdwList.get(j).getCsmc());
						break;
					}
					if ( "1".equals(jc_jcdwList.get(j).getSfmr()) ){
						jcdw = jc_jcdwList.get(j).getCsid();
						sjxxDto.setJcdwmc(jc_jcdwList.get(j).getCsmc());
					}
				}
				sjxxDto.setJcdw(jcdw);
			}
			//log.error("----------receiveInspectInfo---------- 匹配检测单位 :"+ sjxxDto.getJcdw());

			//8、匹配检测项目(代码)
			JcsjDto jcxm_jcsj = compareJcxm(sjxxDto, yblxDto, jcList.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
			if (jcxm_jcsj != null){
				if (StringUtil.isNotBlank(jcxm_jcsj.getFcsid())){
					List<SjjcxmDto> sjjcxms = new ArrayList<>();
					SjjcxmDto sjjcxmDto = new SjjcxmDto();
					sjjcxmDto.setJcxmid(jcxm_jcsj.getFcsid());
					sjjcxmDto.setJczxmid(jcxm_jcsj.getCsid());
					sjjcxms.add(sjjcxmDto);
					sjxxDto.setSjjcxms(sjjcxms);
					sjxxDto.setJcxm(JSON.toJSONString(sjjcxms));
				} else {
					List<String> jcxmids = new ArrayList<>();
					jcxmids.add(jcxm_jcsj.getCsid());
					sjxxDto.setJcxmids(jcxmids);
				}
				sjxxDto.setJcxmmc(jcxm_jcsj.getCsmc());
			}
			//log.error("----------receiveInspectInfo---------- 匹配检测项目 :"+ JSONObject.toJSONString(sjxxDto.getJcxmids()));

			//9、匹配快递类型(代码)
			List<JcsjDto> jc_kdlxList = jcList.get(BasicDataTypeEnum.SD_TYPE.getCode());
			for (int j = 0; j < jc_kdlxList.size(); j++) {
				if(jc_kdlxList.get(j).getCsdm().equals(sjxxDto.getKdlx())){
					sjxxDto.setKdlx(jc_kdlxList.get(j).getCsid());
					break;
				}
			}
			//log.error("----------receiveInspectInfo---------- 匹配快递类型 :"+ sjxxDto.getKdlx());


			//10、匹配是否收费，没匹配到默认为是
			if("否".equals(sjxxDto.getSfsf())) {
				sjxxDto.setSfsf("0");
			}else if("免D".equals(sjxxDto.getSfsf())) {
				sjxxDto.setSfsf("2");
			}else if("免R".equals(sjxxDto.getSfsf())) {
				sjxxDto.setSfsf("3");
			}else {
				sjxxDto.setSfsf("1");
			}
			//log.error("----------receiveInspectInfo---------- 匹配是否收费 :"+ sjxxDto.getSfsf());

			//11、匹配应付金额，若收费且金额为空时，获取默认金额
			if (!"0".equals(sjxxDto.getSfsf()) && StringUtil.isBlank(sjxxDto.getFkje())) {
				SjhbxxDto sjhbxxDto = new SjhbxxDto();
				if (sjxxDto.getJcxmids() != null && sjxxDto.getJcxmids().size() > 0){
					List<SjjcxmDto> sjjcxms_t = new ArrayList<>();
					for (String jcxmid : sjxxDto.getJcxmids()) {
						SjjcxmDto sjjcxmDto = new SjjcxmDto();
						sjjcxmDto.setJcxmid(jcxmid);
						sjjcxms_t.add(sjjcxmDto);
					}
					sjxxDto.setJcxm(JSON.toJSONString(sjjcxms_t));
					sjhbxxDto.setSjxms(sjjcxms_t);
				}else {
					List<SjjcxmDto> sjjcxms_t = sjxxDto.getSjjcxms();
					sjxxDto.setJcxm(JSON.toJSONString(sjjcxms_t));
					sjhbxxDto.setSjxms(sjjcxms_t);
				}
				sjhbxxDto.setHbmc(sjxxDto.getDb());
				SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
				// 判断若为免D或免R时减半
				if (re_sjhbxxDto != null && StringUtil.isNotBlank(re_sjhbxxDto.getSfbz())){
					sjxxDto.setFkje(re_sjhbxxDto.getSfbz());
				}
			}

			//12、匹配年龄单位（特殊处理“月”）
			if (StringUtil.isNotBlank(sjxxDto.getNldw())){
				if ("月".equals(sjxxDto.getNldw())){
					sjxxDto.setNldw("个月");
				} else if (!"个月,年,天,无,岁".contains(sjxxDto.getNldw())) {
					sjxxDto.setNldw("无");
				}
			}
			//保存数据，默认为80状态
			sjxxDto.setYbbh(sjxxDto.getWbbm());
			/*
			 *	1.获取数据后要优先判断 标本编号是否存在，如果不存在则返回错误
			 * 	2.如果存在才去后台确认标本编号是否已使用。
			 * 	3.如果已经使用则返回错误信息
			 */
			if(StringUtil.isNotBlank(sjxxDto.getYbbh())){
//				if (StringUtil.isNotBlank(sjxxDto.getWbbm())){
//					List<SjxxDto> dtosByWbbm = dao.getDtosByWbbm(sjxxDto);
//					if (!CollectionUtils.isEmpty(dtosByWbbm)){
////						throw new BusinessException("msg","该内部编码已存在！");
//						return true;
//					}
//				}
				if (StringUtil.isNotBlank(sjxxDto.getQtxx())){
					Map<String,Object> qtxxMap = JSONObject.parseObject(sjxxDto.getQtxx(), new TypeReference<>() {});
					if (qtxxMap!= null && qtxxMap.containsKey("YBBH") && qtxxMap.get("YBBH") != null){
						sjxxDto.setYbbh(qtxxMap.get("YBBH").toString());
					}
				}
				//样本编号不为空时，根据样本编号查数据库，查到则更新，未查到则插入
				//根据样本编号查数据，查出多条SQL只取第一条
				//当改数据存在时看是否接收，未接受则更新，接收则报编号不能重复使用错误
				List<SjxxDto> sjxxs = dao.queryByYbbh(sjxxDto.getYbbh());
				SjxxDto sjxx = new SjxxDto();
				boolean flag = false;
				if(!CollectionUtils.isEmpty(sjxxs)){
					for (SjxxDto sjxxDtoT:sjxxs){
						if(StringUtil.isNotBlank(sjxxDtoT.getDb()) && sjxxDtoT.getDb().equals(sjxxDto.getDb())){
							sjxx = sjxxDtoT;
						}
					}
					if(StringUtil.isBlank(sjxx.getSjid())){
						flag = true;
					}
				}
				if (StringUtil.isNotBlank(sjxx.getSjid())){
					if (StringUtil.isNotBlank(sjxxDto.getQtxx())){
						SjkzxxDto sjkzxxDto = new SjkzxxDto();
						sjkzxxDto.setSjid(sjxx.getSjid());
						sjkzxxDto.setQtxx(sjxxDto.getQtxx());
						sjkzxxDto.setXgry(sjxxDto.getXgry());
						boolean result = sjkzxxService.update(sjkzxxDto);
						if (!result){
							sjkzxxDto.setLrry(sjxxDto.getLrry());
							sjkzxxService.insertDto(sjkzxxDto);
						}
					}
					String ybbh = sjxx.getYbbh();
					if(StringUtil.isNotBlank(ybbh) && !ybbh.equals(sjxx.getWbbm())){
						int lastDashIndex = ybbh.lastIndexOf('-');
						if(lastDashIndex!= -1){
							String lastPart = ybbh.substring(lastDashIndex + 1);
							if(lastPart.length() == 8){
								ybbh = ybbh.substring(0,lastDashIndex);
							}
						}
					}
					if (StringUtil.isNotBlank(sjxx.getYbbh()) && StringUtil.isNotBlank(sjxx.getWbbm()) && StringUtil.isNotBlank(ybbh) && !ybbh.equals(sjxx.getWbbm()) && StringUtil.isBlank(sjxx.getJsrq())) {
						//针对北大一需要合并的标本，样本编码和外部编码会不一样，当标本还没有接收的情况下，需要根据第二次的标本信息对第一次的内部进行更新
						sjxxDto.setYbbh(null);
						update(sjxxDto);
						sjxxDto.setYbbh(sjxxDto.getWbbm());
						//针对的是标本已经接收，但还未发送报告的情况下，如果接口这边再次发送信息过来，则只更新相应的字段
					}else if((StringUtil.isNotBlank(sjxx.getYbbh()) && StringUtil.isNotBlank(sjxx.getJsrq()) && StringUtil.isBlank(sjxx.getBgrq()))
					//主要是针对艾迪康这边微信端录入后，再次从接口过来，应该要以接口为准进行更新.防止销售人员在本系统录入的单位和艾迪康的系统不一样，以艾迪康系统为准。可能已经接收，也可能没有接收
					||( StringUtil.isNotBlank(sjxx.getYbbh()) && StringUtil.isBlank(sjxx.getWbbm())  && StringUtil.isBlank(sjxx.getBgrq()))){
						logger.error("进入有接收日期原始信息：-----------------"+JSONObject.toJSONString(sjxx));
						SjxxDto updateSjxx = new SjxxDto();
						updateSjxx.setSjid(sjxx.getSjid());
						updateSjxx.setXgry(sjxxDto.getWbbm());
						updateSjxx.setWbbm(sjxxDto.getWbbm());
						updateSjxx.setHzxm(sjxxDto.getHzxm());
						updateSjxx.setXb(sjxxDto.getXb());
						updateSjxx.setNl(sjxxDto.getNl());
						updateSjxx.setNldw(sjxxDto.getNldw());
						updateSjxx.setKs(sjxxDto.getKs());
						updateSjxx.setQtks(sjxxDto.getQtks());
						//updateSjxx.setYblx(sjxxDto.getYblx());
						//updateSjxx.setYblxmc(sjxxDto.getYblxmc());
						updateSjxx.setSjdw(sjxxDto.getSjdw());
						updateSjxx.setSjdwmc(sjxxDto.getSjdwmc());
						updateSjxx.setLczz(sjxxDto.getLczz());
						dao.updateByid(updateSjxx);
					}else {
						//北大一标本合并第二次接收另一个项目标本信息进入。或者不是北大一这种需要合并项目的标本也进入这个地方的处理
						List<SjxxDto> sjxxDtoList = new ArrayList<>();
						sjxxDtoList.add(sjxx);
						receiveSampleDoSomething(sjxxDtoList,sjxxDto,sjqfdm);
					}
				}else{
					if(flag){
						LocalDate currentDate = LocalDate.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
						String formattedDate = currentDate.format(formatter);
						sjxxDto.setYbbh(sjxxDto.getYbbh()+"-"+formattedDate);
					}
					sjxxDto.setLrry(sjxxDto.getWbbm());
					sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
					boolean result = insertDto(sjxxDto);
					if (!result)
						throw new BusinessException("msg","本地保存失败！");
					boolean result_gl = insertAll(sjxxDto,sjxxDto.getLrry());//关联表信息更新（先删除后插入，故新增和更新都可公用）
					if (!result_gl)
						throw new BusinessException("msg","关联表保存失败！");
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADD.getCode() + JSONObject.toJSONString(sjxxDto));

					//送检实验管理表操作
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
					List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto,null, DetectionTypeEnum.DETECT_SJ.getCode());

					if (!CollectionUtils.isEmpty(insertInfo)){
						//更新项目实验数据和送检实验数据
						boolean isSuccess = addOrUpdateSyData(insertInfo,sjxxDto,sjxxDto.getLrry());

						if (isSuccess){
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
					if (StringUtil.isNotBlank(sjxxDto.getQtxx())){
						SjkzxxDto sjkzxxDto = new SjkzxxDto();
						sjkzxxDto.setSjid(sjxxDto.getSjid());
						sjkzxxDto.setQtxx(sjxxDto.getQtxx());
						sjkzxxDto.setXgry(sjxxDto.getXgry());
						result = sjkzxxService.update(sjkzxxDto);
						if (!result){
							sjkzxxDto.setLrry(sjxxDto.getLrry());
							sjkzxxService.insertDto(sjkzxxDto);
						}
					}
				}
			}else {
				throw new BusinessException("msg","标本编号为空");
			}
			List<FjcfbDto> fjcfbDtos = new ArrayList<>();
			DBEncrypt dbEncrypt = new DBEncrypt();
			//判断是否有文件
			if(request instanceof MultipartHttpServletRequest) {
				MultipartFile t_file = ((MultipartHttpServletRequest) request).getFile("file");
				if(t_file != null){
					//送检附件
					List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
					MultipartFile[] imp_file = new MultipartFile[files.size()];
					files.toArray(imp_file);
					List<String> wjms = sjxxDto.getWjms();
					if(wjms != null && wjms.size() > 0){
						for (int j = 0; j < wjms.size(); j++) {
							if(imp_file!=null&& imp_file.length>0){
								for (int k = 0; k < imp_file.length; k++) {
									if(!imp_file[k].isEmpty()){
										MultipartFile file = imp_file[k];
										String wjm = file.getOriginalFilename();
										if(StringUtil.isNotBlank(wjm) && wjm.equals(wjms.get(j))){
											// 根据日期创建文件夹
											String fwjlj = prefix + releaseFilePath + sjxxDto.getYwlx() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
													+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
											int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
											String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
											String fwjm = t_name + wjm.substring(index);
											String wjlj = fwjlj + "/" + fwjm;
											mkDirs(fwjlj);
											FjcfbDto fjcfbDto = new FjcfbDto();
											fjcfbDto.setFjid(StringUtil.generateUUID());
											fjcfbDto.setYwid(sjxxDto.getSjid());
											fjcfbDto.setZywid("");
											fjcfbDto.setXh("1");
											fjcfbDto.setYwlx(sjxxDto.getYwlx());
											fjcfbDto.setWjm(wjm);
											fjcfbDto.setWjlj(dbEncrypt.eCode(wjlj));
											fjcfbDto.setFwjlj(dbEncrypt.eCode(fwjlj));
											fjcfbDto.setFwjm(dbEncrypt.eCode(fwjm));
											fjcfbDto.setZhbj("0");
											boolean result = fjcfbService.insert(fjcfbDto);
											if (!result)
												throw new BusinessException("msg","附件保存失败！");
											byte[] buffer = new byte[4096];
											FileOutputStream fos = null;
											BufferedOutputStream output = null;
											InputStream fis = null;
											BufferedInputStream input = null;
											try {
												fis = file.getInputStream();
												input = new BufferedInputStream(fis);
												mkDirs(fwjlj);
												fos = new FileOutputStream(wjlj);
												output = new BufferedOutputStream(fos);
												int n;
												while ((n = input.read(buffer, 0, 4096)) > -1) {
													output.write(buffer, 0, n);
												}
											} catch (Exception e) {
												e.printStackTrace();
												return false;
											} finally {
												closeStream(new Closeable[] { fis, input, output, fos });
											}
											fjcfbDtos.add(fjcfbDto);
										}
									}
								}
							}
						}
					}
					// 附件排序
					FjcfbDto fjcfbDto = new FjcfbDto();
					fjcfbDto.setYwid(sjxxDto.getSjid());
					fjcfbDto.setYwlx(sjxxDto.getYwlx());
					boolean result_f = fjcfbService.fileSort(fjcfbDto);
					if (!result_f)
						log.error("附件排序失败！");
					if(fjcfbDtos.size() > 0){
						// 拷贝文件到微信服务器
						for (int j = 0; j < fjcfbDtos.size(); j++) {
							FjcfbModel fjcfbModel = fjcfbDtos.get(j);
							if (fjcfbModel != null) {
								String wjlj = fjcfbModel.getWjlj();
								String pathString = dbEncrypt.dCode(wjlj);
								File file = new File(pathString);
								// 文件不存在不做任何操作
								if (file.exists()){
									byte[] bytesArray = new byte[(int) file.length()];
									FileInputStream fis;
									try {
										fis = new FileInputStream(file);
										fis.read(bytesArray); // read file into bytes[]
										fis.close();
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										log.equals("未找到文件："+e.toString());
										throw new BusinessException("msg","未找到文件!");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										log.equals("读写文件异常："+e.toString());
										throw new BusinessException("msg","读写文件异常！");
									}
									// 需要给文件的名称
									ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray) {
										@Override
										public String getFilename() {
											return file.getName();
										}
									};
									MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
									paramMap.add("file", contentsAsResource);
									paramMap.add("fjcfbModel", fjcfbModel);
									// 发送文件到服务器
									RestTemplate t_restTemplate = new RestTemplate();
									String reString = t_restTemplate.postForObject(menuurl + "/wechat/upSaveInspReport", paramMap, String.class);
									if ("OK".equals(reString)) {
										// 更新文件的转换标记为true
										boolean result_zhbj = fjcfbService.updateZhbj(fjcfbModel);
										if (!result_zhbj)
											throw new BusinessException("msg","转换标记更新失败！");
									}
								}
							}
						}
					}
				}
			}
			// 发送钉钉消息
			String url = menuurl + "/common/view/displayView?view_url=/common/view/inspectionView?ybbh=" + sjxxDto.getYbbh() + "&hzxm=" + sjxxDto.getHzxm();
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm("INSPECTION_TYPE");
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
			if (ddxxglDtos != null && ddxxglDtos.size() > 0) {
				String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
				String ICOMM_SJ00001 = xxglService.getMsg("ICOMM_SJ00001");
				for (int j = 0; j < ddxxglDtos.size(); j++) {
					if (StringUtil.isNotBlank(ddxxglDtos.get(j).getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDtos.get(j).getYhm(), ddxxglDtos.get(j).getDdid(), ICOMM_SJ00002, StringUtil.replaceMsg(ICOMM_SJ00001,
								sjxxDto.getYbbh(),sjxxDto.getDb(), sjxxDto.getSjdwmc(), sjxxDto.getYblxmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), url);
					}
				}
			}
		}
		else {
			throw new BusinessException("msg","送检伙伴信息为空！");
		}
		return true;
	}

	/**
	 * 接收样本 数据库操作 封装
	 * @param sjxxs
	 * @param sjxxDto
	 * @param sjqfdm
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void receiveSampleDoSomething(List<SjxxDto> sjxxs,SjxxDto sjxxDto,String sjqfdm) throws BusinessException {
		//2024-02-17 需要解决北大一这边 先接收D，然后再加R的情况，接收日期已存在可能无法增加，同时也得考虑正常情况一旦接收后就不能更改项目的
		if (StringUtil.isBlank(sjxxs.get(0).getJsrq()) || isMegerProject(sjxxs, sjxxDto)){
			sjxxDto.setSjid(sjxxs.get(0).getSjid());
			sjxxDto.setXgry(sjxxDto.getWbbm());
			//这里先设置为null，再set是因为毓璜顶时，会先调用毓璜顶接口，然后艾迪康会调用这个接口 ，两个样本编号不一样，导致医院的样本编号被更新为艾迪康的编号
			sjxxDto.setYbbh(null);
			boolean isupdate = update(sjxxDto);
			sjxxDto.setYbbh(sjxxDto.getWbbm());
			if (!isupdate){ throw new BusinessException("msg","本地更新失败"); }
			boolean result_gl = insertAll(sjxxDto,sjxxDto.getXgry());//关联表信息更新（先删除后插入，故新增和更新都可公用）
			if (!result_gl)
				throw new BusinessException("msg","关联表保存失败！");
			SjtssqDto sjtssqDto=new SjtssqDto();
			sjtssqDto.setSjid(sjxxs.get(0).getSjid());
			sjtssqDto.setZt("80");
			List<SjtssqDto> sjtssqDtos = sjtssqDao.getDtoList(sjtssqDto);
			SjjcxmDto sjjcxmDto=new SjjcxmDto();
			sjjcxmDto.setSjid(sjxxs.get(0).getSjid());
			List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getDtoList(sjjcxmDto);
			sjjcxmService.readjustPayment(sjxxDto,sjjcxmDtos,sjtssqDtos);//公共方法，调整标本信息的付款金额以及检测项目应付金额
			sjjcxmService.updateSjjcxmDtos(sjjcxmDtos);//更新检测项目应付金额
			dao.updateTssq(sjxxDto);//更新标本信息的付款金额
			List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
			if (jcxmlist!=null&&!CollectionUtils.isEmpty(jcxmlist) &&!CollectionUtils.isEmpty(sjjcxmDtos)){
				for (SjjcxmDto dto : jcxmlist) {
					sjjcxmDtos.stream().forEach( item -> {
						if (dto.getXmglid().equals(item.getXmglid())){
							item.setBs(dto.getBs());
						}
					});
				}
			}
			sjxxDto.setJcxm(JSONObject.toJSONString(sjjcxmDtos));
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(sjxxDto));

			//送检实验管理表操作
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
			List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto,null, DetectionTypeEnum.DETECT_SJ.getCode());

			if (!CollectionUtils.isEmpty(insertInfo)){
				//更新项目实验数据和送检实验数据
				boolean flag = addOrUpdateSyData(insertInfo,sjxxDto,StringUtil.isNotBlank(sjxxDto.getXgry())?sjxxDto.getXgry():sjxxDto.getLrry());

				if(flag) {
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
		}else{
			throw new BusinessException("msg","标本编号已经存在,不能重复使用！");
		}
	}

	/**
	 * 
	 * @param sjxxs
	 * @param sjxxDto
	 * @return
	 * @throws Exception
	 */
	private boolean isMegerProject(List<SjxxDto> sjxxs,SjxxDto sjxxDto) throws BusinessException{
		//2024-02-17 需要解决北大一这边 先接收D，然后再加R的情况，接收日期已存在可能无法增加，同时也得考虑正常情况一旦接收后就不能更改项目的
		//sjxxs:数据库数据   sjxxDto：当前数据  Zmxm_flg：自免标记，临时用来区分是否需要合并项目
		if(StringUtil.isNotBlank(sjxxDto.getZmxm_flg())) {
			List<String> t_jcxmids =  sjxxDto.getJcxmids();
			if(t_jcxmids==null || t_jcxmids.isEmpty())
				throw new BusinessException("msg","标本编号已经存在,不能重复使用！-1");
			boolean isFind = false;
			for(int i=0;i<sjxxs.size();i++) {
				if(sjxxs.get(i).getJcxmid().equals(t_jcxmids.get(0))) {
					isFind = true;
					break;
				}
			}
			if(isFind)
				throw new BusinessException("msg","标本编号已经存在,不能重复使用！-2");
			//项目编号不一样，而且接收日期为当天，则进行合并
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String nowDate=sdf.format(new Date());
			if(nowDate.equals(sjxxs.get(0).getJsrq().substring(0, 10))) {
				return true;
			}else {
				throw new BusinessException("msg","标本编号已经存在,不能重复使用！-3");
			}
		}else {
			throw new BusinessException("msg","标本编号已经存在,不能重复使用！-4");
		}
	}
	
	/**
	 * 通过内部编码获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getSjxxDto(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		SjxxDto sjxxDto_t=getDto(sjxxDto);
		if(sjxxDto_t == null)
		{
			return sjxxDto;
		}
		if("1".equals(sjxxDto_t.getYyxxCskz1())) {
			sjxxDto_t.setHospitalname(sjxxDto_t.getHospitalname()+"--"+sjxxDto_t.getSjdwmc());
		}
		String sign = commonService.getSign(sjxxDto.getNbbm());
		int fontflg;
		if (StringUtils.isNotBlank(sjxxDto.getNbbm())&&sjxxDto.getNbbm().length() >= 10){
			fontflg = 2;
		} else{
			fontflg = 1;
		}
		/*try{
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String word=sjxxDto_t.getYbbh();
		String url;
		if(StringUtil.isNotBlank(word)) {
			url = ":8081/Print?code=" + sjxxDto.getNbbm() + "&sign=" + sign + "&num="+sjxxDto.getPrint_num()+"&fontflg="+fontflg+"&word="+word;
		}else {
			url = ":8081/Print?code=" + sjxxDto.getNbbm() + "&sign=" + sign + "&num="+sjxxDto.getPrint_num()+"&fontflg="+fontflg;
		}
		sjxxDto_t.setPrint_demise_ip(url);
		log.error("打印地址：" +url);
		//new PrinterThread(url).start();
		return sjxxDto_t;
	}

	/**
	 * 整体送检结果信息
	 * @param str
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean receiveEntiretyResult(String str) {
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(str)) {
			WeChatDetailedReportModel details = JSONObject.parseObject(str, WeChatDetailedReportModel.class);
			// 查询是否有此标本信息
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setYbbh(details.getYbbh());
			SjxxDto t_sjxxDto = dao.getDto(sjxxDto);
			// 默认检测类型为DNA
			if (StringUtil.isBlank(details.getDetection_type())) {
				details.setDetection_type("D");
			}
			String yhid = details.getUserid();
			if (t_sjxxDto != null) {
				//根据员工号查询用户ID
				if(StringUtil.isNotBlank(yhid)){
					List<DdyhDto> ddyhDtos = ddyhService.getUserByYhm(yhid);
					if(ddyhDtos != null && ddyhDtos.size() == 1){
						yhid = ddyhDtos.get(0).getYhid();
					}
				}
				Map<String, Map<String, WeChatDetailedResultModel>> map = details.getResult();
				List<YhsjxxjgDto> yhsjxxjgDtos = new ArrayList<>();
				int i = 0;
				for (Entry<String, Map<String, WeChatDetailedResultModel>> entrys : map.entrySet()) {
					for (Entry<String, WeChatDetailedResultModel> entry : entrys.getValue().entrySet()) {
						YhsjxxjgDto yhsjxxjgDto = new YhsjxxjgDto();
						yhsjxxjgDto.setXxjgid(StringUtil.generateUUID());
						yhsjxxjgDto.setXh(String.valueOf(i));
						yhsjxxjgDto.setSjid(t_sjxxDto.getSjid());
						yhsjxxjgDto.setJdid(entry.getKey());
						yhsjxxjgDto.setFjdid(entry.getValue().getParent());
						yhsjxxjgDto.setWzywm(entry.getValue().getName());
						yhsjxxjgDto.setWzzwm(entry.getValue().getCn_name());
						yhsjxxjgDto.setWzfl(entrys.getKey());
						yhsjxxjgDto.setFldj(entry.getValue().getRank());
						yhsjxxjgDto.setDqs(entry.getValue().getReads_count());
						yhsjxxjgDto.setZdqs(entry.getValue().getReads_accum());
						yhsjxxjgDto.setDwds(entry.getValue().getRpm());
						yhsjxxjgDto.setDsbfb(entry.getValue().getRatio());
						yhsjxxjgDto.setJyzfgd(entry.getValue().getCoverage());
						yhsjxxjgDto.setSm(entry.getValue().getGenus_name());
						yhsjxxjgDto.setSdds(entry.getValue().getGenus_reads_count());
						yhsjxxjgDto.setXpjcl(entry.getValue().getFrequency());
						yhsjxxjgDto.setGzd(entry.getValue().getInterest());
						yhsjxxjgDto.setXdfd(entry.getValue().getAbundance());
						yhsjxxjgDto.setJclx(details.getDetection_type());
						yhsjxxjgDto.setWzfllx(entry.getValue().getVirus_type());
						yhsjxxjgDto.setYhid(yhid);
						yhsjxxjgDtos.add(yhsjxxjgDto);
						i++;
					}
				}
				if (yhsjxxjgDtos != null && yhsjxxjgDtos.size() > 0) {
					// 通过送检ID、用户ID和检测类型删除信息，所以只需要取一条数据即可
					yhsjxxjgService.deleteByYhsjxxjgDto(yhsjxxjgDtos.get(0));
					return yhsjxxjgService.insertByYhjxxjgDtos(yhsjxxjgDtos);
				} else {
					log.error(" receiveEntiretyResult 没有数据：" + str);
				}
			} else {
				// 不存在此标本，将数据加入错误信息表
				CwglDto cwglDto = new CwglDto();
				cwglDto.setCwlx(ErrorTypeEnum.USERINSP_DETAILED_TYPE.getCode());
				if (str.length() > 4000) {
					str = str.substring(0, 4000);
				}
				cwglDto.setYsnr(str);
				cwglService.insertDto(cwglDto);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * 根据录入人员获取送检表的合作伙伴
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<String> getDbsByLrrys(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getDbsByLrrys(sjxxDto);
	}

	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJsjcdwByjsid(String jsid)
	{
		// TODO Auto-generated method stub
		return dao.getJsjcdwByjsid(jsid);
	}

	private boolean uploadReport(File fileChildDir, SjxxDto sjxxDto, String lsbcdz) throws BusinessException{
		log.error("准备上传报告：" + fileChildDir.getName());
		sjxxDto.setFjid(StringUtil.generateUUID());
		//文件名
		String wjm = fileChildDir.getName();
		int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
		String suffix = wjm.substring(index);
		//获取标本编号文件查询
		String ybbh = wjm.substring(wjm.lastIndexOf("_")+1, wjm.lastIndexOf("."));
		log.error("标本编号：" + ybbh);
		//根据标本编号查询送检信息
		SjxxDto t_sjxxDto = dao.getDtoByYbbh(ybbh);
		if(t_sjxxDto == null)
			return false;

		//获取文件类型
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(t_sjxxDto.getSjid());
		fjcfbDto.setScry(sjxxDto.getLrry());
		if(".pdf".equalsIgnoreCase(suffix)){
			fjcfbDto.setYwlx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx());
		}else{
			fjcfbDto.setYwlx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx() + "_WORD");
		}
		// 删除原有文件
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (fjcfbDtos != null && fjcfbDtos.size() > 0){
			for (int i = 0; i < fjcfbDtos.size(); i++){
				fjcfbService.delFile(fjcfbDtos.get(i));
			}
		}
		// 文件复制到正式文件夹，插入信息至正式表
		//根据日期创建文件夹
		String real_path = prefix + releaseFilePath + fjcfbDto.getYwlx() +"/"+ "UP"+
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		mkDirs(real_path);
		String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
		String saveName = t_name + suffix;

		String newPath = real_path + "/" + saveName;
		String path = lsbcdz + "/" + wjm;

		//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
		CutFile(path, newPath, true);
		//将正式文件夹路径更新至数据库
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto t_fjcfbDto = new FjcfbDto();
		t_fjcfbDto.setFjid(sjxxDto.getFjid());
		t_fjcfbDto.setYwid(t_sjxxDto.getSjid());
		t_fjcfbDto.setZywid("");
		t_fjcfbDto.setXh("1");
		t_fjcfbDto.setYwlx(fjcfbDto.getYwlx());
		t_fjcfbDto.setWjm(wjm);
		t_fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+saveName));
		t_fjcfbDto.setFwjlj(bpe.eCode(real_path));
		t_fjcfbDto.setFwjm(bpe.eCode(saveName));
		t_fjcfbDto.setZhbj("0");
		fjcfbDao.insert(t_fjcfbDto);

		// 附件排序
		boolean isSuccess = fjcfbService.fileSort(fjcfbDto);
		if (!isSuccess)
			return false;
		log.error("拷贝文件：" + sjxxDto.getFjid());
		// 拷贝文件到微信服务器
		FjcfbModel t_fjcfbModel = new FjcfbModel();
		t_fjcfbModel.setFjid(sjxxDto.getFjid());
		FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);
		isSuccess = sendFileToAli(fjcfbModel);
		if (!isSuccess)
			return false;
		//判断是否发送报告
		if(StringUtil.isNotBlank(sjxxDto.getSffs()) && "1".equals(sjxxDto.getSffs())) {
			// 更新本地送检信息，检验结果不知道
			SjxxDto msg_sjxxDto = new SjxxDto();
			msg_sjxxDto.setSjid(t_sjxxDto.getSjid());
			msg_sjxxDto.setXgry(sjxxDto.getLrry());
			Date date = new Date();
			msg_sjxxDto.setBgrq(date.toString());
			msg_sjxxDto.setSfsc("1");
			int count = dao.updateReport(msg_sjxxDto);
			if (count == 0)
				return false;
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(msg_sjxxDto));
			msg_sjxxDto.setPdflx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx());
			msg_sjxxDto.setWordlx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx() + "_WORD");
			log.error("发送消息：" + msg_sjxxDto.getSjid());
			isSuccess = sendReportMessage(msg_sjxxDto);
			return isSuccess;
		}
		return true;
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 送检单位转义
	 * @param
	 * @return
	 */
	public String escapeSjdw(SjxxDto sjxxDto){
		return dao.escapeSjdw(sjxxDto);
	}
	/**
	 * 其它
	 * @return
	 */
	public String getQt(){
		return dao.getQt();
	}
	/**
	 * 其它
	 * @param
	 * @return
	 */
	public String getYyxxQt(){
		return dao.getYyxxQt();
	}

	/**
	 * sjxx导入
	 * @param sjxxDto
	 * @return
	 */
	public boolean insertSjxx(SjxxDto sjxxDto){
		return dao.insertSjxx(sjxxDto);
	}
	/**
	 * 科室转义
	 * @param sjxxDto
	 * @return
	 */
	public String escapeKs(SjxxDto sjxxDto){
		return dao.escapeKs(sjxxDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex,StringBuffer errorMessages) throws BusinessException{
		// TODO Auto-generated method stub
		SjxxDto sjxxDto=(SjxxDto)baseModel;
		int count= getCountByybbh(sjxxDto);
		if(count!=0){
			log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() + "】存在重复数据，请尝试重新导入！");
			errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() + "】存在重复数据，请尝试重新导入！<br/>");
			return false;
			//throw new BusinessException("msg", "标本编号有重复数据，请尝试重新导入！"+sjxxDto.getYbbh());
		}else{
			sjxxDto.setSjid(StringUtil.generateUUID());
			List<SjjcxmDto> list = new ArrayList<>();
			if(StringUtil.isNotBlank(sjxxDto.getSjdwmc())){
				// 针对黄石中心医院的对接，当送检单位名称存在，sjdw字段也存在的时候，不需要重新检索数据库进行对照
				if (StringUtil.isBlank(sjxxDto.getSjdw())) {
					String t_sjdwmc = escapeSjdw(sjxxDto);
					if (StringUtil.isNotBlank(t_sjdwmc)) {
						sjxxDto.setSjdw(t_sjdwmc);
						sjxxDto.setSjdwmc(sjxxDto.getSjdwmc());
					} else {
						sjxxDto.setSjdw(getYyxxQt());
						//sjxxDto.setSjdwmc(sjxxDto.getSjdwmc());
					}
				}
			}

			if(StringUtil.isNotBlank(sjxxDto.getKs())){
				String ks = escapeKs(sjxxDto);
				if(ks!=null){
					sjxxDto.setQtks(sjxxDto.getKs());
					sjxxDto.setKs(ks);
				}else{
					sjxxDto.setQtks(sjxxDto.getKs());
					sjxxDto.setKs(getQt());
				}
			}
			if(StringUtil.isNotBlank(sjxxDto.getJcdw())){
				List<JcsjDto> jcdwList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT.getCode());
				boolean isFind = false;
				for (JcsjDto jcsjDto : jcdwList) {
					if (sjxxDto.getJcdw().equals(jcsjDto.getCsmc()) ){
						sjxxDto.setJcdw(jcsjDto.getCsid());
						sjxxDto.setJcdwmc(jcsjDto.getCsmc());
						isFind = true;
						break;
					}
				}
				if(!isFind){
					log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的检测单位【" + sjxxDto.getJcdw() + "】不存在！");
					errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的检测单位【" + sjxxDto.getJcdw() + "】不存在！<br/>");
					//throw new BusinessException("msg", "此检测单位不存在，保存失败！");
				}
			}

			if(StringUtil.isNotBlank(sjxxDto.getYblxmc())){
				List<JcsjDto> yblxList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
				JcsjDto qt_jcsjDto = null;
				for (JcsjDto jcsjDto : yblxList) {
					if (sjxxDto.getYblxmc().equals(jcsjDto.getCsmc()) ){
						sjxxDto.setYblx(jcsjDto.getCsid());
						sjxxDto.setYblxdm(jcsjDto.getCsdm());
						break;
					}else if ("其他".equals(jcsjDto.getCsmc())){
						qt_jcsjDto = jcsjDto;
					}
				}
				if(StringUtil.isBlank(sjxxDto.getYblx()) && qt_jcsjDto!= null){
					sjxxDto.setYblx(qt_jcsjDto.getCsid());
					sjxxDto.setYblxdm(qt_jcsjDto.getCsdm());
					if("全血".equals(sjxxDto.getYblxmc())||"血浆".equals(sjxxDto.getYblxmc())){
						sjxxDto.setYblxdm("B");
					}
				}
			}
			//送检区分放在最前面，按照送检区分来设置是否收费
			if(StringUtil.isNotBlank(sjxxDto.getSjqf())){
				List<JcsjDto> sjqfList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				boolean isFind = false;
				for (JcsjDto jcsjDto : sjqfList) {
					if (sjxxDto.getSjqf().equals(jcsjDto.getCsmc())){
						sjxxDto.setSjqf(jcsjDto.getCsid());
						if (StringUtil.isNotBlank(jcsjDto.getCskz2())){
							sjxxDto.setSfsf(jcsjDto.getCskz2());
						}
						isFind = true;
						break;
					}
				}
				if(!isFind){
					errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的送检区分【"+sjxxDto.getSjqf() +  "】未匹配系统信息，请尝试重新导入！<br/>");
					log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的送检区分【"+sjxxDto.getSjqf() +  "】未匹配系统信息，请尝试重新导入！<br/>");
				}
			}
			//这里的是否收费判断，移到了上面jcxm判断部分之前，因为项目表格插入时需要有是否免费的信息
			if ("否".equals(sjxxDto.getSfsf())||"0".equals(sjxxDto.getSfsf())){
				sjxxDto.setSfsf("0");
			}else if ("是".equals(sjxxDto.getSfsf())||StringUtil.isBlank(sjxxDto.getSfsf())||"1".equals(sjxxDto.getSfsf())){
				sjxxDto.setSfsf("1");
			}else if ("免D".equals(sjxxDto.getSfsf())||"2".equals(sjxxDto.getSfsf())){
				sjxxDto.setSfsf("2");
			}else if ("免R".equals(sjxxDto.getSfsf())||"3".equals(sjxxDto.getSfsf())){
				sjxxDto.setSfsf("3");
			}else{
				errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的是否免费【"+sjxxDto.getSfsf() +  "】只允许输入【否，是，免D，免R】，请尝试重新导入！<br/>");
				log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的是否免费【"+sjxxDto.getSfsf() +  "】只允许输入【否，是，免D，免R】，请尝试重新导入！<br/>");
			}
			//检测项目匹配
			if(StringUtil.isNotBlank(sjxxDto.getJcxm())){
				// 先按照项目名称查询基础数据，确认是否匹配
				List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode());
				for (JcsjDto jcsjDto : jcxmList) {
					if (sjxxDto.getJcxm().equals(jcsjDto.getCsmc()) ){
						sjxxDto.setJcxmid(jcsjDto.getCsid());
						break;
					}
				}
				// 若匹配不到，则按照 伙伴和 项目名称查询信息对应表，按照类型为外部接口检测项目进行匹配
				if(StringUtil.isBlank(sjxxDto.getJcxmid())){
					XxdyDto xxdyDto_pj = new XxdyDto();
					xxdyDto_pj.setDylxcsdm("JCXMPP");
					xxdyDto_pj.setYxx(sjxxDto.getDb() + "-" + sjxxDto.getJcxm());
					List<XxdyDto> xmdylist = xxdyService.getDtoList(xxdyDto_pj);
					if (xmdylist!=null && !xmdylist.isEmpty()) {
						sjxxDto.setJcxmid(xmdylist.getFirst().getDyxxid());
					}
				}
				if(StringUtil.isNotBlank(sjxxDto.getJcxmid())){
					SjjcxmDto sjjcxmDto = new SjjcxmDto();
					sjjcxmDto.setSjid(sjxxDto.getSjid());
					sjjcxmDto.setXh("1");
					sjjcxmDto.setJcxmid(sjxxDto.getJcxmid());
					sjjcxmDto.setXmglid(StringUtil.generateUUID());
					sjjcxmDto.setBs("1");//用于同步到85表示新增，先删除后新增
					sjjcxmDto.setSfsf(sjxxDto.getSfsf());
					if (StringUtil.isNotBlank(sjxxDto.getDb())){
						// 若收费且金额为空时，获取默认金额
						if ("1".equals(sjxxDto.getSfsf()) && StringUtil.isBlank(sjxxDto.getFkje()))
						{
							HbsfbzDto hbsfbzDto=new HbsfbzDto();
							hbsfbzDto.setHbmc(sjxxDto.getDb());
							List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
							if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0){
								for(HbsfbzDto dto:hbsfbzDtos){
									if(sjjcxmDto.getJcxmid().equals(dto.getXm())){
										sjjcxmDto.setYfje(dto.getSfbz());
										sjxxDto.setFkje(dto.getSfbz());
										break;
									}
								}
							}
						}
					}
					list.add(sjjcxmDto);
					sjxxDto.setJcxmmc(sjxxDto.getJcxm());
				}else{
					errorMessages.append("第" + rowindex +"行标本编号为【"+sjxxDto.getYbbh() +  "】的检测项目【"+sjxxDto.getJcxm() +  "】未匹配系统信息，请尝试重新导入！<br/>");
					log.error("第" + rowindex +"行标本编号为【"+sjxxDto.getYbbh() +  "】的检测项目【"+sjxxDto.getJcxm() +  "】未匹配系统信息，请尝试重新导入！<br/>");
				}
			}
			//检测子项目匹配
			if(StringUtil.isNotBlank(sjxxDto.getJczxm())){
				List<JcsjDto> jczxmList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
				boolean isFind = false;
				for (JcsjDto jcsjDto : jczxmList) {
					if (sjxxDto.getJczxm().equals(jcsjDto.getCsmc())){
						sjxxDto.setJczxm(jcsjDto.getCsid());

						isFind = true;
						break;
					}
				}
				if(!isFind){
					errorMessages.append("第" + rowindex +"行标本编号为【"+sjxxDto.getYbbh() +  "】的检测子项目【"+sjxxDto.getJczxm() +  "】未匹配系统信息，请尝试重新导入！<br/>");
					log.error("第" + rowindex +"行标本编号为【"+sjxxDto.getYbbh() +  "】的检测子项目【"+sjxxDto.getJczxm() +  "】未匹配系统信息，请尝试重新导入！<br/>");
				}
			}
			if(StringUtil.isNotBlank(sjxxDto.getKdlx())){
				List<JcsjDto> jczxmList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode());
				boolean isFind = false;
				for (JcsjDto jcsjDto : jczxmList) {
					if (sjxxDto.getKdlx().equals(jcsjDto.getCsmc())){
						sjxxDto.setKdlx(jcsjDto.getCsid());
						break;
					}
				}
				if(!isFind){
					sjxxDto.setKdlx(null);
				}
			}
			//是否开票
			if(StringUtil.isNotBlank(sjxxDto.getKpsq())){
				List<JcsjDto> kpsqList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode());
				boolean isFind = false;
				for (JcsjDto jcsjDto : kpsqList) {
					if (sjxxDto.getKpsq().equals(jcsjDto.getCsmc())){
						sjxxDto.setKpsq(jcsjDto.getCsid());
						break;
					}
				}
				if(!isFind){
					sjxxDto.setKpsq(null);
				}
			}
			if(StringUtil.isNotBlank(sjxxDto.getXb())){
				if(sjxxDto.getXb().equals("男")){
					sjxxDto.setXb("1");
				}else if(sjxxDto.getXb().equals("女")){
					sjxxDto.setXb("2");
				}else if(sjxxDto.getXb().equals("未知")){
					sjxxDto.setXb("3");
				}
			}
			if(StringUtil.isNotBlank(sjxxDto.getSfjs())){
				if(sjxxDto.getSfjs().equals("是")){
					sjxxDto.setSfjs("1");
					if(StringUtil.isBlank(sjxxDto.getNbbm()) || StringUtil.isBlank(sjxxDto.getJsrq())){
						log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的是否接收设置为是时，需要填写内部编码以及接收日期，请尝试重新导入！");
						errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的是否接收设置为是时，需要填写内部编码以及接收日期，请尝试重新导入！<br/>");
						//throw new BusinessException("msg", "当是否接收设置为是时，需要填写内部编码以及接收日期，请尝试重新导入！");
					}
				}else if(sjxxDto.getSfjs().equals("否")){
					sjxxDto.setSfjs("0");
				}else{
					sjxxDto.setSfjs("0");
				}
			}
			if(sjxxDto.getJyjg()!=null){
				if(sjxxDto.getJyjg().equals("阴性")){
					sjxxDto.setJyjg("0");
				}else if(sjxxDto.getJyjg().equals("阳性")){
					sjxxDto.setJyjg("1");
				}else if(sjxxDto.getJyjg().equals("疑似")){
					sjxxDto.setJyjg("2");
				}
			}
			if(sjxxDto.getSfsc()!=null){
				if(sjxxDto.getSfsc().equals("是")){
					sjxxDto.setSfsc("1");
				}else{
					sjxxDto.setSfsc("0");
				}
			}
			if(sjxxDto.getJcbj()!=null){
				if(sjxxDto.getJcbj().equals("是")){
					sjxxDto.setJcbj("1");
				}else{
					sjxxDto.setJcbj("0");
				}
			}
			if(sjxxDto.getDjcbj()!=null){
				if(sjxxDto.getDjcbj().equals("是")){
					sjxxDto.setDjcbj("1");
				}else{
					sjxxDto.setDjcbj("0");
				}
			}
			if(sjxxDto.getFkbj()!=null){
				if(sjxxDto.getFkbj().equals("已付款")){
					sjxxDto.setFkbj("1");
				}else if(sjxxDto.getFkbj().equals("未付款")){
					sjxxDto.setFkbj("0");
				}else if(sjxxDto.getFkbj().equals("部分付款")){
					sjxxDto.setFkbj("2");
				}else
					sjxxDto.setFkbj("0");
			}
			if(sjxxDto.getSfzmjc()!=null){
				if(sjxxDto.getSfzmjc().equals("是")){
					sjxxDto.setSfzmjc("1");
				}else{
					sjxxDto.setSfzmjc("0");
				}
			}
			if(sjxxDto.getQtjcbj()!=null){
				if(sjxxDto.getQtjcbj().equals("是")){
					sjxxDto.setQtjcbj("1");
				}{
					sjxxDto.setQtjcbj("0");
				}
			}
			if(sjxxDto.getSftj()!=null){
				if(sjxxDto.getSftj().equals("是")){
					sjxxDto.setSftj("1");
				}else if(sjxxDto.getSftj().equals("否")){
					sjxxDto.setSftj("0");
				}else
					sjxxDto.setSftj("0");
			}
			sjxxDto.setLrry(user.getYhid());
			sjxxDto.setScry(user.getYhid());//用于同步到85删除sjjcxm的删除人员

			//确认采样日期格式
			if(StringUtil.isNotBlank(sjxxDto.getCyrq()) && sjxxDto.getCyrq().length()>=10 && !DateUtils.isValidDate(sjxxDto.getCyrq().substring(0,10),"yyyy-MM-dd")){
				errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的采样日期【" + sjxxDto.getCyrq() +"】格式不正确，需要采用YYYY-MM-DD的格式！<br/>");
				log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的采样日期【" + sjxxDto.getCyrq() +"】格式不正确，需要采用YYYY-MM-DD的格式！<br/>");
			}
			//确认接收日期格式
			if(StringUtil.isNotBlank(sjxxDto.getJsrq()) && sjxxDto.getJsrq().length()>=10  && !DateUtils.isValidDate(sjxxDto.getJsrq().substring(0,10),"yyyy-MM-dd")){
				errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的接收日期【" + sjxxDto.getJsrq() +"】格式不正确，需要采用YYYY-MM-DD的格式！<br/>");
				log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的接收日期【" + sjxxDto.getJsrq() +"】格式不正确，需要采用YYYY-MM-DD的格式！<br/>");
			}
			//确认报告日期格式
			if(StringUtil.isNotBlank(sjxxDto.getBgrq()) && sjxxDto.getBgrq().length()>=10 && !DateUtils.isValidDate(sjxxDto.getBgrq().substring(0,10),"yyyy-MM-dd")){
				errorMessages.append("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的报告日期【" + sjxxDto.getBgrq() +"】格式不正确，需要采用YYYY-MM-DD的格式！<br/>");
				log.error("第" + rowindex +"行标本编号【"+sjxxDto.getYbbh() +  "】的报告日期【" + sjxxDto.getBgrq() +"】格式不正确，需要采用YYYY-MM-DD的格式！<br/>");
			}

			//如果有任何错误信息，则此条记录不插入数据库
			if(StringUtil.isNotBlank(errorMessages.toString())) {
				return false;
			}

			insertSjxx(sjxxDto);
			if (!CollectionUtils.isEmpty(list)){
				sjjcxmService.insertSjjcxmDtos(list);
				sjxxDto.setJcxm(JSON.toJSONString(list));
			}

			//送检扩展信息处理
			SjkzxxDto sjkzxxDto = new SjkzxxDto();
			sjkzxxDto.setQtxx(sjxxDto.getQtxx());
			sjkzxxDto.setXgry(user.getYhid());
			sjkzxxDto.setSjid(sjxxDto.getSjid());
			boolean result = sjkzxxService.update(sjkzxxDto);
			if (!result){
				sjkzxxDto.setLrry(user.getYhid());
				sjkzxxService.insertDto(sjkzxxDto);
			}

			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_ADD.getCode() + JSONObject.toJSONString(sjxxDto));
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
			List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(sjxxDto,null,DetectionTypeEnum.DETECT_SJ.getCode());

			if (!CollectionUtils.isEmpty(insertInfo)){
				//更新项目实验数据和送检实验数据
				boolean isSuccess = addOrUpdateSyData(insertInfo,sjxxDto,user.getYhid());
				if (isSuccess){
					SjsyglDto sjsyglDto_t=new SjsyglDto();
					sjsyglDto_t.setSjid(sjxxDto.getSjid());
					//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
					List<SjsyglModel> sjsylist = sjsyglService.getModelList(sjsyglDto_t);
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(sjsylist));
					XmsyglDto xmsyglDto_t = new XmsyglDto();
					xmsyglDto_t.setYwid(sjxxDto.getSjid());
					List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto_t);
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
				}
			}
		}
		return true;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, String>> getListByYbbhs(List<String> ybbhs, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return dao.getListByYbbhs(ybbhs);
	}

	@Override
	public boolean saveImportReport(BaseModel baseModel, User user, StringBuffer errorMessage)
			throws BusinessException {
		// TODO Auto-generated method stub
		SjxxDto sjxxDto = (SjxxDto)baseModel;
		log.error("saveImportReport " + baseModel + " getLsbcdz: "+ sjxxDto.getLsbcdz());
		//获取文件
		File file;
		String fileSuffix = sjxxDto.getLsbcdz().substring((sjxxDto.getLsbcdz().lastIndexOf(".") > 0) ? sjxxDto.getLsbcdz().lastIndexOf(".") : sjxxDto.getLsbcdz().length() - 1);
		if(".zip".equals(fileSuffix)){
			file = new File(sjxxDto.getLsbcdz().substring(0,sjxxDto.getLsbcdz().lastIndexOf(".")));
		}else{
			file = new File(sjxxDto.getLsbcdz());
		}
		//上传
		if(file.isDirectory()){
			String lsbcdz = sjxxDto.getLsbcdz().substring(0,sjxxDto.getLsbcdz().lastIndexOf("."));
			//文件夹，循环取标本编号文件查询
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File fileChildDir : files) {
					log.error("fileChildDir信息："+ fileChildDir.getName() + " 判断是否为文件：" + fileChildDir.isFile());
					if(fileChildDir.isFile()){
						boolean result = uploadReport(fileChildDir, sjxxDto, lsbcdz);
						if(!result) {
							if(StringUtil.isNotBlank(errorMessage)) {
								errorMessage.append(" 、"+fileChildDir.getName());
							}else {
								errorMessage.append("以下文件导入失败："+fileChildDir.getName());
							}
						}
					}else if(fileChildDir.isDirectory()){
						File[] tmp_files = fileChildDir.listFiles();
						lsbcdz = lsbcdz + "/" + fileChildDir.getName();
						for (File tp_fileChildDir : tmp_files) {
							if (tp_fileChildDir.isFile()) {
								boolean result = uploadReport(tp_fileChildDir, sjxxDto, lsbcdz);
								if(!result) {
									if(StringUtil.isNotBlank(errorMessage)) {
										errorMessage.append(" 、"+fileChildDir.getName());
									}else {
										errorMessage.append("以下文件导入失败："+fileChildDir.getName());
									}
								}
							}
						}
					}
				}
			}
		}
		//上传单个文件
		else if(file.isFile()){
			Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+sjxxDto.getFjid());
			//如果文件信息不存在，则返回错误
			if(mFile == null || mFile.isEmpty())
				return false;
			//文件名
			String wjm = (String)mFile.get("wjm");
			int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
			String suffix = wjm.substring(index);
			//获取标本编号文件查询
			String ybbh = wjm.substring(wjm.lastIndexOf("_")+1, wjm.lastIndexOf("."));

			//根据标本编号查询送检信息
			SjxxDto t_sjxxDto = dao.getDtoByYbbh(ybbh);
			if(t_sjxxDto == null)
				return false;

			//获取文件类型
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(t_sjxxDto.getSjid());
			fjcfbDto.setScry(sjxxDto.getLrry());
			if(".pdf".equalsIgnoreCase(suffix)){
				fjcfbDto.setYwlx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx());
			}else{
				fjcfbDto.setYwlx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx() + "_WORD");
			}

			// 删除原有文件
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			if (fjcfbDtos != null && fjcfbDtos.size() > 0){
				for (int i = 0; i < fjcfbDtos.size(); i++){
					fjcfbService.delFile(fjcfbDtos.get(i));
				}
			}
			// 文件复制到正式文件夹，插入信息至正式表
			//分文件名
			String fwjm = (String)mFile.get("fwjm");
			//文件全路径
			String wjlj = (String)mFile.get("wjlj");
			//根据日期创建文件夹
			String real_path = prefix + releaseFilePath + fjcfbDto.getYwlx()+"/"+ "UP"+
					DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			mkDirs(real_path);

			//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
			CutFile(wjlj,real_path+"/"+fwjm,true);
			//将正式文件夹路径更新至数据库
			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto t_fjcfbDto = new FjcfbDto();
			t_fjcfbDto.setFjid(sjxxDto.getFjid());
			t_fjcfbDto.setYwid(t_sjxxDto.getSjid());
			t_fjcfbDto.setZywid("");
			t_fjcfbDto.setXh("1");
			t_fjcfbDto.setYwlx(fjcfbDto.getYwlx());
			t_fjcfbDto.setWjm(wjm);
			t_fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
			t_fjcfbDto.setFwjlj(bpe.eCode(real_path));
			t_fjcfbDto.setFwjm(bpe.eCode(fwjm));
			t_fjcfbDto.setZhbj("0");
			fjcfbDao.insert(t_fjcfbDto);

			// 附件排序
			boolean isSuccess = fjcfbService.fileSort(fjcfbDto);
			if (!isSuccess)
				return false;
			// 拷贝文件到微信服务器
			FjcfbModel t_fjcfbModel = new FjcfbModel();
			t_fjcfbModel.setFjid(sjxxDto.getFjid());
			FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);
			isSuccess = sendFileToAli(fjcfbModel);
			if (!isSuccess)
				return false;
			if(StringUtil.isNotBlank(sjxxDto.getSffs()) && "1".equals(sjxxDto.getSffs())) {
				// 更新本地送检信息，检验结果不知道
				SjxxDto msg_sjxxDto = new SjxxDto();
				msg_sjxxDto.setSjid(t_sjxxDto.getSjid());
				msg_sjxxDto.setXgry(sjxxDto.getLrry());
				Date date = new Date();
				msg_sjxxDto.setBgrq(date.toString());
				msg_sjxxDto.setSfsc("1");
				int count = dao.updateReport(msg_sjxxDto);
				if (count == 0)
					return false;
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(msg_sjxxDto));
				msg_sjxxDto.setWordlx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx() + "_WORD");
				msg_sjxxDto.setPdflx(t_sjxxDto.getCskz3() + "_" + t_sjxxDto.getJclx());
				isSuccess = sendReportMessage(msg_sjxxDto);
				return isSuccess;
			}
		}
		return true;
	}

	/**
	 * 从原路径剪切到目标路径
	 * @param s_srcFile
	 * @param s_distFile
	 * @param coverFlag
	 * @return
	 */
	private boolean CutFile(String s_srcFile,String s_distFile,boolean coverFlag) {
		boolean flag = false;
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile))
			return flag;

		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists())
			return flag;
		//目标文件已经存在
		if(distFile.exists()) {
			if(coverFlag) {
				srcFile.renameTo(distFile);
				flag = true;
			}
		}else {
			srcFile.renameTo(distFile);
			flag = true;
		}
		return flag;
	}

	/**
	 * 送检结果分析列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getAnalysisList(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getAnalysisList(sjxxDto);
	}

	/**
	 * 查询根据日期查询实验的记录数据
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> selectInspSize(SjxxDto sjxxDto){
		return dao.selectInspSize(sjxxDto);
	}

	/**
	 * 外部接口获取送检报告信息
	 * @param request
	 * @param sjxxDto
	 * @param code
	 * @return
	 */
	@Override
	public Map<String, Object> externalGetReport(HttpServletRequest request, SjxxDto sjxxDto, String code) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		//查询合作伙伴
		List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getSjhbByCode(code);
		if(wbhbyzDtos == null || wbhbyzDtos.size() == 0){
			map.put("status", "fail");
			map.put("errorCode", "未查询到伙伴权限！");
			return map;
		}
		List<String> dbs = new ArrayList<>();
		for (int i = 0; i < wbhbyzDtos.size(); i++) {
			dbs.add(wbhbyzDtos.get(i).getHbmc());
		}
		sjxxDto.setDbs(dbs);
		//查询送检数据信息
		List<SjxxDto> sjxxDtos;
		if(StringUtil.isNotBlank(sjxxDto.getWbbm())){
			//通过wbbm获取
			sjxxDtos = dao.getListByWbbm(sjxxDto);
		}else {
			//通过lastwbbm获取
			sjxxDtos = dao.getListByLastWbbm(sjxxDto);
		}
		if(sjxxDtos == null || sjxxDtos.size() == 0){
			map.put("status", "fail");
			map.put("errorCode", "未获取到标本信息！");
			return map;
		}
		List<Map<String, String>> urlList = new ArrayList<>();
		for (int i = 0; i < sjxxDtos.size(); i++) {
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDtos.get(i).getSjid());
			List<String> ywlxs = new ArrayList<>();
			String jclx = sjxxDtos.get(i).getJclx();
			if(StringUtil.isBlank(sjxxDto.getLx()) || "pdf".equals(sjxxDto.getLx())){
				ywlxs.add(sjxxDtos.get(i).getCskz3() + "_" + jclx);
			}else{
				ywlxs.add(sjxxDtos.get(i).getCskz3() + "_" + jclx + "_WORD");
			}
			fjcfbDto.setYwlxs(ywlxs);
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			if(fjcfbDtos!= null && fjcfbDtos.size() > 0){
				DBEncrypt dbEncrypt = new DBEncrypt();
				for (int j = 0; j < fjcfbDtos.size(); j++) {
					String wjlj = fjcfbDtos.get(j).getWjlj();
					String pathString = dbEncrypt.dCode(wjlj);
					File file = new File(pathString);
					// 文件不存在不做任何操作
					if (!file.exists()){
						log.error("未找到文件：" + pathString);
						break;
					}
					Map<String, String> downloadmap = new HashMap<>();
					downloadmap.put("url", applicationurl + "/ws/file/limitDownload");
					downloadmap.put("fjid", fjcfbDtos.get(j).getFjid());
					downloadmap.put("sign", commonService.getSign());
					downloadmap.put("fileName", fjcfbDtos.get(j).getWjm());
					urlList.add(downloadmap);
				}
			}
		}
		map.put("downloadPath", urlList);
		if(urlList.size() > 0){
			map.put("status", "success");
		}else{
			map.put("status", "fail");
			map.put("errorCode", "查询完成，未查询到相关报告！");
		}
		return map;
	}


	/**
	 * 根据wbbm获取送检报告
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListByWbbm(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getListByWbbm(sjxxDto);
	}

	/**
	 * 查找是否存在样本编号不同,外部编码相同的样本
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getDtosByWbbm(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getDtosByWbbm(sjxxDto);
	}

	/**
	 * 根据lastwbbm获取送检报告
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getListByLastWbbm(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getListByLastWbbm(sjxxDto);
	}

	/**
	 * 根据业务ID和业务类型查询下载文件
	 * @param sjxxDtos
	 * @return
	 */
	@Override
	public List<SjxxDto> selectDownloadReportBySjxxDtos(List<SjxxDto> sjxxDtos) {
		// TODO Auto-generated method stub
		return dao.selectDownloadReportBySjxxDtos(sjxxDtos);
	}

	/**
	 * 根据外部编码查询伙伴信息
	 * @param wbbm
	 * @return
	 */
	@Override
	public List<String> getSjhbByCode(String wbbm) {
		// TODO Auto-generated method stub
		List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getSjhbByCode(wbbm);
		if(wbhbyzDtos == null || wbhbyzDtos.size() == 0)
			return null;
		List<String> dbs = new ArrayList<>();
		for (int i = 0; i < wbhbyzDtos.size(); i++) {
			dbs.add(wbhbyzDtos.get(i).getHbmc());
		}
		return dbs;
	}

	/**
	 * 外部发送消息通知
	 * @param str
	 * @return
	 */
	@Override
	public boolean sendExternalMessage(String str) throws BusinessException {
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(str)) {
			ExternalMessageModel messageModel = JSONObject.parseObject(str, ExternalMessageModel.class);
			List<String> ids = messageModel.getIds();
			if(ids == null || ids.size() == 0)
				throw new BusinessException("msg", "未获取到ids信息！");
			String title = messageModel.getTitle();
			if(StringUtil.isBlank(title))
				throw new BusinessException("msg", "未获取到title信息！");
			String message = messageModel.getMessage();
			if(StringUtil.isBlank(message))
				throw new BusinessException("msg", "未获取到message信息！");
			String type = messageModel.getType();
			if(StringUtil.isBlank(type))
				throw new BusinessException("msg", "未获取到type信息！");
			//根据ids查询ddid
			List<User> userList = commonService.getUserListByYhms(ids);
			if(userList != null && userList.size() > 0){
				// 发送钉钉消息
				// 组装地址按钮
				List<BtnJsonList> btnJsonLists = new ArrayList<>();
				if("card".equals(type)){
					BtnJsonList btnJsonList = new BtnJsonList();
					String inaddress = messageModel.getInaddress();
					if(StringUtil.isNotBlank(inaddress)){
						btnJsonList.setTitle("内网访问");
						btnJsonList.setActionUrl(inaddress);
						btnJsonLists.add(btnJsonList);
					}
					btnJsonList = new BtnJsonList();
					String exaddress = messageModel.getExaddress();
					if(StringUtil.isNotBlank(exaddress)){
						btnJsonList.setTitle("外网访问");
						btnJsonList.setActionUrl(exaddress);
						btnJsonLists.add(btnJsonList);
					}
				}
				for (int i = 0; i < userList.size(); i++) {
					if(StringUtil.isNotBlank(userList.get(i).getDdid())){
						if("card".equals(type)){
							talkUtil.sendCardMessage(userList.get(i).getYhm(), userList.get(i).getDdid(), title, message, btnJsonLists, "1");
						}else{
							talkUtil.sendWorkMessage(userList.get(i).getYhm(), userList.get(i).getDdid(), title, message);
						}
					}
				}
				return true;
			}else{
				log.error("未查询到相关用户信息："+ids.toString());
				throw new BusinessException("msg", "未查询到相关用户信息！");
			}
		}
		return false;
	}

	/**
	 * 外部发送消息通知
	 * @param
	 * @return
	 */
	@Override
	public boolean sendExternalMessageNew(ExternalMessageModel externalMessageModel) throws BusinessException {
		// TODO Auto-generated method stub
		List<String> ids = externalMessageModel.getIds();
		if(ids == null || ids.size() == 0)
			throw new BusinessException("msg", "未获取到ids信息！");
		String title = externalMessageModel.getTitle();
		if(StringUtil.isBlank(title))
			throw new BusinessException("msg", "未获取到title信息！");
		String message = externalMessageModel.getMessage();
		if(StringUtil.isBlank(message))
			throw new BusinessException("msg", "未获取到message信息！");
		//根据ids查询ddid
		List<User> userList = commonService.getUserListByYhms(ids);
		if(userList != null && userList.size() > 0){
			// 发送钉钉消息
			for (int i = 0; i < userList.size(); i++) {
				if(StringUtil.isNotBlank(userList.get(i).getDdid())){
					talkUtil.sendWorkMessage(userList.get(i).getYhm(), userList.get(i).getDdid(), title, message);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 删除送检信息
	 * @param sjxxDto
	 */
	@Override
	public void delCheckApply(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(sjxxDto.getSjid())){
			dao.delete(sjxxDto);
		}
	}

	/**
	 * PDF获取数据
	 * @param
	 */
//	private String PDFStringVales(char[] chars) {
//		Integer num = 0;
//		String str = "";
//		for (char var:chars) {
//			if (!"`".equals(var+"")){
//				num = 1;
//				str+=var;
//			}
//			if (num == 1 && "`".equals(var+"")){
//				break;
//			}
//		}
//		log.error("name:"+str);
//		return str;
//	}

	private void xjjyyjsXpert(String[] strings,SjxxDto sjxxDto) {
//		String s1 = strings[11].split("）")[0] + ")";
		List<SjzmjgDto> zmjgList= new ArrayList<>();
		SjzmjgDto sjzmjgDto=new SjzmjgDto();
		for (int i = 12; i < strings.length; i++) {
			if (strings[i].contains("详细探针检测信息如下：")){
				break;
			}
			String[] split = strings[i].replaceAll(" ", "`").split("`");
			//添加list
			sjzmjgDto.setLx("XPERT");
			sjzmjgDto.setJcff(split[1]);
			sjzmjgDto.setZmid(StringUtil.generateUUID());
			sjzmjgDto.setSjid(sjxxDto.getSjid());
			sjzmjgDto.setLrry(sjxxDto.getXgry());
			sjzmjgDto.setXm(split[0]);
			sjzmjgDto.setJg(split[3]);
			sjzmjgDto.setCkz("阴性");
			zmjgList.add(sjzmjgDto);
			sjzmjgDto=new SjzmjgDto();
		}
		int start = 0;
		int end = 0;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].contains("探针信息说明：")){
				start = i;
			}
			if (strings[i].contains("修订版》")){
				end = i;
			}
		}
		String text = "";
		for (int i = start; i <= end; i++) {
			if (i==end){
				text += "			";
			}
			text += strings[i];
		}
		System.out.println(text);
//		sjxxDto.setJsjjy(text);
		sjxxDto.setLx("XPERT");
		zmAndXpertSJ(zmjgList,sjxxDto);
	}


	private void yndayxjysXpert(String[] strings,SjxxDto sjxxDto) {
//		String s1 = strings[11].split("）")[0] + ")";
		List<SjzmjgDto> zmjgList= new ArrayList<>();
		SjzmjgDto sjzmjgDto=new SjzmjgDto();
		for (int i = 7; i < strings.length; i++) {
			if (strings[i].contains("建议与解释：")){
				break;
			}
			String[] split = strings[i].replaceAll(" ", "`").split("`");
			//添加list
			sjzmjgDto.setLx("XPERT");
			sjzmjgDto.setJcff(split[3]);//检测方法
			sjzmjgDto.setZmid(StringUtil.generateUUID());
			sjzmjgDto.setSjid(sjxxDto.getSjid());
			sjzmjgDto.setLrry(sjxxDto.getXgry());
			sjzmjgDto.setXm(split[0]);//项目
			sjzmjgDto.setJg(split[1]);//
			sjzmjgDto.setCkz(split[2]);
			zmjgList.add(sjzmjgDto);
			sjzmjgDto=new SjzmjgDto();
		}
		int start = 0;
		int end = 0;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].contains("建议与解释：")){
				start = i;
			}
			if (strings[i].contains("备注：此")){
				end = i-1;
			}
		}
		String text = "";
		for (int i = start; i <= end; i++) {
			text += strings[i];
		}
		System.out.println(text);
//		sjxxDto.setJsjjy(text);
		sjxxDto.setLx("XPERT");
		zmAndXpertSJ(zmjgList,sjxxDto);
	}

	/**
	 * 自免以及Xpert报告数据处理
	 * @param
	 */
	private void zmAndXpertSJ(List<SjzmjgDto> zmjgList,SjxxDto sjxxDto) {
		//新增自免结果之前先删除之前的自免结果
		SjzmjgDto sjzyjgDto_d=new SjzmjgDto();
		sjzyjgDto_d.setSjid(sjxxDto.getSjid());
		sjzyjgDto_d.setScry(sjxxDto.getXgry());
		sjzyjgDto_d.setLx(sjxxDto.getLx());
		sjzmjgService.deleteSjzmjg(sjzyjgDto_d);
		//新增自免结果信息
		if(zmjgList!=null && zmjgList.size()>0){
			boolean result=sjzmjgService.insertSjzmjg(zmjgList);
			SjzmjgDto sjzmjgDto2=new SjzmjgDto();
			sjzmjgDto2.setSjid(sjxxDto.getSjid());
			sjzmjgDto2.setLx(sjxxDto.getLx());
			List<SjzmjgDto> paramList=sjzmjgService.getDtoList(sjzmjgDto2);
//			if (StringUtil.isNotBlank(sjxxDto.getJsjjy())){
//				paramList.get(0).setJsjjy(sjxxDto.getJsjjy());
//			}
			if(result) {
				// 同步自免结果结果至8085
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MOD_INSPECTION_SELFRESULT.getCode(), JSONObject.toJSONString(zmjgList));
				replaceSelfReport(sjxxDto,paramList);
			}
		}
	}

	/**
	 * 提取自免报告
	 * @param filePath
	 */
	private boolean extractSelfFree(String filePath,SjxxDto sjxxDto) {
  		FileInputStream fileInputStream = null;
		HWPFDocument document = null;
		OutputStream output = null;
		File file=new File(filePath);
		if (StringUtil.isBlank(filePath) || !file.exists()) {
			log.error("模板不存在！请重新确认模板！");
			return false;
		}
		if (StringUtil.isNotBlank(sjxxDto.getLx())){
			try {
			//POIFileUtil poiFileUtil = new POIFileUtil();
				String pdf2String = POIFileUtil.pdf2String(file);
//				String replaceAll = pdf2String.replaceAll(" ", "`").replaceAll("号:","").replaceAll(":","").replaceAll("时间:","").replaceAll("日期:","");
				log.error("pdf2String:"+pdf2String);
				String[] strings = pdf2String.split("\r\n");
				if (strings[0].contains("新疆金域医学检验所")){
					xjjyyjsXpert(strings,sjxxDto);
				}else if (strings[0].contains("云南迪安医学检验所检测报告单")){
					yndayxjysXpert(strings,sjxxDto);
				}
//				if ("GM".equals(sjxxDto.getLx())){
//
//				}else if("GXM".equals(sjxxDto.getLx())){
//
//				}


			} catch (IOException e) {
				e.printStackTrace();
			}

		}else{
			try {
				fileInputStream= new FileInputStream(file);
				POIFSFileSystem pfs = new POIFSFileSystem(fileInputStream);
				document = new HWPFDocument(pfs);
				Range range = document.getRange();//获取全文
				TableIterator it = new TableIterator(range);
				int tabnum = 1;
				while (it.hasNext()) {
					Table tb = (Table) it.next();
					if (tabnum!=2){
						tabnum++;
						continue;
					}
					List<String> strList= new ArrayList<>();
					for (int a = 1; a < tb.numRows(); a++) {
						//迭代行，默认从0开始,可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
						TableRow tr = tb.getRow(a);
						for (int b = 0; b < tr.numCells(); b++) {
							//迭代列，默认从0开始
							TableCell cell=tr.getCell(b);
							StringBuffer sb=new StringBuffer();
							if(StringUtil.isNotBlank(cell.text())) {
								sb.append(cell.text().replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", "").replaceAll("\\u0007",""));
							}
							if(StringUtil.isNotBlank(sb.toString())) {
								strList.add(sb.toString());
							}
						}

					}
					List<SjzmjgDto> zmjgList= new ArrayList<>();
					SjzmjgDto sjzmjgDto=new SjzmjgDto();
					for (int i = 1; i < strList.size()+1; i++) {
						if(i%4==0) {
							//添加list
							sjzmjgDto.setLx("ZM");
							sjzmjgDto.setJcff(strList.get(i-1));
							sjzmjgDto.setZmid(StringUtil.generateUUID());
							sjzmjgDto.setSjid(sjxxDto.getSjid());
							sjzmjgDto.setLrry(sjxxDto.getXgry());
							zmjgList.add(sjzmjgDto);
							//清空Dto
							sjzmjgDto=new SjzmjgDto();
						}else if(i%4==1) {
							sjzmjgDto.setXm(strList.get(i-1));
						}else if(i%4==2) {
							sjzmjgDto.setJg(strList.get(i-1));
						}else if(i%4==3) {
							sjzmjgDto.setCkz(strList.get(i-1));
						}
					}
					sjxxDto.setLx("ZM");
					zmAndXpertSJ(zmjgList,sjxxDto);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			} finally {
				try {
					if (output != null)
						output.close();
					if (document != null)
						document.close();
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				} catch (Exception e) {
					log.error(e.toString());
				}
			}
		}

		return true;
	}

	/**
	 * 自免结果报告生成
	 * @param sjxxDto
	 * @param paramList
	 * @return
	 */
	public boolean replaceSelfReport(SjxxDto sjxxDto, List<SjzmjgDto> paramList) {
		Map<String,String> map=dao.getWjljBySjid(sjxxDto.getSjid());
		String bgrq=map.get("bgrq")==null?null:map.get("bgrq").toString();
		if(StringUtil.isBlank(bgrq)) {
			SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
			Date date=new Date();
			map.put("bgrq", sdf.format(date));
		}
		String wjlj=map.get("wjlj");
		DBEncrypt encrypt=new DBEncrypt();
		String temp_file=encrypt.dCode(wjlj);
		FileInputStream fileInputStream = null;
		XWPFDocument document = null;
		OutputStream output = null;
		File templateFile = new File(temp_file);
		if (StringUtil.isBlank(temp_file) || !templateFile.exists()) {
			log.error("模板不存在！请重新确认模板！");
			return false;
		}
		// 把文件放入流
		try {
			fileInputStream = new FileInputStream(templateFile);
			// 读取word文件
			document = new XWPFDocument(fileInputStream);
			Iterator<XWPFTable> itTable = document.getTablesIterator();
			// 迭代itTable 进行数据的替换
			while (itTable.hasNext()) {
				XWPFTable table = (XWPFTable) itTable.next();
				// 替换表中的数据
				replaceTable(table,map,paramList,sjxxDto);
				//合并相同的行
//				colspan(table);
			}
			String ywlx = sjxxDto.getCskz3() + "_" + sjxxDto.getCskz1() + "_WORD";
			//String wjm=map.get("ybbh")+"-"+map.get("sjdw")+"-"+map.get("hzxm")+"-"+map.get("jcxm")+".docx";
			//自免报告文件名称，根据销售要求，去除后面的项目信息 2021-03-28 lwj
			String wjm=map.get("ybbh")+"-"+map.get("sjdw")+"-"+map.get("hzxm")+".docx";
			String storePath = prefix+releaseFilePath + ywlx + "/UP"+
					DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			mkDirs(storePath);

			String fwjm = System.currentTimeMillis() + RandomUtil.getRandomString()+".docx";
			output = new FileOutputStream(storePath+"/"+fwjm);
			document.write(output);
			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setFjid(StringUtil.generateUUID());
			fjcfbDto.setYwid(map.get("sjid").toString());
			fjcfbDto.setZywid("");
			fjcfbDto.setXh("1");
			fjcfbDto.setYwlx(ywlx);
			fjcfbDto.setWjm(wjm);
			fjcfbDto.setWjlj(bpe.eCode(storePath+"/"+fwjm));
			fjcfbDto.setFwjlj(bpe.eCode(storePath));
			fjcfbDto.setFwjm(bpe.eCode(fwjm));
			fjcfbDto.setZhbj("0");
			boolean isTrue=fjcfbService.insert(fjcfbDto);
			//同步word到8085
			sendFileToAli(fjcfbDto);
			if(isTrue) {
				//刪除之前的word
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
			}
			//转换PDF
			String wjljjm=bpe.dCode(fjcfbDto.getWjlj());
			//连接服务器
			boolean sendFlg=sendWordFile(wjljjm,FTP_URL);
			if(sendFlg) {
				Map<String,String> pdfMap= new HashMap<>();
				pdfMap.put("wordName", fwjm);
				pdfMap.put("fwjlj",fjcfbDto.getFwjlj());
				pdfMap.put("fjid",fjcfbDto.getFjid());
				pdfMap.put("ywlx",ywlx.replace("_WORD", ""));
				pdfMap.put("MQDocOkType",DOC_OK);
				//删除PDF
				FjcfbDto fjcfbDto_t=new FjcfbDto();
				fjcfbDto_t.setYwlx(pdfMap.get("ywlx").toString());
				fjcfbDto_t.setYwid(map.get("sjid").toString());
				fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
				//发送Rabbit消息转换pdf
				amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.toString();
		}finally {
			try {
				if (output != null)
					output.close();
				if (document != null)
					document.close();
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
		return true;
	}

	/**
	 * 替换表格内容
	 * @param table
	 * @param paramList
	 * @param sjxxDto
	 */
	private void replaceTable(XWPFTable table,Map<String,String > map,List<SjzmjgDto> paramList,SjxxDto sjxxDto) {
 		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		// 获取到最后行的第一列 ；
		XWPFTableCell lastcell = tmpRow.getCell(0);
		List<XWPFParagraph> lastcellParList = lastcell.getParagraphs();
		StringBuffer s_cellText = new StringBuffer();
		for (int p = 0; lastcellParList != null && p < lastcellParList.size(); p++) {
			String t_cellText = lastcellParList.get(p).getText();
			if (StringUtil.isNotBlank(t_cellText))
				s_cellText.append(t_cellText);
		}
		String cellText = s_cellText.toString();
		int lastStartTagPos = cellText.indexOf("«");
		// 判断如果为循环表格
		if (cellText != null && lastStartTagPos > -1) {
			// 获取循环表格的变量
			int lastEndTagPos = cellText.indexOf("»");
			// 寻找到需要替换的变量，如果没有找到则到下一个run
			if (lastStartTagPos > -1 && lastEndTagPos > 0 && lastEndTagPos > lastStartTagPos) {
				String varString = cellText.substring(lastStartTagPos + 1, lastEndTagPos);
				replaceListTable(table, paramList, varString,sjxxDto);
			}
		} else {
			// 普通表格，则进行替换操作
			// 获取到table 的行数 rows（多）
			int rcount = table.getNumberOfRows();
			for (int i = 0; i < rcount; i++) {
				// 拿到当前行的内容
				XWPFTableRow row = table.getRow(i);
				// 获取当前行的列数
				List<XWPFTableCell> cells = row.getTableCells();
				// 循环列（cells 多）
				for (XWPFTableCell cell : cells) {
					// 拿到当前列的 段落（多） 
					List<XWPFParagraph> cellParList = cell.getParagraphs();
					// 循环每个段落
					for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
						// 拿到段落中的runs；
						List<XWPFRun> runs = cellParList.get(p).getRuns();
						// 循环runs
						for (int q = 0; runs != null && q < runs.size(); q++) {
							// 获取到run；run为word文档中最小的变量
							String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
							// 过滤掉空的run
							if (StringUtil.isBlank(oneparaString))
								continue;
							// 循环替换的数据
							while (true) {
								int startTagPos = oneparaString.indexOf("«");
								int endTagPos = oneparaString.indexOf("»");
								// 寻找到需要替换的变量，如果没有找到则到下一个run
								if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
									String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
									Object object_value = map.get(varString);
									if (object_value instanceof String) {
										String rep_value = (String) object_value;
										// 如果找不到相应的替换问题，则替换成空字符串
										if (StringUtil.isBlank(rep_value)) {
											oneparaString = oneparaString.replace("«" + varString + "»", " ");
										} else {
											oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
										}
										runs.get(q).setText(oneparaString, 0);
									} else {
										oneparaString = oneparaString.replace("«" + varString + "»", " ");
										runs.get(q).setText(oneparaString, 0);
									}

								} else {
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 替换循环列表
	 *
	 * @param table
	 * @param paramList
	 * @param varString
	 * @param sjxxDto
	 */
	private void replaceListTable(XWPFTable table,List<SjzmjgDto> paramList, String varString,SjxxDto sjxxDto) {
		// 如果没有数据，则清除掉空的row，显示为 --
		if (paramList.size() == 0) {
			return;
		}

		// 获取表格的列数，根据最后一列
		int rowCnt = table.getRows().size();
		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		int cellsize = tmpCells.size();
		for (int x = 0; x < paramList.size(); x++) {
			// 创建一个新的行 createRow
			XWPFTableRow row = table.createRow();
			// 设置新建行的高度（同模板的高度一样）
			row.setHeight(tmpRow.getHeight());
			SjzmjgDto sjzmjgDto = paramList.get(x);
			boolean result = false;
			try {
				List<XWPFTableCell> cells = row.getTableCells();
				// 插入的行会填充与表格第一行相同的列数
				for (int i = 0; i < cellsize; i++) {

					XWPFTableCell cell = cells.get(i);
					XWPFTableCell tmpCell = tmpCells.get(i);

					String tmpText = tmpCell.getText();
					if (StringUtil.isNotBlank(tmpText)) {
						int star = tmpText.indexOf("«");
						int end = tmpText.indexOf("»");
						if (star > -1 && end > -1) {
							String content = tmpText.substring(star, end+1 ).toString();
							int startTagPos = content.indexOf("«");
							int endTagPos = content.indexOf("»");
							// 寻找到需要替换的变量，如果没有找到则到下一个run
							if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
								String tmpVar = content.substring(startTagPos + 1, endTagPos).trim();
								String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
								// 反射方法 把map的value 插入到数据中
								Method t_method = sjzmjgDto.getClass().getMethod("get" + TotmpVar);
								String s_value = (String) t_method.invoke(sjzmjgDto);
								if ("XPERT".equals(sjxxDto.getLx())){
									boolean isCenter = !"«jsjjy»".equals(tmpText);
									setCellTableText(tmpCells.get(i), cell,StringUtil.isNotBlank(s_value)?s_value:"",false,false,false,isCenter);

								}else{
									setCellText(tmpCells.get(i), cell, s_value, "Ckz".equals(TotmpVar),x);
								}
							}
						}
						if ("«jsjjy»".equals(tmpText)){
							result = true;
						}
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
			if (result){
				break;
			}
		}
		// 删除模版行
		table.removeRow(rowCnt - 1);
	}

	/**
	 * 往新建行中插入数据
	 *
	 * @param tmpCell  模板列，用于获取列的样式风格等等
	 * @param cell	 需要插入数据的新建列
	 * @param text	 需要插入的数据
	 * @param isItalic 是否需要斜体
	 * @throws Exception
	 */
	private void setCellTableText(XWPFTableCell tmpCell, XWPFTableCell cell, String text, boolean isItalic,boolean isBreak,boolean setBorder,boolean isCenter)
			throws Exception {
		// TODO Auto-generated method stub
		XWPFRun tmprun=null;
		for (int i = 0; i < tmpCell.getParagraphs().size(); i++) {
			if(tmprun!=null)
				break;
			XWPFParagraph paragraph=tmpCell.getParagraphs().get(i);
			for (int j = 0; j < paragraph.getRuns().size(); j++) {
				if(tmprun!=null)
					break;
				XWPFRun run=paragraph.getRuns().get(j);
				if(StringUtil.isNotBlank(run.getText(paragraph.getRuns().get(j).getTextPosition()))) {
					tmprun=run;
					break;
				}
			}

		}
		int fontSize=0;
		String fontFamily = "";
		if(tmprun!=null) {
			if(tmprun.getFontSize()>-1) {
				fontSize=tmprun.getFontSize();
			}
			if (StringUtil.isNotBlank(tmprun.getFontFamily())) {
				fontFamily = tmprun.getFontFamily();
			}
		}
		CTTc cttc2 = tmpCell.getCTTc();
		CTTcPr ctPr2 = cttc2.getTcPr();

		CTTc cttc = cell.getCTTc();
		CTTcPr ctPr = cttc.addNewTcPr();
		cell.setColor(tmpCell.getColor());
		cell.setVerticalAlignment(tmpCell.getVerticalAlignment());
		if (ctPr2.getTcW() != null) {
			ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
		}
		if (ctPr2.getVAlign() != null) {
			ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
		}
		if (cttc2.getPList().size() > 0) {
			CTP ctp = cttc2.getPList().get(0);
			if (ctp.getPPr() != null) {
				if (ctp.getPPr().getJc() != null) {
					cttc.getPList().get(0).addNewPPr().addNewJc().setVal(ctp.getPPr().getJc().getVal());
				}
			}
		}
		if(setBorder) {
			if (ctPr2.getTcBorders() != null) {
				ctPr.setTcBorders(ctPr2.getTcBorders());
			}
		}
		XWPFParagraph cellP = cell.getParagraphs().get(0);
		cellP.setSpacingBetween(20, LineSpacingRule.EXACT);//这里面的18指的是磅值  //LineSpacingRule.EXACT指的是指定值
		if (isCenter){
			cellP.setAlignment(ParagraphAlignment.CENTER);
		}
		List<XWPFRun> runList = cellP.getRuns();
		XWPFRun cellR;
		XWPFTableCell tmpCellR;
		if(runList== null || runList.size() == 0) {
			tmpCellR = tmpCell;
			cellR = cellP.createRun();
		}else {
			tmpCellR = cell;
			cellR = runList.get(0);
		}
		if(fontSize>0) {
			cellR.setFontSize(fontSize);
		}
		//cellR.setFontFamily("思源黑体");
		if (StringUtil.isNotBlank(fontFamily)) {
			cellR.setFontFamily(fontFamily);
		}
		// 设置斜体
		cellR.setItalic(isItalic);
		String oneparaString = tmpCellR.getText();
		if(StringUtil.isNotBlank(oneparaString)) {
			int startTagPos = oneparaString.indexOf("«");
			int endTagPos = oneparaString.indexOf("»");
			String varString = oneparaString.substring(startTagPos + 1, endTagPos);
			String preString = oneparaString.substring(0,startTagPos);
			int index = 0;
			if(StringUtil.isNotBlank(preString)) {
				cellR.setText(preString,index);
				index ++;
			}
			// 设置换行
			if(isBreak) {
				cellR.addBreak();
			}

			String afterString = oneparaString.substring(startTagPos);

			oneparaString = afterString.replace("«" + varString + "»", text);
			if ("jsjjy".equals(varString)){
				String[] split = text.split("。");
				for (String s:split){
					if (s.contains("建议与解释：") || s.contains("探针信息说明：") || s.contains("建议如下：")){
						if (s.contains("建议如下：")){
							cellR.addBreak();
							cellR.setText("	");
						}
						String[] split1 = s.split("：");
						cellR.setText(split1[0]+"：");
						cellR.addBreak();
						cellR.setText("	");
						for (int i = 1; i < split1.length; i++) {
							if (i==split1.length-1){
								cellR.setText(split1[i]);
								cellR.setText("。");
							}else{
								cellR.setText(split1[i]+":");
							}
						}
					}else if(s.contains("建议与解释:")){
						cellR.addBreak();
						String[] split1 = s.split(":");
						cellR.setText(split1[0]+"：");
						cellR.addBreak();
						cellR.setText("	");
						for (int i = 1; i < split1.length; i++) {
							if (i==split1.length-1){
								cellR.setText(split1[i]);
								cellR.setText("。");
							}else{
								cellR.setText(split1[i]+":");
							}
						}
					}else{
						cellR.addBreak();
						cellR.setText("	"+s+"。");
					}
				}
			}else{
				cellR.setText(oneparaString,index);
			}
		}else {
			cellR.setText(text,0);
		}

	}


	/**
	 * 往新建行中插入数据
	 *
	 * @param tmpCell  模板列，用于获取列的样式风格等等
	 * @param cell	 需要插入数据的新建列
	 * @param text	 需要插入的数据
	 * @param isCenter
	 * @param index
	 * @throws Exception
	 */
	private void setCellText(XWPFTableCell tmpCell, XWPFTableCell cell, String text,boolean isCenter,int index)
			throws Exception {
		// TODO Auto-generated method stub
		if(index % 2==0) {
			 cell.setColor(tmpCell.getColor());
		}else {
			 cell.setColor("FFFFFF");
		}
		CTTc cttc2 = tmpCell.getCTTc();
		CTTcPr ctPr2 = cttc2.getTcPr();

		CTTc cttc = cell.getCTTc();
		CTTcPr ctPr = cttc.addNewTcPr();
		cell.setVerticalAlignment(tmpCell.getVerticalAlignment());
		if (ctPr2.getTcW() != null) {
			ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
		}
		if (ctPr2.getVAlign() != null) {
			ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
		}
		if (cttc2.getPList().size() > 0) {
			CTP ctp = cttc2.getPList().get(0);
			if (ctp.getPPr() != null) {
				if (ctp.getPPr().getJc() != null) {
					cttc.getPList().get(0).addNewPPr().addNewJc().setVal(ctp.getPPr().getJc().getVal());
				}
			}
		}
		if (ctPr2.getTcBorders() != null) {
			ctPr.setTcBorders(ctPr2.getTcBorders());
		}
		for (int i = cell.getParagraphs().size()-1; i > 0; i--) {
			cell.removeParagraph(i);
		}
		XWPFParagraph paragraph = cell.getParagraphs().get(0);
		paragraph.setSpacingBetween(18, LineSpacingRule.EXACT);//这里面的18指的是磅值  //LineSpacingRule.EXACT指的是指定值

		if(isCenter) {
			paragraph.setAlignment(ParagraphAlignment.CENTER);
		}
		XWPFRun run =paragraph.createRun();
		run.setFontFamily("微软雅黑");
		run.setText(text);
	}

//	/**
//	 * 合并相同的行
//	 * 
//	 * @param table
//	 */
//	private void colspan(XWPFTable table) {
//		List<XWPFTableRow> rows= table.getRows();
//		for (int i = 1; i < rows.size(); i++) {// 每一行
//			if(i==rows.size()-1){
//				break;
//			}else if (table.getRow(i).getCell(3).getText().equals(table.getRow(i+1).getCell(3).getText())) {	
//				table.getRow(i).getCell(3).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
//				table.getRow(i+1).getCell(3).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
//			}
//		}
//	}

	/**
	 * 上传Word文件
	 * @return
	 */
	private boolean sendWordFile(String fileName,String FTP_URL) {
		//连接服务器
		//FTPClient ftp=FtpUtil.connect(FTPWORD_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
		//上传到服务器word下边的文件夹
		//FtpUtil.upload(new File(wjljjm), ftp);
		try {
			File t_file = new File(fileName);
			//文件不存在不做任何操作
			if(!t_file.exists())
				return true;

			byte[] bytesArray = new byte[(int) t_file.length()];

			FileInputStream t_fis = new FileInputStream(t_file);
			t_fis.read(bytesArray); //read file into bytes[]
			t_fis.close();
			//需要给文件的名称
			MatridxByteArrayResource contentsAsResource = new MatridxByteArrayResource(bytesArray);
			contentsAsResource.setFilename(t_file.getName());

			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("file", contentsAsResource);
			RestTemplate t_restTemplate = new RestTemplate();
			//发送文件到服务器
			String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);
			return "OK".equals(reString);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取测试数信息根据检测单位区分
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjxxcssByJcdw(SjxxDto sjxxDto){
		return dao.getSjxxcssByJcdw(sjxxDto);
	}
	/**
	 * 获取测试数信息根据检测单位区分(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjxxcssByJcdwAndJsrq(SjxxDto sjxxDto){
		return dao.getSjxxcssByJcdwAndJsrq(sjxxDto);
	}

	/**
	 * 获取测试数信息根据检测单位区分(日报)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjxxcssByDayAndJcdw(SjxxDto sjxxDto){
		return dao.getSjxxcssByDayAndJcdw(sjxxDto);
	}

	/**
	 * 获取测试数信息根据检测单位区分(日报)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjxxcssByDayAndJcdwAndJsrq(SjxxDto sjxxDto){
		return dao.getSjxxcssByDayAndJcdwAndJsrq(sjxxDto);
	}

	/**
	 * 检测项目个数(按照一段周期内检测单位进行)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjxxcssBySomeTimeAndJcdw(SjxxDto sjxxDto){
		return dao.getSjxxcssBySomeTimeAndJcdw(sjxxDto);
	}
	/**
	 * 检测项目个数(按照一段周期内检测单位进行)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjxxcssBySomeTimeAndJcdwAndJsrq(SjxxDto sjxxDto){
		return dao.getSjxxcssBySomeTimeAndJcdwAndJsrq(sjxxDto);
	}

	/**
	 * 查询接收后但未实验的标本信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getNotSyList(SjxxDto sjxxDto){
		return dao.getNotSyList(sjxxDto);
	}
	/**
	 * 查询接收后但未实验的标本信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getNotSyListByJsrq(SjxxDto sjxxDto){
		return dao.getNotSyListByJsrq(sjxxDto);
	}

	/**
	 * 首页统计:统计当天实验的数据量(根据检测单位区分)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getSjlStatisticsByJcdw(SjxxDto sjxxDto){
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		sjxxDto.setJsrq(date);
		return dao.getSjlStatisticsByJcdw(sjxxDto);
	}

	/**
	 * 检查标题定义，主要防止模板信息过旧
	 * @param defined
	 * @return
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}

	/**
	 * 更新报告日期
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public int updateReport(SjxxDto sjxxDto) {
		return dao.updateReport(sjxxDto);
	}

	/**
	 * 以map为返回值查询SJxx
	 * @param sjid
	 * @return
	 */
	@Override
	public Map<String,Object> getMapBySjid(String sjid,String jclx,String jczlx){
		// TODO Auto-generated method stub
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("sjid",sjid);
		paramMap.put("jclx",jclx);
		paramMap.put("jczlx",jczlx);
		Map<String,Object> map=dao.getMapBySjid(paramMap);
		SjkzxxDto sjkzxxDto = sjkzxxService.getDtoById(sjid);
		if(sjkzxxDto!=null && StringUtil.isNotBlank(sjkzxxDto.getQtxx())) {
			String qtxx = sjkzxxDto.getQtxx();
			try {
				Map<String,Object> qtxxmap = JSONObject.parseObject(qtxx, new TypeReference<>() {});
				map.putAll(flatten(qtxxmap,""));
			} catch (Exception e) {
				log.error("解析sjkzxxDto.getQtxx()失败,{}",qtxx);
			}
		}
		if(map.get("gzlxdz")!=null && StringUtil.isNotBlank((String) map.get("gzlxdz"))) {
			DBEncrypt dbEncrypt = new DBEncrypt();
			map.put("gzlxdz",dbEncrypt.dCode((String) map.get("gzlxdz")));
		}
		paramMap.put("hbid",String.valueOf(map.get("hbid")));
		//获取报告模板
		Map<String,Object> bgmb_map=sjxxWsDao.getReportMould(paramMap);
		map.put("bzfwjlj",bgmb_map==null?null:bgmb_map.get("bzfwjlj"));
		map.put("bzfwjm",bgmb_map==null?null:bgmb_map.get("bzfwjm"));
		map.put("bgmbcskz1",bgmb_map==null?null:bgmb_map.get("bgmbcskz1"));
		map.put("jcxmmc",bgmb_map==null?null:bgmb_map.get("jcxmmc"));
		map.put("jcxm_kzcs3",bgmb_map==null?null:bgmb_map.get("jcxm_kzcs3"));
		map.put("jcxmcs1",bgmb_map==null?null:bgmb_map.get("jcxmcs1"));
		map.put("cskz2",bgmb_map==null?null:bgmb_map.get("cskz2"));//报告模板cskz2
		map.put("cskz3",bgmb_map==null?null:bgmb_map.get("cskz3"));//报告模板cskz3
		map.put("tmpate_flag",bgmb_map==null?null:bgmb_map.get("tmpate_flag"));
		map.put("report_syrq",bgmb_map==null?null:bgmb_map.get("report_syrq"));
		//若实验日期为空，可能是q项目出onco报告，关联不到，这种情况从sygl表获取最新的实验日期
		if(bgmb_map!=null && (StringUtil.isBlank(String.valueOf(bgmb_map.get("report_syrq"))) || bgmb_map.get("report_syrq")==null)){
			SjsyglDto syglDto = new SjsyglDto();
			syglDto.setSjid(sjid);
			List<SjsyglDto> sjsyglDtos=sjsyglService.getOrderSyrqDto(syglDto);
			if(!CollectionUtils.isEmpty(sjsyglDtos)){
				map.put("report_syrq",sjsyglDtos.get(0).getSyrq());
			}
		}
		//若没有报告日期，则获取当前日期，主要是考虑到onco报告先出的情况
		if(bgmb_map!=null && (StringUtil.isBlank(String.valueOf(bgmb_map.get("report_date"))) || bgmb_map.get("report_date")==null)){
			map.put("report_date",DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		}

		map.put("bzfwjlj2",bgmb_map==null?null:bgmb_map.get("bzfwjlj2"));
		map.put("bzfwjm2",bgmb_map==null?null:bgmb_map.get("bzfwjm2"));
		map.put("bgmb2cskz1",bgmb_map==null?null:bgmb_map.get("bgmb2cskz1"));
		map.put("bgmb2cskz2",bgmb_map==null?null:bgmb_map.get("bgmb2cskz2"));//报告模板2cskz2

		if(map!=null && map.get("jcdwcsdm")!=null && "32".equals(map.get("jcdwcsdm").toString()) && map.get("hbmc")!=null
				&& StringUtil.isNotBlank(map.get("hbmc").toString()) && !map.get("hbmc").toString().contains("艾迪康")) {
			paramMap.put("hbmc","无");
			Map<String,Object> bgmbMap = sjxxWsDao.getDefaultPartner(paramMap);
			map.put("bzfwjlj2",bgmbMap==null?null:bgmbMap.get("bzfwjlj2"));
			map.put("bzfwjm2",bgmbMap==null?null:bgmbMap.get("bzfwjm2"));
			map.put("bgmb2cskz1",bgmbMap==null?null:bgmbMap.get("bgmb2cskz1"));
			map.put("bgmb2cskz2",bgmbMap==null?null:bgmbMap.get("bgmb2cskz2"));
			map.put("sendFlag","1");
		}

		//获取提取明细中的核酸浓度，时间倒序排列
		List<Map<String, Object>> tqmxList = sjxxWsDao.getTqmxBySjid(sjid);
		String hsnd = "-";
		if (tqmxList!=null && tqmxList.size()>0){
			//获取提取明细中，最近的，且检测项目代码与当前项目一直的一条，并设置核酸浓度
			for (Map<String, Object> tqmx : tqmxList) {
				if(map.get("jcxmcs1") != null && tqmx.get("jcxmdm") != null){
					if(map.get("jcxmcs1").toString().contains(tqmx.get("jcxmdm").toString())){
						hsnd = tqmx.get("hsnd")!=null? tqmx.get("hsnd").toString() : "-";
						break;
					}
				}
			}
			//若核酸浓度未获取到，则获取第一条数据的核酸浓度
			if (hsnd.equals("-")){
				hsnd = tqmxList.get(0).get("hsnd")!=null? tqmxList.get(0).get("hsnd").toString() : "-";
			}
		}
		map.put("hsnd",hsnd);
		//根据jclx获取检测项目信息
		if (map.get("sjrq")==null){
			map.put("sjrq","");
		}
		if(map.get("nl")!=null) {
			String nl=map.get("nl").toString();
			String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
			String nldwString;
			if(map.get("nldw") == null) {
				nldwString = "岁";
			}else {
				nldwString = map.get("nldw").toString();
			}
			if(nl.matches(regex)) {
				map.put("nl",map.get("nl").toString()+ nldwString);
			}
		}else {
			map.put("nl", "无");
		}
		if(map.get("ryzs")==null&&map.get("ryzsbfb")==null) {
			map.put("host_index", "一");
		}else{
			map.put("host_index", (map.get("ryzs")==null?"一":map.get("ryzs").toString())+",高于"+(map.get("ryzsbfb")==null?"一":map.get("ryzsbfb").toString())+"%的同类标本");
		}
		String zxls=map.get("zxls")!=null?map.get("zxls").toString():null;
		String wswjcxls=map.get("wswjcxls")!=null?map.get("wswjcxls").toString():null;
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		if(StringUtil.isNotBlank(zxls)) {
			Double zxlsToInt=Double.parseDouble(zxls);
			zxls = decimalFormat.format(zxlsToInt);
			map.put("zxls", zxls);
		}
		if(StringUtil.isNotBlank(wswjcxls)) {
			Double wswjcxlsToInt=Double.parseDouble(wswjcxls);
			wswjcxls = decimalFormat.format(wswjcxlsToInt);
			map.put("wswjcxls", wswjcxls);
		}
		// 如果没找到模板默认使用合作伙伴为 无 的报告模板
//		if(StringUtil.isBlank((String)map.get("fwjlj"))) {
//			log.error("查询合作伙伴为无的报告模板！");
//			SjhbxxDto t_SjhbxxDto = new SjhbxxDto();
//			t_SjhbxxDto.setYwlx((String)map.get("tmpate_flag"));
//			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getBgmbByHbmc(t_SjhbxxDto);
//			if(sjhbxxDtos != null && sjhbxxDtos.size() > 0) {
//				map.put("fwjm", sjhbxxDtos.get(0).getFwjm());
//				map.put("fwjlj", sjhbxxDtos.get(0).getFwjlj());
//				map.put("hblx", sjhbxxDtos.get(0).getHblx());
//				map.put("hbmc", "无");
//			}
//		}

		List<Map<String,Object>> list=dao.getQqjcForReport(sjid);
		if(list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				map.put(list.get(i).get("csdm").toString(), list.get(i).get("jcz"));
			}
		}
		return map;
	}

	/**
	 * 修改实验日期
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean updateSyrq(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.updateJcbjByid(sjxxDto);
	}

	/**
	 * 查询未发送送检报告
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> querySjxx(SjxxDto sjxxDto) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());

		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());

		sjxxDto.setSyrq(syrq);
		sjxxDto.setDsyrq(syrq);

		sjxxDto.setSyrqstart(syrqStart);
		sjxxDto.setSyrqend(syrqEnd);

		String bgrq = format.format(new Date());
		sjxxDto.setBgrq(bgrq);

		calendar.set(Calendar.HOUR_OF_DAY, 12);
		String cxkssj = formatHH.format(calendar.getTime());
		calendar.add(Calendar.DATE, 1);
		String lrsjEnd = formatHH.format(calendar.getTime());
		sjxxDto.setLrsjStart(cxkssj);
		sjxxDto.setLrsjEnd(lrsjEnd);

		List<SjxxDto> sjxxs = dao.querySjxx(sjxxDto);
		for (SjxxDto sjxx : sjxxs) {
			//复测
			if("RECHECK".equals(sjxx.getLxdm())||"LAB_RECHECK".equals(sjxx.getLxdm())) {
				sjxx.setLxmc("复测");
			}
			if("ADDDETECT".equals(sjxx.getLxdm())||"LAB_ADDDETECT".equals(sjxx.getLxdm())) {
				sjxx.setLxmc("加测");
			}
			if("".equals(sjxx.getLxdm())) {
				sjxx.setLxmc("新检");
			}if(sjxx.getLxdm()==null) {
				sjxx.setLxmc("新检");
			}
		}
		return sjxxs;
	}

	/**
	 * 获取总数
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Integer querysjxxnum(SjxxDto sjxxDto) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());

		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		String cxkssj = formatHH.format(calendar.getTime());
		calendar.add(Calendar.DATE, 1);
		String lrsjEnd = formatHH.format(calendar.getTime());

		sjxxDto.setSyrq(syrq);
		sjxxDto.setDsyrq(syrq);

		sjxxDto.setLrsj(cxkssj);
		sjxxDto.setLrsjStart(cxkssj);
		sjxxDto.setLrsjEnd(lrsjEnd);

		sjxxDto.setSyrqstart(syrqStart);
		sjxxDto.setSyrqend(syrqEnd);

		String bgrq = format.format(new Date());
		sjxxDto.setBgrq(bgrq);

		return dao.querysjxxnum(sjxxDto);
	}

	/**
	 * 获取上传word但未发送报告总数
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public int queryWordNum(SjxxDto sjxxDto) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String bgrq = format.format(new Date());
		sjxxDto.setBgrq(bgrq);
		List<JcsjDto>detectionList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<String>jcxmids_word=new ArrayList<>();
		List<String>jcxmids_pdf=new ArrayList<>();
		for(JcsjDto jcsjDto:detectionList){
			String str_word=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1()+"WORD";
			String str_pdf=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1();
			jcxmids_word.add(str_word);
			jcxmids_pdf.add(str_pdf);
		}
		sjxxDto.setFjlxs(jcxmids_word);
		sjxxDto.setFjlxs1(jcxmids_pdf);
//		List<String> fjlxs = new ArrayList<>();
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_D.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_R.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_C.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_D_REM.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_R_REM.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_C_REM.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_REM_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_REM_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_REM_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_REM_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_REM_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_REM_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_REM_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_REM_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_REM_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z6_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z8_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z12_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z18_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_F_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_T3_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_T6_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_K_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_CONTRACT_WORD.getCode());
//		sjxxDto.setFjlxs(fjlxs);
		return dao.queryWordNum(sjxxDto);
	}

	/**
	 * 获取上传word但未发送报告信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> queryWord(SjxxDto sjxxDto) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String bgrq = format.format(new Date());
		sjxxDto.setBgrq(bgrq);
		List<JcsjDto>detectionList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<String>jcxmids_word=new ArrayList<>();
		List<String>jcxmids_pdf=new ArrayList<>();
		for(JcsjDto jcsjDto:detectionList){
			String str_word=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1()+"WORD";
			String str_pdf=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1();
			jcxmids_word.add(str_word);
			jcxmids_pdf.add(str_pdf);
		}
		sjxxDto.setFjlxs(jcxmids_word);
		sjxxDto.setFjlxs1(jcxmids_pdf);
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_D.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_R.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_C.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_D_REM.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_R_REM.getCode());
//		fjlxs.add(BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_C_REM.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_REM_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_REM_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_REM_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_REM_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_REM_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_REM_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_REM_D_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_REM_R_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_SPEED_REM_C_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z6_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z8_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z12_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_Z18_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_F_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_T3_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_T6_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_REPORT_TEMEPLATE_K_WORD.getCode());
//		fjlxs.add(BusTypeEnum.IMP_CONTRACT_WORD.getCode());
//		sjxxDto.setFjlxs(fjlxs);
		List<SjxxDto> sjxxs = dao.queryWord(sjxxDto);
		for (SjxxDto sjxx : sjxxs) {
			//复测
			if("RECHECK".equals(sjxx.getLxdm())||"LAB_RECHECK".equals(sjxx.getLxdm())) {
				sjxx.setLxmc("复测");
			}
			if("ADDDETECT".equals(sjxx.getLxdm())||"LAB_ADDDETECT".equals(sjxx.getLxdm())) {
				sjxx.setLxmc("加测");
			}
			if("".equals(sjxx.getLxdm())) {
				sjxx.setLxmc("新检");
			}if(sjxx.getLxdm()==null) {
				sjxx.setLxmc("新检");
			}
		}
		return sjxxs;
	}

	/**
	 * 获取前一天到今天6点前审核中的复测验证数量
	 * @param sjxxDto
	 * @return
	 */
	public int queryFjAndYzBeforeNum(SjxxDto sjxxDto){
		return dao.queryFjAndYzBeforeNum(sjxxDto);
	}

	/**
	 * 获取前一天到今天6点前审核中的复测验证信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> queryFjAndYzBefore(SjxxDto sjxxDto){
		return dao.queryFjAndYzBefore(sjxxDto);
	}

	/**
	 * 获取今天6后前审核中的复测验证数量
	 * @param sjxxDto
	 * @return
	 */
	public int queryFjAndYzAfterNum(SjxxDto sjxxDto){
		return dao.queryFjAndYzAfterNum(sjxxDto);
	}

	/**
	 * 获取今天6后前审核中的复测验证信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> queryFjAndYzAfter(SjxxDto sjxxDto){
		return dao.queryFjAndYzAfter(sjxxDto);
	}

	/**
	 * 查询接收日期非空，报告未上传的所有信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getAccptNotUpload(SjxxDto sjxxDto) {
		SimpleDateFormat dayDeal = new SimpleDateFormat("yyyy-MM-dd");
		// 正常送检显示当前时间和前两天内的信息
		sjxxDto.setJsrq(dayDeal.format(DateUtils.getDate(new Date(), -7)));
		// 复检显示当前之间和前两天内的时间
		sjxxDto.setLrsj(dayDeal.format(DateUtils.getDate(new Date(), -7)));
		// 当天实验日期
		sjxxDto.setSyrq(dayDeal.format(DateUtils.getDate(new Date(),0)));

		return dao.getAccptNotUpload(sjxxDto);
	}



	/**
	 * 获取营销resfirst列表数据
	 * @param
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedSalesResFirstList(SjxxDto sjxxDto){
		return sjxxTwoDao.getPagedSalesResFirstList(sjxxDto);
	}

	/**
	 * 获取实验列表数据
	 * @param
	 * @return
	 */
	@Override
	public List<SjxxDto> getExperimentList(SjxxDto sjxxDto){
		return sjxxTwoDao.getExperimentList(sjxxDto);
	}


	/**
	 * 从送检表中查询单条接收日期非空，报告未上传的信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getOneFromSJ(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getOneFromSJ(sjxxDto);
	}

	/**
	 * 从复检表中查询单条接收日期非空，报告未上传的信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public SjxxDto getOneFromFJ(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getOneFromFJ(sjxxDto);
	}

	/**
	 * 从送检表中找出检查项目的类别数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> jcxmFromSJ(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.jcxmFromSJ(sjxxDto);
	}

	/**
	 * 从复检表中找出检查项目的类别数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> jcxmFromFJ(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.jcxmFromFJ(sjxxDto);
	}

	/**
	 * 根据ids(实际传入是fjid)查询送检信息
	 */
	@Override
	public List<SjxxDto> selectSjxxByFjids(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectSjxxByFjids(sjxxDto);
	}



	/**
	 * 修改检测标记（复检表）
	 *
	 * @param sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public boolean updateJcbjByFjid(SjxxDto sjxxDto) throws BusinessException
	{
		// TODO Auto-generated method stub
		if (StringUtil.isBlank(sjxxDto.getJcbj()))
		{
			sjxxDto.setJcbj("0");
			sjxxDto.setSyrq(null);
		}
		if (StringUtil.isBlank(sjxxDto.getDjcbj()))
		{
			sjxxDto.setDjcbj("0");
			sjxxDto.setDsyrq(null);
		}
		if (StringUtil.isBlank(sjxxDto.getQtjcbj()))
		{
			sjxxDto.setQtjcbj("0");
			sjxxDto.setQtsyrq(null);
		}
		boolean result = dao.updateJcbjByFjid(sjxxDto);
		//发送实验通知
		if(result && !"F".equals(sjxxDto.getCskz1())) {
			if("1".equals(sjxxDto.getSfsytz())) {
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
				Date date=new Date();
				String dqrq=formatter.format(date);//当前日期
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(Calendar.DATE,1);
				date=calendar.getTime();
				String mtrq=formatter.format(date);//明天
				String ICOMM_SJ00048=xxglService.getMsg("ICOMM_SJ00048");
				for(int i=0;i<sjxxDto.getIds().size();i++) {
					String sjid = fjsqDao.getSjidFromFjid(sjxxDto.getIds().get(i));
					SjxxDto t_sjxxDto=dao.getDtoById(sjid);
					if(t_sjxxDto!=null) {
						boolean messageBoolean = commonService.queryAuthMessage(t_sjxxDto.getHbid(),"INFORM_HB00008");
						if(messageBoolean){
							String ICOMM_SJ00047=xxglService.getMsg("ICOMM_SJ00047", t_sjxxDto.getHzxm(),t_sjxxDto.getCsmc(),dqrq,mtrq);
							String keyword1 = DateUtils.getCustomFomratCurrentDate("HH:mm:ss");
							String keyword2 = t_sjxxDto.getYbbh();
							Map<String,Object> map= new HashMap<>();
							map.put("yxbt", ICOMM_SJ00048);
							map.put("yxnr", ICOMM_SJ00047);
							map.put("ddbt", ICOMM_SJ00048);
							map.put("ddnr", ICOMM_SJ00047);
							map.put("wxbt", ICOMM_SJ00048);
							map.put("remark", ICOMM_SJ00047);
							map.put("keyword2", keyword1);
							map.put("keyword1", keyword2);
							map.put("keyword3", t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getCsmc());
							map.put("templateid", ybzt_templateid);
							map.put("xxmb","TEMPLATE_EXCEPTION");
							String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+ICOMM_SJ00047;

							map.put("reporturl",reporturl);
							sjxxCommonService.sendMessage(t_sjxxDto, map);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 同步送检支付信息
	 * @param sjxxDto
	 */
	@Override
	public void modInspPayinfo(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		if (sjxxDto.getSjjcxms()!=null && sjxxDto.getSjjcxms().size()>0){
			int result = sjjcxmService.updateSjjcxmDtos(sjxxDto.getSjjcxms());
			if(result == 0)
				log.error("modInspPayinfo 修改检测项目表失败！");
		}
		int result = dao.modInspPayinfo(sjxxDto);
		if(result == 0)
			log.error("送检信息同步失败！");
	}

	@Override
	public List<UserDto> queryYhByGwmc(UserDto userDto) {
		// TODO Auto-generated method stub
		return dao.queryYhByGwmc(userDto);
	}
	/**
	 * 根据角色名称查询用户
	 * @param userDto
	 * @return
	 */
	@Override
	public List<UserDto> queryYhByJsmc(UserDto userDto) {
		// TODO Auto-generated method stub
		return dao.queryYhByJsmc(userDto);
	}
	/**
	 * @author lifan
	 * 发送MQ消息个数提醒(定时任务)
	 * 对mq.tran.basic.ok和 mq.tran.basic.w2p 进行监控，当个数超过5个，发送钉钉和微信提醒
	 */
	public void sendMQMessageCount() {
		String okQueue = MQTypeEnum.CONTRACE_BASIC_OK.getCode();
		String w2pQueue = MQTypeEnum.CONTRACE_BASIC_W2P.getCode();//doc2pdf_exchange//topic
		MQQueueModel queue1 = new MQQueueModel();
		queue1.setQueueName(okQueue);
		MQQueueModel queue2 = new MQQueueModel();
		queue2.setQueueName(w2pQueue);
		MQQueueModel queue3 = new MQQueueModel();
		queue3.setQueueName("mq.tran.basic.ok.");
		List<MQQueueModel> queueList = new ArrayList<>();
		queueList.add(queue1);
		queueList.add(queue2);
		queueList.add(queue3);
		ddxxglService.sendMQMessageCount(queueList);
	}

	/**
	 * 检测实验日期是否为空
	 * @param sjxxDto
	 * @return
	 */
	public Map<String,Object> checkSyrq(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		if("1".equals(sjxxDto.getJcbj()) && StringUtils.isBlank(sjxxDto.getSyrq())) {
			map.put("status", "fail");
			map.put("message", "RNA实验日期不能为空！");
		}else if("1".equals(sjxxDto.getDjcbj()) && StringUtils.isBlank(sjxxDto.getDsyrq())) {
			map.put("status", "fail");
			map.put("message", "DNA实验日期不能为空！");
		}else if("1".equals(sjxxDto.getQtjcbj()) && StringUtils.isBlank(sjxxDto.getQtsyrq())) {
			map.put("status", "fail");
			map.put("message", "其他实验日期不能为空！");
		}else if("0".equals(sjxxDto.getJcbj()) && "0".equals(sjxxDto.getDjcbj())
				&& "0".equals(sjxxDto.getQtjcbj())) {
			map.put("status", "fail");
			map.put("message", "检查类型必须要选择一个！");
		}
		return map;
	}

	/**
	 * 根据检测项目查询检测子项目
	 * @param jcxmid
	 * @return
	 */
	@Override
	public List<JcsjDto> getSubDetect(String jcxmid) {
		// TODO Auto-generated method stub
		List<JcsjDto> jcsjDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(jcxmid)) {
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setFcsid(jcxmid);
			jcsjDto.setJclb(BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			jcsjDtos = jcsjService.getListByFid(jcsjDto);
		}
		return jcsjDtos;
	}

	/**
	 * 清空检测子项目
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean emptySubDetect(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(sjxxDto.getSjid())) {
			int result = dao.emptySubDetect(sjxxDto);
			return result > 0;
		}
		return false;
	}

	/**
	 * 查询所有伙伴
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getAllSjhb(SjxxDto sjxxDto){
		return dao.getAllSjhb(sjxxDto);
	}

	/**
	 * 查询当日标本编号和复检原因
	 * @return
	 */
	public List<SjxxDto> getYbbhAndYyToday(){
		return dao.getYbbhAndYyToday();
	}

	/**
	 * 清单发送给商务部
	 * @return
	 */
	public boolean sendSjDetailedList(){
		List<JcsjDto> dtos = redisUtil.hmgetDto("matridx_jcsj:INSPECTION_DIVISION");
		String csid=null;
		for(JcsjDto dto:dtos){
			if("INSPECT".equals(dto.getCsdm())){
				csid=dto.getCsid();
			}
		}
		List<SjxxDto> list = getDtoBySf(csid);
		List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.UNCOLLECTED_TYPE.getCode());
		String ICOMM_SJ00063 = xxglService.getMsg("ICOMM_SJ00063");
		String ICOMM_SJ00062 = xxglService.getMsg("ICOMM_SJ00062");
		String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/ddUncollected/ddUncollected&csid="+csid,StandardCharsets.UTF_8);
		//访问链接
		List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
		OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
		btnJsonList.setTitle("小程序");
		btnJsonList.setActionUrl(internalbtn);
		btnJsonLists.add(btnJsonList);

		if (ddxxglDtolist != null && ddxxglDtolist.size() > 0) {
			for (int i = 0; i < ddxxglDtolist.size(); i++) {
				if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid())) {
					talkUtil.sendCardMessage(ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(), ICOMM_SJ00063,
							StringUtil.replaceMsg(ICOMM_SJ00062, String.valueOf(list.size())), btnJsonLists,
							"1");
					}
				}
			}
		return true;
	}
	/**
	 * 清单发送给医学代表
	 * @return
	 */
	public boolean sendSjDetailsToYxdb(){
		SjxxDto sjxxDto=new SjxxDto();
		List<String> dbgws = new ArrayList<>();
		dbgws.add("医学代表");
		sjxxDto.setDbgws(dbgws);
		List<JcsjDto> dtos = redisUtil.hmgetDto("matridx_jcsj:INSPECTION_DIVISION");
		String csid=null;
		for(JcsjDto dto:dtos){
			if("INSPECT".equals(dto.getCsdm())){
				csid=dto.getCsid();
			}
		}
		sjxxDto.setCsid(csid);
		List<SjxxDto> list = getCountByYxdb(sjxxDto);
		String ICOMM_SJ00063 = xxglService.getMsg("ICOMM_SJ00063");
		String ICOMM_SJ00062 = xxglService.getMsg("ICOMM_SJ00062");
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (StringUtil.isNotBlank(list.get(i).getDdid())) {
					String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/inspection/yxdbDdUncollectedView?ddid="+list.get(i).getDdid()+"-"+csid;
					// 外网访问
					//String external = externalurl + "/common/view/displayView?view_url=/ws/inspection/yxdbDdUncollectedView?ddid="+list.get(i).getDdid();
					List<BtnJsonList> btnJsonLists = new ArrayList<>();
					BtnJsonList btnJsonList = new BtnJsonList();
					btnJsonList.setTitle("详细情况");
					btnJsonList.setActionUrl(internalbtn);
					btnJsonLists.add(btnJsonList);
					talkUtil.sendCardMessage(list.get(i).getYhm(), list.get(i).getDdid(), ICOMM_SJ00063,
							StringUtil.replaceMsg(ICOMM_SJ00062, String.valueOf(list.get(i).getRs())), btnJsonLists,
							"1");
				}
			}
		}
		return true;
	}
	/**
	 * 清单发送给大区经理
	 * @return
	 */
	public boolean sendSjDetailsToDqjl(){
		SjxxDto sjxxDto=new SjxxDto();
		List<String> dbgws = new ArrayList<>();
		dbgws.add("大区经理");
		dbgws.add("地区经理");
		sjxxDto.setDbgws(dbgws);
		List<JcsjDto> dtos = redisUtil.hmgetDto("matridx_jcsj:INSPECTION_DIVISION");
		String csid=null;
		for(JcsjDto dto:dtos){
			if("INSPECT".equals(dto.getCsdm())){
				csid=dto.getCsid();
			}
		}
		sjxxDto.setCsid(csid);
		List<SjxxDto> list = getCountByDqjl(sjxxDto);
		String ICOMM_SJ00063 = xxglService.getMsg("ICOMM_SJ00063");
		String ICOMM_SJ00062 = xxglService.getMsg("ICOMM_SJ00062");
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (StringUtil.isNotBlank(list.get(i).getDdid())) {
					String internalbtn = applicationurl + "/common/view/displayView?view_url=/ws/inspection/dqjlDdUncollectedView?ddid="+list.get(i).getDdid()+"-"+csid;
					// 外网访问
					//String external = externalurl + "/common/view/displayView?view_url=/ws/inspection/dqjlDdUncollectedView?ddid="+list.get(i).getDdid();
					List<BtnJsonList> btnJsonLists = new ArrayList<>();
					BtnJsonList btnJsonList = new BtnJsonList();
					btnJsonList.setTitle("详细情况");
					btnJsonList.setActionUrl(internalbtn);
					btnJsonLists.add(btnJsonList);
					talkUtil.sendCardMessage(list.get(i).getYhm(), list.get(i).getDdid(), ICOMM_SJ00063,
							StringUtil.replaceMsg(ICOMM_SJ00062, String.valueOf(list.get(i).getRs())), btnJsonLists,
							"1");
				}
			}
		}
		return true;
	}


	/**
	 * 提交送检验证审核
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,Object> sendPCRAudit(SjxxDto sjxxDto){
		Map<String, Object> result = new HashMap<>();
		result.put("status","success");
		result.put("message","保存成功！");
		//注释原因:不再自动发起送检验证   2023/1/10  姚嘉伟
//		SjxxDto t_sjxxDto = getDtoById(sjxxDto);
		//若内部编码不为空
//		if (StringUtil.isBlank(t_sjxxDto.getNbbm())) {
//			result.put("message","保存成功！");
//			return result;
//		}else {
//			String nbbm = t_sjxxDto.getNbbm();
//			String lastNbbm = nbbm.substring(nbbm.length() - 1);
//			//若内部编码最后一位为B或b,则不发送送检验证，提示保存成功
//			if("B".equals(lastNbbm) || "b".equals(lastNbbm)){
//				return result;
//			}
//		}
		//若从收样确认调用，则查询部分数据并set到sjxxDto中
//		if (sjxxDto.getBys() == null || sjxxDto.getBys().size() == 0) {
//			Map<String, String> allSjxxOther = dao.getAllSjxxOther(sjxxDto.getSjid());
//			if (StringUtil.isNotBlank(allSjxxOther.get("gzbyid"))){
//				String[] bys = allSjxxOther.get("gzbyid").split(",");
//				List<String> byList = new ArrayList<String>();
//				for (String by : bys) {
//					byList.add(by);
//				}
//				sjxxDto.setBys(byList);
//			} else {
//				return result;
//			}
//			if (StringUtil.isBlank(sjxxDto.getJcdw())){
//				sjxxDto.setJcdw(t_sjxxDto.getJcdw());
//			}
//			if (StringUtil.isBlank(sjxxDto.getJcxm())){
//				sjxxDto.setJcxm(allSjxxOther.get("jcxm"));
//			}
//		}
		//设置提交人员为公用人员
//		User user = commonService.getDtoByYhm("MGY");
		//从redis中获取关注病原的基础数据
//		List<JcsjDto> matridx_jcsj_pathogeny_type = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode());
//		List<JcsjDto> dtoList = new ArrayList<>();//cskz1为空的病原
//		List<JcsjDto> qt_bys = new ArrayList<>();//cskz1不为空的病原
//		//去掉关注病原中 scbj不为0且参数扩展1不为空的,并放入dtoList中(cskz1 ！= 1 即 非其他病原)
//		for (JcsjDto dto : matridx_jcsj_pathogeny_type) {
//			if ("0".equals(dto.getScbj()) && StringUtil.isBlank(dto.getCskz1())){
//				dtoList.add(dto);
//			}
//			if (StringUtil.isNotBlank(dto.getCskz1())){
//				qt_bys.add(dto);
//			}
//		}
//		//从 bys中去除关注病元的扩展参数不为空的病元
//		if (qt_bys.size()>0) {
//			for (JcsjDto qt_by : qt_bys) {
//				for (int i = sjxxDto.getBys().size()-1; i >=0; i--) {
//					if (qt_by.getCsid().equals(sjxxDto.getBys().get(i))) {
//						sjxxDto.getBys().remove(i);
//					}
//				}
//			}
//		}
//		//若除参数扩展1不为空的关注病院被全选了，则不进行后面的操作（即不发送验证审核）
//		if(sjxxDto.getBys().size() >= dtoList.size()){
//			return result;
//		}
//		//若检测项目cskz1不为D,R,C,P则不发送验证审核
//		SjjcxmDto sjjcxmDto=new SjjcxmDto();
//		sjjcxmDto.setSjid(sjxxDto.getSjid());
//		List<SjjcxmDto> sjjcxmDtos=sjjcxmService.getDtoList(sjjcxmDto);
//		if(sjjcxmDtos!=null && sjjcxmDtos.size()>0){
//			if(!"DRCP".contains(sjjcxmDtos.get(0).getCskz1())){
//				return result;
//			}
//		}

//		List<String> tsbys = new ArrayList<>();
//		Map<String,String> yzlbs = new HashMap<String,String>();
//		//获取特殊病原的基础数据
//		for (JcsjDto dto : matridx_jcsj_pathogeny_type) {
//			if ("0007".equals(dto.getCsdm())){
//				tsbys.add(dto.getCsid());
//				yzlbs.put(dto.getCsid(),dto.getCskz3());
//			}
//			if ("0008".equals(dto.getCsdm())){
//				tsbys.add(dto.getCsid());
//				yzlbs.put(dto.getCsid(),dto.getCskz3());
//			}
//		}
//		boolean sendFlag = true;
//		//获取修改后保存的特殊病原
//		List<String> xtsbys = new ArrayList<>();//现特殊病原
//		if (sjxxDto.getBys().size()>0 && tsbys.size()>0){
//			for (String tsby : tsbys) {
//				for (String by : sjxxDto.getBys()) {
//					if (by.equals(tsby)){
//						xtsbys.add(tsby);
//					}
//				}
//			}
//		}
		//若无原关注病原 说明是从收样确认调用
//		if (sjxxDto.getYgzbys() != null && sjxxDto.getYgzbys().size() > 0) {
//			//获取修改前的特殊病原
//			List<String> ytsbys = new ArrayList<>();//原特殊病原
//			if (sjxxDto.getYgzbys().size()>0 && tsbys.size()>0){
//				for (String tsby : tsbys) {
//					for (String ygzby : sjxxDto.getYgzbys()) {
//						if (ygzby.equals(tsby)){
//							ytsbys.add(tsby);
//						}
//					}
//				}
//			}
//			//判断特殊病原是否有修改，
//			int send = 0;
//			if (ytsbys.size()==xtsbys.size()){
//				for (String ytsby : ytsbys) {
//					for (String xtsby : xtsbys) {
//						if (ytsby.equals(xtsby)){
//							send++;
//						}
//					}
//				}
//			}
//			if (send == xtsbys.size()){
//				sendFlag = false;
//			}
//		}

		//若有修改，发送验证
//		if (xtsbys.size()>0 && sendFlag){
//			result.put("message","提交成功！");
//			log.error("-----------准备执行提交送检验证审核线程------------");
//			SendPCRAudit sendPCRAudit = new SendPCRAudit();
//			sendPCRAudit.init(xtsbys,sjxxDto,redisUtil,yzlbs,sjyzService,shgcService,user);
//			SendPCRAuditThread sendPCRAuditThread = new SendPCRAuditThread(sendPCRAudit);
//			sendPCRAuditThread.start();
//		}
		return result;
	}

	/**
	 * 根据样本编号模糊查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getDtoVague(SjxxDto sjxxDto){
		if (StringUtil.isBlank(sjxxDto.getSjid()) && StringUtil.isBlank(sjxxDto.getYbbh())&&StringUtil.isBlank(sjxxDto.getNbbm()))
			return null;
		// 获取送检信息
		List<SjxxDto> resSjxxDtos = dao.getDtoVague(sjxxDto);
		if(resSjxxDtos!=null && resSjxxDtos.size()>0){
			SjxxDto resSjxxDto=resSjxxDtos.get(0);
			if (resSjxxDto != null)
			{
				if (resSjxxDto.getNldw() == null || resSjxxDto.getNldw() == "")
				{
					resSjxxDto.setNldw("岁");
				}
				if (resSjxxDto.getQtks() != null && resSjxxDto.getQtks() != "")
				{
					resSjxxDto.setKsmc(resSjxxDto.getKsmc() + "-" + resSjxxDto.getQtks());
				}
				Map<String, String> sjxxMap = dao.getAllSjxxOther(resSjxxDto.getSjid());
				if (sjxxMap.get("jcxmmc") != null)
				{
					resSjxxDto.setJcxmmc(sjxxMap.get("jcxmmc"));
				}
				if (sjxxMap.get("cskz1") != null)
				{
					resSjxxDto.setCskz1(sjxxMap.get("cskz1"));
				}
				if (sjxxMap.get("cskz3") != null)
				{
					resSjxxDto.setCskz3(sjxxMap.get("cskz3"));
				}
				if (sjxxMap.get("cskz3s") != null)
				{
					resSjxxDto.setCskz3s(sjxxMap.get("cskz3s"));
				}
				if (sjxxMap.get("jcxm") != null)
				{
					resSjxxDto.setJcxm(sjxxMap.get("jcxm"));
				}
				if (sjxxMap.get("lczz") != null)
				{
					resSjxxDto.setLczzmc(sjxxMap.get("lczz"));
				}
				if (sjxxMap.get("gzbymc") != null)
				{
					resSjxxDto.setGzbymc(sjxxMap.get("gzbymc"));
				}
				if (sjxxMap.get("gzbymc") != null)
				{
					resSjxxDto.setGzbymc(sjxxMap.get("gzbymc"));
				}
				if (sjxxMap.get("qqjcmc") != null)
				{
					resSjxxDto.setQqjcmc(sjxxMap.get("qqjcmc"));
				}
				if (sjxxMap.get("ybztmc") != null)
				{
					resSjxxDto.setYbztmc(sjxxMap.get("ybztmc"));
				}
			}
			return resSjxxDto;
		}else{
			return null;
		}
	}
	/**
	 * 查找接收日期为空的可废弃数据
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<String> getJsrqIsNull(SjxxDto sjxxDto) {
		return dao.getJsrqIsNull(sjxxDto);
	}


	/**
	 * 更新送检信息
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateModel(SjxxDto sjxxDto){
		int result=dao.updateModel(sjxxDto);
		return result > 0;
	}

	/**
	 * 获取样本编号对应检测项目以及合作伙伴的参数扩展3
	 * @param ybbh
	 * @return
	 */
	public SjxxDto getSjxxGlCskz3(String ybbh){
		return dao.getSjxxGlCskz3(ybbh);
	}
	/**
	 * 通过检测单位限制查找数据
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getInfo(SjxxDto sjxxDto){
		SjxxDto resSjxxDto = dao.getInfo(sjxxDto);
		if (resSjxxDto != null)
		{
			if (resSjxxDto.getNldw() == null || resSjxxDto.getNldw() == "")
			{
				resSjxxDto.setNldw("岁");
			}
			if (resSjxxDto.getQtks() != null && resSjxxDto.getQtks() != "")
			{
				resSjxxDto.setKsmc(resSjxxDto.getKsmc() + "-" + resSjxxDto.getQtks());
			}
			Map<String, String> sjxxMap = dao.getAllSjxxOther(resSjxxDto.getSjid());
			if (sjxxMap.get("jcxmmc") != null)
			{
				resSjxxDto.setJcxmmc(sjxxMap.get("jcxmmc"));
			}
			if (sjxxMap.get("cskz1") != null)
			{
				resSjxxDto.setCskz1(sjxxMap.get("cskz1"));
			}
			if (sjxxMap.get("cskz3") != null)
			{
				resSjxxDto.setCskz3(sjxxMap.get("cskz3"));
			}
			if (sjxxMap.get("jcxm") != null)
			{
				resSjxxDto.setJcxm(sjxxMap.get("jcxm"));
			}
			if (sjxxMap.get("lczz") != null)
			{
				resSjxxDto.setLczzmc(sjxxMap.get("lczz"));
			}
			if (sjxxMap.get("gzbymc") != null)
			{
				resSjxxDto.setGzbymc(sjxxMap.get("gzbymc"));
			}
			if (sjxxMap.get("gzbymc") != null)
			{
				resSjxxDto.setGzbymc(sjxxMap.get("gzbymc"));
			}
			if (sjxxMap.get("qqjcmc") != null)
			{
				resSjxxDto.setQqjcmc(sjxxMap.get("qqjcmc"));
			}
			if (sjxxMap.get("ybztmc") != null)
			{
				resSjxxDto.setYbztmc(sjxxMap.get("ybztmc"));
			}
		}
		return resSjxxDto;
	}


	/**
	 * 对外接口接口调用方法(新线程)
	 */
	public void matchingUtilNewRun(Map<String,Object> map,String method){
		MatchingUtil matchingUtil = new MatchingUtil();
		RestTemplate restTemplate = new RestTemplate();
		matchingUtil.init(map, redisUtil, restTemplate, amqpTempl,this,sjjcxmService,sjsyglService, yyxxService,talkUtil,
				ddxxglService,sjhbxxService,xxdyService,sjkzxxService,xmsyglService,fjsqService,jkdymxService,wbsjxxService,sjgzbyService,
                hbsfbzService,sjnyxService,sjdlxxService,sjxxjgService,interfaceExceptionUtil,nyypxxService);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Method m = matchingUtil.getClass().getMethod(method);
					m.invoke(matchingUtil);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();

	}

	/**
	 * 定时任务调用杏和接口查询数据 5.0
	 * @throws Exception
	 */
	public void timedCallXinghe(Map<String,Object> codeMap){
//		code = "hospitalids=33A006,33A007;dateStartNum=-5;dateEndNum=0;startTime=08:00;endTime=18:00;";
		Object hospitalIdsObj = codeMap.get("hospitalIds");
		String hospitalIds = "";
		if(hospitalIdsObj!=null){
			hospitalIds = hospitalIdsObj.toString();
		}else {
			log.error("医院id：hospitalIds为空！");
		}
		String[] hospitals = hospitalIds.split(";");
		for (String hospital : hospitals) {
			Map<String,Object> hospitalMap = new ConcurrentHashMap<>();
			hospitalMap.put("method","selectByDate");
			Object dateStartNum = codeMap.get("dateStartNum");
			if (dateStartNum!=null){
				String dateNum = dateStartNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, Integer.parseInt(dateNum));
				hospitalMap.put("startDate",sdf.format(cal.getTime()));
			}
			Object dateEndNum = codeMap.get("dateEndNum");
			if (dateEndNum!=null){
				String dateNum = dateEndNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, Integer.parseInt(dateNum));
				hospitalMap.put("endDate",sdf.format(cal.getTime()));
			}
			Object timeStartNum = codeMap.get("timeStartNum");
			if (timeStartNum!=null){
				String timeNum = timeStartNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, Integer.parseInt(timeNum));
				hospitalMap.put("startTime",sdf.format(cal.getTime()));
			}
			Object timeEndNum = codeMap.get("timeEndNum");
			if (timeEndNum!=null){
				String timeNum = timeEndNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, Integer.parseInt(timeNum));
				hospitalMap.put("endTime",sdf.format(cal.getTime()));
			}
			Object startDate = codeMap.get("startDate");
			if (startDate!=null){
				String date = startDate.toString();
				hospitalMap.put("startDate",date);
			}
			Object endDate = codeMap.get("endDate");
			if (endDate!=null){
				String date = endDate.toString();
				hospitalMap.put("endDate",date);
			}
			Object startTime = codeMap.get("startTime");
			if (startTime!=null){
				String time = startTime.toString();
				hospitalMap.put("startTime",time);
			}
			Object endTime = codeMap.get("endTime");
			if (endTime!=null){
				String time = endTime.toString();
				hospitalMap.put("endTime",time);
			}
			hospitalMap.put("hospitalId",hospital);
			Object json = codeMap.get("json");
			if (json!=null){
				hospitalMap.put("json",json);
			}
			try {
				hospitalMap.put("xinghe_wsdlurl",xinghe_wsdlurl);
				hospitalMap.put("xinghe_ticket",xinghe_ticket);
				hospitalMap.put("xinghe_hospitalservice",xinghe_hospitalservice);
				matchingUtilNewRun(hospitalMap,"xingheInfo");
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 定时任务调用杏和接口查询数据 - 杏和 6.0
	 * @throws Exception
	 */
	public void timedCallXingheNew(Map<String,Object> codeMap){
//		code = "hospitalids=33A006,33A007;dateStartNum=-5;dateEndNum=0;startTime=08:00;endTime=18:00;";
		Object hospitalIdsObj = codeMap.get("hospitalIds");
		String hospitalIds = "";
		if(hospitalIdsObj!=null){
			hospitalIds = hospitalIdsObj.toString();
		}else {
			log.error("医院id：hospitalIds为空！");
		}
		String[] hospitals = hospitalIds.split(";");
		for (String hospital : hospitals) {
			Map<String,Object> hospitalMap = new ConcurrentHashMap<>();
			hospitalMap.put("method","selectByDate");
			Object dateStartNum = codeMap.get("dateStartNum");
			if (dateStartNum!=null){
				String dateNum = dateStartNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, Integer.parseInt(dateNum));
				hospitalMap.put("startDate",sdf.format(cal.getTime()));
			}
			Object dateEndNum = codeMap.get("dateEndNum");
			if (dateEndNum!=null){
				String dateNum = dateEndNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, Integer.parseInt(dateNum));
				hospitalMap.put("endDate",sdf.format(cal.getTime()));
			}
			Object timeStartNum = codeMap.get("timeStartNum");
			if (timeStartNum!=null){
				String timeNum = timeStartNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, Integer.parseInt(timeNum));
				hospitalMap.put("startTime",sdf.format(cal.getTime()));
			}
			Object timeEndNum = codeMap.get("timeEndNum");
			if (timeEndNum!=null){
				String timeNum = timeEndNum.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR, Integer.parseInt(timeNum));
				hospitalMap.put("endTime",sdf.format(cal.getTime()));
			}
			Object startDate = codeMap.get("startDate");
			if (startDate!=null){
				String date = startDate.toString();
				hospitalMap.put("startDate",date);
			}
			Object endDate = codeMap.get("endDate");
			if (endDate!=null){
				String date = endDate.toString();
				hospitalMap.put("endDate",date);
			}
			Object startTime = codeMap.get("startTime");
			if (startTime!=null){
				String time = startTime.toString();
				hospitalMap.put("startTime",time);
			}
			Object endTime = codeMap.get("endTime");
			if (endTime!=null){
				String time = endTime.toString();
				hospitalMap.put("endTime",time);
			}
			hospitalMap.put("hospitalId",hospital);
			Object json = codeMap.get("json");
			if (json!=null){
				hospitalMap.put("json",json);
			}
			try {
				hospitalMap.put("xinghe_newurl",xinghe_newurl);
				//hospitalMap.put("xinghe_ticket",xinghe_ticket);
				hospitalMap.put("xinghe_ak",xinghe_ak);
				hospitalMap.put("xinghe_sk",xinghe_sk);
				hospitalMap.put("xinghe_hospitalservice",xinghe_hospitalservice);
				matchingUtilNewRun(hospitalMap,"xingheInfoNew");
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 定时任务调用千麦接口查询数据
	 * @param codeMap
	 * @throws Exception
	 */
	public void timedCallQianmai(Map<String,Object> codeMap){
		codeMap.put("method","selectByDate");
		codeMap.put("QIANMAI_URL",QIANMAI_URL);
		codeMap.put("QIANMAI_USERCODE",QIANMAI_USERCODE);
		codeMap.put("QIANMAI_PASSWORD",QIANMAI_PASSWORD);
		codeMap.put("QIANMAI_EntrustHosCode",QIANMAI_EntrustHosCode);
		try {
			matchingUtilNewRun(codeMap,"qianmaiInfo");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 定时任务调用和诺接口查询数据
	 * @param codeMap
	 * @throws Exception
	 */
	public void timedCallHenuo(Map<String,Object> codeMap){
		codeMap.put("method","selectByDate");
		codeMap.put("HENUO_URL",HENUO_URL);
		codeMap.put("HENUO_ACCOUNT",HENUO_ACCOUNT);
		codeMap.put("HENUO_PASSWORD",HENUO_PASSWORD);
		codeMap.put("HENUO_USERTYPE",HENUO_USERTYPE);
		codeMap.put("HENUO_EntrustHosCode",HENUO_EntrustHosCode);
		XtszDto xtszDto = xtszService.selectById("henuo.portconfig");
		if(xtszDto!=null){
			codeMap.put("HENUO_PORTCONFIG",xtszDto.getSzz());
		}
		try {
			matchingUtilNewRun(codeMap,"henuoInfo");
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * 修改保存时根据确认状态发送消息
	 *
	 * @return
	 */
	public boolean sendMessageByConfirmStatus(SjxxDto sjxxDto,User user) throws BusinessException {
		SjxxDto t_sjxxDto = getDto(sjxxDto);
		SjybztDto sjybztDto = new SjybztDto();
		sjybztDto.setSjid(sjxxDto.getSjid());
		List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
		for (SjybztDto sjybztDto_t : sjybztDtos) {
			// if ( (StringUtil.isNotBlank(sjybztDto_t.getYbztCskz1()) && StringUtil.isNotBlank(sjybztDto_t.getYbztCskz2())) && ("B".equals(sjybztDto_t.getYbztCskz1()) || "S".equals(sjybztDto_t.getYbztCskz1()) || "L".equals(sjybztDto_t.getYbztCskz1()) || "N".equals(sjybztDto_t.getYbztCskz1())) ) {
			if(StringUtil.isNotBlank(sjybztDto_t.getYbztCskz1()) && StringUtil.isNotBlank(sjybztDto_t.getYbztCskz2())) {
				SjxxDto messageSjxx = new SjxxDto();
				messageSjxx.setSjid(t_sjxxDto.getSjid());
				messageSjxx.setHzxm(t_sjxxDto.getHzxm());
				messageSjxx.setYblxmc(t_sjxxDto.getYblxmc());
				messageSjxx.setYbzt_flg(sjybztDto_t.getYbztCskz1());
				messageSjxx.setYbztmc(sjybztDto_t.getCsmc());
				messageSjxx.setYbzt_cskz2(sjybztDto_t.getYbztCskz2());//这里取到标本状态的扩展参数2，比如ICOMM_1001
				sendConfirmMessage(messageSjxx);
			}
		}
		return true;
	}

	/**
	 * 查询有word报告的送检信息
	 * @param params
	 * @return
	 */
	public List<SjxxDto> selectReportInfo(Map<String, Object> params){
		return dao.selectReportInfo(params);
	}

	/**
	 * 修改特殊申请
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateTssq(SjxxDto sjxxDto){
		return dao.updateTssq(sjxxDto);
	}

	/**
	 * 根据样本编号查询送检基本信息
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getSjxxInfoByYbbh(SjxxDto sjxxDto){
		return dao.getSjxxInfoByYbbh(sjxxDto);
	}
	/**
	 * 从Dto里把数据放到Map里，减少Dto的属性设置，防止JSON出错
	 * @param sjxxDto
	 * @return
	 */
	public Map<String,Object> pareMapFromDto(SjxxDto sjxxDto){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("jcxms", sjxxDto.getJcxms());
		paramMap.put("hzxm", sjxxDto.getHzxm());
		paramMap.put("nl", sjxxDto.getNl());
		paramMap.put("xb", sjxxDto.getXb());
		paramMap.put("hospitalname", sjxxDto.getHospitalname());
		paramMap.put("db", sjxxDto.getDb());
		paramMap.put("yblx", sjxxDto.getYblx());
		paramMap.put("yblxmc", sjxxDto.getYblxmc());
		paramMap.put("zyh", sjxxDto.getZyh());
		paramMap.put("ybbh", sjxxDto.getYbbh());
		paramMap.put("kdh", sjxxDto.getKdh());
		paramMap.put("nbbm", sjxxDto.getNbbm());
		paramMap.put("cskz5", sjxxDto.getCskz5());
		paramMap.put("qyrqstart", sjxxDto.getQyrqstart());
		paramMap.put("qyrqend", sjxxDto.getQyrqend());
		paramMap.put("ydrqstart", sjxxDto.getYdrqstart());
		paramMap.put("ydrqend", sjxxDto.getYdrqend());
		paramMap.put("jsrqstart", sjxxDto.getJsrqstart());
		paramMap.put("jsrqend", sjxxDto.getJsrqend());
		paramMap.put("bgrqstart",sjxxDto.getBgrqstart());
		paramMap.put("bgrqend",sjxxDto.getBgrqend());
		paramMap.put("syrqstart",sjxxDto.getSyrqstart());
		paramMap.put("syrqflg",sjxxDto.getSyrqflg());
		paramMap.put("syrqend",sjxxDto.getSyrqend());
		paramMap.put("fkrqstart",sjxxDto.getFkrqstart());
		paramMap.put("fkrqend",sjxxDto.getFkrqend());
		paramMap.put("zsxm",sjxxDto.getZsxm());
		paramMap.put("lcfk",sjxxDto.getLcfk());
		paramMap.put("sfzq",sjxxDto.getSfzq());
		paramMap.put("dwids",sjxxDto.getDwids());
		paramMap.put("kdlxs",sjxxDto.getKdlxs());
		paramMap.put("kyxms",sjxxDto.getKyxms());
		paramMap.put("kyxm",sjxxDto.getKyxm());
		paramMap.put("kylxs",sjxxDto.getKylxs());
		paramMap.put("gzlxs",sjxxDto.getGzlxs());
		paramMap.put("sjkzcs1",sjxxDto.getSjkzcs1());
		paramMap.put("sjkzcs2",sjxxDto.getSjkzcs2());
		paramMap.put("sjkzcs3",sjxxDto.getSjkzcs3());
		paramMap.put("sjkzcs4",sjxxDto.getSjkzcs4());
		paramMap.put("jyjgs",sjxxDto.getJyjgs());
		paramMap.put("fkbj",sjxxDto.getFkbj());
		paramMap.put("sfjs",sjxxDto.getSfjs());
		paramMap.put("sftj",sjxxDto.getSftj());
		paramMap.put("sfsfs",sjxxDto.getSfsfs());
		paramMap.put("yblxs",sjxxDto.getYblxs());
		paramMap.put("jcdws",sjxxDto.getJcdws());
		paramMap.put("sjqfs",sjxxDto.getSjqfs());
		paramMap.put("dbm",sjxxDto.getDbm());
		paramMap.put("sfsc",sjxxDto.getSfsc());
		paramMap.put("sfzmjc",sjxxDto.getSfzmjc());
		paramMap.put("ids",sjxxDto.getIds());
		paramMap.put("bglxbj",sjxxDto.getBglxbj());
		paramMap.put("dbs",sjxxDto.getDbs());
		paramMap.put("yyzddjs",sjxxDto.getYyzddjs());
		paramMap.put("jcxmids",sjxxDto.getJcxmids());
		paramMap.put("jcxmdms",sjxxDto.getJcxmdms());
		paramMap.put("ptgss",sjxxDto.getPtgss());
		paramMap.put("xsbms",sjxxDto.getXsbms());
		paramMap.put("userids",sjxxDto.getUserids());
		paramMap.put("sjhbs",sjxxDto.getSjhbs());
		paramMap.put("all_param",sjxxDto.getAll_param());
		paramMap.put("dwjc",sjxxDto.getDwjc());
		paramMap.put("jcdwxz",sjxxDto.getJcdwxz());
		paramMap.put("jcbj",sjxxDto.getJcbj());
		paramMap.put("djcbj",sjxxDto.getDjcbj());
		paramMap.put("qtjcbj",sjxxDto.getQtjcbj());

		paramMap.put("sortName",sjxxDto.getSortName());
		paramMap.put("sortOrder",sjxxDto.getSortOrder());
		paramMap.put("sortLastName",sjxxDto.getSortLastName());
		paramMap.put("sortLastOrder",sjxxDto.getSortLastOrder());
		paramMap.put("pageSize",sjxxDto.getPageSize());
		paramMap.put("pageNumber",sjxxDto.getPageNumber());
//		paramMap.put("pageStart",(sjxxDto.getPageNumber() -1 ) * sjxxDto.getPageSize());
		paramMap.put("sjys",sjxxDto.getSjys());
		paramMap.put("jcjg",sjxxDto.getJcjg());
		paramMap.put("sfjc",sjxxDto.getSfjc());
		paramMap.put("pageStart",sjxxDto.getPageStart());
		//处理检测类型
		String jclx=sjxxDto.getJclx();
		String jczlx=sjxxDto.getJczlx();
		List<String> jclxList=new ArrayList<>();
		if(StringUtil.isNotBlank(jclx)){
			String[] jclxs=jclx.split(",");
			for(String jclx_t:jclxs){
				if(StringUtil.isNotBlank(jclx_t))
					jclxList.add(jclx_t);
			}
		}
		paramMap.put("jclxs",jclxList);

		List<String> jczlxList=new ArrayList<>();
		if(StringUtil.isNotBlank(jczlx)){
			String[] jczlxs=jczlx.split(",");
			for(String jczlx_t:jczlxs){
				if(StringUtil.isNotBlank(jczlx_t))
					jczlxList.add(jczlx_t);
			}
		}
		paramMap.put("jczlxs",jczlxList);
		return paramMap;
	}
	/*
	 * 获取实付总金额
	 * */
	@Override
	public SjxxDto getSfzjeAndTkzje(SjxxDto sjxxDto) {
		return dao.getSfzjeAndTkzje(sjxxDto);
	}

	/**
	 * 查询检测单位
	 * @param hbmc
	 * @return
	 */
	@Override
	public List<JcsjDto> getDetectionUnit(String hbmc) {
		// TODO Auto-generated method stub
		List<String> ids = null;
		if(StringUtil.isNotBlank(hbmc)) {
			// 查询伙伴权限信息
			ids = hbdwqxService.selectByHbmc(hbmc);
		}
		JcsjDto jcsjDto = new JcsjDto();
		// 查询检测项目
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jcsjDto.setIds(ids);
		List<JcsjDto> jcsjDtos = jcsjService.selectDetectionUnit(jcsjDto);
		if(jcsjDtos == null)
			jcsjDtos = new ArrayList<>();
		return jcsjDtos;
	}


	/**
	 * 获取十天内的芯片信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedDtoListDays(SjxxDto sjxxDto){
		return dao.getPagedDtoListDays(sjxxDto);
	}

	/**
	 * 样本进度列表
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedSampleProgressList(SjxxDto sjxxDto){
		return dao.getPagedSampleProgressList(sjxxDto);
	}

	@Override
	public List<SjxxDto> getPagedAuditDevice(SjxxDto sjxxDto) {
		SimpleDateFormat dayDeal = new SimpleDateFormat("yyyy-MM-dd");
		// 正常送检显示当前时间和前两天内的信息
		sjxxDto.setJsrq(dayDeal.format(DateUtils.getDate(new Date(), -7)));
		// 复检显示当前之间和前两天内的时间
		sjxxDto.setLrsj(dayDeal.format(DateUtils.getDate(new Date(), -7)));
		// 当天实验日期
		sjxxDto.setSyrq(dayDeal.format(DateUtils.getDate(new Date(),0)));
		// 获取人员ID和履历号
		List<Map<String,String>> sjxxDtos= sjxxTwoDao.getPagedAuditDevice(sjxxDto);
		if (sjxxDtos == null || sjxxDtos.size() == 0){
			List<SjxxDto> list=new ArrayList<>();
			return list;
		}
		List<SjxxDto> sjxxDtoList = sjxxTwoDao.getAuditListDevice(sjxxDtos);
		commonService.setSqrxm(sjxxDtoList);
		return sjxxDtoList;
	}


	/**
	 * 获取实验员列表头部数据
	 * @return
	 */
	@Override
	public Map<String, Object> querySjSyxxnums(SjxxDto sjxxDto) {
		return dao.querySjSyxxnums(sjxxDto);
	}


	/**
	 * 实验统计数量
	 * @return
	 */
	@Override
	public Map<String, Object> queryExperimentNums(SjxxDto sjxxDto){
		return sjxxTwoDao.queryExperimentNums(sjxxDto);
	}
	/**
	 * 查询所有送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getPagedExperimentDtoList(SjxxDto sjxxDto){
		return dao.getPagedExperimentDtoList(sjxxDto);
	}

	/**
	 * 已接受未实验数据
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getConfirmDtoList(SjxxDto sjxxDto){
		return dao.getConfirmDtoList(sjxxDto);
	}

	@Override
	public List<ResFirstDto> getPagedResFirstList(ResFirstDto resFirstDto) {
		return sjxxTwoDao.getPagedResFirstList(resFirstDto);
	}
	/**
	 * 更新取样信息
	 * @param sjxxDto
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateSampleInfo(SjxxDto sjxxDto){
		return dao.updateSampleInfo(sjxxDto);
	}

	/**
	 * 测试数统计信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> testNumberList(SjxxDto sjxxDto){
		return dao.testNumberList(sjxxDto);
	}

	/**
	 * 定时任务：测试数低于预警提醒
	 */
	public void testNumberRemind(Map<String,String> map) throws BusinessException {
		String jcdwdm = map.get("jcdwdm");
		if(StringUtil.isBlank(jcdwdm)){
			log.error("定时任务---测试数预警提醒---未传检测单位代码");
		}
		String cssl = map.get("cssl");
		if(StringUtil.isBlank(cssl)){
			log.error("定时任务---测试数预警提醒---未传最少测试数");
		}
		if(StringUtil.isNotBlank(jcdwdm)&&StringUtil.isNotBlank(cssl)){
			String DING_WARN001 = xxglService.getMsg("DING_WARN001");
			String DING_WARN002 = xxglService.getMsg("DING_WARN002");
			List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.TEST_NUMBER_WARNING.getCode());
			if(ddxxglDtolist!=null&&ddxxglDtolist.size()>0){
				List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
				jcdwdm=jcdwdm.replaceAll(";",",");
				SjxxDto sjxxDto=new SjxxDto();
				sjxxDto.setIds(jcdwdm);
				List<String> list=dao.testNumberStatistics(sjxxDto);
				if(list!=null&&list.size()>0){
					String[] strings=jcdwdm.split(",");
					for(String dm:strings){
						int count=0;
						String jcdwmc="";
						for(JcsjDto jcsjDto:jcdwList){
							if(dm.equals(jcsjDto.getCsdm())){
								jcdwmc=jcsjDto.getCsmc();
								break;
							}
						}
						for(String s:list){
							if(s.equals(dm)){
								count++;
							}
						}
						int sl=Integer.parseInt(cssl);
						if(count<=sl){
							for(DdxxglDto ddxxglDto :ddxxglDtolist) {//这里是遍历用户
								if(StringUtil.isNotBlank(ddxxglDto.getDdid())) {
									//===================组装访问路径============
									//访问路径 &：%26		=：%3D  ?:%3F
									String url = applicationurl + urlPrefix +  "/common/view/displayView?view_url=/ws/statistics/testNumberListView%3Fjcdwdm%3D"+dm+"%26load_flg%3D2";
									List<BtnJsonList> btnJsonLists = new ArrayList<>();
									BtnJsonList btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("详细");
									btnJsonList.setActionUrl(url);
									btnJsonLists.add(btnJsonList);
									//====================发送钉钉消息=================================
									talkUtil.sendCardMessage(ddxxglDto.getYhm(),
											ddxxglDto.getDdid(),
											DING_WARN001,
											StringUtil.replaceMsg(DING_WARN002, jcdwmc,String.valueOf(count),cssl, DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")),
											btnJsonLists,
											"1");
								}
							}
						}
					}
				}
			}else{
				log.error("定时任务---测试数预警提醒---未设置通知人员");
			}
		}
	}

	/**
	 * 根据送检ID和检测类型获取送检数据
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getSjxxDtosByYbbh(SjxxDto sjxxDto){
		return dao.getSjxxDtosByYbbh(sjxxDto);
	}
	/**
	 * 修改检测单位
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateJcdw(SjxxDto sjxxDto){
		return dao.updateJcdw(sjxxDto);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public Boolean saveProjectAmount(SjjcxmDto sjjcxmDto) throws BusinessException {
		if (StringUtil.isBlank(sjjcxmDto.getSjid()))
			throw new BusinessException("msg","未获取到送检信息！");
		List<SjjcxmDto> list=(List<SjjcxmDto>) JSON.parseArray(sjjcxmDto.getJson(), SjjcxmDto.class);
		if (CollectionUtils.isEmpty(list))
			throw new BusinessException("msg","未获取到送检项目信息！");
		for (SjjcxmDto dto : list) {
			dto.setXgry(sjjcxmDto.getXgry());
		}
		Boolean result = sjjcxmService.updateListNew(list);
		if (!result)
			throw new BusinessException("msg","维护项目数据失败！");
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setXgry(sjjcxmDto.getXgry());
		sjxxDto.setSjid(sjjcxmDto.getSjid());
		sjxxDto.setClbj("1");
		result =  dao.updateDto(sjxxDto)!=0;
		if (!result)
			throw new BusinessException("msg","更新处理标记失败！");
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.AMOU_MOD.getCode() + JSONObject.toJSONString(sjjcxmDto));
		return true;
	}
	/**
	 * 删除
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteDto(SjxxDto sjxxDto) {
		int result = dao.delete(sjxxDto);
		if(result == 0)
			return false;
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_DEL.getCode() + JSONObject.toJSONString(sjxxDto));
		SjkzxxDto sjkzxxDto=new SjkzxxDto();
		sjkzxxDto.setIds(sjxxDto.getIds());
		sjkzxxDto.setScry(sjxxDto.getScry());
		sjkzxxService.delete(sjkzxxDto);
		return true;
	}
	
	/**
	 * 查询极简送检信息，不关联其他表
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getSimpleDto(SjxxDto sjxxDto) {
		return dao.getSimpleDto(sjxxDto);
	}

	/**
	 * 更改是否接收，接收时间，接收人员为null
	 * @param sjxxDtoList
	 */
	@Override
	public void updateSfjsInfo(List<SjxxDto> sjxxDtoList) {
		dao.updateSfjsInfo(sjxxDtoList);
	}

	@Override
	public Map<String, String> getAllSjxxOther(String sjid) {
		return dao.getAllSjxxOther(sjid);
	}

	@Override
	public Map<String, String> getDtoMap(SjxxDto sjxxDto) {
		return dao.getDtoMap(sjxxDto);
	}

	@Override
	public List<Map<String, String>> getFsbgs(String xjsj) {

		Calendar calendar = Calendar.getInstance();
		SjxxDto sjxx = new SjxxDto();
		if(StringUtil.isBlank(xjsj)){
			calendar.setTime(new Date());
		}else{

			try {
				Date date=new Date();
				date.setTime(Long.valueOf(xjsj));
				calendar.setTime(date);
				SimpleDateFormat formatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String xjsjStr = formatAll.format(calendar.getTime());
				sjxx.setXjsj(xjsjStr);
			} catch (Exception e) {
				log.error("日期转换错误");
			}
		}
		Date nowDate = calendar.getTime();
		SimpleDateFormat formatHH = new SimpleDateFormat("yyyy-MM-dd HH");
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		String syrqEnd = formatHH.format(calendar.getTime());
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String syrq = format.format(calendar.getTime());
		String syrqStart = formatHH.format(calendar.getTime());
		String bgrq = format.format(nowDate);
		sjxx.setSyrq(syrq);
		sjxx.setDsyrq(syrq);
		sjxx.setSyrqstart(syrqStart);
		sjxx.setSyrqend(syrqEnd);
		sjxx.setBgrq(bgrq);

		//现在复检类型需要从基础数据查询 张晗
		//<!-- select>
		List<JcsjDto> flsqList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RECHECK.getCode());
		List<String> strList= new ArrayList<>();
		for (JcsjDto jcsjDto:flsqList) {
			//复测的才进行
			if("RECHECK".equals(jcsjDto.getCsdm())) {
				strList.add(jcsjDto.getCsid());
			}
		}

		sjxx.setFjlxs(strList);
		//临时定死 
		List<String> dbs = new ArrayList<String>();
		dbs.add("上海六院");
		dbs.add("安徽省立");
		dbs.add("艾迪康实验室");
		sjxx.setDbs(dbs);
		
		return dao.getFsbgs(sjxx);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean dealSaveInfo(SjxxDto sjxxDto,List<SjsyglDto> sjsyglDtos,String yhid) throws BusinessException {
		boolean isSuccess = dao.updateDealInfo(sjxxDto);
		if (!isSuccess){
			throw new BusinessException("送检信息更新失败！");
		}
		isSuccess = sjjcxmService.insertSjjcxm(sjxxDto, yhid);
		if (!isSuccess){
			throw new BusinessException("检测项目信息更新失败！");
		}
		//处理旧的项目实验管理数据
		XmsyglDto xmsyglDto = new XmsyglDto();
		xmsyglDto.setYwid(sjxxDto.getSjid());
		xmsyglDto.setScry(yhid);
		xmsyglService.deleteInfo(xmsyglDto);
		xmsyglService.delInfo(xmsyglDto);
		//处理旧的送检实验管理数据
		SjsyglDto sjsyglDto = new SjsyglDto();
		sjsyglDto.setSjid(sjxxDto.getSjid());
		sjsyglDto.setLx(DetectionTypeEnum.DETECT_SJ.getCode());
		sjsyglDto.setScry(yhid);
		sjsyglService.deleteInfo(sjsyglDto);
		sjsyglService.delInfo(sjsyglDto);
		if (!CollectionUtils.isEmpty(sjsyglDtos)){
			isSuccess = addOrUpdateSyData(sjsyglDtos, sjxxDto, yhid);
			if (isSuccess) {
				List<SjsyglModel> list = sjsyglService.getModelList(sjsyglDto);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(list));
				List<XmsyglModel> dtos = xmsyglService.getModelList(xmsyglDto);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.XMSY_MOD.getCode() + JSONObject.toJSONString(dtos));
			} else {
				throw new BusinessException("检测项目信息更新失败！");
			}
		}
		return isSuccess;
	}

	@Override
	public List<String> getTimeByTime(String xjsj) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		date.setTime(Long.valueOf(xjsj));
		String time=format.format(date);
		SjxxDto dto=new SjxxDto();
		dto.setXjsj(time);
		return dao.getTimeByTime(dto);
	}
	
	@Override
	public List<SjxxDto> getQdListByYbbh(SjxxDto sjxxDto) {
		return dao.getQdListByYbbh(sjxxDto);
	}


	public Map<String,Object> packageZipAndDownload(HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
		Map<String,Object> map=new HashMap<>();
		//解析上传文件获取报告
		String fjid=request.getParameter("fjid");
		Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + fjid);
		//如果文件信息不存在，则返回错误
		if (mFile == null){
			map.put("status","fail");
			map.put("message","未获取到上传文件!");
		}
		//文件名
		//String wjm = (String) mFile.get("wjm");
		//文件全路径
		String wjlj = (String) mFile.get("wjlj");
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(wjlj));
		int numberOfSheets = workbook.getNumberOfSheets();
		List<String> nbbmlist=new ArrayList<>();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet =workbook.getSheetAt(i);
			for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
				String value = sheet.getRow(0).getCell(j).toString();
				if (null != sheet.getRow(0).getCell(j)){
					if(!"内部编码".equals(value)){
						map.put("status","fail");
						map.put("message","模板错误，请检查模板!");
						return map;
					}
				}
			}
			for(int j = 1; j < sheet.getPhysicalNumberOfRows(); j++){
				nbbmlist.add(sheet.getRow(j).getCell(0).toString());
			}
		}
		if(!CollectionUtils.isEmpty(nbbmlist)){
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setNbbms(nbbmlist);
			sjxxDto.setBglxbj("allpdf");
			map=reportZipDownloadByNbbms(sjxxDto,request);
		}
		return map;
	}

	public Map<String,Object> exportExcel(FjcfbDto fjcfbDto){
		Map<String,Object> excelOut = new HashMap<String,Object>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DBEncrypt dbEncrypt = new DBEncrypt();
		String pathString = dbEncrypt.dCode(fjcfbDto.getWjlj());
		File pdfFile = new File(pathString); // 替换为你的PDF文件路径
		try (FileInputStream fis = new FileInputStream(pdfFile)) {
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

			excelOut.put("fileName",fjcfbDto.getWjm()+"_"+fjcfbDto.getYwlx());
			excelOut.put("outByte",out.toByteArray());
			// 这里你可以使用pdfBytes作为字节数组进行操作，例如保存到数据库或进行其他处理
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			// 不需要显式关闭ByteArrayOutputStream，因为它在垃圾回收时会自动释放资源
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return excelOut;
	}

	//多个文件压缩成压缩包并下载
	public void zipFiles(List<Map<String,Object>> fileList,HttpServletResponse httpResponse) {
		try(ZipOutputStream zipOutputStream = new ZipOutputStream(httpResponse.getOutputStream()); OutputStream out =null) {
			//下载压缩包
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode("报告下载.zip", "UTF-8"));
			// 创建 ZipEntry 对象
			for (Map<String,Object> map:fileList){
				if (StringUtil.isNotBlank((String) map.get("fileName"))){
					ZipEntry zipEntry =  new ZipEntry((String) map.get("fileName"));
					zipOutputStream.putNextEntry(zipEntry);
					zipOutputStream.write((byte[]) map.get("outByte"));
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}


	/**
	 *更新应付金额
	 * @param sjxxDtos
	 * @return
	 */
	public boolean updateFkje(List<SjxxDto> sjxxDtos){
		return dao.updateFkje(sjxxDtos);
	}


	/**
	 * 解析多层map为单层map
	 * @param map
	 * @param prefix
	 * @return
	 */
	public Map<String, Object> flatten(Map<String, Object> map, String prefix) {
		Map<String, Object> result = new HashMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = (prefix.isEmpty() ? "" : prefix + ".") + entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map) {
				// 如果value是Map类型，则递归解析
				result.putAll(flatten((Map<String, Object>) value, key));
			} else {
				// 如果value不是Map类型，直接存入结果map
				result.put(key, value);
			}
		}
		return result;
	}

	 /**
	 * 查找类似标本
	 * @param sjxxDto
	 * @return
	 */
	 public List<SjxxDto> getSimilarList(SjxxDto sjxxDto){
		 return dao.getSimilarList(sjxxDto);
	 }

	 /**
	 * 查找类似标本项目
	 * @param sjxxDto
	 * @return
	 */
	 public List<SjxxDto> getSimilarSjxxJcxmList(SjxxDto sjxxDto){
		 return dao.getSimilarSjxxJcxmList(sjxxDto);
	 }

	 public List<SjwzxxDto> selectWzxxBySjidAndJclx(SjxxDto sjxxDto){
		 return dao.selectWzxxBySjidAndJclx(sjxxDto);
	 }


	/**
	 * 定时删除附件：删除时间作为参数指定，路径也作为参数指定
	 */
	public void delDeadlineSamples(Map<String, String> map) {
		try {
			SjxxDto sjxxDto = new SjxxDto();
			String notDel = map.get("notDel");
			List<String> notDels = new ArrayList<>();
			if (StringUtil.isNotBlank(notDel)){
				notDels = Arrays.asList(notDel.split("@"));
			}
			sjxxDto.setYbbhs(notDels);
			String limitJcxm = map.get("limitJcxm");
			if (StringUtil.isNotBlank(limitJcxm) && limitJcxm.equals("true")){
				sjxxDto.setJcxmmc_flg("1");
			}
			String limitDb = map.get("limitDb");
			List<String> dbs = new ArrayList<>();
			if (StringUtil.isNotBlank(limitDb)){
				dbs = Arrays.asList(limitDb.split("@"));
			}
			sjxxDto.setDbs(dbs);
			String time = map.get("time");
			String dateNum = "-"+(StringUtil.isNotBlank(time) ? time : "365");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, Integer.parseInt(dateNum));
			sjxxDto.setLrsjEnd(sdf.format(cal.getTime()));
			List<String> sjids = dao.getDeadlineSamples(sjxxDto);
			if (!CollectionUtils.isEmpty(sjids)){
				SjxxDto delSjxx = new SjxxDto();
				delSjxx.setIds(sjids);
				delSjxx.setScry("timedTasks");
				deleteDto(delSjxx);
			}
		} catch (Exception e) {
			log.error("定时任务删除设定样本/n" + e.getMessage());
		}
	}


	/**
	 * 匹配检测项目
	 * @param sjxxDto
	 * @param jcxmList
	 */
	public JcsjDto compareJcxm(SjxxDto sjxxDto, JcsjDto yblxDto, List<JcsjDto> jcxmList){
		//1、优先匹配:("合作伙伴-"精确匹配)sjxx.qtxx.detectionType <=> 信息对应JCXMPP中yxx字段
		//2、次先匹配:("合作伙伴-"精确匹配)sjxx.jcxm <=> 信息对应JCXMDMPP中yxx字段
		//3、最后匹配:原匹配方法
		//1、优先匹配
		List<Map<String,Object>> list = new ArrayList<>();
		String qtxx = sjxxDto.getQtxx();
		if (StringUtil.isNotBlank(qtxx)){
			try {
				Map<String,Object> qtxxmap = JSONObject.parseObject(qtxx, new TypeReference<>() {});
				if(qtxxmap.get("detectionType")!=null){
					list.add(Map.of(
						"yxx",sjxxDto.getDb()+"-"+(String) qtxxmap.get("detectionType"),
						"dylx","JCXMPP"
					));
					list.add(Map.of(
						"yxx",(String) qtxxmap.get("detectionType"),
						"dylx","JCXMPP"
					));
					list.add(Map.of(
						"yxx",sjxxDto.getDb()+"-"+(String) qtxxmap.get("detectionType"),
						"dylx","JCZXMPP"
					));
					list.add(Map.of(
						"yxx",(String) qtxxmap.get("detectionType"),
						"dylx","JCZXMPP"
					));
				}
			} catch (Exception e) {
				log.error("compareJcxm:解析sjkzxxDto.getQtxx()失败",e);
			}
		}
		//2、次先匹配
		if (StringUtil.isNotBlank(sjxxDto.getJcxm())) {
			list.add(Map.of(
				"yxx",sjxxDto.getDb()+"-"+sjxxDto.getJcxm(),
				"dylx","JCXMDMPP"
			));
			list.add(Map.of(
				"yxx",sjxxDto.getJcxm(),
				"dylx","JCXMDMPP"
			));
			list.add(Map.of(
				"yxx",sjxxDto.getDb()+"-"+sjxxDto.getJcxm(),
				"dylx","JCZXMDMPP"
			));
			list.add(Map.of(
				"yxx",sjxxDto.getJcxm(),
				"dylx","JCZXMDMPP"
			));
		}
		if (!CollectionUtils.isEmpty(list)){
			List<JcsjDto> pps = xxdyService.getCompareListSortByList(list);
			if (!CollectionUtils.isEmpty(pps)){
 				return pps.get(0);
			}
		}
		//3、最后匹配:cskz1
		if (StringUtil.isNotBlank(sjxxDto.getJcxm())) {
			List<String> QList = List.of("D","R","C");
			String xmCskz3 = "";
			String xmCskz1 = sjxxDto.getJcxm();
			boolean isNeedYblxLimit = false;
			boolean isTCompare = true;//T项目是否匹配完成
			if (QList.contains(sjxxDto.getJcxm())){
				boolean isThirdProject = true;
				//1、判断送检伙伴是否存在于伙伴X限制表中,存在则说明被限制3.0项目(已删除匹配实验室代码)
				List<SjhbxxDto> hbxzDtos = sjxxCommonService.getHbxzDtoByHbmc(sjxxDto.getDb());
				if (hbxzDtos!=null && hbxzDtos.size()>0){
					isThirdProject = false;
				}
				//2、判断实验室是否支持3.0项目
				if (isThirdProject){
					//若系统设置中有thirdProject.jcdw配置，且有值
					Object xtsz_o = redisUtil.hget("matridx_xtsz", "thirdProject.jcdw");
					if (xtsz_o!=null){
						String xtsz_s = xtsz_o.toString();
						if (StringUtil.isNotBlank(xtsz_s)){
							XtszDto xtszDto = JSON.parseObject(xtsz_s, XtszDto.class);
							String thirdJcdw = xtszDto.getSzz();//若检测单位代码在thirdJcdw中，则匹配3.0项目，默认杭州实验室
							//后期若系统设置删除，则表示所有实验室均已更新到3.0系统
							JcsjDto jcdwDto = redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode(),sjxxDto.getJcdw());
							if (StringUtil.isNotBlank(thirdJcdw) && !thirdJcdw.contains(jcdwDto.getCsdm())){
								//若系统设置中thirdProject.jcdw配置不为空，且不包含当前检测单位代码，则匹配2.0项目
								isThirdProject = false;
							}
						}
					}
				}
				if (isThirdProject){
					//匹配3.0不带Onco项目,jcxm <=> cskz1
					xmCskz3 = "IMP_REPORT_SEQ_INDEX_TEMEPLATE";
				} else {
					//匹配3.0不带Onco项目,jcxm <=> cskz1
					xmCskz3 = "IMP_REPORT_ONCO_QINDEX_TEMEPLATE";
				}
			} else{
				if ("T".equals(sjxxDto.getJcxm())){
					isTCompare = false;
					if (sjxxDto.getDb().contains("汉库医学") || sjxxDto.getDb().contains("艾迪康")){
						//艾迪康、汉库医学默认TE
						xmCskz1 = "C";
						xmCskz3 = "IMP_REPORT_SEQ_TNGS_E";
					} else if (yblxDto != null && StringUtil.isNotBlank(yblxDto.getCskz2())){
						isNeedYblxLimit = true;
					}
				} else if ("K".equals(sjxxDto.getJcxm())){
					xmCskz1 = "C";
				} else if ("TA".equals(sjxxDto.getJcxm())){
					//TA
					xmCskz1 = "C";
					xmCskz3 = "IMP_REPORT_TNGS_TEMEPLATE";
				}else if ("TB".equals(sjxxDto.getJcxm())){
					//TB
					xmCskz1 = "C";
					xmCskz3 = "IMP_REPORT_SEQ_TNGS";
				} else if (sjxxDto.getJcxm().length()==2 && "T".equals(sjxxDto.getJcxm().substring(0,1))){
					//TC,TD,TE,TN的匹配
					xmCskz1 = "C";
					xmCskz3 = "IMP_REPORT_SEQ_TNGS_"+sjxxDto.getJcxm().substring(1);
				} else if ("F".equals(sjxxDto.getJcxm())){
					xmCskz1 = "F";
					xmCskz3 = "IMP_REPORT_RFS_TEMEPLATE";
				}
			}
			for (int j = 0; j < jcxmList.size(); j++) {
				String t_cskz3 = jcxmList.get(j).getCskz3()==null?"":jcxmList.get(j).getCskz3();
				if(jcxmList.get(j).getCskz1().equals(xmCskz1) && (t_cskz3.equals(xmCskz3) || (StringUtil.isBlank(xmCskz3) && StringUtil.isNotBlank(xmCskz1) && t_cskz3.contains("IMP_REPORT_SEQ_TNGS")))){
					if (isNeedYblxLimit){
						if (yblxDto.getCskz2().contains(jcxmList.get(j).getCsdm())){
							return jcxmList.get(j);
						}
					} else {
						return jcxmList.get(j);
					}
				}
			}
			Optional<JcsjDto> jc_TE_find = jcxmList.stream().filter(item -> "C".equals(item.getCskz1()) && "IMP_REPORT_SEQ_TNGS_E".equals(item.getCskz3())).findFirst();
			if (!isTCompare && jc_TE_find.isPresent()){
				//若项目中有T项目，且T项目未匹配完成，且找到T-B新项目
				//则在项目中加入T-E项目
				return jc_TE_find.get();
			}
		}
		return null;
	}

	/**
	 * 根据自动化编码获取标本实验信息
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> getSyxxByZdhbm(Map<String,String> map){
		return dao.getSyxxByZdhbm(map);
	}


	//hzxm,xb,nl,dh,sjdw,ks,sjys,ysdh,yblxmc,ybbh,cyrq,nldw,qtks,wbbm,sjdwmc,lczz,qqzd,bz,cwh.
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void copySjxx(SjxxDto copy1, SjxxDto copy2,User user) {
		SjxxDto copySjxxDto=new SjxxDto();
		copySjxxDto.setSjid(copy1.getSjid());
		copySjxxDto.setHzxm(copy2.getHzxm());
		copySjxxDto.setXb(copy2.getXb());
		copySjxxDto.setNl(copy2.getNl());
		copySjxxDto.setDh(copy2.getDh());
		copySjxxDto.setSjdw(copy2.getSjdw());
		copySjxxDto.setYsdh(copy2.getYsdh());
		copySjxxDto.setYblxmc(copy2.getYblxmc());
		copySjxxDto.setYbbh(copy2.getYbbh());
		copySjxxDto.setCyrq(copy2.getCyrq());
		copySjxxDto.setNldw(copy2.getNldw());
		copySjxxDto.setQtks(copy2.getQtks());
		copySjxxDto.setWbbm(copy2.getWbbm());
		copySjxxDto.setSjdwmc(copy2.getSjdwmc());
		copySjxxDto.setLczz(copy2.getLczz());
		copySjxxDto.setQqzd(copy2.getQqzd());
		copySjxxDto.setBz(copy2.getBz());
		copySjxxDto.setCwh(copy2.getCwh());
		copySjxxDto.setXgry(user.getYhid());
		copySjxxDto.setCskz6(copy2.getCskz6());
		copySjxxDto.setLrry(copy2.getLrry());
		dao.update(copySjxxDto);
		//老数据复制到新数据上
		SjxxDto copySjxxDto1=new SjxxDto();
		copySjxxDto1.setSjid(copy2.getSjid());
		copySjxxDto1.setHzxm(copy1.getHzxm());
		copySjxxDto1.setXb(copy1.getXb());
		copySjxxDto1.setNl(copy1.getNl());
		copySjxxDto1.setDh(copy1.getDh());
		copySjxxDto1.setSjdw(copy1.getSjdw());
		copySjxxDto1.setYsdh(copy1.getYsdh());
		copySjxxDto1.setYblxmc(copy1.getYblxmc());
		copySjxxDto1.setYbbh(copy1.getYbbh());
		copySjxxDto1.setCyrq(copy1.getCyrq());
		copySjxxDto1.setNldw(copy1.getNldw());
		copySjxxDto1.setQtks(copy1.getQtks());
		copySjxxDto1.setWbbm(copy1.getWbbm());
		copySjxxDto1.setSjdwmc(copy1.getSjdwmc());
		copySjxxDto1.setLczz(copy1.getLczz());
		copySjxxDto1.setQqzd(copy1.getQqzd());
		copySjxxDto1.setBz(copy1.getBz());
		copySjxxDto1.setCwh(copy1.getCwh());
		copySjxxDto1.setXgry(user.getYhid());
		copySjxxDto1.setCskz6(copy1.getCskz6());
		copySjxxDto1.setLrry(copy1.getLrry());
		dao.update(copySjxxDto1);
	}

/**
	 * @Description: 查询未上传的报告
	 * @param searchMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @Author: 郭祥杰
	 * @Date: 2025/2/25 16:21
	 */
	@Override
	public List<Map<String, Object>> getAbnormalReportList(Map<String, Object> searchMap) {
		return dao.getAbnormalReportList(searchMap);
	}

	/**
	 * @Description: 根据sjid查询检测项目
	 * @param sjid
	 * @return com.matridx.igams.wechat.dao.entities.SjxxDto
	 * @Author: 郭祥杰
	 * @Date: 2025/2/25 16:22
	 */
	@Override
	public SjxxDto selectReportBySjid(String sjid) {
		return dao.selectReportBySjid(sjid);
	}

	@Override
	public List<Map<String, String>> getWkcxBySjid(SjxxDto sjxxDto) {
		return dao.getWkcxBySjid(sjxxDto);
	}

	@Override
	public List<Map<String, String>> getWkcxByWkbh(Map<String, String> map) {
		return dao.getWkcxByWkbh(map);
	}

	@Override
	public List<Map<String, String>> getNcPcByWkbh(Map<String, String> map) {
		return dao.getNcPcByWkbh(map);
	}

	public String dealBlast(SjwzxxDto sjwzxxDto,FjcfbDto fjcfbDto){
		//获取文库编码，便于从文件进行对应
		sjwzxxDto=sjwzxxService.getDtoById(sjwzxxDto.getSjwzid());
		String wkbm=sjwzxxDto.getXpxx();
		String taxid=sjwzxxDto.getWzid();
		String path=fjcfbDto.getWjlj();
		DBEncrypt p = new DBEncrypt();
		Path resultPath = Paths.get(p.dCode(path));
		try {
			byte[] data = Files.readAllBytes(resultPath);
			String result = new String(data, "utf-8");
			JSONObject cr=JSONObject.parseObject(result);
			Map<String, String> map=JSONObject.parseObject(JSON.toJSONString(cr.get(wkbm)), new TypeReference<Map<String, String>>() {});
			String anno=map.get("annot_json");
			JSONObject cr_anno=JSONObject.parseObject(anno);
			Map<String,String> objec=JSONObject.parseObject(JSON.toJSONString(cr_anno.get("taxon")), Map.class);
			JSONArray all=new JSONArray();
			all.addAll(JSONArray.parseArray(JSONObject.toJSONString(objec.get("MTBC")!=null?objec.get("MTBC"):new JSONArray())));
			all.addAll(JSONArray.parseArray(JSONObject.toJSONString(objec.get("NTM")!=null?objec.get("NTM"):new JSONArray())));
			return compareResult(taxid,all);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	public String compareResult(String taxid,JSONArray jsonArray){
		//比较waxid，将匹配成功的detail内容组装成String返回
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject item = jsonArray.getJSONObject(i);
			String wzid = item.getString("taxid");
			if(wzid.equals(taxid)){
				String detail=item.getString("detail");
				if(StringUtil.isNotBlank(detail)){
					JSONArray details=JSONArray.parseArray(detail);
					StringBuilder detailBuilder = new StringBuilder();
					for (int j = 0; j < details.size(); j++) {
						detailBuilder.append(details.get(j)).append("\n");
					}
					return detailBuilder.toString().trim(); // 去除最后空行
				}
			}
		}
		return "";
	}

    /**
     * 修改送检信息根据sjid
     * @param sjxxDto
     * @return
     */
    public boolean updateByid(SjxxDto sjxxDto){
		int cnt = dao.updateByid(sjxxDto);
		return cnt > 0 ? true:false;
	}

	public boolean updateWbbmByList(List<SjxxDto> sjxxDtos){
		return dao.updateWbbmByList(sjxxDtos);
	}
	
	@Override
	public List<Map<String, String>> getFjByWjm(Map<String, String> map) {
		return dao.getFjByWjm(map);
	}

	/**
	 * @Description: 禾诺LIMS接口对接查询数据
	 * @param codeMap
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/7/14 10:52
	 */
	public void timedCallHeNuoNew(Map<String,Object> codeMap){
		codeMap.put("method","selectByDate");
		codeMap.put("HENUO_NEW_URL",HENUO_NEW_URL);
		codeMap.put("HENUO_NEW_ACCOUNT",HENUO_NEW_ACCOUNT);
		codeMap.put("HENUO_NEW_PASSWORD",HENUO_NEW_PASSWORD);
		codeMap.put("HENUO_NEW_USERTYPE",HENUO_NEW_USERTYPE);
		codeMap.put("HENUO_NEW_EntrustHosCode",HENUO_NEW_EntrustHosCode);
		try {
			matchingUtilNewRun(codeMap,"henuoNewInfo");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}

