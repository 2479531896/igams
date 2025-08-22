package com.matridx.igams.warehouse.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.DhjyDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.RkcglDto;
import com.matridx.igams.storehouse.dao.entities.RkglDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDhjyService;
import com.matridx.igams.storehouse.service.svcinterface.IDhxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IRkcglService;
import com.matridx.igams.storehouse.service.svcinterface.IRkglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/warehouse")
public class StockPendingController extends BaseBasicController{
	@Autowired
	IRkglService rkglService;
	@Autowired
	IHwxxService hwxxService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	@Autowired
	ICkxxService ckxxServie;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IRkcglService rkcglService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IQgglService qgglService;
	
	@Autowired
	IHtglService htglService;
	
	@Autowired
	IDhxxService dhxxService;
	
	@Autowired
	IDhjyService dhjyService;
	
	@Autowired
	ISbysService sbysService;
	
	
	
	/**
	 * 跳转待入库列表界面
	 
	 */
	@RequestMapping("/stockPending/pageListStockPending")
	public ModelAndView stockPendingPageList(HttpServletRequest request,RkcglDto rkcglDto) {
		ModelAndView mav = new  ModelAndView("warehouse/stockPending/stockPending_list");
		User user = getLoginInfo(request);
		rkcglDto.setRyid(user.getYhid());
		List<RkcglDto> rkcglDtos = rkcglService.getDtoList(rkcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(rkcglDtos)) {
			for (RkcglDto rkcglDto_t : rkcglDtos) {
				ids.append(",").append(rkcglDto_t.getHwid());
			}
		}
		int rkcsl=rkcglDtos.size();
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.MATERIELQUALITY_TYPE});
		mav.addObject("lblist", jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("rkcsl", Integer.toString(rkcsl));
		mav.addObject("idshw", ids.toString());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;		
	}
	
	/**
	 * 待入库列表
	 
	 */
	@RequestMapping("/stockPending/pageGetListStockPending")
	@ResponseBody
	public Map<String,Object> getStockPendingList(HwxxDto hwxxDto){
		List<HwxxDto> hwxxList = hwxxService.getPagedStockPending(hwxxDto);
		if(!CollectionUtils.isEmpty(hwxxList)) {
			for(HwxxDto dto:hwxxList){
				if("3".equals(dto.getLbcskz1())){
					dto.setJydh(dto.getSbysdh());
				}
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("total",hwxxDto.getTotalNumber());
		map.put("rows",hwxxList);
		return map;
	}
	
	/**
	 * 待入库查看
	 
	 */
	@RequestMapping("/stockPending/viewStockPending")
	public ModelAndView showPutInStorage(HwxxDto hwxxDto) {
		ModelAndView mav = new  ModelAndView("warehouse/stockPending/stockPending_view");
		hwxxDto = hwxxService.getOneByHwid(hwxxDto);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("hwxxDto",hwxxDto);
		return mav;		
	}
	
	/**				
	 * 待检验查看				
	 *
	 */				
	@RequestMapping(value = "/stockPending/viewStock")				
	public ModelAndView viewPendingInspection(HwxxDto hwxxDto) {				
		ModelAndView mav = new ModelAndView("storehouse/inspection/pendingInspection_view");			
		HwxxDto t_hwxxDto = hwxxService.getDtoById(hwxxDto.getHwid());			
		BigDecimal dhsl = new BigDecimal(t_hwxxDto.getDhsl()==null?"0":t_hwxxDto.getDhsl());			
		BigDecimal cythsl = new BigDecimal(t_hwxxDto.getCythsl()==null?"0":t_hwxxDto.getCythsl());			
		String reduce = dhsl.subtract(cythsl).toString();			
		t_hwxxDto.setReduce(reduce);			
		QgglDto qgglDto_t = new QgglDto();			
		if(StringUtil.isNotEmpty(t_hwxxDto.getQgid())) {			
			QgglDto qgglDto = new QgglDto();		
			qgglDto.setQgid(t_hwxxDto.getQgid());		
			qgglDto_t = qgglService.getDto(qgglDto);		
		}			
		HtglDto htglDto = new HtglDto();			
		if(StringUtil.isNotEmpty(t_hwxxDto.getHtid())) {			
			htglDto = htglService.getDtoById(t_hwxxDto.getHtid());		
		}			
		DhxxDto dhxxDto = new DhxxDto();			
		if(StringUtil.isNotEmpty(t_hwxxDto.getDhid())) {			
			dhxxDto = dhxxService.getDtoById(t_hwxxDto.getDhid());		
		}			
		DhjyDto dhjyDto = new DhjyDto();			
		if(StringUtil.isNotEmpty(t_hwxxDto.getDhjyid())) {			
			dhjyDto = dhjyService.getDtoById(t_hwxxDto.getDhjyid());		
		}else{			
			if("3".equals(t_hwxxDto.getLbcskz1())){		
				SbysDto dtoById = sbysService.getDtoById(t_hwxxDto.getHwid());	
				if(dtoById!=null){	
					dhjyDto.setJydh(dtoById.getSbysdh());
					dhjyDto.setJyrq(dtoById.getYsrq());
					dhjyDto.setLrsj(dtoById.getLrry());
					dhjyDto.setDhjyid(t_hwxxDto.getHwid());
				}	
			}		
		}			
		RkglDto rkglDto = new RkglDto();			
		if(StringUtil.isNotEmpty(t_hwxxDto.getRkid())) {			
			rkglDto = rkglService.getDtoById(t_hwxxDto.getRkid());		
		}			
		mav.addObject("htglDto",htglDto);			
		mav.addObject("hwxxDto", t_hwxxDto);			
		mav.addObject("qgglDto",qgglDto_t);			
		mav.addObject("dhxxDto",dhxxDto);			
		mav.addObject("dhjyDto",dhjyDto);			
		mav.addObject("rkglDto",rkglDto);			
		mav.addObject("urlPrefix", urlPrefix);			
		return mav;			
	}				

	
	
	/**
	 * 入库车
	 
	 */
	@RequestMapping("/stockPending/warehousingStockPending")
	public ModelAndView warehousingStockPending(RkglDto rkglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
//		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
//				new BasicDataTypeEnum[] { BasicDataTypeEnum.INBOUND_TYPE, BasicDataTypeEnum.PURCHASE_TYPE});
//		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
//		mav.addObject("cglxlist", jclist.get(BasicDataTypeEnum.PURCHASE_TYPE.getCode()));// 入库类别
		User user = getLoginInfo(request);
		List<RkcglDto> rkcglDtos = rkcglService.getDtoRklb(user.getYhid());
		if(!CollectionUtils.isEmpty(rkcglDtos)) {
			rkglDto.setRklb(rkcglDtos.get(0).getRklb());
			rkglDto.setCglx(rkcglDtos.get(0).getCglx());
			rkglDto.setDhlxdm(rkcglDtos.get(0).getDhlxdm());
		}
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
		String rkdh = rkglService.generatePutInStorageCode(rkglDto);
		rkglDto.setRkdh(rkdh);
		rkglDto.setFormAction("/stockPending/warehousingSaveStockPending");
		rkglDto.setRkrq(sdf.format(date));
		rkglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		mav.addObject("rkglDto",rkglDto);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("xsbj","0");
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("rklbbj", "0");
		mav.addObject("rkcbj", "0");
		mav.addObject("url","/warehouse/stockPending/pagedataGetHwxxList");
		return mav;
	}
	
	/**
	 * 	入库新增保存

	 */
	@ResponseBody
	@RequestMapping(value = "/stockPending/warehousingSaveStockPending")
	public Map<String, Object> addSavePutInStorage(RkglDto rkglDto ,HttpServletRequest request) {
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
	 * 	获取入库车数据

	 */
	@ResponseBody
	@RequestMapping(value = "/stockPending/pagedataGetHwxxList")
	public Map<String, Object> getListByhwid(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		RkcglDto rkcglDto = new RkcglDto();
		User user = getLoginInfo(request);
		rkcglDto.setRyid(user.getYhid());
		List<RkcglDto> rkcglDtos = rkcglService.getDtoList(rkcglDto);
		if(!CollectionUtils.isEmpty(rkcglDtos)) {
			map.put("rows", rkcglDtos);
		}else{
			map.put("rows", null);
		}
		return map;
	}
	
	/**
	 * 加入入库车
	 * @param rkcglDto
	 
	 */
	@ResponseBody
	@RequestMapping(value = "/stockPending/pagedataSaveWarehousing")
	public Map<String, Object> saveWarehousing(RkcglDto rkcglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);		
		List<RkcglDto> rklbs = rkcglService.getDtoRklb(user.getYhid());
		if(!CollectionUtils.isEmpty(rklbs)) {
			for (RkcglDto rklb:rklbs) {
				if (!(StringUtil.isNotBlank(rklb.getRklb())?rklb.getRklb():"").equals(StringUtil.isNotBlank(rkcglDto.getRklb())?rkcglDto.getRklb():"")){
					map.put("status","fail");
					map.put("message","与入库车里的入库类型不符，不能添加！");
					return map;
				}
			}
		}
		
		rkcglDto.setRyid(user.getYhid());
		boolean isSuccess = rkcglService.insert(rkcglDto);
		rkcglDto.setHwid(null);
		List<RkcglDto> rkcglDtos = rkcglService.getDtoList(rkcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(rkcglDtos)) {
			for (RkcglDto rkcglDto_t : rkcglDtos) {
				ids.append(",").append(rkcglDto_t.getHwid());
			}
			ids = new StringBuilder(ids.substring(1));
		}		
		map.put("idshw", ids.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 移除入库车
	 * @param rkcglDto
	 
	 */
	@ResponseBody
	@RequestMapping(value = "/stockPending/pagedataDelWarehousing")
	public Map<String, Object> delWarehousing(RkcglDto rkcglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		rkcglDto.setRyid(user.getYhid());
		boolean isSuccess = rkcglService.delete(rkcglDto);
		rkcglDto.setHwid(null);
		List<RkcglDto> rkcglDtos = rkcglService.getDtoList(rkcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(rkcglDtos)) {
			for (RkcglDto rkcglDto_t : rkcglDtos) {
				ids.append(",").append(rkcglDto_t.getHwid());
			}
			ids = new StringBuilder(ids.substring(1));
		}		
		map.put("idshw", ids.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
