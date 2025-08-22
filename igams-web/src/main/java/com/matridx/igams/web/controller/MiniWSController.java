package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByVoiceResponse;
import com.aliyuncs.exceptions.ClientException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dingtalk.oapi.lib.aes.Utils;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.RslyDto;
import com.matridx.igams.web.dao.entities.RszpDto;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyqxDto;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhssjgDto;
import com.matridx.igams.web.service.svcinterface.IRslyService;
import com.matridx.igams.web.service.svcinterface.IRszpService;
import com.matridx.igams.web.service.svcinterface.IWbcxService;
import com.matridx.igams.web.service.svcinterface.IWbzyqxService;
import com.matridx.igams.web.service.svcinterface.IXtjsService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.web.service.svcinterface.IYhjsService;
import com.matridx.igams.web.service.svcinterface.IYhssjgService;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.dingtalk.DingCallbackCrypto;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import com.taobao.api.ApiException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**		/ws/dingtalk/SingleCallByVoice	钉钉钉一下语音模板通知
 /ws/dingtalk/singleCallByTts	钉钉钉一下文本模板通知
 /dingtalk/singleCallByDdid		通过钉钉id钉一下
 /dingtalk/singleCallByYhm		通过钉钉用户名钉一下
 /dingtalk/singleCallByYhmTwo	通过钉钉用户名钉一下（文本模板固定）
 /showVideo                      视频播放
 /getVideoIOStream               返回视频流
 */
@Controller
@RequestMapping("/ws")
public class MiniWSController {
	@Autowired
	IRslyService rslyService;

	@Autowired
	IRszpService rszpService;


	@Autowired
	private DataSource postsqlDataSource;

	@Autowired
	IWbzyqxService wbzyqxService;

	@Autowired
	IXtyhService xtyhService;

	@Autowired
	IYhjsService yhjsService;


	@Autowired
	IWxyhService wxyhService;

	@Autowired
	IXtjsService xtjsService;

	@Autowired
	IDdspglService ddspglService;

	@Autowired
	DingTalkUtil talkUtil;


	@Autowired
	IDdfbsglService ddfbsglService;

//	@Autowired
//	IYhqtxxService yhqtxxService;

	@Autowired
	IYhssjgService yhssjgService;

	@Autowired
	IJgxxService jgxxService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	private ISjhbxxService sjhbxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;


	public String getPrefix(){
		return urlPrefix;
	}

	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IWbcxService wbcxService;
	private final Logger log = LoggerFactory.getLogger(MiniWSController.class);

