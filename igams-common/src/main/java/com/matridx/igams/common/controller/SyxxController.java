package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.ISyxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;


@Controller
@RequestMapping("/systemmain")
public class SyxxController extends BaseController{
	
	@Autowired
	private ISyxxService syxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private IXxglService xxglService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 跳转
	 */
	@RequestMapping("/syxx/pageList")
	public ModelAndView PageList()
	{
		ModelAndView mav = new ModelAndView("systemmain/syxx/syxx_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 显示水印信息列表
	 */
	@RequestMapping("/syxx/pageGetList")
	@ResponseBody
	public Map<String, Object> getPagedDtoList(SyxxDto syxxDto){
		Map<String,Object> map=new HashMap<>();
		List<SyxxDto> syxxList = syxxService.getPagedDtoList(syxxDto);
		for(SyxxDto syxxdto : syxxList) {
			String syzContent = syxxdto.getSyz().replaceAll("<", "＜").
					replaceAll(">", "＞").replaceAll("\"","＂");
			syxxdto.setSyz(syzContent);
		}
		map.put("total",syxxDto.getTotalNumber());
		map.put("rows",syxxList);
		return map;
	}
	
	/**
	 * 增加点击新增的页面
	 */
	@RequestMapping("/syxx/addSyxx")
	@ResponseBody
	public ModelAndView addSyxx(SyxxDto syxxDto){
		ModelAndView mav=new ModelAndView("systemmain/syxx/syxx_add");
		syxxDto.setFormAction("addSaveSyxx");
		mav.addObject("syxxDto", syxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("documentTypelist",BusTypeEnum.values());
		return mav;
	}
	
	/**
	    * 增加水印信息到数据库
	 */
	@RequestMapping(value="/syxx/addSaveSyxx")
	@ResponseBody
	public Map<String, Object> InsertSyxx(SyxxDto syxxDto){
		Map<String, Object> map=new HashMap<>();
		boolean isSuccess;
		int num = syxxService.getCount(syxxDto);
		if(num>0) {
			map.put("status", "fail");
			map.put("message", "消息已经存在，请核实后添加！");
		}else if(num==0) {
			isSuccess=syxxService.insertDto(syxxDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 水印信息修改页面
	 */
	@RequestMapping(value="/syxx/modSyxx")
	public ModelAndView modSyxx(SyxxDto syxxDto){
		ModelAndView mav=new ModelAndView("systemmain/syxx/syxx_mod");
		syxxDto = syxxService.getDto(syxxDto);
		syxxDto.setFormAction("modSaveSyxx");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("syxxDto", syxxDto);
		mav.addObject("documentTypelist",BusTypeEnum.values());
		return mav;
	}
	/**
	 * 修改水印信息
	 */
	@RequestMapping(value="/syxx/modSaveSyxx")
	@ResponseBody
	public Map<String,Object> UpdateSyxx(SyxxDto syxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = syxxService.updateDto(syxxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 查看水印信息
	 */
	@RequestMapping(value="/syxx/viewSyxx")
	public ModelAndView ViewSyxx(SyxxDto syxxDto){
		ModelAndView mav=new ModelAndView("systemmain/syxx/syxx_view");
		syxxDto = syxxService.getDto(syxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("syxxDto", syxxDto);
		return mav;
	}
	/**
	 * 删除水印信息
	 */
	@RequestMapping(value="/syxx/delSyxx")
	@ResponseBody
	public Map<String,Object> deleteSyxx(SyxxDto syxxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = syxxService.delete(syxxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
}
