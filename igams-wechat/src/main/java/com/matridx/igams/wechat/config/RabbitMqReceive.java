package com.matridx.igams.wechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.SjycfkDto;
import com.matridx.igams.common.dao.entities.SjyctzDto;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.file.AttachHelper;
import com.matridx.igams.common.file.WatermarkUtil;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.ISjycfkService;
import com.matridx.igams.common.service.svcinterface.ISjyctzService;
import com.matridx.igams.common.service.svcinterface.ISjyczdService;
import com.matridx.igams.common.service.svcinterface.ISyxxService;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ExceptionSSEUtil;
import com.matridx.igams.common.util.MqType;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.common.util.sseemitter.SseEmitterAllServer;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IHbdwqxService;
import com.matridx.igams.wechat.service.svcinterface.IMxxxService;
import com.matridx.igams.wechat.service.svcinterface.IPayinfoService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjdlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjgzbyService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjpdglService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjwlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.IWbsjxxService;
import com.matridx.igams.wechat.service.svcinterface.IWlcstxService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.igams.wechat.service.svcinterface.IXmsyglService;
import com.matridx.igams.wechat.service.svcinterface.IXxdjService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.pdf.PdfUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RabbitMqReceive {
	
	/**
	 * FTP服务器下word的路径
	 */
	@Value("${matridx.ftp.wordpath}")
	private String FTPWORD_PATH = null;
	
	/**
	 * FTP服务器下pdf的路径
	 */
	@Value("${matridx.ftp.pdfpath}")
	private String FTPPDF_PATH = null;
	
	/**
	 * FTP服务器地址
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.url}")
	private String FTP_URL = null;
	
	/**
	 * FTP服务器的用户名
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.user}")
	private String FTP_USER = null;
	
	@Value("${matridx.ftp.pd}")
	private String FTP_PD = null;
	
	@Value("${matridx.ftp.port}")
	private Integer FTP_PORT = null;

	@Value("${matridx.rabbit.ssekey:}")
	private String SSE_KEY = null;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Value("${matridx.qianmai.url:}")
	private String QIANMAI_URL;
	@Value("${matridx.qianmai.usercode:}")
	private String QIANMAI_USERCODE;
	@Value("${matridx.qianmai.password:}")
	private String QIANMAI_PASSWORD;
	@Value("${matridx.qianmai.entrustHosCode:}")
	private String QIANMAI_EntrustHosCode;
	@Value("${matridx.xinghe.ticket:}")
	private String xinghe_ticket;
	@Value("${matridx.xinghe.url:}")
	private String xinghe_url;
	@Value("${matridx.xinghe.wsdlurl:}")
	private String xinghe_wsdlurl;
	@Value("${matridx.xinghe.newurl:}")
	private String xinghe_newurl;
	@Value("${matridx.xinghe.newak:}")
	private String xinghe_ak;
	@Value("${matridx.xinghe.newsk:}")
	private String xinghe_sk;
	@Value("${matridx.xinghe.hospitalservice:}")
	private String xinghe_hospitalservice;
	@Value("${matridx.aidikang.url:}")
	private String AIDIKANG_URL;
	@Value("${matridx.aidikang.businessclass:}")
	private String AIDIKANG_BUSINESSCLASS;
	@Value("${matridx.aidikang.username:}")
	private String AIDIKANG_USERNAME;
	@Value("${matridx.aidikang.password:}")
	private String AIDIKANG_PASSWORD;
	@Value("${matridx.aidikang.newurl:}")
	private String AIDIKANG_NEW_URL;
	@Value("${matridx.yuhuangding.wsdlurl:}")
	private String yuhuangding_wsdlurl;

	@Value("${spring.rabbitmq.report.virtualHost:}")
	private String reportVirtualHost;
	@Value("${matridx.xiansheng.url:}")
	private String XIANSHENG_URL;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	ISjxxDao sjxxDao;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Autowired
	IPayinfoService payinfoService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IXxdjService xxdjService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	AttachHelper attachHelper;
	@Autowired 
	ISyxxService  syxxService;
	@Autowired
	IHbdwqxService hbdwqxService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ISjgzbyService sjgzbyService;
	@Autowired
	IFzjcxxService fzjcxxService;
	@Autowired
	IMxxxService mxxxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ISjwlxxService sjwlxxService;
	@Autowired
	ISjpdglService sjpdglService;
	@Autowired
	IWlcstxService wlcstxService;
	@Autowired
	IWbsjxxService wbsjxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.serverapplicationurl:}")
	private String serverapplicationurl;
	@Value("${matridx.wechat.getFjcfbDtoUrl:}")
	private String getFjcfbDtoUrl;
	@Value("${matridx.dingtalk.jumpdingtalkurl:}")
	private String jumpdingtalkurl;
	@Value("${matridx.wechat.downloadUrl:}")
	private String downloadUrl;

	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.createFolder:}")
	private String createFolder;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Autowired
	IWbaqyzService wbaqyzService;
	@Autowired
	ICommonService commonService;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	ISjsyglService sjsyglService;
	@Autowired
	IXmsyglService xmsyglService;
	@Autowired
	ExceptionSSEUtil exceptionSSEUtil;
	@Autowired
	ISjycService sjycService;
	@Autowired
	ISjyctzService sjyctzService;
	@Autowired
	ISjycfkService sjycfkService;
	@Autowired
	ISjyczdService sjyczdService;
	@Autowired
	IFzjcxxPJService fzjcxxPJService;
	@Autowired
	ExtendAmqpUtils extendAmqpUtils;
    @Autowired
	IYyxxService yyxxService;
    @Autowired
	ISjwzxxService sjwzxxService;
    @Autowired
	ISjdlxxService sjdlxxService;
    @Autowired
	ISjnyxService sjnyxService;
    @Autowired
	ISjxxjgService sjxxjgService;
    @Autowired
	ISjbgsmService sjbgsmService;

	@Autowired
	IXxdyService xxdyService;

	@Autowired
	ISjkzxxService sjkzxxService;
	
	/**
     * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	
	private Logger log = LoggerFactory.getLogger(RabbitMqReceive.class);

	@RabbitListener(queues = "wechat.file.dispose", containerFactory="defaultFactory")
	public void ZFileDisPose(String str) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		User userDto = null;
		try {
			log.error("RFS Rabbit:"+str);
			map = JSON.parseObject(str, new TypeReference<Map<String, Object>>(){});

			if(null == map.get("yhid")){
				userDto = new User();
				List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(DingMessageType.RFS_UP_TYPE.getCode());
				if (!CollectionUtils.isEmpty(ddxxList)){
					userDto.setYhid(ddxxList.get(0).getYhid());
					userDto.setYhm(ddxxList.get(0).getYhm());
					userDto.setZsxm(ddxxList.get(0).getZsxm());
					userDto.setDdid(ddxxList.get(0).getDdid());
				}else{
					log.error("天隆RFS上传未配置，钉钉消息，类型：RFS_UP_TYPE");
					return;
				}
			}else{
				String yhid = map.get("yhid").toString();
				User user = new User();
				user.setYhid(yhid);
				userDto = commonService.getUserInfoById(user);
			}
			sjxxService.disposeFile(map,userDto);
		} catch (Exception e) {
			log.error("ResFirst文件上传错误:"+e.toString());
			Object object = redisUtil.get(map.get("fjids").toString());
			out:if (null != object){
				int maxNumber = 4;
				XtszDto dtoById = xtszService.getDtoById("rabbit.errorNumber");
				if (null !=dtoById && StringUtil.isNotBlank(dtoById.getSzz()))
					maxNumber = Integer.parseInt(dtoById.getSzz());
				int count = (int)object;
				if (count == maxNumber){
					redisUtil.del(map.get("fjids").toString());
					if (null == map.get("yhid"))
						break out;
					talkUtil.sendWorkMessage(userDto.getYhm(), userDto.getDdid(), "ResFirst文件上传错误消息",StringUtil.replaceMsg( "# ResFirst文件上传错误消息\n"+
									"请检测上传文件或联系管理员解决！\n" +
									"* 错误消息：#{0}\n" +
									"* 时间：#{1} ",String.valueOf(e.toString())==null?"上传失败！":String.valueOf(e.toString()),
							DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
				}else{
					count ++;
					redisUtil.set(map.get("fjids").toString(),count,60);
				}
			}else{
				redisUtil.set(map.get("fjids").toString(),1,60);
			}
			throw new BusinessException("ResFirst天隆上传重试！");
		}
	}
	/**
	 * 物流派单
	 * @param message
	 */
	@RabbitListener(queues = ("wechat.wlwjd.send.queue"), containerFactory="defaultFactory")
	public void receiveWlwjdMessage(String message){
		log.error("wechat.wlwjd.send.queue接收到消息，内容为："+message);
		try{
			Map<String, String> map = JSON.parseObject(message, new TypeReference<Map<String, String>>(){});
//			List<SjwltzDto> userList = JSONObject.parseArray(String.valueOf(map.get("userList")),SjwltzDto.class);
//			SjpdglDto sjpdglDto = JSONObject.parseObject(String.valueOf(map.get("sjpdglDto")),SjpdglDto.class);

			List<Map> userList = JSONObject.parseArray(String.valueOf(map.get("userList")),Map.class);
			Map<String,String> sjpdglDto = JSONObject.parseObject(String.valueOf(map.get("sjpdglDto")),new TypeReference<Map<String, String>>(){});
//			XtszDto times_sjry = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_overtime_inform_sjry")),XtszDto.class);
			XtszDto times_jdry = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_overtime_inform_jdry")),XtszDto.class);
			XtszDto times_max = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_overtime")),XtszDto.class);
			SjwlxxDto sjwlxxDto =new SjwlxxDto();
			SjwlxxDto dto=JSONObject.parseObject(String.valueOf(redisUtil.get("SJWLXX:"+sjpdglDto.get("wlid"))),SjwlxxDto.class);
			if(dto!=null){
				sjwlxxDto =dto;
			}
			String wlzt = sjwlxxDto.getWlzt();
			if (StringUtil.isBlank(wlzt)){
				//redis获取不到信息，从DB取
				log.error("从数据库获取物流状态");
				sjwlxxDto.setWlid(sjpdglDto.get("wlid"));
				SjwlxxDto sjwlxx = sjwlxxService.getSjwlxxDtoById(sjwlxxDto);
				if (sjwlxx != null ){
					wlzt = sjwlxx.getWlzt();
					redisUtil.set("SJWLXX:"+ sjwlxx.getWlid() , JSON.toJSONString(sjwlxx)  ,28800);//注意redis过期时间是秒为单位，不是毫秒，时效8h
				}
			}
			if (StringUtil.isNotBlank(wlzt)){
				if ("10".equals(wlzt)){//00为保存，10为派单，20为接单
					//未被接单
					WlcstxDto wlcstxDto = new WlcstxDto();
					wlcstxDto.setYwid(sjpdglDto.get("wlid"));
					wlcstxDto = wlcstxService.getDto(wlcstxDto);
					int csnum = Integer.parseInt(wlcstxDto.getCscs());
					if (csnum<=Integer.parseInt(times_max.getSzz())){
						//超时次数小于最大次数
						//判断超时次数是否达到退出死信息的次数，否则在加入延迟队列
						//判断进行接单人员通知还是上级人员通知，并重新入队列
						List<String> jdtzs = Arrays.asList(times_jdry.getSzz().split(","));
						for (String jdtznum : jdtzs){
							int num = Integer.parseInt(jdtznum);
							if (csnum == num){
								//通知接单人员，超时次数+1
//								String token = talkUtil.getToken();
								if (userList.size()>0){
									for (Map<String,String> sjwltzDto: userList) {
										String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/orderlist/orderlist&urlPrefix="+urlPrefix+"&wbfw=1","utf-8");
										List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
										OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
										btnJsonList.setTitle("小程序");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										User user_t=new User();
										user_t.setYhid(sjwltzDto.get("ryid"));
										user_t=commonService.getUserInfoById(user_t);
										if(user_t!=null){
											talkUtil.sendCardMessage(user_t.getYhm(),
													sjwltzDto.get("ddid"),
													xxglService.getMsg("ICOMM_WLCSTZ00001"),
													StringUtil.replaceMsg(  xxglService.getMsg("ICOMM_WLCSTZ00002"),
															sjpdglDto.get("qylxr"),
															sjpdglDto.get("qylxdh"),
															sjpdglDto.get("qydz"),
															sjpdglDto.get("yjsj"),
															sjpdglDto.get("bblxmc"),
															sjpdglDto.get("jcdwmc"),
															sjpdglDto.get("jsfsmc"),
															sjpdglDto.get("pdbz"),
															DateUtils.getCustomFomratCurrentDate("HH:mm:ss")
													),
													btnJsonLists,"1"
											);
										}
									}
								}
							}
						}

						//更新物流超时提醒表的次数++
						boolean isSuccess = wlcstxService.insertOrUpdateWlcstx(wlcstxDto);
						if (!isSuccess)
							return;
						amqpTempl.convertAndSend("wl_delay_exchange", "wl_delay_key", JSONObject.toJSONString(map), new MessagePostProcessor() {
							@Override
							public Message postProcessMessage(Message message) throws AmqpException {
								XtszDto gaptime = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_gaptime")),XtszDto.class);
								int time_min;
								String time_str;
								if (gaptime!=null && StringUtil.isNotBlank(gaptime.getSzz())){
									time_min = Integer.parseInt(gaptime.getSzz())*60000;
									time_str = String.valueOf(time_min);
								}else {
									time_str = "300000";//未取到值则取默认的五分钟
								}
//								message.getMessageProperties().setExpiration("300000");
								message.getMessageProperties().setExpiration(time_str);
								return message;
							}
						});
//				List<String> sjtzs = Arrays.asList(times_sjry.getSzz().split(","));
//				for (String sjtznum : sjtzs){
//					int num = Integer.parseInt(sjtznum);
//					if (csnum == num){
//						//通知上级人员，超时次数+1
//
//						//更新物流超时提醒表的次数++
//						boolean isSuccess = wlcstxService.insertOrUpdateWlcstx(wlcstxDto);
//						if (!isSuccess)
//							return;
//						break;
//					}
//				}
					}
				}
			}else {
				//wlzt为空的情况，抛出错误
				log.error("物流超时rebbit接收消息提醒，接单标记为空");
				throw new BusinessException("物流超时rebbit接收消息提醒，接单标记为空");
			}
		}catch (Exception e){
			log.error("wechat.wlwjd.send.queue物流超时提醒出错"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.OVERTIME_WLPD);
			if(StringUtil.isNotBlank(message)) {
				if(message.length() > 3999) {
					xxdlcwglDto.setYsnr(message.substring(0,3999));
				}else
					xxdlcwglDto.setYsnr(message);
			}
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	//=================================================================================
	
	@RabbitListener(queues = MqType.WECHAR_USER_MOD, containerFactory="defaultFactory")
	public void userMod(String str) {
		log.error("Receive usermod:"+str);
        try {
        	//根据微信号在 微信用户表里查找 不用考虑删除标记，如果已经存在，则更新信息，如果不存在，则新增记录
        	WxyhDto wxyhDto = JSONObject.parseObject(str, WxyhDto.class);
			wxyhService.userMod(wxyhDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAR_USER_MOD);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive usermod:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAR_USER_MOD);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqType.WECHAR_SUBSCIBE, containerFactory="defaultFactory")
	public void subscibe(String str) {
		log.error("Receive subscibe:"+str);
        try {
        	//根据微信号在 微信用户表里查找 不用考虑删除标记，如果已经存在，删除标记为1则更新为0，如果不存在，则新增记录
        	WxyhDto wxyhDto = JSONObject.parseObject(str, WxyhDto.class);
			wxyhService.subscibe(wxyhDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAR_SUBSCIBE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive subscibe:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAR_SUBSCIBE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqType.WECHAR_UNSUBSCIBE, containerFactory="defaultFactory")
	public void unsubscibe(String str) {
		log.error("Receive unsubscibe:"+str);
        try {
			//根据微信号在 微信用户表里查找，如果已经存在，则更新删除标记为1 ，同时更新删除时间，如果不存在，则不做处理
        	WxyhDto wxyhDto  = JSONObject.parseObject(str, WxyhDto.class);
			wxyhService.unsubscibe(wxyhDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAR_UNSUBSCIBE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive unsubscibe:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAR_UNSUBSCIBE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }

	@RabbitListener(queues = MqType.WECHAT_AUTHORIZE, containerFactory="defaultFactory")
	public void authorize(String str) {
		log.error("Receive authorize:"+str);
        try {
			//根据微信号在 微信用户表里查找，如果已经存在，则更新删除标记为1 ，同时更新删除时间，如果不存在，则不做处理
        	WxyhDto wxyhDto  = JSONObject.parseObject(str, WxyhDto.class);
			wxyhService.authorize(wxyhDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAT_AUTHORIZE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive authorize:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAT_AUTHORIZE);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqType.WECHAT_TEXT, containerFactory="defaultFactory")
	public void wechatText(String str) {
		log.error("Receive wechatText:"+str);
        try {
			//给相应的人发送钉钉消息
        	WeChatTextModel weChatTextModel = JSONObject.parseObject(str, WeChatTextModel.class);
        	JcsjDto jcsjDto = new JcsjDto();
    		jcsjDto.setJclb("DINGMESSAGETYPE");
    		jcsjDto.setCsdm("MATRIDX_TEXT_TYPE");
    		List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
			if(ddxxglDtos != null && ddxxglDtos.size() > 0){
				//查询用户信息
				String openid = weChatTextModel.getFromUserName();
				if(StringUtil.isBlank(openid)){
					openid = weChatTextModel.getOpenid();
				}
				WxyhDto wxyhDto = wxyhService.getDtoById(openid);
				if(wxyhDto != null){
					//发送钉钉消息
//					String token = talkUtil.getToken();
					String msgTitle = xxglService.getMsg("ICOMM_SW00001", weChatTextModel.getGzpt());
					String msgInfo = xxglService.getMsg("ICOMM_SW00002",wxyhDto.getWxm(),wxyhDto.getYhm(),weChatTextModel.getContent(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					for(int i=0;i<ddxxglDtos.size();i++){
						if(StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid())){
							talkUtil.sendWorkMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), msgTitle,msgInfo);
						}
					}
				}else {
					//无法获取用户信息，发送钉钉消息
//					String token = talkUtil.getToken();
					String msgTitle = xxglService.getMsg("ICOMM_SW00001", weChatTextModel.getGzpt());
					String msgInfo = xxglService.getMsg("ICOMM_SW00002",weChatTextModel.getNickname(),"无",weChatTextModel.getContent(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"));
					for(int i=0;i<ddxxglDtos.size();i++){
						if(StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid())){
							talkUtil.sendWorkMessage(ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), msgTitle,msgInfo);
						}
					}
				}
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAT_TEXT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive checkApply:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.WECHAT_TEXT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }
	
	@RabbitListener(queues = MqType.MENU_CLICK, containerFactory="defaultFactory")
	public void menuClick(String str) {
		log.error("Receive Menu Click:"+str);
    }
	
	@RabbitListener(queues = MqType.REGISTER, containerFactory="defaultFactory")
	public void getResiter(String str) {
		log.error("Receive getResiter:"+str);
		try{
			XxdjDto xxdjDto=JSONObject.parseObject(str,XxdjDto.class);
			xxdjService.checkXxdjDto(xxdjDto);
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.REGISTER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e){
			// TODO: handle exception
			log.error("Receive checkApply:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.REGISTER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	@RabbitListener(queues = MqType.ADD_TRANSFER, containerFactory="defaultFactory")
	public void addTransfer(String str) {
		log.error("Receive addTransfer:"+str);
        try {
			//把数据更新到 igams_jkdymx 表
        	JkdymxDto jkdymxDto = JSONObject.parseObject(str, JkdymxDto.class);
        	jkdymxService.insertJkdymxDto(jkdymxDto);
        	
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.ADD_TRANSFER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive addTransfer:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.ADD_TRANSFER);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }

	@RabbitListener(queues = MqType.MATCHING_SEND_REPORT, containerFactory="defaultFactory")
	public void matchingReportSend(String str) {
		log.error("Receive matchingReportSend:"+str);
		try {
			//发送报告至对接接口
			log.error("matchingReportSend:开始");
			Map<String,Object> matchingReportSendMap = JSONObject.parseObject(str, new TypeReference<Map<String, Object>>(){});
			if (matchingReportSendMap.containsKey("Repetitions")){
				// 判断重复次数
				int repetitions = Integer.parseInt(matchingReportSendMap.get("Repetitions").toString());
				int systemRepetitions = 5;
				Object xtsz=redisUtil.hget("matridx_xtsz","matching.send.report.repetitions");
				if(xtsz!=null) {
					XtszDto xtszDto = JSON.parseObject(String.valueOf(xtsz), XtszDto.class);
					if(StringUtil.isNotBlank(xtszDto.getSzz())){
						systemRepetitions = Integer.parseInt(xtszDto.getSzz());
					}
				}
				if (repetitions >= systemRepetitions){
					log.error("matchingReportSend:失败,超过系统设置的最大次数");
					// 发送钉钉,通知相应人员
					StringBuilder msgContent = new StringBuilder("报告回传失败!回传次数已超过系统设置的最大次数!");
					msgContent.append("\n\n").append("患者姓名:"+matchingReportSendMap.get("hzxm"));
					msgContent.append("\n\n").append("样本编号:"+matchingReportSendMap.get("ybbh"));
					msgContent.append("\n\n").append("外部编码:"+matchingReportSendMap.get("wbbm"));

					if (matchingReportSendMap.get("ExceptionMessage") != null){
						List<String> exceptionMessage = JSONArray.parseObject((String) matchingReportSendMap.get("ExceptionMessage"), new TypeReference<List<String>>() {}) ;
						for (String s : exceptionMessage) {
							msgContent.append("\n\n").append(s);
						}
					}
					List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.ABNORMAL_REPORT_INFO.getCode());
					if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
						for (DdxxglDto ddxxglDto : ddxxglDtolist) {
							if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
								talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), "报告回传失败!"+matchingReportSendMap.get("ybbh"), msgContent.toString());
							}
						}
					}
					return;
				}
			}
			if ("hospitalQianmai".equals(matchingReportSendMap.get("lrry"))){
				matchingReportSendMap.put("QIANMAI_URL",QIANMAI_URL);
				matchingReportSendMap.put("QIANMAI_USERCODE",QIANMAI_USERCODE);
				matchingReportSendMap.put("QIANMAI_PASSWORD",QIANMAI_PASSWORD);
				matchingReportSendMap.put("QIANMAI_EntrustHosCode",QIANMAI_EntrustHosCode);
				sjxxService.matchingUtilNewRun(matchingReportSendMap,"qianmaiInfo");
			}else if("hospitalHenuo2".equals(matchingReportSendMap.get("lrry"))){
				sjxxService.matchingUtilNewRun(matchingReportSendMap,"henuoInfo");
			}
			else if ("hospitalXinghe".equals(matchingReportSendMap.get("lrry"))||"hospitalXingheNew".equals(matchingReportSendMap.get("lrry"))){
				boolean isMergeSuccess = true;
				Map<String,Object> xingheInfoMap = JSONObject.parseObject(str, new TypeReference<Map<String, Object>>(){});
				String sjid = (String) xingheInfoMap.get("sjid");
				String fwjm = (String) xingheInfoMap.get("fwjm");
				String wjlj = (String) xingheInfoMap.get("wjlj");
				String fwjlj = (String) xingheInfoMap.get("fwjlj");
				String ywlx = (String) xingheInfoMap.get("ywlx");
				String hospitalId =  (String) xingheInfoMap.get("hospitalId");//杏和医院id:浙江医院33A006，浙江省新华医院33A007，浙江省人民医院33A001
				if ("IMP_REPORT_SEQ_INDEX_TEMEPLATE_O".equals(ywlx) && ("33A006".equals(hospitalId) || "33A001".equals(hospitalId))){
					//浙江医院、浙江省人民医院 特殊处理，不回传Onco报告
					log.error("PDF文件上传报告至杏和LIS系统。当前报告为 "+hospitalId+" Onco报告，不调用回传接口。");

				} else {
					List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjid);
					List<FjcfbDto> fjcfbDtos = new ArrayList<>();
					for (FjcfbDto t_fjcfbDto : t_fjcfbDtos) {
						if ((t_fjcfbDto.getYwlx().indexOf("IMP_REPORT_ONCO_QINDEX_TEMEPLATE")>-1 || t_fjcfbDto.getYwlx().indexOf("IMP_REPORT_SEQ_")>-1 || t_fjcfbDto.getYwlx().indexOf("IMP_REPORT_TNGS_TEMEPLATE")>-1)&& t_fjcfbDto.getYwlx().indexOf("WORD")==-1 ){
							if (("33A006".equals(hospitalId) || "33A001".equals(hospitalId)) && t_fjcfbDto.getYwlx().contains("IMP_REPORT_SEQ_INDEX_TEMEPLATE_O")){
								//浙江医院、浙江省人民医院 特殊处理，不合并Onco报告
								log.error("PDF文件上传报告至杏和LIS系统。"+hospitalId+" Onco报告特殊处理。不通过杏和系统回传");
							} else {
								fjcfbDtos.add(t_fjcfbDto);
							}
						}
					}
					DBEncrypt p = new DBEncrypt();
					String pdfWjm = p.dCode(fwjm);
					String pdfWjlj = p.dCode(wjlj);
					if (fjcfbDtos.size()>1){
						pdfWjm = System.currentTimeMillis() + RandomUtil.getRandomString() + ".pdf";
						String mergeFilePath = fwjlj.replace(ywlx,"IMP_REPORT_MREGE");
						mkDirs(mergeFilePath);
						pdfWjlj = mergeFilePath +"/"+ pdfWjm;
						PdfUtil pdfUtil = new PdfUtil();
						DBEncrypt dbEncrypt = new DBEncrypt();
						List<String> fileStrings = new ArrayList<>();
						for (FjcfbDto fjcfbDto : fjcfbDtos) {
							String fileString = dbEncrypt.dCode(fjcfbDto.getWjlj());
							fileStrings.add(fileString);
						}
						isMergeSuccess = pdfUtil.mergePdfFiles(fileStrings, pdfWjlj);
					}
					if (!isMergeSuccess){
						log.error("PDF文件上传报告至杏和LIS系统。PDF合并失败！");
					}else {
						xingheInfoMap.put("xinghe_wsdlurl",xinghe_wsdlurl);
						xingheInfoMap.put("xinghe_newurl",xinghe_newurl);
						xingheInfoMap.put("xinghe_ak",xinghe_ak);
						xingheInfoMap.put("xinghe_sk",xinghe_sk);
						xingheInfoMap.put("xinghe_ticket",xinghe_ticket);
						xingheInfoMap.put("xinghe_hospitalservice",xinghe_hospitalservice);
						xingheInfoMap.put("fwjm",pdfWjm);
						xingheInfoMap.put("wjlj",p.eCode(pdfWjlj));
						if("hospitalXinghe".equals(matchingReportSendMap.get("lrry")))
							sjxxService.matchingUtilNewRun(xingheInfoMap,"xingheInfo");
						else
							sjxxService.matchingUtilNewRun(xingheInfoMap,"xingheInfoNew");
					}
				}
			}
			else if ("hospitalAidikang".equals(matchingReportSendMap.get("lrry"))){
				matchingReportSendMap.put("AIDIKANG_URL",AIDIKANG_URL);
				matchingReportSendMap.put("AIDIKANG_USERNAME",AIDIKANG_USERNAME);//client_id
				matchingReportSendMap.put("AIDIKANG_PASSWORD",AIDIKANG_PASSWORD);//client_secret
				matchingReportSendMap.put("AIDIKANG_BUSINESSCLASS",AIDIKANG_BUSINESSCLASS);//根据具体业务分配，通用代码为ADC_BC0000,联系技术索要编号(必填)
				sjxxService.matchingUtilNewRun(matchingReportSendMap,"aidikangInfo");
				// 等待2秒
				Thread.sleep(2000);
				Map<String,Object> matchingReportSendTwoMap = JSONObject.parseObject(str, new TypeReference<Map<String, Object>>(){});
				matchingReportSendTwoMap.put("AIDIKANG_URL",AIDIKANG_NEW_URL);
				matchingReportSendTwoMap.put("AIDIKANG_USERNAME",AIDIKANG_USERNAME);//client_id
				matchingReportSendTwoMap.put("AIDIKANG_PASSWORD",AIDIKANG_PASSWORD);//client_secret
				matchingReportSendTwoMap.put("AIDIKANG_BUSINESSCLASS",AIDIKANG_BUSINESSCLASS);//根据具体业务分配，通用代码为ADC_BC0000,联系技术索要编号(必填)
				sjxxService.matchingUtilNewRun(matchingReportSendTwoMap,"aidikangNewInfo");
				// 等待2秒
				Thread.sleep(2000);
				Map<String,Object> matchingReportSendThreeMap = JSONObject.parseObject(str, new TypeReference<Map<String, Object>>(){});
				matchingReportSendThreeMap.put("AIDIKANG_URL",AIDIKANG_NEW_URL);
				matchingReportSendThreeMap.put("AIDIKANG_USERNAME",AIDIKANG_USERNAME);//client_id
				matchingReportSendThreeMap.put("AIDIKANG_PASSWORD",AIDIKANG_PASSWORD);//client_secret
				matchingReportSendThreeMap.put("AIDIKANG_BUSINESSCLASS",AIDIKANG_BUSINESSCLASS);//根据具体业务分配，通用代码为ADC_BC0000,联系技术索要编号(必填)
				sjxxService.matchingUtilNewRun(matchingReportSendThreeMap,"aidikangNewNewInfo");
			}
			else if ("hospitalYuhuangding".equals(matchingReportSendMap.get("lrry"))){
				matchingReportSendMap.put("yuhuangding_wsdlurl",yuhuangding_wsdlurl);
				sjxxService.matchingUtilNewRun(matchingReportSendMap,"yuhuangdingInfo");
			}else if ("hospitalCDC".equals(matchingReportSendMap.get("lrry"))){
				sjxxService.matchingUtilNewRun(matchingReportSendMap,"CDCdataExchangePlatformInfo");
			}else if ("hospitalXiansheng".equals(matchingReportSendMap.get("lrry"))){
				matchingReportSendMap.put("XIANSHENG_URL",XIANSHENG_URL);
				if ("confirmById".equals(matchingReportSendMap.get("method"))){
					sjxxService.matchingUtilNewRun(matchingReportSendMap,"xianshengInfo");
				}
			}
			log.error("matchingReportSend:结束");
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.MATCHING_SEND_REPORT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive matchingReportSend:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.MATCHING_SEND_REPORT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	/**
	 * 上传第三方平台
	 *
	 * @param sjxxDto sjxx dto
	 * @param map     地图
	 * @param wjm     WJM
	 * @param fwjm    FWJM
	 * @param wjlj    WJLJ
	 * @param fwjlj   fwjlj
	 */
	private void UploadThirdPartyPlatform(SjxxDto sjxxDto, Map<String, String> map, String wjm, String fwjm, String wjlj, String fwjlj){
		String[] yyxxCskz3 = new String[]{""};
		if(StringUtil.isNotBlank(sjxxDto.getYyxxCskz3())){
			yyxxCskz3 = sjxxDto.getYyxxCskz3().split(",");
		}
		if ("hospitalXinghe".equals(sjxxDto.getLrry()) || "hospitalXingheNew".equals(sjxxDto.getLrry())){
			log.error("PDF文件开始上传报告至杏和LIS系统。");
			Map<String,Object> xingheInfoMap = new HashMap<>();
			xingheInfoMap.put("method","uploadById");
			xingheInfoMap.put("hospitalId",yyxxCskz3[0]);
			if(yyxxCskz3.length>4)
				xingheInfoMap.put("cdchospitalId",yyxxCskz3[4]);
			xingheInfoMap.put("testTeamId",yyxxCskz3[2]);
			xingheInfoMap.put("ybbh",sjxxDto.getYbbh());
			xingheInfoMap.put("hzxm",sjxxDto.getHzxm());
			xingheInfoMap.put("wbbm",sjxxDto.getWbbm());
			xingheInfoMap.put("cskz6",sjxxDto.getCskz6());
			xingheInfoMap.put("xb",sjxxDto.getXb());
			xingheInfoMap.put("yblxmc",sjxxDto.getYblxmc());
			xingheInfoMap.put("shrq",StringUtil.isNotBlank(map.get("shrq"))?map.get("shrq"):new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			xingheInfoMap.put("shry",StringUtil.isNotBlank(map.get("shry"))?map.get("shry"):"马永杰");
			xingheInfoMap.put("jyry",StringUtil.isNotBlank(map.get("jyry"))?map.get("jyry"):"刘泺");
			xingheInfoMap.put("sjid",sjxxDto.getSjid());
			xingheInfoMap.put("fwjm",fwjm);
			xingheInfoMap.put("wjlj",wjlj);
			xingheInfoMap.put("fwjlj",fwjlj);
			xingheInfoMap.put("ywlx",(String)map.get("ywlx"));
			xingheInfoMap.put("lrry",sjxxDto.getLrry());
			if ("33A001".equals(yyxxCskz3[0])){
				sjxxDto.setYwlx((String)map.get("ywlx"));
				Map<String, Object> jcjgMap = sjwzxxService.getJcjgInfoByXinghe(sjxxDto);
				jcjgMap.put("barcode",sjxxDto.getYbbh());//样本编号
				jcjgMap.put("sampleno",sjxxDto.getWbbm());//外部编码
				jcjgMap.put("patient_name", sjxxDto.getHzxm());
				List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				for (JcsjDto jcsjDto : jcxmList) {
					if (((String)map.get("ywlx")).equals("REPORT_"+jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1())){
						jcjgMap.put("order_name", jcsjDto.getCsmc());
						break;
					}
				}
				jcjgMap.put("sample_class", sjxxDto.getYblxmc());
				jcjgMap.put("report_date", sjxxDto.getReport_date());
				xingheInfoMap.put("json",JSON.toJSONString(jcjgMap));
			}
			//Qianmai、Xinghe、Aidikang、Yuhuangding、浙江省疾控、Xiansheng 等第三方平台自动上报
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(xingheInfoMap));
			log.error("PDF文件上传报告至杏和LIS系统。发送rabbitmq消息成功。");
			// 杏和-省人民样本回传疾控
			if ("33A001".equals(yyxxCskz3[0])){

				log.error("病原检测高通量测序结果-----开始上报至疾控系统-----rabbit发送开始");
				// 疾控
				xingheInfoMap.put("lrry","hospitalCDC");
				xingheInfoMap.put("hospitalName",sjxxDto.getHospitalname());
				xingheInfoMap.put("samplingTime",StringUtil.isNotBlank(sjxxDto.getCyrq())?(sjxxDto.getCyrq()+" 00:00:00"):"");
				xingheInfoMap.put("collectionTime",sjxxDto.getJsrq());
				xingheInfoMap.put("inspectTime",sjxxDto.getSyrqend());
				xingheInfoMap.put("sendDoctor",sjxxDto.getSjys());
				xingheInfoMap.put("sampleType",sjxxDto.getYblxmc());
				xingheInfoMap.put("sjxxDto",sjxxDto);
				xingheInfoMap.put("method","saveCDCdate");
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(xingheInfoMap));
				log.error("病原检测高通量测序结果-----开始上报至疾控系统-----rabbit发送结束");
			}
		}
		else if ("hospitalQianmai".equals(sjxxDto.getLrry())){
			try {
				log.error("PDF文件开始上传报告至千麦LIS系统。");
				Map<String,Object> qianmaiInfoMap = new HashMap<>();
				String ywlx = (String)map.get("ywlx");
				qianmaiInfoMap.put("method","uploadById");
				qianmaiInfoMap.put("LabCode",sjxxDto.getYyxxCskz3().split(",")[0]);
				qianmaiInfoMap.put("BarCode",sjxxDto.getYbbh());
				if (sjxxDto.getYbbh().contains("-")){
					//特殊处理700008043360-1样本，回传报告后将该代码删除
					qianmaiInfoMap.put("BarCode",sjxxDto.getYbbh().substring(0, sjxxDto.getYbbh().indexOf("-")));
				}
				String purNames = "";
				String purCodes = "";
				String cskz6 = sjxxDto.getCskz6();
				if (StringUtil.isNotBlank(cskz6)){
					String[] cskz6s = cskz6.split(",");
					if (cskz6s.length == 1){
						String[] jcxmmcdms = cskz6s[0].split(":")[0].split("@");
						purNames = jcxmmcdms[0];
						purCodes = jcxmmcdms[1];
					} else if (cskz6s.length > 1) {
						for (String jcxmpp : cskz6s) {
							if (ywlx.equals(jcxmpp.split(":")[1])){
								String[] jcxmmcdms = jcxmpp.split(":")[0].split("@");
								purNames = jcxmmcdms[0];
								purCodes = jcxmmcdms[1];
								break;
							}
						}
					}
				}
				qianmaiInfoMap.put("PurNames",purNames);
				qianmaiInfoMap.put("PurCodes",purCodes);
				qianmaiInfoMap.put("ybbh",sjxxDto.getYbbh());
				qianmaiInfoMap.put("hzxm",sjxxDto.getHzxm());
				qianmaiInfoMap.put("wbbm",sjxxDto.getWbbm());
				qianmaiInfoMap.put("sjid",sjxxDto.getSjid());
				qianmaiInfoMap.put("wjm",wjm);
				qianmaiInfoMap.put("wjlj",wjlj);
				qianmaiInfoMap.put("ywlx",(String)map.get("ywlx"));
				qianmaiInfoMap.put("lrry","hospitalQianmai");
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(qianmaiInfoMap));
				log.error("PDF文件上传报告至千麦LIS系统。发送rabbitmq消息成功。");
			} catch (Exception e) {
				log.error("PDF文件开始上传报告至千麦LIS系统。-----错误：系统对接项目可能存在问题，可能导致回传报告失败！"+e.getMessage());
			}
		}
		else if ((sjxxDto.getDb().contains("艾迪康") && StringUtil.isNotBlank(sjxxDto.getWbbm()) && !"1".equals((String)map.get("fsbs"))) || "32".equals(sjxxDto.getJcdwdm())){
			//西安实验室3为艾迪康合作的实验室，需要回传报告
			//合作伙伴包含艾迪康，且非备案报告，才调用接口传递
			log.error("PDF文件开始上传报告至艾迪康LIS系统。外部编号：" + sjxxDto.getWbbm());
			SjhbxxDto sjhbxxDto = new SjhbxxDto();
			sjhbxxDto.setHbmc(sjxxDto.getDb());
			sjhbxxDto = sjhbxxService.selectSjhb(sjhbxxDto);
			if (StringUtil.isNotBlank(sjhbxxDto.getHbdm())){
				Map<String,Object> aidikangInfoMap = new HashMap<>();
				aidikangInfoMap.put("method","uploadById");
				aidikangInfoMap.put("AIDIKANG_ORGCODE",sjhbxxDto.getHbdm());//委托机构代号,联系技术索要编号(必填)
				aidikangInfoMap.put("barcode",sjxxDto.getWbbm());//委托方条码(必填)
				aidikangInfoMap.put("patientname",sjxxDto.getHzxm());//报告患者姓名(非必填)
				String cskz3cskz1 = ((String)map.get("ywlx")).replaceAll("_REM", "");
				String extbarcode = "";
				if (cskz3cskz1.contains("IMP_REPORT_SEQ_TNGS")){
					extbarcode = "T";
				}else if ("IMP_REPORT_RFS_TEMEPLATE_F".equals(cskz3cskz1)){
					extbarcode = "F";
				}else if ("IMP_REPORT_SEQ_INDEX_TEMEPLATE_D".equals(cskz3cskz1)){
					extbarcode = "D";
				}else if ("IMP_REPORT_SEQ_INDEX_TEMEPLATE_R".equals(cskz3cskz1)){
					extbarcode = "R";
				}else if ("IMP_REPORT_SEQ_INDEX_TEMEPLATE_C".equals(cskz3cskz1)){
					extbarcode = "C";
				}
				aidikangInfoMap.put("ybbh",sjxxDto.getYbbh());
				aidikangInfoMap.put("hzxm",sjxxDto.getHzxm());
				aidikangInfoMap.put("wbbm",sjxxDto.getWbbm());
				aidikangInfoMap.put("extbarcode",extbarcode);//检测机构项目编号(必填)
				aidikangInfoMap.put("reporttime",sjxxDto.getBgrq());//报告发布时间(必填)
				aidikangInfoMap.put("testman",StringUtil.isNotBlank(map.get("jyry"))?map.get("jyry"):"刘泺");
				aidikangInfoMap.put("checkman",StringUtil.isNotBlank(map.get("shry"))?map.get("shry"):"马永杰");
				aidikangInfoMap.put("filename",wjm.substring(0,wjm.lastIndexOf(".")));
				aidikangInfoMap.put("wjlj",wjlj);
				aidikangInfoMap.put("sjid",sjxxDto.getSjid());
				aidikangInfoMap.put("ywlx",(String)map.get("ywlx"));
				aidikangInfoMap.put("lrry","hospitalAidikang");
				aidikangInfoMap.put("scbgrq",map.get("scbgrq"));
				List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				Optional<JcsjDto> optional = jcxmList.stream().filter(item -> cskz3cskz1.equals(item.getCskz3() + "_" + item.getCskz1())).findFirst();
				if (optional.isPresent()){
					Map<String, Object> xxMap = sjxxService.getMapBySjid(sjxxDto.getSjid(), optional.get().getCsid(),null);
					aidikangInfoMap.put("testResult",xxMap.get("ggzdzb") != null ? xxMap.get("ggzdzb") : "未检出");
					aidikangInfoMap.put("ResultHint",xxMap.get("jyjg") != null ? ("1".equals(xxMap.get("jyjg"))?"阳性":"阴性") : "阴性");
				}else{
					//如果检测项目里没有找到，则查找检测子项目的信息
					List<JcsjDto> jczxmList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
					Optional<JcsjDto> sub_optional = jczxmList.stream().filter(item -> cskz3cskz1.equals(item.getCskz3() + "_" + item.getCskz1())).findFirst();
					if (sub_optional.isPresent()){
						Map<String, Object> xxMap = sjxxService.getMapBySjid(sjxxDto.getSjid(), optional.get().getFcsid(),optional.get().getCsid());
						aidikangInfoMap.put("testResult",xxMap.get("ggzdzb") != null ? xxMap.get("ggzdzb") : "未检出");
						aidikangInfoMap.put("ResultHint",xxMap.get("jyjg") != null ? ("1".equals(xxMap.get("jyjg"))?"阳性":"阴性") : "阴性");
					}
				}
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(aidikangInfoMap));
				log.error("PDF文件上传报告至艾迪康LIS系统。发送rabbitmq消息成功。");
			}
			else {
				log.error("PDF文件上传报告至艾迪康LIS系统。发送rabbitmq消息失败。-----未配置伙伴代码-----"+sjxxDto.getDb());
			}
		}else if("hospitalHenuo2".equals(sjxxDto.getLrry())){
			try {
				log.error("PDF文件开始上传报告至禾诺LIS系统。");
				Map<String,Object> henuoInfoMap = new HashMap<>();
				String ywlx = (String)map.get("ywlx");
				henuoInfoMap.put("method","uploadById");
				henuoInfoMap.put("BarCode",sjxxDto.getYbbh());
				henuoInfoMap.put("ybbh",sjxxDto.getYbbh());
				henuoInfoMap.put("hzxm",sjxxDto.getHzxm());
				henuoInfoMap.put("wbbm",sjxxDto.getWbbm());
				henuoInfoMap.put("sjid",sjxxDto.getSjid());
				henuoInfoMap.put("wjm",wjm);
				henuoInfoMap.put("wjlj",wjlj);
				henuoInfoMap.put("ywlx",(String)map.get("ywlx"));
				henuoInfoMap.put("lrry","hospitalHenuo2");
				XtszDto xtszDto = xtszService.selectById("henuo.portconfig");
				if(xtszDto!=null){
					henuoInfoMap.put("HENUO_PORTCONFIG",xtszDto.getSzz());
				}
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(henuoInfoMap));
				log.error("PDF文件上传报告至禾诺LIS系统。发送rabbitmq消息成功。");
			} catch (Exception e) {
				log.error("PDF文件开始上传报告至禾诺LIS系统。-----错误：系统对接项目可能存在问题，可能导致回传报告失败！"+e.getMessage());
			}
		}


		if ("济南艾迪康毓璜顶定制".equals(sjxxDto.getDb()) && StringUtil.isNotBlank(sjxxDto.getWbbm()) && !"1".equals((String)map.get("fsbs")) && !sjxxDto.getYbbh().startsWith("4Z") ){
			try {

				Map<String,Object> yuhuangdingInfoMap = new HashMap<>();
				yuhuangdingInfoMap.put("ybbh",sjxxDto.getYbbh());
				yuhuangdingInfoMap.put("hzxm",sjxxDto.getHzxm());
				yuhuangdingInfoMap.put("wbbm",sjxxDto.getWbbm());
				yuhuangdingInfoMap.put("sjid",sjxxDto.getSjid());
				yuhuangdingInfoMap.put("method","uploadById");
				yuhuangdingInfoMap.put("wjm",wjm);
				yuhuangdingInfoMap.put("wjlj",wjlj);
				yuhuangdingInfoMap.put("shryzsxm",map.get("shryzsxm"));
				yuhuangdingInfoMap.put("jyryzsxm",map.get("jyryzsxm"));
				yuhuangdingInfoMap.put("lrry","hospitalYuhuangding");
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(yuhuangdingInfoMap));
				log.error("PDF文件开始上传报告至毓璜顶LIS系统。样本编号为：" + sjxxDto.getYbbh());
			} catch (Exception e) {
				log.error("PDF文件开始上传报告至毓璜顶LIS系统。错误样本编号为：" + sjxxDto.getYbbh() +" -----错误：系统对接项目可能存在问题，可能导致回传报告失败！"+e.getMessage());
			}

		}

		//艾迪康上报疾控
		if(sjxxDto.getDb().contains("艾迪康") && yyxxCskz3.length>5 &&"1".equals(yyxxCskz3[5])){
			Map<String,Object> adkInfoMap = new HashMap<>();
			adkInfoMap.put("hospitalId",yyxxCskz3[4]);
			adkInfoMap.put("cdchospitalId",yyxxCskz3[4]);
			adkInfoMap.put("ybbh",sjxxDto.getYbbh());
			adkInfoMap.put("hzxm",sjxxDto.getHzxm());
			adkInfoMap.put("wbbm",sjxxDto.getWbbm());
			adkInfoMap.put("cskz6",sjxxDto.getCskz6());
			adkInfoMap.put("xb",sjxxDto.getXb());
			adkInfoMap.put("yblxmc",sjxxDto.getYblxmc());
			adkInfoMap.put("shrq",StringUtil.isNotBlank(map.get("shrq"))?map.get("shrq"):new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			adkInfoMap.put("shry",StringUtil.isNotBlank(map.get("shry"))?map.get("shry"):"马永杰");
			adkInfoMap.put("jyry",StringUtil.isNotBlank(map.get("jyry"))?map.get("jyry"):"刘泺");
			adkInfoMap.put("sjid",sjxxDto.getSjid());
			adkInfoMap.put("fwjm",fwjm);
			adkInfoMap.put("wjlj",wjlj);
			adkInfoMap.put("fwjlj",fwjlj);
			adkInfoMap.put("ywlx",(String)map.get("ywlx"));
			adkInfoMap.put("lrry",sjxxDto.getLrry());
			sjxxDto.setYwlx((String)map.get("ywlx"));
			Map<String, Object> jcjgMap = sjwzxxService.getJcjgInfoByXinghe(sjxxDto);
			jcjgMap.put("barcode",sjxxDto.getYbbh());//样本编号
			jcjgMap.put("sampleno",sjxxDto.getWbbm());//外部编码
			jcjgMap.put("patient_name", sjxxDto.getHzxm());
			List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			for (JcsjDto jcsjDto : jcxmList) {
				if (((String)map.get("ywlx")).equals("REPORT_"+jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1())){
					jcjgMap.put("order_name", jcsjDto.getCsmc());
					break;
				}
			}
			jcjgMap.put("sample_class", sjxxDto.getYblxmc());
			jcjgMap.put("report_date", sjxxDto.getReport_date());
			adkInfoMap.put("json",JSON.toJSONString(jcjgMap));
			log.error("艾迪康-----开始上报至疾控系统-----rabbit发送开始");
			adkInfoMap.put("lrry","hospitalCDC");
			adkInfoMap.put("hospitalName",sjxxDto.getHospitalname());
			adkInfoMap.put("samplingTime",StringUtil.isNotBlank(sjxxDto.getCyrq())?(sjxxDto.getCyrq()+" 00:00:00"):"");
			adkInfoMap.put("collectionTime",sjxxDto.getJsrq());
			adkInfoMap.put("inspectTime",sjxxDto.getSyrqend());
			adkInfoMap.put("sendDoctor",sjxxDto.getSjys());
			adkInfoMap.put("sampleType",sjxxDto.getYblxmc());
			adkInfoMap.put("sjxxDto",sjxxDto);
			adkInfoMap.put("method","saveCDCdate");
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MATCHING_SEND_REPORT.getCode(),JSONObject.toJSONString(adkInfoMap));
			log.error("艾迪康-----开始上报至疾控系统-----rabbit发送结束");
		}
	}

	/**
	 * 文件管理接收转换后的pdf,如果为送检信息的word文档，则新增一条pdf的附件信息，同时传递到阿里服务器上，并更新最后转换信息
	 * 否则只是更新最后转换信息
	 * @param str
	 */
	@RabbitListener(queues = ("${spring.rabbitmq.docok:mq.tran.basic.ok}"), containerFactory="defaultFactory")
	public void DocChangePdf(String str) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>)JSON.parse(str);
		DBEncrypt p = new DBEncrypt(); 
		String fwjlj=p.dCode((String) map.get("fwjlj"));
		
		fwjlj=fwjlj.replace("_WORD", "");
		mkDirs(fwjlj);
		String fileName=(String) map.get("pdfName");
		//log.error("获取转换后PDF文件信息：spring.rabbitmq.docok:" +fileName+"	Str:"+str);
		//连接服务器
		try{
			//docker无法连接FTP，因为
			//FTPClient ftp=FtpUtil.connect(FTPPDF_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
			//boolean result=FtpUtil.download(ftp, fileName, fwjlj+"/"+fileName);
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("fileName", fileName);
			
			boolean downflg = downloadPdfFile(paramMap,fwjlj+"/"+fileName);

			if(downflg){
				String zhwjxx=p.eCode(fwjlj+"/"+fileName);
				FjcfbModel fjcfbModel=new FjcfbModel();
				FjcfbModel t_fjcfbModel = null;
				if(StringUtil.isNotBlank((String)map.get("fjid"))){
					fjcfbModel.setFjid((String)map.get("fjid"));
					fjcfbModel.setYwlx((String)map.get("ywlx"));
					fjcfbModel.setZhwjxx(zhwjxx);
					fjcfbService.updateZhwjxx(fjcfbModel);
					t_fjcfbModel = fjcfbService.getModel(fjcfbModel);

					if(t_fjcfbModel.getYwlx().indexOf("_WORD")!=-1){//当业务类型不包含WORD时，不执行清除原PDF文件
						FjcfbDto fjcfbDto_t=new FjcfbDto();
						fjcfbDto_t.setYwlx((String)map.get("ywlx").replace("_WORD", ""));
						fjcfbDto_t.setYwid(t_fjcfbModel.getYwid());
						fjcfbService.updateScbjYwidAndYwlxd(fjcfbDto_t);
					}
				}
//				else {
					//当为新冠报告时
//					if(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode().equals((String)map.get("ywlx"))){
//						t_fjcfbModel = new FjcfbModel();
//						//新增PDF附件信息
//						t_fjcfbModel.setFjid(StringUtil.generateUUID());
//						t_fjcfbModel.setYwid((String)map.get("ywid"));
//						t_fjcfbModel.setXh("1");
//						t_fjcfbModel.setZhbj("0");
//						String t_wjm = (String)map.get("pdfName");
//						String pdfwjm = (String)map.get("wjm");
//						String t_fwjlj =  p.dCode((String)map.get("fwjlj"));
//
//						t_fjcfbModel.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
//
//						t_fjcfbModel.setWjm(pdfwjm);
//						t_fjcfbModel.setWjlj(p.eCode(t_fwjlj + "/" + t_wjm));
//						t_fjcfbModel.setFwjm(p.eCode(t_wjm));
//						t_fjcfbModel.setFwjlj(p.eCode(t_fwjlj));
//						//删除原有PDF再新增
//						fjcfbService.deleteByYwidAndYwlx(t_fjcfbModel);
//						fjcfbService.insert(t_fjcfbModel);
//						//========================阿里健康回传报告开始===============================
//						FzjcxxDto fzjcxxDto=new FzjcxxDto();
//						fzjcxxDto.setFzjcid(t_fjcfbModel.getYwid());
//						FzjcxxDto dto = fzjcxxService.getDto(fzjcxxDto);
//						if(dto!=null){
//							if("阿里健康".equals(dto.getPt())){
//								log.error("阿里健康回传报告开始.");
//								String zjh="";
//								String sj="";
//								if("1".equals(dto.getZjlxdm())){
//									DBEncrypt dbEncrypt=new DBEncrypt();
//									if(StringUtil.isNotBlank(dto.getZjh())){
//										zjh=dbEncrypt.dCode(dto.getZjh());
//									}
//									if(StringUtil.isNotBlank(dto.getSj())){
//										sj=dbEncrypt.dCode(dto.getSj());
//									}
//								}
//								String url=applicationurl+"/ws/file/downloadFile?fjid="+t_fjcfbModel.getFjid();
//								AliHealthReturnReport aliHealthReturnReport = new AliHealthReturnReport();
//								aliHealthReturnReport.init(dto.getFzjcid(),t_fjcfbModel.getFjid(),t_fjcfbModel.getWjm(),zjh,sj,url);
//								AliHealthReportThread aliHealthReportThread = new AliHealthReportThread(aliHealthReturnReport);
//								aliHealthReportThread.start();
//							}
//						}
//
//						//=========================阿里健康回传报告=================================
//						t_fjcfbModel.setYswjm(t_fjcfbModel.getWjm());
//						t_fjcfbModel.setYswjlj(t_fjcfbModel.getWjlj());
//						t_fjcfbModel.setShrs((String) map.get("shrList"));
//						t_fjcfbModel.setShsjs((String) map.get("shsjList"));
//						t_fjcfbModel.setShyhms((String) map.get("yhmList"));
//						String signflg = (String) map.get("signflg");
//						String gzlx=(String) map.get("gzlx");
//						//判断是否盖章
//						if(StringUtils.isNotBlank(gzlx))
//							AttachHelper.addChapter(t_fjcfbModel,gzlx);
//						//若为审核时附加电子签名
////						if(StringUtils.isNotBlank((String) map.get("signflg")))
////							attachHelper.addWatermarkBisync(t_fjcfbModel, signflg, (String) map.get("sxrq"),null);
//
//						boolean isSend = sjxxService.sendFileToAli(t_fjcfbModel);
//						if (!isSend){
//							throw new Exception("PDF文件信息未正常发送到阿里云上！！");
//						}
//						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						Date date=new Date();
//						String bgwcsj=sdf.format(date);
//						fzjcxxDto.setBgwcsj(bgwcsj);
//						fzjcxxService.updateBgwcsj(fzjcxxDto);
//						//发送rabbit
//						amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.BGWC_UPD.getCode()+ JSONObject.toJSONString(fzjcxxDto));
//
//						log.error("PDF文件已经发送到阿里云上。");
//						log.error("PDF文件开始发送消息通知。");
//						//获取发送信息的内容以及发送的人员
//						JSONArray jcxmList = JSON.parseArray(map.get("jcxmList"));
//						RestTemplate t_restTemplate = new RestTemplate();
//						for (Object jcxm : jcxmList) {
//							Map<String,Object> msgMap = new HashMap<>();
//							msgMap=JSON.parseObject(JSONObject.toJSONString(jcxm),Map.class);
//							//发送微信消息、短信
//							log.error("-- sendDetectionMessage -- 开始发送消息。 ");
//							fzjcxxService.sendDetectionMessage(msgMap);
//							log.error("-- sendDetectionMessage -- 结束发送短信。 ");
//						}
//						log.error("PDF文件完成消息通知。");
//						t_fjcfbModel = null;
//					}
//				}
				if(t_fjcfbModel != null){
					log.error("PDF文件存在的情况下处理.");
					String wjm = updateSuffix(t_fjcfbModel.getWjm());
					String wjlj = p.eCode(updateSuffix(p.dCode(t_fjcfbModel.getWjlj()).replace("_WORD", "")));
					String fwjm = p.eCode(updateSuffix(p.dCode(t_fjcfbModel.getFwjm()).replace("_WORD", "")));
					String newfwjlj = p.eCode(p.dCode(t_fjcfbModel.getFwjlj()).replace("_WORD", ""));
					//判断业务类型,新增pdf附件信息,发消息sjid-sjxxDto
					if(judgmentType(t_fjcfbModel.getYwlx()) || t_fjcfbModel.getYwlx().startsWith("REPORT_")){
						t_fjcfbModel.setFjid(StringUtil.generateUUID());
						t_fjcfbModel.setYwlx((String)map.get("ywlx").replace("_WORD", ""));
						t_fjcfbModel.setWjm(wjm);
						t_fjcfbModel.setWjlj(wjlj);
						t_fjcfbModel.setFwjm(fwjm);
						t_fjcfbModel.setFwjlj(newfwjlj);
						fjcfbService.insert(t_fjcfbModel);
						//判断pdf盖章，报告模板
//						List<SjxxDto> sjxxDtos = sjxxService.selectBgmbBySjid(t_fjcfbModel.getYwid());
//						if(sjxxDtos != null && sjxxDtos.size() > 0 &&sjxxDtos.get(0)!=null){
							/* 注释固定写法(固定原因不明)，直接根据gzlxmc判断是否盖章   2020-9-21 lishangyuan
							if(StringUtil.isNotBlank(sjxxDtos.get(0).getCskz3()) && "B31E9D01BD934E218752B3C9ABF017DB".equals(sjxxDtos.get(0).getCskz3())){
							  if(StringUtil.isNotBlank(sjxxDtos.get(0).getBgmbmc()) && sjxxDtos.get(0).getBgmbmc().equals("杰毅医诺模板")){
							    AttachHelper.addChapter(t_fjcfbModel);
							  }else if(StringUtil.isNotBlank((String) map.get("gzlxmc"))){
							    AttachHelper.addChapter(t_fjcfbModel,(String) map.get("gzlxmc"));
							  }
							}else if(StringUtil.isNotBlank((String) map.get("gzlxmc"))){
							  AttachHelper.addChapter(t_fjcfbModel,(String) map.get("gzlxmc"));
							}*/
							//判断是否添加水印
							// 1:加水印
//							
							String watermark= map.get("watermark");
							if(StringUtil.isNotBlank(watermark)&&"1".equals(watermark)) {
								//根据文档类型查询水印信息
								String wjlb = t_fjcfbModel.getYwlx();
								if (wjlb.startsWith("REPORT_"))
									wjlb = "IMP_REPORT_TWO";
								SyxxDto syxxDto=syxxService.getDtoByWjlb(wjlb);
								//查询文件地址
								DBEncrypt dbEncrypt=new DBEncrypt();
								String pdfFilePath=dbEncrypt.dCode(t_fjcfbModel.getWjlj());
								if(syxxDto!=null) {
									boolean isSuccess = WatermarkUtil.addWatermark(new File(pdfFilePath),syxxDto);
									if(!isSuccess){
										log.error("送检报告添加水印失败！");
									}
								}else {
									log.error("水印信息为空！");
								}
								
							}
							// 根据gzlxmc判断是否盖章
							if(StringUtil.isNotBlank((String) map.get("gzlxmc"))){
								AttachHelper.addChapter(t_fjcfbModel,(String) map.get("gzlxmc"));
							}
//						}
						//log.error("PDF文件开始发送到阿里云上1。");
						//同步pdf至微信服务器 
						boolean isSend = sjxxService.sendFileToAli(t_fjcfbModel);
						if(!isSend)
							throw new Exception("PDF文件信息未正常发送到阿里云上！！");
						log.error("PDF文件已经发送到阿里云上1。");
						SjxxDto sjxxDto = new SjxxDto();
						sjxxDto.setSjid(t_fjcfbModel.getYwid());
						sjxxDto = sjxxService.getDto(sjxxDto);
						//发送消息通知
						sjxxDto.setWordlx((String)map.get("ywlx") + "_WORD");
						sjxxDto.setPdflx((String)map.get("ywlx"));
						//log.error("PDF文件开始发送消息通知。");
						boolean bol = commonService.queryAuthMessage(sjxxDto.getHbid(),"INFORM_HB00007");
						if(map.get("sendmail")!=null && StringUtil.isNotBlank(map.get("sendmail").toString())){
							sjxxDto.setSendmail(map.get("sendmail").toString());
						}
						if(map.get("sendFlag")!=null && StringUtil.isNotBlank(map.get("sendFlag").toString())){
							sjxxDto.setSendFlag(map.get("sendFlag").toString());
						}
						if(bol){
							//发送标识为1 时代表发送邮箱2
							if ("1".equals((String)map.get("fsbs"))){
								sjxxService.sendReportOnlyToYx(sjxxDto);
							}else{
								sjxxService.sendReportMessage(sjxxDto);
							}
						}
						//log.error("PDF文件完成消息通知。");
						// 上传结果第三方平台、杏和，千麦、艾迪康、济南艾迪康毓璜顶定制
						UploadThirdPartyPlatform(sjxxDto, map, wjm, fwjm, wjlj, fwjlj);
						//修改上传标记
						SjxxDto t_sjxxDto = new SjxxDto();
						t_sjxxDto.setSjid(t_fjcfbModel.getYwid());
						t_sjxxDto.setSfsc("1");
						int result = sjxxDao.updateReport(t_sjxxDto);
						if(result > 0){
							RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(t_sjxxDto));
						}
 						sjxxService.checkSybgSfwc();
						//判断是否发送rabbit消息。 此处用于发送信息到主rabbit(101服务器)，然后主动调用 matridx_wbaqyz 的 address接口，往对方服务器发送报告信息。
						if(StringUtil.isNotBlank(sjxxDto.getHbid())) { //如果送检伙伴不为空，查询伙伴权限
							List<WbaqyzDto> wbaqyzDtos = wbaqyzService.queryByHbid(sjxxDto.getHbid());
							if(!CollectionUtils.isEmpty(wbaqyzDtos)) {
								WbaqyzDto wbaqyzDto = wbaqyzDtos.get(0);
								Map<String, String> StringMap = new HashMap<>();
								//根据报告确定检测项目
								String ywlx=(String)map.get("ywlx");
								int index = ywlx.lastIndexOf("_");
								String cskz3=ywlx.substring(0,index);
								String cskz1=ywlx.substring(index+1);
								JcsjDto jcxm_jcsjDto=new JcsjDto();
								List<JcsjDto> jcxmList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_TYPE.getCode());
								if(jcxmList!=null&&jcxmList.size()>0){
									for(JcsjDto jcxm:jcxmList){
										if(cskz1.equals(jcxm.getCskz1()) && cskz3.equals(jcxm.getCskz3())){
											jcxm_jcsjDto=jcxm;
										}
									}
								}
								StringMap.put("Sjid", sjxxDto.getSjid()); //送检id
								StringMap.put("Cwcs", "0"); //错误次数
								StringMap.put("Hbid", wbaqyzDto.getHbid()); //伙伴id
								StringMap.put("Csid", jcxm_jcsjDto.getCsid()); //检测项目
								StringMap.put("Word", wbaqyzDto.getWord()); //是否为word,有值代表为word，无值为pdf
								StringMap.put("Address", wbaqyzDto.getAddress()); //地址
								Map<String, String> messageMap = new HashMap<>();
								messageMap.put("method", "wbhbyzServiceImpl");
								messageMap.put("parameters", JSONObject.toJSONString(StringMap));
								//log.error("参数parameters"+JSONObject.toJSONString(StringMap));
								//log.error("rabbit传递参数"+JSONObject.toJSONString(messageMap));
								amqpTempl.convertAndSend("wechar.report.outside.send.exchange", "wechar.report.outside.send.queue", JSONObject.toJSONString(messageMap));
							}							
						}
						// 此处设计为发送信息到9002的rabbit，然后由对方接收rabbit信息，根据VirtualHost进行用户切分。对方根据rabbit里的url主动来获取报告文件
						YyxxDto yyxxDto = yyxxService.getYyxxBySjid(sjxxDto.getSjid());
						if (yyxxDto!=null && StringUtil.isNotBlank(yyxxDto.getVirtualhost())){
							//发送rabbit到本地C#程序自动下载报告
							Map<String, Object> localDownReportMap = new HashMap<>();
							localDownReportMap.put("fjid",t_fjcfbModel.getFjid());
							localDownReportMap.put("ywid",t_fjcfbModel.getYwid());
							localDownReportMap.put("ywlx",t_fjcfbModel.getYwlx());
							localDownReportMap.put("fileName",wjm);
							localDownReportMap.put("VirtualHost",yyxxDto.getVirtualhost());
							SjkzxxDto sjkzxxDto = sjkzxxService.getDtoById(t_fjcfbModel.getYwid());
							if(sjkzxxDto!=null && StringUtil.isNotBlank(sjkzxxDto.getQtxx())){
								Map<String,Object> qtxxMap = JSON.parseObject(sjkzxxDto.getQtxx(),new TypeReference<Map<String, Object>>(){});
								if(!CollectionUtils.isEmpty(qtxxMap)){
									localDownReportMap.putAll(qtxxMap);
								}
							}
							SjbgsmDto sjbgsmDto = new SjbgsmDto();
							sjbgsmDto.setSjid(t_fjcfbModel.getYwid());
							sjbgsmDto.setJclx(map.get("jclxid"));
							List<SjbgsmDto> sjbgsmDtos = sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
							if(!CollectionUtils.isEmpty(sjbgsmDtos)){
								localDownReportMap.put("jyry",sjbgsmDtos.get(0).getJyry());
								localDownReportMap.put("shry",sjbgsmDtos.get(0).getShry());
							}
							List<JcsjDto> jcxmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
							JcsjDto jcxm = new JcsjDto();
							for (JcsjDto jcsjDto : jcxmlist) {
								if ((jcsjDto.getCskz3() + "_" +jcsjDto.getCskz1()).equals(t_fjcfbModel.getYwlx())){
									jcxm = jcsjDto;
									break;
								}
							}
							WbsjxxDto wbsjxxDto = new WbsjxxDto();
							wbsjxxDto.setSjid(sjxxDto.getSjid());
							wbsjxxDto.setJcxmid(jcxm.getCsid());
							WbsjxxDto wbsjxx = wbsjxxService.getDtoBySjidAndJcxm(wbsjxxDto);
							if (wbsjxx!=null && StringUtil.isNotBlank(wbsjxx.getSjbm())){
								localDownReportMap.put("ybbh",wbsjxx.getSjbm());
							}else {
								localDownReportMap.put("ybbh",sjxxDto.getWbbm());
							}

							if(localDownReportMap.get("ybbh")!=null && StringUtil.isNotBlank(localDownReportMap.get("ybbh").toString())) {
								boolean isSendMsg = true;
								String url = "/ws/common/downloadDocFilePro?ywid="+sjxxDto.getSjid()+"&fjid="+t_fjcfbModel.getFjid();
								if (StringUtil.isNotBlank(sjxxDto.getDb()) && sjxxDto.getDb().contains("北大一入院")){
									url += "&isNeedMerge=false";
									if ("IMP_REPORT_SEQ_INDEX_TEMEPLATE_O".equals((String)map.get("ywlx"))){
										isSendMsg = false;
									}
								}

								SjwzxxDto sjwzxxDto=new SjwzxxDto();

								sjwzxxDto.setSjid(sjxxDto.getSjid());
								sjwzxxDto.setJclx(map.get("jclxid"));
								List<String> ids=new ArrayList<>();
								ids.add("background");
								ids.add("possible");
								sjwzxxDto.setIds(ids);
								List<SjwzxxDto> sjwzxxDtoList=sjwzxxService.getJcjgByJglx(sjwzxxDto);
								//物种是否检出逻辑
								XxdyDto xxdyDto=new XxdyDto();
								xxdyDto.setDylxcsdm("RYDYWZ");
								//获取对应信息
								List<XxdyDto>xxdyDtoList=xxdyService.getDtoList(xxdyDto);
								String db=sjxxDto.getDb();
								//sjxx
								SjxxDto paramDto=new SjxxDto();
								paramDto.setSjid(t_fjcfbModel.getYwid());
								SjxxDto sjxxDto_yh=sjxxService.getSjxxDto(paramDto);
								localDownReportMap.put("hzxm",sjxxDto_yh.getHzxm());
								//jglx
								List<JcsjDto>jcsjDtoList=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE.getCode());
								Optional<JcsjDto> optional_jclx=jcsjDtoList.stream().filter(x->x.getCsid().equals(map.get("jclxid"))).findFirst();
								String jclx_pj="";
								if(optional_jclx.isPresent()){
									localDownReportMap.put("jglx",optional_jclx.get().getCsmc());
									jclx_pj=optional_jclx.get().getCskz3()+"_"+map.get("jclx");
									try {
										XxdyDto xxdyDto_pj = new XxdyDto();
										xxdyDto_pj.setDylxcsdm("JCXMPP");
										xxdyDto_pj.setDyxx(optional_jclx.get().getCsmc());
										xxdyDto_pj.setYxx(sjxxDto.getDb());
										String dydm = xxdyService.getByDylxcsdmAndDyxxAndYxx(xxdyDto_pj);
										if (StringUtil.isNotBlank(dydm)) {
											localDownReportMap.put("dydm", dydm);
										}
									} catch (Exception e) {
										log.error("getByDylxcsdmAndDyxxAndYxx 出现错误：" + e.getMessage());
									}
								}


								//获取该对应伙伴得对应信息
								List<XxdyDto> dbXxDyList=xxdyDtoList.stream().filter(x->x.getYxx().equals(db)).toList();
								//耐药得结果
								List<Map<String,String>> nyList=new ArrayList<>();
								//物种类型是否检出
								Map<String,String> sfjcwzMap=new HashMap<>();
								//物种是否检出
								Map<String,String> sfjcMap=new HashMap<>();
								Map<String,String> sfjcMap_t=new HashMap<>();
								List<JcsjDto>jcsjist_rynyjy=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.RYNYJY_TYPE.getCode());
								Optional<JcsjDto> ryny_db=jcsjist_rynyjy.stream().filter(x->x.getCsmc().equals(db)).findFirst();
								List<SjwzxxDto>  possibleList=sjwzxxDtoList.stream().filter(x->x.getJglx().equals("possible")).toList();
								if(!CollectionUtils.isEmpty(dbXxDyList)){
									List<SjwzxxDto> sjnyxDtoList=sjnyxService.getNyxMapBySjid(sjxxDto);
									for(SjwzxxDto sjwzxxDto_t:sjnyxDtoList){
										Map<String,String> nyMap=new HashMap<>();
										nyMap.put("jy",sjwzxxDto_t.getJy());
										nyMap.put("xgwz",sjwzxxDto_t.getXgwz());
										nyMap.put("yp",sjwzxxDto_t.getYp());
										nyMap.put("jyfx",sjwzxxDto_t.getJyfx());
										nyList.add(nyMap);
									}
									for(SjwzxxDto sjwzxxDto_t:possibleList){
										Optional<XxdyDto> optional=xxdyDtoList.stream().filter(x->x.getDyxx().equals(sjwzxxDto_t.getWzid())).findFirst();
										if (optional.isPresent()){
											sfjcMap.put(sjwzxxDto_t.getWzzwm(),"阳性");
											if("Bacteria".equals(sjwzxxDto_t.getWzfl())){
												sfjcwzMap.put("细菌","检出");

											}
											if("MCR".equals(sjwzxxDto_t.getWzfl())){
												sfjcwzMap.put("支原体/衣原体/立克次体","检出");
											}
											if("Fungi".equals(sjwzxxDto_t.getWzfl())) {
												sfjcwzMap.put("真菌","检出");
											}
											if("Viruses".equals(sjwzxxDto_t.getWzfl())) {
												sfjcwzMap.put("病毒","检出");
											}
											if("Parasite".equals(sjwzxxDto_t.getWzfl())) {
												sfjcwzMap.put("寄生虫","检出");
											}
											if("Mycobacteria".equals(sjwzxxDto_t.getWzfl())) {
												sfjcMap_t.put("结核分枝杆菌","检出");
											}
										}
									}
									localDownReportMap.put("wzsfjc",sfjcMap);
									//定制化基因

									if (ryny_db.isPresent()){
										if(StringUtil.isNotBlank(ryny_db.get().getCskz1())){
											JSONObject jsonObject=JSON.parseObject(ryny_db.get().getCskz1());
											JSONObject jsonObject_jcxm=jsonObject.getJSONObject(jclx_pj);
											if(jsonObject_jcxm!=null){
												Set<String> set=sfjcMap_t.keySet();
												Map<String,Object>wzjyMap=new HashMap<>();
												if(jsonObject_jcxm.get("结核分枝杆菌")==null){
														if(sfjcMap_t.get("结核分枝杆菌")!=null){
															sfjcMap_t.put("细菌","检出");
															sfjcMap_t.remove("结核分枝杆菌");
														}
												}
												for(String key:set){
													if(jsonObject_jcxm.get(key)!=null){
														String value=jsonObject_jcxm.get(key).toString();
														String[] splitValue=value.split(",");
														Map<String,String> valueMap=new HashMap<>();
														for(String valuestr:splitValue){
															valueMap.put(valuestr,"检出");
														}
														wzjyMap.put(key,valueMap);
													}
												}
												localDownReportMap.put("wzjy_dzh",wzjyMap);
											}
										}
									}
									localDownReportMap.put("nyList",nyList);
									localDownReportMap.put("wzlxsfjc",sfjcwzMap);
								}
								for(SjwzxxDto sjwzxxDto_t:possibleList){
									if("Bacteria".equals(sjwzxxDto_t.getWzfl())){
										sfjcMap_t.put("细菌","检出");
									}
									if("MCR".equals(sjwzxxDto_t.getWzfl())){
										sfjcMap_t.put("支原体/衣原体/立克次体","检出");
									}
									if("Fungi".equals(sjwzxxDto_t.getWzfl())) {
										sfjcMap_t.put("真菌","检出");
										sfjcMap_t.put("真菌/寄生虫","检出");
									}
									if("Viruses".equals(sjwzxxDto_t.getWzfl())) {
										sfjcMap_t.put("病毒","检出");
									}
									if("Parasite".equals(sjwzxxDto_t.getWzfl())) {
										sfjcMap_t.put("寄生虫","检出");
										sfjcMap_t.put("真菌/寄生虫","检出");
									}
									if("Mycobacteria".equals(sjwzxxDto_t.getWzfl())) {
										sfjcMap_t.put("结核分枝杆菌","检出");
									}
								}
								if (ryny_db.isPresent()){
									if(StringUtil.isNotBlank(ryny_db.get().getCskz1())){
										JSONObject jsonObject=JSON.parseObject(ryny_db.get().getCskz1());
										JSONObject jsonObject_jcxm=jsonObject.getJSONObject(jclx_pj);
										if(jsonObject_jcxm!=null){
											Set<String> set=sfjcMap_t.keySet();
											Map<String,Object> wzjyMap=new HashMap<>();
											if(jsonObject_jcxm.get("结核分枝杆菌")==null){
												if(sfjcMap_t.get("结核分枝杆菌")!=null){
													sfjcMap_t.put("细菌","检出");
													sfjcMap_t.remove("结核分枝杆菌");
												}
											}
											for(String key:set){
												if(jsonObject_jcxm.get(key)!=null){
													String value=jsonObject_jcxm.get(key).toString();
													String[] splitValue=value.split(",");
													Map<String,String> valueMap=new HashMap<>();
													for(String valuestr:splitValue){
														valueMap.put(valuestr,"检出");
													}
													wzjyMap.put(key,valueMap);
												}
											}

											localDownReportMap.put("wzjy",wzjyMap);
										}
									}

								}
								//关注微生物

								String gzStr="";
								for(SjwzxxDto sjwzxxDto1:possibleList){
									if(gzStr.equals("")){
										gzStr+=sjwzxxDto1.getWzzwm();
									}else{
										gzStr+=","+sjwzxxDto1.getWzzwm();
									}
								}
								localDownReportMap.put("gzwsw",gzStr);

								//疑似或人体共生
								List<SjwzxxDto>  backgroundList=sjwzxxDtoList.stream().filter(x->x.getJglx().equals("background")).toList();
								String ysStr="";
								for(SjwzxxDto sjwzxxDto1:backgroundList){
									if(ysStr.equals("")){
										ysStr+=sjwzxxDto1.getWzzwm();
									}else{
										ysStr+=","+sjwzxxDto1.getWzzwm();
									}
								}
								localDownReportMap.put("ysrtgs",ysStr);

								localDownReportMap.put("url",applicationurl+url);
								localDownReportMap.put("path",url);
								localDownReportMap.put("fwjm",t_fjcfbModel.getFwjm());
								List<JcsjDto> xxdyList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
								if (isSendMsg){
									boolean isSuccess = extendAmqpUtils.sendDownloadReportMsg(localDownReportMap);
									if (!isSuccess){
										log.error("sendDownloadReportMsg 发送消息失败：{}",JSON.toJSONString(localDownReportMap));
									}
								}else {
									log.error("sendDownloadReportMsg 发送消息禁用：{}",JSON.toJSONString(localDownReportMap));
								}
							}else {
								log.error("sendDownloadReportMsg 因为没有医院编码，禁止发送：{}",JSON.toJSONString(localDownReportMap));
							}
						}
					}else if(BusTypeEnum.IMP_DOCUMENT.getCode().equals(t_fjcfbModel.getYwlx())){
						t_fjcfbModel.setYswjm(t_fjcfbModel.getWjm());
						t_fjcfbModel.setYswjlj(t_fjcfbModel.getWjlj());
						t_fjcfbModel.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
						t_fjcfbModel.setWjm(wjm);
						t_fjcfbModel.setWjlj(wjlj);
						t_fjcfbModel.setFwjm(fwjm);
						t_fjcfbModel.setFwjlj(newfwjlj);
						t_fjcfbModel.setShrs((String) map.get("shrList"));
						t_fjcfbModel.setShsjs((String) map.get("shsjList"));
						t_fjcfbModel.setShyhms((String) map.get("yhmList"));
						String signflg = (String) map.get("signflg");
						if("0".equals(signflg) || "1".equals(signflg)){
							//0:加签名 1:加水印
							
							//根据文档类型查询水印信息
							String wjlb = t_fjcfbModel.getYwlx();
							if (wjlb.startsWith("REPORT_"))
								wjlb = "IMP_REPORT_TWO";
							SyxxDto syxxDto=syxxService.getDtoByWjlb(wjlb);
							if(syxxDto!=null) {
								attachHelper.addWatermark(t_fjcfbModel, signflg, (String) map.get("sxrq"),syxxDto);
							}else {
								log.error("水印信息不存在！");
							}
							
						}
						// replaceflg 为 1 表示转换，0 表示不转换
						String replaceflg = (String) map.get("replaceflg");
						if(StringUtil.isNotBlank(replaceflg) && replaceflg.equals("1")){
							fjcfbService.replaceFile(t_fjcfbModel);
						}
					}else if(BusTypeEnum.IMP_CONTRACT_WORD.getCode().equals(t_fjcfbModel.getYwlx())){
						t_fjcfbModel.setFjid(StringUtil.generateUUID());
						t_fjcfbModel.setYwlx((String)map.get("ywlx"));
						t_fjcfbModel.setWjm(wjm);
						t_fjcfbModel.setWjlj(wjlj);
						t_fjcfbModel.setFwjm(fwjm);
						t_fjcfbModel.setFwjlj(newfwjlj);
						fjcfbService.insert(t_fjcfbModel);
					}else if(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode().equals((String)map.get("ywlx"))){
						t_fjcfbModel = new FjcfbModel();
						//新增PDF附件信息
						t_fjcfbModel.setFjid(StringUtil.generateUUID());
						t_fjcfbModel.setYwid((String)map.get("ywid"));
						t_fjcfbModel.setXh("1");
						t_fjcfbModel.setZhbj("0");
						String t_wjm = (String)map.get("wordName");
						String t_fwjlj =  (String)map.get("fwjlj");
						
						t_fjcfbModel.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
						
						t_fjcfbModel.setWjm(t_wjm);
						t_fjcfbModel.setWjlj(p.eCode(t_fwjlj + "/" + t_wjm));
						t_fjcfbModel.setFwjm(p.eCode(t_wjm));
						t_fjcfbModel.setFwjlj(p.eCode(t_fwjlj));
						
						fjcfbService.insert(t_fjcfbModel);

						t_fjcfbModel.setYswjm(t_fjcfbModel.getWjm());
						t_fjcfbModel.setYswjlj(t_fjcfbModel.getWjlj());
						t_fjcfbModel.setShrs((String) map.get("shrList"));
						t_fjcfbModel.setShsjs((String) map.get("shsjList"));
						t_fjcfbModel.setShyhms((String) map.get("yhmList"));
						//String signflg = (String) map.get("signflg");
						String gzlx=(String) map.get("gzlx");
						//判断是否盖章
						if(StringUtils.isNotBlank(gzlx))
							AttachHelper.addChapter(t_fjcfbModel,gzlx);
						//若为审核时附加电子签名
//						if(StringUtils.isNotBlank((String) map.get("signflg")))
//							attachHelper.addWatermarkBisync(t_fjcfbModel, signflg, (String) map.get("sxrq"),null);

						sjxxService.sendFileToAli(t_fjcfbModel);
					}else if(BusTypeEnum.IMP_REPORT_HPV_WORD.getCode().equals((String)map.get("ywlx")) || BusTypeEnum.IMP_REPORT_HPV.getCode().equals((String)map.get("ywlx"))||
								BusTypeEnum.IMP_REPORT_FLU_WORD.getCode().equals((String)map.get("ywlx")) || BusTypeEnum.IMP_REPORT_FLU.getCode().equals((String)map.get("ywlx")) ||
							BusTypeEnum.IMP_REPORT_JHPCR_WORD.getCode().equals((String)map.get("ywlx")) || BusTypeEnum.IMP_REPORT_JHPCR.getCode().equals((String)map.get("ywlx"))
					){
						t_fjcfbModel = new FjcfbModel();
						//新增PDF附件信息
						t_fjcfbModel.setFjid(StringUtil.generateUUID());
						t_fjcfbModel.setYwid((String)map.get("ywid"));
						t_fjcfbModel.setXh("1");
						t_fjcfbModel.setZhbj("0");
						if (BusTypeEnum.IMP_REPORT_FLU_WORD.getCode().equals((String)map.get("ywlx")) || BusTypeEnum.IMP_REPORT_FLU.getCode().equals((String)map.get("ywlx"))){
							t_fjcfbModel.setYwlx(BusTypeEnum.IMP_REPORT_FLU.getCode());
						}else if (BusTypeEnum.IMP_REPORT_JHPCR_WORD.getCode().equals((String)map.get("ywlx")) || BusTypeEnum.IMP_REPORT_JHPCR.getCode().equals((String)map.get("ywlx"))){
							t_fjcfbModel.setYwlx(BusTypeEnum.IMP_REPORT_JHPCR.getCode());
						}else {
							t_fjcfbModel.setYwlx(BusTypeEnum.IMP_REPORT_HPV.getCode());
						}
						t_fjcfbModel.setWjm(wjm);
						t_fjcfbModel.setWjlj(zhwjxx);
						t_fjcfbModel.setFwjm(fwjm);
						t_fjcfbModel.setFwjlj(p.eCode(fwjlj));

						fjcfbService.insert(t_fjcfbModel);

						t_fjcfbModel.setYswjm(t_fjcfbModel.getWjm());
						t_fjcfbModel.setYswjlj(t_fjcfbModel.getWjlj());
						t_fjcfbModel.setShrs((String) map.get("shrList"));
						t_fjcfbModel.setShsjs((String) map.get("shsjList"));
						t_fjcfbModel.setShyhms((String) map.get("yhmList"));
//						String signflg = (String) map.get("signflg");
						String gzlx=(String) map.get("gzlx");
						//判断是否盖章
						if(StringUtils.isNotBlank(gzlx))
							AttachHelper.addChapter(t_fjcfbModel,gzlx);

						sjxxService.sendFileToAli(t_fjcfbModel);
					}else if(BusTypeEnum.IMP_MARKETING_SIN.getCode().equals(t_fjcfbModel.getYwlx())){
						t_fjcfbModel.setFjid(StringUtil.generateUUID());
						t_fjcfbModel.setYwlx((String)map.get("ywlx"));
						String jsrqstart;
						String jsrqend;
						int dayOfWeek = DateUtils.getWeek(new Date());
						if (dayOfWeek <= 2) {
							jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd");
							jsrqend = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 2), "yyyy-MM-dd");
						} else {
							jsrqstart = DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 1), "yyyy-MM-dd");
							jsrqend = DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd");
						}
						t_fjcfbModel.setWjm(jsrqstart+"~"+jsrqend+"营销看板.pdf");
						t_fjcfbModel.setWjlj(wjlj);
						t_fjcfbModel.setFwjm(fwjm);
						t_fjcfbModel.setFwjlj(newfwjlj);
						fjcfbService.insert(t_fjcfbModel);
						String sign = URLEncoder.encode(commonService.getSign(t_fjcfbModel.getFjid()),
								"UTF-8");
						String download = applicationurl + "/ws/file/downloadFile?fjid="
								+ t_fjcfbModel.getFjid() + "&sign=" + sign;
