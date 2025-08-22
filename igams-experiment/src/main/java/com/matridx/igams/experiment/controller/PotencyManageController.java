package com.matridx.igams.experiment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.TqglDto;
import com.matridx.igams.experiment.dao.entities.TqmxDto;
import com.matridx.igams.experiment.service.svcinterface.ITqglService;
import com.matridx.igams.experiment.service.svcinterface.ITqmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/experiment")
public class PotencyManageController extends BaseController{

	@Autowired
	ITqglService tqglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ITqmxService tqmxService;
	@Autowired
	IGrszService grszService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ITqmxService tqmxServcie;
	@Autowired
	IXtszService xtszService;
	@Autowired
	RedisUtil redisUtil;
	/**
	 * 跳转提取列表界面
	 */
	@RequestMapping(value ="/potency/pageListPotency")
	@ResponseBody
	public ModelAndView getPotencyPage() {
		return new ModelAndView("experiment/potency/potency_list");
	}
	
	/**
	 * 获取提取列表
	 */
	@RequestMapping("/potency/pageGetListPotency")
	@ResponseBody
	public Map<String,Object> getPotencyPageList(TqglDto tqglDto){
		User user=getLoginInfo();
		List<TqglDto> tqgllist;
		List<Map<String,String>> jcdwList=tqglService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null && jcdwList.size() > 0) {
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList= new ArrayList<>();
				for (Map<String, String> stringStringMap : jcdwList) {
					if (stringStringMap.get("jcdw") != null) {
						strList.add(stringStringMap.get("jcdw"));
					}
				}
				if(strList.size()>0) {
					tqglDto.setJcdwxz(strList);
					tqgllist=tqglService.getPagedTqglDtoList(tqglDto);
				}else {
					tqgllist= new ArrayList<>();
				}
			}else {
				tqgllist=tqglService.getPagedTqglDtoList(tqglDto);
			}
		}else {
			tqgllist=tqglService.getPagedTqglDtoList(tqglDto);
		}
		Map<String, Object> map= new HashMap<>();
		map.put("total",tqglDto.getTotalNumber());
		map.put("rows",tqgllist);
		return map;
	}
	
	/**
	 * 跳转新增界面
	 */
	@RequestMapping("/potency/addPotency")
	@ResponseBody
	public ModelAndView getAddPotencyPage(TqglDto tqglDto) {
		ModelAndView mav=new ModelAndView("experiment/potency/potency_add");
		//查询基础数据的检测单位
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});

		String tqmc="";
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		String t_date = date.replaceAll("-", "");
		
		//查询个人设置的检测单位
		GrszDto grszDto=new GrszDto();
		User user=getLoginInfo();
		grszDto.setYhid(user.getYhid());
		//grszDto.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),
				PersonalSettingEnum.REAGENT_SETTINGS.getCode(),
				GlobalString.MATRIDX_EXTRACT_NBBMEXTED,
				PersonalSettingEnum.LIBRARY_SET_TYPE.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode(),
				PersonalSettingEnum.OPERATOR.getCode(),
				PersonalSettingEnum.COLLATOR.getCode(),
				PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
		Map<String,GrszDto> grszDtoMap=grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszDtoCzr = grszDtoMap.get(PersonalSettingEnum.OPERATOR.getCode());
		GrszDto grszDtoHdr = grszDtoMap.get(PersonalSettingEnum.COLLATOR.getCode());
		if(grszDtoCzr!=null) {
			if(StringUtils.isNotBlank(grszDtoCzr.getSzz())) {
				tqglDto.setCzr(grszDtoCzr.getSzz());
			}
			if(StringUtils.isNotBlank(grszDtoCzr.getGlxx())){
				tqglDto.setCzrmc(grszDtoCzr.getGlxx());
			}
		}
		if(grszDtoHdr!=null) {
			if(StringUtils.isNotBlank(grszDtoHdr.getSzz())) {
				tqglDto.setHdr(grszDtoHdr.getSzz());
			}
			if(StringUtils.isNotBlank(grszDtoHdr.getGlxx())){
				tqglDto.setHdrmc(grszDtoHdr.getGlxx());
			}
		}
		GrszDto grszDto_t =grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
		if(grszDto_t==null) {
			grszDto_t=new GrszDto();
		}
		//核酸提取试剂盒个人设置
		//grszDto.setSzlb(PersonalSettingEnum.REAGENT_SETTINGS.getCode());
		GrszDto grszxx_l = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode());
		if(grszxx_l!=null) {
			tqglDto.setSjph(grszxx_l.getSzz());
		}

		JcsjDto jcsjDto=new JcsjDto();
		if(StringUtils.isNotBlank(grszDto_t.getSzz())) {
			jcsjDto=jcsjService.getDtoById(grszDto_t.getSzz());
			if(jcsjDto!= null) {
				tqmc = jcsjDto.getCsdm() + "-" + t_date;
			}else{
				jcsjDto=new JcsjDto();
			}
		}
		tqglDto.setMc(tqmc);
		tqglDto.setFormAction("addSavePotency");
		XtszDto xtszDto = xtszService.getDtoById(GlobalString.MATRIDX_EXTRACT_NBBMEXTED);
		if(xtszDto!=null) {
			tqglDto.setHzqx(xtszDto.getSzz());
		}
		TqmxDto tqmxDto=new TqmxDto();
		if(jcsjDto!=null){
			tqmxDto.setJcdw(jcsjDto.getCsid());
		}else{
			tqmxDto.setJcdw(jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()).get(0).getCsid());
		}
		//自动排版个人设置
		//grszDto.setSzlb(PersonalSettingEnum.LIBRARY_SET_TYPE.getCode());
		GrszDto grszxx_lib = grszDtoMap.get(PersonalSettingEnum.LIBRARY_SET_TYPE.getCode());
		List<TqmxDto>tqmxDtoList=new ArrayList<>();
		if(grszxx_lib!=null) {
			mav.addObject("zdpb", grszxx_lib.getSzz());
			if("1".equals(grszxx_lib.getSzz())){
				tqmxDtoList=tqmxServcie.getInfoByZdpb(tqmxDto);
			}
		}else{
			mav.addObject("zdpb", "0");
			tqmxDtoList=tqmxServcie.getInfoByZdpb(tqmxDto);
		}
		//自动排版个人设置包含
		//grszDto.setSzlb(PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode());
		GrszDto grszxx_lib_in = grszDtoMap.get(PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode());
		if(grszxx_lib_in!=null) {
			mav.addObject("grszxx_lib_in", grszxx_lib_in.getSzz());

		}else{
			mav.addObject("grszxx_lib_in", "");
		}
		//自动排版个人设置不包含
		//grszDto.setSzlb(PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode());
		GrszDto grszxx_lib_notin = grszDtoMap.get(PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode());
		if(grszxx_lib_notin!=null) {
			mav.addObject("grszxx_lib_notin", grszxx_lib_notin.getSzz());
		}else{
			mav.addObject("grszxx_lib_notin", "");
		}
		//系统设置里是根据 inspect.tq.orderby.01(最后的01为检测单位的代码) 去查找数据，如果没有，则取inspect.tq.orderby的数据---用来排序
		String jcdwCsdm="";
		List<JcsjDto> jcdwList=jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for(JcsjDto jcsjDto1:jcdwList){
			if(jcsjDto1.getCsid().equals(tqmxDto.getJcdw())){
				jcdwCsdm=jcsjDto1.getCsdm();
				break;
			}
		}
		List<TqmxDto> tqmxDtos=new ArrayList<>();
		Map<String, List<TqmxDto>> listMap = tqmxDtoList.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getKzcs3()).orElse("")));
		XtszDto JcdwxtszDto = xtszService.getDtoById("inspect.tq.orderby."+jcdwCsdm);
		String pxStr="";
		if(JcdwxtszDto==null){
			JcdwxtszDto=xtszService.getDtoById("inspect.tq.orderby");
		}
		pxStr=JcdwxtszDto.getSzz();
		String[] pxArr=pxStr.split(",");
		for(String px:pxArr){
			if(listMap.get(px)!=null){
				tqmxDtos.addAll(listMap.get(px));
				listMap.remove(px);
			}
		}
		Set<String> keySet=listMap.keySet();
		for(String key:keySet){
			tqmxDtos.addAll(listMap.get(key));
		}
		//提取其他试剂个人设置
		GrszDto grszxx_reagent_json = grszDtoMap.get(PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode());
		Map<String,String> reagent_json_map = null;
		if(grszxx_reagent_json!=null) {
			String reagent_json = grszxx_reagent_json.getSzz();
			if(StringUtil.isNotBlank(reagent_json))
				reagent_json_map = com.alibaba.fastjson.JSONObject.parseObject(reagent_json, HashMap.class);
		}
		if(reagent_json_map == null)
			reagent_json_map = new HashMap<>();
		//获取提取试剂信息
		List<Map<String, String>> tqsjList = getTqsjList(jcsjDto.getCsdm(),reagent_json_map);

		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("mrjcdw", jcsjDto);
		mav.addObject("TqglDto", tqglDto);
		mav.addObject("sjxsbj", "1");
		mav.addObject("tqglDtoList", tqmxDtos);
		mav.addObject("xhList", new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
		mav.addObject("tqsjList", tqsjList);

		return mav;
	}

	/**
	 * 获取系统设置里的提取试剂信息
	 * @param jcdwdm
	 * @param jcdwdm
	 * @param jcdwdm
	 * @return
	 */
	@NotNull
	private List<Map<String, String>> getTqsjList(String jcdwdm,Map<String,String> reagent_json_map) {
		XtszDto tqsjtszDto = xtszService.getDtoById("reagent.extend.extract." + jcdwdm);
		if(tqsjtszDto==null || StringUtil.isBlank(tqsjtszDto.getSzz())){
			tqsjtszDto = xtszService.getDtoById("reagent.extend.extract");
		}
		if(tqsjtszDto==null){
			tqsjtszDto=new XtszDto();
		}
		JSONArray jsonArray_tq=JSONArray.parseArray(tqsjtszDto.getSzz());
		List<Map<String,String>> tqsjList=new ArrayList<>();
		for(int i = 0; i < jsonArray_tq.size(); i++){
			JSONObject jsonObject=jsonArray_tq.getJSONObject(i);
			if(StringUtil.isNotBlank(jsonObject.getString("status"))&&"0".equals(jsonObject.getString("status"))){
				Map<String,String> tqMap=new HashMap<>();
				tqMap.put("title",jsonObject.getString("title"));
				tqMap.put("variable",jsonObject.getString("variable"));
				tqMap.put("type",jsonObject.getString("type"));
				tqMap.put("status",jsonObject.getString("status"));
				if(reagent_json_map!= null && reagent_json_map.containsKey(jsonObject.getString("variable"))){
					tqMap.put("value",reagent_json_map.get(jsonObject.getString("variable")));
				}else{
					tqMap.put("value","");
				}
				tqsjList.add(tqMap);
			}
		}
		return tqsjList;
	}

	@RequestMapping("/potency/pagedataGetTqsj")
	@ResponseBody
	public Map<String,Object> pagedataGetTqsj(TqmxDto tqmxDto){
		Map<String,Object> map=new HashMap<>();
		String jcdw = tqmxDto.getJcdw();
		List<Map<String,String>> tqsjList=new ArrayList<>();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
		if(optional.isPresent()) {
			JcsjDto JcdwDto = optional.get();
			//查询个人设置的检测单位
			GrszDto grszDto=new GrszDto();
			User user=getLoginInfo();
			grszDto.setYhid(user.getYhid());
			//grszDto.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
			grszDto.setSzlbs(new String[]{
					PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
			Map<String,GrszDto> grszDtoMap=grszService.selectGrszMapByYhidAndSzlb(grszDto);
			//提取其他试剂个人设置
			GrszDto grszxx_reagent_json = grszDtoMap.get(PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode());
			Map<String,String> reagent_json_map = null;
			if(grszxx_reagent_json!=null) {
				String reagent_json = grszxx_reagent_json.getSzz();
				if(StringUtil.isNotBlank(reagent_json))
					reagent_json_map = com.alibaba.fastjson.JSONObject.parseObject(reagent_json, HashMap.class);
			}
			if(reagent_json_map == null)
				reagent_json_map = new HashMap<>();
			//获取提取试剂信息
			tqsjList = getTqsjList(JcdwDto.getCsdm(),reagent_json_map);
			map.put("tqsjList",tqsjList);
		}
		return map;
	}
	@RequestMapping("/potency/pagedataGetZdpbTqList")
	@ResponseBody
	public Map<String,Object>  pagedataGetZdpbTqList(TqmxDto tqmxDto){
		Map<String,Object> map=new HashMap<>();
		List<TqmxDto>tqmxDtoList=tqmxServcie.getInfoByZdpb(tqmxDto);
		List<TqmxDto> tqmxDtos=new ArrayList<>();
		String jcdwCsdm="";
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		List<JcsjDto> jcdwList=jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for(JcsjDto jcsjDto1:jcdwList){
			if(jcsjDto1.getCsid().equals(tqmxDto.getJcdw())){
				jcdwCsdm=jcsjDto1.getCsdm();
				break;
			}
		}
		Map<String, List<TqmxDto>> listMap = tqmxDtoList.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getKzcs3()).orElse("")));
		XtszDto JcdwxtszDto = xtszService.getDtoById("inspect.tq.orderby."+jcdwCsdm);
		String pxStr="";
		if(JcdwxtszDto==null){
			JcdwxtszDto=xtszService.getDtoById("inspect.tq.orderby");
		}
		pxStr=JcdwxtszDto.getSzz();
		String[] pxArr=pxStr.split(",");
		for(String px:pxArr){
			if(listMap.get(px)!=null){
				tqmxDtos.addAll(listMap.get(px));
				listMap.remove(px);
			}
		}
		Set<String> keySet=listMap.keySet();
		for(String key:keySet){
			tqmxDtos.addAll(listMap.get(key));
		}
		map.put("tqglDtoList", tqmxDtos);
		return map;
	}
	/**
	 * 跳转添加孔位界面
	 */
	@RequestMapping("/potency/pagedataAddPosition")
	public ModelAndView pagedataAddPosition(TqmxDto tqmxDto,String text,String firstlie) {
		ModelAndView mav = new ModelAndView("experiment/potency/potency_addPosition");
		int size = tqmxDto.getPageSize();
		int number = tqmxDto.getPageNumber();
		List<List<String>> list = new ArrayList<>();
		int count = 1;
		for (int i = 1; i <= size; i++) {
			List<String> stringList = new ArrayList<>();
			for (int j = 1; j <= number; j++) {
				if (count < 10)
					stringList.add("0"+count);
				else
					stringList.add(""+count);
				count ++;
			}
			list.add(stringList);
		}
		mav.addObject("list",list);
		mav.addObject("text",text);
		mav.addObject("firstlie",firstlie);
		mav.addObject("size",number);
		return mav;
	}

	/**
	 * 跳转添加spike界面
	 */
	@RequestMapping("/potency/pagedataAddSpike")
	public ModelAndView pagedataAddSpike(TqmxDto tqmxDto,String text,String firstlie) {
		ModelAndView mav = new ModelAndView("experiment/potency/potency_addSpike");
		int size = tqmxDto.getPageSize();
		int number = tqmxDto.getPageNumber();
		List<List<String>> list = new ArrayList<>();
		int count = 1;
		for (int i = 1; i <= size; i++) {
			List<String> stringList = new ArrayList<>();
			for (int j = 1; j <= number; j++) {
				if (count < 10)
					stringList.add("0"+count);
				else
					stringList.add(""+count);
				count ++;
			}
			list.add(stringList);
		}
		mav.addObject("list",list);
		mav.addObject("text",text);
		mav.addObject("firstlie",firstlie);
		mav.addObject("size",number);
		return mav;
	}

	/**
	 * 鼠标移除获取内部提取代码信息
	 */
	@RequestMapping("/potency/pagedataGetTqdm")
	@ResponseBody
	public Map<String,Object> pagedataGetTqdm(TqmxDto tqmxDto) {
		Map<String, Object> map = new HashMap<>();
		map.put("status",false);
		//所有的-后的信息
		StringBuffer sb = new StringBuffer("");
		//所有 -- 后的信息
		StringBuffer dyhz = new StringBuffer("");
		//扩展的内部编码，包括内部编码和所有的- 后的后缀，但去除
		String kz_nbbm = "";
		
		if (StringUtil.isNotBlank(tqmxDto.getNbbh())){
			if (tqmxDto.getNbbh().startsWith("NC-") || tqmxDto.getNbbh().startsWith("PC-") || tqmxDto.getNbbh().startsWith("DC-")){
				if (tqmxDto.getNbbh().contains("--")){
					tqmxDto.setNbbh(tqmxDto.getNbbh().split("--")[0]);
				}
				kz_nbbm = tqmxDto.getNbbh().split("--")[0];
				tqmxDto.setJcdw(null);
			}else{
				String[] split = tqmxDto.getNbbh().split("--");
				if(split.length>1) {
					for(int i=1;i<split.length;i++) {
						String[] l_sp = split[i].split("-");
						if(l_sp.length > 1) {
							for(int j=1;j<l_sp.length;j++) {
								sb.append("-" + l_sp[j]);
							}
						}
						dyhz.append(l_sp[0]);
					}
				}
				String bhhz = split[0]+sb.toString();
				String[] strings = bhhz.split("-");
				kz_nbbm = bhhz.substring(0,bhhz.length() - strings[strings.length-1].length());
				if (strings[0].length()>6){
					tqmxDto.setKzcs3(strings.length>1?strings[strings.length-1]:null);
					tqmxDto.setNbbh(strings[0]);
				}else{
					if (strings.length>1){
						String nbbm = strings[0]+'-'+strings[1];
						tqmxDto.setKzcs3(strings.length>2?strings[strings.length-1]:null);
						tqmxDto.setNbbh(nbbm);
					}else{
						tqmxDto.setNbbh(strings[0]);
					}
				}
				map.put("dyhz",dyhz.toString());
			}
			List<TqmxDto> dtos = tqmxService.getInfoByNbbm(tqmxDto);
			//为了兼容以前的代码，需要重新设置nbbm
			tqmxDto.setNbbh(kz_nbbm);
			map.put("list",dtos);
		}
		return  map;
	}

	/**
	 * 鼠标移除获取内部文库送检实验管理id
	 */
	@RequestMapping("/potency/pagedataGetSjsygl")
	@ResponseBody
	public Map<String,Object> pagedataGetSjsygl(TqmxDto tqmxDto) {
		return pagedataGetTqdm(tqmxDto);
	}
	/**
	 * 新增提交保存
	 */
	@RequestMapping("/potency/addSavePotency")
	@ResponseBody
	public Map<String,Object> addSavePotency(TqmxDto tqmxDto, HttpServletRequest request) throws BusinessException {
		User user = getLoginInfo();
		//删除临时保存的数据
		if(StringUtils.isNotBlank(tqmxDto.getTqid())) {
			tqglService.deleteLs(tqmxDto.getTqid());
			tqmxService.delTqmxDto(tqmxDto);
		}

		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode(),
				PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		//获取个人设置里的实验室 提取/文库试剂的设置信息
		GrszDto grszxx_reagent_json= grszDtoMap.get(PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode());
		Map<String,String> reagent_json_map = new HashMap<>();
		if(grszxx_reagent_json!=null && StringUtil.isNotBlank(grszxx_reagent_json.getSzz())) {
			reagent_json_map=com.alibaba.fastjson.JSONObject.parseObject(grszxx_reagent_json.getSzz(),Map.class);
			if(reagent_json_map==null ){
				reagent_json_map = new HashMap<>();
			}
		}

		String jcdw=tqmxDto.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
		Map<String,String> tqsjMap=new HashMap<>();
		List<Map<String,String>> sysjglList = new ArrayList<>();
		if(StringUtil.isBlank(tqmxDto.getTqid())){
			tqmxDto.setTqid(StringUtil.generateUUID());
		}
		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//提取文库试剂
			XtszDto tqsjtszDto = xtszService.getDtoById("reagent.extend.extract." +JcdwDto.getCsdm());
			if(tqsjtszDto==null || StringUtil.isBlank(tqsjtszDto.getSzz())){
				tqsjtszDto = xtszService.getDtoById("reagent.extend.extract");
			}
			if(tqsjtszDto==null){
				tqsjtszDto=new XtszDto();
			}
			JSONArray jsonArray_tq=JSONArray.parseArray(tqsjtszDto.getSzz());
			for(int i = 0; i < jsonArray_tq.size(); i++){
				com.alibaba.fastjson.JSONObject jsonObject=jsonArray_tq.getJSONObject(i);
				if(StringUtil.isNotBlank(request.getParameter(jsonObject.getString("variable")))){
					tqsjMap.put(jsonObject.getString("variable"),request.getParameter(jsonObject.getString("variable")));
					Map<String,String> sysjglMap = new HashMap<>();
					sysjglMap.put("sjmc",jsonObject.getString("title"));
					sysjglMap.put("sjph",request.getParameter(jsonObject.getString("variable")));
					sysjglMap.put("sjbm",jsonObject.getString("variable"));
					sysjglMap.put("ywid",tqmxDto.getTqid());
					sysjglMap.put("lrry",user.getYhid());
					sysjglMap.put("jcdw",tqmxDto.getJcdw());
					sysjglMap.put("sjrq",tqmxDto.getTqrq());
					//把页面的试剂信息保存到个人设置里
					reagent_json_map.put(jsonObject.getString("variable"), request.getParameter(jsonObject.getString("variable")));
					sysjglList.add(sysjglMap);
				}
			}
		}
		boolean isSuccess;
		Map<String, Object> map = new HashMap<>();
		//校验名称是否重复
		TqglDto tqglDto=new TqglDto();
		tqglDto.setMc(tqmxDto.getMc());
		List<TqglDto> tqglxx=tqglService.getTqglByMc(tqglDto);
		if(tqglxx!=null && tqglxx.size()>0) {
			map.put("status","fail");
			map.put("message","名称已存在!");
			return map;
		}
		//核酸提取试剂盒个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_SETTINGS.getCode(),tqmxDto.getSjph());
		//自动排版个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE.getCode(),tqmxDto.getZdpb());
		//自动排版个人设置包含
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode(),tqmxDto.getZdpbin());
		//自动排版个人设置不包含
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode(),tqmxDto.getZdpbnotin());
		//提取其他试剂个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode(), com.alibaba.fastjson.JSONObject.toJSONString(reagent_json_map));

		tqmxDto.setLrry(user.getYhid());
		tqmxDto.setYhmc(user.getYhm());
		List<String> notexitnbbhs=tqmxService.exitnbbh(tqmxDto);
		if(notexitnbbhs==null || notexitnbbhs.size()<1 || ("1").equals(tqmxDto.getSfbc())) {
			tqmxDto.setTqsjStr(JSON.toJSONString(tqsjMap));
			tqmxDto.setSysjStr(JSON.toJSONString(sysjglList));
			isSuccess=tqmxService.addSaveTqxx(tqmxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}else {
			map.put("status", "caution");
			map.put("message", "保存失败!内部编号:"+String.join(",",notexitnbbhs)+"不存在!是否继续保存!");
			map.put("notexitnbbhs",notexitnbbhs);
			return map;
		}
	}
	/**
	 * 跳转修改界面
	 */
	@RequestMapping("/potency/modPotency")
	public ModelAndView modPotencyPage(TqglDto tqglDto) {
		ModelAndView mav=new ModelAndView("experiment/potency/potency_add");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		TqglDto t_tqglDto=tqglService.getDtoById(tqglDto.getTqid());
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlb(PersonalSettingEnum.REAGENT_SETTINGS.getCode());
		GrszDto grszxx_o = grszService.selectGrszDtoByYhidAndSzlb(grszDto);
		if(StringUtil.isBlank(t_tqglDto.getSjph()) && grszxx_o!=null) {
			t_tqglDto.setSjph(grszxx_o.getSzz());
			mav.addObject("sjxsbj", "1");
		}else{
			mav.addObject("sjxsbj", "0");
		}
		t_tqglDto.setFormAction("modSavePotency");
		XtszDto xtszDto = xtszService.getDtoById(GlobalString.MATRIDX_EXTRACT_NBBMEXTED);
		t_tqglDto.setHzqx(xtszDto.getSzz());
		mav.addObject("TqglDto", t_tqglDto);
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("xhList", new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
		String jcdw=t_tqglDto.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
		List<Map<String,String>> tqsjList=new ArrayList<>();
		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//提取文库试剂
			String tqsj=t_tqglDto.getTqsj();
			Map<String,String> tqsjMap = new HashMap<>();
			if(StringUtil.isNotBlank(tqsj)){
				tqsjMap=JSONObject.parseObject(tqsj, Map.class);
			}
			//获取提取试剂信息
			tqsjList = getTqsjList(JcdwDto.getCsdm(),tqsjMap);
		}
		mav.addObject("tqsjList",tqsjList);
		return mav;
	}
	
	/**
	 * 获取修改界面内容返回给JS处理
	 */
	@RequestMapping("/potency/pagedataXgxx")
	@ResponseBody
	public List<TqmxDto> getTqXgxx(TqmxDto tqmxDto){
		List<TqmxDto> tqmxlist=tqmxService.getTqmxByTqid(tqmxDto);
		if(tqmxlist.size()==0) {
			TqglDto tqglDto=tqglService.getDtoById(tqmxDto.getTqid());
			TqmxDto t_tqmxDto=new TqmxDto();
			t_tqmxDto.setTqid(tqglDto.getTqid());
			t_tqmxDto.setMc(tqglDto.getMc());
			t_tqmxDto.setJcdw(tqglDto.getJcdw());
			t_tqmxDto.setTqrq(tqglDto.getTqrq());
			tqmxlist.add(t_tqmxDto);
		}
		return tqmxlist;
	}
	
	/**
	 * 修改提交保存
	 */
	@RequestMapping("/potency/modSavePotency")
	@ResponseBody
	public Map<String,Object> updateTqxx(TqmxDto tqmxDto,HttpServletRequest request) {
		boolean isSuccess;
		Map<String, Object> map = new HashMap<>();
		try {
			User user=getLoginInfo();
			GrszDto grszDto = new GrszDto();
			grszDto.setYhid(user.getYhid());
			grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode(),
					PersonalSettingEnum.LIBRARY_SET_TYPE.getCode(),
					PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode(),
					PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode(),
					PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
			Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
			//获取个人设置里的实验室 提取/文库试剂的设置信息
			GrszDto grszxx_reagent_json= grszDtoMap.get(PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode());
			Map<String,String> reagent_json_map = new HashMap<>();
			if(grszxx_reagent_json!=null && StringUtil.isNotBlank(grszxx_reagent_json.getSzz())) {
				reagent_json_map=com.alibaba.fastjson.JSONObject.parseObject(grszxx_reagent_json.getSzz(),Map.class);
				if(reagent_json_map==null ){
					reagent_json_map = new HashMap<>();
				}
			}

			//校验名称是否重复
			TqglDto tqglDto=new TqglDto();
			tqglDto.setMc(tqmxDto.getMc());
			tqglDto.setTqid(tqmxDto.getTqid());
			List<TqglDto> tqglxx=tqglService.getTqglByMc(tqglDto);
			if(tqglxx!=null && tqglxx.size()>0) {
				map.put("status","fail");
				map.put("message","名称已存在!");
				return map;
			}

			String jcdw=tqmxDto.getJcdw();
			List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
			Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
			Map<String,String> tqsjMap=new HashMap<>();
			List<Map<String,String>> sysjglList = new ArrayList<>();
			if(optional.isPresent()){
				JcsjDto JcdwDto=optional.get();
				//提取文库试剂
				XtszDto tqsjtszDto = xtszService.getDtoById("reagent.extend.extract." +JcdwDto.getCsdm());
				if(tqsjtszDto==null || StringUtil.isBlank(tqsjtszDto.getSzz())){
					tqsjtszDto = xtszService.getDtoById("reagent.extend.extract");
				}
				if(tqsjtszDto==null){
					tqsjtszDto=new XtszDto();
				}
				JSONArray jsonArray_tq=JSONArray.parseArray(tqsjtszDto.getSzz());
				for(int i = 0; i < jsonArray_tq.size(); i++){
					com.alibaba.fastjson.JSONObject jsonObject=jsonArray_tq.getJSONObject(i);
					if(StringUtil.isNotBlank(request.getParameter(jsonObject.getString("variable")))){
						tqsjMap.put(jsonObject.getString("variable"),request.getParameter(jsonObject.getString("variable")));
						Map<String,String> sysjglMap = new HashMap<>();
						sysjglMap.put("sjmc",jsonObject.getString("title"));
						sysjglMap.put("sjph",request.getParameter(jsonObject.getString("variable")));
						sysjglMap.put("sjbm",jsonObject.getString("variable"));
						sysjglMap.put("ywid",tqmxDto.getTqid());
						sysjglMap.put("lrry",user.getYhid());
						sysjglMap.put("jcdw",tqmxDto.getJcdw());
						sysjglMap.put("sjrq",tqmxDto.getTqrq());
						//把页面的试剂信息保存到个人设置里
						reagent_json_map.put(jsonObject.getString("variable"), request.getParameter(jsonObject.getString("variable")));
						sysjglList.add(sysjglMap);
					}
				}
			}
			//核酸提取试剂盒个人设置
			resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_SETTINGS.getCode(),tqmxDto.getSjph());
			//自动排版个人设置
			resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE.getCode(),tqmxDto.getZdpb());
			//自动排版个人设置包含
			resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_IN.getCode(),tqmxDto.getZdpbin());
			//自动排版个人设置不包含
			resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN.getCode(),tqmxDto.getZdpbnotin());
			//提取其他试剂个人设置
			resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode(), com.alibaba.fastjson.JSONObject.toJSONString(reagent_json_map));

			tqmxDto.setXgry(user.getYhid());
			tqmxDto.setYhmc(user.getYhm());
			List<String> notexitnbbhs=tqmxService.exitnbbh(tqmxDto);
			if(notexitnbbhs==null || notexitnbbhs.size()<1 || ("1").equals(tqmxDto.getSfbc())) {
				tqmxDto.setTqsjStr(JSON.toJSONString(tqsjMap));
				if(!CollectionUtils.isEmpty(sysjglList)){
					tqmxDto.setSysjStr(JSON.toJSONString(sysjglList));
				}
				isSuccess=tqmxService.updateTqmx(tqmxDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
				return map;
			}else {
				map.put("status", "caution");
				map.put("message", "保存失败!内部编号:"+String.join(",",notexitnbbhs)+"不存在!是否继续保存!");
				map.put("notexitnbbhs",notexitnbbhs);
				return map;
			}
		}catch (BusinessException e){
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}

	}

	/**
	 * 更新用户的个人设置信息
	 * @param grszDtoMap
	 * @param yhid
	 * @param szlb
	 * @param value
	 * @return
	 */
	private void resetPersonalsettingsInfo(Map<String, GrszDto> grszDtoMap,String yhid,String szlb,String value){
		GrszDto	grszDto = new GrszDto();
		grszDto.setSzlb(szlb);
		grszDto.setYhid(yhid);
		GrszDto grszxx = grszDtoMap.get(szlb);
		if (grszxx == null) {
			grszDto.setSzz(value);
			grszDto.setLrry(yhid);
			grszService.insertDto(grszDto);
		} else {
			if (grszxx.getSzz()==null || !grszxx.getSzz().equals(value)) {
				grszDto.setSzz(value);
				grszDto.setSzzisnull("");
				if(StringUtil.isBlank(value)){
					grszDto.setSzzisnull("1");
				}
				grszService.updateByYhidAndSzlb(grszDto);
			}
		}
	}
	
	/**
	 * 跳转浓度查看界面
	 */
	@RequestMapping("/potency/viewPotency")
	public ModelAndView getViewPotencyPage(TqmxDto tqmxDto) {
		ModelAndView mav=new ModelAndView("experiment/potency/potency_view");
		TqglDto tqglDto=tqglService.getDtoById(tqmxDto.getTqid());
		mav.addObject("tqglDto", tqglDto);
		mav.addObject("TqmxDto", tqmxDto);
		String jcdw=tqglDto.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//提取/文库试剂
			String tqsj=tqglDto.getTqsj();
			Map<String,String> tqsjMap = new HashMap<>();
			if(StringUtil.isNotBlank(tqsj)){
				tqsjMap=JSONObject.parseObject(tqsj, Map.class);
			}
			//获取提取试剂信息
			List<Map<String,String>> tqsjList = getTqsjList(JcdwDto.getCsdm(),tqsjMap);
			if(tqsjList==null)
				tqsjList= new ArrayList<>();
			mav.addObject("tqsjList", tqsjList);
		}
		return mav;
	}
	/**
	 * 跳转浓度打印界面
	 */
	@RequestMapping("/potency/pagedataPrintPotency")
	public ModelAndView pagedataPrintPotency(TqmxDto tqmxDto) {
		ModelAndView mav=new ModelAndView("experiment/potency/potency_print_view");
		TqglDto tqglDto=tqglService.getDtoById(tqmxDto.getTqid());
		mav.addObject("tqglDto", tqglDto);
		mav.addObject("tqmxList", tqmxService.getTqmxByTqid(tqmxDto));
		return mav;
	}

	/**
	 * 查看信息交给JS处理
	 */
	@RequestMapping("/potency/pagedataView")
	@ResponseBody
	public List<TqmxDto> getTqmxView(TqmxDto tqmxDto){
		return tqmxService.getTqmxByTqid(tqmxDto);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/potency/delPotency")
	@ResponseBody
	public Map<String,Object> delTqxx(TqglDto tqglDto) {
		User user=getLoginInfo();
		tqglDto.setScry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=tqglService.deleteTqxx(tqglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 根据送检id获取提取信息（给送检报告获取提取信息用）
	 */
	@RequestMapping(value="/potency/commGetTqxxBySjid")
	@ResponseBody
	public List<TqmxDto> getTqxxBySjid(String sjid){
		return tqmxService.getTqxxBySjid(sjid);
	}
	
	/**
	 * 检查两次输入的编号是否一致
	 */
	@RequestMapping(value="/potency/examineNum")
	public ModelAndView examineNum() {
		return new ModelAndView("experiment/potency/potency_examine");
	}
	
	/**
	 * 扫描条码，并同时打印出一张相同的条码
	 */
	@RequestMapping(value="/potency/printNbbm")
	public ModelAndView printNbbm(){
		return new ModelAndView("experiment/potency/potency_print");
	}
	
	/**
	 * 提取管理表临时保存(跳过所有校验只保存页面数据)
	 */
	@RequestMapping(value="/potency/pagedataTqglLsSave")
	@ResponseBody
	public Map<String,Object> pagedataTqglLsSave(TqglDto tqglDto) {
		User user=getLoginInfo();
		Map<String,Object> map= new HashMap<>();
		tqglDto.setZt(StatusEnum.CHECK_NO.getCode());
		tqglDto.setLrry(user.getYhid());
		boolean tqglresult;
		if(StringUtils.isNotBlank(tqglDto.getTqid())) {
			tqglresult=tqglService.updateTqgl(tqglDto);
		}else {
				tqglresult=tqglService.insertDto(tqglDto);
			}
		map.put("tqglDto", tqglDto);
		map.put("status", tqglresult);
		return map;
	}
	
	/**
	 * 提取明细表临时保存(跳过所有校验只保存页面数据)
	 */
	@RequestMapping(value="/potency/pagedataTqmxLsSave")
	@ResponseBody
	public Map<String,Object> tqmxLsSave(TqmxDto tqmxDto) {
		Map<String,Object> map= new HashMap<>();
		boolean result;
		TqmxDto tqmxDto_t=tqmxService.getDtoByIdAndXh(tqmxDto);
		if(tqmxDto_t==null) {
			tqmxDto.setTqmxid(StringUtil.generateUUID());
			result=tqmxServcie.temporarySave(tqmxDto);
		}else {
			try {
				if(StringUtil.isNotBlank(tqmxDto.getHsnd()))
					new BigDecimal(tqmxDto.getHsnd());
			}catch (Exception e){
				tqmxDto.setHsnd(null);
			}
			try {
				if(StringUtil.isNotBlank(tqmxDto.getCdna()))
					new BigDecimal(tqmxDto.getCdna());
			}catch (Exception e){
				tqmxDto.setCdna(null);
			}
			result=tqmxServcie.updateByIdAndXh(tqmxDto);
		}
		map.put("status", result);
		map.put("tqmxid", tqmxDto.getTqmxid());
		return map;
	}
	
	/**
	 * 根据内部编号获取检测项目信息
	 */
	@RequestMapping("/potency/pagedataSjjcxm")
	@ResponseBody
	public Map<String,Object> getSjjcxm(TqmxDto tqmxDto){
		Map<String,Object> map= new HashMap<>();
		TqmxDto t_tqmxDto=tqmxService.getSjDtoByNbbh(tqmxDto);
		map.put("tqmxDto",t_tqmxDto);
		return map;
	}
	
	/**
	 * 检测文库明细数据中此内部编号是否已经存在DNA检测
	 */
	@RequestMapping("/potency/pagedataCheckRNA")
	@ResponseBody
	public boolean checkRNA(TqmxDto tqmxDto) {
		List<TqmxDto> tqmxlist=tqmxService.gettqmxByNbbh(tqmxDto);
		return tqmxlist != null && tqmxlist.size() > 0;
	}


	/**
	 * 导出
	 */
	@RequestMapping("/potency/exportPotency")
	public ModelAndView exportPotency(TqmxDto tqmxDto){
		ModelAndView mav=new ModelAndView("experiment/potency/potency_export");
		List<JcsjDto> wjbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DOCUMENTNUM.getCode());
		for (JcsjDto jcsjDto:wjbmlist){
			if (jcsjDto.getCsdm().equals("TQ")){
				mav.addObject("wjbh", jcsjDto);
			}
		}
		List<TqmxDto> tqmxDtos=new ArrayList<>();
		List<List<TqmxDto>> newList=new ArrayList<>();
		List<List<List<TqmxDto>>> groupList=new ArrayList<>();
		List<TqmxDto> tqmxlist=tqmxService.getTqmxByTqidByPrint(tqmxDto);
		if(tqmxlist!=null&&tqmxlist.size()>0){
			for(int i=0;i<100; i++){
				boolean flag=false;
				TqmxDto tqmxDto_t=new TqmxDto();
				for(TqmxDto dto:tqmxlist){
					int xh=Integer.parseInt(dto.getXh());
					if(xh==(i+1)){
						flag=true;
						tqmxDto_t=dto;
						break;
					}
				}
				if(flag){
					tqmxDtos.add(tqmxDto_t);
				}else{
					tqmxDto_t.setXh(String.valueOf(i+1));
					tqmxDtos.add(tqmxDto_t);
				}
				if((i+1)%10==0){
					if((i+1)%50==0){
						newList.add(tqmxDtos);
						tqmxDtos=new ArrayList<>();
						groupList.add(newList);
						newList=new ArrayList<>();
					}else{
						newList.add(tqmxDtos);
						tqmxDtos=new ArrayList<>();
					}
				}
			}
		}
		String groupStr = JSONObject.toJSONString(groupList);
		mav.addObject("groupList", groupStr);
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		String initDate=date.split("-")[0]+"年"+date.split("-")[1]+"月"+date.split("-")[2]+"日";
		mav.addObject("date", initDate);
		TqglDto tqglDto = tqglService.getCzrAndShrPic(tqmxDto);
		if(tqglDto!=null) {
			if(StringUtils.isNotBlank(tqglDto.getCzr())){
				mav.addObject("czrPic", tqglDto.getCzr());
			}
			if(StringUtils.isNotBlank(tqglDto.getHdr())){
				mav.addObject("hdrPic", tqglDto.getHdr());
			}
		}
		return mav;
	}

	/**
	 * 实验列表提取界面
	 */
	@RequestMapping("/potency/extractExperiment")
	@ResponseBody
	public ModelAndView extractExperiment(TqglDto tqglDto) {
		ModelAndView mav=new ModelAndView("experiment/potency/potency_add");
		//查询基础数据的检测单位
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		//查询个人设置的检测单位
		GrszDto grszDto=new GrszDto();
		User user=getLoginInfo();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),
				PersonalSettingEnum.REAGENT_SETTINGS.getCode()
				,PersonalSettingEnum.OPERATOR.getCode(),
				PersonalSettingEnum.COLLATOR.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszDtoCzr = grszDtoMap.get(PersonalSettingEnum.OPERATOR.getCode());
		GrszDto grszDtoHdr = grszDtoMap.get(PersonalSettingEnum.COLLATOR.getCode());
		if(grszDtoCzr!=null) {
			if(StringUtils.isNotBlank(grszDtoCzr.getSzz())) {
				tqglDto.setCzr(grszDtoCzr.getSzz());
			}
			if(StringUtils.isNotBlank(grszDtoCzr.getGlxx())){
				tqglDto.setCzrmc(grszDtoCzr.getGlxx());
			}
		}
		if(grszDtoHdr!=null) {
			if(StringUtils.isNotBlank(grszDtoHdr.getSzz())) {
				tqglDto.setHdr(grszDtoHdr.getSzz());
			}
			if(StringUtils.isNotBlank(grszDtoHdr.getGlxx())){
				tqglDto.setHdrmc(grszDtoHdr.getGlxx());
			}
		}
		GrszDto grszDto_t = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
		if(grszDto_t==null) {
			grszDto_t=new GrszDto();
		}
		//核酸提取试剂盒个人设置
		GrszDto grszxx_l = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode());
		if(grszxx_l!=null) {
			tqglDto.setSjph(grszxx_l.getSzz());
		}
		JcsjDto jcsjDto=new JcsjDto();
		if(StringUtils.isNotBlank(grszDto_t.getSzz()))
			jcsjDto=jcsjService.getDtoById(grszDto_t.getSzz());
		tqglDto.setFormAction("extractSaveExperiment");
		XtszDto xtszDto = xtszService.getDtoById(GlobalString.MATRIDX_EXTRACT_NBBMEXTED);
		if(xtszDto!=null) {
			tqglDto.setHzqx(xtszDto.getSzz());
		}
		TqmxDto tqmxDto=new TqmxDto();
		tqmxDto.setIds(tqglDto.getIds());
		List<TqmxDto> infoBySyglids = tqmxService.getInfoBySyglids(tqmxDto);
		tqglDto.setTqmx_json(JSON.toJSONString(infoBySyglids));
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("mrjcdw", jcsjDto);
		mav.addObject("TqglDto", tqglDto);
		mav.addObject("sjxsbj", "1");
		return mav;
	}
	/**
	 * 实验列表提取保存
	 */
	@RequestMapping("/potency/extractSaveExperiment")
	@ResponseBody
	public Map<String,Object> extractSaveExperiment(TqmxDto tqmxDto,HttpServletRequest request) throws BusinessException {
		return this.addSavePotency(tqmxDto,request);
	}
}
