package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.LbcxszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.ILbcxszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;


@Controller
@RequestMapping("/systemmain")
public class ListSetController extends BaseController{
	
	@Autowired
	ILbcxszService lbcxszService;
	@Autowired
	IXxglService xxglService;
	
	/**
	 * 列表查询设置
	 * @author asus
	 */
	@RequestMapping("/list/pageListSet")
	public ModelAndView pageListAudit(){
		return new ModelAndView("systemmain/listset/listSet_list");
	}
	/**
	 * 列表查询设置明细
	 */
	@RequestMapping("/list/pageGetListSet")
	@ResponseBody
	public Map<String,Object> listSet(LbcxszDto lbcxszDto){
		List<LbcxszDto> t_List = lbcxszService.getPagedDtoList(lbcxszDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", lbcxszDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 列表查询设置查看
	 */
	@RequestMapping(value="/list/viewListSet")
	public ModelAndView viewAudit(LbcxszDto lbcxszDto){
		ModelAndView mav = new ModelAndView("systemmain/listset/listSet_view");
		LbcxszDto t_lbcxszDto = lbcxszService.getDtoById(lbcxszDto.getYwid());
		mav.addObject("lbcxszDto", t_lbcxszDto);
		
		return mav;
	}
	
	/**
	 * 列表查询设置新增
	 */
	@RequestMapping(value ="/list/addListSet")
	public ModelAndView addAudit(LbcxszDto lbcxszDto){
		ModelAndView mav = new ModelAndView("systemmain/listset/listSet_edit");
		List<LbcxszDto> lbcxszDtos = lbcxszService.getAllCzdmAndChecked(lbcxszDto);
		lbcxszDto.setFormAction("add");
		lbcxszDto.setPreywid(lbcxszDto.getYwid());
		lbcxszDto.setPrezyid(lbcxszDto.getZyid());
		mav.addObject("lbcxszDto", lbcxszDto);
		mav.addObject("lbcxszDtos", lbcxszDtos);
		return mav;
	}
	
	/**
	 * 列表查询设置新增保存
	 */
	@RequestMapping(value="/list/addSaveListSet")
	@ResponseBody
	public Map<String, Object> addSaveListSet(LbcxszDto lbcxszDto){
		//获取用户信息
		User user = getLoginInfo();
		lbcxszDto.setLrry(user.getYhid());
		
		boolean isSuccess = lbcxszService.insertDto(lbcxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 列表查询设置修改
	 */
	@RequestMapping(value="/list/modListSet")
	public ModelAndView modListSet(LbcxszDto lbcxszDto){
		ModelAndView mav = new ModelAndView("systemmain/listset/listSet_edit");
		List<LbcxszDto> lbcxszDtos = lbcxszService.getAllCzdmAndChecked(lbcxszDto);
		mav.addObject("lbcxszDtos", lbcxszDtos);
		
		lbcxszDto = lbcxszService.getDtoById(lbcxszDto.getYwid());
		if(lbcxszDto == null){
			lbcxszDto = new LbcxszDto(); 
		}
		lbcxszDto.setPreywid(lbcxszDto.getYwid());
		lbcxszDto.setPrezyid(lbcxszDto.getZyid());
		lbcxszDto.setFormAction("mod");
		mav.addObject("lbcxszDto", lbcxszDto);
		
		return mav;
	}
	
	/**
	 * 列表查询设置修改保存
	 */
	@RequestMapping(value="/list/modSaveListSet")
	@ResponseBody
	public Map<String, Object> modSaveListSet(LbcxszDto lbcxszDto){
		//获取用户信息
		User user = getLoginInfo();
		lbcxszDto.setXgry(user.getYhid());
		boolean isSuccess = lbcxszService.updateDto(lbcxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除列表
	 */
	@RequestMapping(value="/list/delListSet")
	@ResponseBody
	public Map<String, Object> delListSet(LbcxszDto lbcxszDto){
		User user = getLoginInfo();
		lbcxszDto.setScry(user.getYhid());
		lbcxszDto.setScbj("1");
		boolean isSuccess = lbcxszService.deleteDto(lbcxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
}
