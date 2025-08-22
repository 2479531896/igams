package com.matridx.server.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.server.wechat.dao.entities.AccessTokenModel;
import com.matridx.server.wechat.dao.entities.BqglDto;
import com.matridx.server.wechat.dao.entities.JSTicketReturnModel;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatGetTagModel;
import com.matridx.server.wechat.dao.entities.WeChatMaterialModel;
import com.matridx.server.wechat.dao.entities.WeChatMenuModel;
import com.matridx.server.wechat.dao.entities.WeChatReturnModel;
import com.matridx.server.wechat.dao.entities.WeChatSendDetailModel;
import com.matridx.server.wechat.dao.entities.WeChatSendModel;
import com.matridx.server.wechat.dao.entities.WeChatTagDto;
import com.matridx.server.wechat.dao.entities.WeChatTagModel;
import com.matridx.server.wechat.dao.entities.WeChatTagUserModel;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * WeChatUtil  杰毅检验的公众号  
 * WeChatMatridxUtil  杰毅生物的公众号
 * @author lwj
 *
 */
@Component
public class WeChatUtils {
	

	
	//微信获取token地址
	private final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	//微信获取用户信息
	private final static String USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info";
	//微信批量获取用户信息
	//private final static String BASHUSER_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget";
	//微信获取用户列表
	private final static String USER_OPENID_LIST = "https://api.weixin.qq.com/cgi-bin/user/get";
	//微信创建菜单
	private final static String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//微信删除菜单
	private final static String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	//微信创建个性化菜单
	private final static String CREATE_MATCHRULEMENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token=ACCESS_TOKEN";
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
	//批量设置用户标签
	private final static String SET_TAG = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=ACCESS_TOKEN";
	//批量取消用户标签
	private final static String CANCLE_TAG = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=ACCESS_TOKEN";
	//获取jsapi_ticket，是公众号用于调用微信JS接口的临时票据
	private final static String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	//发送微信通知
	private final static String SEND_TAG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	//获取请求用户信息的access_tokenF
	private final static String USER_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";	
	//根据用户信息的access_token获取用户信息
	//private final static String USERINFO_BYTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	//微信获取token地址错误代码
	private final static String SUCCESS_CODE = "0";
	//获取用户同意授权
	private final static String SNSAPI_USERINFO = "https://open.weixin.qq.com/connect/oauth2/authorize";
	//获取用户信息
	private final static String SNS_USERINFO = "https://api.weixin.qq.com/sns/userinfo";
	// 生成临时二维码路径
	private final static String QR_CODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	// 查看二维码
//	private final static String SHOW_QR_CODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	// 临时二维码  
//	private final static String QR_SCENE = "QR_SCENE";
	// 临时二维码  
	private final static String QR_STR_SCENE = "QR_STR_SCENE";
	// 永久二维码  
//	private final static String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";  
	// 永久二维码(字符串)  
//	private final static String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";
	
	//发送客服消息
	private final static String SEND_CUSTOM = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	//群发消息接口
	private final static String SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	//上传永久素材
	private final static String ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
	
	final static Logger log = LoggerFactory.getLogger(WeChatUtils.class);



	/**
	 * 获取用户同意授权
	 * @return
	 * @throws BusinessException
	 */
	public static String authorization(String appid,String wxid,String applicationurl) throws BusinessException{
		try{
			DBEncrypt dbEncrypt = new DBEncrypt();
			String wechatAppid = dbEncrypt.dCode(appid);
			String addr = dbEncrypt.dCode(applicationurl);
			String redirect_uri =addr+"/wechat/detectionChoose?wxid="+wxid;
			String access_url = new StringBuffer(SNSAPI_USERINFO).append("?appid=").append(wechatAppid)
					.append("&redirect_uri=").append(URLEncoder.encode(redirect_uri,"utf-8")).append("&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect").toString();
			log.error(" redirect_uri"+access_url);
			return access_url;
		}catch(Exception e){
			log.error(e.toString());
			throw new BusinessException("",e.toString());
		}
	}

	/**
	 * 获取accessToken
	 * @return
	 * @throws BusinessException
	 */
	public static String getAccessToken(RestTemplate restTemplate,String appid,String secret,String code,RedisUtil redisUtil) throws BusinessException{
		try{
			DBEncrypt dbEncrypt = new DBEncrypt();
			String wechatAppid = dbEncrypt.dCode(appid);
			String wechatSecret = dbEncrypt.dCode(secret);
			String access_url = new StringBuffer(USER_TOKEN).append("?appid=").append(wechatAppid)
					.append("&secret=").append(wechatSecret).append("&code=").append(code).append("&grant_type=authorization_code").toString();
			log.error(" access_url:"+access_url);
			String access_url_return = restTemplate.getForObject(access_url.toString(), String.class);
			log.error(" access_url_return:"+access_url_return);

			JSONObject jsonObject = JSONObject.parseObject(access_url_return);
			if (StringUtil.isNotBlank(jsonObject.getString("access_token"))){
				log.error(" access_token"+jsonObject.getString("access_token"));
				String sns_uer_info = new StringBuffer(SNS_USERINFO).append("?access_token=").append(jsonObject.getString("access_token"))
						.append("&openid=").append(jsonObject.getString("openid")).append("&lang=zh_CN").toString();
				String sns_uer_info_return = restTemplate.getForObject(sns_uer_info.toString(), String.class);
				log.error(" sns_uer_info_return"+sns_uer_info_return);
				return sns_uer_info_return;
			}
		}catch(Exception e){
			log.error(e.toString());
			throw new BusinessException("",e.toString());
		}
		return "";
	}
	
