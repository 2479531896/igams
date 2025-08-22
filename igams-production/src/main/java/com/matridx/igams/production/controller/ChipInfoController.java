package com.matridx.igams.production.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.production.dao.entities.XpxxDto;
import com.matridx.igams.production.service.svcinterface.IXpxxService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/chipinfo")
public class ChipInfoController extends BaseController{

	@Autowired
	private IXpxxService xpxxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IXxglService xxglService;

	/**
	 * 增加一个芯片信息的页面模块
	 */
	@RequestMapping("/chipinfo/pageListChipinfo")
	public ModelAndView pageListChipinfo() {
		return new ModelAndView("deviceinfo/chipinfo/chipinfo_list");
	}
	
	/**
	 * 显示芯片维护列表
	 */
	@RequestMapping("/chipinfo/pageGetListChipinfo")
	@ResponseBody
	public Map<String, Object> pageGetListChipinfo(XpxxDto xpxxDto){
		Map<String,Object> map=new HashMap<>();
		List<XpxxDto> xpxxlist = xpxxService.getPagedDtoList(xpxxDto);
		map.put("total",xpxxDto.getTotalNumber());
		map.put("rows",xpxxlist);
		return map;
	}
	
	/**
	 * 查看芯片维护列表
	 */
	@RequestMapping("/chipinfo/viewChipinfo")
	public ModelAndView viewShtxList(XpxxDto xpxxDto){
		ModelAndView mav=new ModelAndView("deviceinfo/chipinfo/chipinfo_view");
		xpxxDto = xpxxService.getDto(xpxxDto);
		mav.addObject("xpxxDto", xpxxDto);
		return mav;
	}

	/**
	 * 新增芯片维护列表
	 */
	@RequestMapping("/chipinfo/addChipinfo")
	public ModelAndView addChipinfo(XpxxDto xpxxDto) {
		ModelAndView mav = new ModelAndView("deviceinfo/chipinfo/chipinfo_edit");
		mav.addObject("cxylist",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SEQUENCER_CODE.getCode()));
		mav.addObject("xpxxDto",xpxxDto);
		mav.addObject("formAction","/chipinfo/chipinfo/addSaveChipinfo");
		return mav;
	}

	/**
	 * 新增保存
	 */
	@RequestMapping("/chipinfo/addSaveChipinfo")
	@ResponseBody
	public Map<String, Object> addSaveChipinfo(XpxxDto xpxxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		xpxxDto.setLrsj(user.getYhid());
		boolean isSuccess = xpxxService.addSaveChipinfo(xpxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改芯片维护列表
	 */
	@RequestMapping("/chipinfo/modChipinfo")
	public ModelAndView modChipinfo(XpxxDto xpxxDto) {
		ModelAndView mav = new ModelAndView("deviceinfo/chipinfo/chipinfo_edit");
		xpxxDto=xpxxService.getDto(xpxxDto);
		mav.addObject("cxylist",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SEQUENCER_CODE.getCode()));
		mav.addObject("xpxxDto",xpxxDto);
		mav.addObject("formAction","/chipinfo/chipinfo/modSaveChipinfo");
		return mav;
	}

	/**
	 * 新增保存
	 */
	@RequestMapping("/chipinfo/modSaveChipinfo")
	@ResponseBody
	public Map<String, Object> modSaveChipinfo(XpxxDto xpxxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		xpxxDto.setXgsj(user.getYhid());
		boolean isSuccess = xpxxService.modSaveChipinfo(xpxxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
}
