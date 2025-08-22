package com.matridx.igams.storehouse.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.storehouse.service.impl.RkglServiceImpl;
import com.matridx.igams.warehouse.service.svcinterface.IGysxxService;
import com.matridx.springboot.util.base.StringUtil;
@Controller
@RequestMapping("/warehouse")
public class PutInStorageController extends BaseBasicController {
	@Autowired
	IRkglService rkglService;
	@Autowired
	IXzrkglService xzrkglService;
	@Autowired
	IGysxxService gysxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	ICkxxService ckxxServie;
	
	@Autowired
	IXxglService xxglService;
	@Autowired
	IHwxxService hwxxService;
	@Autowired
	IWlglService wlglService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	ICkxxService ckxxService;
	@Autowired
	IXzrkmxService xzrkmxService;
	private final Logger log = LoggerFactory.getLogger(RkglServiceImpl.class);

	@Autowired
	IFjcfbService fjcfbService;
	//是否发送rabbit标记     1：发送
	@Value("${matridx.rabbit.systemconfigflg:}")
	private String systemconfigflg;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	/**
	 * 入库列表
	 * @return
	 */
	@RequestMapping("/putInStorage/pageListPutInStorage")
	public ModelAndView getPutInStoragePageList() {
		ModelAndView mav = new  ModelAndView("storehouse/putInStorage/putInStorage_list");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		return mav;		
	}
	
