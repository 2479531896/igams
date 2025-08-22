package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.LshgzDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.ILshgzService;
import com.matridx.igams.common.service.svcinterface.IXxglService;

@Controller
@RequestMapping("/production")
public class SerialRuleController extends BaseController{
	
	@Autowired
	ILshgzService lshgzService;
	@Autowired
	IXxglService xxglService;
	
	/**
	 * 流水号规则列表
	 */
	@RequestMapping("/serial/pageListSerialRule")
	public ModelAndView pageListSerialRule(){
		return new ModelAndView("production/serial/serialRule_list");
	}
	
	/**
	 * 流水号规则列表明细
	 */
	@RequestMapping("/serial/pageGetListSerialRule")
	@ResponseBody
	public Map<String,Object> listSerialRule(LshgzDto lshgzDto){
		List<LshgzDto> t_List = lshgzService.getPagedDtoList(lshgzDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", lshgzDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 流水号规则查看
	 */
	@RequestMapping(value="/serial/viewSerialRule")
	public ModelAndView viewSerialRule(LshgzDto lshgzDto){
		ModelAndView mav = new ModelAndView("production/serial/serialRule_view");
		LshgzDto t_lshgzDto = lshgzService.getDtoById(lshgzDto.getLshgzid());
		mav.addObject("lshgzDto", t_lshgzDto);
		
		return mav;
	}
	
	/**
	 * 流水号规则新增
	 */
	@RequestMapping(value ="/serial/addSerialRule")
	public ModelAndView addSerialRule(LshgzDto lshgzDto){
		ModelAndView mav = new ModelAndView("production/serial/serialRule_edit");
		lshgzDto.setFormAction("add");
		mav.addObject("lshgzDto", lshgzDto);
		return mav;
	}
	
	/**
	 * 流水号规则新增保存
	 */
	@RequestMapping(value="/serial/addSaveSerialRule")
	@ResponseBody
	public Map<String, Object> addSaveSerialRule(LshgzDto lshgzDto){
		//获取用户信息
		User user = getLoginInfo();
		lshgzDto.setLrry(user.getYhid());
		
		boolean isSuccess = lshgzService.insert(lshgzDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 流水号规则修改
	 */
	@RequestMapping(value="/serial/modSerialRule")
	public ModelAndView modSerialRule(LshgzDto lshgzDto){
		ModelAndView mav = new ModelAndView("production/serial/serialRule_edit");
		
		lshgzDto = lshgzService.getDtoById(lshgzDto.getLshgzid());
		if(lshgzDto == null){
			lshgzDto = new LshgzDto(); 
		}
		lshgzDto.setFormAction("mod");
		mav.addObject("lshgzDto", lshgzDto);
		
		return mav;
	}
	
	/**
	 * 流水号规则修改保存
	 */
	@RequestMapping(value="/serial/modSaveSerialRule")
	@ResponseBody
	public Map<String, Object> modSaveSerialRule(LshgzDto lshgzDto){
		//获取用户信息
		User user = getLoginInfo();
		lshgzDto.setXgry(user.getYhid());
		
		boolean isSuccess = lshgzService.update(lshgzDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除流水号规则信息
	 */
	@RequestMapping(value="/serial/delSerialRule")
	@ResponseBody
	public Map<String, Object> delSerialRule(LshgzDto lshgzDto){
		User user = getLoginInfo();
		lshgzDto.setScry(user.getYhid());
		
		boolean isSuccess = lshgzService.delete(lshgzDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
}
