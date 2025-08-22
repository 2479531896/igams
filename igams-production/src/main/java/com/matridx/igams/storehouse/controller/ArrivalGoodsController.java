package com.matridx.igams.storehouse.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.service.svcinterface.ISczlglService;
import com.matridx.igams.production.service.svcinterface.ISczlmxService;
import com.matridx.igams.storehouse.service.svcinterface.IFhglService;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDhxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;

/**
 * 包含 共通页面
 * @author hmz
 *
 */
@Controller
@RequestMapping("/storehouse")
public class ArrivalGoodsController extends BaseBasicController{
	@Autowired
	private IDhxxService dhxxService;
	
	@Autowired
	private IHwxxService hwxxService;
	@Autowired
	private ISczlglService sczlglService;
	@Autowired
	private ISczlmxService sczlmxService;
	@Autowired
	private IJcsjService jcsjService;
	
	@Autowired
	private ICkxxService ckxxService;

	@Autowired
	private IFhglService fhglService;
	
	@Autowired
	private ICommonService commonService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	@Autowired
	IXxglService xxglService;

	@Autowired
	IFjcfbService fjcfbService;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	IWlglService wlglService;
	@Autowired
	IXtszService xtszService;

	/**
	 * 到货列表页面
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/pageListArrivalGoods")
	public ModelAndView pageListArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_list");
		dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.ARRIVAL_CATEGORY});
		mav.addObject("rklbList", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("dhlxList", jclist.get(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode()));// 到货类型
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("auditTypeEnum", AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 到货列表
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/pageGetListArrivalGoods")
	@ResponseBody
	public Map<String, Object> listArrivalGoods(DhxxDto dhxxDto) {
		List<DhxxDto> t_List = dhxxService.getPagedDtoList(dhxxDto);
		List<DhxxDto> zl_LIST = dhxxService.getJyzlAndGhzlList();

		for (DhxxDto value : t_List) {
			for (DhxxDto dto : zl_LIST) {
				if ((value.getDhid()).equals(dto.getDhid())) {
					value.setJyzl(dto.getJyzl());
					value.setGhzl(dto.getGhzl());

					if (StringUtils.isBlank(dto.getJyzl()) || Double.parseDouble(dto.getJyzl()) == 0.00) {
						//若查找出的借用总量为null或者0，则借用标记为0，未借用
						value.setJybj("0");
					} else {
						value.setJybj("1");
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("total", dhxxDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}
	/**
	 * 到货列表钉钉
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/minidataListArrivalGoodsDingTalk")
	@ResponseBody
	public Map<String, Object> minidataListArrivalGoodsDingTalk(DhxxDto dhxxDto) {
		List<DhxxDto> t_List = dhxxService.getPagedDtoListDingTalk(dhxxDto);
		List<DhxxDto> zl_LIST = dhxxService.getJyzlAndGhzlList();
		for (DhxxDto value : t_List) {
			for (DhxxDto dto : zl_LIST) {
				if ((value.getDhid()).equals(dto.getDhid())) {
					value.setJyzl(dto.getJyzl());
					value.setGhzl(dto.getGhzl());

					if (StringUtils.isBlank(dto.getJyzl()) || Double.parseDouble(dto.getJyzl()) == 0.00) {
						//若查找出的借用总量为null或者0，则借用标记为0，未借用
						value.setJybj("0");
					} else {
						value.setJybj("1");
					}
				}
			}
		}
		List<JcsjDto> dhlblist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode());
		List<JcsjDto> rklblist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INBOUND_TYPE.getCode());
		Map<String, Object> map = new HashMap<>();
		map.put("total", dhxxDto.getTotalNumber());
		map.put("rows", t_List);
		map.put("dhlblist",dhlblist);
		map.put("rklblist",rklblist);
		return map;
	}
	
	/**
	 * 到货审核列表
	 *
	 */
	@RequestMapping("/arrivalGoods/pageListAudit")
	public ModelAndView pageListAudit() {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 到货审核列表
	 *
	 */
	@RequestMapping("/arrivalGoods/pageGetListAudit")
	@ResponseBody
	public Map<String, Object> getListArrivalGoods_audit(DhxxDto dhxxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(dhxxDto);
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_ARRIVAL);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_PENDING);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(dhxxDto.getDqshzt())) {
			DataPermission.add(dhxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "dhxx", "dhid",
					auditTypes);
		} else {
			DataPermission.add(dhxxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "dhxx", "dhid",
					auditTypes);
		}
		DataPermission.addCurrentUser(dhxxDto, getLoginInfo(request));
		List<DhxxDto> listMap = dhxxService.getPagedAuditDhxx(dhxxDto);
		map.put("total", dhxxDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}
	
	/**
	 * 到货确认
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/confirmArrivalGoods")
	public ModelAndView confirmArrivalGoods(DhxxDto dhxxDto) {
		//调用查看方法，后续只需要维护一个
		Map<String,Object> map = minidataDingtalkviewArrivalGoods(dhxxDto);
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_view");
		mav.addObject("dhxxDto", map.get("dhxxDto"));
		mav.addObject("hwxxDtos", map.get("hwxxDtos"));
		mav.addObject("hwxx_json", JSON.toJSONString(map.get("hwxxDtos")));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 到货查看
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/pagedataViewArrivalGoods")
	public ModelAndView viewArrivalGoods(DhxxDto dhxxDto) {
		//调用查看方法，后续只需要维护一个
		Map<String,Object> map = minidataDingtalkviewArrivalGoods(dhxxDto);
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_view");
		mav.addObject("dhxxDto", map.get("dhxxDto"));
		mav.addObject("hwxxDtos", map.get("hwxxDtos"));
		mav.addObject("hwxx_json", JSON.toJSONString(map.get("hwxxDtos")));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	
	/**
	 * 到货审核查看
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/viewArrivalaudit")
	public ModelAndView viewArrivalaudit(DhxxDto dhxxDto) {
		//调用查看方法，后续只需要维护一个		
		Map<String,Object> map = minidataDingtalkviewArrivalGoods(dhxxDto);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ARRIVALGOODS.getCode());
		fjcfbDto.setYwid(dhxxDto.getDhid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_view");
		mav.addObject("dhxxDto", map.get("dhxxDto"));
		mav.addObject("hwxxDtos", map.get("hwxxDtos"));
		mav.addObject("hwxx_json", JSON.toJSONString(map.get("hwxxDtos")));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
	/**
	 * 标签打印
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/labelprinting")
	public ModelAndView labelprinting(DhxxDto dhxxDto) {
		//调用查看方法，后续只需要维护一个
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_labelprinting");
		mav.addObject("dhxxDto",dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 标签数据
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/pagedataLabelPrinting")
	@ResponseBody
	public Map<String, Object> pageGetListLabelPrinting(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> list=hwxxService.getDtoList(hwxxDto);
		map.put("rows", list);
		return map;
	}
	/**
	 * 货位卡打印
	 */
	@ResponseBody
	@RequestMapping("/arrivalGoods/pagedataLabelPrintingFreight")
	public ModelAndView freightPrint(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_labelprintingFreight");
		List<HwxxDto> hwlist=hwxxService.getDtoListByrkids(hwxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		List<JcsjDto> wjbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DOCUMENTNUM.getCode());
		for (JcsjDto jcsjDto:wjbmlist){
			if (jcsjDto.getCsdm().equals("WLBQ")){
				mav.addObject("mc",jcsjDto.getCskz3());
			}
		}
		mav.addObject("hwlist", hwlist);
		return mav;
	}
	/**
	 * 确认页面点击更新dhxx表中的确认信息人员时间
	 */
	@RequestMapping(value = "/arrivalGoods/confirmSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> confirmModArrivalGoods(DhxxDto dhxxDto, HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setQrry(user.getYhid());
		//确认到货信息，即更新确认人员时间标记
		boolean isSuccess=dhxxService.updateConfirmDhxx(dhxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 钉钉小程序审核到货查看
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/minidataDingtalkviewArrivalGoods")
	@ResponseBody
	public Map<String,Object> minidataDingtalkviewArrivalGoods(DhxxDto dhxxDto) {
		Map<String,Object> map=new HashMap<>();
		DhxxDto t_dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setDhid(dhxxDto.getDhid());
		//是否限制类别参数扩展1,由于和本地共用一个方法，该参数需从小程序传递
		if(StringUtils.isNotBlank(dhxxDto.getSfxzlbcskz1())) 
			hwxxDto.setSfxzlbcskz1(dhxxDto.getSfxzlbcskz1());
		hwxxDto.setLbcskz1(dhxxDto.getLbcskz1());
		List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
		map.put("dhxxDto", t_dhxxDto);
		map.put("hwxxDtos", hwxxDtos);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	
	/**
	 * 到货删除
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/delArrivalGoods")
	@ResponseBody
	public Map<String, Object> delArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		dhxxDto.setScry(user.getYhid());
		//检查是否允许删除
		Map<String,Object> check_map = dhxxService.checkscqx(dhxxDto);
		if(StringUtils.isNotBlank((String)check_map.get("status"))) {
			if("fail".equals(check_map.get("status"))) {
				map.put("status", "fail");
				map.put("message", check_map.get("message"));
				return map;
			}
		}
		try {
			boolean isSuccess;
			if ("cghz".equals(dhxxDto.getRklbdm())){
				isSuccess = dhxxService.purchaseDel(dhxxDto);
			}else {
				isSuccess = dhxxService.deleteDhxx(dhxxDto);
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}
	
	/**
	 * 到货新增
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/pagedataArrivaladd")
	public ModelAndView addArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request) {
		User user = getLoginInfo(request);
		user = commonService.getUserInfoById(user);
		dhxxDto.setSqbmmc(user.getJgmc());
		dhxxDto.setBm(user.getJgid());
		//入库类别
		JcsjDto jcsj = new JcsjDto();
		String gjxgbj = "0";
		jcsj.setJclb(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode());
		if(StringUtil.isNotBlank(dhxxDto.getRklbdm())) {
			if("OA".equals(dhxxDto.getDhlxdm())) {				
				jcsj.setCsdm(dhxxDto.getDhlxdm());//代码
				jcsj=jcsjService.getDto(jcsj);
			}
			if ("ZP".equals(dhxxDto.getRklbdm())){
				gjxgbj = "1";
			}
		}else {
			jcsj.setCsdm("PT");//普通到货
			jcsj=jcsjService.getDto(jcsj);
		}
		dhxxDto.setDhlx(jcsj.getCsid()); //存入到货类型
		ModelAndView mav=new ModelAndView("storehouse/dhxx/arrival_goods_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 采购类型
		if(StringUtils.isBlank(dhxxDto.getDhdh())) {
			String dhdh=dhxxService.generateDhdh();
			dhxxDto.setDhdh(dhdh);
		}
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");//仓库
		jcsjDto=jcsjService.getDto(jcsjDto);
		ckxxDto.setCklb(jcsjDto.getCsid());
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		dhxxDto.setDhrq(sdf.format(date));
		if ("cghz".equals(dhxxDto.getRklbdm())){
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		}else {
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		}
		dhxxDto.setFormAction("addSaveChancetype");
		mav.addObject("cklist", ckxxDtos);
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gjxgbj",gjxgbj);
		//获取追溯号最大流水号
		String prefix = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ARRIVALGOODS.getCode());
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.URL_PREFIX_RECEIVEMATERIEL);
		xtszDto = xtszService.getDto(xtszDto);
		//其他入库引用领料单路径前缀
		mav.addObject("xtszqz",xtszDto==null?"":xtszDto.getSzz());
		mav.addObject("zshlsh",hwxxService.getcCodeSerialZsh(prefix));
		mav.addObject("nowdate",prefix);
		mav.addObject("rklbbj", StringUtil.isNotBlank(dhxxDto.getCskz1())?dhxxDto.getCskz1():"0");
		mav.addObject("cskz3", StringUtil.isNotBlank(dhxxDto.getCskz3())?dhxxDto.getCskz3():"0");
		mav.addObject("rklbdm", dhxxDto.getRklbdm());
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}
	/**
	 * 关联设备
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/pagedataViewGlsb")
	public ModelAndView pagedataViewGlsb(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_glsb");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("url", "/storehouse/arrivalGoods/pagedataGetGlsb");
		mav.addObject("index",request.getParameter("index"));
		return mav;
	}
	/**
	 * 获取关联设备
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/pagedataGetGlsb")
	@ResponseBody
	public Map<String,Object> pagedataGetGlsb(HwxxDto hwxxDto) {
		Map<String,Object> map=new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getPagedGlsbList(hwxxDto);
		map.put("rows", hwxxDtos);
		map.put("total",hwxxDto.getTotalNumber());
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	/**
	 * OA到货新增
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/pagedataSystemArrivalGoods")
	public ModelAndView systemArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request) {
		User user = getLoginInfo(request);
		user = commonService.getUserInfoById(user);
		dhxxDto.setSqbmmc(user.getJgmc());
		dhxxDto.setBm(user.getJgid());
		//入库类别
		JcsjDto jcsj = new JcsjDto();
		jcsj.setJclb(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode());
		if(StringUtil.isNotBlank(dhxxDto.getRklbdm())) {
			if("OA".equals(dhxxDto.getDhlxdm())) {				
				jcsj.setCsdm(dhxxDto.getDhlxdm());//代码
				jcsj=jcsjService.getDto(jcsj);
			}
		}else {
			jcsj.setCsdm("PT");//普通到货
			jcsj=jcsjService.getDto(jcsj);
		}
		dhxxDto.setDhlx(jcsj.getCsid()); //存入到货类型
		ModelAndView mav=new ModelAndView("storehouse/dhxx/arrival_goods_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 采购类型
		if(StringUtils.isBlank(dhxxDto.getDhdh())) {
			String dhdh=dhxxService.generateDhdh();
			dhxxDto.setDhdh(dhdh);
		}
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");//仓库
		jcsjDto=jcsjService.getDto(jcsjDto);
		ckxxDto.setCklb(jcsjDto.getCsid());
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		dhxxDto.setDhrq(sdf.format(date));
		if ("cghz".equals(dhxxDto.getRklbdm())){
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		}else {
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		}
		dhxxDto.setFormAction("systemSaveArrivalGoods");
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.URL_PREFIX_RECEIVEMATERIEL);
		xtszDto = xtszService.getDto(xtszDto);
		//其他入库引用领料单路径前缀
		mav.addObject("xtszqz",xtszDto==null?"":xtszDto.getSzz());
		mav.addObject("cklist", ckxxDtos);
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gjxgbj","0");
		//获取追溯号最大流水号
		String prefix = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ARRIVALGOODS.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("zshlsh",hwxxService.getcCodeSerialZsh(prefix));
		mav.addObject("nowdate",prefix);
		mav.addObject("rklbbj", StringUtil.isNotBlank(dhxxDto.getCskz1())?dhxxDto.getCskz1():"0");
		mav.addObject("cskz3", StringUtil.isNotBlank(dhxxDto.getCskz3())?dhxxDto.getCskz3():"0");
		mav.addObject("rklbdm", dhxxDto.getRklbdm());
		return mav;
	}
	
	/**
	 * OA新增保存到货信息
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/systemSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> systemSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		dhxxDto.setLrry(user.getYhid());
		dhxxDto.setHtid(dhxxDto.getHtnbbhs());
		Map<String,Object> map=new HashMap<>();
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		/*
		 * List<HwxxDto> hwxxDtos = (List<HwxxDto>)
		 * JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class); String issuccess =
		 * hwxxService.judgeZsh(hwxxDtos); if(!issuccess.equals("success")) {
		 * map.put("message",issuccess); return map; }
		 */
		//保存到货信息
		try {
			isSuccess=dhxxService.addSaveArrivalGoods(dhxxDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message",xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("ywid", dhxxDto.getDhid());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	@RequestMapping("/arrivalGoods/pagedataArrivalgoodsdetailList")
	@ResponseBody
	public Map<String,Object> getArrivalGoodsDetailList(DhxxDto dhxxDto) {
		Map<String,Object> map=new HashMap<>();
		HwxxDto hwxxDto=new HwxxDto();
		hwxxDto.setDhid(dhxxDto.getDhid());
		if(StringUtils.isNotBlank(hwxxDto.getDhid())) {
			if ("CGHZ".equals(dhxxDto.getCskz3())){
				List<HwxxDto> hwxxDtolist=hwxxService.getHwxxWithHz(hwxxDto);
				map.put("rows", hwxxDtolist);
			}else {
				List<HwxxDto> hwxxDtolist=hwxxService.getDtoList(hwxxDto);
				map.put("rows", hwxxDtolist);
			}
		}else {
			map.put("rows", null);
		}
		return map;
	}
	
	/**
	 * 新增保存到货信息
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/addSaveChancetype")
	@ResponseBody
	public Map<String,Object> addSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		dhxxDto.setLrry(user.getYhid());
		dhxxDto.setHtid(dhxxDto.getHtnbbhs());
		Map<String,Object> map=new HashMap<>();
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		/*
		 * List<HwxxDto> hwxxDtos = (List<HwxxDto>)
		 * JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class); String issuccess =
		 * hwxxService.judgeZsh(hwxxDtos); if(!issuccess.equals("success")) {
		 * map.put("message",issuccess); return map; }
		 */
		//保存到货信息
		try {
			isSuccess=dhxxService.addSaveArrivalGoods(dhxxDto);
			map.put("ywid", dhxxDto.getDhid());
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message",StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 修改页面
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/modArrivalGoods")
	public ModelAndView modArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		dhxxDto.setFormAction("modSaveArrivalGoods");
		if ("cghz".equals(dhxxDto.getRklbdm())){
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		}else {
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		}
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");//仓库
		jcsjDto=jcsjService.getDto(jcsjDto);
		List<CkxxDto> ckxxDtos= new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxService.getDtoList(ckxxDto);
		}
		mav.addObject("cklist", ckxxDtos);
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		String gjxgbj = "0";
		if ("ZP".equals(dhxxDto.getRklbdm())){
			gjxgbj = "1";
		}
		String prefix = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ARRIVALGOODS.getCode());
		fjcfbDto.setYwid(dhxxDto.getDhid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.URL_PREFIX_RECEIVEMATERIEL);
		xtszDto = xtszService.getDto(xtszDto);
		//其他入库引用领料单路径前缀
		mav.addObject("xtszqz",xtszDto==null?"":xtszDto.getSzz());
		mav.addObject("zshlsh",hwxxService.getcCodeSerialZsh(prefix));
		mav.addObject("nowdate",prefix);
		mav.addObject("gjxgbj",gjxgbj);
		mav.addObject("rklbbj", StringUtil.isNotBlank(dhxxDto.getCskz1())?dhxxDto.getCskz1():"0");
		mav.addObject("cskz3", StringUtil.isNotBlank(dhxxDto.getCskz3())?dhxxDto.getCskz3():"0");
		mav.addObject("rklbdm", dhxxDto.getRklbdm());
		mav.addObject("fjcfbDto",fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
	
	/**
	 * 提交页面
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/submitArrivalGoods")
	public ModelAndView submitArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		dhxxDto.setFormAction("submitSaveArrivalGoods");
		if ("cghz".equals(dhxxDto.getRklbdm())){
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		}else {
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		}
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");//仓库
		jcsjDto=jcsjService.getDto(jcsjDto);
		List<CkxxDto> ckxxDtos= new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxService.getDtoList(ckxxDto);
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ARRIVALGOODS.getCode());
		fjcfbDto.setYwid(dhxxDto.getDhid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.URL_PREFIX_RECEIVEMATERIEL);
		xtszDto = xtszService.getDto(xtszDto);
		//其他入库引用领料单路径前缀
		mav.addObject("xtszqz",xtszDto==null?"":xtszDto.getSzz());
		mav.addObject("cklist", ckxxDtos);
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gjxgbj","0");
		mav.addObject("rklbbj", StringUtil.isNotBlank(dhxxDto.getCskz1())?dhxxDto.getCskz1():"0");
		mav.addObject("cskz3", StringUtil.isNotBlank(dhxxDto.getCskz3())?dhxxDto.getCskz3():"0");
		mav.addObject("rklbdm", dhxxDto.getRklbdm());
		mav.addObject("fjcfbDto",fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
	
	/**
	 * 审核修改页面
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/auditArrivalGoods")
	public ModelAndView auditArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		dhxxDto.setFormAction("auditSaveArrivalGoods");
		if ("cghz".equals(dhxxDto.getRklbdm())){
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		}else {
			dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		}
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");//仓库
		jcsjDto=jcsjService.getDto(jcsjDto);
		List<CkxxDto> ckxxDtos= new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxService.getDtoList(ckxxDto);
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ARRIVALGOODS.getCode());
		fjcfbDto.setYwid(dhxxDto.getDhid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		XtszDto xtszDto = new XtszDto();
		xtszDto.setSzlb(GlobalString.URL_PREFIX_RECEIVEMATERIEL);
		xtszDto = xtszService.getDto(xtszDto);
		//其他入库引用领料单路径前缀
		mav.addObject("xtszqz",xtszDto==null?"":xtszDto.getSzz());
		mav.addObject("cklist", ckxxDtos);
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gjxgbj","0");
		mav.addObject("rklbbj", StringUtil.isNotBlank(dhxxDto.getCskz1())?dhxxDto.getCskz1():"0");
		mav.addObject("cskz3", StringUtil.isNotBlank(dhxxDto.getCskz3())?dhxxDto.getCskz3():"0");
		mav.addObject("rklbdm", dhxxDto.getRklbdm());
		mav.addObject("fjcfbDto",fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
	
	/**
	 * 修改保存提交到货信息
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/auditSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> auditSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());
		dhxxDto.setHtid(dhxxDto.getHtnbbhs());
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		/*
		 * List<HwxxDto> hwxxDtos_t = (List<HwxxDto>)
		 * JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class); String issuccess =
		 * hwxxService.judgeZsh(hwxxDtos_t); if(!issuccess.equals("success")) {
		 * map.put("message",issuccess); return map; }
		 */
		//保存到货信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
		dhxxDto.setHwxxDtos(hwxxDtos);
		try {
			isSuccess=dhxxService.modSaveArrivalGoods(dhxxDto);
			map.put("ywid", dhxxDto.getDhid());
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 修改保存提交到货信息
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/submitSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> submitSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());
		dhxxDto.setHtid(dhxxDto.getHtnbbhs());
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		/*
		 * List<HwxxDto> hwxxDtos_t = (List<HwxxDto>)
		 * JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class); String issuccess =
		 * hwxxService.judgeZsh(hwxxDtos_t); if(!issuccess.equals("success")) {
		 * map.put("message",issuccess); return map; }
		 */
		//保存到货信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
		dhxxDto.setHwxxDtos(hwxxDtos);
		try {
			isSuccess=dhxxService.modSaveArrivalGoods(dhxxDto);
			map.put("ywid", dhxxDto.getDhid());
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 修改保存到货信息
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/modSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> modSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());
		dhxxDto.setHtid(dhxxDto.getHtnbbhs());
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		/*
		 * List<HwxxDto> hwxxDtos_t = (List<HwxxDto>)
		 * JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class); String issuccess =
		 * hwxxService.judgeZsh(hwxxDtos_t); if(!issuccess.equals("success")) {
		 * map.put("message",issuccess); return map; }
		 */
		//保存到货信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
		dhxxDto.setHwxxDtos(hwxxDtos);
		try {
			isSuccess=dhxxService.modSaveArrivalGoods(dhxxDto);
			map.put("ywid", dhxxDto.getDhid());
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 打开打印请检单页面
	 */
	@RequestMapping("/arrivalGoods/printCheckReport")
	public ModelAndView printCheckReport(HwxxDto hwxxDto) {
		ModelAndView mav;
		if ("/labigams".equals(urlPrefix)){
			mav=new ModelAndView("storehouse/dhxx/arrival_goods_new_print");
		}else {
			mav=new ModelAndView("storehouse/dhxx/arrival_goods_print");
		}
		//hwxxDto.setZt(GoodsStatusEnum.GODDS_QUALITY.getCode());
		List<HwxxDto> qjhwlist=hwxxService.getHwxxByZtAndDhids(hwxxDto);
		DhxxDto dhxxDto=dhxxService.getDtoById(hwxxDto.getDhids().get(0));//改为只允许选中一条
		mav.addObject("qjhwlist", qjhwlist);
		mav.addObject("urlPrefix", urlPrefix);
		//根据基础数据获取打印信息		
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("QY");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
		mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		mav.addObject("cskz1",StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		mav.addObject("cskz2", StringUtil.isNotBlank(jcsj.getCskz2())?jcsj.getCskz2():"");
		
		mav.addObject("dhxxDto", dhxxDto);
		return mav;
	}

	/**
	 * 打开打印到货验收单页面
	 */
	@RequestMapping("/arrivalGoods/ysdprintAcceptance")
	public ModelAndView printAcceptanceReport(HwxxDto hwxxDto) {
		ModelAndView mav;
		if("/labigams".equals(urlPrefix)) {
			mav=new ModelAndView("storehouse/dhxx/arrival_goods_printAcceptanceReport_medlab");
		}else {
			mav=new ModelAndView("storehouse/dhxx/arrival_goods_printAcceptanceReport");
		}
//		hwxxDto.setZt(GoodsStatusEnum.GODDS_CHECK.getCode());
		List<HwxxDto> ysdlist=hwxxService.getHwxxByZtAndDhids(hwxxDto);
		//根据基础数据获取打印信息
		
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("YS");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
		mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");		
		mav.addObject("sxrq", StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		mav.addObject("bbh", StringUtil.isNotBlank(jcsj.getCskz2())?jcsj.getCskz2():"");
				
		
		mav.addObject("ysdlist", ysdlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取库位信息
	 */
	@RequestMapping("/arrivalGoods/pagedataGetkwlist")
	@ResponseBody
	public Map<String,Object> getKwList(DhxxDto dhxxDto){
		Map<String,Object> map=new HashMap<>();
		CkxxDto ckxxDto=new CkxxDto();
		ckxxDto.setFckid(dhxxDto.getCkid());
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("KW");//库位
		jcsjDto=jcsjService.getDto(jcsjDto);
		ckxxDto.setCklb(jcsjDto.getCsid());
		List<CkxxDto> kwlist=ckxxService.getKwDtoList(ckxxDto);
		map.put("kwlist", kwlist);
		return map;
	}

	/**
	 * 获取仓库货物信息
	 */
	@RequestMapping("/arrivalGoods/pagedataGetCkhwxx")
	@ResponseBody
	public Map<String,Object> getCkhwxx(HwxxDto hwxxDto){
		Map<String,Object> map=new HashMap<>();
		HwxxDto dto = hwxxService.getCkhwxxByHwid(hwxxDto);
		map.put("hwxxDto", dto);
		return map;
	}
	
	/**
	 * 到货查看 共通页面
	 *
	 */
	@RequestMapping(value = "/arrivalGoods/viewCommonArrivalGoods")
	public ModelAndView viewCommonArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_viewCommon");
		// 获取到货信息
		DhxxDto t_dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		mav.addObject("dhxxDto",t_dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 高级修改页面
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/advancedmodArrivalGoods")
	public ModelAndView advancedmodArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		dhxxDto.setFormAction("advancedmodSaveArrivalGoods");
		dhxxDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");//仓库
		jcsjDto=jcsjService.getDto(jcsjDto);
		List<CkxxDto> ckxxDtos= new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxService.getDtoList(ckxxDto);
		}
		mav.addObject("cklist", ckxxDtos);
		mav.addObject("dhxxDto", dhxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		//增加高级修改标记
		mav.addObject("gjxgbj","1");
		return mav;
	}
	
	/**
	 * 修改保存到货信息
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/advancedmodSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> advancedmodsave(DhxxDto dhxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());
		dhxxDto.setHtid(dhxxDto.getHtnbbhs());
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		//保存到货信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
		dhxxDto.setHwxxDtos(hwxxDtos);
		try {
			isSuccess=dhxxService.advancedmodsave(dhxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("ywid", dhxxDto.getDhid());
		return map;	
	}
	
	/**
   	 * 归还页面
	 * @author yao
	 */
	@RequestMapping("/arrivalGoods/returnbackArrivalGoods")
	public ModelAndView returnbackArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_return");
		// 获取到货信息
		DhxxDto t_dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		HwxxDto hw_ghzl = new HwxxDto();
		hw_ghzl.setDhid(dhxxDto.getDhid());
		hw_ghzl = hwxxService.getJyGhzlByDhid(hw_ghzl);
		t_dhxxDto.setGhzl(hw_ghzl.getGhzl());
		t_dhxxDto.setFormAction("returnbackSaveArrivalGoods");
		mav.addObject("dhxxDto", t_dhxxDto);//
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	@RequestMapping("/arrivalGoods/pagedataGetArrivalGoodsGhList")
	@ResponseBody
	public Map<String,Object> getArrivalGoodsGhList(DhxxDto dhxxDto) {
		Map<String,Object> map=new HashMap<>();
		HwxxDto hwxxDto=new HwxxDto();
		hwxxDto.setDhid(dhxxDto.getDhid());
		if(StringUtils.isNotBlank(hwxxDto.getDhid())) {
			List<HwxxDto> ghlist =hwxxService.getReturnBackList(hwxxDto);//getReturnBackList
			map.put("rows", ghlist);
		}else {
			map.put("rows", null);
		}
		return map;
	}
	
	@RequestMapping("/arrivalGoods/returnbackSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> returnbackSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());
		//保存到货信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
		boolean isSuccess = hwxxService.updateHwxxReturn(hwxxDtos);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 借用页面
	 */
	@RequestMapping("/arrivalGoods/borrowArrivalGoods")
	public ModelAndView borrowArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_brrow");
		// 获取到货信息
		DhxxDto t_dhxxDto = dhxxService.getDtoById(dhxxDto.getDhid());
		HwxxDto hw_jyzl = new HwxxDto();
		hw_jyzl.setDhid(dhxxDto.getDhid());
		hw_jyzl = hwxxService.getJyGhzlByDhid(hw_jyzl);
		t_dhxxDto.setJyzl(hw_jyzl.getJyzl());
		t_dhxxDto.setFormAction("borrowSaveArrivalGoods");
		mav.addObject("dhxxDto", t_dhxxDto);//
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	@RequestMapping("/arrivalGoods/pagedataGetArrivalGoodsJyList")
	@ResponseBody
	public Map<String,Object> getArrivalGoodsJyList(DhxxDto dhxxDto) {
		Map<String,Object> map=new HashMap<>();
		HwxxDto hwxxDto=new HwxxDto();
		hwxxDto.setDhid(dhxxDto.getDhid());
		if(StringUtils.isNotBlank(hwxxDto.getDhid())) {
			List<HwxxDto> jylist =hwxxService.getBorrowList(hwxxDto);
			map.put("rows", jylist);
		}else {
			map.put("rows", null);
		}
		return map;
	}
	
	@RequestMapping("/arrivalGoods/borrowSaveArrivalGoods")
	@ResponseBody
	public Map<String,Object> borrowSaveArrivalGoods(DhxxDto dhxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());

//		//保存到货信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(dhxxDto.getDhmxJson(), HwxxDto.class);
		boolean isSuccess = hwxxService.updateHwxxBorrow(hwxxDtos);

		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
		

	/**
	 * 到货列表废弃功能
	 */

	@ResponseBody
	@RequestMapping(value = "/arrivalGoods/discardContractView")
	public Map<String,Object> discardContractView(DhxxDto DhxxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			DhxxDto.setScry(user.getYhid());
			DhxxDto.setFqbj("2");
			boolean isSuccess = dhxxService.deleteDhxx(DhxxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
// 	 货位卡打印
// 	@ResponseBody
// 	@RequestMapping("/arrivalGoods/freightPrint")
// 	public ModelAndView freightPrint(HwxxDto hwxxDto) {
// 		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_hwPrint");
// //		hwxxDto.setZt(GoodsStatusEnum.GODDS_QUALITY.getCode());
// 		List<HwxxDto> hwlist=hwxxService.getHwxxByZtAndDhids(hwxxDto);
// 		mav.addObject("urlPrefix", urlPrefix);
// 		mav.addObject("hwlist", hwlist);
// 		return mav;
// 	}
//	@RequestMapping("/arrivalGoods/freightPrintOpen")
//	public ModelAndView freightPrintOpen(String ysdh,String wlbm,String wlmc,String zsh,String jldw,String gg,String yxq,String gysmc,String scph,
//										 String dhrq,String dhdh,String lrry,String dhsl,String cskwmc) {
//		HwxxDto hwxxDto = new HwxxDto();
//		hwxxDto.setYsdh(ysdh);
//		hwxxDto.setWlbm(wlbm);
//		hwxxDto.setWlmc(wlmc);
//		hwxxDto.setZsh(zsh);
//		hwxxDto.setJldw(jldw);
//		hwxxDto.setGg(gg);
//		hwxxDto.setYxq(yxq);
//		hwxxDto.setGysmc(gysmc);
//		hwxxDto.setScph(scph);
//		hwxxDto.setDhrq(dhrq);
//		hwxxDto.setDhdh(dhdh);
//		hwxxDto.setLrry(lrry);
//		hwxxDto.setDhsl(dhsl);
//		hwxxDto.setCskwmc(cskwmc);
//		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_hwPrint");
//		mav.addObject("hwxxDto",hwxxDto);
//		mav.addObject("urlPrefix", urlPrefix);
//		return mav;
//	}
	
	/**
	 * 选择入库类型页面
	 */
	@RequestMapping("/arrivalGoods/addChancetype")
	public ModelAndView chanceDhTypePage(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_chance");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE});
		mav.addObject("rklbList", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("url", "/storehouse/arrivalGoods/pagedataArrivaladd");
		mav.addObject("dhxxDto", dhxxDto);
		return mav;
	}
	
	/**
	 * 选择入库类型页面OA
	 */
	@RequestMapping("/arrivalGoods/systemArrivalGoods")
	public ModelAndView systemArrivalGoods(DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_chance");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE});
		mav.addObject("rklbList", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("url", "/storehouse/arrivalGoods/pagedataSystemArrivalGoods");
		mav.addObject("dhxxDto", dhxxDto);
		return mav;
	}
	
	/**
	 *选取物料信息做其他入库成品入库
	 */
	@RequestMapping(value = "/arrivalGoods/pagedataChooseMaterial")
	public ModelAndView chooseMaterial(WlglDto wlglDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_finish");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlglDto", wlglDto);
		return mav;
	}

	/**
	 *选取物料信息做其他入库成品入库
	 */
	@RequestMapping(value = "/arrivalGoods/pagedataChooseProduce")
	public ModelAndView chooseProduce() {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_produce");
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PRODUCTS_TYPE.getCode());
		mav.addObject("lblist", jcsjDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 	获取前15条数据
	 */
	@ResponseBody
	@RequestMapping(value = "/arrivalGoods/pagedataGetMaterial")
	public Map<String,Object> getMaterial(WlglDto wlglDto){
		Map<String,Object> map=new HashMap<>();
		List<WlglDto> wlglDtos = wlglService.getWlgl(wlglDto);
		map.put("rows",wlglDtos);
		return map;
	}
	
	
	/**
	 * 	获取物料信息，用货物信息存储
	 */
	@ResponseBody
	@RequestMapping(value = "/arrivalGoods/pagedataGetListBywlid")
	public Map<String, Object> getListBywlid(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.queryWlglDtosByWlid(hwxxDto);
		map.put("rows", hwxxDtos);
		return map;
	}
	
	/**
	 * 	退货页面
	 */
	@RequestMapping("/arrivalGoods/deliverGoods")
	public ModelAndView deliverGoods(FhglDto fhglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/deliver_goods");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.SELLING_TYPE, BasicDataTypeEnum.DELIVERY_METHOD});
		mav.addObject("xslxlist", jclist.get(BasicDataTypeEnum.SELLING_TYPE.getCode()));// 销售类型
		mav.addObject("ysfslist", jclist.get(BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运送方式
		fhglDto.setFormAction("saveDeliverGoods");
		User user = getLoginInfo(request);
		user = commonService.getUserInfoById(user);
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		fhglDto.setDjrq(sdf.format(date));
		fhglDto.setXsbmmc(user.getJgmc());
		fhglDto.setXsbm(user.getJgid());
		fhglDto.setXsbmdm(user.getJgdm());
		fhglDto.setJsrmc(user.getZsxm());
		fhglDto.setJsr(user.getYhid());
		String thdh = fhglService.GenerateNumber();
		fhglDto.setFhdh(thdh);
		fhglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_ARRIVAL.getCode());
		//获取仓库
		CkxxDto ckxx = new CkxxDto();
		ckxx.setCklbdm("CK");
		List<CkxxDto> cklist = ckxxService.getDtoList(ckxx);
		//获取库位
		ckxx.setCklbdm("KW");
		List<CkxxDto> kwlist = ckxxService.getDtoList(ckxx);
		mav.addObject("fhglDto", fhglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("cklist", JSONObject.toJSONString(cklist));
		mav.addObject("kwlist", JSONObject.toJSONString(kwlist));
		return mav;
	}

	/**
	 * 出货保存
	 */
	@RequestMapping("/arrivalGoods/deliverSaveGoods")
	@ResponseBody
	public Map<String,Object> shipFhSave(FhglDto fhglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//判断调拨单是否存在
		User user = getLoginInfo(request);
		fhglDto.setLrry(user.getYhid());
		// 判断入库单号是否重复
		boolean isSuccess;
		// List<FhglDto> fhdhRepeat = fhglService.isFhdhRepeat(fhglDto);
		// if(!isSuccess) {
		// 	map.put("status", "fail");
		// 	map.put("message", "退货单号不允许重复！");
		// 	return map;
		// }
		try {
			isSuccess = fhglService.thSave(fhglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 引用生产指令单
	 */
	@RequestMapping(value = "/produce/pagedataGetPagedDtoProduceList")
	@ResponseBody
	public Map<String, Object> getPagedDtoProduceList(SczlglDto sczlglDto) {
		Map<String, Object> map = new HashMap<>();
		List<SczlglDto> pagedDtoList = sczlglService.getPagedModelDtoList(sczlglDto);
		map.put("total", sczlglDto.getTotalNumber());
		map.put("rows",pagedDtoList);
		return map;
	}


	/**
	 * 查询生产指令信息
	 */
	@RequestMapping(value = "/produce/pagedataGetProduceInfo")
	@ResponseBody
	public Map<String, Object> getProduceInfo(SczlmxDto sczlmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<SczlmxDto> sczlglDtos = sczlmxService.getDtoList(sczlmxDto);
		map.put("sczlglDtos",sczlglDtos);
		return map;
	}

	/**
	 * 跳转生产指令明细列表
	 */
	@RequestMapping(value = "/produce/pagedataChooseListProduceInfo")
	public ModelAndView chooseListProduceInfo(SczlmxDto sczlmxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/produce_chooseInfoList");
		List<SczlmxDto> sczlglDtos = sczlmxService.getDtoList(sczlmxDto);
		mav.addObject("dtoList", sczlglDtos);
		mav.addObject("dto", sczlmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 出入库统计
	 */
	@RequestMapping(value = "/arrivalGoods/getCountStatistics")
	@ResponseBody
	public Map<String, Object> getCountStatistics(DhxxDto dhxxDto) {
		List<DhxxDto> list = dhxxService.getCountStatistics(dhxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		return map;
	}
	/**
	 * 钉钉出入库统计
	 */
	@RequestMapping(value = "/arrivalGoods/minidataGetCountStatistics")
	@ResponseBody
	public Map<String, Object> minidataGetCountStatistics(DhxxDto dhxxDto) {
		List<DhxxDto> list = dhxxService.getCountStatistics(dhxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		return map;
	}
}
