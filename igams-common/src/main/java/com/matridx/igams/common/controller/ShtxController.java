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
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.ShtxDto;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.igams.common.service.svcinterface.IShtxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/systemmain")
public class ShtxController extends BaseController{
	@Autowired
	private IShtxService shtxService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IShlbService shlbService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	/**
	 * 增加一个审核提醒的页面模块
	 */
	@RequestMapping("/audit/pageListShtx")
	public ModelAndView pageMessage() {
		ModelAndView mav = new ModelAndView("common/shtx/shtx_list");
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 显示审核提醒列表
	 */
	@RequestMapping("/audit/pageGetListShtxData")
	@ResponseBody
	public Map<String, Object> getShtxList(ShtxDto shtxDto){
		Map<String,Object> map=new HashMap<>();
		List<ShtxDto> shtxlist = shtxService.getPagedDtoList(shtxDto);
		map.put("total",shtxDto.getTotalNumber());
		map.put("rows",shtxlist);
		return map;
	}
	
	/**
	 * 查看审核提醒详情
	 */
	@RequestMapping("/audit/viewShtx")
	@ResponseBody
	public ModelAndView viewShtxList(ShtxDto shtxDto){
		ModelAndView mav=new ModelAndView("common/shtx/shtx_view");
		shtxDto = shtxService.getDto(shtxDto);
		mav.addObject("shtxDto", shtxDto);
		return mav;
	}
	
	/**
	 * 点击新增审核提醒的页面
	 */
	@RequestMapping("/audit/addShtx")
	@ResponseBody
	public ModelAndView addShtxList(ShtxDto shtxDto){
		ModelAndView mav=new ModelAndView("common/shtx/shtx_add");
		shtxDto.setFormAction("addSaveShtx");
		List<ShlbDto> shlbList = shlbService.getShlbAllData();
		mav.addObject("shtxDto", shtxDto);
		mav.addObject("shlbList",shlbList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	
	/**
	    * 增加信息到数据库
	 */
    @RequestMapping("/audit/addSaveShtx")
	@ResponseBody
    public Map<String, Object> addSaveShtx(ShtxDto shtxDto){
    	Map<String, Object> map=new HashMap<>();
    	boolean isSuccess;
    	int num = shtxService.getCount(shtxDto);
    	if(num>0) {
    		map.put("status", "fail");
    		map.put("message", "消息已经存在，请核实后添加！");
    	}else if(num==0) {
    		shtxDto.setTxid(StringUtil.generateUUID());
    		isSuccess=shtxService.insertshtx(shtxDto);
        	map.put("status",isSuccess?"success":"fail");
    		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
    	}
		return map;
    }


	/**
	 * 修改时新增一个页面
	 */
	@RequestMapping("/audit/modShtx")
	@ResponseBody
	public ModelAndView modShtxList(ShtxDto shtxDto){
		ModelAndView mav=new ModelAndView("common/shtx/shtx_add");
		shtxDto = shtxService.getDto(shtxDto);
		shtxDto.setFormAction("modSaveShtx");
		List<ShlbDto> shlbList = shlbService.getShlbAllData();
		mav.addObject("shtxDto", shtxDto);
		mav.addObject("shlbList",shlbList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改消息到数据库
	 */
	@RequestMapping("/audit/modSaveShtx")
	@ResponseBody
	public Map<String,Object> modSaveShtx(ShtxDto shtxDto){
		Map<String,Object> map = new HashMap<>();
        boolean isSuccess;
		isSuccess=shtxService.updateDto(shtxDto);
    	map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	
	/**
	 * 删除消息
	 */
	@RequestMapping("/audit/delShtx")
	@ResponseBody
	public Map<String,Object> deleteShtxList(ShtxDto shtxDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = shtxService.delete(shtxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	

}
