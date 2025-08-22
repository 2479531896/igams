package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.DingNotificationTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.server.wechat.dao.entities.FaqxxDto;
import com.matridx.server.wechat.dao.entities.HbsfbzDto;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjkjxxDto;
import com.matridx.server.wechat.dao.entities.SjlczzDto;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjqqjcDto;
import com.matridx.server.wechat.dao.entities.SjqxDto;
import com.matridx.server.wechat.dao.entities.SjsyglDto;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjycWechatDto;
import com.matridx.server.wechat.dao.entities.SjycfkWechatDto;
import com.matridx.server.wechat.dao.entities.SjyczdWechatDto;
import com.matridx.server.wechat.dao.entities.SjysxxDto;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatTextModel;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.XxdjDto;
import com.matridx.server.wechat.dao.entities.XxpzDto;
import com.matridx.server.wechat.enums.IdentityTypeEnum;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IFaqxxService;
import com.matridx.server.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.server.wechat.service.svcinterface.IHzxxService;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.server.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.server.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.server.wechat.service.svcinterface.ISjkjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjlczzService;
import com.matridx.server.wechat.service.svcinterface.ISjnyxService;
import com.matridx.server.wechat.service.svcinterface.ISjqxService;
import com.matridx.server.wechat.service.svcinterface.ISjsyglService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.server.wechat.service.svcinterface.ISjycWechatService;
import com.matridx.server.wechat.service.svcinterface.ISjycfkWechatService;
import com.matridx.server.wechat.service.svcinterface.ISjyczdWechatService;
import com.matridx.server.wechat.service.svcinterface.ISjysxxService;
import com.matridx.server.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWeChatService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.service.svcinterface.IXxdjService;
import com.matridx.server.wechat.service.svcinterface.IXxpzService;
import com.matridx.server.wechat.util.ExceptionWechartUtil;
import com.matridx.server.wechat.util.MessageUtil;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.xml.BasicXmlReader;
import com.matridx.springboot.util.xml.XmlUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/wechat")
public class WeChatControl extends BaseController {

	@Value("${matridx.dashscope.apikey:}")
	private String apikey;

	@Autowired
	IXxglService xxglService;
	@Autowired
	ISjycWechatService sjycService;

	@Autowired
	ExceptionWechartUtil exceptionWechartUtil;
	@Autowired
	ISjyczdWechatService sjyczdService;

	@Autowired
	ISjycfkWechatService sjycfkService;
	@Autowired
	ISjdwxxService sjdwxxService;

	@Autowired
	IHzxxService hzxxService;


	@Autowired
	IJcsjService jcsjService;

	@Autowired
	ISjysxxService sjysxxService;


	@Autowired
	ISjbgsmService sjbgsmservice;

	@Autowired
	ISjjcxmService sjjcxmService;

	@Autowired
	ISjxxService sjxxService;

	@Autowired
	ISjkjxxService sjkjxxService;

	@Autowired
	ISjlczzService sjlczzService;

	@Autowired
	ISjhbxxService sjhbxxService;

	@Autowired
	IWxyhService wxyhService;

	@Autowired
	IFjcfbService fjcfbService;

	@Autowired
	ICommonService commonService;

	@Autowired
	ISjxxjgService sjxxjgService;

	@Autowired
	ISjnyxService sjnyxService;

	@Autowired
	IFaqxxService faqxxService;

	@Autowired
	private IWeChatService weChatService;

	@Autowired
	IXxdjService xxdjService;

	@Autowired
	IXxpzService xxpzService;

	@Autowired
	IXtszService xtszService;

	@Autowired
	IWbcxService wbcxService;

	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	ISjzmjgService sjzmjgService;

	@Autowired
	ISjqxService sjqxService;

	@Autowired
	WechatCommonUtils wechatCommonUtils;

	@Autowired
	ISjsyglService sjsyglService;

