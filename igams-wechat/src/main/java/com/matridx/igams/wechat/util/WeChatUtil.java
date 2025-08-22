package com.matridx.igams.wechat.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.wechat.dao.entities.AccessTokenModel;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.WeChatGetTagModel;
import com.matridx.igams.wechat.dao.entities.WeChatReturnModel;
import com.matridx.igams.wechat.dao.entities.WeChatTagModel;
import com.matridx.igams.wechat.dao.entities.WeChatTagUserModel;
import com.matridx.igams.wechat.dao.entities.WxcdDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.BqglDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

public class WeChatUtil {
	
	private static String access_token = null;
	
	private static String expires_in = null;
	
	private static Date lastGetDate = null;
	
	private static long refreshDate = 300;
	
	//微信获取token地址
	private final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	//微信获取用户信息
	private final static String USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
	//微信批量获取用户信息
	//private final static String BASHUSER_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget";
	//微信创建菜单
//	private final static String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//微信创建标签
	private final static String CREATE_TAG = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=ACCESS_TOKEN";
	//微信编辑标签
	private final static String UPDATE_TAG = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=ACCESS_TOKEN";
	//微信删除标签
	private final static String DELETE_TAG = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=ACCESS_TOKEN";
	//获取微信标签下用户
	private final static String GET_TAGUSER = "https://api.weixin.qq.com/cgi-bin/user/tag/get?access_token=ACCESS_TOKEN";
	//获取微信已创建标签
	private final static String GET_TAG = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=ACCESS_TOKEN";
	
	
	private static Logger log = LoggerFactory.getLogger(WeChatUtil.class);
	
	//微信获取token地址错误代码
	private final static String SUCCESS_CODE = "0";
	
	/**
	 * 获取token信息，如果还未设置获取快要过期，则更新token。
	 * @param restTemplate
	 * @param grant_type
	 * @param appid
	 * @param secret
	 * @return
	 */
	public synchronized static String getToken(RestTemplate restTemplate,String grant_type,String appid,String secret){
		if(StringUtil.isBlank(access_token)){
			getNewToken(restTemplate,grant_type,appid,secret);
		}else if(lastGetDate.getTime() + Long.parseLong(expires_in) +  refreshDate >= new Date().getTime()){
			getNewToken(restTemplate,grant_type,appid,secret);
		}
		return access_token;
	}
	/**
	 * 获取新的token
	 * @param restTemplate
	 * @param grant_type
	 * @param appid
	 * @param secret
	 */
	private static void getNewToken(RestTemplate restTemplate,String grant_type,String appid,String secret){
		String url = new StringBuffer(TOKEN_URL).append("?grant_type=client_credential&appid=").append(appid)
				.append("&secret=").append(secret).toString();
		AccessTokenModel accessTokenModel = restTemplate.getForObject(url, AccessTokenModel.class);
		lastGetDate = new Date();
		access_token = accessTokenModel.getAccess_token();
		expires_in = accessTokenModel.getExpires_in();
	}
	
