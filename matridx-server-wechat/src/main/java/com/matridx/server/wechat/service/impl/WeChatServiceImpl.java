package com.matridx.server.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.server.wechat.dao.entities.SjkjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatButtonModel;
import com.matridx.server.wechat.dao.entities.WeChatCommonButtonModel;
import com.matridx.server.wechat.dao.entities.WeChatComplexButtonModel;
import com.matridx.server.wechat.dao.entities.WeChatMenuModel;
import com.matridx.server.wechat.dao.entities.WeChatTextModel;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.dao.entities.WxcdDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.XxdjDto;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.server.wechat.service.svcinterface.ISjkjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWeChatService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.service.svcinterface.IXxdjService;
import com.matridx.server.wechat.util.MessageUtil;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeChatServiceImpl implements IWeChatService {

	@Autowired
	IWxyhService wxyhService;

	@Autowired
	ISjkjxxService sjkjxxService;

	@Autowired
	ISjxxService sjxxService;
	
	@Autowired
	ISjhbxxService sjhbxxService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	IXtszService xtszService;
	
	@Autowired
	IWbcxService wbcxService;
	
	@Autowired
	IXxdjService xxdjService;
	
	@Autowired
	IJcsjService jcsjService;

	@Autowired
	RedisUtil redisUtil;

	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Value("${matridx.wechat.appid:}")
	private String appid;
	@Value("${matridx.wechat.secret:}")
	private String secret;
	// API密钥
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	
	private Logger log = LoggerFactory.getLogger(WeChatServiceImpl.class);

	@Override
	public String authorization(String wxid) throws BusinessException {
		return WeChatUtils.authorization(appid,wxid,applicationurl);
	}

	@Override
	public String getAccessToken(String code) throws BusinessException {
		return WeChatUtils.getAccessToken(restTemplate,appid,secret,code,redisUtil);
	}

	/**
	 * 返回微信的扫一扫验证信息
	 * @param request
	 * @return
	 */
	public Map<String, Object> getWechatJsApiInfo(HttpServletRequest request, String wbcxdm) {
		Map<String, Object> map = new HashMap<String, Object>();
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(wbcxdm);
		wbcxDto = wbcxService.getDto(wbcxDto);
		if(wbcxDto == null){
			log.error("未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
			return map;
		}
		String ticket = WeChatUtils.getTicket(wbcxdm,redisUtil);
		String noncestr = RandomStringUtils.randomAlphanumeric(16);
		Long timestamp = System.currentTimeMillis() / 1000;
		map.put("ticket", ticket);
		map.put("noncestr", noncestr);
		map.put("timestamp", timestamp);
		DBEncrypt dbEncrypt = new DBEncrypt();
		map.put("appid", dbEncrypt.dCode(wbcxDto.getAppid()));
		StringBuffer shaStr = new StringBuffer();
		shaStr.append("jsapi_ticket=").append(ticket).append("&noncestr=").append(noncestr).append("&timestamp=")
				.append(timestamp).append("&url=").append(WeChatUtils.changeURLCode(request.getParameter("url"),redisUtil));
		String signString = Encrypt.encrypt(shaStr.toString(), "SHA-1");
		map.put("sign", signString);

		return map;
	}

	/**
	 * 验证签名
	 * 
	 * @param token     微信申请上填写的token
	 * @param signature 微信加密签名
	 * @param timestamp 时间戳
	 * @param nonce     随机数
	 * @return
	 */
	@Override
	public boolean checkSignature(String signature, String timestamp, String nonce, WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		if (StringUtil.isBlank(signature) || StringUtil.isBlank(timestamp) || StringUtil.isBlank(nonce))
			return false;

		DBEncrypt dbEncrypt = new DBEncrypt();
		String token = dbEncrypt.dCode(wbcxDto.getToken());
		// 1.定义数组存放tooken，timestamp,nonce
		String[] arr = new String[] { token, timestamp, nonce };
		// 2.对数组进行排序
		Arrays.sort(arr);
		// 3.生成字符串
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
		}
		// 4.sha1加密
		String encyStr = Encrypt.encrypt(sb.toString(), "SHA1");
		// 5.将加密后的字符串，与微信传来的加密签名比较，返回结果
		return encyStr.equalsIgnoreCase(signature);
	}

	/**
	 * 取消关注
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	@Override
	public boolean unsubscribe(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		if(userInfo!=null) {
			// 删除用户信息
			WxyhDto wxyhDto = new WxyhDto();
			wxyhDto.setWxid(userInfo.getOpenid());
			wxyhDto.setGzpt(wbcxDto.getWbcxid());
			boolean result = wxyhService.delete(wxyhDto);
			if (!result)
				return false;
			String jsonString = JSONObject.toJSONString(wxyhDto);
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_UNSUBSCIBE.getCode(), jsonString);
		}
		return true;
	}

	/**
	 * 增加关注
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	@Override
	public boolean subscribe(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		if (userInfo == null) {
			log.error("未正确获取用户信息：" + weChatTextModel.getFromUserName());
			return false;
		}
		userInfo.setTarget_user(weChatTextModel.getToUserName());
		WxyhDto wxyhDto = new WxyhDto();
		wxyhDto.setWxid(userInfo.getOpenid());

		String wxm = "";
		try {
			wxm = new String(userInfo.getNickname().getBytes("ISO-8859-1"), "UTF-8");
			wxyhDto.setWxm(wxm);
		} catch (UnsupportedEncodingException e) {
			log.error("转码错误");
			wxyhDto.setWxm(userInfo.getNickname());
			e.printStackTrace();
		}

		wxyhDto.setYhxb(userInfo.getSex());
		wxyhDto.setYhcs(userInfo.getCity());
		wxyhDto.setBqidlb(userInfo.getTagid_list());
		wxyhDto.setUnionid(userInfo.getUnionid());
		wxyhDto.setGzpt(wbcxDto.getWbcxid());
		wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
		// 通过微信ID查询用户信息
		List<WxyhDto> yhList = wxyhService.getWxyhListByWxid(wxyhDto);
		if (yhList != null && yhList.size() > 0) {
			// 更新用户信息
			boolean result = wxyhService.updateScbjByWxyh(wxyhDto);
			if (!result)
				return false;
		} else {
			// 新增用户信息
			boolean result = wxyhService.insertDto(wxyhDto);
			if (!result)
				return false;
		}
		wxyhDto.setGzptmc(wbcxDto.getGzpt());
		String jsonString = JSONObject.toJSONString(wxyhDto); 
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_SUBSCIBE.getCode(), jsonString);

		return true;
	}
	
	/**
	 * 取消关注(杰毅生物)
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	@Override
	public boolean unsubscribeMatridx(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		// 删除用户信息
		WxyhDto wxyhDto = new WxyhDto();
		wxyhDto.setWxid(userInfo.getOpenid());
		boolean result = wxyhService.delete(wxyhDto);
		if (!result)
			return false;
		String jsonString = JSONObject.toJSONString(wxyhDto);
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_UNSUBSCIBE.getCode(), jsonString);
		return true;
	}
	
	/**
	 * 增加关注(杰毅生物)
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	@Override
	public boolean subscribeMatridx(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		if (userInfo == null) {
			log.error("未正确获取用户信息：" + weChatTextModel.getFromUserName());
			return false;
		}
		//发送客服消息，关注回复
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.WECAHT_SEND_MESSAGE);
		xtszDto = xtszService.getDto(xtszDto);
		XtszDto m_xtszDto = new XtszDto();
		m_xtszDto.setSzlb(GlobalString.WECAHT_UPLOAD_MATERIAL);
		m_xtszDto = xtszService.getDto(m_xtszDto);
		if(xtszDto != null && m_xtszDto != null){
			WeChatUtils.sendGuestImage(restTemplate, userInfo.getOpenid(), m_xtszDto.getKzcs1(), xtszDto.getSzz(), xtszDto.getBz(), xtszDto.getKzcs1(), wbcxDto.getWbcxdm(),redisUtil);
		}
		userInfo.setTarget_user(weChatTextModel.getToUserName());
		WxyhDto wxyhDto = new WxyhDto();
		wxyhDto.setWxid(userInfo.getOpenid());
		wxyhDto.setWxm(userInfo.getNickname());
		wxyhDto.setYhxb(userInfo.getSex());
		wxyhDto.setYhcs(userInfo.getCity());
		wxyhDto.setBqidlb(userInfo.getTagid_list());
		wxyhDto.setUnionid(userInfo.getUnionid());
		wxyhDto.setGzpt(wbcxDto.getWbcxid());
		wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
		// 通过微信ID查询用户信息
		List<WxyhDto> yhList = wxyhService.getWxyhListByWxid(wxyhDto);
		if (yhList != null && yhList.size() > 0) {
			// 更新用户信息
			boolean result = wxyhService.updateScbjByWxyh(wxyhDto);
			if (!result)
				return false;
		} else {
			// 新增用户信息
			boolean result = wxyhService.insertDto(wxyhDto);
			if (!result)
				return false;
		}
		wxyhDto.setGzptmc(wbcxDto.getGzpt());
		String jsonString = JSONObject.toJSONString(wxyhDto);
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_SUBSCIBE.getCode(), jsonString);
		return true;
	}

	/**
	 * 客户发送消息处理
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	public boolean textDeal(WeChatTextModel weChatTextModel, WbcxDto wbcxDto,String msgType) {
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		//关注平台
		userInfo.setGzpt(weChatTextModel.getGzpt());
		//消息内容
		userInfo.setContent(weChatTextModel.getContent());
				
		String jsonString = JSONObject.toJSONString(userInfo);
		log.error("消息内容：" + jsonString);
		if(!MessageUtil.MSGTYPE_EVENT.equals(msgType)){
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAT_TEXT.getCode(), jsonString);
		}
		return true;
	}

	/**
	 * 点击菜单拉取消息时的事件推
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	public boolean clickEvent(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(),wbcxDto.getWbcxdm(),redisUtil);
		if (userInfo != null) {
			log.info("clickEvent ！  userInfo=" + userInfo.getOpenid() + " " + userInfo.getSubscribe() + " "
					+ userInfo.getNickname() + " " + userInfo.getSex() + " " + userInfo.getTagid_list() + " "
					+ userInfo.getCity() + " " + userInfo.getUnionid());
			log.info("clickEvent ！  weChatTextModel=" + weChatTextModel.getEvent() + " " + weChatTextModel.getEventKey()
					+ " " + weChatTextModel.getFromUserName() + " " + weChatTextModel.getContent() + " "
					+ weChatTextModel.getToUserName() + " " + weChatTextModel.getMsgType());
		} else {
			log.error("未找到用户：" + weChatTextModel.getFromUserName());
		}
		// 发送key到公众号
		return true;
	}

	/**
	 * 点击菜单跳转链接时的事件推送
	 * @param weChatTextModel 消息
	 * @param wbcxDto
	 * @return
	 */
	public boolean viewEvent(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// 判断是否需要跳转用户信息录入界面
		WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		// String jsonString = JSONObject.toJSONString(userInfo);
		// amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.ADD_CHECK_APPLY.getCode(), jsonString);
		return true;
	}
	
	/**
	 * 扫描带参数二维码的事件推送
	 * @param weChatTextModel
	 * @param wbcxDto
	 */
	@Override
	public void scanEvent(WeChatTextModel weChatTextModel, WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		// 获取用户信息
		WeChatUserModel userInfo = WeChatUtils.getUserInfo(restTemplate, weChatTextModel.getFromUserName(), wbcxDto.getWbcxdm(),redisUtil);
		if (userInfo == null) {
			log.error("未正确获取用户信息：" + weChatTextModel.getFromUserName());
		}
		// 判断二维码key值
		if("NCLM_TYPE".equals(weChatTextModel.getEventKey())){
			//根据微信ID和消息类型查询更新状态
			XxdjDto xxdjDto = new XxdjDto();
			xxdjDto.setWxid(userInfo.getOpenid());
			xxdjDto.setXxlx(weChatTextModel.getEventKey());
			xxdjDto = xxdjService.getDto(xxdjDto);
			if(xxdjDto == null){
				WeChatUtils.sendGuestText(restTemplate, userInfo.getOpenid(), "您还未进行注册登记， <a href='http://'"+applicationurl+"'/wechat/register/registerAdd?xxlx="+weChatTextModel.getEventKey()+"&wxid="+userInfo.getOpenid()+"&wbcxdm="+wbcxDto.getWbcxdm()+"'>点击注册</a>", wbcxDto.getWbcxdm(),redisUtil);
				log.error("未进行注册登记！！！" + userInfo.getOpenid());
				return;
			}
			if(StringUtil.isNotBlank(xxdjDto.getQdsj())){
				WeChatUtils.sendGuestText(restTemplate, userInfo.getOpenid(), "您已签到，无需重复签到！", wbcxDto.getWbcxdm(),redisUtil);
				log.error("已签到，无需重复签到！！！" + userInfo.getOpenid());
				return;
			}
			xxdjDto.setQdsj(String.valueOf(Calendar.getInstance().getTime()));
			boolean result = xxdjService.update(xxdjDto);
			if(result){
				WeChatUtils.sendGuestText(restTemplate, userInfo.getOpenid(), "签到成功！", wbcxDto.getWbcxdm(),redisUtil);
				log.error("签到成功！！！" + userInfo.getOpenid());
			}else{
				WeChatUtils.sendGuestText(restTemplate, userInfo.getOpenid(), "签到失败，请重新签到！", wbcxDto.getWbcxdm(),redisUtil);
				log.error("签到失败！！！" + userInfo.getOpenid());
			}
		}
	}

	/**
	 * 获取本地推送菜单信息
	 * @param request
	 * @return
	 */
	@Override
	public boolean createMenu(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String sign = request.getParameter("sign");
		boolean checkSign = WeChatUtils.checkSign(sign,redisUtil);
		if (!checkSign) {
			return false;
		}
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			log.error("未获取到本地请求的外部程序代码！");
			return false;
		}
		String s_jsonObject = request.getParameter("wxcdTreeList");
		JSONArray jsonObject = JSONObject.parseArray(s_jsonObject);
		Map<Integer, List<Object>> menuMap = new HashMap<>();
		int predepth = 0;
		for (int i = jsonObject.size() - 1; i >= 0; i--) {
			WxcdDto wxcdDto = JSONObject.toJavaObject((JSONObject) jsonObject.get(i), WxcdDto.class);
			int depth = Integer.parseInt(wxcdDto.getDepth());
			if (depth >= predepth) {
				// 做成最后一层形式
				WeChatCommonButtonModel sub = new WeChatCommonButtonModel();
				if ("view".equals(wxcdDto.getCdlx())) {
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setUrl(WeChatUtils.changeURLCode(wxcdDto.getLjdz(),redisUtil));
				}else if("miniprogram".equals(wxcdDto.getCdlx())){
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setUrl(WeChatUtils.changeURLCode(wxcdDto.getLjdz(),redisUtil));
					sub.setAppid(wxcdDto.getAppid());
					sub.setPagepath(wxcdDto.getYmlj());
				} else {
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setKey(wxcdDto.getCdj());
				}
				// 判断是否有同级list
				if (menuMap.get(depth) != null) {
					List<Object> list = menuMap.get(depth);
					list.add(0, sub);
					menuMap.put(depth, list);
				} else {
					List<Object> list = new ArrayList<>();
					list.add(sub);
					menuMap.put(depth, list);
				}
			} else if (depth < predepth) {
				// 做成带子菜单形式
				WeChatComplexButtonModel complex = new WeChatComplexButtonModel();
				complex.setName(wxcdDto.getCdm());
				List<Object> list = new ArrayList<>();
				list.add(complex);
				WeChatButtonModel[] arr = new WeChatButtonModel[menuMap.get(depth + 1).size()];
				arr = menuMap.get(depth + 1).toArray(new WeChatButtonModel[menuMap.get(depth + 1).size()]);
				complex.setSub_button(arr);
				// 判断是否有同级list
				if (menuMap.get(depth) != null) {
					for (int j = 0; j < menuMap.get(depth).size(); j++) {
						list.add(menuMap.get(depth).get(j));
					}
					menuMap.put(depth, list);
				} else {
					menuMap.put(depth, list);
				}
				// 清空depth+1级的数据
				menuMap.remove(depth + 1);
			}
			predepth = depth;
		}
		int size = menuMap.get(1).size();
		WeChatButtonModel[] menu_buttons = new WeChatButtonModel[size];
		for (int i = 0; i < menuMap.get(1).size(); i++) {
			menu_buttons[i] = (WeChatButtonModel) menuMap.get(1).get(i);
		}
		WeChatMenuModel menu = new WeChatMenuModel();
		menu.setButton(menu_buttons);
		boolean result = WeChatUtils.createMenu(restTemplate, menu, wbcxdm,redisUtil);
		if (!result)
			return false;
		return true;
	}

	/**
	 * 根据用户信息返回初期化送检信息
	 * @param code
	 * @param state
	 * @param wbcxdm
	 * @return
	 */
	public SjxxDto getReportInfoByUserAuth(String code, String state, String wbcxdm, String organ) {
		try {
			// 送检信息里的录入id采用 微信用户表里的用户id
			SjxxDto sjxxDto = new SjxxDto();
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
				WeChatUserModel userModel = WeChatUtils.getUserBaseInfoByLink(restTemplate, code, wbcxDto,redisUtil);
				
				if(userModel == null) {
					log.error("未获取到相应的用户信息  code:"+code +" ");
					//存在 送检清单过来的情况下，code已经无效了，所以可以直接使用  wxid
					return sjxxDto;
				}
				// 根据usermodel里的openid在 送检快捷信息表里获取信息 并放在页面上
				sjkjxxDto = sjkjxxService.getDtoById(userModel.getOpenid());
				sjxxDto.setWxid(userModel.getOpenid());
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
			return sjxxDto;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据微信用户信息返回系统的用户信息
	 * @param code
	 * @param state
	 * @param wbcxdm
	 * @return
	 */
	public SjxxDto getInspectionInfoByUserAuth(String code, String state, String wbcxdm, String organ) {
		SjxxDto resultDto = new SjxxDto();
		try {
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
				WeChatUserModel userModel = WeChatUtils.getUserBaseInfoByLink(restTemplate, code, wbcxDto,redisUtil);
				WxyhDto wxyhDto = new WxyhDto();
				wxyhDto.setWbcxdm(wbcxdm);
				wxyhDto.setWxid(userModel.getOpenid());
				List<WxyhDto> yhList = wxyhService.getWxyhListByWxid(wxyhDto);
				if (yhList != null && yhList.size() > 0) {
					resultDto.setLrry(yhList.get(0).getYhid());
					resultDto.setDb(yhList.get(0).getWxid());
					resultDto.setDbm(yhList.get(0).getYhm());
				}
				
				sjkjxxDto = sjkjxxService.getDtoById(userModel.getOpenid());
			}else{
				sjkjxxDto = sjkjxxService.getDtoById(organ);
			}
			// 送检医院
			resultDto.setSjdw(sjkjxxDto.getSjdw());
			// 科室
			resultDto.setKs(sjkjxxDto.getKs());
			// 送检医生
			resultDto.setSjys(sjkjxxDto.getSjys());
			// 医生电话
			resultDto.setYsdh(sjkjxxDto.getYsdh());
			// 其他科室
			resultDto.setQtks(sjkjxxDto.getQtks());
			// 检测单位
			resultDto.setJcdw(sjkjxxDto.getJcdw());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return resultDto;
	}

	/**
	 * 根据微信信息获取用户信息
	 * @param code
	 * @param state
	 * @param wbcxdm
	 * @return
	 */
	public WeChatUserModel getReportListPageByUserAuth(String code, String state,String wbcxdm) {
		try {
			// 根据点击信息获取用户的openid
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			if(wbcxDto == null){
				log.error("未获取到外部程序编码  "+wbcxdm +" 对应的外部程序信息！");
				return null;
			}
			WeChatUserModel userModel = WeChatUtils.getUserBaseInfoByLink(restTemplate, code, wbcxDto,redisUtil);
			return userModel;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 保存文件到服务器，同时更新附件存放表，更新fjid先确认是否存在，然后决定是新增还是更新
	 * 
	 * @param file
	 * @param fjcfbModel
	 * @return
	 */
	public boolean saveFile(MultipartFile file, FjcfbModel fjcfbModel) {
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(fjcfbModel.getYwid());
		fjcfbDto.setYwlx(fjcfbModel.getYwlx());
		fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
		
		boolean result = fjcfbService.insert(fjcfbModel);
		if (!result)
			return false;
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		InputStream fis = null;
		BufferedInputStream input = null;
		try {
			fis = file.getInputStream();
			input = new BufferedInputStream(fis);

			DBEncrypt dbEncrypt = new DBEncrypt();
			String wjlj = dbEncrypt.dCode(fjcfbModel.getWjlj());
			String storePath = dbEncrypt.dCode(fjcfbModel.getFwjlj());
			mkDirs(storePath);

			fos = new FileOutputStream(wjlj);
			output = new BufferedOutputStream(fos);
			int n = -1;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeStream(new Closeable[] { fis, input, output, fos });
		}
		return true;
	}
	
	/**
	 * 保存文件到服务器，同时更新附件存放表，更新fjid
	 * @param file
	 * @param fjcfbModel
	 * @return
	 */
	@Override
	public boolean saveFileOnly(MultipartFile file, FjcfbModel fjcfbModel) {
		// TODO Auto-generated method stub
		// 根据业务ID和业务类型删除附件再新增
		fjcfbService.deleteByYwidAndYwlx(fjcfbModel);
		boolean result = fjcfbService.insert(fjcfbModel);
		if (!result)
			return false;
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		InputStream fis = null;
		BufferedInputStream input = null;
		try {
			fis = file.getInputStream();
			input = new BufferedInputStream(fis);

			DBEncrypt dbEncrypt = new DBEncrypt();
			String wjlj = dbEncrypt.dCode(fjcfbModel.getWjlj());
			String storePath = dbEncrypt.dCode(fjcfbModel.getFwjlj());
			mkDirs(storePath);

			fos = new FileOutputStream(wjlj);
			output = new BufferedOutputStream(fos);
			int n = -1;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeStream(new Closeable[] { fis, input, output, fos });
		}
		return true;
	}
	/**
	 * 保存多文件到服务器，同时更新附件存放表，更新fjid
	 * @param files
	 * @param fjcfbModels
	 * @return
	 */
	@Override
	public boolean saveFilesOnly(List<MultipartFile> files, List<FjcfbModel> fjcfbModels){
		// TODO Auto-generated method stub
		// 根据业务ID和业务类型删除附件再新增
		FjcfbModel fjcfbModel = new FjcfbModel();
		List<String> ids = new ArrayList<>();
		if (fjcfbModels!=null && fjcfbModels.size()>0) {
			for (FjcfbModel model : fjcfbModels) {
				if (StringUtil.isBlank(fjcfbModel.getYwlx())){
					fjcfbModel.setYwlx(model.getYwlx());
				}
				if (StringUtil.isBlank(fjcfbModel.getYwid())){
					fjcfbModel.setYwid(model.getYwid());
				}
				ids.add(model.getFjid());
			}
		}
		fjcfbModel.setIds(ids);
		fjcfbService.deleteByYwidAndYwlx(fjcfbModel);
		boolean result = false;
		for (FjcfbModel model : fjcfbModels) {
			result = fjcfbService.insert(model);
			if (!result)
				return false;
		}
		DBEncrypt dbEncrypt = new DBEncrypt();
		for (int i = 0; i < files.size(); i++) {
			for (int j = 0; j < fjcfbModels.size(); j++) {
				String fileName = files.get(i).getOriginalFilename();
				String fjName = dbEncrypt.dCode(fjcfbModels.get(j).getFwjm());
				if (fileName.equals(fjName)) {
					MultipartFile file = files.get(i);
					FjcfbModel fjcfb = fjcfbModels.get(j);
					byte[] buffer = new byte[4096];
					FileOutputStream fos = null;
					BufferedOutputStream output = null;

					InputStream fis = null;
					BufferedInputStream input = null;
					try {
						fis = file.getInputStream();
						input = new BufferedInputStream(fis);

						String wjlj = dbEncrypt.dCode(fjcfb.getWjlj());
						String storePath = dbEncrypt.dCode(fjcfb.getFwjlj());
						mkDirs(storePath);

						fos = new FileOutputStream(wjlj);
						output = new BufferedOutputStream(fos);
						int n = -1;
						while ((n = input.read(buffer, 0, 4096)) > -1) {
							output.write(buffer, 0, n);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					} finally {
						closeStream(new Closeable[] { fis, input, output, fos });
					}
				}
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
	private boolean mkDirs(String storePath) {
		File file = new File(storePath);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdirs();
	}

	/**
	 * 关闭流
	 * 
	 * @param stream
	 */
	private static void closeStream(Closeable[] streams) {
		if (streams == null || streams.length < 1)
			return;
		for (int i = 0; i < streams.length; i++) {
			try {
				Closeable stream = streams[i];
				if (null != stream) {
					stream.close();
					stream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateISOtoUTF() throws UnsupportedEncodingException {
		List<WxyhDto> wxyhDtoList =wxyhService.getAllSj();
		for (WxyhDto wxyhDto:wxyhDtoList){
			String wxm=wxyhDto.getWxm();
			if(StringUtil.isNotBlank(wxm)&&!checkString(wxm,"ISO-8859-1")){
				WxyhDto wxyhDto1=new WxyhDto();
				String newWxm=new String(wxm.getBytes("ISO-8859-1"), "UTF-8");
				wxyhDto1.setWxm(newWxm);
				wxyhDto1.setYhid(wxyhDto.getYhid());
				wxyhService.updateWxmByid(wxyhDto1);
			}
		}

	}


	public boolean checkString(String str,String cod) throws UnsupportedEncodingException {
		if(java.nio.charset.Charset.forName(cod).newEncoder().canEncode(str)){
			return false;
		}

		return true;
	}
	    public void getUrlBatch() {
        try {
            restTemplate.getForObject("http://60.191.45.245/ws/verify/pagedataGetOnlyKey", Map.class);
			Object map=redisUtil.hget("UrlBatch","medlab");
			if(map==null){
				map=new HashMap<>();
			}
			Map<Object,Object> redisMap=(Map<Object,Object>)map;
			String mark=redisMap.get("mark")==null?"0":String.valueOf(redisMap.get("mark"));
			log.error("电信网络mark=========================="+mark);
			if("1".equals(mark)){
				log.error("电信网络恢复");
				restTemplate.getForObject("http://60.191.45.245/ws/verify/pagedataContentMsg?type=电信&mark=0", Map.class);//60.191.45.245
				restTemplate.getForObject("http://124.160.226.74/ws/verify/pagedataContentMsg?type=电信&mark=0", Map.class);
			}
			redisMap.put("mark","0");
			redisMap.put("count","0");
			redisUtil.hset("UrlBatch","medlab",redisMap);

        } catch (Exception e) {
			Object map=redisUtil.hget("UrlBatch","medlab");
			if(map==null){
				map=new HashMap<>();
			}
			Map<Object,Object> redisMap=(Map<Object,Object>)map;
			String countStr=redisMap.get("count")==null?"0":String.valueOf(redisMap.get("count"));
            int count = Integer.valueOf(countStr)+ 1;

            if (count ==3) {
                restTemplate.getForObject("http://124.160.226.74/ws/verify/pagedataContentMsg?type=电信", Map.class);
				redisMap.put("mark","1");
			}
			redisMap.put("count",count+"");
			redisUtil.hset("UrlBatch","medlab",redisMap);
            log.error("电信网络出现问题"+e.getMessage());
            e.printStackTrace();
        }


        try {
			restTemplate.getForObject("http://124.160.226.74/ws/verify/pagedataGetOnlyKey", Map.class);
			Object map=redisUtil.hget("UrlBatch","company");
			if(map==null){
				map=new HashMap<>();
			}
			Map<Object,Object> redisMap=(Map<Object,Object>)map;
			String mark=redisMap.get("mark")==null?"0":String.valueOf(redisMap.get("mark"));
			log.error("联通网络mark==================="+mark);
			if("1".equals(mark)){
				log.error("联通网络恢复");
				restTemplate.getForObject("http://124.160.226.74/ws/verify/pagedataContentMsg?type=联通&mark=0", Map.class);
				restTemplate.getForObject("http://60.191.45.245/ws/verify/pagedataContentMsg?type=联通&mark=0", Map.class);
			}
			redisMap.put("mark","0");
			redisMap.put("count","0");
			redisUtil.hset("UrlBatch","company",redisMap);

        } catch (Exception e) {

			Object map=redisUtil.hget("UrlBatch","company");
			if(map==null){
				map=new HashMap<>();
			}
			Map<Object,Object> redisMap=(Map<Object,Object>)map;
			String countStr=redisMap.get("count")==null?"0":String.valueOf(redisMap.get("count"));
			int count = Integer.valueOf(countStr)+ 1;

			if (count ==3) {
				restTemplate.getForObject("http://60.191.45.245/ws/verify/pagedataContentMsg?type=联通", Map.class);
				redisMap.put("mark","1");
			}
			redisMap.put("count",count+"");
			redisUtil.hset("UrlBatch","company",redisMap);
            log.error("联通网络出现问题"+e.getMessage());
            e.printStackTrace();
        }



    }


    /**
     * 测试用账号，用于注册rabbitmq
     *
     * @return
     */
    public boolean test() {

//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_UNSUBSCIBE.getCode(), JSONObject.toJSONString(new WeChatUserModel()));
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAT_TEXT.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SAMPLE_CONFIRM.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.ADD_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.MOD_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.DEL_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.CONFIRM_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.RESULT_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.ADD_PAYINFO.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAT_PAY_RESULT.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.COMMENT_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.CONTRACE_BASIC_W2P.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.CONTRACT_TOPIC.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.RESISTANCE_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.DETAILED_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.FEEDBACK_INSPECTION.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.ADD_PARTNER.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.MOD_PARTNER.getCode(), "测试文本");
//		amqpTempl.convertAndSend("wechat.exchange",MQWechatTypeEnum.DEL_PARTNER.getCode(), "测试文本");
        return true;
    }

}
