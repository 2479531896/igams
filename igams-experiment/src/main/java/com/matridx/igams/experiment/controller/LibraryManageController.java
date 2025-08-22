package com.matridx.igams.experiment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.CxyxxDto;
import com.matridx.igams.experiment.dao.entities.NgsglDto;
import com.matridx.igams.experiment.dao.entities.TqmxDto;
import com.matridx.igams.experiment.dao.entities.WkglDto;
import com.matridx.igams.experiment.dao.entities.WkmxDto;
import com.matridx.igams.experiment.dao.entities.WksjglDto;
import com.matridx.igams.experiment.dao.entities.WksjmxDto;
import com.matridx.igams.experiment.service.svcinterface.ICxyxxService;
import com.matridx.igams.experiment.service.svcinterface.INgsglService;
import com.matridx.igams.experiment.service.svcinterface.ITqglService;
import com.matridx.igams.experiment.service.svcinterface.ITqmxService;
import com.matridx.igams.experiment.service.svcinterface.IWkglService;
import com.matridx.igams.experiment.service.svcinterface.IWkmxService;
import com.matridx.igams.experiment.service.svcinterface.IWksjglService;
import com.matridx.igams.experiment.service.svcinterface.IWksjmxService;
import com.matridx.igams.experiment.util.PoolingUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.GenerateFileForPCR;
import com.matridx.springboot.util.encrypt.Plate;
import com.matridx.springboot.util.file.ftp.FtpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.net.ftp.FTPClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/experiment")
public class LibraryManageController extends BaseController {

	@Autowired
	IWkglService wkglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IWkmxService wkmxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IGrszService grszService;
	@Autowired
	ITqglService tqglService;
	@Autowired
	INgsglService ngsglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IWksjglService wksjglService;
	@Autowired
	IWksjmxService wksjmxService;
	@Autowired
	IJgxxService jgxxService;
	@Autowired
	ICxyxxService cxyxxService;
	@Autowired
	ITqmxService tqmxService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ICommonService commonService;
	@Value("${matridx.fileupload.releasePath:}")
	private String exportFilePath;
	@Value("${matridx.ftpPcr.addr:}")
	private String addr;
	@Value("${matridx.ftpPcr.port:}")
	private Integer pcrport;
	@Value("${matridx.ftpPcr.username:}")
	private String username;
	@Value("${matridx.ftpPcr.password:}")
	private String password;

	private final Logger log = LoggerFactory.getLogger(LibraryManageController.class);
	/**
	 * 跳转文库列表界面
	 */
	@RequestMapping(value = "/library/pageListLibrary")
	@ResponseBody
	public ModelAndView getLibraryPage() {
		return new ModelAndView("experiment/library/library_list");
	}

	/**
	 * 获取文库列表
	 */
	@RequestMapping("/library/pageGetListLibrary")
	@ResponseBody
	public Map<String, Object> getLibraryPageList(WkglDto wkglDto) {
		User user = getLoginInfo();
		List<WkglDto> wglist;
		List<Map<String, String>> jcdwList = tqglService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null && jcdwList.size() > 0) {
			if ("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList = new ArrayList<>();
				for (Map<String, String> stringStringMap : jcdwList) {
					if (stringStringMap.get("jcdw") != null) {
						strList.add(stringStringMap.get("jcdw"));
					}
				}
				if (strList.size() > 0) {
					wkglDto.setJcdwxz(strList);
					wglist = wkglService.getPagedWkglDtoList(wkglDto);
				} else {
					wglist = new ArrayList<>();
				}
			} else {
				wglist = wkglService.getPagedWkglDtoList(wkglDto);
			}
		}else {
			wglist = wkglService.getPagedWkglDtoList(wkglDto);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("total", wkglDto.getTotalNumber());
		map.put("rows", wglist);
		return map;
	}

	/**
	 * 跳转新增界面
	 */
	@RequestMapping("/library/addLibrary")
	@ResponseBody
	public ModelAndView getAddLibraryPage(WkmxDto wkmxDto) {

		ModelAndView mav = new ModelAndView("experiment/library/library_add");

		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),
				PersonalSettingEnum.SEQUENCER_CODE.getCode(),
				PersonalSettingEnum.REAGENT_SELECT.getCode(),
				PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_WK.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_IN_WK.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN_WK.getCode(),
				PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		//查个人设置检查单位
		GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
		GrszDto grszxx_machine = grszDtoMap.get(PersonalSettingEnum.SEQUENCER_CODE.getCode());
		GrszDto grszxx_reagent = grszDtoMap.get(PersonalSettingEnum.REAGENT_SELECT.getCode());

		WkglDto page_wkglDto = new WkglDto();
		//宏基因组DNA建库试剂盒个人设置
		GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
		if(grszxx_o!=null) {
			page_wkglDto.setSjph1(grszxx_o.getSzz());
		}
		//逆转录试剂盒个人设置
		GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
		if(grszxx_t!=null) {
			page_wkglDto.setSjph2(grszxx_t.getSzz());
		}
		//定量试剂 个人设置
		GrszDto grszxx_dlsj = grszDtoMap.get(PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode());
		mav.addObject( "xsbj", "1");
		if(grszxx_dlsj!=null)
			page_wkglDto.setSjph3(grszxx_dlsj.getSzz());
		//自动排版个人设置
		GrszDto grszxx_lib = grszDtoMap.get(PersonalSettingEnum.LIBRARY_SET_TYPE_WK.getCode());
		if(grszxx_lib!=null) {
			mav.addObject("zdpb", grszxx_lib.getSzz());
			if("1".equals(grszxx_lib.getSzz())){
				getZdpbList(user,grszxx,mav);
			}
		}else{
			mav.addObject("zdpb", "0");
			getZdpbList(user,grszxx,mav);
		}
		//自动排版个人设置包含
		GrszDto grszxx_lib_in = grszDtoMap.get(PersonalSettingEnum.LIBRARY_SET_TYPE_IN_WK.getCode());
		if(grszxx_lib_in!=null) {
			mav.addObject("grszxx_lib_in", grszxx_lib_in.getSzz());
		}else{
			mav.addObject("grszxx_lib_in", "");
		}
		//自动排版个人设置不包含
		GrszDto grszxx_lib_notin = grszDtoMap.get(PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN_WK.getCode());
		if(grszxx_lib_notin!=null) {
			mav.addObject("grszxx_lib_notin", grszxx_lib_notin.getSzz());
		}else{
			mav.addObject("grszxx_lib_notin", "");
		}
		//文库其他试剂个人设置
		GrszDto grszxx_reagent_json = grszDtoMap.get(PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode());
		Map<String,String> reagent_json_map = null;
		if(grszxx_reagent_json!=null) {
			String reagent_json = grszxx_reagent_json.getSzz();
			if(StringUtil.isNotBlank(reagent_json))
				reagent_json_map = com.alibaba.fastjson.JSONObject.parseObject(reagent_json, HashMap.class);
		}
		if(reagent_json_map == null)
			reagent_json_map = new HashMap<>();

		WkglDto wkglDto = new WkglDto();
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		String t_date = date.replaceAll("-", "");
		wkglDto.setLrsj(date);
		wkglDto = wkglService.getCountByDay(wkglDto);
		String wkmc;
		if (Integer.parseInt(wkglDto.getCount()) < 9 && Integer.parseInt(wkglDto.getCount()) >= 0) {
			wkmc = t_date + "-0" + (Integer.parseInt(wkglDto.getCount()) + 1);
		} else {
			wkmc = t_date + "-" + (Integer.parseInt(wkglDto.getCount()) + 1);
		}
		JcsjDto jcsjDto = new JcsjDto();

		XtszDto wksjtszDto = null;
		if (grszxx != null) {
			jcsjDto = jcsjService.getDtoById(grszxx.getSzz());
			if (jcsjDto != null) {
				wkmc = jcsjDto.getCsdm() + "-" + wkmc;
			} else {
				jcsjDto = new JcsjDto();
			}
		}
		List<Map<String, String>> wksjList = getWksjList(jcsjDto.getCsdm(),reagent_json_map);
		mav.addObject("wksjList", wksjList);

