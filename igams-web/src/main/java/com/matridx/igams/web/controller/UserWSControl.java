package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiUserGetbyunionidRequest;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.BbxxDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.post.IXtyhDao;
import com.matridx.igams.web.service.svcinterface.IBbxxService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 微信功能对外接口
 * 生物信息部送检信息
 * 		/bioInformation/getUserInfoList 生信根据登录用户返回审核用户信息
 * 
 * */
@Controller
@RequestMapping("/ws")
public class UserWSControl extends BaseController{
	
	@Autowired
	IXtyhService xtyhService;
	@Autowired
	DingTalkUtil dingTalkUtil;
	@Autowired
	IXtyhDao xtyhDao;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	IBbxxService bbxxService;

	@Autowired
	IXxdyService xxdyService;
	@Value("${matridx.systemflg.logourl:}")
	private String logourl;
	@Value("${matridx.systemflg.icourl:/images/favicon.ico}")
	private String icourl;
	@Value("${matridx.systemflg.title:}")
	private String title;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.cookie.domain:}")
	private String domain;
	@Value("${matridx.loginCentre.url:}")
	private String loginCentreUrl;
	@Value("${matridx.cookie.cookiekey:}")
	private String cookiekey;

	private final Logger log = LoggerFactory.getLogger(UserWSControl.class);
	public static Client authClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new Client(config);
	}


	/**
	 * 机器人消息接收
	 * @param
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/rebot", method = RequestMethod.POST)
	@ResponseBody
	public String helloRobots(@RequestBody(required = false)JSONObject json){
		String content = json.getJSONObject("text").getString("content");
		String userId = json.get("senderStaffId").toString();
		XtyhDto xtyhDto = new XtyhDto();
		xtyhDto.setDdid(userId);
		XtyhDto dtoById = xtyhService.getYhid(xtyhDto);
		if (null != dtoById && StringUtil.isNotBlank(dtoById.getZsxm()) && StringUtil.isNotBlank(dtoById.getYhm())){
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx("ROBOT_INFO");
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			if (!CollectionUtils.isEmpty(ddxxglDtos)){
				for (DdxxglDto ddxxglDto : ddxxglDtos) {
					talkUtil.sendWorkMessage("",ddxxglDto.getDdid(),"您有一个新的消息","# 您有一个新的消息\n" +
									"* 工号："+dtoById.getYhm()+"\n" +
									"* 用户名："+dtoById.getZsxm()+"\n" +
									"* 消息："+content+"\n" +
									"* 时间："+simpleDateFormat.format(date));
				}
			}
		}
		return null;
	}

	/**
	 * 用于电脑端钉钉点击小程序直接打开页面的情况，主要是配置于 小程序 -> 开发管理 -> PC端首页地址
	 * @param authCode
	 * @return
	 * @throws Exception
	 */
	//接口地址：注意/auth与钉钉登录与分享的回调域名地址一致
	@RequestMapping(value = "/dingdingPCLogin", method = RequestMethod.GET)
	public ModelAndView dingdingPCLogin(@RequestParam(value = "authCode")String authCode,@RequestParam(value = "systemcode", required = false)String systemcode,HttpServletRequest request) throws Exception {

		log.error("dingdingPCLogin authCode:" + authCode + " systemcode:" + systemcode + " state:" + request.getParameter("state"));

		if(StringUtil.isBlank(systemcode)) {
			systemcode = request.getParameter("state");
		}
		// 获取access_token，注意正式代码要有异常流处理
		Map<String,String> dingtalkMap = dingTalkUtil.getDingTalkInfo(systemcode);
		String access_token = dingTalkUtil.getToken(dingtalkMap.get("appkey"), dingtalkMap.get("appsecret"));

		Client client = authClient();
		DBEncrypt dbEncrypt=new DBEncrypt();
		GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
				//应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
				.setClientId(dbEncrypt.dCode(dingtalkMap.get("appkey")))
				//应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
				.setClientSecret(dbEncrypt.dCode(dingtalkMap.get("appsecret")))
				.setCode(authCode)
				.setGrantType("authorization_code");
		GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
		//获取用户个人token
		String accessToken = getUserTokenResponse.getBody().getAccessToken();
		String userInfo = getUserinfo(accessToken);
		JSONObject jsonObject_t = JSON.parseObject(userInfo);
		String unionid = jsonObject_t.getString("unionId");
		ModelAndView mav = new ModelAndView("globalweb/index");
		DingTalkClient clientDingTalkClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/getbyunionid");
		OapiUserGetbyunionidRequest reqGetbyunionidRequest = new OapiUserGetbyunionidRequest();
		reqGetbyunionidRequest.setUnionid(unionid);
		OapiUserGetbyunionidResponse oapiUserGetbyunionidResponse = clientDingTalkClient.execute(reqGetbyunionidRequest, access_token);
//		System.out.println(oapiUserGetbyunionidResponse.getBody());
		// 根据userId即ddid,从xtyh表中获取用户的用户名和密码
		String userid = oapiUserGetbyunionidResponse.getResult().getUserid();
		XtyhDto xtyhDto=new XtyhDto();
		xtyhDto.setDdid(userid);
		xtyhDto.setMiniappid(dingtalkMap.get("miniappid"));
		XtyhDto xtyhDto_t = xtyhDao.getYhxxByDdid(xtyhDto);
		String yhm="";
		if(xtyhDto_t!=null){
			yhm=xtyhDto_t.getYhm();
		}
		//利用获取到的用户名和验证码去执行原本的登陆操作
		//直接用获取到的ddid当作用户名和密码去获取token
		try {
            List<NameValuePair> pairs = new ArrayList<>();
			pairs.add(new BasicNameValuePair("client_id", yhm));
			org.apache.commons.codec.binary.Base64 base64 = new Base64();
			String enPass = base64.encodeToString(authCode.getBytes());
			pairs.add(new BasicNameValuePair("client_secret", enPass));
			pairs.add(new BasicNameValuePair("grant_type", "matridx"));
			pairs.add(new BasicNameValuePair("sign", dbEncrypt.eCode(authCode)));//DBEncrypt加密临时授权码传入
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse httpresponse = null;
			try {
				httpclient = HttpClients.createDefault();
				HttpPost httppost = new HttpPost(applicationurl+"/oauth/token");
				if (!CollectionUtils.isEmpty(pairs)) {
					httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				}
				httpresponse = httpclient.execute(httppost);
				String response = EntityUtils.toString(httpresponse.getEntity());
				if(response.contains("error")){
					mav = new ModelAndView("globalweb/sessionOut");
					mav.addObject("linshi_acctoken", StringUtil.generateUUID());
					mav.addObject("access_token","01");
					mav.addObject("loginCentreUrl",loginCentreUrl);
				}else{
					JSONObject jsonObject = JSONObject.parseObject(response);
					String token = jsonObject.getString("access_token");
					String refresh_token = jsonObject.getString("refresh_token");
					//用获取到的token去执行原本登录的操作
					List<BbxxDto> bbxxList = bbxxService.getBbxxDtoList();
					mav.addObject("access_token", token);
					mav.addObject("ref", refresh_token);
					mav.addObject("bbxxlist", bbxxList);

					Map<String, Object> resultMap = xtyhService.initMenu(token);

					if (resultMap != null) {
						mav.addObject("topmenu", resultMap.get("topmenu"));

						mav.addObject("mobiletopmenu", resultMap.get("mobiletopmenu"));

						mav.addObject("menuList", resultMap.get("menuList"));

						mav.addObject("jsxxList", resultMap.get("jsxxList"));

						mav.addObject("xtyhDto", resultMap.get("xtyhDto"));
					} else {
						mav = new ModelAndView("globalweb/login");
					}
					mav.addObject("logourl", logourl);
					mav.addObject("icourl", icourl);
					mav.addObject("title", title);
				}
			}catch (Exception e){
				log.error(e.toString());
			}finally {
				if (httpclient != null) {
					httpclient.close();
				}
				if (httpresponse != null) {
					httpresponse.close();
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
		return mav;
	}

	/**
	 * 用于电脑端钉钉点击小程序直接打开页面的情况，主要是配置于 小程序 -> 开发管理 -> PC端首页地址
	 * @param authCode
	 * @return
	 * @throws Exception
	 */
	//接口地址：注意/auth与钉钉登录与分享的回调域名地址一致
	@RequestMapping(value = "/dingdingPCLoginVue", method = RequestMethod.GET)
	public ModelAndView dingdingPCLoginVue(@RequestParam(value = "authCode")String authCode,@RequestParam(value = "systemcode", required = false)String systemcode,HttpServletRequest request) throws Exception {
		
		log.error("dingdingPCLoginVue authCode:" + authCode + " systemcode:" + systemcode);
		
		if(StringUtil.isBlank(systemcode)) {
			systemcode = request.getParameter("state");
		}
		// 获取access_token，注意正式代码要有异常流处理
		Map<String,String> dingtalkMap = dingTalkUtil.getDingTalkInfo(systemcode);
		String access_token = dingTalkUtil.getToken(dingtalkMap.get("appkey"), dingtalkMap.get("appsecret"));

		Client client = authClient();
		DBEncrypt dbEncrypt=new DBEncrypt();
		GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
				//应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
				.setClientId(dbEncrypt.dCode(dingtalkMap.get("appkey")))
				//应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
				.setClientSecret(dbEncrypt.dCode(dingtalkMap.get("appsecret")))
				.setCode(authCode)
				.setGrantType("authorization_code");
		GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
		//获取用户个人token
		String accessToken = getUserTokenResponse.getBody().getAccessToken();
		String userInfo = getUserinfo(accessToken);
		JSONObject jsonObject_t = JSON.parseObject(userInfo);
		String unionid = jsonObject_t.getString("unionId");
		ModelAndView mav = new ModelAndView("globalweb/dingding_redirec");
		DingTalkClient clientDingTalkClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/getbyunionid");
		OapiUserGetbyunionidRequest reqGetbyunionidRequest = new OapiUserGetbyunionidRequest();
		reqGetbyunionidRequest.setUnionid(unionid);
		OapiUserGetbyunionidResponse oapiUserGetbyunionidResponse = clientDingTalkClient.execute(reqGetbyunionidRequest, access_token);
//		System.out.println(oapiUserGetbyunionidResponse.getBody());
		// 根据userId即ddid,从xtyh表中获取用户的用户名和密码
		String userid = oapiUserGetbyunionidResponse.getResult().getUserid();
		XtyhDto xtyhDto=new XtyhDto();
		xtyhDto.setDdid(userid);
		xtyhDto.setMiniappid(dingtalkMap.get("miniappid"));
		XtyhDto xtyhDto_t = xtyhDao.getYhxxByDdid(xtyhDto);
		String yhm="";
		if(xtyhDto_t!=null){
			yhm=xtyhDto_t.getYhm();
		}
		//利用获取到的用户名和验证码去执行原本的登陆操作
		//直接用获取到的ddid当作用户名和密码去获取token
		try {
            List<NameValuePair> pairs = new ArrayList<>();
			pairs.add(new BasicNameValuePair("client_id", yhm));
			org.apache.commons.codec.binary.Base64 base64 = new Base64();
			String enPass = base64.encodeToString(authCode.getBytes());
			pairs.add(new BasicNameValuePair("client_secret", enPass));
			pairs.add(new BasicNameValuePair("grant_type", "matridx"));
			pairs.add(new BasicNameValuePair("sign", dbEncrypt.eCode(authCode)));//DBEncrypt加密临时授权码传入
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse httpresponse = null;
			try {
				httpclient = HttpClients.createDefault();
				HttpPost httppost = new HttpPost(applicationurl+"/oauth/token");
				if (!CollectionUtils.isEmpty(pairs)) {
					httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				}
				httpresponse = httpclient.execute(httppost);
				String response = EntityUtils.toString(httpresponse.getEntity());

				if(response.contains("error")){
//					mav = new ModelAndView("globalweb/sessionOut");
//					mav.addObject("linshi_acctoken", StringUtil.generateUUID());
//					mav.addObject("access_token","01");
//					mav.addObject("loginCentreUrl",loginCentreUrl);
					mav.addObject("status","false");
				}else{
					JSONObject jsonObject = JSONObject.parseObject(response);
					String token = jsonObject.getString("access_token");
					String refresh_token = jsonObject.getString("refresh_token");
					//用获取到的token去执行原本登录的操作
					mav.addObject("access_token", token);
					mav.addObject("ref", refresh_token);
					mav.addObject("cookiekey", cookiekey);
					mav.addObject("applicationurl", applicationurl);
					mav.addObject("domain",domain);
					Map<String, Object> resultMap = xtyhService.initMenu(token);

					if (resultMap != null) {
						mav.addObject("status","true");
					} else {
						mav.addObject("status","false");
					}
					mav.addObject("logourl", logourl);
					mav.addObject("icourl", icourl);
					mav.addObject("title", title);
				}
			}catch (Exception e){
				log.error(e.toString());
			}finally {
				if (httpclient != null) {
					httpclient.close();
				}
				if (httpresponse != null) {
					httpresponse.close();
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
		return mav;
	}


	public static com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalkcontact_1_0.Client(config);
	}
	/**
	 * 获取用户个人信息
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public String getUserinfo(String accessToken) throws Exception {
		com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
		GetUserHeaders getUserHeaders = new GetUserHeaders();
		getUserHeaders.xAcsDingtalkAccessToken = accessToken;
		//获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
        return JSON.toJSONString(client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody());
	}



	/**
	 * 根据group查询用户信息
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/bioInformation/getUserInfoByGroup")
	@ResponseBody
	public Map<String,Object> getUserInfoByWorkNumber(XtyhDto xtyhDto){
		Map<String,Object> map= new HashMap<>();
		if (null != xtyhDto && StringUtil.isNotBlank(xtyhDto.getGrouping())){
			List<XtyhDto> xtyhDtos = xtyhService.getListByYhm(xtyhDto);
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(XtyhDto.class, "yhm","zsxm");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("status","success");
			map.put("userInfo", JSONObject.toJSONString(xtyhDtos,listFilters));
		}else{
			map.put("status","fail");
			map.put("message", "未获取到该用户Group!");
		}
		return map;
	}


	/**
	 * 根据工号查询用户信息
	 * @return
	 * @throws
	 */
	@RequestMapping(value="/bioInformation/getUserInfoList")
	@ResponseBody
	public Map<String,Object> getUserInfoList(XtyhDto xtyhDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		List<XtyhDto> xtyhDtos=new ArrayList<>();
		List<XtyhDto> xtyhDtoList=new ArrayList<>();
		xtyhDto.setMrfz("1");
		xtyhDto.setSfsd("0");
		String userName = request.getParameter("username");
		if(StringUtil.isNotBlank(userName)) {
			XtyhDto t_xtyhDto = xtyhService.getDtoByName(userName);
			if(t_xtyhDto!=null) {
                xtyhDto.setSignflg(t_xtyhDto.getDdid());
				xtyhDto.setYhid(t_xtyhDto.getYhid());
				xtyhDto.setDqjs(t_xtyhDto.getDqjs());
            }
		}
		XxdyDto xxdyDto=new XxdyDto();
		xxdyDto.setDylxcsdm("YHSHJYQX");
		xxdyDto.setDyxxid(xtyhDto.getYhid());
		//获取用户审核检验权限 的对应信息，里面的cskz1 保存的是 对应的工号
		List<XxdyDto> xxdyDtos=xxdyService.getDtoList(xxdyDto);
		xxdyDto.setDylxcsdm("JSSHJYQX");
		xxdyDto.setDyxxid(xtyhDto.getDqjs());
		List<XxdyDto> js_xxdyDtos=xxdyService.getDtoList(xxdyDto);
		if(!CollectionUtils.isEmpty(xxdyDtos) || !CollectionUtils.isEmpty(js_xxdyDtos)){
			List<String> jy_yhms=new ArrayList<>();
			List<String> sh_yhms=new ArrayList<>();
			if(!CollectionUtils.isEmpty(xxdyDtos)){
				String t_yhms=xxdyDtos.get(0).getKzcs1();//默认取第一条的cskz1，检验人
				String b_yhms=xxdyDtos.get(0).getKzcs2();//默认取第一条的cskz2，审核人
				if(StringUtil.isNotBlank(t_yhms)){
					String[] sz_yhms=t_yhms.split(",");
					List<String> yh_yhms= Arrays.asList(sz_yhms);
					for(String yh_yhm:yh_yhms){
						jy_yhms.add(yh_yhm);
					}
				}
				if(StringUtil.isNotBlank(b_yhms)){
					String[] shsz_yhms=b_yhms.split(",");
					List<String> shyh_yhms= Arrays.asList(shsz_yhms);
					for(String shyh_yhm:shyh_yhms){
						sh_yhms.add(shyh_yhm);
					}
				}
			}
			if(!CollectionUtils.isEmpty(js_xxdyDtos)) {
				String t_jsms = js_xxdyDtos.get(0).getKzcs1();//默认取第一条的cskz1
				String sht_jsms = js_xxdyDtos.get(0).getKzcs2();//默认取第一条的cskz2
				if (StringUtil.isNotBlank(t_jsms)) {
					String[] sz_jsms = t_jsms.split(",");
					List<String> js_yhms = Arrays.asList(sz_jsms);
					for (String js_yhm : js_yhms) {
						jy_yhms.add(js_yhm);
					}
				}
				if (StringUtil.isNotBlank(sht_jsms)) {
					String[] shsz_jsms = sht_jsms.split(",");
					List<String> shjs_yhms = Arrays.asList(shsz_jsms);
					for (String shjs_yhm : shjs_yhms) {
						sh_yhms.add(shjs_yhm);
					}
				}
			}
			xtyhDto.setYhms(jy_yhms);
			if(!CollectionUtils.isEmpty(jy_yhms)){
				List<XtyhDto> t_xtyhDtos = xtyhService.getListByYhms(xtyhDto);
				if(!CollectionUtils.isEmpty(t_xtyhDtos)){
					for(XtyhDto xtyhDto1:t_xtyhDtos){
						XtyhDto jyryDto=new XtyhDto();
						jyryDto.setMrfz("JYRY");
						jyryDto.setYhm(xtyhDto1.getYhm());
						jyryDto.setZsxm(xtyhDto1.getZsxm());
						xtyhDtos.add(jyryDto);
					}
				}
			}

			xtyhDto.setYhms(sh_yhms);
			if(!CollectionUtils.isEmpty(sh_yhms)) {
				List<XtyhDto> sh_xtyhDtos = xtyhService.getListByYhms(xtyhDto);
				if (!CollectionUtils.isEmpty(sh_xtyhDtos)) {
					for (XtyhDto xtyhDto1 : sh_xtyhDtos) {
						XtyhDto shryDto = new XtyhDto();
						shryDto.setMrfz("SHRY");
						shryDto.setYhm(xtyhDto1.getYhm());
						shryDto.setZsxm(xtyhDto1.getZsxm());
						xtyhDtos.add(shryDto);
					}
				}
			}
		}else{
			xtyhDtos = xtyhService.getListByYhm(xtyhDto);
			xtyhDto.setMrfz("ALL");
			xtyhDtoList= xtyhService.getListByYhm(xtyhDto);
			if(!CollectionUtils.isEmpty(xtyhDtoList)){//分配到list中
				for(XtyhDto xtyhDto1:xtyhDtoList){
					XtyhDto shryDto=new XtyhDto();
					shryDto.setMrfz("SHRY");
					shryDto.setYhm(xtyhDto1.getYhm());
					shryDto.setZsxm(xtyhDto1.getZsxm());
					xtyhDtos.add(shryDto);
					XtyhDto jyryDto=new XtyhDto();
					jyryDto.setMrfz("JYRY");
					jyryDto.setYhm(xtyhDto1.getYhm());
					jyryDto.setZsxm(xtyhDto1.getZsxm());
					xtyhDtos.add(jyryDto);
				}
			}
		}

		Map<String, List<XtyhDto>> collect = xtyhDtos.stream().collect(Collectors.groupingBy(XtyhDto::getMrfz));
		Iterator<Map.Entry<String, List<XtyhDto>>> entries = collect.entrySet().iterator();
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(XtyhDto.class, "yhm","zsxm");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		while (entries.hasNext()) {
			Map.Entry<String, List<XtyhDto>> entry = entries.next();
			List<XtyhDto> dtoList = entry.getValue();
			if ("JYRY".equals( entry.getKey())){
				map.put("JYRY",JSONObject.toJSONString(dtoList,listFilters));
			}
			if ("SHRY".equals( entry.getKey())){
				map.put("SHRY",JSONObject.toJSONString(dtoList,listFilters));
			}
		}
		if(!map.containsKey("JYRY")) {
			map.put("JYRY","");
		}
		if(!map.containsKey("SHRY")) {
			map.put("SHRY","");
		}
		map.put("status","success");
//		map.put("userInfo", JSONObject.toJSONString(collect,listFilters));
		return map;
	}

	/**
	 * 钉钉扫码登录回调验证
	 */
	@RequestMapping(value = "/dingdingLogin")
	public ModelAndView dingdingLogin(String code,String state) throws ApiException {
		ModelAndView mav = new ModelAndView("globalweb/login");
		mav = dingdingLoginCallback(mav,code,state);
		String redirectUrl=null;
		String[] split=state.split(",");
		if(split.length >2){
			redirectUrl=split[2];
		}
		mav.addObject("cookiekey",cookiekey);
		mav.addObject("redirectUrl",redirectUrl);
		return mav;
	}

	public ModelAndView dingdingLoginCallback(ModelAndView mav,String code,String state) throws ApiException {
		mav.addObject("domain",domain);
		// 获取access_token，注意正式代码要有异常流处理
		DBEncrypt dbEncrypt=new DBEncrypt();
		String[] split=state.split(",");
		String appKey=dbEncrypt.eCode(split[0]);
		String appSecret=dbEncrypt.eCode(split[1]);
		String access_token = dingTalkUtil.getToken(appKey,appSecret);
		log.error("钉钉扫码获取到的appKey:"+appKey+" appSecret:"+appSecret+" access_token:"+access_token);

		// 通过临时授权码获取授权用户的个人信息
		DingTalkClient client2 = new DefaultDingTalkClient("https://oapi.dingtalk.com/sns/getuserinfo_bycode");
		OapiSnsGetuserinfoBycodeRequest reqBycodeRequest = new OapiSnsGetuserinfoBycodeRequest();
		// 通过扫描二维码，跳转指定的redirect_uri后，向url中追加的code临时授权码
		reqBycodeRequest.setTmpAuthCode(code);
		OapiSnsGetuserinfoBycodeResponse bycodeResponse = client2.execute(reqBycodeRequest, dbEncrypt.dCode(appKey), dbEncrypt.dCode(appSecret));
		if(40078==bycodeResponse.getErrcode()){
			//由于授权码只能使用一次，当用户刷新页面时返回到长时间未操作的页面，让其重新扫码
			mav = new ModelAndView("globalweb/sessionOut");
			mav.addObject("linshi_acctoken",StringUtil.generateUUID());
			mav.addObject("access_token","01");
			mav.addObject("loginCentreUrl",loginCentreUrl);
			return mav;
		}
		// 根据unionid获取userid
		String unionid = bycodeResponse.getUserInfo().getUnionid();
		DingTalkClient clientDingTalkClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/getbyunionid");
		OapiUserGetbyunionidRequest reqGetbyunionidRequest = new OapiUserGetbyunionidRequest();
		reqGetbyunionidRequest.setUnionid(unionid);
		OapiUserGetbyunionidResponse oapiUserGetbyunionidResponse = clientDingTalkClient.execute(reqGetbyunionidRequest, access_token);

		// 根据userId即ddid,从xtyh表中获取用户的用户名和密码
		String userid = oapiUserGetbyunionidResponse.getResult().getUserid();
		XtyhDto xtyhDto=new XtyhDto();
		xtyhDto.setDdid(userid);
		Map<String, String> dinginfoMap = talkUtil.getDingTalkInfoByAppid(appKey);
		xtyhDto.setWbcxid(dinginfoMap.get("wbcxid"));
		XtyhDto xtyhDto_t = xtyhDao.getYhxxByDdid(xtyhDto);
		String yhm="";
		if(xtyhDto_t!=null){
			yhm=xtyhDto_t.getYhm();
		}
		//利用获取到的用户名和验证码去执行原本的登陆操作
		//直接用获取到的ddid当作用户名和密码去获取token
		try {
            List<NameValuePair> pairs = new ArrayList<>();
			pairs.add(new BasicNameValuePair("client_id", yhm));
			org.apache.commons.codec.binary.Base64 base64 = new Base64();
			String enPass = base64.encodeToString(code.getBytes());
			pairs.add(new BasicNameValuePair("client_secret", enPass));
			pairs.add(new BasicNameValuePair("grant_type", "matridx"));
			pairs.add(new BasicNameValuePair("sign", dbEncrypt.eCode(code)));//DBEncrypt加密临时授权码传入
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse httpresponse = null;
			try {
				httpclient = HttpClients.createDefault();
				HttpPost httppost = new HttpPost(applicationurl+"/oauth/token");
				if (!CollectionUtils.isEmpty(pairs)) {
					httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				}
				httpresponse = httpclient.execute(httppost);
				String response = EntityUtils.toString(httpresponse.getEntity());
				if(response.contains("error")){
					mav = new ModelAndView("globalweb/sessionOut");
					mav.addObject("linshi_acctoken",StringUtil.generateUUID());
					mav.addObject("access_token","01");
					mav.addObject("loginCentreUrl",loginCentreUrl);
				}else{
					JSONObject jsonObject = JSONObject.parseObject(response);
					String token = jsonObject.getString("access_token");
					String refresh_token = jsonObject.getString("refresh_token");
					mav.addObject("access_token", token);
					mav.addObject("ref", refresh_token);
				}
			}catch (Exception e){
				log.error(e.toString());
				mav = new ModelAndView("globalweb/sessionOut");
				mav.addObject("linshi_acctoken",StringUtil.generateUUID());
				mav.addObject("access_token","01");
				mav.addObject("loginCentreUrl",loginCentreUrl);
			}finally {
				if (httpclient != null) {
					httpclient.close();
				}
				if (httpresponse != null) {
					httpresponse.close();
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
			mav = new ModelAndView("globalweb/sessionOut");
			mav.addObject("linshi_acctoken",StringUtil.generateUUID());
			mav.addObject("access_token","01");
			mav.addObject("loginCentreUrl",loginCentreUrl);
		}
//        return ServiceResult.success(map);
		return mav;
	}

	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value ="/user/pageGetListUser")
	@ResponseBody
	public Map<String,Object> pageGetListUser(XtyhDto xtyhDto){
		List<XtyhDto> t_List = xtyhService.getPagedDtoList(xtyhDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtyhDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

}
