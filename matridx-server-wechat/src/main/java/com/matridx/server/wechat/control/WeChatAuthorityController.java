package com.matridx.server.wechat.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.dao.entities.SjqxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.service.svcinterface.ISjqxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;

@Controller
@RequestMapping("/inspection")
public class WeChatAuthorityController extends BaseController{
	
	@Autowired
	ISjqxService sjqxService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IWbcxService wbcxService;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	ISjxxService sjxxService;

	/**
	 * 跳转送检查看权限界面
	 * @return
	 */
	@RequestMapping(value = "/authority/pageListAuthority")
	public ModelAndView pageListAuthority() {
		ModelAndView mav = new ModelAndView("wechat/authority/authority_list");
		List<WbcxDto> wxzghList = wbcxService.getDtoList(null);
		mav.addObject("wxzghList", wxzghList);
		return mav;
	}
	
	/**
	 * 送检权限列表
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value ="/authority/listAuthority")
	@ResponseBody
	public Map<String,Object> listAuthority(SjqxDto sjqxDto){
		List<SjqxDto> t_List = sjqxService.getPagedDtoList(sjqxDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", sjqxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	
	/**
	 * 送检权限列表
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value="/authority/getDtoList")
	@ResponseBody
	public Map<String, Object> getDtoList(SjqxDto sjqxDto){
		Map<String,Object> map = new HashMap<>();
		List<SjqxDto> sjqxDtos = sjqxService.getDtoList(sjqxDto);
		map.put("sjqxDtos", sjqxDtos);
		return map;
	}
	
	/**
	 * 送检单位科室列表
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/authority/getPagedUnitList")
	@ResponseBody
	public Map<String,Object> getPagedUnitList(SjxxDto sjxxDto){
		List<SjxxDto> sjxxDtos = sjxxService.getPagedUnitList(sjxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", sjxxDto.getTotalNumber());
		map.put("rows", sjxxDtos);
		return map;
	}
	
	/**
	 * 送检权限新增
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value ="/authority/addAuthority")
	public ModelAndView addAuthority(SjqxDto sjqxDto){
		ModelAndView mav = new ModelAndView("wechat/authority/authority_edit");
		// 查询微信列表
		List<WxyhDto> wxyhDtos = wxyhService.getListByGzpt(sjqxDto.getWbcxid());
		mav.addObject("wxyhDtos", wxyhDtos);
		sjqxDto.setFormAction("add");
		mav.addObject("sjqxDto", sjqxDto);
		return mav;
	}
	
	/**
	 * 送检权限新增保存
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value="/authority/addSaveAuthority")
	@ResponseBody
	public Map<String, Object> addSaveAudit(SjqxDto sjqxDto){
		boolean isSuccess = sjqxService.addSaveAudit(sjqxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 送检权限修改
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value="/authority/modAuthority")
	public ModelAndView modAuthority(SjqxDto sjqxDto){
		ModelAndView mav = new ModelAndView("wechat/authority/authority_edit");
		// 查询微信列表
		List<WxyhDto> wxyhDtos = wxyhService.getListByGzpt(sjqxDto.getWbcxid());
		mav.addObject("wxyhDtos", wxyhDtos);
		sjqxDto.setFormAction("mod");
		mav.addObject("sjqxDto", sjqxDto);
		return mav;
	}
	
	/**
	 * 送检权限修改保存
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value="/authority/modSaveAuthority")
	@ResponseBody
	public Map<String, Object> modSaveAuthority(SjqxDto sjqxDto){
		boolean isSuccess = sjqxService.addSaveAudit(sjqxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除送检权限信息
	 * @param sjqxDto
	 * @return
	 */
	@RequestMapping(value="/authority/delAuthority")
	@ResponseBody
	public Map<String, Object> delAuthority(SjqxDto sjqxDto){
		boolean isSuccess = sjqxService.delete(sjqxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
}