	@Autowired
	IHbsfbzService hbsfbzService;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	WeChatUtils weChatUtils;

	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.fileupload.prefix:}")
	private String prefix;
	@Value("${matridx.fileupload.tempPath:}")
	private String tempFilePath;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Value("${matridx.wechat.inspectionurl:}")
	private String inspectionurl;
	//微信通知的模板ID
	@Value("${matridx.wechat.templateid:}")
	private String templateid;
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private Logger log = LoggerFactory.getLogger(WeChatControl.class);

	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 对外访问主页
	 * @param wxid
	 * @return
	 */
	@RequestMapping(value="/authorization")
	public ModelAndView authorization(@RequestParam(name = "wxid", required = false) String wxid) {
		ModelAndView mv = new ModelAndView("wechat/view/authorization");
		try {
			mv.addObject("openurl",weChatService.authorization(wxid));
		} catch (BusinessException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 对外访问主页
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/displayView")
	public ModelAndView displayView(@RequestParam(name = "code", required = false) String code, HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("wechat/view/display_view");
			mv.addObject("view_url",request.getParameter("view_url"));
			mv.addObject("code", code);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 微信返回的事件信息处理方法
	 * @param request
	 * @return
	 */
	@RequestMapping("/serviceEvent")
	@ResponseBody
	public String serviceEvent(HttpServletRequest request){

		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		try {
			String echostr = request.getParameter("echostr");
			// 微信加密签名
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			//根据外部程序代码查询外部程序表信息
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			log.error("杰毅医检 echostr:"+echostr + " wbcxdm:" + wbcxdm);
			if(wbcxDto == null){
				log.error("未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
				return null;
			}
			boolean isSuccess = weChatService.checkSignature(signature, timestamp, nonce, wbcxDto);
			//非法访问，直接返回
			if(!isSuccess) {
                return null;
            }
			//服务器验证
			if(StringUtil.isNotBlank(echostr)){
				// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
				return echostr;
			}else{//其他信息
				InputStream ins = request.getInputStream();
				Map<String, Object> result = BasicXmlReader.readXmlToMap(ins);
				log.error("杰毅医检 result:"+JSON.toJSONString(result));
				String msgType = (String)result.get("MsgType");
				///如果为事件类型
				if(MessageUtil.MSGTYPE_EVENT.equals(msgType)){
					String eventId = (String)result.get("Event");
					//订阅
					//参数	描述
					//ToUserName	开发者微信号
					//FromUserName	发送方帐号（一个OpenID）
					//CreateTime	消息创建时间 （整型）
					//MsgType	消息类型，event
					//Event	事件类型，subscribe(订阅)、unsubscribe(取消订阅)
					if(MessageUtil.MESSAGE_SUBSCIBE.equals(eventId)){
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.subscribe(weChatTextModel, wbcxDto);
					}else if(MessageUtil.MESSAGE_UNSUBSCIBE.equals(eventId)){
						//取消订阅
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.unsubscribe(weChatTextModel, wbcxDto);
					}else if(MessageUtil.MESSAGE_CLICK.equals(eventId)){
						//点击菜单拉取消息时的事件推送
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.clickEvent(weChatTextModel, wbcxDto);
					}else if(MessageUtil.MESSAGE_VIEW.equals(eventId)){
						//点击菜单跳转链接时的事件推送
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.viewEvent(weChatTextModel, wbcxDto);
					}else if(MessageUtil.MESSAGE_SCAN.equals(eventId)){
						//扫描带参数二维码的事件推送
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.scanEvent(weChatTextModel, wbcxDto);
					}
//					else{//else内内容备注是，故将else也注释，2023/10/26
						//WeChatTextModel weChatTextModel= packagTextModel(result);
//					}
				}else if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
					//如果为文本消息
//					WeChatTextModel weChatTextModel= packagTextModel(result);
//					weChatTextModel.setGzpt(wbcxDto.getWbcxmc());
//					if(weChatService.textDeal(weChatTextModel, wbcxDto,msgType)){
//						FaqxxDto faqxxDto=new FaqxxDto();
//						DBEncrypt crypt = new DBEncrypt();
//						faqxxDto.setBt(weChatTextModel.getContent());
//						String url=crypt.dCode(applicationurl);
//						String wz;
//						String bt;
//						int xh;
//						String xx="您的疑问有以下几个信息,请确认:\n";
//						faqxxDto.setGzpt(wbcxDto.getWbcxid());
//						List<FaqxxDto> faqxxlist=faqxxService.getFqaxxByBt(faqxxDto);
//						if(faqxxlist != null && faqxxlist.size()>0) {
//							for(int i=0;i<faqxxlist.size();i++) {
//								wz=faqxxlist.get(i).getWz();
//								bt=faqxxlist.get(i).getBt();
//								xh=i+1;
//								xx=xx+"  "+xh+".<a href='"+url+"/wechat/faq?id="+wz+"'>"+bt+"</a>\n";
//							}
//						}else {
//							List<FaqxxDto> gdfaqxxlist=faqxxService.getFaqxxgd(faqxxDto);
//							xx="很抱歉,没有找到您想要咨询的问题!\n业务相关请关注【杰毅生物】公众号，业务咨询请拨打400-0888365\n";
//							for(int i=0;i<gdfaqxxlist.size();i++) {
//								wz=gdfaqxxlist.get(i).getWz();
//								bt=gdfaqxxlist.get(i).getBt();
//								xh=i+1;
//								xx=xx+"  "+xh+".<a href='"+url+"/wechat/faq?id="+wz+"'>"+bt+"</a>\n";
//							}
//
//
//						}
//						return XmlUtil.getInstance().textMsg(weChatTextModel.getToUserName(), weChatTextModel.getFromUserName(), xx);
//					}
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
        return null;
	}

	/**
	 * 微信返回的事件信息处理方法（杰毅生物）
	 * @param request
	 * @return
	 */
	@RequestMapping("/matridx/serviceEvent")
	@ResponseBody
	public String matridxServiceEvent(HttpServletRequest request){
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MATRIDX.getCode();
		}
		try {
			String echostr = request.getParameter("echostr");
			// 微信加密签名
			String signature = request.getParameter("signature");
			// 时间戳
			String timestamp = request.getParameter("timestamp");
			// 随机数
			String nonce = request.getParameter("nonce");
			//根据外部程序代码查询外部程序表信息
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			if(wbcxDto == null){
				log.error("未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
				return null;
			}
			log.error("杰毅生物 echostr:"+echostr);
			boolean isSuccess =weChatService.checkSignature(signature, timestamp, nonce, wbcxDto);
			if(!isSuccess) {
                return null;
            }

			log.error("杰毅生物 验证通过");
			//服务器验证
			if(StringUtil.isNotBlank(echostr)){
				// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
				return echostr;
				//其他信息
			}else{
				InputStream ins = request.getInputStream();
				Map<String, Object> result = BasicXmlReader.readXmlToMap(ins);
				String msgType = (String)result.get("MsgType");
				///如果为事件类型
				if(MessageUtil.MSGTYPE_EVENT.equals(msgType)){
					String eventId = (String)result.get("Event");
					log.error("MsgType:"+msgType + " eventId:" + eventId);
					//Event	事件类型，subscribe(订阅)、unsubscribe(取消订阅)
					if(MessageUtil.MESSAGE_SUBSCIBE.equals(eventId)){
						//增加订阅
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.subscribeMatridx(weChatTextModel, wbcxDto);
						//如果为文本消息
						weChatTextModel.setGzpt(wbcxDto.getWbcxmc());
						if(weChatService.textDeal(weChatTextModel, wbcxDto,msgType)){
//							String content = weChatTextModel.getContent();//content未被使用，故注释，2023/10/26
							FaqxxDto faqxxDto = new FaqxxDto();
							faqxxDto.setBt("subscribe");
							faqxxDto.setGzpt(wbcxDto.getWbcxid());
							List<FaqxxDto> faqxxlist = faqxxService.getFqaxxByBt(faqxxDto);
							if(faqxxlist != null && faqxxlist.size() > 0) {
								String xx = faqxxlist.get(0).getNr();
								return XmlUtil.getInstance().textMsg(weChatTextModel.getToUserName(), weChatTextModel.getFromUserName(), xx);
							}
						}
					}else if(MessageUtil.MESSAGE_UNSUBSCIBE.equals(eventId)){
						//取消订阅
						WeChatTextModel weChatTextModel= packagTextModel(result);
						weChatService.unsubscribeMatridx(weChatTextModel, wbcxDto);
					}
					//elseif内内容为空，故注释，2023/10/26
//					else if(MessageUtil.MESSAGE_CLICK.equals(eventId)){
//						//点击菜单拉取消息时的事件推送
//					}else if(MessageUtil.MESSAGE_VIEW.equals(eventId)){
//						//点击菜单跳转链接时的事件推送
//					}
				}else if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
					//如果为文本消息，发送rabbit-钉钉消息通知
					WeChatTextModel weChatTextModel = packagTextModel(result);
					log.error("MsgType:"+msgType + " eventId:" + (String)result.get("Content"));
					weChatTextModel.setGzpt(wbcxDto.getWbcxmc());
					if(weChatService.textDeal(weChatTextModel, wbcxDto,msgType)){
						String content = weChatTextModel.getContent();
						FaqxxDto faqxxDto = new FaqxxDto();
						faqxxDto.setBt(content);
						faqxxDto.setGzpt(wbcxDto.getWbcxid());
						List<FaqxxDto> faqxxlist = faqxxService.getFqaxxByBt(faqxxDto);
						if(faqxxlist != null && faqxxlist.size() > 0) {
							String xx = faqxxlist.get(0).getNr();
							return XmlUtil.getInstance().textMsg(weChatTextModel.getToUserName(), weChatTextModel.getFromUserName(), xx);
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 微信订阅处理（用于测试）
	 * @return
	 */
	@RequestMapping("/subscribe")
	@ResponseBody
	public String subscribe(){

		try {
			weChatService.test();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return WeChatUtils.getTokenInfo(ProgramCodeEnum.MDINSPECT.getCode(),redisUtil);
	}

	/**
	 * 收样确认显示页面(微信端的收样确认页面)
	 * @param code
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sampleConfirmByUserAuth")
	public ModelAndView sampleConfirmByUserAuth(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
												HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspConfirm");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		String organ = request.getParameter("organ");
		SjxxDto sjxxDto = weChatService.getInspectionInfoByUserAuth(code, state, wbcxdm, organ);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("wbcxdm", wbcxdm);
		String sign = commonService.getSign();
		mav.addObject("sign", sign);
		return mav;
	}

	/**
	 * 收样确认点击事件处理
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/sampleConfirm")
	@ResponseBody
	public Map<String, Object> sampleConfirm(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		boolean checkSign = commonService.checkSign(sjxxDto.getSign(),request);
		if(!checkSign){
			map.put("status","fail");
			map.put("message",xxglService.getModelById("ICOMM_SJ00013").getXxnr());
			return map;
		}
		boolean isSuccess = sjxxService.sampAcceptConfirm(sjxxDto);

		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());

		return map;
	}

	/**
	 * 送检清单显示页面
	 * @param code
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/reportlistByUserAuth")
	public ModelAndView reportlistByUserAuth(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
											 String flg, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportList");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm",wbcxdm);
		SjxxDto sjxxDto = new SjxxDto();
		String organ = request.getParameter("organ");
		if(StringUtil.isNotBlank(organ)){
			sjxxDto.setWxid(organ);
		}else{
			WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code,state, wbcxdm);
			if(userModel != null) {
				sjxxDto.setWxid(userModel.getOpenid());
			}
		}
		mav.addObject("flg", flg);
		mav.addObject("sjxxDto", sjxxDto);
		String sign = commonService.getSign();
		mav.addObject("sign", sign);
		mav.addObject("ywlx", BusinessTypeEnum.SJ.getCode());
		return mav;
	}

	/**
	 * 送检清单显示页面(医生)
	 * @param code
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/reportlistByUserAuthDoctor")
	public ModelAndView reportlistByUserAuthDoctor(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
												   String flg, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportList");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm",wbcxdm);
		SjxxDto sjxxDto = new SjxxDto();
		String organ = request.getParameter("organ");
		if(StringUtil.isNotBlank(organ)){
			sjxxDto.setWxid(organ);
		}else{
			WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code, state, wbcxdm);
			if(userModel != null) {
				sjxxDto.setWxid(userModel.getOpenid());
			}
		}
		mav.addObject("flg", flg);
		mav.addObject("sjxxDto", sjxxDto);
		String sign = commonService.getSign();
		mav.addObject("sign", sign);

		return mav;
	}

	/**
	 * 小程序调用清单前 获取sign
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getSign")
	@ResponseBody
	public Map<String, Object> getSign(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String userid = request.getParameter("userid");
		if (StringUtil.isNotBlank(userid)){
			String sign = commonService.getSign();
			map.put("status","success");
			map.put("sign",sign);
		}else {
			map.put("status","fail");
			map.put("message","请求错误！");
		}
		return map;
	}


	/**
	 * 送检清单信息
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/reportlist")
	@ResponseBody
	public Map<String, Object> reportlist(SjxxDto sjxxDto,	HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		Map<String,Object> searchMap = new HashMap<>();
		List<SjxxDto> sjxxDtos = null;
		boolean checkSign = commonService.checkSign(sjxxDto.getSign(),request);
		if(!checkSign){
			map.put("total", sjxxDto.getTotalNumber());
			map.put("rows", sjxxDtos);
			return map;
		}
		//根据微信ID获取录入人员列表
		if(StringUtil.isNotBlank(sjxxDto.getWxid()) || StringUtil.isNotBlank(sjxxDto.getDdid())){
			List<String> lrrylist = sjysxxService.getLrrylist(sjxxDto.getWxid(), sjxxDto.getDdid());
			sjxxDto.setLrrys(lrrylist);
			/*sjqx表中暂无数据，故注释 2023.2.2*/
			/*if (StringUtil.isNotBlank(sjxxDto.getWxid())){
				// 判断送检查看权限：1.根据wxid查询unionid；2.根据unionid查询单位科室信息
				WxyhDto wxyhDto = wxyhService.getDtoById(sjxxDto.getWxid());
				if(wxyhDto!=null && StringUtil.isNotBlank(wxyhDto.getUnionid())) {
					SjqxDto sjqxDto = new SjqxDto();
					sjqxDto.setWxid(wxyhDto.getUnionid());
					List<SjqxDto> sjqxList = sjqxService.getDtoList(sjqxDto);
					sjxxDto.setSjqxDtos(sjqxList);
					searchMap.put("sjqxDtos",sjxxDto.getSjqxDtos());
				}
			}*/
			if (StringUtil.isNotBlank(request.getParameter("sfwsbj"))){
				if ("false".equals(request.getParameter("sfwsbj"))){
					searchMap.put("sfwsbj","false");
				}
			}
			searchMap.put("lrrys",sjxxDto.getLrrys());
			searchMap.put("sortOrder",sjxxDto.getSortOrder());
			searchMap.put("sortName",sjxxDto.getSortName());
			searchMap.put("pageNumber",sjxxDto.getPageNumber());
			searchMap.put("pageSize",sjxxDto.getPageSize());
			searchMap.put("pageStart",(sjxxDto.getPageNumber()-1)*sjxxDto.getPageSize());
			searchMap.put("cyrq",sjxxDto.getCyrq());
			searchMap.put("jsrq",sjxxDto.getJsrq());
			searchMap.put("entire",sjxxDto.getEntire());
			searchMap.put("sfjeStart",request.getParameter("sfjeStart"));
			searchMap.put("sfjeEnd",request.getParameter("sfjeEnd"));
			if (StringUtil.isNotBlank(request.getParameter("jcxmdms"))) {
				String str = request.getParameter("jcxmdms");
				String[] jcxmdms = str.split(",");
				List<String> jcxms=new ArrayList<>();
				List<JcsjDto> jcxmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				for (JcsjDto jcsjDto:jcxmlist){
					for (String s:jcxmdms){
						if (jcsjDto.getCsdm().equals(s)){
							jcxms.add(jcsjDto.getCsid());
						}
					}
				}
				searchMap.put("jcxms",jcxms);
			}else if (StringUtil.isNotBlank(request.getParameter("disabledjcxmdms"))) {
				String str = request.getParameter("disabledjcxmdms");
				String[] jcxmdms = str.split(",");
				List<String> jcxms=new ArrayList<>();
				List<JcsjDto> jcxmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				for (JcsjDto jcsjDto:jcxmlist){
					for (String s:jcxmdms){
						if (!jcsjDto.getCsdm().equals(s)){
							jcxms.add(jcsjDto.getCsid());
						}
					}
				}
				searchMap.put("jcxms",jcxms);
			}
			sjxxDtos = sjxxService.getListWithMap(searchMap);
			if(sjxxDtos != null && sjxxDtos.size() > 0){
				for (int i = 0; i < sjxxDtos.size(); i++) {
					sjxxDtos.get(i).setSign(commonService.getSign(sjxxDtos.get(i).getYbbh()));
				}
			}
		}
		map.put("total", sjxxDto.getTotalNumber());
		map.put("rows", sjxxDtos);
		//需要筛选钉钉字段的，请调用该方法
		screenClassColumns(request,map);
		return map;
	}
	/**
	 * 异步获取基础数据子类别
	 * @return
	 */
	@RequestMapping(value ="data/ansyGetJcsjList")
	@ResponseBody
	public List<JcsjDto> ansyGetJcsjList(JcsjDto jcsjDto){

		return jcsjService.getJcsjDtoList(jcsjDto);
	}

	/**
	 * 送检页面 (有网页授权版)
	 * @return
	 */
	@RequestMapping(value="/reportViewByUserAuth")
	public ModelAndView reportViewByUserAuth(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
											 HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspReport");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm",wbcxdm);
		SjxxDto sjxxDto;
		String organ = request.getParameter("organ");
		sjxxDto = weChatService.getReportInfoByUserAuth(code, state, wbcxdm, organ);
		if(sjxxDto == null){
			mav = null;
			return mav;
		}
		// 查询最后一次送检数据    mod 因线上问题，取消查询 2021-06-15 li
		/*sjxxDto.setLrry(sjxxDto.getWxid());
		SjxxDto t_sjxxDto = sjxxService.getLastInfo(sjxxDto);
		if(StringUtil.isNotBlank(t_sjxxDto.getSjid()) && !"80".equals(t_sjxxDto.getZt())) {
			String wxid = sjxxDto.getWxid();
			sjxxDto = sjxxService.getDto(t_sjxxDto, 0);
			sjxxDto.setWxid(wxid);
			//根据文件ID查询附件表信息
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("fjcfbDtos", fjcfbDtos);
		}*/

		Map<String, List<JcsjDto>> g_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.GENDER_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.PATHOGENY_TYPE,
				BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.INSPECTION_DIVISION});
		mav.addObject("genderlist", g_jclist.get(BasicDataTypeEnum.GENDER_TYPE.getCode()));
		mav.addObject("detectlist", g_jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
		mav.addObject("pathogenylist", g_jclist.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		mav.addObject("sdlist", g_jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));
		mav.addObject("zmmddlist" , g_jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		mav.addObject("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
		mav.addObject("divisionlist", g_jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		mav.addObject("ksxxlist", sjdwlist);
		//获取文件类型
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/*新冠、送检 选择界面*/
	@RequestMapping(value="/ViewChoose")
	public ModelAndView ViewChoose(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/viewChoose_Index");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wbcxdm);
		wbcxDto = wbcxService.getDto(wbcxDto);
		mav.addObject("wbcxdm", wbcxdm);
		mav.addObject("gzpt", wbcxDto.getGzpt());
		SjxxDto sjxxDto = new SjxxDto();
		String organ = request.getParameter("organ");
		if(StringUtil.isNotBlank(organ)){
			sjxxDto.setWxid(organ);
		}else{
			WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code,state, wbcxdm);
			if(userModel != null) {
				sjxxDto.setWxid(userModel.getOpenid());
			}
		}
		DBEncrypt dbEncrypt = new DBEncrypt();
		String prefixUrl = dbEncrypt.dCode(applicationurl);
		if (!prefixUrl.endsWith("/")){
			prefixUrl = prefixUrl + "/";
		}
		mav.addObject("prefixUrl", prefixUrl);
		mav.addObject("wxid",sjxxDto.getWxid());//微信id
		mav.addObject("code", code);
		mav.addObject("state",state);
		mav.addObject("request",request);
		return mav;
	}

	/*根据wxid查询微信用户的绑定情况*/
	@RequestMapping(value="/getUserInfoByWxid")
	@ResponseBody
	public Map<String,Object> getUserInfoByWxid(String wxid,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("code", "success");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm",wbcxdm);
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wbcxdm);
		WbcxDto wbcx = wbcxService.getDto(wbcxDto);
		DBEncrypt dbEncrypt = new DBEncrypt();
		String appid = dbEncrypt.dCode(wbcx.getAppid());
		map.put("appid",appid);

		WxyhDto wxyhDto = new WxyhDto();
		wxyhDto.setWxid(wxid);
		wxyhDto.setWbcxdm(wbcxdm);
		wxyhDto.setGzptmc(wbcx.getGzpt());
		List<WxyhDto> wxyhlist = wxyhService.getWxyhListByWxid(wxyhDto);
		if (wxyhlist.size() > 0){
			if (wxyhlist.size() > 1){
				for (WxyhDto wxyhDto_t : wxyhlist) {
					if (StringUtils.isNotBlank(wxyhDto_t.getWxm())){
						map.put("flag", false);//有多个微信用户，返回的是有用户名的
						map.put("wxyhDto",wxyhDto_t);
						return map;
					}
				}
				if (StringUtils.isNotBlank(wxyhDto.getSj())){
					map.put("wxyhDto",wxyhDto);
					map.put("flag",true);//有微信用户，但无用户名
				}

			}else {
				WxyhDto wxyhDto_t = wxyhlist.get(0);
				if (StringUtils.isNotBlank(wxyhDto_t.getWxm())){
					map.put("flag", false);//有微信用户，有用户名，无需请求
				}else {
					map.put("flag",true);//有微信用户，但无用户名，需要请求
				}
				map.put("wxyhDto",wxyhDto_t);
				return map;
			}
		}else {
			map.put("flag",true);
			return map;
		}
		return map;
	}

	/**
	 * 通过用户授权，获取微信用户信息，并跳转指定页面
	 * @param code
	 * @param toUrl
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/getWechatUserInfo")
	@ResponseBody
	public ModelAndView getWechatUserInfo(String code,String toUrl,HttpServletRequest request) throws BusinessException {
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wbcxdm);
		WbcxDto wbcx = wbcxService.getDto(wbcxDto);
		WeChatUserModel weChatUserModel = WeChatUtils.getUserDetailInfoByLink(restTemplate, code, wbcx,redisUtil);
		log.error("授权获取微信用户信息: " + JSON.toJSONString(weChatUserModel));
		//更新微信信息
		WxyhDto wxyhDto = new WxyhDto();
		wxyhDto.setWxid(weChatUserModel.getOpenid());
		String wxm;
		try {
			if(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(weChatUserModel.getNickname())){
				wxm = new String(weChatUserModel.getNickname().getBytes("ISO-8859-1"), "UTF-8");
				wxyhDto.setWxm(wxm);
			}else{
				wxyhDto.setWxm(weChatUserModel.getNickname());
			}

		} catch (UnsupportedEncodingException e) {
			log.error("转码错误");
			wxyhDto.setWxm(weChatUserModel.getNickname());
			e.printStackTrace();
		}
		wxyhDto.setWbcxdm(wbcxdm);
		wxyhDto.setGzpt(wbcx.getWbcxid());
		wxyhDto.setGzptmc(wbcx.getGzpt());
		if (StringUtils.isNotBlank(weChatUserModel.getUnionid())){
			wxyhDto.setUnionid(weChatUserModel.getUnionid());
		}
		wxyhService.authorize(wxyhDto);
		MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
		Set<String> strings = request.getParameterMap().keySet();
		for (String key : strings) {
			if (!"toUrl".equals(key) && !"code".equals(key) && !"state".equals(key)){
				requestMap.add(key,request.getParameter(key));
			}
		}
		requestMap.add("wbcxdm",wbcxdm);
		requestMap.add("wxid",wxyhDto.getWxid());
		DBEncrypt dbEncrypt = new DBEncrypt();
		String prefixUrl = dbEncrypt.dCode(applicationurl);
		if (!prefixUrl.endsWith("/")){
			prefixUrl = prefixUrl + "/";
		}
		Map<String,Object> map = restTemplate.postForObject( prefixUrl + toUrl, requestMap, Map.class);
		ModelAndView mav = new ModelAndView(map.get("mavUrl").toString());
		for (String s : map.keySet()) {
			if (!"mavUrl".equals(s)){
				mav.addObject(s,map.get(s));
			}
		}
		return mav;
	}

	/*送检系统选择页面获取微信授权后跳转*/
	@RequestMapping(value="/viewChooseByUserAuthWithAuthorize")
	@ResponseBody
	public Map<String,Object> viewChooseByUserAuthWithAuthorize(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("mavUrl","wechat/informed/wechat_inspIndex");
		//若有jcxmdms，说明有限制使用的jcxm，将除jcxmdms外的项目排除
		//若无，判断是否存在禁用检测项目disabledjcxmdms，将disabledjcxmdms中的项目排除
		if (StringUtil.isNotBlank(request.getParameter("jcxmdms"))){
			map.put("jcxmdms",request.getParameter("jcxmdms"));
		}else if(StringUtil.isNotBlank(request.getParameter("disabledjcxmdms"))){
			map.put("disabledjcxmdms",request.getParameter("disabledjcxmdms"));
		}
		String flag = request.getParameter("flag");
		if (StringUtil.isNotBlank(flag)){
			map.put("flag",flag);
		}
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm",wbcxdm);
		map.put("wxid", wxid);
		return map;
	}
	/*送检系统选择页面 （无code）*/
	@RequestMapping(value="/reportViewChooseByUserAuthWithoutCode")
	public ModelAndView reportViewChooseByUserAuthWithoutCode(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspIndex");
		//若有jcxmdms，说明有限制使用的jcxm，将除jcxmdms外的项目排除
		//若无，判断是否存在禁用检测项目disabledjcxmdms，将disabledjcxmdms中的项目排除
		if (StringUtil.isNotBlank(request.getParameter("jcxmdms"))){
			mav.addObject("jcxmdms",request.getParameter("jcxmdms"));
		}else if(StringUtil.isNotBlank(request.getParameter("disabledjcxmdms"))){
			mav.addObject("disabledjcxmdms",request.getParameter("disabledjcxmdms"));
		}
		String flag = request.getParameter("flag");
		if (StringUtil.isNotBlank(flag)){
			mav.addObject("flag",flag);
		}
		mav.addObject("wxid", wxid);
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm", wbcxdm);
		log.error("reportViewChooseByUserAuthWithoutCode。Wxid =" + wxid);
		mav.addObject("request",request);
		return mav;
	}


	/*送检系统选择页面*/
	@RequestMapping(value="/reportViewChooseByUserAuth")
	public ModelAndView reportViewChooseByUserAuth(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspIndex");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm",wbcxdm);
		SjxxDto sjxxDto = new SjxxDto();
		String organ = request.getParameter("organ");
		if(StringUtil.isNotBlank(organ)){
			sjxxDto.setWxid(organ);
		}else{
			WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code,state, wbcxdm);
			if(userModel != null) {
				sjxxDto.setWxid(userModel.getOpenid());
			}
		}
		mav.addObject("wxid", sjxxDto.getWxid());
		mav.addObject("request",request);

		log.error("reportViewChooseByUserAuth。wxid =" + sjxxDto.getWxid());
		return mav;
	}

	/**
	 * 快捷支付页面 微信公众号
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspFastReport")
	public ModelAndView inspFastReport(HttpServletRequest request, SjxxDto sjxxDto) {
		String wxid = request.getParameter("wxid");
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspFastReport");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(wxid);
		sjxxDto.setWxid(wxid);
		if (sjkjxxDto != null) {
			sjxxDto.setDb(sjkjxxDto.getDbm());
		}
		mav.addObject("wbcxdm",wbcxdm);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("wxid", wxid);
		mav.addObject("flag", request.getParameter("flag"));
		mav.addObject("ywlx", BusinessTypeEnum.SJ.getCode());
		log.error("快捷支付页面 。wxid =" + wxid);
		return mav;
	}

	/**
	 * 送检系统正常录入页面 微信公众号 mav
	 * @param wxid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/inspReport")
	public ModelAndView inspReport(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
		Map<String, Object> map = inspReportMap(wxid, request);
		ModelAndView mav = new ModelAndView((String) map.get("mavUrl"));
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 送检系统正常录入页面 微信公众号 map
	 * @param wxid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/inspReportMap")
	@ResponseBody
	public Map<String, Object> inspReportMap(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
		String flag = request.getParameter("flag");
		if (StringUtil.isNotBlank(flag)){
			if ("ResFirst".equals(flag)){
				return resFirstReportMap(wxid,request);
			}
		}
		String jcxmdms = request.getParameter("jcxmdms");
		Map<String, Object> map = new HashMap<>();
		map.put("mavUrl","wechat/informed/wechat_inspReport");
		map.put("jcxmdms", jcxmdms);//限制检测项目，若不传，则不限制
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm", wbcxdm);
		SjxxDto sjxxDto = new SjxxDto();
		String organ = request.getParameter("organ");
		// 送检信息里的录入id采用 微信用户表里的用户id
		SjkjxxDto sjkjxxDto = new SjkjxxDto();
		if(StringUtil.isBlank(organ)){
			// 根据点击信息获取用户的openid
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			if(wbcxDto == null){
				log.error("未获取到外部程序编码  "+wbcxdm +" 对应的外部程序信息！");
				return null;
			}
			// 根据wxid在 送检快捷信息表里获取信息 并放在页面上
			sjkjxxDto = sjkjxxService.getDtoById(wxid);
			sjxxDto.setWxid(wxid);
		}else{
			sjkjxxDto = sjkjxxService.getDtoById(organ);
			sjxxDto.setWxid(organ);
		}
		if (sjkjxxDto != null) {
			if(StringUtil.isNotBlank(sjkjxxDto.getSjdwmc())) {
				sjxxDto.setSjdw(sjkjxxDto.getSjdw());
				sjxxDto.setSjdwbj(sjkjxxDto.getSjdwbj());
				sjxxDto.setYymc(sjkjxxDto.getSjdwmc());
				sjxxDto.setSjdwmc(sjkjxxDto.getSjdwmc());
			}
			sjxxDto.setKs(sjkjxxDto.getKs());
			sjxxDto.setQtks(sjkjxxDto.getQtks());
			sjxxDto.setJcdw(sjkjxxDto.getJcdw());
			sjxxDto.setSjys(sjkjxxDto.getSjys());
			sjxxDto.setYsdh(sjkjxxDto.getYsdh());
			sjxxDto.setDb(sjkjxxDto.getDbm());
		}
		map.put("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
		map.put("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		map.put("sdlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
		map.put("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
		map.put("divisionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);
		//获取文件类型
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		map.put("sjxxDto", sjxxDto);
		map.put("flag", flag);
		return map;
	}
	/**
	 * ResFirst录入页面mav
	 * @param wxid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resFirstReport")
	public ModelAndView resFirstReport(@RequestParam(name = "wxid", required = false) String wxid,HttpServletRequest request){
		Map<String, Object> map = resFirstReportMap(wxid, request);
		ModelAndView mav = new ModelAndView((String) map.get("mavUrl"));
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * ResFirst录入页面map
	 * @param wxid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resFirstReportMap")
	@ResponseBody
	public Map<String, Object> resFirstReportMap(@RequestParam(name = "wxid", required = false) String wxid,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		map.put("mavUrl", "wechat/informed/wechat_resFirstReport");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm", wbcxdm);
		map.put("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
		map.put("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		map.put("sdlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
		map.put("divisionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setWxid(wxid);
		//根据检测项目代码限制标本类型
		List<JcsjDto> samplelist=redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		map.put("samplelist", samplelist);
		map.put("clinicallist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLINICAL_TYPE.getCode()));
		map.put("invoicelist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		map.put("jzxmlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));

		// 获取收费权限信息
		String sfqx = null;
		map.put("sfqx", sfqx);
		//获取文件类型
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		String jcxmdms = request.getParameter("jcxmdms");
		map.put("jcxmdms", jcxmdms);
		map.put("sjxxDto", sjxxDto);
		map.put("flag", request.getParameter("flag"));
		return map;
	}

	/**
	 * 微信送检录入页面获取基础数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getInspBasicData")
	@ResponseBody
	public Map<String, Object> getInspBasicData(String flag,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		map.put("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		map.put("sdlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
		map.put("zmmddlist" , redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		map.put("divisionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		List<JcsjDto> jcxmList=  redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> jcxmDtos=new ArrayList<>();
		if("ResFirst".equals(flag)){
			if(jcxmList!=null&&jcxmList.size()>0){
				for(JcsjDto dto:jcxmList){
					if("F".equals(dto.getCskz1())){
						jcxmDtos.add(dto);
					}
				}
			}
		}else{
			jcxmDtos=jcxmList;
		}
		map.put("detectlist", jcxmDtos);
		map.put("kylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));
		map.put("detectsublist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);
		List<JcsjDto> unitlist = sjxxService.getDetectionUnit(request.getParameter("dbm"));
		map.put("unitlist", unitlist);
		return map;
	}

	/*数据完善显示页面（根据送检清单显示页面修改）*/
	@RequestMapping(value="/inspPerfectReport")
	public ModelAndView inspPerfectReport(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportList");
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setWxid(wxid);
		//若有jcxmdms，说明有限制使用的jcxm，将除jcxmdms外的项目排除
		//若无，判断是否存在禁用检测项目disabledjcxmdms，将disabledjcxmdms中的项目排除
		if (StringUtil.isNotBlank(request.getParameter("jcxmdms"))){
			mav.addObject("jcxmdms",request.getParameter("jcxmdms"));
		}else if(StringUtil.isNotBlank(request.getParameter("disabledjcxmdms"))){
			mav.addObject("disabledjcxmdms",request.getParameter("disabledjcxmdms"));
		}
		String flag = request.getParameter("flag");
		if (StringUtil.isNotBlank(flag)){
			mav.addObject("flag", flag);
		}
		mav.addObject("sjxxDto", sjxxDto);
		String sign = commonService.getSign();
		mav.addObject("sign", sign);
		mav.addObject("ywlx", BusinessTypeEnum.SJ.getCode());
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm", wbcxdm);
		return mav;
	}

	/**
	 * 获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/getInspectionInfo")
	@ResponseBody
	public Map<String, Object> getInspectionInfo(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		SjxxDto re_sjxxDto = sjxxService.getDto(sjxxDto);
		if(re_sjxxDto != null){
			//获取临床症状，检测项目
			List<SjlczzDto> sjlczzDtos = sjlczzService.selectLczzBySjid(re_sjxxDto);
			map.put("clinicallist", sjlczzDtos);
			List<SjjcxmDto> sjjcxmDtos = sjjcxmService.selectJcxmBySjid(re_sjxxDto);
			map.put("sjjcxmDtos", sjjcxmDtos);

			//前端需要查询收费信息，然后送检信息里也显示为未收费的时候，查询相应的金额
			if(StringUtil.isNotBlank(sjxxDto.getSfflg()) && !"1".equals(re_sjxxDto.getFkbj())) {
				if(StringUtil.isBlank(re_sjxxDto.getFkje())){
					SjhbxxDto sjhbxxDto = new SjhbxxDto();
					sjhbxxDto.setSjxms(sjjcxmDtos);
					sjhbxxDto.setHbmc(re_sjxxDto.getDb());
					SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
					if(re_sjhbxxDto!=null) {
                        re_sjxxDto.setFy(re_sjhbxxDto.getSfbz());
                    }
				}else{
					re_sjxxDto.setFy(re_sjxxDto.getFkje());
				}

			}else if("1".equals(re_sjxxDto.getFkbj())){
				re_sjxxDto.setFy("该项目已付款 ");
			}
			map.put("unitlist",  sjxxService.getDetectionUnit(re_sjxxDto.getDb()));
			map.put("subdetectlist", sjxxService.getSubDetect(re_sjxxDto.getJcxm()));
			//根据标本类型cskz2限制检测项目
			JcsjDto jc_yblxDto=new JcsjDto();
			if(StringUtils.isNotBlank(re_sjxxDto.getYblx())){
				jc_yblxDto=jcsjService.getDtoById(re_sjxxDto.getYblx());
			}
			List<JcsjDto> detectlist = redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE));
			List<JcsjDto> t_detectlist= new ArrayList<>();
			if(StringUtils.isNotBlank(jc_yblxDto.getCskz2())) {
				if(detectlist!=null && detectlist.size()>0) {
					//原循环add改为addAll,2023/10/26
					t_detectlist.addAll(detectlist);
				}
			}else {
				t_detectlist=detectlist;
			}
			map.put("detectlist", t_detectlist);
		}
		map.put("sjxxDto", re_sjxxDto);
		map.put("status", re_sjxxDto != null?"success":"fail");
		map.put("message", re_sjxxDto != null?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 保存送检第一步信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/addSaveReport")
	public ModelAndView addSaveReport(SjxxDto sjxxDto){
		String wxid = sjxxDto.getWxid();
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspConsent");
		try {
			log.error("saveInspectFirst wxid:" + sjxxDto.getWxid() + " ddid:" + sjxxDto.getDdid());

			boolean isSuccess = sjxxService.addSaveReport(sjxxDto);
			if(isSuccess) {
				sjxxService.saveFile(sjxxDto);
				mav.addObject("status", "success");
				sjxxDto = sjxxService.getDto(sjxxDto,1);
				//根据检测项目代码限制标本类型
				List<JcsjDto> samplelist=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
				List<JcsjDto> t_samplelist= new ArrayList<>();
				if(samplelist!=null && samplelist.size()>0) {
					List<SjjcxmDto> sjjcxmDtos=sjjcxmService.selectJcxmBySjid(sjxxDto);
					for(int i=0;i<samplelist.size();i++) {
						if(StringUtils.isNotBlank(samplelist.get(i).getCskz2())) {
							boolean isSuccess_t=true;
							if (!CollectionUtils.isEmpty(sjjcxmDtos)){
								for (SjjcxmDto sjjcxmDto:sjjcxmDtos){
									if (!samplelist.get(i).getCskz2().contains(sjjcxmDto.getCsdm())){
										isSuccess_t=false;
										break;
									}
								}
							}
							if (isSuccess_t){
								t_samplelist.add(samplelist.get(i));
							}
						}
					}
					String jcxmdms = "";
					for (SjjcxmDto sjjcxmDto : sjjcxmDtos) {
						jcxmdms = jcxmdms + "," + sjjcxmDto.getCsdm();
					}
					if (jcxmdms.length()>0){
						jcxmdms = jcxmdms.substring(1);
					}
					mav.addObject("jcxmdms", jcxmdms);
				}
				mav.addObject("samplelist", t_samplelist);
				mav.addObject("clinicallist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.CLINICAL_TYPE.getCode()));
				mav.addObject("invoicelist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
				// 获取收费权限信息
				String sfqx = null;
				/*MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
				paramMap.add("wxid", wxid);
				DBEncrypt crypt = new DBEncrypt();
				String url = crypt.dCode(companyurl);
				@SuppressWarnings("unchecked")
				Map<String,String> map = restTemplate.postForObject(url+"/ws/dingtalk/getUser", paramMap, Map.class);
				if(map != null && StringUtil.isNotBlank(map.get("sfqx")))
					sfqx = map.get("sfqx");*/
				mav.addObject("sfqx", sfqx);
			}else{
				mav = new ModelAndView("wechat/informed/wechat_inspReport");
				mav.addObject("status", "fail");
				mav.addObject("message", xxglService.getModelById("ICOM00002").getXxnr());
				mav.addObject("genderlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
				mav.addObject("detectlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));
				mav.addObject("pathogenylist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
				mav.addObject("sdlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
				mav.addObject("zmxmlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SELFEXEMPTION_PROJECT.getCode()));
				mav.addObject("zmmddlist" ,redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));
				mav.addObject("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
				mav.addObject("divisionlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
				List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
				mav.addObject("ksxxlist", sjdwlist);
			}
		}catch (BusinessException e) {

			mav = new ModelAndView("wechat/informed/wechat_inspReport");

			mav.addObject("status", "fail");
			mav.addObject("message", e.getMsg());
			mav.addObject("genderlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
			mav.addObject("detectlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));
			mav.addObject("pathogenylist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
			mav.addObject("sdlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
			mav.addObject("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
			mav.addObject("divisionlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
			List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
			mav.addObject("ksxxlist", sjdwlist);
		}catch(Exception e){
			mav = new ModelAndView("wechat/informed/wechat_inspReport");
			mav.addObject("status", "fail");
			mav.addObject("message", e.getMessage());
			mav.addObject("genderlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
			mav.addObject("detectlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));
			mav.addObject("pathogenylist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
			mav.addObject("sdlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
			mav.addObject("zmxmlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SELFEXEMPTION_PROJECT.getCode()));
			mav.addObject("zmmddlist" ,redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));
			mav.addObject("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
			mav.addObject("divisionlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
			List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
			mav.addObject("ksxxlist", sjdwlist);
		}
		sjxxDto.setWxid(wxid);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 保存送检第一步信息(返回map)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/addSaveReportReturnMap")
	@ResponseBody
	public Map<String,Object> addSaveReportReturnMap(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		//String wxid = sjxxDto.getWxid();
		//String pageUrl = "wechat/informed/wechat_inspConsent";
		try {
			log.error("saveInspectFirst wxid:" + sjxxDto.getWxid() + " ddid:" + sjxxDto.getDdid());
			if(StringUtil.isBlank(sjxxDto.getWxid()) && StringUtil.isNotBlank(sjxxDto.getDdid())){
				sjxxDto.setWxid(sjxxDto.getDdid());
			}
			boolean isSuccess = sjxxService.addSaveReport(sjxxDto);
			if(isSuccess) {
				sjxxService.saveFile(sjxxDto);
				map.put("status", "success");
				// 获取收费权限信息
				String sfqx = null;
				map.put("sfqx", sfqx);
				map.put("sjxxDto", sjxxDto);
			}else{
				//pageUrl = "/wechat/goToSecondPage";
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			}
		}catch(Exception e){
			map.put("status", "fail");
			map.put("message", e.getMessage());
		}
		return map;
	}

	/**
	 * 跳转送检录入第二页
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/goToSecondPage")
	public ModelAndView goToSecondPage(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = goToSecondPageMap(sjxxDto, request);
		String pageUrl = (String) map.get("pageUrl");
		ModelAndView mav = new ModelAndView(pageUrl);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 跳转送检录入第二页
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/goToSecondPageMap")
	@ResponseBody
	public Map<String, Object> goToSecondPageMap(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String wxid = sjxxDto.getWxid();
		String pageUrl = "wechat/informed/wechat_inspConsent";
		map.put("pageUrl", pageUrl);
		sjxxDto = sjxxService.getDto(sjxxDto,1);
		//根据检测项目代码限制标本类型
		List<JcsjDto> samplelist=redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<JcsjDto> t_samplelist= new ArrayList<>();
		if(samplelist!=null && samplelist.size()>0) {
			List<SjjcxmDto> sjjcxmDtos=sjjcxmService.selectJcxmBySjid(sjxxDto);
			for(int i=0;i<samplelist.size();i++) {
				if(StringUtils.isNotBlank(samplelist.get(i).getCskz2())) {
					boolean isSuccess_t=true;
					if (!CollectionUtils.isEmpty(sjjcxmDtos)){
						for (SjjcxmDto sjjcxmDto:sjjcxmDtos){
							if (!samplelist.get(i).getCskz2().contains(sjjcxmDto.getCsdm())){
								isSuccess_t=false;
								break;
							}
						}
					}
					if (isSuccess_t){
						t_samplelist.add(samplelist.get(i));
					}
				}
			}
			String jcxmdms = "";
			for (SjjcxmDto sjjcxmDto : sjjcxmDtos) {
				jcxmdms = jcxmdms + "," + sjjcxmDto.getCsdm();
			}
			if (jcxmdms.length()>0){
				jcxmdms = jcxmdms.substring(1);
			}
			map.put("jcxmdms", jcxmdms);
		}
		map.put("samplelist", t_samplelist);
		map.put("clinicallist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLINICAL_TYPE.getCode()));
		map.put("invoicelist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		// 获取收费权限信息
		String sfqx = null;
		map.put("sfqx", sfqx);
		sjxxDto.setWxid(wxid);
		map.put("sjxxDto", sjxxDto);
		map.put("ywlx", BusinessTypeEnum.SJ.getCode());
		String actionFlag = request.getParameter("actionFlag");
		map.put("actionFlag", actionFlag);
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm", wbcxdm);
		return map;
	}

	/**
	 * 保存知情同意书信息，送检第二步信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/addSaveConsentMap")
	@ResponseBody
	public Map<String,Object> addSaveConsentMap(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		if (sjxxDto.getSjqqjcs()==null){
			String sjqqjcs = request.getParameter("sjqqjcss");
			if (StringUtil.isNotBlank(sjqqjcs)){
				List<SjqqjcDto> list = JSONArray.parseArray(sjqqjcs, SjqqjcDto.class);
				sjxxDto.setSjqqjcs(list);
			}
		}
		//String actionFlag = request.getParameter("actionFlag");
		SjxxDto dto = sjxxService.getDto(sjxxDto);
		if (dto != null){
			if(StringUtil.isNotBlank(sjxxDto.getXgsj())&&!sjxxDto.getXgsj().equals(dto.getXgsj())){
				map.put("status", "fail");
				map.put("message", "该送检信息已被修改，请刷新页面后重新操作！");
				return map;
			}
		}
		//如果为完成按钮(即 录单必填项均已填完)
		if("1".equals(sjxxDto.getNewflg())){
			//判断相同用户标本类型是否输入重复
			SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto,1);
			t_sjxxDto.setYblx(sjxxDto.getYblx());
			List<SjxxDto> sjxxDtos = sjxxService.isYblxRepeat(t_sjxxDto);
			if(sjxxDtos != null && sjxxDtos.size() > 0){
				//若有重复则保存失败
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOMM_SJ00007").getXxnr());
				return map;
			}
			//设置状态为80,删除标记为0
			sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
			sjxxDto.setScbj("0");
		}
		//设置录入人员和修改人员，并保存
		boolean isSuccess;
		sjxxDto.setLrry(sjxxDto.getWxid());
		sjxxDto.setXgry(sjxxDto.getWxid());
		isSuccess = sjxxService.addSaveConsentComp(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/modInspection")
	public ModelAndView modInspection(SjxxDto sjxxDto, @RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
									  HttpServletRequest request){
		String flag = request.getParameter("flag");
		String actionFlag = request.getParameter("actionFlag");
		if (StringUtil.isNotBlank(flag)){
			if ("ResFirst".equals(flag)){
				return modResInspection(sjxxDto,code,state,request);
			}
		}
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspReport");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm",wbcxdm);
		sjxxDto = sjxxService.getDto(sjxxDto,0);
		if(sjxxDto == null) {
            return mav;
        }
		String organ = request.getParameter("organ");
		SjxxDto t_sjxxDto = weChatService.getReportInfoByUserAuth(code, state, wbcxdm, organ);
		if(t_sjxxDto != null){
			if(StringUtil.isNotBlank(t_sjxxDto.getWxid())) {
                sjxxDto.setWxid(t_sjxxDto.getWxid());
            } else if(StringUtil.isNotBlank(request.getParameter("wxid"))) {
				sjxxDto.setWxid(request.getParameter("wxid"));
			}
		}
		mav.addObject("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
		mav.addObject("detectlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));
		mav.addObject("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		mav.addObject("sdlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
		mav.addObject("zmxmlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SELFEXEMPTION_PROJECT.getCode()));
		mav.addObject("zmmddlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		mav.addObject("divisionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		mav.addObject("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		mav.addObject("ksxxlist", sjdwlist);
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		mav.addObject("sjxxDto", sjxxDto);
		//根据文件ID查询附件表信息
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("actionFlag", actionFlag);
		return mav;
	}

	/**
	 * 修改res送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/modResInspection")
	public ModelAndView modResInspection(SjxxDto sjxxDto, @RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
									  HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_resFirstReport");
		String actionFlag = request.getParameter("actionFlag");
		mav.addObject("actionFlag", actionFlag);
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm",wbcxdm);
		sjxxDto = sjxxService.getDto(sjxxDto,0);
		if(sjxxDto == null) {
            return mav;
        }
		String organ = request.getParameter("organ");
		SjxxDto t_sjxxDto = weChatService.getReportInfoByUserAuth(code, state, wbcxdm, organ);
		if(t_sjxxDto != null){
			if(StringUtil.isNotBlank(t_sjxxDto.getWxid())) {
                sjxxDto.setWxid(t_sjxxDto.getWxid());
            } else if(StringUtil.isNotBlank(request.getParameter("wxid"))) {
				sjxxDto.setWxid(request.getParameter("wxid"));
			}
		}
		mav.addObject("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));
		mav.addObject("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		mav.addObject("sdlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));
		mav.addObject("divisionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		mav.addObject("ksxxlist", sjdwlist);
		//根据检测项目代码限制标本类型
		List<JcsjDto> samplelist=redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		mav.addObject("samplelist", samplelist);
		mav.addObject("clinicallist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLINICAL_TYPE.getCode()));
		mav.addObject("invoicelist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		mav.addObject("jzxmlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		mav.addObject("sjxxDto", sjxxDto);
		//根据文件ID查询附件表信息
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		String jcxmdms = request.getParameter("jcxmdms");
		mav.addObject("jcxmdms", jcxmdms);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("flag",request.getParameter("flag"));
		return mav;
	}

	/**
	 * 根据输入医生姓名查询医生
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/selectDoctor")
	@ResponseBody
	public Map<String, Object> selectTaskFzr(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjysxxDto> sjysxxDtos = sjysxxService.selectBySjys(sjxxDto);
		map.put("sjysxxDtos", sjysxxDtos);
		return map;
	}

	/**
	 * 用户信息录入页面，当用户信息不完善的时候
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/userInfoView")
	public ModelAndView userInfoView(WxyhDto wxyhDto){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_userInfo");
		String sign = commonService.getSign();
		mav.addObject("sign", sign);
		mav.addObject("wxyhDto", wxyhDto);
		return mav;
	}

	/**
	 * 保存用户录入信息
	 * @return
	 */
	@RequestMapping("/modSaveWeChatUser")
	@ResponseBody
	public Map<String, Object> modSaveWeChatUser(WxyhDto wxyhDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		boolean checkSign = commonService.checkSign(wxyhDto.getSign(),request);
		if(!checkSign){
			map.put("status","fail");
			map.put("message",xxglService.getModelById("ICOMM_SJ00013").getXxnr());
			return map;
		}
		boolean isSuccess;
		try {
			if(StringUtil.isBlank(wxyhDto.getWbcxdm())){
				wxyhDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
			}
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wxyhDto.getWbcxdm());
			wbcxDto = wbcxService.getDto(wbcxDto);
			wxyhDto.setGzpt(wbcxDto.getWbcxid());
			isSuccess = wxyhService.modSaveWeChatUser(wxyhDto);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		map.put("wxyhDto", wxyhDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 用户信息修改页面
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/modUserInfoView")
	public ModelAndView modUserInfoView(WxyhDto wxyhDto, @RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_modUserInfo");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		mav.addObject("wbcxdm", wbcxdm);
		String sign = commonService.getSign();
		mav.addObject("sign", sign);
		if(StringUtil.isBlank(wxyhDto.getWxid())){
			//获取wxid(openid)
			String organ = request.getParameter("organ");
			SjxxDto t_sjxxDto = weChatService.getReportInfoByUserAuth(code, state, wbcxdm, organ);
			wxyhDto.setWxid(t_sjxxDto.getWxid());
		}
		wxyhDto = wxyhService.getDtoById(wxyhDto.getWxid());
		if(wxyhDto == null) {
            wxyhDto = new WxyhDto();
        }
		//获取身份类型
		List<IdentityTypeEnum> identitylist = wxyhService.getIdentityType();
		mav.addObject("identitylist", identitylist);
		mav.addObject("wxyhDto", wxyhDto);
		return mav;
	}

	/**
	 * 获取本地推送菜单信息，接收其他服务器推送过来的菜单信息
	 * @return
	 */
	@RequestMapping("/createMenu")
	@ResponseBody
	public Map<String, Object> createMenu(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		weChatService.createMenu(request);
		return map;
	}

	/**
	 * 获取本地微信用户信息并返回
	 * @return
	 */
	@RequestMapping("/getWeChatUserList")
	@ResponseBody
	public Map<String, Object> getWeChatUserList(HttpServletRequest request, WxyhDto wxyhDto){
		Map<String,Object> map = new HashMap<>();
		wxyhDto.setLrsj(request.getParameter("lrsj"));
		List<WxyhDto> wxyhlist = wxyhService.getDtoList(wxyhDto);
		map.put("wxyhlist", wxyhlist);
		return map;
	}

	/**
	 * 获取JS-api的临时票据信息
	 * @return
	 */
	@RequestMapping("/getJsApiInfo")
	@ResponseBody
	public Map<String, Object> getJsApiInfo(HttpServletRequest request){
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
        return weChatService.getWechatJsApiInfo(request, wbcxdm);
	}

	/**
	 * 接收本地服务器传送的医院信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/creatHis")
	@ResponseBody
	public Map<String, Object> creatHis(HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		sjdwxxService.createHis(request);
		return map;
	}

	/**
	 * 更新用户信息？？？？
	 * @return
	 */
	@RequestMapping("/updateUser")
	@ResponseBody
	public Map<String, Object> updateUser(WxyhDto wxyhdto){
		Map<String,Object> map = new HashMap<>();
		System.out.println(wxyhdto);
		return map;
	}

	/**
	 * 接收服务器传送过来的文件数据，并保存到本地服务器中(删除原有数据)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upInspReport")
	@ResponseBody
	public String upInspReport(HttpServletRequest request){

		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(request.getParts().iterator().next().getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			FjcfbModel fjcfbModel = JSONObject.parseObject(request.getParameter("fjcfbModel"),FjcfbModel.class);
			if(imp_file!=null&& imp_file.length>0){

				for(int i=0;i<imp_file.length;i++){
					if(!imp_file[i].isEmpty()){
						MultipartFile file = imp_file[i];
						//保存文件到服务器，同时把附件记录更新到附件存放表
						weChatService.saveFile(file, fjcfbModel);
					}
				}

				return "OK";
			}else{
				return "Faild";
			}
		} catch (Exception e) {
			return "Faild";
		}
	}

	/**
	 * 接收服务器传送过来的要求，发送模板信息给相应的微信用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendReportTouser")
	@ResponseBody
	public String sendReportTouser(HttpServletRequest request){
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		try{
			String wxid = request.getParameter("wxid");
			String ybbh = request.getParameter("ybbh");
			String downloadurl = request.getParameter("downloadurl");
			String bz = request.getParameter("bz");
			if(StringUtil.isNotBlank(wxid)){
				log.info("sendReportTouser wxid:" + wxid + " Url:" + downloadurl);
				String resultString = WeChatUtils.sendTemplateMsg(restTemplate, "TEMPLATE_REPORT",wxid, ybbh,bz, downloadurl, wbcxdm,redisUtil);
				log.info("sendReportTouser:" + resultString);
				return "OK";
			}else{
				return "Faild";
			}
		} catch (Exception e) {
			return "Faild";
		}
	}

	/**
	 * 接收服务器传送过来的要求，发送周报模板信息给相应的微信用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendWeChatMessage")
	@ResponseBody
	public String sendWeChatMessage(HttpServletRequest request){
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		try{
			String templateid = request.getParameter("templateid");
			String wxid = request.getParameter("wxid");
			String title = request.getParameter("title");
			String keyword1 = request.getParameter("keyword1");
			String keyword2 = request.getParameter("keyword2");
			String keyword3 = request.getParameter("keyword3");
			String keyword4 = request.getParameter("keyword4");
			String remark = request.getParameter("remark");
			String reporturl = request.getParameter("reporturl");

			if(StringUtil.isNotBlank(wxid)){
				log.info("sendWeekReport Url:" + reporturl);
				String resultString = WeChatUtils.sendWeChatMessage(restTemplate, templateid, wxid, title, keyword1, keyword2, keyword3, keyword4,null,remark, reporturl,wbcxdm,redisUtil);
				log.info("sendWeekReport:" + resultString);
				return "OK";
			}else{
				log.info("sendWeekReport error:无微信ID  " + remark);
				return "Faild";
			}
		} catch (Exception e) {
			log.error("sendWeekReport error:" + e.getMessage());
			return "Faild";
		}
	}


	/**
	 * 接收服务器传送过来的要求，发送模板信息给相应的微信用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendWeChatMessageMap")
	@ResponseBody
	public String sendWeChatMessageMap(HttpServletRequest request){
		try{
			String wbcxdm = request.getParameter("wbcxdm");
			String templatedm = request.getParameter("templatedm");
			String wxid = request.getParameter("wxid");
			Map<String, String> messageMap = new HashMap<>();
			Set<String> keys = request.getParameterMap().keySet();
			for (String key : keys) {
				if (!"templateid".equals(key) && !"templatedm".equals(key) && !"wxid".equals(key) && !"wbcxdm".equals(key)){
					messageMap.put(key,request.getParameter(key));
				}
			}
			if(!StringUtil.isAnyBlank(wxid,templatedm)){
				String resultString = WeChatUtils.sendWeChatMessageMap(redisUtil,restTemplate, templatedm, wxid, wbcxdm,messageMap);
				String errcode = JSONObject.parseObject(resultString).getString("errcode");
				if ("0".equals(errcode)){
					return "OK";
				}
				return "Faild";
			}else{
				return "Faild";
			}
		} catch (Exception e) {
			log.error("sendWeChatMessageMap error:" + e.getMessage());
			return "Faild";
		}
	}

	/**
	 * 接收服务器传送过来的文件数据，并保存到本地服务器中(直接保存)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upSaveInspReport")
	@ResponseBody
	public String upSaveInspReport(HttpServletRequest request){
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(request.getParts().iterator().next().getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			FjcfbModel fjcfbModel = JSONObject.parseObject(request.getParameter("fjcfbModel"),FjcfbModel.class);
			if(imp_file != null && imp_file.length > 0 && fjcfbModel != null){
				for(int i = 0; i < imp_file.length; i++){
					if(!imp_file[i].isEmpty()){
						MultipartFile file = imp_file[i];
						//保存文件到服务器，同时把附件记录更新到附件存放表
						weChatService.saveFileOnly(file, fjcfbModel);
					}
				}
				return "OK";
			}else{
				return "Faild";
			}
		} catch (Exception e) {
			return "Faild";
		}
	}

	/**
	 * 接收服务器传送过来的多文件数据，并保存到本地服务器中(直接保存)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upSaveInspReportWithFiles")
	@ResponseBody
	public String upSaveInspReportWithFiles(HttpServletRequest request){
		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(request.getParts().iterator().next().getName());
			MultipartFile[] imp_files = new MultipartFile[files.size()];
			files.toArray(imp_files);
			List<FjcfbModel> fjcfbModels = JSONArray.parseArray(request.getParameter("fjcfbModels"), FjcfbModel.class);
			if (fjcfbModels != null && fjcfbModels.size() > 0 && imp_files != null && imp_files.length > 0) {
				weChatService.saveFilesOnly(files, fjcfbModels);
				return "OK";
			} else {
				return "Faild";
			}
		} catch (Exception e) {
			return "Faild";
		}
	}
	/**
	 * 接收服务器传送过来需要删除的文件数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delInspReportFile")
	@ResponseBody
	public String delInspReportFile(HttpServletRequest request){
		String s_jsonObject = request.getParameter("fjcfbDto");
		JSONObject jsonObject = (JSONObject) JSONObject.parse(s_jsonObject);
		FjcfbDto fjcfbDto = JSONObject.toJavaObject((JSONObject) jsonObject, FjcfbDto.class);

		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		if(isSuccess) {
            return "OK";
        }
		return "Faild";
	}

	/**
	 * 公司获取服务器文件存放表信息接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getFjcfbModel")
	@ResponseBody
	public String getFjcfbModel(HttpServletRequest request){
		String fjids = request.getParameter("fjids");
		String[] fjidArr = fjids.split(",");
		List<String> fjidList = Arrays.asList(fjidArr);

		//获取服务器文件
		try {
			if(fjidList!=null && fjidList.size() > 0){
				List<FjcfbModel> fjcfblist = new ArrayList<>();
				for (int i = 0; i < fjidList.size(); i++) {
					if(StringUtil.isNotBlank(fjidList.get(i))) {
						FjcfbModel t_fjcfbModel = new FjcfbModel();
						t_fjcfbModel.setFjid(fjidList.get(i));
						FjcfbModel fjcfbModel = fjcfbService.getModel(t_fjcfbModel);
						if(fjcfbModel!=null) {
							fjcfblist.add(fjcfbModel);
							//更新文件的转换标记为true
							fjcfbService.updateZhbj(fjcfbModel);
						}
					}
				}
				return JSONObject.toJSONString(fjcfblist);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 公司获取服务器文件接口
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="/getImportFile")
	@ResponseBody
	public void sendUploadVoice(HttpServletResponse response, HttpServletRequest request) {
		String wjlj = request.getParameter("wjlj");
		DBEncrypt dbEncrypt = new DBEncrypt();
		String pathString = dbEncrypt.dCode(wjlj);
		File file = new File(pathString);
		//文件不存在不做任何操作
		if(!file.exists()) {
            return;
        }
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream;
		OutputStream os = null; //输出流
		//获取服务器文件
		try {
			iStream = new FileInputStream(pathString);
			os = response.getOutputStream();
			bis = new BufferedInputStream(iStream);
			int i = bis.read(buffer);
			while(i != -1){
				os.write(buffer);
				os.flush();
				i = bis.read(buffer);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			bis.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 送检清单点击后获取送检结果信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/getReportView")
	public ModelAndView getReportView(SjxxDto sjxxDto, HttpServletRequest request){
		boolean checkSign = commonService.checkSign(sjxxDto.getSign().replace(" ","+"), sjxxDto.getYbbh(), request);
		if(!checkSign){
            return commonService.jumpError();
		}
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportView");
		// 获取收费权限信息
		String sfqx = null;
		/*if(StringUtil.isNotBlank(sjxxDto.getWxid())) {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("wxid", sjxxDto.getWxid());
			DBEncrypt crypt = new DBEncrypt();
			String url = crypt.dCode(companyurl);
			@SuppressWarnings("unchecked")
			Map<String,String> map = restTemplate.postForObject(url+"/ws/dingtalk/getUser", paramMap, Map.class);
			if(map != null && StringUtil.isNotBlank(map.get("sfqx")))
				sfqx = map.get("sfqx");
		}*/
		mav.addObject("sfqx", sfqx);
		
		SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
		
		if(sjxxDto2 != null) {
			//查看送检结果
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto2.getSjid());
			fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				fjcfbDtos.get(i).setSign(commonService.getSign(fjcfbDtos.get(i).getFjid()));
			}
			//查看图片
			FjcfbDto fjcfbDto_fj=new FjcfbDto();
			fjcfbDto_fj.setYwid(sjxxDto.getSjid());
			fjcfbDto_fj.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos_fjs = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_fj);
			mav.addObject("fjcfbDtos_fjs",fjcfbDtos_fjs);
			mav.addObject("fjcfbDtos",fjcfbDtos);
		}else {
			sjxxDto2 = new SjxxDto();
		}
		mav.addObject("sjxxDto", sjxxDto2);
		String sfsj="0";
		List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjxxDto2.getSjid());
		if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
			for(SjsyglDto dto:sjsyglDtos){
				if(StringUtil.isNotBlank(dto.getSjsj())){
					sfsj="1";
					break;
				}
			}
		}
		mav.addObject("sfsj", sfsj);
		mav.addObject("sjsyglDtos", sjsyglDtos);
		return mav;
	}

	/**
	 * 从微信传递的参数获取相应的信息
	 * @param result
	 * @return
	 */
	private WeChatTextModel packagTextModel(Map<String, Object> result){

		WeChatTextModel weChatTextModel = new WeChatTextModel();
		//开发者微信号
		weChatTextModel.setToUserName((String)result.get("ToUserName"));
		// 公众号可获得关注者的OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的。对于不同公众号，同一用户的openid不同）
		weChatTextModel.setFromUserName((String)result.get("FromUserName"));
		//创建时间
		weChatTextModel.setCreateTime((String)result.get("CreateTime"));
		//消息类型
		weChatTextModel.setMsgType((String)result.get("MsgType"));
		//消息类型
		weChatTextModel.setEvent((String)result.get("Event"));
		//事件KEY值，qrscene_为前缀，后面为二维码的参数值

		weChatTextModel.setEventKey((String)result.get("EventKey"));
		//二维码的ticket，可用来换取二维码图片
		weChatTextModel.setTicket((String)result.get("Ticket"));
		//消息ID
		weChatTextModel.setMsgId((String)result.get("MsgId"));
		//消息内容
		weChatTextModel.setContent((String)result.get("Content"));

		return weChatTextModel;
	}

	/**
	 * 获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/getWechatUserReport")
	public ModelAndView getWechatUserReport(SjxxDto sjxxDto, @RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
		if(StringUtil.isNotBlank(sjxxDto.getYbbh())) {
			ModelAndView mav = viewReportPage(sjxxDto);
			mav.addObject("viewFlg","1");
			return mav;
		}
		//录入信息页面
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_userInfo");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		if(StringUtil.isBlank(sjxxDto.getWxid())){
			//获取wxid(openid)
			String organ = request.getParameter("organ");
			SjxxDto t_sjxxDto = weChatService.getReportInfoByUserAuth(code, state, wbcxdm, organ);
			if(t_sjxxDto == null) {
				mav = null;
				log.error("getUserReport null。code =" + code);
				return mav;
			}
			sjxxDto.setWxid(t_sjxxDto.getWxid());
		}
		WxyhDto wxyhDto = wxyhService.getDtoById(sjxxDto.getWxid());
		if(wxyhDto == null){
			mav = null;
			log.error("getUserReport null。Wxid =" + sjxxDto.getWxid());
			return mav;
		}
		//查询微信用户表确认个人身份信息
		if(StringUtil.isBlank(wxyhDto.getSflx())){
			//获取身份类型
			List<IdentityTypeEnum> identitylist = wxyhService.getIdentityType();
			mav.addObject("identitylist", identitylist);
			mav.addObject("wxyhDto",wxyhDto);
			String sign = commonService.getSign();
			mav.addObject("sign", sign);
			return mav;
		}else{
			if(wxyhDto.getSflx().equals(IdentityTypeEnum.DOCTOR.getCode())){
				if(StringUtil.isBlank(wxyhDto.getSj())){
					//录入信息页面
					mav = new ModelAndView("wechat/informed/wechat_userInfo");
					//获取身份类型
					List<IdentityTypeEnum> identitylist = wxyhService.getIdentityType();
					mav.addObject("identitylist", identitylist);
					mav.addObject("wxyhDto",wxyhDto);
					String sign = commonService.getSign();
					mav.addObject("sign", sign);
					return mav;
				}else{
					//跳转医生
//					mav = new ModelAndView("wechat/informed/wechat_viewreportBydoctor");
					mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
				}
			}else if(wxyhDto.getSflx().equals(IdentityTypeEnum.PATIENT.getCode())){
				//跳转患者
//				mav.addObject("submitUrl", "/wechat/getUserInfoView");
				mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
			}else if(wxyhDto.getSflx().equals(IdentityTypeEnum.SALES.getCode())){
				//跳转销售
//				mav = new ModelAndView("wechat/informed/wechat_viewReportBySales");
				mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
			}
		}
		mav.addObject("sign", commonService.getSign());
		mav.addObject("wxyhDto",wxyhDto);
		//如果标本编号不会空，则查询标本信息，同时显示付款金额，方便显示支付宝付款
		if(StringUtil.isNotBlank(sjxxDto.getYbbh())) {
			SjxxDto re_sjxxDto = sjxxService.getDto(sjxxDto);
			//前端需要查询收费信息，然后送检信息里也显示为未收费的时候，查询相应的金额
			if(re_sjxxDto != null){
				if(!"1".equals(re_sjxxDto.getFkbj())) {
					if(StringUtil.isBlank(re_sjxxDto.getFkje())){
						List<SjjcxmDto> sjjcxmDtos = sjjcxmService.selectJcxmBySjid(re_sjxxDto);
						SjhbxxDto sjhbxxDto = new SjhbxxDto();
						sjhbxxDto.setSjxms(sjjcxmDtos);
						sjhbxxDto.setHbmc(re_sjxxDto.getDb());
						SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
						if(re_sjhbxxDto!=null) {
                            re_sjxxDto.setFkje(re_sjhbxxDto.getSfbz());
                        }
					}else{
						re_sjxxDto.setFkje(re_sjxxDto.getFkje());
					}
				}
				mav.addObject("sjxxDto", re_sjxxDto);
			}else {
				mav.addObject("sjxxDto", sjxxDto);
			}
		}else {
			mav.addObject("sjxxDto", sjxxDto);
		}
		return mav;
	}


	/**
	 * 扫码获取送检信息，查看报告第一步
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/getUserReport")
	public ModelAndView getUserReport(SjxxDto sjxxDto, @RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
		if(StringUtil.isNotBlank(sjxxDto.getYbbh())) {
			ModelAndView mav = viewReportPage(sjxxDto);
			mav.addObject("viewFlg","0");
			return mav;
		}
		//录入信息页面
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_userInfo");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		if(StringUtil.isBlank(sjxxDto.getWxid())){
			//获取wxid(openid)
			String organ = request.getParameter("organ");
			SjxxDto t_sjxxDto = weChatService.getReportInfoByUserAuth(code, state, wbcxdm, organ);
			if(t_sjxxDto == null) {
				mav = null;
				log.error("getUserReport null。code =" + code);
				return mav;
			}
			sjxxDto.setWxid(t_sjxxDto.getWxid());
		}
		WxyhDto wxyhDto = wxyhService.getDtoById(sjxxDto.getWxid());
		if(wxyhDto == null){
			mav = null;
			log.error("getUserReport null。Wxid =" + sjxxDto.getWxid());
			return mav;
		}
		//查询微信用户表确认个人身份信息
		if(StringUtil.isBlank(wxyhDto.getSflx())){
			//获取身份类型
			List<IdentityTypeEnum> identitylist = wxyhService.getIdentityType();
			mav.addObject("identitylist", identitylist);
			mav.addObject("wxyhDto",wxyhDto);
			String sign = commonService.getSign();
			mav.addObject("sign", sign);
			return mav;
		}else{
			if(wxyhDto.getSflx().equals(IdentityTypeEnum.DOCTOR.getCode())){
				if(StringUtil.isBlank(wxyhDto.getSj())){
					//录入信息页面
					mav = new ModelAndView("wechat/informed/wechat_userInfo");
					//获取身份类型
					List<IdentityTypeEnum> identitylist = wxyhService.getIdentityType();
					mav.addObject("identitylist", identitylist);
					mav.addObject("wxyhDto",wxyhDto);
					String sign = commonService.getSign();
					mav.addObject("sign", sign);
					return mav;
				}else{
					//跳转医生
//					mav = new ModelAndView("wechat/informed/wechat_viewreportBydoctor");
					mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
				}
			}else if(wxyhDto.getSflx().equals(IdentityTypeEnum.PATIENT.getCode())){
				//跳转患者
//				mav.addObject("submitUrl", "/wechat/getUserInfoView");
				mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
			}else if(wxyhDto.getSflx().equals(IdentityTypeEnum.SALES.getCode())){
				//跳转销售
//				mav = new ModelAndView("wechat/informed/wechat_viewReportBySales");
				mav = new ModelAndView("wechat/informed/wechat_getUserInfo");
			}
		}
		mav.addObject("sign", commonService.getSign());
		mav.addObject("wxyhDto",wxyhDto);
		//如果标本编号不会空，则查询标本信息，同时显示付款金额，方便显示支付宝付款
		if(StringUtil.isNotBlank(sjxxDto.getYbbh())) {
			SjxxDto re_sjxxDto = sjxxService.getDto(sjxxDto);
			//前端需要查询收费信息，然后送检信息里也显示为未收费的时候，查询相应的金额
			if(re_sjxxDto != null){
				if(!"1".equals(re_sjxxDto.getFkbj())) {
					if(StringUtil.isBlank(re_sjxxDto.getFkje())){
						List<SjjcxmDto> sjjcxmDtos = sjjcxmService.selectJcxmBySjid(re_sjxxDto);
						SjhbxxDto sjhbxxDto = new SjhbxxDto();
						sjhbxxDto.setSjxms(sjjcxmDtos);
						sjhbxxDto.setHbmc(re_sjxxDto.getDb());
						SjhbxxDto re_sjhbxxDto = sjhbxxService.getDto(sjhbxxDto);
						if(re_sjhbxxDto!=null) {
                            re_sjxxDto.setFkje(re_sjhbxxDto.getSfbz());
                        }
					}else{
						re_sjxxDto.setFkje(re_sjxxDto.getFkje());
					}
				}
				mav.addObject("sjxxDto", re_sjxxDto);
			}else {
				mav.addObject("sjxxDto", sjxxDto);
			}
		}else {
			mav.addObject("sjxxDto", sjxxDto);
		}
		return mav;
	}

	public ModelAndView viewReportPage(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_error");
		SjxxDto sjxxjcbgDto = sjxxService.getDto(sjxxDto);
		if(sjxxjcbgDto != null) {
			mav=new ModelAndView("wechat/informed/wechat_view");
			sjxxjcbgDto.setSflx("DOCTOR");
			if("1".equals(sjxxjcbgDto.getSjdwbj())) {
				sjxxjcbgDto.setSjdwmc(sjxxjcbgDto.getYymc()+"-"+sjxxjcbgDto.getSjdwmc());
			}
			// 查询送检耐药性
			List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxjcbgDto);

//			if(("Z6").equals(sjxxjcbgDto.getCskz1()) || ("Z12").equals(sjxxjcbgDto.getCskz1()) || ("Z18").equals(sjxxjcbgDto.getCskz1()) || ("F").equals(sjxxjcbgDto.getCskz1())) {
			if( ("Z6").equals(sjxxjcbgDto.getCskz1()) ||("Z").equals(sjxxjcbgDto.getCskz1()) || ("F").equals(sjxxjcbgDto.getCskz1())) {
				List<SjzmjgDto> sjzmList;
				SjzmjgDto sjzmjgDto = new SjzmjgDto();
				sjzmjgDto.setSjid(sjxxjcbgDto.getSjid());
				sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
				mav.addObject("sjzmList", sjzmList);
				mav.addObject("KZCS",sjxxjcbgDto.getCskz1());
			}
			// 查询物种信息
			List<SjwzxxDto> wzxx = sjxxService.selectWzxxBySjid(sjxxjcbgDto);
			if(wzxx!=null && wzxx.size()>0) {
				String xpxx=wzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
				mav.addObject("Xpxx", xpxx);
			}
			//查看送检结果
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxjcbgDto.getSjid());

			fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				fjcfbDtos.get(i).setSign(commonService.getSign(fjcfbDtos.get(i).getFjid()));
			}
			mav.addObject("fjcfbDtos",fjcfbDtos);
			//查看附件
			fjcfbDto.setYwlxs(null);
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos_fjs = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			for (int i = 0; i < fjcfbDtos_fjs.size(); i++) {
				fjcfbDtos_fjs.get(i).setSign(commonService.getSign(fjcfbDtos_fjs.get(i).getFjid()));
			}
			SjbgsmDto sjbgsmdto = new SjbgsmDto();
			sjbgsmdto.setSjid(sjxxjcbgDto.getSjid());
			List<SjbgsmDto> sjbgsmxx = sjbgsmservice.selectSjbgBySjid(sjbgsmdto);

			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
			List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
			List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
			List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页
			if(jcxmlist!=null && jcxmlist.size()>0) {
				for(int i=0;i<jcxmlist.size();i++) {
					boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
					if(wzxx!=null && wzxx.size()>0) {
						for(int j=0;j<wzxx.size();j++) {
							if(wzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
								wz_sftj=true;
								break;
							}
						}
					}
					if(wz_sftj) {
                        c_jcxmlist.add(jcxmlist.get(i));
                    }

					boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
					if(sjbgsmxx!=null && sjbgsmxx.size()>0) {
						for(int j=0;j<sjbgsmxx.size();j++) {
							if(sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
								sftj=true;
								break;
							}
						}
					}
					if(fjcfbDtos!=null && fjcfbDtos.size()>0) {
						for(int j=0;j<fjcfbDtos.size();j++) {
							if(fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()))) {
								sftj=true;
								break;
							}
						}
					}
					if(sftj) {
                        t_jcxmlist.add(jcxmlist.get(i));
                    }
				}
			}

			SjxxjgDto sjxxjgDto=new SjxxjgDto();
			sjxxjgDto.setSjid(sjxxjcbgDto.getSjid());
			List<SjxxjgDto> getJclxCount=sjxxjgService.getJclxCount(sjxxjgDto);

			mav.addObject("SjxxjgList", getJclxCount);
			mav.addObject("sjbgsmList",sjbgsmxx);
			mav.addObject("fjcfbDtos",fjcfbDtos);
			mav.addObject("fjcfbDtos_fjs",fjcfbDtos_fjs);
			mav.addObject("DaobyYbbh",sjxxjcbgDto);
			mav.addObject("Sjwzxx", wzxx);
			mav.addObject("SjnyxDto", sjnyx);
			mav.addObject("jcxmlist",t_jcxmlist);
			mav.addObject("wzjcxmlist", c_jcxmlist);
			String sfsj="0";
			List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjxxjcbgDto.getSjid());
			if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
				for(SjsyglDto dto:sjsyglDtos){
					if(StringUtil.isNotBlank(dto.getSjsj())){
						sfsj="1";
						break;
					}
				}
			}
			mav.addObject("sfsj", sfsj);
			mav.addObject("sjsyglDtos", sjsyglDtos);
		}
		DBEncrypt crypt = new DBEncrypt();
		mav.addObject("applicationurl", crypt.dCode(applicationurl));
		mav.addObject("sjxxDto", sjxxDto);
		// 获取收费权限信息
		String sfqx = null;
		/*if(StringUtil.isNotBlank(sjxxDto.getWxid())) {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
			paramMap.add("wxid", sjxxDto.getWxid());
			String url = crypt.dCode(companyurl);
			@SuppressWarnings("unchecked")
			Map<String,String> map = restTemplate.postForObject(url+"/ws/dingtalk/getUser", paramMap, Map.class);
			if(map != null && StringUtil.isNotBlank(map.get("sfqx")))
				sfqx = map.get("sfqx");
		}*/
		mav.addObject("sfqx", sfqx);
		return mav;
	}

	/**
	 * 扫码后录入患者信息获取报告信息。查看报告第二步
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/getUserInfoView")
	public ModelAndView getUserInfoView(SjxxDto sjxxDto,HttpServletRequest request){
		if(StringUtil.isBlank(sjxxDto.getHzxm())||StringUtil.isBlank(sjxxDto.getYbbh())){
			boolean checkSign = commonService.checkSign(sjxxDto.getSign(),sjxxDto.getYbbh(),request);
			if(!checkSign){
                return commonService.jumpError();
			}
		}
		//判断标本编号和患者姓名是否对应
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_error");
		SjxxDto sjxxjcbgDto = sjxxService.getDto(sjxxDto);
		if(sjxxjcbgDto != null) {
			sjxxjcbgDto.setSflx(sjxxDto.getSflx());
			mav=new ModelAndView("wechat/informed/wechat_view");
			if("1".equals(sjxxjcbgDto.getSjdwbj())) {
				sjxxjcbgDto.setSjdwmc(sjxxjcbgDto.getYymc()+"-"+sjxxjcbgDto.getSjdwmc());
			}
			// 查询送检耐药性
			List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxjcbgDto);

//			if(("Z6").equals(sjxxjcbgDto.getCskz1()) || ("Z12").equals(sjxxjcbgDto.getCskz1()) || ("Z18").equals(sjxxjcbgDto.getCskz1()) || ("F").equals(sjxxjcbgDto.getCskz1())) {
			if( ("Z6").equals(sjxxjcbgDto.getCskz1()) ||("Z").equals(sjxxjcbgDto.getCskz1()) || ("F").equals(sjxxjcbgDto.getCskz1())) {
				List<SjzmjgDto> sjzmList;
				SjzmjgDto sjzmjgDto = new SjzmjgDto();
				sjzmjgDto.setSjid(sjxxjcbgDto.getSjid());
				sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
				mav.addObject("sjzmList", sjzmList);
				mav.addObject("KZCS",sjxxjcbgDto.getCskz1());
			}
			// 查询物种信息
			List<SjwzxxDto> wzxx = sjxxService.selectWzxxBySjid(sjxxjcbgDto);
			if(wzxx!=null && wzxx.size()>0) {
				String xpxx=wzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
				mav.addObject("Xpxx", xpxx);
			}
			//查看送检结果
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxjcbgDto.getSjid());

			fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				fjcfbDtos.get(i).setSign(commonService.getSign(fjcfbDtos.get(i).getFjid()));
			}
			mav.addObject("fjcfbDtos",fjcfbDtos);
			//查看附件
			fjcfbDto.setYwlxs(null);
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos_fjs = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			for (int i = 0; i < fjcfbDtos_fjs.size(); i++) {
				fjcfbDtos_fjs.get(i).setSign(commonService.getSign(fjcfbDtos_fjs.get(i).getFjid()));
			}
			SjbgsmDto sjbgsmdto = new SjbgsmDto();
			sjbgsmdto.setSjid(sjxxjcbgDto.getSjid());
			List<SjbgsmDto> sjbgsmxx = sjbgsmservice.selectSjbgBySjid(sjbgsmdto);

			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
			List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
			List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
			List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页
			if(jcxmlist!=null && jcxmlist.size()>0) {
				for(int i=0;i<jcxmlist.size();i++) {
					boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
					if(wzxx!=null && wzxx.size()>0) {
						for(int j=0;j<wzxx.size();j++) {
							if(wzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
								wz_sftj=true;
								break;
							}
						}
					}
					if(wz_sftj) {
                        c_jcxmlist.add(jcxmlist.get(i));
                    }

					boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
					if(sjbgsmxx!=null && sjbgsmxx.size()>0) {
						for(int j=0;j<sjbgsmxx.size();j++) {
							if(sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
								sftj=true;
								break;
							}
						}
					}
					if(fjcfbDtos!=null && fjcfbDtos.size()>0) {
						for(int j=0;j<fjcfbDtos.size();j++) {
							if(fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()))) {
								sftj=true;
								break;
							}
						}
					}
					if(sftj) {
                        t_jcxmlist.add(jcxmlist.get(i));
                    }
				}
			}

			SjxxjgDto sjxxjgDto=new SjxxjgDto();
			sjxxjgDto.setSjid(sjxxjcbgDto.getSjid());
			List<SjxxjgDto> getJclxCount=sjxxjgService.getJclxCount(sjxxjgDto);

			mav.addObject("SjxxjgList", getJclxCount);
			mav.addObject("sjbgsmList",sjbgsmxx);
			mav.addObject("fjcfbDtos",fjcfbDtos);
			mav.addObject("fjcfbDtos_fjs",fjcfbDtos_fjs);
			mav.addObject("DaobyYbbh",sjxxjcbgDto);
			mav.addObject("Sjwzxx", wzxx);
			mav.addObject("SjnyxDto", sjnyx);
			mav.addObject("jcxmlist",t_jcxmlist);
			mav.addObject("wzjcxmlist", c_jcxmlist);
			String sfsj="0";
			List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjxxjcbgDto.getSjid());
			if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
				for(SjsyglDto dto:sjsyglDtos){
					if(StringUtil.isNotBlank(dto.getSjsj())){
						sfsj="1";
						break;
					}
				}
			}
			mav.addObject("sfsj", sfsj);
			mav.addObject("sjsyglDtos", sjsyglDtos);
			mav.addObject("viewFlg","1");
		}
		DBEncrypt crypt = new DBEncrypt();
		mav.addObject("applicationurl", crypt.dCode(applicationurl));
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 检测报告单点击免责声明跳转免责声明页面
	 * @return
	 */
	@RequestMapping(value="/getmzsm")
	public ModelAndView getMzsm() {
        return new ModelAndView("wechat/informed/wechat_mzsm");
	}

	/**
	 * 检测报告单点击感叹号图标弹出参考指标解释窗口
	 * @return
	 */
	@RequestMapping(value="/getckzbjs")
	public ModelAndView getCkzbjs() {
        return new ModelAndView("wechat/informed/wechat_ckzbjs");
	}

	/**
	 * 保存客户端提交的上传文件
	 * @param request
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value="/file/saveImportFile")
	@ResponseBody
	public Map<String, Object> saveImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();

		try{
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(request.getParts().iterator().next().getName());
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file!=null&& imp_file.length>0){
				User user = null;
				boolean isSuccess = fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
				result.put("fjcfbDto", fjcfbDto);
				result.put("status", isSuccess?"success":"fail");
				result.put("msg", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			}else{
				result.put("status", "fail");
				result.put("msg", "未获取文件");
			}
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", e.getMessage());
		}
		return result;
	}

	/**
	 * 图片预览
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/file/pdfPreview")
	public ModelAndView pdfPreview(FjcfbDto fjcfbDto, HttpServletRequest request) {
		try {
			boolean checkSign = commonService.checkSign(fjcfbDto.getSign(), fjcfbDto.getFjid(), request);
			if(!checkSign){
                return commonService.jumpError();
			}
			String filepath = "/wechat/file/getFileInfo?fjid=" + fjcfbDto.getFjid();
			ModelAndView mv = new ModelAndView("wechat/informed/viewer");
			mv.addObject("file",filepath);

			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="/file/getFileInfo")
	public void getFileInfo(FjcfbDto fjcfbDto, HttpServletResponse response){
		FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
		if(fjcfbModel == null) {
			return;
		}
		//取系统设置表的文件天数保存设置
		DBEncrypt crypt = new DBEncrypt();
		XtszDto xtszDto = new XtszDto();
		String t_url = crypt.dCode(companyurl)+"/ws/miniprogram/downloadDocFile";
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		if (redisUtil.hget("matridx_xtsz","matridx.inspection.daylimit")!=null){
			Object xtszObj = redisTemplate.opsForHash().get("matridx_xtsz","matridx.inspection.daylimit");
			xtszDto = JSON.parseObject(xtszObj.toString(),XtszDto.class);
		}
		boolean isRedirect = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtil.isNotBlank(fjcfbModel.getLrsj()) && StringUtil.isNotBlank(xtszDto.getSzz())){
			try{
				//看是否超过系统设置天数，比较当前日期  和  录入日期+设置天数后的日期
				//录入日期+设置天数后的日期 > 当前日期  ，下载地址无需重定向到86
				//录入日期+设置天数后的日期 < 当前日期  ，下载地址需要重定向到86
				Date lrsjAdd = DateUtils.getDate(DateUtils.parse(sdf.format(sdf.parse(fjcfbModel.getLrsj()))),Integer.parseInt(xtszDto.getSzz()));
				Date datenow = DateUtils.parse(sdf.format(new Date()));
				isRedirect = lrsjAdd.before(datenow);
			}catch (Exception e){
				log.error(e.getMessage());
			}
		}
		paramMap.add("fjid",fjcfbModel.getFjid());
		if (isRedirect){//转发到86端
			ResponseEntity<byte[]> entity = restTemplate.exchange(t_url, HttpMethod.POST, new HttpEntity<>(paramMap), byte[].class);
			response.setHeader("content-type", "application/octet-stream");
			byte[] result = entity.getBody();
			if(result!=null) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try {
					inputStream = new ByteArrayInputStream(result);
					outputStream = response.getOutputStream();
					int len;
					byte[] buf = new byte[1024];
					while ((len = inputStream.read(buf, 0, 1024)) != -1) {
						outputStream.write(buf, 0, len);
					}
					outputStream.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
						if (outputStream != null) {
							outputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			String filePath = crypt.dCode(fjcfbModel.getWjlj());

			if(StringUtil.isBlank(filePath)) {
				return;
			}

			byte[] buffer = new byte[1024];
			BufferedInputStream bis = null;

			OutputStream os = null; //输出流
			try {
				InputStream iStream = new FileInputStream(filePath);

				//设置Content-Disposition
				//response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fjcfbModel.getWjm(), "UTF-8"));
				//指明为流
				//response.setContentType("application/octet-stream; charset=utf-8");

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
				e.printStackTrace();
			}
			try {
				bis.close();
				os.flush();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 跳转选择条码页面
	 * @return
	 */
	@RequestMapping(value="/pay/payBySampleCode")
	public ModelAndView payBySampleCode() {
		try {
			ModelAndView mv = new ModelAndView("wechat/pay/pay_samplecode");

			mv.addObject("sjxxDto",new SjxxDto());

			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转网页支付页面
	 * @return
	 */
	@RequestMapping(value="/pay/payWebsite")
	public ModelAndView payWebsite(SjxxDto sjxxDto, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("wechat/pay/pay_website");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		//根据外部程序代码查询外部程序表信息
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wbcxdm);
		wbcxDto = wbcxService.getDto(wbcxDto);
		DBEncrypt crypt = new DBEncrypt();
		mv.addObject("appid", crypt.dCode(wbcxDto.getAppid()));
		mv.addObject("applicationurl", crypt.dCode(applicationurl));
		mv.addObject("sjxxDto", sjxxDto);
		return mv;
	}

	/**
	 * 删除附件信息
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/delFile")
	@ResponseBody
	public Map<String, Object> inspectionSaveConfirm(FjcfbDto fjcfbDto){
		fjcfbDto.setScry(fjcfbDto.getWxid());
		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 提取用户送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/extractUserInfo")
	@ResponseBody
	public Map<String, Object> extractUserInfo(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		try {
			SjxxDto t_sjxxDto = sjxxService.extractUserInfo(sjxxDto);
			if(t_sjxxDto != null){
				//根据文件ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(t_sjxxDto.getSjid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				map.put("fjcfbDtos",fjcfbDtos);
				map.put("unitlist",  sjxxService.getDetectionUnit(t_sjxxDto.getDb()));
				map.put("subdetectlist", sjxxService.getSubDetect(t_sjxxDto.getJcxm()));
				//根据标本类型cskz2限制检测项目
				JcsjDto jc_yblxDto=new JcsjDto();
				if(StringUtils.isNotBlank(t_sjxxDto.getYblx())) {
                    jc_yblxDto=jcsjService.getDtoById(t_sjxxDto.getYblx());
                }
				Map<String, List<JcsjDto>> g_jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
				List<JcsjDto> detectlist = g_jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
				List<JcsjDto> t_detectlist = new ArrayList<>();
				if(detectlist != null && detectlist.size() > 0) {
					for(int i = 0; i < detectlist.size(); i++) {
						if(StringUtils.isNotBlank(jc_yblxDto.getCskz2())) {
							if(jc_yblxDto.getCskz2().contains(detectlist.get(i).getCsdm())) {
								t_detectlist.add(detectlist.get(i));
							}
						}else {
							t_detectlist=detectlist;
						}
					}
				}
				map.put("detectlist", t_detectlist);
			}
			SjjcxmDto jcxmDto = new SjjcxmDto();
			jcxmDto.setSjid(t_sjxxDto.getSjid());

			List<String> sjjcxmDtos = new ArrayList<>();
			List<SjjcxmDto> dtoList = sjjcxmService.getDtoList(jcxmDto);
			List<String> xmglids=new ArrayList<>();
			if(dtoList!=null&&dtoList.size()>0){
				for(SjjcxmDto dto:dtoList){
					xmglids.add(dto.getXmglid());
					sjjcxmDtos.add(dto.getJcxmid());
				}
			}
			t_sjxxDto.setJcxmids(sjjcxmDtos);
			t_sjxxDto.setXmglids(xmglids);
			//获取送检子项目清单
			t_sjxxDto.setJczxmids(sjjcxmService.getJczxmString(jcxmDto));
			map.put("sjxxDto", t_sjxxDto);
			map.put("sjjcxmDtos", dtoList);
			map.put("status", t_sjxxDto != null?"success":"fail");
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}

	/**
	 * 修改金额页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/inspection/amountEdit")
	public ModelAndView amountEdit(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/informed/Sjxx_fkjetz");
		SjxxDto t_sjxxDto = new SjxxDto();
		if(sjxxDto != null){
			t_sjxxDto = sjxxService.getDto(sjxxDto);
			t_sjxxDto.setWxid(sjxxDto.getWxid());
		}
		mav.addObject("sjxxDto", t_sjxxDto);
		return mav;
	}

	/**
	 * 保存修改金额
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/inspection/amountSaveEdit")
	@ResponseBody
	public Map<String, Object> amountSaveEdit(SjxxDto sjxxDto){
		sjxxDto.setXgry(sjxxDto.getWxid());
		boolean isSuccess = sjxxService.amountSaveEdit(sjxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("sjxxDto", sjxxDto);
		return map;
	}

	/**
	 * 文件下载（用于普通的文件下载使用）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/loadFile")
	public ModelAndView loadFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		ModelAndView mav = commonService.jumpError();
		boolean checkSign = commonService.checkSign(fjcfbDto.getSign(), fjcfbDto.getFjid(), request);
		if(!checkSign){
			log.error("签名信息错误!sign=" + fjcfbDto.getSign() + " fjid="+fjcfbDto.getFjid());
			return mav;
		}
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null)
		{
			log.error("对不起，系统未找到相应文件!");
			return mav;
		}
		//=================================================
		//取系统设置表的文件天数保存设置
		DBEncrypt crypt = new DBEncrypt();
		XtszDto xtszDto = new XtszDto();
        String t_url = crypt.dCode(companyurl)+"/ws/miniprogram/downloadDocFile";
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		if (redisUtil.hget("matridx_xtsz","matridx.inspection.daylimit")!=null){
			Object xtszObj = redisTemplate.opsForHash().get("matridx_xtsz","matridx.inspection.daylimit");
			xtszDto = JSON.parseObject(xtszObj.toString(),XtszDto.class);
		}
		boolean isRedirect = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtil.isNotBlank(t_fjcfbDto.getLrsj()) && StringUtil.isNotBlank(xtszDto.getSzz())){
			try{
				//看是否超过系统设置天数，比较当前日期  和  录入日期+设置天数后的日期
				//录入日期+设置天数后的日期 > 当前日期  ，下载地址无需重定向到86
				//录入日期+设置天数后的日期 < 当前日期  ，下载地址需要重定向到86
				Date lrsjAdd = DateUtils.getDate(DateUtils.parse(sdf.format(sdf.parse(t_fjcfbDto.getLrsj()))),Integer.parseInt(xtszDto.getSzz()));
				Date datenow = DateUtils.parse(sdf.format(new Date()));
				isRedirect = lrsjAdd.before(datenow);
			}catch (Exception e){
				log.error(e.getMessage());
			}
		}
		paramMap.add("fjid",t_fjcfbDto.getFjid());
		paramMap.add("isRedirect","isRedirect");
		//===================================================
		String wjlj = t_fjcfbDto.getWjlj();
		String wjm = t_fjcfbDto.getWjm();
		String filePath = crypt.dCode(wjlj);
		try {
			commonService.dealDonwloadFileName(response,request,wjm);
			log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(wjm,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error("文件下载 异常：" + e1.toString());
			return mav;
		}

		if (isRedirect){//转发到85端
			ResponseEntity<byte[]> entity = restTemplate.exchange(t_url, HttpMethod.POST, new HttpEntity<>(paramMap), byte[].class);
			response.setHeader("content-type", "application/octet-stream");
			byte[] result = entity.getBody();
			if(result!=null) {
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try {
					inputStream = new ByteArrayInputStream(result);
					outputStream = response.getOutputStream();
					int len;
					byte[] buf = new byte[1024];
					while ((len = inputStream.read(buf, 0, 1024)) != -1) {
						outputStream.write(buf, 0, len);
					}
					outputStream.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
						if (outputStream != null) {
							outputStream.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else{
			byte[] buffer = new byte[1024];
			BufferedInputStream bis;
			InputStream iStream;
			OutputStream os; //输出流
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
				e.printStackTrace();
				log.error("文件下载 写文件异常：" + e.toString());
				return mav;
			}
			try {
				bis.close();
				os.flush();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("文件下载 关闭文件流异常：" + e.toString());
				return mav;
			}
		}

		return null;
	}

	/**
	 *图片预览
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value="/wechatpreview")
	public  ModelAndView wechatpreview(FjcfbDto fjcfbDto) {
		ModelAndView mav= new ModelAndView("wechat/informed/wechatpreview");
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}

	/**
	 * 微信端图片预览
	 * @param fjcfbDto
	 * @param response
	 */
	@RequestMapping(value="/file/getwechatFileInfo")
	public void getPubFileInfo(FjcfbDto fjcfbDto,HttpServletResponse response){
		FjcfbModel fjcfbModel = fjcfbService.getModel(fjcfbDto);
		DBEncrypt encrypt = new DBEncrypt();
		String filePath = encrypt.dCode(fjcfbModel.getWjlj());

		if(StringUtil.isBlank(filePath)) {
            return;
        }

		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null; //输出流
		try {
			InputStream iStream = new FileInputStream(filePath);

			//设置Content-Disposition
			//response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fjcfbModel.getWjm(), "UTF-8"));
			//指明为流
			//response.setContentType("application/octet-stream; charset=utf-8");

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
			e.printStackTrace();
		}
		try {
			bis.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发送文件到钉钉
	 * @param fjcfbDto
	 */
	@RequestMapping(value="/file/sendDingTalkFile")
	public void sendDingTalkFile(FjcfbDto fjcfbDto, HttpServletRequest request){
		DBEncrypt dbEncrypt = new DBEncrypt();
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto != null){
			String mediaId = talkUtil.uploadFileToDingTalk(request.getParameter("yhm"), dbEncrypt.dCode(t_fjcfbDto.getWjlj()), t_fjcfbDto.getWjm());
			talkUtil.sendFileMessage(request.getParameter("yhm"),fjcfbDto.getDdid(),mediaId);
		}
	}

	@RequestMapping("/wechat_viewreportBydoctor")
	public ModelAndView wechat_viewreportBydoctor(WxyhDto wxyhDto) {
		ModelAndView mav=new ModelAndView("wechat/informed/wechat_viewreportBydoctor");
		wxyhDto.setSj("13208005203");
		wxyhDto.setWxid("of0Fe1fJPdZJpSLL9vNpj2NCdOlk");
		mav.addObject("wxyhDto", wxyhDto);
		return mav;
	}

	/**
	 * 医生电话解绑，绑定
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping("/updateSj")
	public @ResponseBody  Map<String, Object> updateXX(WxyhDto wxyhDto){
		Map<String,Object> map= new HashMap<>();
		if(StringUtil.isBlank(wxyhDto.getWbcxdm())){
			wxyhDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wxyhDto.getWbcxdm());
		wbcxDto = wbcxService.getDto(wbcxDto);
		wxyhDto.setGzpt(wbcxDto.getWbcxid());
		if(StringUtil.isBlank(wxyhDto.getSj())) {
			if(wxyhService.updateSj(wxyhDto)) {
				map.put("state", "success");
				map.put("message", "解绑成功!");
			}else {
				map.put("state", "fail");
				map.put("message", "解绑失败!");
			}
		}else {
			List<WxyhDto> wxyhlist=wxyhService.getWxyhListBySj(wxyhDto);
			int flag=0;
			if(wxyhlist != null && wxyhlist.size() > 0){
				for (int i = 0; i < wxyhlist.size(); i++){
					//原判断方法有误，故修改，2023/10/26
					if(StringUtil.isNotBlank(wxyhlist.get(i).getSj()) && wxyhlist.get(i).getSj().equals(wxyhDto.getSj())) {
						flag++;
					}
				}
			}
			if(flag==0) {
				boolean result=wxyhService.updateSj(wxyhDto);
				if(result) {
                    map.put("state", "success");
                }
					map.put("message", "绑定成功！");
			}else {
				map.put("state", "fail");
				map.put("message", "绑定失败，该手机号已有人绑定!");
			}
		}
		return map;
	}

	/**
	 * 查询报告列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/getPageListByysdh")
	public @ResponseBody Map<String, Object> getPageListByysdh(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		List<SjxxDto> sjxxList=null;
		boolean checkSign = commonService.checkSign(sjxxDto.getSign(),request);
		if(!checkSign){
			map.put("total", sjxxDto.getTotalNumber());
			map.put("rows", sjxxList);
			return map;
		}
		//原判断方法可能存在问题，故修改，2023/10/26
		if(StringUtil.isAnyBlank(sjxxDto.getHzxm(),sjxxDto.getYsdh())) {
			map.put("total", sjxxDto.getTotalNumber());
			map.put("rows", sjxxList);
			return map;
		}else {
			if(StringUtil.isNotBlank(sjxxDto.getLrry())){
				List<String> lrrylist = sjysxxService.getLrrylist(sjxxDto.getLrry(), null);
				sjxxDto.setLrrys(lrrylist);
			}
			sjxxList=sjxxService.getPagedReportList(sjxxDto);
			for (int i = 0; i < sjxxList.size(); i++) {
				sjxxList.get(i).setSign(commonService.getSign(sjxxList.get(i).getYbbh()));
			}
			map.put("total", sjxxDto.getTotalNumber());
			map.put("rows", sjxxList);
			return map;
		}
	}

	/**
	 * 发送短信验证码
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping("/getSms")
	public @ResponseBody Map<String, Object> sendSms(WxyhDto wxyhDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess = wxyhService.getSms(wxyhDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 接收服务器传送过来的要求，发送信息给相应的微信用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/sendOutMessage")
	@ResponseBody
	public String sendOutMessage(HttpServletRequest request){
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		try{
			String wxid = request.getParameter("wxid");
			String ybbh = request.getParameter("ybbh");
			String message = request.getParameter("message");
			String title = request.getParameter("title");
			String bgrq = request.getParameter("bgrq");
			if(StringUtil.isNotBlank(wxid)){
				String resultString = WeChatUtils.sendOutMsg(restTemplate, templateid,wxid, ybbh, message, title, bgrq, wbcxdm,redisUtil);
				log.info("sendReportTouser:" + resultString);
				return "OK";
			}else{
				return "Faild";
			}
		} catch (Exception e) {
			return "Faild";
		}
	}

	/**
	 * 根据送检ID获取标本详细结果
	 * @param sjxxjgDto
	 * @return
	 */
	@RequestMapping(value = "/getspeciessjxxjg")
	@ResponseBody
	public Map<String,Object> getSpeciesSjxxjg(SjxxjgDto sjxxjgDto){
		Map<String, Object> map= new HashMap<>();
		List<SjxxjgDto>	sjxxjglist=sjxxjgService.getxxjgByFjdidIsNull(sjxxjgDto);
		if(sjxxjglist!=null && sjxxjglist.size()>0) {
			List<SjxxjgDto> sjxxjgcllist=sjxxjgService.getxxInSpecies(sjxxjglist);
			List<SjxxjgDto> finalsjxxjglist= new ArrayList<>();
			if(sjxxjgcllist!=null && sjxxjgcllist.size() > 0) {
				int presize=0;
				for(int i=0;i<sjxxjgcllist.size();i++) {
					int size=sjxxjgcllist.get(i).getNp().length();
					if(i == 0){
						finalsjxxjglist.add(sjxxjgcllist.get(i));
					}else{
						if(presize < size){
							finalsjxxjglist.set(finalsjxxjglist.size()-1, sjxxjgcllist.get(i));
						}else{
							finalsjxxjglist.add(sjxxjgcllist.get(i));
						}
					}
					presize=size;
				}
				map.put("rows", finalsjxxjglist);
			}else {
				map.put("rows",new ArrayList<SjxxjgDto>());
			}
		}
		map.put("total",sjxxjgDto.getTotalNumber());
		return map;
	}

	/**
	 * 根据送检ID获取标本详细结果
	 * @param sjxxjgDto
	 * @return
	 */
	@RequestMapping(value = "/pagedataGenussjxxjg")
	@ResponseBody
	public Map<String,Object> pagedataGenussjxxjg(SjxxjgDto sjxxjgDto){
		Map<String, Object> map= new HashMap<>();
		List<SjxxjgDto> sjxxjglist=sjxxjgService.getxxjgByFjdidIsNull(sjxxjgDto);
		if(sjxxjglist!=null && sjxxjglist.size() > 0) {
			List<SjxxjgDto> genuslist=sjxxjgService.getxxInGenus(sjxxjglist);
			if(genuslist != null && genuslist.size() > 0) {
				map.put("rows",genuslist);
			}else {
				map.put("rows",new ArrayList<SjxxjgDto>());
			}
		}else {
			map.put("rows",new ArrayList<SjxxjgDto>());
		}
		map.put("total",sjxxjgDto.getTotalNumber());
		return map;
	}

	/**
	 * 访问路径封装(周报接口)
	 * @param req
	 * @return
	 */
	@RequestMapping("/packging_weekly")
	public ModelAndView alipayforward(HttpServletRequest req){
		ModelAndView mav=new ModelAndView("common/view/display_viewpc");
		String jsrqstart =req.getParameter("jsrqstart");
		String jsrqend =req.getParameter("jsrqend");
		String db=req.getParameter("db");
		String lrry=req.getParameter("lrry");
		String sign=req.getParameter("sign");
		String url=null;
		if(db!=null) {
			url = inspectionurl+"/wechat/getStatisView?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&db="+db+"&sign="+sign;
		}else if(lrry!=null) {
			url = inspectionurl+"/wechat/getStatisViewBylrry?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&lrry="+lrry+"&sign="+sign;
		}
		mav.addObject("view_url", url);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 销售周报统计页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/getStatisView")
	public ModelAndView getStatisView(SjxxDto sjxxDto,HttpServletRequest request) {
		//原判断方法可能存在问题，故修改，2023/10/26
		if(StringUtil.isBlank(sjxxDto.getDb())) {
            return commonService.jumpError();
		}
		boolean checkSjgn=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrqstart()+sjxxDto.getJsrqend()+sjxxDto.getDb(), request);
		if(!checkSjgn) {
            return commonService.jumpError();
		}
		ModelAndView mav=new ModelAndView("wechat/statis/week_statis_by_sjhb");
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
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend()+sjxxDto.getDb()));
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 周报统计图(通过合作伙伴)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/StatisView")
	@ResponseBody
	public Map<String, Object> StatisView(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<>();
		Map<String, String> tszqMap= new HashMap<>();
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
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
		boolean  checkSjgn=commonService.checkSign(sjxxDto.getSign(), sjxxDto.getJsrqstart()+sjxxDto.getJsrqend()+sjxxDto.getDb(), request);
		if(checkSjgn) {
			sjxxDto.setTj("day");
			//送检情况统计
			List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
			tszqMap.put("ybqk",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			resultmap.put("ybqk", ybqk);
			//送检标本类型（收费，不收费，废弃）
			List<Map<String, String>> ybxx=sjxxService.getYbxxByWeek(sjxxDto);
			tszqMap.put("ybxx",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			resultmap.put("ybxx", ybxx);
			List<Map<String, String>> sjdw=sjxxService.getYbxxBySjdw(sjxxDto);
			tszqMap.put("sjdw",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			//送检单位
			resultmap.put("sjdw", sjdw);
			resultmap.put("searchData",tszqMap);
		}
		return resultmap;
	}

	/**
	 * 设置按天查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(SjxxDto sjxxDto,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtil.isBlank(sjxxDto.getJsrqstart())) {
			if(length >=0 ) {
				sjxxDto.setJsrqstart(daySdf.format(new Date()));
				sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), length)));
			}else {
				sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), length)));
				sjxxDto.setJsrqend(daySdf.format(new Date()));
			}
		}else if(length ==0){
			sjxxDto.setJsrqend(sjxxDto.getJsrqstart());
		}
	}

	/**
	 * 设置周的日期
	 * @param sjxxDto
	 */
	private void setDateByWeek(SjxxDto sjxxDto) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtil.isBlank(sjxxDto.getJsrqstart())) {
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek - 7)));
				sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek-1)));
			} else{
				sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek)));
				sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), 6 - dayOfWeek)));
			}
		}
	}

	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(SjxxDto sjxxDto,int length) {

		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");

		Calendar calendar=Calendar.getInstance();

		if(StringUtil.isNotBlank(sjxxDto.getJsrqstart())) {
			Date jsStartDate = DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqstart());
			calendar.setTime(jsStartDate);
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
		sjxxDto.setJsrqstart(null);
		sjxxDto.setJsrqend(null);
	}

	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几年
	 */
	private void setDateByYear(SjxxDto sjxxDto,int length,boolean isBgFlag) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy");

		Calendar calendar=Calendar.getInstance();

		if(StringUtil.isNotBlank(sjxxDto.getJsrqstart())) {
			Date jsStartDate = DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqstart());
			calendar.setTime(jsStartDate);
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

		sjxxDto.setJsrqstart(null);
		sjxxDto.setJsrqend(null);
	}

	@RequestMapping("/Week_Statis_Tj")
	@ResponseBody
	public Map<String,Object> Week_Statis_Tj(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String method = request.getParameter("method");
		Map<String, String> tszqMap= new HashMap<>();
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("getYbqkByDay".equals(method)) {
			setDateByDay(sjxxDto,-6);
			sjxxDto.setTj("day");
			tszqMap.put("ybqk", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk", ybqk);
		}else if("getYbqkByWeek".equals(method)) {
			setDateByWeek(sjxxDto);
			tszqMap.put("ybqk", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> ybqk=sjxxService.getSjxxWeekBySy(sjxxDto);
			map.put("ybqk", ybqk);
		}else if("getYbqkByMon".equals(method)) {
			setDateByMonth(sjxxDto,-5);
			sjxxDto.setTj("mon");
			tszqMap.put("ybqk", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
			List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk", ybqk);
		}else if("getYbqkByYear".equals(method)) {
			setDateByYear(sjxxDto,-5,false);
			sjxxDto.setTj("year");
			tszqMap.put("ybqk", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());
			List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk", ybqk);
		}else if("getYbxxByDay".equals(method)) {
			setDateByDay(sjxxDto,0);
			sjxxDto.setTj("day");
			tszqMap.put("ybxx", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			//送检标本类型（收费，不收费，废弃）
			List<Map<String, String>> ybxx=sjxxService.getYbxxByWeek(sjxxDto);
			map.put("ybxx", ybxx);
		}else if("getYbxxByWeek".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTj("day");
			tszqMap.put("ybxx", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> ybxx=sjxxService.getYbxxByWeek(sjxxDto);
			map.put("ybxx", ybxx);
		}else if("getYbxxByMon".equals(method)) {
			setDateByMonth(sjxxDto,0);
			sjxxDto.setTj("mon");
			tszqMap.put("ybxx", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
			List<Map<String, String>> ybxx=sjxxService.getYbxxByWeek(sjxxDto);
			map.put("ybxx", ybxx);
		}else if("getYbxxByYear".equals(method)) {
			setDateByYear(sjxxDto,0,false);
			sjxxDto.setTj("year");
			tszqMap.put("ybxx", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());
			List<Map<String, String>> ybxx=sjxxService.getYbxxByWeek(sjxxDto);
			map.put("ybxx", ybxx);
		}else if("getSjdwByDay".equals(method)) {
			setDateByDay(sjxxDto,0);
			sjxxDto.setTj("day");
			tszqMap.put("sjdw", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> sjdw=sjxxService.getYbxxBySjdw(sjxxDto);
			//送检单位
			map.put("sjdw", sjdw);
		}else if("getSjdwByWeek".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTj("day");
			tszqMap.put("sjdw", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			List<Map<String, String>> sjdw=sjxxService.getYbxxBySjdw(sjxxDto);
			map.put("sjdw", sjdw);
		}else if("getSjdwByMon".equals(method)) {
			setDateByMonth(sjxxDto,-0);
			sjxxDto.setTj("mon");
			tszqMap.put("sjdw", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
			List<Map<String, String>> sjdw=sjxxService.getYbxxBySjdw(sjxxDto);
			//送检单位
			map.put("sjdw", sjdw);
		}else if("getSjdwByYear".equals(method)) {
			setDateByYear(sjxxDto,0,false);
			sjxxDto.setTj("year");
			tszqMap.put("sjdw", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());
			List<Map<String, String>> sjdw=sjxxService.getYbxxBySjdw(sjxxDto);
			//送检单位
			map.put("sjdw", sjdw);
		}
		map.put("searchData",tszqMap);
		return map;
	}

	//跳转faq界面
	@RequestMapping("/faq")
	public ModelAndView getfaqpage(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
								   String flg, HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/faq/faqxx");
		String id = request.getParameter("id");
		if(StringUtil.isNotBlank(id)){
			FaqxxDto faqxxDto=new FaqxxDto();
			faqxxDto.setWz(id);
			FaqxxDto dto = faqxxService.getFqaxxByWz(faqxxDto);
			String nr = dto.getNr();
			nr=nr.replaceAll("\n","<br>");
			dto.setNr(nr);
			mav.addObject("flag","search");
			mav.addObject("faqxxDto",dto);
			String s = JSON.toJSONString(dto);
			mav.addObject("str",s);
		}else{
			mav.addObject("flag","all");
		}
		return mav;
	}


	/**
	 * 销售周报统计页面(通过录入人员)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/getStatisViewBylrry")
	public ModelAndView getStatisViewBylrry(SjxxDto sjxxDto,HttpServletRequest request) {
		if(StringUtil.isBlank(sjxxDto.getLrry())) {
			return commonService.jumpError();
		}
		boolean checkSjgn=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrqstart()+sjxxDto.getJsrqend()+sjxxDto.getLrry(), request);
		if(!checkSjgn) {
			return commonService.jumpError();
		}
		ModelAndView mav=new ModelAndView("wechat/statis/week_statis_by_lrry");
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
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend()+sjxxDto.getLrry()));
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 周报统计图(通过录入人员)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/StatisViewBylrry")
	@ResponseBody
	public Map<String, Object> StatisViewBylrry(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<>();
		Map<String, String> tszqMap= new HashMap<>();
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
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
		boolean  checkSjgn=commonService.checkSign(sjxxDto.getSign(), sjxxDto.getJsrqstart()+sjxxDto.getJsrqend()+sjxxDto.getLrry(), request);
		if(checkSjgn) {
			sjxxDto.setTj("day");
			//送检情况统计
			List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
			tszqMap.put("ybqk",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			resultmap.put("ybqk", ybqk);
			//送检标本类型（收费，不收费，废弃）
			List<Map<String, String>> ybxx=sjxxService.getYbxxByWeek(sjxxDto);
			tszqMap.put("ybxx",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			resultmap.put("ybxx", ybxx);
			List<Map<String, String>> sjdw=sjxxService.getYbxxBySjdw(sjxxDto);
			tszqMap.put("sjdw",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
			//送检单位
			resultmap.put("sjdw", sjdw);
			resultmap.put("searchData",tszqMap);
		}
		return resultmap;
	}

	/**
	 * 跳转反馈页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/inspection/feedBack")
	@ResponseBody
	public ModelAndView  feedBack(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/informed/feedback");
		SjxxDto sjxxDto2=sjxxService.getDto(sjxxDto);
		sjxxDto2.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		//查看附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto2.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("sjxxDto", sjxxDto2);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		return mav;
	}

	/**
	 * 保存临床反馈
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/feedback/saveFeedBack")
	@ResponseBody
	public Map<String,Object> saveFeedBack(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjxxService.updateFeedBack(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * PDF预览(不需要签名),预览8085上的文件
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value="/file/pdfView")
	public ModelAndView pdfView(FjcfbDto fjcfbDto) {
		String filepath = "/wechat/file/getFileInfo?fjid=" + fjcfbDto.getFjid();
		ModelAndView mv = new ModelAndView("wechat/informed/feedback_viewer");
		mv.addObject("file",filepath);
		return mv;
	}

	/**
	 * 文件下载（不需要签名）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/file/download")
	public String download(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
		FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(t_fjcfbDto==null)
		{
			System.out.println("对不起，系统未找到相应文件");
			return "对不起，系统未找到相应文件";
		}
		String wjlj = t_fjcfbDto.getWjlj();
		String wjm = t_fjcfbDto.getWjm();
		DBEncrypt crypt = new DBEncrypt();
		String filePath = crypt.dCode(wjlj);
		String agent = request.getHeader("user-agent");
		try {
			if(wjm != null){
				byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
				wjm = new String(bytes, "ISO-8859-1");
				response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//指明为下载
		response.setHeader("content-type", "application/octet-stream");

		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream;
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
			e.printStackTrace();
		}
		try {
			bis.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 微信公众号反馈按钮
	 * @param code
	 * @param state
	 * @param flg
	 * @param request
	 * @return
	 */
	@RequestMapping("/feedback/feedBackMenu")
	public ModelAndView feedBackMenu(SjxxDto sjxxDto,@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
									 String flg, HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/informed/wechat_feedBack");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		String organ = request.getParameter("organ");
		if(StringUtil.isNotBlank(organ)){
			sjxxDto.setLrry(organ);
		}else{
			WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code, state, wbcxdm);
			if(userModel != null) {
				sjxxDto.setLrry(userModel.getOpenid());
			}
		}
		String sign = commonService.getSign(sjxxDto.getLrry());
		sjxxDto.setSign(sign);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("wbcxdm", wbcxdm);
		return mav;
	}

	/**
	 * 查询没有进行过反馈的送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/feedback/getNofeefback")
	@ResponseBody
	public Map<String,Object> getNofeefback(SjxxDto sjxxDto,HttpServletRequest req){
		Map<String,Object> map= new HashMap<>();
		boolean result=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getLrry(), req);
		if(result) {
			if(StringUtil.isNotBlank(sjxxDto.getLrry())) {
				List<String> lrrylist = sjysxxService.getLrrylist(sjxxDto.getLrry(), null);
				sjxxDto.setLrrys(lrrylist);
				List<SjxxDto> sjxxList=sjxxService.getNofeefback(sjxxDto);
				if(sjxxList!=null&&sjxxList.size()>0) {
					for (int i = 0; i < sjxxList.size(); i++){
						String sign = commonService.getSign(sjxxList.get(i).getYbbh());
						try {
							sign = URLEncoder.encode(commonService.getSign(sjxxList.get(i).getYbbh()), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sjxxList.get(i).setSign(sign);
					}
					map.put("sjxxList",sjxxList);
				}
			}
		}
		return map;
	}

	/**
	 * 送检医生信息页面
	 * @param sjysxxDto
	 * @return
	 */
	@RequestMapping("/sjysxx/getSjysxxList")
	public ModelAndView getSjysxxList(SjysxxDto sjysxxDto) {
		ModelAndView mav=new ModelAndView("wechat/informed/wechat_sjysxxList");
		List<SjysxxDto> sjysList;
		List<String> lrrylist=sjysxxService.getLrrylist(sjysxxDto.getWxid(), sjysxxDto.getDdid());
		sjysxxDto.setLrrylist(lrrylist);
		int count=sjysxxService.getCountByWxid(sjysxxDto);
		if(count>10) {
			sjysList=sjysxxService.selectTreeSjysxx(sjysxxDto);
		}else {
			sjysList=sjysxxService.getDtoList(sjysxxDto);
		}
		Map<String, List<JcsjDto>> g_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		//检测单位
		mav.addObject("unitlist", g_jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		//送检科室
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		mav.addObject("sjysList", sjysList);
		mav.addObject("sjysxxDto", sjysxxDto);
		mav.addObject("sjdwlist", sjdwlist);
		return mav;
	}

	/**
	 * 送检医生信息返回信息
	 * @param sjysxxDto
	 * @return
	 */
	@RequestMapping("/sjysxx/getSjysxxListInfo")
	@ResponseBody
	public Map<String,Object> getSjysxxListInfo(SjysxxDto sjysxxDto) {
		Map<String,Object> map=new HashMap<>();
		List<SjysxxDto> sjysList;
		List<String> lrrylist=sjysxxService.getLrrylist(sjysxxDto.getWxid(), sjysxxDto.getDdid());
		sjysxxDto.setLrrylist(lrrylist);
		sjysList=sjysxxService.selectTreeSjysxx(sjysxxDto);
		map.put("sjysList", sjysList);
		return map;
	}

	/**
	 * 新增送检医生信息
	 * @param sjysxxDto
	 * @return
	 */
	@RequestMapping("/sjysxx/saveSjysxx")
	@ResponseBody
	public Map<String,String> saveSjysxx(SjysxxDto sjysxxDto){
		Map<String,String> map= new HashMap<>();
		int count=sjysxxService.getCount(sjysxxDto);
		boolean result = false;
		if(count==0) {
			if(StringUtil.isBlank(sjysxxDto.getYsid())){
				sjysxxDto.setYsid(StringUtil.generateUUID());
			}
			result=sjysxxService.insert(sjysxxDto);
		}

		map.put("status",result?"success" : "fail");
		map.put("message",result?"保存成功!" : "保存失败！");
		return map;
	}

	/**
	 * 删除送检医生信息
	 * @param ysid
	 * @return
	 */
	@RequestMapping("/sjysxx/deleteSjysxx")
	@ResponseBody
	public Map<String,String> deleteSjysxx(String ysid){
		Map<String,String> map= new HashMap<>();
		boolean result=sjysxxService.deleteById(ysid);
		map.put("state",result+"");
		return map;
	}

	/**
	 * 注册查看页面
	 * @return
	 */
	@RequestMapping("/register/registerView")
	public ModelAndView registerView() {
        return new ModelAndView("wechat/register/register_view");
	}

	/**
	 * 手机端注册信息录入
	 * @param code
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping("/register/registerAdd")
	public ModelAndView registerAdd(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,
									HttpServletRequest request){
		String xxlx = request.getParameter("xxlx");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		if(StringUtil.isBlank(xxlx)) {
            return commonService.jumpError();
		}else {
			ModelAndView mav=new ModelAndView("wechat/register/register_add");
			String wxid = request.getParameter("wxid");
			if(StringUtil.isBlank(wxid)){
				wxid = request.getParameter("organ");
				if(StringUtil.isBlank(wxid)){
					WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code, state, wbcxdm);
					if(userModel == null || StringUtil.isBlank(userModel.getOpenid())){
						mav = commonService.jumpError();
						return mav;
					}
					wxid = userModel.getOpenid();
					WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, wxid, wbcxdm,redisUtil);
					log.error("subscribe关注，1为关注 :"+userInfo.getSubscribe());
					/*if(!"1".equals(userInfo.getSubscribe())) {
						//重定向到关注页面
						log.error("重定向到杰毅检验关注页面!");
						String url = "redirect:https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=Mzg3NjE0NDI3MQ==&scene=124#wechat_redirect"; 
				    	return new ModelAndView(url);
					}*/
				}
			}
			XxdjDto xxdjDto = new XxdjDto();
			xxdjDto.setXxlx(xxlx);
			xxdjDto.setWxid(wxid);
			XxdjDto t_xxdjDto = xxdjService.getDto(xxdjDto);
			String flg = request.getParameter("flg");
			if(t_xxdjDto != null){
				xxdjDto = t_xxdjDto;
			}
			if(t_xxdjDto != null && StringUtil.isBlank(flg)){
				XtszDto xtszDto = new XtszDto();
				xtszDto.setSzlb(GlobalString.WECAHT_RESULT_MESSAGE);
				xtszDto = xtszService.getDto(xtszDto);
				String xxnr="";
				if(xtszDto != null && StringUtil.isNotBlank(xtszDto.getSzz())){
					xxnr = StringUtil.replaceMsgByObject(xtszDto.getSzz(), t_xxdjDto);
				}
				mav = new ModelAndView("wechat/register/register_confirm");
				mav.addObject("xxnr", xxnr);
			}else{
				XxpzDto xxpzDto=new XxpzDto();
				xxpzDto.setXxlx(xxlx);
				List<XxpzDto> xxpzList=xxpzService.getDtoList(xxpzDto);
				mav.addObject("xxpzList", xxpzList);
			}
			mav.addObject("xxdjDto", xxdjDto);
			mav.addObject("wbcxdm", wbcxdm);
			return mav;
		}
	}

	/**
	 * 通过wxid和xxlx获取信息登记
	 * @param xxdjDto
	 * @return
	 */
	@RequestMapping("/register/getXxdj")
	@ResponseBody
	public XxdjDto getXxdj(XxdjDto xxdjDto){
        return xxdjService.getDto(xxdjDto);
	}
	/**
	 * 保存信息登记
	 * @param xxdjDto
	 * @return
	 */
	@RequestMapping("/register/saveRegister")
	@ResponseBody
	public Map<String,String> saveRegister(XxdjDto xxdjDto){
		Map<String,String> map= new HashMap<>();
		boolean result=xxdjService.saveRegister(restTemplate, xxdjDto);
		if(result) {
			map.put("state", "success");
		}else {
			map.put("state", "error");
		}
		return map;
	}

	/**
	 * 登记信息确认页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/register/registerConfirm")
	public ModelAndView registerConfirm(HttpServletRequest request){
		String xxlx = request.getParameter("xxlx");
		String wxid = request.getParameter("wxid");
		if(StringUtil.isAnyBlank(xxlx,wxid)){
			return commonService.jumpError();
		}
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.WECAHT_RESULT_MESSAGE);
		xtszDto = xtszService.getDto(xtszDto);
		ModelAndView mav=new ModelAndView("wechat/register/register_confirm");
		XxdjDto xxdjDto = new XxdjDto();
		xxdjDto.setXxlx(xxlx);
		xxdjDto.setWxid(wxid);
		xxdjDto = xxdjService.getDto(xxdjDto);
		mav.addObject("xxdjDto", xxdjDto);
		//替换
		String xxnr="";
		if(xtszDto != null && StringUtil.isNotBlank(xtszDto.getSzz())){
			xxnr = StringUtil.replaceMsgByObject(xtszDto.getSzz(), xxdjDto);
		}
		mav.addObject("xxnr", xxnr);
		return mav;
	}


	/**
	 * 送检清单点击后获取送检结果信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/getFeedView")
	public ModelAndView getFeedView(SjxxDto sjxxDto, HttpServletRequest request){
		boolean checkSign = commonService.checkSign(sjxxDto.getSign(), sjxxDto.getYbbh(), request);
		if(!checkSign){
            return commonService.jumpError();
		}
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_feedbackView");
		SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		List<String> ywlxs = new ArrayList<>();
		ywlxs.add(BusTypeEnum.IMP_REPORT_C_TEMEPLATE_D.getCode());
		ywlxs.add(BusTypeEnum.IMP_REPORT_QINDEX_TEMEPLATE_D.getCode());
		fjcfbDto.setYwlxs(ywlxs);
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		//查看图片
		FjcfbDto fjcfbDto_fj=new FjcfbDto();
		fjcfbDto_fj.setYwid(sjxxDto.getSjid());
		fjcfbDto_fj.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> fjcfbDtos_fjs = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_fj);
		mav.addObject("fjcfbDtos_fjs",fjcfbDtos_fjs);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);
		return mav;
	}

	@RequestMapping("/getIP")
	@ResponseBody
	public String getIP(HttpServletRequest request) {
		log.error(request.getHeader("Proxy-Client-IP"));
		log.error(request.getHeader("WL-Proxy-Client-IP"));
		log.error(request.getHeader("HTTP_CLIENT_IP"));
		log.error(request.getHeader("HTTP_X_FORWARDED_FOR"));
		log.error(request.getRemoteAddr());
		log.error(request.getLocalAddr());
		return null;
	}

	/**
	 * 保存客户端提交的上传文件(js-sdk)
	 * @param request
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value="/file/saveTempFile")
	@ResponseBody
	public Map<String, Object> saveTempFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		result.put("wbcxdm", wbcxdm);
		//根据日期创建文件夹
		String storePath = prefix + tempFilePath + fjcfbDto.getYwlx()+"/"+ "UP"+
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		mkDirs(storePath);

		Map<String,Object> mFile = new HashMap<>();
		String fileID = StringUtil.generateUUID();
		fjcfbDto.setFjid(fileID);
		//获取时间戳当做文件名
		String fileName = DateUtils.getTimeSuffix() + ".jpg";
		mFile.put("wjm", fileName);
		fjcfbDto.setWjm(fileName);
		int index = (fileName.lastIndexOf(".") > 0) ? fileName.lastIndexOf(".") : fileName.length() - 1;

		String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
		String suffix = fileName.substring(index);
		String saveName = t_name + suffix;
		String backName = t_name + "_back" + suffix;

		mFile.put("fwjlj", storePath);
		mFile.put("fwjm", saveName);
		mFile.put("ywlx", fjcfbDto.getYwlx());

		String tempPath = storePath + "/" + saveName;
		String backPath = null;
		if(".doc".equalsIgnoreCase(suffix) || ".docx".equalsIgnoreCase(suffix) || ".xls".equalsIgnoreCase(suffix)
				|| ".xlsx".equalsIgnoreCase(suffix) || ".pdf".equalsIgnoreCase(suffix)) {
			backPath = storePath + "/" + backName;
			mFile.put("b_wjlj", backPath);
		}
		boolean flag = WeChatUtils.getFileStream(restTemplate, fjcfbDto.getMediaid(), tempPath, backPath, wbcxdm,redisUtil);
		if (!flag) {
			result.put("status", "fail");
			result.put("msg", xxglService.getModelById("ICOM00002").getXxnr());
			return result;
		}
		mFile.put("wjlj", storePath + "/" + saveName);
		mFile.put("kzcs1", fjcfbDto.getKzcs1());
		mFile.put("kzcs2", fjcfbDto.getKzcs2());
		mFile.put("kzcs3", fjcfbDto.getKzcs3());
		fjcfbDto.setLsbcdz( storePath + "/" + saveName);
		//失效时间1小时
		redisUtil.hmset("IMP_:_"+fileID, mFile,3600);
		result.put("fjcfbDto", fjcfbDto);
		result.put("status", "success");
		result.put("msg", xxglService.getModelById("ICOM00001").getXxnr());
		return result;
	}

	/**
	 * 获取送检权限列表
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping("/getSjqxList")
	@ResponseBody
	public String getSjqxList(HttpServletRequest request, SjqxDto sjqxDto){
		String unionid = request.getParameter("unionid");
		sjqxDto.setWxid(unionid);
		List<SjqxDto> sjqxList = sjqxService.getDtoList(sjqxDto);
		if(sjqxList == null || sjqxList.size() == 0) {
            return null;
        }
		return JSONObject.toJSONString(sjqxList);
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

	/**
	 * 抽奖查看页面
	 * @return
	 */
	@RequestMapping("/lottery/lotteryView")
	public ModelAndView lotteryView() {
        return new ModelAndView("wechat/register/lottery_view");
	}

	/**
	 * 微信端下载报告页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/file/downloadFile")
	public ModelAndView downloadFilePage(HttpServletRequest request) {
		String fjid=request.getParameter("fjid");
		String sign=request.getParameter("sign");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setFjid(fjid);
		fjcfbDto.setSign(sign);
		ModelAndView mav=new ModelAndView("wechat/file/downloadfile");
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}

	/**
	 * 根据伙伴查询检测单位
	 * @return
	 */
	@RequestMapping("/selectDetectionUnit")
	@ResponseBody
	public Map<String, Object> selectDetectionUnit(String hbmc){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> jcsjlist = sjxxService.getDetectionUnit(hbmc);
		map.put("unitList", jcsjlist);
		return map;
	}

	/**
	 * 根据检测项目查询检测子项目
	 * @param jcxmid
	 * @return
	 */
	@RequestMapping("/getSubDetect")
	@ResponseBody
	public Map<String, Object> getSubDetect(String jcxmid){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> jcsjlist = sjxxService.getSubDetectWithoutDisabled(jcxmid);
		map.put("subdetectlist", jcsjlist);
		return map;
	}

	/**
	 * 清空检测检测子项目
	 */
	@RequestMapping("/emptySubDetect")
	@ResponseBody
	public Map<String, Object> emptySubDetect(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		SjxxDto dto = sjxxService.getDto(sjxxDto);
		if (StringUtil.isNotBlank(dto.getXgsj()) && StringUtil.isNotBlank(sjxxDto.getXgsj()) && !dto.getXgsj().equals(sjxxDto.getXgsj())){
			map.put("status", "fail");
			map.put("message", "该送检信息已被修改，请刷新页面后重新操作！");
			return map;
		}
		boolean isSuccess = sjxxService.emptySubDetect(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"修改成功！":"修改失败！");
		SjxxDto sjxxDto1 = sjxxService.getDto(sjxxDto);
		map.put("xgsj", sjxxDto1.getXgsj());
		return map;
	}

	/**
	 * 获取检测项目录入所需的基础数据
	 */
	@RequestMapping("/getInspReportBasicData")
	@ResponseBody
	public Map<String, Object> getInspReportBasicData(){
		Map<String,Object> map = new HashMap<>();
//		List<JcsjDto> sdList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE));//快递类型
		List<JcsjDto> jcxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
//		List<JcsjDto> jczxmList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DOCUMENT_SUBTYPE.getCode()));检测子项目
//		List<JcsjDto> jczdwList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		map.put("jcxmList",jcxmList);
//		map.put("jcxmList",jcxmList);
//		map.put("jczxmList",jczxmList);
//		map.put("sdList",sdList);
		return map;
	}
	/**
	 * 获取快递录入所需的基础数据
	 */
	@RequestMapping("/getSdData")
	@ResponseBody
	public Map<String, Object> getSdData(){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> sdList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE));//快递类型

		map.put("sdList",sdList);
		return map;
	}


	/**
	 * 获取检测单位录入所需的基础数据
	 */
	@RequestMapping("/getJcdwData")
	@ResponseBody
	public Map<String, Object> getJcdwData(){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> unitList = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT));//单位类型

		map.put("unitList",unitList);
		return map;
	}


	/**
	 * 获取科室录入所需的基础数据
	 */
	@RequestMapping("/getKsData")
	@ResponseBody
	public Map<String, Object> getKsData(){
		Map<String,Object> map = new HashMap<>();
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("sjdwlist",sjdwlist);
		return map;
	}

	@RequestMapping(value="/qrCodeViewReport")
	public ModelAndView qrCodeViewReport(FjcfbDto fjcfbDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("fjid", fjcfbDto.getFjid());
		paramMap.add("ywlx", fjcfbDto.getYwlx());
		Object fjcfb_word = restTemplate.postForObject("https://medlab.matridx.com/ws/qrCodeReport/getFjcfbInfo", paramMap, Object.class);
		if (fjcfb_word != null){
			//word存在,根据ywlx和ywid查找对应的PDF报告
			String wordYwlx = fjcfbDto.getYwlx();
			String pdfYwlx = wordYwlx.replace("_WORD", "");
			FjcfbDto fjcfb_pdf = new FjcfbDto();
			fjcfb_pdf.setYwlx(pdfYwlx);
			fjcfb_pdf.setYwid((String) ((LinkedHashMap) fjcfb_word).get("ywid"));
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfb_pdf);
			if (fjcfbDtos.size()==0){
				//PDF报告不存在
				ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportError");
				mav.addObject("errorText","PDF报告不存在，请稍后再试！");
				return mav;
			}
			if (fjcfbDtos.size()>1){
				//PDF报告存在多份
				ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportError");
				mav.addObject("errorText","PDF报告查询出错，请联系工作人员！");
				return mav;
			}
			ModelAndView mav = new ModelAndView("common/view/display_view");
			String view_url = "/ws/file/pdfPreview?fjid="+fjcfbDtos.get(0).getFjid();
			mav.addObject("view_url", view_url);
			mav.addObject("urlPrefix", urlPrefix);
			return mav;
		}else {
			//word不存在，说明该份报告已作废
			ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportError");
			mav.addObject("errorText","当前报告已作废！");
			return mav;
		}
	}

	/**
	 * 跳转调整页面
	 * @param sjsyglDto
	 * @return
	 */
	@RequestMapping("/inspection/adjust")
	@ResponseBody
	public ModelAndView  adjust(SjsyglDto sjsyglDto,String wxid) {
		ModelAndView mav=new ModelAndView("wechat/informed/wechat_adjust");
		List<SjsyglDto> sjsyglDtos = sjsyglService.getDtoList(sjsyglDto);
		String db="";
		String ids="";
		String sjid="";
		if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
			db=sjsyglDtos.get(0).getDb();
			sjid=sjsyglDtos.get(0).getSjid();
			for(SjsyglDto dto:sjsyglDtos){
				ids=ids+","+dto.getSyglid();
			}
		}
		if(StringUtil.isNotBlank(ids)){
			ids=ids.substring(1);
		}
		mav.addObject("sjsyglDtos", sjsyglDtos);
		mav.addObject("db", db);
		mav.addObject("ids", ids);
		mav.addObject("sjid", sjid);
		mav.addObject("wxid", wxid);
		mav.addObject("hzxm", sjsyglDto.getHzxm());
		mav.addObject("ybbh", sjsyglDto.getYbbh());
		return mav;
	}

	/**
	 * 调整保存
	 * @param sjsyglDto
	 * @param wxid
	 * @return
	 */
	@RequestMapping(value="/miniprogram/adjustSave")
	@ResponseBody
	public Map<String, Object> adjustSave(SjsyglDto sjsyglDto,String wxid,String sjid){
		Map<String,Object> map = new HashMap<>();
		List<SjsyglDto> list = (List<SjsyglDto>) JSON.parseArray(sjsyglDto.getSy_json(), SjsyglDto.class);
		boolean isJcdwSame = true;
		String jcdw = list.get(0).getJcdw();
		for(SjsyglDto dto:list){
			dto.setXgry(wxid);
			if (isJcdwSame && !jcdw.equals(dto.getJcdw())){
				isJcdwSame = false;
			}
		}
		//更新实验管理表数据
		Boolean isSuccess = sjsyglService.updateList(list);
		if (isSuccess && isJcdwSame && StringUtil.isNotBlank(sjid)){
			//若所有检测单位都相同，则更新送检信息的检测单位
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setSjid(sjid);
			sjxxDto.setXgry(wxid);
			sjxxDto.setJcdw(jcdw);
			sjxxService.updateJcdw(sjxxDto);
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?"保存成功！":"保存失败！");
		List<SjsyglDto> sjsyglDtos=sjsyglService.getDtoList(sjsyglDto);

		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.SJSY_MOD.getCode() + JSONObject.toJSONString(sjsyglDtos));
		return map;
	}



	@RequestMapping("/statistics/wxRemark")
	public ModelAndView wxRemark(String remark) {
		ModelAndView mav = new ModelAndView("wechat/wxRemark/wx_remak");
		//将空格替换为换行
		if (StringUtil.isBlank(remark)) {
			remark = "暂无信息！";
		}
		String[] s = remark.split("\r\n");
		List<String> strings = Arrays.asList(s);
		mav.addObject("remarks", strings);
		return mav;
	}
	/**
	 * 获取检测项目以及伙伴收费标准
	 * @param sjid
	 * @return
	 */
	@RequestMapping(value="/inspection/getDetectionPayInfo")
	@ResponseBody
	public List<Map<String, Object>> getDetectionPayInfo(String sjid){
		//获取送检检测项目以及对应项目收费标准
        return sjjcxmService.getDetectionPayInfo(sjid);
	}
	/**
	 * 获取伙伴收费标准
	 * @param hbsfbzDto
	 * @return
	 */
	@RequestMapping(value="/getChargingStandards")
	@ResponseBody
	public Map<String, Object> getChargingStandards(HbsfbzDto hbsfbzDto){
		Map<String,Object> map = new HashMap<>();
		List<HbsfbzDto> dtoList = hbsfbzService.getDtoList(hbsfbzDto);
		map.put("hbsfbzDtos",dtoList);
		return map;
	}

	/**
	 * 获取当前sijd全部检测项目
	 * @param sjid
	 * @return
	 */
	@RequestMapping(value = "/getSjjcxmDtos")
	@ResponseBody
	public Map<String, Object> getSjjcxmDtos(String sjid){
		Map<String, Object> map = new HashMap<>();
		SjjcxmDto sjjcxmDto=new SjjcxmDto();
		sjjcxmDto.setSjid(sjid);
		List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getDtoList(sjjcxmDto);
		map.put("sjjcxmDtos", sjjcxmDtos);
		return map;

	}


	/*投诉清单页面初始化*/
	@RequestMapping(value="/inspComplaint")
	public ModelAndView inspComplaint(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspComplaintList");
		String wxid = request.getParameter("wxid");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		String ywid = request.getParameter("ywid");
		String sign = commonService.getSign(request.getParameter("ybbh"));
		mav.addObject("wxid", wxid);
		mav.addObject("wbcxdm", wbcxdm);
		mav.addObject("ywid", ywid);
		mav.addObject("sign", sign);
		return mav;
	}


	/**
	 * 送检清单显示页面
	 * @param code
	 * @param state
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/inspComplaintListByAuth")
	public ModelAndView inspComplaintListByAuth(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspComplaintList");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		String wxid = "";
		String organ = request.getParameter("organ");
		if(StringUtil.isNotBlank(organ)){
			wxid = organ;
		}else{
			WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code,state, wbcxdm);
			if(userModel != null) {
				wxid = userModel.getOpenid();
			}
		}
		request.setAttribute("wxid", wxid);
		Map<String, Object> map = inspComplaintList(request);
		mav.addAllObjects(map);
		mav.addObject("flag", "list");
		mav.addObject("wbcxdm", wbcxdm);
		return mav;
	}

	/*投诉清单*/
	@RequestMapping(value="/inspComplaintList")
	@ResponseBody
	public Map<String,Object> inspComplaintList(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String wxid = request.getParameter("wxid");
		//wbcxdm未被使用，故注释，2023/10/26
//		String wbcxdm = request.getParameter("wbcxdm");
//		if(StringUtil.isBlank(wbcxdm)){
//			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
//		}
		String ywid = request.getParameter("ywid");
		String ycqf = "";
		List<JcsjDto> ycqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
		if (ycqfList!=null && ycqfList.size()>0) {
			for (JcsjDto jc_ycqf : ycqfList) {
				if ("WECHAT_EXCEPTION".equals(jc_ycqf.getCsdm())){
					ycqf = jc_ycqf.getCsid();
					break;
				}
			}
		}
		//获取送检异常信息
		SjycWechatDto sjycDto = new SjycWechatDto();
		sjycDto.setYhid(wxid);
		if (StringUtil.isNotBlank(ywid)){
			sjycDto.setYwid(ywid);
		}
		sjycDto.setYcqf(ycqf);
		String pageNumber = StringUtil.isNotBlank(request.getParameter("pageNumber"))?request.getParameter("pageNumber"):"1";
		String pageSize = StringUtil.isNotBlank(request.getParameter("pageSize"))?request.getParameter("pageSize"):"15";
		sjycDto.setPageNumber(Integer.parseInt(pageNumber));
		sjycDto.setPageSize(Integer.parseInt(pageSize));
		sjycDto.setPageStart((sjycDto.getPageNumber()-1)*sjycDto.getPageSize());
		List<SjycWechatDto> sjycList = sjycService.getDtoBySjid(sjycDto);
		if (sjycList!=null && sjycList.size()>0) {
			for (SjycWechatDto sjyc : sjycList) {
				String unreadcnt = "0";
				Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + wxid);
				if (exceptionConectMap_redis != null){
					JSONObject exceptionConectMap = JSON.parseObject(exceptionConectMap_redis.toString());
					JSONObject exceptionlist = JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
					if (exceptionlist.get(sjyc.getYcid()) != null) {
						JSONObject exceptionMap = JSON.parseObject(exceptionlist.get(sjyc.getYcid()).toString());
						unreadcnt = exceptionMap.get("ex_unreadcnt").toString();
					}
				}
				sjyc.setCount(unreadcnt);
			}
		}
		map.put("rows", sjycList);
		return map;
	}

	/*投诉编辑页面初始化*/
	@RequestMapping(value="/inspComplaintEdit")
	public ModelAndView inspComplaintEdit(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspComplaintEdit");
		Map<String, Object> map = inspComplaintEditMap(request);
		mav.addAllObjects(map);
		return mav;
	}

	/*投诉编辑页面初始化Map*/
	@RequestMapping(value="/inspComplaintEditMap")
	@ResponseBody
	public Map<String,Object> inspComplaintEditMap(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String wxid = request.getParameter("wxid");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm", wbcxdm);
		String ywid = request.getParameter("ywid");
		String ycqf = request.getParameter("ycqf");
		if (StringUtil.isBlank(ycqf)){
			List<JcsjDto> ycqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
			if (ycqfList!=null && ycqfList.size()>0) {
				for (JcsjDto jc_ycqf : ycqfList) {
					if ("WECHAT_EXCEPTION".equals(jc_ycqf.getCsdm())){
						ycqf = jc_ycqf.getCsid();
						break;
					}
				}
			}
		}
		List<JcsjDto> yctslxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_STATISTICS.getCode());
		List<JcsjDto> yctslx_list=new ArrayList<>();
		for(JcsjDto jcsjDto:yctslxList){
			if(ycqf.equals(jcsjDto.getFcsid())){
				yctslx_list.add(jcsjDto);
			}
		}
		map.put("yctslx_list", yctslx_list);
		SjycWechatDto sjycDto = new SjycWechatDto();
		sjycDto.setYwid(ywid);
		sjycDto.setYcqf(ycqf);
		sjycDto.setTwr(wxid);
		map.put("sjycDto", sjycDto);
		int count = sjycService.getCountBySjid(sjycDto);
		map.put("count", count);
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setSjid(ywid);
		sjxxDto = sjxxService.getDto(sjxxDto);
		map.put("sjxxDto", sjxxDto);
		map.put("backpage",request.getParameter("backpage"));
		map.put("ycqf",ycqf);
		map.put("ywlx",BusTypeEnum.IMP_EXCEPTION.getCode());

		Object noticeRoleType = redisUtil.hget("matridx_xtsz", "wechat.exception.notice.roletype");
		if(noticeRoleType!=null){
			XtszDto xtsz = JSON.parseObject(noticeRoleType.toString(),XtszDto.class);
			String tzjs = xtsz.getSzz();
			map.put("qrjs", tzjs);
			map.put("qrlx", DingNotificationTypeEnum.ROLE_TYPE.getCode());

		}
		return map;
	}

	/*投诉编辑页面基础数据*/
	@RequestMapping(value="/inspComplaintBasicData")
	@ResponseBody
	public Map<String,Object> inspComplaintBasicData(){
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> yclxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_TYPE.getCode());//异常类型
		List<JcsjDto> yczlxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SUBCLASSEXCEPTION_TYPE.getCode());//异常子类型
		map.put("yclxList", yclxList);
		map.put("yczlxList", yczlxList);
		return map;
	}


	/**
	 * 新增异常保存
	 * @return
	 */
	@RequestMapping("/inspComplaintSave")
	@ResponseBody
	public Map<String,Object> inspComplaintSave(SjycWechatDto sjycDto){
		sjycDto.setLrry(sjycDto.getWxid());
//		sjycDto.setQrrmc(user.getZsxm());
		Map<String,Object> map= new HashMap<>();
		if (StringUtil.isBlank(sjycDto.getYcqf())){
			List<JcsjDto> ycqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
			if (ycqfList!=null && ycqfList.size()>0) {
				for (JcsjDto jc_ycqf : ycqfList) {
					if ("WECHAT_EXCEPTION".equals(jc_ycqf.getCsdm())){
						sjycDto.setYcqf(jc_ycqf.getCsid());
						break;
					}
				}
			}
		}
		boolean isSuccess=sjycService.addSaveException(sjycDto);
		WxyhDto wxyhDto=wxyhService.getDtoById(sjycDto.getWxid());
		sjycDto.setLrrymc(wxyhDto.getWxm());
		sjycDto.setTwrmc(wxyhDto.getWxm());
		Map<String,Object> rabbitMap=new HashMap<>();
		rabbitMap.put("type","add");
		rabbitMap.put("sjycDto",sjycDto);
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SSE_SENDMSG_EXCEPRION_MATRIDX.getCode(), JSON.toJSONString(rabbitMap));
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 异常置顶/取消置顶
	 * @param sjyczdDto
	 * @return
	 */
	@RequestMapping("/topComplaint")
	@ResponseBody
	public Map<String, Object> pagedataTopComplaint(SjyczdWechatDto sjyczdDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		//如果是置顶则添加，否则删除
		if ("1".equals(sjyczdDto.getSfzd())) {
			isSuccess = sjyczdService.insert(sjyczdDto);
		} else {
			isSuccess = sjyczdService.delete(sjyczdDto);
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 结束投诉
	 * @param sjycDto
	 * @return
	 */
	@RequestMapping("/finishComplaint")
	@ResponseBody
	public Map<String, Object> finishComplaint(SjycWechatDto sjycDto) {
		//结束投诉
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = sjycService.finishYc(sjycDto);
		Map<String,Object> rabbitMap=new HashMap<>();
		rabbitMap.put("type","finish");   
		rabbitMap.put("sjycDto",sjycDto);
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SSE_SENDMSG_EXCEPRION_MATRIDX.getCode(), JSON.toJSONString(rabbitMap));
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 投诉详情
	 * @return
	 */
	@RequestMapping(value="/inspComplaintInfo")
	public ModelAndView inspComplaintInfo(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_inspComplaintInfo");
		Map<String, Object> map = inspComplaintInfoMap(request);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 投诉详情Map
	 * @return
	 */
	@RequestMapping("/inspComplaintInfoMap")
	@ResponseBody
	public Map<String, Object> inspComplaintInfoMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
//		String ywid = request.getParameter("ywid");//ywid未被使用，故注释，2023/10/26
		String ycid = request.getParameter("ycid");
		String wxid = request.getParameter("wxid");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("sign",commonService.getSign());
		SjycWechatDto sjycDto = new SjycWechatDto();
		sjycDto.setYcid(ycid);
		sjycDto.setGzpt(wbcxdm);
		sjycDto = sjycService.getDto(sjycDto);
		//根据异常ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjycDto.getYcid());
		if(fjcfbDtos != null){
			DBEncrypt dbEncrypt = new DBEncrypt();
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				String wjmhz = fjcfbDtos.get(i).getWjm().substring(fjcfbDtos.get(i).getWjm().lastIndexOf(".") + 1);
				fjcfbDtos.get(i).setWjmhz(wjmhz);
				fjcfbDtos.get(i).setWjlj(dbEncrypt.dCode(fjcfbDtos.get(i).getWjlj()));
			}
		}
		map.put("ywlx",BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		map.put("sjycDto", sjycDto);
		map.put("fjcfbDtos", fjcfbDtos);
		map.put("wbcxdm", wbcxdm);
		map.put("wxid", wxid);
		exceptionWechartUtil.viewExceptionMessage(wxid,ycid);
		return map;
	}
	/**
	 * 投诉评论详情Map
	 * @return
	 */
	@RequestMapping("/inspComplaintComment")
	@ResponseBody
	public Map<String, Object> inspComplaintComment(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String ycid = request.getParameter("ycid");
		String pageSize = request.getParameter("pageSize");
		String pageNumber = request.getParameter("pageNumber");
		//String wxid = request.getParameter("wxid");
		SjycfkWechatDto sjycfkDto = new SjycfkWechatDto();
		sjycfkDto.setYcid(ycid);
		sjycfkDto.setPageSize(Integer.parseInt(pageSize));
		sjycfkDto.setPageNumber(Integer.parseInt(pageNumber));
		sjycfkDto.setPageStart((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize));
		List<SjycfkWechatDto> SjycfkDtos = sjycfkService.getMiniDtoByYcid(sjycfkDto);
		map.put("rows", SjycfkDtos);
		return map;
	}

	/**
	 * 保存异常反馈信息
	 *
	 * @param sjycfkDto
	 * @return
	 */
	@RequestMapping("/saveComplaintComment")
	@ResponseBody
	public Map<String, Object> saveComplaintComment(SjycfkWechatDto sjycfkDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
//		String ycid = request.getParameter("ycid");//ycid未被使用，故注释，2023/10/26
		String wxid = request.getParameter("wxid");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}

		WxyhDto wxyhDto = new WxyhDto();
		wxyhDto.setWxid(wxid);
		wxyhDto.setWbcxdm(wbcxdm);
		List<WxyhDto> wxyhs = wxyhService.getWxyhListByWxid(wxyhDto);
		String wxm = "";
		if (wxyhs!=null && wxyhs.size()>0){
			wxm = StringUtil.isNotBlank(wxyhs.get(0).getWxm())?wxyhs.get(0).getWxm():wxid;
		}
		sjycfkDto.setLrry(wxid);
		sjycfkDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sjycfkDto.setLrsj(dateFm.format(new Date()));
		sjycfkDto.setLrrymc(wxm);
		List<JcsjDto> fkqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.FEEDBACK_DISTINGUISH.getCode());
		if (fkqfList!=null && fkqfList.size()>0) {
			for (JcsjDto jc_fkqf : fkqfList) {
				if ("WECHAT_FEEDBACK".equals(jc_fkqf.getCsdm())){
					sjycfkDto.setFkqf(jc_fkqf.getCsid());
					break;
				}
			}
		}
		sjycfkService.insertDto(sjycfkDto);
		SjycWechatDto sjycDto = new SjycWechatDto();
		sjycDto.setYcid(sjycfkDto.getYcid());
		sjycDto.setZhhfsj(sjycfkDto.getLrsj());
		sjycDto.setZhhfnr(wxm + ":" + sjycfkDto.getFkxx());
		boolean isSuccess = sjycService.modSaveException(sjycDto);
		Map<String,Object> rabbitMap=new HashMap<>();
		rabbitMap.put("type","fk");
		rabbitMap.put("sjycfkDto",sjycfkDto);
		SjycWechatDto _sjycDto=sjycService.getDto(sjycDto);
		rabbitMap.put("sjycDto",_sjycDto);
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SSE_SENDMSG_EXCEPRION_MATRIDX.getCode(), JSON.toJSONString(rabbitMap));
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 查看更多回复内容
	 * @param sjycfkDto
	 * @return
	 */
	@RequestMapping("/viewMoreDiscuss")
	@ResponseBody
	public Map<String,Object> viewMoreDiscuss(SjycfkWechatDto sjycfkDto){
		Map<String,Object> map= new HashMap<>();
		List<SjycfkWechatDto> list=sjycfkService.getDtosByGid(sjycfkDto);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		try {
			if(list!=null && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					Date date=format.parse(list.get(i).getHfsj());
					long l = new Date().getTime() - date.getTime();
					long t = 60*5*1000-l;
					if(t>=0) {
						list.get(i).setSfyxch("1");
					}else {
						list.get(i).setSfyxch("0");
					}
				}
			}
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.getMessage());
		}
		map.put("rows", list);
		return map;
	}


	/**
	 * 投诉评价
	 * @return
	 */
	@RequestMapping(value="/evaluation")
	public ModelAndView evaluation(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/informed/evaluation");
		Map<String, Object> map = evaluationMap(request);
		mav.addAllObjects(map);
		return mav;
	}

	/**
	 * 投诉评价Map
	 * @return
	 */
	@RequestMapping("/evaluationMap")
	@ResponseBody
	public Map<String, Object> evaluationMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String ywid = request.getParameter("ywid");
		String ywlx = request.getParameter("ywlx");
		String wxid = request.getParameter("wxid");
		map.put("ywid",ywid);
		map.put("ywlx",ywlx);
		map.put("wxid",wxid);
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		map.put("wbcxdm",wbcxdm);
		map.put("actionurl","/wechat/evaluationSave");
		return map;
	}


	/**
	 * 投诉评价保存
	 * @return
	 */
	@RequestMapping("/evaluationSave")
	@ResponseBody
	public Map<String, Object> evaluationSave(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		SjycWechatDto sjycDto = new SjycWechatDto();
		sjycDto.setYcid(request.getParameter("ywid"));
		sjycDto.setPjxj(request.getParameter("stars"));
		sjycDto.setPjnr(request.getParameter("remark"));
		sjycDto.setXgry(request.getParameter("wxid"));
		sjycDto.setSfjj(request.getParameter("sfjj"));
		boolean isSuccess = sjycService.evaluation(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取微信信息,并跳转wechatInfoGetBackUrl
	 * @param wechatInfoGetBackUrl
	 * @param wechatMPCode
	 * @param code
	 * @param state
	 * 86端扫码进入:
	 * 		wechatInfoGetBackUrl:/ws/qrCode/qrCodeAnalysis
	 * 		wechatMPCode:MDINSPECT
	 * 		id:DF39B3263C6C4240A2DD91A5434E94F6
	 * 微信回调进入:
	 * 		微信菜单:		https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={redirect_uri}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
	 * 		redirect_uri:	http%3A%2F%2F60.191.45.243%3A8182%2Fwechat%2FgetWechatId%3FwechatInfoGetBackUrl%3Dhttp%253A%252F%252F172.17.52.45%253A8086%252Fws%252FqrCode%252FqrCodeAnalysis%26wechatMPCode%3DHMZInspect
	 * 		wechatMPCode:MDINSPECT
	 * @return
	 */
	@RequestMapping(value = "/getWechatId")
	public ModelAndView getWechatId(String code, String state, String wechatInfoGetBackUrl, String wechatMPCode, String id, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("common/redirect/redirectPage");
		if (StringUtil.isBlank(wechatMPCode)) {
			wechatMPCode = ProgramCodeEnum.MDINSPECT.getCode();
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wechatMPCode);
		wbcxDto = wbcxService.getDto(wbcxDto);
		if (wbcxDto == null) {
			log.error("未获取到外部程序编码  " + wechatMPCode + " 对应的外部程序信息！");
			mav.addObject("message", "未获取到外部程序编码！\n" + wechatMPCode + " 对应的外部程序信息！");
			mav.addObject("status", "info");
			return mav;
		}
		DBEncrypt dbEncrypt = new DBEncrypt();
		String appid = dbEncrypt.dCode(wbcxDto.getAppid());
		if (StringUtil.isNotBlank(code) && StringUtil.isNotBlank(state)) {
			//微信授权回调
			try {
				WeChatUserModel userModel = WeChatUtils.getUserBaseInfoByLink(restTemplate, code, wbcxDto, redisUtil);
				String redirectUrl = wechatInfoGetBackUrl + "?wechatid=" + userModel.getOpenid();
				if (StringUtil.isNotBlank(id)) {
					redirectUrl = redirectUrl + "&id=" + id;
				} else {
					//查询微信是否被绑定
					MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
					paramMap.add("wechatid", userModel.getOpenid());
					// 发送文件到服务器
					Map<String,Object> res = restTemplate.postForObject(dbEncrypt.dCode(companyurl) + "/ws/qrCode/checkWechatIdBound", paramMap, Map.class);
					if (res != null && res.get("id") != null ){
						redirectUrl = redirectUrl + "&id=" + res.get("id");
					}else {
						mav.addObject("wechatMPCode", wechatMPCode);
						mav.addObject("wechatid", userModel.getOpenid());
						mav.addObject("message", "账号\n【" + userModel.getOpenid() + "】\n还未申请相应实验室\n请扫码申请！");
						mav.addObject("status", "waiting");
						return mav;
					}
				}
				mav.addObject("redirectUrl", redirectUrl);
				mav.addObject("status", "info");
				return mav;
			} catch (BusinessException e) {
				log.error("未获取到微信授权信息：" + wechatMPCode + "：" + e.getMessage());
				mav.addObject("message", "未获取到微信授权信息！" + wechatMPCode + "\n" + e.getMessage());
				mav.addObject("status", "info");
				return mav;
			}
		} else if (StringUtil.isNotBlank(wechatInfoGetBackUrl) && StringUtil.isNotBlank(wechatMPCode) && StringUtil.isNotBlank(id)) {
			//86端扫码进入，并跳转到本接口
			String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={redirect_uri}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
			//替换{appid}
			redirectUrl = redirectUrl.replace("{appid}", appid);
			//替换{redirect_uri}
			String wechatInfoGetUrl = dbEncrypt.dCode(menuurl) + "/wechat/getWechatId?wechatInfoGetBackUrl=" + URLEncoder.encode(wechatInfoGetBackUrl) + "&wechatMPCode=" + wechatMPCode + "&id=" + id;
			redirectUrl = redirectUrl.replace("{redirect_uri}", URLEncoder.encode(wechatInfoGetUrl));
			mav.addObject("redirectUrl", redirectUrl);
		}
		return mav;
	}
}



