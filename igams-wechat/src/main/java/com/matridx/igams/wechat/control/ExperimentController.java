package com.matridx.igams.wechat.control;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.ResFirstDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjkzxxDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxCommonService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/experimentS")
public class ExperimentController extends BaseController{
	private final Logger log = LoggerFactory.getLogger(ExperimentController.class);
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private ISjhbxxService sjhbxxService;
	@Autowired
	private ISjqqjcService sjqqjcService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private IGrlbzdszService grlbzdszService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private ISjjcjgService sjjcjgService;
	@Autowired
	private ILbzdszService lbzdszService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	private ISjjcxmService sjjcxmService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	private ISjybztService sjybztService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	ISjxxCommonService sjxxCommonService;	// 微信通知标本状态的模板ID
	@Value("${matridx.wechat.ybzt_templateid:}")
	private String ybzt_templateid = null;
	@Autowired
	ISjsyglService sjsyglService;
	@Autowired
	private ISjyzService sjyzService;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;

	/**
	 * 新增一个界面
	 * @return
	 */
	@RequestMapping("/experimentS/pageListExperiment")
	public ModelAndView pageListDto(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/experiment/experiment_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,
				BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,
				BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.STAMP_TYPE,BasicDataTypeEnum.CLASSIFY,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT});
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> detectlist = new ArrayList<>();
		StringBuilder strings = new StringBuilder();
		if (!CollectionUtils.isEmpty(sjxxDto.getJcxmids()) && !CollectionUtils.isEmpty(jcsjDtos)) {
			for (String jcxmid : sjxxDto.getJcxmids()) {
				for (JcsjDto jcsjDto : jcsjDtos) {
					if (jcsjDto.getCsdm().equals(jcxmid)){
						detectlist.add(jcsjDto);
						strings.append(",").append(jcsjDto.getCsid());
						if ("F".equals(jcsjDto.getCskz1())){
							mav.addObject("RFSFlag", "1");
						}
					}
				}
			}
			if (StringUtil.isNotBlank(strings.toString())){
				strings = new StringBuilder(strings.substring(1));
			}
		}else{
			detectlist = jcsjDtos;
		}
		mav.addObject("jcxmids", strings.toString());
		mav.addObject("detectlist", detectlist);//检测项目
		mav.addObject("kylist", jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));//科研项目
		List<SjdwxxDto> sjdwxxlist=sjxxService.getSjdw();
		mav.addObject("sjdwxxlist", sjdwxxlist);
		List<String> kylxList = new ArrayList<>();
		List<JcsjDto> kyxmList = jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode());
		for (JcsjDto kyxm : kyxmList) {
			String kylx = StringUtil.isNotBlank(kyxm.getCskz1()) ? kyxm.getCskz1() : "其它";
			boolean isIn = false;
			for (String kylxmc : kylxList) {
				if (kylxmc.equals(kylx)){
					isIn = true;
					break;
				}
			}
			if (!isIn){
				kylxList.add(kylx);
			}
		}
		mav.addObject("kylxList", kylxList);//科研项目类型
		mav.addObject("cskz1List", jclist.get(BasicDataTypeEnum.FIRST_SJXXKZ.getCode()));
		mav.addObject("cskz2List", jclist.get(BasicDataTypeEnum.SECOND_SJXXKZ.getCode()));
		mav.addObject("cskz3List", jclist.get(BasicDataTypeEnum.THIRD_SJXXKZ.getCode()));
		mav.addObject("cskz4List", jclist.get(BasicDataTypeEnum.FOURTH_SJXXKZ.getCode()));
		mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		mav.addObject("stamplist", jclist.get(BasicDataTypeEnum.STAMP_TYPE.getCode()));//盖章类型
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjhbflList", jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
		mav.addObject("sjqfList", jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()));//送检区分
		GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
		grlbzdszDto.setYhid(getLoginInfo().getYhid());
		grlbzdszDto.setYwid(sjxxDto.getYwlx());
		LbzdszDto lbzdszDto = new LbzdszDto();
		lbzdszDto.setYwid(sjxxDto.getYwlx());
		User user=getLoginInfo();
		lbzdszDto.setYhid(user.getYhid());
		lbzdszDto.setJsid(user.getDqjs());
		List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
		List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
		StringBuilder xszdlist = new StringBuilder();
		for (LbzdszDto lbzdszdto: choseList) {
			xszdlist.append(",").append(lbzdszdto.getXszd());
		}
		for (LbzdszDto lbzdszdto: waitList) {
			xszdlist.append(",").append(lbzdszdto.getXszd());
		}
		String limitColumns = "";
		if (StringUtil.isNotBlank(xszdlist.toString())){
			limitColumns = "{'sjxxDto':'"+xszdlist.substring(1)+"'}";
		}
		mav.addObject("limitColumns",limitColumns);
		mav.addObject("lx",sjxxDto.getYwlx());
		mav.addObject("choseList", choseList);
		mav.addObject("waitList", waitList);
		return mav;


	}
	
	
	/**
	 * 显示所有数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/experimentS/pageGetListExperiment")
	@ResponseBody
	public Map<String, Object> getPagedExperiment(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		List<SjxxDto> sjxxlist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				List<String> jcdwlist = new ArrayList<>();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjxxDto.setJcdwxz(jcdwlist);
					sjxxlist = sjxxService.getAccptNotUpload(sjxxDto);
				}
				
			}else {
				sjxxlist = sjxxService.getAccptNotUpload(sjxxDto);//自己的方法，无分页
			}
		}else {
			sjxxlist = sjxxService.getAccptNotUpload(sjxxDto);//自己的方法，无分页
		}

		try{
			shgcService.addShgcxxByYwid(sjxxlist, AuditTypeEnum.AUDIT_RFS_FC.getCode(), "zt", "sjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(sjxxlist, AuditTypeEnum.AUDIT_RFS_SJ.getCode(), "zt", "sjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error("/experimentS/pageGetListExperiment捕获异常信息"+e.getMessage());
			log.error(e.toString());
		}

		map.put("total",sjxxlist.size());
		map.put("rows",sjxxlist);
		//筛选出列表显示的字段，加快列表显示
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 文件上传
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/experimentS/uploadFile")
	public ModelAndView uploadReport(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/experiment/experiment_uploadFile");
		String flg_qf = sjxxDto.getFlg_qf();
		List<FjcfbDto> fjcfbDtos = new ArrayList<>();
		if (StringUtil.isNotBlank(flg_qf) && !"undefined".equals(flg_qf)){
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
			fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
			if( flg_qf.equals("0") ) {
				sjxxDto = sjxxService.getOneFromSJ(sjxxDto);
			}else {
				sjxxDto = sjxxService.getOneFromFJ(sjxxDto);
			}
		}
		sjxxDto.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		mav.addObject("sjxxDto",sjxxDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}

	/**
	 * 文件保存
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/experimentS/uploadSaveFile")
	@ResponseBody
	public Map<String, Object> uploadSaveFile(SjxxDto sjxxDto,HttpServletRequest httpServletRequest){
		Map<String,Object> map = new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo();
			sjxxService.saveHttpServletRequest(httpServletRequest);
			sjxxDto.setXgry(user.getYhid());
			String fjids = sjxxService.uploadSaveFile(sjxxDto);
			map.put("status", "success");
			map.put("fjids", fjids);
			map.put("message","上传成功处理中！");
		} catch (BusinessException e) {
			log.error("/experimentS/uploadSaveFile方法捕获异常信息："+e.getMessage());
			log.error(e.toString());
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}



	/**
	 * 文件上传进度
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/experimentS/pagedataUpView")
	public ModelAndView pagedataUpView(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/experiment/experimentpeo_upView");
		mav.addObject("fjids",sjxxDto.getFjid());
		mav.addObject("count",StringUtil.isBlank(sjxxDto.getFjid())?0:sjxxDto.getFjid().split(",").length);
		return mav;
	}

	/**
	 * 文件上传进度数据
	 * @param
	 * @return
	 */
	@RequestMapping(value="/experimentS/pagedataUpList")
	@ResponseBody
	public Map<String, Object> pagedataUpList(SjxxDto sjxxDto){
		Map<String, Object> map= new HashMap<>();
		if (!CollectionUtils.isEmpty(sjxxDto.getFjids())){
			User user = getLoginInfo();
			List<SjjcjgDto> dtos = new ArrayList<>();
			int count = 0;
			for (String fjid : sjxxDto.getFjids()) {
				Object hget = redisUtil.get("RFS_UP:" + user.getYhid()+":"+fjid);
				if (hget != null){
					List<SjjcjgDto> list = (List<SjjcjgDto>) JSON.parseArray(String.valueOf(hget),SjjcjgDto.class);
					dtos.addAll(list);
					count ++;
				}
			}
			map.put("count",count);
			if (!CollectionUtils.isEmpty(dtos) && sjxxDto.getFjids().size() == count)
				map.put("list",dtos);

		}
		return map;
	}
	/**
	 * 查看选中列信息
	 */
	@RequestMapping("/experimentS/viewExperiment")
	public ModelAndView viewExperiment(SjxxDto sjxxDto) {
		ModelAndView mav;
		String flg_qf = sjxxDto.getFlg_qf();
		if( "0".equals(flg_qf) )
		{
			mav = new ModelAndView("wechat/experiment/experiment_view_sj");
		}else {
			mav = new ModelAndView("wechat/experiment/experiment_view_fj");
		}
		Map<String, Object> map = pagedataViewExperimentMap(sjxxDto);//2023/4/14将查看抽取成方法，用于钉钉接口
		mav.addAllObjects(map);
		return  mav;
	}

	/**
	 * 钉钉小程序对接实验查看接口
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/experimentS/pagedataViewExperimentMap")
	@ResponseBody
	public Map<String,Object> pagedataViewExperimentMap(SjxxDto sjxxDto){
		Map<String,Object> resmap = new HashMap<>();
		String flg_qf = sjxxDto.getFlg_qf();
		FjcfbDto fjcfbDto1 = new FjcfbDto();
		fjcfbDto1.setYwid(sjxxDto.getSjid());
		fjcfbDto1.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto1);
		List<JcsjDto> jcxm_jcsjlist = redisUtil.lgetDto("All_matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE);
		String jcxmdm="";
		if (StringUtil.isBlank(sjxxDto.getJcxmdm())){
			for (JcsjDto jcsjDto : jcxm_jcsjlist){
				if (jcsjDto.getCsmc().contains("ResFirst")){
					jcxmdm = jcsjDto.getCsdm();
					break;
				}
			}
		}
		SjsyglDto sjsyglDto =new SjsyglDto();
		sjsyglDto.setSjid(sjxxDto.getSjid());
		sjsyglDto.setJcxm(StringUtil.isNotBlank(sjxxDto.getJcxmdm())?sjxxDto.getJcxmdm():jcxmdm);
		SjsyglDto sjsyglDto_=sjsyglService.getRfs(sjsyglDto);
		if( "0".equals(flg_qf)||StringUtil.isBlank(flg_qf) )
		{
			sjxxDto = sjxxService.getOneFromSJ(sjxxDto);
			resmap.put("fjcfbDtos",fjcfbDtoList);
			resmap.put("sjxxDto", sjxxDto);
		}else {
			sjxxDto = sjxxService.getOneFromFJ(sjxxDto);
			resmap.put("fjcfbDtos",fjcfbDtoList);
			resmap.put("sjxxDto", sjxxDto);
		}
		if(sjxxDto==null)
			return resmap;

		SjjcjgDto sjjcjgDto = new SjjcjgDto();
		sjjcjgDto.setYwid(sjxxDto.getSjid());
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		List<SjjcjgDto> dtoList_zk = new ArrayList<>();
		List<SjjcjgDto> dtoList_jl = new ArrayList<>();
		List<SjjcjgDto> dtoList_yin = new ArrayList<>();
		List<SjjcjgDto> dtoList_yang = new ArrayList<>();
		List<SjjcjgDto> dtoList_hui = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dtoList)){
            for (SjjcjgDto dto : dtoList) {
                if (dto.getJcjgmc().startsWith("NC")) {
                    if ("阳性".equals(dto.getJlmc())) {
                        dto.setStyleFlag("1");
                    }
                    dtoList_zk.add(dto);
                } else if (dto.getJcjgmc().startsWith("PC")) {
                    if ("阴性".equals(dto.getJlmc())) {
                        dto.setStyleFlag("1");
                    }
                    dtoList_zk.add(dto);
                } else {
                    dtoList_jl.add(dto);
                }
            }
			if(!CollectionUtils.isEmpty(dtoList_zk)){
				SjjcjgDto sjjcjgDto_avg1=new SjjcjgDto();
				sjjcjgDto_avg1.setJcjgmc("NC-av1");
				sjjcjgDto_avg1.setJlmc("原始数值");
				SjjcjgDto sjjcjgDto_avg2=new SjjcjgDto();
				sjjcjgDto_avg2.setJcjgmc("NC-av2");
				sjjcjgDto_avg2.setJlmc("原始数值");
                for (SjjcjgDto dto : dtoList) {
                    if ("A".equals(dto.getJcjgcskz4())) {
                        sjjcjgDto_avg1.setJgsz(dto.getPjz());
                    } else if ("B".equals(dto.getJcjgcskz4())) {
                        sjjcjgDto_avg2.setJgsz(dto.getPjz());
                    }
                }
				dtoList_zk.add(sjjcjgDto_avg1);
				dtoList_zk.add(sjjcjgDto_avg2);
			}
			Map<String, List<SjjcjgDto>> map = dtoList_jl.stream().collect(Collectors.groupingBy(SjjcjgDto::getJlmc));
			if (!CollectionUtils.isEmpty(map)){
                for (Map.Entry<String, List<SjjcjgDto>> entry : map.entrySet()) {
                    if ("阴性".equals(entry.getKey())) {
                        dtoList_yin = entry.getValue();
                    } else if ("阳性".equals(entry.getKey())) {
                        dtoList_yang = entry.getValue();
                    } else {
                        dtoList_hui = entry.getValue();
                    }
                }
			}
		}
		resmap.put("dtoList_zk",dtoList_zk);
		resmap.put("dtoList_yin", sjjcjgService.streamList(dtoList_yin));
		resmap.put("dtoList_yang", sjjcjgService.streamList(dtoList_yang));
		resmap.put("dtoList_hui", sjjcjgService.streamList(dtoList_hui));
		if (!CollectionUtils.isEmpty(dtoList)){
			dtoList.sort((sjjcjgDto1, sjjcjgDtoq) -> Double.compare(Double.parseDouble(sjjcjgDtoq.getNcjsz()), Double.parseDouble(sjjcjgDto1.getNcjsz()))
            );
		}
		resmap.put("dtoList", dtoList);
		resmap.put("sjsyglDto", sjsyglDto_==null?new SjsyglDto():sjsyglDto_);
		return  resmap;
	}
		
	/**
	 * 点击实验按钮复用病原体列表中的实验页面
	 */
	@RequestMapping("/experimentS/detectionMod")
	public ModelAndView showExpePageSJ(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/sjxx/sjxx_detection");
		List<SjxxDto> sjxxList = sjxxService.selectSjxxBySjids(sjxxDto);
		List<SjxxDto> sjjcxmList = sjxxService.jcxmFromSJ(sjxxDto);
		SjxxDto sjxxDtoFirst = sjxxList.get(0);
		sjxxDto.setFormAction("/experimentS/experimentS/detectionSaveMod");
		mav.addObject("sjjcxm",sjjcxmList.get(0));
		mav.addObject("sjxxDto",sjxxDto);
		mav.addObject("sjxx",sjxxDtoFirst);
		return mav;
	}
	/**
	 * 修改检测标记
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/experimentS/detectionSaveMod")
	@ResponseBody
	public  Map<String, Object> detectionSaveMod(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		try {
			Map<String,Object> checkSyrq=sjxxService.checkSyrq(sjxxDto);
			if("fail".equals(checkSyrq.get("status"))) {
				return checkSyrq;
			}
			boolean isSuccess = sjxxService.updateJcbjByid(sjxxDto);
			map.put("auditType", AuditTypeEnum.AUDIT_SAMPNUM.getCode());
			map.put("ywid", sjxxDto.getCwid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			log.error("/experimentS/detectionSaveMod方法捕获异常信息："+e.getMessage());
			map.put("status", "fail");
			map.put("message",e.getMsg());
		}

		return map;
	}
	
	/**
	 * 点击实验按钮跳转新的实验页面
	 */
	@RequestMapping("/experimentS/pagedataDetectionFJ")
	public ModelAndView showExpePageFJ(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/experiment/experiment_btnExper");
		List<SjxxDto> sjxxList = sjxxService.selectSjxxByFjids(sjxxDto);
		List<SjxxDto> sjjcxmList = sjxxService.jcxmFromFJ(sjxxDto);
		SjxxDto sjxxDtoFirst = sjxxList.get(0);
		mav.addObject("sjjcxm",sjjcxmList.get(0));
		mav.addObject("sjxxDto",sjxxDto);
		mav.addObject("sjxx",sjxxDtoFirst);
		return mav;
	}
	
	/**
	 * 修改检测标记（正常送检的复用病原体的实验修改检测标记的方法，复检时修改检测标记使用改方法）
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/experimentS/pagedataJcbjByFjid")
	@ResponseBody
	public  Map<String, Object> updateJcbjByid(SjxxDto sjxxDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = sjxxService.updateJcbjByFjid(sjxxDto);
			map.put("auditType", AuditTypeEnum.AUDIT_SAMPNUM.getCode());
			map.put("ywid", sjxxDto.getCwid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			log.error("/experimentS/pagedataJcbjByFjid方法捕获异常信息："+e.getMessage());
			map.put("status", "fail");
			map.put("message",e.getMsg());
		}
		return map;
	}

	/**
	 * 	RFS项目审核
	 * @return
	 */
	@RequestMapping("/experimentS/pageListRFSAudit")
	public ModelAndView pageListRFSAudit(SjxxDto sjxxDto) {
		ModelAndView mav = new  ModelAndView("wechat/experiment/experiment_auditList");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		return mav;
	}

	/**
	 * 显示所有数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/experimentS/pageGetListRFSAudit")
	@ResponseBody
	public Map<String, Object> pageGetListRFSAudit(SjxxDto sjxxDto, HttpServletRequest request){
		DataPermission.addWtParam(sjxxDto);
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_RFS_FC);
		auditTypes.add(AuditTypeEnum.AUDIT_RFS_SJ);
		if(GlobalString.AUDIT_SHZT_YSH.equals(sjxxDto.getDqshzt())){
			DataPermission.add(sjxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sjxx", "sjid",auditTypes);
		}else{
			DataPermission.add(sjxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sjxx", "sjid",auditTypes);
		}
		DataPermission.addCurrentUser(sjxxDto, getLoginInfo(request));
		Map<String,Object> map = new HashMap<>();

		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		List<SjxxDto> sjxxlist;
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				List<String> jcdwlist = new ArrayList<>();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjxxDto.setJcdwxz(jcdwlist);
					sjxxlist = sjxxService.getPagedAuditDevice(sjxxDto);
				}else {
					sjxxlist= new ArrayList<>();
				}

			}else {
				sjxxlist =sjxxService.getPagedAuditDevice(sjxxDto);
			}
		}else {
			sjxxlist =sjxxService.getPagedAuditDevice(sjxxDto);
		}

		map.put("total",sjxxDto.getTotalNumber());
		map.put("rows",sjxxlist);
		//筛选出列表显示的字段，加快列表显示
		screenClassColumns(request,map);
		return map;
	}




	/**
	 * 审核页面
	 * @param
	 * @return
	 */
	@RequestMapping("/experimentS/auditRfs")
	public ModelAndView modRecheck(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/experiment/experiment_audit");
		String flg_qf = sjxxDto.getFlg_qf();
		FjcfbDto fjcfbDto1 = new FjcfbDto();
		fjcfbDto1.setYwid(sjxxDto.getSjid());
		fjcfbDto1.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto1);
		if( flg_qf.equals("0") ) {
			sjxxDto = sjxxService.getOneFromSJ(sjxxDto);
		}else {
			sjxxDto = sjxxService.getOneFromFJ(sjxxDto);
		}
		List<JcsjDto> detectionList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> jlList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_RESULT.getCode());
		SjjcjgDto sjjcjgDto = new SjjcjgDto();
		sjjcjgDto.setYwid(sjxxDto.getSjid());
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		List<SjjcjgDto> dtoList_zk = new ArrayList<>();
		List<SjjcjgDto> dtoList_jl = new ArrayList<>();
		List<SjjcjgDto> dtoList_yin = new ArrayList<>();
		List<SjjcjgDto> dtoList_yang = new ArrayList<>();
		List<SjjcjgDto> dtoList_hui = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dtoList)){
            for (SjjcjgDto dto : dtoList) {
                if (dto.getJcjgmc().startsWith("NC")) {
                    if ("阳性".equals(dto.getJlmc())) {
                        dto.setStyleFlag("1");
                    }
                    dtoList_zk.add(dto);
                } else if (dto.getJcjgmc().startsWith("PC")) {
                    if ("阴性".equals(dto.getJlmc())) {
                        dto.setStyleFlag("1");
                    }
                    dtoList_zk.add(dto);
                } else {
                    dtoList_jl.add(dto);
                }
            }
			if(!CollectionUtils.isEmpty(dtoList_zk)){
				SjjcjgDto sjjcjgDto_avg1=new SjjcjgDto();
				sjjcjgDto_avg1.setJcjgmc("NC-av1");
				sjjcjgDto_avg1.setJlmc("原始数值");
				SjjcjgDto sjjcjgDto_avg2=new SjjcjgDto();
				sjjcjgDto_avg2.setJcjgmc("NC-av2");
				sjjcjgDto_avg2.setJlmc("原始数值");
                for (SjjcjgDto dto : dtoList) {
                    if ("A".equals(dto.getJcjgcskz4())) {
                        sjjcjgDto_avg1.setJgsz(dto.getPjz());
                    } else if ("B".equals(dto.getJcjgcskz4())) {
                        sjjcjgDto_avg2.setJgsz(dto.getPjz());
                    }
                }
				dtoList_zk.add(sjjcjgDto_avg1);
				dtoList_zk.add(sjjcjgDto_avg2);
			}
			Map<String, List<SjjcjgDto>> map = dtoList_jl.stream().collect(Collectors.groupingBy(SjjcjgDto::getJlmc));
			if (!CollectionUtils.isEmpty(map)){
                for (Map.Entry<String, List<SjjcjgDto>> entry : map.entrySet()) {
                    if ("阴性".equals(entry.getKey())) {
                        dtoList_yin = entry.getValue();
                    } else if ("阳性".equals(entry.getKey())) {
                        dtoList_yang = entry.getValue();
                    } else {
                        dtoList_hui = entry.getValue();
                    }
                }
			}
		}
		mav.addObject("dtoList_zk",dtoList_zk);
		mav.addObject("dtoList_yin", sjjcjgService.streamList(dtoList_yin));
		mav.addObject("dtoList_yang", sjjcjgService.streamList(dtoList_yang));
		mav.addObject("dtoList_hui", sjjcjgService.streamList(dtoList_hui));
		mav.addObject("size_yin", dtoList_yin.size());
		mav.addObject("size_yang", dtoList_yang.size());
		mav.addObject("size_hui", dtoList_hui.size());
		mav.addObject("jlList", jlList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("flg_qf", flg_qf);
		mav.addObject("fjcfbDtos",fjcfbDtoList);
		if (!CollectionUtils.isEmpty(dtoList)){
			dtoList.sort((sjjcjgDto1, sjjcjgDtoq) -> Double.compare(Double.parseDouble(sjjcjgDtoq.getNcjsz()), Double.parseDouble(sjjcjgDto1.getNcjsz()))
            );
		}
		mav.addObject("dtoList", dtoList);
		mav.addObject("detectionList", detectionList);
		return mav;
	}

	/**
	 * 附件删除
	 * @param
	 * @return
	 */
	@RequestMapping("/experimentS/pagedataRemoveFj")
	@ResponseBody
	public Map<String, Object> removeFj(FjcfbDto FjcfbDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		boolean success = fjcfbService.delete(FjcfbDto);
		map.put("status",success);
		return map;
	}
	/**
	 * 报告总数
	 * @param
	 * @return
	 */
	@RequestMapping(value="/experimentS/pagedataBgTotal")
	@ResponseBody
	public Map<String, Object> pagedataBgTotal(SjxxDto sjxxDto){
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		//List<SjxxDto> sjxxlist = new ArrayList<SjxxDto>();
		List<String> jcdwlist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjxxDto.setJcdwxz(jcdwlist);
				}
			}
		}

		return sjxxService.querySjSyxxnums(sjxxDto);
	}




	/**
	 * 实验员列表确认按钮
	 */
	@RequestMapping("/experimentS/confirmExperiment")
	public ModelAndView confirmExperiment(SjxxDto sjxxDto) {
        return new ModelAndView("wechat/experiment/experimentpeo_confirm");
	}

	/**
	 * 已接受未实验数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/experimentS/pagedataConfirm")
	@ResponseBody
	public Map<String, Object> pagedataConfirm(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				List<String> jcdwlist = new ArrayList<>();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjxxDto.setJcdwxz(jcdwlist);
				}
			}
		}

		List<SjxxDto> sjxxlist = sjxxService.getConfirmDtoList(sjxxDto);
		map.put("rows",sjxxlist);
		//筛选出列表显示的字段，加快列表显示
		screenClassColumns(request,map);
		return map;
	}
	
		/**
	 * 查看详情按钮
	 */
	@RequestMapping("/experimentS/viewmoreExperiment")
	public ModelAndView viewmoreExperiment(SjxxDto sjxxDto) {
		ModelAndView mav = viewExperiment(sjxxDto);
		mav.addObject("flag", "1");
		return  mav;
	}


	@RequestMapping("/experimentS/pagedataViewmore")
	public ModelAndView pagedataViewmore(SjxxDto sjxxDto) {
		ModelAndView mav = viewExperiment(sjxxDto);
		mav.addObject("flag", "1");
		return  mav;
	}
	/**
	 * 重发报告
	 * @return
	 */
	@RequestMapping("/experiments/rfssendResFirst")
	public ModelAndView rfssendResFirst(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/resfirst/resend_list");
		SjxxDto dtoById = sjxxService.getDtoById(sjxxDto.getSjid());
		List<FjsqDto> list = new ArrayList<>();
		if (StringUtil.isBlank(sjxxDto.getJcxmdm()))
			sjxxDto.setJcxmdm("047");

		FjsqDto fjsqDto = new FjsqDto();
		fjsqDto.setSjid(sjxxDto.getSjid());
		fjsqDto.setJcxmdm(sjxxDto.getJcxmdm());
		List<FjsqDto> dtoList = fjsqService.getDtoList(fjsqDto);
		for (FjsqDto dto : dtoList) {
			if (StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())){
				dto.setFlg("1");
				list.add(dto);
			}
		}
		SjjcxmDto sjjcxmDto = new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> sjjcxmDtos = sjjcxmService.getDtoList(sjjcxmDto);
		if (!CollectionUtils.isEmpty(sjjcxmDtos)){
			for (SjjcxmDto sjjcxmDto1 : sjjcxmDtos) {
				if (sjxxDto.getJcxmdm().equals(sjjcxmDto1.getJcxmdm()) && StringUtil.isNotBlank(dtoById.getBgrq())){
					FjsqDto dto = new FjsqDto();
					dto.setFjid(dtoById.getSjid());
					dto.setNbbm(dtoById.getNbbm());
					dto.setLrsj(dtoById.getLrsj());
					dto.setRsyrq(dtoById.getSyrq());
					dto.setFlg("0");
					dto.setLxmc("正常送检");
					list.add(dto);
					break;
				}
			}
		}

		mav.addObject("list", JSONObject.toJSONString(list));
		mav.addObject("dto", dtoById);
		mav.addObject("jcxmdm", sjxxDto.getJcxmdm());
		return mav;
	}

	/**
	 * 发送ResFirst报告
	 * @param
	 * @return
	 */
	@RequestMapping("/experiments/rfssendSaveResFirst")
	@ResponseBody
	public Map<String, Object> rfssendSaveResFirst(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String shlb = AuditTypeEnum.AUDIT_RFS_SJ.getCode();
		if (!"0".equals(sjxxDto.getFlg_qf()))
			shlb = AuditTypeEnum.AUDIT_RFS_FC.getCode();
		boolean success;
		try {
			success = sjjcjgService.sendReport(sjxxDto.getFjid(),shlb,null);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 新增ResFirst列表界面
	 * @return
	 */
	@RequestMapping("/experiments/pageListResFirst")
	public ModelAndView pageListResFirst(ResFirstDto resFirstDto) {
		ModelAndView mav = new ModelAndView("wechat/resfirst/resfirst_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE, BasicDataTypeEnum.SD_TYPE, BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.CLASSIFY});
		List<SjdwxxDto> sjdwxxList= sjxxService.getSjdw();
		mav.addObject("jcxmdm",resFirstDto.getJcxmdm());
		mav.addObject("sjdwxxlist",sjdwxxList);
		mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjhbflList", jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
		return mav;
	}

	/**
	 * 显示ResFirst列表界面所有数据
	 * @param resFirstDto
	 * @return
	 */
	@RequestMapping("/experiments/pageGetListResFirst")
	@ResponseBody
	public Map<String, Object> pageGetListResFirst(ResFirstDto resFirstDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<ResFirstDto> resfirstlist;
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				if("1".equals(resFirstDto.getSingle_flag())) {
					//判断伙伴权限
					List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
					if(!CollectionUtils.isEmpty(hbqxList)) {
						List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
						if(!CollectionUtils.isEmpty(hbmcList)) {
							resFirstDto.setSjhbs(hbmcList);
						}
					}
				}
				List<String> jcdwlist = new ArrayList<>();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					resFirstDto.setJcdwxz(jcdwlist);
					resfirstlist = sjxxService.getPagedResFirstList(resFirstDto);
				}else {
					resfirstlist= new ArrayList<>();
				}
			}else {
				resfirstlist = sjxxService.getPagedResFirstList(resFirstDto);
			}
		}else {
			resfirstlist = sjxxService.getPagedResFirstList(resFirstDto);
		}


//		SD_TYPE  kdlx kdlxmc   DETECTION_UNIT jcdw jcdwmc   INSPECTION_DIVISION sjqf sjqfmc
		jcsjService.handleCodeToValue(resfirstlist, new BasicDataTypeEnum[] {
						BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.INSPECTION_DIVISION}, new String[] {
						"kdlx:kdlxmc","jcdw:jcdwmc","sjqf:sjqfmc"});

		try{
			shgcService.addShgcxxByYwid(resfirstlist, AuditTypeEnum.AUDIT_RFS_SJ.getCode(), "zt", "sjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error("/experiments/pageGetListResFirst"+e.getMessage());
			log.error(e.toString());
		}
		map.put("total",resFirstDto.getTotalNumber());
		map.put("rows",resfirstlist);
		return map;
	}

	/**
	 * 新增营销ResFirst列表界面
	 * @return
	 */
	@RequestMapping("/experiments/pageListSalesResFirst")
	public ModelAndView pageListSalesResFirst(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/resfirst/salesResfirst_list");
		Map<String, Object> map = pagedataSalesResFirstMap(sjxxDto);
		mav.addAllObjects(map);
		return mav;
	}
	/**
	 * 新增营销ResFirst列表界面
	 * @return
	 */
	@RequestMapping("/experiments/pagedataSalesResFirstMap")
	@ResponseBody
	public Map<String,Object> pagedataSalesResFirstMap(SjxxDto sjxxDto) {
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> jcxm_jcsj = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DETECT_TYPE);
		Object xtsz_toQy = redisUtil.hget("matridx_xtsz","res_qytime_increment");
		Object xtsz_toTq = redisUtil.hget("matridx_xtsz","res_tqtime_increment");
		Object xtsz_toJc = redisUtil.hget("matridx_xtsz","res_jctime_increment");
		Object xtsz_toKz = redisUtil.hget("matridx_xtsz","res_kztime_increment");
		Object xtsz_toBg = redisUtil.hget("matridx_xtsz","res_bgtime_increment");
		XtszDto sz_Qy = JSON.parseObject(String.valueOf(xtsz_toQy), XtszDto.class);
		XtszDto sz_Tq = JSON.parseObject(String.valueOf(xtsz_toTq), XtszDto.class);
		XtszDto sz_Jc = JSON.parseObject(String.valueOf(xtsz_toJc), XtszDto.class);
		XtszDto sz_Kz = JSON.parseObject(String.valueOf(xtsz_toKz), XtszDto.class);
		XtszDto sz_Bg = JSON.parseObject(String.valueOf(xtsz_toBg), XtszDto.class);
		JcsjDto jcxm_jc = new JcsjDto();
		for (JcsjDto jcsjDto : jcxm_jcsj){
			if (jcsjDto.getCsmc().contains("ResFirst")){
				jcxm_jc = jcsjDto;
				break;
			}
		}
		map.put("szz_toQy",sz_Qy.getSzz());
		map.put("szz_toTq",sz_Tq.getSzz());
		map.put("szz_toJc",sz_Jc.getSzz());
		map.put("szz_toKz",sz_Kz.getSzz());
		map.put("szz_toBg",sz_Bg.getSzz());
		map.put("jcxmdm",sjxxDto.getJcxmdm());
		map.put("jcxm",jcxm_jc.getCsid());
		map.put("samplelist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		map.put("detectionList", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		return map;
	}

	/**
	 * 显示营销ResFirst列表界面所有数据
	 * @return
	 */
	@RequestMapping("/experiments/pageGetListSalesResFirst")
	@ResponseBody
	public Map<String, Object> pageGetListSalesResFirst(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<SjxxDto> resfirstlist;
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				if("1".equals(sjxxDto.getSingle_flag())) {
					//判断伙伴权限
					List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
					if(!CollectionUtils.isEmpty(hbqxList)) {
						List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
						if(!CollectionUtils.isEmpty(hbmcList)) {
							sjxxDto.setSjhbs(hbmcList);
						}
					}
				}
				List<String> jcdwlist = new ArrayList<>();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjxxDto.setJcdwxz(jcdwlist);
					resfirstlist = sjxxService.getPagedSalesResFirstList(sjxxDto);
				}else {
					resfirstlist= new ArrayList<>();
				}
			}else {
				resfirstlist = sjxxService.getPagedSalesResFirstList(sjxxDto);
			}
		}else {
			resfirstlist = sjxxService.getPagedSalesResFirstList(sjxxDto);
		}
