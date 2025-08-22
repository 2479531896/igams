package com.matridx.igams.production.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.storehouse.dao.entities.DhjyDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.RkglDto;
import com.matridx.igams.storehouse.service.svcinterface.IDhjyService;
import com.matridx.igams.storehouse.service.svcinterface.IDhxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IRkglService;

@Controller
@RequestMapping("/commonViewPage")
public class CommonViewPageController extends BaseBasicController{
	
	@Autowired
	private IQgmxService qgmxService;

	@Autowired
	private IHtmxService htmxService;
	
	@Autowired
	private IDhxxService dhxxService;
	
	@Autowired
	private IDhjyService dhjyService;
	
	@Autowired
	private IRkglService rkglService;
	
	@Autowired
	private IHwxxService hwxxService;
	
	//@Autowired
	//private IJcsjService jcsjService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	
	/**
	 * 获取货物明细信息 共通页面
	 */
	@ResponseBody
	@RequestMapping(value = "/commonViewPage/viewCommonDetailList")
	public Map<String, Object> viewCommonDetailList(HwxxDto hwxxDto) {
		Map<String,Object> map=new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
		map.put("rows", hwxxDtos);
		return map;
	}

	/**
	 * 多选 获取请购信息 共通页面
	 */
	@ResponseBody
	@RequestMapping(value = "/commonViewPage/viewCommonPurchaseList")
	public Map<String, Object> viewCommonPurchaseList() {
		Map<String,Object> map=new HashMap<>();
		QgmxDto qgmxDto = new QgmxDto();
		qgmxDto.setIds(qgmxDto.getIds());
		List<QgmxDto> qgmxDtos = qgmxService.getCommonDtoListByQgids(qgmxDto);
		map.put("rows", qgmxDtos);
		return map;
	}
	
	/**
	 * 多选 获取合同信息 共通页面
	 */
	@ResponseBody
	@RequestMapping(value = "/commonViewPage/viewCommonContractList")
	public Map<String, Object> viewCommonContractList(HtglDto htglDto) {
		Map<String,Object> map=new HashMap<>();
		HtmxDto htmxDto = new HtmxDto();
		htmxDto.setIds(htglDto.getIds());
		List<HtmxDto> htmxDtos = htmxService.getCommonDtoListByHtmxids(htmxDto);
		map.put("rows", htmxDtos);
		return map;
	}

	/**
	 * 多选 获取到货信息 共通页面
	 */
	@ResponseBody
	@RequestMapping(value = "/commonViewPage/viewCommonArrivalGoodsList")
	public Map<String, Object> viewCommonArrivalGoodsList(HwxxDto hwxxDto) {
		Map<String,Object> map=new HashMap<>();
		DhxxDto dhxxDto = new DhxxDto();
		dhxxDto.setIds(hwxxDto.getIds());
		List<DhxxDto> dhxxDtos = dhxxService.getCommonDtoListByDhids(dhxxDto);
		map.put("rows", dhxxDtos);
		return map;
	}

	/**
	 * 多选 获取到货检验信息 共通页面
	 */
	@ResponseBody
	@RequestMapping(value = "/commonViewPage/viewCommonInspectionGoodsList")
	public Map<String, Object> viewCommonInspectionGoodsList(HwxxDto hwxxDto) {
		Map<String,Object> map=new HashMap<>();
		DhjyDto dhjyDto = new DhjyDto();
		dhjyDto.setIds(hwxxDto.getIds());
		List<DhjyDto> dhjyDtos = dhjyService.getCommonDtoListByDhjyids(dhjyDto);
		map.put("rows", dhjyDtos);
		return map;
	}
	
	/**
	 * 多选 获取入库信息 共通页面
	 */
	@ResponseBody
	@RequestMapping(value = "/commonViewPage/viewCommonputInStorageList")
	public Map<String, Object> viewCommonputInStorageList(HwxxDto hwxxDto) {
		Map<String,Object> map=new HashMap<>();
		RkglDto rkglDto = new RkglDto();
		rkglDto.setIds(hwxxDto.getIds());
		List<RkglDto> rkglDtos = rkglService.getCommonDtoListByRkids(rkglDto);
		map.put("rows", rkglDtos);
		return map;
	}

}
