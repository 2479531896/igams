package com.matridx.igams.detection.molecule.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.PrintEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;

import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.detection.molecule.dao.entities.CsmxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.service.impl.AliHealthGetOrder;
import com.matridx.igams.detection.molecule.service.svcinterface.ICsmxxService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.igams.detection.molecule.util.AliHealthOrderThread;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.taobao.api.internal.spi.CheckResult;
import com.taobao.api.internal.spi.SpiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ws")
public class DetectionWsController extends BaseController {

	@Autowired
	IFzjcxxService fzjcxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.applicationurl:}")
	private String address;
	@Value("${matridx.alihealth.appSecret:}")
	private String alihealthappSecret;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ICommonService commonService;
	@Autowired
	ICsmxxService csmxxService;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IYyxxService yyxxService;
	private byte[] bytes = null;
	private final Logger log = LoggerFactory.getLogger(DetectionWsController.class);

	/**
	 * 报告查询
	 */
	@RequestMapping("/detection/outcome")
	@ResponseBody
	public ModelAndView outcome(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/sampleAccept/outcome");
		FzjcxxDto fzjcxxDtoInfo  = null;
//		List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
		try {
			fzjcxxDtoInfo = fzjcxxService.getSampleAcceptInfo(fzjcxxDto);
//			fzjcxxDtos = fzjcxxService.getSampleAcceptInfoList(fzjcxxDto);
//			if (fzjcxxDtoInfo!=null) {
//				if ((null != fzjcxxDtos && fzjcxxDtos.size() > 1) || fzjcxxDtoInfo.getBbzbh().getBytes().length > 12) {
//					fzjcxxDtoInfo.setXm("混检");
//					fzjcxxDtoInfo.setXb("");
//					fzjcxxDtoInfo.setNl("");
//				}
//			}
		} catch (BusinessException e) {
			mav.addObject("message", e);
		}
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("fzjcxxDtoInfo", fzjcxxDtoInfo);
		return mav;
	}

	/**
	 * 异步获取基础数据子类别
	 */
	@RequestMapping(value ="verification/pagedataAnsyGetJcsjListAndJl")
	@ResponseBody
	public Map<String,Object> ansyGetJcsjListAndJl(JcsjDto jcsjDto){
		Map<String,Object> map= new HashMap<>();
		List<JcsjDto> jcsjDtos = jcsjService.getJcsjDtoListAndJl(jcsjDto);
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_RESULT});
		List<JcsjDto> jcsjDtoList = jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()); //报告结果
		map.put("jcsjDtos",jcsjDtos);
		map.put("jcsjDtoList",jcsjDtoList);
		return map;
	}

	/**
	 * 测试
	 */
	@RequestMapping(value="/cs")
	@ResponseBody
	public void cs(HttpServletResponse response){
		byte[] buffer = bytes;
		OutputStream os = null; //输出流
		try {

			//设置Content-Disposition
			//response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fjcfbModel.getWjm(), "UTF-8"));
			//指明为流
			//response.setContentType("application/octet-stream; charset=utf-8");
			os = response.getOutputStream();
			os.write(buffer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}finally {
			try {
				if(os!=null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
		}
	}
	/**
	 * 测试
	 */
	@RequestMapping(value="/csdk")
	@ResponseBody
	public String csdk(@RequestParam(required=false)MultipartFile file) {
		try {
			bytes = file.getBytes();
			return "OK";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "FAIL";
	}
	/**
	 * 打印机调用
	 */
	@RequestMapping(value="/getInfoByNbbhOrYbbh")
	@ResponseBody
	public Map<String,String> getInfoByNbbhOrYbbh(HttpServletRequest request) {
		Map<String,String> map= new HashMap<>();
		String str=request.getParameter("nbbm");
		log.error("nbbm:" + str);
		String type=request.getParameter("type");
		if (StringUtil.isBlank(type)){
			type =PrintEnum.PRINT_XGLR.getCode();
		}
//		String str = nbbm;
		if(StringUtil.isNotBlank(str)) {
			FzjcxxDto fzjcxxDto=new FzjcxxDto();
//			fzjcxxDto.setNbbh(str);
			fzjcxxDto.setBbzbh(str);
			FzjcxxDto fzjcxxDtoInfo;
//			List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
//			try {
//				fzjcxxDtoInfo = fzjcxxService.getInfoByNbbh(fzjcxxDto);
//				if (fzjcxxDtoInfo!=null){
//					map.put("status", "success");
//					map.put("type", PrintEnum.PRINT_XGQY.getCode());
//					map.put("ybbh", fzjcxxDtoInfo.getYbbh());
//					map.put("nbbh", fzjcxxDtoInfo.getNbbh());
//					if (StringUtil.isNotBlank(fzjcxxDtoInfo.getSyh())){
//						String syh = fzjcxxDtoInfo.getSyh();
//						String[] syhs = syh.split("-");
//						map.put("syh", syhs[1]);
//					}
//				}
//			} catch (BusinessException e) {
			try {
				fzjcxxDtoInfo = fzjcxxService.getSampleAcceptInfo(fzjcxxDto);
//				fzjcxxDtos = fzjcxxService.getSampleAcceptInfoList(fzjcxxDto);
				if (fzjcxxDtoInfo!=null){
					map.put("status", "success");
					map.put("ybbh", type.equals(PrintEnum.PRINT_XGLR.getCode())?fzjcxxDtoInfo.getBbzbh():fzjcxxDtoInfo.getYbbh());
					map.put("nbbh", fzjcxxDtoInfo.getYbbh());
//					if ((null != fzjcxxDtos && fzjcxxDtos.size()> 1)||fzjcxxDtoInfo.getBbzbh().getBytes().length>12){
//						map.put("xm", "混检");
//						map.put("xb", "");
//					}else{
						map.put("xm", StringUtil.isNotBlank(fzjcxxDtoInfo.getXm())?fzjcxxDtoInfo.getXm():fzjcxxDtoInfo.getYblxmc());
						map.put("xb", StringUtil.isNotBlank(fzjcxxDtoInfo.getXb())?fzjcxxDtoInfo.getXb():"");
//					}

					map.put("type", type);
					map.put("syh", StringUtil.isNotBlank(fzjcxxDtoInfo.getSyh())?fzjcxxDtoInfo.getSyh():"");
//						otherUrl = "http://172.17.53.135:8086/ws/detection/outcome";
					map.put("url", address+"/ws/detection/outcome?bbzbh="+fzjcxxDtoInfo.getBbzbh()+"&ybbh="+fzjcxxDtoInfo.getYbbh());
				}
			} catch (BusinessException ex) {
				map.put("status","fail");
				map.put("message","查询失败！");
			}
//			}

		}else{
			map.put("status","fail");
			map.put("message","查询失败！");
		}
		return map;
	}

	//保存预约--阿里健康
	@RequestMapping(value="/saveDetAppointmentInfo")
	@ResponseBody
	public String saveDetAppointmentInfo(HttpServletRequest request) {
		String ipAddress = fzjcxxService.getIpAddress(request);
		CheckResult checkSign = null;  //这里执行验签逻辑
		String result = null;
		try {
			checkSign = SpiUtils.checkSign(request, alihealthappSecret);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (checkSign != null) {
			if(!checkSign.isSuccess()) {
				log.error("验签失败 IP: "+ipAddress + " URL:" + request.getRequestURL().toString());
				Map<String, Object> map = new HashMap<>();
				//如果验签失败则需要返回 验签失败的结果，并且需要和配置对应的上，系统才认为是验签成功
				//按验签失败回应示例
				map.put("resultStatus","UNKNOWN");
				map.put("resultCode","sign-check-failure");
				result=JSON.toJSONString(map);
			}else{
				String appointmentInfo=checkSign.getRequestBody();
				String platform = "alihealth";
				result=fzjcxxService.saveDetAppointmentInfo(ipAddress,platform,appointmentInfo);
			}
		}
//		String appointmentInfo=request.getParameter("appointmentInfo");
//		result=fzjcxxService.saveDetAppointmentInfo(ipAddress,"alihealth",appointmentInfo);
		JSONObject jsonObject = JSONObject.parseObject(result);
		String isvReservationRecordId = jsonObject.getString("isvReservationRecordId");
		AliHealthGetOrder aliHealthGetOrder = new AliHealthGetOrder();
		aliHealthGetOrder.init(isvReservationRecordId,fzjcxxService,amqpTempl);
		AliHealthOrderThread aliHealthOrderThread = new AliHealthOrderThread(aliHealthGetOrder);
		aliHealthOrderThread.start();
		return result;
	}

	//保存预约--橄榄枝健康
	@RequestMapping(value="/saveDetAppointmentInfoByGlzhealth")
	@ResponseBody
	public String saveDetAppointmentInfoByGlzhealth(HttpServletRequest request) {
		String ipAddress = fzjcxxService.getIpAddress(request);
		String result;
		String sign = request.getParameter("sign");
		String organ = request.getParameter("organ");
		Map<String, Object> securityMap = commonService.checkSignUnBack(organ, sign, request);
		if(!"0".equals(securityMap.get("errorCode"))){
			log.error("验签失败 IP: "+ipAddress + " URL:" + request.getRequestURL().toString());
			log.error((String) securityMap.get("errorCode"));
			//如果验签失败则需要返回 验签失败的结果，并且需要和配置对应的上，系统才认为是验签成功
			//按验签失败回应示例
			securityMap.put("resultStatus","UNKNOWN");
			securityMap.put("resultCode","sign-check-failure");
			result=JSON.toJSONString(securityMap);
		}else{
			String appointmentInfo=request.getParameter("appointmentInfo");
			String platform = "glzhealth";
			result=fzjcxxService.saveDetAppointmentInfo(ipAddress,platform,appointmentInfo);
		}
		return result;
	}

	//取消预约(包含修改)--阿里健康
	@RequestMapping(value="/cancelDetAppointmentInfo")
	@ResponseBody
	public String cancelDetAppointmentInfo(HttpServletRequest request){
		String ipAddress = fzjcxxService.getIpAddress(request);
		CheckResult checkSign = null;  //这里执行验签逻辑
		String result = null;
		try {
			checkSign = SpiUtils.checkSign(request, alihealthappSecret);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (checkSign != null) {
			if(!checkSign.isSuccess()) {
				log.error("验签失败 IP: "+ipAddress + " URL:" + request.getRequestURL().toString());
				Map<String, Object> map = new HashMap<>();
				//如果验签失败则需要返回 验签失败的结果，并且需要和配置对应的上，系统才认为是验签成功
				//按验签失败回应示例
				map.put("resultStatus","UNKNOWN");
				map.put("resultCode","sign-check-failure");
				result=JSON.toJSONString(map);
			}else{
				String appointmentInfo=checkSign.getRequestBody();
				String platform = "alihealth";
				result=fzjcxxService.cancelDetAppointmentInfo(ipAddress,platform,appointmentInfo);
			}
		}
//		String appointmentInfo=request.getParameter("appointmentInfo");
//		result=fzjcxxService.cancelDetAppointmentInfo(ipAddress,appointmentInfo);
		return result;
	}

	//取消预约(包含修改)--橄榄枝健康
	@RequestMapping(value="/cancelDetAppointmentInfoByGlzhealth")
	@ResponseBody
	public String cancelDetAppointmentInfoByGlzhealth(HttpServletRequest request){
		String ipAddress = fzjcxxService.getIpAddress(request);
		String result;
		String sign = request.getParameter("sign");
		String organ = request.getParameter("organ");
		Map<String, Object> securityMap = commonService.checkSignUnBack(organ, sign, request);
		if(!"0".equals(securityMap.get("errorCode"))) {
			log.error("验签失败 IP: "+ipAddress + " URL:" + request.getRequestURL().toString());
			log.error((String) securityMap.get("errorCode"));
			//如果验签失败则需要返回 验签失败的结果，并且需要和配置对应的上，系统才认为是验签成功
			//按验签失败回应示例
			securityMap.put("resultStatus","UNKNOWN");
			securityMap.put("resultCode","sign-check-failure");
			result=JSON.toJSONString(securityMap);
		}
		else{
			String appointmentInfo=request.getParameter("appointmentInfo");
			String platform = "glzhealth";
			result=fzjcxxService.cancelDetAppointmentInfo(ipAddress,platform,appointmentInfo);
		}
		return result;
	}

	/**
	 * 扫码获取token
	 */
	@RequestMapping("/detection/checkToken")
	@ResponseBody
	public Map<String, Object> checkFzjcToken(HttpServletRequest request) {
		String csmid = request.getParameter("csmid");
		String sign = request.getParameter("sign");
		String token_param = request.getParameter("access_token");
		Map<String,Object> resmap = new HashMap<>();
		Map<String,Object> csmMap;
		/*验证sign================================================================*/
		//验证场所码传入的csmid和加密csmid是否相等，相等验证通过，不等验证不通过
		boolean isok1 = checkSign(csmid, sign);
		/*验证id================================================================*/
		//取场所码的csmid，先去redis获取场所码信息，若redis无则取DB取，DB取的数据判断有效日期，未过期则存一份到redis
		csmMap = checkCsmid(csmid);
		resmap.put("cydDto", csmMap != null ? csmMap.get("cydDto") : null);
		resmap.put("sjdwDto", csmMap != null ? csmMap.get("sjdwDto") : null);
		resmap.put("jcdwDto", csmMap != null ? csmMap.get("jcdwDto") : null);
		resmap.put("csmxx", csmMap != null ? csmMap.get("csmxx") : null);
		if ( (csmMap != null ? csmMap.get("csmxx") : null) ==null ){
			//场所码失效或不存在改场所码
			resmap.put("status",  "overdue");
			return resmap;
		}
		/*验证yhid================================================================*/
//首先使用yhid去redis取，redis无---login---取到token，存到redis的用户id和token的结构中
//					  redis有---看请求是否传入了token参数,请求无token传入---取redis中token
//													 请求有token传入---比较redis中的token和参数中的token，相等的，刷新token存redis并返回刷新token，不相等返回redis的token
		String token = checkYhid(csmMap, token_param);
		resmap.put("token",token);
		boolean isok3;
		isok3 = StringUtil.isNotBlank(token);
		resmap.put("status", isok1&&isok3 ? "success" : "fail");
		return resmap;
	}

	private boolean checkSign(String csmid, String sign) {
		DBEncrypt dbEncrypt=new DBEncrypt();
		if (StringUtil.isNotBlank(csmid) && StringUtil.isNotBlank(sign)){
			return csmid.equals(dbEncrypt.dCode(sign));
		}else {
			return false;
		}
	}

	private Map<String,Object> checkCsmid(String csmid) {
		try{
			DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String,Object> resmap = new HashMap<>();
			CsmxxDto csmxxRedis = JSON.parseObject(   (String) redisUtil.get("CSM:" + csmid),  CsmxxDto.class);
			CsmxxDto csmxx;
			User user = null;
			if (csmxxRedis != null ){
				csmxx = csmxxRedis;//redis取
			}else {
				CsmxxDto csm = new CsmxxDto();
				csm.setCsmid(csmid);
				CsmxxDto csmxxDB = csmxxService.getDto(csm);
				user = new User();
				user.setYhid(csmxxDB.getYhid());
				user = commonService.getUserInfoById(user);//使用yhid获取user，为了后续重登录的yhm信息
				//redisUtil.set("CSM:"+ csmxxDB.getCsmid(), JSONObject.toJSONString(csmxxDB) ,-1);
				csmxx = csmxxDB;//数据库取
				//比较场所码信息的有效期和当前时间
				String yxrqStr = DateUtils.getCustomFomratCurrentDate(csmxx.getYxrq());
				String nowStr = DateUtils.format(System.currentTimeMillis());
				Date yxrqDate = dfs.parse(yxrqStr);
				Date nowDate = dfs.parse(nowStr);
				long diff = (yxrqDate.getTime() - nowDate.getTime()) / (1000 * 60);//min
				if ( 0 < diff && diff < 6*60 ){
					redisUtil.set("CSM:"+ csmxxDB.getCsmid(), JSONObject.toJSONString(csmxxDB) ,diff*60);
				}else if ( diff > 6*60 ){
					redisUtil.set("CSM:"+ csmxxDB.getCsmid(), JSONObject.toJSONString(csmxxDB) ,21600);
				}else{
//						Map<String, String>  map = commonService.turnToSpringSecurityLogin(user.getYhm(),"q11");//
//						redisUtil.set("CSM_YHIDTOKEN:"+ csmxx.getYhid(), map.get("access_token") ,-1);//存入redis的yhid和token表中
					//diff<0已过期，将csmxx置为null返回
					log.error("checkCsmid从比对场所码有效时间小于当前系统时间，场所码已经失效，csmid为"+csmid);
					resmap.put("csmxx",null);
					return resmap;
				}
			}
			JcsjDto cydDto = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.COLLECT_SAMPLES, csmxx.getCyd());//采样点
			YyxxDto sjdwDto = yyxxService.getDtoById(csmxx.getSjdw());
			JcsjDto jcdwDto = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECTION_UNIT, csmxx.getJcdw());//检测单位，杭州实验室
			resmap.put("cydDto",cydDto);
			resmap.put("sjdwDto",sjdwDto);
			resmap.put("jcdwDto",jcdwDto);
			resmap.put("yhid",csmxx.getYhid());
			resmap.put("user",user);
			resmap.put("csmxx",csmxx);
			return resmap;
		}catch (Exception e){
			log.error("checkCsmid报错 "+e.getMessage());
			return null;
		}
	}

	private String checkYhid(Map<String,Object> csmmap , String token_param) {
		CsmxxDto csmxx = (CsmxxDto) csmmap.get("csmxx");
		User user = (User) csmmap.get("user");
		if (csmxx != null){
			//redis中维护xtyh表，key为yhid
			String token_redis =   (String) redisUtil.get("CSM_YHIDTOKEN:" + csmxx.getYhid()) ;
			if (StringUtil.isBlank(token_redis)){
				//redis中根据yhid取不到token，则login获取toke,并将yhid作为key，token作为value存入redis中
				if (user == null){
					user = new User();
					user.setYhid(csmxx.getYhid());
					user = commonService.getUserInfoById(user);//使用yhid获取user，为了后续重登录的yhm信息
				}
				Map<String, String>  map = commonService.turnToSpringSecurityLogin(user.getYhm(),user.getYhm());
				redisUtil.set("CSM_YHIDTOKEN:"+ csmxx.getYhid(), map.get("access_token") ,21600);//存入redis的yhid和token表中
				return map.get("access_token");
			}else {
				//redis中根据yhid取到token，看参数是否有token，区分请求是钉钉的还是微信的,微信无token过来，钉钉有token过来
				//token_param = request.getParameter("token");
				if (StringUtil.isBlank(token_param)){
					return token_redis;
				}else {
					//比较redis和参数中的token
					if (token_redis.equals(token_param)){
						if (user == null){
							user = new User();
							user.setYhid(csmxx.getYhid());
							user = commonService.getUserInfoById(user);//使用yhid获取user，为了后续重登录的yhm信息
						}
						Map<String, String>  map = commonService.turnToSpringSecurityLogin(user.getYhm(),user.getYhm());
						redisUtil.set("CSM_YHIDTOKEN:"+ csmxx.getYhid(), map.get("access_token") ,21600);//存入redis的yhid和token表中
						return map.get("access_token");
					}
					else
						return token_redis;
				}
			}
		}else {
			return null;
		}
	}

}
