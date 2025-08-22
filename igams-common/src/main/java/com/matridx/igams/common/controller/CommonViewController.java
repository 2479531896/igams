package com.matridx.igams.common.controller;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class CommonViewController extends BaseController{

	@Autowired
	IXxglService xxglService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	IShxxService shxxService;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Autowired
	ICommonService commonService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IFjcfbService fjcfbService;

	private final Logger logger = LoggerFactory.getLogger(CommonViewController.class);


	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 手机端，引用手机端样式
	 */
	@RequestMapping(value="/view/displayView")
	public ModelAndView displayView(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("common/view/display_view");
			
			//mv.addObject("file",URLEncoder.encode(filepath, "UTF-8"));
			String view_url = request.getParameter("view_url");
			//view_url = URLDecoder.decode(view_url, StandardCharsets.UTF_8);
			view_url = view_url.replaceAll("＆","&");
			mv.addObject("view_url", view_url);
			mv.addObject("urlPrefix", urlPrefix);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 电脑端，引用电脑端样式
	 */
	@RequestMapping(value="/view/displayViewPC")
	public ModelAndView displayViewPC(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("common/view/display_viewpc");
			String view_url = request.getParameter("view_url");
			view_url = URLDecoder.decode(view_url, StandardCharsets.UTF_8);
			view_url = view_url.replaceAll("＆","&");
			mv.addObject("view_url", view_url);
			mv.addObject("access_token", request.getParameter("access_token"));
			mv.addObject("urlPrefix", urlPrefix);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 外部访问修改送检信息页面
	 */
	@RequestMapping(value="/view/modInspection")
	public ModelAndView modInspection(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("common/view/display_view");
			
			String ybbh = request.getParameter("ybbh");
			String access_token = request.getParameter("access_token");
			String view_url = "/inspection/inspection/commOutEdit?ybbh="+ybbh+"&access_token="+access_token+"&xg_flg=1";
			mv.addObject("view_url", view_url);
			mv.addObject("urlPrefix", urlPrefix);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 修改审核信息(时间)
	 */
	@RequestMapping(value="/audit/auditmodTimeView")
	public ModelAndView auditmodTimeView(ShxxDto shxxDto) {
		ModelAndView mav = new ModelAndView("common/audit/auditmod_view");
		String ywid = shxxDto.getYwid();
		String shlb = shxxDto.getShlb();
		mav.addObject("ywid", ywid);
		mav.addObject("shlb", shlb);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改审核信息保存
	 */
	@RequestMapping(value="/audit/auditmodSaveTime")
	@ResponseBody
	public Map<String,Object> auditmodSaveTime(ShxxDto shxxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		shxxDto.setXgry(user.getYhid());
		boolean isSuccess = shxxService.auditmodSaveTime(shxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 预览页面(附件ID)
	 */
	@RequestMapping(value="/view/viewerById")
	public ModelAndView viewerById(HttpServletRequest request) {
		try {
			String fjid = request.getParameter("fjid");
			String jmzd = request.getParameter("jmzd"); // 加密字段
			String sign = request.getParameter("sign");
			boolean checkSign = commonService.checkSign(sign, jmzd, request);
			if(!checkSign){
				return commonService.jumpError();
			}
			ModelAndView mv = new ModelAndView("common/file/viewer");
			String filePath = urlPrefix + "/common/view/getFileInfoById?fjid=" + fjid;
			mv.addObject("file",filePath);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 外部预览pdf(附件ID)
	 */
	@RequestMapping(value="/view/getFileInfoById")
	public void getFileInfoById(FjcfbDto fjcfbDto, HttpServletResponse response){
		if(StringUtil.isBlank(fjcfbDto.getFjid()))
			return;
		fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(fjcfbDto == null || StringUtil.isBlank(fjcfbDto.getWjlj()))
			return;
		DBEncrypt crypt = new DBEncrypt();
		String wjlj = crypt.dCode(fjcfbDto.getWjlj());
		downloadFile(wjlj,response);
	}
	
	/**
	 * 预览页面(附件ID)
	 */
	@RequestMapping(value="/view/viewerDocById")
	public ModelAndView viewerDocById(HttpServletRequest request) {
		try {
			String fjid = request.getParameter("fjid");
			String sign = request.getParameter("sign");
			boolean checkSign = commonService.checkSign(sign, fjid, request);
			if(!checkSign){
				return commonService.jumpError();
			}
			ModelAndView mv = new ModelAndView("common/file/viewer");
			String filePath = urlPrefix + "/common/view/getDocFileInfoById?fjid=" + fjid;
			mv.addObject("file",filePath);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 外部预览pdf(附件ID)
	 */
	@RequestMapping(value="/view/getDocFileInfoById")
	public void getDocFileInfoById(FjcfbDto fjcfbDto, HttpServletResponse response){
		if(StringUtil.isBlank(fjcfbDto.getFjid()))
			return;
		fjcfbDto = fjcfbService.getDto(fjcfbDto);
		if(fjcfbDto == null || StringUtil.isBlank(fjcfbDto.getZhwjxx()))
			return;
		DBEncrypt crypt = new DBEncrypt();
		String wjlj = crypt.dCode(fjcfbDto.getZhwjxx());
		downloadFile(wjlj,response);
	}
	
	/**
	 * 外部预览pdf页面(按照文件名称)
	 */
	@RequestMapping(value="/view/pdfPreview")
	public ModelAndView pdfPreview(HttpServletRequest request) {
		try {
			String wjm = request.getParameter("wjm");
			String sign = request.getParameter("sign");
			boolean checkSign = commonService.checkSign(sign, wjm, request);
			if(!checkSign){
				return commonService.jumpError();
			}
			ModelAndView mv = new ModelAndView("common/view/display_viewpc");
			String view_url = "/common/view/viewer?wjm=" + wjm;
			mv.addObject("view_url", view_url);
			mv.addObject("urlPrefix", urlPrefix);
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 预览页面
	 */
	@RequestMapping(value="/view/viewer")
	public ModelAndView viewer(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("common/file/viewer");
			String wjm = request.getParameter("wjm");
			String filePath = urlPrefix + "/common/view/getFileInfo?wjm=" + wjm;
			mv.addObject("file",filePath);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 外部预览pdf
	 */
	@RequestMapping(value="/view/getFileInfo")
	public void getFileInfo(String wjm, HttpServletResponse response){
		if(StringUtil.isBlank(wjm))
			return;
		String wjlj = prefix + tempFilePath + BusTypeEnum.IMP_TEMP_WORD.getCode() + "/" + wjm;
		downloadFile(wjlj,response);
	}
	
	/**
	 * 根据文件路径，获取文件内容，并写到流里
	 */
	private void downloadFile(String filePath,HttpServletResponse response) {
		boolean haveFile = false;
		//没取到文件睡眠1s，取五次
		for (int i = 0; i < 5; i++) {
			File file = new File(filePath);
			if(!file.exists()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				haveFile = true;
				break;
			}
		}
		if(haveFile){
	        byte[] buffer = new byte[1024];
	        BufferedInputStream bis = null;
	        
	        OutputStream os = null; //输出流
	        try {
	    		InputStream iStream = new FileInputStream(filePath);
	    		
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
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
	}
	
	/**
	 * 注册钉钉回调
	 */
	@RequestMapping(value="/dingtalk/registercallbackSavePage")
	@ResponseBody
	public Map<String,Object> registercallbackSavePage(String wbcxid, String url) {
		User user=getLoginInfo();
		logger.error("用户名:"+user.getYhm()+",真实姓名:"+user.getZsxm());
		Map<String, Object> registerCallback = talkUtil.registerCallbackByYhm(user.getYhm(),wbcxid,url);
		if("success".equals(registerCallback.get("status"))) {
			registerCallback.put("message", "回调成功");
		}
		return registerCallback;
	}

	/**
	 * 获取已注册的回调信息
	 */
	@RequestMapping(value="/dingtalk/pagedataGetUrl")
	@ResponseBody
	public Map<String,Object> pagedataGetUrl(String wbcxid) {
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(wbcxid)){
			String token =talkUtil.getToken(wbcxid);
			try {
				Map<String,Object> getCallBackMessage = talkUtil.getCallBackMessage(token);
				if("success".equals(getCallBackMessage.get("status"))) {
					String body= getCallBackMessage.get("message").toString();
					JSONObject json_body=(JSONObject) JSONObject.parse(body);
					String url=json_body.get("url").toString();
					map.put("url", url);
				}else{
					map.put("url", "");
				}
			} catch (ApiException e) {
				map.put("url", "");
			}
		}
		return map;
	}
	
	/**
	 * 获取已注册的回调信息
	 */
	@RequestMapping(value="/dingtalk/registercallbackPage")
	public ModelAndView registercallbackPage(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("systemmain/ddxxgl/ddxx_callback");
		User user = getLoginInfo();
		String wbcxid = null;
		if (null!=user && StringUtil.isNotBlank(user.getWbcxid())){
			wbcxid = user.getWbcxid();
		}
		String token =talkUtil.getToken(StringUtil.isNotBlank(wbcxid)?wbcxid:request.getParameter("wbcxid"));
		mav.addObject("wbcxid", wbcxid);
		try {
			Map<String,Object> getCallBackMessage = talkUtil.getCallBackMessage(token);
			if("success".equals(getCallBackMessage.get("status"))) {
				String body= getCallBackMessage.get("message").toString();
				JSONObject json_body=(JSONObject) JSONObject.parse(body);
				String url=json_body.get("url").toString();
				mav.addObject("url", url);
			}else{
				mav.addObject("url", "");
			}
		} catch (ApiException e) {
			mav.addObject("url", "");
		}
		Map<Object, Object> wbcxInfo = redisUtil.hmget("WbcxInfo");
		if (wbcxInfo!=null && !wbcxInfo.isEmpty()) {
			List<Map<String,String>> maps = new ArrayList<>();
			for (Map.Entry<Object, Object> next : wbcxInfo.entrySet()) {
				Object value = next.getValue();
				Object key = next.getKey();
				if (null != value && null != key) {
					Map<String, String> map = new HashMap<>();
					JSONObject jsonObject = JSONObject.parseObject(String.valueOf(value));
					map.put("wbcxid", String.valueOf(key));
					map.put("aeskey", jsonObject.getString("aeskey"));
					map.put("appkey", jsonObject.getString("appkey"));
					map.put("token", jsonObject.getString("token"));
					map.put("wbcxmc", jsonObject.getString("wbcxmc"));
					maps.add(map);
				}
			}
			mav.addObject("maps", maps);

		}
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
}
