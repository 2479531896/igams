package com.matridx.igams.storehouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;
import com.matridx.igams.storehouse.service.svcinterface.IFhglService;
import com.matridx.igams.storehouse.service.svcinterface.IFhmxService;

@Controller
@RequestMapping("/storehouse")
public class DeliverGoodsController extends BaseBasicController{
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Autowired
	IFhglService fhglService;
	
	@Autowired
	IFhmxService fhmxService;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	/**
	 * 选择发货单页面
	 * @param fhglDto
	 * @return
	 */
	@RequestMapping("/storehouse/pagedataChooseListDeliverGoods")
	public ModelAndView chooseListDeliverGoods(FhglDto fhglDto) {
		ModelAndView mav=new ModelAndView("storehouse/dhxx/deliver_chooseList");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("fhglDto", fhglDto);
		return mav;
	}

	/**
	 * 获取发货列表数据
	 * @param fhglDto
	 * @return
	 */
	@RequestMapping(value = "/storehouse/pagedataGetPageListAlldeliver")
	@ResponseBody
	public Map<String,Object> getPageListAlldeliver(FhglDto fhglDto) {
		Map<String,Object> map=new HashMap<>();
		List<FhglDto> list=fhglService.getPagedDtoKThList(fhglDto);
		map.put("total", fhglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 选择发货明细页面
	 * @param fhmxDto
	 * @return
	 */
	@RequestMapping("/storehouse/pagedataChooseListDeliverGoodsInfo")
	public ModelAndView chooseListDeliverGoodsInfo(FhmxDto fhmxDto) {
		ModelAndView mav=new ModelAndView("storehouse/dhxx/deliver_chooseInfoList");
		//User user = getLoginInfo(request);
		FhglDto fhglDto = fhglService.getDtoById(fhmxDto.getFhid());
		//fhmxDto.setIds(user.getCkqx());
		List<FhmxDto> fhmxList = fhmxService.getFhmxList(fhmxDto);
		mav.addObject("fhglDto", fhglDto);
		mav.addObject("fhmxDtos", fhmxList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取发货明细信息
	 * @param fhmxDto
	 * @return
	 */
	@RequestMapping("/storehouse/pagedataGetdeliverInfo")
	@ResponseBody
	public Map<String,Object> getdeliverInfo(FhmxDto fhmxDto){
		Map<String,Object> map = new HashMap<>();
		//User user = getLoginInfo(request);
		//fhmxDto.setIds(user.getCkqx());
		List<FhmxDto> fhmxList = fhmxService.getFhmxList(fhmxDto);		
		map.put("fhmxList", fhmxList);
		return map;
	}

	/**
	 * 获取发货明细列表数据用于异常
	 * @param fhmxDto
	 * @return
	 */
	@RequestMapping(value = "/storehouse/pagedataListForException")
	@ResponseBody
	public Map<String,Object> pagedataListForException(FhmxDto fhmxDto) {
		Map<String,Object> map=new HashMap<>();
		List<FhmxDto> list=fhmxService.getPagedForException(fhmxDto);
		map.put("total", fhmxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
}