	/**
	 * 小程序获取外部资源权限和收费权限(微信与钉钉公用)
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/getzyqx")
	@ResponseBody
	public Map<String, Object> getWbzyqx(XtyhDto xtyhDto, String zylb, String cdcc, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		YhjsDto yhjsDto = new YhjsDto();//用户角色信息
		List<YhjsDto> yhjslist = new ArrayList<>();
		if (StringUtil.isNotBlank(request.getParameter("wxid"))) {
			xtyhDto.setWechatid(request.getParameter("wxid"));
		}
		XtyhDto xtyhxx = xtyhService.getXtyhRedisAndDbByUserid(xtyhDto);
		if (xtyhxx != null) {
			xtyhxx.setLoginFlg("1");
			if (StringUtil.isNotBlank(xtyhxx.getYhid())) {
				//若当前角色不存在，则获取所用用户角色并取第一个
				if (StringUtil.isBlank(xtyhxx.getDqjs())) {
					yhjsDto.setYhid(xtyhxx.getYhid());
					List<YhjsDto> t_yhjslist = yhjsService.getDtoList(yhjsDto);
					if (!CollectionUtils.isEmpty(t_yhjslist)) {
						yhjsDto = t_yhjslist.get(0);
						yhjslist.add(yhjsDto);
						xtyhxx.setDqjs(yhjsDto.getJsid());
						xtyhxx.setDqjsdm(yhjsDto.getJsdm());
						xtyhxx.setDqjsmc(yhjsDto.getJsmc());
					}
				} else {
					yhjsDto.setJsid(xtyhxx.getDqjs());
					yhjsDto.setJsdm(xtyhxx.getDqjsdm());
					yhjsDto.setJsmc(xtyhxx.getDqjsmc());
				}
				map.put("dqjsDto", yhjsDto);
				map.put("jgid", xtyhxx.getJgid());
				// 返回用户信息(钉钉用)
				map.put("yhid", xtyhxx.getYhid());
				map.put("zsxm", xtyhxx.getZsxm());
				// 默认系统用户具有收费显示权限  lishangyuan 2020-9-29
				map.put("sfqx", "1");
			}
		}
		Object o_ddmenu = null;
		if (xtyhxx != null && StringUtil.isNotBlank(xtyhxx.getDqjs())){
			if (StringUtil.isNotBlank(xtyhDto.getWechatid())) {
				o_ddmenu = redisUtil.hget("Users_WeChatMenu", xtyhxx.getDqjs());
			} else if (StringUtil.isNotBlank(xtyhDto.getDdid())) {
				o_ddmenu = redisUtil.hget("Users_DingdingMenu", xtyhxx.getDqjs());
			}
		}else {
			if (StringUtil.isNotBlank(xtyhDto.getWechatid())) {
				o_ddmenu = redisUtil.hget("Users_WeChatMenu", "default");
			} else if (StringUtil.isNotBlank(xtyhDto.getDdid())) {
				o_ddmenu = redisUtil.hget("Users_DingdingMenu", "default");
			}
		}

		if (o_ddmenu != null) {
			//用户角色只是用了一个角色
			List<WbzyqxDto> wbzyqxlist = JSON.parseArray((String) o_ddmenu, WbzyqxDto.class);
			map.put("wbzyqxlist", wbzyqxlist);
		} else {
			WbzyDto wbzyDto = new WbzyDto();
			wbzyDto.setZylb(zylb);
			if (StringUtils.isBlank(cdcc)) {
                cdcc = "3";
            }
			wbzyDto.setCdcc(cdcc);
			if (StringUtil.isNotBlank(yhjsDto.getJsid())){
				yhjslist.add(yhjsDto);
				wbzyDto.setYhjslist(yhjslist);
			}
			List<WbzyqxDto> wbzyqxlist = wbzyqxService.getWbzyqxList(wbzyDto);
			//用户角色只是用了一个角色
			if (StringUtil.isNotBlank(xtyhDto.getWechatid())) {
				redisUtil.hset("Users_WeChatMenu", StringUtil.isNotBlank(yhjsDto.getJsid()) ?yhjsDto.getJsid():"default", JSON.toJSONString(wbzyqxlist), -1);
			} else if (StringUtil.isNotBlank(xtyhDto.getDdid())) {
				redisUtil.hset("Users_DingdingMenu", StringUtil.isNotBlank(yhjsDto.getJsid()) ?yhjsDto.getJsid():"default", JSON.toJSONString(wbzyqxlist), -1);
			}

			map.put("wbzyqxlist", wbzyqxlist);
		}
		String miniappid = request.getParameter("miniappid");
		if (StringUtil.isNotBlank(miniappid)) {
			Map<String, Object> dingInfoByMiniappid = talkUtil.getDingInfoByMiniappid(miniappid);
			if (dingInfoByMiniappid != null && dingInfoByMiniappid.get("wbcxdm") != null){
				map.put("wbcxdm",dingInfoByMiniappid.get("wbcxdm"));
			}
		}
		return map;
	}

	/**
	 * 小程序获取第二层菜单的外部资源权限(微信与钉钉公用)
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getSeczyqx")
	@ResponseBody
	public Map<String,Object> getSeczyqx(XtyhDto xtyhDto, String zylb,String zyid, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		if(StringUtil.isNotBlank(request.getParameter("wxid"))) {
            xtyhDto.setWechatid(request.getParameter("wxid"));
        }
		XtyhDto xtyhxx=xtyhService.getXtyhByUserid(xtyhDto);
		List<YhjsDto> yhjslist = new ArrayList<>();
		if(xtyhxx != null && StringUtil.isNotBlank(xtyhxx.getYhid())){
			YhjsDto yhjsDto=new YhjsDto();
			yhjsDto.setYhid(xtyhxx.getYhid());
			yhjslist = yhjsService.getDtoList(yhjsDto);
		}
		WbzyDto wbzyDto=new WbzyDto();
		wbzyDto.setZylb(zylb);
		wbzyDto.setZyid(zyid);
		wbzyDto.setYhjslist(yhjslist);
		List<WbzyqxDto> wbzyqxlist=wbzyqxService.getSecWbzyqxList(wbzyDto);
		map.put("wbzyqxlist", wbzyqxlist);
		return map;
	}

	/**
	 * 小程序根据wxid或ddid获取录入人员list
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getLrryList")
	@ResponseBody
	public List<String> getLrryList(HttpServletRequest request){
		String ddid = request.getParameter("ddid");
		String wxid = request.getParameter("wxid");
		List<String> lrrylist= new ArrayList<>();
		if(StringUtil.isNotBlank(ddid)) {
			//根据ddid获取系统用户id，再根据系统用户id获取wxid
			XtyhDto xtyhDto=new XtyhDto();
			xtyhDto.setDdid(ddid);
			XtyhDto xtyhxx=xtyhService.getXtyhByUserid(xtyhDto);
			if (xtyhxx!=null){
				lrrylist.add(xtyhxx.getYhid());
				WxyhDto wxyhDto=new WxyhDto();
				wxyhDto.setXtyhid(xtyhxx.getYhid());
				List<WxyhDto> wxyhlist=wxyhService.getListByXtyhid(wxyhDto);
				if(!CollectionUtils.isEmpty(wxyhlist)) {
					lrrylist.addAll(wxyhlist.stream().map(WxyhDto::getWxid).collect(Collectors.toList()));
				}
			}
			if (ddid.contains("@")) {
				ddid = ddid.split("@")[0];
				lrrylist.add(ddid);
			}
			lrrylist.add(xtyhDto.getDdid());
		}
		if(StringUtil.isNotBlank(wxid)) {
			WxyhDto wxyhDto=new WxyhDto();
			wxyhDto.setWxid(wxid);
			wxyhDto.setWechatid(wxid);
			//根据wxid获取相同系统用户id或相同unionid的微信用户信息
			List<WxyhDto> wxyhlist=wxyhService.getListBySameId(wxyhDto);
			if(!CollectionUtils.isEmpty(wxyhlist)) {
                for (WxyhDto dto : wxyhlist) {
                    lrrylist.add(dto.getWxid());
                    if (StringUtil.isNotBlank(dto.getXtyhid())) {
                        //根据用户id获取ddid
                        lrrylist.add(dto.getXtyhid());
                        XtyhDto xtyh = xtyhService.getDtoById(dto.getXtyhid());
                        if (StringUtil.isNotBlank(xtyh.getDdid())) {
                            lrrylist.add(xtyh.getDdid());
                        }
                    }
                }
			}else {
				lrrylist.add(wxid);
			}
		}
		return lrrylist;
	}

	/**
	 * 根据获取使用人员信息及收费权限(微信与钉钉公用，权限信息用于微信公众号)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getUser")
	@ResponseBody
	public Map<String,String> getUser(HttpServletRequest request){
		Map<String,String> map= new HashMap<>();
		XtyhDto xtyhDto = new XtyhDto();
		if(StringUtil.isNotBlank(request.getParameter("wxid"))) {
            xtyhDto.setWechatid(request.getParameter("wxid"));
        }
		if(StringUtil.isNotBlank(request.getParameter("ddid"))) {
            xtyhDto.setDdid(request.getParameter("ddid"));
        }
		XtyhDto t_xtyhDto = xtyhService.getXtyhByUserid(xtyhDto);
		if(t_xtyhDto!=null && StringUtils.isNotBlank(t_xtyhDto.getYhid())) {
			/*YhqtxxDto yhqtxxDto = yhqtxxService.getDtoById(t_xtyhDto.getYhid());
			if(yhqtxxDto != null)
				map.put("sfqx", yhqtxxDto.getSfqx());*/
			// 默认系统用户具有收费显示权限  lishangyuan 2020-9-29
			// map.put("sfqx", "1");
			map.put("yhid", t_xtyhDto.getYhid());
			map.put("zsxm", t_xtyhDto.getZsxm());
		}
		// 默认都具有收费权限  lishangyuan 2021-3-24
		map.put("sfqx", "1");
		return map;
	}





	/**
	 * 钉钉业务事件回调
	 * @param signature 消息体签名
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
										@RequestParam(value = "timestamp", required = false) String timestamp,
										@RequestParam(value = "nonce", required = false) String nonce,
										@RequestBody(required = false) JSONObject json, HttpServletRequest request) {
		try {
			DBEncrypt crypt = new DBEncrypt();
			String wbcxidString = request.getParameter("wbcxid");
			log.error("外部程序ID为："+wbcxidString);
			//根据wbcxid获取AES_KEY，CROPID，miniappid
			if (StringUtil.isBlank(wbcxidString)){
				wbcxidString = "matridxOA";
			}
			Object wbcxInfo = redisUtil.hget("WbcxInfo", wbcxidString);
			WbcxDto wbcxDto;
			if (wbcxInfo!=null){
				wbcxDto = JSON.parseObject(String.valueOf(wbcxInfo), WbcxDto.class);
			}else {
				wbcxDto = wbcxService.getDtoById(wbcxidString);
			}
			Object accessToken = redisUtil.hget("accessToken", wbcxDto.getWbcxid());
			String token;
			if (accessToken!=null){
				token  = String.valueOf(accessToken);
			}else {
				token = talkUtil.getToken(wbcxidString);
			}
			log.error("=====nonce====="+nonce + " TOKEN:" + token + " AES_KEY:" + wbcxDto.getAeskey() + " CROPID:" + wbcxDto.getCropid() + " wbcxid:" + wbcxidString);
			DingCallbackCrypto dingTalkEncryptor = new DingCallbackCrypto(crypt.dCode(wbcxDto.getToken()), crypt.dCode(wbcxDto.getAeskey()), crypt.dCode(wbcxDto.getCropid()));
			//从post请求的body中获取回调信息的加密数据进行解密处理
			String encryptMsg = json.getString("encrypt");
			String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
//			String token = talkUtil.getToken();
			Map<String,Object>map=new HashMap<>();
			map.put("signature",signature);
			map.put("timestamp",timestamp);
			map.put("nonce",nonce);
			map.put("wbcxid",wbcxidString);
			map.put("json",json);
			map.put("ddhd","1");
			String hdcs = request.getParameter("hdcs");
			if(StringUtil.isBlank(hdcs)){
				map.put("hdcs","1");
			}else{
				int ihdcs=Integer.valueOf(hdcs)+1;
				map.put("hdcs",ihdcs+"");
			}
			ddfbsglService.dealCallback(json,plainText,null,wbcxidString,map);
			// 返回success的加密信息表示回调处理成功
			return dingTalkEncryptor.getEncryptedMap(DingTalkUtil.CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(16));
		} catch (Exception e) {
			// 失败的情况，应用的开发者应该通过告警感知，并干预修复
			String params = " signature:"+signature + " timestamp:"+timestamp +" nonce:"+nonce+" json:"+json;
			log.error("process callback failed！"+params);
			log.error(e.toString());


			return null;
		}
	}


	/**
	 * 钉钉审批回调若成功则更新主站钉钉审批管理和钉钉分布式管理表信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateDdspAndDdfbs")
	@ResponseBody
	public boolean updateDdspAndDdfbs(HttpServletRequest request) {
		String processInstanceId=request.getParameter("processInstanceId");
		DdspglDto ddspglDto=new DdspglDto();
		ddspglDto.setCljg("1");
		ddspglDto.setType("finish");
		ddspglDto.setProcessinstanceid(processInstanceId);
		boolean result =ddspglService.updateAll(ddspglDto);
		if(!result) {
            return false;
        }
		DdfbsglDto ddfbsgldto=new DdfbsglDto();
		ddfbsgldto.setCljg("1");
		ddfbsgldto.setProcessinstanceid(processInstanceId);
        return ddfbsglService.update(ddfbsgldto);
    }

	/**
	 * 跟据员工号查询用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/user/getUserByUserName")
	@ResponseBody
	public Map<String,Object> getUserByUserName(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		List<String> jsmcList= new ArrayList<>();
		YhssjgDto yhssjgDto=new YhssjgDto();
		XtyhDto xtyhDto=new XtyhDto();
		String yhm= request.getParameter("yhm");
		if(StringUtil.isNotBlank(yhm)) {
			xtyhDto=xtyhService.getDtoByName(yhm);
			if(xtyhDto!=null) {
				yhssjgDto=yhssjgService.getDtoById(xtyhDto.getYhid());
				if(yhssjgDto==null) {
					yhssjgDto=new YhssjgDto();
				}
				List<YhjsDto> yhjsList=yhjsService.getDtoListById(xtyhDto.getYhid());
				if(!CollectionUtils.isEmpty(yhjsList)) {
					jsmcList.addAll(yhjsList.stream().map(YhjsDto::getJsmc).collect(Collectors.toList()));
				}
			}else {
				xtyhDto=new XtyhDto();
			}
		}
		map.put("真实姓名",xtyhDto.getZsxm());
		map.put("用户角色",jsmcList);
		map.put("用户岗位",xtyhDto.getGwmc());
		map.put("用户部门",yhssjgDto.getJgmc());
		return map;
	}

	/**
	 * * 钉钉钉一下（语音通知）
	 * @param calledNumber     手机号
	 * @param voiceCode        语音ID
	 * @param CalledShowNumber 被叫显号
	 * @return
	 * 	OK	请求成功
	isp.RAM_PERMISSION_DENY	RAM权限DENY
	isv.OUT_OF_SERVICE	业务停机
	isv.PRODUCT_UN_SUBSCRIPT	未开通云通信产品的阿里云客户
	isv.PRODUCT_UNSUBSCRIBE	产品未开通
	isv.ACCOUNT_NOT_EXISTS	账户不存在
	isv.ACCOUNT_ABNORMAL	账户异常
	isv.VOICE_FILE_ILLEGAL	语音文件不合法
	isv.BILLID_NOT_EXIST	号显不合法
	isv.INVALID_PARAMETERS	参数异常
	isp.SYSTEM_ERROR	系统错误
	isv.MOBILE_NUMBER_ILLEGAL	号码格式非法
	isv.BUSINESS_LIMIT_CONTROL	触发流控
	 */

	@RequestMapping(value = "/dingtalk/SingleCallByVoice")
	@ResponseBody
	public Map<String, Object> SingleCallByVoice(String calledNumber, String voiceCode, String CalledShowNumber,String outId) {
		Map<String, Object> map = new HashMap<>();
		try {
			SingleCallByVoiceResponse response = talkUtil.SingleCallByVoice(calledNumber, voiceCode, CalledShowNumber,outId);
			map.put("status", "OK".equals(response.getCode()) ? "success" : "fail");
			map.put("message", "OK".equals(response.getCode()) ? xxglService.getModelById("ICOM99024").getXxnr()
					: xxglService.getModelById("ICOM99025").getXxnr());
		} catch (ClientException e) {
			log.error(e.toString());
		}
		return map;
	}


	/**
	 * * 钉钉钉一下（文本转语音通知）
	 * @param calledNumber     手机号
	 * @param ttsCode          语音模板ID
	 * @param ttsParam 		         模板变量{"AckNum":"123456"}
	 * @return
	 *
	 */
	@RequestMapping(value = "/dingtalk/singleCallByTts")
	@ResponseBody
	public Map<String, Object> singleCallByTts(String calledNumber,String ttsCode,String ttsParam) {
		Map<String, Object> map = new HashMap<>();
		try {
			SingleCallByTtsResponse response = talkUtil.singleCallByTts(calledNumber, ttsCode, ttsParam);
			map.put("status", "OK".equals(response.getCode()) ? "success" : "fail");
			map.put("message", "OK".equals(response.getCode()) ? xxglService.getModelById("ICOM99024").getXxnr()
					: xxglService.getModelById("ICOM99025").getXxnr());
		} catch (ClientException e) {
			log.error(e.toString());
			log.error("语音通知发送失败");
			map.put("status", "fail");
			map.put("message", "语音通知发送失败");
		}
		return map;
	}


	/**
	 * @Description:钉钉钉一下（通过钉钉ID）
	 * @param request ddids    钉钉ID集合
	 *                ttsCode  语音模板ID
	 *                ttsParam 模板变量{"AckNum":"123456"}
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dingtalk/singleCallByDdid")
	@ResponseBody
	public Map<String, Object> singleCallByDdid(HttpServletRequest request) {
		String ddids = request.getParameter("ddids");
		List<String> ddidsList = JSONObject.parseObject(ddids, ArrayList.class);
		String ttsCode = request.getParameter("ttsCode");
		String ttsParam = request.getParameter("ttsParam");
		return talkUtil.singleCallByDdid(ddidsList,ttsCode,ttsParam);
	}

	/**
	 * @Description:钉钉钉一下（通过用户名）
	 * @param request	yhms     用户名集合
	 * 					ttsCode  语音模板ID
	 * 					ttsParam 模板变量{"AckNum":"123456"}
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dingtalk/singleCallByYhm")
	@ResponseBody
	public Map<String, Object> singleCallByYhm(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String yhms = request.getParameter("yhms");
		List<String> yhmList = JSONObject.parseObject(yhms, ArrayList.class);
		String ttsCode = request.getParameter("ttsCode");
		String ttsParam = request.getParameter("ttsParam");
		/*
		 * List<String> list = new ArrayList<String>(); for (String yhm : yhmList) {
		 * XtyhDto xtyhDto = xtyhService.getDtoByName(yhm); if(xtyhDto==null) {
		 * log.error("用户名："+yhm+"不存在"); }else { list.add(xtyhDto.getDdid()); } }
		 */
		if(!CollectionUtils.isEmpty(yhmList)) {
			map = talkUtil.singleCallByYhm(yhmList,ttsCode,ttsParam);
		}else {
			map.put("status","fail");
			map.put("message","用户名不能为空！");
		}
		return map;
	}

	/**
	 * @Description:钉钉钉一下（通过用户名）
	 * @param request	yhms     用户名集合
	 * 					ttsParam 模板变量{"AckNum":"123456"}
	 * @return
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dingtalk/singleCallByYhmTwo")
	@ResponseBody
	public Map<String, Object> singleCallByYhmTwo(HttpServletRequest request) {
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("TEXT_TEMPID");
		jcsjDto.setCsmc("文本通知1");
		jcsjDto = jcsjService.getDtoByCsmcAndJclb(jcsjDto);
		String yhms = request.getParameter("yhms");
		List<String> yhmList = JSONObject.parseObject(yhms, ArrayList.class);
		String ttsParam = request.getParameter("ttsParam");
		/*
		 * List<String> list = new ArrayList<String>(); for (String yhm : yhmList) {
		 * XtyhDto xtyhDto = xtyhService.getDtoByName(yhm); list.add(xtyhDto.getDdid());
		 * }
		 */
		return talkUtil.singleCallByYhm(yhmList,jcsjDto.getCsdm(),ttsParam);
	}

	/**
	 * 视频播放
	 * @return
	 */
	@RequestMapping("/videoPlayer")
	public ModelAndView videoPlayer() {
        return new ModelAndView("globalweb/centos/videoPlayer");
	}

	/**
	 * 根据本地视频全路径，响应给浏览器1个视频
	 */
	@RequestMapping("/showVideo")
	@ResponseBody
	public void showVideo(HttpServletResponse response) {
		String fileName="/matridx/igamslogs/matridx.mp4";
		response.setContentType("video/*"); //设置返回的文件类型
		FileInputStream fis =null;
		OutputStream toClient=null; //得到向客户端输出二进制数据的对象
		BufferedInputStream bis;
		try{
			fis=new FileInputStream(fileName);
			toClient=response.getOutputStream();
			byte[] data =new byte[1024];
			bis=new BufferedInputStream(fis);
			int i = bis.read(data);//读数据
			while (i!=-1) {
				toClient.write(data);  //输出数据
				toClient.flush();
				i = bis.read(data);
			}
		}catch(Exception e){
			log.error(e.toString());
		}finally {
			try {
				if (toClient != null) {
					toClient.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
		}
	}

	/**
	 * 返回视频流
	 * @param response
	 */
	@RequestMapping("/getVideoIOStream")
	@ResponseBody
	public void getVideoIOStream(HttpServletResponse response) {
		String fileName="/matridx/igamslogs/matridx.mp4";
		response.setContentType("video/*"); //设置返回的文件类型
		FileInputStream fis =null;
		OutputStream toClient=null; //得到向客户端输出二进制数据的对象
		BufferedInputStream bis;
		try{
			fis=new FileInputStream(fileName);
			toClient=response.getOutputStream();
			byte[] data =new byte[1024];
			bis=new BufferedInputStream(fis);
			int i = bis.read(data);//读数据
			while (i!=-1) {
				toClient.write(data);  //输出数据
				toClient.flush();
				i = bis.read(data);
			}
		}catch(Exception e){
			log.error(e.toString());
		}finally {
			try {
				if (toClient != null) {
					toClient.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
		}
	}

	/**
	 * 验证token是否有效
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkWsToken")
	@ResponseBody
	public Map<String,String> checkWsToken(HttpServletRequest request){
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
				Encrypt ecp = new Encrypt();
				//先把页面上传递过来的tokenid进行转换
				String token_id = ecp.extractTokenKey(s_access_token);
				//再根据转换后的token查询用户信息
				XtyhDto xtyhDto=xtyhService.getDtoByToken(token_id);
				List<Map<String,String>> jslist=new ArrayList<>();
				if(xtyhDto!=null){
					YhssjgDto yhssjgDto=yhssjgService.getDtoById(xtyhDto.getYhid());
					if(yhssjgDto!=null){
						resultMap.put("用户机构",yhssjgDto.getJgmc());
						resultMap.put("真实姓名",xtyhDto.getZsxm());
						resultMap.put("用户名",xtyhDto.getYhm());
						resultMap.put("用户岗位",xtyhDto.getGwmc());
					}
					YhjsDto yhjsDto=new YhjsDto();
					yhjsDto.setYhid(xtyhDto.getYhid());
					List<YhjsDto> yhjsDtos=yhjsService.getDtoList(yhjsDto);
					if(!CollectionUtils.isEmpty(yhjsDtos)){
						for (YhjsDto yhjsDto1:yhjsDtos) {
							Map<String,String> map=new HashMap<>();
							map.put("角色ID",yhjsDto1.getJsid());
							map.put("角色名称",yhjsDto1.getJsmc());
							jslist.add(map);
						}
						resultMap.put("用户角色",JSONObject.toJSONString(jslist));
					}
				}else{
					//查询钉钉用户
					XtyhDto ddyh=xtyhService.getDdByToken(token_id);
					if(ddyh!=null){
						YhssjgDto yhssjgDto=yhssjgService.getDtoById(ddyh.getYhid());
						if(yhssjgDto!=null) {
							resultMap.put("用户机构",yhssjgDto.getJgmc());
							resultMap.put("真实姓名",ddyh.getZsxm());
							resultMap.put("用户名",ddyh.getYhm());
							resultMap.put("用户岗位",ddyh.getGwmc());
						}
						YhjsDto yhjsDto=new YhjsDto();
						yhjsDto.setYhid(ddyh.getYhid());
						List<YhjsDto> yhjsDtos=yhjsService.getDtoList(yhjsDto);
						if(!CollectionUtils.isEmpty(yhjsDtos)){
							for (YhjsDto yhjsDto1:yhjsDtos) {
								Map<String,String> map=new HashMap<>();
								map.put("角色ID",yhjsDto1.getJsid());
								map.put("角色名称",yhjsDto1.getJsmc());
								jslist.add(map);
							}
							resultMap.put("用户角色",JSONObject.toJSONString(jslist));
						}
					}else{
						resultMap.put("status", "false");
						resultMap.put("errorCode", "4");
						resultMap.put("errorMessage", "未获取到用户信息！");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		return resultMap;
	}

	/**
	 * 小程序获取用md5加密钉钉ID和真实姓名第一位后的结果
	 * @param request
	 * @return
	 */
	@RequestMapping("/web/oauth/Md5Ecode")
	@ResponseBody
	public Map<String,Object> getMd5EcodeMessage(HttpServletRequest request){
		String userid=request.getParameter("userid");
		String username=request.getParameter("username");
		String t_username="";
		if(StringUtils.isNotBlank(username)) {
            t_username=username.substring(0, 1);
        }
		String ecode_result=Encrypt.stringToMD5(userid+t_username);
		Map<String,Object> map= new HashMap<>();
		map.put("result",ecode_result.toUpperCase());
		return map;
	}

	/**
	 * 传递参数，获取钉钉用户详情
	 * @return
	 * @throws ApiException
	 */
	@RequestMapping(value="/web/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUser(String code,String miniappid) throws ApiException {
		Map<String, Object> map = getUserID(code,miniappid);
		String token = (String) map.get("token");
		String userid = (String) map.get("userid");
//		XtyhDto xtyhDto = new XtyhDto();
//		xtyhDto.setDdid(userid);
//		XtyhDto xtyhByUserid = xtyhService.getXtyhByUserid(xtyhDto);
		User t_user = redisUtil.hugetDto("Users", userid);
		Map<String, Object> t_map = new HashMap<>();
		if(t_user!=null && StringUtil.isNotBlank(t_user.getDdid()) && StringUtil.isNotBlank(t_user.getZsxm()) && StringUtil.isNotBlank(t_user.getDdtxlj()) && StringUtil.isNotBlank(t_user.getGwmc())) {
			log.error("返回页面数据userid:" + t_user.getDdid() + "-----name:" + t_user.getZsxm() + "-----ddtxlj:" + t_user.getDdtxlj() + "----dqgw:" + t_user.getGwmc());
			t_map.put("userid", t_user.getDdid());
			t_map.put("name", t_user.getZsxm());
			t_map.put("ddtxlj", t_user.getDdtxlj());
			t_map.put("dqgw", t_user.getGwmc());
		}else {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
			OapiUserGetRequest request = new OapiUserGetRequest();
			request.setUserid(userid);
			request.setHttpMethod("GET");
			OapiUserGetResponse response = client.execute(request, token);
			log.error("返回页面数据userid:"+response.getUserid()+"-----name:"+response.getName()+"-----ddtxlj:"+response.getAvatar()+"----dqgw:"+response.getPosition()+"----yhm:"+response.getJobnumber());
			t_map.put("userid", response.getUserid());
			t_map.put("name",response.getName());
			t_map.put("ddtxlj", response.getAvatar());
			t_map.put("dqgw", response.getPosition());
			t_map.put("yhm", response.getJobnumber());
		}
		Map<String, Object> dingInfoByMiniappid = talkUtil.getDingInfoByMiniappid(miniappid);
		if (dingInfoByMiniappid!=null) {
			t_map.put("wbcxdm", dingInfoByMiniappid.get("wbcxdm"));
		}
		return t_map;
	}

	/**
	 * 获取用户Userid
	 *
	 * @throws ApiException
	 */
	public Map<String, Object> getUserID(String AuthCode,String miniappid) throws ApiException {
		String token = talkUtil.getDingTokenByMiniappid(miniappid);
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
		OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
		request.setCode(AuthCode);
		request.setHttpMethod("GET");
		OapiUserGetuserinfoResponse t_response = client.execute(request, token);
		String userId = t_response.getUserid();
		log.error("获取userid" + userId);
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		map.put("userid", userId);
		return map;
	}





	/**
	 * 传递参数,更新加班信息
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/web/updateWorkOvertime")
	@ResponseBody
	public Map<String,Object> updateWorkOvertime(){
		Map<String,Object> map= new HashMap<>();
		/*String processInstanceId = request.getParameter("processInstanceId");
		String processCode = request.getParameter("processCode");
		String wbcxid = request.getParameter("wbcxid");
		log.error("updateWorkOvertime processInstanceId: " + processInstanceId);
		log.error("updateWorkOvertime processCode: " + processCode);
		log.error("updateWorkOvertime wbcxid: " + wbcxid);
		String token = talkUtil.getToken(wbcxid);
		SimpleDateFormat formatRq = new SimpleDateFormat("yyyy-MM-dd");
		List<YhkqxxDto> yhkqxxDtos = new ArrayList<>();
		GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
		if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus())&&"agree".equals(approveInfo.getResult())) {
			YhkqxxDto yhkqxxDto = new YhkqxxDto();
			yhkqxxDto.setKqid(StringUtil.generateUUID());
			yhkqxxDto.setWbcxid(wbcxid);
			yhkqxxDto.setRq(formatRq.format(DateUtils.parseDate("yyyy-MM-dd",approveInfo.getCreateTime())));
			yhkqxxDto.setDdid(approveInfo.getOriginatorUserId());
			XtyhDto xtyhDto = new XtyhDto();
			xtyhDto.setDdid(approveInfo.getOriginatorUserId());
			XtyhDto xtyhDto1 = xtyhService.getYhid(xtyhDto);
			if (xtyhDto1 != null) {
				yhkqxxDto.setYhid(xtyhDto1.getYhid());
			} else {
				log.error("updateWorkOvertime--获取yhid,ddid=" + xtyhDto.getDdid());
			}
			//获取表单的值
			List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> formComponentValues = approveInfo.getFormComponentValues();
			for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : formComponentValues) {
				if (StringUtil.isNotBlank(formComponentValue.getName()))
					switch (formComponentValue.getName()){
						case "开始时间": yhkqxxDto.setJbkssj(formComponentValue.getValue());break;
						case "结束时间": yhkqxxDto.setJbjssj(formComponentValue.getValue());break;
						case "班次": yhkqxxDto.setBcmc(StringUtil.isNotBlank(formComponentValue.getValue())?formComponentValue.getValue().split("-")[0]:null);break;
					}
			}
			yhkqxxDtos.add(yhkqxxDto);
		}
		boolean isSuccess=yhkqxxService.insertOrUpdateYhWorkOvertime(yhkqxxDtos);*/
		map.put("status", "success");
		map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
		return map;
	}
	/**
	 * 传递参数,更新招聘信息
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/web/updateRecruitment")
	@ResponseBody
	public Map<String,Object> updateRecruitment(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		String data=request.getParameter("data");
		JSONObject jsonObject = JSON.parseObject(data);
		JSONObject obj = jsonObject.getJSONObject("process_instance");
		RszpDto rszpDto=new RszpDto();
		rszpDto.setWbcxid(request.getParameter("wbcxid"));
		String processInstanceId = request.getParameter("processInstanceId");
		rszpDto.setSpid(processInstanceId);
		XtyhDto xtyhDto=new XtyhDto();
		xtyhDto.setDdid(obj.getString("originator_userid"));
		XtyhDto xtyhDto1 = xtyhService.getYhid(xtyhDto);
		rszpDto.setFqr(xtyhDto1.getYhid());
		rszpDto.setFqsj(obj.getString("create_time"));
		JgxxDto jgxxDto=new JgxxDto();
		String bmmc = obj.getString("originator_dept_name");
		rszpDto.setFqrbmmc(bmmc);
		String[] split = bmmc.split("-");
		jgxxDto.setJgmc(split[split.length-1]);
		JgxxDto jgxx = jgxxService.getJgxxByJgmc(jgxxDto);
		if(jgxx!=null){
			rszpDto.setFqrbm(jgxx.getJgid());
		}
		JSONArray jsonArray = obj.getJSONArray("form_component_values");
		rszpDto.setXqgw(jsonArray.getJSONObject(0).getString("value"));
		rszpDto.setGwbased(jsonArray.getJSONObject(1).getString("value"));
		rszpDto.setFzqyjyy(jsonArray.getJSONObject(2).getString("value"));
		String xqrs=jsonArray.getJSONObject(3).getString("value");
		if(xqrs.contains("人")){
			String str = xqrs.replaceAll("人", "");
			rszpDto.setXqrs(str);
		}else{
			rszpDto.setXqrs(xqrs);
		}
		rszpDto.setYjnx(jsonArray.getJSONObject(4).getString("value"));
		rszpDto.setYjyx(jsonArray.getJSONObject(5).getString("value"));
		String rs = jsonArray.getJSONObject(6).getString("value");
		if("null".equals(rs)){
			rszpDto.setGwxyrs("0");
		}else{
			rszpDto.setGwxyrs(rs);
		}
		rszpDto.setGwyqzz(jsonArray.getJSONObject(7).getString("value"));
		if(!"null".equals(jsonArray.getJSONObject(8).getString("value"))){
			rszpDto.setXwdgrq(jsonArray.getJSONObject(8).getString("value"));
		}
		RszpDto rszpDto_t = rszpService.getSpid(rszpDto);
		if(rszpDto_t!=null){
			rszpDto.setZpid(rszpDto_t.getZpid());
			boolean isSuccess=rszpService.update(rszpDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}else{
			rszpDto.setZpid(StringUtil.generateUUID());
			boolean isSuccess=rszpService.insert(rszpDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 传递参数,更新录用信息
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/web/updateEmployment")
	@ResponseBody
	public Map<String,Object> updateEmployment(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String data = request.getParameter("data");
		JSONObject jsonObject = JSON.parseObject(data);
		JSONObject obj = jsonObject.getJSONObject("process_instance");
		RslyDto rslyDto = new RslyDto();
		rslyDto.setWbcxid(request.getParameter("wbcxid"));
		String processInstanceId = request.getParameter("processInstanceId");
		rslyDto.setSpid(processInstanceId);
		JSONArray jsonArray = obj.getJSONArray("form_component_values");
		String value = jsonArray.getJSONObject(0).getString("value");
		String spxx = jsonArray.getJSONObject(3).getString("ext_value");
		if (spxx != null) {
			JSONObject jsonObject_t = JSON.parseObject(spxx);
			String spid = jsonObject_t.getJSONArray("list").getJSONObject(0).getString("procInstId");
			RszpDto rszpDto = new RszpDto();
			rszpDto.setSpid(spid);
			RszpDto rszpdto = rszpService.getSpid(rszpDto);
			if (rszpdto != null) {
				rslyDto.setZpid(rszpdto.getZpid());
			}
		}
		rslyDto.setBz(jsonArray.getJSONObject(1).getString("value"));
		JSONArray jsonArray_t = JSON.parseArray(value);
		JgxxDto jgxxDto = new JgxxDto();
		String bmmc = jsonArray_t.getJSONObject(1).getString("value");
		String[] split = bmmc.split("-");
		jgxxDto.setJgmc(split[split.length - 1]);
		JgxxDto jgxx = jgxxService.getJgxxByJgmc(jgxxDto);
		if (jgxx != null) {
			rslyDto.setYrbm(jgxx.getJgid());
		}
		rslyDto.setRzygxm(jsonArray_t.getJSONObject(0).getString("value"));
		rslyDto.setZw(jsonArray_t.getJSONObject(2).getString("value"));
		rslyDto.setSj(jsonArray_t.getJSONObject(3).getString("value"));
		String lx = jsonArray_t.getJSONObject(4).getString("value");
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsmc(lx);
		jcsjDto.setJclb(BasicDataTypeEnum.STAFF_TYPE.getCode());
		JcsjDto sjdto = jcsjService.getDto(jcsjDto);
		if (sjdto != null) {
			rslyDto.setYglx(sjdto.getCsid());
		}
		rslyDto.setRzrq(jsonArray_t.getJSONObject(5).getString("value"));
		RslyDto rslyDto_t = rslyService.getSpid(rslyDto);
		if (rslyDto_t != null) {
			rslyDto.setLyid(rslyDto_t.getLyid());
			boolean isSuccess = rslyService.update(rslyDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} else {
			rslyDto.setLyid(StringUtil.generateUUID());
			boolean isSuccess = rslyService.insert(rslyDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 获取角色列表(小程序)
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping("/mini/MiniXtjsList")
	@ResponseBody
	public Map<String,Object> MiniXtjsList(XtjsDto xtjsDto){
		Map<String,Object> map= new HashMap<>();
		List<XtjsDto> jslist=xtjsService.getMiniXtjsList(xtjsDto);
		map.put("list", jslist);
		return map;
	}
	/**
	 * 钉钉离职审核回调
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("/web/aduitDimissionCallback")
	@ResponseBody
	public Map<String,Object> aduitPostPromotionCallback(HttpServletRequest request) {
		String processInstanceId = request.getParameter("processInstanceId");
		String processCode = request.getParameter("processCode");
		String wbcxid = request.getParameter("wbcxid");
		Map<String,Object> map= new HashMap<>();
		try {
			boolean result = xtyhService.aduitDimissionCallback(processInstanceId,processCode,wbcxid);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉离职审核回调
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping("/web/lockUser")
	@ResponseBody
	public Map<String,Object> lockUser(HttpServletRequest request) {
		String yhmsJson = request.getParameter("yhmsJson");
		log.error("锁定用户--lockUser："+yhmsJson);
		Map<String,Object> map= new HashMap<>();
		try {
			boolean result = xtyhService.lockUser(yhmsJson);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}


	/**
	 * 根据用户id获取伙伴权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getHbqxList")
	@ResponseBody
	public List<String> getHbqxList(HttpServletRequest request){
		List<String> hbmcList = new ArrayList<>();
		String yhid = request.getParameter("yhid");
		if(StringUtil.isNotBlank(yhid)) {
			List<String> hbqxList = hbqxService.getHbidByYhid(yhid);
			if (!CollectionUtils.isEmpty(hbqxList)) {
				hbmcList = sjhbxxService.getHbmcByHbid(hbqxList);
			}
		}
		return hbmcList;
	}

}