//						String viewurl = menuurl + "/wechat/file/pdfPreview?fjid=" + t_fjcfbModel.getFjid()
//								+ "&sign=" + sign;
						List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
						OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
			//            btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00004"));
			//            btnJsonList.setActionUrl(viewurl);
			//            btnJsonLists.add(btnJsonList);
			//            btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
						btnJsonList.setTitle(xxglService.getMsg("ICOMM_SJ00005"));
						btnJsonList.setActionUrl(download);
						btnJsonLists.add(btnJsonList);
//						RestTemplate t_restTemplate = new RestTemplate();
						List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.JAVA_ECHART.getCode());
						if (ddxxglDtolist != null && ddxxglDtolist.size() > 0) {
							for (int i = 0; i < ddxxglDtolist.size(); i++) {

								if (StringUtil.isNotBlank(ddxxglDtolist.get(i).getDdid())) {
									talkUtil.sendCardMessage(ddxxglDtolist.get(i).getYhm(), ddxxglDtolist.get(i).getDdid(),
											"营销数据看板",
											"营销数据看板",
											btnJsonLists, "1");
									
								}
							}
						}

						log.error("lwj - sendReportMessage -- 结束发送钉钉。 ");
					}
				}
				
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DOC_OK);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e){
			log.error("Receive DocChangePdf:"+e.toString());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(DOC_OK);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 判断报告类型
	 * @param ywlx
	 * @return
	 */
	private boolean judgmentType(String ywlx) {
		List<String> ywlxs= wechatCommonUtils.getInspectionWordYwlxs();
		for(String t_ywlx : ywlxs){
			if(t_ywlx.equals(ywlx))
				return true;
		}
		return false;
	}
	
	/**
	 * 下载pdf文件
	 * @param paramMap
	 * @param filePath
	 * @return
	 */
	private boolean downloadPdfFile(MultiValueMap<String, Object> paramMap,String filePath) {
		boolean downflg = false;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
			RestTemplate t_restTemplate = new RestTemplate();
			ResponseEntity<byte[]> response = t_restTemplate.exchange("http://" + FTP_URL + ":8756/file/downloadPdfFile", HttpMethod.POST, httpEntity, byte[].class);
			byte[] result = response.getBody();
			inputStream = new ByteArrayInputStream(result);
			
			outputStream = new FileOutputStream(new File(filePath));
			
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf, 0, 1024)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.flush();
			downflg = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("文件下载失败：" + e.getMessage());
        } finally {
        	try {
				if(inputStream != null) inputStream.close();
				if(outputStream != null) outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.error("文件下载关闭流失败：" + e.getMessage());
			}
        }
		
		return downflg;
	}
	
	/**
	 * 将后缀修改为pdf
	 * @param filename
	 * @return
	 */
	private String updateSuffix(String filename){
		if ((filename != null) && (filename.length() > 0)) { 
			int dot = filename.lastIndexOf('.'); 
			if ((dot >-1) && (dot < (filename.length()))) { 
				String substring = filename.substring(0, dot);
				filename = substring.concat(".pdf");
				return filename;
			}
		}
        return filename;
	}
	
	/**
	 * 根据路径创建文件
	 * @param storePath
	 * @return
	 */
	private boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdirs();
	}

	/*----------------------------------------    整合rabbit    ----------------------------------------*/
	
	/**
	 * 送检消息整合
	 * @param str
	 */
	@RabbitListener(queues = MqType.OPERATE_INSPECTION, containerFactory="specialFactory")
	public void receiveInspection( String str) {
		log.error("Receive OPERATE_INSPECTION:"+str);
		String inspupdsjid="";
		//消息处理标记 消息ID
		String onlyId = "";
        try {
			if(StringUtil.isNotBlank(str) && str.contains("{")){
				onlyId=str.substring(0,str.indexOf("{"));
				if(StringUtil.isNotBlank(onlyId)) {
					String clbj = String.valueOf(redisUtil.hget("CLBJ:" + onlyId, "clbj"));
					log.error("获取处理标记 clbj："+ clbj);
					if ("1".equals(clbj) || "0".equals(clbj)) {
						log.error("Receive OPERATE_INSPECTION -- 此消息正在处理中或者已经处理完成，本次不再处理。 onlyId：" + " clbj：" + clbj);
						return;
					}
					//开始准备处理消息
					redisUtil.hset("CLBJ:" + onlyId, "clbj", "0", 24 * 3600);
				}
			}
        	if(StringUtil.isNotBlank(str) && str.length() >= 21) {
				String lx = str.substring(13,21);
				String msg = str.substring(21);
            	if(RabbitEnum.INSP_UPD.getCode().equals(lx)) {
            		log.error("Receive INSP_UPD");
            		SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					inspupdsjid=sjxxDto.getSjid();
					dealInspectionMsgBefore(sjxxDto.getSjid(),str);
            	}else if(RabbitEnum.INSP_DEL.getCode().equals(lx)) {
            		log.error("Receive INSP_DEL");
            		SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
        			sjxxService.delCheckApply(sjxxDto);
            	}else if(RabbitEnum.INSP_CFM.getCode().equals(lx)) {
            		log.error("Receive INSP_CFM");
            		SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
        			sjxxService.sampleConfig(sjxxDto);
            	}else if(RabbitEnum.PYIF_ADD.getCode().equals(lx)) {
            		log.error("Receive PYIF_ADD");
            		PayinfoDto payinfoDto = JSONObject.parseObject(msg, PayinfoDto.class);
                	payinfoService.editPayinfo(payinfoDto);
            	}else if(RabbitEnum.PYIF_MOD.getCode().equals(lx)) {
            		log.error("Receive PYIF_MOD");
            		SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
                	sjxxService.modInspPayinfo(sjxxDto);
            	}else if(RabbitEnum.FDIF_MOD.getCode().equals(lx)) {
            		log.error("Receive FDIF_MOD");
            		FjsqDto fjsqDto = JSONObject.parseObject(msg, FjsqDto.class);
                	fjsqService.modRecheckPayinfo(fjsqDto);
            	}else if(RabbitEnum.PYIF_SUC.getCode().equals(lx)) {
            		log.error("Receive PYIF_SUC");
            		PayinfoDto payinfoDto = JSONObject.parseObject(msg, PayinfoDto.class);
        			payinfoService.paySuccessMessage(payinfoDto);
            	}else if(RabbitEnum.PYIF_RES.getCode().equals(lx)) {
            		log.error("Receive PYIF_RES");
            		SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
        			sjxxService.weChatPayResult(sjxxDto);
            	}else if(RabbitEnum.FEED_BAC.getCode().equals(lx)) {
            		log.error("Receive FEED_BAC");
            		SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
        			sjxxService.getFeedBack(sjxxDto);
				}else if(RabbitEnum.FILE_SAV.getCode().equals(lx)) {
					log.error("Receive FILE_SAV");
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					sjxxService.saveScanFile(sjxxDto);
            	}else if(RabbitEnum.SJSY_MOD.getCode().equals(lx)) {
					log.error("Receive SJSY_MOD");
					List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(msg, SjsyglDto.class);
					dealInspectionMsgBefore(list.get(0).getSjid(),str);
				} else if(RabbitEnum.XMSY_MOD.getCode().equals(lx)) {
					log.error("Receive XMSY_MOD");
					List<XmsyglDto> list=(List<XmsyglDto>)JSON.parseArray(msg,XmsyglDto.class);
					xmsyglService.syncRabbitMsg(list);
				}
        	}
			//平常的处理里不需要把信息放到 XxdlcwglDto 里
        	/*XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.OPERATE_INSPECTION);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);*/
			//消息处理完成，将消息ID储存到redis，用于防止消息重复消费,设置24小时过期
			if(StringUtil.isNotBlank(onlyId)) {
				redisUtil.hset("CLBJ:" + onlyId, "clbj", "1", 24 * 3600);
			}
		} catch (Exception e) {
			log.error("Receive OPERATE_INSPECTION error:"+e.getMessage());
			//消息处理异常，将消息ID储存到redis，用于防止消息重复消费,设置24小时过期
			if(StringUtil.isNotBlank(onlyId)) {
				redisUtil.hset("CLBJ:" + onlyId, "clbj", "2", 24 * 3600);
			}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			if(StringUtil.isNotBlank(str) && str.length()>=21){
				String lx = str.substring(13,21);
				if(RabbitEnum.INSP_UPD.getCode().equals(lx)) {
					xxdlcwglDto.setCwid(StringUtil.isNotBlank(inspupdsjid)?inspupdsjid:StringUtil.generateUUID());
				}
			}
			XxdlcwglDto xxdlcwglDto1=xxdlcwglService.getDto(xxdlcwglDto);
			if(xxdlcwglDto1==null){
				xxdlcwglDto.setCwlx(MqType.OPERATE_INSPECTION);
				if(str.length() > 4000){
					str = str.substring(0, 4000);
				}
				xxdlcwglDto.setYsnr(str);
				xxdlcwglService.insert(xxdlcwglDto);
			}

		}
    }
	//rabbit消息前处理
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dealInspectionMsgBefore(String keyId,String str) throws InterruptedException{
		String lockkey = RedisCommonKeyEnum.WECHAT_INSPECTION_LOCK.getKey()+keyId;
		String dataKey = RedisCommonKeyEnum.WECHAT_INSPECTION_LIST.getKey()+keyId;
		//将消息存储到list中
		redisUtil.lSet(dataKey, str,RedisCommonKeyEnum.WECHAT_INSPECTION_LIST.getExpire());
		dealInspectionMsg(lockkey,dataKey,str);
	}
	//处理rabbit消息
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dealInspectionMsg(String lockkey,String dataKey,String rabbitMsg) throws InterruptedException {
		while(!redisUtil.setIfAbsent(lockkey, "matridx", RedisCommonKeyEnum.INSPECTION_WECHAT_LOCK.getExpire(), TimeUnit.SECONDS)){
			Thread.sleep(50);
		}
		//获取list中的第一个消息
		Object obj = redisUtil.lGetIndex(dataKey, 0);
		//获取消息内容进行处理
		String str;
		//如果为空
		if(obj == null){
			//没有获取到，则不做任何处理
			redisUtil.del(lockkey);
			return;
		}else {
			str = String.valueOf(obj);
		}
		log.error("dealInspectionMsg 获取锁，key:" + lockkey);
		//如果不是我要执行的消息，不做处理
		if (!str.equals(rabbitMsg)){
			log.error("dealInspectionMsg not my message。当前内容:" + str);
			//释放锁
			redisUtil.del(lockkey);
			//让其他去获取锁
			Thread.sleep(100);
			//再次竞争,key 需要更改，否则陷入死循环
			String msg = str.substring(21);
			SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
			String new_keyId = sjxxDto.getSjid();
			String new_lockkey = RedisCommonKeyEnum.INSPECTION_WECHAT_LOCK.getKey() + new_keyId;
			String new_dataKey = RedisCommonKeyEnum.WECHAT_INSPECTION_LIST.getKey() + new_keyId;
			//再次竞争
			dealInspectionMsg(new_lockkey,new_dataKey,str);
		}else {
			try {
				String lx = str.substring(13, 21);
				String msg = str.substring(21);
				log.error("dealInspectionMsg {}", lx);
				if(RabbitEnum.INSP_UPD.getCode().equals(lx)) {
					SjxxDto sjxxDto = JSONObject.parseObject(msg, SjxxDto.class);
					SjxxDto t_sjxxDto = sjxxService.getDtoById(sjxxDto);
					//查询该条信息的原关注病原
					List<String> ygzbys = sjgzbyService.getGzby(sjxxDto.getSjid());
					sjxxDto.setYgzbys(ygzbys);
					if(t_sjxxDto == null){
						sjxxService.checkApply(sjxxDto);
					}else{
						sjxxService.checkModApply(sjxxDto,t_sjxxDto);
					}
					if(StringUtil.isNotBlank(sjxxDto.getMxid())){
						MxxxDto mxxxDto =new MxxxDto();
						mxxxDto.setMxid(sjxxDto.getMxid());
						mxxxDto.setStatus("1");
						mxxxService.update(mxxxDto);
					}
				}else if(RabbitEnum.SJSY_MOD.getCode().equals(lx)) {
					List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(msg, SjsyglDto.class);
					sjsyglService.syncRabbitMsg(list);
					if (list != null && list.size() > 0){
						boolean isJcdwSame = true;
						String jcdw = list.get(0).getJcdw();
						String xgry = list.get(0).getXgry();
						String sjid = list.get(0).getSjid();
						for(SjsyglDto dto:list){
							if (isJcdwSame && !jcdw.equals(dto.getJcdw())){
								isJcdwSame = false;
								break;
							}
						}
						if (isJcdwSame && StringUtil.isNotBlank(sjid)){
							//若所有检测单位都相同，则更新送检信息的检测单位
							SjxxDto sjxxDto = new SjxxDto();
							sjxxDto.setSjid(sjid);
							sjxxDto.setXgry(xgry);
							sjxxDto.setJcdw(jcdw);
							sjxxService.updateJcdw(sjxxDto);
						}
					}
				}
			} catch (Exception e) {
				log.error("dealInspectionMsg 异常{}", str);
			} finally {
				//删除 防止重复处理
				redisUtil.removeValueFromList(dataKey, str);
				redisUtil.del(lockkey);
			}
		}
	}
	@RabbitListener(queues = ("matridx.sse.sendmsg.exception" ), containerFactory="defaultFactory")
	public void sseSendMsgException(String str){
		if(StringUtil.isNotBlank(str)) {
			try {
				Map<String, Object> map = JSONObject.parseObject(str);
				if(map.get("type")!=null) {
					SjycDto _sjycDto=new SjycDto();
					switch (map.get("type").toString()) {
						case "add":
							_sjycDto=JSONObject.parseObject(map.get("sjycDto").toString(),SjycDto.class);
							_sjycDto.setFjbcbj("wechart");
							_sjycDto.setTwrlx(TwrTypeEnum.WECHAT.getCode());
							sjycService.addSaveException(_sjycDto);
							break;
						case "finish":
							_sjycDto=JSONObject.parseObject(map.get("sjycDto").toString(),SjycDto.class);
							if (!(_sjycDto.getIds()!=null && _sjycDto.getIds().size()>0)){
								List<String> ids = new ArrayList<>();
								ids.add(_sjycDto.getYcid());
								_sjycDto.setIds(ids);
							}
							sjycService.finishYc(_sjycDto);
							exceptionSSEUtil.finishException(_sjycDto.getYcid());
							SjycDto dtoById1 = sjycService.getDtoById(_sjycDto.getYcid());
							List<SjycDto> sjycDtos1=new ArrayList<>();
							List<String> yhids1=new ArrayList<>();
							List<String> jsids1=new ArrayList<>();

							if("ROLE_TYPE".equals(dtoById1.getQrlx())){
								if(!jsids1.contains(dtoById1.getQrr())){
									jsids1.add(dtoById1.getQrr());
								}
							}else{
								if(!yhids1.contains(dtoById1.getQrr())){
									yhids1.add(dtoById1.getQrr());
								}
							}
							if(jsids1!=null&&jsids1.size()>0){
								SjyctzDto sjyctzDto_t=new SjyctzDto();
								sjyctzDto_t.setIds(jsids1);
								List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
								for(SjyctzDto dto:yhjsList){
									if(!yhids1.contains(dto.getRyid())){
										SjycDto sjycDto_t=new SjycDto();
										sjycDto_t.setRyid(dto.getRyid());
										sjycDtos1.add(sjycDto_t);
									}
								}
							}

							if(yhids1!=null&&yhids1.size()>0){
								for(String yhid:yhids1){
									SjycDto sjycDto_t=new SjycDto();
									sjycDto_t.setRyid(yhid);
									sjycDtos1.add(sjycDto_t);
								}
							}
							SjycfkDto sjycfkDto_=new SjycfkDto();
							sjycfkDto_.setYcid(_sjycDto.getYcid());
							sjycfkDto_.setLrrymc(_sjycDto.getLrrymc());
							sjycService.sendFkMessage(sjycDtos1,sjycfkDto_,true);
							break;
						case "fk":
							SjycfkDto sjycfkDto=JSONObject.parseObject(map.get("sjycfkDto").toString(),SjycfkDto.class);
							List<JcsjDto> ycqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.FEEDBACK_DISTINGUISH.getCode());
							for(JcsjDto jcsjDto:ycqfList){
								if("WECHAT_FEEDBACK".equals(jcsjDto.getCsdm())){
									sjycfkDto.setFkqf(jcsjDto.getCsid());
								}
							}
							sjycfkDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
							SjycDto sjycDto=new SjycDto();
							sjycDto.setYcid(sjycfkDto.getYcid());
							sjycDto.setZhhfsj(sjycfkDto.getLrsj());
							sjycDto.setZhhfnr(sjycfkDto.getLrrymc()+":"+sjycfkDto.getFkxx());
							sjycService.addSaveExceptionFeedback(sjycfkDto,sjycDto);
							SjycDto dtoById = sjycService.getDtoById(sjycfkDto.getYcid());
							List<SjycDto> sjycDtos=new ArrayList<>();
							List<String> yhids=new ArrayList<>();
							List<String> jsids=new ArrayList<>();
							if("ROLE_TYPE".equals(dtoById.getQrlx())){
								if(!jsids.contains(dtoById.getQrr())){
									jsids.add(dtoById.getQrr());
								}
							}else{
								if(!yhids.contains(dtoById.getQrr())){
									yhids.add(dtoById.getQrr());
								}
							}
							if(jsids!=null&&jsids.size()>0){
								SjyctzDto sjyctzDto_t=new SjyctzDto();
								sjyctzDto_t.setIds(jsids);
								List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
								for(SjyctzDto dto:yhjsList){
									if(!yhids.contains(dto.getRyid())){
										SjycDto sjycDto_t=new SjycDto();
										sjycDto_t.setRyid(dto.getRyid());
										sjycDtos.add(sjycDto_t);
									}
								}
							}

							if(yhids!=null&&yhids.size()>0){
								for(String yhid:yhids){
									SjycDto sjycDto_t=new SjycDto();
									sjycDto_t.setRyid(yhid);
									sjycDtos.add(sjycDto_t);
								}
							}
							exceptionSSEUtil.addExceptionMessage(sjycDtos,sjycDto.getYcid());
							SjycfkDto sjycfkDto1=JSONObject.parseObject(map.get("sjycDto").toString(),SjycfkDto.class);
							sjycfkDto.setYcbt(sjycfkDto1.getYcbt());
							sjycService.sendFkMessage(sjycDtos,sjycfkDto,false);
							break;
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				// TODO Auto-generated catch block
				XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
				xxdlcwglDto.setCwid(StringUtil.generateUUID());
				xxdlcwglDto.setCwlx("matridx.sse.sendmsg.exception");
				if(str.length() > 4000){
					str = str.substring(0, 4000);
				}
				xxdlcwglDto.setYsnr(str);
				xxdlcwglService.insert(xxdlcwglDto);
			}
		}
	}
	@RabbitListener(queues = ("matridx.sse.sendmsg${matridx.rabbit.ssekey:}" ), containerFactory="defaultFactory")
    public void sseSendMsg(String str){
		try {
			if(StringUtil.isNotBlank(str)){
				try {
					Map<String,Object>map=JSONObject.parseObject(str);
					if(map.get("type")!=null){
						switch (map.get("type").toString()){
							case "fjsc":
								if (StringUtil.isNotBlank(String.valueOf(map.get("fjids"))) && StringUtil.isNotBlank(String.valueOf(map.get("key")))){
									String[] strings = map.get("fjids").toString().split(",");
									List<FjcfbDto> fjcfbDtoList = new ArrayList<>();
									for (String string : strings) {
										Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+string);
										if (null != mFile && mFile.size()>0){
											String wjm = (String)mFile.get("wjm");
											String wjlj = (String)mFile.get("wjlj");
											FjcfbDto fjcfbDto = new FjcfbDto();
											fjcfbDto.setFjid(string);
											fjcfbDto.setWjm(wjm);
											fjcfbDto.setWjlj(wjlj);
											fjcfbDtoList.add(fjcfbDto);
										}
									}
									map.put("fjcfbDtoList",fjcfbDtoList);
									SseEmitterAllServer.sendMessage(map.get("key").toString(),JSON.toJSONString(map),map.get("key").toString().split(":")[0]);
								}
								break;
							case "ljgb":
								if (StringUtil.isNotBlank(String.valueOf(map.get("key")))){
									SseEmitterAllServer.removeUser(map.get("key").toString(),map.get("key").toString().split(":")[0]);
									redisUtil.del(map.get("key").toString());
								}
								break;
						case "ljgb_exception":
							if (StringUtil.isNotBlank(String.valueOf(map.get("key")))){
								ExceptionSSEUtil.removeUser(map.get("userid").toString(),map.get("key").toString());
								redisUtil.del(map.get("key").toString());
							}
							break;
						case "sendMsg_exception":
							  ExceptionSSEUtil.batchSendMessage(map.get("wsInfo").toString(),map.get("userid").toString());
							break;
						}
					}
				} catch (Exception e) {

					log.error(e.getMessage());
					// TODO Auto-generated catch block
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("matridx.sse.sendmsg${matridx.rabbit.ssekey:}");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	/**
	 * 微信消息整合(暂未使用)
	 * @param str
	 */
	/*@RabbitListener(queues = MqType.OPERATE_WECHAT, containerFactory="defaultFactory")
	public void receiveWechat(String str) {
		log.error("Receive OPERATE_WECHAT:"+str);
        try {
        	if(StringUtil.isNotBlank(str) && str.length() > 8) {
        		String lx = str.substring(0,8);
        		String msg = str.substring(8);
            	if(RabbitEnum.WECH_MOD.getCode().equals(lx)) {
            		log.error("Receive WECH_MOD");
            		//根据微信号在 微信用户表里查找 不用考虑删除标记，如果已经存在，则更新信息，如果不存在，则新增记录
            		WxyhDto wxyhDto = JSONObject.parseObject(msg, WxyhDto.class);
        			wxyhService.userMod(wxyhDto);
            	}else if(RabbitEnum.WECH_SUB.getCode().equals(lx)) {
            		log.error("Receive WECH_SUB");
            		//根据微信号在 微信用户表里查找 不用考虑删除标记，如果已经存在，删除标记为1则更新为0，如果不存在，则新增记录
                	WxyhDto wxyhDto = JSONObject.parseObject(msg, WxyhDto.class);
        			wxyhService.subscibe(wxyhDto);
            	}else if(RabbitEnum.WECH_UNS.getCode().equals(lx)) {
            		log.error("Receive WECH_UNS");
            		//根据微信号在 微信用户表里查找，如果已经存在，则更新删除标记为1 ，同时更新删除时间，如果不存在，则不做处理
                	WxyhDto wxyhDto  = JSONObject.parseObject(msg, WxyhDto.class);
        			wxyhService.unsubscibe(wxyhDto);
            	}else if(RabbitEnum.WECH_TXT.getCode().equals(lx)) {
            		log.error("Receive WECH_TXT");
        			//给相应的人发送钉钉消息
                	WeChatTextModel weChatTextModel = JSONObject.parseObject(msg, WeChatTextModel.class);
                	List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.MATRIDX_TEXT_TYPE.getCode());
        			if(ddxxglDtos != null && ddxxglDtos.size() > 0){
        				//查询用户信息
        				String openid = weChatTextModel.getFromUserName();
        				WxyhDto wxyhDto = wxyhService.getDtoById(openid);
        				if(wxyhDto != null){
        					//发送钉钉消息
        					String token = talkUtil.getToken();
        					for(int i=0;i<ddxxglDtos.size();i++){
        						if(StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid())){
        							talkUtil.sendWorkMessage("null", ddxxglDtos.get(i).getDdid(), xxglService.getMsg("ICOMM_SW00001", weChatTextModel.getGzpt()),
        									xxglService.getMsg("ICOMM_SW00002",wxyhDto.getWxm(),wxyhDto.getYhm(),weChatTextModel.getContent(),weChatTextModel.getCreateTime()));
        						}
        					}
        				}
        			}
            	}else if(RabbitEnum.WECH_ICK.getCode().equals(lx)) {
            		log.error("Receive WECH_ICK");
            	}
        	}
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.OPERATE_WECHAT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		} catch (Exception e) {
			log.error("Receive usermod:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx(MqType.OPERATE_WECHAT);
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
    }*/

	@RabbitListener(queues = "wechat.flu.file.analysis", containerFactory="defaultFactory")
	public void fluFileAnalysis(String str){
		Map<String, Object> map = new HashMap<>();
		try {
			map = JSON.parseObject(str,new TypeReference<Map<String, Object>>(){});
			log.error("甲乙流文件上传Rabbit:"+str);
			fzjcxxPJService.analysisFluFile(map);
		} catch (Exception e) {
			log.error("甲乙流文件上传错误:"+e.toString());
		}
	}

	/**
	 *
	 * @param str
	 */
	@RabbitListener(queues = "matridx.igams.inspection.detailed", containerFactory="defaultFactory")
	public void igamsInspectDetail(String str) {

		String msginfo = "";
		try {
			log.error("rabbit接收生信详细审核结果："+str);
			//当前rabbit消息处，默认都会加上时间戳，所以需要手动处理
			if(StringUtil.isNotBlank(str) && str.contains("{")) {
				msginfo = str.substring(str.indexOf("{"));
			}else{
				msginfo = str;
			}
			boolean result = sjxxService.receiveDetailedReport(msginfo);
		} catch (Exception e) {
			log.error("igamsInspectDetail 生信详细审核结果 失败:"+e.getMessage());
			try {
				JkdymxDto jkdymxDto = new JkdymxDto();
				jkdymxDto.setLxqf("recv"); // 类型区分 发送 send;接收recv
				jkdymxDto.setDysj(DateUtils.getCustomFomratCurrentDate(null));
				jkdymxDto.setDydz("/ws/pathogen/receiveDetailedReport");
				jkdymxDto.setNr(str);
				jkdymxDto.setQtxx("");
				jkdymxDto.setDyfl("生信详细审核结果");
				jkdymxDto.setDyzfl("191接收处理");
				//为了保存业务ID，需要增加内容的解析
				WeChatDetailedReportModel details = JSONObject.parseObject(msginfo, WeChatDetailedReportModel.class);
				if(details!=null){
					jkdymxDto.setYwid(details.getYbbh());
				}
				jkdymxDto.setSfcg("0"); // 是否成功  0:失败 1:成功 2:未知
				jkdymxDto.setFhnr(e.getMessage()); // 返回内容
				jkdymxDto.setFhsj(DateUtils.getCustomFomratCurrentDate(null)); // 返回时间
				jkdymxService.insertJkdymxDto(jkdymxDto); // 同步新增
			}catch(Exception en){
				log.error("igamsInspectDetail 生信详细审核结果 插入接口调用明细 失败:"+en.getMessage());
			}
		}
	}

	@RabbitListener(queues = ("matridx.inspect.report.notify"), containerFactory="defaultFactory")
	public void sendReport(String str) {
		Map<String,Object> map = JSON.parseObject(str,new TypeReference<Map<String, Object>>(){});
		if(map.get("url")==null){
			log.error("sendReport url为空:" + JSON.toJSONString(map));
			return;
		}
		if(map.get("fjid")==null){
			log.error("sendReport fjid为空:" + JSON.toJSONString(map));
			return;
		}
		if(map.get("fwjm")==null){
			log.error("sendReport fwjm为空:" + JSON.toJSONString(map));
			return;
		}
		if(map.get("ywid")==null){
			log.error("sendReport ywid为空:" + JSON.toJSONString(map));
			return;
		}
		if(map.get("ywlx")==null){
			log.error("sendReport ywlx为空:" + JSON.toJSONString(map));
			return;
		}
		if(map.get("fileName")==null){
			log.error("sendReport fileName为空:" + JSON.toJSONString(map));
			return;
		}
		//为防止 昆附一这边C#无法接收数组 的问题，需进行单独处理
		if(((String)map.get("CSKZ1")).contains("昆明医科大学第一附属医院")){
			//map.remove("wzjy");
			//昆附一的转换成String
			Map<String,Object> wzjyMap = (Map<String, Object>) map.get("wzjy");
			map.put("wzjy",JSONObject.toJSONString(wzjyMap));
		}
		String urlString = map.get("url").toString();
		map.put("VirtualHost",reportVirtualHost);//覆盖成reportVirtualHost
		DBEncrypt p = new DBEncrypt();
		//通过接口获取附件信息
		//RestTemplate restTemplate = new RestTemplate();
		//MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		//paramMap.add("fjid",map.get("fjid"));
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(map.get("fjid").toString());
		fjcfbDto.setFwjm(map.get("fwjm").toString());
		fjcfbDto.setYwid(map.get("ywid").toString());
		fjcfbDto.setYwlx(map.get("ywlx").toString());
		fjcfbDto.setWjm(map.get("fileName").toString());
		fjcfbDto.setXh("1");
		//fjcfbDto = restTemplate.postForObject(serverapplicationurl+getFjcfbDtoUrl, paramMap, FjcfbDto.class);
		//根据日期创建文件夹
		String storePath = "";
		if(StringUtil.isNotBlank(createFolder)){
			storePath = releaseFilePath;
		}else{
			storePath = prefix + releaseFilePath + fjcfbDto.getYwlx()+"/"+ "UP"+
					DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		}
		CloseableHttpClient client= HttpClients.createDefault();
		HttpPost post  =  new  HttpPost(urlString);
		FileOutputStream fos=  null ;
		String fwjm = null;
		try {
			HttpResponse response = client.execute(post);
			org.apache.http.HttpEntity entity_t = response.getEntity();
			fwjm = p.dCode(map.get("fwjm").toString());
			log.error("创建文件，文件路径："+storePath+File.separator+ fwjm);
			File filePath = new File(storePath);
			if(!filePath.isDirectory()){
				filePath.mkdirs();
			}
			File file = new File(storePath+File.separator+fwjm);
			fos =  new  FileOutputStream(file);
			fos.write(EntityUtils. toByteArray(entity_t));
			fos.flush();
			fjcfbDto.setWjlj(p.eCode(storePath+File.separator+fwjm));
		} catch (Exception e) {
			log.error("写入文件失败！");
		}finally {
			try {
				fos.close();
			} catch (Exception e) {
				log.error("流关闭失败！");
			}
		}
		//更改文件路径，fjcfbDto存入数据库
		fjcfbDto.setFwjlj(p.eCode(storePath));
		fjcfbDto.setZhbj("1");
		FjcfbDto fjcfbDto_t = fjcfbService.getDto(fjcfbDto);
		if(fjcfbDto_t==null){
			//新增附件存放表数据
			fjcfbService.insert(fjcfbDto);
		}else{
			fjcfbService.update(fjcfbDto);
		}
		urlString = applicationurl+downloadUrl;
		map.put("url",urlString);
		extendAmqpUtils.sendDownloadReportMsg(map);
	}

}
