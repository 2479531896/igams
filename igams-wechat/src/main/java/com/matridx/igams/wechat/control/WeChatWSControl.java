package com.matridx.igams.wechat.control;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.NoticeTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.ISjycfkService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ExternalUtil;
import com.matridx.igams.common.util.MqType;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.CwglDto;
import com.matridx.igams.wechat.dao.entities.EtiologyDto;
import com.matridx.igams.wechat.dao.entities.ExternalMessageModel;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FxsjglDto;
import com.matridx.igams.wechat.dao.entities.KpsqDto;
import com.matridx.igams.wechat.dao.entities.LaboratoryModel;
import com.matridx.igams.wechat.dao.entities.LsbModel;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjgzbyDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjlczzDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjqqjcDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqDto;
import com.matridx.igams.wechat.dao.entities.SwhtglDto;
import com.matridx.igams.wechat.dao.entities.SwhtmxDto;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionReportModel;
import com.matridx.igams.wechat.dao.entities.WeChatInspectionReportsModel;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.XmsyglDto;
import com.matridx.igams.wechat.service.svcinterface.ICwglService;
import com.matridx.igams.wechat.service.svcinterface.IEtiologyServie;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IFksqService;
import com.matridx.igams.wechat.service.svcinterface.IFxsjglService;
import com.matridx.igams.wechat.service.svcinterface.IHbdwqxService;
import com.matridx.igams.wechat.service.svcinterface.IJcsqglService;
import com.matridx.igams.wechat.service.svcinterface.IKpsqService;
import com.matridx.igams.wechat.service.svcinterface.ILsbService;
import com.matridx.igams.wechat.service.svcinterface.ISendMessageService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjtssqService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxResStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxTwoService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjzzsqService;
import com.matridx.igams.wechat.service.svcinterface.ISwhtglService;
import com.matridx.igams.wechat.service.svcinterface.ISwhtmxService;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.igams.wechat.service.svcinterface.IXmsyglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import com.taobao.api.ApiException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * 微信功能对外接口
 * 生物信息部送检信息
 * 		/ws/inspection/getInfoByYbbh 生信根据样本编码获取PCR验证结果
 * 		/ws/getApplyList	外部获取申请列表（生信部门调用，用于屏蔽不想被看到其他信息时使用，只显示指定字段信息）
 * 		/ws/getApplyByCode	外部获取申请列表，用于根据标本编码获取标本信息,生信系统调用
 * 		/ws/pathogen/receiveEntiretyResult	--接收用户送检详细结果信息(带用户信息，用于AI调试用，让医学部重新分析时所调用的借口)
 * 		--/ws/pathogen/receiveInspectionResult	--接收送检结果，送检耐药性信息，生信系统点击submit时会推送，点击send也会推送  已废弃
 * 		--/ws/pathogen/receiveWordReport	--接收结果报告word文件，生信系统点击点击send会推送  已废弃
 * 		/ws/pathogen/receiveDetailedReport	--接收送检详细结果信息，生信系统点击submit时会推送，点击send则不推送
 * 		/ws/pathogen/sendExternalMessage  -- 提供给外部，用于发送消息接口
 * 		/ws/pathogen/sendOutMessage		--向外部发送延迟消息接口
 *      /ws/inspect/receiveInspectInfo		--接收外部送检信息
 *      /ws/file/limitDownload              --文件下载（用于外部接口限时下载报告清单使用），返回给客户端的是map清单，还需继续调用接口
 *      /ws/file/downloadByCode             --文件下载（用于外部编码下载报告使用），返回给客户端的是文件流
 *      /ws/pathogen/getPathogenResultByCode  -获取检验结果（用于外部编码下载报告使用），返回给客户端的是JSON
 *      /ws/pathogen/externalGetReport		--外部获取送检报告
 * 		/ws/preview   外部 图片在线预览
 *  	/ws/uploadReport	request,sjxxDto, flg, access_token	--生信部调用上传页面
 *  	/ws/inspection/viewSjxx	request,sjxxDto, flg, access_token	--手机端统计报告里的送检报告阳性列表点击查看时调用的
 *      /ws/statistics/getDaily   --日报
 *      /ws/getDbList		--获取代表信息(统一数据查看权限)
 *      /ws/verification/viewVerifi    --钉钉消息查看送检验证信息
 *      /ws/verification/verification/modAuditVerify     --手机钉钉审核送检验证(实验员审核,可上传附件)
 *      /ws/verification/audit_Verifi   --手机钉钉审核送检验证
 *      /ws/samplenum/AuditSampleNum    --手机钉钉审核标本量
 *      /ws/inspection/getRecheckMessage    --生信部获取复测加测信息
 *      /ws/statistics/getWeekLeadStatis 领导周报
 *      /ws/statistics/getWeekLeadProvinceStatis  周报中的省份统计
 *      /ws/pathogen/receiveInspectionGenerateReport 接收送检结果信息生成报告(接口1,采用默认用户)
 *      /ws/pathogen/receiveInspectionGenerateReportSec 接收送检结果信息生成报告(接口2,传递用户名)
 *      /ws/pathogen/SpeciesStatistics 对接生信部门，获取物种统计图（接口参数为MultipartFile file)
 *      /ws/recheck/updateJcdw  --钉钉审核修改检测单位
 *      /ws/library/getLibraryInfo  --外部接口 根据文库名称获取文库信息
 *      /ws/inspection/modExperimentDate  --外部接口 NGS-master调用修改实验日期接口 (自动上报接口/experiment/addSaveNgsData)
 *      /ws/experiments/uploadResFirstResult  --天隆ResFirst结果上传处理
 *      /ws/pathogen/disposeSaveFile  --结核耐药文件接收处理 TBtNGS
 *      /ws/pathogen/disposeSaveFileRetry --结核耐药文件处理重试功能
 *      /ws/common/getBasicdata --获取基础数据
 * @author linwu
 *
 */
@Controller
@RequestMapping("/ws")
public class WeChatWSControl extends BaseController {
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	ISjjcxmService sjjcxmService;
	@Autowired
	IKpsqService kpsqService;
	@Autowired
	ISjxxWsService sjxxWsService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	IFxsjglService fxsjglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired(required = false)
	AmqpTemplate amqpTempl;
	@Autowired
	ISwhtglService swhtglService;
	@Autowired
	ISwhtmxService swhtmxService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ISjsyglService sjsyglService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	ISjnyxService sjnyxService;
	@Autowired
	ISjbgsmService sjbgsmservice;
	@Autowired
	IShlbService shlbService;
	@Autowired
	ISpgwcyService spgwcyService;
	@Autowired
	ILsbService lsbService;
	@Autowired
	ISjyzService sjyzService;
	@Autowired
	ICwglService cwglService;
	@Autowired
	ISjyzjgService sjyzjgService;
	@Autowired
	ISjycService sjycService;
	@Autowired
	ISjycfkService sjycfkService;
	@Autowired
	private ISjzmjgService sjzmjgService;
	//用于送检统计的service
	@Autowired
	ISjxxStatisticService sjxxStatisticService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	IHbdwqxService hbdwqxService;
	@Autowired
	IDdspglService ddspglService;
	@Autowired
	ISjtssqService sjtssqService;
	@Autowired
	ISendMessageService sendMessageService;
	@Autowired
	private ISjzzsqService sjzzsqService;
	@Autowired
	private DataSource postsqlDataSource;
	@Autowired
	private ISjwzxxService sjwzxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	//获取端口号
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	//微信通知的模板ID
	@Value("${matridx.wechat.kpi_templateid:}")
	private String kpi_templateid = null;
	//外网端口号

	//ftp服务器地址
	@Value("${matridx.wechat.externalurl:}")
	private String externalurl;

	@Value("${matridx.wechat.bioaudurl:}")
	private String bioaudurl;

	private Logger log = LoggerFactory.getLogger(WeChatWSControl.class);
//    @Value("${matridx.fileupload.releasePath:}")
//    String releaseFilePath;
//	@Value("${matridx.fileupload.prefix}")
//	String prefix;
	@Autowired
	IXxglService xxglService;
	@Autowired
	private IXszbService xszbService;

	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	ISjjcjgService sjjcjgService;
	@Autowired
	ISjxxResStatisticService sjxxResStatisticService;
	@Autowired
	IXxdyService xxdyService;
	@Autowired
	IXmsyglService xmsyglService;
	@Autowired
	IJcsqglService jcsqglService;
	@Autowired
	ISjxxTwoService sjxxTwoService;
	@Autowired
	IFksqService fksqService;
	@Autowired
	IEtiologyServie etiologyServie;
	@Autowired
	IWbhbyzService wbhbyzService;
	@Value("${matridx.xiansheng.url:}")
	private String XIANSHENG_URL;
	@Value("${matridx.xiansheng.orderurl:}")
	private String XIANSHENG_ORDERURL;
	@Value("${matridx.xiansheng.appId:}")
	private String XIANSHENG_APPID;
	@Value("${matridx.xiansheng.appSecret:}")
	private String XIANSHENG_APPSECRET;
	@Value("${matridx.xiansheng.business:}")
	private String XIANSHENG_BUSINESS;

	/**
	 * 通过样本编号获取相关的信息,PCR he
	 * @return
	 */
	@RequestMapping(value ="/inspection/getInfoByYbbh")
	@ResponseBody
	public Map<String,Object> getInfoByYbbh(SjyzjgDto sjyzjgDto, HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		try {
			map.put("state","success");
			boolean haveTBtNGS = false;
			map.put("haveTBtNGS",haveTBtNGS);
			sjyzjgDto.setCzbs("1");
			List<SjyzjgDto> sjyzjgDtos = sjyzjgService.getInfoByYbbh(sjyzjgDto);
			if (null != sjyzjgDtos && sjyzjgDtos.size() >0 ){
				//map.put("state","fail");
//			}else{
				//送检信息
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SjyzjgDto.class, "ybbh","yzlbmc","bgsj","jlmc","yzjgjgmc","ct1","ct2","nct1","nct2", "pct1","pct2");
				SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
				listFilters[0] = filter;
				map.put("sjxxInfo",JSONObject.toJSONString(sjyzjgDtos,listFilters));
			}

			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setYbbh(sjyzjgDto.getYbbh());
			sjxxDto.setNbbm(sjyzjgDto.getNbbm());
			SjxxDto res_sjxxDto = sjxxService.getDto(sjxxDto);
			if(res_sjxxDto==null) {
                return map;
			}
			SjxxDto searchSjxxDto = new SjxxDto();
			searchSjxxDto.setHzxm(res_sjxxDto.getHzxm());
			searchSjxxDto.setYblx(res_sjxxDto.getYblx());
			searchSjxxDto.setDb(res_sjxxDto.getDb());
			searchSjxxDto.setSjdw(res_sjxxDto.getSjdw());
			searchSjxxDto.setJcdw(res_sjxxDto.getJcdw());
			searchSjxxDto.setXb(res_sjxxDto.getXb());
			searchSjxxDto.setNl(res_sjxxDto.getNl());
			searchSjxxDto.setSjid(res_sjxxDto.getSjid());
			searchSjxxDto.setJcxmcskz("C");
			searchSjxxDto.setJcxmcskz3("IMP_REPORT_SEQ_TNGS_N");
			List<SjxxDto> similarSjxxJcxmList = sjxxService.getSimilarSjxxJcxmList(searchSjxxDto);
			if (!CollectionUtils.isEmpty(similarSjxxJcxmList)){
				haveTBtNGS = true;
				map.put("haveTBtNGS",haveTBtNGS);
			}
			if (haveTBtNGS){
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(similarSjxxJcxmList.get(0).getSjid());
				List<String> ywlxs = new ArrayList<>();
				ywlxs.add("IMP_REPORT_SEQ_TNGS_N_C");
				ywlxs.add("IMP_REPORT_SEQ_TNGS_N_C_WORD");
				fjcfbDto.setYwlxs(ywlxs);
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlxOrderByYwlx(fjcfbDto);
				if (!CollectionUtils.isEmpty(fjcfbDtos)){
					String url = menuurl+"/wechat/getReportView";
					url += "?sjid=" + similarSjxxJcxmList.get(0).getSjid();
					url += "&ybbh=" + similarSjxxJcxmList.get(0).getYbbh();
					url += "&sign=" + commonService.getSign(similarSjxxJcxmList.get(0).getYbbh());
					map.put("viewUrl",url);
				}
			}
			List<SjjcjgDto> weekList = sjjcjgService.getWeekList(res_sjxxDto);
			if(weekList!=null && weekList.size() > 0) {
				SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter(SjjcjgDto.class, "ywid","pdz","ncysz","ncjsz","ckbl2","ckbl1","pjz","jcjgmc","ncysz2","ncjsz2","jlmc","jclxmc","kw","jgsz","jcjgcskz4");
				SimplePropertyPreFilter[] simplePropertyPreFilters = new SimplePropertyPreFilter[1];
				simplePropertyPreFilters[0] = simplePropertyPreFilter;
				map.put("rfs_list",JSONObject.toJSONString(weekList,simplePropertyPreFilters));
			}
		} catch (Exception e) {
			map.put("state","fail");
		}

		return map;
	}

	/**
	 * 获取申请列表
	 * @return
	 */
	@RequestMapping("/getApplyList")
	@ResponseBody
	public String getApplyList(HttpServletRequest request, SjxxDto sjxxDto){
		try{
			//参数检查
			//日期格式不正确
			if(!StringUtil.isValidDate(sjxxDto.getApplydatestart(), "yyyy-MM-dd") || !StringUtil.isValidDate(sjxxDto.getApplydateend(), "yyyy-MM-dd")) {
				return null;
			}
			//boolean result = sjxxService.get
			Map<String,Object> map = sjxxService.getApplyList(sjxxDto);
			//送检信息
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SjxxDto.class, "sjid","sjdwmc","hzxm","xbmc","nl","dh","sjdw","ksmc","sjys","jsrq","fjsrq",
					"ysdh","dbm","yblxmc","ybtj","zyh","cwh","ybbh","cyrq","lczz","qqzd","jqyy","qqjc","ksmc","sjjcxms","sjlczzs","sjqqjcs","sjgzbys","bz");
			SimplePropertyPreFilter xmFilter = new SimplePropertyPreFilter(SjjcxmDto.class, "csmc","csdm");
			SimplePropertyPreFilter lcFilter = new SimplePropertyPreFilter(SjlczzDto.class, "csmc","csdm");
			SimplePropertyPreFilter gzbyFilter = new SimplePropertyPreFilter(SjgzbyDto.class, "csmc","csdm");
			SimplePropertyPreFilter qqjcFilter = new SimplePropertyPreFilter(SjqqjcDto.class, "csmc","csdm","jcz");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[5];
			listFilters[0] = filter;
			listFilters[1] = xmFilter;
			listFilters[2] = lcFilter;
			listFilters[3] = gzbyFilter;
			listFilters[4] = qqjcFilter;
			return JSONObject.toJSONString(map,listFilters);
		}catch(Exception e){
			log.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取申请列表
	 * @return
	 */
	@RequestMapping("/getApplyByCode")
	@ResponseBody
	public String getApplyByCode(HttpServletRequest request, SjxxDto sjxxDto){
		try{
			//参数检查
			//未获取到标本编号
			if(StringUtil.isBlank(sjxxDto.getYbbh())&&StringUtil.isBlank(sjxxDto.getNbbm())) {
                return "";
			}
			String[] strs={"sjid","hzxm","xbmc","nl","dh","sjdw","sjdwmc","ksmc","sjys","jsrq","fjsrq",
					"ysdh","dbm","yblxmc","ybtj","zyh","cwh","ybbh","cyrq","lczz","qqzd","jqyy","qqjc","ksmc","jcxmmc","jcxmdm","jc_cskz1","gzbymc","qqjcmc","lczzmc","db","mbdm","nldw","bz","bgfsdm","hbbz","hbjbmc","sfvip","xxdy_kzcs1","xxdy_kzcs7","pcrflg"};
			List<String> strings=new ArrayList<>(Arrays.asList(strs));
			//boolean result = sjxxService.get
			Map<String,String> getMap= sjxxService.getDtoMap(sjxxDto);
			if(getMap==null) {
                return "";
			}
			XmsyglDto xmsyglDto = new XmsyglDto();
			xmsyglDto.setYwid(getMap.get("sjid"));
			List<XmsyglDto> xmsyglDtos = xmsyglService.getXmsyXXdyYwids(xmsyglDto);
			String xxdy_kzcs1 = String.join(" ", xmsyglDtos.stream().map(XmsyglDto::getXxdy_kzcs1).collect(Collectors.toList()));
			String xxdy_kzcs7 = String.join(" ", xmsyglDtos.stream().map(XmsyglDto::getXxdy_kzcs7).collect(Collectors.toList()));
			getMap.put("xxdy_kzcs1",xxdy_kzcs1);
			getMap.put("xxdy_kzcs7",xxdy_kzcs7);
			//SjxxDto res_sjxxDto = sjxxService.getDto(sjxxDto);
			/**
			 * 目的：为了生信可以自动出报告，需要在/ws/getApplyByCode 接口的标本返回信息里增加是否同步做了PCR结核的标记。生信这边如果没做PCR，则直接出报告，如果做了PCR，则要手动。
			 */
			SjyzDto parmSjyz=new SjyzDto();
			parmSjyz.setYzzts(new String[]{"10","80"});
			parmSjyz.setSjid(getMap.get("sjid"));
			parmSjyz.setScbj("0");
			parmSjyz.setYzdm("MTM");
			List<SjyzDto> sjyzDtoList=sjyzService.getByYzlbAndSjid(parmSjyz);
			if(!org.springframework.util.CollectionUtils.isEmpty(sjyzDtoList)){
				getMap.put("pcrflg","true");
			}else{
				getMap.put("pcrflg","false");
			}
			if(StringUtil.isNotBlank(getMap.get("qtks"))){
				getMap.put("ksmc",getMap.get("qtks"));
			}
			if(StringUtil.isNotBlank(getMap.get("kzsz"))){
				String json=getMap.get("kzsz");//目前存放zdfs:是否自动发送报告，1：发送
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(json);
				Map<String,String> map=mapper.convertValue(jsonNode, new TypeReference<Map<String, String>>() {});
				Set<String> setStr=map.keySet();
				for(String key:setStr){
					strings.add(key);
					getMap.put(key,map.get(key));
				}

			}
			SjjcxmDto sjjcxmDto = new SjjcxmDto();
			sjjcxmDto.setSjid(getMap.get("sjid"));
			List<SjjcxmDto> sjjcxms= sjjcxmService.getDtoList(sjjcxmDto);
			if(sjjcxms!=null && sjjcxms.size() > 0) {
				StringBuffer sb_jcxm =new StringBuffer("");
				StringBuffer sb_jcxmdm =new StringBuffer("");
				int i=0;
				for(SjjcxmDto t_sjjcxm:sjjcxms) {
					if(t_sjjcxm!=null && StringUtil.isNotBlank(t_sjjcxm.getJcxmmc())) {
						if(i !=0)
						{
							sb_jcxm.append(",");
							sb_jcxmdm.append(",");
						}
						sb_jcxm.append(t_sjjcxm.getJcxmmc());
						sb_jcxmdm.append(t_sjjcxm.getJcxmdm());

						i++;
					}
				}
				getMap.put("jcxmmc", sb_jcxm.toString());
				getMap.put("jcxmdm", sb_jcxmdm.toString());
			}
			String[] starr= strings.toArray(new String[0]);
			//送检信息
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SjxxDto.class, starr);
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			//return JSONObject.toJSONString(res_sjxxDto,listFilters);
			String resultString = JSONObject.toJSONString(getMap,listFilters);
			//log.error("确认项目信息是否存在：getApplyByCode:" + resultString);
			return resultString;
			/*map.put("sj_dto",JSONObject.toJSONString(res_sjxxDto,listFilters));
			List<SjjcjgDto> weekList = sjjcjgService.getWeekList(res_sjxxDto);
			SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter(SjjcjgDto.class, "ywid","pdz","ncysz","ncjsz","ckbl2","ckbl1","pjz","jcjgmc","ncysz2","ncjsz2","jlmc","jclxmc","kw","jgsz","jcjgcskz4");
			SimplePropertyPreFilter[] simplePropertyPreFilters = new SimplePropertyPreFilter[1];
			simplePropertyPreFilters[0] = simplePropertyPreFilter;
			map.put("rfs_list",JSONObject.toJSONString(weekList,simplePropertyPreFilters));
			return map;
			*/
		}catch(Exception e){
			log.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取申请列表sj(艾迪康使用)
	 * @return
	 */
	@RequestMapping("/getApplyByCodesToExternal")
	@ResponseBody
	public Map<String,String> getApplyByCodesToExternal(HttpServletRequest request,SjxxDto sjxxDto){
		try{
			Map<String,String> resultmap=new HashMap<>();
			Date date = new Date();
			long milliseconds = date.getTime();
			org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
			if(StringUtil.isBlank(request.getParameter("sign"))){
				resultmap.put("msg","请传递签名sign");
				return resultmap;
			}
			String str=new String(base64.decode(request.getParameter("sign")), StandardCharsets.UTF_8);
			long requestSeconds=Long.valueOf(str);

			if(requestSeconds+(1000*60*5)<milliseconds){
				resultmap.put("msg","权限过期");
				return resultmap;
			}
			//参数检查
			//未获取到标本编号
			if(CollectionUtils.isEmpty(sjxxDto.getYbbhs())&&CollectionUtils.isEmpty(sjxxDto.getNbbms())) {
				resultmap.put("msg","请传递标本编号");
				return resultmap;
			}
			List<SjxxDto> dtoList = sjxxService.getDtoAllList(sjxxDto);
			if(CollectionUtils.isEmpty(dtoList)) {
				resultmap.put("msg","未获取到标本信息");
				return resultmap;
			}
			//送检信息
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SjxxDto.class, "hzxm","xbmc","nl","dh","qtks","sjys","jsrq",
					"ysdh","yblxmc","ybtj","zyh","cwh","ybbh","cyrq","qqzd","jqyy","ksmc","jcxmmc","gzbymc","qqjcmc","lczzmc","nldw","bz","jcdwmc");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			String jsonString = JSONObject.toJSONString(dtoList, listFilters);
			List<Map<String,String>> jsonList = JSONArray.parseObject(jsonString, new com.alibaba.fastjson.TypeReference<List<Map<String, String>>>() {});
			List<String> ywids = dtoList.stream().map(SjxxDto::getSjid).collect(Collectors.toList());
			XmsyglDto xmsyglDto = new XmsyglDto();
			xmsyglDto.setYwids(ywids);
			List<XmsyglDto> xmsyglDtos = xmsyglService.getXmsyXXdyYwids(xmsyglDto);
			for (Map<String,String> dto : jsonList) {
				List<XmsyglDto> collect = xmsyglDtos.stream().filter(item -> item.getYwid().equals(dto.get("sjid"))).collect(Collectors.toList());
				String xxdy_kzcs1 = String.join(" ", collect.stream().map(XmsyglDto::getXxdy_kzcs1).collect(Collectors.toList()));
				String xxdy_kzcs7 = String.join(" ", collect.stream().map(XmsyglDto::getXxdy_kzcs7).collect(Collectors.toList()));
				dto.put("xxdy_kzcs1",xxdy_kzcs1);
				dto.put("xxdy_kzcs7",xxdy_kzcs7);
				dto.put("sjdwmc","第三方");//回传给艾迪康，sjdwmc默认显示第三方
			}
			resultmap.put("sampleList",JSONObject.toJSONString(jsonList));
			return resultmap;
		}catch(Exception e){
			log.error(e.toString());
		}
		return null;
	}

	@RequestMapping("/updateExternalSample")
	@ResponseBody
	public Map<String,String> updateExternalSample(HttpServletRequest request){
		Map<String,String> map=new HashMap<>();
		String result=request.getParameter("result");
		Date date = new Date();
		long milliseconds = date.getTime();
		org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
		if(StringUtil.isBlank(request.getParameter("sign"))){
			map.put("status","fail");
			map.put("msg","请传递签名sign");
			return map;
		}
		String str=new String(base64.decode(request.getParameter("sign")), StandardCharsets.UTF_8);
		long requestSeconds=Long.valueOf(str);

		if(requestSeconds+(1000*60*5)<milliseconds){
			map.put("status","fail");
			map.put("msg","权限过期");
			return map;
		}
		if(StringUtil.isNotBlank(result)){
			try {
				List<SjxxDto> sjxxDtos=JSONArray.parseArray(result,SjxxDto.class);

				//更新外部编码,接收日期，以及是否接收
				if(!CollectionUtils.isEmpty(sjxxDtos)){
					sjxxService.updateWbbmByList(sjxxDtos);
					map.put("status","success");
					map.put("msg","回传成功！");
				}
			}catch (Exception e) {
				log.error(e.getMessage());
				map.put("status","fail");
				map.put("msg","处理异常");
				return map;
			}

		}else{
			map.put("status","fail");
			map.put("msg","请传递参数");
		}
		return map;
	}




	/**
	 * 获取申请列表sj
	 * @return
	 */
	@RequestMapping("/getApplyByCodes")
	@ResponseBody
	public String getApplyByCodes(SjxxDto sjxxDto){
		try{
			//参数检查
			//未获取到标本编号
			if(CollectionUtils.isEmpty(sjxxDto.getYbbhs())&&CollectionUtils.isEmpty(sjxxDto.getNbbms())) {
				return "";
			}
			List<SjxxDto> dtoList = sjxxService.getDtoAllList(sjxxDto);
			if(CollectionUtils.isEmpty(dtoList)) {
				return "";
			}
			//送检信息
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SjxxDto.class, "sjid","hzxm","xbmc","nl","dh","sjdw","sjdwmc","ksmc","sjys","jsrq","fjsrq",
					"ysdh","dbm","yblxmc","ybtj","zyh","cwh","ybbh","cyrq","lczz","qqzd","jqyy","qqjc","ksmc","jcxmmc","jcxmdm","jc_cskz1","gzbymc","qqjcmc","lczzmc","db","mbdm","nldw","bz","bgfsdm");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			String jsonString = JSONObject.toJSONString(dtoList, listFilters);
			List<Map<String,String>> jsonList = JSONArray.parseObject(jsonString, new com.alibaba.fastjson.TypeReference<List<Map<String, String>>>() {});
			List<String> ywids = dtoList.stream().map(SjxxDto::getSjid).collect(Collectors.toList());
			XmsyglDto xmsyglDto = new XmsyglDto();
			xmsyglDto.setYwids(ywids);
			List<XmsyglDto> xmsyglDtos = xmsyglService.getXmsyXXdyYwids(xmsyglDto);
			for (Map<String,String> dto : jsonList) {
				List<XmsyglDto> collect = xmsyglDtos.stream().filter(item -> item.getYwid().equals(dto.get("sjid"))).collect(Collectors.toList());
				String xxdy_kzcs1 = String.join(" ", collect.stream().map(XmsyglDto::getXxdy_kzcs1).collect(Collectors.toList()));
				String xxdy_kzcs7 = String.join(" ", collect.stream().map(XmsyglDto::getXxdy_kzcs7).collect(Collectors.toList()));
				dto.put("xxdy_kzcs1",xxdy_kzcs1);
				dto.put("xxdy_kzcs7",xxdy_kzcs7);
			}
			return JSONObject.toJSONString(jsonList);
		}catch(Exception e){
			log.error(e.toString());
		}
		return null;
	}

	/**
	 * 整体送检结果信息
	 * @param request
	 * @param br
	 * @return
	 */
	@RequestMapping(value = "/pathogen/receiveEntiretyResult")
	@ResponseBody
	public Map<String, Object> receiveEntiretyResult(HttpServletRequest request, BufferedReader br){
		//request.getb
		String inputLine;
		String str = "";
		Map<String,Object> map = new HashMap<>();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
			if(StringUtil.isNotBlank(str)) {
				boolean result = sjxxService.receiveEntiretyResult(str);
				if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "保存失败！");
					log.error("保存失败！");
				}
			}else{
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("errorCode", "IO获取数据异常！");
			log.error(e.toString());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 接收送检结果信息
	 * @param
	 */
	@RequestMapping(value = "/pathogen/receiveInspectionResult")
	@ResponseBody
	public Map<String, Object> receiveInspectionResult(HttpServletRequest request,BufferedReader br){
		//request.getb
		String inputLine;
		String str = "";
		Map<String,Object> map = new HashMap<>();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
			if(StringUtil.isNotBlank(str)) {
				boolean result = sjxxService.receiveInspectionResult(str);
				if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "保存失败！");
					log.error("保存失败！");
				}
			}else{
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("errorCode", "IO获取数据异常！");
			log.error(e.toString());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 接收word文件，并保存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pathogen/receiveWordReport")
	@ResponseBody
	public Map<String, Object> receiveWordReport(HttpServletRequest request, SjxxDto sjxxDto, WeChatInspectionReportsModel info){
		Map<String,Object> map = new HashMap<>();

		boolean isSuccess = sjxxService.saveInspectionResult(info);
		if(!isSuccess){
			map.put("status", "fail");
			return map;
		}
		sjxxDto.setJclx(info.getDetection_type());
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file!=null&& imp_file.length>0){
				for(int i=0;i<imp_file.length;i++){
					if(!imp_file[i].isEmpty()){
						MultipartFile file = imp_file[i];
						//文件处理
						boolean result = sjxxService.receiveWordReport(file, sjxxDto);
						if(result){
							map.put("status","success");
							map.put("errorCode", 0);
						}else{
							map.put("status", "fail");
							map.put("errorCode", "保存失败！");
							log.error("保存失败！");
						}
					}
				}
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMsg());
			log.error(e.getMsg()+e.toString());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 接收详细审核结果，并保存
	 * @param request
	 * @param br
	 * @return
	 */
	@RequestMapping(value="/pathogen/receiveDetailedReport")
	@ResponseBody
	public Map<String, Object> receiveDetailedReport(HttpServletRequest request,BufferedReader br){
		//request.getb
		String inputLine;
		String str = "";
		Map<String,Object> map = new HashMap<>();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
			if(StringUtil.isBlank(str)) {
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}else{
				//log.error("接收到生信详细审核结果:"+str);
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.DETAILED_IGAMS_INSPECTION.getCode(), str);
				//boolean result = sjxxService.receiveDetailedReport(str);
				map.put("status","success");
				map.put("errorCode", 0);
				/*if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "保存失败！");
					log.error("保存失败！");
				}*/
			}
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("errorCode", "IO获取数据异常！");
			log.error("IO获取数据异常！" + e.toString());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 接收外部送检信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/inspect/receiveInspectInfo")
	@ResponseBody
	public Map<String, Object> receiveInspectInfo(HttpServletRequest request, String jsonStr, String organ, String sign){
		DBEncrypt crypt = new DBEncrypt();
		log.error("接收到送检信息-ws："+jsonStr+",organ："+organ+",sign："+sign);
		boolean result = sjxxWsService.checkMessage(jsonStr);
		Map<String, Object> resultMap = new HashMap<>();
		if(!result){
			resultMap.put("status", "success");
			resultMap.put("errorCode", "0");
			resultMap.put("errorInfo", "保存成功！");
			return resultMap;
		}
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setLxqf("recv"); // 类型区分 发送 send;接收recv
		jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null));
		jkdymxDto.setDydz("/ws/inspect/receiveInspectInfo");
		jkdymxDto.setNr(jsonStr);
		jkdymxDto.setQtxx("organ："+organ+",sign："+sign);
		jkdymxDto.setDyfl(InvokingTypeEnum.RECEIVE_INSPECTINFO.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.RECEIVE_INSPECTINFO_WS.getCode());
		List<SjxxDto> sjxxDtos = null;
		try {
			//为了保存业务ID，需要增加内容的解析
			sjxxDtos = JSONObject.parseArray(jsonStr, SjxxDto.class);
			if(sjxxDtos!=null&&sjxxDtos.size()>0){
				jkdymxDto.setYwid(sjxxDtos.get(0).getWbbm());
			}
		}catch(Exception e){

		}
		Map<String, Object> map = sjxxWsService.checkSecurityReceive(organ, jsonStr, sign, crypt,false);
		if(!"0".equals(map.get("errorCode"))){
			map.put("status", "fail");
			log.error((String)map.get("errorInfo"));
			jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
			String fhnr = JSONObject.toJSONString(map);
			if(fhnr.length()>8000)
				jkdymxDto.setFhnr(fhnr.substring(0,8000));
			else
				jkdymxDto.setFhnr(fhnr); // 返回内容
			jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
			jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
			return map;
		}
		try{
			map = sjxxService.receiveInspectInfoMap(request, sjxxDtos);
			jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("errorCode", "189");
			map.put("errorInfo", e.getMsg());
			log.error(e.getMsg()+e.toString());
			jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", "199");
			map.put("errorInfo", e.getMessage());
			log.error(e.toString());
			jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
		}
		String fhnr = JSONObject.toJSONString(map);
		if(fhnr.length()>8000)
			jkdymxDto.setFhnr(fhnr.substring(0,8000));
		else
			jkdymxDto.setFhnr(fhnr); // 返回内容
		//jkdymxDto.setFhnr(JSONObject.toJSONString(map)); // 返回内容
		jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
		jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
		return map;
	}
	/**
	 * 接收外部送检信息,需要合并D,R文库，主要是为了北大一的记录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/inspect/receiveInspectInfoPlus")
	@ResponseBody
	public Map<String, Object> receiveInspectInfoPlus(HttpServletRequest request, String jsonStr, String organ, String sign){
		DBEncrypt crypt = new DBEncrypt();
		log.error("接收到送检信息plus-ws："+jsonStr+",organ："+organ+",sign："+sign);
		JkdymxDto jkdymxDto = new JkdymxDto();
		jkdymxDto.setLxqf("recv"); // 类型区分 发送 send;接收recv
		jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null));
		jkdymxDto.setDydz("/ws/inspect/receiveInspectInfoPlus");
		jkdymxDto.setNr(jsonStr);
		jkdymxDto.setQtxx("organ："+organ+",sign："+sign);
		jkdymxDto.setDyfl(InvokingTypeEnum.RECEIVE_INSPECTINFO.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.RECEIVE_INSPECTINFO_WS_PLUS.getCode());
//		Map<String, Object> map = sjxxWsService.checkSecurityReceive(organ, jsonStr, sign, crypt,false);
//		if(!"0".equals(map.get("errorCode"))){
//			map.put("status", "fail");
//			log.error((String)map.get("errorInfo"));
//			jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
//			String fhnr = JSONObject.toJSONString(map);
//			if(fhnr.length()>8000)
//				jkdymxDto.setFhnr(fhnr.substring(0,8000));
//			else
//				jkdymxDto.setFhnr(fhnr); // 返回内容
//			//jkdymxDto.setFhnr(JSONObject.toJSONString(map)); // 返回内容
//			jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
//			jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
//			return map;
//		}
		Map<String, Object> map = new HashMap<>();
		try{
			map = sjxxService.receiveInspectInfoMapPlus(request, jsonStr);
			jkdymxDto.setSfcg("1"); // 是否成功  0:失败 1:成功 2:未知
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("errorCode", "189");
			map.put("errorInfo", e.getMsg());
			log.error(e.getMsg()+e.toString());
			jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", "199");
			map.put("errorInfo", e.getMessage());
			log.error(e.toString());
			jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
		}
		String fhnr = JSONObject.toJSONString(map);
		if(fhnr.length()>8000)
			jkdymxDto.setFhnr(fhnr.substring(0,8000));
		else
			jkdymxDto.setFhnr(fhnr); // 返回内容
		//jkdymxDto.setFhnr(JSONObject.toJSONString(map)); // 返回内容
		jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
		jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
		return map;
	}

	/**
	 * 外部获取下载报告地址清单接口
	 * @param request
	 * @param code
	 * @param type
	 * @param sign
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/pathogen/externalGetReport")
	@ResponseBody
	public Map<String, Object> externalGetReport(HttpServletRequest request, String organ, String code, String lastcode, String type, String sign, HttpServletResponse response) throws IOException {
		BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		while ((inputStr = streamReader.readLine()) != null)
			responseStrBuilder.append(inputStr);
		try{
			JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
			String param= jsonObject.toJSONString();
			if(StringUtils.isNotBlank(param)){
				if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("organ"))))
					organ=String.valueOf(jsonObject.get("organ"));
				if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("code"))))
					code=String.valueOf(jsonObject.get("code"));
				if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("lastcode"))))
					lastcode=String.valueOf(jsonObject.get("lastcode"));
				if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("type"))))
					type=String.valueOf(jsonObject.get("type"));
				if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("sign"))))
					sign=String.valueOf(jsonObject.get("sign"));
			}
		}catch (Exception e){
			log.error("未获取到JSON格式的参数!");
		}
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = sjxxWsService.checkSecurity(organ, type, code, lastcode, sign, crypt);
		if(!"0".equals(map.get("errorCode"))){
			map.put("status", "fail");
			map.put("errorCode", map.get("errorCode"));
			log.error((String)map.get("errorCode"));
			return map;
		}
		try{
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setWbbm(code);
			sjxxDto.setLastwbbm(lastcode);
			sjxxDto.setLx(type);
			map = sjxxService.externalGetReport(request, sjxxDto, organ);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 外部获取下载报告地址清单接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pathogen/externalGetReportList")
	@ResponseBody
	public String externalGetReportList(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			List<Map<String, String>> ybInfoList = sjxxWsService.getAndCheckYbxxInfo(request,false);
			List<String> ybbhs = ybInfoList.stream().map(item -> item.get("ybbh")).collect(Collectors.toList());
			if (ybInfoList != null && ybInfoList.size() > 0) {
				List<String> ywids = ybInfoList.stream().map(item -> item.get("sjid")).collect(Collectors.toList());
				List<Map<String, Object>> data = sjxxWsService.getJcxmFjInfoByYwids(ywids);
				String limit = "50";
				Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
				if (settting != null) {
					JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
					limit = setttingObj.getString("szz");
				}
				if (Integer.parseInt(limit) == ybInfoList.size()){
					map.put("lastcode",ybbhs.get(ybbhs.size()-1));
				}
				map.put("data", data);
				map.put("status","success");
				map.put("errorCode","0");
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return JSON.toJSONString(map);
	}
	/**
	 * 外部获取下载报告地址清单接口Plus
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pathogen/externalGetReportListPlus")
	@ResponseBody
	public String externalGetReportListPlus(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			List<Map<String, String>> ybInfoList = sjxxWsService.getAndCheckYbxxInfo(request,true);
			if (ybInfoList != null && ybInfoList.size() > 0) {
				List<String> sjbms = ybInfoList.stream().map(item -> item.get("sjbm")).collect(Collectors.toList());
				List<String> ywlxnotlikes = new ArrayList<>();
				ywlxnotlikes.add("WORD");
				List<Map<String, Object>> data = sjxxWsService.getJcxmFjInfoByYwidsPlus(ybInfoList,null,ywlxnotlikes);
				String limit = "50";
				Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
				if (settting != null) {
					JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
					limit = setttingObj.getString("szz");
				}
				if (Integer.parseInt(limit) == ybInfoList.size()){
					map.put("lastcode",sjbms.get(sjbms.size()-1));
				}
				map.put("data", data);
				map.put("status","success");
				map.put("errorCode","0");
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 文件下载（用于外部接口限时下载报告清单使用）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/limitDownload")
	@ResponseBody
	public String limitDownload(HttpServletRequest request, FjcfbDto fjcfbDto, HttpServletResponse response){
		String sign = request.getParameter("sign");
		if(StringUtil.isBlank(sign) || StringUtil.isBlank(fjcfbDto.getFjid())){
			log.error("limitDownload ---- 未获取到签名： " + sign + " 或附件ID： " + fjcfbDto.getFjid());
			return null;
		}
		boolean checkSjgn=commonService.checkSign(sign,null, request);
		if(!checkSjgn) {
			log.error("签名超时或错误：" + sign);
			return null;
		}
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null){
			log.error("未找到文件：" + fjcfbDto.getFjid());
			return null;
		}
		String wjlj = t_fjcfbDto.getWjlj();
		String wjm = t_fjcfbDto.getWjm();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		sjxxWsService.download(filePath, wjm, response);
        return null;
	}

	/**
	 * 文件下载（用于外部编码下载报告使用）
	 * @param
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/downloadByCode")
	public void downloadByCode(HttpServletRequest request, String organ, String type, String code, String lastcode, String sign, HttpServletResponse response){
		sjxxWsService.downloadByCode(request, organ, type, code, lastcode, sign, response);
	}

	/**
	 * 根据标本编码（或者外部编码）获取报告结果 报告结果采用base64的方式进行编码，同时反馈相应的质量信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pathogen/externalGetSampleInfo")
	@ResponseBody
	public Map<String,Object> getPathogenBase64(HttpServletRequest request){
		Map<String,Object>map=new HashMap<>();
		try {
			map=sjxxWsService.commonJsonResult(request,null,null);
		}catch (Exception e){
			map.put("status","error");
			map.put("message",e.getMessage());
		}
		return map;
	}
	/**
	 * 文件下载（用于外部编码下载报告使用）Plus
	 * @param
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/downloadByCodePlus")
	public void downloadByCodePlus(HttpServletRequest request, String organ, String type, String code, String lastcode, String sign, HttpServletResponse response){
		sjxxWsService.downloadByCodePlus(request, organ, type, code, lastcode, sign, response);
	}

	/**
	 *外部 图片在线预览
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/preview")
	public  ModelAndView preview(HttpServletRequest request, SjxxDto sjxxDto) {
		ModelAndView mav= new ModelAndView("wechat/sjxx/preview");
		List<FjcfbDto> fjlist=sjxxService.preview(sjxxDto);
		for (int i = fjlist.size()-1; i >=0; i--){
			int begin=fjlist.get(i).getWjm().lastIndexOf(".");
			int end=fjlist.get(i).getWjm().length();
			if(begin!=-1) {
				String type=fjlist.get(i).getWjm().substring(begin,end);
				if(!(type.equalsIgnoreCase(".jpg"))&&!(type.equalsIgnoreCase(".jpeg"))&&!(type.equalsIgnoreCase(".jfif"))&&!(type.equalsIgnoreCase(".png"))) {
					fjlist.remove(i);
				}
			}
		}
		mav.addObject("fjlist", fjlist);
		return mav;
	}

	/**
	 * word在线预览
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value="/file/wordPreview")
	public ModelAndView wordPreview(HttpServletRequest request, FjcfbDto fjcfbDto) {
		try {
			ModelAndView mv = new ModelAndView("common/file/filepreview");
			mv.addObject("fjcfbDto",fjcfbDto);
			return mv;
		}catch(Exception e) {
			log.error(e.toString());
		}
		return null;
	}

	/**
	 * 生信部修改送检信息 同步到IT部
	 * @param
	 * @return
	 */
	@RequestMapping(value="/sjxx/modSaveSjxx")
	@ResponseBody
	public Map<String, Object> modSaveSjxx(HttpServletRequest request,BufferedReader br) {
		//request.getb
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (IOException e) {
			log.error(e.toString());
		}

		boolean result = sjxxService.receiveModSjxx(str);
		Map<String,Object> map = new HashMap<>();
		map.put("status", result?"success":"fail");
		return map;
	}

	/**
	 * 外部调用报告上传接口
	 *
	 *上传报告接口
	 *http://localhost:8086/common/view/displayViewPC?
	 *view_url=/ws/uploadReport%3Fybbh%3D10000001%26flg%3D1%26access_token%3D8aee77ef-82a8-45ab-a991-255cc633812a
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/uploadReport")
	public  ModelAndView uploadReport(HttpServletRequest request, SjxxDto sjxxDto, String flg, String access_token) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/pathogen_uploadReport");
		sjxxDto = sjxxService.getDto(sjxxDto);
		if(sjxxDto == null)
		{
			mav=new ModelAndView("wechat/sjxx/sjxx_outError");
			return mav;
		}
		// 根据检测项目获取文件类型
		sjxxDto.setYwlx(sjxxDto.getCskz3() + "_" + sjxxDto.getCskz1());
		sjxxDto.setW_ywlx(sjxxDto.getCskz3() + "_" + sjxxDto.getCskz1() + "_WORD");
		mav.addObject("flg", flg);
		mav.addObject("access_token", access_token);
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	@RequestMapping(value="/checkToken")
	@ResponseBody
	public Map<String, String> checkToken(HttpServletRequest request) {

		Map<String, String> resultMap = new HashMap<>();

		String s_access_token = request.getParameter("access_token");

		if(StringUtil.isBlank(s_access_token)) {
			resultMap.put("status", "false");
			resultMap.put("errorCode", "1");
			resultMap.put("errorMessage", "未获取到access_token参数！");
			return resultMap;
		}

		try {
			JdbcTokenStore store = new JdbcTokenStore(postsqlDataSource);

			OAuth2AccessToken accessToken = store.readAccessToken(s_access_token);

			if (accessToken == null) {
				resultMap.put("status", "false");
				resultMap.put("errorCode", "2");
				resultMap.put("errorMessage", "Invalid access token: " + s_access_token);
				return resultMap;
			}
			else if (accessToken.isExpired()) {
				resultMap.put("status", "false");
				resultMap.put("errorCode", "3");
				resultMap.put("errorMessage", "Access token expired: " + s_access_token);
			}else {
				Encrypt encrypt = new Encrypt();
				String tokenId = encrypt.extractTokenKey(s_access_token);
				Map<String, String> userMap = commonService.getUserInfoByToken(tokenId);

				resultMap.put("status", "true");
				if(userMap!=null)
					resultMap.put("userCode", userMap.get("client_id"));
				else {
					resultMap.put("userCode", "");
				}
				resultMap.put("errorCode", "0");
				resultMap.put("errorMessage", "");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}

		return resultMap;
	}

	/**
	 * 统计页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisPage")
	public ModelAndView weekLeadStatisPage(SjxxDto sjxxDto, HttpServletRequest request){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_jsrq");
		boolean checkSjgn=commonService.checkSign(sjxxDto.getSign(),
	    sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), request);
		if(!checkSjgn) {
			mav=commonService.jumpError(); return mav;
		}
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查询合作伙伴分类信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		//服务器页面调用，标记位为0
		String load_flg="0";
		mav.addObject("xszbDtos",xszbDtos);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 统计页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisPageByJsrq")
	public ModelAndView weekLeadStatisPageByJsrq(SjxxDto sjxxDto, HttpServletRequest request){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_jsrq");
		boolean checkSjgn=commonService.checkSign(sjxxDto.getSign(),
	    sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), request);
		if(!checkSjgn) {
			mav=commonService.jumpError(); return mav;
		}
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查询合作伙伴分类信息
//		 List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		//服务器页面调用，标记位为0
		String load_flg="0";
		mav.addObject("xszbDtos",xszbDtos);
//		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("load_flg",load_flg);
		//查平台基础数据
		JcsjDto jcsj_zbfl = new JcsjDto();
		jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
		jcsj_zbfl.setCsdm("TJXSTJ");
		jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
		JcsjDto jcsj_t = new JcsjDto();
		jcsj_t.setFcsid(jcsj_zbfl.getCsid());
		List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
		mav.addObject("jcsjXszfls", tjxs_zfls);
		return mav;
	}

	/**
	 * 返回统计页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/backweekLeadStatisPage")
	public ModelAndView backweekLeadStatisPage(SjxxDto sjxxDto, HttpServletRequest request){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead");
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查询合作伙伴分类信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		String load_flg="1";
		mav.addObject("xszbDtos",xszbDtos);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 返回统计页面(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/backweekLeadStatisPageByJsrq")
	public ModelAndView backweekLeadStatisPageByJsrq(SjxxDto sjxxDto, HttpServletRequest request){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_jsrq");
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查平台基础数据
		JcsjDto jcsj_zbfl = new JcsjDto();
		jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
		jcsj_zbfl.setCsdm("TJXSTJ");
		jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
		JcsjDto jcsj_t = new JcsjDto();
		jcsj_t.setFcsid(jcsj_zbfl.getCsid());
		List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
		mav.addObject("jcsjXszfls", tjxs_zfls);
		//查询合作伙伴分类信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		String load_flg="1";
		mav.addObject("xszbDtos",xszbDtos);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 统计伙伴信息页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisPartnerPage")
	public ModelAndView weekLeadStatisPartnerPage(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto,String load_flg){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_partner");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		//查询合作伙伴信息
		//List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getTjDtoList(sjhbxxDto);
		List<Map<String, String>> sjhbxxDtos= sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
		String dbs = "";
		String tldb = "";
		String tlnum = "";

		for (Map<String, String> mp : sjhbxxDtos) {
			if (StringUtil.isNotBlank(mp.get("db"))
					&& StringUtil.isNotBlank(mp.get("totalnum"))) {
				tldb = tldb + mp.get("db") + ",";
				tlnum = tlnum + mp.get("totalnum") + ",";
			}
		}
		if (StringUtil.isNotBlank(tldb)) {
			mav.addObject("tldb", tldb.substring(0, tldb.length() - 1));
			mav.addObject("tlnum", tlnum.substring(0, tlnum.length() - 1));

		}

		if(sjhbxxDtos!=null&&sjhbxxDtos.size()>0) {
			for(Map<String, String> m:sjhbxxDtos) {
				dbs = dbs+m.get("db")+",";
			}
			if(StringUtil.isNotBlank(dbs))
				dbs = dbs.substring(0, dbs.length()-1);
		}
		mav.addObject("dbhbs",dbs);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 统计伙伴信息页面(接收日期)
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/weekLeadStatisPartnerPageByJsrq")
	public ModelAndView weekLeadStatisPartnerPageByJsrq(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto,String load_flg,String method){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_partner_jsrq");
		if (redisUtil.getExpire("weekLeadStatis")==-2){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		String jsrqstart = sjxxDto.getJsrqstart();
		String jsrqend = sjxxDto.getJsrqend();
		if (StringUtil.isBlank(sjxxDto.getZq())) {
			sjxxDto.setZq("7");
		}
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		List<Map<String, String>> sjhbxxDtos;
		//查询合作伙伴信息
		String dbs = "";
		String tldb = "";
		String tlnum = "";
		if ("getFlByWeek".equals(method)) {
			//周 getFlByWeek
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.getZqs().put("hbztj",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			sjxxDto.setTj("week");
			List<Map<String, String>> lsSjhbxxDtos;
			String key = "jsrq_getAllhbByWeek"+"_"+sjxxDto.getZq();
			Object getAllhbByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByWeek!=null){
				lsSjhbxxDtos = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByWeek);
			}else {
				lsSjhbxxDtos = sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(lsSjhbxxDtos));
			}
			sjhbxxDtos = lsSjhbxxDtos;
		}else if ("getFlByMon".equals(method)){
            //月 getFlByMon
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			sjxxDto.setTj("mon");
			List<Map<String, String>> lsSjhbxxDtos;
			String key = "jsrq_getAllhbByMon"+"_"+sjxxDto.getZq();
			Object getAllhbByMon = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByMon!=null){
				lsSjhbxxDtos = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByMon);
			}else {
				lsSjhbxxDtos = sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(lsSjhbxxDtos));
			}
			sjhbxxDtos = lsSjhbxxDtos;
        }else if ("getFlByYear".equals(method)) {
			//年 getFlByYear
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> lsSjhbxxDtos;
			String key = "jsrq_getAllhbByYear"+"_"+sjxxDto.getZq();
			Object getAllhbByYear = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByYear!=null){
				lsSjhbxxDtos = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByYear);
			}else {
				lsSjhbxxDtos = sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(lsSjhbxxDtos));
			}
			sjhbxxDtos = lsSjhbxxDtos;
        }else {
			//日 getFlByDay或不传
			sjhbxxDtos= sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
		}
		for (Map<String, String> mp : sjhbxxDtos) {
			if (StringUtil.isNotBlank(mp.get("db"))
					&& StringUtil.isNotBlank(mp.get("totalnum"))) {
				tldb = tldb + mp.get("db") + ",";
				tlnum = tlnum + mp.get("totalnum") + ",";
			}
		}
		if (StringUtil.isNotBlank(tldb)) {
			mav.addObject("tldb", tldb.substring(0, tldb.length() - 1));
			mav.addObject("tlnum", tlnum.substring(0, tlnum.length() - 1));
		}
		if(sjhbxxDtos!=null&&sjhbxxDtos.size()>0) {
			for(Map<String, String> m:sjhbxxDtos) {
				dbs = dbs+m.get("db")+",";
			}
			if(StringUtil.isNotBlank(dbs))
				dbs = dbs.substring(0, dbs.length()-1);
		}
		mav.addObject("dbhbs",dbs);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		sjxxDto.setJsrqstart(jsrqstart);
		sjxxDto.setJsrqend(jsrqend);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		mav.addObject("method",method);
		return mav;
	}

	/**
	 * 其他统计界面
	 * @param sjxxDto
	 * @param load_flg
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisOtherPage")
	public ModelAndView weekLeadStatisOtherPage(SjxxDto sjxxDto,String load_flg) {
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_other");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
//		JcsjDto jcsjDto=new JcsjDto();
//		jcsjDto.setJclb(BasicDataTypeEnum.PROVINCE.getCode());
		//List<JcsjDto> sfDtos=jcsjService.getDtoList(jcsjDto);
		List<Map<String, String>> sf_list = sjxxStatisticService.getSfStatisByWeek(sjxxDto);
		mav.addObject("sfDtos", sf_list);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 其他统计界面(接收日期)
	 * @param sjxxDto
	 * @param load_flg
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisOtherPageByJsrq")
	public ModelAndView weekLeadStatisOtherPageByJsrq(SjxxDto sjxxDto,String load_flg) {
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_other_jsrq");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
//		JcsjDto jcsjDto=new JcsjDto();
//		jcsjDto.setJclb(BasicDataTypeEnum.PROVINCE.getCode());
		//List<JcsjDto> sfDtos=jcsjService.getDtoList(jcsjDto);
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx(dyxx.getCsid());
			}
			if ("HBFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx_q(dyxx.getCsid());
			}
		}
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}
		List<Map<String, String>> sf_list = sjxxStatisticService.getSfStatisByWeekAndJsrq(sjxxDto);
		mav.addObject("sfDtos", sf_list);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 统计检测单位信息页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisJcdwPage")
	public ModelAndView weekLeadStatisJcdwPage(SjxxDto sjxxDto,String load_flg){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_jcdw");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}

        List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssBySomeTimeAndJcdw(sjxxDto);
        mav.addObject("jcdwDtos", jcxmDRList);
        //字符串替换
  		if(jcxmDRList!=null && jcxmDRList.size()>0) {
  			for(int i=0;i<jcxmDRList.size();i++) {
  				jcxmDRList.get(i).put("jcdwmc",jcxmDRList.get(i).get("jcdwmc").replace("实验室", ""));
  			}
  		}
		mav.addObject("jcdwDtos", jcxmDRList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 统计检测单位信息页面(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisJcdwPageByJsrq")
	public ModelAndView weekLeadStatisJcdwPageByJsrq(SjxxDto sjxxDto,String load_flg){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_jcdw_jsrq");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}

        List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssBySomeTimeAndJcdwAndJsrq(sjxxDto);
        mav.addObject("jcdwDtos", jcxmDRList);
        //字符串替换
  		if(jcxmDRList!=null && jcxmDRList.size()>0) {
  			for(int i=0;i<jcxmDRList.size();i++) {
  				jcxmDRList.get(i).put("jcdwmc",jcxmDRList.get(i).get("jcdwmc").replace("实验室", ""));
  			}
  		}
		mav.addObject("jcdwDtos", jcxmDRList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 统计省份单位信息页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisProvincePage")
	public ModelAndView weekLeadStatisProvincePage(SjxxDto sjxxDto,String load_flg){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_sf");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		JcsjDto sfDto=jcsjService.getDtoById(sjxxDto.getSf());
		if(sfDto!=null)
			sjxxDto.setSfmc(sfDto.getCsmc());
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 统计省份单位信息页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisProvincePageByJsrq")
	public ModelAndView weekLeadStatisProvincePageByJsrq(SjxxDto sjxxDto,String load_flg){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_sf_jsrq");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		JcsjDto sfDto=jcsjService.getDtoById(sjxxDto.getSf());
		if(sfDto!=null)
			sjxxDto.setSfmc(sfDto.getCsmc());
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 查询伙伴信息
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadStatisPartner")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatisPartner(HttpServletRequest req,SjxxDto sjxxDto, SjhbxxDto sjhbxxDto){
		Map<String, Object> map = new HashMap<>();
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getDtoList(sjhbxxDto);
		map.put("sjhbxxDtos", sjhbxxDtos);
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		map.put("sjxxDto", sjxxDto);
		return map;
	}

	/**
	 * 查询汇报领导的统计
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/getWeekLeadStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatis(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		if (sjxxDto.getZq()==null) {
			sjxxDto.setZq("00");
		}
		if (redisUtil.getExpire("weekLeadStatis")==-2){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		//标本类型统计
		Map<String, Object> map = new HashMap<>();
		sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		List<Map<String, String>> yblxList;
		String key = "syrq_ybtj"+"_"+sjxxDto.getZq();
		Object syrq_ybtj = redisUtil.hget("weekLeadStatis", key);
		if (syrq_ybtj !=null){
			yblxList = (List<Map<String,String>>) JSONArray.parse((String) syrq_ybtj);
		}else {
			yblxList = sjxxService.getStatisByYblx(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(yblxList));
		}
		map.put("ybtj", yblxList);
		//合作伙伴统计
		sjxxDto.setTj("sjdw");
		sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		List<Map<String, String>> sjhbList;
		key = "syrq_dbtj"+"_"+sjxxDto.getZq();
		Object syrq_dbtj = redisUtil.hget("weekLeadStatis", key);
		if (syrq_dbtj !=null){
			sjhbList = (List<Map<String,String>>) JSONArray.parse((String) syrq_dbtj);
		}else {
			sjhbList = sjxxService.getStatisWeekBysjhb(sjxxDto);
			redisUtil.hset("weekLeadStatis", key, JSON.toJSONString(sjhbList));
		}
		map.put("dbtj", sjhbList);

		//标本日期统计
		sjxxDto.setTj("day");
		List<Map<String, String>> rqList;
		key = "syrq_rqtj"+"_"+sjxxDto.getZq();
		Object syrq_rqtj = redisUtil.hget("weekLeadStatis", key);
		if (syrq_rqtj !=null){
			rqList = (List<Map<String,String>>) JSONArray.parse((String) syrq_rqtj);
			map.put("rqtj",rqList);
		}else {
			rqList=sjxxService.getStatisByDate(sjxxDto);
			List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
			List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
			if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
				for (int i = 0; i <sjbglist.size(); i++){
					rqList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
					rqList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
				}
				map.put("rqtj",rqList);
				redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(rqList));
			}
		}

		//标本周统计
		List<Map<String, String>> ybsList;
		key = "syrq_weektj"+"_"+sjxxDto.getZq();
		Object syrq_weektj = redisUtil.hget("weekLeadStatis", key);
		if (syrq_weektj !=null){
			ybsList = (List<Map<String,String>>) JSONArray.parse((String) syrq_weektj);
		}else {
			ybsList=sjxxService.getStatisWeekYbsByJsrq(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(ybsList));
		}
		map.put("weektj", ybsList);

		//送检情况统计
		List<Map<String, String>> ybqk;
		key = "syrq_ybqk"+"_"+sjxxDto.getZq();
		Object syrq_ybqk = redisUtil.hget("weekLeadStatis", key);
		if (syrq_ybqk !=null){
			ybqk = (List<Map<String,String>>) JSONArray.parse((String) syrq_ybqk);
		}else {
			ybqk=sjxxService.getSjxxBySy(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(ybqk));
		}
		map.put("ybqk", ybqk);

		sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());

		//rfs项目送检情况统计
		List<Map<String, String>> rfs;
		key = "syrq_rfs"+"_"+sjxxDto.getZq();
		sjxxDto.setJcxmdm("F");
		Object syrq_rfs = redisUtil.hget("weekLeadStatis", key);
		if (syrq_rfs !=null){
			rfs = (List<Map<String,String>>) JSONArray.parse((String) syrq_rfs);
		}else {
			rfs=sjxxService.getSjxxBySy(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(rfs));
		}
		map.put("rfs", rfs);
		sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//统计复检申请
		FjsqDto fjsqDto=new FjsqDto();
		fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
		fjsqDto.setLrsjend(sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());

		List<Map<String, String>> fjsqMap;
		key = "fjsq"+"_"+sjxxDto.getZq();
		Object fjsq = redisUtil.hget("weekLeadStatis", key);
		if (fjsq !=null){
			fjsqMap = (List<Map<String,String>>) JSONArray.parse((String) fjsq);
		}else {
			fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(fjsqMap));
		}
		map.put("fjsq", fjsqMap);

		//统计废弃标本
		sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());

		List<Map<String, String>> fqybMap;
		key = "fqyb"+"_"+sjxxDto.getZq();
		Object fqyb = redisUtil.hget("weekLeadStatis", key);
		if (fqyb !=null){
			fqybMap = (List<Map<String,String>>) JSONArray.parse((String) fqyb);
		}else {
			fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(fqybMap));
		}
		map.put("fqyb", fqybMap);

		//标本信息检测总条数
		List<Map<String, String>> jcxmnum;
		key = "syrq_jcxmnum"+"_"+sjxxDto.getZq();
		Object syrq_jcxmnum = redisUtil.hget("weekLeadStatis", key);
		if (syrq_jcxmnum !=null){
			jcxmnum = (List<Map<String,String>>) JSONArray.parse((String) syrq_jcxmnum);
		}else {
			jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(jcxmnum));
		}
		sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("jcxmnum", jcxmnum);

		//增加合作医院数，科室数，医生数的统计表
		List<Map<String, String>> sjdwOfsjysOfks;
		key = "syrq_sjdwOfsjysOfks"+"_"+sjxxDto.getZq();
		Object syrq_sjdwOfsjysOfks = redisUtil.hget("weekLeadStatis", key);
		if (syrq_sjdwOfsjysOfks !=null){
			sjdwOfsjysOfks = (List<Map<String,String>>) JSONArray.parse((String) syrq_sjdwOfsjysOfks);
		}else {
			sjdwOfsjysOfks=sjxxService.getSjdwSjysKs(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(sjdwOfsjysOfks));
		}
		sjxxDto.getZqs().put("sjdwOfsjysOfks", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("sjdwOfsjysOfks", sjdwOfsjysOfks);

		//增加科室的圆饼统计图
		List<Map<String, String>> ksList;
		key = "syrq_ksList"+"_"+sjxxDto.getZq();
		Object syrq_ksList = redisUtil.hget("weekLeadStatis", key);
		if (syrq_ksList !=null){
			ksList = (List<Map<String,String>>) JSONArray.parse((String) syrq_ksList);
		}else {
			ksList=sjxxService.getKsByweek(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(ksList));
		}
		sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("ksList", ksList);

		//收费标本下边检测项目的总条数
		List<Map<String, String>> ybxxType;
		key = "syrq_ybxxType"+"_"+sjxxDto.getZq();
		Object syrq_ybxxType = redisUtil.hget("weekLeadStatis", key);
		if (syrq_ybxxType !=null){
			ybxxType = (List<Map<String,String>>) JSONArray.parse((String) syrq_ybxxType);
		}else {
			ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);//涉及实验日期，需修改
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(ybxxType));
		}
		sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("ybxxType", ybxxType);

		//统计上上周的临床反馈结果
		List<Map<String, String>> lcfkList;
		key = "lcfkList"+"_"+sjxxDto.getZq();
		Object o_lcfkList = redisUtil.hget("weekLeadStatis", key);
		if (o_lcfkList !=null){
			lcfkList = (List<Map<String,String>>) JSONArray.parse((String) o_lcfkList);
		}else {
			lcfkList=sjxxService.getLcfkByBefore(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(lcfkList));
		}
		sjxxDto.getZqs().put("lcfkList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("lcfkList", lcfkList);

		//周报增加标本类型前三的阳性率
		List<Map<String, String>> jyjgOfYblx;
		key = "jyjgOfYblx"+"_"+sjxxDto.getZq();
		Object o_jyjgOfYblx = redisUtil.hget("weekLeadStatis", key);
		if (o_jyjgOfYblx !=null){
			jyjgOfYblx = (List<Map<String,String>>) JSONArray.parse((String) o_jyjgOfYblx);
		}else {
			jyjgOfYblx=sjxxService.getJyjgByYblx(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(jyjgOfYblx));
		}
		sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getBgrqstart() + "~" + sjxxDto.getBgrqend());
		map.put("jyjgOfYblx", jyjgOfYblx);
		//按分类查询
		sjxxDto.getZqs().put("fltj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		//按照合作伙伴查询出来的数据太多，改为一次性得到所需要的数据
		//Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByFl(sjxxDtos);
		Map<String,List<Map<String, String>>> fllists;
		key = "syrq_fllists"+"_"+sjxxDto.getZq();
		Object syrq_fllists = redisUtil.hget("weekLeadStatis", key);
		if (syrq_fllists!=null){
			fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) syrq_fllists);
		}else {
			fllists = sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllists));
		}
//		Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
		if(fllists!= null) {
			for(String map1 : fllists.keySet()){
				map.put("fltj_"+map1,fllists.get(map1));
			}
		}
		sjxxDto.setTj("day");
		//rfs各周期平均用时
		List<Map<String, String>> rfspjys;
		key = "rfspjys"+"_"+sjxxDto.getZq();
		Object jsrq_rfspjys = redisUtil.hget("weekLeadStatis", key);
		if (jsrq_rfspjys !=null){
			rfspjys = (List<Map<String,String>>) JSONArray.parse((String) jsrq_rfspjys);
		}else {
			rfspjys = sjxxResStatisticService.getAvgTime(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(rfspjys));
		}
		map.put("rfspjys", rfspjys);
		sjxxDto.getZqs().put("rfspjys", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//rfs周期用时
		List<Map<String, String>> rfszq;
		key = "rfszq"+"_"+sjxxDto.getZq();
		Object jsrq_rfszq = redisUtil.hget("weekLeadStatis", key);
		if (jsrq_rfszq !=null){
			rfszq = (List<Map<String,String>>) JSONArray.parse((String) jsrq_rfszq);
		}else {
			rfszq = sjxxResStatisticService.getLifeCycle(sjxxDto);
			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(rfszq));
		}
		map.put("rfszq", rfszq);
		sjxxDto.getZqs().put("rfszq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//销售达成率，初始化按当前季度显示
//		XszbDto xszbDto = new XszbDto();
//		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//		String start = currentYear;
//		String end = currentYear;
//		xszbDto.setKszq(start);
//		xszbDto.setJszq(end);
//		xszbDto.setJsmc("大区经理");
//		xszbDto.setZblxcsmc("Q");
//		Map<String, Object> salesAttainmentRate = new HashMap<>();
//		key = "syrq_salesAttainmentRate"+"_"+sjxxDto.getZq();
//		Object syrq_salesAttainmentRate = redisUtil.hget("weekLeadStatis", key);
//		if (syrq_salesAttainmentRate!=null){
//			salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) syrq_salesAttainmentRate);
//		}else {
//			salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
//			redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
//		}
////		Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);//涉及实验日期，需修改，redis操作在方法中
//		if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
//			for (String map1 : salesAttainmentRate.keySet()) {
//				map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
//			}
//		}
//		sjxxDto.getZqs().put("syrq_salesAttainmentRate", xszbDto.getKszq() + "~" + xszbDto.getJszq());
		map.put("searchData", sjxxDto);
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatis")==-1){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		return map;
	}
	/**
	 * 查询汇报领导的统计(接收日期)
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/getWeekLeadStatisByJsrq")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatisByJsrq(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		if (sjxxDto.getZq()==null) {
			sjxxDto.setZq("00");
		}
		if (redisUtil.getExpire("weekLeadStatisDefault")==-2){
			redisUtil.hset("weekLeadStatisDefault","ExpirationTime","1440min",86400);
		}
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx(dyxx.getCsid());
			}
			if ("HBFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx_q(dyxx.getCsid());
			}
		}
		List<String> yxxList = new ArrayList<>();
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				yxxList = yxxs;
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}
		Map<String, Object> map = new HashMap<>();
		sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.setTjtj("NOW");
		String key;
//		//标本类型统计   这个统计由于js和html都没有，故后台注释
//		List<Map<String, String>> yblxList = new ArrayList<>();
//		String key = "jsrq_ybtj"+"_"+sjxxDto.getZq();
//		Object jsrq_ybtj = redisUtil.hget("weekLeadStatis", key);
//		if (jsrq_ybtj !=null){
//			yblxList = (List<Map<String,String>>) JSONArray.parse((String) jsrq_ybtj);
//		}else {
//			yblxList = sjxxService.getStatisByYblxAndJsrq(sjxxDto);
//			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(yblxList));
//		}
//		map.put("ybtj", yblxList);

		// //合作伙伴统计
		// sjxxDto.setTj("sjdw");
		// sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		// List<Map<String, String>> sjhbList = new ArrayList<>();
		// key = "jsrq_dbtj"+"_"+sjxxDto.getZq();
		// Object jsrq_dbtj = redisUtil.hget("weekLeadStatis", key);
		// if (jsrq_dbtj !=null){
		// 	sjhbList = (List<Map<String,String>>) JSONArray.parse((String) jsrq_dbtj);
		// }else {
		// 	sjhbList = sjxxService.getStatisWeekBysjhbAndJsrq(sjxxDto);
		// 	redisUtil.hset("weekLeadStatis", key, JSON.toJSONString(sjhbList));
		// }
		// map.put("dbtj", sjhbList);

		//标本日期统计
		/*sjxxDto.setTj("day");
		List<Map<String, String>> rqList = new ArrayList<>();
		if (redisUtil.hget("weekLeadStatis","jsrq_rqtj") !=null){
			rqList = (List<Map<String,String>>) JSONArray.parse((String) redisUtil.hget("weekLeadStatis", "jsrq_rqtj"));
			map.put("rqtj",rqList);
		}else {
			rqList=sjxxService.getStatisByDateAndJsrq(sjxxDto);
			List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
			List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
			if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
				for (int i = 0; i <sjbglist.size(); i++){
					rqList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
					rqList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
				}
				map.put("rqtj",rqList);
				redisUtil.hset("weekLeadStatis","jsrq_rqtj",JSON.toJSONString(rqList));
			}
		}*/
		String jsrqStart=sjxxDto.getJsrqstart();
		sjxxDto.setTj("mon");
		setDateByMonth(sjxxDto,-6);
		List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		sjxxDto.setJsrqstart(jsrqStart);
		//标本周统计
		/*List<Map<String, String>> ybsList = new ArrayList<>();
		if (redisUtil.hget("weekLeadStatis","jsrq_weektj") !=null){
			ybsList = (List<Map<String,String>>) JSONArray.parse((String) redisUtil.hget("weekLeadStatis", "jsrq_weektj"));
		}else {
			ybsList=sjxxService.getStatisWeekYbsByJsrqAndJsrq(sjxxDto);
			redisUtil.hset("weekLeadStatis","jsrq_weektj",JSON.toJSONString(ybsList));
		}
		map.put("weektj", ybsList);*/
		//rfs项目送检情况统计
//		List<Map<String, String>> rfs = new ArrayList<>();
//		key = "jsrq_rfs"+"_"+sjxxDto.getZq();
//		sjxxDto.setJcxmdm("F");
//		Object jsrq_rfs = redisUtil.hget("weekLeadStatis", key);
//		if (jsrq_rfs !=null){
//			rfs = (List<Map<String,String>>) JSONArray.parse((String) jsrq_rfs);
//		}else {
//			String[] strings = sjxxDto.getYxxs();
//			sjxxDto.setYxxs(null);
//			rfs=sjxxService.getSjxxBySyAndJsrq(sjxxDto);
//			sjxxDto.setYxxs(strings);
//			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(rfs));
//		}
//		map.put("rfs", rfs);
//		sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//全国趋势
		List<Map<String, String>> qgqsnums;
		key = "jsrq_qgqsnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_qgqsnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_qgqsnums !=null){
			qgqsnums = (List<Map<String,String>>) JSONArray.parse((String) jsrq_qgqsnums);
		}else {
			qgqsnums = sjxxTwoService.getAllCountryChanges(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(qgqsnums));
		}
		map.put("qgqs", qgqsnums);
		sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//产品趋势
		List<Map<String, Object>> cpqstjnums;
		key = "jsrq_cpqstjnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_cpqstjnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_cpqstjnums !=null){
			cpqstjnums = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_cpqstjnums);
		}else {
			cpqstjnums = sjxxTwoService.getProductionChanges(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpqstjnums));
		}
		map.put("cpqstj", cpqstjnums);
		sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//平台趋势
		List<Map<String, Object>> ptqstjnums;
		key = "jsrq_ptqstjnums"+"_"+sjxxDto.getZq();
		Object jsrq_ptqstjnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_ptqstjnums !=null){
			ptqstjnums = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_ptqstjnums);
		}else {
			ptqstjnums = sjxxTwoService.getPlatformChanges(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ptqstjnums));
		}
		map.put("ptqstj", ptqstjnums);
		sjxxDto.getZqs().put("ptqstjzqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("ptqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//平台销售达成率
		XszbDto xszbDto1 = new XszbDto();
		String currentYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		Calendar calendar = Calendar.getInstance();
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		int currenMonth;
		if (monthDay>7){
			currenMonth = calendar.get(Calendar.MONTH)+1;
		}else {
			currenMonth = calendar.get(Calendar.MONTH);
		}
		String monthEnd = String.valueOf((currenMonth+2)/3*3);
		String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
		String start1 = currentYear1 + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
		String end1 = currentYear1 + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
		xszbDto1.setKszq(start1);
		xszbDto1.setJszq(end1);
		xszbDto1.setZblxcsmc("Q");
		yxxList.add("Resfirst");
		xszbDto1.setXms(yxxList);
		xszbDto1.setYwlx(sjxxDto.getYwlx());
		List<Map<String, Object>> ptzbdclnums;
		key = "jsrq_ptzbdcl"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_ptzbdclnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_ptzbdclnums!=null){
			ptzbdclnums = (List<Map<String, Object>>) JSONArray.parse((String) jsrq_ptzbdclnums);
		}else {
			ptzbdclnums = sjxxStatisticService.getPtzbdcl(xszbDto1);
			redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ptzbdclnums));
		}
		map.put("ptzbdcl", ptzbdclnums);
		sjxxDto.getZqs().put("ptzbdcl", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
		sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//平台业务占比
		List<Map<String, Object>> ptywzbtjnums;
		xszbDto1.setYwlx(sjxxDto.getYwlx());
		xszbDto1.setYwlx_q(sjxxDto.getYwlx_q());
		xszbDto1.setXms(Arrays.asList(sjxxDto.getYxxs()));
		key = "jsrq_ptywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_ptywzbtjnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_ptywzbtjnums !=null){
			ptywzbtjnums = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_ptywzbtjnums);
		}else {
			ptywzbtjnums = sjxxTwoService.getPlatformProportion(xszbDto1);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ptywzbtjnums));
		}
		map.put("ptywzbtj", ptywzbtjnums);
		sjxxDto.getZqs().put("ptywzbtjzqs", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
		sjxxDto.getZqs().put("ptywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//产品业务占比
		List<Map<String, Object>> cpywzbtjnums;
		xszbDto1.setYwlx(sjxxDto.getYwlx());
		xszbDto1.setYwlx_q(sjxxDto.getYwlx_q());
		xszbDto1.setXms(Arrays.asList(sjxxDto.getYxxs()));
		key = "jsrq_cpywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_cpywzbtjnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_cpywzbtjnums !=null){
			cpywzbtjnums = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_cpywzbtjnums);
		}else {
			cpywzbtjnums = sjxxTwoService.getProductionProportion(xszbDto1);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpywzbtjnums));
		}
		map.put("cpywzbtj", cpywzbtjnums);
		sjxxDto.getZqs().put("cpywzbtjzqs", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
		sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//伙伴分类测试数占比
		XxdyDto xxdyDto=new XxdyDto();
		xxdyDto.setCskz1("JCSJ");
		xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
		List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
		sjxxDto.getZqs().put("hbflcsszbzqs", start1 + "~" + end1);
		sjxxDto.getZqs().put("hbflcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		List<Map<String, String>> newList=new ArrayList<>();
		if(xxdyDtos!=null&&xxdyDtos.size()>0){
			SjxxDto sjxxDto_t=new SjxxDto();
			sjxxDto_t.setJsrqstart(start1);
			sjxxDto_t.setJsrqend(end1);
			sjxxDto_t.setYwlx(sjxxDto.getYwlx());
			sjxxDto_t.setYwlx_q(sjxxDto.getYwlx_q());
			sjxxDto_t.setYxxs(sjxxDto.getYxxs());
			List<Map<String, String>> hbflcsszbMap;
			key = "jsrq_getHbflcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object hbflcssRedis = redisUtil.hget("weekLeadStatisDefault", key);
			if (hbflcssRedis!=null){
				hbflcsszbMap = (List<Map<String, String>>) JSONArray.parse((String) hbflcssRedis);
				newList = hbflcsszbMap;
			}else {
				sjxxDto_t.setXg_flg("1");
				hbflcsszbMap = sjxxStatisticService.getHbflcsszb(sjxxDto_t);
				for(XxdyDto xxdyDto_t:xxdyDtos){
					boolean isFind=false;
					for(Map<String, String> map_t:hbflcsszbMap){
						if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))){
							newList.add(map_t);
							isFind=true;
							break;
						}
					}
					if(!isFind){
						Map<String, String> newMap=new HashMap<>();
						newMap.put("num","0");
						newMap.put("yxx",xxdyDto_t.getYxx());
						newList.add(newMap);
					}
				}
				redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(newList));
			}
		}
		map.put("hbflcsszb", newList);

		//送检区分测试数占比
		List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		sjxxDto.getZqs().put("sjqfcsszbzqs", start1 + "~" + end1);
		sjxxDto.getZqs().put("sjqfcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		key = "jsrq_getSjqfcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object sjqfcssRedis = redisUtil.hget("weekLeadStatisDefault", key);
		List<Map<String, String>> sjqfList=new ArrayList<>();
		if (sjqfcssRedis!=null){
			sjqfList = (List<Map<String, String>>) JSONArray.parse((String) sjqfcssRedis);
		}else {
			JcsjDto jcsjDto1 = new JcsjDto();
			jcsjDto1.setCsid("第三方实验室");
			jcsjDto1.setCsmc("第三方实验室");
			sjqfs.add(jcsjDto1);
			JcsjDto jcsjDto2 = new JcsjDto();
			jcsjDto2.setCsid("直销");
			jcsjDto2.setCsmc("直销");
			sjqfs.add(jcsjDto2);
			JcsjDto jcsjDto3 = new JcsjDto();
			jcsjDto3.setCsid("CSO");
			jcsjDto3.setCsmc("CSO");
			sjqfs.add(jcsjDto3);
			if(sjqfs!=null&&sjqfs.size()>0){
				SjxxDto sjxxDto_t=new SjxxDto();
				sjxxDto_t.setJsrqstart(start1);
				sjxxDto_t.setJsrqend(end1);
				sjxxDto_t.setYwlx(sjxxDto.getYwlx());
				sjxxDto_t.setYxxs(sjxxDto.getYxxs());
				sjxxDto_t.setNewflg("zb");
				List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto_t);
				for(JcsjDto jcsjDto:sjqfs){
//					boolean isFind=false;
					for(Map<String, String> map_t:sjqfcsszb){
						if(jcsjDto.getCsid().equals(map_t.get("sjqf"))){
							map_t.put("sjqfmc",jcsjDto.getCsmc());
							sjqfList.add(map_t);
//							isFind=true;
							break;
						}
					}
					// if(!isFind){
					// 	Map<String, String> newMap=new HashMap<>();
					// 	newMap.put("num","0");
					// 	newMap.put("sjqfmc",jcsjDto.getCsmc());
					// 	sjqfList.add(newMap);
					// }
				}
			}
			redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(sjqfList));
		}
		map.put("sjqfcsszb", sjqfList);
		//销售性质测试数趋势
		sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getRqs().get(0) + "~" + sjxxDto.getRqs().get(sjxxDto.getRqs().size()-1));
		sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
		key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object xsxzcssqszqsRedis = redisUtil.hget("weekLeadStatisDefault", key);
		if (xsxzcssqszqsRedis!=null){
			xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) xsxzcssqszqsRedis);
		}else {
			// JcsjDto jcsjDto1 = new JcsjDto();
			// jcsjDto1.setCsid("第三方实验室");
			// jcsjDto1.setCsmc("第三方实验室");
			// sjqfs.add(jcsjDto1);
			// JcsjDto jcsjDto2 = new JcsjDto();
			// jcsjDto2.setCsid("直销");
			// jcsjDto2.setCsmc("直销");
			// sjqfs.add(jcsjDto2);
			// JcsjDto jcsjDto3 = new JcsjDto();
			// jcsjDto3.setCsid("CSO");
			// jcsjDto3.setCsmc("CSO");
			// sjqfs.add(jcsjDto3);
			if(sjqfs!=null&&sjqfs.size()>0){
				sjxxDto.setNewflg("qs");
				List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
				for(int i=0;i<rqs.size();i++){
					for(JcsjDto jcsjDto:sjqfs){
						boolean isFind=false;
						for(Map<String, String> map_t:sjqfcsszb){
							if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
								map_t.put("sjqfmc",jcsjDto.getCsmc());
								if(i==0){
									map_t.put("bl","0");
								}else{
									var num="0";
									for(Map<String, String> stringMap:xsxzcssqsList){
										if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
											num=stringMap.get("num");
										}
									}
									BigDecimal bigDecimal=new BigDecimal(num);
									BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
									map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
								}
								xsxzcssqsList.add(map_t);
								isFind=true;
								break;
							}
						}
						if(!isFind){
							Map<String, String> newMap=new HashMap<>();
							newMap.put("rq",rqs.get(i));
							newMap.put("num","0");
							newMap.put("sjqfmc",jcsjDto.getCsmc());
							newMap.put("sjqf",jcsjDto.getCsid());
							if(i==0){
								newMap.put("bl","0");
							}else{
								var num="0";
								for(Map<String, String> stringMap:xsxzcssqsList){
									if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
										num=stringMap.get("num");
									}
								}
								BigDecimal bigDecimal=new BigDecimal(num);
								BigDecimal bigDecimal1=new BigDecimal("0");
								newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
							}
							xsxzcssqsList.add(newMap);
						}
					}
				}
			}
			redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(xsxzcssqsList));
		}

		map.put("xsxzcssqs", xsxzcssqsList);

		//业务发展部达成率
		XszbDto xszbDto_t = new XszbDto();
		String currentYear_t = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		Calendar calendar_t = Calendar.getInstance();
		int currenMonth_t;
		if (monthDay>7){
			currenMonth_t = calendar_t.get(Calendar.MONTH)+1;
		}else {
			currenMonth_t = calendar_t.get(Calendar.MONTH);
		}
		String monthEnd_t = String.valueOf((currenMonth_t+2)/3*3);
		String monthStart_t = String.valueOf(((currenMonth_t-1)/3*3)+1);
		String start1_t = currentYear_t + "-" + ( monthStart_t.length()<2? ("0"+monthStart_t):monthStart_t);
		String end1_t = currentYear_t + "-" + (monthEnd_t.length()<2? ("0"+monthEnd_t):monthEnd_t);
		xszbDto_t.setKszq(start1_t);
		xszbDto_t.setJszq(end1_t);
		xszbDto_t.setZblxcsmc("Q");
		xszbDto_t.setXms(yxxList);
		xszbDto_t.setYwlx(sjxxDto.getYwlx());
		List<Map<String, Object>> ywfzbnums;
		key = "jsrq_ywfzb"+"_"+xszbDto_t.getKszq() + "~" + xszbDto_t.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_ywfzbnums = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_ywfzbnums!=null){
			ywfzbnums = (List<Map<String, Object>>) JSONArray.parse((String) jsrq_ywfzbnums);
		}else {
			ywfzbnums = sjxxStatisticService.getYwfzbZbdcl(xszbDto_t);
			redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ywfzbnums));
		}
		map.put("ywzrfz", ywfzbnums);
		sjxxDto.getZqs().put("ywzrfz", xszbDto_t.getKszq() + "~" + xszbDto_t.getJszq());
		sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//统计复检申请
//		FjsqDto fjsqDto=new FjsqDto();
//		fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
//		fjsqDto.setLrsjend(sjxxDto.getJsrqend());
//		sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
//		List<Map<String, String>> fjsqMap = new ArrayList<>();
//		key = "fjsq"+"_"+sjxxDto.getZq();
//		Object fjsq = redisUtil.hget("weekLeadStatis", key);
//		if (fjsq!=null){
//			fjsqMap = (List<Map<String,String>>) JSONArray.parse((String) fjsq);
//		}else {
//			fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
//			redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(fjsqMap));
//		}
//		map.put("fjsq", fjsqMap);

		//统计废弃标本
		/*sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		List<Map<String, String>> fqybMap = new ArrayList<>();
		if (redisUtil.hget("weekLeadStatis","fqyb") !=null){
			fqybMap = (List<Map<String,String>>) JSONArray.parse((String) redisUtil.hget("weekLeadStatis", "fqyb"));
		}else {
			fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			redisUtil.hset("weekLeadStatis","fqyb",JSON.toJSONString(fqybMap));
		}
		map.put("fqyb", fqybMap);*/

		//标本信息检测总条数
		List<Map<String, String>> jcxmnum;
		key = "jsrq_jcxmnum"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_jcxmnum = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_jcxmnum !=null){
			jcxmnum = (List<Map<String,String>>) JSONArray.parse((String) jsrq_jcxmnum);
		}else {
			jcxmnum=sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(jcxmnum));
		}
		sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		map.put("jcxmnum", jcxmnum);

		// //增加合作医院数，科室数，医生数的统计表
		// List<Map<String, String>> sjdwOfsjysOfks = new ArrayList<>();
		// key = "jsrq_sjdwOfsjysOfks"+"_"+sjxxDto.getZq();
		// Object jsrq_sjdwOfsjysOfks = redisUtil.hget("weekLeadStatis", key);
		// if (jsrq_sjdwOfsjysOfks !=null){
		// 	sjdwOfsjysOfks = (List<Map<String,String>>) JSONArray.parse((String) jsrq_sjdwOfsjysOfks);
		// }else {
		// 	sjdwOfsjysOfks=sjxxService.getSjdwSjysKsByJsrq(sjxxDto);
		// 	redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(sjdwOfsjysOfks));
		// }
		// sjxxDto.getZqs().put("sjdwOfsjysOfks", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		// map.put("sjdwOfsjysOfks", sjdwOfsjysOfks);

		//增加科室的圆饼统计图
		// List<Map<String, String>> ksList = new ArrayList<>();
		// key = "jsrq_ksList"+"_"+sjxxDto.getZq();
		// Object jsrq_ksList = redisUtil.hget("weekLeadStatis", key);
		// if (jsrq_ksList !=null){
		// 	ksList = (List<Map<String,String>>) JSONArray.parse((String) jsrq_ksList);
		// }else {
		// 	ksList=sjxxService.getKsByweekAndJsrq(sjxxDto);
		// 	redisUtil.hset("weekLeadStatis",key,JSON.toJSONString(ksList));
		// }
		// sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		// map.put("ksList", ksList);

		//收费标本下边检测项目的总条数
		List<Map<String, String>> ybxxType;
		key = "jsrq_ybxxType"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_ybxxType = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_ybxxType !=null){
			ybxxType = (List<Map<String,String>>) JSONArray.parse((String) jsrq_ybxxType);
		}else {
			ybxxType=sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ybxxType));
		}
		sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		map.put("ybxxType", ybxxType);

		//统计上上周的临床反馈结果
		/*List<Map<String, String>> lcfkList = new ArrayList<>();
		if (redisUtil.hget("weekLeadStatis","lcfkList") !=null){
			lcfkList = (List<Map<String,String>>) JSONArray.parse((String) redisUtil.hget("weekLeadStatis", "lcfkList"));
		}else {
			lcfkList=sjxxService.getLcfkByBefore(sjxxDto);
			redisUtil.hset("weekLeadStatis","lcfkList",JSON.toJSONString(lcfkList));
		}
		sjxxDto.getZqs().put("lcfkList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("lcfkList", lcfkList);*/
//		//按分类查询
// 		sjxxDto.getZqs().put("fltj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
// 		//按照合作伙伴查询出来的数据太多，改为一次性得到所需要的数据
// 		//Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByFl(sjxxDtos);
// 		Map<String,List<Map<String, String>>> fllists = new HashMap<>();
// 		key = "jsrq_fllists"+"_"+sjxxDto.getZq();
// 		Object jsrq_fllists = redisUtil.hget("weekLeadStatis", key);
// 		if (jsrq_fllists!=null){
// 			fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) jsrq_fllists);
// 		}else {
// 			fllists = sjxxStatisticService.getStatisByFlLimitAndJsrq(sjxxDto);
// 			redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllists));
// 		}
// //		Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
// 		if(fllists!= null) {
// 			for(String map1 : fllists.keySet()){
// 				map.put("fltj_"+map1,fllists.get(map1));
// 			}
// 		}
		sjxxDto.setTj("week");
		//在service里进行加入 特检销售发展部 平台限制（直销，CSO）
		sjxxDto.setSingle_flag("1");
		//Top20第三方
		List<Map<String, String>> topDsf20;
		key = "jsrq_topDsf20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_topDsf20 = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_topDsf20 !=null){
			topDsf20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topDsf20);
		}else {
			topDsf20 = sjxxTwoService.getTopDsf20(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topDsf20));
		}
		map.put("topDsf20", topDsf20);
		sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//Top20直销
		List<Map<String, String>> topZx20;
		key = "jsrq_topZx20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_topZx20 = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_topZx20 !=null){
			topZx20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topZx20);
		}else {
			topZx20 = sjxxTwoService.getTopZx20(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topZx20));
		}
		map.put("topZx20", topZx20);
		sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//Top20CSO
		List<Map<String, String>> topCSO20;
		key = "jsrq_topCSO20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_topCSO20 = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_topCSO20 !=null){
			topCSO20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topCSO20);
		}else {
			sjxxDto.setXg_flg("1");//代表领导页面进入进入，用于判断CSOTOP图将其他平台的伙伴进行归类
			topCSO20 = sjxxTwoService.getTopCSO20(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topCSO20));
		}
		map.put("topCSO20", topCSO20);
		sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//Top20RY
		List<Map<String, String>> topRY20;
		key = "jsrq_topRY20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_topRY20 = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_topRY20 !=null){
			topRY20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topRY20);
		}else {
			topRY20 = sjxxTwoService.getTopRY20(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topRY20));
		}
		map.put("topRY20", topRY20);
		sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//2.5、TOP10核心医院排行
		List<Map<String, Object>> topHxyxList;
		key = "jsrq_topHxyxList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_topHxyxList = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_topHxyxList !=null){
			topHxyxList = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_topHxyxList);
		}else {
			topHxyxList = sjxxTwoService.getHxyyTopList(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topHxyxList));
		}
		map.put("topHxyxList", topHxyxList);
		sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//销售达成率，初始化按当前季度显示
//		XszbDto xszbDto = new XszbDto();
//		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//		String start = currentYear;
//		String end = currentYear;
//		xszbDto.setKszq(start);
//		xszbDto.setJszq(end);
//		xszbDto.setJsmc("大区经理");
//		xszbDto.setZblxcsmc("Q");
//		Map<String, Object> salesAttainmentRate = new HashMap<>();
//		key = "jsrq_salesAttainmentRate"+"_"+sjxxDto.getZq();
//		Object jsrq_salesAttainmentRate = redisUtil.hget("weekLeadStatis", key);
//		if (jsrq_salesAttainmentRate!=null){
//			salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) jsrq_salesAttainmentRate);
//		}else {
//			salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
//			redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
//		}
//		Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
//		if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
//			for (String map1 : salesAttainmentRate.keySet()) {
//				map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
//			}
//		}
//		sjxxDto.getZqs().put("salesAttainmentRate", xszbDto.getKszq() + "~" + xszbDto.getJszq());
		//特检销售各区域达成率，初始化按当前季度显示
		XszbDto tjXszb = new XszbDto();
		tjXszb.setZblxcsmc("Q");
		tjXszb.setKszq(xszbDto1.getKszq());
		tjXszb.setJszq(xszbDto1.getJszq());
		tjXszb.setXms(yxxList);
		tjXszb.setYwlx(sjxxDto.getYwlx());
//		Map<String,List<Map<String,String>>> tjxsmap = new HashMap<>();//查平台基础数据
		JcsjDto jcsj_zbfl = new JcsjDto();
		jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
		jcsj_zbfl.setCsdm("TJXSTJ");
		jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
		JcsjDto jcsj_t = new JcsjDto();
		jcsj_t.setFcsid(jcsj_zbfl.getCsid());
		tjXszb.setZbfl(jcsj_zbfl.getCsid());
		List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
		map.put("jcsjXszfls", tjxs_zfls);
		if (tjxs_zfls != null){
			for (JcsjDto jcsj : tjxs_zfls){
				List<Map<String,String>> list;
				tjXszb.setQyid(jcsj.getCsid());
				tjXszb.setCskz3(jcsj.getCskz1());
				tjXszb.setZbzfl(jcsj.getCsid());
				tjXszb.setCskz2(jcsj.getCskz2());//传递子分类的参数扩展2
				//由于外部统计数据，需要查找区域下的伙伴去限制外部统计数据范围
				List<String> hbids = xszbService.getDtosByZfl(tjXszb);
                tjXszb.setHbids(hbids);
				key = "jsrq_tjxsdcl"+"_"+jcsj.getCsid()+"_"+ tjXszb.getKszq()+ "~" + tjXszb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
				Object jsrq_tjxsdcl = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_tjxsdcl !=null){
					list = (List<Map<String,String>>) JSONArray.parse((String) jsrq_tjxsdcl);
				}else {
					list = sjxxStatisticService.getTjsxdcl(tjXszb);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(list));
				}
//				tjxsmap.put(jcsj.getCsid() , list);
				map.put("tjxsdcl_"+jcsj.getCsid(), list);
				sjxxDto.getZqs().put("tjxsdcl_"+jcsj.getCsid(), tjXszb.getKszq()+ "~" + tjXszb.getJszq());
				sjxxDto.getZqs().put("tjxsdclyxxs_"+jcsj.getCsid(), StringUtil.join(sjxxDto.getYxxs(),","));
			}
		}
		sjxxDto.setTj("mon");
		sjxxDto.setJsrqMstart(start1);
		sjxxDto.setJsrqMend(end1);
		//2.4、收费科室分布（按照收费测试数统计）
		List<Map<String, Object>> ksSfcssList;
		key = "jsrq_ksSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_ksSfcssList = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_ksSfcssList !=null){
			ksSfcssList = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_ksSfcssList);
		}else {
			ksSfcssList = sjxxTwoService.getChargesDivideByKs(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ksSfcssList));
		}
		map.put("ksSfcssList", ksSfcssList);
		sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("ksSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		//2.5、收费标本类型分布（按照收费测试数统计）
		List<Map<String, Object>> yblxSfcssList;
		key = "jsrq_yblxSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
		Object jsrq_yblxSfcssList = redisUtil.hget("weekLeadStatisDefault", key);
		if (jsrq_yblxSfcssList !=null){
			yblxSfcssList = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_yblxSfcssList);
		}else {
			yblxSfcssList = sjxxTwoService.getChargesDivideByYblx(sjxxDto);
			redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(yblxSfcssList));
		}
		map.put("yblxSfcssList", yblxSfcssList);
		sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		sjxxDto.getZqs().put("yblxSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		map.put("searchData", sjxxDto);
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatisDefault")==-1){
			redisUtil.hset("weekLeadStatisDefault","ExpirationTime","1440min",86400);
		}
		return map;
	}

	/**
	 * 查询汇报领导的伙伴统计
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadPartnerStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadPartnerStatis(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		Map<String, Object> map = new HashMap<>();

		//按伙伴查询
		String zqString = "0";
		if(StringUtil.isNotBlank(sjxxDto.getZq())) {
			zqString = sjxxDto.getZq().split(",")[0];
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		sjxxDto.setTj("day");
		sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString) + 1)));
		List<Map<String, String>> fllist=sjxxService.getStatisByTjHbfl(sjxxDto);
		map.put("hbztj", fllist);
		sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");

		for(String map1 : fllists.keySet()){
			map.put("hbtj_"+map1,fllists.get(map1));
		}
		map.put("searchData", sjxxDto);
		return map;
	}
	/**
	 * 查询汇报领导的伙伴统计(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadPartnerStatisByJsrq")
	@ResponseBody
	public Map<String, Object> getWeekLeadPartnerStatisByJsrq(SjxxDto sjxxDto,String method)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		if (StringUtil.isBlank(sjxxDto.getZq())) {
			sjxxDto.setZq("7");
		}
		Map<String, Object> map = new HashMap<>();

		//按伙伴查询
		String zqString = "0";
		if(StringUtil.isNotBlank(sjxxDto.getZq())) {
			zqString = sjxxDto.getZq().split(",")[0];
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, String>> fllist;
		if ("getFlByWeek".equals(method)) {
			//周 getFlByWeek
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.getZqs().put("hbztj",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			sjxxDto.setTj("week");
			fllist = sjxxService.getStatisByWeekTjHbflAndJsrq(sjxxDto);
		}else if ("getFlByMon".equals(method)){
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			sjxxDto.setTj("mon");
			fllist = sjxxService.getStatisByTjHbflAndJsrq(sjxxDto);
		}else if ("getFlByYear".equals(method)) {
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			fllist=sjxxService.getStatisByTjHbflAndJsrq(sjxxDto);
		} else {
			sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString) + 1)));
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("day");
			fllist=sjxxService.getStatisByTjHbflAndJsrq(sjxxDto);
		}
		map.put("hbztj", fllist);
		Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");

		for(String map1 : fllists.keySet()){
			map.put("hbtj_"+map1,fllists.get(map1));
		}
		map.put("searchData", sjxxDto);
		return map;
	}

	/**
	 * 查询汇报领导的省份统计
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadProvinceStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadProvinceStatis(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
        Map<String, Object> map = new HashMap<>();
        sjxxDto.setTj("day");
		List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);

		sjxxDto.setRqs(rqs);
		//送检情况统计
		List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
		map.put("sfybqk", ybqk);
		sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//标本信息检测总条数
		List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
		sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("sfcss", jcxmnum);
		//收费标本下边检测项目的总条数
		List<Map<String,String>> ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);
		sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("sfsfcss", ybxxType);

		map.put("searchData", sjxxDto);
		return map;
	}
	/**
	 * 查询汇报领导的省份统计(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadProvinceStatisByJsrq")
	@ResponseBody
	public Map<String, Object> getWeekLeadProvinceStatisByJsrq(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx(dyxx.getCsid());
			}
			if ("HBFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx_q(dyxx.getCsid());
			}
		}
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}
        Map<String, Object> map = new HashMap<>();
        sjxxDto.setTj("day");
		List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);

		sjxxDto.setRqs(rqs);
		//送检情况统计
		List<Map<String, String>> ybqk=sjxxService.getSjxxBySyAndJsrq(sjxxDto);
		map.put("sfybqk", ybqk);
		sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//标本信息检测总条数
		List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
		sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("sfcss", jcxmnum);
		//收费标本下边检测项目的总条数
		List<Map<String,String>> ybxxType=sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
		sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("sfsfcss", ybxxType);

		map.put("searchData", sjxxDto);
		return map;
	}

	/**
	 * 查询汇报领导的检测单位统计
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadJcdwStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadJcdwStatis(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		Map<String, Object> map = new HashMap<>();

		sjxxDto.setTj("day");
		//List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
        List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssBySomeTimeAndJcdw(sjxxDto);
		sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());

		map.put("jcdwtj", jcxmDRList);

		List<Map<String, String>> rqList=sjxxService.getStatisByDate(sjxxDto);
		List<String> rqs= new ArrayList<>();
		for(int i=0;i<rqList.size();i++) {
			rqs.add(rqList.get(i).get("rq"));
		}
		sjxxDto.setRqs(rqs);
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> jcsjDtos=jcsjService.getDtoList(jcsjDto);
		for(int i=0;i<jcsjDtos.size();i++) {
			sjxxDto.setJcdw(jcsjDtos.get(i).getCsid());
			List<Map<String, String>> fllists = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+jcsjDtos.get(i).getCsid(),fllists);
		}

		map.put("searchData", sjxxDto);
		return map;
	}
	/**
	 * 查询汇报领导的检测单位统计(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadJcdwStatisByJsrq")
	@ResponseBody
	public Map<String, Object> getWeekLeadJcdwStatisByJsrq(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		Map<String, Object> map = new HashMap<>();
		if (redisUtil.getExpire("weekLeadStatis")==-2){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		sjxxDto.setTj("day");
		//List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
        List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssBySomeTimeAndJcdwAndJsrq(sjxxDto);
		sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());

		map.put("jcdwtj", jcxmDRList);

		List<Map<String, String>> rqList=sjxxService.getStatisByDateAndJsrq(sjxxDto);
		List<String> rqs= new ArrayList<>();
		for(int i=0;i<rqList.size();i++) {
			rqs.add(rqList.get(i).get("rq"));
		}
		sjxxDto.setRqs(rqs);
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> jcsjDtos=jcsjService.getDtoList(jcsjDto);
		for(int i=0;i<jcsjDtos.size();i++) {
			sjxxDto.setJcdw(jcsjDtos.get(i).getCsid());
			List<Map<String, String>> fllists = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
			map.put("jcdwtj_"+jcsjDtos.get(i).getCsid(),fllists);
		}

		map.put("searchData", sjxxDto);
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatis")==-1){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		return map;
	}

	/**
	 * 查询汇报领导的统计
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadStatisByrq")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatisByrq(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();

//		String method = req.getParameter("method");
		map.put("searchData", sjxxDto);

//		if(StringUtil.isBlank(method))
//			return map;
//		else if("getHbdwByrq".equals(method)) {
//			//map = sjxxService.getSjdwBydb(sjxxDto);
//		}
		return map;
	}

	/**
	 * 提供给外部，用于发送消息接口
	 * @param req
	 * @return
	 */
	@RequestMapping("/pathogen/sendExternalMessage")
	@ResponseBody
	public Map<String, Object> sendExternalMessage(HttpServletRequest req, BufferedReader br){
		//request.getb
		String inputLine;
		String str = "";
		Map<String,Object> map = new HashMap<>();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
			if(StringUtil.isBlank(str)) {
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}else{
				boolean result = sjxxService.sendExternalMessage(str);
				if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "发送消息失败！");
					log.error("发送消息失败！");
				}
			}
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("errorCode", "IO获取数据异常！");
			log.error("IO获取数据异常！" + e.toString());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMsg());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 提供给外部，用于发送消息接口
	 * @param
	 * @return
	 */
	@RequestMapping("/pathogen/sendExternalMessageNew")
	@ResponseBody
	public Map<String, Object> sendExternalMessageNew(ExternalMessageModel externalMessageModel){
		Map<String,Object> map = new HashMap<>();
		try {
			if(null == externalMessageModel) {
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}else{
				boolean result = sjxxService.sendExternalMessageNew(externalMessageModel);
				if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "发送消息失败！");
					log.error("发送消息失败！");
				}
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMsg());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}


	/**
	 * 发送消息接口
	 * @param req
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/pathogen/sendOutMessage")
	@ResponseBody
	public Map<String, Object> sendOutMessage(HttpServletRequest req, SjxxDto sjxxDto, String title, String message){
		Map<String, Object> map = new HashMap<>();
		map.put("status", "Fail");
		if(sjxxDto == null ||StringUtil.isBlank(sjxxDto.getYbbh())){
			map.put("message", "Not Found ybbh !");
			return map;
		}
		if(StringUtil.isBlank(message)){
			map.put("message", "Not Found message !");
			return map;
		}
		if(StringUtil.isBlank(title)){
			map.put("message", "Not Found title !");
			return map;
		}
		try {
			//发送信息
			boolean isSuccess = sjxxService.sendOutMessage(sjxxDto, message, title);
			if(isSuccess) {
				map.put("status", "Success");
			}else {
				map.put("message", "消息发送失败 !");
			}
		} catch (BusinessException e) {
			map.put("message", e.getMsg());
		}
		return map;
	}

	/**
	 * 通过条件查询刷新页面
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/getSjxxStatisByTj")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTj(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		if (sjxxDto.getZq()==null) {
			sjxxDto.setZq("00");
		}
		if (redisUtil.getExpire("weekLeadStatis")==-2){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		String method = req.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("getYblxByYear".equals(method)) {
			//标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> statisbyyblxlist;
			String key = "getYblxByYear"+"_"+sjxxDto.getZq();
			Object getYblxByYear = redisUtil.hget("weekLeadStatis", key);
			if (getYblxByYear!=null){
				statisbyyblxlist = (List<Map<String, String>>) JSONArray.parse((String) getYblxByYear);
			}else {
				statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(statisbyyblxlist));
			}
			map.put("ybtj", statisbyyblxlist);
		}else if("getYblxByMon".equals(method)) {
			//标本按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> statisbyyblxlist;
			String key = "getYblxByMon"+"_"+sjxxDto.getZq();
			Object getYblxByMon = redisUtil.hget("weekLeadStatis", key);
			if (getYblxByMon!=null){
				statisbyyblxlist = (List<Map<String, String>>) JSONArray.parse((String) getYblxByMon);
			}else {
				statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(statisbyyblxlist));
			}
			map.put("ybtj", statisbyyblxlist);
		}else if("getYblxByWeek".equals(method)) {
			//标本按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybsList;
			String key = "getYblxByWeek"+"_"+sjxxDto.getZq();
			Object getYblxByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getYblxByWeek!=null){
				ybsList = (List<Map<String, String>>) JSONArray.parse((String) getYblxByWeek);
			}else {
				ybsList = sjxxService.getStatisByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybsList));
			}
			map.put("ybtj", ybsList);
		}else if("getYblxByDay".equals(method)) {
			//标本按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybsList;
			String key = "getYblxByDay"+"_"+sjxxDto.getZq();
			Object getYblxByDay = redisUtil.hget("weekLeadStatis", key);
			if (getYblxByDay!=null){
				ybsList = (List<Map<String, String>>) JSONArray.parse((String) getYblxByDay);
			}else {
				ybsList = sjxxService.getStatisByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybsList));
			}
			map.put("ybtj", ybsList);
		}else if("getSjxxDRByYear".equals(method)) {
			//检测总次数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByYear"+"_"+sjxxDto.getZq();
			Object getSjxxDRByYear = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByYear!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByYear);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByMon".equals(method)) {
			//检测总次数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByMon"+"_"+sjxxDto.getZq();
			Object getSjxxDRByMon = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByMon!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByMon);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByWeek".equals(method)) {
			//检测总次数按周查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByWeek"+"_"+sjxxDto.getZq();
			Object getSjxxDRByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByWeek!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByWeek);
			}else {
				jcxmnum = sjxxService.getSjxxDRByWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByDay".equals(method)) {
			//检测总次数按日查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByDay"+"_"+sjxxDto.getZq();
			Object getSjxxDRByDay = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByDay!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByDay);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
		}else if("getKsByYear".equals(method)) {
			//科室的圆饼统计图(年)
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ksList;
			String key = "getKsByYear"+"_"+sjxxDto.getZq();
			Object getKsByYear = redisUtil.hget("weekLeadStatis", key);
			if (getKsByYear!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByYear);
			}else {
				ksList = sjxxService.getKsByweek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		}else if("getKsByMon".equals(method)) {
			//科室的圆饼统计图(月)
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ksList;
			String key = "getKsByMon"+"_"+sjxxDto.getZq();
			Object getKsByMon = redisUtil.hget("weekLeadStatis", key);
			if (getKsByMon!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByMon);
			}else {
				ksList = sjxxService.getKsByweek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		}else if("getKsByWeek".equals(method)) {
			//科室的圆饼统计图(周)
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ksList;
			String key = "getKsByWeek"+"_"+sjxxDto.getZq();
			Object getKsByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getKsByWeek!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByWeek);
			}else {
				ksList = sjxxService.getKsByweek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			 map.put("ksList", ksList);
		}else if("getKsByDay".equals(method)) {
			//科室的圆饼统计图(日)
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart());
			List<Map<String, String>> ksList;
			String key = "getKsByDay"+"_"+sjxxDto.getZq();
			Object getKsByDay = redisUtil.hget("weekLeadStatis", key);
			if (getKsByDay!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByDay);
			}else {
				ksList = sjxxService.getKsByweek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		}
		else if("getjyjgOfYblxByYear".equals(method)) {
			//标本阳性率（年）
			setDateByYear(sjxxDto,0,true);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jyjgOfYblx;
			String key = "getjyjgOfYblxByYear"+"_"+sjxxDto.getZq();
			Object getjyjgOfYblxByYear = redisUtil.hget("weekLeadStatis", key);
			if (getjyjgOfYblxByYear!=null){
				jyjgOfYblx = (List<Map<String, String>>) JSONArray.parse((String) getjyjgOfYblxByYear);
			}else {
				jyjgOfYblx = sjxxService.getJyjgByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jyjgOfYblx));
			}
			 map.put("jyjgOfYblx", jyjgOfYblx);
		}else if("getjyjgOfYblxByMon".equals(method)) {
			//标本阳性率(月)
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jyjgOfYblx;
			String key = "getjyjgOfYblxByMon"+"_"+sjxxDto.getZq();
			Object getjyjgOfYblxByMon = redisUtil.hget("weekLeadStatis", key);
			if (getjyjgOfYblxByMon!=null){
				jyjgOfYblx = (List<Map<String, String>>) JSONArray.parse((String) getjyjgOfYblxByMon);
			}else {
				jyjgOfYblx = sjxxService.getJyjgByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jyjgOfYblx));
			}
			map.put("jyjgOfYblx", jyjgOfYblx);
		}else if("getjyjgOfYblxByWeek".equals(method)) {
			//标本阳性率(周)
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jyjgOfYblx;
			String key = "getjyjgOfYblxByWeek"+"_"+sjxxDto.getZq();
			Object getjyjgOfYblxByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getjyjgOfYblxByWeek!=null){
				jyjgOfYblx = (List<Map<String, String>>) JSONArray.parse((String) getjyjgOfYblxByWeek);
			}else {
				jyjgOfYblx = sjxxService.getJyjgByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jyjgOfYblx));
			}
			 map.put("jyjgOfYblx", jyjgOfYblx);
		}else if("getjyjgOfYblxByDay".equals(method)) {
			//标本阳性率(日)
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqstart());
			List<Map<String, String>> jyjgOfYblx;
			String key = "getjyjgOfYblxByDay"+"_"+sjxxDto.getZq();
			Object getjyjgOfYblxByDay = redisUtil.hget("weekLeadStatis", key);
			if (getjyjgOfYblxByDay!=null){
				jyjgOfYblx = (List<Map<String, String>>) JSONArray.parse((String) getjyjgOfYblxByDay);
			}else {
				jyjgOfYblx = sjxxService.getJyjgByYblx(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jyjgOfYblx));
			}
			map.put("jyjgOfYblx", jyjgOfYblx);
		}
		else if("getYbxxTypeByYear".equals(method)) {
			//收费标本检测项目次数（年）
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ybxxType;
			String key = "getYbxxTypeByYear"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByYear = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByYear!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByYear);
			}else {
				ybxxType = sjxxService.getYbxxTypeBYWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
			map.put("ybxxType", ybxxType);
		}else if("getYbxxTypeByMon".equals(method)) {
			//检测总次数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ybxxType;
			String key = "getYbxxTypeByMon"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByMon = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByMon!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByMon);
			}else {
				ybxxType = sjxxService.getYbxxTypeBYWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
			map.put("ybxxType", ybxxType);
		}else if("getYbxxTypeByWeek".equals(method)) {
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybxxType;
			String key = "getYbxxTypeByWeek"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByWeek!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByWeek);
			}else {
				ybxxType = sjxxService.getWeekYbxxType(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
			map.put("ybxxType", ybxxType);
		}else if("getYbxxTypeByDay".equals(method)) {
			//检测总次数按日查询
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybxxType;
			String key = "getYbxxTypeByDay"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByDay = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByDay!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByDay);
			}else {
				ybxxType = sjxxService.getYbxxTypeBYWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
//			List<Map<String, String>> ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			map.put("ybxxType", ybxxType);
		}
		else if("getDbByYear".equals(method)) {
			//合作伙伴按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> dblist;
			String key = "getDbByYear"+"_"+sjxxDto.getZq();
			Object getDbByYear = redisUtil.hget("weekLeadStatis", key);
			if (getDbByYear!=null){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) getDbByYear);
			}else {
				dblist = sjxxService.getStatisBysjhb(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByMon".equals(method)) {
			//合作伙伴按月
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			List<Map<String, String>> dblist;
			String key = "getDbByMon"+"_"+sjxxDto.getZq();
			Object getDbByMon = redisUtil.hget("weekLeadStatis", key);
			if (getDbByMon!=null){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) getDbByMon);
			}else {
				dblist = sjxxService.getStatisBysjhb(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByWeek".equals(method)) {
			//合作伙伴按周
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist;
			String key = "getDbByWeek"+"_"+sjxxDto.getZq();
			Object getDbByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getDbByWeek!=null){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) getDbByWeek);
			}else {
				dblist = sjxxService.getStatisWeekBysjhb(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisWeekBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByDay".equals(method)) {
			//合作伙伴按日
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist;
			String key = "getDbByDay"+"_"+sjxxDto.getZq();
			Object getDbByDay = redisUtil.hget("weekLeadStatis", key);
			if (getDbByDay!=null){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) getDbByDay);
			}else {
				dblist = sjxxService.getStatisBysjhb(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getJsrqByYear".equals(method)) {
			//阳性率-送检数量-报告数量 按年
			sjxxDto.setTj("year");
			setDateByYear(sjxxDto,-5,true);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());

			List<Map<String, String>> yblxList;
			String key = "getJsrqByYear"+"_"+sjxxDto.getZq();
			Object getJsrqByYear = redisUtil.hget("weekLeadStatis", key);
			if (getJsrqByYear!=null){
				yblxList = (List<Map<String, String>>) JSONArray.parse((String) getJsrqByYear);
				map.put("jsrq",yblxList);
			}else {
				List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
				yblxList = sjxxService.getStatisByDate(sjxxDto);
				List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("jsrq",yblxList);
				}
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(yblxList));
			}
		}else if("getJsrqByMon".equals(method)) {
			//阳性率-送检数量-报告数量 按月
			sjxxDto.setTj("mon");
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
			List<Map<String, String>> yblxList;
			String key = "getJsrqByMon"+"_"+sjxxDto.getZq();
			Object getJsrqByMon = redisUtil.hget("weekLeadStatis", key);
			if (getJsrqByMon!=null){
				yblxList = (List<Map<String, String>>) JSONArray.parse((String) getJsrqByMon);
				map.put("jsrq",yblxList);
			}else {
				List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
				yblxList = sjxxService.getStatisByDate(sjxxDto);
				List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("jsrq",yblxList);
				}
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(yblxList));
			}
		}else if("getJsrqByWeek".equals(method)) {
			//阳性率-送检数量-报告数量 按周（七天）
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> yblxList;
			String key = "getJsrqByWeek"+"_"+sjxxDto.getZq();
			Object getJsrqByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getJsrqByWeek!=null){
				yblxList = (List<Map<String, String>>) JSONArray.parse((String) getJsrqByWeek);
				map.put("jsrq",yblxList);
			}else {
				List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
				yblxList = sjxxService.getStatisByDate(sjxxDto);
				List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("jsrq",yblxList);
				}
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(yblxList));
			}
		}else if(method.startsWith("getRfsSjxx")) {
			sjxxDto.setJcxmdm("F");
			List<Map<String, String>> rfslist;
			//int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
			Object getRfsSjxx;
			if (method.contains("Year")){
				//标本情况按年
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				key = "getRfsSjxxYearBySy_"+sjxxDto.getZq();
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				key = "getRfsSjxxMonBySy_"+sjxxDto.getZq();
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-5);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "getRfsSjxxWeekBySy_"+sjxxDto.getZq();
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),-6);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "getRfsSjxxDayBySy_"+sjxxDto.getZq();
			}
			getRfsSjxx = redisUtil.hget("weekLeadStatis", key);
			if (getRfsSjxx!=null){
				rfslist = (List<Map<String, String>>) JSONArray.parse((String) getRfsSjxx);
			}else {
				rfslist = sjxxService.getSjxxBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(rfslist));
			}
			map.put("rfs",rfslist);
		}else if("getFlByYear".equals(method)){
			//分类情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			Map<String,List<Map<String, String>>> fllists;
			String key = "getFlByYear_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Object getFlByYear = redisUtil.hget("weekLeadStatis", key);
			if (getFlByYear!=null){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) getFlByYear);
			}else {
				fllists = sjxxStatisticService.getStatisByFlLimit(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getFlByMon".equals(method)){
			//分类情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			Map<String,List<Map<String, String>>> fllists;
			String key = "getFlByMon_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Object getFlByMon = redisUtil.hget("weekLeadStatis", key);
			if (getFlByMon!=null){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) getFlByMon);
			}else {
				fllists = sjxxStatisticService.getStatisByFlLimit(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getFlByWeek".equals(method)){
			//分类情况按周
//			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			Map<String,List<Map<String, String>>> fllists;
			String key = "getFlByWeek_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Object getFlByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getFlByWeek!=null){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) getFlByWeek);
			}else {
				fllists = sjxxStatisticService.getStatisByWeekFlLimit(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByWeekFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getFlByDay".equals(method)){
			//分类情况按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			Map<String,List<Map<String, String>>> fllists;
			String key = "getFlByDay_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Object getFlByDay = redisUtil.hget("weekLeadStatis", key);
			if (getFlByDay!=null){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) getFlByDay);
			}else {
				fllists = sjxxStatisticService.getStatisByFlLimit(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getHbByYear".equals(method)){
			//伙伴情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			List<Map<String, String>> fllist;
			String key = "getHbByYear"+"_"+sjxxDto.getZq();
			Object getHbByYear = redisUtil.hget("weekLeadStatis", key);
			if (getHbByYear!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getHbByYear);
			}else {
				fllist = sjxxService.getStatisByTjHbfl(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByMon".equals(method)){
			//伙伴情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			List<Map<String, String>> fllist;
			String key = "getHbByMon"+"_"+sjxxDto.getZq();
			Object getHbByMon = redisUtil.hget("weekLeadStatis", key);
			if (getHbByMon!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getHbByMon);
			}else {
				fllist = sjxxService.getStatisByTjHbfl(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByWeek".equals(method)){
			//伙伴情况按周
//			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			List<Map<String, String>> fllist;
			String key = "getHbByWeek"+"_"+sjxxDto.getZq();
			Object getHbByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getHbByWeek!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getHbByWeek);
			}else {
				fllist = sjxxService.getStatisByWeekTjHbfl(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByDay".equals(method)){
			//伙伴情况按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			List<Map<String, String>> fllist;
			String key = "getHbByDay"+"_"+sjxxDto.getZq();
			Object getHbByDay = redisUtil.hget("weekLeadStatis", key);
			if (getHbByDay!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getHbByDay);
			}else {
				fllist = sjxxService.getStatisByTjHbfl(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getAllhbByYear".equals(method)) {
			//合作伙伴总数按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> fllist;
			String key = "getAllhbByYear"+"_"+sjxxDto.getZq();
			Object getAllhbByYear = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByYear!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByYear);
			}else {
				fllist = sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByMon".equals(method)) {
			//合作伙伴总数按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			sjxxDto.setTj("mon");
			List<Map<String, String>> fllist;
			String key = "getAllhbByMon"+"_"+sjxxDto.getZq();
			Object getAllhbByMon = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByMon!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByMon);
			}else {
				fllist = sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByWeek".equals(method)) {
			//合作伙伴总数按周
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("week");
			List<Map<String, String>> fllist;
			String key = "getAllhbByWeek"+"_"+sjxxDto.getZq();
			Object getAllhbByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByWeek!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByWeek);
			}else {
				fllist = sjxxService.getStatisByTj(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getStatisByTj(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByDay".equals(method)) {
			//合作伙伴总数按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("day");
			List<Map<String, String>> fllist;
			String key = "getAllhbByDay"+"_"+sjxxDto.getZq();
			Object getAllhbByDay = redisUtil.hget("weekLeadStatis", key);
			if (getAllhbByDay!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAllhbByDay);
			}else {
				fllist = sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getFjsqByYear".equals(method)) {
			//复检按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjYstart(sjxxDto.getJsrqYstart());
			fjsqDto.setLrsjYend(sjxxDto.getJsrqYend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByYear"+"_"+sjxxDto.getZq();
			Object getFjsqByYear = redisUtil.hget("weekLeadStatis", key);
			if (getFjsqByYear!=null){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) getFjsqByYear);
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByMon".equals(method)) {
			//复检按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjMstart(sjxxDto.getJsrqMstart());
			fjsqDto.setLrsjMend(sjxxDto.getJsrqMend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByMon"+"_"+sjxxDto.getZq();
			Object getFjsqByMon = redisUtil.hget("weekLeadStatis", key);
			if (getFjsqByMon!=null){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) getFjsqByMon);
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByWeek".equals(method)) {
			//复检按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
			fjsqDto.setLrsjend(sjxxDto.getJsrqend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByWeek"+"_"+sjxxDto.getZq();
			Object getFjsqByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getFjsqByWeek!=null){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) getFjsqByWeek);
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByDay".equals(method)) {
			//复检按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
			fjsqDto.setLrsjend(sjxxDto.getJsrqend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByDay"+"_"+sjxxDto.getZq();
			Object getFjsqByDay = redisUtil.hget("weekLeadStatis", key);
			if (getFjsqByDay!=null){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) getFjsqByDay);
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFqybByYear".equals(method)) {
			//废弃标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByYear"+"_"+sjxxDto.getZq();
			Object getFqybByYear = redisUtil.hget("weekLeadStatis", key);
			if (getFqybByYear!=null){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) getFqybByYear);
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getFqybByMon".equals(method)) {
			//废弃标本按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByMon"+"_"+sjxxDto.getZq();
			Object getFqybByMon = redisUtil.hget("weekLeadStatis", key);
			if (getFqybByMon!=null){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) getFqybByMon);
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getFqybByWeek".equals(method)) {
			//废弃标本按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByWeek"+"_"+sjxxDto.getZq();
			Object getFqybByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getFqybByWeek!=null){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) getFqybByWeek);
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getFqybByDay".equals(method)) {
			//废弃标本按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByDay"+"_"+sjxxDto.getZq();
			Object getFqybByDay = redisUtil.hget("weekLeadStatis", key);
			if (getFqybByDay!=null){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) getFqybByDay);
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getAlljcdwByYear".equals(method)) {
			//检测单位测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> fllist;
			String key = "getAlljcdwByYear"+"_"+sjxxDto.getZq();
			Object getAlljcdwByYear = redisUtil.hget("weekLeadStatis", key);
			if (getAlljcdwByYear!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAlljcdwByYear);
			}else {
				fllist = sjxxService.getSjxxcssByJcdw(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getAlljcdwByMon".equals(method)) {
			//检测单位测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> fllist;
			String key = "getAlljcdwByMon"+"_"+sjxxDto.getZq();
			Object getAlljcdwByMon = redisUtil.hget("weekLeadStatis", key);
			if (getAlljcdwByMon!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAlljcdwByMon);
			}else {
				fllist = sjxxService.getSjxxcssByJcdw(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist = sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getAlljcdwByWeek".equals(method)) {
			//检测单位测试数按周查询
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fllist;
			String key = "getAlljcdwByWeek"+"_"+sjxxDto.getZq();
			Object getAlljcdwByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getAlljcdwByWeek!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAlljcdwByWeek);
			}else {
				fllist = sjxxService.getSjxxcssByJcdw(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getAlljcdwByDay".equals(method)) {
			//检测单位测试数按日查询
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),0);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fllist;
			String key = "getAlljcdwByDay"+"_"+sjxxDto.getZq();
			Object getAlljcdwByDay = redisUtil.hget("weekLeadStatis", key);
			if (getAlljcdwByDay!=null){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) getAlljcdwByDay);
			}else {
				fllist = sjxxService.getSjxxcssByJcdw(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getSjxxDRByYear".equals(method)) {
			//检测总次数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByYear"+"_"+sjxxDto.getZq();
			Object getSjxxDRByYear = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByYear!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByYear);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByMon".equals(method)) {
			//检测总次数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByMon"+"_"+sjxxDto.getZq();
			Object getSjxxDRByMon = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByMon!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByMon);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByWeek".equals(method)) {
			//检测总次数按周查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByWeek"+"_"+sjxxDto.getZq();
			Object getSjxxDRByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByWeek!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByWeek);
			}else {
				jcxmnum = sjxxService.getSjxxDRByWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByDay".equals(method)) {
			//检测总次数按日查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "getSjxxDRByDay"+"_"+sjxxDto.getZq();
			Object getSjxxDRByDay = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByDay!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByDay);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getjcdwByYear".equals(method)) {
			//检测单位测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jcxmnum;
			String key = "getjcdwByYear"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Object getjcdwByYear = redisUtil.hget("weekLeadStatis", key);
			if (getjcdwByYear!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getjcdwByYear);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getjcdwByMon".equals(method)) {
			//检测单位测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum;
			String key = "getjcdwByMon"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Object getjcdwByMon = redisUtil.hget("weekLeadStatis", key);
			if (getjcdwByMon!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getjcdwByMon);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getjcdwByWeek".equals(method)) {
			//检测单位测试数按周查询
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "getjcdwByWeek"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Object getjcdwByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getjcdwByWeek!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getjcdwByWeek);
			}else {
				jcxmnum = sjxxService.getSjxxDRByWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getjcdwByDay".equals(method)) {
			//检测单位测试数按日查询
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "getjcdwByDay"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Object getjcdwByDay = redisUtil.hget("weekLeadStatis", key);
			if (getjcdwByDay!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getjcdwByDay);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getSjxxYearBySyAndSf".equals(method)) {
			//省份测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> sfybqk;
			String key = "getSjxxYearBySyAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxYearBySyAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxYearBySyAndSf!=null){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) getSjxxYearBySyAndSf);
			}else {
				sfybqk = sjxxService.getSjxxBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String,String>> sfybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxMonBySyAndSf".equals(method)) {
			//省份测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> sfybqk;
			String key = "getSjxxMonBySyAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxMonBySyAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxMonBySyAndSf!=null){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) getSjxxMonBySyAndSf);
			}else {
				sfybqk = sjxxService.getSjxxBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk = sjxxService.getSjxxBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxWeekBySyAndSf".equals(method)) {
			//省份测试数按周查询
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfybqk;
			String key = "getSjxxWeekBySyAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxWeekBySyAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxWeekBySyAndSf!=null){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) getSjxxWeekBySyAndSf);
			}else {
				sfybqk = sjxxService.getSjxxWeekBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk=sjxxService.getSjxxWeekBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxDayBySyAndSf".equals(method)) {
			//省份测试数按日查询
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfybqk;
			String key = "getSjxxDayBySyAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxDayBySyAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDayBySyAndSf!=null){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDayBySyAndSf);
			}else {
				sfybqk = sjxxService.getSjxxBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxDRByYearAndSf".equals(method)) {
			//省份测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> sfcss;
			String key = "getSjxxDRByYearAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxDRByYearAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByYearAndSf!=null){
				sfcss = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByYearAndSf);
			}else {
				sfcss = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfcss));
			}
//			List<Map<String,String>> sfcss=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("sfcss", sfcss);
		}else if("getSjxxDRByMonAndSf".equals(method)) {
			//省份测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> sfcss;
			String key = "getSjxxDRByMonAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxDRByMonAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByMonAndSf!=null){
				sfcss = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByMonAndSf);
			}else {
				sfcss = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfcss));
			}
//			List<Map<String, String>> sfcss = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("sfcss", sfcss);
		}else if("getSjxxDRByWeekAndSf".equals(method)) {
			//检测单位测试数按周查询
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfybqk;
			String key = "getSjxxDRByWeekAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxDRByWeekAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByWeekAndSf!=null){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByWeekAndSf);
			}else {
				sfybqk = sjxxService.getSjxxDRByWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("sfcss", sfybqk);
		}else if("getSjxxDRByDayAndSf".equals(method)) {
			//检测单位测试数按日查询
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfcss;
			String key = "getSjxxDRByDayAndSf"+"_"+sjxxDto.getZq();
			Object getSjxxDRByDayAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByDayAndSf!=null){
				sfcss = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByDayAndSf);
			}else {
				sfcss = sjxxService.getSjxxDRBySy(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfcss));
			}
//			List<Map<String, String>> sfcss=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("sfcss", sfcss);
		}else if("getYbxxTypeByYearAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 年
			setDateByYear(sjxxDto,-6,false);
			List<Map<String, String>> sfsfcss;
			if (redisUtil.hget("weekLeadStatis", "getYbxxTypeByYearAndSf")!=null){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) redisUtil.hget("weekLeadStatis", "getYbxxTypeByYearAndSf"));
			}else {
				sfsfcss = sjxxService.getYbxxTypeBYWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", "getYbxxTypeByYearAndSf",JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if("getYbxxTypeByMonAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 月
			setDateByMonth(sjxxDto,-6);
			List<Map<String, String>> sfsfcss;
			String key = "getYbxxTypeByMonAndSf"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByMonAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByMonAndSf!=null){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByMonAndSf);
			}else {
				sfsfcss = sjxxService.getYbxxTypeBYWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if("getYbxxTypeByWeekAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 周
			List<Map<String, String>> sfsfcss;
			String key = "getYbxxTypeByWeekAndSf"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByWeekAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByWeekAndSf!=null){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByWeekAndSf);
			}else {
				sfsfcss = sjxxService.getWeekYbxxType(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getWeekYbxxType(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if("getYbxxTypeByDayAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 日
			List<Map<String, String>> sfsfcss;
			String key = "getYbxxTypeByDayAndSf"+"_"+sjxxDto.getZq();
			Object getYbxxTypeByDayAndSf = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByDayAndSf!=null){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByDayAndSf);
			}else {
				sfsfcss = sjxxService.getYbxxTypeBYWeek(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if(method.startsWith("getRFSAvgTime")) {
			List<Map<String, String>> rfspjyslist;
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
			Object getRfsAvgTimexx;
			if (method.contains("Year")){
				setDateByYear(sjxxDto,zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("rfspjys", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				key = "getRFSAvgTimeByYear_"+sjxxDto.getZq();
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("rfspjys", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				key = "getRFSAvgTimeByMon_"+sjxxDto.getZq();
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("rfspjys", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "getRFSAvgTimeByWeek_"+sjxxDto.getZq();
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("rfspjys", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "getRFSAvgTimeByDay_"+sjxxDto.getZq();
			}
			getRfsAvgTimexx = redisUtil.hget("weekLeadStatis", key);
			if (getRfsAvgTimexx!=null){
				rfspjyslist = (List<Map<String, String>>) JSONArray.parse((String) getRfsAvgTimexx);
			}else {
				rfspjyslist = sjxxResStatisticService.getAvgTime(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(rfspjyslist));
			}
			map.put("rfspjys",rfspjyslist);
		}else if(method.startsWith("getRfsZq")) {
			List<Map<String, String>> rfszqlist;
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
			Object getRfsZqxx;
			if (method.contains("Year")){
				setDateByYear(sjxxDto,zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("rfszq", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				key = "getRfsZqByYear_"+sjxxDto.getZq();
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("rfszq", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				key = "getgetRfsZqByMon_"+sjxxDto.getZq();
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("rfszq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "getRfsZqByWeek_"+sjxxDto.getZq();
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("rfszq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "getRfsZqByDay_"+sjxxDto.getZq();
			}
			getRfsZqxx = redisUtil.hget("weekLeadStatis", key);
			if (getRfsZqxx!=null){
				rfszqlist = (List<Map<String, String>>) JSONArray.parse((String) getRfsZqxx);
			}else {
				rfszqlist = sjxxResStatisticService.getLifeCycle(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(rfszqlist));
			}
			map.put("rfszq",rfszqlist);
		} else if ("getSalesAttainmentRateByYear".equals(method)) {
			//销售达成率按年
			String qyid = req.getParameter("qyid");
			XszbDto xszbDto = new XszbDto();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR,-4);
			String start = DateUtils.format(calendar.getTime(), "yyyy");
			String end = DateUtils.format(new Date(), "yyyy");
			xszbDto.setKszq(start);
			xszbDto.setJszq(end);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("Y");
			xszbDto.setQyid(qyid);
			Map<String, Object> salesAttainmentRate;
			String key = "getSalesAttainmentRateByYear_"+xszbDto.getQyid()+"_"+sjxxDto.getZq();
			Object getSalesAttainmentRateByYear = redisUtil.hget("weekLeadStatis", key);
			if (getSalesAttainmentRateByYear!=null){
				salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) getSalesAttainmentRateByYear);
			}else {
				salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
			}
//			Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				}
			}
			sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
			map.put("salesAttainmentRate", salesAttainmentRate);
		} else if ("getSalesAttainmentRateByQuarter".equals(method)) {
			//销售达成率按季度,全年季度
			String qyid = req.getParameter("qyid");
			XszbDto xszbDto = new XszbDto();
			String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            xszbDto.setKszq(currentYear);
			xszbDto.setJszq(currentYear);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("Q");
			xszbDto.setQyid(qyid);
			Map<String, Object> salesAttainmentRate;
			String key = "getSalesAttainmentRateByQuarter_"+xszbDto.getQyid()+"_"+sjxxDto.getZq();
			Object getSalesAttainmentRateByQuarter = redisUtil.hget("weekLeadStatis", key);
			if (getSalesAttainmentRateByQuarter!=null){
				salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) getSalesAttainmentRateByQuarter);
			}else {
				salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
			}
//			Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				}
			}
			sjxxDto.getZqs().put("salesAttainmentRate", currentYear + "~" + currentYear);
			map.put("salesAttainmentRate", salesAttainmentRate);
		} else if ("getSalesAttainmentRateByMon".equals(method)) {
			String qyid = req.getParameter("qyid");
			//销售达成率 默认6个月
			XszbDto xszbDto = new XszbDto();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH,-4);
			String start = DateUtils.format(calendar.getTime(), "yyyy-MM");
			String end = DateUtils.format(new Date(), "yyyy-MM");
			xszbDto.setKszq(start);
			xszbDto.setJszq(end);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("M");
			xszbDto.setQyid(qyid);
			Map<String, Object> salesAttainmentRate;
			String key = "getSalesAttainmentRateByMon_"+xszbDto.getQyid()+"_"+sjxxDto.getZq();
			Object getSalesAttainmentRateByMon = redisUtil.hget("weekLeadStatis", key);
			if (getSalesAttainmentRateByMon!=null){
				salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) getSalesAttainmentRateByMon);
			}else {
				salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
			}
//			Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				}
			}
			sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
			map.put("salesAttainmentRate", salesAttainmentRate);
		}
		map.put("searchData", sjxxDto);
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatis")==-1){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		return map;
	}
	/**
	 * 通过条件查询刷新页面(接收日期)
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/getSjxxStatisByTjAndJsrq")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTjAndJsrq(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		Calendar calendar_t = Calendar.getInstance();
		//时间，可以为具体的某一时间
		Date nowDate = new Date();
		calendar_t.setTime(nowDate);
		int monthDay = calendar_t.get(Calendar.DAY_OF_MONTH);
		int yearDay = calendar_t.get(Calendar.DAY_OF_YEAR);
		if (sjxxDto.getZq()==null) {
			sjxxDto.setZq("00");
		}
		sjxxDto.setTjtj("NOW");
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				//项目分类
				sjxxDto.setYwlx(dyxx.getCsid());
			}
			if ("HBFL".equals(dyxx.getCsdm())){
				//伙伴分类
				sjxxDto.setYwlx_q(dyxx.getCsid());
			}
		}
		List<String> yxxList = new ArrayList<>();
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				yxxList = yxxs;
				yxxList.add("Resfirst");
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}else{
			String[] yxxStrs = sjxxDto.getYxxs();
            Collections.addAll(yxxList, yxxStrs);
			yxxList.add("Resfirst");
		}
		if (redisUtil.getExpire("weekLeadStatis")==-2){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		String method = req.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}
//		else if("getTjxsdclByYear".equals(method)) {
//			//统计销售达成率按年查询
//			XszbDto xszb_yzb = new XszbDto();
//			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//				xszb_yzb.setKszq(sjxxDto.getLrsjStart());
//				xszb_yzb.setJszq(sjxxDto.getLrsjEnd());
//			} else {
//				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//				String start1 = currentYear+"-01";
//				String end1 = currentYear+"-12";
//				xszb_yzb.setKszq(start1);
//				xszb_yzb.setJszq(end1);
//			}
//			xszb_yzb.setZblxcsmc("Y");
//			xszb_yzb.setXms(yxxList);
//			xszb_yzb.setQyid(sjxxDto.getZfl());
//
//			sjxxDto.getZqs().put("tjxsdcl_"+sjxxDto.getZfl(), xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq());
//			sjxxDto.getZqs().put("tjxsdclyxxs_"+sjxxDto.getZfl(), StringUtil.join(sjxxDto.getYxxs(),","));
//			List<Map<String, String>> tjxsdclByYearlist = new ArrayList<>();
//			String key = "jsrq_tjxsdcl_"+sjxxDto.getZfl()+"_"+ xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
//			Object getXstjdclByYear = redisUtil.hget("weekLeadStatis", key);
//			if (getXstjdclByYear!=null){
//				tjxsdclByYearlist = (List<Map<String, String>>) JSONArray.parse((String) getXstjdclByYear);
//			}else {
//				tjxsdclByYearlist = sjxxStatisticService.getTjsxdcl(xszb_yzb);
//				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(tjxsdclByYearlist));
//			}
//			map.put("tjxsdcl_"+sjxxDto.getZfl(), tjxsdclByYearlist);
//		}
		else if("getTjxsdclByQuarter".equals(method)) {
			//统计销售达成率按季度查询
			XszbDto xszb_qzb = new XszbDto();
			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
				xszb_qzb.setKszq(sjxxDto.getLrsjStart());
				xszb_qzb.setJszq(sjxxDto.getLrsjEnd());
			} else {
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				xszb_qzb.setKszq(start1);
				xszb_qzb.setJszq(end1);
			}
			xszb_qzb.setZblxcsmc("Q");
			xszb_qzb.setXms(yxxList);
			xszb_qzb.setQyid(sjxxDto.getZfl());
			xszb_qzb.setYwlx(sjxxDto.getYwlx());
			xszb_qzb.setZbzfl(sjxxDto.getZfl());
			xszb_qzb.setCskz3(redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode(), sjxxDto.getZfl()).getCskz1());
			xszb_qzb.setCskz2(redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode(), sjxxDto.getZfl()).getCskz2());
			//由于外部统计数据，需要查找区域下的伙伴去限制外部统计数据范围
			List<String> hbids = xszbService.getDtosByZfl(xszb_qzb);
			xszb_qzb.setHbids(hbids);

			sjxxDto.getZqs().put("tjxsdcl_"+sjxxDto.getZfl(), xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq());
			sjxxDto.getZqs().put("tjxsdclyxxs_"+sjxxDto.getZfl(), StringUtil.join(sjxxDto.getYxxs(),","));
			List<Map<String, String>> tjxsdclByQuarterlist;
			String key = "jsrq_tjxsdcl_"+sjxxDto.getZfl()+"_"+ xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				tjxsdclByQuarterlist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				tjxsdclByQuarterlist = sjxxStatisticService.getTjsxdcl(xszb_qzb);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(tjxsdclByQuarterlist));
			}
			map.put("tjxsdcl_"+sjxxDto.getZfl(), tjxsdclByQuarterlist);
		}
//		else if("getTjxsdclByMon".equals(method)) {
//			//统计销售达成率按月查询
//			XszbDto xszb_mzb = new XszbDto();
//			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//				xszb_mzb.setKszq(sjxxDto.getLrsjStart());
//				xszb_mzb.setJszq(sjxxDto.getLrsjEnd());
//			}else {
//				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//				Calendar calendar = Calendar.getInstance();
//				String currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
//				String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
//				String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
//				xszb_mzb.setKszq(start1);
//				xszb_mzb.setJszq(end1);
//			}
//			xszb_mzb.setZblxcsmc("M");
//			xszb_mzb.setXms(yxxList);
//			xszb_mzb.setQyid(sjxxDto.getZfl());
//
//			sjxxDto.getZqs().put("tjxsdcl_"+sjxxDto.getZfl(), xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq());
//			sjxxDto.getZqs().put("tjxsdclyxxs_"+sjxxDto.getZfl(), StringUtil.join(sjxxDto.getYxxs(),","));
//			List<Map<String, String>> tjxsdclByMonlist = new ArrayList<>();
//			String key = "jsrq_tjxsdcl_"+sjxxDto.getZfl()+"_"+xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
//			Object getXstjdclByMon = redisUtil.hget("weekLeadStatis", key);
//			if (getXstjdclByMon!=null){
//				tjxsdclByMonlist = (List<Map<String, String>>) JSONArray.parse((String) getXstjdclByMon);
//			}else {
//				tjxsdclByMonlist = sjxxStatisticService.getTjsxdcl(xszb_mzb);
//				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(tjxsdclByMonlist));
//			}
//			map.put("tjxsdcl_"+sjxxDto.getZfl(), tjxsdclByMonlist);
//		}
		//平台指标达成率
		else if("getPtzbdclByYear".equals(method)) {
			//平台指标达成率按年查询
			XszbDto xszb_yzb = new XszbDto();
			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
				xszb_yzb.setKszq(sjxxDto.getLrsjStart());
				xszb_yzb.setJszq(sjxxDto.getLrsjEnd());
			} else {
				String  currentYear= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				if (yearDay<21){
					currentYear= String.valueOf(Integer.parseInt(currentYear)-1);
				}
				String start1 = currentYear+"-01";
				String end1 = currentYear+"-12";
				xszb_yzb.setKszq(start1);
				xszb_yzb.setJszq(end1);
			}
			xszb_yzb.setZblxcsmc("Y");
			xszb_yzb.setXms(Arrays.asList(sjxxDto.getYxxs()));
			xszb_yzb.setYwlx(sjxxDto.getYwlx());
			xszb_yzb.setXms(yxxList);

			sjxxDto.getZqs().put("ptzbdcl", xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq());
			sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			List<Map<String, Object>> ptzbdclByYearlist;
			String key = "jsrq_getPtzbdclByYear_"+ xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ptzbdclByYearlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				ptzbdclByYearlist = sjxxStatisticService.getPtzbdcl(xszb_yzb);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptzbdclByYearlist));
			}
			map.put("ptzbdcl", ptzbdclByYearlist);
		}
		else if("getPtzbdclByQuarter".equals(method)) {
			//平台指标达成率按季度查询
			XszbDto xszb_qzb = new XszbDto();
			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
				xszb_qzb.setKszq(sjxxDto.getLrsjStart());
				xszb_qzb.setJszq(sjxxDto.getLrsjEnd());
			} else {
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				xszb_qzb.setKszq(start1);
				xszb_qzb.setJszq(end1);
			}
			xszb_qzb.setZblxcsmc("Q");
			xszb_qzb.setXms(Arrays.asList(sjxxDto.getYxxs()));
			xszb_qzb.setYwlx(sjxxDto.getYwlx());
			xszb_qzb.setXms(yxxList);

			sjxxDto.getZqs().put("ptzbdcl", xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq());
			sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			List<Map<String, Object>> ptzbdclByQuarterlist;
			String key = "jsrq_getPtzbdclByQuarter_"+xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ptzbdclByQuarterlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				ptzbdclByQuarterlist = sjxxStatisticService.getPtzbdcl(xszb_qzb);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptzbdclByQuarterlist));
			}
			map.put("ptzbdcl", ptzbdclByQuarterlist);
		}
		else if("getPtzbdclByMon".equals(method)) {
			//平台指标达成率按月查询
			XszbDto xszb_mzb = new XszbDto();
			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
				xszb_mzb.setKszq(sjxxDto.getLrsjStart());
				xszb_mzb.setJszq(sjxxDto.getLrsjEnd());
			}else {
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				String currentMonth;
				if(calendar.get(Calendar.DAY_OF_MONTH)>10){
					currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
				}else {
					currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
				}
				String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
				String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
				xszb_mzb.setKszq(start1);
				xszb_mzb.setJszq(end1);
			}
			xszb_mzb.setZblxcsmc("M");
			xszb_mzb.setXms(Arrays.asList(sjxxDto.getYxxs()));
			xszb_mzb.setYwlx(sjxxDto.getYwlx());
			xszb_mzb.setXms(yxxList);

			sjxxDto.getZqs().put("ptzbdcl", xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq());
			sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			List<Map<String, Object>> ptzbdclByMonlist;
			String key = "jsrq_getPtzbdclByMon_"+xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ptzbdclByMonlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				ptzbdclByMonlist = sjxxStatisticService.getPtzbdcl(xszb_mzb);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptzbdclByMonlist));
			}
			map.put("ptzbdcl", ptzbdclByMonlist);
		}
		//平台业务占比
		else if(method.startsWith("getPtywzb")) {
			List<Map<String, Object>> ptywzbList;
			String key = null;
//			Object ptywzbListxx = null;
			XszbDto xszbDto = new XszbDto();
			xszbDto.setYwlx_q(sjxxDto.getYwlx_q());
			xszbDto.setYwlx(sjxxDto.getYwlx());
			if (method.contains("Year")){
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszbDto.setKszq(sjxxDto.getLrsjStart().substring(0,4));
					xszbDto.setJszq(sjxxDto.getLrsjEnd().substring(0,4));
				} else {
					if (yearDay<21){
						xszbDto.setKszq(String.valueOf(Integer.parseInt(sjxxDto.getJsrqstart().substring(0,4))-1));
						xszbDto.setJszq(String.valueOf(Integer.parseInt(sjxxDto.getJsrqend().substring(0,4))-1));
					}else {
						xszbDto.setKszq(sjxxDto.getJsrqstart().substring(0,4));
						xszbDto.setJszq(sjxxDto.getJsrqend().substring(0,4));
					}

				}
				xszbDto.setZblxcsmc("Y");
				xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));
				sjxxDto.getZqs().put("ptywzbtjzqs",xszbDto.getKszq()+ "~" + xszbDto.getJszq());
				sjxxDto.getZqs().put("ptywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtywzbByYearlist_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Mon")){
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszbDto.setKszq(sjxxDto.getLrsjStart());
					xszbDto.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					String currentMonth;
					if(calendar.get(Calendar.DAY_OF_MONTH)>10){
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
					}else {
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
					}
					String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
					String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
					xszbDto.setKszq(start1);
					xszbDto.setJszq(end1);
				}
				xszbDto.setZblxcsmc("M");
				xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));
				sjxxDto.getZqs().put("ptywzbtjzqs", xszbDto.getKszq()+ "~" + xszbDto.getJszq());
				sjxxDto.getZqs().put("ptywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtywzbByMonth_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Quarter")){
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszbDto.setKszq(sjxxDto.getLrsjStart());
					xszbDto.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
					xszbDto.setKszq(start1);
					xszbDto.setJszq(end1);
				}
				xszbDto.setZblxcsmc("Q");
				xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));

				sjxxDto.getZqs().put("ptywzbtjzqs", xszbDto.getKszq()+ "~" + xszbDto.getJszq());
				sjxxDto.getZqs().put("ptywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtywzbByQuarter_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ptywzbList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				ptywzbList = sjxxTwoService.getPlatformProportion(xszbDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptywzbList));
			}
			map.put("ptywzbtj",ptywzbList);
		}

		//产品业务占比
		else if(method.startsWith("getCpywzb")) {
			List<Map<String, Object>> cpywzbList;
			String key = null;
//			Object cpywzbListxx = null;
			XszbDto xszbDto = new XszbDto();
			xszbDto.setYwlx_q(sjxxDto.getYwlx_q());
			xszbDto.setYwlx(sjxxDto.getYwlx());
			if (method.contains("Year")){
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszbDto.setKszq(sjxxDto.getLrsjStart().substring(0,4));
					xszbDto.setJszq(sjxxDto.getLrsjEnd().substring(0,4));
				} else {
					if (yearDay<21){
						xszbDto.setKszq(String.valueOf(Integer.parseInt(sjxxDto.getJsrqstart().substring(0,4))-1));
						xszbDto.setJszq(String.valueOf(Integer.parseInt(sjxxDto.getJsrqend().substring(0,4))-1));
					}else {
						xszbDto.setKszq(sjxxDto.getJsrqstart().substring(0,4));
						xszbDto.setJszq(sjxxDto.getJsrqend().substring(0,4));
					}
				}
				xszbDto.setZblxcsmc("Y");
				xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));
				sjxxDto.getZqs().put("cpywzbtjzqs",xszbDto.getKszq()+ "~" + xszbDto.getJszq());
				sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpywzbByYearlist_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Mon")){
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszbDto.setKszq(sjxxDto.getLrsjStart());
					xszbDto.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					String currentMonth;
					if(calendar.get(Calendar.DAY_OF_MONTH)>10){
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
					}else {
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
					}
					String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
					String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
					xszbDto.setKszq(start1);
					xszbDto.setJszq(end1);
				}
				xszbDto.setZblxcsmc("M");
				xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));
				sjxxDto.getZqs().put("cpywzbtjzqs", xszbDto.getKszq()+ "~" + xszbDto.getJszq());
				sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpywzbByMonth_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Quarter")){
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszbDto.setKszq(sjxxDto.getLrsjStart());
					xszbDto.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
					xszbDto.setKszq(start1);
					xszbDto.setJszq(end1);
				}
				xszbDto.setZblxcsmc("Q");
				xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));

				sjxxDto.getZqs().put("cpywzbtjzqs", xszbDto.getKszq()+ "~" + xszbDto.getJszq());
				sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpywzbByQuarter_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				cpywzbList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				cpywzbList = sjxxTwoService.getProductionProportion(xszbDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(cpywzbList));
			}
			map.put("cpywzbtj",cpywzbList);
		}
		//业务准入发展达成率
//		else if("getYwzrfzByYear".equals(method)) {
//			//平台指标达成率按年查询
//			XszbDto xszb_yzb = new XszbDto();
//			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//				xszb_yzb.setKszq(sjxxDto.getLrsjStart());
//				xszb_yzb.setJszq(sjxxDto.getLrsjEnd());
//			} else {
//				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//				String start1 = currentYear+"-01";
//				String end1 = currentYear+"-12";
//				xszb_yzb.setKszq(start1);
//				xszb_yzb.setJszq(end1);
//			}
//			xszb_yzb.setZblxcsmc("Y");
//			xszb_yzb.setXms(yxxList);
//			xszb_yzb.setYwlx(sjxxDto.getYwlx());
//
//			sjxxDto.getZqs().put("ywzrfz", xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq());
//			sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
//			List<Map<String, Object>> ywzrfzByYearlist = new ArrayList<>();
//			String key = "jsrq_getYwzrfzByYear_"+ xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
//			Object getYwzrfzByYear = redisUtil.hget("weekLeadStatis", key);
//			if (getYwzrfzByYear!=null){
//				ywzrfzByYearlist = (List<Map<String, Object>>) JSONArray.parse((String) getYwzrfzByYear);
//			}else {
//				ywzrfzByYearlist = sjxxStatisticService.getYwfzbZbdcl(xszb_yzb);
//				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ywzrfzByYearlist));
//			}
//			map.put("ywzrfz", ywzrfzByYearlist);
//		}
		else if("getYwzrfzByQuarter".equals(method)) {
			//业务准入发展达成率按季度查询
			XszbDto xszb_qzb = new XszbDto();
			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
				xszb_qzb.setKszq(sjxxDto.getLrsjStart());
				xszb_qzb.setJszq(sjxxDto.getLrsjEnd());
			} else {
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				xszb_qzb.setKszq(start1);
				xszb_qzb.setJszq(end1);
			}
			xszb_qzb.setZblxcsmc("Q");
			xszb_qzb.setXms(yxxList);
			xszb_qzb.setYwlx(sjxxDto.getYwlx());

			sjxxDto.getZqs().put("ywzrfz", xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq());
			sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			List<Map<String, Object>> ywzrfzByQuarterlist;
			String key = "jsrq_getYwzrfzByQuarter_"+xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ywzrfzByQuarterlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else{
				ywzrfzByQuarterlist = sjxxStatisticService.getYwfzbZbdcl(xszb_qzb);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ywzrfzByQuarterlist));
			}
			map.put("ywzrfz", ywzrfzByQuarterlist);
		}
//		else if("getYwzrfzByMon".equals(method)) {
//			//业务准入发展达成率按月查询
//			XszbDto xszb_mzb = new XszbDto();
//			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//				xszb_mzb.setKszq(sjxxDto.getLrsjStart());
//				xszb_mzb.setJszq(sjxxDto.getLrsjEnd());
//			}else {
//				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//				Calendar calendar = Calendar.getInstance();
//				String currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
//				String start1 = currentYear + "-" + currentMonth;
//				String end1 = currentYear + "-" + currentMonth;
//				xszb_mzb.setKszq(start1);
//				xszb_mzb.setJszq(end1);
//			}
//			xszb_mzb.setZblxcsmc("M");
//			xszb_mzb.setXms(Arrays.asList(sjxxDto.getYxxs()));
//
//			sjxxDto.getZqs().put("ywzrfz", xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq());
//			sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
//			List<Map<String, Object>>ywzrfzByMonlist = new ArrayList<>();
//			String key = "jsrq_getYwzrfzByMon_"+xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
//			Object getYwzrfzByMon = redisUtil.hget("weekLeadStatis", key);
//			if (getYwzrfzByMon!=null){
//				ywzrfzByMonlist = (List<Map<String, Object>>) JSONArray.parse((String) getYwzrfzByMon);
//			}else {
//				ywzrfzByMonlist = sjxxStatisticService.getPtzbdcl(xszb_mzb);
//				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ywzrfzByMonlist));
//			}
//			map.put("ywzrfz", ywzrfzByMonlist);
//		}
		else if(method.contains("getHbflcsszb")) {
			String start1, end1;
			if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
				start1 = sjxxDto.getLrsjStart();
				end1 = sjxxDto.getLrsjEnd();
			}else if (method.contains("Year")){
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				if (yearDay<21){
					currentYear=String.valueOf(Integer.parseInt(currentYear)-1);
				}
				start1 = currentYear+"-01";
				end1 = currentYear+"-12";
			}else if(method.contains("Quarter")){
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
			}else if(method.contains("Mon")){
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				String currentMonth;
				if(calendar.get(Calendar.DAY_OF_MONTH)>10){
					currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
				}else {
					currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
				}
				start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
				end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
			}else{
				start1 = sjxxDto.getLrsjStart();
				end1 = sjxxDto.getLrsjEnd();
			}
			//伙伴分类测试数占比
			XxdyDto xxdyDto=new XxdyDto();
			xxdyDto.setCskz1("JCSJ");
			xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
			List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
			sjxxDto.getZqs().put("hbflcsszbzqs", start1 + "~" + end1);
			sjxxDto.getZqs().put("hbflcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			List<Map<String, String>> newList=new ArrayList<>();
			if(xxdyDtos!=null&&xxdyDtos.size()>0){
				SjxxDto sjxxDto_t=new SjxxDto();
				sjxxDto_t.setJsrqstart(start1);
				sjxxDto_t.setJsrqend(end1);
				sjxxDto_t.setYwlx(sjxxDto.getYwlx());
				sjxxDto_t.setYwlx_q(sjxxDto.getYwlx_q());
				sjxxDto_t.setYxxs(sjxxDto.getYxxs());
				List<Map<String, String>> hbflcsszbMap;
				String key = "jsrq_"+method+"_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					hbflcsszbMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
					newList = hbflcsszbMap;
				}else {
					sjxxDto_t.setXg_flg("1");
					hbflcsszbMap = sjxxStatisticService.getHbflcsszb(sjxxDto_t);
					for(XxdyDto xxdyDto_t:xxdyDtos){
						boolean isFind=false;
						for(Map<String, String> map_t:hbflcsszbMap){
							if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))){
								newList.add(map_t);
								isFind=true;
								break;
							}
						}
						if(!isFind){
							Map<String, String> newMap=new HashMap<>();
							newMap.put("num","0");
							newMap.put("yxx",xxdyDto_t.getYxx());
							newList.add(newMap);
						}
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(newList));
				}
			}
			map.put("hbflcsszb", newList);
		}
		else if(method.contains("getSjqfcsszb")){
			String start1, end1;
			if (method.contains("Year")){
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				if (yearDay<21){
					currentYear=String.valueOf(Integer.parseInt(currentYear)-1);
				}
				start1 = currentYear+"-01";
				end1 = currentYear+"-12";
			}else if(method.contains("Quarter")){
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
			}else if(method.contains("Mon")){
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				String currentMonth;
				if(calendar.get(Calendar.DAY_OF_MONTH)>10){
					currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
				}else {
					currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
				}
				start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
				end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
			}else{
				start1 = sjxxDto.getLrsjStart();
				end1 = sjxxDto.getLrsjEnd();
			}
			//送检区分测试数占比
			List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			sjxxDto.getZqs().put("sjqfcsszbzqs", start1 + "~" + end1);
			sjxxDto.getZqs().put("sjqfcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			String key = "jsrq_getSjqfcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			List<Map<String, String>> sjqfList=new ArrayList<>();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sjqfList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				JcsjDto jcsjDto1 = new JcsjDto();
				jcsjDto1.setCsid("第三方实验室");
				jcsjDto1.setCsmc("第三方实验室");
				sjqfs.add(jcsjDto1);
				JcsjDto jcsjDto2 = new JcsjDto();
				jcsjDto2.setCsid("直销");
				jcsjDto2.setCsmc("直销");
				sjqfs.add(jcsjDto2);
				JcsjDto jcsjDto3 = new JcsjDto();
				jcsjDto3.setCsid("CSO");
				jcsjDto3.setCsmc("CSO");
				sjqfs.add(jcsjDto3);
				if(sjqfs!=null&&sjqfs.size()>0){
					SjxxDto sjxxDto_t=new SjxxDto();
					sjxxDto_t.setJsrqstart(start1);
					sjxxDto_t.setJsrqend(end1);
					sjxxDto_t.setYwlx(sjxxDto.getYwlx());
					sjxxDto_t.setYxxs(sjxxDto.getYxxs());
					sjxxDto_t.setNewflg("zb");
					List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto_t);
					for(JcsjDto jcsjDto:sjqfs){
//						boolean isFind=false;
						for(Map<String, String> map_t:sjqfcsszb){
							if(jcsjDto.getCsid().equals(map_t.get("sjqf"))){
								map_t.put("sjqfmc",jcsjDto.getCsmc());
								sjqfList.add(map_t);
//								isFind=true;
								break;
							}
						}
						// if(!isFind){
						// 	Map<String, String> newMap=new HashMap<>();
						// 	newMap.put("num","0");
						// 	newMap.put("sjqfmc",jcsjDto.getCsmc());
						// 	sjqfList.add(newMap);
						// }
					}
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sjqfList));
			}
			map.put("sjqfcsszb", sjqfList);
		}
		else if("getXsxzcssqsDay".equals(method)){
			//销售性质测试数趋势
			List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
			List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				JcsjDto jcsjDto1 = new JcsjDto();
				jcsjDto1.setCsid("第三方实验室");
				jcsjDto1.setCsmc("第三方实验室");
				sjqfs.add(jcsjDto1);
				JcsjDto jcsjDto2 = new JcsjDto();
				jcsjDto2.setCsid("直销");
				jcsjDto2.setCsmc("直销");
				sjqfs.add(jcsjDto2);
				JcsjDto jcsjDto3 = new JcsjDto();
				jcsjDto3.setCsid("CSO");
				jcsjDto3.setCsmc("CSO");
				sjqfs.add(jcsjDto3);
				if(sjqfs!=null&&sjqfs.size()>0){
					sjxxDto.setNewflg("qs");
					List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
					for(int i=0;i<rqs.size();i++){
						for(JcsjDto jcsjDto:sjqfs){
							boolean isFind=false;
							for(Map<String, String> map_t:sjqfcsszb){
								if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
									map_t.put("sjqfmc",jcsjDto.getCsmc());
									if(i==0){
										map_t.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
										map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
									}
									xsxzcssqsList.add(map_t);
									isFind=true;
									break;
								}
							}
							if(!isFind){
								Map<String, String> newMap=new HashMap<>();
								newMap.put("rq",rqs.get(i));
								newMap.put("num","0");
								newMap.put("sjqfmc",jcsjDto.getCsmc());
								newMap.put("sjqf",jcsjDto.getCsid());
								if(i==0){
									newMap.put("bl","0");
								}else{
									var num="0";
									for(Map<String, String> stringMap:xsxzcssqsList){
										if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
											num=stringMap.get("num");
										}
									}
									BigDecimal bigDecimal=new BigDecimal(num);
									BigDecimal bigDecimal1=new BigDecimal("0");
									newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
								}
								xsxzcssqsList.add(newMap);
							}
						}
					}
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
			}
			map.put("xsxzcssqs", xsxzcssqsList);
		}
		else if("getXsxzcssqsWeek".equals(method)){
			//销售性质测试数趋势
			List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
			//分类情况按周
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			List<String> rqs = new ArrayList<>();
			// 设置日期
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
				rqs.add(sdf.format(calendar.getTime()));
				int zq = Integer.parseInt(sjxxDto.getZq());
				for (int i = 0; i < zq-1; i++)
				{
					calendar.add(Calendar.DATE, -7);
					rqs.add(sdf.format(calendar.getTime()));
				}
				Collections.reverse(rqs);
			} catch (Exception ex){
				log.error(ex.getMessage());
			}
			List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				JcsjDto jcsjDto1 = new JcsjDto();
				jcsjDto1.setCsid("第三方实验室");
				jcsjDto1.setCsmc("第三方实验室");
				sjqfs.add(jcsjDto1);
				JcsjDto jcsjDto2 = new JcsjDto();
				jcsjDto2.setCsid("直销");
				jcsjDto2.setCsmc("直销");
				sjqfs.add(jcsjDto2);
				JcsjDto jcsjDto3 = new JcsjDto();
				jcsjDto3.setCsid("CSO");
				jcsjDto3.setCsmc("CSO");
				sjqfs.add(jcsjDto3);
				if(sjqfs!=null&&sjqfs.size()>0){
					sjxxDto.setNewflg("qs");
					List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
					for(int i=0;i<rqs.size();i++){
						for(JcsjDto jcsjDto:sjqfs){
							boolean isFind=false;
							for(Map<String, String> map_t:sjqfcsszb){
								if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
									map_t.put("sjqfmc",jcsjDto.getCsmc());
									if(i==0){
										map_t.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
										map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
									}
									xsxzcssqsList.add(map_t);
									isFind=true;
									break;
								}
							}
							if(!isFind){
								Map<String, String> newMap=new HashMap<>();
								newMap.put("rq",rqs.get(i));
								newMap.put("num","0");
								newMap.put("sjqfmc",jcsjDto.getCsmc());
								newMap.put("sjqf",jcsjDto.getCsid());
								if(i==0){
									newMap.put("bl","0");
								}else{
									var num="0";
									for(Map<String, String> stringMap:xsxzcssqsList){
										if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
											num=stringMap.get("num");
										}
									}
									BigDecimal bigDecimal=new BigDecimal(num);
									BigDecimal bigDecimal1=new BigDecimal("0");
									newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
								}
								xsxzcssqsList.add(newMap);
							}
						}
					}
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
			}
			map.put("xsxzcssqs", xsxzcssqsList);
		}
		else if("getXsxzcssqsMon".equals(method)){
			//销售性质测试数趋势
			List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
			List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				JcsjDto jcsjDto1 = new JcsjDto();
				jcsjDto1.setCsid("第三方实验室");
				jcsjDto1.setCsmc("第三方实验室");
				sjqfs.add(jcsjDto1);
				JcsjDto jcsjDto2 = new JcsjDto();
				jcsjDto2.setCsid("直销");
				jcsjDto2.setCsmc("直销");
				sjqfs.add(jcsjDto2);
				JcsjDto jcsjDto3 = new JcsjDto();
				jcsjDto3.setCsid("CSO");
				jcsjDto3.setCsmc("CSO");
				sjqfs.add(jcsjDto3);
				if(sjqfs!=null&&sjqfs.size()>0){
					sjxxDto.setNewflg("qs");
					List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
					for(int i=0;i<rqs.size();i++){
						for(JcsjDto jcsjDto:sjqfs){
							boolean isFind=false;
							for(Map<String, String> map_t:sjqfcsszb){
								if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
									map_t.put("sjqfmc",jcsjDto.getCsmc());
									if(i==0){
										map_t.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
										map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
									}
									xsxzcssqsList.add(map_t);
									isFind=true;
									break;
								}
							}
							if(!isFind){
								Map<String, String> newMap=new HashMap<>();
								newMap.put("rq",rqs.get(i));
								newMap.put("num","0");
								newMap.put("sjqfmc",jcsjDto.getCsmc());
								newMap.put("sjqf",jcsjDto.getCsid());
								if(i==0){
									newMap.put("bl","0");
								}else{
									var num="0";
									for(Map<String, String> stringMap:xsxzcssqsList){
										if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
											num=stringMap.get("num");
										}
									}
									BigDecimal bigDecimal=new BigDecimal(num);
									BigDecimal bigDecimal1=new BigDecimal("0");
									newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
								}
								xsxzcssqsList.add(newMap);
							}
						}
					}
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
			}
			map.put("xsxzcssqs", xsxzcssqsList);
		}
		else if("getXsxzcssqsYear".equals(method)){
			//销售性质测试数趋势
			List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);
			List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
			sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				JcsjDto jcsjDto1 = new JcsjDto();
				jcsjDto1.setCsid("第三方实验室");
				jcsjDto1.setCsmc("第三方实验室");
				sjqfs.add(jcsjDto1);
				JcsjDto jcsjDto2 = new JcsjDto();
				jcsjDto2.setCsid("直销");
				jcsjDto2.setCsmc("直销");
				sjqfs.add(jcsjDto2);
				JcsjDto jcsjDto3 = new JcsjDto();
				jcsjDto3.setCsid("CSO");
				jcsjDto3.setCsmc("CSO");
				sjqfs.add(jcsjDto3);
				if(sjqfs!=null&&sjqfs.size()>0){
					sjxxDto.setNewflg("qs");
					List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
					for(int i=0;i<rqs.size();i++){
						for(JcsjDto jcsjDto:sjqfs){
							boolean isFind=false;
							for(Map<String, String> map_t:sjqfcsszb){
								if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
									map_t.put("sjqfmc",jcsjDto.getCsmc());
									if(i==0){
										map_t.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
										map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
									}
									xsxzcssqsList.add(map_t);
									isFind=true;
									break;
								}
							}
							if(!isFind){
								Map<String, String> newMap=new HashMap<>();
								newMap.put("rq",rqs.get(i));
								newMap.put("num","0");
								newMap.put("sjqfmc",jcsjDto.getCsmc());
								newMap.put("sjqf",jcsjDto.getCsid());
								if(i==0){
									newMap.put("bl","0");
								}else{
									var num="0";
									for(Map<String, String> stringMap:xsxzcssqsList){
										if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
											num=stringMap.get("num");
										}
									}
									BigDecimal bigDecimal=new BigDecimal(num);
									BigDecimal bigDecimal1=new BigDecimal("0");
									newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),RoundingMode.HALF_UP)));
								}
								xsxzcssqsList.add(newMap);
							}
						}
					}
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
			}
			map.put("xsxzcssqs", xsxzcssqsList);
		}
		else if("getSjxxDRByYear".equals(method)) {
			//检测总次数按年查询
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByYear_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
			sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if("getSjxxDRByMon".equals(method)) {
			//检测总次数按月查询
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByMon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object getSjxxDRByMon = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByMon!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByMon);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
			sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		} else if("getSjxxDRByWeek".equals(method)) {
			//检测总次数按周查询
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByWeek_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object getSjxxDRByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByWeek!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByWeek);
			}else {
				jcxmnum = sjxxService.getSjxxDRByWeekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
			sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if("getSjxxDRByDay".equals(method)) {
			//检测总次数按日查询
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByDay_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object getSjxxDRByDay = redisUtil.hget("weekLeadStatis", key);
			if (getSjxxDRByDay!=null){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) getSjxxDRByDay);
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
			map.put("jcxmnum", jcxmnum);
			sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if("getKsByYear".equals(method)) {
			//科室的圆饼统计图(年)
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ksList;
			String key = "jsrq_getKsByYear_"+sjxxDto.getZq();
			Object getKsByYear = redisUtil.hget("weekLeadStatis", key);
			if (getKsByYear!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByYear);
			}else {
				ksList = sjxxService.getKsByweekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		}else if("getKsByMon".equals(method)) {
			//科室的圆饼统计图(月)
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ksList;
			String key = "jsrq_getKsByMon_"+sjxxDto.getZq();
			Object getKsByMon = redisUtil.hget("weekLeadStatis", key);
			if (getKsByMon!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByMon);
			}else {
				ksList = sjxxService.getKsByweekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		}else if("getKsByWeek".equals(method)) {
			//科室的圆饼统计图(周)
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ksList;
			String key = "jsrq_getKsByWeek_"+sjxxDto.getZq();
			Object getKsByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getKsByWeek!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByWeek);
			}else {
				ksList = sjxxService.getKsByweekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		}else if("getKsByDay".equals(method)) {
			//科室的圆饼统计图(日)
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart());
			List<Map<String, String>> ksList;
			String key = "jsrq_getKsByDay_"+sjxxDto.getZq();
			Object getKsByDay = redisUtil.hget("weekLeadStatis", key);
			if (getKsByDay!=null){
				ksList = (List<Map<String, String>>) JSONArray.parse((String) getKsByDay);
			}else {
				ksList = sjxxService.getKsByweekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ksList));
			}
			map.put("ksList", ksList);
		} else if("getYbxxTypeByYear".equals(method)) {
			//收费标本检测项目次数（年）
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ybxxType;
			String key = "jsrq_getYbxxTypeByYear_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object getYbxxTypeByYear = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByYear!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByYear);
			}else {
				ybxxType = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
			map.put("ybxxType", ybxxType);
			sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if("getYbxxTypeByMon".equals(method)) {
			//检测总次数按月查询
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ybxxType;
			String key = "jsrq_getYbxxTypeByMon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object getYbxxTypeByMon = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByMon!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByMon);
			}else {
				ybxxType = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
			map.put("ybxxType", ybxxType);
			sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if("getYbxxTypeByWeek".equals(method)) {
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybxxType;
			String key = "jsrq_getYbxxTypeByWeek_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs());
			Object getYbxxTypeByWeek = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByWeek!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByWeek);
			}else {
				ybxxType = sjxxService.getWeekYbxxTypeByJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
			map.put("ybxxType", ybxxType);
			sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if("getYbxxTypeByDay".equals(method)) {
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			//检测总次数按日查询
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybxxType;
			String key = "jsrq_getYbxxTypeByDay_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			Object getYbxxTypeByDay = redisUtil.hget("weekLeadStatis", key);
			if (getYbxxTypeByDay!=null){
				ybxxType = (List<Map<String, String>>) JSONArray.parse((String) getYbxxTypeByDay);
			}else {
				ybxxType = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ybxxType));
			}
//			List<Map<String, String>> ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			map.put("ybxxType", ybxxType);
			sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		} else if("getDbByYear".equals(method)) {
			//合作伙伴按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> dblist;
			String key = "jsrq_getDbByYear_"+sjxxDto.getZq();
			Object getDbByYear = redisUtil.hget("weekLeadStatis", key);
			if (getDbByYear!=null){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) getDbByYear);
			}else {
				dblist = sjxxService.getStatisBysjhbAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByMon".equals(method)) {
			//合作伙伴按月
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			List<Map<String, String>> dblist;
			String key = "jsrq_getDbByMon_"+sjxxDto.getZq();
			Object getDbByMon = redisUtil.hget("weekLeadStatis", key);
			if (getDbByMon!=null){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) getDbByMon);
			}else {
				dblist = sjxxService.getStatisBysjhbAndJsrq(sjxxDto);
				redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByWeek".equals(method)) {
			//合作伙伴按周
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist;
			String key = "jsrq_getDbByWeek_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				dblist = sjxxService.getStatisWeekBysjhbAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(dblist));
			}
			map.put("dbtj",dblist);
		}else if("getDbByDay".equals(method)) {
			//合作伙伴按日
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist;
			String key = "jsrq_getDbByDay_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				dblist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				dblist = sjxxService.getStatisBysjhbAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(dblist));
			}
//			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getJsrqByYear".equals(method)) {
			//阳性率-送检数量-报告数量 按年
			sjxxDto.setTj("year");
			setDateByYear(sjxxDto,-5,true);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());

			List<Map<String, String>> yblxList;
			String key = "jsrq_getJsrqByYear_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				yblxList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				map.put("jsrq",yblxList);
			}else {
				List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
				yblxList = sjxxService.getStatisByDateAndJsrq(sjxxDto);
				List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("jsrq",yblxList);
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(yblxList));
			}
		}else if("getJsrqByMon".equals(method)) {
			//阳性率-送检数量-报告数量 按月
			sjxxDto.setTj("mon");
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
			List<Map<String, String>> yblxList;
			String key = "jsrq_getJsrqByMon_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				yblxList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				map.put("jsrq",yblxList);
			}else {
				List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
				yblxList = sjxxService.getStatisByDateAndJsrq(sjxxDto);
				List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("jsrq",yblxList);
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(yblxList));
			}
		}else if("getJsrqByWeek".equals(method)) {
			//阳性率-送检数量-报告数量 按周（七天）
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> yblxList;
			String key = "jsrq_getJsrqByWeek_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				yblxList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				map.put("jsrq",yblxList);
			}else {
				List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
				yblxList = sjxxService.getStatisByDateAndJsrq(sjxxDto);
				List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("jsrq",yblxList);
				}
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(yblxList));
			}
		}else if("getSjxxYearBySy".equals(method)) {
			//标本情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ybqklist;
			String key = "jsrq_getSjxxYearBySy_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ybqklist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				ybqklist = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybqklist));
			}
//			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxMonBySy".equals(method)) {
			//标本情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ybqklist;
			String key = "jsrq_getSjxxMonBySy_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ybqklist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				ybqklist = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybqklist));
			}
//			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxWeekBySy".equals(method)){
			//标本情况按周
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybqklist;
			String key = "jsrq_getSjxxWeekBySy_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ybqklist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				ybqklist = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybqklist));
			}
//			List<Map<String, String>> ybqklist=sjxxService.getSjxxWeekBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxDayBySy".equals(method)){
			//标本情况按日
			//setDateByDay(sjxxDto,-6);
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybqklist;
			String key = "jsrq_getSjxxDayBySy_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ybqklist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				ybqklist = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybqklist));
			}
//			List<Map<String, String>> ybqklist=sjx xService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if(method.startsWith("getRfsSjxx")) {
			sjxxDto.setJcxmdm("F");
			sjxxDto.setYxxs(null);
			List<Map<String, String>> rfslist;
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
//			Object getRfsSjxx = null;
			if (method.contains("Year")){
				//标本情况按年
				setDateByYear(sjxxDto,zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				key = "jsrq_getRfsSjxxYearBySy_"+sjxxDto.getZq();
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				key = "jsrq_getRfsSjxxMonBySy_"+sjxxDto.getZq();
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "jsrq_getRfsSjxxWeekBySy_"+sjxxDto.getZq();
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				key = "jsrq_getRfsSjxxDayBySy_"+sjxxDto.getZq();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				rfslist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				rfslist = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(rfslist));
			}
			map.put("rfs",rfslist);
		}else if(method.startsWith("getQgqs")) {
			List<Map<String, String>> qgqslist;
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
//			Object getQgqs = null;
			if (method.contains("Year")){
				setDateByYear(sjxxDto,zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getQgqsSjxxYear_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getQgqsSjxxMon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getQgqsSjxxWeek_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getQgqsSjxxDay_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				qgqslist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				qgqslist = sjxxTwoService.getAllCountryChanges(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(qgqslist));
			}
			map.put("qgqs", qgqslist);
		}else if(method.startsWith("getCpqstj")) {
			List<Map<String, Object>> cpzqtjlist;
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
//			Object getCpqstjs = null;
			if (method.contains("Year")){
				setDateByYear(sjxxDto,zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpqstjSjxxYear_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpqstjSjxxMon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpqstjSjxxWeek_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getCpqstjSjxxDay_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				cpzqtjlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				cpzqtjlist = sjxxTwoService.getProductionChanges(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(cpzqtjlist));
			}
			map.put("cpqstj", cpzqtjlist);
		}else if(method.startsWith("getPtqstj")) {
			List<Map<String, Object>> ptqstjlist;
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			String key = null;
//			Object getPtqstjs = null;
			if (method.contains("Year")){
				setDateByYear(sjxxDto,zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("ptqstjzqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				sjxxDto.getZqs().put("ptqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtqstjSjxxYear_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Mon")){
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("ptqstjzqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("ptqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtqstjSjxxMon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Week")){
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("ptqstjzqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("ptqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtqstjSjxxWeek_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}else if (method.contains("Day")){
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("ptqstjzqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("ptqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getPtqstjSjxxDay_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ptqstjlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				ptqstjlist = sjxxTwoService.getPlatformChanges(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptqstjlist));
			}
			map.put("ptqstj", ptqstjlist);
		}else if("getFlByYear".equals(method)){
			//分类情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			Map<String,List<Map<String, String>>> fllists;
			String key = "jsrq_getFlByYear_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllists = sjxxStatisticService.getStatisByFlLimitAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getFlByMon".equals(method)){
			//分类情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			Map<String,List<Map<String, String>>> fllists;
			String key = "jsrq_getFlByMon_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllists = sjxxStatisticService.getStatisByFlLimitAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getFlByWeek".equals(method)){
			//分类情况按周
//			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			Map<String,List<Map<String, String>>> fllists;
			String key = "jsrq_getFlByWeek_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllists = sjxxStatisticService.getStatisByWeekFlLimitAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByWeekFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getFlByDay".equals(method)){
			//分类情况按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			Map<String,List<Map<String, String>>> fllists;
			String key = "jsrq_getFlByDay_"+sjxxDto.getHbfl()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllists = (Map<String,List<Map<String, String>>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllists = sjxxStatisticService.getStatisByFlLimitAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllists));
			}
//			Map<String,List<Map<String, String>>> fllists=sjxxStatisticService.getStatisByFlLimit(sjxxDto);
			if(fllists != null) {
				for(String map1 : fllists.keySet()){
					map.put("fltj_"+map1,fllists.get(map1));
				}
			}
		}else if("getHbByYear".equals(method)){
			//伙伴情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			List<Map<String, String>> fllist;
			String key = "jsrq_getHbByYear"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getStatisByTjHbflAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByMon".equals(method)){
			//伙伴情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			List<Map<String, String>> fllist;
			String key = "jsrq_getHbByMon"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getStatisByTjHbflAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByWeek".equals(method)){
			//伙伴情况按周
//			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			List<Map<String, String>> fllist;
			String key = "jsrq_getHbByWeek"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getStatisByWeekTjHbflAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByDay".equals(method)){
			//伙伴情况按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			List<Map<String, String>> fllist;
			String key = "jsrq_getHbByDay"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getStatisByTjHbflAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getAllhbByYear".equals(method)) {
			//合作伙伴总数按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> fllist;
			String key = "jsrq_getAllhbByYear"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByMon".equals(method)) {
			//合作伙伴总数按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			sjxxDto.setTj("mon");
			List<Map<String, String>> fllist;
			String key = "jsrq_getAllhbByMon"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByWeek".equals(method)) {
			//合作伙伴总数按周
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("week");
			List<Map<String, String>> fllist;
			String key = "jsrq_getAllhbByWeek"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getStatisByTjAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getStatisByTj(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByDay".equals(method)) {
			//合作伙伴总数按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("day");
			List<Map<String, String>> fllist;
			String key = "jsrq_getAllhbByDay"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxStatisticService.getStatisBySomeTimeDBAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxStatisticService.getStatisBySomeTimeDB(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getFjsqByYear".equals(method)) {
			//复检按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjYstart(sjxxDto.getJsrqYstart());
			fjsqDto.setLrsjYend(sjxxDto.getJsrqYend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByYear"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByMon".equals(method)) {
			//复检按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjMstart(sjxxDto.getJsrqMstart());
			fjsqDto.setLrsjMend(sjxxDto.getJsrqMend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByMon"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByWeek".equals(method)) {
			//复检按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
			fjsqDto.setLrsjend(sjxxDto.getJsrqend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByWeek"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByDay".equals(method)) {
			//复检按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
			fjsqDto.setLrsjend(sjxxDto.getJsrqend());
			List<Map<String, String>> fjsqMap;
			String key = "getFjsqByDay"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fjsqMap = sjxxService.getFjsqByDay(fjsqDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
			}
//			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFqybByYear".equals(method)) {
			//废弃标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByYear"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getFqybByMon".equals(method)) {
			//废弃标本按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByMon"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getFqybByWeek".equals(method)) {
			//废弃标本按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByWeek"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getFqybByDay".equals(method)) {
			//废弃标本按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fqybMap;
			String key = "getFqybByDay"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fqybMap = sjxxService.getFqybByYbzt(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
			}
//			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb",fqybMap);
		}else if("getAlljcdwByYear".equals(method)) {
			//检测单位测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> fllist;
			String key = "jsrq_getAlljcdwByYear"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getSjxxcssByJcdwAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getAlljcdwByMon".equals(method)) {
			//检测单位测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> fllist;
			String key = "jsrq_getAlljcdwByMon"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getSjxxcssByJcdwAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist = sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getAlljcdwByWeek".equals(method)) {
			//检测单位测试数按周查询
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fllist;
			String key = "jsrq_getAlljcdwByWeek"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getSjxxcssByJcdwAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getAlljcdwByDay".equals(method)) {
			//检测单位测试数按日查询
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),0);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> fllist;
			String key = "jsrq_getAlljcdwByDay"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				fllist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				fllist = sjxxService.getSjxxcssByJcdwAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fllist));
			}
//			List<Map<String, String>> fllist=sjxxService.getSjxxcssByJcdw(sjxxDto);
			map.put("jcdwtj", fllist);
		}else if("getSjxxDRByYear".equals(method)) {
			//检测总次数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByYear"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByMon".equals(method)) {
			//检测总次数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByMon"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByWeek".equals(method)) {
			//检测总次数按周查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByWeek"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRByWeekAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByDay".equals(method)) {
			//检测总次数按日查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getSjxxDRByDay"+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getjcdwByYear".equals(method)) {
			//检测单位测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getjcdwByYear"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getjcdwByMon".equals(method)) {
			//检测单位测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getjcdwByMon"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getjcdwByWeek".equals(method)) {
			//检测单位测试数按周查询
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getjcdwByWeek"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRByWeekAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getjcdwByDay".equals(method)) {
			//检测单位测试数按日查询
			sjxxDto.getZqs().put("jcdwtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum;
			String key = "jsrq_getjcdwByDay"+"_"+sjxxDto.getJcdw()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				jcxmnum = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
			}
//			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcdwtj_"+sjxxDto.getJcdw(), jcxmnum);
		}else if("getSjxxYearBySyAndSf".equals(method)) {
			//省份测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> sfybqk;
			String key = "jsrq_getSjxxYearBySyAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfybqk = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String,String>> sfybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxMonBySyAndSf".equals(method)) {
			//省份测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> sfybqk;
			String key = "jsrq_getSjxxMonBySyAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfybqk = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk = sjxxService.getSjxxBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxWeekBySyAndSf".equals(method)) {
			//省份测试数按周查询
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfybqk;
			String key = "jsrq_getSjxxWeekBySyAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfybqk = sjxxService.getSjxxWeekBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk=sjxxService.getSjxxWeekBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxDayBySyAndSf".equals(method)) {
			//省份测试数按日查询
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfybqk;
			String key = "jsrq_getSjxxDayBySyAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfybqk = sjxxService.getSjxxBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("sfybqk", sfybqk);
		}else if("getSjxxDRByYearAndSf".equals(method)) {
			//省份测试数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> sfcss;
			String key = "jsrq_getSjxxDRByYearAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfcss = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfcss));
			}
//			List<Map<String,String>> sfcss=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("sfcss", sfcss);
		}else if("getSjxxDRByMonAndSf".equals(method)) {
			//省份测试数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> sfcss;
			String key = "jsrq_getSjxxDRByMonAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfcss = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfcss));
			}
//			List<Map<String, String>> sfcss = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("sfcss", sfcss);
		}else if("getSjxxDRByWeekAndSf".equals(method)) {
			//检测单位测试数按周查询
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfybqk;
			String key = "jsrq_getSjxxDRByWeekAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfybqk = sjxxService.getSjxxDRByWeekAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
			}
//			List<Map<String, String>> sfybqk=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("sfcss", sfybqk);
		}else if("getSjxxDRByDayAndSf".equals(method)) {
			//检测单位测试数按日查询
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> sfcss;
			String key = "jsrq_getSjxxDRByDayAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfcss = sjxxService.getSjxxDRBySyAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfcss));
			}
//			List<Map<String, String>> sfcss=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("sfcss", sfcss);
		}else if("getYbxxTypeByYearAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 年
			setDateByYear(sjxxDto,-6,false);
			List<Map<String, String>> sfsfcss;
			String key = "jsrq_getYbxxTypeByYearAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfsfcss = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if("getYbxxTypeByMonAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 月
			setDateByMonth(sjxxDto,-6);
			List<Map<String, String>> sfsfcss;
			String key = "jsrq_getYbxxTypeByMonAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfsfcss = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if("getYbxxTypeByWeekAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 周
			List<Map<String, String>> sfsfcss;
			String key = "getYbxxTypeByWeekAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfsfcss = sjxxService.getWeekYbxxTypeByJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getWeekYbxxType(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		}else if("getYbxxTypeByDayAndSf".equals(method)) {
			//省份收费标本下边检测项目的总条数 日
			List<Map<String, String>> sfsfcss;
			String key = "jsrq_getYbxxTypeByDayAndSf"+"_"+sjxxDto.getZq()+"_"+sjxxDto.getSf();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sfsfcss = sjxxService.getYbxxTypeBYWeekAndJsrq(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
			}
//			List<Map<String,String>> sfsfcss=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", sfsfcss);
		} else if ("getSalesAttainmentRateByYear".equals(method)) {
			//销售达成率按年
			String qyid = req.getParameter("qyid");
			XszbDto xszbDto = new XszbDto();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR,-4);
			String start = DateUtils.format(calendar.getTime(), "yyyy");
			String end = DateUtils.format(new Date(), "yyyy");
			xszbDto.setKszq(start);
			xszbDto.setJszq(end);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("Y");
			xszbDto.setQyid(qyid);
			Map<String, Object> salesAttainmentRate;
			String key = "jsrq_getSalesAttainmentRateByYear_"+xszbDto.getQyid()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
			}
//			Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				}
			}
			sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
			map.put("salesAttainmentRate", salesAttainmentRate);
		} else if ("getSalesAttainmentRateByQuarter".equals(method)) {
			//销售达成率按季度,全年季度
			String qyid = req.getParameter("qyid");
			XszbDto xszbDto = new XszbDto();
			String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            xszbDto.setKszq(currentYear);
			xszbDto.setJszq(currentYear);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("Q");
			xszbDto.setQyid(qyid);
			Map<String, Object> salesAttainmentRate;
			String key = "jsrq_getSalesAttainmentRateByQuarter_"+xszbDto.getQyid()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
			}
//			Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				}
			}
			sjxxDto.getZqs().put("salesAttainmentRate", currentYear + "~" + currentYear);
			map.put("salesAttainmentRate", salesAttainmentRate);
		} else if ("getSalesAttainmentRateByMon".equals(method)) {
			String qyid = req.getParameter("qyid");
			//销售达成率 默认6个月
			XszbDto xszbDto = new XszbDto();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH,-4);
			String start = DateUtils.format(calendar.getTime(), "yyyy-MM");
			String end = DateUtils.format(new Date(), "yyyy-MM");
			xszbDto.setKszq(start);
			xszbDto.setJszq(end);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("M");
			xszbDto.setQyid(qyid);
			Map<String, Object> salesAttainmentRate;
			String key = "jsrq_getSalesAttainmentRateByMon_"+xszbDto.getQyid()+"_"+sjxxDto.getZq();
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
			}
//			Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				}
			}
			sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
			map.put("salesAttainmentRate", salesAttainmentRate);
		}else if(method.startsWith("getTopDsf20")) {
			List<Map<String, String>> topDsf20List;
			String key = null;
//			Object topDsf20Listxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopDsf20ByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopDsf20ByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Week")){
				sjxxDto.setTj("week");
				setDateByWeek(sjxxDto);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopDsf20ByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Day")){
				sjxxDto.setTj("day");
				setDateByDay(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopDsf20ByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				topDsf20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				topDsf20List = sjxxTwoService.getTopDsf20(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topDsf20List));
			}
			map.put("topDsf20",topDsf20List);
		}else if(method.startsWith("getTopZx20")) {
			//在service里进行加入 特检销售发展部 平台限制（直销，CSO）
			sjxxDto.setSingle_flag("1");
			List<Map<String, String>> topZx20List;
			String key = null;
//			Object topZx20Listxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopZx20ByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopZx20ByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Week")){
				sjxxDto.setTj("week");
				setDateByWeek(sjxxDto);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopZx20ByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Day")){
				sjxxDto.setTj("day");
				setDateByDay(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopZx20ByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				topZx20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				topZx20List = sjxxTwoService.getTopZx20(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topZx20List));
			}
			map.put("topZx20",topZx20List);
		}else if(method.startsWith("getTopCSO20")) {
			//在service里进行加入 特检销售发展部 平台限制（直销，CSO）
			sjxxDto.setSingle_flag("1");
			List<Map<String, String>> topCSO20List;
			String key = null;
//			Object topCSO20Listxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopCSO20ByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopCSO20ByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Week")){
				sjxxDto.setTj("week");
				setDateByWeek(sjxxDto);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopCSO20ByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Day")){
				sjxxDto.setTj("day");
				setDateByDay(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopCSO20ByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				topCSO20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				sjxxDto.setXg_flg("1");
				topCSO20List = sjxxTwoService.getTopCSO20(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topCSO20List));
			}
			map.put("topCSO20",topCSO20List);
		}else if(method.startsWith("getTopRY20")) {
			List<Map<String, String>> topRY20List;
			String key = null;
//			Object topRY20Listxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopRY20ByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopRY20ByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Week")){
				sjxxDto.setTj("week");
				setDateByWeek(sjxxDto);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopRY20ByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Day")){
				sjxxDto.setTj("day");
				setDateByDay(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopRY20ByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				topRY20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				topRY20List = sjxxTwoService.getTopRY20(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topRY20List));
			}
			map.put("topRY20",topRY20List);
		}else if(method.startsWith("getTopHxyxList")) {
			List<Map<String, Object>> topHxyxList;
			String key = null;
//			Object topHxyxListxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopHxyxListByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopHxyxListByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Week")){
				sjxxDto.setTj("week");
				setDateByWeek(sjxxDto);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopHxyxListByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Day")){
				sjxxDto.setTj("day");
				setDateByDay(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getTopHxyxListByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				topHxyxList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				topHxyxList = sjxxTwoService.getHxyyTopList(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topHxyxList));
			}
			map.put("topHxyxList",topHxyxList);
		}else if(method.startsWith("getChargesDivideByKs")) {
			List<Map<String, Object>> ksSfcssList;
			String key = null;
//			Object ksSfcssListxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("ksSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getChargesDivideByKsByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("ksSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getChargesDivideByKsByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Quarter")){
				sjxxDto.setTj("mon");
				String currentYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				String start1 = currentYear1 + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				String end1 = currentYear1 + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				sjxxDto.setJsrqMstart(start1);
				sjxxDto.setJsrqMend(end1);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("ksSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getChargesDivideByKsByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				ksSfcssList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				ksSfcssList = sjxxTwoService.getChargesDivideByKs(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ksSfcssList));
			}
			map.put("ksSfcssList",ksSfcssList);
		}else if(method.startsWith("getChargesDivideByYblx")) {
			List<Map<String, Object>> yblxSfcssList;
			String key = null;
//			Object yblxSfcssListxx = null;
			if (method.contains("Year")){
				sjxxDto.setTj("year");
				setDateByYear(sjxxDto,0,false);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqYstart(null);
					sjxxDto.setJsrqYend(null);
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				}
				sjxxDto.getZqs().put("yblxSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getChargesDivideByYblxByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Mon")){
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,0);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("yblxSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getChargesDivideByYblxByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}else if (method.contains("Quarter")){
				sjxxDto.setTj("mon");
				String currentYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				String start1 = currentYear1 + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				String end1 = currentYear1 + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				sjxxDto.setJsrqMstart(start1);
				sjxxDto.setJsrqMend(end1);
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqMstart(null);
					sjxxDto.setJsrqMend(null);
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				}
				sjxxDto.getZqs().put("yblxSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				key = "jsrq_getChargesDivideByYblxByQuarter"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd();
			}
			Map<String, Object> objMap = redisUtil.hgetStatistics(key);
			if ( "weekLeadStatis".equals(objMap.get("key")) ){
				yblxSfcssList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
			}else {
				yblxSfcssList = sjxxTwoService.getChargesDivideByYblx(sjxxDto);
				redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(yblxSfcssList));
			}
			map.put("yblxSfcssList",yblxSfcssList);
		}
		map.put("searchData", sjxxDto);
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatis")==-1){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		return map;
	}

	/**
	  * 获取标本路线信息
	 * @param sjxxdto
	 * @return
	 */
	@RequestMapping(value = "/statistics/getYblx")
	@ResponseBody
	public Map<String,Object> getYblx(SjxxDto sjxxdto){
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(sjxxdto.getJsrqstart())) {
			List<Map<String,Object>> yblxlist=sjxxService.getSjxlxx(sjxxdto);
			map.put("yblxlist", yblxlist);
		}else {
			map.put("yblxlist", null);
		}
		return map;
	}

	/**
	 * 设置按年查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几年
	 * @param isBgFlag 是否设置报告日期标记
	 */
	private void setDateByYear(SjxxDto sjxxDto,int length,boolean isBgFlag) {
		setDateByYear(sjxxDto,sjxxDto.getJsrqstart(),length,isBgFlag);
	}

	/**
	 * 设置按年查询的日期
	 * @param sjxxDto
	 * @param date 标准日期
	 * @param length 长度。要为负数，代表向前推移几年
	 * @param isBgFlag 是否设置报告日期标记
	 */
	private void setDateByYear(SjxxDto sjxxDto,String date,int length,boolean isBgFlag) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy");

		Calendar calendar=Calendar.getInstance();
		if(StringUtil.isNotBlank(date)) {
			Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
			calendar.setTime(jsDate);
		}
		if ("NOW".equals(sjxxDto.getTjtj())){
			Date nowDate = new Date();
			calendar.setTime(nowDate);
			int yearDay = calendar.get(Calendar.DAY_OF_YEAR);
			if (yearDay<21){
				calendar.add(Calendar.YEAR,-1);
			}
		}
		if(length >= 0) {
			sjxxDto.setJsrqYstart(monthSdf.format(calendar.getTime()));
			calendar.add(Calendar.YEAR, length);
			sjxxDto.setJsrqYend(monthSdf.format(calendar.getTime()));
		}else {
			sjxxDto.setJsrqYend(monthSdf.format(calendar.getTime()));
			calendar.add(Calendar.YEAR, length);
			sjxxDto.setJsrqYstart(monthSdf.format(calendar.getTime()));
		}

		if(isBgFlag) {
			sjxxDto.setBgrqYstart(sjxxDto.getJsrqYstart());
			sjxxDto.setBgrqYend(sjxxDto.getJsrqYend());
		}
		sjxxDto.setJsrqstart(null);
		sjxxDto.setJsrqend(null);
	}

	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(SjxxDto sjxxDto,int length) {
		setDateByMonth(sjxxDto,sjxxDto.getJsrqstart(),length);
	}

	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param date 标准日期
	 * @param length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(SjxxDto sjxxDto,String date,int length) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");

		Calendar calendar=Calendar.getInstance();
		if(StringUtil.isNotBlank(date)) {
			Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
			calendar.setTime(jsDate);
		}
		if ("NOW".equals(sjxxDto.getTjtj())){
			Date nowDate = new Date();
			calendar.setTime(nowDate);
			int monthDay=calendar.get(Calendar.DAY_OF_MONTH);
			if (monthDay<7){
				calendar.add(Calendar.MONTH,-1);
			}
		}
		if(length>=0) {
			sjxxDto.setJsrqMstart(monthSdf.format(calendar.getTime()));
			calendar.add(Calendar.MONTH,length);
			sjxxDto.setJsrqMend(monthSdf.format(calendar.getTime()));
		}else {
			sjxxDto.setJsrqMend(monthSdf.format(calendar.getTime()));
			calendar.add(Calendar.MONTH,length);
			sjxxDto.setJsrqMstart(monthSdf.format(calendar.getTime()));
		}

		sjxxDto.setBgrqMstart(sjxxDto.getJsrqMstart());
		sjxxDto.setBgrqMend(sjxxDto.getJsrqMend());

		sjxxDto.setJsrqstart(null);
		//sjxxDto.setJsrqend(null);
	}

	/**
	 * 设置周的日期
	 * length 长度。要为负数，代表向前推移几天
	 * @param sjxxDto
	 */
	private void setDateByWeek(SjxxDto sjxxDto) {
		setDateByWeek(sjxxDto,sjxxDto.getJsrqstart(),1);
	}

	/**
	 * 设置周的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 * @param date 标准日期
	 * @param length 几周
	 */
	private void setDateByWeek(SjxxDto sjxxDto,String date,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
		if(StringUtil.isBlank(date)) {
			int dayOfWeek = DateUtils.getWeek(jsDate);
			//往后的周
			if(length >= 0) {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1 + 7*(length-1))));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek )));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek + 7*(length-1))));
				}
			}else {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7*length)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1)));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7*(length-1))));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek)));
				}
			}
		}else if( length > 1 || length < -1) {
			int dayOfWeek = DateUtils.getWeek(jsDate);
			//往后的周
			if(length >= 0) {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1 + 7*(length-1))));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek )));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek + 7*(length-1))));
				}
			}else {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek + 7*length)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1)));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1 + 7*(length+1))));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 5 - dayOfWeek)));
				}
			}
		}
		sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
		sjxxDto.setBgrqend(sjxxDto.getJsrqend());
	}

	/**
	 * 设置按天查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(SjxxDto sjxxDto,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");

		if(length >=0 ) {
			sjxxDto.setJsrqstart(daySdf.format(new Date()));
			sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), length)));
		}else {
			sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), length)));
			sjxxDto.setJsrqend(daySdf.format(new Date()));
		}
		sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
		sjxxDto.setBgrqend(sjxxDto.getJsrqend());

	}


	/**
	 * 设置按天查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(SjxxDto sjxxDto,String date,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");

		if(StringUtil.isNotBlank(date)) {
			if(length >=0 ) {
				sjxxDto.setJsrqstart(date);
				sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",date), length)));
			}else {
				sjxxDto.setJsrqend(date);
				sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",date), length)));
			}
		}else if(length ==0){
			sjxxDto.setJsrqstart(date);
			sjxxDto.setJsrqend(sjxxDto.getJsrqstart());
		}
		sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
		sjxxDto.setBgrqend(sjxxDto.getJsrqend());
	}

	/**
	 * 日报统计，跳转日报页面
	 * @return
	 */
	@RequestMapping("/statistics/getDaily")
	public ModelAndView getDaily(SjxxDto sjxxDto,HttpServletRequest request) {
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		if(!checkSign) {
            return commonService.jumpError();
		}
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead");
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrq()));
		String load_flg="0";
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos", xszbDtos);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	/**
	 * 日报统计，跳转日报页面
	 * @return
	 */
	@RequestMapping("/statistics/getRfsDaily")
	public ModelAndView getRfsDaily(SjxxDto sjxxDto,HttpServletRequest request) {
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		if(!checkSign) {
            return commonService.jumpError();
		}
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_rfsday_lead");
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrq()));
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 日报统计，跳转日报页面(点击检测单位按钮)
	 * @return
	 */
	@RequestMapping("/statistics/getDailyByJcdw")
	public ModelAndView getDailyByJcdw(SjxxDto sjxxDto,HttpServletRequest request) {
		String load_flg=request.getParameter("load_flg");
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead_jcdw");
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrq()));

        List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssByDayAndJcdw(sjxxDto);

        //字符串替换
		if(jcxmDRList!=null && jcxmDRList.size()>0) {
			for(int i=0;i<jcxmDRList.size();i++) {
				jcxmDRList.get(i).put("jcdwmc",jcxmDRList.get(i).get("jcdwmc").replace("实验室", ""));
			}
		}
		mav.addObject("jcdwDtos", jcxmDRList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	/**
	 * 日报统计，跳转日报页面(点击检测单位按钮)(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getDailyByJcdwAndJsrq")
	public ModelAndView getDailyByJcdwAndJsrq(SjxxDto sjxxDto,HttpServletRequest request) {
		String load_flg=request.getParameter("load_flg");
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead_jcdw_jsrq");
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrq()));

        List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssByDayAndJcdw(sjxxDto);

        //字符串替换
		if(jcxmDRList!=null && jcxmDRList.size()>0) {
			for(int i=0;i<jcxmDRList.size();i++) {
				jcxmDRList.get(i).put("jcdwmc",jcxmDRList.get(i).get("jcdwmc").replace("实验室", ""));
			}
		}
		mav.addObject("jcdwDtos", jcxmDRList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}

	/**
	 * 日报统计，跳转日报页面(点击所有伙伴按钮)
	 * @return
	 */
	@RequestMapping("/statistics/getDailyBySjhb")
	public ModelAndView getDailyBySjhb(SjxxDto sjxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead_sjhb");
		List<SjxxDto> sjxxList=sjxxService.getAllSjhb(sjxxDto);
		mav.addObject("sjxxList", sjxxList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 日报统计，跳转日报页面(点击所有伙伴按钮)(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getDailyBySjhbAndJsrq")
	public ModelAndView getDailyBySjhbAndJsrq(SjxxDto sjxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead_sjhb_jsrq");
		List<SjxxDto> sjxxList=sjxxService.getAllSjhb(sjxxDto);
		mav.addObject("sjxxList", sjxxList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 日报统计，由日报页面发起的获取相应信息
	 * @return
	 */
	@RequestMapping("/statistics/getSfsfybxx")
	@ResponseBody
	public Map<String, Object> getSfsfybxx(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		sjxxDto.setBgrq(sjxxDto.getJsrq());
		if(checkSign) {
			//标本信息
			List<Map<String,String>> ybxxMap = sjxxService.getYbxxByDay(sjxxDto);
			map.put("ybxx", ybxxMap);
			//ResFirst™标本信息
			List<Map<String,String>> rybxxMap=sjxxResStatisticService.getRYbxxByDay(sjxxDto);
			Map<String, Object> rfssyMap = new HashMap<>();
			SjxxDto sjxxDto_t = new SjxxDto();
			sjxxDto_t.setJsrq(sjxxDto.getJsrq());
			sjxxDto_t.setCskz3("IMP_REPORT_RFS_TEMEPLATE");
			Map<String,Integer> rfssjfj = sjxxResStatisticService.getSjFjNum(sjxxDto_t);
			rfssyMap.put("rybxx", rybxxMap);
			rfssyMap.put("rfssjfj",rfssjfj);
			map.put("rybxx", rfssyMap);
			//ResFirst™各周期标本数
			Map<String, String> rfszqbbs = sjxxResStatisticService.getLifeCount(sjxxDto);
			map.put("rfszqbbs",rfszqbbs);
			//当日ResFirst™各周期标本用时
			SjxxDto sjxxDto_r = new SjxxDto();
			sjxxDto_r.setJsrq(sjxxDto.getJsrq());
			sjxxDto_r.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, String>> rfsbbys = sjxxResStatisticService.getLifeTimeCount(sjxxDto_r);
			map.put("rfsbbys",rfsbbys);
			//合作伙伴统计
			List<Map<String,String>> sjhbMap=sjxxService.getYbxxByTjmc(sjxxDto);
			if(sjhbMap!=null&&sjhbMap.size()>39) {
				List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
				asc_YbxxBySjh_daily_Map.addAll(sjhbMap.subList(0,39));
				map.put("sjhb", asc_YbxxBySjh_daily_Map);
				Collections.reverse(sjhbMap); // 倒序排列
			}else {
				map.put("sjhb", sjhbMap);
			}
			//统计复检申请
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsj(sjxxDto.getJsrq());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
			//统计废弃标本
			List<Map<String,String>> fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb", fqybMap);
			//统计报高阳性率
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possiblelist",possiblelist);
			//统计物种信息
			//List<Map<String, String>> species=sjxxService.getWzxxBySjid(sjxxDto);
			/*
			 * Map<String,String> testmap=new HashMap<String, String>();
			 * testmap.put("num","5"); testmap.put("wzzwm", "wzzwm");
			 * List<Map<String, String>> species=new
			 * ArrayList<Map<String,String>>(); for (int i = 0; i < 1000; i++){
			 * species.add(testmap); }
			 */
			//map.put("species",species);
			//送检清单
			List<SjxxDto> sjxxList=sjxxService.getListBydaily(sjxxDto);
			map.put("sjxxList",sjxxList);
			//检测项目个数
            List<Map<String,String>> jcxmDRList=sjxxService.getYbxxDRByDay(sjxxDto);
            map.put("jcxmnum",jcxmDRList);
            //收费标本里边检测项目条数
            List<Map<String,String>> jcxmTypeList=sjxxService.getYbxxTypeByDay(sjxxDto);
            map.put("jcxmType",jcxmTypeList);
            List<SjxxDto> notSylist=sjxxService.getNotSyList(sjxxDto);
            map.put("notSylist",notSylist);
         	//销售达成率日统计，默认当前季度
			XszbDto xszbDto = new XszbDto();
			Date searchDate = DateUtils.parseDate("yyyy-MM", sjxxDto.getJsrq());
			String start = DateUtils.format(searchDate);
			String end = "";
			xszbDto.setKszq(start);
			xszbDto.setJszq(end);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("Q");
			Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("xszblist_"+map1,salesAttainmentRate.get(map1));
				}
			}
			//查询ResFirst™的销售达成率
			xszbDto.setCskz3("IMP_REPORT_RFS_TEMEPLATE");
			Map<String,Object> salesRfsAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
			if(salesRfsAttainmentRate!=null && salesRfsAttainmentRate.size()>0){
				for (String map1 : salesRfsAttainmentRate.keySet()) {
					map.put("xszbrfslist_"+map1,salesRfsAttainmentRate.get(map1));
				}
			}
			map.put("searchData", sjxxDto);
			return map;
		}
		return map;
	}
	/**
	 * 日报统计，由日报页面发起的获取相应信息
	 * @return
	 */
	@RequestMapping("/statistics/getRfsYbxx")
	@ResponseBody
	public Map<String, Object> getRfsYbxx(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		if(checkSign) {
			//ResFirst™各周期标本数
			Map<String, String> rfszqbbs = sjxxResStatisticService.getLifeCount(sjxxDto);
			map.put("rfszqbbs",rfszqbbs);
			//当日ResFirst™各周期标本用时
			SjxxDto sjxxDto_r = new SjxxDto();
			sjxxDto_r.setJsrq(sjxxDto.getJsrq());
			sjxxDto_r.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
			List<Map<String, String>> rfsbbys = sjxxResStatisticService.getLifeTimeCount(sjxxDto_r);
			map.put("rfsbbys",rfsbbys);
			//日rfs各周期平均用时
			sjxxDto.setJsrqstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqend(sjxxDto.getJsrq());
			sjxxDto.setTj("day");
			List<String> rqs = new ArrayList<>();
			rqs.add(sjxxDto.getJsrq());
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> rrfspjys = sjxxResStatisticService.getAvgTime(sjxxDto);
			map.put("rrfspjys", rrfspjys);
			map.put("searchData", sjxxDto);
			return map;
		}
		return map;
	}
	/**
	 * 日报统计，由日报页面发起的获取相应信息(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getSfsfybxxByJsrq")
	@ResponseBody
	public Map<String, Object> getSfsfybxxByJsrq(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		sjxxDto.setBgrq(sjxxDto.getJsrq());
		if(checkSign) {
			//标本信息
			List<Map<String,String>> ybxxMap=sjxxService.getYbxxByDayAndJsrq(sjxxDto);
			map.put("ybxx", ybxxMap);
			//合作伙伴统计
			List<Map<String,String>> sjhbMap=sjxxService.getYbxxByTjmcAndJsrq(sjxxDto);
			if(sjhbMap!=null&&sjhbMap.size()>39) {
				List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
				asc_YbxxBySjh_daily_Map.addAll(sjhbMap.subList(0,39));
				map.put("sjhb", asc_YbxxBySjh_daily_Map);
				Collections.reverse(sjhbMap); // 倒序排列
			}else {
				map.put("sjhb", sjhbMap);
			}
			//统计复检申请
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsj(sjxxDto.getJsrq());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
			//统计废弃标本
			List<Map<String,String>> fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb", fqybMap);
			//统计报高阳性率
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possiblelist",possiblelist);
			//统计物种信息
			//List<Map<String, String>> species=sjxxService.getWzxxBySjid(sjxxDto);
			/*
			 * Map<String,String> testmap=new HashMap<String, String>();
			 * testmap.put("num","5"); testmap.put("wzzwm", "wzzwm");
			 * List<Map<String, String>> species=new
			 * ArrayList<Map<String,String>>(); for (int i = 0; i < 1000; i++){
			 * species.add(testmap); }
			 */
			//map.put("species",species);
			//送检清单
			List<SjxxDto> sjxxList=sjxxService.getListBydailyAndJsrq(sjxxDto);
			map.put("sjxxList",sjxxList);
			//检测项目个数
            List<Map<String,String>> jcxmDRList=sjxxService.getYbxxDRByDayAndJsrq(sjxxDto);
            map.put("jcxmnum",jcxmDRList);
            //收费标本里边检测项目条数
            List<Map<String,String>> jcxmTypeList=sjxxService.getYbxxTypeByDayAndJsrq(sjxxDto);
            map.put("jcxmType",jcxmTypeList);
            List<SjxxDto> notSylist=sjxxService.getNotSyListByJsrq(sjxxDto);
            map.put("notSylist",notSylist);
         	//销售达成率日统计，默认当前季度
			XszbDto xszbDto = new XszbDto();
			Date searchDate = DateUtils.parseDate("yyyy-MM", sjxxDto.getJsrq());
			String start = DateUtils.format(searchDate);
			String end = "";
			xszbDto.setKszq(start);
			xszbDto.setJszq(end);
			xszbDto.setJsmc("大区经理");
			xszbDto.setZblxcsmc("Q");
			Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
			if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				for (String map1 : salesAttainmentRate.keySet()) {
					map.put("xszblist_"+map1,salesAttainmentRate.get(map1));
				}
			}
			map.put("searchData", sjxxDto);
			return map;
		}
		return map;
	}

	/**
	 * 日报统计，由日报页面点击检测单位按钮发起的获取相应信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/getCssxxByJcdw")
	@ResponseBody
	public Map<String, Object> getCssxxByJcdw(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		sjxxDto.setBgrq(sjxxDto.getJsrq());
		if(checkSign) {
            List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssByDayAndJcdw(sjxxDto);
            map.put("jcdwccs",jcxmDRList);
            List<Map<String,String>> jcdwlists=sjxxService.getYbxxDRByDayAndJcdw(sjxxDto);

    		for(int i=0;i<jcdwlists.size();i++) {
    			if(map.containsKey("jcdwccs_"+jcdwlists.get(i).get("jcdw"))) {
    				((List<Map<String,String>>)map.get("jcdwccs_"+jcdwlists.get(i).get("jcdw"))).add(jcdwlists.get(i));
    			}else {
    				Map<String,String> tDto = jcdwlists.get(i);
    				List<Map<String,String>> t_list = new ArrayList<>();
    				t_list.add(tDto);
    				map.put("jcdwccs_"+jcdwlists.get(i).get("jcdw"),t_list);
    			}
    		}

			map.put("searchData", sjxxDto);
			return map;
		}
		return map;
	}
	/**
	 * 日报统计，由日报页面点击检测单位按钮发起的获取相应信息(接收日期)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/statistics/getCssxxByJcdwAndJsrq")
	@ResponseBody
	public Map<String, Object> getCssxxByJcdwAndJsrq(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		sjxxDto.setBgrq(sjxxDto.getJsrq());
		if(checkSign) {
            List<Map<String,String>> jcxmDRList=sjxxService.getSjxxcssByDayAndJcdwAndJsrq(sjxxDto);
            map.put("jcdwccs",jcxmDRList);
            List<Map<String,String>> jcdwlists=sjxxService.getYbxxDRByDayAndJcdwAndJsrq(sjxxDto);

    		for(int i=0;i<jcdwlists.size();i++) {
    			if(map.containsKey("jcdwccs_"+jcdwlists.get(i).get("jcdw"))) {
    				((List<Map<String,String>>)map.get("jcdwccs_"+jcdwlists.get(i).get("jcdw"))).add(jcdwlists.get(i));
    			}else {
    				Map<String,String> tDto = jcdwlists.get(i);
    				List<Map<String,String>> t_list = new ArrayList<>();
    				t_list.add(tDto);
    				map.put("jcdwccs_"+jcdwlists.get(i).get("jcdw"),t_list);
    			}
    		}

			map.put("searchData", sjxxDto);
			return map;
		}
		return map;
	}

	/**
	 * 标本数统计点击查看列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/BbsListView")
	public ModelAndView BbsListView(SjxxDto sjxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sampleList");
		List<SjxxDto> sjxxList=sjxxService.getBbsList(sjxxDto);
		mav.addObject("sjxxList", sjxxList);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 标本数统计点击查看列表(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/BbsListViewByJsrq")
	public ModelAndView BbsListViewByJsrq(SjxxDto sjxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sampleList_jsrq");
		List<SjxxDto> sjxxList=sjxxService.getBbsListByJsrq(sjxxDto);
		mav.addObject("sjxxList", sjxxList);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 复检统计点击查看列表
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/FjsqListView")
	public ModelAndView FjsqListView(FjsqDto fjsqDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/recheckList");
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	/**
	 * 复检统计点击查看列表(接收日期)
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/FjsqListViewByJsrq")
	public ModelAndView FjsqListViewByJsrq(FjsqDto fjsqDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/recheckList_jsrq");
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}

	/**
	 * 统计时点击查看列表
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/RecheckStatisPage")
	@ResponseBody public Map<String, Object> RecheckStatisPage(FjsqDto fjsqDto){
		Map<String, Object> map= new HashMap<>();
		if("null".equals(fjsqDto.getLrsj())) { fjsqDto.setLrsj(""); } List<FjsqDto>
		fjsqList=fjsqService.getPagedStatisRecheck(fjsqDto); map.put("total",
		fjsqDto.getTotalNumber()); map.put("rows", fjsqList); return map;
	}

	/**
	 * 废弃标本统计点击查看页面
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/fqybListView")
	public ModelAndView FqybListView(SjybztDto sjybztDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sampleState");
		if(sjybztDto.getZt().equals("0")) {
			sjybztDto.setZtflg("0");
		}else {
			sjybztDto.setZtflg("1");
		}
		mav.addObject("sjybztDto", sjybztDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	/**
	 * 废弃标本统计点击查看页面(接收日期)
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/fqybListViewByJsrq")
	public ModelAndView FqybListViewByJsrq(SjybztDto sjybztDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sampleState_jsrq");
		if(sjybztDto.getZt().equals("0")) {
			sjybztDto.setZtflg("0");
		}else {
			sjybztDto.setZtflg("1");
		}
		mav.addObject("sjybztDto", sjybztDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}

	/**
	 * 统计时点击查看列表
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/sample/sampleStatePage")
	@ResponseBody
	public Map<String, Object> sampleStatePage(SjybztDto sjybztDto){
		Map<String, Object> map= new HashMap<>();
		if("null".equals(sjybztDto.getJsrq())) {
			sjybztDto.setJsrq("");
		}
		List<SjxxDto> sjxxList=sjxxService.getPageFqybList(sjybztDto);
		map.put("total", sjybztDto.getTotalNumber());
		map.put("rows", sjxxList);
		return map;
	}

	/**
	 * 根据 患者姓名，合作伙伴的信息，去查找非本标本编号的其他同类信息(生信部接口)
	 * @param ybbh
	 * @return
	 */
	@RequestMapping(value ="/getOtherSjxxsl")
	@ResponseBody
	public String getOtherSjxxsl(String ybbh) {
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setYbbh(ybbh);
		SjxxDto sjxx=sjxxService.getDto(sjxxDto);
		if(sjxx==null) {
			return ybbh+"不存在!";
		}else {
			FjsqDto fjsqDto = new FjsqDto();
			fjsqDto.setSjid(sjxx.getSjid());
			fjsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
			List<FjsqDto> sjsqDtos=fjsqService.getDtoList(fjsqDto);
			if(sjsqDtos!=null && sjsqDtos.size()>0) {
				sjxx.setFjid(sjsqDtos.get(0).getFjid());
			}
			List<SjxxDto> sjtlxxlist=sjxxService.getTlxxList(sjxx);
			if(sjtlxxlist==null || sjtlxxlist.size()==0) {
				return "0";
			}else {
				return String.valueOf(sjtlxxlist.size());
			}
		}
	}

	/**
	 * 访问路径封装(生信部访问送检信息)
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/packageurl")
	public ModelAndView alipayforward(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mav=new ModelAndView("common/view/display_viewpc");
		String ybbh =req.getParameter("ybbh");
		String url = "/ws/getOtherSjxxPage?ybbh="+ybbh;
		mav.addObject("view_url", url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 跳转送检信息页面(生信部用)
	 * @param ybbh
	 * @return
	 */
	@RequestMapping(value="/getOtherSjxxPage")
	public ModelAndView getOtherSjxxPage(String ybbh) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sx_pagelist");
		mav.addObject("ybbh", ybbh);
		return mav;
	}

	/**
	 * 送检信息列表(生信部用)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/getsjxxpage")
	@ResponseBody
	public Map<String,Object> getOtherSjxxList(SjxxDto sjxxDto){
		Map<String, Object> map= new HashMap<>();
		SjxxDto sjxx=sjxxService.getDto(sjxxDto);
		if(sjxx!=null) {
			FjsqDto fjsqDto = new FjsqDto();
			fjsqDto.setSjid(sjxx.getSjid());
			fjsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
			int cnt=fjsqService.getCount(fjsqDto);
			if(cnt > 0) {
				sjxxDto.setFjid("1");
			}
			sjxxDto.setBgrq(sjxx.getBgrq());
			sjxxDto.setHzxm(sjxx.getHzxm());
			sjxxDto.setYbbh(sjxx.getYbbh());
			sjxxDto.setDb(sjxx.getDb());
			List<SjxxDto> sjtlxxlist=sjxxService.getPagedTlxxList(sjxxDto);
			map.put("total",sjxxDto.getTotalNumber());
			map.put("rows",sjtlxxlist);
		}else {
			map.put("error", "您输入的标本编号不存在!");
		}
		return map;
	}

	@RequestMapping(value="/sjxxpageview")
	public ModelAndView getOtherSjxxView(SjxxDto sjxxDto) {
		List<SjxxDto> sjxxlist=sjxxService.selectSjxxBySjids(sjxxDto);
		List<SjxxDto> sjxxtablist= new ArrayList<>();
		for(int i=0;i<sjxxDto.getIds().size();i++) {
			SjxxDto sjxx=new SjxxDto();
			sjxxtablist.add(sjxx);
			sjxxtablist.get(i).setSjid(sjxxDto.getIds().get(i));
			sjxxtablist.get(i).setYbbh(sjxxDto.getYbbhs().get(i));
		}
		List<SjwzxxDto> sjwzxx=sjxxService.selectWzxxBySjids(sjxxDto);
		ModelAndView mav=new ModelAndView("wechat/sjxx/sx_pageview");
		mav.addObject("sjxxList", sjxxlist);
		mav.addObject("Sjwzxx", sjwzxx);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("sjxxtablist", sjxxtablist);
		return mav;
	}

	/**
	 * 获取参考指标解释页面
	 * @return
	 */
	@RequestMapping(value="/getckzbjs")
	public ModelAndView getckzbjsPage() {
        return new ModelAndView("wechat/sjxx/sjxx_ckzbjs");
	}

	/**
	 * 发送周报消息(合作伙伴)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/sendWeekReport")
	@ResponseBody
	public Map<String, Object> sendWeekReport(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isBlank(kpi_templateid)){
			map.put("status", "fail");
			log.error("sendWeekReport  kpi_templateid为空值！");
			return map;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int dayOfWeek = DateUtils.getWeek(new Date());
		cal.set(Calendar.DAY_OF_WEEK, 1);
		if(dayOfWeek <= 2){
			cal.add(Calendar.DAY_OF_YEAR, -7);
		}
		String startdate = DateUtils.format(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, 7);
		String enddate = DateUtils.format(cal.getTime());
		//List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getDB();
		sjxxDto.setJsrqstart(startdate);
		sjxxDto.setJsrqend(enddate);
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectDtoByJsrq(sjxxDto);
		if(sjhbxxDtos != null && sjhbxxDtos.size() > 0){
			String reporturl = menuurl+"/wechat/packging_weekly?jsrqstart="+startdate+"&jsrqend="+enddate;
			for (int i = 0; i < sjhbxxDtos.size(); i++) {
				if(NoticeTypeEnum.WECAHT.getCode().equals(sjhbxxDtos.get(i).getFsfs())){
					try {
						String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(startdate+enddate+sjhbxxDtos.get(i).getHbmc()),"UTF-8"),"UTF-8");
						reporturl = reporturl+"&db="+URLEncoder.encode(URLEncoder.encode(sjhbxxDtos.get(i).getHbmc(),"UTF-8"),"UTF-8")+"&sign="+ sign;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					Map<String, String> messageMap = new HashMap<>();
//					messageMap.put("title", "您好！");
//					messageMap.put("keyword1", startdate+" ~ "+enddate);
//					messageMap.put("keyword2", "请点击查看");
//					messageMap.put("keyword3", null);
//					messageMap.put("keyword4", null);
//					messageMap.put("reporturl", reporturl);
//					messageMap.put("remark", "送检统计已生成，快点击查看吧！");
//					commonService.sendWeChatMessageMap("templatedm",sjhbxxDtos.get(i).getWxid(),null,messageMap);
					commonService.sendWeChatMessage(kpi_templateid, sjhbxxDtos.get(i).getWxid(), "您好！", startdate+" ~ "+enddate, "请点击查看", null, null, "送检统计已生成，快点击查看吧！", reporturl);
					map.put("status", "success");
				}
			}
		}else{
			map.put("status", "fail");
			log.error("sendWeekReport  未找到合作伙伴信息！");
		}
		return map;
	}

	/**
	 * 发送周报消息(录入人员)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/sendWeekUserReport")
	@ResponseBody
	public Map<String, Object> sendWeekUserReport(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isBlank(kpi_templateid)){
			map.put("status", "fail");
			log.error("sendWeekUserReport  kpi_templateid为空值！");
			return map;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int dayOfWeek = DateUtils.getWeek(new Date());
		cal.set(Calendar.DAY_OF_WEEK, 1);
		if(dayOfWeek <= 2){
			cal.add(Calendar.DAY_OF_YEAR, -7);
		}
		String startdate = DateUtils.format(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, 7);
		String enddate = DateUtils.format(cal.getTime());
		sjxxDto.setJsrqstart(startdate);
		sjxxDto.setJsrqend(enddate);
		List<WxyhDto> wxyhDtos = wxyhService.selectDtoByJsrq(sjxxDto);
		if(wxyhDtos != null && wxyhDtos.size() > 0){
			for (int i = 0; i < wxyhDtos.size(); i++) {
				String reporturl = menuurl+"/wechat/packging_weekly?jsrqstart="+startdate+"&jsrqend="+enddate;
				try {
					String sign = URLEncoder.encode(URLEncoder.encode(commonService.getSign(startdate+enddate+wxyhDtos.get(i).getWxid()),"UTF-8"),"UTF-8");
					reporturl = reporturl+"&lrry="+URLEncoder.encode(URLEncoder.encode(wxyhDtos.get(i).getWxid(),"UTF-8"),"UTF-8")+"&sign="+ sign;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error("sendWeekUserReport  转译异常！"+e.toString());
				}
				//临时测试 start
				if(i > 3) {
					map.put("status", "success");
					return map;
				}
				//临时测试 end   wxyhDtos.get(i).getWxid()
//					Map<String, String> messageMap = new HashMap<>();
//					messageMap.put("title", "您好！");
//					messageMap.put("keyword1", startdate+" ~ "+enddate);
//					messageMap.put("keyword2", "请点击查看");
//					messageMap.put("keyword3", null);
//					messageMap.put("keyword4", null);
//					messageMap.put("reporturl", reporturl);
//					messageMap.put("remark", "送检统计已生成，快点击查看吧！");
//					commonService.sendWeChatMessageMap("templatedm","oj0QJ58d0HnbQ_xraXs0yrOR5clA",null,messageMap);
				commonService.sendWeChatMessage(kpi_templateid, "oj0QJ58d0HnbQ_xraXs0yrOR5clA", "您好！", startdate+" ~ "+enddate, "请点击查看", null, null, "送检统计已生成，快点击查看吧！", reporturl);
				map.put("status", "success");
			}
		}else{
			map.put("status", "fail");
			log.error("sendWeekUserReport  未找到合作伙伴信息！");
		}
		return map;
	}

	/**
	 * 复检审核列表
	 * @param req
	 * @param fjsqDto
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/auditProcess/auditPhoneList")
	public ModelAndView auditPhoneList(FjsqDto fjsqDto,HttpServletRequest req){
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_auditPhoneList");
		fjsqDto.setShlx(AuditTypeEnum.AUDIT_OUT_RECHECK.getCode());
		ShlbDto shlbDto=shlbService.getDtoById(fjsqDto.getShlx());
		String shlbmc=shlbDto.getShlbmc();
		boolean result =commonService.checkSign(fjsqDto.getSign(),fjsqDto.getYhid(), req);
		if(!result) {
			return commonService.jumpAuditError();
		}
		List<FjsqDto> fjsqDtos=fjsqService.getListAuditPhone(fjsqDto);
		try{
			shgcService.addShgcxxByYwid(fjsqDtos,AuditTypeEnum.AUDIT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()
			});
			shgcService.addShgcxxByYwid(fjsqDtos,AuditTypeEnum.AUDIT_OUT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()
			});
			shgcService.addShgcxxByYwid(fjsqDtos,AuditTypeEnum.AUDIT_DING_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()
					});
			shgcService.addShgcxxByYwid(fjsqDtos,AuditTypeEnum.AUDIT_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()
			});
			shgcService.addShgcxxByYwid(fjsqDtos,AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()
			});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < fjsqDtos.size(); i++){
			SpgwcyDto spgwcyDto=new SpgwcyDto();
			spgwcyDto.setGwid(fjsqDtos.get(i).getShxx_dqgwid());
			List<SpgwcyDto> gwcyList=spgwcyService.getDtoList(spgwcyDto);

			List<String> strList= new ArrayList<>();
			if(gwcyList!=null && gwcyList.size()>0) {
				for (int j = 0; j < gwcyList.size(); j++){
					strList.add(gwcyList.get(j).getYhid());
				}
			}
			boolean bool=strList.contains(fjsqDto.getYhid());
			if(bool) {
				fjsqDtos.get(i).setFlg("1");
			}else {
				fjsqDtos.get(i).setFlg("0");
			}
			try{
				String sign= URLEncoder.encode(commonService.getSign(fjsqDtos.get(i).getFjid()),"utf-8");
				fjsqDtos.get(i).setSign(sign);
			} catch (UnsupportedEncodingException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mav.addObject("fjsqDtos", fjsqDtos);
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("shlbmc", shlbmc);
		return mav;
	}

	/**
	 * 复检修改页面
	 * @param fjid
	 * @return
	 */
	@RequestMapping("/recheck/modRecheck")
	public ModelAndView modRecheck(String fjid) {
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_mod");
		FjsqDto fjsqDto=fjsqService.getDtoById(fjid);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("detectlist",  redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//送检单位
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
		if(sjxxDtos!=null) {
			mav.addObject("sjxxDto", sjxxDtos);
		}else {
			mav.addObject("sjxxDto", sjxxDto);
		}
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("modflag", "0");
		return mav;
	}

	/**
	 * 修改检测单位（复检）
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value="/recheck/pagedataUpdateJcdw")
	@ResponseBody
	public Map<String,Object> updateJcdw(FjsqDto fjsqDto,String yjcdw){
		Map<String,Object> map= new HashMap<>();
		boolean result=fjsqService.updateJcdw(fjsqDto,yjcdw);
		if(result) {
			fjsqService.sendUpdateJcdwMessage(fjsqDto);
		}
		map.put("status", result);
		return map;
	}

	/**
	 * 修改检测单位（送检验证）
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping(value="/verification/updateJcdwVerification")
	@ResponseBody
	public Map<String,Object> updateJcdwVerification(SjyzDto sjyzDto){
		Map<String,Object> map= new HashMap<>();
		boolean result=sjyzService.update(sjyzDto);
		if(result) {
			sjyzService.sendUpdateJcdwMessage(sjyzDto);
		}
		map.put("status", result);
		return map;
	}

	/**
	 * 查询不同状态的标本的占比
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/count/getpercentage")
	@ResponseBody
	public Map<String, String> getPercentage(SjybztDto sjybztDto){
		Map<String,String> map= new HashMap<>();
		if(sjybztDto.getZt().equals("0")) {//状态为0的是没有选择标本状态的
			sjybztDto.setZtflg("1");
			sjybztDto.setZt("");
			Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
			Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
			map.put("sjxxNum", sjxxNum+"");
			map.put("ybztNum", ybztNum+"");
		}else {
			Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
			map.put("ybztNum", ybztNum+"");
			JcsjDto jcsjDto=jcsjService.getDtoById(sjybztDto.getZt());//查询标本状态的扩展参数
			if(jcsjDto!=null) {
				if("1".equals(jcsjDto.getCskz1())) { //扩展参数1的为溶血的标本，统计基数为所有的nbbm以B开头的标本
					Integer sjxxNum=sjxxService.getCountAllSjxx(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
				}else {//其他的标本统计基数为所有的废弃的标本
					Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
				}
			}
		}
		return map;
	}

	/**
	 * 查询不同状态的标本的占比(接收日期)
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/count/getpercentageByJsrq")
	@ResponseBody
	public Map<String, String> getPercentageByJsrq(SjybztDto sjybztDto){
		Map<String,String> map= new HashMap<>();
		if(sjybztDto.getZt().equals("0")) {//状态为0的是没有选择标本状态的
			sjybztDto.setZtflg("1");
			sjybztDto.setZt("");
			Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
			Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
			map.put("sjxxNum", sjxxNum+"");
			map.put("ybztNum", ybztNum+"");
		}else {
			Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
			map.put("ybztNum", ybztNum+"");
			JcsjDto jcsjDto=jcsjService.getDtoById(sjybztDto.getZt());//查询标本状态的扩展参数
			if(jcsjDto!=null) {
				if("1".equals(jcsjDto.getCskz1())) { //扩展参数1的为溶血的标本，统计基数为所有的nbbm以B开头的标本
					Integer sjxxNum=sjxxService.getCountAllSjxxByJsrq(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
				}else {//其他的标本统计基数为所有的废弃的标本
					Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
				}
			}
		}
		return map;
	}

	/**
	 * 周报标本点击查看列表
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/statistics/week_PageList_Fjsq")
	public ModelAndView week_PageList_Fjsq(FjsqDto fjsqDto,String load_flg) {
		if(load_flg.equals("1")) {
			ModelAndView mav=new ModelAndView("wechat/statistics/local_recheckList");
			mav.addObject("fjsqDto", fjsqDto);
			return mav;
		}else{
			ModelAndView mav=new ModelAndView("wechat/statistics/recheckList");
			mav.addObject("fjsqDto", fjsqDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}
	}
	/**
	 * 周报标本点击查看列表(接收日期)
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/statistics/week_PageList_Fjsq_jsrq")
	public ModelAndView week_PageList_Fjsq_jsrq(FjsqDto fjsqDto,String load_flg) {
		if(load_flg.equals("1")) {
			ModelAndView mav=new ModelAndView("wechat/statistics/local_recheckList_jsrq");
			mav.addObject("fjsqDto", fjsqDto);
			return mav;
		}else{
			ModelAndView mav=new ModelAndView("wechat/statistics/recheckList");
			mav.addObject("fjsqDto", fjsqDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}
	}

	/**
	 * 周报标本点击查看列表
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/statistics/week_PageList_Fqyb")
	public ModelAndView week_PageList_Fqyb(SjybztDto sjybztDto,String load_flg) {
		if(load_flg.equals("1")) {
			ModelAndView mav=new ModelAndView("wechat/statistics/local_sampleState");
			if(sjybztDto.getZt().equals("0")) {
				sjybztDto.setZtflg("0");
			}else {
				sjybztDto.setZtflg("1");
			}
			mav.addObject("sjybztDto", sjybztDto);
			return mav;
		}else {
			ModelAndView mav=new ModelAndView("wechat/statistics/sampleState");
			if(sjybztDto.getZt().equals("0")) {
				sjybztDto.setZtflg("0");
			}else {
				sjybztDto.setZtflg("1");
			}
			mav.addObject("sjybztDto", sjybztDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}
	}
	/**
	 * 周报标本点击查看列表(接收日期)
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/statistics/week_PageList_Fqyb_jsrq")
	public ModelAndView week_PageList_Fqyb_jsrq(SjybztDto sjybztDto,String load_flg) {
		if(load_flg.equals("1")) {
			ModelAndView mav=new ModelAndView("wechat/statistics/local_sampleState_jsrq");
			if(sjybztDto.getZt().equals("0")) {
				sjybztDto.setZtflg("0");
			}else {
				sjybztDto.setZtflg("1");
			}
			mav.addObject("sjybztDto", sjybztDto);
			return mav;
		}else {
			ModelAndView mav=new ModelAndView("wechat/statistics/sampleState_jsrq");
			if(sjybztDto.getZt().equals("0")) {
				sjybztDto.setZtflg("0");
			}else {
				sjybztDto.setZtflg("1");
			}
			mav.addObject("sjybztDto", sjybztDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}
	}

	/**
	 * 送检报告阳性列表
	 * @param sjwzxxDto
	 * @return
	 */
	@RequestMapping("/statistics/positive_PageList")
	public ModelAndView positive_PageList(SjwzxxDto sjwzxxDto,String load_flg) {
		if(load_flg.equals("1")) {
			ModelAndView mav=new ModelAndView("wechat/statistics/local_rpositive");
			if(sjwzxxDto.getJyjg()==null) {
				if("阳性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("1");
				}else if("阴性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("0");
				}else if("疑似".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("2");
				}
			}
			List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
			mav.addObject("sjxxList",sjxxList);
			mav.addObject("sjwzxxDto", sjwzxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}else {
			ModelAndView mav=new ModelAndView("wechat/statistics/rpositive");
			if(sjwzxxDto.getJyjg()==null) {
				if("阳性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("1");
				}else if("阴性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("0");
				}else if("疑似".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("2");
				}
			}
			List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
			mav.addObject("sjxxList",sjxxList);
			mav.addObject("sjwzxxDto", sjwzxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}
	}

	/**
	 * 送检报告阳性列表(接收日期)
	 * @param sjwzxxDto
	 * @return
	 */
	@RequestMapping("/statistics/positive_PageList_jsrq")
	public ModelAndView positive_PageList_jsrq(SjwzxxDto sjwzxxDto,String load_flg) {
		if(load_flg.equals("1")) {
			ModelAndView mav=new ModelAndView("wechat/statistics/local_rpositive_jsrq");
			if(sjwzxxDto.getJyjg()==null) {
				if("阳性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("1");
				}else if("阴性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("0");
				}else if("疑似".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("2");
				}
			}
			List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
			mav.addObject("sjxxList",sjxxList);
			mav.addObject("sjwzxxDto", sjwzxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}else {
			ModelAndView mav=new ModelAndView("wechat/statistics/rpositive");
			if(sjwzxxDto.getJyjg()==null) {
				if("阳性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("1");
				}else if("阴性".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("0");
				}else if("疑似".equals(sjwzxxDto.getLxmc())) {
					sjwzxxDto.setJyjg("2");
				}
			}
			List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
			mav.addObject("sjxxList",sjxxList);
			mav.addObject("sjwzxxDto", sjwzxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}
	}

	/**
	 * 送检报告阳性返回列表
	 * @param sjwzxxDto
	 * @param load_flg
	 * @return
	 */
	@RequestMapping("/statistics/positive_PageList_back")
	public  ModelAndView positive_PageList_back(SjwzxxDto sjwzxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/local_rpositive");
		if(sjwzxxDto.getJyjg()==null) {
			if("阳性".equals(sjwzxxDto.getLxmc())) {
				sjwzxxDto.setJyjg("1");
			}else if("阴性".equals(sjwzxxDto.getLxmc())) {
				sjwzxxDto.setJyjg("0");
			}else if("疑似".equals(sjwzxxDto.getLxmc())) {
				sjwzxxDto.setJyjg("2");
			}
		}
		List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
		mav.addObject("sjxxList",sjxxList);
		mav.addObject("sjwzxxDto", sjwzxxDto);
		mav.addObject("load_flg", load_flg);
		mav.addObject("back_flg", "weeekly");
		return mav;
	}

	/**
	* 查看送检信息
	* @param sjxxDto
	* @return
	*/
	@RequestMapping(value="/inspection/viewSjxx")
	public ModelAndView viewSjxx(SjxxDto sjxxDto,String load_flg,String back_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sjxx_ListView");
		if(back_flg==null) {
			back_flg="daiky";
		}
		List<SjnyxDto> sjnyx = sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
		if ("1".equals(sjxxDto2.getYyxxCskz1())) {
			sjxxDto2.setHospitalname(sjxxDto2.getHospitalname() + "-" + sjxxDto2.getSjdwmc());
		}
		List<SjwzxxDto> sjwzxx = sjxxService.selectWzxxBySjid(sjxxDto2);
		if (sjwzxx != null && sjwzxx.size() > 0) {
			String xpxx = sjwzxx.get(0).getXpxx();// 由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}

//		if (("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
		if (("Z").equals(sjxxDto2.getCskz1()) || ("Z6").equals(sjxxDto2.getCskz1()) ||("F").equals(sjxxDto2.getCskz1())) {
			SjzmjgDto sjzmjgDto = new SjzmjgDto();
			sjzmjgDto.setSjid(sjxxDto.getSjid());
			List<SjzmjgDto> sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
			mav.addObject("sjzmList", sjzmList);
			mav.addObject("KZCS", sjxxDto2.getCskz1());
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());

		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
		List<FjcfbDto> zhwj = fjcfbService.selectzhpdf(fjcfbDto);
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		SjbgsmDto sjbgsmdto = new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx = sjbgsmservice.selectSjbgBySjid(sjbgsmdto);

		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECT_TYPE });
		List<JcsjDto> jcxmlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist = new ArrayList<>();//用于结果页
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页

		if (jcxmlist != null && jcxmlist.size() > 0) {
			for (int i = 0; i < jcxmlist.size(); i++) {

				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxx!=null && sjwzxx.size()>0) {
					for(int j=0;j<sjwzxx.size();j++) {
						if(sjwzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj)
					c_jcxmlist.add(jcxmlist.get(i));

				boolean sftj = false;// 判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				if (sjbgsmxx != null && sjbgsmxx.size() > 0) {
					for (int j = 0; j < sjbgsmxx.size(); j++) {
						if (sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							sftj = true;
							break;
						}
					}
				}
				if (t_fjcfbDtos != null && t_fjcfbDtos.size() > 0) {
					for (int j = 0; j < t_fjcfbDtos.size(); j++) {
						if (t_fjcfbDtos.get(j).getYwlx()
								.equals((jcxmlist.get(i).getCskz3() + jcxmlist.get(i).getCskz1()))) {
							sftj = true;
							break;
						}
					}
				}
				if (fjcfbDtos != null && fjcfbDtos.size() > 0) {
					for (int j = 0; j < fjcfbDtos.size(); j++) {
						if (fjcfbDtos.get(j).getYwlx()
								.equals((jcxmlist.get(i).getCskz3() + "_" + jcxmlist.get(i).getCskz1()))) {
							sftj = true;
							break;
						}
					}
				}
				if (sftj)
					t_jcxmlist.add(jcxmlist.get(i));
			}
		}
		// 查看当前复检申请信息
		FjsqDto fjsqDto = new FjsqDto();
		String[] zts = { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_PASS.getCode() };
		fjsqDto.setZts(zts);
		fjsqDto.setSjid(sjxxDto2.getSjid());
		List<FjsqDto> fjsqList = fjsqService.getListBySjid(fjsqDto);
		mav.addObject("fjsqList", fjsqList);
		mav.addObject("SjnyxDto", sjnyx);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);
		mav.addObject("Sjwzxx", sjwzxx);
        mav.addObject("jcxmlist",t_jcxmlist);
        mav.addObject("load_flg", load_flg);
        mav.addObject("back_flg", back_flg);
        mav.addObject("wzjcxmlist", c_jcxmlist);

		if(bioaudurl.endsWith("/"))
			bioaudurl = bioaudurl.substring(0, bioaudurl.length() - 1);
		mav.addObject("bioaudurl",bioaudurl);

		return mav;
	}

	/**
	 * 文件下载（用于普通的文件下载使用）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/downloadFile")
	@ResponseBody
	public String downloadFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		if (StringUtil.isNotBlank(request.getParameter("isRedirect"))){
			//从85端下载请求过来的
			fjcfbDto = JSON.parseObject(request.getParameter("fjcfbDto"), FjcfbDto.class);
		}
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null) {
			log.error("对不起，系统未找到相应文件!");
			return null;
		}
		String wjlj = t_fjcfbDto.getWjlj();
		String wjm = t_fjcfbDto.getWjm();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		File downloadFile = new File(filePath);
		response.setContentLength((int) downloadFile.length());
		String agent = request.getHeader("user-agent");
		log.error("文件下载 agent=" + agent);
		//指明为下载
		response.setHeader("content-type", "application/octet-stream");
		try {
			/*if(wjm != null){
				wjm = URLEncoder.encode(wjm, "utf-8");
			}*/
			if(wjm != null){
				if (agent.contains("iPhone") || agent.contains("Trident")){
					//iphone手机 微信内置浏览器 下载
					if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
//						byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
//						wjm = new String(bytes, "ISO-8859-1");
						wjm = URLEncoder.encode(wjm, "utf-8");
						response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
					}else {
						//iphone手机 非微信内置浏览器 下载 或 ie浏览器 下载
						wjm = URLEncoder.encode(wjm,"UTF-8");
						response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
					}
				}else {
					wjm = URLEncoder.encode(wjm, "utf-8");
					response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
				}
			}
			log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(wjm,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			log.error("文件下载 异常：" + e1.toString());
			return null;
		}

		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream = null;
		OutputStream os = null; //输出流
		try {
			iStream = new FileInputStream(filePath);
			os = response.getOutputStream();
			bis = new BufferedInputStream(iStream);
			int i = bis.read(buffer);
			while(i != -1){
				os.write(buffer);
				os.flush();
				i = bis.read(buffer);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("文件下载 写文件异常：" + e.toString());
			return null;
		}
		try {
			bis.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("文件下载 关闭文件流异常：" + e.toString());
			return null;
		}
		return null;
	}


	/**
	 * pdf预览调用方法
	 * @param fjcfbDto
	 * @return
	 */
	/*@RequestMapping(value="/file/pdfPreview")
	public ModelAndView pdfPreview(FjcfbDto fjcfbDto) {
		String filepath = "/ws/file/getFileInfo?fjid=" + fjcfbDto.getFjid();
		ModelAndView mv = new ModelAndView("wechat/statistics/viewer");
		mv.addObject("file",filepath);
		return mv;
	}*/

	/**
	 * 送检列表查看界面跳转参考指标解释
	 * @return
	 */
	@RequestMapping(value = "/sjxx/getckzbjs")
	public ModelAndView getCkzbjs() {
        return new ModelAndView("wechat/sjxx/sjxx_ckzbjs");
	}

	/**
	 * 钉钉标本量列表页面
	 * @return
	 */
	@RequestMapping(value ="/inspection/sampleCapacityListPage")
	public ModelAndView getgetsampleCapacityPage(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_sampleCapacityList");
		mav.addObject("SjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 钉钉标本量列表
	 * @return
	 */
	@RequestMapping(value="/inspection/sampleCapacityList")
	@ResponseBody
	public Map<String,Object> getsampleCapacityList(SjxxDto sjxxDto){
		List<SjxxDto> lists = sjxxService.selectInspSize(sjxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", sjxxDto.getTotalNumber());
		map.put("rows", lists);
		return map;
	}

	/**
	 * 自定义统计（列表）
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getTableCustomStatis")
	@ResponseBody
	public Map<String,Object> getTableCustomStatis(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		List<Map<String,String>> list=sjxxService.getCustomStatisList(sjxxDto);
		map.put("rows", list);
 		return map;
	}

	/**
	 * 自定义统计（图形:条状）
	 * @return
	 */
	@RequestMapping("/statistics/getGraphCustomStatis")
	@ResponseBody
	public Map<String,Object> getGraphCustomStatis(SjxxDto sjxxDto) {
		Map<String, Object> map = new HashMap<>();
		//如果为统计字段
		List<Map<String, String>> list=sjxxService.customStatis(sjxxDto);
		map.put("rows", list);
 		return map;
	}

	@RequestMapping("/dingtalk/getdingtalk")
	public ModelAndView getdingtalk(String code) {
		try{
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
			OapiGettokenRequest request = new OapiGettokenRequest();
			request.setAppkey("dingi7lnfzfvwscusdq9");
			request.setAppsecret("h_SeR3WnDlGxsEAtYrzBUY-OcTLKGPW-McqYJ8deQr-JOtJ6yfNONupY94-ugujK");
			request.setHttpMethod("GET");
			OapiGettokenResponse response = client.execute(request);

			DingTalkClient client2 = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
			OapiUserGetuserinfoRequest request2 = new OapiUserGetuserinfoRequest();
			request2.setCode(code);
			request2.setHttpMethod("GET");
			OapiUserGetuserinfoResponse response2 = client2.execute(request2, (response.getAccessToken()));
			String userId = response2.getUserid();
			log.error(userId);
			} catch (ApiException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return null;
	}

	/**
	 * 查看页面
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/viewRecheck")
	public ModelAndView viewRecheck(FjsqDto fjsqDto, HttpServletRequest request) {
		boolean checkSign = commonService.checkSign(fjsqDto.getSign(), fjsqDto.getFjid(), request);
		if(!checkSign){
            return commonService.jumpError();
		}
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_view_out");
		FjsqDto fjsqDtos=fjsqService.getDto(fjsqDto);
		fjsqDtos.setAuditType(fjsqDtos.getShlx());
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDtos.getSjid());
		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
		if(sjxxDtos!=null) {
			if("1".equals(sjxxDtos.getYyxxCskz1())) {
				sjxxDtos.setHospitalname(sjxxDtos.getHospitalname()+"--"+sjxxDtos.getSjdwmc());
			}
			mav.addObject("sjxxDto", sjxxDtos);
		}else {
			mav.addObject("sjxxDto", sjxxDto);
		}
		mav.addObject("fjsqDto", fjsqDtos);
		return mav;
	}

	/**
	 * 接收临时word文件转换成pdf
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/temp/receiveWord")
	@ResponseBody
	public Map<String, Object> receiveWord(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file!=null&& imp_file.length>0){
				for(int i=0;i<imp_file.length;i++){
					if(!imp_file[i].isEmpty()){
						MultipartFile file = imp_file[i];
						//文件处理
						String wjm = fjcfbService.receiveWord(file);
						if(StringUtil.isNotBlank(wjm)){
							String sign = URLEncoder.encode(commonService.getSign(wjm),"UTF-8");
							//内网访问
							String internal = applicationurl + "/common/view/pdfPreview?wjm=" + wjm + "&sign=" + sign;
							//外网访问
							String external = externalurl + "/common/view/pdfPreview?wjm=" + wjm + "&sign=" + sign;
							map.put("status", "success");
							map.put("errorCode", 0);
							map.put("internal", internal);
							map.put("external", external);
						}else{
							map.put("status", "fail");
							map.put("errorCode", "保存失败！");
							log.error("保存失败！");
						}
					}
				}
			}
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 通过内部编码获取标本编号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getYbbhByNbbm")
	@ResponseBody
	public Map<String,String> getYbbhByNbbm(HttpServletRequest request) {
		Map<String,String> map= new HashMap<>();
		List<Map<String,String>> list = new ArrayList<>();
		String nbbm=request.getParameter("nbbm");
		log.error(" 191_1 getYbbhByNbbm print:" + nbbm);
		if(StringUtil.isNotBlank(nbbm)) {
			String[] infos = nbbm.split("_");
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setNbbm(infos[0]);
			SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
			//如果未找到相应的送检信息，则标本编号不打印，内部编码采用传递过来的参数
			if(sjxxDto_t == null){
				Map<String,String> hashMap= new HashMap<>();
				hashMap.put("word", null);
				hashMap.put("flag",  "2");
				hashMap.put("code", nbbm);
				hashMap.put("qrcode", nbbm);
				hashMap.put("project",null);
				list.add(hashMap);
				map.put("list",JSONObject.toJSONString(list));
				return map;
			}
			//如果只有内部编码，则获取标本编码后直接打印条码
			if (infos.length < 2){
				Map<String,String> hashMap= new HashMap<>();
				hashMap.put("word",  sjxxDto_t.getYbbh());
				hashMap.put("flag",  "2");
				hashMap.put("code", infos[0].startsWith("MD")?infos[0].substring(2):infos[0]);
				hashMap.put("qrcode", infos[0]);
				hashMap.put("project",null);
				list.add(hashMap);
				map.put("list",JSONObject.toJSONString(list));
				log.error(" 191_1 getYbbhByNbbm print 1:" + nbbm);
				return map;
			}
			//如果第二参数为1，则为打印提取总码，或者指定编码
			if (infos[1].equals("1")){
				String project = "";
				//如果参数超过2个，第三参数保存的是提取信息（包括提取代码和后缀）按照|进行切分，按照指定的提取进行打印
				if (infos.length >2){
					String[] strings = infos[2].split("\\|");
					for (String string : strings) {
						project+=" "+string;
					}
				}else{
					//如果不包含提取信息，则从数据库获取提取信息，进行打印
					SjsyglDto sjsyglDto = new SjsyglDto();
					sjsyglDto.setSjid(sjxxDto_t.getSjid());
					List<SjsyglDto> infoList = sjsyglService.getTqInfoList(sjsyglDto);
					for (SjsyglDto dto : infoList) {
						project+=" "+dto.getNbzbm();
					}
				}
				Map<String,String> hashMap= new HashMap<>();
				hashMap.put("word",  StringUtil.isNotBlank(project)?project.substring(1):"");
				hashMap.put("code", infos[0].startsWith("MD")?infos[0].substring(2):infos[0]);
				hashMap.put("qrcode", infos[0]);
				hashMap.put("flag",  "2");
				hashMap.put("project",sjxxDto_t.getYbbh());
				list.add(hashMap);
				map.put("list",JSONObject.toJSONString(list));
				log.error(" 191_1 print:" + JSONObject.toJSONString(list));
			}else{
				//如果第二参数为 2或者其他，则为取样，需要加日期，打印的是文库信息
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
				Date date=new Date();
				String now=simpleDateFormat.format(date);//当前日期
				String project;
				String qrcode;
				SjsyglDto sjsyglDto = new SjsyglDto();
				sjsyglDto.setSjid(sjxxDto_t.getSjid());
				List<String> suffixs = new ArrayList<>();
				if (infos.length >2) {
					String[] strings = infos[2].split("\\|");
                    Collections.addAll(suffixs, strings);
					sjsyglDto.setSuffixs(suffixs);

				}
				//根据不同的提取打印多条记录
				List<SjsyglDto> infoList = sjsyglService.getTqInfoList(sjsyglDto);
				Map<String, List<SjsyglDto>> listMap = infoList.stream().collect(Collectors.groupingBy(SjsyglDto::getKzcs2));
				if (null != listMap && listMap.size()>0){
					Map<String,String> hashMapAll= new HashMap<>();
					String projectAll = "";
					String qrcodeAll = infos[0];
					Iterator<Map.Entry<String, List<SjsyglDto>>> entries = listMap.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String,  List<SjsyglDto>> entry = entries.next();
						List<SjsyglDto> resultModelList = entry.getValue();
						if (null != resultModelList && resultModelList.size() > 0){
							qrcodeAll +=" "+ entry.getKey() + "/"+resultModelList.get(0).getKzcs3();
							projectAll +=" "+ entry.getKey();
							qrcode = infos[0]+ " "+ entry.getKey() + "/"+resultModelList.get(0).getKzcs3();
							project = entry.getKey();
//							for (SjsyglDto resultModel:resultModelList) {
//								project += " "+resultModel.getNbzbm();
//							}
//							if (StringUtil.isNotBlank(project)){
//								project = project.substring(1);
//							}
							Map<String,String> hashMap= new HashMap<>();

							hashMap.put("word", infos[0].startsWith("MD")?infos[0].substring(2):infos[0]);
							hashMap.put("flag",  "2");
							hashMap.put("code", project+" "+now);
							hashMap.put("qrcode", qrcode);
							hashMap.put("project", sjxxDto_t.getYbbh() );
							list.add(hashMap);
						}
					}
					hashMapAll.put("word", infos[0].startsWith("MD")?infos[0].substring(2):infos[0]);
					hashMapAll.put("flag",  "2");
					hashMapAll.put("code", StringUtil.isNotBlank(projectAll)?projectAll.substring(1)+" "+now:"");
					hashMapAll.put("qrcode", qrcodeAll);
					hashMapAll.put("project", sjxxDto_t.getYbbh() );
					list.add(hashMapAll);
				}
				map.put("list",JSONObject.toJSONString(list));

				log.error(" 191_1 getYbbhByNbbm print 3:" + nbbm);
			}
		}
		return map;
	}

	/**
	 * 获取展示页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/displayCurrent")
	public ModelAndView displayCurrent(HttpServletRequest request) {
		ModelAndView mav= new ModelAndView("wechat/sjxx/currentview");

		try {
			LsbModel t_lsbModel = lsbService.getModelById("DXY-TJ");
			if(t_lsbModel != null)
				mav.addObject("body_str", t_lsbModel.getNr());
			else {
				mav.addObject("body_str", "未获取到正确内容");
			}

			//int confirmedNumber_start = mapTop_str.indexOf("confirmedNumber___3WrF5");
			//int confirmedNumber_end = mapTop_str.indexOf("</p>",confirmedNumber_start);

			//String confirmedNumber_str = mapTop_str.substring(confirmedNumber_start + 25, confirmedNumber_end);

			//mav.addObject("confirmedNumber_str", confirmedNumber_str);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value="/getCurrentData")
	@ResponseBody
	public String getCurrentData(HttpServletRequest request) {
        return "";
	}

	@RequestMapping("/test")
	@ResponseBody
	public void  test(HttpServletRequest request) {
//		SslUtils factory = new SslUtils();
//		factory.setReadTimeout(60000);
//		factory.setConnectTimeout(30000);
//		RestTemplate restTemplate = new RestTemplate(factory);
//		String url = "https://adicontest.adicon.com.cn:5000/api/oauth/gettoken";
//		String apiUserCode = "userName=testjysamR1";
//		String apiPassword = "userPwd=testabc11";
//		url = url + "?" + apiUserCode + "&" + apiPassword;
//		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
//		String map = restTemplate.postForObject(url, null, String.class);
//		System.out.println(map);
		String pathString = request.getParameter("path");
		sjxxWsService.restoreDeal(pathString);
	}

	/**
	 * 清单发送给商务部
	 * @param
	 * @return
	 */
	@RequestMapping("/inspection/getDdUncollectedView")
	public ModelAndView getDdUncollectedView(String csid) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ddUncollectedView");
		List<SjxxDto> list = sjxxService.getDtoBySf(csid);
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * 清单发送给医学代表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/yxdbDdUncollectedView")
	public ModelAndView yxdbDdUncollectedView(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ddUncollectedView");
		String[] split = sjxxDto.getDdid().split("-");
		sjxxDto.setDdid(split[0]);
		sjxxDto.setCsid(split[1]);
		List<SjxxDto> list = sjxxService.getListByYxdb(sjxxDto);
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * 定时任务报告发送情况清单列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/bgxxDdUncollectedView")
	public ModelAndView bgxxDdUncollectedView(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/bgxx_ddUncollectedView");
		DBEncrypt p = new DBEncrypt();
		String xjsj=p.dCode(sjxxDto.getXjsj().replace(" ","+"));
		List<Map<String,String>> mapList = sjxxService.getFsbgs(xjsj);
		List<String>timeList=sjxxService.getTimeByTime(xjsj);
		mav.addObject("list",mapList);
		mav.addObject("timeList",timeList);
		return mav;
	}
	/**
	 * 清单发送给大区经理
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/dqjlDdUncollectedView")
	public ModelAndView dqjlDdUncollectedView(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ddUncollectedView");
		String[] split = sjxxDto.getDdid().split("-");
		sjxxDto.setDdid(split[0]);
		sjxxDto.setCsid(split[1]);
		List<SjxxDto> list = sjxxService.getListByDqjl(sjxxDto);
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * 钉钉反馈消息查看页面
	 * @param req
	 * @return
	 */
	@RequestMapping("/inspection/getDdFeedbackView")
	public ModelAndView getDdFeedbackViewPage(HttpServletRequest req) {
		String sjid=req.getParameter("sjid");
		SjxxDto sjxx=new SjxxDto();
		sjxx.setSjid(sjid);
		SjxxDto sjxxDto=sjxxService.getDto(sjxx);
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ddFeedbackView");
		List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2=sjxxService.getDto(sjxxDto);
		List<SjwzxxDto> sjwzxx=sjxxService.selectWzxxBySjid(sjxxDto2);
		if(sjwzxx!=null && sjwzxx.size()>0) {
			String xpxx=sjwzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}
		SjbgsmDto sjbgsmdto=new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx=sjbgsmservice.selectSjbgBySjid(sjbgsmdto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
		List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页
		if(jcxmlist!=null && jcxmlist.size()>0) {
			for(int i=0;i<jcxmlist.size();i++) {
				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxx!=null && sjwzxx.size()>0) {
					for(int j=0;j<sjwzxx.size();j++) {
						if(sjwzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj)
					c_jcxmlist.add(jcxmlist.get(i));
			}
		}
        mav.addObject("Sjwzxx", sjwzxx);
        mav.addObject("SjnyxDto", sjnyx);
        mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("SjxxDto", sjxxDto);
		mav.addObject("wzjcxmlist", c_jcxmlist);
		return mav;
	}

	/**
	 * 查看送检验证信息
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/viewVerifi")
	public ModelAndView viewVerifi(SjyzDto sjyzDto) {
		ModelAndView mav;
		try {
			//查询送检验证信息
			SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
			mav=new ModelAndView("wechat/verification/verification_ddview");
			//查询送检信息
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(sjyzDto_t.getSjid());
			SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
			if("1".equals(sjxxDto_t.getYyxxCskz1())) {
				sjxxDto_t.setHospitalname(sjxxDto_t.getHospitalname()+"--"+sjxxDto_t.getSjdwmc());
			}
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjyzDto.getYzid());
			SjyzjgDto sjyzjgDto=new SjyzjgDto();
			sjyzjgDto.setYzid(sjyzDto.getYzid());
			List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
			mav.addObject("sjxxDto", sjxxDto_t);
			mav.addObject("sjyzDto", sjyzDto_t);
			mav.addObject("fjcfbDtos", fjcfbDtos);
			mav.addObject("yzmxlist", yzmxlist);
		} catch (Exception e) {
			return commonService.jumpError();
		}
		return mav;
	}

	/**
	 * 验证审核页面(手机端钉钉审核,实验员审核,可上传附件)
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/verification/modAuditVerify")
	public ModelAndView modAuditVerify(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_modSubmit");
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		sjyzDto_t.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		sjyzDto_t.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		List<JcsjDto> distinguishList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DISTINGUISH.getCode());
		if (StringUtil.isNotBlank(sjxxDto_t.getNbbm())){
			String lastCode = sjxxDto_t.getNbbm();
			lastCode = lastCode.substring(lastCode.length()-1);
			if ("B".equals(lastCode) || "b".equals(lastCode)){
				List<JcsjDto> lsdistinguishList = new ArrayList<>();
				for (JcsjDto distinguish : distinguishList) {
					if (!"REM".equals(distinguish.getCskz1())){
						lsdistinguishList.add(distinguish);
					}
				}
				distinguishList = lsdistinguishList;
			}
		}
		mav.addObject("distinguishList", distinguishList);//区分
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
//		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjyzDto.getYzid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getFjcfbDtoByYwid(sjyzDto.getYzid());
		List<FjcfbDto> fjcfbDtos1 = new ArrayList<>();//附件删除标记为0
		for (FjcfbDto fj : fjcfbDtos){
			if ("0".equals(fj.getScbj())){
				fjcfbDtos1.add(fj);
			}
		}
		mav.addObject("verificationList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("clientnoticeList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjxxDto", sjxxDto_t);
		mav.addObject("sjyzDto", sjyzDto_t);
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		//根据验证id查询附件表信息
//		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("fjcfbDtos", fjcfbDtos1);
		mav.addObject("yzmxlist", yzmxlist);
		mav.addObject("btnFlg","audit");
		mav.addObject("url", "/ws/verification/updateJcdwVerification");
		return mav;
	}

	/**
	 * 送检验证审核（手机端钉钉审核）
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/verification/audit_Verifi")
	public ModelAndView audit_Verifi(SjyzDto sjyzDto) {
		ModelAndView mav=new ModelAndView("wechat/verification/verification_add");
		SjyzDto sjyzDto_t=sjyzService.getDto(sjyzDto);
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto_t.getSjid());
		SjxxDto sjxxDto_t=sjxxService.getDto(sjxxDto);
		sjxxDto_t.setJcdw(sjyzDto_t.getJcdw());
		SjyzjgDto sjyzjgDto=new SjyzjgDto();
		sjyzjgDto.setYzid(sjyzDto.getYzid());
		List<SjyzjgDto> yzmxlist=sjyzjgService.getDtoList(sjyzjgDto);
		List<SjyzDto> sjyzList = new ArrayList<>();
		if(sjxxDto.getSjid()!=null) {
			sjyzList = sjyzService.getDtoListBySjid(sjxxDto.getSjid());
		}
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListByType(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_TYPE,BasicDataTypeEnum.VERIFICATION_RESULT,BasicDataTypeEnum.DISTINGUISH,BasicDataTypeEnum.CLIENT_NOTICE,BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("verificationList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_TYPE.getCode()));//送检验证类型
		mav.addObject("verificationresultList", jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()));//送检验证结果
		mav.addObject("distinguishList", jcsjlist.get(BasicDataTypeEnum.DISTINGUISH.getCode()));//区分
		mav.addObject("clientnoticeList", jcsjlist.get(BasicDataTypeEnum.CLIENT_NOTICE.getCode()));//客户通知
		mav.addObject("detectionList", jcsjlist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjxsbj", "0");
		mav.addObject("ywxsbj", "0");
		mav.addObject("sjxxDto", sjxxDto_t);
		sjyzDto_t.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION.getCode());
		mav.addObject("sjyzDto", sjyzDto_t);
		mav.addObject("formAction", "/verification/verification/modSaveAuditVerify");
		mav.addObject("yzmxlist", yzmxlist);
		mav.addObject("xsbj", "1");
        mav.addObject("btnFlg","audit");
		mav.addObject("url", "/ws/verification/updateJcdwVerification");
		mav.addObject("sjyzList",sjyzList);
		return mav;
	}

	/**
	 * 标本量审核页面(手机端钉钉审核)
	 * @param cwglDto
	 * @return
	 */
	@RequestMapping("/samplenum/AuditSampleNum")
	public ModelAndView getSumpleNumAuditPage(CwglDto cwglDto) {
		ModelAndView mav =new ModelAndView("wechat/samplenum/samplenum_listviewaudit");
		CwglDto cwglxx=cwglService.getDtoById(cwglDto.getCwid());
		mav.addObject("CwglDto", cwglxx);
		return mav;
	}

	/**
	 * 接收送检结果信息生成报告(接口1,采用默认用户)
	 * 姚嘉伟  add  2020/09/01
	 * @param request
	 */
	@RequestMapping(value = "/pathogen/receiveInspectionGenerateReport")
	@ResponseBody
	public Map<String, Object> receiveInspectionGenerateReport(HttpServletRequest request){
		//若没有传递用户信息，自动添加一个默认的，用于提交加测申请.
		String yhm ="SXJQR";
        return receiveInspectionGenerateReport2(request,yhm);
	}

	/**
	 * 接收送检结果信息生成报告(接口2,传递用户名)
	 * 检测项目修改影响内容
	 * 1.若新增了检测项目，需要维护阈值信息
	 * 2.新增检测项目要同步增加85端
	 * @param request
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "/pathogen/receiveInspectionGenerateReportSec")
	@ResponseBody
	public Map<String, Object> receiveInspectionGenerateReport2(HttpServletRequest request,String userid){
		//request.getb
		Map<String,Object> map = new HashMap<>();
		JkdymxDto jkdymxDto=new JkdymxDto();
		String sample_result = request.getParameter("sample_result");
		log.error("sample_result :" + sample_result);
		try {
			if(StringUtil.isNotBlank(sample_result)) {
				SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//保存调用信息
				WeChatInspectionReportModel inspinfo = JSONObject.parseObject(sample_result, WeChatInspectionReportModel.class);
				jkdymxDto.setLxqf("recv");
				jkdymxDto.setDysj(dateFm.format(new Date()));
				jkdymxDto.setYwid(inspinfo.getYbbh());
				jkdymxDto.setNr(sample_result);
				jkdymxDto.setDydz(request.getRequestURI());
				jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
				jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_REPORT.getCode());
				jkdymxDto.setSfcg("2");
				Map<String,Object> result = sjxxWsService.receiveInspectionGenerateReport(request,sample_result,userid,jkdymxDto);
				log.error("receiveInspectionGenerateReport -- result:"+result.get("status") + " info:"+result.get("errorCode"));
				if("success".equals(result.get("status"))){
					jkdymxDto.setSfcg("1");
					log.error("success!");
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					jkdymxDto.setSfcg("0");
					map.put("status", "fail");
					map.put("errorCode", result.get("errorCode"));
					log.error("保存失败！");
				}
				jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据
			}else{
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}
		}  catch (Exception e) {
			jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据
			map.put("status", "fail");
			map.put("errorCode", e.toString());
			log.error(e.toString());
		}
		//log.error("return map!");
		return map;
	}

	/**
	 * 获取物种统计图
	 * @param file
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/pathogen/SpeciesStatistics")
	@ResponseBody
	public Map<String, Object> SpeciesStatistics(MultipartFile file,HttpServletRequest request, HttpSession session) {
		Map<String,Object> map = new HashMap<>();
		String projectType = "";
		if (StringUtil.isNotBlank(request.getParameter("project_type"))) {
			projectType = "/" + request.getParameter("project_type");
		}
		String dir = "IMP_TEMEPLATE_IMAGES"+projectType+"/D";
		boolean isSuccess = sjxxWsService.upload(file, dir,projectType);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}



	@RequestMapping("/exception/replyException")
	@ResponseBody
	public ModelAndView getreplyException(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		String ycid = request.getParameter("ycid");
		String yhid = request.getParameter("yhid");
		String view_url = "/ws/exception/replyExceptionPage?ycid="+ycid+"&yhid="+yhid;
		mav.addObject("view_url", view_url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取手机端异常反馈界面
	 * @param sjycDto
	 * @return
	 */
	@RequestMapping("/exception/replyExceptionPage")
	@ResponseBody
	public ModelAndView getreplyExceptionPage(SjycDto sjycDto) {
		ModelAndView mav=new ModelAndView("wechat/exception/exception_ddreply");
		SjycDto sjyc;
		sjyc=sjycService.getDto(sjycDto);
		sjyc.setYhid(sjycDto.getYhid());
		SjycfkDto sjycfkDto=new SjycfkDto();
		List<String> ids= new ArrayList<>();
		ids.add(sjyc.getYcid());
		sjycfkDto.setIds(ids);
		sjycfkDto.setYcid(sjyc.getYcid());
		List<SjycfkDto> pls=sjycfkService.getExceptionPls(sjycfkDto);
		sjyc.setPls(String.valueOf(pls.size()));
		List<SjycfkDto> SjycfkDtos=sjycfkService.getDtoByYcid(sjycfkDto);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		String hfsjc;
		try {
			for(int i=0;i<SjycfkDtos.size();i++) {
				Date date=format.parse(SjycfkDtos.get(i).getHfsj());
				long l = new Date().getTime() - date.getTime();
			    long hour=l/(60*60*1000);
			    if(hour>0) {
			    	hfsjc=hour+"小时前";
			    	if(hour>24) {
			    		long day=l/(24*60*60*1000);
			    		long hour_t=(l/(24*60*60*1000)-24*day);
			    		hfsjc=day+"天"+hour_t+"小时前";
			    	}
			    }else {
			    	long min=l/(60*1000);
			    	if(min>0) {
			    		hfsjc=min+"分钟前";
			    	}else {
			    		hfsjc="刚刚";
			    	}
			    }
			    SjycfkDtos.get(i).setHfsjc(hfsjc);
				if("WECHAT_FEEDBACK".equals(SjycfkDtos.get(i).getFkqfdm())){
					SjycfkDtos.get(i).setFkrymc(SjycfkDtos.get(i).getLrrymc());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		mav.addObject("sjycDto", sjyc);
		mav.addObject("sjycfklist", SjycfkDtos);
		return mav;
	}

	/**
	 * 生信部获取复测加测信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/inspection/getRecheckMessage")
	@ResponseBody
	public Map<String,Object> getRecheckMessage(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		String wkbh=request.getParameter("wkbh");
		log.error("生信部获取复测加测信息getRecheckMessage:" + wkbh);
		if(StringUtils.isBlank(wkbh)) {
			map.put("status","OK");
			map.put("message","请传递文库编号!");
		}else {
			try {
				if(wkbh.contains("-NC") || wkbh.contains("-PC")) {
					map.put("status","OK");
					log.error("NC和PC不做处理!");
					map.put("message","");
					return map;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				List<String> list=Arrays.asList(wkbh.split("-"));
				int length=list.size();
				
				if(list.size() <3)
				{
					map.put("status","OK");
					map.put("message","文库编号格式不正确!" + wkbh);
					return map;
				}
				//获取日期，
				String rqend=list.get(length-1);//日期取最后一位
				//判断是否为日期
				int t_index = length-1;
				while(rqend.length()!=8){
					t_index--;
					if(t_index<=0)
						break;
					rqend=list.get(t_index);
				}
				String jcxm=list.get(2);//默认取第三位

				StringBuilder sb = new StringBuilder(rqend);
				sb.insert(4, "-");
				sb.insert(7, "-");
				rqend=sb.toString();
				String nbbh;
				FjsqDto fjsqDto=new FjsqDto();
				//如果为去人源

				if(list.contains("REM")) {
					if(list.get(0).contains("MDY")&&list.get(1).length()<10) {
						nbbh=list.get(0)+"-"+list.get(1);
						nbbh=nbbh.substring(2);
					}else {
						nbbh=list.get(1);
					}
					String[] lxs = new String[6];
					String[] csdms=new String[] {"REM"};
					JcsjDto jcsjDto_t=new JcsjDto();
					jcsjDto_t.setCsdms(csdms);
					List<JcsjDto> jcsjList=jcsjService.getJcsjDtoList(jcsjDto_t);
					if(jcsjList!=null && jcsjList.size()>0) {
						for (int j = 0; j < jcsjList.size(); j++) {
							lxs[j]=jcsjList.get(j).getCsid();
						}

					}
					fjsqDto.setLxs(lxs);
				}else {
					if(list.get(0).contains("MDY")&&list.get(1).length()<10) {
						nbbh=list.get(0)+"-"+list.get(1);
						nbbh=nbbh.substring(2);
					}else {
						nbbh=list.get(1);
					}
					String[] lxs = new String[6];
					String[] csdms=new String[] {"RECHECK","ADDDETECT","REM","LAB_RECHECK","LAB_REM","LAB_ADDDETECT"};
					JcsjDto jcsjDto_t=new JcsjDto();
					jcsjDto_t.setCsdms(csdms);
					jcsjDto_t.setJclb(BasicDataTypeEnum.RECHECK.getCode());
					List<JcsjDto> jcsjList=jcsjService.getJcsjDtoList(jcsjDto_t);
					if(jcsjList!=null && jcsjList.size()>0) {
						for (int j = 0; j < jcsjList.size(); j++) {
							lxs[j]=jcsjList.get(j).getCsid();
						}
					}
					fjsqDto.setLxs(lxs);
				}
				Date date=sdf.parse(rqend);
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(Calendar.DATE,-3);
				Date qdate=calendar.getTime();
				String rqstart=sdf.format(qdate);
				fjsqDto.setNbbm(nbbh);
				fjsqDto.setLrsjstart(rqstart);
				fjsqDto.setYblxdm(nbbh.substring(nbbh.length()-1));
				fjsqDto.setJcxmdm(jcxm);
				//fjsqDto.setLrsjend(rqend);
				//fjsqDto.setJcxms(jcxms);
				String[] zts = { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_PASS.getCode() };
				fjsqDto.setZts(zts);
				List<FjsqDto> fjsqDtos=fjsqService.getDtoListOrderLrsj(fjsqDto);
				if(fjsqDtos==null || fjsqDtos.size()<=0) {
					map.put("status", "OK");
					map.put("message", "");
				}else {
					map.put("status", "OK");
					map.put("message", fjsqDtos.get(0));
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 区域统计，跳转区域列表(日报)
	 * @return
	 */
	@RequestMapping("/statistics/getZoneList")
	public ModelAndView getZoneList(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_zone");
		String load_flg=request.getParameter("load_flg");
		String jsrq =sjxxDto.getJsrq();
		UserDto userDto = new UserDto();
		userDto.setJsmc("大区经理");
		List<UserDto> users = sjxxService.queryYhByJsmc(userDto);
		for (UserDto userDto_t : users) {
			try {
				String newSign = URLEncoder.encode(commonService.getSign(userDto_t.getYhid() + jsrq), "utf-8");
				userDto_t.setNewSign(newSign);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		mav.addObject("userDtos", users);
		return mav;
	}
	/**
	 * 区域统计，跳转区域列表(日报)(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getZoneListByJsrq")
	public ModelAndView getZoneListByJsrq(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_zone_jsrq");
		String load_flg=request.getParameter("load_flg");
		String jsrq =sjxxDto.getJsrq();
		UserDto userDto = new UserDto();
		userDto.setJsmc("大区经理");
		List<UserDto> users = sjxxService.queryYhByJsmc(userDto);
		for (UserDto userDto_t : users) {
			try {
				String newSign = URLEncoder.encode(commonService.getSign(userDto_t.getYhid() + jsrq), "utf-8");
				userDto_t.setNewSign(newSign);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		mav.addObject("userDtos", users);
		return mav;
	}

	/**
	 * 区域统计，跳转区域列表(周报)
	 * @return
	 */
	@RequestMapping("/statistics/getZoneWeekList")
	public ModelAndView getZoneWeekList(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_weekly_zone");
		String jsrqstart = sjxxDto.getJsrqstart();
		String jsrqend = sjxxDto.getJsrqend();
		UserDto userDto = new UserDto();
		userDto.setJsmc("大区经理");
		List<UserDto> users = sjxxService.queryYhByJsmc(userDto);
		for (UserDto userDto_t : users) {
			try {
				String newSign = URLEncoder.encode(commonService.getSign(userDto_t.getYhid() + jsrqstart + jsrqend), "utf-8");
				userDto_t.setNewSign(newSign);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("userDtos", users);
		return mav;
	}
	/**
	 * 区域统计，跳转区域列表(周报)(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getZoneWeekListByJsrq")
	public ModelAndView getZoneWeekListByJsrq(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_weekly_zone_jsrq");
		String jsrqstart = sjxxDto.getJsrqstart();
		String jsrqend = sjxxDto.getJsrqend();
		UserDto userDto = new UserDto();
		userDto.setJsmc("大区经理");
		List<UserDto> users = sjxxService.queryYhByJsmc(userDto);
		for (UserDto userDto_t : users) {
			try {
				String newSign = URLEncoder.encode(commonService.getSign(userDto_t.getYhid() + jsrqstart + jsrqend), "utf-8");
				userDto_t.setNewSign(newSign);
				if (userDto_t.getGwmc() != null && userDto_t.getGwmc().contains("（") && userDto_t.getGwmc().contains("）")){
					String gwmc = "（" + userDto_t.getGwmc().substring(userDto_t.getGwmc().indexOf("（")+1,userDto_t.getGwmc().indexOf("）")) + "）";
					userDto_t.setGwmc(gwmc);
				}else {
					userDto_t.setGwmc("");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("userDtos", users);
		return mav;
	}
	/**
	 * 平台统计，跳转平台列表(周报)(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getPlatformWeekListByJsrq")
	public ModelAndView getPlatformWeekListByJsrq(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_weekly_platform_jsrq");
		List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
		String jsrqstart = sjxxDto.getJsrqstart();
		String jsrqend = sjxxDto.getJsrqend();
		for (JcsjDto jcsjDto : jcsjDtos) {
			try {
				String newSign = URLEncoder.encode(commonService.getSign(jcsjDto.getCsid() + jsrqstart + jsrqend), "utf-8");
				jcsjDto.setNewSign(newSign);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("ptgsDtos", jcsjDtos);
		return mav;
	}
	/**
	 * 周报(营销)筛选设置界面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataScreenContent")
	public ModelAndView detectionunitJsjcdw(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_weekly_jsrq_screencontent");
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.XXDY_TYPE.getCode());
		jcsjDto.setCsdm("XMFL");
		jcsjDto=jcsjService.getDto(jcsjDto);
		XxdyDto xxdyDto=new XxdyDto();
		xxdyDto.setDylx(jcsjDto.getCsid());
		String strlist="";
		if (sjxxDto.getYxxs().length>0){
			for (int i=0;i<sjxxDto.getYxxs().length;i++){
				strlist+=","+sjxxDto.getYxxs()[i];
			}
		}
		if (strlist.length()>0){
			strlist=strlist.substring(1);
		}
		List<XxdyDto> xxdyDtos=xxdyService.getYxxMsg(xxdyDto);
		List<XxdyDto> xxdyDtoList = new ArrayList<>();
		if (StringUtil.isNotBlank(request.getParameter("dcl"))){
			for (XxdyDto xxdydto: xxdyDtos) {
				if (!"2".equals(xxdydto.getKzcs6())){
					xxdyDtoList.add(xxdydto);
				}
			}
			xxdyDtos = xxdyDtoList;
		}
		mav.addObject("xxdyDtos",xxdyDtos);
		mav.addObject("strlist",strlist);
		mav.addObject("bdid",request.getParameter("bdid"));
		mav.addObject("flag",request.getParameter("flag"));
		return mav;
	}
	/**
	 * 根据合作伙伴获取检测单位
	 * @return
	 */
	@RequestMapping("/getDetectionUnit")
	@ResponseBody
	public List<JcsjDto> getDetectionUnit(String hbmc, JcsjDto jcsjDto){
		List<String> ids = null;
		if(StringUtil.isNotBlank(hbmc)) {
			// 查询伙伴权限信息
			ids = hbdwqxService.selectByHbmc(hbmc);
		}
		// 查询检测项目
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jcsjDto.setIds(ids);
		List<JcsjDto> jcsjDtos = jcsjService.selectDetectionUnit(jcsjDto);
		if(jcsjDtos == null)
			jcsjDtos = new ArrayList<>();
		return jcsjDtos;
	}

	/**
	 * 区域经理下销售达成率页面
	 * @return
	 */
	@RequestMapping("/statistics/salesAttainmentRateByAreaView")
	public ModelAndView salesAttainmentRateByAreaView(XszbDto xszbDto, HttpServletRequest request) {
		String flag = request.getParameter("flag");
		String load_flg = request.getParameter("load_flg");
		String url = "";
		if ("daliy".equals(flag)) {
			url = "wechat/statistics/statistics_attainmentrate_daliy_area";
		} else if ("weekly".equals(flag)) {
			url = "wechat/statistics/statistics_attainmentrate_weekly_area";
		} else if ("daliy_wxyh".equals(flag)) {
			url = "wechat/statistic_wxyh/statistics_attainmentrate_daliy_area_wxyh";
		} else if ("weekly_wxyh".equals(flag)) {
			url = "wechat/statistic_wxyh/statistics_attainmentrate_weekly_area_wxyh";
		}
		ModelAndView mav = new ModelAndView(url);
		if ("daliy_wxyh".equals(flag)) {
			String jsrq = request.getParameter("jsrq");
			mav.addObject("jsrq",jsrq);
			mav.addObject("parentId",request.getParameter("parentId"));//用于跳转到统计主页面
		} else if ("weekly_wxyh".equals(flag)) {
			String jsrqstart = request.getParameter("jsrqstart");
			String jsrqend = request.getParameter("jsrqend");
			mav.addObject("jsrqstart",jsrqstart);
			mav.addObject("jsrqend",jsrqend);
			mav.addObject("parentId",request.getParameter("parentId"));//用于跳转到统计主页面
		}
		mav.addObject("load_flg",load_flg);
		mav.addObject("xszbDto", xszbDto);
		return mav;
	}
	/**
	 * 区域经理下销售达成率页面(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/salesAttainmentRateByAreaViewAndJsrq")
	public ModelAndView salesAttainmentRateByAreaViewAndJsrq(XszbDto xszbDto, HttpServletRequest request) {
		String flag = request.getParameter("flag");
		String load_flg = request.getParameter("load_flg");
		String url = "";
		if ("daliy".equals(flag)) {
			url = "wechat/statistics/statistics_attainmentrate_daliy_area_jsrq";
		} else if ("weekly".equals(flag)) {
			url = "wechat/statistics/statistics_attainmentrate_weekly_area_jsrq";
		} else if ("daliy_wxyh".equals(flag)) {
			url = "wechat/statistic_wxyh/statistics_attainmentrate_daliy_area_wxyh_jsrq";
		} else if ("weekly_wxyh".equals(flag)) {
			url = "wechat/statistic_wxyh/statistics_attainmentrate_weekly_area_wxyh_jsrq";
		}
		ModelAndView mav = new ModelAndView(url);
		if ("daliy_wxyh".equals(flag)) {
			String jsrq = request.getParameter("jsrq");
			mav.addObject("jsrq",jsrq);
			mav.addObject("parentId",request.getParameter("parentId"));//用于跳转到统计主页面
		} else if ("weekly_wxyh".equals(flag)) {
			String jsrqstart = request.getParameter("jsrqstart");
			String jsrqend = request.getParameter("jsrqend");
			mav.addObject("jsrqstart",jsrqstart);
			mav.addObject("jsrqend",jsrqend);
			mav.addObject("parentId",request.getParameter("parentId"));//用于跳转到统计主页面
		}
		mav.addObject("load_flg",load_flg);
		mav.addObject("xszbDto", xszbDto);
		return mav;
	}

	/**
	 * 销售达成率各区域经理下的统计
	 * @return
	 */
	@RequestMapping("/statistics/getSalesAttainmentRateByArea")
	@ResponseBody
	public Map<String,Object> getSalesAttainmentRateByArea(XszbDto xszbDto,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		//处理日期
		if(xszbDto.getKszq()!=null&&xszbDto.getJszq()!=null) {
			if("Y".equals(xszbDto.getZblxcsmc())) {
				try {
					Date startDate = DateUtils.parseDate("yyyy", xszbDto.getKszq());
					Date endDate = DateUtils.parseDate("yyyy",xszbDto.getJszq());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(startDate);
					xszbDto.setKszq(String.valueOf(calendar.get(Calendar.YEAR)));
					calendar.setTime(endDate);
					xszbDto.setJszq(String.valueOf(calendar.get(Calendar.YEAR)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		}
		List<Map<String, String>> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByArea(xszbDto);
		result.put("salesAttainmentRate", salesAttainmentRate);
		result.put("xszbDto",xszbDto);
		return result;
	}
	/**
	 * 销售达成率各区域经理下的统计(接收日期)
	 * @return
	 */
	@RequestMapping("/statistics/getSalesAttainmentRateByAreaAndJsrq")
	@ResponseBody
	public Map<String,Object> getSalesAttainmentRateByAreaAndJsrq(XszbDto xszbDto,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		//处理日期
		if(xszbDto.getKszq()!=null&&xszbDto.getJszq()!=null) {
			if("Y".equals(xszbDto.getZblxcsmc())) {
				try {
					Date startDate = DateUtils.parseDate("yyyy", xszbDto.getKszq());
					Date endDate = DateUtils.parseDate("yyyy",xszbDto.getJszq());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(startDate);
					xszbDto.setKszq(String.valueOf(calendar.get(Calendar.YEAR)));
					calendar.setTime(endDate);
					xszbDto.setJszq(String.valueOf(calendar.get(Calendar.YEAR)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		}
		List<Map<String, String>> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByAreaAndJsrq(xszbDto);
		result.put("salesAttainmentRate", salesAttainmentRate);
		result.put("xszbDto",xszbDto);
		return result;
	}

	/**
	 * pcr验证结果
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/library/getSjyzmxResult")
	@ResponseBody
	public Map<String, Object> getSjyzmxResult(HttpServletRequest request) throws UnsupportedEncodingException, IOException{
		Map<String,Object> map = new HashMap<>();
		JkdymxDto jkdymxDto=new JkdymxDto();
		 // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = new BufferedReader(
                new InputStreamReader(request.getInputStream(), "UTF-8"));
        String pcr_result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
        	pcr_result += getLine;
        }
        in.close();
		if(StringUtil.isBlank(pcr_result)) {
			map.put("status", "fail");
			map.put("errorCode", "未正常获取到数据！");
			return map;
		}
		log.error("pcr_result:"+pcr_result);
		WkmxPcrModel wkmxPcrModel = JSONObject.parseObject(pcr_result, WkmxPcrModel.class);
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			jkdymxDto.setLxqf("recv");
			jkdymxDto.setDysj(dateFm.format(new Date()));
			//jkdymxDto.setYwid(inspinfo.getYbbh());
			jkdymxDto.setNr(pcr_result);
			jkdymxDto.setDydz(request.getRequestURI());
			jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_PCRYZ.getCode());
			jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PCRYZ_RESULT.getCode());
			jkdymxDto.setSfcg("2");
			boolean result = sjyzjgService.getSjyzmxResult(wkmxPcrModel);
			if(result) {
				map.put("status", "success");
				map.put("errorCode", "保存成功!");
			}else {
				map.put("status", "fail");
				map.put("errorCode", "保存失败!");
			}
			jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.toString());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 先实现上传文件到服务器
	 */
	@RequestMapping(value = "/tl/uploadTlFile")
	@ResponseBody
	public String wjsc(HttpServletRequest request,MultipartFile file,String jsonparam) throws IOException{
		String ret = sjyzService.uploadTlFile(file,jsonparam);
		log.error("天隆调用上传接口/tl/uploadTlFile，调用完成");
		if (StringUtil.isNotBlank(ret)){
			return ret;
		}
		WkmxPcrModel wkmxPcrModel = JSONObject.parseObject(jsonparam, WkmxPcrModel.class);
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JkdymxDto jkdymxDto=new JkdymxDto();
		jkdymxDto.setLxqf("recv");
		jkdymxDto.setDysj(dateFm.format(new Date()));
		jkdymxDto.setNr(jsonparam);
		jkdymxDto.setDydz(request.getRequestURI());
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_PCRYZ.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PCRYZ_RESULT.getCode());
		jkdymxDto.setSfcg("2");
		jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据
		try {
			sjyzjgService.getSjyzmxResult(wkmxPcrModel);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("天隆ERROR:"+e);
			return StringUtil.isBlank(ret) ? DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")+" 上传成功" : ret;
		}
		return StringUtil.isBlank(ret) ? DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")+" 上传成功" : ret;
	}

	/**
	 * 天隆ResFirst结果上传处理
	 */
	@RequestMapping(value = "/experiments/uploadResFirstResult")
	@ResponseBody
	public String uploadResFirstResult(HttpServletRequest request,MultipartFile file,String jsonparam) {

		log.error("天隆调用上传接口 /experiments/uploadResFirstResult，jsonparam:"+jsonparam );
		String fjid = StringUtil.generateUUID();
		@SuppressWarnings("unchecked")
		Map<String, String> params = JSONObject.parseObject(jsonparam, Map.class);
		sjxxService.saveHttpServletRequest(request);
		params.put("fjid",fjid);
		String ret = sjxxResStatisticService.uploadResFirstResult(file,params);
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			JkdymxDto jkdymxDto=new JkdymxDto();
			jkdymxDto.setLxqf("recv");
			jkdymxDto.setDysj(dateFm.format(new Date()));
			jkdymxDto.setNr(jsonparam);
			jkdymxDto.setDydz(request.getRequestURI());
			jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_PCRYZ.getCode());
			jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_PCRYZ_RESULT.getCode());
			jkdymxDto.setSfcg("2");
			jkdymxService.insertJkdymxDto(jkdymxDto);//添加接口调用明细数据
			if (StringUtil.isBlank(ret)){
				//发送消息
				Map<String,Object> map = new HashMap<>();
				map.put("fjids",fjid);
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.Z_FILE_DISPOSE.getCode(),JSONObject.toJSONString(map));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("天隆ERROR:"+e);
			return StringUtil.isBlank(ret) ? DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")+" 上传成功" : ret;
		}
		return StringUtil.isBlank(ret) ? DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")+" 上传成功" : ret;
	}

	/**
	 * 送检验证页面历史信息
	 */
	@RequestMapping(value ="/sjyzHistory/pagedataSjyzHistory")
	@ResponseBody
	public Map<String,Object> sjyzHistory(SjyzDto sjyzDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		List<SjyzDto> sjyzlist = sjyzService.getByYzlbAndSjid(sjyzDto);
		map.put("sjyzlist",sjyzlist);
		return map;
	}



	/**
	 * 外部提交复检申请
	 * @param request
	 * @param br
	 * @return
	 */
	@RequestMapping(value="/recheck/submitRecheckAudit")
	@ResponseBody
	public Map<String, Object> submitRecheckAudit(HttpServletRequest request,BufferedReader br){
		String inputLine;
		String str = "";
		Map<String,Object> map = new HashMap<>();
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
			if(StringUtil.isBlank(str)) {
				map.put("status", "fail");
				map.put("errorCode", "未正常获取到数据！");
				log.error("未正常获取到数据！");
			}else{
				boolean result = sjxxService.submitRecheckAudit(str, request);
				if(result){
					map.put("status","success");
					map.put("errorCode", 0);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "保存失败！");
					log.error("保存失败！");
					log.error(str);
				}
			}
		} catch (IOException e) {
			map.put("status", "fail");
			map.put("errorCode", "IO获取数据异常！");
			log.error("IO获取数据异常！" + e.toString());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorCode", e.getMessage());
			log.error(e.toString());
		}
		return map;
	}

	/**
	 * 送检特殊申请回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/application/aduitCallback")
	@ResponseBody
	public Map<String,Object> auditCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = sjtssqService.callbackTssqAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
            if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
                if("1".equals((String) t_map.get("sfbcspgl"))) {
                    DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
                    if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
                            || ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
                        ddspglDto.setCljg("0");
                        ddspglService.updatecljg(ddspglDto);
                    }
                }
            }
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 开票申请回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/invoicing/requestsCallback")
	@ResponseBody
	public Map<String,Object> requestsCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = kpsqService.requestsCallback(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
				if("1".equals((String) t_map.get("sfbcspgl"))) {
					DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
					if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
							|| ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
						ddspglDto.setCljg("0");
						ddspglService.updatecljg(ddspglDto);
					}
				}
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 查看开票申请
	 * @param request
	 * @return
	 */
	@RequestMapping("/invoicing/getRequestsUrl")
	@ResponseBody
	public ModelAndView getRequestsUrl(HttpServletRequest request) {
		String ywid=request.getParameter("ywid");
		ModelAndView mav=new ModelAndView("wechat/dingtalk/incoicing_dingtalkView");
		if (StringUtil.isNotBlank(ywid)){
			KpsqDto kpsqDto = kpsqService.getDtoById(ywid);
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(kpsqDto.getFpsqid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INVOICE_CREDIT.getCode());
			List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("list",list);
			mav.addObject("dto",kpsqDto);
		}
		return mav;
	}

	/**
	 * 检出申请回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/application/detectionApplicationCallback")
	@ResponseBody
	public Map<String,Object> detectionApplicationCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = jcsqglService.callbackAuditDetectionApplication(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
				if("1".equals((String) t_map.get("sfbcspgl"))) {
					DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
					if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
							|| ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
						ddspglDto.setCljg("0");
						ddspglService.updatecljg(ddspglDto);
					}
				}
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}


	/**
	 * 商务合同审批回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/application/aduitContractCallback")
	@ResponseBody
	public Map<String,Object> aduitContractCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = swhtglService.callbackContractAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
				if("1".equals((String) t_map.get("sfbcspgl"))) {
					DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
					if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
							|| ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
						ddspglDto.setCljg("0");
						ddspglService.updatecljg(ddspglDto);
					}
				}
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 钉钉复测审批回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/application/aduitRecheckCallback")
	@ResponseBody
	public Map<String,Object> aduitRecheckCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = fjsqService.callbackRecheckAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
				if("1".equals((String) t_map.get("sfbcspgl"))) {
					DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
					if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
							|| ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
						ddspglDto.setCljg("0");
						ddspglService.updatecljg(ddspglDto);
					}
				}
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

		/**
	 * 获取实验室信息
	 * @return
	 */
	@RequestMapping(value ="/laboratory/getLaboratoryInfo")
	@ResponseBody
	public String getLaboratoryInfo(){
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<LaboratoryModel> laboratoryModels=new ArrayList<>();
		if(list!=null&&list.size()>0){
			for(JcsjDto dto:list){
				LaboratoryModel laboratoryModel=new LaboratoryModel();
				laboratoryModel.setName(dto.getCsmc());
				laboratoryModel.setCode(dto.getCsdm());
				laboratoryModels.add(laboratoryModel);
			}
		}
		return JSON.toJSONString(laboratoryModels);
	}
	@RequestMapping("/ceshi")
	@ResponseBody
	public void cehis (SjxxDto sjxxDto){
		List<SjxxDto> sjxxDtos=new ArrayList<>();
		List<FjsqDto> fjsqDtos=new ArrayList<>();
		if("DETECT_SJ".equals(sjxxDto.getJclx())){
			sjxxDtos=sjxxWsService.getDtoByLrsj(sjxxDto);
		}else if("DETECT_FJ".equals(sjxxDto.getJclx())){
			fjsqDtos=sjxxWsService.getFjDtoByLrsj(sjxxDto);
		}
		if(sjxxDtos!=null && sjxxDtos.size()>0){
			for(SjxxDto sjxxDto1:sjxxDtos){
				List<SjsyglDto> sjsyglDtos=sjsyglService.getDetectionInfo(sjxxDto1,null,sjxxDto.getJclx());
				if(sjsyglDtos!=null && sjsyglDtos.size()>0){
					for(SjsyglDto sjsyglDto:sjsyglDtos){
						sjsyglDto.setJsrq(sjxxDto1.getJsrq());
						sjsyglDto.setJsry(sjxxDto1.getJsry());
						sjsyglDto.setQysj(sjxxDto1.getQysj());
						sjsyglDto.setQyry(sjxxDto1.getQyry());
						sjsyglDto.setYwid(sjxxDto1.getSjid());
						sjsyglDto.setSyglid(StringUtil.generateUUID());
						if(StringUtil.isNotBlank(sjsyglDto.getDyid())){
							XxdyDto xxdyDto=new XxdyDto();
							xxdyDto.setDyid(sjsyglDto.getDyid());
							xxdyDto=xxdyService.getDto(xxdyDto);
							if("DNA".equals(xxdyDto.getKzcs2()) || "XD".equals(xxdyDto.getKzcs2())){
								sjsyglDto.setSyrq(sjxxDto1.getDsyrq());
							}else if("RNA".equals(xxdyDto.getKzcs2())) {
								sjsyglDto.setSyrq(sjxxDto1.getSyrq());
							}else if("HS".equals(xxdyDto.getKzcs2())) {
								sjsyglDto.setSyrq(sjxxDto1.getSyrq());
							}else{
								sjsyglDto.setSyrq(sjxxDto1.getQtsyrq());
							}
						}
					}
					sjsyglService.insertList(sjsyglDtos);
				}

			}
		}
		if(fjsqDtos!=null && fjsqDtos.size()>0){
			for(FjsqDto fjsqDto1:fjsqDtos){
				List<SjsyglDto> sjsyglDtos=sjsyglService.getDetectionInfo(null,fjsqDto1,sjxxDto.getJclx());
				if(sjsyglDtos!=null && sjsyglDtos.size()>0){
					for(SjsyglDto sjsyglDto:sjsyglDtos){
						sjsyglDto.setJsrq(fjsqDto1.getJsrq());
						sjsyglDto.setJsry(fjsqDto1.getJsry());
						sjsyglDto.setQysj(fjsqDto1.getQysj());
						sjsyglDto.setQyry(fjsqDto1.getQyr());
						sjsyglDto.setYwid(fjsqDto1.getFjid());
						sjsyglDto.setSyglid(StringUtil.generateUUID());
						if(StringUtil.isNotBlank(sjsyglDto.getDyid())) {
							XxdyDto xxdyDto = new XxdyDto();
							xxdyDto.setDyid(sjsyglDto.getDyid());
							xxdyDto = xxdyService.getDto(xxdyDto);
							if ("DNA".equals(xxdyDto.getKzcs2()) || "XD".equals(xxdyDto.getKzcs2())) {
								sjsyglDto.setSyrq(fjsqDto1.getDsyrq());
							} else if ("RNA".equals(xxdyDto.getKzcs2())) {
								sjsyglDto.setSyrq(fjsqDto1.getSyrq());
							} else if ("HS".equals(xxdyDto.getKzcs2())) {
								sjsyglDto.setSyrq(fjsqDto1.getSyrq());
							} else {
								sjsyglDto.setSyrq(fjsqDto1.getQtsyrq());
							}
						}
					}
					sjsyglService.insertList(sjsyglDtos);
				}
			}
		}
	}

	/**
	 * 返回PDF文件流(用于js直接调用打印机)
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/pdfFileStream")
	public void pdfFileStream(FjcfbDto fjcfbDto, HttpServletResponse response) throws Exception {
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null) {
			log.error("对不起，系统未找到相应文件!");
			throw new Exception("对不起，系统未找到相应文件!");
		}
		String wjlj = t_fjcfbDto.getWjlj();
		DBEncrypt crypt = new DBEncrypt();
		String pdfPath = crypt.dCode(wjlj);
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline;fileName=" + pdfPath + ";fileName*=UTF-8''" + pdfPath);
		//response.setHeader("Content-Disposition", "attachment; filename=\"" + pdfPath + "\"");
		FileInputStream in;
		OutputStream out;
		try {
			in = new FileInputStream(new File(pdfPath));
			out = response.getOutputStream();
			byte[] b = new byte[512];
			while ((in.read(b)) != -1) {
				out.write(b);
				out.flush();
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 纸质报告打印清单（由于对内接口'X-Frame-Options' 为 'deny',无法在iframe显示对应页面，故写成对外接口）
	 */
	@RequestMapping("/paperReport/printDetails")
	public ModelAndView printDetails(SjzzsqDto sjzzsqDto) {
		ModelAndView mav=new ModelAndView("wechat/paperReportsApply/paperReportsApply_print");
		List<SjzzsqDto> dtoList = sjzzsqService.getDtoList(sjzzsqDto);
		if(dtoList!=null&&dtoList.size()>0){
			sjzzsqDto.setSqrxm(dtoList.get(0).getSqrxm());
			sjzzsqDto.setSjr(dtoList.get(0).getSjr());
			sjzzsqDto.setDz(dtoList.get(0).getDz());
		}
		mav.addObject("sjzzsqDto",sjzzsqDto);
		mav.addObject("list",dtoList);
		return mav;
	}


	/**
	 * 获取附件信息
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value ="/qrCodeReport/getFjcfbInfo")
	@ResponseBody
	public FjcfbDto getFjcfbInfo(FjcfbDto fjcfbDto){
        return fjcfbService.getDto(fjcfbDto);
	}

	/**
	 * 获取十天内的芯片信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value ="/xpxx/pageGetListXpxxTenDays")
	@ResponseBody
	public Map<String,Object> pageGetListXpxxTenDays(SjxxDto sjxxDto){
		Map<String,Object>	hashMaps=new HashMap<>();
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,-10);
		Date d=cal.getTime();
		SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
		sjxxDto.setLrsj(sp.format(d));//获取十天前日期
		List<SjxxDto> list = sjxxService.getPagedDtoListDays(sjxxDto);
		hashMaps.put("detectionlist",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		hashMaps.put("sequencerlist",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SEQUENCER_CODE.getCode()));
		List<Map<String,Object>> listmap=new ArrayList<>();
		List<Map<String,Object>> listmap2=new ArrayList<>();
		List<Map<String,Object>> listmap3=new ArrayList<>();
		for (SjxxDto dto : list) {
			Map<String,Object>	 stringStringMap=new HashMap<>();
			Map<String,Object>	 stringStringMap2=new HashMap<>();
			Map<String,Object>	 stringStringMap3=new HashMap<>();
			if(dto.getLrsj()!=null){
				try {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getLrsj());
					Calendar ca = Calendar.getInstance();
					ca.setTime(date);
					int month = ca.get(Calendar.MONTH) + 1;
					int daytime=ca.get(Calendar.DAY_OF_MONTH);
					String time= Integer.toString(month) +"/"+ Integer.toString(daytime);
					stringStringMap.put("time",time);
					stringStringMap2.put("time",time);
					stringStringMap3.put("time",time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			stringStringMap.put("lrsj",dto.getLrsj());
			stringStringMap.put("intensity",dto.getIntensity());
			stringStringMap2.put("lrsj",dto.getLrsj());
			stringStringMap2.put("density",dto.getDensity());
			stringStringMap3.put("lrsj",dto.getLrsj());
			stringStringMap3.put("q30",dto.getQ30());
			listmap.add(stringStringMap);
			listmap2.add(stringStringMap2);
			listmap3.add(stringStringMap3);
		}
		hashMaps.put("intensity",listmap);
		hashMaps.put("density",listmap2);
		hashMaps.put("q30",listmap3);

		return hashMaps;
	}

	/**
	 * 获取redis 数据
	 * @param
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataGetInfo")
	@ResponseBody
	public Map<String, Object> pagedataGetInfo(String key) {
		Map<String,Object> map = new HashMap<>();
		Object hget = redisUtil.get( key);
//		SjxxDto dto = new SjxxDto();
		log.error("二维码手机上传附件："+key);
		if (hget!=null){
			map = JSON.parseObject((String) hget);
		}
		return map;
	}

	/**
	 * 将附件信息发送给OA接收
	 * @param
	 * @return
	 */
	@RequestMapping(value="/inspection/pagedataSendMessage")
	@ResponseBody
	public Map<String, Object> pagedataSendMessage(String key,String fjids) {
		log.error("二维码手机上传附件：key:"+key);
		Map<String,Object> map = new HashMap<>();
		Object hget = redisUtil.get( key);
		if (hget!=null && StringUtil.isNotBlank(fjids)){
			Map<String,Object> result = JSON.parseObject((String) hget);
			result.put("fjids",fjids);
			result.put("key",key);
			result.put("type","fjsc");
			amqpTempl.convertAndSend("wechat.exchange", MqType.SSE_SENDMSG+result.get("ssekey"),JSONObject.toJSONString(result));
		}else{
			map.put("status", "fail");
			return map;
		}

		map.put("status", "success");
		return map;
	}
	/**
	 * 外部接口 根据文库名称获取文库信息
	 * @param
	 * @return
	 */
	@RequestMapping(value ="/library/getLibraryInfo")
	@ResponseBody
	public String getLibraryInfo(String wkmc){
		if (StringUtil.isBlank(wkmc)){
			return "[]";
		}
		//将文库名称分割
		String[] wkmcSplit = wkmc.split("-");
		//获取文库名称中的日期,防止其他混入，2024年应该要以20开始
		int dateIndex = wkmcSplit.length-1;
		for (int i = wkmcSplit.length - 1; i >= 0; i--) {
			if (wkmcSplit[i].length()==8 && wkmcSplit[i].startsWith("20")){
				dateIndex = i;
				break;
			}
		}
		String date = wkmcSplit[dateIndex];
		String[] jcxms = new String[]{"DNA","RNA","tNGS","HS","TBtNGS","XD","Onco"};//内部编码
		//String[] jcxmdms = new String[]{"DNA","RNA","tNGS"};//检测项目
		String jcxm = "";//检测项目代码
		int nbbmIndex = -1;//内部编码索引
		for (int i = wkmcSplit.length - 1; i >= 0; i--) {
			//获取内部编码索引
			if (nbbmIndex==-1){
				for (int j = 0; j < jcxms.length; j++) {
					if (wkmcSplit[i].equals(jcxms[j])){
						nbbmIndex = i-1;
						if ("DNA".equals(jcxms[j])||"tNGS".equals(jcxms[j])||"HS".equals(jcxms[j])||"TBtNGS".equals(jcxms[j])||"XD".equals(jcxms[j])||"Onco".equals(jcxms[j])){
							jcxm = "D";
						} else if ("RNA".equals(jcxms[j])) {
							jcxm = "R";
						} else{
							jcxm = "D";
						}
						break;
					}
				}
			}
//			//获取检测项目代码
//			for (int j = 0; j < jcxmdms.length; j++) {
//				if (wkmcSplit[i].equals(jcxmdms[j])){
//					if ("DNA".equals(jcxmdms[j])){
//						jcxm = "D";
//					} else if ("RNA".equals(jcxmdms[j])) {
//						jcxm = "R";
//					} else if ("tNGS".equals(jcxmdms[j])){
//						jcxm = "D";
//					}
//					break;
//				}
//			}
		}

		String[] kydms = new String[]{"MDY","DY"};//科研前缀
		String kyPrefix = "";
		for (int i = 0; i < kydms.length; i++) {
			if (wkmcSplit[0].contains(kydms[i])&&wkmcSplit[1].length()<10){
				kyPrefix = wkmcSplit[0].substring(2);
				break;
			}
		}
		String nbbh = kyPrefix;
		String nbbm = kyPrefix;
		for (int i = 1; i < dateIndex-1; i++) {
			nbbm += (StringUtil.isNotBlank(nbbm)?("-"):"")+wkmcSplit[i];//带检测项目的内部编码
			if (i<=nbbmIndex){
				nbbh += (StringUtil.isNotBlank(nbbh)?("-"):"")+wkmcSplit[i];//不带检测项目的内部编码
			}
		}
		HashMap<String, Object> sqlMap = new HashMap<>();
		sqlMap.put("nbbm",nbbm);//无检测项目的内部编号
		sqlMap.put("nbbh",nbbh);//有检测项目的内部编号
		sqlMap.put("wkbm",wkmc);//文库编码
		sqlMap.put("jcxm",jcxm);//检测项目代码
		sqlMap.put("cjrq",date);//创建日期
		List<Map<String, Object>> libraryInfoList = sjxxWsService.getLibraryInfo(sqlMap);
		return JSON.toJSONString(libraryInfoList);
	}
	/**
	 * 测试数统计点击查看列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/testNumberListView")
	public ModelAndView testNumberListView(SjxxDto sjxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sampleList");
		List<SjxxDto> sjxxList=sjxxService.testNumberList(sjxxDto);
		mav.addObject("sjxxList", sjxxList);
		mav.addObject("load_flg",load_flg);
		return mav;
	}

	/**
	 * 外部接口 NGS-master调用修改实验日期接口 (自动上报接口/experiment/addSaveNgsData)
	 * @param
	 * @return
	 */
	@RequestMapping(value ="/inspection/modExperimentDate")
	@ResponseBody
	public Map<String,Object> modExperimentDate(String json){
		log.error("NGS-master自动上报 修改实验日期,JSON:  "+json);
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isBlank(json)){
			map.put("status","fail");
			return map;
		}

		List<Map> maps = JSONArray.parseArray(json, Map.class);
		if (null != maps && maps.size()>0){
			List<SjsyglDto> list = new ArrayList<>();
			for (Map map1 : maps) {
				String sampleName =  String.valueOf(map1.get("sampleName"));
				String inspectionUnitName =  String.valueOf(map1.get("InspectionUnitName"));
				String startTime =  String.valueOf(map1.get("StartTime"));
				if (StringUtil.isNotBlank(sampleName) && !(sampleName.startsWith("NC-") || sampleName.startsWith("PC-")|| sampleName.startsWith("DC-"))){
					SjsyglDto sjsyglDto = new SjsyglDto();
					String[] strings = sampleName.split(" ");//中文输入法，可能存在空格丢失情况
					String nbbm = strings[0];
					//截取空格，但可能出现中文输入下，空格丢失情况，此时手动截取内部编码
					if (  StringUtil.isNotBlank(strings[0]) && strings.length == 1  ){
						String[] strings_new = new String[2];
						String cskzinfo="";
						if ( strings[0].length()>=14 && (strings[0].startsWith("DY")||strings[0].startsWith("DX")) ){//科研编号DY开头，14位长度
							nbbm = strings[0].substring(0,14);
							List<String>  nbbm_ts = sjxxTwoService.getSjxxByLikeNbbm(nbbm);
							if(CollectionUtils.isNotEmpty(nbbm_ts) && nbbm_ts.size()==1){
								nbbm = nbbm_ts.get(0);
							}
							cskzinfo = strings[0].substring( nbbm.length());
						}else if( strings[0].length()>=15 && strings[0].startsWith("MD") ){
							nbbm = strings[0].substring(0,15);
                            List<String>  nbbm_ts = sjxxTwoService.getSjxxByLikeNbbm(nbbm);
							if(CollectionUtils.isNotEmpty(nbbm_ts) && nbbm_ts.size()==1){
								nbbm = nbbm_ts.get(0);
							}
							cskzinfo = strings[0].substring( nbbm.length());
						}else if (strings[0].length()>=13){//普通内部编号，13位长度
							nbbm = strings[0].substring(0,13);
							cskzinfo = strings[0].substring(13);
						}else if (strings[0].length()>=11){//内部编号，11位长度
							nbbm = strings[0].substring(0,11);
							cskzinfo = strings[0].substring(11);
						}
						if ( StringUtil.isNotBlank(cskzinfo) ){
							strings_new[0]= nbbm;
							strings_new[1]= cskzinfo;
							strings = strings_new;
						}

					}
					sjsyglDto.setNbbm(nbbm);
					sjsyglDto.setJcdwmc(inspectionUnitName);
					if (strings.length>1){
						sjsyglDto.setKzcs2(strings[1].split("/")[0]);
						sjsyglDto.setKzcs3(strings[1].split("/").length>1?strings[1].split("/")[1]:"");
						List<SjsyglDto> insertInfo = sjsyglService.getInfoByNbbm(sjsyglDto);
						for (SjsyglDto dto : insertInfo) {
							if (StringUtil.isBlank(dto.getSyrq())){
								dto.setSyrq(startTime);
								list.add(dto);
							}
							if (StringUtil.isNotBlank(dto.getJclxcskz1())) {//如果检测类型的扩展参数不为空
								SjsyglDto syInfo = getSyInfo(dto, startTime);
								syInfo.setSyrq(null);
								list.add(syInfo);
							}
						}
					}else{
						//仅使用内部编码和检测单位名称查找数据，存在一条数据，则更新，存在多条数据不处理
						List<SjsyglDto> insertInfo = sjsyglService.getInfoByNbbm(sjsyglDto);
						if (insertInfo != null && insertInfo.size() == 1 ){
							if (StringUtil.isBlank(insertInfo.get(0).getSyrq())){
								insertInfo.get(0).setSyrq(startTime);
								list.add(insertInfo.get(0));
							}
							if (StringUtil.isNotBlank(insertInfo.get(0).getJclxcskz1())) {//如果检测类型的扩展参数不为空
								SjsyglDto syInfo = getSyInfo(insertInfo.get(0), startTime);
								syInfo.setSyrq(null);
								list.add(syInfo);
							}
						}
					}
				}
			}
			if (CollectionUtils.isNotEmpty(list)){
				sjsyglService.updateSyList(list);
			}
		}
		map.put("status","success");
		return map;
	}


	public SjsyglDto getSyInfo(SjsyglDto sjsyglDto,String data){
		SjsyglDto dto = new SjsyglDto();
		dto.setYwid(sjsyglDto.getYwid());
		dto.setYwlx(sjsyglDto.getYwlx());
		if ("D".equals(sjsyglDto.getJclxcskz1())){//如果检测类型的扩展参数1为D
			dto.setDsyrq(data);//插入D实验日期
		}else if ("R".equals(sjsyglDto.getJclxcskz1())){//如果检测类型的扩展参数1为R
			dto.setRsyrq(data);//插入R实验日期
		}else if ("C".equals(sjsyglDto.getJclxcskz1()) || "T".equals(sjsyglDto.getJclxcskz1())){//如果检测类型的扩展参数1为C/T
			if ("D".equals(sjsyglDto.getJcxmcskz1())){//如果检测项目的扩展参数1为D
				dto.setDsyrq(data);//插入D实验日期
			}else if ("R".equals(sjsyglDto.getJcxmcskz1())){//如果检测项目的扩展参数1为R
				dto.setRsyrq(data);//插入R实验日期
			}else if ("C".equals(sjsyglDto.getJcxmcskz1())){//如果检测项目的扩展参数1为C
				dto.setDsyrq(data);//插入D实验日期
				dto.setRsyrq(data);//插入R实验日期
			}else {
				dto.setQtsyrq(data);//插入其他实验日期
			}
		}else {
			dto.setQtsyrq(data);//插入其他实验日期
		}
		return dto;
	}


	/**
	 * 查看商务合同信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/contract/getBusinessContractUrl")
	@ResponseBody
	public ModelAndView getBusinessContractUrl(HttpServletRequest request) {

		String htid=request.getParameter("htid");
		ModelAndView mav=new ModelAndView("wechat/dingtalk/contract_dingtalkView");
		if (StringUtil.isNotBlank(htid)){
			SwhtglDto dtoById = swhtglService.getDtoById(htid);
			if (null == dtoById)
				return mav;
			SwhtmxDto swhtmxDto = new SwhtmxDto();
			swhtmxDto.setHtid(dtoById.getHtid());
			List<SwhtmxDto> dtoList = swhtmxService.getDtoList(swhtmxDto);
			mav.addObject("dto", dtoById);
			mav.addObject("dtoList", dtoList);
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(dtoById.getHtid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_BUSINESS_CONTRACT.getCode());
			List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("list", list);
		}
		return mav;
	}


	/**
	 * 风险上机回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/riskBoard/aduitCallback")
	@ResponseBody
	public Map<String,Object> aduitCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = fxsjglService.callbackRiskBoardAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
				if("1".equals((String) t_map.get("sfbcspgl"))) {
					DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
					if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
							|| ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
						ddspglDto.setCljg("0");
						ddspglService.updatecljg(ddspglDto);
					}
				}
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 查看风险上机信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/risk/getBoardUrl")
	@ResponseBody
	public ModelAndView getBoardUrl(HttpServletRequest request) {

		String fxsjid=request.getParameter("fxsjid");
		ModelAndView mav=new ModelAndView("wechat/dingtalk/board_dingtalkView");
		if (StringUtil.isNotBlank(fxsjid)){
			FxsjglDto dtoById = fxsjglService.getDtoById(fxsjid);
			if (null == dtoById)
				return mav;
			mav.addObject("dto", dtoById);
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(dtoById.getFxsjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_RISK_BOARD.getCode());
			List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("list", list);
		}
		return mav;
	}

		/**
	 * 商务付款申请回调
	 * @param request
	 * @return
	 */
	@RequestMapping("/application/paymentApplicationCallback")
	@ResponseBody
	public Map<String,Object> paymentApplicationCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map= new HashMap<>();
		Map<String,Object> t_map= new HashMap<>();//用于接收返回值
		try {
			boolean result = fksqService.paymentApplicationCallback(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			if(StringUtil.isNotBlank((String) t_map.get("sfbcspgl"))) {
				if("1".equals((String) t_map.get("sfbcspgl"))) {
					DdspglDto ddspglDto=ddspglService.insertInfo(json_obj);
					if(("finish".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_TASK_CHANGE.equals(ddspglDto.getEventtype()))
							|| ("terminate".equals(ddspglDto.getType()) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(ddspglDto.getEventtype()))) {
						ddspglDto.setCljg("0");
						ddspglService.updatecljg(ddspglDto);
					}
				}
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 查看商务付款申请信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/application/getPaymentApplicationInfo")
	@ResponseBody
	public ModelAndView getPaymentApplicationInfo(HttpServletRequest request){
		ModelAndView mav=new ModelAndView("wechat/paymentApplication/paymentApplication_phoneView");
		//付款信息
		String fksqid=request.getParameter("fksqid");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(fksqid);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_BUSINESS_PAYMENT_APPLICATION_FILE.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	@RequestMapping("/pathogen/getPathogenResultByCode")
	@ResponseBody
	public String getPathogenResultByCode(HttpServletRequest request){
		List<Map<String, String>> ybInfoList;
		try {
			ybInfoList = sjxxWsService.getAndCheckYbxxInfo(request,false);
		} catch (BusinessException e) {
			return e.getMessage();
		}
		Map<String,Object> map = new HashMap<>();
		if (ybInfoList != null && ybInfoList.size() > 0) {
			List<String> ybbhs = ybInfoList.stream().map(item -> item.get("ybbh")).collect(Collectors.toList());
			List<Map<String, Object>> resutList = sjwzxxService.getJcjgInfoByCodes(ybbhs);
			map.put("data",resutList);
			String limit = "50";
			Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
			if (settting != null) {
				JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
				limit = setttingObj.getString("szz");
			}
			if (Integer.parseInt(limit) == ybbhs.size()){
				map.put("lastcode",ybbhs.get(ybbhs.size()-1));
			}
		}else {
			map.put("status", "fail");
			map.put("errorCode", "未查询到信息！");
			return JSON.toJSONString(map);
		}
		map.put("status","success");
		map.put("errorCode","0");
		return JSON.toJSONString(map);

	}

	/**
	 * 文件上传页面
	 * @return
	 */
	@RequestMapping("/pathogen/pagedataUploadFile")
	public ModelAndView pagedataUploadFile(String key) {
		ModelAndView mav =new ModelAndView("wechat/sjxx/uploadFile");
		mav.addObject("key",key);
		return mav;
	}

	/**
	 * 手机端文件上传页面
	 * @return
	 */
	@RequestMapping("/pathogen/pagedataPhoneUploadFile")
	public ModelAndView pagedataUploadFile(FjcfbDto fjcfbDto) {
		ModelAndView mav =new ModelAndView("wechat/file/uploadFile");
		mav.addObject("fjcfbDto",fjcfbDto);
		return mav;
	}


	/**
	 * 用于天隆仪器等确认是否可以连接到服务器
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/checkConnectStatus")
	@ResponseBody
	public String checkConnectStatus(HttpServletRequest request) {
		//log.error("checkConnectStatus: IP: "+request.getRemoteAddr());
		return "OK";
	}


	/**
	 * 结核耐药文件接收处理
	 * @param request
	 * @return
	 */
	@RequestMapping("/pathogen/disposeSaveFile")
	@ResponseBody
	public Map<String,Object> disposeSaveFile(HttpServletRequest request,MultipartFile file){
		log.error("耐药文件接收上传 /pathogen/disposeSaveFile filename:" + file.getName());
		Map<String,Object> resultmap=new HashMap<>();
		Date date = new Date();
		long milliseconds = date.getTime();
		org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
		if(StringUtil.isBlank(request.getParameter("sign"))){
			resultmap.put("msg","请传递签名sign");
			return resultmap;
		}
		String str=new String(base64.decode(request.getParameter("sign")), StandardCharsets.UTF_8);
		long requestSeconds=Long.valueOf(str);

		if(requestSeconds+(1000*60*5)<milliseconds){
			resultmap.put("msg","权限过期");
			return resultmap;
		}
		return sjxxWsService.disposeSaveFile(file);
	}
	
	/**
	 * 结核耐药文件接收处理
	 * @param request
	 * @return
	 */
	@RequestMapping("/pathogen/disposeSaveFileRetry")
	@ResponseBody
	public Map<String,Object> disposeSaveFileRetry(HttpServletRequest request){
		log.error("耐药文件接收上传 /pathogen/disposeSaveFileRetry");
		return sjxxWsService.disposeSaveFileRetry();
	}

	/**
	 * 扫码上传复检
	 * @return
	 */
	@RequestMapping("/scanUploadFile")
	public ModelAndView scanUploadFile(String wbcxdm) {
		ModelAndView mav =new ModelAndView("wechat/sjxx/uploadSampleCollectionFile");
		mav.addObject("ywlx", BusTypeEnum.IMP_INSPECTION_CONFIRM.getCode());
		mav.addObject("wbcxdm", wbcxdm);
		mav.addObject("menuurl", menuurl);
		return mav;
	}

	/**
	 * 通过内部编码获取信息
	 * @return
	 */
	@RequestMapping("/getPagInfo")
	@ResponseBody
	public Map<String, Object> getPagInfo(SjxxDto sjxxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(sjxxDto.getNbbm())){
			SjxxDto dtoVague = sjxxService.getDtoVague(sjxxDto);
			if (null == dtoVague){
				//再尝试用样本编号作为条件
				sjxxDto.setYbbh(sjxxDto.getNbbm());
				sjxxDto.setNbbm(null);
				dtoVague = sjxxService.getDtoVague(sjxxDto);
			}
			if(null !=dtoVague){
				map.put("status","success");
				map.put("dto",dtoVague);
			}else{
				map.put("status","fail");
				map.put("message","扫码获取信息失败！");
			}
		}else{
			map.put("status","fail");
			map.put("message","扫码获取信息失败！");
		}
		return map;
	}


	/**
	 * 保存送检附件
	 * @return
	 */
	@RequestMapping("/savePageInfo")
	@ResponseBody
	public Map<String, Object> savePageInfo(SjxxDto sjxxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(sjxxDto.getSjid()) && CollectionUtils.isNotEmpty(sjxxDto.getFjids())){
			boolean success = sjxxService.saveLocalScanFile(sjxxDto);
			if(!success){
				map.put("status","fail");
				map.put("message","附件保存失败！");
				return map;
			}
		}else{
			map.put("status","fail");
			map.put("message","数据丢失保存失败！");
		}
		return map;
	}

	/**
	 * 查看用户删除或者锁定的伙伴
	 */
	@RequestMapping("/partner/getLockedUserView")
	public ModelAndView getLockedUserView(SjhbxxDto sjhbxxDto) {
		ModelAndView mav=new ModelAndView("wechat/partner/partner_Ws_View");
		List<SjhbxxDto> list = sjhbxxService.getDtoList(sjhbxxDto);
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * 刷新营销统计周报redis数据
	 */
	@RequestMapping("/statistics/refreshLocal_weekly_jsrq")
	public void reflashWeeklyStatisticData(){
		sjxxTwoService.weekLeadStatisDefault();
	}
	//图片count
	int count = 0;
	/**
	 * 会议室随机图片显示
	 * @return
	 */
	@RequestMapping("/getRandomImage")
	@ResponseBody
	public void getRandomImage(HttpServletResponse response) {
		List<String> list = new ArrayList<>();
		list.add(applicationurl + "/images/trainImages/dragon-1.png");
		list.add(applicationurl + "/images/trainImages/dragon-2.png");
		list.add(applicationurl + "/images/trainImages/dragon-3.png");
		list.add(applicationurl + "/images/trainImages/dragon-4.png");
		if (count >= list.size()) {
			count = 0;
		}
		String fileUrl = list.get(count);
		count++;
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream;
		OutputStream os = null; //输出流
		try {
			RestTemplate restTemplate = new RestTemplate();
			Resource resource = restTemplate.getForObject(fileUrl, Resource.class);
			String[] array = fileUrl.split("[.]");
			String fileType = array[array.length - 1].toLowerCase();
			response.setContentType("image/" + fileType);
			iStream = resource.getInputStream();
			os = response.getOutputStream();
			bis = new BufferedInputStream(iStream);
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer);
				os.flush();
				i = bis.read(buffer);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@RequestMapping("/statistics/getWsStatistics")
	public ModelAndView getWsStatistics(HttpServletRequest request) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6); // 得到前六天
		Date weekdate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String weektoday = df.format(weekdate);
		String week = weektoday + "~" + date;
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_ws_list");
		mav.addObject("weektime", week);
		mav.addObject("csid", request.getParameter("csid"));
		JcsjDto jcsjDto1=new JcsjDto();
		jcsjDto1.setCsid(request.getParameter("csid"));
		JcsjDto jcsjDto=jcsjService.getJcsjByCsid(jcsjDto1);
		String cskz4Str=jcsjDto.getCskz4();
		String[] cskzArr=cskz4Str.split(",");
		mav.addObject("qhsj", cskzArr[0]);
		mav.addObject("tjsj", cskzArr[1]);
		mav.addObject("pptsj", cskzArr[2]);
		return mav;
	}

	@RequestMapping("/statistics/displayViewpc")
	public ModelAndView displayViewpc(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("common/view/display_viewpc");
			mv.addObject("urlPrefix", urlPrefix);
			String type = request.getParameter("type");
			JcsjDto jcsjDto=new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.HOME_PAGE_TYPE.getCode());
			jcsjDto.setCsdm(type);
			List<JcsjDto> jcsjDtos = jcsjService.getInstopDtoList(jcsjDto);
			if(!CollectionUtils.isEmpty(jcsjDtos)){
				mv.addObject("view_url", jcsjDtos.get(0).getCskz3()+"?csid="+jcsjDtos.get(0).getCsid());
			}
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/statistics/getWzxxBySfmc")
	@ResponseBody
	public Map<String, Object> getWzxxBySfmc(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<>();

		Date date = new Date();
		long milliseconds = date.getTime();
		org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
		if(StringUtil.isBlank(request.getParameter("sign"))){
			resultmap.put("msg","权限过期");
			return resultmap;
		}
		String str=new String(base64.decode(request.getParameter("sign")), StandardCharsets.UTF_8);
		long requestSeconds=Long.valueOf(str);

		if(requestSeconds+(1000*60*5)<milliseconds){
			resultmap.put("msg","权限过期");
			return resultmap;
		}
		SjxxDto t_sjxxDto = new SjxxDto();
		t_sjxxDto.setSfmc(sjxxDto.getSfmc());
		setDateByYear(t_sjxxDto,0,false);
		List<Map<String, String>> listBySf=sjxxService.getStatisWzSf(t_sjxxDto);
		ExternalUtil externalUtil=new ExternalUtil();
		resultmap.put("ggzd", externalUtil.encryptCbcMode_new(JSONObject.toJSONString(listBySf),"matridx123456789"));
		resultmap.put("searchData", externalUtil.encryptCbcMode_new(JSONObject.toJSONString(sjxxDto),"matridx123456789"));
		return resultmap;
	}
	@RequestMapping("/statistics/pagedataSjxxStatis")
	@ResponseBody
	public Map<String, Object> pageGetListSjxxStatis(SjxxDto sjxxDto) throws IOException {
		Map<String, Object> resultmap = new HashMap<>();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getCsid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_PPTGL_FILE.getCode());
		List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		List<String> base64List=new ArrayList<>();
		if(!CollectionUtils.isEmpty(list)){
			DBEncrypt p = new DBEncrypt();
			for(FjcfbDto t_fjcfbDto:list){
				File file = new File(p.dCode(t_fjcfbDto.getZhwjxx()));
				PDDocument doc = PDDocument.load(file);
				PDFRenderer renderer = new PDFRenderer(doc);
				int pageCount = doc.getNumberOfPages();
				for (int i = 0; i < pageCount; i++) {
					BufferedImage image = renderer.renderImageWithDPI(i, 95);
					ByteArrayOutputStream bao = new ByteArrayOutputStream();//io流
					ImageIO.write(image, "png", bao);
					byte[] bytes = Base64.getEncoder().encode(bao.toByteArray());
					String base64 = new String(bytes);
					base64 = base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
					base64List.add(base64);
					bao.close();
				}

			}
		}

		resultmap.put("base64List", base64List);
		SjxxDto t_sjxxDto = new SjxxDto();


		if(sjxxDto.getTj()==null) {
			sjxxDto.setTj("day");
			setDateByDay(sjxxDto,-6);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
		}
		//高关，疑似，无
		setDateByYear(t_sjxxDto,0,false);
		resultmap.put("searchData", sjxxDto);
		EtiologyDto etiologyDto=new EtiologyDto();
		etiologyDto.setCxlx("day");
		List<EtiologyDto> yj_month=etiologyServie.getYjByCxlx(etiologyDto);
		resultmap.put("yj_month",yj_month);
		etiologyDto.setCxlx("week");
		List<EtiologyDto> yj_week=etiologyServie.getYjByCxlx(etiologyDto);
		resultmap.put("yj_week",yj_week);
		return resultmap;
	}

	@RequestMapping("/statistics/pagedataListDtData")
	@ResponseBody
	public Map<String,Object> getdtYbsl(SjxxDto sjxxDto){
		return sjxxService.getYbslByday(sjxxDto);
	}

	/**
	 * 获取标本路线信息
	 * @param sjxxdto
	 * @return
	 */
	@RequestMapping(value = "/statistics/pagedataListYblx")
	@ResponseBody
	public Map<String,Object> pagedataListYblx(SjxxDto sjxxdto){
		Map<String, Object> map = new HashMap<>();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6); //得到最近7天
		Date beforesevendate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startdate=df.format(beforesevendate);//获取到7天前的日期
		sjxxdto.setJsrqstart(startdate);
		sjxxdto.setJsrqend(date);
		List<Map<String,Object>> yblxlist=sjxxService.getSjxlxx(sjxxdto);
		map.put("yblxlist", yblxlist);
		return map;
	}

	/**
	 * 通过条件查询刷新页面
	 *
	 * @return
	 */
	@RequestMapping("/statistics/pagedataSjxxStatisByTj")
	@ResponseBody
	public Map<String, Object> pagedataSjxxStatisByTj(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		String method = req.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("getYblxByYear".equals(method)) {
			//标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
			map.put("yblx", statisbyyblxlist);
		}else if("getYblxByMon".equals(method)) {
			//标本按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
			map.put("yblx", statisbyyblxlist);
		}else if("getYblxByWeek".equals(method)) {
			//标本按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybsList=sjxxService.getStatisByYblx(sjxxDto);
			map.put("yblx", ybsList);
		}else if("getYblxByDay".equals(method)) {
			//标本按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybsList=sjxxService.getStatisByYblx(sjxxDto);
			map.put("yblx", ybsList);
		}else if("getGgzdByYear".equals(method)) {
			//高关注度按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(sjxxDto);
			map.put("ggzd",ggzdlist);
		}else if("getGgzdByMon".equals(method)) {
			//高关注度按月
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(sjxxDto);
			map.put("ggzd",ggzdlist);
		}else if("getGgzdByWeek".equals(method)){
			//高关注度按周
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(sjxxDto);
			map.put("ggzd",ggzdlist);
		}else if("getDbByYear".equals(method)) {
			//合作伙伴按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("db", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("db",dblist);
		}else if("getDbByMon".equals(method)) {
			//合作伙伴按月
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("db", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("db",dblist);
		}else if("getDbByWeek".equals(method)) {
			//合作伙伴按周
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("db", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist=sjxxService.getStatisWeekBysjhb(sjxxDto);
			map.put("db",dblist);
		}else if("getDbByDay".equals(method)) {
			//合作伙伴按日
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("db", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("db",dblist);
		}else if("getJsrqByYear".equals(method)) {
			//阳性率-送检数量-报告数量 按年
			sjxxDto.setTj("year");
			setDateByYear(sjxxDto,-5,true);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());
			List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
			List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
			List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
			if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
				for (int i = 0; i <sjbglist.size(); i++){
					yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
					yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
				}
				map.put("jsrq",yblxList);
			}
		}else if("getJsrqByMon".equals(method)) {
			//阳性率-送检数量-报告数量 按月
			sjxxDto.setTj("mon");
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
			List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
			List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
			List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
			if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
				for (int i = 0; i <sjbglist.size(); i++){
					yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
					yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
				}
				map.put("jsrq",yblxList);
			}
		}else if("getJsrqByWeek".equals(method)) {
			//阳性率-送检数量-报告数量 按周（七天）
			sjxxDto.setTj("day");
			setDateByDay(sjxxDto,-6);
			sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
			List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
			List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
			if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
				for (int i = 0; i <sjbglist.size(); i++){
					yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
					yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
				}
				map.put("jsrq",yblxList);
			}
		}else if("getPossibleByYear".equals(method)) {
			//高关低关 按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possible",possiblelist);
		}else if("getPossibleByMon".equals(method)) {
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possible",possiblelist);
		}else if("getPossibleByWeek".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possible",possiblelist);
		}else if("getSjxxYearBySy".equals(method)) {
			//标本情况按年
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTj("year");
			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxMonBySy".equals(method)) {
			//标本情况按月
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTj("mon");
			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxWeekBySy".equals(method)){
			//标本情况按周
			setDateByWeek(sjxxDto);
			List<Map<String, String>> ybqklist=sjxxService.getSjxxWeekBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxDayBySy".equals(method)){
			//标本情况按日
			setDateByDay(sjxxDto,-6);
			sjxxDto.setTj("day");
			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}
		map.put("searchData", sjxxDto);
		return map;
		}

	/**
	 * 生信根据测序仪代码获取上机信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWksjmxList")
	@ResponseBody
	public Map<String, Object> getWksjmxList(HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();

		String csdm=request.getParameter("csdm");
		if(StringUtil.isBlank(csdm)){
			map.put("message","测序仪编号不能为空");
			map.put("status","error");
			map.put("code","500");
		}else{
			Map<String,String> paramMap=new HashMap<>();
			paramMap.put("csdm",csdm);
			List<Map<String, Object> >mapList=commonService.getWksjmxList(paramMap);
			if(!CollectionUtils.isEmpty(mapList)){
				for(Map<String, Object> dtoMap:mapList){
					dtoMap.put("con.dna",dtoMap.get("condna"));
					dtoMap.put("con.lib",dtoMap.get("conlib"));
				}
			}
			map.put("message","ok");
			map.put("status","success");
			map.put("code","200");
			map.put("mapList",mapList);
		}

		return map;
	}

	/**
	 * 获取申请单信息
	 * @param request
	 * 	organ:
	 * 	db:
	 * 	barcode:
	 * 	startTime:
	 * 	endTime:
	 * 	word: kQq2ZsH85D6B20CF49NcZSjxdlq/vfdz8P2OoZG3c18=
	 * 	sign:
	 * @return
	 */
	@RequestMapping("/getSampleDetails")
	@ResponseBody
	public Map<String, Object> getSampleDetails(HttpServletRequest request){
		String organ = request.getParameter("organ");
		String sign = request.getParameter("sign");
		String db = request.getParameter("db");
		String barcode = StringUtil.isNotBlank(request.getParameter("barcode"))?request.getParameter("barcode"):"";
		String startTime = StringUtil.isNotBlank(request.getParameter("startTime"))?request.getParameter("startTime"):"";
		String endTime = StringUtil.isNotBlank(request.getParameter("endTime"))?request.getParameter("endTime"):"";
		String jsonStr = db + barcode + startTime + endTime;
		log.error("委外接口-getSampleDetails-organ:{},db:{},barcode:{},startTime:{},endTime{},sign:{}",organ,db,barcode,startTime,endTime,sign);
		Map<String, Object> map = sjxxWsService.checkSecurityReceive(organ, jsonStr, sign, new DBEncrypt(),true);
		if(!"0".equals(map.get("errorCode"))){
			log.error("委外接口-getSampleDetails-获取数据失败:{}",map.get("errorInfo"));
			return map;
		}
		try {
			map = sjxxTwoService.getApplicationDetail(request);
			map.put("errorCode", 0);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorInfo", e.getMessage());
			map.put("errorCode", -1);
		}
		return map;
	}

	/**
	 * 获取申请单信息
	 * @param request
	 * 	organ:
	 * 	db:
	 * 	barcode:
	 * 	filename:
	 * 	word: kQq2ZsH85D6B20CF49NcZSjxdlq/vfdz8P2OoZG3c18=
	 * 	sign:
	 * @return
	 */
	@RequestMapping("/sendSampleReport")
	@ResponseBody
	public Map<String, Object> sendSampleReport(HttpServletRequest request){
		String organ = request.getParameter("organ");
		String sign = request.getParameter("sign");
		String db = request.getParameter("db");
		String barcode = request.getParameter("barcode");
		String jcxmcsdm = request.getParameter("jcxmcsdm");
		String jczxmcsdm = request.getParameter("jczxmcsdm");
		String filename = request.getParameter("filename");
		if (StringUtil.isBlank(barcode)){
			Map<String, Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("errorInfo", "未获取到barcode信息!");
			map.put("errorCode", -1);
			return map;
		}
		if (StringUtil.isBlank(jcxmcsdm)){
			Map<String, Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("errorInfo", "未获取到jcxmcsdm信息!");
			map.put("errorCode", -1);
			return map;
		}
		if (StringUtil.isBlank(filename)){
			Map<String, Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("errorInfo", "未获取到filename信息!");
			map.put("errorCode", -1);
			return map;
		}
		String data = StringUtil.isNotBlank(request.getParameter("data"))?request.getParameter("data"):"";
		String jsonStr = db + barcode + jcxmcsdm + (StringUtil.isNotBlank(jczxmcsdm)?jczxmcsdm:"") + data + filename;
		log.error("委外接口-sendSampleReport-organ:{},db:{},barcode:{},jcxmcsdm:{},jczxmcsdm:{},data:{},filename:{},sign:{}",organ,db,barcode,jcxmcsdm,jczxmcsdm,data,filename,sign);
		Map<String, Object> map = sjxxWsService.checkSecurityReceive(organ, jsonStr, sign, new DBEncrypt(),true);
		if(!"0".equals(map.get("errorCode"))){
			log.error("委外接口-sendSampleReport-获取数据失败:{}",map.get("errorInfo"));
			return map;
		}
		try {
			return sjxxTwoService.sendSampleReport(request,barcode,jcxmcsdm);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("errorInfo", e.getMessage());
			map.put("errorCode", -1);
		}
		return map;
	}


	@RequestMapping("/SendYuhuangdingReport")
	@ResponseBody
	public Map<String, Object> SendYuhuangdingReport(SjxxDto dto){
		Map<String, Object> map = new HashMap<>();
		map.put("status","faile");
		map.put("message","失败");
		try {
			SjxxDto sjxxDto = sjxxService.getDto(dto);
			if("济南艾迪康毓璜顶定制".equals(sjxxDto.getDb()) && StringUtil.isNotBlank(sjxxDto.getWbbm())){
				Map<String,Object> yuhuangdingInfoMap = new HashMap<>();
				yuhuangdingInfoMap.put("hzxm",sjxxDto.getHzxm());
				yuhuangdingInfoMap.put("ybbh",sjxxDto.getYbbh());
				yuhuangdingInfoMap.put("wbbm",sjxxDto.getWbbm());
				yuhuangdingInfoMap.put("sjid",sjxxDto.getSjid());
				yuhuangdingInfoMap.put("method","uploadById");
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(sjxxDto.getSjid());
				fjcfbDto.setYwlx(sjxxDto.getYwlx());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if (!CollectionUtils.isEmpty(fjcfbDtos)){
					yuhuangdingInfoMap.put("wjm",fjcfbDtos.get(0).getWjm());
					yuhuangdingInfoMap.put("wjlj",fjcfbDtos.get(0).getWjlj());
					yuhuangdingInfoMap.put("shryzsxm",dto.getShr());
					yuhuangdingInfoMap.put("jyryzsxm",dto.getJyr());
					yuhuangdingInfoMap.put("lrry","hospitalYuhuangding");
					amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(yuhuangdingInfoMap));
					map.put("status","success");
					map.put("message","ok");
				}
			} else {
				map.put("message","样本错误,请确认");
			}
		} catch (Exception e) {
			map.put("message",e.getMessage());
		}
		return map;
	}

	@RequestMapping("/application/pagedataFastCallBack")
	@ResponseBody
	public Map<String, Object> pagedataFastCallBack(HttpServletRequest request, Map<String,Object>map){
		Map<String,String> paramsMap=new HashMap<>();
		String params=request.getParameter("params");
		String share_info=request.getParameter("share_info");
		String failed_uids=request.getParameter("failed_uids");
		String missing_uids=request.getParameter("missing_uids");
		paramsMap.put("params",params);
		paramsMap.put("share_info",share_info);
		paramsMap.put("failed_uids",failed_uids);
		paramsMap.put("missing_uids",missing_uids);
		return jcsqglService.fastqCallBack(paramsMap);

	}


	/**
	 * 根据患者姓名和送检单位查找结果
	 *
	 * @param dto 向
	 * @return {@code Map<String, Object> }
	 */
	@RequestMapping("/inspection/searchResults")
	@ResponseBody
	public Map<String, Object> searchResults(SjxxDto dto){
		Map<String, Object> map = new HashMap<>();
		map.put("status","faile");
		map.put("message","失败");
		try {
			if (StringUtil.isAllBlank(dto.getHzxm(),dto.getJcdw())){
				map.put("message","患者姓名、检测单位请至少传一个！");
				return map;
			}
			List<Map<String,Object>> data = sjwzxxService.searchResult(dto);
			map.put("data",data);
		} catch (Exception e) {
			map.put("message",e.getMessage());
		}
		return map;
	}

	/**
	 * @Description: 发送下载报告异常通知
	 * @param request
	 * @param message
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/4/27 17:00
	 */
	@RequestMapping(value="/inspect/sendMessage")
	@ResponseBody
	public Map<String, Object> receiveInspectInfo(HttpServletRequest request, String message) {
		return sendMessageService.sendMessage(message);
	}


	@RequestMapping(value="/inspect/sendRabbitMsg")
	@ResponseBody
	public Map<String,Object> sendRabbitMsg(HttpServletRequest request, String str){
		Map<String,Object> map=new HashMap<>();
		map.put("str",str);
		log.error("85端2025-05-13错误信息同步接口："+str);
		try{
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECTION.getCode(), str);
		}catch (Exception e){
			map.put("message",e.getMessage());
		}

		return map;
	}

	/**
	 * 先声回调,先声在自己系统中下单，然后会调用这个接口，要求不能有任何的验证，只能在这个接口做ip验证
	 * @param request
	 * @return
	 *
	 **/
	@RequestMapping("/xiansheng/notify")
	@ResponseBody
	public Map<String, Object> getSampleNotify(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String orderId = request.getParameter("orderId");
		String type  = request.getParameter("type");

		boolean checkip=sjxxWsService.checkIp(request,"先声");//校验是否为白名单x
		// 如果 X-Forwarded-For 获取不到, 就去获取 X-Real-IP

		redisUtil.hset("HOSPITAL_XIANSHENG", orderId, type);
		try {
			Map<String, Object> codeMap = new HashMap<>();
			codeMap.put("method","selectById");
			codeMap.put("orderId",orderId);
			codeMap.put("XIANSHENG_URL",XIANSHENG_URL);
			codeMap.put("XIANSHENG_ORDERURL",XIANSHENG_ORDERURL);
			codeMap.put("XIANSHENG_APPID",XIANSHENG_APPID);
			codeMap.put("XIANSHENG_APPSECRET",XIANSHENG_APPSECRET);
			codeMap.put("XIANSHENG_BUSINESS",XIANSHENG_BUSINESS);
			sjxxService.matchingUtilNewRun(codeMap,"xianshengInfo");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		map.put("code", "200");
		map.put("msg", "成功");
//		if (checkip){
//			redisUtil.hset("HOSPITAL_XIANSHENG", orderId, type);
//			try {
//				Map<String, Object> codeMap = new HashMap<>();
//				codeMap.put("method","selectById");
//				codeMap.put("orderId",orderId);
//				codeMap.put("XIANSHENG_URL",XIANSHENG_URL);
//				codeMap.put("XIANSHENG_ORDERURL",XIANSHENG_ORDERURL);
//				codeMap.put("XIANSHENG_APPID",XIANSHENG_APPID);
//				codeMap.put("XIANSHENG_APPSECRET",XIANSHENG_APPSECRET);
//				codeMap.put("XIANSHENG_BUSINESS",XIANSHENG_BUSINESS);
//				sjxxService.matchingUtilNewRun(codeMap,"xianshengInfo");
//			} catch (Exception e) {
//				log.error(e.getMessage());
//			}
//			map.put("code", "200");
//			map.put("msg", "成功");
//		} else {
//
//			map.put("code", "404");
//			map.put("msg", "IP不在白名单");
//		}

		return map;
	}

	/**
	 * 提供给生信用的获取基础数据的接口
	 * @param request
	 * @return
	 *
	 **/
	@RequestMapping("/ws/common/getBasicdata")
	@ResponseBody
	public Map<String, Object> getBasicdata(HttpServletRequest request) {
		Map<String, Object> resultmap = new HashMap<>();
		String type = request.getParameter("type");
		String sign = request.getParameter("sign");

		org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
		String str=new String(base64.decode(request.getParameter("sign")), StandardCharsets.UTF_8);
		long requestSeconds=Long.valueOf(str);
		Date date = new Date();
		long milliseconds = date.getTime();
		if(requestSeconds+(1000*60*5)<milliseconds){
			resultmap.put("msg","权限过期");
			//return resultmap;
		}

		List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		//JcsjModel jcsjModel = new JcsjModel();
		//jcsjModel.setJclb(type);
		//List<JcsjModel> jcsjModels = jcsjService.getModelList(jcsjModel);

		SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter(JcsjDto.class, "csid","jclb","csdm","csmc","cspx","scbj");
		SimplePropertyPreFilter[] simplePropertyPreFilters = new SimplePropertyPreFilter[1];
		simplePropertyPreFilters[0] = simplePropertyPreFilter;
		resultmap.put("basic_list",JSONObject.toJSONString(jclxList,simplePropertyPreFilters));
		resultmap.put("code", "0");
		resultmap.put("msg", "成功");
		return resultmap;
	}

}
