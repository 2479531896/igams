package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.CpxqjhDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.SczlmxDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.service.svcinterface.ICpxqjhService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.ISczlmxService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.service.svcinterface.IXqjhmxService;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;
import com.matridx.igams.storehouse.dao.entities.WlxxDto;
import com.matridx.igams.storehouse.dao.entities.XzllcglDto;
import com.matridx.igams.storehouse.dao.entities.XzllglDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlcglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.igams.storehouse.service.svcinterface.ILyrkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IWlxxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllcglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storehouse")
public class ReceiveMaterielController extends BaseBasicController {

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private IHwllxxService hwllxxService;
	@Autowired
	ICommonService commonService;
	@Autowired
	ICpxqjhService cpxqjhService;
	@Autowired
	IXqjhmxService xqjhmxService;
	@Autowired
	ILlglService llglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ICkhwxxService ckhwxxService;
	@Autowired
	IXzllcglService xzllcglService;
	@Autowired
	IHwllmxService hwllmxService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ILlcglService llcglService;
	@Override
	public String getPrefix() {
		return urlPrefix;
	}
	@Autowired
	ICkxxService ckxxService;
	
	@Autowired
	IHwxxService hwxxService;
	
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXzllglService xzllglService;
	@Autowired
	IXzllmxService xzllmxService;
	@Autowired
	private IQgglService qgglService;
	@Autowired
	private IQgmxService qgmxService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	IWlglService wlglService;
	@Autowired
	ILyrkxxService lyrkxxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IWlxxService wlxxService;
	@Autowired
	ISczlmxService sczlmxService;
	/**
	 * 领料列表页面
	 *
     */