	/**
	 * 根据isRefreshFlg 来判断是更新token 还是，获取token，之所以用同一个方法，是为了同步限制的原因
	 * @param restTemplate 访问微信
	 * @param grant_type
	 * @param gzhid
	 * @param appid 微信的appid
	 * @param secret 微信的secret
	 * @param isRefreshFlg 是否更新token
	 * @param wbcxdm 外部查询代码，判断是哪一个微信
	 * @return
	 */
	public static String getToken(RestTemplate restTemplate, String grant_type, String gzhid, String appid, String secret, boolean isRefreshFlg, String wbcxdm,RedisUtil redisUtil){
		try {
			if(isRefreshFlg) {
				getWechatToken(restTemplate,grant_type,appid,secret,wbcxdm,redisUtil);
			}

			return redisUtil.get("WECHAT_TOKE_"+wbcxdm).toString();
		}catch(Exception e) {
			log.error("更新token失败。" + e.getMessage());
		}
		return null;
	}

	/**
	 * 微信获取token
	 * @param restTemplate
	 * @param grant_type 不传则使用默认值
	 * @param appid 不传则根据wbcxdm获取
	 * @param secret 不传则根据wbcxdm获取
	 * @param wbcxdm 必传
	 * @param redisUtil
	 * @return
	 */
	private synchronized static String getWechatToken(RestTemplate restTemplate,String grant_type, String appid, String secret, String wbcxdm,RedisUtil redisUtil){
		if (StringUtil.isBlank(wbcxdm)){
			return "";
		}
		if (StringUtil.isBlank(grant_type)){
			grant_type="client_credential";
		}
		if (StringUtil.isAnyBlank(appid,secret)){
			DBEncrypt dbEncrypt = new DBEncrypt();
			Map<Object, Object> wbcxInfoMap = redisUtil.hmget("WbcxInfo");
			for(Object obj : wbcxInfoMap.values()) {
				com.alibaba.fastjson.JSONObject wbcxInfo = JSON.parseObject(obj.toString());
				if (wbcxInfo!=null){
					String lx = wbcxInfo.getString("lx");
					if (CharacterEnum.WECHAT.getCode().equals(lx)){
						String dm = wbcxInfo.getString("wbcxdm");
						if (StringUtil.isBlank(dm) && dm.equals(wbcxdm)){
							appid = dbEncrypt.dCode(wbcxInfo.getString("appid"));
							secret = dbEncrypt.dCode(wbcxInfo.getString("secret"));
							break;
						}
					}
				}
			}
		}
		String url = new StringBuffer(TOKEN_URL).append("?grant_type=").append(grant_type).append("&appid=").append(appid).append("&secret=").append(secret).toString();
		int freshCnt = 1;
		boolean isFreshSuccess = false;
		while(!isFreshSuccess && freshCnt <= 3) {
			AccessTokenModel accessTokenModel = restTemplate.getForObject(url, AccessTokenModel.class);
			String accessToken = accessTokenModel.getAccess_token();
			if(StringUtil.isNotBlank(accessToken)) {
				redisUtil.set("WECHAT_TOKE_"+wbcxdm,accessToken);
				log.error("getWechatToken ： wbcxdm= " + wbcxdm +  "   accesstoken=" + accessToken);
				String ticket_url = JSAPI_TICKET.replace("ACCESS_TOKEN", accessToken);
				JSTicketReturnModel ticketModel = restTemplate.getForObject(ticket_url, JSTicketReturnModel.class);
				String jsTicket = ticketModel.getTicket();
				redisUtil.set("JS_TICKET_"+wbcxdm,jsTicket);
				return accessToken;
			}else {
				log.error("getWechatToken ： wbcxdm= " + wbcxdm +  "-----accesstoken 更新失败-----url=" + url);
			}
			freshCnt ++;
		}
		return "";
	}

	/**
	 * 返回token信息
	 * @return
	 */
	public static String getTokenInfo(String wbcxdm,RedisUtil redisUtil) {


		return redisUtil.get("WECHAT_TOKE_"+wbcxdm).toString();
	}
	
	/**
	 * 返回JS的临时票据
	 * @return
	 */
	public static String getTicket(String wbcxdm,RedisUtil redisUtil) {

		return redisUtil.get("JS_TICKET_"+wbcxdm).toString();
	}
	
