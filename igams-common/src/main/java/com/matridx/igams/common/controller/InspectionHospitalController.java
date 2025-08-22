package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.service.svcinterface.IYyxxService;

@Controller
@RequestMapping("/wechat")
public class InspectionHospitalController extends BaseController{

	@Autowired
	IYyxxService yyxxService;
	/***
	 * 送检录入选择送检单位
	 */
	@RequestMapping("/hospital/pagedataCheckUnitView")
	public ModelAndView checkUnitView(YyxxDto ysxxDto,HttpServletRequest request) {
		String access_token=request.getParameter("access_token");
		ModelAndView mav=new ModelAndView("inspection/hospital/hospital_select");
		mav.addObject("YsxxDto", ysxxDto);
		if(access_token!=null) {
			mav.addObject("access_token", access_token);
		}
		return mav;
	}
	
	/**
	 * 查询列表数据
	 */
	@ResponseBody
	@RequestMapping("/hospital/pagedataListHospital")
	public Map<String,Object> getPageListHospital(YyxxDto yyxxDto){
		Map<String,Object> map=new HashMap<>();
		List<YyxxDto> yyxxList=yyxxService.getPagedDtoList(yyxxDto);
		map.put("total", yyxxDto.getTotalNumber());
		map.put("rows", yyxxList);
		return map;
	}
	/**
	 * 钉钉查询列表数据
	 */
	@ResponseBody
	@RequestMapping("/hospital/minidataListHospital")
	public Map<String,Object> minidataListHospital(YyxxDto yyxxDto){
		return getPageListHospital(yyxxDto);
	}
	
	/**
	 * 预输入查询送检单位
	 */
	@ResponseBody
	@RequestMapping(value = "/hospital/pagedataHospitalName")
	public Map<String, Object> selectHospitalName(YyxxDto yyxxDto){
		List<YyxxDto> yyxxDtos = yyxxService.selectHospitalName(yyxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("yyxxDtos", yyxxDtos);
		return map;
	}
}