	/**
	 * 获取微信用户信息
	 * @param restTemplate
	 * @param openId
	 * @return
	 */
	public static String getUserInfo(RestTemplate restTemplate,String openId){
		
		String url = new StringBuffer(USER_URL).append("?access_token=").append(access_token)
				.append("&openid=").append(openId).append("&lang=zh_CN").toString();
		String userList = restTemplate.getForObject(url, String.class);
		try{

            /*
			 * String url2 = new
			 * StringBuffer("https://api.weixin.qq.com/cgi-bin/user/get").append(
			 * "?access_token=").append(access_token) .toString(); String userList2 =
			 * restTemplate.getForObject(url2, String.class); String json2 = new
			 * String(userList2.getBytes("ISO-8859-1"), "UTF-8");
			 */
			return new String(userList.getBytes("ISO-8859-1"), "UTF-8");
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return userList;
	}
	
	/**
	 * 向阿里云服务器发送菜单信息
	 * @param restTemplate
	 * @param wxcdTreeList
	 * @return
	 */
	public static boolean createMenu(String url, RestTemplate restTemplate,List<WxcdDto> wxcdTreeList){
		try{
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("wxcdTreeList", wxcdTreeList);
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DBEncrypt crypt = new DBEncrypt();
			String sign = crypt.eCode(df.format(date));
			paramMap.add("sign", sign);
			restTemplate.postForObject(url, paramMap, WeChatReturnModel.class);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取阿里服务器微信用户信息
	 * @param url
	 * @param restTemplate
	 * @return
	 */
	public static List<WxyhDto> getUserList(String url, RestTemplate restTemplate, WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		String lrsj = wxyhDto.getLrsj();
		paramMap.add("lrsj", lrsj);
		String s_userinfo = restTemplate.postForObject(url, paramMap, String.class);
		if(StringUtil.isNotBlank(s_userinfo)) {
			@SuppressWarnings("unchecked")
			Map<String, List<WxyhDto>> map = JSONObject.parseObject(s_userinfo,Map.class);
            return map.get("wxyhlist");
		}
		return null;
	}
	
	/**
	 * 向阿里云服务器发送医院信息
	 * @param restTemplate
	 * @param sjdwTreeList
	 * @return
	 */
	public static boolean createHis(String url, RestTemplate restTemplate,List<SjdwxxDto> sjdwTreeList){
		try{
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("sjdwTreeList", sjdwTreeList);
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DBEncrypt crypt = new DBEncrypt();
			String sign = crypt.eCode(df.format(date));
			paramMap.add("sign", sign);
			WeChatReturnModel returnModel = restTemplate.postForObject(url, paramMap, WeChatReturnModel.class);
			if(SUCCESS_CODE.equals(returnModel.getErrcode())) {
                return true;
            }
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 因XSS的原因把URL里的&改为了＆，#改为了＃，需要对其进行修正
	 * @param url
	 * @return
	 */
	public String changeURLCode(String url) {
		if(StringUtil.isBlank(url)) {
            return url;
        }
		url = url.replaceAll("＆", "&");
		url = url.replaceAll("＃", "#");
		return url;
	}
	
	/**
	 * 创建微信用户标签信息
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static BqglDto createTag(BqglDto bqglDto, RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(access_token)) {
            return null;
        }
		String url = CREATE_TAG.replace("ACCESS_TOKEN", access_token);
		WeChatTagModel tagModel = new WeChatTagModel();
		tagModel.setName(bqglDto.getBqm());
		String s_taginfo = restTemplate.postForObject(url, tagModel, String.class);
		String json = null;
		try {
			json = new String(s_taginfo.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtil.isNotBlank(json)) {
			WeChatTagModel taginfo = JSONObject.parseObject(json,WeChatTagModel.class);
			
			if(StringUtil.isNotBlank(taginfo.getErrcode()) && !"0".equals(taginfo.getErrcode())) {
                return null;
            }
			bqglDto.setBqid(taginfo.getId());
			bqglDto.setBqm(taginfo.getName());
		}
		return bqglDto;
	}
	/**
	 * 编辑微信用户标签信息
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static boolean updateTag(BqglDto bqglDto, RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(access_token)) {
            return false;
        }
		String url = UPDATE_TAG.replace("ACCESS_TOKEN", access_token);
		WeChatTagModel tagModel = new WeChatTagModel();
		tagModel.setName(bqglDto.getBqm());
		tagModel.setId(bqglDto.getBqid());
		WeChatReturnModel returnModel = restTemplate.postForObject(url, tagModel, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	/**
	 * 删除微信用户标签信息
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static boolean deleteTag(BqglDto bqglDto, RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(access_token)) {
            return false;
        }
		String url = DELETE_TAG.replace("ACCESS_TOKEN", access_token);
		WeChatTagModel tagModel = new WeChatTagModel();
		tagModel.setId(bqglDto.getBqid());
		WeChatReturnModel returnModel = restTemplate.postForObject(url, tagModel, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	/**
	 * 获取已创建标签信息
	 * @param restTemplate
	 */
	public static List<WeChatTagModel> getTag(RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(access_token)) {
            return null;
        }
		String url = GET_TAG.replace("ACCESS_TOKEN", access_token);
		String s_taginfo = restTemplate.getForObject(url, String.class, "");
		String json = null;
		try {
			json = new String(s_taginfo.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtil.isNotBlank(json)) {
			WeChatGetTagModel taginfo = JSONObject.parseObject(json,WeChatGetTagModel.class);
            return taginfo.getTags();
		}
		return null;
	}
	
	/**
	 * 获取标签下粉丝信息
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static WeChatTagUserModel getTagUser(BqglDto bqglDto, RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(access_token)) {
            return null;
        }
		String url = GET_TAGUSER.replace("ACCESS_TOKEN", access_token);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("tagid", bqglDto.getBqid());
		paramMap.add("next_openid", "");
		String s_taginfo = restTemplate.getForObject(url, String.class, paramMap);
		String json = null;
		try {
			json = new String(s_taginfo.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtil.isNotBlank(json)) {
			WeChatTagUserModel taginfo = JSONObject.parseObject(json,WeChatTagUserModel.class);
			
			if(StringUtil.isNotBlank(taginfo.getErrcode()) && !"0".equals(taginfo.getErrcode())) {
                return null;
            }
			return taginfo;
		}
		return null;
	}
	
}