	/**
	 * 对访问的安全进行时间上的确认
	 * @param sign
	 * @return
	 */
	public static boolean checkSign(String sign,RedisUtil redisUtil) {
		DBEncrypt dbEncrypt = new DBEncrypt();
		String s_dateString = dbEncrypt.dCode(sign);
		log.error("s_dateString=" + s_dateString);
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date param_date = df.parse(s_dateString);
			log.error("param_date=" + param_date);
			Date noe_dateDate = new Date();
			log.error("noe_dateDate=" + noe_dateDate);
			// 获得两个时间的毫秒时间差异
		    long diff = noe_dateDate.getTime() - param_date.getTime();
		    log.error("diff=" + diff);
            return diff >= -300000 && diff <= 300000;
		}catch(Exception e) {
			log.error(e.toString());
			log.error("认证日期信息有问题！sign=" + sign);
		}
		return false;
	}
	
	/**
	 * 因XSS的原因把URL里的&改为了＆，#改为了＃，需要对其进行修正
	 * @param url
	 * @return
	 */
	public static String changeURLCode(String url,RedisUtil redisUtil) {
		if(StringUtil.isBlank(url)) {
            return url;
        }
		url = url.replaceAll("＆", "&");
		url = url.replaceAll("＃", "#");
		return url;
	}
	
	/**
	 * 向微信推送菜单信息
	 * @param restTemplate
	 * @param menu
	 * @return
	 */
	public static boolean createMenu(RestTemplate restTemplate,WeChatMenuModel menu, String wbcxdm,RedisUtil redisUtil){
		try{
			log.info("createmenu:"+menu.toString());

			Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
			if(obj==null) {
                return false;
            }
			
			String delurl = DELETE_MENU_URL.replace("ACCESS_TOKEN", obj.toString());
			log.info("delurl:"+delurl);
			WeChatReturnModel delreturnModel = restTemplate.getForObject(delurl, WeChatReturnModel.class);
			log.info("delErrcode:"+delreturnModel.getErrcode()+"  delErrMsg:"+delreturnModel.getErrmsg());
			
			String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", obj.toString());
			log.info("url:"+url);
			WeChatReturnModel returnModel = restTemplate.postForObject(url, menu, WeChatReturnModel.class);
			log.info("Errcode:"+returnModel.getErrcode()+"  ErrMsg:"+returnModel.getErrmsg());
			if(SUCCESS_CODE.equals(returnModel.getErrcode())) {
                return true;
            }
		}catch(Exception e){
			log.error(e.toString());
			return false;
		}
		return false;
	}
	
	/**
	 * 向微信推送个性菜单信息
	 * @param restTemplate
	 * @param menu
	 * @return
	 */
	public static boolean createMatchruleMenu(RestTemplate restTemplate,WeChatMenuModel menu, String wbcxdm,RedisUtil redisUtil){
		try{
			log.info("createMatchrulemenu:"+menu.toString());

			Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
			if(obj==null) {
                return false;
            }
			
			String url = CREATE_MATCHRULEMENU_URL.replace("ACCESS_TOKEN",obj.toString());
			log.info("url:"+url);
			WeChatReturnModel returnModel = restTemplate.postForObject(url, menu, WeChatReturnModel.class);
			log.info("Errcode:"+returnModel.getErrcode()+"  ErrMsg:"+returnModel.getErrmsg());
			if(returnModel.getErrcode() == null) {
                return true;
            }
		}catch(Exception e){
			log.error(e.toString());
			return false;
		}
		return false;
	}
	
	/**
	 * 删除菜单(全部)
	 * @param restTemplate
	 * @return
	 */
	public static boolean deleteMenu(RestTemplate restTemplate, String wbcxdm,RedisUtil redisUtil){

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String delurl = DELETE_MENU_URL.replace("ACCESS_TOKEN", obj.toString());
		log.info("delurl:"+delurl);
		WeChatReturnModel delreturnModel = restTemplate.getForObject(delurl, WeChatReturnModel.class);
		log.info("delErrcode:"+delreturnModel.getErrcode()+"  delErrMsg:"+delreturnModel.getErrmsg());
        return StringUtil.isNotBlank(delreturnModel.getErrcode()) && Integer.valueOf(delreturnModel.getErrcode()) == 0;
    }
	
	/**
	 * 创建微信用户标签信息
	 * @param wxcdDto
	 * @return
	 */
	public static BqglDto createTag(BqglDto bqglDto, RestTemplate restTemplate,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = bqglDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);

		if(obj==null) {
            return null;
        }
		String url = CREATE_TAG.replace("ACCESS_TOKEN", obj.toString());
		WeChatTagModel tagModel = new WeChatTagModel();
		tagModel.setName(bqglDto.getBqm());
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("tag", tagModel);
		String s_taginfo = restTemplate.postForObject(url, paraMap, String.class);
		String json = null;
		try {
			json = new String(s_taginfo.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		if(StringUtil.isNotBlank(json)) {
			WeChatTagDto weChatTag = JSONObject.parseObject(json,WeChatTagDto.class);
			WeChatTagModel taginfo = weChatTag.getTag();
			if(StringUtil.isNotBlank(weChatTag.getErrcode())) {
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
	public static boolean updateTag(BqglDto bqglDto, RestTemplate restTemplate,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = bqglDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = UPDATE_TAG.replace("ACCESS_TOKEN",obj.toString());
		WeChatTagModel tagModel = new WeChatTagModel();
		tagModel.setName(bqglDto.getBqm());
		tagModel.setId(bqglDto.getBqid());
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("tag", tagModel);
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	
	/**
	 * 删除微信用户标签信息
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static boolean deleteTag(BqglDto bqglDto, RestTemplate restTemplate,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = bqglDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = DELETE_TAG.replace("ACCESS_TOKEN", obj.toString());
		WeChatTagModel tagModel = new WeChatTagModel();
		tagModel.setId(bqglDto.getBqid());
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("tag", tagModel);
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	
	/**
	 * 获取已创建标签信息
	 * @param bqglDto
	 * @param restTemplate
	 */
	public static List<WeChatTagModel> getTag(RestTemplate restTemplate, BqglDto bqglDto,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = bqglDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		String url = GET_TAG.replace("ACCESS_TOKEN", obj.toString());
		String s_taginfo = restTemplate.getForObject(url, String.class, "");
		String json = null;
		try {
			log.error("getTag 未转化文字串：" + s_taginfo);
			json = new String(s_taginfo.getBytes("ISO-8859-1"), "UTF-8");
			log.error("getTag 已转化文字串：" + json);
			json = s_taginfo;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
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
	public static WeChatTagUserModel getTagUser(BqglDto bqglDto, RestTemplate restTemplate,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = bqglDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		String url = GET_TAGUSER.replace("ACCESS_TOKEN", obj.toString());
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("tagid", Integer.valueOf(bqglDto.getBqid()));
		paraMap.put("next_openid", "");
		String s_taginfo = restTemplate.postForObject(url,paraMap,String.class);
		String json = null;
		try {
			json = new String(s_taginfo.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
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
	
	
	/**
	 * 获取现有已关注的用户列表，同时获取相应的用户信息后保存到数据库里
	 * @param restTemplate
	 * @param wbcxdm
	 * @return
	 */
	public static List<WeChatUserModel> getUserList(RestTemplate restTemplate, String wbcxdm,RedisUtil redisUtil){

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		
		try{
			List<WeChatUserModel> resultList = new ArrayList<>();
			int maxCount = 0;
			String next_openid = null;
			while(true) {
				maxCount ++;
				if(maxCount > 20) {
                    break;
                }
				//https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID
				StringBuffer url = new StringBuffer(USER_OPENID_LIST).append("?access_token=").append(obj.toString());
				if(StringUtil.isNotBlank(next_openid)) {
                    url.append("&next_openid=").append(next_openid);
                }
				String s_userlist = restTemplate.getForObject(url.toString(), String.class);
				
				JSONObject o_userlist= JSONObject.parseObject(s_userlist);
				// 错误返回 {"errcode":40029,"errmsg":"invalid code"}
				//有错误直接返回
				if(o_userlist.get("errcode") != null) {
					throw new BusinessException("","未获取到用户信息，错误代码为：" + o_userlist.get("errcode"));
				}
				
				JSONArray userList = (JSONArray)o_userlist.getJSONObject("data").getJSONArray("openid");
				if(userList !=null && userList.size() > 0)
				{
					for(int i =0 ;i< userList.size();i++) {
						WeChatUserModel userinfo = getUserInfo(restTemplate, userList.getString(i), wbcxdm,redisUtil);
						resultList.add(userinfo);
					}
				}
				
				next_openid = (String)o_userlist.get("next_openid");
				
				if(o_userlist.getIntValue("count")<10000) {
                    break;
                }
			}
			return resultList;

		}catch(Exception e){
			log.error(e.toString());
		}
		return null;
	}
	/**
	 * 根据用户的openid获取用户信息
	 * @param restTemplate
	 * @param openId
	 * @param wbcxdm
	 * @return
	 */
	public static WeChatUserModel getUserInfo(RestTemplate restTemplate,String openId, String wbcxdm,RedisUtil redisUtil){

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		
		try{
			String url = new StringBuffer(USER_URL).append("?access_token=").append(obj.toString())
					.append("&openid=").append(openId).append("&lang=zh_CN").toString();
			String json = restTemplate.getForObject(url, String.class);
			log.error("viewevent getUserInfo url=" + url +" result=" + json);
			//如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。 https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
			String userInfo = new StringBuffer("https://api.weixin.qq.com/sns/userinfo").append("?access_token=").append(obj.toString()).append("&openid=").append(openId).append("&lang=zh_CN").toString();
			String returnInfo = restTemplate.getForObject(userInfo, String.class);
			log.error("viewevent getUserInfo url=" + userInfo +" returnInfo=" + returnInfo);

			// 2021-2-20 lsy 用户信息编码变更 
			// String json = new String(json.getBytes("ISO-8859-1"), "UTF-8");
			
			if(StringUtil.isNotBlank(json)) {
				WeChatUserModel userinfo = JSONObject.parseObject(json,WeChatUserModel.class);
				
				if(StringUtil.isNotBlank(userinfo.getErrcode()) && !"0".equals(userinfo.getErrcode())) {
                    return null;
                }
				return userinfo;
			}
		}catch(Exception e){
			log.error(e.toString());
		}
		return null;
	}
	
	/**
	 * 批量设置用户标签
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static boolean setTagUser(WxyhDto wxyhDto, RestTemplate restTemplate,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = wxyhDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = SET_TAG.replace("ACCESS_TOKEN", obj.toString());
		Map<String, Object> paraMap = new HashMap<>();
		List<String> ids = wxyhDto.getIds();
		paraMap.put("openid_list", ids);
		paraMap.put("tagid", Integer.valueOf(wxyhDto.getBqid()));
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	
	/**
	 * 批量取消用户标签
	 * @param bqglDto
	 * @param restTemplate
	 * @return
	 */
	public static boolean cancleTagUser(WxyhDto wxyhDto, RestTemplate restTemplate,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String wbcxdm = wxyhDto.getWbcxdm();

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = CANCLE_TAG.replace("ACCESS_TOKEN", obj.toString());
		Map<String, Object> paraMap = new HashMap<>();
		List<String> ids = wxyhDto.getIds();
		paraMap.put("openid_list", ids);
		paraMap.put("tagid", Integer.valueOf(wxyhDto.getBqid()));
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	
	/**
	 * 发送客服消息（图文）
	 * @param restTemplate
	 * @param openid
	 * @return
	 */
	public static boolean sendGuestImage(RestTemplate restTemplate, String openid, String picurl, String title, String description, String jumpurl, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = SEND_CUSTOM.replace("ACCESS_TOKEN", obj.toString());
		Map<String, Object> paraMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> t_map = new HashMap<>();
		List<Map<String, Object>> listMap = new ArrayList<>();
		paraMap.put("touser", openid);
		paraMap.put("msgtype", "news");
		paraMap.put("news", map);
		map.put("articles", listMap);
		t_map.put("title", title);
		t_map.put("description", description);
		t_map.put("url", jumpurl);
		t_map.put("picurl", picurl);
		listMap.add(t_map);	
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);
        return SUCCESS_CODE.equals(returnModel.getErrcode());
    }
	
	/**
	 * 根据微信返回的Code查询相应点击连接的用户的openid 和access_token
	 * 因暂时无需用到用户的详细信息，所以只需调用接口获取openid就可
	 * @param restTemplate
	 * @param code
	 * @param wbcxDto
	 * @return
	 * @throws BusinessException
	 */
	public static WeChatUserModel getUserBaseInfoByLink(RestTemplate restTemplate,String code, WbcxDto wbcxDto,RedisUtil redisUtil) throws BusinessException{
		log.info(" getUserBaseInfoByLink start code:"+code);
		if(StringUtil.isBlank(code)) {
            return null;
        }
		try{
			DBEncrypt dbEncrypt = new DBEncrypt();
			String appid = dbEncrypt.dCode(wbcxDto.getAppid());
			String secret = dbEncrypt.dCode(wbcxDto.getSecret());
			//通过code换取网页授权access_token "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			String access_url = new StringBuffer(USER_TOKEN).append("?appid=").append(appid)
					.append("&secret=").append(secret).append("&code=").append(code).append("&grant_type=authorization_code").toString();
			/* 返回结果
			   "access_token":"ACCESS_TOKEN",  网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
			   "expires_in":7200,  access_token接口调用凭证超时时间，单位（秒）
			   "refresh_token":"REFRESH_TOKEN",   用户刷新access_token
			   "openid":"OPENID",   用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
			   "scope":"SCOPE",    用户授权的作用域，使用逗号（,）分隔
			   "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"  只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
			   */
			String s_token_return = restTemplate.getForObject(access_url, String.class);
			log.info("getUserBaseInfoByLink ----返回值 s_token_return:"+s_token_return);
			JSONObject tokenObject = JSONObject.parseObject(s_token_return);
			// 有错误直接返回  错误返回 {"errcode":40029,"errmsg":"invalid code"}
			if(tokenObject.get("errcode") != null){
				throw new BusinessException("","未获取到用户信息，错误代码为：" + tokenObject.get("errcode"));
			}
			//用户的访问token,以及用户的openid
			String s_access_token = (String)tokenObject.get("access_token");
			String s_openid = (String)tokenObject.get("openid");
			log.info("getUserBaseInfoByLink ----access_token:"+s_access_token + " ----openid:"+s_openid);
			WeChatUserModel userModel = new WeChatUserModel();
			userModel.setOpenid(s_openid);
			userModel.setAccess_token(s_access_token);
			return userModel;
		}catch(Exception e){
			log.error(e.toString());
			throw new BusinessException("",e.toString());
		}
	}
	
	/**
	 * 发送模板消息给相应的微信用户
	 * @param restTemplate
	 * @param templateid
	 * @param wxid
	 * @param ybbh
	 * @param bz
	 * @param downloadurl
	 * @param wbcxdm
	 * @return
	 */
	public static String sendTemplateMsg(RestTemplate restTemplate,String templatedm,String wxid,String ybbh,String bz,String downloadurl, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		String templateid = "";
		List<JcsjDto> templateMessages = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_MESSAGE.getCode());
		if (templateMessages != null && templateMessages.size() > 0) {
			for (JcsjDto templateMessage : templateMessages) {
				if (wbcxdm.equals(templateMessage.getCskz2()) && templatedm.equals(templateMessage.getCskz3())) {
					templateid = templateMessage.getCskz1();
					break;
				}
			}
		}
		if (StringUtil.isBlank(templateid)) {
			return null;
		}
		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		if(StringUtil.isBlank(wxid)) {
            return null;
        }
		String url = SEND_TAG.replace("ACCESS_TOKEN", obj.toString());
		WeChatSendModel sendModel = new WeChatSendModel();
		sendModel.setTemplate_id(templateid);
		sendModel.setTouser(wxid);
		sendModel.setUrl(downloadurl);
		sendModel.setTopcolor("#173177");
		
		Map<String, WeChatSendDetailModel> detailMap = new HashMap<>();
		
		WeChatSendDetailModel first = new WeChatSendDetailModel();
		first.setValue("您好，您送检标本(" + bz + ")的报告单已发出。");
		first.setColor("#173177");
		detailMap.put("first", first);

		WeChatSendDetailModel keyword1 = new WeChatSendDetailModel();
		keyword1.setValue(ybbh);
		keyword1.setColor("#173177");
		detailMap.put("keyword1", keyword1);

		WeChatSendDetailModel keyword2 = new WeChatSendDetailModel();
		keyword2.setValue(ybbh);
		keyword2.setValue("您好，您送检标本(" + bz + ")的报告单已发出。");
		keyword2.setColor("#173177");
		detailMap.put("keyword2", keyword2);

		WeChatSendDetailModel keyword3 = new WeChatSendDetailModel();
		keyword3.setValue("杭州杰毅医学检验实验室竭诚为您服务！");
		keyword3.setColor("#173177");
		detailMap.put("keyword3", keyword3);

		WeChatSendDetailModel keyword4 = new WeChatSendDetailModel();
		keyword4.setValue(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
		keyword4.setColor("#173177");
		detailMap.put("keyword3", keyword4);
		
		WeChatSendDetailModel remark = new WeChatSendDetailModel();
		remark.setValue("杭州杰毅医学检验实验室竭诚为您服务！");
		remark.setColor("#173177");
		detailMap.put("remark", remark);
		
		sendModel.setData(detailMap);

        return restTemplate.postForObject(url, sendModel, String.class);
	}

	/**
	 * 发送模板消息给相应的微信用户
	 * @param redisUtil
	 * @param restTemplate
	 * @param templatedm
	 * @param wxid
	 * @param wbcxdm
	 * @param messageMap
	 * @return
	 */
	public static String sendWeChatMessageMap(RedisUtil redisUtil,RestTemplate restTemplate, String templatedm, String wxid, String wbcxdm, Map<String, String> messageMap) {
		// TODO Auto-generated method stub
		log.error("sendWeChatMessageMap sendModel: wbcxdm="+wbcxdm+" templatedm="+templatedm+" wxid="+wxid+" messageMap="+JSONObject.toJSONString(messageMap));
		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null){
			getWechatToken(restTemplate, null, null, null, wbcxdm, redisUtil);
			obj = redisUtil.get("WECHAT_TOKE_"+wbcxdm);
			if (obj == null){
				return null;
			}
		}
		if (StringUtil.isAnyBlank(wxid,templatedm)){
			return null;
		}
		//根据外部程序代码匹配模板消息
		String templateid = "";
		List<JcsjDto> templateMessages = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.TEMPLATE_MESSAGE.getCode());
		if (templateMessages != null && templateMessages.size() > 0) {
			for (JcsjDto templateMessage : templateMessages) {
				if (wbcxdm.equals(templateMessage.getCskz2()) && templatedm.equals(templateMessage.getCskz3())) {
					templateid = templateMessage.getCskz1();
					break;
				}
			}
		}
		if (StringUtil.isBlank(templateid)) {
			return null;
		}
		String url = SEND_TAG.replace("ACCESS_TOKEN", obj.toString());
		WeChatSendModel sendModel = new WeChatSendModel();
		//模板id
		sendModel.setTemplate_id(templateid);
		//微信id
		sendModel.setTouser(wxid);
		sendModel.setTopcolor("#173177");
		//跳转连接
		String reporturl = messageMap.get("reporturl");
		sendModel.setUrl(reporturl);

		Map<String, WeChatSendDetailModel> detailMap = new HashMap<>();

		//小标题
		WeChatSendDetailModel title = new WeChatSendDetailModel();
		String titleText = messageMap.get("title");
		title.setValue(titleText);
		title.setColor("#173177");
		detailMap.put("first", title);
		//备注
		WeChatSendDetailModel remark = new WeChatSendDetailModel();
		String remarkText = messageMap.get("remark");
		remark.setValue(remarkText);
		remark.setColor("#173177");
		detailMap.put("remark", remark);

		Set<String> keys = messageMap.keySet();
		if (keys != null && keys.size() > 0){
			for (String key : keys) {
				if ("title".equals(key) || "remark".equals(key) || "reporturl".equals(key) || StringUtil.isBlank(messageMap.get(key))){
					continue;
				}
				WeChatSendDetailModel keyword = new WeChatSendDetailModel();
				keyword.setValue(messageMap.get(key));
				keyword.setColor("#173177");
				detailMap.put(key, keyword);
			}
		}

		sendModel.setData(detailMap);

		String returnModel = restTemplate.postForObject(url, sendModel, String.class);
		log.error("sendWeChatMessageMap returnModel:"+returnModel);
		if (StringUtil.isNotBlank(returnModel)) {
			JSONObject jsonObject = JSONObject.parseObject(returnModel);
			if (jsonObject != null && jsonObject.containsKey("errcode") && "40001".equals(jsonObject.getString("errcode"))) {
				log.error("sendWeChatMessageMap returnModel:"+returnModel);
				return returnModel;
			}
			if (jsonObject != null && jsonObject.containsKey("errcode") && "42001".equals(jsonObject.getString("errcode"))) {
				log.error("sendWeChatMessageMap returnModel:"+returnModel);
				getWechatToken(restTemplate, null, null, null, wbcxdm, redisUtil);
				return sendWeChatMessageMap(redisUtil, restTemplate, templatedm, wxid, wbcxdm, messageMap);
			}
		}
		return returnModel;
	}

	/**
	 * 发送周报消息给相应的微信用户
	 * @param restTemplate
	 * @param templateid
	 * @param wxid
	 * @param title
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 * @param remark1
	 * @param reporturl
	 * @param wbcxdm
	 * @return
	 */
	public static String sendWeChatMessage(RestTemplate restTemplate, String templateid, String wxid, String title, String value1, String value2,String value3, String value4, String value5,String remark1,
			String reporturl, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub
		log.error("sendWeChatMessage templateid:"+templateid+" title:"+title+" wxid:"+wxid+" value1:"+value1+" value2:"+value2+" value3:"+value3+" value4:"+value4+" value5:"+value5+" remark1:"+remark1+" reporturl:"+reporturl);

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		if(StringUtil.isBlank(wxid)) {
            return null;
        }
		String url = SEND_TAG.replace("ACCESS_TOKEN",obj.toString());
		log.error("sendWeChatMessage wbcxdm:"+wbcxdm+" url:"+url);
		WeChatSendModel sendModel = new WeChatSendModel();
		sendModel.setTemplate_id(templateid);
		sendModel.setTouser(wxid);
		sendModel.setUrl(reporturl);
		sendModel.setTopcolor("#173177");
		
		Map<String, WeChatSendDetailModel> detailMap = new HashMap<>();
		
		WeChatSendDetailModel first = new WeChatSendDetailModel();
		first.setValue(title);
		first.setColor("#173177");
		detailMap.put("first", first);
		
		WeChatSendDetailModel keyword1 = new WeChatSendDetailModel();
		keyword1.setValue(value1);
		keyword1.setColor("#173177");
		detailMap.put("keyword1", keyword1);
		
		WeChatSendDetailModel keyword2 = new WeChatSendDetailModel();
		keyword2.setValue(value2);
		keyword2.setColor("#173177");
		detailMap.put("keyword2", keyword2);
		
		WeChatSendDetailModel keyword3 = new WeChatSendDetailModel();
		keyword3.setValue(value3);
		keyword3.setColor("#173177");
		detailMap.put("keyword3", keyword3);
		
		WeChatSendDetailModel keyword4 = new WeChatSendDetailModel();
		keyword4.setValue(value4);
		keyword4.setColor("#173177");
		detailMap.put("keyword4", keyword4);

		WeChatSendDetailModel keyword5 = new WeChatSendDetailModel();
		keyword4.setValue(value5);
		keyword4.setColor("#173177");
		detailMap.put("keyword4", keyword5);

		WeChatSendDetailModel remark = new WeChatSendDetailModel();
		remark.setValue(remark1);
		remark.setColor("#173177");
		detailMap.put("remark", remark);
		
		sendModel.setData(detailMap);

        return restTemplate.postForObject(url, sendModel, String.class);
	}
	
	/**
	 * 发送消息给相应的微信用户
	 * @param restTemplate
	 * @param templateid
	 * @param wxid
	 * @param ybbh
	 * @param message
	 * @param title
	 * @param wbcxdm
	 * @return
	 */
	public static String sendOutMsg(RestTemplate restTemplate, String templateid, String wxid, String ybbh, String message, String title, String bgrq, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
		if(StringUtil.isBlank(wxid)) {
            return null;
        }
		String url = SEND_TAG.replace("ACCESS_TOKEN", obj.toString());
		WeChatSendModel sendModel = new WeChatSendModel();
		sendModel.setTemplate_id(templateid);
		sendModel.setTouser(wxid);
		sendModel.setTopcolor("#173177");
		
		Map<String, WeChatSendDetailModel> detailMap = new HashMap<>();
		
		WeChatSendDetailModel first = new WeChatSendDetailModel();
		first.setValue(title);
		first.setColor("#173177");
		detailMap.put("first", first);
		
		WeChatSendDetailModel keyword1 = new WeChatSendDetailModel();
		keyword1.setValue(ybbh);
		keyword1.setColor("#173177");
		detailMap.put("keyword1", keyword1);
		
		WeChatSendDetailModel keyword2 = new WeChatSendDetailModel();
		keyword2.setValue(bgrq);
		keyword2.setColor("#173177");
		detailMap.put("keyword2", keyword2);
		
		WeChatSendDetailModel remark = new WeChatSendDetailModel();
		remark.setValue(message);
		remark.setColor("#173177");
		detailMap.put("remark", remark);
		
		
		sendModel.setData(detailMap);

        return restTemplate.postForObject(url, sendModel, String.class);
	}
	
	/**
	 * 根据media_id获取文件流
	 * @param restTemplate
	 * @return
	 */
	public static boolean getFileStream(RestTemplate restTemplate, String mediaId, String tempPath, String backPath, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + obj.toString() + "&media_id=" + mediaId;
		ResponseEntity<Resource> entity = restTemplate.getForEntity(url, Resource.class);
		InputStream inputStream = null;
		try {
			inputStream = entity.getBody().getInputStream();
			//做标记为了流重复使用
			inputStream.mark(inputStream.available() + 1);
			//将上传的文件存放到：路径前缀+指定路径(临时存放目录)
			boolean flag = uploadFile(inputStream, tempPath);
			//为读取未转换过去的数据，还需保留一份文件，调用标记
			inputStream.reset();
			if(StringUtil.isNotBlank(backPath)) {
				flag = uploadFile(inputStream, backPath);
			}
			return flag;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	/**
	 * 把文件转存到相应路径下
	 * @param file
	 * @param filePath
	 * @return
	 */
	private static boolean uploadFile(InputStream inputStream, String filePath){
		boolean flag = false;
		byte[] data = new byte[10240];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filePath);
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
			flag = true;
		} catch (IOException e) {
			log.error(e.toString());
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	/**
	 * 群发消息接口
	 * @param restTemplate
	 * @return
	 */
	public static boolean sendAllText(RestTemplate restTemplate, String xxnr, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = SEND_ALL.replace("ACCESS_TOKEN", obj.toString());
		Map<String, Object> paraMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> t_map = new HashMap<>();
		map.put("is_to_all", true);
		paraMap.put("filter", map);
		t_map.put("content", xxnr);
		paraMap.put("text", t_map);
		paraMap.put("msgtype", "text");
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);
		if(SUCCESS_CODE.equals(returnModel.getErrcode())) {
            return true;
        }
		log.error(returnModel.getErrcode());
		return false;
	}
	
    /**
     * 上传永久素材(图片)
     *
     * @param filePath    文件路径
     * @param type        消息类型(image)
     * @return
     * @throws Exception
     */
    public static WeChatMaterialModel uploadImageMaterial(String filePath, String type, String wbcxdm,RedisUtil redisUtil){

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null){
    		log.error("未获取到token，外部程序代码："+wbcxdm);
    		return null;
    	}
    	try {
	        File file = new File(filePath);
	        if (!file.exists() || !file.isFile()) {
	        	log.error("未找到文件："+filePath);
	        	return null;
	        }
	        String url = ADD_MATERIAL.replace("ACCESS_TOKEN", obj.toString()).replace("TYPE", type);
	        URL urlObj = new URL(url);
	        // 创建Http连接
	        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoInput(true);
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);   // 使用post提交需要设置忽略缓存
	        //消息请求头信息
	        conn.setRequestProperty("Connection", "Keep-Alive");
	        conn.setRequestProperty("Charset", "UTF-8");
	        //设置边界
	        String BOUNDARY = "----------" + System.currentTimeMillis();
	        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
	        StringBuilder sb = new StringBuilder();
	        sb.append("--");    // 必须多两道线
	        sb.append(BOUNDARY);
	        sb.append("\r\n");
	        sb.append("Content-Disposition:form-data;name=\"media\";filename=\"" + file.getName() + "\";filelength=\"" + file.length() + "\"\r\n");
	        sb.append("Content-Type:application/octet-stream\r\n\r\n");
	        byte[] head = sb.toString().getBytes("utf-8");
	        OutputStream out = new DataOutputStream(conn.getOutputStream());// 创建输出流
	        out.write(head);// 获得输出流表头
	        //文件正文部分
	        DataInputStream in = new DataInputStream(new FileInputStream(file));
	        int bytes = 0;
	        byte[] bufferOut = new byte[1024 * 1024 * 10]; // 10M
	        while ((bytes = in.read(bufferOut)) != -1) {
	            out.write(bufferOut, 0, bytes);
	        }
	        in.close();
	        //结尾
	        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
	        out.write(foot);
	        out.flush();
	        out.close();
	        
	        // 获取响应
	        StringBuffer buffer = new StringBuffer();
	        BufferedReader reader = null;
	        String result = null;
	        try {
	            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                buffer.append(line);
	            }
	            if (result == null) {
	                result = buffer.toString();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            reader.close();
	        }
	        WeChatMaterialModel weChatMaterial = JSONObject.parseObject(result,WeChatMaterialModel.class);
			if(weChatMaterial != null && StringUtil.isNotBlank(weChatMaterial.getErrcode()) && !SUCCESS_CODE.equals(weChatMaterial.getErrcode()) ) {
				log.error("素材上传未成功，错误代码为：" + weChatMaterial.getErrcode());
				return null;
			}
	        return weChatMaterial;
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("素材上传异常："+e.toString());
		}
        return null;
    }
    
    /**
     * 生成字符串临时二维码
     * @param restTemplate
     * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒
     * @param sceneStr 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64; scene_id	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * @return
     */
    public static String generateQRcodeTempStr(RestTemplate restTemplate, String expireSeconds, String sceneStr, String wbcxdm,RedisUtil redisUtil){

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return null;
        }
    	String url = QR_CODE.replace("ACCESS_TOKEN", obj.toString());
    	Map<String, Object> paraMap = new HashMap<>();
    	paraMap.put("expire_seconds", expireSeconds);
    	paraMap.put("action_name", QR_STR_SCENE); // 二维码类型
    	Map<String,String> intMap = new HashMap<>();
    	intMap.put("scene_str",sceneStr);
    	Map<String,Map<String,String>> mapMap = new HashMap<>();
    	mapMap.put("scene", intMap);
		paraMap.put("action_info", mapMap);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(url, paraMap, Map.class);
		String errcode = (String) resultMap.get("errcode");
		if(StringUtil.isNotBlank(errcode)){
			log.error(resultMap.toString());
			return null;
		}
		log.error(resultMap.toString());
    	return (String)resultMap.get("ticket");
    }
    
	
	/* -------------------------------------------- 未使用方法 -------------------------------------------- */
	
    /**
	 * 根据微信返回的Code查询相应点击连接的用户的详细信息
	 * @param restTemplate
	 * @param code
	 * @param appid
	 * @param secret
	 * @return
	 * @throws BusinessException
	 */
	public static WeChatUserModel getUserDetailInfoByLink(RestTemplate restTemplate, String code, WbcxDto wbcxDto,RedisUtil redisUtil) throws BusinessException{
		
		try{
			//通过code换取网页授权access_token和openid
			WeChatUserModel userModel = getUserBaseInfoByLink(restTemplate,code,wbcxDto,redisUtil);
			
			if(userModel == null) {
                throw new BusinessException("","未获取到用户信息。");
            }
			
			//如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。 https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
			 /*"openid":" OPENID",   用户的唯一标识
			   " nickname": NICKNAME,  用户昵称
			   "sex":"1",    用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
			   "province":"PROVINCE"  用户个人资料填写的省份
			   "city":"CITY",   普通用户个人资料填写的城市
			   "country":"COUNTRY",   国家，如中国为CN
			    "headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46", 
			    "privilege":[  
			    "PRIVILEGE1"
			    "PRIVILEGE2"
			    ],
			    "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"*/

			//如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。 https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
			String userInfo = new StringBuffer("https://api.weixin.qq.com/sns/userinfo").append("?access_token=").append(userModel.getAccess_token()).append("&openid=").append(userModel.getOpenid()).append("&lang=zh_CN").toString();
			String returnInfo = restTemplate.getForObject(userInfo, String.class);
			log.error("sns2_userinfo=" + returnInfo);

            return JSONObject.parseObject(returnInfo, WeChatUserModel.class);
		}catch(Exception e){
			throw new BusinessException("",e.toString());
		}
	}
	
	/**
	 * 发送客服消息（文字）
	 * @param restTemplate
	 * @param openid
	 * @return
	 */
	public static boolean sendGuestText(RestTemplate restTemplate,String openid, String xxnr, String wbcxdm,RedisUtil redisUtil) {
		// TODO Auto-generated method stub

		Object obj=redisUtil.get("WECHAT_TOKE_"+wbcxdm);
		if(obj==null) {
            return false;
        }
		String url = SEND_CUSTOM.replace("ACCESS_TOKEN", obj.toString());
		Map<String, Object> paraMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		paraMap.put("touser", openid);
		paraMap.put("msgtype", "text");
		paraMap.put("text", map);
		map.put("content", xxnr);
		WeChatReturnModel returnModel = restTemplate.postForObject(url, paraMap, WeChatReturnModel.class);	
		if(SUCCESS_CODE.equals(returnModel.getErrcode())) {
            return true;
        }
		log.error("发送消息失败："+returnModel.getErrcode());
		return false;
	}
}
