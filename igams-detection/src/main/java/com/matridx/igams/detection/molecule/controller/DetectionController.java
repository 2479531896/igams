package com.matridx.igams.detection.molecule.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.detection.molecule.dao.entities.*;
import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.igams.detection.molecule.enums.NUONUOEnum;
import com.matridx.igams.detection.molecule.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import nuonuo.open.sdk.NNOpenSDK;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Controller
@RequestMapping("/detection")
public class DetectionController extends BaseController {

	@Autowired
	IFzjcxxService fzjcxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired
	IXtszService xtszService;
	@Autowired
	private IGrszService grszService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IHzxxService hzxxService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	IFzjcxmService fzjcxmService;
	@Autowired
	IBkyyrqService bkyyrqService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Autowired
	private ILbzdszService lbzdszService;
	@Autowired
	private IYyxxService yyxxService;
	@Autowired
	private IKzbglService kzbglService;
	@Autowired
	private IKzbmxService kzbmxService;
	@Autowired
	private IFzjcjgService fzjcjgService;
	@Autowired
	private ICsmxxService csmxxService;
	@Value("${matridx.wechat.applicationurl:}")
	private String address;
	private final Logger log = LoggerFactory.getLogger(DetectionController.class);
	/**
	 * 增加一个读取身份信息的页面
	 */
	@RequestMapping("/detection/pageIDCard")
	public ModelAndView pageIDCard(HzxxDto hzxxDto) {
		ModelAndView mav = new ModelAndView("detection/idcard/idcard_view");
		JcsjDto jcsjDto = new JcsjDto();//找到微信标本类型的鼻咽拭子

		jcsjDto.setJclb(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());//查找默认样本类型使用的
		jcsjDto.setCsdm("SH");
		jcsjDto = jcsjService.getDtoByCsdmAndJclb(jcsjDto);

		JcsjDto jcsj_covid = new JcsjDto();//查找检测类型为新冠的jcsj，得到csid，作为下一级查找的fcsid
		jcsj_covid.setJclb(BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		jcsj_covid.setCsdm("TYPE_COVID");
		jcsj_covid = jcsjService.getDtoByCsdmAndJclb(jcsj_covid);

		JcsjDto jcsj_jcxm = new JcsjDto();//查找上级限制为新冠的检测项目基础数据
		jcsj_jcxm.setJclb(BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		jcsj_jcxm.setFcsid(jcsj_covid.getCsid());
		List<JcsjDto> fzjcxmlist = jcsjService.getDtoList(jcsj_jcxm);

		hzxxDto.setYyjcrq(   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))   );

		mav.addObject("wxbblxDto", jcsjDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MOLECULAR_ITEM,
				BasicDataTypeEnum.IDCARD_CATEGORY,BasicDataTypeEnum.PROVINCE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.WECGATSAMPLE_TYPE});
		//新冠项目
		mav.addObject("fzjcxmlist", fzjcxmlist);
		if(jclist.get(BasicDataTypeEnum.MOLECULAR_ITEM.getCode())!=null&&jclist.get(BasicDataTypeEnum.MOLECULAR_ITEM.getCode()).size()>0) {
			mav.addObject("xg", jclist.get(BasicDataTypeEnum.MOLECULAR_ITEM.getCode()).get(0).getCsid());
		}
		mav.addObject("idcardlist", jclist.get(BasicDataTypeEnum.IDCARD_CATEGORY.getCode()));
		mav.addObject("hzxxDto",hzxxDto);
		List<Map<String,String>> sjdwxxList= fzjcxxService.getSjdw();
		mav.addObject("sjdwxxList",sjdwxxList);
		JcsjDto jsDto = new JcsjDto();
		jsDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jsDto.setCskz3("杭州市");
		List<JcsjDto> jcdwxxList= jcsjService.getDtoByJclbAndCskz(jsDto);
		mav.addObject("jcdwxxList",jcdwxxList );
		if(jcdwxxList !=null&&jcdwxxList.size()>0) {
			mav.addObject("jcdhz",jcdwxxList.get(0).getCsid() );
		}else {
			mav.addObject("jcdhz","" );
		}
    	mav.addObject("sflist",jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		mav.addObject("wxbblxList", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
		List<JcsjDto> jclxtypes = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode());
		for (JcsjDto jcsj_jclx : jclxtypes) {
			if ( "1".equals(jcsj_jclx.getSfmr()) ){
				mav.addObject("jclxmr_csid", jcsj_jclx.getCsid() );//默认检测类型
			}
		}
		//检测类型（分子检测子项目），根据分子检测项目进行限制
		List<JcsjDto> jclxDtos = new ArrayList<>();
		for (JcsjDto jcsj_fzjcxm : fzjcxmlist) {
			for (JcsjDto jcsj_jclx : jclxtypes) {
				if ( StringUtil.isNotBlank(jcsj_fzjcxm.getCsdm()) ){
					if ( jcsj_fzjcxm.getCsid().equals(jcsj_jclx.getFcsid()) ){
						jclxDtos.add(jcsj_jclx);
					}
				}
			}
		}

		mav.addObject("jclxtypes", jclxDtos );//检测类型
		mav.addObject("menuurl", menuurl);
		XtszDto xtszDto = xtszService.selectById("xg_zfje");
		String fkje ="";
		if (null!=xtszDto){
			fkje  =  xtszDto.getSzz();//获取token 访问令牌
		}
		mav.addObject("fkje", fkje);
		mav.addObject("cydlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.COLLECT_SAMPLES.getCode()) );//采样点
		return mav;
	}

	@RequestMapping(value="/detection/minidataAfterAppoint")
	@ResponseBody
    public Map<String, Object> minidataAfterAppoint(HzxxDto hzxxDto){
    	Map<String, Object> map= new HashMap<>();
		DBEncrypt crypt = new DBEncrypt();
		//前端页面的证件号存在hzxx的zjh字段中，现在有可能证件号为fzjcxx的主键ID，根据长度判断分为不同情况查询
		if (hzxxDto.getZjh().length()>30){
			//32位的是fzicxx的主键ID，用主键查找时候还要增加限制为预约数据，不能为
            FzjcxxDto fzjcxxDto = new FzjcxxDto();
            HzxxDto hzxx = null;
            fzjcxxDto.setFzjcid(hzxxDto.getZjh());
            fzjcxxDto.setJclx(hzxxDto.getJclx());
			List<JcsjDto> jclxs = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
			if(jclxs.size()>0){
				for (JcsjDto jcsjDto:jclxs){
					if (fzjcxxDto.getJclx().equals(jcsjDto.getCsdm())){
						fzjcxxDto.setJclx(jcsjDto.getCsid());
						break;
					}
				}
			}
            fzjcxxDto = fzjcxxService.getFzjcxxDto(fzjcxxDto);//取删除标记为0的数据
            //根据分子检测ID能获取数据，代表该数据已经录入，没有录入则报错
            if(fzjcxxDto!=null) {
            	//已经录入但已经采集过，则报错，不允许重复采集
            	if(StringUtil.isBlank(fzjcxxDto.getCjsj())) {
					hzxxDto.setHzid(fzjcxxDto.getHzid());
                    hzxx = hzxxService.getHzxxByHzid(hzxxDto);
                    if (hzxx!=null && StringUtil.isNotBlank(hzxx.getSj())){
                    	hzxx.setSj(crypt.dCode(hzxx.getSj()));
						hzxx.setZjh(crypt.dCode(hzxx.getZjh()));
                    }
            	}
            }
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(FzjcxxDto.class, "fzjcid","fkbj","pt","wxid","tw","yyjcrq","yyxxcskz1","sjdwmc","sjdw","ks","yblx","qtks","jcdw","jcxmid");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
            map.put("fzjcxxDto",JSONObject.toJSONString(fzjcxxDto,listFilters));
			SimplePropertyPreFilter filter_t = new SimplePropertyPreFilter(HzxxDto.class, "xm","zjh","sj","hzid","xb","sf","cs","xxdz","nl");
			SimplePropertyPreFilter[] listFilters_t = new SimplePropertyPreFilter[1];
			listFilters_t[0] = filter_t;
			map.put("hzxxDto",JSONObject.toJSONString(hzxx,listFilters_t));
		}else {
			//15或者18位的是证件号
			hzxxDto.setZjh(crypt.eCode(hzxxDto.getZjh()));//加密用于内部查找
			List<JcsjDto> jclxs = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
			if(jclxs.size()>0){
				for (JcsjDto jcsjDto:jclxs){
					if (hzxxDto.getJclx().equals(jcsjDto.getCsdm())){
						hzxxDto.setJclx(jcsjDto.getCsid());
						break;
					}
				}
			}
			map = fzjcxxService.getAppointList(hzxxDto);
			FzjcxxDto fzjcxxDto = (FzjcxxDto) map.get("fzjcxxDto");
			if ( fzjcxxDto != null && StringUtil.isBlank(fzjcxxDto.getSjdw())){
				YyxxDto yyxxDto = new YyxxDto();
				yyxxDto.setSearchParam("散单");
				List<YyxxDto> yyxxDtoList = yyxxService.selectHospitalName(yyxxDto);
				if (yyxxDtoList != null && yyxxDtoList.size()>0 ){
					fzjcxxDto.setSjdw(yyxxDtoList.get(0).getDwid());
					fzjcxxDto.setDwmc(yyxxDtoList.get(0).getDwmc());
				}
			}
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(FzjcxxDto.class, "fzjcid","fkbj","pt","wxid","tw","yyjcrq","yyxxcskz1","sjdwmc","sjdw","ks","yblx","qtks","jcdw","jcxmid");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("fzjcxxDto",JSONObject.toJSONString(map.get("fzjcxxDto"),listFilters));
			SimplePropertyPreFilter filter_t = new SimplePropertyPreFilter(HzxxDto.class, "xm","zjh","sj","hzid","xb","sf","cs","xxdz","nl");
			SimplePropertyPreFilter[] listFilters_t = new SimplePropertyPreFilter[1];
			listFilters_t[0] = filter_t;
			map.put("hzxxDto",JSONObject.toJSONString(map.get("hzxxDto"),listFilters_t));
		}
    	return map;
    }

	@RequestMapping(value="/detection/pagedataAfterAppointOa")
	@ResponseBody
	public Map<String, Object> afterAppointOa(HzxxDto hzxxDto){
		Map<String, Object> map= new HashMap<>();
		DBEncrypt crypt = new DBEncrypt();
		//前端页面的证件号存在hzxx的zjh字段中，现在有可能证件号为fzjcxx的主键ID，根据长度判断分为不同情况查询
		if (hzxxDto.getZjh().length()>30){
			//32位的是fzicxx的主键ID，用主键查找时候还要增加限制为预约数据，不能为
			FzjcxxDto fzjcxxDto = new FzjcxxDto();
			HzxxDto hzxx = null;
			fzjcxxDto.setFzjcid(hzxxDto.getZjh());
			fzjcxxDto = fzjcxxService.getFzjcxxDto(fzjcxxDto);//取删除标记为0的数据
			//根据分子检测ID能获取数据，代表该数据已经录入，没有录入则报错
			if(fzjcxxDto!=null) {
				//已经录入但已经采集过，则报错，不允许重复采集
				if(StringUtil.isBlank(fzjcxxDto.getCjsj())) {
					hzxxDto.setHzid(fzjcxxDto.getHzid());
					hzxx = hzxxService.getHzxxByHzid(hzxxDto);
					if (hzxx!=null && StringUtil.isNotBlank(hzxx.getSj())){
						hzxx.setSj(crypt.dCode(hzxx.getSj()));
						hzxx.setZjh(crypt.dCode(hzxx.getZjh()));
					}
				}
			}
			map.put("fzjcxxDto",fzjcxxDto);
			map.put("hzxxDto",hzxx);
		}else {
			//15或者18位的是证件号
			hzxxDto.setZjh(crypt.eCode(hzxxDto.getZjh()));//加密用于内部查找
			map = fzjcxxService.getAppointList(hzxxDto);
			FzjcxxDto fzjcxxDto = (FzjcxxDto) map.get("fzjcxxDto");
			if ( fzjcxxDto != null && StringUtil.isBlank(fzjcxxDto.getSjdw())){
				YyxxDto yyxxDto = new YyxxDto();
				yyxxDto.setSearchParam("散单");
				List<YyxxDto> yyxxDtoList = yyxxService.selectHospitalName(yyxxDto);
				if (yyxxDtoList != null && yyxxDtoList.size()>0 ){
					fzjcxxDto.setSjdw(yyxxDtoList.get(0).getDwid());
					fzjcxxDto.setDwmc(yyxxDtoList.get(0).getDwmc());
				}
			}
			map.put("fzjcxxDto",map.get("fzjcxxDto"));
			map.put("hzxxDto",map.get("hzxxDto"));
		}
		return map;
	}
	@RequestMapping(value="/detection/pagedataOverdueAppoint")

	@ResponseBody
    public Map<String, Object> pagedatatOverdueAppoint(HzxxDto hzxxDto){
    	Map<String, Object> map= new HashMap<>();
		//查找过期预约数据根据证件号查找
		if(hzxxDto.getZjh().length()>18){
			//传入的是检测id，有检测id取到证件号
			String hzid = fzjcxxService.getHzidByFzjcid(hzxxDto.getZjh());
			hzxxDto.setHzid(hzid);
			hzxxDto.setZjh("");//前端的zjh传入检测ID，已经找出hzid故zjh置为空字符
		}else {
			DBEncrypt crypt = new DBEncrypt();
			hzxxDto.setZjh(crypt.eCode(hzxxDto.getZjh()));
		}
    	List<FzjcxxDto> fzjclist;
    	fzjclist = fzjcxxService.overdueAppoint(hzxxDto);
    	map.put("rows",fzjclist);
    	map.put("hzxxDto",hzxxDto);
    	if(fzjclist!=null) {
    	map.put("total", fzjclist.size());
    	}else {
        	map.put("total", 0);

    	}
    	return map;
    }
	
	@RequestMapping(value="/detection/pagedataSubmitAppoint")
	@ResponseBody
    public Map<String, String> submitAppoint(FzjcxxDto fzjcxxDto, HzxxDto hzxxDto) throws Exception{
    	Map<String, String> map= new HashMap<>();
    	User user = getLoginInfo();
    	hzxxDto.setXgry(user.getYhid());
    	fzjcxxDto.setLrry(user.getYhid());
    	fzjcxxDto.setCjry(user.getYhid());
    	Map<String, String> retMap;
		retMap = fzjcxxService.checkAppointData(fzjcxxDto,hzxxDto);
		String otherUrl = address+"/ws/getInfoByNbbhOrYbbh";
		String openUrl = otherUrl+"?nbbm=" + fzjcxxDto.getBbzbh();
		String sign = commonService.getSign(fzjcxxDto.getBbzbh());
		sign = URLEncoder.encode(sign, StandardCharsets.UTF_8);

		map.put("ResponseSign", sign);
		map.put("RequestLocalCode", URLEncoder.encode("新冠录入打印",StandardCharsets.UTF_8));
		map.put("openUrl", openUrl);
		////ybbh一定是有值的，不存在无值的情况，在预约确认时新增或者更新会赋值ybbh
		map.put("ybbh_ym",retMap.get("ybbh_ym"));
        map.put("xh",retMap.get("xh"));
        if (StringUtil.isNotBlank(retMap.get("sfxz"))){
			map.put("sfxz",retMap.get("sfxz"));
			map.put("fzjcid",retMap.get("fzjcid"));
		}
		return map;
    }
	
	
	/*
	 * 编辑患者信息到数据库（新增和修改患者信息）
	 */
	@RequestMapping(value="/detection/pagedataEditorHzxx")
	@ResponseBody
    public Map<String, Object> editorHzxx(HzxxDto hzxxDto){
    	Map<String, Object> map= new HashMap<>();
		DBEncrypt crypt = new DBEncrypt();
    	//患者个人信息编辑时，查找数据库，有则更新并确认，无则新增
		User user = getLoginInfo();
		if ( StringUtil.isNotBlank( hzxxDto.getSf() ) ){
			if (!"-1".equals(hzxxDto.getSf())){
				JcsjDto jc_sf = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE, hzxxDto.getSf());//  jcsjService.getDtoById(hzxxDto.getSf());
				if (jc_sf != null && StringUtil.isNotBlank(jc_sf.getCsmc())){
					hzxxDto.setSfmc(jc_sf.getCsmc());
				}
			}
		}
		if(StringUtil.isNotBlank( hzxxDto.getCs() )){
			if (!"-1".equals(hzxxDto.getCs())){
				JcsjDto jc_cs = redisUtil.hgetDto("matridx_jcsj:"+BasicDataTypeEnum.CITY, hzxxDto.getCs());//jcsjService.getDtoById(hzxxDto.getCs());
				if (jc_cs != null && StringUtil.isNotBlank(jc_cs.getCsmc())){
					hzxxDto.setCsmc(jc_cs.getCsmc());
				}
			}
		}
		hzxxDto.setSj(  crypt.eCode(hzxxDto.getSj())  );
		hzxxDto.setZjh( crypt.eCode(hzxxDto.getZjh()) );
		HzxxDto hzxx = hzxxService.getHzxxByZjh(hzxxDto);//通过证件号和证件类型查询
    	boolean isSuccess;
		if (hzxx == null) {
			//查找不到进行患者信息, 新增
			hzxxDto.setHzid(StringUtil.generateUUID());
			hzxxDto.setLrry(user.getYhid());
			boolean isInsert = hzxxService.insertDto(hzxxDto);
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.HZXX_ADD.getCode() + JSONObject.toJSONString(hzxxDto));
			isSuccess = isInsert;
		}else {
			//查找到患者信息, 更新并确认
			hzxxDto.setXgry(user.getYhid());
			hzxxDto.setHzid(hzxx.getHzid());
			boolean isUpdate = hzxxService.updateDto(hzxxDto);
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.HZXX_MOD.getCode() + JSONObject.toJSONString(hzxxDto));
			isSuccess = isUpdate;
		}
		//存储之后页面要回显手机号，解码手机号回显
		hzxxDto.setSj( crypt.dCode(hzxxDto.getSj()) );
		map.put("hzxxDto",hzxxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
    }
	/*
	 * 钉钉小程序编辑患者信息到数据库（新增和修改患者信息）
	 */
	@RequestMapping(value="/detection/minidataEditorHzxx")
	@ResponseBody
	public Map<String, Object> minidataEditorHzxx(HzxxDto hzxxDto){
		return editorHzxx(hzxxDto);
	}

	/**
	 * 取样列表
	 */
	@RequestMapping("/detection/acceptSample")
	public ModelAndView acceptSample(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/sampleAccept/sampleAccept");
		mav.addObject("jclx",fzjcxxDto.getJclx());
		Map<String, List<JcsjDto>> bbztlist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.XG_SAMPLESTATE});
		mav.addObject("bbztlist", bbztlist.get(BasicDataTypeEnum.XG_SAMPLESTATE.getCode()));
		User user=getLoginInfo();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlbs(new String[]{PersonalSettingEnum.PRINT_ADDRESS.getCode(),PersonalSettingEnum. KZBGL_SJDW.getCode()});
		grszDto_t.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto_t);
		mav.addObject("grszDto", grszDtoMap.get(PersonalSettingEnum.PRINT_ADDRESS.getCode()) != null ? grszDtoMap.get(PersonalSettingEnum.PRINT_ADDRESS.getCode()) : new GrszDto());
		FzjcxxDto fzjcxxDtoInfo = new FzjcxxDto();
		List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
		if (StringUtil.isNotBlank(fzjcxxDto.getYbbh())){
			try {
				fzjcxxDtoInfo = fzjcxxService.getSampleAcceptInfo(fzjcxxDto);
				fzjcxxDtos = fzjcxxService.getSampleAcceptInfoList(fzjcxxDto);
			} catch (BusinessException e) {
				mav.addObject("message", e);
			}
		}else{
			if (StringUtil.isBlank(fzjcxxDtoInfo.getBbzt())){
				for (JcsjDto jcsj:bbztlist.get(BasicDataTypeEnum.XG_SAMPLESTATE.getCode())) {
					if ("1".equals(jcsj.getSfmr())){
						fzjcxxDtoInfo.setBbzt(jcsj.getCsid());
						break;
					}
				}
			}
		}
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		//查个人设置检查单位
		GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.KZBGL_SJDW.getCode());
		JcsjDto jcsjDto = new JcsjDto();
		if (grszxx != null) {
			jcsjDto = jcsjService.getDtoById(grszxx.getSzz());
		}
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
		mav.addObject("mrjcdw", jcsjDto);
		mav.addObject("jcdwList",jcdwList);
		mav.addObject("fzjcxxDtos", fzjcxxDtos);
		mav.addObject("fzjcxxDtoInfo", fzjcxxDtoInfo);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 查询患者信息
	 */
	@RequestMapping("/detection/pagedataListHzxx")
	@ResponseBody
	public Map<String, Object> pagedataListHzxx(FzjcxxDto fzjcxxDto){
		List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
		if (StringUtil.isNotBlank(fzjcxxDto.getYbbh())){
			try {
				fzjcxxDtos = fzjcxxService.getSampleAcceptInfoList(fzjcxxDto);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> map= new HashMap<>();
		map.put("rows", fzjcxxDtos);
		map.put("total", fzjcxxDto.getTotalNumber());
		return map;
	}
	/**
	 * 查找样品信息
	 */
	@RequestMapping("/detection/pagedataSampleAcceptInfo")
	@ResponseBody
	public Map<String, Object> pagedataSampleAcceptInfo(FzjcxxDto fzjcxxDto, HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		if (StringUtil.isNotBlank(user.getYhid())){
			fzjcxxDto.setXgry(user.getYhid());
			fzjcxxDto.setJsry(user.getYhid());
		}
		FzjcxxDto fzjcxxDtoInfo  = null;
		List<FzjcxxDto> fzjcxxDtos = new ArrayList<>();
		try {
			fzjcxxDtoInfo = fzjcxxService.getSampleAcceptInfo(fzjcxxDto);
			fzjcxxDtos = fzjcxxService.getSampleAcceptInfoList(fzjcxxDto);
		} catch (BusinessException e) {
			map.put("message", e);
		}
		map.put("fzjcxxDtos",fzjcxxDtos);
		map.put("fzjcxxDtoInfo", fzjcxxDtoInfo);
		return map;
	}

	/**
	 * 查找样品信息
	 */
	@RequestMapping("/detection/updateInfoByNbbh")
	@ResponseBody
	public Map<String, Object> updateInfoByNbbh(FzjcxxDto fzjcxxDto, HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		if (StringUtil.isNotBlank(user.getYhid())){
			fzjcxxDto.setSyry(user.getYhid());
		}
		Boolean success = fzjcxxService.updateInfoByNbbh(fzjcxxDto);
		if (success){
			map.put("success", "实验成功!");
		}else {
			map.put("fail", "实验失败!");
		}
		return map;
	}
	/**
	 * 打印标签
	 */
	@RequestMapping("/detection/acceptSaveSample")
	@ResponseBody
	public Map<String, Object> printCheckReport(FzjcxxDto fzjcxxDto, GrszDto grszDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		if (StringUtil.isNotBlank(fzjcxxDto.getSfsy())){
			if (fzjcxxDto.getSfsy().equals("1")){
				fzjcxxDto.setSyry(user.getYhid());
			}
		}
		try {
			if ("1".equals(grszDto.getGrsz_flg())) {
				// 添加个人设置
				grszDto.setYhid(user.getYhid());
				grszDto.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
				grszService.delete(grszDto);
				grszDto.setSzid(StringUtil.generateUUID());
				grszService.insert(grszDto);
			}
			map.put("sz_flg", grszDto.getSzz());
			// 获取用户信息
			fzjcxxDto.setXgry(user.getYhid());
			fzjcxxDto.setJsry(user.getYhid());
			String successMessage = xxglService.getModelById("ICOM00001").getXxnr();
			boolean result = true;
			String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			String prefix = date + "-";
			FzjcxxDto fzjcxxDto1 = fzjcxxService.getSampleAcceptInfo(fzjcxxDto);
			if(fzjcxxDto1==null){
				map.put("status","fail");
				map.put("message","标本编号不存在！");
				return map;
			}
			if (StringUtil.isNotBlank(fzjcxxDto.getNbbh())) {
				if (StringUtil.isNotBlank(fzjcxxDto1.getNbbh())) {
					result = !fzjcxxDto1.getNbbh().equals(fzjcxxDto.getNbbh());
				}
				if (result){
					List<FzjcxxDto> fzjcxxDtoList = fzjcxxService.getFzjcxxByNbbh(fzjcxxDto);
					if (fzjcxxDtoList.size() > 1) {
						String confirm_nbbm = fzjcxxService.generateYbbh(fzjcxxDto);
						if (confirm_nbbm != null) {
							// 采用最新的流水号
							fzjcxxDto.setNbbh(confirm_nbbm);
							// 发送消息通知操作人员
							successMessage = successMessage + "  当前内部编码已经存在,已为您替换为最新的内部编码 [" + confirm_nbbm + "]";
						}
					}
				}

			}
			if (StringUtil.isNotBlank(fzjcxxDto1.getSyh()) && StringUtil.isNotBlank(fzjcxxDto.getSyh()) ) {
				result = !fzjcxxDto1.getSyh().equals(fzjcxxDto.getSyh());
			}
			if (result){
				fzjcxxDto.setSyh(prefix+fzjcxxDto.getSyh());
				List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
				if(jclxList.size()>0){
					for(JcsjDto jcsjDto : jclxList){
						if("TYPE_COVID".equals(jcsjDto.getCsdm()))
							fzjcxxDto.setJclx(jcsjDto.getCsid());
					}
				}
				List<FzjcxxDto> fzjcxxDtos = fzjcxxService.getFzjcxxBySyh(fzjcxxDto);
				if (fzjcxxDtos.size() >= 1){
					String syh = fzjcxxService.getSyhSerial(fzjcxxDto.getJclx());
					if (syh != null) {

						fzjcxxDto.setSyh(syh);
						// 发送消息通知操作人员
						successMessage = successMessage + "  当前实验号已经存在,已为您替换为最新的实验号 [" + syh + "]";
					}
				}
			}
			// 采用最新的流水号
			String syh  = fzjcxxDto.getSyh();
			String[] syhs =syh.split("-");
			if (syhs.length >1){
				syh = syhs[1];
			}else{
				fzjcxxDto.setSyh(prefix+fzjcxxDto.getSyh());
			}
			FzjcxxDto fzjcxxDto2 = new FzjcxxDto();
			List<String> ybbhlist = new ArrayList<>();
			List<String> idslist = new ArrayList<>();
			ybbhlist.add(fzjcxxDto.getYbbh());
			fzjcxxDto2.setYbbhs(ybbhlist);
			List<FzjcxxDto> list = fzjcxxService.getFzjcxxByybbhs(fzjcxxDto2);
			for (FzjcxxDto fzjcxx: list) {
				idslist.add(fzjcxx.getFzjcid());
			}
			fzjcxxDto2.setIds(idslist);
//			if ("R".equals(fzjcxxDto.getJcdxcsdm())){
//				fzjcxxService.cjsjReport(fzjcxxDto2);
//			}
			fzjcxxService.saveFzjcxxInfo(fzjcxxDto);
			//cz为1时执行新增扩增板，为0则是在已有的扩增板上进行新增或修改明细
			if("1".equals(fzjcxxDto.getCz())){
				KzbmxDto kzbmxDto_t=new KzbmxDto();
				kzbmxDto_t.setYbbh(fzjcxxDto.getYbbh());
				KzbmxDto dtoByYbbh = kzbmxService.getDtoByYbbh(kzbmxDto_t);
				//判断样本编号是否重复
				if(dtoByYbbh!=null){
					map.put("status","fail");
					map.put("message","此标本编号已保存，请更换！");
					return map;
				}
				KzbglDto kzbglDto=new KzbglDto();
				kzbglDto.setKzbh(fzjcxxDto.getKzbmc());
				KzbglDto dto = kzbglService.getDto(kzbglDto);
				//判断扩增板是否重复
				if(dto!=null){
					map.put("status","fail");
					map.put("message","扩增板名称已存在！");
					return map;
				}
				boolean isSuccess = kzbglService.insertKzb(kzbglDto, fzjcxxDto, user);
				if(!isSuccess){
					map.put("status","fail");
					map.put("message","新增扩增板信息失败！");
					return map;
				}
				//返回数据用于更新页面数据
				map.put("kzbid",kzbglDto.getKzbid());
				map.put("cz",fzjcxxDto.getCz());
				map.put("hs",fzjcxxDto.getHs());
				map.put("ls",fzjcxxDto.getLs());
				map.put("syh",syh);
				map.put("kzbxh",fzjcxxDto.getKzbxh());
			}else if("0".equals(fzjcxxDto.getCz())){
				KzbmxDto kzbmxDto=new KzbmxDto();
				kzbmxDto.setKzbid(fzjcxxDto.getKzbmc_sel());
				kzbmxDto.setHs(fzjcxxDto.getHs());
				kzbmxDto.setLs(fzjcxxDto.getLs());
				KzbmxDto dto = kzbmxService.getDto(kzbmxDto);
				//获取当前位置的数据，如果存在则执行修改，如果不存在，则执行新增
				if(dto!=null){
					if(!(dto.getYbbh().equals(fzjcxxDto.getYbbh()))){
						KzbmxDto kzbmxDto_t=new KzbmxDto();
						kzbmxDto_t.setYbbh(fzjcxxDto.getYbbh());
						KzbmxDto dtoByYbbh = kzbmxService.getDtoByYbbh(kzbmxDto_t);
						//判断样本编号是否重复
						if(dtoByYbbh!=null){
							map.put("status","fail");
							map.put("message","此标本编号已保存，请更换！");
							return map;
						}
					}
					kzbmxDto.setKzbmxid(dto.getKzbmxid());
					kzbmxDto.setXh(dto.getXh());
					boolean isSuccess = kzbmxService.updateKzbmx(kzbmxDto, fzjcxxDto, user);
					if(!isSuccess){
						map.put("status","fail");
						map.put("message","更新扩增板明细失败！");
						return map;
					}
					map.put("sqlcz","update");
				}else{
					KzbmxDto kzbmxDto_t=new KzbmxDto();
					kzbmxDto_t.setYbbh(fzjcxxDto.getYbbh());
					KzbmxDto dtoByYbbh = kzbmxService.getDtoByYbbh(kzbmxDto_t);
					//判断样本编号是否重复
					if(dtoByYbbh!=null){
						map.put("status","fail");
						map.put("message","此标本编号已保存，请更换！");
						return map;
					}
					kzbmxDto.setKzbmxid(StringUtil.generateUUID());
					boolean isSuccess = kzbmxService.insertKzbmx(kzbmxDto, fzjcxxDto, user);
					if(!isSuccess){
						map.put("status","fail");
						map.put("message","新增扩增板明细失败！");
						return map;
					}
					map.put("sqlcz","insert");
				}
				map.put("kzbid",fzjcxxDto.getKzbmc_sel());
				map.put("cz",fzjcxxDto.getCz());
				map.put("hs",fzjcxxDto.getHs());
				map.put("ls",fzjcxxDto.getLs());
				map.put("syh",syh);
				map.put("kzbxh",fzjcxxDto.getKzbxh());
			}
			map.put("status","success");
			map.put("message",(successMessage));
			if ("1".equals(fzjcxxDto.getSfjs())) {
				String sign = commonService.getSign(fzjcxxDto.getNbbh());
				String type = PrintEnum.PRINT_XGQY.getCode();
				sign = URLEncoder.encode(sign, StandardCharsets.UTF_8);
//				String url = ":8081/Print?RequestLocalCode=新冠取样打印&ResponseSign=" + sign + "&ResponseNum=3&fontflg=2&type="+type+"&nbbh="+fzjcxxDto.getNbbh()+"&ybbh="+fzjcxxDto.getYbbh()+"&syh="+syh;
				String url = ":8081/Print";
				String RequestLocalCode = URLEncoder.encode("新冠取样打印",StandardCharsets.UTF_8);
				map.put("ResponseSign",sign);
				map.put("type",type);
				map.put("RequestLocalCode",RequestLocalCode);
				map.put("nbbh",fzjcxxDto.getYbbh());
				map.put("ybbh",fzjcxxDto.getYbbh());
				map.put("syh",syh);
				map.put("print", url);
//					log.error("打印地址：" + url);
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}
	/**
	 * 扩增板插入特殊标签
	 */
	@RequestMapping("/detection/insertKzbInfo")
	@ResponseBody
	public Map<String, Object> insertKzbInfo(FzjcxxDto fzjcxxDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		String successMessage = xxglService.getModelById("ICOM00001").getXxnr();
		if(StringUtil.isBlank(fzjcxxDto.getSyh())){
			fzjcxxDto.setSyh(fzjcxxDto.getYbbh());
		}
		try {
			if("1".equals(fzjcxxDto.getCz())){
				KzbglDto kzbglDto=new KzbglDto();
				kzbglDto.setKzbh(fzjcxxDto.getKzbmc());
				KzbglDto dto = kzbglService.getDto(kzbglDto);
				if(dto!=null){
					map.put("status","fail");
					map.put("message","扩增板名称已存在！");
					return map;
				}
				boolean isSuccess = kzbglService.insertKzb(kzbglDto, fzjcxxDto, user);
				if(!isSuccess){
					map.put("status","fail");
					map.put("message","新增扩增板信息失败！");
					return map;
				}
				map.put("kzbid",kzbglDto.getKzbid());
				map.put("cz",fzjcxxDto.getCz());
				map.put("hs",fzjcxxDto.getHs());
				map.put("ls",fzjcxxDto.getLs());
				map.put("syh",fzjcxxDto.getSyh());
				map.put("kzbxh",fzjcxxDto.getKzbxh());
			}else if("0".equals(fzjcxxDto.getCz())){
				KzbmxDto kzbmxDto=new KzbmxDto();
				kzbmxDto.setKzbid(fzjcxxDto.getKzbmc_sel());
				kzbmxDto.setHs(fzjcxxDto.getHs());
				kzbmxDto.setLs(fzjcxxDto.getLs());
				KzbmxDto dto = kzbmxService.getDto(kzbmxDto);
				//获取当前位置的数据，如果存在则执行修改，如果不存在，则执行新增
				if(dto!=null){
					if(!(dto.getYbbh().equals(fzjcxxDto.getYbbh()))){
						KzbmxDto kzbmxDto_t=new KzbmxDto();
						kzbmxDto_t.setYbbh(fzjcxxDto.getYbbh());
						kzbmxDto_t.setKzbid(fzjcxxDto.getKzbmc_sel());
						KzbmxDto dtoByYbbh = kzbmxService.getDtoByYbbh(kzbmxDto_t);
						if(dtoByYbbh!=null){
							map.put("status","fail");
							map.put("message","此标本编号已保存，请更换！");
							return map;
						}
					}
					kzbmxDto.setKzbmxid(dto.getKzbmxid());
					kzbmxDto.setXh(dto.getXh());
					boolean isSuccess = kzbmxService.updateKzbmx(kzbmxDto, fzjcxxDto, user);
					if(!isSuccess){
						map.put("status","fail");
						map.put("message","更新扩增板明细失败！");
						return map;
					}
					map.put("sqlcz","update");
				}else{
					KzbmxDto kzbmxDto_t=new KzbmxDto();
					kzbmxDto_t.setYbbh(fzjcxxDto.getYbbh());
					kzbmxDto_t.setKzbid(fzjcxxDto.getKzbmc_sel());
					KzbmxDto dtoByYbbh = kzbmxService.getDtoByYbbh(kzbmxDto_t);
					//判断样本编号是否重复
					if(dtoByYbbh!=null){
						map.put("status","fail");
						map.put("message","此标本编号已保存，请更换！");
						return map;
					}
					kzbmxDto.setKzbmxid(StringUtil.generateUUID());
					boolean isSuccess = kzbmxService.insertKzbmx(kzbmxDto, fzjcxxDto, user);
					if(!isSuccess){
						map.put("status","fail");
						map.put("message","新增扩增板明细失败！");
						return map;
					}
					map.put("sqlcz","insert");
				}
				map.put("kzbid",fzjcxxDto.getKzbmc_sel());
				map.put("cz",fzjcxxDto.getCz());
				map.put("hs",fzjcxxDto.getHs());
				map.put("ls",fzjcxxDto.getLs());
				map.put("syh",fzjcxxDto.getSyh());
				map.put("kzbxh",fzjcxxDto.getKzbxh());
			}
			map.put("status","success");
			map.put("message",(successMessage));
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}



	/**
	 * 获取所有扩增板信息
	 */
	@RequestMapping(value="/pagedataAllKzb")
	@ResponseBody
	public List<KzbglDto> getAllKzb(){
		KzbglDto kzbglDto=new KzbglDto();
		return kzbglService.getDtoList(kzbglDto);
	}

	/**
	 * 获取所有扩增板明细
	 */
	@RequestMapping(value="/pagedataKzbmx")
	@ResponseBody
	public List<KzbmxDto> getKzbmx(String kzbid){
		List<KzbmxDto> dtoList = kzbmxService.getKzbmxListByKzbid(kzbid);
		if(dtoList!=null&&dtoList.size()>0){
			for(KzbmxDto dto:dtoList){
				String[] split = dto.getSyh().split("-");
				if(StringUtil.isNotBlank(split[0])){
					dto.setSyh(split[1]);
				}
			}
		}
		return dtoList;
	}


	/**
	 * 新冠检测列表
	 */
	@RequestMapping("/detection/pageListCovid")
	public ModelAndView pageListCovid(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_list");
		//区分普检数据
		List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		if(jclxList.size()>0){
			for(JcsjDto jcsjDto : jclxList){
				if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
					fzjcxxDto.setJclx(jcsjDto.getCsid());
			}
		}
		User user=getLoginInfo();
		fzjcxxDto.setAuditType(AuditTypeEnum.AUDIT_COVID.getCode());
//		GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
//		grlbzdszDto.setYhid(getLoginInfo().getYhid());
//		grlbzdszDto.setYwid("COVID");
		LbzdszDto lbzdszDto = new LbzdszDto();
		lbzdszDto.setYwid("COVID");
		lbzdszDto.setYhid(user.getYhid());
		lbzdszDto.setJsid(user.getDqjs());
		List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
//		List<String> zdList = lbzdszService.getSjxxZdList();//从列表字段设置表中获取默认设置为 1显示、0隐藏、3角色限制、9主键的SQL字段
		mav.addObject("waitList", waitList);
		mav.addObject("fzjcxxDto",fzjcxxDto);
		mav.addObject("jcdxlxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_OBJECT.getCode())  );
		List<JcsjDto> fxm = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		List<JcsjDto> zxm = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode());
		fxm.removeIf(next -> !(fzjcxxDto.getJclx().equals(next.getFcsid())));
		List<JcsjDto> zxmlist =new ArrayList<>();
		for (JcsjDto jcsjDto : zxm) {
			for (JcsjDto dto : fxm) {
				if(dto.getCsid().equals(jcsjDto.getFcsid())){
					zxmlist.add(jcsjDto);
				}
			}
		}
		mav.addObject("zxmlist", zxmlist );
		return mav;
	}

	/**
	 * 条件查找列表信息
	 */
	@RequestMapping("/detection/pageGetListCovid")
	@ResponseBody
	public Map<String, Object> pageGetListCovid(FzjcxxDto fzjcxxDto){
		DBEncrypt dbEncrypt=new DBEncrypt();
		Map<String, Object> map= new HashMap<>();
		if(StringUtils.isNotBlank(fzjcxxDto.getSj()))
			fzjcxxDto.setSj(dbEncrypt.eCode(fzjcxxDto.getSj()));
		if(StringUtils.isNotBlank(fzjcxxDto.getZjh()))
			fzjcxxDto.setZjh(dbEncrypt.eCode(fzjcxxDto.getZjh()));
		//区分新冠数据
		List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		if(jclxList.size()>0){
			for(JcsjDto jcsjDto : jclxList){
				if("TYPE_COVID".equals(jcsjDto.getCsdm()))
					fzjcxxDto.setJclx(jcsjDto.getCsid());
			}
		}
		List<FzjcxxDto> pagedDtoList = fzjcxxService.getPagedDtoList(fzjcxxDto);
		for (FzjcxxDto value : pagedDtoList) {
			if (StringUtil.isBlank(value.getXm())) {
				if (StringUtil.isNotBlank(value.getYpmc())) {
					value.setXm(value.getYpmc());
				}
			}
		}
		if(pagedDtoList.size()>0){
			for(FzjcxxDto dto:pagedDtoList){
				String zjh = "";
				if(StringUtil.isNotBlank(dto.getZjh())){
					try {
						zjh = dbEncrypt.dCode(dto.getZjh());
					} catch (Exception e) {
						log.error("证件号解密失败！"+ e);
					}
				}
				dto.setZjh(zjh);
			}
		}
		//若钉钉标记为1，说明为钉钉扫描主编码时调用该方法，解密手机号、证件号返回
		if("1".equals(fzjcxxDto.getDdbj())){
			User user = getLoginInfo();
			fzjcxxDto.setCjry(user.getYhid());
			fzjcxxDto.setCyd(fzjcxxDto.getCyd());
			Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto);
			map.put("gs",countData.get("gs"));
			map.put("rs",countData.get("rs"));
			if (pagedDtoList.size()>0) {
				for (FzjcxxDto dto : pagedDtoList) {
					if(StringUtils.isNotBlank(dto.getSj())){
						dto.setSj(dbEncrypt.dCode(dto.getSj()));
					}
				}
			}
		}
		map.put("rows", pagedDtoList);
		map.put("total", fzjcxxDto.getTotalNumber());
		return map;
	}

	/**
	 * 场所码选择页面
	 */
	@RequestMapping("/detection/checklistPrintCheck")
	public ModelAndView checklistPrintCheck(CsmxxDto csmxxDto) {
		ModelAndView mav=new ModelAndView("detection/covid/covid_check");
		List<CsmxxDto> dtoList = csmxxService.getDtoList(csmxxDto);
		mav.addObject("dtoList",dtoList);
		return mav;
	}

	/**
	 * 打印
	 */
	@RequestMapping("/detection/checklistSavePrintCheck")
	public ModelAndView printDeviceCheck(FzjcxxDto fzjcxxDto) {
		ModelAndView mav=new ModelAndView("detection/covid/covid_print");
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.getCheckDtoList(fzjcxxDto);
		mav.addObject("fzjcxxDtos", fzjcxxDtos);
//		RestTemplate re=new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//		for (FzjcxxDto dto : fzjcxxDtos) {
//			paramMap.add("qrCode",dto.getBbzbh());
//			String reString = re.postForObject(menuurl+"/wechat/getQRCode", paramMap, String.class);
//			dto.setFilePath(menuurl+reString);
//		}
		if (StringUtil.isNotBlank(fzjcxxDto.getPrint_cs())){
			DBEncrypt dbEncrypt=new DBEncrypt();
			String sign = dbEncrypt.eCode(fzjcxxDto.getPrint_cs());
			String url = address + "/ws/detection/checkToken?data="+fzjcxxDto.getPrint_cs()+"&sign="+sign;//address是获取配置的applicationurl
//			paramMap.add("qrCode",url);
//			String reString = re.postForObject(menuurl+"/wechat/getQRCode", paramMap, String.class);
//			mav.addObject("csqrCode", menuurl+reString);
			mav.addObject("csqrCode", url);
		}else{
			mav.addObject("csqrCode", "");
		}
		mav.addObject("fzjcxxDtos_json",JSONObject.toJSONString(fzjcxxDtos));
		return mav;
	}

	/**
	 * 选择查询信息
	 */
	@RequestMapping("/detection/pagedataChecklistSize")
	@ResponseBody
	public Map<String, Object> getChecklistSize(FzjcxxDto fzjcxxDto){
		DBEncrypt dbEncrypt=new DBEncrypt();
		Map<String, Object> map= new HashMap<>();
		if(StringUtils.isNotBlank(fzjcxxDto.getSj()))
			fzjcxxDto.setSj(dbEncrypt.eCode(fzjcxxDto.getSj()));
		if(StringUtils.isNotBlank(fzjcxxDto.getZjh()))
			fzjcxxDto.setZjh(dbEncrypt.eCode(fzjcxxDto.getZjh()));
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.getCheckDtoList(fzjcxxDto);
		map.put("total", fzjcxxDtos.size());
		return map;
	}

	/**
	 * 钉钉条件查找列表信息
	 */
	@RequestMapping("/detection/minidataGetCovidList")
	@ResponseBody
	public Map<String, Object> minidataGetCovidList(FzjcxxDto fzjcxxDto){
		DBEncrypt dbEncrypt=new DBEncrypt();
		Map<String, Object> map= new HashMap<>();
		if(StringUtils.isNotBlank(fzjcxxDto.getSj()))
			fzjcxxDto.setSj(dbEncrypt.eCode(fzjcxxDto.getSj()));
		if(StringUtils.isNotBlank(fzjcxxDto.getZjh()))
			fzjcxxDto.setZjh(dbEncrypt.eCode(fzjcxxDto.getZjh()));
		List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		if(jclxList.size()>0){
			for(JcsjDto jcsjDto : jclxList){
				if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
					fzjcxxDto.setJclx(jcsjDto.getCsid());
			}
		}
		List<FzjcxxDto> pagedDtoList = fzjcxxService.getFzjcxxList(fzjcxxDto);
		if(pagedDtoList!=null&&pagedDtoList.size()>0){
			for(FzjcxxDto dto:pagedDtoList){
				String zjh = "";
				if(StringUtil.isNotBlank(dto.getZjh())){
					try {
						zjh = dbEncrypt.dCode(dto.getZjh());
					} catch (Exception e) {
						log.error("证件号解密失败！"+ e);
					}
				}
				dto.setZjh(zjh);
			}
		}
		//若钉钉标记为1，说明为钉钉扫描主编码时调用该方法，解密手机号、证件号返回
		if("1".equals(fzjcxxDto.getDdbj())){
			User user = getLoginInfo();
			fzjcxxDto.setCjry(user.getYhid());
			fzjcxxDto.setCyd(fzjcxxDto.getCyd());
			Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto);
			map.put("gs",countData.get("gs"));
			map.put("rs",countData.get("rs"));
			if (pagedDtoList!=null && pagedDtoList.size()>0) {
				for (FzjcxxDto dto : pagedDtoList) {
					if(StringUtils.isNotBlank(dto.getSj())){
						dto.setSj(dbEncrypt.dCode(dto.getSj()));
					}
				}
			}
		}
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(FzjcxxDto.class, "bbzbh","ybbh","fzjcid","xm","zjh","sj","hzid","fkbj","pt","fzjczxmid","cyd");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		map.put("rows", JSONObject.toJSONString(pagedDtoList,listFilters));
		map.put("total", pagedDtoList != null ? pagedDtoList.size() : 0);
		return map;
	}


	/**
	 * 新冠检测列表查看详情
	 */
	@RequestMapping("/detection/viewCovidDetails")
	public ModelAndView viewCovidDetails(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_view");
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.viewCovidDetails(fzjcxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(fzjcxxDto.getFzjcid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode());
		List<FjcfbDto> wordList = fzjcxxService.getReport(fjcfbDto);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
		List<FjcfbDto> pdfList = fzjcxxService.getReport(fjcfbDto);
		FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
		List<FzjcxxDto> historyList =new ArrayList<>();
		if("R".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","R");
			historyList = fzjcxxService.getHistoryList(fzjcxxDto_t);
		}else if("W".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","W");
		}
		String sj = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getSj())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				sj = dbEncrypt.dCode(fzjcxxDto_t.getSj());
			} catch (Exception e) {
				log.error("手机号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setSj(sj);
		String zjh = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getZjh())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				zjh = dbEncrypt.dCode(fzjcxxDto_t.getZjh());
			} catch (Exception e) {
				log.error("证件号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setZjh(zjh);

		mav.addObject("wordList", wordList);
		mav.addObject("fzjcxxDto",fzjcxxDto_t);
		mav.addObject("pdfList",pdfList);
		mav.addObject("fzjcxxDtos",fzjcxxDtos);
		mav.addObject("historyList",historyList);
		return mav;
	}


	/**
	 * 历史履历记录
	 */
	@RequestMapping("/detection/historicalrecordsResumeView")
	public ModelAndView historicalrecordsResumeView(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_historicalrecords");
		List<FzjcxxDto> resumeList = fzjcxxService.getResumeList(fzjcxxDto);
		mav.addObject("resumeList",resumeList);
		return mav;
	}

	/**
	 * 历史记录
	 */
	@RequestMapping("/detection/historicalrecordsView")
	public ModelAndView historicalrecordsView(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/history_view");
		FzjcxxDto fzjcxxDto_t=new FzjcxxDto();
		fzjcxxDto_t.setFzjcid(fzjcxxDto.getFzjcid());
		fzjcxxDto_t.setXm(fzjcxxDto.getXm());
		fzjcxxDto_t.setJclx(fzjcxxDto.getJclx());
		List<FzjcxxDto> historyList_t = fzjcxxService.getHistoryList(fzjcxxDto_t);
		fzjcxxDto.setXm(null);
		List<String> ids = new ArrayList<>();
		String jcxmmc = fzjcxxDto.getJcxmmc();
		String[] jcxms = jcxmmc.split(",");
		for(String s:jcxms){
			JcsjDto jcsjDto=new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
			jcsjDto.setCsmc(s);
			ids.add(jcsjService.getDto(jcsjDto).getCsid());
		}

		fzjcxxDto.setIds(ids);
		List<FzjcxxDto> historyList = fzjcxxService.getHistoryList(fzjcxxDto);

		mav.addObject("historyList",historyList);
		mav.addObject("historyList_t",historyList_t);
		return mav;
	}

	/**
	 * 上传新冠检测数据文件页面
	 */
	@RequestMapping("/detection/uploadReportPage")
	public ModelAndView uploadReportPage(FzjcxxDto fzjcxxDto){
		ModelAndView mav=new ModelAndView("detection/covid/covid_report_upload");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MOLECULAR_ITEM});
		fzjcxxDto.setAuditType(AuditTypeEnum.AUDIT_COVID.getCode());
		mav.addObject("fzjcxmDtolist",jclist.get(BasicDataTypeEnum.MOLECULAR_ITEM.getCode()));
		mav.addObject("fjywlx",BusTypeEnum.IMP_REPORT_COVID_DATA.getCode());
		mav.addObject("fzjcxxDto",fzjcxxDto);
		return mav;
	}

	/**
	 * 处理新冠检测报告,上传保存分析，生成新冠报告
	 */
	@RequestMapping("/detection/dealReport")
	@ResponseBody
	public Map<String,Object> dealReport(FzjcxxDto fzjcxxDto) throws BusinessException {
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setSyry(user.getYhid());
		boolean isSuccess = fzjcxxService.dealCovidReport(fzjcxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 删除按钮
	 */
	@RequestMapping("/detection/delCovidDetails")
	@ResponseBody
	public Map<String,Object> delCovidDetails(FzjcxxDto fzjcxxDto) {
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setScry(user.getYhid());
		List<FzjcxxDto> dtoList = fzjcxxService.getDtoList(fzjcxxDto);
		boolean rjflag=false;
		boolean wjflag=false;
		for(FzjcxxDto dto:dtoList){
			if("R".equals(dto.getJcdxcsdm())){
				rjflag=true;
			}else if("W".equals(dto.getJcdxcsdm())){
				wjflag=true;
			}
		}
		if(rjflag&&wjflag){
			map.put("status", "fail");
			map.put("message","所选的检测对象类型必须为同一个！");
		}else{
			boolean isSuccess = fzjcxxService.delCovidDetails(fzjcxxDto);
			if(rjflag){
				amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_DEL.getCode() + JSONObject.toJSONString(fzjcxxDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
					: xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}

	/**
	 * 废弃按钮
	 */
	@RequestMapping("/detection/discardCovidDetails")
	@ResponseBody
	public Map<String,Object> discardCovidDetails(FzjcxxDto fzjcxxDto) {
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setScry(user.getYhid());
		List<FzjcxxDto> dtoList = fzjcxxService.getDtoList(fzjcxxDto);
		boolean rjflag=false;
		boolean wjflag=false;
		for(FzjcxxDto dto:dtoList){
			if("R".equals(dto.getJcdxcsdm())){
				rjflag=true;
			}else if("W".equals(dto.getJcdxcsdm())){
				wjflag=true;
			}
		}
		if(rjflag&&wjflag){
			map.put("status", "fail");
			map.put("message","所选的检测对象类型必须为同一个！");
		}else{
			boolean isSuccess = fzjcxxService.discardCovidDetails(fzjcxxDto);
			if(rjflag){
				amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_DIS.getCode() + JSONObject.toJSONString(fzjcxxDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 查找样品信息
	 */
	@RequestMapping("/detection/pagedataInfoByNbbh")
	@ResponseBody
	public Map<String, Object> pagedataInfoByNbbh(FzjcxxDto fzjcxxDto){
		Map<String, Object> map= new HashMap<>();
		FzjcxxDto fzjcxxDtoInfo = null;
		try {
			fzjcxxDtoInfo = fzjcxxService.getInfoByNbbh(fzjcxxDto);
		} catch (BusinessException e) {
			map.put("message", e);
		}
		map.put("fzjcxxDtoInfo", fzjcxxDtoInfo);
		return map;
	}
	
	/**
	 * 新冠报告审核列表
	 */
	@RequestMapping("/detection/pageListExamineCovid")
	public ModelAndView pageListExamineCovid(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_listaudit");
		List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		if(jclxList.size()>0){
			for(JcsjDto jcsjDto : jclxList){
				if(fzjcxxDto.getJclx().equals(jcsjDto.getCsdm()))
					fzjcxxDto.setJclx(jcsjDto.getCsid());
			}
		}
		mav.addObject("jclx",fzjcxxDto.getJclx());
		mav.addObject("jcdxlxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_OBJECT.getCode())  );
		return mav;
	}
	/**
	 * 新冠报告审核列表待审核
	 */
	@RequestMapping("/detection/pageGetListExamineCovid")
	@ResponseBody
	public Map<String, Object> auditingCovidList(FzjcxxDto fzjcxxDto){
		//附加委托参数
		DataPermission.addWtParam(fzjcxxDto);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(fzjcxxDto.getDqshzt())){
			DataPermission.add(fzjcxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fzjcxx", "fzjcid", AuditTypeEnum.AUDIT_COVID);
		}else{
			DataPermission.add(fzjcxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fzjcxx", "fzjcid", AuditTypeEnum.AUDIT_COVID);
		}
		DataPermission.addCurrentUser(fzjcxxDto, getLoginInfo());
		List<FzjcxxDto> t_List = fzjcxxService.getPagedAuditList(fzjcxxDto);

		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", fzjcxxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}


	/**
	 * 新冠报告审核按钮
	 */
	@RequestMapping("/detection/auditCovidReport")
	public ModelAndView auditCovidReport(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_edit");
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.viewCovidDetails(fzjcxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(fzjcxxDto.getFzjcid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode());
		List<FjcfbDto> wordList = fzjcxxService.getReport(fjcfbDto);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
		List<FjcfbDto> pdfList = fzjcxxService.getReport(fjcfbDto);
		FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
		List<FzjcxxDto> historyList =new ArrayList<>();
		if("R".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","R");
			historyList = fzjcxxService.getHistoryList(fzjcxxDto_t);
		}else if("W".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","W");
		}
		String sj = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getSj())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				sj = dbEncrypt.dCode(fzjcxxDto_t.getSj());
			} catch (Exception e) {
				log.error("手机号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setSj(sj);
		mav.addObject("fzjcxxDto",fzjcxxDto_t);
		mav.addObject("wordList", wordList);
		mav.addObject("pdfList",pdfList);
		mav.addObject("fzjcxxDtos",fzjcxxDtos);
		mav.addObject("historyList",historyList);
		return mav;
	}

	/**
	 * 提交按钮
	 */
	@RequestMapping("/detection/submitCovid")
	public ModelAndView submitCovid(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_view");
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.viewCovidDetails(fzjcxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(fzjcxxDto.getFzjcid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode());
		List<FjcfbDto> wordList = fzjcxxService.getReport(fjcfbDto);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
		List<FjcfbDto> pdfList = fzjcxxService.getReport(fjcfbDto);
		FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
		List<FzjcxxDto> historyList =new ArrayList<>();
		if("R".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","R");
			historyList = fzjcxxService.getHistoryList(fzjcxxDto_t);
		}else if("W".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","W");
		}
		String sj = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getSj())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				sj = dbEncrypt.dCode(fzjcxxDto_t.getSj());
			} catch (Exception e) {
				log.error("手机号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setSj(sj);
		String zjh = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getZjh())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				zjh = dbEncrypt.dCode(fzjcxxDto_t.getZjh());
			} catch (Exception e) {
				log.error("证件号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setZjh(zjh);

		mav.addObject("wordList", wordList);
		mav.addObject("fzjcxxDto",fzjcxxDto_t);
		mav.addObject("pdfList",pdfList);
		mav.addObject("fzjcxxDtos",fzjcxxDtos);
		mav.addObject("historyList",historyList);
		return mav;
	}
	/**
	 * 检测列表修改
	 */
	@RequestMapping("/detection/modCovid")
	public ModelAndView modCovid(FzjcxxDto fzjcxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/covid_list_edit");
		FzjcjgDto fzjcjgDto=new FzjcjgDto();
		fzjcjgDto.setFzxmid(fzjcxxDto.getFzxmid());
		fzjcjgDto.setFzjcid(fzjcxxDto.getFzjcid());
		List<FzjcjgDto> list = fzjcjgService.getListByXmidAndJcid(fzjcjgDto);
		if(list!=null&&list.size()>0){
			mav.addObject("flag","1");
		}else{
			mav.addObject("flag","0");
		}
		FzjcxxDto fzjcxx = fzjcxxService.getDto(fzjcxxDto);
		//物检的检测对象默认为物品
		JcsjDto JcsjDto_jcdxlx = new JcsjDto();
		String wjcdxlxid = "";
		String rjcdxlxid = "";
		List<JcsjDto> jcdxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
		for (JcsjDto jcdx : jcdxList) {
			if ("W".equals(jcdx.getCsdm())){
				wjcdxlxid = jcdx.getCsid();
			}
			if ("R".equals(jcdx.getCsdm())){
				rjcdxlxid = jcdx.getCsid();
			}
			if (fzjcxx.getJcdxlx().equals(jcdx.getCsid())){
				JcsjDto_jcdxlx = jcdx;
			}
		}
		mav.addObject("jcdxlxlist", jcdxList);
		if (StringUtil.isNotBlank(fzjcxx.getJcdxlx()) && wjcdxlxid.equals(fzjcxx.getJcdxlx())){
			mav.addObject("JcsjDto_jcdxlx",JcsjDto_jcdxlx);
		}else {
			fzjcxx.setJcdxlx(rjcdxlxid);
			HzxxDto hzxxDto = new HzxxDto();
			hzxxDto.setHzid(fzjcxx.getHzid());
			HzxxDto hzxx = hzxxService.getHzxxByHzid(hzxxDto);
			if(StringUtil.isNotBlank(hzxx.getXb())) {
				if(hzxx.getXb().equals("1")) {
					hzxx.setXb("男");
				}else if(hzxx.getXb().equals("2")){
					hzxx.setXb("女");
				}else {
					hzxx.setXb("未知");
				}
			}
			mav.addObject("hzxx",hzxx);
			String sj = "";
			if (StringUtil.isNotBlank(hzxx.getSj())) {
				try {
					DBEncrypt dbEncrypt = new DBEncrypt();
					sj = dbEncrypt.dCode(hzxx.getSj());
				} catch (Exception e) {
					log.error("手机号解密失败！" + e);
				}
			}
			hzxx.setSj(sj);
		}
		fzjcxx.setAccess_token(fzjcxxDto.getAccess_token());
		FzjcxmDto fzjcxmDto = new FzjcxmDto();
		fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
		List<FzjcxmDto> fzjcxmDtos = fzjcxmService.getDtoListByFzjcid(fzjcxmDto);
		if (fzjcxmDtos != null && fzjcxmDtos.size()>0){
			fzjcxx.setFzjczxmid(fzjcxmDtos.get(0).getFzjczxmid());
		}
		mav.addObject("fzjcxxDto",fzjcxx);
		List<JcsjDto> jcxmList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		List<JcsjDto> fzlist =new ArrayList<>();
		for (JcsjDto jcxm : jcxmList) {
			if ("JX".equals(jcxm.getCsdm())){
				fzjcxxDto.setJcxmid(jcxm.getCsid());
			}
		}
		for (JcsjDto jcsjDto : jcxmList) {
			if(jcsjDto.getFcsid().equals(fzjcxxDto.getJclx()))	{
				fzlist.add(jcsjDto);
			}
		}
		mav.addObject("fzjcxmlist", fzlist);//检测项目
		mav.addObject("bbztlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.XG_SAMPLESTATE.getCode()));
		JcsjDto jsDto = new JcsjDto();
		jsDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		jsDto.setCskz3("杭州市");
		mav.addObject("jcdwxxList", jcsjService.getDtoByJclbAndCskz(jsDto));
		if(StringUtil.isNotBlank(fzjcxx.getJcxmid())) {
			mav.addObject("strlist", Arrays.asList(fzjcxx.getJcxmid().split(",")));
		}else {
			mav.addObject("strlist", "");
		}
		
		List<Map<String,String>> sjdwxxList= fzjcxxService.getSjdw();
		mav.addObject("sjdwxxList",sjdwxxList);

		mav.addObject("wxbblxList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
        mav.addObject("cydList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.COLLECT_SAMPLES.getCode()));
		JcsjDto jcsjDto = new JcsjDto();
		if (StringUtil.isNotBlank(fzjcxx.getJcdxlx())){
			jcsjDto = jcsjService.getDtoById(fzjcxx.getJcdxlx());
		}
		mav.addObject("JcsjDto_jcdxlx",jcsjDto);
		mav.addObject("formAction","modSaveCovid");
		mav.addObject("fzjczxmlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode()));
		mav.addObject("grszDto", new GrszDto());
		return mav;
	}
	  /**
     * 保存修改
     */
    @RequestMapping(value="/detection/modSaveCovid")
    @ResponseBody
    public Map<String, String> modSaveCovid(FzjcxxDto fzjcxxDto) {
    	Map<String, String> map = new HashMap<>();
    	fzjcxxDto.setXgry(getLoginInfo().getYhid());
		fzjcxxDto.setXgsj(   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   );
		FzjcxxDto fzjcxx = fzjcxxService.getDto(fzjcxxDto);
		if(StringUtil.isNotBlank(fzjcxx.getSyh())){
			List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
			if(jclxList.size()>0){
				for(JcsjDto jcsjDto : jclxList){
					if("TYPE_COVID".equals(jcsjDto.getCsdm()))
						fzjcxxDto.setJclx(jcsjDto.getCsid());
				}
			}
			List<FzjcxxDto> fzjcxxBySyh = fzjcxxService.getExistBySyhYbbh(fzjcxxDto);//混检实验号相同，判断实验号是否存在要除去该标本编号外是否有实验号相同的
			if(fzjcxxBySyh!=null&&fzjcxxBySyh.size()>0){
				map.put("status", "fail");
				map.put("message", "当前实验号已存在，请更改再重试");
				return map;
			}
		}
		if(StringUtil.isNotBlank(fzjcxx.getBbzbh())){
			List<FzjcxxDto> fzjcxxByBbzbh = fzjcxxService.getExistBbzbh(fzjcxxDto);
			if(fzjcxxByBbzbh!=null&&fzjcxxByBbzbh.size()>0){
				map.put("status", "fail");
				map.put("message", "当前标本子编号已存在，请更改再重试");
				return map;
			}
		}
		fzjcxxDto.setLlid(StringUtil.generateUUID());
		boolean isSuccess = fzjcxxService.insertInfoResume(fzjcxxDto);
		if (!isSuccess){
			map.put("status", "fail");
			map.put("message", "保存履历信息失败！请联系管理员解决！");
			return map;
		}
		isSuccess =fzjcxxService.saveEditCovid(fzjcxxDto);
        map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
    }

	/*新冠病毒核酸检测预约信息保存*/
	@RequestMapping(value="/detection/pagedataGoPay")
	public ModelAndView goPay(@RequestParam(name = "payUtl", required = false) String payUtl,@RequestParam(name = "fzjcid", required = false) String fzjcid){
		//诺诺支付
//		String str = payUtl.substring(0,payUtl.length()-2);
//		String url = str.replaceAll("＆","&");
		ModelAndView mav = new ModelAndView("detection/idcard/idcard_pay");
		mav.addObject("payUtl",payUtl);
		mav.addObject("fzjcid",fzjcid);
		return mav;
	}

	/*检测列表设置支付金额*/
	@RequestMapping(value="/detection/paymentZfje")
	public ModelAndView paymentZfje(@RequestParam(name = "fzjcid", required = false) String fzjcid){
		ModelAndView mav = new ModelAndView("detection/covid/covid_pay");
		mav.addObject("fzjcid",fzjcid);
		mav.addObject("menuurl", menuurl);
		XtszDto xtszDto = xtszService.selectById("xg_zfje");
		String fkje ="";
		if (null!=xtszDto){
			fkje  =  xtszDto.getSzz();//获取token 访问令牌
		}
		mav.addObject("fkje", fkje);
		return mav;
	}

	/*查询订单信息*/
	@RequestMapping(value="/detection/minidataPayInfo")
	@ResponseBody
	public Map<String, Object> payInfo(FzjcxxDto fzjcxxDto1){
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(fzjcxxDto1.getFzjcid())){
			FzjcxxDto fzjcxxDto = fzjcxxService.getDto(fzjcxxDto1);
			if (StringUtil.isNotBlank(fzjcxxDto.getFkbj()) && "1".equals(fzjcxxDto.getFkbj())){
				map.put("status","success");
				map.put("message","支付成功！");
				return map;
			}else{
				map.put("status","fail");
				map.put("message","未获取到支付信息！");
				return map;
			}
		}else{
			map.put("status","fail");
			map.put("message","未获取到支付信息！");
		}
		return map;
	}


	/*新冠病毒核酸检测预约信息保存*/
	@RequestMapping(value="/detection/pagedataNuonuoPay")
	@ResponseBody
	public Map<String, String> nuonuoPay(FzjcxxModel fzjcxxModel){
		Map<String, String> map = new HashMap<>();
		if (StringUtil.isNotBlank(fzjcxxModel.getFzjcid())){
			fzjcxxModel.setFkbj("1");
			boolean success = fzjcxxService.update(fzjcxxModel);
			if (success){
				amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxModel));
			}
		}
		map.put("status", "success");
		map.put("message", "更新成功！");
		return map;
	}

	/*新冠病毒核酸检测预约信息保存*/
	@RequestMapping(value="/detection/payCallbackUrl")
	@ResponseBody
	public String payCallbackUrl(FzjcxxModel fzjcxxModel){
		String result = "fail";
		if (StringUtil.isNotBlank(fzjcxxModel.getFzjcid())){
			fzjcxxModel.setFkbj("1");
			boolean success = fzjcxxService.update(fzjcxxModel);
			if (success){
				result = "success";
				amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxModel));
			}
		}
		return result;
	}

	/*查询订单信息*/
	@RequestMapping(value="/detection/nuonuoOrderInfo")
	@ResponseBody
	public Map<String, Object> nuonuoOrderInfo(FzjcxxDto fzjcxxDto1){
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(fzjcxxDto1.getFzjcid())){
			FzjcxxDto fzjcxxDto = fzjcxxService.getDto(fzjcxxDto1);
			if (StringUtil.isBlank(fzjcxxDto.getOrderno())){
				map.put("status","fail");
				map.put("message","未获取到支付信息！");
				return map;
			}
			NNOpenSDK sdk = NNOpenSDK.getIntance();
			String taxnum = NUONUOEnum.SX_TAXNUM.getCode(); // 授权企业税号
			String appKey = NUONUOEnum.SX_APPKEY.getCode();
			String appSecret = NUONUOEnum.SX_APPSECRET.getCode();
			String method1 = NUONUOEnum.ORDER_METHOD.getCode(); // API方法名
			String method;
			method = new String( method1.getBytes(StandardCharsets.UTF_8) , StandardCharsets.UTF_8);
			XtszDto xtszDto = xtszService.selectById("access_token");
			String accessToken ="";
			if (null!=xtszDto){
				accessToken  =  xtszDto.getSzz();//获取token 访问令牌
			}
			String content = "{" +
					"\"customerOrderNo\": \""+fzjcxxDto.getOrderno()+"\","+
					"\"taxNo\": \""+taxnum+"\"" +  //税号
					"}";
			String url = NUONUOEnum.SX_URL.getCode();  // SDK请求地址
			String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
			String orderInfo = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
			System.out.println(orderInfo);
			map.put("status","success");
			map.put("orderInfo",orderInfo);
		}else{
			map.put("status","fail");
			map.put("message","未获取到支付信息！");
		}
		return map;
	}

	/*新冠病毒核酸检测预约信息保存*/
	@RequestMapping(value="/detection/detectionAppointmentSave")
	@ResponseBody
	public Map<String, Object> detectionAppointmentSave(FzjcxxDto fzjcxxDto){
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())) {
			NNOpenSDK sdk = NNOpenSDK.getIntance();
			String taxnum = NUONUOEnum.SX_TAXNUM.getCode(); // 授权企业税号
			String appKey = NUONUOEnum.SX_APPKEY.getCode();
			String appSecret = NUONUOEnum.SX_APPSECRET.getCode();
			String method1 = NUONUOEnum.PAY_METHOD.getCode(); // API方法名
			String method;
			method = new String( method1.getBytes(StandardCharsets.UTF_8) , StandardCharsets.UTF_8);
			XtszDto xtszDto = xtszService.selectById("access_token");
			String accessToken ="";
			if (null!=xtszDto){
				accessToken  =  xtszDto.getSzz();//获取token 访问令牌
			}
			String val = StringUtil.generateUUID();
			String returnUrl = address+NUONUOEnum.RETURN_URL.getCode()+"?fzjcid="+fzjcxxDto.getFzjcid();
			String callbackUrl = address+NUONUOEnum.CALLBACK_URL.getCode()+"?fzjcid="+map.get("fzjcid");
			String content = "{" +
					"\"taxNo\": \""+taxnum+"\"," +  //税号
					"\"customerOrderNo\":  \""+val+"\"," + //订单号
					"\"amount\": \""+fzjcxxDto.getAmount()+"\"," +  //支付金额
					"\"subject\":\""+fzjcxxDto.getSubject()+"\"," +  //交易商品
					"\"returnUrl\": \""+returnUrl+"\"," + //支付成功后跳回地址
					"\"billingType\": \"0\"," + //支付即开票标志（0--仅支付 1支付+开票）
//                "\"sn\": \"1809CA884902\"," + //设备号
					"\"sellerNote\": \"杰毅生物\"," + //商户备注
//                "\"deptId\": \"8F0095616A17484DAD2C17925C04B78E\"," + //部门id
					"\"appKey\": \""+appKey+"\"," + //appKey
					"\"timeExpire\": \"15\"," + //订单有效期（单位为分）范围1-120 默认为120
					"\"invoiceExpire\": \"60\"," + //开票有效期（单位为天）范围 1-360，默认为60天
					"\"payee\": \"杰毅生物\"," + //收款人
					"\"autoType\": \"0\"," + //是否自动提交支付，0：否，1：是，为空则默认为“否”
               		 "\"callbackMethod\": \"2\"," + //1-按开放平台配置地址回调 2-按本接口传参地址回调，未传默认为1
               		 "\"callbackUrl\":  \""+callbackUrl+"\"," + //传参回调地址,但若回调方式配置为2，则该字段必传
					"\"goodsListItem\": [ " +
					"{\"amount\": \""+fzjcxxDto.getAmount()+"\"," + //含税单价，0.01-999999999.99
//                    "\"specification\": \"yll03-1391\"," +  ////规格型号，如只
					"\"goodsName\": \""+fzjcxxDto.getSubject()+"\"," + //商品名称
					"\"goodsNum\": \"1\" " + //商品数量，范围1-99999999
					"}]" +
					"}";
			String url = NUONUOEnum.SX_URL.getCode();  // SDK请求地址
			String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
			String payUrl = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
			System.out.println(payUrl);
			fzjcxxDto.setOrderno(val);
			fzjcxxService.update(fzjcxxDto);
			map.put("status","success");
			map.put("payUrl",payUrl);
		}else{
			map.put("status","fail");
			map.put("message","未获取到您的预约信息！");
		}
		return map;
	}
	/**
	 * 点击实验跳转页面
	 */
	@RequestMapping(value="/detection/detectionMod")
	public ModelAndView detectionMod(FzjcxxDto fzjcxxDto) {
		ModelAndView mav=new ModelAndView("detection/covid/covid_detection");
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.checkJcxm(fzjcxxDto);
        FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		fzjcxxDto_t.setSysj(sdf.format(date));
		fzjcxxDto_t.setIds(fzjcxxDto.getIds());
		fzjcxxDto_t.setJclx(fzjcxxDto.getJclx());
		mav.addObject("fzjcxxDto",fzjcxxDto_t);
		return mav;
	}

    /**
     * 点击检测跳转页面前校验所选数据检测项目是否相同
     */
    @RequestMapping(value="/detection/pagedataCheckJcxm")
    @ResponseBody
    public boolean checkJcxm(FzjcxxDto fzjcxxDto) {
        List<FzjcxxDto> jcxmlist=fzjcxxService.checkJcxm(fzjcxxDto);
		return jcxmlist == null || jcxmlist.size() <= 1;
    }

	/**
	 * 实验保存
	 */
	@RequestMapping("/detection/detectionSaveMod")
	@ResponseBody
	public Map<String, Object> detectionSaveMod(FzjcxxDto fzjcxxDto){
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setSyry(user.getYhid());
		if(fzjcxxDto.getSfsy()==null|| Objects.equals(fzjcxxDto.getSfsy(), "")){
			fzjcxxDto.setSfsy("0");
			fzjcxxDto.setSysj(null);
			fzjcxxDto.setSyry(null);
		}
		List<FzjcxxDto> dtoList = fzjcxxService.getDtoList(fzjcxxDto);
		boolean rjflag=false;
		boolean wjflag=false;
		for(FzjcxxDto dto:dtoList){
			if("R".equals(dto.getJcdxcsdm())){
				rjflag=true;
			}else if("W".equals(dto.getJcdxcsdm())){
				wjflag=true;
			}
		}
		if(rjflag&&wjflag){
			map.put("status", "fail");
			map.put("message","所选的检测对象类型必须为同一个！");
		}else{
			boolean isSuccess = fzjcxxService.updateSyzt(fzjcxxDto);
			if(rjflag){
				amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_MOD.getCode() + JSONObject.toJSONString(fzjcxxDto));
			}
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

		/**
	 * 新冠报告确认按钮调用确认页面
	 */
	@RequestMapping("/detection/msgconfirmCovid")
	public ModelAndView msgconfirmCovid(FzjcxxDto fzjcxxDto){
		ModelAndView mav = new ModelAndView("detection/covid/covid_view");
		List<FzjcxxDto> fzjcxxDtos = fzjcxxService.viewCovidDetails(fzjcxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(fzjcxxDto.getFzjcid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID_WORD.getCode());
		List<FjcfbDto> wordList = fzjcxxService.getReport(fjcfbDto);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_COVID.getCode());
		List<FjcfbDto> pdfList = fzjcxxService.getReport(fjcfbDto);
		FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
		List<FzjcxxDto> historyList =new ArrayList<>();
		if("R".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","R");
			fzjcxxDto_t.setJclx(fzjcxxDto.getJclx());
			historyList = fzjcxxService.getHistoryList(fzjcxxDto_t);
		}else if("W".equals(fzjcxxDto_t.getJcdxcsdm())){
			mav.addObject("jcdxcsdm","W");
		}
		String sj = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getSj())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				sj = dbEncrypt.dCode(fzjcxxDto_t.getSj());
			} catch (Exception e) {
				log.error("手机号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setSj(sj);
		String zjh = "";
		if (StringUtil.isNotBlank(fzjcxxDto_t.getZjh())) {
			try {
				DBEncrypt dbEncrypt = new DBEncrypt();
				zjh = dbEncrypt.dCode(fzjcxxDto_t.getZjh());
			} catch (Exception e) {
				log.error("证件号解密失败！" + e);
			}
		}
		fzjcxxDto_t.setZjh(zjh);
		mav.addObject("jclx", fzjcxxDto.getJclx());
		mav.addObject("wordList", wordList);
		mav.addObject("fzjcxxDto",fzjcxxDto_t);
		mav.addObject("pdfList",pdfList);
		mav.addObject("fzjcxxDtos",fzjcxxDtos);
		mav.addObject("historyList",historyList);
		mav.addObject("action","/detection/detection/msgconfirmSaveCovid");
		return mav;
	}
	
	/**
	 * 新冠报告确认功能
	 */
    @RequestMapping(value="/detection/msgconfirmSaveCovid")
    @ResponseBody
    public Map<String, Object> msgconfirmSaveCovid(FzjcxxDto fzjcxxDto) {
		User user = getLoginInfo();
		fzjcxxDto.setQrry(user.getYhid());
    	Map<String, Object> map = new HashMap<>();
		fzjcxxDto.setQrsj(   LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   );
//		boolean isSuccess = fzjcxxService.UpdateSfqrById(fzjcxxDto);
		FzjcxxDto dto = fzjcxxService.getDto(fzjcxxDto);
		boolean isSuccess = fzjcxxService.UpdateSfqrByYbbh(fzjcxxDto);
		if("R".equals(dto.getJcdxcsdm())){
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.FZXX_HQR.getCode() + JSONObject.toJSONString(fzjcxxDto));
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
    	return map;
    }

	/**
	 * 患者列表
	 */
	@RequestMapping(value="/detection/pageListPatients")
	public ModelAndView pageListPatients() {
		return new ModelAndView("detection/covid/patient_list");
	}

	/**
	 * 条件查找患者列表信息
	 */
	@RequestMapping("/detection/pageGetListPatients")
	@ResponseBody
	public Map<String, Object> viewPatientList(HzxxDto hzxxDto){
		if(StringUtil.isNotBlank(hzxxDto.getZjh())){
			try {
				DBEncrypt dbEncrypt=new DBEncrypt();
				hzxxDto.setZjh(dbEncrypt.eCode(hzxxDto.getZjh()));
			} catch (Exception e) {
				log.error("证件号解密失败！"+ e);
			}
		}
		if(StringUtil.isNotBlank(hzxxDto.getSj())){
			try {
				DBEncrypt dbEncrypt=new DBEncrypt();
				hzxxDto.setSj(dbEncrypt.eCode(hzxxDto.getSj()));
			} catch (Exception e) {
				log.error("证件号解密失败！"+ e);
			}
		}
		List<HzxxDto> pagedDtoList = hzxxService.getPagedDtoList(hzxxDto);
		for(HzxxDto hzxxDto_t:pagedDtoList){
			String xjzd="";
			if(StringUtil.isNotBlank(hzxxDto_t.getSf())){
				xjzd=xjzd+hzxxDto_t.getSf();
			}
			if(StringUtil.isNotBlank(hzxxDto_t.getCs())){
				xjzd=xjzd+hzxxDto_t.getCs();
			}
			if(StringUtil.isNotBlank(hzxxDto_t.getXxdz())){
				xjzd=xjzd+hzxxDto_t.getXxdz();
			}
			hzxxDto_t.setXjzd(xjzd);
			String zjh = "";
			if(StringUtil.isNotBlank(hzxxDto_t.getZjh())){
				try {
					DBEncrypt dbEncrypt=new DBEncrypt();
					zjh = dbEncrypt.dCode(hzxxDto_t.getZjh());
				} catch (Exception e) {
					log.error("证件号解密失败！"+ e);
				}
			}
			hzxxDto_t.setZjh(zjh);
			String sj = "";
			if(StringUtil.isNotBlank(hzxxDto_t.getSj())){
				try {
					DBEncrypt dbEncrypt=new DBEncrypt();
					sj = dbEncrypt.dCode(hzxxDto_t.getSj());
				} catch (Exception e) {
					log.error("手机号解密失败！"+ e);
				}
			}
			hzxxDto_t.setSj(sj);
			if(StringUtil.isNotBlank(hzxxDto_t.getZjlx())){
				List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
				for(JcsjDto jcsjDto:jcsjDtos){
					if(jcsjDto.getCsid().equals(hzxxDto_t.getZjlx())){
						hzxxDto_t.setZjlxmc(jcsjDto.getCsmc());
					}
				}
			}
		}
		Map<String, Object> map= new HashMap<>();
		map.put("rows", pagedDtoList);
		map.put("total", hzxxDto.getTotalNumber());
		return map;
	}

	/**
	 * 新冠患者列表查看详情
	 */
	@RequestMapping("/detection/viewPatientDetails")
	public ModelAndView viewPatientDetails(HzxxDto hzxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/patient_view");
		List<HzxxDto> hzxxDtos = hzxxService.viewPatientDetails(hzxxDto);
		for(HzxxDto hzxxDto_t:hzxxDtos){
			hzxxDto_t.setXjzd(hzxxDto_t.getSf()+hzxxDto_t.getCs()+hzxxDto_t.getXxdz());
			String zjh = "";
			if(StringUtil.isNotBlank(hzxxDto_t.getZjh())){
				try {
					DBEncrypt dbEncrypt=new DBEncrypt();
					zjh = dbEncrypt.dCode(hzxxDto_t.getZjh());
				} catch (Exception e) {
					log.error("证件号解密失败！"+ e);
				}
			}
			hzxxDto_t.setZjh(zjh);

			String sj = "";
			if(StringUtil.isNotBlank(hzxxDto_t.getSj())){
				try {
					DBEncrypt dbEncrypt=new DBEncrypt();
					sj = dbEncrypt.dCode(hzxxDto_t.getSj());
				} catch (Exception e) {
					log.error("证件号解密失败！"+ e);
				}
			}
			hzxxDto_t.setSj(sj);
			if(StringUtil.isNotBlank(hzxxDto_t.getZjlx())){
				List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
				for(JcsjDto jcsjDto:jcsjDtos){
					if(jcsjDto.getCsid().equals(hzxxDto_t.getZjlx())){
						hzxxDto_t.setZjlxmc(jcsjDto.getCsmc());
					}
				}
			}
		}
		HzxxDto hzxxDto_t = hzxxDtos.get(0);
		mav.addObject("hzxxDto",hzxxDto_t);
		mav.addObject("hzxxDtos",hzxxDtos);
		return mav;
	}

	/**
	 * 新冠患者列表修改页面
	 */
	@RequestMapping("/detection/modPatientDetails")
	public ModelAndView modPatientDetails(HzxxDto hzxxDto) {
		ModelAndView mav = new ModelAndView("detection/covid/patient_edit");
		List<HzxxDto> hzxxDtos = hzxxService.viewPatientDetails(hzxxDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PROVINCE,BasicDataTypeEnum.CITY});
		HzxxDto hzxxDto_t = hzxxDtos.get(0);
		String zjh = "";
		if(StringUtil.isNotBlank(hzxxDto_t.getZjh())){
			try {
				DBEncrypt dbEncrypt=new DBEncrypt();
				zjh = dbEncrypt.dCode(hzxxDto_t.getZjh());
			} catch (Exception e) {
				log.error("证件号解密失败！"+ e);
			}
		}
		hzxxDto_t.setZjh(zjh);
		String sj = "";
		if(StringUtil.isNotBlank(hzxxDto_t.getSj())){
			try {
				DBEncrypt dbEncrypt=new DBEncrypt();
				sj = dbEncrypt.dCode(hzxxDto_t.getSj());
			} catch (Exception e) {
				log.error("证件号解密失败！"+ e);
			}
		}
		hzxxDto_t.setSj(sj);
		if(StringUtil.isNotBlank(hzxxDto_t.getZjlx())){
			List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
			for(JcsjDto jcsjDto:jcsjDtos){
				if(jcsjDto.getCsid().equals(hzxxDto_t.getZjlx())){
					hzxxDto_t.setZjlxmc(jcsjDto.getCsmc());
				}
			}
		}
		mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.CITY.getCode());
		jcsjDto.setFcsid(hzxxDto_t.getSfid());
		List<JcsjDto> citylist=jcsjService.getJcsjDtoList(jcsjDto);
		mav.addObject("cslist", citylist);
		List<HzxxDto> hzxxDtoList = hzxxService.getZjlx();
		mav.addObject("hzxxDtoList",hzxxDtoList);
		mav.addObject("hzxxDto",hzxxDto_t);
		return mav;
	}

	/**
	 * 修改保存功能
	 */
	@RequestMapping(value="/detection/modSavePatientDetails")
	@ResponseBody
	public Map<String, Object> modSavePatient(HzxxDto hzxxDto) {
		Map<String, Object> map = new HashMap<>();
		if(hzxxDto.getXb()!=null){
			if(hzxxDto.getXb().equals("男")){
				hzxxDto.setXb("1");
			}else{
				hzxxDto.setXb("2");
			}
		}
		if(StringUtil.isNotBlank(hzxxDto.getZjh())){
			try {
				DBEncrypt dbEncrypt=new DBEncrypt();
				hzxxDto.setZjh(dbEncrypt.eCode(hzxxDto.getZjh()));
			} catch (Exception e) {
				log.error("证件号加密失败！"+ e);
			}
		}
		if(StringUtil.isNotBlank(hzxxDto.getSj())){
			try {
				DBEncrypt dbEncrypt=new DBEncrypt();
				hzxxDto.setSj(dbEncrypt.eCode(hzxxDto.getSj()));
			} catch (Exception e) {
				log.error("证件号加密失败！"+ e);
			}
		}
		boolean isSuccess = hzxxService.updateCovidPatient(hzxxDto);
		if(isSuccess)
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.HZXX_UPD.getCode() + JSONObject.toJSONString(hzxxDto));
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除功能
	 */
	@RequestMapping(value="/detection/deletePatientDetails")
	@ResponseBody
	public Map<String, Object> deletePatientDetails(HzxxDto hzxxDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		hzxxDto.setScry(user.getYhid());
		boolean isSuccess = hzxxService.deleteCovidPatient(hzxxDto);
		if(isSuccess)
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECT.getCode(), RabbitEnum.HZXX_DEL.getCode() + JSONObject.toJSONString(hzxxDto));
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 跳转检测结果修改界面
	 */
	@RequestMapping("/detection/resultmodDetectionResult")
	public ModelAndView resultmodDetectionResult(FzjcxxDto fzjcxxDto){
		ModelAndView mav = new ModelAndView("detection/covid/covid_resultMod");
		fzjcxxDto.setAuditType(AuditTypeEnum.AUDIT_COVID.getCode());
		/*
		  姚嘉伟 modify 2021/12/15
		  检测结果修改方式修改，更改为根据ybbhs获取相同标本编号的标本信息进行批量更新
		 */
		StringBuilder fzjcids= new StringBuilder();
		List<FzjcxxDto> list=fzjcxxService.getFzjcxxByybbhs(fzjcxxDto);
		if(list!=null && list.size()>0){
			fzjcxxDto.setJcdxcsdm(list.get(0).getJcdxcsdm());
			for (FzjcxxDto dto : list) {
				if (StatusEnum.CHECK_NO.getCode().equals(dto.getZt()) ||
						StatusEnum.CHECK_UNPASS.getCode().equals(dto.getZt())) {//只修改未提交和审核不通过的
					fzjcids.append(",").append(dto.getFzjcid());
				}
			}
		}
		if(fzjcids.length() > 1)
			fzjcids = new StringBuilder(fzjcids.substring(1));
		fzjcxxDto.setFzjcids(fzjcids.toString());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT});
		mav.addObject("jcjglist", jclist.get(BasicDataTypeEnum.MOLECULAR_DETECTION_RESULT.getCode()));
		mav.addObject("fzjcxxDto",fzjcxxDto);
		return mav;
	}

	/**
	 * 修改检测结果
	 */
	@RequestMapping("/detection/resultmodSaveDetectionResult")
	@ResponseBody
	public Map<String,Object> resultmodSaveDetectionResult(FzjcxxDto fzjcxxDto){
		Map<String,Object> map=new HashMap<>();
		List<FzjcxxDto> dtoList = fzjcxxService.getDtoList(fzjcxxDto);
		boolean rjflag=false;
		boolean wjflag=false;
		for(FzjcxxDto dto:dtoList){
			if("R".equals(dto.getJcdxcsdm())){
				rjflag=true;
			}else if("W".equals(dto.getJcdxcsdm())){
				wjflag=true;
			}
		}
		if(rjflag&&wjflag) {
			map.put("status", "fail");
			map.put("message", "所选的检测对象类型必须为同一个！");
		}else{
			boolean isSuccess=fzjcxxService.modSaveDetectionResult(fzjcxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 根据省份去查城市
	 */
	@RequestMapping(value="/pagedataJscjcity")
	@ResponseBody
	public List<JcsjDto> jcsjcity(JcsjDto jcsjDto){
		jcsjDto.setJclb(BasicDataTypeEnum.CITY.getCode());
		return jcsjService.getJcsjDtoList(jcsjDto);
	}

	/**
	 * 报告上传
	 */
	@RequestMapping("/detection/reportUpload")
	public ModelAndView unloadReport(FzjcxxDto fzjcxxDto){
		ModelAndView mav = new ModelAndView("detection/covid/covid_upload");
		mav.addObject("fzjcxxDto",fzjcxxDto);
		return mav;
	}

	/**
	 *批量上传新冠核酸检测数据至卫建委
	 */
	@RequestMapping(value="/detection/reportSaveUpload")
	@ResponseBody
	public Map<String,Object> dataReportToCity(FzjcxxDto fzjcxxDto){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		if("wjw".equals(fzjcxxDto.getScpt())){
			boolean isSuccess=fzjcxxService.dataReportToCity(fzjcxxDto,user);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		}else if("ali".equals(fzjcxxDto.getScpt())){
			boolean isSuccess=fzjcxxService.dataReportToAli(fzjcxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 重新生成新冠检测报告
	 */
	@RequestMapping("/detection/reportgenerate")
	@ResponseBody
	public Map<String,Object> reportgenerate(FzjcxxDto fzjcxxDto){
		User user = getLoginInfo();
		log.error("分子检测列表报告生成按钮触发者：用户ID:"+user.getYhid()+"  真实姓名："+user.getZsxm()+"  所选择的Fzjcids为："+ fzjcxxDto.getIds());
		Map<String,Object> map=new HashMap<>();
		List<FzjcxxDto> dtoList = fzjcxxService.getDtoList(fzjcxxDto);
		boolean rjflag=false;
		boolean wjflag=false;
		for(FzjcxxDto dto:dtoList){
			if("R".equals(dto.getJcdxcsdm())){
				rjflag=true;
			}else if("W".equals(dto.getJcdxcsdm())){
				wjflag=true;
			}
		}
		if(rjflag&&wjflag) {
			map.put("status", "fail");
			map.put("message", "所选的检测对象类型必须为同一个！");
		}else{
			boolean isSuccess=fzjcxxService.reportgenerate(fzjcxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? "重新生成新冠报告成功！" : "重新生成新冠报告失败！");
		}
		return map;
	}

	/**
	 * 分子检测导入页面
	 */
	@RequestMapping(value ="/detection/pageImportDetection")
	public ModelAndView importDetection(){
		ModelAndView mav = new ModelAndView("detection/detectLab/detection_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_MOLECULAR_DETECTION.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("jcdxList", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_OBJECT.getCode())  );
		return mav;
	}

	/**
	 * 分子物检导入页面
	 */
	@RequestMapping(value ="/detection/pageImportDetectionGoods")
	public ModelAndView importGoodsDetection(){
		ModelAndView mav = new ModelAndView("detection/detectLab/detectionGoods_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_MOLECULAR_GOODSDETECTION.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("jcdxList", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_OBJECT.getCode())  );
		return mav;
	}

	/**
	 * 选择下载报告压缩包条件
	 */
	@RequestMapping(value = "/detection/reportdownloadZip")
	public ModelAndView reportdownloadZip(FzjcxxDto fzjcxxDto){
		ModelAndView mav=new ModelAndView("detection/covid/covid_reportDownload");
		mav.addObject("fzjcxxDto",fzjcxxDto);
		return mav;
	}
	

	/**
	 * 根据条件下载报告压缩包
	 */
	@RequestMapping(value = "/detection/reportdownloadSaveZip")
	@ResponseBody
	public Map<String,Object> reportZipDownloadOut(FzjcxxDto fzjcxxDto, HttpServletRequest request){
		return fzjcxxService.reportZipDownload(fzjcxxDto, request);
	}

	/**
	 * 不可预约日期管理页面
	 */
	@RequestMapping(value ="/detection/pageListUnAppDate")
	public ModelAndView pageListUnAppDate(){
		return new ModelAndView("detection/dateManage/unAppDate_list");
	}

	/**
	 * 获取不可预约日期数据
	 */
	@RequestMapping("/detection/pageGetListUnAppDate")
	@ResponseBody
	public Map<String,Object> getUnAppDateList(BkyyrqDto bkyyrqDto){
		Map<String,Object> map=new HashMap<>();
		List<BkyyrqDto> bkyyrqDtoList = bkyyrqService.getPagedDtoList(bkyyrqDto);
		map.put("total",bkyyrqDto.getTotalNumber());
		map.put("rows",bkyyrqDtoList);
		return map;
	}

	/**
	 * 查看不可预约日期信息
	 */
	@RequestMapping("/detection/viewUnAppDateDetails")
	public ModelAndView viewUnAppDateDetails(BkyyrqDto bkyyrqDto){
		ModelAndView mav = new ModelAndView("detection/dateManage/unAppDate_view");
		//查询不可预约信息
		BkyyrqDto bkyyrqDto_t = bkyyrqService.getBkyyrqDto(bkyyrqDto);
		if (StringUtil.isNotBlank(bkyyrqDto_t.getBkyysjd())){
			String[] sjds = bkyyrqDto_t.getBkyysjd().split("&");
			ArrayList<BkyyrqDto> bkyysjds = new ArrayList<>();
			for (String sjd : sjds) {
				BkyyrqDto dto = new BkyyrqDto();
				String[] split = sjd.split("~");
				if (split.length>1){
					dto.setBkyysjdstart(split[0]);
					dto.setBkyysjdend(split[1]);
				}else {
					dto.setBkyysjdstart(split[0]);
					dto.setBkyysjdend(split[0]);
				}
				bkyysjds.add(dto);
			}
			mav.addObject("bkyysjds",bkyysjds);
		}
		mav.addObject("bkyyrqDto",bkyyrqDto_t);
		return mav;
	}

	/**
	 * 新增不可预约日期
	 */
	@RequestMapping("/detection/addUnAppDateDetails")
	public ModelAndView addUnAppDateDetails(BkyyrqDto bkyyrqDto){
		ModelAndView mav = new ModelAndView("detection/dateManage/unAppDate_edit");
		//查询系统设置中预约时间段
		bkyyrqDto.setFlag("add");
		mav.addObject("bkyyrqDto",bkyyrqDto);
		XtszDto appoinmentTimeStart = xtszService.selectById("appoinmentTimeStart");
		XtszDto appoinmentTimeEnd = xtszService.selectById("appoinmentTimeEnd");
		mav.addObject("appoinmentTimeStart",appoinmentTimeStart.getSzz());
		mav.addObject("appoinmentTimeEnd",appoinmentTimeEnd.getSzz());
		return mav;
	}

	/**
	 * 新增保存不可预约日期
	 */
	@RequestMapping("/detection/addSaveUnAppDateDetails")
	@ResponseBody
	public Map<String,Object> addSaveUnAppDateDetails(BkyyrqDto bkyyrqDto){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lrsj = dateformat.format(new Date());
		//不可预约时间段
		String bkyysjd = "";
		if (StringUtil.isNotBlank(bkyyrqDto.getBkyysjdstart()) && StringUtil.isNotBlank(bkyyrqDto.getBkyysjdend())){
			bkyysjd = bkyyrqDto.getBkyysjdstart()+"~"+bkyyrqDto.getBkyysjdend();
			bkyyrqDto.setBkyysjd(bkyysjd);
		}

		if ("add".equals(bkyyrqDto.getFlag())){
			bkyyrqDto.setLrry(user.getYhid());
			bkyyrqDto.setLrsj(lrsj);
			map = bkyyrqService.insertBkyyrqDtoList(bkyyrqDto);
		} else if ("mod".equals(bkyyrqDto.getFlag())) {
			bkyyrqDto.setXgsj(lrsj);
			bkyyrqDto.setXgry(user.getYhid());
			bkyyrqDto.setBkyysjd(bkyysjd);
			map = bkyyrqService.updateBkyyrqDto(bkyyrqDto);
		}
		return map;
	}

	/**
	 * 修改不可预约日期页面
	 */
	@RequestMapping("/detection/modUnAppDateDetails")
	public ModelAndView modUnAppDateDetails(BkyyrqDto bkyyrqDto){
		ModelAndView mav = new ModelAndView("detection/dateManage/unAppDate_edit");
		//查询不可预约信息
		BkyyrqDto bkyyrqDto_t = bkyyrqService.getBkyyrqDto(bkyyrqDto);
		if (StringUtil.isNotBlank(bkyyrqDto_t.getBkyysjd())){
			//如果有多个时间段
			String[] sjds = bkyyrqDto_t.getBkyysjd().split("&");
			ArrayList<BkyyrqDto> bkyysjds = new ArrayList<>();
			for (String sjd : sjds) {
				BkyyrqDto dto = new BkyyrqDto();
				String[] split = sjd.split("~");
				if (split.length>1){
					dto.setBkyysjdstart(split[0]);
					dto.setBkyysjdend(split[1]);
				}else {
					dto.setBkyysjdstart(split[0]);
					dto.setBkyysjdend(split[0]);
				}
				bkyysjds.add(dto);
			}
			mav.addObject("bkyysjds",bkyysjds);
		}
		bkyyrqDto_t.setFlag("mod");
		mav.addObject("bkyyrqDto",bkyyrqDto_t);
		//查询系统设置中预约时间段
		XtszDto appoinmentTimeStart = xtszService.selectById("appoinmentTimeStart");
		XtszDto appoinmentTimeEnd = xtszService.selectById("appoinmentTimeEnd");
		mav.addObject("appoinmentTimeStart",appoinmentTimeStart.getSzz());
		mav.addObject("appoinmentTimeEnd",appoinmentTimeEnd.getSzz());
		return mav;
	}

	/**
	 * 删除不可预约日期信息
	 */
	@RequestMapping("/detection/delUnAppDateDetails")
	@ResponseBody
	public Map<String,Object> delUnAppDateDetails(BkyyrqDto bkyyrqDto) {
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo();
		bkyyrqDto.setScry(user.getYhid());
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String scsj = dateformat.format(new Date());
		bkyyrqDto.setScsj(scsj);
		boolean isSuccess = bkyyrqService.delUnAppDateDetails(bkyyrqDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
				: xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 查询已被禁用的日期
	 */
	@RequestMapping("/detection/pagedataUnAppDate")
	@ResponseBody
	public Map<String,Object> pagedataUnAppDate(BkyyrqDto bkyyrqDto) {
		Map<String, Object> map=new HashMap<>();
		List<BkyyrqDto> bkyyrqDtos = bkyyrqService.getUnAppDate(bkyyrqDto);
		map.put("bkyyrqList",bkyyrqDtos);
		return map;
	}

	/**
	 * 分子检测-物检新增
	 */
	@RequestMapping(value ="/detection/addObjDetection")
	public ModelAndView addObjDetection(FzjcxxDto fzjcxxDto){
		ModelAndView mav = new ModelAndView("detection/covid/covid_list_edit");
		//检测单位默认，因为标本编号生产规则依靠检测项目+检测单位+流水号
		//设置检测单位为默认的检测单位（杭州实验室）
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for (JcsjDto jcdw : jcdwList) {
			if ("1".equals(jcdw.getSfmr())){
				fzjcxxDto.setJcdw(jcdw.getCsid());
			}
		}
		mav.addObject("jcdwxxList", jcdwList);//检测单位
		//物检的检测项目默认为新冠
		List<JcsjDto> jcxmList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		List<JcsjDto> fzlist =new ArrayList<>();
		for (JcsjDto jcxm : jcxmList) {
			if ("JX".equals(jcxm.getCsdm())){
				fzjcxxDto.setJcxmid(jcxm.getCsid());
			}
		}
		for (JcsjDto jcsjDto : jcxmList) {
			if(jcsjDto.getFcsid().equals(fzjcxxDto.getJclx()))	{
				fzlist.add(jcsjDto);
			}
		}
		mav.addObject("fzjcxmlist", fzlist);//检测项目
		//物检的检测对象默认为物品
		JcsjDto JcsjDto_jcdxlx = new JcsjDto();
		List<JcsjDto> jcdxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
		for (JcsjDto jcdx : jcdxList) {
			if ("W".equals(jcdx.getCsdm())){
				fzjcxxDto.setJcdxlx(jcdx.getCsid());
				JcsjDto_jcdxlx = jcdx;
			}
		}
		mav.addObject("JcsjDto_jcdxlx",JcsjDto_jcdxlx);
		mav.addObject("jcdxlxlist", jcdxList);
		//采样点，设置默认采样点为选中
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for (JcsjDto cyd : cydList) {
			if ("1".equals(cyd.getSfmr())){
				fzjcxxDto.setCyd(cyd.getCsid());
			}
		}
		mav.addObject("cydList", cydList);
		//新冠标本状态
		List<JcsjDto> bbztlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XG_SAMPLESTATE.getCode());
		for (JcsjDto bbzt : bbztlist) {
			if ("1".equals(bbzt.getSfmr())){
				fzjcxxDto.setBbzt(bbzt.getCsid());
			}
		}
		mav.addObject("bbztlist", bbztlist);
		//微信标本类型,默认设置为其他
		List<JcsjDto> wxbblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		for (JcsjDto wxbblx : wxbblxList) {
			if ("1".equals(wxbblx.getCskz1())){
				fzjcxxDto.setYblx(wxbblx.getCsid());
			}
		}
		mav.addObject("wxbblxList", wxbblxList);

		//物检送检单位默认为其他
		YyxxDto yyxxDto = new YyxxDto();
		yyxxDto.setSearchParam("其他");
		List<YyxxDto> yyxxlist = yyxxService.selectHospitalName(yyxxDto);
		if (yyxxlist!=null && yyxxlist.size()>0){
			fzjcxxDto.setSjdw(yyxxlist.get(0).getDwid());
			fzjcxxDto.setYyxxcskz1("1");
			fzjcxxDto.setDwmc(yyxxlist.get(0).getDwmc());
		}
		mav.addObject("fzjcxxDto",fzjcxxDto);
		mav.addObject("formAction","addSaveObjDetection");
		mav.addObject("zffslist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PAYMENT_TYPE.getCode()));//支付方式
		mav.addObject("wxbblxList", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//微信标本类型
		List<Map<String,String>> sjdwxxList= fzjcxxService.getSjdw();
		mav.addObject("sjdwxxList",sjdwxxList);
		mav.addObject("fzjczxmlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode()));

		User user = getLoginInfo();
		GrszDto grszDto_t=new GrszDto();
		grszDto_t.setYhid(user.getYhid());
		grszDto_t.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
		grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto_t);
		mav.addObject("grszDto", Objects.requireNonNullElseGet(grszDto_t, GrszDto::new));

		return mav;
	}

	/**
	 * 	物检 新增保存
	 */
	@RequestMapping("/detection/addSaveObjDetection")
	@ResponseBody
	public Map<String,Object> addSaveObjDetection(FzjcxxDto fzjcxxDto,GrszDto grszDto){
		Map<String,Object> map=new HashMap<>();
		String bbzbh=fzjcxxService.insertObjDetection(fzjcxxDto);
		User user = getLoginInfo();
		if ("1".equals(grszDto.getGrsz_flg())) {
			// 添加个人设置
			grszDto.setYhid(user.getYhid());
			grszDto.setSzlb(PersonalSettingEnum.PRINT_ADDRESS.getCode());
			grszService.delete(grszDto);
			grszDto.setSzid(StringUtil.generateUUID());
			grszService.insert(grszDto);
		}
		map.put("sz_flg", grszDto.getSzz());
		String otherUrl = address+"/ws/getInfoByNbbhOrYbbh";
		String openUrl = otherUrl+"?nbbm=" + bbzbh;
		String sign = commonService.getSign(bbzbh);
		sign = URLEncoder.encode(sign, StandardCharsets.UTF_8);
		map.put("ResponseSign", sign);
		map.put("RequestLocalCode", URLEncoder.encode("新冠录入打印",StandardCharsets.UTF_8));
		map.put("callBackUrl", openUrl);
		map.put("status", StringUtil.isNotBlank(bbzbh) ? "success" : "fail");
		map.put("message", StringUtil.isNotBlank(bbzbh) ? "新增分子物检成功！" : "新增分子物检失败！");
		return map;
	}

	/**
	 * 小程序 人检 预约确认选择页面初始化（扫场场所码）
	 */
	@RequestMapping("/minidataSampleAddrChoose")
	@ResponseBody
	public Map<String,Object> minidataSampleAddrChoose(){
		Map<String,Object> map=new HashMap<>();
		List<JcsjDto> cydList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		List<JcsjDto> jcdwList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		map.put("cydList",cydList);
		map.put("jcdwList",jcdwList);
		return map;
	}

	/**
	 * 小程序 人检 预约确认页面初始化（信息录入）
	 */
	@RequestMapping("/minidataApptConfirm")
	@ResponseBody
	public Map<String,Object> minidataApptConfirm(HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		String yhid = user.getYhid();
		List<JcsjDto> fzjczxmDtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode());
		List<JcsjDto> fzjcxmDtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		JcsjDto fzjcxmJcsjDto = new JcsjDto();
		for (JcsjDto fzjcxm : fzjcxmDtoList) {
			if ("JX".equals(fzjcxm.getCsdm())){
				fzjcxmJcsjDto = fzjcxm;
				break;
			}
		}
		List<JcsjDto> fzjczxmList = new ArrayList<>();
		for (JcsjDto fzjczxm : fzjczxmDtoList) {
			if (fzjcxmJcsjDto.getCsid().equals(fzjczxm.getFcsid())){
				fzjczxmList.add(fzjczxm);
			}
		}
		FzjcxxDto fzjcxxDto = new FzjcxxDto();
		fzjcxxDto.setCjry(yhid);
		fzjcxxDto.setCyd(request.getParameter("cyd"));
		fzjcxxDto.setJclx(request.getParameter("jclx"));
		List<JcsjDto> jclxs = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		if(jclxs.size()>0){
			for (JcsjDto jcsjDto:jclxs){
				if (fzjcxxDto.getJclx().equals(jcsjDto.getCsdm())){
					fzjcxxDto.setJclx(jcsjDto.getCsid());
					break;
				}
			}
		}
		Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto);
		map.put("gs",countData.get("gs"));
		map.put("rs",countData.get("rs"));
		List<JcsjDto> zjlxList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
		map.put("ywlx",BusTypeEnum.IMP_IDENTITY_CARD_IMG.getCode());
		map.put("fkywlx", BusinessTypeEnum.XG.getCode());
		XtszDto xtszDto = xtszService.selectById("xg_zfje");
		String fkje ="";
		if (null!=xtszDto){
			fkje  =  xtszDto.getSzz();//获取token 访问令牌
		}
		map.put("fkje", fkje);
		map.put("zjlxList",zjlxList);
		map.put("fzjczxmList",fzjczxmList);
		return map;
	}

	/**
	 * 小程序 人检 保存确认信息
	 */
	@RequestMapping("/minidataAddApptConfirm")
	@ResponseBody
	public Map<String,Object> minidataAddApptConfirm(FzjcxxDto fzjcxxDto) throws Exception {
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setCjry(user.getYhid());
		//更新分子检测信息和患者信息的确认标记
		boolean result=fzjcxxService.modSaveCovid(fzjcxxDto);
		FzjcxxDto fzjcxxDto_t = new FzjcxxDto();
		fzjcxxDto_t.setCjry(fzjcxxDto.getCjry());
		fzjcxxDto_t.setCyd(fzjcxxDto.getCyd());
		fzjcxxDto_t.setJclx(fzjcxxDto.getJclx());
		Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto_t);
		map.put("gs",countData.get("gs"));
		map.put("rs",countData.get("rs"));
		map.put("status", result ? "success" : "fail");
		map.put("fzjcxxDto",fzjcxxDto);
		return map;
	}

	/**
	 * 小程序 人检 获取患者信息
	 */
	@RequestMapping("/detection/minidataModPatientDetails")
	@ResponseBody
	public Map<String,Object> minidataModPatientDetails(HzxxDto hzxxDto){
		Map<String,Object> map = new HashMap<>();
		List<HzxxDto> hzxxDtos = hzxxService.viewPatientDetails(hzxxDto);
		List<JcsjDto> zjlxList = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
		HzxxDto hzxxDto_t = new HzxxDto();
		if (hzxxDtos!=null && hzxxDtos.size()>0){
			hzxxDto_t = hzxxDtos.get(0);
			String zjh = "";
			if(StringUtil.isNotBlank(hzxxDto_t.getZjh())){
				try {
					DBEncrypt dbEncrypt=new DBEncrypt();
					zjh = dbEncrypt.dCode(hzxxDto_t.getZjh());
				} catch (Exception e) {
					log.error("证件号解密失败！"+ e);
				}
				hzxxDto_t.setZjh(zjh);
			}
			String sj = "";
			if(StringUtil.isNotBlank(hzxxDto_t.getSj())){
				try {
					DBEncrypt dbEncrypt=new DBEncrypt();
					sj = dbEncrypt.dCode(hzxxDto_t.getSj());
				} catch (Exception e) {
					log.error("手机号解密失败！"+ e);
				}
				hzxxDto_t.setSj(sj);
			}
			if(StringUtil.isNotBlank(hzxxDto_t.getZjlx())){
				List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
				for(JcsjDto jcsjDto:jcsjDtos){
					if(jcsjDto.getCsid().equals(hzxxDto_t.getZjlx())){
						hzxxDto_t.setZjlxmc(jcsjDto.getCsmc());
					}
				}
			}
		}
		map.put("hzxxDto",hzxxDto_t);
		map.put("zjlxList",zjlxList);
		return map;
	}

	/**
	 * 小程序 人检 删除操作（回滚确认操作）
	 */
	@RequestMapping("/detection/callbackCovid")
	@ResponseBody
	public Map<String,Object> callbackCovid(FzjcxxDto fzjcxxDto){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=fzjcxxService.callbackCovid(fzjcxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		return map;
	}

	/**
	 * 锁定高风险标本
	 */
	@RequestMapping("/detection/lockPositive")
	@ResponseBody
	public Map<String,Object> lockPositive(FzjcxxDto fzjcxxDto){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=fzjcxxService.positiveLock(fzjcxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? "锁定成功！" : "锁定失败！");
		return map;
	}

	/**
	 * 小程序 人检 新增预约
	 */
	@RequestMapping(value="/minidataDetectionAppointment")
	@ResponseBody
	public Map<String, Object> minidataDetectionAppointment(){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		map.put("lrry",user.getYhid());
		JcsjDto jclx = new JcsjDto();
		List<JcsjDto> jclxs = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		for (JcsjDto lsjclx : jclxs) {
			if ("TYPE_COVID".equals(lsjclx.getCsdm())){
				jclx = lsjclx;
				break;
			}
		}
		List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		for (JcsjDto jcxm : jcxmList) {
			if (StringUtil.isNotBlank(jcxm.getFcsid())) {
				if (jcxm.getFcsid().equals(jclx.getCsid())) {
					if ("1".equals(jcxm.getSfmr())) {
						jcxm.setChecked("1");
					}
				}
			}
		}
		List<JcsjDto> zjlxList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
		map.put("jcxmList",jcxmList);
		map.put("zjlxList",zjlxList);
		map.put("ywlx", BusinessTypeEnum.XG.getCode());
		XtszDto xtszDto = xtszService.selectById("xg_zfje");
		String fkje ="";
		if (null!=xtszDto){
			fkje  =  xtszDto.getSzz();//获取token 访问令牌
		}
		map.put("fkje", fkje);
		return map;
	}

	/**
	 * 小程序 物检 预约确认页面初始化（信息录入）
	 */
	@RequestMapping("/minidataMaterialConfirmInfo")
	@ResponseBody
	public Map<String,Object> minidataMaterialConfirmInfo(HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo();
		String yhid = user.getYhid();
		//处理分子检测项目、子项目
		List<JcsjDto> fzjczxmDtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_SUBITEM.getCode());
		List<JcsjDto> fzjcxmDtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
		JcsjDto fzjcxmJcsjDto = new JcsjDto();
		for (JcsjDto fzjcxm : fzjcxmDtoList) {
			if ("JX".equals(fzjcxm.getCsdm())){
				fzjcxmJcsjDto = fzjcxm;
				break;
			}
		}
		List<JcsjDto> fzjczxmList = new ArrayList<>();
		for (JcsjDto fzjczxm : fzjczxmDtoList) {
			if (fzjcxmJcsjDto.getCsid().equals(fzjczxm.getFcsid())){
				fzjczxmList.add(fzjczxm);
			}
		}
		FzjcxxDto fzjcxxDto = new FzjcxxDto();
		//设置采集人员
		fzjcxxDto.setCjry(yhid);
		//设置采样点
		fzjcxxDto.setCyd(request.getParameter("cyd"));
		//设置统计数据
		//设置检测对象类型（物）
		if (StringUtil.isBlank(fzjcxxDto.getJcdxlx())){
			List<JcsjDto> jcdxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
			for (JcsjDto jcdx : jcdxList) {
				if ("W".equals(jcdx.getCsdm())){
					fzjcxxDto.setJcdxlx(jcdx.getCsid());
					break;
				}
			}
		}
		Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto);
		map.put("gs",countData.get("gs"));
		//分子检测子项目
		map.put("fzjczxmList",fzjczxmList);
		return map;
	}

	/**
	 * 小程序 获取物标信息
	 */
	@RequestMapping("/minidataGetMaterialInfo")
	@ResponseBody
	public Map<String,Object> minidataGetMaterialInfo(FzjcxxDto fzjcxxDto){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setCjry(user.getYhid());
		FzjcxxDto fzjcxxDto_t = fzjcxxService.getFzjcxxDto(fzjcxxDto);
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(FzjcxxDto.class, "fzjcid","ypmc","yblxmc","sccj","scd","scpc","scrq","cjsj");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		FzjcxxDto fzjcxxDto_c = new FzjcxxDto();
		fzjcxxDto_t.setCjry(fzjcxxDto.getCjry());
		fzjcxxDto_t.setCyd(fzjcxxDto.getCyd());
		fzjcxxDto_t.setJcdxlx(fzjcxxDto.getJcdxlx());
		//设置检测对象类型（物）
		if (StringUtil.isBlank(fzjcxxDto_t.getJcdxlx())){
			List<JcsjDto> jcdxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
			for (JcsjDto jcdx : jcdxList) {
				if ("W".equals(jcdx.getCsdm())){
					fzjcxxDto_t.setJcdxlx(jcdx.getCsid());
					break;
				}
			}
		}
		Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto_c);
		map.put("gs",countData.get("gs"));
		map.put("fzjcxxDto",JSONObject.toJSONString(fzjcxxDto_t,listFilters));
		return map;
	}

	/**
	 * 小程序 物检确认
	 */
	@RequestMapping("/minidataMaterialConfirm")
	@ResponseBody
	public Map<String,Object> minidataMaterialConfirm(FzjcxxDto fzjcxxDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setCjry(user.getYhid());
		//更新分子检测信息和患者信息的确认标记
		FzjcxxDto fzjcxxDto_s = fzjcxxService.getFzjcxxDto(fzjcxxDto);
		if (StringUtil.isNotBlank(fzjcxxDto_s.getCjsj())){
			map.put("status","fail");
			map.put("message","当前物标信息已被确认！");
			return map;
		}
		boolean result= false;
		try {
			result = fzjcxxService.materialDetConfirm(fzjcxxDto);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.getMessage());
		}
		FzjcxxDto fzjcxxDto_t = new FzjcxxDto();
		fzjcxxDto_t.setCjry(fzjcxxDto.getCjry());
		fzjcxxDto_t.setCyd(fzjcxxDto.getCyd());
		fzjcxxDto_t.setJcdxlx(fzjcxxDto.getJcdxlx());
		//设置检测对象类型（物）
		if (StringUtil.isBlank(fzjcxxDto_t.getJcdxlx())){
			List<JcsjDto> jcdxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
			for (JcsjDto jcdx : jcdxList) {
				if ("W".equals(jcdx.getCsdm())){
					fzjcxxDto_t.setJcdxlx(jcdx.getCsid());
					break;
				}
			}
		}
		Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto_t);
		map.put("gs",countData.get("gs"));
		map.put("status", result ? "success" : "fail");
		map.put("fzjcxxDto",fzjcxxDto);
		return map;
	}

	/**
	 * 小程序 物检确认
	 */
	@RequestMapping("/minidataReflashMaterialPage")
	@ResponseBody
	public Map<String,Object> minidataReflashMaterialPage(FzjcxxDto fzjcxxDto){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		fzjcxxDto.setCjry(user.getYhid());
		//设置检测对象类型（物）
		if (StringUtil.isBlank(fzjcxxDto.getJcdxlx())){
			List<JcsjDto> jcdxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
			for (JcsjDto jcdx : jcdxList) {
				if ("W".equals(jcdx.getCsdm())){
					fzjcxxDto.setJcdxlx(jcdx.getCsid());
					break;
				}
			}
		}
		Map<String,Object> countData = fzjcxxService.getCountData(fzjcxxDto);
		map.put("gs",countData.get("gs"));
		return map;
	}

	/**
	 * 分子检测-修改交付状态
	 */
	@RequestMapping("/detection/sfjfDetection")
	public ModelAndView sfjfDetection(FzjcxxDto fzjcxxDto){
		ModelAndView mav = new ModelAndView("detection/covid/covid_sfjfEdit");
        mav.addObject("fzjcxxDto",fzjcxxDto);
		return mav;
	}

	/**
	 * 修改分子检测信息是否交付状态
	 */
	@RequestMapping("/detection/sfjfModDetection")
	@ResponseBody
	public Map<String,Object> sfjfModDetection(FzjcxxDto fzjcxxDto){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=fzjcxxService.updateSfjfFzjcxx(fzjcxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	//场所码列表===============================================================================================
	/**
	 * 增加一个场所码信息的页面模块
	 */
	@RequestMapping("/detection/pageListCsm")
	public ModelAndView pageListCsm() {
		return new ModelAndView("detection/csmxx/csmxx_list");
	}

	/**
	 * 显示场所码信息列表
	 */
	@RequestMapping("/detection/pageGetListCsm")
	@ResponseBody
	public Map<String, Object> pageGetListCsm(CsmxxDto csmxxDto){
		Map<String,Object> map= new HashMap<>();
		List<CsmxxDto> csmlist = csmxxService.getPagedDtoList(csmxxDto);

		Map<String,String> jcdwmap= new HashMap<>();
		Map<String,String> cydmap= new HashMap<>();
		//检测单位
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for ( JcsjDto jcdw_jcsj : jcdwList ){
			jcdwmap.put(jcdw_jcsj.getCsid() , jcdw_jcsj.getCsmc());
		}
		//采样点
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for ( JcsjDto cyd_jcsj : cydList ){
			cydmap.put(cyd_jcsj.getCsid() , cyd_jcsj.getCsmc());
		}

		String cydmc, jcdwmc;
		for (CsmxxDto dto : csmlist){
			cydmc = cydmap.get(  dto.getCyd() );
			jcdwmc = jcdwmap.get(  dto.getJcdw() );
			if (StringUtil.isNotBlank(cydmc)){
				dto.setCydmc( cydmc );
			}
			if (StringUtil.isNotBlank(jcdwmc)){
				dto.setJcdwmc(  jcdwmc );
			}
		}
		map.put("total",csmxxDto.getTotalNumber());
		map.put("rows",csmlist);
		return map;
	}

	/**
	 * 查看场所码详情
	 */
	@RequestMapping("/csmxx/viewCsm")
	@ResponseBody
	public ModelAndView viewCsm(CsmxxDto csmxxDto){
		ModelAndView mav=new ModelAndView("detection/csmxx/csmxx_view");
		//这两个map，只是为了getDto少连接几次基础数据表，采样点和检测单位从redis取
		Map<String,String> jcdwmap= new HashMap<>();
		Map<String,String> cydmap= new HashMap<>();
		//检测单位
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for ( JcsjDto jcdw_jcsj : jcdwList ){
			jcdwmap.put(jcdw_jcsj.getCsid() , jcdw_jcsj.getCsmc());
		}
		//采样点
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for ( JcsjDto cyd_jcsj : cydList ){
			cydmap.put(cyd_jcsj.getCsid() , cyd_jcsj.getCsmc());
		}
		csmxxDto = csmxxService.getDto(csmxxDto);
		String cydmc = cydmap.get(  csmxxDto.getCyd() );
		String jcdwmc = jcdwmap.get(  csmxxDto.getJcdw() );
		if (StringUtil.isNotBlank(cydmc)){
			csmxxDto.setCydmc( cydmc );
		}
		if (StringUtil.isNotBlank(jcdwmc)){
			csmxxDto.setJcdwmc(  jcdwmc );
		}
		mav.addObject("csmxxDto", csmxxDto);
		return mav;
	}

	/**
	 * 新增场所码页面
	 */
	@RequestMapping("/csmxx/addCsm")
	@ResponseBody
	public ModelAndView addCsm(CsmxxDto csmxxDto){
		ModelAndView mav=new ModelAndView("detection/csmxx/csmxx_edit");
		csmxxDto.setFormAction("addSaveCsm");
		//采样点，设置默认采样点为选中
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for (JcsjDto cyd : cydList) {
			if ("1".equals(cyd.getSfmr())){
				csmxxDto.setCyd(cyd.getCsid());
			}
		}
		//设置检测单位为默认的检测单位（杭州实验室）
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for (JcsjDto jcdw : jcdwList) {
			if ("1".equals(jcdw.getSfmr())){
				csmxxDto.setJcdw(jcdw.getCsid());
			}
		}
		mav.addObject("cydList", cydList);//采样点
		mav.addObject("jcdwxxList", jcdwList);//检测单位
		mav.addObject("csmxxDto", csmxxDto);
		return mav;
	}

	/**
	 * 增加场所码数据到数据库
	 */
	@RequestMapping("/csmxx/addSaveCsm")
	@ResponseBody
	public Map<String, Object> addSaveCsm(CsmxxDto csmxxDto){
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo();
		csmxxDto.setLrry(user.getYhid());
		csmxxDto.setCsmid(StringUtil.generateUUID());
		boolean isSuccess=csmxxService.insertDto(csmxxDto);

		try {
			//redis存场所码时候要计算系统时间和有效时间，有效时间和当前时间相差6小时以上则redis过期设置6h，大于0小于6小时则使用两者相差的时间间隔，小于0则不存入
			String yxrqStr = DateUtils.getCustomFomratCurrentDate(csmxxDto.getYxrq());
			String nowStr = DateUtils.format(System.currentTimeMillis());
			Date yxrqDate = dfs.parse(yxrqStr);
			Date nowDate = dfs.parse(nowStr);
			long diff = (yxrqDate.getTime() - nowDate.getTime()) / (1000 * 60);//min
			if ( diff > 6*60 ){
				redisUtil.set("CSM:"+ csmxxDto.getCsmid(), JSONObject.toJSONString(csmxxDto) ,21600);//注意redis过期时间是秒为单位，不是毫秒
			}else if ( 0 < diff && diff < 6*60 ){
				redisUtil.set("CSM:"+ csmxxDto.getCsmid(), JSONObject.toJSONString(csmxxDto) ,diff*60);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改时新增一个页面
	 */
	@RequestMapping("/csmxx/modCsm")
	@ResponseBody
	public ModelAndView modCsm(CsmxxDto csmxxDto){
		ModelAndView mav=new ModelAndView("detection/csmxx/csmxx_edit");
		//这两个map，只是为了getDto少连接几次基础数据表，采样点和检测单位从redis取
		Map<String,String> jcdwmap= new HashMap<>();
		Map<String,String> cydmap= new HashMap<>();
		//检测单位
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for ( JcsjDto jcdw_jcsj : jcdwList ){
			jcdwmap.put(jcdw_jcsj.getCsid() , jcdw_jcsj.getCsmc());
		}
		//采样点
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for ( JcsjDto cyd_jcsj : cydList ){
			cydmap.put(cyd_jcsj.getCsid() , cyd_jcsj.getCsmc());
		}

		CsmxxDto csmxx = csmxxService.getDto(csmxxDto);
		csmxx.setFormAction("modSaveCsm");
		csmxx.setAccess_token(csmxxDto.getAccess_token());
		String cydmc = cydmap.get(  csmxx.getCyd() );
		String jcdwmc = jcdwmap.get(  csmxx.getJcdw() );
		if (StringUtil.isNotBlank(cydmc)){
			csmxx.setCydmc( cydmc );
		}
		if (StringUtil.isNotBlank(jcdwmc)){
			csmxx.setJcdwmc(  jcdwmc );
		}

		mav.addObject("cydList", cydList);//采样点
		mav.addObject("jcdwxxList", jcdwList);//检测单位
		mav.addObject("csmxxDto", csmxx);
		return mav;
	}

	/**
	 * 修改消息到数据库
	 */
	@RequestMapping("/csmxx/modSaveCsm")
	@ResponseBody
	public Map<String,Object> modSaveCsm(CsmxxDto csmxxDto){
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		csmxxDto.setXgry(user.getYhid());
		boolean isSuccess;
		isSuccess=csmxxService.updateDto(csmxxDto);

		try {
			//redis存场所码时候要计算系统时间和有效时间，有效时间和当前时间相差6小时以上则redis过期设置6h，大于0小于6小时则使用两者相差的时间间隔，小于0则不存入
			String yxrqStr = DateUtils.getCustomFomratCurrentDate(csmxxDto.getYxrq());
			String nowStr = DateUtils.format(System.currentTimeMillis());
			Date yxrqDate = dfs.parse(yxrqStr);
			Date nowDate = dfs.parse(nowStr);
			long diff = (yxrqDate.getTime() - nowDate.getTime()) / (1000 * 60);//min
			if ( diff > 6*60 ){
				redisUtil.set("CSM:"+ csmxxDto.getCsmid(), JSONObject.toJSONString(csmxxDto) ,21600);
			}else if ( 0 < diff && diff < 6*60 ){
				redisUtil.set("CSM:"+ csmxxDto.getCsmid(), JSONObject.toJSONString(csmxxDto) ,diff*60);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除消息
	 */
	@RequestMapping("/csmxx/delCsm")
	@ResponseBody
	public Map<String,Object> delCsm(CsmxxDto csmxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = csmxxService.delete(csmxxDto);
		//del() redis中删除
		List<String> ids = csmxxDto.getIds();
		for (String id: ids){
			redisUtil.del("CSM:"+id);
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 打印场所码二维码
	 */
	@RequestMapping("/csmxx/printQRCsm")
	public ModelAndView printQRCsm(String csmid) {
		ModelAndView mav=new ModelAndView("detection/csmxx/csmxx_qr");

		//在这里把id的参数加密一下放入
		DBEncrypt dbEncrypt=new DBEncrypt();
		String sign = dbEncrypt.eCode(csmid);
		String url = address + "/ws/detection/checkToken?data="+csmid+"&sign="+sign;//address是获取配置的applicationurl

		//这两个map，只是为了getDto少连接几次基础数据表，采样点和检测单位从redis取
		Map<String,String> jcdwmap= new HashMap<>();
		Map<String,String> cydmap= new HashMap<>();
		//检测单位
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for ( JcsjDto jcdw_jcsj : jcdwList ){
			jcdwmap.put(jcdw_jcsj.getCsid() , jcdw_jcsj.getCsmc());
		}
		//采样点
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for ( JcsjDto cyd_jcsj : cydList ){
			cydmap.put(cyd_jcsj.getCsid() , cyd_jcsj.getCsmc());
		}
		CsmxxDto csmxxDto = csmxxService.getDtoById(csmid);
		String cydmc = cydmap.get(  csmxxDto.getCyd() );
		String jcdwmc = jcdwmap.get(  csmxxDto.getJcdw() );
		if (StringUtil.isNotBlank(cydmc)){
			csmxxDto.setCydmc( cydmc );
		}
		if (StringUtil.isNotBlank(jcdwmc)){
			csmxxDto.setJcdwmc(  jcdwmc );
		}
		mav.addObject("csmid",csmid);
		mav.addObject("sign",sign);
		mav.addObject("url",URLEncoder.encode(url,StandardCharsets.UTF_8));

		mav.addObject("cydmc",csmxxDto.getCydmc());
		mav.addObject("jcdwmc",csmxxDto.getJcdwmc());
		mav.addObject("sjdwmc",csmxxDto.getSjdwmc());
		mav.addObject("xm",csmxxDto.getXm());
		mav.addObject("yxrq",csmxxDto.getYxrq());

		return mav;
	}

	/**
	 * 钉钉扫描场所码（人检物检通用）
	 */
	@RequestMapping("/csmxx/minidataQRCsm")
	@ResponseBody
	public Map<String,Object> minidataQRCsm(String csmid) {
		Map<String,Object> resmap = new HashMap<>();
		//这两个map，只是为了getDto少连接几次基础数据表，采样点和检测单位从redis取
		Map<String,String> jcdwmap= new HashMap<>();
		Map<String,String> cydmap= new HashMap<>();
		//检测单位
		List<JcsjDto> jcdwList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for ( JcsjDto jcdw_jcsj : jcdwList ){
			jcdwmap.put(jcdw_jcsj.getCsid() , jcdw_jcsj.getCsmc());
		}
		//采样点
		List<JcsjDto> cydList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
		for ( JcsjDto cyd_jcsj : cydList ){
			cydmap.put(cyd_jcsj.getCsid() , cyd_jcsj.getCsmc());
		}
		CsmxxDto csmxxDto = csmxxService.getDtoById(csmid);
		String cydmc = cydmap.get(  csmxxDto.getCyd() );
		String jcdwmc = jcdwmap.get(  csmxxDto.getJcdw() );
		if (StringUtil.isNotBlank(cydmc)){
			csmxxDto.setCydmc( cydmc );
		}
		if (StringUtil.isNotBlank(jcdwmc)){
			csmxxDto.setJcdwmc(  jcdwmc );
		}
		resmap.put("csmxxDto",csmxxDto);
		return resmap;
	}

	/**
	 * 采集信息上报卫健
	 */
	@RequestMapping("/detection/cjsjReport")
	@ResponseBody
	public Map<String,Object> cjsjReport(FzjcxxDto fzjcxxDto){
		Map<String,Object> map=new HashMap<>();
//		User user = getLoginInfo();
		boolean isSuccess=fzjcxxService.cjsjReport(fzjcxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

}
