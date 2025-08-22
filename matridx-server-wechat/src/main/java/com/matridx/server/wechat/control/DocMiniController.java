package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjjcxmDto;
import com.matridx.server.wechat.dao.entities.SjkjxxDto;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjqqjcDto;
import com.matridx.server.wechat.dao.entities.SjsyglDto;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjysxxDto;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.server.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.server.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.server.wechat.service.svcinterface.ISjkjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjnyxService;
import com.matridx.server.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.server.wechat.service.svcinterface.ISjsyglService;
import com.matridx.server.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.server.wechat.service.svcinterface.ISjysxxService;
import com.matridx.server.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class DocMiniController extends BaseController {

	@Autowired
	ISjxxService sjxxService;
	@Autowired
	ISjwzxxService sjwzxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISjnyxService sjnyxService;
	@Autowired
	ISjbgsmService sjbgsmService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	ICommonService commonService;
	@Autowired
	ISjxxjgService sjxxjgService;
	@Autowired
	ISjdwxxService sjdwxxService;
	@Autowired
	ISjkjxxService sjkjxxService;
	@Autowired
	ISjysxxService sjysxxService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ISjbgsmService sjbgsmservice;
	@Autowired
	ISjzmjgService sjzmjgService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.miniprogram.api:}")
	private String api;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Autowired
	IWbcxService wbcxService;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	@Autowired
	private ISjqqjcService sjqqjcService;
	@Autowired
	IDxglService dxglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ISjjcxmService sjjcxmService;
	@Autowired
	ISjsyglService sjsyglService;
	
	private Logger logger = LoggerFactory.getLogger(DocMiniController.class);
	
	/*------------------------------------------  登录   -------------------------------------------------*/
	
	/**
	 * 获取小程序当前版本号
	 * @return
	 */
	@RequestMapping("/miniprogram/getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(){
		Map<String,Object> map=new HashMap<>();
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.MATRIDX_WECAHT_VERSION);
		xtszDto = xtszService.getDto(xtszDto);
		map.put("version", xtszDto == null?null:xtszDto.getSzz());
		return map;
	}
	
	/**
	 * 小程序登录获取openid
	 * @param wxyhDto
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/miniprogram/login")
	@ResponseBody
	public Map<String, Object> login(WxyhDto wxyhDto, String code){
		Map<String,Object> map = new HashMap<>();
		//获取身份类型
		Map<String, String> identitymap = wxyhService.getIdentityTypeMap();
		map.put("identitymap", identitymap);
		if(StringUtil.isBlank(code)){
			map.put("status", "fail");
			return map;
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(ProgramCodeEnum.MINIINSP.getCode());
		wbcxDto = wbcxService.getDto(wbcxDto);
		if(wbcxDto == null){
			logger.error("未找到外部编码为 "+ProgramCodeEnum.MINIINSP.getCode()+" 的外部程序信息！");
			map.put("status", "fail");
			return map;
		}
		DBEncrypt dbEncrypt = new DBEncrypt();
		String login_url = new StringBuffer(api).append("?appid=").append(dbEncrypt.dCode(wbcxDto.getAppid()))
				.append("&secret=").append(dbEncrypt.dCode(wbcxDto.getSecret())).append("&js_code=").append(code).append("&grant_type=authorization_code").toString();
		logger.error("微信信息请求地址docmini："+login_url);
		String s_login_return = restTemplate.getForObject(login_url, String.class);
		logger.error("微信信息docmini："+s_login_return);
		JSONObject loginObject = JSONObject.parseObject(s_login_return);
		//有错误直接返回
		Integer errorString = (Integer)loginObject.get("errcode");
		if(errorString != null){
			map.put("status", "fail");
			map.put("errorString", errorString);
			return map;
		}
		map.put("appid", dbEncrypt.dCode(wbcxDto.getAppid()));
		String openid = (String)loginObject.get("openid");
		String unionid = (String)loginObject.get("unionid");
		map.put("openid", openid);
		map.put("unionid", unionid);
		WxyhDto t_wxyhDto = wxyhService.getDtoById(openid);
		if(t_wxyhDto == null){
			wxyhDto.setWxid(openid);
			wxyhDto.setUnionid(unionid);
			wxyhDto.setLrry(openid);
			wxyhDto.setGzpt(wbcxDto.getWbcxid());
			// 新增用户信息
			boolean result = wxyhService.insertDto(wxyhDto);
			if (!result){
				map.put("status", "fail");
				return map;
			}
			wxyhDto.setGzptmc(wbcxDto.getGzpt());
			wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
			String jsonString = JSONObject.toJSONString(wxyhDto);
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_USER_MOD.getCode(), jsonString);
			map.put("wxyhDto", wxyhDto);
		} else if (StringUtil.isBlank(t_wxyhDto.getUnionid())) {
			t_wxyhDto.setUnionid(unionid);
			wxyhService.update(t_wxyhDto);
			t_wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
			String jsonString = JSONObject.toJSONString(t_wxyhDto);
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_USER_MOD.getCode(), jsonString);
			map.put("wxyhDto", t_wxyhDto);
		} else{
			map.put("wxyhDto", t_wxyhDto);
		}
		map.put("status", "success");
		return map;
	}
	
	/**
	 * 获取加密信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getMiniInfo")
	@ResponseBody
	public Map<String, Object> getInspectFlg(HttpServletRequest request){
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(ProgramCodeEnum.MINIINSP.getCode());
		wbcxDto = wbcxService.getDto(wbcxDto);
		DBEncrypt dbEncrypt = new DBEncrypt();
		Map<String,Object> map = new HashMap<>();
        StringBuilder urlPath = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session");
        urlPath.append(String.format("?appid=%s", dbEncrypt.dCode(wbcxDto.getAppid())));
        urlPath.append(String.format("&secret=%s", dbEncrypt.dCode(wbcxDto.getSecret())));
        urlPath.append(String.format("&js_code=%s", request.getParameter("code")));
        urlPath.append(String.format("&grant_type=%s", "authorization_code")); // 固定值
		String str = restTemplate.postForObject(urlPath.toString(), null,String.class);
		logger.error("微信小程序登陆:"+str);
		String openid = (String)JSONObject.parseObject(str).get("openid");
		String session_key = (String)JSONObject.parseObject(str).get("session_key");
		map.put("appid", dbEncrypt.dCode(wbcxDto.getAppid()));
		map.put("openid", openid);
		map.put("status", StringUtil.isNotBlank(session_key)?"success":"fail");
		return map;
	}
	/**
	 * 根据微信提供的code获取手机号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getuserphonenumber")
	@ResponseBody
	public Map<String, Object> getuserphonenumberByWechat(HttpServletRequest request){
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setWbcxdm(ProgramCodeEnum.MINIINSP.getCode());
		wbcxDto = wbcxService.getDto(wbcxDto);
		DBEncrypt dbEncrypt = new DBEncrypt();
		Map<String,Object> map = new HashMap<>();
        StringBuilder urlPath = new StringBuilder("https://api.weixin.qq.com/wxa/business/getuserphonenumber");
		wbcxDto.setToken(dbEncrypt.eCode(WeChatUtils.getToken(restTemplate,"client_credential",null,dbEncrypt.dCode(wbcxDto.getAppid()),dbEncrypt.dCode(wbcxDto.getSecret()),false,ProgramCodeEnum.MINIINSP.getCode(),redisUtil)));
		urlPath.append(String.format("?access_token=%s", dbEncrypt.dCode(wbcxDto.getToken())));
		HashMap<String, Object> codeMap = new HashMap<>();
		codeMap.put("code", request.getParameter("code"));
		//用HttpEntity封装整个请求报文
		HttpEntity<HashMap<String, Object>> body = new HttpEntity<>(codeMap);
		String str = restTemplate.postForObject(urlPath.toString(), body,String.class);
		logger.error("微信小程序获取手机号:"+str);
		JSONObject result = JSONObject.parseObject(str);
		if ("0".equals(result.get("errcode").toString())){
			map.put("status", "success");
			map.put("phone", (String)(JSONObject.parseObject(JSONObject.toJSONString(result.get("phone_info"))).get("phoneNumber")));
			return map;
		}
		map.put("status", "fail");
		map.put("message", (String)result.get("errmsg"));
		return map;
	}
	
	/*------------------------------------------  个人信息页面   -------------------------------------------------*/
	
	/**
	 * 医生小程序修改用户信息(手机绑定)
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/updateDocUserInfo")
	@ResponseBody
	public Map<String, Object> updateDocUserInfo(WxyhDto wxyhDto){
		WbcxDto wbcxDto = new WbcxDto();
		if (StringUtil.isNotBlank(wxyhDto.getWbcxdm())){
			wbcxDto.setWbcxdm(wxyhDto.getWbcxdm());
		}else {
			wxyhDto.setWbcxdm(ProgramCodeEnum.MINIINSP.getCode());
			wbcxDto.setWbcxdm(ProgramCodeEnum.MINIINSP.getCode());
		}
		wbcxDto = wbcxService.getDto(wbcxDto);
		wxyhDto.setGzpt(wbcxDto.getWbcxid());
		logger.error("访问updateDocUserInfo方法 --- wxid:  " + wxyhDto.getWxid() + " unionid: " + wxyhDto.getUnionid());
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess;
		try {
			if(StringUtil.isNotBlank(wxyhDto.getCskz1())){
//				WxyhDto t_wxyhDto = wxyhService.getDtoById(wxyhDto.getWxid());//t_wxyhDto未使用，故注释，2023.10.26
				Map<String, Object> checkMap = dxglService.checkCode(wxyhDto.getSj(), wxyhDto.getCskz1(), "300000");
				if (!"success".equals((String)checkMap.get("status"))){
					throw new BusinessException("msg", (String) checkMap.get("message"));
				}
			}
			isSuccess = wxyhService.update(wxyhDto);
			if(isSuccess){
				WxyhDto t_wxyhDto = wxyhService.getDtoById(wxyhDto.getWxid());
				String jsonString = JSONObject.toJSONString(t_wxyhDto);
				amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_USER_MOD.getCode(), jsonString);
			}

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		logger.error("updateDocUserInfo方法返回结果    ---  " + (isSuccess?"success":"fail"));
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 发送短信验证码
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping("/miniprogram/getSms")
	@ResponseBody
	public Map<String, Object> sendSms(WxyhDto wxyhDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess;
		if(StringUtil.isNoneBlank(wxyhDto.getCskz2()) && "13764520566".equals(wxyhDto.getCskz2())){
			isSuccess = true;
		}else{
			isSuccess = wxyhService.getSms(wxyhDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/*------------------------------------------  清单页面   -------------------------------------------------*/
	
	/**
	 * 送检清单页面初始化
	 * @return
	 */
	@RequestMapping(value="/miniprogram/initInsplist")
	@ResponseBody
	public Map<String, Object> initInsplist(){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> g_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.GENDER_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.PATHOGENY_TYPE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.WECGATSAMPLE_TYPE});
		//性别
		map.put("genderlist", g_jclist.get(BasicDataTypeEnum.GENDER_TYPE.getCode()));
		//检测项目
		map.put("detectlist", g_jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
		//快递类型
		map.put("sdlist", g_jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));
		//检测单位
		map.put("unitlist", g_jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		//标本类型
		map.put("samplelist", g_jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		//送检科室
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);
		return map;
	}
	
	/**
	 * 删除未提交送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/delInspection")
	@ResponseBody
	public Map<String, Object> delInspection(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = false;
		if(StringUtil.isNotBlank(sjxxDto.getSjid())){
			sjxxDto.setScry(sjxxDto.getWxid());
			isSuccess = sjxxService.delete(sjxxDto);
		}
		if(isSuccess){
			String jsonString = JSONObject.toJSONString(sjxxDto);
			RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.INSP_DEL.getCode() + jsonString);
		}
		map.put("sjxxDto", sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 医生小程序查询送检清单信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getDocMiniInsplist")
	@ResponseBody
	public Map<String, Object> getDocMiniInsplist(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjxxDto> sjxxDtos = sjxxService.getDocMiniInsplist(sjxxDto);
		map.put("sjxxDtos", sjxxDtos);
		return map;
	}
	
	/**
	 * 医生小程序送检清单查看送检结果信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/miniprogram/getDocReportView")
	public ModelAndView getDocReportView(SjxxDto sjxxDto){
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportView");
		SjxxDto sjxxjcbgDto = sjxxService.getDto(sjxxDto);
		if(sjxxjcbgDto != null&&StringUtil.isNotBlank(sjxxjcbgDto.getBgrq())) {
			mav=new ModelAndView("wechat/informed/wechat_view");
			sjxxjcbgDto.setSflx(sjxxDto.getSflx());
			if("1".equals(sjxxjcbgDto.getSjdwbj())) {
				sjxxjcbgDto.setSjdwmc(sjxxjcbgDto.getYymc()+"-"+sjxxjcbgDto.getSjdwmc());
			}
		    // 查询送检耐药性
		    List<SjnyxDto> sjnyx=sjnyxService.getNyxBySjid(sjxxjcbgDto);
		    
//			if(("Z6").equals(sjxxjcbgDto.getCskz1()) || ("Z12").equals(sjxxjcbgDto.getCskz1()) || ("Z18").equals(sjxxjcbgDto.getCskz1()) || ("F").equals(sjxxjcbgDto.getCskz1())) {
			if(("Z6").equals(sjxxjcbgDto.getCskz1()) || ("Z").equals(sjxxjcbgDto.getCskz1()) || ("F").equals(sjxxjcbgDto.getCskz1())) {
	        	List<SjzmjgDto> sjzmList;
	        	SjzmjgDto sjzmjgDto = new SjzmjgDto();
	        	sjzmjgDto.setSjid(sjxxjcbgDto.getSjid());
	        	sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
	        	mav.addObject("sjzmList", sjzmList);
	        	mav.addObject("KZCS",sjxxjcbgDto.getCskz1());
	        }
	        // 查询物种信息
		    List<SjwzxxDto> wzxx = sjxxService.selectWzxxBySjid(sjxxjcbgDto);
			if(wzxx!=null && wzxx.size()>0) {
				String xpxx=wzxx.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
				mav.addObject("Xpxx", xpxx);
			}
	        //查看送检结果
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxjcbgDto.getSjid());
			fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			for (int i = 0; i < fjcfbDtos.size(); i++) {
				fjcfbDtos.get(i).setSign(commonService.getSign(fjcfbDtos.get(i).getFjid()));
			}
			mav.addObject("fjcfbDtos",fjcfbDtos);
			 //查看附件
		    fjcfbDto.setYwlxs(null);
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		    List<FjcfbDto> fjcfbDtos_fjs = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		    for (int i = 0; i < fjcfbDtos_fjs.size(); i++) {
		    	fjcfbDtos_fjs.get(i).setSign(commonService.getSign(fjcfbDtos_fjs.get(i).getFjid()));
			}
			SjbgsmDto sjbgsmdto = new SjbgsmDto();
			sjbgsmdto.setSjid(sjxxjcbgDto.getSjid());
			List<SjbgsmDto> sjbgsmxx = sjbgsmservice.selectSjbgBySjid(sjbgsmdto);
			
			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
			List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
			List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
			List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页
			if(jcxmlist!=null && jcxmlist.size()>0) {
				for(int i=0;i<jcxmlist.size();i++) {
					boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
					if(wzxx!=null && wzxx.size()>0) {
						for(int j=0;j<wzxx.size();j++) {
							if(wzxx.get(j).getJcxmid() != null && wzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
								wz_sftj=true;
								break;
							}
						}
					}
					if(wz_sftj) {
                        c_jcxmlist.add(jcxmlist.get(i));
                    }
					
					boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
					if(sjbgsmxx!=null && sjbgsmxx.size()>0) {
						for(int j=0;j<sjbgsmxx.size();j++) {
							if(sjbgsmxx.get(j).getJcxmid() != null && sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
								sftj=true;
								break;
							}
						}
					}
					if(fjcfbDtos!=null && fjcfbDtos.size()>0) {
						for(int j=0;j<fjcfbDtos.size();j++) {
							if(fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()))) {
								sftj=true;
								break;
							}
						}
					}
					if(sftj) {
                        t_jcxmlist.add(jcxmlist.get(i));
                    }
				}
			}
			
	        SjxxjgDto sjxxjgDto=new SjxxjgDto();
			sjxxjgDto.setSjid(sjxxjcbgDto.getSjid());
			List<SjxxjgDto> getJclxCount=sjxxjgService.getJclxCount(sjxxjgDto);
			
	        mav.addObject("SjxxjgList", getJclxCount);
			mav.addObject("sjbgsmList",sjbgsmxx);
		    mav.addObject("fjcfbDtos",fjcfbDtos);
		    mav.addObject("fjcfbDtos_fjs",fjcfbDtos_fjs);
			mav.addObject("DaobyYbbh",sjxxjcbgDto);
			mav.addObject("Sjwzxx", wzxx);
			mav.addObject("SjnyxDto", sjnyx);
	        mav.addObject("jcxmlist",t_jcxmlist);
	        mav.addObject("wzjcxmlist", c_jcxmlist);
	        
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("viewFlg","1");
		}else {
			if(sjxxjcbgDto == null) {
                sjxxjcbgDto = new SjxxDto();
            }
			mav.addObject("sjxxDto", sjxxjcbgDto);
		}
		DBEncrypt crypt = new DBEncrypt();
		mav.addObject("applicationurl", crypt.dCode(applicationurl));
		// 获取收费权限信息
		String sfqx = null;
		if(StringUtil.isNotBlank(sjxxDto.getWxid())) {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("wxid", sjxxDto.getWxid());
			String url = crypt.dCode(companyurl);
			@SuppressWarnings("unchecked")
			Map<String,String> map = restTemplate.postForObject(url+"/ws/dingtalk/getUser", paramMap, Map.class);
			if(map != null && StringUtil.isNotBlank(map.get("sfqx"))) {
                sfqx = map.get("sfqx");
            }
		}
		mav.addObject("sfqx", sfqx);
		String sfsj="0";
		List<SjsyglDto> sjsyglDtos = sjsyglService.getViewDetectData(sjxxjcbgDto.getSjid());
		if(sjsyglDtos!=null&&sjsyglDtos.size()>0){
			for(SjsyglDto dto:sjsyglDtos){
				if(StringUtil.isNotBlank(dto.getSjsj())){
					sfsj="1";
					break;
				}
			}
		}
		mav.addObject("sfsj", sfsj);
		mav.addObject("sjsyglDtos", sjsyglDtos);
		return mav;
	}


	/**
	 * 支付宝引导页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/informed/downloadGuide")
	public ModelAndView downloadGuide(HttpServletRequest request) {
		if (!commonService.checkSign(request.getParameter("sign"),request.getParameter("fjid"),request)){
			return commonService.jumpError();
		}
		ModelAndView mav = new ModelAndView("wechat/informed/downloadGuide");
		mav.addObject("fjid", request.getParameter("fjid"));
		mav.addObject("sign", commonService.getSign(request.getParameter("fjid")));
		mav.addObject("action", request.getParameter("action"));
		return mav;
	}
	/**
	 * 获取反馈信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/miniprogram/getDocFeedback")
	@ResponseBody
	public Map<String, Object>  getDocFeedback(SjxxDto sjxxDto) {
		Map<String,Object> map = new HashMap<>();
		SjxxDto t_sjxxDto=sjxxService.getDto(sjxxDto);
		t_sjxxDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		//查看附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(t_sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_FEEDBACK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("sjxxDto", t_sjxxDto);
		map.put("fjcfbDtos", fjcfbDtos);
		return map;
	}
	
	/**
	 * 保存临床反馈
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/miniprogram/saveFeedBack")
	@ResponseBody
	public Map<String,Object> saveFeedBack(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=sjxxService.updateFeedBack(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/*------------------------------------------  文件操作   -------------------------------------------------*/
	
	/**
	 * 附件下载（用于普通的文件下载使用）
	 * @param fjcfbDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/miniprogram/downloadDocFile")
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
	 * 附件下载（用于根据文件路径下载使用）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getPicturePreview")
	public String getPicturePreview(HttpServletResponse response,HttpServletRequest request){
		String filePath = request.getParameter("path");
		try {
			filePath = URLDecoder.decode(filePath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(StringUtil.isBlank(filePath)) {
            return "未获取到文件路径";
        }
		File file = new File(filePath);
		String agent = request.getHeader("user-agent");
		try {
			String wjm = file.getName();
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
	@RequestMapping(value = "/miniprogram/delDocFile")
	@ResponseBody
	public Map<String, Object> delDocFile(FjcfbDto fjcfbDto){
		fjcfbDto.setScry(fjcfbDto.getWxid());
		boolean isSuccess = fjcfbService.delFile(fjcfbDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/*------------------------------------------  统计页面   -------------------------------------------------*/
	
	/**
	 * 初始化统计页面
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/initStatis")
	@ResponseBody
	public Map<String, Object> initStatis(){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> s_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE});
		map.put("samplelist", s_jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		return map;
	}
	
	/**
	 * 送检物种类型统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/getSpeciesStatis")
	@ResponseBody
	public Map<String, Object> getSpeciesStatis(SjxxDto sjxxDto){
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			List<String> lrryList = sjysxxService.getLrrylist(sjxxDto.getWxid(), null);
			sjxxDto.setLrrys(lrryList);
		}
		List<SjwzxxDto> sjwzxxDtos = sjwzxxService.getSpeciesStatis(sjxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("sjwzxxDtos", sjwzxxDtos);
		return map;
	}
	
	/**
	 * 检测结果类型统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/getDetectionResultStatis")
	@ResponseBody
	public Map<String, Object> getDetectionResultStatis(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(sjxxDto.getWxid())){
			List<String> lrryList = sjysxxService.getLrrylist(sjxxDto.getWxid(), null);
			sjxxDto.setLrrys(lrryList);
		}
		List<Map<String, String>> sjxxDtos = sjxxService.getDetectionResultStatis(sjxxDto);
		map.put("sjxxDtos", sjxxDtos);
		return map;
	}
	
	/*------------------------------------------  送检页面   -------------------------------------------------*/
	
	/**
	 * 小程序送检第一页初始化
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/initInspectFirst")
	@ResponseBody
	public Map<String, Object> initInspectFirst(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> g_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.GENDER_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.PATHOGENY_TYPE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT});
		map.put("genderlist", g_jclist.get(BasicDataTypeEnum.GENDER_TYPE.getCode()));
		map.put("pathogenylist", g_jclist.get(BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));
		map.put("sdlist", g_jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));
		map.put("zmmddlist",g_jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		map.put("sjqflist",g_jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));
		map.put("kyxmlist",g_jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);
		if(StringUtil.isNotBlank(sjxxDto.getSjid())){
			sjxxDto = sjxxService.getDto(sjxxDto,0);
			if(sjxxDto == null) {
                sjxxDto = new SjxxDto();
            }
			//根据文件ID查询附件表信息
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			map.put("fjcfbDtos",fjcfbDtos);
		}else{
			// 查询最后一次送检数据    mod 因线上问题，取消查询 2021-06-15 li
			/*sjxxDto.setLrry(sjxxDto.getWxid());
			SjxxDto s_sjxxDto = sjxxService.getLastInfo(sjxxDto);
			if(StringUtil.isNotBlank(s_sjxxDto.getSjid()) && !"80".equals(s_sjxxDto.getZt())) {
				String wxid = sjxxDto.getWxid();
				sjxxDto = sjxxService.getDto(s_sjxxDto);
				sjxxDto.setWxid(wxid);
				//根据文件ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(sjxxDto.getSjid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				map.put("fjcfbDtos", fjcfbDtos);
			}else {
				//查询快捷信息
				if(!StringUtil.isBlank(sjxxDto.getWxid())){
					SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
					if(sjkjxxDto != null){
						SjxxDto t_sjxxDto = new SjxxDto();
						t_sjxxDto.setSjdw(sjkjxxDto.getSjdw());
						t_sjxxDto.setKs(sjkjxxDto.getKs());
						t_sjxxDto.setQtks(sjkjxxDto.getQtks());
						t_sjxxDto.setSjys(sjkjxxDto.getSjys());
						t_sjxxDto.setYsdh(sjkjxxDto.getYsdh());
						t_sjxxDto.setJcdw(sjkjxxDto.getJcdw());
						t_sjxxDto.setWxid(sjxxDto.getWxid());
						sjxxDto = t_sjxxDto;
					}
				}
			}*/
			
			//查询快捷信息
			if(!StringUtil.isBlank(sjxxDto.getWxid())){
				SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
				if(sjkjxxDto != null){
					SjxxDto t_sjxxDto = new SjxxDto();
					t_sjxxDto.setSjdw(sjkjxxDto.getSjdw());
					t_sjxxDto.setKs(sjkjxxDto.getKs());
					t_sjxxDto.setQtks(sjkjxxDto.getQtks());
					t_sjxxDto.setSjys(sjkjxxDto.getSjys());
					t_sjxxDto.setYsdh(sjkjxxDto.getYsdh());
					t_sjxxDto.setJcdw(sjkjxxDto.getJcdw());
					t_sjxxDto.setWxid(sjxxDto.getWxid());
					sjxxDto = t_sjxxDto;
				}
			}
		}
		//根据标本类型cskz2限制检测项目
		JcsjDto jc_yblxDto = new JcsjDto();
		if(StringUtils.isNotBlank(sjxxDto.getYblx())) {
			jc_yblxDto = jcsjService.getDtoById(sjxxDto.getYblx());
			if(jc_yblxDto == null) {
                jc_yblxDto = new JcsjDto();
            }
		}
		List<JcsjDto> detectlist = g_jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_detectlist = new ArrayList<>();
		if(detectlist != null && detectlist.size() > 0) {
			for(int i = 0; i < detectlist.size(); i++) {
				if(StringUtils.isNotBlank(jc_yblxDto.getCskz2())) {
					if(jc_yblxDto.getCskz2().contains(detectlist.get(i).getCsdm())) {
						t_detectlist.add(detectlist.get(i));
					}
				}else {
					t_detectlist=detectlist;
				}
			}
		}
		map.put("detectlist", t_detectlist);
		//获取文件类型
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		map.put("sjxxDto", sjxxDto);
		map.put("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));
		map.put("subdetectlist", sjxxService.getSubDetect(sjxxDto.getJcxm()));
		return map;
	}


	/**
	 * 小程序送检第一页初始化
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/initInspReport")
	@ResponseBody
	public Map<String, Object> initInspReport(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String flag = request.getParameter("flag");
		if (StringUtil.isNotBlank(flag)){
			if ("ResFirst".equals(flag)){
				return null;
			}
		}
		//若有jcxmdms，说明有限制使用的jcxm，将除jcxmdms外的项目排除
		//若无，判断是否存在禁用检测项目disabledjcxmdms，将disabledjcxmdms中的项目排除
		if (StringUtil.isNotBlank(request.getParameter("jcxmdms"))){
			map.put("jcxmdms",request.getParameter("jcxmdms"));
		}else if(StringUtil.isNotBlank(request.getParameter("disabledjcxmdms"))){
			map.put("disabledjcxmdms",request.getParameter("disabledjcxmdms"));
		}
		if(StringUtil.isNotBlank(sjxxDto.getSjid())){
			//sjid不为空，根据sjid查询送检信息，修改
			//获取第一页送检信息
			sjxxDto = sjxxService.getDto(sjxxDto,0);
			if(sjxxDto == null) {
                sjxxDto = new SjxxDto();
            }
			//根据文件ID查询附件表信息
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			map.put("fjcfbDtos",fjcfbDtos);
		}else{
			//sjid为空，新增，查询快捷信息
			if(!StringUtil.isBlank(sjxxDto.getYhid())){
				SjkjxxDto sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getYhid());
				if(sjkjxxDto != null){
					SjxxDto t_sjxxDto = new SjxxDto();
					t_sjxxDto.setSjdw(sjkjxxDto.getSjdw());//送检单位
					t_sjxxDto.setKs(sjkjxxDto.getKs());//科室
					t_sjxxDto.setQtks(sjkjxxDto.getQtks());//报告显示科室
					t_sjxxDto.setSjys(sjkjxxDto.getSjys());//送检医生
					t_sjxxDto.setYsdh(sjkjxxDto.getYsdh());//医生电话
					t_sjxxDto.setJcdw(sjkjxxDto.getJcdw());//检测单位
					t_sjxxDto.setYhid(sjxxDto.getYhid());
					sjxxDto = t_sjxxDto;
				}
			}
		}
		//获取文件类型
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		map.put("sjxxDto", sjxxDto);
		map.put("samplelist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//基础数据：样本类型
		map.put("detectlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode()));//基础数据：检测项目
		map.put("detectsublist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode()));//基础数据：检测子项目
		map.put("genderlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.GENDER_TYPE.getCode()));//基础数据：性别
		map.put("pathogenylist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PATHOGENY_TYPE.getCode()));//基础数据：关注病原
		map.put("sdlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode()));//基础数据：快递类型
		map.put("unitlist", sjxxService.getDetectionUnit(sjxxDto.getDb()));//基础数据：检测单位（根据合作伙伴限制）
		map.put("divisionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//基础数据：送检区分
		map.put("kyxmlist",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));//基础数据：科研项目（立项编号）
		List<SjdwxxDto> sjdwlist = sjdwxxService.selectSjdwList();
		map.put("ksxxlist", sjdwlist);//基础数据：科室信息
		map.put("actionFlag", request.getParameter("actionFlag"));//操作标记：1：完善；其他：修改
		//筛选出列表显示的字段，加快列表显示
		screenClassColumns(request,map);
		return map;
	}
	/**
	 * 提取用户送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/extractUserInfo")
	@ResponseBody
	public Map<String, Object> extractUserInfo(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		try {
			SjxxDto t_sjxxDto = sjxxService.extractUserInfo(sjxxDto);
			if(t_sjxxDto != null){
				//根据文件ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(t_sjxxDto.getSjid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				map.put("fjcfbDtos",fjcfbDtos);
				t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				map.put("unitlist",  sjxxService.getDetectionUnit(t_sjxxDto.getDb()));
				map.put("subdetectlist", sjxxService.getSubDetect(t_sjxxDto.getJcxm()));
				//根据标本类型cskz2限制检测项目
				JcsjDto jc_yblxDto=new JcsjDto();
				if(StringUtils.isNotBlank(t_sjxxDto.getYblx())) {
                    jc_yblxDto=jcsjService.getDtoById(t_sjxxDto.getYblx());
                }
				Map<String, List<JcsjDto>> g_jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
				List<JcsjDto> detectlist = g_jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
				List<JcsjDto> t_detectlist = new ArrayList<>();
				if(detectlist != null && detectlist.size() > 0) {
					for(int i = 0; i < detectlist.size(); i++) {
						if(StringUtils.isNotBlank(jc_yblxDto.getCskz2())) {
							if(jc_yblxDto.getCskz2().contains(detectlist.get(i).getCsdm())) {
								t_detectlist.add(detectlist.get(i));
							}
						}else {
							t_detectlist=detectlist;
						}
					}
				}
				map.put("detectlist", t_detectlist);
			}
			map.put("sjxxDto", t_sjxxDto);
			map.put("status", t_sjxxDto != null?"success":"fail");
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 小程序送检第一页保存
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/saveInspectFirst")
	@ResponseBody
	public Map<String, Object> saveInspectFirst(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		logger.error("saveInspectFirst wxid:" + sjxxDto.getWxid() + " ddid:" + sjxxDto.getDdid());
		if(StringUtil.isBlank(sjxxDto.getWxid()) && StringUtil.isNotBlank(sjxxDto.getDdid())){
			sjxxDto.setWxid(sjxxDto.getDdid());
		}else if(StringUtil.isBlank(sjxxDto.getWxid()) && StringUtil.isBlank(sjxxDto.getDdid())) {
			logger.error("快捷支付未获取到相应的微信或者钉钉信息。");
			map.put("status", "fail");
			map.put("message", "没有获取到您的微信信息，请重新从公众号打开！");
			return map;
		}
		try {
			boolean isSuccess = sjxxService.addSaveReport(sjxxDto);
			if(isSuccess) {
				map.put("sjxxDto", sjxxDto);
				sjxxService.saveFile(sjxxDto);
				sjxxDto = sjxxService.getDto(sjxxDto);
				if(sjxxDto == null) {
                    sjxxDto = new SjxxDto();
                }
				//根据文件ID查询附件表信息
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(sjxxDto.getSjid());
				fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if ("ResFirst".equals(request.getParameter("flag"))){
					sjxxService.addSaveConsentComp(sjxxDto);
				}
				map.put("fjcfbDtos",fjcfbDtos);
				map.put("sjxxDto", sjxxDto);
				map.put("status", "success");
				map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
			}else{
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
		return map;
	}
	
	/**
	 * 小程序送检第二页初始化
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getInspectSecond")
	@ResponseBody
	public Map<String, Object> getInspectSecond(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> w_jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.CLINICAL_TYPE,BasicDataTypeEnum.INVOICE_APPLICATION});
		List<JcsjDto> samplelist=w_jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<JcsjDto> t_samplelist= new ArrayList<>();
		sjxxDto = sjxxService.getDto(sjxxDto,1);
		if(samplelist!=null && samplelist.size()>0) {
			JcsjDto jcsjDto=new JcsjDto();
			jcsjDto.setCsmc("无");
			t_samplelist.add(jcsjDto);
			//用到了检测项目
			List<SjjcxmDto> sjjcxmDtos=sjjcxmService.selectJcxmBySjid(sjxxDto);
			for(int i=0;i<samplelist.size();i++) {
				if(StringUtils.isNotBlank(samplelist.get(i).getCskz2())) {
					boolean isSuccess_t=true;
					if (!CollectionUtils.isEmpty(sjjcxmDtos)){
						for (SjjcxmDto sjjcxmDto:sjjcxmDtos){
							if (!samplelist.get(i).getCskz2().contains(sjjcxmDto.getCsdm())){
								isSuccess_t=false;
								break;
							}
						}
					}
					if (isSuccess_t){
						t_samplelist.add(samplelist.get(i));
					}
				}
			}
		}
		sjxxDto.setSjqqjcs(sjqqjcService.getJcz(sjxxDto.getSjid()));//查询前期检测项目；
		map.put("samplelist", t_samplelist);	
		map.put("clinicallist", w_jclist.get(BasicDataTypeEnum.CLINICAL_TYPE.getCode()));
		map.put("sfkplist",w_jclist.get(BasicDataTypeEnum.INVOICE_APPLICATION.getCode()));
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 小程序送检保存第二页信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/saveInspectSecond")
	@ResponseBody
	public Map<String, Object> saveInspectSecond(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjqqjcDto> sjqqjsDtos = getSjqqjcs(sjxxDto);
		if(sjqqjsDtos != null && sjqqjsDtos.size() > 0){
			sjxxDto.setSjqqjcs(sjqqjsDtos);
		}
		if(StringUtils.isNotBlank(sjxxDto.getWxid())) {
			sjxxDto.setLrry(sjxxDto.getWxid());
			sjxxDto.setXgry(sjxxDto.getWxid());
		}else {
			sjxxDto.setLrry(sjxxDto.getDdid());
			sjxxDto.setXgry(sjxxDto.getDdid());
		}
		boolean isSuccess = sjxxService.addSaveConsentBack(sjxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("sjxxDto", sjxxDto);
		SjxxDto sjxxDto1 = sjxxService.getDto(sjxxDto);
		map.put("xgsj", sjxxDto1.getXgsj());
		return map;
	}
	
	/**
	 * 小程序送检第二页完成
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/miniprogram/saveInspectCompile")
	@ResponseBody
	public Map<String, Object> saveInspectCompile(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjqqjcDto> sjqqjsDtos = getSjqqjcs(sjxxDto);
		if(sjqqjsDtos != null && sjqqjsDtos.size() > 0){
			sjxxDto.setSjqqjcs(sjqqjsDtos);
		}
		//判断相同用户标本类型是否输入重复
		SjxxDto i_sjxxDto = sjxxService.getDto(sjxxDto,1);
		i_sjxxDto.setYblx(sjxxDto.getYblx());
		List<SjxxDto> sjxxDtos = sjxxService.isYblxRepeat(i_sjxxDto);
		if(sjxxDtos != null && sjxxDtos.size() > 0){
			//保存现有信息
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_SJ00007").getXxnr());
			return map;
		}
		sjxxDto.setLrry(sjxxDto.getWxid());
		sjxxDto.setXgry(sjxxDto.getWxid());
		sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
		sjxxDto.setScbj("0");
		boolean isSuccess = sjxxService.addSaveConsentComp(sjxxDto);
		if(isSuccess){
			SjxxDto t_sjxxDto = new SjxxDto();
			SjkjxxDto sjkjxxDto = new SjkjxxDto();
			if(!StringUtil.isBlank(sjxxDto.getWxid())){
				sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getWxid());
				t_sjxxDto.setWxid(sjxxDto.getWxid());
			}else if(!StringUtil.isBlank(sjxxDto.getDdid())) {
				sjkjxxDto = sjkjxxService.getDtoById(sjxxDto.getDdid());
				t_sjxxDto.setDdid(sjxxDto.getDdid());
			}
			if(StringUtil.isNotBlank(sjkjxxDto.getSjdwmc())) {
				t_sjxxDto.setSjdw(sjkjxxDto.getSjdw());
				t_sjxxDto.setSjdwbj(sjkjxxDto.getSjdwbj());
				t_sjxxDto.setYymc(sjkjxxDto.getSjdwmc());
			}
			t_sjxxDto.setKs(sjkjxxDto.getKs());
			t_sjxxDto.setQtks(sjkjxxDto.getQtks());
			t_sjxxDto.setSjys(sjkjxxDto.getSjys());
			t_sjxxDto.setYsdh(sjkjxxDto.getYsdh());
			t_sjxxDto.setJcdw(sjkjxxDto.getJcdw());
			t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
			map.put("sjxxDto", t_sjxxDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		
		return map;
	}
	
	/**
	 * 获取前期检测信息
	 * @param sjxxDto
	 * @return
	 */
	private List<SjqqjcDto> getSjqqjcs(SjxxDto sjxxDto){
		List<String> qqjcids = sjxxDto.getQqjcids();
		List<String> jczs = sjxxDto.getJczs();
		if(qqjcids != null && qqjcids.size() > 0){
			List<SjqqjcDto> sjqqjsDtos = new ArrayList<>();
			for (int i = 0; i < qqjcids.size(); i++) {
				SjqqjcDto sjqqjcDto = new SjqqjcDto();
				sjqqjcDto.setJcz(jczs.get(i));
				sjqqjcDto.setYjxm(qqjcids.get(i));
				sjqqjsDtos.add(sjqqjcDto);
			}
			return sjqqjsDtos;
		}
		return null;
	}
	
    /**
     * 获取送检医生信息（仅微信小程序在使用，后续需要切换成/wechat/sjysxx/getSjysxxListInfo）
     * @param sjysxxDto
     * @return
    */
    @RequestMapping("/miniprogram/getSjysxxList")
    @ResponseBody
    public  Map<String, Object> getSjysxxList(SjysxxDto sjysxxDto) {
    	Map<String,Object> map = new HashMap<>();
        List<SjysxxDto> sjysxxDtos;
        List<String> lrrylist=sjysxxService.getLrrylist(sjysxxDto.getWxid(), sjysxxDto.getDdid());
        sjysxxDto.setLrrylist(lrrylist);
        int count = sjysxxService.getCountByWxid(sjysxxDto);
        if(count > 15) {
        	sjysxxDtos = sjysxxService.selectTreeSjysxx(sjysxxDto);
		}else {
			sjysxxDtos = sjysxxService.getDtoList(sjysxxDto); 
		}
        map.put("sjysxxDtos", sjysxxDtos);
        return map;
    }
    
    /**
     * 新增送检医生信息（仅微信小程序在使用，后续需要切换成/wechat/sjysxx/saveSjysxx）
     * @param sjysxxDto
     * @return
     */
    @RequestMapping("/miniprogram/saveSjysxx")
    @ResponseBody
    public Map<String,Object> saveSjysxx(SjysxxDto sjysxxDto){
        Map<String,Object> map= new HashMap<>();
        if(StringUtil.isBlank(sjysxxDto.getYsid())){
            sjysxxDto.setYsid(StringUtil.generateUUID());
        }
        List<SjysxxDto> sjysxxDtos;
        List<String> lrrylist=sjysxxService.getLrrylist(sjysxxDto.getWxid(), sjysxxDto.getDdid());
        sjysxxDto.setLrrylist(lrrylist);
        //如果为从钉钉小程序添加医生信息，则将ddid当做lrry放入wxid字段
        if(StringUtil.isNotBlank(sjysxxDto.getDdid())) {
        	sjysxxDto.setWxid(sjysxxDto.getDdid());
        }
        boolean result = sjysxxService.insert(sjysxxDto);
        int count = sjysxxService.getCountByWxid(sjysxxDto);
        if(count > 15) {
        	sjysxxDtos = sjysxxService.selectTreeSjysxx(sjysxxDto);
		}else {
			sjysxxDtos = sjysxxService.getDtoList(sjysxxDto); 
		}
        map.put("sjysxxDtos", sjysxxDtos);
        map.put("status",result?"success" : "fail");
        return map;
    }
    
    /**
     * 删除送检医生信息（仅微信小程序在使用，后续需要切换成/wechat/sjysxx/deleteSjysx）
     * @param ysid
     * @return
     */
    @RequestMapping("/miniprogram/delSjysxx")
    @ResponseBody
    public Map<String,Object> delSjysxx(String ysid){
        Map<String,Object> map= new HashMap<>();
        boolean result=sjysxxService.deleteById(ysid);
        map.put("status",result?"success" : "fail");
        return map;
    }
    
	/**
	 * 获取请求IP地址
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getCurrentIP")
	@ResponseBody
	public Map<String,Object> getCurrentIP(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		map.put("ip", request.getRemoteAddr());
		return map;
	}
	
	/*------------------------------------------  报告分析   -------------------------------------------------*/
	
	/**
	 * 判断是否第一次请求结果分析页
	 * @return
	 */
	@RequestMapping(value="/miniprogram/isReportPage")
	@ResponseBody
	public Map<String,Object> isReportPage(){
		Map<String,Object> map= new HashMap<>();
		map.put("status", "1");
		return map;
	}

	/**
	 * 根据送检id 获取送检详细信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/miniprogram/getSjxxInfo")
	@ResponseBody
	public Map<String, Object> getSjxxInfo(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		SjxxDto t_sjxxDto=sjxxService.getDto(sjxxDto);
		map.put("status", "success");
		map.put("sjxxDto", t_sjxxDto);
		return map;
	}
}
