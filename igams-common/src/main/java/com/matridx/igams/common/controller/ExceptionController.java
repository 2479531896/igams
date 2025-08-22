package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.SjycStatisticsDto;
import com.matridx.igams.common.dao.entities.SjycfkDto;
import com.matridx.igams.common.dao.entities.SjyctzDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DingNotificationTypeEnum;
import com.matridx.igams.common.enums.GlobalParmEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.TwrTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.ISjycStatisticsService;
import com.matridx.igams.common.service.svcinterface.ISjycfkService;
import com.matridx.igams.common.service.svcinterface.ISjyctzService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.ExceptionSSEUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/exception")
public class ExceptionController extends BaseController{

	private final Logger log = LoggerFactory.getLogger(ExceptionController.class);
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ISjycService sjycService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ISjycfkService sjycfkService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISjyctzService sjyctzService;
	@Autowired
	IGzglService gzglService;
	@Autowired
	ICommonService commonService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ExceptionSSEUtil exceptionSSEUtil;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.dingtalk.jumpdingtalkurl:}")
	private String jumpdingtalkurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	@Autowired
	private ISjycStatisticsService sjycStatisticsService;


	/**
	 * 异常列表页面
	 */
	@RequestMapping("/exception/pageListException")
	public ModelAndView getPagedListException(SjycDto sjycDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		ModelAndView mav=new ModelAndView("common/exception/exception_list");
		mav.addObject("detectionList",  redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("exceptionlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_TYPE.getCode()));//异常类型
		mav.addObject("exceptioncopylist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_COPY_TYPE.getCode()) );//异常复制类型
		mav.addObject("ycqfList",  redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));//异常区分


		mav.addObject("sjycDto",sjycDto);
		mav.addObject("ryid",user.getYhid());
		mav.addObject("ycqf_flg",StringUtil.isBlank(sjycDto.getYcqf_flg())?"":sjycDto.getYcqf_flg());
		List<JcsjDto> ycqfList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
		List<JcsjDto>newYcqfList=new ArrayList<>();
		if(StringUtil.isNotBlank(sjycDto.getYcqf_flg())){
			for (JcsjDto jcsjDto:ycqfList){
				if(StringUtil.isNotBlank(sjycDto.getYcqf_flg())){
					switch (sjycDto.getYcqf_flg()){
						case "1":
							if(jcsjDto.getCsdm().equals("NORMAL_EXCEPTION")){
								sjycDto.setYcqf(jcsjDto.getCsid());
								newYcqfList.add(jcsjDto);
							}
							break;
						case "2":
							if(jcsjDto.getCsdm().equals("OTHER_EXCEPTION")){
								sjycDto.setYcqf(jcsjDto.getCsid());
								newYcqfList.add(jcsjDto);
							}
							break;
						case "3":
							if(jcsjDto.getCsdm().equals("PRODUCT_EXCEPTION")){
								sjycDto.setYcqf(jcsjDto.getCsid());
								newYcqfList.add(jcsjDto);
							}
							break;
						case "4":
							if(jcsjDto.getCsdm().equals("WECHAT_EXCEPTION")){
								sjycDto.setYcqf(jcsjDto.getCsid());
								newYcqfList.add(jcsjDto);
							}
							break;
					}
				}

			}
			mav.addObject("ycqfs",sjycDto.getYcqf());
			mav.addObject("ycqfList",  newYcqfList);//异常区分
		}

		Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + user.getYhid());
		if(exceptionConectMap_redis!=null){
			JSONObject exceptionConectMap= JSON.parseObject(exceptionConectMap_redis.toString());
			mav.addObject("unreadNum",exceptionConectMap.get("unreadcount").toString());
			mav.addObject("finreadNum",exceptionConectMap.get("finreadcount")!=null?exceptionConectMap.get("finreadcount").toString():"0");
		}else{
			mav.addObject("unreadNum","0");
			mav.addObject("finreadNum","0");
		}
		mav.addObject("key", GlobalParmEnum.SPECIMEN_EXCEPTION.getCode()+":"+user.getYhid());
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	@RequestMapping("/exception/pageGetListException")
	@ResponseBody
	public Map<String,Object> getExceptionList(SjycDto sjycDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		if("1".equals(sjycDto.getPersonal_flg())){
			sjycDto.setRyid(user.getYhid());
			//判断伙伴权限
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("yhid", user.getYhid());
			RestTemplate t_restTemplate = new RestTemplate();
			List<String> hbmcList = t_restTemplate.postForObject(applicationurl+"/ws/getHbqxList", paramMap, List.class);
			sjycDto.setSjhbs(hbmcList);
		}else if(!"0".equals(sjycDto.getPersonal_flg())){//0代表不做任何限制，若该列表不做任何限制则不进入以下逻辑
			List<Map<String,String>> jcdwList=sjycService.getJsjcdwByjsid(user.getDqjs());
			if(jcdwList!=null && !jcdwList.isEmpty()) {
				//判断检测单位是否为1（单位限制）
				if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
					//取出检测单位为一个List
					List<String> strList = new ArrayList<>();
					for (Map<String, String> stringStringMap : jcdwList) {
						if (stringStringMap.get("jcdw") != null) {
							strList.add(stringStringMap.get("jcdw"));
						}
					}
					//如果检测单位不为空，进行查询。
					if(!strList.isEmpty()) {
						sjycDto.setJcdwxz(strList);
					}
				}
			}
		}

		List<SjycDto> list=sjycService.getPagedDtoList(sjycDto);
		if(list!=null&& !list.isEmpty()){
			Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + user.getYhid());
			if(exceptionConectMap_redis!=null){
				JSONObject exceptionConectMap= JSON.parseObject(exceptionConectMap_redis.toString());
				JSONObject exceptionlist=JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
				for(SjycDto dto:list){
					Object obj=exceptionlist.get(dto.getYcid());
					if(obj!=null){
						JSONObject exceptionMap=JSON.parseObject(exceptionlist.get(dto.getYcid()).toString());
						dto.setUnreadnum(exceptionMap.get("ex_unreadcnt").toString());
					}else{
						dto.setUnreadnum("0");
					}
				}
			}
		}
		map.put("total", sjycDto.getTotalNumber());
    	map.put("rows",  list);
    	return map;
	}

	/**
	 * 新增异常页面
	 */
	@RequestMapping("/exception/addException")
	public ModelAndView getAddExceptionPage(SjycDto sjycDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		ModelAndView mav=new ModelAndView("common/exception/exception_add");
		sjycDto.setFormAction("add");
		sjycDto.setMrtwr(user.getYhid());
		sjycDto.setMrtwrmc(user.getZsxm());
		sjycDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		// 通知类型
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("code", tzlxType.getCode());
			map.put("value", tzlxType.getValue());
			tzlxList.add(map);
		}
		String key = GlobalParmEnum.SPECIMEN_EXCEPTION.getCode()+":"+user.getYhid()+"_"+new Date().getTime();
		Object hget = redisUtil.get(key);
		if (hget!=null){
			redisUtil.del(key);
		}
		Map<String,Object> jsonMap=new HashMap<>();
		jsonMap.put("ywlx", BusTypeEnum.IMP_EXCEPTION.getCode());
		jsonMap.put("flagmc", GlobalParmEnum.SPECIMEN_EXCEPTION.getValue());
		jsonMap.put("flag", GlobalParmEnum.SPECIMEN_EXCEPTION.getCode());
		redisUtil.set(key,JSON.toJSONString(jsonMap),5000);
		mav.addObject("key", key);
		mav.addObject("switchoverurl", applicationurl+"/ws/pathogen/pagedataUploadFile?key="+key);
		DBEncrypt p = new DBEncrypt();
		String dingtalkurl = p.dCode(jumpdingtalkurl);
		String dingtalkbtn = dingtalkurl+"page=/pages/index/index"+ URLEncoder.encode("?toPageUrl=/pages/index/sjsyfjpage/sjsyfjpage&key="+key,StandardCharsets.UTF_8);
		log.error("dingtalkurl:"+dingtalkbtn);
		mav.addObject("dingtalkurl", dingtalkbtn);
		mav.addObject("yclblist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_TYPE.getCode()));
		List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
		if(jcsjDtos!=null && !jcsjDtos.isEmpty()){
			for(JcsjDto dto:jcsjDtos){
				if("1".equals(dto.getSfmr())){
					sjycDto.setYcqfdm(dto.getCsdm());
					break;
				}
			}
		}
		//根据角色查询到角色检测单位信息
		List<Map<String,String>> jcdwList = sjycService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null && !jcdwList.isEmpty()) {
			//判断检测单位是否为1（单位限制）
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				sjycDto.setJcdw(jcdwList.get(0).get("jcdw"));
			}
		}
		mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("ycqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));
		mav.addObject("yczqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_SUBDISTINGUISH.getCode()));
		mav.addObject("yctjlxlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_STATISTICS.getCode()));
		mav.addObject("sjycDto", sjycDto);
		mav.addObject("tzlxList", tzlxList);
		mav.addObject("urlPrefix", urlPrefix);
		if (StringUtil.isNotBlank(sjycDto.getYwid())){
			SjycDto sjxxByYwid = sjycService.getSjxxByYwid(sjycDto.getYwid());
			if (null != sjxxByYwid){
				sjycDto.setYbbh(sjxxByYwid.getYbbh());
				sjycDto.setHzxm(sjxxByYwid.getHzxm());
				sjycDto.setDb(sjxxByYwid.getDb());
				sjycDto.setYblxmc(sjxxByYwid.getYblxmc());
				sjycDto.setJcdw(sjxxByYwid.getJcdw());
				sjycDto.setYcbt(sjxxByYwid.getHzxm()+"-"+sjxxByYwid.getYblxmc()+"-"+sjxxByYwid.getDb());
			}
		}
		if (!CollectionUtils.isEmpty(sjycDto.getSjids())){
			List<SjycDto> sjxxDtos = sjycService.getSjxxByYwids(sjycDto.getSjids());
			if (!CollectionUtils.isEmpty(sjxxDtos)){
				mav.addObject("sjxxDtos",sjxxDtos);
			}
		}
		if(request.getParameter("listflg")!=null&&"1".equals(request.getParameter("listflg"))){
			mav=new ModelAndView("common/exception/exception_viewList");
			sjycDto.setSjid(sjycDto.getYwid());
			sjycDto.setSjids(sjycDto.getSjids());
			sjycDto.setYhid(user.getYhid());
			sjycDto.setMrtwr(user.getYhid());
			sjycDto.setMrtwrmc(user.getZsxm());
			sjycDto.setMrtwrjs(user.getDqjs());
			sjycDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
			mav.addObject("addbj","1");
			mav.addObject("sjycDto", sjycDto);
			mav.addObject("modqx", "0");
			mav.addObject("urlPrefix", urlPrefix);
		}
		return mav;
	}

	/**
	 * 新增异常保存
	 */
	@RequestMapping("/exception/addSaveException")
	@ResponseBody
	public Map<String,Object> addSaveException(SjycDto sjycDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		sjycDto.setLrry(user.getYhid());
		sjycDto.setQrrmc(user.getZsxm());
		if(StringUtil.isBlank(sjycDto.getTwr())){
			sjycDto.setTwr(user.getYhid());
			sjycDto.setTwrmc(user.getZsxm());
		}
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess = false;
		if (!CollectionUtils.isEmpty(sjycDto.getSjids())){
			List<SjycDto> sjxxDtos = sjycService.getSjxxByYwids(sjycDto.getSjids());
			for (SjycDto sjxxDto : sjxxDtos) {
				SjycDto dto = JSON.parseObject(JSON.toJSONString(sjycDto), SjycDto.class);
				dto.setYcid(null);
				dto.setYcbt((StringUtil.isNotBlank(sjycDto.getYcbt())?sjycDto.getYcbt()+"-":"") + sjxxDto.getHzxm()+"-"+sjxxDto.getYblxmc()+"-"+sjxxDto.getDb());
				dto.setYwid(sjxxDto.getSjid());
				isSuccess = sjycService.addSaveException(dto);
			}
		}else {
			isSuccess=sjycService.addSaveException(sjycDto);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 选择送检信息界面
	 */
	@RequestMapping("/exception/pagedataSjxxChance")
	public ModelAndView pageListSjxxChance() {
		ModelAndView mav = new ModelAndView("common/exception/exception_sjxxlistChance");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 查看异常任务
	 */
	@RequestMapping("/exception/pagedataGetException")
	@ResponseBody
	public Map<String,Object> pagedataGetException(SjycDto sjycDto) {
		SjycDto sjyc=sjycService.getDto(sjycDto);
		Map<String,Object> map=new HashMap<>();

		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> sjyctzDtos = sjyctzService.getDtoList(sjyctzDto);
		StringBuilder tzrys= new StringBuilder();
		if(sjyctzDtos!=null&& !sjyctzDtos.isEmpty()){
			for(SjyctzDto dto:sjyctzDtos){
				tzrys.append(",").append(dto.getMc());
			}
			if(StringUtil.isNotBlank(tzrys.toString())){
				tzrys = new StringBuilder(tzrys.substring(1));
			}
		}
		sjyc.setTzry(tzrys.toString());
		map.put("sjycDto", sjyc);
		map.put("status", "success");
		return map;
	}


	/**
	 * 查看异常任务
	 */
	@RequestMapping("/exception/viewException")
	@ResponseBody
	public ModelAndView getExceptionView(SjycDto sjycDto) {
		SjycDto sjyc=sjycService.getDto(sjycDto);
		ModelAndView mav = new ModelAndView("common/exception/exception_view");
		//根据异常ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjycDto.getYcid());
		if(fjcfbDtos != null){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> sjyctzDtos = sjyctzService.getDtoList(sjyctzDto);
		StringBuilder tzrys= new StringBuilder();
		if(sjyctzDtos!=null&& !sjyctzDtos.isEmpty()){
			for(SjyctzDto dto:sjyctzDtos){
				tzrys.append(",").append(dto.getMc());
			}
			if(StringUtil.isNotBlank(tzrys.toString())){
				tzrys = new StringBuilder(tzrys.substring(1));
			}
		}

		sjyc.setTzry(tzrys.toString());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("sjycDto", sjyc);
		mav.addObject("urlPrefix", urlPrefix);
		SjycStatisticsDto sjycStatisticsDto=new SjycStatisticsDto();
		sjycStatisticsDto.setYcid(sjycDto.getYcid());
		List<SjycStatisticsDto> sjycStatisticsDtoList=sjycStatisticsService.getByYcid(sjycStatisticsDto);
		String statisticsids="";
		if(sjycStatisticsDtoList!=null&& !sjycStatisticsDtoList.isEmpty()){
			List<JcsjDto> yctjlxList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_STATISTICS.getCode());
			for(SjycStatisticsDto sjycStatisticsDto1:sjycStatisticsDtoList){
				for(JcsjDto jcsjDto:yctjlxList){
					if(jcsjDto.getCsid().equals(sjycStatisticsDto1.getJcsjcsid())){
						if(StringUtil.isBlank(statisticsids)){
							statisticsids+=jcsjDto.getCsmc();
						}else{
							statisticsids+=","+jcsjDto.getCsmc();
						}
					}
				}

			}

		}
		mav.addObject("statisticsids", StringUtil.isBlank(statisticsids)?"":statisticsids);
		return mav;
	}

	/**
	 * 跳转修改界面
	 */
	@RequestMapping("/exception/modException")
	public ModelAndView getModExceptionPage(SjycDto sjycDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EXCEPTION_TYPE});
		SjycDto sjyc=sjycService.getDto(sjycDto);
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> sjyctzlist=sjyctzService.getDtoList(sjyctzDto);
		StringBuilder ids= new StringBuilder();
		if(sjyctzlist!=null && !sjyctzlist.isEmpty()) {
			for (SjyctzDto dto : sjyctzlist) {
				if (dto.getLx().equals(DingNotificationTypeEnum.ROLE_TYPE.getCode())) {
					ids.append(",ROLE_TYPE-").append(dto.getRyid());
				} else {
					ids.append(",USER_TYPE-").append(dto.getRyid());
				}
			}
			ids = new StringBuilder(ids.substring(1));
			sjyc.setStr_tzrys(ids.toString());
		}
		sjyc.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		sjyc.setFormAction("mod");
		ModelAndView mav=new ModelAndView("common/exception/exception_add");
		if (StringUtil.isNotBlank(request.getParameter("action"))) {
			mav.addObject("action",request.getParameter("action"));
		}

		//查看附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjycDto.getYcid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		// 通知类型
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("code", tzlxType.getCode());
			map.put("value", tzlxType.getValue());
			tzlxList.add(map);
		}
		String key = GlobalParmEnum.SPECIMEN_EXCEPTION.getCode()+":"+user.getYhid()+"_"+new Date().getTime();
		Object hget = redisUtil.get(key);
		if (hget!=null){
			redisUtil.del(key);
		}	Map<String,Object> jsonMap=new HashMap<>();
		jsonMap.put("ywlx", BusTypeEnum.IMP_EXCEPTION.getCode());
		jsonMap.put("flagmc", GlobalParmEnum.SPECIMEN_EXCEPTION.getValue());
		jsonMap.put("flag", GlobalParmEnum.SPECIMEN_EXCEPTION.getCode());
		redisUtil.set(key,JSON.toJSONString(jsonMap),5000);
		mav.addObject("key", key);
		mav.addObject("switchoverurl", applicationurl+"/ws/pathogen/pagedataUploadFile?key="+key);
		DBEncrypt p = new DBEncrypt();
		String dingtalkurl = p.dCode(jumpdingtalkurl);
		String dingtalkbtn = dingtalkurl+"page=/pages/index/index"+ URLEncoder.encode("?toPageUrl=/pages/index/sjsyfjpage/sjsyfjpage&key="+key,StandardCharsets.UTF_8);
		log.error("dingtalkurl:"+dingtalkbtn);
		mav.addObject("dingtalkurl", dingtalkbtn);
		mav.addObject("yclblist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_TYPE.getCode()));
		List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
		if(jcsjDtos!=null && !jcsjDtos.isEmpty()){
			for(JcsjDto dto:jcsjDtos){
				if("1".equals(dto.getSfmr())){
					sjycDto.setYcqfdm(dto.getCsdm());
					break;
				}
			}
		}
		mav.addObject("ycqflist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode()));
		mav.addObject("yclblist", jclist.get(BasicDataTypeEnum.EXCEPTION_TYPE.getCode()));
		mav.addObject("jcdwlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjycDto",sjyc);
		mav.addObject("tzlxList", tzlxList);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("flag", GlobalParmEnum.SPECIMEN_EXCEPTION.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		SjycStatisticsDto sjycStatisticsDto=new SjycStatisticsDto();
		sjycStatisticsDto.setYcid(sjycDto.getYcid());
		List<SjycStatisticsDto> sjycStatisticsDtoList=sjycStatisticsService.getByYcid(sjycStatisticsDto);
		String statisticsids="";
		if(sjycStatisticsDtoList!=null&& !sjycStatisticsDtoList.isEmpty()){
			for(SjycStatisticsDto sjycStatisticsDto1:sjycStatisticsDtoList){
				if(StringUtil.isBlank(statisticsids)){
					statisticsids+=sjycStatisticsDto1.getJcsjcsid();
				}else{
					statisticsids+=","+sjycStatisticsDto1.getJcsjcsid();
				}
			}

		}
		mav.addObject("statisticsids", StringUtil.isBlank(statisticsids)?"":statisticsids);
		return mav;
	}

	/**
	 * 修改保存异常信息（异常结束）
	 */
	@RequestMapping("/exception/modSaveException")
	@ResponseBody
	public Map<String,Object> modSaveException(SjycDto sjycDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		sjycDto.setXgry(user.getYhid());
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=sjycService.modSaveException(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除异常信息
	 */
	@RequestMapping("/exception/delException")
	@ResponseBody
	public Map<String,Object> delException(SjycDto sjycDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		sjycDto.setScry(user.getYhid());
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=sjycService.delException(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 获取异常反馈信息
	 */
	@RequestMapping("/exception/pagedataExceptionFeedback")
	@ResponseBody
	public Map<String,Object> getExceptionFeedback(SjycfkDto sjycfkDto){
		List<SjycfkDto> SjycfkDtos=sjycfkService.getMiniDtoByYcid(sjycfkDto);
		if(SjycfkDtos!=null&& !SjycfkDtos.isEmpty()){
			for(SjycfkDto dto:SjycfkDtos){
				if("WECHAT_FEEDBACK".equals(dto.getFkqfdm())){
					dto.setFkrymc(dto.getLrrymc());
				}
			}
		}
		Map<String,Object> map=new HashMap<>();
		map.put("ycfklist", SjycfkDtos);
		return map;
	}

	/**
	 * 保存异常反馈信息
	 */
	@RequestMapping("/exception/pagedataSaveExceptionFk")
	@ResponseBody
	public Map<String,Object> addSaveExceptionFeedback(SjycfkDto sjycfkDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		SjycDto ycdto = sjycService.getDtoById(sjycfkDto.getYcid());
		if ("1".equals(ycdto.getSfjs())){
			map.put("status", "fail");
			map.put("message", "该样本已经结束，不能再反馈异常信息！");
			return map;
		}
		User user=getLoginInfo(request);
		sjycfkDto.setLrry(user.getYhid());
		sjycfkDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sjycfkDto.setLrsj(dateFm.format(new Date()));
		List<JcsjDto> fkqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.FEEDBACK_DISTINGUISH.getCode());
		if (fkqfList!=null && !fkqfList.isEmpty()) {
			for (JcsjDto jc_fkqf : fkqfList) {
				if ("NORMAL_FEEDBACK".equals(jc_fkqf.getCsdm())){
					sjycfkDto.setFkqf(jc_fkqf.getCsid());
					break;
				}
			}
		}
		SjycDto sjycDto=new SjycDto();
		sjycDto.setYcid(sjycfkDto.getYcid());
		sjycDto.setXgry(user.getYhid());
		sjycDto.setZhhfsj(sjycfkDto.getLrsj());
		sjycDto.setZhhfnr(user.getZsxm()+":"+sjycfkDto.getFkxx());
		boolean isSuccess=sjycService.addSaveExceptionFeedback(sjycfkDto,sjycDto);
		String fkxx=sjycfkDto.getFkxx();
		List<String> yhms=new ArrayList<>();
		if(StringUtil.isNotBlank(fkxx)){
			String[] strings=fkxx.split("@");
			if(strings.length>1){
				for(String yhm:strings){
					if(StringUtil.isNotBlank(yhm)){
						yhms.add(yhm.substring(1,6));
					}
				}
			}
		}
		SjycDto dtoById = sjycService.getDtoById(sjycfkDto.getYcid());
		List<SjycDto> sjycDtos=new ArrayList<>();
		List<String> yhids=new ArrayList<>();
		List<String> jsids=new ArrayList<>();
		List<String> no_yhids=new ArrayList<>();
		SjyctzDto sjyctzDto=new SjyctzDto();
		sjyctzDto.setYcid(sjycfkDto.getYcid());
		List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto);
		if(!yhms.isEmpty()){
			User user_t=new User();
			user_t.setIds(yhms);
			List<User> users=commonService.getUsersByYhms(user_t);
			if(users!=null&& !users.isEmpty()){
				List<SjyctzDto> sjyctzDtos = new ArrayList<>();
				for(User t_user:users){
					no_yhids.add(t_user.getYhid());
					boolean findSame=false;
					for(SjyctzDto sjyctzDto_t:dtoList){
						if(t_user.getYhid().equals(sjyctzDto_t.getRyid())){
							findSame=true;
							break;
						}
					}
					if(!findSame){
						SjyctzDto sjyctzDto_t = new SjyctzDto();
						sjyctzDto_t.setRyid(t_user.getYhid());
						sjyctzDto_t.setLx(DingNotificationTypeEnum.USER_TYPE.getCode());
						sjyctzDto_t.setYctzid(StringUtil.generateUUID());
						sjyctzDto_t.setYcid(sjycfkDto.getYcid());
						sjyctzDtos.add(sjyctzDto_t);
					}
				}
				if(!sjyctzDtos.isEmpty()){
					sjyctzService.insertList(sjyctzDtos);
				}
				sjycfkDto.setUsers(users);
				sjycfkDto.setYcbt(dtoById.getYcbt());

				sjycfkService.sendMessageFromOA(sjycfkDto);
			}
		}
		if("ROLE_TYPE".equals(dtoById.getQrlx())){
			jsids.add(dtoById.getQrr());
		}else{
			yhids.add(dtoById.getQrr());
		}
		for(SjyctzDto dto:dtoList){
			if("ROLE_TYPE".equals(dto.getLx())){
				if(!jsids.contains(dto.getId())){
					jsids.add(dto.getId());
				}
			}else{
				if(!yhids.contains(dto.getId())){
					yhids.add(dto.getId());
				}
			}
		}
		if(!jsids.isEmpty()){
			SjyctzDto sjyctzDto_t=new SjyctzDto();
			sjyctzDto_t.setIds(jsids);
			List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
			for(SjyctzDto dto:yhjsList){
				if(!dto.getRyid().equals(user.getYhid())&&!yhids.contains(dto.getRyid())&&!no_yhids.contains(dto.getRyid())){
					SjycDto sjycDto_t=new SjycDto();
					sjycDto_t.setRyid(dto.getRyid());
					sjycDtos.add(sjycDto_t);
				}
			}
		}

		if(!yhids.isEmpty()){
			for(String yhid:yhids){
				if(!yhid.equals(user.getYhid())&&!no_yhids.contains(yhid)){
					SjycDto sjycDto_t=new SjycDto();
					sjycDto_t.setRyid(yhid);
					sjycDtos.add(sjycDto_t);
				}
			}
		}
		exceptionSSEUtil.addExceptionMessage(sjycDtos,sjycDto.getYcid());
		if("WECHAT_EXCEPTION".equals(dtoById.getYcqfdm())&&TwrTypeEnum.WECHAT.getCode().equals(dtoById.getTwrlx())){
			Map<String,Object> rabbitMap=new HashMap<>();
			rabbitMap.put("type","hf");
			rabbitMap.put("yhid",dtoById.getTwr());
			rabbitMap.put("ycid",sjycfkDto.getYcid());
			sjycfkDto.setLrrymc(user.getZsxm());
			rabbitMap.put("sjycfkDto",sjycfkDto);
			rabbitMap.put("sjycDto",ycdto);

			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		}
		//保存人员操作习惯
		sjycfkDto.setLrrymc(user.getZsxm());
        sjycfkDto.setYcbt(ycdto.getYcbt());
		sjycService.sendFkMessage(sjycDtos,sjycfkDto,false);
		map.put("status",isSuccess);
		return map;
		}

	/**
	 * 异常反馈界面
	 */
	@RequestMapping("/exception/exceptionlistView")
	public ModelAndView ExceptionFeedbackPage(SjycDto sjycDto, HttpServletRequest request) {
		User user=getLoginInfo(request);

		ModelAndView mav =new ModelAndView("common/exception/exception_viewList");
		if (StringUtil.isNotBlank(request.getParameter("action"))) {
			mav.addObject("action",request.getParameter("action"));
		}
		SjycDto sjycDto_t=new SjycDto();
		if(StringUtil.isNotBlank(sjycDto.getYcid())){
			sjycDto_t=sjycService.getDto(sjycDto);
		}
		if("1".equals(sjycDto_t.getSfjs())){
			exceptionSSEUtil.viewFinishException(user.getYhid(),sjycDto.getYcid());
		}else{
			exceptionSSEUtil.viewExceptionMessage(user.getYhid(),sjycDto.getYcid());
		}
		sjycDto_t.setMrtwr(user.getYhid());
		sjycDto_t.setMrtwrmc(user.getZsxm());
		sjycDto_t.setMrtwrjs(user.getDqjs());
		mav.addObject("addbj","0");
		mav.addObject("sjycDto", sjycDto_t);
		mav.addObject("modqx", "1");
		mav.addObject("urlPrefix",urlPrefix);
		
		return mav;
	}

	/**
	 * 结束异常信息
	 */
	@RequestMapping("/exception/finishSaveException")
	@ResponseBody
	public Map<String,Object> finishSaveException(SjycDto sjycDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		sjycDto.setJsry(user.getYhid());
		sjycDto.setXgry(user.getYhid());
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=sjycService.finishYc(sjycDto);
		if(StringUtil.isNotBlank(sjycDto.getSfjj())||StringUtil.isNotBlank(sjycDto.getPjxj())||StringUtil.isNotBlank(sjycDto.getPjnr())){
			isSuccess=sjycService.evaluation(sjycDto);
		}
		Map<String,Object> rabbitMap=new HashMap<>();
		rabbitMap.put("type","finish");
		rabbitMap.put("sjycDto",sjycDto);

		List<String> ycids=sjycDto.getIds();
		ycids.stream().forEach(s -> exceptionSSEUtil.finishException(s));
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.SSE_SENDMSG_EXCEPRION_WECHART.getCode(), JSON.toJSONString(rabbitMap));
		for(String ycid:ycids) {
			SjycDto dtoById = sjycService.getDtoById(ycid);


			List<SjycDto> sjycDtos = new ArrayList<>();
			List<String> yhids = new ArrayList<>();
			List<String> jsids = new ArrayList<>();

			if ("ROLE_TYPE".equals(dtoById.getQrlx())) {
				jsids.add(dtoById.getQrr());
			} else {
				if (!dtoById.getQrr().equals(user.getYhid())) {
					yhids.add(dtoById.getQrr());
				}
			}
			SjyctzDto sjyctzDto = new SjyctzDto();
			sjyctzDto.setYcid(ycid);
			List<SjyctzDto> dtoList = sjyctzService.getDtoList(sjyctzDto);
			if (dtoList != null && !dtoList.isEmpty()) {
				if ("ROLE_TYPE".equals(dtoList.get(0).getLx())) {
					for (SjyctzDto dto : dtoList) {
						if (!jsids.contains(dto.getId())) {
							jsids.add(dto.getId());
						}
					}
				} else {
					for (SjyctzDto dto : dtoList) {
						if (!dto.getId().equals(user.getYhid()) && !yhids.contains(dto.getId())) {
							yhids.add(dto.getId());
						}
					}
				}
			}

			if (!jsids.isEmpty()) {
				SjyctzDto sjyctzDto_t = new SjyctzDto();
				sjyctzDto_t.setIds(jsids);
				List<SjyctzDto> yhjsList = sjyctzService.getYhjsList(sjyctzDto_t);
				for (SjyctzDto dto : yhjsList) {
					if (!dto.getRyid().equals(user.getYhid()) && !yhids.contains(dto.getRyid())) {
						SjycDto sjycDto_t = new SjycDto();
						sjycDto_t.setRyid(dto.getRyid());
						sjycDtos.add(sjycDto_t);
					}
				}
			}

			if (!yhids.isEmpty()) {
				for (String yhid : yhids) {
					SjycDto sjycDto_t = new SjycDto();
					sjycDto_t.setRyid(yhid);
					sjycDtos.add(sjycDto_t);
				}
			}
			SjycfkDto sjycfkDto = new SjycfkDto();
			//保存人员操作习惯
			sjycfkDto.setYcid(ycid);
			sjycfkDto.setLrrymc(user.getZsxm());
			sjycService.sendFkMessage(sjycDtos, sjycfkDto, true);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 跳转转发页面
	 */
	@RequestMapping("/exception/pagedataChoseRepeatObject")
	public ModelAndView choseRepeatObject(SjycDto sjycDto) {
		ModelAndView mav=new ModelAndView("common/exception/exception_repeatObj");
		// 通知类型
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("code", tzlxType.getCode());
			map.put("value", tzlxType.getValue());
			tzlxList.add(map);
		}
		sjycDto = sjycService.getDto(sjycDto);
		SjyctzDto sjyctzDto = new SjyctzDto();
		sjyctzDto.setYcid(sjycDto.getYcid());
		List<SjyctzDto> list = sjyctzService.getDtoList(sjyctzDto);
		if (!list.isEmpty()){
			sjycDto.setTzlx(list.get(0).getLx());
		}
		sjycDto.setFormAction("/exception/exception/pagedataRepeatObject");
		mav.addObject("yctzList",list);
		mav.addObject("sjycDto", sjycDto);
		mav.addObject("tzlxList", tzlxList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 异常转发
	 */
	@RequestMapping("/exception/pagedataRepeatObject")
	@ResponseBody
	public Map<String,Object> repeatObject(SjycDto sjycDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		SjycDto dto = sjycService.getDto(sjycDto);
		if(StringUtil.isNotBlank(dto.getZfsj())){
			if(!dto.getZfsj().equals(sjycDto.getZfsj())){
				map.put("status", "fail");
				map.put("message", "该异常已被别人转发!");
				return map;
			}
		}
		User user=getLoginInfo(request);
		sjycDto.setZfrymc(user.getZsxm());
		sjycDto.setXgry(user.getYhid());
		boolean isSuccess=sjycService.exceptionRepeat(sjycDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM99026").getXxnr():xxglService.getModelById("ICOM99027").getXxnr());
		return map;
	}

	/**
	 * 获取通知人员
	 */
	@RequestMapping("/exception/pagedataNotices")
	@ResponseBody
	public Map<String,Object> getNotices(SjyctzDto sjyctzDto){
		Map<String,Object> map=new HashMap<>();
		List<SjyctzDto> sjyctzDtoList = sjyctzService.getDtoList(sjyctzDto);
		map.put("sjyctzDtoList", sjyctzDtoList);
		return map;
	}

	/**
	 * 发送附件页面
	 */
	@RequestMapping("/exception/pagedataSendFilePage")
	public ModelAndView sendFilePage(SjycfkDto sjycfkDto) {
		sjycfkDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		ModelAndView mav=new ModelAndView("common/exception/exception_sendFile");
		// 查询临时文件并显示
		List<String> fjids = sjycfkDto.getFjids();
		List<FjcfbDto> redisList = fjcfbService.getRedisList(fjids);
		mav.addObject("redisList", redisList);
		// 查询正式文件并显示
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjycfkDto.getFkid());
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("SjycfkDto", sjycfkDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	/**
	 * 异常评论页面获取临时附件信息
	 */
	@RequestMapping("/exception/pagedataLsFile")
	@ResponseBody
	public Map<String,Object> getLsFile(SjycfkDto sjycfkDto){
		sjycfkDto.setYwlx(BusTypeEnum.IMP_EXCEPTION_FEEDBACK.getCode());
		// 查询临时文件并显示
		List<String> fjids = sjycfkDto.getFjids();
		List<FjcfbDto> redisList = fjcfbService.getRedisList(fjids);
		Map<String,Object> map=new HashMap<>();
		map.put("redisList", redisList);
		return map;
	}

	/**
	 * 撤回异常评论(时差不得超过5分钟)
	 */
	@RequestMapping("/exception/minidataRecall")
	@ResponseBody
	public Map<String,Object> recallException(SjycfkDto sjycfkDto){
		sjycfkDto.setScry(sjycfkDto.getYhid());
		Map<String,Object> map=new HashMap<>();
		List<SjycfkDto> list=sjycfkService.getZplByFkid(sjycfkDto);
		//如果有子评论不允许撤回
		if(list!=null && !list.isEmpty()) {
			map.put("status", "fail");
			map.put("message", "不允许撤回！");
		}else {
			boolean isSuccess =sjycfkService.delete(sjycfkDto);
			if(isSuccess) {
				map.put("status", "success");
				map.put("message", "撤回成功！");
			}else {
				map.put("status", "fail");
				map.put("message", "撤回失败！");
			}
		}
		return map;
	}

	/**
	 * 根据输入信息查询用户真实姓名
	 */
	@RequestMapping(value = "/exception/pagedataSelectUser")
	@ResponseBody
	public Map<String, Object> pagedataSelectUser(GzglDto gzglDto){
		List<GzglDto> f_gzglDtos =new ArrayList<>();
		String zsxm=gzglDto.getZsxm();
		if(StringUtil.isNotBlank(zsxm)){
			String[] strings=zsxm.split("@");
			if(strings.length>1){
				gzglDto.setZsxm(strings[strings.length-1]);
				f_gzglDtos = gzglService.selectTaskFzr(gzglDto);
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("f_gzglDtos", f_gzglDtos);
		return map;
	}

	/**
	 * 	销售明细列表
	 */
	@RequestMapping("/exception/pagedataSalesDetails")
	public ModelAndView pagedataSalesDetails() {
		ModelAndView mav = new  ModelAndView("common/exception/exception_chooseSale");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 	借用明细列表
	 */
	@RequestMapping("/exception/pagedataBorrowingDetails")
	public ModelAndView pagedataBorrowingDetails() {
		ModelAndView mav = new  ModelAndView("common/exception/exception_chooseBorrow");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 	领料明细列表
	 */
	@RequestMapping("/exception/pagedataPickingDetails")
	public ModelAndView pagedataPickingDetails() {
		ModelAndView mav = new  ModelAndView("common/exception/exception_choosePick");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 	选择通知人员
	 */
	@RequestMapping("/exception/pagedataChooseTzry")
	public ModelAndView pagedataChooseTzry() {
		ModelAndView mav = new  ModelAndView("common/exception/exception_chooseTzry");
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("code", tzlxType.getCode());
			map.put("value", tzlxType.getValue());
			tzlxList.add(map);
		}
		mav.addObject("tzlxList", tzlxList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	@RequestMapping("/exception/pagedataMoreException")
	@ResponseBody
	public Map<String,Object> getMoreException(SjycDto sjycDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		sjycDto.setYhid(user.getYhid());
		// List<SjycDto> yhjsList = sjycService.getYhjsList(sjycDto);
		// if (yhjsList!=null && yhjsList.size()>0){
		// 	List<String> yhjss = new ArrayList<>();
		// 	for (SjycDto dto : yhjsList) {
		// 		yhjss.add(dto.getJsid());
		// 	}
		// 	sjycDto.setYhjss(yhjss);
		// }
		String sjid = sjycDto.getSjid();
		List<SjycDto> sjyclist;
		if(StringUtil.isNotBlank(sjycDto.getYcid())){
			sjycDto.setSjid(null);
			sjyclist=sjycService.getDtoBySjid(sjycDto);
			if(sjyclist==null|| sjyclist.isEmpty()){
				sjycDto.setYcid(null);
				sjycDto.setSjid(sjid);
				sjyclist=sjycService.getDtoBySjid(sjycDto);
			}
		}else{
			sjyclist=sjycService.getDtoBySjid(sjycDto);
		}

		List<String> ids=new ArrayList<>();
		if(sjyclist!=null && !sjyclist.isEmpty()) {
			for (SjycDto dto : sjyclist) {
				ids.add(dto.getYcid());
			}
			SjycfkDto sjycfkDto=new SjycfkDto();
			sjycfkDto.setIds(ids);
			List<SjycfkDto> pls=sjycfkService.getExceptionPls(sjycfkDto);
			if(pls!=null && !pls.isEmpty()) {
				for (SjycfkDto pl : pls) {
					for (SjycDto dto : sjyclist) {
						if (pl.getYcid().equals(dto.getYcid())) {
							dto.setPls(pl.getPls());
						}
					}
				}
			}
		}
		map.put("sjyclist", sjyclist);
		return map;
	}
	/**
	 * 获取统计详细数据
	 */
	@RequestMapping(value = "/exception/pagedataViewInfo")
	@ResponseBody
	public Map<String, Object> pagedataViewInfo(HttpServletRequest request){
		User user=getLoginInfo(request);
		Map<String,Object> map = new HashMap<>();
		Object exceptionConectMap_redis = redisUtil.get("EXCEPTION_CONNECT:" + user.getYhid());
		List<SjycDto> list=new ArrayList<>();
		if(exceptionConectMap_redis!=null){
			JSONObject exceptionConectMap= JSON.parseObject(exceptionConectMap_redis.toString());
			JSONObject exceptionlist=JSON.parseObject(exceptionConectMap.get("exceptionlist").toString());
			Set<String> keyList=exceptionlist.keySet();
			List<String> ids=new ArrayList<>();
			for(String key:keyList){
				if(ids.size()<5){
					JSONObject jsonObject=JSONObject.parseObject(exceptionlist.get(key).toString());
					if("0".equals(jsonObject.getString("sfjs"))){
						ids.add(key);
					}
				}else{
					break;
				}
			}

			if(!ids.isEmpty()){
				SjycDto sjycDto=new SjycDto();
				sjycDto.setIds(ids);
				list= sjycService.getDtoList(sjycDto);
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 	结束
	 */
	@RequestMapping("/exception/finishException")
	public ModelAndView finishException(SjycDto sjycDto) {
		ModelAndView mav = new  ModelAndView("common/exception/exception_finish");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("sjycDto", sjycDto);
		return mav;
	}
}
