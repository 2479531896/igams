package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqyyDto;
import com.matridx.igams.wechat.dao.entities.PayinfoDto;
import com.matridx.igams.wechat.dao.entities.SjjcjgDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqyyService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IPayinfoService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recheck")
public class RecheckController extends BaseController{
	
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired
	private IFjsqService fjsqService;
	@Autowired
	private IFjcfbService fjcfbService;
	
	@Autowired 
	private IXxglService xxglservice;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private ISjjcjgService sjjcjgService;
	
	@Autowired
	private IJcsjService jcsjService;
	
	@Autowired
	private IHbqxService hbqxService;
	
	@Autowired
	private ISjhbxxService sjhbxxservice;
	
	@Autowired
	IPayinfoService payinfoService;
	
	@Autowired
	ICommonService commonService;

	@Autowired
	IFjsqyyService fjsqyyService;
	
	@Autowired
	private ISjybztService sjybztService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 复检申请页面
	 * @return
	 */
	@RequestMapping("/recheck/pageListRecheck")
	public ModelAndView pageList(FjsqDto fjsqDto) {
		User user=getLoginInfo();
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_List");
		fjsqDto.setAuditType(AuditTypeEnum.AUDIT_RECHECK.getCode());
		fjsqDto.setAuditTypeOut(AuditTypeEnum.AUDIT_OUT_RECHECK.getCode());
		fjsqDto.setAuditTypeDing(AuditTypeEnum.AUDIT_DING_RECHECK.getCode());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE});
		fjsqDto.setAuth_flag("0");
		mav.addObject("RecheckType", jclist.get(BasicDataTypeEnum.RECHECK.getCode()));
		mav.addObject("DetectionUnit", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("DetectType",jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("yhid",user.getYhid());
		return mav;
	}
	
	/**
	 * 钉钉高级筛选条件
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/recheck/minidataGetFilterForRecheck")
	@ResponseBody
	public Map<String,Object> getFilterForRecheck(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK,BasicDataTypeEnum.DETECTION_UNIT});
		map.put("recheckType", jclist.get(BasicDataTypeEnum.RECHECK.getCode()));//复检类型
		map.put("detectionUnit", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		return map;
	}
	/**
	 * 复检申请列表
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/pagedataListRecheck")
	@ResponseBody
	public Map<String,Object> getPagedDtoList(FjsqDto fjsqDto,HttpServletRequest request){
		User user=getLoginInfo();
		List<FjsqDto> fjsqList;
		if("1".equals(fjsqDto.getLoad_flag())) {
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
				fjsqDto.setUserids(userids);
			}
			//判断伙伴权限
			List<String> hbqxList = hbqxService.getHbidByYhid(user.getYhid());
			if(hbqxList!=null && hbqxList.size()>0) {
				List<String> hbmcList=sjhbxxservice.getHbmcByHbid(hbqxList);
				if(hbmcList!=null  && hbmcList.size()>0) {
					fjsqDto.setSjhbs(hbmcList);
				}
			}
		}
		//判断是正常复检列表还是销售复检列表
		if(("1").equals(fjsqDto.getAuth_flag())) {
			List<String> hbqxList=hbqxService.getHbidByYhid(user.getYhid());
			if(hbqxList!=null && hbqxList.size()>0) {
				fjsqDto.setHbids(hbqxList);
			}
		}
		List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					fjsqDto.setJcdwxz(strList);
					fjsqList=fjsqService.getPagedDtoList(fjsqDto);
				}else {
					fjsqList= new ArrayList<>();
				}
			}else {
				fjsqList=fjsqService.getPagedDtoList(fjsqDto);
			}
		}else {
			fjsqList=fjsqService.getPagedDtoList(fjsqDto);
		}

		Map<String, Object> map= new HashMap<>();
		map.put("total", fjsqDto.getTotalNumber());
		map.put("rows", fjsqList);
		screenClassColumns(request,map);
		return map;
	}

	/**
	 * 高级修改页面
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/advancedmodRecheck")
	public ModelAndView advancedmodRecheck(FjsqDto fjsqDto) {
		return modRecheck(fjsqDto);
	}

	
	/**
	 * 修改页面
	 * @param dto
	 * @return
	 */ 
	@RequestMapping("/recheck/modRecheck")
	public ModelAndView modRecheck(FjsqDto dto) {
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_mod");
		FjsqDto fjsqDto=fjsqService.getDtoById(dto.getFjid());
		fjsqDto.setXg_flg(dto.getXg_flg());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.RECHECK_REASON});
		FjsqyyDto fjsqyyDto=new FjsqyyDto();
		fjsqyyDto.setFjid(dto.getFjid());
		List<FjsqyyDto> fjsqyyDtos=fjsqyyService.getDtoList(fjsqyyDto);
		List<JcsjDto> fjyylist=new ArrayList<>();
		List<JcsjDto> fjyyDtos=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK_REASON.getCode());
		if(fjsqyyDtos!=null && fjsqyyDtos.size()>0){
			for(int i=0;i<fjyyDtos.size();i++){
				if(fjsqDto.getLx().equals(fjyyDtos.get(i).getFcsid())) {
					fjyylist.add(fjyyDtos.get(i));
				}
				for(FjsqyyDto t_fjsqyyDto : fjsqyyDtos){
					if(t_fjsqyyDto.getYy().equals(fjyyDtos.get(i).getCsid())){
						fjyyDtos.get(i).setChecked("1");//是否选中
					}
				}
			}
		}
		mav.addObject("detectlist", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//送检单位
		mav.addObject("fjyylist", fjyylist);//复检原因
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
		if(sjxxDtos!=null) {
			if("1".equals(sjxxDtos.getYyxxCskz1())) {
				sjxxDtos.setSjdw(sjxxDtos.getHospitalname() + "-" + sjxxDtos.getSjdwmc());
			}else {
				sjxxDtos.setSjdw(sjxxDtos.getHospitalname());
			}
			mav.addObject("sjxxDto", sjxxDtos);
		}else {
			mav.addObject("sjxxDto", sjxxDto);
		}
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("modflag", "0");
		return mav;
	}

