package com.matridx.las.home.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.las.netty.dao.entities.WksyypDto;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/lashome")
public class CodeEntryController {

	
	@Autowired
	IXxglService xxglService;
	/**
	 * 跳转内部编码录入界面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/codeEntry/codeEntry")
	public ModelAndView codeEntry() {
		ModelAndView mav = new ModelAndView("lashome/codeEntry/codeEntry_interface");
		return mav;
	}
	
	/**
	 * 内部编号保存
	 * @param
	 * @return
	 */
	@RequestMapping("/codeEntry/codeEntrySave")
	@ResponseBody
	public Map<String, Object> codeEntrySave(WksyypDto wksyypDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			boolean isSuccess = false;//wksyypService.numberEntry(wksyypDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			//map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} 
		
		return map;
	}
}