	/**
	 * 查找出入库列表的数据
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping("/putInStorage/pageGetListPutInStorage")
	@ResponseBody
	public Map<String,Object> getPutInStorageList(RkglDto rkglDto){
		List<RkglDto> rkglList = rkglService.getPagedDtoList(rkglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("total",rkglDto.getTotalNumber());
		map.put("rows",rkglList);
		return map;
	}
	/**
	 * 查找出入库列表的数据钉钉
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping("/putInStorage/minidataGetPagedDtoListDingTalk")
	@ResponseBody
	public Map<String,Object> getPagedDtoListDingTalk(RkglDto rkglDto){
		List<RkglDto> rkglList = rkglService.getPagedDtoListDingTalk(rkglDto);
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> rklblist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INBOUND_TYPE.getCode());
		map.put("total",rkglDto.getTotalNumber());
		map.put("rows",rkglList);
		map.put("rklxlist",rklblist);
		return map;
	}
	/**
	 * 
	 * 查看入库列表的一条信息
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping("/putInStorage/viewPutInStorage")
	public ModelAndView viewPutInStorage(RkglDto rkglDto) {
		ModelAndView mav = new  ModelAndView("storehouse/putInStorage/putInStorage_view");
		rkglDto = rkglService.getDtoById(rkglDto.getRkid());
		mav.addObject("rkglDto",rkglDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;		
	}
	/**
	 * 查看入库列表的一条信息
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping("/putInStorage/pagedataViewPutInStorage")
	public ModelAndView pagedataViewPutInStorage(RkglDto rkglDto) {
		ModelAndView mav = new  ModelAndView("storehouse/putInStorage/putInStorage_view");
		rkglDto = rkglService.getDtoById(rkglDto.getRkid());
		mav.addObject("rkglDto",rkglDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 查看入库列表的一条信息钉钉
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping("/putInStorage/minidataGetViewPutInStorage")
	@ResponseBody
	public Map<String,Object> getViewPutInStorage(RkglDto rkglDto){
		rkglDto = rkglService.getDtoById(rkglDto.getRkid());
		HwxxDto hwxxDto=new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> hwxxList =hwxxService.getDtoListByRkid(hwxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("rkglDto",rkglDto);
		map.put("hwxxList",hwxxList);
		return map;
	}

	/**
	 * 入库新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value="/putInStorage/pagedataPutinstorage")
	public ModelAndView addPutInStorage(RkglDto rkglDto) {
		ModelAndView mav = new  ModelAndView("storehouse/putInStorage/putInStorage_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PURCHASE_TYPE,BasicDataTypeEnum.INBOUND_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 采购类型类别
		// 设置默认入库日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxDtos = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxServie.getDtoList(ckxxDto);
		}
		rkglDto.setFormAction("/putInStorage/addSaveChancerktypepage");
		rkglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		String rkdh = rkglService.generatePutInStorageCode(rkglDto);
		rkglDto.setRkdh(rkdh);
		rkglDto.setRkrq(sdf.format(date));
		mav.addObject("rkglDto",rkglDto);
		mav.addObject("xsbj","1");
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("url", "/warehouse/stockPending/pagedataGetHwxxList");
		mav.addObject("rklbbj", "1");
		return mav;	
	}

	
	/**
	 * 入库单号刷新
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/pagedataGetPutInStorageCode")
	public Map<String, Object> getPutInStorageCode(RkglDto rkglDto) {
		Map<String, Object> map = new HashMap<>();
		// 生成入库单号
		String rkdh = rkglService.generatePutInStorageCode(rkglDto);
		rkglDto.setRkdh(rkdh);
		map.put("rkglDto", rkglDto);
		return map;
	}
	

	
	/**
	 * 跳转请到货明细列表
	 * @param hwxxDto
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/pagedataChooseListHwxx")
	public ModelAndView chooseListHwxx(HwxxDto hwxxDto) {
		ModelAndView mav=new ModelAndView("warehouse/stockPending/stockPending_chooseList");
		List<HwxxDto> hwxxDtos = hwxxService.chooseListHwxx(hwxxDto);
		
		String hwxxJson_choose = JSONObject.toJSONString(hwxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwxxDtos", hwxxDtos);
		mav.addObject("hwxxJson_choose", hwxxJson_choose);
		return mav;
	}
	
	/**
	 * 	获取货物信息
	 * @param hwxxDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/pagedataGetListByhwid")
	public Map<String, Object> pagedataGetListByhwid(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		if(!CollectionUtils.isEmpty(hwxxDto.getIds())) {
			List<HwxxDto> hwxxList = hwxxService.getDtoList(hwxxDto);
			map.put("rows", hwxxList);
		}else if(StringUtil.isNotBlank(hwxxDto.getRkid())){
			List<HwxxDto> hwxxList = hwxxService.getDtoList(hwxxDto);
			map.put("rows", hwxxList);
		}else {
			map.put("rows", null);
		}
		return map;
	}
		/**
	 * 	获取货物信息
	 * @param hwxxDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/pageGetListByhwid")
	public Map<String, Object> getListByhwid(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		if(!CollectionUtils.isEmpty(hwxxDto.getIds())) {
			List<HwxxDto> hwxxList = hwxxService.getDtoList(hwxxDto);
			map.put("rows", hwxxList);
		}else if(StringUtil.isNotBlank(hwxxDto.getRkid())){
			List<HwxxDto> hwxxList = hwxxService.getDtoList(hwxxDto);
			map.put("rows", hwxxList);
		}else {
			map.put("rows", null);
		}
		return map;
	}
	/**
	 * 	入库新增保存
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/addSaveChancerktypepage")
	public Map<String, Object> addSaveChancerktypepage(RkglDto rkglDto ,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		rkglDto.setLrry(user.getYhid());
		// 判断入库单号是否重复
		boolean isSuccess = rkglService.isRkdhRepeat(rkglDto.getRkdh(),rkglDto.getRkid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "入库单号不允许重复！");
			return map;
		}
		
		try {
			isSuccess = rkglService.addSavePutInStorage(rkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
			map.put("ywid", rkglDto.getRkid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	

	/**
	 * 入库修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/modPutInStorage")
	public ModelAndView modPutInStorage(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		// 设置默认入库日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxDtos = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxServie.getDtoList(ckxxDto);
		}
		RkglDto rkglDto_t = rkglService.getDtoById(rkglDto.getRkid());
		rkglDto_t.setFormAction("/putInStorage/modSavePutInStorage");
		rkglDto_t.setRkrq(sdf.format(date));
		rkglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		if (StringUtil.isNotBlank(rkglDto.getBs())){
			rkglDto_t.setBs(rkglDto.getBs());
		}
		if(StatusEnum.CHECK_PASS.getCode().equals(rkglDto_t.getZt())) {
			mav.addObject("xsbj","0");
		}else {
			mav.addObject("xsbj","1");
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> dtoListByRkid = hwxxService.getDtoListByRkid(hwxxDto);
		String dhlxdm = dtoListByRkid.get(0).getDhlxdm();
		rkglDto_t.setDhlxdm(dhlxdm);
		mav.addObject("rkglDto",rkglDto_t);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("url", "/warehouse/putInStorage/pagedataGetListByhwid");
		mav.addObject("rklbbj", '1');
		return mav;
	}

	/**
	 * 入库修改页面(提交)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/submitPutInStorage")
	public ModelAndView submitPutInStorage(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		// 设置默认入库日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxDtos = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxServie.getDtoList(ckxxDto);
		}
		RkglDto rkglDto_t = rkglService.getDtoById(rkglDto.getRkid());
		rkglDto_t.setFormAction("/putInStorage/submitSavePutInStorage");
		rkglDto_t.setRkrq(sdf.format(date));
		rkglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		if (StringUtil.isNotBlank(rkglDto.getBs())){
			rkglDto_t.setBs(rkglDto.getBs());
		}
		if(StatusEnum.CHECK_PASS.getCode().equals(rkglDto_t.getZt())) {
			mav.addObject("xsbj","0");
		}else {
			mav.addObject("xsbj","1");
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> dtoListByRkid = hwxxService.getDtoListByRkid(hwxxDto);
		String dhlxdm = dtoListByRkid.get(0).getDhlxdm();
		rkglDto_t.setDhlxdm(dhlxdm);
		mav.addObject("rkglDto",rkglDto_t);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("url", "/warehouse/putInStorage/pagedataGetListByhwid");
		mav.addObject("rklbbj", '1');
		return mav;
	}

	/**
	 * 	入库修改保存(提交)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/submitSavePutInStorage")
	public Map<String, Object> submitSavePutInStorage(RkglDto rkglDto ,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		rkglDto.setXgry(user.getYhid());
		// 判断入库单号是否重复
		boolean isSuccess = rkglService.isRkdhRepeat(rkglDto.getRkdh(),rkglDto.getRkid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM99051").getXxnr());
			return map;
		}
    	try {	     	       
			isSuccess = rkglService.updateRkgl(rkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} 
		return map;
	}

	
	/**
	 * 	入库修改保存
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/modSavePutInStorage")
	public Map<String, Object> modSavePutInStorage(RkglDto rkglDto ,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		rkglDto.setXgry(user.getYhid());
		// 判断入库单号是否重复
		boolean isSuccess = rkglService.isRkdhRepeat(rkglDto.getRkdh(),rkglDto.getRkid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM99051").getXxnr());
			return map;
		}
    	try {	     	       
			isSuccess = rkglService.updateRkgl(rkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} 
		return map;
	}

	
	/**
	 * 入库修改页面(审核)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/auditPutinstorage")
	public ModelAndView auditPutinstorage(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		// 设置默认入库日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxDtos = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxServie.getDtoList(ckxxDto);
		}
		RkglDto rkglDto_t = rkglService.getDtoById(rkglDto.getRkid());
		rkglDto_t.setFormAction("auditSavePutinstorage");
		rkglDto_t.setRkrq(sdf.format(date));
		rkglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		if (StringUtil.isNotBlank(rkglDto.getBs())){
			rkglDto_t.setBs(rkglDto.getBs());
		}
		if(StatusEnum.CHECK_PASS.getCode().equals(rkglDto_t.getZt())) {
			mav.addObject("xsbj","0");
		}else {
			mav.addObject("xsbj","1");
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> dtoListByRkid = hwxxService.getDtoListByRkid(hwxxDto);
		String dhlxdm = dtoListByRkid.get(0).getDhlxdm();
		rkglDto_t.setDhlxdm(dhlxdm);
		mav.addObject("rkglDto",rkglDto_t);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("url", "/warehouse/putInStorage/pagedataGetListByhwid");
		mav.addObject("rklbbj", '1');
		return mav;
	}

	/**
	 * 入库修改页面(审核)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/auditSavePutinstorage")
	public ModelAndView auditSavePutinstorage(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		// 设置默认入库日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxDtos = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxServie.getDtoList(ckxxDto);
		}
		RkglDto rkglDto_t = rkglService.getDtoById(rkglDto.getRkid());
		rkglDto_t.setFormAction("submitSavePutInStorage");
		rkglDto_t.setRkrq(sdf.format(date));
		rkglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		if (StringUtil.isNotBlank(rkglDto.getBs())){
			rkglDto_t.setBs(rkglDto.getBs());
		}
		if(StatusEnum.CHECK_PASS.getCode().equals(rkglDto_t.getZt())) {
			mav.addObject("xsbj","0");
		}else {
			mav.addObject("xsbj","1");
		}
		mav.addObject("rkglDto",rkglDto_t);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("url", "/warehouse/putInStorage/pagedataGetListByhwid");
		mav.addObject("rklbbj", '1');
		return mav;
	}
	
	/**
	 * 入库高级修改页面
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/advancedmodPut")
	public ModelAndView advancedmodPut(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		// 设置默认入库日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxDtos = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxDtos = ckxxServie.getDtoList(ckxxDto);
		}
		RkglDto rkglDto_t = rkglService.getDtoById(rkglDto.getRkid());
		rkglDto_t.setFormAction("/putInStorage/advancedmodSavePut");
		rkglDto_t.setRkrq(sdf.format(date));
		rkglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		if(StatusEnum.CHECK_PASS.getCode().equals(rkglDto_t.getZt())) {
			mav.addObject("xsbj","0");
		}else {
			mav.addObject("xsbj","1");
		}
		if (StringUtil.isNotBlank(rkglDto.getBs())){
			rkglDto_t.setBs(rkglDto.getBs());
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> dtoListByRkid = hwxxService.getDtoListByRkid(hwxxDto);
		String dhlxdm = dtoListByRkid.get(0).getDhlxdm();
		rkglDto_t.setDhlxdm(dhlxdm);
		mav.addObject("rkglDto",rkglDto_t);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("url", "/warehouse/putInStorage/pagedataGetListByhwid");
		mav.addObject("rklbbj", '1');
		return mav;
	}
	/**
	 *     入库高级修改保存
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/advancedmodSavePut")
	public Map<String, Object> advancedModSavePutInStorage(RkglDto rkglDto ,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		rkglDto.setXgry(user.getYhid());
		// 判断入库单号是否重复
		boolean isSuccess = rkglService.isRkdhRepeat(rkglDto.getRkdh(),rkglDto.getRkid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM99051").getXxnr());
			return map;
		}
		try {
			isSuccess = rkglService.advancedUpdateRkgl(rkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	
	/**
	 * 	入库审核
	 * @return
	 */
	@RequestMapping("/putInStorage/pageListPutInAudite")
	public ModelAndView pageListPutInAudite() {
		ModelAndView mav = new  ModelAndView("storehouse/putInStorage/putInStorage_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;		
	}
	
	/**
	 * 	入库审核列表
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping("/putInStorage/pageGetListPutInAudite")
	@ResponseBody
	public Map<String, Object> getListPutIn_audit(RkglDto rkglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(rkglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(rkglDto.getDqshzt())) {
			DataPermission.add(rkglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "rkgl", "rkid",
					AuditTypeEnum.AUDIT_GOODS_STORAGE);
		} else {
			DataPermission.add(rkglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "rkgl", "rkid",
					AuditTypeEnum.AUDIT_GOODS_STORAGE);
		}
			DataPermission.addCurrentUser(rkglDto, getLoginInfo(request));
		List<RkglDto> listMap = rkglService.getPagedAuditRkgl(rkglDto);
		map.put("total", rkglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 	入库审核（行政）
	 * @return
	 */
	@RequestMapping("/putInAudit/pageListPutInAdminAudite")
	public ModelAndView pageListPutInAdminAudite() {
		ModelAndView mav = new  ModelAndView("storehouse/putInStorage/adminStorage_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 	入库审核列表（行政）
	 * @return
	 */
	@RequestMapping("/putInAudit/pageGetListPutInAdminAudite")
	@ResponseBody
	public Map<String, Object> getListPutInAdmin_audit(XzrkglDto xzrkglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(xzrkglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(xzrkglDto.getDqshzt())) {
			DataPermission.add(xzrkglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "xzrkgl", "xzrkid",
					AuditTypeEnum.AUDIT_INSTORE);
		} else {
			DataPermission.add(xzrkglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "xzrkgl", "xzrkid",
					AuditTypeEnum.AUDIT_INSTORE);
		}
		DataPermission.addCurrentUser(xzrkglDto, getLoginInfo(request));
		List<XzrkglDto> listMap = xzrkglService.getPagedAuditPutInAdmin(xzrkglDto);
		map.put("total", xzrkglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 行政请购审核按钮
	 *
	 * @return
	 */
	@RequestMapping("/putInAudit/auditPurchase")
	public ModelAndView instorePurchase(XzrkglDto xzrkglDto) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_instore");
		String ids ="";
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		CkxxDto ckxxDto = new CkxxDto();
		CkxxDto kwxxDto = new CkxxDto();
		for (JcsjDto jcsjDto:jcsjDtos) {
			if ("CK".equals(jcsjDto.getCsdm())){
				ckxxDto.setCklb(jcsjDto.getCsid());
			}else if("KW".equals(jcsjDto.getCsdm())){
				kwxxDto.setCklb(jcsjDto.getCsid());
			}
		}
		XzrkglDto xzrkglDto_t = xzrkglService.getDtoById(xzrkglDto.getXzrkid());
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		List<CkxxDto> kwxxDtos = ckxxService.getDtoList(kwxxDto);
		mav.addObject("formAction", "/purchase/purchase/saveInstorePurchase");
		mav.addObject("url", "/warehouse/putInAudit/pagedataGetMxList");
		mav.addObject("cklist",JSONObject.toJSONString(ckxxDtos));
		mav.addObject("kwlist",JSONObject.toJSONString(kwxxDtos));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("ids",ids);
		mav.addObject("flg","1");
		mav.addObject("xzrkglDto", xzrkglDto_t);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_INSTORE.getCode());
		return mav;
	}

	/**
	 * 采购明细数据(不分页)
	 *
	 * @return
	 */
	@RequestMapping("/putInAudit/pagedataGetMxList")
	@ResponseBody
	public Map<String, Object> getRKmxList(XzrkmxDto xzrkmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<XzrkmxDto> list = xzrkmxService.getDtoList(xzrkmxDto);
		for(XzrkmxDto xzrkmxDto_t:list){
			xzrkmxDto_t.setQgmxid(xzrkmxDto_t.getXzrkmxid());
		}
		map.put("rows", list);
		return map;
	}

	
	/**
	 * 	入库删除
	 * @param rkglDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/delPutInStorage")
	public Map<String,Object> delPutInStorage(RkglDto rkglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			rkglDto.setScry(user.getYhid());
			rkglDto.setScbj("1");
			boolean isSuccess = rkglService.deleteRkgl(rkglDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}
	
	/**
	 * 	入库废弃
	 * @param rkglDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/discardPutInStorage")
	public Map<String,Object> discardPutInStorage(RkglDto rkglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			rkglDto.setScry(user.getYhid());
			rkglDto.setScbj("2");
			boolean isSuccess = rkglService.deleteRkgl(rkglDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		} 
		return map;
	}
	/**
	 * 入库查看 共通页面
	 * 
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/viewCommonPutInStorage")
	public ModelAndView viewCommonPutInStorage(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_viewCommon");
		// 获取到货信息
		RkglDto t_rkglDto = rkglService.getDtoById(rkglDto.getRkid());
		mav.addObject("rkglDto",t_rkglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 *选取物料信息做其他入库成品入库
	 * 
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/chooseMaterial")
	public ModelAndView chooseMaterial() {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putlnStorage_finish");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 	获取前15条数据
	 * @param wlglDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/getMaterial")
	public Map<String,Object> getMaterial(WlglDto wlglDto){
		Map<String,Object> map=new HashMap<>();
		List<WlglDto> wlglDtos = wlglService.getWlgl(wlglDto);
		map.put("rows",wlglDtos);
		return map;
	}
	
	/**
	 * 	获取物料信息，用货物信息存储
	 * @param hwxxDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/putInStorage/getListBywlid")
	public Map<String, Object> getListBywlid(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.queryWlglDtosByWlid(hwxxDto);
		map.put("rows", hwxxDtos);
		return map;
	}
	
	/**
	 * 选择入库类型页面
	 * @return
	 */
	@RequestMapping("/putInStorage/addChancerktypepage")
	public ModelAndView chanceRkTypePage() {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putlnStorage_chance");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE});
		mav.addObject("rklbList", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 钉钉小程序审核入库查看、钉钉小程序入库查看
	 * 
	 * @param rkglDto
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/minidataDingtalkviewputInStorage")
	@ResponseBody
	public Map<String,Object> minidataDingtalkviewputInStorage(RkglDto rkglDto) {
		Map<String,Object> map=new HashMap<>();
		log.error("---钉钉传递参数---- 入库id:"+rkglDto.getRkid());
		RkglDto t_rkglDto = rkglService.getDtoById(rkglDto.getRkid());
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
		map.put("rkglDto", t_rkglDto);
		map.put("hwxxDtos", hwxxDtos);
		return map;
	}
	/**
	 * 货位卡打印
	 */
	@ResponseBody
	@RequestMapping("/putInStorage/pagedataHwprintFreight")
	public ModelAndView freightPrint(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putlnStorage_hwPrint");
//		hwxxDto.setZt(GoodsStatusEnum.GODDS_QUALITY.getCode());
		List<HwxxDto> hwlist=hwxxService.getDtoListByrkids(hwxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwlist", hwlist);
		return mav;
	}

	/**
	 * 入库列表废弃功能
	 */

	@ResponseBody
	@RequestMapping(value = "/putInStorage/discardContractView")
	public Map<String,Object> discardContractView(RkglDto RkglDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			RkglDto.setScry(user.getYhid());
			RkglDto.setFqbj("2");
			boolean isSuccess = rkglService.deleteRkgl(RkglDto);
			if("1".equals(systemconfigflg) && isSuccess) {
				RkglDto.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.del",JSONObject.toJSONString(RkglDto));
			}
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

	//===========================下面为新的导入内容=============================
	/**
	 * 领料导入页面
	 * @return
	 */
	@RequestMapping("/materialImport/materialImport")
	public ModelAndView importSamp(){
		ModelAndView mav = new ModelAndView("storehouse/materialAudit/material_import");
		
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REQUISITION.getCode());
		HwllxxDto hwllxxDto = new HwllxxDto();
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("hwllxxDto", hwllxxDto);
		
		return mav;
	}
	/**
	 * 单条入库点击查看
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/putInStorage/queryByRkxx")
	@ResponseBody
	public Map<String, Object> queryByRkxx( HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		HwxxDto hwxxDto_t=hwxxService.queryByRkxx(hwxxDto);
		map.put("Hwxxdto",hwxxDto_t);
		return map;
	}
	/**
	 * 选择入库明细信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/putInStorage/hwprintPageList")
	public ModelAndView hwprintPageList(RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putlnStorage_choosePrint");
		List<String> rkids=rkglDto.getRkids();
		mav.addObject("rkids",StringUtil.join(rkids,','));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取出库明细信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/putInStorage/pagedataListHw")
	@ResponseBody
	public Map<String, Object> hwprintPageGetList(HwxxDto hwxxDto){
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos =hwxxService.getDtoList(hwxxDto);
		map.put("rows",hwxxDtos);
		return map;
	}
}