//	/**
//	 * 高级修改
//	 * @param fjid
//	 * @return
//	 */
//	@RequestMapping("/recheck/advancedmodRecheck")
//	public ModelAndView advancedmodRecheck(String fjid) {
//		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_mod");
//		FjsqDto fjsqDto=fjsqService.getDtoById(fjid);
//		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.RECHECK_REASON});
//		FjsqyyDto fjsqyyDto=new FjsqyyDto();
//		fjsqyyDto.setFjid(fjid);
//		List<FjsqyyDto> fjsqyyDtos=fjsqyyService.getDtoList(fjsqyyDto);
//		List<JcsjDto> fjyylist=new ArrayList<>();
//		List<JcsjDto> fjyyDtos=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK_REASON.getCode());
//		if(fjsqyyDtos!=null && fjsqyyDtos.size()>0){
//			for(int i=0;i<fjyyDtos.size();i++){
//				if(fjsqDto.getLx().equals(fjyyDtos.get(i).getFcsid())) {
//					fjyylist.add(fjyyDtos.get(i));
//				}
//				for(FjsqyyDto t_fjsqyyDto : fjsqyyDtos){
//					if(t_fjsqyyDto.getYy().equals(fjyyDtos.get(i).getCsid())){
//						fjyyDtos.get(i).setChecked("1");//是否选中
//					}
//				}
//			}
//		}
//		mav.addObject("detectlist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
//		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//送检单位
//		mav.addObject("fjyylist", fjyylist);//复检原因
//		SjxxDto sjxxDto=new SjxxDto();
//		sjxxDto.setSjid(fjsqDto.getSjid());
//		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
//		if(sjxxDtos!=null) {
//			if("1".equals(sjxxDtos.getYyxxCskz1())) {
//				sjxxDtos.setSjdw(sjxxDtos.getHospitalname() + "-" + sjxxDtos.getSjdwmc());
//			}else {
//				sjxxDtos.setSjdw(sjxxDtos.getHospitalname());
//			}
//			mav.addObject("sjxxDto", sjxxDtos);
//		}else {
//			mav.addObject("sjxxDto", sjxxDto);
//		}
//		mav.addObject("fjsqDto", fjsqDto);
//		mav.addObject("modflag", "1");
//		return mav;
//	}
	
	/**
	 * 修改保存
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/modSaveRecheck")
	@ResponseBody
	public Map<String,Object> modSaveRecheck(FjsqDto fjsqDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		fjsqDto.setXgry(user.getYhid());
		boolean isSuccess=fjsqService.updateFjsq(fjsqDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 确认页面
	 * @param fjid
	 * @return
	 */
	@RequestMapping("/recheck/confirmRecheck")
	public ModelAndView confirmRecheck(String fjid) {
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_confirm");
		if(StringUtil.isNotBlank(fjid)){
			FjsqDto fjsqDto=fjsqService.getDtoById(fjid);
			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.RECHECK_REASON});
			List<JcsjDto> fjyylist=new ArrayList<>();
			mav.addObject("detectlist", jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
			mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//送检单位
			mav.addObject("fjyylist", fjyylist);//复检原因
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(fjsqDto.getSjid());
			SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
			if(sjxxDtos!=null) {
				SjxxDto sjxxDto_t=new SjxxDto();
				sjxxDto_t.setYbbh(sjxxDtos.getYbbh());
				sjxxDto_t.setNbbm(sjxxDtos.getNbbm());
				mav.addObject("sjxxDto", sjxxDto_t);
			}else {
				mav.addObject("sjxxDto", sjxxDto);
			}
			FjsqDto fjsqDto_t=new FjsqDto();
			mav.addObject("fjsqDto", fjsqDto_t);
		}else{
			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.RECHECK_REASON});
			FjsqDto fjsqDto=new FjsqDto();
			List<JcsjDto> fjyylist=new ArrayList<>();
			mav.addObject("detectlist", jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
			mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//送检单位
			mav.addObject("fjyylist", fjyylist);//复检原因
			SjxxDto sjxxDto=new SjxxDto();
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("fjsqDto", fjsqDto);
		}
		return mav;
	}

	/**
	 * 根据样本编号获取信息
	 * @param fjsqDto_t
	 * @return
	 */
	@RequestMapping("/recheck/pagedataInfoByYbbh")
	@ResponseBody
	public Map<String,Object> getInfoByYbbh(FjsqDto fjsqDto_t){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		fjsqDto_t.setJsid(user.getJsid());
		FjsqDto fjsqDto=fjsqService.getDtoByYbbh(fjsqDto_t);
		if(fjsqDto!=null){
			List<JcsjDto> fjyyDtos=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK_REASON.getCode());
			FjsqyyDto fjsqyyDto=new FjsqyyDto();
			fjsqyyDto.setFjid(fjsqDto.getFjid());
			List<FjsqyyDto> fjsqyyDtos=fjsqyyService.getDtoList(fjsqyyDto);
			List<JcsjDto> fjyylist=new ArrayList<>();
			if(fjsqyyDtos!=null && fjsqyyDtos.size()>0){
				for(int i=0;i<fjyyDtos.size();i++){
					if(fjsqDto.getLx().equals(fjyyDtos.get(i).getFcsid())) {
						fjyylist.add(fjyyDtos.get(i));
					}
					for(FjsqyyDto t_fjsqyyDto : fjsqyyDtos){
						if(t_fjsqyyDto.getYy().equals(fjyyDtos.get(i).getCsid())){
							fjyyDtos.get(i).setChecked("1");//是否选中
						}
					}
				}
			}
			map.put("fjyylist", fjyylist);//复检原因
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(fjsqDto.getSjid());
			SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
			if(sjxxDtos!=null) {
				if("1".equals(sjxxDtos.getYyxxCskz1())) {
					sjxxDtos.setSjdw(sjxxDtos.getHospitalname() + "-" + sjxxDtos.getSjdwmc());
				}else {
					sjxxDtos.setSjdw(sjxxDtos.getHospitalname());
				}
				map.put("sjxxDto", sjxxDtos);
			}else {
				map.put("sjxxDto", sjxxDto);
			}
		}
		map.put("fjsqDto", fjsqDto);
		return map;
	}
	/**
	 * 查看页面
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/viewRecheck")
	public ModelAndView viewRecheck(FjsqDto fjsqDto) {
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_view");
		FjsqDto fjsqDtos=fjsqService.getDto(fjsqDto);
		SjybztDto sjybztDto = new SjybztDto();
		sjybztDto.setSjid(fjsqDtos.getSjid());
		String xzbj="";
		sjybztDto.setYbztCskz1("S");
		List<SjybztDto> sjybztDtos = sjybztService.getDtoList(sjybztDto);
		if(sjybztDtos!=null && sjybztDtos.size()>0) {
			xzbj = "1";
		}
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDtos.getSjid());
		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
		if(sjxxDtos!=null) {
			if("1".equals(sjxxDtos.getYyxxCskz1())) {
				sjxxDtos.setSjdw(sjxxDtos.getHospitalname() + "-" + sjxxDtos.getSjdwmc());
			}else {
				sjxxDtos.setSjdw(sjxxDtos.getHospitalname());
			}
			mav.addObject("sjxxDto", sjxxDtos);
		}else {
			mav.addObject("sjxxDto", sjxxDto);
		}
		FjcfbDto fjcfbDto1 = new FjcfbDto();
		fjcfbDto1.setYwid(fjsqDtos.getFjid());
		fjcfbDto1.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
		List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto1);
		mav.addObject("fjcfbDtos",fjcfbDtoList);
		mav.addObject("fjsqDto", fjsqDtos);
		mav.addObject("xzbj", xzbj);

		SjjcjgDto sjjcjgDto = new SjjcjgDto();
		sjjcjgDto.setYwid(fjsqDtos.getFjid());
		List<SjjcjgDto> dtoList = sjjcjgService.getDtoList(sjjcjgDto);
		if (null != dtoList && dtoList.size() >0){
			List<SjjcjgDto> dtoList_zk = new ArrayList<>();
			List<SjjcjgDto> dtoList_jl = new ArrayList<>();
			for (int i = 0; i <dtoList.size() ; i++) {
				if (dtoList.get(i).getJcjgmc().startsWith("NC") ){
					if("阳性".equals(dtoList.get(i).getJlmc())){
						dtoList.get(i).setStyleFlag("1");
					}
					dtoList_zk.add(dtoList.get(i));
				}else if(dtoList.get(i).getJcjgmc().startsWith("PC")){
					if("阴性".equals(dtoList.get(i).getJlmc())){
						dtoList.get(i).setStyleFlag("1");
					}
					dtoList_zk.add(dtoList.get(i));
				}else{
					dtoList_jl.add(dtoList.get(i));
				}
			}
			if(dtoList_zk!=null&&dtoList_zk.size() > 0){
				SjjcjgDto sjjcjgDto_avg1=new SjjcjgDto();
				sjjcjgDto_avg1.setJcjgmc("NC-av1");
				sjjcjgDto_avg1.setJlmc("原始数值");
				SjjcjgDto sjjcjgDto_avg2=new SjjcjgDto();
				sjjcjgDto_avg2.setJcjgmc("NC-av2");
				sjjcjgDto_avg2.setJlmc("原始数值");
				for (int i = 0; i <dtoList.size() ; i++) {
					if ("A".equals(dtoList.get(i).getJcjgcskz4())){
						sjjcjgDto_avg1.setJgsz(dtoList.get(i).getPjz());
					}else if("B".equals(dtoList.get(i).getJcjgcskz4())){
						sjjcjgDto_avg2.setJgsz(dtoList.get(i).getPjz());
					}
				}
				dtoList_zk.add(sjjcjgDto_avg1);
				dtoList_zk.add(sjjcjgDto_avg2);
			}
			List<SjjcjgDto> dtoList_yin = new ArrayList<>();
			List<SjjcjgDto> dtoList_yang = new ArrayList<>();
			List<SjjcjgDto> dtoList_hui = new ArrayList<>();
			Map<String, List<SjjcjgDto>> map = dtoList_jl.stream().collect(Collectors.groupingBy(SjjcjgDto::getJlmc));
			if (null != map && map.size()>0){
				Iterator<Map.Entry<String, List<SjjcjgDto>>> entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, List<SjjcjgDto>> entry = entries.next();
					if ("阴性".equals(entry.getKey())){
						dtoList_yin = entry.getValue();
					}else if ("阳性".equals(entry.getKey())){
						dtoList_yang = entry.getValue();
					}else {
						dtoList_hui = entry.getValue();
					}
				}
			}
			mav.addObject("dtoList_zk", dtoList_zk);
			mav.addObject("dtoList_yin", sjjcjgService.streamList(dtoList_yin));
			mav.addObject("dtoList_yang", sjjcjgService.streamList(dtoList_yang));
			mav.addObject("dtoList_hui", sjjcjgService.streamList(dtoList_hui));
			if (null!= dtoList && dtoList.size()>0){
				dtoList.sort(new Comparator<>() {
                                 @Override
                                 public int compare(SjjcjgDto sjjcjgDto, SjjcjgDto sjjcjgDtoq) {
									 return Double.compare(Double.parseDouble(sjjcjgDtoq.getNcjsz()), Double.parseDouble(sjjcjgDto.getNcjsz()));
                                 }
                             }
				);
			}
			mav.addObject("dtoList", dtoList);
		}
		return mav;
	}
	
	/**
	 * 钉钉查看附件信息
	 * @param fjsqDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/recheck/minidataViewRecheckDingtalk")
	@ResponseBody
	public Map<String,Object> viewRecheckDingtalk(FjsqDto fjsqDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		FjsqDto fjsqDtos=fjsqService.getDto(fjsqDto);
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDtos.getSjid());
		SjxxDto sjxxDtos=sjxxService.getDto(sjxxDto);
		if(sjxxDtos!=null) {
			map.put("sjxxDto", sjxxDtos);
		}else {
			map.put("sjxxDto", sjxxDto);
		}
		map.put("fjsqDto", fjsqDtos);
		map.put("shlb",AuditTypeEnum.AUDIT_RECHECK.getCode());
		screenClassColumns(request,map);
		return map;
	}
	/**
	 * 删除操作
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/delRecheck")
	@ResponseBody
	public Map<String,Object> delRecheck(FjsqDto fjsqDto){
		Map<String, Object> map= new HashMap<>();
		User user=getLoginInfo(); 
		fjsqDto.setScry(user.getYhid()); 
		boolean isSuccess=fjsqService.deleteFjsq(fjsqDto);
		map.put("status",isSuccess?"success":"fail"); 
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr()); 
		return map;
	}
	
	/**
	 * 复检申请审核列表
	 * @return
	 */
	@RequestMapping("/recheck/pageListRecheck_Audit")
	public ModelAndView  Recheck_AuditView() {
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_audit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK,BasicDataTypeEnum.DETECTION_UNIT,BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE});
		mav.addObject("RecheckType", jclist.get(BasicDataTypeEnum.RECHECK.getCode()));
		mav.addObject("DetectionUnit", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		mav.addObject("samplelist", jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));//标本类型
		mav.addObject("DetectType",jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));//检测项目
		return mav;
	}
	
	/**
	 * 复检申请审核
	 * @param fjsqDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/recheck/pageGetListRecheck_Audit")
	@ResponseBody
	public Map<String,Object> pageGetListRecheck_Audit(FjsqDto fjsqDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		//附加委托参数
		DataPermission.addWtParam(fjsqDto);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(fjsqDto.getDqshzt())) {
			List<AuditTypeEnum> auditTypes = new ArrayList<>();
			auditTypes.add(AuditTypeEnum.AUDIT_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_OUT_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_DING_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_ADDCHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_ADDCHECK_REM);
			auditTypes.add(AuditTypeEnum.AUDIT_LAB_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_LAB_ADDCHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM);
			DataPermission._add(fjsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fjsq", "fjid", auditTypes, null);
		}else {
			List<AuditTypeEnum> auditTypes = new ArrayList<>();
			auditTypes.add(AuditTypeEnum.AUDIT_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_OUT_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_DING_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_ADDCHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_ADDCHECK_REM);
			auditTypes.add(AuditTypeEnum.AUDIT_LAB_RECHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_LAB_ADDCHECK);
			auditTypes.add(AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM);
			DataPermission._add(fjsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fjsq", "fjid", auditTypes, null);
		}
		//获取当前角色的检测单位
		User user=getLoginInfo();
		List<FjsqDto> listMap;
		List<Map<String,String>> jcdwList=sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null&&jcdwList.size() > 0){
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++){
					if(jcdwList.get(i).get("jcdw")!=null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if(strList!=null && strList.size()>0) {
					fjsqDto.setJcdwxz(strList);
					listMap=fjsqService.getPagedAuditRecheck(fjsqDto);
				}else {
					listMap= new ArrayList<>();
				}
			}else{
				listMap=fjsqService.getPagedAuditRecheck(fjsqDto);
			}
		}else{
			listMap=fjsqService.getPagedAuditRecheck(fjsqDto);
		}

		map.put("total", fjsqDto.getTotalNumber());
		map.put("rows", listMap);
		screenClassColumns(request,map);
		return map;
	}
	
	/**
	 * 销售复检列表（加权限限制）
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/pageListForSales")
	public ModelAndView pageListForSales(FjsqDto fjsqDto) {
		ModelAndView mav=new ModelAndView("wechat/recheck/recheck_List");
		fjsqDto.setAuditType(AuditTypeEnum.AUDIT_RECHECK.getCode());
		fjsqDto.setAuditTypeOut(AuditTypeEnum.AUDIT_OUT_RECHECK.getCode());
		fjsqDto.setAuditTypeDing(AuditTypeEnum.AUDIT_DING_RECHECK.getCode());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RECHECK});
		fjsqDto.setAuth_flag("1");
		mav.addObject("RecheckType", jclist.get(BasicDataTypeEnum.RECHECK.getCode()));
		mav.addObject("fjsqDto", fjsqDto);
		return mav;
	}
	
	/**
	 * 修改检测单位
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value="/recheck/pagedataUpdateJcdw")
	@ResponseBody
	public Map<String,Object> updateJcdw(FjsqDto fjsqDto,String yjcdw){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		fjsqDto.setXgry(user.getYhid());
		boolean result=fjsqService.updateJcdw(fjsqDto,yjcdw);
		if(result) {
			fjsqService.sendUpdateJcdwMessage(fjsqDto);
		}
		map.put("status", result);
		return map;
	}
	
	/**
	 * 退款确认页面
	 * @param fjsqDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/recheck/refundPay")
	public ModelAndView refundPay(FjsqDto fjsqDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/recheck/recheck_payRefund");
		FjsqDto t_fjsqDto = fjsqService.getDto(fjsqDto);
		mav.addObject("fjsqDto", t_fjsqDto);
		// 查询送检退款信息
		List<PayinfoDto> payinfoDtos = payinfoService.selectByYwxx(t_fjsqDto.getFjid(), BusinessTypeEnum.FJ.getCode());
		mav.addObject("payinfoDtos", payinfoDtos);
		return mav;
	}

	/**
	 * 扩展修改
	 * @param fjsqDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/recheck/extend")
	public ModelAndView extend(FjsqDto fjsqDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("wechat/recheck/recheck_extend");
		FjsqDto t_fjsqDto = fjsqService.getDtoById(fjsqDto.getFjid());
//		if(StringUtil.isBlank(t_fjsqDto.getSfgs())){
//			t_fjsqDto.setSfgs("1");
//		}
		mav.addObject("fjsqDto", t_fjsqDto);
		return mav;
	}

	/**
	 * 保存扩展修改
	 * @param fjsqDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/recheck/pagedataDoExtend")
	@ResponseBody
	public Map<String, Object> pagedataDoExtend(FjsqDto fjsqDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo();
		fjsqDto.setXgry(user.getYhid());
		try {
			fjsqService.updateFjsjByFjid(fjsqDto);
			map.put("message","修改成功");
			map.put("status","success");
		}catch (Exception e){
			map.put("message",e.getMessage());
			map.put("status","fail");
		}
		return map;
	}
	/**
	 * 生成退款订单
	 * @param payinfoDto
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/recheck/refundSavePay")
	@ResponseBody
	public Map<String, Object> refundSavePay(PayinfoDto payinfoDto, HttpServletRequest request){
		User user=getLoginInfo();
		payinfoDto.setLrry(user.getYhid());
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		String sign = commonService.getSign(payinfoDto.getZfid());
		payinfoDto.setSign(sign);
		payinfoDto.setYwlx(BusinessTypeEnum.FJ.getCode());
		paramMap.add("payinfoDto", payinfoDto);
		String str = restTemplate.postForObject(menuurl + "/wechat/pay/localRefundApply", paramMap, String.class);
		return JSON.parseObject(str, HashMap.class);
	}
	
	/**
	 * 实验按钮页面
	 */
	@RequestMapping(value = "/recheck/detectionRefund")
	public ModelAndView detectionPage(FjsqDto fjsqDto) {
		ModelAndView mav = new ModelAndView("wechat/recheck/recheck_btnDetection");
		List<FjsqDto> fjsqList = fjsqService.getFjxxByfjids(fjsqDto);
		List<FjsqDto> fjjcxmlist = fjsqService.checkJcxm(fjsqDto);
		mav.addObject("fjsqDto",fjsqDto);
		mav.addObject("fjsq",fjsqList.get(0));
		mav.addObject("fjjcxm",fjjcxmlist.get(0));
		return mav;
	}
	
	/**
	 * 点击检测跳转页面前校验所选数据检测项目是否相同
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value="/recheck/pagedataCheckJcxm")
	@ResponseBody
	public boolean checkJcxm(FjsqDto fjsqDto) {
		List<FjsqDto> fjjcxmlist=fjsqService.checkJcxm(fjsqDto);
        return fjjcxmlist == null || fjjcxmlist.size() <= 1;
	}
	
	/**
	 * 修改检测标记
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value="/recheck/detectionSaveRefund")
	@ResponseBody
	public  Map<String, Object> detectionSaveRefund(FjsqDto fjsqDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		fjsqDto.setXgry(user.getYhid());
		try {
			Map<String,Object> checkSyrq = checkSyrq(fjsqDto);
			if("fail".equals(checkSyrq.get("status"))) {
				return checkSyrq;
			}
			boolean isSuccess = fjsqService.updateJcbjByid(fjsqDto);
			map.put("auditType", AuditTypeEnum.AUDIT_SAMPNUM.getCode());
			map.put("ywid", fjsqDto.getCwid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message",xxglservice.getModelById("ICOM00002").getXxnr());
		}
		
		return map;
	}

	private Map<String,Object> checkSyrq(FjsqDto fjsqDto){
		Map<String,Object> map= new HashMap<>();
		if("1".equals(fjsqDto.getJcbj()) && StringUtils.isBlank(fjsqDto.getSyrq())) {
	    	map.put("status", "fail");
	    	map.put("message", "RNA实验日期不能为空！");
	    }else if("1".equals(fjsqDto.getDjcbj()) && StringUtils.isBlank(fjsqDto.getDsyrq())) {
	    	map.put("status", "fail");
	    	map.put("message", "DNA实验日期不能为空！");
	    }else if("1".equals(fjsqDto.getQtjcbj()) && StringUtils.isBlank(fjsqDto.getQtsyrq())) {
	    	map.put("status", "fail");
	    	map.put("message", "其他实验日期不能为空！");
	    }else if("0".equals(fjsqDto.getJcbj()) && "0".equals(fjsqDto.getDjcbj()) 
	    		&& "0".equals(fjsqDto.getQtjcbj())) {
	    	map.put("status", "fail");
	    	map.put("message", "检查类型必须要选择一个！");
	    }
	    return map;
	}

	/**
	 * 详细查看页面
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/recheck/viewmoreRecheck")
	public ModelAndView viewmoreRecheck(FjsqDto fjsqDto) {
		ModelAndView mav=viewRecheck(fjsqDto);
		mav.addObject("flag","1");
		return mav;
	}

	/**
	 * 获取历史信息
	 */
	@RequestMapping(value ="/recheck/pagedataRecheckHistory")
	@ResponseBody
	public Map<String,Object> pagedataRecheckHistory(FjsqDto fjsqDto){
		Map<String,Object> map = new HashMap<>();
		List<FjsqDto> list = fjsqService.getHistoryList(fjsqDto);
		map.put("list",list);
		return map;
	}

	/**
	 * 检验结果导入
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value="/recheck/uploadReport")
	public ModelAndView uploadReport(FjsqDto fjsqDto){
		ModelAndView mav=new ModelAndView("wechat/sjxx/pathogen_uploadReport");
		fjsqDto = fjsqService.getDto(fjsqDto);
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		sjxxDto.setHzxm(fjsqDto.getHzxm());
		sjxxDto.setYbbh(fjsqDto.getYbbh());
		sjxxDto.setYymc(fjsqDto.getSjdwmc());
		sjxxDto.setJcxmmc(fjsqDto.getJcxmmc());
		sjxxDto.setJczxmmc(fjsqDto.getJczxmmc());
		sjxxDto.setBgrq(fjsqDto.getBgrq());
		sjxxDto.setCskz1(fjsqDto.getJcxmcskz());
		sjxxDto.setCskz3(fjsqDto.getJcxmcskz3());
		// 根据检测项目获取文件类型
		sjxxDto.setYwlx(fjsqDto.getJcxmcskz3()+"_"+fjsqDto.getJcxmcskz());
		sjxxDto.setW_ywlx(fjsqDto.getJcxmcskz3()+"_"+fjsqDto.getJcxmcskz()+"_WORD");
		sjxxDto.setYwlx_q(fjsqDto.getJcxmcskz3()+"_REM_"+fjsqDto.getJcxmcskz());
		sjxxDto.setW_ywlx_q(fjsqDto.getJcxmcskz3()+"_REM_"+fjsqDto.getJcxmcskz()+"_WORD");
		sjxxDto.setW_ywlx_z(BusTypeEnum.IMP_WORD_DETECTION_SELF.getCode());
		mav.addObject("sjxxDto",sjxxDto);
		Object ob_xm = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(),fjsqDto.getJcxm());
		List<JcsjDto> detectlist=  new ArrayList<>();
		List<JcsjDto> subdetectlist=  new ArrayList<>();
		if(ob_xm != null){
			JcsjDto jc_xm = JSONObject.parseObject((String)ob_xm, JcsjDto.class);
			detectlist.add(jc_xm);
			List<JcsjDto> list_zxm= redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			for (JcsjDto jcsjDto : list_zxm) {
				if(jcsjDto.getFcsid().equals(fjsqDto.getJcxm())&&jcsjDto.getCsid().equals(fjsqDto.getJczxm())){
					subdetectlist.add(jcsjDto);
				}
			}
		}

		mav.addObject("detectlist", detectlist);//检测项目
		mav.addObject("subdetectlist", subdetectlist);//检测子项目
		return mav;
	}
}