		page_wkglDto.setWkmc(wkmc);
		JcsjDto jcsjDto_machine = null;
		if (grszxx_machine != null && StringUtil.isNotBlank(grszxx_machine.getSzz())) {
			jcsjDto_machine = jcsjService.getDtoById(grszxx_machine.getSzz());
		}
		if(jcsjDto_machine== null)
			jcsjDto_machine = new JcsjDto();
		JcsjDto jcsjdto_reagent = null;
		if (grszxx_reagent != null&& StringUtil.isNotBlank(grszxx_reagent.getSzz())) {
			jcsjdto_reagent = jcsjService.getDtoById(grszxx_reagent.getSzz());
		}
		if(jcsjdto_reagent == null)
			jcsjdto_reagent = new JcsjDto();
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECTION_UNIT,
						BasicDataTypeEnum.SEQUENCER_CODE,
						BasicDataTypeEnum.REAGENT_SELECT });
		mav.addObject("jcdwList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));// 检测单位
		mav.addObject("yqlxList", jclist.get(BasicDataTypeEnum.SEQUENCER_CODE.getCode()));// 测序仪
		mav.addObject("sjxzList", jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode()));// 试剂选择
		mav.addObject("sjxzJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode())));// 试剂选择json

		wkmxDto.setFormAction("addSaveLibrary");

		Object coefficient=redisUtil.hget("matridx_xtsz","pooling.coefficient");
		if(coefficient!=null){
			com.alibaba.fastjson.JSONObject job= com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(coefficient));
			mav.addObject("poolingCoefficient", job.getString("szz"));
		}

		mav.addObject("wkglDto",page_wkglDto);
		mav.addObject("mrjcdw", jcsjDto);
		mav.addObject("mryqlx", jcsjDto_machine);
		mav.addObject("mrsjxz", jcsjdto_reagent);
		mav.addObject("wkmxDto", wkmxDto);
		mav.addObject("sjxsbj1", "1");
		mav.addObject("sjxsbj2", "1");
		mav.addObject("sfjyh", "0");

		return mav;
	}

	@RequestMapping("/library/pagedataJcsjByFcsidAndJclb")
	@ResponseBody
	public Map<String,Object> pagedataJcsjByFcsidAndJclb(HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		String jclb = request.getParameter("jclb");
		String fcsid = request.getParameter("fcsid");
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setFcsid(fcsid);
		jcsjDto.setJclb(jclb);
		List<JcsjDto> list = jcsjService.getDtoList(jcsjDto);
		map.put("list",list);
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(fcsid)).findFirst();
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
			List<Map<String, String>> wksjList = getWksjList(JcdwDto.getCsdm(),reagent_json_map);
			map.put("wksjList",wksjList);
		}
		return map;
	}

	@RequestMapping("/potency/pagedataGetZdpbWkList")
	@ResponseBody
	public Map<String,Object> pagedataGetZdpbWkList(WkmxDto wkmxDto){
		Map<String,Object> map=new HashMap<>();
		User user =getLoginInfo();
		List<String> jcdws =new ArrayList<>();
		List<TqmxDto> list = new ArrayList<>();
		List<TqmxDto> tqmxList = new ArrayList<>();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());

		if("1".equals(user.getDqjsdwxdbj())) {
			Map<String, String> paramMap = new HashMap<String,String>();
			paramMap.put("jsid", user.getDqjs());
			List<Map<String, String>> dwlist = commonService.getJsjcdwDtoList(paramMap);
			if(dwlist!=null&&dwlist.size()>0){
				for(Map<String, String> jcsjDto:dwlist){
					if("0z".equals(jcsjDto.get("csdm"))){
						jcdws.add(jcsjDto.get("jcdw"));
					}else if("0W".equals(jcsjDto.get("csdm"))){
						jcdws.add(jcsjDto.get("jcdw"));
					}
				}
			}
		}else {
			if(jcdwList!=null&&jcdwList.size()>0){
				for(JcsjDto jcsjDto:jcdwList){
					if("0z".equals(jcsjDto.getCsdm())){
						jcdws.add(jcsjDto.getCsid());
					}else if("0W".equals(jcsjDto.getCsdm())){
						jcdws.add(jcsjDto.getCsid());
					}
				}
			}
		}
		jcdws.add(wkmxDto.getJcdw());
		Map<String,Object> sjsyglMap=new HashMap<>();
		if(jcdws!=null&&jcdws.size()>0){
			sjsyglMap.put("jcdws",jcdws.toArray(new String[jcdws.size()]));
		}
		sjsyglMap.put("jcdw", wkmxDto.getJcdw());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=sdf.format(new Date());
		Map<String,String>countMap=tqmxService.getCountByjcdw(sjsyglMap);
		if(countMap!=null){
			if(countMap.get("count")!=null&&Integer.valueOf(countMap.get("count").toString())==0){
				sjsyglMap.put("noTq","1");
			}
		}
		sjsyglMap.put("wkrq",wkmxDto.getWkrq());
		String qzStr="DEFAULT";
		String pxzd="kzcs1";
		String pxStr="";
		String sort="";
		if(StringUtil.isNotBlank(wkmxDto.getYqlx())){
			List<JcsjDto> cxyList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());
			if(!CollectionUtils.isEmpty(cxyList)){
				Optional<JcsjDto> cxyOpt=cxyList.stream().filter(x->x.getCsid().equals(wkmxDto.getYqlx())).findFirst();
				if(cxyOpt.isPresent()){
					JcsjDto cxyDto=cxyOpt.get();
					String cskz3=cxyDto.getCskz3();
					if(StringUtil.isNotBlank(cskz3)){
						com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(cskz3);
						if(jsonObject!=null){
							Object sfjyhObj=jsonObject.get("MERGE_CONC");
							if(sfjyhObj!=null){
								String sfjyhStr=sfjyhObj.toString();
								if("1".equals(sfjyhStr)){
									qzStr=jsonObject.getString("SETTING_VOLUMN")+"_";
									sjsyglMap.put("qz",qzStr);

									if(jsonObject.get("pxzd")!=null){
										pxzd=jsonObject.get("pxzd").toString();
									}
									if(jsonObject.get("tj")!=null){
										sjsyglMap.put("tj",jsonObject.get("tj")==null?"asc":jsonObject.get("tj"));
										sort=jsonObject.get("tj")==null?"asc":jsonObject.get("tj").toString();
									}else if(jsonObject.get("jcxmmc")!=null){
										String jcxmStr=jsonObject.get("jcxmmc").toString();
										sjsyglMap.put("jcxms",jcxmStr.split(","));
										pxStr=jcxmStr;
									}else if(jsonObject.get("yblxmc")!=null){
										String yblxStr=jsonObject.get("yblxmc").toString();
										sjsyglMap.put("yblxmcs",yblxStr.split(","));
										pxStr=yblxStr;
									}else if(jsonObject.get("yblx")!=null){
										String yblxStr=jsonObject.get("yblx").toString();
										sjsyglMap.put("yblxs",yblxStr.split(","));
										pxStr=yblxStr;
									}


								}
							}
						}
					}
				}
			}


		}
		list =tqmxService.getWkInfoListZdpb(sjsyglMap);
		List<String>sjids=new ArrayList<>();
		if(list!=null&&list.size()>0) {
			for (TqmxDto tqmxDto : list) {
				sjids.add(tqmxDto.getSjid());
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sjids", sjids);
			params.put("jcdws", sjsyglMap.get("jcdws") == null ? "" : sjsyglMap.get("jcdws"));
			params.put("rq", wkmxDto.getWkrq());

			tqmxList = tqmxService.getExtractInfoZdpb(params);
			//系统设置里是根据 inspect.wk.orderby.01(最后的01为检测单位的代码) 去查找数据，如果没有，则取inspect.wk.orderby的数据---用来排序
			String jcdwCsdm = "";
			for (JcsjDto jcsjDto1 : jcdwList) {
				if (jcsjDto1.getCsid().equals(wkmxDto.getJcdw())) {
					jcdwCsdm = jcsjDto1.getCsdm();
					break;
				}
			}
			List<TqmxDto> tqmxDtos = new ArrayList<>();

			Map<String, List<TqmxDto>> listMap = new HashMap<>();
			pxzd="kzcs1";
			if(pxzd.equals("kzcs1")){
				listMap=list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getKzcs1()).orElse("")));
			}else if(pxzd.equals("yblxmc")){
				listMap=list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getYblxmc()).orElse("")));
			}else if(pxzd.equals("jcxmmc")){
				listMap=list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getJcxmmc()).orElse("")));
			}else if(pxzd.equals("tj")){
				if("asc".equals(sort)){
					listMap=list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getTj()).orElse("")));
				}else{

					listMap=list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getTj()).orElse("")));
					listMap=listMap.entrySet().stream().sorted(Map.Entry.<String, List<TqmxDto>>comparingByKey().reversed())
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				}

			}else if(pxzd.equals("yblx")){
				listMap=list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getYblx()).orElse("")));
			}
			XtszDto JcdwxtszDto = xtszService.getDtoById("inspect.wk.orderby." + jcdwCsdm);
			if (JcdwxtszDto == null) {
				JcdwxtszDto = xtszService.getDtoById("inspect.wk.orderby");
			}
			if(StringUtil.isBlank(pxStr)){
				pxStr = JcdwxtszDto == null ? "" : JcdwxtszDto.getSzz();
			}
			String[] pxArr = pxStr.split(",");
			for (String px : pxArr) {
				if (listMap.get(px) != null) {
					tqmxDtos.addAll(listMap.get(px));
					listMap.remove(px);
				}
			}
			Set<String> keySet = listMap.keySet();
			for (String key : keySet) {
				if (StringUtil.isNotBlank(key)) {
					tqmxDtos.addAll(listMap.get(key));
				}

			}
			Map<String, List<TqmxDto>> tqListMap = tqmxList.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getSjid()).orElse("")));
			for (TqmxDto tqmxDto : tqmxDtos) {
				if (tqListMap.get(tqmxDto.getSjid()) != null) {
					tqmxDto.setTqmxDtoList(tqListMap.get(tqmxDto.getSjid()));
				}
			}
			map.put("wklist", tqmxDtos);
			map.put("pxzd", pxzd);
		}

		return map;
	}
	public void getZdpbList(User user,GrszDto grszDto,ModelAndView modelAndView){
		Map<String,Object> sjsyglMap=new HashMap<>();
		List<String> jcdws =new ArrayList<>();
		List<TqmxDto> list = new ArrayList<>();
		List<TqmxDto> tqmxList = new ArrayList<>();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		LocalDate currentDate = LocalDate.now();
		if(hour<8){
			LocalDate previousDay = currentDate.minusDays(1);
			sjsyglMap.put("wkrq",previousDay.toString());
		}else{
			sjsyglMap.put("wkrq",currentDate.toString());
		}
		if("1".equals(user.getDqjsdwxdbj())) {
			Map<String, String> paramMap = new HashMap<String,String>();
			paramMap.put("jsid", user.getDqjs());
			List<Map<String, String>> dwlist = commonService.getJsjcdwDtoList(paramMap);
			if(dwlist!=null&&dwlist.size()>0){
				for(Map<String, String> jcsjDto:dwlist){
					if("0z".equals(jcsjDto.get("csdm"))){
						jcdws.add(jcsjDto.get("jcdw"));
					}else if("0W".equals(jcsjDto.get("csdm"))){
						jcdws.add(jcsjDto.get("jcdw"));
					}
				}
			}
		}else {
			if(jcdwList!=null&&jcdwList.size()>0){
				for(JcsjDto jcsjDto:jcdwList){
					if("0z".equals(jcsjDto.getCsdm())){
						jcdws.add(jcsjDto.getCsid());
					}else if("0W".equals(jcsjDto.getCsdm())){
						jcdws.add(jcsjDto.getCsid());
					}
				}
			}
		}
		if(grszDto!=null){
			jcdws.add(StringUtil.isBlank(grszDto.getSzz())?jcdwList.get(0).getCsid():grszDto.getSzz());
            sjsyglMap.put("jcdw", StringUtil.isBlank(grszDto.getSzz())?jcdwList.get(0).getCsid():grszDto.getSzz());
		}else{
			jcdws.add(jcdwList.get(0).getCsid());
            sjsyglMap.put("jcdw", jcdwList.get(0).getCsid());
		}
		if(jcdws!=null&&jcdws.size()>0){
			sjsyglMap.put("jcdws",jcdws.toArray(new String[jcdws.size()]));
		}

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=sdf.format(new Date());
		Map<String,String>countMap=tqmxService.getCountByjcdw(sjsyglMap);
		if(countMap!=null){
			if(countMap.get("count")!=null&&Integer.valueOf(countMap.get("count").toString())==0){
				sjsyglMap.put("noTq","1");
			}
		}
		list =tqmxService.getWkInfoListZdpb(sjsyglMap);
		List<String>sjids=new ArrayList<>();
		if(list!=null&&list.size()>0){
			for(TqmxDto tqmxDto:list){
				sjids.add(tqmxDto.getSjid());
			}
			//防止没有选中单位的情况
			if(jcdws==null||jcdws.size() ==0)
				return;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sjids",sjids);
			params.put("jcdws",sjsyglMap.get("jcdws")==null?"":sjsyglMap.get("jcdws"));
			if(hour<8){
				LocalDate previousDay = currentDate.minusDays(1);
				params.put("rq",previousDay.toString());
			}else{
				params.put("rq",currentDate.toString());
			}
			params.put("rq",nowDate);

			tqmxList=tqmxService.getExtractInfoZdpb(params);
			//系统设置里是根据 inspect.wk.orderby.01(最后的01为检测单位的代码) 去查找数据，如果没有，则取inspect.wk.orderby的数据---用来排序
			String jcdwCsdm="";
			for(JcsjDto jcsjDto1:jcdwList){
				if(jcsjDto1.getCsid().equals(grszDto==null?"":grszDto.getSzz())){
					jcdwCsdm=jcsjDto1.getCsdm();
					break;
				}
			}
			List<TqmxDto> tqmxDtos=new ArrayList<>();
			Map<String, List<TqmxDto>> listMap = list.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getKzcs1()).orElse("")));
			XtszDto JcdwxtszDto = xtszService.getDtoById("inspect.wk.orderby."+jcdwCsdm);
			String pxStr="";
			if(JcdwxtszDto==null){
				JcdwxtszDto=xtszService.getDtoById("inspect.wk.orderby");
			}
			pxStr=JcdwxtszDto==null?"":JcdwxtszDto.getSzz();
			String[] pxArr=pxStr.split(",");
			for(String px:pxArr){
				if(listMap.get(px)!=null){
					tqmxDtos.addAll(listMap.get(px));
					listMap.remove(px);
				}
			}
			Set<String> keySet=listMap.keySet();
			for(String key:keySet){
				if(StringUtil.isNotBlank(key)){
					tqmxDtos.addAll(listMap.get(key));
				}

			}
			Map<String, List<TqmxDto>> tqListMap = tqmxList.stream().collect(Collectors.groupingBy(x -> Optional.ofNullable(x.getSjid()).orElse("")));
			for(TqmxDto tqmxDto:tqmxDtos){
				if(tqListMap.get(tqmxDto.getSjid())!=null){
					tqmxDto.setTqmxDtoList(tqListMap.get(tqmxDto.getSjid()));
				}
			}
			modelAndView.addObject("wklist",tqmxDtos);
		}

	}
	/**
	 * 跳转添加接头界面
	 */
	@RequestMapping("/library/pagedataAddConnect")
	public ModelAndView getAddConnectPage(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_connectadd");
		mav.addObject("WkmxDto", wkmxDto);
		return mav;
	}

	/**
	 * 新增提交保存
	 */
	@RequestMapping("/library/addSaveLibrary")
	@ResponseBody
	public Map<String, Object> addSaveLibrary(WkmxDto wkmxDto,HttpServletRequest request){
		User user = getLoginInfo();
		boolean isSuccess;
		Map<String, Object> map = new HashMap<>();

		//获取当前个人设置
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),
				PersonalSettingEnum.SEQUENCER_CODE.getCode(),
				PersonalSettingEnum.REAGENT_SELECT.getCode(),
				PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_WK.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_IN_WK.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN_WK.getCode(),
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
		//获取系统设置里的实验室 提取/文库试剂的设置信息
		String jcdw=wkmxDto.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
		Map<String,String> wksjMap=new HashMap<>();
		List<Map<String,String>> sysjglList=new ArrayList<>();
		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//提取文库试剂
			XtszDto wksjtszDto = xtszService.getDtoById("reagent.extend.library." +JcdwDto.getCsdm());
			if(wksjtszDto==null || StringUtil.isBlank(wksjtszDto.getSzz())){
				wksjtszDto = xtszService.getDtoById("reagent.extend.library" );
			}
			if(wksjtszDto != null && StringUtil.isNotBlank(wksjtszDto.getSzz())) {
				JSONArray jsonArray_wk = JSONArray.parseArray(wksjtszDto.getSzz());
				for (int i = 0; i < jsonArray_wk.size(); i++) {
					com.alibaba.fastjson.JSONObject jsonObject = jsonArray_wk.getJSONObject(i);
					if (StringUtil.isNotBlank(request.getParameter(jsonObject.getString("variable")))) {
						wksjMap.put(jsonObject.getString("variable"), request.getParameter(jsonObject.getString("variable")));
						Map<String, String> sysjglMap = new HashMap<>();
						sysjglMap.put("sjmc", jsonObject.getString("title"));
						sysjglMap.put("sjph", request.getParameter(jsonObject.getString("variable")));
						sysjglMap.put("sjbm", jsonObject.getString("variable"));
						sysjglMap.put("lrry", user.getYhid());
						sysjglMap.put("jcdw", wkmxDto.getJcdw());
						sysjglMap.put("sjrq", wkmxDto.getWkrq());
						//把页面的试剂信息保存到个人设置里
						reagent_json_map.put(jsonObject.getString("variable"), request.getParameter(jsonObject.getString("variable")));
						sysjglList.add(sysjglMap);
					}
				}
			}
		}
		wkmxDto.setLrry(user.getYhid());
		//开始保存个人设置
		//检测单位个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.TEST_PLACE.getCode(),wkmxDto.getJcdw());
		//仪器类型个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.SEQUENCER_CODE.getCode(),wkmxDto.getYqlx());
		//试剂选择个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_SELECT.getCode(),wkmxDto.getSjxz());
		//宏基因组DNA建库试剂盒个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),wkmxDto.getSjph1());
		//逆转录试剂盒个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),wkmxDto.getSjph2());
		//自动排版个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_WK.getCode(),wkmxDto.getZdpb());
		//自动排版个人设置包含
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_IN_WK.getCode(),wkmxDto.getZdpbin());
		//自动排版个人设置不包含
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN_WK.getCode(),wkmxDto.getZdpbnotin());
		//定量试剂个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),wkmxDto.getSjph3());
		//文库其他试剂个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode(), com.alibaba.fastjson.JSONObject.toJSONString(reagent_json_map));
		List<String> notexitnbbhs;
		try {
			notexitnbbhs = wkmxService.exitnbbh(wkmxDto);
			WkglDto wkglDto = new WkglDto();
			wkglDto.setWkmc(wkmxDto.getWkmc());
			wkglDto = wkglService.getDtoByWkmc(wkglDto);
			if (wkglDto != null) {
				map.put("status", "fail");
				map.put("message", "文库名称不允许重复!");
				return map;
			}
			if (notexitnbbhs == null || notexitnbbhs.size() < 1 || ("1").equals(wkmxDto.getSfbc())) {
				wkmxDto.setWksj(JSON.toJSONString(wksjMap));
				wkmxDto.setSysjStr(JSON.toJSONString(sysjglList));
				isSuccess = wkmxService.addSaveWkxx(wkmxDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
						: xxglService.getModelById("ICOM00002").getXxnr());
				return map;
			} else {
				map.put("status", "caution");
				map.put("message", "保存失败!内部编号:" + String.join(",", notexitnbbhs) + "不存在!是否继续保存!");
				map.put("notexitnbbhs", notexitnbbhs);
				return map;
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
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
	 * 跳转修改界面
	 */
	@RequestMapping("/library/modLibrary")
	@ResponseBody
	public ModelAndView modLibraryPage(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_add");
		wkmxDto.setFormAction("modSaveLibrary");

		WkglDto wkglxx = wkglService.getDtoById(wkmxDto.getWkid());
		if(wkglxx== null)
			return mav;
		wkmxDto.setJcdw(wkglxx.getJcdw());
		wkmxDto.setYqlx(wkglxx.getYqlx());
		wkmxDto.setSjxz(wkglxx.getSjxz());
		wkmxDto.setWkmc(wkglxx.getWkmc());
		wkmxDto.setWkrq(wkglxx.getWkrq());
		wkmxDto.setSjph1(wkglxx.getSjph1());
		wkmxDto.setSjph2(wkglxx.getSjph2());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECTION_UNIT,
						BasicDataTypeEnum.SEQUENCER_CODE,
						BasicDataTypeEnum.REAGENT_SELECT });
		mav.addObject("jcdwList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));// 检测单位
		mav.addObject("yqlxList", jclist.get(BasicDataTypeEnum.SEQUENCER_CODE.getCode()));// 测序仪
		mav.addObject("sjxzList", jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode()));// 试剂选择
		mav.addObject("sjxzJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode())));// 试剂选择json
		mav.addObject("wkmxDto", wkmxDto);
		Object coefficient=redisUtil.hget("matridx_xtsz","pooling.coefficient");
		if(coefficient!=null){
			com.alibaba.fastjson.JSONObject job= com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(coefficient));
			mav.addObject("poolingCoefficient", job.getString("szz"));
		}
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),
				PersonalSettingEnum.SEQUENCER_CODE.getCode(),
				PersonalSettingEnum.REAGENT_SELECT.getCode(),
				PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
		GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
		if(StringUtil.isBlank(wkglxx.getSjph1()) && grszxx_o!=null) {
			wkglxx.setSjph1(grszxx_o.getSzz());
			mav.addObject("sjxsbj1", "1");
		}else{
			mav.addObject("sjxsbj1", "0");
		}
		if(StringUtil.isBlank(wkglxx.getSjph2()) && grszxx_t!=null) {
			wkglxx.setSjph2(grszxx_t.getSzz());
			mav.addObject("sjxsbj2", "1");
		}else{
			mav.addObject("sjxsbj2", "0");
		}
		//定量试剂 个人设置
		GrszDto grszxx_dlsj = grszDtoMap.get(PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode());
		if (StringUtil.isBlank(wkglxx.getSjph3()) && grszxx_dlsj != null){
			wkglxx.setSjph3(grszxx_dlsj.getSzz());
			mav.addObject( "xsbj", "1");
		}else {
			mav.addObject( "xsbj", "0");
		}
		mav.addObject("wkglDto",wkglxx);
		GrszDto grszxx_machine = grszDtoMap.get(PersonalSettingEnum.SEQUENCER_CODE.getCode());
		GrszDto grszxx_reagent = grszDtoMap.get(PersonalSettingEnum.REAGENT_SELECT.getCode());
		JcsjDto jcsjDto_machine = new JcsjDto();
		if (grszxx_machine!=null && StringUtil.isNotBlank(grszxx_machine.getSzz())) {
			jcsjDto_machine = jcsjService.getDtoById(grszxx_machine.getSzz());
			if(jcsjDto_machine == null)
				jcsjDto_machine = new JcsjDto();
		}
		JcsjDto jcsjdto_reagent = new JcsjDto();
		if (grszxx_reagent != null && StringUtil.isNotBlank(grszxx_reagent.getSzz())) {
			jcsjdto_reagent = jcsjService.getDtoById(grszxx_reagent.getSzz());
			if(jcsjdto_reagent == null)
				jcsjdto_reagent = new JcsjDto();
		}
		mav.addObject("mryqlx", jcsjDto_machine);
		mav.addObject("mrsjxz", jcsjdto_reagent);
		String jcdw=wkglxx.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();

		List<Map<String, String>> wksjList = new ArrayList<>();
		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//提取文库试剂
			String wksj=wkglxx.getWksj();
			Map<String,String> wksjMap = new HashMap<>();
			if(StringUtil.isNotBlank(wksj)){
				wksjMap= com.alibaba.fastjson.JSONObject.parseObject(wksj, Map.class);
			}
			wksjList = getWksjList(JcdwDto.getCsdm(),wksjMap);
		}
		mav.addObject("wksjList",wksjList);
		return mav;
	}

	/**
	 * 合并界面
	 */
	@RequestMapping("/library/mergeLibrary")
	@ResponseBody
	public ModelAndView mergeLibrary(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_add");
		wkmxDto.setFormAction("mergeSaveLibrary");
		//页面所使用的文库DTO
		WkglDto wkglDto_t = new WkglDto();

		//文库名称新增
		WkglDto wkglDto = new WkglDto();
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		wkglDto.setLrsj(date);
		wkglDto = wkglService.getCountByDay(wkglDto);
		wkglDto.setWkid(wkmxDto.getWkid());
		wkglDto.setIds(wkmxDto.getIds());
		StringBuilder ids= new StringBuilder();
		for (String id :wkmxDto.getIds()){
			ids.append(",").append(id);
		}
		if (StringUtil.isNotBlank(ids.toString())){
			ids = new StringBuilder(ids.substring(1));
		}
		mav.addObject("ids", ids.toString());
        List<WkglDto> wkglxxlist = wkglService.getDtoList(wkglDto);
		wkmxDto.setJcdw(wkglxxlist.get(0).getJcdw());
		String wkmc = "";
		for (WkglDto wk : wkglxxlist){
			if (wk.getWkmc().contains("合并")){
				wkmc = wk.getWkmc();
				break;
			}
		}
		if (StringUtil.isBlank(wkmc)){
			wkglDto_t.setWkmc(wkglxxlist.get(0).getWkmc()+"(合并)");
		}else{
			wkglDto_t.setWkmc(wkmc);
		}
		//wkmxDto.setWkrq(wkglxxlist.get(0).getWkrq());
		wkglDto_t.setWkrq(wkglxxlist.get(0).getWkrq());

		//个人试剂处理
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});
		//获取个人配置
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		Set<String> sjph1= new HashSet<>();
		Set<String> sjph2= new HashSet<>();
		Set<String> sjph3= new HashSet<>();
		for (WkglDto wkgl : wkglxxlist){
			if (StringUtil.isNotBlank(wkgl.getSjph1())){
				sjph1.add(wkgl.getSjph1());
			}
			if (StringUtil.isNotBlank(wkgl.getSjph2())){
				sjph2.add(wkgl.getSjph2());
			}
			if (StringUtil.isNotBlank(wkgl.getSjph3())){
				sjph3.add(wkgl.getSjph3());
			}
		}
		if (!sjph1.isEmpty()){
			mav.addObject("sjxsbj1", "0");
			wkglDto_t.setSjph1(String.join(",", sjph1));
		}else{
			GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
			if(StringUtil.isBlank(wkglDto_t.getSjph1()) && grszxx_o!=null) {
				wkglDto_t.setSjph1(grszxx_o.getSzz());
				mav.addObject("sjxsbj1", "1");
			}else{
				mav.addObject("sjxsbj1", "0");
			}
		}
		if (!sjph2.isEmpty()){
			mav.addObject("sjxsbj2", "0");
			wkglDto_t.setSjph2(String.join(",", sjph2));
		}else{
			GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
			if(StringUtil.isBlank(wkglDto_t.getSjph2()) && grszxx_t!=null) {
				wkglDto_t.setSjph2(grszxx_t.getSzz());
				mav.addObject("sjxsbj2", "1");
			}else{
				mav.addObject("sjxsbj2", "0");
			}
		}
		if (!sjph3.isEmpty()){
			mav.addObject( "xsbj", "0");
			wkglDto_t.setSjph3(String.join(",", sjph3));
		}else{
			//定量试剂 个人设置
			GrszDto grszxx_dlsj = grszDtoMap.get(PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode());
			if(StringUtil.isBlank(wkglDto_t.getSjph3()) && grszxx_dlsj!=null) {
				wkglDto_t.setSjph3(grszxx_dlsj.getSzz());
				mav.addObject("xsbj", "1");
			}else{
				mav.addObject("xsbj", "0");
			}
		}
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECTION_UNIT,
						BasicDataTypeEnum.SEQUENCER_CODE,
						BasicDataTypeEnum.REAGENT_SELECT });
		mav.addObject("jcdwList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));// 检测单位
		mav.addObject("yqlxList", jclist.get(BasicDataTypeEnum.SEQUENCER_CODE.getCode()));// 测序仪
		mav.addObject("sjxzList", jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode()));// 试剂选择
		mav.addObject("sjxzJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode())));// 试剂选择json
		mav.addObject("wkmxDto", wkmxDto);
		Object coefficient=redisUtil.hget("matridx_xtsz","pooling.coefficient");
		if(coefficient!=null){
			com.alibaba.fastjson.JSONObject job= com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(coefficient));
			mav.addObject("poolingCoefficient", job.getString("szz"));
		}

		//查个人设置检查单位
		GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
		GrszDto grszxx_machine = grszDtoMap.get(PersonalSettingEnum.SEQUENCER_CODE.getCode());
		GrszDto grszxx_reagent = grszDtoMap.get(PersonalSettingEnum.REAGENT_SELECT.getCode());
		JcsjDto jcsjDto = null;
		if (grszxx != null) {
			jcsjDto = jcsjService.getDtoById(grszxx.getSzz());
			if(jcsjDto != null) {
				wkmc = jcsjDto.getCsdm() + "-" + wkmc;
			}else{
				jcsjDto = new JcsjDto();
			}
		}
		JcsjDto jcsjDto_machine = null;
		if (grszxx_machine != null && StringUtil.isNotBlank(grszxx_machine.getSzz())) {
			jcsjDto_machine = jcsjService.getDtoById(grszxx_machine.getSzz());
		}
		if(jcsjDto_machine== null)
			jcsjDto_machine = new JcsjDto();
		JcsjDto jcsjdto_reagent = null;
		if (grszxx_reagent != null&& StringUtil.isNotBlank(grszxx_reagent.getSzz())) {
			jcsjdto_reagent = jcsjService.getDtoById(grszxx_reagent.getSzz());
		}
		if(jcsjdto_reagent == null)
			jcsjdto_reagent = new JcsjDto();
		//实验室提取文库试剂 因为需要把各个文库的试剂进行合并，wkglxxlist，所以此处暂时不进行统一
		XtszDto wksjszDto = xtszService.getDtoById("reagent.extend.library." + wkglxxlist.get(0).getJcdwdm());
		if(wksjszDto==null || StringUtil.isBlank(wksjszDto.getSzz())){
			//通用提取文库试剂
			wksjszDto = xtszService.getDtoById("reagent.extend.library" );
		}
		List<Map<String,String>>wksjList=new ArrayList<>();
		if(wksjszDto!= null && StringUtil.isNotBlank(wksjszDto.getSzz())) {
			JSONArray jsonArray_wk = JSONArray.parseArray(wksjszDto.getSzz());
			for (int i = 0; i < jsonArray_wk.size(); i++) {
				com.alibaba.fastjson.JSONObject jsonObject = jsonArray_wk.getJSONObject(i);
				if (StringUtil.isNotBlank(jsonObject.getString("status")) && "0".equals(jsonObject.getString("status"))) {
					Map<String, String> wksjMap = new HashMap<>();
					wksjMap.put("title", jsonObject.getString("title"));
					wksjMap.put("variable", jsonObject.getString("variable"));
					wksjMap.put("type", jsonObject.getString("type"));
					wksjMap.put("status", jsonObject.getString("status"));
					//循环获取当前已有的文库里的试剂信息，采用Set去重
					Set<String> wksjSet = new HashSet<>();
					for (WkglDto wkgl : wkglxxlist){
						com.alibaba.fastjson.JSONObject wksj = JSON.parseObject(wkgl.getWksj());
						if (wksj != null){
							String sj_value = wksj.getString(jsonObject.getString("variable"));
							if (StringUtil.isNotBlank(sj_value)){
								wksjSet.add(sj_value);
							}
						}
					}
					if (!wksjSet.isEmpty()) {
						wksjMap.put("value", String.join(",", wksjSet));
					}else{
						wksjMap.put("value", "");
					}
					wksjList.add(wksjMap);
				}
			}
		}
		mav.addObject("wksjList", wksjList);
		mav.addObject("mrjcdw", jcsjDto);
		mav.addObject("mryqlx", jcsjDto_machine);
		mav.addObject("mrsjxz", jcsjdto_reagent);
		mav.addObject("wkglDto",wkglDto_t);
		mav.addObject("xsbj", "0");
		return mav;
	}

	/**
	 * 合并提交保存
	 */
	@RequestMapping("/library/mergeSaveLibrary")
	@ResponseBody
	public Map<String, Object> mergeSaveLibrary(WkmxDto wkmxDto,HttpServletRequest request){
		boolean isSuccess;
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		wkmxDto.setLrry(user.getYhid());
		List<String> notexitnbbhs;
		try {
			notexitnbbhs = wkmxService.exitnbbh(wkmxDto);
			WkglDto wkglDto = new WkglDto();
			wkglDto.setWkmc(wkmxDto.getWkmc());
			wkglDto = wkglService.getDtoByWkmc(wkglDto);
			if (wkglDto != null) {
				map.put("status", "fail");
				map.put("message", "文库名称不允许重复!");
				return map;
			}

			String jcdw=wkmxDto.getJcdw();
			List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
			Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
			Map<String,String> wksjMap=new HashMap<>();
			List<Map<String,String>> sysjglList=new ArrayList<>();
			if(optional.isPresent()){
				JcsjDto JcdwDto=optional.get();
				//文库试剂
				XtszDto wksjszDto = xtszService.getDtoById("reagent.extend.library." + JcdwDto.getCsdm());
				if(wksjszDto==null || StringUtil.isBlank(wksjszDto.getSzz())){
					//通用提取文库试剂
					wksjszDto = xtszService.getDtoById("reagent.extend.library");
					if (wksjszDto == null)
						wksjszDto = new XtszDto();
				}
				JSONArray jsonArray_wk=JSONArray.parseArray(wksjszDto.getSzz());
				for(int i = 0; i < jsonArray_wk.size(); i++){
					com.alibaba.fastjson.JSONObject jsonObject=jsonArray_wk.getJSONObject(i);
					if(StringUtil.isNotBlank(request.getParameter(jsonObject.getString("variable")))){
						wksjMap.put(jsonObject.getString("variable"),request.getParameter(jsonObject.getString("variable")));
						Map<String,String> sysjglMap = new HashMap<>();
						sysjglMap.put("sjmc",jsonObject.getString("title"));
						sysjglMap.put("sjph",request.getParameter(jsonObject.getString("variable")));
						sysjglMap.put("sjbm",jsonObject.getString("variable"));
						sysjglMap.put("lrry",user.getYhid());
						sysjglMap.put("jcdw",wkmxDto.getJcdw());
						sysjglMap.put("sjrq",wkmxDto.getWkrq());
						sysjglList.add(sysjglMap);
					}
				}
			}
			wkmxDto.setWksj(JSON.toJSONString(wksjMap));
			wkmxDto.setSysjStr(JSON.toJSONString(sysjglList));
			//新增一条合并后的文库数据
			if (notexitnbbhs == null || notexitnbbhs.size() < 1 || ("1").equals(wkmxDto.getSfbc())) {
				isSuccess = wkglService.mergeSaveWk(wkmxDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
						: xxglService.getModelById("ICOM00002").getXxnr());
				return map;
			} else {
				map.put("status", "caution");
				map.put("message", "保存失败!内部编号:" + String.join(",", notexitnbbhs) + "不存在!是否继续保存!");
				map.put("notexitnbbhs", notexitnbbhs);
				return map;
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message",e.getMsg());
			return map;
		}
	}

	/**
	 * 取消合并
	 */
	@RequestMapping("/library/cancelmergeLibrary")
	@ResponseBody
	public Map<String, Object> cancelmergeLibrary(WkglDto wkglDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		WkglDto wkglDto_t = wkglService.getDto(wkglDto);
		wkglDto_t.setScry(user.getYhid());
		wkglDto_t.setAccess_token(wkglDto.getAccess_token());
		//判断ywkid如果为空，则直接返回
		if (StringUtil.isBlank(wkglDto_t.getYwkid())){
			map.put("status", "fail");
			map.put("message", "非合并数据，请选择合并数据再进行取消合并操作");
		}
		boolean isSuccess = wkglService.cancelMerge(wkglDto_t);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;

	}

	/**
	 * 获取修改界面内容返回给JS处理
	 */
	@RequestMapping("/library/pagedataGetXgxx")
	@ResponseBody
	public List<WkmxDto> getXgxx(WkmxDto wkmxDto) {
		List<WkmxDto> wkmxlist = wkmxService.getWkmxByWkid(wkmxDto);
		List<String> sjids=new ArrayList<>();
		if(!CollectionUtils.isEmpty(wkmxlist)){
			
			WkglDto wkglDto = wkglService.getDtoById(wkmxDto.getWkid());
			
			for(WkmxDto dto:wkmxlist){
				if(StringUtil.isNotBlank(dto.getSjid()))
					sjids.add(dto.getSjid());
			}
			sjids = sjids.stream().distinct().collect(Collectors.toList());
			TqmxDto tqmxDto=new TqmxDto();
			tqmxDto.setIds(sjids);
			//增加日期限制，否则NC-A这种通用的阴性对照会找出非常多的数据 2024-02-05
			if(wkglDto!=null) {
				tqmxDto.setSyrq(wkglDto.getWkrq());
				tqmxDto.setJcdw(wkglDto.getJcdw());
			}
			List<TqmxDto> dtoList = tqmxService.getDtoList(tqmxDto);
			if(!CollectionUtils.isEmpty(dtoList)){
				Map<String, List<TqmxDto>> listMap = dtoList.stream().collect(Collectors.groupingBy(TqmxDto::getSjid));
				if (null != listMap && listMap.size()>0){
					Iterator<Map.Entry<String, List<TqmxDto>>> entries = listMap.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String,  List<TqmxDto>> entry = entries.next();
						String key = entry.getKey();
						List<TqmxDto> resultModelList = entry.getValue();
						for(WkmxDto dto:wkmxlist){
							if(key.equals(dto.getSjid())){
								dto.setTqmxDtos(resultModelList);
							}
						}
					}
				}
			}
		}
		return wkmxlist;
	}

	/**
	 * 修改提交保存
	 */
	@RequestMapping("/library/modSaveLibrary")
	@ResponseBody
	public Map<String, Object> modSaveLibrary(WkmxDto wkmxDto,HttpServletRequest request){
		boolean isSuccess;
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		wkmxDto.setXgry(user.getYhid());
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),
				PersonalSettingEnum.SEQUENCER_CODE.getCode(),
				PersonalSettingEnum.REAGENT_SELECT.getCode(),
				PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_WK.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_IN_WK.getCode(),
				PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN_WK.getCode(),
				PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode()});

		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		//检测单位个人设置保存
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.TEST_PLACE.getCode(),wkmxDto.getJcdw());
		//仪器类型个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.SEQUENCER_CODE.getCode(),wkmxDto.getYqlx());
		//试剂选择个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_SELECT.getCode(),wkmxDto.getSjxz());
		//宏基因组DNA建库试剂盒个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),wkmxDto.getSjph1());
		//逆转录试剂盒个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode(),wkmxDto.getSjph2());
		//ngsDNA建库定量试剂盒个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_THREE_SETTINGS.getCode(),wkmxDto.getSjph3());
		//自动排版个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_WK.getCode(),wkmxDto.getZdpb());
		//自动排版个人设置包含
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_IN_WK.getCode(),wkmxDto.getZdpbin());
		//自动排版个人设置不包含
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.LIBRARY_SET_TYPE_NOTIN_WK.getCode(),wkmxDto.getZdpbnotin());
		//获取个人设置里的实验室 提取/文库试剂的设置信息
		GrszDto grszxx_reagent_json= grszDtoMap.get(PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode());
		Map<String,String> reagent_json_map = new HashMap<>();
		if(grszxx_reagent_json!=null && StringUtil.isNotBlank(grszxx_reagent_json.getSzz())) {
			reagent_json_map=com.alibaba.fastjson.JSONObject.parseObject(grszxx_reagent_json.getSzz(),Map.class);
		}

		List<String> notexitnbbhs;
		String jcdw=wkmxDto.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();
		Map<String,String> wksjMap=new HashMap<>();
		List<Map<String,String>> sysjglList = new ArrayList<>();
		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//文库试剂
			XtszDto wksjszDto = xtszService.getDtoById("reagent.extend.library." + JcdwDto.getCsdm());
			if(wksjszDto==null || StringUtil.isBlank(wksjszDto.getSzz())){
				//通用提取文库试剂
				wksjszDto = xtszService.getDtoById("reagent.extend.library");
				if (wksjszDto == null)
					wksjszDto = new XtszDto();
			}
			JSONArray jsonArray_wk=JSONArray.parseArray(wksjszDto.getSzz());
			for(int i = 0; i < jsonArray_wk.size(); i++){
				com.alibaba.fastjson.JSONObject jsonObject=jsonArray_wk.getJSONObject(i);
				if(StringUtil.isNotBlank(request.getParameter(jsonObject.getString("variable")))){
					wksjMap.put(jsonObject.getString("variable"),request.getParameter(jsonObject.getString("variable")));
					Map<String,String> sysjglMap = new HashMap<>();
					sysjglMap.put("sjmc",jsonObject.getString("title"));
					sysjglMap.put("sjph",request.getParameter(jsonObject.getString("variable")));
					sysjglMap.put("sjbm",jsonObject.getString("variable"));
					sysjglMap.put("ywid",wkmxDto.getWkid());
					sysjglMap.put("lrry",user.getYhid());
					sysjglMap.put("jcdw",wkmxDto.getJcdw());
					sysjglMap.put("sjrq",wkmxDto.getWkrq());
					//把页面的试剂信息保存到个人设置里
					reagent_json_map.put(jsonObject.getString("variable"), request.getParameter(jsonObject.getString("variable")));
					sysjglList.add(sysjglMap);
				}
			}
		}
		//文库其他试剂个人设置
		resetPersonalsettingsInfo(grszDtoMap,user.getYhid(),PersonalSettingEnum.REAGENT_JSON_SETTINGS.getCode(), com.alibaba.fastjson.JSONObject.toJSONString(reagent_json_map));
		try {
			//根据dto里的Nbbhs查询sjxx里是否存在并接收，返回不存在的内部编码列表。存在则返回错误。
			notexitnbbhs = wkmxService.exitnbbh(wkmxDto);
			WkglDto wkglDto = new WkglDto();
			wkglDto.setWkmc(wkmxDto.getWkmc());
			wkglDto.setWkid(wkmxDto.getWkid());
			wkglDto = wkglService.getDtoByWkmc(wkglDto);
			if (wkglDto != null) {
				map.put("status", "fail");
				map.put("message", "文库名称不允许重复!");
				return map;
			}
			//没有不存在的内部编码，则继续处理
			if (notexitnbbhs == null || notexitnbbhs.size() < 1 || "1".equals(wkmxDto.getSfbc())) {
				//根据wkmx更新文库明细的内容
				wkmxDto.setWksj(JSON.toJSONString(wksjMap));
				wkmxDto.setSysjStr(JSON.toJSONString(sysjglList));
				isSuccess = wkmxService.updateWkmxByWkidAndXh(wkmxDto);
				if (StringUtil.isNotBlank(wkmxDto.getYqlx())){
					JcsjDto cxy = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode(), wkmxDto.getYqlx());
					if (cxy!=null){
						WksjglDto wksjglDto = new WksjglDto();
						CxyxxDto cxyxxDto_mc = new CxyxxDto();
						cxyxxDto_mc.setMc(cxy.getCsmc());
						CxyxxDto cyxxx = cxyxxService.getDto(cxyxxDto_mc);
						wksjglDto.setCxy(cyxxx.getMc());
						wksjglDto.setCxyid(cyxxx.getCxyid());
						wksjglDto.setWkid(wkmxDto.getWkid());
						wksjglService.updateCxy(wksjglDto);
					}
				}
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
						: xxglService.getModelById("ICOM00002").getXxnr());
				return map;
			} else {
				map.put("status", "caution");
				map.put("message", "保存失败!内部编号:" + String.join(",", notexitnbbhs) + "不存在!是否继续保存!");
				map.put("notexitnbbhs", notexitnbbhs);
				return map;
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			return map;
		}
	}

	/**
	 * 跳转文库明细查看页面
	 */
	@RequestMapping(value = "/library/viewLibrary")
	@ResponseBody
	public ModelAndView getLibraryViewPage(WkmxDto wkmxDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("experiment/library/library_view");
		String flag = request.getParameter("flag");
		if (StringUtil.isNotBlank(flag) && flag.equals("merge")){
			mav = new ModelAndView("experiment/library/library_mergeview");
		}
		WkglDto wkglDto = wkglService.getDtoById(wkmxDto.getWkid());
		//查找合并数据
		List<WkglDto> wkglList = new ArrayList<>();
		if (StringUtil.isNotBlank(wkglDto.getYwkid())){
			WkglDto wkglDto2 = new WkglDto();
			List<String> ids2 = Arrays.asList(wkglDto.getYwkid().split(","));
			wkglDto2.setIds(ids2);
			wkglList = wkglService.getDtoList(wkglDto2);
		}
		List<WksjglDto> wksjgl = wksjglService.getWksjxxByWkid(wkmxDto.getWkid());//问阿伟按逻辑来讲只会存在一条文库列表数据对应一条文库上机管理数据
		if(wksjgl != null && wksjgl.size() > 0 ){
			mav.addObject("wksjglDto", wksjgl.get(0));
		}else {
			mav.addObject("wksjglDto", new WksjglDto());
		}
		mav.addObject("WkmxDto", wkmxDto);
		mav.addObject("wkglDto", wkglDto);
		mav.addObject("wkglList", wkglList);
		String jcdw=wkglDto.getJcdw();
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		Optional<JcsjDto> optional=jcdwList.stream().filter(e->e.getCsid().equals(jcdw)).findFirst();

		if(optional.isPresent()){
			JcsjDto JcdwDto=optional.get();
			//文库试剂
			String wksj=wkglDto.getWksj();
			Map<String,String> wksjMap = new HashMap<>();
			if(StringUtil.isNotBlank(wksj)){
				wksjMap= com.alibaba.fastjson.JSONObject.parseObject(wksj, Map.class);
			}
			List<Map<String,String>> wksjList =getWksjList(JcdwDto.getCsdm(),wksjMap);
			mav.addObject("wksjList", wksjList);
		}
		return mav;
	}

	/**
	 * 获取查看界面内容返回给JS处理
	 */
	@RequestMapping("/library/pagedataCkxx")
	@ResponseBody
	public List<WkmxDto> getCkxx(WkmxDto wkmxDto) {
		return wkmxService.getWkmxByWkid(wkmxDto);
	}

	/**
	 * 删除文库信息
	 */
	@RequestMapping("/library/delLibrary")
	@ResponseBody
	public Map<String, Object> deleteWkxxlist(WkglDto wkglDto) {
		User user = getLoginInfo();
		wkglDto.setScry(user.getYhid());
		boolean isSuccess = wkglService.deleteWkxxlist(wkglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr())
				: xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 修改单个接头信息
	 */
	@RequestMapping("/library/pagedataModeJt")
	@ResponseBody
	public ModelAndView modJt(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_connectadd");
		wkmxDto.setDjtbj("1");
		mav.addObject("WkmxDto", wkmxDto);
		return mav;
	}

	/**
	 * 检测文库明细数据中此内部编号是否已经存在DNA检测
	 */
	@RequestMapping("/library/pagedataCheckRNA")
	@ResponseBody
	public boolean checkDNA(WkmxDto wkmxDto) {
		List<WkmxDto> wkmxlist = wkmxService.getWkmxByNbbh(wkmxDto);
		return wkmxlist != null && wkmxlist.size() > 0;
	}

	/**
	 * 验证是否存在相同syglid
	 */
	@RequestMapping("/library/pagedataVerifySame")
	@ResponseBody
	public Map<String, Object> pagedataVerifySame(WkmxDto wkmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<WkmxDto> wkmxDto_t = wkmxService.verifySame(wkmxDto);
		if (wkmxDto_t != null && wkmxDto_t.size() > 0) {
			map.put("status","false");
		} else {
			map.put("status","true");
		}
		return map;
	}

	/**
	 * 跳转浓度录入页面

	 */
	@RequestMapping(value = "/library/addpotencyWk")
	public ModelAndView getAddPotencyPage(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_potencyadd");
		wkmxDto.setFormAction("addpotencySaveWk");
		mav.addObject("WkmxDto", wkmxDto);
		return mav;
	}

	/**
	 * 文库浓度提交保存
	 */
	@RequestMapping(value = "/library/addpotencySaveWk")
	@ResponseBody
	public Map<String, Object> addpotencySaveWk(WkmxDto wkmxDto) {
		User user = getLoginInfo();
		List<WkmxDto> list = new ArrayList<>();
		for (int i = 0; i < wkmxDto.getQuantitys().length; i++) {
			WkmxDto wkmx = new WkmxDto();
			wkmx.setQuantity(wkmxDto.getQuantitys()[i]);
			wkmx.setXh(wkmxDto.getXhs()[i]);
			wkmx.setWkid(wkmxDto.getWkid());
			wkmx.setXgry(user.getYhid());
			list.add(wkmx);
		}
		boolean isSuccess = wkmxService.updateWknd(list);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 根据送检id获取明细数据(送检报告实验tab下获取文库信息的接口)
	 */
	@RequestMapping(value = "/library/commGetWkmxBySjid")
	@ResponseBody
	public List<WkmxDto> getWkmxBySjid(String sjid) {
		List<WkmxDto> wkmxlist = wkmxService.getWkmxBySjid(sjid);
		/*
		  11.25修改显示内部编码 过滤掉开头是X包含tngs得，开头是x&&有rem&&检测项目是c得改为DR
		 */
		List<WkmxDto> wkmxlist1=new ArrayList<>();
		boolean flag= false;
		for(WkmxDto wkmxDto:wkmxlist){
			if(wkmxDto.getNbbh().startsWith("X")||(StringUtil.isNotBlank(wkmxDto.getWksxbm())&&wkmxDto.getWksxbm().startsWith("X"))){
				if(!wkmxDto.getNbbh().contains("tNGS")){
					if(wkmxDto.getNbbh().contains("REM")&&wkmxDto.getJcxmdm().equals("C")){
						flag =true;
					}
					if(flag){
						String[] nbbhArr=wkmxDto.getNbbh().split("-");
						String newNbbm=nbbhArr[0]+"-DR";
						wkmxDto.setNbbh(newNbbm);
						flag=false;
					}
					wkmxlist1.add(wkmxDto);
				}
			}else{
				wkmxlist1.add(wkmxDto);
			}
		}
		return wkmxlist1;
	}

	/**
	 * 根据文库ID获取文库上机信息
	 */
	@RequestMapping(value="/library/commGetWksjxxBySjid")
	@ResponseBody
	public List<WksjglDto> getWksjxxBySjid(String sjid){
		List<WksjglDto> wksjxxlist= new ArrayList<>();
		List<WksjglDto> wksjxxlist1= new ArrayList<>();
		List<WkmxDto> wkmxlist = wkmxService.getWkmxBySjid(sjid);
		if(wkmxlist!=null && wkmxlist.size()>0){
			List<String> ids=new ArrayList<>();
			for(WkmxDto dto:wkmxlist){
				ids.add(dto.getWkid());
			}
			WksjglDto wksjglDto=new WksjglDto();
			wksjglDto.setIds(ids);
			List<WksjglDto> dtoList = wksjglService.getDtoList(wksjglDto);
			boolean flag= false;
			String yjxjsj="";
			String xjsj="";
			WkmxDto tngsDto=null;
			if(dtoList!=null&&dtoList.size()>0){
				for(WkmxDto wkmxDto : wkmxlist){
					for(WksjglDto wksjglDto_t:dtoList){
						if(wkmxDto.getWkid().equals(wksjglDto_t.getWkid())){
							if(wkmxDto.getNbbh().startsWith("X")||(StringUtil.isNotBlank(wkmxDto.getWksxbm())&&wkmxDto.getWksxbm().startsWith("XD"))){
								if(!wkmxDto.getNbbh().contains("tNGS")){
									wksjxxlist1.add(wksjglDto_t);
								}else{
									tngsDto=wkmxDto;
									yjxjsj=wksjglDto_t.getYjxjsj();
									xjsj=wksjglDto_t.getXjsj();
								}
							}else {
								wksjxxlist.add(wksjglDto_t);
							}
						}
					}
				}
			}
			if(wksjxxlist1.size()>0){
				for(WksjglDto wksjglDto_t:wksjxxlist1){
					for (WkmxDto wkmxDto : wkmxlist) {
						if (wkmxDto.getWkid().equals(wksjglDto_t.getWkid())){
							if(wkmxDto.getNbbh().contains("REM")&&wkmxDto.getJcxmdm().equals("C")){
								flag =true;
							}
							if(flag){
								String[] nbbhArr=wkmxDto.getNbbh().split("-");
								String newNbbm=nbbhArr[0]+"-DR";
								wkmxDto.setNbbh(newNbbm);
								flag=false;
							}
							if(tngsDto!=null&&tngsDto.getWkrq().equals(wkmxDto.getWkrq())){
								SimpleDateFormat si=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								try {
									if(si.parse(wksjglDto_t.getXjsj()).getTime()<si.parse(xjsj).getTime()){
										wksjglDto_t.setXjsj(xjsj);
									}
									if(si.parse(wksjglDto_t.getYjxjsj()).getTime()<si.parse(yjxjsj).getTime()){
										wksjglDto_t.setYjxjsj(yjxjsj);
									}
								}catch (Exception e){
									log.error("时间转换错误");
								}

							}
							wksjxxlist.add(wksjglDto_t);
						}
					}
				}
			}
		}

		return wksjxxlist;
	}

	/**
	 * 导入文库列表
	 */
	@RequestMapping(value = "/library/importconcLibray")
	public ModelAndView importconcLibray(WkglDto wkglDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("wkglDto", wkglDto);
		return mav;
	}

	/**
	 * 填写pooling表信息
	 */
	@RequestMapping(value = "/library/pagedataConfrimExport")
	@ResponseBody
	public Map<String, Object> pagedataConfrimExport(String filePath) {
		return wkglService.analysisPooling(filePath);
	}

	/**
	 * 获取pooling表计算后的数据
	 */
	@RequestMapping(value = "/library/pagedataGetAllPooling")
	@ResponseBody
	public Map<String, Object> getAllPooling() {
		return null;
	}

	/**
	 * 文库扫描时从ngsgl表获取接头
	 */
	@RequestMapping("/library/pagedataGetJtFromNGS")
	@ResponseBody
	public Map<String, Object> getJtFromNGS(NgsglDto ngsglDto) {
		Map<String, Object> map = new HashMap<>();
		List<NgsglDto> ngsglDtos = ngsglService.getDtoByZbbmAndNbbm(ngsglDto);
		map.put("ngsglDtos", ngsglDtos);
		return map;
	}

	/**
	 * 获取送检检测项目信息
	 */
	@RequestMapping("/library/pagedataGetSjjcxm")
	@ResponseBody
	public Map<String, Object> pagedataGetSjjcxm(WkmxDto wkmxDto) {
		Map<String, Object> map = new HashMap<>();
		WkmxDto t_wkmxDto = wkmxService.getSjDtoByNbbh(wkmxDto);
		map.put("wkmxDto", t_wkmxDto);
		return map;
	}
	
    /**
     * 文库列表生成PCR对接文档页面
     */
	@RequestMapping(value = "/library/pcrdockingfilePcr")
	public ModelAndView librayPCRPage(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_pcr");
		User user = getLoginInfo();
		String ljmc = "/"+user.getYhm();
		JgxxDto jgxx = new JgxxDto();
		jgxx.setJgid(user.getJgid());
		jgxx = jgxxService.selectJgxxByJgid(jgxx);
		if (StringUtil.isNotBlank(user.getJgid())){
			ljmc = ljmc + "/" + jgxx.getJgmc();
		}
		wkmxDto.setSrlj(ljmc);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PCRMACHINE_ID,BasicDataTypeEnum.PCR_TEMPLATE});
		for (JcsjDto jcsjDto : jclist.get("PCRMACHINE_ID")){
			if ("Gentier 96R".equals(jcsjDto.getCsmc())){
				wkmxDto.setMachineId(jcsjDto.getCsdm());
			}
		}
		//文库PCR模板的基础数据
		JcsjDto wkDto = new JcsjDto();
		wkDto.setCsdm("WK");
		wkDto.setJclb("PCR_TEMPLATE");		
		wkDto = jcsjService.getDtoByCsdmAndJclb(wkDto);
		if(wkDto == null) {
			wkDto = new JcsjDto();
		}
		WkglDto wkglDto = wkglService.getDtoById(wkmxDto.getWkid());
		mav.addObject("machineIdList", jclist.get("PCRMACHINE_ID"));//机器ID
		mav.addObject("wkmxDto",wkmxDto);
		mav.addObject("wkDto", wkDto);
		mav.addObject("wkglDto", wkglDto);
		return mav;
	}
	
	/**
	 * 文库列表生成PCR对接文档
	 */
	@RequestMapping("/library/pcrdockingfileSavePcr")
	@ResponseBody
	public Map<String,Object> pcrdockingfileSavePcr(WkmxDto wkmxDto) {
		Map<String,Object> map = new HashMap<>();
		//文库PCR模板的基础数据
		JcsjDto wkDto = new JcsjDto();
		wkDto.setCsdm("WK");
		wkDto.setJclb("PCR_TEMPLATE");		
		wkDto = jcsjService.getDtoByCsdmAndJclb(wkDto);
		if(wkDto == null) {
 			map.put("status","fail");
 			map.put("message","文库的PCR模板信息不存在");
 			return map;
 		}
		//文库PCR模板的附件存放信息
		FjcfbDto wk_fjDto=new FjcfbDto();
		wk_fjDto.setYwid(wkDto.getCsid());
		wk_fjDto.setYwlx(BusTypeEnum.IMP_PCR_TEMEPLATE.getCode());
		List<FjcfbDto> wk_fj=fjcfbService.selectFjcfbDtoByYwidAndYwlx(wk_fjDto);
		//文库存放路径解码
		DBEncrypt p = new DBEncrypt();
		String wk = wk_fj.get(0).getWjlj();
		String wjlj = p.dCode(wk);//检查一下文件路径
		
		List<Plate> listPlate = new ArrayList<>();
 		GenerateFileForPCR tool = new GenerateFileForPCR();	
 		List<WkmxDto> list = wkmxService.getDtoForPcrReady(wkmxDto);
 		String createfilepath = null;
 		if(list.size() == 0) {
 			map.put("status","fail");
 			map.put("message","数据为0条");
 			return map;
 		}else {
 			for(WkmxDto wkmxdto: list) {
 				Plate plate = new Plate();
// 				plate.setSampleUid(wkmxdto.getYbbh());
				plate.setSampleName(wkmxdto.getYbbh());
 				plate.setXh(wkmxdto.getXh());
 				plate.setType("UNK");
 				String[] channelset = {"10","00","00","00","00","00"};//FAM,ROX{"10","00","31","00","00","00"}
 				plate.setChannelIsEnable(channelset);
// 				plate.setSampleName(wkmxdto.getYblxmc());
 				listPlate.add(plate);
 			}
 			try {
				//生产文件并返回生产文件路径
				createfilepath = tool.createFile(exportFilePath+BusTypeEnum.IMP_PCR_TEMEPLATE.getCode()+"/",wkmxDto.getMachineId(),wkmxDto.getWkmc(), wjlj ,listPlate);
				//上传文件到ftp
				FTPClient ftp = FtpUtil.connectDir(wkmxDto.getSrlj(), addr, pcrport, username, password);
				File file1 = new File(createfilepath);
				FtpUtil.upload(file1,ftp);
 			} catch (IOException e) {
 				e.printStackTrace();
				log.error(e.getMessage());
 	 			map.put("status","fail");
 	 			map.put("message",e.getMessage());
 	 			return map;
 			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				map.put("status","fail");
				map.put("message",e.getMessage());
			}
			map.put("status","success");
	 		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
	 		
	 		map.put("wjm", createfilepath != null ? createfilepath.substring(createfilepath.lastIndexOf("/") + 1) : null);
	 		map.put("wjlj",wjlj);//模板文件路径
	 		map.put("newWjlj",createfilepath);//生成的文件的路径
 			return map;
 		}		
	}
	
	
	/**
	 * PCR对接文件下载
	 */
	@RequestMapping(value="/file/pagedataPredownPcrFile")
	public String predownloadFile(HttpServletRequest request,HttpServletResponse response){
		String wjm = request.getParameter("wjm");
		String newWjlj = request.getParameter("newWjlj");
		File downloadFile = new File(newWjlj);
		if(wjm != null){
			wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
		}
		response.setHeader("content-type", "application/octet-stream");
		response.setContentLength((int) downloadFile.length());
        //指明为下载
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + wjm);
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
        try {
        	iStream = new FileInputStream(newWjlj);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
			log.error(e.getMessage());
        }
        try {
            if(bis!=null)
                bis.close();
            if(os != null)
            {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
			log.error(e.getMessage());
        }
        return null;
	}
	
	/**
	 * 上传上机表页面
	 */
	@RequestMapping("/library/uploadcomputerwatchWk")
	public ModelAndView uploadComputerWatch(WkglDto wkglDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_uploadComputerWatch");
		WksjglDto wksjglDto=new WksjglDto();
		wksjglDto.setWkid(wkglDto.getWkid());
		wksjglDto=wksjglService.getDtoByWkid(wksjglDto);
		//获取测序仪信息
		List<CxyxxDto> cxyxxlist=cxyxxService.getDtoList(new CxyxxDto());
		for (CxyxxDto cxyxxDto : cxyxxlist) {
			if(cxyxxDto.getCxybh()!=null&& !Objects.equals(cxyxxDto.getCxybh(), "")){
				if((cxyxxDto.getCxybh()).length()>3){
					cxyxxDto.setMc(cxyxxDto.getMc()+"-"+ ((cxyxxDto.getCxybh()).substring((cxyxxDto.getCxybh()).length()-3)));
				}
				else {
					cxyxxDto.setMc(cxyxxDto.getMc()+"-"+cxyxxDto.getCxybh());
				}
			}
		}
		mav.addObject("cxyxxlist",cxyxxlist);
		List<WksjmxDto> wksjmxList;
		WksjmxDto wksjmxDto=new WksjmxDto();
		String cxycskz3="";//测序仪cskz3
		String sfqyjyh="";//是否启用均一化
		String nddw="";//浓度单位
		String tjqz="";//体积前缀
		//如果是第一次上机,从文库导出获取
		if(wksjglDto==null) {
			WksjglDto wksjglDto_new = new WksjglDto();
			wksjmxDto.setWkid(wkglDto.getWkid());
			wksjmxList=wksjmxService.getListForWksj(wksjmxDto);
			WkglDto dto = wkglService.getDtoById(wkglDto.getWkid());
			if (StringUtil.isNotBlank(dto.getYqlx())){
				JcsjDto cxy = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode(), dto.getYqlx());
				if (cxy!=null){
					CxyxxDto cxyxxDto_mc = new CxyxxDto();
					cxyxxDto_mc.setMc(cxy.getCsmc());
					CxyxxDto cyxxx = cxyxxService.getDto(cxyxxDto_mc);
					wksjglDto_new.setCxy(cyxxx.getMc());
					wksjglDto_new.setCxyid(cyxxx.getCxyid());
					cxycskz3=cxy.getCskz3();
				}
			}
			mav.addObject("wksjglDto",wksjglDto_new);
		}else {
			//如果不是则从文库上机明细表获取
			wksjmxDto.setWksjid(wksjglDto.getWksjid());
			wksjmxList=wksjmxService.getDtoList(wksjmxDto);
			mav.addObject("wksjglDto",wksjglDto);
			cxycskz3=wksjglDto.getCxycskz3();
		}
		if(StringUtil.isNotBlank(cxycskz3)){
			com.alibaba.fastjson.JSONObject jsoncskz3=JSON.parseObject(cxycskz3);
			sfqyjyh=jsoncskz3.getString("MERGE_CONC");
			nddw=jsoncskz3.getString("CONC_UNIT");
			tjqz=jsoncskz3.getString("SETTING_VOLUMN");
		}
		mav.addObject("sfqyjyh",sfqyjyh);
		//获取芯片列表
		boolean active=true;//active=True时不显示被隐藏的芯片
		boolean can_upload=true;//是否仅显示可上传上机表的芯片

		try {
			String access_token=wkglDto.getAccess_token();
			RestTemplate restTemplate=new RestTemplate();
			//获取生信部芯片列表
			String sluglisturl = "http://bcl.matridx.top/BCL/api/sequence/";
			@SuppressWarnings("unchecked")
			Map<String,Object> t_result = restTemplate.getForObject(sluglisturl +"?active="+active+"&can_upload="+can_upload+"&access_token="+access_token,Map.class);
			String xpstr= null;
			if (t_result != null) {
				xpstr = JSON.toJSONString(t_result.get("results"));
			}
			List<WksjglDto> xplist = JSONArray.parseArray(xpstr,WksjglDto.class);
			mav.addObject("xplist",xplist);
		}catch(Exception e) {
			log.error(e.toString());
			mav.addObject("xplist",new ArrayList<WksjglDto>());
		}
		if(wksjmxList!=null && wksjmxList.size()>0){
			mav.addObject("length",wksjmxList.size());
			List<JcsjDto> jcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
			List<JcsjDto> zkjcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.QUALITY_SAMPLE_SETTING.getCode());
			for(WksjmxDto dto:wksjmxList){
				String wkbm=dto.getWkbm();
				if(wksjglDto==null){
					if(!wkbm.contains("-DNA")&&!wkbm.contains("-RNA")&&!wkbm.contains("-Onco")
					&& (!"0".equals(sfqyjyh) || StringUtil.isBlank(sfqyjyh))){
						Optional<JcsjDto> jcsjDto= Optional.of(new JcsjDto());
						if(dto.getNbbm()!=null && (dto.getNbbm().startsWith("NC-") || dto.getNbbm().startsWith("PC-")|| dto.getNbbm().startsWith("DC-"))){
							jcsjDto=zkjcsjList.stream().filter(item->item.getCsdm().equals(dto.getNbbm())).findFirst();
						}else{
							String nbbm=dto.getNbbm();
							if(StringUtil.isNotBlank(nbbm) ) {
								String lastNbbm = nbbm.substring(nbbm.length() - 1, nbbm.length());
								jcsjDto = jcsjList.stream().filter(item -> item.getCsdm().equals(lastNbbm)).findFirst();
								if (jcsjDto.isEmpty()) {//若没有匹配成功，则默认为该标本的样本类型为其他
									jcsjDto = jcsjList.stream().filter(item -> item.getCsdm().equals("XXX")).findFirst();
								}
							}else{
								jcsjDto = jcsjList.stream().filter(item -> item.getCsdm().equals("XXX")).findFirst();
							}
						}
						if(!jcsjDto.isEmpty()&&StringUtil.isNotBlank(jcsjDto.get().getCskz3())) {
							com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(jcsjDto.get().getCskz3());
							if(jsonObject!=null){
								if(StringUtil.isNotBlank(tjqz)){//若体积前缀不为空，则拼接完全的key，从微信标本cskz3获取体积
									if(wkbm.contains("-TBtNGS")){
										dto.setTj(jsonObject.getString(tjqz+"_TBT_VOLUMN"));
									}else{
										dto.setTj(jsonObject.getString(tjqz+"_VOLUMN"));
									}
								}else{
									if(wkbm.contains("-TBtNGS")){
										dto.setTj(jsonObject.getString("TBT_VOLUMN"));
									}else{
										dto.setTj(jsonObject.getString("DEFAULT_VOLUMN"));
									}
								}
							}
							if(StringUtil.isNotBlank(nddw)){
								dto.setNddw(nddw);
							}
						}
						dto.setWknd("-1");
						dto.setDlnd("-1");
					}
				}
				if(StringUtil.isNotBlank(dto.getFcjcdm())){
					if("ADDDETECT".equals(dto.getFcjcdm())){
						dto.setYy("备注：加测申请");
					}else if("RECHECK".equals(dto.getFcjcdm())){
						dto.setYy("备注：复测申请");
					}else if("REM".equals(dto.getFcjcdm())){
						dto.setYy("备注：加测去人源申请");
					}else if("PK".equals(dto.getFcjcdm())){
						dto.setYy("备注：PK申请");
					}else if("LAB_RECHECK".equals(dto.getFcjcdm())){
						dto.setYy("备注：实验室复测申请");
					}else if("LAB_REM".equals(dto.getFcjcdm())){
						dto.setYy("备注：实验室加测去人源申请");
					}else if("LAB_ADDDETECT".equals(dto.getFcjcdm())){
						dto.setYy("备注：实验室加测申请");
					}
				}
				String xmdm = dto.getXmdm();
				//针对项目代码为空的情况，需要做一下特殊处理
				if(StringUtil.isBlank(xmdm) && StringUtil.isNotBlank(dto.getNbbh())){
					//根据内部编号和后缀和内部编号重新查找送检实验管理表，也就是文库明细没有添加正确的情况下
					Map<String, String> params = new HashMap<>();
					StringBuffer tsb_nbbh = new StringBuffer("");
					String t_hzm = "";
					// 非NC，PC，DC的情况下截取后缀信息
					if(dto.getNbbh()!=null && (!dto.getNbbh().startsWith("NC-") && !dto.getNbbh().startsWith("PC-")&& !dto.getNbbh().startsWith("DC-"))){
						String[] s_exp_nbbh = dto.getNbbh().split("--");
						String[] s_nbbh = s_exp_nbbh[0].split("-");
						for(int i= s_nbbh.length-1;i>=0;i--){
							String s_info = s_nbbh[i];

							List<JcsjDto> list_library_suffix = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.LIBRARY_SUFFIX.getCode());
							boolean isFind = false;
							for(JcsjDto jcsjDto : list_library_suffix){
								if(jcsjDto.getCsdm().equals(s_info)){
									isFind = true;
									t_hzm = jcsjDto.getCsdm();
									break;
								}
							}
							if(isFind){
								for(int j= 0;j < i;j++){
									if(j>0)
										tsb_nbbh.append("-");
									tsb_nbbh.append(s_nbbh[j]);
								}
								break;
							}
						}
						String t_nbbh = tsb_nbbh.toString();
						if(StringUtil.isBlank(t_nbbh)){
							t_nbbh = dto.getNbbh();
						}
						params.put("nbbm", t_nbbh);
						params.put("hzmc", t_hzm);
						List<Map<String, String>> l_result = wkmxService.getXmdmFromSjsyByWk(params);
						boolean isFind = false;
						if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
							dto.setXmdm(l_result.get(0).get("kzcs7"));
							dto.setSyglid(l_result.get(0).get("syglid"));
							isFind = true;
						}
						// 如果还未找到，则根据内部编号的项目和后缀查找信息对应表，获取第一条数据的项目代码
						if (!isFind) {
							l_result = wkmxService.getXmdmFromSjxmByWk(params);

							if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
								dto.setXmdm(l_result.get(0).get("kzcs7"));
								//dto.setSyglid("getXmdmFromSjxmByWk");
								isFind = true;
							}
						}
						// 如果还未找到，则根据内部编号查找送检实验管理表，获取第一条数据的项目代码
						if (!isFind) {
							params.put("hzmc", null);
							l_result = wkmxService.getXmdmFromSjsyByWk(params);

							if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
								dto.setXmdm(l_result.get(0).get("kzcs7"));
								dto.setSyglid(l_result.get(0).get("syglid"));
								isFind = true;
							}
						}
						// 如果还未找到，则根据后缀查找信息对应表，获取一条数据的项目代码
						if (!isFind) {
							params.put("hzmc", t_hzm);
							l_result = wkmxService.getXmdmFromXxdyByWk(params);

							if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
								dto.setXmdm(l_result.get(0).get("kzcs7"));
								//dto.setSyglid("getXmdmFromXxdyByWk");
								isFind = true;
							}
						}
					}
				}
			}
		}
		mav.addObject("wksjmxList", wksjmxList);
		mav.addObject("wkglDto", wkglDto);
		Map<String, Object> poolingInfo = wkglService.getPoolingInfo(wkglDto);
		if (poolingInfo == null || poolingInfo.get("cskz1") == null){
			Object detectionExtend=redisUtil.hget("matridx_xtsz","pooling.detection.extend");
			if(detectionExtend!=null){
				com.alibaba.fastjson.JSONObject job= com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detectionExtend));
				mav.addObject("poolingjson",job.getString("szz"));
			}
		}else {
			mav.addObject("poolingjson", poolingInfo.get("cskz1"));
		}
		return mav;

	}

	/**
	 * 将文库上机信息上传至生信接口，若返回成功信息，在本地保存一份数据，否则不保存
	 */
	@RequestMapping("/library/uploadcomputerwatchSaveWk")
	@ResponseBody
	public Map<String,Object> uploadSaveComputerWatch(WksjglDto wksjglDto){
        User user =getLoginInfo();
		wksjglDto.setLrry(user.getYhid());
		wksjglDto.setXgry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		try {
			//核验接头1和接头2是否错误
			JSONObject wksjmx_json=JSONObject.fromObject(wksjglDto.getWksj_Json());
			String data_str=wksjmx_json.getString("data");
			List<Map<String, String>> wksjmxmaps= (List<Map<String, String>>) net.sf.json.JSONArray.toCollection(net.sf.json.JSONArray.fromObject(data_str),Map.class);
			boolean isok = true;
			List<WkmxDto> wkxxlist = new ArrayList<>();
			if(wksjmxmaps!=null && wksjmxmaps.size()>0){
				//增加核验
				for (Map<String, String> wksjmxmap : wksjmxmaps) {
					String[] uids = wksjmxmap.get("uid").split("-");
					String jtindex = "";
					for (int j = uids.length - 1; j >= 1; j--) {
						if (uids[j].length() == 8) {
							jtindex = uids[j - 1];
							break;
						}
					}
					String index = wksjmxmap.get("index");
					String index2 = wksjmxmap.get("index2");
					if (StringUtil.isBlank(jtindex)) {
						//未查找到8位日期字符串，故文库编号中找不到接头信息
						isok = false;
					} else {
						if (!((index.contains(jtindex) || index.contains("NA")) && (index2.contains(jtindex) || index2.contains("NA") || StringUtil.isBlank(index2))) && !"NA".equals(index2) && !"M99".equals(index2)) {//校验接头1和接头2
							isok = false;
						}
					}
					if (StringUtil.isNotBlank(wksjmxmap.get("syglid"))){
						WkmxDto wkmxDto = new WkmxDto();
						wkmxDto.setXgry(user.getYhid());
						wkmxDto.setSyglid(wksjmxmap.get("syglid"));
						wkxxlist.add(wkmxDto);
					}
				}
			}
			WkmxDto wkmxDto = new WkmxDto();
			wkmxDto.setXgry(user.getYhid());
			boolean updateDetectionDate = wkmxService.updateDetectionDate(wkmxDto,wkxxlist);
			if (isok){
				//如果点保存，只保存到OA，不上传到生信
				if("1".equals(wksjglDto.getBcbj())){
					boolean isSuccess=wksjglService.addSaveWksj(wksjglDto);
					map.put("status", isSuccess ? "success" : "fail");
					map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
							: xxglService.getModelById("ICOM00002").getXxnr());
				}else{
					WkglDto t_wkglDto = wkglService.getDtoById(wksjglDto.getWkid());//更新文库上机上传时间
					if(t_wkglDto==null && !StatusEnum.CHECK_PASS.getCode().equals(t_wkglDto.getZt())){
						map.put("status", "fail");
						map.put("message", "请先点击文库的确认按钮对实验信息进行更改后再上传上机表!");
						return map;
					}

					boolean isSuccess=wksjglService.addSaveWksj(wksjglDto);
					WksjmxDto wksjmxDto=new WksjmxDto();
					wksjmxDto.setXpm(wksjglDto.getXpmc());
					List<WksjmxDto> list = wksjmxService.getWkbmByXpm(wksjmxDto);
					if(list!=null&&list.size()>0){
						List<String> ids=new ArrayList<>();
						for(WksjmxDto dto:list){
							boolean flag=false;
							if (wksjmxmaps != null) {
								for (Map<String, String> wksjmxmap : wksjmxmaps) {
									String uid = wksjmxmap.get("uid");
									if (uid.equals(dto.getWkbm())) {
										flag = true;
										break;
									}
								}
							}
							if(!flag){
								ids.add(dto.getWkcxid());
							}
						}
						if(ids.size()>0){
							wksjmxDto.setIds(ids);
							wksjmxService.updateByWkcxids(wksjmxDto);
						}
					}
					if(isSuccess){
						log.error("文库上报信息:" + wksjglDto.getWksjid() + ":" + wksjglDto.getWksj_Json());
						//处理json中的合并浓度，进行换算
						dealConUnit(wksjglDto);
						//上传上机表至生信接口
						String uploadComputerWatchUrl = "http://bcl.matridx.top/BCL/api/library/upload/";
						@SuppressWarnings("unchecked")
						Map<String,String> backresult= (Map<String,String>) restTemplate.postForObject(
								uploadComputerWatchUrl +"?access_token="+wksjglDto.getAccess_token(),
								JSONObject.fromObject(wksjglDto.getWksj_Json()),Map.class);
						String status= null;
						if (backresult != null) {
							status = backresult.get("status");
						}
						if("OK".equals(status)){
							WkglDto wkglDto=new WkglDto();
							wkglDto.setWkid(wksjglDto.getWkid());
							wkglService.updateSjscsj(wkglDto);//更新文库上机上传时间
							map.put("status", "success");
							map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
						}else{
							map.put("status", "success");
							map.put("message", "生信部保存时出错! 报错信息：" + com.alibaba.fastjson.JSONObject.toJSONString(backresult));
						}
					}else{
						map.put("status", "fail");
						map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
					}
				}
				if(StringUtil.isNotBlank(wksjglDto.getCxy()) || StringUtil.isNotBlank(wksjglDto.getCxyid())){
					WkglDto wkglDto = new WkglDto();
					wkglDto.setWkid(wksjglDto.getWkid());
					String cyxmc = wksjglDto.getCxy();
					if (StringUtil.isBlank(wksjglDto.getCxy())){
						CxyxxDto cxyxxDto = new CxyxxDto();
						cxyxxDto.setCxyid(wksjglDto.getCxyid());
						CxyxxDto dto = cxyxxService.getDto(cxyxxDto);
						cyxmc = dto.getMc();
					}
					List<JcsjDto> cxyList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());
					for (JcsjDto cyx : cxyList) {
						if (cyx.getCsmc().equals(cyxmc)){
							wkglDto.setYqlx(cyx.getCsid());
							wkglDto.setXgry(user.getYhid());
							wkglService.updateYqlx(wkglDto);
						}
					}
				}
			}else {
				map.put("status", "fail");
				map.put("message", "确认接头1和接头2是否填写正确!");
			}
		} catch (Exception e) {
			map.put("status","fail");
			map.put("message",e.getMessage());
			log.error(e.getMessage());
		}
		return map;
	}

    /**
     * 创建任务单
     * @param request
     * @return
     */
	@RequestMapping("/library/pagedataCreateTask")
	@ResponseBody
	public Map<String,Object> createTask(HttpServletRequest request){
		Map<String,Object>map=new HashMap<>();
		String sjids=request.getParameter("sjids");
		List<String>sjidList=Arrays.asList(sjids.split(","));
		boolean flag=wkmxService.createTaskReda(sjidList);
		map.put("status",flag);
		return map;
	}

	public void dealConUnit(WksjglDto wksjglDto) throws Exception {
		CxyxxDto cxyxxDto=new CxyxxDto();
		cxyxxDto.setCxyid(wksjglDto.getCxyid());
		cxyxxDto=cxyxxService.getDto(cxyxxDto);
		String xs="1";
		if(StringUtil.isNotBlank(cxyxxDto.getCxycskz3())){
			com.alibaba.fastjson.JSONObject jsoncskz3=JSON.parseObject(cxyxxDto.getCxycskz3());
			if(StringUtil.isNotBlank(jsoncskz3.getString("CON_COE")) && jsoncskz3.getString("CON_COE").replaceAll(".","").chars().allMatch(Character::isDigit)){
				xs=jsoncskz3.getString("CON_COE");
			}
		}
		JSONObject wksjmx_json=JSONObject.fromObject(wksjglDto.getWksj_Json());
		String data_str=wksjmx_json.getString("data");
		List<Map<String, String>> wksjmxmaps= (List<Map<String, String>>) net.sf.json.JSONArray.toCollection(net.sf.json.JSONArray.fromObject(data_str),Map.class);
		if(!CollectionUtils.isEmpty(wksjmxmaps)){
			for(Map<String, String> wksjmx:wksjmxmaps){
				if("ng/μL".equals(wksjmx.get("original_con_unit")) && StringUtil.isNotBlank(wksjmx.get("con.sum"))){
					if(!wksjmx.get("con.sum").replaceAll(".","").chars().allMatch(Character::isDigit))
						throw new Exception(String.valueOf(wksjmx.get("con.sum"))+"不是一个数字");
					BigDecimal hbnd=new BigDecimal(wksjmx.get("con.sum"));
                    //ng/μL与pm的换算公式为合并浓度*系数
					try{
						hbnd=hbnd.multiply(new BigDecimal(xs)).setScale(2, RoundingMode.HALF_UP);
					}catch(Exception e){
						//若设置的系数不是数字，则默认为1
						hbnd=hbnd.multiply(new BigDecimal("1")).setScale(2, RoundingMode.HALF_UP);
					}

					wksjmx.put("con.sum",String.valueOf(hbnd));
				}
			}
			Gson gson = new Gson();
			wksjmx_json.put("data",gson.toJson(wksjmxmaps));
			wksjglDto.setWksj_Json(String.valueOf(wksjmx_json));
		}
	}

	@RequestMapping("/library/pagedataCxyxxByID")
	@ResponseBody
	public Map<String,Object> getCxyxxByID(CxyxxDto cxyxxDto){
		String wkid=cxyxxDto.getWkid();
		cxyxxDto=cxyxxService.getDto(cxyxxDto);
		cxyxxDto.setWkid(wkid);
		Map<String,Object> map= new HashMap<>();
		String sfjyh="";
		String nddw="";
		
		try{
			if(StringUtil.isNotBlank(cxyxxDto.getCxycskz3())){
				com.alibaba.fastjson.JSONObject jsoncskz3=JSON.parseObject(cxyxxDto.getCxycskz3());
				sfjyh=jsoncskz3.getString("MERGE_CONC");
				nddw=jsoncskz3.getString("CONC_UNIT");
			}
		}catch(Exception e){
			map.put("error","测序仪cskz3设置格式错误!");
			return map;
		}

		map=wksjglService.getWksjmxInfo(cxyxxDto,nddw,sfjyh,wkglService);

		return map;
	}

	@RequestMapping("/library/updatecxyData")
	@ResponseBody
	public Map<String,Object> updatecxyData(CxyxxDto cxyxxDto) throws BusinessException {
		boolean isSuccess=cxyxxService.updateCxyxx(cxyxxDto);
		Map<String,Object> map= new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr())
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 从文库上级管理和明细表获取上机数据
	 */
	@RequestMapping("/library/pagedataWksjxxData")
	@ResponseBody
	public Map<String,Object> getWksjxxData(WksjglDto wksjglDto){
		wksjglDto=wksjglService.getDtoByWkid(wksjglDto);
		String xs="1";//默认1
		if(StringUtil.isNotBlank(wksjglDto.getCxycskz3())){
			com.alibaba.fastjson.JSONObject jsoncskz3=JSON.parseObject(wksjglDto.getCxycskz3());
			if(StringUtil.isNotBlank(jsoncskz3.getString("CON_COE")) && jsoncskz3.getString("CON_COE").replaceAll(".","").chars().allMatch(Character::isDigit)){
				xs=jsoncskz3.getString("CON_COE");
				if(!xs.replaceAll(".","").chars().allMatch(Character::isDigit)){
					xs="1";//若设置的系数不是一个数字，则默认为1
				}
			}
		}
		Map<String,Object> map= new HashMap<>();
		List<WksjmxDto> wksjmxlist = new ArrayList<>();
		if (StringUtil.isNotBlank(wksjglDto.getWksjid())){
			WksjmxDto wksjmxDto = new WksjmxDto();
			wksjmxDto.setWksjid(wksjglDto.getWksjid());
			wksjmxDto.setXs(xs);
			wksjmxlist = wksjmxService.getDtoList(wksjmxDto);
		}
		map.put("wksjmxlist", wksjmxlist);
		return map;
	}
	//放入测试pcr仪器传输数据问题
		@RequestMapping("/library/sendLibrary")
		@ResponseBody
		public Map<String,Object> sendLibrary(WkmxDto wkmxDto) {
			Map<String,Object> map= new HashMap<>();
			boolean result=	wkmxService.sendLibrary(wkmxDto);

			map.put("status", result ? "success" : "fail");
			map.put("message", result ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
			return map;
		}

	/**
	 * 检测文库明细数据中此内部编号是否已经存在对应的提取码
	 */
	@RequestMapping("/library/pagedataCheck")
	@ResponseBody
	public  Map<String,Object> pagedataCheck(WkmxDto wkmxDto) {
		Map<String,Object> map= new HashMap<>();
		List<WkmxDto> wkmxlist = wkmxService.getWkmxByNbbh(wkmxDto);
		map.put("wkmxlist",wkmxlist);
		return map;
	}

	/**
	 * 根据nbbh查询文库明细
	 */
	@RequestMapping("/library/pagedataGetWkmx")
	@ResponseBody
	public  Map<String,Object> pagedataGetWkmx(WkmxDto wkmxDto) {
		Map<String,Object> map= new HashMap<>();
		WkmxDto dto= wkmxService.getDto(wkmxDto);
		//上机明细这边是从文库明细这边获取cskz7 ，从而得到 项目代码
		//当没有数据时，为了防止生信这边出错，需要强制给一个项目代码 20250716
		if(dto==null || StringUtil.isBlank(dto.getKzcs7())){
			try {
				if(dto == null)
					dto = new WkmxDto();
				String nbbh = wkmxDto.getNbbh();
				int symbolindex = nbbh.lastIndexOf("-");
				boolean isFind = false;
				if (symbolindex >= 0) {
					String t_nbbm = nbbh.substring(0, symbolindex);
					String t_hzm = nbbh.substring(symbolindex + 1);
					//根据内部编号和后缀和内部编号重新查找送检实验管理表，也就是文库明细没有添加正确的情况下
					Map<String, String> params = new HashMap<>();
					params.put("nbbm", t_nbbm);
					params.put("hzmc", t_hzm);
					List<Map<String, String>> l_result = wkmxService.getXmdmFromSjsyByWk(params);

					if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
						dto.setKzcs7(l_result.get(0).get("kzcs7"));
						dto.setSyglid(l_result.get(0).get("syglid"));
						isFind = true;
					}
					// 如果还未找到，则根据内部编号的项目和后缀查找信息对应表，获取第一条数据的项目代码
					if (!isFind) {
						l_result = wkmxService.getXmdmFromSjxmByWk(params);

						if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
							dto.setKzcs7(l_result.get(0).get("kzcs7"));
							//dto.setSyglid("getXmdmFromSjxmByWk");
							isFind = true;
						}
					}
					// 如果还未找到，则根据内部编号查找送检实验管理表，获取第一条数据的项目代码
					if (!isFind) {
						params.put("hzmc", null);
						l_result = wkmxService.getXmdmFromSjsyByWk(params);

						if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
							dto.setKzcs7(l_result.get(0).get("kzcs7"));
							dto.setSyglid(l_result.get(0).get("syglid"));
							isFind = true;
						}
					}
					// 如果还未找到，则根据后缀查找信息对应表，获取一条数据的项目代码
					if (!isFind) {
						params.put("hzmc", t_hzm);
						l_result = wkmxService.getXmdmFromXxdyByWk(params);

						if (l_result != null && !l_result.isEmpty() &&StringUtil.isNotBlank(l_result.get(0).get("kzcs7"))) {
							dto.setKzcs7(l_result.get(0).get("kzcs7"));
							//dto.setSyglid("getXmdmFromXxdyByWk");
							isFind = true;
						}
					}
				}
			} catch (Exception e) {
				log.error("因没有文库代码，采用特殊处理获取文库代码时发生异常。异常信息为" + e.getMessage());
			}
		}
		map.put("wkmxDto",dto);
		String cxycskz3="";//测序仪cskz3
		String sfqyjyh="";//是否启用均一化
		String nddw="";//浓度单位
		String tjqz="";//体积前缀
		String tj="";
		String wknd="";
		String dlnd="";
		com.alibaba.fastjson.JSONObject jsoncskz3=null;

		if(StringUtil.isNotBlank(wkmxDto.getCxyid())) {
			CxyxxDto cxyxxDto = new CxyxxDto();
			cxyxxDto.setCxyid(wkmxDto.getCxyid());
			CxyxxDto cxy = cxyxxService.getDto(cxyxxDto);

			if(cxy !=null) {
				cxycskz3 = cxy.getCxycskz3();
				if (StringUtil.isNotBlank(cxycskz3)) {
					try {
						jsoncskz3 = JSON.parseObject(cxycskz3);
						sfqyjyh = jsoncskz3.getString("MERGE_CONC");
						tjqz = jsoncskz3.getString("SETTING_VOLUMN");
					}catch (Exception e) {
						//若设置的系数不是数字，则默认为1
						sfqyjyh = "1";
					}
				}
			}
		}
		map.put("sfqyjyh",sfqyjyh);
		List<JcsjDto> jcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<JcsjDto> zkjcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.QUALITY_SAMPLE_SETTING.getCode());
		if(!wkmxDto.getWksxbm().contains("-RNA")&&!wkmxDto.getWksxbm().contains("-DNA")&&!wkmxDto.getWksxbm().contains("-Onco")
				&& (!"0".equals(sfqyjyh) || StringUtil.isBlank(sfqyjyh))){
			Optional<JcsjDto> jcsjDto= Optional.of(new JcsjDto());
			if(wkmxDto.getNbbh().startsWith("NC-") || wkmxDto.getNbbh().startsWith("PC-")|| wkmxDto.getNbbh().startsWith("DC-")){
				jcsjDto=zkjcsjList.stream().filter(item->item.getCsdm().equals(wkmxDto.getNbbh())).findFirst();
			}else{
				String[] nbbm=wkmxDto.getNbbh().split("-");
				String lastNbbm=nbbm[0].substring(nbbm[0].length()-1,nbbm[0].length());
				jcsjDto=jcsjList.stream().filter(item->item.getCsdm().equals(lastNbbm)).findFirst();
				if(jcsjDto.isEmpty()){//若没有匹配成功，则默认为该标本的样本类型为其他
					jcsjDto=jcsjList.stream().filter(item->item.getCsdm().equals("XXX")).findFirst();
				}
			}
			if(!jcsjDto.isEmpty()&&StringUtil.isNotBlank(jcsjDto.get().getCskz3())) {
				com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(jcsjDto.get().getCskz3());
				if(jsonObject!=null){
					if(StringUtil.isNotBlank(tjqz)){
						if(wkmxDto.getWksxbm().contains("-TBtNGS")){
							tj=jsonObject.getString(tjqz+"_TBT_VOLUMN");
						}else{
							tj=jsonObject.getString(tjqz+"_VOLUMN");
						}
					}else{
						if(wkmxDto.getWksxbm().contains("-TBtNGS")){
							tj=jsonObject.getString("TBT_VOLUMN");
						}else{
							tj=jsonObject.getString("DEFAULT_VOLUMN");
						}
					}
				}
			}
			wknd="-1";
			dlnd="-1";
			if(jsoncskz3!=null)
				nddw=jsoncskz3.getString("CONC_UNIT");
		}
		map.put("nddw",nddw);
		map.put("tj",tj);
		map.put("wknd",wknd);
		map.put("dlnd",dlnd);
		return map;
	}

	/**
	 * 实验列表文库新增
	 */
	@RequestMapping("/library/libraryExperiment")
	@ResponseBody
	public ModelAndView libraryExperiment(WkmxDto wkmxDto) {
		User user = getLoginInfo();
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.TEST_PLACE.getCode(),
				PersonalSettingEnum.SEQUENCER_CODE.getCode(),
				PersonalSettingEnum.REAGENT_SELECT.getCode(),
				PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode(),
				PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		//查个人设置检查单位
		GrszDto grszxx = grszDtoMap.get(PersonalSettingEnum.TEST_PLACE.getCode());
		GrszDto grszxx_machine = grszDtoMap.get(PersonalSettingEnum.SEQUENCER_CODE.getCode());
		GrszDto grszxx_reagent = grszDtoMap.get(PersonalSettingEnum.REAGENT_SELECT.getCode());
		//宏基因组DNA建库试剂盒个人设置
		GrszDto grszxx_o = grszDtoMap.get(PersonalSettingEnum.REAGENT_ONE_SETTINGS.getCode());
		if(grszxx_o!=null) {
			wkmxDto.setSjph1(grszxx_o.getSzz());
		}
		//逆转录试剂盒个人设置
		GrszDto grszxx_t = grszDtoMap.get(PersonalSettingEnum.REAGENT_TWO_SETTINGS.getCode());
		if(grszxx_t!=null) {
			wkmxDto.setSjph2(grszxx_t.getSzz());
		}
		ModelAndView mav = new ModelAndView("experiment/library/library_add");
		WkglDto wkglDto = new WkglDto();
		String date = DateUtils.format(new Date(), "yyyy-MM-dd");
		String t_date = date.replaceAll("-", "");
		wkglDto.setLrsj(date);
		wkglDto = wkglService.getCountByDay(wkglDto);
		String wkmc;
		if (Integer.parseInt(wkglDto.getCount()) < 9 && Integer.parseInt(wkglDto.getCount()) >= 0) {
			wkmc = t_date + "0" + (Integer.parseInt(wkglDto.getCount()) + 1);
		} else {
			wkmc = t_date + (Integer.parseInt(wkglDto.getCount()) + 1);
		}
		wkmxDto.setWkmc(wkmc);
		JcsjDto jcsjDto = new JcsjDto();
		if (grszxx != null) {
			jcsjDto = jcsjService.getDtoById(grszxx.getSzz());
		}
		JcsjDto jcsjDto_machine = new JcsjDto();
		if (grszxx_machine != null && StringUtil.isNotBlank(grszxx_machine.getSzz())) {
			jcsjDto_machine = jcsjService.getDtoById(grszxx_machine.getSzz());
		}
		JcsjDto jcsjdto_reagent = new JcsjDto();
		if (grszxx_reagent != null && StringUtil.isNotBlank(grszxx_reagent.getSzz())) {
			jcsjdto_reagent = jcsjService.getDtoById(grszxx_reagent.getSzz());
		}
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECTION_UNIT,
						BasicDataTypeEnum.SEQUENCER_CODE,
						BasicDataTypeEnum.REAGENT_SELECT });
		mav.addObject("jcdwList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));// 检测单位
		mav.addObject("yqlxList", jclist.get(BasicDataTypeEnum.SEQUENCER_CODE.getCode()));// 测序仪
		mav.addObject("sjxzList", jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode()));// 试剂选择
		mav.addObject("sjxzJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.REAGENT_SELECT.getCode())));// 试剂选择json
		Object coefficient=redisUtil.hget("matridx_xtsz","pooling.coefficient");
		if(coefficient!=null){
			com.alibaba.fastjson.JSONObject job= com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(coefficient));
			mav.addObject("poolingCoefficient", job.getString("szz"));
		}
		wkmxDto.setFormAction("librarySaveExperiment");
		List<JcsjDto> jcdwList = jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<String> jcdws =new ArrayList<>();
		String tyid="";
		if(jcdwList!=null&&jcdwList.size()>0){
			for(JcsjDto dto:jcdwList){
				if("0z".equals(dto.getCsdm())){
					tyid=dto.getCsid();
					break;
				}
			}
		}
		if(!tyid.equals(jcsjDto.getCsid())){
			jcdws.add(jcsjDto.getCsid());
			jcdws.add(tyid);
		}
		if(jcdws.size()>0){
			wkmxDto.setJcdws(jcdws.toArray(new String[jcdws.size()]));
		}
		List<WkmxDto> infoBySyglids = wkmxService.getInfoBySyglids(wkmxDto);
		if(infoBySyglids!=null&&!infoBySyglids.isEmpty()){
			List<String> ids =new ArrayList<>();
			Map<String, List<WkmxDto>> listMap = infoBySyglids.stream().collect(Collectors.groupingBy(WkmxDto::getSjid));
			if (!CollectionUtils.isEmpty(listMap)){
				Iterator<Map.Entry<String, List<WkmxDto>>> entryIterator = listMap.entrySet().iterator();
				while (entryIterator.hasNext()) {
					Map.Entry<String, List<WkmxDto>> stringListEntry = entryIterator.next();
					String sjid = stringListEntry.getKey();
					ids.add(sjid);
				}
			}
			if(!ids.isEmpty()){
				TqmxDto tqmxDto=new TqmxDto();
				tqmxDto.setIds(ids);
				List<TqmxDto> dtoList = tqmxService.getDtoList(tqmxDto);
				if(dtoList!=null&&!dtoList.isEmpty()){
					Map<String, List<TqmxDto>> stringListMap = dtoList.stream().collect(Collectors.groupingBy(TqmxDto::getSjid));
					if (!CollectionUtils.isEmpty(stringListMap)){
						Iterator<Map.Entry<String, List<TqmxDto>>> entryIterator = stringListMap.entrySet().iterator();
						while (entryIterator.hasNext()) {
							Map.Entry<String, List<TqmxDto>> stringListEntry = entryIterator.next();
							String sjid = stringListEntry.getKey();
							for(WkmxDto dto:infoBySyglids){
								if(sjid.equals(dto.getSjid())){
									dto.setTqmxDtos(stringListEntry.getValue());
								}
							}
						}
					}
				}
			}
		}
		wkmxDto.setWkmx_json(JSON.toJSONString(infoBySyglids, SerializerFeature.DisableCircularReferenceDetect));
		mav.addObject("mrjcdw", jcsjDto);
		mav.addObject("mryqlx", jcsjDto_machine);
		mav.addObject("mrsjxz", jcsjdto_reagent);
		mav.addObject("wkmxDto", wkmxDto);
		mav.addObject("sjxsbj1", "1");
		mav.addObject("sjxsbj2", "1");
		return mav;
	}

	/**
	 * 实验列表文库新增保存
	 */
	@RequestMapping("/library/librarySaveExperiment")
	@ResponseBody
	public  Map<String,Object> librarySaveExperiment(WkmxDto wkmxDto,HttpServletRequest request) {
		return this.addSaveLibrary(wkmxDto,request);
	}

	/**
	 * 导出
	 */
	@RequestMapping("/library/exportLibrary")
	@ResponseBody
	public ModelAndView exportLibrary(WkmxDto wkmxDto) {
		ModelAndView mav = new ModelAndView("experiment/library/library_export");
		List<JcsjDto> wjbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DOCUMENTNUM.getCode());
		for (JcsjDto jcsjDto:wjbmlist){
			if (jcsjDto.getCsdm().equals("WK")){
				mav.addObject("wjbh", jcsjDto);
			}
		}
		List<WkmxDto> dtoList = wkmxService.getDtoList(wkmxDto);
		mav.addObject("wkmxDtos",JSON.toJSONString(dtoList));
		return mav;
	}

	/**
	 * pooling回传
	 * @param wkglDto
	 * @return
	 */
	@RequestMapping("/library/poolingupload")
	public ModelAndView poolingupload(WkglDto wkglDto){
		ModelAndView mav = new ModelAndView("experiment/library/pooling_upload");
		wkglDto=wkglService.getDtoById(wkglDto.getWkid());
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_POOLING_UPLOAD.getCode());
		fjcfbDto.setYwid(wkglDto.getWkid());
		List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("wkglDto",wkglDto);
		mav.addObject("ywlx",BusTypeEnum.IMP_LIBRARY_POOLING_UPLOAD.getCode());
		return mav;
	}

	@RequestMapping("/library/pagedataPoolingupload")
	@ResponseBody
	public Map<String,Object> savePoolingupload(WkglDto wkglDto){
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("status", "success");
		resultMap.put("message", "保存成功！");
		//保存附件
		if (!CollectionUtils.isEmpty(wkglDto.getFjids())) {
			for (int i = 0; i < wkglDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(wkglDto.getFjids().get(i), wkglDto.getWkid());
			}
		}else{
			resultMap.put("status", "fail");
			resultMap.put("message", "请上传附件");
		}
		return resultMap;
	}

	/**
	 * pooling导出
	 */
	@RequestMapping(value = "/library/poolingexport")
	public ModelAndView poolingexport(WkglDto wkglDto) {
		ModelAndView mav = new ModelAndView("experiment/library/pooling_export");
		Map<String, Object> map = wkglService.generatePoolingSetting(wkglDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(wkglDto.getWkid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_LIBRARY_POOLING_EXPORT.getCode());//已生成的pooling文件
		List<FjcfbDto> t_fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addAllObjects(map);
		mav.addObject("wkid", wkglDto.getWkid());
		mav.addObject("ywlx", fjcfbDto.getYwlx());
		mav.addObject("t_fjcfbDtos", t_fjcfbDtos);
		return mav;
	}

	/**
	 * 处理pooling表
	 */
	@RequestMapping(value="/library/pagedataDealPooling")
	@ResponseBody
	public Map<String, Object> pagedataDealPooling(WkglDto wkglDto,HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = wkglService.dealPooping(wkglDto,request);
		return map;
	}

	/**
	 * 处理pooling导出设置项
	 */
	@RequestMapping(value="/library/pagedataSavePoolingSetting")
	@ResponseBody
	public Map<String, Object> pagedataSavePoolingSetting(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		String json = request.getParameter("json");
		String yqlx = request.getParameter("yqlx");
		if (!StringUtil.isAnyBlank(yqlx,json)){
			JcsjDto cxy = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode(), yqlx);
			String cskz1 = cxy.getCskz1();
			Map<String,Object> parse = (Map<String, Object>) JSON.parse(json);
			String item = (String) parse.get("Item");
			String reagentName = (String) parse.get("ReagentName");
			try {
				if (StringUtil.isNotBlank(cskz1)){
					List<Map<String,Object>> jsonList = JSONArray.parseObject(cskz1, new TypeReference<List<Map<String, Object>>>() {});
					for (Map<String,Object> tempSetting : jsonList) {
						//匹配项目
						if (item.equals(tempSetting.get("Item")) && reagentName.equals(tempSetting.get("ReagentName"))){
							if (tempSetting.get("Coefficient") != null){
								parse.put("Coefficient",tempSetting.get("Coefficient"));
							}
							jsonList.set(jsonList.indexOf(tempSetting), parse);
							cxy.setCskz1(JSON.toJSONString(jsonList));
							break;
						}
					}
				}else {
					List<Map<String,Object>> jsonList = new ArrayList<>();
					jsonList.add(parse);
					cxy.setCskz1(JSON.toJSONString(jsonList));
				}
				jcsjService.updateJcsj(cxy);
				map.put("status","success");
				map.put("message","保存成功！");
				return map;
			} catch (Exception e) {
				map.put("status","fail");
				map.put("message","保存失败！"+e.getMessage());
				return map;
			}
		}
		map.put("status","fail");
		map.put("message","保存失败！");
		return map;
	}

	/**
	 * pooling导出取消
	 * @param wkglDto
	 * @return
	 */
	@RequestMapping("/library/pagedataPoolCancel")
	@ResponseBody
	public  Map<String,Object> pagedataPoolCancel(WkglDto wkglDto) {
		Map<String,Object> map= new HashMap<>();
		PoolingUtil poolingUtil = new PoolingUtil();
		poolingUtil.setPoolingRedisStatus(redisUtil, wkglDto.getWkid()+"_"+wkglDto.getTimezone(),"isCancel",null);
		boolean isCancel = poolingUtil.isStatusTrue(redisUtil, "isCancel", wkglDto.getWkid()+"_"+wkglDto.getTimezone());
		if (isCancel){
			map.put("cancel", true);
			map.put("result", true);
			map.put("msg",(String) redisUtil.hget("POOLING_EXPORT:" + wkglDto.getWkid()+"_"+wkglDto.getTimezone(), "message"));
			return map;
		}
		return map;
	}

	/**
	 * 检查pooling导出状态
	 * @param wkglDto
	 * @return
	 */
	@RequestMapping("/library/pagedataCheckPoolingStatus")
	@ResponseBody
	public  Map<String,String> pagedataCheckPoolingStatus(WkglDto wkglDto) {
		PoolingUtil poolingUtil = new PoolingUtil();
		Map<String, String> poolingStatus = poolingUtil.getPoolingRedisStatus(redisUtil, wkglDto.getWkid()+"_"+wkglDto.getTimezone());
		return poolingStatus;
	}

	/**
	 * pooling文件下载
	 * @param wkglDto
	 * @param response
	 */
	@RequestMapping("/library/pagedataPoolingFileDownload")
	@ResponseBody
	public void pagedataPoolingFileDownload(WkglDto wkglDto,HttpServletResponse response) {
		PoolingUtil poolingUtil  = new PoolingUtil();
		poolingUtil.poolingDownloadExport(redisUtil,wkglDto.getWkid(),wkglDto.getTimezone(),response);
	}

	@RequestMapping("/library/pagedataCxyList")
	@ResponseBody
	public Map<String,Object> pagedataCxyList(CxyxxDto cxyxxDto) {
		Map<String,Object> map=new HashMap<>();
		List<CxyxxDto> list=cxyxxService.getDtoByMcOrCxybh(cxyxxDto);
		map.put("cxylist",list);
		return map;
	}

	/**
	 * 删除并重新创建上机表信息
	 * @param wkglDto
	 * @return
	 */
	@RequestMapping("/library/pagedataRegenerationSjbxx")
	@ResponseBody
	public Map<String, Object> pagedataRegenerationSjbxx(WkglDto wkglDto) {
		Map<String, Object> map = new HashMap<>();
		WksjmxDto wksjmxDto = new WksjmxDto();
		wksjmxDto.setWkid(wkglDto.getWkid());
		List<WksjmxDto> wksjmxList = wksjmxService.getListForWksj(wksjmxDto);
		String cxycskz3="";//测序仪cskz3
		String sfqyjyh="";//是否启用均一化
		String tjqz="";//体积前缀
		String nddw="";
		com.alibaba.fastjson.JSONObject jsoncskz3=null;
		CxyxxDto cxyxxDto=new CxyxxDto();
		cxyxxDto.setCxyid(wkglDto.getCxy());
		CxyxxDto cxy=cxyxxService.getDto(cxyxxDto);
		cxycskz3=cxy.getCxycskz3();
		if(StringUtil.isNotBlank(cxycskz3)){
			jsoncskz3=JSON.parseObject(cxycskz3);
			sfqyjyh=jsoncskz3.getString("MERGE_CONC");
			nddw=jsoncskz3.getString("CONC_UNIT");
			tjqz=jsoncskz3.getString("SETTING_VOLUMN");
		}
		if(!CollectionUtils.isEmpty(wksjmxList)){
			WksjglDto wksjglDto=new WksjglDto();
			wksjglDto.setWkid(wkglDto.getWkid());
			wksjglDto=wksjglService.getDtoByWkid(wksjglDto);
			List<JcsjDto> jcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
			List<JcsjDto> zkjcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.QUALITY_SAMPLE_SETTING.getCode());
			for(WksjmxDto dto:wksjmxList){
				String wkbm=dto.getWkbm();
				if(wksjglDto==null){
					if(!wkbm.contains("-RNA")&&!wkbm.contains("-DNA")&&!wkbm.contains("-Onco")
							&& (!"0".equals(sfqyjyh) || StringUtil.isBlank(sfqyjyh))){
						Optional<JcsjDto> jcsjDto= Optional.of(new JcsjDto());
						if(dto.getNbbm().startsWith("NC-") || dto.getNbbm().startsWith("PC-")|| dto.getNbbm().startsWith("DC-")){
							jcsjDto=zkjcsjList.stream().filter(item->item.getCsdm().equals(dto.getNbbm().substring(0,dto.getNbbm().lastIndexOf("-")))).findFirst();
						}else{
							String nbbm=dto.getNbbm();
							String lastNbbm=nbbm.substring(nbbm.length()-1,nbbm.length());
							jcsjDto=jcsjList.stream().filter(item->item.getCsdm().equals(lastNbbm)).findFirst();
							if(jcsjDto.isEmpty()){//若没有匹配成功，则默认为该标本的样本类型为其他
								jcsjDto=jcsjList.stream().filter(item->item.getCsdm().equals("XXX")).findFirst();
							}
						}
						try {
							com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(jcsjDto.get().getCskz3());
							if(jsonObject!=null){
								if(StringUtil.isNotBlank(tjqz)){
									if(wkbm.contains("-TBtNGS")){
										dto.setTj(jsonObject.getString(tjqz+"_TBT_VOLUMN"));
									}else{
										dto.setTj(jsonObject.getString(tjqz+"_VOLUMN"));
									}
								}else{
									if(wkbm.contains("-TBtNGS")){
										dto.setTj(jsonObject.getString("TBT_VOLUMN"));
									}else{
										dto.setTj(jsonObject.getString("DEFAULT_VOLUMN"));
									}
								}
							}
							dto.setNddw(nddw);
							dto.setWknd("-1");
							dto.setDlnd("-1");
						}catch (Exception e){
							map.put("error","标本类型cskz3设置格式错误!");
							return map;
						}
					}
				}
			}
		}

		map.put("wksjmxList", wksjmxList);
		return map;
	}


	/**
	 * 下载
	 * @param wksjglDto
	 * @param response
	 */
	@RequestMapping("/library/pagedataChipFileDownload")
	@ResponseBody
	public void pagedataChipFileDownload(WksjglDto wksjglDto,HttpServletResponse response) {
		wksjglService.downloadChipExport(wksjglDto,response);
	}

	/**
	 * 获取系统设置里的文库试剂信息
	 * @param jcdwdm
	 * @param jcdwdm
	 * @param jcdwdm
	 * @return
	 */
	private List<Map<String, String>> getWksjList(String jcdwdm,Map<String,String> reagent_json_map) {
		//相应实验室的试剂设置
		XtszDto wksjtszDto = xtszService.getDtoById("reagent.extend.library." + jcdwdm);
		if(wksjtszDto==null || StringUtil.isBlank(wksjtszDto.getSzz())){
			//通用提取文库试剂
			wksjtszDto = xtszService.getDtoById("reagent.extend.library" );
		}
		if(wksjtszDto == null)
			wksjtszDto = new XtszDto();
		//页面文库试剂显示处理
		List<Map<String,String>> wksjList=new ArrayList<>();
		JSONArray jsonArray_wk=JSONArray.parseArray(wksjtszDto.getSzz());
		for(int i = 0; i < jsonArray_wk.size(); i++){
			com.alibaba.fastjson.JSONObject jsonObject=jsonArray_wk.getJSONObject(i);
			if(StringUtil.isNotBlank(jsonObject.getString("status"))&&"0".equals(jsonObject.getString("status"))){
				Map<String,String> wkMap=new HashMap<>();
				wkMap.put("title",jsonObject.getString("title"));
				wkMap.put("variable",jsonObject.getString("variable"));
				wkMap.put("type",jsonObject.getString("type"));
				wkMap.put("status",jsonObject.getString("status"));
				//wkMap.put("value",reagent_json_map.get(jsonObject.getString("variable")));
				if(reagent_json_map!= null && reagent_json_map.containsKey(jsonObject.getString("variable"))){
					wkMap.put("value",reagent_json_map.get(jsonObject.getString("variable")));
				}else{
					wkMap.put("value","");
				}
				wksjList.add(wkMap);
			}
		}

		return wksjList;
	}
}
