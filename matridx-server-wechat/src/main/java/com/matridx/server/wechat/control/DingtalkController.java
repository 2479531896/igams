package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class DingtalkController extends BaseController{

	@Value("${matridx.dingtalkprogram.appKey:}")
	private String appKey;
	@Value("${matridx.dingtalkprogram.appSecret:}")
	private String appSecret;
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	@Value("${matridx.fileupload.tempPath:}")
	private String tempFilePath;
	@Autowired
	RedisUtil redisUtil;
	
	private Logger log = LoggerFactory.getLogger(DingtalkController.class);

	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ICommonService commonService;

    
	/**
	 * 获取Token
	 * @return
	 * @throws ApiException
	 */
	public OapiGettokenResponse getToken() throws ApiException {
		//获取token
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
		OapiGettokenRequest request = new OapiGettokenRequest();
		DBEncrypt p = new DBEncrypt();
		String dappSecret=p.dCode(appSecret);
		request.setAppkey(appKey);
		request.setAppsecret(dappSecret);
		request.setHttpMethod("GET");
        return client.execute(request);
	}
	
	/**
	 * 获取用户Userid
	 * @throws ApiException 
	 */
	public Map<String,Object> getUserID(String AuthCode) throws ApiException {
		OapiGettokenResponse response=getToken();
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
		OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
		request.setCode(AuthCode);
		request.setHttpMethod("GET");
		OapiUserGetuserinfoResponse t_response = client.execute(request, response.getAccessToken());
		String userId = t_response.getUserid();
		Map<String,Object> map= new HashMap<>();
		map.put("token", response.getAccessToken());
		map.put("userid", userId);
		return map;
	}
	/**
	 * 传递参数，获取钉钉用户详情
	 * @return
	 */
	@RequestMapping(value="/getUserInfo")
	@ResponseBody
	public Map<String,Object> getUser(String code,String miniappid) {
		DBEncrypt crypt = new DBEncrypt();
		String t_url=crypt.dCode(companyurl)+"/ws/web/getUserInfo";
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("code", code);
		paramMap.add("miniappid", miniappid);
        return restTemplate.postForObject(t_url, paramMap, Map.class);
	}

	/**
	 * 保存临时文件
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveDdImportFile")
	@ResponseBody
	public Map<String, Object> saveImportFile(FjcfbDto fjcfbDto,HttpServletRequest request){
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
		Map<String, Object> result = new HashMap<>();
		try{
			MultipartFile[] imp_file = new MultipartFile[files.size()];
			files.toArray(imp_file);
			if(imp_file!=null&& imp_file.length>0){
				User user = null;
				fjcfbService.save2TempFile(imp_file, fjcfbDto,user);
				
				result.put("status", "success");
				result.put("fjcfbDto", fjcfbDto);
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
	 * 钉钉小程序修改保存反馈信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/savefeedback")
	@ResponseBody
	public Map<String,Object> saveFeedBack(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjxxService.updateFeedBack(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 附件下载（用于普通的文件下载使用）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/downloadFkFile")
	public String downloadDocFile(FjcfbDto fjcfbDto,HttpServletResponse response,HttpServletRequest request){
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
	 * 删除附件信息
	 * @param fjcfbDto
	 * @return
	 */
	@RequestMapping(value = "/delFkFile")
	@ResponseBody
	public Map<String, Object> delFkFile(FjcfbDto fjcfbDto){
		fjcfbDto.setScry(fjcfbDto.getWxid());
		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 小程序查看本地文件
	 * @param url
	 * @param data
	 * @param response
     */
	@RequestMapping(value="/downloadMiniFile")
	@ResponseBody
	public void downloadMiniFile(String url,String data,HttpServletResponse response) {
		//paramMap未被使用，故注释，2023/10/26
//		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
//		if (data != null && data != "") {
//			@SuppressWarnings("unchecked")
//			List<Map<String, String>> list = JSONArray.parseObject(data, List.class);
//			for (int i = 0; i < list.size(); i++) {
//				if (("null").equals(list.get(i).get("value"))) {
//					paramMap.add((String) list.get(i).get("title"), null);
//				} else {
//					paramMap.add((String) list.get(i).get("title"), list.get(i).get("value"));
//				}
//			}
//		}
		// 判断url是否为空，若为null则返回错误页面
		if (StringUtil.isBlank(url)) {
			return;
		}
		DBEncrypt crypt = new DBEncrypt();
		String t_url = crypt.dCode(companyurl) + url;

		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> entity = restTemplate.exchange(t_url, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

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
	}
	
	/**
	 * 钉钉小程序共通请求接口
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/commonRequest")
	@ResponseBody
	public Map<String,Object> getData(String url,String data){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		if(StringUtil.isNotBlank(data)) {
			@SuppressWarnings("unchecked")
			List<Map<String,String>> list= JSONArray.parseObject(data,List.class);
			for(int i=0;i<list.size();i++) {
				if(("null").equals(list.get(i).get("value"))) {
					paramMap.add((String) list.get(i).get("title"), null);
				}else {
					paramMap.add((String) list.get(i).get("title").trim(), list.get(i).get("value").trim());
				}
			}
		}
		
		//判断url是否为空，若为null则返回错误页面
		if(StringUtil.isBlank(url)) {
			return null;
		}
		DBEncrypt crypt = new DBEncrypt();
		String t_url=crypt.dCode(companyurl)+url;
		log.error("---commonRequest 调用参数---- url:"+t_url+"---Map:"+paramMap.toString());
		//@SuppressWarnings("unchecked")
		Map<String,Object> result = new HashMap<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Requested-With","XMLHttpRequest");
		HttpEntity<Object> httpEntity = new HttpEntity<>(paramMap,headers);
		try{
			//result=restTemplate.postForObject(t_url, paramMap, Map.class);
			result=restTemplate.postForObject(t_url, httpEntity, Map.class);
		}catch (Exception e){
			log.error("error!!"+e.getMessage());
			result.put("authFail",e.getMessage());
			return result;
		}
		//String result=restTemplate.postForObject(t_url, paramMap, String.class);
		log.error("---commonRequest 调用参数---- url:"+t_url+"---back");
		return result;
	}
	
	/**
	 * 共通转接接口
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/commonTransmit")
	@ResponseBody
	public Map<String,Object> commonTransmit(HttpServletRequest req){
		String url = req.getParameter("url");
		//判断url是否为空，若为null则返回错误页面
		if(StringUtil.isBlank(url)) {
			return null;
		}
		
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		Enumeration<String> em = req.getParameterNames();
		while (em.hasMoreElements()) {
		    String name = (String) em.nextElement();
		    String[] value_map = req.getParameterMap().get(name);
		    if(value_map==null||value_map.length==0) {
		    	 paramMap.add(name, null);
		    }else if(value_map.length==1){
		    	String value = req.getParameter(name);
		    	if(value!=null) {
		    		paramMap.add(name, value);
		    	}
		    }else if(value_map.length>1){
		    	StringBuilder sb = new StringBuilder();  
            	for (int i = 0; i < value_map.length; i++) {  
	                if (i < value_map.length - 1) {  
	                    sb.append(value_map[i] + ",");  
	                } else {  
	                    sb.append(value_map[i]);  
	                }  
	            }  
		    	paramMap.add(name,sb.toString());
		    }
		}
		DBEncrypt crypt = new DBEncrypt();
		String t_url=crypt.dCode(companyurl)+url;
		log.error("---commonTransmit 调用参数---- url:"+t_url+"---Map:"+paramMap.toString());
		@SuppressWarnings("unchecked")
		Map<String,Object> result=restTemplate.postForObject(t_url, paramMap, Map.class);
		log.error("---commonTransmit 返回参数---- url:"+t_url+"---back--size:" + (result!=null ? result.size():"0"));

		return result;
	}
	
	/**
	 * 钉钉小程序获取统计接口，并根据访问的来源是内网或者外网返回正确的URL
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getStaticUrl")
	@ResponseBody
	public Map<String,String> getStaticUrl(String url, String params, String data){
		
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		if(StringUtil.isNotBlank(data)) {
			@SuppressWarnings("unchecked")
			List<Map<String,String>> list= JSONArray.parseObject(data,List.class);
			for(int i=0;i<list.size();i++) {
				paramMap.add((String) list.get(i).get("title"), list.get(i).get("value"));
			}
		}
		//判断url是否为空，若为null则返回错误页面
		if(StringUtil.isBlank(url)) {
			return null;
		}
		DBEncrypt crypt = new DBEncrypt();
		String t_url=crypt.dCode(companyurl)+url;
        log.error("getStaticUrl--t_url="+t_url);
		@SuppressWarnings("unchecked")
		Map<String,Object> result=restTemplate.postForObject(t_url, paramMap, Map.class);
		
		String urlString;
		//如果为公司的地址，则返回内网地址
		//if("101.68.81.90".equals(request.getRemoteAddr()) || "124.160.226.76".equals(request.getRemoteAddr()) || "60.191.45.242".equals(request.getRemoteAddr())) {
			urlString = (String)result.get("interurl");
			//如果有参数，则把参数加到地址后面
			if(StringUtil.isNotBlank(params)) {
				urlString = urlString + params + "?netflg=inter";
			}
	//	} 
			/*
			 * else { //返回外网地址 urlString = (String)result.get("url"); //如果有参数，则把参数加到地址后面
			 * if(StringUtil.isNotBlank(params)) { urlString = urlString + params +
			 * "?netflg="; } }
			 */
		
		Map<String,String> res = new HashMap<>();
		res.put("url", urlString);
		
		return res;
	}
	
	/**
	 * 获取钉钉小程序版本号
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(){
	    Map<String,Object> map= new HashMap<>();
	    XtszDto xtszDto = new XtszDto();
	    xtszDto.setSzlb(GlobalString.MATRIDX_DINGTALK_VERSION);
	    xtszDto = xtszService.getDto(xtszDto);
	    map.put("version", xtszDto == null?null:xtszDto.getSzz());
	    return map;
	}
	
	/**
	 * 小程序获取临时文件路径并保存到正式文件夹
	 * @return
	 */
	@RequestMapping("/getFileAddress")
	@ResponseBody
	public String getFileAddress(HttpServletRequest request) {
		log.error("开始获取临时文件地址!");
		String fjids = request.getParameter("fjids");
//		String ywid = request.getParameter("ywid");
		String[] fjidArr = fjids.split(",");
		List<String> fjidList = Arrays.asList(fjidArr);
		List<FjcfbModel> fjcfblist = new ArrayList<>();
		if(fjidList!=null && fjidList.size()>0) {
			for(int i=0;i<fjidList.size();i++) {
				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjidList.get(i));
				//如果文件信息不存在，则返回错误
				if(mFile != null) {
					FjcfbModel t_fjcfbModel = new FjcfbModel();
					t_fjcfbModel.setFjid(fjidList.get(i));
					//文件全路径
					String wjlj = (String)mFile.get("wjlj");
					//文件路径
					String pathString = (String)mFile.get("fwjlj");
					//文件名
					String wjm = (String)mFile.get("wjm");
					//分文件名
					String fwjm = (String)mFile.get("fwjm");
					//业务类型
					String ywlx = (String)mFile.get("ywlx");
					//文件路径
					String t_wjlj=wjlj.replace(tempFilePath, "");
					String t_fwjlj=pathString.replace(tempFilePath, "");
					log.error("文件全路径:"+wjlj+",文件路径:"+pathString+",文件名:"+wjm+",分文件名:"+fwjm+",业务类型:"+ywlx);
					DBEncrypt crypt = new DBEncrypt();
					t_fjcfbModel.setWjlj(crypt.eCode(tempFilePath+t_wjlj));
					t_fjcfbModel.setWjm(wjm);
					t_fjcfbModel.setFwjlj(crypt.eCode(pathString));
					t_fjcfbModel.setFwjm(fwjm);
					t_fjcfbModel.setYwlx(ywlx);
					t_fjcfbModel.setXwjlj(t_wjlj);//存放剪切后的文件路径
					t_fjcfbModel.setXfwjlj(crypt.eCode(t_fwjlj));//存放剪切后的分文件路径
					fjcfblist.add(t_fjcfbModel);
//					if(fjidList != null && fjidList.size()>0){
//						for (int j = 0; j < fjidList.size(); j++) {
//							boolean saveFile = fjcfbService.save2RealFile(fjidList.get(j),ywid);
//							if(!saveFile)
//								 throw new BusinessException("msg","保存附件信息失败！");
//						}
//					}
				}
			}
		}
		return JSONObject.toJSONString(fjcfblist);
	}
}