	@RequestMapping("/receiveMateriel/pageListReceiveMateriel")
	public ModelAndView PageListReceiveMateriel(LlglDto llglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_list");
		Map<String,List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] {
				BasicDataTypeEnum.MATERIELQUALITY_TYPE,	//类别
				BasicDataTypeEnum.MATERIEL_TYPE	//分组
				});
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		mav.addObject("llglDto", llglDto);
		mav.addObject("lblist",jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("fzlist",jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("khlblist",redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 钉钉小程序领料审核查看
	 *
	 */
	@RequestMapping(value = "/receivegoods/minidataDingtalkviewReceivegoods")
	@ResponseBody
	public Map<String,Object> dingtalkviewInspectionGoods(LlglDto llglDto,HttpServletRequest request) {
		User user = getLoginInfo(request);
		Map<String,Object> map=new HashMap<>();
		LlglDto t_llglDto = llglService.getDtoById(llglDto.getLlid());
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setLlid(llglDto.getLlid());
//		List<HwxxDto> hwxxDtos = hwxxService.getDtoListByLlid(hwxxDto);
		List<HwllxxDto> hwllxxDtos = hwllxxService.queryHwllxx(llglDto.getLlid(),"0",user.getDqjs());
		map.put("llglDto", t_llglDto);
		map.put("hwxxDtos", hwllxxDtos);
		return map;
	}

	/**
	 * 获取领料列表
	 *
	 */

	@RequestMapping("/receiveMateriel/pageGetListReceiveMateriel")
	@ResponseBody
	public Map<String, Object> getPagedListReceiveMateriel(LlglDto llglDto) {
		Map<String, Object> map = new HashMap<>();
		List<LlglDto> llglDtos = llglService.getPagedDtoReceiveMaterielList(llglDto);
		map.put("total", llglDto.getTotalNumber());
		map.put("rows", llglDtos);
		return map;
	}
	/**
	 * 获取领料列表
	 *
	 */
	@RequestMapping("/receiveMateriel/pagedataGetListReceiveMateriel")
	@ResponseBody
	public Map<String, Object> pagedataGetListReceiveMateriel(LlglDto llglDto) {
		return getPagedListReceiveMateriel(llglDto);
	}
	/**
	 * 获取领料列表（钉钉）
	 *
	 */

	@RequestMapping("/receiveMateriel/minidataGetPagedReceiveMateriel")
	@ResponseBody
	public Map<String, Object> minidataGetPagedReceiveMateriel(LlglDto llglDto) {
		Map<String, Object> map = new HashMap<>();
		List<LlglDto> llglDtos = llglService.getPagedReceiveMateriel(llglDto);
		map.put("total", llglDto.getTotalNumber());
		map.put("rows", llglDtos);
		return map;
	}

	/**
	 * 查看领料信息
 	*/
	@RequestMapping(value = "/receiveMateriel/viewReceiveMateriel") 
	@ResponseBody
	public ModelAndView getDtoReceiveMaterielByLlid(LlglDto llglDto) { 
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_view"); 
		// 查看 领料信息
		LlglDto llxxDto = llglService.getDtoReceiveMaterielByLlid(llglDto.getLlid()); 
		mav.addObject("llxxDto", llxxDto);
		// 查看 货物领料信息
		List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoHwllxxListByLlidPrint(llglDto.getLlid());
		for (HwllxxDto hwllxxDto : hwllxxDtos) {
			hwllxxDto.setHwllmxDtos(hwllmxService.getDtoHwllmxListByHwllid(hwllxxDto.getHwllid()));
		}
		mav.addObject("hwllxxDtos",hwllxxDtos);
		
		mav.addObject("urlPrefix", urlPrefix);
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",fjcfbDtos);
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		return mav; 
	}
	/**
	 * 查看领料信息
	 */
	@RequestMapping(value = "/receiveMateriel/pagedataReceiveMateriel")
	@ResponseBody
	public ModelAndView pagedataReceiveMateriel(LlglDto llglDto) {
		return getDtoReceiveMaterielByLlid(llglDto);
	}
		/**
         * 获取领料列表（钉钉）
         *
		 */

	@RequestMapping("/receiveMateriel/minidataGetDtoReceiveMateriel")
	@ResponseBody
	public Map<String, Object> minidataGetDtoReceiveMateriel(LlglDto llglDto) {
		Map<String, Object> map = new HashMap<>();
		// 查看 货物领料信息
		List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoHwllmxByLlid(llglDto.getLlid());
		map.put("hwllmxDtos", hwllmxDtos);
		return map;
	}
	/**
	 * 获取领料信息及明细（钉钉）
	 *
	 */
	@RequestMapping("/receiveMateriel/minidataGetReceiveMateriel")
	@ResponseBody
	public Map<String, Object> minidataGetReceiveMateriel(LlglDto llglDto) {
		Map<String, Object> map = new HashMap<>();
		LlglDto llxxDto = llglService.getDtoReceiveMaterielByLlid(llglDto.getLlid());
		HwllxxDto hwllxxDto = new HwllxxDto();
		hwllxxDto.setLlid(llglDto.getLlid());
		List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoList(hwllxxDto);
		// 查看 货物领料信息
		List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoHwllmxByLlid(llglDto.getLlid());
		map.put("llxxDto", llxxDto);
		map.put("hwllxxDtos", hwllxxDtos);
		map.put("hwllmxDtos", hwllmxDtos);
		return map;
	}

	/**
	 * 删除货物领料信息
	*/
	@RequestMapping("/requisition/delRequisition")
	@ResponseBody
	public Map<String,Object> delRequisition(LlglDto llglDto, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		try {
			LyrkxxDto lyrkxxDto=new LyrkxxDto();
			lyrkxxDto.setIds(llglDto.getIds());
			List<LyrkxxDto> dtoList = lyrkxxService.getDtoList(lyrkxxDto);
			if(!CollectionUtils.isEmpty(dtoList)){
				for(LyrkxxDto dto:dtoList){
					BigDecimal sl=new BigDecimal(dto.getSl());
					BigDecimal kcl=new BigDecimal(dto.getKcl());
					if(kcl.compareTo(sl) < 0){
						map.put("status", "fail");
						map.put("message","领料单号为 "+dto.getLldh()+" 的留样库存不足，不允许删除");
						return map;
					}
				}
			}
			// 获取用户信息
			User user = getLoginInfo(request);
			llglDto.setScry(user.getYhid());
			llglDto.setScbj("1");
			boolean isSuccess = llglService.deleteLlgl(llglDto,dtoList);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		} 
		return map;
	}

	/**
	 * 确认审核流程
	 */
	@RequestMapping("/requisition/pagedataConfirmAuditType")
	@ResponseBody
	public Map<String,Object> confirmAuditType(HwxxDto hwxxDto){
		Map<String,Object> map = new HashMap<>();
		if (!CollectionUtils.isEmpty(hwxxDto.getWlids())){
			List<HwxxDto> hwxxDtoList = hwxxService.getGZDtoList(hwxxDto);
			if(!CollectionUtils.isEmpty(hwxxDtoList)){
				map.put("auditType", AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
			}else{
				map.put("auditType", AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
			}
			map.put("status", "success");
		}else{
			map.put("status", "fail");
			map.put("message", "领料信息有误！");
		}
		return map;
	}

	/**
	 * 撤回确认审核流程
	 */
	@RequestMapping("/requisition/pagedataRetractConfirmAuditType")
	@ResponseBody
	public Map<String,Object> retractConfirmAuditType(HwllxxDto hwllxxDto){
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(hwllxxDto.getLlid())){
			List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoList(hwllxxDto);
			if (!CollectionUtils.isEmpty(hwllxxDtos)){
				List<String> wlids = new ArrayList<>();
				for (HwllxxDto hwllxx:hwllxxDtos){
					wlids.add(hwllxx.getWlid());

				}
				HwxxDto hwxxDto1 = new HwxxDto();
				hwxxDto1.setWlids(wlids);
				List<HwxxDto> hwxxDtos = hwxxService.getGZDtoList(hwxxDto1);
				if (!CollectionUtils.isEmpty(hwxxDtos)){
					map.put("auditType", AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
				}else{
					map.put("auditType", AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
				}
				ShgcDto dtoByYwid = shgcService.getDtoByYwid(hwllxxDto.getLlid());
				map.put("auditType", dtoByYwid.getShlb());
				map.put("status", "success");
			}else{
				map.put("status", "fail");
				map.put("message", "获取领料货物信息失败！");
			}
		}else{
			map.put("status", "fail");
			map.put("message", "未获取到领料ID！");
		}
		return map;
	}
	/**
	 * 领料新增页面
	 */
	@RequestMapping("/requisition/pickingcarRequisition")
	public ModelAndView pickingcarRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}		
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/pickingcarSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}
	
	/**
	 * 领料新增页面
	 */
	@RequestMapping("/requisition/addRequisition")
	public ModelAndView addRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}		
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/addSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}

	
	/**
	 * 留样领料新增页面
	 */
	@RequestMapping("/requisition/samplepickRequisition")
	public ModelAndView samplepickRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}		
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/samplepickSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}
	/**
	 * OA留样领料新增页面
	 */
	@RequestMapping("/requisition/oasamplepickRequisition")
	public ModelAndView oasamplepickRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/oasamplepickSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}
	/**
	 * OA留样领料新增保存
     */
	@RequestMapping("/requisition/oasamplepickSaveRequisition")
	@ResponseBody
	public Map<String,Object> oasamplepickSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		return this.addSaveRequisition(request,llglDto);
	}
	/**
	 * 留样领料新增保存
     */
	@RequestMapping("/requisition/samplepickSaveRequisition")
	@ResponseBody
	public Map<String,Object> samplepickSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setLrry(user.getYhid());
		boolean isSuccess = true;
		//判断领料单号是否重复
		isSuccess = llglService.getDtoByLldh(llglDto);
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		String auditType = AuditTypeEnum.AUDIT_GOODS_APPLY.getCode();
		if (StringUtil.isNotBlank(llglDto.getQueryWlids())){
			String[] queryWlids = llglDto.getQueryWlids().split(",");
			List<String> queryWlidlist = Arrays.asList(queryWlids);
			WlglDto wlglDto = new WlglDto();
			wlglDto.setIds(queryWlidlist);
			List<WlglDto> wlglDtos = wlglService.queryByWllxByWlids(wlglDto);
			if (!CollectionUtils.isEmpty(wlglDtos)){
				for (WlglDto dto : wlglDtos) {
					if ("3".equals(dto.getCskz1())) {
						auditType = AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode();
					}
				}
			}
		}
		//新增保存
		try {
			isSuccess = llglService.addSavePicking(llglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",auditType);
			map.put("ywid", llglDto.getLlid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} 
		return map;
	}
	/**
	 * 领料OA新增页面
	 */
	@RequestMapping("/requisition/systemRequisition")
	public ModelAndView systemRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}		
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/systemSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}

	
	/**
	 * 生成纪录编号
	 */
	@RequestMapping("/requisition/pagedataGenerateLljlbh")
	@ResponseBody
	public Map<String, Object> generateLljlbh(LlglDto llglDto) {
		Map<String, Object> map = new HashMap<>();
		String jlbh = llglService.generateLljlbh(llglDto);
		map.put("jlbh",jlbh);
		return map;
	}
	
	/**
	 * 领料物料明细
	 */
	@RequestMapping("/requisition/pagedataPicking")
	@ResponseBody
	public Map<String, Object> getPicking(HwllxxDto hwllxxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		//判断领料id是否为空，不为空是修改，空是新增
		if(StringUtil.isNotBlank(hwllxxDto.getLlid())) {
			List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoList(hwllxxDto);
			String copyFlg = hwllxxDto.getCopyflg();
			if(StringUtil.isBlank(copyFlg)){
				List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoHwllmxListByLlid(hwllxxDto.getLlid());
				map.put("hwllmxDtos",hwllmxDtos);
			}
			map.put("rows", hwllxxDtos);
		}else {
			//若为新增从领料车获取已加入领料车的数据
			LlcglDto llcglDto=new LlcglDto();
			llcglDto.setRyid(user.getYhid());
			List<LlcglDto> llcgllist = llcglService.getLlcDtoList(llcglDto);
			if(!CollectionUtils.isEmpty(llcgllist)) {
				map.put("rows", llcgllist);
			}else {
				map.put("rows", null);
			}
		}
		return map;
	}


	/**
	 * 领料物料明细
	 */
	@RequestMapping("/requisition/pagedataGetCopyPicking")
	@ResponseBody
	public Map<String, Object> getCopyPicking(HwllxxDto hwllxxDto) {
		Map<String, Object> map = new HashMap<>();
		//判断领料id是否为空，不为空是修改，空是新增
		if(StringUtil.isNotBlank(hwllxxDto.getLlid())) {
			List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoCopyList(hwllxxDto);
			for(HwllxxDto dto:hwllxxDtos){
				dto.setCopyflg("1");
			}
			List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoHwllmxListByLlid(hwllxxDto.getLlid());
			map.put("rows", hwllxxDtos);
			map.put("hwllmxDtos",hwllmxDtos);
		}
		return map;
	}
	
	/**
	 * 根据输入信息查询物料
     */
	@RequestMapping("/requisition/pagedataQueryWlxx")
	@ResponseBody
	public Map<String,Object> queryWlxx(CkhwxxDto ckhwxxDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		List<CkhwxxDto> ckhwxxDtos = ckhwxxService.queryWlxx(ckhwxxDto,user.getDqjs());
		Map<String,Object> map = new HashMap<>();
		map.put("ckhwxxDtos", ckhwxxDtos);
		return map;
	}
	
	/**
	 * 查询要添加的物料信息
     */
	@RequestMapping("/requisition/pagedataQueryWlxxByWlid")
	@ResponseBody
	public Map<String,Object> queryWlxxByWlid(CkhwxxDto ckhwxxDto){
		CkhwxxDto ckhwxxDto_t = ckhwxxService.queryWlxxByWlid(ckhwxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("ckhwxxDto", ckhwxxDto_t);
		return map;
	}
	
	/**
	 * 领料OA新增保存
     */
	@RequestMapping("/requisition/systemSaveRequisition")
	@ResponseBody
	public Map<String,Object> systemSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		return this.addSaveRequisition(request,llglDto);
	}
	/**
	 * 领料新增保存
     */
	@RequestMapping("/requisition/addSaveRequisition")
	@ResponseBody
	public Map<String,Object> addSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setLrry(user.getYhid());
		boolean isSuccess;
		//判断领料单号是否重复
		isSuccess = llglService.getDtoByLldh(llglDto);
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		String auditType = AuditTypeEnum.AUDIT_GOODS_APPLY.getCode();
		if (StringUtil.isNotBlank(llglDto.getQueryWlids())){
			String[] queryWlids = llglDto.getQueryWlids().split(",");
			List<String> queryWlidlist = Arrays.asList(queryWlids);
			WlglDto wlglDto = new WlglDto();
			wlglDto.setIds(queryWlidlist);
			List<WlglDto> wlglDtos = wlglService.queryByWllxByWlids(wlglDto);
			if (!CollectionUtils.isEmpty(wlglDtos)){
				for (WlglDto dto : wlglDtos) {
					if ("3".equals(dto.getCskz1())) {
						auditType = AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode();
					}
				}
			}
		}
		//新增保存
		try {
			isSuccess = llglService.addSavePicking(llglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",auditType);
			map.put("ywid", llglDto.getLlid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} 
		return map;
	}
	
	/**
	 * 领料新增保存
     */
	@RequestMapping("/requisition/pickingcarSaveRequisition")
	@ResponseBody
	public Map<String,Object> pickingcarSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		return this.addSaveRequisition(request,llglDto);
	}
	
	/**
	 * 领料审核页面
	 */
	@RequestMapping("/requisition/auditRequisition")
	public ModelAndView auditRequisition(LlglDto llglDto) {
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/modSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING,BasicDataTypeEnum.DELIVERY_METHOD});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("ysfslist", jclist.get(BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运输方式
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));// 省份
		mav.addObject("cplxlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));// 产品类型
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		llglDto_t.setXsbj("1");
		mav.addObject("llglDto", llglDto_t);
		return mav;
	}
	
	/**
	 * 领料修改页面
	 */
	@RequestMapping("/requisition/modRequisition")
	public ModelAndView modRequisition(LlglDto llglDto) {
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/modSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING,BasicDataTypeEnum.DELIVERY_METHOD});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("ysfslist", jclist.get(BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运输方式
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));// 省份
		mav.addObject("cplxlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));// 产品类型
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		llglDto_t.setXsbj("1");
		llglDto_t.setXzbj(llglDto.getXzbj());
		mav.addObject("llglDto", llglDto_t);
		return mav;
	}
	/**
	 * 领料提交页面
	 */
	@RequestMapping("/requisition/resubmitRequisition")
	public ModelAndView resubmitRequisition(LlglDto llglDto) {
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/modSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING,BasicDataTypeEnum.DELIVERY_METHOD});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("ysfslist", jclist.get(BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运输方式
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));// 省份
		mav.addObject("cplxlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));// 产品类型
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		llglDto_t.setXsbj("1");
		llglDto_t.setXzbj(llglDto.getXzbj());
		mav.addObject("llglDto", llglDto_t);
		return mav;
	}

	/**
	 * 领料修改保存
     */
	@RequestMapping("/requisition/modSaveRequisition")
	@ResponseBody
	public Map<String,Object> savemodRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setXgry(user.getYhid());
		boolean isSuccess;
		//判断领料单号是否重复
		isSuccess = llglService.getDtoByLldh(llglDto);
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		String auditType = AuditTypeEnum.AUDIT_GOODS_APPLY.getCode();
		if (StringUtil.isNotBlank(llglDto.getQueryWlids())){
			String[] queryWlids = llglDto.getQueryWlids().split(",");
			List<String> queryWlidlist = Arrays.asList(queryWlids);
			WlglDto wlglDto = new WlglDto();
			wlglDto.setIds(queryWlidlist);
			List<WlglDto> wlglDtos = wlglService.queryByWllxByWlids(wlglDto);
			if (!CollectionUtils.isEmpty(wlglDtos)){
				for (WlglDto dto : wlglDtos) {
					if ("3".equals(dto.getCskz1())) {
						auditType = AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode();
					}
				}
			}
		}
		//修改保存
		try {
			isSuccess = llglService.modSavePicking(llglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr()+"或许是因为存在了相同仓库的物料！");
			map.put("auditType",auditType);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 领料审核页面
	 */
	@RequestMapping("/requisition/pagedataSeniorModRequisition")
	public ModelAndView seniorModRequisition(LlglDto llglDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_senioredit");
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/saveseniormodRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING,BasicDataTypeEnum.DECONTAMINATION_METHOD});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		llglDto_t.setXsbj("2");
		llglDto_t.setXzbj(llglDto.getXzbj());
		List<HwllxxDto> hwllxxDtos = hwllxxService.queryHwllxx(llglDto.getLlid(),"0",user.getDqjs());
		List<HwxxDto> hwxxDtos = new ArrayList<>();
		for (HwllxxDto hwllxxDto : hwllxxDtos) {
			hwxxDtos.addAll(hwllxxDto.getHwxxDtos());
		}
		llglDto_t.setHwllmx_json(JSONObject.toJSONString(hwxxDtos));
		mav.addObject("llglDto", llglDto_t);
		mav.addObject("hwllxxDtos", hwllxxDtos);
		//访问标记
		mav.addObject("fwbj", request.getParameter("fwbj"));
		mav.addObject("qwfslist", jclist.get(BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode()));
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		return mav;
	}
	
	/**
	 * 领料审核保存
     */
	@RequestMapping("/requisition/saveseniormodRequisition")
	@ResponseBody
	public Map<String,Object> saveseniormodRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setXgry(user.getYhid());
		boolean isSuccess = true;
		//判断领料单号是否重复
		isSuccess = llglService.getDtoByLldh(llglDto);
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		//修改保存
		try {
			isSuccess = llglService.seniormodSavePicking(llglDto);
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
	 * 领料出库页面
	 */
	@RequestMapping("/requisition/deliveryRequisition")
	public ModelAndView deliveryRequisition(LlglDto llglDto, HttpServletRequest request) {
		//获取当前用户
		User user = getLoginInfo(request);
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		llglDto_t.setLlr(llglDto_t.getSqry());
		llglDto_t.setLlrmc(llglDto_t.getSqrmc());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_delivery");
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/deliverySaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		CkxxDto ckxxDto = new CkxxDto();
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto.setCsdm("CK");
		JcsjDto jcsj = jcsjService.getByAndCsdm(jcsjDto);
		ckxxDto.setCklb(jcsj.getCsid());
//		     WAREHOUSE_TYPE
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		mav.addObject("cklist", ckxxDtos);
		List<HwllxxDto> hwllxxDtos = hwllxxService.queryHwllxx(llglDto.getLlid(),"1",user.getDqjs());
		List<HwxxDto> hwxxDtos = new ArrayList<>();
		for (HwllxxDto hwllxxDto : hwllxxDtos) {
			hwxxDtos.addAll(hwllxxDto.getHwxxDtos());
		}
		llglDto_t.setHwllmx_json(JSONObject.toJSONString(hwxxDtos));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		llglDto_t.setCkrq(sdf.format(date));

		llglDto_t.setFlry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto_t.setFlrmc(user.getZsxm());
		}
		String ckdh = llglService.generateCkdh(llglDto); //生成领料单
		llglDto_t.setCkdh(ckdh);
		mav.addObject("hwllxxDtos", hwllxxDtos);
		mav.addObject("llglDto", llglDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 领料出库保存
     */
	@RequestMapping("/requisition/deliverySaveRequisition")
	@ResponseBody
	public Map<String,Object> deliveryRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setXgry(user.getYhid());
		llglDto.setZsxm(user.getZsxm());
		boolean isSuccess = true;
		//修改保存
		try {
			isSuccess = llglService.deliverymodSavePicking(llglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_DELIVERY.getCode());
			map.put("ywid", llglDto.getLlid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 打开打印材料领料单页面
	 */
	@RequestMapping("/requisition/lldprintMaterialrequisition")
	public ModelAndView printMaterialRequisition(LlglDto llglDto) {
		ModelAndView mav = null;
		if ("/labigams".equals(urlPrefix)){
			mav=new ModelAndView("storehouse/receiveMateriel/receiveMateriel_printprintOAMaterialRequisition.html");
		}else {
			mav=new ModelAndView("storehouse/receiveMateriel/receiveMateriel_printprintMaterialRequisition.html");
		}
		// 查看 领料信息
		List<LlglDto> llglDtoList= llglService.getDtoReceiveMaterielWithPrint(llglDto);
		for (LlglDto dto : llglDtoList) {
			// 查看 货物领料信息
			List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoHwllmxListByPrint(dto.getLlid());
			dto.setHwllmxDtos(hwllmxDtos);
		}
		mav.addObject("llglDtoList", llglDtoList);
		mav.addObject("urlPrefix", urlPrefix);
		//根据基础数据获取打印信息	
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("LL");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
		mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		mav.addObject("cskz1",StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		mav.addObject("cskz2", StringUtil.isNotBlank(jcsj.getCskz2())?jcsj.getCskz2():"");
		
		return mav;
	}

	/**
	 * 全部领料列表
	 */

	@RequestMapping("/requisition/pageListRequisitionAll")
	public ModelAndView pageListRequisitionAll() {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_AllList");
		Map<String,List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] {
				BasicDataTypeEnum.MATERIELQUALITY_TYPE,	//类别
				BasicDataTypeEnum.MATERIEL_TYPE	//分组
		});
		LlglDto t_llglDto = new LlglDto();
		t_llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		mav.addObject("llglDto", t_llglDto);
		mav.addObject("lblist",jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("fzlist",jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取全部领料列表
	 *
	 */

	@RequestMapping("/requisition/pageGetListRequisitionAll")
	@ResponseBody
	public Map<String, Object> getPagedAllListReceiveMateriel(LlglDto llglDto) {
		Map<String, Object> map = new HashMap<>();
		List<LlglDto> llglDtos = llglService.getPagedDtoReceiveMaterielList(llglDto);
		map.put("total", llglDto.getTotalNumber());
		map.put("rows", llglDtos);
		return map;
	}

	/**
	 * 领料列表废弃功能
	 */

	@ResponseBody
	@RequestMapping(value = "/")
	public Map<String,Object> discardContractView(LlglDto llglDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			LyrkxxDto lyrkxxDto=new LyrkxxDto();
			lyrkxxDto.setIds(llglDto.getIds());
			List<LyrkxxDto> dtoList = lyrkxxService.getDtoList(lyrkxxDto);
			if(!CollectionUtils.isEmpty(dtoList)){
				for(LyrkxxDto dto:dtoList){
					BigDecimal sl=new BigDecimal(dto.getSl());
					BigDecimal kcl=new BigDecimal(dto.getKcl());
					if(kcl.compareTo(sl) < 0){
						map.put("status", "fail");
						map.put("message","领料单号为 "+dto.getLldh()+" 的留样库存不足，不允许删除");
						return map;
					}
				}
			}
			//获取用户信息
			User user = getLoginInfo(request);
			llglDto.setScry(user.getYhid());
			llglDto.setScbj("2");
			boolean isSuccess = llglService.deleteLlgl(llglDto,dtoList);
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

	/**
	 * 选择追溯号页面
	 */
	@RequestMapping("/receiveMateriel/pagedataChooseZsh")
	public ModelAndView chooseZsh(LlglDto llglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_chooseZsh");
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("llglDto",llglDto);
		return mav;
	}
	
	/**
	 * 获取货物信息数据
	 */
	@ResponseBody
	@RequestMapping(value = "/receiveMateriel/pagedataQueryHwxx")
	public Map<String,Object> queryHwxx(HwxxDto hwxxDto){
		Map<String,Object> map=new HashMap<>();
		List<HwxxDto> hwxxDtos=hwxxService.queryHWxx(hwxxDto,"1");
		map.put("rows", hwxxDtos);
		return map;
	}
	
	//===========================领料导入=============================
	/**
	 * 领料导入页面
     */
	@RequestMapping("/materialImport/pageListMaterialimport")
	public ModelAndView importSamp(){
		ModelAndView mav = new ModelAndView("storehouse/materialAudit/material_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_REQUISITION.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 领料高级修改页面
	 */
	@RequestMapping("/requisition/advancedmodRequisition")
	public ModelAndView advancedmodRequisition(LlglDto llglDto) {
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/advancedmodSaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		llglDto_t.setXsbj("1");
		llglDto_t.setXzbj(llglDto.getXzbj());
		mav.addObject("llglDto", llglDto_t);
		return mav;
	}
	/**
	 * 领料修改保存
     */
	@RequestMapping("/requisition/advancedmodSaveRequisition")
	@ResponseBody
	public Map<String,Object> advancedmodSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setXgry(user.getYhid());
		boolean isSuccess = true;
		//判断领料单号是否重复
		isSuccess = llglService.getDtoByLldh(llglDto);
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		//修改保存
		try {
			isSuccess = llglService.AdvancedmodSavePicking(llglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 行政领料列表页面
     */

	@RequestMapping("/receiveAdministrationMateriel/pageListReceiveAdministrationMateriel")
	public ModelAndView PageListReceiveAdministrationMateriel() {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_list");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.LOCATION_CATEGORY, BasicDataTypeEnum.LOCATION_CATEGORY ,BasicDataTypeEnum.LOCATION_CATEGORY});
		mav.addObject("kwlblist", jclist.get(BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
		XzllglDto xzllglDto = new XzllglDto();
		xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		mav.addObject("xzllglDto", xzllglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取领料列表
	 */
	@RequestMapping("/receiveAdministrationMateriel/pageGetListReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String, Object> getPagedListReceiveAdministrationMateriel(XzllglDto xzllglDto) {
		Map<String, Object> map = new HashMap<>();
		List<XzllglDto> xzllglDtos = xzllglService.getPagedDtoListReceiveAdministrationMateriel(xzllglDto);
		map.put("total", xzllglDto.getTotalNumber());
		map.put("rows", xzllglDtos);
		return map;
	}
	/**
	 * 获取钉钉行政领料列表
	 */
	@RequestMapping("/receiveAdministrationMateriel/minidataGetListReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String, Object> minidataGetListReceiveAdministrationMateriel(XzllglDto xzllglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User loginInfo = getLoginInfo(request);
		xzllglDto.setJgid(loginInfo.getJgid());
		List<XzllglDto> xzllglDtos = xzllglService.getPagedDtoListReceiveAdministrationMateriel(xzllglDto);
		map.put("total", xzllglDto.getTotalNumber());
		map.put("rows", xzllglDtos);
		return map;
	}
	/**
	 * 领料新增页面
	 */
	@RequestMapping("/requisition/addXzRequisition")
	public ModelAndView addXzRequisition(XzllglDto xzllglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		xzllglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			xzllglDto.setSqrmc(user.getZsxm());
			xzllglDto.setSqbmmc(user.getJgmc());
			xzllglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			xzllglDto.setSqrq(sdf.format(date));//申请时间
			xzllglDto.setJgid(user.getJgid());
			xzllglDto.setJgdm(user.getJgdm());
		}
		xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		if ("1".equals(xzllglDto.getLllx())){
			xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode());
		}
		String lldh = xzllglService.generateDjh(); //生成领料单
		xzllglDto.setLldh(lldh);
		xzllglDto.setFormAction("/storehouse/requisition/saveAddXzRequisition");
		mav.addObject("url", "/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xzllglDto", xzllglDto);
		return mav;
	}

	/**
	 * 行政领料新增保存
	 */
	@RequestMapping("/requisition/saveAddXzRequisition")
	@ResponseBody
	public Map<String,Object> saveAddXzRequisition(HttpServletRequest request,XzllglDto xzllglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		xzllglDto.setLrry(user.getYhid());
		xzllglDto.setLlry(xzllglDto.getSqry());
		boolean isSuccess = true;
		//判断领料单号是否重复
		isSuccess = xzllglService.getDtoByLldh(xzllglDto) == null;
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		//新增保存
		try {
			if(StringUtils.isBlank(xzllglDto.getXzllid())) {
				xzllglDto.setXzllid(StringUtil.generateUUID());
			}
			isSuccess = xzllglService.addSaveXzPicking(xzllglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
			map.put("ywid", xzllglDto.getXzllid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 领料货物明细
	 */
	@RequestMapping("/requisition/pagedataGetXzPicking")
	@ResponseBody
	public Map<String, Object> getXzPicking(String xzllid,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<XzllmxDto> xzllmxDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(xzllid)){
			xzllmxDtos = xzllmxService.getDtoXzllmxListByXzllid(xzllid);
			map.put("rows", xzllmxDtos);
		}else{
			XzllcglDto xzllcglDto=new XzllcglDto();
			User user=getLoginInfo(request);
			xzllcglDto.setRyid(user.getYhid());
			List<XzllcglDto> dtoList = xzllcglService.getDtoList(xzllcglDto);
			if(!CollectionUtils.isEmpty(dtoList)){
				for(XzllcglDto dto:dtoList){
					dto.setQlsl(dto.getKlsl());
				}
			}
			map.put("rows", dtoList);
		}
		return map;
	}

	/**
	 * 查看领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/viewReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView getDtoReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_view");
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		mav.addObject("xzllxxDto", xzllxxDto);
		// 查看 行政领料明细信息
		List<XzllmxDto> xzllmxDtos = xzllmxService.getDtoXzllmxListByXzllid(xzllxxDto.getXzllid());

		mav.addObject("xzllmxDtos",xzllmxDtos);

		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 钉钉查看行政领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/minidataViewReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String, Object> minidataViewReceiveAdministrationMateriel(XzllglDto xzllglDto) {
		Map<String, Object> map = new HashMap<>();
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		map.put("xzllxxDto", xzllxxDto);
		// 查看 行政领料明细信息
		List<XzllmxDto> xzllmxDtos = xzllmxService.getDtoXzllmxListByXzllid(xzllxxDto.getXzllid());
		map.put("xzllmxDtos",xzllmxDtos);
		map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	/**
	 * 新增行政领料信息
	 */
	@RequestMapping(value = "/stock/pickingCarAdministrationMateriel")
	@ResponseBody
	public ModelAndView pickingCarAdministrationMateriel(XzllglDto xzllglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		// 查看 行政领料信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		xzllglDto.setSqrq(sdf.format(date));
		User user = getLoginInfo(request);
		xzllglDto.setLlry(user.getYhid());// 默认
		xzllglDto.setLlrymc(user.getZsxm());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			xzllglDto.setSqbm(user.getJgid());//申请部门
			xzllglDto.setSqbmmc(user.getJgmc());
		}
		xzllglDto.setLldh(xzllglService.generateDjh());
		xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		xzllglDto.setFormAction("/storehouse/receiveAdministrationMateriel/pagedataSaveReceiveAdminMateriel");
		mav.addObject("xzllglDto", xzllglDto);
		mav.addObject("url","/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("loadFlag","add");
		return mav;
	}
	/**
	 * 新增行政领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/addReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView addReceiveAdministrationMateriel(XzllglDto xzllglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		// 查看 行政领料信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		xzllglDto.setSqrq(sdf.format(date));
		User user = getLoginInfo(request);
		xzllglDto.setLlry(user.getYhid());// 默认
		xzllglDto.setLlrymc(user.getZsxm());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			xzllglDto.setSqbm(user.getJgid());//申请部门
			xzllglDto.setSqbmmc(user.getJgmc());
		}
		xzllglDto.setLldh(xzllglService.generateDjh());
		xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		if ("1".equals(xzllglDto.getLllx())){
			xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode());
		}
		xzllglDto.setFormAction("/storehouse/receiveAdministrationMateriel/pagedataSaveReceiveAdminMateriel");
		mav.addObject("xzllglDto", xzllglDto);
		mav.addObject("url","/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("loadFlag","mod");
		return mav;
	}
	/**
	 * 设备领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/devicemateringReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView devicemateringReceiveAdministrationMateriel(XzllglDto xzllglDto,HttpServletRequest request) {
		return addReceiveAdministrationMateriel(xzllglDto,request);
	}
	/**
	 * 跳转请购选择列表
	 *
     */
	@RequestMapping(value = "/receiveAdministrationMateriel/pagedataListChoosePicking")
	public ModelAndView chooseAdminPurchaseList(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrainMaterial_chooseRequisition");
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 跳转请购明细列表
	 *
     */
	@RequestMapping(value = "/receiveAdministrationMateriel/pagedataChoosePickingListInfo")
	public ModelAndView cchoosePickingListInfo(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrainMaterial_chooseRequisitionInfo");
		QgglDto qgglDto = qgglService.getDtoById(qgmxDto.getQgid());
		List<QgmxDto> qgmxDtos = qgmxService.getQgmxListByQgidAndQgmxid(qgmxDto);
		mav.addObject("qgmxDtos", qgmxDtos);
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 获取请购列表数据
	 *
     */
	@RequestMapping("/receiveAdministrationMateriel/pagedataListRequisition")
	@ResponseBody
	public Map<String, Object> getPageListRequisition(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		qgglDto.setZt("80");
		List<QgglDto> list = qgglService.getPagedListAdminStock(qgglDto);
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 获取所选请购单数据
	 *
     */
	@RequestMapping("/receiveAdministrationMateriel/pagedataGetRequisitionList")
	@ResponseBody
	public Map<String, Object> getRequisitionList(QgmxDto qgmxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if ("1".equals(request.getParameter("flag"))){
			List<QgmxDto> qgmxDtos=qgmxService.getQgmxListByQgidAndQgmxid(qgmxDto);
			map.put("rows", qgmxDtos);
		}
		else {
			List<QgmxDto> qgmxDtos = qgmxService.getXzkcList(qgmxDto);
			map.put("rows", qgmxDtos);
		}
		return map;
	}


	/**
	 * 新增保存
	 *
     */
	@RequestMapping("/receiveAdministrationMateriel/pagedataSaveReceiveAdminMateriel")
	@ResponseBody
	public Map<String,Object> addSaveReceiveAdminMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo(request);
		xzllglDto.setLrry(user.getYhid());
		XzllglDto dtoByLldh = xzllglService.getDtoByLldh(xzllglDto);
		if (dtoByLldh!=null){
			map.put("status","fail");
			map.put("message","领料单号不允许重复！");
			return map;
		}
		try {
			boolean isSuccess = xzllglService.addReceiveAdminMateriel(xzllglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
			if ("1".equals(xzllglDto.getLllx())){
				map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode());
			}
			map.put("ywid", xzllglDto.getXzllid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 钉钉新增保存
	 *
     */
	@RequestMapping("/receiveAdministrationMateriel/minidataSaveReceiveAdminMateriel")
	@ResponseBody
	public Map<String,Object> minidataSaveReceiveAdminMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		xzllglDto.setSqrq(sdf.format(date));
		User user = getLoginInfo(request);
		xzllglDto.setLlry(user.getYhid());// 默认
		xzllglDto.setLlrymc(user.getZsxm());
		user = commonService.getUserInfoById(user);
		xzllglDto.setSqbm(user.getJgid());//申请部门
		xzllglDto.setSqbmmc(user.getJgmc());
		xzllglDto.setLldh(xzllglService.generateDjh());
		// 获取用户信息
		xzllglDto.setLrry(user.getYhid());
		try {
			boolean isSuccess = xzllglService.addReceiveAdminMateriel(xzllglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
			map.put("ywid", xzllglDto.getXzllid());
			map.put("lldh", xzllglDto.getLldh());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 提交行政领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/submitReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView submitReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		xzllxxDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		if ("1".equals(xzllxxDto.getLllx())){
			xzllxxDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode());
		}
		xzllxxDto.setFormAction("/storehouse/receiveAdministrationMateriel/pagedataModSaveReceiveAdminMateriel");
		mav.addObject("xzllglDto", xzllxxDto);
		mav.addObject("url","/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("loadFlag","mod");
		return mav;
	}
	/**
	 * 审核行政领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/auditReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView auditReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		xzllxxDto.setTssh(xzllglDto.getTssh());
		xzllxxDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		if ("1".equals(xzllxxDto.getLllx())){
			xzllxxDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode());
		}
		xzllxxDto.setFormAction("/storehouse/receiveAdministrationMateriel/pagedataModSaveReceiveAdminMateriel");
		mav.addObject("xzllglDto", xzllxxDto);
		mav.addObject("url","/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("loadFlag","mod");
		return mav;
	}
	/**
	 * 修改行政领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/modReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView modReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		xzllxxDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		xzllxxDto.setFormAction("/storehouse/receiveAdministrationMateriel/pagedataModSaveReceiveAdminMateriel");
		mav.addObject("xzllglDto", xzllxxDto);
		mav.addObject("url","/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("loadFlag","mod");
		return mav;
	}
	/**
	 * 钉钉提交行政领料页面信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/minidataSubmitReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String,Object> minidataSubmitReceiveAdministrationMateriel(XzllglDto xzllglDto) {
		Map<String,Object> map = new HashMap<>();
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		List<XzllmxDto> xzllmxDtos = xzllmxService.getDtoXzllmxListByXzllid(xzllglDto.getXzllid());
		map.put("xzllglDto", xzllxxDto);
		map.put("xzllmxDtos", xzllmxDtos);
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	/*
	 * 修改保存行政领料信息
	 */
	@RequestMapping("/receiveAdministrationMateriel/pagedataModSaveReceiveAdminMateriel")
	@ResponseBody
	public Map<String,Object> modSaveReceiveAdminMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo(request);
		xzllglDto.setXgry(user.getYhid());
		XzllglDto dtoByLldh = xzllglService.getDtoByLldh(xzllglDto);
		if (dtoByLldh!=null){
			map.put("status","fail");
			map.put("message","领料单号不允许重复！");
			return map;
		}
		try {
			boolean isSuccess = xzllglService.modReceiveAdminMateriel(xzllglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
			if ("1".equals(xzllglDto.getLllx())){
				map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode());
			}
			map.put("ywid", xzllglDto.getXzllid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 出库行政领料信息
	 */
	@RequestMapping(value = "/receiveAdministrationMateriel/releaseReceiveAdministrationMateriel")
	@ResponseBody
	public ModelAndView releaseReceiveAdministrationMateriel(XzllglDto xzllglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_edit");
		// 查看 行政领料信息
		XzllglDto xzllxxDto = xzllglService.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
		xzllxxDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		xzllxxDto.setFormAction("/storehouse/receiveAdministrationMateriel/pagedataReleaseSaveReceiveAdminMateriel");
		User user = getLoginInfo(request);
		xzllxxDto.setFlry(user.getYhid());// 默认
		xzllxxDto.setFlrymc(user.getZsxm());
		mav.addObject("xzllglDto", xzllxxDto);
		mav.addObject("url","/storehouse/requisition/pagedataGetXzPicking");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("loadFlag","mod");
		return mav;
	}

	/*
	 * 出库保存行政领料信息
	 */
	@RequestMapping("/receiveAdministrationMateriel/pagedataReleaseSaveReceiveAdminMateriel")
	@ResponseBody
	public Map<String,Object> releaseSaveReceiveAdminMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo(request);
		xzllglDto.setXgry(user.getYhid());
		try {
			boolean isSuccess = xzllglService.releaseSaveReceiveAdminMateriel(xzllglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
			map.put("ywid", xzllglDto.getXzllid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}


	/*
	 * 删除行政领料信息
	 */
	@RequestMapping("/receiveAdministrationMateriel/discardReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String,Object> discardReceiveAdministrationMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo(request);
		xzllglDto.setScry(user.getYhid());
		boolean isSuccess = xzllglService.delReceiveAdministrationMateriel(xzllglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/*
	 * 删除行政领料信息
	 */
	@RequestMapping("/receiveAdministrationMateriel/delReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String,Object> delReceiveAdministrationMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo(request);
		xzllglDto.setScry(user.getYhid());
		boolean isSuccess = xzllglService.delReceiveAdministrationMateriel(xzllglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/*
	 * 钉钉废弃行政领料信息
	 */
	@RequestMapping("/receiveAdministrationMateriel/minidataDelReceiveAdministrationMateriel")
	@ResponseBody
	public Map<String,Object> minidataDelReceiveAdministrationMateriel(XzllglDto xzllglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		// 获取用户信息
		User user = getLoginInfo(request);
		xzllglDto.setScry(user.getYhid());
		boolean isSuccess = xzllglService.delReceiveAdministrationMateriel(xzllglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 行政领料审核列表页面
     */

	@RequestMapping("/receiveAdministrationMateriel/pageListReceiveAdministrationAuditing")
	public ModelAndView PageListAdministrAtionuditing() {
		ModelAndView mav = new ModelAndView("storehouse/AdministrationAuditing/AdministrationAuditing_list");
		XzllglDto xzllglDto = new XzllglDto();
		xzllglDto.setAuditType(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
		mav.addObject("xzllglDto", xzllglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取领料审核列表
	 */
	@RequestMapping("/receiveAdministrationMateriel/pageGetListReceiveAdministrationAuditing")
	@ResponseBody
	public Map<String, Object> getPagedListAdministrAtionuditing(XzllglDto xzllglDto,HttpServletRequest request) {
		//附加委托参数
		DataPermission.addWtParam(xzllglDto);
		List<AuditTypeEnum> auditTypes=new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_ADMINISTRATION);
		auditTypes.add(AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(xzllglDto.getDqshzt())){
			DataPermission.add(xzllglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "xzllgl", "xzllid", auditTypes);
		}else{
			DataPermission.add(xzllglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "xzllgl", "xzllid", auditTypes);
		}
		DataPermission.addCurrentUser(xzllglDto, getLoginInfo(request));
		List<XzllglDto> xzllglDtos = xzllglService.getPagedAuditXzllgl(xzllglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", xzllglDto.getTotalNumber());
		map.put("rows", xzllglDtos);
		return map;
	}
	
		/**
	 * 需求计划列表页面
	 *
         */
	@RequestMapping(value = "/receiveMateriel/pageListProgress")
	public ModelAndView pageListProgress() {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdministrationMateriel_progress");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 需求计划列表页面
	 *
     */
	@RequestMapping(value = "/receiveMateriel/pagedataProgress")
	public ModelAndView pagedataProgress() {
		return pageListProgress();
	}
	/**
	 * 跳转需求明细列表
	 *
	 */
	@RequestMapping(value = "/receiveMateriel/pagedataChooseListProgressInfo")
	public ModelAndView chooseListPurchaseInfo(CpxqjhDto cpxqjhDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/progress_chooseInfoList");
		CpxqjhDto dtoById = cpxqjhService.getDtoById(cpxqjhDto.getCpxqid());
		XqjhmxDto xqjhmxDto = new XqjhmxDto();
		xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
		List<XqjhmxDto> dtoList = xqjhmxService.getDtoList(xqjhmxDto);
		mav.addObject("dtoList", dtoList);
		mav.addObject("cpxqjhDto", dtoById);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取需求明细数据
	 */
	@RequestMapping("/receiveMateriel/pagedataProgressListInfo")
	@ResponseBody
	public Map<String,Object> ProgressListInfo(XqjhmxDto xqjhmxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(xqjhmxDto.getLllb())&&"1".equals(xqjhmxDto.getLllb())){
			User user = getLoginInfo(request);
			xqjhmxDto.setJsid(user.getDqjs());
		}
		List<XqjhmxDto> dtoList = xqjhmxService.getDtoList(xqjhmxDto);
		map.put("status", "success");
		map.put("dtoList",dtoList);
		return map;
	}
	/**
	 * 获取货物领料信息
	 */
	@RequestMapping("/receiveMateriel/minidataGetHwllxx")
	@ResponseBody
	public HwllxxDto minidataGetHwllxx(HwllxxDto hwllxxDto) {
        return hwllxxService.getDtoById(hwllxxDto.getHwllid());
	}
	/**
	 * 销售领料新增页面
	 */
	@RequestMapping("/requisition/salepickRequisition")
	public ModelAndView salepickRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/salepickSaveRequisition");
		redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode());
		mav.addObject("cklblist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("ysfslist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运输方式
		mav.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));// 省份
		mav.addObject("xmdllist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("cplxlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));// 产品类型
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}
	/**
	 * 销售领料新增保存
     */
	@RequestMapping("/requisition/salepickSaveRequisition")
	@ResponseBody
	public Map<String,Object> salepickSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		return this.addSaveRequisition(request,llglDto);
	}
	/**
	 * 物流维护
	 */
	@RequestMapping("/requisition/logisticsupholdPage")
	public ModelAndView logisticsupholdPage(WlxxDto wlxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/logisticsuphold_Page");
		wlxxDto.setFormAction("/storehouse/requisition/logisticsupholdSavePage");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlxxDto", wlxxDto);
		mav.addObject("flag", "wlwh");
		return mav;
	}
	/**
	 * 物流维护保存
	 */
	@RequestMapping("/requisition/logisticsupholdSavePage")
	@ResponseBody
	public Map<String,Object> logisticsupholdSavePage(WlxxDto wlxxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		wlxxDto.setLrry(user.getYhid());
		boolean isSuccess = false;
		try {
			isSuccess = wlxxService.logisticsupholdSave(wlxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}
	/**
	 * 物流签收
	 */
	@RequestMapping("/requisition/signforPage")
	public ModelAndView signforPage(WlxxDto wlxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/logisticsuphold_Page");
		wlxxDto.setFormAction("/storehouse/requisition/signforSavePage");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlxxDto", wlxxDto);
		mav.addObject("flag", "wlqs");
		return mav;
	}
	/**
	 * 物流签收保存
	 */
	@RequestMapping("/requisition/signforSavePage")
	@ResponseBody
	public Map<String,Object> signforSavePage(WlxxDto wlxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = false;
		try {
			isSuccess = wlxxService.signforSave(wlxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}
	/**
	 * 物流签收确认
	 */
	@RequestMapping("/requisition/signforconfirmPage")
	public ModelAndView signforconfirmPage(WlxxDto wlxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/logisticsuphold_Page");
		wlxxDto.setFormAction("/storehouse/requisition/signforconfirmSavePage");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlxxDto", wlxxDto);
		mav.addObject("flag", "wlqsqr");
		return mav;
	}
	/**
	 * 物流签收保存
	 */
	@RequestMapping("/requisition/signforconfirmSavePage")
	@ResponseBody
	public Map<String,Object> signforconfirmSavePage(WlxxDto wlxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = false;
		try {
			isSuccess = wlxxService.signforconfirmSave(wlxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}
	/**
	 * 获取物流信息
	 */
	@RequestMapping("/requisition/pagedataGetWlxxByYwid")
	@ResponseBody
	public Map<String, Object> pagedataGetWlxxByYwid(WlxxDto wlxxDto){
		Map<String, Object> map = new HashMap<>();
		List<WlxxDto> dtoList = wlxxService.getDtoListById(wlxxDto.getYwid());
		map.put("rows",dtoList);
		map.put("total",wlxxDto.getTotalNumber());
		return map;
	}

	/**
	 * 附件查看
	 */
	@RequestMapping("/requisition/pagedataViewFj")
	public ModelAndView uploadQyxxFile(FjcfbDto fjcfbDto) {
		ModelAndView mav=new ModelAndView("production/retention/retention_uploadQyxxFile");
		List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 领料复制页面
	 */
	@RequestMapping("/requisition/copyRequisition")
	public ModelAndView copyRequisition(LlglDto llglDto) {
		LlglDto llglDto_t = llglService.getDtoById(llglDto.getLlid());
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto_t);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto_t.setJlbh(jlbh);
		}
		llglDto_t.setLldh(lldh);
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBPICKING.getCode());
		fjcfbDto.setYwid(llglDto.getLlid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		llglDto_t.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		llglDto_t.setFormAction("/storehouse/requisition/copySaveRequisition");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING,BasicDataTypeEnum.DELIVERY_METHOD});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("ysfslist", jclist.get(BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运输方式
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));// 省份
		mav.addObject("cplxlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));// 产品类型
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx",BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		llglDto_t.setXsbj("1");
		llglDto_t.setXzbj(llglDto.getXzbj());
		mav.addObject("llglDto", llglDto_t);
		mav.addObject("copyflg", "1");
		return mav;
	}
	/**
	 * 领料复制保存
     */
	@RequestMapping("/requisition/copySaveRequisition")
	@ResponseBody
	public Map<String,Object> copySaveRequisition(HttpServletRequest request,LlglDto llglDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		llglDto.setLrry(user.getYhid());
		llglDto.setQybj("0");
		llglDto.setLlid(StringUtil.generateUUID());
		boolean isSuccess = true;
		//判断领料单号是否重复
		isSuccess = llglService.getDtoByLldh(llglDto);
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}
		String auditType = AuditTypeEnum.AUDIT_GOODS_APPLY.getCode();
		if (StringUtil.isNotBlank(llglDto.getQueryWlids())){
			String[] queryWlids = llglDto.getQueryWlids().split(",");
			List<String> queryWlidlist = Arrays.asList(queryWlids);
			WlglDto wlglDto = new WlglDto();
			wlglDto.setIds(queryWlidlist);
			List<WlglDto> wlglDtos = wlglService.queryByWllxByWlids(wlglDto);
			if (!CollectionUtils.isEmpty(wlglDtos)){
				for (WlglDto dto : wlglDtos) {
					if ("3".equals(dto.getCskz1())) {
						auditType = AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode();
					}
				}
			}
		}
		//新增保存
		try {
			isSuccess = llglService.addSavePicking(llglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",auditType);
			map.put("ywid", llglDto.getLlid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 生产领料新增页面
	 */
	@RequestMapping("/requisition/produceRequisition")
	public ModelAndView produceRequisition(LlglDto llglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_edit");
		//获取当前用户
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
			llglDto.setJgid(user.getJgid());
			llglDto.setJgdm(user.getJgdm());
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		String jgdh = llglService.getlljgdh(llglDto);//得到机构的扩展参数
		if(StringUtil.isNotBlank(jgdh)) {
			llglDto.setJgdh(jgdh);
			String jlbh = llglService.generateLljlbh(llglDto); //生成记录编号
			llglDto.setJlbh(jlbh);
		}
		llglDto.setLldh(lldh);
		llglDto.setFormAction("/storehouse/requisition/produceSaveRequisition");
		redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode());
		mav.addObject("cklblist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));// 省份
		mav.addObject("xmdllist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
		mav.addObject("cplxlist", JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));// 产品类型
		mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode()));
		mav.addObject("url", "/storehouse/requisition/pagedataPicking");
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBPICKING.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", llglDto);
		return mav;
	}
	/**
	 * 生产料新增保存
     */
	@RequestMapping("/requisition/produceSaveRequisition")
	@ResponseBody
	public Map<String,Object> produceSaveRequisition(HttpServletRequest request,LlglDto llglDto){
		return this.addSaveRequisition(request,llglDto);
	}

	/**
	 * 生产指令选择页面
	 *
     */
	@RequestMapping(value = "/receiveMateriel/pagedataChooseProduce")
	public ModelAndView pagedataChooseProduce() {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveChoose_Produce");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 生产指令选择数据
     */
	@RequestMapping("/requisition/pagedataGetProduce")
	@ResponseBody
	public Map<String,Object> pagedataGetProduce(SczlmxDto sczlmxDto){
		List<SczlmxDto> sczlmxDtos = sczlmxService.getPagedProduce(sczlmxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("rows",sczlmxDtos);
		map.put("total",sczlmxDto.getTotalNumber());
		return map;
	}
	/**
	 * 跳转领料选择列表
	 */
	@RequestMapping(value = "/receiveMateriel/pagedataChooseLlxx")
	public ModelAndView pagedataChooseLlxx() {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_chooseLlxx");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 跳转领料明细选择列表
	 */
	@RequestMapping(value = "/receiveMateriel/pagedataChooseLlmx")
	public ModelAndView pagedataChooseLlmx(LlglDto llglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_chooseLlmx");
		LlglDto dtoById = llglService.getDtoById(llglDto.getLlid());
		HwllmxDto hwllmxDto = new HwllmxDto();
		hwllmxDto.setLlid(llglDto.getLlid());
		List<HwllmxDto> dtoList = hwllmxService.getDtoList(hwllmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llglDto", dtoById);
		mav.addObject("hwllmxDtos", dtoList);
		return mav;
	}
	/**
	 * 领料明细
	 */
	@RequestMapping("/receiveMateriel/pagedataGetHwllmx")
	@ResponseBody
	public Map<String,Object> pagedataGetHwllmx(LlglDto llglDto){
		Map<String, Object> map = new HashMap<>();
		HwllmxDto hwllmxDto = new HwllmxDto();
		if (!CollectionUtils.isEmpty(llglDto.getLlmxids())){
			hwllmxDto.setLlmxids(llglDto.getLlmxids());
		}else {
			hwllmxDto.setLlid(llglDto.getLlid());
		}
		List<HwllmxDto> dtoList = hwllmxService.getDtoList(hwllmxDto);
		map.put("hwllmxDtos", dtoList);
		return map;
	}
}