//		SD_TYPE  kdlx kdlxmc   DETECTION_UNIT jcdw jcdwmc   INSPECTION_DIVISION sjqf sjqfmc
		jcsjService.handleCodeToValue(resfirstlist, new BasicDataTypeEnum[] {
				BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.INSPECTION_DIVISION}, new String[] {
				"kdlx:kdlxmc","jcdw:jcdwmc","sjqf:sjqfmc"});

		map.put("total",sjxxDto.getTotalNumber());
		map.put("rows",resfirstlist);
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 增加点击新增的页面
	 * @param
	 * @return
	 */
	@RequestMapping("/experiments/addResFirst")
	@ResponseBody
	public ModelAndView addResFirst(){
		ModelAndView mav=new ModelAndView("wechat/resfirst/resfirst_edit");
		List<SjdwxxDto> sjdwxxList= sjxxService.getSjdw();
		List<SjhbxxDto> sjhbList=sjhbxxService.getDB();
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSfzmjc("0");
		sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		sjxxDto.setSjqqjcs(sjqqjcService.getJcz(sjxxDto.getSjid()));//查询前期检测项目；
		sjxxDto.setFormAction("addSaveResFirst");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SAMPLESTATE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT});
		List<JcsjDto> jcxm_jcsjDtos = new ArrayList<>();
		for (JcsjDto jcsj : jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode())){
			if ( jcsj.getCsmc().contains("ResFirst") )
				jcxm_jcsjDtos.add(jcsj);
		}
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("genderlist", jclist.get(BasicDataTypeEnum.GENDER_TYPE.getCode()));//性别
		mav.addObject("datectlist", jcxm_jcsjDtos);//检测项目，仅resfirst项目
		mav.addObject("samplestate", jclist.get(BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
		mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		mav.addObject("decetionlist", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sjdwxxList",sjdwxxList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("sjhbList", sjhbList);
		mav.addObject("divisionList",jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()) );//送检区分
		mav.addObject("kyxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));// 科研项目
		//获取文件类型
		return mav;
	}

	/**
	 * 增加信息到数据库
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/experiments/addSaveResFirst")
	@ResponseBody
	public Map<String, Object> addSaveResFirst(SjxxDto sjxxDto,SjkzxxDto sjkzxxDto){
		Map<String, Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjxxDto.setLrry(user.getYhid());
		sjkzxxDto.setLrry(user.getYhid());
		sjxxDto.setJcbj("0");
		sjxxDto.setZt(StatusEnum.CHECK_PASS.getCode());
		boolean isSuccess=false;
		int countybbh=sjxxService.getCountByybbh(sjxxDto);
		if(countybbh > 0) {
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():"标本编号已经存在,不能重复使用！");
		}else {
			if(StringUtil.isNotBlank(sjxxDto.getNbbm())) {
				int count=sjxxService.getCountBynbbm(sjxxDto);
				if(count > 0) {
					map.put("status",isSuccess?"success":"fail");
					map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():"内部编码已经存在,不能重复使用！");
				}else {
					isSuccess=sjxxService.AddSjxx(sjxxDto,sjkzxxDto);
					map.put("status",isSuccess?"success":"fail");
					map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
				}
			}else {
				isSuccess=sjxxService.AddSjxx(sjxxDto,sjkzxxDto);
				map.put("status",isSuccess?"success":"fail");
				map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			}
		}
		return map;
	}

	/**
	 * 修改时新增一个页面
	 * @param
	 * @return
	 */
	@RequestMapping(value="/experiments/modResFirst")
	@ResponseBody
	public ModelAndView modResFirst(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/resfirst/resfirst_edit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SAMPLESTATE,BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT});
		SjxxDto t_sjxxDto=sjxxService.getDtoById(sjxxDto.getSjid());
		List<SjdwxxDto> sjdwxxList= sjxxService.getSjdw();//查询出所有送检单位；
		t_sjxxDto.setJcxmids(sjjcxmService.getSjjcxm(sjxxDto.getSjid()));//把查询出来的送检项目set到送检信息中去；
		t_sjxxDto.setZts(sjybztService.getZtBysjid(sjxxDto.getSjid()));//查询出的送检样本状态
		t_sjxxDto.setFormAction("modSaveResFirst");
		t_sjxxDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		t_sjxxDto.setXg_flg(sjxxDto.getXg_flg());
		List<FjcfbDto> fjcfbDtos=sjxxService.selectFjByWjid(sjxxDto.getSjid());
		List<JcsjDto> jcxm_jcsjDtos = new ArrayList<>();
		for (JcsjDto jcsj : jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode())){
			if ( jcsj.getCsmc().contains("ResFirst") )
				jcxm_jcsjDtos.add(jcsj);
		}
		SjjcxmDto sjjcxmDto = new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		List<SjjcxmDto> dtoList = sjjcxmService.getDtoList(sjjcxmDto);
		mav.addObject("dtoList", JSON.toJSONString(dtoList));
		mav.addObject("sjxxDto", t_sjxxDto);
		mav.addObject("sjdwxxList", sjdwxxList);
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("datectlist", jcxm_jcsjDtos);//检测项目，仅resfirst项目
		mav.addObject("samplestate", jclist.get(BasicDataTypeEnum.SAMPLESTATE.getCode()));//标本状态
		mav.addObject("expressage", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));//快递类型
		mav.addObject("decetionlist", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("divisionList",jclist.get(BasicDataTypeEnum.INSPECTION_DIVISION.getCode()) );//送检区分
		mav.addObject("kyxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_PROJECT.getCode()));// 科研项目
		if(!CollectionUtils.isEmpty(t_sjxxDto.getJcxmids())) {
			mav.addObject("subdetectlist", sjxxService.getSubDetect(t_sjxxDto.getJcxmids().get(0)));
		}else {
			mav.addObject("subdetectlist", null);
		}
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}

	/**
	 * 修改消息到数据库
	 */
	@RequestMapping(value="/experiments/modSaveResFirst")
	@ResponseBody
	public Map<String,Object> modSaveResFirst(SjxxDto sjxxDto,SjkzxxDto sjkzxxDto){
		Map<String, Object> map= new HashMap<>();
		User user=getLoginInfo();
		sjxxDto.setXgry(user.getYhid());
		sjkzxxDto.setXgry(user.getYhid());
		boolean isSuccess=false;
		if (StringUtil.isNotBlank(sjxxDto.getKyxm())){
			JcsjDto kyxmDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.RESEARCH_PROJECT, sjxxDto.getKyxm());
			if (StringUtil.isNotBlank(kyxmDto.getCskz2())){
				sjxxDto.setSfsf(kyxmDto.getCskz2());
				List<SjjcxmDto> jcxmlist=(List<SjjcxmDto>) JSON.parseArray(sjxxDto.getJcxm(), SjjcxmDto.class);
				if (jcxmlist!=null&&!CollectionUtils.isEmpty(jcxmlist)){
					for(SjjcxmDto dto:jcxmlist){
						dto.setSfsf(kyxmDto.getCskz2());
					}
				}
			}
		}
		try {
			int countybbh = sjxxService.getCountByybbh(sjxxDto);
			if (countybbh > 0) {
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : "标本编号已经存在,不能重复使用！");
			} else {
				if (StringUtil.isNotBlank(sjxxDto.getNbbm())) {
					int count = sjxxService.getCountBynbbm(sjxxDto);
					if (count > 0) {
						map.put("status", isSuccess ? "success" : "fail");
						map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : "内部编码已存在，不能重复使用！");
					} else {
						Map<String, Object> objectMap = sjxxService.UpdateSjxx(sjxxDto,user,sjkzxxDto);
						map.put("status", objectMap.get("status"));
						map.put("message", objectMap.get("message"));
					}
				} else {
					Map<String, Object> objectMap = sjxxService.UpdateSjxx(sjxxDto,user,sjkzxxDto);
					map.put("status", objectMap.get("status"));
					map.put("message", objectMap.get("message"));
				}
			}
		} catch (BusinessException e) {
			log.error("/experiments/modSaveResFirst捕获异常信息"+e.getMessage());
			map.put("status", "fail");
			map.put("message", e.getMsg());
		} catch (Exception e) {
			log.error("/experiments/modSaveResFirst捕获异常信息"+e.getMessage());
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}

	/**
	 * 删除消息
	 * @param
	 * @return
	 */
	@RequestMapping(value="/experiments/delResFirst")
	@ResponseBody
	public Map<String,Object> delResFirst(SjxxDto sjxxDto){
		User user=getLoginInfo();
		sjxxDto.setScry(user.getYhid());
		boolean isSuccess=sjxxService.delete(sjxxDto);
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_DEL.getCode() + JSONObject.toJSONString(sjxxDto));
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 查看消息详情
	 */
	@RequestMapping(value="/experiments/viewResFirst")
	@ResponseBody
	public ModelAndView viewResFirst(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/resfirst/resfirst_view");
		mav.addObject("flag", "1");
		FjcfbDto fjcfbDto1 = new FjcfbDto();
		fjcfbDto1.setYwid(sjxxDto.getSjid());
		fjcfbDto1.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		SjsyglDto sjsyglDto =new SjsyglDto();
		sjsyglDto.setSjid(sjxxDto.getSjid());
		sjsyglDto.setJcxm(sjxxDto.getJcxmdm());
		SjsyglDto sjsyglDto_=sjsyglService.getRfs(sjsyglDto);
		List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto1);
		sjxxDto = sjxxService.getDto(sjxxDto);
		SjjcjgDto sjjcjgDto = new SjjcjgDto();
		sjjcjgDto.setYwid(sjxxDto.getSjid());
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		List<SjjcjgDto> dtoList_jl = new ArrayList<>();
		List<SjjcjgDto> dtoList_zk = new ArrayList<>();
		List<SjjcjgDto> dtoList_yin = new ArrayList<>();
		List<SjjcjgDto> dtoList_yang = new ArrayList<>();
		List<SjjcjgDto> dtoList_hui = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dtoList)){
            for (SjjcjgDto dto : dtoList) {
                if (dto.getJcjgmc().startsWith("NC")) {
                    if ("阳性".equals(dto.getJlmc())) {
                        dto.setStyleFlag("1");
                    }
                    dtoList_zk.add(dto);
                } else if (dto.getJcjgmc().startsWith("PC")) {
                    if ("阴性".equals(dto.getJlmc())) {
                        dto.setStyleFlag("1");
                    }
                    dtoList_zk.add(dto);
                } else {
                    dtoList_jl.add(dto);
                }
            }
			if(!CollectionUtils.isEmpty(dtoList_zk)){
				SjjcjgDto sjjcjgDto_avg1=new SjjcjgDto();
				sjjcjgDto_avg1.setJcjgmc("NC-av1");
				sjjcjgDto_avg1.setJlmc("原始数值");
				SjjcjgDto sjjcjgDto_avg2=new SjjcjgDto();
				sjjcjgDto_avg2.setJcjgmc("NC-av2");
				sjjcjgDto_avg2.setJlmc("原始数值");
                for (SjjcjgDto dto : dtoList) {
                    if ("A".equals(dto.getJcjgcskz4())) {
                        sjjcjgDto_avg1.setJgsz(dto.getPjz());
                    } else if ("B".equals(dto.getJcjgcskz4())) {
                        sjjcjgDto_avg2.setJgsz(dto.getPjz());
                    }
                }
				dtoList_zk.add(sjjcjgDto_avg1);
				dtoList_zk.add(sjjcjgDto_avg2);
			}

			Map<String, List<SjjcjgDto>> map = dtoList_jl.stream().collect(Collectors.groupingBy(SjjcjgDto::getJlmc));
			if (!CollectionUtils.isEmpty(map)){
                for (Map.Entry<String, List<SjjcjgDto>> entry : map.entrySet()) {
                    if ("阴性".equals(entry.getKey())) {
                        dtoList_yin = entry.getValue();
                    } else if ("阳性".equals(entry.getKey())) {
                        dtoList_yang = entry.getValue();
                    } else {
                        dtoList_hui = entry.getValue();
                    }
                }
			}
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_RFS_TEMEPLATE_F.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("bgDtos",fjcfbDtos);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_RFS_TEMEPLATE_F_WORD.getCode());
		List<FjcfbDto> zhwj=fjcfbService.selectzhpdf(fjcfbDto);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("dtoList_zk", dtoList_zk);
		mav.addObject("dtoList_yin", sjjcjgService.streamList(dtoList_yin));
		mav.addObject("dtoList_yang", sjjcjgService.streamList(dtoList_yang));
		mav.addObject("dtoList_hui", sjjcjgService.streamList(dtoList_hui));
		if (!CollectionUtils.isEmpty(dtoList)){
			dtoList.sort((sjjcjgDto1, sjjcjgDtoq) -> Double.compare(Double.parseDouble(sjjcjgDtoq.getNcjsz()), Double.parseDouble(sjjcjgDto1.getNcjsz()))
            );
		}
		//查看当前复检申请信息
		FjsqDto fjsqDto=new FjsqDto();
		String[] zts= {StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_PASS.getCode()};
		fjsqDto.setZts(zts);
		fjsqDto.setSjid(sjxxDto.getSjid());
		List<FjsqDto> fjsqList=fjsqService.getListBySjid(fjsqDto);
		List<SjyzDto> yzxxList = sjyzService.getDtoListBySjid(sjxxDto.getSjid());
		mav.addObject("dtoList", dtoList);
		mav.addObject("fjsqList", fjsqList);
		mav.addObject("yzxxList", yzxxList);
		mav.addObject("fjcfbDtos",fjcfbDtoList);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("sjsyglDto", sjsyglDto_==null?new SjsyglDto():sjsyglDto_);

		return  mav;
	}
	/**
	 * 取样
	 * @return
	 */
	@RequestMapping(value="/experimentS/samplingExperiment")
	public ModelAndView samplingExperiment(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/experiment/experiment_sampling");
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 取样保存
	 * @param sjsyglDto
	 * @return
	 */
	@RequestMapping(value = "/experimentS/samplingSaveExperiment")
	@ResponseBody
	public Map<String, Object> samplingSaveExperiment(SjsyglDto sjsyglDto){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		boolean isSuccess;
		try{
			String[] strings = sjsyglDto.getNbbm().split(" ");
			sjsyglDto.setQyry(user.getYhid());
			sjsyglDto.setXgry(user.getYhid());
			isSuccess = sjsyglService.updateQyrq(sjsyglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("sz_flg",sjsyglDto.getPrint_flg());
			if (isSuccess) {
				List<String> printList = new ArrayList<>();
				printList.add("");
				List<SjsyglDto> infoList = sjsyglService.getTqInfoList(sjsyglDto);
				StringBuilder qrcode = new StringBuilder(strings[0]);
				StringBuilder allCode = new StringBuilder();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
				Date date=new Date();
				String now=simpleDateFormat.format(date);//当前日期

				Map<String, List<SjsyglDto>> listMap = infoList.stream().collect(Collectors.groupingBy(SjsyglDto::getKzcs2));
				if (!CollectionUtils.isEmpty(listMap)){
                    for (Map.Entry<String, List<SjsyglDto>> entry : listMap.entrySet()) {
                        List<SjsyglDto> resultModelList = entry.getValue();
                        if (!CollectionUtils.isEmpty(resultModelList)) {
                            qrcode.append(" ").append(entry.getKey()).append("/").append(resultModelList.get(0).getKzcs3());
                            allCode.append(" ").append(entry.getKey());
                            String code = strings[0] + " " + entry.getKey() + "/" + resultModelList.get(0).getKzcs3();
                            String nbzbm = entry.getKey();
//							for (SjsyglDto resultModel:resultModelList) {
//								nbzbm += " "+resultModel.getNbzbm();
//							}
//							if (StringUtil.isNotBlank(nbzbm)){
//								nbzbm = nbzbm.substring(1);
//							}
                            String sign = commonService.getSign(code);
                            int fontflg = 1;
                            if (code.length() > 10) {
                                fontflg = 2;
                            }
                            //sign = URLEncoder.encode(sign, StandardCharsets.UTF_8);
                            if ("1".equals(sjsyglDto.getPrint_flg())) {
                                String url = ":8082/Print?code=" + nbzbm + " " + now + "&sign=" + sign + "&num=" + (StringUtil.isNotBlank(sjsyglDto.getPrint_num()) ? sjsyglDto.getPrint_num() : "3") + "&fontflg=" + fontflg
                                        + "&flg=2&word=" + (strings[0].startsWith("MD") ? strings[0].substring(2) : strings[0]) + "&qrcode=" + code + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
                                printList.add(url);
                                log.error("打印地址：" + url);
                            } else {
                                String url = ":8081/Print?code=" + nbzbm + " " + now + "&sign=" + sign + "&num=" + (StringUtil.isNotBlank(sjsyglDto.getPrint_num()) ? sjsyglDto.getPrint_num() : "3") + "&fontflg=" + fontflg
                                        + "&flg=2&word=" + (strings[0].startsWith("MD") ? strings[0].substring(2) : strings[0]) + "&qrcode=" + code + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
                                printList.add(url); 
                                log.error("打印地址：" + url);
                            }
                        }
                    }
					String sign = commonService.getSign(qrcode.toString());
					int fontflg = 1;
					if (qrcode.length() > 10) {
						fontflg = 2;
					}
					if ("1".equals(sjsyglDto.getPrint_flg())){
						String url = ":8082/Print?code=" + allCode.substring(1) +" "+now + "&sign=" + sign + "&num="+(StringUtil.isNotBlank(sjsyglDto.getPrint_num())?sjsyglDto.getPrint_num():"3")+"&fontflg=" + fontflg
								+ "&word=" + (strings[0].startsWith("MD")?strings[0].substring(2):strings[0]) + "&qrcode=" + qrcode + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
						printList.set(0,url);
						log.error("打印地址2：" + url);
					}else{
						String url = ":8081/Print?code=" + allCode.substring(1) +" "+now + "&sign=" + sign + "&num="+(StringUtil.isNotBlank(sjsyglDto.getPrint_num())?sjsyglDto.getPrint_num():"3")+"&fontflg=" + fontflg
								+ "&word=" + (strings[0].startsWith("MD")?strings[0].substring(2):strings[0]) + "&qrcode=" + qrcode + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
						printList.set(0,url);
						log.error("打印地址2：" + url);
					}
				}else{
					printList.remove(0);
				}
				map.put("print", printList);
			}
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}

	/**
	 * 获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/experimentS/pagedataGetInspect")
	@ResponseBody
	public Map<String, Object> pagedataGetInspect(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		List<String> jcdwlist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				for (Map<String, String> stringStringMap : dwAndbjlist) {
					if (stringStringMap.get("jcdw") != null) {
						jcdwlist.add(stringStringMap.get("jcdw"));
					}
				}
			}
		}

		if(sjxxDto.getJcdws()!=null){
			Collections.addAll(jcdwlist, sjxxDto.getJcdws());
		}
		String[] strings = sjxxDto.getNbbm().split(" ");
		if (strings != null && strings.length>0){
			sjxxDto.setNbbm(strings[0]);
		}
		SjxxDto dto = sjxxService.getDto(sjxxDto);
		List<SjsyglDto> list = new ArrayList<>();
		List<SjsyglDto> infoList = new ArrayList<>();
		if(dto!=null){
			if(StringUtil.isBlank(dto.getQysj())){
				SjsyglDto sjsyglDto = new SjsyglDto();
				sjsyglDto.setSjid(dto.getSjid());
				//			sjsyglDto.setSfjs("1");
				if(!CollectionUtils.isEmpty(jcdwlist)){
					sjsyglDto.setJcdwxz(jcdwlist);
				}
				for (SjsyglDto sjsyglDto1 : sjsyglService.getDtoList(sjsyglDto)) {
					//注释复检审核通过数据取样操作
					if(DetectionTypeEnum.DETECT_FJ.getCode().equals(sjsyglDto1.getYwlx())){
						if (StatusEnum.CHECK_NO.getCode().equals(sjsyglDto1.getFjzt()) || StatusEnum.CHECK_UNPASS.getCode().equals(sjsyglDto1.getFjzt())){
							sjsyglDto1.setBz("复测/加测审核未通过！");
							infoList.add(sjsyglDto1);
							continue;
						}
						if (StatusEnum.CHECK_SUBMIT.getCode().equals(sjsyglDto1.getFjzt())){
							ShgcDto dtoByYwid = shgcService.getDtoByYwid(sjsyglDto1.getYwid());
							if (null == dtoByYwid || "1".equals(dtoByYwid.getXlcxh())){
								sjsyglDto1.setBz("第一次审核还未通过！");
								infoList.add(sjsyglDto1);
								continue;
							}
						}
					}
					if (StringUtil.isBlank(sjsyglDto1.getQysj()) && StringUtil.isBlank(sjsyglDto1.getSyrq()) && "1".equals(sjsyglDto1.getSfjs()))
						list.add(sjsyglDto1);
					else{
						if (StringUtil.isNotBlank(sjsyglDto1.getQysj())){
							sjsyglDto1.setBz("已取样，请从打印机打印！（内部编码 拼 _2）");
						}else if (StringUtil.isBlank(sjsyglDto1.getSfjs()) || "0".equals(sjsyglDto1.getSfjs())){
							sjsyglDto1.setBz("未接收，先接收再取样！");
						}
						infoList.add(sjsyglDto1);
					}
				}
			}
		}
		map.put("map", list.stream().collect(Collectors.groupingBy(SjsyglDto::getSyglid)));
		map.put("infoMap", infoList.stream().collect(Collectors.groupingBy(SjsyglDto::getSyglid)));
		return map;
	}

	/**
	 * 发送实验信息
	 * @return
	 */
	@RequestMapping(value = "/experimentS/pagedataSendMessage")
	@ResponseBody
	public Map<String, Object> pagedataSendMessage(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String ids = request.getParameter("ids");
		String flag = request.getParameter("flag");
		if(StringUtil.isNotBlank(ids)){
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			String dqrq=formatter.format(date);//当前日期
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.DATE,1);
			date=calendar.getTime();
			String mtrq=formatter.format(date);//明天
			String ICOMM_SJ00048=xxglService.getMsg("ICOMM_SJ00048");
			List<String> list = JSONObject.parseArray(ids,String.class);
			for(String id:list){
				String sjid="";
				if("sj".equals(flag)){
					sjid=id;
				}else if("fj".equals(flag)){
					sjid = fjsqService.getSjidFromFjid(id);
				}
				SjxxDto t_sjxxDto=sjxxService.getDtoById(sjid);
				if(t_sjxxDto!=null) {
					boolean messageBoolean = commonService.queryAuthMessage(t_sjxxDto.getHbid(),"INFORM_HB00008");
					if(messageBoolean){
						String ICOMM_SJ00047=xxglService.getMsg("ICOMM_SJ00047", t_sjxxDto.getHzxm(),t_sjxxDto.getCsmc(),dqrq,mtrq);
						String keyword1 = DateUtils.getCustomFomratCurrentDate("HH:mm:ss");
						String keyword2 = t_sjxxDto.getYbbh();
						Map<String,Object> newMap= new HashMap<>();
						newMap.put("yxbt", ICOMM_SJ00048);
						newMap.put("yxnr", ICOMM_SJ00047);
						newMap.put("ddbt", ICOMM_SJ00048);
						newMap.put("ddnr", ICOMM_SJ00047);
						newMap.put("wxbt", ICOMM_SJ00048);
						newMap.put("remark", ICOMM_SJ00047);
						newMap.put("keyword2", keyword1);
						newMap.put("keyword1", keyword2);
						newMap.put("keyword3", ICOMM_SJ00047);
						newMap.put("templateid", ybzt_templateid);
						map.put("xxmb","TEMPLATE_EXCEPTION");
						String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+ICOMM_SJ00047;

						map.put("reporturl",reporturl);
						sjxxCommonService.sendMessageThread(t_sjxxDto, newMap);
					}
				}
			}
			map.put("status","success");
		}else{
			map.put("status","fail");
		}
		return map;
	}

	/**
	 * 送检实验列表
	 * @return
	 */
	@RequestMapping("/experimentS/pageListExperimentList")
	public ModelAndView pageListExperimentList(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/experiment/experiment_newlist");
		mav.addObject("single_flag", sjxxDto.getSingle_flag());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.FIRST_SJXXKZ,
				BasicDataTypeEnum.SECOND_SJXXKZ,BasicDataTypeEnum.THIRD_SJXXKZ,BasicDataTypeEnum.FOURTH_SJXXKZ,BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.SD_TYPE,
				BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.STAMP_TYPE,BasicDataTypeEnum.CLASSIFY,BasicDataTypeEnum.INSPECTION_DIVISION,BasicDataTypeEnum.RESEARCH_PROJECT});
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> detectlist = new ArrayList<>();
		StringBuilder strings = new StringBuilder();
		if (!CollectionUtils.isEmpty(sjxxDto.getJcxmids()) && !CollectionUtils.isEmpty(jcsjDtos)) {
			mav.addObject("jcxmdm", sjxxDto.getJcxmids().get(0));
			for (String jcxmid : sjxxDto.getJcxmids()) {
				for (JcsjDto jcsjDto : jcsjDtos) {
					if (jcsjDto.getCsdm().equals(jcxmid)){
						detectlist.add(jcsjDto);
						strings.append(",").append(jcsjDto.getCsid());
						if ("F".equals(jcsjDto.getCskz1())){
							mav.addObject("RFSFlag", "1");
						}
					}
				}
			}
			if (StringUtil.isNotBlank(strings.toString())){
				strings = new StringBuilder(strings.substring(1));
			}
		}else{
			detectlist = jcsjDtos;
		}
		if (StringUtil.isNotBlank(sjxxDto.getNbzbm())){
			mav.addObject("nbzbm", sjxxDto.getNbzbm());//内部子编码
		}
		mav.addObject("detectlist", detectlist);//检测项目
		mav.addObject("jcxmids", strings.toString());
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				StringBuilder jcdwxz = new StringBuilder();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwxz.append(",").append(stringStringMap.get("jcdw"));

                    }
                }
				if(StringUtil.isNotBlank(jcdwxz.toString())) {
					mav.addObject("jcdwxz", jcdwxz.substring(1));
				}

			}
		}

		GrlbzdszDto grlbzdszDto = new GrlbzdszDto();
		grlbzdszDto.setYhid(getLoginInfo().getYhid());
		grlbzdszDto.setYwid(sjxxDto.getYwlx());
		LbzdszDto lbzdszDto = new LbzdszDto();
		lbzdszDto.setYwid(sjxxDto.getYwlx());
		lbzdszDto.setYhid(user.getYhid());
		lbzdszDto.setJsid(user.getDqjs());
		List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
		List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);
		StringBuilder xszdlist = new StringBuilder();
		for (LbzdszDto lbzdszdto: choseList) {
			xszdlist.append(",").append(lbzdszdto.getXszd());
		}
		for (LbzdszDto lbzdszdto: waitList) {
			xszdlist.append(",").append(lbzdszdto.getXszd());
		}
		String limitColumns = "";
		if (StringUtil.isNotBlank(xszdlist.toString())){
			limitColumns = "{'sjxxDto':'"+xszdlist.substring(1)+"'}";
		}
		mav.addObject("limitColumns",limitColumns);
		mav.addObject("lx",sjxxDto.getYwlx());
		mav.addObject("choseList", choseList);
		mav.addObject("waitList", waitList);
		return mav;


	}


	/**
	 * 实验列表数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/experimentS/pagedataListExperimentInfo")
	@ResponseBody
	public Map<String, Object> pagedataListExperimentInfo(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(jcdwList) && "1".equals(jcdwList.get(0).get("dwxdbj"))) {
			//判断是否显示个人清单
			if("1".equals(sjxxDto.getSingle_flag())) {
				List<String> userids= new ArrayList<>();
				if(user.getYhid()!=null) {
					userids.add(user.getYhid());
				}
				if(user.getDdid()!=null) {
					userids.add(user.getDdid());
				}
				if(user.getWechatid()!=null) {
					userids.add(user.getWechatid());
				}
				if(userids.size()>0) {
					sjxxDto.setUserids(userids);
				}
				//判断伙伴权限
				List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
				if(hbqxList!=null && hbqxList.size()>0) {
					List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
					if(hbmcList!=null  && hbmcList.size()>0) {
						sjxxDto.setSjhbs(hbmcList);
					}
				}
			}
		}
		List<SjxxDto> sjxxlist = sjxxService.getExperimentList(sjxxDto);
		try{
			shgcService.addShgcxxByYwid(sjxxlist, AuditTypeEnum.AUDIT_RFS_FC.getCode(), "zt", "sjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(sjxxlist, AuditTypeEnum.AUDIT_RFS_SJ.getCode(), "zt", "sjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error("/experimentS/pageGetListExperiment捕获异常信息"+e.getMessage());
			log.error(e.toString());
		}

		map.put("total",sjxxlist.size());
		map.put("rows",sjxxlist);
		//筛选出列表显示的字段，加快列表显示
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 实验统计数量
	 * @param
	 * @return
	 */
	@RequestMapping(value="/experimentS/pagedataExperimentTotal")
	@ResponseBody
	public Map<String, Object> pagedataExperimentTotal(SjxxDto sjxxDto){
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		//List<SjxxDto> sjxxlist = new ArrayList<SjxxDto>();
		if(!CollectionUtils.isEmpty(dwAndbjlist)){
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				List<String> jcdwlist = new ArrayList<>();
                for (Map<String, String> stringStringMap : dwAndbjlist) {
                    if (stringStringMap.get("jcdw") != null) {
                        jcdwlist.add(stringStringMap.get("jcdw"));
                    }
                }
				if(!CollectionUtils.isEmpty(jcdwlist)) {
					sjxxDto.setJcdwxz(jcdwlist);
				}
				//判断是否显示个人清单
				if("1".equals(sjxxDto.getSingle_flag())) {
					List<String> userids= new ArrayList<>();
					if(user.getYhid()!=null) {
						userids.add(user.getYhid());
					}
					if(user.getDdid()!=null) {
						userids.add(user.getDdid());
					}
					if(user.getWechatid()!=null) {
						userids.add(user.getWechatid());
					}
					if(userids.size()>0) {
						sjxxDto.setUserids(userids);
					}
					//判断伙伴权限
					List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
					if(hbqxList!=null && hbqxList.size()>0) {
						List<String> hbmcList=sjhbxxService.getHbmcByHbid(hbqxList);
						if(hbmcList!=null  && hbmcList.size()>0) {
							sjxxDto.setSjhbs(hbmcList);
						}
					}
				}
			}
		}

		return sjxxService.queryExperimentNums(sjxxDto);
	}

	/**
	 * 打印
	 * @return
	 */
	@RequestMapping(value="/experimentS/printExperiment")
	public ModelAndView printExperiment(SjxxDto sjxxDto){
		ModelAndView mav=new ModelAndView("wechat/experiment/experiment_print");
		SjxxDto dto = sjxxService.getDto(sjxxDto);
		List<SjsyglDto> list = new ArrayList<>();
		if(dto!=null){
			SjsyglDto sjsyglDto = new SjsyglDto();
			sjsyglDto.setSjid(dto.getSjid());
			sjsyglDto.setSfjs("1");
			for (SjsyglDto sjsyglDto1 : sjsyglService.getDtoList(sjsyglDto)) {
				if (StringUtil.isNotBlank(sjsyglDto1.getQysj()))
					list.add(sjsyglDto1);
			}
			mav.addObject("sjxxDto",dto);
		}else{
			FjsqDto fjsqDto = fjsqService.getDtoById(sjxxDto.getSjid());
			SjsyglDto sjsyglDto = new SjsyglDto();
			sjsyglDto.setYwid(fjsqDto.getFjid());
			sjsyglDto.setSfjs("1");
			for (SjsyglDto sjsyglDto1 : sjsyglService.getDtoList(sjsyglDto)) {
				if (StringUtil.isNotBlank(sjsyglDto1.getQysj()))
					list.add(sjsyglDto1);
			}
			mav.addObject("sjxxDto",fjsqDto);
		}
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * 取样保存
	 * @param sjsyglDto
	 * @return
	 */
	@RequestMapping(value = "/experimentS/printSaveExperiment")
	@ResponseBody
	public Map<String, Object> printSaveExperiment(SjsyglDto sjsyglDto){
		Map<String, Object> map = new HashMap<>();
		try{
			String[] strings = sjsyglDto.getNbbm().split(" ");
			map.put("sz_flg",sjsyglDto.getPrint_flg());
			List<String> printList = new ArrayList<>();
			printList.add("");
			List<SjsyglDto> infoList = sjsyglService.getTqInfoList(sjsyglDto);
			StringBuilder qrcode = new StringBuilder(strings[0]);
			StringBuilder allCode = new StringBuilder();

			Map<String, List<SjsyglDto>> listMap = infoList.stream().collect(Collectors.groupingBy(SjsyglDto::getKzcs2));
			if (!CollectionUtils.isEmpty(listMap)){
                for (Map.Entry<String, List<SjsyglDto>> entry : listMap.entrySet()) {
                    List<SjsyglDto> resultModelList = entry.getValue();
                    if (!CollectionUtils.isEmpty(resultModelList)) {
                        qrcode.append(" ").append(entry.getKey()).append("/").append(resultModelList.get(0).getKzcs3());
                        allCode.append(" ").append(entry.getKey());
                        String code = strings[0] + " " + entry.getKey() + "/" + resultModelList.get(0).getKzcs3();
                        String nbzbm = entry.getKey();
//							for (SjsyglDto resultModel:resultModelList) {
//								nbzbm += " "+resultModel.getNbzbm();
//							}
//							if (StringUtil.isNotBlank(nbzbm)){
//								nbzbm = nbzbm.substring(1);
//							}
                        String sign = commonService.getSign(code);
                        int fontflg = 1;
                        if (code.length() > 10) {
                            fontflg = 2;
                        }
                        //sign = URLEncoder.encode(sign, StandardCharsets.UTF_8);
                        if ("1".equals(sjsyglDto.getPrint_flg())) {
                            String url = ":8082/Print?code=" + nbzbm + "&sign=" + sign + "&num=" + (StringUtil.isNotBlank(sjsyglDto.getPrint_num()) ? sjsyglDto.getPrint_num() : "3") + "&fontflg=" + fontflg
                                    + "&word=" + (strings[0].startsWith("MD") ? strings[0].substring(2) : strings[0]) + "&qrcode=" + code + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
                            printList.add(url);
                            log.error("打印地址：" + url);
                        } else {
                            String url = ":8081/Print?code=" + nbzbm + "&sign=" + sign + "&num=" + (StringUtil.isNotBlank(sjsyglDto.getPrint_num()) ? sjsyglDto.getPrint_num() : "3") + "&fontflg=" + fontflg
                                    + "&word=" + (strings[0].startsWith("MD") ? strings[0].substring(2) : strings[0]) + "&qrcode=" + code + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
                            printList.add(url);
                            log.error("打印地址：" + url);
                        }
                    }
                }
				String sign = commonService.getSign(qrcode.toString());
				int fontflg = 1;
				if (qrcode.length() > 10) {
					fontflg = 2;
				}
				if ("1".equals(sjsyglDto.getPrint_flg())){
					String url = ":8082/Print?code=" + allCode.substring(1) + "&sign=" + sign + "&num="+(StringUtil.isNotBlank(sjsyglDto.getPrint_num())?sjsyglDto.getPrint_num():"3")+"&fontflg=" + fontflg
							+ "&word=" + (strings[0].startsWith("MD")?strings[0].substring(2):strings[0]) + "&qrcode=" + qrcode + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
					printList.set(0,url);
					log.error("打印地址：" + url);
				}else{
					String url = ":8081/Print?code=" + allCode.substring(1)  + "&sign=" + sign + "&num="+(StringUtil.isNotBlank(sjsyglDto.getPrint_num())?sjsyglDto.getPrint_num():"3")+"&fontflg=" + fontflg
							+ "&word=" + (strings[0].startsWith("MD")?strings[0].substring(2):strings[0]) + "&qrcode=" + qrcode + "&project=" + sjsyglDto.getYbbh() + "&rownum=3";
					printList.set(0,url);
					log.error("打印地址：" + url);
				}
			}else{
				printList.remove(0);
			}
			map.put("print", printList);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		map.put("status", "success");
		return map;
	}
}
