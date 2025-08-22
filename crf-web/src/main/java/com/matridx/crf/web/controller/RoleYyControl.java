package com.matridx.crf.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.crf.web.dao.entities.JsyyqxDto;
import com.matridx.crf.web.service.svcinterface.IJsyyqxService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.service.svcinterface.IXtjsService;

@Controller
@RequestMapping("/systemcrf")
public class RoleYyControl extends BaseController {
	@Autowired
	IJsyyqxService jsyyqxService;
	@Autowired
	IXtjsService xtjsService;
	@Autowired
	IXxglService xxglService;
	/**
	 * 权限列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/role/pageListRole")
	public ModelAndView pageListRole() {
		ModelAndView mav = new ModelAndView("systemrole/role_yy_list");

		return mav;
	}

	/**
	 * 用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/role/listRole")
	@ResponseBody
	public Map<String, Object> listRole(XtjsDto xtjsDto) {
		List<XtjsDto> t_List = xtjsService.getPagedDtoList(xtjsDto);
		// Json格式的要求{total:22,rows:{}}
		// 构造成Json的格式传递
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", xtjsDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 医院设置页面
	 * 
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping(value = "/role/setYyUnit")
	public ModelAndView setUnit(XtjsDto xtjsDto) {
		ModelAndView mav = new ModelAndView("systemrole/role_yy_configunit");
		mav.addObject("XtjsDto", xtjsDto);
		return mav;
	}

	/**
	 * 获取医院列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/configunit/listUnSelectYyxx")
	@ResponseBody
	public Map<String, Object> getJgxx(JsyyqxDto jsyyqxDto) {
		List<JsyyqxDto> otherjgxx = jsyyqxService.getPagedOtherYyxxList(jsyyqxDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", jsyyqxDto.getTotalNumber());
		map.put("rows", otherjgxx);
		return map;
	}

	/**
	 * 获取已选机构列表
	 * 
	 * @param jgxxDto
	 * @return
	 */
	@RequestMapping(value = "/configunit/listSelectYyxx")
	@ResponseBody
	public Map<String, Object> getYxJgxx(JsyyqxDto jsyyqxDto) {
		List<JsyyqxDto> otheryyxx = jsyyqxService.getPagedYxYyxxList(jsyyqxDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", jsyyqxDto.getTotalNumber());
		map.put("rows", otheryyxx);
		return map;
	}

	/**
	 * 添加医院
	 * 
	 * @return
	 */
	@RequestMapping(value = "/configunit/toSelectedYy")
	@ResponseBody
	public Map<String, Object> toSelectedYy(JsyyqxDto jsyyqxDto) {
		try {
			boolean result = jsyyqxService.toSelectedYy(jsyyqxDto);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", result ? "success" : "fail");
			map.put("message", result ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}

	/**
	 * 去除医院
	 * 
	 * @param yhjsDto
	 * @return
	 */
	@RequestMapping(value = "/configunit/toOptionalYy")
	@ResponseBody
	public Map<String, Object> toOptionalYy(JsyyqxDto jsyyqxDto) {
		try {
			boolean result = jsyyqxService.toOptionalYy(jsyyqxDto);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", result ? "success" : "fail");
			map.put("message", result ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}

}
