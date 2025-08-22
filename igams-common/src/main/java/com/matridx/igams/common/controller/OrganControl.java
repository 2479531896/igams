package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/systemrole")
public class OrganControl extends BaseController{

	@Autowired
	IJgxxService jgxxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	IXxglService xxglService;

	@Autowired
	IGzglService gzglService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	
	/**
	 * 跳转机构列表页面
	 */
	@RequestMapping(value ="/organ/pageListOrgan")
	@ResponseBody
	public ModelAndView getOrganPage() {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/organ_list");
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 获取机构信息列表
	 */
	@RequestMapping(value="/organ/pageGetListOrgan")
	@ResponseBody
	public Map<String,Object> getOrganPageList(JgxxDto jgxxDto){
		List<JgxxDto> jglist=jgxxService.getPagedJgxxList(jgxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("total",jgxxDto.getTotalNumber());
		map.put("rows",jglist);
		return map;
	}

	/**
	 * 获取机构信息列表
	 */
	@RequestMapping(value="/organ/pagedataGetListOrganUser")
	@ResponseBody
	public Map<String,Object> pagedataGetListOrganUser(){
		GzglDto selectDto=new GzglDto();
		User user=getLoginInfo();
		selectDto.setYhid(user.getYhid());
		GzglDto gzglDto=gzglService.getYhssjgandjgxx(selectDto);
		Map<String, Object> map= new HashMap<>();
		map.put("JgxxDto",gzglDto);
		return map;
	}

	/**
	 * 获取机构信息列表
	 */
	@RequestMapping(value="/organ/pagedataGetListOrgan")
	@ResponseBody
	public Map<String,Object> pagedataGetListOrgan(JgxxDto jgxxDto){
		return getOrganPageList(jgxxDto);
	}
	
	/**
	 * 跳转机构新增页面
	 */
	@RequestMapping(value="/organ/addOrgan")
	@ResponseBody
	public ModelAndView getOrganAddPage(JgxxDto jgxxDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/organ_add");
		List<JgxxDto> jglist=jgxxService.getJgxxList();
		mav.addObject("urlPrefix",urlPrefix);
		jgxxDto.setFormAction("addSaveOrgan");
		mav.addObject("JgxxList", jglist);
		mav.addObject("JgxxDto", jgxxDto);
		return mav;
	}
	
	/**
	 * 新增保存机构信息
	 */
	@RequestMapping(value="/organ/addSaveOrgan")
	@ResponseBody
	public Map<String,Object> addSaveJgxx(JgxxDto jgxxDto, HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		jgxxDto.setLrry(user.getYhid());
		JgxxDto jgxx=jgxxService.getJgxxByJgmc(jgxxDto);
		if(jgxx!=null) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_JG00001").getXxnr());
			return map;
		}else {
			boolean isSuccess=jgxxService.insertDto(jgxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}
		
	}
	
	/**
	 * 跳转修改界面
	 */
	@RequestMapping(value="/organ/modOrgan")
	@ResponseBody
	public ModelAndView getOrganModPage(JgxxDto jgxxDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/organ_add");
		JgxxDto jgxx=jgxxService.selectJgxxByJgid(jgxxDto);
		List<JgxxDto> jglist=jgxxService.getJgxxList();
		jgxx.setFormAction("modSaveOrgan");
		mav.addObject("JgxxList", jglist);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("JgxxDto", jgxx);
		return mav;
	}

	
	/**
	 * 修改保存机构信息
	 */
	@RequestMapping(value="/organ/modSaveOrgan")
	@ResponseBody
	public Map<String,Object> modSaveOrgan(JgxxDto jgxxDto,HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		jgxxDto.setXgry(user.getYhid());
		//校验父机构不能为机构本身
		if(jgxxDto.getFjgid().equals(jgxxDto.getJgid())) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_JG00002").getXxnr());
			return map;
		}else {
			boolean isSuccess = jgxxService.updateJgxx(jgxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}
	}
	
	/**
	 * 跳转查看界面
	 */
	@RequestMapping(value="/organ/viewOrgan")
	@ResponseBody
	public ModelAndView getViewOrganPage(JgxxDto jgxxDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/organ_view");
		List<JgxxDto> jglist=jgxxService.getJgxxList();
		JgxxDto jgxx=jgxxService.selectJgxxByJgid(jgxxDto);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("JgxxDto", jgxx);
		mav.addObject("AllJgxx", jglist);
		return mav;
	}
	
	/**
	 * 删除机构信息
	 */
	@RequestMapping(value="/organ/delOrgan")
	@ResponseBody
	public Map<String,Object> delJgxx(JgxxDto jgxxDto,HttpServletRequest request){
		User user=getLoginInfo(request);
		jgxxDto.setScry(user.getYhid());
		boolean isSuccess=jgxxService.deleteJgxx(jgxxDto);
		Map<String, Object> map=new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 模糊查询机构信息
	 */
	@RequestMapping("/organ/pagedataJgxxByjgmc")
	@ResponseBody
	public Map<String, Object> selsetJgxxByjgmc(JgxxDto jgxxDto){
		Map<String, Object> map=new HashMap<>();
		List<JgxxDto> jgxxList=jgxxService.selsetJgxxByjgmc(jgxxDto);
		map.put("jgxxList",jgxxList);
		map.put("jgxxDto",jgxxDto);
		return map;
	}
	
}
