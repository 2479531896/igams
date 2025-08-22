package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IStatisService;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.web.dao.entities.BbxxDto;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.XtyhModel;
import com.matridx.igams.web.dao.entities.XtzyDto;
import com.matridx.igams.web.service.svcinterface.IBbxxService;
import com.matridx.igams.web.service.svcinterface.IWbcxService;
import com.matridx.igams.web.service.svcinterface.IXtjsService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.web.service.svcinterface.IXtzyService;
import com.matridx.igams.wechat.dao.entities.YhqtxxDto;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexControl extends BaseController {

	@Autowired
	IXtyhService xtyhService;

	@Autowired
	IXtjsService xtjsService;

	@Autowired
	IXtzyService xtzyService;

	@Autowired
	IYhqtxxService yhqtxxService;


	@Autowired
	DingTalkUtil dingTalkUtil;

	@Autowired
	IXxglService xxglService;

	@Autowired
	IBbxxService bbxxService;
	// @Autowired
	// private DataSource postsqlDataSource;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.systemflg.logourl:}")
	private String logourl;
	@Value("${matridx.systemflg.icourl:/images/favicon.ico}")
	private String icourl;
	@Value("${matridx.systemflg.title:}")
	private String title;
	@Value("${matridx.vueView.url:}")
	private String vueViewUrl;
	//cookie的有效域，就是域名
	@Value("${matridx.cookie.domain:}")
	private String domain;
	@Value("${matridx.cookie.cookiekey:}")
	private String cookiekey;
	//用于删除cookie的地址
	@Value("${matridx.loginCentre.url:}")
	private String loginCentreUrl;
	@Autowired
	private IWbcxService wbcxService;

	@Autowired
	private IWbaqyzService wbaqyzService;
	private final Logger log = LoggerFactory.getLogger(IndexControl.class);

	@RequestMapping("/")
	public ModelAndView toLogin() {
		DateFormat df = new SimpleDateFormat("yyyyMdd");
		String key = df.format(new Date());
		List<BbxxDto> bbxxList = bbxxService.getBbxxDtoList();
		//登录界面增加温馨提示，去redis中获取matridx_xtsz --> szlb为login.reminder的数据
		XtszDto xtszDto = new XtszDto();
		if (redisUtil.hget("matridx_xtsz", "login.reminder") != null){
			Object xtszObj = redisTemplate.opsForHash().get("matridx_xtsz", "login.reminder");
			xtszDto = JSON.parseObject(xtszObj.toString(),XtszDto.class);
		}

		ModelAndView mav = new ModelAndView("globalweb/login");
		mav.addObject("key", key);
		mav.addObject("logourl", logourl);
		mav.addObject("icourl", icourl);
		mav.addObject("title", title);
		mav.addObject("bbxxlist", bbxxList);
		if(xtszDto == null) {
            xtszDto = new XtszDto();
        }
		mav.addObject("xtszDto", xtszDto);
		mav.addObject("applicationurl", applicationurl);
		DBEncrypt dbEncrypt=new DBEncrypt();
		List<Object> l_wbcxList = redisUtil.lGet("wbcxList");
		List<WbcxDto> wbcxDtos = null;
		if(l_wbcxList==null|| l_wbcxList.isEmpty()){
			log.error("IndexControl toLogin 进入根目录获取wbcx  Error--lwj--");
			WbcxDto wbcxDto=new WbcxDto();
			wbcxDto.setLx("DINGDING");
			wbcxDtos=wbcxService.getDtoList(wbcxDto);
			if(!CollectionUtils.isEmpty(wbcxDtos)){
				for(WbcxDto dto:wbcxDtos){
					if(StringUtil.isNotBlank(dto.getAppid())){
						String appid=dto.getAppid();
						dto.setAppid(dbEncrypt.dCode(appid));
						String secret=dto.getSecret();
						dto.setSecret(dbEncrypt.dCode(secret));
					}
					redisUtil.lSet("wbcxList",  JSON.toJSONString(dto));
				}
			}

		}else{
			wbcxDtos = new ArrayList<>();
			for(Object o:l_wbcxList){
				wbcxDtos.add(JSON.parseObject(o.toString(),WbcxDto.class));
			}
		}

		mav.addObject("wbcxDtos", wbcxDtos);
		mav.addObject("domain",domain);
		mav.addObject("cookiekey",cookiekey);
		return mav;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request,String redirectUrl) {
		ModelAndView mav = new ModelAndView("globalweb/login");
		String indexType= request.getParameter("indexType");
		if(StringUtil.isNotBlank(indexType)){
			mav = new ModelAndView("globalweb/loginOther");
		}
		mav.addObject("logourl", logourl);
		DBEncrypt dbEncrypt=new DBEncrypt();
		mav.addObject("domain",domain);
		mav.addObject("cookiekey",cookiekey);
		mav.addObject("applicationurl", applicationurl);
		mav.addObject("redirectUrl",redirectUrl);
		String n_titleString = request.getParameter("title");
		mav.addObject("title", StringUtil.isBlank(n_titleString)?title:n_titleString);

		List<Object> l_wbcxList = redisUtil.lGet("wbcxList");
		List<WbcxDto> wbcxDtos = null;
		if(l_wbcxList==null|| l_wbcxList.isEmpty()){
			log.error("IndexControl login 登录时获取wbcx  Error--lwj--");
			WbcxDto wbcxDto=new WbcxDto();
			wbcxDto.setLx("DINGDING");
			wbcxDtos=wbcxService.getDtoList(wbcxDto);
			if(!CollectionUtils.isEmpty(wbcxDtos)){
				for(WbcxDto dto:wbcxDtos){
					if(StringUtil.isNotBlank(dto.getAppid())){
						String appid=dto.getAppid();
						dto.setAppid(dbEncrypt.dCode(appid));
						String secret=dto.getSecret();
						dto.setSecret(dbEncrypt.dCode(secret));
					}
					redisUtil.lSet("wbcxList",  JSON.toJSONString(dto));
				}
			}

		}else{
			wbcxDtos = new ArrayList<>();
			for(Object o:l_wbcxList){
				wbcxDtos.add(JSON.parseObject(o.toString(),WbcxDto.class));
			}
		}
		List<BbxxDto> bbxxList = bbxxService.getBbxxDtoList();
		mav.addObject("bbxxlist", bbxxList);
		mav.addObject("wbcxDtos", wbcxDtos);
		return mav;
	}

	/**
	 * 老系统页面通过统一身份认证登录
	 * @param access_token
	 * @param ref
	 * @param mm_flg
	 * @return
	 */
	@RequestMapping("/tokenToIndex")
	public ModelAndView tokenToIndex(String access_token, String ref, String mm_flg, RedirectAttributes attr){
		//采用RedirectAttributes 或者 redirect:/index 方法，都会把参数显示在url上，所以采用页面进行跳转避开参数显示问题
        ModelAndView mav = new ModelAndView("globalweb/loginCentreLogin");
        mav.addObject("access_token",access_token);
        mav.addObject("ref",ref);
        mav.addObject("mm_flg",mm_flg);
		return mav;
	}

	@RequestMapping("/accessDenied")
	public ModelAndView accessDenied(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("globalweb/sessionOut");
		mav.addObject("linshi_acctoken",StringUtil.generateUUID());
		mav.addObject("vueViewUrl",vueViewUrl);
		mav.addObject("access_token",request.getParameter("access_token"));
		mav.addObject("loginCentreUrl",loginCentreUrl);
		mav.addObject("domain",domain);
		mav.addObject("cookiekey",cookiekey);
		return mav;
	}

	/**
	 * 系统登陆时验证用户名密码，如果已经存在现有的token，则直接采用现有的，不在调用登陆接口，如果没有则调用新登陆
	 * 确认token的有效期，如果低于1小时，则重新登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/oauth/localtoken")
	@ResponseBody
	public Map<String, String> loginCheck(HttpServletRequest request) {
		Map<String, String> returnMap = null;
		try {
			// 检测是否已经存在现有的token
			// returnMap = checkLoginInfo(request);

			returnMap = turnToSpringSecurityLogin(request);
			/*
			 * if(returnMap==null || !"true".equals(returnMap.get("isLogin"))) { //转入正常的验证
			 * returnMap = turnToSpringSecurityLogin(request); }
			 */
		} catch (Exception e) {
			log.error(e.toString());
		}
		log.error("====/oauth/localtoken 认证"+JSON.toJSONString(returnMap));		
		return returnMap;
	}

	@RequestMapping("/oauth/externaltoken")
	@ResponseBody
	public Map<String, String> wbaqyzLoginCheck(HttpServletRequest request) {
		Map<String, String> returnMap = null;
		try {
			// 检测是否已经存在现有的token
			// returnMap = checkLoginInfo(request);

			returnMap = turnToSpringSecurityLogin(request);
			WbaqyzDto wbaqyzDto=new WbaqyzDto();
			String username = request.getParameter("username");
			wbaqyzDto.setCode(username);
			wbaqyzDto.setYzsj(LocalDateTime.now().format(
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			));
			if(returnMap==null){
				wbaqyzDto.setYzcs("1");
			}else{
				if(returnMap.get("access_token")!=null){
					wbaqyzDto.setYzcs("0");
				}else{
					wbaqyzDto.setYzcs("1");
				}

			}
			wbaqyzService.updateDtoByIndex(wbaqyzDto);
			/*
			 * if(returnMap==null || !"true".equals(returnMap.get("isLogin"))) { //转入正常的验证
			 * returnMap = turnToSpringSecurityLogin(request); }
			 */
		} catch (Exception e) {
			log.error(e.toString());
		}
		log.error("====/oauth/localtoken 认证:"+JSON.toJSONString(returnMap));
		return returnMap;
	}

	/**
	 * 忘记密码跳转页面
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/client/toForgetPwd")
	public ModelAndView toForgetPwd() {
        return new ModelAndView("globalweb/forget_pwd");
	}

	/**
	 * 判断用户名是否存在
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/client/getyhm",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getyhm(String yhm) {
		Map<String, Object> map = new HashMap<>();
		if (!"".equals(yhm)) {// 判断用户名是否为空
			XtyhDto xtyhDto = xtyhService.getDtoByName(yhm);
			boolean isSuccess = (xtyhDto != null);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? null : xxglService.getModelById("ICOMM_MM00001").getXxnr());
		} else {
			map.put("status", "success");
		}
		return map;
	}

	/**
	 * 发送验证码
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/client/sendyzm",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> sendyzm(String yhm) {
		Map<String, Object> map = new HashMap<>();
		// 判断用户名是否为空
		if (!"".equals(yhm)) {
			XtyhDto xtyhDto = xtyhService.getDtoByName(yhm);
			// 判断用户名是否存在
			if (xtyhDto != null) {
				String yzm = RandomUtil.getRandomStringNumber();// 生成验证码
				YhqtxxDto yhqtxxDto = new YhqtxxDto();
				yhqtxxDto.setYhid(xtyhDto.getYhid());
				yhqtxxDto = yhqtxxService.getDto(yhqtxxDto);
				// 判断是否生成过验证码
				if (yhqtxxDto != null) {
					// 数据库数据存在，存入新验证码执行更新
					yhqtxxDto.setYzm(yzm);
					yhqtxxDto.setXgry(yhm);
					boolean iSsuccess = yhqtxxService.update(yhqtxxDto);
					// 更新成功发送验证码通知
					if (iSsuccess) {
						boolean iSsuccesss = dingTalkUtil.sendWorkMessage(xtyhDto.getYhm(), xtyhDto.getDdid(),
								xxglService.getMsg("ICOMM_MM00004", yzm), xxglService.getMsg("ICOMM_MM00005", yhm,yzm));
						// 判断验证码是否发送成功
						map.put("status", iSsuccesss ? "success" : "fail");
						map.put("message", iSsuccesss ? xxglService.getModelById("ICOM99024").getXxnr()
								: xxglService.getModelById("ICOMM_MM00002").getXxnr());
					} else { // 更新失败，返回失败信息
						map.put("status", "fail");
						map.put("message", xxglService.getModelById("ICOMM_MM00002").getXxnr());
					}
				} else {
					// 数据库数据不存在，执行添加操作
					YhqtxxDto yhqtxxDtos = new YhqtxxDto();
					yhqtxxDtos.setYzm(yzm);
					yhqtxxDtos.setLrry(yhm);
					yhqtxxDtos.setYhid(xtyhDto.getYhid());
					boolean iSsuccess = yhqtxxService.insert(yhqtxxDtos);
					if (iSsuccess) {
						// 添加成功，发送验证码通知
						boolean iSsuccesss = dingTalkUtil.sendWorkMessage(xtyhDto.getYhm(), xtyhDto.getDdid(),
								xxglService.getMsg("ICOMM_MM00004", yzm), xxglService.getMsg("ICOMM_MM00005", yhm,yzm));
						// 判断消息是否发送成功
						map.put("status", iSsuccesss ? "success" : "fail");
						map.put("message", iSsuccesss ? xxglService.getModelById("ICOM99024").getXxnr()
								: xxglService.getModelById("ICOMM_MM00002").getXxnr());
					} else {
						// 添加失败返回失败消息
						map.put("status", "fail");
						map.put("message", xxglService.getModelById("ICOMM_MM00002").getXxnr());
					}
				}
			} else { // 不存在，返回失败信息
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOMM_MM00001").getXxnr());
			}
		} else { // 为空，返回失败信息
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_MM00003").getXxnr());
		}
		return map;
	}

	/**
	 * 验证验证码
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/client/verification",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> Verification(YhqtxxDto yhqtxxDto) {
		Map<String, Object> map = new HashMap<>();
		// 判断用户名是否存在
		XtyhDto xtyhDto = xtyhService.getDtoByName(yhqtxxDto.getYhm());
		if (xtyhDto != null) {
			// 用户名存在
			yhqtxxDto.setYhid(xtyhDto.getYhid());
			yhqtxxDto = yhqtxxService.getDtoByYzm(yhqtxxDto);
			// 判断验证码是否正确
			if (yhqtxxDto != null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nowdate = df.format(new Date());
				DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					// 判断验证码发送时常是否超过5分钟
					Date d1 = dfs.parse(nowdate);
					Date d2 = dfs.parse(yhqtxxDto.getYzsj());
					long diff = (d1.getTime() - d2.getTime()) / (1000 * 60);
					boolean iSsuccess = (diff > 5);
					// 超过需要重新验证
					map.put("status", iSsuccess ? "fail" : "success");
					map.put("message", iSsuccess ? xxglService.getModelById("ICOMM_MM00006").getXxnr() : null);
					map.put("xtyhid", iSsuccess ? null : xtyhDto.getYhid());
				} catch (ParseException e) {
					System.out.println("时间日期解析出错。");
					log.error(e.toString());
				}
			} else {
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOMM_MM00006").getXxnr());
			}
		} else {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_MM00007").getXxnr());
		}
		return map;
	}

	/**
	 * 找回密码跳转页面
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/client/toFindPwd")
	public ModelAndView toFindPwd(XtyhDto xtyhDto) {
		if (xtyhDto.getYhid() != null) {
			ModelAndView mav = new ModelAndView("globalweb/pwd_find");
			mav.addObject("xtyhDto", xtyhDto);
			return mav;
		} else {
            return new ModelAndView("globalweb/forget_pwd");
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/client/updatePwd")
	@ResponseBody
	public Map<String, Object> updatePwd(XtyhDto xtyhDto) {
		Map<String, Object> map = new HashMap<>();
		xtyhDto.setXgry(xtyhDto.getYhid());
		boolean isSuccess = xtyhService.modPass(xtyhDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 验证用户名密码，如果正确，则验证token有效期
	 * 
	 * @param request
	 * @return
	 */
	protected Map<String, String> checkLoginInfo(HttpServletRequest request) {
//		Map<String, String> returnMap = null;

		String username = request.getParameter("username");
//		String password = request.getParameter("password");
		String grant_type = request.getParameter("grant_type");

		if (StringUtil.isNotBlank(grant_type) && "password".equals(grant_type)) {
			XtyhDto xtyhDto = xtyhService.getDtoByName(username);

			if (xtyhDto == null) {
                return null;
            }

//			String mmString = xtyhDto.getMm();

//			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			// 密码验证通过，则查找当前token信息
//			if (passwordEncoder.matches(password, mmString)) {
//				// JdbcTokenStore store = new JdbcTokenStore(postsqlDataSource);
//
//			}
		}

		return null;
	}

	/**
	 * 转到正常的spring Security 认证
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> turnToSpringSecurityLogin(HttpServletRequest request) {
		Map<String, String> returnMap = null;
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			List<NameValuePair> pairs = new ArrayList<>();
			pairs.add(new BasicNameValuePair("client_id", username));
			Base64 base64 = new Base64();
			String enPass = base64.encodeToString(password.getBytes());
			pairs.add(new BasicNameValuePair("client_secret", enPass));
			pairs.add(new BasicNameValuePair("grant_type", "matridx"));
			CloseableHttpClient httpclient = null;
			CloseableHttpResponse httpresponse = null;
			try {
				httpclient = HttpClients.createDefault();
				HttpPost httppost = new HttpPost(applicationurl + "/oauth/token");
				// StringEntity stringentity = new StringEntity(data);
				if (!CollectionUtils.isEmpty(pairs)) {
					httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				}
				httpresponse = httpclient.execute(httppost);
				String response = EntityUtils.toString(httpresponse.getEntity());

				returnMap = JSONObject.parseObject(response, Map.class);
				if (returnMap!=null&&returnMap.get("expires_in") != null) {
					String ex_in = String.valueOf(returnMap.get("expires_in"));
					returnMap.remove("expires_in");
					returnMap.put("expires_in", ex_in);
				}
			} finally {
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
		return returnMap;
	}
	@RequestMapping("/initIndex")
	@ResponseBody
	public Map<String, Object> initIndex(XtyhModel xtyhmodel){
        return xtyhService.initMenu(xtyhmodel.getAccess_token());
	}

	@RequestMapping("/initBbxx")
	@ResponseBody
	public Map<String, Object> initBbxx(){
		Map<String, Object> resultMap = new HashMap<>();
		List<BbxxDto> bbxxList = bbxxService.getBbxxDtoList();
		resultMap.put("bbxxlist", bbxxList);
		return resultMap;
	}
	/**
	 * 主页
	 * 
	 * @param xtyhmodel
	 * @return
	 */
	// @PreAuthorize("authenticated and hasPermission('hello', 'view')"):
	// 表示只有当前已登录的并且拥有("hello", "view")权限的用户才能访问此页面
	// SecurityContextHolder.getContext().getAuthentication().getName():
	// 获取当前登录的用户，也可以通过HttpServletRequest.getRemoteUser()获取
	@RequestMapping("/index")
	public ModelAndView toIndex(XtyhModel xtyhmodel) {
		User user = getLoginInfo();
		if (("1").equals(xtyhmodel.getMm_flg())) {
			ModelAndView mav = new ModelAndView("globalweb/pwd_edit");
			xtyhmodel.setYhid(user.getYhid());
			mav.addObject("xtyhmodel", xtyhmodel);
			mav.addObject("title", title);
			mav.addObject("content","您当前的密码强度较低，建议修改密码");
			return mav;
		} else {
			Object passwordExpirationTimeDto = redisUtil.hget("matridx_xtsz", "passwordExpirationTime");
			//默认密码过期时长为180天
			int mmgqsc = 180;
			if(passwordExpirationTimeDto!=null){
				try {
					XtszDto mmxgscXtsz = JSON.parseObject(passwordExpirationTimeDto.toString(),XtszDto.class);
					mmgqsc = Integer.parseInt(mmxgscXtsz.getSzz());
				} catch (NumberFormatException e) {
					log.error("passwordExpirationTime设置错误！采用默认密码过期时长180天！");
				}
			}
			Date nowDate = new Date();
			Date mmxgsj;
			try {
				String s_mmxgsj;
				if(user.getMmxgsj() != null && user.getMmxgsj().length() > 19) {
                    s_mmxgsj = user.getMmxgsj().substring(0,19);
                } else {
                    s_mmxgsj = user.getMmxgsj();
                }
				mmxgsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s_mmxgsj);
			} catch (Exception e) {
				log.error("当前账户："+user.getYhm()+"无密码修改时间或时间转换失败！");
				ModelAndView mav = new ModelAndView("globalweb/pwd_edit");
				xtyhmodel.setYhid(user.getYhid());
				mav.addObject("xtyhmodel", xtyhmodel);
				mav.addObject("title", title);
				mav.addObject("content","未查询到您的密码修改时间，请重新设置密码！");
				return mav;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(mmxgsj);
			calendar.add(Calendar.DATE, mmgqsc);
			Date mmgqsj = calendar.getTime();
			if(mmgqsj.before(nowDate)){
				ModelAndView mav = new ModelAndView("globalweb/pwd_edit");
				xtyhmodel.setYhid(user.getYhid());
				mav.addObject("xtyhmodel", xtyhmodel);
				mav.addObject("title", title);
				mav.addObject("content","您的密码修改时间已超过有效期"+mmgqsc+"天，请重新设置密码！");
				return mav;
			}

			List<BbxxDto> bbxxList = bbxxService.getBbxxDtoList();
			ModelAndView mav = new ModelAndView("globalweb/index");
			mav.addObject("access_token", xtyhmodel.getAccess_token());
			mav.addObject("ref", xtyhmodel.getRef());
			mav.addObject("bbxxlist", bbxxList);

			Map<String, Object> resultMap = xtyhService.initMenu(xtyhmodel.getAccess_token());

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
			mav.addObject("domain",domain);
			return mav;
		}

	}

	/**
	 * 电脑端首页
	 * 
	 * @return
	 */
	@RequestMapping("/web/main/commonPage")
	public ModelAndView DisplayFirstPage() {
		User user = getLoginInfo();
		String sqjs = user.getDqjs();

		XtjsDto xtjsDto = xtjsService.getDtoById(sqjs);

		if (xtjsDto != null && StringUtil.isNotBlank(xtjsDto.getSylxdz())) {
			String sylxdz = xtjsDto.getSylxdz();
			IStatisService statis = (IStatisService) ServiceFactory.getService(sylxdz);
			return statis.getStatisPage();
		} else {
			return new ModelAndView("globalweb/firstpage");
		}
	}

	/**
	 * 手机端首页
	 * 
	 * @return
	 */
	@RequestMapping("/web/main/commonFirstPageOfMobile")
	public ModelAndView DisplayMobileFirstPage() {
		User user = getLoginInfo();
		String sqjs = user.getDqjs();

		XtjsDto xtjsDto = xtjsService.getDtoById(sqjs);

		if (xtjsDto != null && StringUtil.isNotBlank(xtjsDto.getSylxdz())) {
			String sylxdz = xtjsDto.getSylxdz();
			IStatisService statis = (IStatisService) ServiceFactory.getService(sylxdz);
			return statis.getStatisPage();
		} else {
			return new ModelAndView("globalweb/firstpageofmobile");
		}
	}

	/**
	 * 路径命名规则：模块名+页面名+功能名
	 * 
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/web/main/ansySubMenu")
	@ResponseBody
	public List<XtzyDto> DisplaySubMenu(String menuId) {
		User user = getLoginInfo();

		XtzyDto xtzyDto = new XtzyDto();
		xtzyDto.setJsid(user.getDqjs());
		xtzyDto.setZyid(menuId);
        return xtzyService.getSubMenuByMenuId(xtzyDto);
	}
	/**
	 * 切换角色新oa
	 *
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping("/web/main/pagedataSwitchRole_xoa")
	@ResponseBody
	public Map<String, Object> switchRole_xoa(XtyhDto xtyhDto) {

		//Map<String, Object> resultMap = xtyhService.switchMenu(xtyhDto);

		return xtyhService.switchMenu(xtyhDto);
	}
	/**
	 * 切换角色
	 * 
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping("/web/main/pagedataSwitchRole")
	public ModelAndView switchRole(XtyhDto xtyhDto) {

		ModelAndView mav = new ModelAndView("globalweb/index");

		Map<String, Object> resultMap = xtyhService.switchMenu(xtyhDto);

		if (resultMap != null) {
			mav.addObject("flag", "checkRole");

			mav.addObject("topmenu", resultMap.get("topmenu"));

			mav.addObject("mobiletopmenu", resultMap.get("mobiletopmenu"));

			mav.addObject("menuList", resultMap.get("menuList"));

			mav.addObject("jsxxList", resultMap.get("jsxxList"));

			mav.addObject("xtyhDto", resultMap.get("xtyhDto"));

			mav.addObject("access_token", xtyhDto.getAccess_token());
		} else {
			mav = new ModelAndView("globalweb/login");
		}

		return mav;
	}

	/**
	 * 用ajax查询系统角色
	 * 
	 * @return
	 */
	@RequestMapping("/web/main/selectxtjsDto")
	@ResponseBody
	public List<XtjsDto> selectxtjsDto(XtjsDto xtjsDto) {
        return xtjsService.getDtoList(xtjsDto);
	}

	/**
	 * 通过系统角色id获取所有用户
	 * 
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping("/web/main/pagedataYhmByjsid")
	@ResponseBody
	public List<XtyhDto> getyhmByjsid(XtjsDto xtjsDto) {
        return xtjsService.getyhmByjsid(xtjsDto);
	}

	/**
	 * 小程序获取该用户所有角色等信息
	 * 
	 * @param xtyhmodel
	 * @return
	 */
	@RequestMapping("/web/mini/getRole")
	@ResponseBody
	public Map<String, Object> getRole(XtyhModel xtyhmodel) {
        return xtyhService.initMenu(xtyhmodel.getAccess_token());
	}

	/**
	 * 切换角色
	 * 
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping("/web/min/changeRole")
	@ResponseBody
	public Map<String, Object> changeRole(XtyhDto xtyhDto) {
        return xtyhService.switchMenu(xtyhDto);
	}
	
	/**
	 * 利用框架检测token是否有效
	 * 
	 * @return
	 */
	@RequestMapping("/checkToken")
	@ResponseBody
	public String checkToken() {
		log.error("checkToken");
		return "OK";
	}

	/**
	 * 获取版本信息
	 */
	@RequestMapping("/pagedataBbxxInIndex")
	@ResponseBody
	public Map<String,Object> getBbxxInIndex(){
		Map<String,Object> map = new HashMap<>();
		List<BbxxDto> bbxxDtoList = bbxxService.getBbxxDtoList();
		List<BbxxDto> list = new ArrayList<>();
		if (!CollectionUtils.isEmpty(bbxxDtoList)){
			for (int i = 0; i < bbxxDtoList.size(); i++) {
				if (i<3){
					list.add(bbxxDtoList.get(i));
				}
			}
		}
		map.put("bbxxList",list);
		return map;
	}

}
