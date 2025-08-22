package com.matridx.igams.dmp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.dmp.dao.entities.SjkxxDto;
import com.matridx.igams.dmp.dao.entities.ZytgfDto;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.service.svcinterface.ILjfxxService;
import com.matridx.igams.dmp.service.svcinterface.ISjkxxService;
import com.matridx.igams.dmp.service.svcinterface.IZytgfService;
import com.matridx.igams.dmp.service.svcinterface.IZyxxService;

@Controller
@RequestMapping("/dmp")
public class DataPlatController extends BaseController{

	@Autowired
	IZyxxService zyxxService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IZytgfService zytgfService;
	@Autowired
	ISjkxxService sjkxxService;
	@Autowired
	ILjfxxService ljfxxSerivce;
	/**
	 * 数据列表页面
	 * @return
	 */
	@RequestMapping(value ="/data/pageListDataPlat")
	public ModelAndView pageListDataPlat(){
        return new ModelAndView("dmp/data/dataplat_list");
	}
	
	/**
	 * 数据列表
	 * @param zyxxDto
	 * @return
	 */
	@RequestMapping(value ="/data/listDataPlat")
	@ResponseBody
	public Map<String,Object> listDataPlat(ZyxxDto zyxxDto){
		
		List<ZyxxDto> t_List = zyxxService.getPagedDtoList(zyxxDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", zyxxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 数据新增页面
	 * @param zyxxDto
	 * @return
	 */
	@RequestMapping(value ="/data/addDataPlat")
	public ModelAndView addDataPlat(ZyxxDto zyxxDto){
		ModelAndView mav = new ModelAndView("dmp/data/dataplat_edit");
		zyxxDto.setFormAction("add");
		mav.addObject("zyxxDto", zyxxDto);
		//查询资源提供方信息
		List<ZytgfDto> zytgfDtos = zytgfService.getZytgfDtoList();
		mav.addObject("zytgfDtos", zytgfDtos);
		//查询数据库信息
		List<SjkxxDto> sjkxxDtos = sjkxxService.getSjkxxDtoList();
		mav.addObject("sjkxxDtos", sjkxxDtos);
		//获取数据库类型
		List<String> sjklxs = zyxxService.getSjklx();
		mav.addObject("sjklxs", sjklxs);
		//获取调用方式
		List<String> dyfss = zyxxService.getDyfs();
		mav.addObject("dyfss", dyfss);
		//获取请求方式
		List<String> qqfss = zyxxService.getQqfs();
		mav.addObject("qqfss", qqfss);
		return mav;
	}
	
	/**
	 * 数据新增保存
	 * @param zyxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/data/addSaveDataPlat")
	@ResponseBody
	public Map<String, Object> addSaveDataPlat(ZyxxDto zyxxDto){
		//获取用户信息
		User user = getLoginInfo();
		zyxxDto.setLrry(user.getYhid());
		
		boolean isSuccess = zyxxService.addSaveDataPlat(zyxxDto);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 提供方新增页面
	 * @return
	 */
	@RequestMapping(value ="/data/addProvider")
	public ModelAndView addProvider(ZytgfDto zytgfDto){
		ModelAndView mav = new ModelAndView("dmp/data/provider_edit");
		zytgfDto.setFormAction("add");
		mav.addObject("zytgfDto", zytgfDto);
		return mav;
	}
	
	/**
	 * 提供方新增保存
	 * @param zytgfDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/data/addSaveProvider")
	@ResponseBody
	public Map<String, Object> addSaveProvider(ZytgfDto zytgfDto){
		//获取用户信息
		User user = getLoginInfo();
		zytgfDto.setLrry(user.getYhid());
		
		boolean isSuccess = zytgfService.addSaveProvider(zytgfDto);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("zytgfDto", zytgfDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 根据数据库ID查询数据库信息
	 * @param zytgfDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/data/selectSjkxxBySjkid")
	@ResponseBody
	public SjkxxDto selectSjkxxBySjkid(SjkxxDto sjkxxDto){
		return sjkxxService.getDtoById(sjkxxDto.getSjkid());
	}
}
