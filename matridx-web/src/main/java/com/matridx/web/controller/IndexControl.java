package com.matridx.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IStatisService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.XtyhModel;
import com.matridx.web.dao.entities.XtzyDto;
import com.matridx.web.service.svcinterface.IXtjsService;
import com.matridx.web.service.svcinterface.IXtyhService;
import com.matridx.web.service.svcinterface.IXtzyService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexControl extends BaseController{
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	IXtyhService xtyhService;
	
	@Autowired
	IXtjsService xtjsService;
	
	@Autowired
	IXtzyService xtzyService;

	@Value("${matridx.systemflg.icourl:/images/favicon.ico}")
	private String icourl;
	@Value("${matridx.systemflg.title:杰毅生物}")
	private String title;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.systemflg.logourl:}")
	private String logourl;
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
	private Logger log = LoggerFactory.getLogger(IndexControl.class);

	@RequestMapping("/")
	public ModelAndView toLogin(){
		DateFormat df=new SimpleDateFormat("yyyyMdd");
		String key = df.format(new Date());
		
		ModelAndView mav = new ModelAndView("globalweb/login");
		mav.addObject("key", key);
		mav.addObject("icourl", icourl);
		mav.addObject("title", title);
		mav.addObject("logourl", logourl);
		mav.addObject("applicationurl", applicationurl);
		mav.addObject("domain",domain);
		mav.addObject("cookiekey",cookiekey);
		return mav;
	}
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, String redirectUrl) {
		ModelAndView mav = new ModelAndView("globalweb/login");
		mav.addObject("logourl", logourl);
		mav.addObject("domain",domain);
		mav.addObject("cookiekey",cookiekey);
		mav.addObject("applicationurl", applicationurl);
		mav.addObject("redirectUrl",redirectUrl);
		String n_titleString = request.getParameter("title");
		mav.addObject("title", StringUtil.isBlank(n_titleString)?title:n_titleString);
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
	 * 主页
	 * @param xtyhmodel
	 * @return
	 */
	//@PreAuthorize("authenticated and hasPermission('hello', 'view')"): 表示只有当前已登录的并且拥有("hello", "view")权限的用户才能访问此页面
	//SecurityContextHolder.getContext().getAuthentication().getName(): 获取当前登录的用户，也可以通过HttpServletRequest.getRemoteUser()获取
	@RequestMapping("/index")
	public ModelAndView toIndex(XtyhModel xtyhmodel){
		User user = getLoginInfo();
		if ("1".equals(xtyhmodel.getMm_flg())) {
			ModelAndView mav = new ModelAndView("globalweb/pwd_edit");
			xtyhmodel.setYhid(user.getYhid());
			mav.addObject("xtyhmodel", xtyhmodel);
			mav.addObject("title", title);
			mav.addObject("content","您当前的密码强度较低，建议修改密码");
			return mav;
		}else {
			Object passwordExpirationTimeDto = redisUtil.hget("matridx_xtsz", "passwordExpirationTime");
			//默认密码过期时长为180天
			int mmgqsc = 180;
			if (passwordExpirationTimeDto != null) {
				try {
					XtszDto mmxgscXtsz = JSON.parseObject(passwordExpirationTimeDto.toString(), XtszDto.class);
					mmgqsc = Integer.parseInt(mmxgscXtsz.getSzz());
				} catch (NumberFormatException e) {
					log.error("passwordExpirationTime设置错误！采用默认密码过期时长180天！");
				}
			}
			Date nowDate = new Date();
			Date mmxgsj = null;
			try {
				mmxgsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(user.getMmxgsj());
			} catch (Exception e) {
				log.error("当前账户：" + user.getYhm() + "无密码修改时间或时间转换失败！");
				ModelAndView mav = new ModelAndView("globalweb/pwd_edit");
				xtyhmodel.setYhid(user.getYhid());
				mav.addObject("xtyhmodel", xtyhmodel);
				mav.addObject("title", title);
				mav.addObject("content", "未查询到您的密码修改时间，请重新设置密码！");
				return mav;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(mmxgsj);
			calendar.add(Calendar.DATE, mmgqsc);
			Date mmgqsj = calendar.getTime();
			if (mmgqsj.before(nowDate)) {
				ModelAndView mav = new ModelAndView("globalweb/pwd_edit");
				xtyhmodel.setYhid(user.getYhid());
				mav.addObject("xtyhmodel", xtyhmodel);
				mav.addObject("title", title);
				mav.addObject("content", "您的密码修改时间已超过有效期" + mmgqsc + "天，请重新设置密码！");
				return mav;
			}
			ModelAndView mav = new ModelAndView("globalweb/index");
			mav.addObject("access_token", xtyhmodel.getAccess_token());
			mav.addObject("ref", xtyhmodel.getRef());

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

	@RequestMapping("/initIndex")
	@ResponseBody
	public Map<String, Object> initIndex(XtyhModel xtyhmodel){
		Map<String, Object> resultMap = xtyhService.initMenu(xtyhmodel.getAccess_token());
		return resultMap;
	}

	@RequestMapping("/toIndexView")
	@ResponseBody
	public Map<String,Object> toIndexView(XtyhModel xtyhmodel){
		Map<String,Object> map = new HashMap<>();
		map.put("access_token", xtyhmodel.getAccess_token());
		map.put("ref", xtyhmodel.getRef());

		Map<String, Object> resultMap = xtyhService.initMenu(xtyhmodel.getAccess_token());

		if(resultMap != null){
			map.put("topmenu", resultMap.get("topmenu"));

			map.put("mobiletopmenu", resultMap.get("mobiletopmenu"));

			map.put("menuList", resultMap.get("menuList"));

			map.put("jsxxList", resultMap.get("jsxxList"));

			map.put("xtyhDto", resultMap.get("xtyhDto"));
			map.put("url","globalweb/index");
		}else{
			map.put("url","globalweb/login");
		}

		return map;
	}
	
	/**
	 * 电脑端首页
	 * @return
	 */
	@RequestMapping("/web/main/commonPage")
	public ModelAndView DisplayFirstPage(){
		
		User user = getLoginInfo();
		String sqjs = user.getDqjs();
		
		XtjsDto xtjsDto = xtjsService.getDtoById(sqjs);
		
		if(xtjsDto!=null && StringUtil.isNotBlank(xtjsDto.getSylxdz())){
			String sylxdz = xtjsDto.getSylxdz();
			IStatisService statis = (IStatisService)ServiceFactory.getService(sylxdz);
			return statis.getStatisPage();
		}else{
			return new ModelAndView("globalweb/firstpage");
		}
	}
	
	/**
	 * 手机端首页
	 * @return
	 */
	@RequestMapping("/web/main/commonFirstPageOfMobile")
	public String DisplayMobileFirstPage(){
		return "globalweb/firstpageofmobile";
	}
	
	/**
	 * 路径命名规则：模块名+页面名+功能名
	 * @param menuId
	 * @return
	 */
	@RequestMapping("/web/main/ansySubMenu")
	@ResponseBody
	public List<XtzyDto> DisplaySubMenu(String menuId){
		User user = getLoginInfo();

		XtzyDto xtzyDto = new XtzyDto();
		xtzyDto.setJsid(user.getDqjs());
		xtzyDto.setZyid(menuId);
		List<XtzyDto> list = xtzyService.getSubMenuByMenuId(xtzyDto);
		return list;
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
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping("/web/main/pagedataSwitchRole")
	public ModelAndView switchRole(XtyhDto xtyhDto){

		ModelAndView mav = new ModelAndView("globalweb/index");

		Map<String, Object> resultMap = xtyhService.switchMenu(xtyhDto);

		if(resultMap != null){
			mav.addObject("flag", "checkRole");

			mav.addObject("topmenu", resultMap.get("topmenu"));

			mav.addObject("mobiletopmenu", resultMap.get("mobiletopmenu"));

			mav.addObject("menuList", resultMap.get("menuList"));

			mav.addObject("jsxxList", resultMap.get("jsxxList"));

			mav.addObject("xtyhDto", resultMap.get("xtyhDto"));

			mav.addObject("access_token", xtyhDto.getAccess_token());
		}else{
			mav = new ModelAndView("globalweb/login");
		}

		return mav;
	}
	/**
	* @Description: C#端获取Linux Token
	* @param request
	* @return java.util.Map<java.lang.String,java.lang.String>
	* @Author: 郭祥杰
	* @Date: 2024/5/16 14:26
	*/
	@RequestMapping("/oauth/externaltoken")
	@ResponseBody
	public Map<String, String> externaltoken(HttpServletRequest request) {
		Map<String, String> returnMap = null;
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			List<NameValuePair> pairs = new ArrayList<>();
			pairs.add(new BasicNameValuePair("client_id", username));
			org.apache.commons.codec.binary.Base64 base64 = new Base64();
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
	
	/**
	 * 利用框架检测token是否有效
	 * 
	 * @return
	 */
	@RequestMapping("/checkToken")
	@ResponseBody
	public String checkToken() {
		//log.error("checkToken");
		return "OK";
	}
}
