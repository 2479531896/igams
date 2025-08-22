package com.matridx.server.wechat.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;

@Controller
@RequestMapping("/wechat")
public class WechatCommonController extends BaseController{
	
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	
	private Logger log = LoggerFactory.getLogger(WechatCommonController.class);

	
	/**
	 * 小程序获取用md5加密钉钉ID和真实姓名第一位后的结果
	 * @param request
	 * @return
	 */
	@RequestMapping("/oauth/Md5Ecode")
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
	 * 小程序登陆时验证用户名密码，如果已经存在现有的token，则直接采用现有的，不在调用登陆接口，如果没有则调用新登陆
	 * 确认token的有效期，如果低于1小时，则重新登陆
	 * @param request
	 * @return
	 */
	@RequestMapping("/oauth/minilocaltoken")
	@ResponseBody
	public Map<String, String> miniLoginCheck(HttpServletRequest request){
		Map<String, String> returnMap = null;
        try {
        	//检测是否已经存在现有的token
        	//returnMap = checkLoginInfo(request);
        	
	        	//转入正常的验证
	        	returnMap = turnToSpringSecurityLogin(request);
        	
        } catch (Exception e) {
            e.printStackTrace();
            log.error("miniLoginCheck:" + e.toString());
        }
        return returnMap;
	}
	
	/**
	 * 转到正常的spring Security 认证
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> turnToSpringSecurityLogin(HttpServletRequest request) {
		Map<String, String> returnMap = null;
        try {
        	String username = request.getParameter("username");
        	String password= request.getParameter("password");
        	if(StringUtil.isBlank(username)) {
        		log.error("turnToSpringSecurityLogin 未获取到用户名信息：" + username + ":" + password);
        	}
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
                DBEncrypt crypt = new DBEncrypt();
                HttpPost httppost = new HttpPost(crypt.dCode(companyurl)+"/oauth/token");
                log.error(" turnToSpringSecurityLogin url " + crypt.dCode(companyurl)+"/oauth/token");
                if (pairs != null && pairs.size() > 0) {
                    httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
                }
                httpresponse = httpclient.execute(httppost);
                String response = EntityUtils
                        .toString(httpresponse.getEntity());
                
                returnMap = JSONObject.parseObject(response, Map.class);
                Date date =new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str_date=formatter.format(date);
                long now_time=formatter.parse(str_date).getTime()/1000;
                returnMap.put("now_time", String.valueOf(now_time));
                
                if(returnMap.get("expires_in")!=null) {
                	int end_time=Integer.parseInt(String.valueOf(now_time))+Integer.parseInt(String.valueOf(returnMap.get("expires_in")));
                    String ex_in =String.valueOf(returnMap.get("expires_in"));
                    returnMap.remove("expires_in");
                    returnMap.put("expires_in", ex_in);
                    returnMap.put("end_time", String.valueOf(end_time));
                }
            } catch (Exception e) {
                log.error("turnToSpringSecurityLogin post:" + e.toString());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            log.error("turnToSpringSecurityLogin ext:" + e.toString());
        }
        return returnMap;
	}
	
}
